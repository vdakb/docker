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
    Subsystem   :   IDP Discovery Plugin

    File        :   IDPDiscoveryPlugin.java

    Author      :   nitin.popli@oracle.com

    Purpose     :   This class matches the IDP name in the authentication context 
                    with the IDP name in the plugin parameter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      11.11.2022  npopli    First release version

*/
package com.oracle.authentication.plugin;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import oracle.security.am.plugin.ExecutionStatus;
import oracle.security.am.plugin.MonitoringData;
import oracle.security.am.plugin.PluginConfig;
import oracle.security.am.plugin.authn.AbstractAuthenticationPlugIn;
import oracle.security.am.plugin.authn.AuthenticationContext;
import oracle.security.am.plugin.authn.AuthenticationException;
import oracle.security.am.plugin.authn.PlugInUtil;

////////////////////////////////////////////////////////////////////////////////
// class IDPDiscoveryPlugin
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
** OAM plugin that gets the IDP name from the authentication context and matches
** it with the IDP name in the plugin parameter.
**
** @author  nitin.popli@oracle.com
** @version 0.0.0.1
*/

public class IDPDiscoveryPlugin extends AbstractAuthenticationPlugIn {
   
    //////////////////////////////////////////////////////////////////////////////
    // static final attributes
    //////////////////////////////////////////////////////////////////////////////

    
    private static final Logger LOGGER = Logger.getLogger("OCS.PLUGIN");
    private String idpName = null;
    
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
        idpName = (String)config.getParameter("IDP_Name");
        LOGGER.info(method + " IDP_Name:" + idpName);
        LOGGER.info(method + " Exit");
        return ExecutionStatus.SUCCESS;
    }
    //////////////////////////////////////////////////////////////////////////////
       // Method:   process()
       /**
        ** This method is used to read the IDP name from the authentication 
        ** context and match it with the IDP name in the plugin parameter.
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
        String idpNameContext = context.getStringAttribute("IDP_NAME");
        LOGGER.info(method + " IDP Name context:" + idpNameContext);
        String idpNameInstance = PlugInUtil.getFlowParam(context.getStringAttribute("StepName"),"IDP_Name",context);
        LOGGER.info(method + " IDP Name instance:" + idpNameInstance);
        if(idpNameInstance == null|| (idpNameInstance.length() == 0)){
            LOGGER.info(method + " IDP Name not set in the authentication plugin");
            LOGGER.info(method + " Exit");
            return ExecutionStatus.FAILURE; 
        }else{
            if(idpNameContext == null || (idpNameContext.length() == 0)){
                LOGGER.info(method + " IDP Name not set in the authentication context");
                LOGGER.info(method + " Exit");
                return ExecutionStatus.FAILURE;          
            }else{
                if(idpNameContext.equalsIgnoreCase(idpNameInstance)){
                    LOGGER.info(method + " IDP Name in the authentication plugin matches with the IDP name set in the authentication context");
                    LOGGER.info(method + " Exit");
                    return ExecutionStatus.SUCCESS; 
                }
                else{
                    LOGGER.info(method + " IDP Name in the authentication plugin does not match with the IDP name set in the authentication context");
                    LOGGER.info(method + " Exit");
                    return ExecutionStatus.FAILURE;
                }
                
            }  
        }
        
    }

    @Override
    public String getDescription() {
        return "OAM plugin to discover the IDP";
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
