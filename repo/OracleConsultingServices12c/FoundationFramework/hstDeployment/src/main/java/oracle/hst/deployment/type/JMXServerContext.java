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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities

    File        :   JMXServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JMXServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import java.util.Map;
import java.util.Locale;
import java.util.HashMap;

import java.io.IOException;

import java.net.MalformedURLException;

import javax.naming.Context;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnectorFactory;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class JMXServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>JMXServerContext</code> server is a special server and runtime
 ** implementation of {@link ServerContext} tooling that can adjust its
 ** behaviour by a server type definition file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JMXServerContext extends ServerContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTEXT_TYPE     = "jmx";

  public static final String PROVIDER_PACKAGE = "weblogic.management.remote";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             server           = null;
  private JMXConnector       connector        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JMXServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JMXServerContext(){
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JMXServerContext</code> for the specified
   ** <code>type</code>, <code>protocol</code>, <code>host</code> and
   ** <code>port</code>.
   ** <br>
   ** The required security context is provided by <code>username</code> and
   ** <code>password</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the JMX
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            Allowed object is {@link String}.
   */
  public JMXServerContext(final ServerType type, final String protocol, final String host, final String port, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, new SecurityPrincipal(username, password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JMXServerContext</code> for the specified
   ** <code>type</code>, <code>protocol</code>, <code>host</code> and
   ** <code>port</code>.
   ** <br>
   ** The required security context is provided by <code>username</code> and
   ** <code>password</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the JMX
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            JMX provider connection.
   **                            Allowed object is <code>int</code>.
   ** @param  timeoutResponse    the timeout period for reading data on an
   **                            already established connection.
   **                            Allowed object is <code>int</code>.
   ** @param  username           the name of the administrative user.
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            Allowed object is {@link String}.
   */
  public JMXServerContext(final ServerType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, timeoutConnection, timeoutResponse, new SecurityPrincipal(username, password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JMXServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the JMX
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public JMXServerContext(final ServerType type, final String protocol, final String host, final String port, final SecurityPrincipal principal) {
    // ensure inheritance
    super(type, protocol, host, port, TIMEOUT_DEFAULT_CONNECTION, TIMEOUT_DEFAULT_RESPONSE, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JMXServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the JMX
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the JMX provider.
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            JMX provider connection.
   **                            Allowed object is <code>int</code>.
   ** @param  timeoutResponse    the timeout period for reading data on an
   **                            already established connection.
   **                            Allowed object is <code>int</code>.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public JMXServerContext(final ServerType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final SecurityPrincipal principal) {
    // ensure inheritance
    super(type, protocol, host, port, timeoutConnection, timeoutResponse, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connector
  /**
   ** Returns the {@link JMXConnector}.
   **
   ** @return                    the {@link JMXConnector}
   */
  public final JMXConnector connector() {
    return this.connector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Map} from the attributes of this task that afterwards
   ** can be passed to establish a connection to the target system.
   **
   ** @return                    the context this connector use to communicate
   **                            with the JMX MBean server.
   **
   */
  private Map<String, Object> environment() {
    final Map<String, Object> environment = new HashMap<String, Object>();
    // Set up environment for creating initial context
    environment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, PROVIDER_PACKAGE);
    environment.put("jmx.remote.x.request.waiting.timeout",         new Long(this.timeoutResponse()));
    environment.put("weblogic.management.remote.locale",            Locale.getDefault());
    environment.put(Context.SECURITY_PRINCIPAL,                     this.username());
    environment.put(Context.SECURITY_CREDENTIALS,                   this.password());
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextType (AbstractContext)
  /**
   ** Returns the specific type of the implemented context.
   **
   ** @return                    the specific type of the implemented context.
   */
  @Override
  public final String contextType() {
    return CONTEXT_TYPE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (AbstractContext)
  /**
   ** Establishes the connection to the server.
   ** <p>
   ** This method binds to the context of the server instance.
   **
   ** @throws ServiceException   if the connection could not be established.
   */
  @Override
  public void connect()
    throws ServiceException {

    if (StringUtility.isEmpty(this.server))
      handleAttributeMissing("server");

    connect(this.server);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractContext)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void disconnect()
    throws ServiceException {

    try {
      if (this.connector != null) {
        this.connector.close();
        this.connector = null;
        this.established(false);
      }
    }
    catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to perform.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate()
    throws BuildException {

    // a principal is required for this context type ...
    if (principal() == null)
      handleAttributeMissing("principal");

    // ... and therefore needs also be validated
    principal().validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect ()
  /**
   ** Establishes the connection to the server.
   ** <p>
   ** This method binds to the context of the server instance.
   **
   ** @param  server             the name of the MBean Server to connect to.
   **
   ** @throws ServiceException   if the connection could not be established.
   */
  public void connect(final String server)
    throws ServiceException {

    if (this.connector == null) {
      // Constructs an initial context object using environment properties and
      // connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      try {
        final JMXServiceURL contextURL = new JMXServiceURL(this.protocol(), this.host(), Integer.parseInt(this.port()), server);
        this.connector = JMXConnectorFactory.connect(contextURL, environment());
        this.established(true);
      }
      catch (MalformedURLException e) {
        throw new ServiceException(ServiceError.UNHANDLED, e.getLocalizedMessage());
      }
      catch (IOException e) {
        Throwable cause  = e;
        Throwable cursor = cause;
        // the exception catched here may not be the root cause in case we are
        // not able to reach the target system
        while (cursor != null) {
          cause  = cursor;
          cursor = cursor.getCause();
        }
        throw new ServiceException(cause);
      }
    }
  }
}