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

    File        :   IdentityDomainSessionFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/

package bka.iam.identity.ui.oauth.backend;

import bka.iam.identity.ui.oauth.model.Application;
import bka.iam.identity.ui.oauth.model.Client;
import bka.iam.identity.ui.oauth.model.IdentityDomain;
import bka.iam.identity.ui.oauth.model.OAMConnection;
import bka.iam.identity.ui.oauth.model.Scope;
import bka.iam.identity.ui.oauth.rest.ApplicationProvider;
import bka.iam.identity.ui.oauth.rest.ClientProvider;
import bka.iam.identity.ui.oauth.rest.IdentityDomainProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

////////////////////////////////////////////////////////////////////////////////
// class IdentityDomainSessionFacade
// ~~~~~ ~~~~~~~~
/**
 ** Session Facade Bean used by OAuth Registration customization
 ** In this session facade bean are exposed methods used to manipluate OAuth
 ** configuration on OAM Server
 ** 
 ** This Session Facade is used as implementation of the ADF DataControl <code>IdentityDomainSessionFacade</code>
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityDomainSessionFacade {
    
  private static final String className = IdentityDomainSessionFacade.class.getName();
  private static Logger       logger = Logger.getLogger(className);
  
  private List<IdentityDomain> identityDomains;
  private IdentityDomain identityDomain;
  
  private List<Application> applications;
  private Application application;
  
  private List<Client> clients;
  private Client client;
  
  private IdentityDomainProvider identProv;
  private ApplicationProvider    appProv;
  private ClientProvider         clientProv;
  
  private List<Scope>            allScopes;
    
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityDomainSessionFacade</code> session bean that allows use as a
   ** ADF Data Control.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityDomainSessionFacade() {
    super();
    this.identityDomains = new  ArrayList<IdentityDomain>();
    this.identityDomain  = new IdentityDomain();
    
    this.applications = new  ArrayList<Application>();
    this.application  = new  Application();
    
    this.clients = new  ArrayList<Client>();
    this.client  = new  Client();
    
  }


  public void setIdentityDomains(List<IdentityDomain> identityDomains) {
    this.identityDomains = identityDomains;
  }

  public List<IdentityDomain> getIdentityDomains() {
    return identityDomains;
  }
  
  public void setIdentityDomain(IdentityDomain identityDomain) {
    this.identityDomain = identityDomain;
  }

  public IdentityDomain getIdentityDomain() {
    return identityDomain;
  }

  public void setApplications(List<Application> applications) {
    this.applications = applications;
  }

  public List<Application> getApplications() {
    return applications;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  public Application getApplication() {
    return application;
  }

  public void setClients(List<Client> clients) {
    this.clients = clients;
  }

  public List<Client> getClients() {
    return clients;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Client getClient() {
    return client;
  }

  public void setAllScopes(List<Scope> allScopes) {
    this.allScopes = allScopes;
  }

  public List<Scope> getAllScopes() {
    return allScopes;
  }

  public String loadIdentityDomains(String oamConnectionName){
    String methodName = "loadIdentityDomains";
    logger.entering(className, methodName);
    logger.finest("oamConnectionName = "+oamConnectionName);
    
    String errorMessage = null;
    if(oamConnectionName != null){
      setIdentityDomainProvider(oamConnectionName);
      if(identProv != null){
        try {
          identityDomains = identProv.getIdentityDomains();
        } catch (Exception e) {
          errorMessage = e.getMessage();
          e.printStackTrace();
        }
      }
    }
    else{
      errorMessage="Input parameter oamConnectionName is null";
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String deleteIdentityDomain(String oamConnectionName, String identityDomainName){
    String methodName = "deleteIdentityDomain";
    logger.entering(className, methodName);
    logger.finest("oamConnectionName="+oamConnectionName+"identityDomainName="+identityDomainName);
    String errorMessage = null;
    if(identProv == null){
      setIdentityDomainProvider(oamConnectionName);
    }
    if(identProv != null){
      try {
        identProv.deleteIdentityDomain(identityDomainName);
        logger.finest("Identity Domain collection size before remove is: "+this.identityDomains.size());
        for(IdentityDomain domain: this.identityDomains){
          if(domain.getName().equals(identityDomainName)){
            this.identityDomains.remove(domain);
            break;
          }
        }
        logger.finest("Identity Domain collection size after remove is: "+this.identityDomains.size());
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String createIdentityDomain(String oamConnectionName, IdentityDomain identityDomain){
    String methodName = "createIdentityDomain";
    logger.entering(className, methodName);
    String errorMessage = null;
    if(identProv == null){
      setIdentityDomainProvider(oamConnectionName);
    }
    if(identProv != null){
      try {
        identProv.createIdentityDomain(identityDomain);
        if(identityDomains != null)
          identityDomains.add(identityDomain); 
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String updateIdentityDomain(String oamConnectionName, IdentityDomain identityDomain){
    String methodName = "updateIdentityDomain";
    logger.entering(className, methodName);
    String errorMessage = null;
    if(identProv == null){
      setIdentityDomainProvider(oamConnectionName);
    }
    if(identProv != null){
      try {
        identProv.updateIdentityDomain(identityDomain);
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String refreshIdentityDomains(){
    String methodName = "refreshIdentityDomains";
    logger.entering(className, methodName);
    String errorMessage = null;
    logger.finest("Identity Domain collection size before refresh is: "+identityDomains.size());
    if(identProv != null){
      try {
        identityDomains = identProv.getIdentityDomains();
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.finest("Identity Domain collection size after refresh is: "+identityDomains.size());
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String loadIdentityDomain(String oamConnectionName, String identityDomainName) {
    String methodName = "loadIdentityDomain";
    logger.entering(className, methodName);
    String errorMessage = null;
    if(identityDomainName == null){
      // Create empty identity domain, it is used during a creation of the new domain.
      identityDomain = new IdentityDomain();
    }
    else{
      try {
        if(identProv == null){
          setIdentityDomainProvider(oamConnectionName);
        }
        if(identProv != null){
          identityDomain = identProv.getIdentityDomain(identityDomainName);
        }
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String loadApplications(String oamConnectionName, String identityDomainName){
    String methodName = "loadApplications";
    logger.entering(className, methodName);
    String errorMessage = null;
    if(appProv == null){
      setApplicationProvider(oamConnectionName,identityDomainName);
    }
    if(appProv != null){
      try {
        applications = appProv.getApplications();
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String loadApplication(String oamConnectionName, String identityDomainName, String applicationName){
    String methodName = "loadApplication";
    logger.entering(className, methodName);
    String errorMessage = null;
    if(appProv == null){
      setApplicationProvider(oamConnectionName,identityDomainName);
    }
    if(appProv != null){
      try {
        application = appProv.getApplication(applicationName);
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String loadClients(String oamConnectionName, String identityDomainName){
    String methodName = "loadClients";
    logger.entering(className, methodName);
    String errorMessage = null;
    if(clientProv == null){
      setClientProvider(oamConnectionName,identityDomainName);
    }
    if(clientProv != null){
      try {
        clients = clientProv.getClients();
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String loadClient(String oamConnectionName, String identityDomainName, String clietId){
    String methodName = "loadClient";
    logger.entering(className, methodName);
    String errorMessage = null;
    if(clientProv == null){
      setClientProvider(oamConnectionName,identityDomainName);
    }
    if(clientProv != null){
      try {
        client = clientProv.getClient(clietId);
        client.setAllScopes(loadAllScopes(oamConnectionName,identityDomainName));
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String refreshClients(){
    String methodName = "refreshClients";
    logger.entering(className, methodName);
    String errorMessage = null;
    logger.finest("Client collection size before refresh is: "+clients.size());
    if(clientProv != null){
      try {
        this.clients = clientProv.getClients();
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.finest("Client collection size after refresh is: "+clients.size());
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String refreshApplications(){
    String methodName = "refreshApplications";
    logger.entering(className, methodName);
    
    String errorMessage = null;
    logger.finest("Application collection size before refresh is: "+applications.size());
    if(appProv != null){
      try {
        this.applications = appProv.getApplications();
      } catch (Exception e) {
        errorMessage =  e.getMessage();
      }
    }
    logger.finest("Application collection size after refresh is: "+applications.size());
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String createClient(String oamConnectionName, String identityDomainName, Client client){
    String methodName = "createClient";
    logger.entering(className, methodName);
    
    String errorMessage = null;
    if(clientProv == null){
      setClientProvider(oamConnectionName,identityDomainName);
    }
    if(clientProv != null){
      try {
        clientProv.createClient(client);
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  
  public String createApplication(String oamConnectionName, String identityDomainName, Application application) {
    String methodName = "createApplication";
    logger.entering(className, methodName);
    
    String errorMessage = null;
    if(appProv == null){
      setApplicationProvider(oamConnectionName,identityDomainName);
    }
    if(appProv != null){
      try {
        appProv.createApplication(application);
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String updateClient(String oamConnectionName, String identityDomainName, Client client){
    String methodName = "updateClient";
    logger.entering(className, methodName);
    
    String errorMessage = null;
    if(clientProv == null){
      setClientProvider(oamConnectionName,identityDomainName);
    }
    if(clientProv != null){
      try {
        clientProv.updateClient(client);
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  
  public String updateApplication(String oamConnectionName, String identityDomainName, Application application){
    String methodName = "updateApplication";
    logger.entering(className, methodName);
    
    String errorMessage = null;
    if(appProv == null){
      setApplicationProvider(oamConnectionName,identityDomainName);
    }
    if(appProv != null){
      try {
        appProv.updateApplication(application);
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String deleteClient(String oamConnectionName, String identityDomainName, String clientId) {
    String methodName = "deleteClient";
    logger.entering(className, methodName);
    
    String errorMessage = null;
    if(clientProv == null){
      setClientProvider(oamConnectionName,identityDomainName);
    }
    if(clientProv != null){
      try {
        clientProv.deleteClient(clientId);
        for(Client client: this.clients){
          if(client.getId().equals(clientId)){
            this.clients.remove(client);
            break;
          }
        }
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
      
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  public String deleteApplication(String oamConnectionName, String identityDomainName, String applicationId) {
    String methodName = "deleteApplication";
    logger.entering(className, methodName);
    
    String errorMessage = null;
    if(appProv == null){
      setApplicationProvider(oamConnectionName,identityDomainName);
    }
    if(appProv != null){
      try {
        appProv.deleteApplication(applicationId);
        for(Application application: this.applications){
          if(application.getResourceServerId().equals(applicationId)){
            this.applications.remove(application);
            break;
          }
        }
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  
  public String resetClientPassword(String oamConnectionName, 
                                  String identityDomainName, 
                                  String clientId,
                                  String newPassword) {
    String methodName = "resetClientPassword";
    logger.entering(className, methodName);
    String errorMessage = null;
    if(clientProv == null){
      setClientProvider(oamConnectionName,identityDomainName);
    }
    if(clientProv != null){
      try {
        clientProv.resetClientPassword(identityDomainName, clientId, newPassword);
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
    }
    logger.exiting(className, methodName);
    return errorMessage;
  }
  
  
  public List<Scope> loadAllScopes(String oamConnectionName, String identityDomainName){
    String methodName = "loadAllScopes";
    logger.entering(className, methodName);
    
    List<Scope> scopes = new ArrayList<Scope>();
    if(appProv == null){
      setApplicationProvider(oamConnectionName,identityDomainName);
    }
    if(appProv != null){
      try {
        scopes = appProv.getAllScopes();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    this.allScopes = scopes;
    logger.exiting(className, methodName);
    return this.allScopes;
  }

  private void setIdentityDomainProvider(String oamConnectionName){
    String methodName = "setIdentityDomainProvider";
    logger.entering(className, methodName);
    OAMConnectionSessionFacade oamConnections = new OAMConnectionSessionFacade();
    OAMConnection oamConnection = oamConnections.getOAMConnection(oamConnectionName);
    if(oamConnection != null){
      identProv = new IdentityDomainProvider(oamConnection.getIdentityDomainConnectionString(),
                                             oamConnection.getUsername(),
                                             oamConnection.getPassword());
    }
    logger.exiting(className, methodName);
  }
  
  private void setApplicationProvider(String oamConnectionName, String identityDomainName){
    String methodName = "setApplicationProvider";
    logger.entering(className, methodName);
    OAMConnectionSessionFacade oamConnections = new OAMConnectionSessionFacade();
    OAMConnection oamConnection = oamConnections.getOAMConnection(oamConnectionName);
    if(oamConnection != null){
      appProv = new ApplicationProvider(oamConnection.getApplicationConnectionString(),
                                        oamConnection.getUsername(),
                                        oamConnection.getPassword(),
                                        identityDomainName);
    }
    logger.exiting(className, methodName);
  }
  
  private void setClientProvider(String oamConnectionName, String identityDomainName){
    String methodName = "setClientProvider";
    logger.entering(className, methodName);
    OAMConnectionSessionFacade oamConnections = new OAMConnectionSessionFacade();
    OAMConnection oamConnection = oamConnections.getOAMConnection(oamConnectionName);
    if(oamConnection != null){
      clientProv = new ClientProvider(oamConnection.getClientConnectionSting(),
                                oamConnection.getUsername(),
                                oamConnection.getPassword(),
                                identityDomainName);
    }
    logger.exiting(className, methodName);
  }
  

  public static void main(String[] args) throws LoginException {
    /*
    OIMService.initialize("t3://vmoig12.imguru.eu:7201", "xelsysadm", "Welcome1");
    IdentityDomainSessionFacade facade = new IdentityDomainSessionFacade();
    
    facade.loadIdentityDomains("OCI OAM SP Server");
    
    
    List<IdentityDomain> domains = facade.getIdentityDomains();
    System.out.println(domains.size());
        
    facade.delete("mytestIdentityDomain4");

    facade.refresh();
    List<IdentityDomain> domains = facade.getIdentityDomains();
    System.out.println(domains.size());

    facade.loadIdentityDomain("OCI OAM SP Server","OIDCDefaultDomain");
    IdentityDomain identDomain = facade.getIdentityDomain();
    
    System.out.println(facade.getIdentityDomain());
    
    
    facade.loadClients("OCI OAM SP Server","OIDCDefaultDomain");
    List<Client> clients = facade.getClients();
    System.out.println(clients);
    
    facade.loadClient("OCI OAM SP Server","OIDCDefaultDomain","OIM_TestClient");
    
    System.out.println(facade.getClient());
    
    IdentityDomain id = new IdentityDomain();
    id.setName("TestDeleteMe");
    id.setDescription("TestDeleteMe Description");
    facade.createIdentityDomain("OCI OAM SP Server", id);
    
    //facade.loadApplications("OCI OAM SP Server", "OIDCDefaultDomain");
    
    //facade.loadApplication("OCI OAM SP Server", "OIDCDefaultDomain","ZIMP");
    facade.resetClientPassword("OCI OAM SP Server", "DeleteME","d44","newPassword45");
    */

  }



}
