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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Lookup.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;

import org.apache.myfaces.trinidad.model.RowKeySet;

import oracle.adf.view.rich.model.QueryModel;
import oracle.adf.view.rich.model.TableModel;
import oracle.adf.view.rich.model.QueryDescriptor;
import oracle.adf.view.rich.model.ListOfValuesModel;

import oracle.hst.foundation.faces.backing.AbstractBean;
import oracle.hst.foundation.faces.backing.AbstractModel;

////////////////////////////////////////////////////////////////////////////////
// class AbstractLookupModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Declares global accessible methods to render Identity Manager Lookup
 ** Definitions.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AbstractLookupModel<T extends AbstractModel> extends ListOfValuesModel {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final AbstractBean       model;
  private final AbstractQueryModel query;
  private final DataProvider<T>    provider;

  private QueryDescriptor          latest;
  private Integer                  selectedIndex;
  private List<T>                  entries;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractLookupModel</code> which use the specified
   ** {@link AbstractModel} as the model.
   **
   ** @param  model              the {@link AbstractModel} providing the model.
   ** @param  provider           the {@link DataProvider} providing the data.
   */
  public AbstractLookupModel(final Class<T> model, final DataProvider<T> provider) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.model    = AbstractBean.create(model);
    this.provider = provider;

    QueryDescriptor defaultQd = new AbstractQueryDescriptor(this.model, "Default");
    this.query   = new AbstractQueryModel(this.model, Arrays.<QueryDescriptor>asList(defaultQd));
    this.entries = this.provider.search(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataProvider
  public DataProvider<T> getDataProvider() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSelectedData
  public T getSelectedData() {
    return this.entries.get(this.selectedIndex);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getQueryDescriptor (ListOfValuesModel)
  /**
   ** Returns a {@link QueryDescriptor} required to render the criteria part of
   ** a query component. A <code>null</code> value indicates that a query
   ** component is not used for searching a value among a list of values.
   **
   ** @return                    a {@link QueryDescriptor} instance.
   **                            Usually the selected {@link QueryDescriptor}.
   */
  @Override
  public QueryDescriptor getQueryDescriptor() {
    return this.query.getCurrentDescriptor();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getQueryModel (ListOfValuesModel)
  /**
   ** Returns a {@link QueryModel} required by a query component.
   ** <p>
   ** A <code>null</code> value indicates that a query component is not required
   ** for searching a value among a list of values.
   **
   ** @return                    a {@link QueryModel} instance.
   */
  @Override
  public QueryModel getQueryModel() {
    return this.query;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTableModel (ListOfValuesModel)
  /**
   ** Returns a {@link TableModel} object that is in turn used to retrieve a
   ** {@link TableModel} which supplies a list of values to select from.
   ** <p>
   ** A {@link TableModel} object should always be provided.
   **
   ** @return                    a {@link TableModel} instance.
   */
  @Override
  public TableModel getTableModel() {
    return new AbstractTableModel(this.model, this.entries);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   autoCompleteValue (ListOfValuesModel)
  /**
   ** Called by the framework during Apply Request values phase.
   ** <p>
   ** This method is called only when <code>autoSubmit</code> is set to
   ** <code>true</code> on the component and user either presses enter key or
   ** tab key on the input field. The method determines the number of matches
   ** the parameter 'value' has in the list of values.
   ** <p>
   ** Returns <code>null</code>, if no match was found, otherwise returns a
   ** {@link List} of rows that match the value. In the case of an exact match,
   ** the {@link List} contains a single entry. The type of object returned is
   ** left to the discretion of the model implementor, but it should be noted
   ** that in the case of a single match, the value obtained from list.get(0) is
   ** passed to the {@link #valueSelected(Object)} methods.
   **
   ** @param  value              the user entered value in the input field.
   **
   ** @return                    a {@link List} of Object instances.
   */
  @Override
  public List<Object> autoCompleteValue(final Object value) {
    return Collections.emptyList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAutoCompleteEnabled (ListOfValuesModel)
  /**
   ** Whether the autoComplete feature is enabled for the LOV component.
   **
   ** @return                    <code>true</code> if the autoComplete feature
   **                            is enabled; otherwise <code>false</code>.
   */
  @Override
  public boolean isAutoCompleteEnabled() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueSelected (ListOfValuesModel)
  /**
   ** This method is called by the framework at the end of the Invoke
   ** Application phase, to set parameter 'value' as the selected value in the
   ** list.
   ** <p>
   ** This method sets up the model such that results/related fields could show
   ** the relevant data. Also, this method results in value to be added to the
   ** most-recently-used or favorites list.
   **
   ** @param  value              the value can be
   **                            <ul>
   **                              <li>a List&lt;Object&gt; of size 1, where
   **                                  <code>Object</code> belongs to list
   **                                  returned by the call to
   **                                  {@link #getItems()} (or
   **                                  {@link #getRecentItems()})
   **                              <li>a RowKeySet, containing the rowKeys of
   **                                  the selected rows belonging to the
   **                                  <code>Collection Model</code>.
   **                              <li>Object, which happens to be the single
   **                                  matched row, returned by call to
   **                                  {@link #autoCompleteValue(Object)}
   **                            </ul>
   */
  @Override
  public void valueSelected(final Object value) {
    final RowKeySet rks = (RowKeySet)value;
    Iterator<Object> keyIterator = rks.iterator();
    if (keyIterator.hasNext()) {
      this.selectedIndex = (Integer)keyIterator.next();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getItems (ListOfValuesModel)
  /**
   ** Returns a list of items used within the searchable list in an
   ** inputComboxListOfValues. This does not include the recentItems.
   **
   ** @return                    a {@link List} of Object instances.
   **
   ** @see     #getRecentItems()
   */
  @Override
  public List<? extends Object> getItems() {
    return Collections.emptyList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRecentItems (ListOfValuesModel)
  /**
   ** Returns a list of recently selected items (or MRU items) used within the
   ** searchable list in an inputComboxListOfValues
   **
   ** @return                    a {@link List} of Object instances.
   */
  @Override
  public List<? extends Object> getRecentItems() {
    return Collections.emptyList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   performQuery (ListOfValuesModel)
  /**
   ** Called when a query is to be performed on a {@link QueryDescriptor}.
   ** <p>
   ** This method will be called when the user enters his search criteria and
   ** hits a button to perform the search, in order to select a value for the
   ** LOV field.
   **
   ** @param  descriptor         the {@link QueryDescriptor} for which the query
   **                            is to be performed.
   */
  @Override
  public void performQuery(final QueryDescriptor descriptor) {
    this.entries = this.provider.search(descriptor);
    this.latest  = descriptor;
  }
}