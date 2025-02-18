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

    File        :   EntryEditorController.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EntryEditorController.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods;

import java.util.List;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import oracle.ide.Context;
import oracle.ide.IdeConstants;

import oracle.ide.model.Node;

import oracle.ide.view.View;
import oracle.ide.view.ViewEvent;
import oracle.ide.view.ViewListener;

import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;

import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;

import oracle.ide.controls.Toolbar;
import oracle.ide.controls.ToggleToolButton;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointElement;

////////////////////////////////////////////////////////////////////////////////
// class EntryEditorController
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to add menu
 ** items and submenus to the context menu.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class EntryEditorController implements Controller
                                   ,          ViewListener 
                                   ,          TableModelListener {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private EntryEditor      editor  = null;
  private Toolbar          toolbar = null;
  private ToggleToolButton freeze  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntryEditorController</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private EntryEditorController() {
    // ensure inheritance
    super();

    // initialize instance
    // register this controller on additional actions to be able to search for
    // entries
    registerAction();
    initializeToolbar();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toolbar
  /**
   ** Returns the toolbar managed by this {@link Controller}.
   **
   ** @return                    the toolbar associated with this view.
   **                            <br>
   **                            Possible object is {@link Toolbar}.
   */
  public Toolbar toolbar() {
    return this.toolbar;
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
   **                            <br>
   **                            Allowed object is {@link IdeAction}.
   ** @param  context            where was the user when the action was
   **                            signaled.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise
   */
  @Override
  public boolean handleEvent(final IdeAction action, final Context context) {
    // first, bounce bogus input
    if (action == null || context == null || this.editor == null)
      return true;

    // second, bounce bogus state
    if (!editor(context.getView()))
      return false;

    // This is how to override standard IDE actions. Because the controller is
    // associated with our view, we are only overriding the behavior when ours
    // is the active view. Our controller has no effect on the action when
    // invoked in other views.
    final EditorAction cmd = EditorAction.from(action.getCommandId());
    switch (cmd) {
      case FREEZE  : handleFreeze(context.getNode());
                     break;
      case SAVE    : this.editor.update();
                     break;
      case UNDO    : this.editor.revert();
                     break;
      case DELETE  : handleDelete();
                     break;
      case REFRESH : this.editor.refresh();
                     break;
      default      : return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (Controller)
  /**
   ** This method updates the enabled status of the specified action within the
   ** specified context.
   **
   ** @param  action             the action that is to be executed.
   **                            <br>
   **                            Allowed object is {@link IdeAction}.
   ** @param  context            where was the user when the action was
   **                            signaled.
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
    if (action == null || context == null || this.editor == null)
      return true;

    // second, bounce bogus state
    if (!editor(context.getView()))
      return false;

    // This is how to override standard IDE actions. Because the controller is
    // associated with our view, we are only overriding the behavior when ours
    // is the active view. Our controller has no effect on the action when
    // invoked in other views.
    final int cmd = action.getCommandId();
    switch (cmd) {
      case IdeConstants.FREEZE_CMD_ID  : updateFreeze(context.getNode());
                                         break;
      case IdeConstants.SAVE_CMD_ID    : EditorAction.SAVE.action().setEnabled(this.editor.changed());
                                         break;
      case IdeConstants.UNDO_CMD_ID    : EditorAction.UNDO.action().setEnabled(this.editor.changed());
                                         break;
      case IdeConstants.DELETE_CMD_ID  : EditorAction.DELETE.action().setEnabled(true);
                                         break;
      case IdeConstants.REFRESH_CMD_ID : EditorAction.REFRESH.action().setEnabled(true);
                                         break;
      default                          : return false;
    }
    return true;
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
    this.editor = (EntryEditor)event.getView();
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
    this.editor.removeViewListener(this);
    this.editor = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tableChanged (TableModelListener)
  /**
   ** This fine grain notification tells listeners the exact range of cells,
   ** rows, or columns that changed.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link TableModelEvent}.
   */
  @Override
  public void tableChanged(final TableModelEvent event) {
    updateAction();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>EntryEditorController</code>.
   **
   ** @return                    the <code>EntryEditorController</code> created.
   **                            <br>
   **                            Possible object is
   **                            <code>EntryEditorController</code>.
   */
  public static EntryEditorController build() {
    return new EntryEditorController();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterAction
  /**
   ** Removes to menu actions in IDE.
   ** <br>
   ** This method is called from the editor on close.
   */
  protected void unregisterAction() {
    // unregister this controller on additional actions to be able to save
    // changes made on an Directory Service entry
    EditorAction.SAVE.unregister(this);
    // unregister this controller on additional actions to be able to revert
    // changes entirely made on an Directory Service entry
    EditorAction.REFRESH.unregister(this);
    // unregister this controller on additional actions to be able to delete
    // an Directory Service entry
    EditorAction.DELETE.unregister(this);
    // unregister this controller on additional actions to be able to undo
    // changes made on a Directory Service entry
    EditorAction.UNDO.unregister(this);
    // unregister this controller on additional actions to be able to
    // freeze/unfreeze an editor view
    EditorAction.FREEZE.unregister(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerAction
  /**
   ** Add to menu actions in IDE.
   */
  private void registerAction() {
    // register this controller on additional actions to be able to save changes
    // made on an Directory Service entry
    EditorAction.SAVE.register(this);
    // register this controller on additional actions to be able to undo changes
    // made on a Directory Service entry
    EditorAction.UNDO.register(this);
    // register this controller on additional actions to be able to
    // freeze/unfreeze an editor view
    EditorAction.FREEZE.register(this);
    // register this controller on additional actions to be able to refresh
    // an Directory Service entry
    EditorAction.REFRESH.register(this);
    // register this controller on additional actions to be able to delete
    // an Directory Service entry
    EditorAction.DELETE.register(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateAction
  /**
   ** Updates to menu actions in IDE.
   */
  private void updateAction() {
    View.updateToolbarActions(this.toolbar);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeToolbar
  /**
   ** This method is reponsible for initializing the toolbar of the view.
   ** <br>
   ** It should be called at the end of the view constructor.
   */
  private void initializeToolbar() {
    this.toolbar = new Toolbar();

    this.freeze = this.toolbar.addToggleButton(EditorAction.FREEZE.action());
    this.toolbar.addSeparator();
    this.toolbar.add(EditorAction.SAVE.action());
    this.toolbar.add(EditorAction.DELETE.action());
    this.toolbar.addSeparator();
    this.toolbar.add(EditorAction.UNDO.action());
    this.toolbar.add(EditorAction.REFRESH.action());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateFreeze
  /**
   ** This method updates the enabled status of the
   ** {@link IdeConstants#FREEZE_CMD_ID} action within the specified context.
   **
   ** @param  element            where was the user when the action was
   **                            signaled.
   **                            <br>
   **                            Allowed object is {@link Node}.
   */
  private void updateFreeze(final Node element) {
    final boolean pinned = Tracker.pinned(element);
    this.freeze.setIcon(Bundle.icon(pinned ? Bundle.EDITOR_ACTION_FROZEN_ICON : Bundle.EDITOR_ACTION_FREEZE_ICON));
    EditorAction.FREEZE.action().setState(pinned);
    // toggle buttuns are always enabled
    EditorAction.FREEZE.action().setEnabled(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleFreeze
  /**
   ** This method updates the enabled status of the
   ** {@link IdeConstants#FREEZE_CMD_ID} action within the specified context.
   **
   ** @param  element            where was the user when the action was
   **                            signaled.
   **                            <br>
   **                            Allowed object is {@link Node}.
   */
  private void handleFreeze(final Node element) {
    boolean pinned = Tracker.pinned(element);
    if (pinned) {
      this.freeze.setIcon(Bundle.icon(Bundle.EDITOR_ACTION_FREEZE_ICON));
      Tracker.unfreeze(element);
    }
    else {
      this.freeze.setIcon(Bundle.icon(Bundle.EDITOR_ACTION_FROZEN_ICON));
      Tracker.freeze(element);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleDelete
  /**
   ** Handle the event {@link IdeConstants#DELETE_CMD_ID}.
   */
  private void handleDelete() {
    if (this.editor.delete()) {
      final Node node = this.editor.getContext().getNode();
      node.delete();
      final EndpointElement parent = ((EndpointElement)node).parent();
      if (parent instanceof Manageable.Refreshable) {
        ((Manageable.Refreshable)parent).refresh();
         parent.fireStructureChanged();
      }
      // if the node is delete by the editor itself close the editor to avoid UI
      // inconsistency
      final List<Editor> candidate = new ArrayList<>();
      candidate.add(this.editor);
      EditorManager.getEditorManager().closeEditors(candidate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editor
  /**
   ** Verifies if the specified {@link View} belongs to this editor.
   **
   ** @param  view               the {@link View} that originates to the context
   **                            event.
   **                            <br>
   **                            Allowed object is {@link View}.
   **
   ** @return                    <code>true</code> if <code>view</code> belongs
   **                            to this editor; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean editor(final View view) {
    return ((view instanceof Editor)) ? view.getId().equals(this.editor.getId()) : false;
  }
}