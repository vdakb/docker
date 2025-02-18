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

    File        :   Member.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Member.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.scim.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class Member
// ~~~~~ ~~~~~~
/**
 ** Stores user membership for the Member.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Member {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX    = "member";

  /** The canonical value of a membership to indicate direct assignment */
  public static final String USER      = "user";

  /** The canonical value of a membership to indicate indirect assignment */
  public static final String GROUP     = "group";

  /** The canonical value of a membership origin to indicate it's an internal user */
  public static final String INTERNAL  = "uaa";

  /** The canonical value of a membership origin to indicate an user authenticated by an external Identity Provider*/
  public static final String EXTERNAL  = "saml";

  /** The canonical value of a membership origin to indicate an user authenticated by OpenID Connect*/
  public static final String OPENID    = "oidc";

  /** The canonical value of a membership origin to indicate an user authenticated by a directory*/
  public static final String DIRECTORY = "ldap";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("value")
  @Attribute(description="The identifier of a Member member.", required=true, mutability=Definition.Mutability.IMMUTABLE)
  private String value;

  @JsonProperty("type")
  @Attribute(description="A human readable name, primarily used for display purposes.", canonical={USER, GROUP}, mutability=Definition.Mutability.IMMUTABLE)
  private String type;

  @JsonProperty("origin")
  @Attribute(description="The alias of the identity provider that authenticated this user. \"uaa\" is an internal UAA user. This value will NOT change during an update (put request) if the membership already exists under a different origin.", canonical={INTERNAL, EXTERNAL, OPENID, DIRECTORY}, reference={ "User", "Member" }, mutability=Definition.Mutability.IMMUTABLE)
  private String origin;

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
   ** @return                    the {@link Member} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Member}.
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
  // Method:   type
  /**
   ** Sets the type of the membership.
   ** <br>
   ** Either "USER" or "GROUP".
   **
   ** @param  type               the type of the membership.
   **                            <br>
   **                            Either "USER" or "GROUP".
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Member} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Member}.
   */
  public final Member type(final String type) {
    this.type = type;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the membership.
   ** <br>
   ** Either "USER" or "GROUP".
   **
   ** @return                    the type of the membership.
   **                            <br>
   **                            Either "USER" or "GROUP".
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Sets the alias of the identity provider that authenticated this user.
   ** <br>
   ** <code>uaa</code> is an internal UAA user.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This value will NOT change during an update (put request) if the membership
   ** already exists under a different origin.
   **
   ** @param  origin             the alias of the identity provider that
   **                            authenticated this user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Member} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Member}.
   */
  public final Member origin(final String origin) {
    this.origin = origin;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Returns the alias of the identity provider that authenticated this user.
   ** <br>
   ** <code>uaa</code> is an internal UAA user.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This value will NOT change during an update (put request) if the membership
   ** already exists under a different origin.
   **
   ** @return                    the alias of the identity provider that
   **                            authenticated this user.
   **                            <br>
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String origin() {
    return this.origin;
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
    result = 31 * result + (this.type   != null ? this.type.hashCode()   : 0);
    result = 31 * result + (this.origin != null ? this.origin.hashCode() : 0);
    return result;
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

    final Member that = (Member)other;
    if (this.value != null ? !this.value.equals(that.value) : that.value != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    return !(this.origin != null ? !this.origin.equals(that.origin) : that.origin != null);
  }
}