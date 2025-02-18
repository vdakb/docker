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

    File        :   IdentityNavigatorController.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityNavigatorController.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.navigator;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.Ide;
import oracle.ide.Context;
import oracle.ide.IdeConstants;

import oracle.ide.model.Element;

import oracle.ide.view.View;

import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;

import oracle.ide.model.Node;
import oracle.ide.navigator.NavigatorWindow;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointElement;

import oracle.jdeveloper.connection.iam.navigator.ods.DirectoryNavigatorAction;
import oracle.jdeveloper.connection.iam.navigator.ods.DirectoryServiceBase;
import oracle.jdeveloper.connection.iam.navigator.ods.DirectoryServiceRoot;
import oracle.jdeveloper.connection.oim.Bundle;

import oracle.jdeveloper.connection.oim.wizard.IdentityServerWizard;

////////////////////////////////////////////////////////////////////////////////
// class IdentityNavigatorController
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to add
 ** navigation panels.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityNavigatorController implements Controller
                                         ,          ContextMenuListener {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityNavigatorController</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityNavigatorController() {
    // ensure inheritance
    super();

    // register this controller to be able to modify elements
    IdeAction.get(IdeConstants.PROPERTIES_CMD_ID).addController(this);
    // register this controller to be able to create a new connection
    IdeAction.get(IdentityNavigatorManager.create()).addController(this);
    // register this controller to be able to modify connection properties
    IdeAction.get(IdentityNavigatorManager.modify()).addController(this);
    // register this controller on additional actions to be able to refresh
    // elements
    IdeAction.get(IdeConstants.REFRESH_CMD_ID).addController(this);
    // register this controller on additional actions to be able to delete
    // elements
    IdeAction.get(IdeConstants.DELETE_CMD_ID).addController(this);
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
   ** @param  menu               the context menu being shown.
   */
  @Override
  public void menuWillShow(final ContextMenu menu) {
    // add a menu to the context menu only when user clicked on Identity Server
    // Node class.
    final Context context = menu.getContext();
    final View    view    = context.getView();
    if (!navigator(view)) {
      return;
    }

    // if the current element match the element type managed by the navigator
    // remove all default menu items
    menu.removeAll();

    final Element element = context.getElement();
    // if the current element is the root model of the connection tree add the
    // menu item to create a new Identity Service connection
    if (element instanceof IdentityNavigatorRoot) {
      menu.add(menu.createMenuItem(IdeAction.get(IdeConstants.REFRESH_CMD_ID)), 00.0F);
      menu.addSeparator();
      menu.add(menu.createMenuItem(IdeAction.get(IdentityNavigatorManager.create())), 10.0F);
    }
    if (element instanceof IdentityServiceRoot) {
      menu.add(menu.createMenuItem(IdeAction.get(IdentityNavigatorManager.close())), 00.0F);
      menu.add(menu.createMenuItem(IdeAction.get(IdeConstants.DELETE_CMD_ID)),       00.0F);
      menu.addSeparator();
      menu.add(menu.createMenuItem(IdeAction.get(IdeConstants.PROPERTIES_CMD_ID)),   10.0F);
    }
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
   ** @param  contextMenu        the context menu being hidden.
   **                            <br>
   **                            Allowed object is {@link ContextMenu}.
   */
  @Override
  public void menuWillHide(final ContextMenu contextMenu) {
    // most context menu listeners will do nothing in this method. In
    // particular, you should *not* remove menu items in this method.
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleEvent (Controller)
  /**
   ** Handle the events that this controller is asked to handle
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

    // second, bounce bogus state
    if (!navigator(context.getView()))
      return false;

    // This is how to override standard IDE actions. Because the controller is
    // associated with our view, we are only overriding the behavior when ours
    // is the active view. Our controller has no effect on the action when
    // invoked in other views.
    final int cmd = action.getCommandId();
    if (cmd == IdeConstants.PROPERTIES_CMD_ID) {
      final Element[] selection = context.getSelection();
      if ((selection.length == 1) && ((selection[0] instanceof Manageable.Modifiable))) {
        final Manageable.Modifiable modifiable = (Manageable.Modifiable)selection[0];
        if (modifiable.modify()) {
          // do some action after modify
        }
      }
      return true;
    }
    else if (cmd == IdeConstants.REFRESH_CMD_ID) {
      final Manageable.Refreshable refreshable = (Manageable.Refreshable)context.getElement();
      refreshable.refresh();
    }
    else if (cmd == IdeConstants.DELETE_CMD_ID) {
      final Element[] selection = context.getSelection();
      if ((selection.length == 1) && ((selection[0] instanceof Manageable.Removeable))) {
        final Manageable.Removeable removeable = (Manageable.Removeable)selection[0];
        if (MessageDialog.confirm(Ide.getMainWindow(), Bundle.format(Bundle.IDENTITY_DELETE_SINGLE, selection[0].getShortLabel()), Bundle.string(Bundle.IDENTITY_DELETE_TITLE), null)) {
          removeable.remove();
        }
      }
      else if (selection.length > 0) {
        if (MessageDialog.confirm(Ide.getMainWindow(), Bundle.string(Bundle.IDENTITY_DELETE_MULTIPLE), Bundle.string(Bundle.IDENTITY_DELETE_TITLE), null)) {
          for (Element e : selection) {
            if ((e instanceof Manageable.Removeable)) {
              final Manageable.Removeable removeable = (Manageable.Removeable)e;
              removeable.remove();
              ((EndpointElement)e).delete();
            }
          }
        }
      }
      return true;
    }
    else if (IdentityNavigatorManager.create() == cmd) {
      final Element element = context.getElement();
      // at the navigator root a new connection might be created
      if (element instanceof IdentityNavigatorRoot) {
        if (IdentityServerWizard.create()) {
          ((IdentityNavigatorRoot)element).reload();
          ((IdentityNavigatorRoot)element).fireStructureChanged();
        }
      }
      // at the service root a new suffix might be created
      return true;
    }
    else if (IdentityNavigatorManager.modify() == cmd) {
      final Element element = context.getElement();
      // at the navigator root a new connection might be created
      if (element instanceof Manageable.Modifiable) {
        ((Manageable.Modifiable)element).modify();
      }
      // at the service root a new suffix might be created
      return true;
    }
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

    // Because the controller is associated with our view, we are only
    // overriding the behavior when ours is the active view. Our controller has
    // no effect on the action when invoked in other views.
    if (!navigator(context.getView()))
      return false;

    final Element element = context.getElement();
    if (element == null)
      return false;

    // this is how to override standard IDE actions.
    switch(action.getCommandId()) {
      case IdeConstants.REFRESH_CMD_ID    : action.setEnabled(element instanceof Manageable.Refreshable);
                                            return true;
      // can delete anything except the root node
      case IdeConstants.DELETE_CMD_ID     : action.setEnabled(element instanceof Manageable.Removeable);
                                            return true;
      // can properties modified
      case IdeConstants.PROPERTIES_CMD_ID : action.setEnabled(element instanceof Manageable.Modifiable);
                                            return true;
      default                             : return false;
    }
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
   ** @param  context            the context on which the default action needs
   **                            to occur.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean handleDefaultAction(final Context context) {
    // first, bounce bogus input
    if (context == null)
      return false;

    // second, bounce bogus state
    if (!navigator(context.getView()))
      return false;

    final Node element = context.getNode();
    if ((element instanceof DirectoryServiceBase) || (element instanceof DirectoryServiceRoot))
      return handleEvent(DirectoryNavigatorAction.MODIFY.find(), context);
    else
      return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigator
  /**
   ** Verifies if the specified {@link View} belongs to this navigator.
   **
   ** @param  view               the {@link View} that originates to the context
   **                            event.
   **
   ** @return                    <code>true</code> if <code>view</code> belongs
   **                            to this navigator; otherwise
   **                            <code>false</code>.
   */
  private boolean navigator(final View view) {
    if ((view instanceof NavigatorWindow)) {
      final NavigatorWindow  window  = (NavigatorWindow)view;
      return window.getManager() == IdentityNavigatorManager.instance();
    }
    return false;
  }
}
