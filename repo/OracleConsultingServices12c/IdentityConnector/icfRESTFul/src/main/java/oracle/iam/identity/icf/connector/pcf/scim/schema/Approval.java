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

    File        :   Approval.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Approval.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.scim.schema;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class Approval
// ~~~~~ ~~~~~~~~
/**
 ** Stores approvals made by a user.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Approval {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX   = "approval";

  /** The canonical value of a approved request */
  public static final String APPROVED = "APPROVED";

  /** The canonical value of a denied request */
  public static final String DENIED   = "DENIED";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("userId")
  @Attribute(description="The user id on the approval. Will be the same as the id field.", mutability=Definition.Mutability.READ_ONLY)
  private String   userId;

  @JsonProperty("clientId")
  @Attribute(description="The client id on the approval. Represents the application this approval or denial was for.", mutability=Definition.Mutability.READ_ONLY)
  private String   clientId;

  @JsonProperty("scope")
  @Attribute(description="The scope on the approval. Will be a group display value.", mutability=Definition.Mutability.READ_ONLY)
  private String   scope;

  @JsonProperty("status")
  @Attribute(description="The status of the approval. Status may be either APPROVED or DENIED", canonical={APPROVED, DENIED}, mutability=Definition.Mutability.READ_ONLY)
  private String   status;

  @JsonProperty("lastUpdatedAt")
  @Attribute(description="Date this approval was last updated.", mutability=Definition.Mutability.READ_ONLY)
  private Calendar updated;

  @JsonProperty("expiresAt")
  @Attribute(description="Date this approval will expire.", mutability=Definition.Mutability.READ_ONLY)
  private Calendar expires;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Approval</code> SCIM Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Approval() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userId
  /**
   ** Sets the userId indicating the approver.
   **
   ** @param  value              the userId indicating the approver.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Approval} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Approval}.
   */
  public final Approval userId(final String value) {
    this.userId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userId
  /**
   ** Returns the userId indicating the approver.
   **
   ** @return                    the userId indicating the approver.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userId() {
    return this.userId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientId
  /**
   ** Sets client id on the approval.
   ** <br>
   ** Represents the application this approval or denial was for.
   **
   ** @param  value              client id on the approval.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Approval} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Approval}.
   */
  public final Approval clientId(final String value) {
    this.clientId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientId
  /**
   ** Returns client id on the approval.
   ** <br>
   ** Represents the application this approval or denial was for.
   **
   ** @return                    client id on the approval.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String clientId() {
    return this.clientId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Sets status of the approval.
   ** <br>
   ** Status may be either {@link #APPROVED} or {@link #DENIED}.
   **
   ** @param  value              status of the approval.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Approval} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Approval}.
   */
  public final Approval status(final String value) {
    this.status = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns status of the approval.
   ** <br>
   ** Status may be either {@link #APPROVED} or {@link #DENIED}.
   **
   ** @return                    status of the approval.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scope
  /**
   ** Sets scope of the approval.
   ** <br>
   ** Will be a group display value
   **
   ** @param  value              scope of the approval.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Approval} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Approval}.
   */
  public final Approval scope(final String value) {
    this.scope = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scope
  /**
   ** Returns scope of the approval.
   ** <br>
   ** Will be a group display value
   **
   ** @return                    scope of the approval.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String scope() {
    return this.scope;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updated
  /**
   ** Sets the Date this approval was last updated.
   **
   ** @param  value              the Date this approval was last updated.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the {@link Approval} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Approval}.
   */
  public final Approval updated(final Calendar value) {
    this.updated = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updated
  /**
   ** Returns the Date this approval was last updated.
   **
   ** @return                    the Date this approval was last updated.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  public final Calendar updated() {
    return this.updated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expires
  /**
   ** Sets the Date this approval will expire.
   **
   ** @param  value              the Date this approval will expire.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the {@link Approval} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Approval}.
   */
  public final Approval expires(final Calendar value) {
    this.expires = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expires
  /**
   ** Returns the Date this approval will expire.
   **
   ** @return                    the Date this approval will expire.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  public final Calendar expires() {
    return this.expires;
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
    int result = this.userId != null ? this.userId.hashCode() : 0;
    result = 31 * result + (this.clientId != null ? this.clientId.hashCode() : 0);
    result = 31 * result + (this.scope    != null ? this.scope.hashCode()    : 0);
    result = 31 * result + (this.status   != null ? this.status.hashCode()   : 0);
    result = 31 * result + (this.status   != null ? this.status.hashCode()   : 0);
    result = 31 * result + (this.updated  != null ? this.updated.hashCode()  : 0);
    result = 31 * result + (this.expires  != null ? this.expires.hashCode()  : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Approval</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Approval</code>s may be different even though they contain the same
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

    final Approval that = (Approval)other;
    if (this.userId != null ? !this.userId.equals(that.userId) : that.userId != null)
      return false;

    if (this.clientId != null ? !this.clientId.equals(that.clientId) : that.clientId != null)
      return false;

    if (this.scope != null ? !this.scope.equals(that.scope) : that.scope != null)
      return false;

    if (this.status != null ? !this.status.equals(that.status) : that.status != null)
      return false;

    if (this.updated != null ? !this.updated.equals(that.updated) : that.updated != null)
      return false;

    return !(this.expires != null ? !this.expires.equals(that.expires) : that.expires != null);
  }
}