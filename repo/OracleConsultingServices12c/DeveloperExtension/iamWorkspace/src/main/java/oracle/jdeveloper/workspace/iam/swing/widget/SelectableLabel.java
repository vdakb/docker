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

    File        :   SelectableLabel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SelectableLabel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.SwingConstants;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import oracle.ide.controls.LabelColorScheme;
import oracle.ide.controls.DefaultLabelColorScheme;

import oracle.jdeveloper.workspace.iam.swing.Selectable;

////////////////////////////////////////////////////////////////////////////////
// class SelectableLabel
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An implementation of a label that have an additional border of three pixels
 ** on the left and on the right side.
 ** <p>
 ** The indended use is in JTrees and JList in compination with check boxes to
 ** adjust the vertical alignment.
 ** <p>
 ** This widget also extends the capabilities of the standard label to visualize
 ** the state of selected and/or focused.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class SelectableLabel extends JLabel implements Selectable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final Border      adjustedBorder     = new EmptyBorder(0, 3, 0, 3);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6468117322103460636")
  private static final long         serialVersionUID    = -6110509914940830050L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient boolean          focused            = false;
  private transient boolean          selected           = false;
  private transient LabelColorScheme colorScheme        = new DefaultLabelColorScheme();

  private transient int              verticalAdjustment = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SelectableLabel</code> instance with no image and with
   ** an empty string for the title.
   ** <p>
   ** The label is centered vertically in its display area. The label's
   ** contents, once set, will be displayed on the leading edge of the label's
   ** display area.
   */
  public SelectableLabel() {
    // ensure inheritance
    super();

    // intialize UI
    setBorder(adjustedBorder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NullLabel</code> instance with the specified text.
   ** <p>
   ** The label is aligned against the leading edge of its display area, and
   ** centered vertically.
   **
   ** @param  text                the text to be displayed by the label.
   */
  public SelectableLabel(final String text) {
    // ensure inheritance
    this(text, null, SwingConstants.LEADING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SelectableLabel</code> instance with the specified text
   ** and horizontal alignment.
   ** <p>
   ** The label is centered vertically in its display area.
   **
   ** @param  text                the text to be displayed by the label.
   ** @param  horizontalAlignment one of the following constants defined in
   **                             <code>SwingConstants</code>:
   **                             <ul>
   **                               <li><code>LEFT</code>
   **                               <li><code>CENTER</code>
   **                               <li><code>RIGHT</code>
   **                               <li><code>LEADING</code>
   **                               <li><code>TRAILING</code>
   **                             </ul>.
   */
  public SelectableLabel(final String text, final int horizontalAlignment) {
    // ensure inheritance
    this(text, null, horizontalAlignment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SelectableLabel</code> instance with the specified
   ** text, image, and horizontal alignment.
   ** <p>
   ** The label is centered vertically in its display area. The text is on the
   ** trailing edge of the image.
   **
   ** @param  text                the text to be displayed by the label.
   ** @param  icon                the image to be displayed by the label.
   ** @param  horizontalAlignment one of the following constants defined in
   **                             <code>SwingConstants</code>:
   **                             <ul>
   **                               <li><code>LEFT</code>
   **                               <li><code>CENTER</code>
   **                               <li><code>RIGHT</code>
   **                               <li><code>LEADING</code>
   **                               <li><code>TRAILING</code>
   **                             </ul>.
   */
  public SelectableLabel(final String text, final Icon icon, final int horizontalAlignment) {
    // ensure inheritance
    super(text, icon, horizontalAlignment);

    // intialize UI
    setBorder(adjustedBorder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SelectableLabel</code> instance with the specified
   ** image.
   ** <p>
   ** The label is aligned against the leading edge of its display area, and
   ** centered vertically.
   **
   ** @param  icon                the image to be displayed by the label.
   */
  public SelectableLabel(final Icon icon) {
    // ensure inheritance
    this(icon, SwingConstants.LEADING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SelectableLabel</code> instance with the specified
   ** image and horizontal alignment.
   ** <p>
   ** The label is centered vertically in its display area. The text is on the
   ** trailing edge of the image.
   **
   ** @param  icon                the image to be displayed by the label.
   ** @param  horizontalAlignment one of the following constants defined in
   **                             <code>SwingConstants</code>:
   **                             <ul>
   **                               <li><code>LEFT</code>
   **                               <li><code>CENTER</code>
   **                               <li><code>RIGHT</code>
   **                               <li><code>LEADING</code>
   **                               <li><code>TRAILING</code>
   **                             </ul>.
   */
  public SelectableLabel(final Icon icon, final int horizontalAlignment) {
    // ensure inheritance
    super(icon, horizontalAlignment);

    // intialize UI
    setBorder(adjustedBorder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verticalAdjustment
  /**
   ** Sets the adjusmnet of the label's contents along the Y axis.
   ** <p>
   ** The default value of this property is <code>0</code>.
   **
   ** @param  adjustment         One of the following constants
   */
  public void verticalAdjustment(final int adjustment) {
    this.verticalAdjustment = adjustment;
    super.setVerticalAlignment(this.verticalAdjustment == 0 ? SwingConstants.CENTER : SwingConstants.BOTTOM);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verticalAdjustment
  /**
   ** Returns the adjusmnet of the label's contents along the Y axis.
   ** <p>
   ** The default value of this property is <code>0</code>.
   **
   ** @return                    the adjusmnet of the label's contents along the
   **                            Y axis.
   */
  public boolean verticalAdjustment() {
    return this.focused;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   focused
  /**
   ** Sets whether the label should reflect that its focused or not.
   **
   ** @param  state              <code>true</code> if the label should reflect
   **                            that its focused, otherwise <code>false</code>.
   */
  public void focused(final boolean state) {
    this.focused = state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   focused
  /**
   ** Returns whether this label has the focus or not.
   ** <p>
   ** <code>true</code> if the label reflects focused, <code>false</code> if
   ** it's not.
   **
   ** @return                    <code>true</code> if the label selected,
   **                            otherwise <code>false</code>.
   */
  public boolean focused() {
    return this.focused;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnabled (overridden)
  /**
   ** Sets whether or not this label appears enabled.
   **
   ** @param  state              <code>true</code> if this label should
   **                            appear enabled, <code>false</code> otherwise.
   */
  @Override
  public void setEnabled(final boolean state) {
    super.setEnabled(state);
    updateAppearance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredSize (overridden)
  /**
   ** If the <code>preferredSize</code> has been set to a
   ** non-<code>null</code> value just returns it regarding the adjusmant of the
   ** height.
   ** <p>
   ** If the UI delegate's <code>getPreferredSize</code> method returns a non
   ** <code>null</code> value then return that; otherwise defer to the
   ** component's layout manager.
   **
   ** @return                    the value of the <code>preferredSize</code>
   **                            property.
   */
  @Override
  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    size.height += this.verticalAdjustment;
    return size;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected (Selectable)
  /**
   ** Returns the selection state of the label.
   ** <p>
   ** <code>true</code> if the label reflects selected, <code>false</code> if
   ** it's not.
   **
   ** @return                    <code>true</code> if the label selected,
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean selected() {
    return this.selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected (Selectable)
  /**
   ** Sets the selection state of the label.
   ** <p>
   ** Note that this method does not trigger an <code>actionEvent</code>.
   **
   ** @param  state              <code>true</code> if the label selected,
   **                            otherwise <code>false</code>.
   */
  @Override
  public void selected(final boolean state) {
    this.selected = state;
    updateAppearance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggle (Selectable)
  /**
   ** Toggles the selection status.
   */
  @Override
  public void toggle() {
    selected(!this.selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled
  /**
   ** Enable selection change.
   ** <p>
   ** Enabled <code>false</code> doesn't mean selected is <code>false</code>. If
   ** it is selected before, enableed(false) won't make selected become
   ** <code>false</code>. In the other word, enabled won't change the value
   ** of Selected().
   **
   ** @param  enabled            <code>true</code> if it is enabled; otherwise
   **                            <code>false</code>.
   */
  @Override
  public void enabled(final boolean enabled) {
    setEnabled(enabled);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled
  /**
   ** Determines if selection change is allowed.
   **
   ** @return                    <code>true</code> selection change is allowed;
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean enabled() {
    return super.isEnabled();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateUI (overridden)
  /**
   ** Resets the UI property to a value from the current look and feel.
   */
  @Override
  public void updateUI() {
    super.updateUI();
    if (this.colorScheme != null)
      this.colorScheme.updateUI();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   paint (overridden)
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
    // evaluate the rectangle to drwa in
    final Rectangle drawRect = new Rectangle(0, this.verticalAdjustment, getWidth(), getHeight() - this.verticalAdjustment);

    // draw the selected state
    if (this.selected()) {
      context.setColor(getBackground());
      context.fillRect(drawRect.x, drawRect.y, drawRect.width, drawRect.height);
    }

    // draw the focused state
    if (this.focused()) {
      Border border = UIManager.getBorder("Tree.focusBorder");
      if (border != null) {
        context.setColor(getForeground());
        border.paintBorder(this, context, drawRect.x, drawRect.y, drawRect.width - 1, drawRect.height);
      }
      else {
        context.setColor(isEnabled() ? this.colorScheme.getBorderSelectionColor() : this.colorScheme.getBackgroundSelectionColor());
        context.drawRect(drawRect.x, drawRect.y, drawRect.width - 1, drawRect.height -1 );
      }
    }
    super.paint(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateAppearance
  /**
   ** Updates the UI property to a value from the current collor scheme.
   */
  protected void updateAppearance() {
    if (selected()) {
      setForeground(this.colorScheme.getTextSelectionColor());
      setBackground(this.colorScheme.getBackgroundSelectionColor());
    }
    else if (!isEnabled()) {
      setForeground(Color.gray);
      setBackground(this.colorScheme.getBackgroundNonSelectionColor());
    }
    else {
      setForeground(this.colorScheme.getTextNonSelectionColor());
      setBackground(this.colorScheme.getBackgroundNonSelectionColor());
    }
  }
}