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

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Entity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface Entity
// ~~~~~~~~~ ~~~~~~
/**
 ** The <code>Entity</code> declares the usefull constants to deal with
 ** <code>Oracle Identity Manager</code>s common data field declarations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the mapping key contained in a collection to specify that the system
   ** identifier of an Entity should be mapped
   */
  static final String FIELD_KEY          = "Key";

  /**
   ** the mapping key contained in a collection to specify that the name of an
   ** Entity should be mapped
   */
  static final String FIELD_NAME         = "Name";

  /**
   ** the mapping key contained in a collection to specify that the description
   ** of an Entity should be mapped
   */
  static final String FIELD_DESCRIPTION  = "Description";

  /**
   ** the mapping key contained in a collection to specify that the encrypted
   ** flag of an Entity should be mapped
   */
  static final String FIELD_ENCRYPTED    = "Encrypted";

  /**
   ** the mapping key contained in a collection to specify that the deleted flag
   ** of an Entity should be mapped
   */
  static final String FIELD_DELETED      = "Soft Deleted";

  /**
   ** the mapping key contained in a collection to specify that the status of an
   ** Entity should be mapped
   */
  static final String FIELD_STATUS       = "Status";

  /**
   ** the mapping key contained in a collection to specify that the row version
   ** of an Entity should be mapped
   */
  static final String FIELD_VERSION      = "Row Version";

  /**
   ** the mapping key contained in a collection to specify that the data
   ** protection level of an Entity should be mapped
   */
  static final String FIELD_SYSTEMLEVEL  = "System Level";

  /**
   ** the mapping key contained in a collection to specify that the created by
   ** of an Entity should be mapped
   */
  static final String FIELD_CREATEDBY    = "Created By";

  /**
   ** the mapping key contained in a collection to specify that the creation
   ** date of an Entity should be mapped
   */
  static final String FIELD_CREATIONDATE = "Creation Date";

  /**
   ** the mapping key contained in a collection to specify that the update by of
   ** an Entity should be mapped
   */
  static final String FIELD_UPDATEDBY    = "Updated By";

  /**
   ** the mapping key contained in a collection to specify that the update date
   ** of an Entity should be mapped
   */
  static final String FIELD_UPDATEDATE   = "Update Date";

  /**
   ** the mapping key contained in a collection to specify that the notes of an
   ** Entity should be mapped
   */
  static final String FIELD_NOTE         = "Note";

  /** Status indicating entity is active. */
  static final String STATUS_ACTIVE      = "Active";

  /** Status indicating entity is deleted. */
  static final String STATUS_DELETED     = "Deleted";

  /** Status indicating entity is disabled. */
  static final String STATUS_DISABLED    = "Disabled";
}