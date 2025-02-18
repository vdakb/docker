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

    File        :   ActionType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    ActionType.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

////////////////////////////////////////////////////////////////////////////////
// enum ActionType
// ~~~~ ~~~~~~~~~~
/**
 ** The canonical values of a required user action.
 ** <p>
 ** Required Actions are tasks that a user must finish before they are allowed
 ** to log in. A user must provide their credentials before required actions are
 ** executed. Once a required action is completed, the user will not have to
 ** perform the action again.
 ** <p>
 ** <b>Built-in required action types</b>
 ** <br>
 ** <ul>
 **   <li>Update Password (UPDATE_PASSWORD)
 **       <br>
 **       When set, a user must change their password.
 **   <li>Configure OTP (CONFIGURE_TOTP)
 **       <br>
 **       When set, a user must configure a one-time password generator on their
 **       mobile device using either the Free OTP or Google Authenticator
 **       application.
 **   <li>Verify Email (VERIFY_EMAIL)
 **       <br>
 **       When set, a user must verify that they have a valid email account. An
 **       email will be sent to the user with a link they have to click. Once
 **       this workflow is successfully completed, they will be allowed to log
 **       in.
 **   <li>Update Profile (UPDATE_PROFILE)
 **       <br>
 **       This required action asks the user to update their profile
 **       information, i.e. their name, address, email, and/or phone number.
 **   <li>Update User Locale (update_user_locale)
 **   <li>Terms and Conditions (TERMS_AND_CONDITIONS)
 **   <li>Webauthn Register (webauthn-register)
 **   <li>Webauthn Register Passwordless (webauthn-register-passwordless)
 ** </ul>
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Be carefull in changing the Enum names. By default, Jackson will use the
 ** Enum name to deserialize from JSON.
 ** <p>
 ** The <code>id</code> wrapped in this Enum is the suffix of the layered
 ** attribute passed through ICF Framwork.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum ActionType {
    /**
     ** When set, a user must verify that they have a valid email account. An
     ** email will be sent to the user with a link they have to click. Once
     ** this workflow is successfully completed, they will be allowed to log
     ** in.
     */
    EMAILVERIFY("VERIFY_EMAIL", "emailVerify")
    /**
     **
     */
  , DELETEACCOUNT("delete_account", "deleteAccount")
    /**
     **
     */
  , PROFILEVERIFY("VERIFY_PROFILE", "profileVerify")
    /**
     ** This required action asks the user to update their profile
     ** information, i.e. their name, address, email, and/or phone number.
     */
  , PROFILEUPDATE("UPDATE_PROFILE", "profileUpdate")
    /**
     ** When set, a user must change their password.
     */
  , PASSWORDUPDATE("UPDATE_PASSWORD", "passwordUpdate")
    /**
     ** This required action asks the user to update their profile
     ** information, i.e. their local.
     */
  , LOCALEUPDATE("update_user_locale", "localeUpdate")
    /**
     ** When set, a user must configure a one-time password generator on their
     ** mobile device using either the <i>Free OTP</i> or
     ** <i>Google Authenticator</i> application.
     */
  , CONFIGUREOTP("CONFIGURE_TOTP", "configureOTP")
    /**
     ** When set, a user must confirm the Terms and Conditions to use the
     ** service.
     */
  , CONFIRMED("TERMS_AND_CONDITIONS", "termsAndCondition")
    /**
     **
     */
  , REGISTER("webauthn-register", "webauthnRegister")
    /**
     **
     */
  , PASSWORDLESS("webauthn-register-passwordless", "webauthnPassword")
  , DELETECREDENTIAL("delete_credential", "deleteCredential")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  public final String id;

  public final String origin;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>ActionType</code> with a constraint value.
   **
   ** @param  origin           the constraint name (used in REST schemas) of
   **                          the object.
   **                          <br>
   **                          Allowed object is {@link String}.
   ** @param  id               the constraint name (used in ICF schemas) of
   **                          the object.
   **                          <br>
   **                          Allowed object is {@link String}.
   */
  ActionType(final String origin, final String id) {
    this.origin = origin;
    this.id     = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Factory method to create a proper <code>ActionType</code> constraint from
   ** the given string value.
   **
   ** @param  id                 the string value the type constraint should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ActionType</code> constraint.
   **                            <br>
   **                            Possible object is <code>ActionType</code>.
   */
  @JsonCreator
  public static ActionType origin(final String id) {
    for (ActionType cursor : ActionType.values()) {
      if (cursor.origin.equals(id))
        return cursor;
    }
    throw new IllegalArgumentException(id);
  }

  @JsonValue
  public String value() {
    return origin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a proper <code>ActionType</code> constraint from
   ** the given string value.
   **
   ** @param  id                 the string value the type constraint should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ActionType</code> constraint.
   **                            <br>
   **                            Possible object is <code>ActionType</code>.
   */
  public static ActionType from(final String id) {
    for (ActionType cursor : ActionType.values()) {
      if (cursor.id.equals(id))
        return cursor;
    }
    throw new IllegalArgumentException(id);
  }
}