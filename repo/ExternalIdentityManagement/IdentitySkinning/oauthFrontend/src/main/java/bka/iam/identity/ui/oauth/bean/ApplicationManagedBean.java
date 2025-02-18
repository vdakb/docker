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

    File        :   ApplicationManagedBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.bean;

import bka.iam.identity.ui.oauth.model.Application;
import bka.iam.identity.ui.oauth.model.Scope;
import bka.iam.identity.ui.oauth.model.TokenAttribute;
import bka.iam.identity.ui.oauth.utils.ADFFacesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.iam.ui.platform.utils.FacesUtils;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationManagedBean
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attribute
 ** values of <code>OAuth Applications</code>.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ApplicationManagedBean extends AbstractMB {
  
  private static final String className = ApplicationManagedBean.class.getName();
  private static Logger       logger = Logger.getLogger(className);  
  
  public static final String OAM_CONNECTION_NAME_TF_PARAM_NAME   = "oam_connection_name";
  public static final String IDENTITY_DOMAIN_NAME_TF_PARAM_NAME_ = "identity_domain_name";
  
  public static final String APPLICATION_DETAIL_TASKFLOW_URL = "/WEB-INF/bka/iam/identity/ui/oauth/tfs/application-details-tf.xml#application-details-tf";
  public static final String TF_PARAM_NAME_APPLICATION_NAME  = "application_name";
  
  /////////////////////////////////////////////////////
  // Action button/menu handlers
  /////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchCreateApplication 
  /**
   * Launch Create Application task flow
   * @param actionEvent Action event data
   */
  public void launchCreateApplication(ActionEvent actionEvent){
    String methodName = "launchCreateApplication";
    logger.entering(className, methodName);
    
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    
    Map<String,Object> params = new HashMap<String,Object>();  
    params.put(TF_PARAM_OAM_CONNECTION_NAME , oam_connection_name);
    params.put(TF_PARAM_IDENTITY_DOMAIN_NAME, identity_domain_name);
    params.put(TF_PARAM_MODE                , TF_MODE_CREATE);
    
    String title = FacesUtils.getBundleValue(UI_BUNDLE, "application-details-new-application");
    ADFFacesUtils.launchTaskFlow(title,
                              APPLICATION_DETAIL_TASKFLOW_URL,
                              title, 
                              null,
                              null,
                              null,
                              false, 
                              params);
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchEditApplication 
  /**
   * Launch Edit Application task flow
   * @param actionEvent
   */
  public void launchEditApplication(ActionEvent actionEvent){
    String methodName = "launchEditApplication";
    logger.entering(className, methodName);
    
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    String selectedApplicationName = getValueFromSelectedRow("name");
    
    Map<String,Object> params = new HashMap<String,Object>();  
    params.put(TF_PARAM_OAM_CONNECTION_NAME , oam_connection_name);
    params.put(TF_PARAM_IDENTITY_DOMAIN_NAME, identity_domain_name);
    params.put(TF_PARAM_MODE                , TF_MODE_EDIT);
    params.put(TF_PARAM_NAME_APPLICATION_NAME    , selectedApplicationName);
    
    String title = FacesUtils.getBundleValue(UI_BUNDLE, "application-details-header")+ " "+selectedApplicationName;
    
    ADFFacesUtils.launchTaskFlow(title,
                              APPLICATION_DETAIL_TASKFLOW_URL,
                              title, 
                              null,
                              null,
                              null,
                              false, 
                              params);
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createApplication 
  /**
   * Read Application details and save it
   * @return Error message in case Appliaction can't be created
   */
  public String createApplication(){
    String methodName = "createApplication";
    logger.entering(className, methodName);
    
    String status = "";
    
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    
    Application application = featchApplicationData();
    application.setIdDomain(identity_domain_name);
    
    // Create OAuth Application in OAM
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("identityDomainName",identity_domain_name);
    params.put("oamConnectionName",oam_connection_name);
    params.put("application",application);
    String errorMessage = executeBindingMethod("createApplication",params);
    
    if(errorMessage == null){
      showInfo(FacesUtils.getBundleValue(UI_BUNDLE, "application-details-info-application-created"));
      status = "success";
    }
    
    logger.exiting(className, methodName);
    return status;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   saveApplication 
  /**
   * Read Application details and update it
   * @return Error message in case Appliaction can't be updated
   */
  public String saveApplication(){
    String methodName = "saveApplication";
    logger.entering(className, methodName);
    
    String status = "";
    
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    
    Application application = featchApplicationData();
    application.setIdDomain(identity_domain_name);
    
    // Update client in OAM
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("identityDomainName",identity_domain_name);
    params.put("oamConnectionName",oam_connection_name);
    params.put("application",application);
    String errorMessage = executeBindingMethod("updateApplication",params);
    
    if(errorMessage == null){
      showInfo(FacesUtils.getBundleValue(UI_BUNDLE, "application-details-info-application-updated"));
      status = "success";
    }
    
    logger.exiting(className, methodName);
    return status;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteApplication 
  /**
   * Delete OAuth Application
   * @param dialogEvent Dialog Event data
   */
  public void deleteApplication(DialogEvent dialogEvent){
    String methodName = "deleteApplication";
    logger.entering(className, methodName);
    
    if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
      String  selectedApplicationId = getValueFromSelectedRow("resourceServerId");    
      logger.finer("Selected applicationId: "+selectedApplicationId);
      
      String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
      String oam_connection_name  = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
      if(selectedApplicationId != null){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("oamConnectionName",oam_connection_name);
        params.put("identityDomainName",identity_domain_name);
        params.put("applicationId",selectedApplicationId);
        executeBindingMethod("deleteApplication",params);
      }
      
      //Refresh data in table
      DCIteratorBinding applicationsIterator = FacesUtils.findIterator("applicationsIterator");
      if(applicationsIterator != null){
        applicationsIterator.executeQuery();
      }
      AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getSearchTable()); 
    }
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshApplications 
  /**
   * Refresh list of the OAuth Applications
   * @param actionEvent
   */
  public void refreshApplications(ActionEvent actionEvent){
    String methodName = "refreshApplications";
    logger.entering(className, methodName);
    
    //Reload data from OAM Server
    executeBindingMethod("refreshApplications");
    
    //Refresh data in table
    DCIteratorBinding appItIter = FacesUtils.findIterator("applicationsIterator");
    if(appItIter != null){
       appItIter.executeQuery();
    }
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getSearchTable()); 
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActionWarningMessageDelete 
  /**
   * Get customized warning message for selected Application
   * @return
   */
  public String getActionWarningMessageDelete(){
    String methodName = "getActionWarningMessageDelete";
    logger.entering(className, methodName);
    
    String selectedApplicationName = getValueFromSelectedRow("name");
    String message = FacesUtils.getBundleValue(UI_BUNDLE, "application-action-warning-message-for-delete");
    
    logger.exiting(className, methodName);
    return  message + selectedApplicationName + " ?" ;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   featchApplicationData 
  /**
   * Read all data provided in ADF page and construct OAuth Application object
   * @return OAuth Application object with user provided data
   */
  private Application featchApplicationData(){
    String methodName = "featchApplicationData";
    logger.entering(className, methodName);
    
    // Fetch client from ADF bindings
    Application application = new Application();
    
    application.setResourceServerId(ADFFacesUtils.getAttributeBindingValue("resourceServerId",String.class));
    application.setName            (ADFFacesUtils.getAttributeBindingValue("name",String.class));
    application.setDescription     (ADFFacesUtils.getAttributeBindingValue("description",String.class));
    application.setResServerType   (ADFFacesUtils.getAttributeBindingValue("resServerType",String.class));
    application.setResourceServerNameSpacePrefix(ADFFacesUtils.getAttributeBindingValue("resourceServerNameSpacePrefix",String.class));
    
    //attributesIterator
    DCIteratorBinding tokenAttributesIterator = FacesUtils.findIterator("tokenAttributesIterator");
    RowSetIterator rowTokenAttributesIterator = tokenAttributesIterator.getViewObject().createRowSetIterator(null);
    while(rowTokenAttributesIterator.hasNext()){
      Row row = rowTokenAttributesIterator.next();
      String attrName  = (String)row.getAttribute("attrName");
      String attrValue = (String)row.getAttribute("attrValue");
      String attrType  = (String)row.getAttribute("attrType");
      application.getTokenAttributes().add(new TokenAttribute(attrName,attrValue,attrType ));
    }
    
    //redirectURIsIterator
    DCIteratorBinding scopesIterator = FacesUtils.findIterator("scopesIterator");
    RowSetIterator rowScopesIterator = scopesIterator.getViewObject().createRowSetIterator(null);
    while(rowScopesIterator.hasNext()){
      Row row = rowScopesIterator.next();
      String scopeName  = (String)row.getAttribute("scopeName");
      String description = (String)row.getAttribute("description");
      application.getScopes().add(new Scope(scopeName,description ));
    }
      
    logger.finest("Application data:"+application);
    
    logger.exiting(className, methodName);
    return application;
  } 
}