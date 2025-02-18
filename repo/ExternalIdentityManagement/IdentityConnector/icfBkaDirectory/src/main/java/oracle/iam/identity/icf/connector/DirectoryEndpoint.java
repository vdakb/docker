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

    File        :   DirectoryEndpoint.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryEndpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import oracle.iam.identity.icf.foundation.AbstractEndpoint;
import oracle.iam.identity.icf.foundation.SystemConstant;
import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.resource.SystemBundle;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;
import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.resource.Connector;
import oracle.iam.identity.icf.resource.ConnectorBundle;
import oracle.iam.identity.icf.resource.DirectoryBundle;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.ObjectClass;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertificateException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

///////////////////////////////////////////////////////////////////////////////
// class DirectoryEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryEndpoint</code> wraps the Directory Service endpoint
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryEndpoint extends AbstractEndpoint<DirectoryEndpoint> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the protocol each Directory Service is using */
  static final String   PROTOCOL_DEFAULT                          = "ldap";

  /** the protocol each Directory Service is using over SSL */
  static final String   PROTOCOL_DEFAULT_SECURE                   = "ldaps";

  /** the property the security protocol to use. */
  static final String   PROTOCOL_SECURITY                         = "ssl";

  /** the type of certificate used to secure a connection with SSL. */
  static final String   CERTIFICATE_TYPE                          = "X.509";

  /**
   ** Default value of the name of the generic object class of an entry in a V3
   ** compliant Directory Service.
   */
  static final String   OBJECT_CLASS_DEFAULT                      = "objectClass";

  /**
   ** Default value of the initial context factory.
   */
  static final String   INITIAL_CONTEXT_FACTORY_DEFAULT           = "com.sun.jndi.ldap.LdapCtxFactory";

  /**
   ** Default value of the SSL security provider.
   */
  static final String   SECURITY_PROVIDER_DEFAULT                 = "com.sun.net.ssl.internal.ssl.Provider";

  static final String   SUPPORTED_CONTROL                         = "supportedControl";

  /**
   ** Default value of the name of the server-assigned Universally Unique
   ** Identifier UUID for the entry in a V3 compliant Directory Service.
   */
  static final String   ENTRY_IDENTIFIER_ATTRIBUTE_DEFAULT        = "entryUUID";

  /**
   ** Default value of the name of the creator name attribute of an entry in
   ** a V3 compliant Directory Service.
   */
  static final String   ENTRY_CREATOR_ATTRIBUTE_DEFAULT           = "creatorsName";

  /**
   ** Default value of the name of the create timestamp attribute of an entry in
   ** a V3 compliant Directory Service.
   */
  static final String   ENTRY_CREATED_ATTRIBUTE_DEFAULT           = "createTimestamp";

  /**
   ** Default value of the name of the modifier name attribute of an entry in
   ** a V3 compliant Directory Service.
   */
  static final String   ENTRY_MODIFIER_ATTRIBUTE_DEFAULT          = "modifiersName";

  /**
   ** Default value of the name of the modified timestamp attribute of an entry in
   ** a V3 compliant Directory Service.
   */
  static final String   ENTRY_MODIFIED_ATTRIBUTE_DEFAULT          = "modifyTimestamp";

  static final String   VENDOR_VERSION                            = "vendorVersion";

  /**
   ** Active Directory does not expose vendorName nor vendorVersion.
   ** <br>
   ** Using that proprietary rootDSE attribute to detect Active Directory.
   */
  static final String   DOMAIN_CONTEXT                            = "rootDomainNamingContext";

  /**
   ** Internet Directory does not expose vendorName nor vendorVersion.
   ** <br>
   ** Using that proprietary rootDSE attribute to detect Internet Directory.
   */
  static final String   ORCACLE_CONTEXT                           = "orclnormdn";

  static final String[] DSE_ATTRIBUTES                            = new String[] {
    SUPPORTED_CONTROL
  , VENDOR_VERSION
  , DOMAIN_CONTEXT
  , ORCACLE_CONTEXT
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the URL encoding.
   */
  private String                     urlEncoding                = "UTF-8";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the Initial Context factory.
   */
  private String                     contextFactory             = "com.sun.jndi.ldap.LdapCtxFactory";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the TLS security provider.
   */
  private String                     securityProvider           = "com.sun.net.ssl.internal.ssl.Provider";
  /**
   ***
   */
  private boolean                    fetchSchema                = false;
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the specifc type of the connected Directory Service.
   ** <p>
   ** Defaults to {@link Type#OUD}.
   */
  private Type                       serviceType                = Type.OUD;
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
  private boolean                    referentialIntegrity       = false;
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the <code>SearchControl</code> that instruct the
   ** server to return the results of a search request in smaller, more
   ** manageable packets rather than in one large block.
   */
  private boolean                    simplePageControl          = false;
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the <code>SearchControl</code> that instruct the
   ** server to return the results of a search request in smaller, more
   ** manageable packets rather than in one large block.
   */
  private boolean                    virtualListControl         = false;
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy where a V3 compliant Directory Service will publish schema
   ** entries.
   */
  private String                     schemaContainer            = "cn=Schema";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy where a V3 compliant Directory Service will publish catalog
   ** entries.
   */
  private String                     catalogContainer           = "cn=Catalog";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy where a V3 compliant Directory Service will publish changelog
   ** entries.
   */
  private String                     changeLogContainer         = "cn=changeLog";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name of the change type that a V3 compliant
   ** Directory Service provides in the changelog.
   */
  private String                     changeLogChangeType        = "changeType";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name to access the global uid that a V3
   ** compliant Directory Service provides in the changelog.
   */
  private String                     changeLogChangeNumber      = "changeNumber";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name to access the first change number that a V3
   ** compliant Directory Service provides in the root DSE.
   */
  private String                     changeLogNumberFirst       = "firstChangeNumber";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name to access the last change number that a V3
   ** compliant Directory Service provides in the root DSE.
   */
  private String                     changeLogNumberLast        = "lastChangeNumber";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name to access the global uid that a V3
   ** compliant Directory Service provides in the changelog.
   */
  private String                     changeLogTargetGUID        = "targetGUID";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy the attribute name to access the distinguished name of a
   ** change subject that a V3 compliant Directory Service provides in the
   ** changelog.
   */
  private String                     changeLogTargetDN          = "targetDN";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specifiy where a V3 compliant Directory Service will publish change
   ** status entries.
   */
  private String                     changeStatusContainer      = "cn=changeStatus";
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the modified timestamp attribute of an entry in a
   ** directory.
   */
  private String                     objectClassName            = OBJECT_CLASS_DEFAULT;
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the object attributes that are binary attributes of
   ** an entry in the Directory Service.
   */
//  private Set<String>                binary                     = CollectionUtility.caseInsensitiveSet(CollectionUtility.set("audio","authorityRevocationList","caCertificate","certificateRevocationList","crossCertificatePair","deltaRevocationList","jpegPhoto","personalSignature","photo","supportedAlgorithms","thumbnailLogo","thumbnailPhoto","userCertificate","userPassword","x500UniqueIdentifier"));
  private Set<String>                binary                     = CollectionUtility.set("audio","authorityRevocationList","caCertificate","certificateRevocationList","crossCertificatePair","deltaRevocationList","jpegPhoto","personalSignature","photo","supportedAlgorithms","thumbnailLogo","thumbnailPhoto","userCertificate","userPassword","x500UniqueIdentifier");
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the names of the distinguished name attributes that may be
   ** contained any entry in a V3 compliant Directory Service.
   */
  private String                     distinguishedName          = "distinguishedName";
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the distinguished name attribute prefix of an entry
   ** in a V3 compliant Directory Service.
   ** <p>
   ** The standard prefix of a Directory Service V3 entry distinguished name
   ** attribute is <code>DN</code>.
   */
  private String                     distinguishedNamePrefix    = "dn";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the <code>any<code> object class of an entry in a
   ** V3 compliant Directory Service.
   */
  private Schema                     anyEntity                  = new Schema("*", CollectionUtility.set("*"));
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the role object class of an entry in a V3
   ** compliant Directory Service.
   */
  private Schema                     roleEntity                 = new Schema("cn", CollectionUtility.set("role"), "uniqueMember");
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the role object class of an entry in a V3
   ** compliant Directory Service.
   */
  private Schema                     scopeEntity                 = new Schema("cn", CollectionUtility.set("scope"), "uniqueMember");
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the role object class of an entry in a V3
   ** compliant Directory Service.
   */
  private Schema                     groupEntity                = new Schema("cn", CollectionUtility.set("groupOfUniqueNames"), "uniqueMember");
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the domain object class of an entry in a V3
   ** compliant Directory Service.
   ** <p>
   ** The standard object class name of a Directory Service V3 account is
   ** <code>domain</code> with the <code>dc</code> as the prefix.
   */
  private Entry                      domainEntity               = new Entry("dc", CollectionUtility.set("domain"), "uniqueMember", "uniqueMember");
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the locality object class of an entry in a V3
   ** compliant Directory Service.
   ** <p>
   ** The standard object class name of a Directory Service V3 account is
   ** <code>locality</code> with the <code>l</code> as the prefix.
   */
  private Entry                      localityEntity             = new Entry("l", CollectionUtility.set("locality"), "uniqueMember", "uniqueMember");
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the country object class of an entry in a V3
   ** compliant Directory Service.
   ** <p>
   ** The standard object class name of a Directory Service V3 account is
   ** <code>country</code> with the <code>c</code> as the prefix.
   */
  private Entry                      countryEntity              = new Entry("c", CollectionUtility.set("country"), "uniqueMember", "uniqueMember");
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organization object class of an entry in a V3
   ** compliant Directory Service.
   ** <p>
   ** The standard object class name of a Directory Service V3 account is
   ** <code>organization</code> with the <code>o</code> as the prefix.
   */
  private Entry                      organizationEntity         = new Entry("o", CollectionUtility.set("organization"), "uniqueMember", "uniqueMember");
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the organization object class of an entry in a V3
   ** compliant Directory Service.
   ** <p>
   ** The standard object class name of a Directory Service V3 account is
   ** <code>organization</code> with the <code>ou</code> as the prefix.
   */
  private Entry                      organizationalUnitEntity   = new Entry("ou", CollectionUtility.set("organization"), "uniqueMember", "uniqueMember");
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the account object class of an entry in a V3
   ** compliant Directory Service.
   ** <p>
   ** The standard object class name of a Directory Service V3 account is
   ** <code>inetOrgPerson</code> with the <code>uid</code> as the prefix.
   */
  private Bind                       accountEntity              = new Bind("uid", CollectionUtility.set("inetOrgPerson","orclUser","orclUserV2"), "uniqueMember", "uniqueMember", "userPassword", false);

  private String[]                   serviceURL                 = null;

  private DirectoryEntry             root;
  private DirectorySchema            schema                     = null;
  private LdapContext                connection                 = null;

  private Set<String>                controls;

  private final Object               lock                       = new Object();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Type
  /**
   ** <code>Type</code> classifies the specific behavior of a Directory Service
   ** to take care about of vendor specific implementations.
   */
  public enum Type {
      ANY
    , OUD
    , OID
    , ADS
    , LDS
    , DSEE
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Schema
  /**
   ** <code>Schema</code> wraps the common properties of an entry in a connected
   ** Directory Service.
   */
  public static class Schema {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the prefix of the object class how it might exits in the connected
     ** Directory Service Schema.
     */
    protected String      prefix     = null;
    /**
     ** the name of the 'linking attribute' form one object class entry to
     ** another one how it might exits in the connected Directory Service
     ** Schema.
     */
    protected String      member     = null;
    /**
     ** the names of the object classes how they might defined in the connected
     ** Directory Service Schema.
     */
    protected Set<String> clazz      = null;
    /**
     ** the names of the multi-value attributes how they might be declared in
     ** the connected Directory Service Schema for the correspoding object
     ** class.
     */
    protected Set<String> multiValue = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a object class entry with the specified class name and
     ** prefix.
     **
     ** @paramm  prefix          the prefix of the entry used as the relative
     **                          distinguisehd name.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @paramm  clazz           the names of the object classes how they might
     **                          defined in the connected Directory Service
     **                          Schema.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     */
    private Schema(final String prefix, final Set<String> clazz) {
      // ensure inheritance
      this(prefix, clazz, null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a object class entry link with the specified class name and
     ** prefix.
     **
     ** @paramm  prefix          the prefix of the entry used as the relative
     **                          distinguisehd name.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @paramm  clazz           the names of the object classes how they might
     **                          defined in the connected Directory Service
     **                          Schema.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @paramm  member          the name of the 'linking attribute' form one
     **                          object class entry to another one how it might
     **                          exits in the connected Directory Service
     **                          Schema.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Schema(final String prefix, final Set<String> clazz, final String member) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.clazz  = clazz;
      this.prefix = prefix;
      this.member = member;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Entry
  /**
   ** <code>Entry</code> wraps the common properties of linking {@link Entry}s
   ** in a connected Directory Service.
   ** <p>
   ** A permission can be a <code>Group</code> or <code>Role</code>.
   */
  private class Entry extends Schema {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the name of the 'linking attribute' of a member object class entry to
     ** a role permission class how it might exits in the connected Directory
     ** Service Schema.
     */
    protected String role  = null;
    /**
     ** the name of the 'linking attribute' of a member object class entry to
     ** a role permission class how it might exits in the connected Directory
     ** Service Schema.
     */
    protected String scope  = null;
    /**
     ** the name of the 'linking attribute' of a member object class entry to
     ** a group permission class how it might exits in the connected Directory
     ** Service Schema.
     */
    protected String group = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a object class entry with the specified class name and
     ** prefix.
     **
     ** @paramm  prefix          the prefix of the entry used as the relative
     **                          distinguisehd name.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @paramm  clazz           the names of the object classes how they might
     **                          defined in the connected Directory Service
     **                          Schema.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param   role            the name of the 'linking attribute' of a member
     **                          object class entry to a role permission class
     **                          how it might exits in the connected Directory
     **                          Service Schema.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param   group           the name of the 'linking attribute' of a member
     **                          object class entry to a group permission class
     **                          how it might exits in the connected Directory
     **                          Service Schema.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Entry(final String prefix, final Set<String> clazz, final String role, final String group) {
      // ensure inheritance
      super(prefix, clazz);

      // initialize instance attributes
      this.role  = role;
      this.group = group;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Bind
  /**
   ** <code>Bind</code> wraps the capabiltities of a password crredential
   ** in a connected Directory Service.
   */
  class Bind extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the prefix of the password credential attribute of an object class entry
     ** how it might exits in the connected Directory Service Schema.
     */
    protected String  credential = null;
    /**
     ** Whether the password credential value needs to be quoted in the
     ** connected Directory Service Schema.
     */
    protected boolean quoted     = false;

    private final Set<Status> statusAttributes;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a object class entry with the specified class name and
     ** prefix.
     **
     **
     ** @paramm  prefix          the prefix of the entry used as the relative
     **                          distinguisehd name.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @paramm  clazz           the names of the object classes how they might
     **                          defined in the connected Directory Service
     **                          Schema.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param   role            the name of the 'linking attribute' of a member
     **                          object class entry to a role permission class
     **                          how it might exits in the connected Directory
     **                          Service Schema.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param   group           the name of the 'linking attribute' of a member
     **                          object class entry to a group permission class
     **                          how it might exits in the connected Directory
     **                          Service Schema.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  credential       the prefix of the credential attribute how it
     **                          might declared in the connected Directory
     **                          Service Schema.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  quoted           <code>true</code> if the password credential
     **                          value needs to be quoted; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    private Bind(final String prefix, final Set<String> clazz, final String role, final String group, final String credential, final boolean quoted) {
      // ensure inheritance
      super(prefix, clazz, role, group);

      // initialize instance attributes
      this.quoted     = quoted;
      this.credential = credential;
      this.statusAttributes = new HashSet<>();
    }

    private void addStatusAttribute(String[] values) {
      this.statusAttributes.add(new Status(values[0], values[1], values[2]));
    }

    class Status {
      private final String name;
      private final String enabled;
      private final String disabled;

      private Status(final String name, final String enabled, final String disabled) {
        this.name = name;
        this.enabled = enabled;
        this.disabled = disabled;
      }

      public String name() {
        return this.name;
      }

      public String enabled() {
        return this.enabled;
      }

      public String disabled() {
        return this.disabled;
      }

      @Override
      public String toString() {
        return this.name + "::" + this.enabled + "::" + this.disabled;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DirectoryEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private DirectoryEndpoint(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DirectoryEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  primary            the primary server properties on which the
   **                            Directory Service is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Directory Service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  language           the language code of the Directory Service
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            Country code of the ervice Provider
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private DirectoryEndpoint(final Loggable loggable, final Server primary, final String rootContext, final Principal principal, final boolean secureSocket, final String language, final String country, final String timeZone) {
    // ensure inheritance
    super(loggable, primary, rootContext, principal, secureSocket, language, country, timeZone);

    // initialize defaults
    entryIdentifierAttribute("entryUUID");
    // ensure the base context is setup properly base on the information
    // provided
    this.root = DirectoryEntry.build((StringUtility.empty(this.rootContext) ? DirectoryName.ROOT : DirectoryName.quiet(this.rootContext)), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap
  /**
   ** Returns the {@link LdapContext} this context is use to connect and perform
   ** operations on the Directory Service.
   **
   ** @return                    the {@link LdapContext} this task is use to
   **                            connect and perform operations on the
   **                            Directory Service.
   **                            <br>
   **                            Possible object is {@link LdapContext}.
   */
  public final LdapContext unwrap() {
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext (overridden)
  /**
   ** Sets the <code>rootContext</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>rootContext</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  @Override
  public final DirectoryEndpoint rootContext(final String value) {
    // ensure inheritance
    super.rootContext(value);

    // initialize the instance attributes base on the information provided
    this.root = DirectoryEntry.build((StringUtility.empty(this.rootContext) ? DirectoryName.ROOT : DirectoryName.quiet(this.rootContext)), null);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Sets the value of the URL encoding.
   **
   ** @param  value              the value of the URL encoding.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint urlEncoding(final String value) {
    this.urlEncoding = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the value of the URL encoding.
   **
   ** @return                    the value of the URL encoding.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String urlEncoding() {
    return this.urlEncoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextFactory
  /**
   ** Sets the value of the initial context factory.
   **
   ** @param  value              the value of the initial context factory.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint contextFactory(final String value) {
    this.contextFactory = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextFactory
  /**
   ** Returns the value of the initial context factory.
   **
   ** @return                    the value of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextFactory() {
    return this.contextFactory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityProvider
  /**
   ** Sets the value of the TLS security provider.
   **
   ** @param  value              the value of the TLS security provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint securityProvider(final String value) {
    this.securityProvider = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityProvider
  /**
   ** Returns the value of the TLS security provider.
   **
   ** @return                    the value of the TLS security provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String securityProvider() {
    return this.securityProvider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failover
  /**
   ** Sets the servers that should be used if it will not be possible to
   ** establish a connection to the primary server defined in the
   ** <code>IT Resource</code>.
   **
   ** @param  value              the servers that should be used if it will not
   **                            be possible to establish a connection to the
   **                            primary server defined in the
   **                            <code>IT Resource</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Server}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint failover(final List<Server> value) {
    this.failover = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failover
  /**
   ** Returns the servers that should be used if it will not be possible to
   ** establish a connection to the primary server defined in the
   ** <code>IT Resource</code>.
   **
   ** @return                    the servers that should be used if it will not
   **                            be possible to establish a connection to the
   **                            primary server defined in the
   **                            <code>IT Resource</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Server}.
   */
  public final List<Server> failover() {
    return this.failover;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   base
  /**
   ** Returns the root context of the connected Directory Service.
   **
   ** @return                    the root context of the connected Directory
   **                            Service.
   **                            <br>
   **                            Possible object is {@link DirectoryName}.
   */
  public final DirectoryName base() {
    return this.root.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchSchema
  /**
   ** Set <code>true</code> if the schema description should be obtain natively
   ** from the Directory Service.
   ** <br>
   ** If <code>false</code> is specified a well know static schmea description
   ** is populated without a roundtrip to the Directory Service.
   ** <p>
   ** Per default no roundtrip to the Directory Service happens.
   **
   ** @param  value              <code>true</code> if the schema description
   **                            should be obtain natively from the Directory
   **                            Service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint fetchSchema(final boolean value) {
    this.fetchSchema = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchSchema
  /**
   ** Returns <code>true</code> if the schema description should be obtain
   ** natively from the Directory Service.
   ** <br>
   ** If <code>false</code> is specified a well know static schmea description
   ** is populated without a roundtrip to the Directory Service.
   ** <p>
   ** Per default no roundtrip to the Directory Service happens.
   **
   ** @return                    <code>true</code> if the schema description
   **                            should be obtain natively from the Directory
   **                            Service.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean fetchSchema() {
    return this.fetchSchema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceType
  /**
   ** Set the type of the connected Directory Service.
   **
   ** @param  value              the type of the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Type}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint serviceType(final Type value) {
    this.serviceType = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceType
  /**
   ** Returns the type of the connected Directory Service.
   **
   ** @return                    the type of the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Type}.
   */
  public final Type serviceType() {
    return this.serviceType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryIdentifierAttribute (overridden)
  /**
   ** Returns the name of the attribute to detect the creator name of an
   ** Directory Service entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** As long as the configuration isn't equipped with a value this method
   ** returns {@link #ENTRY_IDENTIFIER_ATTRIBUTE_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            creator name of an Directory Service entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String entryIdentifierAttribute() {
    return this.identifier == null ? ENTRY_IDENTIFIER_ATTRIBUTE_DEFAULT : this.identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatorAttribute (overridden)
  /**
   ** Returns the name of the attribute to detect the creator name of an
   ** Directory Service entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** As long as the configuration isn't equipped with a value this method
   ** returns {@link #ENTRY_CREATOR_ATTRIBUTE_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            creator name of an Directory Service entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String entryCreatorAttribute() {
    return this.creator == null ? ENTRY_CREATOR_ATTRIBUTE_DEFAULT : this.creator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatedAttribute (overridden)
  /**
   ** Returns the name of the attribute to detect the created timestamp of an
   ** Directory Service entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** As long as the configuration isn't equipped with a value this method
   ** returns {@link #ENTRY_CREATOR_ATTRIBUTE_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            created timestamp of an Directory Service
   **                            entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryCreatedAttribute() {
    return this.created == null ? ENTRY_CREATED_ATTRIBUTE_DEFAULT : this.created;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifierAttribute (overridden)
  /**
   ** Returns the name of the attribute to detect the modifier name of an
   ** Directory Service entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** As long as the configuration isn't equipped with a value this method
   ** returns {@link #ENTRY_MODIFIER_ATTRIBUTE_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            modifier name of an Directory Service entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryModifierAttribute() {
    return this.modifier == null ? ENTRY_MODIFIER_ATTRIBUTE_DEFAULT : this.created;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifiedAttribute (overridden)
  /**
   ** Returns the name of the attribute to detect the modified timestamp of an
   ** Directory Service entry.
   ** <b>Note</b>:
   ** <br>
   ** As long as the configuration isn't equipped with a value this method
   ** returns {@link #ENTRY_MODIFIED_ATTRIBUTE_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            modified timestamp of an Directory Service
   **                            entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryModifiedAttribute() {
    return this.modified == null ? ENTRY_MODIFIED_ATTRIBUTE_DEFAULT : this.modified;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaSupport
  /**
   ** Returns the schema configuration for the specified {@link ObjectClass}
   ** <code>type</code>.
   **
   ** @param  type               the {@link ObjectClass} to lookup the schema.
   **                            <br>
   **                            Possible object is {@link ObjectClass}.
   **
   ** @return                    the schema configuration for the specified type
   **                            <br>
   **                            Possible object is
   **                            {@link DirectoryEndpoint.Schema}.
   */
  public final Schema schemaSupport(final ObjectClass type) {
    if (DirectorySchema.ANY.equals(type))
      return anyEntity;
    else if (ObjectClass.ACCOUNT.equals(type))
      return accountEntity;
    else if (ObjectClass.GROUP.equals(type))
      return groupEntity;
    else if (DirectorySchema.SCOPE.equals(type))
      return scopeEntity;
    else if (DirectorySchema.ROLE.equals(type))
      return roleEntity;
    else if (DirectorySchema.ORGANIZATION.equals(type))
      return organizationEntity;
    else if (DirectorySchema.ORGANIZATIONAL_UNIT.equals(type))
      return organizationalUnitEntity;
    else if (DirectorySchema.DOMAIN.equals(type))
      return domainEntity;
    else if (DirectorySchema.COUNTRY.equals(type))
      return countryEntity;
    else if (DirectorySchema.LOCALITY.equals(type))
      return localityEntity;
    else
      return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   referentialIntegrity
  /**
   ** Sets referential integrity is enabled in target Directory Service.
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
   **
   ** @param  value              separator sign for Strings that provides more
   **                            than one value.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint referentialIntegrity(final boolean value) {
    this.referentialIntegrity = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   referentialIntegrity
  /**
   ** Returns referential integrity is enabled in target Directory Service.
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
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean referentialIntegrity() {
    return this.referentialIntegrity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simplePageControl
  /**
   ** Set to <code>true</code> if the Simple Page Control should be used by a
   ** search operation.
   ** <p>
   ** Simple Page Control is not enabled by default.
   **
   ** @param  value              <code>true</code> if Simple Page Control should
   **                            used by a search operation; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint simplePageControl(final boolean value) {
    this.simplePageControl = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simplePageControl
  /**
   ** Returns <code>true</code> if the Simple Page Control should be used by a
   ** search operation.
   ** <p>
   ** Simple Page Control is not enabled by default.
   **
   ** @return                    <code>true</code> if Simple Page Control should
   **                            used by a search operation; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean simplePageControl() {
    return this.simplePageControl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   virtualListControl
  /**
   ** Set to <code>true</code> if the Virtual List Control should be used by a
   ** search operation.
   ** <p>
   ** Virtual List Control is not enabled by default.
   **
   ** @param  value              <code>true</code> if Virtual List Control
   **                            should used by a search operation; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint virtualListControl(final boolean value) {
    this.virtualListControl = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   virtualListControl
  /**
   ** Returns <code>true</code> if the Virtual List Control should be used by a
   ** search operation.
   ** <p>
   ** Virtual List Control is not enabled by default.
   **
   ** @return                    <code>true</code> if Virtual List Control
   **                            should used by a search operation; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean virtualListControl() {
    return this.virtualListControl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaContainer
  /**
   ** Sets the value where a V3 compliant Directory Service will publish schema
   ** entries.
   **
   ** @param  value              the value where a V3 compliant Directory
   **                            Service will publish schema entries.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint schemaContainer(final String value) {
    this.schemaContainer = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaContainer
  /**
   ** Returns the value where a V3 compliant Directory Service will publish schema
   ** entries.
   **
   ** @return                    the value where a V3 compliant Directory
   **                            Service will publish schema entries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String schemaContainer() {
    return this.schemaContainer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogContainer
  /**
   ** Sets the value where a V3 compliant Directory Service will publish catalog
   ** entries.
   **
   ** @param  value              the value where a V3 compliant Directory
   **                            Service will publish catalog entries.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint catalogContainer(final String value) {
    this.catalogContainer = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogContainer
  /**
   ** Returns the value where a V3 compliant Directory Service will publish catalog
   ** entries.
   **
   ** @return                    the value where a V3 compliant Directory
   **                            Service will publish catalog entries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String catalogContainer() {
    return this.catalogContainer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogContainer
  /**
   ** Sets the value where a V3 compliant Directory Service will publish
   ** changeLog entries.
   **
   ** @param  value              the value where a V3 compliant Directory
   **                            Service will publish changeLog entries.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint changeLogContainer(final String value) {
    this.changeLogContainer = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogContainer
  /**
   ** Returns the value where a V3 compliant Directory Service will publish changeLog
   ** entries.
   **
   ** @return                    the value where a V3 compliant Directory
   **                            Service will publish changeLog entries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changeLogContainer() {
    return this.changeLogContainer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogChangeType
  /**
   ** Sets the attribute name of the change type that a V3 compliant Directory
   ** Service provides in the changelog.
   **
   ** @param  value              the attribute name of the change type that a
   **                            V3 compliant Directory Service provides in the
   **                            changelog.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint changeLogChangeType(final String value) {
    this.changeLogChangeType = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogChangeType
  /**
   ** Returns the attribute name of the change type that a V3 compliant
   ** Directory Service provides in the changelog.
   **
   ** @return                    the attribute name of the change type that a
   **                            V3 compliant Directory Service provides in the
   **                            changelog.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changeLogChangeType() {
    return this.changeLogChangeType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogChangeNumber
  /**
   ** Sets the attribute name of the change number that a V3 compliant Directory
   ** Service provides in the changelog.
   **
   ** @param  value              the attribute name of the change number that a
   **                            V3 compliant Directory Service provides in the
   **                            changelog.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint changeLogChangeNumber(final String value) {
    this.changeLogChangeNumber = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogChangeNumber
  /**
   ** Returns the attribute name of the change number that a V3 compliant
   ** Directory Service provides in the changelog.
   **
   ** @return                    the attribute name of the change number that a
   **                            V3 compliant Directory Service provides in the
   **                            changelog.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changeLogChangeNumber() {
    return this.changeLogChangeNumber;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogNumberFirst
  /**
   ** Sets the attribute name of the first change number that a V3 compliant
   ** Directory Service provides in the root DSE.
   **
   ** @param  value              the attribute name of the first change number
   **                            that a V3 compliant Directory Service provides
   **                            in the root DSE.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint changeLogNumberFirst(final String value) {
    this.changeLogNumberFirst = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogNumberFirst
  /**
   ** Returns the attribute name of the first change number that a V3 compliant
   ** Directory Service provides in the root DSE.
   **
   ** @return                    the attribute name of the first change number
   **                            that a V3 compliant Directory Service provides
   **                            in the root DSE.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changeLogNumberFirst() {
    return this.changeLogNumberFirst;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogNumberLast
  /**
   ** Sets the attribute name of the last change number that a V3 compliant
   ** Directory Service provides in the root DSE.
   **
   ** @param  value              the attribute name of the last change number
   **                            that a V3 compliant Directory Service provides
   **                            in the root DSE.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint changeLogNumberLast(final String value) {
    this.changeLogNumberLast = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogNumberLast
  /**
   ** Returns the attribute name of the last change number that a V3 compliant
   ** Directory Service provides in the root DSE.
   **
   ** @return                    the attribute name of the last change number
   **                            that a V3 compliant Directory Service provides
   **                            in the root DSE.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changeLogNumberLast() {
    return this.changeLogNumberLast;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogTargetGUID
  /**
   ** Sets the attribute name to access the global uid that a V3 compliant
   ** Directory Service provides in the changelog.
   **
   ** @param  value              the attribute name to access the global uid
   **                            that a V3 compliant Directory Service provides
   **                            in the changelog.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint changeLogTargetGUID(final String value) {
    this.changeLogTargetGUID = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogTargetGUID
  /**
   ** Returns the attribute name to access the global uid that a V3 compliant
   ** Directory Service provides in the changelog.
   **
   ** @return                    the attribute name to access the global uid
   **                            that a V3 compliant Directory Service provides
   **                            in the changelog.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changeLogTargetGUID() {
    return this.changeLogTargetGUID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogTargetDN
  /**
   ** Sets the attribute name to access the distinguished name that a V3
   ** compliant Directory Service provides in the changelog.
   **
   ** @param  value              the attribute name to access the distinguished
   **                            name that a V3 compliant Directory Service
   **                            provides in the changelog.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint changeLogTargetDN(final String value) {
    this.changeLogTargetDN = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLogTargetDN
  /**
   ** Returns the attribute name to access the distinguished name that a V3
   ** compliant Directory Service provides in the changelog.
   **
   ** @return                    the attribute name to access the distinguished
   **                            name that a V3 compliant Directory Service
   **                            provides in the changelog.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changeLogTargetDN() {
    return this.changeLogTargetDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeStatusContainer
  /**
   ** Sets the value where a V3 compliant Directory Service will publish
   ** change status entries.
   **
   ** @param  value              the value where a V3 compliant Directory
   **                            Service will publish change status entries.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint changeStatusContainer(final String value) {
    this.changeStatusContainer = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeStatusContainer
  /**
   ** Returns the value where a V3 compliant Directory Service will publish
   ** change status entries.
   **
   ** @return                    the value where a V3 compliant Directory
   **                            Service will publish change status entries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changeStatusContainer() {
    return this.changeStatusContainer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassName
  /**
   ** Sets the name of the object class attribute of an entry in a V3 compliant
   ** Directory Service.
   **
   ** @param  value              the name of the object class attribute of an
   **                            entry in a V3 compliant Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint objectClassName(final String value) {
    this.objectClassName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassName
  /**
   ** Returns the name of the object class attribute of an entry in a V3 compliant
   ** Directory Service.
   **
   ** @return                    the name of the object class attribute of an
   **                            entry in a V3 compliant Directory Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String objectClassName() {
    return this.objectClassName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binary
  /**
   ** Sets the name of the object attributes that are binary attributes of an
   ** entry in the Directory Service.
   **
   ** @param  value              the names of the multi-valued attributes for
   **                            object class account.
   **                            <br>
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint binary(final Set<String> value) {
    this.binary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binary
  /**
   ** Returns the name of the object attributes that are binary attributes of an
   ** entry in the Directory Service.
   **
   ** @return                    the name of the object attributes that are
   **                            binary attributes of an entry in the Directory
   **                            Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> binary() {
    return this.binary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedName
  /**
   ** Sets the name of the object attributes that are distinguishedName attributes of an
   ** entry in the Directory Service.
   ** <p>
   ** The toolkit is programmed to recognize the following set of common
   ** Directory Service distinguished name attributes:
   ** <table border="0" cellspacing="10" cellpadding="5" summary="">
   ** <tr><th align="left">Attribute ID</th><th align="left">OID</th><th align="left">Reference</th></tr>
   ** <tr><td>uniqueMember</td><td>2.5.4.50</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** <tr><td>manager</td><td>0.9.2342.19200300.100.1.10</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** <tr><td>secretary</td><td>0.9.2342.19200300.100.1.21</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** </table>
   **
   ** @param  value              the name of the object attributes that are
   **                            distinguishedName attributes of an entry in the Directory
   **                            Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint distinguishedName(final String value) {
    this.distinguishedName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedName
  /**
   ** Returns the name of the object attributes that are distinguishedName attributes of an
   ** entry in the Directory Service.
   ** <p>
   ** The toolkit is programmed to recognize the following set of common
   ** Directory Service distinguished name attributes:
   ** <table border="0" cellspacing="10" cellpadding="5" summary="">
   ** <tr><th align="left">Attribute ID</th><th align="left">OID</th><th align="left">Reference</th></tr>
   ** <tr><td>uniqueMember</td><td>2.5.4.50</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** <tr><td>manager</td><td>0.9.2342.19200300.100.1.10</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** <tr><td>secretary</td><td>0.9.2342.19200300.100.1.21</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** </table>
   **
   ** @return                    the name of the object attributes that are
   **                            distinguishedName attributes of an entry in the Directory
   **                            Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String distinguishedName() {
    return this.distinguishedName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNamePrefix
  /**
   ** Sets the name of the distinguished name attribute prefix of an entry in a
   ** V3 compliant Directory Service.
   **
   ** @param  value              the name of the distinguished name attribute
   **                            prefix of an entry in a V3 compliant Directory
   **                            Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint distinguishedNamePrefix(final String value) {
    this.distinguishedNamePrefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNamePrefix
  /**
   ** Returns the name of the distinguished name attribute prefix of an entry in
   ** a V3 compliant Directory Service.
   **
   ** @return                    the name of the distinguished name attribute
   **                            prefix of an entry in a V3 compliant Directory
   **                            Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String distinguishedNamePrefix() {
    return this.distinguishedNamePrefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   anyClass
  /**
   ** Returns the names of the any object classes how they might defined in the
   ** connected Directory Service Schema.
   **
   ** @return                    the names of the any object classes how they
   **                            might defined in the connected Directory
   **                            Service Schema.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> anyClass() {
    return this.anyEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   anyPrefix
  /**
   ** Sets the name of the any object prefix defined for a Directory Service.
   **
   ** @param  value              the name of the any object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint anyPrefix(final String value) {
    this.anyEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   anyPrefix
  /**
   ** Returns the name of the any object prefix defined for a Directory Service.
   **
   ** @return                    the name of the any object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String anyPrefix() {
    return this.anyEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleClass
  /**
   ** Sets the collection of names of the role object classes that need to be
   ** set for en entry if its created in the connected Directory Service.
   **
   ** @param  value              the collection of names of the role object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint roleClass(final Set<String> value) {
    this.roleEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleClass
  /**
   ** Returns the collection of names of the role object classes that need to be
   ** set for en entry if its created in the connected Directory Service.
   **
   ** @return                    the collection of names of the role object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> roleClass() {
    return this.roleEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rolePrefix
  /**
   ** Sets the name of the role entry prefix defined for a Directory Service.
   **
   ** @param  value              the name of the role object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint rolePrefix(final String value) {
    this.roleEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rolePrefix
  /**
   ** Returns the name of the role entry prefix defined for a Directory Service.
   **
   ** @return                    the name of the role object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String rolePrefix() {
    return this.roleEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleMember
  /**
   ** Sets the name of the attribute prefix of a membership of an object
   ** within a role defined for a Directory Service.
   **
   ** @param  value              the names of the multi-valued attribute prefix
   **                            for object class role.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint roleMember(final String value) {
    this.roleEntity.member = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleMember
  /**
   ** Returns the name of the attribute prefix of a membership of an object
   ** within a role defined for a Directory Service.
   **
   ** @return                    the name of the attribute prefix of a
   **                            membership of an object within a role.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String roleMember() {
    return this.roleEntity.member;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   scopeClass
  /**
   ** Sets the collection of names of the scope object classes that need to be
   ** set for en entry if its created in the connected Directory Service.
   **
   ** @param  value              the collection of names of the scope object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint scopeClass(final Set<String> value) {
    this.scopeEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scopeClass
  /**
   ** Returns the collection of names of the scope object classes that need to
   ** be set for en entry if its created in the connected Directory Service.
   **
   ** @return                    the collection of names of the scope object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> scopeClass() {
    return this.scopeEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scopePrefix
  /**
   ** Sets the name of the scope entry prefix defined for a Directory Service.
   **
   ** @param  value              the name of the scope object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint scopePrefix(final String value) {
    this.scopeEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scopPrefix
  /**
   ** Returns the name of the scope entry prefix defined for a Directory Service.
   **
   ** @return                    the name of the scope object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String scopePrefix() {
    return this.scopeEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scopeMember
  /**
   ** Sets the name of the attribute prefix of a membership of an object
   ** within a scope defined for a Directory Service.
   **
   ** @param  value              the names of the multi-valued attribute prefix
   **                            for object class scope.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint scopeMember(final String value) {
    this.scopeEntity.member = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scopeMember
  /**
   ** Returns the name of the attribute prefix of a membership of an object
   ** within a scope defined for a Directory Service.
   **
   ** @return                    the name of the attribute prefix of a
   **                            membership of an object within a scope.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String scopeMember() {
    return this.scopeEntity.member;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupClass
  /**
   ** Sets the collection of names of the group object classes that need to be
   ** set for en entry if its created in the connected Directory Service.
   **
   ** @param  value              the collection of names of the group object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint groupClass(final Set<String> value) {
    this.groupEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupClass
  /**
   ** Returns the collection of names of the group object classes that need to
   ** be set for en entry if its created in the connected Directory Service.
   **
   ** @return                    the collection of names of the group object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> groupClass() {
    return this.groupEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupPrefix
  /**
   ** Sets the name of the group object prefix defined for a Directory Service.
   **
   ** @param  value              the name of the group object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint groupPrefix(final String value) {
    this.groupEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupPrefix
  /**
   ** Returns the name of the group object prefix defined for a Directory
   ** Service.
   **
   ** @return                    the name of the group object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupPrefix() {
    return this.groupEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupMember
  /**
   ** Sets the name of the attribute prefix of a membership of an object within
   ** a group defined for a Directory Service.
   **
   ** @param  value              the name of the attribute prefix of a
   **                            membership of an object within a group defined
   **                            for a Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint groupMember(final String value) {
    this.groupEntity.member = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupMember
  /**
   ** Returns the name of the attribute prefix of a membership of an object
   ** within a group defined for a Directory Service.
   **
   ** @return                    the name of the attribute prefix of a
   **                            membership of an object within a group defined
   **                            for a Directory Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupMember() {
    return this.groupEntity.member;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainClass
  /**
   ** Sets the collection of names of the domain object classes that need to be
   ** set for en entry if its created in the connected Directory Service.
   **                            
   **
   ** @param  value              the collection of names of the domain object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint domainClass(final Set<String> value) {
    this.domainEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainClass
  /**
   ** Returns the collection of names of the domain object classes that need to
   ** be set for en entry if its created in the connected Directory Service.
   **
   ** @return                    the collection of names of the domain object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> domainClass() {
    return this.domainEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPrefix
  /**
   ** Sets the name of the domain object prefix defined for a Directory Service.
   **
   ** @param  value              the name of the domain object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint domainPrefix(final String value) {
    this.domainEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPrefix
  /**
   ** Returns the name of the domain object prefix defined for a Directory
   ** Service.
   **
   ** @return                    the name of the domain object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String domainPrefix() {
    return this.domainEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityClass
  /**
   ** Sets the collection of names of the locality object classes that need to
   ** be set for en entry if its created in the connected Directory Service.
   **
   ** @param  value              the collection of names of the locality object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint localityClass(final Set<String> value) {
    this.localityEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityClass
  /**
   ** Returns the collection of names of the locality object classes that need
   ** to be set for en entry if its created in the connected Directory Service.
   **
   ** @return                    the collection of names of the locality object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> localityClass() {
    return this.localityEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityPrefix
  /**
   ** Sets the name of the locality object prefix defined for a Directory Service.
   **
   ** @param  value              the name of the locality object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint localityPrefix(final String value) {
    this.localityEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityPrefix
  /**
   ** Returns the name of the locality object prefix defined for a Directory
   ** Service.
   **
   ** @return                    the name of the locality object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localityPrefix() {
    return this.localityEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryClass
  /**
   ** Sets the collection of names of the country object classes that need to
   ** be set for en entry if its created in the connected Directory Service.
   **
   ** @param  value              the collection of names of the country object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint countryClass(final Set<String> value) {
    this.countryEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryClass
  /**
   ** Returns the collection of names of the country object classes that need to
   ** be set for en entry if its created in the connected Directory Service.
   **
   ** @return                    the collection of names of the country object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> countryClass() {
    return this.countryEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryPrefix
  /**
   ** Sets the name of the country object prefix defined for a Directory
   ** Service.
   **
   ** @param  value              the name of the country object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint countryPrefix(final String value) {
    this.countryEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryPrefix
  /**
   ** Returns the name of the country object prefix defined for a Directory
   ** Service.
   **
   ** @return                    the name of the country object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String countryPrefix() {
    return this.countryEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationClass
  /**
   ** Sets the collection of names of the organization object classes that need
   ** to be set for en entry if its created in the connected Directory Service.
   **
   **
   ** @param  value              the collection of names of the organization
   **                            object classes that need to be set for en entry
   **                            if its created in the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationClass(final Set<String> value) {
    this.organizationEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationClass
  /**
   ** Returns the collection of names of the organization object classes that
   ** need to be set for en entry if its created in the connected Directory
   ** Service.
   **
   ** @return                    the collection of names of the organization
   **                            object classes that need to be set for en entry
   **                            if its created in the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> organizationClass() {
    return this.organizationEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationPrefix
  /**
   ** Sets the name of the organization object prefix defined for a Directory
   ** Service.
   **
   ** @param  value              the name of the organization object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationPrefix(final String value) {
    this.organizationEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationPrefix
  /**
   ** Returns the name of the organization object prefix defined for a Directory
   ** Service.
   **
   ** @return                    the name of the organization object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationPrefix() {
    return this.organizationEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationMultiValueAttribute
  /**
   ** Sets the {@link Set} of names of the organization multi-valued attributes
   ** defined in a Directory Service.
   **
   ** @param  value              the names of the multi-valued attributes for
   **                            object class organization.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationMultiValueAttribute(final Set<String> value) {
    this.organizationEntity.multiValue = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationMultiValueAttribute
  /**
   ** Returns the {@link Set} of names of the organization multi-valued
   ** attributes defined in a Directory Service.
   **
   ** @return                    the names of the multi-valued attributes for
   **                            object class organization.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {|link String}.
   */
  public final Set<String> organizationMultiValueAttribute() {
    return this.organizationEntity.multiValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationRoleMember
  /**
   ** Sets the name of the organization role membership attribute prefix defined
   ** for a Directory Service.
   **
   ** @param  value              the name of the organization role membership
   **                            attribute prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationRoleMember(final String value) {
    this.organizationEntity.role = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationRoleMember
  /**
   ** Returns the name of the organization role membership attribute prefix
   ** defined for a Directory Service.
   **
   ** @return                    the name of the organization role membership
   **                            attribute prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationRoleMember() {
    return this.organizationEntity.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationGroupMember
  /**
   ** Sets the name of the organization group membership attribute prefix
   ** defined for a Directory Service.
   **
   ** @param  value              the name of the organization group membership
   **                            attribute prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationGroupMember(final String value) {
    this.organizationEntity.group = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationGroupMember
  /**
   ** Returns the name of the organization group membership attribute prefix
   ** defined for a Directory Service.
   **
   ** @return                    the name of the organization group membership
   **                            attribute prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationGroupMember() {
    return this.organizationEntity.group;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitClass
  /**
   ** Sets the collection of names of the organizationalUnit object classes that
   ** need to be set for en entry if its created in the connected Directory
   ** Service.
   **
   ** @param  value              the collection of names of the
   **                            organizationalUnit object classes that need to
   **                            be set for en entry if its created in the
   **                            connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationalUnitClass(final Set<String> value) {
    this.organizationalUnitEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitClass
  /**
   ** Returns the collection of names of the organizationalUnit object classes
   ** that need to be set for en entry if its created in the connected Directory
   ** Service.
   **
   ** @return                    the collection of names of the
   **                            organizationalUnit object classes that need to
   **                            be set for en entry if its created in the
   **                            connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> organizationalUnitClass() {
    return this.organizationalUnitEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitPrefix
  /**
   ** Sets the name of the organizationalUnit object prefix defined for a
   ** Directory Service.
   **
   ** @param  value              the name of the organizationalUnit object
   **                            prefix. <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationalUnitPrefix(final String value) {
    this.organizationalUnitEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitPrefix
  /**
   ** Returns the name of the organizationalUnit object prefix defined for a
   ** Directory Service.
   **
   ** @return                    the name of the organizationalUnit object
   **                            prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationalUnitPrefix() {
    return this.organizationalUnitEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitMultiValueAttribute
  /**
   ** Sets the {@link Set} of names of the organizationalUnit multi-valued
   ** attributes defined in a Directory Service.
   **
   ** @param  value              the names of the multi-valued attributes for
   **                            object class organizationalUnit.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationalUnitMultiValueAttribute(final Set<String> value) {
    this.organizationalUnitEntity.multiValue = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitMultiValueAttribute
  /**
   ** Returns the {@link Set} of names of the organizationalUnit multi-valued
   ** attributes defined in a Directory Service.
   **
   ** @return                    the names of the multi-valued attributes for
   **                            object class organizationalUnit.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {|link String}.
   */
  public final Set<String> organizationalUnitMultiValueAttribute() {
    return this.organizationalUnitEntity.multiValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitRoleMember
  /**
   ** Sets the name of the organizationalUnit role membership attribute prefix
   ** defined for a Directory Service.
   **
   ** @param  value              the name of the organizationalUnit role
   **                            membership attribute prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationalUnitRoleMember(final String value) {
    this.organizationalUnitEntity.role = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitRoleMember
  /**
   ** Returns the name of the organizationalUnit role membership attribute
   ** prefix defined for a Directory Service.
   **
   ** @return                    the name of the organizationalUnit role
   **                            membership attribute prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationalUnitRoleMember() {
    return this.organizationalUnitEntity.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitGroupMember
  /**
   ** Sets the name of the organizationalUnit group membership attribute prefix
   ** defined for a Directory Service.
   **
   ** @param  value              the name of the organizationalUnit group
   **                            membership attribute prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint organizationalUnitGroupMember(final String value) {
    this.organizationalUnitEntity.group = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitGroupMember
  /**
   ** Returns the name of the organizationalUnit group membership attribute
   ** prefix defined for a Directory Service.
   **
   ** @return                    the name of the organizationalUnit group
   **                            membership attribute prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationalUnitGroupMember() {
    return this.organizationalUnitEntity.group;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountClass
  /**
   ** Sets the collection of names of the account object classes that need to be
   ** set for en entry if its created in the connected Directory Service.
   **
   ** @param  value              the collection of names of the account object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint accountClass(final Set<String> value) {
    this.accountEntity.clazz = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountClass
  /**
   ** Returns the collection of names of the account object classes that need to
   ** be set for en entry if its created in the connected Directory Service.
   **
   ** @return                    the collection of names of the account object
   **                            classes that need to be set for en entry if its
   **                            created in the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> accountClass() {
    return this.accountEntity.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPrefix
  /**
   ** Sets the name of the account object prefix defined for a Directory
   ** Service.
   **
   ** @param  value              the name of the account object prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint accountPrefix(final String value) {
    this.accountEntity.prefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPrefix
  /**
   ** Returns the name of the account object prefix defined for a Directory
   ** Service.
   **
   ** @return                    the name of the account object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountPrefix() {
    return this.accountEntity.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountMultiValue
  /**
   ** Sets the {@link Set} of names of the account multi-valued attributes
   ** defined in a Directory Service.
   **
   ** @param  value              the names of the multi-valued attributes for
   **                            object class account.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint accountMultiValue(final Set<String> value) {
    this.accountEntity.multiValue = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountMultiValue
  /**
   ** Returns the {@link Set} of names of the account multi-valued attributes
   ** defined in a Directory Service.
   **
   ** @return                    the names of the multi-valued attributes for
   **                            object class account.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {|link String}.
   */
  public final Set<String> accountMultiValue() {
    return this.accountEntity.multiValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRolePrefix
  /**
   ** Sets the prefix of the account role membership attribute prefix defined for
   ** a Directory Service.
   **
   ** @param  value              the prefix of the account role membership
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint accountRolePrefix(final String value) {
    this.accountEntity.role = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRolePrefix
  /**
   ** Returns the prefix of the account role membership attribute prefix defined
   ** for a Directory Service.
   **
   ** @return                    the prefix of the account role membership
   **                            attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountRolePrefix() {
    return this.accountEntity.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountGroupPrefix
  /**
   ** Sets the prefix of the account group membership attribute prefix defined
   ** for Directory Service.
   **
   ** @param  value              the prefix of the account group membership
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint accountGroupPrefix(final String value) {
    this.accountEntity.group = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountGroupPrefix
  /**
   ** Returns the prefix of the account group membership attribute prefix defined
   ** for a Directory Service.
   **
   ** @return                    the prefix of the account group membership
   **                            attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountGroupPrefix() {
    return this.accountEntity.group;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCredential
  /**
   ** Sets the prefix of the password credential attribute of an object class
   ** entry how it might exits in the connected Directory Service Schema.
   **
   ** @param  value              the prefix of the password credential attribute
   **                            of an object class entry how it might exits in
   **                            the connected Directory Service Schema.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint accountCredential(final String value) {
    this.accountEntity.credential = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCredential
  /**
   ** Returns the prefix of the password credential attribute of an object class
   ** entry how it might exits in the connected Directory Service Schema.
   **
   ** @return                    the prefix of the password credential attribute
   **                            of an object class entry how it might exits in
   **                            the connected Directory Service Schema.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountCredential() {
    return this.accountEntity.credential;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCredentialQuoted
  /**
   ** Whether the password credential value needs to be quoted in the connected
   ** Directory Service.
   **
   ** @param  value              <code>true</code> if the password credential
   **                            value needs to be quoted in a Directory Service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint accountCredentialQuoted(final boolean value) {
    this.accountEntity.quoted = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCredentialQuoted
  /**
   ** Returns <code>true</code> if the password value needs to be quoted for a
   ** Directory Service.
   **
   ** @return                    <code>true</code> if the password credential
   **                            value needs to be quoted in a Directory Service.
   **                            Possible object <code>boolean</code>.
   */
  public final boolean accountCredentialQuoted() {
    return this.accountEntity.quoted;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryStatusAttributes
  /**
   ** Whether the password credential value needs to be quoted in the connected
   ** Directory Service.
   **
   ** @param  value              <code>true</code> if the password credential
   **                            value needs to be quoted in a Directory Service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>DirectoryEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public final DirectoryEndpoint entryStatusAttributes(final String value) {
    String[] split = value.split("\\|");
    for (String cursor : split) {
      this.accountEntity.addStatusAttribute(cursor.split("::"));
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryStatusAttributes
  /**
   ** Returns <code>true</code> if the password value needs to be quoted for a
   ** Directory Service.
   **
   ** @return                    <code>true</code> if the password credential
   **                            value needs to be quoted in a Directory Service.
   **                            Possible object <code>boolean</code>.
   */
  public final Set<Bind.Status> entryStatusAttributes() {
    return this.accountEntity.statusAttributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryPrefix
  /**
   ** Returns the prefix of an embedded attribute belonging to the specified
   ** {@link ObjectClass}.
   **
   ** @param  type               the {@link ObjectClass} to lookup the prefix
   **                            from the configuration.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    the prefix for the attribute belonging to the
   **                            specified {@link ObjectClass}.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryPrefix(final ObjectClass type) {
    if (DirectorySchema.ANY.equals(type))
      return anyPrefix();
    else if (ObjectClass.ACCOUNT.equals(type))
      return accountPrefix();
    else if (ObjectClass.GROUP.equals(type))
      return groupPrefix();
    else if (DirectorySchema.ROLE.equals(type))
      return rolePrefix();
    else if (DirectorySchema.ORGANIZATION.equals(type))
      return organizationPrefix();
    else if (DirectorySchema.ORGANIZATIONAL_UNIT.equals(type))
      return organizationalUnitPrefix();
    else if (DirectorySchema.DOMAIN.equals(type))
      return domainPrefix();
    else if (DirectorySchema.COUNTRY.equals(type))
      return countryPrefix();
    else if (DirectorySchema.LOCALITY.equals(type))
      return localityPrefix();
    else
      return "entryDN";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordPrefix
  /**
   ** Returns the prefix of an password attribute belonging to the specified
   ** {@link ObjectClass}.
   **
   ** @param  objectClass        the {@link ObjectClass} to lookup the prefix of
   **                            an password attribute from the configuration.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    the the prefix of an password attribute
   **                            belonging to the specified {@link ObjectClass}.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String passwordPrefix(final ObjectClass objectClass) {
    if (ObjectClass.ACCOUNT_NAME.equals(objectClass.getObjectClassValue()))
      return accountCredential();
    else
      return "userPassword";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClass
  /**
   ** Returns the object class names for the specified {@link ObjectClass}.
   ** <p>
   ** If there are more than one object classe the list of these object classes
   ** is delimitted by the <code>|</code> (pipe) character.
   **
   ** @param  type               the {@link ObjectClass} to lookup the object
   **                            class names from the configuration.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    the names of the object classes how
   **                            they might defined in the connected Directory
   **                            Service Schema.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is as of type {@link String}.
   */
  public Set<String> objectClass(final ObjectClass type) {
    if (DirectorySchema.ANY.equals(type))
      return anyClass();
    else if (ObjectClass.ACCOUNT.equals(type))
      return accountClass();
    else if (ObjectClass.GROUP.equals(type))
      return groupClass();
    else if (DirectorySchema.ROLE.equals(type))
      return roleClass();
    else if (DirectorySchema.SCOPE.equals(type))
      return scopeClass();
    else if (DirectorySchema.ORGANIZATION.equals(type))
      return organizationClass();
    else if (DirectorySchema.ORGANIZATIONAL_UNIT.equals(type))
      return organizationalUnitClass();
    else if (DirectorySchema.DOMAIN.equals(type))
      return domainClass();
    else if (DirectorySchema.LOCALITY.equals(type))
      return localityClass();
    else if (DirectorySchema.COUNTRY.equals(type))
      return countryClass();
    else
      return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>DirectoryEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an newly created instance of
   **                            <code>DirectoryEndpoint</code> providing
   **                            default values only.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public static DirectoryEndpoint build(final Loggable loggable) {
    return new DirectoryEndpoint(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>DirectoryEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverHost         the host name or IP address of the target
   **                            system on which the Directory Service is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Directory Service is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the acccount of the Directory Service with
   **                            administrator privikeges.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   ** @param  language           the language code of the Directory Service
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            Country code of the ervice Provider
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
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
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>DirectoryEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public static DirectoryEndpoint build(final Loggable loggable, final String serverHost, final int serverPort, final String rootContext, final String principalName, final GuardedString principalPassword, final boolean secureSocket, final String language, final String country, final String timeZone) {
    return new DirectoryEndpoint(loggable, new Server(serverHost, serverPort), rootContext, new Principal(principalName, principalPassword), secureSocket, language, country, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>DirectoryEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  primary            the primary server properties on which the
   **                            Directory Service is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Directory Service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  language           the language code of the Directory Service
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            Country code of the ervice Provider
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
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
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>DirectoryEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryEndpoint</code>.
   */
  public static final DirectoryEndpoint build(final Loggable loggable, final Server primary, final String rootContext, final Principal principal, final boolean secureSocket, final String language, final String country, final String timeZone) {
    // ensure inheritance
    return new DirectoryEndpoint(loggable, primary, rootContext, principal, secureSocket, language, country, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Releases a {@link Context}'s resources immediately, instead of waiting
   ** for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent: invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param context             the {@link Context} to close.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @throws SystemException    in the specified context cannot be closed.
   */
  public static final void close(final Context context)
    throws SystemException {

    try {
      if (context != null) {
        context.close();
      }
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Determines if the configuration is valid.
   ** <p>
   ** A valid configuration is one that is ready to be used by the connector: it
   ** is complete (all the required properties have been given values) and the
   ** property values are well-formed (are in the expected range, have the
   ** expected format, etc.)
   ** <p>
   ** Implementations of this method <b>should not</b> connect to the resource
   ** in an attempt to validate the configuration. For example, implementations
   ** should not attempt to check that a host of the specified name exists by
   ** making a connection to it. Such checks can be performed in the
   ** implementation of the TestOp.test() method.
   **
   ** @throws RuntimeException   if the configuration is not valid.
   **                            Implementations are encouraged to throw the
   **                            most specific exception available. When no
   **                            specific exception is available,
   **                            implementations can throw
   **                            ConfigurationException.
   **
   ** @throws SystemException    if required properties are missed.
   */
  public final void validate()
    throws SystemException {

    // intentionally left blank
    if (this.identifier == null) {
      throw SystemException.propertyRequired(ConnectorBundle.string(Connector.Feature.ENTRY_IDENTIFIER_PREFIX_LABEL));      
    }
    if (this.creator == null) {
      throw SystemException.propertyRequired(ConnectorBundle.string(Connector.Feature.ENTRY_CREATOR_PREFIX_LABEL));      
    }
    if (this.created == null) {
      throw SystemException.propertyRequired(ConnectorBundle.string(Connector.Feature.ENTRY_CREATED_PREFIX_LABEL));      
    }
    if (this.modified == null) {
      throw SystemException.propertyRequired(ConnectorBundle.string(Connector.Feature.ENTRY_MODIFIER_PREFIX_LABEL));      
    }
    if (this.modifier == null) {
      throw SystemException.propertyRequired(ConnectorBundle.string(Connector.Feature.ENTRY_MODIFIED_PREFIX_LABEL));      
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Releases a {@link NamingEnumeration}'s resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent: invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param context             the {@link NamingEnumeration} to close.
   **                            <br>
   **                            Allowed object is {@link NamingEnumeration}.
   **
   ** @throws SystemException    in the specified context cannot be closed.
   */
  public static final void close(final NamingEnumeration<?> context)
    throws SystemException {

    try {
      if (context != null)
        context.close();
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Return the schema upon request.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link DirectorySchema}.
   **
   ** @throws SystemException    if the discovery of the schema fails.
   */
  public DirectorySchema schema()
    throws SystemException {

    if (this.schema == null)
      this.schema = this.fetchSchema ? new DirectorySchema.Server(this) : new DirectorySchema.Static(this);

    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   test
  /**
   ** Determines if the underlying JNDI/LDAP {@link Context} is valid.
   **
   ** @throws RuntimeException   if the underlying JNDI/LDAP
   **                            {@link Context} is not valid otherwise do
   **                            nothing.
   */
  public void test() {
    final String method = "test";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      debug(method, "connection tested");
    }
    catch (Exception e) {
//      throw ConnectorException.wrap(e);
    }
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootDSE
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource to connect
   ** to the rootDSE of an Directory Service.
   ** <p>
   ** In Directory Service 3.0, rootDSE is defined as the root of the directory
   ** data tree on a directory server. The rootDSE is not part of any namespace.
   ** The purpose of the rootDSE is to provide data about the directory server.
   **
   ** @return                    the context this connector use to communicate
   **                            with the rootDSE of an Directory Service.
   **                            <br>
   **                            Possible object is {@link LdapContext}.
   **
   ** @throws SystemException    if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public final LdapContext rootDSE()
    throws SystemException {

    StringBuilder url     = new StringBuilder();
    String[]     services = serviceURL();
    for (int i = 0; i < services.length; i++) {
      if (i > 0)
        url.append(SystemConstant.BLANK);
      // extend the list of urls with the current service
      url.append(services[i]);
    }

    // To search for root DSE,
    // 1. Set LDAP version to LDAP_V3 before binding
    // 2. Set the search base to an empty string
    // 3. Set the search filter to (objectclass=*)
    // 4. Set the search scope to LDAP_SCOPE_BASE
    return connect(environment(url.toString(), false));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   ** <p>
   ** This metho connects to the <code>null</code> node of the Directory
   ** Service.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Directory Service.
   **                            <br>
   **                            Possible object is {@link LdapContext}.
   **
   ** @throws SystemException    if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public LdapContext connect()
    throws SystemException {

    return connect(DirectoryName.ROOT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @param  contextPath        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Directory Service.
   **                            <br>
   **                            Possible object is {@link LdapContext}.
   **
   ** @throws SystemException    if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public LdapContext connect(final DirectoryName contextPath)
    throws SystemException {

    if (this.connection == null) {
      // Constructs an Directory Service context object using environment
      // properties and connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      this.connection = connect(environment(contextURL(contextPath), true));
    }
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Closes the managed directory context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   */
  public void disconnect() {
    disconnect(this.connection);
    this.connection = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Closes an unmanaged directory context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param  context            the {@link LdapContext} to close.
   **                            <br>
   **                            Allowed object is {@link LdapContext}.
   */
  public void disconnect(final LdapContext context) {
    try {
      if (context != null)
        context.close();
    }
    catch (NamingException e) {
      error("disconnect", DirectoryException.unhandled(e).getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Directory Information Tree.
   ** <p>
   ** The URL consists of the server part of the ldap url, ldap://host:port and
   ** the absolute path to the entry. The entry is post fixed with the context
   ** root of the connection.
   **
   ** @param  distinguishedName  a component of a Directory Information Tree
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the full qualified Directory Service URL.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the given distinguished name cannot
   **                            converted to a
   **                            <code>application/x-www-form-urlencoded</code>
   **                            MIME format.
   */
  public final String contextURL(final DirectoryName distinguishedName)
    throws SystemException {

    // encode the distinguished name that we want to access with the restriction
    // that the URLEncoder implements the HTML Specifications for how to encode
    // URLs in HTML forms. This fails for Directory Service URL's so we have to
    // replace all plus signs back to a space manually after the encoder return
    // the result
    String encodedPath = null;
    try {
      encodedPath = URLEncoder.encode((distinguishedName != null && distinguishedName.size() > 0) ? distinguishedName.absolute(this.root.name).toString() : "", this.urlEncoding);
      encodedPath = encodedPath.replace("+", "%20");
    }
    catch (UnsupportedEncodingException e) {
      throw SystemException.encodingUnsupported(this.urlEncoding);
    }

    StringBuilder url = new StringBuilder();
    String[] services = serviceURL();
    for (int i = 0; i < services.length; i++) {
      if (i > 0)
        url.append(SystemConstant.BLANK);
      // extend the list of urls with the current service
      url.append(services[i]);
      // create the service url (server name and port) prefixed with the
      // protocol check if the service url end already with a slash '/'
      if (services[i].charAt(services[i].length() - 1) != SystemConstant.SLASH)
        url.append(SystemConstant.SLASH);
      url.append(encodedPath);
    }
    // return the resulting url by escaping all space characters that may
    // contained by the appropriate encoded character
    return url.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server parts of the ldap url, ldap://host:port
   **
   ** @return                    the server part of the ldap url,
   **                            ldap://host:port
   */
  public String[] serviceURL() {
    if (this.serviceURL != null)
      return this.serviceURL;

    // lazy initialization of the configured service URL's
    final String primary = this.primary.serviceURL(this.secureSocket ? PROTOCOL_DEFAULT_SECURE : PROTOCOL_DEFAULT);
    // if no failover configuration is declared only the primary service
    // evaluated by the properties of the assigned IT Resource Definition can
    // be used
    if (this.failover == null || this.failover.size() == 0) {
      // create the array that provides exactly one entry for the primary
      // service
      this.serviceURL    = new String[1];
      this.serviceURL[0] = primary;
    }
    // if a failover configuration is declared the primary service evaluated
    // by the properties of the assigned IT Resource Definition and all
    // configured secondary services will be used
    else {
        // create the array that is large enough to receive all services
      this.serviceURL = new String[this.failover.size() + 1];
      // set the primary service as the first that has to be used by the
      // connection establishment
      int  service               = 0;
      this.serviceURL[service++] = primary;
      for (Server secondary : failover)
        this.serviceURL[service++] = secondary.serviceURL(this.secureSocket ? PROTOCOL_DEFAULT_SECURE : PROTOCOL_DEFAULT);
    }
    return this.serviceURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supported
  /**
   ** Returns <code>true</code> if the control with the given OID is supported
   ** by the Directory Service.
   **
   ** @param  control            the string representation of the OID to verify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the OID regarding the
   **                            given control is supported by the Directory
   **                            Service.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if the operation fails
   */
  public boolean supported(final String control)
    throws SystemException {

    return controls().contains(control);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates object and binds to the passed distinguished name and the
   ** attributes.
   ** <br>
   ** Calls the connection.createSubcontext() to create object and bind it with
   ** the passed distinguished name and the attributes.
   **
   ** @param  entryDN            the name of the object whose attributes will be
   **                            created.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  attribute          the initial attributes of the object.
   **                            <br>
   **                            Allowed object is {@link BasicAttributes}.
   **
   ** @return                    the context of the object that is created.
   **                            <br>
   **                            Possible object is {@link DirContext}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public final DirContext create(final DirectoryName entryDN, final BasicAttributes attribute)
    throws SystemException {
    try {
      connect(this.root.name);
      // create object
      return this.connection.createSubcontext(entryDN.relative(this.root.name), attribute);
    }
    // this exception is thrown when a context implementation does not support
    // the operation being invoked.
    catch (OperationNotSupportedException e) {
      throw DirectoryException.notSupported(e);
    }
    // this exception is thrown when attempting to perform an operation for
    // which the client has no permission.
    // The access control/permission model is dictated by the directory/naming
    // server. 
    catch (NoPermissionException e) {
      throw DirectoryException.notPermitted(e);
    }
    // this exception is thrown by methods to indicate that a binding cannot be
    // added because the name is already bound to another object.
    catch (NameAlreadyBoundException e) {
      throw DirectoryException.entryExists(e, entryDN);
    }
    // this exception is thrown when a method in some ways violates the schema.
    // An example of schema violation is modifying attributes of an object that
    // violates the object's schema definition. Another example is renaming or
    // moving an object to a part of the namespace that violates the namespace's
    // schema definition. 
    catch (SchemaViolationException e) {
      throw DirectoryException.schemaViolated(e);
    }
    // this exception is thrown when an operation attempts to add an attribute
    // that already exists. 
    catch (AttributeInUseException e) {
      throw DirectoryException.attributeInUse(e);
    }
    // this exception is thrown when an attempt is made to add or modify an
    // attribute set that has been specified incompletely or incorrectly.
    // This could happen, for example, when attempting to add or modify a
    // binding, or to create a new subcontext without specifying all the
    // mandatory attributes required for creation of the object.
    // Another situation in which this exception is thrown is by specification
    // of incompatible attributes within the same attribute set, or attributes
    // in conflict with that specified by the object's schema. 
    catch (InvalidAttributesException e) {
      throw DirectoryException.attributeInvalidData(e, entryDN);
    }
    // this exception is thrown when an attempt is made to add to create an
    // attribute with an invalid attribute identifier.
    // the validity of an attribute identifier is directory-specific. 
    catch (InvalidAttributeIdentifierException e) {
      throw DirectoryException.attributeInvalidType(e, entryDN);
    }
    // this exception is thrown when an attempt is made to add to an attribute a
    // value that conflicts with the attribute's schema definition.
    // This could happen, for example, if attempting to add an attribute with no
    // value when the attribute is required to have at least one value, or if
    // attempting to add more than one value to a single valued-attribute, or if
    // attempting to add a value that conflicts with the syntax of the
    // attribute. 
    catch (InvalidAttributeValueException e) {
      throw DirectoryException.attributeInvalidValue(e, entryDN);
    }
    // this exception is thrown when a component of the name cannot be resolved
    // because it is not bound.
    catch (NameNotFoundException e) {
      // if we got this type of exception it can be assumed that it belongs to
      // the context where the entry will be created within
      throw DirectoryException.entryNotFound(e, entryDN.suffix());
    }
    // this is the superclass of all exceptions thrown by operations in the
    // Context and DirContext interfaces.
    // The nature of the failure is described by the name of the subclass.
    // This exception captures the information pinpointing where the operation
    // failed, such as where resolution last proceeded to.
    catch (NamingException e) {
      throw DirectoryException.entryNotCreated(e, entryDN);
    }
    finally {
      disconnect();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes object of the passed distinguished name.
   ** <br>
   ** Calls the connection.destroySubcontext() to delete object.
   **
   ** @param  entryDN            the distinguished name of the entry to delete
   **                            in the Directory Service.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  deleteControl      the possibly <code>null</code> controls to
   **                            use. If <code>null</code>, no controls are
   **                            used.
   **                            <br>
   **                            Allowed object is array of {@link Control}.
   **
   ** @throws SystemException    if the operation fails
   */
  public final void delete(final DirectoryName entryDN, final Control[] deleteControl)
    throws SystemException {

    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      connect(this.root.name);
      if (deleteControl != null)
        this.connection.setRequestControls(deleteControl);

      // delete object
      this.connection.destroySubcontext(entryDN.relative(this.root.name));
    }
    // this exception is thrown when a context implementation does not support
    // the operation being invoked.
    catch (OperationNotSupportedException e) {
      throw DirectoryException.notSupported(e);//, distinguishedName);
    }
    // this exception is thrown when attempting to perform an operation for
    // which the client has no permission.
    // The access control/permission model is dictated by the directory/naming
    // server. 
    catch (NoPermissionException e) {
      throw DirectoryException.notPermitted(e);
    }
    // this exception is thrown when a component of the name cannot be resolved
    // because it is not bound.
    catch (NameNotFoundException e) {
      throw DirectoryException.entryNotFound(e, entryDN);
    }
    catch (NamingException e) {
      throw DirectoryException.entryNotDeleted(e, entryDN);
    }
    finally {
      disconnect();
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies object and binds to the passed distinguished name and the
   ** attributes.
   ** <br>
   ** Calls the connection.modifyAttributes() to modify the attributes of an
   ** entry.
   **
   ** @param  entryDN            the name of the object whose attributes will be
   **                            updated.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  attribute          the attributes to be used for the modification
   **                            <br>
   **                            <b>Must</b> not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link BasicAttributes}.
   ** @param  operation          the modification operation, one of:
   **                            <ul>
   **                              <li>{@link LdapContext#ADD_ATTRIBUTE}
   **                              <li>{@link LdapContext#REPLACE_ATTRIBUTE}
   **                              <li>{@link LdapContext#REMOVE_ATTRIBUTE}
   **                            </ul>
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws SystemException    if the operation fails.
   */
  public final void modify(final DirectoryName entryDN, final BasicAttributes attribute, final int operation)
    throws SystemException {

    final String method = "modify";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      connect(this.root.name);
      // modify object
      this.connection.modifyAttributes(entryDN.relative(this.root.name), operation, attribute);
    }
    // this exception is thrown when a context implementation does not support
    // the operation being invoked.
    catch (OperationNotSupportedException e) {
      throw DirectoryException.notSupported(e);
    }
    // this exception is thrown when attempting to perform an operation for
    // which the client has no permission.
    // The access control/permission model is dictated by the directory/naming
    // server. 
    catch (NoPermissionException e) {
      throw DirectoryException.notPermitted(e);
    }
    // this exception is thrown when a method in some ways violates the schema.
    // An example of schema violation is modifying attributes of an object that
    // violates the object's schema definition. Another example is renaming or
    // moving an object to a part of the namespace that violates the namespace's
    // schema definition. 
    catch (SchemaViolationException e) {
      throw DirectoryException.schemaViolated(e);
    }
    // this exception is thrown when an operation attempts to add an attribute
    // that already exists. 
    catch (AttributeInUseException e) {
      throw DirectoryException.attributeInUse(e);
    }
    // this exception is thrown when an attempt is made to add or modify an
    // attribute set that has been specified incompletely or incorrectly.
    // This could happen, for example, when attempting to add or modify a
    // binding, or to create a new subcontext without specifying all the
    // mandatory attributes required for creation of the object.
    // Another situation in which this exception is thrown is by specification
    // of incompatible attributes within the same attribute set, or attributes
    // in conflict with that specified by the object's schema. 
    catch (InvalidAttributesException e) {
      throw DirectoryException.attributeInvalidData(e, entryDN);
    }
    // this exception is thrown when an attempt is made to add to create an
    // attribute with an invalid attribute identifier.
    // the validity of an attribute identifier is directory-specific. 
    catch (InvalidAttributeIdentifierException e) {
      throw DirectoryException.attributeInvalidType(e, entryDN);
    }
    // this exception is thrown when an attempt is made to add to an attribute
    // a value that conflicts with the attribute's schema definition.
    // This could happen, for example, if attempting to add an attribute with no
    // value when the attribute is required to have at least one value, or if
    // attempting to add more than one value to a single valued-attribute, or if
    // attempting to add a value that conflicts with the syntax of the
    // attribute. 
    catch (InvalidAttributeValueException e) {
      throw DirectoryException.attributeInvalidValue(e, entryDN);
    }
    // this exception is thrown when a component of the name cannot be resolved
    // because it is not bound.
    catch (NameNotFoundException e) {
      // if we got this type of exception it can be assumed that it belongs
      // to the context where the entry will be created within
      throw DirectoryException.entryNotFound(e, entryDN);
    }
    // this is the superclass of all exceptions thrown by operations in the
    // Context and DirContext interfaces.
    // The nature of the failure is described by the name of the subclass.
    // This exception captures the information pinpointing where the operation
    // failed, such as where resolution last proceeded to.
    catch (NamingException e) {
      switch (operation) {
        case  LdapContext.ADD_ATTRIBUTE     : throw DirectoryException.attributeNotAssigned(e, entryDN);
        case  LdapContext.REMOVE_ATTRIBUTE  : throw DirectoryException.attributeNotRemoved(e, entryDN);
        case  LdapContext.REPLACE_ATTRIBUTE : 
        default                             : throw DirectoryException.entryNotUpdated(e, entryDN);
      }
    }
    catch (Exception e) {
      System.out.println("Exception: " + e);
    }
    finally {
      disconnect();
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** IT Resource that afterwards can be passed to a {@link LdapContext} to
   ** establish a connection to the target system.
   **
   ** @param  contextPath        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the environment context this connector use to
   **                            communicate with the Directory Service.
   **                            <br>
   **                            Possible object is {@link Properties}.
   **
   ** @throws SystemException    if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  protected final Properties environment(final String contextPath)
    throws SystemException {

    return environment(contextPath, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** IT Resource that afterwards can be passed to a {@link LdapContext} to
   ** establish a connection to the target system.
   **
   ** @param  contextPath        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  pooling            advice whether or not the underlying system
   **                            should pool the connection.
   **                            Refere to http://java.sun.com/products/jndi/tutorial/ldap/connect/pool.html
   **                            for more information.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the environment context this connector use to
   **                            communicate with the Directory Service.
   **                            <br>
   **                            Possible object is {@link Properties}.
   **
   ** @throws SystemException    if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  protected final Properties environment(final String contextPath, final boolean pooling)
    throws SystemException {

    final String method = "environment";
    final Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(Context.PROVIDER_URL,                 contextPath);
    environment.put(Context.INITIAL_CONTEXT_FACTORY,      this.contextFactory);
    environment.put("java.naming.ldap.version",           "3");
    environment.put("com.sun.jndi.ldap.connect.timeout",  String.valueOf(this.timeOutConnect));
    environment.put("com.sun.jndi.ldap.read.timeout",     String.valueOf(this.timeOutResponse));
    // Enable or disable connection pooling
    environment.put("com.sun.jndi.ldap.connect.pool", pooling ? SystemConstant.TRUE : SystemConstant.FALSE);

    if (!StringUtility.blank(this.principal.username())) {
      environment.put(Context.SECURITY_AUTHENTICATION, "simple");
      environment.put(Context.SECURITY_PRINCIPAL, principal.username());
      if (this.principal.password() != null) {
        environment.put(Context.SECURITY_CREDENTIALS, CredentialAccessor.string(this.principal.password()));
      }
    }
    else  {
      environment.put(Context.SECURITY_AUTHENTICATION, "none");
    }

    if (this.secureSocket) {
      environment.put(Context.SECURITY_PROTOCOL, PROTOCOL_SECURITY);
      Provider provider = Security.getProvider(this.securityProvider);
      try {
        if (provider == null) {
          @SuppressWarnings("unchecked")
          Class<Provider> clazz = (Class<Provider>)Class.forName(SECURITY_PROVIDER_DEFAULT);
          Provider provider1 = clazz.newInstance();
          Security.addProvider(provider1);
        }
      }
      catch (ClassNotFoundException e) {
        error(method, SystemBundle.string(SystemError.CLASSNOTFOUND, SECURITY_PROVIDER_DEFAULT));
      }
      catch (InstantiationException e) {
        error(method, SystemBundle.string(SystemError.CLASSNOTCREATE, SECURITY_PROVIDER_DEFAULT));
      }
      catch (IllegalAccessException e) {
        error(method, SystemBundle.string(SystemError.CLASSNOACCESS, SECURITY_PROVIDER_DEFAULT));
      }
    }
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @param  environment        environment used to create the initial
   **                            {@link InitialLdapContext}.
   **                            <code>null</code> indicates an empty
   **                            environment.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Directory Service.
   **                            <br>
   **                            Possible object is {@link LdapContext}.
   **
   ** @throws SystemException    if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  private final LdapContext connect(final Properties environment)
    throws SystemException {

    LdapContext  context  = null;
    try {
      final String url = URLDecoder.decode(environment.getProperty(Context.PROVIDER_URL), this.urlEncoding);
      info(DirectoryBundle.string(DirectoryMessage.CONNECTING_BEGIN, url, principalUsername()));
      // Constructs an Directory Service context object using environment
      // properties and connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      context = new InitialLdapContext(environment, null);
      info(DirectoryBundle.string(DirectoryMessage.CONNECTING_SUCCESS, url, principalUsername()));
    }
    catch (UnsupportedEncodingException e) {
      throw SystemException.encodingUnsupported(this.urlEncoding);
    }
    // when the host, port or something else is wrong in the server properties
    catch (CommunicationException e) {
      // a CommunicationException can have several reasons wrap in an embeded
      // exception
      // investigate the exception further to be able to send back the real
      // reason why the request failed
      Throwable cause = e;
      while(cause.getCause() != null) {
        cause = cause.getCause();
      }
      if (UnknownHostException.class.isInstance(cause)) {
        throw SystemException.unknownHost(primaryHost());
      }
      else if (SocketException.class.isInstance(cause)) {
        throw SystemException.createSocket(primaryHost(), primaryPort());
      }
      else if (SocketTimeoutException.class.isInstance(cause)) {
        throw SystemException.timedOut(primaryHost());
      }
      else if (SSLException.class.isInstance(cause)) {
        throw SystemException.createSocketSecure(primaryHost(), primaryPort());
      }
      else if (SSLHandshakeException.class.isInstance(cause)) {
        throw SystemException.createSocketSecure(primaryHost(), primaryPort());
      }
      else if (CertificateException.class.isInstance(cause)) {
        throw SystemException.certificatePath(primaryHost());
      }
      else if (CertPathBuilderException.class.isInstance(cause)) {
        throw SystemException.certificatePath(primaryHost());
      }
      else
        throw SystemException.connection(e);
    }
    // when the principal or password is wrong in the server properties
    catch (AuthenticationException e) {
      throw SystemException.authentication(principalUsername());
    }
    // when a problem may be with physical connectivity or Target System is not alive
    catch (ServiceUnavailableException e) {
      throw SystemException.unavailable(e);
    }
    // when the context path to connecto to is invalid
    catch (InvalidNameException e) {
      throw SystemException.general(e);
    }
    // when the operation fails in general
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    return context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controls
  /**
   ** Returns the OID's of all controls supported by the  Directory Service.
   **
   ** @return                    the {@link Set} of suported OID's
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if the operation fails
   */
  private Set<String> controls()
    throws SystemException {

    // lazy initailaize the supported control cacche
    if (this.controls == null) {
      rootAttributes();
      this.controls = this.root.values(SUPPORTED_CONTROL);
    }
    return this.controls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootAttributes
  /**
   ** Request to populate attributes configured from the Directory Service root
   ** entry (DSE).
   **
   ** @throws SystemException    if the operation fails
   */
  private synchronized void rootAttributes()
    throws SystemException {

    if (this.root.attribute == null) {
      LdapContext root = null;
      try {
        synchronized (this.lock) {
          root = rootDSE();
          this.root.attribute = root.getAttributes(DirectoryName.ROOT, DSE_ATTRIBUTES);
          root.close();
        }
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
      finally  {
        disconnect(root);
      }
    }
  }
}