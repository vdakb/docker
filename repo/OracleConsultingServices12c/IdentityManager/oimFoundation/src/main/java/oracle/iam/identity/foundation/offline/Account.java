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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   Account.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Account.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;

import oracle.hst.foundation.collection.MultiSet;
import oracle.hst.foundation.collection.MultiHashSet;

import oracle.iam.provisioning.vo.Entitlement;

////////////////////////////////////////////////////////////////////////////////
// class Account
// ~~~~~ ~~~~~~~
/**
 ** An <code>Account</code> aggregates all permissions an account should
 ** be provisoned to.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Account extends Identity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6783501740765815122")
  private static final long                                  serialVersionUID = -557099033450666415L;


  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link Set} of <code>Access Policies</code> that are provisioning this
   ** <code>Identity</code>
   */
  private Set<AccessPolicy>                                  policy           = null;

  /**
   ** The container that provides the <code>Account Data</code> the user have in
   ** the source system.
   */
  private transient Map<String, Object>                      sourceData;

  /**
   ** The container that provides the <code>Account Data</code> the user should
   ** have in the target system.
   */
  private transient Map<String, Object>                      targetData;

  /** The container with the entitlements the policy provisions. */
  private transient Map<String, MultiSet<EntitlementEntity>> entitlement = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Account</code> with the specified name but without an
   ** valid identifier.
   ** <p>
   ** The identifier the <code>Account</code> belongs to has to be populated
   ** manually.
   **
   ** @param  name               the identifying name of the
   **                            <code>Account</code>.
   */
  public Account(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Account</code> which is associated with the
   ** specified task.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>Account</code> to load.
   ** @param  name               the identifying name of the
   **                            <code>Account</code>.
   */
  public Account(final long identifier, final String name) {
    // ensure inheritance
    super(identifier, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policy
  /**
   ** Returns the <code>Access Policies</code> for this
   ** <code>Application</code>.
   **
   ** @return                    the <code>Access Policies</code> for this
   **                            <code>Application</code>.
   */
  public final Set<AccessPolicy> policy() {
    return this.policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisionedBy
  /**
   ** Returns <code>true</code> if the specified {@link AccessPolicy}
   ** provisions this <code>Application</code>.
   **
   ** @param  policy             the {@link AccessPolicy} that provisions this
   **                            <code>Application</code> to test.
   **
   ** @return                    <code>true</code> if this
   **                            <code>Application</code> is provisioned by the
   **                            specified {@link AccessPolicy}.
   */
  public final boolean provisionedBy(final AccessPolicy policy) {
    return (this.policy == null) ? false : this.policy.contains(policy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceData
  /**
   ** Returns the {@link Map} of <code>Account Data</code>s that are
   ** provisioned to the user in the source system.
   **
   ** @return                    the {@link Map} of
   **                            <code>Account Data</code>s that are provisioned
   **                            to the user in the source system.
   */
  public final Map<String, Object> sourceData() {
    return this.sourceData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceData
  /**
   ** Sets the {@link Map} of <code>Account Data</code>s that are
   ** provisioned to the user in the source system.
   **
   ** @param  data               the {@link Map} of
   **                            <code>Account Data</code>s that are provisioned
   **                            to the user in the source system.
   */
  public final void sourceData(final Map<String, Object> data) {
    this.sourceData = data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   targetData
  /**
   ** Returns the {@link Map} of <code>Account Data</code>s that should be
   ** provisioned to the user in the target system.
   **
   ** @return                    the {@link Map} of
   **                            <code>Account Data</code>s that should be
   **                            provisioned to the user in the target system.
   */
  public final Map<String, Object> targetData() {
    return this.targetData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceData
  /**
   ** Sets the {@link Map} of <code>Account Data</code>s that should be
   ** provisioned to the user in the target system.
   **
   ** @param  data               the {@link Map} of
   **                            <code>Account Data</code>s that should be
   **                            provisioned to the user in the target system.
   */
  public final void targetData(final Map<String, Object> data) {
    this.targetData = data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasEntitlement
  /**
   ** Returns <code>true</code> if this <code>EntitlementHolder</code> has an
   ** <code>Entitlement</code> assigned; otherwise <code>false</code>.
   **
   ** @return                    <code>true</code> if this
   **                            <code>EntitlementHolder</code> has an
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
   ** provisioned by this <code>EntitlementHolder</code>.
   **
   ** @return                    the {@link Map} of <code>Entitlement</code>s
   **                            that are provisioned by this
   **                            <code>EntitlementHolder</code>.
   */
  public final Map<String, MultiSet<EntitlementEntity>> entitlement() {
    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementToAssign
  /**
   ** Returns the array {@link Entitlement} for a specific
   ** <code>Role</code> if this <code>Role</code> contained in the
   ** collection of <code>Role</code> to assign.
   **
   ** @param  role               the {@link RoleEntity} to discover.
   ** @return                    the array {@link Entitlement} for a specific
   **                            <code>Role</code> contained in the
   **                            collection of <code>Role</code> to
   **                            assign.
   */
  public final Collection<EntitlementEntity> entitlementToAssign(final RoleEntity role) {
    return (this.entitlement != null) ? this.entitlement.get(role) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPolicy
  /**
   ** Adds the {@link AccessPolicy} that provisions this
   ** <code>Application</code>.
   **
   ** @param  policy             the {@link AccessPolicy} that provisions this
   **                            <code>Application</code> to add.
   **
   ** @return                    <code>true</code> if this account did not
   **                            already contain the specified
   **                            {@link AccessPolicy}.
   */
  public final boolean addPolicy(final AccessPolicy policy) {
    if (this.policy == null)
      this.policy = new TreeSet<AccessPolicy>();

    return this.policy.add(policy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePolicy
  /**
   ** Removes the {@link AccessPolicy} that provisions this
   ** <code>Application</code>.
   **
   ** @param  policy             the {@link AccessPolicy} that provisions this
   **                            <code>Application</code> to remove.
   **
   ** @return                    <code>true</code> if this account contained the
   **                            specified {@link AccessPolicy}.
   */
  public final boolean removePolicy(final AccessPolicy policy) {
    if (this.policy == null)
      return false;

    return this.policy.remove(policy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addEntitlement
  /**
   ** Adds the specified <code>EntitlementEntity</code>s to the list of
   ** entitlements that should be provisioned to this <code>identity</code>.
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
  public final Collection<EntitlementEntity> addEntitlement(final String entitlement, final EntitlementEntity[] value) {
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
   ** Adds the specified <code>Entitlement</code>s to the list of entitlements
   ** that should be provisioned by this <code>Identity</code>.
   **
   ** @param  entitlement        the internal name of the
   **                            <code>EntitlementEntity</code>s that is
   **                            handled inside of Oracle Identity Manager.
   ** @param  values             the <code>Entitlement</code>s representation
   **                            that should be provisioned to this
   **                            <code>Identity</code> to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Collection<EntitlementEntity> addEntitlement(final String entitlement, final MultiSet<EntitlementEntity> values) {
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
   ** entitlements that should be provisioned by this <code>Identity</code>.
   **
   **
   ** @param  entitlement        the <code>Entitlement</code>s that should be
   **                            provisioned by this <code>Identity</code> to
   **                            remove.
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