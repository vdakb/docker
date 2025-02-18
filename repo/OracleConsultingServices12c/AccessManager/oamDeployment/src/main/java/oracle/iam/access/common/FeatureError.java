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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   FeatureError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common;

////////////////////////////////////////////////////////////////////////////////
// interface FeatureError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface FeatureError extends FeatureConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00011 - 00020 operational errors
  static final String OPERATION_PRINT_FAILED        = PREFIX + "00011";

  // 00021 - 00030 common errors
  static final String COMMON_PORTNUMBER_INVALID     = PREFIX + "00021";

  // 00031 - 00040 instance element related errors
  static final String INSTANCE_MANDATORY            = PREFIX + "00031";
  static final String INSTANCE_ONLYONCE             = PREFIX + "00032";
  static final String INSTANCE_EXISTS               = PREFIX + "00033";
  static final String INSTANCE_NOTEXISTS            = PREFIX + "00034";
  static final String INSTANCE_CREATE               = PREFIX + "00035";
  static final String INSTANCE_MODIFY               = PREFIX + "00036";
  static final String INSTANCE_DELETE               = PREFIX + "00037";

  // 00041 - 00050 type element related errors
  static final String PARTNER_TYPE_MANDATORY        = PREFIX + "00041";
  static final String PARTNER_TYPE_ONLYONCE         = PREFIX + "00042";

  // 00051 - 00060 parameter element related errors
  static final String PARAMETER_VALUE_INVALID       = PREFIX + "00051";
  static final String PARAMETER_UNMODIFIABLE        = PREFIX + "00062";

  // 00061 - 00070 management element related errors
  static final String DOMAIN_OBJECTNAME_MALFORMED   = PREFIX + "00061";
  static final String DOMAIN_RUNTIMEBEAN_NOTFOUND   = PREFIX + "00062";
  static final String DOMAIN_CONFIGBEAN_NOTFOUND    = PREFIX + "00063";
  static final String DOMAIN_ATTRIBUTENAME_NOTFOUND = PREFIX + "00064";
  static final String DOMAIN_BEANINSTANCE_NOTFOUND  = PREFIX + "00065";
  static final String DOMAIN_QUERY_FAILED           = PREFIX + "00066";

  // 00071 - 00080 registration related errors
  static final String REMOTE_BINDING_CONTEXT        = PREFIX + "00071";
  static final String REMOTE_BINDING_SCHEMA         = PREFIX + "00072";
  static final String REMOTE_BINDING_VALIDATION     = PREFIX + "00073";
  static final String REMOTE_BINDING_VIOLATION      = PREFIX + "00074";
  static final String REMOTE_BINDING_MARSHAL        = PREFIX + "00075";
  static final String REMOTE_BINDING_UNMARSHAL      = PREFIX + "00076";
  static final String REMOTE_BINDING_ENCODING       = PREFIX + "00077";
  static final String REMOTE_BINDING_FILE_OPEN      = PREFIX + "00078";
}