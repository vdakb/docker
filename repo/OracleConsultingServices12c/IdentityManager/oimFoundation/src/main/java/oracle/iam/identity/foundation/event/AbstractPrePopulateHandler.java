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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Provisioning Facilities

    File        :   AbstractPrePopulateHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractPrePopulateHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.event;

import oracle.iam.request.plugins.PrePopulationAdapter;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractPrePopulateHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractPrePopulateHandler</code> provide the basic
 ** implementation of common tasks a pre-populate adapter needs.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractPrePopulateHandler extends    AbstractEventHandler
                                                 implements PrePopulationAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the plugin point of Pre-Populate adapters to lookup the metadata */
  protected static final String EVENT = "oracle.iam.request.plugins.PrePopulationAdapter";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractPrePopulateHandler</code> which use the
   ** default category for logging purpose.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractPrePopulateHandler() {
    // ensure inheritance
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractPrePopulateHandler</code> which use the
   ** specified category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractPrePopulateHandler(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }
}