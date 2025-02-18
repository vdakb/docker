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

    File        :   GenericProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    GenericProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.provisioning;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.naming.directory.BasicAttribute;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.ldap.DirectoryError;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class GenericProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>GenericProvisioning</code> acts as the service end point for
 ** the Oracle Identity Manager to provision generic entries to a
 ** Directory Service.
 ** <br>
 ** This is wrapper class has methods for operations like create entry, modify
 ** entry, delete entry etc.
 ** <br>
 ** This class internally calls {@link DirectoryConnector} to talk to the target
 ** system and returns appropriate message code.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class GenericProvisioning extends Provisioning {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GenericProvisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   ** @param  instance           the system identifier of the
   **                            <code>IT Resource</code> used by this adapter
   **                            to establish a connection to the LDAP Server.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public GenericProvisioning(final tcDataProvider provider, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(provider, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GenericProvisioning</code> task adapter.
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
  public GenericProvisioning(final tcDataProvider provider, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final String secureSocket, final String relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(provider, serverName, Provisioning.integerValue(serverPort), rootContext, principalName, principalPassword, Provisioning.booleanValue(secureSocket), Provisioning.booleanValue(relativeDN), targetLanguage, targetCountry, targetTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookups an <code>Entry</code> in the target system.
   **
   ** @param  parentDN           the distinguihsed name of the superordinated
   **                            entry to lookup the entry within and specified
   **                            in the process form.
   **                            It can be either empty or the entry name
   **                            selected.
   ** @param  entryPrefix        the server side prefix of the entry to lookup.
   ** @param  entryName          the name of the entry to lookup.
   **
   ** @return                    the distinguished name of the entry or an empty
   **                            String if the entry does not exists.
   */
  public final String find(final String parentDN, final String entryPrefix, final String entryName) {
    return super.lookup(parentDN, entryPrefix, entryName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create an <code>Organization</code> in the target system.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  parentDN           the distinguihsed name of the parent branch
   **                            to create the organization within and specified
   **                            in the process form.
   **                            It can be either empty or the OU name selected.
   ** @param  entryPrefix        the appropriate object entry prefix.
   ** @param  entryClass         the appropriate object class.
   **                            If the object class contains more than one
   **                            entry split the entries by the configured multi
   **                            value separator.
   **
   ** @return                    an appropriate response message.
   */
  public final String create(final Integer taskInstance, final String parentDN, final String entryPrefix, final String entryClass) {
    // Create the object classes an entry should be assigned to
    final BasicAttribute  objectClass = new BasicAttribute(connector().objectClassName());
    final StringTokenizer tokenizer   = new StringTokenizer(entryClass, connector().multiValueSeparator());
    while (tokenizer.hasMoreTokens())
      objectClass.add(tokenizer.nextToken());

    return super.create(taskInstance, ensureParentRDN(parentDN), entryPrefix, objectClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete an <code>Entry</code> in the target system.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to delete.
   ** @param  deleteLeafNode     whether or not the entry and its leaf nodes
   **                            shall be deleted on the target system.
   **
   ** @return                    an appropriate response message.
   */
  public final String delete(final Integer taskInstance, final String entryDN, final boolean deleteLeafNode) {
    final String superiorDN = normalizePath(DirectoryConnector.superiorDN(entryDN));
    return super.delete(ensureParentRDN(superiorDN), entryDN, deleteLeafNode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Entry</code> in the target system.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to modify.
   **
   ** @return                    an appropriate response message.
   */
  public final String modify(final Integer taskInstance, final String entryDN) {
    return super.modify(taskInstance, entryDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Rename an <code>Entry</code> from one rdn in the directory tree to
   ** another.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  parentDN           the fullqualified distinguished name of the
   **                            superordinated entry of the entry to rename.
   ** @param  entryPrefix        the appropriate object entry prefix.
   ** @param  entryName          the name the existing entry to rename.
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
   ** @return                    a appropriate response message.
   */
  public final String rename(final Integer taskInstance, final String parentDN, final String entryPrefix, final String entryName, final String attributeFilter) {
    return super.rename(this.connector().entitlementPrefixRequired() ? DirectoryConnector.unescapePrefix(parentDN) : parentDN, entryPrefix, entryName, (String)data().remove(entryPrefix), attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   move
  /**
   ** Moves an <code>Organization</code> from one point in the directory tree to
   ** another
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to move.
   ** @param  targetDN           holds the name of the entry path where the
   **                            the <code>entryDN</code> has to be moved to.
   **
   ** @return                    a appropriate response message.
   */
  public final String move(final Integer taskInstance, final String entryDN, final String targetDN) {
    return super.move(entryDN, targetDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables a <code>Organization</code> in the target system.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to enable.
   ** @param  entryControlName   the <code>Entry Control Attribute</code> name
   ** @param  entryControlValue  the <code>Entry Control Attribute</code> value
   **
   ** @return                    an appropriate response message
   */
  public final String enable(final Integer taskInstance, final String entryDN, final String entryControlName, final String entryControlValue) {
    return super.maskAttribute(entryDN, entryControlName, entryControlValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables a <code>Organization</code> in the target system.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to disable.
   ** @param  entryControlName   the <code>Entry Control Attribute</code> name
   ** @param  entryControlValue  the <code>Entry Control Attribute</code> value
   **
   ** @return                    an appropriate response message.
   */
  public final String disable(final Integer taskInstance, final String entryDN, final String entryControlName, final String entryControlValue) {
    return super.maskAttribute(entryDN, entryControlName, entryControlValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates an entry in the target system.
   ** <p>
   ** Use this method if there is a group update of fields. This will be useful
   ** when a set of attributes have to updated together.
   **
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry where the attribute should be added to.
   ** @param  newValues          the key/value-pair mapping of attributes
   **                            containing the new values.
   ** @param  oldValues          the key/value-pair mapping of attributes
   **                            containing the old values.
   **
   ** @return                    an appropriate response message.
   */
  public final String update(final Long processInstance, final Integer taskInstance, final String entryDN, final Map<String, Object> newValues, Map<String, Object> oldValues) {
    final Map<String, Object> changes = new HashMap<String, Object>();
    try {
      final Map<String, String> descriptor = describeForm(processInstance);
      for (String cursor : newValues.keySet())
        changes.put(descriptor.get(cursor), newValues.get(cursor));
    }
    catch (TaskException e) {
      return e.code();
    }
    return super.modify(taskInstance, entryDN, this.descriptor.attributeMapping().filterByEncoded(changes));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMultiValueAttribute
  /**
   ** Adds an entry to a multi-value attribute at an organization.
   **
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry where the attribute should be added to.
   ** @param  attributeName      the name of the attribute that has to be
   **                            added.
   ** @param  attributeValue     the value of the attribute has to be added.
   **
   ** @return                    an appropriate response message.
   */
  public final String addMultiValueAttribute(final Integer taskInstance, final String entryDN, final String attributeName, final String attributeValue) {
    return super.addMultiValueAttribute(entryDN, attributeName, attributeValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateMultiValueAttribute
  /**
   ** Modifies an entry from a multi-value attribute from an organization.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry where the attribute should be modified.
   ** @param  attributeName      the name of the attribute that has to be
   **                            modified.
   ** @param  oldAttributeValue  the old value of the attribute has to be
   **                            modified.
   ** @param  newAttributeValue  the new value of the attribute has to be
   **                            modified.
   **
   ** @return                    an appropriate response message.
   */
  public final String updateMultiValueAttribute(final Integer taskInstance, final String entryDN, final String attributeName, final String oldAttributeValue, final String newAttributeValue) {
    return super.updateMultiValueAttribute(entryDN, attributeName, oldAttributeValue, newAttributeValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteMultiValueAttribute
  /**
   ** Deletes an entry from a multi-value attribute from an organization.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry where the attribute should be removed
   **                            from.
   ** @param  attributeName      the name of the attribute that has to be
   **                            removed.
   ** @param  attributeValue     the value of the attribute has to be removed.
   **
   ** @return                    an appropriate response message.
   */
  public final String deleteMultiValueAttribute(final Integer taskInstance, final String entryDN, final String attributeName, final String attributeValue) {
    return super.deleteMultiValueAttribute(entryDN, attributeName, attributeValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateAttributes
  /**
   ** Modifies one or more attributes of an <code>Organization</code> in the
   ** Target System for the specified distinguished name
   ** <code>organizationDN</code>.
   ** <p>
   ** This method expects the initialized mapping of attributes that has to be
   ** changed.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to change.
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
   ** @return                    an appropriate response message.
   */
  public final String updateAttributes(final Integer taskInstance, final String entryDN, final String attributeFilter) {
    return super.updateAttribute(entryDN, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addToEntry
  /**
   ** Adds an <code>Entry</code> as a member to another <code>Entry</code>.
   ** <p>
   ** A pre condition check is done in this method to validate if the
   ** <code>Entry</code> is a member of the other <code>Entry</code>. If
   ** <code>Entry</code> is not a member of the other <code>Entry</code> the
   ** <code>responseCode</code> returnd to the process task is
   ** {@link DirectoryError#OBJECT_ALREADY_ASSIGNED}.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the other entry
   **                            specified by the full qualified distinguished
   **                            name <code>targetDN</code>.
   ** @param  containerDN        the fullqualified distinguished name of the
   **                            target selected in the process form
   ** @param  containerClass     the objectClass of the container entry the
   **                            entry should be added to.
   ** @param  containerPrefix    the object prefix of the container entry the
   **                            entry should be added to.
   ** @param  memberAttribute    the name of the attribute of the LDAP object
   **                            specified by <code>containerDN</code> where the
   **                            <code>entryDN</code> has to be added to.
   **
   ** @return                    an appropriate response message.
   */
  public final String addToEntry(final Integer taskInstance, final String entryDN, final String containerDN, final String containerClass, final String containerPrefix, final String memberAttribute) {
    try {
      final Reference reference = createEntryReference(containerClass, containerPrefix, memberAttribute, null, null);
      return addToEntry(entryDN, containerDN, reference);
    }
    // when the operation fails in general
    catch (DirectoryException e) {
      return e.code();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeFromEntry
  /**
   ** Removes an <code>Entry</code> as a member from another <code>Entry</code>.
   ** <p>
   ** A pre condition check is done in this method to validate if the
   ** <code>Entry</code> is a member of the other <code>Entry</code>. If
   ** <code>Entry</code> is not a member of the other <code>Entry</code> the
   ** <code>responseCode</code> returnd to the process task is
   ** {@link DirectoryError#OBJECT_ALREADY_REMOVED}.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the other entry
   **                            specified by the full qualified distinguished
   **                            name <code>targetDN</code>.
   ** @param  containerDN        the fullqualified distinguished name of the
   **                            target selected in the process form
   ** @param  containerClass     the objectClass of the container entry the
   **                            entry should be added to.
   ** @param  containerPrefix    the object prefix of the container entry the
   **                            entry should be added to.
   ** @param  memberAttribute    the name of the attribute of the LDAP object
   **                            specified by <code>containerDN</code> where the
   **                            <code>entryDN</code> has to be added to.
   **
   ** @return                    an appropriate response message.
   */
  public final String removeFromEntry(final Integer taskInstance, final String entryDN, final String containerDN, final String containerClass, final String containerPrefix, final String memberAttribute) {
    try {
      final Reference reference = createEntryReference(containerClass, containerPrefix, memberAttribute, null, null);
      return removeFromEntry(entryDN, containerDN, reference);
    }
    // when the operation fails in general
    catch (DirectoryException e) {
      return e.code();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureParentRDN
  /**
   ** Ensures that a valid RDN is used by operations
   *
   ** @param  parentRDN         the name of the entry DN which is selected in
   **                           the process data form.
   **                           <br>
   **                           It can be either empty or the context name
   **                           selected.
   **
   ** @return                   a valid relative distinguished name.
   */
  protected final String ensureParentRDN(final String parentRDN) {
    // If rdn is empty then pass cn=users as hierarchy name
    return super.ensureParentRDN(parentRDN, this.connector().genericContainer());
  }
}