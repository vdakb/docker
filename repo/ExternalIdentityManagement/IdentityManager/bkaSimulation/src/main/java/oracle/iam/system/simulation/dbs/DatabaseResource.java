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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   DatabaseResource.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseResource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseResource
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseResource</code> implements the base functionality of a
 ** Database IT Resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the timeout period for establishment of the connection.
   */
  public static final int     CONNECTION_TIMEOUT_DEFAULT        = 1000;

  /**
   ** Default value of the number of consecutive attempts to be made at
   ** establishing a connection with the target system.
   */
  public static final int     CONNECTION_RETRY_COUNT_DEFAULT    = 2;

  /**
   ** Default value of the interval (in milliseconds) between consecutive
   ** attempts at establishing a connection with the target system.
   */
  public static final int     CONNECTION_RETRY_INTERVAL_DEFAULT = 1000;

  /**
   ** Default value of the timeout period the target system doesn't response.
   */
  public static final int     RESPONSE_TIMEOUT_DEFAULT          = 10000;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The host name or IP address of the database listener on which the Database
   ** Instance to connect to is registered.
   */
  final String host;

  /**
   ** The port number the database listener is listening on and awaiting
   ** requests.
   */
  final int    port;

  /**
   ** The service name the Database Instance to connect is registered at the
   ** database listener.
   */
  final String name;

  /**
   ** The database account with sufficient permission to access the database
   ** schema.
   */
  final String principalName;

  /**
   ** The password of the target system account specified by the
   ** <code>principalName</code>.
   */
  final String principalPassword;

  /**
   ** The time (in milliseconds) within which the target system is expected to
   ** respond to a connection attempt.
   */
  int          connectionTimeOut      = CONNECTION_TIMEOUT_DEFAULT;

  /**
   ** The number of consecutive attempts to be made at establishing a connection
   ** with the target system.
   */
  int          connectionRetryCount   = CONNECTION_RETRY_COUNT_DEFAULT;

  /**
   ** The interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the target system.
   */
  int         connectionRetryInterval = CONNECTION_RETRY_INTERVAL_DEFAULT;

  /**
   ** The timeout period the service provider doesn't get a response.
   ** <p>
   ** If this property has not been specified, the default is to wait for the
   ** response until it is received.
   */
  int        responseTimeOut          = RESPONSE_TIMEOUT_DEFAULT;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseResource</code> resource descriptor.
   **
   ** @param  host               the host name or IP address of the database
   **                            listener on which the Database Instance to
   **                            connect to is registered.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  port               the port number the database listener is
   **                            listening on and awaiting requests.
   **                            <br>
   **                            Default value for non-SSL: 1521
   **                            Default value for SSL: ????
   **                            Allowed object <code>int</code>.
   ** @param  name               the service name the Database Instance to
   **                            connect is registered at the database listener.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  principalName      the database account with sufficient permission
   **                            to access the database schema.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object {@link String}.
   */
  private DatabaseResource(final String host, final int port, final String name, final String principalName, final String principalPassword) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.host              = host;
    this.port              = port;
    this.name              = name;
    this.principalName     = principalName;
    this.principalPassword = principalPassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   host
  /**
   ** Returns the host name or IP address of the database listener on which the
   ** Database Instance to connect to is registered.
   **
   ** @return                    the host name or IP address of the database
   **                            listener on which the Database Instance to
   **                            connect to is registered.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String host() {
    return this.host;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port
  /**
   ** Returns the port number the database listener is listening on and awaiting
   ** requests.
   **
   ** @return                    the port number the database listener is
   **                            listening on and awaiting requests.
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  public final int port() {
    return this.port;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the service name the Database Instance to connect is registered at
   ** the database listener.
   **
   **
   ** @return                    the service name the Database Instance to
   **                            connect is registered at the database listener.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the name of the principal of a target system to establish a
   ** connection.
   **
   ** @return                    the name of the principal to establish a
   **                            connection.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String principalName() {
    return this.principalName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password for the principal of a target system to establish
   ** a connection.
   **
   ** @return                    the password for the principal of a target
   **                            system to establish a connection.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String principalPassword() {
    return this.principalPassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionTimeOut
  /**
   ** Sets the time (in milliseconds) within which the target system is expected
   ** to respond to a connection attempt.
   **
   ** @param  value              the time (in milliseconds) within which the
   **                            target system is expected to respond to a
   **                            connection attempt.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the {@link DatabaseResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link DatabaseResource}.
   */
  public final DatabaseResource connectionTimeOut(final int value) {
    this.connectionTimeOut = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionTimeOut
  /**
   ** Returns the time (in milliseconds) within which the target system is
   ** expected to respond to a connection attempt.
   **
   ** @return                    the time (in milliseconds) within which the
   **                            target system is expected to respond to a
   **                            connection attempt.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int connectionTimeOut() {
    return this.connectionTimeOut;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionRetryCount
  /**
   ** Sets the number of consecutive attempts to be made at establishing a
   ** connection with the target system.
   **
   ** @param  value              the number of consecutive attempts to be made
   **                            at establishing a connection with the target
   **                            system.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the {@link DatabaseResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link DatabaseResource}.
   */
  public final DatabaseResource connectionRetryCount(final int value) {
    this.connectionRetryCount = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionRetryCount
  /**
   ** Returns the number of consecutive attempts to be made at establishing a
   ** connection with the target system.
   **
   ** @return                    the number of consecutive attempts to be made
   **                            at establishing a connection with the target
   **                            system.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int connectionRetryCount() {
    return this.connectionRetryCount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionRetryInterval
  /**
   ** Sets the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the target system.
   **
   ** @param  value              the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the target system.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the {@link DatabaseResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link DatabaseResource}.
   */
  public final DatabaseResource connectionRetryInterval(final int value) {
    this.connectionRetryInterval = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionRetryInterval
  /**
   ** Returns the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the target system.
   **
   ** @return                    the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the target system.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int connectionRetryInterval() {
    return this.connectionRetryInterval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseTimeOut
  /**
   ** Sets timeout period the service provider doesn't get a response.
   **
   ** @param  value              the timeout period the service provider doesn't
   **                            get a response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the {@link DatabaseResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link DatabaseResource}.
   */
  public final DatabaseResource responseTimeOut(final int value) {
    this.responseTimeOut = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseTimeOut
  /**
   ** Returns the timeout period the service provider doesn't get a response.
   **
   ** @return                    the timeout period the service provider doesn't
   **                            get a response.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int responseTimeOut() {
    return this.responseTimeOut;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DatabaseResource</code> resource
   ** descriptor.
   **
   ** @param  host               the host name or IP address of the database
   **                            listener on which the Database Instance to
   **                            connect to is registered.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  port               the port number the database listener is
   **                            listening on and awaiting requests.
   **                            <br>
   **                            Default value for non-SSL: 1521
   **                            Default value for SSL: ????
   **                            Allowed object <code>int</code>.
   ** @param  name               the service name the Database Instance to
   **                            connect is registered at the database listener.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  principalName      the database account with sufficient permission
   **                            to access the database schema.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object {@link String}.
   **
   ** @return                   the endpoint descriptor of a database.
   **                            <br>
   **                            Possible object {@link DatabaseResource}.
   */
  public static DatabaseResource build(final String host, final int port, final String name, final String principalName, final String principalPassword) {
    return new DatabaseResource(host, port, name, principalName, principalPassword);
  }

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
   **
   ** @see    Object#equals(Object)
   ** @see    Object#hashCode()
   ** @see    #equals(Object)
   */
  @Override
  public int hashCode() {
    return 31 * this.port
         + 31 * (this.host == null              ? 0 : this.host.hashCode())
         + 31 * (this.name == null              ? 0 : this.name.hashCode())
         + 31 * (this.principalName == null     ? 0 : this.principalName.hashCode())
         + 31 * (this.principalPassword == null ? 0 : this.principalPassword.hashCode());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>DatabaseResource</code>s are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>DatabaseResource</code>s may be different even though they
   ** contain the same set of names with the same values, but in a different
   ** order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final DatabaseResource that = (DatabaseResource)other;
    if (this.port != that.port)
      return false;

    if (this.host != null ? !this.host.equals(that.port) : that.host != null)
      return false;

    if (this.name != null ? !this.name.equals(that.port) : that.name != null)
      return false;

    if (this.principalName != null ? !this.principalName.equals(that.principalName) : that.principalName != null)
      return false;

    return (this.principalPassword == null ? principalPassword.equals(that.principalPassword) : that.principalPassword == null);
  }
}