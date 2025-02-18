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

    File        :   Assertion.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Assertion.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

import java.util.List;

import java.time.ZonedDateTime;

////////////////////////////////////////////////////////////////////////////////
// final class Assertion
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** SAML 2.0 Core Assertion.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Assertion {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** SAML 2.0 Assertion local element name. */
  public static final String LOCAL         = "Assertion";

  /** ID attribute name. */
  public static final String ID            = "ID";

  /** Version attribute name. */
  public static final String VERSION       = "Version";

  /** IssueInstant attribute name. */
  public static final String ISSUE_INSTANT = "IssueInstant";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** ID of the assertion. */
  private final String          id;

  /** Issue Instant of the assertion. */
  private final ZonedDateTime   issueInstant;

  /** Version of the assertion. */
  private final Version         version;

  /** Issuer of the assertion. */
  private final Issuer          issuer;

  /** Subject of the assertion. */
  private final Subject         subject;

  /** Conditions of the assertion. */
  private final Conditions      condition;

  /** Statements of the assertion. */
  private final List<Statement> statement;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link Assertion}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** ID of the assertion. */
    private String          id;

    /** Issue Instant of the assertion. */
    private ZonedDateTime   issueInstant;

    /** Version of the assertion. */
    private Version         version;

    /** Issuer of the assertion. */
    private Issuer          issuer;

    /** Subject of the assertion. */
    private Subject         subject;

    /** Conditions of the assertion. */
    private Conditions      condition;

    /** Statements of the assertion. */
    private List<Statement> statement;

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
    // Method: id
    /**
     ** Sets the ID of the assertion.
     **
     ** @param  value            the ID of the assertion.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder id(final String value) {
      this.id = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: issueInstant
    /**
     ** Sets the issue instant of the assertion.
     **
     ** @param  value            the issue instant of the assertion.
     **                          <br>
     **                          Possible object is {@link ZonedDateTime}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>issueInstant</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder issueInstant(final ZonedDateTime value) {
      this.issueInstant = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: version
    /**
     ** Sets the {@link Version} of the assertion.
     **
     ** @param  value            the {@link Version} of the assertion.
     **                          <br>
     **                          Allowed object is {@link Version}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>issueInstant</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder version(final Version value) {
      this.version = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: issuer
    /**
     ** Sets the {@link Issuer} of the assertion.
     **
     ** @param                   value the {@link Issuer} of the assertion.
     **                          <br>
     **                          Allowed object is {@link Issuer}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>issueInstant</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder issuer(final Issuer value) {
      this.issuer = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: subject
    /**
     ** Sets the {@link Subject} of the assertion.
     **
     ** @param                   value the {@link Subject} of the assertion.
     **                          <br>
     **                          Allowed object is {@link Issuer}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>issueInstant</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder subject(final Subject value) {
      this.subject = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: condition
    /**
     ** Sets the {@link Conditions} of the assertion.
     **
     ** @param                   value the {@link Conditions} of the assertion.
     **                          <br>
     **                          Allowed object is {@link Issuer}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>issueInstant</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder condition(final Conditions value) {
      this.condition = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: statement
    /**
     ** Sets the collection of {@link Statement} s attached to the assertion.
     **
     ** @param  value            the collection of {@link Statement}s attached
     **                          to the assertion.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Statement}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>issueInstant</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder statement(final List<Statement> value) {
      this.statement = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link Assertion}.
     **
     ** @return               the SAML 2.0 {@link Assertion}.
     **                       <br>
     **                       Possible object is {@link Assertion}.
     */
    public Assertion build() {
      return new Assertion(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Assertion</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private Assertion(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.id           = builder.id;
    this.issueInstant = builder.issueInstant;
    this.issuer       = builder.issuer;
    this.version      = builder.version;
    this.subject      = builder.subject;
    this.condition    = builder.condition;
    this.statement    = builder.statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the ID of this assertion.
   **
   ** @return                    the ID of this assertion.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   issueInstant
  /**
   ** Returns the issue instance of this assertion.
   **
   ** @return                    the issue instance of this assertion.
   **                            <br>
   **                            Possible object is {@link ZonedDateTime}.
   */
  public final ZonedDateTime issueInstant() {
    return this.issueInstant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the {@link oracle.iam.platform.saml.v2.schema.Version} of this
   ** assertion.
   **
   ** @return                    the {@link Version} of this assertion.
   **                            <br>
   **                            Possible object is {@link Version}.
   */
  public final Version version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   issuer
  /**
   ** Returns the {@link Issuer} of this assertion.
   **
   ** @return                    the {@link Issuer} of this assertion.
   **                            <br>
   **                            Possible object is {@link Issuer}.
   */
  public final Issuer issuer() {
    return this.issuer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subject
  /**
   ** Returns the {@link Subject} of this assertion.
   **
   ** @return                    the {@link Subject} of this assertion.
   **                            <br>
   **                            Possible object is {@link Subject}.
   */
  public final Subject subject() {
    return this.subject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   condition
  /**
   ** Returns the {@link Conditions} of this assertion.
   **
   ** @return                    the {@link Conditions} of this assertion.
   **                            <br>
   **                            Possible object is {@link Conditions}.
   */
  public final Conditions condition() {
    return this.condition;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   condition
  /**
   ** Returns the collection of {@link Statement} s attached to this assertion.
   **
   ** @return                    the collection of {@link Statement}s attached
   **                            to this assertion.
   **                            <br>
   **                            Possible object is {@link Conditions}.
   */
  public final List<Statement> statement() {
    return this.statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>Assertion</code> {@link Builder}.
   **
   ** @return                    a new <code>Assertion</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>Assertion.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}