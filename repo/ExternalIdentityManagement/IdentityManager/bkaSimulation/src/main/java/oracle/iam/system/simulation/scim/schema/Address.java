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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   Address.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Address.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class Address
// ~~~~~ ~~~~~~~
/**
 ** Stores address for the user.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Address {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX = "adresses";

  /** The canonical value of a an Address belonging to work purpose */
  public static final String WORK   = "work";

  /** The canonical value of a an Address belonging to home purpose */
  public static final String HOME   = "home";

  /** The canonical value of a an Address belonging to other purpose */
  public static final String OTHER  = "other";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("primary")
  @Attribute(description="A Boolean value indicating the 'primary' or preferred attribute value for this attribute, e.g., the preferred address. The primary attribute value 'true' MUST appear no more than once.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT)
  private Boolean primary;

  @JsonProperty("type")
  @Attribute(description="A label indicating the attribute's function; e.g., 'work' or 'home'.", required=false, caseExact=false, canonical={WORK, HOME, OTHER}, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  type;

  @JsonProperty("formatted")
  @Attribute(description="The full mailing address, formatted for display or use with a mailing label. This attribute MAY contain newlines.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  formatted;

  @JsonProperty("streetAddress")
  @Attribute(description="The full street address component, which may include house number, street name, PO BOX, and multi-line extended street address information. This attribute MAY contain newlines.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  streetAddress;

  @JsonProperty("locality")
  @Attribute(description="The city or locality component.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  locality;

  @JsonProperty("region")
  @Attribute(description="The state or region component.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  region;

  @JsonProperty("postalCode")
  @Attribute(description="The zipcode or postal code component.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  postalCode;

  @JsonProperty("country")
  @Attribute(description="The country name component.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  country;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Address</code> SCIM Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Address() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute, for
   ** example, the preferred address.
   ** <p>
   ** The primary attribute value 'true' MUST appear no more than once.
   **
   ** @param  value              the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Address</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Address</code>.
   */
  public Address primary(final Boolean value) {
    this.primary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute, for
   ** example, the preferred address.
   ** <p>
   ** The primary attribute value 'true' MUST appear no more than once.
   **
   ** @return                    the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean primary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the label indicating the attribute's function; for example, 'work' or
   ** 'home'.
   **
   ** @param  value              the label indicating the attribute's function;
   **                            for example, 'work' or 'home'.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Address</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Address</code>.
   */
  public Address type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the label indicating the attribute's function; for example, 'work'
   ** or 'home'.
   **
   ** @return                    the label indicating the attribute's function;
   **                            for example, 'work' or 'home'.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Sets the full address, formatted for display or use with a address label.
   ** <p>
   ** This attribute MAY contain newlines.
   **
   ** @param  value              the address, formatted for display or use with
   **                            a address label.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Address</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Address</code>.
   */
  public Address formatted(final String value) {
    this.formatted = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Returns the full address, formatted for display or use with a address
   ** label.
   ** <p>
   ** This attribute MAY contain newlines.
   **
   ** @return                    the address, formatted for display or use with
   **                            a address label.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String formatted() {
    return this.formatted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streetAddress
  /**
   ** Sets the full street address component, which may include house number,
   ** street name, PO BOX, and multi-line extended street address information.
   ** <p>
   ** This attribute MAY contain newlines.
   **
   ** @param  value              the full street address component, which may
   **                            include house number, street name, PO BOX, and
   **                            multi-line extended street address information.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Address</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Address</code>.
   */
  public Address streetAddress(final String value) {
    this.streetAddress = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streetAddress
  /**
   ** Returns the full street address component, which may include house number,
   ** street name, PO BOX, and multi-line extended street address information.
   ** <p>
   ** This attribute MAY contain newlines.
   **
   ** @return                    the full street address component, which may
   **                            include house number, street name, PO BOX, and
   **                            multi-line extended street address information.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String streetAddress() {
    return this.streetAddress;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   country
  /**
   ** Sets the country name component.
   **
   ** @param  value              the country name component.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Address</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Address</code>.
   */
  public Address country(final String value) {
    this.country = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   country
  /**
   ** Returns the country name component.
   **
   ** @return                    the country name component.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String country() {
    return this.country;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locality
  /**
   ** Sets the city or locality component.
   **
   ** @param  value              the city or locality component.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Address</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Address</code>.
   */
  public Address locality(final String value) {
    this.locality = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locality
  /**
   ** Returns the city or locality component.
   **
   ** @return                    the city or locality component.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String locality() {
    return this.locality;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   region
  /**
   ** Sets the state or region component.
   **
   ** @param  value              the state or region component.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Address</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Address</code>.
   */
  public Address region(final String value) {
    this.region = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   region
  /**
   ** Returns the state or region component.
   **
   ** @return                    the state or region component.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String region() {
    return this.region;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postalCode
  /**
   ** Sets the zipcode or postal code component.
   **
   ** @param  value              the zipcode or postal code component.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Address</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Address</code>.
   */
  public Address postalCode(final String value) {
    this.postalCode = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postalCode
  /**
   ** Returns the zipcode or postal code component.
   **
   ** @return                    the zipcode or postal code component.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String postalCode() {
    return this.postalCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int result = this.formatted != null ? this.formatted.hashCode() : 0;
    result = 31 * result + (this.streetAddress != null ? this.streetAddress.hashCode() : 0);
    result = 31 * result + (this.region        != null ? this.region.hashCode()        : 0);
    result = 31 * result + (this.postalCode    != null ? this.postalCode.hashCode()    : 0);
    result = 31 * result + (this.country       != null ? this.country.hashCode()       : 0);
    result = 31 * result + (this.type          != null ? this.type.hashCode()          : 0);
    result = 31 * result + (this.primary       != null ? this.primary.hashCode()       : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Address</code>es are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Address</code>es may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Address that = (Address)other;
    if (this.formatted != null ? !this.formatted.equals(that.formatted) : that.formatted != null)
      return false;

    if (this.streetAddress != null ? !this.streetAddress.equals(that.streetAddress) : that.streetAddress != null)
      return false;

    if (this.locality != null ? !this.locality.equals(that.locality) : that.locality != null)
      return false;

    if (this.region != null ? !this.region.equals(that.region) : that.region != null)
      return false;

    if (this.postalCode != null ? !this.postalCode.equals(that.postalCode) : that.postalCode != null)
      return false;

    if (this.country != null ? !this.country.equals(that.country) : that.country != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    return !(this.primary != null ? !this.primary.equals(that.primary) : that.primary != null);
  }
}