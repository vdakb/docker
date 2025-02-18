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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   RoleInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceOperation;

import oracle.iam.identity.deployment.type.MemberShip;

////////////////////////////////////////////////////////////////////////////////
// class RoleInstance
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>RoleInstance</code> represents a <code>Role</code> in Identity Manager
 ** that might be created, deleted or changed after or during an import
 ** operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleInstance extends EntitlementData {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                     category   = null;
  private Long                       categoryID = null;

  private List<MemberShip.Recipient> assignee   = new ArrayList<MemberShip.Recipient>();
  private List<MemberShip.Recipient> revokee    = new ArrayList<MemberShip.Recipient>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleInstance</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleInstance() {
    // ensure inheritance
    super(OIMType.Role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   categoryID
  /**
   ** Called to inject the argument for parameter <code>categoryID</code>.
   **
   ** @param  id                 the identifier of a role category in Oracle
   **                            Identity Manager the role belongs to.
   */
  public void categoryID(final Long id) {
    this.categoryID = id;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns identifier of the role category the role belongs to in Oracle
   ** Identity Manager to handle.
   **
   ** @return                    the identifier of a role category in Oracle
   **                            Identity Manager the role belongs to.
   */
  public final Long categoryID() {
    return this.categoryID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Called to inject the argument for parameter <code>category</code>.
   **
   ** @param  category           the category of the <code>Role</code> in
   **                            Identity Manager to handle.
   */
  public void category(final String category) {
    this.category = category;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns category of the <code>Role</code> in Identity Manager to handle.
   **
   ** @return                    the category of the <code>Role</code> in
   **                            Identity Manager to handle.
   */
  public final String category() {
    return this.category;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   assignee
  /**
   ** Returns the {@link List} of recipients in Identity Manager to be assigned
   ** to the <code>Role</code>.
   **
   ** @return                    the {@link List} of recipients in Identity
   **                            Manager to be assigned to this
   **                            <code>Role</code>.
   */
  public final List<MemberShip.Recipient> assignee() {
    return this.assignee;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   revokee
  /**
   ** Returns the {@link List} of recipients in Identity Manager to be revoked
   ** from this <code>Role</code>.
   **
   ** @return                    the {@link List} of recipients in Identity
   **                            Manager to be revoked from this
   **                            <code>Role</code>.
   */
  public final List<MemberShip.Recipient> revokee() {
    return this.revokee;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of a role like catogory and additional parameters. If
   ** it's <code>false</code> only the name of the role has to be present.
   **
   ** @param  operation          the operational mode of validation.
   **                            If it's set to <code>create</code> the
   **                            validation is extended to check for all the
   **                            mandatory parameters of a role like displayName
   **                            etc. If it's something else only the name of
   **                            the role has to be present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation)
    throws BuildException {

    // ensure inheritance
    super.validate(operation);

    // enforce validation of mandatory attributes if requested
    if (operation == ServiceOperation.create) {
      if (StringUtility.isEmpty(this.category))
        handleAttributeMissing("category");

      if (StringUtility.isEmpty(displayName()))
        handleAttributeMissing("displayName");

      if (this.revokee != null && this.revokee.size() > 0)
        handleElementUnexpected("memberShip", operation.id());
    }
    else if (operation == ServiceOperation.delete) {
      if (!this.parameter().isEmpty())
        handleElementUnexpected("parameter", operation.id());

      if (this.assignee.size() > 0)
        handleElementUnexpected("memberShip", ServiceOperation.assign.id());

      if (this.revokee.size() > 0)
        handleElementUnexpected("memberShip", ServiceOperation.revoke.id());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMemberShip
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  action             the operation to apply on the specified
   **                            <code>userid</code>; either
   **                            {@link ServiceOperation#assign} or
   **                            {@link ServiceOperation#revoke}.
   ** @param  recipient          the {@link List} of recipients either user
   **                            profiles or roles in Identity Manager to become
   **                            membership of the role.
   */
  public void addMemberShip(final ServiceOperation action, final List<MemberShip.Recipient> recipient) {
    // distribute the given collection accordingly to the operational collection
    if (ServiceOperation.assign == action) {
      this.assignee.addAll(recipient);
    }
    else if (ServiceOperation.revoke == action) {
      this.revokee.addAll(recipient);
    }
  }
}