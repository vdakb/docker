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

    File        :   FeatureMessage.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common;

////////////////////////////////////////////////////////////////////////////////
// interface FeatureMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface FeatureMessage extends FeatureConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 01001 - 01020 entry operation related messages
  static final String OBJECT_CREATE_BEGIN   = PREFIX + "01001";
  static final String OBJECT_CREATE_SUCCESS = PREFIX + "01002";
  static final String OBJECT_CREATE_SKIPPED = PREFIX + "01003";
  static final String OBJECT_MODIFY_BEGIN   = PREFIX + "01004";
  static final String OBJECT_MODIFY_SUCCESS = PREFIX + "01005";
  static final String OBJECT_MODIFY_SKIPPED = PREFIX + "01006";
  static final String OBJECT_DELETE_BEGIN   = PREFIX + "01007";
  static final String OBJECT_DELETE_SUCCESS = PREFIX + "01008";
  static final String OBJECT_RENAME_BEGIN   = PREFIX + "01009";
  static final String OBJECT_RENAME_SUCCESS = PREFIX + "01010";
  static final String OBJECT_MOVE_BEGIN     = PREFIX + "01011";
  static final String OBJECT_MOVE_SUCCESS   = PREFIX + "01012";

  // 01021 - 01030 export/import operation related messages
  static final String EXPORT_OBJECT         = PREFIX + "01021";
}