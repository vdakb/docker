package bka.iam.identity.scim.extension.rest;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URI;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.logging.Logger;
import oracle.hst.platform.core.utility.CollectionUtility;
import oracle.hst.platform.core.utility.PropertyUtility;
import oracle.hst.platform.rest.ServiceBundle;
import oracle.hst.platform.rest.ServiceMessage;
///////////////////////////////////////////////////////////////////////////////
// class LoggerFeature
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This feature enables logging request and/or response on client-side and/or
 ** server-side depending on the context's {@link RuntimeType}.
 ** <p>
 ** The feature may be register programmatically like other features by calling
 ** any of {@link javax.ws.rs.core.Configurable} {@code register(...)} method,
 ** i.e. {@link javax.ws.rs.core.Configurable#register(Class)} or by setting any
 ** of the configuration property listed bellow.
 ** <p>
 ** Common configurable properties applies for both client and server and are
 ** following:
 ** <ul>
 **   <li>{@link #LOGGER_CATEGORY}
 **   <li>{@link #LOGGER_LEVEL}
 **   <li>{@link #LOGGER_VERBOSITY}
 **   <li>{@link #LOGGER_ENTITY_SIZE}
 ** </ul>
 ** If any of the configuration value is not set, following default values are
 ** applied:
 ** <ul>
 **   <li>logger name: <code>oracle.iam.identity.icf.connector.http.LoggerFeature</code>
 **   <li>logger level: {@link Level#FINE}
 **   <li>verbosity: {@link Verbosity#PAYLOAD_TEXT}
 **   <li>maximum entity size: {@value #ENTITY_SIZE_DEFAULT}
 ** </ul>
 ** Server configurable properties:
 ** <ul>
 **   <li>{@link #LOGGER_CATEGORY_SERVER}
 **   <li>{@link #LOGGER_LEVEL_SERVER}
 **   <li>{@link #LOGGER_VERBOSITY_SERVER}
 **   <li>{@link #LOGGER_ENTITY_SIZE_SERVER}
 ** </ul>
 ** Client configurable properties:
 ** <ul>
 **   <li>{@link #LOGGER_CATEGORY_CLIENT}
 **   <li>{@link #LOGGER_LEVEL_CLIENT}
 **   <li>{@link #LOGGER_VERBOSITY_CLIENT}
 **   <li>{@link #LOGGER_ENTITY_SIZE_CLIENT}
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LoggerFeature implements Feature {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Default logger name to log request and response messages. */
  public static final String          NAME_DEFAULT              = LoggerFeature.class.getName();

  /**
   ** Default logger level which will be used for logging request and response
   ** messages.
   */
  public static final String          LEVEL_DEFAULT             = Level.FINEST.getName();

  /** Default maximum entity bytes to be logged. */
  public static final int             ENTITY_SIZE_DEFAULT       = 8 * 1024;

  /** Default verbosity for entity logging. See {@link Verbosity}. */
  public static final Verbosity       VERBOSITY_DEFAULT         = Verbosity.PAYLOAD_TEXT;

  /** The entity stream property */
  static final String                 LOGGER_ENTITY             = LoggerFeature.class.getName() + ".entityLogger";

  /** Logging record id property */
  static final String                 LOGGING_ID                = LoggerFeature.class.getName() + ".id";

  /** Prefix will be printed before notifications */
  static final String                 PREFIX_NOTICE             = "*** ";

  /** Prefix will be printed before requests */
  static final String                 PREFIX_REQUEST            = "--> ";

  /** Prefix will be printed before response */
  static final String                 PREFIX_RESPONSE           = "<-- ";

  private static final String         PREFIX_LOGGER             = "oracle.gws.logger";
  private static final String         PREFIX_CLIENT             = "oracle.gws.logger.client";
  private static final String         PREFIX_SERVER             = "oracle.gws.logger.server";

  private static final String         POSTFIX_CATEGORY          = ".logger.name";
  private static final String         POSTFIX_LEVEL             = ".logger.level";
  private static final String         POSTFIX_VERBOSITY         = ".verbosity";
  private static final String         POSTFIX_SIZE              = ".entity.size";

  /** Common logger name property */
  public static final String          LOGGER_CATEGORY           = PREFIX_LOGGER + POSTFIX_CATEGORY;
  public static final String          LOGGER_CATEGORY_CLIENT    = PREFIX_CLIENT + POSTFIX_CATEGORY;
  public static final String          LOGGER_CATEGORY_SERVER    = PREFIX_SERVER + POSTFIX_CATEGORY;

  /** Common logger level property */
  public static final String          LOGGER_LEVEL              = PREFIX_LOGGER + POSTFIX_LEVEL;
  public static final String          LOGGER_LEVEL_CLIENT       = PREFIX_CLIENT + POSTFIX_LEVEL;
  public static final String          LOGGER_LEVEL_SERVER       = PREFIX_SERVER + POSTFIX_LEVEL;

  /** Common property for configuring a verbosity of entity */
  public static final String          LOGGER_VERBOSITY          = PREFIX_LOGGER + POSTFIX_VERBOSITY;
  public static final String          LOGGER_VERBOSITY_CLIENT   = PREFIX_CLIENT + POSTFIX_VERBOSITY;
  public static final String          LOGGER_VERBOSITY_SERVER   = PREFIX_SERVER + POSTFIX_VERBOSITY;

  /**
   ** Common property for configuring a maximum number of bytes of entity to be
   ** logged.
   */
  public static final String          LOGGER_ENTITY_SIZE        = PREFIX_LOGGER + POSTFIX_SIZE;
  public static final String          LOGGER_ENTITY_SIZE_CLIENT = PREFIX_CLIENT + POSTFIX_SIZE;
  public static final String          LOGGER_ENTITY_SIZE_SERVER = PREFIX_SERVER + POSTFIX_SIZE;

  private static final Set<MediaType> READABLE_TYPES            = CollectionUtility.unmodifiableSet(
    new MediaType[] {
      //      add(TEXT_MEDIA_TYPE);
      MediaType.APPLICATION_ATOM_XML_TYPE
    , MediaType.APPLICATION_FORM_URLENCODED_TYPE
    , MediaType.APPLICATION_JSON_TYPE
    , MediaType.APPLICATION_SVG_XML_TYPE
    , MediaType.APPLICATION_XHTML_XML_TYPE
    , MediaType.APPLICATION_XML_TYPE
    }
  );

  private static final Comparator<Map.Entry<String, List<String>>> COMPARATOR = new Comparator<Map.Entry<String, List<String>>>() {
    @Override
    public int compare(final Map.Entry<String, List<String>> o1, final Map.Entry<String, List<String>> o2) {
      return o1.getKey().compareToIgnoreCase(o2.getKey());
    }
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Logger    logger;
  private final Verbosity verbosity;
  private final Integer   size;
  private final Level     level;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Verbosity
  // ~~~~~ ~~~~~~~~~
  /**
   ** Determines how detailed message will be logged.
   ** <ul>
   **   <li>The lowest verbosity ({@link #HEADERS_ONLY}) will log only
   **       request/response headers.
   **   <li>The medium verbosity will log request/response headers, as well as
   **       an entity if considered a readable text. See {@link #PAYLOAD_TEXT}.
   **   <li>The highest verbosity will log all types of an entity (besides the
   **       request/response headers.
   ** </ul>
   ** <b>Note</b>
   ** <br>
   ** The entity is logged up to the maximum number specified in any of the
   ** following constructors
   ** <code>LoggerFeature(Loggable, Integer)</code>,
   ** <code>LoggerFeature(Loggable, Level, Verbosity, Integer)</code> or by some
   ** of the feature's properties (see {@link #LOGGER_ENTITY_SIZE},
   ** {@link #LOGGER_ENTITY_SIZE_CLIENT}, {@link #LOGGER_ENTITY_SIZE_SERVER}.
   */
  public enum Verbosity {
     /**
      ** Only content of HTTP headers is logged. No message payload data are
      ** logged.
      */
      HEADERS_ONLY,

    /**
     ** Content of HTTP headers as well as entity content of textual media
     ** types is logged. Following is the list of media types that are
     ** considered textual for the logging purposes:
     ** <ul>
     **   <li><code>text/*</code>
     **   <li><code>application/atom+xml</code>
     **   <li><code>application/json</code>
     **   <li><code>application/svg+xml</code>
     **   <li><code>application/x-www-form-urlencoded</code>
     **   <li><code>application/xhtml+xml</code>
     **   <li><code>application/xml</code>
     ** </ul>
     */
    PAYLOAD_TEXT,

    /**
     ** Full verbose logging.
     ** <br>
     ** Content of HTTP headers as well as any message payload content will be
     ** logged.
     */
    PAYLOAD_ANY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Interceptor
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** An interceptor that logs an entity if configured so and provides a common
   ** logic for {@link Client} and {@link Server} logging filter.
   */
  class Interceptor implements WriterInterceptor {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Logger     logger;
    final Level      level;
    final Integer    size;
    final Verbosity  verbosity;

    final AtomicLong id         = new AtomicLong(0);

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Stream
    // ~~~~~ ~~~~~~
    /**
     ** Helper class used to log an entity to the output stream up to the
     ** specified maximum number of bytes.
     */
    class Stream extends FilterOutputStream {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final StringBuilder         capture;
      private final ByteArrayOutputStream output  = new ByteArrayOutputStream();

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Creates <code>Stream</code> with the entity and the underlying output
       ** stream as parameters.
       **
       ** @param  capture        contains the entity to log.
       ** @param  stream         the underlying output stream.
       */
      Stream(final StringBuilder capture, final OutputStream stream) {
        // ensure inheritance
        super(stream);

        // initialize instance attributes
        this.capture = capture;
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: capture
      StringBuilder capture(final Charset charset) {
        // write entity to the capture buffer
        final byte[] entity = this.output.toByteArray();
        this.capture.append(new String(entity, 0, Math.min(entity.length, Interceptor.this.size), charset));
        if (entity.length > Interceptor.this.size) {
          this.capture.append(ServiceBundle.string(ServiceMessage.LOGGER_ELIPSE_MORE));
        }
        return this.capture.append('\n');
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by funtionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: write
      /**
       ** Writes a portion of the capture buffer.
       **
       ** @param  length         the number of characters to write
       **
       ** @throws IOException    if an I/O error occurs
       */
      public final void write(final int length)
        throws IOException {

        if (this.output.size() <= Interceptor.this.size)
          this.output.write(length);

        this.out.write(length);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a logging filter with custom logger and entity logging turned
     ** on, but potentially limiting the size of entity to be buffered and
     ** logged.
     **
     ** @param  logger           the logger to log messages to.
     ** @param  level            the level at which the messages will be logged.
     ** @param  verbosity        verbosity of the logged messages.
     ** @param  size             the maximum number of entity bytes to be logged
     **                          (and buffered) - if the entity is larger,
     **                          logging filter will print (and buffer in
     **                          memory) only the specified number of bytes and
     **                          print "...and more..." string at the end.
     **                          Negative values are interpreted as zero.
     */
    Interceptor(final Logger logger, final Level level, final Verbosity verbosity, final Integer size) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.logger    = logger;
      this.level     = level;
      this.verbosity = verbosity;
      this.size      = Math.max(0, size);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: aroundWriteTo
    /**
     ** Interceptor method wrapping calls to <code>MessageBodyWriter.writeTo(T, java.lang.Class&lt;?&gt;, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap&lt;java.lang.String, java.lang.Object&gt;, java.io.OutputStream)</code>
     ** method. The parameters of the wrapped method called are available from
     ** <code>context</code>.
     ** <br>
     ** Implementations of this method SHOULD explicitly call
     ** {@link WriterInterceptorContext.proceed()} to invoke the next
     ** interceptor in the chain, and ultimately the wrapped
     ** <code>MessageBodyWriter.writeTo</code> method.
     **
     ** @param  context          the invocation context.
     **
     ** @throws IOException      if an IO error arises or is thrown by the
     **                          wrapped <code>MessageBodyWriter.writeTo</code>
     **                          method.
     */
    @Override
    public void aroundWriteTo(final WriterInterceptorContext context)
      throws IOException {

      final Stream stream = (Stream)context.getProperty(LOGGER_ENTITY);
      context.proceed();
      if (this.logger.loggable(this.level) && printable(this.verbosity, context.getMediaType())) {
        if (stream != null) {
          log(stream.capture(charset(context.getMediaType())));
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by funtionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: log
    /**
     ** Logs a {@link StringBuilder} parameter at required level.
     **
     ** @param  capture          the message capture buffer to log.
     */
    void log(final StringBuilder capture) {
      if (this.logger != null && this.logger.loggable(this.level)) {
        this.logger.log(this.level, capture.toString());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: printRequest
    void printRequest(final StringBuilder b, final String note, final long id, final String method, final URI uri) {
      print(b, id).append(ServiceBundle.string(ServiceMessage.LOGGER_THREAD_NAME, note, Thread.currentThread().getName()));
      print(b, id).append(PREFIX_REQUEST).append(method).append(" ").append(uri.toASCIIString()).append('\n');
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: printResponse
    void printResponse(final StringBuilder b, final String note, final long id, final int status) {
      print(b, id).append(ServiceBundle.string(ServiceMessage.LOGGER_THREAD_NAME, note, Thread.currentThread().getName()));
      print(b, id).append(PREFIX_RESPONSE).append(Integer.toString(status)).append('\n');
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: printHeader
    void printHeader(final StringBuilder b, final long id, final String prefix, final MultivaluedMap<String, String> headers) {
      for (final Map.Entry<String, List<String>> headerEntry : headers(headers.entrySet())) {
        final List<?> value  = headerEntry.getValue();
        final String  header = headerEntry.getKey();
        if (value.size() == 1) {
          print(b, id).append(prefix).append(header).append(": ").append(value.get(0)).append('\n');
        }
        else {
          final StringBuilder sb  = new StringBuilder();
          boolean             add = false;
          for (final Object cursor : value) {
            if (add) {
              sb.append(',');
            }
            add = true;
            sb.append(cursor);
          }
          print(b, id).append(prefix).append(header).append(": ").append(sb.toString()).append('\n');
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: printEntity
    InputStream printEntity(final StringBuilder b, final long id, final String prefix, InputStream stream, final Charset charset)
      throws IOException {

      if (!stream.markSupported())
        stream = new BufferedInputStream(stream);

      stream.mark(this.size + 1);
      final byte[] entity = new byte[this.size + 1];
      final int    size   = stream.read(entity);
      print(b, id).append(prefix).append(new String(entity, 0, Math.min(size, this.size), charset));
      if (size > this.size)
        print(b, id).append(prefix).append(ServiceBundle.string(ServiceMessage.LOGGER_ELIPSE_MORE));

      b.append('\n');
      stream.reset();
      return stream;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: print
    /**
     ** Print the sequence of the request/response flow to the capture buffer.
     **
     ** @param  capture          the {@link StringBuilder} capture buffer.
     ** @param  sequence         the sequence of the request/response flow.
     **
     ** @return                  the {@link StringBuilder} capture buffer
     **                          containing the the request/response flow
     **                          sequence.
     */
    private StringBuilder print(final StringBuilder capture, final long sequence) {
      return capture.append(Long.toString(sequence)).append(" ");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: headers
    /**
     ** Returns the headers of a request or a response sorted in a
     ** lexicographically sorted manner.
     **
     ** @param  headers          the {@link Set} of HTTP headers.
     **
     ** @return                  the same {@link Set} of HTTP headers
     **                          lexicographically sorted.
     */
    Set<Map.Entry<String, List<String>>> headers(final Set<Map.Entry<String, List<String>>> headers) {
      final TreeSet<Map.Entry<String, List<String>>> sorted = new TreeSet<Map.Entry<String, List<String>>>(COMPARATOR);
      sorted.addAll(headers);
      return sorted;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Client
  // ~~~~~ ~~~~~~
  /**
   ** Client filter logs requests and responses to specified logger, at
   ** required level, with entity or not.
   ** <p>
   ** The filter is registered in
   ** {@link LoggerFeature#configure(FeatureContext)} and can be used on client
   ** side only. The priority is set to the minimum value, which means that
   ** filter is called as the last filter when request is sent and similarly as
   ** the first filter when the response is received, so request and response is
   ** logged as sent or as received.
   */
  final class Client extends    Interceptor
                     implements ClientRequestFilter
                     ,          ClientResponseFilter {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a logging filter with custom logger and entity logging turned
     ** on, but potentially limiting the size of entity to be buffered and
     ** logged.
     **
     ** @param  logger           the logger to log messages to.
     ** @param  level            the level at which the messages will be logged.
     ** @param  verbosity        verbosity of the logged messages.
     ** @param  size             the maximum number of entity bytes to be logged
     **                          (and buffered) - if the entity is larger,
     **                          logging filter will print (and buffer in
     **                          memory) only the specified number of bytes and
     **                          print "...and more..." string at the end.
     **                          Negative values are interpreted as zero.
     */
    Client(final Logger logger, final Level level, final Verbosity verbosity, final Integer size) {
      // ensure inheritance
      super(logger, level, verbosity, size);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter (ClientRequestFilter)
    /**
     ** Filter method called before a request has been dispatched to a client
     ** transport layer
     ** <br>
     ** Filters in the filter chain are ordered according to their
     ** javax.annotation.Priority class-level annotation value.
     **
     ** @param  context          the {@link ClientRequestContext request context}.
     **
     ** @throws IOException      if an I/O exception occurs.
     */
    @Override
    public void filter(final ClientRequestContext context)
      throws IOException {

      if (!this.logger.loggable(this.level))
        return;

      final long sequence = this.id.incrementAndGet();
      context.setProperty(LOGGING_ID, sequence);

      final StringBuilder capture = new StringBuilder();
      printRequest(capture, ServiceBundle.string(ServiceMessage.LOGGER_CLIENT_REQUEST), sequence, context.getMethod(), context.getUri());
      printHeader(capture, sequence, PREFIX_REQUEST, context.getStringHeaders());

      if (context.hasEntity() && printable(this.verbosity, context.getMediaType())) {
        final OutputStream stream = new Stream(capture, context.getEntityStream());
        context.setEntityStream(stream);
        context.setProperty(LOGGER_ENTITY, stream);
        // not calling log(capture) here - it will be called by the interceptor
      }
      else {
        log(capture);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter (ClientResponseFilter)
    /**
     ** Filter method called after a response has been provided for a request
     ** (either by a {@link ClientRequestFilter request filter} or when the HTTP
     ** invocation returns.
     ** <br>
     ** Filters in the filter chain are ordered according to their
     ** javax.annotation.Priority class-level annotation value.
     **
     ** @param  request          the {@link ClientRequestContext request context}.
     ** @param  response         the {@link ClientResponseContext response context}.
     **
     ** @throws IOException      if an I/O exception occurs.
     */
    @Override
    public void filter(final ClientRequestContext request, final ClientResponseContext response)
      throws IOException {

      if (!this.logger.loggable(this.level))
        return;

      final Object requestId = request.getProperty(LOGGING_ID);
      final long   sequence  = requestId != null ? (Long)requestId : this.id.incrementAndGet();

      final StringBuilder capture = new StringBuilder();

      printResponse(capture, ServiceBundle.string(ServiceMessage.LOGGER_CLIENT_RESPONSE), sequence, response.getStatus());
      printHeader(capture, sequence, PREFIX_RESPONSE, response.getHeaders());

      if (response.hasEntity() && printable(this.verbosity, response.getMediaType())) {
        response.setEntityStream(printEntity(capture, sequence, PREFIX_RESPONSE, response.getEntityStream(), charset(response.getMediaType())));
      }
      log(capture);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** Server filter logs requests and responses to specified logger, at required
   ** level, with entity or not.
   ** <p>
   ** The filter is registered in
   ** {@link LoggerFeature#configure(FeatureContext)} and can be used on server
   ** side only. The priority is set to the maximum  value, which means that
   ** filter is called as the first filter when request arrives  and similarly
   ** as the last filter when the response is dispatched, so request and
   ** response is logged as arrives or as dispatched.
   */
  final class Server extends    Interceptor
                     implements ContainerRequestFilter
                     ,          ContainerResponseFilter{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a logging filter with custom logger and entity logging turned
     ** on, but potentially limiting the size of entity to be buffered and
     ** logged.
     **
     ** @param  logger           the logger to log messages to.
     ** @param  level            the level at which the messages will be logged.
     ** @param  verbosity        verbosity of the logged messages.
     ** @param  size             the maximum number of entity bytes to be logged
     **                          (and buffered) - if the entity is larger,
     **                          logging filter will print (and buffer in
     **                          memory) only the specified number of bytes and
     **                          print "...and more..." string at the end.
     **                          Negative values are interpreted as zero.
     */
    Server(final Logger logger, final Level level, final Verbosity verbosity, final Integer size) {
      // ensure inheritance
      super(logger, level, verbosity, size);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter (ContainerRequestFilter)
    /**
     ** Filter method called before a request has been dispatched to a resource.
     ** <p>
     ** Filters in the filter chain are ordered according to their
     ** javax.annotation.Priority class-level annotation value.
     ** <br>
     ** If a request filter produces a response by calling
     ** {@link ContainerRequestContext.abortWith(javax.ws.rs.core.Response)}
     ** method, the execution of the (either pre-match or post-match) request
     ** filter chain is stopped and the response is passed to the corresponding
     ** response filter chain (either pre-match or post-match).
     ** <br>
     ** For example, a pre-match caching filter may produce a response in this
     ** way, which would effectively skip any post-match request filters as well
     ** as post-match response filters.
     ** <br>
     ** <b>Note</b>
     ** <br>
     ** However that a responses produced in this manner would still be
     ** processed by the pre-match response filter chain.
     **
     ** @param  context          the {@link ContainerRequestContext request context}.
     **
     ** @throws IOException      if an I/O exception occurs.
     */
    @Override
    public void filter(final ContainerRequestContext context)
      throws IOException {

      if (!this.logger.loggable(this.level))
        return;

      final long sequence = this.id.incrementAndGet();
      context.setProperty(LOGGING_ID, id);

      final StringBuilder capture = new StringBuilder();
      printRequest(capture, ServiceBundle.string(ServiceMessage.LOGGER_SERVER_REQUEST), sequence, context.getMethod(), context.getUriInfo().getRequestUri());
      printHeader(capture, sequence, PREFIX_REQUEST, context.getHeaders());
      if (context.hasEntity() && printable(verbosity, context.getMediaType())) {
        context.setEntityStream(printEntity(capture, sequence, PREFIX_REQUEST, context.getEntityStream(), charset(context.getMediaType())));
      }
      log(capture);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter (ContainerResponseFilter)
    /**
     ** Filter method called after a response has been provided for a request
     ** (either by a {@link ContainerRequestContext request filter} or by a
     ** matched resource method.
     ** <br>
     ** Filters in the filter chain are ordered according to their
     ** javax.annotation.Priority class-level annotation value.
     **
     ** @param  request          the {@link ContainerRequestContext request context}.
     ** @param  response         the {@link ContainerResponseContext response context}.
     **
     ** @throws IOException      if an I/O exception occurs.
     */
    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response)
      throws IOException {


      if (!this.logger.loggable(this.level))
        return;

      final Object requestId = request.getProperty(LOGGING_ID);
      final long   sequence  = requestId != null ? (Long)requestId : this.id.incrementAndGet();

      final StringBuilder capture = new StringBuilder();

      printResponse(capture, ServiceBundle.string(ServiceMessage.LOGGER_SERVER_RESPONSE), sequence, response.getStatus());
      printHeader(capture, sequence, PREFIX_RESPONSE, response.getStringHeaders());

      if (response.hasEntity() && printable(this.verbosity, response.getMediaType())) {
        final OutputStream stream = new Stream(capture, response.getEntityStream());
        response.setEntityStream(stream);
        request.setProperty(LOGGER_ENTITY, stream);
        // not calling log(capture) here - it will be called by the interceptor
      }
      else {
        log(capture);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>LoggerFeature</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LoggerFeature() {
    // ensure inheritance
    this(null, null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LoggerFeature</code> which is associated with
   ** the specified {@link Loggable}.
   **
   ** @param  loggable           the {@link Loggable} to log requests and
   **                            responses.
   **                            Allowed object is {@link Loggable}.
   */
  public LoggerFeature(final Loggable loggable) {
    // ensure inheritance
    this(loggable, null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LoggerFeature</code> which is associated with
   ** the specified {@link Loggable} and verbosity.
   **
   ** @param  loggable           the {@link Loggable} to log requests and
   **                            responses.
   **                            Allowed object is {@link Loggable}.
   ** @param  verbosity          verbosity of logged messages.
   **                            Allowed object is {@link Verbosity}.
   */
  public LoggerFeature(final Loggable loggable, final Verbosity verbosity) {
    // ensure inheritance
    this(loggable, null, verbosity, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LoggerFeature</code> which is associated with
   ** the specified {@link Loggable} and maximum number of bytes of entity to
   ** log.
   **
   ** @param  loggable           the {@link Loggable} to log requests and
   **                            responses.
   **                            Allowed object is {@link Loggable}.
   ** @param   size              maximum number of entity bytes to be logged
   **                            (and buffered) - if the entity is larger,
   **                            logging filter will print (and buffer in
   **                            memory) only the specified number of bytes and
   **                            print " ...and more..." string at the end.
   **                            Negative values are interpreted as zero.
   **                            Allowed object is {@link Integer}.
   */
  public LoggerFeature(final Loggable loggable, final Integer size) {
    // ensure inheritance
    this(loggable, null, VERBOSITY_DEFAULT, size);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LoggerFeature</code> which is associated with
   ** the specified {@link Loggable}, it's level, message verbosity and maximum
   ** number of bytes of entity to log.
   **
   ** @param  loggable           the {@link Loggable} to log requests and
   **                            responses.
   **                            Allowed object is {@link Loggable}.
   ** @param  level              level on which the messages will be logged.
   **                            Allowed object is {@link Level}.
   ** @param  verbosity          verbosity of logged messages.
   **                            Allowed object is {@link Verbosity}.
   ** @param   size              maximum number of entity bytes to be logged
   **                            (and buffered) - if the entity is larger,
   **                            logging filter will print (and buffer in
   **                            memory) only the specified number of bytes and
   **                            print " ...and more..." string at the end.
   **                            Negative values are interpreted as zero.
   **                            Allowed object is {@link Integer}.
   */
  public LoggerFeature(final Loggable loggable, final Level level, final Verbosity verbosity, final Integer size) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.logger    = loggable == null ? null : loggable.logger();
    this.level     = level;
    this.verbosity = verbosity;
    this.size      = size;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure (Feature)
  /**
   ** A call-back method called when the feature is to be enabled in a given
   ** runtime configuration scope. The responsibility of the feature is to
   ** properly update the supplied runtime configuration context and return
   ** <code>true</code> if the feature was successfully enabled or
   ** <code>false</code> otherwise.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Under some circumstances the feature may decide not to enable itself,
   ** which is indicated by returning <code>false</code>. In such case the
   ** configuration context does not add the feature to the collection of
   ** enabled features and a subsequent call to Configuration.isEnabled(Feature)
   ** or Configuration.isEnabled(Class) method would return <code>false</code>.
   **
   ** @param  context            the configurable context in which the feature
   **                            should be enabled.
   **
   ** @return                    <code>true</code> if the feature was
   **                            successfully enabled, <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean configure(final FeatureContext context) {
    context.register(interceptor(context));
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable
  /**
   ** Returns <code>true<code> if entity has to be printed.
   **
   ** @param  verbosity          the configured verbosity.
   ** @param  mediaType          the media type of the payload.
   **
   ** @return                    <code>true</code> if entity has to be printed;
   **                            <code>false</code> otherwise.
   */
  static boolean printable(final Verbosity verbosity, final MediaType mediaType) {
    return verbosity == Verbosity.PAYLOAD_ANY || (verbosity == Verbosity.PAYLOAD_TEXT && readable(mediaType));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readable
  /**
   ** Returns <code>true</code> if specified {@link MediaType} is considered
   ** textual.
   ** <p>
   ** See {@link #READABLE_APP_MEDIA_TYPES}.
   **
   ** @param  mediaType          the media type of the entity.
   **
   ** @return                    <code>true</code> if specified
   **                            {@link MediaType} is considered textual.
   */
  static boolean readable(final MediaType mediaType) {
    if (mediaType != null) {
      for (MediaType readableMediaType : READABLE_TYPES) {
        if (readableMediaType.isCompatible(mediaType)) {
          return true;
        }
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   charset
  /**
   ** Returns the character set from a media type.
   ** <p>
   ** he character set is obtained from the media type parameter "charset".
   ** If the parameter is not present the {@link StringUtility#UNICODE} charset
   ** is utilized.
   **
   ** @param  mediaType          the media type.
   **
   ** @return                    the character set.
   */
  static Charset charset(final MediaType mediaType) {
    final String name = (mediaType == null) ? null : mediaType.getParameters().get(MediaType.CHARSET_PARAMETER);
    return (name == null) ? StandardCharsets.UTF_8 : Charset.forName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   interceptor
  private Interceptor interceptor(final FeatureContext context) {
    final RuntimeType    runtime    = context.getConfiguration().getRuntimeType();
    final Map<String, ?> properties = context.getConfiguration().getProperties();
    final String         category   = PropertyUtility.value(properties, runtime == RuntimeType.SERVER ? LOGGER_CATEGORY_SERVER    : LOGGER_CATEGORY_CLIENT,    PropertyUtility.value(properties, LOGGER_CATEGORY,    this.logger != null ? this.logger.category() : NAME_DEFAULT));
    final String         level      = PropertyUtility.value(properties, runtime == RuntimeType.SERVER ? LOGGER_LEVEL_SERVER       : LOGGER_LEVEL_CLIENT,       PropertyUtility.value(properties, LOGGER_LEVEL,       LEVEL_DEFAULT));
    final Verbosity      verbosity  = PropertyUtility.value(properties, runtime == RuntimeType.SERVER ? LOGGER_VERBOSITY_SERVER   : LOGGER_VERBOSITY_CLIENT,   PropertyUtility.value(properties, LOGGER_VERBOSITY,   VERBOSITY_DEFAULT));
    final int            entitySize = PropertyUtility.value(properties, runtime == RuntimeType.SERVER ? LOGGER_ENTITY_SIZE_SERVER : LOGGER_ENTITY_SIZE_CLIENT, PropertyUtility.value(properties, LOGGER_ENTITY_SIZE, ENTITY_SIZE_DEFAULT));

    Level loggerLevel = Level.parse(level);
    if (runtime == RuntimeType.SERVER) {
      return new Server(this.logger != null ? this.logger : Logger.create(category), this.level != null ? this.level : loggerLevel, this.verbosity != null ? this.verbosity : verbosity, this.size != null ? this.size : entitySize);
    }
    else {
      return new Client(this.logger != null ? this.logger : Logger.create(category), this.level != null ? this.level : loggerLevel, this.verbosity != null ? this.verbosity : verbosity, this.size != null ? this.size : entitySize);
    }
  }
}
