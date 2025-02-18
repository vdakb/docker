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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryContextDialog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryContextDialog.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.awt.Dimension;

import oracle.bali.ewt.dialog.JEWTDialog;
import oracle.bali.ewt.dialog.DialogHeader;

import oracle.ide.Ide;

import oracle.ide.panels.TDialogLauncher;
import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.connection.iam.navigator.context.DirectoryContext;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryContextDialog
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous collection utility methods. Mainly for internal use.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class DirectoryContextDialog {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContextDialog</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new DirectoryContextDialog()" and enforces use of the public method
   ** below.
   **
   ** @param  title              the String to display in the dialog's title
   **                            bar.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  mask               the bitwise combination of button constants.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  private DirectoryContextDialog() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create a new {@link DirectoryContext} as a subentry of the specified
   ** <code>context</code>.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   **
   ** @return                    <code>true</code> if the command
   **                            execution completes successfully; otherwise
   **                            <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean create(final TraversableContext context) {
    return launch(
      Bundle.string(Bundle.ENTRY_CREATE_TITLE)
    , Bundle.string(Bundle.ENTRY_CREATE_HEADER)
    , JEWTDialog.BUTTON_OK | JEWTDialog.BUTTON_CANCEL
    , new Dimension(1024, 640)
    , DirectoryContextPanel.create()
    , context
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   similar
  /**
   ** Create a new {@link DirectoryContext} that is similar to the specified
   ** <code>context</code>.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   **
   ** @return                    <code>true</code> if the command
   **                            execution completes successfully; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean similar(final TraversableContext context) {
    return launch(
      Bundle.string(Bundle.ENTRY_CREATE_TITLE)
    , Bundle.string(Bundle.ENTRY_CREATE_HEADER)
    , JEWTDialog.BUTTON_OK | JEWTDialog.BUTTON_CANCEL
    , new Dimension(1024, 640)
    , DirectoryContextPanel.similar()
    , context
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Renames a {@link DirectoryContext} to a new name request from user.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   **
   ** @return                    <code>true</code> if the command
   **                            execution completes successfully; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean rename(final TraversableContext context) {
    return launch(
      Bundle.string(Bundle.ENTRY_RENAME_TITLE)
    , Bundle.string(Bundle.ENTRY_RENAME_HEADER)
    , JEWTDialog.BUTTON_OK | JEWTDialog.BUTTON_CANCEL
    , new Dimension(450, 200)
    , DirectoryContextPanel.rename()
    , context
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   move
  /**
   ** Moves a {@link DirectoryContext} to a new name request from user.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   **
   ** @return                    <code>true</code> if the command
   **                            execution completes successfully; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean move(final TraversableContext context) {
    return launch(
      Bundle.string(Bundle.ENTRY_MOVE_TITLE)
    , Bundle.string(Bundle.ENTRY_MOVE_HEADER)
    , JEWTDialog.BUTTON_OK | JEWTDialog.BUTTON_CANCEL
    , new Dimension(450, 200)
    , DirectoryContextPanel.move()
    , context
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Exports a {@link DirectoryContext} to a file.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   **
   ** @return                    <code>true</code> if the command
   **                            execution completes successfully; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean marshal(final TraversableContext context) {
    return launch(
      Bundle.string(Bundle.EXPORT_TITLE)
    , Bundle.string(Bundle.EXPORT_HEADER)
    , JEWTDialog.BUTTON_OK | JEWTDialog.BUTTON_CANCEL
    , new Dimension(450, 280)
    , DirectoryContextPanel.marshal()
    , context
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Exports a {@link DirectoryContext} to a file.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   **
   ** @return                    <code>true</code> if the command
   **                            execution completes successfully; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean unmarshal(final TraversableContext context) {
    return launch(
      Bundle.string(Bundle.IMPORT_TITLE)
    , Bundle.string(Bundle.IMPORT_HEADER)
    , JEWTDialog.BUTTON_OK | JEWTDialog.BUTTON_CANCEL
    , new Dimension(450, 290)
    , DirectoryContextPanel.unmarshal()
    , context
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launch
  /**
   ** Launches the dialog dialog with <code>OK</code> and <code>Cancel</code>
   ** buttons.
   **
   ** @param  title              the dialog title.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  instruction        the dialog header.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  mask               the bitwise combination of button constants.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  preferredSize      the preferredSize of the dialog.
   **                            <br>
   **                            This should not exceed the standard defined by
   **                            the UI team which will soon by 800x600. If the
   **                            preferredSize is larger than 800x600 width and
   **                            height will be adjusted accordingly.
   **                            <br>
   **                            Allowed object is {@link Dimension}.
   ** @param  panel              the content panel
   **                            <br>
   **                            Allowed object is
   **                            {@link DirectoryContextPanel}.
   ** @param  data               the {@link DirectoryContext} listener to be
   **                            added.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   **
   ** @return                    <code>true</code> if <code>OK</code> was
   **                            selected; <code>false</code> if
   **                            <code>Cancel</code> was selected or if the
   **                            Traversable associated with the commit action
   **                            is <code>null</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean launch(final String title, final String instruction, final int mask, final Dimension preferredSize, final DirectoryContextPanel panel, final TraversableContext data) {
    final DialogHeader     header   = new DialogHeader(instruction, Bundle.image(Bundle.ENTRY_DIALOG_IMAGE).getImage());
    final TDialogLauncher  launcher = new TDialogLauncher(Ide.getMainWindow(), title, panel, data);
    launcher.setDialogHeader(header);
    launcher.setInitialSize(preferredSize);
    launcher.setPackDialog(true);
    // initializes the JEWTDialog that the TDialogLauncher will use to host the
    // Traversable.
    final JEWTDialog dialog = launcher.initDialog();
    dialog.setButtonMask(mask);
    dialog.setPreferredSize(preferredSize);
    dialog.setMinimumSize(preferredSize);
    // try to prevent vertival resizing
    dialog.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)preferredSize.getHeight()));
    dialog.setInitialFocus(panel.initialFocus());
    // the panel itself is alos a vetoable change listener
    dialog.addVetoableChangeListener(panel);
    // shows dialog with OK, Cancel, and Help buttons
    // the specified Namespace must not be null, or else an
    // IllegalArgumentException is thrown.
    return launcher.showDialog();
  }
}