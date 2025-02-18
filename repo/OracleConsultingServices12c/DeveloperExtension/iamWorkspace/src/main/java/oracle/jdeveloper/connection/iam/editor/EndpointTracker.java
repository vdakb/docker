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

    File        :   EndpointEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;

import oracle.ide.Context;

import oracle.ide.model.Node;

import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;
import oracle.ide.editor.EditorListener;

////////////////////////////////////////////////////////////////////////////////
// class EndpointTracker
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>EndpointTracker</code> is the integration layer between the IDE and
 ** the editor components inside the IDE.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class EndpointTracker implements EditorListener {

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  private static List<Node> pinned = new LinkedList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointTracker</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EndpointTracker() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editorActivated (EditorListener)
  /**
   ** Called when the editor is activated.
   ** <br>
   ** The {@link Editor} provides information about the editor activated.
   **
   ** @param  editor             the editor instance being activeted.
   **                            <br>
   **                            Allowed object is {@link Editor}.
   */
  @Override
  public void editorActivated(final Editor editor) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editorDeactivated (EditorListener)
  /**
   ** Called when the editor is deactivated.
   ** <br>
   ** The {@link Editor} provides information about the editor deactivated.
   **
   ** @param  editor             the editor instance being deactiveted.
   **                            <br>
   **                            Allowed object is {@link Editor}.
   */
  @Override
  public void editorDeactivated(final Editor editor) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editorOpened (EditorListener)
  /**
   ** Called when the editor is opened.
   ** <br>
   ** The {@link Editor} provides information about the editor activated.
   **
   ** @param  editor             the editor instance being opened.
   **                            <br>
   **                            Allowed object is {@link Editor}.
   */
  @Override
  public void editorOpened(final Editor editor) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    editorClosed (EditorListener)
  /**
   ** Called when the editor is closed.
   ** <br>
   ** The {@link Editor} provides information about the editor closed.
   **
   ** @param  editor             the editor instance being closed.
   **                            <br>
   **                            Allowed object is {@link Editor}.
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
  // Method:   freeze
  public static void freeze(final Node node) {
    if (EndpointTracker.pinned(node))
      return;

    pinned.add(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unfreeze
  public static void unfreeze(final Node node) {
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
   **                            <br>
   **                            Allowed object is {@link Editor}.
   */
  protected static void closeUnpinned(final Editor editor) {
    int    limit = 10;
    EndpointTracker.truncate(pinned, limit);

    final Context          editorContext  = editor.getContext();
    final Node             editorNode     = editorContext != null ? editorContext.getNode() : null;
    final List<Editor>     editorList     = new ArrayList<Editor>();

    final EditorManager    editorManager  = EditorManager.getEditorManager();
    final Iterator<Editor> editorIterator = editorManager.getAllEditors().iterator();
    do {
      // if we have finished close all editors that belongs to us
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