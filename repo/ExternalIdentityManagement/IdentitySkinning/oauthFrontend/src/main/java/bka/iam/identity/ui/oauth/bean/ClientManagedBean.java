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

    File        :   ClientManagedBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.bean;

import bka.iam.identity.ui.oauth.model.Client;
import bka.iam.identity.ui.oauth.model.RedirectURI;
import bka.iam.identity.ui.oauth.model.Scope;
import bka.iam.identity.ui.oauth.model.SingleAttribute;
import bka.iam.identity.ui.oauth.model.TokenAttribute;
import bka.iam.identity.ui.oauth.utils.ADFFacesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.iam.ui.platform.utils.FacesUtils;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

////////////////////////////////////////////////////////////////////////////////
// class ClientManagedBean
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attribute
 ** values of <code>OAuth Clients</code>.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ClientManagedBean extends AbstractMB {
  
  private static final String className = ClientManagedBean.class.getName();
  private static Logger       logger = Logger.getLogger(className);  
 
  private final String TF_URL_CLIENT_DETAIL         = "/WEB-INF/bka/iam/identity/ui/oauth/tfs/client-details-tf.xml#client-details-tf";
  private final String TF_URL_SCOPE_PICKER          = "/WEB-INF/bka/iam/identity/ui/oauth/tfs/scopes-picker-tf.xml#scopes-picker-tf";
  
  private final String TF_PARAM_CLIENT_ID           = "client_id";
  private final String TF_PARAM_SELECTION_TYPE      = "selection_type";
  private final String TF_PARAM_EVENT_DISTINGUISHER = "event_distinguisher";
  
  private transient RichInputText secretConfirmation;
  
  private transient RichTable scopesTable;

  private transient RichInputText defaultScopeInputText;
  
  private transient RichInputText newPassword;
  private transient RichInputText confirmNewPassword;

  
  ///////////////////////////////////////////////////////////////////
  // ADF Component bindings (Setter and getter methods)
  ///////////////////////////////////////////////////////////////////
  
  public void setSecretConfirmation(RichInputText secretConfirmation) {
    this.secretConfirmation = secretConfirmation;
  }

  public RichInputText getSecretConfirmation() {
    return secretConfirmation;
  }

  public void setScopesTable(RichTable scopesTable) {
    this.scopesTable = scopesTable;
  }

  public RichTable getScopesTable() {
    return scopesTable;
  }

  public void setDefaultScopeInputText(RichInputText defaultScopeInputText) {
    this.defaultScopeInputText = defaultScopeInputText;
  }

  public RichInputText getDefaultScopeInputText() {
    return defaultScopeInputText;
  }

  public void setNewPassword(RichInputText newPassword) {
    this.newPassword = newPassword;
  }

  public RichInputText getNewPassword() {
    return newPassword;
  }

  public void setConfirmNewPassword(RichInputText confirmNewPassword) {
    this.confirmNewPassword = confirmNewPassword;
  }

  public RichInputText getConfirmNewPassword() {
    return confirmNewPassword;
  }
  
  /////////////////////////////////////////////////////
  // Action button/menu handlers
  /////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchCreateClient 
  /**
   ** Launch Create Client task flow
   ** @param actionEvent Action event data
   */
  public void launchCreateClient(ActionEvent actionEvent){
    String methodName = "launchCreateClient";
    logger.entering(className, methodName);

    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    
    Map<String,Object> params = new HashMap<String,Object>();  
    params.put(TF_PARAM_OAM_CONNECTION_NAME , oam_connection_name);
    params.put(TF_PARAM_IDENTITY_DOMAIN_NAME, identity_domain_name);
    params.put(TF_PARAM_MODE                , "CREATE");
    
    String title = FacesUtils.getBundleValue(UI_BUNDLE, "client-details-new-client");
    
    ADFFacesUtils.launchTaskFlow(title,
                              TF_URL_CLIENT_DETAIL,
                              title, 
                              null, null, null,
                              false, 
                              params);
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchEditClient 
  /**
   ** Launch Edit Client task flow
   ** @param actionEvent Action event data
   */
  public void launchEditClient(ActionEvent actionEvent){
    String methodName = "launchEditClient";
    logger.entering(className, methodName);
    
    String selectedClientId = getValueFromSelectedRow("id");
    logger.finer("launchEditClient for client"+selectedClientId);
    
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    
    Map<String,Object> params = new HashMap<String,Object>();  
    params.put(TF_PARAM_OAM_CONNECTION_NAME , oam_connection_name);
    params.put(TF_PARAM_IDENTITY_DOMAIN_NAME, identity_domain_name);
    params.put(TF_PARAM_MODE                , "EDIT");
    params.put(TF_PARAM_CLIENT_ID           , selectedClientId);
    
    String title = selectedClientId;
    ADFFacesUtils.launchTaskFlow(title,
                              TF_URL_CLIENT_DETAIL,
                              title, 
                              null, null, null,
                              false, 
                              params);
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchScopePicker 
  /**
   ** Launch Scopes Picker task flow in dialog
   ** @param actionEvent Action event data
   */
  public void launchScopePicker(ActionEvent actionEvent){
    String methodName = "launchScopePicker";
    logger.entering(className, methodName);
    
    
    String oam_connection_name  = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    
    Map<String,Object> params = new HashMap<String,Object>();  
    params.put(TF_PARAM_OAM_CONNECTION_NAME  , oam_connection_name);
    params.put(TF_PARAM_IDENTITY_DOMAIN_NAME , identity_domain_name);
    params.put(TF_PARAM_SELECTION_TYPE       , "single");
    params.put(TF_PARAM_EVENT_DISTINGUISHER  , "pick_scope");
    
    String title = FacesUtils.getBundleValue(UI_BUNDLE, "scope-picker-title-select-scope");
    ADFFacesUtils.launchTaskFlow(title,
                              TF_URL_SCOPE_PICKER,
                              title, 
                              null, null, null,
                              true, 
                              params);
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchScopesPicker 
  /**
   ** Launch Scope Picker task flow in dialog
   ** @param actionEvent Action event data
   */
  public void launchScopesPicker(ActionEvent actionEvent){
    String methodName = "launchScopesPicker";
    logger.entering(className, methodName);
    
    String oam_connection_name  = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    
    Map<String,Object> params = new HashMap<String,Object>();  
    params.put(TF_PARAM_OAM_CONNECTION_NAME  , oam_connection_name);
    params.put(TF_PARAM_IDENTITY_DOMAIN_NAME , identity_domain_name);
    params.put(TF_PARAM_SELECTION_TYPE       , "multiple");
    params.put(TF_PARAM_EVENT_DISTINGUISHER  , "pick_scopes");
    
    String title = FacesUtils.getBundleValue(UI_BUNDLE, "scope-picker-title-add-scopes");
    ADFFacesUtils.launchTaskFlow(title,
                              TF_URL_SCOPE_PICKER,
                              title, 
                              null, null, null,
                              true, 
                              params);
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createClient 
  /**
   ** Read OAuth Client data from ADF page and create OAuth Client on OAM server
   ** @return Error message in case OAuth Client can't be created
   */
  public String createClient(){
    String methodName = "createClient";
    logger.entering(className, methodName);
    
    String status = "";
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    
    Client client = featchClientData();
    client.setIdDomain(identity_domain_name);
    
    if(isSecretCorrect(client.getSecret(), (String)secretConfirmation.getValue())){
      // Update client in OAM
      Map<String,Object> params = new HashMap<String,Object>();
      params.put("identityDomainName",identity_domain_name);
      params.put("oamConnectionName",oam_connection_name);
      params.put("client",client);
      String errorMessage = executeBindingMethod("createClient",params);
      if(errorMessage == null){
        showInfo(FacesUtils.getBundleValue(UI_BUNDLE, "client-details-info-client-created"));
        status = "success";
      }
    }
    else{
      showErrors(FacesUtils.getBundleValue(UI_BUNDLE, "client-details-error-secreet-match"));
    }
    
    logger.exiting(className, methodName);
    return status;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   saveClient 
  /**
   ** Read OAuth Client data from ADF page and update OAuth Client on OAM server
   ** @return Error message in case OAuth Client can't be updated
   */
  public String saveClient(){
    String methodName = "saveClient";
    logger.entering(className, methodName);
    
    String status = "";
    
    String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
    String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
    
    Client client = featchClientData();
    // Password can't be changed with save operation, dedicated operation for password change is needed
    client.setSecret(null);
    
    // Update client in OAM
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("identityDomainName",identity_domain_name);
    params.put("oamConnectionName",oam_connection_name);
    params.put("client",client);
    String errorMessage = executeBindingMethod("updateClient",params);
    if(errorMessage == null){
      showInfo(FacesUtils.getBundleValue(UI_BUNDLE, "client-details-info-client-updated"));
      status = "success";
    }
   
    logger.exiting(className, methodName);
   return status;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteClient 
  /**
   ** Delete OAuth Client in OAM
   ** @param dialogEvent Dialot Event data  
   */
  public void deleteClient(DialogEvent dialogEvent){
    String methodName = "deleteClient";
    logger.entering(className, methodName);
    
    if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
      String selectedClientId = getValueFromSelectedRow("id");
      
      String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
      String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);

      if(selectedClientId != null){       
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("oamConnectionName",oam_connection_name);
        params.put("identityDomainName",identity_domain_name);
        params.put("clientId",selectedClientId);
        executeBindingMethod("deleteClient",params);
      }

      //Refresh data in table
      DCIteratorBinding clientIter = FacesUtils.findIterator("clientsIterator");
      if(clientIter != null){
        clientIter.executeQuery();
      }
      AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getSearchTable()); 
    }
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetClientPassword 
  /**
   ** Reset OAuth Client password in OAM
   ** @param dialogEvent Dialot Event data  
   */
  public void resetClientPassword(DialogEvent dialogEvent){
    String methodName = "resetClientPassword";
    logger.entering(className, methodName);
    
    String newPassword        = (String)getNewPassword().getValue();
    String confirmNewPassword = (String)getConfirmNewPassword().getValue();
    
    if(newPassword != null && newPassword.length()>0 && newPassword.equals(confirmNewPassword)){
      String identity_domain_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.identity_domain_name}",String.class);
      String oam_connection_name = ADFFacesUtils.getValueFromELExpression("#{pageFlowScope.oam_connection_name}",String.class);
      String selectedClientId = getValueFromSelectedRow("id");
      
      Map<String,Object> params = new HashMap<String,Object>();
      params.put("oamConnectionName",oam_connection_name);
      params.put("identityDomainName",identity_domain_name);
      params.put("clientId",selectedClientId);
      params.put("newPassword",newPassword);
      executeBindingMethod("resetClientPassword",params);      
    }
    else{
      String validationError = FacesUtils.getBundleValue(UI_BUNDLE, "client-reset-password-error-passwords-match");
      showErrors(validationError);
    }
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshClients 
  /**
   ** Refresh OAuth client from OAM
   ** @param actionEvent Action Event data
   */
  public void refreshClients(ActionEvent actionEvent){
    String methodName = "refreshClients";
    logger.entering(className, methodName);
    
    //Reload data from OAM Server
    executeBindingMethod("refreshClients");      
    
    //Refresh data in table
    DCIteratorBinding clientIter = FacesUtils.findIterator("clientsIterator");
    if(clientIter != null){
       clientIter.executeQuery();
    }
    AdfFacesContext.getCurrentInstance().addPartialTarget(getSearchTable()); 
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAddDefaultScope 
  /**
   ** Force ADF to update Default Scope when a new value is set to binding attribute
   ** @param scope Scope object
   */
  public void handleAddDefaultScope(Scope scope){
    String methodName = "handleAddDefaultScope";
    logger.entering(className, methodName);
    
    if(scope != null){
      logger.finer("handleAddDefaultScope scopeName= "+scope.getScopeName());
      ADFFacesUtils.setAttributeBindingValue("defaultScope", scope.getScopeName());
      // Three lines bellow is trick to force update ADF RichInputText to binding value
      getDefaultScopeInputText().setSubmittedValue(null);
      getDefaultScopeInputText().resetValue();
      AdfFacesContext.getCurrentInstance().addPartialTarget(getDefaultScopeInputText());
    }
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAddScope 
  /**
   ** Add a new scope to the list of selected scopes
   ** @param scope Scope object
   */
  public void handleAddScope(Scope scope){
    String methodName = "handleAddScope";
    logger.entering(className, methodName);
    
    boolean isScopeUnique = true;
    DCIteratorBinding scopesIterator = FacesUtils.findIterator("scopesIterator");
    RowSetIterator rowScopesIterator = scopesIterator.getViewObject().createRowSetIterator(null);
    // Check if the scope is unique
    while(rowScopesIterator.hasNext()){
      String iteratorValue = (String)rowScopesIterator.next().getAttribute("value");
      String scopeName = scope.getScopeName();
      if(iteratorValue != null && scopeName != null && iteratorValue.equals(scopeName)){
        isScopeUnique =  false;
      }
    }
    // Add row to the table
    if(isScopeUnique){
      ViewObject vo = scopesIterator.getViewObject();
      Row row = vo.createRow();      
      row.setAttribute("value", scope.getScopeName());
      vo.insertRow(row);
    }

    // Refresh scope table
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getScopesTable());
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetPasswordAction 
  /**
   ** Reset Password action default value is success
   ** @return Every time returns succes
   */
  public String resetPasswordAction(){
    String methodName = "resetPasswordAction";
    logger.entering(className, methodName);
    
    logger.exiting(className, methodName);
    return "success";
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancelAction 
  /**
   ** Cancel action default value is cancel
   ** @return Every time returns cancel
   */
  public String cancelAction(){
    String methodName = "cancelAction";
    logger.entering(className, methodName);
    
    logger.exiting(className, methodName);
    return "cancel";
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMessageResetClientPassword 
  /**
   ** Get customized message for selected OAuth Client during password reset operation
   ** @return
   */
  public String getMessageResetClientPassword(){
    String methodName = "getMessageResetClientPassword";
    logger.entering(className, methodName);
    
    String selectedClientId = getValueFromSelectedRow("id");
    String message = FacesUtils.getBundleValue(UI_BUNDLE, "client-reset-password-description");
    
    logger.exiting(className, methodName);
    return  message + selectedClientId;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActionWarningMessageDelete 
  /**
   ** Get customized Warning Message during client delete operation
   ** @return
   */
  public String getActionWarningMessageDelete(){
    String methodName = "getActionWarningMessageDelete";
    logger.entering(className, methodName);
    
    String selectedClientId = getValueFromSelectedRow("id");
    String message = FacesUtils.getBundleValue(UI_BUNDLE, "client-action-warning-message-for-delete");
    
    logger.exiting(className, methodName);
    return  message + selectedClientId + " ?" ;
  }
  
  /////////////////////////////////////////////////////
  // Private Methods
  /////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   featchClientData 
  /**
   ** Read all OAuth client data from ADF page and create OAuth Client model object
   ** @return OAuth Client object
   */
  private Client featchClientData(){
    String methodName = "featchClientData";
    logger.entering(className, methodName);
    
    // Fetch client from ADF bindings
    Client client = new Client();
    
    client.setId                     (ADFFacesUtils.getAttributeBindingValue("id",String.class));
    client.setName                   (ADFFacesUtils.getAttributeBindingValue("name",String.class));
    client.setDescription            (ADFFacesUtils.getAttributeBindingValue("description",String.class));
    client.setSecret                 (ADFFacesUtils.getAttributeBindingValue("secret",String.class));
    client.setIdDomain               (ADFFacesUtils.getAttributeBindingValue("idDomain",String.class));
    client.setClientType             (ADFFacesUtils.getAttributeBindingValue("clientType",String.class));
    client.setDefaultScope           (ADFFacesUtils.getAttributeBindingValue("defaultScope",String.class));
    client.setTokenEndpointAuthMethod(ADFFacesUtils.getAttributeBindingValue("tokenEndpointAuthMethod",String.class));
    client.setUsePKCE                (ADFFacesUtils.getAttributeBindingValue("usePKCE",String.class));
    client.setTlsClientAuthSANDNS    (ADFFacesUtils.getAttributeBindingValue("tlsClientAuthSANDNS",String.class));
    client.setTlsClientAuthSANEmail  (ADFFacesUtils.getAttributeBindingValue("tlsClientAuthSANEmail",String.class));
    client.setTlsClientAuthSANIP     (ADFFacesUtils.getAttributeBindingValue("tlsClientAuthSANIP",String.class));
    client.setTlsClientAuthSANURI    (ADFFacesUtils.getAttributeBindingValue("tlsClientAuthSANURI",String.class));
    client.setTlsClientAuthSubjectDN (ADFFacesUtils.getAttributeBindingValue("tlsClientAuthSubjectDN",String.class));
    client.setIssueTLSClientCertificateBoundAccessTokens(ADFFacesUtils.getAttributeBindingValue("issueTLSClientCertificateBoundAccessTokens",Boolean.class));
        
    // scopesIterator
    DCIteratorBinding scopesIterator = FacesUtils.findIterator("scopesIterator");
    RowSetIterator rowScopesIterator = scopesIterator.getViewObject().createRowSetIterator(null);
    while(rowScopesIterator.hasNext()){
      client.getScopes().add(new SingleAttribute((String)rowScopesIterator.next().getAttribute("value")));
    }
    
    // grantTypesIterator
    DCIteratorBinding grantTypesIterator = FacesUtils.findIterator("grantTypesIterator");
    RowSetIterator rowGrantTypesIterator = grantTypesIterator.getViewObject().createRowSetIterator(null);
    while(rowGrantTypesIterator.hasNext()){
      client.getGrantTypes().add(new SingleAttribute((String)rowGrantTypesIterator.next().getAttribute("value")));
    }
    
    // accessTokenCustomClaimsIterator
    DCIteratorBinding accessTokenCustomClaimsIterator = FacesUtils.findIterator("accessTokenCustomClaimsIterator");
    RowSetIterator rowAccessTokenCustomClaimsIterator = accessTokenCustomClaimsIterator.getViewObject().createRowSetIterator(null);
    while(rowAccessTokenCustomClaimsIterator.hasNext()){
      client.getAccessTokenCustomClaims().add(new SingleAttribute((String)rowAccessTokenCustomClaimsIterator.next().getAttribute("value")));
    }
    
    // idTokenCustomClaimsIterator
    DCIteratorBinding idTokenCustomClaimsIterator = FacesUtils.findIterator("idTokenCustomClaimsIterator");
    RowSetIterator rowIdTokenCustomClaimsIterator = idTokenCustomClaimsIterator.getViewObject().createRowSetIterator(null);
    while(rowIdTokenCustomClaimsIterator.hasNext()){
      client.getIdTokenCustomClaims().add(new SingleAttribute((String)rowIdTokenCustomClaimsIterator.next().getAttribute("value")));
    }
    
    // userInfoCustomClaimsIterator
    DCIteratorBinding userInfoCustomClaimsIterator = FacesUtils.findIterator("userInfoCustomClaimsIterator");
    RowSetIterator rowUserInfoCustomClaimsIterator = userInfoCustomClaimsIterator.getViewObject().createRowSetIterator(null);
    while(rowUserInfoCustomClaimsIterator.hasNext()){
      client.getUserInfoCustomClaims().add(new SingleAttribute((String)rowUserInfoCustomClaimsIterator.next().getAttribute("value")));
    }
    
    //attributesIterator
    DCIteratorBinding attributesIterator = FacesUtils.findIterator("attributesIterator");
    RowSetIterator rowAttributesIterator = attributesIterator.getViewObject().createRowSetIterator(null);
    while(rowAttributesIterator.hasNext()){
      Row row = rowAttributesIterator.next();
      String attrName  = (String)row.getAttribute("attrName");
      String attrValue = (String)row.getAttribute("attrValue");
      String attrType  = (String)row.getAttribute("attrType");
      client.getAttributes().add(new TokenAttribute(attrName,attrValue,attrType ));
    }
    
    //redirectURIsIterator
    DCIteratorBinding redirectURIsIterator = FacesUtils.findIterator("redirectURIsIterator");
    RowSetIterator rowRedirectURIsIterator = redirectURIsIterator.getViewObject().createRowSetIterator(null);
    while(rowRedirectURIsIterator.hasNext()){
      Row row = rowRedirectURIsIterator.next();
      String url  = (String)row.getAttribute("url");
      Boolean isHttps = (Boolean)row.getAttribute("isHttps");
      client.getRedirectURIs().add(new RedirectURI(url,isHttps ));
    }
          
    logger.exiting(className, methodName);
    return client;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   isSecretCorrect 
  /**
   ** Check if the new provided password and confirm passwords are equals
   ** @param secret New OAuth client password
   ** @param secretConfirm New OAuth confirm client password
   ** @return true in case the provided password and confirm password are equals
   */
  private boolean isSecretCorrect(String secret, String secretConfirm){
    String methodName = "isSecretCorrect";
    logger.entering(className, methodName);
    
    boolean status = false;
    if(secret == null && secretConfirm == null){
      status = true;
    }else if(secret != null && secret.equals(secretConfirm)){
      status = true;
    }
    
    logger.exiting(className, methodName);
    return status;
  }

}
