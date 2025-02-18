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

    File        :   EntryTracker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EntryTracker.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-04-10  DSteding    First release version
*/

package oracle.jdeveloper.deployment.oim.editor;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import oracle.ide.Context;

import oracle.ide.model.Node;

import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;
import oracle.ide.editor.EditorListener;

import oracle.jdeveloper.deployment.oim.navigator.InventoryNavigatorRoot;

////////////////////////////////////////////////////////////////////////////////
// class EntryTracker
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>EntryTracker</code> implements the interface {@link EditorListener}
 ** to receive notifications about {@link Editor} instances being opened,
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
public class EntryTracker implements EditorListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final List<Node> pinned = new ArrayList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntryTracker</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntryTracker() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
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
  // Method:   editorDeactivated (EditorListener)
  /**
   ** Notified that a new Editor has been deactivated.
   ** <br>
   ** This method is called after an Editor is deactivated, and its
   ** <code>deactivate()</code> method called.
   ** <br>
   ** The {@link Editor} provides information about the editor deactivated.
   **
   ** @param  editor             the editor instance that was deactivated.
   */
  @Override
  public void editorDeactivated(final Editor editor) {
    // intentionally left blank
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
   */
  @Override
  public void editorOpened(final Editor editor) {
    final Context context = editor.getContext();
    final Node    node    = context == null ? null  : context.getNode();
    if (!(node instanceof InventoryNavigatorRoot))
      return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    editorClosed (EditorListener)
  /**
   ** Notified that a new Editor has been closed.
   ** <br>
   ** This method is called after an Editor is closed, and its
   ** <code>close()</code> method called.
   ** <br>
   ** The {@link Editor} provides information about the editor closed.
   **
   ** @param  editor             the editor instance that was closed.
   */
  @Override
  public void editorClosed(final Editor editor) {
    pinned.remove(editor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pinned
  public static boolean pinned(final Node node) {
    return pinned.contains(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   freezeNode
  public static void freezeNode(final Node node) {
    if (EntryTracker.pinned(node))
      return;

    pinned.add(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unfreezeNode
  public static void unfreezeNode(final Node node) {
    pinned.remove((Object)node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeUnpinned
  /**
   ** Close all unpinned editors which are not the given editor.
   ** <p>
   ** This is more or less a housekeeping because a user can freeze an editor
   ** and open a new one. After the new one is open and ready the frozen editor
   ** cann be thawed.
   ** <p>
   ** Keeping this editor open violates the rulle that only one unfrozen editor
   ** should be kept open.
   **
   ** @param  editor             the current {@link Editor} that belongs to us.
   */
  private static void closeUnpinned(final Editor editor) {
    int    limit = 10;
    EntryTracker.truncate(pinned, limit);

    final Context          editorContext  = editor.getContext();
    final Node             editorNode     = editorContext != null ? editorContext.getNode() : null;
    final List<Editor>     editorList     = new ArrayList<Editor>();

    final EditorManager    editorManager  = EditorManager.getEditorManager();
    final Iterator<Editor> editorIterator = editorManager.getAllEditors().iterator();
    do {
      // if we have finished close all editors tht abelongs to us
      if (!editorIterator.hasNext()) {
        editorManager.closeEditors(editorList);
        break;
      }
      final Editor  cursorEditor  = editorIterator.next();
      final Context cursorContext = cursorEditor.getContext();
      final Node    cursorNode    = cursorContext != null ? cursorContext.getNode() : null;
//      if (!(cursorNode instanceof PlSqlNode) && !(cursorNode instanceof SearchIdeNode) || editorNode == cursorNode || pinned.contains(cursorNode) || cursorNode.getURL() == null || cursorNode.getClass() != editorNode.getClass())
      if (editorNode == cursorNode || pinned.contains(cursorNode) || cursorNode.getURL() == null || cursorNode.getClass() != editorNode.getClass())
        continue;

      editorList.add(cursorEditor);
    } while (true);
  }

  private static void truncate(final List<Node> list, final int limit) {
    if (list.size() <= limit)
      return;

    // remove the first editor from the list
    list.remove(0);
    // call ourself recursivly until the provided list has reched the limit
    truncate(list, limit);
  }
}