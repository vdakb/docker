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

    File        :   AvailableServiceType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    AvailableServiceType.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import oracle.hst.deployment.ServiceAction;

////////////////////////////////////////////////////////////////////////////////
// enum AvailableServiceType
// ~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AvailableServiceType</code> defines specific parameter type
 ** declarations regarding <code>Access Service</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum AvailableServiceType implements ServiceAction {

    /**
     ** The Oracle Access Management platform enables Identity Context data to
     ** be collected, propagated across the involved components, and made
     ** available for granting or denying authorization to acess protected
     ** resources. The Identity Context Service (ICS) allows access to the
     ** Identity Context Runtime through the Identity Context API.
     ** <br>
     ** The Identity Context Dictionary schema specifies the Identity Context
     ** attributes.
     ** <p>
     ** Default: <b>enabled</b>
     */
    ICS("identity-context-publishing",      "IdentityContextServicesDescriptor/PublishingServiceDescriptor")
    /**
     ** Access Manager functionality is enabled by default.
     ** <br>
     ** Access Manager Service is required to set SSO policies, configure Access
     ** Manager, as well as Common Configuration, and when REST Services are
     ** enabled.
     ** <p>
     ** Default: <b>enabled</b>
     */
  , OAMSSO("single-signon-standard",        "OAMServicesDescriptor/WebSSOServiceDescriptor")
    /**
     ** The Detached Credential Collector (DCC) is simply a WebGate configured
     ** to use the additional Credential Collection capability in a deployment.
     ** There are two deployment types depending on whether the DCC WebGate is
     ** also protecting the applications or not.
     ** <br>
     ** The DCC provides network isolation for greater security in production
     ** deployments, and is required for some forms of authentication.
     ** <p>
     ** Default: <b>enabled</b>
     */
  , OAMOAUTH("oauth-access-manager",        "OAMOAuthServicesDescriptor/OAMOAuthServiceDescriptor")
    /**
     ** Oracle Identity Federation enables business partners to achieve a
     ** federated environment by providing the mechanism with which companies
     ** can form a federation and securely share services and data across their
     ** respective security domains.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , OAMDCC("credential-collector-detached", "OAMServicesDescriptor/DCCEnabled")
    /**
     ** The default Embedded Credential Collector (ECC) is installed with the
     ** Access Manager Server and can be used as-is with no additional
     ** installation or set up steps.
     ** <p>
     ** Default: <b>enabled</b>
     */ 
  , OAMECC("credential-collector-embedded", "OAMServicesDescriptor/ECCEnabled")
    /**
     ** Unsolicited Login is a new feature available since OAM 11g Release 2
     ** (11.1.2.x).
     ** <br>
     ** Using Unsolicited login functionality, you can post the credentials
     ** directly from an application page to OAM and perform user
     ** authentication. To enable unsolicited login in OAM you need to make the
     ** required configuration changes.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , OAMDAS("authentication-unsolicited",    "OAMServicesDescriptor/DirectAuthenticationServiceDescriptor")
    /**
     ** The Adaptive Authentication Service offers stronger multifactor (also
     ** referred to as second factor) authentication for sensitive applications
     ** that require additional security in addition to the standard user name
     ** and password type authentication.
     ** <p>
     ** Multifactor authentication involves more than one stage when verifying
     ** the identity of an entity attempting to access services from a server or
     ** on a network. For example, when multifactor authentication is enabled
     ** and configured, the traditional user name and password is the first
     ** factor. Additional security is enforced by adding a One Time Pin (OTP)
     ** step, or an Access Request (Push) Notification step as a second factor
     ** in the authentication process.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , OAAM("authentication-adaptive",         "AdaptiveAuthnServicesDescriptor/AdaptiveAuthnServiceDescriptor")
    /**
     ** A federated environment (as defined in the identity management realm) is
     ** one in which organizations that provide services and identity data
     ** (business partners) have established trust in order to share access to a
     ** set of protected resources while protecting the same from unauthorized
     ** access. Oracle Identity Federation enables business partners to achieve
     ** this by providing the mechanism with which companies can form a
     ** federation and securely share services and data across their respective
     ** security domains.
     ** <p>
     ** An identity provider (abbreviated IdP or IDP) is a system entity that
     ** creates, maintains, and manages identity information for principals
     ** while providing authentication services to relying applications within a
     ** federation or distributed network.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , FEDIDP("federation-identity-provider",  "FederationServicesDescriptor/IdProviderServiceDescriptor")
    /**
     ** A federated environment (as defined in the identity management realm) is
     ** one in which organizations that provide services and identity data
     ** (business partners) have established trust in order to share access to a
     ** set of protected resources while protecting the same from unauthorized
     ** access. Oracle Identity Federation enables business partners to achieve
     ** this by providing the mechanism with which companies can form a
     ** federation and securely share services and data across their respective
     ** security domains.
     ** <p>
     ** A service provider (SP) integration module creates a user authenticated
     ** session at an identity and access management (IAM) system like Oracle
     ** Access Manager.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , FEDSP("federation-service-provider",    "FederationServicesDescriptor/SPServiceDescriptor")
    /**
     ** <p>
     ** Default: <b>disabled</b>
     */
  , STS("secure-token-service",             "STSServicesDescriptor/WSTrustServiceDescriptor")
    /**
     ** Federation supports both Relaying Party and OpenID Provider in
     ** accordance to recent OpenID 2.0 specification. It enables organizations
     ** to start accepting OpenID from leading providers such as Yahoo and
     ** Google or to become OpenID provider in a matter of hours. With federated
     ** users can leverage their corporate identity at OpenID-enabled blogging
     ** sites and socials networks, such as Facebook.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , OICRP("openid-connect-relying-party",  "OICServicesDescriptor/RPServiceDescriptor")
    /**
     ** <p>
     ** Default: <b>disabled</b>
     */
  , OICREST("openid-token-service",        "OICServicesDescriptor/RestTokenServiceDescriptor")
    /**
     ** The Oracle Access Management OAuth Service allows organizations to
     ** implement the open standard OAuth 2.0 Web authorization protocol in an
     ** Access Manager environment. OAuth enables a client to access Oracle
     ** Access Manager (OAM) protected resources that belong to another user
     ** (that is, the resource owner).
     ** <br>
     ** OpenIDConnect implements authentication as an extension to the OAuth 2.0
     ** authorization process. It provides easily consumable ID Tokens that are
     ** obtained by Clients using OAuth 2.0 flows.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , OIDC("openid-connect-service",         "OpenIDConnectServicesDescriptor/OpenIDConnectServiceDescriptor")
    /**
     ** The Oracle Access Management OAuth Service allows organizations to
     ** implement the open standard OAuth 2.0 Web authorization protocol in an
     ** Access Manager environment. OAuth enables a client to access Oracle
     ** Access Manager (OAM) protected resources that belong to another user
     ** (that is, the resource owner).
     ** <br>
     ** OpenIDConnect implements authentication as an extension to the OAuth 2.0
     ** authorization process. It provides easily consumable ID Tokens that are
     ** obtained by Clients using OAuth 2.0 flows.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , OAUTH("oauth",                         "OAuthServicesDescriptor/OAuthServiceDescriptor")
    /**
     ** As part of the Identity Context Service (IDS), Oracle Enterprise Single
     ** Sign-on (OESSO) can publish and propagate client-based Identity Context
     ** attributes.
     ** <br>
     ** Once full integration has been configured, client-specific Identity
     ** Context attributes (as documented in Identity Context Dictionary) will
     ** be sent by OESSO to OAM in the session initiation request together with
     ** the user credentials submitted in the access request.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , OESSO("single-signon-enterprise",      "ESSOServicesDescriptor/ESSOServiceDescriptor")
    /**
     ** Oracle Access Management Mobile and Social (Mobile and Social) provides
     ** REST-based authentication services, in addition to a user profile
     ** service and an authorization service, for mobile and desktop devices.
     ** <br>
     ** When Mobile and Social is configured to provide authentication using
     ** Access Manager, it can publish Identity Context attributes provided by
     ** the mobile client to Access Manager. The Identity Context attributes are
     ** published by the Mobile and Social SDK for iOS and Java platforms.
     ** <p>
     ** Default: <b>disabled</b>
     */
  , MOBIL("mobile-security",               "MobileSecurityServicesDescriptor/MobileSecurityServiceDescriptor")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String id;
  final String path;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AvailableServiceType</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  AvailableServiceType(final String id, final String path) {
    this.id   = id;
    this.path = path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Evaluates the path to the configuration item from the top root element
   ** until the <code>item</code>.
   **
   ** @return                    path to the configuration item from the top
   **                            .root element
   */
  public String path() {
    return String.format("/DeployedComponent/Descriptors/OAMSEntityDescriptor/%s/ServiceStatus", this.path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (FeatureProperty)
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a proper access server service from the given
   ** string value.
   **
   ** @param  value              the string value the access server service
   **                            should be returned for.
   **
   ** @return                    the access server property.
   */
  public static AvailableServiceType from(final String value) {
    for (AvailableServiceType cursor : AvailableServiceType.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}