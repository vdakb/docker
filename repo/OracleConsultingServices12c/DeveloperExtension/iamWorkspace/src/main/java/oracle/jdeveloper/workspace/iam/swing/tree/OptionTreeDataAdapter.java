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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   OptionTreeEditorAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionTreeEditorAdapter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.util.EventObject;

import java.io.Serializable;

import java.awt.Component;

import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.ItemListener;
import java.awt.event.FocusListener;
import java.awt.event.ActionListener;

import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import javax.swing.border.EmptyBorder;

import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.CellEditorListener;

import javax.swing.tree.TreeCellEditor;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class OptionTreeDataAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This class is in charge of editing treen nodes on demand for the main JTree
 ** class that will display the tree model of containing editable parameters.
 ** This is in fact the CellEditor class that is registered with the tree.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class OptionTreeDataAdapter implements TreeCellEditor
                            ,          KeyListener
                            ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the default number of columns to use to calculate the preferred width of
   ** an text editor
   */
  public static final  int    DEFAULT_COLUMNS  = 40;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6850949230856127241")
  private static final long   serialVersionUID = 5917352818903674349L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**  the value of a cell. */
  protected final JTree       tree;

  /**  the value of a cell. */
  protected OptionTreeNode    node;

  /** the Swing {@link JComponent} being edited. */
  protected JComponent        editor;

  /** the Swing {@link CellEditorListener} being notified. */
  protected EventListenerList listener    = new EventListenerList();

  /**
   ** the Swing {@link ChangeEvent} to notify registered
   ** {@link CellEditorListener}.
   */
  protected ChangeEvent       changeEvent = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class CommitEditing
  // ~~~~~ ~~~~~~~~~~~~~
  class CommitEditing implements ActionListener
                      ,          ItemListener {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: actionPerformed (ActionListener)
    /**
     ** Invoked when the action occurs.
     **
     ** @param  event            a {@link ActionEvent} object describing the
     **                          event source and what has changed.
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
      OptionTreeDataAdapter.this.stopCellEditing();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: actionPerformed (ItemListener)
    /**
     ** Invoked when an item has been selected or deselected by the user.
     ** <p>
     ** The code written for this method performs the operations that need to
     ** occur when an item is selected (or deselected).
     **
     ** @param  event            a {@link ItemEvent} object describing the
     **                          event source and what has changed.
     */
    @Override
    public void itemStateChanged(final ItemEvent event) {
      OptionTreeDataAdapter.this.stopCellEditing();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class CancelEditing
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Cancels the cell editing if any update happens while modifying a value.
   */
  class CancelEditing implements ActionListener
                      ,          ItemListener {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: actionPerformed (ActionListener)
    /**
     ** Invoked when the action occurs.
     **
     ** @param  event            a {@link ActionEvent} object describing the
     **                          event source and what has changed.
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
      OptionTreeDataAdapter.this.cancelCellEditing();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: actionPerformed (ItemListener)
    /**
     ** Invoked when an item has been selected or deselected by the user.
     ** <p>
     ** The code written for this method performs the operations that need to
     ** occur when an item is selected (or deselected).
     **
     ** @param  event            a {@link ItemEvent} object describing the
     **                          event source and what has changed.
     */
    @Override
    public void itemStateChanged(final ItemEvent event) {
      OptionTreeDataAdapter.this.stopCellEditing();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class SelectOnFocus
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Select all text when focus gained, deselect when focus lost.
   */
  class SelectOnFocus implements FocusListener {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: focusGained (FocusListener)
    /**
     ** Invoked when a component gains the keyboard focus.
     **
     ** @param  event            a {@link FocusEvent} object describing the
     **                          event source and what has changed.
     */
    public void focusGained(final FocusEvent event) {
      if (!(event.getSource() instanceof JTextField))
        return;

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          ((JTextField)event.getSource()).selectAll();
        }
      });
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: focusLost (FocusListener)
    /**
     ** Invoked when a component loses the keyboard focus.
     **
     ** @param  event            a {@link FocusEvent} object describing the
     **                          event source and what has changed.
     */
    public void focusLost(final FocusEvent event) {
      if (!(event.getSource() instanceof JTextField))
        return;

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          ((JTextField)event.getSource()).select(0, 0);
        }
      });
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeDataAdapter</code> with the specified
   ** {@link JComponent}.
   **
   ** @param  component             the {@link JComponent} used for editing.
   */
  private OptionTreeDataAdapter(final OptionTree tree, final JComponent component) {
    // ensure inheritance
    super();

    this.tree = tree;

    // prevent bogus input
    // we use tree cell renderer als as the tree cell editor
    // this means this method will be called if we are creating the cell
    // renderer that will never provide the non-null component for editing
    if (component != null) {
      this.editor = component;
      this.editor.addKeyListener(this);
      if (this.editor instanceof JTextField) {
        JTextField field = (JTextField)component;
        field.addFocusListener(new SelectOnFocus());
        field.addActionListener(new CommitEditing());
        field.registerKeyboardAction(new CancelEditing(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
        field.setBorder(new EmptyBorder(0, 0, 0, 0));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCellEditable (CellEditor)
  /**
   ** Asks the editor if it can start editing using <code>event</code>.
   ** <p>
   ** <code>event</code> is in the invoking component coordinate system. The
   ** editor can not assume the Component returned by
   ** <code>getCellEditorComponent</code> is installed. This method is intended
   ** for the use of client to avoid the cost of setting up and installing the
   ** editor component if editing is not possible. If editing can be started
   ** this method returns <code>true</code>.
   ** <p>
   ** This tells the @link OptionTree} whether or not to let nodes in the tree
   ** be edited. Basically all this does is return <code>false</code> for all
   ** nodes that aren't have flagged as editable in the underlying data model -
   ** which don't have any interactive widgets.
   ** <p>
   ** The hotpot itself is not part of this due to its state is maintained by
   ** the tree itself.
   **
   ** @param  event              the event the editor should use to consider
   **                            whether to begin editing or not.
   **
   ** @return                    <code>true</code> if editing can be started;
   **                            otherwise <code>false</code>.
   **
   ** @see    #shouldSelectCell(EventObject)
   */
  @Override
  public boolean isCellEditable(final EventObject event) {
    // prevent bogus input
    if (event == null)
      return false;

    boolean returnValue = false;
    // if we don't know where we are we do nothing to be safe
    if (event.getSource() == this.tree) {
      final Object component = this.tree.getLastSelectedPathComponent();
        if (component instanceof OptionTreeNode) {
          this.node = (OptionTreeNode)component;
          returnValue = this.node.data.textEditable();
        }
    }
    return returnValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shouldSelectCell (CellEditor)
  /**
   ** Returns <code>true</code> if the editing cell should be selected,
   ** <code>false</code> otherwise.
   ** <p>
   ** Typically, the return value is <code>true</code>, because is most cases
   ** the editing cell should be selected. However, it is useful to return
   ** <code>false</code> to keep the selection from changing for some types of
   ** edits. eg. A table that contains a column of check boxes, the user might
   ** want to be able to change those checkboxes without altering the selection.
   **
   ** @param  event              the event the editor should use to start
   **                            editing.
   **
   ** @return                    <code>true</code> if the editor would like the
   **                            editing cell to be selected; otherwise returns
   **                            <code>false</code>.
   */
  @Override
  public boolean shouldSelectCell(final EventObject event) {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCellEditorValue (CellEditor)
  /**
   ** Returns the value contained in the editor.
   **
   ** @return                    the value contained in the editor
   */
  @Override
  public Object getCellEditorValue() {
    return ((JTextField)this.editor).getText();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTreeCellEditorComponent (TreeCellEditor)
  /**
   ** Implements the <code>TreeCellEditor</code> interface.
   **
   ** @param  tree               the {@link JTree} that is asking the renderer
   **                            to draw; can be <code>null</code>.
   ** @param  value              the value of the cell to be rendered. It is up
   **                            to the specific renderer to interpret and draw
   **                            the value. For example, if <code>value</code>
   **                            is the string "true", it could be rendered as a
   **                            string or it could be rendered as a check box
   **                            that is checked. <code>null</code> is a valid
   **                            value.
   ** @param  selected           <code>true</code> if the cell is to be rendered
   **                            with the selection highlighted; otherwise
   **                            <code>false</code>.
   ** @param  expanded           <code>true</code> if the cell is to be rendered
   **                            expanded; otherwise <code>false</code>.
   ** @param  leaf               <code>true</code> if the cell is to be rendered
   **                            does not have children; otherwise
   **                            <code>false</code>.
   ** @param  row                the row index of the cell being drawn. When
   **                            drawing the header, the value of
   **                            <code>row</code> is <code>-1</code>.
   */
  @Override
  public Component getTreeCellEditorComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row) {
    this.node = (OptionTreeNode)value;
    ((JTextField)this.editor).setText(this.node.data().text());
    return this.editor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  stopCellEditing (CellEditor)
  /**
   ** Forwards the message from the <code>CellEditor</code> to the
   ** <code>delegate</code>.
   */
  @Override
  public boolean stopCellEditing() {
    this.node.data().text(((JTextField)this.editor).getText());
    this.node = null;
    fireEditingStopped();
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancelCellEditing (CellEditor)
  /**
   ** Forwards the message from the <code>CellEditor</code> to the
   ** <code>delegate</code>.
   */
  @Override
  public void cancelCellEditing() {
    this.node = null;
    fireEditingCanceled();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCellEditorListener (CellEditor)
  /**
   ** Register a listener for the <code>CellEditor</code> event.
   ** <p>
   ** When a {@link TreeCellEditor} changes its value it should fire a
   ** <code>PropertyChange</code> event on all registered
   ** {@link CellEditorListener}s, specifying the <code>null</code> value for
   ** the property name and itself as the source.
   **
   ** @param  listener           an object to be invoked when a
   **                            <code>PropertyChange</code> event is fired.
   **                            <br>
   **                            Allowed object is {@link CellEditorListener}.
   */
  @Override
  public void addCellEditorListener(final CellEditorListener listener) {
    this.listener.add(CellEditorListener.class, listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeCellEditorListener (CellEditor)
  /**
   ** Remove a listener for the <code>CellEditor</code> event.
   **
   ** @param  listener           the {@link CellEditorListener} to be removed.
   **                            <br>
   **                            Allowed object is {@link CellEditorListener}.
   */
  @Override
  public void removeCellEditorListener(final CellEditorListener listener) {
    this.listener.remove(CellEditorListener.class, listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  keyPressed (KeyListener)
  /**
   ** Invoked when a key has been pressed.
   ** <p>
   ** See the class description for {@link KeyEvent} for a definition of a key
   ** preseed event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  @Override
  public void keyPressed(final KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_DOWN) {
      this.stopCellEditing();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          OptionTreeDataAdapter.this.tree.dispatchEvent(event);
        }
      });
    }
  }

  //////////////////////////////////////////////////////////////////////////////
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
    if (this.node != null)
     this.node.data().text(((JTextField)this.editor).getText());
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
  @Override
  public void keyTyped(final KeyEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTextEditor
  /**
   **
   */
  public final static OptionTreeDataAdapter createTextEditor(final OptionTree tree) {
    return attachEditor(tree, new JTextField(StringUtility.EMPTY, DEFAULT_COLUMNS));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attachEditor
  /**
   **
   */
  public final static OptionTreeDataAdapter attachEditor(final OptionTree tree, final JComponent component) {
    return new OptionTreeDataAdapter(tree, component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fireEditingStopped
  /**
   ** Notifies all listeners that have registered interest for notification on
   ** this event type. The event instance is created lazily.
   **
   ** @see    EventListenerList
   */
  protected void fireEditingStopped() {
    // Guaranteed to return a non-null array
    Object[] listeners = this.listener.getListenerList();
    // Process the listeners last to first, notifying those that are interested
    // in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == CellEditorListener.class) {
        // Lazily create the event:
        if (changeEvent == null)
          changeEvent = new ChangeEvent(this);
        ((CellEditorListener)listeners[i + 1]).editingStopped(changeEvent);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fireEditingCanceled
  /**
   ** Notifies all listeners that have registered interest for notification on
   ** this event type. The event instance is created lazily.
   **
   ** @see    EventListenerList
   */
  protected void fireEditingCanceled() {
    // Guaranteed to return a non-null array
    Object[] listeners = this.listener.getListenerList();
    // Process the listeners last to first, notifying those that are interested
    // in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == CellEditorListener.class) {
        // Lazily create the event:
        if (changeEvent == null)
          changeEvent = new ChangeEvent(this);
        ((CellEditorListener)listeners[i + 1]).editingCanceled(changeEvent);
      }
    }
  }
}