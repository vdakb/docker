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

    File        :   ParameterEditorFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ParameterEditorFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JTextField;

import javax.swing.text.JTextComponent;

import oracle.bali.inspector.editor.EditorComponentSetupRegistry;
import oracle.bali.inspector.editor.EditorComponentFactoryRegistry;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.model.Property;
import oracle.jdeveloper.workspace.iam.model.AbstractProperty;

////////////////////////////////////////////////////////////////////////////////
// class ParameterEditorFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A <code>ParameterEditorFactory</code> is a factory to create the
 ** appropriate editor for an {@link AbstractProperty}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class ParameterEditorFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default display width of an editable component like JTextField. */
  private static final int MAXIMUM_DISPLAY_LENGTH = 300;

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editorFor
  public static LabelAndEditor editorFor(final AbstractProperty property) {
    EditorContainer container = editor(property);
    if (container == null)
      return null;

    LabelAndEditor labelAndEditor = LabelAndEditor.build(property, container);
//    if (container.editor() instanceof JTextField)
//      ((JTextField)container.editor()).getDocument().addDocumentListener(property);
    return labelAndEditor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editor
  public static EditorContainer editor(final Property property) {
    JComponent inlineEditor  = findEditor(property);
    JComponent displayEditor = truncateToDisplay(inlineEditor, property);
    if (displayEditor == inlineEditor)
      setText(inlineEditor, property);
    else
      inlineEditor = displayEditor;

    return setupEditor(inlineEditor, property);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findEditor
  private static JComponent findEditor(final Property property) {
    if (property.isPaintable()) {
      final EditorComponentFactoryRegistry registry = EditorComponentFactoryRegistry.instance();
      return (JComponent)registry.createPaintableEditor(property, property.writable());
    }

    if ((property.hasInlineEditor()) && (property.writable())) {
      final JComponent inline = (JComponent)property.getInlineEditor();
      EditorComponentSetupRegistry.instance().configure(inline, property, property);
      return inline;
    }
    return (JComponent)EditorComponentFactoryRegistry.instance().createReadOnlyEditor(property);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setupEditor
  private static EditorContainer setupEditor(final JComponent component, final Property property) {
    if (!property.writable())
      return new EditorContainer(component);

    EditorContainer.Button customEditor = null;
    if (property.type() == File.class) {
      customEditor = new EditorContainer.Button(property, component);
    }
    return new EditorContainer(component, customEditor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setText
  /**
   ** Sets the text of the specified @link TextComponent} to the value of the
   ** specified property. If the value of the property is <code>null</code> or
   ** empty, has no effect.
   **
   ** @param  component          the {@link JComponent} the text to be set.
   ** @param  property           the {@link AbstractProperty} the value has to
   **                            be used to set the text for {@link JComponent}.
   */
  public static void setText(final JComponent component, final Property property) {
    if (!(component instanceof JTextComponent))
      return;

    final String propertyValue = property.getAsText();
    if (StringUtility.empty(propertyValue))
      return;

    final JTextComponent editor = (JTextComponent)component;
    if (propertyValue.equals(editor.getText()))
      return;
    editor.setText(propertyValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   truncateToDisplay
  private static JComponent truncateToDisplay(final JComponent component, final Property property) {
    EditorComponentFactoryRegistry factories = EditorComponentFactoryRegistry.instance();
    if ((component instanceof JTextField)) {
      String propertyValue = property.getAsText();
      // if the length of the value exceeds the limits create a display value
      // that fits in the dimension of the editor by truncating the value to
      // the appropriate length
      if ((propertyValue != null) && (propertyValue.length() > MAXIMUM_DISPLAY_LENGTH)) {
        propertyValue = propertyValue.substring(0, 50) + "...";
        final JComponent editor = (JComponent)factories.updateReadOnlyEditor(component, propertyValue);
        if (editor == null)
          return (JComponent)factories.createReadOnlyEditor(propertyValue);
      }
    }
    return component;
  }
}