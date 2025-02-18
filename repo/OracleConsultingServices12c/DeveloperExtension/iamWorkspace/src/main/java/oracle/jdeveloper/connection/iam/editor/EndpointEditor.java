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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   EndpointEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor;

import java.awt.Component;

import javax.swing.UIManager;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import oracle.ide.util.Namespace;

import oracle.ide.editor.Editor;

import oracle.ide.model.UpdateMessage;

////////////////////////////////////////////////////////////////////////////////
// class EndpointEditor
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>EndpointEditor</code> is the integration layer between the IDE and
 ** the editor components inside the IDE.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class EndpointEditor extends Editor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected       EndpointContent content = null;
  protected final Namespace       data    = new Namespace();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointEditor</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EndpointEditor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEditorAttribute (overridden)
  /**
   ** Obtains values from the {@link Editor} property store.
   ** <p>
   ** Some of the properties returning defaults that might be set for the
   ** {@link Editor} but this will not have any effect.
   **
   ** @param  attribute          takes values from {@link Editor}
   **                            <code>ATTRIBUTE_</code> constants. 
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value for the specified attribute.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  @Override
  public Object getEditorAttribute(final String attribute) {
    // by convention, flat editors use the window color as their background
    // color
    // the window color is usually white on most systems
    if (Editor.ATTRIBUTE_BACKGROUND_COLOR.equals(attribute)) {
      return UIManager.getColor("window");
    }
    // if you want framework provided scrolling, comment out the next three
    // if conditions. You probably want to control your own scrolling if your
    // editor contains category tabs
    else if (Editor.ATTRIBUTE_SCROLLABLE.equals(attribute)) {
      return Boolean.FALSE;
    }
    else if (Editor.ATTRIBUTE_HORIZONTAL_SCROLLBAR_POLICY.equals(attribute)) {
      return JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
    }
    else if (Editor.ATTRIBUTE_VERTICAL_SCROLLBAR_POLICY.equals(attribute)) {
      return JScrollPane.VERTICAL_SCROLLBAR_NEVER;
    }
    else
      return super.getEditorAttribute(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDefaultFocusComponent (overridden)
  /**
   ** This method is used to know what component should get the focus by
   ** default.
   ** <br>
   ** The returned component should be a child of the component returned by
   ** getGUI() or getGUI() itself and should be requestFocusable.
   ** <br>
   ** By contract, getGUI() will always be called before
   ** getDefaultFocusComponent().
   **
   ** @return                    the component that should take the focus.
   **                            <br>
   **                            Possible object is {@link Component}.
   */
  @Override
  public Component getDefaultFocusComponent() {
    return this.content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (Observer)
  /**
   ** Subjects call this method when they notify their observers that the
   ** subjects state has changed.
   **
   ** @param  observed           the subject whose state has changed.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  change             the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link UpdateMessage}.
   */
  @Override
  public void update(final Object observed, final UpdateMessage change) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGui (View)
  /**
   ** Returns the root graphical user interface component.
   **
   ** @return                    the root graphical user interface component.
   **                            <br>
   **                            Possible object is {@link Component}.
   */
  @Override
  public Component getGUI() {
    return this.content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open (Editor)
  /**
   ** Open this editor on the context set by a prior call to setContext.
   ** <br>
   ** If the editor cannot be open (file not found for example), this method
   ** should throw an OpenAbortedException.
   */
  @Override
  public void open() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableComponent
  /**
   ** Creates a {@link JScrollPane} that displays the contents of the specified
   ** component, where vertical scrollbar appear whenever the component's
   ** contents are larger than the view.
   **
   ** @param  view               the component to display in the scrollpane's
   **                            viewport.
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @return                    a {@link JScrollPane} that displays the
   **                            contents of the specified component in its
   **                            viewport.
   **                            <br>
   **                            Possible object is {@link JScrollPane}.
   */
  public static JScrollPane scrollableComponent(final Component view) {
    final JScrollPane pane = new JScrollPane(view);
    pane.setBorder(null);
    pane.getViewport().setBackground(UIManager.getColor("window"));
    pane.getVerticalScrollBar().setUnitIncrement(20);
    pane.getHorizontalScrollBar().setUnitIncrement(20);
    pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    return pane;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (overridden)
  /**
   ** Close this editor - this gives us a chance to clean up any resources.
   ** <b>Note</b>:
   ** <br>
   ** Although the editor is closed, the UndoableEdits continue to survive in
   ** the CommandProcessor queue!  This means that we cannot clean out anything
   ** that that functionality (undo) depends on.
   ** <br>
   ** Once closed a CodeEditor instance should not be used again as behaviour
   ** cannot be guaranteed and exceptions may occur while calling methods.
   */
  @Override
  public void close() {
    // cleanup
    this.content = null;
    // ensure inheritance
    super.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addContent (overridden)
  /**
   ** Adds a <code>component</code> with the specified tab title.
   **
   ** @param  title              the title to be displayed in the tab.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  component          the component to be displayed when the tab is
   **                            clicked.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   */
  public void addContent(final String title, final JComponent component) {
    this.content.tabbed.add(title, scrollableComponent(component));
  }
}