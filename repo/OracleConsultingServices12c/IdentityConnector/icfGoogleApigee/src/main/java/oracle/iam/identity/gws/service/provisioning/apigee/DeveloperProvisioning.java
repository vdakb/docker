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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   DeveloperProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DeveloperProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.gws.service.provisioning.apigee;

import java.util.Map;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import org.identityconnectors.framework.api.operations.APIOperation;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.connector.service.DescriptorTransformer;

////////////////////////////////////////////////////////////////////////////////
// class DeveloperProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DeveloperProvisioning</code> acts as the service end point for
 ** Identity Manager to provision developer account properties to a Service
 ** Provider.
 ** <br>
 ** This wrapper class has methods for developer account operations like create
 ** developer, modify developer, delete developer etc.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DeveloperProvisioning extends Service {
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the object class for a organization entitlement */
  protected static final ObjectClass TENANT         = DescriptorTransformer.objectClass("TENANT");

  /** the object class for an developer acount */
  protected static final ObjectClass DEVELOPER      = DescriptorTransformer.objectClass("DEVELOPER");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String tenant;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DeveloperProvisioning</code> task adapter.
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
  public DeveloperProvisioning(final tcDataProvider provider, final Long processInstance, final Integer processTask) {
    // ensure inheritance
    super(provider, processInstance, processTask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create a <code>Developer</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String create(final Long resourceInstance, final String tenant) {
    // prevent bogus input
    if (StringUtility.isEmpty(tenant))
      return updateTask(TaskException.abort(TaskBundle.format(TaskError.ARGUMENT_IS_NULLOREMPTY, "tenant")));

    // latch the tenant locally it will be picked up later in building the
    // operation options
    this.tenant = tenant;
    
    // ensure inheritance
    return super.createEntry(DEVELOPER, resourceInstance);
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
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
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
  public String rename(final Long resourceInstance, final String tenant, final String origin, final String attributeFilter) {
    // latch the original username locally it will be picked up later in
    // building the operation options
    this.origin = origin;
    return modify(resourceInstance, tenant, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify (overridden)
  /**
   ** Modifies a <code>Developer</code> entirely in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
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
  public final String modify(final Long resourceInstance, final String tenant, final Map<String, Object> attribute) {
    // prevent bogus input
    if (StringUtility.isEmpty(tenant))
      return updateTask(TaskException.abort(TaskBundle.format(TaskError.ARGUMENT_IS_NULLOREMPTY, "tenant")));

    // latch the tenant locally it will be picked up later in building the
    // operation options
    this.tenant = tenant;
    
    // this hack is required due to Apigee is expecting the complete set of
    // attributes on update; unfortunately the bulk update in Idetity Manager
    // populates the given map with the update attributes only hence we need to
    // merge the missing values
    this.data.entrySet().stream().filter(
      (entry) -> (attribute.get(entry.getKey()) == null)).forEachOrdered((entry) -> {
        attribute.put(entry.getKey(), entry.getValue());
      }
    );
    return super.modifyAttribute(DEVELOPER, resourceInstance, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies one or more attribute values of a <code>Developer</code> in the
   ** Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attributeFilter    the name of the attribute to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String modify(final Long resourceInstance, final String tenant, final String attributeFilter) {
    // prevent bogus input
    if (StringUtility.isEmpty(tenant))
      return updateTask(TaskException.abort(TaskBundle.format(TaskError.ARGUMENT_IS_NULLOREMPTY, "tenant")));

    // latch the tenant locally it will be picked up later in building the
    // operation options
    this.tenant = tenant;
    
    // ensure inheritance
    return super.modifyAttribute(DEVELOPER, resourceInstance, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activate (overridden)
  /**
   ** Activates a deactivated <code>Developer</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String activate(final Long resourceInstance, final String tenant) {
    // prevent bogus input
    if (StringUtility.isEmpty(tenant))
      return updateTask(TaskException.abort(TaskBundle.format(TaskError.ARGUMENT_IS_NULLOREMPTY, "tenant")));

    // latch the tenant locally it will be picked up later in building the
    // operation options
    this.tenant = tenant;
    
    // ensure inheritance
    return super.activateEntry(DEVELOPER, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deactivate (overridden)
  /**
   ** Deactivates a activated <code>Developer</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String deactivate(final Long resourceInstance, final String tenant) {
    // prevent bogus input
    if (StringUtility.isEmpty(tenant))
      return updateTask(TaskException.abort(TaskBundle.format(TaskError.ARGUMENT_IS_NULLOREMPTY, "tenant")));

    // latch the tenant locally it will be picked up later in building the
    // operation options
    this.tenant = tenant;
    
    // ensure inheritance
    return super.deactivateEntry(DEVELOPER, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete a <code>Developer</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String delete(final Long resourceInstance, final String tenant) {
    // prevent bogus input
    if (StringUtility.isEmpty(tenant))
      return updateTask(TaskException.abort(TaskBundle.format(TaskError.ARGUMENT_IS_NULLOREMPTY, "tenant")));

    // latch the tenant locally it will be picked up later in building the
    // operation options
    this.tenant = tenant;
    
    return super.deleteEntry(DEVELOPER, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign (overridden)
  /**
   ** Assign a <code>Developer</code> to an entity in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String assign(final Long resourceInstance, final String tenant) {
    // prevent bogus input
    if (StringUtility.isEmpty(tenant))
      return updateTask(TaskException.abort(TaskBundle.format(TaskError.ARGUMENT_IS_NULLOREMPTY, "tenant")));

    // latch the tenant locally it will be picked up later in building the
    // operation options
    this.tenant = tenant;
    
    return assignPermission(DEVELOPER, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke (overridden)
  /**
   ** Revoke a <code>Developer</code> from an entity in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  tenant             the tenant the developer account belongs to
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String revoke(final Long resourceInstance, final String tenant) {
    // prevent bogus input
    if (StringUtility.isEmpty(tenant))
      return updateTask(TaskException.abort(TaskBundle.format(TaskError.ARGUMENT_IS_NULLOREMPTY, "tenant")));

    // latch the tenant locally it will be picked up later in building the
    // operation options
    this.tenant = tenant;
    
    return revokePermission(DEVELOPER, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationBuilder (overridden)
  /**
   ** Callback method to create {@link OperationOptionsBuilder} for provided
   ** operation.
   ** <p>
   ** Form values are available in instance variable <code>data</code> that is
   ** populated before this method will be invoked.
   ** <p>
   ** The defautl implementation creates an empty
   ** {@link OperationOptionsBuilder} only. Subclasses should override the
   ** method if their is a need to customize the {@link OperationOptions} that
   ** will be passed to the connector.
   **
   ** @param  operation          the {@link APIOperation} for which the
   **                            operation options are provided, if
   **                            <code>null</code> all operation options
   **                            specified in Operation Options lookup will be
   **                            provided.
   **
   ** @return                    the appropriate {@link OperationOptionsBuilder}
   **                            instance.
   **                            Never be <code>null</code>.
   **                            <br>
   **                            Possible object
   **                            {@link OperationOptionsBuilder}.
   **
   ** @throws TaskException      if the {@link OperationOptionsBuilder} could
   **                            not be created properly; e.g. missing required
   **                            data.
   */
  @Override
  protected OperationOptionsBuilder operationBuilder(final Class<? extends APIOperation> operation)
    throws TaskException {

    // regardless what kind of operation is build the developer account requires
    // a container
    return super.operationBuilder(operation).setContainer(new QualifiedUid(TENANT, new Uid(tenant)));
  }
}