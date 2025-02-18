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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   LDIF Flatfile Connector

    File        :   Controller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Controller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-31-05  DSteding    First release version
*/

package oracle.iam.identity.ldif.service;

import java.util.List;

import java.io.File;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.utility.file.LDAPError;
import oracle.iam.identity.utility.file.LDAPException;

import oracle.iam.identity.ldif.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Controller
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Controller</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Scheduler which handles data
 ** provided by or delivered to a LDIF file.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class Controller extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Defines the string to specify ISO-LATIN-1 file encoding. */
  protected static final String  ISO_ENCODING     = "ISO-8859-1";

  /** Defines the string to specify UTF-8 file encoding. */
  protected static final String  UTF_ENCODING     = "UTF-8";

  /**
   ** Attribute tag which must be defined on a scheduled task to tell the
   ** process how the raw files are encoded.
   */
  protected static final String  FILE_ENCODING    = "File Encoding";

  /**
   ** Attribute tag which must be defined on a scheduled task to tell the
   ** process by which character the values are separated from the name.
   */
  protected static final String  VALUE_SEPARATOR = "Value Separator";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of Metadata Descriptor which is specifying the feature descriptor of the
   ** LDIF file.
   */
  public static final String     SERVER_FEATURE  = "Server Feature";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the raw file to reconcile.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_FILE       = "Data File";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the folder the file to reconcile is located in the filesystem.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_FOLDER     = "Data Folder";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the
   ** location where of the raw files should be copied after they are proceed by
   ** the preprocessor and might be some errors detected.
   */
  protected static final String  ERROR_FOLDER    = "Error Folder";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the character used to separate name from value */
  private char                   valueSeparator   = SystemConstant.COLON;

  /** the abstract file which this connector should be use as the data folder */
  private File                   dataFolder       = null;

  /**
   ** the abstract file which this connector should be use as the folder to
   ** place there the files which contains errors
   */
  private File                   errorFolder      = null;

  /** the abstraction layer to describe the connection to the target system */
  private DirectoryResource      resource;

  /** the abstraction layer to communicate with the target system */
  private DirectoryConnector     connector;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Controller</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  public Controller(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFileName
  /**
   ** Returns the name of the file which is used to read or write raw data.
   ** <br>
   ** Convenience method to shortens the access to the task attribute
   ** {@link #DATA_FILE}.
   **
   ** @return                    the name of the file which is used to read or
   **                            write raw data.
   */
  protected final String dataFileName() {
    return stringValue(DATA_FILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueSeparator
  /**
   ** Returns the character used to separate name from value.
   **
   ** @return                    the character used to separate name from value.
   */
  protected final char valueSeparator() {
    return this.valueSeparator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFolder
  /**
   ** Returns the abstract {@link File} handle of the file which is used as the
   ** data folder.
   **
   ** @return                    the abstract {@link File} handle of the file
   **                            which is used as the data folder.
   */
  protected final File dataFolder() {
    return this.dataFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorFolder
  /**
   ** Returns the abstract {@link File} handle of the file which is used as the
   ** error folder.
   **
   ** @return                    the abstract {@link File} handle of the file
   **                            which is used as the error folder.
   */
  protected final File errorFolder() {
    return this.errorFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the {@link DirectoryResource} this task is using to describe the
   ** connection parameter to the LDAP server.
   **
   ** @return                    the {@link DirectoryResource} this task is
   **                            using to describe the connection parameter to
   **                            the LDAP server.
   */
  protected final DirectoryResource resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connector
  /**
   ** Returns the {@link DirectoryConnector} this task is using to connect and
   ** perform operations on the LDAP server.
   **
   ** @return                    the {@link DirectoryConnector} this task is
   **                            using to connect and perform operations on the
   **                            LDAP server.
   */
  protected final DirectoryConnector connector() {
    return this.connector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormat
  /**
   ** Returns the format of a timestamp value in target Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#TIMESTAMP_FORMAT}.
   ** <p>
   ** If {@link DirectoryConstant#TIMESTAMP_FORMAT} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#TIMESTAMP_FORMAT_DEFAULT}
   **
   ** @return                    the format of a timestamp value in target
   **                            Directory Service.
   */
  public final String timestampFormat() {
    return this.connector.timestampFormat();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassName
  /**
   ** Returns the name of the generic object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#OBJECT_CLASS_NAME}.
   ** <p>
   ** If {@link DirectoryConstant#OBJECT_CLASS_NAME} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#OBJECT_CLASS}
   **
   ** @return                    the name of the generic object class.
   */
  public final String objectClassName() {
    return this.connector.objectClassName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryObjectAttributes
  /**
   ** Returns the name of the object attribute that is a binary attribute
   ** of an entry in a LDAP server.
   ** <p>
   ** The toolkit is programmed to recognize the following set of common LDAP
   ** binary attributes:
   ** <table border="0" cellspacing="10" cellpadding="5" summary="">
   ** <tr><th align="left">Attribute ID</th><th align="left">OID</th><th align="left">Reference</th></tr>
   ** <tr><td>photo</td><td>0.9.2342.19200300.100.1.7</td><td><a href="http://www.ietf.org/rfc/rfc1274.txt">RFC 1274</a></td></tr>
   ** <tr><td>personalSignature</td><td>0.9.2342.19200300.100.1.53</td><td><a href="http://www.ietf.org/rfc/rfc1274.txt">RFC 1274</a></td></tr>
   ** <tr><td>audio</td><td>0.9.2342.19200300.100.1.55</td><td><a href="http://www.ietf.org/rfc/rfc1274.txt">RFC 1274</a></td></tr>
   ** <tr><td>jpegPhoto</td><td>0.9.2342.19200300.100.1.60</td><td><a href="http://www.ietf.org/rfc/rfc2798.txt">RFC 2798</a></td></tr>
   ** <tr><td>javaSerializedData</td><td>1.3.6.1.4.1.42.2.27.4.1.7</td><td><a href="http://www.ietf.org/rfc/rfc2713.txt">RFC 2713</a></td></tr>
   ** <tr><td>thumbnailPhoto</td><td>2.16.128.113533.1.1400.1</td><td><a href="http://www.netapps.org/">NAC LIP Schema</a></td></tr>
   ** <tr><td>thumbnailLogo</td><td>2.16.128.113533.1.1400.2</td><td><a href="http://www.netapps.org/">NAC LIP Schema</a></td></tr>
   ** <tr><td>userPassword</td><td>2.5.4.35</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>userCertificate</td><td>2.5.4.36</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>cACertificate</td><td>2.5.4.37</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>authorityRevocationList</td><td>2.5.4.38</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>certificateRevocationList</td><td>2.5.4.38</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>crossCertificatePair</td><td>2.5.4.39</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>x500UniqueIdentifier</td><td>2.5.4.45</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** </table>
   **
   ** @return                    the name(s) of the object attributes that are
   **                            treated as binary.
   */
  public final String binaryObjectAttribute() {
    return this.connector.binaryObjectAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNameAttribute
  /**
   ** Returns the name of the attribute to fetch the distinguished name of a
   ** LDAP entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#DISTINGUISHED_NAME_ATTRIBUTE}.
   ** <p>
   ** If {@link DirectoryConstant#DISTINGUISHED_NAME_ATTRIBUTE} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#DN}
   **
   ** @return                    the name of the attribute to detect the
   **                            distinguished name.
   */
  public final String distinguishedNameAttribute() {
    return this.connector.distinguishedNameAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNameCaseSensitive
  /**
  ** Returns <code>true</code> if the distinguished names of an LDAP entry has
   ** to be handled case sensitive.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#DISTINGUISHED_NAME_CASESENSITIVE}.
   ** <p>
   ** If {@link DirectoryConstant#DISTINGUISHED_NAME_ATTRIBUTE} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** <code>false</code>.
   **
   ** @return                    <code>true</code> if the distinguished names of
   **                            an LDAP entry has to be handled case sensitive;
   **                            otherwise <code>false</code>.
   */
  public final boolean distinguishedNameCaseSensitive() {
    return this.connector.distinguishedNameCaseSensitive();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectCreatedAttribute
  /**
   ** Returns the name of the attribute to detect the created timestamp of a
   ** LDAP entry.
   ** <br>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTRY_CREATED_ATTRIBUTE}.
   ** <p>
   ** If {@link DirectoryConstant#ENTRY_CREATED_ATTRIBUTE} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTRY_CREATED_ATTRIBUTE_DEFAULT}
   **
   ** @return                    the name of the attribute to detect the
   **                            created timestamp.
   */
  public final String objectCreatedAttribute() {
    return this.connector.objectCreatedAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectModifiedAttribute
  /**
   ** Returns the name of the attribute to detect the modified timestamp of a
   ** LDAP entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTRY_MODIFIED_ATTRIBUTE}.
   ** <p>
   ** If {@link DirectoryConstant#ENTRY_MODIFIED_ATTRIBUTE} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTRY_MODIFIED_ATTRIBUTE_DEFAULT}
   **
   ** @return                    the name of the attribute to detect the
   **                            modified timestamp.
   */
  public final String objectModifiedAttribute() {
    return this.connector.objectModifiedAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainObjectClass
  /**
   ** Returns the name of the domain object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#DOMAIN_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#DOMAIN_OBJECT_PREFIX} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#DOMAIN_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the domain object class.
   */
  public final String domainObjectClass() {
    return this.connector.domainObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainObjectPrefix
  /**
   ** Returns the name of the domain object prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#DOMAIN_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#DOMAIN_OBJECT_PREFIX} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#DOMAIN_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the domain object prefix.
   */
  public final String domainObjectPrefix() {
    return this.connector.domainObjectPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainMultiValueAttribute
  /**
   ** Returns the {@link List} of names of the domain multi-valued attributes
   ** for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#DOMAIN_MULTIVALUE_ATTRIBUTE}.
   **
   ** @return                   the name of the domain multi-valued attributes.
   */
  public final List<String> domainMultiValueAttribute() {
    return this.connector.domainMultiValueAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryObjectClass
  /**
   ** Returns the name of the country object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#COUNTRY_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#COUNTRY_OBJECT_CLASS} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#COUNTRY_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the country object class.
   */
  public final String countryObjectClass() {
    return this.connector.countryObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryObjectPrefix
  /**
   ** Returns the name of the country object prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#COUNTRY_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#COUNTRY_OBJECT_PREFIX} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#COUNTRY_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the country object prefix.
   */
  public final String countryObjectPrefix() {
    return this.connector.countryObjectPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryMultiValueAttribute
  /**
   ** Returns the {@link List} of names of the country multi-valued attributes
   ** for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#COUNTRY_MULTIVALUE_ATTRIBUTE}.
   **
   ** @return                   the name of the country multi-valued attributes.
   */
  public final List<String> countryMultiValueAttribute() {
    return this.connector.countryMultiValueAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityObjectClass
  /**
   ** Returns the name of the locality object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#LOCALITY_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#LOCALITY_OBJECT_CLASS} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#LOCALITY_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the locality object class.
   */
  public final String localityObjectClass() {
   return this.connector.localityObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityObjectPrefix
  /**
   ** Returns the name of the locality object prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#LOCALITY_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#LOCALITY_OBJECT_PREFIX} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#LOCALITY_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the locality object prefix.
   */
  public final String localityObjectPrefix() {
    return this.connector.localityObjectPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityMultiValueAttribute
  /**
   ** Returns the {@link List} of names of the locality multi-valued attributes
   ** for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#LOCALITY_MULTIVALUE_ATTRIBUTE}.
   **
   ** @return                   the name of the locality multi-valued
   **                           attribute names.
   */
  public final List<String> localityMultiValueAttribute() {
    return this.connector.localityMultiValueAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleObjectClass
  /**
   ** Returns the name of the role object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ROLE_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ROLE_OBJECT_CLASS} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** <code>null</code>.
   **
   ** @return                    the name of the role object class.
   */
  public final String roleObjectClass() {
   return this.connector.roleObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleObjectPrefix
  /**
   ** Returns the name of the role object prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ROLE_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ROLE_OBJECT_PREFIX} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** <code>null</code>.
   **
   ** @return                    the name of the role object prefix.
   */
  public final String roleObjectPrefix() {
    return this.connector.roleObjectPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleObjectMemberAttribute
  /**
   ** Returns the name of the attribute of a membership of an object within a
   ** role defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ROLE_OBJECTMEMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the attribute of a membership of an
   **                            object within a role.
   */
  public final String roleObjectMemberAttribute() {
    return this.connector.roleObjectMemberAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupObjectClass
  /**
   ** Returns the name of the group object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#GROUP_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#GROUP_OBJECT_CLASS_DEFAULT} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#GROUP_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the group object class.
   */
  public final String groupObjectClass() {
    return this.connector.groupObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupObjectPrefix
  /**
   ** Returns the name of the group object prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#GROUP_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#GROUP_OBJECT_PREFIX} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#GROUP_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the group object prefix.
   */
  public final String groupObjectPrefix() {
    return this.connector.groupObjectPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupObjectMemberAttribute
  /**
   ** Returns the name of the object group membership attribute of en entry in a
   ** LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#GROUP_OBJECTMEMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the object group membership
   **                            attribute of en entry in a LDAP server.
   */
  public final String groupObjectMemberAttribute() {
    return this.connector.groupObjectMemberAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationObjectClass
  /**
   ** Returns the name of the organization object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ORGANIZATION_OBJECT_CLASS} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ORGANIZATION_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the organization object class.
   */
  public final String organizationObjectClass() {
    return this.connector.organizationObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationObjectPrefix
  /**
   ** Returns the name of the organization object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ORGANIZATION_OBJECT_PREFIX} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ORGANIZATION_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the organization object class.
   */
  public final String organizationObjectPrefix() {
    return this.connector.organizationObjectPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationMultiValueAttribute
  /**
   ** Returns the {@link List} of names of the organization multi-valued
   ** attributes for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_MULTIVALUE_ATTRIBUTE}.
   **
   ** @return                   the names of the organization multi-valued
   **                           attributes.
   */
  public final List<String> organizationMultiValueAttribute() {
    return this.connector.organizationMultiValueAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationRoleMemberAttribute
  /**
   ** Returns the of the organization role membership attribute defined for a
   ** LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_ROLEMEMBER_ATTRIBUTE}.
   **
   ** @return                    of the organization role membership attribute.
   */
  public final String organizationRoleMemberAttribute() {
    return this.connector.organizationRoleMemberAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationRoleMemberAttributeDN
  /**
   ** Returns <code>true</code> if the organization role membership attribute
   ** will contain a fullqualified DN.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_ROLEMEMBER_ATTRIBUTE_DN}.
   **
   ** @return                    <code>true</code> if the organization role
   **                            membership attribute will contain a
   **                            fullqualified DN; otherwise <code>false</code>
   */
  public final boolean organizationRoleMemberAttributeDN() {
    return this.connector.organizationRoleMemberAttributeDN();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationGroupMemberAttribute
  /**
   ** Returns the of the organization group membership attribute defined for a
   ** LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_GROUPMEMBER_ATTRIBUTE}.
   **
   ** @return                    of the organization group membership attribute.
   */
  public final String organizationGroupMemberAttribute() {
    return this.connector.organizationGroupMemberAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationGroupMemberAttributeDN
  /**
   ** Returns <code>true</code> if the organization group membership attribute
   ** will contain a fullqualified DN.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_GROUPMEMBER_ATTRIBUTE_DN}.
   **
   ** @return                    <code>true</code> if the organization group
   **                            membership attribute will contain a
   **                            fullqualified DN; otherwise <code>false</code>
   */
  public final boolean organizationGroupMemberAttributeDN() {
    return this.connector.organizationGroupMemberAttributeDN();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountObjectClass
  /**
   ** Returns the name of the account object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ACCOUNT_OBJECT_CLASS} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ACCOUNT_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the account object class.
   */
  public final String accountObjectClass() {
    return this.connector.accountObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountObjectPrefix
  /**
   ** Returns the name of the account object prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ACCOUNT_OBJECT_PREFIX} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ACCOUNT_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the account object prefix.
   */
  public final String accountObjectPrefix() {
    return this.connector.accountObjectPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPasswordAttribute
  /**
   ** Returns the name of the account password attribute defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_PASSWORD_ATTRIBUTE}.
   ** <p>
   ** If {@link DirectoryConstant#ACCOUNT_PASSWORD_ATTRIBUTE} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ACCOUNT_PASSWORD_ATTRIBUTE_DEFAULT}
   **
   ** @return                    the name of the account password attribute.
   */
  public final String accountPasswordAttribute() {
    return this.connector.accountPasswordAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountMultiValueAttribute
  /**
   ** Returns the name of the account multi-valued attribute names for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_MULTIVALUE_ATTRIBUTE}.
   **
   ** @return                   the name of the account multi-valued attribute
   **                           names.
   */
  public final List<String> accountMultiValueAttribute() {
    return this.connector.accountMultiValueAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRoleMemberAttribute
  /**
   ** Returns the name of the account role membership attribute defined for a
   ** LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_ROLEMEMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the account role membership
   **                            attribute defined for a LDAP server.
   */
  public final String accountRoleMemberAttribute() {
    return this.connector.accountRoleMemberAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRoleMemberAttributeDN
  /**
   ** Returns <code>true</code> if the account role membership attribute will
   ** contain a fullqualified DN.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_ROLEMEMBER_ATTRIBUTE_DN}.
   **
   ** @return                    <code>true</code> if the account role
   **                            membership attribute will contain a
   **                            fullqualified DN; otherwise <code>false</code>
   */
  public final boolean accountRoleMemberAttributeDN() {
    return this.connector.accountRoleMemberAttributeDN();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountGroupMemberAttribute
  /**
   ** Returns the name of the account group membership attribute defined for a
   ** LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_GROUPMEMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the account group membership
   **                            attribute defined for a LDAP server.
   */
  public final String accountGroupMemberAttribute() {
    return this.connector.accountGroupMemberAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountGroupMemberAttributeDN
  /**
   ** Returns <code>true</code> if the account group membership attribute will
   ** contain a fullqualified DN.
   ** <br>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_GROUPMEMBER_ATTRIBUTE_DN}.
   **
   ** @return                    <code>true</code> if the account group
   **                            membership attribute will contain a
   **                            fullqualified DN; otherwise <code>false</code>
   */
  public final boolean accountGroupMemberAttributeDN() {
    return this.connector.accountGroupMemberAttributeDN();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oracleContextContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the oracle context
   ** container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORACLE_CONTEXT_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#ORACLE_CONTEXT_CONTAINER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ORACLE_CONTEXT_CONTAINER_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the oracle context container.
   */
  public final String oracleContextContainer() {
    return this.connector.oracleContextContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oracleProductContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the oracle context container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORACLE_CONTEXT_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#ORACLE_PRODUCT_CONTAINER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ORACLE_PRODUCT_CONTAINER_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the oracle context container.
   */
  public final String oracleProductContainer() {
    return this.connector.oracleProductContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDomainContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the database domain
   ** container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_DOMAIN_CONTAINER} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_CONTAINER_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the catalog container.
   */
  public final String enterpriseDomainContainer() {
    return this.connector.enterpriseDomainContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDatabaseClass
  /**
   ** Returns the name of the enterprise database object class defined for a
   ** LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_CLASS} is not
   ** mapped in the underlying <code>Metadata Descriptor</code> this method
   ** returns {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the database mapping class.
   */
  public final String enterpriseDatabaseClass() {
    return this.connector.enterpriseDatabaseClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDatabasePrefix
  /**
   ** Returns the name of the enterprise database object prefix defined for a
   ** LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_PREFIX} is not
   ** mapped in the underlying <code>Metadata Descriptor</code> this method
   ** returns {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the database mapping prefix.
   */
  public final String enterpriseDatabasePrefix() {
    return this.connector.enterpriseDatabasePrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDomainClass
  /**
   ** Returns the name of the enterprise domain object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_DOMAIN_OBJECT_CLASS} is not mapped
   ** in the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the domain mapping class.
   */
  public final String enterpriseDomainClass() {
    return this.connector.enterpriseDomainClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDomainPrefix
  /**
   ** Returns the name of the enterprise domain object prefix defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_DOMAIN_OBJECT_PREFIX} is not mapped
   ** in the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the domain mapping prefix.
   */
  public final String enterpriseDomainPrefix() {
    return this.connector.enterpriseDomainPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseUserClass
  /**
   ** Returns the name of the schema mapping class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_CLASS} is not mapped
   ** in the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the schema mapping class.
   */
  public final String enterpriseSchemaClass() {
    return this.connector.enterpriseSchemaClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseUserPrefix
  /**
   ** Returns the name of the schema mapping prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_PREFIX} is not mapped
   ** in the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the schema mapping prefix.
   */
  public final String enterpriseSchemaPrefix() {
    return this.connector.enterpriseSchemaPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseRoleClass
  /**
   ** Returns the name of the enterprise role object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_CLASS} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the role mapping class.
   */
  public final String enterpriseRoleClass() {
    return this.connector.enterpriseRoleClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseRolePrefix
  /**
   ** Returns the name of the enterprise role object prefix defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_PREFIX} is not mapped
   ** in the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the role mapping prefix.
   */
  public final String enterpriseRolePrefix() {
    return this.connector.enterpriseRolePrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordOperationSecured
  /**
   ** Returns the <code>true</code> if the LDAP Server requires secure socket
   ** configuration for password oprations.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#PASSWORD_OPERATION_SECURED}.
   **
   ** @return                    <code>true</code> the LDAP Server requires
   **                            secure socket configuration for password
   **                            operations; otherwise <code>false</code>
   */
  public final boolean passwordOperationSecured() {
    return this.connector.passwordOperationSecured();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleContainer
  /**
   ** Returns the default container of newly created for roles if no
   ** distinguished name is provided a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ROLE_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            roles if no distinguished name is provided.
   */
  public final String roleContainer() {
    return this.connector.roleContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupContainer
  /**
   ** Returns the default container of newly created for groups if no
   ** distinguished name is provided a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#GROUP_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            groups if no distinguished name is provided.
   */
  public final String groupContainer() {
    return this.connector.groupContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryContainer
  /**
   ** Returns the default container of newly created for countries if no
   ** distinguished name is provided a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#COUNTRY_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            countries if no distinguished name is provided.
   */
  public final String countryContainer() {
    return this.connector.countryContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityContainer
  /**
   ** Returns the default container of newly created for localities if no
   ** distinguished name is provided a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#LOCALITY_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            localities if no distinguished name is provided.
   */
  public final String localityContainer() {
    return this.connector.localityContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationContainer
  /**
   ** Returns the default container of newly created for organizations if no
   ** distinguished name is provided a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            organizations if no distinguished name is
   **                            provided.
   */
  public final String organizationContainer() {
    return this.connector.organizationContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountContainer
  /**
   ** Returns the default container of newly created for accounts if no
   ** distinguished name is provided a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            accounts if no distinguished name is provided.
   */
  public final String accountContainer() {
    return this.connector.accountContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    final String character = parameter(VALUE_SEPARATOR);
    this.valueSeparator    = StringUtility.isEmpty(character) ? SystemConstant.COLON : character.charAt(0);

    initializeConnector();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeConnector
  /**
   ** Initalize the connector capabilities.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected void initializeConnector()
    throws TaskException {

    final String method = "initializeConnector";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.resource  = new DirectoryResource(this, this.resourceName());
      this.connector = new DirectoryConnector(this, this.resource);
      // produce the logging output only if the logging level is enabled for
      if (this.logger.debugLevel())
        debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.connector.toString()));
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDataFolder
  /**
   ** Creates the abstract {@link File} used as the data folder.
   ** <br>
   ** Convenience method to shortens the creation of the data folder specified
   ** by the task attribute {@link #DATA_FOLDER}.
   **
   ** @return                    the abstract {@link File} representing the
   **                            data directory.
   **
   ** @throws TaskException      if the folder doesn't fit the requirements.
   */
  protected final File createDataFolder()
    throws TaskException {

    if (this.dataFolder == null)
      this.dataFolder = createFolder(DATA_FOLDER);

    return this.dataFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createErrorFolder
  /**
   ** Creates the abstract {@link File} used as the error folder.
   ** <br>
   ** Convenience method to shortens the creation of the error folder specified
   ** by the task attribute {@link #ERROR_FOLDER}.
   **
   ** @return                    the abstract {@link File} representing the
   **                            directory for files with errors.
   **
   ** @throws TaskException      if the folder doesn't fit the requirements.
   */
  protected final File createErrorFolder()
    throws TaskException {

    if (this.errorFolder == null)
      this.errorFolder = createFolder(ERROR_FOLDER);

    return this.errorFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFile
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>fileName</code> in the specified <code>folderName</code> and check
   ** if the resulting file is writable by this IT Resource.
   **
   ** @param  pathName           the folder in the filesystem where the
   **                            specified <code>fileName</code> should be
   **                            contained.
   ** @param  fileName           the name of the file in the filesystem file to
   **                            wrapp.
   **
   ** @return                    the abstract {@link File} constructed from
   **                            <code>folder</code> and <code>fileName</code>.
   **
   */
  protected final File createFile(String pathName, String fileName) {
    final String method = "createFile";
    if (!pathName.endsWith(File.separator))
      pathName = pathName + File.separator;

    final String path = pathName + fileName;
    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
      debug(method, ControllerBundle.format(ControllerMessage.CREATING_FILE, path));

    return new File(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFile
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>fileName</code> in the specified <code>folderName</code> and check
   ** if the resulting file is writable by this scheduled task.
   **
   ** @param  folder             the abstract {@link File} of the filesystem
   **                            folder where the specified
   **                            <code>fileName</code> should be contained.
   ** @param  fileName           the name of the filesystem file to create and
   **                            check.
   **                            wrapp.
   **
   ** @return                    the abstract {@link File} constructed from
   **                            <code>pathName</code> and
   **                            <code>fileName</code>.
   **
   ** @throws TaskException      if the file doesn't fit the requirements.
   */
  protected File createFile(final File folder, final String fileName)
    throws TaskException {

    final File file = createFile(folder.getAbsolutePath(), fileName);

    // check it's really a file
    if (file.isDirectory()) {
      final String[] values = { fileName , file.getAbsolutePath()};
      throw new LDAPException(LDAPError.NOTAFILE, values);
    }

    return file;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFolder
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>folderName</code> and check if the resulting file is writable by
   ** this scheduled task.
   **
   ** @param  folderName         the name of the filesystem folder to create and
   **                            check.
   **
   ** @throws TaskException      if the folder doesn't fit the requirements.
   */
  private final File createFolder(final String folderName)
    throws TaskException {

    final String method = "createFolder";
    final String path   = stringValue(folderName);
    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
      debug(method, ControllerBundle.format(ControllerMessage.CREATING_FOLDER, path));

    final File   file   = new File(path);
    // check if the abstract file is a directory
    if (!file.isDirectory()) {
      String[] values = { folderName, path };
      throw new LDAPException(LDAPError.NOTAFOLDER, values);
    }

    // check if the abstract file writable
    if (!file.canWrite()) {
      String[] values = { folderName, path };
      throw new LDAPException(LDAPError.NOTWRITABLE, values);
    }

    return file;
  }
}