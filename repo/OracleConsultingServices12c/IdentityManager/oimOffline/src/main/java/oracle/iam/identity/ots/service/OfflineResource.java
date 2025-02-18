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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   OfflineResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OfflineResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service;

import java.util.Map;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.ITResourceAttribute;

////////////////////////////////////////////////////////////////////////////////
// class OfflineResource
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>OfflineResource</code> implements the base functionality of a
 ** Generic Offline IT Resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class OfflineResource extends AbstractResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the name of
   ** the SOA Composite that the provisioning process invokes and this IT
   ** Resource belongs to.
   */
  public static final String COMPOSITE_NAME    = "Composite Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the URL of
   ** the SOA Composite that the provisioning process invokes and this IT
   ** Resource belongs to.
   */
  public static final String COMPOSITE_URL     = "Composite URL";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the service
   ** name of the SOA Composite that the provisioning process invokes and this
   ** IT Resource belongs to.
   */
  public static final String COMPOSITE_SERVICE = "Composite Service Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the name of
   ** the Connector Server that the provisioning process may invoke and this
   ** IT Resource belongs to.
   */
  public static final String CONNECTOR_SERVER  = "Connector Server Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the name of
   ** the Identity Gateway that the provisioning process may invoke and this
   ** IT Resource belongs to.
   */
  public static final String IDENTITY_GATEWAY  = "Identity Gateway Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the name of
   ** Metadata Descriptor which is specifying the additional features of the
   ** IT Resource.
   */
  public static final String SERVER_FEATURE    = "Configuration Lookup";

  /** the array with the attributes for the IT Resource */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(COMPOSITE_NAME,    ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(COMPOSITE_URL,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(COMPOSITE_SERVICE, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CONNECTOR_SERVER,  ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(IDENTITY_GATEWAY,  ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(SERVER_FEATURE,    ITResourceAttribute.OPTIONAL)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OfflineResource</code> which is associated
   ** with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            IT Resource instance where this wrapper belongs
   **                            to.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public OfflineResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OfflineResource</code> which is associated with the
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
  public OfflineResource(final Loggable loggable, final Long instanceKey)
    throws TaskException {

    super(loggable, instanceKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OfflineResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   ** @param  instanceName       the name of the IT Resource instance where this
   **                            wrapper belongs to.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries or
   **                            one or more attributes are missing on the
   **                            <code>IT Resource</code> Definition.
   */
  public OfflineResource(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OfflineResource</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The IT Resource will be populated from the specified parameters.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   ** @param  compositeName      the name of the SOA Composite that the
   **                            provisioning process invokes and this IT
   **                            Resource belongs to.
   ** @param  compositeURL       the URL of the SOA Composite that the
   **                            provisioning process invokes and this IT
   **                            Resource belongs to.
   ** @param  compositeService   the service name of the SOA Composite that the
   **                            provisioning process invokes and this IT
   **                            Resource belongs to.
   ** @param  connectorServer    the name of the Connector Server that the
   **                            provisioning process may invoke and this
   **                            IT Resource belongs to.
   ** @param  identityGateway    the name of the Identity Gateway that the
   **                            provisioning process may invoke and this
   **                            IT Resource belongs to.
   ** @param  serverFeature      the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public OfflineResource(final Loggable loggable, final String compositeName, final String compositeURL, final String compositeService, final String connectorServer, final String identityGateway, final String serverFeature)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(COMPOSITE_NAME,    compositeName);
    attribute(COMPOSITE_URL,     compositeURL);
    attribute(COMPOSITE_SERVICE, compositeService);
    attribute(CONNECTOR_SERVER,  connectorServer);
    attribute(IDENTITY_GATEWAY,  identityGateway);
    attribute(SERVER_FEATURE,    serverFeature);

    validateAttributes(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compositeName
  /**
   ** Returns the name of the SOA Composite that the provisioning process
   ** invokes and this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #COMPOSITE_NAME}.
   **
   ** @return                    the name of the SOA Composite that the
   **                            provisioning process invokes and this IT
   **                            Resource belongs to.
   */
  public final String compositeName() {
    return stringValue(COMPOSITE_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compositeURL
  /**
   ** Returns the URL of the SOA Composite that the provisioning process invokes
   ** and this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #COMPOSITE_URL}.
   **
   ** @return                    the URL of the SOA Composite that the
   **                            provisioning process invokes and this IT
   **                            Resource belongs to.
   */
  public final String compositeURL() {
    return stringValue(COMPOSITE_URL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compositeService
  /**
   ** Returns the service name of the SOA Composite that the provisioning
   ** process invokes and this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #COMPOSITE_SERVICE}.
   **
   ** @return                    the service name of the SOA Composite that the
   **                            provisioning process invokes and this IT
   **                            Resource belongs to.
   */
  public final String compositeService() {
    return stringValue(COMPOSITE_SERVICE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorServer (overridden)
  /**
   ** Returns the name of the Connector Server that the provisioning process may
   ** invoke and this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #CONNECTOR_SERVER}.
   **
   ** @return                    the name of the Connector Server that the
   **                            provisioning process may invoke and this
   **                            IT Resource belongs to.
   */
  public final String connectorServer() {
    return stringValue(CONNECTOR_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityGateway
  /**
   ** Returns the name of the Identity Gateway that the provisioning process may
   ** invoke and this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #IDENTITY_GATEWAY}.
   **
   ** @return                    the name of the Identity Gateway that the
   **                            provisioning process may invoke and this
   **                            IT Resource belongs to.
   */
  public final String identityGateway() {
    return stringValue(IDENTITY_GATEWAY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverFeature
  /**
   ** Returns the name of the Metadata Descriptor providing the target system
   ** specific features like objectClasses, attribute id's etc.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_FEATURE}.
   **
   ** @return                    the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   */
  public final String serverFeature() {
    return stringValue(SERVER_FEATURE);
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