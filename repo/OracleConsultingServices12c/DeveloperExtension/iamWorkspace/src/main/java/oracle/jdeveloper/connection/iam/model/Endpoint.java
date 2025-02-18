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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   Endpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Endpoint.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

import javax.naming.Referenceable;

////////////////////////////////////////////////////////////////////////////////
// class Endpoint
// ~~~~~ ~~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties.
 ** <p>
 ** This data might also been used to display a specific node in the Resource
 ** Catalog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class Endpoint extends Storage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the {@link Referenceable} where this <code>IT Resource</code>
   ** belongs to.
   */
  public static final String  NAME                   = "name";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the target system where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String  SERVER_NAME            = "server-name";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the port of the target system where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String  SERVER_PORT            = "server-port";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the path to the key store used to verify trusted communication
   ** with an endpoint.
   */
  public static final String  SERVER_TRUST_KEYSTORE  = "server-trust-keystore";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the password to unlock the key store used to verify trusted
   ** communication with an endpoint.
   */
  public static final String  SERVER_TRUST_PASSWORD  = "server-trust-password";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify if you plan to configure SSL to secure communication between
   ** JDeveloper and the target system.
   */
  public static final String  SERVER_SOCKET_SSL      = "server-socket-ssl";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify if you plan to configure TLS to secure communication between
   ** JDeveloper and the target system.
   */
  public static final String  SERVER_SOCKET_TLS      = "server-socket-tls";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the server context where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String  SERVER_CONTEXT         = "server-context";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Endpoint</code> that belongs to the specified
   ** connection type and factory.
   **
   ** @param  type               the connection type.
   ** @param  factory            the connection factory.
   */
  protected Endpoint(final Class<? extends Referenceable> type, final Class<? extends EndpointFactory> factory) {
    // ensure inheritance
    super(type, factory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the this <code>IT Resource</code>.
   **
   ** @param  name               the name of this <code>IT Resource</code>.
   */
  public void name(final String name) {
    property(NAME, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the this <code>IT Resource</code>.
   **
   ** @return                    the name of this <code>IT Resource</code>.
   */
  public String name() {
    return stringValue(NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Sets the name of the server where the target system is deployed on and
   ** this <code>IT Resource</code> is configured for.
   **
   ** @param  host               the name of the server where the target system
   **                            is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   */
  public void serverName(final String host) {
    property(SERVER_NAME, host);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the server where the target system is deployed on and
   ** this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_NAME}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the name of the server where the target system
   **                            is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   */
  public String serverName() {
    return stringValue(SERVER_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Sets the listener port of the server the target system is deployed on
   ** and this <code>IT Resource</code> is configured for.
   **
   ** @param  port               the listener port of the server the target
   **                            system is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   */
  public void serverPort(final String port) {
    property(SERVER_PORT, port);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the listener port of the server the target system is deployed on
   ** and this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PORT}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the listener port of the server the target
   **                            system is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   */
  public int serverPort() {
    return integerValue(SERVER_PORT, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePath
  /**
   ** Sets the path to the key store used to verify trusted communication with
   ** an endpoint.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_TRUST_KEYSTORE}.
   **
   ** @param  path               the path to the key store used to verify
   **                            trusted communication with an endpoint.
   */
  public void trustStorePath(final String path) {
    property(SERVER_TRUST_KEYSTORE, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePath
  /**
   ** Returns the path to the key store used to verify trusted communication
   ** with an endpoint.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_TRUST_KEYSTORE}.
   **
   ** @return                    the path to the key store used to verify
   **                            trusted communication with an endpoint.
   */
  public String trustStorePath() {
    return stringValue(SERVER_TRUST_KEYSTORE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePassword
  /**
   ** Sets the password required to unlock the trusted key store.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_TRUST_PASSWORD}.
   **
   ** @param  password           the password required to unlock the trusted key
   **                            store.
   */
  public void trustStorePassword(final String password) {
    property(SERVER_TRUST_PASSWORD, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePassword
  /**
   ** Returns the password required to unlock the trusted key store.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_TRUST_PASSWORD}.
   **
   ** @return                    the password required to unlock the trusted key
   **                            store.
   */
  public String trustStorePassword() {
    return stringValue(SERVER_TRUST_KEYSTORE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSocketSSL
  /**
   ** Sets the socket security of the this <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_SOCKET_SSL}.
   **
   ** @param  state              <code>true</code> if the transport security use
   **                            SSL; otherwise <code>false</code>.
   */
  public void serverSocketSSL(final boolean state) {
    property(SERVER_SOCKET_SSL, Boolean.toString(state));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSocketSSL
  /**
   ** Returns the socket security of the this <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_SOCKET_SSL}.
   **
   ** @return                    <code>true</code> if the transport security use
   **                            SSL; otherwise <code>false</code>.
   */
  public boolean serverSocketSSL() {
    return booleanValue(SERVER_SOCKET_SSL, Boolean.FALSE.booleanValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSocketTLS
  /**
   ** Sets the socket security of the this <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_SOCKET_TLS}.
   **
   ** @param  state              <code>true</code> if the transport security use
   **                            TLS; otherwise <code>false</code>.
   */
  public void serverSocketTLS(final boolean state) {
    property(SERVER_SOCKET_TLS, Boolean.toString(state));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSocketTLS
  /**
   ** Returns the socket security of the this <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_SOCKET_TLS}.
   **
   ** @return                    <code>true</code> if the transport security use
   **                            TLS; otherwise <code>false</code>.
   */
  public boolean serverSocketTLS() {
    return booleanValue(SERVER_SOCKET_TLS, Boolean.FALSE.booleanValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverContext
  /**
   ** Sets the context of the server where the target system is deployed on and
   ** this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_CONTEXT}.
   **
   ** @param  context            the context of the server where the target
   **                            system is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   */
  public void serverContext(final String context) {
    property(SERVER_CONTEXT, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverContext
  /**
   ** Returns the context of the server where the target system is deployed on
   ** and this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_CONTEXT}.
   **
   ** @return                    the context of the server where the target
   **                            system is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   */
  public String serverContext() {
    return stringValue(SERVER_CONTEXT);
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
   */
  @Override
  public int hashCode() {
    return (this.name() != null ? this.name().hashCode() : 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Endpoint</code> object that represents
   ** the same <code>name</code> as this object.
   **
   ** @param other             the object to compare this <code>Endpoint</code>
   **                          against.
   **
   ** @return                  <code>true</code> if the <code>Endpoint</code>s
   **                          are equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Endpoint))
      return false;

    if (name() == null && ((Endpoint)other).name() == null)
      return true;
    else if (name() == null && ((Endpoint)other).name() != null)
      return false;
    else if (other == null && name() == null)
      return true;
    else
      return name().equals(((Endpoint)other).name());
  }
}