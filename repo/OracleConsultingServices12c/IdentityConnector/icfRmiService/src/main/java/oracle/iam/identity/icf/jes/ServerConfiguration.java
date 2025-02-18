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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   ServerConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerConfiguration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.jes;

import org.identityconnectors.framework.spi.ConfigurationProperty;

import oracle.iam.identity.icf.resource.Connector;

import oracle.iam.identity.icf.connector.AbstractConfiguration;

////////////////////////////////////////////////////////////////////////////////
// abstract class ServerConfiguration
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Encapsulates the configuration of the connector.
 ** <p>
 ** The configuration includes every property that a caller <b>must</b> specify
 ** in order to use the connector, as well as every property that a caller
 ** <b>may</b> specify in order to affect the behavior of the connector overall
 ** (as opposed to operation options, which affect only a specific invocation of
 ** a specific operation).
 ** <br>
 ** Required configuration parameters ConfigurationProperty.required() generally
 ** include the information required to connect to a target instance -- such as
 ** a URL, username and password.
 ** <br>
 ** Optional configuration parameters often specify preferences as to how the
 ** connector-bundle should behave.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ServerConfiguration extends AbstractConfiguration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String CATEGORY = "JCS.CONNECTOR.JES";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the host name for the target <code>Server Endpoint</code>.
   */
  protected final ServerEndpoint endpoint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>ServerConfiguration</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServerConfiguration() {
    // ensure inheritance
    super(CATEGORY);

    // initialize instance attributes
    this.endpoint = ServerEndpoint.build(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link ServerEndpoint} this configuration intialize.
   **
   ** @return                    the {@link ServerEndpoint} this configuration
   **                            initialize.
   **                            <br>
   **                            Possible object {@link ServerEndpoint}.
   */
  public final ServerEndpoint endpoint() {
    return this.endpoint;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerHost
  /**
   ** Set the <code>primaryHost</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>primaryHost</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=1, displayMessageKey=Connector.Endpoint.SERVER_HOST_LABEL, helpMessageKey=Connector.Endpoint.SERVER_HOST_HINT, required=true)
  public final void setServerHost(final String value) {
    this.endpoint.primaryHost(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerHost
  /**
   ** Returns the <code>primaryHost</code> attribute for the content provider.
   **
   ** @return                    the <code>primaryHost</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getServerHost() {
    return this.endpoint.primaryHost();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerPort
  /**
   ** Set the <code>primaryPort</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>primaryPort</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=2, displayMessageKey=Connector.Endpoint.SERVER_PORT_LABEL, helpMessageKey=Connector.Endpoint.SERVER_PORT_HINT, required=true)
  public final void setServerPort(final int value) {
    this.endpoint.primaryPort(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerPort
  /**
   ** Returns the <code>primaryPort</code> attribute for the content provider.
   **
   ** @return                    the <code>primaryPort</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int getServerPort() {
    return this.endpoint.primaryPort();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerName
  /**
   ** Set the <code>serverName</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>serverName</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=3, displayMessageKey=Connector.Endpoint.SERVER_NAME_LABEL, helpMessageKey=Connector.Endpoint.SERVER_NAME_HINT, required=true)
  public final void setServerName(final String value) {
    this.endpoint.serverName(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerName
  /**
   ** Returns the <code>serverName</code> attribute for the content provider.
   **
   ** @return                    the <code>serverName</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getServerName() {
    return this.endpoint.serverName();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalUsername
  /**
   ** Set the username of the security principal for the target system used to
   ** connect to.
   **
   ** @param  value              the username of the security principal for the
   **                            target system used to connect to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=4, displayMessageKey=Connector.Endpoint.PRINCIPAL_USERNAME_LABEL, helpMessageKey=Connector.Endpoint.PRINCIPAL_USERNAME_HINT, required=true)
  public final void setPrincipalUsername(final String value) {
    this.endpoint.principalUsername(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalUsername
  /**
   ** Returns the username of the security principal for the target system used
   ** to connect to.
   **
   ** @return                    the username of the security principal for the
   **                            target system used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getPrincipalUsername() {
    return this.endpoint.principalUsername();
  }
}