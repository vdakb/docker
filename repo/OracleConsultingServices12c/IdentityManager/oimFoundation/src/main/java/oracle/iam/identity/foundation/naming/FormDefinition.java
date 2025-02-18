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

    File        :   FormDefinition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FormDefinition.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface FormDefinition
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>FormDefinition</code> declares the usefull constants to deal with
 ** <code>User Defined Form</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface FormDefinition extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for Form Definitions. */
  static final String PREFIX                 = "Structure Utility.";

  /**
   ** the mapping key contained in a collection to specify that the form system
   ** key should be resolved
   */
  static final String KEY                    = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the form name
   ** should be resolved
   */
  static final String NAME                   = PREFIX + "Table Name";

  /**
   ** the mapping key contained in a collection to specify that the form name
   ** should be resolved
   */
  static final String DESCRIPTION            = PREFIX + FIELD_DESCRIPTION;

  /**
   ** the mapping key contained in a collection to specify that the form type
   ** should be resolved
   */
  static final String TYPE                   = PREFIX + "Form Type";

  /**
   ** the mapping key contained in a collection to specify that the current form
   ** version should be resolved
   */
  static final String VERSION_CURRENT        = "SDK_CURRENT_VERSION";

  /**
   ** the mapping key contained in a collection to specify that the current form
   ** version label should be resolved
   */
  static final String LABEL_CURRENT           = "CURRENT_SDL_LABEL";

  /**
   ** the mapping key contained in a collection to specify that the latest form
   ** version should be resolved
   */
  static final String VERSION_LATEST         = PREFIX + "Latest Version";

  /**
   ** the mapping key contained in a collection to specify that the latest form
   ** version label should be resolved
   */
  static final String LABEL_LATEST           = PREFIX + "Structure Utility Version Label.Latest Version Label";

  /**
   ** the mapping key contained in a collection to specify that the latest form
   ** version should be resolved
   */
  static final String VERSION_ACTIVE         = PREFIX + "Active Version";

  /**
   ** the mapping key contained in a collection to specify that the latest form
   ** label should be resolved
   */
  static final String LABEL_ACTIVE           = PREFIX + "Structure Utility Version Label.Active Version Label";

  /**
   ** the mapping key contained in a collection to specify that the database
   ** schema should be resolved
   */
  static final String SCHEMA                 = PREFIX + "Schema";
  /**
   ** the mapping key contained in a collection to specify that the notes should
   ** be resolved
   */
  static final String NOTE                   = PREFIX + FIELD_NOTE;

  /** Column prefix name for <code>Process Form</code> columns. */
  static final String COLUMN_PREFIX          = PREFIX + "Additional Columns.";
  /**
   ** the mapping key contained in a collection to specify that the column key
   ** should be resolved
   */
  static final String COLUMN_KEY             = COLUMN_PREFIX + FIELD_KEY;

  static final String COLUMN_VERSION         = COLUMN_PREFIX + "Version";

  /**
   ** the mapping key contained in a collection to specify that the column name
   ** should be resolved
   */
  static final String COLUMN_NAME            = COLUMN_PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the column label
   ** should be resolved
   */
  static final String COLUMN_LABEL           = COLUMN_PREFIX + "Field Label";

  /**
   ** the mapping key contained in a collection to specify that the column type
   ** should be resolved
   */
  static final String COLUMN_TYPE            = COLUMN_PREFIX + "Field Type";

  /**
   ** the mapping key contained in a collection to specify that the column
   ** variant type should be resolved
   */
  static final String COLUMN_VARIANT         = COLUMN_PREFIX + "Variant Type";

  /**
   ** the mapping key contained in a collection to specify that the column type
   ** should be resolved
   */
  static final String COLUMN_LENGTH          = COLUMN_PREFIX + "Length";

  /**
   ** the mapping key contained in a collection to specify that the column
   ** default value should be resolved
   */
  static final String COLUMN_VALUE           = COLUMN_PREFIX + "Default Value";

  static final String COLUMN_PROFILE_ENABLED = COLUMN_PREFIX + "Profile Enabled";

  /**
   ** the mapping key contained in a collection to specify that the column flag
   ** encrypted should be resolved
   */
  static final String COLUMN_ENCRYPTED       = COLUMN_PREFIX + FIELD_ENCRYPTED;

  /** Prefix name for <code>Process Form</code> child table properties. */
  static final String CHILD_PREFIX           = PREFIX + "Child Tables.";

  /**
   ** the mapping key contained in a collection to specify that the child table
   ** key should be resolved
   */
  static final String CHILD_KEY              = CHILD_PREFIX + "Child Key";

  /**
   ** the mapping key contained in a collection to specify that the child table
   ** version should be resolved
   */
  static final String CHILD_VERSION          = CHILD_PREFIX + "Child Version";
}