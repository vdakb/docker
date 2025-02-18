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

    File        :   AbstractContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractContext
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The client environment wrapper of a generic connection.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractContext extends ServiceDataType {

  /**
   ** Default value of the timeout period for establishment of the content
   ** provider connection.
   */
  public static final int   TIMEOUT_DEFAULT_CONNECTION = 3000;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean           established                = false;

  private String            protocol                   = null;
  private String            host                       = null;
  private String            port                       = null;
  private int               timeoutConnection          = TIMEOUT_DEFAULT_CONNECTION;

  /** needs to be left unitialized to enforce validation */
  private SecurityPrincipal principal                  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   **
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the content provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the content provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            content provider connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}}.
   */
  protected AbstractContext(final String protocol, final String host, final String port, final int timeoutConnection, final SecurityPrincipal principal) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.protocol          = protocol;
    this.host              = host;
    this.port              = port;
    this.principal         = principal;
    this.timeoutConnection = timeoutConnection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid (override)
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>refid</code>.
   **
   ** @param  reference          the id of this instance.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException   if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    // prevent bogus input
    if (this.host != null || this.port != null || this.principal != null)
      handleAttributeError("refid");

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof RMIServerContext) {
      AbstractContext that = (AbstractContext)other;
      this.protocol  = that.protocol;
      this.host      = that.host;
      this.port      = that.port;
      this.principal = that.principal;
      // ensure inheritance
      super.setRefid(reference);
    }
    else {
      final String[]         parameter = {reference.getRefId(), contextType(), reference.getRefId(), other.getClass().getName() };
      final ServiceException rootCause = new ServiceException(ServiceError.TYPE_REFERENCE_MISMATCH, parameter);
      throw new BuildException(rootCause);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextType
  /**
   ** Returns the specific type of the implemented context.
   **
   ** @return                    the specific type of the implemented context.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public abstract String contextType();

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setProtocol
  /**
   ** Call by the ANT kernel to inject the <code>protocol</code> attribute for
   ** the content provider.
   **
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if the <code>protocol</code> attribute is
   **                            already specified by a reference.
   */
  public final void setProtocol(final String protocol)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("protocol");

    this.protocol = protocol;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Returns the <code>protocol</code> attribute for the content provider.
   **
   ** @return                    the <code>protocol</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String protocol() {
    return this.protocol;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setHost
  /**
   ** Call by the ANT kernel to inject the <code>host</code> attribute for the
   ** content provider.
   **
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the content provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if the <code>host</code> attribute is already
   **                            specified by a reference.
   */
  public final void setHost(final String host)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("host");

    this.host = host;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   host
  /**
   ** Returns the <code>host</code> attribute for the content provider.
   **
   ** @return                    the <code>host</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String host() {
    return this.host;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPort
  /**
   ** Call by the ANT kernel to inject the <code>port</code> attribute
   ** for the content provider.
   **
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the content provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if the <code>port</code> attribute is already
   **                            specified by a reference.
   */
  public final void setPort(final String port)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("port");

    this.port = port;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port
  /**
   ** Returns the <code>port</code> attribute for the content provider.
   **
   ** @return                    the <code>port</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String port() {
    return this.port;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>principal</code> as a {@link Reference} of a declared
   ** {@link SecurityPrincipal} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link SecurityPrincipal}.
   */
  public void setPrincipalRef(final Reference reference)
    throws BuildException {

    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof SecurityPrincipal))
      handleReferenceError(reference, SecurityPrincipal.TYPE, object.getClass());

    principal((SecurityPrincipal)object);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   principal
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>principal</code>.
   **
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}.
   **
   ** @throws BuildException     if the <code>principal</code> attribute is
   **                            already specified by a reference.
   */
  public void principal(final SecurityPrincipal principal)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("principal");

    this.principal = principal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principal
  /**
   ** Returns the security principal used to establish a connection to the
   ** server.
   **
   ** @return                    the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Possible object is {@link SecurityPrincipal}.
   */
  public SecurityPrincipal principal() {
    return this.principal;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setUsername
  /**
   ** Call by the ANT kernel to inject the argument for parameter principalName.
   **
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public void setUsername(final String username)
    throws BuildException {

    if (this.principal == null)
      this.principal = new SecurityPrincipal();

    this.principal.setUsername(username);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Returns the username of the security principal for the server used to
   ** connect to.
   **
   ** @return                    the username of the security principal for the
   **                            server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String username() {
    return this.principal == null ? null : this.principal.name();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** principalPassword.
   **
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public void setPassword(final String password)
    throws BuildException {

    if (this.principal == null)
      this.principal = new SecurityPrincipal();

    this.principal.setPassword(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the password of the security principal for the server used to
   ** connect to.
   **
   ** @return                    the password of the security principal for the
   **                            server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String password() {
    return this.principal == null ? null : this.principal.password();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeOutConnection
  /**
   ** Sets the timeout period to establish a connection to the content provider.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** content provider. When connection pooling has been requested, this
   ** property also specifies the maximum wait time or a connection when all
   ** connections in pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the content provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   **
   ** @param  value              the timeout to establish a JMX connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public final void setTimeOutConnection(final int value) {
    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("timeoutConnection");

    this.timeoutConnection = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeoutConnection
  /**
   ** Returns the timeout period for establishment of the connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** content provider. When connection pooling has been requested, this
   ** property also specifies the maximum wait time or a connection when all
   ** connections in pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the content provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   **
   ** @return                    the timeout to establish a connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int timeoutConnection() {
    return this.timeoutConnection;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   established
  /**
   ** Sets the state of the connection of this context.
   **
   ** @param  established        <code>true</code> if the connection of the
   **                            related server instance is established.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  protected final void established(final boolean established) {
    this.established = established;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   established
  /**
   ** returns the state of the connection of this context.
   **
   ** @return                    <code>true</code> if the connection of the
   **                            related server instance is established;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean established() {
    return this.established;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
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
   */
  public String serviceURL() {
    // Create a URL from the parts describe by protocol, host and port
    return String.format("%s://%s:%s", this.protocol(), this.host(), this.port());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   */
  @Override
  public void validate() {
    if (StringUtility.isEmpty(this.protocol))
      handleAttributeMissing("protocol");

    if (StringUtility.isEmpty(this.host))
      handleAttributeMissing("host");

    if (StringUtility.isEmpty(this.port))
      handleAttributeMissing("port");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Establishes the connection to the server.
   ** <p>
   ** This method binds to the context of the server instance.
   **
   ** @throws ServiceException   if the connection could not be established.
   */
  public abstract void connect()
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public abstract void disconnect()
    throws ServiceException;
}