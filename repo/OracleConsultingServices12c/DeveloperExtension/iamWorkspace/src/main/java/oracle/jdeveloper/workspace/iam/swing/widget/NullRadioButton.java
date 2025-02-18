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

    File        :   NullRadioButton.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    NullRadioButton.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.Icon;
import javax.swing.Action;
import javax.swing.JRadioButton;

////////////////////////////////////////////////////////////////////////////////
// class NullRadioButton
// ~~~~~ ~~~~~~~~~~~~~~~
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
public class NullRadioButton extends JRadioButton {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7333087304244978232")
  private static final long serialVersionUID = -4370962744374021843L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with no text.
   */
  public NullRadioButton() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with text.
   **
   ** @param  text               the text of the radio button.
   */
  public NullRadioButton(final String text) {
    // ensure inheritance
    super(text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially selected radio button with text and specifies
   ** whether or not it is initially selected.
   **
   ** @param  text               the text of the radio button.
   ** @param  selected           a boolean value indicating the initial
   **                            selection state. If <code>true</code> the check
   **                            box is selected.
   */
  public NullRadioButton(final String text, final boolean selected) {
    // ensure inheritance
    super(text, selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with the specified image
   ** but no text.
   **
   ** @param  icon               the {@link Icon} image to display.
   */
  public NullRadioButton(final Icon icon) {
    // ensure inheritance
    super(icon);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially selected radio button the specified image but no
   ** text and specifies whether or not it is initially selected.
   **
   ** @param  icon               the {@link Icon} image to display.
   ** @param  selected           a boolean value indicating the initial
   **                            selection state. If <code>true</code> the check
   **                            box is selected.
   */
  public NullRadioButton(final Icon icon, final boolean selected) {
    // ensure inheritance
    super(icon, selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with the specified text
   ** and image.
   **
   ** @param  text               the text of the radio button.
   ** @param  icon               the {@link Icon} image to display.
   */
  public NullRadioButton(final String text, final Icon icon) {
    // ensure inheritance
    super(text, icon);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with the text and image
   ** and specifies whether or not it is initially selected.
   ** .
   **
   ** @param  text               the text of the radio button.
   ** @param  icon               the {@link Icon} image to display.
   ** @param  selected           a boolean value indicating the initial
   **                            selection state. If <code>true</code> the check
   **                            box is selected.
   */
  public NullRadioButton(String text, Icon icon, boolean selected) {
    // ensure inheritance
    super(text, icon, selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a radio button where properties are taken from the {@link Action}
   ** supplied.
   **
   ** @param  action             the {@link Action} to bind to the control.
   */
  public NullRadioButton(final Action action) {
    // ensure inheritance
    super(action);
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