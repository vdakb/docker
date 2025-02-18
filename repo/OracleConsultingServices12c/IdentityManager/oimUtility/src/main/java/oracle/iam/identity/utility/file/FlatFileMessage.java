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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   FlatFileMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

////////////////////////////////////////////////////////////////////////////////
// interface FlatFileMessage
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public interface FlatFileMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX      = "TXT-";

  // 0000 generic message template
  static final String MESSAGE     = PREFIX + "01000";

  // 01001 - 01010 processing messages
  static final String VALIDATING  = PREFIX + "01001";
  static final String LOADING     = PREFIX + "01002";
  static final String PROCESSING  = PREFIX + "01003";
  static final String COMPLETED   = PREFIX + "01004";
  static final String IDENTICALLY = PREFIX + "01005";
  static final String LINECOUNT   = PREFIX + "01006";
  static final String ENDOFFILE   = PREFIX + "01007";
  static final String EMPTYFILE   = PREFIX + "01008";
  static final String CHUNKSIZE   = PREFIX + "01009";
  static final String CHUNKMERGED = PREFIX + "01010";
}