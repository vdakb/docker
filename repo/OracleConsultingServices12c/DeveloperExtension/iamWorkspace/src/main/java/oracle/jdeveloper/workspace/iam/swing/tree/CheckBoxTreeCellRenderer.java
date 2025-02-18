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

    File        :   CheckBoxTreeCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CheckBoxTreeCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.awt.Component;
import java.awt.BorderLayout;

import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.BorderFactory;

import oracle.jdeveloper.workspace.iam.swing.widget.TriStateButton;
import oracle.jdeveloper.workspace.iam.swing.widget.TriStateCheckBox;
import oracle.jdeveloper.workspace.iam.swing.widget.NullTriStateCheckBox;

////////////////////////////////////////////////////////////////////////////////
// class CheckBoxTreeCellRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Displays an option entry (CheckBox) in a tree.
 ** <p>
 ** <code>CheckBoxTreeCellRenderer</code> is not opaque and unless you subclass
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
public class CheckBoxTreeCellRenderer extends OptionTreeCellRenderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5090151384836220821")
  private static final long        serialVersionUID = 3908062267389303067L;

  /**
   ** The component that is used to paint the hotspot button in cell renderer if
   ** it is visible
   */
  protected static final JCheckBox PROTOTYP         = new TriStateCheckBox();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>CheckBoxTreeCellRenderer</code> that has a check box
   ** and a label.
   */
  public CheckBoxTreeCellRenderer() {
    // ensure inheritance
    this(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>CheckBoxTreeCellRenderer</code> that has a check box
   ** and a label.
   ** <p>
   ** The supplied {@link ContentTreeCellRenderer} is used to render the content
   ** of the cell.
   **
   ** @param  renderer           the {@link ContentTreeCellRenderer} is used to
   **                            render the content of the cell.
   **                            <br>
   **                            Allowed object is {@link ContentTreeCellRenderer}.
   */
  public CheckBoxTreeCellRenderer(final ContentTreeCellRenderer renderer) {
    // ensure inheritance
    super(renderer);

    // initialize instance
    ((TriStateCheckBox)this.hotspot).setOpaque(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getToolTipText (overridden)
  /**
   ** Returns the string to be used as the tooltip for <code>event</code>.
   ** <p>
   ** By default this returns any string set using <code>setToolTipText</code>.
   ** If a component provides more extensive API to support differing tooltips
   ** at different locations, this method should be overridden.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   **
   ** @return                    the string to be used as the tooltip for
   **                            <code>event</code>.
   */
  @Override
  public String getToolTipText(final MouseEvent event) {
    return toolTipText(event, ((TriStateCheckBox)this.hotspot).getWidth());
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

    applyComponentOrientation(tree.getComponentOrientation());

    OptionTreeNodeData data = null;
    if (value instanceof OptionTreeNode)
      data = ((OptionTreeNode)value).data();

    renderHotspot((OptionTree)tree, data, selected, row);
    if (this.content != null) {
      // most of the rendering is delegated to the wrapped
      // DefaultTreeCellRenderer, the rest depends on the TreeCheckingModel
      JComponent renderer = (JComponent)this.content.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, focused);
      setBorder(renderer.getBorder());
      renderer.setBorder(BorderFactory.createEmptyBorder());
      super.render(tree, data, selected, expanded, leaf, row, focused);
      add(label);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotHit (OptionTreeCellRenderer)
  /**
   ** Determines whether the specified relative coordinates insist on the
   ** intended hotspot. It is used by the OptionTreeHandler to figure out
   ** whether to toggle a node or not.
   **
   ** @param  x                  the relative coordinates of a mouse action
   **                            along the X axis.
   ** @param  y                  the relative coordinates of a mouse action.
   **                            along the Y axis.
   **
   ** @return                    <code>true</code> is the specified relative
   **                            coordinates are inside the hotspot area,
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean hotspotHit(final int x, final int y) {
    return ((TriStateCheckBox)this.hotspot).contains(x, y);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createHotspot (OptionTreeCellRenderer)
  /**
   ** Creates the hotspot button this rendere will use.
   **
   ** @return                    the hotspot button this rendere will use.
   */
  @Override
  protected final TriStateButton createHotspot() {
    return new NullTriStateCheckBox();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPrototyps (OptionTreeCellRenderer)
  /**
   ** Creates the prototyp button this rendere will use.
   **
   ** @return                    the hotspot button this rendere will use.
   */
  @Override
  protected final JComponent createPrototype() {
    return PROTOTYP;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renderHotspot (OptionTreeCellRenderer)
  /**
   ** Renders the hotspot state based on the selection in the selection model.
   ** By default, we check if the path is selected. If yes, we mark the hotspot
   ** as TriStateButton.State.SELECTED. If not, we will check if the path is
   ** partially selected, if yes, we set the hotspot as <code>null</code> or
   ** TriStateButton.State.PARTIAL_SELECTED or
   ** TriStateButton.State.PARTIAL_UNSELECTED respecivly to indicate the path is
   ** partially selected. Otherwise, we set it to
   ** TriStateButton.State.UNSELECTED.
   **
   ** @param  tree               the {@link JTree} that is asking the renderer
   **                            to draw; can be <code>null</code>.
   ** @param  data               the value of the cell to be rendered. It is up
   **                            to the specific renderer to interpret and draw
   **                            the value. For example, if <code>value</code>
   **                            is the string "true", it could be rendered as a
   **                            string or it could be rendered as a check box
   **                            that is checked. <code>null</code> is a valid
   **                            value.
   ** @param  selected           <code>true</code> if the cell is to be rendered
   **                            with the selection highlighted; otherwise
   **                            <code>false</code>.
   ** @param  row                the row index of the cell being drawn. When
   **                            drawing the header, the value of
   **                            <code>row</code> is <code>-1</code>.
   */
  @Override
  protected final void renderHotspot(final OptionTree tree, final OptionTreeNodeData data, final boolean selected, final int row) {
    ((TriStateCheckBox)this.hotspot).setPreferredSize(PROTOTYP.getPreferredSize());
    this.empty.setPreferredSize(PROTOTYP.getPreferredSize());

    final OptionTreeSelectionModel selectionModel = tree.optionSelection;
    boolean enabled = tree.isEnabled() && tree.hotspotEnabled() && (data != null && data.hotspotEnabled());
    if (!enabled && !selected) {
      if (getBackground() != null) {
        setForeground(getBackground().darker());
      }
    }
    if ((data != null && data.hotspotVisible())) {
     ((TriStateCheckBox)this.hotspot).setBorderPaintedFlat(true);
     ((TriStateCheckBox)this.hotspot).setFocusPainted(false);
     ((TriStateCheckBox)this.hotspot).setEnabled(tree.isEnabled() ? data.hotspotEnabled() : false);
    }
    updateState(tree.getPathForRow(row), selectionModel);

    if (data == null || data != null && !data.hotspotVisible()) {
      remove(((TriStateCheckBox)this.hotspot));
      // expand the tree node size to be the same as the one with check box
//      add(this.empty, BorderLayout.BEFORE_LINE_BEGINS);
    }
    else {
//      remove(this.empty);
      add((TriStateCheckBox)this.hotspot, BorderLayout.BEFORE_LINE_BEGINS);
    }
  }
}