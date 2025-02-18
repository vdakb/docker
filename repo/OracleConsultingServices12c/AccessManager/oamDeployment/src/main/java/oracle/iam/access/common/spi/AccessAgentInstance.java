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

    File        :   AccessAgentInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessAgentInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.io.PrintStream;
import java.io.StringReader;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;

import javax.xml.transform.stream.StreamSource;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureException;
import oracle.iam.access.common.FeatureMessage;
import oracle.iam.access.common.FeatureResourceBundle;

import oracle.iam.access.common.spi.schema.LogInUrls;
import oracle.iam.access.common.spi.schema.LogOutUrls;
import oracle.iam.access.common.spi.schema.BaseRequest;
import oracle.iam.access.common.spi.schema.ObjectFactory;
import oracle.iam.access.common.spi.schema.OrclSSOCreate;
import oracle.iam.access.common.spi.schema.OrclSSOUpdate;
import oracle.iam.access.common.spi.schema.OpenSSOCreate;
import oracle.iam.access.common.spi.schema.OpenSSOUpdate;
import oracle.iam.access.common.spi.schema.Agent10gCreate;
import oracle.iam.access.common.spi.schema.Agent10gResponse;
import oracle.iam.access.common.spi.schema.Agent10gUpdate;
import oracle.iam.access.common.spi.schema.Agent11gCreate;
import oracle.iam.access.common.spi.schema.Agent11gResponse;
import oracle.iam.access.common.spi.schema.Agent11gUpdate;
import oracle.iam.access.common.spi.schema.BaseResponse;
import oracle.iam.access.common.spi.schema.NotEnforcedUrls;
import oracle.iam.access.common.spi.schema.PrimaryServerList;
import oracle.iam.access.common.spi.schema.SecondaryServerList;
import oracle.iam.access.common.spi.schema.UserDefinedParameters;
import oracle.iam.access.common.spi.schema.IpValidationExceptions;
import oracle.iam.access.common.spi.schema.MiscellaneousProperties;
import oracle.iam.access.common.spi.schema.ProfileAttributeMapping;
import oracle.iam.access.common.spi.schema.SessionAttributeMapping;
import oracle.iam.access.common.spi.schema.ResponseAttributeMapping;
import oracle.iam.access.common.spi.schema.Server;
import oracle.iam.access.common.spi.schema.UserDefinedParam;

////////////////////////////////////////////////////////////////////////////////
// class AccessAgentInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessAgentInstance</code> represents an <code>Access Agent</code> in
 ** <i>Oracle Access Manager</i> (<b>OAM</b>) infrastructure that might be
 ** created, deleted or configured after or during an import operation.
 ** <p>
 ** A <code>Access Agent</code> is a web-server plug-in for <i>Oracle Access
 ** Manager</i> (<b>OAM</b>) that intercepts HTTP requests and forwards them to
 ** the <code>Access Server</code> for authentication and authorization.
 ** <p>
 ** <i>Oracle Access Manager</i> (<b>OAM</b>) authenticates each user with a
 ** customer-specified authentication method to determine the identity and
 ** leverages information stored in the user identity store. <i>Oracle Access
 ** Manager</i> (<b>OAM</b>) authentication supports several authentication
 ** methods and different authentication levels. Resources with varying degrees
 ** of sensitivity can be protected by requiring higher levels of authentication
 ** that correspond to more stringent authentication methods.
 ** <p>
 ** When a user tries to access a protected application, the request is received
 ** by <b>OAM</b> which checks for the existence of the <b>SSO</b> cookie.
 ** <p>
 ** After authenticating the user and setting up the user context and token,
 ** <b>OAM</b> sets the <b>SSO</b> cookie and encrypts the cookie with the
 ** SSO Server key (which can be decrypted only by the SSO Engine).
 ** <p>
 ** Depending on the actions (responses in OAM 11g) specified for authentication
 ** success and authentication failure, the user may be redirected to a specific
 ** URL, or user information might be passed on to other applications through a
 ** header variable or a cookie value.
 ** <p>
 ** Based on the authorization policy and results of the check, the user is
 ** allowed or denied access to the requested content. If the user is denied
 ** access, she is redirected to another URL (specified by the administrator in
 ** <code>Access Agent</code> registration).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessAgentInstance extends RegistrationInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link ObjectFactory} to create the request payload which will be send
   ** to the registration servlet.
   */
  private static final ObjectFactory factory = new ObjectFactory();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  AccessAgentProperty.Version version;
  PrimaryServerList           primaryServer;
  SecondaryServerList         secondaryServer;
  UserDefinedParameters       userDefinedParameter;
  LogInUrls                   loginURL;
  LogOutUrls                  logoutURL;
  IpValidationExceptions      validationException;

  // OpenSSO specific attributes
  NotEnforcedUrls             notEnforcedResource;
  ProfileAttributeMapping     profileMapping;
  SessionAttributeMapping     sessionMapping;
  ResponseAttributeMapping    responseMapping;
  MiscellaneousProperties     miscellaneousProperty;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessAgentInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessAgentInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Access and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Called to inject the argument for attribute <code>version</code>.
   **
   ** @param  version            the version of the agent instance in Oracle
   **                            Access Manager to handle.
   **                            Allowed object is
   **                            {@link AccessAgentProperty.Version}.
   */
  public void version(final AccessAgentProperty.Version version) {
    this.version = version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryServer
  /**
   ** Called to inject the argument for attribute <code>primaryServer</code>.
   ** <p>
   ** Identifies Primary Server details for an <code>Access Agent</code>.
   ** <br>
   ** The default is based on the OAM Server:
   ** <ol>
   **   <li>Server Name
   **   <li>Host Name
   **   <li>Host Port
   **   <li>Max Number (maximum connections the <code>Access Agent</code> will
   **       establish with the <code>Access Server</code> (not the maximum total
   **       connections the <code>Access Agent</code> can establish with all
   **       <code>Access Server</code>s).)
   ** </ol>
   **
   ** @param  value              the list of primary <code>Access Servers</code>
   **                            to set for the instance.
   **                            Allowed object is {@link PrimaryServerList}.
   */
  public void primaryServer(final PrimaryServerList value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "primary"));

    // prevent bogus state
    if (this.primaryServer != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "primary"));

    this.primaryServer = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secondaryServer
  /**
   ** Called to inject the argument for attribute <code>secondaryServer</code>.
   ** <p>
   ** Identifies Secondary Server details for an <code>Access Agent</code>.
   ** <br>
   ** The default is based on the OAM Server:
   ** <ol>
   **   <li>Server Name
   **   <li>Host Name
   **   <li>Host Port
   **   <li>Max Number (maximum connections the <code>Access Agent</code> will
   **       establish with the <code>Access Server</code> (not the maximum total
   **       connections the <code>Access Agent</code> can establish with all
   **       <code>Access Server</code>s).)
   ** </ol>
   **
   ** @param  value              the list of secondary
   **                            <code>Access Servers</code> to set for the
   **                            instance.
   **                            Allowed object is {@link SecondaryServerList}.
   */
  public void secondaryServer(final SecondaryServerList value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "secondary"));

    // prevent bogus state
    if (this.secondaryServer != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "secondary"));

    this.secondaryServer = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notEnforcedResource
  /**
   ** Called to inject the argument for attribute
   ** <code>notEnforcedResource</code>.
   **
   ** @param  value              the list of <code>Resource</code>s to set for
   **                            the instance.
   **                            Allowed object is {@link NotEnforcedUrls}.
   */
  public void notEnforcedResource(final NotEnforcedUrls value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "notEnforcedResource"));

    // prevent bogus state
    if (this.notEnforcedResource != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "notEnforcedResource"));

    this.notEnforcedResource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginURL
  /**
   ** Called to inject the argument for attribute <code>loginURL</code>.
   **
   ** @param  value             the list of <code>loginURL</code>s.
   **                            Allowed object is {@link LogOutUrls}.
   */
  public void loginURL(final LogInUrls value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "loginURL"));

    // prevent bogus state
    if (this.loginURL != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "loginURL"));

    this.loginURL = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logoutURL
  /**
   ** Called to inject the argument for attribute <code>logoutURL</code>.
   **
   ** @param  value              the list of <code>logoutURL</code>s.
   **                            Allowed object is {@link LogOutUrls}.
   */
  public void logoutURL(final LogOutUrls value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "logoutURL"));

    // prevent bogus state
    if (this.logoutURL != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "logoutURL"));

    this.logoutURL = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationException
  /**
   ** Called to inject the argument for attribute
   ** <code>validationException</code>.
   **
   ** @param  value              the ValidationExceptions to set.
   **                            Allowed object is
   **                            {@link IpValidationExceptions}.
   */
  public void validationException(final IpValidationExceptions value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "validationException"));

    // prevent bogus state
    if (this.validationException != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "validationException"));

    this.validationException = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userProperty
  /**
   ** Called to inject the argument for attribute
   ** <code>userProperty</code>.
   **
   ** @param  value              the {@link UserDefinedParameters} to set.
   **                            Allowed object is {@link UserDefinedParameters}.
   */
  public void userProperty(final UserDefinedParameters value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "userProperty"));

    // prevent bogus state
    if (this.userDefinedParameter != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "userProperty"));

    this.userDefinedParameter = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeMapping
  /**
   ** Called to inject the argument for attribute <code>profileMapping</code>.
   **
   ** @param  value              the {@link ProfileAttributeMapping} to set.
   **                            Allowed object is
   **                            {@link ProfileAttributeMapping}.
   */
  public void attributeMapping(final ProfileAttributeMapping value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "profileMapping"));

    // prevent bogus state
    if (this.profileMapping != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "profileMapping"));

    this.profileMapping = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeMapping
  /**
   ** Called to inject the argument for attribute <code>responseMapping</code>.
   **
   ** @param  value              the {@link ResponseAttributeMapping} to set.
   **                            Allowed object is
   **                            {@link ResponseAttributeMapping}.
   */
  public void attributeMapping(final ResponseAttributeMapping value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "responseMapping"));

    // prevent bogus state
    if (this.responseMapping != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "responseMapping"));

    this.responseMapping = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeMapping
  /**
   ** Called to inject the argument for attribute <code>sessionMapping</code>.
   **
   ** @param  value              the {@link SessionAttributeMapping} to set.
   **                            Allowed object is
   **                            {@link SessionAttributeMapping}.
   */
  public void attributeMapping(final SessionAttributeMapping value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "sessionMapping"));

    // prevent bogus state
    if (this.sessionMapping != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "sessionMapping"));

    this.sessionMapping = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   miscellaneousProperty
  /**
   ** Called to inject the argument for attribute
   ** <code>miscellaneousProperty</code>.
   **
   ** @param  value              the {@link MiscellaneousProperties} to set.
   **                            Allowed object is
   **                            {@link MiscellaneousProperties}.
   */
  public void miscellaneousProperty(final MiscellaneousProperties value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "miscellaneousProperty"));

    // prevent bogus state
    if (this.miscellaneousProperty != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "miscellaneousProperty"));

    this.miscellaneousProperty = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  property           the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>property</code> to set on
   **                            this instance.
   **
   ** @throws BuildException     if the specified property id is already part of
   **                            the parameter mapping.
   */
  public void add(final AccessAgentProperty property, final String value)
    throws BuildException {

    // validate basic requirements
    if (property.required && StringUtility.isEmpty(value))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, property.id));

    // ensure inheritance and apply further validation
    super.addParameter(property.id, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    // validate strictly for create to avoid side effects
    validate(ServiceOperation.create);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @param  operation          the {@link ServiceOperation} to validate for
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#modify}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#validate}
   **                            </ul>
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation) {
    // ensure inheritance
    super.validate();

    // only create and modify commands requires specific mandatory attributes
    if (operation == ServiceOperation.delete || operation == ServiceOperation.validate || operation == ServiceOperation.print)
      return;

    // validate that at least one parameter is specified for configuration
    // purpose
    final HashMap<String, Object> parameter = this.parameter();
    if (parameter.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.TYPE_PARAMETER_EMPTY));

    // validate all required parameter if a agent is about to register
    if (operation == ServiceOperation.create) {
      // initialize instance with default values if a value is not provided by
      // the frontend for a parameter which has a default
      for (AccessAgentProperty cursor : AccessAgentProperty.values()) {
        if (cursor.required() && cursor.defaultValue() != null && !parameter.containsKey(cursor.id))
          add(cursor, cursor.defaultValue());
      }
      for (AccessAgentProperty cursor : AccessAgentProperty.values()) {
        if (cursor.required() && !parameter.containsKey(cursor.id))
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, cursor.id));
      }
    }

    // validate the syntax of typed parameters
    for (String cursor : parameter.keySet()) {
      final String              value    = stringParameter(cursor);
      final AccessAgentProperty property = AccessAgentProperty.from(cursor);
      switch (property.type) {
        case BOOLEAN : if (value != null && (!(value.equals(Boolean.TRUE.toString()) || value.equals(Boolean.FALSE.toString())))) {
                         final String[] argument = { AccessAgentProperty.ENTITY, cursor, value};
                         throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
                       }
                       break;
        case STATUS  : if (!validStatus(value)) {
                         final String[] argument = { AccessAgentProperty.ENTITY, cursor, value};
                         throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
                       }
                       break;
        case URI     : if (!validURI(value)) {
                         final String[] argument = { AccessAgentProperty.ENTITY, cursor, value};
                         throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
                       }
                       break;
        case URL     : if (!validURL(value)) {
                         final String[] argument = { AccessAgentProperty.ENTITY, cursor, value};
                         throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
                       }
                       break;
      }
    }

    // validate if the cache elment property is valid
    if (parameter.containsKey(AccessAgentProperty.CACHE_ELEMENTS_MAX.id)) {
      final String value = stringParameter(AccessAgentProperty.CACHE_ELEMENTS_MAX.id);
      if (!validInteger(value, 0)) {
        final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.CACHE_TIMEOUT.id, value};
        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
      }
    }

    // validate if the cache timeout property is valid
    if (parameter.containsKey(AccessAgentProperty.CACHE_TIMEOUT.id)) {
      final String value = stringParameter(AccessAgentProperty.CACHE_TIMEOUT.id);
      if (!validInteger(value, 0)) {
        final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.CACHE_TIMEOUT.id, value};
        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
      }
    }

    // validate if the token valid time property is valid
    if (parameter.containsKey(AccessAgentProperty.TOKEN_VALIDITY_TIME.id)) {
      final String value = stringParameter(AccessAgentProperty.TOKEN_VALIDITY_TIME.id);
      if (!validInteger(value, 0)) {
        final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.TOKEN_VALIDITY_TIME.id, value};
        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
      }
    }

    // validate if the max session time property is valid
    if (parameter.containsKey(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id)) {
      final String value = stringParameter(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id);
      if (!validInteger(value, 0)) {
        final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id, value};
        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
      }
    }

    // validate if the failover threshold property is valid
    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_FAILOVER.id)) {
      final String value = stringParameter(AccessAgentProperty.THRESHOLD_FAILOVER.id);
      if (!validInteger(value, 0)) {
        final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.THRESHOLD_FAILOVER.id, value};
        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
      }
    }

    // validate if the server timeout threshold property is valid
    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_TIMEOUT.id)) {
      final String value = stringParameter(AccessAgentProperty.THRESHOLD_TIMEOUT.id);
      if (!validInteger(value, -1)) {
        final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.THRESHOLD_TIMEOUT.id, value};
        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
      }
    }

    // validate if the sleep property is valid
    if (parameter.containsKey(AccessAgentProperty.SLEEP.id)) {
      final String value = stringParameter(AccessAgentProperty.SLEEP.id);
      if (!validInteger(value, 0)) {
        final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.SLEEP.id, value};
        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
      }
    }

    // validate if the logout target URL property is valid
    if (parameter.containsKey(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id) && stringParameter(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id).indexOf(">") > -1) {
      final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id, stringParameter(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id)};
      throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
    }

    // validate if the security mode property is valid
    if (parameter.containsKey(AccessAgentProperty.SECURITY.id)) {
      final String value = stringParameter(AccessAgentProperty.SECURITY.id);
      if (!validSecurity(value)) {
        final String[] argument = { AccessAgentProperty.ENTITY, AccessAgentProperty.SECURITY.id, value};
        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
      }
    }

    // validate if the logout URL property is valid
    if (this.logoutURL != null) {
      final List<String> collection = this.logoutURL.getUrl();
      for (String cursor : collection) {
        if (cursor.isEmpty()) {
          final String[] argument = { AccessAgentProperty.ENTITY, "url", cursor};
          throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
        }
        if (!validURI(cursor)) {
          final String[] argument = { AccessAgentProperty.ENTITY, "url", cursor};
          throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
        }
      }
    }

    // validate if the IP security exception property is valid
    if (this.validationException != null) {
      final List<String> collection = this.validationException.getIpAddress();
      for (String cursor : collection) {
        if (cursor.isEmpty()) {
          final String[] argument = { AccessAgentProperty.ENTITY, "address", cursor};
          throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
        }
        if (!validAddress(cursor)) {
          final String[] argument = { AccessAgentProperty.ENTITY, "address", cursor};
          throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_VALUE_INVALID, argument));
        }
      }
    }

    // validate parameters that might by passed to the operation but isn't
    // allowed to changed
//    if (operation == ServiceOperation.modify) {
//      if (parameter.containsKey(AccessAgentProperty.HOST_IDENTIFIER.id))
//        throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_UNMODIFIABLE, AccessAgentProperty.ENTITY, AccessAgentProperty.HOST_IDENTIFIER.id));
//    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Factory method to create the request parameter to create an entity
   ** instance.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data for create.
   **                            Possible object is {@link BaseRequest}.
   */
  protected final BaseRequest createRequest() {
    switch (this.version) {
      case AGENT10    : return agent10gCreate();
      case AGENT11    : return agent11gCreate();
      case LEGACYSUN  : return openSSOCreate();
      case LEGACYORCL : return orclSSOCreate();
      default         : return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResponse
  /**
   ** This method unmarshalles an XML string to give a parsed object.
   **
   ** @param  xml                the XML fragment to parse.
   **
   ** @return                    the root of content tree being unmarshalled.
   **
   ** @throws FeatureException   if an error was encountered while creating the
   **                            {@link Unmarshaller} object.
   */
  protected final BaseResponse createResponse(final String xml)
    throws FeatureException {

    switch (this.version) {
      case AGENT10    : return agent10gResponse(xml);
      case AGENT11    : return agent11gResponse(xml);
//      case LEGACYSUN  : return openSSOCreate(xml);
//      case LEGACYORCL : return orclSSOCreate(xml);
      default         : return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResponse
  /**
   ** This method spools.
   **
   ** @param  stream             the output channel.
   ** @param  response           the response to spool out.
   */
  protected final void createReport(final PrintStream stream, final BaseResponse response) {
    switch (this.version) {
      case AGENT10    : agent10gPrint(stream, response);
                        break;
      default         : agent11gPrint(stream, response);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   ** Factory method to create the request parameter to modify an entity
   ** instance.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data for modify.
   **                            Possible object is {@link BaseRequest}.
   */
  protected final BaseRequest modifyRequest() {
    switch (this.version) {
      case AGENT10    : return agent10gModify();
      case AGENT11    : return agent11gModify();
      case LEGACYSUN  : return openSSOModify();
      case LEGACYORCL : return orclSSOModify();
      default         : return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   baseRequest
  /**
   ** Factory method to create an {@link BaseRequest} request with
   ** operational mode <code>agentDelete</code> or <code>agentValidate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#DELETE
   ** @see    AccessAgentProperty.Mode#VALIDATE
   */
  protected final BaseRequest baseRequest() {
    return factory.createBaseRequest(this.mode, this.serviceURL, this.username, this.password, name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent10gReport
  /**
   ** Factory method to create an {@link BaseRequest} request with
   ** operational mode <code>agentinfo</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#REPORT
   ** @see    AccessAgentProperty.Mode#VALIDATE
   */
  protected final BaseRequest agent10gReport() {
    return factory.createBaseRequest(BaseRequest.TYPE_AGENT10, this.mode, this.serviceURL, this.username, this.password, name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent11gReport
  /**
   ** Factory method to create an {@link BaseRequest} request with
   ** operational mode <code>agentinfo</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#REPORT
   ** @see    AccessAgentProperty.Mode#VALIDATE
   */
  protected final BaseRequest agent11gReport() {
    return factory.createAgent11gRequest(this.mode, this.serviceURL, this.username, this.password, name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent10gValidation
  /**
   ** Factory method to create an {@link BaseRequest} validation request with
   ** operational mode <code>validateagent</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#REPORT
   ** @see    AccessAgentProperty.Mode#VALIDATE
   */
  protected final BaseRequest agent10gValidation() {
    return factory.createBaseRequest(BaseRequest.TYPE_AGENT10, this.mode, this.serviceURL, this.username, this.password, name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent11gValidation
  /**
   ** Factory method to create an {@link BaseRequest} validation request with
   ** operational mode <code>validateagent</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#REPORT
   ** @see    AccessAgentProperty.Mode#VALIDATE
   */
  protected final BaseRequest agent11gValidation() {
    return factory.createBaseRequest(BaseRequest.TYPE_AGENT11, this.mode, this.serviceURL, this.username, this.password, name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent10gCreate
  /**
   ** Factory method to create an {@link Agent10gUpdate} request with
   ** operational mode <code>agentCreate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#CREATE
   */
  private final BaseRequest agent10gCreate() {
    final Agent10gCreate request = factory.createAgent10gCreate(this.mode, this.serviceURL, this.username, this.password, name());
    final Map<String, Object> parameter = parameter();
    if (parameter.containsKey(AccessAgentProperty.HOST_IDENTIFIER.id))
      request.setHostIdentifier(stringParameter(AccessAgentProperty.HOST_IDENTIFIER.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_BASE_URL.id))
      request.setAgentBaseUrl(stringParameter(AccessAgentProperty.AGENT_BASE_URL.id));

    if (parameter.containsKey(AccessAgentProperty.VIRTUAL_HOST.id))
      request.setVirtualHost(booleanParameter(AccessAgentProperty.VIRTUAL_HOST.id));

    if (parameter.containsKey(AccessAgentProperty.APPLICATION_DOMAIN.id))
      request.setApplicationDomain(stringParameter(AccessAgentProperty.APPLICATION_DOMAIN.id));

    if (parameter.containsKey(AccessAgentProperty.AUTO_CREATE_POLICY.id))
      request.setAutoCreatePolicy(booleanParameter(AccessAgentProperty.AUTO_CREATE_POLICY.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_PASSWORD.id))
      request.setAccessClientPasswd(stringParameter(AccessAgentProperty.AGENT_PASSWORD.id));

    if (parameter.containsKey(AccessAgentProperty.COOKIE_DOMAIN_PRIMARY.id))
      request.setPrimaryCookieDomain(stringParameter(AccessAgentProperty.COOKIE_DOMAIN_PRIMARY.id));

    if (parameter.containsKey(AccessAgentProperty.PREFERRED_HOST.id))
      request.setPreferredHost(stringParameter(AccessAgentProperty.PREFERRED_HOST.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_ELEMENTS_MAX.id))
      request.setMaxCacheElement(integerParameter(AccessAgentProperty.CACHE_ELEMENTS_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_TIMEOUT.id))
      request.setCacheTimeout(integerParameter(AccessAgentProperty.CACHE_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.COOKIE_SESSION_TIME.id))
      request.setCookieSessionTime(integerParameter(AccessAgentProperty.COOKIE_SESSION_TIME.id));

    if (parameter.containsKey(AccessAgentProperty.CONNECTION_MAX.id))
      request.setMaxConnections(integerParameter(AccessAgentProperty.CONNECTION_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id))
      request.setMaxSessionTime(integerParameter(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.SESSION_IDLE_TIMEOUT.id))
      request.setIdleSessionTimeout(integerParameter(AccessAgentProperty.SESSION_IDLE_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_FAILOVER.id))
      request.setFailoverThreshold(integerParameter(AccessAgentProperty.THRESHOLD_FAILOVER.id));

    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_TIMEOUT.id))
      request.setAaaTimeoutThreshold(integerParameter(AccessAgentProperty.THRESHOLD_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.SLEEP.id))
      request.setSleepFor(integerParameter(AccessAgentProperty.SLEEP.id));

    if (parameter.containsKey(AccessAgentProperty.DEBUG.id))
      request.setDebug(booleanParameter(AccessAgentProperty.DEBUG.id));

    if (parameter.containsKey(AccessAgentProperty.SECURITY.id))
      request.setSecurity(stringParameter(AccessAgentProperty.SECURITY.id));

    if (parameter.containsKey(AccessAgentProperty.DENY_NOT_PROTECTED.id))
      request.setDenyOnNotProtected(booleanParameter(AccessAgentProperty.DENY_NOT_PROTECTED.id) ? 1 : 0);

    if (parameter.containsKey(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id))
      request.setAllowManagementOperations(booleanParameter(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_PRAGMA_HEADER.id))
      request.setCachePragmaHeader(stringParameter(AccessAgentProperty.CACHE_PRAGMA_HEADER.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_CONTROL_HEADER.id))
      request.setCacheControlHeader(stringParameter(AccessAgentProperty.CACHE_CONTROL_HEADER.id));

    if (parameter.containsKey(AccessAgentProperty.IP_VALIDATION.id))
      request.setIpValidation(booleanParameter(AccessAgentProperty.IP_VALIDATION.id) ? 1 : 0);

    if (parameter.containsKey(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id))
      request.setLogoutTargetUrlParamName(stringParameter(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id));

    if (parameter.containsKey(AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id))
      request.setProtectedAuthnScheme(stringParameter(AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id));

    if (parameter.containsKey(AccessAgentProperty.AUTHENTICATION_SCHEME_PUBLIC.id))
      request.setPublicAuthnScheme(stringParameter(AccessAgentProperty.AUTHENTICATION_SCHEME_PUBLIC.id));

    if (parameter.containsKey(AccessAgentProperty.FUSION_APPLICATION.id))
      request.setIsFusionAppRegistration(booleanParameter(AccessAgentProperty.FUSION_APPLICATION.id));

    if (this.variation != null)
      request.setHostPortVariationsList(this.variation);

    if (this.validationException != null)
      request.setIpValidationExceptions(this.validationException);

    if (this.logoutURL != null)
      request.setLogOutUrls(this.logoutURL);

    if (this.primaryServer != null)
      request.setPrimaryServer(this.primaryServer);

    if (this.secondaryServer != null)
      request.setSecondaryServer(this.secondaryServer);

    if (this.protectedResource != null)
      request.setProtectedResource(this.protectedResource);

    if (this.publicResource != null)
      request.setPublicResource(this.publicResource);

    if (this.excludedResource != null)
      request.setExcludedResource(this.excludedResource);

    if (this.userDefinedParameter != null)
      request.setUserDefinedParameters(this.userDefinedParameter);

    if (this.applicationDomain != null)
      request.setRregApplicationDomain(this.applicationDomain);

    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent10gModify
  /**
   ** Factory method to create an {@link Agent10gUpdate} request with
   ** operational mode <code>agentUpdate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#MODIFY
   */
  private final BaseRequest agent10gModify() {
    final Agent10gUpdate request = factory.createAgent10gUpdate(this.mode, this.serviceURL, this.username, this.password, name());
    final Map<String, Object> parameter = parameter();
    if (parameter.containsKey(AccessAgentProperty.PREFERRED_HOST.id))
      request.setPreferredHost(stringParameter(AccessAgentProperty.PREFERRED_HOST.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_PASSWORD.id)) {
      request.setAccessClientPasswd(stringParameter(AccessAgentProperty.AGENT_PASSWORD.id));
      request.setModifyAccessClientPasswdFlag(Boolean.TRUE);
    }
    else {
      request.setAccessClientPasswd(SystemConstant.EMPTY);
      request.setModifyAccessClientPasswdFlag(Boolean.FALSE);
    }

    if (parameter.containsKey(AccessAgentProperty.COOKIE_DOMAIN_PRIMARY.id))
      request.setPrimaryCookieDomain(stringParameter(AccessAgentProperty.COOKIE_DOMAIN_PRIMARY.id));

    if (parameter.containsKey(AccessAgentProperty.SECURITY.id))
      request.setSecurity(stringParameter(AccessAgentProperty.SECURITY.id));

    if (parameter.containsKey(AccessAgentProperty.STATE.id))
      request.setState(stringParameter(AccessAgentProperty.STATE.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_ELEMENTS_MAX.id))
      request.setMaxCacheElement(integerParameter(AccessAgentProperty.CACHE_ELEMENTS_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_TIMEOUT.id))
      request.setCacheTimeout(integerParameter(AccessAgentProperty.CACHE_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.CONNECTION_MAX.id))
      request.setMaxConnections(integerParameter(AccessAgentProperty.CONNECTION_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id))
      request.setMaxSessionTime(integerParameter(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_FAILOVER.id))
      request.setFailoverThreshold(integerParameter(AccessAgentProperty.THRESHOLD_FAILOVER.id));

    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_TIMEOUT.id))
      request.setAaaTimeoutThreshold(integerParameter(AccessAgentProperty.THRESHOLD_TIMEOUT.id));

    if (this.logoutURL != null)
      request.setLogOutUrls(this.logoutURL);

    if (this.userDefinedParameter != null)
      request.setUserDefinedParameters(this.userDefinedParameter);

    if (parameter.containsKey(AccessAgentProperty.SLEEP.id))
      request.setSleepFor(integerParameter(AccessAgentProperty.SLEEP.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_PRAGMA_HEADER.id))
      request.setCachePragmaHeader(stringParameter(AccessAgentProperty.CACHE_PRAGMA_HEADER.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_CONTROL_HEADER.id))
      request.setCacheControlHeader(stringParameter(AccessAgentProperty.CACHE_CONTROL_HEADER.id));

    if (parameter.containsKey(AccessAgentProperty.DEBUG.id))
      request.setDebug(booleanParameter(AccessAgentProperty.DEBUG.id));

    if (parameter.containsKey(AccessAgentProperty.IP_VALIDATION.id))
      request.setIpValidation(booleanParameter(AccessAgentProperty.IP_VALIDATION.id) ? 1 : 0);

    if (this.validationException != null)
      request.setIpValidationExceptions(this.validationException);

    if (parameter.containsKey(AccessAgentProperty.DENY_NOT_PROTECTED.id))
      request.setDenyOnNotProtected(booleanParameter(AccessAgentProperty.DENY_NOT_PROTECTED.id) ? 1 : 0);

    if (parameter.containsKey(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id))
      request.setAllowManagementOperations(booleanParameter(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id));

    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent10gResponse
  /**
   ** This method unmarshalles an XML string to give a parsed object.
   **
   ** @param  xml                the XML fragment to parse.
   **
   ** @return                    the root of content tree being unmarshalled.
   **
   ** @throws FeatureException   if an error was encountered while creating the
   **                            {@link Unmarshaller} object.
   */
  private final Agent10gResponse agent10gResponse(final String xml)
    throws FeatureException {

    final Unmarshaller unmarshaller = RegistrationHandler.createUnmarshaller();
    Agent10gResponse response = null;
    try {
      response = (Agent10gResponse)unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
    }
    catch (JAXBException e) {
      e.printStackTrace();
    }
    return response;
  }

  private final void agent10gPrint(final PrintStream stream, final BaseResponse response) {
    final Agent10gResponse r = (Agent10gResponse)response;
    final TableFormatter   f = new TableFormatter();
    f.header(FeatureResourceBundle.string(FeatureMessage.AGENT_PROPERTY)).header(FeatureResourceBundle.string(FeatureMessage.AGENT_VALUE));
    f.row().column(AccessAgentProperty.DESCRIPTION.id).column(r.getDescription());
    f.row().column(AccessAgentProperty.PREFERRED_HOST.id).column(r.getPreferredHost());
    f.row().column(AccessAgentProperty.IP_VALIDATION.id).column(r.getIpValidation());

    stream.println(r.getAgentName());
    f.print(new TableFormatter.Tabbed(stream));
  }

  private final void agent11gPrint(final PrintStream stream, final BaseResponse response) {
    final Agent11gResponse r = (Agent11gResponse)response;
    final TableFormatter   f = new TableFormatter();
    f.header(FeatureResourceBundle.string(FeatureMessage.AGENT_PROPERTY)).header(FeatureResourceBundle.string(FeatureMessage.AGENT_VALUE));
    f.row().column(AccessAgentProperty.STATE.id).column(r.getState());

    f.row().column(AccessAgentProperty.SECURITY.id).column(r.getSecurity() == null ? "open" : r.getSecurity());
    f.row().column(AccessAgentProperty.CONNECTION_MAX.id).column(r.getMaxConnections());
    f.row().column(AccessAgentProperty.CACHE_ELEMENTS_MAX.id).column(r.getMaxCacheElems());
    f.row().column(AccessAgentProperty.CACHE_TIMEOUT.id).column(r.getCacheTimeout());
    f.row().column(AccessAgentProperty.TOKEN_VALIDITY_TIME.id).column(r.getTokenValidityPeriod());
    f.row().column(AccessAgentProperty.THRESHOLD_FAILOVER.id).column(r.getFailoverThreshold());
    f.row().column(AccessAgentProperty.PREFERRED_HOST.id).column(r.getPreferredHost());
    f.row().column(AccessAgentProperty.LOGOUT_CALLBACK_URL.id).column(r.getLogoutCallbackUrl());
    f.row().column(AccessAgentProperty.LOGOUT_REDIRECT_URL.id).column(r.getLogoutRedirectUrl());

    f.row().column(AccessAgentProperty.DENY_NOT_PROTECTED.id).column(r.getDenyOnNotProtected());
    f.row().column(AccessAgentProperty.SLEEP.id).column(r.getSleepFor());
    f.row().column(AccessAgentProperty.CACHE_PRAGMA_HEADER.id).column(r.getCachePragmaHeader());
    f.row().column(AccessAgentProperty.CACHE_CONTROL_HEADER.id).column(r.getCacheControlHeader());
    f.row().column(AccessAgentProperty.DEBUG.id).column(r.getDebug());
    f.row().column(AccessAgentProperty.IP_VALIDATION.id).column(r.getIpValidation() == 0 ? "false" : "true");

    f.row().column(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id).column(r.getAllowManagementOperations());
    f.row().column(AccessAgentProperty.ALLOW_COLLECTOR_OPERATION.id).column(r.getAllowCredentialCollectorOperations() == null ? "false" : r.getAllowCredentialCollectorOperations());
    f.row().column(AccessAgentProperty.ALLOW_MASTER_TOKEN_RETRIEVAL.id).column(r.getAllowMasterTokenRetrieval() == null ? "false" : r.getAllowMasterTokenRetrieval());
    f.row().column(AccessAgentProperty.ALLOW_TOKEN_SCOPE_OPERATION.id).column(r.getAllowTokenScopeOperations() == null ? "false" : r.getAllowTokenScopeOperations());

    if (r.getPrimaryServer() != null) {
      final List<Server> c = r.getPrimaryServer().getServer();
      for (int i = 0; i < c.size(); i++) {
        final Server s = c.get(i);
        f.row().column((i == 0) ? "primaryServerList" : "").column(String.format("%s:%d#%d", s.getServerHost(), s.getServerPort(), s.getConnectionMax()));
      }
    }
    if (r.getSecondaryServer() != null) {
      final List<Server> c = r.getSecondaryServer().getServer();
      for (int i = 0; i < c.size(); i++) {
        final Server s = c.get(i);
        f.row().column((i == 0) ? "secondaryServerList" : "").column(String.format("%s:%d#%d", s.getServerHost(), s.getServerPort(), s.getConnectionMax()));
      }
    }

    if (r.getUserDefinedParameters() != null) {
      final List<UserDefinedParam> c = r.getUserDefinedParameters().getUserDefinedParam();
      for (int i = 0; i < c.size(); i++) {
        final UserDefinedParam p = c.get(i);
        f.row().column((i == 0) ? "userDefinedParameters" : "").column(String.format("%s: %s", p.getName(), p.getValue()));
      }
    }
    stream.println(r.getAgentName());
    f.print();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent11gCreate
  /**
   ** Factory method to create an {@link Agent11gUpdate} request with
   ** operational mode <code>agentCreate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#CREATE
   */
  private final BaseRequest agent11gCreate() {
    final Agent11gCreate request = factory.createAgent11gCreate(this.mode, this.serviceURL, this.username, this.password, name());
    final Map<String, Object> parameter = parameter();
    if (parameter.containsKey(AccessAgentProperty.HOST_IDENTIFIER.id))
      request.setHostIdentifier(stringParameter(AccessAgentProperty.HOST_IDENTIFIER.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_BASE_URL.id))
      request.setAgentBaseUrl(stringParameter(AccessAgentProperty.AGENT_BASE_URL.id));

    if (parameter.containsKey(AccessAgentProperty.VIRTUAL_HOST.id))
      request.setVirtualHost(booleanParameter(AccessAgentProperty.VIRTUAL_HOST.id));

    if (parameter.containsKey(AccessAgentProperty.APPLICATION_DOMAIN.id))
      request.setApplicationDomain(stringParameter(AccessAgentProperty.APPLICATION_DOMAIN.id));

    if (parameter.containsKey(AccessAgentProperty.AUTO_CREATE_POLICY.id))
      request.setAutoCreatePolicy(booleanParameter(AccessAgentProperty.AUTO_CREATE_POLICY.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_PASSWORD.id))
      request.setAccessClientPasswd(stringParameter(AccessAgentProperty.AGENT_PASSWORD.id));

    if (parameter.containsKey(AccessAgentProperty.PREFERRED_HOST.id))
      request.setPreferredHost(stringParameter(AccessAgentProperty.PREFERRED_HOST.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_ELEMENTS_MAX.id))
      request.setMaxCacheElement(integerParameter(AccessAgentProperty.CACHE_ELEMENTS_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_TIMEOUT.id))
      request.setCacheTimeout(integerParameter(AccessAgentProperty.CACHE_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.TOKEN_VALIDITY_TIME.id))
      request.setTokenValidityPeriod(integerParameter(AccessAgentProperty.TOKEN_VALIDITY_TIME.id));

    if (parameter.containsKey(AccessAgentProperty.CONNECTION_MAX.id))
      request.setMaxConnections(integerParameter(AccessAgentProperty.CONNECTION_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id))
      request.setMaxSessionTime(integerParameter(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_FAILOVER.id))
      request.setFailoverThreshold(integerParameter(AccessAgentProperty.THRESHOLD_FAILOVER.id));

    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_TIMEOUT.id))
      request.setAaaTimeoutThreshold(integerParameter(AccessAgentProperty.THRESHOLD_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.SLEEP.id))
      request.setSleepFor(integerParameter(AccessAgentProperty.SLEEP.id));

    if (parameter.containsKey(AccessAgentProperty.DEBUG.id))
      request.setDebug(booleanParameter(AccessAgentProperty.DEBUG.id));

    if (parameter.containsKey(AccessAgentProperty.SECURITY.id))
      request.setSecurity(stringParameter(AccessAgentProperty.SECURITY.id));

    if (parameter.containsKey(AccessAgentProperty.DENY_NOT_PROTECTED.id))
      request.setDenyOnNotProtected(booleanParameter(AccessAgentProperty.DENY_NOT_PROTECTED.id) ? 1 : 0);

    if (parameter.containsKey(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id))
      request.setAllowManagementOperations(booleanParameter(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id));

    if (parameter.containsKey(AccessAgentProperty.ALLOW_MASTER_TOKEN_RETRIEVAL.id))
      request.setAllowMasterTokenRetrieval(booleanParameter(AccessAgentProperty.ALLOW_MASTER_TOKEN_RETRIEVAL.id));

    if (parameter.containsKey(AccessAgentProperty.ALLOW_TOKEN_SCOPE_OPERATION.id))
      request.setAllowTokenScopeOperations(booleanParameter(AccessAgentProperty.ALLOW_TOKEN_SCOPE_OPERATION.id));

    if (parameter.containsKey(AccessAgentProperty.ALLOW_COLLECTOR_OPERATION.id))
      request.setAllowCredentialCollectorOperations(booleanParameter(AccessAgentProperty.ALLOW_COLLECTOR_OPERATION.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_PRAGMA_HEADER.id))
      request.setCachePragmaHeader(stringParameter(AccessAgentProperty.CACHE_PRAGMA_HEADER.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_CONTROL_HEADER.id))
      request.setCacheControlHeader(stringParameter(AccessAgentProperty.CACHE_CONTROL_HEADER.id));

    if (parameter.containsKey(AccessAgentProperty.IP_VALIDATION.id))
      request.setIpValidation(booleanParameter(AccessAgentProperty.IP_VALIDATION.id) ? 1 : 0);

    if (parameter.containsKey(AccessAgentProperty.LOGOUT_CALLBACK_URL.id))
      request.setLogoutCallbackUrl(stringParameter(AccessAgentProperty.LOGOUT_CALLBACK_URL.id));

    if (parameter.containsKey(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id))
      request.setLogoutTargetUrlParamName(stringParameter(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id));

    if (parameter.containsKey(AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id))
      request.setProtectedAuthnScheme(stringParameter(AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id));

    if (parameter.containsKey(AccessAgentProperty.AUTHENTICATION_SCHEME_PUBLIC.id))
      request.setPublicAuthnScheme(stringParameter(AccessAgentProperty.AUTHENTICATION_SCHEME_PUBLIC.id));

    if (parameter.containsKey(AccessAgentProperty.FUSION_APPLICATION.id))
      request.setIsFusionAppRegistration(booleanParameter(AccessAgentProperty.FUSION_APPLICATION.id));

    if (this.variation != null)
      request.setHostPortVariationsList(this.variation);

    if (this.validationException != null)
      request.setIpValidationExceptions(this.validationException);

    if (this.logoutURL != null)
      request.setLogOutUrls(this.logoutURL);

    if (this.primaryServer != null)
      request.setPrimaryServer(this.primaryServer);

    if (this.secondaryServer != null)
      request.setSecondaryServer(this.secondaryServer);

    if (this.protectedResource != null)
      request.setProtectedResource(this.protectedResource);

    if (this.publicResource != null)
      request.setPublicResource(this.publicResource);

    if (this.excludedResource != null)
      request.setExcludedResource(this.excludedResource);

    if (this.userDefinedParameter != null)
      request.setUserDefinedParameters(this.userDefinedParameter);

    if (this.applicationDomain != null)
      request.setRregApplicationDomain(this.applicationDomain);

    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent11gModify
  /**
   ** Factory method to create an {@link Agent11gUpdate} request with
   ** operational mode <code>agentUpdate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#MODIFY
   */
  private final BaseRequest agent11gModify() {
    final Agent11gUpdate request = factory.createAgent11gUpdate(this.mode, this.serviceURL, this.username, this.password, name());
    final Map<String, Object> parameter = parameter();
    if (parameter.containsKey(AccessAgentProperty.PREFERRED_HOST.id))
      request.setPreferredHost(stringParameter(AccessAgentProperty.PREFERRED_HOST.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_PASSWORD.id)) {
      request.setAccessClientPasswd(stringParameter(AccessAgentProperty.AGENT_PASSWORD.id));
      request.setModifyAccessClientPasswdFlag(Boolean.TRUE);
    }
    else {
      request.setAccessClientPasswd(SystemConstant.EMPTY);
      request.setModifyAccessClientPasswdFlag(Boolean.FALSE);
    }
    if (parameter.containsKey(AccessAgentProperty.SECURITY.id))
      request.setSecurity(stringParameter(AccessAgentProperty.SECURITY.id));

    if (parameter.containsKey(AccessAgentProperty.STATE.id))
      request.setState(stringParameter(AccessAgentProperty.STATE.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_ELEMENTS_MAX.id))
      request.setMaxCacheElement(integerParameter(AccessAgentProperty.CACHE_ELEMENTS_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_TIMEOUT.id))
      request.setCacheTimeout(integerParameter(AccessAgentProperty.CACHE_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.TOKEN_VALIDITY_TIME.id))
      request.setTokenValidityPeriod(integerParameter(AccessAgentProperty.TOKEN_VALIDITY_TIME.id));

    if (parameter.containsKey(AccessAgentProperty.CONNECTION_MAX.id))
      request.setMaxConnections(integerParameter(AccessAgentProperty.CONNECTION_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id))
      request.setMaxSessionTime(integerParameter(AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_FAILOVER.id))
      request.setFailoverThreshold(integerParameter(AccessAgentProperty.THRESHOLD_FAILOVER.id));

    if (parameter.containsKey(AccessAgentProperty.THRESHOLD_TIMEOUT.id))
      request.setAaaTimeoutThreshold(integerParameter(AccessAgentProperty.THRESHOLD_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.LOGOUT_CALLBACK_URL.id))
      request.setLogoutCallbackUrl(stringParameter(AccessAgentProperty.LOGOUT_CALLBACK_URL.id));

    if (parameter.containsKey(AccessAgentProperty.LOGOUT_REDIRECT_URL.id))
      request.setLogoutRedirectUrl(stringParameter(AccessAgentProperty.LOGOUT_REDIRECT_URL.id));

    if (parameter.containsKey(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id))
      request.setLogoutTargetUrlParamName(stringParameter(AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id));

    if (parameter.containsKey(AccessAgentProperty.SLEEP.id))
      request.setSleepFor(integerParameter(AccessAgentProperty.SLEEP.id));

    if (parameter.containsKey(AccessAgentProperty.DEBUG.id))
      request.setDebug(booleanParameter(AccessAgentProperty.DEBUG.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_PRAGMA_HEADER.id))
      request.setCachePragmaHeader(stringParameter(AccessAgentProperty.CACHE_PRAGMA_HEADER.id));

    if (parameter.containsKey(AccessAgentProperty.CACHE_CONTROL_HEADER.id))
      request.setCacheControlHeader(stringParameter(AccessAgentProperty.CACHE_CONTROL_HEADER.id));

    if (parameter.containsKey(AccessAgentProperty.IP_VALIDATION.id))
      request.setIpValidation(booleanParameter(AccessAgentProperty.IP_VALIDATION.id) ? 1 : 0);

    if (parameter.containsKey(AccessAgentProperty.DENY_NOT_PROTECTED.id))
      request.setDenyOnNotProtected(booleanParameter(AccessAgentProperty.DENY_NOT_PROTECTED.id) ? 1 : 0);

    if (parameter.containsKey(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id))
      request.setAllowManagementOperations(booleanParameter(AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id));

    if (parameter.containsKey(AccessAgentProperty.ALLOW_MASTER_TOKEN_RETRIEVAL.id))
      request.setAllowMasterTokenRetrieval(booleanParameter(AccessAgentProperty.ALLOW_MASTER_TOKEN_RETRIEVAL.id));

    if (parameter.containsKey(AccessAgentProperty.ALLOW_TOKEN_SCOPE_OPERATION.id))
      request.setAllowTokenScopeOperations(booleanParameter(AccessAgentProperty.ALLOW_TOKEN_SCOPE_OPERATION.id));

    if (parameter.containsKey(AccessAgentProperty.ALLOW_COLLECTOR_OPERATION.id))
      request.setAllowCredentialCollectorOperations(booleanParameter(AccessAgentProperty.ALLOW_COLLECTOR_OPERATION.id));

    if (this.primaryServer != null)
      request.setPrimaryServer(this.primaryServer);

    if (this.secondaryServer != null)
      request.setSecondaryServer(this.secondaryServer);

    if (this.logoutURL != null)
      request.setLogOutUrls(this.logoutURL);

    if (this.userDefinedParameter != null)
      request.setUserDefinedParameters(this.userDefinedParameter);

    if (this.validationException != null)
      request.setIpValidationExceptions(this.validationException);

    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agent11gResponse
  /**
   ** This method unmarshalles an XML string to give a parsed object.
   **
   ** @param  xml                the XML fragment to parse.
   **
   ** @return                    the root of content tree being unmarshalled.
   **
   ** @throws FeatureException   if an error was encountered while creating the
   **                            {@link Unmarshaller} object.
   */
  private final Agent11gResponse agent11gResponse(final String xml)
    throws FeatureException {

    final Unmarshaller unmarshaller = RegistrationHandler.createUnmarshaller();
    Agent11gResponse response = null;
    try {
      response = (Agent11gResponse)unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
    }
    catch (JAXBException e) {
      e.printStackTrace();
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   openSSOCreate
  /**
   ** Factory method to create an {@link OpenSSOCreate} request with
   ** operational mode <code>agentCreate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#CREATE
   */
  private final BaseRequest openSSOCreate() {
    final OpenSSOCreate request = factory.createOpenSSOCreate(this.mode, this.serviceURL, this.username, this.password, name());
    final Map<String, Object> parameter = parameter();
    if (parameter.containsKey(AccessAgentProperty.HOST_IDENTIFIER.id))
      request.setHostIdentifier(stringParameter(AccessAgentProperty.HOST_IDENTIFIER.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_BASE_URL.id))
      request.setAgentBaseUrl(stringParameter(AccessAgentProperty.AGENT_BASE_URL.id));

    if (parameter.containsKey(AccessAgentProperty.APPLICATION_DOMAIN.id))
      request.setApplicationDomain(stringParameter(AccessAgentProperty.APPLICATION_DOMAIN.id));

    if (parameter.containsKey(AccessAgentProperty.AUTO_CREATE_POLICY.id))
      request.setAutoCreatePolicy(booleanParameter(AccessAgentProperty.AUTO_CREATE_POLICY.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_PASSWORD.id))
      request.setAgentPassword(stringParameter(AccessAgentProperty.AGENT_PASSWORD.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_TYPE.id))
      request.setAgentType(stringParameter(AccessAgentProperty.OPENSSO_TYPE.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_VERSION.id))
      request.setAgentVersion(stringParameter(AccessAgentProperty.OPENSSO_VERSION.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_DEBUG_DIRECTORY.id))
      request.setAgentDebugDir(stringParameter(AccessAgentProperty.OPENSSO_DEBUG_DIRECTORY.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_AUDIT_DIRECTORY.id))
      request.setAgentAuditDir(stringParameter(AccessAgentProperty.OPENSSO_AUDIT_DIRECTORY.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_AUDIT_FILENAME.id))
      request.setAgentAuditFileName(stringParameter(AccessAgentProperty.OPENSSO_AUDIT_FILENAME.id));

    if (parameter.containsKey(AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id))
      request.setProtectedAuthnScheme(stringParameter(AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id));

    if (this.protectedResource != null)
      request.setProtectedResource(this.protectedResource);

    if (this.publicResource != null)
      request.setPublicResource(this.publicResource);

    if (this.excludedResource != null)
      request.setExcludedResource(this.excludedResource);

    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   openSSOModify
  /**
   ** Factory method to create an {@link OpenSSOUpdate} request with
   ** operational mode <code>agentCreate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#CREATE
   */
  private final BaseRequest openSSOModify() {
    final OpenSSOUpdate request = factory.createOpenSSOUpdate(this.mode, this.serviceURL, this.username, this.password, name());
    final Map<String, Object> parameter = parameter();
    if (parameter.containsKey(AccessAgentProperty.HOST_IDENTIFIER.id))
      request.setHostIdentifier(stringParameter(AccessAgentProperty.HOST_IDENTIFIER.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_BASE_URL.id))
      request.setAgentBaseUrl(stringParameter(AccessAgentProperty.AGENT_BASE_URL.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_PASSWORD.id)) {
      request.setAgentPassword(stringParameter(AccessAgentProperty.AGENT_PASSWORD.id));
      request.setModifyAccessClientPasswdFlag(Boolean.TRUE);
    }

    if (parameter.containsKey(AccessAgentProperty.STATE.id))
      request.setState(stringParameter(AccessAgentProperty.STATE.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_TYPE.id))
      request.setAgentType(stringParameter(AccessAgentProperty.OPENSSO_TYPE.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_ORGANIZATION_NAME.id))
      request.setOrganizationName(stringParameter(AccessAgentProperty.OPENSSO_ORGANIZATION_NAME.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_FILTER_MODE.id))
      request.setFilterMode(stringParameter(AccessAgentProperty.OPENSSO_FILTER_MODE.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_SESSION_MAX.id))
      request.setMaxSession(stringParameter(AccessAgentProperty.OPENSSO_SESSION_MAX.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_SESSION_TIMEOUT.id))
      request.setSessionTimeout(stringParameter(AccessAgentProperty.OPENSSO_SESSION_TIMEOUT.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_DEBUG_DIRECTORY.id))
      request.setAgentDebugDir(stringParameter(AccessAgentProperty.OPENSSO_DEBUG_DIRECTORY.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_DEBUG_FILENAME.id))
      request.setAgentDebugFileName(stringParameter(AccessAgentProperty.OPENSSO_DEBUG_FILENAME.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_AUDIT_DIRECTORY.id))
      request.setAgentAuditDir(stringParameter(AccessAgentProperty.OPENSSO_AUDIT_DIRECTORY.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_AUDIT_FILENAME.id))
      request.setAgentAuditFileName(stringParameter(AccessAgentProperty.OPENSSO_AUDIT_FILENAME.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_COOKIE_NAME.id))
      request.setCookieName(stringParameter(AccessAgentProperty.OPENSSO_COOKIE_NAME.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_COOKIE_SEPARATOR.id))
      request.setCookieSeperator(stringParameter(AccessAgentProperty.OPENSSO_COOKIE_SEPARATOR.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_COOKIE_ENCODING.id))
      request.setCookieEncode(booleanParameter(AccessAgentProperty.OPENSSO_COOKIE_ENCODING.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_SSO_ONLY.id))
      request.setSsoOnly(booleanParameter(AccessAgentProperty.OPENSSO_SSO_ONLY.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_ACCESS_DENIED.id))
      request.setAccessDeniedUrl(stringParameter(AccessAgentProperty.OPENSSO_ACCESS_DENIED.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_DEBUG_LEVEL.id))
      request.setDebugLevel(stringParameter(AccessAgentProperty.OPENSSO_DEBUG_LEVEL.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_USERID_PARAM_TYPE.id))
      request.setUserIdParamType(stringParameter(AccessAgentProperty.OPENSSO_USERID_PARAM_TYPE.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_USERID_PARAM.id))
      request.setUserIdParam(stringParameter(AccessAgentProperty.OPENSSO_USERID_PARAM.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_USER_MAPPING_MODE.id))
      request.setUserMappingMode(stringParameter(AccessAgentProperty.OPENSSO_USER_MAPPING_MODE.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_USER_ATTRIBUTE_NAME.id))
      request.setUserAttributeName(stringParameter(AccessAgentProperty.OPENSSO_USER_ATTRIBUTE_NAME.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_USER_PRINCIPAL.id))
      request.setUserPrincipal(stringParameter(AccessAgentProperty.OPENSSO_USER_PRINCIPAL.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_USER_TOKEN.id))
      request.setUserToken(stringParameter(AccessAgentProperty.OPENSSO_USER_TOKEN.id));

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_FETCHMODE_PROFILE.id))
      request.setProfileAttributeFetchMode(stringParameter(AccessAgentProperty.OPENSSO_FETCHMODE_PROFILE.id));

    if (this.loginURL != null)
      request.setLogInUrls(this.loginURL);

    if (this.logoutURL != null)
      request.setLogOutUrls(this.logoutURL);

    if (this.notEnforcedResource != null)
      request.setNotEnforcedUrls(this.notEnforcedResource);

    if (this.profileMapping != null)
      request.setProfileAttributeMapping(this.profileMapping);

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_FETCHMODE_SESSION.id))
      request.setSessionAttributeFetchMode(stringParameter(AccessAgentProperty.OPENSSO_FETCHMODE_SESSION.id));

    if (this.sessionMapping != null)
      request.setSessionAttributeMapping(this.sessionMapping);

    if (parameter.containsKey(AccessAgentProperty.OPENSSO_FETCHMODE_RESPONSE.id))
      request.setResponseAttributeFetchMode(stringParameter(AccessAgentProperty.OPENSSO_FETCHMODE_RESPONSE.id));

    if (this.responseMapping != null)
      request.setResponseAttributeMapping(this.responseMapping);

    if (this.miscellaneousProperty != null)
      request.setMiscellaneousProperties(this.miscellaneousProperty);

    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclSSOCreate
  /**
   ** Factory method to create an {@link OrclSSOCreate} request with
   ** operational mode <code>agentCreate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#CREATE
   */
  private final BaseRequest orclSSOCreate() {
    final OrclSSOCreate request = factory.createOrclSSOCreate(this.mode, this.serviceURL, this.username, this.password, name());
    final Map<String, Object> parameter = parameter();
    if (parameter.containsKey(AccessAgentProperty.HOST_IDENTIFIER.id))
      request.setHostIdentifier(stringParameter(AccessAgentProperty.HOST_IDENTIFIER.id));

    if (parameter.containsKey(AccessAgentProperty.AGENT_BASE_URL.id))
      request.setAgentBaseUrl(stringParameter(AccessAgentProperty.AGENT_BASE_URL.id));

    if (parameter.containsKey(AccessAgentProperty.APPLICATION_DOMAIN.id))
      request.setApplicationDomain(stringParameter(AccessAgentProperty.APPLICATION_DOMAIN.id));

    if (parameter.containsKey(AccessAgentProperty.AUTO_CREATE_POLICY.id))
      request.setAutoCreatePolicy(booleanParameter(AccessAgentProperty.AUTO_CREATE_POLICY.id));

    if (parameter.containsKey(AccessAgentProperty.TOKEN_VERSION.id))
      request.setSsoServerVersion(stringParameter(AccessAgentProperty.TOKEN_VERSION.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_ORACLE_HOME_PATH.id))
      request.setOracleHomePath(stringParameter(AccessAgentProperty.ORCLSSO_ORACLE_HOME_PATH.id));

    if (parameter.containsKey(AccessAgentProperty.VIRTUAL_HOST.id))
      request.setVirtualHost(stringParameter(AccessAgentProperty.VIRTUAL_HOST.id));

    if (parameter.containsKey(AccessAgentProperty.UPDATE_MODE.id))
      request.setUpdateMode(stringParameter(AccessAgentProperty.UPDATE_MODE.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_ADMIN_INFO.id))
      request.setAdminInfo(stringParameter(AccessAgentProperty.ORCLSSO_ADMIN_INFO.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_ADMIN_ID.id))
      request.setAdminId(stringParameter(AccessAgentProperty.ORCLSSO_ADMIN_ID.id));

    if (parameter.containsKey(AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id))
      request.setProtectedAuthnScheme(stringParameter(AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id));

    if (parameter.containsKey(AccessAgentProperty.AUTHENTICATION_SCHEME_PUBLIC.id))
      request.setPublicAuthnScheme(stringParameter(AccessAgentProperty.AUTHENTICATION_SCHEME_PUBLIC.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_LOGOUT_URL.id))
      request.setLogoutUrl(stringParameter(AccessAgentProperty.ORCLSSO_LOGOUT_URL.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_FAILURE_URL.id))
      request.setFailureUrl(stringParameter(AccessAgentProperty.ORCLSSO_FAILURE_URL.id));

    if (this.protectedResource != null)
      request.setProtectedResource(this.protectedResource);

    if (this.publicResource != null)
      request.setPublicResource(this.publicResource);

    if (this.applicationDomain != null)
      request.setRregApplicationDomain(this.applicationDomain);

    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclSSOModify
  /**
   ** Factory method to create an {@link OrclSSOUpdate} request with
   ** operational mode <code>agentCreate</code>.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessAgentProperty.Mode
   ** @see    AccessAgentProperty.Mode#CREATE
   */
  private final BaseRequest orclSSOModify() {
    final OrclSSOUpdate request = factory.createOrclSSOUpdate(this.mode, this.serviceURL, this.username, this.password, name());
    final Map<String, Object> parameter = parameter();
    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_ADMIN_INFO.id))
      request.setAdminInfo(stringParameter(AccessAgentProperty.ORCLSSO_ADMIN_INFO.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_ADMIN_ID.id))
      request.setAdminId(stringParameter(AccessAgentProperty.ORCLSSO_ADMIN_ID.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_HOME_URL.id))
      request.setHomeUrl(stringParameter(AccessAgentProperty.ORCLSSO_HOME_URL.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_SUCCESS_URL.id))
      request.setSuccessUrl(stringParameter(AccessAgentProperty.ORCLSSO_SUCCESS_URL.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_LOGOUT_URL.id))
      request.setLogoutUrl(stringParameter(AccessAgentProperty.ORCLSSO_LOGOUT_URL.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_FAILURE_URL.id))
      request.setFailureUrl(stringParameter(AccessAgentProperty.ORCLSSO_FAILURE_URL.id));

    if (parameter.containsKey(AccessAgentProperty.ORCLSSO_START_DATE.id))
      request.setStartDate(stringParameter(AccessAgentProperty.ORCLSSO_START_DATE.id));

    return request;
  }
}