/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Ligth-Weight HTTP Server

    File        :   Server.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Server.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.http;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import java.util.zip.GZIPOutputStream;
import java.util.zip.DeflaterOutputStream;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import java.io.File;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.InterruptedIOException;

import java.net.URI;
import java.net.URL;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

import javax.net.ServerSocketFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocketFactory;

////////////////////////////////////////////////////////////////////////////////
// class Server
// ~~~~~ ~~~~~~
/**
 ** The <code>Server</code> class implements a light-weight HTTP server.
 ** <p>
 ** This server implements all functionality required by RFC 2616 ("Hypertext
 ** Transfer Protocol -- HTTP/1.1"), as well as some of the optional
 ** functionality (this is termed "conditionally compliant" in the RFC). In
 ** fact, a couple of bugs in the RFC itself were discovered (and fixed) during
 ** the development of this server.
 ** <p>
 ** <b>Feature Overview</b>
 ** <ul>
 **   <li>RFC compliant - correctness is not sacrificed for the sake of size
 **   <li>Virtual hosts - multiple domains and subdomains per server
 **   <li>File serving - built-in handler to serve files and folders from disk
 **   <li>Mime type mappings - configurable via API or a standard mime.types file
 **   <li>Directory index generation - enables browsing folder contents
 **   <li>Welcome files - configurable default filename (e.g. index.html)
 **   <li>All HTTP methods supported - GET/HEAD/OPTIONS/TRACE/POST/PUT/DELETE/custom
 **   <li>Conditional statuses - ETags and If-* header support</li>
 **   <li>Chunked transfer encoding - for serving dynamically-generated data streams
 **   <li>Gzip/deflate compression - reduces bandwidth and download time
 **   <li>HTTPS - secures all server communications
 **   <li>Partial content - download continuation (a.k.a. byte range serving)
 **   <li>File upload - multipart/form-data handling as stream or iterator
 **   <li>Multiple context handlers - a different handler method per URL path
 **   <li>@Context annotations - auto-detection of context handler methods
 **   <li>Parameter parsing - from query string or x-www-form-urlencoded body
 **   <li>A single source file - super-easy to integrate into any application
 **   <li>Standalone - no dependencies other than the Java runtime
 **   <li>Small footprint - standard jar is ~50K, stripped jar is ~35K
 **   <li>Extensible design - easy to override, add or remove functionality
 **   <li>Reusable utility methods to simplify your custom code
 **   <li>Extensive documentation of API and implementation (&gt;40% of source lines)
 ** </ul>
 ** <p>
 ** <b>Use Cases</b>
 ** <p>
 ** Being a lightweight, standalone, easily embeddable and tiny-footprint
 ** server, it is well-suited for
 ** <ul>
 **   <li>Resource-constrained environments such as embedded devices.
 **       For really extreme constraints, you can easily remove unneeded
 **       functionality to make it even smaller (and use the -Dstripped
 **       maven build option to strip away debug info, license, etc.)
 **   <li>Unit and integration tests - fast setup/teardown times, small overhead
 **       and simple context handler setup make it a great web server for
 **       testing client components under various server response conditions.
 **   <li>Embedding a web console into any headless application for
 **       administration, monitoring, or a full portable GUI.
 **   <li>A full-fledged standalone web server serving static files,
 **       dynamically-generated content, REST APIs, pseudo-streaming, etc.
 **   <li>A good reference for learning how HTTP works under the hood.
 ** </ul>
 ** <p>
 ** <b>Implementation Notes</b>
 ** <p>
 ** The design and implementation of this server attempt to balance correctness,
 ** compliance, readability, size, features, extensibility and performance, and
 ** often prioritize them in this order, but some trade-offs must be made.
 ** <p>
 ** This server is multithreaded in its support for multiple concurrent HTTP
 ** connections, however most of its constituent classes are not thread-safe and
 ** require external synchronization if accessed by multiple threads
 ** concurrently.
 ** <p>
 ** <b>Source Structure and Documentation</b>
 ** <p>
 ** This server is intentionally written as a single source file, in order to
 ** make it as easy as possible to integrate into any existing project - by
 ** simply adding this single file to the project sources. It does, however, aim
 ** to maintain a structured and flexible design. There are no external package
 ** dependencies.
 ** <p>
 ** This file contains extensive documentation of its classes and methods, as
 ** well as implementation details and references to specific RFC sections which
 ** clarify the logic behind the code. It is recommended that anyone attempting
 ** to modify the protocol-level functionality become acquainted with the RFC,
 ** in order to make sure that protocol compliance is not broken.
 ** <p>
 ** <b>Getting Started</b>
 ** <p>
 ** For an example and a good starting point for learning how to use the API,
 ** see the {@link #main main} method at the bottom of the file, and follow the
 ** code into the API from there. Alternatively, you can just browse through the
 ** classes and utility methods and read their documentation and code.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Server {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** A convenience array containing the carriage-return and line feed chars. */
  private static final byte[]              CRLF        = {0x0d, 0x0a};

  /** The HTTP status description strings. */
  private static final String[]            STATUS   = new String[600];

  /**
   ** A mapping of path suffixes (e.g. file extensions) to their corresponding
   ** MIME types.
   */
  private static final Map<String, String> MIME     = new ConcurrentHashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    // initialize status descriptions lookup table
    Arrays.fill(STATUS, "Unknown Status");
    STATUS[100] = "Continue";
    STATUS[200] = "OK";
    STATUS[204] = "No Content";
    STATUS[206] = "Partial Content";
    STATUS[301] = "Moved Permanently";
    STATUS[302] = "Found";
    STATUS[304] = "Not Modified";
    STATUS[307] = "Temporary Redirect";
    STATUS[400] = "Bad Request";
    STATUS[401] = "Unauthorized";
    STATUS[403] = "Forbidden";
    STATUS[404] = "Not Found";
    STATUS[405] = "Method Not Allowed";
    STATUS[408] = "Request Timeout";
    STATUS[412] = "Precondition Failed";
    STATUS[413] = "Request Entity Too Large";
    STATUS[414] = "Request-URI Too Large";
    STATUS[416] = "Requested Range Not Satisfiable";
    STATUS[417] = "Expectation Failed";
    STATUS[500] = "Internal Server Error";
    STATUS[501] = "Not Implemented";
    STATUS[502] = "Bad Gateway";
    STATUS[503] = "Service Unavailable";
    STATUS[504] = "Gateway Time-out";

    // add some default common content types
    // see http://www.iana.org/assignments/media-types/ for full list
    contentType("application/font-woff", "woff");
    contentType("application/font-woff2", "woff2");
    contentType("application/java-archive", "jar");
    contentType("application/javascript", "js");
    contentType("application/json", "json");
    contentType("application/octet-stream", "exe");
    contentType("application/pdf", "pdf");
    contentType("application/x-7z-compressed", "7z");
    contentType("application/x-compressed", "tgz");
    contentType("application/x-gzip", "gz");
    contentType("application/x-tar", "tar");
    contentType("application/xhtml+xml", "xhtml");
    contentType("application/zip", "zip");
    contentType("audio/mpeg", "mp3");
    contentType("image/gif", "gif");
    contentType("image/jpeg", "jpg", "jpeg");
    contentType("image/png", "png");
    contentType("image/svg+xml", "svg");
    contentType("image/x-icon", "ico");
    contentType("text/css", "css");
    contentType("text/csv", "csv");
    contentType("text/html; charset=utf-8", "htm", "html");
    contentType("text/plain", "txt", "text", "log");
    contentType("text/xml", "xml");
  }

  //////////////////////////////////////////////////////////////////////////////
  // intstance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected volatile int                   port;
  protected volatile boolean               secure;
  protected volatile int                   timeout = 10000;
  protected volatile ServerSocketFactory   socketFactory;
  protected volatile Executor              executor;
  protected volatile ServerSocket          instance;
  protected final Map<String, VirtualHost> hosts = new ConcurrentHashMap<String, VirtualHost>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Header
  // ~~~~~ ~~~~~~
  /**
   ** The <code>Header</code> class encapsulates a single HTTP header.
   */
  public static class Header {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final String name;
    final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Header</code> with the given <code>name</code> and
     ** <code>value</code>.
     ** <br>
     ** Leading and trailing whitespace are trimmed.
     **
     ** @param  name             the header name.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the header value.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public Header(final String name, final String value) {
      // ensure inheritance
      super();

      // prevent bogus input
      // RFC2616#14.23
      // header can have an empty value (e.g. Host)  but name cannot be empty
      if (name.length() == 0)
        throw new IllegalArgumentException("Name cannot be empty");

      // initialize instance attributes
      this.name  = name.trim();
      this.value = value.trim();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Headers
  // ~~~~~ ~~~~~~~
  /**
   ** The <code>Headers</code> class encapsulates a collection of HTTP headers.
   ** <p>
   ** Header names are treated case-insensitively, although this class retains
   ** their original case. Header insertion order is maintained as well.
   */
  public static class Headers implements Iterable<Header> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected int      count;
    // due to the requirements of case-insensitive name comparisons, retaining
    // the original case, and retaining header insertion order, and due to the
    // fact that the number of headers is generally quite small (usually under
    // 12 headers), we use a simple array with linear access times, which proves
    // to be more efficient and straightforward than the alternatives
    protected Header[] headers = new Header[12];

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Headers</code> collection that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Headers() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   size
    /**
     ** Returns the number of headers in this collection.
     **
     ** @return                  the number of headers in this collection.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int size() {
      return this.count;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   contains
    /**
     ** Returns whether there exists a header with the given name.
     **
     ** @param  name             the header name (case insensitive).
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  whether there exists a header with the given
     **                          name.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean contains(final String name) {
      return get(name) != null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   getDate
    /**
     ** Returns the {@link Date} value of the header with the given name.
     **
     ** @param  name             the header name (case insensitive).
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the header value as a {@link Date}, or
     **                          <code>null</code> if none exists or if the
     **                          value is not in any supported date format.
     **                          <br>
     **                          Possible object is {@link Date}.
     */
    public Date getDate(final String name) {
      try {
        String header = get(name);
        return header == null ? null : Support.parseDate(header);
      }
      catch (IllegalArgumentException iae) {
        return null;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   get
    /**
     ** Returns the value of the first header with the given name.
     **
     ** @param  name             the header name (case insensitive).
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the header value, or <code>null</code> if none
     **                          exists.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String get(final String name) {
      for (int i = 0; i < count; i++)
        if (this.headers[i].name.equalsIgnoreCase(name))
          return this.headers[i].value;

      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   parameter
    /**
     ** Returns a header's value.
     ** <br>
     ** Parameter order is maintained, and the first key (in iteration order) is
     ** the header's value without the parameters.
     **
     ** @param  name             the header name (case insensitive).
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the header's parameter names and values.
     **                          <br>
     **                          Possible object is {@link Map}.
     */
    public Map<String, String> parameter(final String name) {
      Map<String, String> params = new LinkedHashMap<String, String>();
      for (String param : Support.split(get(name), ";", -1)) {
        String[] pair  = Support.split(param, "=", 2);
        String   value = pair.length == 1 ? "" : Support.trimLeft(Support.trimRight(pair[1], '"'), '"');
        params.put(pair[0], value);
      }
      return params;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   iterator (Iterable)
    /**
     ** Returns an iterator over the headers, in their insertion order.
     ** <br>
     ** If the headers collection is modified during iteration, the iteration
     ** result is undefined.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The remove operation is unsupported.
     **
     ** @return                  an {@link Iterator} over the headers
     **                          <br>
     **                          Possible object is {@link Iterator} where each
     **                          element is of type {@link Header}.
     */
    public Iterator<Header> iterator() {
      // we use the built-in wrapper instead of a trivial custom implementation
      // since even a tiny anonymous class here compiles to a 1.5K class file
      return Arrays.asList(this.headers).subList(0, this.count).iterator();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   add
    /**
     ** Adds a header with the given name and value to the end of this
     ** collection of headers.
     ** <br>
     ** Leading and trailing whitespace are trimmed.
     **
     ** @param  name             the header name (case insensitive).
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the header value.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public void add(final String name, final String value) {
      // also validates
      final Header header = new Header(name, value);
      // expand array if necessary
      if (this.count == this.headers.length) {
        final Header[] expanded = new Header[2 * count];
        System.arraycopy(this.headers, 0, expanded, 0, count);
        this.headers = expanded;
      }
      // inlining header would cause a bug!
      this.headers[count++] = header;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   add
    /**
     ** Adds all given headers to the end of this collection of headers, in
     ** their original order.
     **
     ** @param  headers          the headers to add.
     **                          <br>
     **                          Allowed object is <code>Headers</code>.
     */
    public void addAll(final Headers headers) {
      for (Header header : headers)
        add(header.name, header.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   replace
    /**
     ** Adds a header with the given name and value, replacing the first
     ** existing header with the same name. If there is no existing header with
     ** the same name, it is added as in {@link #add}.
     **
     ** @param  name             the header name (case insensitive).
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the header value.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the replaced header, or <code>null</code> if
     **                          none exists.
     **                          <br>
     **                          Possible object is {@link Header}.
     */
    public Header replace(final String name, final String value) {
      for (int i = 0; i < count; i++) {
        if (this.headers[i].name.equalsIgnoreCase(name)) {
          final Header prev = this.headers[i];
          this.headers[i] = new Header(name, value);
          return prev;
        }
      }
      add(name, value);
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   remove
    /**
     ** Removes all headers with the given name (if any exist).
     **
     ** @param  name             the header name (case insensitive).
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public void remove(final String name) {
      int j = 0;
      for (int i = 0; i < this.count; i++)
        if (!this.headers[i].name.equalsIgnoreCase(name))
          this.headers[j++] = this.headers[i];
      while (this.count > j)
        this.headers[--this.count] = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   writeTo
    /**
     ** Writes the headers to the given stream (including trailing
     ** <code>CRLF</code>).
     **
     ** @param  stream           the stream to write the headers to.
     **                          <br>
     **                          Allowed object is {@link OutputStream}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public void writeTo(final OutputStream stream)
      throws IOException {

      for (int i = 0; i < this.count; i++) {
        stream.write(Support.bytes(this.headers[i].name, ": ", this.headers[i].value));
        stream.write(CRLF);
      }
      // ends header block
      stream.write(CRLF);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class LimitedInputStream
  // ~~~~~ ~~~~~~~~~~~~~~~~~~
  /**
   ** The <code>LimitedInputStream</code> provides access to a limited number of
   ** consecutive bytes from the underlying InputStream, starting at its current
   ** position. If this limit is reached, it behaves as though the end of stream
   ** has been reached (although the underlying stream remains open and may
   ** contain additional data).
   */
  public static class LimitedInputStream extends FilterInputStream {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // decremented when read, until it reaches zero
    protected long    limit;
    protected boolean premature;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>LimitedInputStream</code> with the given
     ** underlying input stream and limit.
     **
     ** @param  stream           the underlying input stream.
     **                          <br>
     **                          Allowed object is {@link InputStream}.
     ** @param  limit            the maximum number of bytes that may be
     **                          consumed from the underlying stream before this
     **                          stream ends. If zero or negative, this stream
     **                          will be at its end from initialization.
     **                          <br>
     **                          Allowed object is <code>long</code>}.
     ** @param  premature        specifies the stream's behavior when the
     **                          underlying stream end is reached before the
     **                          limit is reached: if <code>true</code>, an
     **                          exception is thrown, otherwise this stream
     **                          reaches its end as well (i.e. read() returns
     **                          <code>-1</code>)
     **                          <br>
     **                          Allowed object is <code>boolean</code>}.
     */
    public LimitedInputStream(final InputStream stream, final long limit, final boolean premature) {
      // ensure inheritance
      super(stream);

      // prevent bogus input
      if (stream == null)
        throw new NullPointerException("Input stream is null");

      // initialize instance attributes
      this.limit     = limit < 0 ? 0 : limit;
      this.premature = premature;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: available (overridden)
    /**
     ** Tests if this input stream supports the <code>mark</code> and
     ** <code>reset</code> methods.
     **
     ** @return                  <code>true</code> if this stream type supports
     **                          the <code>mark</code> and <code>reset</code>
     **                          method; <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean markSupported() {
      return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: available (overridden)
    /**
     ** Returns an estimate of the number of bytes that can be read (or skipped
     ** over) from this input stream without blocking by the next caller of a
     ** method for this input stream. The next caller might be the same thread
     ** or another thread. A single read or skip of this many bytes will not
     ** block, but may read or skip fewer bytes.
     ** <p>
     ** This method returns the result of {@link #in this.in}.available().
     **
     ** @return                  an estimate of the number of bytes that can be
     **                          read (or skipped over) from this input stream
     **                          without blocking.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public final int available()
      throws IOException {

      int res = this.in.available();
      return res > this.limit ? (int)this.limit : res;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: skip (overridden)
    /**
     ** Skips over and discards <code>length</code> bytes of data from the input
     ** stream. The <code>skip</code> method may, for a variety of reasons, end
     ** up skipping over some smaller number of bytes, possibly <code>0</code>.
     ** The actual number of bytes skipped is returned.
     ** <p>
     ** This method simply performs <code>this.in.skip(n)</code>.
     **
     ** @param  length           the number of bytes to be skipped.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     **
     ** @return                  the actual number of bytes skipped.
     **                          <br>
     **                          Possible object is <code>long</code>.
     **
     ** @throws IOException      if the stream does not support seek, or if
     **                          some other I/O error occurs.
     */
    @Override
    public final long skip(final long length)
      throws IOException {

      long res = this.in.skip(length > this.limit ? this.limit : length);
      this.limit -= res;
      return res;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (overridden)
    /**
     ** Reads the next byte of data from this input stream. The value byte is
     ** returned as an <code>int</code> in the range <code>0</code> to
     ** <code>255</code>. If no byte is available because the end of the stream
     ** has been reached, the value <code>-1</code> is returned. This method
     ** blocks until input data is available, the end of the stream is detected,
     ** or an exception is thrown.
     ** <p>
     ** This method simply performs <code>stream.read()</code> and returns the
     ** result.
     **
     ** @return                  the next byte of data, or <code>-1</code> if
     **                          the end of the stream is reached.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     **
     ** @see    java.io.FilterInputStream#in
     */
    @Override
    public int read()
      throws IOException {

      int res = this.limit == 0 ? -1 : this.in.read();
      if (res < 0 && limit > 0 && this.premature)
        throw new IOException("Unexpected end of stream");

      this.limit = res < 0 ? 0 : this.limit - 1;
      return res;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (overridden)
    /**
     ** Reads up to <code>length</code> bytes of data from this input stream
     ** into an array of bytes. If <code>length</code> is not zero, the method
     ** blocks until some input is available; otherwise, no bytes are read and
     ** <code>0</code> is returned.
     ** <p>
     ** This method simply performs <code>this.in.read(b, off, len)</code> and
     ** returns the result.
     **
     ** @param  buffer           the byte buffer into which the data is read.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     ** @param  offset           the start offset in the destination array <code>b</code>
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  length           the maximum number of bytes read.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the total number of bytes read into the buffer,
     **                          or <code>-1</code> if there is no more data
     **                          because the end of the stream has been reached.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws NullPointerException      if <code>buffer</code> is
     **                                   <code>null</code>.
     ** @throws IndexOutOfBoundsException if <code>offset</code> is negative,
     **                                   <code>length</code> is negative, or
     **                                   <code>length</code> is greater than
     **                                   <code>buffer.length - offset</code>.
     ** @throws IOException               if an I/O error occurs.
     **
     ** @see    java.io.FilterInputStream#in
     */
    @Override
    public int read(final byte[] buffer, final int offset, final int length)
      throws IOException {

      int res = this.limit == 0 ? -1 : in.read(buffer, offset, length > this.limit ? (int)this.limit : length);
      if (res < 0 && this.limit > 0 && this.premature)
        throw new IOException("Unexpected end of stream");

      this.limit = res < 0 ? 0 : this.limit - res;
      return res;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: close (overridden)
    /**
     ** Usually called to closes this input stream and releases any system
     ** resources associated with the stream.
     ** <b>Note</b>:
     ** <br>
     ** The underlying stream will not closed.
     */
    @Override
    public final void close() {
      // end this stream, but don't close the underlying stream
      this.limit = 0;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ChunkedInputStream
  // ~~~~~ ~~~~~~~~~~~~~~~~~~
  /**
   ** The <code>ChunkedInputStream</code> decodes an {@link InputStream} whose
   ** data has the <i>chunked</i> transfer encoding applied to it, providing the
   ** underlying data.
   */
  public static class ChunkedInputStream extends LimitedInputStream {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected Headers header;
    protected boolean initialized;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>ChunkedInputStream</code> with the given
     ** underlying input stream, and a headers container to which the stream's
     ** trailing headers will be added.
     **
     ** @param  stream           the underlying <i>chunked</i>-encoded input
     **                          stream.
     **                          <br>
     **                          Allowed object is {@link InputStream}.
     ** @param  header           the headers container to which the stream's
     **                          trailing headers will be added, or
     **                          <code>null</code> if they are to be discarded.
     **                          <br>
     **                          Allowed object is {@link Headers}.
     */
    public ChunkedInputStream(final InputStream stream, final Headers header) {
      // ensure inheritance
      super(stream, 0, true);

      // initialize instance attributes
      this.header = header;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (overridden)
    /**
     ** Reads the next byte of data from this input stream. The value byte is
     ** returned as an <code>int</code> in the range <code>0</code> to
     ** <code>255</code>. If no byte is available because the end of the stream
     ** has been reached, the value <code>-1</code> is returned. This method
     ** blocks until input data is available, the end of the stream is detected,
     ** or an exception is thrown.
     ** <p>
     ** This method simply performs <code>stream.read()</code> and returns the
     ** result.
     **
     ** @return                  the next byte of data, or <code>-1</code> if
     **                          the end of the stream is reached.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     **
     ** @see    java.io.FilterInputStream#in
     */
    @Override
    public final int read()
      throws IOException {

      return limit <= 0 && initChunk() < 0 ? -1 : super.read();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: initChunk
    /**
     ** Initializes the next chunk.
     ** <br>
     ** If the previous chunk has not yet ended, or the end of stream has been
     ** reached, does nothing.
     **
     ** @return                  the length of the chunk, or <code>-1</code> if
     **                          the end of stream has been reached.
     **                          <br>
     **                          Possible object is <code>long</code>.
     **
     ** @throws IOException      if an I/O error occurs or the stream is
     **                          corrupt.
     */
    protected long initChunk()
      throws IOException {

      // finished previous chunk
      if (this.limit == 0) {
        // read chunk-terminating CRLF if it's not the first chunk
        if (this.initialized && Support.readLine(this.in).length() > 0)
          throw new IOException("Chunk data must end with CRLF");

        this.initialized = true;
        // read next chunk size
        this.limit = parseChunkSize(Support.readLine(in));
        // last chunk has size 0
        if (this.limit == 0) {
          // mark end of stream
          this.limit = -1;
          // read trailing headers, if any
          Headers trailingHeaders = readHeader(this.in);
          if (this.header != null)
            this.header.addAll(trailingHeaders);
        }
      }
      return this.limit;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: parseChunkSize
    /**
     ** Parses a chunk-size line.
     **
     ** @param  segment          the chunk-size line to parse.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the chunk size.
     **                          <br>
     **                          Possible object is <code>long</code>.
     **
     ** @throws IllegalArgumentException if the chunk-size line is invalid.
     */
    protected static long parseChunkSize(String segment)
      throws IllegalArgumentException {

      int pos = segment.indexOf(';');
      // ignore params, if any
      segment = pos < 0 ? segment : segment.substring(0, pos);
      try {
        // throws NFE
        return Support.parseUnsignedLong(segment, 16);
      }
      catch (NumberFormatException nfe) {
        throw new IllegalArgumentException("Invalid chunk size line [" + segment + "]");
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ChunkedOutputStream
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~
  /**
   ** The <code>ChunkedOutputStream</code> encodes an {@link OutputStream} with
   ** the <i>chunked</i> transfer encoding. It should be used only when the
   ** content length is not known in advance, and with the response
   ** Transfer-Encoding header set to <i>chunked</i>.
   ** <p>
   ** Data is written to the stream by calling the
   ** {@link #write(byte[], int, int)} method, which writes a new chunk per
   ** invocation. To end the stream, the {@link #writeTrailingChunk} method must
   ** be called or the stream closed.
   */
  public static class ChunkedOutputStream extends FilterOutputStream {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // the current stream state
    protected int state;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>ChunkedOutputStream</code> with the given
     ** underlying output stream.
     **
     ** @param  stream           the underlying output stream to which the
     **                          chunked stream is written.
     **                          <br>
     **                          Allowed object is {@link OutputStream}.
     */
    public ChunkedOutputStream(final OutputStream stream) {
     // ensure inheritance
      super(stream);

      // prevent bogus input
      if (stream == null)
        throw new NullPointerException("Output stream is null");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: write (overridden)
    /**
     ** Writes a chunk containing the given byte.
     ** <br>
     ** This method initializes a new chunk of size 1, and then writes the byte
     ** as the chunk data.
     **
     ** @param  b                the byte to write as a chunk.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public void write(final int b)
      throws IOException {

      write(new byte[] { (byte)b }, 0, 1);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: write (overridden)
    /**
     ** Writes a chunk containing the given bytes.
     ** <br>
     ** This method initializes a new chunk of the given size, and then writes
     ** the chunk data.
     **
     ** @param  buffer           an array containing the bytes to write.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     ** @param  offset           the offset within the array where the data
     **                          starts.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  length           the length of the data in bytes.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @throws IOException               if an I/O error occurs.
     ** @throws IndexOutOfBoundsException if the given offset or length are
     **                                   outside the bounds of the given array.
     */
    @Override
    public void write(final byte[] buffer, final int offset, final int length)
      throws IOException {

      // zero-sized chunk is the trailing chunk
      if (length > 0)
        initChunk(length);

      this.out.write(buffer, offset, length);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: close (overridden)
    /**
     ** Writes the trailing chunk if necessary, and closes the underlying
     ** stream.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public void close()
      throws IOException {

      if (this.state > -1)
        writeTrailingChunk(null);

      super.close();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: initChunk
    /**
     ** Writes the trailing chunk which marks the end of the stream.
     **
     ** @param  header           the (optional) trailing headers to write, or
     **                          <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Header}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public void writeTrailingChunk(final Headers header)
      throws IOException {

      // zero-sized chunk marks the end of the stream
      initChunk(0);
      if (header == null)
        // empty header block
        this.out.write(CRLF);
      else
        header.writeTo(this.out);
      this.state = -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: initChunk
    /**
     ** Initializes the next chunk with the given size.
     **
     ** @param  size             the length of the chunk (must be positive).
     **                          <br>
     **                          Allowed object is <code>long</code>.
     **
     ** @throws IOException      if an I/O error occurs or the stream has
     **                          already been closed.
     */
    protected void initChunk(final long size)
      throws IOException {

      if (size < 0)
        throw new IllegalArgumentException("Invalid size: " + size);
      if (this.state > 0)
        // end previous chunk
        this.out.write(CRLF);
      else if (this.state == 0)
        // start first chunk
        this.state = 1;
      else
        throw new IOException("Chunked stream has already ended");

      this.out.write(Support.bytes(Long.toHexString(size)));
      this.out.write(CRLF);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // class ResponseOutputStream
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~
  /**
   ** The <code>ResponseOutputStream</code> encompasses a single response over a
   ** connection, and does not close the underlying stream so that it can be
   ** used by subsequent responses.
   */
  public static class ResponseOutputStream extends FilterOutputStream {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>ResponseOutputStream</code> with the given
     ** underlying output stream.
     **
     ** @param  stream           the underlying output stream to which the
     **                          chunked stream is written.
     **                          <br>
     **                          Allowed object is {@link OutputStream}.
     */
    public ResponseOutputStream(final OutputStream stream) {
     // ensure inheritance
      super(stream);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: write (overridden)
    /**
     ** Writes a chunk containing the given bytes.
     ** <br>
     ** This method initializes a new chunk of the given size, and then writes
     ** the chunk data.
     **
     ** @param  buffer           an array containing the bytes to write.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     ** @param  offset           the offset within the array where the data
     **                          starts.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  length           the length of the data in bytes.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @throws IOException               if an I/O error occurs.
     ** @throws IndexOutOfBoundsException if the given offset or length are
     **                                   outside the bounds of the given array.
     */
    @Override
    public void write(final byte[] buffer, final int offset, final int length)
      throws IOException {

      // override the very inefficient default implementation
      this.out.write(buffer, offset, length);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: close (overridden)
    /**
     ** Writes the trailing chunk if necessary, and closes the underlying
     ** stream.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public void close()
      throws IOException {

      // intentionally left blank
      // keep underlying connection stream open
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Context
  // ~~~~~~~~~ ~~~~~~~
  /**
   * The {@code Context} annotation decorates methods which are mapped
   * to a context (path) within the server, and provide its contents.
   * <p>
   * The annotated methods must have the same signature and contract
   * as {@link ContextHandler#serve}, but can have arbitrary names.
   *
   * @see VirtualHost#add(Object)
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public @interface Context {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** The context (path) that this field maps to (must begin with '/').
     **
     ** @return                  the context (path) that this field maps to.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    String value();

    ////////////////////////////////////////////////////////////////////////////
    // Method:   methods
    /**
     ** The HTTP methods supported by this context handler (default is "GET").
     **
     ** @return                  the HTTP methods supported by this context
     **                          handler.
     **                          <br>
     **                          Possible object is array of {@link String}.
     */
    String[] methods() default "GET";
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface ContextHandler
  // ~~~~~~~~~ ~~~~~~~~~~~~~~
  /**
   ** A <code>ContextHandler</code> serves the content of resources within a
   ** context.
   **
   ** @see VirtualHost#add
   */
  public interface ContextHandler {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   serve
    /**
     ** Serves the given request using the given response.
     **
     ** @param  request          the request to be served.
     **                          <br>
     **                          Allowed object is {@link Request}.
     ** @param  response         the response to be filled.
     **                          <br>
     **                          Allowed object is {@link Response}.
     **
     ** @return                  an HTTP status code, which will be used in
     **                          returning a default response appropriate for
     **                          this status. If this method invocation already
     **                          sent anything in the response (headers or
     **                          content), it must return <code>0</code>, and no
     **                          further processing will be done.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    int serve(final Request request, final Response response)
      throws IOException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class FileContextHandler
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~
  /**
   ** The <code>FileContextHandler</code> services a context by mapping it to a
   ** file or folder (recursively) on disk.
   */
    public static class FileContextHandler implements ContextHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final File base;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>FileContextHandler</code> for the given
     ** <code>directory</code>.
     **
     ** @param  base             the abstract path the content is delivered to
     **                          the client from.
     **                          <br>
     **                          Allowed object is {@link File}.
     **
     ** @throws IOException      if an I/O error occurs, which is possible
     **                          because the construction of the canonical
     **                          pathname may require filesystem queries.
     */
    public FileContextHandler(final File base)
      throws IOException {

      // ensure inheritance
      super();

      // initialize instance attributes
      this.base = base.getCanonicalFile();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   serve (ContextHandler)
    /**
     ** Serves the given request using the given response.
     **
     ** @param  request          the request to be served.
     **                          <br>
     **                          Allowed object is {@link Request}.
     ** @param  response         the response to be filled.
     **                          <br>
     **                          Allowed object is {@link Response}.
     **
     ** @return                  an HTTP status code, which will be used in
     **                          returning a default response appropriate for
     **                          this status. If this method invocation already
     **                          sent anything in the response (headers or
     **                          content), it must return <code>0</code>, and no
     **                          further processing will be done.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public int serve(final Request request, final Response response)
      throws IOException {

      return serveFile(this.base, request.context().path, request, response);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MethodContextHandler
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~
  /**
   ** A <code>MethodContextHandler</code> services a context by invoking a
   ** handler method on a specified object.
   ** <p>
   ** The method must have the same signature and contract as
   ** {@link ContextHandler#serve}, but can have an arbitrary name.
   **
   ** @see VirtualHost#add(Object)
   */
  public static class MethodContextHandler implements ContextHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final Method method;
    protected final Object subject;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>MethodContextHandler</code> for the given
     ** <code>method</code> serving the specified <code>subject</code>.
     **
     ** @param  method           the host's name, or <code>null</code> if it is
     **                          the default host.
     **                          <br>
     **                          Allowed object is {@link Method}.
     ** @param  subject          ...
     **                          <br>
     **                          Allowed object is {@link Object}.
     */
    public MethodContextHandler(final Method method, final Object subject) {
      // ensure inheritance
      super();

      // prevent bogus input
      final Class<?>[] params = method.getParameterTypes();
      if (params.length != 2
      || !Request.class.isAssignableFrom(params[0])
      || !Response.class.isAssignableFrom(params[1])
      || !int.class.isAssignableFrom(method.getReturnType()))
        throw new IllegalArgumentException("Invalid method signature: " + method);

      // initialize instance attributes
      this.method  = method;
      this.subject = subject;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   serve (ContextHandler)
    /**
     ** Serves the given request using the given response.
     **
     ** @param  request          the request to be served.
     **                          <br>
     **                          Allowed object is {@link Request}.
     ** @param  response         the response to be filled.
     **                          <br>
     **                          Allowed object is {@link Response}.
     **
     ** @return                  an HTTP status code, which will be used in
     **                          returning a default response appropriate for
     **                          this status. If this method invocation already
     **                          sent anything in the response (headers or
     **                          content), it must return <code>0</code>, and no
     **                          further processing will be done.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public int serve(final Request request, final Response response)
      throws IOException {

      try {
        return (Integer)this.method.invoke(this.subject, request, response);
      }
      catch (InvocationTargetException e) {
        throw new IOException("Error: " + e.getCause().getMessage());
      }
      catch (Exception e) {
        throw new IOException("Error: " + e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class VirtualHost
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** The <code>VirtualHost</code> class represents a virtual host in the
   ** server.
   */
  public static class VirtualHost {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final String                             name;
    protected final Set<String>                        alias    = new CopyOnWriteArraySet<String>();
    protected volatile String                          index    = "index.html";
    protected volatile boolean                         generate;
    protected final Set<String>                        method   = new CopyOnWriteArraySet<String>();
    protected final ContextInfo                        empty    = new ContextInfo(null);
    protected final ConcurrentMap<String, ContextInfo> context  = new ConcurrentHashMap<String, ContextInfo>();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class ContextInfo
    // ~~~~~ ~~~~~~~~~~~
    /**
     ** The <code>ContextInfo</code> class holds a single context's information.
     */
    public class ContextInfo {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      protected final String path;
      protected final Map<String, ContextHandler> handlers = new ConcurrentHashMap<String, ContextHandler>(2);

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>ContextInfo</code> with the given context path.
       **
       ** @param  path           the context path (without trailing slash).
       **                        <br>
       **                        Allowed object is {@link String}.
       */
      public ContextInfo(final String path) {
        // ensure inheritance
        super();

        // initialize instance attributes
        this.path = path;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: add
      /**
       ** Adds (or replaces) a context handler for the given HTTP methods.
       **
       ** @param  handler        the context handler.
       **                        <br>
       **                        Allowed object is {@link ContextHandler}.
       ** @param  method         the HTTP methods supported by the handler
       **                        (default is "GET").
       **                        <br>
       **                        Allowed object is array of {@link String}.
       */
      public void add(final ContextHandler handler, String... method) {
        if (method.length == 0)
          method = new String[]{"GET"};

        for (String cursor : method) {
          this.handlers.put(cursor, handler);
          // it's now supported by server
          VirtualHost.this.method.add(cursor);
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>VirtualHost</code> with the given name.
     **
     ** @param  name             the host's name, or <code>null</code> if it is
     **                          the default host.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public VirtualHost(final String name) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.name = name;
      // for "OPTIONS *"
      this.context.put("*", new ContextInfo(null));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: context
    /**
     ** Returns the context handler for the given path.
     ** <p>
     ** If a context is not found for the given path, the search is repeated for
     ** its parent path, and so on until a base context is found. If neither the
     ** given path nor any of its parents has a context, an empty context is
     ** returned.
     **
     ** @param  path             the context's path.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the context info for the given path, or an
     **                          empty context if none exists.
     **                          <br>
     **                          Possible object is {@link ContextInfo}.
     */
    public ContextInfo context(String path) {
      // all context paths are without trailing slash
      for (path = Support.trimRight(path, '/'); path != null; path = Support.parentPath(path)) {
        ContextInfo info = this.context.get(path);
        if (info != null)
          return info;
      }
      return this.empty;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: alias
    /**
     ** Adds an alias for this host.
     **
     ** @param  alias            the alias to add.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public void alias(final String alias) {
      this.alias.add(alias);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: alias
    /**
     ** Returns this host's aliases.
     **
     ** @return                  the (unmodifiable) set of aliases (which may be
     **                          empty).
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link String}.
     */
    public Set<String> alias() {
      return Collections.unmodifiableSet(this.alias);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: index
    /**
     ** Sets the directory index file.
     ** <br>
     ** For every request whose URI ends with a '/' (i.e. a directory), the
     ** index file is appended to the path, and the resulting resource is served
     ** if it exists. If it does not exist, an auto-generated index for the
     ** requested directory may be served, depending on whether
     ** {@link #index a generated index is allowed}, otherwise an error is
     ** returned.
     ** <p>
     ** The default directory index file is "index.html".
     **
     ** @param  value            the directory index file, or <code>null</code>
     **                          if no index file should be used.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public void index(final String value) {
      this.index = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: index
    /**
     ** Returns this host's directory index file.
     **
     ** @return                  the directory index file, or <code>null</code>
     **                          if no index file is used.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String index() {
      return this.index;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Adds contexts for all methods of the given object that are annotated
     ** with the {@link Context} annotation.
     **
     ** @param  subject          the object whose annotated methods are added.
     **
     ** @throws IllegalArgumentException if a Context-annotated method has an
     **                                  {@link Context invalid signature}.
     */
    public void add(final Object subject)
      throws IllegalArgumentException {
      for (Class<?> c = subject.getClass(); c != null; c = c.getSuperclass()) {
        // add to contexts those with @Context annotation
        for (Method m : c.getDeclaredMethods()) {
          Context context = m.getAnnotation(Context.class);
          if (context != null) {
            m.setAccessible(true); // allow access to private method
            ContextHandler handler = new MethodContextHandler(m, subject);
            add(context.value(), handler, context.methods());
          }
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Adds a context and its corresponding context handler to this server.
     ** <br>
     ** Paths are normalized by removing trailing slashes (except the root).
     **
     ** @param  path             the context's path (must start with '/').
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  handler          the context handler for the given path.
     **                          <br>
     **                          Allowed object is {@link ContextHandler}.
     ** @param  method           the HTTP methods supported by the context
     **                          handler (default is "GET").
     **                          <br>
     **                          Allowed object is array of {@link String}.
     **
     ** @throws IllegalArgumentException if path is malformed.
     */
    public void add(String path, final ContextHandler handler, final String... method) {
      if (path == null || !path.startsWith("/") && !path.equals("*"))
        throw new IllegalArgumentException("Invalid path: " + path);

      // remove trailing slash
      path = Support.trimRight(path, '/');
      ContextInfo info     = new ContextInfo(path);
      ContextInfo existing = this.context.putIfAbsent(path, info);
      info = existing != null ? existing : info;
      info.add(handler, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Request
  // ~~~~~ ~~~~~~~
  /**
   ** The <code>Request</code> class encapsulates a single HTTP request.
   */
  public class Request {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected String                  method;
    protected URI                     uri;
    protected URL                     baseURL;
    protected String                  version;
    protected Headers                 header;
    protected InputStream             content;
    protected Socket                  socket;
    protected Map<String, String>     parameter;

    private   VirtualHost             host;
    private   VirtualHost.ContextInfo context;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Request</code> from the data in the given input
     ** stream.
     **
     ** @param  stream           the input stream from which the request is read.
     **                          <br>
     **                          Allowed object is {@link InputStream}.
     ** @param  socket           the underlying connected socket.
     **                          <br>
     **                          Allowed object is {@link Socket}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public Request(final InputStream stream, final Socket socket)
       throws IOException {

      // ensure inheritance
      super();

      // initialize instance attributes
      this.socket = socket;
      parseRequest(stream);
      this.header = readHeader(stream);
      // RFC2616#3.6 - if "chunked" is used, it must be the last one
      // RFC2616#4.4 - if non-identity Transfer-Encoding is present, it must
      //               either include "chunked" or close the connection after
      //               the body, and in any case ignore Content-Length.
      // if there is no such Transfer-Encoding, use Content-Length
      // if neither header exists, there is no body
      String header = this.header.get("Transfer-Encoding");
      if (header != null && !header.toLowerCase(Locale.US).equals("identity")) {
        if (Arrays.asList(Support.splitElements(header, true)).contains("chunked"))
          this.content = new ChunkedInputStream(stream, this.header);
        else
          this.content = stream; // body ends when connection closes
      }
      else {
        header = this.header.get("Content-Length");
        long len = header == null ? 0 : Support.parseUnsignedLong(header, 10);
        this.content = new LimitedInputStream(stream, len, false);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   baseURL
    /**
     ** Returns the base URL (scheme, host and port) of the request resource.
     ** <br>
     ** The host name is taken from the request URI or the Host header or a
     ** default host (see RFC2616#5.2).
     **
     ** @return                  the base URL of the requested resource, or
     **                          <code>null</code> if it is malformed.
     */
    public URL baseURL() {
      if (this.baseURL != null)
        return this.baseURL;

      // normalize host header
      String host = this.uri.getHost();
      if (host == null) {
        host = this.header.get("Host");
        // missing in HTTP/1.0
        if (host == null)
          host = Support.localHostName();
      }

      int pos = host.indexOf(':');
      host = pos < 0 ? host : host.substring(0, pos);
      try {
        return this.baseURL = new URL(secure ? "https" : "http", host, port, "");
      }
      catch (MalformedURLException mue) {
        return null;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   range
    /**
     ** Returns the absolute (zero-based) content range value read from the
     ** <i>Range</i> header. If multiple ranges are requested, a single range
     ** containing all of them is returned.
     **
     ** @param  length           the full length of the requested resource.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     **
     ** @return                  the requested range, or <code>null</code> if
     **                          the <i>Range</i> header is missing or invalid.
     **                          <br>
     **                          Possible object is <code>long</code>.
     */
    public long[] range(final long length) {
      String header = this.header.get("Range");
      return header == null || !header.startsWith("bytes=") ? null : Support.parseRange(header.substring(6), length);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   context
    /**
     ** Returns the info of the context handling this request.
     **
     ** @return                  the info of the context handling this request,
     **                          or an empty context.
     */
    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public VirtualHost.ContextInfo context() {
      return this.context != null ? this.context : (this.context = virtualHost().context(this.uri.getPath()));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   virtualHost
    /**
     ** Returns the virtual host corresponding to the requested host name, or
     ** the default host if none exists.
     **
     ** @return                  the virtual host corresponding to the requested
     **                          host name, or the default virtual host.
     **                          <br>
     **                          Possible object is {@link VirtualHost}.
     */
    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public VirtualHost virtualHost() {
      return this.host != null ? host : (this.host = Server.this.virtualHost(baseURL().getHost())) != null ? this.host : (this.host = Server.this.virtualHost(null));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   parameter
    /**
     ** Returns the request parameters, which are parsed both from the query
     ** part of the request URI, and from the request body if its content type
     ** is "application/x-www-form-urlencoded" (i.e. a submitted form).
     ** <br>
     ** UTF-8 encoding is assumed in both cases.
     ** <p>
     ** For multivalued parameters (i.e. multiple parameters with the same
     ** name), only the first one is considered. For access to all values, use
     ** {@link #parameter()} instead.
     ** <p>
     ** The map iteration retains the original order of the parameters.
     **
     ** @return                  the request parameters name-value pairs, or an
     **                          empty map if there are none.
     **                          <br>
     **                          Possible object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link String} as the value.
     **
     ** @throws IOException      if an I/O error occurs.
     **
     ** @see    #parameterList()
     */
    public Map<String, String> parameter()
      throws IOException {

      if (this.parameter == null)
        this.parameter = Support.toMap(parameterList());

      return this.parameter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   parameterList
    /**
     ** Returns the request parameters, which are parsed both from the query
     ** part of the request URI, and from the request body if its content type
     ** is "application/x-www-form-urlencoded" (i.e. a submitted form).
     ** <br>
     ** UTF-8 encoding is assumed in both cases.
     ** <p>
     ** The parameters are returned as a list of string arrays, each containing
     ** the parameter name as the first element and its corresponding value as
     ** the second element (or an empty string if there is no value).
     ** <p>
     ** The list retains the original order of the parameters.
     **
     ** @return                  the request parameters name-value pairs, or an
     **                          empty list if there are none.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public List<String[]> parameterList()
      throws IOException {

      List<String[]> query = Support.parseParameter(this.uri.getRawQuery());
      List<String[]> body  = Collections.emptyList();
      String         type  = this.header.get("Content-Type");
      if (type != null && type.toLowerCase(Locale.US).startsWith("application/x-www-form-urlencoded"))
        // 2MB limit
        body = Support.parseParameter(Support.readToken(this.content, -1, "UTF-8", 2097152));
      if (body.isEmpty())
        return query;
      if (query.isEmpty())
        return body;
      query.addAll(body);
      return query;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   path
    /**
     ** Sets the path component of the request URI.
     ** <br>
     ** This can be useful in URL rewriting, etc.
     **
     ** @param  path             the path to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @throws IllegalArgumentException if the given path is malformed.
     */
    public void path(final String path) {
      try {
        this.uri = new URI(this.uri.getScheme(), this.uri.getUserInfo(), this.uri.getHost(), this.uri.getPort(), Support.trimDuplicates(path, '/'), uri.getQuery(), uri.getFragment());
        // clear cached context so it will be recalculated
        this.context = null;
      }
      catch (URISyntaxException e) {
        throw new IllegalArgumentException("Error setting path", e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   parseRequest
    /**
     ** Reads the request line, parsing the method, URI and version string.
     **
     ** @param  stream           the underlying input stream from which the
     **                          request line is read.
     **                          <br>
     **                          Allowed object is {@link InputStream}.
     **
     ** @throws IOException      if an I/O error occurs or the request line is
     **                          invalid.
     */
    protected void parseRequest(final InputStream stream)
      throws IOException {

      // RFC2616#4.1: should accept empty lines before request line
      // RFC2616#19.3: tolerate additional whitespace between tokens
      String line;
      try {
        do {
          line = Support.readLine(stream);
        } while (line.length() == 0);
      }
      // if EOF, timeout etc.
      catch (IOException e) {
        // signal that the request did not begin
        throw new IOException("Missing request line");
      }

      String[] tokens = Support.split(line, " ", -1);
      if (tokens.length != 3)
        throw new IOException("Invalid request line: \"" + line + "\"");

      try {
        this.method  = tokens[0];
        // must remove '//' prefix which constructor parses as host name
        this.uri     = new URI(Support.trimDuplicates(tokens[1], '/'));
        // RFC2616#2.1: allow implied LWS; RFC7230#3.1.1: disallow it
        this.version = tokens[2];
      }
      catch (URISyntaxException e) {
        throw new IOException("Unvalid URI: " + e.getMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Response
  // ~~~~~ ~~~~~~~~
  /**
   ** The <code>Response</code> class encapsulates a single HTTP response.
   */
  public class Response implements Closeable {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // the underlying output stream
    protected OutputStream stream;
    // chained encoder streams
    protected OutputStream encoded;
    protected Headers      header;
    protected boolean      discard;
    // nothing sent, headers sent, or closed
    protected int          state;
    // request used in determining client capabilities
    protected Request      request;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Response</code> whose output is written to the given
     ** stream.
     **
     ** @param  stream           the input stream from which the request is read.
     **                          <br>
     **                          Allowed object is {@link OutputStream}.
     */
    public Response(final OutputStream stream) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.stream = stream;
      this.header = new Headers();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   close (Closeable)
    /**
     ** Closes this response and flushes all output.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public void close()
      throws IOException {

      // closed
      this.state = -1;
      if (this.encoded != null)
        // close all chained streams (except the underlying one)
        this.encoded.close();
      // always flush underlying stream (even if content was never called)
      this.stream.flush();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   redirect
    /**
     ** Sends a 301 or 302 response, redirecting the client to the given URL.
     **
     ** @param  url              the absolute URL to which the client is
     **                          redirected.
     ** @param  permanent        specifies whether a permanent (301) or
     **                          temporary (302) redirect status is sent.
     **
     ** @throws IOException      if an I/O error occurs or url is malformed.
     */
    public void redirect(String url, boolean permanent)
      throws IOException {

      try {
        url = new URI(url).toASCIIString();
      }
      catch (URISyntaxException e) {
        throw new IOException("Malformed URL: " + url);
      }

      this.header.add("Location", url);
      // some user-agents expect a body, so we send it
      if (permanent)
        sendError(301, "Permanently moved to " + url);
      else
        sendError(302, "Temporarily moved to " + url);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   sendContent
    /**
     ** Sends the response body.
     ** <br>
     ** This method must be called only after the response headers have been
     ** sent (and indicate that there is a body).
     **
     ** @param  body             a stream containing the response body.
     **                          <br>
     **                          Allowed object is {@link InputStream}.
     ** @param  length           the full length of the response body, or
     **                          <code>-1</code> for the whole stream.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     ** @param  range            the sub-range within the response body that
     **                          should be sent, or <code>null</code> if the
     **                          entire body should be sent.
     **                          <br>
     **                          Allowed object is array of <code>long</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public void sendContent(final InputStream body, long length, final long[] range)
      throws IOException {
      OutputStream out = content();
      if (out != null) {
        if (range != null) {
          long offset = range[0];
          length = range[1] - range[0] + 1;
          while (offset > 0) {
            long skip = body.skip(offset);
            if (skip == 0)
              throw new IOException("Can't skip to " + range[0]);
            offset -= skip;
          }
        }
        Support.transfer(body, out, length);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   sendError
    /**
     ** Sends an error response with the given status and default body.
     **
     ** @param  status           the response status.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public void sendError(int status)
      throws IOException {

      sendError(status, status < 400 ? ":)" : "sorry it didn't work out :(");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   sendError
    /**
     ** Sends an error response with the given status and detailed message.
     ** <br>
     ** An HTML body is created containing the status and its description, as
     ** well as the message, which is escaped using the
     ** {@link Support#escape escape} method.
     **
     ** @param  status           the response status.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  text             the text body (sent as text/html)
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public void sendError(final int status, final String text)
      throws IOException {

      send(status, String.format("<!DOCTYPE html>%n<html>%n<head><title>%d %s</title></head>%n" + "<body><h1>%d %s</h1>%n<p>%s</p>%n</body></html>", status, STATUS[status], status, STATUS[status], Support.escape(text)));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   send
    /**
     ** Sends the full response with the given status, and the given string as
     ** the body. The text is sent in the UTF-8 charset. If a Content-Typ
     ** header was not explicitly set, it will be set to text/html, and so the
     ** text must contain valid (and properly {@link Support#escape escaped})
     ** HTML.
     **
     ** @param  status           the response status.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  text             the text body (sent as text/html)
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public void send(final int status, final String text)
      throws IOException {
      byte[] content = text.getBytes("UTF-8");
      sendHeader(status, content.length, -1, "W/\"" + Integer.toHexString(text.hashCode()) + "\"", "text/html; charset=utf-8", null);
      OutputStream out = content();
      if (out != null)
        out.write(content);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   content
    /**
     ** Returns an output stream into which the response body can be written.
     ** <br>
     ** The stream applies encodings (e.g. compression) according to the sent
     ** headers.
     ** <br>
     ** This method must be called after response headers have been sent that
     ** indicate there is a body. Normally, the content should be prepared
     ** (not sent) even before the headers are sent, so that any errors during
     ** processing can be caught and a proper error response returned - after
     ** the headers are sent, it's too late to change the status into an error.
     **
     ** @return                  an output stream into which the response body
     **                          can be written, or <code>null</code> if the
     **                          body should not be written (e.g. it is
     **                          discarded).
     **                          <br>
     **                          Possible object is {@link OutputStream}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public OutputStream content()
      throws IOException {
      if (this.encoded != null || this.discard)
        // return the existing stream (or null)
        return this.encoded;
      // set up chain of encoding streams according to headers
      List<String> te = Arrays.asList(Support.splitElements(this.header.get("Transfer-Encoding"), true));
      List<String> ce = Arrays.asList(Support.splitElements(this.header.get("Content-Encoding"), true));
      // leaves underlying stream open when closed
      this.encoded = new ResponseOutputStream(this.stream);
      if (te.contains("chunked"))
        this.encoded = new ChunkedOutputStream(this.encoded);
      if (ce.contains("gzip") || te.contains("gzip"))
        this.encoded = new GZIPOutputStream(this.encoded, 4096);
      else if (ce.contains("deflate") || te.contains("deflate"))
        this.encoded = new DeflaterOutputStream(this.encoded);
      // return the outer-most stream
      return this.encoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   sendHeader
    /**
     ** Sends the response headers, including the given response status and
     ** description, and all response headers. If they do not already exist,
     ** the following headers are added as necessary:
     ** <ul>
     **   <li>Content-Range
     **   <li>Content-Type
     **   <li>Transfer-Encoding
     **   <li>Content-Encoding
     **   <li>Content-Length
     **   <li>Last-Modified
     **   <li>ETag
     **   <li>Connection
     **   <li>Date
     ** </ul>
     ** Ranges are properly calculated as well, with a 200 status changed to a
     ** 206 status.
     **
     ** @param  status           the response status.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  length           the response body length, or zero if there is
     **                          no body, or negative if there is a body but its
     **                          length is not yet known.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     ** @param  lastModified     the last modified date of the response
     **                          resource, or non-positive if unknown. A time in
     **                          the future will be replaced with the current
     **                          system time.
     ** @param  etag             the <code>ETag</code> of the response resource,
     **                          or <code>null</code> if unknown
     **                          (see RFC2616#3.11).
     ** @param  contentType      the content type of the response resource, or
     **                          <code>null</code> if unknown (in which case
     **                          "application/octet-stream" will be sent).
     ** @param  range            the content range that will be sent, or
     **                          <code>null</code> if the entire resource will
     **                          be sent.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public void sendHeader(int status, long length, final long lastModified, final String etag, final String contentType, final long[] range)
      throws IOException {
      if (range != null) {
        this.header.add("Content-Range", "bytes " + range[0] + "-" + range[1] + "/" + (length >= 0 ? length : "*"));
        length = range[1] - range[0] + 1;
        if (status == 200)
          status = 206;
      }
      String ct = this.header.get("Content-Type");
      if (ct == null) {
        ct = contentType != null ? contentType : "application/octet-stream";
        this.header.add("Content-Type", ct);
      }
      if (!this.header.contains("Content-Length") && !this.header.contains("Transfer-Encoding")) {
        // RFC2616#3.6: transfer encodings are case-insensitive and must not be sent to an HTTP/1.0 client
        boolean      modern    = this.request != null && this.request.version.endsWith("1.1");
        String       accepted  = this.request == null ? null : this.request.header.get("Accept-Encoding");
        List<String> encodings = Arrays.asList(Support.splitElements(accepted, true));
        String       compression = encodings.contains("gzip") ? "gzip" : encodings.contains("deflate") ? "deflate" : null;
        if (compression != null && (length < 0 || length > 300) && Support.compressible(ct) && modern) {
          this.header.add("Transfer-Encoding", "chunked"); // compressed data is always unknown length
          this.header.add("Content-Encoding", compression);
        }
        else if (length < 0 && modern) {
          this.header.add("Transfer-Encoding", "chunked"); // unknown length
        }
        else if (length >= 0) {
          this.header.add("Content-Length", Long.toString(length)); // known length
        }
      }
      // RFC7231#7.1.4: Vary field should include headers
      if (!this.header.contains("Vary"))
        // that are used in selecting representation
        this.header.add("Vary", "Accept-Encoding");
      // RFC2616#14.29
      if (lastModified > 0 && !this.header.contains("Last-Modified"))
        this.header.add("Last-Modified", Support.formatDate(Math.min(lastModified, System.currentTimeMillis())));
      if (etag != null && !this.header.contains("ETag"))
        this.header.add("ETag", etag);
      if (this.request != null && "close".equalsIgnoreCase(this.request.header.get("Connection")) && !this.header.contains("Connection"))
        // #RFC7230#6.6: should reply to close with close
        this.header.add("Connection", "close");
      sendHeader(status);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   sendHeader
    /**
     ** Sends the response headers with the given response status.
     ** <br>
     ** A Date header is added if it does not already exist.
     ** <br>
     ** If the response has a body, the Content-Length/Transfer-Encoding and
     ** Content-Type headers must be set before sending the headers.
     **
     ** @param  status           the response status.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs, or headers were already
     **                          sent.
     **
     ** @see    #sendHeader(int, long, long, String, String, long[])
     */
    public void sendHeader(int status)
      throws IOException {

      if (this.state == 1)
        throw new IOException("Headers were already sent");

      if (!this.header.contains("Date"))
        this.header.add("Date", Support.formatDate(System.currentTimeMillis()));
      this.header.add("Server", "lHTTP/2.6");
      this.stream.write(Support.bytes("HTTP/1.1 ", Integer.toString(status), " ", STATUS[status]));
      this.stream.write(CRLF);
      this.header.writeTo(this.stream);
      // headers sent
      this.state = 1;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class SocketHandler
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** The <code>SocketHandler</code> handles accepted sockets.
   */
  protected class SocketHandler extends Thread {

    @Override @SuppressWarnings("oracle.jdeveloper.java.insufficient-catch-block")
    public void run() {
      setName(getClass().getSimpleName() + "-" + port);
      try {
        // keep local to avoid NPE when stopped
        ServerSocket instance = Server.this.instance;
        while (instance != null && !instance.isClosed()) {
          final Socket socket = instance.accept();
          Server.this.executor.execute(
            new Runnable() {
              public void run() {
                try {
                  try {
                    socket.setSoTimeout(Server.this.timeout);
                    // we buffer anyway, so improve latency
                    socket.setTcpNoDelay(true);
                    handleConnection(socket.getInputStream(), socket.getOutputStream(), socket);
                  }
                  finally {
                    try {
                      // RFC7230#6.6 - close socket gracefully
                      // (except SSL socket which doesn't support half-closing)
                      if (!(socket instanceof SSLSocket)) {
                        // half-close socket (only output)
                        socket.shutdownOutput();
                        // consume input
                        Support.transfer(socket.getInputStream(), null, -1);
                      }
                    }
                    finally {
                      // and finally close socket fully
                      socket.close();
                    }
                  }
                }
                catch (IOException e) {
                  // intentionally left balnk
                }
              }
            }
          );
        }
      }
      catch (IOException e) {
        // intentionally left balnk
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Server</code> which can accept connections on the given
   ** port.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The {@link #start()} method must be called to start accepting connections.
   **
   ** @param  port               the port on which this server will accept
   **                            connections.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public Server(final int port) {
    // ensure inheritance
    super();

    // initialize intenace attributes
    this.port = port;
    // add default virtual host
    add(new VirtualHost(null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port
  /**
   ** Sets the port on which this server will accept connections.
   **
   ** @param  value              the port on which this server will accept
   **                            connections.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public void setPort(final int value) {
    this.port = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   socketFactory
  /**
   ** Sets the factory used to create the server socket.
   ** <br>
   ** If <code>null</code> or not set, the default
   ** {@link ServerSocketFactory#getDefault()} is used.
   ** <br>
   ** For secure sockets (HTTPS), use an SSLServerSocketFactory instance.
   ** <br>
   ** The port should usually also be changed for HTTPS, e.g. port 443 instead
   ** of 80.
   ** <p>
   ** If using the default SSLServerSocketFactory returned by
   ** {@link SSLServerSocketFactory#getDefault()}, the appropriate system
   ** properties must be set to configure the default JSSE provider, such as
   ** <code>javax.net.ssl.keyStore</code> and
   ** <code>javax.net.ssl.keyStorePassword}</code>.
   **
   ** @param  value              the server socket factory to use.
   **                            <br>
   **                            Allowed object is {@link ServerSocketFactory}.
   */
  public void socketFactory(final ServerSocketFactory value) {
    this.secure        = value instanceof SSLServerSocketFactory;
    this.socketFactory = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeout
  /**
   ** Sets the socket timeout for established connections.
   **
   ** @param  value              the socket timeout in milliseconds.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public void timeout(final int value) {
    this.timeout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executor
  /**
   ** Sets the executor used in servicing HTTP connections.
   ** <br>
   ** If <code>null</code>, a default executor is used.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The caller is responsible for shutting down the provided executor when
   ** necessary.
   **
   ** @param  value              the {@link Executor} to use.
   **                            <br>
   **                            Allowed object is {@link Executor}.
   */
  public void executor(final Executor value) {
    this.executor = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   virtualHost
  /**
   ** Returns the virtual host with the given name.
   **
   ** @param  name               the name of the virtual host to return, or
   **                            <code>null</code> for the default virtual host.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the virtual host with the given name, or
   **                            <code>null</code> if it doesn't exist.
   **                            <br>
   **                            Possible object is {@link VirtualHost}.
   */
  public VirtualHost virtualHost(final String name) {
    return this.hosts.get(name == null ? "" : name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   virtualHost
  /**
   ** Returns all virtual hosts.
   **
   ** @return                    all virtual hosts (as an unmodifiable set).
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link VirtualHost}.
   */
  public Set<VirtualHost> virtualHost() {
    return Collections.unmodifiableSet(new HashSet<VirtualHost>(this.hosts.values()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Starts a stand-alone HTTP server, serving files from disk.
   **
   ** @param  args               the command line arguments
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  public static void main(String[] args) {
    try {
      if (args.length == 0) {
        System.err.printf("Usage: java [-options] %s <directory> [port]%n To enable SSL: specify options -Djavax.net.ssl.keyStore, -Djavax.net.ssl.keyStorePassword, etc.%n", Server.class.getName());
        return;
      }

      final File dir = new File(args[0]);
      if (!dir.canRead())
        throw new FileNotFoundException(dir.getAbsolutePath());

      int port = args.length < 2 ? 80 : (int)Support.parseUnsignedLong(args[1], 10);

      // set up server
      for (File f : Arrays.asList(new File("/etc/mime.types"), new File(dir, ".mime.types")))
        if (f.exists())
          contentType(new FileInputStream(f));

      final Server server = new Server(port);
      // enable SSL if configured
      if (System.getProperty("javax.net.ssl.keyStore") != null)
        server.socketFactory(SSLServerSocketFactory.getDefault());

      // default host
      final VirtualHost host = server.virtualHost(null);
      // with directory index pages
      host.generate = true;
      host.add("/",         new FileContextHandler(dir));
      host.add("/api/time", new ContextHandler() {
        public int serve(final Request request, final Response response)
          throws IOException {

          long now = System.currentTimeMillis();
          response.header.add("Content-Type", "text/plain");
          response.send(200, String.format("%tF %<tT", now));
          return 0;
        }
      });
      server.start();
      System.out.println("Server is listening on port " + port);
    }
    catch (Exception e) {
      System.err.println("error: " + e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentType
  /**
   ** Adds a Content-Type mapping for the given path suffixes.
   ** <br>
   ** If any of the path suffixes had a previous Content-Type associated with
   ** it, it is replaced with the given one.
   ** <br>
   ** Path suffixes are considered case-insensitive, and contentType is
   ** converted to lowercase.
   **
   ** @param  type               the content type (MIME type) to be associated
   **                            with the given path suffixes.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  suffixes           the path suffixes which will be associated with
   **                            the contentType, e.g. the file extensions of
   **                            served files (excluding the '.' character).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static void contentType(final String type, final String... suffixes) {
    for (String suffix : suffixes)
      MIME.put(suffix.toLowerCase(Locale.US), type.toLowerCase(Locale.US));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentType
  /**
   ** Returns the content type for the given path, according to its suffix, or
   ** the given default content type if none can be determined.
   **
   ** @param  path               the path whose content type is requested.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultContent     a default content type which is returned if
   **                            none can be determined.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the content type for the given path, or the
   **                            given default.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String contentType(final String path, final String defaultContent) {
    int    dot = path.lastIndexOf('.');
    String type = dot < 0 ? defaultContent : MIME.get(path.substring(dot + 1).toLowerCase(Locale.US));
    return type != null ? type : defaultContent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentType
  /**
   ** Adds Content-Type mappings from a standard mime.types file.
   **
   ** @param  source             a stream containing a mime.types file
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws IOException           if an I/O error occurs.
   ** @throws FileNotFoundException if the file is not found or cannot be read.
   */
  public static void contentType(final InputStream source)
    throws IOException {
    try {
      while (true) {
        // throws EOFException when done
        final String line = Support.readLine(source).trim();
        if (line.length() > 0 && line.charAt(0) != '#') {
          final String[] tokens = Support.split(line, " \t", -1);
          for (int i = 1; i < tokens.length; i++)
            contentType(tokens[0], tokens[i]);
        }
      }
    }
    // the end of file was reached - it's ok
    catch (EOFException ignore) {
      // intentionally left blank
      ;
    }
    finally {
      source.close();
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   readHeader
  /**
   ** Reads headers from the given stream. Headers are read according to the
   * RFC, including folded headers, element lists, and multiple headers
   * (which are concatenated into a single element list header).
   * Leading and trailing whitespace is removed.
   **
   ** @param  stream             the input stream from which the headers are
   **                            read.
   **                            <br>
   **                            Allowed object is {@link Headers}.
   **
   ** @return                    the read headers (possibly empty, if none
   **                            exist)
   **                            <br>
   **                            Possible object is {@link InputStream}.
   **
   ** @throws IOException        if an I/O error occurs or the headers are
   **                            malformed or there are more than 100 header
   **                            lines.
   */
  @SuppressWarnings({ "oracle.jdeveloper.java.nested-assignment", "oracle.jdeveloper.java.empty-statement" })
  public static Headers readHeader(final InputStream stream)
    throws IOException {

    String  current;
    String  previous = "";
    int     count    = 0;
    Headers headers  = new Headers();
    while ((current = Support.readLine(stream)).length() > 0) {
      // start of line data (after whitespace)
      int start;
      for (start = 0; start < current.length() && Character.isWhitespace(current.charAt(start)); start++)
        ;
      // unfold header continuation line
      if (start > 0)
        current = previous + ' ' + current.substring(start);
      int separator = current.indexOf(':');
      if (separator < 0)
        throw new IOException("Invalid header: [" + current + "]");

      String name     = current.substring(0, separator);
      // ignore LWS
      String value    = current.substring(separator + 1).trim();
      Header replaced = headers.replace(name, value);
      // concatenate repeated headers (distinguishing repeated from folded)
      if (replaced != null && start == 0) {
        value   = replaced.value + ", " + value;
        current = name + ": " + value;
        headers.replace(name, value);
      }
      previous = current;
      if (++count > 100)
        throw new IOException("Too many header lines");
    }
    return headers;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serveFile
  /**
   ** Serves a context's contents from a file based resource.
   ** <p>
   ** The file is located by stripping the given context prefix from the
   ** request's path, and appending the result to the given base directory.
   ** <p>
   ** Missing, forbidden and otherwise invalid files return the appropriate
   ** error response. Directories are served as an HTML index page if the
   ** virtual host allows one, or a forbidden error otherwise. Files are sent
   ** with their corresponding content types, and handle conditional and partial
   ** retrievals according to the RFC.
   **
   ** @param  base               the base directory to which the context is
   **                            mapped.
   ** @param  context            the context which is mapped to the base
   **                            directory.
   ** @param  request            the request to be served.
   **                            <br>
   **                            Allowed object is {@link Request}.
   ** @param  response           the response into which the content is written.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @return                    the HTTP status code to return, or
   **                            <code>0</code> if a response was sent.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  public static int serveFile(final File base, final String context, final Request request, final Response response)
    throws IOException {

    String relativePath = request.uri.getPath().substring(context.length());
    File   file = new File(base, relativePath).getCanonicalFile();
    if (!file.exists() || file.isHidden() || file.getName().startsWith(".")) {
      return 404;
    }
    // validate
    else if (!file.canRead() || !file.getPath().startsWith(base.getPath())) {
      return 403;
    }
    else if (file.isDirectory()) {
      if (relativePath.endsWith("/")) {
        if (!request.virtualHost().generate)
          return 403;

        response.send(200, Support.createIndex(file, request.uri.getPath()));
      }
      // redirect to the normalized directory URL ending with '/'
      else {
        response.redirect(request.baseURL() + request.uri.getPath() + "/", true);
      }
    }
    else if (relativePath.endsWith("/")) {
      // non-directory ending with slash (File constructor removed it)
      return 404;
    }
    else {
      serveFileContent(file, request, response);
    }
    return 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serveFileContent
  /**
   ** Serves the contents of a file, with its corresponding content type, last
   ** modification time, etc. conditional and partial retrievals are handled
   ** according to the RFC.
   **
   ** @param  file               the existing and readable file whose contents
   **                            are served.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  request            the request to be served.
   **                            <br>
   **                            Allowed object is {@link Request}.
   ** @param  response           the response into which the content is written.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  public static void serveFileContent(final File file, final Request request, final Response response)
    throws IOException {

    long length = file.length();
    long modified = file.lastModified();
    // a weak tag based on date
    String etag = "W/\"" + modified + "\"";
    int    status = 200;
    // handle range or conditional request
    long[] range = request.range(length);
    if (range == null || length == 0) {
      status = evaluateStatus(request, modified, etag);
    }
    else {
      String ifRange = request.header.get("If-Range");
      if (ifRange == null) {
        if (range[0] >= length)
          // unsatisfiable range
          status = 416;
        else
          status = evaluateStatus(request, modified, etag);
      }
      else if (range[0] >= length) {
        // RFC2616#14.16, 10.4.17: invalid If-Range gets everything
        range = null;
      }
      // send either range or everything
      else {
        if (!ifRange.startsWith("\"") && !ifRange.startsWith("W/")) {
          Date date = request.header.getDate("If-Range");
          if (date != null && modified > date.getTime())
            // modified - send everything
            range = null;
        }
        else if (!ifRange.equals(etag)) {
          // modified - send everything
          range = null;
        }
      }
    }
    // send the response
    switch (status) {
      case 304 : // no other headers or body allowed
                 response.header.add("ETag", etag);
                 response.header.add("Vary", "Accept-Encoding");
                 response.header.add("Last-Modified", Support.formatDate(modified));
                 response.sendHeader(304);
                 break;
      case 412 : response.sendHeader(412);
                 break;
      case 416 : response.header.add("Content-Range", "bytes */" + length);
                 response.sendHeader(416);
                 break;
      case 200 : // send OK response
                 response.sendHeader(200, length, modified, etag, contentType(file.getName(), "application/octet-stream"), range);
                 // send body
                 InputStream in = new FileInputStream(file);
                 try {
                   response.sendContent(in, length, range);
                 }
                 finally {
                   in.close();
                 }
                 break;
      default  : // should never happen
                 response.sendHeader(500);
                 break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evaluateStatus
  /**
   ** Calculates the appropriate response status for the given request and its
   ** resource's last-modified time and ETag, based on the conditional headers
   ** present in the request.
   **
   ** @param  request            the request to be served.
   **                            <br>
   **                            Allowed object is {@link Request}.
   ** @param  lastModified       the resource's last modified time
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  etag               the resource's ETag.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the appropriate response status for the
   **                            request.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int evaluateStatus(final Request request, final long lastModified, final String etag) {
    // If-Match
    String header = request.header.get("If-Match");
    if (header != null && !Support.match(true, Support.splitElements(header, false), etag))
      return 412;
    // If-Unmodified-Since
    Date date = request.header.getDate("If-Unmodified-Since");
    if (date != null && lastModified > date.getTime())
      return 412;
    // If-Modified-Since
    int     status = 200;
    boolean force = false;
    date = request.header.getDate("If-Modified-Since");
    if (date != null && date.getTime() <= System.currentTimeMillis()) {
      if (lastModified > date.getTime())
        force = true;
      else
        status = 304;
    }
    // If-None-Match
    header = request.header.get("If-None-Match");
    if (header != null) {
      // RFC7232#3.2: use weak matching
      if (Support.match(false, Support.splitElements(header, false), etag))
        status = request.method.equals("GET") || request.method.equals("HEAD") ? 304 : 412;
      else
        force = true;
    }
    return force ? 200 : status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   socketFactory
  /**
   ** Adds the given virtual host to the server.
   ** <br>
   ** If the host's name or aliases already exist, they are overwritten.
   **
   ** @param  host               the virtual host to add.
   **                            <br>
   **                            Allowed object is {@link VirtualHost}.
   */
  public void add(final VirtualHost host) {
    hosts.put(host.name == null ? "" : host.name, host);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Starts this server.
   ** <br>
   ** If it is already started, does nothing.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Once the server is started, configuration-altering methods of the server
   ** and its virtual hosts must not be used. To modify the configuration, the
   ** server must first be stopped.
   **
   ** @throws IOException        if the server cannot begin accepting
   **                            connections.
   */
  public synchronized void start()
    throws IOException {

    if (this.instance != null)
      return;

    // assign default server socket factory if needed
    if (this.socketFactory == null)
      // plain sockets
      this.socketFactory = ServerSocketFactory.getDefault();

    this.instance = createSocket();
    // assign default executor if needed
    if (this.executor == null)
      // consumes no resources when idle
      this.executor = Executors.newCachedThreadPool();

    // register all host aliases (which may have been modified)
    for (VirtualHost host : virtualHost())
      for (String alias : host.alias())
        this.hosts.put(alias, host);

    // start handling incoming connections
    new SocketHandler().start();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Stops this server. If it is already stopped, does nothing.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** If an {@link #executor(Executor) Executor} was set, it must be closed
   ** separately.
   */
  public synchronized void stop() {
    try {
      if (this.instance != null)
        this.instance.close();
    }
    catch (IOException e) {
      // intentionally left blank
      ;
    }
    this.instance = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket
  /**
   ** Creates the server socket used to accept connections, using the configured
   ** {@link #socketFactory ServerSocketFactory} and {@link #port}.
   ** <p>
   ** Cryptic errors seen here often mean the factory configuration details are
   ** wrong.
   **
   ** @return                    the created server socket.
   **                            <br>
   **                            Possible object is {@link ServerSocket}.
   **
   ** @throws IOException        if the socket cannot be created.
   */
  protected ServerSocket createSocket()
    throws IOException {

    final ServerSocket server = this.socketFactory.createServerSocket();
    server.setReuseAddress(true);
    server.bind(new InetSocketAddress(this.port));
    return server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleConnection
  /**
   ** Handles communications for a single connection over the given streams.
   ** <br>
   ** Multiple subsequent transactions are handled on the connection, until the
   ** streams are closed, an error occurs, or the request contains a
   ** <i>Connection: close</i> header which explicitly requests the connection
   ** be closed after the transaction ends.
   **
   ** @param  source             the stream from which the incoming requests are
   **                            read.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  output             the stream into which the outgoing responses
   **                            are written.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   ** @param  socket             the connected socket.
   **                            <br>
   **                            Allowed object is {@link Socket}.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  protected void handleConnection(InputStream source, OutputStream output, final Socket socket)
    throws IOException {

    source = new BufferedInputStream(source, 4096);
    output = new BufferedOutputStream(output, 4096);
    Request  request;
    Response response;
    do {
      // create request and response and handle transaction
      request  = null;
      response = new Response(output);
      try {
        request = new Request(source, socket);
        handleTransaction(request, response);
      }
      // unhandled errors (not normal error responses like 404)
      catch (Throwable t) {
        // error reading request
        if (request == null) {
          if (t instanceof IOException && t.getMessage().contains("missing request line"))
            // we're not in the middle of a transaction - so just disconnect
            break;
          // about to close connection
          response.header.add("Connection", "close");
          // e.g. SocketTimeoutException
          if (t instanceof InterruptedIOException)
            response.sendError(408, "Timeout waiting for client request");
          else
            response.sendError(400, "Invalid request: " + t.getMessage());
        }
        // if headers were not already sent, we can send an error response
        else if (!(response.state == 1)) {
          // ignore whatever headers may have already been set
          response = new Response(output);
          // about to close connection
          response.header.add("Connection", "close");
          response.sendError(500, "Error processing request: " + t.getMessage());
        } // otherwise just abort the connection since we can't recover
        // proceed to close connection
        break;
      }
      finally {
         // close response and flush output
        response.close();
      }
      // consume any leftover body data so next request can be processed
      Support.transfer(request.content, null, -1);
      // RFC7230#6.6: persist connection unless client or server close explicitly (or legacy client)
    } while (!"close".equalsIgnoreCase(request.header.get("Connection")) && !"close".equalsIgnoreCase(response.header.get("Connection")) && request.version.endsWith("1.1"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleTransaction

  /**
   ** Handles a single transaction on a connection.
   ** <p>
   ** Subclasses can override this method to perform filtering on the request or
   ** response, apply wrappers to them, or further customize the transaction
   ** processing in some other way.
   **
   ** @param  request            the transaction request.
   **                            <br>
   **                            Allowed object is {@link Request}.
   ** @param  response           the transaction responseinto which the response
   **                            is written.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  protected void handleTransaction(final Request request, final Response response)
    throws IOException {

    response.request = request;
    if (preprocessTransaction(request, response))
      handleMethod(request, response);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preprocessTransaction
  /**
   ** Preprocesses a transaction, performing various validation checks and
   ** required special header handling, possibly returning an appropriate
   ** response.
   **
   ** @param  request            the transaction request.
   **                            <br>
   **                            Allowed object is {@link Request}.
   ** @param  response           the transaction response into which the content
   **                            is written.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @return                    whether further processing should be performed
   **                            on the transaction.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  protected boolean preprocessTransaction(final Request request, final Response response)
    throws IOException {

    // validate request
    if (request.version.equals("HTTP/1.1")) {
      if (!request.header.contains("Host")) {
        // RFC2616#14.23: missing Host header gets 400
        response.sendError(400, "Missing required Host header");
        return false;
      }
      // return a continue response before reading body
      String expect = request.header.get("Expect");
      if (expect != null) {
        if (expect.equalsIgnoreCase("100-continue")) {
          Response tempResp = new Response(response.content());
          tempResp.sendHeader(100);
          response.content().flush();
        }
        else {
          // RFC2616#14.20: if unknown expect, send 417
          response.sendError(417);
          return false;
        }
      }
    }
    else if (request.version.equals("HTTP/1.0") || request.version.equals("HTTP/0.9")) {
      // RFC2616#14.10 - remove connection headers from older versions
      for (String token : Support.splitElements(request.header.get("Connection"), false))
        request.header.remove(token);
    }
    else {
      response.sendError(400, "Unknown version: " + request.version);
      return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleMethod
  /**
   ** Handles a transaction according to the request method.
   **
   ** @param  request            the transaction request.
   **                            <br>
   **                            Allowed object is {@link Request}.
   ** @param  response           the transaction response into which the content
   **                            is written.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  protected void handleMethod(final Request request, final Response response)
    throws IOException {

    // RFC 2616#5.1.1 - GET and HEAD must be supported
    if (request.method.equals("GET") || request.context().handlers.containsKey(request.method)) {
      // method is handled by context handler (or 404)
      serve(request, response);
    }
    // default HEAD handler
    else if (request.method.equals("HEAD")) {
       // identical to a GET
      request.method   = "GET";
      // process normally but discard body
      response.discard = true;
      serve(request, response);
    }
    // default TRACE handler
    else if (request.method.equals("TRACE")) {
      handleTrace(request, response);
    }
    else {
      Set<String> methods = new LinkedHashSet<String>();
      // built-in methods
      methods.addAll(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));
      // "*" is a special server-wide (no-context) request supported by OPTIONS
      boolean isServerOptions = request.uri.getPath().equals("*") && request.method.equals("OPTIONS");
      methods.addAll(isServerOptions ? request.virtualHost().method : request.context().handlers.keySet());
      response.header.add("Allow", Support.join(", ", methods));
      // default OPTIONS handler
      if (request.method.equals("OPTIONS")) {
        // RFC2616#9.2
        response.header.add("Content-Length", "0");
        response.sendHeader(200);
      }
      else if (request.virtualHost().method.contains(request.method)) {
        // supported by server, but not this context (nor built-in)
        response.sendHeader(405);
      }
      else {
        // unsupported method
        response.sendError(501);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleTrace
  /**
   ** Handles a TRACE method request.
   **
   ** @param  request            the transaction request.
   **                            <br>
   **                            Allowed object is {@link Request}.
   ** @param  response           the transaction response into which the content
   **                            is written.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  protected void handleTrace(final Request request, final Response response)
    throws IOException {

    response.sendHeader(200, -1, -1, null, "message/http", null);
    final OutputStream output = response.content();
    output.write(Support.bytes("TRACE ", request.uri.toString(), " ", request.version));
    output.write(CRLF);
    request.header.writeTo(output);
    Support.transfer(request.content, output, -1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleTrace
  /**
   ** Serves the content for a request by invoking the context handler for the
   ** requested context (path) and HTTP method.
   **
   ** @param  request            the transaction request.
   **                            <br>
   **                            Allowed object is {@link Request}.
   ** @param  response           the transaction response into which the content
   **                            is written.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  protected void serve(Request request, Response response)
    throws IOException {

    // get context handler to handle request
    ContextHandler handler = request.context().handlers.get(request.method);
    if (handler == null) {
      response.sendError(404);
      return;
    }

    // serve request
    int status = 404;
    // add directory index if necessary
    String path = request.uri.getPath();
    if (path.endsWith("/")) {
      String index = request.virtualHost().index;
      if (index != null) {
        request.path(path + index);
        status = handler.serve(request, response);
        request.path(path);
      }
    }
    if (status == 404)
      status = handler.serve(request, response);
    if (status > 0)
      response.sendError(status);
  }
}