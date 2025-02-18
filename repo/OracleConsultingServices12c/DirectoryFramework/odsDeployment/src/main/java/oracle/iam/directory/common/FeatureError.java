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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   FeatureError.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common;

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

  // 00001 - 00010 general operational errors
  static final String UNHANDLED                       = PREFIX + "00001";
  static final String GENERAL                         = PREFIX + "00002";
  static final String ABORT                           = PREFIX + "00003";
  static final String NOTIMPLEMENTED                  = PREFIX + "00004";
  static final String CLASSINVALID                    = PREFIX + "00007";
  static final String CLASSCONSTRUCTOR                = PREFIX + "00008";
  static final String ARGUMENT_IS_NULL                = PREFIX + "00004";
  static final String ATTRIBUTE_IS_NULL               = PREFIX + "00005";

  // 00011 - 00020 file related errors
  static final String FILE_MISSING                    = PREFIX + "00011";
  static final String FILE_ENCODING_TYPE              = PREFIX + "00012";

  // 00021 - 00040 export/import related errors
  static final String EXPORT_ATTRIBUTE_ONLYONCE       = PREFIX + "00021";
  static final String EXPORT_FILE_MANDATORY           = PREFIX + "00022";
  static final String EXPORT_FILE_ONLYONCE            = PREFIX + "00023";
  static final String EXPORT_FILE_SEARCH              = PREFIX + "00024";
  static final String EXPORT_FILE_DESCRIPTION         = PREFIX + "00025";
  static final String EXPORT_FILE_ISDIRECTORY         = PREFIX + "00026";
  static final String EXPORT_FILE_NODIRECTORY         = PREFIX + "00027";
  static final String EXPORT_FILE_NOPERMISSION        = PREFIX + "00028";
  static final String IMPORT_FILE_CONSTRAINT          = PREFIX + "00029";
  static final String IMPORT_FILE_MANDATORY           = PREFIX + "00030";
  static final String IMPORT_FILE_ONLYONCE            = PREFIX + "00031";
  static final String IMPORT_FILE_ISDIRECTORY         = PREFIX + "00032";
  static final String IMPORT_FILE_NOTEXISTS           = PREFIX + "00033";
  static final String IMPORT_FILE_NOPERMISSION        = PREFIX + "00034";
  static final String IMPORT_FILE_FETCH               = PREFIX + "00035";

  // 00041 - 00060 LDIF parsing related errors
  static final String LDIF_LINE                       = PREFIX + "00041";
  static final String LDIF_LINE_NOWHERE               = PREFIX + "00047";
  static final String LDIF_UNEXPECTED                 = PREFIX + "00042";
  static final String LDIF_EXPECTING_OID              = PREFIX + "00043";
  static final String LDIF_EXPECTING_PREFIX           = PREFIX + "00044";
  static final String LDIF_EXPECTING_SEPARATOR        = PREFIX + "00045";
  static final String LDIF_EXPECTING_ATTRIBUTE        = PREFIX + "00046";
  static final String LDIF_EXPECTING_CRITICALITY      = PREFIX + "00047";
  static final String LDIF_CONSTRUCT_URL              = PREFIX + "00048";
  static final String LDIF_CONSTRUCT_STRING           = PREFIX + "00049";
  static final String LDIF_CHANGE_TYPE_UNKNOW         = PREFIX + "00050";
  static final String LDIF_CHANGE_TYPE_NOTSUPPORTED   = PREFIX + "00051";

  // 00061 - 00080 DSML parsing related errors
  static final String DSML_SEARCH_DESCRIPTOR          = PREFIX + "00061"; /* A search descriptor is required to perform this operation */
  static final String DSML_LISTENER_ONLYONE           = PREFIX + "00062"; /* Only one listener supported */
  static final String DSML_TAG_OPENING_NOT_RECOGNIZED = PREFIX + "00063"; /* The opening tag %1$s is not recognized in this context */
  static final String DSML_TAG_CLOSING_NOT_RECOGNIZED = PREFIX + "00064"; /* The closing tag %1$s is not recognized in this context */
  static final String DSML_UNEXPECTED_EVENT           = PREFIX + "00065"; /* Document error: An unexpected event occured */
  static final String DSML_EXPECTIING_NAMESPACE       = PREFIX + "00066"; /* Document error: Expecting namespace declaration %1$s */
  static final String DSML_EXPECTIING_OPENING_TAG     = PREFIX + "00067"; /* Document error: Expecting opening tag %1$s, found %2$s instead */
  static final String DSML_EXPECTIING_CLOSING_TAG     = PREFIX + "00068"; /* Document error: Expecting closing tag %1$s, found %2$s instead */
  static final String DSML_EXPECTIING_ATTRIBUTE       = PREFIX + "00069"; /* Document error: The element %1$s is missing required attribute %2$s */
  static final String DSML_EXPECTIING_ATTRIBUTE_ONCE  = PREFIX + "00070"; /* Document error: Entries can only have one dishtinguished name */
  static final String DSML_VALUE_INVALID              = PREFIX + "00071"; /* Document error: Element %1$s has invalid value %2$s */
  static final String DSML_ROOT_CLOSING_OUTSIDE       = PREFIX + "00072"; /* Document error: Closing tag %1$s was not expected, the document root has been closed */
  static final String DSML_ROOT_DOCUMENT_STILL_OPEN   = PREFIX + "00073"; /* Document error: The root element is still open at end of document */

  // 00081 - 00100 entry and attribute operational related errors
  static final String NOT_SUPPORTED                   = PREFIX + "00081";
  static final String NOT_EMPTY                       = PREFIX + "00082";
  static final String OBJECT_CREATE                   = PREFIX + "00083";
  static final String OBJECT_DELETE                   = PREFIX + "00084";
  static final String OBJECT_MODIFY                   = PREFIX + "00085";
  static final String OBJECT_RENAME                   = PREFIX + "00086";
  static final String OBJECT_EXISTS                   = PREFIX + "00087";
  static final String OBJECT_NOT_EXISTS               = PREFIX + "00088";
  static final String OBJECT_AMBIGUOUS                = PREFIX + "00089";
  static final String OBJECT_ASSIGN                   = PREFIX + "00090";
  static final String ATTRIBUTE_SCHEMA                = PREFIX + "00091";
  static final String ATTRIBUTE_DATA                  = PREFIX + "00092";
  static final String ATTRIBUTE_TYPE                  = PREFIX + "00093";
  static final String ATTRIBUTE_SIZE                  = PREFIX + "00094";
  static final String ATTRIBUTE_IN_USE                = PREFIX + "00095";

  // 00101 - 00110 configuration related errors
  static final String COMMAND_PROPERTY_ONLYONCE       = PREFIX + "00101";
  static final String COMMAND_EXCECUTION_FAILED       = PREFIX + "00102";
}