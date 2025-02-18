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

    File        :   AccessPolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicy.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashSet;

import oracle.hst.foundation.collection.MultiSet;
import oracle.hst.foundation.collection.MultiHashSet;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicy
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>AccessPolicy</code> act as generic wrapper for an
 ** <code>Access Policy</code> instance of Oracle Identity Manager.
 **
 ** @author Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicy extends    Entity
                          implements oracle.iam.identity.foundation.naming.AccessPolicy {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-535480798112186598")
  private static final long                        serialVersionUID = 5586500591761051679L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  private Set<ApplicationEntity>                   application      = null;

  /** The container with the {@link RoleEntity}'s that triggers. */
  private Set<RoleEntity>                          role             = null;

  /** The container with the entitlements the policy provisions. */
  private Map<String, MultiSet<EntitlementEntity>> entitlement      = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Access Policy</code> which is associated with the
   ** specified task.
   **
   ** @param  policyKey          the internal system identifier of the
   **                            <code>Access Policy</code>.
   ** @param  policyName         the public name of the
   **                            <code>Access Policy</code>.
   */
  public AccessPolicy(final long policyKey, final String policyName) {
    // ensure inheritance
    super(policyKey, policyName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasApplication
  /**
   ** Returns <code>true</code> if this <code>Access Policy</code> has an
   ** <code>Resource Object</code> assigned; otherwise <code>false</code>.
   **
   ** @return                    <code>true</code> if this
   **                            <code>Access Policy</code> has an
   **                            <code>Resource Object</code> assigned;
   **                            otherwise <code>false</code>.
   */
  public final boolean hasApplication() {
    return (this.application != null && this.application.size() > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the {@link Set} of <code>ApplicationEntity</code>s that are
   ** assigned to this <code>Access Policy</code>.
   **
   ** @return                    the {@link Set} of
   **                            <code>ApplicationEntity</code>s that are
   **                            assigned to this <code>Access Policy</code>.
   */
  public final Set<ApplicationEntity> application() {
    return this.application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasRole
  /**
   ** Returns <code>true</code> if this <code>Access Policy</code> has an
   ** <code>Role</code> assigned; otherwise <code>false</code>.
   **
   ** @return                    <code>true</code> if this
   **                            <code>Access Policy</code> has an
   **                            <code>Role</code> assigned; otherwise
   **                            <code>false</code>.
   */
  public final boolean hasRole() {
    return (this.role != null && this.role.size() > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the {@link Set} of {@link RoleEntity}'s that are assigned to this
   ** <code>Access Policy</code>.
   **
   ** @return                    the {@link Set} of {@link RoleEntity}'s that
   **                            are assigned to this
   **                            <code>Access Policy</code>.
   */
  public final Set<RoleEntity> role() {
    return this.role;
  }

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
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elements (XMLSerializable)
  /**
   ** Returns the XML element tag to marshal/unmarshal multiple entities of
   ** this type.
   **
   ** @return                    the XML element tag to marshal/unmarshal
   **                            multiple entity of this type.
   */
  @Override
  public final String elements() {
    return "AccessPolicies";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (XMLSerializable)
  /**
   ** Returns the XML element tag to marshal/unmarshal a single entity of this
   ** type.
   **
   ** @return                    the XML element tag to marshal/unmarshal a
   **                            single entity of this type.
   */
  @Override
  public final String element() {
    return "AccessPolicy";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity (Entity)
  /**
   ** Returns the XML name used for informational purpose with an end user.
   **
   ** @return                    the XML name used for informational purpose
   **                            with an end user.
   */
  @Override
  public final String entity() {
    return element();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRole
  /**
   ** Adds the specified {@link RoleEntity} to the list of groups that
   ** triggers <code>Access Policy</code>.
   **
   ** @param  role               the {@link RoleEntity} that triggers this
   **                            <code>Access Policy</code> to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addRole(final RoleEntity role) {
    if (this.role == null)
      this.role = new LinkedHashSet<RoleEntity>();

    return this.role.add(role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeRole
  /**
   ** Removes the specified {@link RoleEntity} from the list of groups that
   ** triggers <code>Access Policy</code>.
   **
   ** @param  role               the {@link RoleEntity} that triggers this
   **                            <code>Access Policy</code> to remove.
   **
   ** @return                    <code>true</code> if the list contained the
   **                            specified <code>Role</code>.
   */
  public final boolean removeRole(final RoleEntity role) {
    if (this.role == null)
      return false;

    return this.role.remove(role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   triggeredBy
  /**
   ** Returns <code>true</code> if the specified {@link RoleEntity} is
   ** assigned to this <code>Access Policy</code>; otherwise <code>false</code>.
   **
   ** @param  role               the name of the {@link RoleEntity} whose
   **                            presence in this set is to be tested.
   **
   ** @return                    <code>true</code> if the specified
   **                            <code>Role</code>; is assigned to
   **                            this <code>Access Policy</code>; otherwise
   **                            <code>false</code>.
   */
  public final boolean triggeredBy(final RoleEntity role) {
    if (!hasRole())
      return false;

    return this.role.contains(role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addResource
  /**
   ** Adds the specified <code>ApplicationEntity</code> to the list of resources
   ** that should be provisioned by this <code>Access Policy</code>.
   **
   ** @param  resource           the <code>ApplicationEntity</code> that should
   **                            be provisioned by this
   **                            <code>Access Policy</code> to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addResource(final ApplicationEntity resource) {
    if (this.application == null)
      this.application = new LinkedHashSet<ApplicationEntity>();

    return this.application.add(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeResource
  /**
   ** Removes the specified <code>ApplicationEntity</code> from the list of
   ** resources that should be provisioned by this <code>Access Policy</code>.
   **
   ** @param  resource           the <code>ApplicationEntity</code> that should
   **                            be provisioned by this
   **                            <code>Access Policy</code> to remove.
   **
   ** @return                    <code>true</code> if the list contained the
   **                            specified <code>Resource Object</code>.
   */
  public final boolean removeResource(final ApplicationEntity resource) {
    if (this.application == null)
      return false;

    return this.application.remove(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisions
  /**
   ** Returns <code>true</code> if the specified <code>Access Policy</code> is
   ** assigned to provisioned by this <code>Access Policy</code>; otherwise
   ** <code>false</code>.
   **
   ** @param  resource           the {@link Application}whose presence in this
   **                            set is to be tested as provisioned
   **                            <code>Application</code> of this
   **                            <code>Access Policy</code>.
   **
   ** @return                    <code>true</code> if the specified
   **                            <code>Access Policy</code>; is assigned to
   **                            provisioned by this <code>Access Policy</code>;
   **                            otherwise <code>false</code>
   */
  public final boolean provisions(final Application resource) {
    if (!hasApplication())
      return false;

    return this.application.contains(resource.name());
  }

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
   ** Adds the specified <code>EntitlementEntity</code>s to the list of
   ** entitlements that should be provisioned by this <code>Access Policy</code>.
   **
   ** @param  entitlement        the internal name of the
   **                            <code>EntitlementEntity</code>s that is handled
   **                            inside of Oracle Identity Manager.
   ** @param  values             the <code>EntitlementEntity</code>s
   **                            representation (Hashed Value) that should be
   **                            provisioned by this <code>Access Policy</code>
   **                            to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Collection<EntitlementEntity> addEntitlement(final String entitlement, final Set<EntitlementEntity> values) {
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

    set.addAll(values);
    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addEntitlement
  /**
   ** Adds the specified <code>EntitlementEntity</code>s to the set of
   ** entitlements that should be provisioned by this
   ** <code>Access Policy</code>.
   **
   ** @param  entitlement        the <code>EntitlementEntity</code>s that should
   **                            be provisioned by this
   **                            <code>Access Policy</code> to add.
   ** @param  values             the {@link EntitlementEntity}s to add.
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
   ** entitlements that should be provisioned by this <code>Access Policy</code>.
   **
   **
   ** @param  entitlement        the <code>EntitlementEntity</code>s that should
   **                            be provisioned by this
   **                            <code>Access Policy</code> to remove.
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