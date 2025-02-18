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

    File        :   DatabaseProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2011-03-01  DSteding    First release version
*/

package oracle.iam.identity.eus.service.provisioning;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseProvisioning</code> implements the base functionality of a
 ** for the Oracle Identity Adapter Factory which handles data delivered to a
 ** Directory Service that provides Enterprise User Security capabilities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseProvisioning extends SchemaProvisioning {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseProvisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   ** @param  instance           the system identifier of the
   **                            <code>IT Resource</code> used by this adapter
   **                            to establish a connection to the LDAP Server.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public DatabaseProvisioning(final tcDataProvider provider, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(provider, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseProvisioning</code> task adapter.
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
  public DatabaseProvisioning(final tcDataProvider provider, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final String secureSocket, final String relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(provider, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, targetLanguage, targetCountry, targetTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookups an <code>Enterprise Schema Mapping</code> in the
   ** <code>Enterprise Domain</code>.
   **
   ** @param  databaseDN         the distinguihsed name of the Enterprise
   **                            Database to lookup the
   **                            <code>Database Schema Mapping</code> within and
   **                            specified in the process form.
   **
   ** @return                    the distinguished name of the account or an
   **                            empty String if the account does not exists.
   */
  public String find(final String databaseDN) {
    return super.lookup(databaseDN, DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_PREFIX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create an <code>Database Schema Mapping</code> in the Enterprise Domain
   ** where the specfied distinguished name belongs to.
   **
   ** @param  databaseDN         the distinguihsed name of the Enterprise Database
   **                            to create the <code>Database Schema Mapping</code> and
   **                            specified in the process form.
   ** @param  accountDN          the distinguihsed name of the account to create
   **                            the <code>Database Schema Mapping</code> for
   **                            and specified in the process form.
   **
   ** @return                    an appropriate response message
   */
  public String create(final String databaseDN, String accountDN) {
    // expand the attribute mapping with the appropriate entry
    this.data().put(this.connector().feature().enterpriseSchemaAccount(), denormalizePath(accountDN));
    this.data().remove(this.connector().accountObjectPrefix());
    // forward to the superclass to create the entry
    return super.create(ensureParentRDN(databaseDN), DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_PREFIX, DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete a <code>Database Schema Mapping</code> from the Enterprise
   ** Database where the specfied distinguished name belongs to.
   **
   ** @param  accountDN          the fullqualified distinguished name of the
   **                            schema mapping to.
   ** @param  deleteLeafNode     whether or not the account and its leaf nodes
   **                            should be deleted on the target system.
   **
   ** @return                    an appropriate response message
   */
  public String delete(final String accountDN, final boolean deleteLeafNode) {
    final String superiorDN = normalizePath(DirectoryConnector.superiorDN(accountDN));
    return super.delete(ensureParentRDN(superiorDN), accountDN, deleteLeafNode);
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
  // Method:   renameAccount
  /**
   ** Rename Account from the all oldAccountName to the newAccountName.
   ** <p>
   ** On the process form is stored only a account name, before a rename can be
   ** triggerd we have to get full DN of the account.
   ** <p>
   ** Method update role membership in the all enterprise roles and then rename
   ** account on the mapping.
   **
   ** @param  mappingDN          Curren container where is account created
   ** @param  accountDN          New account DN
   ** @param  oldAccountName     Old account name
   ** @param  newAccountName     New accoutt name
   **
   ** @return                    an appropriate response message
   */
  public String renameAccount(final String mappingDN, String accountDN, final String oldAccountName, final String newAccountName) {
    return super.renameAccount(mappingDN, accountDN, oldAccountName, newAccountName);
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
    return super.ensureParentRDN(parentRDN, connector().oracleContextContainer());
  }
}