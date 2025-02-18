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

    File        :   ITResourceType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ITResourceType.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface ITResource
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>ITResourceType</code> declares the usefull constants to deal with
 ** <code>IT Resource Type Definition</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface ITResourceType extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for organizations. */
  static final String PREFIX              = "IT Resources Type ";
  static final String DEFINITION          = PREFIX + "Definition.";
  static final String PARAMETER           = PREFIX + "Parameter.";

  /**
   ** the suffix contained in a collection to specify that the name should be
   ** mapped.
   */
  static final String FIELD_NAME          = "Server Type";

  /**
   ** the suffix in a collection to specify that the flag should be mapped that
   ** specifies that an IT Resource Definition type can be used multiple times.
   */
  static final String FIELD_MULTIPLE      = "Insert Multiple";

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition system key should be resolved
   */
  static final String KEY                 = DEFINITION + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition name should be resolved
   */
  static final String NAME                = DEFINITION + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the flag
   ** that an IT Resource Definition Type can be used multiple times should be
   ** resolved
   */
  static final String MULTIPLE            = DEFINITION + FIELD_MULTIPLE;

  /**
   ** the mapping key contained in a collection to specify that an IT Resource
   ** Definition Type is deleted should be resolved
   */
  static final String DELETED             = "IT Resource Type Definition.Soft Delete";

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition note should be resolved
   */
  static final String NOTE                = DEFINITION + FIELD_NOTE;

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition parameter system identifier should be resolved
   */
  static final String PARAMETER_KEY       = PARAMETER + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition parameter name should be resolved
   */
  static final String PARAMETER_NAME      = PARAMETER + "Name";

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition parameter default value should be resolved
   */
  static final String PARAMETER_DEFAULT   = "IT Resource Type Definition.IT Resource Type Parametr.Default Value";

  /**
   ** the mapping key contained in a collection to specify that the flag
   ** encrypted of a type definition parameter value should be resolved
   */
  static final String PARAMETER_ENCRYPTED = PARAMETER + FIELD_ENCRYPTED;

  /**
   ** the mapping key contained in a collection to specify that the flag
   ** deleted of a type definition parameter value should be resolved
   */
  static final String PARAMETER_DELETED   = "IT Resource Type Definition.IT Resources Type Parameter.Soft Delete";
}