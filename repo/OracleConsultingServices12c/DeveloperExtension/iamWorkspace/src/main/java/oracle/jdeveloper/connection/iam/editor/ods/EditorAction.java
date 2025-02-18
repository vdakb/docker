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

    File        :   EditorAction.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the enum
                    EditorAction.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods;

import oracle.ide.Ide;
import oracle.ide.IdeConstants;

import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// enum EditorAction
// ~~~~ ~~~~~~~~~~~~
/**
 ** The <code>EntryEditor</code> is the integration layer between the IDE and
 ** the editor components to provide an entry editor inside the IDE.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public enum EditorAction {
    /**
     ** The action id to save an modified Directory Service entry.
     ** <br>
     ** This command use the predefined {@link IdeConstants#DELETE_CMD_ID}
     ** command id.
     */
    SAVE(IdeConstants.SAVE_CMD_ID),

  /**
   ** The action id to undo modification at an existing Directory Service
   ** entry.
   ** <br>
   ** This command use the predefined {@link IdeConstants#DELETE_CMD_ID}
   ** command id.
   */
  UNDO(IdeConstants.UNDO_CMD_ID),

  /**
   ** The action id to freeze an existing Directory Service entry.
   ** <br>
   ** This command use the predefined {@link IdeConstants#FREEZE_CMD_ID}
   ** command id.
   */
  FREEZE(IdeConstants.FREEZE_CMD_ID),

  /**
   ** The action id to delete an existing Directory Service entry.
   ** <br>
   ** This command use the predefined {@link IdeConstants#DELETE_CMD_ID}
   ** command id.
   */
  DELETE(IdeConstants.DELETE_CMD_ID),

  /**
   ** The action id to refresh an existing Directory Service entry.
   ** <br>
   ** This command use the predefined {@link IdeConstants#REFRESH_CMD_ID}
   ** command id.
   */
  REFRESH(IdeConstants.REFRESH_CMD_ID)
  ;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  public final int id;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  EditorAction(final int value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the id of the action that is responsible for create a new package
   ** in a Directory Service.
   **
   ** @param  command            a command id used to identify a system command.
   **                            <br>
   **                            System commands are recorded in a hashtable
   **                            because they can be overriden by specifying a
   **                            different command class in the
   **                            addins.properties file.
   **
   ** @return                    the id of the action that is responsible for
   **                            create a new package in a Directory Service.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int action(final String command) {
    final Integer id = Ide.findCmdID(command);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, command));

    return id;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Factory method to create a proper <code>EditorAction</code> from the given
   ** command identifier value.
   **
   ** @param  id                 the command identifier value the
   **                            <code>EditorAction</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>EditorAction</code>.
   **                            <br>
   **                            Possible object is <code>EditorAction</code>.
   */
  public static EditorAction from(final int id) {
    for (EditorAction cursor : EditorAction.values()) {
      if (cursor.id == id)
        return cursor;
    }
    throw new IllegalArgumentException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Adds the specified controller to the list of controllers that manage a
   ** {@link IdeAction} that belongs to the identifier of this
   ** <code>EditorAction</code>.
   **
   ** @param  controller         the {@link Controller} interface to add.
   **                            <br>
   **                            Allowed object is {@link Controller}.
   */
  public void register(final Controller controller) {
    // register the controller on this actions
    final IdeAction action = action();
    if (action != null) {
      action.addController(controller);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregister
  /**
   ** Removes the specified controller from the list of controllers that manage
   ** a {@link IdeAction} that belongs to the identifier of this
   ** <code>EditorAction</code>.
   **
   ** @param  controller         the {@link Controller} interface to remove.
   **                            <br>
   **                            Allowed object is {@link Controller}.
   */
  public void unregister(final Controller controller) {
    // unregister the controller on this actions
    final IdeAction action = action();
    if (action != null) {
      action.removeController(controller);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the action associated with the identifier of this
   ** <code>EditorAction</code> if it already exists. Otherwise, it creates a
   ** new action.
   **
   ** @return                    the action if found, otherwise,
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link IdeAction}.
   */
  public IdeAction action() {
    return IdeAction.get(this.id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Finds the {@link IdeAction} associated with the identifier of this
   ** <code>EditorAction</code>.
   **
   ** @return                    the action if found, otherwise,
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link IdeAction}.
   */
  public IdeAction find() {
    return IdeAction.find(this.id);
  }
}