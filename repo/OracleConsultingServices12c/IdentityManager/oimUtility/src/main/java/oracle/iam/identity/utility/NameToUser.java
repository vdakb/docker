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

    Copyright © 2006. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Utility Facilities

    File        :   NameToUser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NameToUser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import oracle.hst.foundation.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// class NameToUser
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>NameToUser</code> transforms a given name to the system identifier
 ** of user by looking up the user with the given login name.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
@Deprecated
public class NameToUser extends NameToUserWeak {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>NameToUser</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose.
   */
  public NameToUser(final Logger logger) {
    // ensure inheritance
    super(logger);
  }
}