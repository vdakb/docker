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
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryResourceController.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryResourceController.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods;

import java.util.Iterator;

import oracle.adf.rc.core.CatalogContext;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.net.URLFileSystem;

import oracle.ide.model.Node;
import oracle.ide.model.Element;

import oracle.ide.controller.Command;
import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.jdevimpl.rescat2.dockable.RepositoriesView;

import oracle.jdeveloper.rescat2.model.nodes.GenericNode;
import oracle.jdeveloper.rescat2.model.nodes.DirContextNode;
import oracle.jdeveloper.rescat2.model.nodes.RepositoryRootNode;
import oracle.jdeveloper.rescat2.model.nodes.ConnectionTypeNode;
import oracle.jdeveloper.rescat2.model.nodes.ResourceNodeFactory;
import oracle.jdeveloper.rescat2.model.nodes.RepositoriesRootNode;

import oracle.jdeveloper.connection.iam.adapter.DirectoryType;
import oracle.jdeveloper.connection.iam.adapter.DirectoryNode;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.navigator.ods.DirectoryNavigatorAction;

import oracle.jdeveloper.connection.iam.wizard.DirectoryServerDialog;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryResourceController
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to add menu
 ** items and submenus to the context menu.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryResourceController implements Controller
                                         ,          ContextMenuListener {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryResourceController</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryResourceController() {
    // ensure inheritance
    super();

    // initialize instance
    registerAction();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleEvent (Controller)
  /**
   ** Handle the events that this controller is asked to handle.
   **
   ** @param  action             the action that is to be executed
   ** @param  context            where was the user when the action was
   **                            signaled.
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean handleEvent(final IdeAction action, final Context context) {
    // first, bounce bogus input
    if (action == null || context == null)
      return true;

    // This is how to override standard IDE actions. Because the controller is
    // associated with our view, we are only overriding the behavior when ours
    // is the active view. Our controller has no effect on the action when
    // invoked in other views.
    final DirectoryNavigatorAction command = DirectoryNavigatorAction.from(action.getCommandId());
    switch (command) {
      case CREATE : return dispatchCreate(context);
      case DELETE : return dispatchDelete(context);
      case RENAME : return dispatchRename(context);
      default     : return false;
    }
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
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean update(final IdeAction action, final Context context) {
    // first, bounce bogus input
    if (action == null || context == null)
      return true;

    // This is how to override standard IDE actions. Because the controller is
    // associated with our view, we are only overriding the behavior when ours
    // is the active view. Our controller has no effect on the action when
    // invoked in other views.
    final Element                  element = context.getElement();
    final DirectoryNavigatorAction command = DirectoryNavigatorAction.from(action.getCommandId());
    switch (command) {
      case CREATE :
      case DELETE : if (directoryEntry(element)) {
                      action.setEnabled(true);
                      return true;
                    }
                    return true;
      case RENAME : return true;
      case MOVE   : return true;
      default     : return false;
    }
  }

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
    final Context context = contextMenu.getContext();
    // add a menu to the context menu only when user clicked on Metadata Package
    // Node classes.
    if (context.getView() instanceof RepositoriesView) {
      final Element[] selections = context.getSelection();
      if ((selections == null) || (selections.length != 1)) {
        return;
      }

      final Element element = context.getElement();
      if (element instanceof RepositoryRootNode) {
        if (((RepositoryRootNode)element).getConnectionType() instanceof DirectoryType) {
          contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.CREATE.find()));
        }
      }
      else if (DirectoryResourceController.directoryEntry(element)) {
        contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.CREATE.find()));
        contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.MODIFY.find()));
        contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.RENAME.find()));
        contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.MOVE.find()));
      }
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   directoryEntry
  /**
   ** Whether the specified {@link Element} is an instance of
   ** {@link DirectoryContext}.
   **
   ** @param  element            the {@link Element} to verify.
   **
   ** @return                    <code>true</code> the specified {@link Element}
   **                            is an instance of
   **                            {@link DirectoryContext}; otherwise
   **                            <code>false</code>.
   */
  static boolean directoryEntry(final Element element) {
    if (!(element instanceof DirContextNode))
      return false;

    final DirContextNode dcn = (DirContextNode)element;
    final CatalogContext ctx = dcn.getCatalogContext();
    return (ctx instanceof DirectoryNode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceEntry
  static DirectoryNode resourceEntry(final Element element) {
    return (DirectoryNode)ResourceNodeFactory.getInstance().resolveURL(((Node)element).getURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchCreate
  /**
   ** Performs all actions required to create a new entry in the context of the
   ** current element.
   ** <br>
   ** An element can be the navigator itself which leads to create a new
   ** connection or a node visible in the navigator tree.
   **
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchCreate(final Context context) {
    final Element element = context.getElement();
    if (directoryEntry(element)) {
      final DirContextNode dcn = (DirContextNode)element;
//      DirectoryContextWizard.create(context, resourceEntry(dcn));
      dcn.forceRefresh();
      return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchDelete
  /**
   ** Performs all actions required to delete an existing entry in the context
   ** of the current element.
   ** <br>
   ** An element can be the navigator itself which leads to delete the
   ** connection or a node visible in the navigator tree.
   **
   ** @param  Context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchDelete(final Context context) {
    final Element element = context.getElement();
    if (element instanceof RepositoryRootNode) {
      final RepositoryRootNode rrn = (RepositoryRootNode)element;
      if (rrn.getConnectionType() instanceof DirectoryType) {
        // latch the parent node to be able to refresh this node later
        final GenericNode parent = (GenericNode)rrn.getParent();
        if (Command.OK == DirectoryServerDialog.delete(rrn.getConnectionId())) {
          final DirectoryNode subject = resourceEntry(rrn);
          try {
            subject.provider().disconnect();
          }
          catch (DirectoryException e) {
            ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, Bundle.format(Bundle.RESOURCE_REMOVE_FAILED, e.getMessage()));              
          }
          if (parent != null) {
            parent.forceRefresh();
          }
        }
        return true;
      }
    }
    else if (directoryEntry(element)) {
      final DirContextNode dcn = (DirContextNode)element;
      // latch the parent node to be able to refresh this node later
      final GenericNode   parent  = (GenericNode)dcn.getParent();
      final DirectoryNode subject = resourceEntry(dcn);
//      if (Command.OK == DirectoryContextDialog.delete(subject)) {
//        if (parent != null) {
//          parent.forceRefresh();
//        }
//        MessageDialog.information(Ide.getMainWindow(), Bundle.format(Bundle.ENTRY_ACTION_DELETE_SUCCESS, subject.name()), Bundle.string(Bundle.ENTRY_ACTION_DELETE_TITLE),  null);
//      }
      return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchRename
  /**
   ** Performs all actions required to rename an entry in the context of the
   ** current element.
   ** <br>
   ** An element can be the navigator itself which leads to rename the
   ** connection or a node visible in the navigator tree.
   **
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchRename(final Context context) {
    final Element element = context.getElement();
    if (directoryEntry(element)) {
      final DirectoryNode entry = resourceEntry(element);
//      if (Command.OK == DirectoryContextDialog.rename(entry)) {
//        refreshParentNode(context, (Node)element, entry.getRepositoryReference().getRepositoryId());
//      }
      return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerAction
  /**
   ** Add to menu actions in IDE.
   ** <p>
   ** The action <code>
   */
  private void registerAction() {
    // register this controller on additional actions to be able to delete
    // a Metadata Document entry
    DirectoryNavigatorAction.DELETE.register(this);
    // register this controller on additional actions to be able to create a
    // entry
    DirectoryNavigatorAction.CREATE.register(this);
    // register this controller on additional actions to be able to create a
    // entry
    DirectoryNavigatorAction.MODIFY.register(this);
    // register this controller on additional actions to be able to rename
    // a certain entry
    DirectoryNavigatorAction.RENAME.register(this);
    // register this controller on additional actions to be able to move
    // a certain entry
    DirectoryNavigatorAction.MOVE.register(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshParentNode
  /**
   ** Refresh the parent context of a navigaable node after action.
   **
   ** @param  context            the IDE context of the Resource Catalog.
   ** @param  entryNode          the actual context the event occured.
   ** @param  connectionName     the name of the connection the
   **                            <code>entryNode</code> belongs top.
   */
  private void refreshParentNode(final Context context, final Node entryNode, final String connectionName) {
    final String      path    = URLFileSystem.getParent(entryNode.getURL()).getPath();
    final GenericNode root    = RepositoriesRootNode.getFromContext(context);
    final Iterator    palette = root.getChildren();
    while (palette.hasNext()) {
      final Element element = (Element)palette.next();
      if (element instanceof ConnectionTypeNode && ((ConnectionTypeNode)element).getConnectionType() instanceof DirectoryType) {
        final Iterator cursor = element.getChildren();
        while (cursor.hasNext()) {
          final Element connection = (Element)cursor.next();
          if (connection.getLongLabel().equals(connectionName)) {
            final DirContextNode parent = directoryNode((DirContextNode)connection, path.substring(0, path.length() - 1));
            if (parent == null)
              break;
            parent.forceRefresh();
            break;
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   directoryNode
  private DirContextNode directoryNode(final DirContextNode parent, final String path) {
    DirContextNode node  = null;
    final Iterator nodes = parent.getChildren();
    while (nodes.hasNext()) {
      final Element element = (Element)nodes.next();
      if ((element instanceof DirContextNode)) {
        DirContextNode currentNode = (DirContextNode)element;
        if (currentNode.getURL().getPath().equals(path)) {
          return currentNode;
        }
        node = directoryNode(currentNode, path);
        if (node != null) {
          return node;
        }
      }
    }
    return null;
  }
}