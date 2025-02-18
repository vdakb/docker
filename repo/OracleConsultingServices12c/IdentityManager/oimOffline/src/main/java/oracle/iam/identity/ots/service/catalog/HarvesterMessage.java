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

    File        :   HarvesterMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    HarvesterMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

////////////////////////////////////////////////////////////////////////////////
// interface HarvesterMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public interface HarvesterMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PREFIX                   = "ARC-";

  // 00071 - 00080 export to xml process related errors
  static final String EXPORTING_XML_BEGIN          = PREFIX + "00071";
  static final String EXPORTING_XML_COMPLETE       = PREFIX + "00072";
  static final String EXPORTING_XML_STOPPED        = PREFIX + "00073";
  static final String EXPORTING_XML_SUCCESS        = PREFIX + "00074";
  static final String EXPORTING_XML_ERROR          = PREFIX + "00075";

  static final String COLLECTING_XML_BEGIN         = PREFIX + "00076";
  static final String COLLECTING_XML_COMPLETE      = PREFIX + "00077";

  static final String PROCESSING_ROWS             = PREFIX + "00078";
  static final String EXPORTING_XML_SUMMARY       = PREFIX + "00079";

  // 01001 - 01010 import process related messages
  static final String IMPORTING_BEGIN          = PREFIX + "01001";
  static final String IMPORTING_COMPLETE       = PREFIX + "01002";
  static final String IMPORTING_STOPPED        = PREFIX + "01003";
  static final String IMPORTING_SUCCESS        = PREFIX + "01004";
  static final String IMPORTING_ERROR          = PREFIX + "01005";
  static final String IMPORT_BEGIN             = PREFIX + "01006";
  static final String IMPORT_COMPLETE          = PREFIX + "01007";
  static final String IMPORT_SKIP              = PREFIX + "01008";
  static final String IMPORT_CATALOG_SUMMARY   = PREFIX + "01009";

  // 01011 - 01020 export process related messages
  static final String EXPORTING_BEGIN          = PREFIX + "01011";
  static final String EXPORTING_COMPLETE       = PREFIX + "01012";
  static final String EXPORTING_STOPPED        = PREFIX + "01013";
  static final String EXPORTING_SUCCESS        = PREFIX + "01014";
  static final String EXPORTING_ERROR          = PREFIX + "01015";

  static final String COLLECTING_BEGIN         = PREFIX + "01016";
  static final String COLLECTING_COMPLETE      = PREFIX + "01017";

  // 01031 - 01040 object/entity operation related messages
  static final String OPERATION_CREATE_BEGIN   = PREFIX + "01031";
  static final String OPERATION_CREATE_SUCCESS = PREFIX + "01032";
  static final String OPERATION_MODIFY_BEGIN   = PREFIX + "01033";
  static final String OPERATION_MODIFY_SUCCESS = PREFIX + "01034";
  static final String OPERATION_EXPORT_BEGIN   = PREFIX + "01035";
  static final String OPERATION_EXPORT_SUCCESS = PREFIX + "01036";
  static final String OPERATION_EXPORT_IGNORED = PREFIX + "01037";

  // 01041 - 01050 web service operation related messages
  static final String SERVICE_REQUEST_PAYLOAD  = PREFIX + "01041";
  static final String SERVICE_RESPONSE_PAYLOAD = PREFIX + "01042";
}