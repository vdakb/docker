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
    Subsystem   :   Common Shared Offline Application Management

    File        :   ApplicationAccount.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationAccount.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.LinkedList;

import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationAccount
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A {@link ApplicationEntity} that wrappes the custom level type
 ** <code>Application</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class ApplicationAccount extends ApplicationEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8788946924298893507")
  private static final long  serialVersionUID = 512586326605134800L;

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  public static final String MULTIPLE         = "accounts";
  public static final String SINGLE           = "account";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link TreeSet} of <code>Access Policies</code> that are
   ** provisioning this <code>Application</code>
   */
  private transient Set<AccessPolicy>                    policy = null;

  // we use a TreeMap to ensure that the order in which the collection will
  // return the KeySet is always the same
  private transient Map<String, List<EntitlementEntity>> entitlements = new TreeMap<String, List<EntitlementEntity>>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationAccount</code> with the specified name but
   ** without an valid identifier.
   ** <p>
   ** The identifier the <code>ApplicationAccount</code> belongs to has to be
   ** populated manually.
   **
   ** @param  name               the identifying name of the
   **                            <code>Application</code>.
   */
  public ApplicationAccount(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationAccount</code> with the specified
   ** identifier and name.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>Application</code> to load.
   ** @param  name               the identifying name of the
   **                            <code>Application</code>.
   */
  public ApplicationAccount(final long identifier, final String name) {
    // ensure inheritance
    super(identifier, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationAccount</code> which is associated with the
   ** specified task.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>Application</code> to load.
   ** @param  name               the identifyingname of the
   **                            <code>Application</code>.
   ** @param  status             the status of the <code>Application</code>.
   */
  public ApplicationAccount(final long identifier, final String name, final String status) {
    // ensure inheritance
    super(identifier, name, status);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elements (overridden)
  /**
   ** Returns the XML element tag to marshal/unmarshal multiple entities of
   ** this type.
   **
   ** @return                    the XML element tag to marshal/unmarshal
   **                            multiple entity of this type.
   */
  @Override
  public String elements() {
    return MULTIPLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (overridden)
  /**
   ** Returns the XML element tag to marshal/unmarshal a single entity of this
   ** type.
   **
   ** @return                    the XML element tag to marshal/unmarshal a
   **                            single entity of this type.
   */
  @Override
  public final String element() {
    return SINGLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity (overridden)
  /**
   ** Returns the XML name used for informational purpose with an end user.
   **
   ** @return                    the XML name used for informational purpose
   **                            with an end user.
   */
  @Override
  public final String entity() {
    return TaskBundle.string(TaskMessage.ENTITY_ACCOUNT);
  }

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
  public final boolean hasEntitlements() {
    return (this.entitlements != null && this.entitlements.size() > 0);
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
  public final Map<String, List<EntitlementEntity>> entitlements() {
    return this.entitlements;
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
   ** Adds the specified <code>Entitlement</code>s to the list of entitlements
   ** that should be provisioned by this <code>EntitlementHolder</code>.
   **
   ** @param  entitlement        the internal name of the
   **                            <code>Entitlement</code>s that is handled
   **                            inside of Oracle Identity Manager.
   ** @param  values             the <code>Entitlement</code>s representation
   **                            (Hashed Value) that should be provisioned by
   **                            this <code>EntitlementHolder</code> to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Object addEntitlement(final String entitlement, final List<EntitlementEntity> values) {
    List<EntitlementEntity> set = null;
    if (!this.entitlements.containsKey(entitlement)) {
      set = new LinkedList<EntitlementEntity>();
      this.entitlements.put(entitlement, set);
    }
    else
      set = this.entitlements.get(entitlement);

    set.addAll(values);

    return set;
  }
}