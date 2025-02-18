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

    File        :   OrganizationInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OrganizationInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>OrganizationInstance</code> represents a organization in Oracle
 ** Identity Manager that might be created, deleted or changed after or during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OrganizationInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String GRANT  = "grant";
  public static final String REVOKE = "revoke";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private List<String> grant  = new ArrayList<String>();
  private List<String> revoke = new ArrayList<String>();

  private List<RoleInstance> role = new ArrayList<RoleInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrganizationInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrganizationInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   grant
  /**
   ** Returns the {@link List} of userid's in Identity Manager to grant to the
   ** organization.
   **
   ** @return                    the {@link List} of userid's in Identity
   **                            Manager to grant or revoke from the
   **                            organization.
   */
  public final List<String> grant() {
    return this.grant;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Returns the {@link List} of userid's in Identity Manager to revoke from
   ** the administrative role of the organization.
   **
   ** @return                    the {@link List} of userid's in Identity
   **                            Manager to grant or revoke from the
   **                            organization.
   */
  public final List<String> revoke() {
    return this.revoke;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   roles
  /**
   ** Returns the administrative role mapping of the Object Instance in Identity
   ** Manager to handle.
   **
   ** @return                    the administrative role mapping of the Object
   **                            Instance in Identity Manager to handle.
   */
  public final List<RoleInstance> role() {
    return this.role;
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
   ** mandatory attributes of an organization like type. If it's
   ** <code>false</code> only the name of the organization has to be present.
   **
   ** @param  strict             the mode of validation.
   **                            If it's set to <code>true</code> the validation
   **                            is extended to check for all the mandatory
   **                            attributes of an organization like type. If
   **                            it's <code>false</code> only the name of the
   **                            organization has to be present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final boolean strict)
    throws BuildException {

    // enforce validation of mandatory attributes if requested
    if (strict) {
      if (this.parameter().isEmpty())
        handleElementMissing("parameter");

      if (StringUtility.isEmpty((String)this.parameter().get(OrganizationManagerConstants.AttributeName.ORG_TYPE.getId())))
        handleAttributeMissing(OrganizationManagerConstants.AttributeName.ORG_TYPE.getId());
    }

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRole
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  role               the administrative role configuration to be
   **                            applied on the organization in Idenrity
   **                            Manager.
   **
   ** @throws BuildException     in case the {@link  RoleInstance} is already
   **                            addeddoes to the task to perform.
   */
  public void addRole(final RoleInstance role) {
    if (this.role.contains(role))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, role));

    this.role.add(role);
  }
}