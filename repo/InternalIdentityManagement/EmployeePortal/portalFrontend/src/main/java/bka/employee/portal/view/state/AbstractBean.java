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

    File        :   AbstractBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package bka.employee.portal.view.state;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import javax.faces.component.UIViewRoot;

import javax.faces.application.ViewHandler;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.model.RowKeySet;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierBinding;

import oracle.iam.ui.platform.view.backing.AttributeBean;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import bka.employee.portal.AbstractManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class AbstractBean
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Declares methods a user interface state provides.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class AbstractBean extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String    PARAMETER_TASKFLOW     = "taskFlowId";
  public static final String    PARAMETER_MODE         = "mode";

  protected static final String EVENT_DISTINGUISHER    = "eventDistinguisher";
  protected static final String EVENT_SELECTIONTYPE    = "selectionType";
  protected static final String SELECTIONTYPE_SINGLE   = "single";
  protected static final String SELECTIONTYPE_MULTIPLE = "multiple";

  protected static final String MODE_VIEW              = "view";
  protected static final String MODE_EDIT              = "edit";
  protected static final String MODE_CREATE            = "create";
  protected static final String MODE_ASSIGN            = "assign";

  /** the action binding to submit any changes */
  protected static final String SUBMIT                 = "commit";

  /** the action binding to revert any changes */
  protected static final String REVERT                 = "rollback";

  /** the outcome if the action binding {@link #SUBMIT} was successful */
  protected static final String SUCCESS                = "success";

  /** the outcome of the action binding {@link #ERROR} ended up with errors */
  protected static final String ERROR                  = "error";

  /** the resource bundle providing the common localized values */
  protected static final String BUNDLE                 = "bka.employee.portal.bundle.Foundation";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8973124290075666503")
  private static final long     serialVersionUID = 522349454048590583L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String          id;
  private final String          name;
  private final String          status;

  private String                actionName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ManagedBean</code> managed bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractBean() {
    // ensure inheritance
    this(null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ManagedBean</code> managed bean which mappes the
   ** specified id and name attribute metadata.
   **
   ** @param  id                 the name of the key field.
   ** @param  name               the name of the name field.
   */
  protected AbstractBean(final String id, final String name) {
    // ensure inheritance
    this(id, name, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ManagedBean</code> managed bean which mappes the
   ** specified id and name attribute metadata.
   **
   ** @param  id                 the name of the key field.
   ** @param  name               the name of the name field.
   ** @param  status             the name of the status field.
   */
  protected AbstractBean(final String id, final String name, final String status) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.id     = id;
    this.name   = name;
    this.status = status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the value of the id property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the id property.
   **                            Possible object is {@link String}.
   */
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the value of the name property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the name property.
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the value of the status property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the status property.
   **                            Possible object is {@link String}.
   */
  public String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setActionName
  /**
   ** Sets the value of the actionName property.
   **
   ** @param  value              the value of the actionName property.
   **                            Allowed object is {@link String}.
   */
  public void setActionName(final String value) {
    this.actionName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActionName
  /**
   ** Returns the value of the actionName property.
   **
   ** @return                    the value of the actionName property.
   **                            Possible object is {@link String}.
   */
  public String getActionName() {
    return this.actionName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLocalizedAction
  /**
   ** Returns the localized action label.
   **
   ** @return                    the localized action label.
   */
  public String getLocalizedAction() {
    // allow sub classes to kick in to localize action
    final String actionName = getActionName();
    return (StringUtility.isEmpty(actionName)) ? "???-emptyornull-???" : ADF.resourceBundleValue(BUNDLE, actionName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshPage
  /**
   ** Reload page
   */
  public void refreshPage() {
    final FacesContext faces   = JSF.context();
    final String       page    = faces.getViewRoot().getViewId();
    final ViewHandler  handler = faces.getApplication().getViewHandler();
    final UIViewRoot   root    = handler.createView(faces, page);
    root.setViewId(page);
    faces.setViewRoot(root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   estimatedRowCount
  /**
   ** Actually returns the number of rows in the specified component by
   ** hitting getQueryHitCount() which runs the select count(*) on the VO query
   ** iterator.
   **
   ** @param  iterator           the name of the iterator bindung to get the
   **                            estimated row number from.
   **
   ** @return                    the estimated number of rows.
   */
  protected int estimatedRowCount(final String iterator) {
    final String method= "estimatedRowCount";
    entering(method);

    int count = 0;
    if (iterator == null || iterator.equals("")) {
      return count;
    }

    final Long expression = JSF.valueFromExpression("#{bindings." + iterator + ".estimatedRowCount}", Long.class);
    if (expression != null || expression.longValue() > 0)
      count = expression.intValue();

    exiting(method);
    return count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowSelected
  /**
   ** Returns <code>true</code> if the selection state for the specified
   ** componenent <code>component</code> has a selection.
   **
   ** @param  component          the {@link RichTable} component to verify for
   **                            the selection state.
   **
   ** @return                    <code>true</code> if the selection state for
   **                            the specified {@link RichTable}
   **                            <code>component</code> is available; otherwise
   **                            <code>false</code>.
   */
  protected boolean rowSelected(final RichTable component) {
    final String method= "rowSelected";
    entering(method);
    boolean result = selectedRowCount(component) > 0;
    exiting(method);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRowCount
  /**
   ** Returns the number of selected of rows in the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            number of selected rows for.
   **
   ** @return                    the number of selected of rows in the specified
   **                            specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected int selectedRowCount(final RichTable component) {
    final String method= "selectedRowCount";
    entering(method);

    int count = 0;
    if (component == null) {
      exiting(method);
      return count;
    }

    final RowKeySet rowSet = component.getSelectedRowKeys();
    if (rowSet != null)
      count = rowSet.getSize();

    exiting(method);
    return count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRowCount
  /**
   ** Returns the number of selected of rows by status if passed or by default
   ** fetchAll in the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            number of selected rows for.
   ** @param  status             the status information any row needs to match
   **                            to be populated.
   **
   ** @return                    the number of selected of rows in the specified
   **                            specified {@link RichTable}
   **                            <code>component</code> which match the
   **                            specified status.
   */
  protected int selectedRowCount(final RichTable component, final Object status) {
    return selectedRowKeys(component, status).size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRowKey
  /**
   ** Returns the identifier of the current selected row in the specified
   ** component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            {@link List} of rows of selected rows for.
   **
   ** @return                    the identifier of the current selected row in
   **                            the specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected Serializable selectedRowKey(final RichTable component) {
    return selectedRowKey(id(), component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRowKey
  /**
   ** Returns the identifier of the current selected row in the specified
   ** component.
   **
   ** @param  identifier         the name of the identifying attribute in the
   **                            row collection.
   ** @param  component          the {@link RichTable} component to return the
   **                            {@link List} of rows of selected rows for.
   **
   ** @return                    the identifier of the current selected row in
   **                            the specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected Serializable selectedRowKey(final String identifier, final RichTable component) {
    final String method= "selectedRowKey";
    entering(method);
    Serializable keyValue = null;
    if (selectedRowCount(component) == 1) {
      final List<Object> obj = selectedTableData(component);
      if (obj != null && obj.size() == 1) {
        final AttributeBean bean = (AttributeBean)obj.get(0);
        keyValue = bean.getKey();
      }
      else {
        exiting(method);
        throw new RuntimeException("Could not find " + identifier + " in table " + component.getSummary());
      }
    }
    exiting(method);
    return keyValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRowKeys
  /**
   ** Returns the keys of selected of rows in the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            keys of selected rows for.
   **
   ** @return                    the keys of selected of rows in the specified
   **                            specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected List<Object> selectedRowKeys(final RichTable component) {
    final String method= "selectedRowKeys";
    entering(method);

    final List<Object> result = new ArrayList<Object>();
    RowKeySet rowKeySet = null;
    if (component != null)
      rowKeySet = component.getSelectedRowKeys();

    if (rowKeySet == null || rowKeySet.getSize() <= 0) {
      exiting(method);
      return result;
    }

    final Iterator<Object> rows = rowKeySet.iterator();
    while (rows.hasNext()) {
      final Iterator<Key> keys = ((Collection)rows.next()).iterator();
      while (keys.hasNext()) {
        final Key key = keys.next();
        result.add(key.getAttributeValues()[0]);
        // TODO: Need to see the possiblity of surrogated key ?
        break;
      }
    }

    exiting(method);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRowKeys
  /**
   ** Returns the keys of selected of rows by status if passed or by default
   ** fetchAll in the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            keys of selected rows for.
   ** @param  status             the status information any row needs to match
   **                            to be populated.
   **
   ** @return                    the keys of selected of rows in the specified
   **                            specified {@link RichTable}
   **                            <code>component</code> which match the
   **                            specified status..
   */
  protected List<Object> selectedRowKeys(final RichTable component, final Object status) {
    return selectedTableData(component, status, true, false, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedTableData
  /**
   ** Returns the {@link List} of the selected row data by default fetchAll in
   ** the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            {@link List} of selected data for.
   **
   ** @return                    the {@link List} of selected data in the
   **                            specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected List<Object> selectedTableData(final RichTable component) {
    // fetch row data for selected rows.
    return selectedTableData(component, null, false, false, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedTableData
  /**
   ** Returns the {@link List} of the selected row data by status if passed or
   ** by default fetchAll in the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            {@link List} of selected data for.
   ** @param  status             the status information any row needs to match
   **                            to be populated.
   **
   ** @return                    the {@link List} of selected data in the
   **                            specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected List<Object> selectedTableData(final RichTable component, final Object status) {
    // fetch row data for selected rows based on status.
    return selectedTableData(component, status, false, false, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedTableData
  /**
   ** Returns the {@link List} of the selected row data by status if passed or
   ** by default fetchAll in the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            {@link List} of selected data for.
   ** @param  status             the status information any row needs to match
   **                            to be populated.
   ** @param  addBreak           if <code>true</code>, only one row will be
   **                            fetched.
   ** @param  attributeList      the list of attributes to be populated.
   **
   ** @return                    the {@link List} of selected data in the
   **                            specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected List<Object> selectedTableData(final RichTable component, final Object status, final boolean addBreak, final String[] attributeList) {
    return selectedTableData(component, status, false, addBreak, attributeList);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedTableData
  /**
   ** Returns the {@link List} of selected data in the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            {@link List} of selected data for.
   ** @param  status             the status information any row needs to match
   **                            to be populated.
   ** @param  onlyKeys           if <code>true</code> only the IDs of selected
   **                            rows will be fetched.
   ** @param  addBreak           if <code>true</code>, only one row will be
   **                            fetched.
   ** @param  attributeList      the list of attributes to be populated.
   **
   ** @return                    the {@link List} of selected data in the
   **                            specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected List<Object> selectedTableData(final RichTable component, final Object status, final boolean onlyKeys, final boolean addBreak, final String[] attributeList) {
    final Set<Object> latch = new HashSet<Object>();
    if (component != null) {
      final JUCtrlHierNodeBinding rowNode = (JUCtrlHierNodeBinding)component.getRowData();
      if (rowNode != null) {
        // susujosh - for commandlink click return the data from rowData.
        // this way we can handle generically command link returning data from
        // rowData rather than from selection and find by key
        final Row row = rowNode.getRow();
        boolean addRow = false;
        // if status is not null, then add the row depending on the expected
        // status
        if (status != null) {
          final Object statusValue = row.getAttribute(status());
          if (status.equals(statusValue))
            addRow = true;
        }
        else
          addRow = true;

        // if current row not to add; continue to fetch another.
        if (!addRow)
          return new ArrayList<Object>(latch);

        Object   id           = null;
        Object   value        = null;
        Object[] rowAttrValue = null;
        // if attributeList is null; get only ID and Name
        if (attributeList == null) {
          id    = row.getAttribute(id());
          value = id;
          if (name() != null)
            value = row.getAttribute(name());
        }
        else {
          // populate the attributeList with rowData
          rowAttrValue = new Object[attributeList.length];
          for (int i = 0; i < attributeList.length; i++)
            rowAttrValue[i] = row.getAttribute(attributeList[i]);
        }
        if (onlyKeys) {
          // if only keys required, then send ID_FIELD instead of attributeList
          latch.add(id);
        }
        else if (attributeList != null) {
          latch.add(rowAttrValue);
        }
        else {
          latch.add(new AttributeBean((Serializable)id, (Serializable)value));
        }
        return new ArrayList<Object>(latch);
      }
    }

    RowSetIterator rsi = null;
    if (component != null && component.getValue() != null && component.getValue() instanceof FacesCtrlHierBinding.FacesModel) {
      final FacesCtrlHierBinding.FacesModel tree = (FacesCtrlHierBinding.FacesModel)component.getValue();
      if (tree.getBinding() != null && tree.getBinding().getIteratorBinding() != null)
        rsi = tree.getBinding().getIteratorBinding().getRowSetIterator();
    }

    final RowKeySet rks =  (component == null) ? null : component.getSelectedRowKeys();
    if (rsi == null || rks == null || rks.getSize() <= 0)
      return new ArrayList<Object>(latch);

    final Iterator<Object> cursor = rks.iterator();
    while (cursor.hasNext()) {
      final Iterator<Object> row = ((Collection)cursor.next()).iterator();
      while (row.hasNext()) {
        final Key key    = (Key)row.next();
        final Row rows[] = rsi.findByKey(key, 1);
        if (rows.length == 0)
          continue;

        boolean addRow = false;
        // if status is not null, then add the row depending on the expected
        // status
        if (status != null) {
          final Object statusValue = rows[0].getAttribute(status());
          if (status.equals(statusValue))
            addRow = true;
        }
        else
          addRow = true;

        // if current row not to add; continue to fetch another.
        if (!addRow)
          continue;

        Object   id    = null;
        Object   name  = null;
        Object[] value = null;
        // if attributeList is null; get only ID and Name
        if (attributeList == null) {
          id    = rows[0].getAttribute(id());
          name  = id;
          if (name() != null)
            name = rows[0].getAttribute(name());
        }
        else {
          // populate the attribute values with row data
          value = new Object[attributeList.length];
          for (int i = 0; i < attributeList.length; i++) {
            value[i] = rows[0].getAttribute(attributeList[i]);
          }
        }
        // if only keys required, then send ID_FIELD instead of attributeList
        if (onlyKeys)
          latch.add(id);
        else if (attributeList != null)
          latch.add(value);
        else
          latch.add(new AttributeBean((Serializable)id, (Serializable)name));

        // forcebily breaking, since we don't support multiple keys
        break;
      }
      // add break, if only one value is required.
      if (addBreak)
        break;
    } // endof while loop
    return new ArrayList<Object>(latch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetSelectedRowKey
  /**
   ** Reset the selected rows of the specified {@link RichTable} component.
   **
   ** @param  component          the {@link RichTable} component to reset the
   **                            selection.
   */
  protected void resetSelectedRowKey(final RichTable component) {
    if (component != null) {
      final RowKeySet rowSet = component.getSelectedRowKeys();
      if (rowSet != null) {
        rowSet.removeAll();
        ADF.partialRender(component);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRow
  /**
   ** Returns the {@link List} of selected rows in the specified component.
   **
   ** @param  component          the {@link RichTable} component to return the
   **                            {@link List} of selected rows for.
   **
   ** @return                    the {@link List} of selected rows in the
   **                            specified {@link RichTable}
   **                            <code>component</code>.
   */
  protected List<Row> selectedRow(final RichTable component) {
    final String method = "selectedRow";
    entering(method);

    final List<Row> result = new ArrayList<Row>();
    if (component == null) {
      exiting(method);
      return result;
    }

    final JUCtrlHierNodeBinding rowNode = (JUCtrlHierNodeBinding)component.getRowData();
    if (rowNode != null) {
      // susujosh for commandlink click return the data from rowData.
      // this way we can handle generically command link returning data from
      // rowData rather than from selection and find by key
      result.add(rowNode.getRow());
      exiting(method);
      return result;
    }

    final RowKeySet rks = component.getSelectedRowKeys();
    if (rks == null || rks.getSize() <= 0) {
      exiting(method);
      return result;
    }

    RowSetIterator rsi = null;
    if (component.getValue() != null && component.getValue() instanceof FacesCtrlHierBinding.FacesModel) {
      final FacesCtrlHierBinding.FacesModel tree = (FacesCtrlHierBinding.FacesModel)component.getValue();
      if (tree.getBinding() != null && tree.getBinding().getIteratorBinding() != null)
        rsi = tree.getBinding().getIteratorBinding().getRowSetIterator();
    }
    if (rsi == null) {
      exiting(method);
      return result;
    }

    final Iterator<Object> rowCursor = rks.iterator();
    while (rowCursor.hasNext()) {
      final Iterator<Key> keyCursor = ((Collection<Key>)rowCursor.next()).iterator();
      while (keyCursor.hasNext()) {
        final Key key   = keyCursor.next();
        final Row row[] = rsi.findByKey(key, 1);
        if (row.length == 0)
          continue;

        result.add(row[0]);
        // forcebily breaking, since we don't support multiple keys
        break;
      }
    } // endof while loop
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedTreeTableRow
  /**
   ** Returns the {@link List} of selected rows in the specified component.
   **
   ** @param  component          the {@link RichTreeTable} component to return
   **                            the {@link List} of selected rows for.
   **
   ** @return                    the {@link List} of selected rows in the
   **                            specified {@link RichTreeTable}
   **                            <code>component</code>.
   */
  protected List<Row> selectedTreeTableRow(final RichTreeTable component) {
    final List<Row> nodes = new ArrayList<Row>();

    if (component != null) {
      final Object old = component.getRowKey();
      try {
        final RowKeySet rks = component.getSelectedRowKeys();
        if (rks != null) {
          for (Object cursor : rks) {
            // position the
            component.setRowKey(cursor);
            final JUCtrlHierNodeBinding node = (JUCtrlHierNodeBinding)component.getRowData();
            if (node != null)
              nodes.add(node.getRow());
          }
        }
      }
      finally {
        // reset the selection to the entry state
        component.setRowKey(old);
      }
    }
    return nodes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetTreeTableSelectedRowKey
  /**
   ** Removing each selected row from the specified component.
   **
   ** @param  component          the {@link RichTreeTable} component remove any
   **                            selection from.
   **
   */
  protected void resetTreeTableSelectedRowKey(final RichTreeTable component) {
    if (component != null) {
      final RowKeySet rowSet = component.getSelectedRowKeys();

      if (rowSet != null)
        rowSet.clear();

      ADF.partialRender(component);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowSetIterator
  /**
   ** Returns the {@link RowSetIterator} from the specified binding iterator.
   **
   ** @param  binding            the fullqualified name of the iterator binding
   **                            to return the {@link RowSetIterator} for.
   **
   ** @return                    the {@link RowSetIterator} of all rows from the
   **                            specified binding iterator.
   */
  protected RowSetIterator rowSetIterator(final String binding) {
    final DCIteratorBinding iterator = ADF.iteratorBinding(binding);
    return iterator.getRowSetIterator();
  }
}