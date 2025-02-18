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

    File        :   AbstractPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AbstractPage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel;

import java.util.List;

import javax.swing.JComponent;

import oracle.javatools.ui.Header;
import oracle.javatools.ui.HeaderPanel;

import oracle.javatools.ui.layout.VerticalFlowLayout;

import oracle.ide.util.Namespace;

import oracle.ide.controls.FlatEditorTransparentPanel;

import oracle.jdeveloper.connection.iam.model.DirectoryValue;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractPage
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display all properties of a directory
 ** service entry in a editor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class AbstractPage extends FlatEditorTransparentPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:396229262280999023")
  private static final long serialVersionUID = 4206877226719836906L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected transient String    title;
  protected transient Header    header;
  protected transient Namespace data;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractPage</code> responsible to visualize the page
   ** belonging to a Directory Service entry.
   **
   ** @param  title              the page title of that
   **                            <code>FlatEditorTransparentPanel</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  protected AbstractPage(final String title, final Namespace data) {
    // ensure inheritance
    super();

    // initialize instance
    this.data  = data;
    this.title = title;

    // create the panel responsible to visualize the general information about
    // the directory entry
    initializeLayout();
    initializePage();
    initializeView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns the {@link String} value object in the {@link Namespace}
   ** <code>data</code> whose name path is the specified {@link String}
   ** <code>path</code>.
   ** <br>
   ** If not found, returns <code>null</code>. A <code>null</code> or empty path
   ** is treated as if it were a single <code>null</code> path element.
   ** <br>
   ** No parent {@link Namespace} is searched.
   **
   ** @param  key                he key whose associated {@link String} value is
   **                            to be returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} to search.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the {@link String} value to which the specified
   **                            key is mapped, or <code>null</code> if the
   **                            {@link Namespace} <code>data</code> contains no
   **                            mapping for the key.
   **
   ** @throws ClassCastException if the key is of an inappropriate type for the
   **                            {@link Namespace}.
   */
  public static String stringValue(final String key, final Namespace data) {
    return data.get(key).toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleValue
  /**
   ** Returns the {@link DirectoryValue.Item} object in the {@link Namespace}
   ** <code>data</code> whose name path is the specified {@link String}
   ** <code>path</code>.
   ** <br>
   ** If not found, returns <code>null</code>. A <code>null</code> or empty path
   ** is treated as if it were a single <code>null</code> path element.
   ** <br>
   ** No parent {@link Namespace} is searched.
   **
   ** @param  key                the key whose associated
   **                            {@link DirectoryValue.Item} value is to be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} to search.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the {@link DirectoryValue.Item} value to which
   **                            the specified key is mapped, or
   **                            <code>null</code> if the {@link Namespace}
   **                            <code>data</code> contains no mapping for the
   **                            key.
   **                            <br>
   **                            Allowed object is {@link DirectoryValue.Item}.
   **
   ** @throws ClassCastException if the key is of an inappropriate type for the
   **                            {@link Namespace}.
   */
  public static DirectoryValue.Item singleValue(final String key, final Namespace data) {
    final DirectoryValue value = listValue(key, data);
    return value == null ? null : value.get(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listValue
  /**
   ** Returns the {@link DirectoryValue} value object in the {@link Namespace}
   ** <code>data</code> whose name path is the specified {@link String}
   ** <code>path</code>.
   ** <br>
   ** If not found, returns <code>null</code>. A <code>null</code> or empty path
   ** is treated as if it were a single <code>null</code> path element.
   ** <br>
   ** No parent {@link Namespace} is searched.
   **
   ** @param  key                he key whose associated {@link DirectoryValue}
   **                            value is to be returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} to search.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the {@link List} value to which the specified
   **                            key is mapped, or <code>null</code> if the
   **                            {@link Namespace} <code>data</code> contains no
   **                            mapping for the key.
   **
   ** @throws ClassCastException if the key is of an inappropriate type for the
   **                            {@link Namespace}.
   */
  public static DirectoryValue listValue(final String key, final Namespace data) {
    return (DirectoryValue)data.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the page.
   ** <br>
   ** By default the layout in initialized as a {@link VerticalFlowLayout}.
   ** <p>
   ** Sub classes should override this mathod of anaothe layout is more
   ** appropriate
   */
  protected void initializeLayout() {
    setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5, true, true));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializePage
  /**
   ** Initialize the page title of this
   ** <code>FlatEditorTransparentPanel</code>.
   */
  protected void initializePage() {
    this.header = new Header();
    this.header.setLevel(Header.Level.PAGE);
    add(this.header);

    updatePage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePage
  /**
   ** Updates the page title of this <code>FlatEditorTransparentPanel</code>.
   */
  protected void updatePage() {
    this.header.setText(stringValue(this.title, this.data));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeView
  /**
   ** The data that the <code>FlatEditorTransparentPanel</code> should use to
   ** populate specific components comes from the {@link Namespace} passed to
   ** this page.
   */
  protected abstract void initializeView();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateView
  /**
   ** The data that the <code>FlatEditorTransparentPanel</code> should use to
   ** populate specific components comes from the {@link Namespace} passed to
   ** this page.
   */
  public abstract void updateView();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeHeaderPanel
  /**
   ** Factory method to create a panel wrapping the specified
   ** <code>component</code>.
   **
   ** @param  title              the title of the component to visualize.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  component          the {@link JComponent} to visualize.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   */
  protected void initializeHeaderPanel(final String title, final JComponent component) {
    final HeaderPanel<JComponent> panel  = new HeaderPanel<JComponent>(component);
    initializeHeader(panel.getHeader(), title);
    add(panel);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeHeader
  /**
   ** Factory method to setup the panel header.
   **
   ** @param  header             the {@link Header} to visualize.
   **                            <br>
   **                            Allowed object is {@link Header}.
   ** @param  title              the title of the component to visualize.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected void initializeHeader(final Header header, final String title) {
    header.setText(title);
    header.setLevel(Header.Level.SUB);
  }
}