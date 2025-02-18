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

    File        :   Organization.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Organization.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface Organization
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>Organization</code> declares the usefull constants to deal with
 ** <code>Oracle Identity Manager Organization</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface Organization extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard object name for organizations. */
  static final String RESOURCE          = "Xellerate Organization";

  /** Standard prefix name for organizations. */
  static final String PREFIX            = "Organizations.";

  /** Standard object key for organizations. */
  static final String DEFAULT_KEY       = "1";

  /** Standard object name for organizations. */
  static final String DEFAULT_NAME      = "Xellerate Users";

  /** Standard customer type for organizations. */
  static final String DEFAULT_TYPE      = "Company";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** organizations name should be mapped
   */
  static final String FIELD_NAME        = "Organization Name";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** organizations status should be mapped
   */
  static final String FIELD_STATUS      = "Status";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** organizations type should be mapped
   */
  static final String FIELD_TYPE        = "Type";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** organizations parent should be mapped
   */
  static final String FIELD_PARENT_KEY  = "Parent Key";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** organizations parent name should be mapped
   */
  static final String FIELD_PARENT_NAME = "Parent Name";

  /**
   ** the mapping key contained in a collection to specify that the organization
   ** system key should be resolved
   */
  static final String KEY               = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the organization
   ** name should be resolved
   */
  static final String NAME              = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the organization
   ** status should be resolved
   */
  static final String STATUS            = PREFIX + FIELD_STATUS;

  /**
   ** the mapping key contained in a collection to specify that the organization
   ** type should be resolved
   */
  static final String TYPE              = PREFIX + FIELD_TYPE;

  /**
   ** the mapping key contained in a collection to specify that the key of the
   ** organization parent organization should be resolved
   */
  static final String PARENT_KEY        = PREFIX + FIELD_PARENT_KEY;

  /**
   ** the mapping key contained in a collection to specify that the name of the
   ** organization parent organization should be resolved
   */
  static final String PARENT_NAME       = PREFIX + FIELD_PARENT_NAME;
}