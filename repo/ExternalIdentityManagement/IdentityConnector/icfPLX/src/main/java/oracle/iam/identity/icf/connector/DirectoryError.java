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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DirectoryError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

////////////////////////////////////////////////////////////////////////////////
// interface DirectoryError
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface DirectoryError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                          = "GDS-";

  // 00041 - 00050 control extension support related errors
  static final String CONTROL_EXTENSION_NOT_EXISTS    = PREFIX + "00041";
  static final String CONTROL_EXTENSION_NOT_SUPPORTED = PREFIX + "00042";

  // 00051 - 00060 naming related errors
  static final String ENCODING_NOT_SUPPORTED          = PREFIX + "00051";
  static final String OPERATION_NOT_SUPPORTED         = PREFIX + "00052";
  static final String OPERATION_NOT_PERMITTED         = PREFIX + "00053";
  static final String INSUFFICIENT_INFORMATION        = PREFIX + "00054";
  static final String SEARCH_FILTER_INVALID           = PREFIX + "00055";
  static final String OBJECT_NAME_INVALID             = PREFIX + "00056";
  static final String OBJECT_PATH_NOT_EXISTS          = PREFIX + "00057";
  static final String OBJECT_PATH_NOT_RESOLVED        = PREFIX + "00058";
  static final String PASSWORD_CHANGE_REQUIRES_TLS    = PREFIX + "00059";

  // 00101 - 00120 entry operation errors
  static final String ENTRY_EXISTS                    = PREFIX + "00101";
  static final String ENTRY_AMBIGUOUS                 = PREFIX + "00102";
  static final String ENTRY_NOT_FOUND                 = PREFIX + "00103";
  static final String ENTRY_NOT_CREATED               = PREFIX + "00104";
  static final String ENTRY_NOT_DELETED               = PREFIX + "00105";
  static final String ENTRY_NOT_UPDATED               = PREFIX + "00106";
  static final String ENTRY_NOT_ENABLED               = PREFIX + "00107";
  static final String ENTRY_NOT_DISABLED              = PREFIX + "00108";
  static final String ENTRY_NOT_RENAMED               = PREFIX + "00109";
  static final String ENTRY_NOT_MOVED                 = PREFIX + "00110";
  static final String ENTRY_ALREADY_ENABLED           = PREFIX + "00111";
  static final String ENTRY_ALREADY_DISABLED          = PREFIX + "00112";
  static final String ENTRY_ALREADY_LOCKED            = PREFIX + "00113";
  static final String ENTRY_ALREADY_UNLOCKED          = PREFIX + "00114";
  static final String ENTRY_CONTEXT_NOT_EMPTY         = PREFIX + "00115";

  // 00121 - 00140 attribute operation errors
  static final String ATTRIBUTE_SCHEMA_VIOLATED        = PREFIX + "00121";
  static final String ATTRIBUTE_IN_USE                 = PREFIX + "00122";
  static final String ATTRIBUTE_INVALID_DATA           = PREFIX + "00123";
  static final String ATTRIBUTE_INVALID_TYPE           = PREFIX + "00124";
  static final String ATTRIBUTE_INVALID_VALUE          = PREFIX + "00125";
  static final String ATTRIBUTE_INVALID_SIZE           = PREFIX + "00126";
  static final String ATTRIBUTE_NOT_ASSIGNED           = PREFIX + "00127";
  static final String ATTRIBUTE_ALREADY_ASSIGNED       = PREFIX + "00128";
  static final String ATTRIBUTE_NOT_REMOVED            = PREFIX + "00129";
  static final String ATTRIBUTE_ALREADY_REMOVED        = PREFIX + "00130";

  // 00141 - 00150 changeLog related errors
  static final String CHANGELOG_NUMBER                 = PREFIX + "00141";
}