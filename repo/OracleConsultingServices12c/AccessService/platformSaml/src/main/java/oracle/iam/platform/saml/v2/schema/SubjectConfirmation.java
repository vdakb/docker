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

    File        :   SubjectConfirmation.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SubjectConfirmation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

////////////////////////////////////////////////////////////////////////////////
// final class SubjectConfirmation
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** SAML 2.0 Core SubjectConfirmation.
 ** <p>
 ** A SAML Assertion may contain an element called
 ** <code>SubjectConfirmation</code>.
 ** <br>
 ** In practical terms, what <code>SubjectConfirmation</code> says is "these are
 ** the conditions under which an attesting entity (somebody trying to use the
 ** assertion) is permitted to do so".
 ** <br>
 ** The entity trying to use the assertion, or the "wielder", is attesting to
 ** its right to do so, usually by implying a relationship with the subject. An
 ** assertion can have any number of <code>SubjectConfirmation</code> elements,
 ** but an attesting entity only has to satisfy one of them.
 ** <p>
 ** The <code>SubjectConfirmation</code> element provides the means for a
 ** relying party to verify the correspondence of the subject of the assertion
 ** with the party with whom the relying party is communicating. The method
 ** attribute indicates the specific method that the relying party should use
 ** to make this determination.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class SubjectConfirmation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Element local name. */
  public static final String            LOCAL  = "SubjectConfirmation";

  /** Method attribute name. */
  public static final String            METHOD = "Method";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** BaseID child element. */
  private final BaseID                  baseID;

  /** NameID child element. */
  private final NameID                  nameID;

  /** EncryptedID child element. */
  private final EncryptedID             encryptedID;

  /** SubjectConfirmationData of the Confirmation. */
  private final SubjectConfirmationData data;

  /** Method of the Confirmation. */
  private final String                  method;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link SubjectConfirmation}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** BaseID child element. */
    private BaseID                   baseID;

    /** NameID child element. */
    private NameID                   nameID;

    /** EncryptedID child element. */
    private EncryptedID             encryptedID;

    /**Data of the  SubjectConfirmation. */
    private SubjectConfirmationData data;

    /** Method of the Confirmation. */
    private String                  method;

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
    // Method: baseID
    /**
     ** Sets the base identifier of the principal for the request.
     **
     ** @param  value            the base identifier of the principal for the
     **                          request.
     **                          <br>
     **                          Allowed object is {@link BaseID}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder baseID(final BaseID value) {
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
     **                          Allowed object is {@link EncryptedID}.
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
    // Method: data
    /**
     ** Sets the data about how this subject was confirmed or constraints on the
     ** confirmation.
     **
     ** @param  value            the data about how this subject was confirmed
     **                          or constraints on the confirmation.
     **                          <br>
     **                          Allowed object is
     **                          {@link SubjectConfirmationData}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder data(final SubjectConfirmationData value) {
      this.data = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: method
    /**
     ** Sets the method used to confirm this subject.
     **
     ** @param  value            the method used to confirm this subject.
     **                          <br>
     **                          Allowed object is {@link NameID}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder method(final String value) {
      this.method = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 Core {@link SubjectConfirmation}.
     **
     ** @return                  the SAML 2.0 Core {@link SubjectConfirmation}.
     **                          <br>
     **                          Possible object is {@link SubjectConfirmation}.
     */
    public SubjectConfirmation build() {
      return new SubjectConfirmation(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SubjectConfirmation</code> with the specified
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
  private SubjectConfirmation(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.data        = builder.data;
    this.baseID      = builder.baseID;
    this.nameID      = builder.nameID;
    this.encryptedID = builder.encryptedID;
    this.method      = builder.method;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   baseID
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
  // Method:   data
  /**
   ** Returns the data about how this subject was confirmed or constraints on
   ** the confirmation.
   **
   ** @return                    the data about how this subject was confirmed
   **                            or constraints on the confirmation.
   **                            <br>
   **                            Possible object is
   **                            {@link SubjectConfirmationData}.
   */
  public final SubjectConfirmationData data() {
    return this.data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Returns the method used to confirm this subject.
   **
   ** @return                    the method used to confirm this subject.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String method() {
    return this.method;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>SubjectConfirmation</code>
   ** {@link Builder}.
   **
   ** @return                    a new <code>SubjectConfirmation</code>
   **                            {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>SubjectConfirmation.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}