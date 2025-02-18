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

    File        :   Organization.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Organization.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import oracle.iam.identity.common.spi.OrganizationInstance;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.common.spi.RoleInstance;

////////////////////////////////////////////////////////////////////////////////
// class Organization
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>Organization</code> represents a organization in Identity Manager that
 ** might be created, updated or deleted after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Organization extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Organization</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Organization() {
    // ensure inheritance
    super(new OrganizationInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRole
  /**
   ** Call by the ANT deployment to inject the embedded element
   ** <code>role</code>.
   **
   ** @param  profile            the {@link Role} to add.
   **
   ** @throws BuildException     if the specified {@link Role} object is
   **                            already part of this operation.
   */
  public void addConfiguredRole(final Role profile)
    throws BuildException {

    instance().addRole((RoleInstance)profile.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link OrganizationInstance} delegate of Identity Manager to
   ** handle.
   **
   ** @return                    the {@link OrganizationInstance} delegate of
   **                            Identity Manager to handle.
   */
  public final OrganizationInstance instance() {
    if (isReference())
      return ((Organization)getCheckedRef()).instance();

    return (OrganizationInstance)this.delegate;
  }
}