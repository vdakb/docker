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

    File        :   Utilitiy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Utilitiy.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

import java.util.Set;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.RenderingHints;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.JComponent;
import javax.swing.AbstractButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ListCellRenderer;

import javax.swing.text.View;

import javax.swing.tree.TreeCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class Utilitiy
// ~~~~~ ~~~~~~~~
/**
 ** A utilities class for Swing.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Utilitiy {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static RenderingHints renderingHints = null;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    renderingHints = (RenderingHints)(toolkit.getDesktopProperty("awt.font.desktophints"));
    toolkit.addPropertyChangeListener("awt.font.desktophints", new PropertyChangeListener() {
      public void propertyChange(final PropertyChangeEvent event) {
        if (event.getNewValue() instanceof RenderingHints) {
          renderingHints = (RenderingHints)event.getNewValue();
        }
      }
    });
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   drawStringUnderlineCharAt
  /**
   ** Draw a string with the graphics context at location (x, y) just like
   ** context.drawString would.
   ** <p>
   ** The character at index <code>index</code> in text will be underlined. If
   ** index is beyond the bounds of text (including &lt; 0), nothing will be
   ** underlined.
   **
   ** @param  canvas             the graphics context to draw with.
   ** @param  text               the string to be rendered.
   ** @param  index              the index of the character to underline.
   ** @param  x0                 the x coordinate of the location where the
   **                            <code>String</code> should be rendered.
   ** @param  y0                 the y coordinate of the location where the
   **                            <code>String</code> should be rendered.
   */
  public static void drawStringUnderlineCharAt(final Graphics2D canvas, final String text, final int index, final int x0, final int y0) {
    drawString(canvas, text, x0, y0);

    if (index >= 0 && index < text.length()) {
      FontMetrics m = canvas.getFontMetrics();
      int         x = x0 + m.stringWidth(text.substring(0, index));
      int         y = y0;
      int         w = m.charWidth(text.charAt(index));
      int         h = 1;
      canvas.fillRect(x, y + m.getDescent() - 1, w, h);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   drawString
  /**
   ** Renders the text of the specified <code>String</code>, using the current
   ** text attribute state in the <code>Graphics2D</code> context.
   ** <p>
   ** The baseline of the first character is at position (<i>x</i>,&nbsp;<i>y</i>)
   ** in  the User Space.
   **
   ** @param  canvas             the graphics context to draw with
   ** @param  text               the string to be rendered
   ** @param  x                  the x coordinate of the location where the
   **                            <code>String</code> should be rendered
   ** @param  y                  the y coordinate of the location where the
   **                            <code>String</code> should be rendered
   */
  public static void drawString(final Graphics2D canvas, final String text, final int x, final int y) {
    RenderingHints oldHints = null;
    if (renderingHints != null) {
      oldHints = renderingHints(canvas, renderingHints, null);
      canvas.addRenderingHints(renderingHints);
    }
    canvas.drawString(text, x, y);
    if (oldHints != null)
      canvas.addRenderingHints(oldHints);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orientationOf
  /**
   ** Changes the orientation of a {@link Container} and all of its cildren.
   ** <p>
   ** If the components are a Swing component, the default implementation is
   ** this.
   ** <br>
   ** <pre>
   **   Utilities.orientationOf(this, orientation);
   ** </pre>
   **
   ** @param  container          the {@link Container} the orientation has to
   **                            returned for.
   ** @param  orientation        the new orientation.
   **                            The value has to be either
   **                            <code>SwingConstants.HORIZONTAL</code> or
   **                            <code>SwingConstants#VERTICAL</code>.
   */
  public static void orientationOf(final Container container, int orientation) {
    Component[] components = container.getComponents();
    for (Component component : components) {
      orientationOf(component, orientation);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orientationOf
  /**
   ** Changes the orientation.
   ** <p>
   ** If the component is a Swing component, the default implementation is this.
   ** <br>
   ** <pre>
   **   Utilities.orientationOf(this, orientation);
   ** </pre>
   **
   ** @param  component          the {@link Component} the orientation has to
   **                            set for.
   **
   ** @param  orientation        the new orientation.
   **                            The value has to be either
   **                            <code>SwingConstants.HORIZONTAL</code> or
   **                            <code>SwingConstants#VERTICAL</code>.
   */
  public static void orientationOf(final Component component, final int orientation) {
    int old = orientationOf(component);
    if (orientation != old) {
      if (component instanceof Alignable) {
        ((Alignable)component).orientation(orientation);
      }
      else if (component instanceof JComponent) {
        ((JComponent)component).putClientProperty(Alignable.CLIENT_PROPERTY, orientation);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orientationOf
  /**
   ** Returns the orientation of the specified {@link Component}
   ** <code>component</code>.
   ** <p>
   ** If the component is a Swing component, the default implementation is this.
   ** <br>
   ** <pre>
   **   Utilities.orientationOf(this);
   ** </pre>
   **
   ** @param  component          the {@link Component} the orientation has to
   **                            returned for.
   **
   ** @return                    either <code>SwingConstants.HORIZONTAL</code>
   **                            or <code>SwingConstants#VERTICAL</code>
   **                            regarding the current orientation of the
   **                            component.
   */
  public static int orientationOf(final Component component) {
    if (component instanceof Alignable) {
      return ((Alignable)component).orientation();
    }
    else if (component instanceof JComponent) {
      Integer value = (Integer)((JComponent)component).getClientProperty(Alignable.CLIENT_PROPERTY);
      if (value != null)
        return value;
    }
    return SwingConstants.HORIZONTAL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertMouseListener
  /**
   ** Adds the specified mouse listener to receive mouse events from the
   ** specified component.
   ** <p>
   ** The mouse listener will be inserted in the listeners' chain at the
   ** particular index <code>index</code>.
   ** <p>
   ** If listener <code>listener</code> is <code>null</code>, no exception is
   ** thrown and no action is performed.
   **
   ** @param  component          the {@link Component} that should send  mouse
   **                            events to the specified {@link MouseListener}
   **                            <code>listener</code>.
   ** @param  listener           the {@link MouseListener} to insert in the
   **                            chain of listeners' of the {@link Component}
   **                            <code>component</code>.
   ** @param  index              the index (priority) of the
   **                            {@link MouseListener} <code>listener</code> in
   **                            the chain of listeners' of {@link Component}
   **                            <code>component</code>.
   */
  public static void insertMouseListener(final Component component, final MouseListener listener, final int index) {
    MouseListener[] listeners = component.getMouseListeners();
    for (MouseListener cursor : listeners)
      component.removeMouseListener(cursor);

    for (int i = 0; i < listeners.length; i++) {
      MouseListener cursor = listeners[i];
      if (index == i)
        component.addMouseListener(listener);

      component.addMouseListener(cursor);
    }

    // index is too large, add to the end.
    if (index < 0 || index > listeners.length - 1)
      component.addMouseListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertMouseMotionListener
  /**
   ** Adds the specified mouse motion listener to receive mouse motion events
   ** from the specified component.
   ** <p>
   ** The mouse motion listener will be inserted in the listeners' chain at the
   ** particular index <code>index</code>.
   ** <p>
   ** If listener <code>listener</code> is <code>null</code>, no exception is
   ** thrown and no action is performed.
   **
   ** @param  component          the {@link Component} that should send  mouse
   **                            motion events to the specified
   **                            {@link MouseMotionListener}
   **                            <code>listener</code>.
   ** @param  listener           the {@link MouseMotionListener} to insert in
   **                            the chain of listeners' of the {@link Component}
   **                            <code>component</code>.
   ** @param  index              the index (priority) of the
   **                            {@link MouseMotionListener}
   **                            <code>listener</code> in the chain of
   **                            listeners' of {@link Component}
   **                            <code>component</code>.
   */
  public static void insertMouseMotionListener(final Component component, final MouseMotionListener listener, final int index) {
    MouseMotionListener[] listeners = component.getMouseMotionListeners();
    for (MouseMotionListener cursor : listeners)
      component.removeMouseMotionListener(cursor);

    for (int i = 0; i < listeners.length; i++) {
      MouseMotionListener cursor = listeners[i];
      if (index == i)
        component.addMouseMotionListener(listener);

      component.addMouseMotionListener(cursor);
    }

    // index is too large, add to the end.
    if (index < 0 || index > listeners.length - 1) {
      component.addMouseMotionListener(listener);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adjustPreferredScrollableViewportSize
  /**
   ** This method can be used to fix two JDK bugs.
   ** <p>
   ** One is to fix the row height is wrong when the first element in the model
   ** is <code>null</code> or empty string. The second bug is only on JDK1.4.2
   ** where the vertical scroll bar is shown even all rows are visible. To use
   ** it, you just need to override JList#getPreferredScrollableViewportSize and
   ** call this method.
   ** <pre>
   **   public Dimension getPreferredScrollableViewportSize() {
   **     return Utilities.adjustPreferredScrollableViewportSize(this, super.getPreferredScrollableViewportSize());
   **   }
   ** </pre>
   **
   ** @param  list                the JList
   ** @param  defaultViewportSize the default viewport size from
   **                             JList#getPreferredScrollableViewportSize().
   **
   ** @return                     the adjusted size.
   */
  public static Dimension adjustPreferredScrollableViewportSize(final JList list, final Dimension defaultViewportSize) {
    // workaround the bug that the list is tiny when the first element is empty
    Rectangle cellBonds = list.getCellBounds(0, 0);
    if (cellBonds != null && cellBonds.height < 3) {
      ListCellRenderer renderer = list.getCellRenderer();
      if (renderer != null) {
        Component c = renderer.getListCellRendererComponent(list, "DUMMY STRING", 0, false, false);
        if (c != null) {
          Dimension preferredSize = c.getPreferredSize();
          if (preferredSize != null) {
            int height = preferredSize.height;
            if (height < 3) {
              try {
                height = list.getCellBounds(1, 1).height;
              }
              catch (Exception e) {
                height = 16;
              }
            }
            list.setFixedCellHeight(height);
          }
        }
      }
    }
    if (true) {
      return defaultViewportSize;
    }
    else {
      // in JDK1.4.2, the vertical scroll bar is shown because of the wrong size is calculated.
      Dimension size = defaultViewportSize;
      size.height++;
      return size;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adjustPreferredScrollableViewportSize
  /**
   ** This method can be used to fix two JDK bugs.
   ** <p>
   ** One is to fix the row height is wrong when the first element in the model
   ** is <code>null</code> or empty string. The second bug is only on JDK1.4.2
   ** where the vertical scroll bar is shown even all rows are visible. To use
   ** it, you just need to override JTree#getPreferredScrollableViewportSize and
   ** call this method.
   ** <pre>
   **   public Dimension getPreferredScrollableViewportSize() {
   **     return Utilities.adjustPreferredScrollableViewportSize(this, super.getPreferredScrollableViewportSize());
   **   }
   ** </pre>
   **
   ** @param  tree                the JTree
   ** @param  defaultViewportSize the default viewport size from
   **                             JTree#getPreferredScrollableViewportSize().
   **
   ** @return                     the adjusted size.
   */
  public static Dimension adjustPreferredScrollableViewportSize(final JTree tree, final Dimension defaultViewportSize) {
    // workaround the bug that the list is tiny when the first element is empty
    Rectangle cellBonds = tree.getRowBounds(0);
    if (cellBonds != null && cellBonds.height < 3) {
      TreeCellRenderer renderer = tree.getCellRenderer();
      if (renderer != null) {
        Component c = renderer.getTreeCellRendererComponent(tree, "DUMMY STRING", false, false, true, 0, false);
        if (c != null) {
          Dimension preferredSize = c.getPreferredSize();
          if (preferredSize != null) {
            int height = preferredSize.height;
            if (height < 3) {
              try {
                height = tree.getRowBounds(0).height;
              }
              catch (Exception e) {
                height = 16;
              }
            }
            tree.setRowHeight(height);
          }
        }
      }
    }
    if (true) {
      return defaultViewportSize;
    }
    else {
      // in JDK1.4.2, the vertical scroll bar is shown because of the wrong size is calculated.
      Dimension size = defaultViewportSize;
      size.height++;
      return size;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preferredButtonSize
  public static Dimension preferredButtonSize(final AbstractButton button, final int textIconGap, final boolean horizontal) {
    if (button.getComponentCount() > 0)
      return null;

    final String    text  = button.getText();
    final Font      font  = button.getFont();
    final Rectangle iconR = new Rectangle();
    final Rectangle textR = new Rectangle();
    final Rectangle viewR = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);

    layoutCompoundLabel(button, button.getFontMetrics(font), text, button.getIcon(), horizontal, button.getVerticalAlignment(), button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewR, iconR, textR, (text == null ? 0 : textIconGap));

    // the preferred size of the button is the size of the text and icon
    // rectangles plus the buttons insets.
    final Rectangle union  = iconR.union(textR);
    final Insets    insets = button.getInsets();
    union.width  += insets.left + insets.right;
    union.height += insets.top  + insets.bottom;

    return union.getSize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layoutCompoundLabel
  /**
   ** Compute and return the location of the icons origin, the location of
   ** origin of the text baseline, and a possibly clipped version of the
   ** compound labels string.
   ** <p>
   ** Locations are computed relative to the viewR rectangle. This
   ** layoutCompoundLabel() does not know how to handle LEADING/TRAILING values
   ** in horizontalTextPosition (they will default to RIGHT) and in
   ** horizontalAlignment (they will default to CENTER). Use the other version
   ** of layoutCompoundLabel() instead.
   **
   ** @param  fm                 a {@link FontMetrics} object to compute with.
   ** @param  text               the text of the lable to layout.
   ** @param  icon               the optional {@link Icon} to layout along with
   **                            the label.
   ** @param  horizontal         <code>true</code> if the label and icon are
   **                            layouted horizontal; <code>false</code> if
   **                            vertical layout is required.
   ** @param  verticalAlignment  the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalAlignment the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#LEFT
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#RIGHT
   **                            </ul>
   ** @param  verticalTextPosition  the vertical position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalTextPosition  the horizantal position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  viewR              the {@link Rectangle} of the view component to
   **                            compute with.
   ** @param  iconR              the {@link Rectangle} of the icon component to
   **                            compute with.
   ** @param  textR              the {@link Rectangle} of the text component to
   **                            compute with.
   ** @param  textIconGap        the gap between icon and text to compute with.
   **
   ** @return                    the location of the icons origin, the location
   **                            of origin of the text baseline, and a possibly
   **                            clipped version of the compound labels string
   **                            for a horizontal or vertical layouted
   **                            component.
   */
  public static String layoutCompoundLabel(final FontMetrics fm, final String text, final Icon icon, final boolean horizontal, final int verticalAlignment, final int horizontalAlignment, final int verticalTextPosition, final int horizontalTextPosition, final Rectangle viewR, final Rectangle iconR, final Rectangle textR, final int textIconGap) {
    return layoutCompoundLabelImpl(null, fm, text, icon, horizontal, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layoutCompoundLabel
  /**
   ** Compute and return the location of the icons origin, the location of
   ** origin of the text baseline, and a possibly clipped version of the
   ** compound labels string.
   ** <p>
   ** Locations are computed relative to the view rectangle. The JComponents
   ** orientation (LEADING/TRAILING) will also be taken into account and
   ** translated into LEFT/RIGHT values accordingly.
   **
   ** @param  component          the {@link JComponent} the lable needs to be
   **                            layouted for.
   ** @param  fm                 a {@link FontMetrics} object to compute with.
   ** @param  text               the text of the lable to layout.
   ** @param  icon               the optional {@link Icon} to layout along with
   **                            the label.
   ** @param  horizontal         <code>true</code> if the label and icon are
   **                            layouted horizontal; <code>false</code> if
   **                            vertical layout is required.
   ** @param  verticalAlignment  the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalAlignment the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#LEFT
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#RIGHT
   **                            </ul>
   ** @param  verticalTextPosition  the vertical position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalTextPosition  the horizantal position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  viewR              the {@link Rectangle} of the view component to
   **                            compute with.
   ** @param  iconR              the {@link Rectangle} of the icon component to
   **                            compute with.
   ** @param  textR              the {@link Rectangle} of the text component to
   **                            compute with.
   ** @param  textIconGap        the gap between icon and text to compute with.
   **
   ** @return                    the location of the icons origin, the location
   **                            of origin of the text baseline, and a possibly
   **                            clipped version of the compound labels string
   **                            for a horizontal or vertical layouted
   **                            component.
   */
  public static String layoutCompoundLabel(final JComponent component, final FontMetrics fm, final String text, final Icon icon, final boolean horizontal, final int verticalAlignment, final int horizontalAlignment, final int verticalTextPosition, final int horizontalTextPosition, final Rectangle viewR, final Rectangle iconR, final Rectangle textR, final int textIconGap) {
    boolean ltor     = true;
    if (component != null) {
      if (!(component.getComponentOrientation().isLeftToRight()))
        ltor = false;
    }
    // Translate LEADING/TRAILING values in horizontalAlignment to LEFT/RIGHT
    // values depending on the components orientation
    int align = horizontalAlignment;
    switch (horizontalAlignment) {
      case SwingConstants.LEADING  : align = ltor ? SwingConstants.LEFT : SwingConstants.RIGHT;
                                     break;
      case SwingConstants.TRAILING : align = ltor ? SwingConstants.RIGHT : SwingConstants.LEFT;
                                     break;
    }
    // Translate LEADING/TRAILING values in horizontalTextPosition to LEFT/RIGHT
    // values depending on the components orientation
    int position = horizontalTextPosition;
    switch (horizontalTextPosition) {
      case SwingConstants.LEADING  : position = ltor ? SwingConstants.LEFT : SwingConstants.RIGHT;
                                     break;
      case SwingConstants.TRAILING : position = ltor ? SwingConstants.RIGHT : SwingConstants.LEFT;
                                     break;
    }
    return layoutCompoundLabelImpl(component, fm, text, icon, horizontal, verticalAlignment, align, verticalTextPosition, position, viewR, iconR, textR, textIconGap);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layoutCompoundLabelImpl
  /**
   ** Compute and return the location of the icons origin, the location of
   ** origin of the text baseline, and a possibly clipped version of the
   ** compound labels string.
   ** <p>
   ** Locations are computed relative to the viewR rectangle. This
   ** layoutCompoundLabel() does not know how to handle LEADING/TRAILING values
   ** in horizontalTextPosition (they will default to RIGHT) and in
   ** horizontalAlignment (they will default to CENTER). Use the other version
   ** of layoutCompoundLabel() instead.
   **
   ** @param  c                  the {@link JComponent} the lable needs to be
   **                            layouted for.
   ** @param  fm                 a {@link FontMetrics} object to compute with.
   ** @param  text               the text of the lable to layout.
   ** @param  icon               the optional {@link Icon} to layout along with
   **                            the label.
   ** @param  horizontal         <code>true</code> if the label and icon are
   **                            layouted horizontal; <code>false</code> if
   **                            vertical layout is required.
   ** @param  verticalAlignment  the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalAlignment the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#LEFT
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#RIGHT
   **                            </ul>
   ** @param  verticalTextPosition  the vertical position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalTextPosition  the horizantal position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  viewR              the {@link Rectangle} of the view component to
   **                            compute with.
   ** @param  iconR              the {@link Rectangle} of the icon component to
   **                            compute with.
   ** @param  textR              the {@link Rectangle} of the text component to
   **                            compute with.
   ** @param  textIconGap        the gap between icon and text to compute with.
   **
   ** @return                    the location of the icons origin, the location
   **                            of origin of the text baseline, and a possibly
   **                            clipped version of the compound labels string
   **                            for a horizontal or vertical layouted
   **                            component.
   */
  private static String layoutCompoundLabelImpl(final JComponent c, final FontMetrics fm, final String text, final Icon icon, final boolean horizontal, final int verticalAlignment, final int horizontalAlignment, final int verticalTextPosition, final int horizontalTextPosition, final Rectangle viewR, final Rectangle iconR, final Rectangle textR, final int textIconGap) {
    if (horizontal)
      return layoutCompoundLabelImplHorizontal(c, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap);
    else
      return layoutCompoundLabelImplVertical(c, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layoutCompoundLabelImplHorizontal
  /**
   ** Compute and return the location of the icons origin, the location of
   ** origin of the text baseline, and a possibly clipped version of the
   ** compound labels string for a horizontal layouted component.
   ** <p>
   ** Locations are computed relative to the viewR rectangle. This
   ** layoutCompoundLabel() does not know how to handle LEADING/TRAILING values
   ** in horizontalTextPosition (they will default to RIGHT) and in
   ** horizontalAlignment (they will default to CENTER).
   **
   ** @param  c                  the {@link JComponent} the lable needs to be
   **                            layouted for.
   ** @param  fm                 a {@link FontMetrics} object to compute with.
   ** @param  text               the text of the lable to layout.
   ** @param  icon               the optional {@link Icon} to layout along with
   **                            the label.
   ** @param  verticalAlignment  the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalAlignment the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#LEFT
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#RIGHT
   **                            </ul>
   ** @param  verticalTextPosition  the vertical position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalTextPosition  the horizantal position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  viewR              the {@link Rectangle} of the view component to
   **                            compute with.
   ** @param  iconR              the {@link Rectangle} of the icon component to
   **                            compute with.
   ** @param  textR              the {@link Rectangle} of the text component to
   **                            compute with.
   ** @param  textIconGap        the gap between icon and text to compute with.
   **
   ** @return                    the location of the icons origin, the location
   **                            of origin of the text baseline, and a possibly
   **                            clipped version of the compound labels string
   **                            for a horizontal layouted component.
   */
  private static String layoutCompoundLabelImplHorizontal(JComponent c, FontMetrics fm, String text, Icon icon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR, int textIconGap) {
    // Initialize the icon bounds rectangle iconR.
    if (icon != null) {
      iconR.width  = icon.getIconWidth();
      iconR.height = icon.getIconHeight();
    }
    else {
      iconR.width  = 0;
      iconR.height = 0;
    }

    // initialize the text bounds rectangle textR.
    // if a null or and empty String was specified we substitute "" here and use
    // 0,0,0,0 for textR.
    boolean textIsEmpty = (text == null) || text.equals("");

    View v = null;
    if (textIsEmpty) {
      textR.width  = 0;
      textR.height = 0;
      text = "";
    }
    else {
      v = (c != null) ? (View)c.getClientProperty("html") : null;
      if (v != null) {
        textR.width  = (int)v.getPreferredSpan(View.X_AXIS);
        textR.height = (int)v.getPreferredSpan(View.Y_AXIS);
      }
      else {
        if (false) { // TODO: debug switch
          // in this case, we will wrap the text into two lines
          boolean wrapText = (verticalTextPosition == SwingConstants.BOTTOM && horizontalTextPosition == SwingConstants.CENTER);
          if (wrapText) {
            textR.width  = SwingUtilities.computeStringWidth(fm, longestWord(text));
            // gap between the two lines is 2
            textR.height = fm.getHeight() + fm.getAscent() + 2;
          }
          else {
            // add an extra pixel at the end of the text
            textR.width  = SwingUtilities.computeStringWidth(fm, text) + 1;
            textR.height = fm.getHeight();
          }
        }
        else {
          // add an extra pixel at the end of the text
          textR.width  = SwingUtilities.computeStringWidth(fm, text);
          textR.height = fm.getHeight();
        }
      }
    }

    // unless both text and icon are non-null, we effectively ignore the value
    // of textIconGap. The code that follows uses the value of gap instead of
    // textIconGap.
    int gap = (textIsEmpty || (icon == null)) ? 0 : textIconGap;

    if (!textIsEmpty) {
      // if the label text string is too wide to fit within the available space
      // "..." and as many characters as will fit will be displayed instead
      int availTextWidth;
      if (horizontalTextPosition == SwingConstants.CENTER)
        availTextWidth = viewR.width;
      else
        availTextWidth = viewR.width - (iconR.width + gap);

      if (textR.width > availTextWidth) {
        if (v != null) {
          textR.width = availTextWidth;
        }
        else {
          String clipString = "...";
          int totalWidth = SwingUtilities.computeStringWidth(fm, clipString);
          int nChars;
          for (nChars = 0; nChars < text.length(); nChars++) {
            totalWidth += fm.charWidth(text.charAt(nChars));
            if (totalWidth > availTextWidth)
              break;
          }
          text = text.substring(0, nChars) + clipString;
          textR.width = SwingUtilities.computeStringWidth(fm, text);
        }
      }
    }

    // Compute textR.x,y given the verticalTextPosition and
    // horizontalTextPosition properties
    if (verticalTextPosition == SwingConstants.TOP) {
      if (horizontalTextPosition != SwingConstants.CENTER)
        textR.y = 0;
      else
        textR.y = -(textR.height + gap);
    }
    else if (verticalTextPosition == SwingConstants.CENTER)
      textR.y = (iconR.height >> 1) - (textR.height >> 1);
    // (verticalTextPosition == BOTTOM)
    else {
      if (horizontalTextPosition != SwingConstants.CENTER)
        textR.y = iconR.height - textR.height;
      else
        textR.y = (iconR.height + gap);
    }

    if (horizontalTextPosition == SwingConstants.LEFT)
      textR.x = -(textR.width + gap);
    else if (horizontalTextPosition == SwingConstants.CENTER)
      textR.x = (iconR.width >> 1) - (textR.width >> 1);
    // (horizontalTextPosition == RIGHT)
    else
      textR.x = (iconR.width + gap);

    // labelR is the rectangle that contains iconR and textR
    // move it to its proper position given the labelAlignment properties.
    // to avoid actually allocating a Rectangle, Rectangle.union has been
    // inlined below.
    int labelR_x      = Math.min(iconR.x, textR.x);
    int labelR_width  = Math.max(iconR.x + iconR.width, textR.x + textR.width) - labelR_x;
    int labelR_y      = Math.min(iconR.y, textR.y);
    int labelR_height = Math.max(iconR.y + iconR.height, textR.y + textR.height) - labelR_y;

    int dx, dy;
    if (verticalAlignment == SwingConstants.TOP)
      dy = viewR.y - labelR_y;
    else if (verticalAlignment == SwingConstants.CENTER)
      dy = (viewR.y + (viewR.height >> 1)) - (labelR_y + (labelR_height >> 1));
    // (verticalAlignment == BOTTOM)
    else
      dy = (viewR.y + viewR.height) - (labelR_y + labelR_height);

    if (horizontalAlignment == SwingConstants.LEFT)
      dx = viewR.x - labelR_x;
    else if (horizontalAlignment == SwingConstants.RIGHT)
      dx = (viewR.x + viewR.width) - (labelR_x + labelR_width);
    // (horizontalAlignment == CENTER)
    else
      dx = (viewR.x + (viewR.width >> 1)) - (labelR_x + (labelR_width >> 1));

    // Translate textR and glypyR by dx,dy.
    textR.x += dx;
    textR.y += dy;
    iconR.x += dx;
    iconR.y += dy;

    return text;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layoutCompoundLabelImplVertical
  /**
   ** Compute and return the location of the icons origin, the location of
   ** origin of the text baseline, and a possibly clipped version of the
   ** compound labels string for a vertical layouted component.
   ** <p>
   ** Locations are computed relative to the viewR rectangle. This
   ** layoutCompoundLabel() does not know how to handle LEADING/TRAILING values
   ** in horizontalTextPosition (they will default to RIGHT) and in
   ** horizontalAlignment (they will default to CENTER).
   **
   ** @param  c                  the {@link JComponent} the lable needs to be
   **                            layouted for.
   ** @param  fm                 a {@link FontMetrics} object to compute with.
   ** @param  text               the text of the lable to layout.
   ** @param  icon               the optional {@link Icon} to layout along with
   **                            the label.
   ** @param  verticalAlignment  the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalAlignment the vertical alignment of the base line of the
   **                            label.
   **                            <ul>
   **                              <li>SwingConstants#LEFT
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#RIGHT
   **                            </ul>
   ** @param  verticalTextPosition  the vertical position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  horizontalTextPosition  the horizantal position of the label text.
   **                            <ul>
   **                              <li>SwingConstants#TOP
   **                              <li>SwingConstants#CENTER
   **                              <li>SwingConstants#BOTTOM
   **                            </ul>
   ** @param  viewR              the {@link Rectangle} of the view component to
   **                            compute with.
   ** @param  iconR              the {@link Rectangle} of the icon component to
   **                            compute with.
   ** @param  textR              the {@link Rectangle} of the text component to
   **                            compute with.
   ** @param  textIconGap        the gap between icon and text to compute with.
   **
   ** @return                    the location of the icons origin, the location
   **                            of origin of the text baseline, and a possibly
   **                            clipped version of the compound labels string
   **                            for a vertical layouted component.
   */
  private static String layoutCompoundLabelImplVertical(JComponent c, FontMetrics fm, String text, Icon icon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR, int textIconGap) {
    // Initialize the icon bounds rectangle iconR
    if (icon != null) {
      iconR.width  = icon.getIconWidth();
      iconR.height = icon.getIconHeight();
    }
    else {
      iconR.width  = 0;
      iconR.height = 0;
    }

    // Initialize the text bounds rectangle textR
    // if a null or and empty String was specified we substitute "" here and use
    // 0,0,0,0 for textR.
    boolean textIsEmpty = (text == null) || text.equals("");

    View v = null;
    if (textIsEmpty) {
      textR.width  = 0;
      textR.height = 0;
      text         = "";
    }
    else {
      v = (c != null) ? (View)c.getClientProperty("html") : null;
      if (v != null) {
        textR.height = (int)v.getPreferredSpan(View.X_AXIS);
        textR.width  = (int)v.getPreferredSpan(View.Y_AXIS);
      }
      else {
        textR.height = SwingUtilities.computeStringWidth(fm, text);
        textR.width  = fm.getHeight();
      }
    }

    // Unless both text and icon are non-null, we effectively ignore the value
    // of textIconGap. The code that follows uses the value of gap instead of
    // textIconGap.
    int gap = (textIsEmpty || (icon == null)) ? 0 : textIconGap;

    if (!textIsEmpty) {
      // If the label text string is too wide to fit within the available space
      // "..." and as many characters as will fit will be displayed instead.
      int availTextHeight;
      if (horizontalTextPosition == SwingConstants.CENTER)
        availTextHeight = viewR.height;
      else
        availTextHeight = viewR.height - (iconR.height + gap);

      if (textR.height > availTextHeight) {
        if (v != null) {
          textR.height = availTextHeight;
        }
        else {
          String clipString = "...";
          int totalHeight = SwingUtilities.computeStringWidth(fm, clipString);
          int nChars;
          for (nChars = 0; nChars < text.length(); nChars++) {
            totalHeight += fm.charWidth(text.charAt(nChars));
            if (totalHeight > availTextHeight)
              break;
          }
          text = text.substring(0, nChars) + clipString;
          textR.height = SwingUtilities.computeStringWidth(fm, text);
        }
      }
    }

    // Compute textR.x,y given the verticalTextPosition and
    // horizontalTextPosition properties
    if (verticalTextPosition == SwingConstants.TOP) {
      if (horizontalTextPosition != SwingConstants.CENTER)
        textR.x = 0;
      else
        textR.x = -(textR.width + gap);
    }
    else if (verticalTextPosition == SwingConstants.CENTER)
      textR.y = (iconR.width >> 1) - (textR.width >> 1);
    // (verticalTextPosition == BOTTOM)
    else {
      if (horizontalTextPosition != SwingConstants.CENTER)
        textR.x = iconR.width - textR.width;
      else
        textR.x = (iconR.width + gap);
    }

    if (horizontalTextPosition == SwingConstants.LEFT)
      textR.y = -(textR.height + gap);
    else if (horizontalTextPosition == SwingConstants.CENTER)
      textR.y = (iconR.height >> 1) - (textR.height >> 1);
    // (horizontalTextPosition == RIGHT)
    else
      textR.y = (iconR.height + gap);

    // labelR is the rectangle that contains iconR and textR
    // move it to its proper position given the labelAlignment properties
    // to avoid actually allocating a Rectangle, Rectangle.union has been
    // inlined below.
    int labelR_x      = Math.min(iconR.y, textR.y);
    int labelR_width  = Math.max(iconR.y + iconR.height, textR.y + textR.height) - labelR_x;
    int labelR_y      = Math.min(iconR.x, textR.x);
    int labelR_height = Math.max(iconR.x + iconR.width, textR.x + textR.width) - labelR_y;

    // because we will rotate icon, so the position will be different from text.
    // However after transform, they will be same
    int dIcony;
    int dx, dy;
    if (verticalAlignment == SwingConstants.TOP) {
      dy = viewR.x - labelR_y;
      dIcony = (viewR.x + viewR.width) - (labelR_y + labelR_height);
    }
    else if (verticalAlignment == SwingConstants.CENTER) {
      dy = (viewR.x + (viewR.width >> 1)) - (labelR_y + (labelR_height >> 1));
      dIcony = dy;
    }
    // (verticalAlignment == BOTTOM)
    else {
      dy = (viewR.x + viewR.width) - (labelR_y + labelR_height);
      dIcony = viewR.x - labelR_y;
    }

    if (horizontalAlignment == SwingConstants.LEFT)
      dx = viewR.y - labelR_x;
    else if (horizontalAlignment == SwingConstants.RIGHT)
      dx = (viewR.y + viewR.height) - (labelR_x + labelR_width);
    // (horizontalAlignment == CENTER)
    else
      dx = (viewR.y + (viewR.height >> 1)) - (labelR_x + (labelR_width >> 1));

    // Translate textR and iconR by dx,dy.
    textR.y += dx;
    textR.x += dy;
    iconR.y += dx;
    iconR.x += dIcony;

    return text;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longestWord
  private static String longestWord(String text) {
    if (text.indexOf(' ') == -1)
      return text;

    int minDiff = text.length();
    int minPos = -1;
    int mid = text.length() / 2;

    int pos = -1;
    while (true) {
      pos = text.indexOf(' ', pos + 1);
      if (pos == -1)
        break;

      int diff = Math.abs(pos - mid);
      if (diff < minDiff) {
        minDiff = diff;
        minPos = pos;
      }
    }
    return minPos >= mid ? text.substring(0, minPos) : text.substring(minPos + 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renderingHints
  /**
   ** Returns rendering hints from a Graphics instance.
   ** <p>
   ** "hintsToSave" is a Map of RenderingHint key-values. For each hint key
   ** present in that map, the value of that hint is obtained from the Graphics
   ** and stored as the value for the key in savedHints.
   */
  private static RenderingHints renderingHints(final Graphics2D canvas, final RenderingHints hintsToSave, final RenderingHints savedHints) {
    if (savedHints != null)
//      savedHints = new RenderingHints(null);
//    else
      savedHints.clear();

    if (hintsToSave == null || hintsToSave.size() == 0)
      return savedHints;

    // RenderingHints.keySet() returns Set
    final Set<Object> objects = hintsToSave.keySet();
    for (Object o : objects) {
      RenderingHints.Key key = (RenderingHints.Key)o;
      Object value = canvas.getRenderingHint(key);
      savedHints.put(key, value);
    }

    return savedHints;
  }
}
