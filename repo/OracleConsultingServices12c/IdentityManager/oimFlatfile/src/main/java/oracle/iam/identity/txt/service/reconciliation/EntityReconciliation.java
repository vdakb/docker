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
    Subsystem   :   TXT Flatfile Connector

    File        :   EntityReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntityReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-18  DSteding    First release version
*/

package oracle.iam.identity.txt.service.reconciliation;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.SystemMessage;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

import oracle.iam.identity.utility.file.FlatFileAttribute;
import oracle.iam.identity.utility.file.FlatFileDescriptor;

////////////////////////////////////////////////////////////////////////////////
// abstract class EntityReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>EntityReconciliation</code> acts as the service end point for
 ** Oracle Identity Manager to to reconcile entity information from a flat file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class EntityReconciliation extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the all purpose container to search and post objects
  protected Map<String, Object> filter = new HashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntityReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
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

    final String method = "initialize";

    // ensure inheritance
    // this will produce the trace of the configured task parameter
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

    final String path = stringValue(RECONCILE_DESCRIPTOR);
    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(reconcileDescriptor()));
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildIdentifier
  /**
   ** Create a String with the concatenated idtentifier attributes.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @return                    a String with the concatenated idtentifier
   **                            attributes.
   */
  protected final String buildIdentifier(final Map<String, Object> subject) {
    // build the identifier of the subject
    StringBuilder buffer= new StringBuilder();
    // get the identifier from the descriptor
    Iterator<FlatFileAttribute> i = this.processor().descriptor().identifierIterator();
    while(i.hasNext()) {
      String name = i.next().name();
      buffer.append(subject.get(name));
      if (i.hasNext())
        buffer.append(SystemConstant.BLANK);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do target reconciliation of Oracle Identity Manager Users.
   **
   ** @param  identifier         the composed identifier of the {@link Map} to
   **                            reconcile.
   ** @param  transaction        the transaction code of the {@link Map} to
   **                            reconcile.
   ** @param  data               the {@link Map} to reconcile.
   ** @param  multivalued        the {@link Map} of attributes treated as
   **                            multi-valued.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected void processSubject(final String identifier, final String transaction, final Map<String, Object> data, final Map<String, Object> multivalued)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter  = {TaskBundle.string(TaskMessage.ENTITY_ACCOUNT),  identifier};
    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
     debug(method, TaskBundle.format(TaskMessage.ENTITY_RECONCILE, parameter));

    try {
      // handle deletion at first
      if (FlatFileDescriptor.DELETE.equalsIgnoreCase(transaction)) {
        // create the reconciliation event for deletion
        processDeleteEvent(data);
      }
      else {
        if (this.facade().ignoreEvent(this.reconcileObject(), data)) {
          info(TaskBundle.format(TaskMessage.EVENT_IGNORED, this.descriptor.identifier(), identifier));
          incrementIgnored();
        }
        else {
          // create the reconciliation event for create or modify
          long eventKey = changelogEvent(data, (multivalued != null && multivalued.size() > 0));
          closeEvent(eventKey);
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}