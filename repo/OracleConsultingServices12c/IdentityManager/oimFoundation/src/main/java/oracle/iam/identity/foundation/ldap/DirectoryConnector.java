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

    File        :   DirectoryConnector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryConnector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Date;
import java.util.Calendar;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.FileInputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.net.UnknownHostException;
import java.net.URLEncoder;
import java.net.URLDecoder;

import java.security.Security;
import java.security.Provider;

import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import javax.naming.NamingException;
import javax.naming.CommunicationException;
import javax.naming.AuthenticationException;
import javax.naming.ServiceUnavailableException;
import javax.naming.OperationNotSupportedException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.InvalidNameException;
import javax.naming.Context;
import javax.naming.CompositeName;
import javax.naming.NamingEnumeration;

import javax.naming.directory.SchemaViolationException;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.naming.directory.AttributeInUseException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import javax.naming.ldap.LdapContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.Control;
import javax.naming.ldap.SortControl;

import com.sun.jndi.ldap.ctl.TreeDeleteControl;
import com.sun.jndi.ldap.ctl.PagedResultsResponseControl;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.ReferenceException;
import oracle.mds.naming.DocumentName;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.logging.TableFormatter;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLoggable;
import oracle.iam.identity.foundation.AbstractMetadataTask;

import oracle.iam.identity.foundation.resource.TaskBundle;
import oracle.iam.identity.foundation.resource.DirectoryBundle;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryConnector
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryConnector</code> implements the base functionality
 ** of an Oracle Identity Manager Connector for a Generic Directory Service.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class DirectoryConnector extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** factory used to create the secure socket layer */
  private static SSLSocketFactory     socketFactory   = null;

  private static Map<String, Integer> scope           = new HashMap<String, Integer>(3);

  private static Date                 zero            = null;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    scope.put(DirectoryConstant.SCOPE_OBJECT,   new Integer(SearchControls.OBJECT_SCOPE));
    scope.put(DirectoryConstant.SCOPE_ONELEVEL, new Integer(SearchControls.ONELEVEL_SCOPE));
    scope.put(DirectoryConstant.SCOPE_SUBTREE,  new Integer(SearchControls.SUBTREE_SCOPE));

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(1900,0,1,0,0,0);
    // should be result in a string of 19000101000000.0Z
    zero = calendar.getTime();
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final boolean          secureSocket;
  protected final String           serverName;
  protected final int              serverPort;
  protected final String           rootContext;
  protected final boolean          relativeDN;
  protected final String           principalName;
  protected final String           principalPassword;
  protected final String           localeLanguage;
  protected final String           localeCountry;
  protected final String           localeTimeZone;

  /**
   ** the wrapper of target specific features where this connector is attached
   ** to
   */
  protected final DirectoryFeature feature;

  private String[]                 serviceURL = null;
  private LdapContext              context    = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryConnector</code> which is associated with the
   ** specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  resource           the {@link DirectoryResource} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public DirectoryConnector(final AbstractMetadataTask task, final DirectoryResource resource)
    throws TaskException {

    this(task, resource.serverName(), resource.serverPort(), resource.rootContext(), resource.principalName(), resource.principalPassword(), resource.secureSocket(), resource.distinguishedNameRelative(), resource.localeLanguage(), resource.localeCountry(), resource.localeTimeZone(), resource.feature());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryConnector</code> which is associated with the
   ** specified task.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  resource           the {@link DirectoryResource} IT Resource
   **                            definition where this connector is associated
   **                            with.
   ** @param  feature            the Metadata Descriptor providing the target
   **                            system specific features like objectClasses,
   **                            attribute id's etc.
   */
  public DirectoryConnector(final Loggable loggable, final DirectoryResource resource, final DirectoryFeature feature) {
    this(loggable, resource.serverName(), resource.serverPort(), resource.rootContext(), resource.principalName(), resource.principalPassword(), resource.secureSocket(), resource.distinguishedNameRelative(), resource.localeLanguage(), resource.localeCountry(), resource.localeTimeZone(), feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryConnector</code> task adapter.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
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
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   ** @param  localeTimeZone     use this parameter to specify the time zone of
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
  public DirectoryConnector(final AbstractMetadataTask task, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    this(task, serverName, Integer.parseInt(serverPort), rootContext, principalName, principalPassword, secureSocket, relativeDN, localeLanguage, localeCountry, localeTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryConnector</code> task adapter.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
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
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   ** @param  localeTimeZone     use this parameter to specify the time zone of
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
  public DirectoryConnector(final AbstractMetadataTask task, final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    this(task, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, localeLanguage, localeCountry, localeTimeZone, unmarshal(task, feature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryConnector</code> task adapter.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this {@link AbstractLoggable}.
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
   **                            <b>Note</b>:
   **                            If <code>null</code> or an empty string is
   **                            passed for <code>rootContext</code> this
   **                            parameter will be ignored and all pathes will
   **                            be treated as absolute.
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   ** @param  localeTimeZone     use this parameter to specify the time zone of
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
   ** @param  features           the {@link DirectoryFeature} providing the
   **                            target system specific features like
   **                            objectClasses, attribute id's etc.
   */
  public DirectoryConnector(final Loggable loggable, final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String localeLanguage, final String localeCountry, final String localeTimeZone, final DirectoryFeature features) {
    // ensure inheritance
    super(loggable);

    this.serverName        = serverName;
    this.serverPort        = serverPort;
    this.rootContext       = rootContext;
    this.relativeDN        = StringUtility.isEmpty(this.rootContext) ? false : relativeDN;
    this.principalName     = principalName;
    this.principalPassword = principalPassword;
    this.secureSocket      = secureSocket;
    this.localeLanguage    = localeLanguage;
    this.localeCountry     = localeCountry;
    this.localeTimeZone    = localeTimeZone;

    // create the property mapping for the LDAP control
    this.feature           = features;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchScope
  /**
   ** Returns the predefined search scope for the given name.
   **
   ** @param  searchScope        the scope of search to obtain for the
   **                            predefined objects.
   **
   ** @return                    the predefined search scope for the given name.
   */
  public static final SearchControls searchScope(final String searchScope) {
    final Integer controlCode = scope.get(searchScope);
    return searchScope(controlCode.intValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchScope
  /**
   ** Returns the predefined search scope for the given enum.
   **
   ** @param  searchScope        the scope of search to obtain for the
   **                            predefined objects.
   **
   ** @return                    the predefined search scope for the given name.
   */
  public static final SearchControls searchScope(final int searchScope) {
    return new SearchControls(searchScope, 0, 0, null, false, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the LDAP Server used to connect to.
   **
   ** @return                    name of the LDAP Server used to connect to.
   */
  public final String serverName() {
    return this.serverName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the port of the LDAP Server used to connect to.
   **
   ** @return                    port of the LDAP Server used to connect to.
   */
  public final int serverPort() {
    return this.serverPort;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns whether the connection to the LDAP Server is secured by SSL.
   **
   ** @return                    <code>true</code> if the connection to the
   **                            LDAP Server is secured by SSL,
   **                            <code>false</code> otherwise.
   */
  public final boolean secureSocket() {
    return this.secureSocket;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Returns the root context of the LDAP Server used to connect to.
   **
   ** @return                    root context of the LDAP Server used to connect
   **                            to.
   */
  public final String rootContext() {
    return this.rootContext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNameRelative
  /**
   ** Returns whether all pathes are treated as relative to the naming context
   ** of the connected LDAP Server.
   ** Context.
   **
   ** @return                    <code>true</code> if all pathes are treated as
   **                            relative in the connectec LDAP Server.
   */
  public final boolean isDistinguishedNameRelative() {
    return this.relativeDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the name of the security principal of the LDAP Server used to
   ** connect to.
   **
   ** @return                    the name of the security principal LDAP Server
   **                            used to connect to.
   */
  public final String principalName() {
    return this.principalName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password of the security principal of the LDAP Server used to
   ** connect to.
   **
   ** @return                    the password of the security principal LDAP
   **                            Server used to connect to.
   */
  public final String principalPassword() {
    return this.principalPassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeCountry
  /**
   ** Returns the country code of the LDAP Server used to connect to.
   **
   ** @return                    the country code of the LDAP Server used to
   **                            connect to.
   */
  public final String localeCountry() {
    return this.localeCountry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeLanguage
  /**
   ** Returns the language code of the LDAP Server used to connect to.
   **
   ** @return                    the language code of the LDAP Server used to
   **                            connect to.
   */
  public final String localeLanguage() {
    return this.localeLanguage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeTimeZone
  /**
   ** Returns the time zone of the LDAP Server used to connect to.
   **
   ** @return                    the time tone of the LDAP Server used to
   **                            connect to.
   */
  public final String localeTimeZone() {
    return this.localeTimeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   feature
  /**
   ** Returns feature mapping of the connector.
   **
   ** @return                    feature mapping of the connector.
   */
  public DirectoryFeature feature() {
    return this.feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the URL encoding.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#URL_ENCODING}.
   ** <p>
   ** If {@link DirectoryConstant#URL_ENCODING} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#URL_ENCODING_DEFAULT}
   **
   ** @return                    the URL encoding.
   */
  public final String urlEncoding() {
    return this.feature.urlEncoding();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialContextFactory
  /**
   ** Returns the class name of the initial context factory.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#INITIAL_CONTEXT_FACTORY}.
   ** <p>
   ** If {@link DirectoryConstant#INITIAL_CONTEXT_FACTORY} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#INITIAL_CONTEXT_FACTORY_DEFAULT}
   **
   ** @return                    the class name of the initial context factory.
   */
  public final String initialContextFactory() {
    return this.feature.initialContextFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityProvider
  /**
   ** Returns the class name of the security provider implementation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#SECURITY_PROVIDER}.
   ** <p>
   ** If {@link DirectoryConstant#SECURITY_PROVIDER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#SECURITY_PROVIDER_DEFAULT}
   **
   ** @return                    the class name of the security provider
   **                            implementation.
   */
  public final String securityProvider() {
    return this.feature.securityProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator sign for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link DirectoryConstant#MULTIVALUE_SEPARATOR} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#MULTIVALUE_SEPARATOR_DEFAULT}
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String multiValueSeparator() {
    return this.feature.multiValueSeparator();
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
   ** If {@link DirectoryConstant#SCHEMA_CONTAINER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#SCHEMA_CONTAINER_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the schema container.
   */
  public final String schemaContainer() {
    return this.feature.schemaContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogContainer
  /**
   ** Returns the distinguished name from the <code>Metadata Descriptor</code>
   ** that defines the catalog container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CATALOG_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#CATALOG_CONTAINER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#CATALOG_CONTAINER_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the catalog container.
   */
  public final String catalogContainer() {
    return this.feature.catalogContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogContainer
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** change log container.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CHANGELOG_CONTAINER}.
   ** <p>
   ** If {@link DirectoryConstant#CHANGELOG_CONTAINER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#CHANGELOG_CONTAINER_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the change log container.
   */
  public final String changelogContainer() {
    return this.feature.changelogContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogChangeType
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> that defines the
   ** attribute name type of changelog entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#CHANGELOG_CHANGETYPE}.
   ** <p>
   ** If {@link DirectoryConstant#CHANGELOG_CHANGETYPE} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#CHANGE_TYPE}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines attribute name type of changelog
   **                            entry.
   */
  public final String changelogChangeType() {
    return this.feature.changelogChangeType();
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
   ** If {@link DirectoryConstant#CHANGELOG_CHANGENUMBER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#CHANGE_NUMBER}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines attribute name of the change
   **                            number within a changelog entry.
   */
  public final String changelogChangeNumber() {
    return this.feature.changelogChangeNumber();
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
   ** If {@link DirectoryConstant#CHANGELOG_TARGETGUID} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#CHANGELOG_TARGETGUID_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines attribute name of the change
   **                            number within a changelog entry.
   */
  public final String changelogTargetGUID() {
    return this.feature.changelogTargetGUID();
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
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#CHANGELOG_TARGETDN_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines attribute name that provides the
   **                            distinguished name of the target entry
   ** within a changelog entry.
   */
  public final String changelogTargteDN() {
    return this.feature.changelogTargteDN();
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
   ** If {@link DirectoryConstant#CHANGESTATUS_CONTAINER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#CHANGESTATUS_CONTAINER_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the change status container.
   */
  public final String changestatusContainer() {
    return this.feature.changestatusContainer();
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
   ** If {@link DirectoryConstant#CONNECTION_TIMEOUT} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#CONNECTION_TIMEOUT_DEFAULT}
   **
   ** @return                    the timeout period for establishment of the
   **                            LDAP connection.
   */
  public final String connectionTimeout() {
    return this.feature.connectionTimeout();
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
   */
  public final String responseTimeout() {
    return this.feature.responseTimeout();
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
   ** If {@link DirectoryConstant#REFERENTIAL_INTEGRITY} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#REFERENTIAL_INTEGRITY_DEFAULT}
   **
   ** @return                    <code>true</code> if referential integrity is
   **                            enabled in target Directory Service; otherwise
   **                            <code>false</code>.
   */
  public final boolean referentialIntegrity() {
    return this.feature.referentialIntegrity();
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
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#TIMESTAMP_FORMAT_DEFAULT}
   **
   ** @return                    the format of a timestamp value in target
   **                            Directory Service.
   */
  public final String timestampFormat() {
    return this.feature.timestampFormat();
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
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the failover configuration.
   */
  public final List<DirectoryServer> failoverConfiguration() {
    return this.feature.failoverConfiguration();
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
   ** If {@link DirectoryConstant#PAGINATION_CONTROL} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#PAGINATION_CONTROL_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the pagination control.
   */
  public final String paginationControl() {
    return this.feature.paginationControl();
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
   ** If {@link DirectoryConstant#PAGINATION_CONTROL_CRITICAL} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link Control#CRITICAL}
   **
   ** @return                    <code>true</code> if the PaginationControl
   **                            defined in the <code>Metadata Descriptor</code>
   **                            is treated as a critical control extension;
   **                            <code>false</code> otherwise.
   */
  public final boolean paginationControlCritical() {
    return this.feature.paginationControlCritical();
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
    return this.feature.objectClassName();
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
    return this.feature.binaryObjectAttribute();
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
    return this.feature.distinguishedNameAttribute();
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
    return this.feature.distinguishedNameCaseSensitive();
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
    return this.feature.objectCreatedAttribute();
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
    return this.feature.objectModifiedAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainObjectClass
  /**
   ** Returns the name of the domain object class defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#DOMAIN_OBJECT_CLASS}.
   ** <p>
   ** If {@link DirectoryConstant#DOMAIN_OBJECT_PREFIX} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#DOMAIN_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the domain object class.
   */
  public final String domainObjectClass() {
    return this.feature.domainObjectClass();
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
    return this.feature.domainObjectPrefix();
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
    return this.feature.domainMultiValueAttribute();
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
    return this.feature.countryObjectClass();
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
    return this.feature.countryObjectPrefix();
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
    return this.feature.countryMultiValueAttribute();
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
   return this.feature.localityObjectClass();
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
    return this.feature.localityObjectPrefix();
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
    return this.feature.localityMultiValueAttribute();
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
   return this.feature.roleObjectClass();
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
    return this.feature.roleObjectPrefix();
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
    return this.feature.roleObjectMemberAttribute();
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
    return this.feature.groupObjectClass();
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
    return this.feature.groupObjectPrefix();
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
    return this.feature.groupObjectMemberAttribute();
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
    return this.feature.organizationObjectClass();
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
    return this.feature.organizationObjectPrefix();
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
    return this.feature.organizationMultiValueAttribute();
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
    return this.feature.organizationRoleMemberAttribute();
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
    return this.feature.organizationRoleMemberAttributeDN();
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
    return this.feature.organizationGroupMemberAttribute();
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
    return this.feature.organizationGroupMemberAttributeDN();
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
    return this.feature.accountObjectClass();
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
    return this.feature.accountObjectPrefix();
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
    return this.feature.accountPasswordAttribute();
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
    return this.feature.accountMultiValueAttribute();
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
    return this.feature.accountRoleMemberAttribute();
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
    return this.feature.accountRoleMemberAttributeDN();
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
    return this.feature.accountGroupMemberAttribute();
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
    return this.feature.accountGroupMemberAttributeDN();
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
    return this.feature.oracleContextContainer();
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
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ORACLE_PRODUCT_CONTAINER_DEFAULT}
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            that defines the oracle context container.
   */
  public final String oracleProductContainer() {
    return this.feature.oracleProductContainer();
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
    return this.feature.enterpriseDomainContainer();
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
   ** If {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_CLASS} is not mapped
   ** in the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_DATABASE_OBJECT_CLASS_DEFAULT}
   **
   ** @return                    the name of the database mapping class.
   */
  public final String enterpriseDatabaseClass() {
    return this.feature.enterpriseDatabaseClass();
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
    return this.feature.enterpriseDatabasePrefix();
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
    return this.feature.enterpriseDomainClass();
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
    return this.feature.enterpriseDomainPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaClass
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
    return this.feature.enterpriseSchemaClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaPrefix
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
    return this.feature.enterpriseSchemaPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseSchemaAccount
  /**
   ** Returns the name of the schema mapping prefix defined for a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_ACCOUNT}.
   ** <p>
   ** If {@link DirectoryConstant#ENTERPRISE_SCHEMA_ACCOUNT} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_SCHEMA_ACCOUNT_DEFAULT}
   **
   ** @return                    the name of the schema mapping prefix.
   */
  public final String enterpriseSchemaAccount() {
    return this.feature.enterpriseSchemaAccount();
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
    return this.feature.enterpriseRoleClass();
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
   ** If {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_PREFIX} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryConstant#ENTERPRISE_ROLE_OBJECT_PREFIX_DEFAULT}
   **
   ** @return                    the name of the role mapping prefix.
   */
  public final String enterpriseRolePrefix() {
    return this.feature.enterpriseRolePrefix();
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
    return this.feature.passwordOperationSecured();
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
   */
  public final boolean entitlementPrefixRequired() {
    return this.feature.entitlementPrefixRequired();
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
    return this.feature.roleContainer();
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
    return this.feature.groupContainer();
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
    return this.feature.countryContainer();
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
    return this.feature.accountContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   genericContainer
  /**
   ** Returns the default container of newly created for generics if no
   ** distinguished name is provided a LDAP server.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryConstant#GENERIC_CONTAINER}.
   **
   ** @return                    the default container of newly created
   **                            generics if no distinguished name is provided.
   */
  public final String genericContainer() {
    return this.feature.genericContainer();
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
    return this.feature.localityContainer();
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
    return this.feature.organizationContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abtract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (AbstractLoggable)
  /**
   ** Writes this mapping to the associated logger
   **
   ** @param method              the name of the implementing method where the
   **                            request originates to.
   */
//  @Override
  public void debug(final String method) {
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel())
      debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.toString()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexString
  /**
   ** Convert the given raw specified byte array to a String in hex format
   **
   ** @param  bytes              the raw data
   **
   ** @return                    the String converted from the given binary.
   */
  public static String hexString(byte[] bytes) {
    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < bytes.length; ++i)
      buffer.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));

    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeContext
  /**
   ** Releases a {@link Context}'s resources immediately, instead of waiting
   ** for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent: invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param context             the {@link Context} to close.
   **
   ** @throws DirectoryException in the specified context cannot be closed.
   */
  public static void closeContext(final Context context)
    throws DirectoryException {

    try {
      if (context != null) {
        context.close();
      }
    }
    catch (NamingException e) {
      throw new DirectoryException(DirectoryError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeContext
  /**
   ** Releases a {@link NamingEnumeration}'s resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent: invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param context             the {@link NamingEnumeration} to close.
   **
   ** @throws DirectoryException in the specified context cannot be closed.
   */
  public static void closeContext(final NamingEnumeration<?> context)
    throws DirectoryException {

    try {
      if (context != null)
        context.close();
    }
    catch (NamingException e) {
      throw new DirectoryException(DirectoryError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superiorDN
  /**
   ** Returns the individual components of the superior of a distinguished name
   ** (DN).
   ** <p>
   ** The returned hierarchy is a {@link List} of 2-element string arrays.
   ** <p>
   ** <b>Important</b>:
   ** <br>
   ** Remember, that since the distinguished name was probably retrieved through
   ** a search which returns RDNs (like getUsers, getGroup, etc), to get the
   ** hierarchy relative to the root context, you must pass in as the second
   ** parameter the hierarchy below which the, search was done.
   **
   ** @param distinguishedName   distinguished name of which you want to get the
   **                            components in the format
   **                            "cn=Admin,ou=Groups,dc=pxed,dc=pfizer,dc=com,o=PXED-DEV"
   **                            <br>
   **                            <b>Important</b>:
   **                            <br>
   **                            deepest first
   **
   ** @return                    a {@link List} of String arrays, where each
   **                            element is ["object , class", "object name"],
   **                            e.g. ["ou", "Oracle Consulting Services"]
   */
  public static String superiorDN(final String distinguishedName) {

    List<String[]> result = DirectoryName.explode(distinguishedName, null);
    if (!result.isEmpty())
      result.remove(0);

    return DirectoryName.compose(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureParentRDN
  /**
   ** Ensures that a valid container RDN is used by operations
   *
   ** @param  parentRDN         the name of the DN which is selected in the
   **                           process data form.
   **                           <br>
   **                           It can be either empty or the OU name selected.
   ** @param  defaultContainer  the container RDN returned if
   **                           <code>parentRDN</code> isn't valid,
   **
   ** @return                   a valid rdn
   */
  public static String ensureContainerRDN(final String parentRDN, final String defaultContainer) {
    // If rdn is empty then pass cn=users as hierarchy name
    return (StringUtility.isEmpty(parentRDN)) ? defaultContainer : parentRDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryRDN
  /**
   ** Returns the first RDN from the specified distinguished name.
   **
   ** @param distinguishedName   string in the format
   **                            "cn=Admin,ou=Groups,dc=pxed,dc=pfizer,dc=com,o=PXED-DEV"
   **
   ** @return                    String "cn=Admin"
   */
  public static String entryRDN(final String distinguishedName) {
    String entryRDN = null;
    int firstComponent  = distinguishedName.indexOf(SystemConstant.EQUAL);
    if (firstComponent == -1) {
      // error parsing the distinguishedName. returning as it is
      entryRDN = distinguishedName;
    }
    else {
      int secondComponent = distinguishedName.indexOf(SystemConstant.EQUAL, firstComponent + 1);
      if (secondComponent == -1)
        entryRDN = distinguishedName;
      else {
        int endIndex = distinguishedName.lastIndexOf(SystemConstant.COMMA, secondComponent);
        entryRDN = distinguishedName.substring(0, endIndex);
      }
    }
    return entryRDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeName
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   ** @param  value              the value
   **
   ** @return                    the composed name
   */
  public static String composeName(final String prefix, final String value) {
    String[] parameter = { prefix, DirectoryName.escape(value) };
    return composeName(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeName
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  component          the attribute prefix, e.g <code>cn</code> and
   **                            the value
   **
   ** @return                    the composed name
   */
  public static String composeName(final Object[] component) {
    return String.format("%s=%s", component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeFilter
  /**
   ** This method gives the filter condition value for the given prefix and
   ** value string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   ** @param  value              the value
   **
   ** @return                    the filter condition
   */
  public static String composeFilter(final String prefix, final String value) {
    return composeFilter(prefix, "=", value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeFilter
  /**
   ** This method gives the filter condition value for the given prefix and
   ** value string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   ** @param  predicate          the ....
   ** @param  value              the value.
   **
   ** @return                    the composed filter condition embraced in
   **                            brackets.
   */
  public static String composeFilter(final String prefix, final String predicate, final String value) {
    return String.format("(%s%s%s)", prefix, predicate, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttributes
  /**
   ** Creates {@link Attributes} object for a given {@link Map} containing key,
   ** values.
   ** <p>
   ** This method create the attributes only for mapping entries that has
   ** non-empty values.
   **
   ** @param  mapping            the {@link Map} containing the name to value
   **                            attribute mapping.
   **
   ** @return                    the {@link Attributes} object created from the
   **                            given {@link Map}.
   **
   ** @throws DirectoryException if the operation fails.
   */
  public static Attributes createAttributes(final Map<String, Object> mapping)
    throws DirectoryException {

    // avoid bogus input
    if (mapping == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "mapping");

    Attributes attributes = new BasicAttributes(false);
    for (String name : mapping.keySet()) {
      final String value = (String)mapping.get(name);
      // we have to decide if we put a value in the attribute or not
      // this is necessary to prevent the delivery of attributes to the
      // backend server that don't have a value
      if (!StringUtility.isEmpty(value))
        attributes.put(new BasicAttribute(name, value));
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttributes
  /**
   ** Creates the container of attributes that contains the data provision to
   ** the target system by extracting the process data.
   ** <p>
   ** A filter is applied to extract only particular field from the process
   ** data.
   **
   ** @param  mapping            the {@link Map} containing the name to value
   **                            attribute mapping.
   ** @param  filter             the names of the attributes where the callee is
   **                            interrested in.
   **                            <br>
   **                            This is used a filter to pick up only special
   **                            fields from the  attribute mapping.
   **
   ** @return                    the {@link Attributes} created from the
   **                            provided <code>mapping</code>.
   **
   **
   ** @throws DirectoryException if the operation fails.
   */
  public static Attributes createAttributes(final Map<String, Object> mapping, final List<String> filter)
    throws DirectoryException {

    // avoid bogus input
    if (mapping == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "mapping");

    if (filter == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "filter");

    Attributes attributes = new BasicAttributes(false);
    for (String id : mapping.keySet()) {
      if (filter.contains(id)) {
        final String value = (String)mapping.get(id);
        // we have to decide if we put a value in the attribute or not
        // this is necessary to allow a clean delete of the attribute if it's
        // empty on certain LDAP servers that checks the syntax of an attribute
        // value in a delete operation also
        if (StringUtility.isEmpty(value))
          attributes.put(new BasicAttribute(id));
        else
          attributes.put(new BasicAttribute(id, value));
      }
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isSortControlSupported
  /**
   ** Determines if the LDAP Server supports tree deletion
   **
   ** @param  context            the {@link LdapContext} to check for support
   **
   ** @return                    <code>true</code> the LDAP Server supports the
   **                            requested operation; <code>false</code>
   **                            otherwise.
   ** @throws DirectoryException if the operation to check the supported
   **                            {@link Control}s fails.
   */
  public boolean isSortControlSupported(final LdapContext context)
    throws DirectoryException {

    return isControlSupported(context, SortControl.OID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isTreeDeleteControlSupported
  /**
   ** Determines if the LDAP Server supports tree deletion
   **
   ** @param  context            the {@link LdapContext} to check for support
   **
   ** @return                    <code>true</code> the LDAP Server supports the
   **                            requested operation; <code>false</code>
   **                            otherwise.
   ** @throws DirectoryException if the operation to check the supported
   **                            {@link Control}s fails.
   */
  public boolean isTreeDeleteControlSupported(final LdapContext context)
    throws DirectoryException {

    return isControlSupported(context, TreeDeleteControl.OID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isTreeDeleteControlSupported
  /**
   ** Determines if the LDAP Server supports tree deletion
   **
   ** @param  context            the {@link LdapContext} to check for support
   **
   ** @return                    <code>true</code> the LDAP Server supports the
   **                            requested operation; <code>false</code>
   **                            otherwise.
   ** @throws DirectoryException if the operation to check the supported
   **                            {@link Control}s fails.
   */
  private boolean isControlSupported(final LdapContext context, final String OID)
    throws DirectoryException {

    SearchControls searchcontrols = new SearchControls();
    searchcontrols.setReturningAttributes(new String[] { "supportedControl" });
    searchcontrols.setSearchScope(SearchControls.OBJECT_SCOPE);

    boolean                         supported = false;
    NamingEnumeration<SearchResult> control   = null;
    try {
      control = context.search(SystemConstant.EMPTY, composeFilter(objectClassName(), "*"), searchcontrols);
      while (!supported && control.hasMore()) {
        SearchResult searchresult = control.next();
        NamingEnumeration<? extends Attribute> all = searchresult.getAttributes().getAll();
        while (!supported && all.hasMore()) {
          Attribute         attribute = all.next();
          NamingEnumeration<?> id        = attribute.getAll();
          while (!supported && id.hasMore()) {
            if (OID.equals(id.next()))
              supported = true;
          }
        }
      }
    }
    catch (NamingException e) {
      throw new DirectoryException(DirectoryError.UNHANDLED, e);
    }
    finally {
      closeContext(control);
    }
    return supported;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogFilter
  /**
   ** Builds a filter that can by applied on a search in the change log.
   **
   ** @param  lastChangeNumber   the change number all changes will be retrievd
   **                            from the target system that are greater.
   ** @param  deleteChangesOnly  <code>true</code> if only deletes should be
   **                            returned by the search.
   **
   ** @return                    a {@link String} with the search filter
   **                            criteria.
   */
  public String changelogFilter(final String lastChangeNumber, final boolean deleteChangesOnly) {
    StringBuilder searchFilter = new StringBuilder();
    searchFilter.append("(&");
    searchFilter.append("(!(").append(composeFilter(changelogChangeNumber(), "=", lastChangeNumber)).append("))");
    searchFilter.append("(").append(composeFilter(changelogChangeNumber(), ">=", lastChangeNumber)).append(")");

    if (deleteChangesOnly == true)
      searchFilter.append("(").append(composeFilter(changelogChangeType(), "=", DirectoryConstant.CHANGE_TYPE_DELETE)).append(")");
    else
      searchFilter.append("(!(").append(composeFilter(changelogChangeType(), "=", DirectoryConstant.CHANGE_TYPE_DELETE)).append("))");
    searchFilter.append(")");

    return searchFilter.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                   the string representation of this instance.
   */
  @Override
  public String toString() {
    final TableFormatter table = new TableFormatter()
    .header(SystemBundle.string(SystemMessage.PROPERTY_NAME))
    .header(SystemBundle.string(SystemMessage.PROPERTY_VALUE))
    ;
    StringUtility.formatValuePair(table, DirectoryResource.SERVER_NAME,                 this.serverName());
    StringUtility.formatValuePair(table, DirectoryResource.SERVER_PORT,                 this.serverPort());
    StringUtility.formatValuePair(table, DirectoryResource.ROOT_CONTEXT,                this.rootContext());
    StringUtility.formatValuePair(table, DirectoryResource.PRINCIPAL_NAME,              this.principalName());
    StringUtility.formatValuePair(table, DirectoryResource.PRINCIPAL_PASSWORD,          this.principalPassword());
    StringUtility.formatValuePair(table, DirectoryResource.SECURE_SOCKET,               this.secureSocket());
    StringUtility.formatValuePair(table, DirectoryResource.DISTINGUISHED_NAME_RELATIVE, this.isDistinguishedNameRelative());
    StringUtility.formatValuePair(table, DirectoryResource.LOCALE_COUNTRY,              this.localeCountry());
    StringUtility.formatValuePair(table, DirectoryResource.LOCALE_LANGUAGE,             this.localeLanguage());
    StringUtility.formatValuePair(table, DirectoryResource.LOCALE_TIMEZONE,             this.localeTimeZone());

    StringBuilder builder = new StringBuilder();
    table.print(builder);
    builder.append(this.feature.toString());
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchServerProperties
  /**
   ** Searches LDAP for the RootDSE object and get the value of a  particular
   ** property from it
   **
   ** @param  attributeNames     the attributes whose values have to be
   **                            returned.
   **                            Set it to null, if all attribute values have to
   **                            be returned.
   **
   ** @return                    the {@link List} containing the names of the
   **                            LDAP objects find for the specified filter.
   **
   ** @throws DirectoryException in case the search operation cannot be
   **                            performed.
   */
  public List<SearchResult> fetchServerProperties(final String[] attributeNames)
    throws DirectoryException {

    SearchControls controls = new SearchControls();
    // the search will not cross naming system boundaries.
    controls.setSearchScope(SearchControls.OBJECT_SCOPE);
    if (attributeNames != null)
      controls.setReturningAttributes(attributeNames);

    NamingEnumeration<SearchResult> names   = null;
    LdapContext                     context =  null;
    try {
      context = connectDSE();
      // The NamingEnumeration that results from context.search() using
      // OBJECT_SCOPE contains elements of objects from the object (including
      // the named context) that satisfy the search filter specified in
      // context.search().
      // The names of elements in the NamingEnumeration are either relative to
      // the named context or is a URL string.
      // If the named context satisfies the search filter, it is included in the
      // enumeration with the empty string as its name.
      names = context.search(SystemConstant.EMPTY, composeFilter(objectClassName(), "*"), controls);

      // seems to be we are ok; transform the SearchResult contained in the
      // NamingEnumeration in a list of strings to return this list to the caller
      List<SearchResult> result = new ArrayList<SearchResult>();
      while(names.hasMoreElements()) {
        final SearchResult element = names.nextElement();
        result.add(element);
      }
      return result;
    }
    catch (NamingException e) {
      throw new DirectoryException(DirectoryError.UNHANDLED, e);
    }
    finally {
      closeContext(names);
      closeContext(context);
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
   ** <p>
   ** This version of retrieving the context URl returns always the context root
   ** configured in the IT Resource definition associated with this instance.
   **
   ** @return                    the context URl the context root configured in
   **                            the IT Resource definition associated with this
   **                            instance.
   **
   ** @throws DirectoryException if the given distinguished name cannot
   **                            converted to a <code>application/x-www-form-urlencoded</code>
   **                            MIME format.
   */
  public final String contextURL()
    throws DirectoryException {

    return contextURL(SystemConstant.EMPTY);
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
   **
   ** @return                    the full qualified LDAP URL.
   **
   ** @throws DirectoryException if the given distinguished name cannot
   **                            converted to a <code>application/x-www-form-urlencoded</code>
   **                            MIME format.
   */
  public final String contextURL(final String distinguishedName)
    throws DirectoryException {

    // encode the distinguished name that we want to access with the restriction
    // that the URLEncoder implements the HTML Specifications for how to encode
    // URLs in HTML forms. This fails for LDAP URL's so we have to replace all
    // plus signs back to a space manually after the encoder return the result
    String encodedPath = null;
    try {
      encodedPath = URLEncoder.encode(denormalizePath(distinguishedName), urlEncoding());
      encodedPath = encodedPath.replace("+", "%20");
    }
    catch (UnsupportedEncodingException e) {
      throw new DirectoryException(DirectoryError.CONNECTION_ENCODING_NOTSUPPORTED, urlEncoding());
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
  // Method:   denormalizePath
  /**
   ** Forms the basis of building the hierarchical tree to the LDAP object.
   ** <p>
   ** Used by connect to build the correct connection to an entry by returning
   ** a fullqualified distinguished name (absolute path) to the entry.
   **
   ** @param  context            Contains the elements in the tree, deepest one
   **                            first. The String must be of format
   **                            "Class Type=Object CN,Class Type=Object CN"
   **                            where:
   **                            <ul>
   **                              <li>Class Type is the objects class type ("CN", "OU", ...)
   **                              <li>Object CN is the LDAP objects common name ("dsteding", "finance group", ... )
   **                            </ul>
   **                            Basically whatever is assigned to the
   **                            mandatory property "cn" or "ou". e.g.
   **                            <code>CN=Dumbo,OU=Leaders,OU=Elephants</code>
   **
   ** @return                    String of the canonical path (including the
   **                            root context), e.g.
   **                            OU=Users,OU=abc,OU=Companies,DC=oracle,DC=com
   */
  public String denormalizePath(final String context) {
    StringBuilder path = new StringBuilder();
    // if context is empty or null
    if (StringUtility.isEmpty(context))
      path.append(this.rootContext());
    else {
      if (this.relativeDN) {
        // if context value passed is Relative Distinguished Name
        path.append(context);
        path.append(SystemConstant.COMMA);
        path.append(this.rootContext());
      }
      else
        // if context value is treated as a Distinguished Name of the context
        path.append(context);
    }
    return path.toString();
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
  public String normalizePath(final String distinguishedName) {
    // if all distinguished names has to be handled absolute or the root context
    // is empty return the passed value immediatly
    if (!this.relativeDN || StringUtility.isEmpty(this.rootContext))
      return distinguishedNameCaseSensitive() ? distinguishedName : distinguishedName.toLowerCase();

    // normalize the string that we are able to detect the context
    String rootContext = this.rootContext.toLowerCase();
    String nameContext = distinguishedName.toLowerCase();

    // get the position of the root context in the FQDN
    int contextPos = nameContext.lastIndexOf(rootContext);
    // if the root context is not conained in the FQDN return the name as it is
    if (contextPos < 0)
      return distinguishedName;

    // search downward for the next comma separator; only this ensures that we
    // get the correct naming part
    int pos = contextPos;
    for (; pos > 0; pos--)
      if (nameContext.charAt(pos) == SystemConstant.COMMA)
        break;

    // if the root context is defined then remove this part from the
    // distinguished name
    nameContext = (pos > -1) ? distinguishedName.substring(0, pos) : distinguishedName;
    return distinguishedNameCaseSensitive() ? nameContext : nameContext.toLowerCase();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findEntry
  /**
   ** Retrieves the RDN of the object with the specified objectGUID attribute.
   **
   ** @param  searchBase         the base DN to search from
   **
   ** @param  entryPrefix        the filter expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   ** @param  entryValue         the attributes whose values have to be
   **                            searched.
   **
   ** @return                    the "Absolute Distinguished Name" of the
   **                            directory object relative to the root context,
   **                            e.g. CN=nkaushik,OU=Engineering.Returns empty
   **                            string if object is not found or an error is
   **                            encountered
   **
   ** @throws DirectoryException in case the search operation cannot be
   **                            performed.
   */
  public String findEntry(final String searchBase, final String entryPrefix, final String entryValue)
    throws DirectoryException {

    return findEntry(searchBase, DirectoryConstant.SCOPE_SUBTREE, entryPrefix, entryValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findEntry
  /**
   ** Retrieves the RDN of the object with the specified objectGUID attribute.
   **
   ** @param  searchBase         the base DN to search from
   ** @param  searchScope        search scope either
   **                            <ul>
   **                              <li>OneLevel
   **                              <li>SubTree
   **                              <li>Object
   **                            </ul>
   ** @param  entryPrefix        the filter expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   ** @param  entryValue         the attributes whose values have to be
   **                            searched.
   **
   ** @return                    the distinguished name of the directory object
   **                            relative to the root context,
   **                            e.g. CN=nkaushik,OU=Engineering.Returns empty
   **                            string if object is not found or an error is
   **                            encountered
   **
   ** @throws DirectoryException in case the search operation cannot be
   **                            performed.
   */
  public String findEntry(final String searchBase, final String searchScope, final String entryPrefix, final String entryValue)
    throws DirectoryException {

    final String             searchFilter = composeFilter(entryPrefix, entryValue);
    final String[]           returning    = { entryPrefix };
    final List<SearchResult> result       = search(searchBase, searchFilter, searchScope, returning);
    if (result.isEmpty())
      throw new DirectoryException(DirectoryError.OBJECT_NOT_EXISTS, searchFilter);
    if (result.size() > 1)
      throw new DirectoryException(DirectoryError.OBJECT_AMBIGUOUS, searchFilter);

    final SearchResult entry = result.get(0);
    return normalizePath(entry.getNameInNamespace());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public LdapContext connect()
    throws DirectoryException {

    // call connect without any container instead will bind to the principal dn
    return connect(SystemConstant.EMPTY);
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
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public LdapContext connect(final String contextPath)
    throws DirectoryException {

    if (this.context == null) {
      // Constructs an LDAP context object using environment properties and
      // connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      this.context = connect(environment(this.contextURL(contextPath), true));
    }

    return this.context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectDSE
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   ** <p>
   ** In LDAP 3.0, rootDSE is defined as the root of the directory data tree on
   ** a directory server. The rootDSE is not part of any namespace. The purpose
   ** of the rootDSE is to provide data about the directory server.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public final LdapContext connectDSE()
    throws DirectoryException {

    StringBuilder url     = new StringBuilder();
    String[]     services = serviceURL();
    for (int i = 0; i < services.length; i++) {
      if (i > 0)
        url.append(SystemConstant.BLANK);
      // extend the list of urls with the current service
      url.append(services[i]);
      // create the service url (server name and port) prefixed with the
      // protocol check if the service url end already with a slash '/'
      if (services[i].charAt(services[i].length() - 1) != SystemConstant.SLASH)
        url.append(SystemConstant.SLASH);
    }

    // To search for root DSE,
    // 1. Set LDAP version to LDAP_V3 before binding
    // 2. Set the search base to an empty string
    // 3. Set the search filter to (objectclass=*)
    // 4. Set the search scope to LDAP_SCOPE_BASE
    return connect(environment(url.toString(), false));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectDomain
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   ** <p>
   ** The connection will bind to the Realm specific for Enterprise User
   ** Security.
   **
   ** @param  domainName         the name of the enterprise domain to connect
   **                            to.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws DirectoryException if the connection could not be established at
   **                            the first time this method is invoked.
   */
  public LdapContext connectDomain(final String domainName)
    throws DirectoryException {

    try {
      CompositeName contextPath = new CompositeName(composeName(this.enterpriseDomainPrefix(), domainName));
      contextPath.add(this.enterpriseDomainContainer());
      return connect(normalizePath("cn=Internal Database Domain,cn=OracleDBSecurity,cn=Products,cn=OracleContext"));
    }
    catch (InvalidNameException e) {
      throw new DirectoryException(DirectoryError.GENERAL, e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectRole
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   ** <p>
   ** The connection will bind to the Realm specific for Enterprise User
   ** Security.
   **
   ** @param  domainName         the name of the enterprise domain to connect
   **                            to.
   ** @param  roleName           the name of the enterprise role within the
   **                            domain.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws DirectoryException if the connection could not be established at
   **                            the first time this method is invoked.
   */
  public LdapContext connectRole(final String domainName, final String roleName)
    throws DirectoryException {

    try {
      CompositeName contextPath = new CompositeName(composeName(this.enterpriseRolePrefix(), roleName));
      contextPath.add(composeName(this.enterpriseDomainPrefix(), domainName));
      contextPath.add(this.enterpriseDomainContainer());
      return connect(normalizePath(contextPath.toString()));
    }
    catch (InvalidNameException e) {
      throw new DirectoryException(DirectoryError.GENERAL, e.getLocalizedMessage());
    }
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
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public final LdapContext connect(final Properties environment)
    throws DirectoryException {

    final String method    = "connect";
    LdapContext context = null;
    try {
      debug(method, DirectoryBundle.format(DirectoryMessage.CONNECTING_TO, URLDecoder.decode(environment.getProperty(Context.PROVIDER_URL), urlEncoding())));
      // Constructs an LDAP context object using environment properties and
      // connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      context = new InitialLdapContext(environment, null);
    }
    catch (UnsupportedEncodingException e) {
      throw new DirectoryException(DirectoryError.CONNECTION_ENCODING_NOTSUPPORTED, urlEncoding());
    }
    // when the host, port or something else is wrong in the server properties
    catch (CommunicationException e) {
      throw new DirectoryException(e);
    }
    // when the principal or password is wrong in the server properties
    catch (AuthenticationException e) {
      throw new DirectoryException(this.principalName(), e);
    }
    // when a problem may be with physical connectivity or Target System is not alive
    catch (ServiceUnavailableException e) {
      throw new DirectoryException(e);
    }
    // when the context path to connecto to is invalid
    catch (InvalidNameException e) {
      throw new DirectoryException(DirectoryError.GENERAL, e.getLocalizedMessage());
    }
    // when the operation fails in general
    catch (NamingException e) {
      throw new DirectoryException(e);
    }

    return context;
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
    this.disconnect(this.context);
    this.context = null;
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
   */
  public void disconnect(final LdapContext context) {
    try {
      if (context != null)
        context.close();
    }
    catch (NamingException e) {
      DirectoryException throwable = new DirectoryException(e);
      error("disconnect", throwable.getLocalizedMessage());
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
   **
   ** @return                    the environment context this connector use to
   **                            communicate with the LDAP server.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public final Properties environment(final String contextPath)
    throws DirectoryException {

    return this.environment(contextPath, true);
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
   ** @param  pooling            advice whether or not the underlying system
   **                            should pool the connection.
   **                            Refere to http://java.sun.com/products/jndi/tutorial/ldap/connect/pool.html
   **                            for more information.
   **
   ** @return                    the environment context this connector use to
   **                            communicate with the LDAP server.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public final Properties environment(final String contextPath, final boolean pooling)
    throws DirectoryException {

    final String method = "environment";
    Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY,      initialContextFactory());
    environment.put(Context.PROVIDER_URL,                 contextPath);
    environment.put("java.naming.ldap.version",           "3");
    environment.put(Context.SECURITY_PRINCIPAL,           this.principalName());
    environment.put(Context.SECURITY_CREDENTIALS,         this.principalPassword());
    environment.put("com.sun.jndi.ldap.connect.timeout",  this.connectionTimeout());
    environment.put("com.sun.jndi.ldap.read.timeout",     this.responseTimeout());
    // Enable or disable connection pooling
    environment.put("com.sun.jndi.ldap.connect.pool",     pooling ? SystemConstant.TRUE : SystemConstant.FALSE);

    // Register the binary objects in transport layer
    if (!StringUtility.isEmpty(binaryObjectAttribute())) {
      // The toolkit is programmed to recognize the following set of common
      // LDAP binary attributes see binaryObjectAttribute() for further
      // explanation

      // create the space-separated attribute names
      final String binaryObjectAttribute = binaryObjectAttribute().replace('|', ' ');
      environment.put("java.naming.ldap.attributes.binary", binaryObjectAttribute);
    }

    if (this.secureSocket()) {
      validateCertificates();
      environment.put(Context.SECURITY_PROTOCOL, DirectoryConstant.SECURITY_PROTOCOL);
      invalidateSSLSession();

      Provider provider = Security.getProvider(securityProvider());
      try {
        if (provider == null) {
          @SuppressWarnings("unchecked")
          Class<Provider> clazz = (Class<Provider>)Class.forName(DirectoryConstant.SECURITY_PROVIDER_DEFAULT);
          Provider provider1 = clazz.newInstance();
          Security.addProvider(provider1);
        }
      }
      catch (ClassNotFoundException e) {
        error(method, TaskBundle.format(TaskError.CLASSNOTFOUND, DirectoryConstant.SECURITY_PROVIDER_DEFAULT));
      }
      catch (InstantiationException e) {
        error(method, TaskBundle.format(TaskError.CLASSNOTCREATE, DirectoryConstant.SECURITY_PROVIDER_DEFAULT));
      }
      catch (IllegalAccessException e) {
        error(method, TaskBundle.format(TaskError.CLASSNOACCESS, DirectoryConstant.SECURITY_PROVIDER_DEFAULT));
      }
    }
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Searches in the named context or object for entries that satisfy the given
   ** search filter. Performs the search in the <code>searchScope</code> and
   ** returns the number of entries found.
   ** <br>
   ** If the named context satisfies the search filter, it is included in the
   ** enumeration with the empty string as
   **
   ** @param  searchBase         the base DN to search from
   ** @param  searchFilter       the filter expression to use for the search;
   **                            may not be null, e.g.
   ** @param  searchScope        search scope either
   **                            <ul>
   **                              <li>OneLevel
   **                              <li>SubTree
   **                              <li>Object
   **                            </ul>
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **
   ** @return                    the number of entries for the specified filter.
   **
   ** @throws DirectoryException in case the count operation cannot be
   **                            performed.
   */
  public int count(final String searchBase, final String searchFilter, final String searchScope)
    throws DirectoryException {

    return count(searchBase, searchFilter, searchScope, distinguishedNameAttribute());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Searches in the named context or object for entries that satisfy the given
   ** search filter. Performs the search in the <code>searchScope</code> and
   ** returns the number of entries found.
   **
   ** @param  searchBase         the base DN to search from
   ** @param  searchFilter       the filter expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   ** @param  searchScope        search scope either
   **                            <ul>
   **                              <li>OneLevel
   **                              <li>SubTree
   **                              <li>Object
   **                            </ul>
   ** @param  attributeName      the attributes whose values have to be
   **                            counted.
   **
   ** @return                    the {@link List} containing the names of the
   **                            LDAP objects find for the specified filter.
   **
   ** @throws DirectoryException in case the count operation cannot be
   **                            performed.
   */
  public int count(final String searchBase, final String searchFilter, final String searchScope, final String attributeName)
    throws DirectoryException {

    // the search will not cross naming system boundaries.
    final String[]       returning = { attributeName };
    final SearchControls controls  = searchScope(searchScope);
    controls.setReturningAttributes(returning);

    connect();
    // The NamingEnumeration that returned by context.search() using
    // SUBTREE_SCOPE contains elements of objects from the subtree (including
    // the named context) that satisfy the search filter specified in
    // context.search().
    // The names of elements in the NamingEnumeration are either relative to the
    // named context or is a URL string.
    // If the named context satisfies the search filter, it is included in the
    // enumeration with the empty string as its name.
    final NamingEnumeration<SearchResult> names = search(searchBase, searchFilter, controls);
    // seems to be we are ok; transform the SearchResult contained in the
    // NamingEnumeration in a list of strings to return this list to the caller
    int count = 0;
    while(names.hasMoreElements()) {
      names.nextElement();
      count++;
    }
    closeContext(names);
    disconnect();

    return count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches in the named context or object for entries that satisfy the given
   ** search filter. Performs the search in the sub-tree.
   ** <br>
   ** The {@link NamingEnumeration} that results from the search against the
   ** directory context contains elements of objects from the subtree (including
   ** the named context) that satisfy the search filter specified in search().
   ** <br>
   ** The names of elements in the NamingEnumeration are either relative to the
   ** named context or is a URL string.
   ** <br>
   ** If the named context satisfies the search filter, it is included in the
   ** enumeration with the empty string as
   **
   ** @param  searchBase         the base DN to search from
   ** @param  searchFilter       the filter expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   ** @param  searchScope        search scope either
   **                            <ul>
   **                              <li>OneLevel
   **                              <li>SubTree
   **                              <li>Object
   **                            </ul>
   **
   ** @return                    the {@link List} containing the names of the
   **                            LDAP objects find for the specified filter.
   **
   ** @throws DirectoryException in case the search operation cannot be
   **                            performed.
   */
  public List<SearchResult> search(final String searchBase, final String searchFilter, final String searchScope)
    throws DirectoryException {

    return search(searchBase, searchFilter, searchScope, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches in the named context or object for entries that satisfy the given
   ** search filter. Performs the search in the sub-tree.
   **
   ** @param  searchBase         the base DN to search from
   ** @param  searchFilter       the filter expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   ** @param  searchScope        search scope either
   **                            <ul>
   **                              <li>OneLevel
   **                              <li>SubTree
   **                              <li>Object
   **                            </ul>
   ** @param  attributeNames     the attributes whose values have to be
   **                            returned.
   **                            Set it to null, if all attribute values have to
   **                            be returned.
   **
   ** @return                    the {@link List} containing the names of the
   **                            LDAP objects find for the specified filter.
   **
   ** @throws DirectoryException in case the search operation cannot be
   **                            performed.
   */
  public List<SearchResult> search(final String searchBase, final String searchFilter, final String searchScope, final String[] attributeNames)
    throws DirectoryException {

    // the search will not cross naming system boundaries.
    SearchControls controls = searchScope(searchScope);
    if (attributeNames != null)
      controls.setReturningAttributes(attributeNames);

    connect();
    // seems to be we are ok; transform the SearchResult contained in the
    // NamingEnumeration in a list of strings to return this list to the caller
    final List<SearchResult> result = new ArrayList<SearchResult>();
    try {
      // The NamingEnumeration that results from context.search() using
      // SearchControls contains elements of objects from the subtree (including
      // the named context) that satisfy the search filter specified in
      // context.search().
      // The names of elements in the NamingEnumeration are either relative to the
      // named context or is a URL string.
      // If the named context satisfies the search filter, it is included in the
      // enumeration with the empty string as its name.
      final NamingEnumeration<SearchResult> names = search(searchBase, searchFilter, controls);
      while(names.hasMoreElements())
        result.add(names.nextElement());
      closeContext(names);
    }
    finally {
      disconnect();
    }

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches in the specified context or object for entries that satisfy the
   ** given search filter. Performs the search as specified by the search
   ** controls.
   ** <p>
   ** The format and interpretation of <code>filter</code> fullows RFC 2254 with
   ** the fullowing interpretations for <code>attr</code> and <code>value</code>
   ** mentioned in the RFC.
   ** <p>
   ** <code>attr</code> is the attribute's identifier.
   ** <p>
   ** <code>value</code> is the string representation the attribute's value.
   ** The translation of this string representation into the attribute's value
   ** is directory-specific.
   ** <p>
   ** For the assertion "someCount=127", for example, <code>attr</code>
   ** is "someCount" and <code>value</code> is "127". The provider determines,
   ** based on the attribute ID ("someCount") (and possibly its schema), that
   ** the attribute's value is an integer. It then parses the string "127"
   ** appropriately.
   ** <p>
   ** Any non-ASCII characters in the filter string should be represented by the
   ** appropriate Java (Unicode) characters, and not encoded as UTF-8 octets.
   ** Alternately, the "backslash-hexcode" notation described in RFC 2254 may be
   ** used.
   ** <p>
   ** If the directory does not support a string representation of some or all
   ** of its attributes, the form of <code>search</code> that accepts filter
   ** arguments in the form of Objects can be used instead. The service provider
   ** for such a directory would then translate the filter arguments to its
   ** service-specific representation for filter evaluation.
   ** See <code>search(Name, String, Object[], SearchControls)</code>.
   ** <p>
   ** RFC 2254 defines certain operators for the filter, including substring
   ** matches, equality, approximate match, greater than, less than.  These
   ** operators are mapped to operators with corresponding semantics in the
   ** underlying directory. For example, for the equals operator, suppose
   ** the directory has a matching rule defining "equality" of the
   ** attributes in the filter. This rule would be used for checking
   ** equality of the attributes specified in the filter with the attributes
   ** of objects in the directory. Similarly, if the directory has a
   ** matching rule for ordering, this rule would be used for
   ** making "greater than" and "less than" comparisons.
   ** <p>
   ** Not all of the operators defined in RFC 2254 are applicable to all
   ** attributes. When an operator is not applicable, the exception
   ** <code>InvalidSearchFilterException</code> is thrown.
   ** <p>
   ** The result is returned in an enumeration of <code>SearchResult</code>s.
   ** Each <code>SearchResult</code> contains the name of the object and other
   ** information about the object (see SearchResult). The name is either
   ** relative to the target context of the search (which is named by the
   ** <code>name</code> parameter), or it is a URL string. If the target context
   ** is included in the enumeration (as is possible when <code>controls</code>
   ** specifies a search scope of <code>SearchControls.OBJECT_SCOPE</code> or
   ** <code>SearchControls.SUBSTREE_SCOPE</code>), its name is the empty string.
   ** The <code>SearchResult</code> may also contain attributes of the matching
   ** object if the <code>controls</code> argument specified that attributes be
   ** returned.
   ** <p>
   ** If the object does not have a requested attribute, that nonexistent
   ** attribute will be ignored. Those requested attributes that the object does
   ** have will be returned.
   ** <p>
   ** A directory might return more attributes than were requested (see
   ** <strong>Attribute Type Names</strong> in the class description) but is not
   ** allowed to return arbitrary, unrelated attributes.
   ** <p>
   ** See also <strong>Operational Attributes</strong> in the class description.
   **
   ** @param  searchBase         the base DN to search from
   ** @param  searchFilter       the filter expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   ** @param  searchControls     the search controls that control the search. If
   **                            <code>null</code>, the default search controls
   **                            are used (equivalent to
   **                            <code>(new SearchControls())</code>).
   **
   ** @return                    an enumeration of <code>SearchResult</code>s of
   **                            the objects that satisfy the filter; never
   **                            <code>null</code>.
   **
   ** @throws DirectoryException in case the search operation cannot be
   **                            performed.
   */
  public NamingEnumeration<SearchResult> search(final String searchBase, final String searchFilter, final SearchControls searchControls)
    throws DirectoryException {

    final String method = "search";
    try {
      // prevent bogus instance state
      if (this.context == null)
        throw new DirectoryException(DirectoryError.INSTANCE_ATTRIBUTE_IS_NULL, "context");

      return this.context.search(searchBase, searchFilter, searchControls);
    }
    catch (InvalidAttributesException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_DATA, searchFilter, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (InvalidAttributeIdentifierException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_TYPE, searchFilter, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntry
  /**
   ** Creates object and binds to the passed distinguished name  and the
   ** attributes.
   ** <br>
   ** Calls the context.createSubcontext() to create object and bind it with the
   ** passed distinguished name and the attributes.
   **
   ** @param  objectRDN          the relative distinguished name of the object
   **                            to create, e.g. <code>cn=george</code>.
   ** @param  attributes         the attributes of the object.
   **
   ** @throws DirectoryException if the operation fails
   */
  public void createEntry(final String objectRDN, final Attributes attributes)
    throws DirectoryException {

    final String method = "createEntry";
    try {
      // create object
      this.context.createSubcontext(objectRDN, attributes);
    }
    catch (NameAlreadyBoundException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OBJECT_ALREADY_EXISTS, objectRDN, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (AttributeInUseException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_IN_USE, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (InvalidAttributesException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_DATA, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (SchemaViolationException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_SCHEMA_VIOLATED, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (OperationNotSupportedException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OPERATION_NOT_SUPPORTED, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (InvalidAttributeIdentifierException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_TYPE, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OBJECT_NOT_CREATED, objectRDN, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteEntry
  /**
   ** Deletes object of the passed distinguished name.
   ** <br>
   ** Calls the context.destroySubcontext() to delete object
   **
   ** @param  objectRDN           the distinguished name of the object, e.g.
   **                             <code>'cn=george,ou=people,dc=company,dc=com'</code>
   ** @param  deleteControl       the possibly <code>null</code> controls to
   **                             use. If <code>null</code>, no controls are
   **                             used.
   **
   ** @throws DirectoryException if the operation fails
   */
  public void deleteEntry(final String objectRDN, final Control[] deleteControl)
    throws DirectoryException {

    final String method = "deleteEntry";
    try {
      if (deleteControl != null)
        this.context.setRequestControls(deleteControl);

      // delete object
      this.context.destroySubcontext(objectRDN);
    }
    catch (NameNotFoundException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OBJECT_NOT_EXISTS, objectRDN, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (OperationNotSupportedException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OPERATION_NOT_SUPPORTED, objectRDN, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OBJECT_NOT_DELETED, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moveEntry
  /**
   ** Moves the object to the passed new DN
   **
   ** @param  source              path of the old distinguished name
   ** @param  target              path of the new distinguished name where it is
   **                             to be moved
   ** @param  name                name of the object to be moved
   **
   ** @throws DirectoryException if the operation fails
   */
  public void moveEntry(final String source, final String target, final String name)
    throws DirectoryException {

    String sourcePath = name;
    if (!StringUtility.isEmpty(source))
      sourcePath += "," + source;

    String targetPath = name;
    if (!StringUtility.isEmpty(target))
      targetPath += "," + target;

    rename(sourcePath, targetPath);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Rename object of the passed new distinguished name.
   ** <br>
   ** Calls the context.destroySubcontext() to delete object
   **
   ** @param  oldDistinguishedName the distinguished name of the object, e.g.
   **                              <code>'cn=george,ou=people,dc=company,dc=com'</code>
   ** @param  newDistinguishedName the distinguished name of the object, e.g.
   **                              <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **
   ** @throws DirectoryException if the operation fails
   */
  public void rename(final String oldDistinguishedName, final String newDistinguishedName)
    throws DirectoryException {

    final String method = "rename";
    try {
      // rename object
      this.context.rename(oldDistinguishedName, newDistinguishedName);
    }
    catch (NameNotFoundException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OBJECT_NOT_EXISTS, oldDistinguishedName, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NameAlreadyBoundException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OBJECT_ALREADY_EXISTS, newDistinguishedName, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OBJECT_NOT_RENAMED, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeClear
  /**
   ** Set an attribute specified by <code>attributeName</code> to
   ** <code>null</code>.
   **
   ** @param  baseRDN            the hierarchical structure to the parent of
   **                            the entry. This is the RDN of the parent,
   **                            relative to the root context, e.g.
   **                            OU=development,OU=Engineering.
   ** @param  objectRDN          the "Relative Distinguished Name" of the
   **                            directory object relative to the hierarchy
   **                            specified by the <code>baseRDN</code> parameter,
   **                            usually it is just the objects name e.g.
   **                            CN=DSteding
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be set to <code>null</code>,
   **                            e.g. "uSNChanged". Keep it case sensitive, if
   **                            possible.
   **
   ** @throws DirectoryException in case the operation fails.
   */
  public void attributeClear(final String baseRDN, final String objectRDN, final String attributeName)
    throws DirectoryException {

    try {
      connect(baseRDN);
      BasicAttributes attrs = new BasicAttributes(true);
      attrs.put(new BasicAttribute(attributeName));
      this.attributesDelete(objectRDN, attrs);
    }
    finally {
      disconnect();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesAdd
  /**
   ** Adds passed attributes to a existing object at the distinguished name.
   ** <br>
   ** Calls the <code>context.modifyAttributes()</code> to replace the attributes.
   **
   ** @param  objectRDN           the relative distinguished name of the object,
   **                             e.g. <code>'cn=george'</code>
   ** @param  attributes          the new attributes of the object
   **
   ** @throws DirectoryException if the operation fails
   */
  public void attributesAdd(final String objectRDN, final Attributes attributes)
    throws DirectoryException {

    final String method = "attributesAdd";
    try {
      // modify object
      this.context.modifyAttributes(objectRDN, LdapContext.ADD_ATTRIBUTE, attributes);
      debug(method, DirectoryBundle.format(DirectoryMessage.ATTRIBUTE_ADDED, objectRDN, attributes.toString()));
    }
    catch (AttributeInUseException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_IN_USE, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (InvalidAttributesException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_DATA, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (SchemaViolationException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_SCHEMA_VIOLATED, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (InvalidAttributeIdentifierException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_TYPE, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.OBJECT_NOT_ASSIGNED, objectRDN, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesModify
  /**
   ** Modifies existing attributes of an object at the passed distinguished name
   ** with the specified attributes
   ** <br>
   ** Calls the <code>context.modifyAttributes()</code> to replace the attributes.
   **
   ** @param  entryRDN            the relative distinguished name of the entry,
   **                             e.g. <code>'cn=george'</code>
   ** @param  attributes          the new attributes of the object
   **
   ** @throws DirectoryException if the operation fails
   */
  public void attributesModify(final String entryRDN, final Attributes attributes)
    throws DirectoryException {

    final String    method   = "attributesModify";
    final Attribute password = attributes.get(accountPasswordAttribute());
    final String    display  = password == null ? attributes.toString() : "{" + accountPasswordAttribute() + "=" + accountPasswordAttribute() + ": ******}";
    // supress generation of output if logger is not configured for
    if (this.logger != null && this.logger.debugLevel())
      // suppress logging password
      debug(method, DirectoryBundle.format(DirectoryMessage.ATTRIBUTE_TOMODIFY, entryRDN, display));

    try {
      // modify object
      this.context.modifyAttributes(entryRDN, LdapContext.REPLACE_ATTRIBUTE, attributes);
      // supress generation of output if logger is not configured for
      if (this.logger != null && this.logger.debugLevel())
        debug(method, DirectoryBundle.format(DirectoryMessage.ATTRIBUTE_MODIFIED, entryRDN, display));
    }
    catch (AttributeInUseException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_IN_USE, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (InvalidAttributeIdentifierException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_TYPE, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      error(method, DirectoryBundle.format(DirectoryError.OBJECT_NOT_UPDATED, e.getLocalizedMessage()));
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesDelete
  /**
   ** Deletes existing attributes of an object at the passed distinguished name
   ** with the specified attributes
   ** <br>
   ** Calls the <code>context.modifyAttributes()</code> to replace the attributes.
   **
   ** @param  objectRDN           the relative distinguished name of the object,
   **                             e.g. <code>'cn=george'</code>
   ** @param  attributes          the attributes of the object to remove
   **
   ** @throws DirectoryException if the operation fails
   */
  public void attributesDelete(final String objectRDN, final Attributes attributes)
    throws DirectoryException {

    final String method = "attributesDelete";
    try {
      // modify object
      this.context.modifyAttributes(objectRDN, LdapContext.REMOVE_ATTRIBUTE, attributes);
      debug(method, DirectoryBundle.format(DirectoryMessage.ATTRIBUTE_DELETED, objectRDN, attributes.toString()));
    }
    catch (NameNotFoundException e) {
      DirectoryException throwable =  new DirectoryException(DirectoryError.OBJECT_NOT_EXISTS, objectRDN, e);
      error(method, e.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBitMaskedAttribute
  /**
   ** There are many attributes in LDAP that are stored as integers but are
   ** actually bit masks (e.g. userOrganizationControl, systemFlags). This
   ** method allows you to set or unset a particular bit (or group of bits)
   ** using a bit mask.
   **
   ** @param  baseRDN            the hierarchical structure to the parent of
   **                            the entry. This is the RDN of the parent,
   **                            relative to the root context, e.g.
   **                            OU=development,OU=Engineering.
   ** @param  objectRDN          the "Relative Distinguished Name" of the
   **                            directory object relative to the hierarchy
   **                            specified by the <code>baseRDN</code> parameter,
   **                            usually it is just the objects name e.g.
   **                            CN=DSteding
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved. e.g.
   **                            "uSNChanged". Keep it case sensitive, if
   **                            possible.
   ** @param  attributeMask      the integer mask to use to locate the bit(s) to
   **                            be checked, e.g. if we want to see the 5th bit,
   **                            the mask would be binary 10000, and so
   **                            attributeMask = 16
   ** @param  checked            indicating if we want to set the bit(s)
   **                            (check=<code>true</code>) or unset the bit(s)
   **                            (check=<code>false</code>)
   **
   ** @throws DirectoryException if the operation fails
   */
  public void setBitMaskedAttribute(final String baseRDN, final String objectRDN, final String attributeName, final int attributeMask, final boolean checked)
    throws DirectoryException {

    List<Object> value = fetchAttributeValues(baseRDN, objectRDN, attributeName);
    int          bit   = Integer.parseInt((String)value.get(0));

    try {
      connect(baseRDN);
      // the attribute should be set (the bit has to be set) or should not be
      // checked (the bit has to be unset)
      int mask =  checked ?  bit | attributeMask : bit & (~attributeMask);
      attributesModify(objectRDN, new BasicAttributes(attributeName, String.valueOf(mask), true));
    }
    finally {
      disconnect();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasBitMaskedAttribute
  /**
   ** There are many attributes in LDAP that are stored as integers but are
   ** actually bit masks (e.g. userOrganizationControl, systemFlags). This
   ** method allows you to see if a particular bit is set or unset.
   **
   ** @param  baseRDN            the hierarchical structure to the parent of
   **                            the entry. This is the RDN of the parent,
   **                            relative to the root context, e.g.
   **                            OU=development,OU=Engineering.
   ** @param  objectRDN          the "Relative Distinguished Name" of the
   **                            directory object relative to the hierarchy
   **                            specified by the <code>baseRDN</code> parameter,
   **                            usually it is just the objects name e.g.
   **                            CN=DSteding
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved. e.g.
   **                            "uSNChanged". Keep it case sensitive, if
   **                            possible.
   ** @param  attributeMask      the integer mask to use to locate the bit(s) to
   **                            be checked, e.g. if we want to see the 5th bit,
   **                            the mask would be binary 10000, and so
   **                            attributeMask = 16.
   **
   ** @return                    <code>true</code> if the bit is set
   **                           (corresponding to the property being checked),
   **                            <code>false</code> if the bit is unset
   **                            (corresponding to the property being unchecked)
   **
   ** @throws DirectoryException if the operation fails
   */
  public boolean hasBitMaskedAttribute(final String baseRDN, final String objectRDN, final String attributeName, final int attributeMask)
    throws DirectoryException {

    List<Object> value = fetchAttributeValues(baseRDN, objectRDN, attributeName);
    int          bit   = Integer.parseInt((String)value.get(0));

    int checked = bit & attributeMask;
    return (checked == 0) ? false : (checked == attributeMask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAttributeValue
  /**
   ** Returns the value for the specified attribute of the specified
   ** directory object.
   ** <p>
   ** It throws an exception if more than one value is found.
   **
   ** @param  baseRDN            the hierarchical structure to the parent of
   **                            the entry. This is the RDN of the parent,
   **                            relative to the root context, e.g.
   **                            OU=development,OU=Engineering.
   ** @param  objectRDN          the "Relative Distinguished Name" of the
   **                            directory object relative to the hierarchy
   **                            specified by the <code>baseRDN</code> parameter,
   **                            usually it is just the objects name e.g.
   **                            CN=DSteding
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved. e.g.
   **                            "uSNChanged". Keep it case sensitive, if
   **                            possible.
   **
   ** @return                    a String holding the value.
   **                            Returns empty string if object is not found or
   **                            an error is encountered
   **
   ** @throws DirectoryException if the operation fails
   */
  public String fetchAttributeValue(final String baseRDN, final String objectRDN, final String attributeName)
    throws DirectoryException {

    final String       method = "fetchAttributeValue";
    final List<Object> result = fetchAttributeValues(baseRDN, objectRDN, attributeName);
    if ((result == null) || (result.isEmpty()))
      return SystemConstant.EMPTY;
    if (result.size() > 1)
      error(method, DirectoryBundle.string(DirectoryError.ATTRIBUTE_INVALID_SIZE));

    return (String)result.get(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAttributeValues
  /**
   ** Lists the value(s) of a particular object attribute.
   ** <p>
   ** Returns it as a {@link List} to accommodate properties that have a list of
   ** values.
   **
   ** @param  baseRDN            the hierarchical structure to the parent of
   **                            the entry. This is the RDN of the parent,
   **                            relative to the root context, e.g.
   **                            OU=development,OU=Engineering.
   ** @param  objectRDN          the "Relative Distinguished Name" of the
   **                            directory object relative to the hierarchy
   **                            specified by the <code>baseRDN</code> parameter,
   **                            usually it is just the objects name e.g.
   **                            CN=DSteding
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **
   ** @return                    a {@link List} of Strings, consisting of all
   **                            the values this attribute was set to.
   **
   ** @throws DirectoryException if the operation fails
   */
  public List<Object> fetchAttributeValues(final String baseRDN, final String objectRDN, final String attributeName)
    throws DirectoryException {

    final String method = "fetchAttributeValues";

    connect(baseRDN);

    List<Object> values    = null;
    Attributes   response  = null;
    String[]     parameter = { attributeName };
    try {
      response = this.context.getAttributes(objectRDN, parameter);
      // check if we got a result from the server
    }
    catch (NameNotFoundException e) {
      DirectoryException throwable =  new DirectoryException(DirectoryError.OBJECT_NOT_EXISTS, objectRDN, e);
      error(method, e.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
    finally {
      disconnect();
    }

    if (response == null)
      return values;

    try {
      Attribute attribute = response.get(attributeName);
      if (attribute != null) {
        values = new ArrayList<Object>(attribute.size());
        for (int i = 0; i < attribute.size(); i++)
          values.add(attribute.get(i));
      }
    }
    catch (InvalidAttributesException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_DATA, attributeName, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (InvalidAttributeIdentifierException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_TYPE, attributeName, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
    return values;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAllAttributes
  /**
   ** Returns all the attributes of an entry from target system.
   **
   ** @param  baseRDN            the hierarchical structure to the parent of
   **                            the entry. This is the RDN of the parent,
   **                            relative to the root context, e.g.
   **                            OU=development,OU=Engineering.
   ** @param  objectRDN          the "Relative Distinguished Name" of the
   **                            directory object relative to the hierarchy
   **                            specified by the <code>baseRDN</code> parameter,
   **                            usually it is just the objects name e.g.
   **                            CN=DSteding
   **
   ** @return                    {@link Attributes} of the entry
   **
   ** @throws DirectoryException if the operation fails
   */
  public Attributes fetchAllAttributes(final String baseRDN, final String objectRDN)
    throws DirectoryException {

    final String method = "fetchAllAttributes";

    connect(baseRDN);

    Attributes attributes = null;
    try {
      attributes = this.context.getAttributes(objectRDN);
    }
    catch (NameNotFoundException e) {
      throw new DirectoryException(DirectoryError.OBJECT_NOT_EXISTS, objectRDN, e);
    }
    catch (InvalidAttributesException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_DATA, objectRDN, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (InvalidAttributeIdentifierException e) {
      DirectoryException throwable = new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_TYPE, objectRDN, e);
      error(method, throwable.getLocalizedMessage());
      throw throwable;
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
    finally {
      disconnect();
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetRequestControl
  /**
   ** This method restes all RequestControl in the context.
   **
   ** @param  context            the {@link LdapContext} the {@link Control}s
   **                            will be assigned to.
   **
   ** @throws TaskException      if the passed context is <code>null</code>.
   ** @throws DirectoryException if an error was encountered while encoding the
   **                            supplied arguments into a control or if an
   **                            error occurred while setting the request
   **                            control.
   */
  public void resetRequestControl(final LdapContext context)
    throws TaskException {

    // prevent bogus input
    if (context == null)
      throw TaskException.argumentIsNull("context");

    try {
      // reset the request controls in the context
      context.setRequestControls(null);
    }
    catch (NamingException e) {
      // throw a wrapped unhandled exception
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parsePagingResponse
  /**
   ** Extract the paging controls from the {@link Control}s returned from the
   ** LDAP server after a search operation was performed.
   **
   **
   ** @return                    an OCTET string wrapping the BER-encoded
   **                            version control sequence.
   **
   ** @throws TaskException      if the operation fails
   */
  public byte[] parsePagingResponse()
    throws TaskException {

    // prevent bogus instance state
    if (this.context == null)
      throw new DirectoryException(DirectoryError.INSTANCE_ATTRIBUTE_IS_NULL, "context");

    return parsePagingResponse(this.context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parsePagingResponse
  /**
   ** Extract the paging controls from the {@link Control}s returned from the
   ** LDAP server after a search operation was performed.
   **
   ** @param  context            the {@link LdapContext}.
   **
   ** @return                    an OCTET string wrapping the BER-encoded
   **                            version control sequence.
   **
   ** @throws TaskException      if the passed context is <code>null</code>.
   ** @throws DirectoryException if the operation fails
   */
  public byte[] parsePagingResponse(final LdapContext context)
    throws TaskException {

    // prevent bogus instance state
    if (context == null)
      throw TaskException.argumentIsNull("context");

    try {
      return parsePagingResponse(context.getResponseControls());
    }
    catch (NamingException e) {
      // throw a wrapped unhandled exception
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parsePagingResponse
  /**
   ** Extract the paging controls from the {@link Control}s returned from the
   ** LDAP server after a search operation was performed.
   **
   ** @param  controls           the {@link Control}s returned from the LDAP
   **                            server after a search operation.
   **
   ** @return                    an OCTET string wrapping the BER-encoded
   **                            version control sequence.
   **
   ** @throws DirectoryException if the operation fails
   */
  public static byte[] parsePagingResponse(final Control[] controls)
    throws DirectoryException {

    byte[] cookie = null;
    if (controls != null) {
      for (int i = 0; i < controls.length; i++) {
        controls[i] = controlInstance(controls[i]);
        if (controls[i] instanceof PagedResultsResponseControl) {
          final PagedResultsResponseControl response = (PagedResultsResponseControl)controls[i];
          cookie = response.getCookie();
        }
      }
    }
    return (cookie == null) ? new byte[0] : cookie;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controlInstance
  /**
   ** @param  control            the {@link Control} returned from the LDAP
   **                            server after a search operation.
   **
   ** @return                    {@link Control}
   **
   ** @throws DirectoryException if the operation fails
   */
  public static Control controlInstance(final Control control)
    throws DirectoryException {

    Control response = null;
    final String  oid = control.getID();
    if (PagedResultsResponseControl.OID.equals(oid))
      try {
        response = new PagedResultsResponseControl(oid, control.isCritical(), control.getEncodedValue());
      }
      catch (IOException e) {
        throw new DirectoryException(DirectoryError.UNHANDLED, e);
      }

    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server part of the ldap url, ldap://host:port for the specified
   ** properties
   **
   ** @param  protocol           the protocol of the url to build.
   ** @param  host               the name of the host the service is running on.
   ** @param  port               the port the service is listining on.
   **
   ** @return                    the server part of the ldap url,
   **                            ldap://host:port
   */
  public static String serviceURL(final String protocol, final String host, final int port) {
    StringBuilder service = new StringBuilder(protocol);
    service.append("://");
    if (host != null)
      service.append(host).append(SystemConstant.COLON).append(port);

    return service.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformToDate
  /**
   ** Returns the specified <code>attributeValue</code> as an appropriate
   ** Date transformation.
   ** <p>
   ** LDAP Timestamp is in the form of "yyyyMMddHHmmss'Z'". The date and time
   ** specified in LDAP Timestamp is based on the Coordinated Universal Time
   ** (UTC). The date and time "Mon Jul 30 17:42:00 2001" is represented in
   ** LDAP Timestamp as "20010730174200'Z'".
   **
   ** @param  attributeValue     the specific attribute value that has to be
   **                            transformed.
   **
   ** @return                    the transformed {@link Date} value.
   */
  public Date transformToDate(final String attributeValue) {
    final SimpleDateFormat formatter = new SimpleDateFormat(timestampFormat());

    Date timestamp = zero;
    // apply tranformation only if we don't have an empty value
    if (!StringUtility.isEmpty(attributeValue)) {
      try {
        timestamp = formatter.parse(attributeValue);
      }
      catch (ParseException e) {
        error("transformToDate", String.format("Cannot transform %s to date using format %s", attributeValue, timestampFormat()));
      }
    }
    return timestamp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link DirectoryFeature} from a path.
   **
   ** @param  task               the {@link AbstractMetadataTask} where the
   **                            object to create will belong to.
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **
   ** @return                    the {@link DirectoryFeature} created from the
   **                            specified propertyFile.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected static DirectoryFeature unmarshal(final AbstractMetadataTask task, final String path)
    throws TaskException {

    DirectoryFeature feature = DirectoryFeature.build(task);
    try {
      final MDSSession session  = task.createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      DirectoryFeatureFactory.configure(feature, document);
    }
    catch (ReferenceException e) {
      throw new DirectoryException(e);
    }
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server parts of the ldap url, ldap://host:port
   **
   ** @return                    the server part of the ldap url,
   **                            ldap://host:port
   */
  protected String[] serviceURL() {
    if (this.serviceURL != null)
      return this.serviceURL;

    // lazy initialization of the configured service URL's
    final String                primary  = serviceURL(DirectoryConstant.PROTOCOL_DEFAULT, serverName(), serverPort());
    final List<DirectoryServer> failover = failoverConfiguration();
    // if no failover configuration is declared only the primary service
    // evaluated by the properties of the assigned IT Resource Definition can
    // be used
    if (failover == null || failover.size() == 0) {
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
      this.serviceURL = new String[failover.size() + 1];
      // set the primary service as the first that has to be used by the
      // connection establishment
      int  service               = 0;
      this.serviceURL[service++] = primary;
      for (DirectoryServer secondary : failover)
        this.serviceURL[service++] = serviceURL(DirectoryConstant.PROTOCOL_DEFAULT, secondary.serverName, secondary.serverPort);
    }
    return this.serviceURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidateSSLSession
  /**
   ** Invalidates the SSL session.
   ** <br>
   ** Future connections will not be able to resume or join this session.
   ** However, any existing connection using this session can continue to use
   ** the session until the connection is closed.
   **
   ** @throws DirectoryException if the operation fails
   */
  protected void invalidateSSLSession()
    throws DirectoryException {

    try {
      // if not already done obtain the default SSL socket factory
      if (socketFactory == null)
        socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();

      final String     host    = this.serverName();
      final int        port    = this.serverPort();

      // creates a SSL socket and connect it to the remote host at the remote
      // port.
      final SSLSocket  socket  = (SSLSocket)socketFactory.createSocket(host, port);
      final SSLSession session = socket.getSession();
      session.invalidate();
      socket.close();
    }
    // if the host is not known
    catch (UnknownHostException e) {
      throw new DirectoryException(DirectoryError.CONNECTION_UNKNOWN_HOST, this.serverName());
    }
    // if an I/O error occurs when creating the socket
    catch (IOException e) {
      String[] parameter = {this.serverName(), String.valueOf(this.serverPort())};
      throw new DirectoryException(DirectoryError.CONNECTION_CREATE_SOCKET, parameter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateCertificates
  /**
   ** Get the certificate path from the <code>java.library.path</code> and check
   ** whether the critical extension is supported in the certificate or not.
   **
   ** @return                    <code>true</code> if the critical extension is
   **                            supported in the certificate or not
   **
   ** @throws DirectoryException if the operation fails
   */
  protected boolean validateCertificates()
    throws DirectoryException {

    final String method = "validateCertificates";
    boolean extentionSupported = true;
    String libraryPath         = System.getProperty("java.library.path");
    String certificateFile     = SystemConstant.EMPTY;
    if (libraryPath != null && libraryPath.length() > 0) {
      String parts[] = libraryPath.split(File.separator);
      if (parts != null && parts.length > 0)
        certificateFile = parts[parts.length - 1];
    }
    try {
      if (certificateFile != null && certificateFile.length() > 0 && certificateFile.indexOf(".cer") >= 0) {
        FileInputStream stream = new FileInputStream(certificateFile);

        // Generates a certificate factory object that implements the X.509 type.
        // If the default provider package provides an implementation of the
        // requested certificate type, an instance of certificate factory
        // containing that implementation is returned.
        // If the type is not available in the default package, other packages
        // are searched.
        CertificateFactory factory     = CertificateFactory.getInstance(DirectoryConstant.CERTIFICATE_TYPE);
        X509Certificate    certificate = (X509Certificate)factory.generateCertificate(stream);
        stream.close();
        Set<String> set = certificate.getCriticalExtensionOIDs();
        if (set != null && !set.isEmpty())
          info(DirectoryBundle.string(DirectoryError.CONTROL_EXTENSION_EXISTS));
        else
          info(DirectoryBundle.string(DirectoryError.CONTROL_EXTENSION_NOT_EXISTS));

        extentionSupported = !certificate.hasUnsupportedCriticalExtension();
      }
    }
    // if the requested certificate type is not available in the default
    // provider package or any of the other provider packages that were
    // searched.
    catch (CertificateException e) {
      throw new DirectoryException(DirectoryError.CERTIFICATE_TYPE_NOT_AVAILABLE, DirectoryConstant.CERTIFICATE_TYPE,  e);
    }
    // if the certificate file is not available in the default provider package
    // or any of the other provider packages that were searched.
    catch (FileNotFoundException e) {
      throw new DirectoryException(DirectoryError.CERTIFICATE_FILE_NOT_FOUND, certificateFile,  e);
    }
    catch (IOException e) {
      error(method, e.getLocalizedMessage());
    }
    return extentionSupported;
  }
}