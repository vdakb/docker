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

    File        :   DirectoryNavigatorAction.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the enum
                    DirectoryNavigatorAction.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.ods;

import oracle.ide.Ide;
import oracle.ide.IdeConstants;

import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

public enum DirectoryNavigatorAction {
    /**
     ** The id of the command to search entries in a Directory Service.
     ** <br>
     ** This matches the id <code>ods.action.search</code> defined for this
     ** action in extension.xml.
     */
    SEARCH(action("ods.action.search"))
    /**
     ** The id of the command to create a new entry in a Directory Service.
     ** <br>
     ** This matches the id <code>ods.action.create</code> defined for this
     ** action in extension.xml.
     */
  , CREATE(action("ods.action.create"))
    /**
     ** The id of the command to create a new entry in a Directory Service using
     ** an existing on as a template.
     ** <br>
     ** This matches the id <code>ods.action.likely</code> defined for this
     ** action in extension.xml.
     */
  , LIKELY(action("ods.action.likely"))
    /**
     ** The action id to modify an existing Directory Service entry.
     ** <br>
     ** This matches the id <code>ods.action.modify</code> defined for this
     ** action in extension.xml.
     */
  , MODIFY(action("ods.action.modify"))
    /**
     ** The action id to rename an existing Directory Service entry.
     ** <br>
     ** This matches the id <code>ods.action.rename</code> defined for this
     ** action in extension.xml.
     */
  , RENAME(action("ods.action.rename"))
    /**
     ** The action id to move around an existing Directory Service entry.
     ** <br>
     ** This matches the id <code>ods.action.move</code> defined for this
     ** action in extension.xml.
     */
  , MOVE(action("ods.action.move"))
    /**
     ** The action id to open an existing Directory Service entry.
     ** <br>
     ** This command use the predefined {@link IdeConstants#OPEN_CMD_ID}
     ** command id.
     */
  , OPEN(IdeConstants.OPEN_CMD_ID)
    /**
     ** The action id to delete an existing Directory Service entry.
     ** <br>
     ** This command use the predefined {@link IdeConstants#DELETE_CMD_ID}
     ** command id.
     */
  , DELETE(IdeConstants.DELETE_CMD_ID)
    /**
     ** The action id to refresh an existing Directory Service entry.
     ** <br>
     ** This command use the predefined {@link IdeConstants#REFRESH_CMD_ID}
     ** command id.
     */
  , REFRESH(IdeConstants.REFRESH_CMD_ID)
    /**
     ** The id of the command to export entries from a Directory Service.
     ** <br>
     ** This matches the id <code>ods.action.export</code> defined for this
     ** action in extension.xml.
     */
  , EXPORT(action("ods.action.export"))
    /**
     ** The id of the command to import entries into a Directory Service.
     ** <br>
     ** This matches the id <code>ods.action.import</code> defined for this
     ** action in extension.xml.
     */
  , IMPORT(action("ods.action.import"))
    /**
     ** The action id to show property dialog for an existing Directory Service
     ** entry.
     ** <br>
     ** This command use the predefined {@link IdeConstants#PROPERTIES_CMD_ID}
     ** command id.
     */
  , PROPERTY(IdeConstants.PROPERTIES_CMD_ID)
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
  DirectoryNavigatorAction(final int value) {
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
   ** Factory method to create a proper <code>DirectoryNavigatorAction</code>
   ** from the given command identifier value.
   **
   ** @param  id                 the command identifier value the
   **                            <code>DirectoryNavigatorAction</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>DirectoryNavigatorAction</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryNavigatorAction</code>.
   */
  public static DirectoryNavigatorAction from(final int id) {
    for (DirectoryNavigatorAction cursor : DirectoryNavigatorAction.values()) {
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
   ** <code>DirectoryNavigatorAction</code>.
   **
   ** @param  controller         the {@link Controller} interface to add.
   **                            <br>
   **                            Allowed object is {@link Controller}.
   */
  public void register(final Controller controller) {
    // register this controller on additional actions to be able to create a
    // new connection
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
   ** <code>DirectoryNavigatorAction</code> if it already exists. Otherwise, it
   ** creates a new action.
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
   ** <code>DirectoryNavigatorAction</code>.
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