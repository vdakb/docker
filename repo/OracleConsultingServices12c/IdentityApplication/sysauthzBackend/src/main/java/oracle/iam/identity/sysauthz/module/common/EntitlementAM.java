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
    Subsystem   :   System Authorization Management

    File        :   EntitlementAM.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    EntitlementAM.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.module.common;

import java.util.Map;
import java.util.List;

import oracle.jbo.ApplicationModule;

import oracle.iam.platform.authopss.vo.EntityPublication;

import oracle.iam.ui.platform.model.common.IdentityModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementAM
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>Application Module</code> to coordinate the particular task
 ** belonging to entitlements.
 ** <p>
 ** ---------------------------------------------------------------------
 ** --- File generated by Oracle ADF Business Components Design Time.
 ** --- Mon Mar 06 19:50:30 CET 2017
 ** ---------------------------------------------------------------------
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public interface EntitlementAM extends ApplicationModule {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchByIdentifier
  /**
   ** Finds a certain <code>Entitlement</code> by executing a query against the
   ** persistence layer which leverage the primary key of the entity object.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Entitlement</code> to fetch from the
   **                            persistence layer.
   */
  void fetchByIdentifier(final String identifier);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchByName
  /**
   ** Finds a certain <code>Entitlement</code> by executing a query against the
   ** persistence layer which leverage the unique key of the entity object.
   **
   ** @param  name               the system identifier of the
   **                            <code>Entitlement</code> to fetch from the
   **                            persistence layer.
   */
  void fetchByName(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignEntitlementPublication
  /**
   ** Assigns the passed <code>Entity Publication</code>s to an
   ** <code>Entitlement</code>.
   **
   ** @param  selection          the {@link List} of organizations represented
   **                            by {@link IdentityModelAdapterBean} to be
   **                            assigned.
   */
  void assignEntitlementPublication(final List<IdentityModelAdapterBean> selection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeEntitlementPublication
  /**
   ** Revokes the passed <code>Entity Publication</code>s from an
   ** <code>Entitlement</code>.
   **
   ** @param  selection          the {@link List} of {@link EntityPublication}
   **                            to revoke.
   */
  void revokeEntitlementPublication(final List<EntityPublication> selection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshAttribute
  /**
   ** Refresh the attributes belonging to a certain <code>Entitlement</code>.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Entitlement</code> to initialize.
   **                            Allowed object is {@link String}.
   */
  void refreshAttribute(final String identifier);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requeryPublication
  /**
   ** Initialize the <code>Entity Publication</code>s belonging to a certain
   ** <code>Entitlement</code> to take in account pending changes on
   ** publications.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Entitlement</code> to initialize.
   **                            Allowed object is {@link String}.
   ** @param  pending            the collection of pending changes belonging to
   **                            assigned, revoked and modified publications.
   */
  void requeryPublication(final String identifier, final List<Map<String, Object>> pending);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePublication
  /**
   ** Performing an update on the passed <code>Entity Publications</code>.
   **
   ** @param  entityType         the type of the entity to update, either
   **                            <ul>
   **                              <li>Entitlement
   **                              <li>Entitlement
   **                            </ul>.
   ** @param  entityId           the system identifier of the entity to update.
   ** @param  metadata           the data providing the changes.
   */
  void updatePublication(final String entityType, final String entityId, final Map<String, Map<String, Object>> metadata);
}