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

    File        :   CatalogListener.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CatalogListener.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.Collection;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// interface CatalogListener
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The listener notified to harvest a particular batch of catalog items.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface CatalogListener extends EntityListener<CatalogEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  processRole
  /**
   ** Reconciles a particular batch of {@link RoleEntity}'s.
   **
   ** @param  batch              the {@link Collection} of {@link RoleEntity}'s
   **                            to synchronize with the catalog.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  void processRole(final Collection<RoleEntity> batch)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:  processInstance
  /**
   ** Reconciles a particular batch of {@link ApplicationEntity}'s.
   **
   ** @param  batch              the {@link Collection} of
   **                            {@link ApplicationEntity}'s to synchronize with
   **                            the catalog.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  void processInstance(final Collection<ApplicationEntity> batch)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:  processEntitlement
  /**
   ** Reconciles a particular batch of {@link EntitlementEntity}'s belonging to
   ** the specified {@link ApplicationEntity}.
   **
   ** @param  instance           the {{@link ApplicationEntity} the specified
   **                            {@link Collection} of
   **                            {@link EntitlementEntity}'s belongs to.
   ** @param  namespace          the identifier the {@link EntitlementEntity}'s
   **                            belongs to.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  void processEntitlement(final ApplicationEntity instance, final String namespace)
    throws TaskException;
}