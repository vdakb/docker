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

    File        :   ITResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ITResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface ITResource
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>ITResource</code> declares the usefull constants to deal with
 ** <code>IT Resource Definition</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface ITResource extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for IT Resources. */
  static final String PREFIX                    = "IT Resources.";

  /** Standard prefix name for permissions. */
  static final String PERMISSION_PREFIX         = "IT Resource-IT Resource Ownership.";

  /**
   ** the suffix contained in a collection to specify that the remote manager
   ** key should be mapped.
   */
  static final String FIELD_REMOTE_KEY          = "Remote Manager Key";

  /**
   ** the suffix contained in a collection to specify that the remote manager
   ** name should be mapped.
   */
  static final String FIELD_REMOTE_NAME         = "Remote Manager Name";

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** system identifier should be resolved
   */
  static final String KEY                       = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** name should be resolved
   */
  static final String NAME                      = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** remote manager key should be resolved
   */
  static final String REMOTE_KEY                = PREFIX + FIELD_REMOTE_KEY;

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** remote manager name should be resolved
   */
  static final String REMOTE_NAME               = PREFIX + FIELD_REMOTE_NAME;

  /**
   ** the mapping key contained in a collection to specify that an IT Resource
   ** is deleted should be resolved
   */
  static final String DELETED                   = "IT Resource.Soft Delete";

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** note should be resolved
   */
  static final String NOTE                      = PREFIX + FIELD_NOTE;

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** parameter system identifier should be resolved
   */
  static final String PARAMETER_KEY             = ITResourceType.PARAMETER + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** parameter name should be resolved
   */
  static final String PARAMETER_NAME            = ITResourceType.PARAMETER + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** parameter value key should be resolved
   */
  static final String PARAMETER_VALUE           = "IT Resources Type Parameter Value.Value";

  /**
   ** the mapping key contained in a collection to specify that an IT Resource
   ** is deleted should be resolved
   */
  static final String PARAMETER_DELETED         = "IT Resource.Parameter.Soft Delete";

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** parameter value key should be resolved
   */
  static final String PARAMETER_VALUE_KEY       = "IT Resources Type Parameter Value.Key";

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** parameter value encryption state should be resolved
   */
  static final String PARAMETER_VALUE_ENCRYPTED = "IT Resources Type Parameter.Encrypted";

  /**
   ** the mapping key contained in a collection to specify that the IT Resource
   ** parameter value note value should be resolved
   */
  static final String PARAMETER_VALUE_NOTE      = "IT Resources Type Parameter Value.Note";

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Read Access</code> should be resolved
   */
  static final String PERMISSION_READ           = PERMISSION_PREFIX + "Read";

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Write Access</code> should be resolved
   */
  static final String PERMISSION_WRITE          = PERMISSION_PREFIX + "Write";

  /**
   ** the mapping key contained in a collection to specify that the property
   ** <code>Delete Access</code> should be resolved
   */
  static final String PERMISSION_DELETE         = PERMISSION_PREFIX + "Delete";
}