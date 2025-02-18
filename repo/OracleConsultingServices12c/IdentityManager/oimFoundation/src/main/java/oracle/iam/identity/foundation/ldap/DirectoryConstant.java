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
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryConstant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DirectoryConstant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

////////////////////////////////////////////////////////////////////////////////
// interface DirectoryConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
public interface DirectoryConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the standard LDAP port  */
  static final int    PORT_DEFAULT                                = 389;

  /** the default port number for secure LDAP connections. */
  static final int    PORT_DEFAULT_SECURE                         = 636;

  /** the protocol each LDAP server is using */
  static final String PROTOCOL_DEFAULT                            = "ldap";

  /** the protocol each LDAP server is using over SSL */
  static final String PROTOCOL_DEFAULT_SECURE                     = "ldaps";

  /** the property the security protocol to use. */
  static final String SECURITY_PROTOCOL                           = "ssl";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  static final String SCOPE_OBJECT                                = "Object";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  static final String SCOPE_ONELEVEL                              = "OneLevel";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  static final String SCOPE_SUBTREE                               = "SubTree";

  /** to match LDAP SSHA password encoding */
  static final int    SALT_LEN                                    = 4;
  static final String SALT_ENCODING                               = "{SSHA}";

  /** the type of certificate used to secure a connection with SSL. */
  static final String CERTIFICATE_TYPE                            = "X.509";

  /**
   ** Default value of the distinguished name attibute of an entry in a V3
   ** compliant LDAP server.
   */
  static final String DN                                          = "dn";

  static final String CONTROL                                     = "control";
  /**
   ** Default value of the name of the generic object class of an entry in a V3
   ** compliant LDAP server.
   */
  static final String OBJECT_CLASS                                = "objectClass";

  static final String VERSION                                     = "version";

  /**
   ** Default value to access the type of the entry in the changelog.
   */
  static final String CHANGE_TYPE                                 = "changeType";

  /**
   ** Default value to access the number of the entry in the changelog.
   */
  static final String CHANGE_NUMBER                               = "changeNumber";

  /**
   ** The name to specify that the type of a change in the directory is an add
   */
  static final String CHANGE_TYPE_ADD                             = "add";

  /**
   ** The name to specify that the type of a change in the directory is a
   ** modification
   */
  static final String CHANGE_TYPE_MODIFY                          = "modify";

  /**
   ** The name to specify that the type of a change in the directory is a
   ** delete
   */
  static final String CHANGE_TYPE_DELETE                          = "delete";

  static final String CHANGE_TYPE_RENAME_DN                       = "moddn";
  static final String CHANGE_TYPE_RENAME_RDN                      = "modrdn";
  static final String CHANGE_TYPE_INVALID                         = "invalid";
  static final String CHANGE_OPERATION_ADD                        = "add";
  static final String CHANGE_OPERATION_REPLACE                    = "replace";
  static final String CHANGE_OPERATION_REMOVE                     = "remove";
  static final String CHANGE_OPERATION_INVALID                    = "invalid";
  static final String RDNNEW                                      = "newrdn";
  static final String RDNOLD_DELETE                               = "deleteoldrdn";
  static final String PARENTNEW                                   = "newparent";
  static final String SUPERIORNEW                                 = "newsuperior";

  /**
   ** The name to specify that the external change log request control has to be
   ** used in search operations.
   */
  static final String CHANGELOG_CONTROL_EXTERNAL                  = "ExternalChangeLog";

  /**
   ** The name to specify that the simple page request control has to be used
   ** in search operations.
   */
  static final String PAGINATION_CONTROL_SIMPLE                   = "SimplePage";

  /**
   ** The name to specify that the virtual list request control has to be used
   ** in search operations.
   */
  static final String PAGINATION_CONTROL_VIRTUALLIST              = "VirtualList";

  /**
   ** Default value of the URL encoding.
   */
  static final String URL_ENCODING_DEFAULT                        = "UTF-8";

  /**
   ** Default value of the initial context factory.
   */
  static final String INITIAL_CONTEXT_FACTORY_DEFAULT             = "com.sun.jndi.ldap.LdapCtxFactory";

  /**
   ** Default value of the SSL security provider.
   */
  static final String SECURITY_PROVIDER_DEFAULT                   = "com.sun.net.ssl.internal.ssl.Provider";

  /**
   ** Default value of the result set pagination.
   */
  static final String PAGINATION_CONTROL_DEFAULT                  = PAGINATION_CONTROL_SIMPLE;

  /**
   ** Default value of the separator to specify multiple value for a
   ** configuration tag name.
   */
  static final String MULTIVALUE_SEPARATOR_DEFAULT                = "|";

  /**
   ** Default value of the timeout period for establishment of the LDAP
   ** connection.
   */
  static final String CONNECTION_TIMEOUT_DEFAULT                  = "3000";

  /**
   ** Default value of the timeout period the LDAP provider doesn't get an LDAP
   ** response.
   */
  static final String RESPONSE_TIMEOUT_DEFAULT                    = "10000";

  /**
   ** Default value indicating if referential integrity is enabled in target
   ** Directory Service
   */
  static final boolean REFERENTIAL_INTEGRITY_DEFAULT              = false;

  /**
   ** Default value indicating the format of a timestamp values in the target
   ** Directory Service
   */
  static final String  TIMESTAMP_FORMAT_DEFAULT                   = "yyyyMMddHHmmss.SSS'Z'";

  /**
   ** Default value to access the schema.
   */
  static final String SCHEMA_CONTAINER_DEFAULT                    = "cn=schema";

  /**
   ** Default value to access the catalog.
   */
  static final String CATALOG_CONTAINER_DEFAULT                   = "cn=catalog";

  /**
   ** Default value to access the changelog.
   */
  static final String CHANGELOG_CONTAINER_DEFAULT                 = "cn=changelog";

  /**
   ** Default value to access the changelog global uid.
   */
  static final String CHANGELOG_TARGETGUID_DEFAULT                = "targetGUID";

  /**
   ** Default value to access the distinguished name of the target entry in the
   ** changelog.
   */
  static final String CHANGELOG_TARGETDN_DEFAULT                  = "targetDN";

  /**
   ** Default value to access the changestatus.
   */
  static final String CHANGESTATUS_CONTAINER_DEFAULT              = "cn=changestatus";

  /**
   ** Default value of the name of the create timestamp attribute of an entry in
   ** a V3 compliant LDAP server.
   */
  static final String ENTRY_CREATED_ATTRIBUTE_DEFAULT             = "createTimestamp";

  /**
   ** Default value of the name of the modify timestamp attribute of an entry in
   ** a V3 compliant LDAP server.
   */
  static final String ENTRY_MODIFIED_ATTRIBUTE_DEFAULT            = "modifyTimestamp";

  /**
   ** Default value of the name of the group membership attribute name of an
   ** entry in the LDAP server.
   ** <b>Note</b>:
   ** V3 compliant LDAP servers doesn't have such attributes it either a schema
   ** extension to the standard by a structural or auxilary class or in case of
   ** a Microssoft Active Directory a vendor specific extension.
   */
  static final String BACKLINK_MEMBERSHIP_DEFAULT                 = "memberOf";

  /**
   ** Default value of the name of the membership of an entry in a V3 compliant
   ** LDAP server in a group.
   */
  static final String GROUP_MEMBERSHIP_DEFAULT                    = "uniqueMember";

  /**
   ** Default value of the domain objectClass of an entry in a V3 compliant LDAP
   ** server.
   */
  static final String DOMAIN_OBJECT_CLASS_DEFAULT                 = "domain";

  /**
   ** Default value of the domain prefix of an entry in a V3 compliant LDAP
   ** server.
   */
  static final String DOMAIN_OBJECT_PREFIX_DEFAULT                = "dc";

  /**
   ** Default value of the country objectClass of an entry in a V3 compliant
   ** LDAP server.
   */
  static final String COUNTRY_OBJECT_CLASS_DEFAULT                = "country";

  /**
   ** Default value of the country prefix of an entry in a V3 compliant LDAP
   ** server.
   */
  static final String COUNTRY_OBJECT_PREFIX_DEFAULT               = "c";

  /**
   ** Default value of the locality objectClass of an entry in a V3 compliant
   ** LDAP server.
   */
  static final String LOCALITY_OBJECT_CLASS_DEFAULT               = "locality";

  /**
   ** Default value of the locality prefix of an entry in a V3 compliant LDAP
   ** server.
   */
  static final String LOCALITY_OBJECT_PREFIX_DEFAULT              = "l";

  /**
   ** Default value of the organization objectClass of an entry in a V3
   ** compliant LDAP server.
   */
  static final String ORGANIZATION_OBJECT_CLASS_DEFAULT           = "organization";

  /**
   ** Default value of the organization prefix of an entry in a V3 compliant
   ** LDAP server.
   */
  static final String ORGANIZATION_OBJECT_PREFIX_DEFAULT          = "o";

  /**
   ** Default value of the organizationalUnit objectClass of an entry in a V3
   ** compliant LDAP server.
   */
  static final String ORGANIZATIONALUNIT_OBJECT_CLASS_DEFAULT     = "organizationalUnit";

  /**
   ** Default value of the organizational unit prefix of an entry in a V3
   ** compliant LDAP server.
   */
  static final String ORGANIZATIONALUNIT_OBJECT_PREFIX_DEFAULT    = "ou";

  /**
   ** Default value of the group objectClass of an entry in a V3 compliant LDAP
   ** server.
   */
  static final String GROUP_OBJECT_CLASS_DEFAULT                  = "groupOfUniqueNames";

  /**
   ** Default value of the group prefix of an entry in a V3 compliant LDAP
   ** server.
   */
  static final String GROUP_OBJECT_PREFIX_DEFAULT                 = "cn";

  /**
   ** Default value of the account objectClass of an entry in a V3 compliant
   ** LDAP server.
   */
  static final String ACCOUNT_OBJECT_CLASS_DEFAULT                = "inetOrgPerson";

  /**
   ** Default value of the account prefix of an entry in a V3 compliant LDAP
   ** server in a group.
   */
  static final String ACCOUNT_OBJECT_PREFIX_DEFAULT               = "cn";

  /**
   ** Default value of the account password attribute name of an entry in a V3
   ** compliant LDAP server in a group.
   */
  static final String ACCOUNT_PASSWORD_ATTRIBUTE_DEFAULT          = "userPassword";

  /**
   ** Default value to access the Oracle Product specific context in a Directory
   ** Service.
   */
  static final String ORACLE_CONTEXT_CONTAINER_DEFAULT            = "cn=OracleContext";

  /**
   ** Default value to access the Oracle Product specific context in a Directory
   ** Service.
   */
  static final String ORACLE_PRODUCT_CONTAINER_DEFAULT            = "cn=Products," + ORACLE_CONTEXT_CONTAINER_DEFAULT;

  /**
   ** Default value to access the Oracle Product specific context in a Directory
   ** Service.
   */
  static final String ENTERPRISE_DOMAIN_CONTAINER_DEFAULT         = "cn=OracleDBSecurity," + ORACLE_PRODUCT_CONTAINER_DEFAULT;

  /**
   ** The name of the Enterprise Domain that is prt of the standard installation
   ** within a security Realm.
   */
  static final String ENTERPRISE_STANDARD_DOMAIN                  = "OracleDefaultDomain";

  /**
   ** Default value of the enterprise database object class of a registered
   ** database.
   */
  static final String ENTERPRISE_DATABASE_OBJECT_CLASS_DEFAULT    = "orclDBServer";

  /**
   ** Default value of the enterprise database object prefix of a registered
   ** database.
   */
  static final String ENTERPRISE_DATABASE_OBJECT_PREFIX_DEFAULT   = "cn";

  /**
   ** Default value of the object class of an Enterprise Security Domain.
   */
  static final String ENTERPRISE_DOMAIN_OBJECT_CLASS_DEFAULT      = "orclDBEnterpriseDomain";

  /**
   ** Default value of the object prefix of of an Enterprise Security Domain.
   */
  static final String ENTERPRISE_DOMAIN_OBJECT_PREFIX_DEFAULT     = "cn";

  /**
   ** Default value of the schema mapping object class of an entry.
   */
  static final String ENTERPRISE_SCHEMA_OBJECT_CLASS_DEFAULT      = "orclDBEntryLevelMapping";

  /**
   ** Default value of the schema mapping prefix of an entry.
   */
  static final String ENTERPRISE_SCHEMA_OBJECT_PREFIX_DEFAULT     = "cn";

  /**
   ** Default value of the attribute schema mapping account attribute of an
   ** entry.
   */
  static final String ENTERPRISE_SCHEMA_ACCOUNT_DEFAULT           = "orclDBDistinguishedName";

  /**
   ** Default value of the object class of an Enterprise Security Role.
   */
  static final String ENTERPRISE_ROLE_OBJECT_CLASS_DEFAULT        = "orclDBEnterpriseRole";

  /**
   ** Default value of the object prefix of an Enterprise Security Role.
   */
  static final String ENTERPRISE_ROLE_OBJECT_PREFIX_DEFAULT       = "cn";

  /**
   ** Default value of the enterprise role object member attribute of an entry.
   */
  static final String ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT    = "uniqueMember";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the URL encoding.
   */
  static final String URL_ENCODING                                = "url-encoding";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the Initial Context factory.
   */
  static final String INITIAL_CONTEXT_FACTORY                     = "context-factory";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the Initial Context factory.
   */
  static final String SECURITY_PROVIDER                           = "security-provider";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the character that separates multiple values for the same entry
   ** tag name.
   */
  static final String MULTIVALUE_SEPARATOR                        = "multi-value-separator";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the <code>Metadata Descriptor</code> that defines
   ** the servers that should be used if it will not be possible to establish a
   ** connection to the primary server defined in the <code>IT Resource</code>.
   ** <p>
   ** To configure failover systems, you need to specify the complete URL in the
   ** following format:
   ** ldap://SERVERADDRESS:PORT/ ldap://SERVERADDRESS1:PORT1/
   */
  static final String FAILOVER_CONFIGURATION                      = "failover";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the timeout period for establishment of the LDAP connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an LDAP
   ** server. When connection pooling has been requested, this property also
   ** specifies the maximum wait time or a connection when all connections in
   ** pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the LDAP provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   */
  static final String CONNECTION_TIMEOUT                          = "connection-timeout";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the timeout period the LDAP provider doesn't get an LDAP
   ** response.
   ** <p>
   ** If this property has not been specified, the default is to wait for the
   ** response until it is received.
   */
  static final String RESPONSE_TIMEOUT                            = "response-timeout";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify if referential integrity is enabled in target Directory
   ** Service.
   ** <p>
   ** Referential integrity is the process of maintaining consistent
   ** relationships among sets of data. If referential Integrity is enabled in
   ** the Directory Service, whenever an entry updated in the directory, the
   ** server also updates other entries that refer to that entry. For example,
   ** if you a account entry is removed from the directory, and the account is a
   ** member of a group, the server also removes the account from the group. If
   ** referential integrity is not enabled, the user remains a member of the
   ** group until manually removed.
   ** <p>
   ** Referential integrity is not enabled by default.
   */
  static final String REFERENTIAL_INTEGRITY                       = "referential-integrity";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the format of a timestamp value in target Directory Service.
   */
  static final String TIMESTAMP_FORMAT                            = "timestamp-format";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the <code>SearchControl</code> that instruct the
   ** server to return the results of a search request in smaller, more
   ** manageable packets rather than in one large block.
   */
  static final String PAGINATION_CONTROL                          = "pagination-control";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify that the expected {@link #PAGINATION_CONTROL} is treated as
   ** critical extension.
   */
  static final String PAGINATION_CONTROL_CRITICAL                 = "pagination-control-critical";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy where a V3 compliant LDAP server will publish schema entries.
   */
  static final String SCHEMA_CONTAINER                            = "schema-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy where a V3 compliant LDAP server will publish catalog entries.
   */
  static final String CATALOG_CONTAINER                           = "catalog-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy where a V3 compliant LDAP server will publish changelog
   ** entries.
   */
  static final String CHANGELOG_CONTAINER                         = "changelog-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name of the change type that a V3 compliant LDAP
   ** server provides in the changelog.
   */
  static final String CHANGELOG_CHANGETYPE                        = "changelog-changeType";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name of the change number that a V3 compliant
   ** LDAP server provides in the changelog.
   */
  static final String CHANGELOG_CHANGENUMBER                      = "changelog-changeNumber";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name to access the global uid that a V3
   ** compliant LDAP server provides in the changelog.
   */
  static final String CHANGELOG_TARGETGUID                        = "changelog-targetGUID";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name to access the distinguished name of a
   ** change subject that a V3 compliant LDAP server provides in the changelog.
   */
  static final String CHANGELOG_TARGETDN                          = "changelog-targetDN";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy where a V3 compliant LDAP server will publish change status
   ** entries.
   */
  static final String CHANGESTATUS_CONTAINER                     = "changestatus-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the name of the object class attribute of an entry in a V3
   ** compliant LDAP server.
   */
  static final String OBJECT_CLASS_NAME                           = "object-class-name";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the object attributes that are binary attributes of
   ** an entry in the LDAP server.
   */
  static final String BINARY_OBJECT_ATTRIBUTE                     = "binary";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the names of the distinguished name attributes that may be
   ** contained any entry in a V3 compliant LDAP server.
   */
  static final String DISTINGUISHED_NAMES                         = "distinguishedNames";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the distinguished name attribute of an entry in a
   ** V3 compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 entry distinguished name attribute is
   ** <code>DN</code>.
   */
  static final String DISTINGUISHED_NAME_ATTRIBUTE                = "distinguished-name-attribute";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify to normalize distinguished name attributes of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** Normalization means that if the distinguished names are returned case
   ** sensitive no conversion is necessary. If the results are inconclusive like
   ** in a Sun Java System Directory it is recommended to set this property to
   ** <code>false</code>.
   */
  static final String DISTINGUISHED_NAME_CASESENSITIVE            = "distinguished-name-casesensitive";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the create timestamp attribute of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 entry created timestamp attribute is
   ** <code>createTimestamp</code>.
   */
  static final String ENTRY_CREATED_ATTRIBUTE                     = "entry-created-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the modified timestamp attribute of an entry in a
   ** V3 compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 entry modified timestamp fieldname is
   ** <code>modifyTimestamp</code>.
   */
  public static final String ENTRY_MODIFIED_ATTRIBUTE             = "entry-modified-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the role object class of an entry in the LDAP
   ** server.
   */
  static final String ROLE_OBJECT_CLASS                           = "role-object-class";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the role object prefix of an entry in the LDAP
   ** server.
   */
  static final String ROLE_OBJECT_PREFIX                          = "role-object-prefix";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the object role membership prefix of an entry in
   ** the LDAP server.
   */
  static final String ROLE_OBJECTMEMBER_ATTRIBUTE                 = "role-member-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the group object class of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard object class name of a LDAP V3 group is
   ** <code>groupOfUniqueNames</code>.
   */
  static final String GROUP_OBJECT_CLASS                          = "group-object-class";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the group object prefix of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 group entry is <code>cn</code>.
   */
  static final String GROUP_OBJECT_PREFIX                         = "group-object-prefix";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the object group membership prefix of an entry in a
   ** V3 compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 User group membership is
   ** <code>uniqueMember</code>.
   **
   ** @see #GROUP_MEMBERSHIP_DEFAULT
   */
  static final String GROUP_OBJECTMEMBER_ATTRIBUTE                = "group-member-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the domain object class of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard object class of a LDAP V3 account entry is
   ** <code>domain</code>.
   */
  static final String DOMAIN_OBJECT_CLASS                         = "domain-object-class";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the domain object prefix of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 country is <code>dc</code>.
   */
  static final String DOMAIN_OBJECT_PREFIX                        = "domain-object-prefix";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the names of the multi-valued attributes of a domain entry in
   ** the LDAP server.
   ** <p>
   ** If there has to be specified more than one attribute the list of these
   ** attributes has to be separated by the character defined by the property
   ** {@link #MULTIVALUE_SEPARATOR} of this configuration.
   */
  static final String DOMAIN_MULTIVALUE_ATTRIBUTE                 = "account-multi-value-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the country object class of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard object class of a LDAP V3 account entry is
   ** <code>country</code>.
   */
  static final String COUNTRY_OBJECT_CLASS                        = "country-object-class";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the country object prefix of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 country is <code>c</code>.
   */
  static final String COUNTRY_OBJECT_PREFIX                       = "country-object-prefix";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the names of the multi-valued attributes of a country entry in
   ** the LDAP server.
   ** <p>
   ** If there has to be specified more than one attribute the list of those
   ** attributes has to be separated by the character defined by the property
   ** {@link #MULTIVALUE_SEPARATOR} of this configuration.
   */
  static final String COUNTRY_MULTIVALUE_ATTRIBUTE                = "country-multi-value-aAttribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the locality object class of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard object class of a LDAP V3 account entry is
   ** <code>locality</code>.
   */
  static final String LOCALITY_OBJECT_CLASS                       = "locality-object-class";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the Locality object prefix of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 locality is <code>l</code>.
   */
  static final String LOCALITY_OBJECT_PREFIX                      = "locality-object-prefix";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the names of the multi-valued attributes of a locality entry in
   ** the LDAP server.
   ** <p>
   ** If there has to be specified more than one attribute the list of those
   ** attributes has to be separated by the character defined by the property
   ** {@link #MULTIVALUE_SEPARATOR} of this configuration.
   */
  static final String LOCALITY_MULTIVALUE_ATTRIBUTE               = "locality-multi-value-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organization object class of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard object class name of a LDAP V3 Organization is
   ** <code>organization</code>.
   */
  static final String ORGANIZATION_OBJECT_CLASS                   = "organization-object-class";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organization object prefix of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 organization is <code>o</code>.
   */
  static final String ORGANIZATION_OBJECT_PREFIX                  = "organization-object-prefix";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the names of the multi-valued attributes of an organization
   ** entry in the LDAP server.
   ** <p>
   ** If there has to be specified more than one attribute the list of those
   ** attributes has to be separated by the character defined by the property
   ** {@link #MULTIVALUE_SEPARATOR} of this configuration.
   */
  static final String ORGANIZATION_MULTIVALUE_ATTRIBUTE           = "organization-multi-value-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organization role membership of an entry in the
   ** LDAP server.
   */
  static final String ORGANIZATION_ROLEMEMBER_ATTRIBUTE           = "organization-role-membership-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify how the role membership attribute of an organizational entry
   ** will be handled.
   */
  static final String ORGANIZATION_ROLEMEMBER_ATTRIBUTE_DN        = "organization-role-membership-attribute-dn";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organization group membership of an entry in
   ** the LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 entry group membership is
   ** <code>uniqueMember</code>.
   */
  static final String ORGANIZATION_GROUPMEMBER_ATTRIBUTE          = "organization-group-membership-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify how the group membership attribute of an organizational entry
   ** will be handled.
   */
  static final String ORGANIZATION_GROUPMEMBER_ATTRIBUTE_DN       = "organization-group-membership-attribute-dn";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organization unit object class of an entry in a
   ** V3 compliant LDAP server.
   ** <p>
   ** The standard object class name of a LDAP V3 Organization is
   ** <code>organizationalUnit</code>.
   */
  static final String ORGANIZATIONALUNIT_OBJECT_CLASS             = "organizationunit-object-class";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organization unit object prefix of an entry in
   ** a V3 compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 organization is <code>ou</code>.
   */
  static final String ORGANIZATIONALUNIT_OBJECT_PREFIX            = "organizationunit-object-prefix";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the names of the multi-valued attributes of an organizational
   ** unit entry in the LDAP server.
   ** <p>
   ** If there has to be specified more than one attribute the list of these
   ** attributes has to be separated by the character defined by the property
   ** {@link #MULTIVALUE_SEPARATOR} of this configuration.
   */
  static final String ORGANIZATIONALUNIT_MULTIVALUE_ATTRIBUTE     = "organizationunit-multi-value-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organizational unit role membership of an entry
   ** in the LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 entry role membership is
   ** <code>uniqueMember</code>.
   */
  static final String ORGANIZATIONALUNIT_ROLEMEMBER_ATTRIBUTE     = "organizationunit-role-membership-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify how the role membership attribute of an organizational unit
   ** entry will be handled.
   */
  static final String ORGANIZATIONALUNIT_ROLEMEMBER_ATTRIBUTE_DN  = "organizationunit-role-membership-attribute-dn";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organizational unit group membership of an
   ** entry in the LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 entry group membership is
   ** <code>uniqueMember</code>.
   */
  static final String ORGANIZATIONALUNIT_GROUPMEMBER_ATTRIBUTE    = "organizationunit-group-membership-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify how the group membership attribute of an organizational unit
   ** entry will be handled.
   */
  static final String ORGANIZATIONALUNIT_GROUPMEMBER_ATTRIBUTE_DN = "organizationunit-group-membership-attribute-dn";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the account object class of an entry in a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard object class of a LDAP V3 account entry is
   ** <code>inetOrgPerson</code>.
   */
  static final String ACCOUNT_OBJECT_CLASS                        = "account-object-class";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the account object prefix of an entryin a V3
   ** compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 account entry is <code>cn</code>.
   */
  static final String ACCOUNT_OBJECT_PREFIX                       = "account-object-prefix";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the account password attribute name of an entry in
   ** a V3 compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 account password attribute is
   ** <code>userPassword</code>.
   */
  static final String ACCOUNT_PASSWORD_ATTRIBUTE                  = "account-password-attribute";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the names of the multi-valued attributes of an account entry in
   ** the LDAP server.
   ** <p>
   ** If there has to be specified more than one attribute the list of these
   ** attributes has to be separated by the character defined by the property
   ** {@link #MULTIVALUE_SEPARATOR} of this configuration.
   */
  static final String ACCOUNT_MULTIVALUE_ATTRIBUTE                = "account-multi-value-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the account role membership attribute of an entry
   ** in the LDAP server.
   */
  static final String ACCOUNT_ROLEMEMBER_ATTRIBUTE                = "account-role-membership-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify how the role membership attribute will be handled.
   */
  static final String ACCOUNT_ROLEMEMBER_ATTRIBUTE_DN             = "account-role-membership-attribute-dn";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the account group membership attribute of an entry
   ** in a V3 compliant LDAP server.
   ** <p>
   ** The standard prefix of a LDAP V3 entry group membership is
   ** <code>uniqueMember</code>.
   */
  public static final String ACCOUNT_GROUPMEMBER_ATTRIBUTE               = "account-group-membership-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify how the group membership attribute will be handled.
   */
  static final String ACCOUNT_GROUPMEMBER_ATTRIBUTE_DN            = "account-group-membership-attribute-dn";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where Oracle specific entries are located
   ** in the LDAP server.
   ** <p>
   ** The standard container in a security realm is
   ** <code>cn=OracleContext</code>.
   */
  static final String ORACLE_CONTEXT_CONTAINER                    = "context-container";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where product registrations are located
   ** in the LDAP server.
   ** <p>
   ** The standard container in a security realm is
   ** <code>cn=Products,cn=OracleContext</code>.
   */
  static final String ORACLE_PRODUCT_CONTAINER                    = "product-container";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where database domains are located in the
   ** LDAP server.
   ** <p>
   ** The standard container in a security realm is
   ** <code>cn=OracleDBSecurity,cn=Products,cn=OracleContext</code>.
   */
  static final String ENTERPRISE_DOMAIN_CONTAINER                 = "domain-container";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the enterprise database object class of an entry in
   ** the LDAP server.
   ** <p>
   ** The standard object class in a security realm is
   ** <code>orclDBServer</code>.
   */
  static final String ENTERPRISE_DATABASE_OBJECT_CLASS            = "enterprise-database-object-class";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the enterprise database object prefix of an entry
   ** in the LDAP server.
   ** <p>
   ** The standard object class in a security realm is <code>cn</code>.
   */
  static final String ENTERPRISE_DATABASE_OBJECT_PREFIX           = "enterprise-database-object-prefix";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the enterprise domain object class of an entry in
   ** the LDAP server.
   ** <p>
   ** The standard object class in a security realm is
   ** <code>orclDBEnterpriseDomain</code>.
   */
  static final String ENTERPRISE_DOMAIN_OBJECT_CLASS              = "enterprise-domain-object-class";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the enterprise domain object prefix of an entry in
   ** the LDAP server.
   ** <p>
   ** The standard object class in a security realm is <code>cn</code>.
   */
  static final String ENTERPRISE_DOMAIN_OBJECT_PREFIX             = "enterprise-domain-object-prefix";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the schema mapping object class of an entry in the
   ** LDAP server.
   ** <p>
   ** The standard object class in a security realm is
   ** <code>orclDBEntryLevelMapping</code>.
   */
  static final String ENTERPRISE_SCHEMA_OBJECT_CLASS              = "enterprise-schema-object-class";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the schema mapping object prefix of an entry in the
   ** LDAP server.
   ** <p>
   ** The standard object class in a security realm is <code>cn</code>.
   */
  static final String ENTERPRISE_SCHEMA_OBJECT_PREFIX             = "enterprise-schema-object-prefix";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the schema mapping object prefix of an entry in the
   ** LDAP server.
   ** <p>
   ** The standard account DN maping in a security realm is
   ** <code>orclDBDistinguishedName</code>.
   */
  static final String ENTERPRISE_SCHEMA_ACCOUNT                   = "enterprise-schema-account";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the enterprise role object class of an entry in the
   ** LDAP server.
   ** <p>
   ** The standard object class in a security realm is
   ** <code>orclDBEnterpriseRole</code>.
   */
  static final String ENTERPRISE_ROLE_OBJECT_CLASS                = "enterprise-role-object-class";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the enterprise role object prefix of an entry in
   ** the LDAP server.
   ** <p>
   ** The standard object class in a security realm is <code>cn</code>.
   */
  static final String ENTERPRISE_ROLE_OBJECT_PREFIX               = "enterprise-role-object-prefix";

  /**
   ** Attribute tag which might be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the object group membership prefix of an entry in a
   ** Enterprise Role.
   ** <p>
   ** The standard prefix of a Enterprise Role membership is
   ** <code>uniqueMember</code>.
   **
   ** @see #ENTERPRISE_ROLE_MEMBER_ATTRIBUTE_DEFAULT
   */
  static final String ENTERPRISE_ROLE_MEMBER_ATTRIBUTE            = "enterprise-role-object-member-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify if the LDAP server requires secure sockets on password
   ** operations.
   */
  static final String PASSWORD_OPERATION_SECURED                  = "password-operation-secured";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify if the entitlement loaded from a LDAP server needs to be
   ** prefixed with the internal system identifier and/or the name of the
   ** <code>IT Resource</code>.
   */
  static final String ENTITLEMENT_PREFIX_REQUIRED                 = "entitlement-prefix-required";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where roles should be created in the LDAP
   ** server.
   */
  static final String ROLE_CONTAINER                              = "role-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where groups should be created in the
   ** LDAP server.
   */
  static final String GROUP_CONTAINER                             = "group-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where domains should be created in the
   ** LDAP server.
   */
  static final String DOMAIN_CONTAINER                            = "domain-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where countries should be created in the
   ** LDAP server.
   */
  static final String COUNTRY_CONTAINER                           = "country-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where accounts should be created in the
   ** LDAP server.
   */
  static final String ACCOUNT_CONTAINER                           = "account-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where generics should be created in the
   ** LDAP server.
   */
  static final String GENERIC_CONTAINER                           = "generic-container";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where localities should be created in the
   ** LDAP server.
   */
  static final String LOCALITY_CONTAINER                          = "locality-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where organizations should be created in
   ** the LDAP server.
   */
  static final String ORGANIZATION_CONTAINER                      = "organization-container";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify name of the container where organizational units should be
   ** created in the LDAP server.
   */
  static final String ORGANIZATIONALUNIT_CONTAINER                = "organizationalunit-container";
}