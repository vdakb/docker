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

    File        :   AbstractResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcITResourceNotFoundException;

import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import com.oracle.oim.gcp.pool.PoolConfiguration;

import oracle.iam.platform.Platform;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ITResource;

////////////////////////////////////////////////////////////////////////////////
// class AbstractResource
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractResource</code> implements the base functionality
 ** of a Oracle Identity Manager <code>IT Resource</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AbstractResource extends AbstractObject {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the timeout period for establishment of the connection.
   */
  public static final int     CONNECTION_TIMEOUT_DEFAULT        = 1000;

  /**
   ** Default value of the number of consecutive attempts to be made at
   ** establishing a connection with the target system.
   */
  public static final int     CONNECTION_RETRY_COUNT_DEFAULT    = 2;

  /**
   ** Default value of the interval (in milliseconds) between consecutive
   ** attempts at establishing a connection with the target system.
   */
  public static final int     CONNECTION_RETRY_INTERVAL_DEFAULT = 1000;

  /**
   ** Default value of the timeout period the target system doesn't response.
   */
  public static final int     RESPONSE_TIMEOUT_DEFAULT          = 10000;

  /**
   ** Default value of the preferred connection pooling implementation.
   */
  public static final String  POOL_PREFERENCE_DEFAULT           = "Default";

  /**
   ** Default value of the preferred connection pooling implementation.
   */
  public static final String  POOL_PREFERENCE_NATIVE            = "Native";

  /**
   ** Default value of the minimum number of connections that must be in the
   ** pool at any point of time.
   */
  public static final int     POOL_MINIMUM_SIZE_DEFAULT         = 5;

  /**
   ** Default value of the maximum number of connections that must be
   ** established in the pool at any point of time.
   */
  public static final int     POOL_MAXIMUM_SIZE_DEFAULT         = 100;

  /**
   ** Default value of the number of connections that must be established when
   ** the connection pool is initialized.
   */
  public static final int     POOL_INITIAL_SIZE_DEFAULT         = 1;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> if
   ** connections have to be validated before they are lent by the pool.
   */
  public static final boolean POOL_VALIDATE_ONBORROW_DEFAULT    = false;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the maximum time (in seconds) for which the connector must wait
   ** for a connection to be available.
   */
  public static final int     POOL_TIMEOUT_DEFAULT              = 60;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the time interval (in seconds) at which the other timeouts
   ** specified by the other parameters must be checked.
   */
  public static final int     POOL_VALIDATE_INTERVAL_DEFAULT    = 30;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the time (in seconds) of inactivity after which a connection must
   ** be dropped and replaced by a new connection in the pool.
   */
  public static final int     POOL_INACTIVE_TIMEOUT_DEFAULT     = 600;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specify the time (in seconds) after which a connection must be
   ** automatically closed if it is not returned to the pool.
   */
  public static final int     POOL_ABANDONED_TIMEOUT_DEFAULT    = 600;

  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** hold the name of the <code>IT Resource</code> refering to a remote
   ** deployed Connector Server.
   */
  public static final String  CONNECTOR_SERVER                  = "Connector Server";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the target system where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String  SERVER_NAME                       = "Server Name";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the port of the target system where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String  SERVER_PORT                       = "Server Port";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the path to the Metadata Descriptor which is specifying the feature
   ** descriptor of the <code>IT Resource</code>.
   */
  public static final String  SERVER_FEATURE                    = "Server Feature";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the root context where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String  ROOT_CONTEXT                      = "Root Context";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify if you plan to configure SSL to secure communication between
   ** Identity Manager and the target system.
   */
  public static final String  SECURE_SOCKET                     = "Secure Socket";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the user name of the target system account to be used to establish
   ** a connection.
   */
  public static final String  PRINCIPAL_NAME                    = "Principal Name";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the password of the target system account specified by the
   ** #PRINCIPAL_NAME parameter.
   */
  public static final String  PRINCIPAL_PASSWORD                = "Principal Password";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the name of
   ** language the target system is using.
   */
  public static final String  LOCALE_LANGUAGE                   = "Locale Language";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of language region the target system is using.
   */
  public static final String  LOCALE_COUNTRY                    = "Locale Country";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of time zone the target system is using.
   */
  public static final String  LOCALE_TIMEZONE                   = "Locale TimeZone";

  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specifiy the time (in milliseconds) within which the target system is
   ** expected to respond to a connection attempt.
   */
  public static final String  CONNECTION_TIMEOUT                = "Connection Timeout";

  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specifiy the number of consecutive attempts to be made at establishing a
   ** connection with the target system.
   */
  public static final String  CONNECTION_RETRY_COUNT            = "Connection Retry Count";

  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specifiy the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the target system.
   */
  public static final String  CONNECTION_RETRY_INTERVAL         = "Connection Retry Interval";

  /**
   ** Attribute tag which may be defined on a <code>IT Resource</code> to
   ** specify the timeout period the service provider doesn't get a response.
   ** <p>
   ** If this property has not been specified, the default is to wait for the
   ** response until it is received.
   */
  public static final String  RESPONSE_TIMEOUT                  = "Response Timeout";

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the preferred connection pooling implementation.
   */
  public static final String  POOL_SUPPORTED                    = PoolConfiguration.CONN_POOL_SUPPORTED;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the preferred connection pooling implementation.
   */
  public static final String  POOL_PREFERENCE                   = PoolConfiguration.POOL_PREFERENCE;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specify the concrete implementation of the <code>ResourceConnection</code>
   ** class.
   */
  public static final String  POOL_ONLY_ONE_CONNECTION          = PoolConfiguration.TARGET_SUPPORTS_ONE_CONN;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the minimum number of connections that must be in the pool at any
   ** point of time.
   */
  public static final String  POOL_MINIMUM_SIZE                 = PoolConfiguration.MIN_POOL_SIZE;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the maximum number of connections that must be established in the
   ** pool at any point of time.
   */
  public static final String  POOL_MAXIMUM_SIZE                 = PoolConfiguration.MAX_POOL_SIZE;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the number of connections that must be established when the
   ** connection pool is initialized.
   */
  public static final String  POOL_INITIAL_SIZE                 = PoolConfiguration.INITIAL_POOL_SIZE;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** if you want connections to be validated before they are lent by the pool.
   */
  public static final String  POOL_VALIDATE_ONBORROW            = PoolConfiguration.VALIDATE_CONN_ON_BORROW;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the maximum time (in seconds) for which the connector must wait
   ** for a connection to be available.
   */
  public static final String  POOL_TIMEOUT                      = PoolConfiguration.CONN_WAIT_TIMEOUT;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the time interval (in seconds) at which the other timeouts
   ** specified by the other parameters must be checked.
   */
  public static final String  POOL_VALIDATE_INTERVAL            = PoolConfiguration.VALIDATE_CONN_ON_BORROW;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specifiy the time (in seconds) of inactivity after which a connection must
   ** be dropped and replaced by a new connection in the pool.
   */
  public static final String  POOL_INACTIVE_TIMEOUT             = PoolConfiguration.INACTIVE_CONN_TIMEOUT;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specify the time (in seconds) after which a connection must be
   ** automatically closed if it is not returned to the pool.
   */
  public static final String  POOL_ABANDONED_TIMEOUT            = PoolConfiguration.ABANDON_CONN_TIMEOUT;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specify the concrete implementation of the <code>ResourceConnection</code>
   ** class.
   */
  public static final String  POOL_RESOURCE_CLASS               = PoolConfiguration.RESOURCECONNECTION_CLASS_DEFINITION;

  /**
   ** Attribute tag which may be defined on a <code>IT Resource</code> to
   ** specify the wrapper to the native pool mechanism that implements the
   ** GenericPool. Set a value only if the pool preference is set to Native.
   */
  public static final String  POOL_NATIVE_CLASS                 = PoolConfiguration.NATIVEPOOL_CLASS_DEFINITION;

  /**
   ** Attribute tag which must be defined on a <code>IT Resource</code> to
   ** specify the comma-separated list of fields not needed for creating a
   ** connection. When any of the specified fields are updated, the pool is not
   ** refreshed.
   */
  public static final String  POOL_EXCUDED_FIELDS               = "Pool excluded fields";

  /**
   ** Maping attribute passed in by the <code>Generic Connection Pool</code>
   ** management if this <code>IT Resource</code> is indirectly configured.
   */
  public static final String  POOL_CONNECTION_NAME              = "svr_name";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the key of the IT Resource instance where this wrapper belongs to */
  private long               instance;

  /** the name of the IT Resource instance where this wrapper belongs to */
  private String             name;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractResource</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The <code>IT Resource</code> specified does not belong a
   ** {@link AbstractTask} thus it has to populated from manually.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   */
  protected AbstractResource(final Loggable loggable) {
    // ensure inheritance
    super(loggable);

    // initialize instance
    this.instance = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractResource</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            IT Resource instance where this wrapper belongs
   **                            to.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public AbstractResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    // initialize instance
    this.instance = -1L;

    // if the Generic Connection Pool mechanism is instantiating this IT
    // Resource obtain the name from the mapping passed in and remove it
    if (parameter.containsKey(POOL_CONNECTION_NAME)) {
      this.name = parameter.remove(POOL_CONNECTION_NAME);
    }

    // obtain declared attributes
    for (AbstractAttribute cursor : attributes()) {
      this.attribute.put(cursor.id(), parameter.get(cursor.id()));
    }
    validateAttributes(attributes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractResource</code> which is associated
   ** with the specified {@link Loggable}.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Oracle Identity Manager and also all well known
   ** attributes using the specified <code>instance</code> key.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            Allowed object {@link Long}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries for
   **                            the given key or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  public AbstractResource(final Loggable loggable, final Long instance)
    throws TaskException {

    // ensure inheritance
    this(loggable);

    // initialize instance
    find(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractResource</code> which is associated
   ** with the specified task and belongs to the <code>IT Resource</code>
   ** specified by the given name.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Oracle Identity Manager and also all well known
   ** attributes using the specified <code>instance</code> name.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  instance           the public identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            Allowed object {@link String}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries for
   **                            the given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  public AbstractResource(final Loggable loggable, final String instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the instance key of the <code>IT Resource</code> where this
   ** wrapper belongs to.
   **
   ** @return                    the instance key of the
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   **                            Possible object <code>long</code>.
   */
  public final long instance() {
    return this.instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the instance name of the <code>IT Resource</code> where this wrapper
   ** belongs to.
   **
   ** @param  name               the instance name of the
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   **                            Allowed object {@link String}.
   */
  public final void name(final String name) {
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the instance name of the <code>IT Resource</code> where this
   ** wrapper belongs to.
   **
   ** @return                    the instance name of the
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   **                            Possible object {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorServer
  /**
   ** Returns the name of the <code>IT Resource</code> refering to a remote
   ** deployed Connector Server.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #CONNECTOR_SERVER}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the name of the <code>IT Resource</code>
   **                            refering to a remote deployed Connector Server.
   */
  public String connectorServer() {
    return stringValue(CONNECTOR_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the server where the target system is deployed on and
   ** this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_NAME}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the name of the server where the target system
   **                            is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String serverName() {
    return stringValue(SERVER_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the listener port of the server the target system is deployed on
   ** and this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PORT}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the listener port of the server the target
   **                            system is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  public int serverPort() {
    return integerValue(SERVER_PORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the listener port of the server the target system is deployed on
   ** and this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PORT}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the server port.
   **                            <br>
   **                            Allowed object <code>int</code>.
   **
   ** @return                    the listener port of the server the target
   **                            system is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  public int serverPort(final int defaultValue) {
    return integerValue(SERVER_PORT, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   feature
  /**
   ** Returns the name of the feature mapping of the target system where
   ** this <code>IT Resource</code> will be working on.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_FEATURE}.
   **
   ** @return                    the name of the feature mapping of the
   **                            target system.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String feature() {
    return stringValue(SERVER_FEATURE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns the <code>true</code> if the server is using a secure protocol.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURE_SOCKET}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    <code>true</code> if the server is using
   **                            a ecure protocol; otherwise <code>false</code>.
   **                            Possible object <code>boolean</code>.
   **                            <br>
   **                            Possible object <code>boolean</code>.
   */
  public boolean secureSocket() {
    return booleanValue(SECURE_SOCKET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the name of the principal of a target system to establish a
   ** connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PRINCIPAL_NAME}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the name of the principal to establish a
   **                            connection.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String principalName() {
    return stringValue(PRINCIPAL_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password for the principal of a target system to establish
   ** a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PRINCIPAL_PASSWORD}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the password for the principal of a target
   **                            system to establish a connection.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String principalPassword() {
    return stringValue(PRINCIPAL_PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Returns the name of the root context in a target system where this
   ** <code>IT Resource</code> will be working on
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #ROOT_CONTEXT}.
   **
   ** @return                    the name of the root context in a target
   **                            system.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String rootContext() {
    return stringValue(ROOT_CONTEXT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeLanguage
  /**
   ** Returns the language code a target system used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #LOCALE_LANGUAGE}.
   **
   ** @return                    the language code a target system used to
   **                            connect to.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String localeLanguage() {
    return stringValue(LOCALE_LANGUAGE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeCountry
  /**
   ** Returns the country code a target system used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #LOCALE_COUNTRY}.
   **
   ** @return                    the country code a target system used to
   **                            connect to.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String localeCountry() {
    return stringValue(LOCALE_COUNTRY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeTimeZone
  /**
   ** Returns the time zone of the a target system used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #LOCALE_TIMEZONE}.
   **
   ** @return                    the time zone of the a target system used to
   **                            connect to.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String localeTimeZone() {
    return stringValue(LOCALE_TIMEZONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   poolSupported
  /**
   ** Returns <code>true</code> if connection pooling is enabled for this
   ** <code>IT Resource</code> installation.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #POOL_SUPPORTED}.
   **
   ** @return                    <code>true</code> if connection pooling is
   **                            enabled for this <code>IT Resource</code>
   **                            installation.
   **                            <br>
   **                            Possible object <code>boolean</code>.
   */
  public final boolean poolSupported() {
    return booleanValue(POOL_SUPPORTED, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionTimeout
  /**
   ** Returns the timeout period for establishment of the
   ** <code>IT Resource</code> connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an
   ** target system. When connection pooling has been requested, this property
   ** also specifies the maximum wait time or a connection when all connections
   ** in pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the target system provider will
   ** wait indefinitely for a pooled connection to become available, and to wait
   ** for the default TCP timeout to take effect when creating a new connection.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #CONNECTION_TIMEOUT}.
   ** <p>
   ** If {@link #CONNECTION_TIMEOUT} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #CONNECTION_TIMEOUT_DEFAULT}
   **
   ** @return                    the timeout period for establishment of the
   **                            <code>IT Resource</code> connection.
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  public final int connectionTimeout() {
    return integerValue(CONNECTION_TIMEOUT, CONNECTION_TIMEOUT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionRetryCount
  /**
   ** Returns the number of consecutive attempts to be made at establishing a
   ** connection with the <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #CONNECTION_RETRY_COUNT}.
   ** <p>
   ** If {@link #CONNECTION_RETRY_COUNT} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #CONNECTION_RETRY_COUNT_DEFAULT}
   **
   ** @return                    the timeout period for establishment of the
   **                            <code>IT Resource</code> connection.
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  public final int connectionRetryCount() {
    return integerValue(CONNECTION_RETRY_COUNT, CONNECTION_RETRY_COUNT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionRetryInterval
  /**
   ** Returns the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #CONNECTION_RETRY_INTERVAL}.
   ** <p>
   ** If {@link #CONNECTION_RETRY_INTERVAL} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #CONNECTION_RETRY_INTERVAL_DEFAULT}
   **
   ** @return                    the timeout period for establishment of the
   **                            <code>IT Resource</code> connection.
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  public final int connectionRetryInterval() {
    return integerValue(CONNECTION_RETRY_INTERVAL, CONNECTION_RETRY_INTERVAL_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseTimeout
  /**
   ** Returns the timeout period the target system doesn't get a response.
   ** <p>
   ** When an request is made by a client to a server and the server does not
   ** respond for some reason, the client waits forever for the server to
   ** respond until the TCP timeouts. On the client-side what the user
   ** experiences is esentially a process hang. In order to control the request
   ** in a timely manner, a read timeout can be configured for the service
   ** provider.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #RESPONSE_TIMEOUT}.
   ** <p>
   ** If {@link #RESPONSE_TIMEOUT} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #RESPONSE_TIMEOUT_DEFAULT}.
   **
   ** @return                    the timeout period the target system doesn't
   **                            get a response.
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  public final int responseTimeout() {
    return integerValue(RESPONSE_TIMEOUT, RESPONSE_TIMEOUT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractObject)
  /**
   ** Returns the array with names which should be populated from the
   ** IT Resource definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  public AbstractAttribute[] attributes()
    throws TaskException {

    return new AbstractAttribute[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateAttributes (AbstractObject)
  /**
   ** Populates all well known attributes defined on an
   ** <code>IT Resource</code>.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the <code>IT Resource</code> does no
   **                                  longer exists in Oracle Identity Manager
   **                                  meta data entries with the key populated
   **                                  by {@link #populateInstance(String)} of
   **                                  this wrapper.
   **                              <li>one of the expected field names was not
   **                                  defined on the <code>IT Resource</code>
   **                                  instance.
   **                              <li>a generell error has occurred.
   **                            </ul>
   */
  @Override
  public void populateAttributes(final String instanceName)
    throws TaskException {

    final tcITResourceInstanceOperationsIntf facade = Platform.getService(tcITResourceInstanceOperationsIntf.class);
    populateAttributes(facade, instanceName);
    facade.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateInstance (AbstractObject)
  /**
   ** Obtains the <code>IT Resource</code> definition from Oracle Identity
   ** Manager.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            <code>IT Resource</code> Definition where this
   **                            wrapper belongs to.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the <code>IT Resource</code> does no
   **                                  longer exists in Oracle Identity Manager
   **                                  meta data entries with the key populated
   **                                  by {@link #populateInstance(String)} of
   **                                  this wrapper.
   **                              <li>more than one instance was found for the
   **                                  name; seems to that data corruption has
   **                                  been occurred;
   **                              <li>the field <code>IT_RESOURCE_KEY</code>
   **                                  was not defined in the meta data entries
   **                                  of the <code>IT Resource</code>.
   **                              <li>a generell error has occurred.
   **                            </ul>
   **                            or a generell error has occurred.
   */
  @Override
  protected void populateInstance(final String instanceName)
    throws TaskException {

    final String method = "populateInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    final tcITResourceInstanceOperationsIntf facade = Platform.getService(tcITResourceInstanceOperationsIntf.class);
    try {
      final Map<String, Object> map = new HashMap<String, Object>();
      map.put(ITResource.NAME, instanceName);
      tcResultSet resultSet =  facade.findITResourceInstances(map);
      if (resultSet .getRowCount() != 1)
        throw new TaskException(resultSet.isEmpty() ? TaskError.ITRESOURCE_NOT_FOUND : TaskError.ITRESOURCE_AMBIGUOUS, instanceName);

      resultSet.goToRow(0);
      this.instance = fetchLongValue(resultSet,   ITResource.KEY);
      this.name     = fetchStringValue(resultSet, ITResource.NAME);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      facade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchLongValue
  /**
   ** Populates a well known attribute defined on an <code>IT Resource</code>
   ** using the provided <code>instanceName</code> to find the
   ** <code>IT Resource</code> and the specified <code>parameterName</code> to
   ** obtain the attribute.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   ** @param  parameterName      the name of the attribute where we are
   **                            interested in.
   **
   ** @return                    the value mapped at<code>parameterName</code>
   **                            for <code>IT Resource</code>
   **                            <code>instanceName</code>.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the <code>IT Resource</code> does no
   **                                  longer exists in Oracle Identity Manager
   **                                  meta data entries with the key populated
   **                                  by {@link #populateInstance(String)} of
   **                                  this wrapper.
   **                              <li>one of the expected field names was not
   **                                  defined on the <code>IT Resource</code>
   **                                  instance.
   **                              <li>a generell error has occurred.
   **                            </ul>
   */
  public static long fetchLongValue(final String instanceName, final String parameterName)
    throws TaskException {

    final Map<String, Object> filter = new HashMap<String, Object>();
    filter.put(ITResource.NAME, String.valueOf(instanceName));
    final tcITResourceInstanceOperationsIntf facade = Platform.getService(tcITResourceInstanceOperationsIntf.class);
    long value = -1L;
    try {
      tcResultSet resultSet =  facade.findITResourceInstances(filter);
      if (resultSet .getRowCount() != 1)
        throw new TaskException(resultSet.isEmpty() ? TaskError.ITRESOURCE_NOT_FOUND : TaskError.ITRESOURCE_AMBIGUOUS, instanceName);
      resultSet.goToRow(0);
      // fetch IT Resource instance metadata
      value = fetchLongValue(resultSet, parameterName);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      facade.close();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchStringValue
  /**
   ** Populates a well known attribute defined on an <code>IT Resource</code>
   ** using the provided <code>instanceName</code> to find the
   ** <code>IT Resource</code> and the specified <code>parameterName</code> to
   ** obtain the attribute.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   ** @param  parameterName      the name of the attribute where we are
   **                            interested in.
   **
   ** @return                    the value mapped at<code>parameterName</code>
   **                            for <code>IT Resource</code>
   **                            <code>instanceName</code>.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the <code>IT Resource</code> does no
   **                                  longer exists in Oracle Identity Manager
   **                                  meta data entries with the key populated
   **                                  by {@link #populateInstance(String)} of
   **                                  this wrapper.
   **                              <li>one of the expected field names was not
   **                                  defined on the <code>IT Resource</code>
   **                                  instance.
   **                              <li>a generell error has occurred.
   **                            </ul>
   */
  public static String fetchStringValue(final String instanceName, final String parameterName)
    throws TaskException {

    final Map<String, Object> filter = new HashMap<String, Object>();
    filter.put(ITResource.NAME, String.valueOf(instanceName));
    final tcITResourceInstanceOperationsIntf facade = Platform.getService(tcITResourceInstanceOperationsIntf.class);
    String value = null;
    try {
      tcResultSet resultSet =  facade.findITResourceInstances(filter);
      if (resultSet .getRowCount() != 1)
        throw new TaskException(resultSet.isEmpty() ? TaskError.ITRESOURCE_NOT_FOUND : TaskError.ITRESOURCE_AMBIGUOUS, instanceName);
      resultSet.goToRow(0);
      // fetch IT Resource instance metadata
      value = fetchStringValue(resultSet, parameterName);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      facade.close();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookup of an <code>IT Resource</code> definition from Oracle Identity
   ** Manager for the given instance key.
   **
   ** @param  instanceKey        the internal identifier of an
   **                            <code>IT Resource</code> in Oracle Identity
   **                            Manager.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the <code>IT Resource</code> is not
   **                                  defined in the Oracle Identity Manager
   **                                  meta data entries with the given key
   **                                  passed.
   **                              <li>more than one instance was found for the
   **                                  given key; seems to that data corruption
   **                                  has been occurred;
   **                              <li>a generell error has occurred.
   **                            </ul>
   */
  protected void find(final Long instanceKey)
    throws TaskException {

    final String method = "find";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Map<String, Object> filter = new HashMap<String, Object>();
    filter.put(ITResource.KEY, String.valueOf(instanceKey));
    final tcITResourceInstanceOperationsIntf facade = Platform.getService(tcITResourceInstanceOperationsIntf.class);
    try {
      tcResultSet resultSet =  facade.findITResourceInstances(filter);
      if (resultSet .getRowCount() != 1)
        throw new TaskException(resultSet.isEmpty() ? TaskError.ITRESOURCE_NOT_FOUND : TaskError.ITRESOURCE_AMBIGUOUS, String.valueOf(instanceKey));

      resultSet.goToRow(0);
      // fetch IT Resource instance metadata
      this.instance = fetchLongValue(resultSet,   ITResource.KEY);
      this.name     = fetchStringValue(resultSet, ITResource.NAME);
      // populate IT Resource instance attributes
      populateAttributes(facade, this.name);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      facade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateAttributes
  /**
   ** Obtains the <code>IT Resource</code> definition from Identity Manager.
   **
   ** @param  facade             the Business Facade instance to populate the
   **                            attributes of the <code>IT Resource</code>
   **                            which is mapped to <code>instanceName</code>.
   ** @param  instanceName       the name of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the <code>IT Resource</code> is not
   **                                  defined in the Identity Manager meta data
   **                                  entries with the name passed to
   **                                  the constructor of this wrapper
   **                              <li>more than one instance was found for the
   **                                  name; seems to that data corruption has
   **                                  been occurred;
   **                              <li>a generell error has occurred.
   **                            </ul>
   **                            or a generell error has occurred.
   */
  protected void populateAttributes(final tcITResourceInstanceOperationsIntf facade, final String instanceName)
    throws TaskException {

    final String method = "populate";
    trace(method, SystemMessage.METHOD_ENTRY);

    // clear the value holder
    this.attribute.clear();
    try {
      // obtain declared attributes
      final tcResultSet rs = facade.getITResourceInstanceParameters(this.instance);
      for (int i = 0; i < rs.getRowCount(); i++) {
        rs.goToRow(i);

        // get the name of the parameter from the IT Resource definition
        final String name = fetchStringValue(rs, ITResource.PARAMETER_NAME);
        this.attribute.put(name, fetchStringValue(rs, ITResource.PARAMETER_VALUE));
      }
      validateAttributes(attributes());
    }
    catch (tcITResourceNotFoundException e) {
      throw new TaskException(TaskError.ITRESOURCE_NOT_FOUND, instanceName);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      facade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes this mapping to the associated logger
   **
   ** @param method              the name of the implementing method where the
   **                            request originates to.
   */
  public void debug(final String method) {
    // produce the logging output only if the logging level is enabled for
    if (this.logger != null && this.logger.debugLevel())
      debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.toString()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateAttributes
  /**
   ** Populates all well known attributes defined on an
   ** <code>IT Resource</code>.
   **
   ** @param  attr               the array of attribute definition which needs
   **                            to be validated.
   **
   ** @throws TaskException      in case of one of the expected field names was
   **                            not defined on the <code>IT Resource</code>
   **                            instance or a generell error has occurred.
   */
  protected void validateAttributes(final AbstractAttribute[] attr)
    throws TaskException {

    final String method = "validateAttributes";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      // check if all attributes which are declared mandatory contained in the
      // attribute mapping and the optional attributes have a value if they are
      // not declared on the IT resource
      for (int i = 0; i < attr.length; i++) {
        final String id = attr[i].id();
        if (!this.attribute.containsKey(id)) {
          if (attr[i].optional())
            this.attribute.put(id, attr[i].defaultValue());
          else {
            attributeRequired(id);
          }
        }
        else {
          final Object value = attribute(id);
          // as a first step check if a value is availabe in general for
          // mandatory declared mapping
          if (attr[i].mandatory() && value == null) {
            attributeEmpty(id);
          }
          // as a second step check if a value is availabe for mandatory
          // declared mapping if it is a string definition
          else if (value instanceof String) {
            if (StringUtility.isEmpty((String)value)) {
              if (attr[i].optional())
                this.attribute.put(id, attr[i].defaultValue());
              else {
                attributeEmpty(id);
              }
            }
          }
        }
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeRequired
  /**
   ** Throw a {@link TaskException} with the key word
   ** {@link TaskError#ITRESOURCE_ATTRIBUTE_MISSING} and the specific attribute
   ** name as its parameter to inform the user about misconfigration of the
   ** <code>IT Resource</code>.
   **
   ** @param  name               the name of the attribute of the
   **                            <code>IT Resource</code> that raise the
   **                            exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      the {@link TaskException} with the information
   **                            about the misconfigration of the
   **                            <code>IT Resource</code>.
   */
  protected void attributeRequired(final String name)
    throws TaskException {

    final String[] arguments = {this.name, name};
    throw new TaskException(TaskError.ITRESOURCE_ATTRIBUTE_MISSING, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeEmpty
  /**
   ** Throw a {@link TaskException} with the key word
   ** {@link TaskError#ITRESOURCE_ATTRIBUTE_ISNULL} and the specific attribute
   ** name as its parameter to inform the user about misconfigration of the
   ** <code>IT Resource</code>.
   **
   ** @param  name               the name of the attribute of the
   **                            <code>IT Resource</code> that raise the
   **                            exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      the {@link TaskException} with the information
   **                            about the misconfigration of the
   **                            <code>IT Resource</code>.
   */
  protected void attributeEmpty(final String name)
    throws TaskException {

    final String[] arguments = {this.name, name};
    throw new TaskException(TaskError.ITRESOURCE_ATTRIBUTE_ISNULL, arguments);
  }
}