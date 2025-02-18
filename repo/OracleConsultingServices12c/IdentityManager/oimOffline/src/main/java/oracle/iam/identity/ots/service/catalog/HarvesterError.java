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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   HarvesterError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    HarvesterError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

////////////////////////////////////////////////////////////////////////////////
// interface HarvesterError
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public interface HarvesterError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PREFIX                     = "ARC-";

  // 00001 - 00010 task related errors
  static final String UNHANDLED                  = PREFIX + "00001";
  static final String GENERAL                    = PREFIX + "00002";
  static final String ABORT                      = PREFIX + "00003";
  static final String NOTIMPLEMENTED             = PREFIX + "00004";

  // 00011 - 00030 import process related errors
  static final String CATALOG_NOTFOUND           = PREFIX + "00011";
  static final String CATALOG_AMBIGUOUS          = PREFIX + "00012";
  static final String ROLE_NOTFOUND              = PREFIX + "00013";
  static final String ROLE_AMBIGUOUS             = PREFIX + "00014";
  static final String INSTANCE_MANDATORY         = PREFIX + "00015";
  static final String INSTANCE_NOTFOUND          = PREFIX + "00016";
  static final String INSTANCE_AMBIGUOUS         = PREFIX + "00017";
  static final String INSTANCE_LOOKUP_NOTFOUND   = PREFIX + "00018";
  static final String INSTANCE_LOOKUP_AMBIGUOUS  = PREFIX + "00019";
  static final String NAMEPACE_NOTFOUND          = PREFIX + "00020";
  static final String NAMEPACE_AMBIGUOUS         = PREFIX + "00021";
  static final String ENTITLEMENT_NOTFOUND       = PREFIX + "00022";
  static final String ENTITLEMENT_AMBIGUOUS      = PREFIX + "00023";
  static final String ENTITLEMENT_MANDATORY      = PREFIX + "00024";
  static final String ENTITLEMENT_NO_PREFIX      = PREFIX + "00025";

  // 00031 - 00040 import process related errors
  static final String MODIFY_CATALOG             = PREFIX + "00031";
  static final String MODIFY_CATALOG_IGNORED     = PREFIX + "00032";
  static final String MODIFY_ROLE                = PREFIX + "00033";
  static final String MODIFY_ROLE_IGNORED        = PREFIX + "00034";
  static final String MODIFY_INSTANCE            = PREFIX + "00035";
  static final String MODIFY_INSTANCE_IGNORED    = PREFIX + "00036";
  static final String MODIFY_ENTITLEMENT         = PREFIX + "00037";
  static final String MODIFY_ENTITLEMENT_IGNORED = PREFIX + "00038";

  // 00041 - 00041 import process related errors
  static final String OBJECT_ELEMENT_NOTFOUND    = PREFIX + "00041";
  static final String OBJECT_ELEMENT_EXISTS      = PREFIX + "00042";
  static final String OBJECT_ELEMENT_AMBIGUOUS   = PREFIX + "00043";
  static final String OBJECT_ATTRIBUTE_MISSING   = PREFIX + "00044";

  // 00051 - 00060 import process related errors
  static final String OPERATION_UNSUPPORTED      = PREFIX + "00051";
  static final String OPERATION_EXPORT_FAILED    = PREFIX + "00052";
  static final String OPERATION_IMPORT_FAILED    = PREFIX + "00053";
  static final String OPERATION_CREATE_FAILED    = PREFIX + "00054";
  static final String OPERATION_DELETE_FAILED    = PREFIX + "00055";
  static final String OPERATION_ENABLE_FAILED    = PREFIX + "00056";
  static final String OPERATION_DISABLE_FAILED   = PREFIX + "00057";
  static final String OPERATION_MODIFY_FAILED    = PREFIX + "00058";
  static final String OPERATION_ASSIGN_FAILED    = PREFIX + "00059";
  static final String OPERATION_REVOKE_FAILED    = PREFIX + "00050";

  // 00061 - 00070 request related errors
  static final String REQUEST_PAYLOAD_EMPTY      = PREFIX + "00061";
  static final String REQUEST_PAYLOAD_INCOMPLETE = PREFIX + "00062";
  static final String REQUEST_APPLICATION_MISSED = PREFIX + "00063";
  static final String REQUEST_NAMESPACE_MISSED   = PREFIX + "00064";
  static final String REQUEST_ENTITLEMENT_MISSED = PREFIX + "00065";
}