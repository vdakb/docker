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

    File        :   OAMServersManagedBean.java

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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.iam.ui.platform.utils.FacesUtils;

////////////////////////////////////////////////////////////////////////////////
// class OAMServersManagedBean
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attribute
 ** values of <code>OAM Servers</code>.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OAMServersManagedBean extends AbstractMB {
  
  private static final String className = OAMServersManagedBean.class.getName();
  private static Logger       logger = Logger.getLogger(className);
  
  private static final String OAM_CONNECTION_NAME          = "oamConnectionName";
  private static final String IDENTITY_DOMAIN_TASKFLOW_URL = "/WEB-INF/bka/iam/identity/ui/oauth/tfs/identity-domain-tf.xml#identity-domain-tf";
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   callIdentityDomains
  /**
   ** Launch OAuth Idendity Domian task flow
   ** @param actionEvent
   */
  public void callIdentityDomains(ActionEvent actionEvent){
    String methodName = "callIdentityDomains";
    logger.entering(className, methodName);
    
    String oam_connection_name = (String)(actionEvent.getComponent().getAttributes().get("oam_connection_name"));
    logger.finest(TF_PARAM_OAM_CONNECTION_NAME+" = "+oam_connection_name);
    
    //Check if OUAuth connection is corret
    BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
    OperationBinding loadIdentityDomainsOB = bindings.getOperationBinding("loadIdentityDomains");
    loadIdentityDomainsOB.getParamsMap().put(OAM_CONNECTION_NAME,oam_connection_name);
    String errorMessage = (String)loadIdentityDomainsOB.execute();

    if(errorMessage == null){
      // Open a new tab with all identity domains
      Map<String,Object> params = new HashMap<String,Object>();  
      params.put(TF_PARAM_OAM_CONNECTION_NAME, oam_connection_name);
      String title = ADFFacesUtils.getBundleValue(UI_BUNDLE, "oam-header")+": "+oam_connection_name;
      ADFFacesUtils.launchTaskFlow(title,
                                IDENTITY_DOMAIN_TASKFLOW_URL,
                                title, 
                                null,
                                null,
                                null,
                                false, 
                                params);
    }
    else{
      this.showErrors(errorMessage);      
    }
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshOAMServersList
  /**
   ** Refresh list of the OAM servers from OIM IT resources (IT Resource type OAM Server)
   ** @param actionEvent
   */
  public void refreshOAMServersList(ActionEvent actionEvent) {
    String methodName = "refreshOAMServersList";
    logger.entering(className, methodName);
    
    //Refresh data in table
    DCIteratorBinding listIter = FacesUtils.findIterator("OAMConnectionsIterator");
    if(listIter != null){
      listIter.executeQuery();
    }
    AdfFacesContext.getCurrentInstance().addPartialTarget((UIComponent)getSearchTable()); 
    
    logger.exiting(className, methodName);
  }

}
