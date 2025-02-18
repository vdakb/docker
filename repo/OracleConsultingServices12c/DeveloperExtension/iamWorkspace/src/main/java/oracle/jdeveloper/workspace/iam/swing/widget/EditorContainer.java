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

    File        :   EditorContainer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EditorContainer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.Dimension;
import java.awt.Component;

import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import javax.swing.text.JTextComponent;

import oracle.bali.inspector.PropertyEditorFactory2;

import oracle.ide.net.URLTextField;

////////////////////////////////////////////////////////////////////////////////
// class EditorContainer
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A wraper that renders the component to provide user input capabilities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class EditorContainer extends Box {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6070538319092727354")
  private static final long    serialVersionUID = -2957146174097759497L;

  private static final int     MIN_WIDTH        = 200;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient JComponent editor;
  private transient Button     button;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Button
  // ~~~~~ ~~~~~~
  /**
   ** A button with a fixed size to workaround bugs in OSX. Submitted by Hani
   ** Suleiman. Hani uses an icon for the ellipsis, we've decided to hardcode
   ** the dimension to 16x30 but only on Mac OS X.
   */
  public static class Button extends JButton {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:3120685969032308052")
    private static final long serialVersionUID = -3860379483602281416L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Component                        editor;
    private transient PropertyEditorFactory2 factory;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Button</code> that components is laid out in the
     ** direction of a line of text as determined by the
     ** <code>ComponentOrientation</code> property.
     **
     ** @param  factory          the {@link PropertyEditorFactory2} this
     **                          component renders to provide user lookup
     **                          capabilities.
     ** @param  editor           the {@link Component} this component renders to
     **                          provide user input capabilities.
     */
    public Button(final PropertyEditorFactory2 factory, final Component editor) {
      // ensure inheritance
      super();

      // initialize instance
      this.factory = factory;
      this.editor  = editor;

      // initialize UI
      setOpaque(true);
      if (editor instanceof URLTextField)
        addActionListener((URLTextField)editor);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addActionListener
    /**
     ** Adds an {@link ActionListener} to the button.
     ** <p>
     ** Renders the action component as a buttan with the <code>...</code>
     ** (3 dots) as the text.
     **
     ** @param  listener         the {@link ActionListener} to be added.
     */
    public void addActionListener(final ActionListener listener) {
      setMargin(new Insets(0, 2, 0, 2));
      setText("...");
      for (ActionListener l : getActionListeners()) {
        removeActionListener(l);
      }
      super.addActionListener(listener);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EditorContainer</code> that components is laid out in
   ** the direction of a line of text as determined by the
   ** <code>ComponentOrientation</code> property.
   **
   ** @param  editor             the {@link JComponent} this component renders
   **                            to provide user input capabilities.
   */
  public EditorContainer(final JComponent editor) {
    // ensure inheritance
    this(editor, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EditorContainer</code> that components is laid out in
   ** the direction of a line of text as determined by the
   ** <code>ComponentOrientation</code> property.
   **
   ** @param  editor             the {@link JComponent} this component renders
   **                            to provide user input capabilities.
   */
  public EditorContainer(final JComponent editor, final Button button) {
    super(BoxLayout.LINE_AXIS);

    // initialize instance
    this.editor = editor;
    this.button = button;

    // initialize UI
    update(editor);
    layoutComponents();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editor
  /**
   ** Returns the {@link Component} this component renders.
   **
   ** @return                    the {@link JComponent} this component renders.
   **                            <br>
   **                            Possible object is {@link JComponent}.
   */
  public final JComponent editor() {
    return this.editor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   button
  /**
   ** Returns the {@link Button} this component renders.
   **
   ** @return                    the {@link Button} this component renders.
   **                            <br>
   **                            Possible object is {@link Button}.
   */
  public final Button button() {
    return this.button;
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
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @Override
  public void setEnabled(final boolean state) {
    // ensure inheritance
    super.setEnabled(state);

    // disable its children
    if (this.editor instanceof JTextComponent)
      ((JTextComponent)this.editor).setEditable(state);
    else
      this.editor.setEnabled(state);

    if (this.button != null)
      this.button.setEnabled(state);
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
   **                            <br>
   **                            Possible object is {@link Dimension}.
   */
  @Override
  public Dimension getPreferredSize() {
    Dimension fromSuper = super.getPreferredSize();
    if ((editor instanceof JTextField) || (editor instanceof JComboBox) || (editor instanceof JCheckBox)) {
      if (fromSuper.width < MIN_WIDTH)
        fromSuper.width = MIN_WIDTH;
    }
    return fromSuper;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates this component with the properties from the specified
   ** <code>EditorContainer</code>.
   **
   ** @param  template           the <code>EditorContainer</code> used as the
   **                            template to update this
   **                            <code>EditorContainer</code>.
   **                            <br>
   **                            Allowed object is {@link EditorContainer}.
   */
  public final void update(final EditorContainer template) {
    update(template.editor(), template.button());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates this component with the properties from the specified
   ** <code>editor</code> and <code>button</code>.
   **
   ** @param  editor             the {@link JComponent} to provide user input
   **                            capabilities.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   ** @param  button             the {@link Button} used as the
   **                            template to update this
   **                            <code>EditorContainer</code>.
   **                            <br>
   **                            Allowed object is {@link Button}.
   */
  void update(final JComponent editor, final Button button) {
    update(editor);
    this.button = button;
    removeAll();
    layoutComponents();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates this component with the properties from the specified
   ** editor {@link Component}.
   **
   ** @param  editor             the {@link JComponent} to provide user input
   **                            capabilities.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   */
  void update(final JComponent editor) {
    this.editor = editor;
    if (this.editor == null)
      return;

    if ((this.editor instanceof JCheckBox))
      return;

    final Dimension preferred = this.editor.getPreferredSize();
    this.editor.setPreferredSize(new Dimension(5, preferred.height));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layoutComponents
  /**
   ** Layout the box.
   */
  private void layoutComponents() {
    add(this.editor);
    if (this.button != null) {
      add(createHorizontalStrut(3));
      add(this.button);
    }
  }
}