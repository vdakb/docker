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

    File        :   PanelAccordion.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PanelAccordion.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.JComponent;

////////////////////////////////////////////////////////////////////////////////
// class PanelAccordion
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A <code>PanelAccordion</code> is the visual componemt to expamd and collapse
 ** panel like nodes in a tree.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class PanelAccordion extends JComponent {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6996351206500103025")
  private static final long serialVersionUID = 8971395679768720930L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean           expanded;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PanelAccordion</code>.
   */
  public PanelAccordion() {
    super();

    setBackground(UIManager.getColor("Tree.background"));
    addMouseListener(new PanelAccordionHandler(this));
    setFocusable(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expand
  /**
   ** Sets the state thats the component represents.
   **
   ** @param  state             <code>true</code> if the state of this
   **                           component is expanded, otherwiese
   **                           <code>flase</code>.
   */
  public void expand(final boolean state) {
    if (state == this.expanded)
      return;

    this.expanded = state;
    repaint();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expanded
  /**
   ** Returns the state thats the component represents.
   **
   ** @return                   <code>true</code> if the state of this
   **                           component is expanded, otherwiese
   **                           <code>flase</code>.
   */
  public boolean expanded() {
    return this.expanded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns an <code>Icon</code> from the defaults that represents the state
   ** of the componenent.
   ** <p>
   ** The returned value is:
   ** <table summary="">
   ** <tr>
   ** <th>steta</th><th>Icon</th>
   ** </tr>
   ** <tr>
   ** <td>expanded</td><td>Tree.expandedIcon</td>
   ** </tr>
   ** <tr>
   ** <td>collapsed</td><td>Tree.collapsedIcon</td>
   ** </tr>
   ** </table>
   **
   ** @return                    the <code>Icon</code> object for the state
   */
  protected Icon icon() {
    return UIManager.getIcon(this.expanded ? "Tree.expandedIcon" : "Tree.collapsedIcon");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaximumSize (overridden)
  /**
   ** Return the same dimensionn as {@link #getPreferredSize()} due uo this
   ** component doesm't grow.
   **
   ** @return                    the value of the <code>maximumSize</code>
   **                            property.
   */
  @Override
  public Dimension getMaximumSize() {
     return getPreferredSize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredSize (overridden)
  /**
   ** If the <code>preferredSize</code> has been set to a non-<code>null</code>
   ** value just returns it with the respects to the embedded editor component.
   ** If the UI delegate's <code>getPreferredSize</code> method returns a non
   ** <code>null</code> value then return that; otherwise defer to the
   ** component's layout manager.
   **
   ** @return                    the value of the <code>preferredSize</code>
   **                            property
   */
  @Override
  public Dimension getPreferredSize() {
    final Icon   icon   = icon();
    final Insets insets = getInsets();
    return new Dimension(icon.getIconWidth() + insets.left + insets.right, icon.getIconHeight() + insets.top + insets.bottom);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggle
  /**
   ** Toggles (expand/collapse) the state of the associated panels
   */
  public void toggle() {
    expand(!expanded());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   paintComponent (overridden)
  /**
   ** Calls the UI delegate's paint method, if the UI delegate is
   ** non-<code>null</code>.
   ** <p>
   ** We pass the delegate a copy of the {@link Graphics} object to protect the
   ** rest of the paint code from irrevocable changes
   ** (for example, <code>Graphics.translate</code>).
   ** <p>
   ** If you override this in a subclass you should not make permanent changes
   ** to the passed in {@link Graphics}. For example, you should not alter the
   ** clip <code>Rectangle</code> or modify the transform. If you need to do
   ** these operations you may find it easier to create a new {@link Graphics}
   ** from the passed in {@link Graphics} and manipulate it. Further, if you do
   ** not invoker super's implementation you must honor the opaque property,
   ** that is if this component is opaque, you must completely fill in the
   ** background in a non-opaque color. If you do not honor the opaque property
   ** you will likely see visual artifacts.
   ** <p>
   ** The passed in <code>Graphics</code> object might have a transform other
   ** than the identify transform installed on it.  In this case, you might get
   ** unexpected results if you cumulatively apply another transform.
   **
   ** @param  canvas             the {@link Graphics} object to protect
   */
  @Override
  protected void paintComponent(final Graphics canvas) {
    // ensure inheritance
    super.paintComponent(canvas);

    final Icon   icon   = icon();
    final Insets insets = getInsets();
    icon.paintIcon(this, canvas, insets.left + (getWidth() - icon.getIconWidth()) / 2, insets.top + (getHeight() - icon.getIconHeight()) / 2);
  }
}