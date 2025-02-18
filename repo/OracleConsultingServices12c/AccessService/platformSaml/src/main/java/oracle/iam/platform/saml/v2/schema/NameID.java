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

    File        :   NameID.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    NameID.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

////////////////////////////////////////////////////////////////////////////////
// class NameID
// ~~~~~ ~~~~~~
/**
 ** SAML 2.0 Core NameID.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NameID {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** NameID local element name. */
  public static final String LOCAL                = "NameID";

  /** Format attribute name. */
  public static final String FORMAT               = "Format";

  /** NameQualifier attribute name. */
  public static final String NAME_QUALIFIER       = "NameQualifier";

  /** SPNameQualifier attribute name. */
  public static final String SP_NAME_QUALIFIER    = "SPNameQualifier";

  /** SAML 2.0 NameID SPProviderID attribute name. */
  public static final String SPPROVIDED_ID        = "SPProvidedID";

  /** SAML 1.1 URI for unspecified name format. */
  public static final String UNSPECIFIED          = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";

  /** SAML 1.1 URI for email name format. */
  public static final String EMAIL                = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";

  /** URI for X509 subject name format. */
  public static final String X509_SUBJECT         = "urn:oasis:names:tc:SAML:1.1:nameid-format:x509SubjectName";

  /** SAML 1.1 URI for windows domain qualified name name format. */
  public static final String WIN_DOMAIN_QUALIFIED = "urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName";

  /** SAML 2.0 URI for kerberos name format. */
  public static final String KERBEROS             = "urn:oasis:names:tc:SAML:2.0:nameid-format:kerberos";

   /** SAML 2.0 URI for SAML entity name format. */
  public static final String ENTITY               = "urn:oasis:names:tc:SAML:2.0:nameid-format:entity";

  /** SAML 2.0 URI for persistent name format. */
  public static final String PERSISTENT           = "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent";

  /** SAML 2.0 URI for transient name format. */
  public static final String TRANSIENT            = "urn:oasis:names:tc:SAML:2.0:nameid-format:transient";

  /** SAML 2.0 URI used by NameIDPolicy to indicate a NameID should be encrypted. */
  public static final String ENCRYPTED            = "urn:oasis:names:tc:SAML:2.0:nameid-format:encrypted";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Name of the Name ID. */
  private final String name;

  /**
   ** NameFormat of the Name ID.
   ** <p>
   ** The NameFormat property specifies the format that the Name property is in.
   */
  private final String format;

  /** SP ProvidedID of the NameID. */
  private final String spProvidedID;

  /**
   ** NameQualifier of the Name ID.
   ** <p>
   ** The NameQualifier property specifies the domain in which the Name property
   ** resides in.
   ** <p>
   ** The NameQualifier property corresponds to the NameQualifier attribute of
   ** the <code>&lt;saml:NameIdentifier&gt;</code> element that is defined in
   ** the Assertions and Protocol for the OASIS Security Assertion Markup
   ** Language (SAML) specification.
   */
  private final String nameQualifier;

  /** SP Name Qualifier of the Name ID. */
  private final String spNameQualifier;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link NameID}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Name of the Name ID. */
    private String name;

    /** Format of the Name ID. */
    private String format;

    /** SP ProvidedID of the NameID. */
    private String spProvidedID;

    /** Name Qualifier of the Name ID. */
    private String nameQualifier;

    /** SP Name Qualifier of the Name ID. */
    private String spNameQualifier;

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
    // Method: value
    /**
     ** Sets the value of the NameID.
     **
     ** @param  value            the value of the NameID.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>value</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder value(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: format
    /**
     ** Sets the format of the NameID.
     **
     ** @param  value            the format of the NameID.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>format</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder format(final String value) {
      this.format = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: nameQualifier
    /**
     ** Sets the SPProvddedID of the NameID.
     **
     ** @param  value            the SPProvidedID of the NameID.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>nameQualifier</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder spProvidedID(final String value) {
      this.spProvidedID = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: nameQualifier
    /**
     ** Sets the NameQualifier the NameID.
     **
     ** @param  value            the NameQualifier the NameID.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>nameQualifier</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder nameQualifier(final String value) {
      this.nameQualifier = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: spNameQualifier
    /**
     ** Sets the SPNameQualifier the NameID.
     **
     ** @param  value            the SPNameQualifier the NameID.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>spNameQualifier</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder spNameQualifier(final String value) {
      this.spNameQualifier = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link NameID}.
     **
     ** @return                  the SAML 2.0 {@link NameID}.
     **                          <br>
     **                          Possible object is {@link NameID}.
     */
    public NameID build() {
      return new NameID(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NameID</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private NameID(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name            = builder.name;
    this.format          = builder.format;
    this.spProvidedID    = builder.spProvidedID;
    this.nameQualifier   = builder.nameQualifier;
    this.spNameQualifier = builder.spNameQualifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of this <code>NameID</code>.
   **
   ** @return                    the value of this <code>NameID</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String value() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Returns the format of this <code>NameID</code>.
   **
   ** @return                    the format of this <code>NameID</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String format() {
    return this.format;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   spProvidedID
  /**
   ** Returns the SPProvidedID value this <code>NameID</code>.
   **
   ** @return                    the SPProvidedID value this
   **                            <code>NameID</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String spProvidedID() {
    return this.spProvidedID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameQualifier
  /**
   ** Returns the NameQualifier value this <code>NameID</code>.
   **
   ** @return                    the NameQualifier value this
   **                            <code>NameID</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String nameQualifier() {
    return this.nameQualifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   spNameQualifier
  /**
   ** Returns the SPNameQualifier value this <code>NameID</code>.
   **
   ** @return                    the SPNameQualifier value this
   **                            <code>NameID</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
   public final String spNameQualifier() {
     return this.spNameQualifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>NameID</code> {@link Builder}.
   **
   ** @return                    a new <code>NameID</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>NameID.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}