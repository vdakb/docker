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

    File        :   OverlayCheckBox.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OverlayCheckBox.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.Icon;
import javax.swing.Action;
import javax.swing.JCheckBox;

import oracle.jdeveloper.workspace.iam.swing.OverlayUtility;

////////////////////////////////////////////////////////////////////////////////
// class OverlayCheckBox
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An implementation of a check box that can be overlayed to another component.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OverlayCheckBox extends JCheckBox {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4149173752275904514")
  private static final long serialVersionUID = -3515854457930819478L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected check box with no text, no
   ** icon.
   */
  public OverlayCheckBox() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected check box with text.
   **
   ** @param  text               the text of the check box.
   */
  public OverlayCheckBox(String text) {
    // ensure inheritance
    super(text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially selected check box with text and specifies whether
   ** or not it is initially selected.
   **
   ** @param  text               the text of the check box.
   ** @param  selected           a boolean value indicating the initial
   **                            selection state. If <code>true</code> the check
   **                            box is selected.
   */
  public OverlayCheckBox(final String text, final boolean selected) {
    // ensure inheritance
    super(text, selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected check box with the specified image but
   ** no text.
   **
   ** @param  icon               the {@link Icon} image to display.
   */
  public OverlayCheckBox(final Icon icon) {
    // ensure inheritance
    super(icon);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially selected check box the specified image but no text
   ** and specifies whether or not it is initially selected.
   **
   ** @param  icon               the {@link Icon} image to display.
   ** @param  selected           a boolean value indicating the initial
   **                            selection state. If <code>true</code> the check
   **                            box is selected.
   */
  public OverlayCheckBox(final Icon icon, final boolean selected) {
    // ensure inheritance
    super(icon, selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected check box with the specified text and
   ** image.
   **
   ** @param  text               the text of the check box.
   ** @param  icon               the {@link Icon} image to display.
   */
  public OverlayCheckBox(final String text, final Icon icon) {
    // ensure inheritance
    super(text, icon);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected check box with the text and image and
   ** specifies whether or not it is initially selected.
   ** .
   **
   ** @param  text               the text of the check box.
   ** @param  icon               the {@link Icon} image to display.
   ** @param  selected           a boolean value indicating the initial
   **                            selection state. If <code>true</code> the check
   **                            box is selected.
   */
  public OverlayCheckBox(final String text, final Icon icon, final boolean selected) {
    // ensure inheritance
    super(text, icon, selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a check box where properties are taken from the {@link Action}
   ** supplied.
   **
   ** @param  action             the {@link Action} bound at the component.
   */
  public OverlayCheckBox(Action action) {
    // ensure inheritance
    super(action);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repaint (overridden)
  /**
   ** Adds the specified region to the dirty region list if the component is
   ** showing. The component will be repainted after all of the currently
   ** pending events have been dispatched.
   **
   ** @param  tm                 this parameter is not used
   ** @param  x                  the x value of the dirty region
   ** @param  y                  the y value of the dirty region
   ** @param  width              the width of the dirty region
   ** @param  height             the height of the dirty region
   */
  @Override
  public void repaint(long tm, int x, int y, int width, int height) {
    // ensure inheritance
    super.repaint(tm, x, y, width, height);
    // repaint the overlay
    OverlayUtility.repaint(this);
  }
}