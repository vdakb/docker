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

    File        :   Subject.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Subject.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

import java.util.List;

////////////////////////////////////////////////////////////////////////////////
// final class Subject
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** SAML 2.0 Core Subject.
 ** <p>
 ** Identifies the subject of a SAML assertion, which is typically the user who
 ** is being authenticated.
 ** <br>
 ** It corresponds to the <code>&lt;saml:Subject&gt;&lt;saml:NameID&gt;</code>
 ** element in the SAML assertion.
 ** <br>
 ** Default value is preferred_username. Most service providers use the user
 ** name as the name identifier.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Subject {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** SAML 2.0 Subject local element name. */
  public static final String              LOCAL           = "Subject";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** BaseID child element. */
  private final BaseID                    baseID;

  /** NameID child element. */
  private final NameID                    nameID;

  /** EncryptedID child element. */
  private final EncryptedID               encryptedID;

  /** Subject Confirmations of the Subject. */
  private final List<SubjectConfirmation> confirmation;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link Subject}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** BaseID child element. */
    private BaseID                    baseID;

    /** NameID child element. */
    private NameID                    nameID;

    /** EncryptedID child element. */
    private EncryptedID               encryptedID;

    /** Subject Confirmations of the Subject. */
    private List<SubjectConfirmation> confirmation;

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
    // Method: nameID
    /**
     ** Sets the base identifier of the principal for this request.
     **
     ** @param  value            the base identifier of the principal for this
     **                          request.
     **                          <br>
     **                          Allowed object is {@link NameID}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder nameID(final BaseID value) {
      this.baseID = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: nameID
    /**
     ** Sets the name identifier of the principal for the request.
     **
     ** @param  value            the name identifier of the principal for the
     **                          request.
     **                          <br>
     **                          Allowed object is {@link NameID}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder nameID(final NameID value) {
      this.nameID = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: encryptedID
    /**
     ** Sets the encrypted name identifier of the principal for this request.
     **
     ** @param  value            the encrypted name identifier of the principal
     **                          for this request.
     **                          <br>
     **                          Allowed object is {@link NameID}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder encryptedID(final EncryptedID value) {
      this.encryptedID = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: confirmation
    /**
     ** Sets the confirmations made about the subject.
     **
     ** @param  value            the confirmations made about the subject.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link SubjectConfirmation}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder confirmation(final List<SubjectConfirmation> value) {
      this.confirmation = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link Subject}.
     **
     ** @return                  the SAML 2.0 {@link Subject}.
     **                          <br>
     **                          Possible object is {@link Subject}.
     */
    public Subject build() {
      return new Subject(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Subject</code> with the specified {@link Builder}.
   **
   ** @param builder the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private Subject(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.baseID       = builder.baseID;
    this.nameID       = builder.nameID;
    this.encryptedID  = builder.encryptedID;
    this.confirmation = builder.confirmation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   BaseID
  /**
   ** Returns the base identifier of the principal for this request.
   **
   ** @return                    the base identifier of the principal for this
   **                            request.
   **                            <br>
   **                            Possible object is {@link BaseID}.
   */
  public final BaseID baseID() {
    return this.baseID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameID
  /**
   ** Returns the name identifier of the principal for this request.
   **
   ** @return                    the name identifier of the principal for this
   **                            request.
   **                            <br>
   **                            Possible object is {@link NameID}.
   */
  public final NameID nameID() {
    return this.nameID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encryptedID
  /**
   ** Returns the encrypted name identifier of the principal for this request.
   **
   ** @return                    the encrypted name identifier of the principal
   **                            for this request.
   **                            <br>
   **                            Possible object is {@link EncryptedID}.
   */
  public final EncryptedID encryptedID() {
    return this.encryptedID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirmation
  /**
   * Returns the confirmations made about the subject.
   *
   * @return                     the confirmations made about the subject.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link SubjectConfirmation}.
   */
  public final List<SubjectConfirmation> confirmation() {
    return this.confirmation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>Subject</code> {@link Builder}.
   **
   ** @return                    a new <code>Subject</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>Subject.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}