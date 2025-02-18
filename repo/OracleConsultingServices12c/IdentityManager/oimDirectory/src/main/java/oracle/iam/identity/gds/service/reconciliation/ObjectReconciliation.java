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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   ObjectReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ObjectReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

////////////////////////////////////////////////////////////////////////////////
// abstract class ObjectReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ObjectReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile entity information from a Directory
 ** Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
abstract class ObjectReconciliation extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the all purpose container to search and post objects
  protected final HashMap filter = new HashMap();

  private final   String  multiValueFeature;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ObjectReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   */
  protected ObjectReconciliation() {
    // ensure inheritance
    this(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ObjectReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   **
   ** @param  multiValueFeature  the entry in the <code>Metadata Descriptor</code>
   **                            Server Feature that declares the multi-valued
   **                            attributes.
   */
  protected ObjectReconciliation(final String multiValueFeature) {
    // ensure inheritance
    super();

    this.multiValueFeature = multiValueFeature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes (Reconciliation)
  /**
   ** Returns the array of attribute names that will be passed to a Directory
   ** Server search operation to specify which attributes the Directory Server
   ** should return.
   **
   ** @return                   the array of attribute names that will be passed
   **                           to a Directory Server search operation to
   **                           specify which attributes the Directory Server
   **                           should return.
   */
  @Override
  protected final Set<String> returningAttributes() {
    return this.descriptor.returningAttributes();
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

    final String method  = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // ensure inheritance
      // this will produce the trace of the configured task parameter
      super.initialize();

      // trigger fetching the multi-valued attribute configuration defined for
      // the object class from the server feature configuration
      if (!StringUtility.isEmpty(this.multiValueFeature))
        initializeMultivalueAttribute(this.multiValueFeature);

      final String path = reconcileDescriptor();
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
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryDN
  /**
   ** Returns the distinguished name of an entry from the provided attribute
   ** mapping.
   **
   ** @param  subject            the attribute mapping to inspect.
   **
   ** @return                    the distinguished name of an entry from the
   **                            provided attribute mapping.
   */
  protected String entryDN(final Map<String, Object> subject) {
    // obtain the distinguished name from the subject
    return (String)subject.get(this.connector().distinguishedNameAttribute());
  }
}