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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   Member.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Member.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.schema;

import java.util.Objects;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.platform.scim.annotation.Attribute;

import static oracle.iam.platform.scim.annotation.Attribute.Returned;
import static oracle.iam.platform.scim.annotation.Attribute.Mutability;
import static oracle.iam.platform.scim.annotation.Attribute.Uniqueness;

////////////////////////////////////////////////////////////////////////////////
// final class Member
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** Stores user membership for the Member.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Member {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX = "member";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("value")
  @Attribute(description="The identifier of a Member member.", required=true, caseExact=false, mutability=Mutability.IMMUTABLE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String value;

  @JsonProperty("display")
  @Attribute(description="A human readable name, primarily used for display purposes.", required=false, caseExact=false, mutability=Mutability.IMMUTABLE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String display;

  @JsonProperty("$ref")
  @Attribute(description="The URI of the member resource.", required=true, reference={ "User", "Member" }, mutability=Mutability.IMMUTABLE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private URI    ref;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Member</code> SCIM Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Member() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Member</code> SCIM Resource with the properties
   ** specified.
   **
   ** @param  value              the identifier of the member.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  display            the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Member(final String value, final String display) {
 	  // ensure inheritance
    super();

 	  // initalize instance attributes
    this.value   = value;
    this.display = display;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the identifier of the Group's Member.
   **
   ** @param  value              the identifier of the Group's Member.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Member</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public final Member value(final String value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the identifier of the Group's Member.
   **
   ** @return                    the identifier of the Group's Member.
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
   ** @param  display            the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Member</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public final Member display(final String display) {
    this.display = display;
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
   ** Sets the URI of the SCIM resource corresponding to this group member.
   **
   ** @param  value              the URI of the SCIM resource corresponding to
   **                            this group member.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>Member</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public final Member ref(final URI value) {
    this.ref = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ref
  /**
   ** Returns the URI of the SCIM resource corresponding to this group member.
   **
   ** @return                    the URI of the SCIM resource corresponding to
   **                            this group member.
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
  // Method:   of
  /**
   ** Factory method to create a new <code>Member</code> with the properties
   ** specified.
   **
   ** @param  value              the identifier of the User's group.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  display            the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Member</code> populated with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public static Member of(final String value, final String display) {
    return new Member(value, display);
  }

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
    return Objects.hash(this.display, this.ref);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Member</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Member</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
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

    final Member that = (Member)other;
    return super.equals(that) && Objects.equals(this.value, that.value) && Objects.equals(this.ref, that.ref);
  }
}