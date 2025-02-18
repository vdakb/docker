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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic WebService Connector

    File        :   ClientIdentifier.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ClientIdentifier.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.authn.oauth.v2;

///////////////////////////////////////////////////////////////////////////////
// class ClientIdentifier
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Client Identifier that contains information about client id and client
 ** secret issues by a Service Provider for application.
 ** <br>
 ** The class stores client secret as byte array to improve security.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ClientIdentifier {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String clientPublic;
  private final byte[] clientSecret;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ClientIdentifier</code> with client id and client
   ** secret in form of String value.
   **
   ** @param  clientPublic       the public client identifier.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientSecret       the client secret.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public ClientIdentifier(final String clientPublic, final String clientSecret) {
    // ensure inheritance
    this(clientPublic, clientSecret.getBytes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ClientIdentifier</code> with client id and client
   ** secret in form of byte array.
   **
   ** @param  clientPublic       the public client identifier.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientSecret       the client secret.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   */
  public ClientIdentifier(final String clientPublic, final byte[] clientSecret) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.clientPublic = clientPublic;
    this.clientSecret = clientSecret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientPublic
  /**
   ** Returns the public client identifier.
   **
   ** @return                    the public client identifier.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String clientPublic() {
    return this.clientPublic;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientSecret
  /**
   ** Returns the client secret.
   **
   ** @return                    the client secret.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  public byte[] clientSecret() {
    return this.clientSecret;
  }
}