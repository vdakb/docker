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

    File        :   EntryEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EntryEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods;

import java.util.Map;
import java.util.HashMap;

import java.awt.Component;

import javax.swing.Icon;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.model.Node;

import oracle.ide.view.View;

import oracle.ide.controller.Controller;

import oracle.ide.controls.Toolbar;

import oracle.jdeveloper.connection.iam.editor.EndpointEditor;
import oracle.jdeveloper.connection.iam.editor.EndpointContent;

import oracle.jdeveloper.connection.iam.model.DirectoryEntry;
import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;
import oracle.jdeveloper.connection.iam.navigator.context.DirectoryContext;

import oracle.jdeveloper.connection.iam.navigator.ods.DirectoryServiceBase;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.panel.entry.PolicyPage;
import oracle.jdeveloper.connection.iam.editor.ods.panel.entry.SecurityPage;
import oracle.jdeveloper.connection.iam.editor.ods.panel.entry.AttributePage;

////////////////////////////////////////////////////////////////////////////////
// class EntryEditor
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>EntryEditor</code> is the integration layer between the IDE and
 ** the editor components to provide an entry editor inside the IDE.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class EntryEditor extends EndpointEditor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String          PATH       = "ods/entry";
  public static final String          SCHEMA     = "ods/schema";
  public static final String          POLICY     = PATH + ".policy";
  public static final String          SECURITY   = PATH + ".security";
  public static final String          ATTRIBUTE  = PATH + ".attribute";
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                      label      = null;
  private PolicyPage                  policy     = null;
  private SecurityPage                security   = null;
  private AttributePage               attribute  = null;
  private final EntryEditorController controller = EntryEditorController.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntryEditor</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntryEditor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTabLabel (overridden)
  /**
   ** This method is called to get the title to display in the tab hosting this
   ** editor by a prior call to {@link #setContext(Context)}.
   **
   ** @return                    the title to display in the tab hosting this
   **                            editor.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getTabLabel() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTabIcon (overridden)
  /**
   ** This method is called to get the icon to display in the tab hosting this
   ** editor by a prior call to {@link #setContext(Context)}.
   **
   ** @return                    the icon to display in the tab hosting this
   **                            editor.
   **                            <br>
   **                            Possible object is {@link Icon}.
   */
  @Override
  public synchronized Icon getTabIcon() {
    return Bundle.icon(Bundle.ICON_DIRECTORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getToolbar (overridden)
  /**
   ** Returns the toolbar associated with this view.
   **
   ** @return                    the toolbar associated with this view.
   **                            <br>
   **                            Possible object is {@link Toolbar}.
   */
  @Override
  public Toolbar getToolbar() {
    return this.controller.toolbar();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getController (overridden)
  /**
   ** Returns the toolbar associated with this view.
   **
   ** @return                    the toolbar associated with this view.
   **                            <br>
   **                            Possible object is {@link Controller}.
   */
  @Override
  public Controller getController() {
    return this.controller;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFixedTopMargin (overridden)
  /**
   ** Returns the non-scrollable top margin component.
   **
   ** @return                    the non-scrollable top margin if there is one
   **                            or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Component}.
   */
  public Component getFixedTopMargin() {
    return getToolbar();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContext (overridden)
  /**
   ** Set the context behind this editor.
   **
   ** @param  context            the current editor context.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  @Override
  public synchronized void setContext(final Context context) {
    // ensure inheritance to avoid NPE
    super.setContext(context);
    if (context != null) {
      this.label = ((DirectoryServiceBase)context.getNode()).manageable().service().resource().name();
      this.data.put(SCHEMA, ((DirectoryServiceBase)context.getNode()).manageable().schema());

      // do some sanity checking in case something goes wrong in the context or
      // element - don't blow up the editor this will bring up a blank tab
      // (no editor) instead of blowing up
      if (this.content == null) {
        // first time initialization for our editor.
        // go ahead and create all our UI components based on this context.
        initializeEditor();
      }
      else {
        updateEditor();
      }
      View.updateToolbarActions(getToolbar());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (overridden)
  /**
   ** Close this editor - this gives us a chance to clean up any resources.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Although the editor is closed, the UndoableEdits continue to survive in
   ** the CommandProcessor queue!  This means that we cannot clean out anything
   ** that that functionality (undo) depends on.
   ** <br>
   ** Once closed an <code>EntryEditor</code> instance should not be used again
   ** as behaviour cannot be guaranteed and exceptions may occur while calling
   ** methods.
   */
  @Override
  public void close() {
    Tracker.unfreeze(getContext().getNode());
    removeViewListener(this.controller);
    this.controller.unregisterAction();

    // ensure inheritance
    super.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refresh the values back to what it was initialized with for the specified
   ** {@link DirectoryEntry}.
   */
  public void refresh() {
    if (changed()) {
      // ask for confirmation
      final Node node = getContext().getNode();
      if (MessageDialog.confirm(Ide.getMainWindow(), Bundle.format(Bundle.ENTRY_REFRESH_CONFIRM, node.getLongLabel()), Bundle.string(Bundle.ENTRY_REFRESH_TITLE), null))
        updateEditor();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  /**
   ** Revert the values back to what it was initialized with for the specified
   ** {@link DirectoryEntry}.
   */
  public void revert() {
    this.attribute.model().revert();
    this.security.model().revert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Update the {@link DirectoryEntry}.
   */
  public void update() {
    final DirectoryContext base = ((DirectoryServiceBase)getContext().getNode()).manageable();
    try {
      base.service().entryModify(base.name(), this.attribute.model().changes());
      updateEditor();
    }
    catch (DirectoryException e) {
      MessageDialog.error(Ide.getMainWindow(), Bundle.format(Bundle.ENTRY_MODIFY_FAILED, base.name().prefix(), base.name().suffix(), e.getLocalizedMessage()), Bundle.string(Bundle.ENTRY_MODIFY_TITLE), null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Returns <code>true</code> if an attributc of the  {@link DirectoryEntry}
   ** is changed.
   **
   ** @return                    <code>true</code> if an attribute of the
   **                            {@link DirectoryEntry} is changed; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @SuppressWarnings("unchecked")
  public boolean changed() {
    boolean dirty = false;
    for (DirectoryValue cursor : (((Map<DirectoryAttribute, DirectoryValue>)this.data.get(ATTRIBUTE))).values()) {
      if (cursor.changed()) {
        dirty = true;
        break;
      }
    }
    return dirty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete the {@link DirectoryEntry}.
   **
   ** @return                    <code>true</code> if deletion is confirmed and
   **                            succeded afterwards.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean delete() {
    final Node node = getContext().getNode();
    if (MessageDialog.confirm(Ide.getMainWindow(), Bundle.format(Bundle.ENTRY_DELETE_SINGLE, node.getShortLabel()), Bundle.string(Bundle.ENTRY_DELETE_TITLE), null)) {
      ((Manageable.Removeable)node).remove();
      return true;
    }
    else
      return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeEditor
  /**
   ** This method is reponsible for initializing the view.
   ** <br>
   ** It should be called at the end of the view constructor.
   */
  private void initializeEditor() {
    // *** DON"T DO ANY INITIALIZATION WORK UNTIL AFTER COMMENT BELOW **
    //
    // Reason: initialization maybe aborted up to that point

    // Following detector can abort initialization, so don't do any set up
    // before this.
    populate();

    // *** INITIALIZATION FROM HERE ONWARDS ***
    this.content   = new EndpointContent();
    this.policy    = PolicyPage.build(this.data);
    this.security  = SecurityPage.build(this.data);
    this.attribute = AttributePage.build(this.data);
    addContent(Bundle.string(Bundle.ENTRY_ATTRIBUTE_PANEL_TITLE), this.attribute);
    addContent(Bundle.string(Bundle.ENTRY_POLICY_PANEL_TITLE),    this.policy);
    addContent(Bundle.string(Bundle.ENTRY_SECURITY_PANEL_TITLE),  this.security);
    
    this.policy.addListener(this.controller);
    this.security.addListener(this.controller);
    this.attribute.addListener(this.controller);
    addViewListener(this.controller);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateEditor
  /**
   ** Update the entry editor and editor component with the associated context.
   */
  private void updateEditor() {
    populate();
    this.attribute.updateView();
    this.security.updateView();
    this.policy.updateView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Fetch the data the editor belongs to from the backend system.
   */
  private void populate() {
    // the entry being edited by this editor
    final DirectoryEntry                          entry     = ((DirectoryServiceBase)getContext().getNode()).manageable().entry();
    final Map<DirectoryAttribute, DirectoryValue> policy    = new HashMap<>();
    final Map<DirectoryAttribute, DirectoryValue> security  = new HashMap<>();
    final Map<DirectoryAttribute, DirectoryValue> attribute = new HashMap<>();
    for (DirectoryAttribute cursor : entry.value().keySet()) {
      if (cursor.name.equals("aci"))
        security.put(cursor, entry.value().get(cursor));
      else if (cursor.name.startsWith("pwd"))
        policy.put(cursor, entry.value().get(cursor));
      else
        attribute.put(cursor, entry.value().get(cursor));
    }
    this.data.put(PATH,      entry.name());
    this.data.put(POLICY,    policy);
    this.data.put(SECURITY,  security);
    this.data.put(ATTRIBUTE, attribute);
  }
}