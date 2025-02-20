package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMScimContext;

import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.DateUtility;

import oracle.iam.identity.foundation.TaskException;

public abstract class AbstractEndpoint implements Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String  LOGGER_CATEGORY = "BKA.SCIM.EXTENSION";
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String prefix;
  private final Logger logger;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractEventHandler</code> which use the default
   ** category for logging purpose.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractEndpoint() {
    this(LOGGER_CATEGORY);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractEventHandler</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractEndpoint(final String loggerCategory) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.prefix = ClassUtility.shortName(this);
    this.logger    = Logger.create(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger (Loggable)
  /**
   ** Returns the logger associated with this task.
   **
   ** @return                    the logger associated with this task.
   **                            <br>
   **                            Possible object is {@link Logger}.
   */
  @Override
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Writes a critical message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the exception as the reason to log.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  @Override
  public final void fatal(final String method, final Throwable throwable) {
    if (this.logger != null)
      this.logger.fatal(this.prefix, method, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Writes a non-critical message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void error(final String method, final String message) {
    if (this.logger != null)
      this.logger.error(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void warning(final String message) {
    if (this.logger != null)
      this.logger.warn(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void warning(final String method, final String message) {
    if (this.logger != null)
      this.logger.warn(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes a informational message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void info(final String message) {
    if (this.logger != null)
      this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void debug(final String method, final String message) {
    if (this.logger != null)
      this.logger.debug(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a trace message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void trace(final String method, final String message) {
    if (this.logger != null)
      this.logger.trace(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@code Record} with message "ENTRY", log level FINER, and the given
   ** sourceMethod and sourceClass is logged.
   **
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void entering(final String method) {
    this.logger.entering(this.prefix, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry, with an array of parameters.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <br>
   ** A {@code Record} with message "ENTRY", log level FINER, and the given
   ** source method and source class and parameter is logged.
   **
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          parameter to the method being entered.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  public void entering(final String method, final Object... parameter) {
    this.logger.entering(this.prefix, method, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exiting
  /**
   ** Log a method return.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <br>
   ** A {@code Record} with message "RETURN", log level FINER, and the given
   ** source method and source class and result object is logged.
   **
   ** @param  method             the name of method that is being returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  result             the Object that is being returned.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  public void exiting(final String method, final Object... result) {
    this.logger.exiting(this.prefix, method, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwing
  /**
   ** Log throwing an exception.
   ** <p>
   ** This is a convenience method to log that a method is terminating by
   ** throwing an exception. The logging is done using the FINER level.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given arguments are stored in a {@code Record} which is forwarded to all
   ** registered output handlers. The {@code Record} 's message is set to
   ** "THROW".
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** The thrown argument is stored in the {@code Record} thrown property,
   ** rather than the {@code Record} parameters property. Thus is it
   ** processed specially by output Formatters and is not treated as a
   ** formatting parameter to the LogRecord message property.
   **
   ** @param  method             the name of method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> that is being
   **                            thrown.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void throwing(final String method, final Throwable throwable) {
    this.logger.throwing(this.prefix, method, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unescapePrefix
  /**
   ** Returns the unescaped value for the lookup fields after removing the '~'
   ** delimiter.
   **
   ** @param  value              contains the field value to be unescaped. For
   **                            example: 1~XYZ.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    string containing the unescaped value. For
   **                            example: XYZ
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String unescapePrefix(final String value) {
    final char delimiter = '~';
    // this is fail safe as long if the entitlement itself does not contain
    // the delimiter
    return value.substring(value.lastIndexOf(delimiter) + 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime
  /**
   ** The current time will be different from this system time.
   ** <p>
   ** To be able to plugin your own method this is the placeholder method to
   ** fetch the server time from the target system.
   ** <p>
   ** This current implementation does not ask the connected server for
   ** anything; it returns simple the current data of the machine where this
   ** library is used.
   **
   ** @return                    the timestamp of the local system.
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @throws TaskException      if the operation fails.
   */
  public Date systemTime()
    throws TaskException {

    return DateUtility.now();
  }
  
  protected Set<String> mergeQueryParameterAttributes(final Set<String> set) {
    final Set<String> attributes = new HashSet<String>();
    for (String element : set) {
      String[] attribute = element.split(",");
      if (attribute != null) {
        Collections.addAll(attributes, attribute);
      }
    }
    return attributes;
  }
  
  protected Map<String, Object> getQueryParameter(HttpServletRequest httpRequest, String... excludedParams) {
    final Map<String, Object> filteredParam = new HashMap<String, Object>();
    final Map<String, String[]> paramMap = httpRequest.getParameterMap();
    
    Set<String> excludedSet = new HashSet<>(Arrays.asList(excludedParams)); 
    for (Map.Entry<String, String[]> queryParam : paramMap.entrySet()) {
      final String      key = queryParam.getKey();
      final String[] values = queryParam.getValue();
      if (!excludedSet.contains(key)) {
        StringBuilder attributeBuilder = new StringBuilder();
        if (key.contains(OIMScimContext.FILTER_ATTRIBUTE_QUERY_PARAM)) {
          Iterator<String> iterator =  Arrays.asList(values).iterator();
          while (iterator.hasNext()) {
            attributeBuilder.append(iterator.next());
            if (iterator.hasNext())
              attributeBuilder.append(" and ");
          }
        }
        else {
          attributeBuilder.append(String.join(",", values));
        }
        filteredParam.put(key, attributeBuilder.toString());
      }
      
    }
    return filteredParam;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getURI
  /**
   ** Constructs a URI by combining the server URL, root context, and additional path elements.
   ** Each path element is properly encoded to handle special characters.
   **
   ** @param serverURL                 the base server URL
   **                                  Must not be null or blank.
   ** @param rootContext               the root context of the URI
   **                                  (e.g., "/api/v2").
   ** @param pathElements              optional path elements to be appended to
   **                                  the root context.
   **                                  Each element will be encoded to ensure a
   **                                  valid URI.
   ** 
   ** @return                          the constructed URI combining the server
   **                                  URL, root context, and path elements.
   ** 
   ** @throws ScimException            if a ServerException is raised.
   */
  protected URI getURI(final String serverURL, final String rootContext, final String... pathElements)
  throws ScimException {
    // Validate the server URL
    if (serverURL == null || serverURL.isEmpty()) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, "Server URL cannot be null or blank.");
    }

    try {
      // Build the path using the path elements
      StringBuilder pathBuilder = new StringBuilder();
      if (rootContext.startsWith("/"))
        pathBuilder.append(rootContext.replaceFirst("/", ""));
      pathBuilder.append(rootContext);
      for (String element : pathElements) {
          if (element != null) {
              if (pathBuilder.length() > 0 && !pathBuilder.toString().endsWith("/")) {
                  pathBuilder.append("/");
              }
              // Encode the path element to escape special characters
              pathBuilder.append(URLEncoder.encode(element, "UTF-8"));
          }
      }
  
      // Construct the full URI
      String fullPath = serverURL.endsWith("/") ? serverURL + pathBuilder : serverURL + "/" + pathBuilder;
      
      
      return new URI(fullPath);
    }
    catch (URISyntaxException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (UnsupportedEncodingException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  
}
