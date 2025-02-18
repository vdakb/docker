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
    Subsystem   :   Identity Manager Facility

    File        :   InventoryNavigatorManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    InventoryNavigatorManager.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-04-10  DSteding    First release version
*/

package oracle.jdeveloper.deployment.oim.navigator;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.layout.ViewId;

import oracle.ide.controller.IdeAction;

import oracle.ide.editor.EditorManager;

import oracle.ideri.navigator.DefaultNavigatorWindow;

import oracle.jdeveloper.connection.iam.navigator.EndpointNavigatorManager;
import oracle.jdeveloper.deployment.oim.Bundle;
import oracle.jdeveloper.deployment.oim.editor.EntryTracker;
import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// class InventoryNavigatorManager
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A navigator manager for the custom "Inventory" navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class InventoryNavigatorManager extends EndpointNavigatorManager {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The action id that is responsible for toggling the visibility of this
   ** manager's toolbar.
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_TOOLBAR    = "oim.deployment.action.toolbar";

  /**
   ** The id of the show inventory server navigator action.
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_NAVIGATOR  = "oim.deployment.action.navigator";

  /**
   ** The id of the create a new  Inventory Service deployment.
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_CREATE     = "oim.deployment.action.create";

  /**
   ** The action id of the modify an existing  Inventory Service deployment
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_MODIFY     = "oim.deployment.action.modify";
  
  /**
   ** The ID of dockable views created by this navigator manager.
   ** <<br>
   ** This is returned from the getNavigatorID() method.
   */
  static final String NAVIGATOR_ID      = "inventory-navigator";

  /**
   ** The ID of the toolbar associated with this navigator manager.
   ** <<br>
   ** This is returned from the getToolbarId() method.
   */
  static final String NAVIGATOR_TOOLBAR = "oim.deployment.toolbar";

  /**
   ** The view ID of dockable views created by this navigator manager.
   ** <<br>
   ** This is returned from the getViewCategory() method.
   */
  static final String NAVIGATOR_VIEW    = "InventoryNavigatorView";

  /**
   ** The view name of dockable views created by this navigator manager.
   ** <<br>
   ** This is returned from the getDefaultName() method.
   */
  static final String NAVIGATOR_NAME    = "InventoryNavigatorName";

  /**
   ** The {@link InventoryNavigatorManager} implements the singleton pattern.
   ** <br>
   ** The static attribute {@link #instance} holds this single instance.
   */
  private static InventoryNavigatorManager instance = null;

  //////////////////////////////////////////////////////////////////////////////
  // class Window
  // ~~~~~ ~~~~~~
  /**
   ** We override the DefaultNavigatorWindow just so that we can install a custom
   ** controller to handle the standard IDE Delete action. Arguably, the IDE
   ** should make it easier for us to override this behavior without having to
   ** create a subclass of DefaultNavigatorWindow.
   */
  public static class Window extends DefaultNavigatorWindow {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>InventoryNavigatorManager</code> for the
     ** specified {@link Context} and the associated view.
     **
     ** @param  context          the global context of the Oracle JDeveloper
     **                          IDE.
     ** @param  viewId           the id of the view.
     */
    private Window(final Context context, final String viewId) {
      // ensure inheritance
      super(context, viewId);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getNavigatorID (overridden)
    @Override
    protected String getNavigatorID() {
      return NAVIGATOR_ID;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getNavigatorID (overridden)
    /**
     ** Returns the id of the toolbar associated with this Navigator window.
     ** <b>Important</b>:
     ** <br>
     ** The DefaultNavigatorWindow resolves the toolbar by its view name
     ** appended with the static string '.Toolbar'. Nevertheless we use our own
     ** naming conventions in the extensions manifest hence overloading the
     ** method creates less confusion.
     **
     ** @return                  the id of the toolbar associated with this
     **                          Navigator window.
     */
    @Override
    protected String getToolbarId() {
      return NAVIGATOR_TOOLBAR;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTitleName (overridden)
    @Override
    public String getTitleName() {
      return Bundle.string(Bundle.INVENTORY_NODE_LABEL);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>InventoryNavigatorManager</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private InventoryNavigatorManager() {
    // ensure inheritance
    super(NAVIGATOR_VIEW, NAVIGATOR_NAME, new InventoryNavigatorModel());

    // initialize instance
    initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setToolbarVisible (overridden)
  /**
   ** <code>true</code> if the toolbar associated with the navigator
   ** window should be visible.
   **
   ** @param  visible            <code>true</code> if the toolbar associated
   **                            with the navigator window is visible; otherwise
   **                            <code>false</code>.
   */
  @Override
  protected void setToolbarVisible(final boolean visible) {
    // ensure inheritance
    super.setToolbarVisible(visible);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isToolbarVisible (overridden)
  /**
   ** Returns <code>true</code> if the toolbar associated with the navigator
   ** window is visible.
   **
   ** @return                    <code>true</code> if the toolbar associated
   **                            with the navigator window is visible; otherwise
   **                            <code>false</code>.
   */
  @Override
  protected boolean isToolbarVisible() {
    // ensure inheritance
    return super.isToolbarVisible();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createShowNavigatorAction (DefaultNavigatorManager)
  /**
   ** Get the action used to show this navigator.
   ** <br>
   ** The superclass takes care of attaching a controller to this action which
   ** actually makes the navigator visible.
   */
  @Override
  protected final IdeAction createShowNavigatorAction() {
    return IdeAction.find(action());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** The <code>InventoryNavigatorManager</code> is a singleton class.
   ** This method gets this manager's single instance.
   **
   ** @return                    the <code>InventoryNavigatorManager</code>
   **                            single instance.
   */
  public static synchronized InventoryNavigatorManager instance() {
    if (instance == null) {
      instance = new InventoryNavigatorManager();
      EditorManager.getEditorManager().addEditorListener(new EntryTracker());
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the id of the show inventory navigator action.
   **
   ** @return                    the id of the show inventory server navigator
   **                            action.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int action() {
    final Integer cmd = Ide.findCmdID(ACTION_NAVIGATOR);
    if (cmd == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, ACTION_NAVIGATOR));

    return cmd;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toolbar
  /**
   ** Returns the id of the action that is responsible for toggling the
   ** visibility of this manager's toolbar.
   **
   ** @return                    the id of the action that is responsible for
   **                            toggling the visibility of this manager's
   **                            toolbar.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int toolbar() {
    final Integer cmd = Ide.findCmdID(ACTION_TOOLBAR);
    if (cmd == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, ACTION_TOOLBAR));

    return cmd;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Returns the id of the action that is responsible for create a new
   ** Directory Service connection.
   **
   ** @return                    the id of the action that is responsible for
   **                            create a new Directory Service connection.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int create() {
    final Integer id = Ide.findCmdID(ACTION_CREATE);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, ACTION_CREATE));

    return id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Returns the id of the action that is responsible for modify an existing
   ** Directory Service connection.
   **
   ** @return                    the id of the action that is responsible for
   **                            modify an existing Directory Service
   **                            connection.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int modify() {
    final Integer id = Ide.findCmdID(ACTION_MODIFY);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, ACTION_MODIFY));

    return id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNavigatorWindow (overridden)
  /**
   ** This is the method that actually creates a new instance of a
   ** {@link DefaultNavigatorWindow}.
   ** <br>
   ** We override it here to create our custom subclass (required in order to
   ** use a different controller to handle the standard Delete action).
   **
   ** @param  context            the global context of the Oracle JDeveloper
   **                            IDE.
   ** @param  viewId             the id of the view.
   **
   ** @return                    the actually created a new instance of a
   **                            {@link DefaultNavigatorWindow}.
   */
  @Override
  protected DefaultNavigatorWindow createNavigatorWindow(final Context context, final ViewId viewId) {
    final Window window = new Window(context, viewId.getId());
    window.addViewSelectionListener(InventoryNavigatorListener.instance);
    return window;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createToggleToolbarAction (overridden)
  /**
   ** Subclasses needs override this method to return the {@link IdeAction} that
   ** is responsible for toggling the visibility of this manager's toolbar.
   ** <br>
   ** If the manager does not have a toolbar, this method should return
   ** <code>null</code>.
   **
   ** @return                    the {@link IdeAction} that is responsible for
   **                            toggling the visibility of this manager's toolbar.
   */
  @Override
  protected IdeAction createToggleToolbarAction() {
    return IdeAction.find(toolbar());
  }
}