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
    Subsystem   :   Metadata Store Library

    File        :   Metadata.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Metadata.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.platform.core.mds;

import java.util.Objects;

import java.io.Reader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.nio.charset.Charset;

import org.xml.sax.InputSource;

import oracle.mds.config.PConfig;
import oracle.mds.config.MDSConfig;
import oracle.mds.config.MDSConfigurationException;

import oracle.mds.core.MDSSession;
import oracle.mds.core.MDSInstance;
import oracle.mds.core.IsolationLevel;
import oracle.mds.core.SessionOptions;

import oracle.mds.exception.MDSException;
import oracle.mds.exception.MDSRuntimeException;

import oracle.mds.naming.DocumentName;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.MetadataStore;

import oracle.mds.persistence.stores.db.DBMetadataStore;

import oracle.hst.platform.core.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// class Metadata
// ~~~~~ ~~~~~~~~
/**
 ** The client wrapper of a connection to a Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Metadata {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private static final String CATEGORY = Metadata.class.getName();
  private static final Logger LOGGER   = Logger.create(CATEGORY);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Stream
  // ~~~~~ ~~~~~~
  /**
   ** The <code>Stream</code> implements the base functionality of an Oracle
   ** Metadata Service Document Stream.
   */
  private static class Stream extends InputStream {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Reader  reader;
    final String  encoding;

    char[]        chars;
    int           length;
    char[]        surrogate = new char[2];
    byte[]        bytes;
    int           offset;
    boolean       eof       = false;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Stream</code> that fetches a metadata document
     ** through the specified {@link Reader} and honor the specified
     ** <code>encoding</code>.
     **
     ** @param  reader           the {@link Reader} to the metadata document.
     **                          be used as the template for parsing.
     **                          <br>
     **                          Allowed object is {@link Reader}.
     ** @param  encoding         the character encoding for a byte stream.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @throws NullPointerException if <code>reader</code> or
     **                              <code>encoding</code> is <code>null</code>.
     */
    private Stream(final Reader reader, final String encoding)
      throws RuntimeException {

      // ensure inheritance
      super();

      // prevent bogus input
      if (Charset.forName(Objects.requireNonNull(encoding)) == null)
        throw new RuntimeException("No Charset for '" + encoding + '\'');

      // initialize instance attributes
      this.reader = reader;
      this.encoding = encoding;
      this.length = 8192;
      this.chars = new char[this.length];
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (InputStream)
    /**
     ** Reads the next byte of data from the input stream.
     ** <br>
     ** The value byte is returned as an <code>int</code> in the range
     ** <code>0</code> to <code>255</code>. If no byte is available because the
     ** end of the stream has been reached, the value <code>-1</code> is
     ** returned.
     ** <br>
     ** This method blocks until input data is available, the end of the stream
     ** is detected, or an exception is thrown.
     **
     ** @return                  the next byte of data, or <code>-1</code> if
     **                          the end of the stream is reached.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public int read()
      throws IOException {

      byte[] buf = new byte[1];
      int    n = read(buf, 0, 1);
      if (n == -1) {
        return -1;
      }
      return buf[0] & 0xFF;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (overridden)
    /**
     ** Reads up to <code>length</code> bytes of data from the input stream into
     ** an array of bytes. An attempt is made to read as many as
     ** <code>length</code> bytes, but a smaller number may be read.
     ** <br>
     ** The number of bytes actually read is returned as an integer.
     ** <p>
     ** This method blocks until input data is available, end of file is
     ** detected, or an exception is thrown.
     ** <p>
     ** If <code>length</code> is zero, then no bytes are read and
     ** <code>0</code> is returned; otherwise, there is an attempt to read at
     ** least one byte. If no byte is available because the stream is at end of
     ** file, the value <code>-1</code> is returned; otherwise, at least one
     ** byte is read and stored into <code>buffer</code>.
     ** <p>
     ** The first byte read is stored into element <code>buffer[offset]</code>,
     ** the next one into <code>buffer[offset + 1]</code>, and so on. The number
     ** of bytes read is, at most, equal to <code>length</code>. Let <i>k</i> be
     ** the number of bytes actually read; these bytes will be stored in
     ** elements <code>buffer[offset]</code> through
     ** <code>buffer[offset + </code><i>k</i><code>-1]</code>, leaving elements
     ** <code>buffer[offset + </code><i>k</i><code>]</code> through
     ** <code>buffer[offset + length - 1]</code> unaffected.
     ** <p>
     ** In every case, elements <code>buffer[0]</code> through
     ** <code>buffer[offset]</code> and elements
     ** <code>buffer[offset + length]</code> through
     ** <code>buffer[buffer.length - 1]</code> are unaffected.
     **
     ** @param  buffer           the buffer into which the data is read.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     ** @param  offset           the start offset in array <code>b</code> at
     **                          which the data is written.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  length           the maximum number of bytes to read.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the total number of bytes read into the buffer,
     **                          or <code>-1</code> if there is no more data
     **                          because the end of the stream has been reached.
     **                          <br>
     **                          Possible object is array of <code>int</code>.
     **
     ** @throws IOException               if the first byte cannot be read for
     **                                   any reason other than end of file, or
     **                                   if the input stream has been closed,
     **                                   or if some other I/O error occurs.
     ** @throws NullPointerException      if <code>b</code> is
     **                                   <code>null</code>.
     ** @throws IndexOutOfBoundsException if <code>off</code> is negative,
     **                                   <code>len</code> is negative, or
     **                                   <code>len</code> is greater than
     **                                   <code>b.length - off</code>.
     */
    @Override
    public int read(final byte[] buffer, int offset, int length)
      throws IOException {

      // prevent bogus input
      if (buffer == null)
        throw new NullPointerException();

      // prevent bogus input
      if ((offset < 0) || (offset > buffer.length) || (length < 0) || (offset + length > buffer.length) || (offset + length < 0))
        throw new IndexOutOfBoundsException();

      // prevent bogus input
      if (length == 0)
        return 0;

      // prevent bogus state
      if (this.eof)
        return -1;

      int n = -1;
      if (this.bytes != null) {
        n = this.bytes.length - this.offset;
        if (n >= length) {
          System.arraycopy(this.bytes, this.offset, buffer, offset, length);
          if (n == length) {
            this.bytes = null;
          }
          this.offset += length;
          return length;
        }
        System.arraycopy(this.bytes, this.offset, buffer, offset, n);
        length -= n;
        offset += n;
        this.bytes = null;
        this.offset = 0;
      }

      if ((this.chars == null) || (this.chars.length < length)) {
        this.chars = new char[length];
      }

      this.length = this.reader.read(this.chars);
      if (this.length == -1) {
        this.eof = true;
        return n;
      }

      String surrogateStr = null;
      // determines if the first char value is a Unicode low-surrogate code unit
      // (also known as trailing-surrogate code unit)
      if (Character.isLowSurrogate(this.chars[0])) {
        this.surrogate[1] = this.chars[0];
        // determines whether the surrogatePair values is a valid Unicode
        // surrogate pair
        if (Character.isSurrogatePair(this.surrogate[0], this.surrogate[1])) {
          surrogateStr = new String(this.surrogate, 0, 2);
        }
        this.surrogate = new char[2];
      }

      int isLastHighSurrogate = 0;
      // determines if the last char value is a Unicode high-surrogate code unit
      // (also known as leading-surrogate code unit)
      if (Character.isHighSurrogate(this.chars[(this.length - 1)])) {
        this.surrogate[0] = this.chars[(this.length - 1)];
        isLastHighSurrogate = 1;
      }

      String str = "";
      if (surrogateStr != null) {
        if (this.length > 1) {
          str = new String(this.chars, 1, this.length - isLastHighSurrogate);
          str = surrogateStr.concat(str);
        }
        else {
          str = surrogateStr;
        }
      }
      else {
        str = new String(this.chars, 0, this.length - isLastHighSurrogate);
      }
      this.bytes = str.getBytes(this.encoding);
      this.offset = 0;

      if (this.bytes.length <= length) {
        int byteCount = this.bytes.length;
        System.arraycopy(this.bytes, 0, buffer, offset, byteCount);
        this.bytes = null;
        if (n != -1) {
          byteCount += n;
        }
        return byteCount;
      }

      System.arraycopy(this.bytes, 0, buffer, offset, length);
      this.offset += length;

      if (n != -1) {
        length += n;
      }
      return length;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Metadata</code> handler that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Metadata() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close the source or destination of data preventing propagation of
   ** <code>Exception</code>/.
   **
   ** @param  source             the source or destination to be closed.
   **                            <br>
   **                            Allowed object is {@link Closable}.
   */
  public static void close(final Closeable source) {
    if (source != null) {
      try {
        source.close();
      }
      catch (Throwable e) {
        e.printStackTrace();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   session
  /**
   ** Establish a connection to the Metadata Store and creates the connection
   ** used during task execution.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method, except {@link #aquire(String, String, String)}.
   **
   ** @param  instance           the instance to the metadata store.
   **                            <br>
   **                            Allowed object is {@link MDSInstance}.
   **
   ** @throws MetadataException  if an error occurs attempting to establish a
   **                            connection.
   */
  public static MDSSession session(final MDSInstance instance)
    throws MetadataException {

    final String method = "session";
    LOGGER.entering(CATEGORY, method);
    try {
      // create a session to the Metadata Store using the session options and
      // without any specific state handlers
      return instance.createSession(new SessionOptions(IsolationLevel.READ_COMMITTED, null, null), null);
    }
    finally {
      LOGGER.exiting(CATEGORY, method);
    }
  }

  public static PDocument retrieve(final MDSSession session, final String path) {
    final String method = "retrieveFile";
    LOGGER.entering(CATEGORY, method);

    PDocument document = null;
    try {
      document = session.getPersistenceManager().getDocument(session.getPContext(), DocumentName.create(path));
    }
    catch (Exception e) {
      LOGGER.throwing(CATEGORY, method, e);
    }
    LOGGER.debug(CATEGORY, method, "Returning document: " + document);
    return document;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Creates the instance to the metadata store leveraging the provided
   ** <code>dataSource</code> and will be accesssible by the provided
   ** <code>name</code>.
   ** <p>
   ** If an instance already existe for the provided <code>name</code> it will
   ** be reused to create a session
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   **
   ** @param  name               the name of the connection registered in
   **                            the connection cache.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  config             the {@link MDSConfig} used to perform the
   **                            operation.
   **                            <br>
   **                            Possible object is {@link MDSConfig}.
   **
   ** @return                    the instance to the metadata store.
   **                            <br>
   **                            Possible object is {@link MDSInstance}.
   **
   ** @throws MetadataException  if the connection instance cannot be created.
   */
  public static MDSInstance aquire(final String name, final String dataSource, final String partition)
    throws MetadataException {

    MDSInstance instance = MDSInstance.getInstance(name);
    if (instance != null)
       return instance;

    // create one if ther is no instance for this name
    final String method = "aquireInstance";
    LOGGER.entering(CATEGORY, method, name, dataSource, partition);
    // lookup the name of the instance in the instance cache
    try {
      // create a metadata store that leverage a database
      final MetadataStore store    = new DBMetadataStore(dataSource, false, true, partition);
      final MDSConfig     config   = new MDSConfig(null, new PConfig(store), null);
      instance = MDSInstance.getOrCreateInstance(name, config);
    }
    catch (MDSConfigurationException e) {
      LOGGER.throwing(CATEGORY, method, new MetadataException(e));
    }
    catch (MDSException e) {
      LOGGER.throwing(CATEGORY, method, new MetadataException(e));
    }
    finally {
      LOGGER.exiting(CATEGORY, method, name, dataSource, partition);
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release all resources aquired for the metadata store.
   **
   ** @param  name               the name of the connection registered in
   **                            the connection cache.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws MetadataException  if the connection instance cannot be released.
   */
  public static void release(final String name)
    throws MetadataException {

    if (MDSInstance.getInstance(name) == null)
      return;

    // release an instance from the metadata store
    final String method = "releaseInstance";
    LOGGER.entering(CATEGORY, method, name);
    try {
      MDSInstance.releaseInstance(name);
    }
    catch (MDSRuntimeException e) {
      LOGGER.throwing(CATEGORY, method, new MetadataException(e));
    }
    finally {
      LOGGER.exiting(CATEGORY, method, name);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentStream
  /**
   ** Copies the given @link InputSource} <code>source</code> to an
   ** corresponding {@link InputStream}.
   **
   ** @param  source             the source to process.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    the byte stream copied from
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link InputStream}.
   **
   ** @throws MetadataException  if an I/O error occured.
   */
  public static InputStream documentStream(final InputSource source)
    throws MetadataException {

    InputStream stream = source.getByteStream();
    if (stream == null)
      stream = new Stream(source.getCharacterStream(), source.getEncoding() == null ? "UTF-8" : source.getEncoding());

    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    final byte[] buffer = new byte[1024];
    try {
      int len;
      while ((len = stream.read(buffer)) > -1) {
        bos.write(buffer, 0, len);
       }
      bos.flush();
      stream.close();
      stream = null;
    }
    catch (IOException e) {
      throw new MetadataException("Unhandled::", e);
    }
    finally {
      close(source.getCharacterStream());
      close(stream);
    }
    return new ByteArrayInputStream(bos.toByteArray());
  }
}