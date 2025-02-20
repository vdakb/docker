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

    File        :   UserExtension.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserExtension.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.v2.schema;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.rest.schema.Discoverable;

import oracle.iam.system.simulation.scim.annotation.Schema;
import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class UserExtension
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This represents a SCIM schema extension used in representing users
 ** that belong to, or act on behalf of a eFBS User.
 ** <br>
 ** The schema for <code>User</code> is identified using the URI's:
 ** <code>urn:com:rola:scim:schemas:extension:efbs:2.0:EFBSUser</code>
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id=UserExtension.ID, name="EFBSUser", description="EFBS User")
public class UserExtension<T extends UserExtension> implements Discoverable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ID = "urn:com:rola:scim:schemas:extension:efbs:2.0:EFBSUser";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("admin")
  @Attribute(required=true, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private Boolean           admin;

  @JsonProperty("validFrom")
  @Attribute(required=true, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private Date              validFrom;

  @JsonProperty("validTo")
  @Attribute(required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private Date              validTo;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty SCIM <code>UserExtension</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserExtension() {
  	// ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   admin
  /**
   ** <code>true</code> if the account has administrator permissions; otherwise
   ** <code>false</code>.
   **
   ** @param  value              <code>true</code> if the account has
   **                            administrator permissions; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the {@link UserExtension} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T admin(final Boolean value) {
    this.admin = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   admin
  /**
   ** Whether the account has administrator permissions.
   **
   ** @return                    <code>true</code> if the account has
   **                            administrator permissions; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean admin() {
    return this.admin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validFrom
  /**
   ** Sets start data of the validity the User.
   **
   ** @param  value              the start data of the validity the User.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the {@link UserExtension} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T validFrom(final Date value) {
    this.validFrom = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validFrom
  /**
   ** Returns start data of the validity the User.
   **
   ** @return                    the start data of the validity the User.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public final Date validFrom() {
    return this.validFrom;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validTo
  /**
   ** Sets end data of the validity the User.
   **
   ** @param  value              the end data of the validity the User.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the {@link UserExtension} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T validTo(final Date value) {
    this.validTo = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validTo
  /**
   ** Returns end data of the validity the User.
   **
   ** @return                    the end data of the validity the User.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public final Date validTo() {
    return this.validTo;
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
    result = 31 * result + (this.validFrom != null ? this.validFrom.hashCode() : 0);
    result = 31 * result + (this.validTo   != null ? this.validTo.hashCode()   : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>User</code>s filter are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>User</code>s filter may be different even though they
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

    // ensure inheritance
    if (!super.equals(other))
      return false;

    final UserExtension that = (UserExtension)other;
    if (this.validFrom != null ? !this.validFrom.equals(that.validFrom) : that.validFrom != null)
      return false;

    return !(this.validTo != null ? !this.validTo.equals(that.validTo) : that.validTo != null);
  }
}
