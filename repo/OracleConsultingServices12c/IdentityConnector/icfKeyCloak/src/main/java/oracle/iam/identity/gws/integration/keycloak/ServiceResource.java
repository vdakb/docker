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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   RedHat KeyCloak Connector

    File        :   ServiceResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-04-06  SBernet    First release version
*/

package oracle.iam.identity.gws.integration.keycloak;

import java.util.Map;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.ITResourceAttribute;

import oracle.iam.identity.connector.integration.TargetResource;

import static oracle.iam.identity.gws.integration.keycloak.Service.OIM.Resource.TYPE_ACCEPT;
import static oracle.iam.identity.gws.integration.keycloak.Service.OIM.Resource.TYPE_CONTENT;
import static oracle.iam.identity.gws.integration.keycloak.Service.OIM.Resource.CLIENT_SECRET;
import static oracle.iam.identity.gws.integration.keycloak.Service.OIM.Resource.CLIENT_IDENTIFIER;
import static oracle.iam.identity.gws.integration.keycloak.Service.OIM.Resource.RESOURCE_OWNER;
import static oracle.iam.identity.gws.integration.keycloak.Service.OIM.Resource.RESOURCE_CREDENTIAL;
import static oracle.iam.identity.gws.integration.keycloak.Service.OIM.Resource.AUTHORIZATION_SERVER;
import static oracle.iam.identity.gws.integration.keycloak.Service.OIM.Resource.AUTHENTICATION_SCHEME;

////////////////////////////////////////////////////////////////////////////////
// class ServiceResource
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>ServiceResource</code> implements the base functionality of a
 ** Service <code>IT Resource</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceResource extends TargetResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the array with the attributes for the <code>IT Resource</code> */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(SERVER_NAME,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PORT,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,         ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(ROOT_CONTEXT,          ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(TYPE_CONTENT,          ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(TYPE_ACCEPT,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(AUTHENTICATION_SCHEME, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(AUTHORIZATION_SERVER,  ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CLIENT_IDENTIFIER,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CLIENT_SECRET,         ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(PRINCIPAL_NAME,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(PRINCIPAL_PASSWORD,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_CREDENTIAL,   ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_OWNER,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_CREDENTIAL,   ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_OWNER,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_CREDENTIAL,   ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,       ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_COUNTRY,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,       ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(SERVER_FEATURE,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(CONNECTION_TIMEOUT,    CONNECTION_TIMEOUT_DEFAULT)
  , ITResourceAttribute.build(RESPONSE_TIMEOUT,      RESPONSE_TIMEOUT_DEFAULT)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance key.
   ** <br>
   ** The <code>IT Resource</code> will not be populated from the repository of
   ** Identity Manager. Only instance key and instance name are obtained.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries.
   */
  private ServiceResource(final Loggable loggable, final Long instance)
          throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of
   ** Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the public identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries or one or
   **                            more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private ServiceResource(final Loggable loggable, final String instance)
          throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} for logging purpose.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private ServiceResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractResource)
  /**
   ** Returns the array with names which should be populated from the
   ** IT Resource definition of Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   **                            <br>
   **                            Possible object is array of
   **                            {@link AbstractAttribute} where each.
   */
  @Override
  public AbstractAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registry (TargetResource)
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
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   */
  @Override
  public final Map<String, String> registry() {
    return Service.RESOURCE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given identifier.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> key.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries for the
   **                            given key or one or more attributes are missing
   **                            on the <code>IT Resource</code> instance.
   */
  public static ServiceResource build(final Loggable loggable, final Long instance)
          throws TaskException {

    return new ServiceResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given name.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> name.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the name of the IT Resource instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  public static ServiceResource build(final Loggable loggable, final String instance)
          throws TaskException {

    return new ServiceResource(loggable, instance);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given identifier.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> key.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries for the
   **                            given key or one or more attributes are missing
   **                            on the <code>IT Resource</code> instance.
   */
  public static ServiceResource build(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    return new ServiceResource(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return StringUtility.formatCollection(this);
  }
}