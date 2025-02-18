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

    File        :   AccountProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-04-06  SBernet    First release version
*/

package oracle.iam.identity.gws.service.provisioning.keycloak;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import oracle.hst.foundation.utility.StringUtility;
import oracle.iam.identity.connector.service.AttributeFactory;
import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.foundation.resource.TaskBundle;
import org.identityconnectors.framework.api.operations.ScriptOnConnectorApiOp;
import org.identityconnectors.framework.api.operations.UpdateApiOp;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.Uid;
import org.xml.sax.InputSource;

import org.identityconnectors.framework.common.objects.ObjectClass;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Provisioning;

import oracle.iam.identity.connector.integration.TargetFeature;
import oracle.iam.identity.connector.integration.TargetResource;

import oracle.iam.identity.gws.integration.keycloak.ServiceFeature;
import oracle.iam.identity.gws.integration.keycloak.ServiceResource;

////////////////////////////////////////////////////////////////////////////////
// class AccountProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountProvisioning</code> acts as the service end point for
 ** Identity Manager to provision account properties to a Service Provider.
 ** <br>
 ** This wrapper class has methods for account operations like create account,
 ** modify account, delete account etc.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccountProvisioning extends Provisioning {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String LOGGER_CATEGORY = "OCS.RKC.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountProvisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   **                            <br>
   **                            Allowed object is {@link tcDataProvider}.
   ** @param  processInstance    the Process Instance Key providing the data for
   **                            the provisioning tasks.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  processTask        the Process Task Key providing the data for
   **                            the provisioning tasks.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public AccountProvisioning(final tcDataProvider provider, final Long processInstance, final Integer processTask) {
    // ensure inheritance
    super(provider, processInstance, processTask, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateResource  (Provisioning)
  /**
   ** Initalize the IT Resource capabilities.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    a {@link TargetResource} populated and
   **                            validated.
   **                            <br>
   **                            Possible object {@link TargetResource}.
   **
   ** @throws TaskException      if the initialization of the
   **                            <code>IT Resource</code> fails.
   */
  @Override
  protected TargetResource populateResource(final Long resourceInstance)
    throws TaskException {

    final String method  = "populateResource";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      return ServiceResource.build(this, resourceInstance);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateFeature (Reconciliation)
  /**
   ** Factory method to create and populate the <code>Metadata Descriptor</code>
   ** form the <code>Metadata Service</code>.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    an instance of {@link TargetFeature} populated
   **                            form the <code>Metadata Service</code>.
   **                            <br>
   **                            Possible object is {@link TargetFeature}.
   **
   ** @throws TaskException      in case marshalling the
   **                            <code>Metadata Descriptor</code> fails.
   */
  @Override
  protected final TargetFeature populateFeature(final InputSource source)
    throws TaskException {

    return ServiceFeature.build(this, source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create an <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String create(final Long resourceInstance) {
    return super.createEntry(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Renames an <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  origin             the value of the unique name attribute before
   **                            the event happend.
   **                            <br>
   **                            This is used to evaluate the correct identifier
   **                            passed as the UID to the connector.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  attributeFilter    the name of the attribute to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String rename(final Long resourceInstance, final String origin, final String attributeFilter) {
    this.origin = origin;
    return modify(resourceInstance, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Account</code> entirely in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  attribute          the name(s) and the values of the attribute(s)
   **                            to modify.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element consist as a {@link String} for the key
   **                            and an {@link Object} as its value.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String modify(final Long resourceInstance, final Map<String, Object> attribute) {
    return super.modifyAttribute(ObjectClass.ACCOUNT, resourceInstance, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies one or more attribute values of an <code>Account</code> in the
   ** Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  attributeFilter    the name of the attribute to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String modify(final Long resourceInstance, final String attributeFilter) {
    return super.modifyAttribute(ObjectClass.ACCOUNT, resourceInstance, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activate
  /**
   ** Activates an deactivated <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String activate(final Long resourceInstance) {
    return super.activateEntry(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deactivate
  /**
   ** Deactivates an activated <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String deactivate(final Long resourceInstance) {
    return super.deactivateEntry(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete an <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String delete(final Long resourceInstance) {
    return super.deleteEntry(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Assign an <code>Account</code> to an entity in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String assign(final Long resourceInstance) {
    return assignPermission(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revoke an <code>Account</code> from an entity in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String revoke(final Long resourceInstance) {
    return revokePermission(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Update an <code>Account</code> from an entity in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String update(final Long resourceInstance) {
    return super.modifyPermission(ObjectClass.ACCOUNT, resourceInstance);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyEntry
  /**
   ** Modifies an entry of specified type in the <code>Service Provider</code>.
   ** Overrides the param <code>filter</code> to ensure all the fields values
   ** are included in the modifications, which is necessary because the Keycloak API
   ** cannot patch attributes one-by-one.
   ** In case of an attribute is excluded from the modification and its value is not set
   ** properly, the value will be set as null or will be deleted by the API.
   ** @param  type               the entry type to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  dataSet            the attribute values for the entry type to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   ** @param  filter             the name(s) of the attribute(s) to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched anywhere.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String modifyEntry(ObjectClass type, Long endpoint, Set<Attribute> dataSet, Set<String> filter) {
    this.data.put(Uid.NAME, this.data.get(this.descriptor.identifier()));
    return super.modifyEntry(type, endpoint, dataSet, filter);
  }
}
