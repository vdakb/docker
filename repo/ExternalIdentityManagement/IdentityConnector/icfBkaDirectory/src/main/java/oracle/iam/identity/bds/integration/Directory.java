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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   Directory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Directory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.bds.integration;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import oracle.hst.foundation.utility.CollectionUtility;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassUtil;

////////////////////////////////////////////////////////////////////////////////
// interface Directory
// ~~~~~~~~~ ~~~~~~~~~
public interface Directory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the distinguished name prefix of an entry in a V3
   ** compliant Directory Service.
   */
  static final String     DISTINGUISHED_NAME_PREFIX_DEFAULT      = "dn";

  /**
   ** Default value of the name of the generic object class of an entry in a V3
   ** compliant Directory Service.
   */
  static final String     OBJECT_CLASS_DEFAULT                   = "objectClass";

  static final String     VERSION                                = "version";

  /**
   ** Default value to access the type of the entry in the changelog.
   */
  static final String     CHANGE_TYPE_DEFAULT                    = "changeType";

  /**
   ** Default value to access the number of the entry in the changelog.
   */
  static final String     CHANGE_NUMBER_DEFAULT                  = "changeNumber";

  /**
   ** Default value of the separator to specify multiple value for a
   ** configuration tag name.
   */
  static final String     MULTIVALUE_SEPARATOR_DEFAULT           = "|";

  /**
   ** Default value of the URL encoding.
   */
  static final String     URL_ENCODING_DEFAULT                   = "UTF-8";

  /**
   ** Default value of the initial context factory.
   */
  static final String     INITIAL_CONTEXT_FACTORY_DEFAULT        = "com.sun.jndi.ldap.LdapCtxFactory";

  /**
   ** Default value of the TLS security provider.
   */
  static final String     SECURITY_PROVIDER_DEFAULT              = "com.sun.net.ssl.internal.ssl.Provider";

  /**
   ** Default value indicating if referential integrity is enabled in target
   ** Directory Service
   */
  static final boolean     REFERENTIAL_INTEGRITY_DEFAULT         = false;

  /**
   ** Default value indicating the format of a timestamp values in the target
   ** Directory Service
   */
  static final String      TIMESTAMP_FORMAT_DEFAULT              = "yyyyMMddHHmmss.SSS'Z'";

  /**
   ** Default value to access the schema.
   */
  static final String      SCHEMA_CONTAINER_DEFAULT              = "cn=schema";

  /**
   ** Default value to access the catalog.
   */
  static final String      CATALOG_CONTAINER_DEFAULT             = "cn=catalog";

  /**
   ** Default value to access the changelog.
   */
  static final String      CHANGELOG_CONTAINER_DEFAULT           = "cn=changelog";

  /**
   ** Default value to access the changelog global uid.
   */
  static final String      CHANGELOG_TARGETGUID_DEFAULT          = "targetGUID";

  /**
   ** Default value to access the distinguished name of the target entry in the
   ** changelog.
   */
  static final String      CHANGELOG_TARGETDN_DEFAULT            = "targetDN";

  /**
   ** Default value of the name of the group membership attribute name of an
   ** entry in the Directory Service.
   ** <b>Note</b>:
   ** V3 compliant Directory Services doesn't have such attributes it either a
   ** schema extension to the standard by a structural or auxilary class or in
   ** case of a Microssoft Active Directory a vendor specific extension.
   */
  static final String      BACKLINK_MEMBER_DEFAULT               = "memberOf";

  /**
   ** Default value of the name of the membership of an entry in a V3 compliant
   ** Directory Service in a group.
   */
  static final String      GROUP_MEMBER_PREFIX_DEFAULT           = "uniqueMember";

  /**
   ** Default value of the group objectClass of an entry in a V3 compliant
   ** Directory Service.
   */
  static final String      GROUP_CLASS_DEFAULT                   = "groupOfUniqueNames";

  /**
   ** Default value of the group prefix of an entry in a V3 compliant
   ** Directory Service.
   */
  static final String      GROUP_PREFIX_DEFAULT                  = "cn";

  /**
   ** Default value of the account objectClass of an entry in a V3 compliant
   ** Directory Service.
   */
  static final String      ACCOUNT_CLASS_DEFAULT                 = "inetOrgPerson";

  /**
   ** Default value of the account prefix of an entry in a V3 compliant
   ** Directory Service in a group.
   */
  static final String      ACCOUNT_PREFIX_DEFAULT                = "uid";

  /**
   ** Default value of the account password attribute name of an entry in a V3
   ** compliant Directory Service in a group.
   */
  static final String      ACCOUNT_CREDENTIAL_PREFIX_DEFAULT     = "userPassword";

  /**
   ** Default value indicating if passwordvalue of an entry in a V3 compliant
   ** Directory Service needs quotes.
   */
  static final boolean     ACCOUNT_CREDENTIAL_QUOTED_DEFAULT     = false;

  /**
   ** Default value to access the Oracle Product specific context in a Directory
   ** Service.
   */
  static final String      ORACLE_CONTEXT_CONTAINER_DEFAULT      = "cn=OracleContext";

  /**
   ** Default value to access the Oracle Product specific context in a Directory
   ** Service.
   */
  static final String      ORACLE_PRODUCT_CONTAINER_DEFAULT      = "cn=Products," + ORACLE_CONTEXT_CONTAINER_DEFAULT;

  /**
   ** Default value to access the Oracle Product specific context in a Directory
   ** Service.
   */
  static final String      ENTERPRISE_DOMAIN_CONTAINER_DEFAULT   = "cn=OracleDBSecurity," + ORACLE_PRODUCT_CONTAINER_DEFAULT;

  /**
   ** The name of the Enterprise Domain that is prt of the standard installation
   ** within a security Realm.
   */
  static final String      ENTERPRISE_STANDARD_DOMAIN            = "OracleDefaultDomain";

  /**
   ** Default value of the enterprise database object class of a registered
   ** database.
   */
  static final String      ENTERPRISE_DATABASE_CLASS_DEFAULT     = "orclDBServer";

  /**
   ** Default value of the enterprise database object prefix of a registered
   ** database.
   */
  static final String      ENTERPRISE_DATABASE_PREFIX_DEFAULT    = "cn";

  /**
   ** Default value of the object class of an Enterprise Security Domain.
   */
  static final String      ENTERPRISE_DOMAIN_CLASS_DEFAULT       = "orclDBEnterpriseDomain";

  /**
   ** Default value of the object prefix of of an Enterprise Security Domain.
   */
  static final String      ENTERPRISE_DOMAIN_PREFIX_DEFAULT      = "cn";

  /**
   ** Default value of the schema mapping object class of an entry.
   */
  static final String      ENTERPRISE_SCHEMA_CLASS_DEFAULT       = "orclDBEntryLevelMapping";

  /**
   ** Default value of the schema mapping prefix of an entry.
   */
  static final String      ENTERPRISE_SCHEMA_PREFIX_DEFAULT      = "cn";

  /**
   ** Default value of the attribute schema mapping account attribute of an
   ** entry.
   */
  static final String      ENTERPRISE_SCHEMA_ACCOUNT_DEFAULT     = "orclDBDistinguishedName";

  /**
   ** Default value of the object class of an Enterprise Security Role.
   */
  static final String      ENTERPRISE_ROLE_CLASS_DEFAULT         = "orclDBEnterpriseRole";

  /**
   ** Default value of the object prefix of an Enterprise Security Role.
   */
  static final String      ENTERPRISE_ROLE_PREFIX_DEFAULT        = "cn";

  /**
   ** Default value of the enterprise role object member attribute of an entry.
   */
  static final String      ENTERPRISE_ROLE_MEMBER_PREFIX_DEFAULT = "uniqueMember";

  /**
   ** Default value of the name of the server-assigned Universally Unique
   ** Identifier UUID for the entry in a V3 compliant Directory Service.
   */
  static final String      ENTRY_IDENTIFIER_PREFIX_DEFAULT       = "entryUUID";

  /**
   ** Default value of the name of the creator name attribute of an entry in
   ** a V3 compliant Directory Service.
   */
  static final String      ENTRY_CREATOR_PREFIX_DEFAULT          = "creatorsName";

  /**
   ** Default value of the name of the create timestamp attribute of an entry in
   ** a V3 compliant Directory Service.
   */
  static final String      ENTRY_CREATED_PREFIX_DEFAULT          = "createTimestamp";

  /**
   ** Default value of the name of the modifier name attribute of an entry in
   ** a V3 compliant Directory Service.
   */
  static final String      ENTRY_MODIFIER_PREFIX_DEFAULT         = "modifiersName";

  /**
   ** Default value of the name of the modified timestamp attribute of an entry in
   ** a V3 compliant Directory Service.
   */
  static final String      ENTRY_MODIFIED_PREFIX_DEFAULT         = "modifyTimestamp";

  /**
   ** Default value of the name of the srarus attribute of an entry in a V3
   ** compliant Directory Service.
   */
  static final String      ENTRY_STATUS_PREFIX_DEFAULT           = "BlaBla";

  /** the name of the any object class */
  static final String      ANY_NAME                              = ObjectClassUtil.createSpecialName("ANY");

  /** the name of the object class for a domain */
  static final String      DOMAIN_NAME                           = ObjectClassUtil.createSpecialName("DOMAIN");

  /** the name of the object class for a country */
  static final String      COUNTRY_NAME                          = ObjectClassUtil.createSpecialName("COUNTRY");

  /** the name of the object class for a locality */
  static final String      LOCALITY_NAME                         = ObjectClassUtil.createSpecialName("LOCALITY");

  /** the name of the object class for a role */
  static final String      ROLE_NAME                             = ObjectClassUtil.createSpecialName("ROLE");
  
  /** the name of the object class for a scope */
  static final String      SCOPE_NAME                            = ObjectClassUtil.createSpecialName("SCOPE");

  /** the name of the object class for an organization */
  static final String      ORGANIZATION_NAME                     = ObjectClassUtil.createSpecialName("ORGANIZATION");

  /** the name of the object class for an organization */
  static final String      ORGANIZATIONAL_UNIT_NAME              = ObjectClassUtil.createSpecialName("ORGANIZATIONALUNIT");

  /** the any object class */
  static final ObjectClass ANY                                   = new ObjectClass(ANY_NAME);

  /** the object class for a domain */
  static final ObjectClass DOMAIN                                = new ObjectClass(DOMAIN_NAME);

  /** the object class for a country */
  static final ObjectClass COUNTRY                               = new ObjectClass(COUNTRY_NAME);

  /** the object class for a locality */
  static final ObjectClass LOCALITY                              = new ObjectClass(LOCALITY_NAME);

  /** the object class for a role */
  static final ObjectClass ROLE                                  = new ObjectClass(ROLE_NAME);
  
  /** the object class for a scope */
  static final ObjectClass SCOPE                                 = new ObjectClass(SCOPE_NAME);

  /** the object class for an organization */
  static final ObjectClass ORGANIZATION                          = new ObjectClass(ORGANIZATION_NAME);

  /** the object class for an organizational uit */
  static final ObjectClass ORGANIZATIONAL_UNIT                   = new ObjectClass(ORGANIZATIONAL_UNIT_NAME);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface ICF
  // ~~~~~~~~~ ~~~
  interface ICF {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Resource
    // ~~~~~~~~~ ~~~~~~~~
    public interface Resource {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the host name for the target system Directory Service.
       */
      static final String PRIMARY_HOST                                = "primaryHost";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the listener port for the target system Directory Service.
       */
      static final String PRIMARY_PORT                                = "primaryPort";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of the root context where this <code>IT Resource</code>
       ** will be working on.
       */
      static final String  ROOT_CONTEXT                               = "rootContext";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the user name of the target system account to be used to
       ** establish a connection.
       */
      static final String PRINCIPAL_NAME                              = "principalUsername";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the password of the target system account specified by the
       ** #PRINCIPAL_NAME parameter.
       */
      static final String PRINCIPAL_PASSWORD                          = "principalPassword";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify if you plan to configure SSL to secure communication between
       ** Identity Manager and the target system.
       */
      static final String SECURE_SOCKET                               = "secureSocket";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of language the server is using.
       */
      static final String LOCALE_LANGUAGE                             = "language";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of language region the server is using.
       */
      static final String LOCALE_COUNTRY                              = "country";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of time zone the server is using.
       */
      static final String LOCALE_TIMEZONE                             = "timeZone";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the time (in milliseconds) within which the target system is
       ** expected to respond to a connection attempt.
       */
      static final String CONNECTION_TIMEOUT                          = "connectionTimeOut";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the number of consecutive attempts to be made at establishing
       ** a connection with the target system.
       */
      static final String CONNECTION_RETRY_COUNT                      = "connectionRetryCount";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the interval (in milliseconds) between consecutive attempts
       ** at establishing a connection with the target system.
       */
      static final String CONNECTION_RETRY_INTERVAL                   = "connectionRetryInterval";
      /**
       ** Attribute tag which may be defined on a <code>IT Resource</code>
       ** to specify the timeout period the Directory Service consumer doesn't
       ** get a LDAP response.
       ** <p>
       ** If this property has not been specified, the default is to wait for the
       ** response until it is received.
       */
      static final String RESPONSE_TIMEOUT                            = "responseTimeOut";
    }

    ////////////////////////////////////////////////////////////////////////////
    // interface Feature
    // ~~~~~~~~~ ~~~~~~~~
    public interface Feature {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the character that
       ** separates multiple values for the same entry tag name.
       */
      static final String MULTIVALUE_SEPARATOR                        = "multiValueSeparator";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the URL encoding.
       */
      static final String URL_ENCODING                                = "urlEncoding";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the Initial Context factory.
       */
      static final String INITIAL_CONTEXT_FACTORY                     = "contextFactory";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the Security Provider
       ** for encrypted sessions.
       */
      static final String SECURITY_PROVIDER                           = "securityProvider";

     /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the the
       ** servers that should be used if it will not be possible to establish a
       ** connection to the primary server defined in the
       ** <code>IT Resource</code>.
       ** <p>
       ** To configure failover systems, you need to specify the complete URL in
       ** the following format:
       ** ldap://SERVERADDRESS:PORT/ ldap://SERVERADDRESS1:PORT1/
       */
      static final String FAILOVER_CONFIGURATION                      = "failover";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify if referential integrity
       ** is enabled in target Directory Service.
       ** <p>
       ** Referential integrity is the process of maintaining consistent
       ** relationships among sets of data. If referential Integrity is enabled
       ** in the Directory Service, whenever an entry updated in the directory,
       ** the server also updates other entries that refer to that entry. For
       ** example, if an account entry is removed from the directory, and the
       ** account is a member of a group, the server also removes the account
       ** from the group. If referential integrity is not enabled, the account
       ** remains a member of the group until manually removed.
       ** <p>
       ** Referential integrity is not enabled by default.
       */
      static final String REFERENTIAL_INTEGRITY                       = "referentialIntegrity";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the format of a timestamp
       ** value in target Directory Service.
       */
      static final String TIMESTAMP_FORMAT                            = "timestampFormat";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the
       ** <code>SearchControl</code> that instruct the server to return the
       ** results of a search request in smaller, more manageable packets rather
       ** than in one large block.
       */
      static final String SIMPLEPAGE_CONTROL                          = "simplePageControl";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the
       ** <code>SearchControl</code> that instruct the server to return the
       ** results of a search request in smaller, more manageable packets rather
       ** than in one large block.
       */
      static final String VIRTUALLIST_CONTROL                         = "virtualListControl";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy where a V3 compliant
       ** Directory Service will publish schema entries.
       */
      static final String SCHEMA_CONTAINER                            = "schemaContainer";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy where a V3 compliant
       ** Directory Service will publish catalog entries.
       */
      static final String CATALOG_CONTAINER                           = "catalogContainer";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy where a V3 compliant
       ** Directory Service will publish changelog entries.
       */
      static final String CHANGELOG_CONTAINER                         = "changeLogContainer";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the attribute name of the
       ** change type that a V3 compliant Directory Service provides in the
       ** changelog.
       */
      static final String CHANGELOG_CHANGETYPE                        = "changeLogChangeType";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the attribute name of the
       ** change number that a V3 compliant Directory Service provides in the
       ** changelog.
      */
      static final String CHANGELOG_CHANGENUMBER                      = "changeLogChangeNumber";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the attribute name to
       ** access the global uid that a V3 compliant Directory Service provides
       ** in the changelog.
       */
      static final String CHANGELOG_TARGETGUID                        = "changeLogTargetGUID";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the attribute name to
       ** access the distinguished name of a change subject that a V3 compliant
       ** Directory Service provides in the changelog.
       */
      static final String CHANGELOG_TARGETDN                          = "changeLogTargetDN";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the name of the object
       ** class attribute of an entry in a V3 compliant Directory Service.
       */
      static final String OBJECT_CLASS_NAME                           = "objectClassName";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the object
       ** attributes that are binary attributes of an entry in the Directory
       ** Service.
       */
      static final String BINARY_OBJECT_ATTRIBUTE                     = "binary";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the names of the
       ** distinguished name attributes that may be contained any entry in a V3
       ** compliant Directory Service.
       */
      static final String DISTINGUISHED_NAME                          = "distinguishedName";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the
       ** distinguished name attribute of an entry in a V3 compliant
       ** Directory Service.
       ** <p>
       ** The standard prefix of a LDAP V3 entry distinguished name attribute is
       ** <code>DN</code>.
       */
      static final String DISTINGUISHED_NAME_PREFIX                   = "distinguishedNamePrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the
       ** server-assigned Universally Unique Identifier UUID for the entry in a
       ** V3 compliant Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 UUID is
       ** <code>entryUUID</code>.
       */
      static final String ENTRY_IDENTIFIER_PREFIX                     = "entryIdentifierPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the creator
       ** name attribute of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 create timestamp attribute is
       ** <code>createTimestamp</code>.
       */
      static final String ENTRY_CREATOR_PREFIX                        = "entryCreatorPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the create
       ** timestamp attribute of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 create timestamp attribute is
       ** <code>createTimestamp</code>.
       */
      static final String ENTRY_CREATED_PREFIX                        = "entryCreatedPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the modifier
       ** name attribute of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 create timestamp attribute is
       ** <code>createTimestamp</code>.
       */
      static final String ENTRY_MODIFIER_PREFIX                       = "entryModifierPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the modified
       ** timestamp attribute of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 modify timestamp attribute is
       ** <code>modifyTimestamp</code>.
       */
      static final String ENTRY_MODIFIED_PREFIX                       = "entryModifiedPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the status
       ** attribute of an entry in a Directory Service.
       */
      static final String ENTRY_STATUS_PREFIX                         = "entryStatusPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the role
       ** object class of an entry in the Directory Service.
       */
      static final String ROLE_CLASS                                  = "roleClass";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the role
       ** object prefix of an entry in the Directory Service.
       */
      static final String ROLE_PREFIX                                 = "rolePrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the object
       ** role membership prefix of an entry in the Directory Service.
       */
      static final String ROLE_MEMBER_PREFIX                          = "roleMemberPrefix";
      
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the scope
       ** object class of an entry in the Directory Service.
       */
      static final String SCOPE_CLASS                                 = "scopeClass";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the scope
       ** object prefix of an entry in the Directory Service.
       */
      static final String SCOPE_PREFIX                                = "scopePrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the object
       ** scope membership prefix of an entry in the Directory Service.
       */
      static final String SCOPE_MEMBER_PREFIX                         = "scopeMemberPrefix";


      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the group
       ** object class of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard object class name of a LDAP V3 group is
       ** <code>groupOfUniqueNames</code>.
       */
      static final String GROUP_CLASS                                 = "groupClass";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the group
       ** object of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard prefix of a LDAP V3 group entry is <code>cn</code>.
       */
      static final String GROUP_PREFIX                                = "groupPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the object
       ** group membership of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard prefix of a LDAP V3 User group membership is
       ** <code>uniqueMember</code>.
       **
       ** @see #GROUP_MEMBER_PREFIX_DEFAULT
       */
      static final String GROUP_MEMBER_PREFIX                         = "groupMemberPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** object class of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard object class of a LDAP V3 account entry is
       ** <code>inetOrgPerson</code>.
       */
      static final String ACCOUNT_CLASS                               = "accountClass";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** object prefix of an entryin a V3 compliant Directory Service.
       ** <p>
       ** The standard prefix of a LDAP V3 account entry is <code>cn</code>.
       */
      static final String ACCOUNT_PREFIX                              = "accountPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** password attribute name of an entry in a V3 compliant Directory
       ** Service.
       ** <p>
       ** The standard prefix of a LDAP V3 account password attribute is
       ** <code>userPassword</code>.
       */
      static final String ACCOUNT_CREDENTIAL_PREFIX                   = "accountCredentialPrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify if the password value
       ** needs to be quoted for a Directory Service.
       */
      static final String ACCOUNT_CREDENTIAL_QUOTED                   = "accountCredentialQuoted";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the names of the
       ** multi-valued attributes of an account entry in the Directory Service.
       ** <p>
       ** If there has to be specified more than one attribute the list of
       ** these attributes has to be separated by the character defined by the
       ** property {@link #MULTIVALUE_SEPARATOR} of this configuration.
       */
      static final String ACCOUNT_MULTIVALUE                          = "accountMultiValue";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** role membership attribute of an entry in the Directory Service.
       */
      static final String ACCOUNT_ROLE_PREFIX                         = "accountRolePrefix";
      
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** scope membership attribute of an entry in the Directory Service.
       */
      static final String ACCOUNT_SCOPE_PREFIX                        = "accountScopePrefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** group membership attribute of an entry in a V3 compliant Directory
       ** Service.
       ** <p>
       ** The standard prefix of a LDAP V3 entry group membership is
       ** <code>uniqueMember</code>.
       */
      static final String ACCOUNT_GROUP_PREFIX                        = "accountGroupPrefix";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the sets of attributes
       ** that are used as status flags on directory entries.
       */
      static final String ENTRY_STATUS                                = "entryStatusAttributes";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface OIM
  // ~~~~~~~~~ ~~~
  interface OIM {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Resource
    // ~~~~~~~~~ ~~~~~~~~
    public interface Resource {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////
    }

    ////////////////////////////////////////////////////////////////////////////
    // interface Feature
    // ~~~~~~~~~ ~~~~~~~~
    interface Feature {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the character that
       ** separates multiple values for the same entry tag name.
       */
      static final String MULTIVALUE_SEPARATOR                        = "multi-value-separator";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the URL encoding.
       */
      static final String URL_ENCODING                                = "url-encoding";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the Initial Context factory.
       */
      static final String INITIAL_CONTEXT_FACTORY                     = "context-factory";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the Security Provider
       ** for encrypted sessions.
       */
      static final String SECURITY_PROVIDER                           = "security-provider";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the
       ** <code>Metadata Descriptor</code> that defines the servers that should
       ** be used if it will not be possible to establish a connection to the
       ** primary server defined in the <code>IT Resource</code>.
       ** <p>
       ** To configure failover systems, you need to specify the complete URL in
       ** the following format:
       ** ldap://SERVERADDRESS:PORT/ ldap://SERVERADDRESS1:PORT1/
       */
      static final String FAILOVER_CONFIGURATION                      = "failover";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify if referential integrity
       ** is enabled in target Directory Service.
       ** <p>
       ** Referential integrity is the process of maintaining consistent
       ** relationships among sets of data. If referential Integrity is enabled
       ** in the Directory Service, whenever an entry updated in the directory,
       ** the server also updates other entries that refer to that entry. For
       ** example, if an account entry is removed from the directory, and the
       ** account is a member of a group, the server also removes the account
       ** from the group. If referential integrity is not enabled, the account
       ** remains a member of the group until manually removed.
       ** <p>
       ** Referential integrity is not enabled by default.
       */
      static final String REFERENTIAL_INTEGRITY                       = "referential-integrity";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the format of a timestamp
       ** value in target Directory Service.
       */
      static final String TIMESTAMP_FORMAT                            = "timestamp-format";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the
       ** <code>SearchControl</code> that instruct the server to return the
       ** results of a search request in smaller, more manageable packets rather
       ** than in one large block.
       */
      static final String SIMPLEPAGE_CONTROL                          = "simplepage-control";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the
       ** <code>SearchControl</code> that instruct the server to return the
       ** results of a search request in smaller, more manageable packets rather
       ** than in one large block.
       */
      static final String VIRTUALLIST_CONTROL                         = "virtuallist-control";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy where a V3 compliant
       ** Directory Service will publish schema entries.
       */
      static final String SCHEMA_CONTAINER                            = "schema-container";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy where a V3 compliant
       ** Directory Service will publish catalog entries.
       */
      static final String CATALOG_CONTAINER                           = "catalog-container";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy where a V3 compliant
       ** Directory Service will publish changelog entries.
       */
      static final String CHANGELOG_CONTAINER                         = "changelog-container";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the attribute name of the
       ** change type that a V3 compliant Directory Service provides in the
       ** changelog.
       */
      static final String CHANGELOG_CHANGETYPE                        = "changelog-changeType";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the attribute name of the
       ** change number that a V3 compliant Directory Service provides in the
       ** changelog.
      */
      static final String CHANGELOG_CHANGENUMBER                      = "changelog-changeNumber";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the attribute name to
       ** access the global uid that a V3 compliant Directory Service provides
       ** in the changelog.
       */
      static final String CHANGELOG_TARGETGUID                        = "changelog-targetGUID";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the attribute name to
       ** access the distinguished name of a change subject that a V3 compliant
       ** Directory Service provides in the changelog.
       */
      static final String CHANGELOG_TARGETDN                          = "changelog-targetDN";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specifiy the name of the object
       ** class attribute of an entry in a V3 compliant Directory Service.
       */
      static final String OBJECT_CLASS_NAME                           = "object-class-name";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the object
       ** attributes that are binary attributes of an entry in the Directory
       ** Service.
       */
      static final String BINARY_OBJECT_ATTRIBUTE                     = "binary";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the names of the
       ** distinguished name attributes that may be contained any entry in a V3
       ** compliant Directory Service.
       */
      static final String DISTINGUISHED_NAME                          = "distinguished-name";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the
       ** distinguished name attribute of an entry in a V3 compliant
       ** Directory Service.
       ** <p>
       ** The standard prefix of a LDAP V3 entry distinguished name attribute is
       ** <code>DN</code>.
       */
      static final String DISTINGUISHED_NAME_PREFIX                   = "distinguished-name-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the
       ** server-assigned Universally Unique Identifier UUID for the entry in a
       ** V3 compliant Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 UUID is
       ** <code>entryUUID</code>.
       */
      static final String ENTRY_IDENTIFIER_PREFIX                     = "entry-identifier-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the creator
       ** name attribute of an entry in a Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 create timestamp attribute is
       ** <code>createTimestamp</code>.
       */
      static final String ENTRY_CREATOR_PREFIX                        = "entry-creator-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the create
       ** timestamp attribute of an entry in a Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 create timestamp attribute is
       ** <code>createTimestamp</code>.
       */
      static final String ENTRY_CREATED_PREFIX                        = "entry-created-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the modifier
       ** name attribute of an entry in a Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 modify timestamp attribute is
       ** <code>modifyTimestamp</code>.
       */
      static final String ENTRY_MODIFIER_PREFIX                       = "entry-modifier-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the modified
       ** timestamp attribute of an entry in a Directory Service.
       ** <p>
       ** The standard attribute name of a LDAP V3 modify timestamp attribute is
       ** <code>modifyTimestamp</code>.
       */
      static final String ENTRY_MODIFIED_PREFIX                       = "entry-modified-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the status
       ** attribute of an entry in a Directory Service.
       */
      static final String ENTRY_STATUS_PREFIX                         = "entry-status-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the role
       ** object class of an entry in the Directory Service.
       */
      static final String ROLE_CLASS                                  = "role-class";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the role
       ** object prefix of an entry in the Directory Service.
       */
      static final String ROLE_PREFIX                                 = "role-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the object
       ** role membership of an entry in the Directory Service.
       */
      static final String ROLE_MEMBER_PREFIX                          = "role-member-prefix";
      
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the scope
       ** object class of an entry in the Directory Service.
       */
      static final String SCOPE_CLASS                                 = "scope-class";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the scope
       ** object prefix of an entry in the Directory Service.
       */
      static final String SCOPE_PREFIX                                = "scope-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the object
       ** scope membership of an entry in the Directory Service.
       */
      static final String SCOPE_MEMBER_PREFIX                         = "scope-member-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the group
       ** object class of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard object class name of a LDAP V3 group is
       ** <code>groupOfUniqueNames</code>.
       */
      static final String GROUP_CLASS                                 = "group-class";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the group
       ** object of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard prefix of a LDAP V3 group entry is <code>cn</code>.
       */
      static final String GROUP_PREFIX                                = "group-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the object
       ** group membership of an entry in a V3 compliant Directory
       ** Service.
       ** <p>
       ** The standard prefix of a LDAP V3 User group membership is
       ** <code>uniqueMember</code>.
       **
       ** @see #GROUP_MEMBER_PREFIX_DEFAULT
       */
      static final String GROUP_MEMBER_PREFIX                         = "group-member-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** object class of an entry in a V3 compliant Directory Service.
       ** <p>
       ** The standard object class of a LDAP V3 account entry is
       ** <code>inetOrgPerson</code>.
       */
      static final String ACCOUNT_CLASS                               = "account-class";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** object prefix of an entryin a V3 compliant Directory Service.
       ** <p>
       ** The standard prefix of a LDAP V3 account entry is <code>cn</code>.
       */
      static final String ACCOUNT_PREFIX                              = "account-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the account
       ** password attribute name of an entry in a V3 compliant Directory
       ** Service.
       ** <p>
       ** The standard prefix of a LDAP V3 account password attribute is
       ** <code>userPassword</code>.
       */
      static final String ACCOUNT_CREDENTIAL_PREFIX                   = "account-credential-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify if the password value
       ** needs to be quoted for a Directory Service.
       */
      static final String ACCOUNT_CREDENTIAL_QUOTED                   = "account-credential-quoted";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the names of the
       ** multi-valued attributes of an account entry in the Directory Service.
       ** <p>
       ** If there has to be specified more than one attribute the list of
       ** these attributes has to be separated by the character defined by the
       ** property {@link #MULTIVALUE_SEPARATOR} of this configuration.
       */
      static final String ACCOUNT_MULTIVALUE                          = "account-multi-value";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the account
       ** role membership attribute of an entry in the Directory Service.
       */
      static final String ACCOUNT_ROLE_PREFIX                         = "account-role-prefix";
      
       /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the account
       ** role membership attribute of an entry in the Directory Service.
       */
      static final String ACCOUNT_SCOPE_PREFIX                        = "account-scope-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the account
       ** group membership attribute of an entry in a V3 compliant Directory
       ** Service.
       ** <p>
       ** The standard prefix of a LDAP V3 entry group membership is
       ** <code>uniqueMember</code>.
       */
      public static final String ACCOUNT_GROUP_PREFIX                 = "account-group-prefix";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify name of the container
       ** where Oracle specific entries are located in the Directory Service.
       ** <p>
       ** The standard container in a security realm is
       ** <code>cn=OracleContext</code>.
       */
      static final String ORACLE_CONTEXT_CONTAINER                    = "context-container";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify name of the container
       ** where product registrations are located in the Directory Service.
       ** <p>
       ** The standard container in a security realm is
       ** <code>cn=Products,cn=OracleContext</code>.
       */
      static final String ORACLE_PRODUCT_CONTAINER                    = "product-container";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify name of the container
       ** where database domains are located in the Directory Service.
       ** <p>
       ** The standard container in a security realm is
       ** <code>cn=OracleDBSecurity,cn=Products,cn=OracleContext</code>.
       */
      static final String ENTERPRISE_DOMAIN_CONTAINER                 = "domain-container";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the enterprise
       ** database object class of an entry in the Directory Service.
       ** <p>
       ** The standard object class in a security realm is
       ** <code>orclDBServer</code>.
       */
      static final String ENTERPRISE_DATABASE_CLASS                   = "enterprise-database-class";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the
       ** enterprise database object prefix of an entry in the Directory Service.
       ** <p>
       ** The standard object class in a security realm is <code>cn</code>.
       */
      static final String ENTERPRISE_DATABASE_PREFIX                  = "enterprise-database-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the enterprise
       ** domain object class of an entry in the Directory Service.
       ** <p>
       ** The standard object class in a security realm is
       ** <code>orclDBEnterpriseDomain</code>.
       */
      static final String ENTERPRISE_DOMAIN_CLASS                     = "enterprise-domain-class";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the
       ** enterprise domain object prefix of an entry in the Directory Service.
       ** <p>
       ** The standard object class in a security realm is <code>cn</code>.
       */
      static final String ENTERPRISE_DOMAIN_PREFIX                    = "enterprise-domain-prefix";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the schema
       ** mapping object class of an entry in the Directory Service.
       ** <p>
       ** The standard object class in a security realm is
       ** <code>orclDBEntryLevelMapping</code>.
       */
      static final String ENTERPRISE_SCHEMA_CLASS                     = "enterprise-schema-class";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify the prefix of the schema
       ** mapping object prefix of an entry in the Directory Service.
       ** <p>
       ** The standard object class in a security realm is <code>cn</code>.
       */
      static final String ENTERPRISE_SCHEMA_PREFIX                    = "enterprise-schema-prefix";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the schema
       ** mapping object prefix of an entry in the Directory Service.
       ** <p>
       ** The standard account DN maping in a security realm is
       ** <code>orclDBDistinguishedName</code>.
       */
      static final String ENTERPRISE_SCHEMA_ACCOUNT                   = "enterprise-schema-account";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the enterprise
       ** role object class of an entry in the Directory Service.
       ** <p>
       ** The standard object class in a security realm is
       ** <code>orclDBEnterpriseRole</code>.
       */
      static final String ENTERPRISE_ROLE_CLASS                        = "enterprise-role-class";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the
       ** enterprise role object prefix of an entry in the Directory Service.
       ** <p>
       ** The standard object class in a security realm is <code>cn</code>.
       */
      static final String ENTERPRISE_ROLE_PREFIX                       = "enterprise-role-prefix";

      /**
       ** Attribute tag which might be defined on a
       ** <code>Metadata Descriptor</code> to specify the name of the object
       ** group membership prefix of an entry in a Enterprise Role.
       ** <p>
       ** The standard prefix of a Enterprise Role membership is
       ** <code>uniqueMember</code>.
       **
       ** @see #ENTERPRISE_ROLE_MEMBER_PREFIX_DEFAULT
       */
      static final String ENTERPRISE_ROLE_MEMBER_PREFIX               = "enterprise-role-member-prefix";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify if the Directory Service
       ** requires secure sockets on password operations.
       */
     static final String PASSWORD_OPERATION_SECURED                  = "password-operation-secured";

      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify if the entitlement loaded
       ** from a Directory Server needs to be prefixed with the internal system
       ** identifier and/or the name of the <code>IT Resource</code>.
       */
      static final String ENTITLEMENT_PREFIX_REQUIRED                = "entitlement-prefix-required";

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the sets of attributes
       ** that are used as status flags on directory entries.
       */
      static final String ENTRY_STATUS                               = "entry-status";

      /**
       ** Attribute tag which may be defined on a
       ** <code>entry-status</code> to specify the details a status attribute.
       */
      public static final String STATUS_ATTRIBUTE                   = "status-attribute";
    }
  }

  static final Set<String> PROPERTY = CollectionUtility.unmodifiableSet(
    OIM.Feature.MULTIVALUE_SEPARATOR
  , OIM.Feature.INITIAL_CONTEXT_FACTORY
  , OIM.Feature.SECURITY_PROVIDER
  , OIM.Feature.REFERENTIAL_INTEGRITY
  , OIM.Feature.TIMESTAMP_FORMAT
  , OIM.Feature.OBJECT_CLASS_NAME
  , OIM.Feature.DISTINGUISHED_NAME_PREFIX

  , OIM.Feature.PASSWORD_OPERATION_SECURED
  , OIM.Feature.ENTITLEMENT_PREFIX_REQUIRED

  , OIM.Feature.SCHEMA_CONTAINER
  , OIM.Feature.ENTRY_STATUS_PREFIX
  , OIM.Feature.CATALOG_CONTAINER
  , OIM.Feature.CHANGELOG_CONTAINER
  , OIM.Feature.CHANGELOG_CHANGETYPE
  , OIM.Feature.CHANGELOG_CHANGENUMBER
  , OIM.Feature.CHANGELOG_TARGETGUID
  , OIM.Feature.CHANGELOG_TARGETDN
  , OIM.Feature.CHANGELOG_TARGETDN

  , OIM.Feature.ENTRY_CREATED_PREFIX
  , OIM.Feature.ENTRY_MODIFIED_PREFIX
  , OIM.Feature.ENTRY_IDENTIFIER_PREFIX
  , OIM.Feature.ENTITLEMENT_PREFIX_REQUIRED
  );

  static final Map<String, String> FEATURE =
    new HashMap<String, String>() { {
      put(ICF.Feature.MULTIVALUE_SEPARATOR,      OIM.Feature.MULTIVALUE_SEPARATOR);
      put(ICF.Feature.URL_ENCODING,              OIM.Feature.URL_ENCODING);
      put(ICF.Feature.INITIAL_CONTEXT_FACTORY,   OIM.Feature.INITIAL_CONTEXT_FACTORY);
      put(ICF.Feature.SECURITY_PROVIDER,         OIM.Feature.SECURITY_PROVIDER);
      put(ICF.Feature.FAILOVER_CONFIGURATION,    OIM.Feature.FAILOVER_CONFIGURATION);

      put(ICF.Feature.REFERENTIAL_INTEGRITY,     OIM.Feature.REFERENTIAL_INTEGRITY);
      put(ICF.Feature.TIMESTAMP_FORMAT,          OIM.Feature.TIMESTAMP_FORMAT);
      put(ICF.Feature.SIMPLEPAGE_CONTROL,        OIM.Feature.SIMPLEPAGE_CONTROL);
      put(ICF.Feature.VIRTUALLIST_CONTROL,       OIM.Feature.VIRTUALLIST_CONTROL);
      put(ICF.Feature.SCHEMA_CONTAINER,          OIM.Feature.SCHEMA_CONTAINER);
      put(ICF.Feature.CATALOG_CONTAINER,         OIM.Feature.CATALOG_CONTAINER);
      put(ICF.Feature.CHANGELOG_CONTAINER,       OIM.Feature.CHANGELOG_CONTAINER);
      put(ICF.Feature.CHANGELOG_CHANGETYPE,      OIM.Feature.CHANGELOG_CHANGETYPE);
      put(ICF.Feature.CHANGELOG_CHANGENUMBER,    OIM.Feature.CHANGELOG_CHANGENUMBER);
      put(ICF.Feature.CHANGELOG_TARGETGUID,      OIM.Feature.CHANGELOG_TARGETGUID);
      put(ICF.Feature.CHANGELOG_TARGETDN,        OIM.Feature.CHANGELOG_TARGETDN);
      put(ICF.Feature.OBJECT_CLASS_NAME,         OIM.Feature.OBJECT_CLASS_NAME);
      put(ICF.Feature.DISTINGUISHED_NAME,        OIM.Feature.DISTINGUISHED_NAME);
      put(ICF.Feature.BINARY_OBJECT_ATTRIBUTE,   OIM.Feature.BINARY_OBJECT_ATTRIBUTE);
      put(ICF.Feature.DISTINGUISHED_NAME_PREFIX, OIM.Feature.DISTINGUISHED_NAME_PREFIX);
      put(ICF.Feature.ENTRY_IDENTIFIER_PREFIX,   OIM.Feature.ENTRY_IDENTIFIER_PREFIX);
      put(ICF.Feature.ENTRY_CREATOR_PREFIX,      OIM.Feature.ENTRY_CREATOR_PREFIX);
      put(ICF.Feature.ENTRY_CREATED_PREFIX,      OIM.Feature.ENTRY_CREATED_PREFIX);
      put(ICF.Feature.ENTRY_MODIFIER_PREFIX,     OIM.Feature.ENTRY_MODIFIER_PREFIX);
      put(ICF.Feature.ENTRY_MODIFIED_PREFIX,     OIM.Feature.ENTRY_MODIFIED_PREFIX);
      put(ICF.Feature.ENTRY_STATUS_PREFIX,       OIM.Feature.ENTRY_STATUS_PREFIX);

      put(ICF.Feature.ROLE_CLASS,                OIM.Feature.ROLE_CLASS);
      put(ICF.Feature.ROLE_PREFIX,               OIM.Feature.ROLE_PREFIX);
      put(ICF.Feature.ROLE_MEMBER_PREFIX,        OIM.Feature.ROLE_MEMBER_PREFIX);
      
      put(ICF.Feature.SCOPE_CLASS,               OIM.Feature.SCOPE_CLASS);
      put(ICF.Feature.SCOPE_PREFIX,              OIM.Feature.SCOPE_PREFIX);
      put(ICF.Feature.SCOPE_MEMBER_PREFIX,       OIM.Feature.SCOPE_MEMBER_PREFIX);

      put(ICF.Feature.GROUP_CLASS,               OIM.Feature.GROUP_CLASS);
      put(ICF.Feature.GROUP_PREFIX,              OIM.Feature.GROUP_PREFIX);
      put(ICF.Feature.GROUP_MEMBER_PREFIX,       OIM.Feature.GROUP_MEMBER_PREFIX);

      put(ICF.Feature.ACCOUNT_CLASS,             OIM.Feature.ACCOUNT_CLASS);
      put(ICF.Feature.ACCOUNT_PREFIX,            OIM.Feature.ACCOUNT_PREFIX);
      put(ICF.Feature.ACCOUNT_CREDENTIAL_PREFIX, OIM.Feature.ACCOUNT_CREDENTIAL_PREFIX);
      put(ICF.Feature.ACCOUNT_CREDENTIAL_QUOTED, OIM.Feature.ACCOUNT_CREDENTIAL_QUOTED);
      put(ICF.Feature.ACCOUNT_MULTIVALUE,        OIM.Feature.ACCOUNT_MULTIVALUE);
      put(ICF.Feature.ACCOUNT_ROLE_PREFIX,       OIM.Feature.ACCOUNT_ROLE_PREFIX);
      put(ICF.Feature.ACCOUNT_SCOPE_PREFIX,      OIM.Feature.ACCOUNT_SCOPE_PREFIX);
      put(ICF.Feature.ACCOUNT_GROUP_PREFIX,      OIM.Feature.ACCOUNT_GROUP_PREFIX);

      put(ICF.Feature.ENTRY_STATUS,              OIM.Feature.ENTRY_STATUS);
    }
  };

  static final Map<String, String> RESOURCE =
    new HashMap<String, String>() { {
      put(ICF.Resource.PRIMARY_HOST,              DirectoryResource.SERVER_NAME);
      put(ICF.Resource.PRIMARY_PORT,              DirectoryResource.SERVER_PORT);
      put(ICF.Resource.ROOT_CONTEXT,              DirectoryResource.ROOT_CONTEXT);
      put(ICF.Resource.PRINCIPAL_NAME,            DirectoryResource.PRINCIPAL_NAME);
      put(ICF.Resource.PRINCIPAL_PASSWORD,        DirectoryResource.PRINCIPAL_PASSWORD);
      put(ICF.Resource.SECURE_SOCKET,             DirectoryResource.SECURE_SOCKET);
      put(ICF.Resource.LOCALE_LANGUAGE,           DirectoryResource.LOCALE_LANGUAGE);
      put(ICF.Resource.LOCALE_COUNTRY,            DirectoryResource.LOCALE_COUNTRY);
      put(ICF.Resource.LOCALE_TIMEZONE,           DirectoryResource.LOCALE_TIMEZONE);
      put(ICF.Resource.RESPONSE_TIMEOUT,          DirectoryResource.RESPONSE_TIMEOUT);
      put(ICF.Resource.CONNECTION_TIMEOUT,        DirectoryResource.CONNECTION_TIMEOUT);
      put(ICF.Resource.CONNECTION_RETRY_COUNT,    DirectoryResource.CONNECTION_RETRY_COUNT);
      put(ICF.Resource.CONNECTION_RETRY_INTERVAL, DirectoryResource.CONNECTION_RETRY_INTERVAL);
    }
  };
}