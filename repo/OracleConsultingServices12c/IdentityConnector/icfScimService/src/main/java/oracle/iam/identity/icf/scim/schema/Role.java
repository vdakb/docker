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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Library

    File        :   Role.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Role.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Role
// ~~~~~ ~~~~
/**
 ** Stores role membership for the user that collectively represent who the User
 ** is; e.g., 'Student', "Faculty".
 ** <p>
 ** No vocabulary or syntax is specified though it is expected that a role value
 ** is a String or label representing a collection of entitlements.
 ** <p>
 ** This value has NO canonical types.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Role {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  @JsonIgnore(true)
  public static final String PREFIX = "role";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("value")
  @Attribute(description="The value of a role.")
  private String value;

  @JsonProperty("display")
  @Attribute(description="A human readable name, primarily used for display purposes.")
  private String display;

  @JsonProperty("type")
  @Attribute(description="A label indicating the attribute's function.")
  private String type;

  @JsonProperty("primary")
  @Attribute(description = "A Boolean value indicating the 'primary' or preferred attribute value for this attribute. The primary attribute value 'true' MUST appear no more than once.")
  private Boolean primary;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Role</code> SCIM Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Role() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the identifier of the User's group.
   **
   ** @param  value              the identifier of the User's group.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role value(final String value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the identifier of the User's group.
   **
   ** @return                    the identifier of the User's group.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String value() {
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
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role display(final String value) {
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
  public final String display() {
    return this.display;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the label indicating the attribute's function.
   **
   ** @param  type               the label indicating the attribute's function.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role type(final String type) {
    this.type = type;
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
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
   ** <p>
   ** The primary attribute value 'true' MUST appear no more than once.
   **
   ** @param  value              the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public Role primary(final Boolean value) {
    this.primary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
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
   ** Two <code>Role</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Role</code>s may be
   ** different even though they contain the same set of names with the same
   ** values, but in a different order.
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

    final Role that = (Role)other;
    if (this.value != null ? !this.value.equals(that.value) : that.value != null)
      return false;

    if (this.display != null ? !this.display.equals(that.display) : that.display != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    return !(this.primary != null ? !this.primary.equals(that.primary) : that.primary != null);
  }
}