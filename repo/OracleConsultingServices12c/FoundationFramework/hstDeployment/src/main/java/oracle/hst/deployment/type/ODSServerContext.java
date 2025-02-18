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

    File        :   ODSServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ODSServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import javax.naming.ldap.LdapContext;
import javax.naming.ldap.InitialLdapContext;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class ODSServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>ODSServerContext</code> server is a special server and runtime
 ** implementation of {@link ServerContext} tooling that can adjust its
 ** behaviour by a server type definition file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ODSServerContext extends DirectoryContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTEXT_TYPE  = "ods";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private LdapContext        context       = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class UnifiedDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** <code>UnifiedDirectory</code> defines the attribute restriction to
   ** {@link DirectoryType.UnifiedDirectory}.
   */
  public static class UnifiedDirectory extends ODSServerContext{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>UnifiedDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  username         the name of the administrative user.
     ** @param  password         the password of the administrative user.
     */
    public UnifiedDirectory(final String host, final String port, final String username, final String password){
      // ensure inheritance
      this(host, port, new SecurityPrincipal(username, password));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>UnifiedDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  principal        the security principal used to establish a
     **                          connection to the server.
     */
    public UnifiedDirectory(final String host, final String port, final SecurityPrincipal principal){
      // ensure inheritance
      super(new DirectoryType.UnifiedDirectory(), "ldap", host, port, principal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class InternetDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** <code>InternetDirectory</code> defines the attribute restriction to
   ** {@link DirectoryType.InternetDirectory}.
   */
  public static class InternetDirectory extends ODSServerContext{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>InternetDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  username         the name of the administrative user.
     ** @param  password         the password of the administrative user.
     */
    public InternetDirectory(final String host, final String port, final String username, final String password){
      // ensure inheritance
      this(host, port, new SecurityPrincipal(username, password));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>InternetDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  principal        the security principal used to establish a
     **                          connection to the server.
     */
    public InternetDirectory(final String host, final String port, final SecurityPrincipal principal){
      // ensure inheritance
      super(new DirectoryType.InternetDirectory(), "ldap", host, port, principal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class VirtualDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** <code>VirtualDirectory</code> defines the attribute restriction to
   ** {@link DirectoryType.VirtualDirectory}.
   */
  public static class VirtualDirectory extends ODSServerContext{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>VirtualDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  username         the name of the administrative user.
     ** @param  password         the password of the administrative user.
     */
    public VirtualDirectory(final String host, final String port, final String username, final String password){
      // ensure inheritance
      this(host, port, new SecurityPrincipal(username, password));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>VirtualDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  principal        the security principal used to establish a
     **                          connection to the server.
     */
    public VirtualDirectory(final String host, final String port, final SecurityPrincipal principal){
      // ensure inheritance
      super(new DirectoryType.VirtualDirectory(), "ldap", host, port, principal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class NovellDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** <code>NovellDirectory</code> defines the attribute restriction to
   ** {@link DirectoryType.NovellDirectory}.
   */
  public static class NovellDirectory extends ODSServerContext{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>NovellDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  username         the name of the administrative user.
     ** @param  password         the password of the administrative user.
     */
    public NovellDirectory(final String host, final String port, final String username, final String password){
      // ensure inheritance
      this(host, port, new SecurityPrincipal(username, password));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>NovellDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  principal        the security principal used to establish a
     **                          connection to the server.
     */
    public NovellDirectory(final String host, final String port, final SecurityPrincipal principal){
      // ensure inheritance
      super(new DirectoryType.NovellDirectory(), "ldap", host, port, principal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ActiveDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** <code>ActiveDirectory</code> defines the attribute restriction to
   ** {@link DirectoryType.ActiveDirectory}.
   */
  public static class ActiveDirectory extends ODSServerContext{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ActiveDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  username         the name of the administrative user.
     ** @param  password         the password of the administrative user.
     */
    public ActiveDirectory(final String host, final String port, final String username, final String password){
      // ensure inheritance
      this(host, port, new SecurityPrincipal(username, password));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ActiveDirectory</code> server context for the
     ** specified <code>host</code> and <code>port</code>.
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
     **                          used as the LDAP provider.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the LDAP provider.
     ** @param  principal        the security principal used to establish a
     **                          connection to the server.
     */
    public ActiveDirectory(final String host, final String port, final SecurityPrincipal principal){
      // ensure inheritance
      super(new DirectoryType.ActiveDirectory(), "ldap", host, port, principal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ODSServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ODSServerContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ODSServerContext</code> for the specified
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
   **                            used as the LDAP provider.
   **                            Allowed object is {@link DirectoryType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the LDAP
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the LDAP provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the LDAP provider.
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            Allowed object is {@link String}.
   */
  public ODSServerContext(final DirectoryType type, final String protocol, final String host, final String port, final String username, final String password) {
    // ensure inheritance
    super(type, protocol, host, port, username, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ODSServerContext</code> for the specified
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
   **                            used as the LDAP provider.
   **                            Allowed object is {@link DirectoryType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the LDAP
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the LDAP provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the LDAP provider.
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public ODSServerContext(final DirectoryType type, final String protocol, final String host, final String port, final SecurityPrincipal principal) {
    // ensure inheritance
    super(type, protocol, host, port, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap
  /**
   ** Returns an object that implements the given interface to allow access to
   ** non-standard methods, or standard methods not exposed by the wrapper.
   **
   ** @return                    an object that implements the connection
   **                            interface. May be a proxy for the actual
   **                            implementing object.
   **                            Possible object is {@link LdapContext}.
   */
  public final LdapContext unwrap() {
    return this.context;
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
   **                            Possible object is {@link String}.
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

    connect(SystemConstant.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @param  contextPath        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @throws ServiceException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public void connect(final String contextPath)
    throws ServiceException {

    // everytime to check
    validate();

    if (this.context == null) {
      // Constructs an LDAP context object using environment properties and
      // connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      this.context = connect(environment(contextPath));
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
}