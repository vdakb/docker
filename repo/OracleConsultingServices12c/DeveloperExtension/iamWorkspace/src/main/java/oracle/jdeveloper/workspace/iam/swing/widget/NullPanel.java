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

    File        :   NullPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    NullPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.LayoutManager;

import javax.swing.JPanel;

////////////////////////////////////////////////////////////////////////////////
// class NullPanel
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
public class NullPanel extends JPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5999781850226637777")
  private static final long serialVersionUID = -2031956125367937522L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new buffered <code>NullPanel</code> with a flow layout.
   */
  public NullPanel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new buffered <code>NullPanel</code> with
   ** <code>FlowLayout</code> and the specified buffering strategy.
   ** <p>
   ** If <code>isDoubleBuffered</code> is <code>true</code>, the
   ** <code>NullPanel</code> will use a double buffer.
   **
   ** @param  isDoubleBuffered   a boolean, <code>true</code> for
   **                            double-buffering, which uses additional memory
   **                            space to achieve fast, flicker-free updates.
   */
  public NullPanel(final boolean isDoubleBuffered) {
    // ensure inheritance
    super(isDoubleBuffered);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new buffered <code>NullPanel</code> with the specified layout
   ** manager.
   **
   ** @param  layout             the {@link LayoutManager} to use.
   */
  public NullPanel(final LayoutManager layout) {
    // ensure inheritance
    super(layout);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>NullPanel</code> with the specified layout manager
   ** and buffering strategy.
   **
   ** @param  layout             the {@link LayoutManager} to use.
   ** @param  isDoubleBuffered   a boolean, <code>true</code> for
   **                            double-buffering, which uses additional memory
   **                            space to achieve fast, flicker-free updates.
   */
  public NullPanel(final LayoutManager layout, boolean isDoubleBuffered) {
    // ensure inheritance
    super(layout, isDoubleBuffered);
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