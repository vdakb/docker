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

    File        :   GoogleFlowBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    GoogleFlowBuilder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.authn.oauth.v2;

public class GoogleFlowBuilder extends AbstractFlowBuilder<GoogleFlowBuilder> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Property key that defines values for "login_hint" parameter used in Google
   ** OAuth flow.
   */
  public static final String LOGIN_HINT = "login_hint";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum AccessType
  // ~~~~ ~~~~~~~~~~
  /**
   ** Enum that defines values for "access_type" parameter used in Google OAuth
   ** flow.
   ** <br>
   ** Defines whether the offline access is allowed (without user active session).
   */
  public static enum AccessType {
      ONLINE("online")
    , OFFLINE("offline")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>AccessType</code> with a constraint value.
     **
     ** @param  value            the constraint name used in Google OAuth flow.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    AccessType(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: key
    /**
     ** Returns the property name of the access type constraint.
     **
     ** @return                  the property name of the access type
     **                          constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public static String key() {
      return "access_type";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Prompt
  // ~~~~ ~~~~~~
  /**
   ** Enum that defines values for "prompt" parameter used in Google OAuth flow.
   */
  public static enum Prompt {

      NONE("none")
      /**
       ** User will be asked for approval each time the authorization is
       ** performed.
       */
    , CONSENT("consent")
    , SELECT("select_account")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Prompt</code> with a constraint value.
     **
     ** @param  value            the constraint name used in Google OAuth flow.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Prompt(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: key
    /**
     ** Returns the property name of the prompt constraint.
     **
     ** @return                  the property name of the prompt constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public static String key() {
      return "prompt";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Display
  // ~~~~ ~~~~~~~
  /**
   ** Enum that defines values for "display" parameter used in Google OAuth
   ** flow.
   */
  public static enum Display {
      PAGE("page")
    , POPUP("popup")
    , TOUCH("touch")
    , WAP("wap")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Display</code> with a constraint value.
     **
     ** @param  value            the constraint name used in Google OAuth flow.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Display(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: key
    /**
     ** Returns the property name of the display constraint.
     **
     ** @return                  the property name of the display constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public static String key() {
      return "display";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>GoogleFlowBuilder</code> for OAuth2 that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GoogleFlowBuilder() {
    super(new Authorization.Builder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessType
  /**
   ** Set <code>access type</code> parameter used in Authorization Request.
   **
   ** @param  accessType         the access type value.
   **                            <br>
   **                            Allowed object is {@link AccessType}.
   **
   ** @return                    the {@link GoogleFlowBuilder} authorization
   **                            flow builder.
   **                            <br>
   **                            Possible object is {@link GoogleFlowBuilder}.
   */
  public GoogleFlowBuilder accessType(final AccessType accessType) {
    return property(AuthorizationFlow.Phase.AUTHORIZATION, AccessType.key(), accessType.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prompt
  /**
   ** Set <code>prompt</code> parameter used in Authorization Request.
   **
   ** @param  prompt             the prompt value.
   **                            <br>
   **                            Allowed object is {@link Prompt}.
   **
   ** @return                    the {@link GoogleFlowBuilder} authorization
   **                            flow builder.
   **                            <br>
   **                            Possible object is {@link GoogleFlowBuilder}.
   */
  public GoogleFlowBuilder prompt(final Prompt prompt) {
    return property(AuthorizationFlow.Phase.AUTHORIZATION, Prompt.key(), prompt.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Set <code>display</code> parameter used in Authorization Request.
   **
   ** @param  display            the display value.
   **                            <br>
   **                            Allowed object is {@link Display}.
   **
   ** @return                    the {@link GoogleFlowBuilder} authorization
   **                            flow builder.
   **                            <br>
   **                            Possible object is {@link GoogleFlowBuilder}.
   */
  public GoogleFlowBuilder display(final Display display) {
    return property(AuthorizationFlow.Phase.AUTHORIZATION, Display.key(), display.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginHint
  /**
   ** Set <code>login hint</code> parameter used in Authorization Request.
   **
   ** @param  loginHint          the login hint value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link GoogleFlowBuilder} authorization
   **                            flow builder.
   **                            <br>
   **                            Possible object is {@link GoogleFlowBuilder}.
   */
  public GoogleFlowBuilder loginHint(final String loginHint) {
    return property(AuthorizationFlow.Phase.AUTHORIZATION, Display.key(), loginHint);
  }
}