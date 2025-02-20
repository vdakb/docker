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

    File        :   Certificate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Certificate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class Certificate
// ~~~~~ ~~~~~~~~~~~
/**
 ** Stores Certificate for the user.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Certificate {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX = "certificate";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("primary")
  @Attribute(description="A Boolean value indicating the 'primary' or preferred attribute value for this attribute. The primary attribute value 'true' MUST appear no more than once.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT)
  private Boolean primary;

  @JsonProperty("type")
  @Attribute(description="A label indicating the attribute's function.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  type;

  @JsonProperty("value")
  @Attribute(description="The value of a X509 certificate.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private byte[]  value;

  @JsonProperty("display")
  @Attribute(description="A human readable name, primarily used for display purposes.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  display;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Certificate</code> SCIM Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Certificate() {
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
   ** example, the preferred Certificate.
   ** <p>
   ** The primary attribute value 'true' MUST appear no more than once.
   **
   ** @param  value              the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Certificate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Certificate</code>.
   */
  public Certificate primary(final Boolean value) {
    this.primary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute, for
   ** example, the preferred Certificate.
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
   ** @return                    the <code>Certificate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Certificate</code>.
   */
  public Certificate type(final String value) {
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
  // Method:   value
  /**
   ** Sets the value of a X509 certificate.
   **
   ** @param  value              the value of a X509 certificate.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the <code>Certificate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Certificate</code>.
   */
  public Certificate value(final byte[] value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of a X509 certificate for the user.
   **
   ** @return                    the value of a X509 certificate.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  public byte[] value() {
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
   ** @return                    the <code>Certificate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Certificate</code>.
   */
  public Certificate display(final String value) {
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
    int result = this.value != null ? Arrays.hashCode(value) : 0;
    result = 31 * result + (this.display != null ? this.display.hashCode() : 0);
    result = 31 * result + (this.type    != null ? this.type.hashCode()    : 0);
    result = 31 * result + (this.primary != null ? this.primary.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Certificate</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Certificate</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
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

    final Certificate that = (Certificate)other;
    if (!Arrays.equals(value, that.value))
      return false;

    if (this.display != null ? !this.display.equals(that.display) : that.display != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    return !(this.primary != null ? !this.primary.equals(that.primary) : that.primary != null);
  }
}