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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   AccountProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.provisioning;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class AccountProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountProvisioning</code> acts as the service end point for the
 ** Oracle Identity Manager to provision account properties to a Directory
 ** Service.
 ** <br>
 ** This is wrapper class has methods for account operations like create account,
 ** modify account, delete account etc.
 ** <br>
 ** This class internally calls {@link DirectoryConnector} to talk to the target
 ** system and returns appropriate message code.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class AccountProvisioning extends Provisioning {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountProvisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   ** @param  instance           the system identifier of the
   **                            <code>IT Resource</code> used by this adapter
   **                            to establish a connection to the LDAP Server.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public AccountProvisioning(final tcDataProvider provider, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(provider, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountProvisioning</code> task adapter.
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
  public AccountProvisioning(final tcDataProvider provider, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final String secureSocket, final String relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(provider, serverName, Provisioning.integerValue(serverPort), rootContext, principalName, principalPassword, Provisioning.booleanValue(secureSocket), Provisioning.booleanValue(relativeDN), targetLanguage, targetCountry, targetTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   denormalizePath
  /**
   ** Forms the basis of building the hierarchical tree to the LDAP object.
   ** Used by connect to build the correct distinguished name.
   **
   ** @param  accountRDN         Contains the elements in the tree, deepest one
   **                            first. The String must be of format
   **                            "Class Type=Object CN,Class Type=Object CN"
   **                            where:
   **                            <ul>
   **                              <li>Class Type is the objects class type ("CN", "OU", ...)
   **                              <li>Object CN is the LDAP objects common name ("dsteding", "finance group", ... )
   **                            </ul>
   **                            Basically whatever is assigned to the
   **                            mandatory property "cn" or "ou". e.g.
   **                            <code>CN=Dumbo,OU=Leaders,OU=Elephants</code>
   **
   ** @return                    String of the canonical path (including the
   **                            root context), e.g.
   **                            OU=Users,OU=abc,OU=Companies,DC=thordev,DC=com
   */
  public String denormalizePath(final String accountRDN) {
    return super.denormalizePath(accountRDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookups an <code>Account</code> in the target system.
   **
   ** @param  parentDN           the distinguihsed name of the organizational
   **                            unit to lookup the account within and specified
   **                            in the process form.
   **                            It can be either empty or the OU name selected.
   **
   ** @return                    the distinguished name of the account or an
   **                            empty String if the account does not exists.
   */
  public String find(final String parentDN) {
    return super.lookup(parentDN, DirectoryConstant.ACCOUNT_OBJECT_PREFIX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookups an <code>Account</code> in the target system.
   **
   ** @param  parentDN           the distinguihsed name of the parent branch to
   **                            lookup the account within and specified in the
   **                            process form.
   **                            It can be either empty or the OU name selected.
   ** @param  accountRDN         the relative distinguihsed name of the account
   **                            to lookup the account.
   **
   ** @return                    the distinguished name of the account or an
   **                            empty String if the account does not exists.
   */
  public String find(final String parentDN, final String accountRDN) {
    return super.lookup(parentDN, DirectoryConstant.ACCOUNT_OBJECT_PREFIX, accountRDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create an <code>Account</code> in the target system.
   **
   ** @param  parentDN           the distinguihsed name of the organizational
   **                            unit to create the account within and specified
   **                            in the process form.
   **                            It can be either empty or the OU name selected.
   **
   ** @return                    an appropriate response message
   */
  public String create(final String parentDN) {
    final String accountPasswordAttribute = this.connector().accountPasswordAttribute();
    // check if we the account password is mapped
    if (accountPasswordAttribute != null) {
      // remove always the password field from the mapping it will be set in a
      // separate task.
      // the password can only be set on an existing user. Do achive that a
      // depended provisioning task must be called from Oracle Identity Manager
      // to change the password on the created user
      data().remove(accountPasswordAttribute);
    }

    return super.create(ensureParentRDN(parentDN), DirectoryConstant.ACCOUNT_OBJECT_PREFIX, DirectoryConstant.ACCOUNT_OBJECT_CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete an <code>Account</code> in the target system.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to delete.
   ** @param  deleteLeafNode     whether or not the account and its leaf nodes
   **                            should be deleted on the target system.
   **
   ** @return                    an appropriate response message
   */
  public String delete(final String accountDN, final boolean deleteLeafNode){
    try {
      final Reference reference  = createEntryReference(DirectoryConstant.GROUP_OBJECT_CLASS, DirectoryConstant.GROUP_OBJECT_PREFIX, DirectoryConstant.GROUP_OBJECTMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE_DN);
      final String    superiorDN = normalizePath(DirectoryConnector.superiorDN(accountDN));
      return super.delete(ensureParentRDN(superiorDN), accountDN, reference, deleteLeafNode);
    }
    catch (DirectoryException e) {
      return e.code();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Rename an <code>Account</code> from one point in the directory tree to
   ** another.
   **
   ** @param  parentDN           the fullqualified distinguished name of the
   **                            superordinated entry of the account to rename.
   ** @param  accountName        the name the existing account to rename.
   ** @param  attributeFilter    the names of the attributes where the callee is
   **                            also interrested in.
   **                            <br>
   **                            This is used a filter to pick up only special
   **                            fields from the previous attribute mapping.
   **                            <br>
   **                            <b>Note:</b>
   **                            The given String must separate the fields with
   **                            the character specified in the
   **                            <code>DirectoryConstant</code> of the associated
   **                            {@link DirectoryConnector} by
   **                            <code>Multi Value Separator</code>
   **
   ** @return                    a appropriate response message
   */
  public String rename(final String parentDN, final String accountName, final String attributeFilter) {
    return super.rename(parentDN, DirectoryConstant.ACCOUNT_OBJECT_PREFIX, accountName, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   move
  /**
   ** Moves an <code>Account</code> from one point in the directory tree to
   ** another.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to move.
   ** @param  targetDN           holds the name of the organizational path where
   **                            where the <code>accountDN</code> has to be
   **                            moved to.
   **
   ** @return                    a appropriate response message
   */
  public String move(final String accountDN, final String targetDN) {
    return super.move(accountDN, targetDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables a <code>Account</code> in the target system.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to enable.
   ** @param  entryControlName   the <code>Entry Control Attribute</code> name
   ** @param  entryControlValue  the <code>Entry Control Attribute</code> value
   **
   ** @return                    an appropriate response message
   */
  public String enable(final String accountDN, final String entryControlName, final String entryControlValue) {
    return super.maskAttribute(accountDN, entryControlName, entryControlValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables a <code>Account</code> in the target system.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to disable.
   ** @param  entryControlName   the <code>Entry Control Attribute</code> name
   ** @param  entryControlValue  the <code>Entry Control Attribute</code> value
   **
   ** @return                    an appropriate response message
   */
  public String disable(String accountDN, String entryControlName, final String entryControlValue) {
    return super.maskAttribute(accountDN, entryControlName, entryControlValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unlock
  /**
   ** Unlocks a <code>Account</code> in the target system.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to unlock.
   ** @param  entryControlName   the <code>Entry Control Attribute</code> name
   ** @param  entryControlValue  the <code>Entry Control Attribute</code> value
   **
   ** @return                    an appropriate response message
   */
  public String unlock(final String accountDN, final String entryControlName, final String entryControlValue) {
    return super.maskAttribute(accountDN, entryControlName, entryControlValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lock
  /**
   ** Loacks a <code>Account</code> in the target system.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to lock.
   ** @param  entryControlName   the <code>Entry Control Attribute</code> name
   ** @param  entryControlValue  the <code>Entry Control Attribute</code> value
   **
   ** @return                    an appropriate response message
   */
  public String lock(final String accountDN, final String entryControlName, final String entryControlValue) {
    return super.maskAttribute(accountDN, entryControlName, entryControlValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMultiValueAttribute
  /**
   ** Adds an entry to a multi-value attribute at an account.
   **
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account where the attribute should be added to.
   ** @param  attributeName      the name of the attribute that has to be
   **                            added.
   ** @param  attributeValue     the value of the attribute has to be added
   **
   ** @return                    an appropriate response message
   */
  public final String addMultiValueAttribute(final String accountDN, final String attributeName, final String attributeValue) {
    return super.addMultiValueAttribute(accountDN, attributeName, attributeValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateMultiValueAttribute
  /**
   ** Modifies an entry from a multi-value attribute from an organization.
   **
   ** @param  organizationDN     the fullqualified distinguished name of the
   **                            organization where the attribute should be
   **                            modified.
   ** @param  attributeName      the name of the attribute that has to be
   **                            modified.
   ** @param  oldAttributeValue  the old value of the attribute has to be
   **                            modified.
   ** @param  newAttributeValue  the new value of the attribute has to be
   **                            modified.
   **
   ** @return                    an appropriate response message
   */
  public final String updateMultiValueAttribute(final String organizationDN, final String attributeName, final String oldAttributeValue, final String newAttributeValue) {
    return super.updateMultiValueAttribute(organizationDN, attributeName, oldAttributeValue, newAttributeValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteMultiValueAttribute
  /**
   ** Deletes an entry from a multi-value attribute at an account.
   **
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account where the attribute should be removed
   **                            from.
   ** @param  attributeName      the name of the attribute that has to be
   **                            removed.
   ** @param  attributeValue     the value of the attribute has to be removed.
   **
   ** @return                    an appropriate response message
   */
  public final String deleteMultiValueAttribute(final String accountDN, final String attributeName, final String attributeValue) {
    return super.deleteMultiValueAttribute(accountDN, attributeName, attributeValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateAttribute
  /**
   ** Modifies one or more attributes of an <code>Account</code> in the Target
   ** System for the specified distinguished name <code>accountDN</code>.
   ** <p>
   ** This method expects the initialized mapping of attributes that has to be
   ** changed.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to enable.
   ** @param  attributeFilter    the names of the attributes where the callee is
   **                            interrested in.
   **                            <br>
   **                            This is used a filter to pick up only special
   **                            fields from the previous attribute mapping.
   **                            <br>
   **                            <b>Note:</b>
   **                            The given String must separate the fields with
   **                            the character specified in the
   **                            <code>DirectoryConstant</code> of the associated
   **                            {@link DirectoryConnector} by
   **                            <code>Multi Value Separator</code>
   **
   ** @return                    an appropriate response message
   */
  public String updateAttribute(final String accountDN, final String attributeFilter) {
    return super.updateAttribute(accountDN, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword
  /**
   ** Set the password attrinute of a <code>Account</code> in the target system.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account the password has to be set.
   ** @param  passwordAttribute  the name of the attribute where the password of
   **                            an account has to be set.
   ** @param  namedCharacterSet  the name of a supported charset the password
   **                            should be conterted to.
   **                            <br>
   **                            If <code>null</code> or an empty String is
   **                            passed to this parameter the
   **                            <code>password</code> paraemter is converted to
   **                            an array of bytes.
   **
   ** @return                    an appropriate response message
   */
  public String setPassword(final String accountDN, final String passwordAttribute, final String namedCharacterSet) {
    return super.updatePassword(accountDN, passwordAttribute, namedCharacterSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changePassword
  /**
   ** Modifies the password attrinute of a <code>Account</code> in the target
   ** system.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account the password has to be set.
   ** @param  password           the password of a account to set.
   ** @param  namedCharacterSet  the name of a supported charset the password
   **                            should be conterted to.
   **                            <br>
   **                            If <code>null</code> or an empty String is
   **                            passed to this parameter the
   **                            <code>password</code> paraemter is converted to
   **                            an array of bytes.
   **
   ** @return                    an appropriate response message
   */
  public String changePassword(final String accountDN, final String password, final String namedCharacterSet) {
    return super.updatePassword(accountDN, password, namedCharacterSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addToGroup
  /**
   ** Adds an account to a group.
   **
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to added as member to the group
   **                            specified by the full qualified distinguished
   **                            name <code>groupDN</code>.
   ** @param  groupDN            the fullqualified distinguished name of the
   **                            group selected in the process form
   **
   ** @return                    an appropriate response message
   */
  public String addToGroup(final String accountDN, final String groupDN) {
    return addToEntry(accountDN, groupDN, DirectoryConstant.GROUP_OBJECT_CLASS, DirectoryConstant.GROUP_OBJECT_PREFIX, DirectoryConstant.GROUP_OBJECTMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE_DN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeFromGroup
  /**
   ** Removes an account from a group.
   **
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to be removed as member from the group
   **                            specified by the full qualified distinguished
   **                            name <code>groupDN</code>.
   ** @param  groupDN            the relative distinguished name of the group
   **                            selected in the process form
   **
   ** @return                    an appropriate response message
   */
  public String removeFromGroup(final String accountDN, final String groupDN) {
    return removeFromEntry(accountDN, groupDN, DirectoryConstant.GROUP_OBJECT_CLASS, DirectoryConstant.GROUP_OBJECT_PREFIX, DirectoryConstant.GROUP_OBJECTMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE_DN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addToRole
  /**
   ** Adds an account to a role.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to be added as member to the role
   **                            specified by the full qualified distinguished
   **                            name <code>roleDN</code>.
   ** @param  roleDN             the fullqualified distinguished name of the
   **                            role selected in the process form
   **
   ** @return                    an appropriate response message
   */
  public String addToRole(final String accountDN, final String roleDN) {
    return addToEntry(accountDN, roleDN, DirectoryConstant.ROLE_OBJECT_CLASS, DirectoryConstant.ROLE_OBJECT_PREFIX, DirectoryConstant.ROLE_OBJECTMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_ROLEMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_ROLEMEMBER_ATTRIBUTE_DN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeFromRole
  /**
   ** Removes an account from a role.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            account to be removed as member from the role
   **                            specified by the full qualified distinguished
   **                            name <code>roleDN</code>.
   ** @param  roleDN             the relative distinguished name of the role
   **                            selected in the process form
   **
   ** @return                    an appropriate response message
   */
  public String removeFromRole(final String accountDN, final String roleDN) {
    return removeFromEntry(accountDN, roleDN, DirectoryConstant.ROLE_OBJECT_CLASS, DirectoryConstant.ROLE_OBJECT_PREFIX, DirectoryConstant.ROLE_OBJECTMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_ROLEMEMBER_ATTRIBUTE, DirectoryConstant.ACCOUNT_ROLEMEMBER_ATTRIBUTE_DN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureParentRDN
  /**
   ** Ensures that a valid RDN is used by operations
   *
   ** @param  parentRDN         the name of the Organization DN which is
   **                           selected in the process data form.
   **                           <br>
   **                           It can be either empty or the OU name selected.
   **
   ** @return                   a valid rdn
   */
  protected final String ensureParentRDN(final String parentRDN) {
    // If rdn is empty then pass cn=users as hierarchy name
    return super.ensureParentRDN(parentRDN, this.connector().accountContainer());
  }
}