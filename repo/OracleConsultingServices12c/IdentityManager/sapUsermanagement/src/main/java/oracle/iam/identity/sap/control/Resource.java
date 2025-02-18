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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Resource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

import java.util.Map;

import com.sap.conn.jco.ext.DestinationDataProvider;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.AbstractTask;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.ITResourceAttribute;

////////////////////////////////////////////////////////////////////////////////
// class Resource
// ~~~~~ ~~~~~~~~
/**
 ** The <code>Resource</code> is the value holder for the IT Resource of a
 ** SAP R3 System assigned to the provisoning or reconciliation task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class Resource extends AbstractResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the name of
   ** the SAP/R3 Application Server host name or IP address where this IT
   ** Resource belongs to.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.ashost {@link DestinationDataProvider#JCO_ASHOST}
   **
   ** @see DestinationDataProvider
   */
  public static final String APPLICATION_SERVER_HOST    = "Application Server Host";

  /**
   ** Attribute tag which may be defined on an IT Resource to hold the name of
   ** the group of SAP/R3 Application Servers.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.group {@link DestinationDataProvider#JCO_GROUP}
   **
   ** @see DestinationDataProvider
   */
  public static final String APPLICATION_SERVER_GROUP   = "Application Server Group";

  /**
   ** Attribute tag which may be defined on an IT Resource to specify additional
   ** logon parameter to define the codepage type of the SAP/R3 System.
   ** <ul>
   **   <li>1 - non unicode
   **   <li>2 - unicode enabled
   ** </ul>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.group {@link DestinationDataProvider#JCO_GROUP}
   **
   ** @see DestinationDataProvider
   */
  public static final String APPLICATION_SERVER_UNICODE = "Application Server Unicode";

  /**
   ** Attribute tag which may be defined on an IT Resource to hold the route
   ** to the SAP/R3 Application Server host name or IP address where this IT
   ** Resource belongs to.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.saprouter {@link DestinationDataProvider#JCO_SAPROUTER}
   **
   ** @see DestinationDataProvider
   */
  public static final String APPLICATION_SERVER_ROUTE   = "Application Server Router";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the name of
   ** Metadata Descriptor which is specifying the feature descriptor of the
   ** SAP/R3 System.
   */
  public static final String APPLICATION_SERVER_FEATURE = "Application Server Feature";

  /**
   ** Attribute tag which may be defined on an IT Resource to hold the name of
   ** the SAP/R3 Message Server hostname or IP address where this IT Resource
   ** belongs to.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.mshost {@link DestinationDataProvider#JCO_MSHOST}
   **
   ** @see DestinationDataProvider
   */
  public static final String MESSAGE_SERVER_HOST        = "Message Server Host";

  /**
   ** Attribute tag which may be defined on an IT Resource to hold the SAP
   ** message service port where this IT Resource belongs to.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.msserv {@link DestinationDataProvider#JCO_MSSERV}
   **
   ** @see DestinationDataProvider
   */
  public static final String MESSAGE_SERVER_PORT        = "Message Server Port";

  /**
   ** Attribute tag which may be defined on an IT Resource to hold the name of
   ** the SAP/R3 Gateway hostname or IP address where this IT Resource belongs to.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.gwhost {@link DestinationDataProvider#JCO_GWHOST}
   **
   ** @see DestinationDataProvider
   */
  public static final String GATEWAY_SERVER_HOST        = "Gateway Server Host";

  /**
   ** Attribute tag which may be defined on an IT Resource to hold the name of
   ** the SAP/R3 Gateway service where this IT Resource belongs to.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.gwserv {@link DestinationDataProvider#JCO_GWSERV}
   **
   ** @see DestinationDataProvider
   */
  public static final String GATEWAY_SERVICE_NAME       = "Gateway Service Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to indicate that the
   ** communication with the SAP/R3 System has to be secured.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.snc_mode {@link DestinationDataProvider#JCO_SNC_MODE}
   **
   ** @see DestinationDataProvider
   */
  public static final String SECURE_SOCKET              = "Secure Socket";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the SAP/R3
   ** System number where this IT Resource belongs to.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.sysnr {@link DestinationDataProvider#JCO_SYSNR}
   **
   ** @see DestinationDataProvider
   */
  public static final String SYSTEM_NUMBER              = "System Number";

  /**
   ** Attribute tag which may be defined on an IT Resource to hold the name of
   ** the SAP/R3 System where this IT Resource belongs to.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.r3name {@link DestinationDataProvider#JCO_R3NAME}
   **
   ** @see DestinationDataProvider
   */
  public static final String SYSTEM_NAME                = "System Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the language
   ** of the principal used to establish a connection to the SAP/R3 System.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.lang {@link DestinationDataProvider#JCO_LANG}
   **
   ** @see DestinationDataProvider
   */
  public static final String SYSTEM_LANGUAGE            = "System Language";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the timezone
   ** of the SAP/R3 System.
   */
  public static final String SYSTEM_TIMEZONE            = "System TimeZone";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the SAP/R3
   ** System the RFC destination value that is used for identification of the
   ** SAP/R3 System. This value must be same as that of the Logical System name.
   ** <p>
   ** Sample value: EH6CLNT001
   ** <br>
   ** Here the sample value is based on the following format used in SAP system:
   ** <br>
   ** <code>&lt;SYSTEM_ID&gt;CLNT&lt;CLIENT_NUM&gt;</code>
   ** <br>
   ** In this sample value, EH6 is the System ID of the target system and 001 is
   ** the client number.
   */
  public static final String MASTER_SYSTEM_NAME         = "Master System Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the SAP/R3
   ** Client of the SAP/R3 System where this IT Resource will be working on.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.client {@link DestinationDataProvider#JCO_CLIENT}
   **
   ** @see DestinationDataProvider
   */
  public static final String CLIENT_LOGON               = "Client Logon";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the
   ** distiguished name of the principal to establish a connection with the SAP
   ** system .
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.user {@link DestinationDataProvider#JCO_USER}
   **
   ** @see DestinationDataProvider
   */
  public static final String PRINCIPAL_NAME             = "Principal Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the password
   ** of the principal used to establish a connection to the SAP/R3 System.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.passwd {@link DestinationDataProvider#JCO_PASSWD}
   **
   ** @see DestinationDataProvider
   */
  public static final String PRINCIPAL_PASSWORD         = "Principal Password";

  /**
   ** Attribute tag which may be defined on an IT Resource to hold the initial
   ** code page in SAP notation used during communication with the SAP/R3
   ** System.
   ** <p>
   ** A code page is used whenever character data is processed on the
   ** application server, appears on the front end, or is rendered by a printer.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.codepage {@link DestinationDataProvider#JCO_CODEPAGE}
   **
   ** @see DestinationDataProvider
   */
  public static final String PRINCIPAL_CODEPAGE          = "Principal Codepage";

  /**
   ** Attribute tag which may be defined for an IT Resource Fature to specify
   ** if a CUA is in front of the SAP/R3 System to manage.
   */
  public static final String CENTRAL_USER_ADMINISTRATION = "CUA Managed";

  /** the array with the attributes for the IT Resource */
  private static final ITResourceAttribute[] attribute = {
    new ITResourceAttribute(CENTRAL_USER_ADMINISTRATION, ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(APPLICATION_SERVER_HOST,     ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(APPLICATION_SERVER_GROUP,    ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(APPLICATION_SERVER_UNICODE,  ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(APPLICATION_SERVER_FEATURE,  ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(MESSAGE_SERVER_HOST,         ITResourceAttribute.OPTIONAL)
  , new ITResourceAttribute(MESSAGE_SERVER_PORT,         ITResourceAttribute.OPTIONAL)
  , new ITResourceAttribute(GATEWAY_SERVER_HOST,         ITResourceAttribute.OPTIONAL)
  , new ITResourceAttribute(GATEWAY_SERVICE_NAME,        ITResourceAttribute.OPTIONAL)
  , new ITResourceAttribute(SYSTEM_NUMBER,               ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(SYSTEM_NAME,                 ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(SYSTEM_LANGUAGE,             ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(SYSTEM_TIMEZONE,             ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(MASTER_SYSTEM_NAME,          ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(CLIENT_LOGON,                ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(PRINCIPAL_NAME,              ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(PRINCIPAL_PASSWORD,          ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(PRINCIPAL_CODEPAGE,          ITResourceAttribute.OPTIONAL)
  , new ITResourceAttribute(SECURE_SOCKET,               ITResourceAttribute.MANDATORY)
  , new ITResourceAttribute(CONNECTION_RETRY_COUNT,      ITResourceAttribute.OPTIONAL)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance key.
   ** <br>
   ** The <code>IT Resource</code> will not be populated from the repository of
   ** the Oracle Identity Manager. Only instance key and instance name are
   ** obtained.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   */
  public Resource(final Loggable loggable)
    throws TaskException {

    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance key.
   ** <br>
   ** The <code>IT Resource</code> will not be populated from the repository of
   ** the Oracle Identity Manager. Only instance key and instance name are
   ** obtained.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   ** @param  instanceKey        the system identifier of the
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries.
   */
  public Resource(final Loggable loggable, final Long instanceKey)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> which is associated
   ** with the specified {@link Loggable} and belongs to the IT Resource
   ** specified by the given name.
   ** <br>
   ** The IT Resource will be populated from the repository of the Oracle
   ** Identity Manager and also all well known attributes.
   **
   ** @param  task               the {@link AbstractTask} which has
   **                            instantiated this IT Resource wrapper.
   ** @param  instanceName       the name of the IT Resource instance where this
   **                            wrapper belongs to.
   **
   ** @throws TaskException      if the IT Resource is not defined in the Oracle
   **                            Identity manager meta entries or one or more
   **                            attributes are missing on the IT Resource Type
   **                            Definition.
   */
  public Resource(final AbstractTask task, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(task, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> that is not associated with a task.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   ** @param  parameter          the {@link Map} providing the parameter of this
   **                            <code>IT Resource</code> configuration where
   **                            this wrapper belongs to.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            IT Resource Type Definition.
   */
  public Resource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> which is associated
   ** with the specified {@link Loggable} and belongs to an customizable IT
   ** Resource with the specified properties.
   ** <br>
   ** The IT Resource will not be populated from the repository of the Oracle
   ** Identity Manager.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   ** @param  applicationHost    the name of the SAP application server hostname
   **                            or IP address where this IT Resource belongs
   **                            to.
   **                            <br>
   **                            Must not be <code>null</code>.
   ** @param  applicationGroup   the name of the group of SAP application
   **                            servers this IT Resource belongs to.
   ** @param  applicationRoute   the route to the SAP application server
   **                            hostname or IP address where this IT Resource
   **                            belongs to.
   ** @param  messageHost        the name of the SAP message server hostname
   **                            or IP address where this IT Resource belongs
   **                            to.
   ** @param  messagePort        the number of the SAP message server port where
   **                            this IT Resource belongs to.
   ** @param  gatewayHost        the name of the SAP gateway server hostname
   **                            or IP address where this IT Resource belongs
   **                            to.
   ** @param  gatewayName        the name of the SAP gateway  where this IT
   **                            Resource belongs to.
   ** @param  systemNumber       the SAP/R3 System number where this IT Resource
   **                            belongs to.
   **                            <br>
   **                            Must not be <code>null</code>.
   ** @param  systemName         the name of the SAP/R3 System where this IT
   **                            Resource belongs to.
   ** @param  systemLanguage     the language used to establish a connection to
   **                            the SAP/R3 System.
   ** @param  systemTimeZone     the timezone of the SAP/R3 System where this IT
   **                            Resource belongs to.
   ** @param  masterSystemName   the RFC destination value that is used for
   **                            identification of the SAP/R3 System and this IT
   **                            Resource belongs to.
   ** @param  clientLogon        the SAP client of the SAP/R3 System where this
   **                            IT Resource belongs to.
   **                            <br>
   **                            Must not be <code>null</code>.
   ** @param  principalName      the user name of the target system account to
   **                            be used to establish a connection.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   ** @param  principalCodepage  the initial code page in SAP notation used
   **                            during communication with the SAP/R3 System.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the target system.
   ** @param  cuaManaged         <code>true</code> if you want to enable
   **                            connection pooling for this target system
   **                            installation.
   ** @param  poolSupported      <code>true</code> if you want to enable
   **                            connection pooling for this target system
   **                            installation.
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **                            <br>
   **                            Must not be <code>null</code>.
   **
   ** @throws SystemException    if one or more attributes are missing on the
   **                            IT Resource Type Definition.
   */
  public Resource(final Loggable loggable, final String applicationHost, final String applicationGroup, final boolean applicationUnicode, final String applicationRoute, final String messageHost, final String messagePort, final String gatewayHost, final String gatewayName, final String systemNumber, final String systemName, final String systemLanguage, final String systemTimeZone, final String masterSystemName, final String clientLogon, final String principalName, final String principalPassword, final String principalCodepage, final boolean secureSocket, final boolean cuaManaged, final boolean poolSupported, final int retryInterval, final String feature)
    throws SystemException {

    // ensure inheritance
    super(loggable);

    attribute(APPLICATION_SERVER_HOST,     applicationHost);
    attribute(APPLICATION_SERVER_GROUP,    applicationGroup);
    attribute(APPLICATION_SERVER_UNICODE,  applicationUnicode ? "2" : "1");
    attribute(APPLICATION_SERVER_ROUTE,    applicationRoute);
    attribute(APPLICATION_SERVER_FEATURE,  feature);
    attribute(MESSAGE_SERVER_HOST,         messageHost);
    attribute(MESSAGE_SERVER_PORT,         messagePort);
    attribute(GATEWAY_SERVER_HOST,         gatewayHost);
    attribute(GATEWAY_SERVICE_NAME,        gatewayName);
    attribute(SYSTEM_NAME,                 systemName);
    attribute(SYSTEM_NUMBER,               systemNumber);
    attribute(SYSTEM_LANGUAGE,             systemLanguage);
    attribute(SYSTEM_TIMEZONE,             systemTimeZone);
    attribute(MASTER_SYSTEM_NAME,          masterSystemName);
    attribute(CLIENT_LOGON,                clientLogon);
    attribute(PRINCIPAL_NAME,              principalName);
    attribute(PRINCIPAL_PASSWORD,          principalPassword);
    attribute(PRINCIPAL_CODEPAGE,          principalCodepage);
    attribute(SECURE_SOCKET,               secureSocket ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(CENTRAL_USER_ADMINISTRATION, cuaManaged   ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(CONNECTION_RETRY_INTERVAL,   String.valueOf(retryInterval));
    attribute(POOL_SUPPORTED,              poolSupported ? SystemConstant.TRUE : SystemConstant.FALSE);

    validateAttributes(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationHost
  /**
   ** Returns the name of the SAP application server hostname or IP address
   ** where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #APPLICATION_SERVER_HOST}.
   **
   ** @return                    the name of the SAP application server hostname
   **                            or IP address where this IT Resource belongs
   **                            to.
   **                            <br>
   **                            Will never be <code>null</code>.
   */
  public final String applicationHost() {
    return stringValue(APPLICATION_SERVER_HOST);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationGroup
  /**
   ** Returns the name of the group of SAP application servers.where this IT
   ** Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #APPLICATION_SERVER_GROUP}.
   **
   ** @return                    the name of the group of SAP application
   **                            servers.where this IT Resource belongs to.
   */
  public final String applicationGroup() {
    return stringValue(APPLICATION_SERVER_GROUP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationUnicode
  /**
   ** Returns the additional logon parameter to define the codepage type of the
   ** SAP System.
   ** <ul>
   **   <li>1 - non unicode
   **   <li>2 - unicode enabled
   ** </ul>
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #APPLICATION_SERVER_UNICODE}.
   **
   ** @return                    the additional logon parameter to define the
   **                            codepage type of the SAP System.
   */
  public final String applicationUnicode() {
    return stringValue(APPLICATION_SERVER_UNICODE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationRoute
  /**
   ** Returns the name of the SAP router server hostname or IP address
   // where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #APPLICATION_SERVER_ROUTE}.
   **
   ** @return                    the name of the SAP router server hostname
   **                            or IP address where this IT Resource belongs
   **                            to.
   */
  public final String applicationRoute() {
    return stringValue(APPLICATION_SERVER_ROUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   messageHost
  /**
   ** Returns the name of the SAP message server hostname or IP address
   // where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #MESSAGE_SERVER_HOST}.
   **
   ** @return                    the name of the SAP message server hostname
   **                            or IP address where this IT Resource belongs
   **                            to.
   */
  public final String messageHost() {
    return stringValue(MESSAGE_SERVER_HOST);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   messagePort
  /**
   ** Returns the port of the SAP message server where this IT Resource belongs
   ** to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #MESSAGE_SERVER_PORT}.
   **
   ** @return                    the port of the SAP message server this IT
   **                            Resource belongs to.
   */
  public final String messagePort() {
    return stringValue(MESSAGE_SERVER_PORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gatewayHost
  /**
   ** Returns the name of the SAP gateway server hostname or IP address
   ** where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #GATEWAY_SERVER_HOST}.
   **
   ** @return                    the name of the SAP gateway server hostname
   **                            or IP address where this IT Resource belongs
   **                            to.
   */
  public final String gatewayHost() {
    return stringValue(GATEWAY_SERVER_HOST);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gatewayName
  /**
   ** Returns the name of the SAP gateway service where this IT Resource
   ** belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #GATEWAY_SERVICE_NAME}.
   **
   ** @return                    the name of the SAP gateway service where this
   **                            IT Resource belongs to.
   */
  public final String gatewayName() {
    return stringValue(GATEWAY_SERVICE_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemNumber
  /**
   ** Returns the SAP/R3 System number where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SYSTEM_NUMBER}.
   **
   ** @return                    the SAP/R3 System number where this IT Resource
   **                            belongs to.
   **                            <br>
   **                            Will never be <code>null</code>.
   */
  public final String systemNumber() {
    return stringValue(SYSTEM_NUMBER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemName
  /**
   ** Returns the SAP/R3 System name where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SYSTEM_NAME}.
   **
   ** @return                    the SAP/R3 System name where this IT Resource
   **                            belongs to.
   */
  public final String systemName() {
    return stringValue(SYSTEM_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemLanguage
  /**
   ** Returns the logon language of the SAP/R3 System account specified by the
   ** <code>principalName</code> parameter to establish a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SYSTEM_LANGUAGE}.
   **
   ** @return                    the logon language of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   */
  public final String systemLanguage() {
    return stringValue(SYSTEM_LANGUAGE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTimeZone
  /**
   ** Returns the timezone of the SAP/R3 System where this IT Resource belongs
   ** to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SYSTEM_TIMEZONE}.
   **
   ** @return                    the timezone of the SAP/R3 System where this IT
   **                            Resource belongs to.
   */
  public final String systemTimeZone() {
    return stringValue(SYSTEM_TIMEZONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   masterSystemName
  /**
   ** Returns the RFC destination value that is used for identification of the
   ** SAP/R3 System. This value must be same as that of the Logical System name.
   ** <p>
   ** Sample value: EH6CLNT001
   ** <br>
   ** Here the sample value is based on the following format used in SAP system:
   ** <br>
   ** <code>&lt;SYSTEM_ID&gt;CLNT&lt;CLIENT_NUM&gt;</code>
   ** <br>
   ** In this sample value, EH6 is the System ID of the target system and 001 is
   ** the client number. where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #MASTER_SYSTEM_NAME}.
   **
   ** @return                    the RFC destination value that is used for
   **                            identification of the SAP/R3 System.
   **                            This value must be same as that of the Logical
   **                            System name.
   */
  public final String masterSystemName() {
    return stringValue(MASTER_SYSTEM_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientLogon
  /**
   ** Returns the SAP client of the SAP/R3 System where this IT Resource belongs
   ** to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #CLIENT_LOGON}.
   **
   ** @return                    the SAP client of the SAP/R3 System where this
   **                            IT Resource belongs to.
   **                            <br>
   **                            Will never be <code>null</code>.
   */
  public final String clientLogon() {
    return stringValue(CLIENT_LOGON);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the user name of the SAP/R3 System account to be used to establish
   ** a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PRINCIPAL_NAME}.
   **
   ** @return                    the user name of the SAP/R3 System account to
   **                            be used to establish a connection.
   */
  public final String principalName() {
    return stringValue(PRINCIPAL_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password of the SAP/R3 System account specified by the
   ** <code>principalName</code> parameter to establish a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   */
  public final String principalPassword() {
    return stringValue(PRINCIPAL_PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalCodepage
  /**
   ** Returns the code page of the SAP/R3 System used by the communication with
   ** the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PRINCIPAL_CODEPAGE}.
   **
   ** @return                    the code page of the SAP/R3 System used by the
   **                            communication with the SAP/R3 System.
   */
  public final String principalCodepage() {
    return stringValue(PRINCIPAL_CODEPAGE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns the <code>true</code> if SSL is used to secure communication
   ** between Oracle Identity Manager and the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURE_SOCKET}.
   ** <br>
   ** If {@link #SECURE_SOCKET} is not mapped in the underlying mapping this
   ** method returns <code>false</code>.
   **
   ** @return                    whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the SAP/R3 System.
   */
  public final boolean secureSocket() {
    return booleanValue(SECURE_SOCKET, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cuaManaged
  /**
   ** Returns the <code>true</code> if a Central User Administration is in front
   ** of the SAP/R3 System to manage where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #CENTRAL_USER_ADMINISTRATION}.
   ** <br>
   ** If {@link #CENTRAL_USER_ADMINISTRATION} is not mapped in the underlying
   ** mapping this method returns <code>false</code>.
   **
   ** @return                    whether or not a Central User Administration is
   **                            in front of the SAP/R3 System to manage where
   **                            this IT Resource belongs to.
   */
  public final boolean cuaManaged() {
    return booleanValue(CENTRAL_USER_ADMINISTRATION, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryConnectionCount
  /**
   ** Returns the number of consecutive attempts to be made at establishing a
   ** connection with the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #CONNECTION_RETRY_COUNT}.
   ** <br>
   ** If {@link #CONNECTION_RETRY_COUNT} is not mapped in the underlying
   ** mapping this method returns {@link #CONNECTION_RETRY_COUNT_DEFAULT}.
   **
   ** @return                    the number of consecutive attempts to be made
   **                            at establishing a connection with the SAP/R3
   **                            System.
   */
  public final int retryConnectionCount() {
    return integerValue(CONNECTION_RETRY_COUNT, CONNECTION_RETRY_COUNT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryConnectionInterval
  /**
   ** Returns the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #CONNECTION_RETRY_INTERVAL}.
   ** If {@link #CONNECTION_RETRY_INTERVAL} is not mapped in the underlying
   ** mapping this method returns {@link #CONNECTION_RETRY_INTERVAL_DEFAULT}.
   **
   ** @return                    the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the SAP/R3 System.
   */
  public final int retryConnectionInterval() {
    return integerValue(CONNECTION_RETRY_INTERVAL, CONNECTION_RETRY_INTERVAL_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   features
  /**
   ** Returns the name of the feature mapping of the SAP/R3 System where this IT
   ** Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #APPLICATION_SERVER_FEATURE}.
   **
   ** @return                    the name of the feature mapping of the SAP
   **                            system where this IT Resource belongs to.
   */
  public final String feature() {
    return stringValue(APPLICATION_SERVER_FEATURE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstrcatConnector)
  /**
   ** Returns the array with names which should be populated from the
   ** IT Resource definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  public AbstractAttribute[] attributes() {
    return attribute;
  }
}