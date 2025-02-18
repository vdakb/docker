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

    File        :   IdentityDomainManagedBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.bean;

import bka.iam.identity.ui.oauth.model.Attribute;
import bka.iam.identity.ui.oauth.model.IdentityDomain;
import bka.iam.identity.ui.oauth.model.TokenSetting;
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
// class IdentityDomainManagedBean
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attribute
 ** values of <code>OAuth Identity Domains</code>.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityDomainManagedBean extends AbstractMB{
  
  private static final String className = IdentityDomainManagedBean.class.getName();
  private static Logger       logger    = Logger.getLogger(className);  

  private final String TF_URL_IDENTITY_DOMAIN_DETAILS = "/WEB-INF/bka/iam/identity/ui/oauth/tfs/identity-domain-details-tf.xml#identity-domain-details-tf";
  
  /////////////////////////////////////////////////////
  // Action button/menu handlers
  /////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchCreateIdentityDomain 
  /**
   ** Launch Edit Identity Domain task flow
   ** @param actionEvent Action Event details
   */
  public void launchCreateIdentityDomain(ActionEvent actionEvent){
    String methodName = "launchCreateIdentityDomain";
    logger.entering(className, methodName);

    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    
    Map<String,Object> params = new HashMap<String,Object>();  
    params.put(TF_PARAM_OAM_CONNECTION_NAME, oam_connection_name);
    params.put(TF_PARAM_MODE               , TF_MODE_CREATE);
    
    String title = FacesUtils.getBundleValue(UI_BUNDLE, "identity-domain-details-new-identity-domain");
    
    ADFFacesUtils.launchTaskFlow(title,
                              TF_URL_IDENTITY_DOMAIN_DETAILS,
                              title, 
                              null, null, null,
                              false, 
                              params);
   
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchEditIdentityDomain 
  /**
   ** Launch Create Identity Domain task flow
   ** @param actionEvent Action Event details
   */
  public void launchEditIdentityDomain(ActionEvent actionEvent){
    String methodName = "launchEditIdentityDomain";
    logger.entering(className, methodName);
       
    String selectedIdentityDomainName = getValueFromSelectedRow("name");
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    
    Map<String,Object> params = new HashMap<String,Object>();  
    params.put(TF_PARAM_OAM_CONNECTION_NAME , oam_connection_name);
    params.put(TF_PARAM_IDENTITY_DOMAIN_NAME, selectedIdentityDomainName);
    params.put(TF_PARAM_MODE                , TF_MODE_EDIT);
    
    String title = selectedIdentityDomainName+":"+oam_connection_name;
    ADFFacesUtils.launchTaskFlow(title,
                              TF_URL_IDENTITY_DOMAIN_DETAILS,
                              title, 
                              null, null, null,
                              false, 
                              params);
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteIdentityDomain 
  /**
   ** Delete OAuth Identity Domain
   ** @param dialogEvent Dialog Event details
   */
  public void deleteIdentityDomain(DialogEvent dialogEvent){
    String methodName = "deleteIdentityDomain";
    logger.entering(className, methodName);
    
    if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
      String selectedIdentityDomainName = getValueFromSelectedRow("name");
    
      String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);      
      if(selectedIdentityDomainName != null){
        Map<String,String> params = new HashMap<String,String>();
        params.put("oamConnectionName",oam_connection_name);
        params.put("identityDomainName",selectedIdentityDomainName);        
        executeBindingMethod("deleteIdentityDomain",params);
      }
      
      //Refresh data in table
      DCIteratorBinding listIter = FacesUtils.findIterator("IdentityDomainSessionFacadeIterator");
      if(listIter != null){
        listIter.executeQuery();
      }
      AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getSearchTable()); 
    }
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshIdentityDomains 
  /**
   ** Reload OAuth Identity Domains from OAM
   ** @param actionEvent Action Event details
   */
  public void refreshIdentityDomains(ActionEvent actionEvent) {
    String methodName = "refreshIdentityDomains";
    logger.entering(className, methodName);
    
    //Reload data from OAM Server   
    executeBindingMethod("refreshIdentityDomains");
    
    //Refresh data in table
    DCIteratorBinding listIter = FacesUtils.findIterator("IdentityDomainSessionFacadeIterator");
    if(listIter != null){
      listIter.executeQuery();
    }
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getSearchTable()); 
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentityDomain 
  /**
   ** Create OAuth Identity Domains in OAM
   ** @return Error message in case OAuth Client can't be created
   */
  public String createIdentityDomain() {
    String methodName = "createIdentityDomain";
    logger.entering(className, methodName);
    
    String status = "";
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    
    IdentityDomain identityDomain = featchIdentityDomain();
    
    // Update client in OAM
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("oamConnectionName",oam_connection_name);
    params.put("identityDomain",identityDomain);        
    String errorMessage = executeBindingMethod("createIdentityDomain",params);
    
    if(errorMessage == null){
      showInfo("IdentityDomain is succesfully created");
      showInfo(FacesUtils.getBundleValue(UI_BUNDLE, "identity-domain-details-info-domain-created"));
      status = "success";
    }
    
    logger.exiting(className, methodName);
    return status;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   saveIdentityDomain 
  /**
   ** Update OAuth Identity Domains in OAM
   ** @return Error message in case OAuth Client can't be updated
   */
  public String saveIdentityDomain() {
    String methodName = "saveIdentityDomain";
    logger.entering(className, methodName);
    
    String status = "";
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    IdentityDomain identityDomain = featchIdentityDomain();
    
    // Update client in OAM
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("oamConnectionName",oam_connection_name);
    params.put("identityDomain",identityDomain);        
    String errorMessage = executeBindingMethod("updateIdentityDomain",params);
  
    if(errorMessage == null){
      showInfo(FacesUtils.getBundleValue(UI_BUNDLE, "identity-domain-details-info-domain-updated"));
      status = "success";
    }
    
    logger.exiting(className, methodName);
    return status;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActionWarningMessageDelete 
  /**
   ** Get customized warning message for selected OAuth client
   ** @return Customized warning message for selected OAuth client
   */
  public String getActionWarningMessageDelete(){
    String methodName = "getActionWarningMessageDelete";
    logger.entering(className, methodName);
    
    String selectedIdentityDomainName = getValueFromSelectedRow("name");
    String message = FacesUtils.getBundleValue(UI_BUNDLE, "identity-domain-warning-message-for-delete");
    
    logger.exiting(className, methodName);
    return  message + selectedIdentityDomainName + " ?" ;
  }
  
  /////////////////////////////////////////////////////
  // Private Methods
  /////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   featchIdentityDomain 
  /**
   ** Read all OAutch Client data provided on ADF page
   ** @return OAuth Client model object
   */
  private IdentityDomain featchIdentityDomain(){
    String methodName = "featchIdentityDomain";
    logger.entering(className, methodName);
    
    // Fetch client from ADF bindings
    IdentityDomain identityDomain = new IdentityDomain();
    
    identityDomain.setName             (ADFFacesUtils.getAttributeBindingValue("name",String.class));
    identityDomain.setDescription      (ADFFacesUtils.getAttributeBindingValue("description",String.class));
    identityDomain.setConsentPageURL   (ADFFacesUtils.getAttributeBindingValue("consentPageURL",String.class));
    identityDomain.setErrorPageURL     (ADFFacesUtils.getAttributeBindingValue("errorPageURL",String.class));
    identityDomain.setIdentityProvider (ADFFacesUtils.getAttributeBindingValue("identityProvider",String.class));
    
    // customAttrs
    DCIteratorBinding customAttrsIterator = FacesUtils.findIterator("customAttrsIterator");
    RowSetIterator rowCustomAttrsIterator = customAttrsIterator.getViewObject().createRowSetIterator(null);
    while(rowCustomAttrsIterator.hasNext()){
      Row row = rowCustomAttrsIterator.next();
      identityDomain.getCustomAttrs().add(new Attribute((String)row.getAttribute("key"),(String)row.getAttribute("value")));
    }
    
    //tokenSettings
    DCIteratorBinding tokenSettingsIterator = FacesUtils.findIterator("tokenSettingsIterator");
    RowSetIterator rowTokenSettingsIterator = tokenSettingsIterator.getViewObject().createRowSetIterator(null);
    while(rowTokenSettingsIterator.hasNext()){
      Row row = rowTokenSettingsIterator.next();
      identityDomain.getTokenSettings().add(new TokenSetting((String)row.getAttribute("tokenType"),
                                                             (Integer)row.getAttribute("tokenExpiry"),
                                                             (Boolean)row.getAttribute("lifeCycleEnabled"),
                                                             (Boolean)row.getAttribute("refreshTokenEnabled"),
                                                             (Integer)row.getAttribute("refreshTokenExpiry"),
                                                             (Boolean)row.getAttribute("refreshTokenLifeCycleEnabled")
                                                             ));
    }
    
    logger.exiting(className, methodName);
    return identityDomain;
  } 
}