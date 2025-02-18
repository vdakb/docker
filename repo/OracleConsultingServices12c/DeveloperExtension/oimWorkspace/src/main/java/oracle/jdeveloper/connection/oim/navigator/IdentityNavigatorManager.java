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

    File        :   IdentityNavigatorManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityNavigatorManager.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.navigator;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.layout.ViewId;

import oracle.ide.controller.IdeAction;

import oracle.ide.editor.EditorManager;

import oracle.ideri.navigator.DefaultNavigatorWindow;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.connection.oim.Bundle;

import oracle.jdeveloper.connection.iam.navigator.EndpointNavigatorManager;

////////////////////////////////////////////////////////////////////////////////
// class IdentityNavigatorManager
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A navigator manager for the custom "Identity Service" navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityNavigatorManager extends EndpointNavigatorManager {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The action id that is responsible for toggling the visibility of this
   ** manager's toolbar.
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_TOOLBAR    = "iam.connection.oim.action.toolbar";

  /**
   ** The id of the show directory server navigator action.
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_NAVIGATOR  = "iam.connection.oim.action.navigator";

  /**
   ** The id of the create a new Identity Service connection.
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_CREATE     = "iam.connection.oim.action.create";

  /**
   ** The action id of the modify an existing Identity Service connection
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_MODIFY     = "iam.connection.oim.action.modify";

  /**
   ** The action id of the close an opened Identity Service connection
   ** This matches the id defined for this action in extension.xml.
   */
  static final String ACTION_CLOSE     = "iam.connection.oim.action.close";
  
  /**
   ** The ID of dockable views created by this navigator manager.
   ** <<br>
   ** This is returned from the getNavigatorID() method.
   */
  static final String NAVIGATOR_ID      = "identity-navigator";

  /**
   ** The ID of the toolbar associated with this navigator manager.
   ** <<br>
   ** This is returned from the getToolbarId() method.
   */
  static final String NAVIGATOR_TOOLBAR = "iam.connection.oim.toolbar";

  /**
   ** The view ID of dockable views created by this navigator manager.
   ** <<br>
   ** This is returned from the getViewCategory() method.
   */
  static final String NAVIGATOR_VIEW    = "IdentityNavigatorView";

  /**
   ** The view name of dockable views created by this navigator manager.
   ** <<br>
   ** This is returned from the getDefaultName() method.
   */
  static final String NAVIGATOR_NAME    = "IdentityNavigatorName";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link IdentityNavigatorManager} implements the singleton pattern.
   ** <br>
   ** The static attribute {@link #instance} holds this single instance.
   */
  private static IdentityNavigatorManager instance = null;

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
     ** Constructs an empty <code>IdentityNavigatorWindow</code> for the
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
      return Bundle.string(Bundle.IDENTITY_NODE_LABEL);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityNavigatorManager</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private IdentityNavigatorManager() {
    // ensure inheritance
    super(NAVIGATOR_VIEW, NAVIGATOR_NAME, new IdentityNavigatorModel());

    // initialize instance
    initialize();
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
   ** The <code>IdentityNavigatorManager</code> is a singleton class.
   ** This method gets this manager's single instance.
   **
   ** @return                    the <code>IdentityNavigatorManager</code>
   **                            single instance.
   */
  public static synchronized IdentityNavigatorManager instance() {
    if (instance == null) {
      instance = new IdentityNavigatorManager();
//      EditorManager.getEditorManager().addEditorListener(new Tracker());
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the id of the show directory server navigator action.
   **
   ** @return                    the id of the show directory server navigator
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
   ** Identity Service connection.
   **
   ** @return                    the id of the action that is responsible for
   **                            create a new Identity Service connection.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int create() {
    final Integer id = Ide.findCmdID(ACTION_CREATE);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, ACTION_CREATE));

    return id.intValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Returns the id of the action that is responsible for modify an existing
   ** Identity Service connection.
   **
   ** @return                    the id of the action that is responsible for
   **                            modify an existing Identity Service
   **                            connection.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int modify() {
    final Integer id = Ide.findCmdID(ACTION_MODIFY);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, ACTION_MODIFY));

    return id.intValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Returns the id of the action that is responsible for close an opened
   ** Identity Service connection.
   **
   ** @return                    the id of the action that is responsible for
   **                            closing an opened Identity Service
   **                            connection.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int close() {
    final Integer id = Ide.findCmdID(ACTION_CLOSE);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, ACTION_MODIFY));

    return id.intValue();
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
    window.addViewSelectionListener(IdentityNavigatorListener.instance);
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