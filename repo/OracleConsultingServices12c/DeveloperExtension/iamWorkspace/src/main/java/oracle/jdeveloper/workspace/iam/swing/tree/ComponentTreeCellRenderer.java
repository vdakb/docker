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

    File        :   ComponentTreeCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ComponentTreeCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.util.Map;
import java.util.HashMap;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;

import javax.swing.tree.TreeCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class ComponentTreeCellRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A tree cell renderer that displays scaled replicas of Swing components at
 ** the nodes of the tree.
 ** <p>
 ** <code>ComponentTreeCellRenderer</code> is not opaque and unless you subclass
 ** paint you should not change this.
 ** <p>
 ** See <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use Trees</a>
 ** in <em>The Java Tutorial</em> for examples of customizing node display using
 ** such classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class ComponentTreeCellRenderer extends    JPanel
                                       implements CompoundTreeCellRenderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:61227819261614047")
  private static final long serialVersionUID = 1415484198174119798L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Label     label            = new Label();

  // Icons
  /** Icon used to show non-leaf nodes that aren't expanded. */
  protected transient Icon  closedIcon;

  /** Icon used to show leaf nodes. */
  protected transient Icon  leafIcon;

  /** Icon used to show non-leaf nodes that are expanded. */
  protected transient Icon  openIcon;

  private transient Map<Class<?>, TreeCellRenderer> renderers = new HashMap<Class<?>, TreeCellRenderer>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>ComponentTreeCellRenderer</code> suitable for rendering
   ** scaled snapshots of Swing components.
   **
   ** @see    TreeCellRenderer
   */
  public ComponentTreeCellRenderer() {
    // ensure inheritance
    super();

    // initialize UI
    setLayout(null);
    add(this.label);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOpenIcon
  /**
   ** Sets the icon used to represent non-leaf nodes that are expanded.
   **
   ** @param  newIcon            the icon used to represent non-leaf nodes that
   **                            are expanded.
   */
  public void setOpenIcon(Icon newIcon) {
    this.openIcon = newIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClosedIcon
  /**
   ** Sets the icon used to represent non-leaf nodes that are not expanded.
   **
   ** @param  newIcon            the icon used to represent non-leaf nodes that
   **                            are not expanded.
   */
  public void setClosedIcon(final Icon newIcon) {
    this.closedIcon = newIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLeafIcon
  /**
   ** Sets the icon used to represent leaf nodes.
   **
   ** @param  newIcon            the icon used to represent leaf nodes.
   */
  public void setLeafIcon(final Icon newIcon) {
    this.leafIcon = newIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredSize (overridden)
  /**
   ** If the <code>preferredSize</code> has been set to a
   ** non-<code>null</code> value just returns it.
   ** <p>
   ** If the UI delegate's <code>getPreferredSize</code> method returns a non
   ** <code>null</code> value then return that; otherwise defer to the
   ** component's layout manager.
   **
   ** @return                    the value of the <code>preferredSize</code>
   **                            property
   */
  @Override
  public Dimension getPreferredSize() {
    return this.label.getPreferredSize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLeafIcon (CompoundTreeCellRenderer)
  /**
   ** If the <code>leafIcon</code> has been set to a non-<code>null</code> value
   ** just returns it; otherwise defer to the component's UI manager defaults.
   **
   ** @return                    the {@link Icon} used to represent leaf nodes.
   */
  @Override
  public Icon getLeafIcon() {
    return this.leafIcon == null ? UIManager.getIcon(CompoundTreeCellRenderer.LEAF_ICON) : this.leafIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOpenIcon (CompoundTreeCellRenderer)
  /**
   ** If the <code>openIcon</code> has been set to a non-<code>null</code>
   ** value just returns it; otherwise defer to the component's UI manager
   ** defaults.
   **
   ** @return                    the {@link Icon} used to represent non-leaf
   **                            nodes that are expanded.
   */
  @Override
  public Icon getOpenIcon() {
    return this.openIcon == null ? UIManager.getIcon(CompoundTreeCellRenderer.OPEN_ICON) : this.openIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClosedIcon (CompoundTreeCellRenderer)
  /**
   ** If the <code>closedIcon</code> has been set to a non-<code>null</code>
   ** value just returns it; otherwise defer to the component's UI manager
   ** defaults.
   **
   ** @return                    the {@link Icon} used to represent non-leaf
   **                            nodes that are not expanded.
   */
  @Override
  public Icon getClosedIcon() {
    return this.closedIcon == null ? UIManager.getIcon(CompoundTreeCellRenderer.CLOSED_ICON) : this.closedIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  register
  /**
   ** Registers a {@link TreeCellRenderer} for the specific node class.
   **
   ** @param  clazz              the {@link Class} to bind the renderer.
   ** @param  renderer           the {@link TreeCellRenderer} to bind at the
   **                            given class.
   **
   ** @return                    the {@link TreeCellRenderer} for method
   **                            chaining purpose.
   **
   */
  public final TreeCellRenderer register(final Class<?> clazz, final TreeCellRenderer renderer) {
    return this.renderers.put(clazz, renderer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  unregister
  /**
   ** Unregisters a the specific node class.
   **
   ** @param  clazz              the {@link Class} of the renderer to remove.
   **
   ** @return                    the removed {@link TreeCellRenderer} if it was
   **                            bound at the givven class.
   */
  public final TreeCellRenderer unregister(final Class<?> clazz) {
    return this.renderers.remove(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  getTreeCellRendererComponent (overridden)
  /**
   ** Configures the renderer based on the passed in components.
   ** <p>
   ** The value is set from messaging the tree with
   ** <code>convertValueToText</code>, which ultimately invokes
   ** <code>toString</code> on <code>value</code>.
   ** <p>
   ** The foreground color is set based on the selection and the icon is set
   ** based on the <code>leaf</code> and <code>expanded</code> parameters.
   **
   ** @param  tree               the {@link JTree} that is asking the renderer
   **                            to draw; can be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JTree}.
   ** @param  value              the value of the cell to be rendered. It is up
   **                            to the specific renderer to interpret and draw
   **                            the value. For example, if <code>value</code>
   **                            is the string "true", it could be rendered as a
   **                            string or it could be rendered as a check box
   **                            that is checked.  <code>null</code> is a valid
   **                            value.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  selected           <code>true</code> if the cell is to be rendered
   **                            with the selection highlighted; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  expanded           <code>true</code> if the cell is to be rendered
   **                            expanded; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  row                the index of the cell being drawn.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  focused            if <code>true</code>, render cell
   **                            appropriately. For example, put a special
   **                            border on the cell, if the cell can be edited,
   **                            render in the color used to indicate editing.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @Override
  public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean focused) {
    Class<?>         clazz     = value.getClass();
    Component        component = null;
    TreeCellRenderer renderer  = null;
    if (!this.renderers.containsKey(clazz)) {
      component = this;
      String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, focused);
      setEnabled(tree.isEnabled());
      this.label.setFont(tree.getFont());
      this.label.setText(stringValue);
      this.label.setSelected(selected);
      this.label.setFocus(focused);
      if (leaf)
        label.setIcon(getLeafIcon());
      else if (expanded)
        label.setIcon(getOpenIcon());
      else
        label.setIcon(getClosedIcon());
    }
    else {
      renderer  = this.renderers.get(clazz);
      component = renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, focused);
    }
    return component;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doLayout (overridden)
  /**
   ** Causes this container to lay out its components.
   ** <p>
   ** Most programs  should not call this method directly, but should invoke the
   ** <code>validate</code> method instead.
   */
  @Override
  public void doLayout() {
    Dimension label = this.label.getPreferredSize();

    this.label.setLocation(0, 0);
    this.label.setBounds(0, 0, label.width, label.height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** Overridden for performance reasons.
   ** See the <code>DefaultListCellRenderer</code> <a href="DefaultListCellRenderer#override">Implementation Note</a>.
   */
  public void validate() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revalidate (overridden)
  /**
   ** Overridden for performance reasons.
   ** See the <code>DefaultListCellRenderer</code> <a href="DefaultListCellRenderer#override">Implementation Note</a>.
   */
  public void revalidate() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repaint (overridden)
  /**
   ** Overridden for performance reasons.
   ** See the <code>DefaultListCellRenderer</code> <a href="DefaultListCellRenderer#override">Implementation Note</a>.
   */
  public void repaint(long tm, int x, int y, int width, int height) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repaint (overridden)
  /**
   ** Overridden for performance reasons.
   ** See the <code>DefaultListCellRenderer</code> <a href="DefaultListCellRenderer#override">Implementation Note</a>.
   */
  public void repaint(Rectangle r) {
    // intentionally left blank
  }
}
