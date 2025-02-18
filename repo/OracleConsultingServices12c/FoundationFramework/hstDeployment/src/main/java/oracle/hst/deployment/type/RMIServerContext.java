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

    File        :   RMIServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RMIServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class RMIServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>RMIServerContext</code> server is a special server and runtime
 ** implementation of {@link ServerContext} tooling that can adjust its
 ** behaviour by a server type definition file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RMIServerContext extends ServerContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTEXT_TYPE   = "rmi";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             contextFactory = null;
  private String             contextURL     = null;

  private InitialContext     context        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class WebLogic
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>WebLogic</code> defines the attribute restriction to
   ** {@link ServerType.WebLogic}.
   */
  public static class WebLogic extends RMIServerContext{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>WebLogic</code> server context for the specified
     ** <code>host</code> and <code>port</code>.
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
     ** @param  host             the value for the attribute <code>host</code>
     **                          used as the RMI/JNDI provider.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the RMI/JNDI provider.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  contextURL       the fullqualified context URL to the server.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  username         the name of the administrative user.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the password of the administrative user.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public WebLogic(final String host, final String port, final String contextURL, final String username, final String password){
      // ensure inheritance
      super(new ServerType.WebLogic(), "t3", host, port, "weblogic.jndi.WLInitialContextFactory", contextURL, username, password);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>WebLogic</code> server context for the specified
     ** <code>host</code> and <code>port</code>.
     ** <br>
     ** The required security context is provided by {@link SecurityPrincipal}
     ** <code>principal</code>.
     ** <p>
     ** <b>Note</b>:
     ** This constructor is mainly used for testing prupose only.
     ** The ANT task and type that leverage this type will be use the non-arg
     ** default constructor and inject their configuration values by the
     ** appropriate setters..
     **
     ** @param  host             the value for the attribute <code>host</code>
     **                          used as the RMI/JNDI provider.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the RMI/JNDI provider.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  contextURL       the fullqualified context URL to the server.
     **                          Allowed object is {@link String}.
     **                          <br>
     ** @param  principal        the security principal used to establish a
     **                          connection to the server.
     **                          <br>
     **                          Allowed object is {@link SecurityPrincipal}.
     */
    public WebLogic(final String host, final String port, final String contextURL, final SecurityPrincipal principal){
      // ensure inheritance
      super(new ServerType.WebLogic(), "t3", host, port, "weblogic.jndi.WLInitialContextFactory", contextURL, principal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RMIServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RMIServerContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RMIServerContext</code> for the specified
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
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the RMI/JNDI
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contextFactory     the context factory to bind to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contextURL         the fullqualified context URL to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public RMIServerContext(final ServerType type, final String protocol, final String host, final String port, final String contextFactory, final String contextURL, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, TIMEOUT_DEFAULT_CONNECTION, TIMEOUT_DEFAULT_RESPONSE, contextFactory, contextURL, username, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RMIServerContext</code> for the specified
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
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the RMI/JNDI
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            RMI/JNDI provider connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  timeoutResponse    the timeout period for reading data on an
   **                            already established connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  contextFactory     the context factory to bind to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contextURL         the fullqualified context URL to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public RMIServerContext(final ServerType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final String contextFactory, final String contextURL, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, timeoutConnection, timeoutResponse, contextFactory, contextURL, new SecurityPrincipal(username, password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RMIServerContext</code> for the specified
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
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the RMI/JNDI
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contextFactory     the context factory to bind to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contextURL         the fullqualified context URL to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public RMIServerContext(final ServerType type, final String protocol, final String host, final String port, final String contextFactory, final String contextURL, final SecurityPrincipal principal) {
    // ensure inheritance
    this(type, protocol, host, port, TIMEOUT_DEFAULT_CONNECTION, TIMEOUT_DEFAULT_RESPONSE, contextFactory, contextURL, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RMIServerContext</code> for the specified
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
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the RMI/JNDI
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the RMI/JNDI provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            RMI/JNDI provider connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  timeoutResponse    the timeout period for reading data on an
   **                            already established connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  contextFactory     the context factory to bind to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contextURL         the fullqualified context URL to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public RMIServerContext(final ServerType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final String contextFactory, final String contextURL, final SecurityPrincipal principal) {
    // ensure inheritance
    super(type, protocol, host, port, timeoutConnection, timeoutResponse, principal);

    // initialize instance attributes
    this.contextFactory = contextFactory;
    this.contextURL     = contextURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>refid</code>.
   **
   ** @param  reference          the id of this instance.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    // prevent bogus input
    if (this.contextFactory != null || !StringUtility.isEmpty(this.contextURL))
      handleAttributeError("refid");

    // ensure inheritance
    super.setRefid(reference);

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof RMIServerContext) {
      RMIServerContext that = (RMIServerContext)other;
      this.contextURL       = that.contextURL;
      this.contextFactory   = that.contextFactory;
    }
    else
      handleReferenceError(reference, CONTEXT_TYPE, other.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextFactory
  /**
   ** Call by the ANT kernel to inject the argument for parameter contextFactory.
   **
   ** @param  contextFactory     the context factory to bind to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public void setContextFactory(final String contextFactory)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("contextFactory");

    this.contextFactory = contextFactory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextURL
  /**
   ** Call by the ANT kernel to inject the argument for parameter contextURL.
   **
   ** @param  contextURL         the fullqualified context URL to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public void setContextURL(final String contextURL)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("contextURL");

    this.contextURL = contextURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextFactory
  /**
   ** Returns the class name of the initial context factory.
   **
   ** @return                    the class name of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextFactory() {
    return this.contextFactory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the remote server.
   **
   ** @return                    the fullqualified URL to the remote server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextURL() {
    return this.contextURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** with this task that afterwards can be passed to establish a connection to
   ** the target system.
   **
   ** @return                    the context this connector use to communicate
   **                            with the RMI/JNDI server.
   **                            <br>
   **                            Possible object is {@link Properties}.
   */
  public final Properties environment() {
    return environment(serviceURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of this task that
   ** afterwards can be passed to establish a connection to the target system.
   **
   ** @param  providerURL        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context this connector use to communicate
   **                            with the RMI server.
   **                            <br>
   **                            Possible object is {@link Properties}.
   */
  public final Properties environment(final String providerURL) {
    final Properties environment = new Properties();
    // setup environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY, this.contextFactory);
    environment.put(Context.PROVIDER_URL,            providerURL);
    environment.put(Context.SECURITY_PRINCIPAL,      this.username());
    environment.put(Context.SECURITY_CREDENTIALS,    this.password());
    if (type().isWebLogic()) {
      environment.put("weblogic.jndi.connectTimeout",      new Long(timeoutConnection()));
      environment.put("weblogic.jndi.responseReadTimeout", new Long(timeoutResponse()));
    }
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public String contextType() {
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

    // everytime to check
    validate();

    if (this.context == null) {
      // Constructs an initial context object using environment properties and
      // connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      this.context = connect(environment(serviceURL()));
    }
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
      this.context.close();
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to perform.
   */
  @Override
  public void validate() {
    // a principal is required for this context type ...
    if (principal() == null)
      handleAttributeMissing("principal");

    // ... and therefore needs also be validated
    principal().validate();

    // without a context factory this context isn't operational
    if (StringUtility.isEmpty(this.contextFactory))
      handleAttributeMissing("contextFactory");

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL (overridden)
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
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BuildException     if the method is not able to retun a valid
   **                            URL.
   */
  @Override
  public final String serviceURL()
    throws BuildException {

    return StringUtility.isEmpty(this.contextURL) ? super.serviceURL() : this.contextURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link InitialContext} by creating the appropriate
   ** environment from the instance attributes.
   **
   ** @param  environment        environment used to create the initial
   **                            {@link InitialContext}.
   **                            <code>null</code> indicates an empty
   **                            environment.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    the context this task use to communicate with
   **                            the application server.
   **                            <br>
   **                            Possible object is {@link InitialContext}.
   **
   ** @throws ServiceException   if the {@link InitialContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  private final InitialContext connect(final Properties environment)
    throws ServiceException {

    try {
      // be optimistic
      this.established(true);
      // Constructs an initial context object using environment properties and
      // connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
     return new InitialContext(environment);
    }
    catch (NamingException e) {
      this.established(false);
      throw new ServiceException(ServiceError.CONTEXT_INITIALIZE, e);
    }
  }
}