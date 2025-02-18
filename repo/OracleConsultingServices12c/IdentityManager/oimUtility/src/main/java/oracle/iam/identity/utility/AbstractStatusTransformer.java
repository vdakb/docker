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

    File        :   AbstractStatusTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractStatusTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-04-29  DSteding    First release version
*/

package oracle.iam.identity.utility;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractStatusTransformer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractStatusTransformer</code> is the base class to transform a
 ** a value representing a status information to the value of Oracle Identity
 ** Manager and vice versa.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractStatusTransformer extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the status value assingnable to a identity or organizational profile in
   ** Oracle Identity Manager to reflect the active state
   */
  protected static String  STATUS_ACTIVE   = "Active";

  /**
   ** the status value assingnable to a identity or organizational profile in
   ** Oracle Identity Manager to reflect the deleted state
   */
  protected static String  STATUS_DELETED  = "Deleted";

  /**
   ** the status value assingnable to <code>Resource Object</code>s in Oracle
   ** Identity Manager to reflect the enabled state.
   */
  protected static String  STATUS_ENABLED  = "Enabled";

  /**
   ** the status value assingnable to a identity or organizational profile and
   ** also to Resource Objects in Oracle Identity Manager to reflect the
   ** disabled state.
   */
  protected static String  STATUS_DISABLED = "Disabled";

  /**
   ** the status value assingnable to <code>Resource Object</code>s in Oracle
   ** Identity Manager to reflect the yes state.
   */
  protected static String  STATUS_YES      = "yes";

  /**
   ** the status value assingnable to <code>Resource Object</code>s in Oracle
   ** Identity Manager to reflect the yes state.
   */
  protected static String  STATUS_Y        = "y";

  /**
   ** the status value assingnable to <code>Resource Object</code>s in Oracle
   ** Identity Manager to reflect the no state.
   */
  protected static String  STATUS_NO      = "no";

  /**
   ** the status value assingnable to <code>Resource Object</code>s in Oracle
   ** Identity Manager to reflect the no state.
   */
  protected static String  STATUS_N        = "n";

  /**
   ** the status value assingnable to <code>Resource Object</code>s in Oracle
   ** Identity Manager to reflect the numeric <code>true</code> state.
   */
  protected static Integer NUMERIC_TRUE    = new Integer(1);

  /**
   ** the status value assingnable to <code>Resource Object</code>s in Oracle
   ** Identity Manager to reflect the numeric <code>false</code> state.
   */
  protected static Integer NUMERIC_FALSE   = new Integer(0);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractStatusTransformer</code> which use the
   ** specified {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   */
  public AbstractStatusTransformer(final Logger logger) {
    // ensure inheritance
    super(logger);
  }
}