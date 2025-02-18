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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Enterprise User Security

    File        :   SchemaProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EnterpriseSecurityProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2011-03-01  TSebo       First release version
*/

package oracle.iam.identity.eus.service.provisioning;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import javax.naming.NamingException;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcFormInstanceOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;

import oracle.iam.identity.foundation.naming.FormDefinition;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryException;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;

import oracle.iam.identity.gds.service.provisioning.Provisioning;

////////////////////////////////////////////////////////////////////////////////
// abstract class SchemaProvisioning
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>SchemaProvisioning</code> implements the base functionality of a
 ** for the Oracle Identity Adapter Factory which handles data delivered to a
 ** Directory Service that provides Enterprise User Security capabilities.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
abstract class SchemaProvisioning extends Provisioning {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String LOGGER_CATEGORY = "OCS.EUS.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SchemaProvisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   ** @param  instance           the system identifier of the
   **                            <code>IT Resource</code> used by this adapter
   **                            to establish a connection to the LDAP Server.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected SchemaProvisioning(final tcDataProvider provider, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(provider, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SchemaProvisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the LDAP Server.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
   ** @param  targetLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   ** @param  targetCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   ** @param  targetTimeZone     use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected SchemaProvisioning(final tcDataProvider provider, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final String secureSocket, final String relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(provider, serverName, Provisioning.integerValue(serverPort), rootContext, principalName, principalPassword, Provisioning.booleanValue(secureSocket), Provisioning.booleanValue(relativeDN), targetLanguage, targetCountry, targetTimeZone, feature, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   account
  /**
   ** Lookups an account in the specified Server Hierarchy.
   ** <p>
   ** Note:
   ** Normally we are always declaring such methods as final but OIM cannot
   ** access such a method with the Reflection API if the method is in an
   ** abstract class,
   **
   ** @param  hierarchyDN        the distinguihsed name of the Direcctory
   **                            Hierarchy to lookup the account within and
   **                            specified in the process form.
   **
   ** @return                    the distinguished name of the account or an
   **                            empty String if the account does not exists.
   */
  public String account(final String hierarchyDN) {
    return super.lookup(hierarchyDN, DirectoryConstant.ACCOUNT_OBJECT_PREFIX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeConnector (overridden)
  /**
   ** Initalize the connector capabilities.
   **
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the LDAP Server.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
   ** @param  targetLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   ** @param  targetCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   ** @param  targetTimeZone     use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **
   ** @return                    the instance of the connector capabilities
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  @Override
  protected DirectoryConnector initializeConnector(final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature)
    throws TaskException {

    return new DirectoryConnector(this, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, targetLanguage, targetCountry, targetTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureParentRDN
  /**
   ** Ensures that a valid RDN is used by operations
   **
   ** @param  parentRDN          the name of the Organization DN which is
   **                            selected in the process data form.
   **                            <br>
   **                            It can be either empty or the OU name selected.
   **
   ** @return                    a valid rdn
   */
  protected String ensureParentRDN(final String parentRDN, final String defaultContainer) {
    // If rdn is empty then pass cn=users as hierarchy name
    return (StringUtility.isEmpty(parentRDN)) ? defaultContainer : parentRDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  updateAttribute
  /**
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to be mofified.
   ** @param  attributeName      name of the attribute to modify.
   ** @param  attributeValue     value of the attribute.
   **
   ** @return                    an appropriate response message.
   */
  protected String updateAttribute(final String entryDN, final String attributeName, String attributeValue) {
    String method = "updateAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    String responseCode = SUCCESS;
    final DirectoryConnector connector = this.connector();
    try {
      final Attributes attributes = new BasicAttributes(false);
      attributes.put(new BasicAttribute(attributeName, attributeValue));

      final String superiorDN = connector.normalizePath(DirectoryConnector.superiorDN(entryDN));
      final String relativeDN = DirectoryConnector.entryRDN(entryDN);
      connector.connect(superiorDN);
      connector.attributesModify(relativeDN, attributes);
    }
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    finally {
      connector.disconnect();
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addToRole
  /**
   ** Adds an account to a role.
   **
   ** @param  account            the distinguished name of the account to be
   **                            removed as member from the role specified by
   **                            the distinguished name <code>role</code>.
   **                            This distinguished name might be trimmed to the
   **                            root context the connector belongs to.
   ** @param  role               the fullqualified distinguished name of the
   **                            role selected in the process form.
   **                            This DN might be encoded as an Oracle Identity
   **                            Manager Entitlement hence unescaping has to be
   **                            applied on that string.
   **
   ** @return                    an appropriate response message.
   */
  protected String addToRole(final String account, final String role) {
    final String method = "addToRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    // build the fullqualified DN of the account
    final String accountDN = denormalizePath(account);
    // transform the role name to its physical form if required
    final String roleDN    = this.connector().entitlementPrefixRequired() ? DirectoryConnector.unescapePrefix(role) : role;
    // set the response code in an optimistic manner
    String responseCode    = SUCCESS;
    try {
      // check if we have to solve conflicts on the assigment
      if (existsAttribute(roleDN, DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT, accountDN)) {
        warning(method, "Can't assign account to role!. Account \"" + accountDN + "\" is already member of Enterprise Role \"" + roleDN + "\"");
      }
      else {
        // add the entry to the container
        connector().connect();
        connector().attributesAdd(roleDN, new BasicAttributes(DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT, accountDN, true));
      }
    }
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
      connector().disconnect();
    }
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeFromRole
  /**
   ** Removes an account from a role.
   **
   ** @param  account            the distinguished name of the account to be
   **                            removed as member from the role specified by
   **                            the distinguished name <code>role</code>.
   **                            This distinguished name might be trimmed to the
   **                            root context the connector belongs to.
   ** @param  role               the distinguished name of the role selected in
   **                            the process form.
   **                            This DN might be encoded as an Oracle Identity
   **                            Manager Entitlement hence unescaping has to be
   **                            applied on that string.
   **
   ** @return                    an appropriate response message
   */
  protected String removeFromRole(final String account, final String role) {
    String method = "removeFromRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    // build the fullqualified DN of the account
    final String accountDN = denormalizePath(account);
    // transform the role name to its physical form if required
    final String roleDN    = this.connector().entitlementPrefixRequired() ? DirectoryConnector.unescapePrefix(role) : role;
    // set the response code in an optimistic manner
    String responseCode    = SUCCESS;
    try {
      // check if we have to solve conflicts on the assigment
      if (existsAttribute(roleDN, DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT, accountDN)) {
        // remove the entry from the container
        connector().connect();
        connector().attributesDelete(roleDN, new BasicAttributes(DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT, accountDN, true));
      }
      else
        warning(method, "Can't remove account from role!. Account \"" + accountDN + "\" is not member of Enterprise Role \"" + roleDN + "\"");
    }
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
      connector().disconnect();
    }
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renameAccount
  /**
   ** Rename Account from oldAccountName to newAccountName.
   ** <p>
   ** On the process form is stored only a account name, before a rename can be
   ** triggerd we have to get full DN of the account. Method update role
   ** membership in the all enterprise roles and then rename account on the
   ** mapping.
   **
   ** @param  mappingDN          the current container where the account was
   **                            created.
   ** @param  accountDN          the new account DN.
   ** @param  oldAccountName     the old account name.
   ** @param  newAccountName     the new account name.
   **
   ** @return                    an appropriate response message
   */
  protected String renameAccount(final String mappingDN, final String accountDN, final String oldAccountName, final String newAccountName) {
    String method = "renameAccount";
    trace(method, SystemMessage.METHOD_ENTRY);

    String responseCode = SUCCESS;
    try {
      final String superiorDN   = DirectoryConnector.superiorDN(mappingDN);
      final String oldAccountDN = findAccountDN(mappingDN);
      final String newAccountDN = denormalizePath(accountDN);
      // Update referecnces
      responseCode = updateMembership(superiorDN, oldAccountDN, newAccountDN);
      // Update orcldbdistinguishedname (Account reference)
      responseCode = updateAttribute(mappingDN, this.connector().enterpriseSchemaAccount(), newAccountDN);
    }
    catch (NamingException e) {
      responseCode = e.getExplanation();
    }
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete a <code>Mapping</code> in the target system.
   ** <p>
   ** Before a <code>Mapping</code> is deleted all references from the roles are
   ** removed.
   **
   ** @param  mappingDN          the fullqualified distinguished name of the
   **                            schema mapping that to be deleted.
   ** @param  deleteLeafNode     whether or not the account and its leaf nodes
   **                            should be deleted on the target system.
   **
   ** @return                    an appropriate response message
   */
  protected String delete(final String mappingDN, final boolean deleteLeafNode) {
    final String method = "delete";
    trace(method, SystemMessage.METHOD_ENTRY);

    String responseCode = SUCCESS;
    // Get a parent DN form the mappingDN
    final String domainDN = DirectoryConnector.superiorDN(mappingDN);
    try {
      // Find a LDAP accout which is removed from the mapping
      String accountDN = findAccountDN(mappingDN);

      // Delete all account memberships from enterprise roles
      deleteMembership(domainDN, accountDN);

      // Delete mapping
      responseCode = delete(domainDN, mappingDN, deleteLeafNode);
    }
    catch (NamingException e) {
      responseCode = e.getExplanation();
    }
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findAccountDN
  /**
   ** Find a accoundDN based on the mapping DN.
   **
   ** @param  mappingDN          the fullqualified distinguished name of the
   **                            schema mapping the existing account will be
   **                            returned for.
   **
   ** @return                    the fullqualified distinguished name of the
   **                            account DN.
   **
   ** @throws NamingException    if a naming exception was encountered while
   **                            retrieving the value.
   ** @throws DirectoryException in case the search operation for the
   **                            distinguished name of the account cannot be
   **                            performed.
   */
  protected String findAccountDN(String mappingDN)
    throws NamingException
    ,      DirectoryException {

    // find a LDAP account which need to be removed from all enterprise role
    // references
    final String searchFilterAccount = "(objectclass=orclDBEntryLevelMapping)";
    final String returningAccount[]  = { this.connector().enterpriseSchemaAccount() };
    final List<SearchResult> result  = connector().search(mappingDN, searchFilterAccount, DirectoryConstant.SCOPE_OBJECT, returningAccount);
    return result.get(0).getAttributes().get(this.connector().enterpriseSchemaAccount()).get().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteMembership
  /**
   ** Deletes all account memberships from enterprise roles.
   **
   ** @param  domainDN           the FQDN of the database domain.
   ** @param  accountDN          the FQDN of the User Account.
   **
   ** @throws DirectoryException in case the search operation for enterprise
   **                            roles cannot be performed.
   */
  protected void deleteMembership(final String domainDN, final String accountDN)
    throws DirectoryException {

    // retrieve a list of the enterprise roles
    final List<SearchResult> roles = enterpriseRoles(domainDN, accountDN);
    // Remove reference from the all Enterprise Security Roles
    for (SearchResult role : roles) {
      final String roleDN = role.getNameInNamespace();
      deleteMultiValueAttribute(roleDN, DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT, accountDN);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateMembership
  /**
   ** Update  userAccount membership in the all Enterprise Roles.
   **
   ** @param  domainDN           the container where a Enterprise Roles exists.
   ** @param  oldUserAccountDN   the fullqualified distinguished name of the old
   **                            account.
   ** @param  newUserAccountDN   the fullqualified distinguished name of the new
   **                            account.
   **
   ** @return                    an appropriate response message
   **
   ** @throws DirectoryException in case the search operation for enterprise
   **                            roles cannot be performed.
   */
  protected String updateMembership(String domainDN, String oldUserAccountDN, String newUserAccountDN)
    throws DirectoryException {

    // Return list of the enterprise roles
    String method = "updateMembership";
    trace(method, SystemMessage.METHOD_ENTRY);

    String responseCode = SUCCESS;
    try {
      // retrieve a list of the enterprise roles
      final List<SearchResult> roles = enterpriseRoles(domainDN, oldUserAccountDN);
      for (SearchResult role : roles) {
        final String roleDN = role.getNameInNamespace();
        responseCode = updateMultiValueAttribute(roleDN, DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT, oldUserAccountDN, newUserAccountDN);
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseRoles
  /**
   ** Return list of the all Enterprise Roles linked with a UserAccount.
   **
   ** @param  domainDN           the container where a search will be executed.
   **                            This distinguished name might be trimmed to the
   **                            root context the connector belongs to.
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account the membership has to be returned for.
   **
   ** @return                    the {@link List} of the all Enterprise Roles
   **                            linked with a UserAccount.
   **
   ** @throws DirectoryException in case the search operation for enterprise
   **                            roles cannot be performed.
   */
  protected List<SearchResult> enterpriseRoles(final String domainDN, final String accountDN)
    throws DirectoryException {

    // build the search filter to get all the enterprise roles of the domain the
    // account is member of
    final StringBuilder searchFilter = new StringBuilder();
    searchFilter.append("(&");
    searchFilter.append(DirectoryConnector.composeFilter(connector().objectClassName(), DirectoryConstant.ENTERPRISE_ROLE_OBJECT_CLASS_DEFAULT));
    searchFilter.append(DirectoryConnector.composeFilter(DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT, accountDN));
    searchFilter.append(")");

    final String returning[] = { DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT };
    return connector().search(domainDN, searchFilter.toString(), DirectoryConstant.SCOPE_SUBTREE, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findChildFormKey
  /**
   ** Remove all entries from the child table
   **
   ** @param  processKey         the Process Instance Key
   ** @param  childForm          the name of the child form to clear
   **
   ** @return                    the Form Definition Key for the specified
   **                            parameter or <code>0</code> if no child form
   **                            exists.
   **
   ** @throws TaskException      if the operation fails.
   */
  protected String cleanChildForm(long processKey, String childForm)
    throws TaskException {

    final String method = "findChildFormKey";
    trace(method, SystemMessage.METHOD_ENTRY);

    final tcFormInstanceOperationsIntf formInstanceFacade = this.formInstanceFacade();
    try {
      long formKey = formInstanceFacade.getProcessFormDefinitionKey(processKey);
      tcResultSet child = formInstanceFacade.getChildFormDefinition(formKey, formInstanceFacade.getProcessFormVersion(processKey));
      int childCount = child.getRowCount();
      for (int i = 0; i < childCount; i++) {
        child.goToRow(i);
        if (childForm.equalsIgnoreCase(child.getStringValue(FormDefinition.NAME))) {
          long childKey = child.getLongValue(FormDefinition.CHILD_KEY);
          tcResultSet childData = formInstanceFacade.getProcessFormChildData(childKey, processKey);
          for (int j = 0; j < childData.getRowCount(); j++) {
            childData.goToRow(j);
            long rowKey = childData.getLongValue(childForm + "_KEY");
            formInstanceFacade.removeProcessFormChildData(childKey, rowKey);
          }
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      formInstanceFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return SUCCESS;
  }

  /**
   * Return column of the child table as list of the strings
   * @param processKey        Database process key
   * @param childForm         Name of the Child table
   * @param childFormColumn   Name of the child table column
   * @return                  All data from one child table column
   * @throws TaskException    if the operation fails.
   */
  protected List<String> getChildFormData(long processKey, String childForm, String childFormColumn)
    throws TaskException {

    final String method = "findChildFormKey";
    trace(method, SystemMessage.METHOD_ENTRY);
    List<String> childColumnData = new ArrayList<String>();
    final tcFormInstanceOperationsIntf formInstanceFacade = this.formInstanceFacade();
    try {
      long formKey = formInstanceFacade.getProcessFormDefinitionKey(processKey);
      tcResultSet child = formInstanceFacade.getChildFormDefinition(formKey, formInstanceFacade.getProcessFormVersion(processKey));
      int childCount = child.getRowCount();
      for (int i = 0; i < childCount; i++) {
        child.goToRow(i);
        if (childForm.equalsIgnoreCase(child.getStringValue(FormDefinition.NAME))) {
          long childKey = child.getLongValue(FormDefinition.CHILD_KEY);
          tcResultSet childData = formInstanceFacade.getProcessFormChildData(childKey, processKey);
          for (int j = 0; j < childData.getRowCount(); j++) {
            childData.goToRow(j);
            childColumnData.add(childData.getStringValue(childFormColumn));
          }
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      formInstanceFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return childColumnData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getKey
  /**
   ** Search in the lookup based on the value and return key.
   **
   ** @param  lookup             contained a key value pair
   ** @param  searchValue        Value based on which we are searching
   **
   ** @return                     key
   */
  protected String getKey(final AbstractLookup lookup, final String searchValue) {
    Set<String> keys = lookup.keySet();
    Iterator<String> i = keys.iterator();
    while (i.hasNext()) {
      String key = i.next();
      String value = (String)lookup.get(key);
      if (value != null && value.equals(searchValue)) {
        return key;
      }
    }
    return null;
  }
}