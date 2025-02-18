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

    File        :   AuthnStatement.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AuthnStatement.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

import java.time.ZonedDateTime;

////////////////////////////////////////////////////////////////////////////////
// final class AuthnStatement
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** SAML 2.0 Core AuthnStatement.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class AuthnStatement implements Statement {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Element local name. */
  public static final String LOCAL         = "AuthnStatement";

  /** AuthnInstant attribute name. */
  public static final String AUTHN_INSTANT = "AuthnInstant";

  /** SessionIndex attribute name. */
  public static final String SESSION_INDEX = "SessionIndex";

  /** SessionNoOnOrAfter attribute name. */
  public static final String SESSION_AFTER = "SessionNotOnOrAfter";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Time when the authentication took place. */
  private final ZonedDateTime   authnInstant;

  /** Index of the session. */
  private final String          sessionIndex;

  /** Time at which the session ends. */
  private final ZonedDateTime   sessionAfter;

  /** Authentication Context of the Authentication Statement. */
  private final AuthnContext    authnContext;

  /** Subject Locality of the Authentication Statement. */
  private final SubjectLocality subjectLocality;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link AuthnStatement}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Time when the authentication took place. */
    private ZonedDateTime  authnInstant;

    /** Index of the session. */
    private String         sessionIndex;

    /** Time at which the session ends. */
    private ZonedDateTime  sessionAfter;

    /** Authentication Context of the Authentication Statement. */
    private AuthnContext   authnContext;

    /** Subject Locality of the Authentication Statement. */
    private SubjectLocality subjectLocality;

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
    // Method: authnInstant
    /**
     ** Sets the time when the authentication took place.
     **
     ** @param  value            the time when the authentication took place.
     **                          <br>
     **                          Allowed object is {@link ZonedDateTime}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder authnInstant(final ZonedDateTime value) {
      this.authnInstant = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sessionIndex
    /**
     ** Sets the session index between the principal and the authenticating
     ** authority.
     **
     ** @param  value            the session index between the principal and the
     **                          authenticating authority.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder sessionIndex(final String value) {
      this.sessionIndex = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sessionAfter
    /**
     ** Sets the time when the session between the principal and the SAML
     ** authority ends.
     **
     ** @param  value            the time when the session between the principal
     **                          and the SAML authority ends.
     **                          <br>
     **                          Allowed object is {@link ZonedDateTime}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder sessionAfter(final ZonedDateTime value) {
      this.sessionAfter = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: authnContext
    /**
     ** Sets the context used to authenticate the subject.
     **
     ** @param value               the context used to authenticate the subject.
     **                            <br>
     **                            Allowed object is {@link AuthnContext}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder authnContext(final AuthnContext value) {
      this.authnContext = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: subjectLocality
    /**
     ** Sets the DNS domain and IP address of the system where the principal was
     ** authenticated.
     **
     ** @param value             the DNS domain and IP address of the system
     **                          where the principal was authenticated.
     **                          <br>
     **                          Allowed object is {@link SubjectLocality}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder subjectLocality(final SubjectLocality value) {
      this.subjectLocality = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link AuthnStatement}.
     **
     ** @return                  the SAML 2.0 {@link AuthnStatement}.
     **                          <br>
     **                          Possible object is {@link AuthnStatement}.
     */
    public AuthnStatement build() {
      return new AuthnStatement(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthnStatement</code> with the specified
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
  private AuthnStatement(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.authnInstant    = builder.authnInstant;
    this.sessionIndex    = builder.sessionIndex;
    this.sessionAfter    = builder.sessionAfter;
    this.authnContext    = builder.authnContext;
    this.subjectLocality = builder.subjectLocality;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authnInstant
  /**
   ** Returns the time when the authentication took place.
   **
   ** @return                    the time when the authentication took place.
   **                            <br>
   **                            Possible object is {@link ZonedDateTime}.
   */
  public final ZonedDateTime authnInstant() {
    return this.authnInstant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionIndex
  /**
   ** Returns the session index between the principal and the authenticating
   ** authority.
   **
   ** @return                    the session index between the principal and the
   **                            authenticating authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String sessionIndex() {
    return this.sessionIndex;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionAfter
  /**
   ** Returns the time when the session between the principal and the SAML
   ** authority ends.
   **
   ** @return                    the time when the session between the principal
   **                            and the SAML authority ends.
   **                            <br>
   **                            Possible object is {@link ZonedDateTime}.
   */
  public final ZonedDateTime sessionAfter() {
    return this.sessionAfter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authnContext
  /**
   ** Returns the context used to authenticate the subject.
   **
   ** @return                    the context used to authenticate the subject.
   **                            <br>
   **                            Possible object is {@link AuthnContext}.
   */
  public final AuthnContext authnContext() {
    return this.authnContext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subjectLocality
  /**
   ** Returns the DNS domain and IP address of the system where the principal
   ** was authenticated.
   **
   ** @return                    the DNS domain and IP address of the system
   **                            where the principal was authenticated.
   **                            <br>
   **                            Possible object is {@link SubjectLocality}.
   */
  public final SubjectLocality subjectLocality() {
    return this.subjectLocality;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>AuthnStatement</code> {@link Builder}.
   **
   ** @return                    a new <code>AuthnStatement</code>
   **                            {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthnStatement.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}