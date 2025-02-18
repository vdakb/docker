/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   EndpointError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.api;

////////////////////////////////////////////////////////////////////////////////
// interface EndpointError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface EndpointError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                            = "UID-";

  // 00011 - 00015 method argument related errors
  static final String ARGUMENT_IS_NULL                  = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE                 = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE                = PREFIX + "00013";
  static final String ARGUMENT_LENGTH_MISMATCH          = PREFIX + "00014";
  static final String ARGUMENT_SIZE_MISMATCH            = PREFIX + "00015";

  // 00021 - 00030 method invokation related errors
  static final String METHOD_NOT_PERMITTED              = PREFIX + "00021";

  // 00031 - 00040 operation related errors
  static final String OPERATION_NOT_PERMITTED           = PREFIX + "00031";

  // 00041 - 00050 tenant related errors
  static final String TENANT_SEARCH_NOT_PERMITTED       = PREFIX + "00041";
  static final String TENANT_LOOKUP_NOT_PERMITTED       = PREFIX + "00042";
  static final String TENANT_MODIFY_NOT_PERMITTED       = PREFIX + "00043";
  static final String TENANT_GENERATE_NOT_PERMITTED     = PREFIX + "00044";
  static final String TENANT_REGISTER_NOT_PERMITTED     = PREFIX + "00045";
}