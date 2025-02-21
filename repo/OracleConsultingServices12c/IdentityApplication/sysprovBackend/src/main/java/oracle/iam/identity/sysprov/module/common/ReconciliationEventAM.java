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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Provisioning Management

    File        :   ReconciliationEventAM.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ReconciliationEventAM.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.module.common;

import oracle.jbo.ApplicationModule;

////////////////////////////////////////////////////////////////////////////////
// class ReconciliationEventAM
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>Application Module</code> to coordinate the particular task
 ** belonging to <code>Reconciliation Event</code>s.
 ** <p>
 ** ---------------------------------------------------------------------
 ** ---    File generated by Oracle ADF Business Components Design Time.
 ** ---    Mon Mar 20 14:49:55 CET 2017
 ** ---------------------------------------------------------------------
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public interface ReconciliationEventAM extends ApplicationModule {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchByIdentifier
  /**
   ** Finds a certain <code>Reconciliation Event</code> by executing a query
   ** against the persistence layer which leverage the primary key of the entity
   ** object.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to fetch from
   **                            the persistence layer.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  void fetchByIdentifier(final Long identifier);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchByStatus
  /**
   ** Finds a certain <code>Reconciliation Event</code> by executing a query
   ** against the persistence layer which leverage the status of the event.
   **
   ** @param  status             the status value of the
   **                            <code>Reconciliation Event</code> to fetch from
   **                            the persistence layer.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  void fetchByStatus(final String status);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchDetail
  /**
   ** Initialize the model to maintain an existing
   ** <code>Reconciliation Event</code>.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to fetch from
   **                            the persistence layer.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  void fetchDetail(final Long identifier);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshAttribute
  /**
   ** Refresh the attributes belonging to a certain
   ** <code>Reconciliation Event</code>.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to
   **                            initialize.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  void refreshAttribute(final Long identifier);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeEvent
  /**
   ** Close the <code>Reconciliation Event</code> with the given
   ** identifier.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to
   **                            reevaluate.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  void closeEvent(final Long identifier);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reevaluateEvent
  /**
   ** Re-Evaluates the <code>Reconciliation Event</code> with the given
   ** identifier.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to
   **                            reevaluate.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  void reevaluateEvent(final Long identifier);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   linkEvent
  /**
   ** Link the <code>Reconciliation Event</code> with the given
   ** identifier to the specififed identity.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to link.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  identity           the system identifier of the
   **                            <code>Identity</code> to link.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  void linkEvent(final Long identifier, final Long identity);
}