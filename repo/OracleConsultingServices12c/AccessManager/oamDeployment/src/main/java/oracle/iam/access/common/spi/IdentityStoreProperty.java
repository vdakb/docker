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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   IdentityStoreProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    IdentityStoreProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import oracle.iam.access.common.FeatureProperty;

////////////////////////////////////////////////////////////////////////////////
// enum IdentityStoreProperty
// ~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>IdentityStoreProperty</code> defines sepcific parameter type
 ** declarations regarding <code>Identity Store</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum IdentityStoreProperty implements FeatureProperty {

  /**
   ** An entry form the list of all supported LDAP providers from which you
   ** can choose. You can have multiple <code>Identity Store</code>s, as but its
   ** need to be one of
   ** <ul>
   **   <li>AD: Microsoft Active Directory
   **   <li>eDirectory: Novell eDirectory
   **   <li>IBM: OBM Tivoli Directory
   **   <li>ODSEE: Oracle Directory Server Enterprise Edition
   **   <li>OID: Oracle Internet Directory
   **   <li>OUD: Oracle Unified Directory
   **   <li>OVD: Oracle Virtual Directory
   **   <li>SJS: sun Java System Directory
   **   <li>SLAPD: OpenLDAP Directory
   ** </ul>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  TYPE("serviceType", Type.STRING, true, null),

  /**
   ** The ldap type of the LDAP store to be created or modified. You can have
   ** multiple <code>Identity Store</code>s, as but its need to be one of
   ** <ul>
   **   <li>AD: Microsoft Active Directory
   **   <li>eDirectory: Novell eDirectory
   **   <li>IBM: OBM Tivoli Directory
   **   <li>ODSEE: Oracle Directory Server Enterprise Edition
   **   <li>OID: Oracle Internet Directory
   **   <li>OUD: Oracle Unified Directory
   **   <li>OVD: Oracle Virtual Directory
   **   <li>SJS: sun Java System Directory
   **   <li>SLAPD: OpenLDAP Directory
   ** </ul>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  PROVIDER("serviceProvider", Type.STRING, true, null),

  /**
   ** The URL for the LDAP host, including the port number.
   ** Oracle Access Management 11g support multiple LDAP URLs with failover
   ** capability. The Identity Assertion Provider fails over to the next LDAP
   ** URL based on the order in which these appear.
   ** <br>
   ** Enter one (or more) LDAP URLs in <code>host:port</code> format, multiple
   ** URLs must be separated by a space or new line. There is no need to
   ** specify <code>ldap://</code> or <code>ldaps://</code> (which supports
   ** SSL_NO_AUTH) in the URL value.
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  URL("serviceURL", Type.STRING, true, null),

  /**
   ** The password of the principal for the LDAP <code>Identity Store</code> to
   ** be created or modified.
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  CREDENTIAL("serviceCredential", Type.STRING, true, null),

  /**
   ** The user DN for the connection pool over which all other BINDs occur.
   ** <p>
   ** Oracle recommends a non administrative user with appropriate Read and
   ** Search privileges for the user and group base DNs.
   ** <br>
   ** e.g.: <code>uid=amldapuser,ou=people,o=org</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  PRINCIPAL("servicePrincipal", Type.STRING, true, null),

  /**
   ** The Boolean field of the LDAP ID store to be created of the LDAP
   ** <code>Identity Store</code> to be created or modified.
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  PRIMARY("primary", Type.BOOLEAN, true, "false"),

  /**
   ** Indicating whether it is a system <code>Identity Store</code> of the LDAP
   ** ID store to be created or modified.
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  SYSTEM("system", Type.BOOLEAN, true, "false"),

  NATIVE("native", Type.BOOLEAN, false, "false"),

  /**
   ** A list of comma-separated user attributes; for example, email, phone,
   ** mobile.
   ** <p>
   ** The OAM server will cache the list of user attributes in memory while it
   ** authenticates the user against the identity store. The cached values will
   ** be used to compute the Authentication response headers, Authorization
   ** policy response headers and Authorization policy conditions. Pre-fetched
   ** attributes provide huge performance improvements by avoiding a round trip
   ** to the user <code>Identity Store</code>.
   ** <p>
   ** The OAM Administrator has to make sure all the user attributes used in
   ** Authentication and Authorization policy response headers and Authorization
   ** conditions are defined as prefetched attributes in the user
   ** <code>Identity Store</code> profile.
   */
  PREFETCHED_ATTRIBUTES("prefetchedAttributes",   Type.STRING, false, null),

  ROLE_APPLICATION_ADMIN("roleApplicationAdmin",  Type.STRING, false, null),
  ROLE_SECURITY_ADMIN("roleSecurityAdmin",        Type.STRING, false, null),
  ROLE_SECURITY_GROUPS("roleSecurityAdminGroups", Type.STRING, false, null),
  ROLE_SECURITY_USERS("roleSecurityAdminUsers",   Type.STRING, false, null),
  ROLE_SYSTEM_MANAGER("roleSystemManager",        Type.STRING, false, null),
  ROLE_SYSTEM_MONITOR("roleSystemMonitor",        Type.STRING, false, null),

  /**
   ** Boolean value for group cache: <code>true</code> or <code>false</code>.
   ** <br>
   ** Default: <code>true</code>
   ** <br>
   ** <b>Note</b>: To make things simple we chnage the default to
   **              <code>false</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  GROUP_CACHE_ENABLED("groupCacheEnabled", Type.BOOLEAN, true, "false"),

  /**
   ** Integer for the group cache size.
   ** <br>
   ** Default: <code>10000</code>
   */
  GROUP_CACHE_SIZE("groupCacheSize", Type.INTEGER, false, "1000"),

  /**
   ** Integer (in seconds) for Time To Live (TTL) for group cache elements.
   ** <br>
   ** Default: <code>0</code>
   */
  GROUP_CACHE_TTL("groupCacheTTL", Type.INTEGER, false, "0"),

  /**
   ** The attribute that identifies the group name.
   ** <br>
   ** e.g.: <code>cn</code>
   ** <br>
   ** Default: <code>cn</code>
   */
  GROUP_NAME_ATTRIBUTE("groupNameAttribute", Type.STRING, false, "cn"),

  /**
   ** The object classes to be included in the search results for groups, in a
   ** comma-separated list of group object classes.
   ** <br>
   ** e.g.: <code>groups,groupOfNames</code>
   ** <br>
   ** Default: <code>groupOfUniqueNames</code>
   */
  GROUP_OBJECT_CLASS("groupObjectClass", Type.STRING, false, "groupOfUniqueNames"),

  /**
   ** The node in the directory information tree (DIT) under which group data is
   ** stored, and the highest possible base for all group data searches.
   ** <p>
   ** Currently only static groups are supported, with the
   ** <code>uniquemember</code> attribute.
   ** <br>
   ** e.g.: <code>ou=groups,ou=myrealm,dc=base_domain</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  GROUP_SEARCH_BASE("groupSearchBase", Type.STRING, true, null),

  /**
   ** The attribute that identifies the login ID (user name).
   ** <br>
   ** e.g.: <code>uid</code>
   ** <br>
   ** Default: <code>uid</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  USER_NAME_ATTRIBUTE("userNameAttribute", Type.STRING, true, "uid"),

  /**
   ** The attribute in the user <code>Identity Store</code> (LDAP directory)
   ** which stores the user's password. This is made configurable for added
   ** flexibility.
   ** <br>
   ** e.g.: <code>userPassword</code>
   */
  USER_PASSWORD_ATTRIBUTE("userPasswordAttribute", Type.STRING, false, null),

  /**
   ** This enables getting the authentication code for natively
   ** locked/disabled/pw_must_change code in the LDAP authentication module.
   ** <br>
   ** e.g.: <code>false</code>
   */
  USER_NATIVE_PROVIDER("userNativeProvider", Type.STRING, false, null),

  /**
   ** The node in the directory information tree (DIT) under which user data is
   ** stored, and the highest possible base for all user data searches.
   ** <br>
   ** e.g.: <code>ou=people,ou=myrealm,dc=base_domain</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  USER_SEARCH_BASE("userSearchBase", Type.STRING, true, null),

  /**
   ** The object classes to be included in search results for users, in a
   ** comma-separated list of user object class names.
   ** <br>
   ** e.g.: <code>user,person</code>
   ** <br>
   ** Default: <code>person</code>
   */
  USER_OBJECT_CLASS("userObjectClass", Type.STRING, false, "person"),

  /**
   ** Referrals allow a directory tree to be partitioned and distributed between
   ** multiple LDAP servers, which means that LDAP servers may not store the
   ** entire DIT while still being capable of containing references to other
   ** LDAP servers that offer requested information instead.
   ** <p>
   ** One of these values:
   ** <ul>
   **   <li>follow: Follows referrals during an LDAP search (Default)
   **   <li>ignore: Ignores referral entries during an LDAP search
   **   <li>throw: Results in a Referral Exception, which can be caught by the
   **              component user.
   ** </ul>
   */
  REFERRAL_POLICY("referralPolicy", Type.STRING, false, null),

  /**
   ** Retrieves the maximum length of time in milliseconds that an operation
   ** should be allowed to block while waiting for a response from the server.
   ** <br>
   ** Default: 120000
   */
  SEARCHTIME_LIMIT("searchTimeLimit", Type.INTEGER, false, "120000"),

  /**
   ** Enables password policy enforcement against the attribute values listed
   ** below.
   ** <ul>
   **   <li>{@link #IDSTORE_CHALLENGE_ANSWERS}
   **   <li>{@link #IDSTORE_CHALLENGE_QUESTIONS}
   **   <li>{@link #IDSTORE_EMAIL_ATTRIBUTE}
   **   <li>{@link #IDSTORE_FIRSTNAME_ATTRIBUTE}
   **   <li>{@link #IDSTORE_LASTNAME_ATTRIBUTE}
   **   <li>{@link #IDSTORE_USERID_ATTRIBUTE}
   ** </ul>
   ** The corresponding options in the password policy must be configured as
   ** well.
   ** <br>
   ** e.g.: <code>false</code>
   ** <br>
   ** Default: <code>false</code>
   */
  IDSTORE_PASSWORD_POLICY("enablePasswordPolicy", Type.BOOLEAN, true, "false"),

  /**
   ** Not currently supported.
   **
   ** @see #IDSTORE_PASSWORD_POLICY
   */
  IDSTORE_CHALLENGE_QUESTIONS("challengeQuestionsAttribute", Type.STRING, false, null),

  /**
   ** Not currently supported.
   **
   ** @see #IDSTORE_PASSWORD_POLICY
   */
  IDSTORE_CHALLENGE_ANSWERS("challengeAnswersAttribute", Type.STRING, false, null),

  /**
   ** Specifies the E-Mail attribute name.
   ** <br>
   ** This attribute will be used as part of the password policy to check that
   ** the user's E-Mail address is not part of the password.
   ** <br>
   ** Not currently supported.
   **
   ** @see #IDSTORE_PASSWORD_POLICY
   */
  IDSTORE_EMAIL_ATTRIBUTE("idstoreEmailAttribute", Type.STRING, false, null),

  /**
   ** Specifies the First Name attribute name.
   ** <br>
   ** This attribute will be used as part of the password policy to check that
   ** the user's first name is not part of the password.
   **
   ** @see #IDSTORE_PASSWORD_POLICY
   */
  IDSTORE_FIRSTNAME_ATTRIBUTE("idstoreFirstNameAttribute", Type.STRING, false, null),

  /**
   ** Specifies the Last Name attribute name.
   ** <br>
   ** This attribute will be used as part of the password policy to check that
   ** the user's last name is not part of the password.
   **
   ** @see #IDSTORE_PASSWORD_POLICY
   */
  IDSTORE_LASTNAME_ATTRIBUTE("idstoreLastNameAttribute", Type.STRING, false, null),

  /**
   ** Specifies the User ID attribute name.
   ** <br>
   ** This attribute will be used as part of the password policy to check that
   ** the user ID is not part of the password.
   **
   ** @see #IDSTORE_PASSWORD_POLICY
   */
  IDSTORE_USERID_ATTRIBUTE("idstoreUserIDAttribute", Type.STRING, false, null),

  /**
   ** Enables the use of OBLIX schema instead of standard Oracle schema.
   **
   ** @see #IDSTORE_PASSWORD_POLICY
   */
  IDSTORE_PASSWORD_SCHEMA("idstoreOblixUserSchema", Type.BOOLEAN, false, "false"),

  /**
   ** The smallest size set for the connection pool.
   ** <br>
   ** Default: 10
   */
  CONNECTION_POOL_SIZEMIN("connectionPoolSizeMin", Type.INTEGER, false, "10"),

  /**
   ** The greatest size set for the connection pool.
   ** <br>
   ** Default: 50
   */
  CONNECTION_POOL_SIZEMAX("connectionPoolSizeMax", Type.INTEGER, false, "50"),

  /**
   ** The number (in seconds) that connection requests can wait before timing
   ** out in the event of a fully utilized pool.
   ** <br>
   ** Default: 120
   */
  CONNECTION_WAIT_TIMEOUT("connectionWaitTimeout", Type.INTEGER, false, "120"),

  /**
   ** The number of consecutive attempts to be made at establishing a connection
   ** with the LDAP server.
   ** <br>
   ** Default: 3
   */
  CONNECTION_RETRY_COUNT("connectionRetryCount", Type.INTEGER, false, "3")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** operation executed to determine existance of an
   ** <code>Identity Store</code>
   */
  static final String   REPORT           = "displayLDAP";

  /** operation executed to create an <code>Identity Store</code> */
  static final String   CREATE           = "addLDAP";

  /** operation executed to modify an <code>Identity Store</code> */
  static final String   MODIFY           = "editLDAP";

  /** operation executed to delete an <code>Identity Store</code> */
  static final String   DELETE           = "deleteLDAP";

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to report an <code>Identity Store</code> configuration
   */
  static final String[] SIGNATURE_REPORT = {
    String.class.getName() //  0: name
  };

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to create an <code>Identity Store</code> configuration
   */
  static final String[] SIGNATURE_CREATE = {
    String.class.getName() //  0: name
  , String.class.getName() //  1: servicePrincipal
  , String.class.getName() //  2: serviceCredential
  , String.class.getName() //  3: serviceType
  , String.class.getName() //  4: userAttribute
  , String.class.getName() //  5: serviceProvider
  , String.class.getName() //  6: roleSecAdmin
  , String.class.getName() //  7: roleSecAdminUsers
  , String.class.getName() //  8: roleSecAdminGroups
  , String.class.getName() //  9: roleSysMonitor
  , String.class.getName() // 10: roleAppAdmin
  , String.class.getName() // 11: roleSysManager
  , String.class.getName() // 12: searchBase
  , String.class.getName() // 13: ldapURL
  , String.class.getName() // 14: primary
  , String.class.getName() // 15: system
  , String.class.getName() // 16: userIDProvider
  , String.class.getName() // 17: groupSearchBase
  , String.class.getName() // 18: supplementaryReturnAttributes
  };

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to modify an <code>Identity Store</code> configuration
   */
  static final String[] SIGNATURE_MODIFY = {
    String.class.getName() //  0: name
  , String.class.getName() //  1: servicePrincipal
  , String.class.getName() //  2: serviceCredential
  , String.class.getName() //  3: serviceType
  , String.class.getName() //  4: userAttribute
  , String.class.getName() //  5: serviceProvider
  , String.class.getName() //  6: roleSecAdmin
  , String.class.getName() //  7: roleSecAdminUsers
  , String.class.getName() //  8: roleSecAdminGroups
  , String.class.getName() //  9: roleSysMonitor
  , String.class.getName() // 10: roleAppAdmin
  , String.class.getName() // 11: roleSysManager
  , String.class.getName() // 12: searchBase
  , String.class.getName() // 13: ldapURL
  , String.class.getName() // 14: primary
  , String.class.getName() // 15: system
  , String.class.getName() // 16: userIDProvider
  , String.class.getName() // 17: groupSearchBase
  , String.class.getName() // 18: userObjectClasses
  , String.class.getName() // 19: groupObjectClasses
  , String.class.getName() // 20: referralPolicy
  , String.class.getName() // 21: searchTimeLimit
  , String.class.getName() // 22: minConnections
  , String.class.getName() // 23: maxConnections
  , String.class.getName() // 24: connectionWaitTimeout
  , String.class.getName() // 25: connectionRetryCount
  , String.class.getName() // 26: groupNameAttribute
  , String.class.getName() // 27: groupCacheEnabled
  , String.class.getName() // 28: groupCacheSize
  , String.class.getName() // 29: groupCacheTTL
  , String.class.getName() // 30: enablePasswordPolicy
  , String.class.getName() // 31: supplementaryReturnAttributes
  , String.class.getName() // 32: idStorePwdSchema
  , String.class.getName() // 33: idStoreFirstName
  , String.class.getName() // 34: idStoreGlobalUserId
  , String.class.getName() // 35: idStoreChallengeQuestions
  , String.class.getName() // 36: idStoreChallengeAnswers
  , String.class.getName() // 37: idStoreLastName
  , String.class.getName() // 38: idStoreEmailId
  , String.class.getName() // 39: isNative
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6844418972851037916")
  private static final long serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String   id;
  final Type     type;
  final boolean  required;
  final String   defaultValue;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum ServiceProvider
  // ~~~~ ~~~~~~~~~~~~~~~
  /**
   ** Enum to verify the provided value for IdentityStoreProperty#PROVIDER.
   ** <p>
   ** This Enum is not really used by the implementation. It's for validation
   ** purpose only.
   */
  enum ServiceProvider {
      ADS("AD")
    , NDS("eDirectory")
    , IDS("IBM")
    , ODS("ODSEE")
    , OID("OID")
    , OUD("OUD")
    , OVD("OVD")
    , SDS("SJS")
    , OLD("SLAPD")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:775042238356069292")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String encoded;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ServiceProvider</code>
     **
     ** @param  encoded          the encoded string value.
     */
    ServiceProvider(final String encoded) {
      this.encoded = encoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: encoded
    /**
     ** Returns the value of the encoded property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String encoded() {
      return this.encoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper service provider from the given string
     ** value.
     **
     ** @param  value              the string value the service provider should
     **                            be returned for.
     **
     ** @return                    the service provider.
     */
    public static ServiceProvider from(final String value) {
      for (ServiceProvider cursor : ServiceProvider.values()) {
        if (cursor.encoded.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum ReferralPolicy
  // ~~~~ ~~~~~~~~~~~~~~
  /**
   ** Enum to verify the provided value for
   ** IdentityStoreProperty#REFERRAL_POLICY.
   ** <p>
   ** This Enum is not really used by the implementation. It's for validation
   ** purpose only.
   */
  enum ReferralPolicy {
      FOLLOW("follow")
    , IGNORE("ignore")
    , THROW("throw")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:8298504619387096336")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String policy;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ReferralPolicy</code>
     **
     ** @param  policy          the policy string value.
     */
    ReferralPolicy(final String policy) {
      this.policy = policy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: policy
    /**
     ** Returns the value of the policy property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String policy() {
      return this.policy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper referral policy from the given string
     ** value.
     **
     ** @param  value              the string value the referral policy should
     **                            be returned for.
     **
     ** @return                    the referral policy.
     */
    public static ReferralPolicy from(final String value) {
      for (ReferralPolicy cursor : ReferralPolicy.values()) {
        if (cursor.policy.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityStoreProperty</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  IdentityStoreProperty(final String id, final Type type, final boolean required, final String defaultValue) {
    this.id           = id;
    this.type         = type;
    this.required     = required;
    this.defaultValue = defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (ServiceAction)
  /**
   ** Returns the id of the property.
   **
   ** @return                    the id of the property.
   */
  @Override
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type (FeatureProperty)
  /**
   ** Returns the type of the property.
   **
   ** @return                    the type of the property.
   */
  @Override
  public Type type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required (FeatureProperty)
  /**
   ** Returns <code>true</code> if the property is mandatory.
   **
   ** @return                    <code>true</code> if the property is mandatory;
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean required() {
    return this.required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultValue (FeatureProperty)
  /**
   ** Returns the defaultValue of the property if any.
   **
   ** @return                    the defaultValue of the property if any.
   */
  @Override
  public String defaultValue() {
    return this.defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a proper identity store property from the given
   ** string value.
   **
   ** @param  value              the string value the identity store property
   **                            should be returned for.
   **
   ** @return                    the identity store property.
   */
  public static IdentityStoreProperty from(final String value) {
    for (IdentityStoreProperty cursor : IdentityStoreProperty.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}