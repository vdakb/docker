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

    File        :   CipherData.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CipherData.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.xml.crypto;

import oracle.iam.platform.saml.xml.type.XSDBase64Binary;

////////////////////////////////////////////////////////////////////////////////
// final class CipherData
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** An XML object representing XML Encryption, version 20021210, CipherData
 ** element.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class CipherData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Element local name. */
  public static final String     LOCAL = "CipherData";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** CipherValue child element. */
  private final XSDBase64Binary value;
    
  /** CipherReference child element. */
  private final CipherReference reference;

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link CipherData}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** CipherValue child element. */
    private XSDBase64Binary value;
    
    /** CipherReference child element. */
    private CipherReference reference;

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
     ** Sets the base64-encoded data representing the the encrypted form of the
     ** plaintext data.
     **
     ** @param  value            the new base64-encoded encrypted data
     **                          <br>
     **                          Allowed object is {@link XSDBase64Binary}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder value(final XSDBase64Binary value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reference
    /**
     ** Sets the {@link CipherReference} which points to the location encrypted
     ** data.
     **
     ** @param  value            the {@link CipherReference} which points to the
     **                          location encrypted data.
     **                          <br>
     **                          Allowed object is {@link CipherReference}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder reference(final CipherReference value) {
      this.reference = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a XML {@link CipherData}.
     **
     ** @return                  the XML {@link CipherData}.
     **                          <br>
     **                          Possible object is {@link CipherData}.
     */
    public CipherData build() {
      return new CipherData(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CipherValue</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private CipherData(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.value     = builder.value;
    this.reference = builder.reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the base64-encoded data representing the encrypted form of the
   ** plaintext data.
   **
   ** @return                    the base64-encoded data representing the
   **                            encrypted form of the plaintext data.
   **                            <br>
   **                            Possible object is {@link XSDBase64Binary}.
   */
  public final XSDBase64Binary value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** Returns the {@link CipherReference} which points to the location encrypted
   ** data.
   **
   ** @return                    the {@link CipherReference} which points to the
   **                            location encrypted data.
   **                            <br>
   **                            Possible object is {@link CipherReference}.
   */
  public final CipherReference reference() {
    return this.reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>CipherData</code> {@link Builder}.
   **
   ** @return                    a new <code>CipherData</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>CipherData.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}