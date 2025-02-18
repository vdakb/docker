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

    Copyright © 2024 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   OAuth Registration

    File        :   ScopePickerManagedBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.bean;

import bka.iam.identity.ui.oauth.model.Scope;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.iam.ui.platform.utils.FacesUtils;
import oracle.iam.ui.platform.utils.TaskFlowUtils;
import oracle.iam.ui.platform.view.event.TargetSelectionEvent;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;

////////////////////////////////////////////////////////////////////////////////
// class ScopePickerManagedBean
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attribute
 ** values of <code>Scope Picker</code>.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ScopePickerManagedBean extends AbstractMB {

  // Logger related static attributes
  private static final String className = ScopePickerManagedBean.class.getName();
  private static Logger       logger = Logger.getLogger(className);

  // ADF Components
  private transient RichCommandToolbarButton addSelectedButton;
  private transient RichCommandToolbarButton addAllButton;
  private transient RichCommandToolbarButton removeSelectedButton;
  private transient RichCommandToolbarButton removeAllButton;
  private transient RichTable searchTable;
  private transient RichTable selectedRowsTable;
  private transient RichCommandButton addButton;
  private transient List<Scope> selectedTargets;
  private transient List<Row> rows;


  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ScopePickerManagedBean</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ScopePickerManagedBean() {
    this.selectedTargets = new ArrayList<>();
  }

  ///////////////////////////////////////////////////////////////////
  // ADF Component bindings (Setter and getter methods)
  ///////////////////////////////////////////////////////////////////

  public void setAddSelectedButton(RichCommandToolbarButton addSelectedButton) {
    this.addSelectedButton = addSelectedButton;
  }

  public RichCommandToolbarButton getAddSelectedButton() {
    return addSelectedButton;
  }

  public void setAddAllButton(RichCommandToolbarButton addAllButton) {
    this.addAllButton = addAllButton;
  }

  public RichCommandToolbarButton getAddAllButton() {
    return addAllButton;
  }

  public void setRemoveSelectedButton(RichCommandToolbarButton removeSelectedButton) {
    this.removeSelectedButton = removeSelectedButton;
  }

  public RichCommandToolbarButton getRemoveSelectedButton() {
    return removeSelectedButton;
  }

  public void setRemoveAllButton(RichCommandToolbarButton removeAllButton) {
    this.removeAllButton = removeAllButton;
  }

  public RichCommandToolbarButton getRemoveAllButton() {
    return removeAllButton;
  }

  public void setSearchTable(RichTable searchTable) {
    this.searchTable = searchTable;
  }

  public RichTable getSearchTable() {
    return searchTable;
  }

  public void setSelectedRowsTable(RichTable selectedRowsTable) {
    this.selectedRowsTable = selectedRowsTable;
  }

  public RichTable getSelectedRowsTable() {
    return selectedRowsTable;
  }

  public void setAddButton(RichCommandButton addButton) {
    this.addButton = addButton;
  }

  public RichCommandButton getAddButton() {
    return addButton;
  }

  public void setSelectedTargets(List<Scope> selectedTargets) {
    this.selectedTargets = selectedTargets;
  }

  public List<Scope> getSelectedTargets() {
    return selectedTargets;
  }

  public void setRows(List<Row> rows) {
    this.rows = rows;
  }

  public List<Row> getRows() {
    String methodName = "getRows";
    logger.entering(className, methodName);
    
    if (this.rows == null)
      this.rows = getAllRows("allScopesIterator");
    
    logger.exiting(className, methodName);
    return this.rows;
  }

  ///////////////////////////////////////////////////////////////////
  // ADF button listeners
  ///////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectedActionListener 
  /**
   ** Add selected source lines to selected target list
   ** @param actionEvent
   */
  public void addSelectedActionListener(ActionEvent actionEvent) {
    String methodName = "addSelectedActionListener";
    logger.entering(className, methodName);
    
    List<Row> selRows = getSelectedTableRow(getSearchTable());
    for (Row row : selRows) {
      Scope scope = new Scope((String) row.getAttribute("scopeName"), (String) row.getAttribute("description"));
      if (getSelectedTargets() == null)
        setSelectedTargets(new ArrayList<Scope>());
      if (!getSelectedTargets().contains(scope))
        getSelectedTargets().add(scope);
    }
    refreshSelectedTargets();
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAllActionListener 
  /**
   ** Add all selected source lines to selected target list
   ** @param actionEvent
   */
  public void addAllActionListener(ActionEvent actionEvent) {
    String methodName = "getRows";
    logger.entering(className, methodName);
    
    List<Row> selRows = getRows();
    for (Row row : selRows) {
      if (row == null)
        continue;
      Scope scope = new Scope((String) row.getAttribute("scopeName"), (String) row.getAttribute("description"));
      if (getSelectedTargets() == null)
        setSelectedTargets(new ArrayList<Scope>());
      if (!getSelectedTargets().contains(scope))
        getSelectedTargets().add(scope);
    }
    refreshSelectedTargets();
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeSelectedActionListener 
  /**
   ** Remove selected target lines from selected target list
   ** @param actionEvent
   */
  public void removeSelectedActionListener(ActionEvent actionEvent) {
    String methodName = "removeSelectedActionListener";
    logger.entering(className, methodName);
    
    List<Scope> removeRows = new ArrayList<Scope>();
    RichTable rtt = getSelectedRowsTable();
    RowKeySet keys = rtt.getSelectedRowKeys();
    RowKeySet oldKeys = keys.clone();
    Object oldRowKey = rtt.getRowKey();
    for (Iterator i = keys.iterator(); i.hasNext();) {
      Object key = i.next();
      rtt.setRowKey(key);
      Scope rowData = (Scope) rtt.getRowData();
      removeRows.add(rowData);
    }
    this.selectedTargets.removeAll(removeRows);
    rtt.setSelectedRowKeys(oldKeys);
    rtt.setRowKey(oldRowKey);
    resetSelectedRowKey(rtt);
    refreshSelectedTargets();
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAllActionListener 
  /**
   ** Remove all selected target lines
   ** @param actionEvent
   */
  public void removeAllActionListener(ActionEvent actionEvent) {
    String methodName = "removeAllActionListener";
    logger.entering(className, methodName);
    
    this.selectedTargets.clear();
    resetSelectedRowKey(getSelectedRowsTable());
    refreshSelectedTargets();
    
    logger.exiting(className, methodName);
  }

  ///////////////////////////////////////////////////////////////////
  // ADF selection listeners
  ///////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectionListener 
  /**
   ** Refresh data in selected source list
   ** @param selectionEvent
   */
  public void selectionListener(SelectionEvent selectionEvent) {
    String methodName = "selectionListener";
    logger.entering(className, methodName);
    
    if (getSelectionType().equalsIgnoreCase("single")) {
      AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getAddButton());
    } else {
      refreshSourceTargets();
    }
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedRowsSelectListener 
  /**
   ** Refresh data in selected target list
   ** @param selectionEvent
   */
  public void selectedRowsSelectListener(SelectionEvent selectionEvent) {
    String methodName = "selectedRowsSelectListener";
    logger.entering(className, methodName);
    
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getRemoveAllButton());
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getRemoveSelectedButton());
    
    logger.exiting(className, methodName);
  }

  //////////////////////////////////////////////////////////////////
  // Action handlers
  //////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectedRows 
  /**
   ** Trigger ADF Event with list of selected rows.
   ** @return Return succes in case the rows were added otherwise return error
   */
  public String addSelectedRows() {
    String methodName = "addSelectedRows";
    logger.entering(className, methodName);
    
    String distinguisher = (String) FacesUtils.getValueFromELExpression("#{pageFlowScope.event_distinguisher}");
    if (getSelectionType().equalsIgnoreCase("single")) {
      List<Row> selectedRows = getSelectedTableRow(getSearchTable());
      Scope scope =
        new Scope((String) selectedRows.get(0).getAttribute("scopeName"),
                  (String) selectedRows.get(0).getAttribute("description"));
      List<Serializable> returnList = new ArrayList<>();
      if ("pick_scope".equalsIgnoreCase(distinguisher)) {
        returnList.add(scope);
      }
      TaskFlowUtils.raiseEvent("return", new TargetSelectionEvent(distinguisher, returnList));
      if (getSearchTable() != null)
        getSearchTable().getSelectedRowKeys().clear();
    } else {
      List<Scope> selectedRowData = getSelectedTargets();
      List<Serializable> returnList = new ArrayList<>();
      for (Scope s : selectedRowData) {
        returnList.add(s);
      }
      if (selectedRowData == null || selectedRowData.size() == 0)
        return "error";
      TaskFlowUtils.raiseEvent("return", new TargetSelectionEvent(distinguisher, returnList));
    }
    
    logger.exiting(className, methodName);
    return "success";
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancelAction 
  /**
   ** Cancel action string
   ** @return String cancel
   */
  public String cancelAction() {
    String methodName = "cancelAction";
    logger.entering(className, methodName);
    
    if (getSearchTable() != null)
      getSearchTable().getSelectedRowKeys().clear();
    
    logger.exiting(className, methodName);
    return "cancel";
  }

  //////////////////////////////////////////////////////////////////
  // Check Button disable status
  //////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRemoveAllDisabled 
  /**
   ** Validate if the <code>Remove All</code> buttion is disabled
   ** @return Return true in case the buttion <code>Remove All</code> is disabled 
   */
  public boolean isRemoveAllDisabled() {
    String methodName = "isRemoveAllDisabled";
    logger.entering(className, methodName);
    
    boolean retFlag = true;
    if (getSelectedTargets() != null && getSelectedTargets().size() > 0)
      retFlag = false;
    
    logger.exiting(className, methodName);
    return retFlag;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRemoveSelectedDisabled 
  /**
   ** Validate if the <code>Remove Selected</code> buttion is disabled
   ** @return Return true in case the buttion <code>Remove Selected</code> is disabled 
   */
  public boolean isRemoveSelectedDisabled() {
    String methodName = "isRemoveSelectedDisabled";
    logger.entering(className, methodName);
    
    boolean retFlag = true;
    int count = getSelectedRowCount(getSelectedRowsTable());
    if (count > 0)
      retFlag = false;
    
    logger.exiting(className, methodName);
    return retFlag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAddSelectedDisabled 
  /**
   ** Validate if the <code>Add Selected</code> buttion is disabled
   ** @return Return true in case the buttion <code>Add Selected</code> is disabled 
   */
  public boolean isAddSelectedDisabled() {
    String methodName = "isAddSelectedDisabled";
    logger.entering(className, methodName);
    
    boolean retFlag = true;
    int count = getSelectedRowCount(getSearchTable());
    if (count > 0)
      retFlag = false;
    
    logger.exiting(className, methodName);
    return retFlag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAddAllDisabled 
  /**
   ** Validate if the <code>Add All</code> buttion is disabled
   ** @return Return true in case the buttion <code>Add All</code> is disabled 
   */
  public boolean isAddAllDisabled() {
    String methodName = "isAddAllDisabled";
    logger.entering(className, methodName);
    
    boolean disable = false;
    try {
      disable = !isIteratorHasRows("allScopesIterator");
    } catch (Exception e) {
      disable = false;
    }
    
    logger.exiting(className, methodName);
    return disable;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAddButtonDisabled 
  /**
   ** Validate if the <code>Add</code> buttion is disabled
   ** @return Return true in case the buttion <code>Add</code> is disabled
   */
  public boolean isAddButtonDisabled() {
    String methodName = "isAddButtonDisabled";
    logger.entering(className, methodName);
    
    boolean retFlag = true;
    if (getSelectionType().equalsIgnoreCase("single")) {
      int count = getSelectedRowCount(getSearchTable());
      if (count > 0)
        retFlag = false;
    } else if (getSelectedTargets() != null && getSelectedTargets().size() > 0) {
      retFlag = false;
    }logger.exiting(className, methodName);
    
    logger.exiting(className, methodName);
    return retFlag;
  }

  //////////////////////////////////////////////////////////////////
  // Private methods
  //////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSelectionType 
  /**
   ** Return seletiont type from task flow scope parameter selection_type
   ** @return Selection Type input parameter value
   */
  private String getSelectionType() {
    String selectionType = (String) FacesUtils.getValueFromELExpression("#{pageFlowScope.selection_type}");
    return selectionType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshSelectedTargets
  /**
   ** Parsial refresh of command buttons related to selected data
   */
  private void refreshSelectedTargets() {
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getSelectedRowsTable());
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getRemoveAllButton());
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getRemoveSelectedButton());
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getAddButton());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshSourceTargets
  /**
   ** Parsial refresh of command buttons related to selected data
   */
  private void refreshSourceTargets() {
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getAddAllButton());
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent) getAddSelectedButton());
  }
}
