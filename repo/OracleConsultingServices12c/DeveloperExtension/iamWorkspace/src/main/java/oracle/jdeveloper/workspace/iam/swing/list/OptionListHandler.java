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

    File        :   OptionListHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionListHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.list;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.Rectangle;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

////////////////////////////////////////////////////////////////////////////////
// class OptionListHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The a general-purpose implementitaion for the events raised by the
 ** <code>OptionList</code>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionListHandler implements KeyListener
                               ,          MouseListener
                               ,          PropertyChangeListener
                               ,          ListSelectionListener
                               ,          ListDataListener {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final OptionList list;
  private final int        hotspot = new JCheckBox().getPreferredSize().width;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates an instance of the general-purpose handler for the specified
   ** {@link OptionList}.
   **
   ** @param  list               the {@link OptionList}.
   */
  public OptionListHandler(final OptionList list) {
    // ensure inheritance
    super();

    // initialize instance
    this.list = list;
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
    if (event.isConsumed() || !this.list.hotspotEnabled())
      return;

    if (event.getModifiers() == 0 && event.getKeyChar() == KeyEvent.VK_SPACE)
      toggleSelections();
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
    if (event.isConsumed() || !this.list.hotspotEnabled())
      return;

//    if (!this.list.isClickInCheckBoxOnly() || hotspotHit(event)) {
    if (hotspotHit(event)) {
      toggleSelection(eventRow(event));
      event.consume();
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
    if (event.isConsumed() || !this.list.hotspotEnabled())
      return;

//    if (!this.list.isClickInCheckBoxOnly() || hotspotHit(event)) {
    if (hotspotHit(event))
      event.consume();
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
  // Method:   propertyChange (PropertyChangeListener)
  /**
   ** This method gets called when a bound property is changed.
   **
   ** @param  event              a {@link PropertyChangeEvent} object describing
   **                            the event source  and the property that has
   **                            changed.
   **                            <br>
   **                            Allowed object is {@link PropertyChangeEvent}.
   */
  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    if (event.getOldValue() instanceof ListModel)
      ((ListModel)event.getOldValue()).removeListDataListener(this);

    if (event.getNewValue() instanceof ListModel) {
      this.list.selectionModel().model((ListModel)event.getNewValue());
      ((ListModel)event.getNewValue()).addListDataListener(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueChanged (ListSelectionListener)
  /**
   ** Called whenever the value of the selection changes.
   **
   ** @param  event              the event that characterizes the change.
   **                            <br>
   **                            Allowed object is {@link ListSelectionEvent}.
   */
  @Override
  public void valueChanged(final ListSelectionEvent event) {
    this.list.repaint();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intervalAdded (ListDataListener)
  /**
   ** Called after the indices in the index0, index1  interval have been
   ** inserted in the data model.
   ** <p>
   ** The new interval includes both index0 and index1.
   **
   ** @param  event              a {@link ListDataEvent} encapsulating the event
   **                            information.
   */
  @Override
  public void intervalAdded(final ListDataEvent event) {
    int minIndex = Math.min(event.getIndex0(), event.getIndex1());
    int maxIndex = Math.max(event.getIndex0(), event.getIndex1());

    // synchronize the selection model with the data model
    ListSelectionModel selection = this.list.selectionModel();
    if (selection != null)
      selection.insertIndexInterval(minIndex, maxIndex - minIndex + 1, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intervalRemoved (ListDataListener)
  /**
   ** Called after the indices in the index0, index1  interval have been
   ** removed from the data model.
   ** <p>
   ** The new interval includes both index0 and index1.
   **
   ** @param  event              a {@link ListDataEvent} encapsulating the event
   **                            information.
   */
  @Override
  public void intervalRemoved(final ListDataEvent event) {
    // synchronize the selection model with the data model
    ListSelectionModel selection = this.list.selectionModel();
    if (selection != null)
      selection.removeIndexInterval(event.getIndex0(), event.getIndex1());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intervalRemoved (ListDataListener)
  /**
   ** Called after the contents of the list has changed in a way  that's too
   ** complex to characterize with the previous methods.
   ** <p>
   ** For example, this is sent when an item has been replaced. Index0 and
   ** index1 bracket the change.
   **
   ** @param  event              a {@link ListDataEvent} encapsulating the event
   **                            information.
   **                            <br>
   **                            Allowed object is {@link ListDataEvent}.
   */
  @Override
  public void contentsChanged(final ListDataEvent event) {
    // intentionally left blank
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
   ** @return                    the row index the {@link MouseEvent} belongs
   **                            to. May be <code>-1</code> if the
   **                            {@link MouseEvent} occured somewhere else.
   */
  protected int eventRow(final MouseEvent event) {
    return this.list.locationToIndex(event.getPoint());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotHit
  /**
   ** Determines if the mouse event has on the botspot button component occured.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   **
   ** @return                    <code>true</code> if the mouse event has
   **                            occured on the hotspot component.
   */
  protected boolean hotspotHit(final MouseEvent event) {
    return hotspotHit(event, eventRow(event));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotHit
  /**
   ** Determines if the mouse event has on the botspot button component occured.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   ** @param  row                the row index covering the node the
   **                            event is raised for.
   **
   ** @return                    <code>true</code> if the mouse event has
   **                            occured on the hotspot component.
   */
  protected boolean hotspotHit(final MouseEvent event, final int row) {
    final Rectangle bounds = this.list.getCellBounds(row, row);
    if (this.list.getComponentOrientation().isLeftToRight())
      return event.getX() < bounds.x + this.hotspot;
    else
      return event.getX() > bounds.x + bounds.width - this.hotspot;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleSelections
  /**
   ** Toggles (select/unselect) the selection state of all selected elements, if
   ** they are enabled.
   */
  protected void toggleSelections() {
    int[] row   = this.list.getSelectedIndices();
    if (row.length == 0)
      return;

    OptionListSelectionModel selection = this.list.selectionModel();
    selection.removeListSelectionListener(this);
    selection.setValueIsAdjusting(true);
    try {
      boolean selected = selection.isSelectedIndex(row[0]);
      for (int i : row) {
        if (!this.list.hotspotEnabled(i))
          continue;

        if (selected && selection.isSelectedIndex(i))
          selection.removeSelectionInterval(i, i);
        else if (!selected && !selection.isSelectedIndex(i))
          selection.addSelectionInterval(i, i);
      }
    }
    finally {
      selection.setValueIsAdjusting(false);
      selection.addListSelectionListener(this);
      this.list.repaint();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleSelection
  /**
   ** Toggles (select/unselect) the selection state of the specified index, if
   ** this is enabled.
   */
  private void toggleSelection(final int index) {
    if (!this.list.isEnabled() || !this.list.hotspotEnabled())
      return;

    OptionListSelectionModel selection = this.list.selectionModel();
    boolean selected = selection.isSelectedIndex(index);
    selection.removeListSelectionListener(this);
    try {
      if (selected)
        selection.removeSelectionInterval(index, index);
      else
        selection.addSelectionInterval(index, index);
    }
    finally {
      selection.addListSelectionListener(this);
      this.list.repaint();
    }
  }
}