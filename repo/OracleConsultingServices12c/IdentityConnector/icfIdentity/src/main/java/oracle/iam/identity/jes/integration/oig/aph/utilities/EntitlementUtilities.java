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

    File        :   EntitlementUtilities.java

    Compiler    :   JDK 1.8

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implement the class
                    EntitlementUtilities.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.09.2024  TSebo    First release version
*/
package oracle.iam.identity.jes.integration.oig.aph.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.EntitlementAlreadyProvisionedException;
import oracle.iam.provisioning.exception.EntitlementNotFoundException;
import oracle.iam.provisioning.exception.EntitlementNotProvisionedException;
import oracle.iam.provisioning.exception.GenericEntitlementServiceException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.ImproperAccountStateException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.provisioning.vo.EntitlementInstance;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementUtilities
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>EntitlementUtilities</code> is a utility class related to OIM Entitlements
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EntitlementUtilities {
  // Logger
  private static final Logger logger = Logger.getLogger(EntitlementUtilities.class.getName());

  // OIM API Services
  private final ProvisioningService provServOps;
  private final UserManager userMgrOps;
  private final EntitlementService entServ;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Construct EntitlementUtilities
   ** @param provServOps
   ** @param userMgrOps
   ** @param entServ
   */
  public EntitlementUtilities(ProvisioningService provServOps, UserManager userMgrOps, EntitlementService entServ) {
    this.provServOps = provServOps;
    this.userMgrOps = userMgrOps;
    this.entServ = entServ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntitlementDefinition
  /**
   ** Get entitlements from the OIM environment.
   ** @param ent_code Entitlement code, it the name can be used * as wild character
   ** @throws GenericEntitlementServiceException
   */
  public void printEntitlementDefinition(String ent_code) throws GenericEntitlementServiceException {
    // Get all Entitlement Definitions
    SearchCriteria criteria = new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute.ENTITLEMENT_CODE.getId(), 
                                                 ent_code,
                                                 SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> configParams = new HashMap<String, Object>();
    List<Entitlement> entitlements = entServ.findEntitlements(criteria, configParams);
    logger.log(Level.FINE, "Entitlement List: {0}", new Object[] { entitlements });
    System.out.println(entitlements);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntitlementDefinition
  /**
   ** Get all the entitlements from the OIM environment.
   ** @throws GenericEntitlementServiceException
   */
  public void printEntitlementDefinition() throws GenericEntitlementServiceException {
    // Get all Entitlement Definitions
    printEntitlementDefinition("*");
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlement
  /**
   **
   ** @param ent_code
   ** @return
   ** @throws GenericEntitlementServiceException
   */
  public Entitlement getEntitlement(String ent_code) throws GenericEntitlementServiceException {

    Entitlement ent = null;
    SearchCriteria criteria = new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute.ENTITLEMENT_CODE.getId(), 
                                                 ent_code,
                                                 SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> configParams = new HashMap<String, Object>();
    List<Entitlement> entitlements = entServ.findEntitlements(criteria, configParams);
    if (entitlements != null && entitlements.size() == 1) {
      ent = entitlements.get(0);
    }
    if (entitlements != null && entitlements.size() > 1) {
      logger.log(Level.WARNING, "Multiple Entitlements were returned for code {0}: {1}", new Object[] { ent_code, entitlements });
    }
    logger.log(Level.FINE, "Entitlement : {0}", new Object[] { ent });
    return ent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printUserEntitlementInstances
  /**
   ** Print all user's entitlement instances.
   ** @param userLogin OIM.USR_LOGIN
   ** @throws GenericProvisioningException
   ** @throws UserNotFoundException
   */
  public void printUserEntitlementInstances(String userLogin) throws GenericProvisioningException,
                                                                     UserNotFoundException, NoSuchUserException,
                                                                     UserLookupException {
    // Get user's key
    String userKey = this.getUserKeyByUserLogin(userLogin);
    logger.log(Level.FINE, "User key: {0}", new Object[] { userKey });

    // Get user's entitlements
    List<EntitlementInstance> userEntitlements = this.provServOps.getEntitlementsForUser(userKey);

    // Iterate each entitlement and print to logs
    for (EntitlementInstance userEntitlement : userEntitlements) {
      Long accountId = userEntitlement.getAccountKey(); // OIU_KEY
      logger.log(Level.INFO, "Entitlement Instance: {0}, Account ID (OIU_KEY): {1}",
                 new Object[] { userEntitlement, accountId });
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grantEntitlementToUser
  /**
   ** Add an entitlement to a user. Entitlements are stored
   ** in a resource account in the form of child data.
   ** @param userKey                 OIM User Login (USR_KEY)
   ** @param appInstName               Application Instance Display Name
   ** @param entitlementCode           Entitlement Code (ENT_LIST.ENT_CODE)
   ** @param entitlementAttributes     Attributes on entitlement
   ** @throws NoSuchUserException
   ** @throws UserLookupException
   ** @throws UserNotFoundException
   ** @throws GenericProvisioningException
   ** @throws GenericEntitlementServiceException
   ** @throws AccountNotFoundException
   ** @throws ImproperAccountStateException
   ** @throws EntitlementNotFoundException
   ** @throws EntitlementAlreadyProvisionedException
   */
  public void grantEntitlementToUser(String userKey, 
                                     String appInstName, 
                                     String entitlementCode,
                                     Map<String, Object> entitlementAttributes) throws NoSuchUserException,
                                                                                           UserLookupException,
                                                                                           UserNotFoundException,
                                                                                           GenericProvisioningException,
                                                                                           GenericEntitlementServiceException,
                                                                                           AccountNotFoundException,
                                                                                           ImproperAccountStateException,
                                                                                           EntitlementNotFoundException,
                                                                                           EntitlementAlreadyProvisionedException {
  
    // Get user's account filtered by application instance display name
    boolean populateAcctData = false;
    SearchCriteria appInstCriteria = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.DISPLAY_NAME.getId(), 
                                                        appInstName,
                                                        SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> acctConfigParams = new HashMap<String, Object>();
    List<Account> userAccounts =  this.provServOps.getAccountsProvisionedToUser(userKey, appInstCriteria, acctConfigParams, populateAcctData);
    logger.log(Level.FINE, "User accounts fetched: {0}", new Object[] { userAccounts });

    // Get specific Entitlement Definitions
    SearchCriteria entDefCriteria = new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute.ENTITLEMENT_CODE.getId(),
                                                       entitlementCode,
                                                       SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> entConfigParams = new HashMap<String, Object>();
    List<Entitlement> entitlements = entServ.findEntitlements(entDefCriteria, entConfigParams);
    logger.log(Level.FINE, "Entitlement Definition Fetched: {0}", new Object[] { entitlements });

    // Ensure an entitlement can be added to a specific resource on a user
    if (userAccounts != null && !userAccounts.isEmpty() && entitlements != null && !entitlements.isEmpty()) {
      // Get the "Primary" application instance
      Account userAccount = null;
      for(Account account: userAccounts){
        if(Account.ACCOUNT_TYPE.Primary == account.getAccountType()){
          userAccount = account;
          break;
        }
      }
      if(userAccount != null){
        String accountKey = userAccount.getAccountID(); // OIU_KEY
        logger.log(Level.FINE, "Add entitlement to account: Account Key = {0}", new Object[] { accountKey });
  
        // Get first entitlement definition
        Entitlement entitlement = entitlements.get(0);
        logger.log(Level.FINE, "Entitlement Definition: {0}", new Object[] { entitlement });
  
        // Instantiate Entitlement Instance Object
        EntitlementInstance grantEntInst = new EntitlementInstance();
  
        // Set required fields to grant entitlement
        grantEntInst.setEntitlement(entitlement); // **
        grantEntInst.setAccountKey(Long.parseLong(accountKey)); // ** OIU_KEY
  
        // Set attributes on entitlement if any
        grantEntInst.setChildFormValues(entitlementAttributes);
        
        // Add entitlement for user
        this.provServOps.grantEntitlement(grantEntInst);
      }
      else{
        logger.log(Level.WARNING, "None of the appInstances {0} is marked as primary on user key {1}", new Object[] { appInstName,userKey  });
      }
    }

    else {
      logger.log(Level.FINE, "Did not grant entitlement to user.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlementInstance
  /**
   ** Return entitlement instance on user based on the entitlement value
   ** @param userKey         Login of OIM user   (OIM.USR_KEY)
   ** @param entitlementName   Entitlement to remove from user (OIM.ENT_LIST.ENT_DISPLAY_NAME)
   */
  public EntitlementInstance getEntitlementInstance(String userKey, String entitlementName) throws UserNotFoundException,
                                                                                   GenericProvisioningException {
    EntitlementInstance entInst = null;
    // Get specific entitlement from user filtered by Entitlement Display Name (ENT_LIST.ENT_DISPLAY_NAME)
    SearchCriteria criteria =
      new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute
                                                                      .ENTITLEMENT_DISPLAYNAME
                                                                      .getId(), entitlementName,
                                                                       SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> configParams = new HashMap<String, Object>();
    List<EntitlementInstance> userEntitlementInsts =  this.provServOps.getEntitlementsForUser(userKey, criteria, configParams);
    logger.log(Level.FINE, "Entitlement Instances Fetched: {0}", new Object[] { userEntitlementInsts });
    
    // Check if there is at least one entitlement
    if (userEntitlementInsts != null && !userEntitlementInsts.isEmpty()) {
        // Get first item in user's entitlement list
        entInst = userEntitlementInsts.get(0);
    }
    else {
        logger.log(Level.INFO, "No such entitlement instance found: {0}", new Object[] { entitlementName });
    }
    
    return entInst;
    
  }
  

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeEntitlementFromUser
  /**
   ** Removes an entitlement instance form a user based on the display name of the entitlement.
   ** @param userLogin         Login of OIM user   (OIM.USR_LOGIN)
   ** @param entitlementName   Entitlement to remove from user (OIM.ENT_LIST.ENT_DISPLAY_NAME)
   ** @throws UserNotFoundException
   ** @throws GenericProvisioningException
   ** @throws AccountNotFoundException
   ** @throws EntitlementNotProvisionedException
   ** @throws NoSuchUserException
   ** @throws UserLookupException
   */
  public void revokeEntitlementFromUser(String userLogin, String entitlementName) throws UserNotFoundException,
                                                                                         GenericProvisioningException,
                                                                                         AccountNotFoundException,
                                                                                         EntitlementNotProvisionedException,
                                                                                         NoSuchUserException,
                                                                                         UserLookupException {
    // Get user's key
    String userKey = this.getUserKeyByUserLogin(userLogin);
    logger.log(Level.FINE, "User key: {0}", new Object[] { userKey });

    // Get specific entitlement from user filtered by Entitlement Display Name (ENT_LIST.ENT_DISPLAY_NAME)
    SearchCriteria criteria =
      new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute
                                                                      .ENTITLEMENT_DISPLAYNAME
                                                                      .getId(), entitlementName,
                         SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> configParams = new HashMap<String, Object>();
    List<EntitlementInstance> userEntitlementInsts =
      this.provServOps.getEntitlementsForUser(userKey, criteria, configParams);
    logger.log(Level.FINE, "Entitlement Instances Fetched: {0}", new Object[] { userEntitlementInsts });

    // Check if there is at least one entitlement
    if (userEntitlementInsts != null && !userEntitlementInsts.isEmpty()) {
      // Get first item in user's entitlement list
      EntitlementInstance revokeEntInst = userEntitlementInsts.get(0);

      // Remove entitlement from user
      this.provServOps.revokeEntitlement(revokeEntInst);
      logger.log(Level.FINE, "Removed Entitlement Instance: {0}", new Object[] { revokeEntInst });
    }

    else {
      logger.log(Level.FINE, "No such entitlement instance to remove: {0}", new Object[] { entitlementName });
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateEntitlementInstanceOnUser
  /**
   ** Update an entitlement on a user.
   ** @param userLogin                 User Login
   ** @param entitlementName           Display name of entitlement
   ** @param entitlementAttributes     Attributes to update on entitlement
   ** @throws NoSuchUserException
   ** @throws UserLookupException
   ** @throws UserNotFoundException
   ** @throws GenericProvisioningException
   ** @throws AccountNotFoundException
   ** @throws EntitlementNotFoundException
   */
  public void updateEntitlementInstanceOnUser(String userLogin, String entitlementName,
                                              HashMap<String, Object> entitlementAttributes) throws NoSuchUserException,
                                                                                                    UserLookupException,
                                                                                                    UserNotFoundException,
                                                                                                    GenericProvisioningException,
                                                                                                    AccountNotFoundException,
                                                                                                    EntitlementNotFoundException {
    // Get user's key
    String userKey = this.getUserKeyByUserLogin(userLogin);
    logger.log(Level.FINE, "User key: {0}", new Object[] { userKey });

    // Get specific entitlement from user filtered by Entitlement Display Name (ENT_LIST.ENT_DISPLAY_NAME)
    SearchCriteria criteria =
      new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute
                                                                      .ENTITLEMENT_DISPLAYNAME
                                                                      .getId(), entitlementName,
                         SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> configParams = new HashMap<String, Object>();
    List<EntitlementInstance> userEntitlementInsts =
      this.provServOps.getEntitlementsForUser(userKey, criteria, configParams);
    logger.log(Level.FINE, "Entitlement Instances Fetched: {0}", new Object[] { userEntitlementInsts });

    // Check if there is at least one entitlement
    if (userEntitlementInsts != null && !userEntitlementInsts.isEmpty()) {
      // Get first item in user's entitlement list
      EntitlementInstance updateEntInst = userEntitlementInsts.get(0);

      // Stage updates for entitlement
      updateEntInst.setChildFormValues(entitlementAttributes);

      // Update entitlement instance on user
      this.provServOps.updateEntitlement(updateEntInst);
      logger.log(Level.FINE, "Update Entitlement Instance: {0}", new Object[] { updateEntInst });
    }

    else {
      logger.log(Level.FINE, "No such entitlement instance to update: {0}", new Object[] { entitlementName });
    }

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserKeyByUserLogin
  /**
   ** Get the OIM User's USR_KEY
   ** @param   userLogin     OIM.User Login (USR_LOGIN)
   ** @return value of USR_KEY
   ** @throws NoSuchUserException
   ** @throws UserLookupException
   */
  private String getUserKeyByUserLogin(String userLogin) throws NoSuchUserException, UserLookupException {
    boolean userLoginUsed = true;
    HashSet<String> attrsToFetch = new HashSet<String>();
    attrsToFetch.add(UserManagerConstants.AttributeName.USER_KEY.getId());
    attrsToFetch.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
    User user = userMgrOps.getDetails(userLogin, attrsToFetch, userLoginUsed);
    logger.log(Level.FINE, "User Details: {0}", new Object[] { user });
    return user.getEntityId();
  }
}