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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Workflow Facility

    File        :   ServiceProvider.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.context;

import java.util.Properties;

import java.text.MessageFormat;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.security.auth.login.LoginException;

import oracle.security.jps.JpsException;
import oracle.security.jps.JpsContext;
import oracle.security.jps.JpsContextFactory;

import oracle.security.jps.service.credstore.CredStoreException;
import oracle.security.jps.service.credstore.CredentialExpiredException;
import oracle.security.jps.service.credstore.Credential;
import oracle.security.jps.service.credstore.CredentialStore;
import oracle.security.jps.service.credstore.PasswordCredential;

import oracle.iam.platform.OIMClient;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.workflow.ServiceError;
import oracle.iam.identity.workflow.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class ServiceProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>ServiceProvider</code> ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class ServiceProvider implements Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static enum  Type { WEBLOGIC, JBOSS, OC4J, GLASSFISH, WEBSPHERE};

  static final String LOGGER_CATEGORY  = "OCS.IDENTITY.WORKFLOW";

  static final String DEFAULT_HOST     = "localhost";

  static final String EXCEPTION_PREFIX = "java.net.ConnectException: ";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String  shortName;
  protected final Logger  logger;

  private OIMClient       platform         = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceProvider</code> RMI/JNDI provider that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected ServiceProvider() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.shortName = ClassUtility.shortName(this);
    this.logger    = Logger.create(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Returns the <code>protocol</code> attribute for the RMI/JNDI provider.
   **
   ** @return                    the <code>protocol</code> attribute for the
   **                            RMI/JNDI provider.
   */
  protected abstract String protocol();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   host
  /**
   ** Returns the <code>host</code> attribute for the RMI/JNDI provider.
   ** <p>
   ** Subclasses may override this implementation to refere to a diffenrent host
   ** as {@link #DEFAULT_HOST}.
   **
   ** @return                    the <code>host</code> attribute for the
   **                            RMI/JNDI provider.
   */
  protected String host() {
    return DEFAULT_HOST;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port
  /**
   ** Returns the <code>port</code> attribute for the RMI/JNDI provider.
   ** <p>
   ** Subclasses must override this implementation.
   **
   ** @return                    the <code>port</code> attribute for the
   **                            RMI/JNDI provider.
   */
  protected abstract String port();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextFactory
  /**
   ** Returns the value of the context factory passed to the instantiazion of an
   ** JNDI Context.
   **
   ** @return                    the value of the context factory passed to the
   **                            instantiazion of an JNDI Context.
   */
  protected abstract String contextFactory();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packagePrefix
  /**
   ** Returns the package prefix of the URL to locate the
   ** {@link OIMClient}.
   ** <p>
   ** The prefix consists of the URL scheme id and a suffix to construct the
   ** class name, as follows:
   ** <pre>
   **   prefix.schemeId.schemeIdURLContextFactory
   ** </pre>
   ** This property is used when a URL name is passed to the
   ** {@link OIMClient} (in the API reference documentation) methods.
   **
   ** @return                    the value of the context factory passed to the
   **                            instantiazion of an JNDI Context.
   */
  protected abstract String packagePrefix();

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger (Loggable)
  /**
   ** Returns the logger associated with this task.
   **
   ** @return                    the logger associated with this task.
   */
  @Override
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String method, final Throwable what) {
    this.logger.fatal(this.shortName, method, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void error(final String method, final String message) {
    this.logger.error(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String message) {
    this.logger.warn(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String method, final String message) {
    this.logger.warn(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void info(final String message) {
    this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void debug(final String method, final String message) {
    this.logger.debug(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void trace(final String method, final String message) {
    this.logger.trace(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** with this task that afterwards can be passed to establish a connection to
   ** the target system.
   **
   ** @param  contextURL         the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @return                    the context this locator use to lookup JNDI
   **                            resources.
   */
  public final Properties environment(final String contextURL) {
    Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, contextFactory());
    environment.put(OIMClient.JAVA_NAMING_PROVIDER_URL,    contextURL);
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** with this task that afterwards can be passed to establish a connection to
   ** the target system.
   **
   ** @param  contextURL         the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @throws TaskException      if the <code>OIMClient</code> context could not
   **                            be created at the first time this method is
   **                            invoked.
   */
  public final void connect(final String contextURL)
    throws TaskException {

    final String method = "credential";
    trace(method, SystemMessage.METHOD_ENTRY);

    // get system credentials from credential security framework
    SecurityPrincipal principal = null;
    try {
      final JpsContext      context    = JpsContextFactory.getContextFactory().getContext();
      final CredentialStore store      = context.getServiceInstance(CredentialStore.class);
      final Credential      credential = AccessController.doPrivileged(
        new PrivilegedExceptionAction<Credential>() {
          public Credential run() throws JpsException {
            return store.getCredential("oracle.oim.sysadminMap", "sysadmin");
          }
        }
      );
      if (credential instanceof PasswordCredential) {
        final PasswordCredential temp = (PasswordCredential)credential;
        principal = new SecurityPrincipal(temp.getName(), temp.getPassword());
      }
      connect(contextURL, SecurityPrincipal.credential());
    }
    catch (CredentialExpiredException e) {
      throw new TaskException(e);
    }
    catch (CredStoreException e) {
      throw new TaskException(e);
    }
    catch (JpsException e) {
      throw new TaskException(e);
    }
    catch (PrivilegedActionException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** with this task that afterwards can be passed to establish a connection to
   ** the target system.
   **
   ** @param  contextURL         the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   ** @param  principal          the identity of the user (that is, a
   **                            <code>User</code> defined in a J2EE Server
   **                            security realm) for authentication purposes.
   **
   ** @throws TaskException      if the <code>OIMClient</code> context could not
   **                            be created at the first time this method is
   **                            invoked.
   */
  public final void connect(final String contextURL, final SecurityPrincipal principal)
    throws TaskException {

    connect(contextURL, principal.getName(), principal.getPassword());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Retrieves an environment value for a given name from Java ENC.
   **
   ** @param  contextURL         the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   ** @param  username           the administrative user used to login in
   **                            Identity Manager.
   ** @param  credential         the passsword for above.
   **
   ** @throws TaskException      if the <code>OIMClient</code> context could not
   **                            be created at the first time this method is
   **                            invoked.
   */
  public void connect(final String contextURL, final String username, final char[] credential)
    throws TaskException {

    if (this.platform == null) {
      // Passing environment in constructor disables lookup for environment in
      // setup. In any case, we can always enforce manual environment settings
      // by OIMClient.setLookupEnv(configEnv) method.
      this.platform = new OIMClient(this.environment(contextURL));

    try {
      this.platform.login(username, credential);
    }
    catch (LoginException e) {
      final String message = e.getMessage();
      if (message.startsWith(EXCEPTION_PREFIX))
        throw new TaskException(e);
      else
        throw new ServiceException(ServiceError.CONTEXT_ACCESS_DENIED, e.getLocalizedMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Close a connection to the target system.
   */
  public void disconnect() {

    if (this.platform != null) {
      this.platform.logout();
      this.platform = null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerURL
  /**
   ** Constructs an service URL to bind to.
   ** <p>
   ** At first it checks if the context URL is set. If so it will return it as
   ** it is.
   **
   ** @return                    the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @throws ServiceException   if the method is not able to retun a valid
   **                            URL.
   */
  protected String providerURL()
    throws ServiceException {

    if (StringUtility.isEmpty(this.protocol()))
      throw new ServiceException(ServiceError.ATTRIBUTE_MISSING, "protocol");

    if (StringUtility.isEmpty(this.host()))
      throw new ServiceException(ServiceError.ATTRIBUTE_MISSING, "host");

    if (StringUtility.isEmpty(this.port()))
      throw new ServiceException(ServiceError.ATTRIBUTE_MISSING, "port");

    // Create a URL from the parts describe by protocol, host and port
    // a thrown MalformedURLException indicates that the string is not a
    // valid URL
    final Object[] arguments = { this.protocol(), this.host(), this.port()};
    return MessageFormat.format("{0}://{1}:{2}", arguments);
  }
}