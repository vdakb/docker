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
    Subsystem   :   Identity Governance Connector

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.jes.service.reconciliation.oig;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;

import org.xml.sax.InputSource;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.connector.integration.TargetFeature;
import oracle.iam.identity.connector.integration.TargetResource;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.EntityReconciliation;

import oracle.iam.identity.jes.integration.oig.ServiceFeature;
import oracle.iam.identity.jes.integration.oig.ServiceResource;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** The <code>Service</code> acts as the service end point for the Oracle
 ** Identity Manager to reconcile account information from a
 ** <code>openfire™ Database</code> Service Provider.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Service extends EntityReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The category of the logging facility to use. */
  private static final String CATEGORY  = "OCS.OIG.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Service</code> job that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Service() {
    // ensure inheritance
    super(CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateResource  (Reconciliation)
  /**
   ** Initalize the IT Resource capabilities.
   ** <p>
   ** The approach implementing this method should be:
   ** <ol>
   **   <li>If only  Application Name is configured as job parameter and
   **       IT Resource or Reconciliation Object is not configured, then
   **       IT Resource and Reconciliation Object will be fetched mapped with
   **       the given application Name
   **   <li>If IT Resource and Reconciliation Object are configured as job
   **       parameter then they gets priority even application name is also
   **       configured as job parameter
   **   <li>getParameter method logic handle the precedence logic of for cases
   **       mentioned in point #1 &amp; 2
   ** </ol>
   **
   ** @return                    a {@link TargetResource} populated and
   **                            validated.
   **
   ** @throws TaskException      if the initialization of the
   **                            <code>IT Resource</code> fails.
   */
  @Override
  protected TargetResource populateResource()
    throws TaskException {

    final String method  = "populateResource";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    TargetResource resource = null;
    try {
      // 1. If only  Application Name is configured as job parameter and
      //    IT Resource or Reconciliation Object is not configured, then
      //    IT Resource and Reconciliation Object will be fetched mapped with
      //    the given application Name
      // 2. If IT Resource and Reconciliation Object are configured as job
      //    parameter then they gets priority even application name is also
      //    configured as job parameter
      // 3. getParameter method logic handle the precedence logic of for cases
      //    mentioned in point #1 & 2
      if(!StringUtility.isBlank(resourceName())){
        resource = ServiceResource.build(this, resourceName());
      }
      else {
        final ApplicationInstance instance = populateInstance(applicationInstance());
        resource = ServiceResource.build(this, instance.getItResourceKey());
      }
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return resource;
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
  // Method groupd by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** The call back method just invoked before reconciliation takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException    in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    final String method  = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // iterate over the reconciliation field and check for multi-valued
    // attributes
    String columnName = null;
    String fieldName  = null;
    // get the reconciliation field definition from the Resource Object
    // handled by this task
    Map filter = new HashMap(1);
    filter.put(ResourceObject.NAME, reconcileObject());
    try {
      tcResultSet resultSet = null;
      // retrieve the reconciliation field definition of the Resource Object
      // that this task will reconciling
      try {
        resultSet = this.objectFacade().findObjects(filter);
        if (resultSet.getRowCount() == 0)
          throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
        if (resultSet.getRowCount() > 1)
          throw new TaskException(TaskError.RESOURCE_AMBIGUOUS, reconcileObject());

        resultSet = this.objectFacade().getReconciliationFields(resultSet.getLongValue(ResourceObject.KEY));
        // if no fields defined abort the execution of this task by throwing an
        // appropriate exception
        if (resultSet.getRowCount() == 0)
          throw new TaskException(TaskError.RESOURCE_RECONFIELD, reconcileObject());
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, ResourceObject.KEY);
      }
      catch (tcObjectNotFoundException e) {
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
      }

      final Set<String> reference = this.descriptor.referenceTarget();
      try {
        for (int i = 0; i < resultSet.getRowCount(); i++) {
          resultSet.goToRow(i);
          columnName  = ResourceObject.RECON_FIELD_TYPE;
          if (Descriptor.MULTI_VALUE.equals(resultSet.getStringValue(columnName))) {
            columnName = ResourceObject.RECON_FIELD_NAME;
            fieldName  = resultSet.getStringValue(columnName);
             if (!(reference.contains(fieldName))) {
              String[] arguments = { reconcileObject(), fieldName };
              warning(TaskBundle.format(TaskError.RESOURCE_RECON_MULTIVALUE, arguments));
            }
          }
        }
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, columnName);
      }
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}