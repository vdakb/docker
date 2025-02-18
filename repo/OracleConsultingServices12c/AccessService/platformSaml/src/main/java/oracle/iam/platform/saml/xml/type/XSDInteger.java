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

    File        :   XSDInteger.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    XSDInteger.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.xml.type;

////////////////////////////////////////////////////////////////////////////////
// final class XSDInteger
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** An object that represents a <code>int</code> schema type.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class XSDInteger {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The cached Integer representation of the element's int value. */
  private final Integer value;

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link XSDInteger}.
   */
  public static final class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The cached Integer representation of the element's string value. */
    private Integer value;

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
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Convenience method to set the <code>int</code> value for the DOM Element
     ** from a {@link String}.
     **
     ** @param  value            the <code>int</code> value for the DOM Element.
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
      return value(Integer.valueOf(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the int value for the DOM Element.
     **
     ** @param  value            the int value for the DOM Element.
     **                          <br>
     **                          Possible object is {@link Integer}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>value</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public final Builder value(final Integer value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a XML <code>int</code> value.
     **
     ** @return                  the XML <code>int</code> value.
     **                          <br>
     **                          Possible object is {@link XSDInteger}.
     */
    public final XSDInteger build() {
      return new XSDInteger(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XSDInteger</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private XSDInteger(final Builder builder) {
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
   ** Convenience method to get the value of the element as an {@link Integer}
   ** type.
   **
   ** @return                    the value for the XML element.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>XSDInteger</code> {@link Builder}.
   **
   ** @return                    a new <code>XSDInteger</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>XSDInteger.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}