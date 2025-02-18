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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AssertionMessage.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AssertionMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml;

////////////////////////////////////////////////////////////////////////////////
// interface AssertionMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface AssertionMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX           = "OAM-";

  // 01001 - 01010 XML parser related messages
  static final String PARSER_ENTITY_RESOLVED        = PREFIX + "01001";

  // 01011 - 01020 signature related messages
  static final String SIGNATURE_CORE_PASSED         = PREFIX + "01011";
  static final String SIGNATURE_CORE_STATUS         = PREFIX + "01012";
  static final String SIGNATURE_REFRENCE_STATUS     = PREFIX + "01013";
  static final String SIGNATURE_STATUS_VALID        = PREFIX + "01014";
  static final String SIGNATURE_STATUS_INVALID      = PREFIX + "01015";
  static final String SIGNATURE_STATUS_VALIDATED    = PREFIX + "01016";
  static final String SIGNATURE_STATUS_NOTVALIDATED = PREFIX + "01017";
}