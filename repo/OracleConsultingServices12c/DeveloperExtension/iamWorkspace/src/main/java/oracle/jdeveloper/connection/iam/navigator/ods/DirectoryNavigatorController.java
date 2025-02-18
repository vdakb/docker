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

    File        :   DirectoryNavigatorController.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryNavigatorController.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.ods;

import java.io.File;

import javax.swing.SwingUtilities;

import oracle.javatools.dialogs.MessageDialog;
import oracle.javatools.dialogs.ExceptionDialog;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.view.View;

import oracle.ide.controls.WaitCursor;

import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;

import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;
import oracle.ide.editor.OpenEditorOptions;

import oracle.ide.model.Node;
import oracle.ide.model.Element;

import oracle.ide.view.ViewEvent;
import oracle.ide.view.ViewListener;

import oracle.ide.util.Namespace;

import oracle.ide.navigator.NavigatorWindow;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.connection.iam.editor.ods.RootEditor;
import oracle.jdeveloper.connection.iam.editor.ods.EntryEditor;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.model.DirectoryEntry;
import oracle.jdeveloper.connection.iam.model.DirectoryName;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointElement;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;
import oracle.jdeveloper.connection.iam.navigator.context.DirectoryContext;

import oracle.jdeveloper.connection.iam.support.LDAPFile;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

import oracle.jdeveloper.connection.iam.wizard.DirectoryServerWizard;
import oracle.jdeveloper.connection.iam.wizard.DirectoryContextPanel;
import oracle.jdeveloper.connection.iam.wizard.DirectoryContextDialog;
import oracle.jdeveloper.connection.iam.wizard.DirectoryContextExport;
import oracle.jdeveloper.connection.iam.wizard.DirectoryContextImport;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryNavigatorController
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to add menu
 ** items and submenus to the context menu.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryNavigatorController implements Controller
                                          ,          ContextMenuListener
                                          ,          ViewListener {

  //////////////////////////////////////////////////////////////////////////////
  // the current editor under control
  //////////////////////////////////////////////////////////////////////////////

  /** The editor opened at the root DSA level */
  private Editor root  = null;

  /** The editor opened at the regular entry level */
  private Editor entry = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryNavigatorController</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryNavigatorController() {
    // ensure inheritance
    super();

    // register this controller on additional actions to be able to search for
    // entries
    DirectoryNavigatorAction.SEARCH.register(this);
    // register this controller on additional actions to be able to create a
    // new connection or entries
    DirectoryNavigatorAction.CREATE.register(this);
    // register this controller on additional actions to be able to create a
    // similar entry
    DirectoryNavigatorAction.LIKELY.register(this);
    // register this controller on additional actions to be able to open an
    // selected element in an appropriate editor
    DirectoryNavigatorAction.OPEN.register(this);
    // register this controller on additional actions to be able to modify
    // connection properties or entries
    DirectoryNavigatorAction.MODIFY.register(this);
    // register this controller on additional actions to be able to rename
    // connection properties or entries
    DirectoryNavigatorAction.RENAME.register(this);
    // register this controller on additional actions to be able to move
    // entries
    DirectoryNavigatorAction.MOVE.register(this);
    // register this controller on additional actions to be able to delete
    // connection properties or entries
    DirectoryNavigatorAction.DELETE.register(this);
    // register this controller on additional actions to be able to export
    // entries
    DirectoryNavigatorAction.EXPORT.register(this);
    // register this controller on additional actions to be able to import
    // entries
    DirectoryNavigatorAction.IMPORT.register(this);
    // register this controller on additional actions to be able to refresh
    // elements
    DirectoryNavigatorAction.REFRESH.register(this);
    // register this controller on additional actions to be able to delete
    // elements
    DirectoryNavigatorAction.DELETE.register(this);
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
   ** @param  contextMenu        the context menu being shown.
   **                            <br>
   **                            Allowed object is {@link ContextMenu}.
   */
  @Override
  public void menuWillShow(final ContextMenu contextMenu) {
    // add a menu to the context menu only when user clicked on Directory Server
    // Node class.
    Context context = contextMenu.getContext();
    View view = context.getView();
    if (!navigator(view)) {
      return;
    }

    // if the current element match the element type managed by the navigator
    // remove all default menu items
    contextMenu.removeAll();

    final Element node = context.getElement();
    // if the current element is the navigator root add the menu item to be able
    // to create a new Directory Service connection
    if ((node instanceof DirectoryNavigatorRoot)) {
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.CREATE.action()),  10.0F);
    }
    // if the current element is a directory service root avoid the menu item to
    // create new element
    else if ((node instanceof DirectoryServiceRoot)) {
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.OPEN.action()),    10.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.LIKELY.action()),  10.0F);
    }
    // if the current element is a regular entry put most of the item on the
    // element
    else if ((node instanceof DirectoryServiceEntry)) {
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.MODIFY.action()),  10.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.CREATE.action()),  10.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.LIKELY.action()),  10.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.RENAME.action()),  20.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.MOVE.action()),    20.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.EXPORT.action()),  40.0F);
    }
    // if the current element is the base model of the avoid the menu item to
    // create similar elements
    else if ((node instanceof DirectoryServiceBase)) {
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.MODIFY.action()),  10.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.CREATE.action()),  10.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.EXPORT.action()),  40.0F);
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.IMPORT.action()),  40.0F);
    }
    // if the current element is searchable add the menu item to be able to
    // apply a search at the current element in a editor
    if ((node instanceof Manageable.Searchable)) {
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.SEARCH.action()),  00.0F);
    }
    // if the current element is deletable add the menu item to be able to
    // delete the current element in a editor
    if ((node instanceof Manageable.Removeable)) {
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.DELETE.action()),  10.0F);
    }
    // if the current element is refreshable add the menu item to be able to
    // refresh the current element in a editor
    if ((node instanceof Manageable.Refreshable)) {
      contextMenu.add(contextMenu.createMenuItem(DirectoryNavigatorAction.REFRESH.action()), 30.0F);
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
   ** @param  action             the action that is to be executed.
   **                            <br>
   **                            Allowed object is {@link IdeAction}.
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  @SuppressWarnings("oracle.jdeveloper.java.comment-period-missing")
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
    final DirectoryNavigatorAction cmd = DirectoryNavigatorAction.from(action.getCommandId());
    switch (cmd) {
      case OPEN    : return dispatchOpen(context);
      case REFRESH : return dispatchRefresh(context);
      case SEARCH  : return dispatchSearch(context);
      case CREATE  : return dispatchCreate(context);
      case LIKELY  : return dispatchSimilar(context);
      case MODIFY  : return dispatchModify(context);
      case RENAME  : return dispatchRename(context);
      case DELETE  : return dispatchDelete(context);
      case MOVE    : return dispatchMove(context);
      case EXPORT  : return dispatchExport(context);
      case IMPORT  : return dispatchImport(context);
      default      : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (Controller)
  /**
   ** This method updates the enabled status of the specified action within the
   ** specified context.
   **
   ** @param  action             the action that state is to be updated.
   **                            <br>
   **                            Allowed object is {@link IdeAction}.
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean update(final IdeAction action, final Context context) {
    // first, bounce bogus input
    if (action == null || context == null)
      return true;

    // second, bounce bogus state
    if (!navigator(context.getView()))
      return false;

    final Element[] selection = context.getSelection();
    // This is how to override standard IDE actions. Because the controller is
    // associated with our view, we are only overriding the behavior when ours
    // is the active view. Our controller has no effect on the action when
    // invoked in other views.
    final DirectoryNavigatorAction cmd = DirectoryNavigatorAction.from(action.getCommandId());
    switch (cmd) {
      case REFRESH  : // refresh is always enabled
                      action.setEnabled(true);
                      return true;
      case CREATE   : // can handle only one entry at a time
                      action.setEnabled(selection != null && selection.length == 1 && (!((selection[0] instanceof DirectoryServiceRoot))));
                      return true;
      case LIKELY   : // can handle only one entry at a time
                      if (selection != null && selection.length == 1)
                        action.setEnabled((selection[0] instanceof DirectoryServiceRoot) || (selection[0] instanceof DirectoryServiceBase));
                      else
                        action.setEnabled(false);
                      return true;
      case DELETE   : // can delete anything except the favorites folder
                      if (selection != null && selection.length > 0) {
                        boolean enabled = false;
                        for (Element e : selection) {
                          if (e instanceof Manageable.Removeable) {
                            enabled = true;
                            break;
                          }
                        }
                        // the selection does not contain the favorites folder
                        action.setEnabled(enabled);
                      }
                      return true;
      case SEARCH   : // can search anything except the favorites folder
                      if (selection != null && selection.length > 0) {
                        action.setEnabled(selection[0] instanceof Manageable.Searchable);
                      }
                      return true;
      case EXPORT   : // can export only at the base folder
                      if (selection != null && selection.length == 1) {
                        action.setEnabled((selection[0] instanceof Manageable.Exportable));
                      }
                      return true;
      case IMPORT   : // can import only at the base folder
                      if (selection != null && selection.length == 1) {
                        action.setEnabled(selection[0] instanceof Manageable.Importable);
                      }
                      return true;
      case OPEN     : // can handle only one entry at a time
                      if (selection != null && selection.length == 1)
                        action.setEnabled(selection[0] instanceof DirectoryServiceRoot);
                      else
                        action.setEnabled(false);
                      return true;
      case MODIFY   :
      case PROPERTY : // can handle only one entry at a time
                      if (selection != null && selection.length == 1)
                        action.setEnabled(!(selection[0] instanceof DirectoryNavigatorRoot));
                      else
                        action.setEnabled(false);
                      return true;
      default       : return false;
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
  // Method:   viewActivated (ViewListener)
  /**
   ** Called when the view is activated.
   ** <br>
   ** The {@link ViewEvent} provides information about the view activated.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link ViewEvent}.
   */
  @Override
  public void viewActivated(final ViewEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewDeactivated (ViewListener)
  /**
   ** Called when the view is deactivated.
   ** <br>
   ** The {@link ViewEvent} provides information about the view deactivated.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link ViewEvent}.
   */
  @Override
  public void viewDeactivated(final ViewEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewClosed (ViewListener)
  /**
   ** Called when the view is closed.
   ** <br>
   ** The {@link ViewEvent} provides information about the view closed.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link ViewEvent}.
   */
  @Override
  public void viewClosed(final ViewEvent event) {
    final View view = event.getView();
    // remove the this as a listener on view events
    event.getView().removeViewListener(this);
    if (view instanceof RootEditor)
      this.root  = null;
    else
      this.entry = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchRefresh
  /**
   ** Performs all actions required to refresh an entry in the context of the
   ** current element.
   ** <br>
   ** An element can be the navigator itself which leads to refresh the
   ** connection or a node visible in the navigator tree.
   **
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchRefresh(final Context context) {
    final Element element = context.getElement();
    if ((element instanceof Manageable.Refreshable)) {
      if (element instanceof DirectoryNavigatorRoot) {
        final Manageable.Refreshable refreshable = (Manageable.Refreshable)element;
        refreshable.refresh();
      }
      else if ((element instanceof DirectoryServiceBase)) {
        // refresh the node in the navigator first
        ((DirectoryServiceBase)element).refresh();
        // refresh the editor to if its open
        if (this.entry != null) {
          final View latch = context.getView();
          context.setView(this.entry);
          this.entry.getController().handleEvent(DirectoryNavigatorAction.REFRESH.action(), context);
          context.setView(latch);
        }
      }
      return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchSearch
  /**
   ** Performs all actions required to search for entries in the context of the
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
  private boolean dispatchSearch(final Context context) {
    return true;
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
    // at the navigator root a new connection might be created
    if (element instanceof DirectoryNavigatorRoot) {
      if (DirectoryServerWizard.create()) {
        ((EndpointElement)element).fireStructureChanged();
      }
    }
    // at any entry a new subentry might be created
    else if (element instanceof DirectoryServiceBase) {
      final DirectoryServiceBase node = (DirectoryServiceBase)element;
      final DirectoryContext     base = node.manageable();
      // initialize transferable context
      final TraversableContext store = new TraversableContext(new Namespace(), 0);
      try {
        store.put(DirectoryContextPanel.CONTEXT, base);
        // lookup the service that represents the root folder to obtain the
        // schema required to pass to the create entry dialog
        store.put(DirectoryContextPanel.SCHEMA, base.schema());
        if (DirectoryContextDialog.create(store)) {
          final DirectoryEntry entry = (DirectoryEntry)store.get(DirectoryContextPanel.ENTRY);
          base.service().entryCreate(entry);
          node.refresh();
          node.fireStructureChanged();
          information(Bundle.string(Bundle.ENTRY_CREATE_TITLE), Bundle.format(Bundle.ENTRY_CREATE_SUCCESS, base.name(), entry.name()));
        }  
      }
      catch (DirectoryException e) {
        final DirectoryEntry entry = (DirectoryEntry)store.get(DirectoryContextPanel.ENTRY);
        critical(Bundle.string(Bundle.ENTRY_CREATE_TITLE), Bundle.format(Bundle.ENTRY_CREATE_FAILED, entry.name().prefix(), base.name(), e.getLocalizedMessage()));
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchSimilar
  /**
   ** Performs all actions required to create a new entry like the selected one
   ** in the context of the current element.
   **
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchSimilar(final Context context) {
    final Element element = context.getElement();
    // at any entry a new subentry might be created
    if (element instanceof DirectoryServiceBase) {
      final DirectoryServiceBase node = (DirectoryServiceBase)element;
      final DirectoryContext     base = node.manageable();
      // initialize transferable context
      final TraversableContext store = new TraversableContext(new Namespace(), 0);
      try {
        store.put(DirectoryContextPanel.CONTEXT, base);
        // lookup the service that represents the root folder to obtain the
        // schema required to pass to the create entry dialog
        store.put(DirectoryContextPanel.SCHEMA, base.schema());
        if (DirectoryContextDialog.similar(store)) {
          final DirectoryEntry entry = (DirectoryEntry)store.get(DirectoryContextPanel.ENTRY);
          base.service().entryCreate(entry);
          node.refresh();
          node.fireStructureChanged();
          information(Bundle.string(Bundle.ENTRY_CREATE_TITLE), Bundle.format(Bundle.ENTRY_CREATE_SUCCESS, base.name(), entry.name()));
        }
      }
      catch (DirectoryException e) {
        critical(Bundle.string(Bundle.ENTRY_CREATE_TITLE), Bundle.format(Bundle.ENTRY_CREATE_FAILED, base.name(), e.getLocalizedMessage()));
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchOpen
  /**
   ** Performs all actions required to open an existing entry in the context
   ** of the current element.
   ** <br>
   ** An element can be the navigator itself which leads to modify the
   ** connection or a node visible in the navigator tree.
   **
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchOpen(final Context context) {
    final Element element = context.getElement();
    // at the navigator root a the connection might be modified
    if (element instanceof DirectoryServiceRoot) {
      if (this.root == null) {
        openRoot(context);
      }
      else {        
        this.root.setContext(context);
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchModify
  /**
   ** Performs all actions required to modify an existing entry in the context
   ** of the current element.
   ** <br>
   ** An element can be the navigator itself which leads to modify the
   ** connection or a node visible in the navigator tree.
   **
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchModify(final Context context) {
    final Element element = context.getElement();
    if (element instanceof DirectoryServiceBase) {
      if (this.entry == null) {
        openEntry(context);
      }
      else {
        this.entry.setContext(context);
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchRename
  /**
   ** Performs all actions required to rename an existing entry in the context
   ** of the current element.
   ** <br>
   ** An element can be the navigator itself which leads to modify the
   ** connection or a node visible in the navigator tree.
   **
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchRename(final Context context) {
    final Element element = context.getElement();
    // only base entries can be renamed
    if (element instanceof DirectoryServiceBase) {
      // initialize transferable context
      final TraversableContext store  = new TraversableContext(new Namespace(), 0);
      // initialize storage attributes
      final DirectoryName      origin = ((DirectoryServiceBase)element).manageable().name();
      store.put(DirectoryContextPanel.NAME, origin);
      if (DirectoryContextDialog.rename(store)) {
        final DirectoryName target = (DirectoryName)store.get(DirectoryContextPanel.NAME);
        // prevent unessecary actions if the name of the entry isn't changed
        if (!origin.equals(target)) {
          try {
            // ensure the we have a valid resource name derived from the
            // specified parameters of this method
            ((DirectoryServiceBase)element).manageable().service().entryRename(origin, target);
            ((EndpointElement)element).fireStateChanged();
            information(Bundle.string(Bundle.ENTRY_RENAME_TITLE), Bundle.format(Bundle.ENTRY_RENAME_SUCCESS, origin, target));
          }
          catch (DirectoryException e) {
            fatal(e);
          }
        }
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchMove
  /**
   ** Performs all actions required to move an existing entry out of the context
   ** of the current element.
   ** <br>
   ** An element can be the navigator itself which leads to modify the
   ** connection or a node visible in the navigator tree.
   **
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchMove(final Context context) {
    final Element element = context.getElement();
    // only base entries can be moved
    if (element instanceof DirectoryServiceBase) {
      // initialize transferable context
      final TraversableContext store  = new TraversableContext(new Namespace(), 0);
      // initialize storage attributes
      final DirectoryName      origin = ((DirectoryServiceBase)element).manageable().name();
      store.put(DirectoryContextPanel.NAME, origin);
      if (DirectoryContextDialog.move(store)) {
        final DirectoryName target = (DirectoryName)store.get(DirectoryContextPanel.NAME);
        // prevent unessecary actions if the name of the entry isn't changed
        if (!origin.equals(target)) {
          try {
            // ensure the we have a valid resource name derived from the specified
            // parameters of this method
            ((DirectoryServiceBase)element).manageable().service().entryRename(origin, target);
            ((EndpointElement)element).fireStructureChanged();
            information(Bundle.string(Bundle.ENTRY_MOVE_TITLE), Bundle.format(Bundle.ENTRY_MOVE_SUCCESS, origin, target));
          }
          catch (DirectoryException e) {
            fatal(e);
          }
        }
      }
    }
    return true;
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
   ** @param  context            where was the user when the action was
   **                            triggered.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private boolean dispatchDelete(final Context context) {
    final Element[] selection = context.getSelection();
    if ((selection.length == 1) && ((selection[0] instanceof Manageable.Removeable))) {
      if (MessageDialog.confirm(Ide.getMainWindow(), Bundle.format(Bundle.ENTRY_DELETE_SINGLE, selection[0].getLongLabel()), Bundle.string(Bundle.ENTRY_DELETE_TITLE), null)) {
        ((Manageable.Removeable)selection[0]).remove();
        return true;
      }
    }
    else if (selection.length > 0) {
      if (MessageDialog.confirm(Ide.getMainWindow(), Bundle.string(Bundle.ENTRY_DELETE_MULTIPLE), Bundle.string(Bundle.ENTRY_DELETE_TITLE), null)) {
        for (Element e : selection) {
          if ((e instanceof Manageable.Removeable)) {
            ((Manageable.Removeable)e).remove();
          }
        }
        return true;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchExport
  /**
   ** Performs all actions required to export entries from the context of the
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
  private boolean dispatchExport(final Context context) {
    final Element element = context.getElement();
    // only base entries can be renamed
    if (element instanceof DirectoryServiceBase) {
      final DirectoryContext base = ((DirectoryServiceBase)element).manageable();
      // initialize transferable context
      final TraversableContext store  = new TraversableContext(new Namespace(), 0);
      // initialize storage attributes
      store.put(DirectoryContextExport.CONTEXT, base);
      store.put(DirectoryContextExport.SUB,     Boolean.TRUE);
      store.put(DirectoryContextExport.OPR,     Boolean.TRUE);
      store.put(DirectoryContextExport.ATT,     Boolean.FALSE);
      store.put(DirectoryContextExport.FMT,     LDAPFile.Format.DSML2.type);
      if (DirectoryContextDialog.marshal(store)) {
        int             count  = 0;
        File            file   = (File)store.get(DirectoryContextExport.FILE);
        try {
          count = base.service().exportContent(base.name(), (File)store.get(DirectoryContextExport.FILE), (String)store.get(DirectoryContextExport.FMT), (Boolean)store.get(DirectoryContextExport.SUB), (Boolean)store.get(DirectoryContextExport.OPR), (Boolean)store.get(DirectoryContextExport.ATT));
          information(Bundle.string(Bundle.EXPORT_TITLE), Bundle.format(Bundle.EXPORT_COMPLETE, String.valueOf(count), file.getAbsolutePath()));
        }
        catch (DirectoryException e) {
          fatal(e);
        }
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatchImport
  /**
   ** Performs all actions required to import entries into the context of the
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
  private boolean dispatchImport(final Context context) {
    final Element element = context.getElement();
    // only base entries can be renamed
    if (element instanceof DirectoryServiceBase) {
      // initialize transferable context
      final TraversableContext store = new TraversableContext(new Namespace(), 0);
      // initialize storage attributes
      store.put(DirectoryContextImport.CONTEXT, ((DirectoryServiceBase)element).manageable());
      store.put(DirectoryContextImport.STOP,    Boolean.TRUE);
      store.put(DirectoryContextImport.FMT,     LDAPFile.Format.DSML2.type);

      if (DirectoryContextDialog.unmarshal(store)) {
        final File file  = (File)store.get(DirectoryContextImport.FILE);
        try {
          int count = ((DirectoryServiceBase)element).manageable().service().importContent(file, (File)store.get(DirectoryContextImport.ERROR), (String)store.get(DirectoryContextImport.FMT), (Boolean)store.get(DirectoryContextImport.STOP));
          ((Manageable.Refreshable)element).refresh();
          information(Bundle.string(Bundle.IMPORT_TITLE), Bundle.format(Bundle.IMPORT_COMPLETE, String.valueOf(count), file.getAbsolutePath()));
        }
        catch (DirectoryException e) {
          fatal(e);
        }
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigator
  /**
   ** Verifies if the specified {@link View} belongs to this navigator.
   **
   ** @param  view               the {@link View} that originates to the context
   **                            event.
   **                            <br>
   **                            Allowed object is {@link View}.
   **
   ** @return                    <code>true</code> if <code>view</code> belongs
   **                            to this navigator; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean navigator(final View view) {
    return ((view instanceof NavigatorWindow)) ? ((NavigatorWindow)view).getManager() == DirectoryNavigatorManager.instance() : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   openRoot
  /**
   ** Opens the specified context using the default registered editor class for
   ** a node type. If the context is already open in another editor, this will
   ** just activate the current editor.
   ** <br>
   ** This method is preferred over the different
   ** openDefaultEditorInFrame(java.net.URL) and
   ** openEditorInFrame(java.lang.Class, oracle.ide.addin.Context) variants
   ** which will eventually be deprecated.
   **
   ** @param  context            the context describing the node to open.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private void openRoot(final Context context) {
    if (this.root != null && this.root.getEditorAddin().getEditorClass() == RootEditor.class)
      return;

    final OpenEditorOptions option = new OpenEditorOptions(context, RootEditor.class);
    option.setFlags(OpenEditorOptions.RAISE);
    SwingUtilities.invokeLater(
      new Runnable() {
        @Override
        public void run() {
          DirectoryNavigatorController.this.root = openEditor(option);
        }
      }
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   openEntry
  /**
   ** Opens the specified context using the default registered editor class for
   ** a node type. If the context is already open in another editor, this will
   ** just activate the current editor.
   ** <br>
   ** This method is preferred over the different
   ** openDefaultEditorInFrame(java.net.URL) and
   ** openEditorInFrame(java.lang.Class, oracle.ide.addin.Context) variants
   ** which will eventually be deprecated.
   **
   ** @param  context            the context describing the node to open.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private void openEntry(final Context context) {
    if (this.entry != null && this.entry.getEditorAddin().getEditorClass() == EntryEditor.class)
      return;

    final OpenEditorOptions option = new OpenEditorOptions(context, EntryEditor.class);
    option.setFlags(OpenEditorOptions.RAISE);
    SwingUtilities.invokeLater(
      new Runnable() {
        @Override
        public void run() {
          DirectoryNavigatorController.this.entry = openEditor(option);
        }
      }
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   openEditor
  /**
   ** Opens an editor.
   ** <br>
   ** This method is preferred over the different
   ** openDefaultEditorInFrame(java.net.URL) and
   ** openEditorInFrame(java.lang.Class, oracle.ide.addin.Context) variants
   ** which will eventually be deprecated.
   **
   ** @param  option             used to specify how to open an editor.
   **                            <br>
   **                            Allowed object is {@link OpenEditorOptions}.
   **
   ** @return                    the opened {@link Editor}.
   **                            <br>
   **                            Possible object is {@link Editor}.
   */
  private Editor openEditor(final OpenEditorOptions option) {
    final WaitCursor waitCursor = DirectoryNavigatorManager.instance().waitCursor();
    Editor editor = null;
    try {
      waitCursor.show();
      editor = EditorManager.getEditorManager().openEditor(option);
      editor.addViewListener(this);
      View.updateToolbarActions(editor.getToolbar());
    }
    finally {
      waitCursor.hide();
    }
    return editor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   information
  /**
   ** Display an information alert.
   ** <br>
   ** An information alert is used to provide the user with harmless status
   ** information. The event communicated by an information alert is not an
   ** interruption of a task initiated by the user. The event also has not
   ** resulted in data loss and does not require any resolution; it merely
   ** informs the user that something has happened.
   **
   ** @param  title              the title of the dialog. This should be a one
   **                            to three word summary of the event.
   **                            <br>
   **                            <code>null</code> or <code>""</code>
   **                            are <b>not</b> valid values.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to display.
   **                            <br>
   **                            This should contain an extended narrative of
   **                            the event.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void information(final String title, final String message) {
    MessageDialog.information(Ide.getMainWindow(), message, title,  null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   critical
  /**
   ** Display an critical alert.
   ** <br>
   ** A critical alert is used to notify the user about a severe unanticipated
   ** condition which will not allow task completion. The interruption may have
   ** reseulted in significant data loss, however the is little the user can do
   ** to resolve the condition.
   ** <br>
   ** In general, these are potentially harmful events that occur outside of the
   ** application and are largely beyond the user's control.
   **
   ** @param  title              the title of the dialog. This should be a one
   **                            to three word summary of the event.
   **                            <br>
   **                            <code>null</code> or <code>""</code>
   **                            are <b>not</b> valid values.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message text should explain what the
   **                            problem is including an error number if
   **                            available, and any known reasons for why the
   **                            error might have occurred. It is also useful to
   **                            suggest some possible solution to help the user
   **                            understand what might need to be done to fix
   **                            the condition (if known).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void critical(final String title, final String message) {
    MessageDialog.critical(Ide.getMainWindow(), message, title,  null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  private void fatal(final Exception e) {
    if (e == null)
      return;

    ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e);  
  }
}