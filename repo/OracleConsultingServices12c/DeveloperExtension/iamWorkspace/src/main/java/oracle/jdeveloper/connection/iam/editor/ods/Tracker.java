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

    File        :   Tracker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Tracker.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods;

import oracle.ide.Context;

import oracle.ide.model.Node;

import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;
import oracle.ide.editor.EditorListener;

import oracle.jdeveloper.connection.iam.editor.EndpointTracker;

import oracle.jdeveloper.connection.iam.navigator.ods.DirectoryNavigatorRoot;

////////////////////////////////////////////////////////////////////////////////
// class Tracker
// ~~~~~ ~~~~~~~
/**
 ** The <code>Tracker</code> implements the interface {@link EditorListener} to
 ** receive notifications about {@link Editor} instances being opened,
 ** activated, deactivated, or closed in the IDE.
 ** <p>
 ** This allows to attach to or detach from the given Editor for the purpose of
 ** providing extra functionality. For example, the debugger may use this for
 ** determining when a Code Editor is opened so that it can display the
 ** breakpoint icon locations properly.
 ** <p>
 ** Clients should register listeners with the EditorManager singleton instance.
 ** When an editor is opened, the <code>editorOpened()</code> method will be
 ** called. When an editor is about to be closed, the
 ** <code>editorClosed()</code> method will be called.
 **
 ** @see  Editor
 ** @see  EditorManager
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class Tracker extends EndpointTracker {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Tracker</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Tracker() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editorActivated (EditorListener)
  /**
   ** Notified that a new Editor has been activated.
   ** <br>
   ** This method is called after an Editor is activated, and its
   ** <code>activate()</code> method called.
   ** <br>
   ** The {@link Editor} provides information about the editor activated.
   **
   ** @param  editor             the editor instance that was activate.
   **                            <br>
   **                            Allowed object is {@link Editor}.
   */
  @Override
  public void editorActivated(final Editor editor) {
    final Context context = editor.getContext();
    final Node    node    = context == null ? null  : context.getNode();
    /*
    if (!(node instanceof DirectoryNavigatorRoot)) {
      if (!(node instanceof TextNode))
        return;
      if (!(editor instanceof EquippedWithParser))
        return;
    }
    */
//    OutlineWindow.getInstance().refresh(editor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editorOpened (EditorListener)
  /**
   ** Notified that a new Editor has been opened.
   ** <br>
   ** This method is called after an Editor is opened, and its
   ** <code>open()</code> method called.
   ** <br>
   ** The {@link Editor} provides information about the editor opened.
   **
   ** @param  editor             the editor instance that was opened.
   **                            <br>
   **                            Allowed object is {@link Editor}.
   */
  @Override
  public void editorOpened(final Editor editor) {
    // close any previously opened editor if they are not frozen
    if (editor instanceof EntryEditor || editor instanceof RootEditor || editor instanceof SchemaEditor) {
      closeUnpinned(editor);
    }
    final Context context = editor.getContext();
    final Node    node    = context == null ? null  : context.getNode();
    if (!(node instanceof DirectoryNavigatorRoot))
      return;
//    OutlineWindow.getInstance().refresh(editor);
  }
}