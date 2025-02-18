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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   BKA Access Policy Holder

    File        :   APHPolicyEvaluation.java

    Compiler    :   JDK 1.8

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implement the class
                    APHPolicyEvaluation.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.09.2024  TSebo    First release version
*/
package oracle.iam.identity.jes.integration.oig.aph.service;

import Thor.API.Operations.tcLookupOperationsIntf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.iam.accesspolicy.api.AccessPolicyService;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.jes.integration.oig.aph.model.OrgEntitlement;
import oracle.iam.identity.jes.integration.oig.aph.utilities.AccessPolicyUtility;
import oracle.iam.identity.jes.integration.oig.aph.utilities.EntitlementUtilities;
import oracle.iam.identity.jes.integration.oig.aph.utilities.LookupUtils;
import oracle.iam.identity.jes.integration.oig.aph.utilities.ProvisioningUtils;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.Platform;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.EntitlementAlreadyProvisionedException;
import oracle.iam.provisioning.exception.EntitlementNotFoundException;
import oracle.iam.provisioning.exception.EntitlementNotProvisionedException;
import oracle.iam.provisioning.exception.GenericEntitlementServiceException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.ImproperAccountStateException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.provisioning.vo.EntitlementInstance;

////////////////////////////////////////////////////////////////////////////////
// class APHPolicyEvaluation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>APHPolicyEvaluation</code> is a service class or the APH Connector.
 ** It reads list of the access policies and organizations assigned on the APH Account.
 ** Based on the access policies assinged on APH Account evaluate user entitlements. In
 ** case a missing entitlement is discovered, this service provision it to the user.
 ** Missing application instancesa are automaticaly provisioned to the user. Service provision
 ** entitlement via provisioning mechanism "DIRECT PROVISION".<br>
 ** 
 ** Organization name mapping is stored in lookup Lookup.APH.OUMappingDescriptor. Where code
 ** contains from two parts separated by ~. In the first part is "Application Instance Name" and in the 
 ** second part is a "Child Name" where the organizatio needs to be provisioned, like APCAccount~UD_APC_GRP.
 ** In the decode value is column name on the child form where the organization name needs to be provisioned, 
 ** like UD_APC_GRP_OUNAME.<BR>
 ** <strong>NOTE:</strong> In case the mapping is not provided connector won't know where to provision
 ** organization name. In this case entitlmenet is provisioned but without the organization name.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class APHPolicyEvaluation {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private static final String className = APHPolicyEvaluation.class.getName();
  private static final Logger logger = Logger.getLogger(className);
  
  
  private static final String APH_APP_INST_NAME     = "OIG Account";
  private static final String APH_OU_COLUMN_NAME    = "UD_OIG_UPO_OU";
  private static final String APH_OU_MAPPING_LOOKUP = "OIG.OU Role Mappings";
  
  //////////////////////////////////////////////////////////////////////////////
  // OIM API services
  //////////////////////////////////////////////////////////////////////////////
  private ProvisioningService        provService;
  private ApplicationInstanceService appInstService;
  private EntitlementService         entitlementService;
  private UserManager                userManager;
  private AccessPolicyService        accessPolicyService;
  private tcLookupOperationsIntf     lookupService;
  
  //////////////////////////////////////////////////////////////////////////////
  // OIM API utils
  //////////////////////////////////////////////////////////////////////////////
  private ProvisioningUtils    provUtils;
  private AccessPolicyUtility  apUtils;
  private EntitlementUtilities entUtils;
  private LookupUtils          lookupUtils;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  public APHPolicyEvaluation(){
    super();
  
    this.provService         = Platform.getService(ProvisioningService.class);
    this.appInstService      = Platform.getService(ApplicationInstanceService.class);
    this.entitlementService  = Platform.getService(EntitlementService.class);
    this.userManager         = Platform.getService(UserManager.class);
    this.accessPolicyService = Platform.getService(AccessPolicyService.class);
    this.lookupService       = Platform.getService(tcLookupOperationsIntf.class);
    
    this.provUtils      = new ProvisioningUtils(provService, appInstService, userManager);
    this.apUtils        = new AccessPolicyUtility(accessPolicyService,provService,appInstService,entitlementService);
    this.entUtils       = new EntitlementUtilities(provService, userManager, entitlementService);
    this.lookupUtils    = new LookupUtils(this.lookupService);
  }
  
  
  public APHPolicyEvaluation(OIMClient client){
    super();
   
    this.provService         = client.getService(ProvisioningService.class);
    this.appInstService      = client.getService(ApplicationInstanceService.class);
    this.entitlementService  = client.getService(EntitlementService.class);
    this.userManager         = client.getService(UserManager.class);
    this.accessPolicyService = client.getService(AccessPolicyService.class);
    this.lookupService       = client.getService(tcLookupOperationsIntf.class);
   
    this.provUtils      = new ProvisioningUtils(provService, appInstService, userManager);
    this.apUtils        = new AccessPolicyUtility(accessPolicyService,provService,appInstService,entitlementService);
    this.entUtils       = new EntitlementUtilities(provService, userManager, entitlementService);
    this.lookupUtils    = new LookupUtils(this.lookupService);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   evaluatePolicyForUser
  /**
   ** This is a main method used for APH Policy Evaluation, as input parameter is used user key (usr_key).
   ** APH policy evaluation consist of following steps:
   ** <ol>
   **   <li>Search for all entitlements on AccessPolicyHolder (APH Account)</li>
   **   <li>Get all entitlements related to the AccessPolicyHolder</li>
   **   <li>Calculate entitlements which needs to be provisioned</li>
   **   <li>Create missing accounts related to the AccessPolicyHolder</li>
   **   <li>Provision missing entitlements</li>
   ** </ol>
   ** @param userKey
   */
  public void evaluatePolicyForUser(String userKey){
    String methodName = "evaluatePolicyForUser";
    logger.entering(className, methodName);
    
    try {
      //1. Find "Special Access Policy " (entitlements) on AccessPolicyHolder Application instance (APH Account)  
      List<OrgEntitlement> accessPolicyEnts = getAccessPolicyOrgEntitlements(userKey);
      if(accessPolicyEnts.size() >0){
        //2. Get all entitlements provisioned to user exlude APH entitlements
        List<EntitlementInstance> userEntitlements = provUtils.getEntitlementsForUser(userKey, APH_APP_INST_NAME,true);
        logger.log(Level.FINEST, " Already provisioned entitlements: {0}" , new Object[] { userEntitlements});
            
        //3. Calculate entitlements which needs to be provisioned
        removeAlreadyProvisioned(accessPolicyEnts, userEntitlements);
        logger.log(Level.FINEST, "APH need to provsision size {0},  data: {1}" , new Object[] { (accessPolicyEnts == null?"0":accessPolicyEnts.size()), accessPolicyEnts});
                 
        //4. Create missing accounts on user profile
        createMissingAccounts(userKey);
        
        //5. Provision missing entitlements
        provisionEntitlements(userKey,accessPolicyEnts);
      }
      
    } catch (GenericProvisioningException | UserNotFoundException e) {
      logger.warning("Special Access Policy can't be found on AppInstance '"+APH_APP_INST_NAME+"', error message: "+e);
    }

    logger.exiting(className, methodName);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccessPolicyOrgEntitlements
  /**
   * Get all entitlements defined on "Special Access Policy" with organization name.
   * This method serach for all entitlements on "APH Account", this entitlement represent "Special Access Policy" id
   * with organization name. Method return all entitlements defined on "Specla Access Policies" together with org name.
   * @param userKey User key (USR.USR_KEY)
   * @return all entitlements defined on "Specla Access Policies" together with org name
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<OrgEntitlement> getAccessPolicyOrgEntitlements(String userKey) throws UserNotFoundException,
                                                                         GenericProvisioningException {
    return getAccessPolicyOrgEntitlements(userKey, null);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccessPolicyOrgEntitlements
  /**
   * Get all entitlements defined on "Special Access Policy" with organization name.
   * This method serach for all entitlements on "APH Account", this entitlement represent "Special Access Policy" id
   * with organization name. Method return all entitlements defined on "Specla Access Policies" together with org name.
   * @param userKey User key (USR.USR_KEY)
   * @param exlucdePolKey policy key which needs to be exluded from result
   * @return all entitlements defined on "Specla Access Policies" together with org name
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<OrgEntitlement> getAccessPolicyOrgEntitlements(String userKey, String exlucdePolKey) throws UserNotFoundException,
                                                                         GenericProvisioningException {
    String methodName = "getAccessPolicyOrgEntitlements";
    logger.entering(className, methodName);
    
    List<OrgEntitlement> orgEntitlements= new ArrayList<OrgEntitlement>();
    
    List<EntitlementInstance> specialAPs = provUtils.getEntitlementsForUser(userKey, APH_APP_INST_NAME);
    logger.fine("On userKey '"+userKey+"' was fond '"+ specialAPs.size() +" Special AP");
    
    for(EntitlementInstance specialAP: specialAPs){
      String specialAPKey = removeFirstTilda(specialAP.getEntitlement().getEntitlementCode());
      if(specialAPKey != null && !specialAPKey.equals(exlucdePolKey)){ //Exclude PolKey
        String orgName = (String) specialAP.getChildFormValues().get(APH_OU_COLUMN_NAME);
        logger.log(Level.FINE, "Special Access Policy Key: {0}, Org Name: {1}", new Object[] { specialAPKey, orgName });
        
        //2.2. Get all entitlements from Special Access policy
        List<Entitlement> aphEnt = apUtils.getEntitlements(specialAPKey);
        for(Entitlement ent : aphEnt){
          orgEntitlements.add(new OrgEntitlement(ent,orgName));
        }
        logger.log(Level.FINEST, " AccessPolicyKey: {0} AccessPolicyEnt: {1}" , new Object[] { specialAPKey, aphEnt});
      }
    }
    
    logger.exiting(className, methodName);
    return orgEntitlements;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserOrgEntitlements
  /**
   * Get list of the APH entitlements curretnly assinged to the user identified via userKey (usr_key)
   * @param userKey User indentitfier (usr_key)
   * @return list of the APH entitlements curretnly assinged to the user identified via userKey
   * @throws UserNotFoundException
   * @throws GenericProvisioningException
   */
  public List<OrgEntitlement> getUserOrgEntitlements(String userKey) throws UserNotFoundException,
                                                                            GenericProvisioningException {
    String methodName = "getUserOrgEntitlements";
    logger.entering(className, methodName);
    List<OrgEntitlement> orgEntitlements= new ArrayList<OrgEntitlement>();
    
    //Get entitlement assigned to user
    List<EntitlementInstance> userEntitlements = provUtils.getEntitlementsForUser(userKey, APH_APP_INST_NAME,true);
    for(EntitlementInstance ei : userEntitlements){
      if("DIRECT PROVISION".equals(ei.getProvisionedByMechanism())){
        String orgColName = getEntitlementOrgNameColumnName(ei.getEntitlement());
        String entOrgName = (String)ei.getChildFormValues().get(orgColName);
        orgEntitlements.add(new OrgEntitlement(ei.getEntitlement(),entOrgName));
      }
      
    }
    
    logger.exiting(className, methodName);
    return orgEntitlements;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAccessPolicyHolderEntitlement
  /**
   ** This method is called from Process Task Adapter, with input parameters lisk usr_key, access policy key and 
   ** organization name. This adapter method is executed when a APH entitlement is removed from user.
   ** @param userKey User key (usr_key)
   ** @param specialAPKey Access Policy Key (pol_key)
   ** @param orgName Organization Name stored on APH entitlement
   ** @return
   */
  public String removeOURole(long userKey, long specialAPKey, String orgName){
    String methodName = "removeAccessPolicyHolderEntitlement";
    logger.entering(className, methodName);
    String response = "SUCCESS";

    
    try { 
      //1. Entitlement defined by "Special Access Policies"  on user profile
      //   entitlement of specialAPKey which which going to be removed are not present
      //   in remainingOrgEnt
      List<OrgEntitlement> remainingOrgEnt = getAccessPolicyOrgEntitlements(Long.toString(userKey), Long.toString(specialAPKey));
      
      //2. Get entitlements defined by "Special Access Policy", which should be removed
      List<EntitlementInstance> revokeEntitlements = new ArrayList<EntitlementInstance>();
      List<Entitlement> remove = apUtils.getEntitlements(Long.toString(specialAPKey));
      for(Entitlement ent : remove){
        // Entitlement which should be removed
        OrgEntitlement toRemove = null;
        if(getEntitlementOrgNameColumnName(ent)!=null){
          toRemove = new OrgEntitlement(ent,orgName);
        }
        else{
          toRemove = new OrgEntitlement(ent,null);
        }
        // Before entitlement can be removed, must be checked if the entitlement is already provisioned on user
        // and entitlemnet is not defined on different "Special Access Policy" assinged to user
        // Entitlement can be removed only in case is NOT defined on remaining the special access policy
        if(!remainingOrgEnt.contains(toRemove)){
          EntitlementInstance entInst = entUtils.getEntitlementInstance(Long.toString(userKey), toRemove.getEntitlement().getDisplayName());
          // Check if the entitlement is provisioned to user with the same organzization
          // and if it is a direct provisioning
          if(entInst != null){
            if("DIRECT PROVISION".equals(entInst.getProvisionedByMechanism())){
              String orgColName = getEntitlementOrgNameColumnName(entInst.getEntitlement());
              String entOrgName = (String)entInst.getChildFormValues().get(orgColName);
              OrgEntitlement userOrgEntitlement =  new OrgEntitlement(entInst.getEntitlement(),entOrgName);
              if(userOrgEntitlement.equals(toRemove)){
                revokeEntitlements.add(entInst);    
              }
              else{
                logger.log(Level.INFO, "Entitlement with code: {0}, can't be revoked becuse is not provisioned on user", new Object[] { toRemove.getEntitlement().getEntitlementCode()});
              }
            }
            else{
              logger.log(Level.INFO, "Entitlement with code: {0}, can't be revoked becuse wasn't provisioned via DIRECT Provisioning", new Object[] { toRemove.getEntitlement().getEntitlementCode()});
            }
          }
        }
        else{
          logger.log(Level.INFO, "Entitlement with code: {0}, can't be revoked becuse is already defined on remaining Special Access Policies.", new Object[] { toRemove.getEntitlement().getEntitlementCode()});
        }
      }
      
      //3. Revoke Entitlements from user
      for(EntitlementInstance revokeEntitlement :revokeEntitlements){
        try {
          provService.revokeEntitlement(revokeEntitlement);
          logger.log(Level.INFO, "Entitlement with code: {0}, was revked from user [usr_key]: {1}", new Object[] { revokeEntitlement.getEntitlement().getEntitlementCode(),userKey});
        } catch (AccountNotFoundException | EntitlementNotProvisionedException e) {
          logger.log(Level.WARNING, "Entitlement with code: {0}, can't be revoked: {1}", new Object[] { revokeEntitlement.getEntitlement().getEntitlementCode(),e.getErrorMessage()});
          response = "ERROR";
        }
      }
            
    } catch (GenericProvisioningException | UserNotFoundException  e) {
      logger.log(Level.WARNING, "GenericProvisioningException error message: {0}.", new Object[] { e.toString()});
      response = "ERROR";
    } 

    logger.exiting(className, methodName);
    return response;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMissingAccounts
  /**
   ** Create missing accounts needed for entitlement provisioning
   ** @param userKey
   ** @throws UserNotFoundException
   ** @throws GenericProvisioningException
   */
  private void createMissingAccounts(String userKey) throws UserNotFoundException, GenericProvisioningException {
    String methodName = "createMissingAccounts";
    logger.entering(className, methodName);
  
    List<EntitlementInstance> specialAPs = provUtils.getEntitlementsForUser(userKey, APH_APP_INST_NAME);
    logger.fine("On userKey '"+userKey+"' was fond '"+ specialAPs.size() +" Special AP");
    
    for(EntitlementInstance specialAP: specialAPs){
      String specialAPKey = removeFirstTilda(specialAP.getEntitlement().getEntitlementCode());
      apUtils.createAccounts(specialAPKey,userKey);
    }
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAlreadyProvisioned
  /**
   * Remove already provisioned entitlements @param userEntitlements from the list of the @param accessPolicyEnts
   * @param accessPolicyEnts
   * @param userEntitlements
   */
  private void removeAlreadyProvisioned(List<OrgEntitlement> accessPolicyEnts, List<EntitlementInstance> userEntitlements) {
    String methodName = "removeAlreadyProvisioned";
    logger.entering(className, methodName);
    List<OrgEntitlement> toRemove = new ArrayList<OrgEntitlement>();
    if(accessPolicyEnts != null){
      for(OrgEntitlement accessPolicyEnt:accessPolicyEnts){
        if(userEntitlements != null){
          for(EntitlementInstance userEntitlement: userEntitlements){
            if(accessPolicyEnt.getEntitlement().getEntitlementCode().equals(userEntitlement.getEntitlement().getEntitlementCode())){
              toRemove.add(accessPolicyEnt);
            }
          }
        }
      }
      // Remove entitlement from list;
      accessPolicyEnts.removeAll(toRemove);
    }
    logger.exiting(className, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisionEntitlements
  /**
   ** Provision entitlemnets with organization name to the user identified by userKey
   ** @param userKey
   ** @param accessPolicyEnts
   */
  private void provisionEntitlements(String userKey, List<OrgEntitlement> accessPolicyEnts) {
    String methodName = "removeAlreadyProvisioned";
    logger.entering(className, methodName);
    //1. Get mapping lookuup Lookup.APH.OUMappingDescriptor
    Map<String,String> ouMap = this.lookupUtils.getLookupValues(APH_OU_MAPPING_LOOKUP);
    
    //2. Provision entitlement to OIM
    for(OrgEntitlement accessPolicyEnt: accessPolicyEnts){
      // Set organization attribute on supported connectors
      Map<String, Object> entitlementAttributes = new HashMap<String, Object>();
      String colName = getEntitlementOrgNameColumnName(accessPolicyEnt.getEntitlement());
      if(colName != null){
        entitlementAttributes.put(colName, accessPolicyEnt.getOrgName());
      }
      
      //Provision entitlement to user
      try {
        this.entUtils.grantEntitlementToUser(userKey, 
                                             accessPolicyEnt.getEntitlement().getAppInstance().getDisplayName(),
                                             accessPolicyEnt.getEntitlement().getEntitlementCode(),
                                             entitlementAttributes);
      } catch (AccountNotFoundException | EntitlementAlreadyProvisionedException | EntitlementNotFoundException |
               GenericEntitlementServiceException | GenericProvisioningException | ImproperAccountStateException |
               NoSuchUserException | UserLookupException | UserNotFoundException e) {
        logger.warning("Entitlement '"+accessPolicyEnt.getEntitlement().getEntitlementCode()+"' can't be provisioned to the usr_key '"+userKey+"', error: "+e);
      }
    }
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlementOrgNameColumnName
  /**
   ** Get Entitlement column name on Organization Name supported connectors
   ** @param ent OIG entitlement
   ** @return Column name representing Organization Name on entitlement
   */
  private String getEntitlementOrgNameColumnName(Entitlement ent){
    Map<String,String> ouMap = this.lookupUtils.getLookupValues(APH_OU_MAPPING_LOOKUP);
    String filter = String.format("%s~%s",ent.getAppInstance().getApplicationInstanceName(),ent.getFormName());
    logger.fine("Searching for '"+filter+"' in lookup '"+APH_OU_MAPPING_LOOKUP+"'");
    String colName = ouMap.get(filter);
    if(colName != null){
      logger.fine("OU column name was found:'"+colName+"', in lookup '"+APH_OU_MAPPING_LOOKUP+"' for filter value'"+filter+"'");
    }
    else{
      logger.info("OU Column name was not found in lookup '"+APH_OU_MAPPING_LOOKUP+"', for filter value'"+filter+"'");
    }
    return colName;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeFirstTilda
  /**
   ** Remove text before the fist tida (~) from string
   ** @param entitlementCode String which contains ~
   ** @return Substring after special character ~
   */
  private String removeFirstTilda(String entitlementCode){
    String value = null;
    if(entitlementCode != null){
      value = entitlementCode.substring(entitlementCode.indexOf("~")+1);
    }
    return value;
  }
  
}
