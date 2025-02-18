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

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AuthzStatement.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AuthzStatement.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

import java.util.List;

////////////////////////////////////////////////////////////////////////////////
// final class AuthzStatement
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** SAML 2.0 Core AuthnStatement.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class AuthzStatement implements Statement {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Element local name. */
  public static final String LOCAL    = "AuthzDecisionStatement";

  /** Resource attribute name. */
  public static final String RESOURCE = "Resource";

  /** Decision attribute name. */
  public static final String DECISION = "Decision";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** URI of the resource to which authorization is sought. */
  private final String       resource;

  /** Actions authorized to be performed. */
  private final List<Action> action;

  /** Decision of the authorization request. */
  private final Decision     decision;

  /**
   ** SAML assertion the authority relied on when making the authorization
   ** decision.
   */
  private final Evidence     evidence;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Decision
  // ~~~~~ ~~~~~~~~
  /**
   * A type safe enumeration of {@link AuthzStatement} decision types.
   */
  public enum Decision {
      /** Permit decision type. */
      Permit("permit"),

    /** Permit decision type. */
    Deny("deny")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The lower case string value for this decision type. */
    private final String type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Decision</code> with a constraint value.
     **
     ** @param  value            the decision type of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Decision(final String value) {
      this.type = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Decision</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Decision</code> constraint.
     **                          <br>
     **                          Possible object is <code>Decision</code>.
     */
    public static Decision from(final String value) {
      for (Decision cursor : AuthzStatement.Decision.values()) {
        if (cursor.type.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link AuthzStatement}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** URI of the resource to which authorization is sought. */
    private String       resource;

    /** Actions authorized to be performed. */
    private List<Action> action;

    /** Decision of the authorization request. */
    private Decision     decision;

    /**
     ** SAML assertion the authority relied on when making the authorization
     ** decision.
     */
    private Evidence     evidence;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Builder</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: resource
    /**
     ** Sets the URI of the resource to which authorization is saught.
     **
     ** @param  value            the URI of the resource to which authorization
     **                          is saught.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder resource(final String value) {
      this.resource = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: action
    /**
     ** Sets the actions authorized to be performed.
     **
     ** @param  value            the actions authorized to be performed.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Action}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder action(final List<Action> value) {
      this.action = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decision
    /**
     ** Sets the decision type of the authorization request.
     **
     ** @param  value            the decision type of the authorization request.
     **                          <br>
     **                          Allowed object is {@link Evidence}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder decision(final Decision value) {
      this.decision = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: evidence
    /**
     ** Sets the SAML assertion the authority relied on when making the
     ** authorization decision.
     **
     ** @param  value            the SAML assertion the authority relied on when
     **                          making the authorization decision.
     **                          <br>
     **                          Allowed object is {@link Evidence}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder evidence(final Evidence value) {
      this.evidence = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link AuthzStatement}.
     **
     ** @return                  the SAML 2.0 {@link AuthzStatement}.
     **                          <br>
     **                          Possible object is {@link AuthzStatement}.
     */
    public AuthzStatement build() {
      return new AuthzStatement(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthzStatement</code> with the specified
   ** {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private AuthzStatement(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.action   = builder.action;
    this.decision = builder.decision;
    this.evidence = builder.evidence;
    this.resource = builder.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the URI of the resource to which authorization is saught.
   **
   ** @return                    the URI of the resource to which authorization
   **                            is saught.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the actions authorized to be performed.
   **
   ** @return                    the actions authorized to be performed.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Action}.
   */
  public final List<Action> action() {
    return this.action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decision
  /**
   ** Returns the decision of the authorization request.
   **
   ** @return                    the decision of the authorization request.
   **                            <br>
   **                            Possible object is {@link Decision}.
   */
  public final Decision decision() {
    return this.decision;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evidence
  /**
   ** Returns the SAML assertion the authority relied on when making the
   ** authorization decision.
   **
   ** @return                    the SAML assertion the authority relied on when
   **                            making the authorization decision.
   **                            <br>
   **                            Possible object is {@link Evidence}.
   */
  public final Evidence evidence() {
    return this.evidence;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>AuthzStatement</code> {@link Builder}.
   **
   ** @return                    a new <code>AuthzStatement</code>
   **                            {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthzStatement.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}