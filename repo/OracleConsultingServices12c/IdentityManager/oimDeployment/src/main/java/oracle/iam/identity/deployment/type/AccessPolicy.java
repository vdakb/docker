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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AccessPolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AccessPolicy.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.AccessPolicyInstance;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicy
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>AccessPolicy</code> represents an <code>Access Policy</code> instance
 ** in Identity Manager that might be created or configured after or during an
 ** import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicy extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the exportable/importableobject definitions in alphabetical order
  private static String[] registry = { AccessPolicyInstance.OwnerType.ROLE.id(), AccessPolicyInstance.OwnerType.USER.id() };

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class OwnerType
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Owner</code> defines the type of a ownership operation
   */
  public static class OwnerType extends EnumeratedAttribute {

    //////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the value of the oparation a task will execute.
     **
     ** @return                    the value of the oparation a task will
     **                            execute.
     */
    public final String value() {
      return super.getValue();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   getValues (EnumeratedAttribute)
    /**
     ** The only method a subclass needs to implement.
     **
     ** @return                  an array holding all possible values of the
     **                          enumeration. The order of elements must be
     **                          fixed so that indexOfValue(String) always
     **                          return the same index for the same value.
     */
    @Override
    public String[] getValues() {
      return registry;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Allow
  // ~~~~~ ~~~~~
  /**
   ** <code>Allow</code> represents a <code>Application</code> instance in
   ** Identity Manager that will be provisioned by an <code>Access Policy</code>.
   */
  public static class Allow extends Provisioning {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Allow</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Allow() {
      // ensure inheritance
      super(new AccessPolicyInstance.Provision());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setReset
    /**
     ** Called to inject the argument for parameter <code>reset</code>.
     **
     ** @param  value            <code>true</code> if in a modify operation all
     **                          previously defined default data on the effected
     **                          <code>Access Policy</code> are deleted and only
     **                          the default data contained in the data set are
     **                          applied as default data.
     */
    public void setReset(final boolean value) {
      checkAttributesAllowed();
      ((AccessPolicyInstance.Provision)this.delegate).reset(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setRevoke

    /**
     ** Called to inject the argument for parameter <code>revoke</code>.
     **
     ** @param  value            if <code>true</code> the account and the
     **                          entitlements associated with an access policy
     **                          are revoked when the access policy is no longer
     **                          applicable.
     */
    public void setRevoke(final boolean value) {
      checkAttributesAllowed();
      ((AccessPolicyInstance.Provision)this.delegate).revoke(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setDisable
    /**
     ** Called to inject the argument for parameter <code>disable</code>.
     **
     ** @param  value            if <code>true</code> the account and the
     **                          entitlements associated with an access policy
     **                          are disabled when the access policy is no
     **                          longer applicable.
     */
    public void setDisable(final boolean value) {
      checkAttributesAllowed();
      ((AccessPolicyInstance.Provision)this.delegate).disable(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessPolicy</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessPolicy() {
    // ensure inheritance
    super(new AccessPolicyInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>AccessPolicy</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(((AccessPolicyInstance)this.delegate).description()))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRetrofit
  /**
   ** Called to inject the argument for parameter <code>retrofit</code>.
   **
   ** @param  retrofit           if <code>true</code> then existing role
   **                            memberships are taken into consideration at the
   **                            time the the Evaluate User Policies job is run.
   */
  public void setRetrofit(final boolean retrofit) {
    checkAttributesAllowed();
    final AccessPolicyInstance delegate = (AccessPolicyInstance)this.delegate;
    delegate.retrofit(retrofit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRequest
  /**
   ** Called to inject the argument for parameter <code>request</code>.
   **
   ** @param  request            <code>true</code> if the access policy is
   **                            created with request approval. When an access
   **                            policy become applicable, a request is created,
   **                            and provisioning of resources is subject to
   **                            request approval.
   */
  public void setRequest(final boolean request) {
    checkAttributesAllowed();
    final AccessPolicyInstance delegate = (AccessPolicyInstance)this.delegate;
    delegate.request(request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPriority
  /**
   ** Called to inject the argument for parameter <code>priority</code>.
   **
   ** @param  priority           the priority of the <code>Access Policy</code>
   **                            instance of Identity Manager object.
   */
  public void setPriority(final long priority) {
    checkAttributesAllowed();
    final AccessPolicyInstance delegate = (AccessPolicyInstance)this.delegate;
    delegate.priority(priority);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Called to inject the argument for parameter <code>description</code>.
   **
   ** @param  description        the description of the
   **                            <code>Access Policy</code> instance in Identity
   **                            Manager.
   */
  public void setDescription(final String description) {
    checkAttributesAllowed();
    final AccessPolicyInstance delegate = (AccessPolicyInstance)this.delegate;
    delegate.description(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOwnerType
  /**
   ** Called to inject the argument for parameter <code>ownerType</code>.
   **
   ** @param  owner              the owner type of the
   **                            <code>Access Policy</code> instance in
   **                            Identity Manager.
   **                            This is either the public identifier of a
   **                            <code>Role</code> or <code>User</code>
   **                            entity.
   */
  public void setOwnerType(final OwnerType owner) {
    checkAttributesAllowed();
    final AccessPolicyInstance delegate = (AccessPolicyInstance)this.delegate;
    delegate.ownerType(owner.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOwnerName
  /**
   ** Called to inject the argument for parameter <code>owner</code>.
   **
   ** @param  owner              the owner of the <code>Access Policy</code>
   **                            instance in Identity Manager.
   **                            This is either the public identifier of a
   **                            <code>Role</code> or <code>User</code>
   **                            entity.
   */
  public void setOwnerName(final String owner) {
    checkAttributesAllowed();
    final AccessPolicyInstance delegate = (AccessPolicyInstance)this.delegate;
    delegate.ownerName(owner);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link AccessPolicyInstance} delegate of Identity Manager to
   ** handle.
   **
   ** @return                    the {@link AccessPolicyInstance} delegate of
   **                            Identity Manager to handle.
   */
  public final AccessPolicyInstance instance() {
    if (isReference())
      return ((AccessPolicy)getCheckedRef()).instance();

    return (AccessPolicyInstance)this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredProvision
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>provision</code>.
   **
   ** @param  resource           the {@link Allow} to add.
   **
   ** @throws BuildException     if the specified {@link Resource} object
   **                            is already part of the denied resources.
   */
  public void addConfiguredProvision(final Allow resource)
    throws BuildException {

    final AccessPolicyInstance delegate = (AccessPolicyInstance)this.delegate;
    delegate.addAllow((AccessPolicyInstance.Provision)resource.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredDeny
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>deny</code>.
   **
   ** @param  resource           the {@link Provisioning} to add.
   **
   ** @throws BuildException     if the specified {@link Provisioning} object
   **                            is already part of the denied resources.
   */
  public void addConfiguredDeny(final Provisioning resource)
    throws BuildException {

    final AccessPolicyInstance delegate = (AccessPolicyInstance)this.delegate;
    delegate.addDenied(resource.instance());
  }
}