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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   IdentityServerController.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityServerController.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.navigator;

import oracle.ide.Context;

import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerController
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to add menu
 ** items and submenus to the context menu.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityServerController implements Controller
                                      ,          ContextMenuListener {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityServerController</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityServerController() {
    // ensure inheritance
    super();

    // initialize instance attributes
    IdeAction.find(IdentityNavigatorManager.create()).addController(this);
    IdeAction.find(IdentityNavigatorManager.create()).addController(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   menuWillShow (ContextMenuListener)
  /**
   ** This method is called just before a context menu is shown.
   ** <p>
   ** Implementations should add their items to the context menu here.
   **
   ** @param  contextMenu         the context menu being shown.
   */
  @Override
  public void menuWillShow(final ContextMenu contextMenu) {
    final Context context  = contextMenu.getContext();
  }

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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleEvent (Controller)
  /**
   ** Handle the events that this controller is asked to handle.
   **
   ** @param  action             the action that is to be executed
   ** @param  context            where was the user when the action was signaled
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise
   */
  @Override
  public boolean handleEvent(final IdeAction action, final Context context) {
    // first, bounce bogus input
    if (action == null || context == null)
      return true;

    final int id = action.getCommandId();
    if (id == IdentityNavigatorManager.create()) {
      return true;
    }
    else if (id == IdentityNavigatorManager.close()) {
      return true;
    }

    // all commands are handled by the specific command itself
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (Controller)
  /**
   ** This method updates the enabled status of the specified action within the
   ** specified context.
   **
   ** @param  action             the action that is to be executed
   ** @param  context            where was the user when the action was signaled
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise
   */
  @Override
  public boolean update(final IdeAction action, final Context context) {
    // first, bounce bogus input
    if (action == null || context == null)
      return true;

    // Now check to see what the command is
    final int id = action.getCommandId();
    if (id == IdentityNavigatorManager.create()) {
      action.setEnabled(true);
      return true;
    }
    else if (id == IdentityNavigatorManager.close()) {
      action.setEnabled(true);
      return true;
    }

    // all updates are handled by the specific command itself
    return false;
  }
}