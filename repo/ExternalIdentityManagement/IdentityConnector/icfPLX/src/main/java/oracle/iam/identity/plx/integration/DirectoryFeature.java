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

    File        :   DirectoryFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.plx.integration;

import java.io.File;
import java.io.InputStream;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.connector.integration.TargetFeature;
import oracle.iam.identity.foundation.TaskException;

import org.xml.sax.InputSource;
////////////////////////////////////////////////////////////////////////////////
// class DirectoryFeature
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryFeature</code> provides the base feature description of
 ** a LDAP Directory.
 ** <br>
 ** Implementation of an LDAP may vary in locations of certain informations and
 ** object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryFeature extends TargetFeature {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryFeature</code> which is associated with
   ** the specified task.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DirectoryFeature</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  public DirectoryFeature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);

    // initialize default values as appropriate to avoid configuration and
    // parsing effort
    put(Directory.OIM.Feature.SECURITY_PROVIDER,         Directory.SECURITY_PROVIDER_DEFAULT);
    put(Directory.OIM.Feature.INITIAL_CONTEXT_FACTORY,   Directory.INITIAL_CONTEXT_FACTORY_DEFAULT);
    put(Directory.OIM.Feature.DISTINGUISHED_NAME_PREFIX, Directory.DISTINGUISHED_NAME_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.SCHEMA_CONTAINER,          Directory.SCHEMA_CONTAINER_DEFAULT);
    put(Directory.OIM.Feature.CHANGELOG_CONTAINER,       Directory.CHANGELOG_CONTAINER_DEFAULT);
    put(Directory.OIM.Feature.CHANGELOG_TARGETDN,        Directory.CHANGELOG_TARGETDN_DEFAULT);
    put(Directory.OIM.Feature.ENTRY_STATUS_PREFIX,       Directory.ENTRY_STATUS_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.REFERENTIAL_INTEGRITY,     Directory.REFERENTIAL_INTEGRITY_DEFAULT);
    put(Directory.OIM.Feature.ENTRY_IDENTIFIER_PREFIX,   Directory.ENTRY_IDENTIFIER_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.ENTRY_CREATOR_PREFIX,      Directory.ENTRY_CREATOR_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.ENTRY_CREATED_PREFIX,      Directory.ENTRY_CREATED_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.ENTRY_MODIFIER_PREFIX,     Directory.ENTRY_MODIFIER_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.ENTRY_MODIFIED_PREFIX,     Directory.ENTRY_MODIFIED_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.ENTRY_MODIFIED_PREFIX,     Directory.ENTRY_MODIFIED_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.GROUP_CLASS,               Directory.GROUP_CLASS_DEFAULT);
    put(Directory.OIM.Feature.GROUP_PREFIX,              Directory.GROUP_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.GROUP_MEMBER_PREFIX,       Directory.GROUP_MEMBER_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.ACCOUNT_CLASS,             Directory.ACCOUNT_CLASS_DEFAULT);
    put(Directory.OIM.Feature.ACCOUNT_PREFIX,            Directory.ACCOUNT_PREFIX_DEFAULT);
    put(Directory.OIM.Feature.ACCOUNT_CREDENTIAL_PREFIX, Directory.ACCOUNT_CREDENTIAL_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the url encoding.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#URL_ENCODING}.
   ** <p>
   ** If {@link Directory.OIM.Feature#URL_ENCODING} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#URL_ENCODING_DEFAULT}.
   **
   ** @return                    the url encoding.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public String urlEncoding() {
    return stringValue(Directory.OIM.Feature.URL_ENCODING, Directory.URL_ENCODING_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialContextFactory
  /**
   ** Returns the class name of the initial context factory.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#INITIAL_CONTEXT_FACTORY}.
   ** <p>
   ** If {@link Directory.OIM.Feature#INITIAL_CONTEXT_FACTORY} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#INITIAL_CONTEXT_FACTORY_DEFAULT}.
   **
   ** @return                    the class name of the initial context factory.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public String initialContextFactory() {
    return stringValue(Directory.OIM.Feature.INITIAL_CONTEXT_FACTORY, Directory.INITIAL_CONTEXT_FACTORY_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityProvider
  /**
   ** Returns the class name of the security provider implementation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#INITIAL_CONTEXT_FACTORY}.
   ** <p>
   ** If {@link Directory.OIM.Feature#SECURITY_PROVIDER} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#SECURITY_PROVIDER_DEFAULT}.
   **
   ** @return                    the class name of the security provider
   **                            implementation.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public String securityProvider() {
    return stringValue(Directory.OIM.Feature.SECURITY_PROVIDER, Directory.SECURITY_PROVIDER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator character for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link Directory.OIM.Feature#MULTIVALUE_SEPARATOR} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#MULTIVALUE_SEPARATOR_DEFAULT}.
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String multiValueSeparator() {
    return stringValue(Directory.OIM.Feature.MULTIVALUE_SEPARATOR, Directory.MULTIVALUE_SEPARATOR_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaContainer
  /**
   ** Returns the distinguished name from the <code>Metadata Descriptor</code>
   ** that defines the schema container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#SCHEMA_CONTAINER}.
   ** <p>
   ** If {@link Directory.OIM.Feature#SCHEMA_CONTAINER} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#SCHEMA_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link TargetFeature} that
   **                            defines the schema container.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String schemaContainer() {
    return stringValue(Directory.OIM.Feature.SCHEMA_CONTAINER, Directory.SCHEMA_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the catalog container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#CATALOG_CONTAINER}.
   ** <p>
   ** If {@link Directory.OIM.Feature#CATALOG_CONTAINER} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#CATALOG_CONTAINER_DEFAULT}.
   **
   ** @return                    the relative distinguished name of the catalog
   **                            container.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String catalogContainer() {
    return stringValue(Directory.OIM.Feature.CATALOG_CONTAINER, Directory.CATALOG_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the change log container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#CHANGELOG_CONTAINER}.
   ** <p>
   ** If {@link Directory.OIM.Feature#CHANGELOG_CONTAINER} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#CHANGELOG_CONTAINER_DEFAULT}.
   **
   ** @return                    the relative distinguished name of the change
   **                            log container.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String changelogContainer() {
    return stringValue(Directory.OIM.Feature.CHANGELOG_CONTAINER, Directory.CHANGELOG_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogChangeType
  /**
   ** Returns the name of the type of a changelog entry from the
   ** <code>Metadata Descriptor</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#CHANGELOG_CHANGETYPE}.
   ** <p>
   ** If {@link Directory.OIM.Feature#CHANGELOG_CHANGETYPE} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#CHANGE_TYPE_DEFAULT}.
   **
   ** @return                    the name of the type of a changelog entry.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String changelogChangeType() {
    return stringValue(Directory.OIM.Feature.CHANGELOG_CHANGETYPE, Directory.CHANGE_TYPE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogChangeNumber
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** attribute name of the change number within a changelog entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#CHANGELOG_CHANGENUMBER}.
   ** <p>
   ** If {@link Directory.OIM.Feature#CHANGELOG_CHANGENUMBER} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#CHANGE_NUMBER_DEFAULT}.
   **
   ** @return                    the name of the {@link TargetFeature} that
   **                            defines attribute name of the change number
   **                            within a changelog entry.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String changelogChangeNumber() {
    return stringValue(Directory.OIM.Feature.CHANGELOG_CHANGENUMBER, Directory.CHANGE_NUMBER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogTargetGUID
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** attribute name of the global uid within a changelog entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#CHANGELOG_TARGETGUID}.
   ** <p>
   ** If {@link Directory.OIM.Feature#CHANGELOG_TARGETGUID} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#CHANGELOG_TARGETGUID_DEFAULT}.
   **
   ** @return                    the name of the {@link TargetFeature} that
   **                            defines attribute name of the change number
   **                            within a changelog entry.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String changelogTargetGUID() {
    return stringValue(Directory.OIM.Feature.CHANGELOG_TARGETGUID, Directory.CHANGELOG_TARGETGUID_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogTargteDN
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** attribute name that provides the distinguished name of the target entry
   ** within a changelog entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#CHANGELOG_TARGETDN}.
   ** <p>
   ** If {@link Directory.OIM.Feature#CHANGELOG_TARGETDN} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#CHANGELOG_TARGETDN_DEFAULT}.
   **
   ** @return                    the name of the {@link TargetFeature} that
   **                            defines attribute name that provides the
   **                            distinguished name of the target entry
   **                            within a changelog entry.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String changelogTargteDN() {
    return stringValue(Directory.OIM.Feature.CHANGELOG_TARGETDN, Directory.CHANGELOG_TARGETDN_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   referentialIntegrity
  /**
   ** Returns <code>true</code> if referential integrity is enabled in target
   ** Directory Service.
   ** <p>
   ** Referential integrity is the process of maintaining consistent
   ** relationships among sets of data. If referential Integrity is enabled in
   ** the Directory Service, whenever an entry updated in the directory, the
   ** server also updates other entries that refer to that entry. For example,
   ** if a account entry is removed from the directory, and the account is a
   ** member of a group, the server also removes the account from the group. If
   ** referential integrity is not enabled, the user remains a member of the
   ** group until manually removed.
   ** <p>
   ** Referential integrity is not enabled by default.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#REFERENTIAL_INTEGRITY}.
   ** <p>
   ** If {@link Directory.OIM.Feature#REFERENTIAL_INTEGRITY} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#REFERENTIAL_INTEGRITY_DEFAULT}.
   **
   ** @return                    <code>true</code> if referential integrity is
   **                            enabled in target Directory Service; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean referentialIntegrity() {
    return booleanValue(Directory.OIM.Feature.REFERENTIAL_INTEGRITY, Directory.REFERENTIAL_INTEGRITY_DEFAULT);
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean simplePageControl() {
    return booleanValue(Directory.OIM.Feature.SIMPLEPAGE_CONTROL, false);
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
    return booleanValue(Directory.OIM.Feature.VIRTUALLIST_CONTROL, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormat
  /**
   ** Returns the format of a timestamp value in target Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#TIMESTAMP_FORMAT}.
   ** <p>
   ** If {@link Directory.OIM.Feature#TIMESTAMP_FORMAT} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#TIMESTAMP_FORMAT_DEFAULT}.
   **
   ** @return                    the format of a timestamp value in target
   **                            Directory Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String timestampFormat() {
    return stringValue(Directory.OIM.Feature.TIMESTAMP_FORMAT, Directory.TIMESTAMP_FORMAT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failoverConfiguration
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** failover configuration.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#FAILOVER_CONFIGURATION}.
   **
   ** @return                    the name of the {@link TargetFeature} that
   **                            defines the failover configuration.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link DirectoryServer}.
   */
  public final List<DirectoryServer> failoverConfiguration() {
    @SuppressWarnings("unchecked")
    final List<DirectoryServer> list = (List<DirectoryServer>)get(Directory.OIM.Feature.FAILOVER_CONFIGURATION);
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassName
  /**
   ** Returns the name of the generic object class defined for a Directory
   ** Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#OBJECT_CLASS_NAME}.
   ** <p>
   ** If {@link Directory.OIM.Feature#OBJECT_CLASS_NAME} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the name of the generic object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String objectClassName() {
    return stringValue(Directory.OIM.Feature.OBJECT_CLASS_NAME, Directory.OBJECT_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedName
  /**
   ** Returns the name of the attributes that are distinguished names in an
   ** entry in a Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#DISTINGUISHED_NAME}.
   ** <p>
   ** The toolkit is programmed to recognize the following set of common LDAP
   ** distinguished name attributes:
   ** <table border="0" cellspacing="10" cellpadding="5" summary="">
   ** <tr><th align="left">Attribute ID</th><th align="left">OID</th><th align="left">Reference</th></tr>
   ** <tr><td>uniqueMember</td><td>2.5.4.50</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** <tr><td>manager</td><td>0.9.2342.19200300.100.1.10</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** <tr><td>secretary</td><td>0.9.2342.19200300.100.1.21</td><td><a href="http://www.ietf.org/rfc/rfc4519.txt">RFC 4519</a></td></tr>
   ** </table>
   **
   ** @return                    the names of the object attributes that are
   **                            treated as binaries.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> distinguishedName() {
    return new HashSet<String>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryObjectAttribute
  /**
   ** Returns the name of the object attribute that is a binary attribute
   ** of an entry in a Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#BINARY_OBJECT_ATTRIBUTE}.
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
   ** @return                    the names of the object attributes that are
   **                            treated as binaries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String binaryObjectAttribute() {
    return stringValue(Directory.OIM.Feature.BINARY_OBJECT_ATTRIBUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNamePrefix
  /**
   ** Returns the name of the attribute to fetch the distinguished name of a
   ** LDAP entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#DISTINGUISHED_NAME_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#DISTINGUISHED_NAME_PREFIX} is not
   ** mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#DISTINGUISHED_NAME_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            distinguished name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String distinguishedNamePrefix() {
    return stringValue(Directory.OIM.Feature.DISTINGUISHED_NAME_PREFIX, Directory.DISTINGUISHED_NAME_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryIdentifierPrefix
  /**
   ** Returns the prefix of the server-assigned Universally Unique Identifier
   ** UUID attribute for the entry in a V3 compliant Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTRY_IDENTIFIER_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTRY_IDENTIFIER_PREFIX} is not mapped
   ** in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTRY_IDENTIFIER_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            created timestamp.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryIdentifierPrefix() {
    return stringValue(Directory.OIM.Feature.ENTRY_IDENTIFIER_PREFIX, Directory.ENTRY_IDENTIFIER_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatoPrefix
  /**
   ** Returns the prefix of the attribute to detect the creator name attribute
   ** for the entry in a V3 compliant Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTRY_CREATOR_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTRY_CREATOR_PREFIX} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTRY_CREATOR_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            creator name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryCreatorAttribute() {
    return stringValue(Directory.OIM.Feature.ENTRY_CREATOR_PREFIX, Directory.ENTRY_CREATOR_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatedPrefix
  /**
   ** Returns the prefix of the attribute to detect the created timestamp
   ** attribute for the entry in a V3 compliant Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTRY_CREATED_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTRY_CREATED_PREFIX} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTRY_CREATED_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            created timestamp.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryCreatedPrefix() {
    return stringValue(Directory.OIM.Feature.ENTRY_CREATED_PREFIX, Directory.ENTRY_CREATED_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifierPrefix
  /**
   ** Returns the prefix of the attribute to detect the modifier name attribute
   ** for the entry in a V3 compliant Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTRY_MODIFIER_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTRY_MODIFIER_PREFIX} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTRY_MODIFIER_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            modifier name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryModifierPrefix() {
    return stringValue(Directory.OIM.Feature.ENTRY_MODIFIER_PREFIX, Directory.ENTRY_MODIFIER_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifiedPrefix
  /**
   ** Returns the name of the attribute to detect the modified timestamp
   ** attribute for the entry in a V3 compliant Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTRY_MODIFIED_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTRY_MODIFIED_PREFIX} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTRY_MODIFIED_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            modified timestamp.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryModifiedPrefix() {
    return stringValue(Directory.OIM.Feature.ENTRY_MODIFIED_PREFIX, Directory.ENTRY_MODIFIED_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryStatusPrefix
  /**
   ** Returns the name of the attribute to detect the srarus attribute for the
   ** entry in a V3 compliant Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTRY_STATUS_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTRY_STATUS_PREFIX} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTRY_STATUS_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the attribute to detect the
   **                            modified timestamp.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryStatusPrefix() {
    return stringValue(Directory.OIM.Feature.ENTRY_STATUS_PREFIX, Directory.ENTRY_STATUS_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyClass
  /**
   ** Returns the name of the proxy object class defined for a Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#PROXY_CLASS}.
   **
   ** @return                    the name of the proxy object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String proxyClass() {
    return stringValue(Directory.OIM.Feature.PROXY_CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyPrefix
  /**
   ** Returns the name of the proxy object prefix defined for a Directory
   ** Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#PROXY_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#PROXY_PREFIX} is not mapped in the
   ** underlying {@link TargetFeature} this method returns <code>null</code>.
   **
   ** @return                    the name of the proxy object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String proxyPrefix() {
    return stringValue(Directory.OIM.Feature.PROXY_PREFIX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyMemberPrefix
  /**
   ** Returns the name of the attribute of a membership of an object within a
   ** proxy defined for a Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#PROXY_MEMBER_PREFIX}.
   **
   ** @return                    the name of the attribute of a membership of an
   **                            object within a proxy.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String proxyMemberPrefix() {
    return stringValue(Directory.OIM.Feature.PROXY_MEMBER_PREFIX);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   oganizationRootDN
  /**
   ** Returns the dn of the root organization where every assignable proxy
   ** entries in the home organization must be member of.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#HOME_ORGANIZATION_DN}.
   **
   ** @return                    the dn of the root organization.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String oganizationRootDN() {
    return stringValue(Directory.OIM.Feature.HOME_PROXY_DN);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRootDN
  /**
   ** Returns the dn of the root entry where every assignable user
   ** entries must be located as a sub-entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#HOME_ORGANIZATION_DN}.
   **
   ** @return                    the dn of the root organization.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountRootDN() {
    return stringValue(Directory.OIM.Feature.HOME_ORGANIZATION_DN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupClass
  /**
   ** Returns the name of the group object class defined for a Directory
   ** Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#GROUP_CLASS}.
   ** <p>
   ** If {@link Directory.OIM.Feature#GROUP_CLASS} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#GROUP_CLASS_DEFAULT}.
   **
   ** @return                    the name of the group object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupClass() {
    return stringValue(Directory.OIM.Feature.GROUP_CLASS, Directory.GROUP_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupPrefix
  /**
   ** Returns the prefix of the group object prefix defined for a Directory
   ** Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#GROUP_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#GROUP_PREFIX} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#GROUP_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the group object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupPrefix() {
    return stringValue(Directory.OIM.Feature.GROUP_PREFIX, Directory.GROUP_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupMemberPrefix
  /**
   ** Returns the prefix of the attribute of a membership of an object within a
   ** group defined for a Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#GROUP_MEMBER_PREFIX}.
   **
   ** @return                    the name of the attribute of a membership of an
   **                            object within a group.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupMemberPrefix() {
    return stringValue(Directory.OIM.Feature.GROUP_MEMBER_PREFIX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountClass
  /**
   ** Returns the name of the account object class defined for a Directory
   ** Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ACCOUNT_CLASS}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ACCOUNT_CLASS} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#ACCOUNT_CLASS_DEFAULT}.
   **
   ** @return                    the name of the account object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountClass() {
    return stringValue(Directory.OIM.Feature.ACCOUNT_CLASS, Directory.ACCOUNT_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPrefix
  /**
   ** Returns the prefix of the account object prefix defined for a Directory
   ** Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ACCOUNT_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ACCOUNT_PREFIX} is not mapped in the
   ** underlying {@link TargetFeature} this method returns
   ** {@link Directory#ACCOUNT_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the account object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountObjectPrefix() {
    return stringValue(Directory.OIM.Feature.ACCOUNT_PREFIX, Directory.ACCOUNT_PREFIX_DEFAULT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountMemberPrefix
  /**
   ** Returns the prefix of the attribute of a membership of an object within a
   ** account defined for a Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ACCOUNT_MEMBER_PREFIX}.
   **
   ** @return                    the name of the attribute of a membership of an
   **                            object within a account.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountMemberPrefix() {
    return stringValue(Directory.OIM.Feature.ACCOUNT_MEMBER_PREFIX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCredentialPrefix
  /**
   ** Returns the name of the account password prefix defined for a Directory
   ** Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ACCOUNT_CREDENTIAL_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ACCOUNT_CREDENTIAL_PREFIX} is not mapped
   ** in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ACCOUNT_CREDENTIAL_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the account password prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountCredentialPrefix() {
    return stringValue(Directory.OIM.Feature.ACCOUNT_CREDENTIAL_PREFIX, Directory.ACCOUNT_CREDENTIAL_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCredentialQuoted
  /**
   ** Returns the <code>true</code> if the account password needs to be in
   ** quotes for a Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ACCOUNT_CREDENTIAL_QUOTED}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ACCOUNT_CREDENTIAL_QUOTED} is not mapped
   ** in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ACCOUNT_CREDENTIAL_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the account password prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final boolean accountCredentialQuoted() {
    return booleanValue(Directory.OIM.Feature.ACCOUNT_CREDENTIAL_QUOTED, Directory.ACCOUNT_CREDENTIAL_QUOTED_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountMultiValue
  /**
   ** Returns the {@link List} of names of the account multi-valued attributes
   ** for a Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ACCOUNT_MULTIVALUE}.
   **
   ** @return                    the name of the account multi-valued
   **                            attributes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final List<String> accountMultiValue() {
    return stringList(Directory.OIM.Feature.ACCOUNT_MULTIVALUE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   oracleContextContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the oracle context
   ** container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ORACLE_CONTEXT_CONTAINER}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ORACLE_CONTEXT_CONTAINER} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ORACLE_CONTEXT_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link TargetFeature} that
   **                            defines the oracle context container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String oracleContextContainer() {
    return stringValue(Directory.OIM.Feature.ORACLE_CONTEXT_CONTAINER, Directory.ORACLE_CONTEXT_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oracleProductContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the oracle context
   ** container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ORACLE_PRODUCT_CONTAINER}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ORACLE_PRODUCT_CONTAINER} is not mapped in
   ** the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ORACLE_PRODUCT_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link TargetFeature} that
   **                            defines the oracle context container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String oracleProductContainer() {
    return stringValue(Directory.OIM.Feature.ORACLE_PRODUCT_CONTAINER, Directory.ORACLE_PRODUCT_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDomainContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the database domain
   ** container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_DOMAIN_CONTAINER}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_DOMAIN_CONTAINER} is not mapped
   ** in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_DOMAIN_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link TargetFeature} that
   **                            defines the database domain container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDomainContainer() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_DOMAIN_CONTAINER, Directory.ENTERPRISE_DOMAIN_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDatabaseClass
  /**
   ** Returns the name of the enterprise database object class defined for a
   ** Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_DATABASE_CLASS}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_DATABASE_CLASS} is not
   ** mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_DATABASE_CLASS_DEFAULT}.
   **
   ** @return                    the name of the database mapping class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDatabaseClass() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_DATABASE_CLASS, Directory.ENTERPRISE_DATABASE_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDatabasePrefix
  /**
   ** Returns the name of the enterprise database object prefix defined for a
   ** Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_DATABASE_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_DATABASE_PREFIX} is not
   ** mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_DATABASE_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the database mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDatabasePrefix() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_DATABASE_PREFIX, Directory.ENTERPRISE_DATABASE_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDomainClass
  /**
   ** Returns the name of the enterprise domain object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_DOMAIN_CLASS}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_DOMAIN_CLASS} is not
   ** mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_DOMAIN_CLASS_DEFAULT}.
   **
   ** @return                    the name of the domain mapping class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDomainClass() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_DOMAIN_CLASS, Directory.ENTERPRISE_DOMAIN_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseDomainPrefix
  /**
   ** Returns the name of the enterprise domain object prefix defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_DOMAIN_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_DOMAIN_PREFIX} is not
   **  mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_DOMAIN_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the domain mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDomainPrefix() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_DOMAIN_PREFIX, Directory.ENTERPRISE_DOMAIN_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaClass
  /**
   ** Returns the name of the schema db mapping object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_SCHEMA_CLASS}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_SCHEMA_CLASS} is not
   **  mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_SCHEMA_CLASS_DEFAULT}.
   **
   ** @return                    the name of the db schema mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseSchemaClass() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_SCHEMA_CLASS, Directory.ENTERPRISE_SCHEMA_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaPrefix
  /**
   ** Returns the name of the schema db mapping prefix defined for a Directory
   ** Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_SCHEMA_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_SCHEMA_PREFIX} is not
   ** mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_SCHEMA_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the db schema mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseSchemaPrefix() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_SCHEMA_PREFIX, Directory.ENTERPRISE_SCHEMA_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaAccount
  /**
   ** Returns the name of the schema mapping attribute defined for an LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_SCHEMA_ACCOUNT}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_SCHEMA_ACCOUNT} is not mapped
   ** in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_SCHEMA_ACCOUNT_DEFAULT}.
   **
   ** @return                    the name of the schema mapping attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseSchemaAccount() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_SCHEMA_ACCOUNT, Directory.ENTERPRISE_SCHEMA_ACCOUNT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseRoleClass
  /**
   ** Returns the name of the enterprise role object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_ROLE_CLASS}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_ROLE_CLASS} is not
   ** mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_ROLE_CLASS_DEFAULT}.
   **
   ** @return                    the name of the role mapping class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseRoleClass() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_ROLE_CLASS, Directory.ENTERPRISE_ROLE_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseRolePrefix
  /**
   ** Returns the name of the enterprise role object prefix defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTERPRISE_ROLE_PREFIX}.
   ** <p>
   ** If {@link Directory.OIM.Feature#ENTERPRISE_ROLE_PREFIX} is not
   ** mapped in the underlying {@link TargetFeature} this method returns
   ** {@link Directory#ENTERPRISE_ROLE_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the role mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseRolePrefix() {
    return stringValue(Directory.OIM.Feature.ENTERPRISE_ROLE_PREFIX, Directory.ENTERPRISE_ROLE_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordOperationSecured
  /**
   ** Returns the <code>true</code> if the LDAP Server requires secure socket
   ** configuration for password operations.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#PASSWORD_OPERATION_SECURED}.
   **
   ** @return                    <code>true</code> the LDAP Server requires
   **                            secure socket configuration for password
   **                            operations; otherwise <code>false</code>
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final boolean passwordOperationSecured() {
    return booleanValue(Directory.OIM.Feature.PASSWORD_OPERATION_SECURED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementPrefixRequired
  /**
   ** Returns the <code>true</code> if the entitlements has to be prefixed with
   ** the internal system identifier and the name of the
   ** <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Directory.OIM.Feature#ENTITLEMENT_PREFIX_REQUIRED}.
   **
   ** @return                    <code>true</code> the entitlements has to be
   **                            prefixed with the internal system identifier
   **                            and the name of the <code>IT Resource</code>;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final boolean entitlementPrefixRequired() {
    return booleanValue(Directory.OIM.Feature.ENTITLEMENT_PREFIX_REQUIRED);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   homeOrganizationDN
  /**
   ** Returns specify the dn of the home organization where the should be a
   ** sub-entry of.
   **
   ** @return                    the dn of the home organization.
   **                            <br>
   **                            Possible object is (@link String}.
   */
  public final String homeOrganizationDN() {
    return stringValue(Directory.OIM.Feature.HOME_PROXY_DN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registry  (TargetResource)
  /**
   ** Returns the mapping of attribute names defined on this
   ** <code>IT Resource</code> and the parameter names expected by connector
   ** bundle configuration.
   **
   ** @return                    the mapping of attribute names defined
   **                            on this <code>IT Resource</code> and the
   **                            parameter names expected by connector bundle
   **                            configuration.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final Map<String, String> registry() {
    return Directory.FEATURE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Metadata Descriptor</code> which
   ** is associated with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to be populated manually
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an instance of <code>DirectoryFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static DirectoryFeature build(final Loggable loggable) {
    return new DirectoryFeature(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link File}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    an instance of <code>DirectoryFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DirectoryFeature build(final Loggable loggable, final File file)
    throws TaskException {

    final DirectoryFeature feature = new DirectoryFeature(loggable);
    DirectoryFeatureFactory.configure(feature, file);
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link InputStream}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  stream             the mapping configuration file for the
   **                            descriptor to create as a XML stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    an instance of <code>DirectoryFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DirectoryFeature build(final Loggable loggable, final InputStream stream)
    throws TaskException {

    final DirectoryFeature feature = new DirectoryFeature(loggable);
    DirectoryFeatureFactory.configure(feature, stream);
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link InputSource}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    an instance of <code>DirectoryFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DirectoryFeature build(final Loggable loggable, final InputSource source)
    throws TaskException {

    final DirectoryFeature feature = new DirectoryFeature(loggable);
    DirectoryFeatureFactory.configure(feature, source);
    return feature;
  }
}