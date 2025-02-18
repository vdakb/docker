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

    File        :   AbstractSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractSearch.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-03-03  DSteding    First release version
*/

package bka.employee.portal.view.state;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.io.Serializable;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import org.apache.myfaces.trinidad.event.SelectionEvent;

import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.view.rich.component.rich.RichMenu;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.layout.RichToolbar;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.hst.foundation.faces.ADF;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractSearch
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods a user interface search state provides.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractSearch extends AbstractBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String           GENERIC_UNDO     = "GENERIC_UNDO";
  public static final String           GENERIC_DELETE   = "GENERIC_DELETE";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-413582475455389212")
  private static final long            serialVersionUID = 2895125502927971640L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient ComponentReference toolbar;
  private transient ComponentReference actionMenu;
  private transient ComponentReference searchTable;
  private transient ComponentReference totalRowCountLabel;

  private transient RichPopup          confirmPopup = null;

  private Serializable                 selected;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractSearch</code> managed bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractSearch() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a search state <code>ManagedBean</code> which maps to the
   ** specified id and name attribute metadata.
   **
   ** @param  id                 the name of the key field.
   ** @param  name               the name of the name field.
   */
  protected AbstractSearch(final String id, final String name) {
    // ensure inheritance
    super(id, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a search state <code>ManagedBean</code> which maps to the
   ** specified id, name and status attribute metadata.
   **
   ** @param  id                 the name of the key field.
   ** @param  name               the name of the name field.
   ** @param  status             the name of the status field.
   */
  protected AbstractSearch(final String id, final String name, final String status) {
    // ensure inheritance
    super(id, name, status);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setConfirmPopup
  /**
   ** Sets the UI component which renders the confirmation popup.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   **
   ** @param  component          the UI component which renders the confirmation
   **                            popup.
   */
  public void setConfirmPopup(final RichPopup component) {
    this.confirmPopup = component;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getConfirmPopup
  /**
   ** Returns the UI component which renders the confirmation popup.
   **
   ** @return                    the UI component which renders the confirmation
   **                            popup.
   */
  public RichPopup getConfirmPopup() {
    return this.confirmPopup;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setToolbar
  /**
   ** Sets the UI component which renders the toolbar of the search result
   ** table display.
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
   **   public void setToolbar(final RichToolbar component) {
   **     this.toolBar = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference toolBar;
   **   ...
   **   public void setToolbar(final RichToolbar component) {
   **     this.toolBar = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the action menu
   **                            of the search result table display.
   */
  public final void setToolbar(final RichToolbar component) {
    this.toolbar = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getToolbar
  /**
   ** Returns the UI component which renders the action menu of the search
   ** result table display.
   **
   ** @return                    the UI component which renders the action menu
   **                            of the search result table display.
   */
  public final RichToolbar getToolbar() {
    return (this.toolbar != null) ? (RichToolbar)this.toolbar.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setActionMenu
  /**
   ** Sets the UI component which renders the action menu of the search result
   ** table display.
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
   **   public void setActionMenu(final RichMenu component) {
   **     this.actionMenu = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference actionMenu;
   **   ...
   **   public void setActionMenu(final RichMenu component) {
   **     this.actionMenu = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the action menu
   **                            of the search result table display.
   */
  public final void setActionMenu(final RichMenu component) {
    this.actionMenu = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getActionMenu
  /**
   ** Returns the UI component which renders the action menu of the search
   ** result table display.
   **
   ** @return                    the UI component which renders the action menu
   **                            of the search result table display.
   */
  public final RichMenu getActionMenu() {
    return (this.actionMenu != null) ? (RichMenu)this.actionMenu.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTotalRowCountLabel
  /**
   ** Sets the UI component which renders the estimated number of rows in the
   ** status region of the search result table display.
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
   **   public void setTotalRowCountLabel(final RichOutputText component) {
   **     this.totalRowCountLabel = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference totalRowCountLabel;
   **   ...
   **   public void setTotalRowCountLabel(final RichOutputText component) {
   **     this.totalRowCountLabel = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the estimated
   **                            number of rows in the status region of the
   **                            search result table display.
   */
  public final void setTotalRowCountLabel(final RichOutputText component) {
    this.totalRowCountLabel = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTotalRowCountLabel
  /**
   ** Returns the UI component which renders the estimated number of rows in the
   ** status region of the search result table display.
   **
   ** @return                    the UI component which renders the estimated
   **                            number of rows in the status region of the
   **                            search result table display.
   */
  public final RichOutputText getTotalRowCountLabel() {
    return (this.totalRowCountLabel != null) ? (RichOutputText)this.totalRowCountLabel.getComponent() : null;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSelectedRow
  /**
   ** Returns the collection of selected {@link Row}s.
   ** <br>
   ** This is a shortcut used by the UI components and provides the same
   ** functionality as {@link #selectedRow(RichTable)}.
   **
   ** @return                    the {@link List} of selected rows in the bound
   **                            search table component.
   */
  public List<Row> getSelectedRow() {
    return selectedRow(getSearchTable());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSelectedRowCount
  /**
   ** Returns the number of selected of rows in the {@link RichTable}.
   **
   ** @return                    the number of selected of rows in the
   **                            {@link RichTable}.
   */
  public int getSelectedRowCount() {
    return selectedRowCount(getSearchTable());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSearchTable
  /**
   ** Sets the UI component which renders the search result table display.
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
   **   public void setSearchTable(final RichTable component) {
   **     this.searchTable = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference searchTable;
   **   ...
   **   public void setSearchTable(final RichTable component) {
   **     this.searchTable = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the task header
   **                            display.
   */
  public final void setSearchTable(final RichTable component) {
    this.searchTable = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSearchTable
  /**
   ** Returns the UI component which renders the search result table display.
   **
   ** @return                    the UI component which renders the search
   **                            result table display.
   */
  public RichTable getSearchTable() {
    return (this.searchTable != null) ? (RichTable)this.searchTable.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSelected
  /**
   ** Sets the value of the selected property.
   **
   ** @param  value              the value of the selected property.
   **                            Allowed object is {@link Serializable}.
   */
  protected void setSelected(final Serializable value) {
    this.selected = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSelected
  /**
   ** Returns the value of the selected property.
   **
   ** @return                    the value of the selected property.
   **                            Possible object is {@link String}.
   */
  public Serializable getSelected() {
    return this.selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteratorHasRows
  /**
   ** Returns <code>true</code> if the specified query iterator has at least one
   ** row.
   **
   ** @param  binding            the fullqualified name of the iterator binding
   **                            to check.
   **
   ** @return                    <code>true</code> if the query iterator has at
   **                            least one row; otherwise <code>false</code>.
   */
  protected boolean iteratorHasRows(final String binding) {
    final RowSetIterator rsi = rowSetIterator(binding);
    // latch the current position of the iterator to restore it later
    final int            old = rsi.getCurrentRowIndex();
    rsi.setCurrentRowAtRangeIndex(0);
    final Row row = rsi.first();
    // restore the current position of the row iterator
    rsi.setCurrentRowAtRangeIndex(old);
    return row != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteratorAllRow
  /**
   ** Returns the {@link List} of all rows from the specified binding iterator.
   **
   ** @param  binding            the fullqualified name of the iterator binding
   **                            to return all rows from.
   **
   ** @return                    the {@link List} of all rows from the specified
   **                            binding iterator.
   */
  protected List<Row> iteratorAllRow(final String binding) {
    final RowSetIterator rsi = rowSetIterator(binding);
    // latch the current position of the iterator to restore it later
    final int            old = rsi.getCurrentRowIndex();
    rsi.setRangeSize(100);
    rsi.setCurrentRowAtRangeIndex(0);
    List<Row> rows = new ArrayList<Row>();
    // scroll the range to first row in first page
    final Row r = rsi.first();
    if (r != null)
      rows.add(r);

    Row[] cur = rsi.getAllRowsInRange();
    if (cur!=null) {
      rows.addAll(Arrays.asList(cur));
    }

    // Adding counter to check for end of current page. When rsi is on the
    // last node and next() is called, the next row is loaded, eventually
    // loading all rows.
    while ((cur = rsi.getNextRangeSet()).length != 0 && rows.size() < 1001) {
      rows.addAll(Arrays.asList(cur));
      if( rows.size() > 1000) {
        rows = rows.subList(0, 1000);
        ADF.current().getRequestScope().put("overThousandResultsPicker", ADF.resourceBundleValue("oracle.iam.ui.OIMUIBundle", "SEARCH_PICKER_OVER_THOUSAND_RESULT"));
        break;
      }
    }
    // restore the current position of the row iterator
    rsi.setCurrentRowAtRangeIndex(old);
    return rows;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSelect
  /**
   ** Handles the event if the selection in the search table becomes to change.
   **
   ** @param  event              the {@link SelectionEvent} delivered from the
   **                            action occured in a UIXTable component.
   */
  public void searchSelect(final @SuppressWarnings("unused") SelectionEvent event) {
    setSelected(selectedRowKey(getSearchTable()));
    refreshSearchTableTargets();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   refresh
  /**
   ** Refreshing the state of the UI component which displays the table of
   ** queried entries and controls the further actions for those enries in this
   ** table.
   **
   ** @param  iterator           the name of the iterator binding to refresh
   */
  protected void refresh(final String iterator) {
    reset();

    final DCIteratorBinding binding = ADF.iteratorBinding(iterator);
    if (binding != null)
      binding.executeQuery();

    refreshSearchTableStatus();
    ADF.partialRender(getSearchTable());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  /**
   ** Helper method to Rollback previous search data, to fix ADFbc caching
   ** issue.
   */
  protected void reset() {
    resetSelectedRowKey(getSearchTable());
// TODO: remove comment
//    ADF.executeOperation("Rollback");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshSearchTableStatus
  /**
   ** Refresh the state of the search result table by reseting any selection and
   ** triggering a partial rendering of the components which display the state
   ** of the search result table.
   ** <p>
   ** The partial rendering request does not enqueue a partial rendering request
   ** of the search table component itself.
   */
  protected void refreshSearchTableStatus() {
    resetSelectedRowKey(getSearchTable());
    refreshSearchTableTargets();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshSearchTableTargets
  /**
   ** Refresh the state of the ccomponents like toolbar and menubar which
   ** triggers actions on the search result table.
   */
  protected void refreshSearchTableTargets() {
    if (getToolbar() != null) {
      ADF.partialRender(getToolbar());
    }
    if (getActionMenu() != null) {
      ADF.partialRender(getActionMenu());
    }
  }
}