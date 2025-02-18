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

    File        :   Group.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Group.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.schema;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class Group
// ~~~~~ ~~~~~
/**
 ** Stores group membership a user belongs to, either through direct
 ** membership, nested groups, or dynamically calculated.
 ** <br>
 ** The values are meant to enable expression of common group or role based
 ** access control models, although no explicit authorization model is defined.
 ** It is intended that the semantics of group membership and any behavior or
 ** authorization granted as a result of membership are defined by the Service
 ** Provider.
 ** <br>
 ** The Canonical types "direct" and "indirect" are defined to describe how the
 ** group membership was derived. Direct group membership indicates the User is
 ** directly associated with the group and <b>should</b> indicate that Consumers
 ** may modify membership through the Group Resource. Indirect membership
 ** indicates User membership is transitive or dynamic and implies that
 ** Consumers cannot modify indirect group membership through the Group resource
 ** but <b>may</b> modify direct group membership through the Group resource
 ** which <b>may</b> influence indirect memberships. If the SCIM Service
 ** Provider exposes a Group resource, the value <b>must</b> be the "id"
 ** attribute of the corresponding Group resources to which the user belongs.
 ** <br>
 ** Since this attribute is read-only, group membership changes <b>must</b> be
 ** applied via the Group Resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Group {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  @JsonIgnore(true)
  public static final String PREFIX   = "group";

  /** The name to identitify this resource. */
  @JsonIgnore(true)
  public static final String NAME     = "Group";

  /** The name of the unique identifier property. */
  @JsonIgnore(true)
  public static final String UNIQUE   = "displayName";

  /** The canonical value of a membership to indicate direct assignment */
  @JsonIgnore(true)
  public static final String DIRECT   = "direct";

  /** The canonical value of a membership to indicate indirect assignment */
  @JsonIgnore(true)
  public static final String INDIRECT = "indirect";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("type")
  @Attribute(description="A label indicating the attribute's function; e.g., 'direct' or 'indirect'.", canonical={DIRECT, INDIRECT}, mutability=Definition.Mutability.READ_ONLY)
  private String type;

  @JsonProperty("value")
  @Attribute(description="The identifier of the User's group.", mutability=Definition.Mutability.READ_ONLY)
  private String value;

  @JsonProperty("display")
  @Attribute(description="A human readable name, primarily used for display purposes.", mutability=Definition.Mutability.READ_ONLY)
  private String display;

  @JsonProperty("$ref")
  @Attribute(description="The URI of the corresponding Group resource to which the user belongs", reference={ "User", "Group" }, mutability=Definition.Mutability.READ_ONLY)
  private URI    ref;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Group</code> SCIM Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Group() {
	// ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type indicating the attribute's function.
   **
   ** @param  value              the type indicating the groups's assigment.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type indicating the attribute's function.
   **
   ** @return                    the type indicating the groups's assigment.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the identifier of the User's group.
   **
   ** @param  value              the identifier of the User's group.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group value(final String value) {
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
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group display(final String value) {
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
  // Method:   ref
  /**
   ** Sets the URI of the corresponding Group resource to which the user
   ** belongs.
   **
   ** @param  value              the URI of the corresponding Group resource to
   **                            which the user belongs.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group ref(final URI value) {
    this.ref = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRef
  /**
   ** Returns the URI of the corresponding Group resource to which the user
   ** belongs.
   **
   ** @return                    the URI of the corresponding Group resource to
   **                            which the user belongs.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI ref() {
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.value != null ? this.value.hashCode() : 0;
    result = 31 * result + (this.ref     != null ? this.ref.hashCode()     : 0);
    result = 31 * result + (this.display != null ? this.display.hashCode() : 0);
    result = 31 * result + (this.type    != null ? this.type.hashCode()    : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Group</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Group</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different
   ** order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Group that = (Group)other;
    if (this.value != null ? !this.value.equals(that.value) : that.value != null)
      return false;

    if (this.ref != null ? !this.ref.equals(that.ref) : that.ref != null)
      return false;

    if (this.display != null ? !this.display.equals(that.display) : that.display != null)
      return false;

    return !(this.type != null ? !this.type.equals(that.type) : that.type != null);
  }
}