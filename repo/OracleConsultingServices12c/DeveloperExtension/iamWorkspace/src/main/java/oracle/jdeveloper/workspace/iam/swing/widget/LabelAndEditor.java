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

    File        :   LabelAndEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LabelAndEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.JLabel;
import javax.swing.JCheckBox;

import oracle.jdeveloper.workspace.iam.model.AbstractProperty;

////////////////////////////////////////////////////////////////////////////////
// class LabelAndEditor
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A wraper that holds the label and the editing components of a parameter.
 */
class LabelAndEditor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String COLON     = ":";
  private static final String MANDATORY = " *";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private JLabel          label;
  private EditorContainer editor;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LabelAndEditor</code> that component are laid out in
   ** the direction of a line of text as determined by the
   ** <code>ComponentOrientation</code> property.
   **
   ** @param  label              the {@link JLabel} this component renders.
   **                            <br>
   **                            Allowed object is {@link JLabel}.
   ** @param  editor             the {@link EditorContainer} this component
   **                            renders.
   **                            <br>
   **                            Allowed object is {@link EditorContainer}.
   */
  private LabelAndEditor(final JLabel label, final EditorContainer editor) {
    // ensure inheritance
    super();

    // initialize instance
    this.label  = label;
    this.editor = editor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the component used as the label.
   **
   ** @return                    the component used as the label.
   */
  public JLabel label() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editor
  /**
   ** Returns the component used as the editor.
   **
   ** @return                    the component used as the editor.
   */
  public EditorContainer editor() {
    return this.editor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>LabelAndEditor</code> that component are
   ** laid out in the direction of a line of text as determined by the
   ** <code>ComponentOrientation</code> property.
   **
   ** @param  property           the {@link AbstractProperty} this component
   **                            renders.
   **                            <br>
   **                            Allowed object is {@link JLabel}.
   ** @param  editor             the {@link EditorContainer} this component
   **                            renders.
   **                            <br>
   **                            Allowed object is {@link EditorContainer}.
   */
   public static LabelAndEditor build(final AbstractProperty property, final EditorContainer editor) {
    JLabel label = LabelContainer.build(property.label(), property.icon());
    textForLabel(label, property.mandatory());
    return build(label, editor);
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>LabelAndEditor</code> that component are
   ** laid out in the direction of a line of text as determined by the
   ** <code>ComponentOrientation</code> property.
   **
   ** @param  label              the {@link JLabel} this component renders.
   **                            <br>
   **                            Allowed object is {@link JLabel}.
   ** @param  mandatory          <code>true</code> if the label should reflect
   **                            that the parameter is mandatory.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  editor             the {@link EditorContainer} this component
   **                            renders.
   **                            <br>
   **                            Allowed object is {@link EditorContainer}.
   */
  public static LabelAndEditor build(final JLabel label, boolean mandatory, EditorContainer editor) {
    if ((editor.editor() instanceof JCheckBox)) {
      textForLabel(label, mandatory);
    }
    return build(label, editor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>LabelAndEditor</code> that component are
   ** laid out in the direction of a line of text as determined by the
   ** <code>ComponentOrientation</code> property.
   **
   ** @param  label              the {@link JLabel} this component renders.
   **                            <br>
   **                            Allowed object is {@link JLabel}.
   ** @param  editor             the {@link EditorContainer} this component
   **                            renders.
   **                            <br>
   **                            Allowed object is {@link EditorContainer}.
   */
  public static LabelAndEditor build(final JLabel label, final EditorContainer editor) {
    return new LabelAndEditor(label, editor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textForLabel
  /**
   ** Returns the appropriate string used as the label.
   ** <p>
   ** The string will be postfixed all the time by a <code>:</code> (colon). If
   ** the specified <code>property</code> is mandatory the returning string is
   ** additionally postfied by <code> *</code>.
   **
   ** @param  label              the string for the label.
   ** @param  mandatory          <code>true</code> if the label should reflect
   **                            that the parameter is mandatory.
   **
   ** @return                    the appropriate string used as the label based
   **                            on the properties of the specified
   **                            {link AbstractProperty}.
   */
  static void textForLabel(final JLabel label, final boolean mandatory) {
    textForLabel(label);
    if (mandatory)
      label.setText(label.getText() + MANDATORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textForLabel
  /**
   ** Returns the appropriate string used as the label.
   ** <p>
   ** The string will be postfixed all the time by a <code>:</code> (colon)
   ** only.
   **
   ** @param  label              the string for the label.
   **
   ** @return                    the appropriate string used as the label based
   **                            on the properties of the specified
   **                            {link AbstractProperty}.
   */
  static void textForLabel(final JLabel label) {
    label.setText(label.getText() + COLON);
  }
}