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

    File        :   CompoundTreeCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    CompoundTreeCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.io.Serializable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;

import javax.swing.plaf.ColorUIResource;

import javax.swing.tree.TreeCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// interface CompoundTreeCellRenderer
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Defines the requirements for an object that displays a tree node.
 ** See
 ** <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use Trees</a>
 ** in <em>The Java Tutorial</em> for an example of implementing a tree cell
 ** renderer that displays custom icons.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface CompoundTreeCellRenderer extends TreeCellRenderer
                                          ,       Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String SELECTION_BORDER_COLOR = "Tree.selectionBorderColor";
  static final String SELECTION_BACKGROUND   = "Tree.selectionBackground";
  static final String SELECTION_FOREGROUND   = "Tree.selectionForeground";
  static final String TEXT_BACKGROUND        = "Tree.textBackground";
  static final String TEXT_FOREGROUND        = "Tree.textForeground";

  static final String LEAF_ICON              = "Tree.leafIcon";
  static final String OPEN_ICON              = "Tree.openIcon";
  static final String CLOSED_ICON            = "Tree.closedIcon";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8187557919771087845")
  static final long   serialVersionUID       = -3093881143726868540L;


  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Label
  // ~~~~~ ~~~~~
  /**
   ** A display area for a short text string or an image, or both of a option
   ** node.
   ** <p>
   ** A label does not react to input events. As a result, it cannot get the
   ** keyboard focus. A label can, however, display a keyboard alternative as a
   ** convenience for a nearby component that has a keyboard alternative but
   ** can't display it.
   */
  public class Label extends JLabel {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    boolean selected;
    boolean focused;

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setSelected
    /**
     ** Sets the selected state of this component to the specified value.
     **
     ** @param  selected         <code>true</code> selects this component,
     **                          <code>false</code> deselects the toggle button.
     */
    public void setSelected(final boolean selected) {
      this.selected = selected;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setFocus
    /**
     ** Sets the focused state of this Component to the specified value.
     **
     ** @param  focused          <code>true</code> sets the focus to this
     **                          component, <code>false</code> removes the focus
     **                          from this component.
     */
    public void setFocus(final boolean focused) {
      this.focused = focused;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getPreferredSize (overridden)
    /**
     ** If the <code>preferredSize</code> has been set to a
     ** non-<code>null</code> value just returns it.
     ** <p>
     ** If the UI delegate's <code>getPreferredSize</code> method returns a non
     ** <code>null</code> value then return that; otherwise defer to the
     ** component's layout manager.
     **
     ** @return                  the value of the <code>preferredSize</code>
     **                          property
     */
    @Override
    public Dimension getPreferredSize() {
      Dimension dimension = super.getPreferredSize();
      if (dimension != null)
        dimension = new Dimension(dimension.width + 3, dimension.height);

      return dimension;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setBackground (overridden)
    /**
     ** Sets the background color of this component.
     ** <p>
     ** It is up to the look and feel to honor this property, some may choose to
     ** ignore it.
     **
     ** @param  background       the desired background {@link Color}
     */
    @Override
    public void setBackground(Color background) {
      if (background instanceof ColorUIResource)
        background = null;
      super.setBackground(background);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: paint (overridden)
    /**
     ** Invoked by Swing to draw components.
     ** <p>
     ** Applications should not invoke <code>paint</code> directly, but should
     ** instead use the <code>repaint</code> method to schedule the component
     ** for redrawing.
     **
     ** @param  context  the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(final Graphics context) {
      final String text = getText();
      if (text != null && text.length() > 0) {
        context.setColor(UIManager.getColor(this.selected ? CompoundTreeCellRenderer.SELECTION_BACKGROUND : CompoundTreeCellRenderer.TEXT_BACKGROUND));
        final Dimension size   = getPreferredSize();
        final Icon      icon   = getIcon();
        final int       offset = (icon != null) ? icon.getIconWidth() + Math.max(0, getIconTextGap() - 1) : 0;
        context.fillRect(offset, 0, size.width - 1 - offset, size.height);
        if (this.focused) {
          context.setColor(UIManager.getColor(CompoundTreeCellRenderer.SELECTION_BORDER_COLOR));
          context.drawRect(offset, 0, size.width - 1 - offset, size.height - 1);
        }
      }
      super.paint(context);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLeafIcon
  /**
   ** If the <code>leafIcon</code> has been set to a non-<code>null</code> value
   ** just returns it; otherwise defer to the component's UI manager defaults.
   **
   ** @return                    the {@link Icon} used to represent leaf nodes.
   */
  Icon getLeafIcon();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOpenIcon
  /**
   ** If the <code>openIcon</code> has been set to a non-<code>null</code>
   ** value just returns it; otherwise defer to the component's UI manager
   ** defaults.
   **
   ** @return                    the {@link Icon} used to represent non-leaf
   **                            nodes that are expanded.
   */
  Icon getOpenIcon();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClosedIcon
  /**
   ** If the <code>closedIcon</code> has been set to a non-<code>null</code>
   ** value just returns it; otherwise defer to the component's UI manager
   ** defaults.
   **
   ** @return                    the {@link Icon} used to represent non-leaf
   **                            nodes that are not expanded.
   */
  Icon getClosedIcon();
}