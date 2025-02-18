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

    File        :   LabelFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LabelFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

import java.awt.Font;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

////////////////////////////////////////////////////////////////////////////////
// class LabelFactory
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A builder class for creating labels.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class LabelFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final JLabel label = new JLabel();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LabelFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private LabelFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Return the {@link JLabel} with the properties applied so far.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
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
   ** Factory method to create a <code>LabelFactory</code> to form custom
   ** labels.
   **
   ** @return                    this <code>LabelFactory</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>LabelFactory</code>.
   */
  public static LabelFactory build() {
    return new LabelFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
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
  public static JLabel label(final String text) {
    return label(text, SwingConstants.LEADING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  text               the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final String text, final int alignment) {
    return label(text, alignment, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  text               the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   ** @param  component          the component to be associated with the label.
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final String text, final int alignment, final Component component) {
    return new LabelFactory().prompt(text).alignment(alignment).forComponent(component).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  text               the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  component          the component to be associated with the label.
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final String text, final Component component) {
    return label(text, null, component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  icon               the icon of the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final Icon icon) {
    return label(icon, SwingConstants.CENTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  icon               the icon of the label.
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
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final Icon icon, final int alignment) {
    return new LabelFactory().symbol(icon).alignment(alignment).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  icon               the icon of the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   ** @param  component          the component to be associated with the label.
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final Icon icon, final Component component) {
    return new LabelFactory().symbol(icon).forComponent(component).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  icon               the icon of the label.
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
   ** @param  component          the component to be associated with the label.
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final Icon icon, final int alignment, final Component component) {
    return new LabelFactory().symbol(icon).alignment(alignment).forComponent(component).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  text               the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  icon               the icon of the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final String text, final Icon icon) {
    return label(text, icon, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  text               the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  icon               the icon of the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   ** @param  component          the component to be associated with the label.
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final String text, final Icon icon, final Component component) {
    return new LabelFactory().prompt(text).symbol(icon).forComponent(component).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  text               the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  icon               the icon of the label.
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
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final String text, final Icon icon, final int alignment) {
    return label(text, icon, alignment, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Factory method to create a {@link JLabel} using <code>text</code> to
   ** get the mnemonic and name.
   **
   ** @param  text               the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  icon               the icon of the label.
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
   ** @param  component          the component to be associated with the label.
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @return                    the {@link JLabel} associated with
   **                            {@link Component}.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public static JLabel label(final String text, final Icon icon, final int alignment, final Component component) {
    return new LabelFactory().prompt(text).symbol(icon).alignment(alignment).forComponent(component).label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prompt
  /**
   ** Set the label text.
   **
   ** @param  text               the text of the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    this <code>LabelFactory</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>LabelFactory</code>.
   */
  public LabelFactory prompt(final String text) {
    final String[] amp = text.split("&");
    if (amp.length == 1)
      this.label.setText(text);
    else {
      this.label.setText(amp[0] + amp[1]);
      this.label.setDisplayedMnemonic(amp[1].charAt(0));
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prompt
  /**
   ** Set the label text.
   **
   ** @param  text               the text of the label.
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
   **
   ** @return                    this <code>LabelFactory</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>LabelFactory</code>.
   */
  public LabelFactory prompt(final String text, final int alignment) {
    prompt(text);
    this.label.setHorizontalAlignment(alignment);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   symbol
  /**
   ** Set the label icon.
   **
   ** @param  icon               the icon of the label.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   **
   ** @return                    this <code>LabelFactory</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>LabelFactory</code>.
   */
  public LabelFactory symbol(final Icon icon) {
    this.label.setIcon(icon);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bold
  /**
   ** Make the label font bold.
   **
   ** @return                    this <code>LabelFactory</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>LabelFactory</code>.
   */
  public LabelFactory bold() {
    this.label.setFont(this.label.getFont().deriveFont(Font.BOLD));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forComponent
  /**
   ** Set the component this is labelling.
   ** <br>
   ** Can be <code>null</code> if this does not label a Component.
   **
   ** @param  component          the component to be associated with the label.
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @return                    this <code>LabelFactory</code> instance for
   **                            method chaining purpose.
   */
  public LabelFactory forComponent(final Component component) {
    this.label.setLabelFor(component);
    return this;
  }

  private LabelFactory alignment(final int alignment) {
    this.label.setHorizontalAlignment(alignment);
    return this;
  }
}