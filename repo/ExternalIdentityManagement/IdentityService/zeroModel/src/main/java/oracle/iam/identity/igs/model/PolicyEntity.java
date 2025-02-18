/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   RoleEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoleEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

////////////////////////////////////////////////////////////////////////////////
// class RoleEntity
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>RoleEntity</code>'s make it easier to assign access levels to
 ** identities and to audit those assignments on an ongoing basis.
 ** <p>
 ** Identity Manager provides a comprehensive set of role-based access control
 ** capabilities. <code>RoleEntity</code>-based access control ensures higher
 ** visibility and ease in assigning and unassigning access privileges to
 ** identities, enforces the notion of least privilege, and enables compliance
 ** and audit insight.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="role"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://www.oracle.com/schema/igs}entity"&gt;
 *+       &lt;sequence&gt;
 **         &lt;element name="identities"    type="{http://www.oracle.com/schema/igs}member"      minOccurs="0" maxOccurs="unbounded"/&gt;
 **         &lt;element name="organizations" type="{http://www.oracle.com/schema/igs}publication" minOccurs="0" maxOccurs="unbounded"/&gt;
 **       &lt;/sequence&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 ** <b>Note</b>:
 ** <br>
 ** The name of the class is chossen in this way to avoid conflicts if the
 ** implementation hits IdentityManager where a class with similar semantic is
 ** offered by the EJB's.
 **
 ** @see     Entity
 ** @see     AbstractEntity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PolicyEntity extends AbstractEntity<PolicyEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1346688114567837109")
  private static final long serialVersionUID = 1978439389645712457L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The owner type of this particular policy, values from OIM.
   */
  private String                                       ownerType;
  
  /**
   ** The owner ID of this particular policy, values from OIM.
   */
  private String                                       ownerId;
  
  /**
   ** The type of this particular policy - "Primary", "Other" etc., values from OIM.
   */
  private String                                       type;
  
  /**
   ** The description of this particular policy.
   */
  private String                                       description;
  
  /**
   ** The initial creation date of this particular policy.
   */
  private Date                                         createDate;
  
  /**
   ** The last update date of this particular policy.
   */
  private Date                                         updateDate;
  
  /**
   ** The priority of this particular policy.
   */
  private long                                         priority;
  
 /**
   ** The list with the <code>AccountEntity</code>'s the policy provisions.
   */
  private transient List<AccountEntity>                account = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PolicyEntity</code> with the specified public identifier
   ** but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the <code>PolicyEntity</code>
   ** belongs must be populated in manually.
   **
   ** @param  id                 public identifier of <code>PolicyEntity</code>
   **                            (usually the value of the
   **                            <code>Access Policy Name</code> for the policy in Identity
   **                            Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  PolicyEntity(final String id) {
    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerType
  /**
   ** Returns the owner type of this <code>Entity</code>.
   **
   ** @return                    the owner type of this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String ownerType() {
    return this.ownerType;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerType
  /**
   ** Sets the Access Policy owner type.
   **
   ** @param  value              owner type of Access Policy.
   **                            <br>
   **                            Allowed object is <code>String</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final PolicyEntity ownerType(final String value) {
    this.ownerType = value != null ? value : "";
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerId
  /**
   ** Returns the owner ID of this <code>Entity</code>.
   **
   ** @return                    the owner ID of this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String ownerId() {
    return this.ownerId;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerId
  /**
   ** Sets the Access Policy owner ID.
   **
   ** @param  value              owner ID of Access Policy.
   **                            <br>
   **                            Allowed object is <code>String</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final PolicyEntity ownerId(final String value) {
    this.ownerId = value != null ? value : "";
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the status of this account.
   **
   ** @return                    the status of this account.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String type() {
    return this.type;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the status to be applied on this account.
   **
   ** @param  value              the status describing this account.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>t</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>t</code>.
   */
  public PolicyEntity type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of this <code>Entity</code>.
   **
   ** @return                    the description of this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.description;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the description of this <code>Entity</code>.
   **
   ** @param  value              description of Access Policy.
   **                            <br>
   **                            Allowed object is <code>String</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final PolicyEntity description(final String value) {
    this.description = value != null ? value : "";
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDate
  /**
   ** Returns the create date date of of this <code>Entity</code>.
   **
   ** @return                    the create date of this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public final Date createDate() {
    return this.createDate;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDate
  /**
   ** Sets create date date of of this <code>Entity</code>.
   **
   ** @param  value              creation date of this Access Policy.
   **                            <br>
   **                            Allowed object is <code>Date</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final PolicyEntity createDate(final Date value) {
    this.createDate = value;
    return this;
  }

  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateDate
  /**
   ** Returns the last update date of this <code>Entity</code>.
   **
   ** @return                    the update date of this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public final Date updateDate() {
    return this.updateDate;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateDate
  /**
   ** Sets last update date of of this <code>Entity</code>.
   **
   ** @param  value              update date of this Access Policy.
   **                            <br>
   **                            Allowed object is <code>Date</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final PolicyEntity updateDate(final Date value) {
    this.updateDate = value;
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   priority
  /**
   ** Returns the priority of this <code>Entity</code>.
   **
   ** @return                    the priority of this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final long priority() {
    return this.priority;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   priority
  /**
   ** Sets the priority of <code>Entity</code>.
   **
   ** @param  value              priority of Access Policy.
   **                            <br>
   **                            Allowed object is <code>String</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final PolicyEntity priority(final long value) {
    this.priority = value;
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   account
  /**
   ** Sets the specified {@link List} of {@link AccountEntity} objects that
   ** should be provisioned by this <code>PolicyEntity</code>.
   **
   ** @param  value              the {@link List} of @link AccountEntity} objects
   **                            that should be provisioned to this
   **                            <code>PolicyEntity</code>.
   **                            <br>
   **                            Allowed object is {@link List} where
   **                            each element is of type
   **                            {@link PolicyEntity}.
   **
   ** @return                    the <code>PolicyEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>PolicyEntity</code>.
   */
  public final PolicyEntity account(final AccountEntity value) {
    return account(Arrays.asList(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   account
  /**
   ** Sets the specified {@link Arrays} of {@link AccountEntity} objects that
   ** should be provisioned by this <code>PolicyEntity</code>.
   **
   ** @param  value              the {@link Arrays} of @link AccountEntity} objects
   **                            that should be provisioned to this
   **                            <code>PolicyEntity</code>.
   **                            <br>
   **                            Allowed object is {@link Arrays} where
   **                            each element is of type
   **                            {@link PolicyEntity}.
   **
   ** @return                    the <code>PolicyEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>PolicyEntity</code>.
   */
  public final PolicyEntity account(final AccountEntity... value) {
    return account(Arrays.asList(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   account
  /**
   ** Sets the specified {@link List} of {@link AccountEntity} objects that
   ** should be provisioned by this <code>PolicyEntity</code>.
   **
   ** @param  values             the {@link List} of @link AccountEntity} objects
   **                            that should be provisioned to this
   **                            <code>PolicyEntity</code>.
   **                            <br>
   **                            Allowed object is {@link List} where
   **                            each element is of type
   **                            {@link PolicyEntity}.
   **
   ** @return                    the <code>PolicyEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>PolicyEntity</code>.
   */
  public final PolicyEntity account(final List<AccountEntity> values) {
    if (this.account == null)
      this.account = new ArrayList<AccountEntity>();
    this.account = values;
    return this;
  }
    
  //////////////////////////////////////////////////////////////////////////////
  // Method:   account
  /**
   ** Returns the {@link List} of {@link AccountEntity} objectsthat are provisioned by this
   ** <code>PolicyEntity</code>.
   **
   ** @return                    the {@link List} of {@link AccountEntity} objecta that
   **                            are provisioned by this <code>PolicyEntity</code>.
   **                            <br>
   **                            Possible result is {@link List} of 
   **                            {@link AccountEntity} objects.
   */
  public final List<AccountEntity> account() {
    return this.account;
  }
}
