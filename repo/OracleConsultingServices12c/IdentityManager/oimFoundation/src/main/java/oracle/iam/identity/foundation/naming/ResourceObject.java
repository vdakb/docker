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

    File        :   ResourceObject.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ResourceObject.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface ResourceObject
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>ResourceObject</code> declares the usefull constants to deal with
 ** <code>Resource</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface ResourceObject extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String STATUS_PROVISIONED     = "Provisioned";
  static final String STATUS_ENABLED         = "Enabled";
  static final String STATUS_DISABLED        = "Disabled";
  static final String STATUS_REVOKED         = "Revoked";

  static final String TYPE_SYSTEM            = "System";
  static final String TYPE_GENERIC           = "Generic";
  static final String TYPE_APPLICATION       = "Application";
  static final String TYPE_DISCONNECTED      = "Disconnected";

  static final String ORDER_FOR_USER         = "U";
  static final String ORDER_FOR_ORGANIZATION = "O";

  /** Standard prefix name for Resource Objects. */
  static final String PREFIX                 = "Objects.";

  /** Standard prefix name for permissions. */
  static final String PERMISSION_PREFIX      = "Objects-Groups.";

  /** Standard prefix name for provisioning status. */
  static final String STATUS_PREFIX          = "Object Status.";

  /** Standard prefix name for Object Instance. */
  static final String OBJECT_INSTANCE_PREFIX = "Object Instance.";

  /** Standard prefix name for User Object Instance (OIU). */
  static final String USER_INSTANCE_PREFIX   = "Users-Object Instance For User.";

  /** Standard prefix name for Reconciliation Field. */
  static final String RECONCILAITION_PREFIX  = "Reconciliation Fields.";

  /**
   ** the logical name of the column to access the type of a Resource Object.
   */
  static final String FIELD_TYPE             = "Type";

  /**
   ** the logical name of the column to access the order for of a Resource
   ** Object.
   */
  static final String FIELD_ORDER_FOR        = "Order For";

  /**
   ** the logical name of the column to access the provisioning status of a
   ** Resource Object.
   */
  static final String FIELD_STATUS           = STATUS_PREFIX + "Status";

  /**
   ** the logical name of the column to access the property
   ** <code>Allow All</code> of a Resource Object.
   */
  static final String FIELD_ALLOWALL         = "Allow All";

  /**
   ** the logical name of the column to access the property
   ** <code>Allow Multiple</code> of a Resource Object.
   */
  static final String FIELD_ALLOWMULTIPLE    = "Allow Multiple";

  /**
   ** the logical name of the column to access the property
   ** <code>Auto Pre-Populate</code> of a Resource Object.
   */
  static final String FIELD_PREPOPULATE      = "Auto Prepopulate";

  /**
   ** the logical name of the column to access the property
   ** <code>Auto Save</code> of a Resource Object.
   */
  static final String FIELD_AUTOSAVE         = "Auto Save";

  /**
   ** the logical name of the column to access the property
   ** <code>Auto Launch</code> of a Resource Object.
   */
  static final String FIELD_AUTOLAUNCH       = "Auto Launch";

  /**
   ** the logical name of the column to access the property
   ** <code>Provisioning by Object Admin Only</code> of a Resource Object.
   */
  static final String FIELD_ADMINONLY        = "Admin Only";

  /**
   ** the mapping key contained in a collection to specify that the object
   ** system key should be resolved
   */
  static final String KEY                    = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the object name
   ** should be resolved
   */
  static final String NAME                   = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the object type
   ** should be resolved
   */
  static final String TYPE                   = PREFIX + FIELD_TYPE;

  /**
   ** the mapping key contained in a collection to specify that should be
   ** resolved for what type of object category the Resource Object is
   ** orderable.
   */
  static final String ORDER_FOR              = PREFIX + FIELD_ORDER_FOR;

  /**
   ** the mapping key contained in a collection to specify that the object name
   ** should be resolved
   */
  static final String STATUS                 = PREFIX + FIELD_STATUS;

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Allow All</code> should be resolved
   */
  static final String ALLOWALL               = PREFIX + FIELD_ALLOWALL;

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Allow Multiple</code> should be resolved
   */
  static final String ALLOWMULTIPLE          = PREFIX + FIELD_ALLOWMULTIPLE;

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Auto Pre-populate</code> should be resolved
   */
  static final String PREPOULATE             = PREFIX + FIELD_PREPOPULATE;

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Auto Save</code> should be resolved
   */
  static final String AUTOSAVE               = PREFIX + FIELD_AUTOSAVE;

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Auto Launch</code> should be resolved
   */
  static final String AUTOLAUNCH             = PREFIX + FIELD_AUTOLAUNCH;

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Provisioning by Object Admin Only</code> should be resolved
   */
  static final String ADMINONLY              = PREFIX + FIELD_ADMINONLY;

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Write Access</code> should be resolved
   */
  static final String PERMISSION_WRITE       = PERMISSION_PREFIX + "Write";

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Delete Access</code> should be resolved
   */
  static final String PERMISSION_DELETE       = PERMISSION_PREFIX + "Delete";

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Soft Deleted</code> should be resolved
   */
  static final String DELETED                = PREFIX + FIELD_DELETED;

  /**
   ** the mapping key contained in a collection to specify that the object
   ** instance should be resolved
   */
  static final String OBJECT_INSTANCE        = OBJECT_INSTANCE_PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the object
   ** instance should be resolved
   */
  static final String USER_INSTANCE          = USER_INSTANCE_PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the
   ** reconciliation field system identifier should be resolved
   */
  static final String RECON_FIELD_KEY        = PREFIX + RECONCILAITION_PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the
   ** reconciliation field name should be resolved
   */
  static final String RECON_FIELD_NAME       = PREFIX + RECONCILAITION_PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the
   ** reconciliation field type should be resolved
   */
  static final String RECON_FIELD_TYPE       = PREFIX + RECONCILAITION_PREFIX + "Type";

  /**
   ** the mapping key contained in a collection to specify that the
   ** reconciliation field nullable should be resolved
   */
  static final String RECON_FIELD_NULLABLE   = PREFIX + RECONCILAITION_PREFIX + "Nullable";

  /**
   ** the mapping key contained in a collection to specify that the
   ** reconciliation field parent field should be resolved
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** This field name is not prefixed like all other logical field names
   */
  static final String RECON_FIELD_PARENT     = "PARENTFIELD";
}