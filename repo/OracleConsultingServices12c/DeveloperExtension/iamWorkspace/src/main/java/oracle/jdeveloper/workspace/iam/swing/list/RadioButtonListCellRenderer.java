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

    File        :   RadioButtonListCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    RadioButtonListCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.list;

import java.awt.Component;
import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.BorderFactory;

import oracle.jdeveloper.workspace.iam.swing.widget.TriStateButton;
import oracle.jdeveloper.workspace.iam.swing.widget.TriStateRadioButton;
import oracle.jdeveloper.workspace.iam.swing.widget.NullTriStateRadioButton;

////////////////////////////////////////////////////////////////////////////////
// class RadioButtonListCellRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Displays an option entry (RadioButton) in a list.
 ** <p>
 ** <code>RadioButtonListCellRenderer</code> is not opaque and unless you
 ** subclass paint you should not change this.
 ** <p>
 ** See <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use Trees</a>
 ** in <em>The Java Tutorial</em> for examples of customizing node display using
 ** such classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class RadioButtonListCellRenderer extends OptionListCellRenderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3039337633049282578")
  private static final long         serialVersionUID = 1081567229682953599L;

  private static final JRadioButton prototype        = new TriStateRadioButton();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>RadioButtonListCellRenderer</code> that has a radio
   ** bottun and a label.
   */
  public RadioButtonListCellRenderer() {
    // ensure inheritance
    this(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>RadioButtonListCellRenderer</code> that has a check box
   ** and a label.
   ** <p>
   ** The supplied {@link ContentListCellRenderer} is used to render the content
   ** of the cell.
   **
   ** @param  renderer           the {@link ContentListCellRenderer} is used to
   **                            render the content of the cell.
   */
  public RadioButtonListCellRenderer(final ContentListCellRenderer renderer) {
    // ensure inheritance
    super(renderer);

    // initialize instance
    ((TriStateRadioButton)this.hotspot).setOpaque(false);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  getListCellRendererComponent (overridden)
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
   ** @param  list             the {@link JList} that is asking the renderer
   **                          to draw; can be <code>null</code>.
   ** @param  value            the value of the cell to be rendered. It is up
   **                          to the specific renderer to interpret and draw
   **                          the value. For example, if <code>value</code>
   **                          is the string "true", it could be rendered as a
   **                          string or it could be rendered as a check box
   **                          that is checked. <code>null</code> is a valid
   **                          value.
   ** @param  index            the row index of the cell being drawn. When
   **                          drawing the header, the value of
   **                          <code>index</code> is <code>-1</code>.
   ** @param  selected         <code>true</code> if the cell is to be rendered
   **                          with the selection highlighted; otherwise
   **                          <code>false</code>.
   ** @param  focused          if <code>true</code>, render cell
   **                          appropriately. For example, put a special
   **                          border on the cell, if the cell can be edited,
   **                          render in the color used to indicate editing.
   */
  @Override
  public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean selected, final boolean focused) {
    ((TriStateRadioButton)this.hotspot).setPreferredSize(RadioButtonListCellRenderer.prototype.getPreferredSize());
    this.empty.setPreferredSize(RadioButtonListCellRenderer.prototype.getPreferredSize());

    OptionListItemData data = null;
    if (value instanceof OptionListItemData)
      data = (OptionListItemData)value;

    if (list instanceof OptionList) {
      final OptionList               optionList     = (OptionList)list;
      final OptionListSelectionModel selectionModel = optionList.optionSelection;
      if (selectionModel != null) {
        boolean enabled = optionList.isEnabled() && optionList.hotspotEnabled() && (data != null && data.hotspotEnabled());
        if (!enabled) {
          if (getBackground() != null) {
            setForeground(getBackground().darker());
          }
        }
        if ((data != null && data.hotspotVisible())) {
         ((TriStateRadioButton)this.hotspot).setFocusPainted(false);
         ((TriStateRadioButton)this.hotspot).setEnabled(list.isEnabled() ? data.hotspotEnabled() : false);
        }
        updateState(this.hotspot, index, selectionModel);
      }
    }

    if (this.content != null) {
      // most of the rendering is delegated to the wrapped
      // DefaultTreeCellRenderer, the rest depends on the TreeCheckingModel
      JComponent renderer = (JComponent)this.content.getListCellRendererComponent(list, value, index, selected, focused);
      setBorder(renderer.getBorder());
      renderer.setBorder(BorderFactory.createEmptyBorder());
      if (!(list instanceof OptionList) || (data == null) || (data != null && !data.hotspotVisible())) {
        remove(((TriStateRadioButton)this.hotspot));
        // expand the tree node size to be the same as the one with check box
        add(this.empty, BorderLayout.AFTER_LINE_ENDS);
      }
      else {
        remove(this.empty);
        add((TriStateRadioButton)this.hotspot, BorderLayout.BEFORE_LINE_BEGINS);
      }
      super.render(list, data, index, selected, focused);
      add(label);
      add(renderer);
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
    return prototype.contains(x, y);
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
    return new NullTriStateRadioButton();
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
    return prototype;
  }
}