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
    Subsystem   :   Oracle Identity Manager Connector

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Provisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed Defect DE-000128
                                         Batch Size is an optional argument but
                                         if its isn't defined the job loops
                                         infinite.
                                         Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;

import oracle.iam.platform.OIMClient;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.foundation.reconciliation.Descriptor;
import oracle.iam.identity.foundation.rmi.IdentityServerResource;

import oracle.iam.identity.gis.service.ManagedServer;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> acts as the service end point for the Oracle
 ** Identity Manager to reconcile entities from Identity Manager itself.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
abstract class Reconciliation extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String     LOGGER_CATEGORY  = "OCS.GIS.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the connection to the target system */
  protected IdentityServerResource resource;

  /** the abstraction layer to communicate with the target system */
  protected ManagedServer          server          = null;
  protected OIMClient              targetService   = null;

  ////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  protected Reconciliation() {
    // ensure inheritance
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> with the specified logging
   ** category.
   ** <p>
   ** Unfortunately we tried to initailize the system watch here with the
   ** appropriate name of the Job but the name of the job is not know at the
   ** time the constructor is ivoked. It looks uggly in the logs if the is an
   ** object instance of the system watch that doen't know its name. For that
   ** reason we moved the initialization of the system watch to
   ** <code>initialize</code>.
   **
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  protected Reconciliation(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Directory Service.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    String[] parameter = { reconcileObject(), getName(), resourceName() };
    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, parameter));
    try {
      populateChanges(batchSize(), returningAttributes());
    }
    // in any case of an unhandled exception
    catch (TaskException e) {
      warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, parameter));
      throw e;
    }
    info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateChanges
  /**
   ** Do all action which should take place for reconciliation by fetching the
   ** data from the target system.
   **
   ** @param  bulkSize           the size of a block processed in a thread
   ** @param  returning          the attributes whose values have to be returned.
   **                            Set it to <code>null</code>, if all attribute
   **                            values have to be returned
   **
   ** @throws TaskException      if the operation fails
   */
  protected abstract void populateChanges(final int bulkSize, final Set<String> returning)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes
  /**
   ** Returns the {@link Set} of attribute names that will be passed to a Target
   ** System search operation to specify which attributes the Service Provider
   ** should return for an account.
   **
   ** @return                   the array of attribute names that will be
   **                           passed to a Target System search operation to
   **                           specify which attributes the Service Provider
   **                           should return.
   */
  protected abstract Set<String> returningAttributes();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected abstract void processSubject(Map<String, Object> subject)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureDescriptor
  /**
   ** Configure the descriptor capabilities.
   **
   ** @throws TaskException      if the operation fails for any reason.
   */
  protected void configureDescriptor()
    throws TaskException {

    final String method = "configureDescriptor";
    final String path   = stringValue(RECONCILE_DESCRIPTOR);
    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(reconcileDescriptor()));

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      this.descriptor = new Descriptor(this);

      DescriptorFactory.configure(this.descriptor, document);
      // produce the logging output only if the logging level is enabled for
     if (this.logger != null && this.logger.debugLevel())
        debug(method,this.descriptor.toString());
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
  }

  protected List<ApplicationInstance> findApplicationInstance(String appInstance){
    final SearchCriteria scFilter =  new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.APPINST_NAME.getId(), appInstance, SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> configParams = new HashMap<String, Object>();
    configParams.put("FORMDETAILS", "EXCLUDE");
    try {
      return((service(ApplicationInstanceService.class)).findApplicationInstance(scFilter, configParams));
    } catch (GenericAppInstanceServiceException e) {
      return null;
    }
  }
}