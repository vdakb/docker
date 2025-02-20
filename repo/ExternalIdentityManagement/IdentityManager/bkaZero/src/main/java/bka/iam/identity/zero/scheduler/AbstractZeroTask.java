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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Zero Provisioning

    File        :   AbstractZeroTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    AbstractZeroTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2023-12-09  Sbernet     First release version
*/
package bka.iam.identity.zero.scheduler;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.tcResultSet;

import bka.iam.identity.zero.ZeroError;
import bka.iam.identity.zero.ZeroException;
import bka.iam.identity.zero.ZeroMessage;
import bka.iam.identity.zero.api.AccountsFacade;
import bka.iam.identity.zero.event.ProvisioningReport;
import bka.iam.identity.zero.model.Account;
import bka.iam.identity.zero.model.Identity;
import bka.iam.identity.zero.resources.ZeroBundle;
import bka.iam.identity.zero.server.LDAP;

import java.io.StringWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriterFactory;

import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.exception.AccessDeniedException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.EventMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.ldap.DirectoryException;
import oracle.iam.identity.foundation.naming.ITResource;
import oracle.iam.identity.foundation.resource.EventBundle;
import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.Schema;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.api.UserManager;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.EMAIL;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.LOCALE;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.exception.EventException;
import oracle.iam.notification.exception.MultipleTemplateException;
import oracle.iam.notification.exception.NotificationException;
import oracle.iam.notification.exception.NotificationResolverNotFoundException;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.exception.UnresolvedNotificationDataException;
import oracle.iam.notification.exception.UserDetailsNotFoundException;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.service.api.RequestMessage;
////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractZeroTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractZeroTask</code> implements the base functionality of Zero
 ** provisioning service accounts.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractZeroTask extends AbstractSchedulerTask {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** 
   ** Attribute to advice which named IT Resource should be used for LDAP.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String    IT_RESOURCE_LDAP           = "IT Resource LDAP";
  
  /**
   ** Attribute tag that must be set on this task to specify the name of
   ** the organization where the scheduled task operating on.
   ** <br>
   ** This attribute is optional.
   */
  public static final String ORGANIZATION                  = "Organization";
  
  /**
   ** Attribute tag which must be defined on this task to specify the
   ** application name where the scheduled task is operating on.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String APPLICATION_NAME              = "Application Name";
  
  /**
   ** Attribute tag which must be defined on this task to specify where is the
   ** DN entry point which carries account and access on this application.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String APPLICATION_ROOT_CONTEXT      = "Application Root Context";
  
  /**
   ** Attribute tag which must be defined on this task to specify which entry
   ** RDN holds application access.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String ACCOUNTS_RDN                  = "Accounts RDN";
  
  /**
   ** Attribute tag that must be set on this task to specify the name of the
   ** object class that carries access to the application.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String OBJECT_CLASS_ACCOUNT          = "Account Object Class";
  
  /**
   ** Attribute tag which must be defined on this task to specify the name of
   ** the entity attibute that holds members in the application.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String MEMBER_ATTRIBUTE_ACCOUNT      = "Account Member Attribute";
  
  /**
   ** Attribute to advise which template  the notification to send is based on.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String TEMPLATE                      = "Notification Template";
  
  /**
   ** Attribute to advise who is responsible for this application. Any
   ** operations made on the application will be notify to him.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String ADMIN_LOGIN                   = "Administrator Login";
  
  /**
   ** Default format to prefix LDAP entries.
   */
  public static final String ENTRY_PREFIX                  = "%s=%s";
  /**
   ** Default format to prefix LDAP entries.
   */
  public static final int   APPLICATION_RDN_INDEX          = 4;
  
  /**
   ** Default attribute name where the value of the entitlement name is stored
   ** on the LDAP entry.
   */
  public static final String DEFAULT_ENTITLEMENT_ATTRIBUTE = "cn";
  
  /**
   ** Default format to prefix LDAP entries.
   */
  public static final int    ACCOUNT_RDN_INDEX             = 4;
  
  /** the category of the logging facility to use */
  private static final String  CATEGORY                    = "BKA.ZERO.PROVISIONING";



  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer in order to use Zero LDAP system*/
  protected LDAP                  ldapServer;
  /** the abstraction layer in order to use Zero OIM service*/
  protected AccountsFacade        accountsFacade;
  /** Custom object to report change on user*/
  protected ProvisioningReport    zeroReporter;
  /** The name of the application where the scheduled task is operating on*/
  protected String                applicationName;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ProvisionningTask</code> scheduler
   ** instance that allows use as a JavaBean.
   ** <br>
   ** No parameter defined by the constructor.
   */
  public AbstractZeroTask() {
    // ensure inheritance
    super(CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   ldapResourceName
  /**
   ** Returns the name of the IT Resource that hold the connection information
   ** on the LDAP system.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #IT_RESOURCE_LDAP}.
   **
   ** @return                    the name of the IT Resource which will be used.
   */
  public final String ldapResourceName() {
    return stringValue(IT_RESOURCE_LDAP);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Returns the name of the organization where the scheduled task is operating
   ** on.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #ORGANIZATION}.
   **
   ** @return                    the name of the organization where the
   **                            scheduled task is operating on.
   */
  public final String organization() {
    return stringValue(ORGANIZATION);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationName
  /**
   ** Returns the name of the application where the scheduled task is operating
   ** on.
   **
   ** @return                    the name of the application where the scheduled
   **                            task is operating on.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String applicationName() {
    return stringValue(APPLICATION_NAME);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationRootContext
  /**
   ** Returns the root DN where accounts and access information is represented 
   ** for the provided application application.
   **
   ** @return                    the root DN where accounts and access information is represented 
   **                            for the provided application application.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String applicationRootContext() {
    return stringValue(APPLICATION_ROOT_CONTEXT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountsRDN
  /**
   ** Returns the RDN entry which defined which identity has access to the
   ** provided application.
   **
   ** @return                    the RDN entry which defined which identity has
   **                            access to the provided application.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String accountsRDN() {
    return stringValue(ACCOUNTS_RDN);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassAccount
  /**
   ** Returns the object class value that carries access to the application.
   **
   ** @return                    the object class value that carries access to
   **                            the application.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String objectClassAccount() {
    return stringValue(OBJECT_CLASS_ACCOUNT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberAttributeAccount
  /**
   ** Returns the name of the entity attibute that holds members in the
   ** application.
   **
   ** @return                    the name of the entity attibute that holds
   **                            members in the application
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String memberAttributeAccount() {
    return stringValue(MEMBER_ATTRIBUTE_ACCOUNT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   template
  /**
   ** Returns the name of the notification template that will be use for
   ** displaying the report.
   **
   ** @return                    the name of the notification template that will
   **                            be use for displaying the report.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String template() {
    return stringValue(TEMPLATE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   adminLogin
  /**
   ** Returns the application administrator login. Any notification regarding
   ** zero service will be send to him.
   **
   ** @return                    the application administrator login.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String adminLogin() {
    return stringValue(ADMIN_LOGIN);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution
  /**
   ** Initalize the configuration capabilities.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    final String method = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.applicationName = applicationName();
      this.ldapServer      = new LDAP(this, ldapResourceName());
      this.accountsFacade  = new AccountsFacade();
      this.zeroReporter    = new ProvisioningReport();
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterExecution
  /**
   ** Send status report for this task.
   */
  @Override
  protected void afterExecution() {
    
    final String method = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    // send notification
    try {
      if (this.zeroReporter.isEmpty()) {
        debug(method, ZeroBundle.format(ZeroMessage.REPORTER_EMPTY, this.applicationName));
        return;
      }
      
      final User admin = getUser(adminLogin());
      if (admin == null) {
        debug(method, ZeroBundle.format(ZeroMessage.REPORTER_NOT_FOUND, adminLogin()));
        return;
      }
      debug(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_CREATE));
      final NotificationEvent event = createNotificationEvent(admin.getId(), this.applicationName);
      debug(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_SEND));
      sendNotification(event);
    }
    catch (Exception e) {
      // somethings went wrong with notification but it's a non blocking
      // exception
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getAccountMembers
  /**
   ** Return account members (DN representation) of the application belonging
   ** to the tenant.
   ** 
   ** @param tenantPrefix        the tenant prefix value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the list of members (DN) of the belonging
   **                            to the specified tenant.
   **                            
   ** @throws TaskException      if the research in the ldap failed.
   ** 
   */
  protected List<String> getAccountMembers(final String tenantPrefix)
    throws TaskException {
    
    final String method = "getAccountMembers";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final List<String> accountsDN = new ArrayList<String>();
    
    final Set<String> objectClass         = StringUtility.isEmpty(objectClassAccount())     ? null : new HashSet<String>(Arrays.asList(objectClassAccount()));
    final Set<String> returnAttributes    = StringUtility.isEmpty(memberAttributeAccount()) ? null : new HashSet<String>(Arrays.asList(memberAttributeAccount()));
    final String      applicationAccessDN = String.format("%s,%s", accountsRDN(), applicationRootContext());
    
    try {
      final Attributes applicationAccessEntry = this.ldapServer.lookupEntries(applicationAccessDN, objectClass, returnAttributes);
      
      //Check if attribute has been return -> thorw error on administrator  
      final Attribute members = applicationAccessEntry.get(memberAttributeAccount());
      if (members == null) {
        logger.warn(ZeroBundle.format(ZeroMessage.LDAP_ATTR_NOT_FOUND, memberAttributeAccount(), applicationAccessDN));
        trace(method, SystemMessage.METHOD_EXIT);
        return accountsDN;
      }
      for (NamingEnumeration attribute = applicationAccessEntry.get(memberAttributeAccount()).getAll(); attribute.hasMore();) {
        final String memberDN      = (String) attribute.next();
        final String memberValue   = getFirstRDNValue(memberDN);
        //if (memberValue.toUpperCase().startsWith(tenantPrefix.toUpperCase()))
          accountsDN.add(memberDN);
      }
      
      trace(method, SystemMessage.METHOD_EXIT);
      return accountsDN;
    }
    catch (DirectoryException e) {
      logger.fatal(e);
      throw new ZeroException(ZeroError.LDAP_ERROR, e.getMessage());
    }
    catch (NamingException e) {
      logger.fatal(e);
      throw new ZeroException(ZeroError.LDAP_ERROR, e.getMessage());
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: searchAccountEntity
  /**
   ** Return the account antity that match with the provided account ID.
   ** 
   ** @param identities          The <code>Identity</code> Entity List.
   **                            <br>
   **                            Allowed object is {@link List}.
   ** 
   **
   ** @return                    the account antity object that match with the
   **                            provided account ID.<code>null</code> otherwise
   **
   ** @throws TaskException      if the research in the ldap failed.
   */
  protected List<AccountEntity> searchAccountEntity(final List<Identity> identities)
    throws TaskException {
    
    final String method = "searchAccountEntity";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final List<AccountEntity> result = new ArrayList<>();
    final List<oracle.iam.provisioning.vo.Account> accounts = getProvAccount(identities, this.applicationName);
    final List<oracle.iam.provisioning.vo.Account> filteredAccounts = accounts.stream().filter(account -> identities.stream().anyMatch(identity -> identity.identity().equalsIgnoreCase(account.getAccountDescriptiveField()))).collect(Collectors.toList());
    
    for (oracle.iam.provisioning.vo.Account account :  filteredAccounts) {
      if (account.getAccountType().equals(oracle.iam.provisioning.vo.Account.ACCOUNT_TYPE.Primary))
        result.add(buildAccountEntity(account));
    }
    
    trace(method, SystemMessage.METHOD_EXIT);
    return result;
  }
  
   //////////////////////////////////////////////////////////////////////////////
  // Method: popAccountEntity
  /**
   ** Return the account entity that match with the provided user login and
   ** remove from the provided Account Entity list.
   ** 
   ** @param accountEntities     The <code>Identity</code> Entity List.
   **                            <br>
   **                            Allowed object is {@link List}.
   **                            
   ** @param usrLogin            The account entities login to be search for
   **                            Allowed object is {@link String}
   ** 
   **
   ** @return                    the account entity object that match with the
   **                            provided user login.<code>null</code> otherwise
   */
  protected AccountEntity popAccountEntity(final List<AccountEntity> accountEntities, final String usrLogin) {
    
    for (AccountEntity account : accountEntities) {
      if (account.id().equalsIgnoreCase(usrLogin)) {
        accountEntities.remove(account);
        return account;
      }
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: lookupAccountEntity
  /**
   ** Return the account entity that match with the provided user login.
   ** 
   ** @param accountEntities     The <code>Identity</code> Entity List.
   **                            <br>
   **                            Allowed object is {@link List}.
   **                            
   ** @param usrLogin            The account entities login to be search for
   **                            Allowed object is {@link String}
   ** 
   **
   ** @return                    the account entity object that match with the
   **                            provided user login.<code>null</code> otherwise
   */
  protected AccountEntity lookupAccountEntity(final List<AccountEntity> accountEntities, final String usrLogin) {
    
    for (AccountEntity account : accountEntities) {
      if (account.id().equalsIgnoreCase(usrLogin)) {
        return account;
      }
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getITResourceName
  /**
   ** Return the IT Resource name for the provided IT Resource key.
   **
   ** @param itResKey                  the IT Resource key.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the IT Resource name for the provided IT
   **                                  Resource key
   **                                  <br>
   **                                  Possible object is {@link String}.
   **
   ** @throws TaskException            if any error occurred when fetching IT
   **                                  Resource.
   **/
  protected String getITResourceName(final String itResKey)
    throws TaskException {
    final String method = "getITResourceName";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final Map<String, String> filter = new HashMap<String, String>();
    final tcITResourceInstanceOperationsIntf itResouceInst = service(tcITResourceInstanceOperationsIntf.class);
    
    try {
      filter.put(ITResource.KEY, itResKey);
      final tcResultSet resultSet = itResouceInst.findITResourceInstances(filter);
      for (int j = 0; j < resultSet.getRowCount(); j++) {
        resultSet.goToRow(j);
        return resultSet.getStringValue(ITResource.NAME);
      }
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(e);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getRDNValue
  /**
   ** Return the RDN index value from the provided DN value.
   **
   ** @param  dn                 A non-null distinguished name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  indexRDN           the cursor of the RDN in the provided dn.
   **                            <br>
   **                            Allowed object is {@link int}.
   **
   ** @return                    the RDN index value of the supplied DN.
   ** 
   ** @throws ZeroException      if the provided DN is incorrect or if the index
   **                            is outside RDNs number range.
   */
  protected String getRDNValue(final String dn, final int indexRDN)
    throws ZeroException {
    
    final String method = "getRDNValue";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    String rdn = null;
    try {
      final LdapName ldapDN   = new LdapName(dn);
      //final int      indexRDN = ldapDN.size() - 1;
      if (indexRDN > 0 || indexRDN < ldapDN.size()) {
        // LdapName checks the validity of the DN. Don't need to check if it
        // contains 2 elements.
        rdn = ldapDN.get(indexRDN).split("=")[1];
      }
      else {
        throw new ZeroException(ZeroError.INDEX_RDN_OUT_RANGE, String.valueOf(indexRDN), dn);
      }
    }
    catch (InvalidNameException e) {
      throw new ZeroException(ZeroError.NAMING_DN_ERROR, dn, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return rdn;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getFirstRDNValue
  /**
   ** Return the RDN index value from the provided DN value.
   **
   ** @param  dn                 A non-null distinguished name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the RDN index value of the supplied DN.
   ** 
   ** @throws ZeroException      if the provided DN is incorrect or if the index
   **                            is outside RDNs number range.
   */
  protected String getFirstRDNValue(final String dn)
    throws ZeroException {
    
    final String method = "getFirstRDNValue";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    String rdn = null;
    try {
      final LdapName ldapDN   = new LdapName(dn);
      final int      indexRDN = ldapDN.size() - 1;
      if (indexRDN > 0 || indexRDN < ldapDN.size()) {
        // LdapName checks the validity of the DN. Don't need to check if it
        // contains 2 elements.
        rdn = ldapDN.get(indexRDN).split("=")[1];
      }
      else {
        throw new ZeroException(ZeroError.INDEX_RDN_OUT_RANGE, String.valueOf(indexRDN), dn);
      }
    }
    catch (InvalidNameException e) {
      throw new ZeroException(ZeroError.NAMING_DN_ERROR, dn, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return rdn;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getUser
  /**
   ** Return true if the provided user login exist in OIM, false
   ** otherwise.
   **
   ** @param  usrLogin           the login of the user in OIM.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    true if the provided user login exist in OIM,
   **                            false otherwise.
   **                            
   ** @throws TaskException      if lookup operation on user fails.
   */
  protected User getUser(final String usrLogin)
    throws TaskException {
    
    final String method = "getUser";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    try {
      final UserManager usrMgr = service(UserManager.class);
      final User        user   = usrMgr.getDetails(usrLogin, null, true);
      return user;
    }
    catch (AccessDeniedException e) {
      throw new TaskException(e);
    }
    catch (NoSuchUserException  e) {
      // Do nothing. Variable found already set to false.
    }
    catch (UserLookupException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getOrganization
  /**
   ** Return the {@link Organization} object if the provided organization name
   ** exists in OIM, null otherwise.
   **
   ** @param  orgName            the name of the organization.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Organization} object if the provided
   **                            organization name exists in OIM, null otherwise.
   ** 
   ** @throws TaskException      if an error occurred while checking the
   **                            existence of the organization.
   */
  protected Organization getOrganization(final String orgName)
    throws TaskException {
    
    final String method = "getOrganization";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final OrganizationManager  orgMgr  = service(OrganizationManager.class);
      final Organization         orgIns = orgMgr.getDetails(orgName, null, true);
      return orgIns;
    }
    catch (OrganizationManagerException e) {
      // Do nothing. Variable found already set to false.
    }
    catch (AccessDeniedException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: appInstExists
  /**
   ** Return the application instance object if the provided appInstName exist
   ** in OIM, null otherwise.
   **
   ** @param  appInstName        the name of the Application Instance to check.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ApplicationInstance} object or null
   **                            otherwise.
   **                           
   ** @throws TaskException     if an error occurred while fetching application
   **                           record in the db.
   */
  protected ApplicationInstance getAppInstance(final String appInstName)
    throws TaskException {
    
    final String method = "getAppInstance";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    try {
      final ApplicationInstanceService appInstService = service(ApplicationInstanceService.class);
      final ApplicationInstance        appInst = appInstService.findApplicationInstanceByName(appInstName);
      
      return appInst;
    }
    catch (GenericAppInstanceServiceException e) {
      throw new TaskException(e);
    }
    catch (ApplicationInstanceNotFoundException e) {
      // Do nothing. Variable found already set to false.
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getProvAccount
  /**
   ** Return the application instance object if the provided appInstName exist
   ** in OIM, empty list otherwise.
   **
   ** @param  identities         the list of {@link Identity} from which the
   **                            accounts belong to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  appInstName        the name of the Application Instance to check.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ApplicationInstance} object or empty
   **                            list otherwise.
   **                            
   ** @throws TaskException     if an error occurred while fetching application
   **                           record in the db.
   **                           
   */
  protected List<oracle.iam.provisioning.vo.Account> getProvAccount(final List<Identity> identities, final String appInstName)
    throws TaskException {
    
    final String method = "getProvAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final List<oracle.iam.provisioning.vo.Account> accountsDetails = new ArrayList<oracle.iam.provisioning.vo.Account>();
    
    try {
      final ApplicationInstance appInst     = getAppInstance(appInstName);
      final ProvisioningService provService = service(ProvisioningService.class);
      
      final UserManager usrMgr = service(UserManager.class);
     
      for (Identity identity : identities) {
        final User        user   = usrMgr.getDetails(identity.identity(), null, true);
        accountsDetails.addAll(provService.getUserAccountDetailsInApplicationInstance(user.getId(), appInst.getApplicationInstanceKey(), true));
      }
      
      return accountsDetails;
    }
    catch (GenericProvisioningException e) {
      logger.error(method, e);
    }
    catch(ApplicationInstanceNotFoundException | UserNotFoundException e) {
      logger.error(method, e);
    }
    catch(NoSuchUserException | UserLookupException e) {
      logger.error(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    
    return accountsDetails;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: pushAccountEntity
  /**
   ** Return the application instance object if the provided appInstName exist
   ** in OIM, null otherwise.
   **
   ** @param  applicationInstance  the name of the Application Instance to check.
   **                              <br>
   **                              Allowed object is {@link String}.
   **                              
   ** @param  userID               the account ID of the application.
   **                              <br>
   **                              Allowed object is {@link String}.
   ** 
   ** @param  entlByNameSpace     The list of entitlement by namespace. Can be
   **                             null
   **                             <br>
   **                             Allowed object is {@link String}.
   **                             
   ** @param  action              Type of operation on the account.
   **                             <br>
   **                             Allowed object is {@link AccountEntity.Action}.
   */
  protected void pushAccountEntity(final ApplicationInstance applicationInstance, final String userID, final Map<String, List<EntitlementEntity>> entlByNameSpace, final AccountEntity.Action action) {
    
    final String method = "pushAccountEntity";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final AccountEntity accountEntity = Entity.account(userID);
    accountEntity.action(action);
    accountEntity.type(oracle.iam.provisioning.vo.Account.ACCOUNT_TYPE.Primary.name());
    accountEntity.status("");
    
    
    
    if (entlByNameSpace != null && !entlByNameSpace.isEmpty()) {
      for (Map.Entry<String, List<EntitlementEntity>> cursor : entlByNameSpace.entrySet()) {
        accountEntity.namespace(Entity.namespace(cursor.getKey()).element(cursor.getValue()));
      }
    }
    
    // Printing json object from Account Entity is time consuming. Check
    // first if debug log is enabled.
    if (this.logger.debugLevel()) {
      final String jsonAsString = formJsonObjectasString(Schema.marshalAccount(accountEntity));
      debug(method,  ZeroBundle.format(ZeroMessage.REQUEST_NEW_ACCOUNT, jsonAsString));
    }
    try {
      final List<RequestMessage> messages = this.accountsFacade.processAccountEntity(accountEntity, applicationInstance.getApplicationInstanceName(), null);
      this.zeroReporter.add(ProvisioningReport.Action.from(accountEntity.action().id), userID, messages);
    }
    catch (Exception e) {
      final List<RequestMessage> exception = new ArrayList<RequestMessage>();
      exception.add(new RequestMessage(RequestMessage.UNHANDLED, e.getMessage()));
      this.zeroReporter.add(ProvisioningReport.Action.from(accountEntity.action().id), userID, exception);
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrieveRecipientLocale
  /**
   ** Return the locale of the provided user key.
   **
   ** @param  userKey           the identifier of a {@link User} in Identity
   **                           Manager.
   **                           <br>
   **                           Allowed object is {@link String}.
   **
   ** @return                   the locale of the provided user key.
   */
  protected Locale retrieveRecipientLocale(final String userKey) {
    final String method = "retrieveRecipientLocal";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    try {
      final UserManager  userManager = service(UserManager.class);
      User identity = null;
      if (userKey != null) {
        Set<String> attributes = new HashSet<String>();
        attributes.add(LOCALE.getId());

        // retrieving User ID
        identity = userManager.getDetails(userKey, attributes, false);
        // if exists add it to the list of recipients
        return new Locale(identity.getAttribute(LOCALE.getId()).toString());
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    return Locale.getDefault();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNotificationEvent
  /**
   ** This method creates/configure event object corresponding to the event when
   ** accounts on the application has been modified.
   **
   ** @param  userKey            the identifier of a {@link User} in Identity
   **                            Manager.
   ** @param  applicationName    The name of the application that concerns
   **                            the notification.
   **
   ** @return                   the {@link NotificationEvent} to send out.
   */
  private NotificationEvent createNotificationEvent(final String userKey, final String applicationName) {
    final String method = "createNotificationEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final NotificationEvent event = new NotificationEvent();
    
    final List<String> recipient   = new ArrayList<String>();
    User identity = null;
    // Get user id to create recipient
    try {
      final UserManager  userManager = service(UserManager.class);
      if (userKey != null) {
        Set<String> attributes = new HashSet<String>();
        attributes.add(USER_LOGIN.getId());
        attributes.add(EMAIL.getId());
        attributes.add(LOCALE.getId());

        // retrieving User ID
        identity = userManager.getDetails(userKey, attributes, false);
        // if exists add it to the list of recipients
        recipient.add(identity.getAttribute(USER_LOGIN.getId()).toString());
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    
    // Retrieve the recipient's language to customize the report in their own
    // language.
    final String locale = identity.getAttribute(LOCALE.getId()).toString();
    
    // set user identifier to whom notification is to be sent and set it in the event
    // object being created
    event.setUserIds(recipient.toArray(new String[0]));
    // set template name to be used to send notification for this event
    event.setTemplateName(template());
    // setting senderId as null here hence default sender ID would get picked up
    event.setSender(null);

    // create a map with key value pair for the parameters declared at time of
    // configuring notification event
    final HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("applicationName", applicationName);
    map.put("emailRecipient", identity.getEmail());
    // custom parameters
    map.put("added",    this.zeroReporter.getStatus(ProvisioningReport.Action.CREATE, new Locale(locale)));
    map.put("modified", this.zeroReporter.getStatus(ProvisioningReport.Action.MODIFY, new Locale(locale)));
    map.put("revoked",  this.zeroReporter.getStatus(ProvisioningReport.Action.DELETE, new Locale(locale)));

    event.setParams(map);
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return event;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   sendNotification
  /**
   ** Call notification engine passing an event object to it.
   **
   ** @param  event              the {@link NotificationEvent} to pass to the
   **                            engine.
   **
   ** @throws TaskException     if the operations fails in gerneral.
   */
  private void sendNotification(final NotificationEvent event)
    throws TaskException {

    final String method = "sendNotification";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // call notify method of NotificationService to pass on the event to
      // notification engine
      NotificationService facade = service(NotificationService.class);
      facade.notify(event);
    }
    catch (EventException e) {
      throw new ZeroException(ZeroError.NOTIFICATION_FAILED, e);
    }
    catch (UnresolvedNotificationDataException e) {
      throw new ZeroException(ZeroError.NOTIFICATION_UNRESOLVED_DATA, e);
    }
    catch (TemplateNotFoundException e) {
      throw new ZeroException(ZeroError.NOTIFICATION_TEMPLATE_NOTFOUND, e);
    }
    catch (MultipleTemplateException e) {
      throw new ZeroException(ZeroError.NOTIFICATION_TEMPLATE_AMBIGOUS, e);
    }
    catch (NotificationResolverNotFoundException e) {
      throw new ZeroException(ZeroError.NOTIFICATION_RESOLVER_NOTFOUND, e);
    }
    catch (UserDetailsNotFoundException e) {
      throw new ZeroException(ZeroError.NOTIFICATION_IDENTITY_NOTFOUND, e);
    }
    catch (NotificationException e) {
      throw new ZeroException(ZeroError.NOTIFICATION_EXCEPTION, e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAccountEntity
  /**
   ** Used to create {@link AccountEntity} object from {@link Account}.
   **
   ** @param inputAccount              the input {@link Account} object.
   **                                  <br>
   **                                  Allowed object is {@link Account}.
   **                                  
   ** @return                          the constructed {@link AccountEntity} object.
   **                                  <br>
   **                                  Possible object is {@link AccountEntity}.
   **
   ** @throws TaskException            if any error occurred when building
   **                                  {@link AccountEntity} object.
   **/
  protected abstract AccountEntity buildAccountEntity(final oracle.iam.provisioning.vo.Account inputAccount)
    throws TaskException;
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: formJsonObjectasString
  /**
   ** Formats a {@link JsonObject} as {@link String} for debugging purpose
   **
   ** @param  json               the Json object to print.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **                            
   ** @return                    a {@link JsonObject} formatted as a String
   **           
   */
  protected String formJsonObjectasString(final JsonObject json) {
    
    final StringBuilder jsonString = new StringBuilder();
    
    final Map<String, Boolean> config = new HashMap<>();
    config.put(javax.json.stream.JsonGenerator.PRETTY_PRINTING, true);
    
    final JsonWriterFactory writerFactory = Json.createWriterFactory(config);    
    final Writer writer = new StringWriter();
    
    writerFactory.createWriter(writer).write(json);
    jsonString.append(writer.toString());

    return jsonString.toString();
  }
}
