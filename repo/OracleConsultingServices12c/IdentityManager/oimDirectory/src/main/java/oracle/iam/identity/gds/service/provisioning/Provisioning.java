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

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Provisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.provisioning;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.io.UnsupportedEncodingException;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import javax.naming.ldap.Control;

import com.sun.jndi.ldap.ctl.TreeDeleteControl;

import com.thortech.xl.dataaccess.tcDataProvider;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcTaskNotFoundException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcStaleDataUpdateException;
import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Exceptions.tcAwaitingObjectDataCompletionException;
import Thor.API.Exceptions.tcAwaitingApprovalDataCompletionException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import  oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.ldap.DirectoryName;
import oracle.iam.identity.foundation.ldap.DirectoryError;
import oracle.iam.identity.foundation.ldap.DirectoryFeature;
import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryException;

import oracle.iam.identity.foundation.naming.ProcessInstance;

import oracle.iam.identity.foundation.provisioning.AbstractProvisioningTask;

import oracle.iam.identity.foundation.resource.DirectoryBundle;

import oracle.iam.identity.gds.resource.ProvisioningBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Provisioning
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>Provisioning</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Adapter Factory which handles data
 ** delivered to a LDAP Directory Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class Provisioning extends AbstractProvisioningTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String      LOGGER_CATEGORY = "OCS.GDS.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final DirectoryConnector connector;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Reference
  // ~~~~~ ~~~~~~~~~
  protected class Reference {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected String  containerObjectClass;
    protected String  containerObjectPrefix;
    protected String  containerAttribute;
    protected String  entryAttribute;
    protected boolean entryAttributeDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   ** @param  instance           the system identifier of the
   **                            <code>IT Resource</code> used by this adapter
   **                            to establish a connection to the LDAP Server.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected Provisioning(final tcDataProvider provider, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(provider, LOGGER_CATEGORY);

    // initialize instance attributes
    final DirectoryResource resource = new DirectoryResource(this, instance);
    this.connector = new DirectoryConnector(this, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
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
  protected Provisioning(final tcDataProvider provider, final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    this(provider, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, targetLanguage, targetCountry, targetTimeZone, feature, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
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
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected Provisioning(final tcDataProvider provider, final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature, final String loggerCategory)
    throws TaskException {

    // ensure inheritance
    super(provider, loggerCategory);

    // initialize instance attributes
    this.connector = initializeConnector(serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, targetLanguage, targetCountry, targetTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
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
  protected Provisioning(final tcDataProvider provider, final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final DirectoryFeature feature)
    throws TaskException {

    // ensure inheritance
    this(provider, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, targetLanguage, targetCountry, targetTimeZone, feature, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
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
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected Provisioning(final tcDataProvider provider, final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final DirectoryFeature feature, final String loggerCategory)
    throws TaskException {

    // ensure inheritance
    super(provider, loggerCategory);

    // initialize instance attributes
    this.connector = new DirectoryConnector(this, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, targetLanguage, targetCountry, targetTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connector
  /**
   ** Returns the connector associated with this task.
   **
   ** @return                    the connector associated with this task.
   */
  protected final DirectoryConnector connector() {
    return this.connector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeConnector
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
  protected DirectoryConnector initializeConnector(final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String targetLanguage, final String targetCountry, final String targetTimeZone, final String feature)
    throws TaskException {

    return new DirectoryConnector(this, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, targetLanguage, targetCountry, targetTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationUnitExists
  /**
   ** Checks whether an organization exists when no hierarchy given
   **
   ** @param  organizationName   organization name
   **
   ** @return                    <code>true</code> if the organization exists;
   **                            otherwise <code>false</code>.
   */
  public boolean organizationUnitExists(String organizationName) {
    return this.organizationUnitExists(organizationName, SystemConstant.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationUnitExists
  /**
   ** Checks whether an organization exists when no hierarchy given
   **
   ** @param  organizationName   organization to be searched
   ** @param  parentDN            hierarchical path for the organization
   **
   ** @return                    <code>true</code> if the organization exists in;
   **                            the branch of <code>parentDn</code>; otherwise
   **                            <code>false</code>.
   */
  public boolean organizationUnitExists(final String organizationName, final String parentDN) {
    final String   entryRDN  = DirectoryConnector.composeName(this.connector.organizationObjectPrefix(), organizationName);
    return entryExists(denormalizePath(parentDN), entryRDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryExists
  /**
   ** Checks whether the entry exists or not
   **
   ** @param  containerDN        relative path of an object i.e. userDN
   ** @param  entryID            ID of an entry i.e. an account.
   **
   ** @return                    <code>true</code> if the object exists;
   **                            otherwise <code>false</code>.
   */
  public boolean entryExists(final String containerDN, final String entryID) {
    final String method = "entryExists";
    try {
      // get the full search base dn
      final List<SearchResult> result = this.connector.search(denormalizePath(containerDN), entryID, DirectoryConstant.SCOPE_SUBTREE, null);
      return (!result.isEmpty());
    }
    catch (DirectoryException e) {
      error(method, e.getLocalizedMessage());
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationDN
  /**
   ** Returns the full path of the organization i.e. the full DN of an
   ** organization
   ** @param  organizationName   organization to be searched
   **
   ** @return                    the fullqualified DN of an organization or an
   **                            empty String if the organization does not
   **                            exist.
   **
   ** @throws DirectoryException when the operation fails in general
   */
  public String organizationDN(final String organizationName)
    throws DirectoryException {

    final String method = "organizationDN";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final String[]           returning = { this.connector.organizationObjectPrefix() }; // ou or o
      final String             baseDN    = denormalizePath(SystemConstant.EMPTY).toLowerCase(); // dc=corp,dc=x,dc=com
      final List<SearchResult> vector    = this.connector.search(baseDN, DirectoryConnector.composeName(returning[0], organizationName), DirectoryConstant.SCOPE_SUBTREE, returning);
      if (vector.isEmpty()) { // if the vector is empty
        // organization unit does not exist
        warning(method, "Organisation does not exist");
        return SystemConstant.EMPTY;
      }
      else {
        warning(method, "Organisation already exist");
        // to get the full path of the OU
        final SearchResult entry = vector.get(0);
        // to set the member variable organizationUnitFullPath with the full
        // path e.g. ou=ou9,ou=superusers
        final String entryDN = entry.getName();
        debug(method, "Organisation DN from Directory Service " + entryDN);
        // organization unit already exist
        return entryDN;
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   denormalizePath
  /**
   ** Forms the basis of building the hierarchical tree to the LDAP object.
   ** Used by connect to build the correct distinguished name.
   **
   ** @param  entryRDN           Contains the elements in the tree, deepest one
   **                            first. The String must be of format
   **                            "Class Type=Object CN,Class Type=Object CN"
   **                            where:
   **                            <ul>
   **                              <li>Class Type is the objects class type ("CN", "OU", ...)
   **                              <li>Object CN is the LDAP objects common name ("Dumbo", "finance group", ... )
   **                            </ul>
   **                            Basically whatever is assigned to the
   **                            mandatory property "cn" or "ou". e.g.
   **                            <code>CN=Dumbo,OU=Leaders,OU=Elephants</code>
   **
   ** @return                    String of the canonical path (including the
   **                            root context), e.g.
   **                            OU=Users,OU=abc,OU=Companies,DC=thordev,DC=com
   */
  public String denormalizePath(String entryRDN) {
    return this.connector.denormalizePath(entryRDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizePath
  /**
   ** Remove the root context name from the distinguished name.
   **
   ** @param  distinguishedName  the path of the object (relative or absolute to
   **                            the root context hierarchy)
   **
   ** @return                    the relative distinguished name without the
   **                            root context
   */
  public String normalizePath(String distinguishedName) {
    return this.connector.normalizePath(distinguishedName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAttribute
  /**
   ** Lists the value(s) of a particular object attribute.
   ** <p>
   ** Returns it as a {@link List} to accommodate properties that have a list of
   ** values.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the object
   **                            specified by the full qualified distinguished
   **                            name <code>containerDN</code>.
   ** @param  attributeName      the name of the attribute selected in the
   **                            process form.
   **
   ** @return                    a {@link List} of Strings, consisting of all
   **                            the values this attribute was set to.
   **
   ** @throws DirectoryException if the operation fails
   */
  public List<Object> fetchAttribute(final String entryDN, final String attributeName)
    throws DirectoryException {

    final String method = "fetchAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String superiorDN = normalizePath(DirectoryConnector.superiorDN(entryDN));
    final String entryRDN   = DirectoryConnector.entryRDN(entryDN);

    List<Object> result = null;
    try {
      result = this.connector.fetchAttributeValues(superiorDN, entryRDN, attributeName);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePassword
  /**
   ** Modifies the password attrinute of an <code>Entry</code> in the target
   ** system.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry the password has to be set.
   ** @param  passwordAttribute  the attribute name of the password to resolve
   **                            from the mapped process data.
   ** @param  namedCharacterSet  the name of a supported charset the password
   **                            should be conterted to.
   **                            <br>
   **                            If <code>null</code> or an empty String is
   **                            passed to this parameter the
   **                            <code>password</code> paraemter is converted to
   **                            an array of bytes.
   **
   ** @return                    an appropriate response message.
   */
  public String updatePassword(final String entryDN, final String passwordAttribute, final String namedCharacterSet) {

    final String method = "updatePassword";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode       = SUCCESS;
    try {
      final String superior   = normalizePath(DirectoryConnector.superiorDN(entryDN));
      final String rdn        = DirectoryConnector.entryRDN(entryDN);

      // Encoding the password and putting it in Attributes
      byte[] passwordValue = null; ;
      if (StringUtility.isEmpty(namedCharacterSet))
        passwordValue = ((String)data().get(passwordAttribute)).getBytes();
      else
        passwordValue =  new String("\"" + data().get(passwordAttribute)+  "\"").getBytes(namedCharacterSet);

      // Setting password attribute in Directory Service only if suecure sockets
      // not required to set the password or if required the connector is
      // configured for SSL
      if (!this.connector.passwordOperationSecured() || (this.connector.passwordOperationSecured() && connector.secureSocket())) {
        debug(method, ProvisioningBundle.format(ProvisioningMessage.ENTRY_UPDATE_PASSWORD, entryDN));
        this.connector.connect(superior);

        // Setting password attribute value in Directory Service
        // if the attribute value is not null, then replace the attribute with
        // the value supplied; otherwise if the attribute value is null then
        // delete the attribute
        BasicAttributes attributes = new BasicAttributes(this.connector.accountPasswordAttribute(), passwordValue, true);
        if (passwordValue.length == 0)
          this.connector.attributesDelete(rdn, attributes);
        else
          this.connector.attributesModify(rdn, attributes);
      }
      else {
        error(method, DirectoryBundle.string(DirectoryError.PASSWORD_CHANGE_REQUIRES_SSL));
        responseCode = DirectoryError.PASSWORD_CHANGE_REQUIRES_SSL;
      }
    }
    // when the password encoding is not supported
    catch (UnsupportedEncodingException e) {
      error(method, DirectoryBundle.format(DirectoryError.ENCODING_TYPE_NOT_SUPPORTED, namedCharacterSet));
      responseCode = DirectoryError.ENCODING_TYPE_NOT_SUPPORTED;
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Lookups an <code>Entry</code> in the target system.
   **
   ** @param  parentDN           the distinguihsed name of the organizational
   **                            unit to lookup the organization within and
   **                            specified in the process form.
   **                            It can be either empty or the OU name selected.
   ** @param  entryPrefix        the Server Feature Key to fetch the
   **                            appropriate object entry prefix.
   **
   ** @return                    the distinguished name of the organization or
   **                            an empty String if the organization does not
   **                            exists.
   */
  protected String lookup(final String parentDN, final String entryPrefix) {

    final String method = "lookup";
    // check if the condition of an exisiting object prefix is satisfied
    final String prefix = this.connector.feature().stringValue(entryPrefix);
    if (StringUtility.isEmpty(prefix)) {
      error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, entryPrefix));
      return null;
    }

    return lookup(parentDN, prefix, (String)data().get(prefix));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Lookups an <code>Entry</code> in the target system.
   **
   ** @param  parentDN           the distinguihsed name of the organizational
   **                            unit to lookup the organization within and
   **                            specified in the process form.
   **                            It can be either empty or the OU name selected.
   ** @param  entryPrefix        the server side prefix of the entry to lookup.
   ** @param  entryName          the name of the entry to lookup.
   **
   ** @return                    the distinguished name of the organization or
   **                            an empty String if the organization does not
   **                            exists.
   */
  protected final String lookup(final String parentDN, final String entryPrefix, final String entryName) {
    final String method = "lookup";
    trace(method, SystemMessage.METHOD_ENTRY);

    // check if the condition of an mapped object id is satisfied
    if (StringUtility.isEmpty(entryPrefix)) {
      error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, entryPrefix));
      return null;
    }

    // check if we are able to proceed further
    if (StringUtility.isEmpty(entryName)) {
      error(method, ProvisioningBundle.format(ProvisioningError.MAPPING_PROPERTY_MISSING, entryName));
      return null;
    }

    // search for all entries thats meets the requested conditions
    String objectDN = null;
    try {
      objectDN = this.connector.findEntry(this.connector.entitlementPrefixRequired() ? DirectoryConnector.unescapePrefix(parentDN) : parentDN, entryPrefix, entryName);
    }
    // when the operation fails in general spool out the error code
    catch (DirectoryException e) {
      error(method, e.getLocalizedMessage());
    }
    trace(method, SystemMessage.METHOD_EXIT);

    return objectDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create an <code>Entry</code> in the target system.
   **
   ** @param  parentDN           the distinguihsed name of the organizational
   **                            path to create the entry within and specified
   **                            in the process form.
   **                            It must not be empty.
   ** @param  objectPrefix       the Server Feature Key to fetch the
   **                            appropriate object entry prefix.
   ** @param  objectClass        the Server Feature Key to fetch the
   **                            appropriate object class.
   **
   ** @return                    an appropriate response message.
   */
  protected String create(final String parentDN, final String objectPrefix, final String objectClass) {
    final String method = "create";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String classes = this.connector.feature().stringValue(objectClass);
    // check if we are able to proceed further
    if (StringUtility.isEmpty(classes)) {
      error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, objectClass));
      trace(method, SystemMessage.METHOD_EXIT);
      return DirectoryError.INSUFFICIENT_INFORMATION;
    }

    final String entryPrefix = this.connector.feature().stringValue(objectPrefix);
    // check if we are able to proceed further
    if (StringUtility.isEmpty(entryPrefix)) {
      error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, objectPrefix));
      trace(method, SystemMessage.METHOD_EXIT);
      return DirectoryError.INSUFFICIENT_INFORMATION;
    }

    // Create the object classes an entry should be assigned to
    final BasicAttribute  entryClass = new BasicAttribute(this.connector.objectClassName());
    final StringTokenizer tokenizer  = new StringTokenizer(classes, this.connector.multiValueSeparator());
    while (tokenizer.hasMoreTokens())
      entryClass.add(tokenizer.nextToken());

    return create(parentDN, entryPrefix, entryClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create an <code>Entry</code> in the target system.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  parentDN           the distinguihsed name of the organizational
   **                            path to create the entry within and specified
   **                            in the process form.
   **                            It must not be empty.
   ** @param  entryPrefix        the appropriate object entry prefix.
   ** @param  entryClass         the  appropriate object class.
   **
   ** @return                    an appropriate response message.
   */
  protected String create(final Integer taskInstance, final String parentDN, final String entryPrefix, final BasicAttribute entryClass) {
    final String method = "create";
    trace(method, SystemMessage.METHOD_ENTRY);

    // remove the attribute specifying the relative distinguished name from
    // field mapping and check for not empty
    final String entryName = (String)data().remove(entryPrefix);
    if (StringUtility.isEmpty(entryName)) {
      error(method, ProvisioningBundle.format(DirectoryError.INSUFFICIENT_INFORMATION, entryPrefix));
      trace(method, SystemMessage.METHOD_EXIT);
      return DirectoryError.INSUFFICIENT_INFORMATION;
    }

    // Form a relative distinguished name from local attribute
    // Note: Connector.compositeName escapes the objectID
    final String entryRDN = DirectoryConnector.composeName(entryPrefix, entryName);

    final StringBuilder taskNote   = new StringBuilder();
    final StringBuilder taskReason = new StringBuilder();
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      final Attributes attributes = DirectoryConnector.createAttributes(data());
      attributes.put(entryClass);

      this.connector.connect(this.connector.entitlementPrefixRequired() ? DirectoryConnector.unescapePrefix(parentDN) : parentDN);
      taskNote.append(createTaskDetail(ProvisioningBundle.format(ProvisioningMessage.ENTRY_CREATE, entryRDN, parentDN)));
      this.connector.createEntry(entryRDN, attributes);
      taskNote.append(createTaskDetail(ProvisioningBundle.format(ProvisioningMessage.ENTRY_CREATED, entryRDN, parentDN)));
    }
    // when the operation fails in general return the error code as the
    // response
    catch (TaskException e) {
      taskReason.append(createTaskDetail(ProvisioningBundle.format(ProvisioningError.ENTRY_CREATE, entryRDN, parentDN, e.getMessage())));
      responseCode = e.code();
    }
    finally {
      updateTaskDetail(taskInstance, taskNote.toString(), taskReason.toString());
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create an <code>Entry</code> in the target system.
   **
   ** @param  parentDN           the distinguihsed name of the organizational
   **                            path to create the entry within and specified
   **                            in the process form.
   **                            It must not be empty.
   ** @param  entryPrefix        the appropriate object entry prefix.
   ** @param  entryClass         the  appropriate object class.
   **
   ** @return                    an appropriate response message.
   */
  protected String create(final String parentDN, final String entryPrefix, final BasicAttribute entryClass) {
    final String method = "create";
    trace(method, SystemMessage.METHOD_ENTRY);

    // remove the attribute specifying the relative distinguished name from
    // field mapping and check for not empty
    final String entryName = (String)data().remove(entryPrefix);
    if (StringUtility.isEmpty(entryName)) {
      error(method, ProvisioningBundle.format(DirectoryError.INSUFFICIENT_INFORMATION, entryPrefix));
      trace(method, SystemMessage.METHOD_EXIT);
      return DirectoryError.INSUFFICIENT_INFORMATION;
    }

    // Form a relative distinguished name from local attribute
    // Note: Connector.compositeName escapes the objectID
    final String entryRDN = DirectoryConnector.composeName(entryPrefix, entryName);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      final Attributes attributes = DirectoryConnector.createAttributes(data());
      attributes.put(entryClass);

      this.connector.connect(this.connector.entitlementPrefixRequired() ? DirectoryConnector.unescapePrefix(parentDN) : parentDN);
      this.connector.createEntry(entryRDN, attributes);
    }
    // when the operation fails in general return the error code as the
    // response
    catch (TaskException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete an <code>Entry</code> in the target system.
   **
   ** @param  superiorDN         the distinguihsed name of the superior entry
   **                            to delete the entry within and specified in the
   **                            process form.
   **                            It must not be empty.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to delete.
   ** @param  reference          the potential references of the entry to
   **                            delete before the entry itself can be deleted.
   ** @param  deleteLeafNode     whether or not the entry and its leaf nodes
   **                            should be deleted on the target system.
   **
   ** @return                    an appropriate response message.
   */
  protected String delete(final String superiorDN, final String entryDN, final Reference reference, final boolean deleteLeafNode){
    final String method = "delete";
    trace(method, SystemMessage.METHOD_ENTRY);

    String responseCode = SUCCESS;
    try {
      // build the search filter to get all the groups the entry is member of
      final String             searchFilter = String.format("(&%s%s)", DirectoryConnector.composeFilter(this.connector.objectClassName(), reference.containerObjectClass), DirectoryConnector.composeFilter(reference.containerAttribute, denormalizePath(entryDN)));
      final String             returning[]  = { reference.containerAttribute };
      final List<SearchResult> results      = this.connector.search(SystemConstant.EMPTY, searchFilter, DirectoryConstant.SCOPE_SUBTREE, returning);

      this.connector.connect();
      final BasicAttributes entryAttribute = new BasicAttributes(reference.containerAttribute, denormalizePath(entryDN), true);
      for (int i = 0; i < results.size(); i++) {
        final SearchResult entry    = results.get(i);
        String             entryRDN = entry.getNameInNamespace();
        if (this.connector.isDistinguishedNameRelative())
          entryRDN = normalizePath(entryRDN);
        // take the fullqualified distinguished name
        this.connector.attributesDelete(entryRDN, entryAttribute);
      }
      this.connector.disconnect();

      final String rdn = DirectoryConnector.entryRDN(entryDN);

      this.connector.connect(superiorDN);
      if (!deleteLeafNode) {
        this.connector.deleteEntry(rdn, null);
      }
      else {
        Control control[] = { new TreeDeleteControl() };
        this.connector.deleteEntry(rdn, control);
      }
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return delete(superiorDN, entryDN, deleteLeafNode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete an <code>Entry</code> in the target system.
   **
   ** @param  superiorDN         the distinguihsed name of the superior entry
   **                            to delete the entry within and specified in the
   **                            process form.
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to delete.
   ** @param  deleteLeafNode     whether or not the entry and its leaf nodes
   **                            should be deleted on the target system.
   **
   ** @return                    an appropriate response message.
   */
  protected String delete(final String superiorDN, final String entryDN, final boolean deleteLeafNode){

    final String method = "delete";
    trace(method, SystemMessage.METHOD_ENTRY);

    String responseCode = SUCCESS;
    try {
      final String rdn = DirectoryConnector.entryRDN(entryDN);

      this.connector.connect(superiorDN);
      if (!deleteLeafNode) {
        this.connector.deleteEntry(rdn, null);
      }
      else {
        Control control[] = { new TreeDeleteControl() };
        this.connector.deleteEntry(rdn, control);
      }
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Entry</code> in the target system.
   **
   ** @param  entryDN            the distinguihsed name of the entry path to
   **                            modify and specified in the process form.
   **                            It must not be empty.
   **
   ** @return                    an appropriate response message.
   */
  protected String modify(final String entryDN) {
    return modify(entryDN, data());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Entry</code> in the target system.
   **
   ** @param  entryDN            the distinguihsed name of the entry path to
   **                            modify and specified in the process form.
   **                            It must not be empty.
   ** @param  data               the {@link Map} containing the name to value
   **                            attribute mapping.
   **
   ** @return                    an appropriate response message.
   */
  protected String modify(final String entryDN, final Map<String, Object> data) {
    final String method = "modify";
    trace(method, SystemMessage.METHOD_ENTRY);
    final String        parentDN   = this.connector.normalizePath(DirectoryConnector.superiorDN(entryDN));
    final String        entryRDN   = DirectoryConnector.entryRDN(entryDN);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      final Attributes attributes = DirectoryConnector.createAttributes(data);

      this.connector.connect(parentDN);
      // the connector has prepared the attribute in the correct manner means
      // if the attribute is empty no value is assinged that leads in an empty
      // attribute that we will be removed by the backend server if it is
      // assinged to the entry
      this.connector.attributesModify(entryRDN, attributes);
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Entry</code> in the target system.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the distinguihsed name of the entry path to
   **                            modify and specified in the process form.
   **                            It must not be empty.
   **
   ** @return                    an appropriate response message.
   */
  protected String modify(final Integer taskInstance, final String entryDN) {
    return modify(taskInstance, entryDN, data());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Entry</code> in the target system.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  entryDN            the distinguihsed name of the entry path to
   **                            modify and specified in the process form.
   **                            It must not be empty.
   ** @param  data               the {@link Map} containing the name to value
   **                            attribute mapping.
   **
   ** @return                    an appropriate response message.
   */
  protected String modify(final Integer taskInstance, final String entryDN, final Map<String, Object> data) {
    final String method = "modify";
    trace(method, SystemMessage.METHOD_ENTRY);

    final StringBuilder taskNote   = new StringBuilder();
    final StringBuilder taskReason = new StringBuilder();
    final String        parentDN   = this.connector.normalizePath(DirectoryConnector.superiorDN(entryDN));
    final String        entryRDN   = DirectoryConnector.entryRDN(entryDN);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      final Attributes attributes = DirectoryConnector.createAttributes(data);

      this.connector.connect(parentDN);
      taskNote.append(createTaskDetail(ProvisioningBundle.format(ProvisioningMessage.ENTRY_MODIFY, entryRDN, parentDN)));
      // the connector has prepared the attribute in the correct manner means
      // if the attribute is empty no value is assinged that leads in an empty
      // attribute that we will be removed by the backend server if it is
      // assinged to the entry
      this.connector.attributesModify(entryRDN, attributes);
      taskNote.append(createTaskDetail(ProvisioningBundle.format(ProvisioningMessage.ENTRY_MODIFIED, entryRDN, parentDN)));
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      taskReason.append(createTaskDetail(ProvisioningBundle.format(ProvisioningError.ENTRY_MODIFY, entryRDN, parentDN, e.getMessage())));
      responseCode = e.code();
    }
    finally {
      updateTaskDetail(taskInstance, taskNote.toString(), taskReason.toString());
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Renames an <code>Entry</code> in the directory tree.
   **
   ** @param  superiorDN         the distinguihsed name of the superior entry
   **                            to rename the entry within and specified in the
   **                            process form.
   ** @param  objectPrefix       the Server Feature Key to fetch the
   **                            appropriate object entry prefix.
   ** @param  entryName          the name the existing entryName to rename.
   ** @param  attributeFilter    the names of the attributes where the callee is
   **                            also interrested in.
   **                            <br>
   **                            This is used a filter to pick up only special
   **                            fields from the previous attribute mapping.
   **                            <br>
   **                            <b>Note:</b>
   **                            The given String must separate the fields with
   **                            the character specified in the
   **                            <code>DirectoryFeature</code> of the associated
   **                            {@link DirectoryConnector} by
   **                            <code>Multi Value Separator</code>
   **
   ** @return                    a appropriate response message
   */
  protected String rename(final String superiorDN, final String objectPrefix, final String entryName, final String attributeFilter) {
    final String method = "rename";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String prefix = this.connector.feature().stringValue(objectPrefix);
    // check if we are able to proceed further
    if (prefix == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, objectPrefix));
      trace(method, SystemMessage.METHOD_EXIT);
      return DirectoryError.INSUFFICIENT_INFORMATION;
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return rename(superiorDN, prefix, entryName, (String)data().remove(prefix), attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Renames an <code>Entry</code> in the directory tree.
   **
   ** @param  superiorDN         the distinguihsed name of the superior entry
   **                            to rename the entry within and specified in the
   **                            process form.
   ** @param  entryPrefix        the appropriate object entry prefix.
   ** @param  oldName            the old name the existing entry to rename.
   ** @param  newName            the new name the entry to rename.
   ** @param  attributeFilter    the names of the attributes where the callee is
   **                            also interrested in.
   **                            <br>
   **                            This is used a filter to pick up only special
   **                            fields from the previous attribute mapping.
   **                            <br>
   **                            <b>Note:</b>
   **                            The given String must separate the fields with
   **                            the character specified in the
   **                            <code>DirectoryFeature</code> of the associated
   **                            {@link DirectoryConnector} by
   **                            <code>Multi Value Separator</code>
   **
   ** @return                    a appropriate response message
   */
  protected String rename(final String superiorDN, final String entryPrefix, final String oldName, final String newName, final String attributeFilter) {
    final String method = "rename";
    trace(method, SystemMessage.METHOD_ENTRY);
    // prepare the names that we need
    final String oldRDN = DirectoryConnector.composeName(entryPrefix, oldName);
    final String newRDN = DirectoryConnector.composeName(entryPrefix, newName);

    // initialize the latch retruned to the process tasks and in the logs for
    // the relative distinguished name
    String rdn          = null;
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      // check got rxistence of the existing entry
      rdn = oldRDN;
      this.connector.findEntry(superiorDN, DirectoryConstant.SCOPE_ONELEVEL, entryPrefix, oldName);
      try {
        rdn = newRDN;
        this.connector.findEntry(superiorDN, DirectoryConstant.SCOPE_ONELEVEL, entryPrefix, newName);
        error(method, ProvisioningBundle.format(DirectoryError.OBJECT_ALREADY_EXISTS, newName));
        trace(method, SystemMessage.METHOD_EXIT);
        responseCode = DirectoryError.OBJECT_ALREADY_EXISTS;
      }
      catch (DirectoryException e) {
        // rename the entry only if we catched the exception with the
        // appropriate code
        if (DirectoryError.OBJECT_NOT_EXISTS.equals(e.code())) {
          this.connector.connect(superiorDN);
          this.connector.rename(oldRDN, newRDN);
          // change all affected attributes specified by the invoker
          if (!StringUtility.isEmpty(attributeFilter)) {
            final List<String> filter = new ArrayList<String>();
            final StringTokenizer tokenizer = new StringTokenizer(attributeFilter, this.connector.multiValueSeparator());
            while (tokenizer.hasMoreElements()) {
              final String name = tokenizer.nextToken();
              // put only attribute name in the filter that does not match the
              // object prefix
              if (!entryPrefix.equalsIgnoreCase(name))
                filter.add(name);
            }
            final Attributes attributes = DirectoryConnector.createAttributes(data(), filter);
            this.connector.attributesModify(newRDN, attributes);
          }
        }
      }
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
      error(method, DirectoryBundle.format(responseCode, rdn));
    }
    finally {
      this.connector.disconnect();
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return responseCode;
  }

  /////////////////////////////////////////////////////////////////////////////
  // Method:   move
  /**
   ** Moves an <code>Entry</code> from one point in the directory tree to
   ** another.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to move/rename.
   ** @param  targetDN           holds the name of the organizational path
   **                            where the entry has to be moved
   **
   ** @return                    a appropriate response message
   */
  protected String move(final String entryDN, final String targetDN) {

    final String method = "move";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode       = SUCCESS;
    try {
      final String superiorDN = this.connector.normalizePath(DirectoryConnector.superiorDN(entryDN));
      final String rdn        = DirectoryConnector.entryRDN(entryDN);

      this.connector.connect();
      this.connector.moveEntry(superiorDN, targetDN, rdn);
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maskAttribute
  /**
   ** Modifies one or more attributes of an <code>Entry</code> in the Target
   ** System for the specified distinguished name <code>entryDN</code>.
   ** <p>
   ** This method expects the initialized mapping of attributes that has to be
   ** changed.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to modify.
   ** @param  attributeName      the name of the attributes where the callee is
   **                            interrested in.
   **                            <br>
   **                            This is used as a filter to pick up only
   **                            special fields from attribute mapping that must
   **                            be created previuosly.
   **                            <br>
   **                            <b>Note:</b>
   **                            The given String must separate the fields with
   **                            the character specified in the
   **                            <code>DirectoryFeature</code> of the associated
   **                            {@link DirectoryConnector} by
   **                            <code>Multi Value Separator</code>
   ** @param  attributeValue     the valeue of the status attributes to mask the
   **                            existing attribute value
   **
   ** @return                    an appropriate response message.
   */
  protected final String maskAttribute(final String entryDN, final String attributeName, final String attributeValue) {

    final String method = "maskAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    if (StringUtility.isEmpty(this.connector.multiValueSeparator())) {
      error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, this.connector.multiValueSeparator()));
      return DirectoryError.INSUFFICIENT_INFORMATION;
    }

    if (StringUtility.isEmpty(attributeName)) {
      error(method, ProvisioningBundle.format(ProvisioningError.MAPPING_PROPERTY_MISSING, "attributeName"));
      return DirectoryError.INSUFFICIENT_INFORMATION;
    }

    List<String>    filter    = new ArrayList<String>();
    StringTokenizer tokenizer = new StringTokenizer(attributeName, this.connector.multiValueSeparator());
    while (tokenizer.hasMoreElements()) {
      final String token = tokenizer.nextToken();
      filter.add(token);
      // extend the attribute mapping by the parsed token
      data().put(token, attributeValue);
    }

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      final Attributes attributes = DirectoryConnector.createAttributes(data(), filter);
      final String superiorDN     = this.connector.normalizePath(DirectoryConnector.superiorDN(entryDN));
      final String rdn            = DirectoryConnector.entryRDN(entryDN);

      this.connector.connect(superiorDN);
      // if the attribute value is not null, then replace the attribute with the
      // value supplied; otherwise if the attribute value is null then delete the
      // attribute
      if (StringUtility.isEmpty(attributeValue))
        this.connector.attributesDelete(rdn, attributes);
      else
        this.connector.attributesModify(rdn, attributes);
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateAttribute
  /**
   ** Modifies one or more attributes of an <code>Entry</code> in the Target
   ** System for the specified distinguished name <code>entryDN</code>.
   ** <p>
   ** This method expects the initialized mapping of attributes that has to be
   ** changed.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to enable.
   ** @param  attributeFilter    the names of the attributes where the callee is
   **                            interrested in.
   **                            <br>
   **                            This is used a filter to pick up only special
   **                            fields from the previous attribute mapping.
   **                            <br>
   **                            <b>Note:</b>
   **                            The given String must separate the fields with
   **                            the character specified in the
   **                            <code>DirectoryFeature</code> of the associated
   **                            {@link DirectoryConnector} by
   **                            <code>Multi Value Separator</code>
   **
   ** @return                    an appropriate response message.
   */
  protected String updateAttribute(final String entryDN, final String attributeFilter) {

    final String method = "updateAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    if (StringUtility.isEmpty(attributeFilter)) {
      error(method, ProvisioningBundle.format(ProvisioningError.MAPPING_PROPERTY_MISSING, "attributeFilter"));
      return DirectoryError.INSUFFICIENT_INFORMATION;
    }

    final List<String>    filter    = new ArrayList<String>();
    final StringTokenizer tokenizer = new StringTokenizer(attributeFilter, this.connector.multiValueSeparator());
    while (tokenizer.hasMoreElements())
      filter.add(tokenizer.nextToken());

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      final Attributes attributes = DirectoryConnector.createAttributes(data(), filter);
      final String superiorDN     = this.connector.normalizePath(DirectoryConnector.superiorDN(entryDN));
      final String rdn            = DirectoryConnector.entryRDN(entryDN);

      this.connector.connect(superiorDN);
      // the connector has prepared the attribute in the correct manner means
      // if the attribute is empty no value is assinged that leads in an empty
      // attribute that we will be removed by the backend server if it is
      // assinged to the entry
      this.connector.attributesModify(rdn, attributes);
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
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
   ** @param  defaultContainer  the fallback DIT entry to use if
   **                           <code>parentRDN</code> is empty.
   **
   ** @return                   a valid rdn
   */
  protected String ensureParentRDN(final String parentRDN, final String defaultContainer) {
    // If rdn is empty then pass cn=users as hierarchy name
    return (StringUtility.isEmpty(parentRDN)) ? defaultContainer : parentRDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMultiValueAttribute
  /**
   ** Adds a multi-valued <code>Attribute</code> to an <code>Entry</code>.
   ** <p>
   ** A pre condition check is done in this method to validate if the
   ** <code>Entry</code> is a member of the other <code>Entry</code>. If
   ** <code>Entry</code> is not a member of the other <code>Entry</code> the
   ** <code>responseCode</code> returnd to the process task is
   ** {@link DirectoryError#OBJECT_ALREADY_ASSIGNED}.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the object
   **                            specified by the full qualified distinguished
   **                            name <code>containerDN</code>.
   ** @param  entryAttribute     the name of the attribute of the LDAP object
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   ** @param  attributeValue     the value of the attribute of the LDAP object
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   **
   ** @return                    an appropriate response message.
   */
  protected String addMultiValueAttribute(final String entryDN, final String entryAttribute, final String attributeValue) {
    final String method = "addMultiValueAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      if (existsAttribute(entryDN, entryAttribute, attributeValue))
        return DirectoryError.ATTRIBUTE_ALREADY_ASSIGNED;

      this.connector.connect();
      // add the attribute to the entry
      this.connector.attributesAdd(entryDN, new BasicAttributes(entryAttribute, attributeValue, true));
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMultiValueAttribute
  /**
   ** Adds a multi-valued <code>Attribute</code> to an <code>Entry</code>.
   ** <p>
   ** A pre condition check is done in this method to validate if the
   ** <code>Entry</code> is a member of the other <code>Entry</code>. If
   ** <code>Entry</code> is not a member of the other <code>Entry</code> the
   ** <code>responseCode</code> returnd to the process task is
   ** {@link DirectoryError#OBJECT_ALREADY_ASSIGNED}.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the object
   **                            specified by the full qualified distinguished
   **                            name <code>containerDN</code>.
   ** @param  attributeName      the name of the attribute of the LDAP object
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   ** @param  oldAttributeValue  the value of the attribute of the LDAP object
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   ** @param  newAttributeValue  the value of the attribute of the LDAP object
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   **
   ** @return                    an appropriate response message.
   */
  protected String updateMultiValueAttribute(final String entryDN, final String attributeName, final String oldAttributeValue, final String newAttributeValue) {
    final String method = "updateMultiValueAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      List<Object> result = this.fetchAttribute(entryDN, attributeName);
      if (!result.contains(oldAttributeValue))
        return DirectoryError.ATTRIBUTE_ALREADY_REMOVED;
      if (result.contains(newAttributeValue))
        return DirectoryError.ATTRIBUTE_ALREADY_ASSIGNED;

      // adjust the list of attribut values by rreplacing the old value with the
      // new value
      result.remove(oldAttributeValue);
      result.add(newAttributeValue);

      // transform the list back in the objects the API understands
      // value
      BasicAttribute attribute = new BasicAttribute(attributeName, true);
      Iterator<Object> i = result.iterator();
      while (i.hasNext())
        attribute.add(i.next());

      // put the transformation in a container
      BasicAttributes attributeSet = new BasicAttributes(true);
      attributeSet.put(attribute);

      this.connector.connect();
      // modify the attribute to the entry
      this.connector.attributesModify(entryDN, attributeSet);
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteMultiValueAttribute
  /**
   ** Adds a multi-valued <code>Attribute</code> to an <code>Entry</code>.
   ** <p>
   ** A pre condition check is done in this method to validate if the
   ** <code>Entry</code> is a member of the other <code>Entry</code>. If
   ** <code>Entry</code> is not a member of the other <code>Entry</code> the
   ** <code>responseCode</code> returnd to the process task is
   ** {@link DirectoryError#OBJECT_ALREADY_ASSIGNED}.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the object
   **                            specified by the full qualified distinguished
   **                            name <code>containerDN</code>.
   ** @param  entryAttribute     the name of the attribute of the LDAP object
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   ** @param  attributeValue     the value of the attribute of the LDAP object
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   **
   ** @return                    an appropriate response message.
   */
  protected String deleteMultiValueAttribute(final String entryDN, final String entryAttribute, final String attributeValue) {
    final String method = "deleteMultiValueAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      if (!existsAttribute(entryDN, entryAttribute, attributeValue))
        return DirectoryError.ATTRIBUTE_NOT_ASSIGNED;

      this.connector.connect();
      // add the attribute to the entry
      this.connector.attributesDelete(normalizePath(entryDN), new BasicAttributes(entryAttribute, attributeValue, true));
    }
    // when the operation fails in general return the error code as the
    // response
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addToEntry
  /**
   ** Adds an <code>Entry</code> as a member to another <code>Entry</code>.
   ** <p>
   ** The pre requisites of this method are that the {@link DirectoryConnector}
   ** associated with this task is configured properly. This means this method
   ** validates that in the <code>DirectoryFeature</code> of the
   ** {@link DirectoryConnector} used by this task are all necessary definitions
   ** contained.
   ** <p>
   ** A pre condition check is done in this method to validate if the
   ** <code>Entry</code> is a member of the other <code>Entry</code>. If
   ** <code>Entry</code> is not a member of the other <code>Entry</code> the
   ** <code>responseCode</code> returnd to the process task is
   ** {@link DirectoryError#OBJECT_ALREADY_ASSIGNED}.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the object
   **                            specified by the full qualified distinguished
   **                            name <code>containerDN</code>.
   ** @param  containerDN        the fullqualified distinguished name of the
   **                            object selected in the process form.
   ** @param  containerClass     the objectClass of the container entry the
   **                            entry should be added to.
   ** @param  containerPrefix    the object prefix of the container entry the
   **                            entry should be added to.
   ** @param  memberAttribute    the name of the attribute of the LDAP object
   **                            specified by <code>containerDN</code> where the
   **                            <code>entryDN</code> has to be added to.
   ** @param  entryAttribute     the name of the attribute of the LDAP object
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   ** @param  entryAttributeDN   the kind how the entry attribute will be
   **                            handled, <code>true</code> will provision the
   **                            fullqualified DN, <code>false</code> will only
   **                            write the value specified by groupPrefix to
   **                            the attribute.
   **
   ** @return                    an appropriate response message.
   */
  protected final String addToEntry(final String entryDN, final String containerDN, final String containerClass, final String containerPrefix, final String memberAttribute, final String entryAttribute, final String entryAttributeDN) {
    final String method = "addToEntry";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      final Reference reference = createEntryReference(containerClass, containerPrefix, memberAttribute, entryAttribute, entryAttributeDN);
      responseCode = addToEntry(entryDN, containerDN, reference);
    }
    // when the operation fails in general
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
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
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the object
   **                            specified by the full qualified distinguished
   **                            name <code>containerDN</code>.
   ** @param  containerDN        the fullqualified distinguished name of the
   **                            object selected in the process form.
   ** @param  reference          the descriptor of the reference to create.
   **
   ** @return                    an appropriate response message.
   */
  protected String addToEntry(final String entryDN, final String containerDN, final Reference reference) {
    final String method = "addToEntry";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if entitlement prefix has to be removed from the containerDN
    String container = this.connector.entitlementPrefixRequired() ? DirectoryConnector.unescapePrefix(containerDN) : containerDN;
    try {
      if (existsReference(entryDN, container, reference))
        responseCode = DirectoryError.OBJECT_ALREADY_ASSIGNED;
      else {
        this.connector.connect();
        // add the object to the container
        this.connector.attributesAdd(container, new BasicAttributes(reference.containerAttribute, denormalizePath(entryDN), true));

        // check if we have to handle the back link attribute
        if (!StringUtility.isEmpty(reference.entryAttribute)) {
          if (reference.entryAttributeDN)
            container = denormalizePath(container);
          else {
            final List<String[]> component= DirectoryName.explode(container);
            container = component.get(0)[1];
          }

          // add the container to the entry
          this.connector.attributesAdd(entryDN, new BasicAttributes(reference.entryAttribute, container, true));
        }
      }
    }
    // when the operation fails in general
    catch (DirectoryException e) {
      responseCode = e.code();
    }

    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeFromEntry
  /**
   ** Removes an <code>Entry</code> as a member from another <code>Entry</code>.
   ** <p>
   ** The pre requisites of this method are that the {@link DirectoryConnector}
   ** associated with this task is configured properly. This means this method
   ** validates that in the <code>DirectoryFeature</code> of the
   ** {@link DirectoryConnector} used by this task are all necessary definitions
   ** contained.
   ** <p>
   ** A pre condition check is done in this method to validate if the
   ** <code>Entry</code> is a member of the other <code>Entry</code>. If
   ** <code>Entry</code> is not a member of the other <code>Entry</code> the
   ** <code>responseCode</code> returnd to the process task is
   ** {@link DirectoryError#OBJECT_ALREADY_REMOVED}.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            <code>Entry</code> to be removed as member
   **                            from the object specified by the full qualified
   **                            distinguished name <code>containerDN</code>.
   ** @param  containerDN        the fullqualified distinguished name of the
   **                            object selected in the process form.
   ** @param  containerClass     the objectClass of the container entry the
   **                            entry should be added to.
   ** @param  containerPrefix    the object prefix of the container entry the
   **                            entry should be added to.
   ** @param  memberAttribute   the name of the attribute of the LDAP object
   **                            specified by <code>containerDN</code> where the
   **                            <code>objectDN</code> has to be removed from.
   ** @param  entryAttribute     the name of the attribute of the LDAP object
   **                            specified by <code>entryDN</code> where the
   **                            <code>containerDN</code> has to be removed.
   ** @param  entryAttributeDN   the kind how the entry attribute will be
   **                            handled, <code>true</code> will provision the
   **                            fullqualified DN, <code>false</code> will only
   **                            write the value specified by groupPrefix to
   **                            the attribute.
   **
   ** @return                    an appropriate response message.
   */
  protected final String removeFromEntry(final String entryDN, final String containerDN, final String containerClass, final String containerPrefix, final String memberAttribute, final String entryAttribute, final String entryAttributeDN) {
    final String method = "removeFromEntry";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    try {
      final Reference reference = createEntryReference(containerClass, containerPrefix, memberAttribute, entryAttribute, entryAttributeDN);
      responseCode = removeFromEntry(entryDN, containerDN, reference);
    }
    // when the operation fails in general
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);

    return responseCode;
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
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            <code>Entry</code> to be removed as member
   **                            from the object specified by the full qualified
   **                            distinguished name <code>containerDN</code>.
   ** @param  containerDN        the fullqualified distinguished name of the
   **                            object selected in the process form.
   ** @param  reference          the descriptor of the reference to remove.
   **
   ** @return                    an appropriate response message.
   */
  protected final String removeFromEntry(final String entryDN, final String containerDN, final Reference reference) {
    final String method = "removeFromEntry";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if entitlement prefix has to be removed from the containerDN
    String container = this.connector.entitlementPrefixRequired() ? DirectoryConnector.unescapePrefix(containerDN) : containerDN;
    try {
      if (!existsReference(entryDN, container, reference))
        responseCode = DirectoryError.OBJECT_ALREADY_REMOVED;
      else {
        this.connector.connect();
        // remove the entry from the container
        this.connector.attributesDelete(container, new BasicAttributes(reference.containerAttribute, denormalizePath(entryDN), true));

        // check if we have to handle the back link attribute
        if (!StringUtility.isEmpty(reference.entryAttribute)) {
          if (reference.entryAttributeDN)
            container = denormalizePath(container);
          else {
            final List<String[]> component= DirectoryName.explode(containerDN);
            container = component.get(0)[1];
          }
          // remove the container from the entry
          this.connector.attributesDelete(entryDN, new BasicAttributes(reference.entryAttribute, container, true));
        }
      }
    }
    // when the operation fails in general
    catch (DirectoryException e) {
      responseCode = e.code();
    }
    this.connector.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryObjectClass
  /**
   ** Creates the {@link BasicAttribute} for every <code>objectClass</code>
   ** which needs to be assigned to an entry in the Directory Service if a new
   ** entry is created.
   **
   ** @param  classes            the delemitted list aof class names to create
   **                            as an entry in the resulting
   **                            {@link BasicAttribute}.
   **
   ** @return                    the {@link BasicAttribute} containg the
   **                            objectClasses of the entry.
   */
  protected BasicAttribute entryObjectClass(final String classes) {
    // Create the object classes an entry should be assigned to
    final BasicAttribute  entryClass = new BasicAttribute(this.connector.objectClassName());
    final StringTokenizer tokenizer  = new StringTokenizer(classes, this.connector.multiValueSeparator());
    while (tokenizer.hasMoreTokens())
      entryClass.add(tokenizer.nextToken());
    return entryClass;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   existsAttribute
  /**
   ** Performs a LDAP search to lookup the existence of an object reference like
   ** group membership.
   ** <p>
   ** The search validates if the <code>Entry</code> is a member of the other
   ** <code>Entry</code>. If <code>Entry</code> is not a member of the other
   ** <code>Entry</code> the returned value is <code>true</code>.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the object
   **                            specified by the full qualified distinguished
   **                            name <code>containerDN</code>.
   ** @param  attributeName      the name of the attribute selected in the
   **                            process form.
   ** @param  attributeValue     the value of the attribute selected in the
   **                            process form.
   **
   ** @return                    <code>true</code> if the attribute value
   **                            already assigned to the <code>Entry</code>;
   **                            otherwise <code>false</code>
   **
   ** @throws DirectoryException when the operation fails in general
   */
  protected final boolean existsAttribute(final String entryDN, final String attributeName, final String attributeValue)
    throws DirectoryException {

    final String method = "existsAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Object> result = null;
    try {
      result = this.fetchAttribute(entryDN, attributeName);
      return (result != null) ? result.contains(attributeValue) : false;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   existsReference
  /**
   ** Performs a LDAP search to lookup the existence of an object reference like
   ** group membership.
   ** <p>
   ** The search validates if the <code>Entry</code> is a member of the other
   ** <code>Entry</code>. If <code>Entry</code> is not a member of the other
   ** <code>Entry</code> the returned value is <code>true</code>.
   **
   ** @param  entryDN            the fullqualified distinguished name of the
   **                            entry to added as member to the object
   **                            specified by the full qualified distinguished
   **                            name <code>containerDN</code>.
   ** @param  containerDN        the fullqualified distinguished name of the
   **                            object selected in the process form.
   ** @param  reference          the {@link Reference} descriptor to use.
   **
   ** @return                    <code>true</code> if the <code>Entry</code> is
   **                            a member of the other <code>Entry</code>;
   **                            otherwise <code>false</code>
   **
   ** @throws DirectoryException when the operation fails in general
   */
  protected final boolean existsReference(final String entryDN, final String containerDN, final Reference reference)
    throws DirectoryException {

    final String method = "existsReference";
    trace(method, SystemMessage.METHOD_ENTRY);

    // build the search filter for the distinguished name of the requested object
    final String searchFilter = String.format("(&%s(%s)%s)"
      , DirectoryConnector.composeFilter(this.connector.objectClassName(), reference.containerObjectClass)
      , DirectoryConnector.entryRDN(containerDN)
      , DirectoryConnector.composeFilter(reference.containerAttribute, denormalizePath(entryDN))
    );

    try {
      // search for the occurrence of the association in the LDAP Server
      final String[]           returnning = { reference.containerAttribute };
      final List<SearchResult> members    = this.connector.search(DirectoryConnector.superiorDN(containerDN), searchFilter, DirectoryConstant.SCOPE_ONELEVEL, returnning);
      return (members != null && members.size() > 0);
   }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntryReference
  /**
   ** Creates the object with valid group configuration an <code>Object</code>
   ** as a member of a <code>Group Object</code>.
   ** <p>
   ** The pre requisites of this method are that the {@link DirectoryConnector}
   ** associated with this task is configured properly. This means this method
   ** validates that in the <code>DirectoryFeature</code> of the
   ** {@link DirectoryConnector} used by this task are all necessary definitions
   ** contained.
   **
   ** @param  containerClass     the objectClass of the container entry the
   **                            entry should be added to.
   ** @param  containerPrefix    the object prefix of the container entry the
   **                            entry should be added to.
   ** @param  memberAttribute    the name of the attribute of the LDAP container
   **                            specified by <code>containerDN</code> where the
   **                            <code>entryDN</code> has to be added to.
   ** @param  entryAttribute     the name of the attribute of the LDAP container
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   ** @param  entryAttributeDN   the kind how the entry attribute will be
   **                            handled, <code>true</code> will provision the
   **                            fullqualified DN, <code>false</code> will only
   **                            write the value specified by containerPrefix to
   **                            the attribute.
   **
   ** @return                    a {@link Provisioning.Reference} with the
   **                            values obtained from the Server Feature
   **                            configuration.
   **
   ** @throws DirectoryException if one of the entries is missing
   */
  protected final Reference createEntryReference(final String containerClass, final String containerPrefix, final String memberAttribute, final String entryAttribute, final String entryAttributeDN)
    throws DirectoryException {

    final String method = "createGroupReference";
    trace(method, SystemMessage.METHOD_ENTRY);

    Reference reference = new Reference();
    try {
      // ensure that we can build a search filter based on the object class of the
      // group entry
      reference.containerObjectClass = this.connector.feature().stringValue(containerClass);
      if (StringUtility.isEmpty(reference.containerObjectClass)) {
        error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, containerClass));
        throw new DirectoryException(DirectoryError.INSUFFICIENT_INFORMATION);
      }

      // ensure that we can build a search filter based on the object prefix of
      // the container entry
      reference.containerObjectPrefix = this.connector.feature().stringValue(containerPrefix);
      if (StringUtility.isEmpty(reference.containerObjectPrefix)) {
        error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, containerPrefix));
        throw new DirectoryException(DirectoryError.INSUFFICIENT_INFORMATION);
      }

      // ensure that we can build a search filter based on the attribute name of
      // the container entry
      reference.containerAttribute = this.connector.feature().stringValue(memberAttribute);
      if (StringUtility.isEmpty(reference.containerAttribute)) {
        error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, memberAttribute));
        throw new DirectoryException(DirectoryError.INSUFFICIENT_INFORMATION);
      }

      // if the object class contains more than one entry split the entries by
      // the configured multi value separator
      final StringTokenizer tokenizer = new StringTokenizer(reference.containerObjectClass, this.connector.multiValueSeparator());
      if (tokenizer.hasMoreTokens())
        reference.containerObjectClass = tokenizer.nextToken();

      // the name of the attribute
      reference.entryAttribute   = this.connector.feature().stringValue(entryAttribute);
      // the kind how the value will be handled
      reference.entryAttributeDN = this.connector.feature().booleanValue(entryAttributeDN);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRoleReference
  /**
   ** Creates the object with valid role configuration an <code>Object</code>
   ** as a member of a <code>Role Object</code>.
   ** <p>
   ** The pre requisites of this method are that the {@link DirectoryConnector}
   ** associated with this task is configured properly. This means this method
   ** validates that in the <code>DirectoryFeature</code> of the
   ** {@link DirectoryConnector} used by this task are all necessary definitions
   ** contained.
   **
   ** @param  roleClass          the objectClass of the role entry the
   **                            entry should be added to.
   ** @param  rolePrefix         the object prefix of the role entry the
   **                            entry should be added to.
   ** @param  memberAttribute   the name of the attribute of the LDAP role
   **                            specified by <code>containerDN</code> where the
   **                            <code>entryDN</code> has to be added to.
   ** @param  entryAttribute     the name of the attribute of the LDAP role
   **                            specified by <code>objectDN</code> where the
   **                            <code>containerDN</code> has to be relinked.
   **
   ** @return                    a {@link Provisioning.Reference} with the
   **                            values obtained from the Server Feature
   **                            configuration.
   **
   ** @throws DirectoryException if one of the entries is missing
   */
  protected final Reference createRoleReference(final String roleClass, final String rolePrefix, final String memberAttribute, final String entryAttribute)
    throws DirectoryException {

    final String method = "createRoleReference";
    trace(method, SystemMessage.METHOD_ENTRY);

    Reference reference = new Reference();
    try {
      // ensure that we can build a search filter based on the object class of the
      // group entry
      reference.containerObjectClass = this.connector.feature().stringValue(roleClass);

      // if the object class of a role is defined all other attributes are
      // mandatory
      if (!StringUtility.isEmpty(reference.containerObjectClass)) {
        // ensure that we can build a search filter based on the object prefix of
        // the container entry
        reference.containerObjectPrefix = this.connector.feature().stringValue(rolePrefix);
        if (StringUtility.isEmpty(reference.containerObjectPrefix)) {
          error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, rolePrefix));
          throw new DirectoryException(DirectoryError.INSUFFICIENT_INFORMATION);
        }

        // ensure that we can build a search filter based on the attribute name of
        // the container entry
        reference.containerAttribute = this.connector.feature().stringValue(memberAttribute);
        if (StringUtility.isEmpty(reference.containerAttribute)) {
          error(method, ProvisioningBundle.format(ProvisioningError.FEATURE_PROPERTY_MISSING, memberAttribute));
          throw new DirectoryException(DirectoryError.INSUFFICIENT_INFORMATION);
        }

        // if the object class contains more than one entry split the entries by
        // the configured multi value separator
        StringTokenizer tokenizer = new StringTokenizer(reference.containerObjectClass, this.connector.multiValueSeparator());
        if (tokenizer.hasMoreTokens())
          reference.containerObjectClass = tokenizer.nextToken();

        reference.entryAttribute = this.connector.feature().stringValue(entryAttribute);
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTaskDetail
  /**
   ** Updates the task detail specified by <code>taskInstance</code> with the
   ** appropriate informations.
   **
   ** @param  taskInstance       the task instance key used to update particular
   **                            task informations like notes etc.
   ** @param  note               the note text to append on the existing notes.
   ** @param  error              the error text to append on the existing
   **                            errors.
   */
  protected final void updateTaskDetail(final Integer taskInstance, final String note, final String error) {
   final String method = "updateTaskDetail";
   trace(method, SystemMessage.METHOD_ENTRY);

   final tcProvisioningOperationsIntf facade = service(tcProvisioningOperationsIntf.class);
   final Map<String, String>          detail = new HashMap<String, String>();
   try {
     // obtain the current task details
     final tcResultSet set = facade.getProvisioningTaskDetails(taskInstance);
     // add note to task
     detail.put(ProcessInstance.NOTE, appendTaskDetail(set, ProcessInstance.NOTE, note));
     // add error reason to task if available
     if (!StringUtility.isEmpty(error))
       detail.put(ProcessInstance.REASON, appendTaskDetail(set, ProcessInstance.REASON, error));

     // update task details with proper informations
     facade.updateTask(taskInstance, set.getByteArrayValue(ProcessInstance.VERSION), detail);
    }
    catch (tcColumnNotFoundException e) {
      error(method, e.getMessage());
    }
    catch (tcTaskNotFoundException e) {
      error(method, e.getMessage());
    }
    catch (tcStaleDataUpdateException e) {
      error(method, e.getMessage());
    }
    catch (tcAwaitingObjectDataCompletionException e) {
      error(method, e.getMessage());
    }
    catch (tcAwaitingApprovalDataCompletionException e) {
      error(method, e.getMessage());
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
    finally {
      facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appendTaskDetail
  /**
   ** Appends the specified <code>message</code> text to the existing text of a
   ** task detail.
   **
   ** @param  taskDetails        the {@link tcResultSet} providing the former
   **                            details of a task instance.
   ** @param  attribute          the name of a attribute of the specified task
   **                            details to format.
   ** @param  message            the text message to append for the specific
   **                            attribute.
   **
   ** @return                    a message.
   */
  protected final String appendTaskDetail(final tcResultSet taskDetails, final String attribute, final String message) {
   final String method = "appendTaskDetail";
   trace(method, SystemMessage.METHOD_ENTRY);
   String result = message;
   try {
      result = String.format("%s\n%s", taskDetails.getStringValue(attribute), createTaskDetail(message));
    }
    catch (tcColumnNotFoundException e) {
      error(method, e.getMessage());
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
   trace(method, SystemMessage.METHOD_EXIT);
   return message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTaskDetail
  /**
   ** Appends the specified <code>message</code> text to the existing text of a
   ** task detail.
   **
   ** @param  message            the text message to append for the specific
   **                            attribute.
   **
   ** @return                    a message.
   */
  protected static String createTaskDetail(final String message) {
   return String.format("| %1$tm.%<te.%<tY %<tH:%<tM:%<tS:%<tL | %2$-60s|", new Date(), message);
  }
}