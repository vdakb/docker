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

    File        :   RoleProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Tomas.Sebo@oracle.com

    Purpose     :   This file implements the class
                    RoleProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2011-03-01  TSebo       First release version
*/

package oracle.iam.identity.eus.service.provisioning;

import java.util.List;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;

////////////////////////////////////////////////////////////////////////////////
// class RoleProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>RoleProvisioning</code> implements the base functionality of a
 ** for the Oracle Identity Adapter Factory which handles data delivered to a
 ** Directory Service that provides Enterprise User Security capabilities.
 **
 ** @author  Tomas.Sebo@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class RoleProvisioning extends SchemaProvisioning {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleProvisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   ** @param  instance           the system identifier of the
   **                            <code>IT Resource</code> used by this adapter
   **                            to establish a connection to the LDAP Server.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public RoleProvisioning(final tcDataProvider provider, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(provider, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleProvisioning</code> task adapter.
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
  public RoleProvisioning(final tcDataProvider provider, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final String secureSocket, final String relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(provider, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, targetLanguage, targetCountry, targetTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete all enterprise roles from the child table.
   **
   ** @param processKey          the Process Instance Key for the procecc form.
   ** @param childForm           the Name of the child form e.g. UD_EUS_ROR.
   **
   ** @return                    the status of the operation
   */
  public String delete(final String processKey, final String childForm) {
    try {
      return cleanChildForm(Long.parseLong(processKey), childForm);
    }
    catch (TaskException e) {
      return e.code();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renameAccount
  public String renameAccount(final String processKey, String newAccountDN, final String oldAccountName, final String oldHierarchieName) {
    final String method = "renameAccount";
    trace(method, SystemMessage.METHOD_ENTRY);

    String responseCode = SUCCESS;
    // Construct the New and Old Account DN
    String oldAccountDN = connector().accountObjectPrefix() + "=" + oldAccountName + "," + oldHierarchieName;
    if (connector().isDistinguishedNameRelative()) {
      oldAccountDN = denormalizePath(oldAccountDN);
      newAccountDN = denormalizePath(newAccountDN);
    }
    if (!connector().distinguishedNameCaseSensitive()) {
      oldAccountDN = oldAccountDN.toLowerCase();
      newAccountDN = newAccountDN.toLowerCase();
      ;
    }
    // Update OldAccountDN t the NewAccountDN for all enterprise roles
    try {
      AbstractLookup lookup = new AbstractLookup(this, "EUS.Role");
      //Get list of the all Enterpriser Roles assigned to the user
      List<String> enterpriseRole = getChildFormData(Long.parseLong(processKey), "UD_EUS_ROR", "UD_EUS_ROR_NAME");
      for (String roleName : enterpriseRole) {
        //Convert roleName to the roleDN
        String roleNameDN = getKey(lookup, roleName);
        // Update account in the particular Enterprise Roles
        responseCode = updateMembership(roleNameDN, oldAccountDN, newAccountDN);
      }
    }
    catch (TaskException e) {
      return e.code();
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }
}