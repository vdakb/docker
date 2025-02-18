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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   EntityReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntityReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.reconciliation;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

import oracle.iam.identity.foundation.offline.Entity;

////////////////////////////////////////////////////////////////////////////////
// abstract class EntityReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>EntityReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile entity information from a CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
abstract class EntityReconciliation<E extends Entity> extends Reconciliation<E> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntityReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   */
  protected EntityReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method groupd by functionality
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
    // this will produce the trace of the configured task parameter and create
    // the abstract file paths to the data and error directories
    // it will instantiate the unmarshaller and resolve the resource object an
    // it resource the configured application instance belongs to
    super.initialize();

    try {
      this.filter.clear();
      this.filter.put(ResourceObject.NAME, this.reconcileObject());
      tcResultSet resultSet = this.objectFacade().findObjects(filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { this.reconcileObject()  };
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
      }
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }

    final String method = "initialize";
    final String path   = stringValue(RECONCILE_DESCRIPTOR);
    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      if (document == null)
        throw new TaskException(TaskError.METADATA_OBJECT_NOTFOUND, path);

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

    if (this.descriptor.attributeMapping().isEmpty())
      throw new TaskException(TaskError.ATTRIBUTE_MAPPING_EMPTY);
  }
}