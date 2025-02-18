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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Group.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Group.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface Group
// ~~~~~~~~~ ~~~~~
/**
 ** The <code>Group</code> declares the usefull constants to deal with
 ** <code>Oracle Identity Manager Group</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface Group extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for users. */
  static final String PREFIX                   = "Groups.";

  /** Standard object key for users. */
  static final String DEFAULT_KEY              = "1";

  /** Standard object type for users. */
  static final String DEFAULT_NAME             = "Employee";

  /** Standard object name for user group. */
  static final String RESOURCE                 = "Xellerate Group";

  /**
   ** the key contained in a collection to specify that the group name should be
   ** mapped
   */
  static final String FIELD_NAME               = "Group Name";

  /**
   ** the key contained in a collection to specify that the role name should be
   ** mapped
   */
  static final String FIELD_ROLE               = "Role Name";

  /**
   ** the key contained in a collection to specify that the role display name
   ** should be mapped
   */
  static final String FIELD_DISPLAY_NAME       = "Role Display Name";

  /**
   ** the key contained in a collection to specify that the role description
   ** should be mapped
   */
  static final String FIELD_DESCRIPTION        = "Role Description";

  /**
   ** the key contained in a collection to specify that the role category system
   ** identifier should be mapped
   */
  static final String FIELD_CATEGORY_KEY       = "Role Category Key";

  /**
   ** the key contained in a collection to specify that the role category name
   ** should be mapped
   */
  static final String FIELD_CATEGORY_NAME      = "ROLE CATEGORY NAME";
  /**
   ** the key contained in a collection to specify that the role owner system
   ** identifier should be mapped
   */
  static final String FIELD_OWNER_KEY          = "Role Owner Key";

  /**
   ** the key contained in a collection to specify that the role owner first
   ** name should be mapped
   */
  static final String FIELD_OWNER_FIRST_NAME   = "OWNER FIRST NAME";

  /**
   ** the key contained in a collection to specify that the role owner last
   ** name should be mapped
   */
  static final String FIELD_OWNER_LAST_NAME    = "OWNER LAST NAME";

  /**
   ** the key contained in a collection to specify that the role owner login
   ** name should be mapped
   */
  static final String FIELD_OWNER_LOGIN_NAME   = "OWNER LOGIN NAME";

  /**
   ** the key contained in a collection to specify that the role owner display
   ** name should be mapped
   */
  static final String FIELD_OWNER_DISPLAY_NAME = "OWNER DISPLAY NAME";

  /**
   ** the key contained in a collection to specify that the group email address
   ** should be mapped
   */
  static final String FIELD_EMAIL              = "E-mail";

  /**
   ** the key contained in a collection to specify that the role namespace
   ** should be mapped
   */
  static final String FIELD_NAMESPACE          = "Role Namespace";

  /**
   ** the mapping key contained in a collection to specify that the group system
   ** key should be resolved
   */
  static final String KEY                      = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the group name
   ** should be resolved
   */
  static final String NAME                     = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the role name
   ** should be resolved
   */
  static final String ROLE                     = PREFIX + FIELD_ROLE;

  /**
   ** the mapping key contained in a collection to specify that the group
   ** display name should be resolved
   */
  static final String DISPLAY_NAME             = PREFIX + FIELD_DISPLAY_NAME;

  /**
   ** the mapping key contained in a collection to specify that the group
   ** description should be resolved
   */
  static final String DESCRIPTION              = PREFIX + FIELD_DESCRIPTION;

  /**
   ** the mapping key contained in a collection to specify that the group email
   ** address should be resolved
   */
  static final String EMAIL                    = PREFIX + FIELD_EMAIL;

  /**
   ** the mapping key contained in a collection to specify that the groupl
   ** namespace should be resolved
   */
  static final String NAMESPACE                = PREFIX + FIELD_NAMESPACE;
  /**
   ** the mapping key contained in a collection to specify that the group
   ** category system identifier should be resolved
   */
  static final String CATEGORY_KEY             = PREFIX + FIELD_CATEGORY_KEY;

  /**
   ** the mapping key contained in a collection to specify that the group
   ** category name should be resolved
   */
  static final String CATEGORY_NAME            = FIELD_CATEGORY_NAME;

  /**
   ** the mapping key contained in a collection to specify that the group
   ** owner system identifier should be resolved
   */
  static final String OWNER_KEY                = PREFIX + FIELD_OWNER_KEY;

  /**
   ** the mapping key contained in a collection to specify that the group
   ** owner first name should be resolved
   */
  static final String OWNER_FIRST_NAME         = FIELD_OWNER_FIRST_NAME;

  /**
   ** the mapping key contained in a collection to specify that the group
   ** owner last name should be resolved
   */
  static final String OWNER_LAST_NAME          = FIELD_OWNER_LAST_NAME;

  /**
   ** the mapping key contained in a collection to specify that the group
   ** owner login name should be resolved
   */
  static final String OWNER_LOGIN_NAME         = FIELD_OWNER_LOGIN_NAME;

  /**
   ** the mapping key contained in a collection to specify that the group
   ** owner display name should be resolved
   */
  static final String OWNER_DISPLAY_NAME       = FIELD_OWNER_DISPLAY_NAME;
}