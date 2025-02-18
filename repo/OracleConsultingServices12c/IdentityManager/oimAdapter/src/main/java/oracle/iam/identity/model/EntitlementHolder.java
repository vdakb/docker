/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   EntitlementHolder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementHolder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.model;

import java.util.Map;
import java.util.TreeMap;

import oracle.hst.foundation.collection.MultiSet;
import oracle.hst.foundation.collection.MultiHashSet;

import oracle.iam.identity.foundation.offline.EntitlementEntity;

////////////////////////////////////////////////////////////////////////////////
// abstract class EntitlementHolder
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A factory to create objects and their instance relationships.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class EntitlementHolder extends Accessor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /** The container with the entitlements the policy provisions. */
  private Map<String, MultiSet<EntitlementEntity>> entitlement = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementHolder</code> which is associated with the
   ** specified task.
   **
   ** @param  objectKey          the internal system identifier of the
   **                            Oracle Identity Manager object.
   ** @param  objectName         the public name of the Oracle Identity Manager
   **                            object.
   */
  public EntitlementHolder(final long objectKey, final String objectName) {
    // ensure inheritance
    super(objectKey, objectName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasEntitlement
  /**
   ** Returns <code>true</code> if this <code>Access Policy</code> has an
   ** <code>Entitlement</code> assigned; otherwise <code>false</code>.
   **
   ** @return                    <code>true</code> if this
   **                            <code>Access Policy</code> has an
   **                            <code>Entitlement</code> assigned; otherwise
   **                            <code>false</code>.
   */
  public final boolean hasEntitlement() {
    return (this.entitlement != null && this.entitlement.size() > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Returns the {@link Map} of <code>Entitlement</code>s that are
   ** provisioned by this <code>Access Policy</code>.
   **
   ** @return                    the {@link Map} of <code>Entitlement</code>s
   **                            that are provisioned by this
   **                            <code>Access Policy</code>.
   */
  public final Map<String, MultiSet<EntitlementEntity>> entitlement() {
    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addEntitlement
  /**
   ** Adds the specified <code>EntitlementEntity</code>s to the list of
   ** entitlements that should be provisioned by this
   ** <code>Access Policy</code>.
   **
   ** @param  entitlement        the internal name of the
   **                            <code>EntitlementEntity</code>s that is
   **                            handled inside of Oracle Identity Manager.
   ** @param  value              the <code>Entitlement</code>s representation
   **                            (Hashed Value) that should be provisioned by
   **                            this <code>Access Policy</code> to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Object addEntitlement(final String entitlement, final EntitlementEntity[] value) {
    if (this.entitlement == null)
      // we use a TreeMap to ensure that the order in which the collection will
      // return the KeySet is always the same
      this.entitlement = new TreeMap<String, MultiSet<EntitlementEntity>>();

    MultiSet<EntitlementEntity> set = null;
    if (!this.entitlement.containsKey(entitlement)) {
      set = new MultiHashSet<EntitlementEntity>();
      this.entitlement.put(entitlement, set);
    }
    else
      set = this.entitlement.get(entitlement);

    for (int i = 0; i < value.length; i++)
      set.add(value[i]);

    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addEntitlement
  /**
   ** Adds the specified <code>EntitlementEntity</code>s to the list of
   ** entitlements that should be provisioned by this <code>Access Policy</code>.
   **
   ** @param  entitlement        the <code>EntitlementEntity</code>s that
   **                            should be provisioned by this
   **                            <code>Access Policy</code> to add.
   ** @param  values             the collection of
   **                            <code>EntitlementEntity</code>s discovered so
   **                            far.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Object addEntitlement(final String entitlement, final MultiSet<EntitlementEntity> values) {
    if (this.entitlement == null)
      // we use a TreeMap to ensure that the order in which the collection will
      // return the KeySet is always the same
      this.entitlement = new TreeMap<String, MultiSet<EntitlementEntity>>();

    MultiSet<EntitlementEntity> set = null;
    if (!this.entitlement.containsKey(entitlement)) {
      set = new MultiHashSet<EntitlementEntity>();
      this.entitlement.put(entitlement, values);
    }
    else
      set = this.entitlement.get(entitlement);

    set.addAll(values);
    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeEntitlement
  /**
   ** Removes the specified <code>Entitlement</code> from the list of
   ** entitlements that should be provisioned by this <code>Access Policy</code>.
   **
   **
   ** @param  entitlement        the <code>Entitlement</code>s that should be
   **                            provisioned by this <code>Access Policy</code>
   **                            to remove.
   **
   ** @return                    <code>true</code> if the list contained the
   **                            specified <code>Entitlement Object</code>.
   */
  public final Object removeEntitlement(final String entitlement) {
    if (this.entitlement == null)
      return false;

    return this.entitlement.remove(entitlement);
  }
}