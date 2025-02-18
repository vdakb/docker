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

    File        :   AbstractMB.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.bean;

import bka.iam.identity.ui.oauth.utils.ADFFacesUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.RichMenu;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.layout.RichToolbar;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierNodeBinding;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.iam.ui.platform.view.backing.BaseStateMB;

import oracle.jbo.Row;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationManagedBean
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attribute
 ** values of all OAuth managed beans.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractMB extends BaseStateMB {
  
  private static final long serialVersionUID = 1L;
  
  private static final String className = AbstractMB.class.getName();
  private static Logger       logger = Logger.getLogger(className);  
  
  public static String UI_BUNDLE = "bka.iam.identity.ui.oauth.OAuthBundle";
  
  protected static final String TF_PARAM_OAM_CONNECTION_NAME  = "oam_connection_name";
  protected static final String TF_PARAM_IDENTITY_DOMAIN_NAME = "identity_domain_name";
  protected static final String TF_PARAM_MODE                 = "mode";
  
  protected static final String TF_MODE_CREATE = "CREATE";
  protected static final String TF_MODE_EDIT   = "EDIT";
  
  // Key fiels in th table
  private transient List<Row> rowData;
  protected transient Object[]  selectedData;
  
  // ADF GUI Components
  private transient RichTable searchTable;
  private transient RichToolbar toolbar;
  private transient RichMenu actionMenu;
  
  ///////////////////////////////////////////////////////////////////
  // ADF Component bindings (Setter and getter methods)
  ///////////////////////////////////////////////////////////////////

  public void setSearchTable(RichTable searchTable) {
    this.searchTable = searchTable;
  }

  public RichTable getSearchTable() {
    return searchTable;
  }

  public void setToolbar(RichToolbar toolbar) {
    this.toolbar = toolbar;
  }

  public RichToolbar getToolbar() {
    return toolbar;
  }

  public void setActionMenu(RichMenu actionMenu) {
    this.actionMenu = actionMenu;
  }

  public RichMenu getActionMenu() {
    return actionMenu;
  }
  
  ///////////////////////////////////////////////////////////////////
  // Operations on ADF components
  ///////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleCheckPartialTargets 
  /**
   ** Refresh Toolbar and Action Menu 
   */
  public void handleCheckPartialTargets() {
    if (getToolbar() != null)
      AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getToolbar()); 
    if (getActionMenu() != null)
      AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getActionMenu()); 
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSelectedRowCount 
  /**
   ** Get number of selected rows on mulit select table
   * @return number of selected rows
   */
  public int getSelectedRowCount() {
      return getSelectedRowCount(getSearchTable());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEditButtonDisabled 
  /**
   * Check if the Edit button is disabled
   * @return Return true in case the Edit button is disabled
   */
  public boolean isEditButtonDisabled() {
    boolean flag = false;
    if (getSelectedRowCount(getSearchTable()) <= 0)
      flag = true; 
    return flag;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDeleteButtonDisabled 
  /**
   * Check if the Delete button is disabled
   * @return Return true in case the Delete button is disabled
   */
  public boolean isDeleteButtonDisabled() {
      boolean flag = false;
      if (getSelectedRowCount(getSearchTable()) <= 0) {
        flag = true;
      } 
      return flag;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   showInfo 
  /**
   * Show ADF info message
   * @param message Message text
   */
  protected void showInfo(String message){
    FacesMessage facesMessage = new FacesMessage();

    facesMessage.setDetail(message);
    facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);     
    ADFFacesUtils.showFacesMessage(facesMessage);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   showErrors 
  /**
   * Show ADF error message
   * @param exeptions Error messages as list of execptions
   */
  protected void showErrors(List<Throwable> exeptions){
    FacesMessage facesMessage = new FacesMessage();
    String errorMessage = "";
    
    if((exeptions.get(0)).getCause() != null){
      errorMessage = (exeptions.get(0)).getCause().getMessage();
    }
    else{
      errorMessage = (exeptions.get(0)).getMessage();
    }
      
    facesMessage.setDetail(errorMessage);
    facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);     
    ADFFacesUtils.showFacesMessage(facesMessage);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   showErrors 
  /**
   * Show ADF error message
   * @param errorMessage Error message text
   */
  protected void showErrors(String errorMessage){
    FacesMessage facesMessage = new FacesMessage(errorMessage);  
    facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);     
    ADFFacesUtils.showFacesMessage(facesMessage);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValueFromSelectedRow 
  /**
   * Get value from the selected value
   * @param columnName Column name from which selected value is returned
   * @return Selected column value
   */
  protected String getValueFromSelectedRow(String columnName){
    String methodName = "getValueFromSelectedRow";
    logger.entering(className, methodName);
    
    String value = null;
    // Get the instance for table component in backing bean
    RichTable table = getSearchTable();
    // Get the Selected Row key set iterator
    Iterator selectionIt = table.getSelectedRowKeys().iterator();
    while(selectionIt.hasNext()){
      Object  rowKey = selectionIt.next();
      table.setRowKey(rowKey); 
      int index = table.getRowIndex();
      FacesCtrlHierNodeBinding row = (FacesCtrlHierNodeBinding) table.getRowData(index);
      Row selectedRow = row.getRow();
      if (selectedRow != null){
        value = (String)selectedRow.getAttribute(columnName);
      }
    }
    
    logger.exiting(className, methodName);
    return value;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValueFromSelectedRows 
  /**
   * Get values from selected lines
   * @param columnName Name of the column from which selected value is returned
   * @return List of selected values from specified column
   */
  protected List<String> getValueFromSelectedRows(String columnName){
    String methodName = "getValueFromSelectedRows";
    logger.entering(className, methodName);
    
    List<String> values = new ArrayList<String>();
    // Get the instance for table component in backing bean
    RichTable table = getSearchTable();
    // Get the Selected Row key set iterator
    Iterator selectionIt = table.getSelectedRowKeys().iterator();
    while(selectionIt.hasNext()){
      Object  rowKey = selectionIt.next();
      table.setRowKey(rowKey); 
      int index = table.getRowIndex();
      FacesCtrlHierNodeBinding row = (FacesCtrlHierNodeBinding) table.getRowData(index);
      Row selectedRow = row.getRow();
      if (selectedRow != null){
        values.add((String)selectedRow.getAttribute(columnName));
      }
    }
    
    logger.exiting(className, methodName);
    return values;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeBindingMethod 
  /**
   * Execute binding method
   * @param method Binding method name
   * @param params Method input parameters
   * @return
   */
  protected String executeBindingMethod(String method, Map params){
    String methodName = "executeBindingMethod";
    logger.entering(className, methodName);
    
    BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
    OperationBinding binding = bindings.getOperationBinding(method);
    if(params != null){
      binding.getParamsMap().putAll(params);
    }
    String errorMessage = (String)binding.execute();
    if(errorMessage != null){
      this.showErrors(errorMessage);
    }
    
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeBindingMethod 
  /**
   * Execute binding method without parameters
   * @param method Binding method name
   * @return
   */
  protected String executeBindingMethod(String method){
    return executeBindingMethod(method, null);
  }
}