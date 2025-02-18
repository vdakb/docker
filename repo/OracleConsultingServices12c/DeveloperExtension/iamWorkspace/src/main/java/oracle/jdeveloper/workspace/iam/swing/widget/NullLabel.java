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

    File        :   NullLabel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    NullLabel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.Icon;
import javax.swing.JLabel;

////////////////////////////////////////////////////////////////////////////////
// class NullLabel
// ~~~~~ ~~~~~~~~~
/**
 ** This is part of the null-components (Null Object Pattern).
 ** <p>
 ** A null-component doesn't have foreground, background or font value set. In
 ** the other words, the foreground, background and font value of null-component
 ** are <code>null</code>. But this doesn't mean getBackground(),
 ** getForeground() or getFont() will return <code>null</code>. According to
 ** {@link java.awt.Component#getBackground()},
 ** {@link java.awt.Component#getForeground()} and
 ** {@link java.awt.Component#getFont()}, if the value is <code>null</code>, it
 ** will get the value from its parent. In the other words, if you add a
 ** null-component to JPanel, you can use JPanel to control the background,
 ** foreground and font of this null-component. The feature is very helpful if
 ** you want to make sure all components in a JPanel has the same background,
 ** foreground or font.
 ** <p>
 ** We creates a few null-components. It doesn't cover all components. You can
 ** always create your own. All you need to do is this
 ** <pre>
 **   public class NullXxxComponent extends XxxComponent {
 **     // all the constructors
 **     ...
 **     public void updateUI() {
 **       super.updateUI();
 **      setFont(null);
 **      setBackground(null);
 **      // do not do this for JButton since JButton always paints button
 **      // content background. So it'd better to leave the foreground alone
 **      setForeground(null);
 **     }
 **     ...
 **  }
 ** </pre>
 **
 ** @see NullPanel
 ** @see NullButton
 ** @see NullLabel
 ** @see NullCheckBox
 ** @see NullRadioButton
 ** @see NullTriStateCheckBox
 ** @see NullTriStateRadioButton
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class NullLabel extends JLabel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3132246534174903121")
  private static final long serialVersionUID = -8202606445416486071L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NullLabel</code> instance with no image and with an
   ** empty string for the title.
   ** <p>
   ** The label is centered vertically in its display area. The label's
   ** contents, once set, will be displayed on the leading edge of the label's
   ** display area.
   */
  public NullLabel() {
    // ensure inheritance
    super();
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
  public NullLabel(final String text) {
    // ensure inheritance
    super(text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NullLabel</code> instance with the specified text and
   ** horizontal alignment.
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
  public NullLabel(final String text, final int horizontalAlignment) {
    // ensure inheritance
    super(text, horizontalAlignment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NullLabel</code> instance with the specified text,
   ** image, and horizontal alignment.
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
  public NullLabel(final String text, final Icon icon, final int horizontalAlignment) {
    // ensure inheritance
    super(text, icon, horizontalAlignment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NullLabel</code> instance with the specified image.
   ** <p>
   ** The label is aligned against the leading edge of its display area, and
   ** centered vertically.
   **
   ** @param  icon                the image to be displayed by the label.
   */
  public NullLabel(final Icon icon) {
    // ensure inheritance
    super(icon);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NullLabel</code> instance with the specified image and
   ** horizontal alignment.
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
  public NullLabel(final Icon icon, final int horizontalAlignment) {
    // ensure inheritance
    super(icon, horizontalAlignment);
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
    setFont(null);
    setBackground(null);
    setForeground(null);
  }
}