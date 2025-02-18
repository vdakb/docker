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

    System      :   Oracle Identity Manager Plugin Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   AbstractNamePolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractNamePolicy.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.foundation.event;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import oracle.iam.platform.Platform;

import oracle.iam.platform.authz.exception.AccessDeniedException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.identity.exception.UserSearchException;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserNamePolicy;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractNamePolicy
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>UserNameHander</code> provide the basic implementation of common
 ** tasks a user name policy needs.
 ** <p>
 ** This class implements the interface {@link UserNamePolicy}
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractNamePolicy extends    AbstractProcessHandler
                                         implements UserNamePolicy {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractNamePolicy</code> which use the default
   ** category for logging purpose.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractNamePolicy() {
    // ensure inheritance
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractNamePolicy</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractNamePolicy(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isUnique
  /**
   ** Check if the user with a specific attribute is unique.
   **
   ** @param  attribute          the logical name of the user entity to check.
   ** @param  value              the value for <code>attribute</code> to check
   **                            for uniqueness.
   **
   ** @return                    <code>true</code> if the user with a specific
   **                            attribute is unique; otherwise
   **                            <code>false</code>.
   **
   ** @throws AccessDeniedException if the executing user has no access to the
   **                               details.
   ** @throws UserSearchException   if an unkown attribute is used for the
   **                               operation.
   */
  protected boolean isUnique(final String attribute, final String value)
    throws AccessDeniedException
    ,      UserSearchException {

    UserManager    manager   = Platform.getService(UserManager.class);
    SearchCriteria criteria  = new SearchCriteria(attribute, value, SearchCriteria.Operator.EQUAL);
    Set<String>    returning = new HashSet<String>();
    returning.add(attribute);

    List<User> list = manager.search(criteria, returning, null);
    return (list == null || list.size() == 0 ? true : false);
  }
}
