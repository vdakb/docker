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

    File        :   ProvisioningUtils.java

    Compiler    :   JDK 1.8

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implement the class
                    ProvisioningUtils.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.09.2024  TSebo    First release version
*/
package oracle.iam.identity.jes.integration.oig.aph.utilities;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.ChildTableRecord;
import oracle.iam.provisioning.vo.EntitlementInstance;

////////////////////////////////////////////////////////////////////////////////
// class LookupUtils
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ProvisioningUtils</code> is a utility class related to OIM Provisioning
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProvisioningUtils {
  
  
  private static final String className = ProvisioningUtils.class.getName(); 
  // Logger
  private static final Logger logger = Logger.getLogger(className);

  // Get OIM API services
  private final UserManager usrMgr;
  private final ApplicationInstanceService appInstService;
  private final ProvisioningService provService;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Construct ProvisioningUtils
   ** @param provService OIM Provisioning Service
   ** @param appInstService OIM ApplicationInstanceService
   ** @param usrMgr OIM UserManager Service
   */
  public ProvisioningUtils(ProvisioningService provService, 
                           ApplicationInstanceService appInstService,
                           UserManager usrMgr ) {
    this.provService = provService;
    this.appInstService = appInstService;
    this.usrMgr = usrMgr;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisionAccountToUser
  /**
   ** Provision a resource account to a user.
   ** @param userKey     OIM User Login (USR.USR_LOGIN)
   ** @param appInstName   Name of application instance (APP_INSTANCE.APP_INSTANCE_NAME)
   ** @param parentData    Data to populate the parent process form
   ** @param childData     Data to populate the child process form(s)
   ** @return Account Id (OIU_KEY)
   ** @throws AccessDeniedException
   ** @throws NoSuchUserException
   ** @throws UserLookupException
   ** @throws ApplicationInstanceNotFoundException
   ** @throws GenericAppInstanceServiceException
   ** @throws UserNotFoundException
   ** @throws GenericProvisioningException
   */
  public Long provisionAccountToUser(String userKey, 
                                     String appInstName, 
                                     Map<String, Object> parentData,
                                     Map<String, ArrayList<ChildTableRecord>> childData) 
                                      throws  AccessDeniedException,
                                              NoSuchUserException,
                                              UserLookupException,
                                              ApplicationInstanceNotFoundException,
                                              GenericAppInstanceServiceException,
                                              UserNotFoundException,
                                              oracle.iam.platform.authopss.exception.AccessDeniedException,
                                              GenericProvisioningException {
    String methodName = "provisionAccountToUser";
    logger.entering(className, methodName);

    // Get application instance by name (APP_INSTANCE.APP_INSTANCE_NAME)
    ApplicationInstance appInst = appInstService.findApplicationInstanceByName(appInstName);
    logger.log(Level.FINE, "Application Instance: {0}", new Object[] { appInst });

    // Get information required provisioning resource account
    Long resourceFormKey = appInst.getAccountForm().getFormKey(); // Get Process Form Key (SDK_KEY)
    logger.log(Level.FINE, "Resource Process Form Key: {0}", new Object[] { resourceFormKey });
    String udTablePrimaryKey = null;

    // Construct-Stage Resource Account
    AccountData accountData = new AccountData(String.valueOf(resourceFormKey), udTablePrimaryKey, parentData);
    accountData.setChildData(childData);
    Account resAccount = new Account(appInst, accountData);

    // Provision resource account to user
    Long accountId = provService.provision(userKey, resAccount); // Account Key = OIU_KEY
    logger.log(Level.FINE, "Provisioning Account Id: {0}", new Object[] { accountId });

    logger.exiting(className, methodName);
    return accountId;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlementsForUser
  /**
   ** Return entitlements provisioned to user, fitlered based on the ahe application instance name.
   ** If the @param exlude is set to false only provisioned entitlements of the application instance are returned.
   ** If the @param exlude is set to true all entitlements are returned expect entitlements related to app instance definded
   ** in the @param appInstName.
   ** @param userKey
   ** @param appInstName
   ** @param exclude
   ** @return
   ** @throws UserNotFoundException
   ** @throws GenericProvisioningException
   */
  public List<EntitlementInstance> getEntitlementsForUser (String userKey, 
                                                           String appInstName,
                                                           boolean exclude) 
                                                          throws UserNotFoundException,
                                                          GenericProvisioningException {
    
    SearchCriteria.Operator operator = SearchCriteria.Operator.EQUAL;
    if(exclude){
      operator = SearchCriteria.Operator.NOT_EQUAL;
    }
    
    SearchCriteria sc = new SearchCriteria(ProvisioningConstants.EntitlementSearchAttribute.APPINST_NAME.getId(),
                                           appInstName,
                                           operator);
    
    return  provService.getEntitlementsForUser(userKey, sc, null);
                         
                         
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlementsForUser
  /**
   ** Return all entitlement on user related to the @param appInstName
   ** @param userKey
   ** @param appInstName
   ** @return
   ** @throws UserNotFoundException
   ** @throws GenericProvisioningException
   */
  public List<EntitlementInstance> getEntitlementsForUser (String userKey, 
                                                           String appInstName
                                                          ) 
                                                          throws UserNotFoundException,
                                                          GenericProvisioningException {
    return getEntitlementsForUser(userKey, appInstName,false);
  }


}
