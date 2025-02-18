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

    System      :   Foundation Shared Library
    Subsystem   :   Common shared naming facilities

    File        :   LocatorError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    LocatorError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.naming;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemError;

////////////////////////////////////////////////////////////////////////////////
// interface LocatorError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface LocatorError extends SystemError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00061 - 00070 naming and lookup related errors
  static final String LOCATOR_INITIALIZE     = SystemConstant.PREFIX + "00061";
  static final String CONTEXT_CONNECTION     = SystemConstant.PREFIX + "00062";
  static final String CONTEXT_INITIALIZE     = SystemConstant.PREFIX + "00063";
  static final String CONTEXT_CLOSE          = SystemConstant.PREFIX + "00064";
  static final String CONTEXT_ENVIRONMENT    = SystemConstant.PREFIX + "00065";
  static final String OBJECT_LOOKUP          = SystemConstant.PREFIX + "00066";
  static final String OBJECT_CREATION        = SystemConstant.PREFIX + "00067";
  static final String OBJECT_ACCESS          = SystemConstant.PREFIX + "00068";
}