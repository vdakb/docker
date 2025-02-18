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

    Copyright © 2021. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ModelContextListener.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ModelContextListener.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven;

import oracle.ide.Context;

import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;

////////////////////////////////////////////////////////////////////////////////
// abstract class ModelContextListener
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to add menu
 ** items and submenus to the context menu.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class ModelContextListener implements ContextMenuListener {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ModelContextListener</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ModelContextListener() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   menuWillHide (ContextMenuListener)
  /**
   ** This method is called just before a showing context menu is dismissed.
   ** <p>
   ** Most implementations should not do anything in this method. In particular,
   ** it is not necessary to clean out menu items or submenus that were added
   ** during.
   **
   ** @param  contextMenu         the context menu being hidden.
   */
  @Override
  public void menuWillHide(final ContextMenu contextMenu) {
    // most context menu listeners will do nothing in this method. In
    // particular, you should *not* remove menu items in this method.
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleDefaultAction (ContextMenuListener)
  /**
   ** Called when the user double clicks on an item that has a context menu.
   ** <p>
   ** If the listener handles the action, then it must return <code>true</code>;
   ** otherwise it must return <code>false</code>. Processing of
   ** handleDefaultAction stops on the first return of <code>true</code>.
   **
   ** @param  context             the context on which the default action needs
   **                             to occur.
   **
   ** @return                     always <code>false</code> for our purpose.
   */
  @Override
  public boolean handleDefaultAction(final Context context) {
    // You can implement this method if you want to handle the default
    // action (usually double click) for some context.
    return false;
  }
}