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
    Subsystem   :   Oracle Database Account Connector

    File        :   Reconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Reconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.service.reconciliation;

import java.util.Set;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseResource;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.dbs.persistence.Administration;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> acts as the service end point for the Oracle
 ** Identity Manager to reconcile tablespace, role and profile lookup
 ** information from an Oracle Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class Reconciliation extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String     LOGGER_CATEGORY = "OCS.DBS.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Administration connection;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> task adpater that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Reconciliation() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    initializeConnector();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** The call back method just invoked before reconciliation takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    // ensure inheritance
    super.beforeExecution();

    this.connection.connect();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterExecution (overridden)
  /**
   ** The call back method just invoked after reconciliation finished.
   ** <br>
   ** Close all resources requested before reconciliation takes place.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void afterExecution()
    throws TaskException {

    this.connection.disconnect();

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateConsistency
  protected void validateConsistency(final String option, final String target)
    throws ReconciliationException {

    final String method = "validateConsistency";
    if (booleanValue(option) && StringUtility.isEmpty(stringValue(target))) {
      final String[] argument = {option, target };
      ReconciliationException e = new ReconciliationException(ReconciliationError.ATTRIBUTE_INCONSISTENT, argument);
      error(method, e.getLocalizedMessage());
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeConnector
  /**
   ** Initalize the connection capabilities.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected void initializeConnector()
    throws TaskException {

    final String method = "initializeConnector";
    this.connection = new Administration(this, new DatabaseResource(this, this.resourceName()));
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel())
      debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.connection.toString()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureDescriptor
  /**
   ** Configure the descriptor capabilities.
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in  Oracle Identity Manager metadata
   **                            entries or one or more attributes are missing
   **                            on the <code>Metadata Descriptor</code>.
   */
  protected void configureDescriptor()
    throws TaskException {

    final String method = "configureDescriptor";
    final String path   = stringValue(RECONCILE_DESCRIPTOR);
    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session  = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(reconcileDescriptor()));

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      this.descriptor = new Descriptor(this);
      DescriptorFactory.configure(this.descriptor, document);
      // produce the logging output only if the logging level is enabled for
      if (this.logger != null && this.logger.debugLevel())
        debug(method, this.descriptor.toString());
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateControlList
  /**
   ** Do all action which should take place for reconciliation for an particular
   ** subject.
   **
   ** @param  lookup             the name of the Lookup Definition to populate
   **                            from Oracle Identity manager repository.
   **
   ** @return                    the {@link Set} of encoded values obtained from
   **                            Lookup Definition.
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in  Oracle Identity Manager metadata
   **                            entries or one or more attributes are missing
   **                            on the <code>Metadata Descriptor</code>.
   */
  protected Set<String> populateControlList(final String lookup)
    throws TaskException {

    final AbstractLookup control = new AbstractLookup(this, lookup);
    return control.keySet();
  }
}