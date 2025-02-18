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

    File        :   EncryptedID.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EncryptedID.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

import oracle.iam.platform.saml.xml.crypto.Encrypted;

////////////////////////////////////////////////////////////////////////////////
// final class EncryptedID
// ~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** SAML 2.0 Core EncryptedID.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class EncryptedID {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** EncryptedID  local element name. */
  public static final String LOCAL = "EncryptedID";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** EncryptedKey child element. */
  private final Encrypted.Key  key;

  /** EncryptedData child element. */
  private final Encrypted.Data data;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link EncryptedID}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** EncryptedKey child element. */
    private Encrypted.Key key;

    /** EncryptedData child element. */
    private Encrypted.Data data;

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
    // Method: key
    /**
     ** Sets the EncryptedKey child element.
     **
     ** @param  value            the EncryptedKey child element.
     **                          <br>
     **                          Allowed object is {@link Encrypted.Key}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>value</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder key(final Encrypted.Key value) {
      this.key = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: data
    /**
     ** Sets the EncryptedData child element.
     **
     ** @param  value            the EncryptedData child element.
     **                          <br>
     **                          Allowed object is {@link Encrypted.Data}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>value</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder data(final Encrypted.Data value) {
      this.data = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link EncryptedID}.
     **
     ** @return                  the SAML 2.0 {@link EncryptedID}.
     **                          <br>
     **                          Possible object is {@link EncryptedID}.
     */
    public EncryptedID build() {
      return new EncryptedID(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EncryptedID</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private EncryptedID(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.key  = builder.key;
    this.data = builder.data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the EncryptedKey child element.
   **
   ** @return                    the EncryptedKey child element.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final Encrypted.Key key() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   data
  /**
   ** Returns the EncryptedData child element.
   **
   ** @return                    the EncryptedData child element.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final Encrypted.Data data() {
    return this.data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>EncryptedID</code> {@link Builder}.
   **
   ** @return                    a new <code>EncryptedID</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>EncryptedID.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}