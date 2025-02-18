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

    File        :   AccessPolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AccessPolicy.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface AccessPolicy
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>AccessPolicy</code> declares the usefull constants to deal with
 ** <code>Access Policies</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface AccessPolicy extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for Access Policies. */
  static final String PREFIX              = "Access Policies.";

  /** Standard prefix name for Resource Objects. */
  static final String RESOURCE_PREFIX     = "Access Policy-Resource Objects.";

  /** Standard prefix name for User Groups. */
  static final String GROUP_PREFIX        = "Access Policy-User Groups.";

  /** Standard prefix name for Policy Fields. */
  static final String FIELD_PREFIX        = "Policy Field Definition.";

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy Priority should be mapped
   */
  static final String FIELD_PRIORITY      = "Priority";

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy Retrofit Flag should be mapped
   */
  static final String FIELD_RETROFIT_FLAG = "Retrofit Flag";

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy By Request should be mapped
   */
  static final String FIELD_BYREQUEST     = "By Request";

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy Owner Type should be mapped
   */
  static final String FIELD_OWNERTYPE     = "Owner Type";

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy Owner should be mapped
   */
  static final String FIELD_OWNER         = "Owner";

  /**
   ** the mapping key contained in a collection to specify that the object
   ** system key should be resolved
   */
  static final String KEY                 = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the object name
   ** should be resolved
   */
  static final String NAME                = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy description should be resolved
   */
  static final String DESCRIPTION         = PREFIX + FIELD_DESCRIPTION;

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy priority should be resolved
   */
  static final String PRIORITY            = PREFIX + FIELD_PRIORITY;

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy resource objects denial should be resolved
   */
  static final String DENIAL              = RESOURCE_PREFIX + "Denial";

  /**
   ** the mapping key contained in a collection to specify that the flags
   ** be resolved that that specifies that the Resource Objects should be
   ** revoked if the policy doesn't fit anymore.
   */
  static final String OBJECT_REVOKE       = RESOURCE_PREFIX + "Revoke Objects";

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy retrofit flag should be resolved
   */
  static final String RETROFIT_FLAG       = PREFIX + FIELD_RETROFIT_FLAG;

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy by request should be resolved
   */
  static final String BYREQUEST           = PREFIX + FIELD_BYREQUEST;

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy owner type should be resolved
   */
  static final String OWNERTYPE           = PREFIX + FIELD_OWNERTYPE;

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy owner should be resolved
   */
  static final String OWNER               = PREFIX + FIELD_OWNER;

  /**
   ** the mapping key contained in a collection to specify that the access
   ** policy note should be resolved
   */
  static final String NOTE                = PREFIX + FIELD_NOTE;
}