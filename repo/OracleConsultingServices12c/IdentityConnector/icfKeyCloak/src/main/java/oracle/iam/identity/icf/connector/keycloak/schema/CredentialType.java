/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   CredentialType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    CredentialType.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

////////////////////////////////////////////////////////////////////////////////
// enum CredentialType
// ~~~~ ~~~~~~~~~~~~~~
/**
 ** The canonical values of a credential.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum CredentialType {
    /**
     ** Simple Password verified localy by Keycloak
     */
    PASSWORD("password")
    /**
     ** Time Based One-Time Password
     */
  , TOTP("totp")
    /**
     ** Counter Based One-Time Password
     */
  , HOTP("hotp")
  , SECRET("secret")
  , KERBEROS("kerberos")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  public final String id;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor for <code>CredentialType</code> with a constraint value.
   **
   ** @param  id            the constraint name (used in REST schemas) of
   **                          the object.
   **                          <br>
   **                          Allowed object is {@link String}.
   */
  CredentialType(final String id) {
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  from
  /**
   ** Factory method to create a proper <code>CredentialType</code> constraint
   ** from the given string value.
   **
   ** @param  id                 the string value the order constraint should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>CredentialType</code> constraint.
   **                            <br>
   **                            Possible object is <code>CredentialType</code>.
   */
  @JsonCreator
  public static CredentialType from(final String id) {
    for (CredentialType cursor : CredentialType.values()) {
      if (cursor.id.equals(id))
        return cursor;
    }
    throw new IllegalArgumentException(id);
  }

  @JsonValue
  public String value() {
    return this.id;
  }
}