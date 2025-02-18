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

    File        :   AttributePage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributePage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.entry;

import javax.swing.event.TableModelListener;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.connection.iam.editor.ods.EntryEditor;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeModel;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPage;

////////////////////////////////////////////////////////////////////////////////
// class AttributePage
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A flat editor page suitable to display all properties of a directory
 ** service entry in a editor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributePage extends AbstractPage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1464877524724049550")
  private static final long serialVersionUID = -5166009124881553617L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  AttributePanel panel;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AttributePage</code> responsible to visualize the
   ** table UI of an attribute view that populates its data from the specified
   ** {@link Namespace}.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            data.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  private AttributePage(final Namespace data) {
    // ensure inheritance
    super(EntryEditor.PATH, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Returns the <code>DirectoryTableModel</code> of the embedded view.
   **
   ** @return                    the <code>DirectoryTableModel</code> of the
   **                            embedded view.
   **                            <br>
   **                            Possible object is {@link AttributeModel}.
   */
  public final AttributeModel model() {
    return this.panel.model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeView (AbstractPage)
  /**
   ** The data that the <code>FlatEditorTransparentPanel</code> should use to
   ** populate specific components comes from the {@link Namespace} passed to
   ** this page.
   */
  @Override
  protected final void initializeView() {
    this.panel = AttributePanel.build(EntryEditor.ATTRIBUTE, this.data);
    initializeHeaderPanel(Bundle.string(Bundle.ENTRY_ATTRIBUTE_PANEL_TITLE), this.panel);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateView (AbstractPage)
  /**
   ** The data that the <code>FlatEditorTransparentPanel</code> should use to
   ** populate specific components comes from the {@link Namespace} passed to
   ** this page.
   */
  @Override
  public void updateView() {
    updatePage();
    this.panel.updateView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>AttributePage</code> that populates its
   ** data from the specified {@link Namespace}.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the validated <code>AttributePage</code>.
   **                            <br>
   **                            Possible object <code>AttributePage</code>.
   */
  public static AttributePage build(final Namespace data) {
    return new AttributePage(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addListener
  /**
   ** Adds a listener to the table model that's notified each time a change to
   ** the data model occurs.
   **
   ** @param listener            the {@link TableModelListener} to add.
   **                            <br>
   **                            Allowed object is {@link TableModelListener}.
   */
  public void addListener(final TableModelListener listener) {
    this.panel.addListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeListener
  /**
   ** Removes a listener to the table model that's notified each time a change
   ** to the data model occurs.
   **
   ** @param listener            the {@link TableModelListener} to remove.
   **                            <br>
   **                            Allowed object is {@link TableModelListener}.
   */
  public void removeListener(final TableModelListener listener) {
    this.panel.removeListener(listener);
  }
}