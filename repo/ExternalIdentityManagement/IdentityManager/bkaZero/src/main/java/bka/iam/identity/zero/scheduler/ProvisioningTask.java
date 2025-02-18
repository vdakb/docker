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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Scheduler Facilities

    File        :   ProvisioningTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    ProvisioningTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2023-17-07  Sbernet     First release version
*/
package bka.iam.identity.zero.scheduler;

import bka.iam.identity.zero.ZeroError;
import bka.iam.identity.zero.ZeroException;
import bka.iam.identity.zero.ZeroMessage;
import bka.iam.identity.zero.model.Identity;
import bka.iam.identity.zero.resources.ZeroBundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.EntitlementEntity.Action;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.NamespaceEntity;
import oracle.iam.identity.igs.model.Risk;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;
////////////////////////////////////////////////////////////////////////////////
// abstract class ProvisioningTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>ProvisionningTask</code> provision accounts for a specific
 ** application base on information found on a LDAP authoritative system.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProvisioningTask extends AbstractZeroTask {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /**
   ** Attribute tag which must be defined on this task to specify which entry
   ** RDN holds the entitlements for the provided application.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String ENTITLEMENT_RDN               = "Entitlement RDN";
  
  /**
   ** Attribute tag that must be set on this task to specify the name of the
   ** object class that carries entitlement objects.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String OBJECT_CLASS_ENTITLEMENT      = "Entitlement Object Class";
  
  /**
   ** Attribute tag that must be set on this task to specify the name of
   ** the namespace where entitlements belong to.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String ENTITLEMENT_NAMESPACE         = "Entitlement Namespace";
  
  /**
   ** Attribute tag that must be set on this task to specify the name of
   ** attribute on the childform where entitlements belong to.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String CHILDFORM_ATTRIBUTE_NAME      = "ChildForm Attribute Name";
  
  /**
   ** Attribute tag that must be set on this task to specify the name of
   ** the entity attibute that holds the name of the entitlement.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String ENTITLEMENT_ATTRIBUTE_NAME    = "Entitlement Attribute";
  /**
   ** Attribute tag that must be set on this task to specify the name of
   ** the entity attibute that holds members in entitlement entries.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String MEMBER_ATTRIBUTE_ENTITLEMENT  = "Entitlement Member Attribute";
  
  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attributes      = {
     /** the task attribute IT Resource */
     TaskAttribute.build(IT_RESOURCE_LDAP,              TaskAttribute.MANDATORY)
     /** the task attribute for scope */
  ,  TaskAttribute.build(ORGANIZATION,                  TaskAttribute.MANDATORY)
     /** the task attribute to indicated the application root context */
  ,  TaskAttribute.build(APPLICATION_ROOT_CONTEXT,      TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate the RDN entry that contains the
      ** accounts
      */
  ,  TaskAttribute.build(ACCOUNTS_RDN,                  TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate the object class that represents
      ** accesses to the application
      */
  ,  TaskAttribute.build(OBJECT_CLASS_ACCOUNT,          TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate the name of the attribute that
      ** represents application members in the application.
      */
  ,  TaskAttribute.build(MEMBER_ATTRIBUTE_ACCOUNT,      TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate the RDN sub-entries that contains
      ** entitlements
      */
  ,  TaskAttribute.build(ENTITLEMENT_RDN,               TaskAttribute.OPTIONAL)
      /**
      ** the task attribute to indicate the object class that represents
      ** entitlement for applications
      */
  ,  TaskAttribute.build(OBJECT_CLASS_ENTITLEMENT,      TaskAttribute.OPTIONAL)
     /**
      ** the task attribute to indicate the name of the attribute that
      ** represents members entitlement.
      */
  ,  TaskAttribute.build(MEMBER_ATTRIBUTE_ENTITLEMENT,  TaskAttribute.OPTIONAL)
     /** the task attribute childform name */
  ,  TaskAttribute.build(ENTITLEMENT_NAMESPACE,         TaskAttribute.OPTIONAL)
     /** the task attribute the value of the childform attribute name */
  ,  TaskAttribute.build(CHILDFORM_ATTRIBUTE_NAME,      TaskAttribute.OPTIONAL)
  ,  TaskAttribute.build(ENTITLEMENT_ATTRIBUTE_NAME,    TaskAttribute.OPTIONAL)
     /**
      ** the task attribute to indicate the name of the template used for
      ** sending report
      */
  ,  TaskAttribute.build(TEMPLATE,                      TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate to whom the notification will be send
      */
  ,  TaskAttribute.build(ADMIN_LOGIN,                   TaskAttribute.MANDATORY)
  };
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ProvisionningTask</code> scheduler
   ** instance that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProvisioningTask() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementRDN
  /**
   ** Returns the RDN entry which defined the subtree which holds every entry
   ** that represents an entitlement.
   **
   ** @return                    the RDN entry which defined which identity has
   **                            access to the provided application.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String entitlementRDN() {
    return stringValue(ENTITLEMENT_RDN);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassEntitlement
  /**
   ** Returns the name of the object class that carries entitlement objects.
   **
   ** @return                   the name of the object class that carries
   **                           entitlement objects.
   **                           <br>
   **                           Possiblle object {@link String}.
   */
  public final String objectClassEntitlement() {
    return stringValue(OBJECT_CLASS_ENTITLEMENT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberAttributeEntitlement
  /**
   ** Returns the name of the entity attibute that holds members in entitlement
   ** entries.
   **
   ** @return                    the name of the entity attibute that holds
   **                            members in entitlement entries.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String memberAttributeEntitlement() {
    return stringValue(MEMBER_ATTRIBUTE_ENTITLEMENT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementNamespace
  /**
   ** Returns the name of the namespace that belong to the entitlement.
   **
   ** @return                    the name of the namespace that belong to the
   **                            entitlement.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String entitlementNamespace() {
    return stringValue(ENTITLEMENT_NAMESPACE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   childformAttributeName
  /**
   ** Returns the name of the attribute in the childform where the entitlement
   ** belong to.
   **
   ** @return                    the name of the attribute in the childform
   **                            where the entitlement belong to.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String childformAttributeName() {
    return stringValue(CHILDFORM_ATTRIBUTE_NAME);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAttributeName
  /**
   ** Returns the name of the entity attibute that holds members in entitlement
   ** entries.
   **
   ** @return                    the name of the entity attibute that holds
   **                            members in entitlement entries.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public final String entitlementAttributeName() {
    return StringUtility.isEmpty(stringValue(ENTITLEMENT_ATTRIBUTE_NAME)) ? stringValue(ENTITLEMENT_ATTRIBUTE_NAME) : DEFAULT_ENTITLEMENT_ATTRIBUTE;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attributes;
  }

  @Override
  protected void onExecution()
    throws TaskException {
    
    final ApplicationInstance appInst = getAppInstance(this.applicationName);
    final Organization        orgInst = getOrganization(organization());
    
    if (appInst == null) {
      throw new ZeroException(ZeroError.APPLICATION_NOTFOUND, this.applicationName);
    }
    if (orgInst == null) {
      throw new ZeroException(ZeroError.ORGANIZATION_NOTFOUND, organization());
    }
    
    final StringBuilder          filterTenant = new StringBuilder(organization()).append(".*");
    final List<String>                members = getAccountMembers(filterTenant.toString());
      
    if (members.isEmpty()) {
      logger.warn(ZeroBundle.string(ZeroMessage.NO_ACCOUNT_TO_REQUEST));
      return;
    }
      
    final List<Identity>    identityEntities  = accountsFacade.list(applicationName, organization(), null);
    final List<AccountEntity> accountEntities = searchAccountEntity(identityEntities);

    for (String userDN : members) {
      final String usrLogin = getRDNValue(userDN, ACCOUNT_RDN_INDEX);
      // Skip identity that does not exist in OIM. This also avoid touching
      // identity that no belong to the tenant as the real login name
      // (without the tenant) contains one tenant prefix.
      if (getUser(usrLogin) == null)
        continue;
        
      final AccountEntity      account = lookupAccountEntity(accountEntities, usrLogin);
      final Set<String> lhsEntitlement = getAccountEntitlement(userDN);
      final Set<String> rhsEntitlement = account != null ? getEntitlementFromAccountEntity(account, entitlementNamespace()) : null;
        
      final Map<String, List<EntitlementEntity>> entitlementByNamespace = new HashMap<String, List<EntitlementEntity>>();
      // No account found on OIM. Means create an account from the
      // autoritative system.
      if (account == null) {
        final List<EntitlementEntity> entilements = new ArrayList<EntitlementEntity>();
        for (String entitlement : lhsEntitlement) {
          entilements.add(Entity.entitlement(EntitlementEntity.Action.assign, Risk.none).action(Action.assign.name()).value(childformAttributeName(), entitlement));
        }
        if (!entilements.isEmpty())
          entitlementByNamespace.put(entitlementNamespace(), entilements);
        pushAccountEntity(appInst, usrLogin, entitlementByNamespace, AccountEntity.Action.create);
        continue;
      }
      // Account has been found on OIM but modification might be needed.
      // Let's compare the delta with the autoritative system.
      final Set<String> addEntitlement = new HashSet<String>(lhsEntitlement);
      final Set<String> delEntitlement = new HashSet<String>(rhsEntitlement);
      // Do the magic: remove each element from each set, we've got two
      // list to update: add and revoke.
      addEntitlement.removeAll(rhsEntitlement);
      delEntitlement.removeAll(lhsEntitlement);
        
      final List<EntitlementEntity> updateEntitlement = new ArrayList<EntitlementEntity>();
      for (String entitlement : addEntitlement) {
        updateEntitlement.add(Entity.entitlement(EntitlementEntity.Action.assign, Risk.none).action(Action.assign.name()).value(childformAttributeName(), entitlement));
      }
      for (String entitlement : delEntitlement) {
        updateEntitlement.add(Entity.entitlement(EntitlementEntity.Action.assign, Risk.none).action(Action.revoke.name()).value(childformAttributeName(), entitlement));
      }
        
      if (!updateEntitlement.isEmpty()) {
        entitlementByNamespace.put(entitlementNamespace(), updateEntitlement);
        pushAccountEntity(appInst, usrLogin, entitlementByNamespace, AccountEntity.Action.modify);
      }
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
   ** @throws TaskException            if any error occurred when fetching IT
   **                                  Resource.
   **/
  @Override
  protected AccountEntity buildAccountEntity(final Account inputAccount)
    throws TaskException {
    final String method = "buildAccountEntity";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    AccountEntity outputAccount = Entity.account(inputAccount.getAccountDescriptiveField());
    outputAccount = NamespaceEntity.account(inputAccount.getAccountDescriptiveField());

    final Map<String, List<Object>> data = inputAccount.getNormalizedData();
    final List<Map> childDataList = (List) data.get(entitlementNamespace());
    
    final List<EntitlementEntity> entitlementList = new ArrayList<EntitlementEntity>();
    if (childDataList != null) {
      for (Map<String,Object> childData : childDataList) {
        for (Map.Entry<String, Object> entry : childData.entrySet()) {
        final String[] encodedChildForm = entry.getValue().toString().split("~");
        final String decodedChidlForm = String.format("%s~%s", getITResourceName(encodedChildForm[0]), encodedChildForm[1]);
        entitlementList.add(Entity.entitlement(EntitlementEntity.Action.assign, Risk.none).action(oracle.iam.identity.igs.model.AccountEntity.Action.modify.name()).value(entry.getKey(), decodedChidlForm));
        }
      }
      outputAccount.namespace(Entity.namespace(entitlementNamespace()).element(entitlementList));
    }
    
    trace(method, SystemMessage.METHOD_EXIT);
    return outputAccount;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getEntitlementEntries
  /**
   ** Return entitlement entries of the application.
   **
   ** @return                    the Map of entitlement entries of the
   **                            application.
   **                            
   ** @throws TaskException      if the research in the ldap failed.
   */
  protected Map<String, Attributes> getEntitlementEntries()
    throws TaskException {
    
    final String method = "getEntitlementMembers";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    
    Set<String> objectClass      = StringUtility.isEmpty(objectClassEntitlement())     ? null : new HashSet<String>(Arrays.asList(objectClassEntitlement()));
    Set<String> returnAttributes = StringUtility.isEmpty(memberAttributeEntitlement()) ? null : new HashSet<String>(Arrays.asList(memberAttributeEntitlement()));
    
    final String                  applicationAccessDN    = String.format("%s,%s", entitlementRDN(), applicationRootContext());
    final Map<String, Attributes> applicationAccessEntry = this.ldapServer.searchEntries(applicationAccessDN, objectClass, null, returnAttributes);
    
    trace(method, SystemMessage.METHOD_EXIT);
    
    return applicationAccessEntry;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getAccountEntitlement
  /**
   ** Returns entitlement to which the provided account belongs.
   ** 
   ** @param  userDN             the account DN.
   **
   ** @return                    the list of entitlement (DN) for the provided
   **                            account.
   **                            
   ** @throws TaskException      if the research in the ldap failed.
   */
  protected Set<String> getAccountEntitlement(final String userDN)
    throws TaskException {
    
    final String method = "getAccountEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final Set<String> objectClass      = StringUtility.isEmpty(objectClassEntitlement())     ? null : new HashSet<String>(Arrays.asList(objectClassEntitlement()));
    final Set<String> returnAttributes = StringUtility.isEmpty(memberAttributeEntitlement()) ? null : new HashSet<String>(Arrays.asList(memberAttributeEntitlement(), entitlementAttributeName()));
    final String      filter           = String.format("(%s=%s)", memberAttributeEntitlement(), userDN);
    
    final String                  applicationAccessDN    = String.format("%s,%s", entitlementRDN(), applicationRootContext());
    final Map<String, Attributes> applicationAccessEntry = this.ldapServer.searchEntries(applicationAccessDN, objectClass, filter, returnAttributes);
    
    final Set<String> entryName = new HashSet<String>();
    for (Map.Entry<String, Attributes> entry : applicationAccessEntry.entrySet()) {
      try {
        Attributes attributeEntry = entry.getValue();
        Attribute attribute = attributeEntry.get(entitlementAttributeName());
        entryName.add(attribute.get().toString());
      }
      catch (NamingException e) {
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return entryName;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getEntitlementFromAccountEntity
  /**
   ** Return entitlement value for the provided account entity in the requested
   ** namespace entitlements.
   ** 
   ** @param account              the account entity where the entitlements will
   **                             be pick up.
   **                            
   ** @param namespaceEntitlement the namespace value of the entitlement.
   **
   ** @return                     the set of entitlement belonging to the
   **                             requested namespace.
   */
  protected Set<String> getEntitlementFromAccountEntity(final AccountEntity account, final String namespaceEntitlement) {
    
    final String method = "getEntitlementFromAccountEntity";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final Set<String> result = new HashSet<String>();
    
    final Map<String, List<NamespaceEntity>> namespace = account.namespace();
    // downstream the namespace array entitlements
    if (namespace != null && namespace.size() > 0) {
      for (Map.Entry<String, List<NamespaceEntity>> collection : namespace.entrySet()) {
          // stream the namespace data
         for (NamespaceEntity cursor : collection.getValue()) {
            // it the requested namespace entitlement doesn't match do not
            // procced futher.
            if (!namespaceEntitlement.equals(cursor.id()))
              continue;
           
            for (EntitlementEntity entitlement : cursor.element()) {
              if (entitlement.size() > 0) {
                for (Map.Entry<String, Object> attribute : entitlement.entrySet()) {
                  result.add((String) attribute.getValue());
                }
              }
            }
         }
      }
    }
    
    trace(method, SystemMessage.METHOD_EXIT);
    return result;
  }
}
