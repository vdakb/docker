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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   GroupResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GroupResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.scim.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Schema;
import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.schema.Entity;

////////////////////////////////////////////////////////////////////////////////
// class GroupResource
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** SCIM provides a resource type for <code>GroupResource</code> resources.
 ** <br>
 ** The core schema for <code>Group</code> is identified using the URI:
 ** <code>urn:scim:schemas:core:1.0</code>
 ** <b>Note</b>:
 ** <br>
 ** Pivotal doesn't follow the recommendation of the RFC to extend the schema
 ** levaraging the extension mechanism. Instead to agree in the standards
 ** Pivotal creates its own user resource uder the hood of
 ** <code>urn:scim:schemas:core:1.0</code>.
 ** <br>
 ** Due to the violation and disagreement to standads by pivotal we are not able
 ** to use the standard classes provided by the SCIM implementation of the
 ** connector and needs to duplicate most of the code lines of the classes
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id="urn:scim:schemas:core:1.0", name="Group", description="Pivotal Cloud Foundry Group Resource")
public class GroupResource extends Entity<GroupResource> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("displayName")
  @Attribute(description="A human readable name, primarily used for display purposes.", mutability=Definition.Mutability.READ_ONLY)
  private String       displayName;

  @JsonProperty("description")
  @Attribute(description="A human-readable description of the group, displayed e.g. when approving scopes")
  private String       description;

  @JsonProperty("zoneId")
  @Attribute(description="The Identity Zone this group belongs to. The value uaa refers to the default zone.", required=true)
  private String       zoneId;

  @JsonProperty("members")
  @Attribute(description="A list of members of the Group.", multiValueClass=Member.class)
  private List<Member> members;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty SCIM <code>GroupResource</code> resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GroupResource() {
  	// ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Sets the name of the Group, suitable for display to end-users.
   **
   ** @param  value              the name of the Group, suitable for display to
   **                            end-users.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>GroupResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>GroupResource</code>.
   */
  public GroupResource display(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Returns the name of the Group, suitable for display to end-users.
   **
   ** @return                    the name of the Group, suitable for display to
   **                            end-users.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String display() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zoneId
  /**
   ** Sets the identifier for the identity zone to which the group belongs.
   **
   ** @param  value              the identifier for the identity zone to which
   **                            the group belongs.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>GroupResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>GroupResource</code>.
   */
  public GroupResource zoneId(final String value) {
    this.zoneId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zoneId
  /**
   ** Returns the identifier for the identity zone to which the group belongs.
   **
   ** @return                    the identifier for the identity zone to which
   **                            the group belongs.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String zoneId() {
    return this.zoneId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets human-readable description of the group.
   **
   ** @param  value              the human-readable description of the group.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>GroupResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>GroupResource</code>.
   */
  public GroupResource description(final String value) {
    this.description = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns human-readable description of the group.
   **
   ** @return                    human-readable description of the group.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   members
  /**
   ** Sets the name of the Group, suitable for display to end-users.
   **
   ** @param  value              the list of group members.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Member}.
   **
   ** @return                    the <code>GroupResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>GroupResource</code>.
   */
  public GroupResource members(final List<Member> value) {
    this.members = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   members
  /**
   ** Returns the list of group members.
   **
   ** @return                    the list of group members.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Member}.
   */
  public List<Member> members() {
    return this.members;
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
    int result = super.hashCode();
    result = 31 * result + (this.displayName != null ? this.displayName.hashCode() : 0);
    result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
    result = 31 * result + (this.zoneId      != null ? this.zoneId.hashCode()      : 0);
    result = 31 * result + (this.members     != null ? this.members.hashCode()     : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>GroupResource</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>GroupResource</code>s may be different even though they contain the
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

    // ensure inheritance
    if (!super.equals(other))
      return false;

    final GroupResource that = (GroupResource)other;
    if (this.displayName != null ? !this.displayName.equals(that.displayName) : that.displayName!= null)
      return false;

    if (this.description != null ? !this.description.equals(that.description) : that.description != null)
      return false;

    return !(this.zoneId != null ? !this.zoneId.equals(that.zoneId) : that.zoneId != null);
  }
}