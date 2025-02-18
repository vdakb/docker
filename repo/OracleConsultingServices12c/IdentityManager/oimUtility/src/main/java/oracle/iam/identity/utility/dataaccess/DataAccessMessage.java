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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Data Access Facilities

    File        :   DataAccessMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DataAccessMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

////////////////////////////////////////////////////////////////////////////////
// interface DataAccessMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface DataAccessMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Constant used to demarcate method entry/exit */
  static final String METHOD_ENTRY      = "entry";
  static final String METHOD_EXIT       = "exit";
  static final String METHOD_EXECUTE    = "execute";
  static final String METHOD_INITIALIZE = "init";

  /** the default message prefix. */
  static final String PREFIX            = "DAM-";

  static final String MESSAGE           = PREFIX + "00000";

  static final String USER              = PREFIX + "00072";
  static final String GROUP             = PREFIX + "00073";
  static final String OBJECT            = PREFIX + "00071";
  static final String PROCESSDEFINITION = PREFIX + "00074";
  static final String PROCESSINSTANCE   = PREFIX + "00075";
  static final String FORMDEFINITION    = PREFIX + "00076";
  static final String FORMINSTANCE      = PREFIX + "00077";

  static final String KEY_TORESOLVE     = PREFIX + "00090";
  static final String KEY_RESOLVED      = PREFIX + "00091";
  static final String NAME_TORESOLVE    = PREFIX + "00092";
  static final String NAME_RESOLVED     = PREFIX + "00093";
}
