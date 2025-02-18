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

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   MultiValued.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MultiValued.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.domain.type;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class MultiValued
// ~~~~~ ~~~~~~~~~~~
/**
 ** This class is used to represent SCIM multi-valued objects.
 ** <br>
 ** To create a SCIM multi-valued attribute, just define a java.util.List of
 ** this type (or a class that extends this one).
 **
 ** @param  <T>                  the type of the value of this object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MultiValued<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A boolean that indicates if this is the primary value for this
   ** multi-valued object.
   ** <p>
   ** Only <b>one</b> of the attribute values in a list should have this boolean
   ** set to <code>true</code>.
   */
  @JsonProperty("primary")
  @Attribute(description="Boolean to indicate if this is the primary value.")
  private Boolean primary;

  /**
   ** A string describing the type of this attribute's value.
   ** This should probably be one of the SCIM datatypes.
   */
  @JsonProperty("type")
  @Attribute(description="The type for this multi-valued attribute's value.")
  private String type;

  /**
   ** The actual value of this occurrence of a multi-valued attribute.
   */
  @JsonProperty("value")
  @Attribute(description="The actual value of the attribute attribute.")
  private T value;

  /**
   ** The display name for this value.
   ** <br>
   ** For example:
   ** <br>
   ** "<code>Work phone number<code>".
   */
  @Attribute(description="The display name of the attribute attribute.", mutability=Definition.Mutability.IMMUTABLE)
  private String display;

  /**
   ** The URI of the value if it is a reference.
   ** <br>
   ** If the value is not a reference, this should not be set.
   */
  @JsonProperty("$ref")
  @Attribute(description="The URI of the value if it is a reference.")
  private URI ref;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MultiValued</code> SCIM Attribute that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MultiValued() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
   **
   ** @param  value              the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the {@link MultiValued} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link MultiValued}.
   */
  public MultiValued primary(final Boolean value) {
    this.primary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
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
   ** Sets the label indicating the attribute's function.
   **
   ** @param  value              the label indicating the attribute's function.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link MultiValued} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link MultiValued}.
   */
  public MultiValued type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the label indicating the attribute's function.
   **
   ** @return                    the label indicating the attribute's function.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the value of this occurrence of the multi-valued attribute.
   **
   ** @param  value              the value of this occurrence of the
   **                            multi-valued attribute.
   **                            <br>
   **                            <br>
   **                            Allowed object is &lt;T&gt;.
   **
   ** @return                    the {@link MultiValued} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link MultiValued}.
   */
  public MultiValued value(final T value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of this occurrence of the multi-valued attribute.
   **
   ** @return                    the value of this occurrence of the
   **                            multi-valued attribute.
   **                            <br>
   **                            Possible object is &lt;T&gt;.
   */
  public T value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Sets the display name, primarily used for display purposes.
   **
   ** @param  value              the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link MultiValued} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link MultiValued}.
   */
  public MultiValued display(final String value) {
    this.display = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Returns the display name, primarily used for display purposes.
   **
   ** @return                    the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String display() {
    return this.display;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ref
  /**
   ** Sets the {@link URI} for this value of the multi-valued attribute if the
   ** value is a reference.
   **
   ** @param  value              the {@link URI} for this value of the
   **                            multi-valued attribute if the value is a
   **                            reference.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the {@link MultiValued} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link MultiValued}.
   */
  public MultiValued ref(final URI value) {
    this.ref = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ref
  /**
   ** Returns the {@link URI} for this value of the multi-valued attribute if
   ** the value is a reference.
   **
   ** @return                    the {@link URI} for this value of the
   **                            multi-valued attribute if the value is a
   **                            reference.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public URI ref() {
    return this.ref;
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
    int result = this.value != null ? this.value.hashCode() : 0;
    result = 31 * result + (this.primary != null ? this.primary.hashCode() : 0);
    result = 31 * result + (this.type    != null ? this.type.hashCode()    : 0);
    result = 31 * result + (this.display != null ? this.display.hashCode() : 0);
    result = 31 * result + (this.ref     != null ? this.ref.hashCode()     : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Group</code>s filter are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Group</code>s filter may be different even though they
   ** contain the same set of names with the same values, but in a different
   ** order.
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

    final MultiValued that = (MultiValued)other;
    if (this.value != null ? !this.value.equals(that.value) : that.value != null)
      return false;

    if (this.ref != null ? !this.ref.equals(that.ref) : that.ref != null)
      return false;

    if (this.display != null ? !this.display.equals(that.display) : that.display != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    return !(this.primary != null ? !this.primary.equals(that.primary) : that.primary != null);
  }
}