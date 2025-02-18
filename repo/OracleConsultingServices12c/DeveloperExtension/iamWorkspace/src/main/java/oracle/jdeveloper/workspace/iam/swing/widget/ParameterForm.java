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

    File        :   ParameterForm.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ParameterForm.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.util.List;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;

import javax.swing.text.JTextComponent;

import oracle.bali.inspector.editor.EditorComponentFactoryRegistry;

import oracle.jdeveloper.workspace.iam.model.Property;
import oracle.jdeveloper.workspace.iam.model.AbstractProperty;

import oracle.jdeveloper.workspace.iam.swing.LayoutFactory;

////////////////////////////////////////////////////////////////////////////////
// class ParameterForm
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A <code>ParameterForm</code> is a general-purpose panel to render property
 ** values in a grid.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class ParameterForm extends JPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-376046615973912623")
  private static final long        serialVersionUID = -8368197648382351762L;

  private static final int         LEFT_PADDING     = 2;

  /** gap in pixel between the rows in the canvas panel */
  private static final int         VERTICAL_GAP     = 4;

  /** the gap in pixel between the label and the editot */
  private static final int         EDITOR_GAP       = 6;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final GridBagConstraints constraints      = LayoutFactory.defaultConstraints();
  private JPanel                   canvas;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    // register the factories in revers order, due to the registry put the
    // factory to be registered always on top
    // our prefered order is that the URLFile factory is queried befor the
    // combobox factory
    EditorComponentFactoryRegistry.instance().register(new ListEditorFactory());
    EditorComponentFactoryRegistry.instance().register(new FileEditorFactory());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new<code>ParameterForm</code> with a {@link GridBagLayout} as
   ** the layout manager.
   */
  protected ParameterForm() {
    // ensure inhertitance
    super(new GridBagLayout());

    // initialize instance
    this.canvas = this;

    // initialize UI
    setBorder(BorderFactory.createEmptyBorder());
    setOpaque(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constraints
  /**
   ** Returns the defensive copy of the {@link GridBagConstraints} instance to
   ** render the panel.
   **
   ** @return                    the {@link GridBagConstraints} instance to
   **                            render the panel.
   */
  public final GridBagConstraints constraints() {
    return (GridBagConstraints)this.constraints.clone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canvas
  /**
   ** Returns the {@link JPanel} instance to render the properties.
   **
   ** @return                    the {@link JPanel} instance to render the
   **                            properties.
   */
  public final JPanel canvas() {
    return this.canvas;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBackground
  /**
   ** Sets the background color of this component.
   **
   ** @param  color              the desired background <code>Color</code>
   */
  @Override
  public void setBackground(final Color color) {
    // ensure inheritance
    super.setBackground(color);

    if (this.canvas == null)
      this.canvas = this;

    if (this.canvas != this)
      this.canvas.setBackground(color);

    for (Component c : this.canvas.getComponents()) {
      if (((c instanceof ParameterGroup)) || ((c instanceof ParameterChoice)))
        c.setBackground(color);

      if (!(c instanceof EditorContainer))
        continue;

      Component editor = ((EditorContainer)c).editor();
      if (!(editor instanceof JCheckBox))
        continue;

      editor.setBackground(color);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnabled
  /**
   ** Sets whether or not this component is enabled.
   ** <p>
   ** A component that is enabled may respond to user input, while a component
   ** that is not enabled cannot respond to  user input. Some components may
   ** alter their visual representation when they are disabled in order to
   ** provide feedback to the user that they cannot take input.
   **
   ** @param  state              <code>true</code> if this component should be
   **                            enabled, <code>false</code> otherwise
   */
  @Override
  @SuppressWarnings("cast")
  public void setEnabled(final boolean state) {
    // ensure inheritance
    super.setEnabled(state);

    // disable its children
    for (Component c : this.canvas.getComponents()) {
      if (((c instanceof ParameterGroup)) || ((c instanceof ParameterChoice)))
        c.setEnabled(state);

      if ((c instanceof EditorContainer))
        ((EditorContainer)c).setEnabled(state);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateCanvas
  /**
   ** Set the panel used to render the properties.
   ** <p>
   ** A {@link GridBagLayout} is assigned to zje specified panel as the
   ** <code>LayoutManager</code>
   **
   ** @param  panel              the panel used to render the properties.
   */
  public final void updateCanvas(final JPanel panel) {
    this.canvas = panel;
    this.canvas.setLayout(new GridBagLayout());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearCanvas
  /**
   ** Removes all the components from tha canvas container.
   ** <p>
   ** This method also notifies the layout manager to remove the components from
   ** the canvas container's layout via the <code>removeLayoutComponent</code>
   ** method.
   */
  public final void clearCanvas() {
    if (this.canvas != null) {
      // remve the property change listener from all components
      for (Component c : this.canvas.getComponents()) {
        if ((c instanceof EditorContainer)) {
          final EditorContainer container =  (EditorContainer)c;
          if (container.editor() instanceof JTextComponent)
            ((JTextComponent)container.editor()).getDocument().addDocumentListener(null);
        }
      }
      this.canvas.removeAll();
    }

    resetConstraints();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showCanvas
  /**
   ** Instructs this panel to show the property canvas.
   */
  public final void showCanvas() {
    if (this.canvas != null)
      this.canvas.setVisible(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hideCanvas
  /**
   ** Instructs this panel to hide the property canvas.
   */
  public final void hideCanvas() {
    if (this.canvas != null)
      this.canvas.setVisible(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProperty
  /**
   ** Add and renders the specified {@link AbstractProperty} in the panel.
   **
   ** @param  property           the {@link AbstractProperty} to render.
   */
  public final void addProperty(final AbstractProperty property) {
    renderProperty(property, 1, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (overridden)
  /**
   ** Adds the specified component to the end of the canvas panel.
   ** <p>
   ** The canvas panel will notify the layout manager to add the component to
   ** its container's layout using.
   ** <p>
   ** This is a convenience method for {@link #addImpl}.
   **
   ** @param  component          the component to be added
   */
  @Override
  public final Component add(final Component component) {
    this.canvas.add(component, this.constraints);
    return component;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renderProperty
  /**
   ** Renders the specified {@link AbstractProperty} in the panel.
   **
   ** @param  property           the {@link AbstractProperty} to render.
   ** @param  editorWidth        the cell width the editor components spans in
   **                            the layout.
   ** @param  startCell          the cell the property will start.
   */
  final void renderProperty(final AbstractProperty property, final int editorWidth, final int startCell) {
    LabelAndEditor labelAndEditor = ParameterEditorFactory.editorFor(property);
    render(labelAndEditor, editorWidth, startCell);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetConstraints
  /**
   ** Resets the {@link GridBagConstraints} attribute <code>constraint</code>
   ** to the specifed cell on the x-axis.
   */
  final void resetConstraints() {
    this.constraints.gridy = 0;
    resetConstraints(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetConstraints
  /**
   ** Resets the {@link GridBagConstraints} attribute <code>constraint</code>
   ** to the specifed cell on the x-axis.
   **
   ** @param  startCell        the grid cell on the x-axis to reset.
   */
  final void resetConstraints(final int startCell) {
    this.constraints.gridx       = startCell;
    this.constraints.insets.top  = VERTICAL_GAP;
    this.constraints.insets.left = 0;
    this.constraints.weightx     = 0.0D;
    this.constraints.weighty     = 0.0D;
    this.constraints.gridwidth   = 1;
    this.constraints.gridheight  = 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   render
  /**
   ** Renders the specified {@link LabelAndEditor} in the panel.
   **
   ** @param  labelAndEditor     the {@link Property} row to render.
   ** @param  editorWidth        the cell width the editor components spans in
   **                            the layout.
   ** @param  startCell          the cell the property will start.
   */
  private LabelAndEditor render(final LabelAndEditor labelAndEditor, final int editorWidth, final int startCell) {
    resetConstraints(startCell);
    addIndent();
    EditorContainer container = labelAndEditor.editor();
    if ((container.editor() instanceof JCheckBox)) {
      addCheckBoxEditor(container, editorWidth);
      return labelAndEditor;
    }
    addLabel(labelAndEditor.label());
    addContainer(container, editorWidth);
    this.constraints.gridy++;
    return labelAndEditor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addIndent
  /**
   ** Indents the rendering cell to the next cell on the x-axis.
   */
  private void addIndent() {
    addIndent(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addIndent
  /**
   ** Indents the rendering cell to the bext cell on the x-axis.
   ** <p>
   ** If the specified {@link List} of properties is non-null and not empty a
   ** controll compomemt is add
   */
  private void addIndent(final List<Property> subProperties) {
    this.constraints.gridx++;
    if ((subProperties != null) && (!subProperties.isEmpty()))
      addAccordion();
    else
      addSpace();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSpace
  /**
   ** Adds an empty vomponent to the layout of the target canvas.
   */
  private void addSpace() {
    this.constraints.insets.right = 0;
    add(Box.createHorizontalStrut(LEFT_PADDING));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAccordion
  /**
   ** Add the accordion to the target canvas to expand/collapse the canvas.
   */
  private void addAccordion() {
    this.constraints.insets.right = LEFT_PADDING;
    add(new PanelAccordion());
    this.constraints.insets.right = 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addLabel
  /**
   ** Add the label component to the target canvas.
   **
   ** @param  label              the {@link Component} displayed as the label.
   */
  private void addLabel(final Component label) {
    this.constraints.gridx++;
    this.constraints.gridwidth    = 1;
    this.constraints.insets.right = 0;
    this.constraints.fill         = GridBagConstraints.BOTH;
    add(label);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addContainer
  /**
   ** Add the editor container to the target canvas.
   **
   ** @param  editor              the {@link Component} displayed as the editor.
   ** @param  span                the number if cells in the-axis the component
   **                             spans.
   */
  private void addContainer(final Component editor, final int span) {
    this.constraints.gridx++;
    this.constraints.gridwidth   = span;
    this.constraints.insets.left = EDITOR_GAP;
    this.constraints.weightx     = 1.0D;
    this.constraints.fill        = GridBagConstraints.BOTH;
    add(editor);
    this.constraints.gridwidth   = 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCheckBoxEditor
  /**
   ** Add the editor component to the target canvas.
   **
   ** @param  container           the {@link EditorContainer} displayed as the
   **                             editor.
   ** @param  span                the number if cells in the-axis the component
   **                             spans.
   */
  private void addCheckBoxEditor(final EditorContainer container, final int span) {
    this.constraints.gridx++;
    this.constraints.gridwidth    = (span + 2);
    this.constraints.insets.left  = 0;
    this.constraints.insets.right = 0;
    this.constraints.weightx      = 0.0D;
    this.constraints.fill         = GridBagConstraints.NONE;
    add(container);

    container.editor().setBackground(this.canvas().getBackground());
    this.constraints.gridwidth    = 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconContainer
  /**
   ** @param  icon               the {@link Icon} to wrap with a
   **                            {@link Container} component to layout the
   **                            icon.
   **
   ** @return                    the {@link Container} with the specified
   **                            {@link Icon}.
   */
  private Container iconContainer(final Icon icon) {
    final Container container = LayoutFactory.emptyContainer();
    if (icon != null) {
      container.add(new JLabel(icon));
    }
    else {
      container.add(Box.createHorizontalStrut(11));
    }
    return container;
  }
}