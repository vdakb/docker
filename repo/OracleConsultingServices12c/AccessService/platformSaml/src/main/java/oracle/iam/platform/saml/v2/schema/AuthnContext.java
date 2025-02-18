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

    File        :   AuthnContext.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AuthnContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

////////////////////////////////////////////////////////////////////////////////
// final class AuthnContext
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** SAML 2.0 Core AuthnContext.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class AuthnContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Local Name of AuthnContext. */
  public static final String LOCAL                            = "AuthnContext";

  /** URI for Internet Protocol authentication context. */
  public static final String IP_AUTHN_CTX                     = "urn:oasis:names:tc:SAML:2.0:ac:classes:InternetProtocol";

  /** URI for Internet Protocol Password authentication context. */
  public static final String IP_PASSWORD_AUTHN_CTX            = "urn:oasis:names:tc:SAML:2.0:ac:classes:InternetProtocolPassword";

  /** URI for Kerberos authentication context. */
  public static final String KERBEROS_AUTHN_CTX               = "urn:oasis:names:tc:SAML:2.0:ac:classes:Kerberos";

  /** URI for Mobile One Factor Unregistered authentication context. */
  public static final String MOFU_AUTHN_CTX                   = "urn:oasis:names:tc:SAML:2.0:ac:classes:MobileOneFactorUnregistered";

  /** URI for Mobile Two Factor Unregistered authentication context. */
  public static final String MTFU                             = "urn:oasis:names:tc:SAML:2.0:ac:classes:MobileTwoFactorUnregistered";

  /** URI for Mobile One Factor Contract authentication context. */
  public static final String MOFC_AUTHN_CTX                   = "urn:oasis:names:tc:SAML:2.0:ac:classes:MobileOneFactorContract";

  /** URI for Mobile Two Factor Contract authentication context. */
  public static final String MTFC_AUTHN_CTX                   = "urn:oasis:names:tc:SAML:2.0:ac:classes:MobileTwoFactorContract";

  /** URI for Password authentication context. */
  public static final String PASSWORD_AUTHN_CTX               = "urn:oasis:names:tc:SAML:2.0:ac:classes:Password";

  /** URI for Password Protected Transport authentication context. */
  public static final String PPT_AUTHN_CTX                    = "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport";

  /** URI for Previous Session authentication context. */
  public static final String PREVIOUS_SESSION_AUTHN_CTX       = "urn:oasis:names:tc:SAML:2.0:ac:classes:PreviousSession";

  /** URI for X509 Public Key authentication context. */
  public static final String X509_AUTHN_CTX                   = "urn:oasis:names:tc:SAML:2.0:ac:classes:X509";

  /** URI for PGP authentication context. */
  public static final String PGP_AUTHN_CTX                    = "urn:oasis:names:tc:SAML:2.0:ac:classes:PGP";

  /** URI for SPKI authentication context. */
  public static final String SPKI_AUTHN_CTX                   = "urn:oasis:names:tc:SAML:2.0:ac:classes:SPKI";

  /** URI for XML Digital Signature authentication context. */
  public static final String XML_DSIG_AUTHN_CTX               = "urn:oasis:names:tc:SAML:2.0:ac:classes:XMLDSig";

  /** URI for Smart Card authentication context. */
  public static final String SMARTCARD_AUTHN_CTX              = "urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard";

  /** URI for Smart Card PKI authentication context. */
  public static final String SMARTCARD_PKI_AUTHN_CTX          = "urn:oasis:names:tc:SAML:2.0:ac:classes:SmartcardPKI";

  /** URI for Software PKU authentication context. */
  public static final String SOFTWARE_PKI_AUTHN_CTX           = "urn:oasis:names:tc:SAML:2.0:ac:classes:SoftwarePKI";

  /** URI for Telephony authentication context. */
  public static final String TELEPHONY_AUTHN_CTX              = "urn:oasis:names:tc:SAML:2.0:ac:classes:Telephony";

  /** URI for Nomadic Telephony authentication context. */
  public static final String NOMAD_TELEPHONY_AUTHN_CTX        = "urn:oasis:names:tc:SAML:2.0:ac:classes:NomadTelephony";

  /** URI for Personalized Telephony authentication context. */
  public static final String PERSONAL_TELEPHONY_AUTHN_CTX      = "urn:oasis:names:tc:SAML:2.0:ac:classes:PersonalTelephony";

  /** URI for Authenticated Telephony authentication context. */
  public static final String AUTHENTICATED_TELEPHONY_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:AuthenticatedTelephony";

  /** URI for Secure Remote Password authentication context. */
  public static final String SRP_AUTHN_CTX                     = "urn:oasis:names:tc:SAML:2.0:ac:classes:SecureRemotePassword";

  /** URI for SSL/TLS Client authentication context. */
  public static final String TLS_CLIENT_AUTHN_CTX              = "urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient";

  /** URI for Time Synchornized Token authentication context. */
  public static final String TIME_SYNC_TOKEN_AUTHN_CTX         = "urn:oasis:names:tc:SAML:2.0:ac:classes:TimeSyncToken";

  /** URI for unspecified authentication context. */
  public static final String UNSPECIFIED_AUTHN_CTX             = "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** URI of the Context Class. */
  private final ClassRef classRef;

  /** Declaration of the Authentication Context. */
  private final Decl     decl;

  /** URI of the Declaration of the Authentication Context. */
  private final DeclRef  declRef;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Any
  // ~~~~~ ~~~~
  /**
   ** SAML 2.0 Core AuthnContextDecl.
   */
  public static class Any {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Any</code> with the specified value.
     **
     ** @param  value            the value of the <code>Any</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    protected Any(final String value) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Any</code> element.
     **
     ** @return                  the value of the <code>Any</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class ClassRef
  // ~~~~~ ~~~~~ ~~~~~~~~
  /**
   ** SAML 2.0 Core AuthnContextClassRef.
   */
  public static final class ClassRef extends Any {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Local Name of AuthnContext. */
    public static final String LOCAL = "AuthnContextClassRef";

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AuthnContextClassRef</code> with the specified value.
     **
     ** @param  value            the value of the
     **                          <code>AuthnContextClassRef</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private ClassRef(final String value) {
      // ensure inheritance
      super(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Decl
  // ~~~~~~~~~ ~~~~
  /**
   ** SAML 2.0 Core AuthnContextDecl.
   */
  public static final class Decl extends Any {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Local Name of AuthnContextDecl. */
    public static final String LOCAL = "AuthnContextDecl";

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AuthnContextDecl</code> with the specified value.
     **
     ** @param  value            the value of the <code>AuthnContextDecl</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Decl(final String value) {
      // ensure inheritance
      super(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Decl
  // ~~~~~~~~~ ~~~~
  /**
   ** SAML 2.0 Core AuthnContextDeclRef.
   */
  public static final class DeclRef extends Any {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Local Name of AuthnContextDeclRef. */
    public static final String LOCAL = "AuthnContextDeclRef";

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AuthnContextDeclRef</code> with the specified value.
     **
     ** @param  value            the value of the
     **                          <code>AuthnContextDeclRef</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private DeclRef(final String value) {
      // ensure inheritance
      super(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link AuthnContext}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** URI of the Context Class. */
    private ClassRef classRef;

    /** Declaration of the Authentication Context. */
    private Decl     decl;

    /** URI of the Declaration of the Authentication Context. */
    private DeclRef declRef;

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
    // Method: classRef
    /**
     ** Sets the URI identifying the Context Class of the Authentication
     ** Context.
     **
     ** @param  value            the URI of the Authentication Context's Class.
     **                          <br>
     **                          Possible object is {@link ClassRef}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>issuer</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder classRef(final ClassRef value) {
      this.classRef = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decl
    /**
     ** Sets the Declaration of the Authentication Context.
     **
     ** @param  value            the Declaration of the Authentication Context.
     **                          <br>
     **                          Possible object is {@link Decl}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>issuer</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder decl(final Decl value) {
      this.decl = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: declRef
    /**
     ** Sets the URI of the Declaration of the Authentication Context.
     **
     ** @param value             the URI of the Declaration of the
     **                          Authentication Context.
     **                          <br>
     **                          Possible object is {@link DeclRef}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>issuer</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder declRef(final DeclRef value) {
      this.declRef = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link AuthnContext}.
     **
     ** @return                  the SAML 2.0 {@link AuthnContext}.
     **                          <br>
     **                          Possible object is {@link AuthnContext}.
     */
    public AuthnContext build() {
      return new AuthnContext(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthnContext</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private AuthnContext(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.decl     = builder.decl;
    this.declRef  = builder.declRef;
    this.classRef = builder.classRef;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decl
  /**
   ** Returns the Declaration of this Authentication Context.
   **
   ** @return                    the declaration of this Authentication Context.
   **                            <br>
   **                            Possible object is {@link Decl}.
   */
  public final Decl decl() {
    return this.decl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   declRef
  /**
   ** Returns the URI of the Declaration of this Authentication Context.
   **
   ** @return                    the URI of the Declaration of this
   **                            Authentication Context.
   **                            <br>
   **                            Possible object is {@link DeclRef}.
   */
  public DeclRef declRef() {
    return this.declRef;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>AuthnContext</code> {@link Builder}.
   **
   ** @return                    a new <code>AuthnContext</code>
   **                            {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthnContext.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classRef
  /**
   ** Factory method to creates an {@link ClassRef}.
   **
   ** @param  value              the value of the
   **                            <code>AuthnContextClassRef</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new {@link ClassRef}.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthnContext.ClassRef</code>.
   */
  public static ClassRef classRef(final String value) {
    return new ClassRef(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decl
  /**
   ** Factory method to creates an {@link Decl}.
   **
   ** @param  value              the value of the
   **                            <code>AuthnContext.Decl</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new {@link Decl}.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthnContext.Decl</code>.
   */
  public static Decl decl(final String value) {
    return new Decl(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   declRef
  /**
   ** Factory method to creates an {@link DeclRef}.
   **
   ** @param  value              the value of the
   **                            <code>AuthnContext.DeclRef</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new {@link DeclRef}.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthnContext.DeclRef</code>.
   */
  public static DeclRef declRef(final String value) {
    return new DeclRef(value);
  }
}