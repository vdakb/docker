/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   RoleFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import oracle.iam.identity.adapter.spi.RoleAdapter;

import oracle.iam.identity.foundation.AbstractServiceTask;

import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.AccessPolicy;

////////////////////////////////////////////////////////////////////////////////
// abstract class RoleFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** A factory to create <code>Role</code>s and their instance relationships.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class RoleFactory extends Factory {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a <code>RoleEntity</code> provisionable to users.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  roleName           the name of the <code>Role</code>.
   ** @param  policy             the <code>Access Policy</code> that is
   **                            triggered by the group membership operation.
   **
   ** @return                    the wrapper of a <code>User Group</code>.
   **
   ** @throws Exception          if reslving the role fails.
   */
  public static final RoleEntity create(final AbstractServiceTask task, final String roleName, AccessPolicy policy)
    throws Exception {

    RoleAdapter operation = new RoleAdapter(task.provider());
    return create(operation.resolveRole(roleName), roleName, policy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a <code>RoleEntity</code> provisionable to users.
   **
   ** @param  roleKey            the internal system identifier of the
   **                            <code>Role</code> to create.
   ** @param  roleName           the name of the <code>Role</code>.
   ** @param  policy             the <code>Access Policy</code> that is
   **                            triggered by the group membership operation.
   **
   ** @return                     the wrapper of a <code>User Group</code>.
   */
  public static final RoleEntity create(final String roleKey, final String roleName, AccessPolicy policy) {
    return create(Long.parseLong(roleKey), roleName, policy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a <code>RoleEntity</code> provisionable to users.
   **
   ** @param  roleKey            the internal system identifier of the
   **                            <code>Role</code> to create.
   ** @param  roleName           the name of the <code>Role</code>.
   ** @param  policy             the <code>Access Policy</code> that is
   **                            triggered by the group membership operation.
   **
   ** @return                     the wrapper of a <code>User Group</code>.
   */
  public static RoleEntity create(final long roleKey, final String roleName, AccessPolicy policy) {
    return new RoleEntity(roleKey, roleName, policy);
  }
}