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

    File        :   DirectoryFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.util.List;

import javax.naming.ldap.Control;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.TaskException;

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
 ** @since   0.0.0.2
 */
public class DirectoryFeature extends AbstractLookup {

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
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this {@link AbstractLookup} configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private DirectoryFeature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryFeature</code> which is associated with the
   ** specified task and belongs to the Metadata Descriptor specified by the
   ** given name.
   ** <br>
   ** The Metadata Descriptorn will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this {@link AbstractLookup} configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instanceName       the name of the {@link AbstractLookup}
   **                            instance where this configuration wrapper will
   **                            obtains the values.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if the {@link AbstractLookup} is not defined in
   **                            the Oracle Identity Manager metadata entries.
   */
  private DirectoryFeature(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
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
   ** {@link DirectoryConstant#URL_ENCODING}.
   ** <p>
   ** If {@link DirectoryConstant#URL_ENCODING} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#URL_ENCODING_DEFAULT}.
   **
   ** @return                    the url encoding.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String urlEncoding() {
    return stringValue(DirectoryConstant.URL_ENCODING, DirectoryConstant.URL_ENCODING_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialContextFactory
  /**
   ** Returns the class name of the initial context factory.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#INITIAL_CONTEXT_FACTORY}.
   ** <p>
   ** If {@link DirectoryConstant#INITIAL_CONTEXT_FACTORY} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#INITIAL_CONTEXT_FACTORY_DEFAULT}.
   **
   ** @return                    the class name of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String initialContextFactory() {
    return stringValue(DirectoryConstant.INITIAL_CONTEXT_FACTORY, DirectoryConstant.INITIAL_CONTEXT_FACTORY_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityProvider
  /**
   ** Returns the class name of the security provider implementation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#INITIAL_CONTEXT_FACTORY}.
   ** <p>
   ** If {@link DirectoryConstant#SECURITY_PROVIDER} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#SECURITY_PROVIDER_DEFAULT}.
   **
   ** @return                    the class name of the security provider
   **                            implementation.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String securityProvider() {
    return stringValue(DirectoryConstant.SECURITY_PROVIDER, DirectoryConstant.SECURITY_PROVIDER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator character for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link DirectoryConstant#MULTIVALUE_SEPARATOR} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#MULTIVALUE_SEPARATOR_DEFAULT}.
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String multiValueSeparator() {
    return stringValue(DirectoryConstant.MULTIVALUE_SEPARATOR, DirectoryConstant.MULTIVALUE_SEPARATOR_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaContainer
  /**
   ** Returns the distinguished name from the <code>Metadata Descriptor</code>
   ** that defines the schema container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#SCHEMA_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#SCHEMA_CONTAINER} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#SCHEMA_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines the schema container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String schemaContainer() {
    return stringValue(DirectoryConstant.SCHEMA_CONTAINER, DirectoryConstant.SCHEMA_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the catalog container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CATALOG_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#CATALOG_CONTAINER} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#CATALOG_CONTAINER_DEFAULT}.
   **
   ** @return                    the relative distinguished name of the catalog
   **                            container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String catalogContainer() {
    return stringValue(DirectoryConstant.CATALOG_CONTAINER, DirectoryConstant.CATALOG_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the change log container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CHANGELOG_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#CHANGELOG_CONTAINER} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#CHANGELOG_CONTAINER_DEFAULT}.
   **
   ** @return                    the relative distinguished name of the change
   **                            log container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changelogContainer() {
    return stringValue(DirectoryConstant.CHANGELOG_CONTAINER, DirectoryConstant.CHANGELOG_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogChangeType
  /**
   ** Returns the name of the type of a changelog entry from the
   ** <code>Metadata Descriptor</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CHANGELOG_CHANGETYPE}.
   ** <p>
   ** If {@link DirectoryConstant#CHANGELOG_CHANGETYPE} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#CHANGE_TYPE}.
   **
   ** @return                    the name of the type of a changelog entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changelogChangeType() {
    return stringValue(DirectoryConstant.CHANGELOG_CHANGETYPE, DirectoryConstant.CHANGE_TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogChangeNumber
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** attribute name of the change number within a changelog entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CHANGELOG_CHANGENUMBER}.
   ** <p>
   ** If {@link DirectoryConstant#CHANGELOG_CHANGENUMBER} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#CHANGE_NUMBER}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines attribute name of the change number
   **                            within a changelog entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changelogChangeNumber() {
    return stringValue(DirectoryConstant.CHANGELOG_CHANGENUMBER, DirectoryConstant.CHANGE_NUMBER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogTargetGUID
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** attribute name of the global uid within a changelog entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CHANGELOG_TARGETGUID}.
   ** <p>
   ** If {@link DirectoryConstant#CHANGELOG_TARGETGUID} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#CHANGELOG_TARGETGUID_DEFAULT}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines attribute name of the change number
   **                            within a changelog entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changelogTargetGUID() {
    return stringValue(DirectoryConstant.CHANGELOG_TARGETGUID, DirectoryConstant.CHANGELOG_TARGETGUID_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogTargteDN
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** attribute name that provides the distinguished name of the target entry
   ** within a changelog entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CHANGELOG_TARGETDN}.
   ** <p>
   ** If {@link DirectoryConstant#CHANGELOG_TARGETDN} is not mapped in the
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#CHANGELOG_TARGETDN_DEFAULT}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines attribute name that provides the
   **                            distinguished name of the target entry within a
   **                            changelog entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changelogTargteDN() {
    return stringValue(DirectoryConstant.CHANGELOG_TARGETDN, DirectoryConstant.CHANGELOG_TARGETDN_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changestatusContainer
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** change status container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CHANGESTATUS_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#CHANGESTATUS_CONTAINER} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#CHANGESTATUS_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines the change log container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String changestatusContainer() {
    return stringValue(DirectoryConstant.CHANGESTATUS_CONTAINER, DirectoryConstant.CHANGESTATUS_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionTimeout
  /**
   ** Returns the timeout period for establishment of the LDAP connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an LDAP
   ** server. When connection pooling has been requested, this property also
   ** specifies the maximum wait time or a connection when all connections in
   ** pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the LDAP provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CONNECTION_TIMEOUT}.
   ** <p>
   ** If {@link DirectoryConstant#CONNECTION_TIMEOUT} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#CONNECTION_TIMEOUT_DEFAULT}.
   **
   ** @return                    the timeout period for establishment of the
   **                            LDAP connection.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String connectionTimeout() {
    return stringValue(DirectoryConstant.CONNECTION_TIMEOUT, DirectoryConstant.CONNECTION_TIMEOUT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseTimeout
  /**
   ** Returns the timeout period the LDAP provider doesn't get an LDAP response.
   ** <p>
   ** When an LDAP request is made by a client to a server and the server does
   ** not respond for some reason, the client waits forever for the server to
   ** respond until the TCP timeouts. On the client-side what the user
   ** experiences is esentially a process hang. In order to control the LDAP
   ** request in a timely manner, a read timeout can be configured for the
   ** JNDI/LDAP Service Provider since Java SE 6.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#RESPONSE_TIMEOUT}.
   ** <p>
   ** If {@link DirectoryConstant#RESPONSE_TIMEOUT} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#RESPONSE_TIMEOUT_DEFAULT}
   **
   ** @return                    the timeout period for establishment of the
   **                            LDAP connection.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String responseTimeout() {
    return stringValue(DirectoryConstant.RESPONSE_TIMEOUT, DirectoryConstant.RESPONSE_TIMEOUT_DEFAULT);
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
   ** if you a account entry is removed from the directory, and the account is a
   ** member of a group, the server also removes the account from the group. If
   ** referential integrity is not enabled, the user remains a member of the
   ** group until manually removed.
   ** <p>
   ** Referential integrity is not enabled by default.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#REFERENTIAL_INTEGRITY}.
   ** <p>
   ** If {@link DirectoryConstant#REFERENTIAL_INTEGRITY} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#REFERENTIAL_INTEGRITY_DEFAULT}.
   **
   ** @return                    <code>true</code> if referential integrity is
   **                            enabled in target Directory Service; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolea</code>.
   */
  public final boolean referentialIntegrity() {
    return booleanValue(DirectoryConstant.REFERENTIAL_INTEGRITY, DirectoryConstant.REFERENTIAL_INTEGRITY_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormat
  /**
   ** Returns the format of a timestamp value in target Directory Service.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#TIMESTAMP_FORMAT}.
   ** <p>
   ** If {@link DirectoryConstant#TIMESTAMP_FORMAT} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#TIMESTAMP_FORMAT_DEFAULT}.
   **
   ** @return                    the format of a timestamp value in target
   **                            Directory Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String timestampFormat() {
    return stringValue(DirectoryConstant.TIMESTAMP_FORMAT, DirectoryConstant.TIMESTAMP_FORMAT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failoverConfiguration
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** failover configuration.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#FAILOVER_CONFIGURATION}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines the failover configuration.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final List<DirectoryServer> failoverConfiguration() {
    @SuppressWarnings("unchecked")
    final List<DirectoryServer> list = (List<DirectoryServer>)get(DirectoryConstant.FAILOVER_CONFIGURATION);
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   paginationControl
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** name of the <code>SearchControl</code> that instruct the server to return
   ** the results of a search request in smaller, more manageable packets rather
   ** than in one large block.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#PAGINATION_CONTROL}.
   ** <p>
   ** If {@link DirectoryConstant#PAGINATION_CONTROL} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#PAGINATION_CONTROL_DEFAULT}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines the pagination control.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String paginationControl() {
    return stringValue(DirectoryConstant.PAGINATION_CONTROL, DirectoryConstant.PAGINATION_CONTROL_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   paginationControlCritical
  /**
   ** Returns <code>true</code> if the PaginationControl defined in the
   ** <code>Metadata Descriptor</code> is treated as a critical control
   ** extension.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#PAGINATION_CONTROL_CRITICAL}.
   ** <p>
   ** If {@link DirectoryConstant#PAGINATION_CONTROL_CRITICAL} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link Control#CRITICAL}.
   **
   ** @return                    <code>true</code> if the PaginationControl
   **                            defined in the {@link AbstractLookup} is
   **                            treated as a critical control extension;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean paginationControlCritical() {
    return booleanValue(DirectoryConstant.PAGINATION_CONTROL_CRITICAL, Control.CRITICAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassName
  /**
   ** Returns the name of the generic object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#OBJECT_CLASS_NAME}.
   ** <p>
   ** If {@link DirectoryConstant#OBJECT_CLASS_NAME} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#OBJECT_CLASS}.
   **
   ** @return                    the name of the generic object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String objectClassName() {
    return stringValue(DirectoryConstant.OBJECT_CLASS_NAME, DirectoryConstant.OBJECT_CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNameList
  /**
   ** Returns the name of the attributes that are distinguished names in an
   ** entry in a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#DISTINGUISHED_NAMES}.
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
  public final List<String> distinguishedNames() {
    return stringList(DirectoryConstant.DISTINGUISHED_NAMES);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryObjectAttribute
  /**
   ** Returns the name of the object attribute that is a binary attribute
   ** of an entry in a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#BINARY_OBJECT_ATTRIBUTE}.
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
    return stringValue(DirectoryConstant.BINARY_OBJECT_ATTRIBUTE);
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
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#DN}.
   **
   ** @return                    the name of the attribute to detect the
   **                            distinguished name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String distinguishedNameAttribute() {
    return stringValue(DirectoryConstant.DISTINGUISHED_NAME_ATTRIBUTE, DirectoryConstant.DN);
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
   ** the underlying {@link AbstractLookup} this method returns
   ** <code>false</code>.
   **
   ** @return                    <code>true</code> if the distinguished names of
   **                            an LDAP entry has to be handled case sensitive;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final boolean distinguishedNameCaseSensitive() {
    return booleanValue(DirectoryConstant.DISTINGUISHED_NAME_CASESENSITIVE, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectCreatedAttribute
  /**
   ** Returns the name of the attribute to detect the created timestamp of a
   ** LDAP entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTRY_CREATED_ATTRIBUTE}.
   ** <p>
   ** If {@link DirectoryConstant#ENTRY_CREATED_ATTRIBUTE} is not mapped in the
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTRY_CREATED_ATTRIBUTE_DEFAULT}
   **
   ** @return                    the name of the attribute to detect the
   **                            created timestamp.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String objectCreatedAttribute() {
    return stringValue(DirectoryConstant.ENTRY_CREATED_ATTRIBUTE, DirectoryConstant.ENTRY_CREATED_ATTRIBUTE_DEFAULT);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTRY_MODIFIED_ATTRIBUTE_DEFAULT}
   **
   ** @return                    the name of the attribute to detect the
   **                            modified timestamp.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String objectModifiedAttribute() {
    return stringValue(DirectoryConstant.ENTRY_MODIFIED_ATTRIBUTE, DirectoryConstant.ENTRY_MODIFIED_ATTRIBUTE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainObjectClass
  /**
   ** Returns the name of the domain object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#DOMAIN_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#DOMAIN_OBJECT_CLASS} is not mapped in the
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#DOMAIN_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the domain object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String domainObjectClass() {
    return stringValue(DirectoryConstant.DOMAIN_OBJECT_CLASS, DirectoryConstant.DOMAIN_OBJECT_CLASS_DEFAULT);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#DOMAIN_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the domain object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String domainObjectPrefix() {
    return stringValue(DirectoryConstant.DOMAIN_OBJECT_PREFIX, DirectoryConstant.DOMAIN_OBJECT_PREFIX_DEFAULT);
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
   ** @return                    the name of the domain multi-valued attributes.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public final List<String> domainMultiValueAttribute() {
    return stringList(DirectoryConstant.DOMAIN_MULTIVALUE_ATTRIBUTE);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#COUNTRY_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the country object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String countryObjectClass() {
    return stringValue(DirectoryConstant.COUNTRY_OBJECT_CLASS, DirectoryConstant.COUNTRY_OBJECT_CLASS_DEFAULT);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#COUNTRY_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the country object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String countryObjectPrefix() {
    return stringValue(DirectoryConstant.COUNTRY_OBJECT_PREFIX, DirectoryConstant.COUNTRY_OBJECT_PREFIX_DEFAULT);
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
   ** @return                    the name of the country multi-valued attributes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final List<String> countryMultiValueAttribute() {
    return stringList(DirectoryConstant.COUNTRY_MULTIVALUE_ATTRIBUTE);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#LOCALITY_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the locality object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localityObjectClass() {
    return stringValue(DirectoryConstant.LOCALITY_OBJECT_CLASS, DirectoryConstant.LOCALITY_OBJECT_CLASS_DEFAULT);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#LOCALITY_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the locality object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localityObjectPrefix() {
    return stringValue(DirectoryConstant.LOCALITY_OBJECT_PREFIX, DirectoryConstant.LOCALITY_OBJECT_PREFIX_DEFAULT);
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
   ** @return                    the name of the locality multi-valued
   **                            attributes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final List<String> localityMultiValueAttribute() {
    return stringList(DirectoryConstant.LOCALITY_MULTIVALUE_ATTRIBUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleObjectClass
  /**
   ** Returns the name of the role object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ROLE_OBJECT_CLASS}.
   **
   ** @return                    the name of the role object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String roleObjectClass() {
    return stringValue(DirectoryConstant.ROLE_OBJECT_CLASS);
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
   ** underlying {@link AbstractLookup} this method returns <code>null</code>.
   **
   ** @return                    the name of the role object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String roleObjectPrefix() {
    return stringValue(DirectoryConstant.ROLE_OBJECT_PREFIX);
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String roleObjectMemberAttribute() {
    return stringValue(DirectoryConstant.ROLE_OBJECTMEMBER_ATTRIBUTE);
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
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#GROUP_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the group object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupObjectClass() {
    return stringValue(DirectoryConstant.GROUP_OBJECT_CLASS, DirectoryConstant.GROUP_OBJECT_CLASS_DEFAULT);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#GROUP_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the group object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupObjectPrefix() {
    return stringValue(DirectoryConstant.GROUP_OBJECT_PREFIX, DirectoryConstant.GROUP_OBJECT_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupObjectMemberAttribute
  /**
   ** Returns the name of the attribute of a membership of an object within a
   ** group defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#GROUP_OBJECTMEMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the attribute of a membership of an
   **                            object within a group.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupObjectMemberAttribute() {
    return stringValue(DirectoryConstant.GROUP_OBJECTMEMBER_ATTRIBUTE);
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
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ORGANIZATION_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the organization object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationObjectClass() {
    return stringValue(DirectoryConstant.ORGANIZATION_OBJECT_CLASS, DirectoryConstant.ORGANIZATION_OBJECT_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationObjectPrefix
  /**
   ** Returns the name of the organization object prefix defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ORGANIZATION_OBJECT_PREFIX} is not mapped in
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ORGANIZATION_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the organization object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationObjectPrefix() {
    return stringValue(DirectoryConstant.ORGANIZATION_OBJECT_PREFIX, DirectoryConstant.ORGANIZATION_OBJECT_PREFIX_DEFAULT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitObjectClass
  /**
   ** Returns the name of the organization object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATIONALUNIT_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ORGANIZATIONALUNIT_OBJECT_CLASS} is not mapped in
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ORGANIZATIONALUNIT_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the organization object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationalUnitObjectClass() {
    return stringValue(DirectoryConstant.ORGANIZATIONALUNIT_OBJECT_CLASS, DirectoryConstant.ORGANIZATIONALUNIT_OBJECT_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitObjectPrefix
  /**
   ** Returns the name of the organization object prefix defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATIONALUNIT_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ORGANIZATIONALUNIT_OBJECT_PREFIX} is not mapped in
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ORGANIZATIONALUNIT_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the organization object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationalUnitObjectPrefix() {
    return stringValue(DirectoryConstant.ORGANIZATIONALUNIT_OBJECT_PREFIX, DirectoryConstant.ORGANIZATIONALUNIT_OBJECT_PREFIX_DEFAULT);
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
   ** @return                    the name of the organization multi-valued
   **                            attributes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final List<String> organizationMultiValueAttribute() {
    return stringList(DirectoryConstant.ORGANIZATION_MULTIVALUE_ATTRIBUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationRoleMemberAttribute
  /**
   ** Returns the name of the organization role membership attribute defined for
   ** a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_ROLEMEMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the organization role membership
   **                            attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationRoleMemberAttribute() {
    return stringValue(DirectoryConstant.ORGANIZATION_ROLEMEMBER_ATTRIBUTE);
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean organizationRoleMemberAttributeDN() {
    return booleanValue(DirectoryConstant.ORGANIZATION_ROLEMEMBER_ATTRIBUTE_DN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationGroupMemberAttribute
  /**
   ** Returns the name of the organization group membership attribute defined
   ** for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_GROUPMEMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the organization group membership
   **                            attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationGroupMemberAttribute() {
    return stringValue(DirectoryConstant.ORGANIZATION_GROUPMEMBER_ATTRIBUTE);
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean organizationGroupMemberAttributeDN() {
    return booleanValue(DirectoryConstant.ORGANIZATION_GROUPMEMBER_ATTRIBUTE_DN);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ACCOUNT_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the account object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountObjectClass() {
    return stringValue(DirectoryConstant.ACCOUNT_OBJECT_CLASS, DirectoryConstant.ACCOUNT_OBJECT_CLASS_DEFAULT);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ACCOUNT_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the account object prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountObjectPrefix() {
    return stringValue(DirectoryConstant.ACCOUNT_OBJECT_PREFIX, DirectoryConstant.ACCOUNT_OBJECT_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPasswordAttribute
  /**
   ** Returns the name of the account password prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_PASSWORD_ATTRIBUTE}.
   ** <p>
   ** If {@link DirectoryConstant#ACCOUNT_PASSWORD_ATTRIBUTE} is not mapped in
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ACCOUNT_PASSWORD_ATTRIBUTE_DEFAULT}
   **
   ** @return                    the name of the account password prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountPasswordAttribute() {
    return stringValue(DirectoryConstant.ACCOUNT_PASSWORD_ATTRIBUTE, DirectoryConstant.ACCOUNT_PASSWORD_ATTRIBUTE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountMultiValueAttribute
  /**
   ** Returns the {@link List} of names of the account multi-valued attributes
   ** for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_MULTIVALUE_ATTRIBUTE}.
   **
   ** @return                   the name of the account multi-valued attributes.
   */
  public final List<String> accountMultiValueAttribute() {
    return stringList(DirectoryConstant.ACCOUNT_MULTIVALUE_ATTRIBUTE);
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
   **                            attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountRoleMemberAttribute() {
    return stringValue(DirectoryConstant.ACCOUNT_ROLEMEMBER_ATTRIBUTE);
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean accountRoleMemberAttributeDN() {
    return booleanValue(DirectoryConstant.ACCOUNT_ROLEMEMBER_ATTRIBUTE_DN);
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
   **                            attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountGroupMemberAttribute() {
    return stringValue(DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountnGroupMemberAttributeDN
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean accountGroupMemberAttributeDN() {
    return booleanValue(DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE_DN);
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
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ORACLE_CONTEXT_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines the oracle context container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String oracleContextContainer() {
    return stringValue(DirectoryConstant.ORACLE_CONTEXT_CONTAINER, DirectoryConstant.ORACLE_CONTEXT_CONTAINER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oracleProductContainer
  /**
   ** Returns the relative distinguished name from the
   ** <code>Metadata Descriptor</code> that defines the oracle context
   ** container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORACLE_PRODUCT_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#ORACLE_PRODUCT_CONTAINER} is not mapped in the
   ** underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ORACLE_PRODUCT_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines the oracle context container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String oracleProductContainer() {
    return stringValue(DirectoryConstant.ORACLE_PRODUCT_CONTAINER, DirectoryConstant.ORACLE_PRODUCT_CONTAINER_DEFAULT);
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
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_CONTAINER_DEFAULT}.
   **
   ** @return                    the name of the {@link AbstractLookup} that
   **                            defines the database domain container.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDomainContainer() {
    return stringValue(DirectoryConstant.ENTERPRISE_DOMAIN_CONTAINER, DirectoryConstant.ENTERPRISE_DOMAIN_CONTAINER_DEFAULT);
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
   ** mapped in the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the name of the database mapping class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDatabaseClass() {
    return stringValue(DirectoryConstant.ENTERPRISE_DATABASE_OBJECT_CLASS, DirectoryConstant.ENTERPRISE_DATABASE_OBJECT_CLASS_DEFAULT);
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
   ** mapped in the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the database mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDatabasePrefix() {
    return stringValue(DirectoryConstant.ENTERPRISE_DATABASE_OBJECT_PREFIX, DirectoryConstant.ENTERPRISE_DATABASE_OBJECT_PREFIX_DEFAULT);
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
   ** in the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the name of the domain mapping class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDomainClass() {
    return stringValue(DirectoryConstant.ENTERPRISE_DOMAIN_OBJECT_CLASS, DirectoryConstant.ENTERPRISE_DOMAIN_OBJECT_CLASS_DEFAULT);
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
   ** in the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DOMAIN_OBJECT_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the domain mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseDomainPrefix() {
    return stringValue(DirectoryConstant.ENTERPRISE_DOMAIN_OBJECT_PREFIX, DirectoryConstant.ENTERPRISE_DOMAIN_OBJECT_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaClass
  /**
   ** Returns the name of the schema db mapping object class defined for a LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_CLASS} is not mapped
   ** in the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the name of the db schema mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseSchemaClass() {
    return stringValue(DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_CLASS, DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaPrefix
  /**
   ** Returns the name of the schema db mapping prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_PREFIX}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_PREFIX} is not mapped
   ** in the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_OBJECT_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the db schema mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseSchemaPrefix() {
    return stringValue(DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_PREFIX, DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaAccount
  /**
   ** Returns the name of the schema mapping attribute defined for an LDAP
   ** server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_ACCOUNT}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_SCHEMA_ACCOUNT} is not mapped in
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_ACCOUNT_DEFAULT}.
   **
   ** @return                    the name of the schema mapping attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseSchemaAccount() {
    return stringValue(DirectoryConstant.ENTERPRISE_SCHEMA_ACCOUNT, DirectoryConstant.ENTERPRISE_SCHEMA_ACCOUNT_DEFAULT);
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
   ** the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the name of the role mapping class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseRoleClass() {
    return stringValue(DirectoryConstant.ENTERPRISE_ROLE_OBJECT_CLASS, DirectoryConstant.ENTERPRISE_ROLE_OBJECT_CLASS_DEFAULT);
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
   ** in the underlying {@link AbstractLookup} this method returns
   ** {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_PREFIX_DEFAULT}.
   **
   ** @return                    the name of the role mapping prefix.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String enterpriseRolePrefix() {
    return stringValue(DirectoryConstant.ENTERPRISE_ROLE_OBJECT_PREFIX, DirectoryConstant.ENTERPRISE_ROLE_OBJECT_PREFIX_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordOperationSecured
  /**
   ** Returns the <code>true</code> if the LDAP Server requires secure socket
   ** configuration for password operations.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#PASSWORD_OPERATION_SECURED}.
   **
   ** @return                    <code>true</code> the LDAP Server requires
   **                            secure socket configuration for password
   **                            operations; otherwise <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean passwordOperationSecured() {
    return booleanValue(DirectoryConstant.PASSWORD_OPERATION_SECURED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementPrefixRequired
  /**
   ** Returns the <code>true</code> if the entitlements has to be prefixed with
   ** the internal system identifier and the name of the
   ** <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTITLEMENT_PREFIX_REQUIRED}.
   **
   ** @return                    <code>true</code> the entitlements has to be
   **                            prefixed with the internal system identifier
   **                            and the name of the <code>IT Resource</code>;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean entitlementPrefixRequired() {
    return booleanValue(DirectoryConstant.ENTITLEMENT_PREFIX_REQUIRED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleContainer
  /**
   ** Returns the default container of newly created for roles if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ROLE_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            roles if no distinguished name is provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String roleContainer() {
    return stringValue(DirectoryConstant.ROLE_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupContainer
  /**
   ** Returns the default container of newly created for groups if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#GROUP_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            groups if no distinguished name is provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String groupContainer() {
    return stringValue(DirectoryConstant.GROUP_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainContainer
  /**
   ** Returns the default container of newly created for domains if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#COUNTRY_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            domains if no distinguished name is provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String domainContainer() {
    return stringValue(DirectoryConstant.DOMAIN_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countryContainer
  /**
   ** Returns the default container of newly created for countries if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#COUNTRY_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            countries if no distinguished name is provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String countryContainer() {
    return stringValue(DirectoryConstant.COUNTRY_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localityContainer
  /**
   ** Returns the default container of newly created for localities if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#LOCALITY_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            localities if no distinguished name is
   **                            provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localityContainer() {
    return stringValue(DirectoryConstant.LOCALITY_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationContainer
  /**
   ** Returns the default container of newly created for organizations if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATION_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            organizations if no distinguished name is
   **                            provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationContainer() {
    return stringValue(DirectoryConstant.ORGANIZATION_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitContainer
  /**
   ** Returns the default container of newly created for organizations if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ORGANIZATIONALUNIT_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            organizational units if no distinguished name
   **                            is provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String organizationalUnitContainer() {
    return stringValue(DirectoryConstant.ORGANIZATIONALUNIT_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountContainer
  /**
   ** Returns the default container of newly created for accounts if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ACCOUNT_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            accounts if no distinguished name is provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountContainer() {
    return stringValue(DirectoryConstant.ACCOUNT_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   genericContainer
  /**
   ** Returns the default container of newly created for generics if no
   ** distinguished name is provided.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#GENERIC_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            generics if no distinguished name is provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String genericContainer() {
    return stringValue(DirectoryConstant.GENERIC_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DirectoryFeature</code> which is
   ** associated with the specified task.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this {@link AbstractLookup} configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an empty <code>IT Resource</code> feature
   **                            instance wrapped as
   **                            <code>DirectoryFeature</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryFeature</code>.
   */
  public static DirectoryFeature build(final Loggable loggable) {
    return new DirectoryFeature(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryFeature</code> which is
   ** associated with the specified task and populated from the specified
   ** <code>instanceName</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this {@link AbstractLookup} configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instanceName       the name of the {@link AbstractLookup}
   **                            instance where this configuration wrapper will
   **                            obtains the values.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a <code>IT Resource</code> feature instance
   **                            wrapped as <code>DirectoryFeature</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryFeature</code>.
   **
   ** @throws TaskException      if the {@link AbstractLookup} is not defined in
   **                            the Oracle Identity Manager metadata entries.
   */
  public static DirectoryFeature build(final Loggable loggable, final String instanceName)
    throws TaskException {

    return new DirectoryFeature(loggable, instanceName);
  }
}