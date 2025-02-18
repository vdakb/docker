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

    File        :   MetadataResourceController.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataResourceController.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.mds;

import java.util.Iterator;

import oracle.adf.rc.core.CatalogContext;

import oracle.adf.rc.resource.Resource;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.Ide;
import oracle.ide.Context;
import oracle.ide.IdeConstants;

import oracle.ide.net.URLFileSystem;

import oracle.ide.model.Node;
import oracle.ide.model.Element;

import oracle.ide.controller.Command;
import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;

import oracle.jdevimpl.rescat2.dockable.RepositoriesView;

import oracle.jdeveloper.rescat2.model.nodes.GenericNode;
import oracle.jdeveloper.rescat2.model.nodes.DirContextNode;
import oracle.jdeveloper.rescat2.model.nodes.RepositoryRootNode;
import oracle.jdeveloper.rescat2.model.nodes.ConnectionTypeNode;
import oracle.jdeveloper.rescat2.model.nodes.ResourceNodeFactory;
import oracle.jdeveloper.rescat2.model.nodes.RepositoriesRootNode;

import oracle.jdeveloper.connection.iam.adapter.MetadataType;
import oracle.jdeveloper.connection.iam.adapter.MetadataPackage;
import oracle.jdeveloper.connection.iam.adapter.MetadataDocument;

import oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.mds.command.ResourcePurge;
import oracle.jdeveloper.connection.iam.editor.mds.command.ResourceCreate;
import oracle.jdeveloper.connection.iam.editor.mds.command.ResourceDelete;
import oracle.jdeveloper.connection.iam.editor.mds.command.ResourceExport;
import oracle.jdeveloper.connection.iam.editor.mds.command.ResourceImport;
import oracle.jdeveloper.connection.iam.editor.mds.command.ResourceUpdate;
import oracle.jdeveloper.connection.iam.editor.mds.command.ResourceRename;
import oracle.jdeveloper.connection.iam.editor.mds.command.ResourceVersion;
import oracle.jdeveloper.connection.iam.editor.mds.command.ConnectionClose;

////////////////////////////////////////////////////////////////////////////////
// class MetadataResourceController
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to add menu
 ** items and submenus to the context menu.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class MetadataResourceController implements Controller
                                        ,          ContextMenuListener {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MetadataResourceController</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MetadataResourceController() {
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

    final Element element = context.getElement();
    if (action.getCommandId() == IdeConstants.DELETE_CMD_ID) {
      if (element instanceof RepositoryRootNode) {
        final RepositoryRootNode rrn = (RepositoryRootNode)element;
        if (rrn.getConnectionType() instanceof MetadataType) {
          // close the connection first because no access to it exists later on
          // at this point it is not important if the deletion of the catalog
          // element will succeed
          // if the connection cannot be deleted and still usable in the UI the
          // service implementation will automatically reconnect with a short
          // delay due to the closed socket
          ConnectionClose.execute((MetadataPackage)rrn.getCatalogContext());
          // latch the parent node to be able to refresh this node later
          final GenericNode parent = (GenericNode)rrn.getParent();
          if (Command.OK == ResourceDelete.execute(rrn.getConnectionId())) {
            if (parent != null) {
              parent.forceRefresh();
            }
          }
          return true;
        }
      }
      else if (metadataPackage(element)) {
        final DirContextNode dcn = (DirContextNode)element;
        // latch the parent node to be able to refresh this node later
        final GenericNode     parent  = (GenericNode)dcn.getParent();
        final MetadataPackage subject = resourcePackage(dcn);
        if (Command.OK == ResourceDelete.execute(subject)) {
          if (parent != null) {
            parent.forceRefresh();
          }
          MessageDialog.information(Ide.getMainWindow(), Bundle.format(Bundle.RESOURCE_DELETE_SUCCESS, subject.name().getAbsoluteName()), Bundle.string(Bundle.RESOURCE_DELETE_TITLE),  null);
        }
        return true;
      }
      else if (metadataDocument(element)) {
        final MetadataDocument doc = resourceDocument(element);
        if (Command.OK == ResourceDelete.execute(doc)) {
          refreshParentNode(context, (Node)element, doc.getRepositoryReference().getRepositoryId());
          MessageDialog.information(Ide.getMainWindow(), Bundle.format(Bundle.RESOURCE_DELETE_SUCCESS, doc.name().getAbsoluteName()), Bundle.string(Bundle.RESOURCE_DELETE_TITLE),  null);
        }
        return true;
      }
    }
    else if (action.getCommandId() == ConnectionClose.action()) {
      if (element instanceof RepositoryRootNode) {
        final RepositoryRootNode rrn = (RepositoryRootNode)element;
        if ((rrn.getCatalogContext() instanceof MetadataPackage)) {
          ConnectionClose.execute((MetadataPackage)rrn.getCatalogContext());
          return true;
        }
      }
    }
    else if (action.getCommandId() == ResourceCreate.action()) {
      if (metadataPackage(element)) {
        final DirContextNode dcn = (DirContextNode)element;
        ResourceCreate.execute(resourcePackage(dcn));
        dcn.forceRefresh();
        return true;
      }
    }
    else if (action.getCommandId() == ResourceExport.action()) {
      if (metadataDocument(element)) {
        ResourceExport.execute(resourceDocument(element));
        return true;
      }
    }
    else if (action.getCommandId() == ResourceImport.action()) {
      if (metadataPackage(element)) {
        final DirContextNode dcn = (DirContextNode)element;
        ResourceImport.execute(resourcePackage(dcn));
        dcn.forceRefresh();
        return true;
      }
    }
    else if (action.getCommandId() == ResourceUpdate.action()) {
      if (metadataDocument(element)) {
        ResourceUpdate.execute(resourceDocument(element));
        return true;
      }
    }
    else if (action.getCommandId() == ResourceRename.action()) {
      if (metadataDocument(element)) {
        final MetadataDocument doc = resourceDocument(element);
        if (Command.OK == ResourceRename.execute(doc)) {
          refreshParentNode(context, (Node)element, doc.getRepositoryReference().getRepositoryId());
        }
        return true;
      }
    }
    else if (action.getCommandId() == ResourceVersion.action()) {
      if (metadataDocument(element)) {
        ResourceVersion.execute(resourceDocument(element));
        return true;
      }
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
    final int cmd = action.getCommandId();
    final Element element = context.getElement();
    if (cmd == IdeConstants.DELETE_CMD_ID) {
      if (metadataPackage(element)) {
        action.setEnabled(MetadataPackage.deleteable(resourcePackage(element).name()));
        return true;
      }
      else if (metadataDocument(element)) {
        action.setEnabled(MetadataPackage.deleteable(resourceDocument(element).name()));
        return true;
      }
    }
    else if (cmd == ResourceExport.action()) {
      return true;
    }
    else if (cmd == ResourceImport.action()) {
      return true;
    }
    else if (cmd == ResourceUpdate.action()) {
      return true;
    }
    else if (cmd == ResourceRename.action()) {
      return true;
    }
    else if (cmd == ResourceVersion.action()) {
      return true;
    }
    else if (cmd == ResourcePurge.action()) {
      return true;
    }
    else if (cmd == ConnectionClose.action()) {
      if (element instanceof RepositoryRootNode) {
        final RepositoryRootNode rrn = (RepositoryRootNode)element;
        if ((rrn.getCatalogContext() instanceof MetadataPackage)) {
          action.setEnabled(resourcePackage(element).provider().established());
        }
        return true;
      }
    }
    else if (cmd == ResourceCreate.action()) {
      if (metadataPackage(element)) {
        action.setEnabled(true);
        return true;
      }
    }
    return false;
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
        if (((RepositoryRootNode)element).getConnectionType() instanceof MetadataType) {
          contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ResourceCreate.action())), 20F);
          contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ResourcePurge.action())),  20F);
          contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ConnectionClose.action())));
        }
      }
      else if (MetadataResourceController.metadataPackage(element)) {
        contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ResourceCreate.action())), 20F);
        contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ResourceImport.action())), 20F);
      }
      else if (MetadataResourceController.metadataDocument(element)) {
        contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ResourceExport.action())),  20F);
        contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ResourceUpdate.action())),  20F);
        contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ResourceRename.action())),  20F);
        contextMenu.add(contextMenu.createMenuItem(IdeAction.find(ResourceVersion.action())), 20F);
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
  // Method:   metadataPackage
  /**
   ** Whether the specified {@link Element} is an instance of
   ** {@link MetadataPackage}.
   **
   ** @param  element            the {@link Element} to verify.
   **
   ** @return                    <code>true</code> the specified {@link Element}
   **                            is an instance of {@link MetadataPackage};
   **                            otherwise <code>false</code>.
   */
  static boolean metadataPackage(final Element element) {
    if (!(element instanceof DirContextNode))
      return false;
    final DirContextNode dcn = (DirContextNode)element;
    final CatalogContext ctx = dcn.getCatalogContext();
    return (ctx instanceof MetadataPackage);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadataDocument
  /**
   ** Whether the specified {@link Element} is an instance of
   ** {@link MetadataDocument}.
   **
   ** @param  element            the {@link Element} to verify.
   **
   ** @return                    <code>true</code> the specified {@link Element}
   **                            is an instance of {@link MetadataDocument};
   **                            otherwise <code>false</code>.
   */
  static boolean metadataDocument(final Element element) {
    if (!(element instanceof Node))
      return false;

    final Resource resource = ResourceNodeFactory.getInstance().resolveURL(((Node)element).getURL());
    return ((resource != null) && ((resource instanceof MetadataDocument)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourcePackage
  static MetadataPackage resourcePackage(final Element element) {
    return (MetadataPackage)((DirContextNode)element).getCatalogContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceDocument
  static MetadataDocument resourceDocument(final Element element) {
    return (MetadataDocument)ResourceNodeFactory.getInstance().resolveURL(((Node)element).getURL());
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
    IdeAction.addController(IdeConstants.DELETE_CMD_ID, this);
    // register this controller on additional actions to be able to create
    // a folder
    IdeAction.addController(ResourceCreate.action(), this);
    // register this controller on additional actions to be able to export
    // a certain document
    IdeAction.addController(ResourceExport.action(), this);
    // register this controller on additional actions to be able to import
    // a certain document
    IdeAction.addController(ResourceImport.action(), this);
    // register this controller on additional actions to be able to update
    // a certain document
    IdeAction.addController(ResourceUpdate.action(), this);
    // register this controller on additional actions to be able to rename
    // a certain document
    IdeAction.addController(ResourceRename.action(), this);
    // register this controller on additional actions to be able to obtain
    // versions of a certain document
    IdeAction.addController(ResourceVersion.action(), this);
    // register this controller on additional actions to be able to purge a
    // repository
    IdeAction.addController(ResourcePurge.action(), this);
    // register this controller on additional actions to be able to close an
    // opened connection
    IdeAction.addController(ConnectionClose.action(), this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshParentNode
  /**
   ** Refresh the parent context of a navigaable node after action.
   **
   ** @param  context            the IDE context of the Resource Catalog.
   ** @param  documentNode       the actual context the event occured.
   ** @param  connectionName     the name of the connection the
   **                            <code>entryNode</code> belongs top.
   */
  private void refreshParentNode(final Context context, final Node documentNode, final String connectionName) {
    final String      path    = URLFileSystem.getParent(documentNode.getURL()).getPath();
    final GenericNode root    = RepositoriesRootNode.getFromContext(context);
    final Iterator    palette = root.getChildren();
    while (palette.hasNext()) {
      final Element element = (Element)palette.next();
      if (element instanceof ConnectionTypeNode && ((ConnectionTypeNode)element).getConnectionType() instanceof MetadataType) {
        final Iterator cursor = element.getChildren();
        while (cursor.hasNext()) {
          final Element connection = (Element)cursor.next();
          if (connection.getLongLabel().equals(connectionName)) {
            final DirContextNode parent = packageNode((DirContextNode)connection, path.substring(0, path.length() - 1));
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
  // Method:   packageNode
  private DirContextNode packageNode(final DirContextNode parent, final String path) {
    DirContextNode node  = null;
    final Iterator nodes = parent.getChildren();
    while (nodes.hasNext()) {
      final Element element = (Element)nodes.next();
      if ((element instanceof DirContextNode)) {
        DirContextNode currentNode = (DirContextNode)element;
        if (currentNode.getURL().getPath().equals(path)) {
          return currentNode;
        }
        node = packageNode(currentNode, path);
        if (node != null) {
          return node;
        }
      }
    }
    return null;
  }
}