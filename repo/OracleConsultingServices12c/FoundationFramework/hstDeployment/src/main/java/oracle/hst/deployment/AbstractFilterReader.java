package oracle.hst.deployment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.Hashtable;
import java.util.Properties;

import java.io.Reader;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.resources.FileResource;

import org.apache.tools.ant.filters.BaseParamFilterReader;

import org.apache.tools.ant.util.FileUtils;

public class AbstractFilterReader extends    BaseParamFilterReader
                                  implements ServiceFrontend {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** default "begin token" character. */
  private static final String         DEFAULT_TOKEN_BEGIN = "@";

  /** default "end token" character. */
  private static final String         DEFAULT_TOKEN_END   = "@";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** character marking the beginning of a token. */
  protected String                    tokenBegin          = DEFAULT_TOKEN_BEGIN;

  /** character marking the end of a token. */
  protected String                    tokenEnd            = DEFAULT_TOKEN_END;

  /** flag to control verbosity. */
  private boolean                     verbose             = false;

  /** flag to control behavior of failure handling. */
  private boolean                     failonerror         = true;

  /** map to hold the original replacee-replacer pairs (String to String). */
  protected Hashtable<String, String> token               = new Hashtable<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractFilterReader</code> Ant filter that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractFilterReader() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractFilterReader</code> as an Ant filtered reader.
   **
   ** @param  reader             a {@link Reader} object providing the
   **                            underlying stream.
   **                            Must not be <code>null</code>.
   */
  public AbstractFilterReader(final Reader reader) {
    // ensure inheritance
    super(reader);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPropertiesResource
  /**
   ** A resource containing properties, each of which is interpreted as a
   ** token/value pair.
   **
   ** @param  r                  Resource
   */
  public void setPropertiesResource(final Resource r) {
    make(r);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBeginToken
  /**
   ** Sets the character used to denote the beginning of a token.
   **
   ** @param  value               the character used to denote the beginning of
   **                             a token.
   */
  public void setBeginToken(final String value) {
    this.tokenBegin = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenBegin
  /**
   ** Returns the character used to denote the beginning of a token.
   **
   ** @return                    the character used to denote the beginning of
   **                            a token.
   */
  protected String tokenBegin() {
    return this.tokenBegin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEndToken
  /**
   ** Sets the character used to denote the end of a token.
   **
   ** @param  value              the character used to denote the end of a
   **                            token.
   */
  public void setEndToken(final String value) {
    this.tokenEnd = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenEnd
  /**
   ** Returns the character used to denote the end of a token.
   **
   ** @return                    the character used to denote the end of a
   **                            token.
   */
  protected String tokenEnd() {
    return this.tokenEnd;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVerbose
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>verbose</code>.
   ** <p>
   ** Set the flag indicating that verbosity is requested.
   **
   ** @param verbose             <code>true</code> if verbosity is requested;
   **                            otherwise <code>false</code>.
   */
  public void setVerbose(final boolean verbose) {
    this.verbose = verbose;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFailOnError
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>failonerror</code>.
   ** <p>
   ** Sets how the task behavior will be in case an error is detected.
   **
   ** @param  failonerror        how the task behavior will be in case an error
   **                            is detected.
   */
  public final void setFailOnError(final boolean failonerror) {
    this.failonerror = failonerror;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token
  /**
   ** Sets the map of tokens which will be replaced..
   **
   ** @param  token              a map (String-&gt;String) of token keys to
   **                            replacement values.
   **                            Must not be <code>null</code>.
   */
  protected final void token(final Hashtable<String, String> token) {
    this.token = token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token
  /**
   ** Returns the map of tokens which will be replaced.
   **
   ** @return                    a map (String-&gt;String) of token keys to
   **                            replacement values.
   */
  protected Hashtable<String, String> token() {
    return this.token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verbose (ServiceFrontend)
  /**
   ** Returns how the task will provide verbosity of operations.
   **
   ** @return                    <code>true</code> if verbosity is requested;
   **                            otherwise <code>false</code>.
   */
  @Override
  public final boolean verbose() {
    return this.verbose;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failonerror (ServiceFrontend)
  /**
   ** Return how the task behavior will be in case an error is detected.
   **
   ** @return                    how the task behavior will be in case an error
   **                            is detected.
   */
  @Override
  public final boolean failonerror() {
    return this.failonerror;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (ServiceFrontend)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final Throwable what) {
    System.err.println(what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (ServiceFrontend)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String location, final Throwable what) {
    System.err.println(what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ServiceFrontend)
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  @Override
  public final void error(final String pattern, final String argument) {
    String[] values = { argument };
    error(pattern, values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ServiceFrontend)
  /**
   ** Writes an normal error message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void error(final String pattern, final Object[] arguments) {
    error(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ServiceFrontend)
  /**
   ** Logs an normal error message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void error(final String message) {
    getProject().log(message, Project.MSG_ERR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ServiceFrontend)
  /**
  ** Writes a warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  @Override
  public final void warning(final String pattern, final String argument) {
    String[] arguments = { argument };
    warning(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ServiceFrontend)
  /**
   ** Writes an warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void warning(final String pattern, final Object[] arguments) {
    warning(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ServiceFrontend)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            what is the reason to log.
   */
  @Override
  public final void warning(final String message) {
    getProject().log(message, Project.MSG_WARN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (ServiceFrontend)
  /**
  ** Writes a informational message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  @Override
  public final void info(final String pattern, final String argument) {
    String[] arguments = { argument };
    info(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (ServiceFrontend)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments.
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void info(final String pattern, final Object[] arguments) {
    info(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (ServiceFrontend)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void info(final String message) {
    getProject().log(message, Project.MSG_INFO);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (ServiceFrontend)
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  @Override
  public final void trace(final String pattern, final String what) {
    String[] arguments = { what };
    trace(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (ServiceFrontend)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void trace(final String pattern, final Object[] arguments) {
    trace(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   trace (ServiceFrontend)
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  @Override
  public final void trace(final String message) {
    getProject().log(message, Project.MSG_VERBOSE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (ServiceFrontend)
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  @Override
  public final void debug(final String pattern, final String what) {
    String[] arguments = { what };
    debug(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (ServiceFrontend)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void debug(final String pattern, final Object[] arguments) {
    debug(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   debug (ServiceFrontend)
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  @Override
  public final void debug(final String message) {
    getProject().log(message, Project.MSG_DEBUG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initializes tokens and loads the replacee-replacer hashtable.
   */
  protected void initialize() {
    final Parameter[] params = getParameters();
    if (params != null) {
      for (Parameter param : params) {
        if (param != null) {
          final String type = param.getType();
          if ("tokenchar".equals(type)) {
            final String name = param.getName();
            if ("begintoken".equals(name)) {
              this.tokenBegin = param.getValue();
            }
            else if ("endtoken".equals(name)) {
              this.tokenEnd = param.getValue();
            }
          }
          else if ("token".equals(type)) {
            final String name  = param.getName();
            final String value = param.getValue();
            this.token.put(name, value);
          }
          else if ("propertiesfile".equals(type)) {
            make(new FileResource(new File(param.getValue())));
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   make
  /**
   ** Adds properties from a specified properties file in the token map.
   **
   ** @param  resource           the resource to load properties from.
   */
  private void make(final Resource resource) {
    final Properties properties = properties(resource);
    properties.stringPropertyNames().forEach(key -> this.token.put(key, properties.getProperty(key)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Returns properties from a specified properties file.
   **
   ** @param  resource           the resource to load properties from.
   */
  private Properties properties(final Resource resource) {
    InputStream      stream     = null;
    final Properties properties = new Properties();
    try {
      stream = resource.getInputStream();
      properties.load(stream);
    }
    catch (IOException e) {
      if (this.failonerror) {
        throw new BuildException(e);
      }
      error(ServiceResourceBundle.format(ServiceError.FILTER_PROPERTY_FILE, resource.getName()), e.getLocalizedMessage());
    }
    finally {
      FileUtils.close(stream);
    }

    return properties;
  }
}