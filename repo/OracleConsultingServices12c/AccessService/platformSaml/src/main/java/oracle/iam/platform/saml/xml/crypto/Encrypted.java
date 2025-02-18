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

    File        :   Encrypted.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Encrypted.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.xml.crypto;

////////////////////////////////////////////////////////////////////////////////
// class Encrypted
// ~~~~~ ~~~~~~~~~
/**
 ** An XML object representing XML Encryption, version 20021210, EncryptedType
 ** element.
 ** <br>
 ** This is the base type for {@link Data} and {@link Key} elements.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Encrypted {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Id attribute name. */
  public static final String ID       = "Id";

  /** Type attribute name. */
  public static final String TYPE     = "Type";

  /** MimeType attribute name. */
  public static final String MIMETYPE = "MimeType";

  /** Encoding attribute name. */
  public static final String ENCODING = "Encoding";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** id attribute value. */
  private final String               id;

  /** Type attribute value. */
  private final String               type;

  /** MimeType attribute value. */
  private final String               mimeType;

  /** Encoding attribute value. */
  private final String               encoding;

  /** EncryptionMethod child element. */
  private final EncryptionMethod     encryptionMethod;

  /** CipherData child element. */
  private final CipherData           cipherData;

  /** EncryptionProperties child element. */
//  private final EncryptionProperties encryptionProperties;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Data
  // ~~~~~ ~~~~
  /**
   ** An XML encrypted core element representing XML Encryption, version
   ** 20021210, EncryptedData element.
   */
  public static final class Data extends Encrypted {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Element local name. */
    public static final String LOCAL = "EncryptedData";

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Data</code> with the specified {@link Builder}.
     **
     ** @param  builder        the {@link Builder} providing the values to
     **                        configure.
     **                        <br>
     **                        If some parameters is not set, the default
     **                        values are used.
     **                        <br>
     **                        Allowed object is {@link Builder}.
     */
    private Data(final Builder builder) {
      // ensure inheritance
      super(builder);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Key
  // ~~~~~ ~~~~
  /**
   ** An XML encrypted core element representing XML Encryption, version
   ** 20021210, EncryptedKey element.
   */
  public static final class Key extends Encrypted {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Element local name. */
    public static final String LOCAL     = "EncryptedKey";

    /** Recipient attribute name. */
    public static final String RECIPIENT = "Recipient";

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Recipient value. */
    private String recipient;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Key</code> with the specified {@link Builder}.
     **
     ** @param  builder        the {@link Builder} providing the values to
     **                        configure.
     **                        <br>
     **                        If some parameters is not set, the default
     **                        values are used.
     **                        <br>
     **                        Allowed object is {@link Builder}.
     */
    private Key(final Builder builder) {
      // ensure inheritance
      super(builder);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: recipient
    /**
     ** Returns the hint about for whom this encrypted key is intended.
     **
     ** @return                  the hint about who this encrypted key is
     **                          intended for.
     */
    public final String recipient() {
      return this.recipient;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recipient
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link Encrypted}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** id attribute value. */
    private String               id;

    /** Type attribute value. */
    private String               type;

    /** MimeType attribute value. */
    private String               mimeType;

    /** Encoding attribute value. */
    private String               encoding;

    /** EncryptionMethod child element. */
    private EncryptionMethod     encryptionMethod;

    /** CipherData child element. */
    private CipherData           cipherData;

    /** EncryptionProperties child element. */
  //  private EncryptionProperties encryptionProperties;

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
     ** Sets the unique ID for the XML element.
     **
     ** @param  value            the unique ID for the XML element.
     **                          <br>
     **                          Allowed object is {@link String}.
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
    // Method: type
    /**
     ** Sets the type information for the plaintext content.
     **
     ** @param  value            the type information for the plaintext content.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder type(final String value) {
      this.type = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mimeType
    /**
     ** Sets the MIME type of the plaintext content.
     **
     ** @param  value            the MIME type of the plaintext content.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder mimeType(final String value) {
      this.mimeType = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: encoding
    /**
     ** Sets the encoding applied to the plaintext content prior to encryption.
     **
     ** @param  value            the encoding applied to the plaintext content
     **                          prior to encryption.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder encoding(final String value) {
      this.encoding = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: encryptionMethod
    /**
     ** Sets the {@link EncryptionMethod} child element.
     **
     ** @param  value            the {@link EncryptionMethod} child element.
     **                          <br>
     **                          Allowed object is {@link EncryptionMethod}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder encryptionMethod(final EncryptionMethod value) {
      this.encryptionMethod = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: data
    /**
     ** Factory method to create a XML encrypted {@link Data}.
     **
     ** @return                  the XML encrypted {@link Data}.
     **                          <br>
     **                          Possible object is {@link Data}.
     */
    public Data data() {
      return new Data(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: key
    /**
     ** Factory method to create a XML encrypted {@link Key}.
     **
     ** @return                  the XML encrypted {@link Key}.
     **                          <br>
     **                          Possible object is {@link Key}.
     */
    public Key key() {
      return new Key(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Encrypted</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private Encrypted(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.id               = builder.id;
    this.type             = builder.type;
    this.mimeType         = builder.mimeType;
    this.encoding         = builder.encoding;
    this.cipherData       = builder.cipherData;
    this.encryptionMethod = builder.encryptionMethod;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the unique ID for the XML element.
   **
   ** @return                    the unique ID for the XML element.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type information for the plaintext content.
   **
   ** @return                    the type information for the plaintext content.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mimeType
  /**
   ** Returns the MIME type of the plaintext content.
   **
   ** @return                    the MIME type of the plaintext content.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String mimeType() {
    return this.mimeType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoding
  /**
   ** Returns the encoding applied to the plaintext content prior to encryption.
   **
   ** @return                    the encoding applied to the plaintext content
   **                            prior to encryption.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String encoding() {
    return this.encoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encryptionMethod
  /**
   ** Returns the {@link EncryptionMethod} child element.
   **
   ** @return                    the {@link EncryptionMethod} child element.
   **                            <br>
   **                            Possible object is {@link EncryptionMethod}.
   */
  public final EncryptionMethod encryptionMethod() {
    return this.encryptionMethod;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cipherData
  /**
   ** Returns the {@link CipherData} child element.
   **
   ** @return                    the {@link CipherData} child element.
   **                            <br>
   **                            Possible object is {@link CipherData}.
   */
  public final CipherData keyInfo() {
    return this.cipherData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>Encrypted</code> {@link Builder}.
   **
   ** @return                    a new <code>Encrypted</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>Encrypted.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}