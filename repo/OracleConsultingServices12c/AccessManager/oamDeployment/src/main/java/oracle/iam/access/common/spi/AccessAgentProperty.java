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

    File        :   AccessAgentProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    AccessAgentProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import oracle.iam.access.common.FeatureProperty;

////////////////////////////////////////////////////////////////////////////////
// enum AccessAgentProperty
// ~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessAgentProperty</code> defines specific parameter type
 ** declarations regarding <code>Access Agent</code>s aka WebGates.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum AccessAgentProperty implements FeatureProperty {
  /**
   ** The host and port of the computer on which the Web Server for the
   ** <code>Access Agent</code> is installed. For example,
   ** <code>http://example_host:port</code> or
   ** <code>https://example_host:port</code>. The port number is optional.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** A particular Base URL can be registered once only. There is a one-to-one
   ** mapping from this Base URL to the Web Server domain on which the
   ** <code>Access Agent</code> is installed (as specified with the Host
   ** Identifier element). However, one domain can have multiple Base URLs.
   */
  AGENT_BASE_URL("agentBaseURL", Type.URL, false, null),

  /**
   ** An optional, unique password for the <code>Access Agent</code>, which
   ** can be assigned during the registration process.
   ** <p>
   ** When a registered <code>Access Agent</code> connects to an
   ** <code>Access Server</code>, the password is used for authentication to
   ** prevent unauthorized <code>Access Agent</code>s from connecting to
   ** <code>Access Server</code>s and obtaining policy information.
   */
  AGENT_PASSWORD("agentPassword", Type.STRING, false, null),

  /**
   ** Requested for only <b>Cert</b> mode communication, this passphrase is
   ** used to encrypt the private key used for SSL communication between
   ** <code>Access Agent</code> and the <code>Access Server</code> in
   ** <b>Simple</b> and <b>Cert</b> modes.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** The <b>Agent Key Password</b> has no relationship to the
   ** <b>Access Client Password</b> described {@link #AGENT_PASSWORD}.
   ** <p>
   ** <b>Cert Mode</b>
   ** <br>
   ** In this mode, the agent key can be different on the client and server; it
   ** is no longer global. Administrators must enter the
   ** <b>Agent Key Password</b> to enable generation of a
   ** <code>password.xml</code> file during agent registration, which must be
   ** copied to the agent side. For certificate generation, you must encrypt
   ** the private key (used for SSL) using this password through openssl or
   ** other third-party tools to be placed inside <code>aaa_key.pem</code>. At
   ** runtime, <code>Access Agent</code> retrieves the key from
   ** <code>password.xml</code>, and uses it to decrypt the key in
   ** <code>aaa_key.pem</code>.
   ** <ul>
   **   <li>If the key is encrypted, <code>Access Agent</code> internally
   **       invokes the call back function to obtain the password.
   **   <li>If the key is encrypted and <code>password.xml</code> does not
   **       exist, <code>Access Agent</code> cannot establish connections with
   **       the <code>Access Server</code>.
   **   <li>If the key is not encrypted, there is no attempt to read
   **       <code>password.xml</code>.
   ** </ul>
   */
//  AGENT_KEY_PASSWORD("agentKeyPassword", Type.STRING, false, null),

  /**
   ** This Agent Privilege function enables the provisioning of session
   ** operations per agent, as follows:
   ** <ul>
   **   <li>Terminate session
   **   <li>Enumerate sessions
   **   <li>Add or Update attributes for an existing session
   **   <li>List all attributes for a given session ID or read session
   ** </ul>
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Only privileged agents can invoke session management operations. When this
   ** parameter is enabled, session management requests (listed above) are
   ** processed by the <code>Access Server</code>. If disabled, such requests
   ** are rejected for the agent.
   ** <p>
   ** Default: <code>Enabled</code>
   */
  ALLOW_MANAGEMENT_OPERATION("allowManagementOperation", Type.BOOLEAN, false, Boolean.TRUE.toString()),

  /**
   ** This Agent Privilege activates <code>Access Agent</code> detached
   ** credential collector functionality for simple-form or dynamic multi-factor
   ** authentication.
   ** <p>
   ** Default: <code>Disabled</code>
   */
  ALLOW_COLLECTOR_OPERATION("allowCollectorOperation", Type.BOOLEAN, false, Boolean.FALSE.toString()),

  /**
   ** Allows the ASDK code to retrieve the OAM_ID cookie.
   */
  ALLOW_MASTER_TOKEN_RETRIEVAL("allowMasterTokenRetrieval", Type.BOOLEAN, false, Boolean.FALSE.toString()),

  /**
   ** Allows the ASDK code to scope the OAM_ID cookie to the domain level
   ** instead of host level.
   */
  ALLOW_TOKEN_SCOPE_OPERATION("allowTokenScopeOperation", Type.BOOLEAN, false, Boolean.FALSE.toString()),

  /**
   ** The top-level construct of the Oracle Access Manager policy model is the
   ** <code>Application Domain</code>.
   ** <br>
   ** Each application domain provides a logical container for resources, and
   ** the associated authentication and authorization policies that dictate who
   ** can access these.
   ** <p>
   ** The size and number of <code>Application Domain</code>s is up to the
   ** administrator; the decision can be based on individual
   ** <code>Application Resource</code>s or any other logical grouping as
   ** needed. An <code>Application Domain</code> is automatically created during
   ** <code>Access Agent</code> registration. Also, administrators can protect
   ** multiple <code>Application Domain</code>s using the same agent by manually
   ** creating the <code>Application Domain</code> and adding the resources and
   ** policies.
   */
  APPLICATION_DOMAIN("applicationDomain", Type.STRING, false, null),

  AUTHENTICATION_SCHEME_PROTECTED("authenticationSchemeProtected", Type.STRING, false, null),
  AUTHENTICATION_SCHEME_PUBLIC("authenticationSchemePublic", Type.STRING, false, null),

  /**
   ** During <code>Access Agent</code> registration, authentication and
   ** authorization can be policies created automatically.
   ** <p>
   ** <b>Shared Registration and Policies</b>:
   ** <br>
   ** Multiple <code>Access Agent</code>s (or Access Clients) installed on
   ** different Web Servers can share a single registration and policies to
   ** protect the same resources. This is useful in a high-availability failover
   ** environment. To do this:
   ** <ol>
   **   <li>Register the first <code>Access Agent</code> and enable
   **       <b>Auto Create Policies</b> to generate a host identifier (a name as
   **       you like) and policies.
   **   <li>Register a second <code>Access Agent</code> with the same host
   **       identifier as Step 1, and disable <b>Auto Create Policies</b>
   **       to eliminate policy creation.
   ** </ol>
   ** <p>
   ** Default: <code>false</code>
   */
  AUTO_CREATE_POLICY("autoCreatePolicy", Type.BOOLEAN, true, Boolean.FALSE.toString()),

  /**
   ** This setting apply only to browser based <code>Access Agent</code>s and
   ** control the browser's cache.
   ** <p>
   ** By default, the parameter is set to <code>no-cache</code>. This prevents
   ** browser based <code>Access Agent</code>s from caching data at the Web
   ** server application and the user's browser.
   ** <p>
   ** However, this may prevent certain operations such as downloading PDF files
   ** or saving report files when the site is protected by a browser based
   ** <code>Access Agent</code>s.
   ** <p>
   ** You can set the Access Manager SDK caches that the browser based
   ** <code>Access Agent</code>s uses to different levels. See
   ** http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html section 14.9 for
   ** details.
   ** <p>
   ** All of the cache-response-directives are allowed. For example, you may
   ** need to set the cache value to public to allow PDF files to be downloaded.
   ** <br>
   ** Default: <code>no-cache</code>
   */
  CACHE_CONTROL_HEADER("browserCacheControlHeader", Type.STRING, false, "no-cache"),

  /**
   ** Number of elements maintained in the cache. Caches are the following:
   ** <ul>
   **   <li><b>Resource to Authentication Scheme</b> - This cache maintains
   **       information about Resources (URLs), including whether it is
   **       protected and, if so, the authentication scheme used for
   **       protection.
   **   <li><b>Resource to Authorization Policy</b> - This cache maintains
   **       information about Resources and associated authorization policy
   **       <br>
   **       This cache stores authentication scheme information for a specific
   **       authentication scheme ID.
   **       <br>
   **       (11g Webgate only)
   ** </ul>
   ** The value of this setting refers to the maximum consolidated count for
   ** elements in these caches.
   ** <br>
   ** Default: <code>100000</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  CACHE_ELEMENTS_MAX("cacheElementsMax", Type.INTEGER, true, "100000"),

  /**
   ** This setting apply only to browser based <code>Access Agent</code>s and
   ** control the browser's cache.
   ** <p>
   ** By default, the parameter is set to <code>no-cache</code>. This prevents
   ** browser based <code>Access Agent</code>s from caching data at the Web
   ** server application and the user's browser.
   ** <p>
   ** However, this may prevent certain operations such as downloading PDF files
   ** or saving report files when the site is protected by a browser based
   ** <code>Access Agent</code>s.
   ** <p>
   ** You can set the Access Manager SDK caches that the browser based
   ** <code>Access Agent</code>s uses to different levels. See
   ** http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html section 14.9 for
   ** details.
   ** <p>
   ** All of the cache-response-directives are allowed. For example, you may
   ** need to set the cache value to public to allow PDF files to be downloaded.
   ** <br>
   ** Default: <code>no-cache</code>
   */
  CACHE_PRAGMA_HEADER("browserCachePragmaHeader",   Type.STRING, false, "no-cache"),

  /**
   ** Amount of time cached information remains in the
   ** <code>Access Agent</code> caches (Resource to Authentication Scheme,
   ** Authentication Schemes, and 11g <code>Access Agent</code> only Resource
   ** to Authorization Policy) when the information is neither used nor
   ** referenced.
   ** <br>
   ** Default: <code>1800</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  CACHE_TIMEOUT("cacheTimeout", Type.INTEGER, true, "1800"),

  /**
   ** The maximum number of connections that the <code>Access Agent</code> can
   ** establish with the <code>Access Server</code>. This number must be the
   ** same as (or greater than) the number of connections that are actually
   ** associated with the <code>Access Agent</code>.
   ** <br>
   ** Default: <code>1</code>
   */
  CONNECTION_MAX("connectionMax", Type.INTEGER, true, "1"),

  /**
   ** Maximum amount of time (in hours) to keep connections from the
   ** <code>Access Agent</code> to <code>Access Server</code> network alive.
   ** <br>
   ** After this time has elapsed, all connections from
   ** <code>Access Agent</code> to <code>Access Server</code> network will be
   ** shutdown and replaced with new ones.The unit of time is based on the
   ** user-defined parameter <code>maxSessionTimeUnits</code> which can be
   ** <code>minutes</code> or <code>hours</code>. When
   ** <code>maxSessionTimeUnits</code> is not defined, the unit is defaulted to
   ** <code>hours</code>.
   ** <br>
   ** Default: <code>1</code>
   */
  CONNECTION_SESSION_TIME_MAX("connectionSessionTimeMax", Type.INTEGER, false, "1"),

  /**
   ** Maximum valid time period for an agent token (the content of
   ** <b><code>ObSSOCookie</code></b> for 10g WebGate).
   ** <p>
   ** <b>10g WebGate only</b>
   **
   ** @see   #TOKEN_VALIDITY_TIME
   */
  COOKIE_SESSION_TIME("cookieSessionTime", Type.INTEGER, false, "3600"),

  /**
   ** This parameter describes the Web Server domain on which the
   ** <code>Access Agent</code> is deployed, for instance,
   ** <code>.example.com</code>.
   ** <p>
   ** You must configure the cookie domain to enable single sign-on among
   ** Web Servers. Specifically, the Web Servers for which you configure
   ** single sign-on must have the same Primary Cookie Domain value.
   ** <code>Access Agent</code> uses this parameter to create the
   ** <b><code>ObSSOCookie</code></b> authentication cookie.
   ** <p>
   ** This parameter defines which Web Servers participate within the cookie
   ** domain and have the ability to receive and update the
   ** <b><code>ObSSOCookie</code></b>. This cookie domain is not used to
   ** populate the <b><code>ObSSOCookie</code></b>; rather it defines which
   ** domain the <b><code>ObSSOCookie</code></b> is valid for, and which Web
   ** servers have the ability to accept and change the
   ** <b><code>ObSSOCookie</code></b> contents.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The more general the domain name, the more inclusive your single sign-on
   ** implementation will be. For example, if you specify <code>b.com</code>
   ** as your primary cookie domain, users will be able to perform single
   ** sign-on for resources on <code>b.com</code> and on <code>a.b.com</code>.
   ** However, if you specify <code>a.b.com</code> as your primary cookie
   ** domain, users will have to re-authenticate when they request resources
   ** on <code>b.com</code>.
   ** <p>
   ** <b>Default</b>:
   ** <br>
   ** If the client side domain can be determined during registration, the
   ** Primary Cookie Domain is populated with that value. However, if no
   ** domain is found, there is no value and <code>Access Agent</code> uses
   ** the host-based cookie.
   */
  COOKIE_DOMAIN_PRIMARY("cookieDomainPrimary", Type.STRING, false, null),

  /**
   ** Debugging can be enabled or not.
   ** <br>
   ** When set to <code>true</code>, the <code>Access Server</code> logs
   ** messages for:
   ** <ul>
   **   <li>Login success and login failure events
   **   <li>Logout success and logout failure events
   **   <li>Log messages at different logging levels (FATAL, ERROR, WARNING,
   **       DEBUG, TRACE), each of which indicates severity in descending order.
   ** </ul>
   ** <p>
   ** <b>Default</b>: <code>false</code>
   */
  DEBUG("debug", Type.BOOLEAN, false, Boolean.FALSE.toString()),

  /**
   ** When enabled, this element denies access to all resources to which access
   ** is not explicitly allowed by a rule or policy. Enabling this can limit the
   ** number of times the <code>Access Agent</code> queries the
   ** <code>Access Server</code>, and can improve performance for large or busy
   ** <code>Application Domain</code>s.
   ** <p>
   ** <b>Important</b>
   ** <br>
   ** <b>Deny on Not Protected</b> overrides <b>Host Identifiers</b> and
   ** <b>Preferred Host</b>. Oracle recommends enabling
   ** <b>Deny on Not Protected</b>. Otherwise security holes can occur in large
   ** installations with multiple <b>Host Identifiers</b>, virtual hosts, and
   ** other complex configurations.
   ** <p>
   ** <b>Default</b>: <code>true</code>
   */
  DENY_NOT_PROTECTED("denyNotProtected", Type.BOOLEAN, true, Boolean.TRUE.toString()),

  DESCRIPTION("description", Type.STRING, false, null),

  FUSION_APPLICATION("fusionApplication", Type.BOOLEAN, false, Boolean.FALSE.toString()),

  /**
   ** This identifier represents the Web Server host.
   ** <br>
   ** This is automatically seeded with the value in the agent name field.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** You can register multiple <code>Access Agent</code> (or Access Clients)
   ** under a single host identifier with the same Application Domain and
   ** policies, as follows:
   ** <ol>
   **   <li>When you register a <code>Access Agent</code>, allow the process to
   **       create a host identifier (a name of your choice), and enable
   **       <b>Auto Create Policies</b>.
   **   <li>Register a second <code>Access Agent</code> with the same host
   **       identifier as Step 1, and clear the <b>Auto Create Policies</b> box
   **       to eliminate policy creation.
   ** </ol>
   */
  HOST_IDENTIFIER("hostIdentifier", Type.STRING, true, "SERVER_NAME"),

  /**
   ** The trusted user for impersonation, in Active Directory. This user should
   ** not be used for anything other than impersonation. The constraints are the
   ** same as any other user in Active Directory.
   ** <b>Note</b>:
   ** <br>
   ** SharePoint impersonation is separate and distinct from the
   ** <code>Access Manager</code> user impersonation feature described in the
   ** <a href="https://docs.oracle.com/cd/E52734_01/oam/AIDEV/impersonation.htm#AIDEV422">
   ** Oracle Fusion Middleware Developer's Guide for Oracle Access Management}</a>.
   */
  IMPERSONATION_USERNAME("impersonationUsername", Type.STRING, false, null),

  /**
   ** This is the trusted user password for impersonation. The constraints are
   ** the same as any other user password in Active Directory.
   ** <p>
   ** Oracle recommends that the user choose a very complex password, because
   ** the trusted user is granted powerful permissions. Also, check the box
   ** Password Never Expires. The impersonation module should be the only entity
   ** that ever sees the trusted user account. It is extremely difficult for an
   ** outside agency to discover that the password has expired.
   */
  IMPERSONATION_PASSWORD("impersonationPassword", Type.STRING, false, null),

  /**
   ** IP address validation is a function that determines if a client's IP
   ** address is the same as the IP address stored in the cookie generated for
   ** single sign-on. The IPValidation parameter turns IP address validation on
   ** and off; it is a WebGate specific parameter found in the WebGate profile.
   ** <p>
   ** If IPValidation is <code>true</code>, the IP address stored in the cookie
   ** must match the client's IP address, otherwise, the SSO cookie is rejected
   ** and the user must reauthenticate. By default, IPValidation is
   ** <code>false</code>. The following is true in regards to enabling and
   ** disabling IP Validation.
   ** <ul>
   **   <li>Enabling IP Validation on the <code>Access Agent</code>
   **       automatically enables it on the <code>Access Server</code> side;
   **       this can be verified in the Access Manager settings.
   **   <li>Disabling IP Validation on the <code>Access Agent</code> will not
   **       disable it on the <code>Access Server</code>.
   **   <li>IP Validation on the <code>Access Server</code> side should be
   **       disabled manually, if and only if it is disabled on all the
   **       <code>Access Agent</code>s.
   **   <li>When IP Validation is enabled on the <code>Access Agent</code>side,
   **       server side IP Validation should never be turned off.
   ** </ul>
   ** <b>Note</b>:
   ** <br>
   ** You can register multiple <code>Access Agent</code> (or Access Clients)
   ** under a single host identifier with the same Application Domain and
   ** policies, as follows:
   ** <ol>
   **   <li>When you register a <code>Access Agent</code>, allow the process to
   **       create a host identifier (a name of your choice), and enable
   **       <b>Auto Create Policies</b>.
   **   <li>Register a second <code>Access Agent</code> with the same host
   **       identifier as Step 1, and clear the <b>Auto Create Policies</b> box
   **       to eliminate policy creation.
   ** </ol>
   ** <b>Default</b>: <code>false</code>
   */
  IP_VALIDATION("ipValidation", Type.BOOLEAN, false, Boolean.FALSE.toString()),

  /**
   ** The URL to <code>oam_logout_success</code>, which clears cookies during
   ** the call back. This can be a URI format without <code>host:port</code>
   ** (recommended), where the <code>Access Server</code> calls back on the
   ** <code>host:port</code> of the original resource request. For example:
   ** <br>
   ** Default: <code>/oam_logout_success</code>
   ** <p>
   ** <b>11g WebGate only</b>
   **
   ** @see   #LOGOUT_REDIRECT_URL
   ** @see   #LOGOUT_TARGET_URL_PARAM
   */
  LOGOUT_CALLBACK_URL("logoutCallbackURL", Type.URI, false, "/oam_logout_success"),

  /**
   ** Specifies the URL (absolute path) to the central logout page
   ** (logout.html).
   ** <br>
   ** This parameter is automatically populated after agent registration
   ** completes. By default, this is based on the <code>Access Server</code>
   ** host name with a default port of 14200. For example:
   ** <br>
   ** Default: <code>/http://access_server_host:14200/oam/server/logout</code>
   ** <p>
   ** <b>11g WebGate only</b>
   **
   ** @see   #LOGOUT_CALLBACK_URL
   ** @see   #LOGOUT_TARGET_URL_PARAM
   */
  LOGOUT_REDIRECT_URL("logoutRedirectURL", Type.URL, false, null),

  /**
   ** The value is the name for the query parameter that the OPSS applications
   ** passes to WebGate during logout; the query parameter specifies the target
   ** URL of the landing page after logout completes.
   ** <br>
   ** Default: <code>end_url</code>
   ** <p>
   ** <b>11g WebGate only</b>
   ** <b>Note</b>:
   ** <br>
   ** The <code>end_url</code> value is configured using
   ** <code>param.logout.targeturl</code> in <code>jps-config.xml</code>.
   **
   ** @see   #LOGOUT_CALLBACK_URL
   ** @see   #LOGOUT_REDIRECT_URL
   */
  LOGOUT_TARGET_URL_PARAM("logoutTargetURLParam", Type.STRING, false, "end_url"),

  /**
   ** The URI to which the user is directed if access to the requested resource
   ** is denied. This is available for both Web and J2EE Agents, each with its
   ** own format requirements:
   ** <ul>
   **   <li><code>Web Agent</code> (full URL): http://host:port/context/accessDeniedURL.html
   **   <li><code>J2EE Agent</code> (relative URL): /context/accessDeniedURL.htm
   **   <li>
   ** </ul>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_ACCESS_DENIED("openSSOAccessDeniedURI", Type.URI, false, null),

  /**
   ** Defines the directory path for audit logs from the
   ** <code>Access Server</code>:
   ** <ul>
   **   <li>Audit Login events
   **   <li>Audit Logout success events
   ** </ul>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_AUDIT_DIRECTORY("openSSOAuditDirectory", Type.STRING, false, null),

  /**
   ** Defines the audit log file name.
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_AUDIT_FILENAME("openSSOAuditFilename", Type.STRING, false, null),

  /**
   ** The name of the OpenSSO cookie.
   ** <p>
   ** Default: <code>iPlanetDirectoryPro</code>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_COOKIE_NAME("openSSOCookieName", Type.STRING, false, "iPlanetDirectoryPro"),

  /**
   ** Defines the character to be used as a separator when multiple values of
   ** the same attribute are being set as a cookie. For example, the pipe symbol
   ** "|", can be used.
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_COOKIE_SEPARATOR("openSSOCookieSeparator", Type.STRING, false, null),

  /**
   ** Identifies whether cookie encoding is enabled or not.
   ** <p>
   ** Default: <code>true</code>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_COOKIE_ENCODING("openSSOCookieEncoding", Type.BOOLEAN, false, Boolean.FALSE.toString()),

  /**
   ** The filesystem directory path for audit logs from the OAM Server:
   ** <ul>
   **   <li>Audit Login events
   **   <li>Audit Logout success events
   ** </ul>
   ** With <code>openSSOAgentDebugLevel</code> set, you can configure the
   ** directory path for logged agent messages.
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_DEBUG_DIRECTORY("openSSODebugDirectory", Type.STRING, false, null),

  /**
   ** Defines the filesystem directory path to the local component event logging
   ** file.
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_DEBUG_FILENAME("openSSODebugFilenameDebug", Type.STRING, false, null),

  /**
   ** Debugging can be enabled or not.
   ** <br>
   ** When set, the <code>Access Server</code> logs messages for:
   ** <ul>
   **   <li>Login success and login failure events
   **   <li>Logout success and logout failure events
   **   <li>Log messages at different logging levels (FATAL, ERROR, WARNING,
   **       DEBUG, TRACE), each of which indicates severity in descending order.
   ** </ul>
   ** <p>
   ** Default: <code>Error</code>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_DEBUG_LEVEL("openSSODebugLevel", Type.STRING, false, "Error"),

  /**
   ** Certain applications rely on the presence of user-specific profile
   ** information in some form to process user requests appropriately. The
   ** <code>Access Agent</code> can make these attributes from the user's
   ** profile available in various forms. when you specify a Fetch Mode for
   ** Profile, Response, or Session Attributes:
   ** <ul>
   **   <li><code>NONE</code>           - No attributes are fetched.
   **   <li><code>HTTP_HEADER</code>    - When the agent is configured to
   **                                     provide the LDAP attributes as HTTP
   **                                     headers, these attributes can be
   **                                     retrieved.
   **   <li><code>REQUEST_HEADER</code> - When the agent is configured to provide
   **                                     the LDAP attributes as request
   **                                     attributes, the agent populates these
   **                                     attribute values into
   **                                     HttpServletRequest as attributes that
   **                                     can later be used by the application
   **                                     as necessary. For example, fetch
   **                                     profile attributes, assign a mode to
   **                                     the profile attribute property, and
   **                                     map the profile attributes to be
   **                                     populated under specific names for the
   **                                     currently authenticated user.
   **   <li><code>HTTP_COOKIE</code>    - When the agent is configured to
   **                                     provide the LDAP attributes as
   **                                     cookies, the necessary values are set
   **                                     as server specific cookies by the
   **                                     agent with the path specified as "/."
   **                                     multi-valued attributes are set as a
   **                                     single cookie value such that all
   **                                     values of the attribute are
   **                                     concatenated into a single string
   **                                     using a separator character that can
   **                                     be specified by the property labeled
   **                                     Cookie Separator.
   ** </ul>
   ** <p>
   ** <b>OpenSSO only</b>
   **
   ** @see    #OPENSSO_COOKIE_SEPARATOR
   ** @see    #OPENSSO_FETCHMODE_SESSION
   ** @see    #OPENSSO_FETCHMODE_RESPONSE
   */
  OPENSSO_FETCHMODE_PROFILE("openSSOFetchModeProfile", Type.STRING, false, null),

  /**
   ** Certain applications rely on the presence of user-specific profile
   ** information in some form to process user requests appropriately. The
   ** <code>Access Agent</code> can make these attributes from the user's
   ** profile available in various forms. when you specify a Fetch Mode for
   ** Profile, Response, or Session Attributes:
   ** <ul>
   **   <li><code>NONE</code>           - No attributes are fetched.
   **   <li><code>HTTP_HEADER</code>    - When the agent is configured to
   **                                     provide the LDAP attributes as HTTP
   **                                     headers, these attributes can be
   **                                     retrieved.
   **   <li><code>REQUEST_HEADER</code> - When the agent is configured to provide
   **                                     the LDAP attributes as request
   **                                     attributes, the agent populates these
   **                                     attribute values into
   **                                     HttpServletRequest as attributes that
   **                                     can later be used by the application
   **                                     as necessary. For example, fetch
   **                                     profile attributes, assign a mode to
   **                                     the profile attribute property, and
   **                                     map the profile attributes to be
   **                                     populated under specific names for the
   **                                     currently authenticated user.
   **   <li><code>HTTP_COOKIE</code>    - When the agent is configured to
   **                                     provide the LDAP attributes as
   **                                     cookies, the necessary values are set
   **                                     as server specific cookies by the
   **                                     agent with the path specified as "/."
   **                                     multi-valued attributes are set as a
   **                                     single cookie value such that all
   **                                     values of the attribute are
   **                                     concatenated into a single string
   **                                     using a separator character that can
   **                                     be specified by the property labeled
   **                                     Cookie Separator.
   ** </ul>
   ** <p>
   ** <b>OpenSSO only</b>
   **
   ** @see    #OPENSSO_COOKIE_SEPARATOR
   ** @see    #OPENSSO_FETCHMODE_PROFILE
   ** @see    #OPENSSO_FETCHMODE_SESSION
   */
  OPENSSO_FETCHMODE_RESPONSE("openSSOFetchModeResponse", Type.STRING, false, null),

  /**
   ** Certain applications rely on the presence of user-specific profile
   ** information in some form to process user requests appropriately. The
   ** <code>Access Agent</code> can make these attributes from the user's
   ** profile available in various forms. when you specify a Fetch Mode for
   ** Profile, Response, or Session Attributes:
   ** <ul>
   **   <li><code>NONE</code>           - No attributes are fetched.
   **   <li><code>HTTP_HEADER</code>    - When the agent is configured to
   **                                     provide the LDAP attributes as HTTP
   **                                     headers, these attributes can be
   **                                     retrieved.
   **   <li><code>REQUEST_HEADER</code> - When the agent is configured to provide
   **                                     the LDAP attributes as request
   **                                     attributes, the agent populates these
   **                                     attribute values into
   **                                     HttpServletRequest as attributes that
   **                                     can later be used by the application
   **                                     as necessary. For example, fetch
   **                                     profile attributes, assign a mode to
   **                                     the profile attribute property, and
   **                                     map the profile attributes to be
   **                                     populated under specific names for the
   **                                     currently authenticated user.
   **   <li><code>HTTP_COOKIE</code>    - When the agent is configured to
   **                                     provide the LDAP attributes as
   **                                     cookies, the necessary values are set
   **                                     as server specific cookies by the
   **                                     agent with the path specified as "/."
   **                                     multi-valued attributes are set as a
   **                                     single cookie value such that all
   **                                     values of the attribute are
   **                                     concatenated into a single string
   **                                     using a separator character that can
   **                                     be specified by the property labeled
   **                                     Cookie Separator.
   ** </ul>
   ** <p>
   ** <b>OpenSSO only</b>
   **
   ** @see    #OPENSSO_COOKIE_SEPARATOR
   ** @see    #OPENSSO_FETCHMODE_PROFILE
   ** @see    #OPENSSO_FETCHMODE_RESPONSE
   */
  OPENSSO_FETCHMODE_SESSION("openSSOFetchModeSession", Type.STRING, false, null),

  /**
   ** The Agent filter is installed within the protected application. It
   ** facilitates the enforcement of security policies, governing the access to
   ** all resources within the protected application. Every application
   ** protected by the J2EE Agent must have its deployment descriptors changed
   ** to reflect that it is configured to use the agent filter. Applications
   ** that do not have this setting are not protected by J2EE the Agent and
   ** ^might malfunction or become unusable if deployed on a deployment
   ** container where the Agent realm is installed.
   ** <p>
   ** Filter modes must be set for the J2EE Agent by choosing one of the
   ** following options:
   ** <ul>
   **   <li>SSO_ONLY (Access Manager Authentication Only):
   **                Enables the least restrictive mode of operation for the
   **                filter; the agent simply ensures that all users who try to
   **                access protected web resources are authenticated.
   **   <li>URL_Policy (Access Manager Authentication and Authorization):
   **                Enables the agent filter to enforce URL policies. By
   **                default, with Web Agents,
   **                .com.sun.identity.agents.config.sso.onlyattribute is set to
   **                "false".
   ** </ul>
   ** <br>
   ** Default: <code>URL_Policy</code>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_FILTER_MODE("openSSOFilterMode", Type.STRING, false, "URL_Policy"),

  OPENSSO_ORGANIZATION_NAME("openSSOOrganizationName", Type.STRING, false, null),

  /**
   ** <p>
   ** Default: <code>10</code>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_SESSION_MAX("openSSOSessionMax", Type.STRING, false, "10"),

  /**
   ** The period of time (in seconds), after which the session times out and the
   ** user must re-authenticate.
   ** <p>
   ** Default: <code>0</code>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_SESSION_TIMEOUT("openSSOSessionTimeOut", Type.STRING, false, "0"),

  /**
   ** Enables OpenSSO <code>Access Agent</code> to bootstrap and authenticate
   ** with the <code>Access Server</code> using the <code>OpenSSO Proxy</code>
   ** provided by <code>Access Manager</code>:
   ** <ol>
   **   <li>The end user accesses the application or resource protected by the
   **       OpenSSO <code>Access Agen</code>, which redirects the
   **       unauthenticated user to the <code>Access Server</code> for
   **       authentication.
   **   <li>After successful authentication, the <code>OpenSSO Proxy</code>
   **       redirects the user back to the protected application or resource and
   **       sets the <code>OpenSSO Session ID</code> in the response cookie.
   **   <li>The authenticated user with a valid OpenSSO session accesses the
   **       application or resource protected by the OpenSSO
   **       <code>Access Agen</code>, which validates the session against the
   **       <code>Access Server</code> using the <code>OpenSSO Proxy</code>.
   **   <li>The end user gets access based on <code>Access Manager</code>
   **       authorization policy.
   ** </ol>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_SSO_ONLY("openSSOSSOOnly", Type.BOOLEAN, false, null),

  /**
   ** OpenSSO agent types can be either:
   ** <ul>
   **   <li>Web  - Use with Web resources and Web resource URLs.
   **   <li>J2EE - Use J2EE type agents for Java EE resources and applications.
   ** </ul>
   ** <br>
   ** Default: <code>J2EE</code>
   ** <p>
   ** For the J2EE Agent, Filter modes must be set by choosing either:
   ** <ul>
   **   <li>SSO_ONLY (Access Manager Authentication Only):
   **                Enables the least restrictive mode of operation for the
   **                filter; the agent simply ensures that all users who try to
   **                access protected web resources are authenticated.
   **   <li>URL_Policy (Access Manager Authentication and Authorization):
   **                Enables the agent filter to enforce URL policies. By
   **                default, with Web Agents,
   **                .com.sun.identity.agents.config.sso.onlyattribute is set to
   **                "false".
   ** </ul>
   ** <b>Note</b>:
   ** <br>
   ** Both agent types provide access protection when you also choose SSO only.
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_TYPE("openSSOType", Type.STRING, false, "J2EE"),

  /**
   ** Attribute retrieval fetches and sets user attributes in the HTTP request
   ** for consumption by the applications.
   ** <br>
   ** The following Attribute Mapping panels are available:
   ** <ul>
   **   <li>Profile Attributes
   **   <li>Response Attributes
   **   <li>Session Attributes
   ** </ul>
   ** <p>
   ** <b>OpenSSO only</b>
   **
   ** @see    #OPENSSO_FETCHMODE_PROFILE
   ** @see    #OPENSSO_FETCHMODE_SESSION
   ** @see    #OPENSSO_FETCHMODE_RESPONSE
   */
  OPENSSO_USER_ATTRIBUTE_NAME("openSSOUserAttributeName", Type.STRING, false, null),

  /**
   ** <ul>
   **   <li>USER_ID
   **   <li>HTTP_HEADER
   **   <li>PROFILE_ATTRIBUTE
   **   <li>SESSION_PROPERTY
   ** </ul>
   ** <p>
   ** Default: <code>User_ID</code>
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_USER_MAPPING_MODE("openSSOUserMappingMode", Type.STRING, false, "User_ID"),
  OPENSSO_USERID_PARAM("openSSOUserIDParam", Type.STRING, false, null),
  OPENSSO_USERID_PARAM_TYPE("openSSOUserIDParamType", Type.STRING, false, null),
  OPENSSO_USER_PRINCIPAL("openSSOUserPrincipal", Type.STRING, false, null),
  OPENSSO_USER_TOKEN("openSSOUserToken", Type.STRING, false, null),

  /**
   ** <p>
   ** <b>OpenSSO only</b>
   */
  OPENSSO_VERSION("openSSOVersion", Type.STRING, false, null),

  /**
   ** Administrator log in ID for the <code>mod_osso</code> instance.
   ** <br>
   ** For example, <code>Application Administrator</code>.
   ** Default: <code>None specified</code>
   ** <p>
   ** <b>OSSO only</b>
   */
  ORCLSSO_ADMIN_ID("orclSSOAdminId", Type.STRING, false, null),

  /**
   ** Administrator details for the <code>mod_osso</code> instance.
   ** <br>
   ** For example, <code>Application Administrator</code>.
   ** Default: <code>None specified</code>
   ** <p>
   ** <b>OSSO only</b>
   */
  ORCLSSO_ADMIN_INFO("orclSSOAdminInfo", Type.STRING, false, null),

  /**
   ** The redirect URL to be used when logging out.
   ** <br>
   ** This redirects the user to the global logout page on the server:
   ** <code>osso_logout_success</code>.
   ** <br>
   ** By default, the fully qualified host and port specified with the Agent
   ** Base URL are used:
   ** <br>
   ** Default: https://example.domain.com:7001/osso_logout_success
   ** <p>
   ** <b>OSSO only</b>
   */
  ORCLSSO_LOGOUT_URL("orclSSOLogoutURL", Type.URL, false, null),

  /**
   ** The redirect URL to be used if authentication fails.
   ** <br>
   ** By default, <code>osso_login_failure</code> on the fully qualified host
   ** and port specified with the Agent Base URL are used:
   ** <br>
   ** Default: <code>https://example.domain.com:7001/osso_login_failure</code>
   ** <p>
   ** <b>OSSO only</b>
   */
  ORCLSSO_FAILURE_URL("orclSSOFailureURL", Type.URL, false, null),

  /**
   ** The redirect URL to be used for the Home page after authentication.
   ** <br>
   ** By default, the fully qualified host and port specified with the Agent
   ** Base URL are used:
   ** <br>
   ** Default: <code>https://example.domain.com:7001</code>
   ** <p>
   ** <b>OSSO only</b>
   */
  ORCLSSO_HOME_URL("orclSSOHomeURL", Type.URL, false, null),

  /**
   ** The absolute file system directory path to the <code>mod_osso</code>
   ** agent.
   ** <p>
   ** <b>OSSO only</b>
   */
  ORCLSSO_ORACLE_HOME_PATH("orclSSOOracleHomePath", Type.STRING, false, null),

  /**
   ** First month, day, and year for which log in to the application is allowed
   ** by the server.
   ** <br>
   ** Default: The date the Agent was registered.
   ** <p>
   ** <b>OSSO only</b>
   */
  ORCLSSO_START_DATE("orclSSOStartDate", Type.STRING, false, null),

  /**
   ** The redirect URL to be used upon successful authentication.
   ** <br>
   ** By default, <code>osso_login_success</code> on the fully qualified host
   ** and port specified with the Base URL are used. For example:
   ** <br>
   ** Default: <code>https://example.domain.com:7001/osso_login_success</code>
   ** <p>
   ** <b>OSSO only</b>
   */
  ORCLSSO_SUCCESS_URL("orclSSOSuccessURL", Type.URL, false, null),

  /**
   ** Specifies how the hostname appears in all HTTP requests as users attempt
   ** to access the protected Web Server.
   ** <br>
   ** The hostname within the HTTP request is translated into the value
   ** entered into this field regardless of the way it was defined in a user's
   ** HTTP request.
   ** <p>
   ** The Preferred Host function prevents security holes that can be
   ** inadvertently created if a host's identifier is not included in the Host
   ** Identifiers list. However, it cannot be used with virtual Web hosting.
   ** <br>
   ** For virtual hosting, you must use the Host Identifiers feature.
   */
  PREFERRED_HOST("preferredHost", Type.STRING, false, "SERVER_NAME"),

  /**
   ** Level of communication transport security between the
   ** <code>Access Agent</code> and the <code>Access Server</code> (this must
   ** match the level specified for the <code>Access Server</code>):
   ** <ul>
   **   <li><b>Open</b>   - No transport security
   **   <li><b>Simple</b> - SSL v3/TLS v1.0 secure transport using dynamically
   **                       generated session keys
   **   <li><b>Cert</b>   - SSL v3/TLS v1.0 secure transport using server
   **                       side x.509 certificates.
   **                       <br>
   **                       Choosing this option displays a field where you
   **                       can enter the Agent Key Password.
   **                       <br>
   **                       Agent Key Password: The private key file
   **                       (aaa_key.pem) is encrypted using DES algorithm.
   **                       The Agent Key Password is saved in obfuscated
   **                       format in password.xml and is required by the
   **                       server to generate password.xml. However, this
   **                       password is not retained by the server. When
   **                       editing an 11g Webgate registration, password.xml
   **                       is updated only when the mode is changed from
   **                       <b>Open</b> to <b>Cert</b> or <b>Simple</b> to
   **                       <b>Cert</b>. In <b>Cert</b> mode, once generated,
   **                       password.xml cannot be updated. Editing the Agent
   **                       Key Password does not result in creation of a new
   **                       password.xml.
   ** </ul>
   ** <br>
   ** Default: <code>open</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  SECURITY("security", Type.STRING, true, Security.OPEN.value),

  /**
   ** Release 7.0.4 <code>Access Agent</code>s enforced their own idle session
   ** timeout only.
   ** <br>
   ** 10.1.4.0.1 <code>Access Agent</code>s enforced the most restrictive
   ** timeout value among all <code>Access Agent</code>s the token had
   ** visited.
   ** <br>
   ** With 10g (10.1.4.3), the 7.0.4 behavior was reinstated as the default
   ** with this element.
   ** To set Idle Session Timeout logic:
   ** <ul>
   **   <li>The default value of <b>leastComponentIdleTimeout</b> instructs
   **       the <code>Access Agent</code> to use the most restrictive timeout
   **       value for idle session timeout enforcement.
   **   <li>A value of <b>currentComponentIdleTimeout</b> instructs the
   **       <code>Access Agent</code>s to use the current
   **       <code>Access Agent</code> timeout value for idle session timeout
   **       enforcement.
   ** </ul>
   ** <br>
   ** Default: <code>3600</code>
   ** <p>
   ** <b>10g WebGate only</b>
   ** <b>Mandatory parameter</b>
   */
  SESSION_IDLE_TIMEOUT("sessionIdleTimeout", Type.INTEGER, false, "3600"),

  /**
   ** The frequency (in seconds) with which the <code>Access Server</code>
   ** checks its connections to the directory server. For example, if you set a
   ** value of 60 seconds, the <code>Access Server</code> checks its connections
   ** every 60 seconds from the time it comes up.
   ** <br>
   ** Default: <code>60</code>
   */
  SLEEP("sleep", Type.INTEGER, false, "60"),

  /**
   ** Specifies whether the <code>Access Agent</code> is enabled or disabled.
   ** <br>
   ** Default: <code>Enabled</code>
   */
  STATE("state", Type.STATUS, false, Status.ENABLED.value),

  /**
   ** Number representing the point when the <code>Access Agent</code> opens
   ** connections to a secondary <code>Access Server</code>.
   ** <br>
   ** For example, if you type 30 in this field and the number of connections
   ** to primary <code>Access Server</code> falls to 29, this
   ** <code>Access Agent</code> opens connections to secondary
   ** <code>Access Server</code>.
   ** <br>
   ** Default: <code>1</code>
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  THRESHOLD_FAILOVER("failoverThreshold", Type.INTEGER, false, "1"),

  /**
   ** Threshold Number (in seconds) to wait for a response from the
   ** <code>Access Server</code>.
   ** <br>
   ** If this parameter is set, it is used as an application TCP/IP timeout
   ** instead of the default TCP/IP timeout.
   ** <p>
   ** A typical value for this parameter is between 30 and 60 seconds. If set
   ** to a very low value, the socket connection can be closed before a reply
   ** from <code>Access Server</code> is received, resulting in an error.
   ** <p>
   ** For example, suppose an <code>Access Agent</code> is configured to talk
   ** to one primary <code>Access Server</code> and one secondary
   ** <code>Access Server</code>. If the network wire is pulled from the
   ** primary <code>Access Server</code>, the <code>Access Agent</code> waits
   ** for the TCP/IP timeout to learn that there is no connection to the
   ** primary <code>Access Server</code>. The <code>Access Agent</code> tries
   ** to reestablish the connections to available servers starting with the
   ** primary <code>Access Server</code>. Again, the <code>Access Agent</code>
   ** waits for the TCP/IP timeout to determine if a connection can be
   ** established. If it cannot, the next server in the list is tried. If a
   ** connection can be established to another <code>Access Server</code>
   ** (either a primary or secondary), the requests are re-routed. However
   ** this can take longer than desired.
   ** <p>
   ** When finding new connections, <code>Access Agent</code> checks the list
   ** of available servers in the order specified in its configuration. If
   ** there is only one primary <code>Access Server</code> and one secondary
   ** <code>Access Server</code> specified, and the connection to the primary
   ** <code>Access Server</code> times out, the <code>Access Agent</code>
   ** still tries the primary <code>Access Server</code> first. As a result,
   ** the <code>Access Agent</code> cannot send requests to an
   ** <code>Access Server</code> for a period greater than twice the setting
   ** in the <code>Access Server</code> Timeout Threshold.
   ** <br>
   ** Default: <code>-1</code> (default network TCP/IP timeout is used)
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  THRESHOLD_TIMEOUT("timeoutThreshold", Type.INTEGER, true, "-1"),

  /**
   ** Maximum valid time period for an agent token (the content of
   ** <b><code>OAMAuthnCookie</code></b> for 11g WebGate).
   ** <br>
   ** Default: <code>3600</code>
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For 10g WebGates, use <b>Cookie Session Time</b> to set the Token Validity
   ** Period.
   **
   ** @see   #COOKIE_SESSION_TIME
   */
  TOKEN_VALIDITY_TIME("tokenValidityPeriod", Type.INTEGER, true, "3600"),

  /**
   ** SSO Token version values:
   ** <ul>
   **   <li>v3.0: - Most secure token using AES encryption standard for
   **               encrypting tokens exchanged between
   **               <code>Access Server</code> and <code>mod_osso</code>. This
   **               is the default value. This was supported by OSSO 10.1.4.3
   **               patch set.
   **   <li>v1.4: - This is supported by OSSO 10g prior to OSSO 10.1.4.3 patch
   **               set. Uses DES encryption standard.
   **   <li>v1.2: - This used to be version of tokens exchanged between OSSO
   **               partners prior to OSSO 10.1.4.0.1. Uses DES.
   ** </ul>
   ** <br>
   ** Default: <code>v3.0</code>
   ** <p>
   ** <b>OSSO only</b>
   */
  TOKEN_VERSION("tokenVersion", Type.STRING, false, "v3.0"),

  /**
   ** Default: <code>None specified</code>
   ** <p>
   ** <b>OSSO only</b>
   */
  UPDATE_MODE("updateMode", Type.STRING, false, null),

  /**
   ** <code>Virtual Host</code> should be set to <code>true</code> if the
   ** <code>Access Agent</code> is installed on a Web Server that contains
   ** multiple Web Site and domain names. The <code>Access Agent</code> must
   ** reside in a location that enables it to protect all of the Web Sites on
   ** that server.
   ** <br>
   ** Default: <code>true</code>
   */
  VIRTUAL_HOST("virtualHost", Type.BOOLEAN, false, Boolean.TRUE.toString())
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the entity type declaration for debugging purpose */
  public static final String ENTITY           = "Access Agent";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:915454229678156652")
  private static final long  serialVersionUID = -1L;

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
  // enum Mode
  // ~~~~ ~~~~
  /**
   ** enum to specify the mode of an <code>Access Agent</code> request.
   */
  public enum Mode {
      REPORT("infoagent")
    , CREATE("agentcreate")
    , MODIFY("agentupdate")
    , DELETE("agentdelete")
    , VALIDATE("agentvalidate")
    , PASSWORD("agentPasswordUpdate")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:8839548363584898825")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Mode</code>
     **
     ** @param  value            the mode value.
     */
    Mode(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper request mode from the given string
     ** value.
     **
     ** @param  value              the string value the request mode should be
     **                            returned for.
     **
     ** @return                    the request mode.
     */
    public static Mode from(final String value) {
      for (Mode cursor : Mode.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Version
  // ~~~~ ~~~~~~~
  /**
   ** Constraint to specify the version of an <code>Access Agent</code>.
   */
  public enum Version {
      AGENT11("11g")
    , AGENT10("10g")
    , LEGACYSUN("openSSO")
    , LEGACYORCL("orclSSO")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-8640350720825990104")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Version</code>
     **
     ** @param  value            the version string value.
     */
    Version(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper agent version from the given string
     ** value.
     **
     ** @param  value              the string value the agent version should be
     **                            returned for.
     **
     ** @return                    the agent version.
     */
    public static Version from(final String value) {
      for (Version cursor : Version.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Security
  // ~~~~ ~~~~~~~~
  /**
   ** enum to specify the security mode of an <code>Access Agent</code> request.
   */
  public enum Security {
      OPEN("open")
    , SIMPLE("simple")
    , CERIFICATE("cert")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-9118558771649950421")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Security</code>
     **
     ** @param  value            the mode value.
     */
    Security(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper request mode from the given string
     ** value.
     **
     ** @param  value              the string value the request mode should be
     **                            returned for.
     **
     ** @return                    the request mode.
     */
    public static Security from(final String value) {
      for (Security cursor : Security.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Status
  // ~~~~ ~~~~~~
  /**
   ** enum to specify the security mode of an <code>Access Agent</code> request.
   */
  public enum Status {
      ENABLED("Enabled")
    , DISABLED("Disabled")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-4258295132818741800")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Status</code>
     **
     ** @param  value            the mode value.
     */
    Status(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper request mode from the given string
     ** value.
     **
     ** @param  value              the string value the request mode should be
     **                            returned for.
     **
     ** @return                    the request mode.
     */
    public static Status from(final String value) {
      for (Status cursor : Status.values()) {
        if (cursor.value.equals(value))
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
   ** Constructs a <code>AccessAgentProperty</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  AccessAgentProperty(final String id, final Type type, final boolean required, final String defaultValue) {
    this.id           = id;
    this.type         = type;
    this.required     = required;
    this.defaultValue = defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (ServiceOperation)
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
   ** Factory method to create a proper access server property from the given
   ** string value.
   **
   ** @param  value              the string value the access server property
   **                            should be returned for.
   **
   ** @return                    the access server property.
   */
  public static AccessAgentProperty from(final String value) {
    for (AccessAgentProperty cursor : AccessAgentProperty.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}