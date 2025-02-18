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

    File        :   LabelContainer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LabelContainer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import javax.swing.SwingConstants;

import oracle.jdeveloper.workspace.iam.swing.LabelFactory;

////////////////////////////////////////////////////////////////////////////////
// class LabelContainer
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A wraper that renders the label of a parameter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class LabelContainer extends Box {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7402360549723561538")
  private static final long serialVersionUID = -3144880622304930405L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient JLabel  label;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LabelContainer</code> instance with no image and with
   ** an empty string for the title.
   ** <p>
   ** The label is centered vertically in its display area. The label's
   ** contents, once set, will be displayed on the leading edge of the label's
   ** display area.
   */
  public LabelContainer() {
    // ensure inheritance
    this(new JLabel());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LabelContainer</code> that component is laid out in the
   ** direction of a line of text as determined by the
   ** <code>ComponentOrientation</code> property.
   **
   ** @param  label              the {@link JLabel} this component renders.
   */
  private LabelContainer(final JLabel label) {
    // ensure inheritance
    super(BoxLayout.LINE_AXIS);

    // initialize instance
    this.label = label;

    // initialize UI
    add(this.label);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the {@link JLabel} this component renders.
   **
   ** @return                    the {@link JLabel} this component renders.
   */
  public final JLabel label() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  text               the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel build(final String text) {
    return new LabelContainer(LabelFactory.label(text)).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link JLabel} with the specified text and
   ** horizontal alignment.
   ** <p>
   ** The label is centered vertically in its display area.
   **
   ** @param  text               the text to be displayed by the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  alignment          one of the following constants defined in
   **                            <code>SwingConstants</code>:
   **                            <ul>
   **                              <li><code>LEFT</code>
   **                              <li><code>CENTER</code>
   **                              <li><code>RIGHT</code>
   **                              <li><code>LEADING</code>
   **                              <li><code>TRAILING</code>
   **                            </ul>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static JLabel build(final String text, final int alignment) {
    return new LabelContainer(LabelFactory.label(text, alignment)).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link JLabel} with the specified image.
   ** <p>
   ** The label is aligned against the leading edge of its display area, and
   ** centered vertically.
   **
   ** @param  icon               the image to be displayed by the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   */
  public static JLabel build(final Icon icon) {
    return new LabelContainer(LabelFactory.label(icon)).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link JLabel} with the specified image and
   ** horizontal alignment.
   ** <p>
   ** The label is centered vertically in its display area. The text is on the
   ** trailing edge of the image.
   **
   ** @param  icon               the image to be displayed by the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   ** @param  alignment          one of the following constants:
   **                            <ul>
   **                              <li>{@link SwingConstants#LEFT}
   **                              <li>{@link SwingConstants#CENTER}
   **                              <li>{@link SwingConstants#RIGHT}
   **                              <li>{@link SwingConstants#LEADING}
   **                              <li>{@link SwingConstants#TRAILING}
   **                            </ul>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static JLabel build(final Icon icon, final int alignment) {
    return new LabelContainer(LabelFactory.label(icon, alignment)).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link JLabel} with the specified text, image
   ** and horizontal alignment.
   ** <p>
   ** The label is centered vertically in its display area. The text is on the
   ** leading edge of the image.
   **
   ** @param  text               the text to be displayed by the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  icon               the image to be displayed by the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   */
  public static JLabel build(final String text, final Icon icon) {
    return new LabelContainer(LabelFactory.label(text, icon)).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link JLabel} with the specified text,
   ** image, and horizontal alignment.
   ** <p>
   ** The label is centered vertically in its display area. The text is on the
   ** trailing edge of the image.
   **
   ** @param  text               the text to be displayed by the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  icon               the image to be displayed by the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   ** @param  alignment          one of the following constants:
   **                             <ul>
   **                               <li>{@link SwingConstants#LEFT}
   **                               <li>{@link SwingConstants#CENTER}
   **                               <li>{@link SwingConstants#RIGHT}
   **                               <li>{@link SwingConstants#LEADING}
   **                               <li>{@link SwingConstants#TRAILING}
   **                             </ul>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static JLabel build(final String text, final Icon icon, final int alignment) {
    return new LabelContainer(LabelFactory.label(text, icon, alignment)).label();
  }
}