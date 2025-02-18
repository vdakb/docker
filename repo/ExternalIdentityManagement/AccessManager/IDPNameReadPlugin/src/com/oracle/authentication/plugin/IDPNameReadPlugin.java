/*
    ORACLE Deutschland B.V. & Co. KG

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

    Copyright  2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   OAM Authentication Plugins
    Subsystem   :   IDP Name Read Plugin

    File        :   IDPNameReadPlugin.java

    Author      :   nitin.popli@oracle.com

    Purpose     :   This class is used to read the IDP name from the request.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      10.11.2022  npopli    First release version

*/
package com.oracle.authentication.plugin;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import oracle.security.am.plugin.ExecutionAction;
import oracle.security.am.plugin.ExecutionStatus;
import oracle.security.am.plugin.MonitoringData;
import oracle.security.am.plugin.PluginConfig;
import oracle.security.am.plugin.authn.AbstractAuthenticationPlugIn;
import oracle.security.am.plugin.authn.AuthenticationContext;
import oracle.security.am.plugin.authn.AuthenticationException;
import oracle.security.am.plugin.authn.PlugInUtil;
import oracle.security.am.plugin.impl.CredentialMetaData;
import oracle.security.am.plugin.impl.UserAction;
import oracle.security.am.plugin.impl.UserActionContext;
import oracle.security.am.plugin.impl.UserContextData;
import oracle.security.am.plugin.impl.UserActionMetaData;

////////////////////////////////////////////////////////////////////////////////
// class IDPNameReadPlugin
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
** OAM plugin that reads the IDP name from the request
**
** @author  nitin.popli@oracle.com
** @version 0.0.0.1
*/

public class IDPNameReadPlugin extends AbstractAuthenticationPlugIn {
   
    //////////////////////////////////////////////////////////////////////////////
    // static final attributes
    //////////////////////////////////////////////////////////////////////////////

    
    private static final Logger LOGGER = Logger.getLogger("OCS.PLUGIN");

    
    //////////////////////////////////////////////////////////////////////////////
       // Method:   initialize()
       /**
        ** This method is used to read the init configuration attributes
        **
        ** @param  config
        ** @return Execution Status
        **
        */
    @Override
    public ExecutionStatus initialize(PluginConfig config){
        final String method = "initialize()";
        LOGGER.info(method + " Enter"); 
        super.initialize(config);
        LOGGER.info(method + " Exit");
        return ExecutionStatus.SUCCESS;
    }
    //////////////////////////////////////////////////////////////////////////////
       // Method:   process()
       /**
        ** This method is used to read the IDP name from request and set it 
        ** in the authentication context
        ** 
        **
        ** @param  context
        ** @return Execution Status
        **
        */ 
    @Override
    public ExecutionStatus process(AuthenticationContext context) throws AuthenticationException{
        final String method = "process()";
        LOGGER.info(method + " Enter");
        LOGGER.info(method + " Resource URL:" + context.getResourceURL());
        String  idpName = null;
        String queryString = "";
        String url = context.getResourceURL();
        if(url != null && url.contains("?")){
            queryString = url.split("\\?")[1];
            String[] urlParams = queryString.split("&");
            for(int i = 0 ; i < urlParams.length ; i++){
                String urlParam = urlParams[i];
                if(urlParam.startsWith("idp_name=")){
                   idpName = urlParam.split("=")[1];
                   break;
                }    
            }   
        }
        if(idpName == null){
            LOGGER.info(method + " IDP Name missing in the request,redirecting to IDP discovery page");
            String idpDiscoveryURL = PlugInUtil.getFlowParam(context.getStringAttribute("StepName"),"IDP_Discovery_URL",context);
            LOGGER.info(method + " idpDiscoveryURL:" + idpDiscoveryURL);
            UserContextData urlContext = new UserContextData(idpDiscoveryURL, new CredentialMetaData("URL"));
            LOGGER.info(method + " QueryString:" + queryString);
            UserContextData queryStringContext = new UserContextData(queryString, new CredentialMetaData("QUERY_STRING"));
            UserActionContext actionContext = new UserActionContext();
            actionContext.getContextData().add(urlContext);
            actionContext.getContextData().add(queryStringContext);
            UserActionMetaData userAction = UserActionMetaData.REDIRECT_GET;
            UserAction action = new UserAction(actionContext, userAction);
            context.setAction((ExecutionAction)action);
            LOGGER.info(method + " Pause");
            return ExecutionStatus.PAUSE;          
        }else{
            context.setStringAttribute("IDP_NAME", idpName);
            LOGGER.info(method + " IDP Name: "+ idpName);
            LOGGER.info(method + " Exit");
            return ExecutionStatus.SUCCESS;
        }  
    }

    @Override
    public String getDescription() {
        return "OAM plugin to read the IDP name from the request";
    }

    @Override
    public Map<String, MonitoringData> getMonitoringData() {
        return Collections.emptyMap();
    }

    @Override
    public boolean getMonitoringStatus() {
        return false;
    }

    @Override
    public String getPluginName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getRevision() {
        return 0;
    }

    @Override
    public void setMonitoringStatus(boolean b) {
    }
}
