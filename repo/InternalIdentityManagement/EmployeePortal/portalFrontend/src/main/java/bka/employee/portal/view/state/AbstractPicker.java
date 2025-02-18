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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Self Service Portal
    Subsystem   :   Common Shared Components

    File        :   AbstractPicker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractPicker.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-03-03  DSteding    First release version
*/

package bka.employee.portal.view.state;

import java.util.List;

import javax.faces.event.ActionEvent;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.nav.RichButton;

import oracle.hst.foundation.faces.ADF;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractPicker
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to search and select.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractPicker extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9143116496286340754")
  private static final long serialVersionUID = -5445393004506108008L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient ComponentReference addSelectedButton;
  private transient ComponentReference addAllButton;
  private transient ComponentReference removeSelectedButton;
  private transient ComponentReference removeAllButton;

  private transient ComponentReference selectionTable;
  private transient ComponentReference addButton;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractPicker</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractPicker() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAddButton
  /**
   ** Sets the UI component which renders the add button.
   **
   ** @param  component          the UI component which renders the add button.
   **                            Allowed object is {@link RichButton}.
   */
  public void setAddButton(final RichButton component) {
    this.addButton = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAddButton
  /**
   ** Returns the UI component which renders the add button.
   **
   ** @return                    the UI component which renders the add button.
   **                            Possible object is {@link RichButton}.
   */
  public RichButton getAddButton() {
    return (this.addButton != null) ? (RichButton)this.addButton.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAddSelectedButton
  /**
   ** Sets the UI component which renders the single item select button.
   **
   ** @param  component          the UI component which renders the single
   **                            item select button.
   **                            Allowed object is {@link RichButton}.
   */
  public void setAddSelectedButton(final RichButton component) {
    this.addSelectedButton = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAddSelectedButton
  /**
   ** Returns the UI component which renders the single item select button.
   **
   ** @return                    the UI component which renders the single
   **                            item select button.
   **                            Possible object is {@link RichButton}.
   */
  public RichButton getAddSelectedButton() {
    return (this.addSelectedButton != null) ? (RichButton)this.addSelectedButton.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isAddSelectedDisabled
  /**
   ** Whether at least one row (aka Resource Object) in the search table is
   ** selected.
   **
   ** @return                    <code>true</code> if at least on row in the
   **                            search table is selected.
   */
  public boolean isAddSelectedDisabled() {
    return selectedRowCount(getSearchTable()) == 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAddAllButton
  /**
   ** Sets the UI component which renders the all items select button.
   **
   ** @param  component          the UI component which renders the all items
   **                            select button.
   **                            Allowed object is {@link RichButton}.
   */
  public void setAddAllButton(final RichButton component) {
    this.addAllButton = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAddAllButton
  /**
   ** Returns the UI component which renders the all items select button.
   **
   ** @return                    the UI component which renders the all items
   **                            select button.
   **                            Possible object is {@link RichButton}.
   */
  public RichButton getAddAllButton() {
    return (this.addAllButton != null) ? (RichButton)this.addAllButton.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRemoveSelectedButton
  /**
   ** Sets the UI component which renders the single item remove button.
   **
   ** @param  component          the UI component which renders the single
   **                            item remove button.
   **                            Allowed object is {@link RichButton}.
   */
  public void setRemoveSelectedButton(final RichButton component) {
    this.removeSelectedButton = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRemoveSelectedButton
  /**
   ** Returns the UI component which renders the single item remove button.
   **
   ** @return                    the UI component which renders the single
   **                            item remove button.
   **                            Possible object is {@link RichButton}.
   */
  public RichButton getRemoveSelectedButton() {
    return (this.removeSelectedButton != null) ? (RichButton)this.removeSelectedButton.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRemoveAllButton
  /**
   ** Sets the UI component which renders the all items remove button.
   **
   ** @param  component          the UI component which renders the all items
   **                            remove button.
   **                            Allowed object is {@link RichButton}.
   */
  public void setRemoveAllButton(final RichButton component) {
    this.removeAllButton = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRemoveAllButton
  /**
   ** Returns the UI component which renders the all items remove button.
   **
   ** @return                    the UI component which renders the all items
   **                            remove button.
   **                            Possible object is {@link RichButton}.
   */
  public RichButton getRemoveAllButton() {
    return (this.removeAllButton != null) ? (RichButton)this.removeAllButton.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSelectionTable
  /**
   ** Sets the UI component which renders the selection result table display.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setSelectionTable(final RichTable component) {
   **     this.selectionTable = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference selectionTable;
   **   ...
   **   public void setSelectionTable(final RichTable component) {
   **     this.selectionTable = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the task header
   **                            display.
   **                            Allowed object is {@link RichTable}.
   */
  public final void setSelectionTable(final RichTable component) {
    this.selectionTable = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSelectionTable
  /**
   ** Returns the UI component which renders the selection result table display.
   **
   ** @return                    the UI component which renders the selection
   **                            result table display.
   **                            Possible object is {@link RichTable}.
   */
  public RichTable getSelectionTable() {
    return (this.selectionTable != null) ? (RichTable)this.selectionTable.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectionListener
  /**
   ** Handles the event if the selection in the table of available entries
   ** becomes to change.
   **
   ** @param  event              the {@link SelectionEvent} delivered from the
   **                            action occured in a UIXTable component.
   */
  public abstract void selectionListener(final SelectionEvent event);

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   addSelectedRows
  /**
   ** Adds the selected entries to the result which needs to be returned to
   ** the invoking taskflow handler for further processing.
   **
   ** @return                   the action name the embedding task flow requires
   **                           to handle.
   */
  public abstract String addSelectedRows();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRowsSelectListener
  /**
   ** Handles the event if the selection in the table of the selected entries
   ** becomes to change.
   **
   ** @param  event              the {@link SelectionEvent} delivered from the
   **                            action occured in a UIXTable component.
   */
  public void selectedRowsSelectListener(final @SuppressWarnings("unused") SelectionEvent event) {
    ADF.partialRender(getRemoveAllButton());
    ADF.partialRender(getRemoveSelectedButton());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectedActionListener
  /**
   ** Add the selected rows in the search table to the selection.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public void addSelectedActionListener(final @SuppressWarnings("unused") ActionEvent event) {
    addSelection(selectedRow(getSearchTable()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAllActionListener
  /**
   ** Add the all rows in the search table to the selection.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public abstract void addAllActionListener(final ActionEvent event);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeSelectedActionListener
  /**
   ** Remove the selected rows from the selection table.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public abstract void removeSelectedActionListener(final @SuppressWarnings("unused") ActionEvent event);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAllActionListener
  /**
   ** Remove the all rows from the selection table.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public abstract void removeAllActionListener(final @SuppressWarnings("unused") ActionEvent event);


  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelection
  /**
   ** Add the provided collection of {@link Row}s to the selected items.
   **
   ** @param  selection          the collection of {@link Row}s to add to the
   **                            selected items.
   */
  protected abstract void addSelection(final List<Row> selection);

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   refreshSourceTargets
  /**
   ** Refreshing the state of the UI component which displays the search result
   ** table.
   */
  protected void refreshSourceTargets() {
    ADF.partialRender(getAddAllButton());
    ADF.partialRender(getAddSelectedButton());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   refreshSelectedTargets
  /**
   ** Refreshing the state of the UI component which displays the table of
   ** selected items.
   */
  protected void refreshSelectedTargets() {
    ADF.partialRender(getSelectionTable());
    ADF.partialRender(getRemoveAllButton());
    ADF.partialRender(getRemoveSelectedButton());
    ADF.partialRender(getAddButton());
  }
}