/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Unique Identifier Administration 

    File        :   SearchModel.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.model;

import java.util.Map;
import java.util.Calendar;

import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;

import oracle.hst.platform.core.stream.Collectors;

import oracle.hst.platform.core.reflect.Discovery;

import oracle.hst.platform.jpa.SortOption;
import oracle.hst.platform.jpa.SearchFilter;

import bka.iam.identity.jpa.provider.Base;

////////////////////////////////////////////////////////////////////////////////
// abstract class SearchModel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>SearchModel</code> provides common behavior definition of a
 ** paginated resultset leveraging a {@link LazyDataModel}.
 ** <p>
 ** The page size is managed by a session scoped bean to keep it consistent
 ** across view requests.
 **
 ** @param  <T>                  the type of the source entity representations.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class SearchModel<T extends Base> extends LazyDataModel<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String APP              = "app$bundle";
  public static final String UID              = "uid$bundle";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2872837170927701785")
  private static final long  serialVersionUID = 637502897161402446L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected boolean          filter;
  protected String           status;
  protected T                selected;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SearchModel</code> that allows use as a JavaBean.
   **
   ** @param  pageSize           the initial value of the page size to display.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  protected SearchModel(final int pageSize) {
    // ensure inheritance
    super();

    // initialize instance
    // must be invoked on super class to avoid subclass involved catching UI
    // event
    super.setPageSize(pageSize);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModel
  /**
   ** Returns the collection model of elements.
   **
   ** @return                    the collection model of <code>T</code>.
   **                            <br>
   **                            Possible object is {@link LazyDataModel} for
   **                            type <code>T</code>.
   */
  public LazyDataModel<T> getModel() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSelected
  /**
   ** Sets the selected table entry.
   **
   ** @param  value              the selected table entry.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  public void setSelected(final T value) {
    this.selected = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSelected
  /**
   ** Returns the selected table entry.
   **
   ** @return                    the selected table entry.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public T getSelected() {
    return this.selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setStatus
  /**
   ** Sets the search value of the <code>active</code> property.
   **
   ** @param  value              the search value of the <code>active</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setStatus(final String value) {
    this.status = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStatus
  /**
   ** Returns the search value of the <code>active</code> property.
   **
   ** @return                    the search value of the <code>active</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getStatus() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Togggles the <code>filter</code> mode of the current view to show or hide
   ** the input fields to apply the search criteria.
   */
  public void filter() {
    this.filter = !this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isFilter
  /**
   ** Returns <code>true</code> if the view is in mode <code>filter</code> mode
   ** and displays the input fields to apply the search criteria.
   **
   ** @return                    <code>true</code> if the view is in mode
   **                            <code>filter</code> mode and displays the input
   **                            fields to apply the search criteria.
   **                            <br>
   **                            Possible object is <code>true</code>.
   */
  public boolean isFilter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Callback method invoke by the action <code>refresh</code> in the UI to
   ** update the view with the current state of the persistence layer.
   */
  public abstract void refresh();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  /**
   ** Method invoke by the action callbacks to reset the state of the current
   ** view with the current state of the persistence layer.
   */
  protected void reset() {
    this.filter   = false;
    this.selected = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applySort
  /**
   ** Evaluates to sort order criteria to apply on the view.
   **
   ** @param  criteria           the collection of sort metadata to apply.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link SortMeta} for the value.
   **
   ** @return                    the {@link SortOption} to be passed to the
   **                            persistence layer.
   **                            <br>
   **                            Possible object is {@link SortOption}.
   */
  protected SortOption applySort(final  Map<String, SortMeta> criteria) {
    SortOption option = null;
    if (!criteria.isEmpty()) {
      option = SortOption.by(
        criteria.values().stream()
          .filter(d -> !d.getOrder().isUnsorted())
          .map(meta -> SortOption.Order.by(meta.getField(), meta.getOrder().isAscending() ? SortOption.Direction.ASCENDING : SortOption.Direction.DESCENDING))
          .collect(Collectors.toList())
      );
    }
    return option;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyFilter
  /**
   ** Evaluates to filter criteria to apply on the view.
   **
   ** @param  criteria           the collection of filter metadata to apply.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link FilterMeta} for the value.
   **
   ** @return                    <code>true</code> if the value provided by
   **                            <code>object</code> match the filter
   **                            <code>criteria</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected SearchFilter applyFilter(final Map<String, FilterMeta> criteria) {
    // a "little" effort is required to build a proper search filter
    // the filter metadata providing only the condition values to apply without
    // any information about the type of the underlying implementation
    // the active checkbox is hitting a converter at persistence layer that
    // expects a boolean to be converted in an integer hence this attribute
    // needs special preparation for search filter
    adjustFilter(criteria);
    SearchFilter filter = null;
    if (!criteria.isEmpty()) {
      filter = SearchFilter.and(
        criteria.values().stream()
          .filter(m -> !Discovery.empty(m.getFilterValue()))
          .map(m -> criteriaValue(m))
          .collect(Collectors.toList())
      );
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adjustFilter
  /**
   ** Handle search behavior.
   **
   ** @param  criteria           the collection of filter metadata to apply.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link FilterMeta} for the value.
   */
  protected void adjustFilter(final Map<String, FilterMeta> criteria) {
    // a "little" effort is required to build a proper search filter
    // the filter metadata providing only the condition values to apply without
    // any information about the type of the underlying implementation
    // the active checkbox is hitting a converter at persistence layer that
    // expects a boolean to be converted in an integer hence this attribute
    // needs special preparation for search filter
    if (criteria.containsKey(Base.ACTIVE)) {
      final FilterMeta meta  = criteria.get(Base.ACTIVE);
      final Object     value = meta.getFilterValue();
      if (value == null || "0".equals(value))
        criteria.remove(Base.ACTIVE);
      else
        meta.setFilterValue("1".equals(value));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   criteriaValue
  /**
   ** Obtains the value for specific <code>field</code> from filter criteria.
   **
   ** @param  criteria           the collection of filter metadata to apply.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link FilterMeta} for the value.
   **
   ** @return                    <code>null</code> if no value provided by
   **                            <code>criteria</code>; otherwise the value
   **                            mapped at <code>field</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link SearchFilter.Criteria}.
   */
  private  static SearchFilter<?> criteriaValue(final FilterMeta meta) {
    final Object value = meta.getFilterValue();
    switch (meta.getMatchMode()) {
      case LESS_THAN           : // TODO: the ugly way doesn't feel good
                                 if (value instanceof Number)
                                   return SearchFilter.lt(meta.getField(), (Number)value);
                                 else if (value instanceof Calendar)
                                   return SearchFilter.lt(meta.getField(), (Calendar)value);
                                 else
                                   throw new IllegalArgumentException();
      case LESS_THAN_EQUALS    : // TODO: the ugly way doesn't feel good
                                 if (value instanceof Number)
                                   return SearchFilter.le(meta.getField(), (Number)value);
                                 else if (value instanceof Calendar)
                                   return SearchFilter.le(meta.getField(), (Calendar)value);
                                 else
                                   throw new IllegalArgumentException();
      case GREATER_THAN        : // TODO: the ugly way doesn't feel good
                                 if (value instanceof Number)
                                   return SearchFilter.gt(meta.getField(), (Number)value);
                                 else if (value instanceof Calendar)
                                   return SearchFilter.gt(meta.getField(), (Calendar)value);
                                 else
                                   throw new IllegalArgumentException();
      case GREATER_THAN_EQUALS : // TODO: the ugly way doesn't feel good
                                 if (value instanceof Number)
                                   return SearchFilter.ge(meta.getField(), (Number)value);
                                 else if (value instanceof Calendar)
                                   return SearchFilter.ge(meta.getField(), (Calendar)value);
                                 else
                                   throw new IllegalArgumentException();
      case CONTAINS            : return SearchFilter.co(meta.getField(), (String)value);
      case ENDS_WITH           : return SearchFilter.ew(meta.getField(), (String)value);
      case STARTS_WITH         : return SearchFilter.sw(meta.getField(), (String)value);
      default                  : // TODO: the ugly way doesn't feel good
                                 if (value instanceof Boolean)
                                   return SearchFilter.eq(meta.getField(), (Boolean)value);
                                 else if (value instanceof Number)
                                   return SearchFilter.eq(meta.getField(), (Number)value);
                                 else if (value instanceof Calendar)
                                   return SearchFilter.eq(meta.getField(), (Calendar)value);
                                 else  if (value instanceof String)
                                   return SearchFilter.eq(meta.getField(), (String)value);
                                 else
                                   throw new IllegalArgumentException();
    }
  }
}