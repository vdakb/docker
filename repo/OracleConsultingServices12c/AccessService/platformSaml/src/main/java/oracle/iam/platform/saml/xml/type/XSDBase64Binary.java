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

    File        :   XSDBase64Binary.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    XSDBase64Binary.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.xml.type;

import java.util.Base64;

////////////////////////////////////////////////////////////////////////////////
// class XSDBase64Binary
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An object that represents a <code>base64Binary</code> schema type.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class XSDBase64Binary {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The cached byte representation of the element's base64-encoded
   ** value.
   */
  private final byte[] value;

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link XSDBase64Binary}.
   */
  public static final class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The cached String representation of the element's base64-encoded
     ** value.
     */
    protected byte[] value;

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
     ** Sets the base64-encoded binary value for the DOM Element.
     ** Convenience method to set the base64-encoded binary value for the DOM
     ** Element from a {@link String}.
     **
     ** @param  value            the base64-encoded binary value for the DOM
     **                          Element.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>value</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public final Builder value(final String value) {
      return value(Base64.getDecoder().decode(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the element's content in its binary form.
     **
     ** @param  value            the element's content in its binary form.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder value(final byte[] value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a XML <code>base64Binary</code> value.
     **
     ** @return                  the XML <code>base64Binary</code> value.
     **                          <br>
     **                          Possible object is {@link XSDBase64Binary}.
     */
    public final XSDBase64Binary build() {
      return new XSDBase64Binary(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XSDBase64Binary</code> with the specified
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
  protected XSDBase64Binary(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.value = builder.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the element's content in its binary form.
   **
   ** @return                    the element's content in its binary form.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  public final byte[] value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>XSDBase64Binary</code> {@link Builder}.
   **
   ** @return                    a new <code>XSDBase64Binary</code>
   **                            {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>XSDBase64Binary.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}