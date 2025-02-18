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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   Principal.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Principal.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.network;

///////////////////////////////////////////////////////////////////////////////
// class Principal
// ~~~~~ ~~~~~~~~~
/**
 ** Identifier that contains information about username and credential to
 ** authenticate an application client or user.
 ** <br>
 ** <code>Principal</code> is a value holder for the security principal used
 ** to authenticate a connection request.
 ** <p>
 ** The class stores secret as byte array to improve security.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Principal {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the user name of the <code>Service Provider</code> endpoint
   ** account used to establish a connection.
   */
  public final String    username;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the credential of the <code>Service Provider</code> endpoint
   ** account used to establish a connection.
   */
  protected final byte[] password;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Principal</code> with <code>username</code> and
   ** <code>password</code>.
   **
   ** @param  username           the <code>username</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the <code>password</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   */
  private Principal(final String username, final byte[] password) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.username = username;
    this.password = password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Returns the <code>username</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>username</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String username() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the <code>password</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>password</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  public final byte[] password() {
    return password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Principal</code> with the specified
   ** username and password.
   **
   ** @param  username           the principal username to authenticae.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password credential to authenticate the
   **                            given principal username.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>Principal</code> populated with
   **                            the properties specified.
   **                            <br>
   **                            Possible object is <code>Principal</code>.
   */
  public static Principal of(final String username, final String password) {
    return of(username, password.getBytes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Principal</code> with the specified
   ** username and password.
   **
   ** @param  username           the principal username to authenticae.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password credential to authenticate the
   **                            given principal username.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the <code>Principal</code> populated with
   **                            the properties specified.
   **                            <br>
   **                            Possible object is <code>Principal</code>.
   */
  public static Principal of(final String username, final byte[] password) {
    return new Principal(username, password);
  }
}
