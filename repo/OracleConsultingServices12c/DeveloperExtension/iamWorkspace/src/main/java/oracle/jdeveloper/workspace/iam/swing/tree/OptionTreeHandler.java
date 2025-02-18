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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   OptionTreeHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionTreeHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.io.Serializable;

import java.awt.Point;
import java.awt.Rectangle;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import javax.swing.tree.TreePath;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

////////////////////////////////////////////////////////////////////////////////
// class OptionTreeHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The a general-purpose implementitaion for the events raised by the
 ** <code>OptionTree</code>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionTreeHandler implements Serializable
                               ,          KeyListener
                               ,          MouseListener
                               ,          TreeSelectionListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7827531446852536800")
  private static final long      serialVersionUID = -5031367009985620350L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final OptionTree       tree;
  private int                    toggle           = -1;
  int                            hotspot          = new JCheckBox().getPreferredSize().width;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates an instance of the general-purpose handler for the specified
   ** {@link OptionTree}.
   **
   ** @param  tree               the {@link OptionTree}.
   */
  public OptionTreeHandler(final OptionTree tree) {
    // ensure inheritance
    super();

    // initialize instance
    this.tree = tree;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented innterfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyPressed (KeyListener)
  /**
   ** Invoked when a key has been pressed.
   ** <p>
   ** See the class description for {@link KeyEvent} for a definition of a key
   ** pressed event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  @Override
  public void keyPressed(final KeyEvent event) {
    if (event.isConsumed() || !this.tree.hotspotEnabled())
      return;

    if (event.getModifiers() == 0 && event.getKeyChar() == KeyEvent.VK_SPACE)
      togglePaths();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   keyReleased (KeyListener)
  /**
   ** Invoked when a key has been released.
   ** <p>
   ** See the class description for {@link KeyEvent} for a definition of a key
   ** released event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  @Override
  public void keyReleased(final KeyEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyTyped (KeyListener)
  /**
   ** Invoked when a key has been typed.
   ** <p>
   ** See the class description for {@link KeyEvent} for a definition of a key
   ** typed event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  public void keyTyped(final KeyEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mousePressed (MouseListener)
  /**
   ** Invoked when a mouse button has been pressed on a component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse pressed event.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This inner class is actually the Swing way, but since JTree is still based
   ** on the AWT mechanism for event handling (what causes the last added
   ** listener to be invoked last) its absolutly necessary that this listener
   ** is the first in the chain of mouse listeners'. This can be achieved by:
   ** <pre>
   **   this.handler = createHandler();
   **   Utilities.insertMouseListener(this, this.handler, 0);
   ** </pre>
   ** We use the mousePressed method instead of mouseClicked for performance
   ** reason.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mousePressed(final MouseEvent event) {
    if (event.isConsumed())
      return;

    final TreePath eventPath = preventToggleEvent(event);
    if (eventPath != null) {
      // determine if the hotspot was the target of the event
      if (hotspotHit(event, eventPath)) {
        event.consume();
        this.tree.optionSelection.adapter.togglePath(eventPath); //_toggleSelection(path);
      }
/*
      else {
        final Object component = eventPath.getLastPathComponent();
        if (component instanceof OptionTreeNode) {
          OptionTreeCellRenderer renderer = (OptionTreeCellRenderer)this.tree.getCellEditor();
          renderer.adapter.tree = (OptionTree)event.getSource();
          renderer.adapter.node = (OptionTreeNode)component;
        }
      }
*/
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mouseReleased (MouseListener)
  /**
   ** Invoked when a mouse button has been released on a component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse released event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mouseReleased(final MouseEvent event) {
    if (event.isConsumed())
      return;

    TreePath eventPath = preventToggleEvent(event);
    if (eventPath != null) {
      // determine if the hotspot was the target of the event
      if (hotspotHit(event, eventPath)) {
        event.consume();
      }
    }

    if (this.toggle != -1)
      this.tree.setToggleClickCount(this.toggle);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mouseClicked (MouseListener)
  /**
   ** Invoked when the mouse button has been clicked (pressed and released) on a
   ** component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse clicked event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mouseClicked(final MouseEvent event) {
    if (event.isConsumed())
      return;

    TreePath eventPath = preventToggleEvent(event);
    if (eventPath != null) {
      /*
      // determine if the hotspot was the target of the event
      if (!hotspotHit(event, eventPath)) {
        final Object component = eventPath.getLastPathComponent();
        if (component instanceof OptionTreeNode) {
          OptionTreeCellRenderer renderer = (OptionTreeCellRenderer)this.tree.getCellEditor();
          if (renderer.adapter.isCellEditable(event))
            this.tree.startEditingAtPath(eventPath);
        }
      }
      */
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mouseEntered (MouseListener)
  /**
   ** Invoked when the mouse enters a component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse entered event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mouseEntered(final MouseEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mouseExited (MouseListener)
  /**
   ** Invoked when the mouse exits a component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse exits event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mouseExited(final MouseEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueChanged (TreeSelectionListener)
  /**
   ** Called whenever the value of the selection changes.
   **
   ** @param  event              the event that characterizes the change.
   **                            <br>
   **                            Allowed object is {@link TreeSelectionEvent}.
   */
  @Override
  public void valueChanged(final TreeSelectionEvent event) {
    this.tree.treeDidChange();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventRow
  /**
   ** Determines the row number the specified {@link MouseEvent} belongs
   ** to.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   **
   ** @return                    the {@link TreePath} row index the
   **                            {@link MouseEvent} belongs to. May be
   **                            <code>-1</code> if the {@link MouseEvent}
   **                            occured somewhere else,
   */
  protected int eventRow(final MouseEvent event) {
    final TreePath path = eventPath(event);
    return path == null ? -1 : this.tree.getRowForPath(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventPath
  /**
   ** Determines the {@link TreePath} the specified {@link MouseEvent} belongs
   ** to.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   **
   ** @return                    the {@link TreePath} the {@link MouseEvent}
   **                            belongs to. May be <code>null</code> if the
   **                            {@link MouseEvent} occured somewhere else,
   */
  protected TreePath eventPath(final MouseEvent event) {
    if (!SwingUtilities.isLeftMouseButton(event))
      return null;

    if (!this.tree.hotspotEnabled())
      return null;

    TreePath path = this.tree.getPathForLocation(event.getX(), event.getY());
    if (path == null)
      return null;
    else
      return path;
//    return hotspotHit(event, path) ? path : null;
    //    return hotspotHit(path, point) ? null : path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotHit
  /**
   ** Determines if the mouse event has on the botspot button component occured.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   ** @param  path               the {@link TreePath} covering the node the
   **                            event is raised for.
   **
   ** @return                    <code>true</code> if the mouse event has
   **                            occured on the hotspot component.
   */
  protected boolean hotspotHit(final MouseEvent event, final TreePath path) {
    return hotspotHit(this.tree.getPathBounds(path), event.getPoint());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotHit
  /**
   ** Determines if the mouse event has on the botspot button component occured.
   **
   ** @param  bounds             the {@link Rectangle} node was be drawn.
   ** @param  point              the {@link Point} with the x,y position of the
   **                            mouse event relative to the source component.
   **
   ** @return                    <code>true</code> if the mouse event has
   **                            occured on the hotspot component.
   */
  private boolean hotspotHit(final Rectangle bounds, final Point point) {
    if (this.tree.getComponentOrientation().isLeftToRight())
      return point.x < bounds.x + this.hotspot;
//      return this.tree.getCellRenderer().hotspotHit(point.x - bounds.x, point.y - bounds.y);
    else
      return point.x > bounds.x + bounds.width - this.hotspot;
//      return this.tree.getCellRenderer().hotspotHit(point.x - (bounds.x + bounds.width), point.y - bounds.y);
/*
    Object object = path.getLastPathComponent();
    if (!(object instanceof OptionTreeNode))
      return false;

    OptionTreeData data = ((OptionTreeNode)object).model();
    if (!data.hotspotVisible())
      return false;

    final Rectangle bounds = this.tree.getPathBounds(path);
    if (this.tree.getComponentOrientation().isLeftToRight())
//      return point.x < bounds.x + this.hotspot.getWidth();
      return this.tree.getCellRenderer().hotspotHit(point.x - bounds.x, point.y - bounds.y);
    else
//      return point.x > bounds.x + bounds.width - this.hotspot.getWidth();
      return this.tree.getCellRenderer().hotspotHit(point.x - (bounds.x + bounds.width), point.y - bounds.y);
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preventToggleEvent
  /**
   ** Determines the {@link TreePath} the specified {@link MouseEvent} belongs
   ** to.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   **
   ** @return                    the {@link TreePath} the {@link MouseEvent}
   **                            belongs to.
   **                            May be <code>null</code> if the
   **                            {@link MouseEvent} occured somewhere else.
   */
  private TreePath preventToggleEvent(final MouseEvent event) {
    // determaine the path the user has clicked in the tree
    final TreePath eventPath = eventPath(event);
    if (eventPath != null) {
      final int count = this.tree.getToggleClickCount();
      if (count != -1) {
        this.toggle = count;
        this.tree.setToggleClickCount(-1);
      }
    }
    return eventPath;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   togglePath
  /**
   ** Toggles (select/unselect) the selection state of all selected paths, if
   ** they are enabled, and possibly propagate the change, according to the
   ** propagation mode.
   */
  protected void togglePaths() {
    TreePath[] treePaths = this.tree.getSelectionPaths();
    if (treePaths == null)
      return;

    for (TreePath treePath : treePaths)
      togglePath(treePath);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   togglePath
  /**
   ** Toggles (select/unselect) the selection state of the specified path, if
   ** this is enabled, and possibly propagate the change, according to the
   ** propagation mode.
   */
  private void togglePath(final TreePath path) {
    if (!this.tree.isEnabled() || !this.tree.hotspotEnabled(path))
      return;

    this.tree.optionSelection.adapter.togglePath(path);
/*
    OptionTreeSelectionModel selectionModel = this.tree.selectionModel();
    boolean selected = selectionModel.pathSelected(path);
    // TODO: How to get it back
//    selectionModel.removeTreeSelectionListener(this);
    try {
//      if (!selectionModel.singleEventMode())
//        selectionModel.batchMode(true);

      selectionModel.togglePath(path);
    }
    finally {
//      if (!selectionModel.singleEventMode())
//        selectionModel.batchMode(false);

//      selectionModel.addTreeSelectionListener(this);
//      this.tree.treeDidChange();
    }
*/
  }
}