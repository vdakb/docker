/*
    Oracle Consulting Services

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
    NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
    LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
    ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   EntityListener.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    EntityListener.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.Collection;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// interface EntityListener
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** The listener notified to process a particular batch of {@link Entity}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface EntityListener<E extends Entity> extends Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final int DEFAULT_BULK_SIZE = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   batchSize
  /**
   ** Returns the size of a batch the reconciliation target of this handler
   ** accepts.
   **
   ** @return                    the size of a batch the reconciliation target
   **                            of this handler accepts.
   */
  int batchSize();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullValue
  /**
   ** Returns the value which represents a <code>null</code> for an attribute
   ** element.
   ** <p>
   ** Such specification is required to distinct between empty attribute
   ** elements which are not passed through and overriding an already existing
   ** metadata to make it <code>null</code>.
   **
   ** @return                    the value which represents a <code>null</code> for
   **                            an attribute element.
   */
  String nullValue();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  process
  /**
   ** Reconciles a particular bulk of entities of typ E.
   **
   ** @param  bulk               the {@link Collection} to reconcile.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  void process(final Collection<E> bulk)
    throws TaskException;
}