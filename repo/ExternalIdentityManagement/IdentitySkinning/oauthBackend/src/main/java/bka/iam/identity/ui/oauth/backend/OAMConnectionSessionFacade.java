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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   OAuth Registration

    File        :   OAMConnectionSessionFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.backend;


import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.tcResultSet;

import bka.iam.identity.ui.oauth.model.OAMConnection;
import bka.iam.identity.ui.service.OIMService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

////////////////////////////////////////////////////////////////////////////////
// class OAMConnectionSessionFacade
// ~~~~~ ~~~~~~~~
/**
 ** Session Facade Bean used by OAuth Registration customization
 ** In this session facade bean are exposed methods to get OAM Configuration from OIM server
 ** 
 ** This Session Facade is used as implementation of the ADF DataControl <code>IdentityDomainSessionFacade</code>
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OAMConnectionSessionFacade {
  
  private static final String className = OAMConnectionSessionFacade.class.getName();
  private static Logger       logger = Logger.getLogger(className);
  
  public static final String IT_RESOURCE_TYPE = "OAM Server";
  public static final String API_IT_SERVER_DEFINITION_SERVER_TYPE = "IT Resources Type Definition.Server Type";
  public static final String API_IT_RESOURCE_KEY                  = "IT Resources.Key";
  public static final String API_IT_RESOURCE_NAME                 = "IT Resources.Name";
  public static final String API_IT_RESOURCE_PARAM_NAME           = "IT Resources Type Parameter.Name";
  public static final String API_IT_RESOURCE_PARAM_VALUE          = "IT Resources Type Parameter Value.Value";
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OAMConnectionSessionFacade</code> session bean that allows use as a
   ** ADF Data Control.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OAMConnectionSessionFacade() {
    super();
  }
  
  
  public List<OAMConnection> getOAMConnections(){
    String methodName = "getOAMConnections";
    logger.entering(className, methodName);
    
    List<OAMConnection> oamConnections = findOAMConnections("%");
    logger.finest("Number of found OAM Connestions is: "+oamConnections.size());
    
    logger.exiting(className, methodName);
    return oamConnections;
  }
  
  
  public OAMConnection getOAMConnection(String connectionName){
    String methodName = "getOAMConnection";
    logger.entering(className, methodName);
    
    OAMConnection oamConnection = null;
    List<OAMConnection> oamConnections = findOAMConnections(connectionName);
    if (oamConnections.size() == 1){
      oamConnection = oamConnections.get(0);
    }
    
    logger.exiting(className, methodName);
    return oamConnection;
  }
  
  
  
  private List<OAMConnection> findOAMConnections(String itResourceName){
    String methodName = "findOAMConnections";
    logger.entering(className, methodName);
    
    tcITResourceInstanceOperationsIntf itInstance = OIMService.getService(tcITResourceInstanceOperationsIntf.class);    
    List<OAMConnection> oamConnections = new ArrayList<OAMConnection>();

    Map<String,String> search = new HashMap<String,String>();
    search.put(API_IT_SERVER_DEFINITION_SERVER_TYPE, IT_RESOURCE_TYPE);
    search.put(API_IT_RESOURCE_NAME,itResourceName);
    try {
      tcResultSet oamServerKeys = itInstance.findITResourceInstances(search);
    
      for (int i = 0; i < oamServerKeys.getRowCount(); i++) {
        oamServerKeys.goToRow(i);
        long resourceKey    = oamServerKeys.getLongValue(API_IT_RESOURCE_KEY);
        String resourceName = oamServerKeys.getStringValue(API_IT_RESOURCE_NAME);
        
        OAMConnection oamConnection =new OAMConnection();
        oamConnection.setName(resourceName);
        
        tcResultSet oamServerParams = itInstance.getITResourceInstanceParameters(resourceKey);
        
        for (int j = 0; j < oamServerParams.getRowCount(); j++) {
          oamServerParams.goToRow(j);          
          String parameterName = oamServerParams.getStringValue(API_IT_RESOURCE_PARAM_NAME);
          String paramValue = oamServerParams.getStringValue(API_IT_RESOURCE_PARAM_VALUE);
          if (paramValue != null){
            paramValue = paramValue.trim();
          }
          //System.out.println(parameterName);
          switch(parameterName){
            case "host": 
              oamConnection.setHost(paramValue);
              break;
            case "port":
              oamConnection.setPort(paramValue);
              break;
            case "secure": 
              oamConnection.setSecure("TRUE".equalsIgnoreCase(paramValue) || "1".equals(paramValue)?true:false);
              break;
            case "identityDomainURL":
              oamConnection.setIdentityDomainURL(paramValue);
              break;
            case "applicationURL":
              oamConnection.setApplicationURL(paramValue);
              break;
            case "clientURL":
              oamConnection.setClientURL(paramValue);
              break;
            case "username" : 
              oamConnection.setUsername(paramValue);
              break;
            case "password":
              oamConnection.setPassword(paramValue);
              break;
          }
        }
        oamConnections.add(oamConnection); 
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally{
      itInstance.close();  
    }
    
    logger.exiting(className, methodName);
    return oamConnections;
  }
  
  
  
  public static void main(String args[]) throws LoginException {
    /*
    OIMService.initialize("t3://vmoig12.imguru.eu:7201", "xelsysadm", "Welcome1");
    OAMConnectionSessionFacade sf = new OAMConnectionSessionFacade();
    
    System.out.println(sf.getOAMConnections());
    System.out.println(sf.getOAMConnection("OAM Server1").getIdentityDomainConnectionString());
    System.out.println(sf.getOAMConnection("OAM Server2").getIdentityDomainConnectionString());
    */
  }
  
  
  
}
