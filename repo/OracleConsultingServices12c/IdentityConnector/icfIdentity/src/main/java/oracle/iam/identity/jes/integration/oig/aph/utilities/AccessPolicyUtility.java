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

    File        :   AccessPolicyUtility.java

    Compiler    :   JDK 1.8

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implement the class
                    AccessPolicyUtility.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.09.2024  TSebo    First release version
*/
package oracle.iam.identity.jes.integration.oig.aph.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.iam.accesspolicy.api.AccessPolicyService;
import oracle.iam.accesspolicy.exception.AccessPolicyServiceException;
import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.accesspolicy.vo.AccessPolicyElement;
import oracle.iam.accesspolicy.vo.ChildAttribute;
import oracle.iam.accesspolicy.vo.DefaultData;
import oracle.iam.accesspolicy.vo.Record;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.GenericEntitlementServiceException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.Entitlement;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicyUtility
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccessPolicyUtility</code> is a utility class related to OIM Access Policy
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicyUtility {
  
  private static final String className = AccessPolicyUtility.class.getName();
  private static final Logger logger = Logger.getLogger(className);
  
  private AccessPolicyService         accessPolicyService;
  private EntitlementService          entitlementService;
  private ApplicationInstanceService  appInstanceService;
  private ProvisioningService         provService;
  
  
  private String ACCESS_POLICY_NAME = "name";
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Construct AccessPolicyUtility
   ** @param acessPolicyService
   ** @param provService
   ** @param appInstanceService
   ** @param entitlementService
   */
  public AccessPolicyUtility( AccessPolicyService acessPolicyService,
                              ProvisioningService         provService,
                              ApplicationInstanceService  appInstanceService,
                              EntitlementService entitlementService
                             ) {
    super();
    this.accessPolicyService = acessPolicyService;
    this.provService        = provService;
    this.appInstanceService = appInstanceService;
    this.entitlementService = entitlementService;
    
    
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   findAccessPolicy
  /**
   ** Find access policy based on the Access Policy Name
   ** @param policyName Access Policy name
   ** @param populateDefaultData if the default data should be populated. 
   **        true means it should populate the default data, false otherwise. 
   **        Only populate the default data when needed as populating the default 
   **        data might take some time depends on the number of attributes.
   ** @return an access policy that matches the access policy ID given, 
   **         null if there is no access policy with the given ID.
   ** @throws AccessPolicyServiceException if any other generic error occurs.
   **                                      The embedded exception indicates the root cause.
   */
  public AccessPolicy findAccessPolicy(String policyName, boolean populateDefaultData) throws AccessPolicyServiceException {
    
    String methodName = "findAccessPolicy";
    logger.entering(className, methodName);
    
    AccessPolicy accessPolicy = null;
    SearchCriteria sc = new SearchCriteria(ACCESS_POLICY_NAME,
                                           policyName,
                                           SearchCriteria.Operator.EQUAL);

    HashMap<String, Object> apConfigParams = new HashMap<String, Object>();
    List<AccessPolicy> acessPolicies = accessPolicyService.findAccessPolicies(sc, apConfigParams);
    if(acessPolicies != null && acessPolicies.size() == 1){

      if(populateDefaultData){
        accessPolicy= accessPolicyService.getAccessPolicy(acessPolicies.get(0).getEntityId(),true);
      }
      else{
        accessPolicy = acessPolicies.get(0);  
      }
    }
    else{
      logger.log(Level.WARNING, "AccessPolicie with name {0} can't be found in OIM", new Object[] { policyName});
    }
    if(acessPolicies != null && acessPolicies.size() > 1){
      logger.log(Level.WARNING, "Multiple AccessPolicies were returned for policy name {0}: {1}", new Object[] { policyName, acessPolicies });
    }
    
    logger.exiting(className, methodName);
    return accessPolicy;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccounts
  /**
   ** Check if Account is already provisioned on user, if not created account
   ** @param accessPolicyKey
   */
  public void createAccounts(String accessPolicyKey, String userKey) {
    
    String methodName = "createAccounts";
    logger.entering(className, methodName);
    
    try{
      AccessPolicy ap = this.accessPolicyService.getAccessPolicy(accessPolicyKey, true);
      if(ap != null){
        for(AccessPolicyElement ape: ap.getPolicyElements()){
          if(!ape.isDenial()){
            long appInstanceKey = ape.getApplicationInstanceID();
            try {
              ApplicationInstance appInst = appInstanceService.findApplicationInstanceByKey(appInstanceKey);
              logger.log(Level.FINE, "Application Instance: {0}", new Object[] { appInst });
              
              // Check if the application instance is not already provisioned
              if (!provService.isApplicationInstanceProvisionedToUser(userKey,appInst)) {
                
                // Get parent data from AccessPolicy
                DefaultData dd = ape.getDefaultData();
                Map<String, Object> parentData = new HashMap<String, Object>();
                List<Record> records = dd.getData();
                for (Record record : records) {
                  parentData.put(record.getAttributeName(), record.getAttributeValue());
                }
                logger.log(Level.FINEST, "AccessPolicie with Key {0} have defined folowing parent data: {1}",
                           new Object[] { accessPolicyKey, parentData });
                
                // Get information required provisioning resource account
                Long resourceFormKey = appInst.getAccountForm().getFormKey(); // Get Process Form Key (SDK_KEY)
                logger.log(Level.FINE, "Resource Process Form Key: {0}", new Object[] { resourceFormKey });
                String udTablePrimaryKey = null;

                // Construct-Stage Resource Account
                AccountData accountData = new AccountData(String.valueOf(resourceFormKey), udTablePrimaryKey, parentData);
                Account resAccount = new Account(appInst, accountData);

                // Provision resource account to user
                Long accountId = provService.provision(userKey, resAccount); // Account Key = OIU_KEY
                logger.log(Level.FINE, "Access Policy Key: {0} , Provisioning Account Id: {1}", new Object[] { accessPolicyKey, accountId });
              }
            } catch (ApplicationInstanceNotFoundException | GenericAppInstanceServiceException |
                     GenericProvisioningException | UserNotFoundException e) {
              logger.log(Level.WARNING, "Application instance key {0} can't be found on userKye {1}. Error: {2}", new Object[] { appInstanceKey,userKey,e} );
            }
          }
        }
      }
    }
    catch (AccessPolicyServiceException e) {
      logger.warning("Special Access Policy Key can't be found in OIM: '"+accessPolicyKey+"', error message: "+e);
    } 
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlements
  /**
   ** Get list of the entitlements defined on the access policy identified by policy key (pol_key)
   ** @param accessPolicyKey Access Policy Key (pol_key)
   ** @return List of the entitlemente defined by access policy
   */
  public List<Entitlement> getEntitlements(String accessPolicyKey) {
    String methodName = "getEntitlements";
    logger.entering(className, methodName);
    
    List<Entitlement> entitlements = new ArrayList<Entitlement>();
    
    try{
      AccessPolicy ap = this.accessPolicyService.getAccessPolicy(accessPolicyKey, true);
      if(ap != null){
        for(AccessPolicyElement ape: ap.getPolicyElements()){
          if(ape.isChildEntity()){
            for(ChildAttribute ca : ape.getDefaultData().getChildAttributes()){
              String entCode = ca.getAttributeValue();
              //System.out.println("EntitlementCode: "+entCode);
              Entitlement ent = null;
              try {
                ent = getEntitlement(entCode);
              } catch (GenericEntitlementServiceException e) {
                logger.warning("Entitlement can't be found in OIM: '"+entCode+"', error message: "+e);        
              }
              if(ent != null){
                entitlements.add(ent);
              }
              
            }
          }
        }
      }
    }
    catch (AccessPolicyServiceException e) {
      logger.warning("Special Access Policy Key can't be found in OIM: '"+accessPolicyKey+"', error message: "+e);
    } 
    logger.exiting(className, methodName);
    return entitlements;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlement
  /**
   ** Get entitlmenend based on the entitlement code
   ** @param ent_code Entitlement Code
   ** @return Entitlement identified by entitlement code
   ** @throws GenericEntitlementServiceException
   */
  public Entitlement getEntitlement(String ent_code) throws GenericEntitlementServiceException {

    String methodName = "getEntitlements";
    logger.entering(className, methodName);

    Entitlement ent = null;
    SearchCriteria criteria = new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute.ENTITLEMENT_CODE.getId(), 
                                                 ent_code,
                                                 SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> configParams = new HashMap<String, Object>();
    List<Entitlement> entitlements = entitlementService.findEntitlements(criteria, configParams);
    if (entitlements != null && entitlements.size() == 1) {
      ent = entitlements.get(0);
    }
    if (entitlements != null && entitlements.size() > 1) {
      logger.log(Level.WARNING, "Multiple Entitlements were returned for code {0}: {1}", new Object[] { ent_code, entitlements });
    }
    logger.log(Level.FINE, "Entitlement : {0}", new Object[] { ent });
    logger.exiting(className, methodName);
    
    return ent;
  }
  
  
  /*
  private boolean isAccountProvisionedToUser(long appInstanceKey, String userKey) throws ApplicationInstanceNotFoundException,
                                                                    GenericAppInstanceServiceException,
                                                                    UserNotFoundException,
                                                                    GenericProvisioningException {
    
    boolean provisione = false;
    ApplicationInstance appInst = appInstanceService.findApplicationInstanceByKey(appInstanceKey);
    
    boolean populateAcctData = false;
    SearchCriteria appInstCriteria = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.DISPLAY_NAME.getId(), 
                                                        appInst.getDisplayName(),
                                                        SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> acctConfigParams = new HashMap<String, Object>();
    List<Account> userAccounts =  this.provService.getAccountsProvisionedToUser(userKey, appInstCriteria, acctConfigParams, populateAcctData);
    logger.log(Level.FINE, "User accounts fetched: {0}", new Object[] { userAccounts });
    if(userAccounts != null && userAccounts.size()>0){
      for(Account account : userAccounts){
        String accountStatus = account.getAccountStatus();
        if(!accountStatus.equals(ProvisioningConstants.ObjectStatus.REVOKED.getId())){
          provisione=true;
          break;
        }
      }
    }
    
    return provisione;
  }
*/

}
