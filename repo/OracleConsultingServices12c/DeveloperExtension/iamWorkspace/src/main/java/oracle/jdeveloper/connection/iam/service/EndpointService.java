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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   EndpointService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointService.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.service;

import oracle.jdeveloper.connection.iam.model.Endpoint;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointService
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointService {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the wrapper of target specific parameters where this connector is attachd
   ** to
   */
  protected final Endpoint resource;

  private boolean          established = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointService</code> which is associated with the
   ** specified resource descriptor.
   **
   ** @param  resource           the {@link Endpoint} definition where this
   **                            connector is associated with.
   **                            <br>
   **                            Allowed object is {@link Endpoint}.
   */
  public EndpointService(final Endpoint resource) {
    // ensure inheritance
    super();

    // create the property mapping for the Endpoint control
    this.resource = resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   established
  /**
   ** Sets the state of the connection of this service.
   **
   ** @param  established        <code>true</code> if the connection of the
   **                            related target system is established;
   **                            otherwise <code>false</code>.
   **                            Allowed object is <code>boolean</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  protected final void established(final boolean established) {
    this.established = established;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   established
  /**
   ** returns the state of the connection of this service.
   **
   ** @return                    <code>true</code> if the connection of the
   **                            related target system is established;
   **                            otherwise <code>false</code>.
   **                            Possible object is <code>boolean</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public boolean established() {
    return this.established;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the connection resource.
   **
   ** @return                    the connection resource.
   **                            <br>
   **                            Possible object is {@link Endpoint}.
   */
  public final Endpoint resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the host name of the target system used to connect to.
   **
   ** @return                    the host name of the target system used to
   **                            connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverName() {
    return this.resource.serverName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the port the target system used to connect to is listening on.
   **
   ** @return                    the port the target system used to connect to
   **                            is listening on.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int serverPort() {
    return this.resource.serverPort();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverContext
  /**
   ** Returns the context the target system used to connect.
   **
   ** @return                    the context the target system used to connect.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverContext() {
    return this.resource.serverContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSocketSSL
  /**
   ** Returns the socket security of the this <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Endpoint#SERVER_SOCKET_SSL}.
   **
   ** @return                    <code>true</code> if the transport security use
   **                            SSL.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final boolean serverSocketSSL() {
    return this.resource.serverSocketSSL();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSocketTLS
  /**
   ** Returns the socket security of the this <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Endpoint#SERVER_SOCKET_TLS}.
   **
   **
   ** @return                    <code>true</code> if the transport security use
   **                            TLS.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean serverSocketTLS() {
    return this.resource.serverSocketTLS();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverStartTLS
  /**
   ** Returns the socket security of the this <code>IT Resource</code>.
   **
   ** @return                    <code>true</code> if the transport security use
   **                            startTLS negotiation.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean serverStartTLS() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the name of the security principal of the target system used to
   ** connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Endpoint#PRINCIPAL_NAME}.
   **
   ** @return                    the name of the security principal target
   **                            system used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String principalName() {
    return this.resource.principalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password of the security principal of the target system used
   ** to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Endpoint#PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the security principal
   **                            target system used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String principalPassword() {
    return this.resource.principalPassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>imt</code>.
   */
  @Override
  public int hashCode() {
    return (this.resource != null ? this.resource.hashCode() : 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>EndpointService</code> object that
   ** represents the same <code>resource</code> and value as this object.
   **
   ** @param other               the object to compare this
   **                            <code>EndpointService</code> against.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the
   **                            <code>EndpointService</code>s are
   **                            equal; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof EndpointService))
      return false;

    if (this.resource == null && ((EndpointService)other).resource == null)
      return true;
    else if (this.resource == null && ((EndpointService)other).resource != null)
      return false;
    else if (other == null && this.resource == null)
      return true;
    else
      return this.resource.equals(((EndpointService)other).resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Establishes the connection to the target system.
   ** <p>
   ** This method binds to the context of the server instance.
   **
   ** @throws EndpointException  if the connection could not be established at
   **                            the first time this method is invoked.
   */
  public abstract void connect()
    throws EndpointException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Close a connection to the target system.
   **
   ** @throws EndpointException  in case an error does occur.
   */
  public abstract void disconnect()
    throws EndpointException;
}