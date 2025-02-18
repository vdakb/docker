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

    File        :   XSDBoolean.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    XSDBoolean.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.xml.type;

////////////////////////////////////////////////////////////////////////////////
// final class XSDBoolean
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** An object that represents a <code>boolean</code> schema type.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class XSDBoolean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The cached Boolean representation of the element's boolean value. */
  private final Boolean value;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link XSDBoolean}.
   */
  public static final class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The cached Boolean representation of the element's string value. */
    private Boolean value;

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
     ** Convenience method to set the <code>boolean</code> value for the DOM
     ** Element from a {@link String}.
     **
     ** @param  value            the <code>boolean</code> value for the DOM Element.
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
      if (value != null) {
        if (value.equals("1")) {
          return value(Boolean.TRUE);
        }
        else if (value.equals("0")) {
          return value(Boolean.FALSE);
        }
        else if (value.equals("true")) {
          return value(Boolean.TRUE);
        }
      }
      return value(Boolean.valueOf(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the <code>boolean</code> value for the DOM Element.
     **
     ** @param  value            the boolean value for the DOM Element.
     **                          <br>
     **                          Possible object is {@link Integer}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>value</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public final Builder value(final Boolean value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a XML <code>boolean</code> value.
     **
     ** @return                  the XML <code>boolean</code> value.
     **                          <br>
     **                          Possible object is {@link XSDBoolean}.
     */
    public final XSDBoolean build() {
      return new XSDBoolean(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XSDBoolean</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private XSDBoolean(final Builder builder) {
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
   ** Convenience method to get the value of the element as an {@link Boolean}
   ** type.
   **
   ** @return                    the value for the XML element.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>XSDBoolean</code> {@link Builder}.
   **
   ** @return                    a new <code>XSDBoolean</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>XSDBoolean.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}