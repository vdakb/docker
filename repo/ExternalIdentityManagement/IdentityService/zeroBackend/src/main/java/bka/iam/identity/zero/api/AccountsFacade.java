/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   ZeRo Backend

    File        :   AccountsFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    AccountsFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.api;

import bka.iam.identity.helper.Helper;
import bka.iam.identity.zero.model.Identity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.json.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.security.auth.login.LoginException;

import javax.sql.DataSource;

import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.catalog.api.CatalogService;
import oracle.iam.catalog.exception.CatalogException;
import oracle.iam.catalog.vo.Catalog;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.AdditionalAttributeEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.NamespaceEntity;
import oracle.iam.identity.igs.model.Risk;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMInternalClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.utils.vo.OIMType;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;
import oracle.iam.provisioning.exception.GenericEntitlementServiceException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.ChildTableRecord;
import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.provisioning.vo.EntitlementInstance;
import oracle.iam.provisioning.vo.FormField;
import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.request.api.RequestService;
import oracle.iam.request.exception.RequestServiceException;
import oracle.iam.request.vo.Request;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestSearchCriteria;
import oracle.iam.service.api.RequestAdapter;
import oracle.iam.service.api.RequestException;
import oracle.iam.service.api.RequestMessage;

////////////////////////////////////////////////////////////////////////////////
// class AccountsFacade
// ~~~~~ ~~~~~~~~~~~~~~
/**
 * The session facade to manage {@link bka.iam.identity.zero.model.SchemaAttribute} entity.
 *
 * @author adrien.farkas@oracle.com
 * @version 1.0.0.0
 * @since 1.0.0.0
 */
@Stateless(name = AccountsFacade.NAME)
public class AccountsFacade {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String  NAME   = "AccountsFacade";
  
  // Establish some reasonable defaults
  // Don't forger that SQL SELECT command indexes from 0!
  public static final int     START  = 1;
  public static final int     ITEMS  = 25;

  private static final String CLASS  = AccountsFacade.class.getName();
  private static final Logger LOGGER = Logger.create(CLASS);
  
  //////////////////////////////////////////////////////////////////////////////
  // non-static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  @EJB
  OIMEntitlementFacade entFacade;
  @EJB
  AppInstanceFacade appFacade;
  
  private DataSource        operationsDS     = null;

  private OIMInternalClient client           = new OIMInternalClient(new Hashtable<String, String>());

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2063532653579549662")
  private static final long  serialVersionUID = 6164211711658242785L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountsFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountsFacade() {
    // ensure inheritance
    super();

    // initialize instance
    final String method = "<init>";
    LOGGER.entering(CLASS, method);
    try {
      this.operationsDS = (DataSource)new InitialContext().lookup("jdbc/operationsDB");
    }
    catch (NamingException e) {
      LOGGER.throwing(CLASS, method, e);
    }
    LOGGER.exiting(CLASS, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Request to retrieve list of {@link String} objects representing account
   ** identifiers from an application instance identified by <code>name</code>.
   **
   ** @param appInstanceName           the name of the {@link ApplicationInstance}
   **                                  to lookup accounts for.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @param orgName                   the name of the {@link ApplicationInstance}
   **                                  to lookup accounts for.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @param userName                  the name administrator user performing the
   **                                  query. Used to employ OIM-defined
   **                                  restrictions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the list of {@link String} objects
   **                                  present on {@link ApplicationInstance}
   **                                  identified by <code>name</code>.
   **                                  <br>
   **                                  Possible object is {@link List} of
   **                                  {@link AccountEntity} objects.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public List<Identity> list(final String appInstanceName, final String orgName, final String userName)
    throws IllegalArgumentException {
    final String method = "list";
    LOGGER.entering(CLASS, method, "appInstanceName=", appInstanceName, "orgName=", orgName, "userName=", userName);
    
    List<Identity> entities = new ArrayList<>();
    if (appInstanceName == null || "".equals(appInstanceName)) {
      final StringBuffer message = new StringBuffer("Name of application must be supplied!");
      LOGGER.error(CLASS, method, message.toString());
      IllegalArgumentException e = new IllegalArgumentException(message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw e;
    }
    
    try {
      if (userName == null) {
        client.loginAsOIMInternal();
      } else {
        client.signatureLogin(userName);
      }
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    
    if (Helper.getIgnoredApplications(client).contains(appInstanceName)) {
      LOGGER.trace(CLASS, method, "Ignoring app " + appInstanceName + " as per Lookup configuration");
      return(entities);
    }
    
    // Validate the application instance exists
    Helper.getApplicationInstance(this.client, appInstanceName);

    // preparing variables used
    Map<String, String> userIdMap = new HashMap<>();
    Map<String, List<bka.iam.identity.zero.model.Account>> userAccountsMap = new HashMap<>();
      
    if (orgName != null && !"".equals(orgName)) {
      try {
        // first, identity the organization and retrievs its ID
        OrganizationManager orgService = client.getService(OrganizationManager.class);
        Organization org = orgService.getDetails(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), orgName, null);
        List<User> identityList = orgService.getOrganizationMembers(org.getEntityId(), null, null, null);
        for (User u : identityList) {
          LOGGER.debug(CLASS, method, "Processing user:" + u);
          userAccountsMap.put(u.getLogin(), new ArrayList<bka.iam.identity.zero.model.Account>());
          userIdMap.put(u.getId(), u.getLogin());
        }
      } catch (OrganizationManagerException e) {
        final StringBuilder message = new StringBuilder("Organization Manager exception occurred");
        LOGGER.error(CLASS, method, message.toString());
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message.toString(), e);
      } catch (SearchKeyNotUniqueException e) {
        // should not occur since we're searching based on the "name" attribute that is unique by definition, but still
        final StringBuilder message = new StringBuilder("Organization Name not unique");
        LOGGER.error(CLASS, method, message.toString());
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message.toString(), e);
      }     
      LOGGER.debug(CLASS, method, "Dump of userIdMap:" + userIdMap);
    }
    
    try {  
      // And now on to accout listing
      ProvisioningService provService = client.getService(ProvisioningService.class);
      List<Account> accounts =
        provService.getProvisionedAccountsForAppInstance(appInstanceName,
          new SearchCriteria(
            new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), "Provisioning", SearchCriteria.Operator.EQUAL),
            new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), "Provisioned", SearchCriteria.Operator.EQUAL),
            SearchCriteria.Operator.OR
          ),
          new HashMap<String, Object>());

      LOGGER.debug(CLASS, method, "Retrieved account list: " + accounts);
      // let's this one prepared even if we won't use it if organization is supplied
      UserManager userMgrService = client.getService(UserManager.class);
      if (accounts != null) {
        for (Account account : accounts) {
          Account detailedAccount = provService.getAccountDetails(Long.valueOf(account.getAccountID()));
          if (orgName != null && !"".equals(orgName)) {
            if (userIdMap.containsKey(detailedAccount.getUserKey())) {
              // Get existing accounts for a user (safe to do since it must exist)
              List<bka.iam.identity.zero.model.Account> currAccounts = userAccountsMap.remove(userIdMap.get(detailedAccount.getUserKey()));
              currAccounts.add(new bka.iam.identity.zero.model.Account(detailedAccount.getAccountDescriptiveField(), detailedAccount.getAccountType()));
              userAccountsMap.put(userIdMap.get(detailedAccount.getUserKey()), currAccounts);
            } else {
              LOGGER.debug(CLASS, method, "Account " + detailedAccount.getAccountDescriptiveField() + " does not \"belong\" to organization " + orgName + ", skipping.");
            }
          } else {
            // if no organization is supplied simply return all users and their corresponding owners
            User owner = userMgrService.getDetails(UserManagerConstants.AttributeName.USER_KEY.getId(), detailedAccount.getUserKey(), null);
            // in this case userIdMap and userAccountsMap are initially empty, we need to actually start building it
            List<bka.iam.identity.zero.model.Account> currAccounts;
            if (userAccountsMap.containsKey(owner.getLogin())) {
              currAccounts = userAccountsMap.remove(owner.getLogin());
            } else {
              currAccounts = new ArrayList<>();
            }
            currAccounts.add(new bka.iam.identity.zero.model.Account(detailedAccount.getAccountDescriptiveField(), detailedAccount.getAccountType()));
            userAccountsMap.put(owner.getLogin(), currAccounts);
          }
        }
      }
    } catch (GenericProvisioningException e) {
      final StringBuilder message = new StringBuilder("Generic Provisioning exception occurred");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (AccountNotFoundException e) {
      // should not occur since we're retrieving details about account we just retrieved, but still
      final StringBuilder message = new StringBuilder("Account not found");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (NoSuchUserException e) {
      // should not occur since we're retrieving details about user we just found reference to, but still
      final StringBuilder message = new StringBuilder("User not found");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (UserLookupException e) {
      final StringBuilder message = new StringBuilder("User Lookup exception occurred");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (SearchKeyNotUniqueException e) {
      // should not occur since we're searching based on the "key" attribute that is unique by definition, but still
      final StringBuilder message = new StringBuilder("User Key not unique");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
    client.logout();
    
    // Finally, iterate through the userAccountsMap and create a simple list of Identity objects
    for (String key : userAccountsMap.keySet()) {
        entities.add(new Identity(key, userAccountsMap.get(key)));
    }
    
    LOGGER.exiting(CLASS, method);
    return entities;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Request to retrieve list of {@link String} objects representing account
   ** identifiers from an application instance identified by <code>name</code>.
   **
   ** @param appInstanceName           the name of the {@link ApplicationInstance}
   **                                  to lookup accounts for.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @param userName                  the name administrator user performing the
   **                                  query. Used to employ OIM-defined
   **                                  restrictions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @param  start                    page start.
   **                                  <br>
   **                                  Allowed object is {@link Integer}.
   **
   ** @param  requestedItems           requested paging number of items. Can be lower
   **                                  if there are less results than start + items.
   **                                  <br>
   **                                  Allowed object is {@link Integer}.
   **
   ** @return                          the list of {@link String} objects
   **                                  present on {@link ApplicationInstance}
   **                                  identified by <code>name</code>.
   **                                  <br>
   **                                  Possible object is {@link List} of
   **                                  {@link AccountEntity} objects.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public List<AccountEntity> list(final String appInstanceName, final String userName, int start, int requestedItems)
    throws IllegalArgumentException {
    final String method = "list";
    LOGGER.entering(CLASS, method, "appInstanceName=", appInstanceName,
                    "userName=", userName,
                    "start=", start, "requestedItems=", requestedItems);
    
    List<AccountEntity> result = new ArrayList<>();
    try {
      if (userName == null) {
        client.loginAsOIMInternal();
      } else {
        client.signatureLogin(userName);
      }
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    
    ApplicationInstance appInst = Helper.getApplicationInstance(this.client, appInstanceName);
    if (Helper.getIgnoredApplications(client).contains(appInstanceName) || appInst == null) {
      final StringBuilder message = new StringBuilder("Application Instance not found");
      LOGGER.error(CLASS, method, message.toString());
      throw new IllegalArgumentException(message.toString());
    }
    try {  
      // And now on to accout listing
      ProvisioningService provService = client.getService(ProvisioningService.class);
      
      // Prepare the paging criteria
      HashMap<String, Object> configParams = new HashMap<>();
      configParams.put(ApplicationInstance.STARTROW, start);
      configParams.put(ApplicationInstance.ENDROW, start + requestedItems - 1);
      LOGGER.debug(CLASS, method, "configParams map dump: " + configParams);

      List<Account> accounts = provService.getProvisionedAccountsForAppInstance(appInst.getApplicationInstanceName(),
        new SearchCriteria(
          new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), "Provisioning", SearchCriteria.Operator.EQUAL),
          new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), "Provisioned", SearchCriteria.Operator.EQUAL),
          SearchCriteria.Operator.OR
        ),
        configParams
      );
      LOGGER.debug(CLASS, method, "Retrieved account list: " + accounts);

      if (accounts != null) {
        for (Account account : accounts) {
          Account detailedAccount = provService.getAccountDetails(Long.valueOf(account.getAccountID()));
          LOGGER.debug(CLASS, method, "Retrieved account with details: " + detailedAccount);
          result.add(buildAccountEntity(detailedAccount));
          try {
            detailedAccount = provService.getAccountDetails(Long.valueOf(account.getAccountID()));
            LOGGER.debug(CLASS, method, "Retrieved account with details: " + detailedAccount);
            result.add(buildAccountEntity(detailedAccount));
          } catch (AccountNotFoundException e) {
            // should not occur since we're retrieving details about account we just retrieved, but still
            final StringBuilder message = new StringBuilder("Account not found");
            LOGGER.error(CLASS, method, message.toString());
            LOGGER.throwing(CLASS, method, e);
          } catch (oracle.iam.platform.authopss.exception.AccessDeniedException e) {
            LOGGER.debug(CLASS, method, "Permission denied while getting account details: " + account.getAccountDescriptiveField());
          }
        }
      }
    } catch (GenericProvisioningException e) {
      final StringBuilder message = new StringBuilder("Generic Provisioning exception occurred");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (AccountNotFoundException e) {
      // should not occur since we're retrieving details about account we just retrieved, but still
      final StringBuilder message = new StringBuilder("Account not found");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (oracle.iam.platform.authopss.exception.AccessDeniedException e) {
      final String message = "Permission denied";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw e;
    } finally {
      client.logout();
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listAccounts
  /**
   ** Request to retrieve list of {@link String} objects representing account
   ** identifiers from an application instance identified by <code>name</code>.
   **
   ** @param targetUserName            the name target user to retrieve list of
   **                                  accounts for.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @param userName                  the name administrator user performing the
   **                                  query. Used to employ OIM-defined
   **                                  restrictions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the list of {@link String} objects
   **                                  present on {@link ApplicationInstance}
   **                                  identified by <code>name</code>.
   **                                  <br>
   **                                  Possible object is {@link List} of
   **                                  {@link AccountEntity} objects.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public Map<String, List<AccountEntity>> listAccounts(final String targetUserName, final String userName)
    throws IllegalArgumentException {
    final String method = "list";
    LOGGER.entering(CLASS, method, "targetUserName=", targetUserName,
                    "userName=", userName);
    
    Map<String, List<AccountEntity>> mapResult = new HashMap<>();
    try {
      if (userName == null) {
        client.loginAsOIMInternal();
      } else {
        client.signatureLogin(userName);
      }
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    
    try {  
      // And now on to accout listing
      ProvisioningService provService = client.getService(ProvisioningService.class);
      
      User user = Helper.getUser(client, targetUserName, true);
      if (user == null) {
        
      } else {
        LOGGER.debug(CLASS, method, "User ID: " + user.getId());
        List<Account> accounts = provService.getAccountsProvisionedToUser(user.getId(), true);
        LOGGER.debug(CLASS, method, "Retrieved account list: " + accounts);
  
        if (accounts != null) {
          for (Account account : accounts) {
              Account detailedAccount = provService.getAccountDetails(Long.valueOf(account.getAccountID()));
              LOGGER.debug(CLASS, method, "Retrieved account with details: " + detailedAccount);
              String appInstanceName = account.getAppInstance().getApplicationInstanceName();
              if (!Helper.getIgnoredApplications(client).contains(appInstanceName)) {
                List<AccountEntity> appInstanceAccounts = mapResult.getOrDefault(appInstanceName, new ArrayList<AccountEntity>());
                appInstanceAccounts.add(buildAccountEntity(detailedAccount));
                mapResult.put(appInstanceName, appInstanceAccounts);
              } else {
                  LOGGER.trace(CLASS, method, "Ignoring app " + appInstanceName + " as per Lookup configuration");
              }
            }
          }
      }
    } catch (GenericProvisioningException e) {
      final StringBuilder message = new StringBuilder("Generic Provisioning exception occurred");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (UserNotFoundException e) {
      final StringBuilder message = new StringBuilder("User not found");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (AccountNotFoundException e) {
      // should not occur since we're retrieving details about account we just retrieved, but still
      final StringBuilder message = new StringBuilder("Account not found");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (oracle.iam.platform.authopss.exception.AccessDeniedException e) {
      final String message = "Permission denied";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw e;
    } finally {
      client.logout();
    }
    
    return mapResult;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processAccountEntity
  /**
   ** Used to parse {@link AccountEntity} object and issue the appropriate requests.
   **
   ** @param inputEntity               the input {@link AccountEntity} object.
   **                                  <br>
   **                                  Allowed object is {@link AccountEntity}.
   **                                  
   ** @param appInstanceName           the name of the associated application
   **                                  instance.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param userName                  the user name of the user asking for the
   **                                  processing, used for authorization checking.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the constructed {@link List} of {@link
   **                                  RequestMessage} objects containing the result
   **                                  of the requested operations.
   **                                  <br>
   **                                  Possible object is {@link List} of {@link
   **                                  RequestMessage}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public List<RequestMessage> processAccountEntity(final AccountEntity inputEntity, final String appInstanceName, String userName)
    throws IllegalArgumentException {
    final String method = "processAccountEntity";
    LOGGER.entering(CLASS, method, "inputEntity=", inputEntity, "appInstanceName=", appInstanceName, "userName=", userName);
    
    List<RequestMessage> resultMessages = new ArrayList<>();

    String accountId = inputEntity.id();
    try {
      if (userName == null) {
        client.loginAsOIMInternal();
      } else {
        client.signatureLogin(userName);
      }
    } catch (LoginException e) {
      final StringBuilder message = new StringBuilder("Login exception caught");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      resultMessages.add(new RequestMessage(RequestMessage.GENERAL));
      // This exception is fatal but still let's process it as a standard KO error not revealing internal details to the caller
      return resultMessages;
    }
    
    if (Helper.getIgnoredApplications(client).contains(appInstanceName)) {
      LOGGER.trace(CLASS, method, "Ignoring app " + appInstanceName + " as per Lookup configuration");
      resultMessages.add(new RequestMessage(RequestMessage.REQUEST_APPLICATION_NOTFOUND, appInstanceName));
      // If the application does not exist this is a KO - not much to do otherwise
      return resultMessages;
    }

    // Validate the application instance exists
    ApplicationInstance appInst = Helper.getApplicationInstance(this.client, appInstanceName);
    if (appInst == null) {
      final StringBuilder message = new StringBuilder("Application instance ").append(appInstanceName).append(" does not exist");
      LOGGER.debug(CLASS, method, message.toString());
      resultMessages.add(new RequestMessage(RequestMessage.REQUEST_APPLICATION_NOTFOUND, appInstanceName));
      // If the application does not exist this is a KO - not much to do otherwise
      return resultMessages;
    }

    // Identify the field for account identification
    String identifierFieldName = null;
    List<FormField> fields = appInst.getAccountForm().getFormFields();
    List<String> allowedAttributes = new ArrayList<>();
    for (FormField field : fields) {
//      allowedAttributes.add(field.getLabel());
      allowedAttributes.add(field.getName());
      if ("true".equals(field.getProperty("AccountName"))) {
//        identifierFieldName = field.getLabel();
        identifierFieldName = field.getName();
      }
    }

    if (identifierFieldName == null) {
      final StringBuilder message = new StringBuilder("Could not identify account identifying attribute for application ").append(appInstanceName);
      resultMessages.add(new RequestMessage(RequestMessage.NO_NAMING_ATTRIBUTE, appInstanceName));
      LOGGER.error(CLASS, method, message.toString());
    } else {
      final StringBuilder message = new StringBuilder("Identified account identifying attribute to be:").append(identifierFieldName);
      LOGGER.debug(CLASS, method, message.toString());
    }

    // Validate entity attributes against application instance form
    for (String key : inputEntity.attribute().keySet()) {
      if (!allowedAttributes.contains(key)) {
        final StringBuilder message = new StringBuilder("Attribute ").append(key).append(" referred in request does not exist for application");
        resultMessages.add(new RequestMessage(RequestMessage.ATTR_NOT_FOUND, key, appInstanceName));
        LOGGER.error(CLASS, method, message.toString());
      }
    }
    
    // Here we assume (by hard) that the account name to process and associated owning User have the same login
    User user = Helper.getUser(client, accountId, true);
    if (user == null) {
      final StringBuilder message = new StringBuilder("User ").append(accountId).append(" not found");
      resultMessages.add(new RequestMessage(RequestMessage.USER_NOT_FOUND, accountId));
      LOGGER.error(CLASS, method, message.toString());
      // If the user does not exist this is a KO - not much to do otherwise
      return resultMessages;
    }      

    Account accountToProcess = identifyAccount(user, appInst);
    LOGGER.debug(CLASS, method, "accountToProcess=" + accountToProcess);
    // If the action=create the account may not exist, otherwise the account must exist
    if (accountToProcess == null && !inputEntity.action().equals(AccountEntity.Action.create)) {
      LOGGER.debug(CLASS, method, "accountToProcess is null and the action is not create");
      final StringBuilder message = new StringBuilder("Could not identify account for user ").append(accountId);
      resultMessages.add(new RequestMessage(RequestMessage.ACCOUNT_NOT_IDENTIFIED, accountId, appInstanceName));
      LOGGER.error(CLASS, method, message.toString());
    } else if (accountToProcess != null && inputEntity.action().equals(AccountEntity.Action.create)) {
      final StringBuilder message = new StringBuilder("Account already created for user ").append(accountId);
      LOGGER.error(CLASS, method, message.toString());
      resultMessages.add(new RequestMessage(RequestMessage.ACCOUNT_ALREADY_EXISTS, accountId, appInstanceName));
    }
    
    // Another set of checks - do not allow enabling enabled and disabling disabled accounts
    // Technically, this is allowed but the request fails
    if (accountToProcess != null && inputEntity.action().equals(AccountEntity.Action.enable) && !"Disabled".equals(accountToProcess.getAccountStatus())) {
      final StringBuilder message = new StringBuilder("Account not disabled, cannot enable");
      LOGGER.error(CLASS, method, message.toString());
      resultMessages.add(new RequestMessage(RequestMessage.ACCOUNT_NOT_DISABLED, accountId, appInstanceName));
    } else if (accountToProcess != null && inputEntity.action().equals(AccountEntity.Action.disable) && "Disabled".equals(accountToProcess.getAccountStatus())) {
      final StringBuilder message = new StringBuilder("Account already disabled");
      LOGGER.error(CLASS, method, message.toString());
      resultMessages.add(new RequestMessage(RequestMessage.ACCOUNT_ALREADY_DISABLED, accountId, appInstanceName));
    }
    
    // Prepare the map of child forms keyed by child form name
    Map<String, FormInfo> childForms = new HashMap<>();
    for (FormInfo childForm : appInst.getChildForms()) {
      childForms.put(childForm.getName(), childForm);
    }
    LOGGER.debug(CLASS, method, "Child forms map: " + childForms);
    
    // Entitlement validation
    // This one needs to be higher, it'll be used later
    Map<String, Entitlement> entitlements = new HashMap<>();
    // Next, retrieve the existing entitlement assignments IDs
    Map<String, EntitlementInstance> entitlementInstances = getEntitlementInstances(user);
    Map<String, List<NamespaceEntity>> ns = inputEntity.namespace();
    // First check - verify that all assignments to be assigned are existing
    if (ns != null) {
      try {
        List<EntitlementEntity> allMentionedEntitlements = new ArrayList<>();
        for (String nsEntryKey : ns.keySet()) {
          LOGGER.debug(CLASS, method, "Entitlements to process in namespace " + nsEntryKey +":");
          if (!childForms.containsKey(nsEntryKey)) {
            LOGGER.debug(CLASS, method, "Namespace " + nsEntryKey + " is not valid for application");
            resultMessages.add(new RequestMessage(RequestMessage.NAMESPACE_UNKNOWN, nsEntryKey, appInstanceName));
            continue;
          } else {
            LOGGER.debug(CLASS, method, "Namespace " + nsEntryKey + " seems to be known");
          }
          List<FormField> childFormFieldsFields = childForms.get(nsEntryKey).getFormFields();
          LOGGER.debug(CLASS, method, "Child form fields: " + childFormFieldsFields);
          
          String entitlementFieldName = null;
          List<String> entitlementFieldNames = new ArrayList<>();
          for (FormField childFormField : childFormFieldsFields) {
//            entitlementFieldNames.add(childFormField.getLabel());
            entitlementFieldNames.add(childFormField.getName());
            if ("true".equals((String) childFormField.getProperty("Entitlement"))) {
//              entitlementFieldName = childFormField.getLabel();
              entitlementFieldName = childFormField.getName();
            }
          }
//          List<String> entitlementFieldNames =
//            childFormFieldsFields.stream()
//                                 .filter(c -> "true".equals((String) c.getProperty("Entitlement")))
//                                 .map(t -> t.getLabel())
//                                 .collect(Collectors.toList());
          LOGGER.debug(CLASS, method, "Entitlement field names: " + entitlementFieldNames);
          if (entitlementFieldNames.size() >= 1 && entitlementFieldName != null) {
//            entitlementFieldName = entitlementFieldNames.get(0);
            LOGGER.debug(CLASS, method, "Identified unique entitlement field name to be: " + entitlementFieldName);
          } else {
            LOGGER.debug(CLASS, method, "Could not uniquely identify entitlement field name for the namespace " + nsEntryKey);
            resultMessages.add(new RequestMessage(RequestMessage.NO_NAMESPACE_ENT_ATTRIBUTE, nsEntryKey));
            continue;
          }

          allMentionedEntitlements.addAll(inputEntity.toAssign(nsEntryKey));
          allMentionedEntitlements.addAll(inputEntity.toRevoke(nsEntryKey));
          allMentionedEntitlements.addAll(inputEntity.toModify(nsEntryKey));
          for (EntitlementEntity ent : allMentionedEntitlements) {
            Boolean entitlementValidationPassed = true;
            
            Map<String, Object> entAttrs = new HashMap<>(ent.attribute());
            for (AdditionalAttributeEntity additionalAttribute : ent.additionalAttributes()) {
              entAttrs.putAll(additionalAttribute.attribute());
            }
            LOGGER.debug(CLASS, method, "All entitlement attribute values: " + entAttrs);
            // Validation whether all the attributes supplied are known to OIM (child table column labels)
            for (Map.Entry entAttributeEntry : entAttrs.entrySet()) {
              if (!entitlementFieldNames.contains(entAttributeEntry.getKey())) {
                LOGGER.debug(CLASS, method, "Attribute " + entAttributeEntry.getKey() + " is not known in namespace " + nsEntryKey);
                entitlementValidationPassed = false;
                resultMessages.add(new RequestMessage(RequestMessage.NAMESPACE_ATTRIBUTE_UNKNOWN, entAttributeEntry.getKey(), nsEntryKey));
              } else {
                LOGGER.debug(CLASS, method, "Attribute " + entAttributeEntry.getKey() + " is known in namespace " + nsEntryKey);
              }
            }
            // Validate whether all required child table columns are filled in in the attributes part
            List<String> requiredFieldNames =
              childFormFieldsFields.stream()
                                   .filter(c -> "true".equals((String) c.getProperty("Required")))
//                                   .map(t -> t.getLabel())
                                   .map(t -> t.getName())
                                   .collect(Collectors.toList());
            LOGGER.debug(CLASS, method, "Required field names: " + requiredFieldNames);
            for (String requiredField : requiredFieldNames) {
              if (!entitlementFieldNames.contains(requiredField)) {
                LOGGER.debug(CLASS, method, "Required field " + requiredField + " is not present among the entitlement attributes");
                entitlementValidationPassed = false;
                resultMessages.add(new RequestMessage(RequestMessage.NO_REQUIRED_ATTRIBUTE, requiredField));
              } else {
                LOGGER.debug(CLASS, method, "Required field " + requiredField + " is present among the entitlemen attributes");
              }
            }
            LOGGER.debug(CLASS, method, "Looking for entitlement: " + ent.get(entitlementFieldName));
            if (entitlementValidationPassed) {
              List<Entitlement> ents = client
                .getService(EntitlementService.class)
                .findEntitlements(new SearchCriteria(ProvisioningConstants.EntitlementInstanceSearchAttribute.ENTITLEMENT_VALUE.getId(),
                                                     (String) ent.get(entitlementFieldName),
                                                     SearchCriteria.Operator.EQUAL),
                                  new HashMap<String, Object>());
              LOGGER.debug(CLASS, method, "Entitlements found: " + ents);
              if (ents.size() == 1) {
                LOGGER.debug(CLASS, method, "Found exactly one, that's probably the one: " + ents);
                entitlements.put(ents.get(0).getEntitlementValue(), ents.get(0));
                // But still check the entitlement is not yet assigned if the action is not "revoke"
                if (EntitlementEntity.Action.assign.equals(ent.action()) && entitlementInstances.containsKey(ents.get(0).getEntitlementValue())) {
                  resultMessages.add(new RequestMessage(RequestMessage.ENTITLEMENT_ALREADY_ASSIGNED, ents.get(0).getEntitlementValue(), accountId));
                }
              } else {
                LOGGER.debug(CLASS, method, "Entitlement " + entAttrs.get(entitlementFieldName) +
                                               " could not be uniquely identified, number of returned entitlements " + ents.size());
                resultMessages.add(new RequestMessage(RequestMessage.ENTITLEMENT_NOT_UNIQUE, entAttrs.get(entitlementFieldName)));
              }
            }
          }
          // ..and finally verify all the assignments to modify/revoke are present in the list
          LOGGER.debug(CLASS, method, "Entitlements to modify/revoke in namespace " + nsEntryKey +":");
          List<EntitlementEntity> entitlementsToProcess = new ArrayList<>();
          entitlementsToProcess.addAll(inputEntity.toRevoke(nsEntryKey));
          entitlementsToProcess.addAll(inputEntity.toModify(nsEntryKey));
          for (EntitlementEntity ent : entitlementsToProcess) {
//            List<AdditionalAttributeEntity> additionalAttributes = ent.additionalAttributes();
//            for (AdditionalAttributeEntity additionalAttribute : additionalAttributes) {
//              String entValue = (String) additionalAttribute.attribute().get(entitlementFieldName);
              String entValue = (String) ent.attribute().get(entitlementFieldName);
              LOGGER.debug(CLASS, method, "Verifying entitlement " + entValue);
              if ("".equals(entValue) || !entitlementInstances.containsKey(entValue)) {
                LOGGER.debug(CLASS, method, "Entitlement not assigned or not correct: " + entValue);
                resultMessages.add(new RequestMessage(RequestMessage.ENTITLEMENT_NOT_ASSIGNED, entValue, accountId));
              } else {
                // If it does exist and is assigned let's make sure the entitlement revoke request is not pending
                LOGGER.debug(CLASS, method, "Testing if the entitlement " + ent + " revoke request is pending");
                try {
//                  EntitlementInstance entInst = entitlementInstances.get(additionalAttribute.get(additionalAttribute.keySet().toArray()[0]));
                  EntitlementInstance entInst = entitlementInstances.get(ent.get(ent.keySet().toArray()[0]));
                  LOGGER.debug(CLASS, method, "Entitlement Instance retrieved: " + entInst);
                  RequestSearchCriteria searchCriteria = new RequestSearchCriteria();
                  searchCriteria.setConjunctionOp(RequestSearchCriteria.Operator.AND);
                  searchCriteria.addExpression(RequestConstants.REQUEST_BENEFICIARY_USERID, accountToProcess.getAccountDescriptiveField(), RequestSearchCriteria.Operator.EQUAL);
                  searchCriteria.addExpression(RequestConstants.ENTITLEMENT_KEY_REMOVE_OPERATION, entInst.getEntitlement().getEntitlementKey(), RequestSearchCriteria.Operator.EQUAL);
                  searchCriteria.addExpression(RequestConstants.REQUEST_STATUS, oracle.iam.request.vo.RequestStage.STAGE_AWAITING_APPROVAL, RequestSearchCriteria.Operator.EQUAL);
                  
                  Set<String> attrs = new HashSet<>();
                  attrs.add(RequestConstants.REQUEST_ID);
                  attrs.add(RequestConstants.REQUEST_STATUS);
                  
                  List<Request> reqList = client.getService(RequestService.class).search(searchCriteria, attrs, new HashMap<String,Object>());
                  LOGGER.debug(CLASS, method, "Number of existing requests: " + reqList.size());
                  if (reqList.size() > 0) {
                    Iterator<Request> it = reqList.iterator();
                    StringBuffer reqIds = new StringBuffer();
                    while (it.hasNext()) {
                      Request req = it.next();
                      LOGGER.debug(CLASS, method, "Found existing request: " + req.getRequestID());
                      LOGGER.debug(CLASS, method, "Found existing request status: " + req.getRequestStatus());
                      reqIds.append(req.getRequestID());
                      if (it.hasNext()) {
                        reqIds.append(", ");
                      }
                    }
                    resultMessages.add(new RequestMessage(RequestMessage.REQUEST_PENDING, reqIds.toString()));
                  }
                } catch (RequestServiceException e) {
                  final StringBuilder message = new StringBuilder("Request Exception occurred.");
                  LOGGER.error(CLASS, method, message.toString());
                  LOGGER.throwing(CLASS, method, e);
                  client.logout();
                  throw new IllegalArgumentException(message.toString(), e);
                }
              }
//            }
          }
        }
      } catch (GenericEntitlementServiceException e) {
        final StringBuilder message = new StringBuilder("Generic Entitlement Service Exception occurred");
        LOGGER.error(CLASS, method, message.toString());
        LOGGER.throwing(CLASS, method, e);
        client.logout();
        throw new IllegalArgumentException(message.toString(), e);      
      }
      // TODO maybe: Also check that the requested assignments are not yet present (i.e. not yet assigned)
    }

    if (resultMessages.size() == 0) {
      switch (inputEntity.action()) {
      case disable:
      case enable:
        resultMessages.add(disableEnableAccount(inputEntity, accountToProcess));
        break;
      case delete:
        resultMessages.add(revokeAccount(accountToProcess));
        break;
      case create:
      case modify:
        resultMessages.add(requestAccount(inputEntity, accountToProcess, user.getId(),
                                          Helper.getApplicationInstance(this.client, appInstanceName),
                                          entitlements, entitlementInstances));
        break;
      default:
        LOGGER.error(CLASS, method, "Action is:" + inputEntity.action());
        break;
      }
    }
    for (RequestMessage mes : resultMessages) {
      LOGGER.debug(CLASS, method, mes.toString());      
    }
    LOGGER.exiting(CLASS, method);
    return resultMessages;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAccountEntity
  /**
   ** Used to create {@link AccountEntity} object from {@link Account}.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param inputAccount              the input {@link Account} object.
   **                                  <br>
   **                                  Allowed object is {@link Account}.
   **                                  
   ** @return                          the constructed {@link AccountEntity} object.
   **                                  <br>
   **                                  Possible object is {@link AccountEntity}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private AccountEntity buildAccountEntity(final Account inputAccount)
    throws IllegalArgumentException {
    final String method = "buildAccountEntity";
    LOGGER.entering(CLASS, method, "account=", inputAccount);
    
    LOGGER.debug(CLASS, method, "Account ID: " + inputAccount.getAccountDescriptiveField());
    AccountEntity outputAccount = NamespaceEntity.account(inputAccount.getAccountDescriptiveField())
                                                 .status(inputAccount.getAccountStatus());
    outputAccount.type(inputAccount.getAccountType().name());
    List<String> childFormNames = new ArrayList<>();
    List<EntitlementInstance> entitlements = inputAccount.getEntitlementGrants();
    for (FormInfo f : inputAccount.getAppInstance().getChildForms()) {
      childFormNames.add(f.getName());
    }
    LOGGER.debug(CLASS, method, "Account entitlements: " + entitlements);
    Map<String, List<EntitlementEntity>> namespaces = new HashMap<>();
    for (EntitlementInstance entInst : entitlements) {
      String namespace = entInst.getChildFormName();
      Entitlement ent = entInst.getEntitlement();
      List<EntitlementEntity> entList = namespaces.getOrDefault(namespace, new ArrayList<>());
      Catalog catalogEntitlement = getCatalogObject(ent.getEntitlementKey(), OIMType.Entitlement);
      LOGGER.debug(CLASS, method, "Catalog object: " + catalogEntitlement);
      Integer riskLevel = catalogEntitlement.getItemRisk();
      LOGGER.debug(CLASS, method, "Entitlement risk level: " + riskLevel);
      EntitlementEntity entEntity = Entity.entitlement(EntitlementEntity.Action.modify,
                                                       Risk.from(riskLevel))
                                          .status(entInst.getStatus());
      AdditionalAttributeEntity additionalAttr = Entity.additionalAttribute();
      String entitlementAttribute = "";
      for (FormField childFormField : entInst.getEntitlement().getChildForm().getFormFields()) {
        if ("true".equals(childFormField.getProperty("Entitlement"))) {
          entitlementAttribute = childFormField.getName();
          break;
        }
      }
      for (Map.Entry<String, Object> entValue : entInst.getChildFormValues().entrySet()) {
        if (entValue.getKey().equals(entitlementAttribute)) {
          // Encode? Decode?
//          entEntity.put(ent.getChildForm().getFormField(entValue.getKey()).getName(),
//                        Helper.decodeLookupValue(client, entValue.getKey(), (String) entValue.getValue()));
          entEntity.put(ent.getChildForm().getFormField(entValue.getKey()).getName(), entValue.getValue());
        } else {
//          additionalAttr.put(ent.getChildForm().getFormField(entValue.getKey()).getLabel(), entValue.getValue());
          additionalAttr.put(ent.getChildForm().getFormField(entValue.getKey()).getName(), entValue.getValue());
        }
      }
      if (additionalAttr.size() > 0) {
        entEntity.addAdditionalAttribute(additionalAttr);
      }
      // Work-in-progress: Members fun
      
      entList.add(entEntity);
      namespaces.put(namespace, entList);
    }
    for (String namespace : namespaces.keySet()) {
      NamespaceEntity ns = Entity.namespace(namespace);
      ns.addAll(namespaces.get(namespace));
      outputAccount.namespace(ns);
    }
    
    if (inputAccount.getNormalizedData() != null) {
      for (Map.Entry<String, List<Object>> attribute : inputAccount.getNormalizedData().entrySet()) {
        String key = attribute.getKey();
        // Only emit the attribute if it's not one of the child table names, i.e. entitlements, these
        // are emitted separately
        if (!childFormNames.contains(key)) {
          // Dirty, I know
          if (attribute.getValue().size() > 0) {
            outputAccount.put(key, attribute.getValue().get(0));
          } else {
            outputAccount.put(key, null);
          }
        }
      }
    }
    outputAccount.action(AccountEntity.Action.modify);
//    dumpJson(Schema.marshalAccount(outputAccount));
    LOGGER.exiting(CLASS, method, outputAccount);
    return outputAccount;
  }
    
  //////////////////////////////////////////////////////////////////////////////
  // Method:   listUserAccounts
  /**
   ** Used to return {@link List} of {@link Account} objects owned by the user in the particular
   ** {@link ApplicationInstance}.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param userId                    the {@link String} representation of user ID
   **                                  identifying the user to retrieve the accounts for.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param appInstance               the input {@link ApplicationInstance} object identifying
   **                                  tthe application instance to return accounts from.
   **                                  <br>
   **                                  Allowed object is {@link ApplicationInstance}.
   **                                  
   ** @return                          the retrieved {@link List} of {@link Account} objects
   **                                  provisioned for the user in the particular application.
   **                                  <br>
   **                                  Possible object is {@link List} of {@link Account}s.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private List<Account> listUserAccounts(String userId, ApplicationInstance appInstance)
    throws IllegalArgumentException {
    final String method = "listUserAccounts";
    LOGGER.entering(CLASS, method, "userId=", userId, "appInstance=", appInstance);
    try {
      ProvisioningService provService = client.getService(ProvisioningService.class);
      List<Account> accList = provService.getUserAccountDetailsInApplicationInstance(userId, appInstance.getApplicationInstanceKey(), true);
      final StringBuilder message = new StringBuilder("Found ").append(accList.size()).append(" accounts owner ID ").append(userId);
      LOGGER.debug(CLASS, method, message.toString());
      LOGGER.exiting(CLASS, method, accList);
      return accList;
    } catch (UserNotFoundException e) {
      // May not happen, we already retrieved the used before
      final StringBuilder message = new StringBuilder("User with ID ").append(userId).append(" not found");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (ApplicationInstanceNotFoundException e) {
      final StringBuilder message = new StringBuilder("Application ").append(appInstance.getApplicationInstanceName()).append(" not found");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (GenericProvisioningException e) {
      final StringBuilder message = new StringBuilder("Generic Provisioning exception occurred: ");
      if (e.getCause() != null) {
        message.append(e.getCause().getMessage());
      } else {
        message.append(e.getMessage());
      }
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserKey
  /**
   ** Used to retrieve {@link String} user key from {@link String} user login.
   **
   ** @param userName                  the {@link String} representation of user.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the retrieved {@link String} key for User
   **                                  identified by the input user name or null
   **                                  when the user is not found.
   **                                  <br>
   **                                  Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public String getUserKey(String userName)
    throws IllegalArgumentException {
    final String method = "getUserKey";
    LOGGER.entering(CLASS, method, "userName=", userName);

    try {
      client.loginAsOIMInternal();
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    User user = Helper.getUser(client, userName, true);
    LOGGER.trace(CLASS, method, "Retrieved user: " + user);
    client.logout();

    if (user != null) {
      return user.getId();
    } else {
      return null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableEnableAccount
  /**
   ** Used to construct and launch request to disable or enable an account.
   **
   ** @param entity                    the input {@link AccountEntity} object describing the
   **                                  activity.
   **                                  <br>
   **                                  Allowed object is {@link AccountEntity}.
   **                                  
   ** @param account                   the input {@link Account} object describing the
   **                                  activity.
   **                                  <br>
   **                                  Allowed object is {@link Account}.
   **                                  
   ** @return                          the retrieved {@link List} of {@link Account} objects
   **                                  provisioned for the user in the particular application.
   **                                  <br>
   **                                  Possible object is {@link List} of {@link Account}s.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private RequestMessage disableEnableAccount(AccountEntity entity, Account account)
    throws IllegalArgumentException {
    final String method = "requestAccount";
    LOGGER.entering(CLASS, method, "entity=", entity, "account=", account);
    
    RequestAdapter ad = RequestAdapter.build(client);
    try {
      ad.prepare(account.getUserKey());
      
      RequestBeneficiaryEntity rbe = new RequestBeneficiaryEntity();
      rbe.setRequestEntityType(OIMType.ApplicationInstance);
      rbe.setEntitySubType(account.getAppInstance().getApplicationInstanceName());
      rbe.setEntityKey(account.getAccountID());
      rbe.setOperation(entity.action().equals(AccountEntity.Action.disable) ?
                       RequestConstants.MODEL_DISABLE_ACCOUNT_OPERATION :
                       RequestConstants.MODEL_ENABLE_ACCOUNT_OPERATION);

      String submitResult = ad.splitSubmit(CollectionUtility.list(rbe));

      final StringBuilder message = new StringBuilder("Request ").append(submitResult).append(" for account ");
      message.append(entity.action().equals("disable") ? "disabling" : "enabling");
      message.append(" submited.");
      LOGGER.exiting(CLASS, method, message.toString());
      return new RequestMessage(RequestMessage.OK, submitResult);

    } catch (RequestException e) {
      final StringBuilder message = new StringBuilder("Request Exception occurred.");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestAccount
  /**
   ** Used to perform account requests - create and modify operations.
   **
   ** @param entity                    the input {@link AccountEntity} object identifying
   **                                  the account to process.
   **                                  <br>
   **                                  Allowed object is {@link AccountEntity}.
   **                                  
   ** @param account                   the input {@link Account} object identifying
   **                                  the corresponding OIM Account object.
   **                                  <br>
   **                                  Allowed object is {@link Account}.
   **                                  
   ** @param entitlements              the {@link Map} of {@link Entitlement}s objects keyed by
   **                                  the entitlement name (also known as "value" in OIM).
   **                                  <br>
   **                                  Allowed object is {@link Map} of {@link Entitlement}s.
   **                                  
   ** @param entitlementInstances      the {@link Map} of {@link EntitlementInstance}s objects keyed by
   **                                  the entitlement name (also known as "value" in OIM) representing
   **                                  entitlements assigned to account.
   **                                  <br>
   **                                  Allowed object is {@link Map} of {@link EntitlementInstance}s.
   **                                  
   ** @return                          the {@link RequestMessage} object containing request message.
   **                                  <br>
   **                                  Possible object is {@link RequestMessage}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private RequestMessage requestAccount(AccountEntity entity, Account account, String userKey,
                                        ApplicationInstance appInstance,
                                        Map<String, Entitlement> entitlements,
                                        Map<String, EntitlementInstance> entitlementInstances)
    throws IllegalArgumentException {
    final String method = "requestAccount";
    LOGGER.entering(CLASS, method, "entity=", entity, "userKey=", userKey,
                    "account=", account, "appInstance=", appInstance,
                    "entitlements=", entitlements,
                    "entitlementInstances=", entitlementInstances);
    
    RequestAdapter ad = RequestAdapter.build(client);
    try {
      ad.prepare(userKey);
      List<RequestBeneficiaryEntity> rbeList = new ArrayList<>();
      
      // Account modification first (includes entitlement request, this can be done using attributes)
      RequestBeneficiaryEntity rbeAccount = new RequestBeneficiaryEntity();
      rbeAccount.setRequestEntityType(OIMType.ApplicationInstance);
      rbeAccount.setEntitySubType(appInstance.getApplicationInstanceName());

      rbeAccount.setEntityKey(account == null ? Long.toString(appInstance.getApplicationInstanceKey()) : account.getAccountID());
      rbeAccount.setOperation(account == null ? RequestConstants.MODEL_PROVISION_APPLICATION_INSTANCE_OPERATION : RequestConstants.MODEL_MODIFY_ACCOUNT_OPERATION);
      rbeList.add(rbeAccount);

      final List<RequestBeneficiaryEntityAttribute> attributes = new ArrayList<RequestBeneficiaryEntityAttribute>();
      for (Map.Entry<String, Object> attributeEntry : entity.attribute().entrySet()) {
        LOGGER.debug(CLASS, method, "Request attribute: " + attributeEntry);
        Object value =  attributeEntry.getValue();
        String valueType = "String";
        if (value instanceof Long) {
          valueType = "Long";
        } else if (value instanceof Short) {
          valueType = "Short";
        } else if (value instanceof Double) {
          valueType = "Double";
        } else if (value instanceof Short) {
          valueType = "Short";
        } else if (value instanceof Integer) {
          valueType = "Integer";
        } else if (value instanceof Boolean) {
          valueType = "Boolean";
        }
        attributes.add(RequestAdapter.createAttribute(attributeEntry.getKey(), value.toString(), valueType));
      }
      // Entitlement assignments are added here
      attributes.addAll(prepareAccountAssignments(account, entity, entitlements));
      
      // Only submit the request if either entitlements are assigned or attribute(s) is/are changed
      if (attributes.size() > 0) {
        rbeAccount.setEntityData(attributes);
      }

    /*
      // Process entitlement revoke as separate requests
      Map<String, List<NamespaceEntity>> ns = entity.namespace();
      if (ns != null) {
        for (String nsEntryKey : ns.keySet()) {
          // Not here, done as part of account management, no separate request here
//          LOGGER.debug(CLASS, method, "Entitlements to assign in namespace " + nsEntryKey +":");
//          for (EntitlementEntity ent : entity.toAssign(nsEntryKey)) {
//                    final RequestBeneficiaryEntityAttribute  parent = new RequestBeneficiaryEntityAttribute(permission.id().tag, "", RequestBeneficiaryEntityAttribute.TYPE.String);
//                    // the key is the name of the child table like UD_CTS_UGP
//                    final List<RequestBeneficiaryEntityAttribute> record = new ArrayList<RequestBeneficiaryEntityAttribute>();
//                    record.add(createAttribute(attribute.getId(), lookupEntitlement(attribute.getValue()), attribute.type()));
//                    parent.setAction(RequestBeneficiaryEntityAttribute.ACTION.Add);
//                    parent.setChildAttributes(record);
//          }
          for (EntitlementEntity ent : entity.toRevoke(nsEntryKey)) {
            // Retrieve the EntitlementEntity attribute "id", for now let's assume all are single-attribute entitlements
            EntitlementInstance entInst = entitlementInstances.get(ent.get(ent.keySet().toArray()[0]));
            LOGGER.debug(CLASS, method, "Removing entitlement: " + ent + ", the appropriate instance is " + entInst);

            RequestBeneficiaryEntity rbeRevokeEnt = new RequestBeneficiaryEntity();
            // ---
            rbeRevokeEnt.setRequestEntityType(OIMType.Entitlement);
            rbeRevokeEnt.setEntitySubType(String.valueOf(entInst.getEntitlement().getEntitlementKey()));
            rbeRevokeEnt.setOperation(RequestConstants.MODEL_REVOKE_ENTITLEMENT_OPERATION);
            rbeRevokeEnt.setEntityKey(String.valueOf(entInst.getEntitlementInstanceKey()));

            LOGGER.debug(CLASS, method, "String.valueOf(entInst.getEntitlement().getEntitlementKey()) " + String.valueOf(entInst.getEntitlement().getEntitlementKey()));
            LOGGER.debug(CLASS, method, "String.valueOf(entInst.getEntitlementInstanceKey()) " + String.valueOf(entInst.getEntitlementInstanceKey()));
            
            LOGGER.debug(CLASS, method, "Adding RBE for Entitlement revoke: " + rbeRevokeEnt);
            rbeList.add(rbeRevokeEnt);
          }
//        entity.toModify(nsEntryKey);
        }
      }
    */
      String submitResult = ad.splitSubmit(rbeList);

      final StringBuilder message = new StringBuilder("Request ").append(submitResult).append(" for account ");
      message.append(account == null ? "creation" : "modification");
      message.append(" submited.");
      LOGGER.exiting(CLASS, method, message.toString());
      return new RequestMessage(RequestMessage.OK, submitResult);

    } catch (RequestException e) {
      final StringBuilder message = new StringBuilder("Request Exception occurred.");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeAccount
  /**
   ** Used to construct and submit request to revoke an account.
   **
   ** @param account                   the input {@link Account} object identifying
   **                                  the corresponding OIM Account object.
   **                                  <br>
   **                                  Allowed object is {@link Account}.
   **                                  
   ** @return                          the {@link RequestMessage} object containing request message.
   **                                  <br>
   **                                  Possible object is {@link RequestMessage}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private RequestMessage revokeAccount(Account account)
    throws IllegalArgumentException {
    final String method = "revokeAccount";
    LOGGER.entering(CLASS, method, "account=", account);
    RequestAdapter ad = RequestAdapter.build(client);
    try {
      ad.prepare(account.getUserKey());
      
      LOGGER.debug(CLASS, method, "Account ID: " + account.getAccountID());
      LOGGER.debug(CLASS, method, "ApplicationInstance Key: " + account.getAppInstance().getApplicationInstanceKey());
      LOGGER.debug(CLASS, method, "ApplicationInstance Name: " + account.getAppInstance().getApplicationInstanceName());

      RequestBeneficiaryEntity rbe = new RequestBeneficiaryEntity();
      rbe.setRequestEntityType(OIMType.ApplicationInstance);
      rbe.setEntitySubType(account.getAppInstance().getApplicationInstanceName());
      rbe.setEntityKey(account.getAccountID());
      rbe.setOperation(RequestConstants.MODEL_REVOKE_ACCOUNT_OPERATION);

      LOGGER.debug(CLASS, method, "Requesting " + rbe.getRequestEntityType() + ": " + rbe.getEntitySubType() + " (ID " + rbe.getEntityKey() + ")");
      String submitResult = ad.splitSubmit(CollectionUtility.list(rbe));

      final StringBuilder message = new StringBuilder("Request ").append(submitResult).append(" for account revoke submited.");
      LOGGER.exiting(CLASS, method, message.toString());
      return new RequestMessage(RequestMessage.OK, submitResult);
    } catch (RequestException e) {
      final StringBuilder message = new StringBuilder("Request Exception occurred.");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
  }

  private List<RequestBeneficiaryEntityAttribute> prepareAccountAssignments(Account account, AccountEntity entity, Map<String, Entitlement> entitlements)
    throws IllegalArgumentException {
    final String method = "prepareAccountAssignments";
    LOGGER.entering(CLASS, method, "entity=", entity);
    
    final List<RequestBeneficiaryEntityAttribute> assignmentAttributes = new ArrayList<>();
    if (entity.namespace() != null) {
      Map<String, List<NamespaceEntity>> ns = entity.namespace();
      for (String nsEntryKey : ns.keySet()) {
        // Each namespace corresponds to a child table name
        LOGGER.debug(CLASS, method, "Entitlements to assign in namespace " + nsEntryKey +":");
        List<EntitlementEntity> allMentionedEntitlements = new ArrayList<>();
        allMentionedEntitlements.addAll(entity.toAssign(nsEntryKey));
        allMentionedEntitlements.addAll(entity.toModify(nsEntryKey));
        allMentionedEntitlements.addAll(entity.toRevoke(nsEntryKey));
        for (EntitlementEntity entEnt : allMentionedEntitlements) {
          final RequestBeneficiaryEntityAttribute entitlementToAssign = new RequestBeneficiaryEntityAttribute(
            nsEntryKey,
            "",
            RequestBeneficiaryEntityAttribute.TYPE.String
            );
          LOGGER.debug(CLASS, method, "Entitlement entity: " + entEnt);
          Map<String, Object> attributes = new HashMap<>(entEnt.attribute());
          List<AdditionalAttributeEntity> additionalAttributes = entEnt.additionalAttributes();
          if (additionalAttributes.size() > 0) {
            for (AdditionalAttributeEntity additionalAttribute : additionalAttributes) {
              attributes.putAll(additionalAttribute.attribute());
            }
          }
          // the key is the name of the child table like UD_CTS_UGP
          final List<RequestBeneficiaryEntityAttribute> entitlementToAssignAttributes = new ArrayList<>();
//          for (AdditionalAttributeEntity additionalAttribute : additionalAttributes) {
            // Each entitlement request can have multiple attributes
            for (Map.Entry<String, Object> childAttribute : attributes.entrySet()) {
              LOGGER.debug(CLASS, method, "Processing child attribute: " + childAttribute.getKey() + "=" + String.valueOf(childAttribute.getValue()));
              Entitlement ent = entitlements.get(String.valueOf(childAttribute.getValue()));
              if (ent != null) {
                LOGGER.debug(CLASS, method, "Translated child attribute value to code: " + ent.getEntitlementCode());
                LOGGER.debug(CLASS, method, "Translated child attribute value to key: " + ent.getEntitlementKey());
                entitlementToAssignAttributes.add(new RequestBeneficiaryEntityAttribute(childAttribute.getKey(),
                                                                      entitlements.get(String.valueOf(childAttribute.getValue())).getEntitlementCode(),
                                                                      RequestBeneficiaryEntityAttribute.TYPE.String));
              } else {
                LOGGER.debug(CLASS, method, "No translation, it's an additional attribute");
                entitlementToAssignAttributes.add(new RequestBeneficiaryEntityAttribute(childAttribute.getKey(),
                                                                      String.valueOf(childAttribute.getValue()),
                                                                      RequestBeneficiaryEntityAttribute.TYPE.String));
              }
            }
//          }
          LOGGER.debug(CLASS, method, "Action to perform for the child attributes: " + action(entEnt.action()));
            
          if(EntitlementEntity.Action.modify.equals(entEnt.action()) || EntitlementEntity.Action.revoke.equals(entEnt.action())) {
            String rowKey = null;
            Map<String, ArrayList<ChildTableRecord>> childData = account.getAccountData().getChildData();
            ArrayList<ChildTableRecord> childTableRecords = childData.get(nsEntryKey);
            for (ChildTableRecord childTableRecord : childTableRecords) {
              for (Map.Entry<String, Object> childAttribute : entEnt.attribute().entrySet()) {
                String formFieldName = "";
                String z = String.valueOf(childAttribute.getValue());
                FormInfo fi = entitlements.get(z).getChildForm();
                for(FormField ff : fi.getFormFields()){
//                  if(ff.getLabel().equals(childAttribute.getKey())){
                  if(ff.getName().equals(childAttribute.getKey())){
                        formFieldName = ff.getName();
                        break;
                    }
                }
                String childRecordAttrValue =
                              String.valueOf(childTableRecord.getChildData().get(formFieldName));
                String x = String.valueOf(childAttribute.getValue());
                Entitlement e = entitlements.get(x);
                String code = e.getEntitlementCode();
                if (childRecordAttrValue.equals(code)) {
                  rowKey = childTableRecord.getRowKey();
                  break;
                }
              }
              if(rowKey != null){
                break;    
              }
            }
            entitlementToAssign.setRowKey(rowKey);
          }
          entitlementToAssign.setAction(action(entEnt.action()));
          entitlementToAssign.setChildAttributes(entitlementToAssignAttributes);
          assignmentAttributes.add(entitlementToAssign);
        }
//        assignmentAttributes.add(parent);
      }
      LOGGER.debug(CLASS, method, "Total size of assignment attributes: " + assignmentAttributes.size());
    }
    LOGGER.exiting(CLASS, method, assignmentAttributes);
    return assignmentAttributes;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Helper method to convert {@link EntitlementEntity.Action} to {@link
   ** RequestBeneficiaryEntityAttribute.ACTION} object.
   **
   ** @param entAction                 the input {@link EntitlementEntity.Action} object.
   **                                  <br>
   **                                  Allowed object is {@link EntitlementEntity.Action}.
   **                                  
   ** @return                          the identified {@link
   **                                  RequestBeneficiaryEntityAttribute.ACTION}
   **                                  corresponding to the input or <code>null</code> if
   **                                  the input cannot be mapped.
   **                                  <br>
   **                                  Possible object is {@link
   **                                  RequestBeneficiaryEntityAttribute.ACTION}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private RequestBeneficiaryEntityAttribute.ACTION action(EntitlementEntity.Action entAction) {
    switch (entAction) {
    case assign:
      return RequestBeneficiaryEntityAttribute.ACTION.Add;
    case modify:
      return RequestBeneficiaryEntityAttribute.ACTION.Modify;
    case revoke:
      return RequestBeneficiaryEntityAttribute.ACTION.Delete;
    default:
      return null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifyAccount
  /**
   ** Used to identify account owner by the {@link User} in the particular
   ** {@link ApplicationInstance}. Returns null 
   **
   ** @param user                      the {@link User} representation of the OIM user.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the identified {@link Account} owner by the user
   **                                  or <code>null</code> if no such account exists.
   **                                  <br>
   **                                  Possible object is {@link Account}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private Account identifyAccount(User user, ApplicationInstance appInstance) {
    final String method = "identifyAccount";
    LOGGER.entering(CLASS, method, "user=", user, "appInstance=", appInstance);
    
    List<Account> accList = listUserAccounts(user.getId(), appInstance);
    final StringBuilder message = new StringBuilder("Found ")
      .append(accList.size())
      .append(" accounts for ")
      .append(user.getLogin())
      .append(" (")
      .append(user.getId())
      .append(")");
    LOGGER.debug(CLASS, method, message.toString());

    // Identify the account to be processed
    Account accountToProcess = null;
    for (Account acc : accList) {
      // Currently, it was assumed that the account name is the same as user name, this facade does not support
      // other account names at this time.
      LOGGER.debug(CLASS, method, "AppInstance name: " + appInstance.getApplicationInstanceName());
      LOGGER.debug(CLASS, method, "Account AppInstance name: " + acc.getAppInstance().getApplicationInstanceName());
      LOGGER.debug(CLASS, method, "Owner name: " + user.getLogin());
      LOGGER.debug(CLASS, method, "Account descriptive field: " + acc.getAccountDescriptiveField());
      
      if (appInstance.getApplicationInstanceName().equals(acc.getAppInstance().getApplicationInstanceName())&&
          user.getLogin().equalsIgnoreCase(acc.getAccountDescriptiveField())) {
        // Seems we found it, let's dump it and break
        LOGGER.debug(CLASS, method, "Account identified: " + acc.getAccountID());
        accountToProcess = acc;
        break;
      }
    }
    LOGGER.exiting(CLASS, method, accountToProcess);
    return accountToProcess;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlementInstances
  /**
   ** Used to retrieve {@link Map} of {@link EntitlementInstance} objects for
   ** OIM user represented by the {@link User} object.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param user                      the {@link User} representation of the OIM user.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the retrieved {@link Map} of {@link EntitlementInstance}
   **                                  objects representing list of assigned entitlements
   **                                  for the input user. Keyed by entitlement name (also
   **                                  known as "value" in OIM).
   **                                  <br>
   **                                  Possible object is {@link Map} of
   **                                  {@link EntitlementInstance}s.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private Map<String, EntitlementInstance> getEntitlementInstances(User user)
  throws IllegalArgumentException {
    final String method = "retrieveEntitlementInstances";
    LOGGER.entering(CLASS, method, "user=", user);

    Map<String, EntitlementInstance> entitlementInstances = new HashMap<>();
    try {
      for (EntitlementInstance ent :
           client.getService(ProvisioningService.class).getEntitlementsForUser(user.getId())) {
        entitlementInstances.put(ent.getEntitlement().getEntitlementValue(), ent);
//                                 String.valueOf(ent.getEntitlementInstanceKey()));
      }
    } catch (UserNotFoundException e) {
      // May not happen, we already retrieved the used before
      final StringBuilder message = new StringBuilder("User Not Found Exception occurred");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);      
    } catch (GenericProvisioningException e) {
      final StringBuilder message = new StringBuilder("Generic Provisioning Exception occurred");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);      
    }
    LOGGER.exiting(CLASS, method, entitlementInstances);
    return entitlementInstances;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCatalogObject
  /**
   ** Method to identify the lookup associated with the input column name and return
   ** the decoded value based on input encoded value.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param entityKey                 the {@link Long} key of the entity to retrieve
   **                                  from the catalog.
   **                                  <br>
   **                                  Allowed object is {@link Long}.
   **                                  
   ** @param entityType                the {@link OIMType} type of the entity to retrieve
   **                                  from the catalog.
   **                                  <br>
   **                                  Allowed object is {@link OIMType}.
   **                                  
   ** @return                          the retrieved {@link Catalog} item.
   **                                  <br>
   **                                  Possible object is {@link Catalog}.
   **/
  private Catalog getCatalogObject(long entityKey, OIMType entityType) {
    final String method = "getCatalogObject";
    LOGGER.entering(CLASS, method, "entityKey=", entityKey, "entityType=", entityType);
    
    Catalog catalogEntity = null;
    try {
      CatalogService catalogService = client.getService(CatalogService.class);
      catalogEntity = catalogService.getCatalogItemDetails(null,
                                String.valueOf(entityKey),
                                entityType,
                                null);
      catalogEntity.getId();
    } catch (CatalogException e) {
      final StringBuilder message = new StringBuilder("CatalogException caught: ").append(e.getMessage());
      LOGGER.debug(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (NullPointerException e) {
      final StringBuilder message = new StringBuilder("No object retrieved: ").append(e.getMessage());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
    LOGGER.exiting(CLASS, method, catalogEntity);
    return catalogEntity;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCatalogObject
  /**
   ** Method to identify the lookup associated with the input column name and return
   ** the decoded value based on input encoded value.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param catalogItemId             the {@link Long} key of the catalog object.
   **                                  <br>
   **                                  Allowed object is {@link Long}.
   **                                  
   ** @return                          the retrieved {@link Catalog} item.
   **                                  <br>
   **                                  Possible object is {@link Catalog}.
   **/
  private Catalog getCatalogObject(long catalogItemId) {
    final String method = "getCatalogObject";
    LOGGER.entering(CLASS, method, "catalogItemId=", catalogItemId);
    
    Catalog catalogEntity = null;
    try {
      CatalogService catalogService = client.getService(CatalogService.class);
      catalogEntity = catalogService.getCatalogItemDetails(catalogItemId,
                                                           null,
                                                           null,
                                                           null);
      catalogEntity.getId();
    } catch (CatalogException e) {
      final StringBuilder message = new StringBuilder("CatalogException caught: ").append(e.getMessage());
      LOGGER.debug(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (NullPointerException e) {
      final StringBuilder message = new StringBuilder("No object retrieved: ").append(e.getMessage());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
    LOGGER.exiting(CLASS, method, catalogEntity);
    return catalogEntity;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   dumpJson
  /**
   ** Method to identify the lookup associated with the input column name and return
   ** the decoded value based on input encoded value.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param inputObject               the {@link JsonObject} to beautify.
   **                                  <br>
   **                                  Allowed object is {@link JsonObject}.
   **/
  private void dumpJson(JsonObject inputObject) {
    final String method = "dumpJson";
    LOGGER.entering(CLASS, method, "inputObject=", inputObject);

    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    try {
      String prettyJson = mapper.writeValueAsString(inputObject);
      LOGGER.debug(CLASS, method, "Beautified JSON dump: " + prettyJson);
    } catch (JsonProcessingException e) {
      LOGGER.throwing(CLASS, method, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountById
  /**
   ** Request to retrieve an {@link Account} object identified by
   ** <code>accountId</code>.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param  accountId                ID of the account to retrieve.
   **                                  <br>
   **                                  Allowed object is {@link Long}.
   **
   ** @return                          the instance {@link AccountEntity} object
   **                                  identified by <code>accountId</code>.
   **                                  <br>
   **                                  Possible object {@link AccountEntity}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private Account getAccountById(final long accountId)
    throws IllegalArgumentException {
    final String method = "getAccountById(long)";
    LOGGER.entering(CLASS, method, "accountId=", accountId);
    
    try {  
      ProvisioningService provService = client.getService(ProvisioningService.class);
      Account account = provService.getAccountDetails(accountId);
      LOGGER.debug(CLASS, method, "Retrieved account: " + account);
      return account;
    } catch (GenericProvisioningException e) {
      final StringBuilder message = new StringBuilder("Generic Provisioning exception occurred");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (AccountNotFoundException e) {
      final StringBuilder message = new StringBuilder("Account not found");
      LOGGER.error(CLASS, method, message.toString());
      return null;
//      LOGGER.throwing(CLASS, method, e);
//      throw new IllegalArgumentException(message.toString(), e);
    } catch (oracle.iam.platform.authopss.exception.AccessDeniedException e) {
      final String message = "Permission denied";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountById
  /**
   ** Request to retrieve an {@link Account} object identified by
   ** <code>accountId</code>.
   **
   ** @param  accountId                ID of the account to retrieve.
   **                                  <br>
   **                                  Allowed object is {@link Long}.
   **
   ** @param userName                  the name administrator user performing the
   **                                  query. Used to employ OIM-defined
   **                                  restrictions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the instance {@link Account} object
   **                                  identified by <code>accountId</code>.
   **                                  <br>
   **                                  Possible object {@link Account}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public Account getAccountById(final long accountId, String userName)
    throws IllegalArgumentException {
    final String method = "getAccountById";
    LOGGER.entering(CLASS, method, "accountId=", accountId,
                    "userName=", userName);
    
    try {
      if (userName == null) {
        client.loginAsOIMInternal();
      } else {
        client.signatureLogin(userName);
      }
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    
    try {  
      Account account = getAccountById(accountId);
      LOGGER.debug(CLASS, method, "Retrieved account: " + account);
      if (!Helper.getIgnoredApplications(client).contains(account.getAppInstance().getApplicationInstanceName())) {
        AccountEntity accEnt = buildAccountEntity(account);
        client.logout();
        LOGGER.debug(CLASS, method, "Constructed account entity: " + accEnt);
        return account;
      } else {
        LOGGER.trace(CLASS, method, "Ignoring app " + account.getAppInstance().getApplicationInstanceName() + " as per Lookup configuration");
        return null;
      }
    } catch (IllegalArgumentException e) {
      final StringBuilder message = new StringBuilder("Illegal Argument Exception caught");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (oracle.iam.platform.authopss.exception.AccessDeniedException e) {
      final String message = "Permission denied";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountEntityById
  /**
   ** Request to retrieve an {@link AccountEntity} object identified by
   ** <code>accountId</code>.
   **
   ** @param  accountId                ID of the account to retrieve.
   **                                  <br>
   **                                  Allowed object is {@link Long}.
   **
   ** @param userName                  the name administrator user performing the
   **                                  query. Used to employ OIM-defined
   **                                  restrictions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the instance {@link AccountEntity} object
   **                                  identified by <code>accountId</code>.
   **                                  <br>
   **                                  Possible object {@link AccountEntity}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public AccountEntity getAccountEntityById(final long accountId, String userName)
    throws IllegalArgumentException {
    final String method = "getAccountEntityById";
    LOGGER.entering(CLASS, method, "accountId=", accountId,
                    "userName=", userName);

    try {
      if (userName == null) {
        client.loginAsOIMInternal();
      } else {
        client.signatureLogin(userName);
      }
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    
    try {
      Account account = getAccountById(accountId);
      LOGGER.debug(CLASS, method, "Retrieved account: " + account);
      if (account == null) {
        return null;
      }
      LOGGER.trace(CLASS, method, "Account app: " + account.getAppInstance().getApplicationInstanceName());
      if (!Helper.getIgnoredApplications(client).contains(account.getAppInstance().getApplicationInstanceName())) {
        AccountEntity accEnt = buildAccountEntity(account);
        client.logout();
        LOGGER.debug(CLASS, method, "Constructed account entity: " + accEnt);
        return accEnt;
      } else {
        LOGGER.trace(CLASS, method, "Ignoring app " + account.getAppInstance().getApplicationInstanceName() + " as per Lookup configuration");
        return null;
      }
    } catch (IllegalArgumentException e) {
      final StringBuilder message = new StringBuilder("Illegal Argument Exception caught");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
  }

}