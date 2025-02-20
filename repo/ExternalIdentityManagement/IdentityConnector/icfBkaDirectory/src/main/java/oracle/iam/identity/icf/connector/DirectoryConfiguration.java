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

    File        :   DirectoryConfiguration.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryConfiguration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.spi.ConfigurationProperty;

import org.identityconnectors.framework.spi.operations.SyncOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.CreateOp;

import oracle.iam.identity.icf.foundation.SystemConstant;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.resource.Connector;
import oracle.iam.identity.icf.resource.DirectoryBundle;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryConfiguration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Encapsulates the configuration of the connector.
 ** <p>
 ** The configuration includes every property that a caller MUST specify in
 ** order to use the connector, as well as every property that a caller MAY
 ** specify in order to affect the behavior of the connector overall (as opposed
 ** to operation options, which affect only a specific invocation of a specific
 ** operation).
 ** <br>
 ** Required configuration parameters ConfigurationProperty.required() generally
 ** include the information required to connect to a target instance--such as a
 ** URL, username and password.
 ** <br>
 ** Optional configuration parameters often specify preferences as to how the
 ** connector-bundle should behave.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryConfiguration extends AbstractConfiguration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String             CATEGORY = "OCS.BDS.CONNECTOR";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the host name for the target system Directory Service.
   */
  private final DirectoryEndpoint endpoint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default Directory Service connector
   ** <code>DirectoryConfiguration</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryConfiguration() {
    // ensure inheritance
    super(CATEGORY);

    // initialize instance
    setConnectorMessages(DirectoryBundle.RESOURCE);

    // initialize instance attributes
    this.endpoint = DirectoryEndpoint.build(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrimaryHost
  /**
   ** Set the <code>primaryHost</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>primaryHost</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=1, displayMessageKey=Connector.Endpoint.HOST_LABEL, helpMessageKey=Connector.Endpoint.HOST_HINT, required=true)
  public final void setPrimaryHost(final String value) {
    this.endpoint.primaryHost(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrimaryHost
  /**
   ** Returns the <code>primaryHost</code> attribute for the content provider.
   **
   ** @return                    the <code>primaryHost</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getPrimaryHost() {
    return this.endpoint.primaryHost();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrimaryPort
  /**
   ** Set the <code>primaryPort</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>primaryPort</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=2, displayMessageKey=Connector.Endpoint.PORT_LABEL, helpMessageKey=Connector.Endpoint.PORT_HINT, required=true)
  public final void setPrimaryPort(final int value) {
    this.endpoint.primaryPort(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrimaryPort
  /**
   ** Returns the <code>primaryPort</code> attribute for the content provider.
   **
   ** @return                    the <code>primaryPort</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int getPrimaryPort() {
    return this.endpoint.primaryPort();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRootContext
  /**
   ** Call by the ICF framework to inject the argument for parameter
   ** <code>rootContext</code>.
   **
   ** @param  value              the value for the attribute
   **                            <code>rootContext</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=3, displayMessageKey=Connector.Endpoint.ROOT_CONTEXT_LABEL, helpMessageKey=Connector.Endpoint.ROOT_CONTEXT_HINT)
  public final void setRootContext(final String value) {
    this.endpoint.rootContext(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRootContext
  /**
   ** Returns the rootContext type of the Directory Service used to connect to.
   **
   ** @return                    tthe value for the attribute
   **                            <code>rootContext</code> used as the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getRootContext() {
    return this.endpoint.rootContext();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setSecureSocket
  /**
   ** Set the credential of the security principal for the Directory Service
   ** used to connect to.
   **
   ** @param  value              specifies whether or not to use TLS to secure
   **                            communication between Identity Manager and the
   **                            target system Directory Service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=4, displayMessageKey=Connector.Endpoint.SECURE_LABEL, helpMessageKey=Connector.Endpoint.SECURE_HINT)
  public void setSecureSocket(final boolean value) {
    this.endpoint.secureSocket(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecureSocket
  /**
   ** Returns the credential of the security principal for the Directory Service
   ** used to connect to.
   **
   ** @return                    specifies whether or not to use TLS to secure
   **                            communication between Identity Manager and the
   **                            target system Directory Service.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean getSecureSocket() {
    return this.endpoint.secureSocket();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalUsername
  /**
   ** Set the username of the security principal for the Directory Service used
   ** to connect to.
   **
   ** @param  value              the username of the security principal for the
   **                            Directory Service used to connect to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=5, displayMessageKey=Connector.Endpoint.PRINCIPAL_USERNAME_LABEL, helpMessageKey=Connector.Endpoint.PRINCIPAL_USERNAME_HINT, required=true)
  public void setPrincipalUsername(final String value) {
    this.endpoint.principalUsername(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalUsername
  /**
   ** Returns the username of the security principal for the Directory Service
   ** used to connect to.
   **
   ** @return                    the username of the security principal for the
   **                            Directory Service used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getPrincipalUsername() {
    return this.endpoint.principalUsername();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalPassword
  /**
   ** Set the credential of the security principal for the Directory Service
   ** used to connect to.
   **
   ** @param  value              the credential of the security principal for
   **                            the Directory Service used to connect to.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   */
  @ConfigurationProperty(order=6, displayMessageKey=Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL, helpMessageKey=Connector.Endpoint.PRINCIPAL_PASSWORD_HINT, required=true)
  public void setPrincipalPassword(final GuardedString value) {
    this.endpoint.principalPassword(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalPassword
  /**
   ** Returns the credential of the security principal for the Directory Service
   ** used to connect to.
   **
   ** @return                    the credential of the security principal for
   **                            the Directory Service used to connect to.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public GuardedString getPrincipalPassword() {
    return this.endpoint.principalPassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLanguage
  /**
   ** Set the <code>language</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>language</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=7, displayMessageKey=Connector.Endpoint.LANGUAGE_LABEL, helpMessageKey=Connector.Endpoint.LANGUAGE_HINT)
  public final void setLanguage(final String value) {
    this.endpoint.localeLanguage(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLanguage
  /**
   ** Returns the <code>language</code> attribute.
   **
   ** @return                    the <code>language</code> attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getLanguage() {
    return this.endpoint.localeLanguage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCountry
  /**
   ** Set the <code>country</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>country</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=8, displayMessageKey=Connector.Endpoint.COUNTRY_LABEL, helpMessageKey=Connector.Endpoint.COUNTRY_HINT)
  public final void setCountry(final String value) {
    this.endpoint.localeCountry(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCountry
  /**
   ** Returns the <code>country</code> attribute.
   **
   ** @return                    the <code>country</code> attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getCountry() {
    return this.endpoint.localeCountry();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeZone
  /**
   ** Set the <code>timeZone</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>timeZone</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=9, displayMessageKey=Connector.Endpoint.TIMEZONE_LABEL, helpMessageKey=Connector.Endpoint.TIMEZONE_HINT)
  public final void setTimeZone(final String value) {
    this.endpoint.localeTimeZone(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTimeZone
  /**
   ** Returns the <code>timeZone</code> attribute.
   **
   ** @return                    the <code>timeZone</code> attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getTimeZone() {
    return this.endpoint.localeTimeZone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectionTimeOut
  /**
   ** Sets the timeout period to establish a connection to the content provider.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** content provider. When connection pooling has been requested, this
   ** property also specifies the maximum wait time or a connection when all
   ** connections in pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the content provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   **
   ** @param  value              the timeout to establish a JMX connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=10, displayMessageKey=Connector.Connection.CONNECT_TIMEOUT_LABEL, helpMessageKey=Connector.Connection.CONNECT_TIMEOUT_HINT)
  public final void setConnectionTimeOut(final int value) {
    this.endpoint.timeOutConnect(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConnectionTimeOut
  /**
   ** Returns the timeout period for establishment of the connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** content provider. When connection pooling has been requested, this
   ** property also specifies the maximum wait time or a connection when all
   ** connections in pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the content provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   **
   ** @return                    the timeout to establish a connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int getConnectionTimeOut() {
    return this.endpoint.timeOutConnect();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResponseTimeOut
  /**
   ** Sets the timeout period the service consumer doesn't get a response.
   ** <p>
   ** When an servive request is made by a client to a server and the server
   ** does not respond for some reason, the client waits forever for the
   ** server to respond until the TCP timeouts. On the client-side what the
   ** user experiences is esentially a process hang. In order to control the
   ** service request in a timely manner, a read timeout can be configured for
   ** the service provider.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   **
   ** @param  value              timeout period the service consumer will wait
   **                            for a response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=11, displayMessageKey=Connector.Connection.RESPONSE_TIMEOUT_LABEL, helpMessageKey=Connector.Connection.RESPONSE_TIMEOUT_HINT)
  public final void setResponseTimeOut(final int value) {
    this.endpoint.timeOutResponse(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResponseTimeOut
  /**
   ** Returns the timeout period the service consumer doesn't get a response.
   ** <p>
   ** When an servive request is made by a client to a server and the server
   ** does not respond for some reason, the client waits forever for the
   ** server to respond until the TCP timeouts. On the client-side what the
   ** user experiences is esentially a process hang. In order to control the
   ** service request in a timely manner, a read timeout can be configured for
   ** the service provider.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   **
   ** @return                    timeout period the service consumer will wait
   **                            for a response.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int getResponseTimeOut() {
    return this.endpoint.timeOutResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectionRetryCount
  /**
   ** Set the number of consecutive attempts to be made at establishing a
   ** connection with the Directory Server.
   **
   ** @param  value              the number of consecutive attempts to be made
   **                            at establishing a connection with the Directory
   **                            Server.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=12, displayMessageKey=Connector.Connection.CONNECT_RETRYCOUNT_LABEL, helpMessageKey=Connector.Connection.CONNECT_RETRYCOUNT_HINT)
  public final void setConnectionRetryCount(final int value) {
    this.endpoint.retryCount(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConnectionRetryCount
  /**
   ** Returns the number of consecutive attempts to be made at establishing a
   ** connection with the Directory Server.
   **
   ** @return                    the timeout period for establishment of the
   **                            Directory Server connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int getConnectionRetryCount() {
    return this.endpoint.retryCount();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectionRetryInterval
  /**
   ** Set the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the Directory Server.
   **
   ** @param  value              the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the Directory Server.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=13, displayMessageKey=Connector.Connection.CONNECT_RETRYINTERVAL_LABEL, helpMessageKey=Connector.Connection.CONNECT_RETRYINTERVAL_HINT)
  public final void setConnectionRetryInterval(final int value) {
    this.endpoint.retryInterval(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConnectionRetryInterval
  /**
   ** Returns the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the Directory Server.
   **
   ** @return                    the timeout period for establishment of the
   **                            Directory Server connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int getConnectionRetryInterval() {
    return this.endpoint.retryInterval();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUrlEncoding
  /**
   ** Sets the value of the URL encoding.
   **
   ** @param  value              value of the URL encoding.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=14, displayMessageKey=Connector.Feature.URL_ENCODING_LABEL, helpMessageKey=Connector.Feature.URL_ENCODING_HINT)
  public final void setUrlEncoding(final String value) {
    this.endpoint.urlEncoding(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUrlEncoding
  /**
   ** Returns the value of the URL encoding.
   **
   ** @return                    value of the URL encoding.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getUrlEncoding() {
    return this.endpoint.urlEncoding();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextFactory
  /**
   ** Sets the value of the initial context factory.
   **
   ** @param  value              the value of the initial context factory.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=15, displayMessageKey=Connector.Feature.CONTEXT_FACTORY_LABEL, helpMessageKey=Connector.Feature.CONTEXT_FACTORY_HINT)
  public final void setContextFactory(final String value) {
    this.endpoint.contextFactory(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContextFactory
  /**
   ** Returns the value of the initial context factory.
   **
   ** @return                    the value of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getContextFactory() {
    return this.endpoint.contextFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecurityProvider
  /**
   ** Sets the value of the TLS security provider.
   **
   ** @param  value              the value of the TLS security provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=16, displayMessageKey=Connector.Feature.SECURITY_PROVIDER_LABEL, helpMessageKey=Connector.Feature.SECURITY_PROVIDER_HINT)
  public final void setSecurityProvider(final String value) {
    this.endpoint.securityProvider(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecurityProvider
  /**
   ** Returns the value of the TLS security provider.
   **
   ** @return                    the value of the TLS security provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getSecurityProvider() {
    return this.endpoint.securityProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFailover
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
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=17, displayMessageKey=Connector.Feature.FAILOVER_LABEL, helpMessageKey=Connector.Feature.FAILOVER_HINT)
  public final void setFailover(final String value) {
    // a value passed in is never null but can be empty hence avoid creation of
    // waste
    if (!StringUtility.blank(value)) {
      List<DirectoryEndpoint.Server> exist = this.endpoint.failover();
      if (exist == null) {
        this.endpoint.failover(new ArrayList<DirectoryEndpoint.Server>());
        exist = this.endpoint.failover();
      }
      exist.add(new DirectoryEndpoint.Server(value, -1));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFailover
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
   **                            Possible object is {@link String}.
   */
  public final String getFailover() {
    return (CollectionUtility.empty(this.endpoint.failover())) ? SystemConstant.EMPTY : this.endpoint.failover().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setReferentialIntegrity
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
   ** @param  value              <code>true</code> if referential integrity is
   **                            enabled in target Directory Service; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=18, displayMessageKey=Connector.Feature.REFERENTIAL_INTEGRITY_LABEL, helpMessageKey=Connector.Feature.REFERENTIAL_INTEGRITY_HINT)
  public final void setReferentialIntegrity(final boolean value) {
    this.endpoint.referentialIntegrity(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getReferentialIntegrity
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
   ** @return                    <code>true</code> if referential integrity is
   **                            enabled in target Directory Service; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean getReferentialIntegrity() {
    return this.endpoint.referentialIntegrity();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimestampFormat
  /**
   ** Sets separator character for Strings that provides more than one value.
   **
   ** @param  value              separator sign for Strings that provides more
   **                            than one value.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=19, displayMessageKey=Connector.Feature.TIMESTAMP_FORMAT_LABEL, helpMessageKey=Connector.Feature.TIMESTAMP_FORMAT_HINT)
  public final void setTimestampFormat(final String value) {
    this.endpoint.timestampFormat(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTimestampFormat
  /**
   ** Returns separator character for Strings that provides more than one value.
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getTimestampFormat() {
    return this.endpoint.timestampFormat();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSimplePageControl
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
   */
  @ConfigurationProperty(order=20, operations={SearchOp.class}, displayMessageKey=Connector.Feature.SIMPLEPAGE_CONTROL_LABEL, helpMessageKey=Connector.Feature.SIMPLEPAGE_CONTROL_HINT)
  public final void setSimplePageControl(final boolean value) {
    this.endpoint.simplePageControl(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSimplePageControl
  /**
   ** Returns if the Simple Page Control should be used by a search operation.
   ** <p>
   ** Simple Page Control is not enabled by default.
   **
   ** @return                    <code>true</code> if Simple Page Control should
   **                            used by a search operation; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean getSimplePageControl() {
    return this.endpoint.simplePageControl();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVirtualListControl
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
   */
  @ConfigurationProperty(order=21, operations={SearchOp.class}, displayMessageKey=Connector.Feature.VIRUALLIST_CONTROL_LABEL, helpMessageKey=Connector.Feature.VIRUALLIST_CONTROL_HINT)
  public final void setVirtualListControl(final boolean value) {
    this.endpoint.virtualListControl(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVirtualListControl
  /**
   ** Returns if the Virtual List Control should be used by a search operation.
   ** <p>
   ** Virtual List Control is not enabled by default.
   **
   ** @return                    <code>true</code> if Virtual List Control
   **                            should used by a search operation; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean getVirtualListControl() {
    return this.endpoint.virtualListControl();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSchemaContainer
  /**
   ** Sets the value where a V3 compliant Directory Service will publish schema
   ** entries.
   **
   ** @param  value              the value where a V3 compliant Directory
   **                            Service will publish schema entries.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=22, displayMessageKey=Connector.Feature.SCHEMA_CONTAINER_LABEL, helpMessageKey=Connector.Feature.SCHEMA_CONTAINER_HINT)
  public final void setSchemaContainer(final String value) {
    this.endpoint.schemaContainer(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSchemaContainer
  /**
   ** Returns the value where a V3 compliant Directory Service will publish schema
   ** entries.
   **
   ** @return                    the value where a V3 compliant Directory
   **                            Service will publish schema entries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getSchemaContainer() {
    return this.endpoint.schemaContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCatalogContainer
  /**
   ** Sets the value where a V3 compliant Directory Service will publish catalog
   ** entries.
   **
   ** @param  value              the value where a V3 compliant Directory
   **                            Service will publish catalog entries.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=23, displayMessageKey=Connector.Feature.CATALOG_CONTAINER_LABEL, helpMessageKey=Connector.Feature.CATALOG_CONTAINER_HINT)
  public final void setCatalogContainer(final String value) {
    this.endpoint.catalogContainer(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCatalogContainer
  /**
   ** Returns the value where a V3 compliant Directory Service will publish catalog
   ** entries.
   **
   ** @return                    the value where a V3 compliant Directory
   **                            Service will publish catalog entries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getCatalogContainer() {
    return this.endpoint.catalogContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setChangeLogContainer
  /**
   ** Sets the value where a V3 compliant Directory Service will publish
   ** changeLog entries.
   **
   ** @param  value              the value where a V3 compliant Directory
   **                            Service will publish changeLog entries.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=24, operations={SyncOp.class}, displayMessageKey=Connector.Feature.CHANGELOG_CONTAINER_LABEL, helpMessageKey=Connector.Feature.CHANGELOG_CONTAINER_HINT)
  public final void setChangeLogContainer(final String value) {
    this.endpoint.changeLogContainer(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getChangeLogContainer
  /**
   ** Returns the value where a V3 compliant Directory Service will publish changeLog
   ** entries.
   **
   ** @return                    the value where a V3 compliant Directory
   **                            Service will publish changeLog entries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getChangeLogContainer() {
    return this.endpoint.changeLogContainer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setChangeLogChangeType
  /**
   ** Sets the attribute name of the change type that a V3 compliant Directory
   ** Service provides in the changelog.
   **
   ** @param  value              the attribute name of the change type that a
   **                            V3 compliant Directory Service provides in the
   **                            changelog.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=25, operations={SyncOp.class}, displayMessageKey=Connector.Feature.CHANGELOG_CHANGETYPE_LABEL, helpMessageKey=Connector.Feature.CHANGELOG_CHANGETYPE_HINT)
  public final void setChangeLogChangeType(final String value) {
    this.endpoint.changeLogChangeType(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getChangeLogChangeType
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
  public final String getChangeLogChangeType() {
    return this.endpoint.changeLogChangeType();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setChangeLogChangeNumber
  /**
   ** Sets the attribute name of the change number that a V3 compliant Directory
   ** Service provides in the changelog.
   **
   ** @param  value              the attribute name of the change number that a
   **                            V3 compliant Directory Service provides in the
   **                            changelog.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=26, operations={SyncOp.class}, displayMessageKey=Connector.Feature.CHANGELOG_CHANGENUMBER_LABEL, helpMessageKey=Connector.Feature.CHANGELOG_CHANGENUMBER_HINT)
  public final void setChangeLogChangeNumber(final String value) {
    this.endpoint.changeLogChangeNumber(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getChangeLogChangeNumber
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
  public final String getChangeLogChangeNumber() {
    return this.endpoint.changeLogChangeNumber();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setChangeLogTargetGUID
  /**
   ** Sets the attribute name to access the global uid that a V3 compliant
   ** Directory Service provides in the changelog.
   **
   ** @param  value              the attribute name to access the global uid
   **                            that a V3 compliant Directory Service provides
   **                            in the changelog.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=27, operations={SyncOp.class}, displayMessageKey=Connector.Feature.CHANGELOG_TARGETGUID_LABEL, helpMessageKey=Connector.Feature.CHANGELOG_TARGETGUID_HINT)
  public final void setChangeLogTargetGUID(final String value) {
    this.endpoint.changeLogTargetGUID(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getChangeLogTargetGUID
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
  public final String getChangeLogTargetGUID() {
    return this.endpoint.changeLogTargetGUID();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setChangeLogTargetDN
  /**
   ** Sets the attribute name to access the distinguished name that a V3
   ** compliant Directory Service provides in the changelog.
   **
   ** @param  value              the attribute name to access the distinguished
   **                            name that a V3 compliant Directory Service
   **                            provides in the changelog.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=28, operations={SyncOp.class}, displayMessageKey=Connector.Feature.CHANGELOG_TARGETDN_LABEL, helpMessageKey=Connector.Feature.CHANGELOG_TARGETDN_HINT)
  public final void setChangeLogTargetDN(final String value) {
    this.endpoint.changeLogTargetDN(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getChangeLogTargetDN
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
  public final String getChangeLogTargetDN() {
    return this.endpoint.changeLogTargetDN();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setObjectClassName
  /**
   ** Sets the name of the object class attribute of an entry in a V3 compliant
   ** Directory Service.
   **
   ** @param  value              the name of the object class attribute of an
   **                            entry in a V3 compliant Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=29, displayMessageKey=Connector.Feature.OBJECTCLASS_NAME_LABEL, helpMessageKey=Connector.Feature.OBJECTCLASS_NAME_HINT)
  public final void setObjectClassName(final String value) {
    this.endpoint.objectClassName(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getObjectClassName
  /**
   ** Returns the name of the object class attribute of an entry in a V3 compliant
   ** Directory Service.
   **
   ** @return                    the name of the object class attribute of an
   **                            entry in a V3 compliant Directory Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getObjectClassName() {
    return this.endpoint.objectClassName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBinary
  /**
   ** Sets the name of the object attributes that are binary attributes of an
   ** entry in the Directory Service.
   ** <p>
   ** The toolkit is programmed to recognize the following set of common
   ** Directory Service binary attributes:
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
   ** <p>
   ** If there has to be specified more than one attribute the list of these
   ** attributes has to be delimitted by the <code>|</code> (pipe) character.
   **
   ** @param  value              the name of the object attributes that are
   **                            binary attributes of an entry in the Directory
   **                            Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=30, displayMessageKey=Connector.Feature.BINARY_LABEL, helpMessageKey=Connector.Feature.BINARY_HINT)
  public final void setBinary(final String value) {
    // a value passed in is never null but can be empty
    // avoid creation of wast
    if (StringUtility.blank(value)) {
      this.endpoint.binary().clear();
    }
    else {
      this.endpoint.binary(CollectionUtility.set(value.split("\\|")));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBinary
  /**
   ** Returns the name of the object attributes that are binary attributes of an
   ** entry in the Directory Service.
   ** <p>
   ** The toolkit is programmed to recognize the following set of common
   ** Directory Service binary attributes:
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
   ** @return                    the name of the object attributes that are
   **                            binary attributes of an entry in the Directory
   **                            Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getBinary() {
    return (CollectionUtility.empty(this.endpoint.binary())) ? SystemConstant.EMPTY : this.endpoint.binary().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDistinguishedName
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
   */
  @ConfigurationProperty(order=31, displayMessageKey=Connector.Feature.DISTINGUISHED_NAME_LABEL, helpMessageKey=Connector.Feature.DISTINGUISHED_NAME_HINT)
  public final void setDistinguishedName(final String value) {
    this.endpoint.distinguishedName(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDistinguishedName
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
  public final String getDistinguishedName() {
    return this.endpoint.distinguishedName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDistinguishedNamePrefix
  /**
   ** Sets the name of the distinguished name attribute prefix of an entry in a
   ** V3 compliant Directory Service.
   ** <p>
   ** If there has to be specified more than one attribute the list of these
   ** attributes has to be delimitted by the <code>|</code> (pipe) character.
   **
   ** @param  value              the name of the distinguished name attribute
   **                            prefix of an entry in a V3 compliant Directory
   **                            Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=32, displayMessageKey=Connector.Feature.DISTINGUISHED_NAME_PREFIX_LABEL, helpMessageKey=Connector.Feature.DISTINGUISHED_NAME_PREFIX_HINT)
  public final void setDistinguishedNamePrefix(final String value) {
    this.endpoint.distinguishedNamePrefix(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDistinguishedNamePrefix
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
  public final String getDistinguishedNamePrefix() {
    return this.endpoint.distinguishedNamePrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntryIdentifierPrefix
  /**
   ** Set the prefix of the identifier attribute of an Directory Service entry.
   **
   ** @param  value              the prefix of the identifier attribute of an
   **                            Directory Service entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=33, displayMessageKey=Connector.Feature.ENTRY_IDENTIFIER_PREFIX_LABEL, helpMessageKey=Connector.Feature.ENTRY_IDENTIFIER_PREFIX_HINT)
  public final void setEntryIdentifierPrefix(final String value) {
    this.endpoint.entryIdentifierAttribute(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntryIdentifierPrefix
  /**
   ** Returns the prefix of the identifier attribute of an Directory Service
   ** entry.
   **
   ** @return                    the prefix of the identifier attribute of an
   **                            Directory Service entry.
   **                            <br>
   **                            Should never be null.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getEntryIdentifierPrefix() {
    return this.endpoint.entryIdentifierAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntryStatusPrefix
  /**
   ** Set the prefix of the status attribute of an Directory Service entry.
   **
   ** @param  value              the prefix of the status attribute of an
   **                            Directory Service entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=34, displayMessageKey=Connector.Feature.ENTRY_STATUS_PREFIX_LABEL, helpMessageKey=Connector.Feature.ENTRY_STATUS_PREFIX_HINT)
  public final void setEntryStatusPrefix(final String value) {
    this.endpoint.entryStatusAttribute(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntryStatusPrefix
  /**
   ** Returns the prefix of the status attribute of an Directory Service entry.
   **
   ** @return                    the prefix of the status attribute of an
   **                            Directory Service entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getEntryStatusPrefix() {
    return this.endpoint.entryStatusAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntryCreatorPrefix
  /**
   ** Set the prefix of the attribute to detect the creator name of an
   ** Directory Service entry.
   **
   ** @param  value              the prefix of the attribute to detect the
   **                            creator name of an Directory Service entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=35, operations={SearchOp.class, SyncOp.class}, displayMessageKey=Connector.Feature.ENTRY_CREATOR_PREFIX_LABEL, helpMessageKey=Connector.Feature.ENTRY_CREATOR_PREFIX_HINT)
  public final void setEntryCreatorPrefix(final String value) {
    this.endpoint.entryCreatorAttribute(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntryCreatorPrefix
  /**
   ** Returns the prefix of the attribute to detect the creator name of an
   ** Directory Service entry.
   **
   ** @return                    the prefix of the attribute to detect the
   **                            creator name of an Directory Service entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getEntryCreatorPrefix() {
    return this.endpoint.entryCreatorAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntryCreatedPrefix
  /**
   ** Set the prefix of the attribute to detect the created timestamp of an
   ** Directory Service entry.
   **
   ** @param  value              the prefix of the attribute to detect the
   **                            created timestamp of an Directory Service
   **                            entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=36, operations={SearchOp.class, SyncOp.class}, displayMessageKey=Connector.Feature.ENTRY_CREATED_PREFIX_LABEL, helpMessageKey=Connector.Feature.ENTRY_CREATED_PREFIX_HINT)
  public final void setEntryCreatedPrefix(final String value) {
    this.endpoint.entryCreatedAttribute(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntryCreatedPrefix
  /**
   ** Returns the prefix of the attribute to detect the created timestamp of an
   ** Directory Service entry.
   **
   ** @return                    the prefix of the attribute to detect the
   **                            created timestamp of an Directory Service
   **                            entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getEntryCreatedPrefix() {
    return this.endpoint.entryCreatedAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntryModifierPrefix
  /**
   ** Set the prefix of the attribute to detect the modifier name of an
   ** Directory Service entry.
   **
   ** @param  value              the prefix of the attribute to detect the
   **                            modifier name of an Directory Service entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=37, operations={SearchOp.class, SyncOp.class}, displayMessageKey=Connector.Feature.ENTRY_MODIFIER_PREFIX_LABEL, helpMessageKey=Connector.Feature.ENTRY_MODIFIER_PREFIX_HINT)
  public final void setEntryModifierPrefix(final String value) {
    this.endpoint.entryModifierAttribute(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntryModifierPrefix
  /**
   ** Returns the prefix of the attribute to detect the modifier timestamp of an
   ** Directory Service entry.
   **
   ** @return                    the prefix of the attribute to detect the
   **                            modifier name of an Directory Service entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getEntryModifierPrefix() {
    return this.endpoint.entryModifierAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntryModifiedPrefix
  /**
   ** Set the prefix of the attribute to detect the modified timestamp of an
   ** Directory Service entry.
   **
   ** @param  value              the prefix of the attribute to detect the
   **                            modified timestamp of an Directory Service
   **                            entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=38, operations={SearchOp.class, SyncOp.class}, displayMessageKey=Connector.Feature.ENTRY_MODIFIED_PREFIX_LABEL, helpMessageKey=Connector.Feature.ENTRY_MODIFIED_PREFIX_HINT)
  public final void setEntryModifiedPrefix(final String value) {
    this.endpoint.entryModifiedAttribute(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntryModifiedPrefix
  /**
   ** Returns the prefix of the attribute to detect the modified timestamp of an
   ** Directory Service entry.
   **
   ** @return                    the prefix of the attribute to detect the
   **                            modified timestamp of an Directory Service
   **                            entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getEntryModifiedPrefix() {
    return this.endpoint.entryModifiedAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRoleClass
  /**
   ** Sets the name of the role object class defined for a Directory
   ** Service.
   **
   ** @param  value              the name of the role object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=39, operations={CreateOp.class, SearchOp.class}, objectClasses="__ROLE__", displayMessageKey=Connector.Feature.ROLE_CLASS_LABEL, helpMessageKey=Connector.Feature.ROLE_CLASS_HINT)
  public final void setRoleClass(final String value) {
    // a value passed in is never null but can be empty hence avoid creation of
    // waste
    if (!StringUtility.blank(value)) {
      this.endpoint.roleClass(CollectionUtility.set(value.split("\\|")));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRoleClass
  /**
   ** Returns the name of the role object class defined for a Directory
   ** Service.
   **
   ** @return                    the name of the role object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getRoleClass() {
    return (CollectionUtility.empty(this.endpoint.roleClass())) ? SystemConstant.EMPTY : this.endpoint.roleClass().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRolePrefix
  /**
   ** Sets the prefix of the role object class defined for a Directory
   ** Service.
   **
   ** @param  value              the prefix of the role object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=40, operations={CreateOp.class, SearchOp.class}, objectClasses="__ROLE__", displayMessageKey=Connector.Feature.ROLE_PREFIX_LABEL, helpMessageKey=Connector.Feature.ROLE_PREFIX_HINT)
  public final void setRolePrefix(final String value) {
    this.endpoint.rolePrefix(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRolePrefix
  /**
   ** Returns the prefix of the role object class defined for a Directory
   ** Service.
   **
   ** @return                    the prefix of the role object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getRolePrefix() {
    return this.endpoint.rolePrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRoleMemberPrefix
  /**
   ** Sets the prefix of the attribute of a membership of an object within a role
   ** defined for a Directory Service.
   **
   ** @param  value              the prefix of the attribute of a membership of
   **                            an object within a role.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=41, displayMessageKey=Connector.Feature.ROLE_MEMBER_PREFIX_LABEL, helpMessageKey=Connector.Feature.ROLE_MEMBER_PREFIX_HINT)
  public final void setRoleMemberPrefix(final String value) {
    this.endpoint.roleMember(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRoleMemberPrefix
  /**
   ** Returns the prefix of the attribute of a membership of an object within a
   ** role defined for a Directory Service.
   **
   ** @return                    the prefix of the attribute of a membership of
   **                            an object within a role.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getRoleMemberPrefix() {
    return this.endpoint.roleMember();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setScopeClass
  /**
   ** Sets the name of the scope object class defined for a Directory
   ** Service.
   **
   ** @param  value              the name of the scope object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=42, operations={CreateOp.class, SearchOp.class}, objectClasses="__SCOPE__", displayMessageKey=Connector.Feature.SCOPE_CLASS_LABEL, helpMessageKey=Connector.Feature.SCOPE_CLASS_HINT)
  public final void setScopeClass(final String value) {
    // a value passed in is never null but can be empty hence avoid creation of
    // waste
    if (!StringUtility.blank(value)) {
      this.endpoint.scopeClass(CollectionUtility.set(value.split("\\|")));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getScopeClass
  /**
   ** Returns the name of the scope object class defined for a Directory
   ** Service.
   **
   ** @return                    the name of the scope object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getScopeClass() {
    return (CollectionUtility.empty(this.endpoint.scopeClass())) ? SystemConstant.EMPTY : this.endpoint.scopeClass().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setScopePrefix
  /**
   ** Sets the prefix of the scop object class defined for a Directory
   ** Service.
   **
   ** @param  value              the prefix of the scope object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=43, operations={CreateOp.class, SearchOp.class}, objectClasses="__SCOPE__", displayMessageKey=Connector.Feature.SCOPE_PREFIX_LABEL, helpMessageKey=Connector.Feature.SCOPE_PREFIX_HINT)
  public final void setScopePrefix(final String value) {
    this.endpoint.scopePrefix(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getScopePrefix
  /**
   ** Returns the prefix of the scope object class defined for a Directory
   ** Service.
   **
   ** @return                    the prefix of the scope object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getScopePrefix() {
    return this.endpoint.scopePrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setScopeMemberPrefix
  /**
   ** Sets the prefix of the attribute of a membership of an object within a
   ** scope defined for a Directory Service.
   **
   ** @param  value              the prefix of the attribute of a membership of
   **                            an object within a scope.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=44, displayMessageKey=Connector.Feature.SCOPE_MEMBER_PREFIX_LABEL, helpMessageKey=Connector.Feature.SCOPE_MEMBER_PREFIX_HINT)
  public final void setScopeMemberPrefix(final String value) {
    this.endpoint.scopeMember(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getScopeMemberPrefix
  /**
   ** Returns the prefix of the attribute of a membership of an object within a
   ** scope defined for a Directory Service.
   **
   ** @return                    the prefix of the attribute of a membership of
   **                            an object within a scope.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getScopeMemberPrefix() {
    return this.endpoint.scopeMember();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setGroupClass
  /**
   ** Sets the name of the group object class defined for a Directory
   ** Service.
   **
   ** @param  value              the name of the group object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=45, operations={CreateOp.class, SearchOp.class}, objectClasses="__GROUP__", displayMessageKey=Connector.Feature.GROUP_CLASS_LABEL, helpMessageKey=Connector.Feature.GROUP_CLASS_HINT)
  public final void setGroupClass(final String value) {
    // a value passed in is never null but can be empty hence avoid creation of
    // waste
    if (!StringUtility.blank(value)) {
      this.endpoint.groupClass(CollectionUtility.set(value.split("\\|")));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGroupClass
  /**
   ** Returns the name of the group object class defined for a Directory
   ** Service.
   **
   ** @return                    the name of the group object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getGroupClass() {
    return (CollectionUtility.empty(this.endpoint.groupClass())) ? SystemConstant.EMPTY : this.endpoint.groupClass().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setGroupPrefix
  /**
   ** Sets the prefix of the group object class defined for a Directory
   ** Service.
   **
   ** @param  value              the prefix of the group object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=46, displayMessageKey=Connector.Feature.GROUP_PREFIX_LABEL, helpMessageKey=Connector.Feature.GROUP_PREFIX_HINT)
  public final void setGroupPrefix(final String value) {
    this.endpoint.groupPrefix(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGroupPrefix
  /**
   ** Returns the prefix of the group object class defined for a Directory
   ** Service.
   **
   ** @return                    the prefix of the group object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getGroupPrefix() {
    return this.endpoint.groupPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setGroupMemberPrefix
  /**
   ** Sets the prefix of the attribute of a membership of an object within a
   ** group defined for a Directory Service.
   **
   ** @param  value              the prefix of the attribute of a membership of
   **                            an object within a group.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=47, displayMessageKey=Connector.Feature.GROUP_MEMBER_PREFIX_LABEL, helpMessageKey=Connector.Feature.GROUP_MEMBER_PREFIX_HINT)
  public final void setGroupMemberPrefix(final String value) {
    this.endpoint.groupMember(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGroupMemberPrefix
  /**
   ** Returns the prefix of the attribute of a membership of an object within a
   ** group defined for a Directory Service.
   **
   ** @return                    the prefix of the attribute of a membership of
   **                            an object within a group.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getGroupMemberPrefix() {
    return this.endpoint.groupMember();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountClass
  /**
   ** Sets the names of the account object classes how they might defined in the
   ** connected Directory Service Schema.
   ** <p>
   ** If there has to be specified more than one object classe the list of
   ** these object classe has to be delimitted by the <code>|</code> (pipe)
   ** character.
   **
   ** @param  value              the names of the account object classes how
   **                            they might defined in the connected Directory
   **                            Service Schema.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=48, operations={CreateOp.class, SearchOp.class}, objectClasses="__ACCOUNT__", displayMessageKey=Connector.Feature.ACCOUNT_CLASS_LABEL, helpMessageKey=Connector.Feature.ACCOUNT_CLASS_HINT)
  public final void setAccountClass(final String value) {
    // a value passed in is never null but can be empty hence avoid creation of
    // waste
    if (!StringUtility.blank(value)) {
      this.endpoint.accountClass(CollectionUtility.set(value.split("\\|")));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountClass
  /**
   ** Returns the names of the account object classes how they might defined in
   ** the connected Directory Service Schema.
   ** <p>
   ** If there are more than one object classe the list of these object classes
   ** is separated by the <code>|</code> (pipe) character.
   **
   ** @return                    the names of the account object classes how
   **                            they might defined in the connected Directory
   **                            Service Schema.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getAccountClass() {
    return (CollectionUtility.empty(this.endpoint.accountClass())) ? SystemConstant.EMPTY : this.endpoint.accountClass().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountPrefix
  /**
   ** Sets the prefix of the account object class defined for a Directory
   ** Service.
   **
   ** @param  value              the prefix of the account object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=49, displayMessageKey=Connector.Feature.ACCOUNT_PREFIX_LABEL, helpMessageKey=Connector.Feature.ACCOUNT_PREFIX_HINT)
  public final void setAccountPrefix(final String value) {
    this.endpoint.accountPrefix(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountPrefix
  /**
   ** Returns the prefix of the account object class defined for a
   ** Directory Service.
   **
   ** @return                    the prefix of the account object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getAccountPrefix() {
    return this.endpoint.accountPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountMultiValue
  /**
   ** Sets the of names of the account multi-valued attributes defined in a
   ** Directory Service.
   ** <p>
   ** If there has to be specified more than one attribute the list of these
   ** attributes has to be delimitted by the <code>|</code> (pipe) character.
   **
   ** @param  value              the names of the multi-valued attributes for
   **                            object class account.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=50)
  public final void setAccountMultiValue(final String value) {
    // a value passed in is never null but can be empty hence avoid creation of
    // waste
    if (!StringUtility.blank(value)) {
      this.endpoint.accountMultiValue(CollectionUtility.set(value.split("\\|")));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountMultiValue
  /**
   ** Returns the names of the account multi-valued attributes defined in a
   ** Directory Service.
   **
   ** @return                    the names of the multi-valued attributes for
   **                            object class account.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getAccountMultiValue() {
    return (CollectionUtility.empty(this.endpoint.accountMultiValue())) ? SystemConstant.EMPTY : this.endpoint.accountMultiValue().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountRolePrefix
  /**
   ** Sets the prefix of the account role membership attribute defined for a
   ** Directory Service.
   **
   ** @param  value              the prefix of the account role membership
   **                            attribute defined for a Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=51, displayMessageKey=Connector.Feature.ACCOUNT_ROLE_PREFIX_LABEL, helpMessageKey=Connector.Feature.ACCOUNT_ROLE_PREFIX_HINT)
  public final void setAccountRolePrefix(final String value) {
    this.endpoint.accountRolePrefix(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountRolePrefix
  /**
   ** Returns the prefix of the account role membership attribute defined for
   ** a Directory Service.
   **
   ** @return                    the prefix of the account role membership
   **                            attribute defined for a Directory Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getAccountRolePrefix() {
    return this.endpoint.accountRolePrefix();
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountScopePrefix
  /**
   ** Sets the prefix of the account scope membership attribute defined for a
   ** Directory Service.
   **
   ** @param  value              the prefix of the account scope membership
   **                            attribute defined for a Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=52, displayMessageKey=Connector.Feature.ACCOUNT_SCOPE_PREFIX_LABEL, helpMessageKey=Connector.Feature.ACCOUNT_SCOPE_PREFIX_HINT)
  public final void setAccountScopePrefix(final String value) {
    this.endpoint.accountRolePrefix(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountScopePrefix
  /**
   ** Returns the prefix of the account scope membership attribute defined for
   ** a Directory Service.
   **
   ** @return                    the prefix of the account scope membership
   **                            attribute defined for a Directory Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getAccountScopePrefix() {
    return this.endpoint.accountRolePrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountGroupPrefix
  /**
   ** Sets the prefix of the account group membership attribute defined for a
   ** Directory Service.
   **
   ** @param  value              the prefix of the account group membership
   **                            attribute defined for a Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=53, displayMessageKey=Connector.Feature.ACCOUNT_GROUP_PREFIX_LABEL, helpMessageKey=Connector.Feature.ACCOUNT_GROUP_PREFIX_HINT)
  public final void setAccountGroupPrefix(final String value) {
    this.endpoint.accountGroupPrefix(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountGroupPrefix
  /**
   ** Returns the prefix of the account group membership attribute defined
   ** for a Directory Service.
   **
   ** @return                    the prefix of the account group membership
   **                            attribute defined for a Directory Service.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getAccountGroupPrefix() {
    return this.endpoint.accountGroupPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountCredentialPrefix
  /**
   ** Sets the prefix of the password credential attribute of an object class
   ** entry how it might exits in the connected Directory Service Schema.
   **
   ** @param  value              the prefix of the password credential attribute
   **                            of an object class entry how it might exits in
   **                            the connected Directory Service Schema.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=54)
  public final void setAccountCredentialPrefix(final String value) {
    this.endpoint.accountCredential(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountCredentialPrefix
  /**
   ** Returns the prefix of the password credential attribute of an object class
   ** entry how it might exits in the connected Directory Service Schema.
   **
   ** @return                    the prefix of the password credential attribute
   **                            of an object class entry how it might exits in
   **                            the connected Directory Service Schema.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String getAccountCredentialPrefix() {
    return this.endpoint.accountCredential();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountCredentialQuoted
  /**
   ** Whether the password credential value needs to be quoted in the connected
   ** Directory Service.
   **
   ** @param  value              <code>true</code> if the password credential
   **                            value needs to be quoted in a Directory Service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=55)
  public final void setAccountCredentialQuoted(final boolean value) {
    this.endpoint.accountCredentialQuoted(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountCredentialQuoted
  /**
   ** Returns <code>true</code> if the password value needs to be quoted for a
   ** Directory Service.
   **
   ** @return                    <code>true</code> if the password credential
   **                            value needs to be quoted in a Directory Service.
   **                            <br>
   **                            Possible object <code>boolean</code>.
   */
  public final boolean getAccountCredentialQuoted() {
    return this.endpoint.accountCredentialQuoted();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntryStatusAttributes
  /**
   ** Sets the status related attributes and values in
   ** Directory Service.
   **
   ** @param  value              String value of the custom status attributes.
   *                             eg: attrName1::enabledValue1::disabledValue1|attrName2::enabledValue2::disabledValue2
   */
  @ConfigurationProperty(order=56)
  public final void setEntryStatusAttributes(final String value) {
    if (!StringUtility.blank(value)) {
      this.endpoint.entryStatusAttributes(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntryStatusAttributes
  /**
   ** Returns the status related attributes and values in
   ** Directory Service
   **
   ** @return               Returns the status related attributes and values in
   **                       Directory Service.
   */
  public final String getEntryStatusAttributes() {
    return this.endpoint.entryStatusAttributes().stream().map(DirectoryEndpoint.Bind.Status::toString).collect(Collectors.joining("|"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link DirectoryEndpoint} this configuration intialize.
   **
   ** @return                    the {@link DirectoryEndpoint} this
   **                            configuration initialize.
   **                            <br>
   **                            Possible object {@link DirectoryEndpoint}.
   */
  public final DirectoryEndpoint endpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractConfiguration)
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
   */
  @Override
  public void validate() {
    try {
      // intentionally left blank
      this.endpoint.validate();
    }
    catch (SystemException e) {
      DirectoryService.propagate(e);
    }
  }
}