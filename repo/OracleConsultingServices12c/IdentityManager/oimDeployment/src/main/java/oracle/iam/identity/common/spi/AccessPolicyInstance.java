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

    File        :   AccessPolicyInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.entitymgr.vo.EntityConstants;

import oracle.iam.accesspolicy.vo.AccessPolicy;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceAction;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicyInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessPolicyInstance</code> represents an <code>Access Policy</code>
 ** instance in Identity Manager that might be created, deleted or configured
 ** after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicyInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String OWNER_DEFAULT = "xelsysadm";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean   request = false;
  private boolean   retrofit = true;
  private long      priority = -1L;
  private OwnerType ownerType = OwnerType.USER;
  private String    ownerName = OWNER_DEFAULT;
  private String    description = null;

  /**
   ** the collection of <code>Resource Object</code>s the
   ** <code>Access Policy</code> provisiones if triggered
   */
  private List<Provision> allow = new ArrayList<Provision>();

  /**
   ** the collection of <code>Resource Object</code>s the
   ** <code>Access Policy</code> no longer denies if triggered
   */
  private List<ProvisioningInstance> denied = new ArrayList<ProvisioningInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class OwnerType
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>OwnerType</code> defines the type of a ownership operation
   */
  public static enum OwnerType implements ServiceAction {

      USER(EntityConstants.USER_TYPE.toLowerCase(), AccessPolicy.OwnerType.USER)
    , ROLE(EntityConstants.ROLE_TYPE.toLowerCase(), AccessPolicy.OwnerType.ROLE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String                 id;
    private AccessPolicy.OwnerType value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>OwnerType</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    OwnerType(final String id, final AccessPolicy.OwnerType value) {
      this.id = id;
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the value of the owner type to be exported or imported.
     **
     ** @return                    the value of the owner type to be exported or
     **                            imported.
     */
    public final AccessPolicy.OwnerType value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id (ServiceAction)
    /**
     ** Returns the id of the property.
     **
     ** @return                  the id of the property.
     */
    @Override
    public String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper owner type from the given string
     ** value.
     **
     ** @param  value              the string value the owner type should be
     **                            returned for.
     **
     ** @return                    the owner type property.
     */
    public static OwnerType from(final String value) {
      for (OwnerType cursor : OwnerType.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Provision
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Provision</code> represents a <code>Resource Object</code>
   ** instance in Identity Manager that will be provisioned by an
   ** <code>Access Policy</code>.
   */
  public static class Provision extends ProvisioningInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** <code>true</code> to revoke the account and the entitlements associated
     ** with an access policy when the access policy is no longer applicable.
     */
    private boolean revoke = true;

    /**
     ** determines how previously defined default data are handled in a modify
     ** operation.
     ** <br>
     ** If <code>true</code> all previously defined default data on the effected
     ** <code>Access Policy</code> are deleted and only the default data
     ** contained in the data set are applied as default data.
     */
    private boolean reset = false;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Provision</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Provision() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: revoke
    /**
     ** Called to inject the argument for parameter <code>revoke</code>.
     **
     ** @param  revoke           if <code>true</code> the account and the
     **                          entitlements associated with an access policy
     **                          are revoked when the access policy is no longer
     **                          applicable.
     */
    public void revoke(final boolean revoke) {
      this.revoke = revoke;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: revoke
    /**
     ** Returns <code>true</code> if the account and the entitlements associated
     ** with an access policy are revoked when the access policy is no longer
     ** applicable.
     **
     ** @return                  <code>true</code> if the account and the
     **                          entitlements associated with an access policy
     **                          are revoked when the access policy is no longer
     **                          applicable.
     */
    public final boolean revoke() {
      return this.revoke;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reset
    /**
     ** Called to inject the argument for parameter <code>reset</code>.
     **
     ** @param  reset            <code>true</code> if in a modify operation all
     **                          previously defined default data on the effected
     **                          <code>Access Policy</code> are deleted and only
     **                          the default data contained in the data set are
     **                          applied as default data.
     */
    public void reset(final boolean reset) {
      this.reset = reset;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reset
    /**
     ** Returns <code>true</code> if in a modify operation all previously
     ** defined default data on the effected <code>Access Policy</code> are
     ** deleted and only the default data contained in the data set are
     ** applied as default data.
     **
     ** @return                  <code>true</code> if in a modify operation all
     **                          previously defined default data on the effected
     **                          <code>Access Policy</code> are deleted and only
     **                          the default data contained in the data set are
     **                          applied as default data.
     */
    public final boolean reset() {
      return this.reset;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: disable
    /**
     ** Called to inject the argument for parameter <code>disable</code>.
     **
     ** @param  disable          if <code>true</code> the account and the
     **                          entitlements associated with an access policy
     **                          are disabled when the access policy is no
     **                          longer applicable.
     */
    public void disable(final boolean disable) {
      this.revoke = !disable;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: disable
    /**
     ** Returns <code>true</code> if the account and the entitlements associated
     ** with an access policy are disabled when the access policy is no longer
     ** applicable.
     **
     ** @return                  <code>true</code> if the account and the
     **                          entitlements associated with an access policy
     **                          are disabled when the access policy is no
     **                          longer applicable.
     */
    public final boolean disable() {
      return !this.revoke;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessPolicyInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessPolicyInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Called to inject the argument for parameter <code>request</code>.
   **
   ** @param  request            if <code>true</code> the access policy is
   **                            created with request approval. When an access
   **                            policy become applicable, a request is created,
   **                            and provisioning of resources is subject to
   **                            request approval.
   */
  public void request(final boolean request) {
    this.request = request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Returns <code>true</code> the access policy is created with request
   ** approval. When an access policy become applicable, a request is created,
   ** and provisioning of resources is subject to request approval.
   **
   ** @return                    <code>true</code> if the access policy is
   **                            created with request approval. When an access
   **                            policy become applicable, a request is created,
   **                            and provisioning of resources is subject to
   **                            request approval.
   */
  public final boolean request() {
    return this.request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrofit
  /**
   ** Called to inject the argument for parameter <code>retrofit</code>.
   **
   ** @param  retrofit           if <code>true</code> then existing role
   **                            memberships are taken into consideration at the
   **                            time the the Evaluate User Policies job is run.
   */
  public void retrofit(final boolean retrofit) {
    this.retrofit = retrofit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrofit
  /**
   ** Returns <code>true</code> if the existing role memberships are taken
   ** into consideration at the time the the Evaluate User Policies job is run.
   **
   ** @return                    <code>true</code> then existing role
   **                            memberships are taken into consideration at the
   **                            time the the Evaluate User Policies job is run.
   */
  public final boolean retrofit() {
    return this.retrofit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   priority
  /**
   ** Called to inject the argument for parameter <code>priority</code>.
   **
   ** @param  priority           the priority of the <code>Access Policy</code>
   **                            instance in Identity Manager.
   */
  public void priority(final long priority) {
    this.priority = priority;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   priority
  /**
   ** Returns the priority of the access policy in Identity Manager to handle.
   **
   ** @return                    the priority of the access policy in Identity
   **                            Manager to handle.
   */
  public final long priority() {
    return this.priority;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Called to inject the argument for parameter <code>description</code>.
   **
   ** @param  description        the description of the
   **                            <code>Access Policy</code> instance in Identity
   **                            Manager.
   */
  public void description(final String description) {
    this.description = description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the <code>Access Policy</code> instance of
   ** Identity Manager to handle.
   **
   ** @return                    the name of the <code>Access Policy</code>
   **                            instance in Identity Manager.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerType
  /**
   ** Called to inject the argument for parameter <code>ownerType</code>.
   **
   ** @param  type               the type of the owner of the
   **                            <code>Access Policy</code> instance of Identity
   **                            Manager object.
   **                            This is either <code>Role</code> or
   **                            <code>User</code>.
   */
  public void ownerType(final String type) {
    ownerType(OwnerType.from(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerType
  /**
   ** Called to inject the argument for parameter <code>ownerType</code>.
   **
   ** @param  type               the type of the owner of the
   **                            <code>Access Policy</code> instance in Identity
   **                            Manager.
   **                            This is either <code>Role</code> or
   **                            <code>User</code>.
   */
  public void ownerType(final OwnerType type) {
    this.ownerType = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerType
  /**
   ** Returns the owner of the <code>Access Policy</code> instance of
   ** Identity Manager to handle.
   **
   ** @return                    the type of the owner of the
   **                            <code>Access Policy</code> instance in Identity
   **                            Manager.
   **                            This is either <code>Role</code> or
   **                            <code>User</code>.
   */
  public final OwnerType ownerType() {
    return this.ownerType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerName
  /**
   ** Called to inject the argument for parameter <code>ownerName</code>.
   **
   ** @param  name               the owner of the <code>Access Policy</code>
   **                            instance in Identity Manager.
   **                            This is either the public identifier of a
   **                            <code>Role</code> or <code>User</code>
   **                            entity.
   */
  public void ownerName(final String name) {
    this.ownerName = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ownerName
  /**
   ** Returns the owner of the <code>Access Policy</code> instance of
   ** Identity Manager to handle.
   **
   ** @return                    the owner of the <code>Access Policy</code>
   **                            instance in Identity Manager.
   **                            This is either the public identifier of a
   **                            <code>Role</code> or <code>User</code>
   **                            entity.
   */
  public final String ownerName() {
    return this.ownerName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   allow
  /**
   ** Returns the collection of <code>Resource Object</code>s the access
   ** policy provisions.
   **
   ** @return                  the collection of <code>Resource Object</code>s
   **                          the access policy provisions.
   */
  public final List<Provision> allow() {
    return this.allow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   denied
  /**
   ** Returns the collection of <code>Resource Object</code>s the access
   ** policy denies.
   **
   ** @return                  the collection of <code>Resource Object</code>s
   **                          the access policy denies.
   */
  public final List<ProvisioningInstance> denied() {
    return this.denied;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAllow
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link Provision} to add.
   **
   ** @throws BuildException     if the specified {@link Provision} is already
   **                            assigned to this task.
   */
  public void addAllow(final Provision object)
    throws BuildException {

    // prevent bogus input
    if ((this.allow != null && this.allow.contains(object)) || (this.denied != null && this.denied.contains(object)))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    this.allow.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDenied
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link ProvisioningInstance} to add.
   **
   ** @throws BuildException     if the specified {@link ProvisioningInstance}
   **                            is already assigned to this task.
   */
  public void addDenied(final ProvisioningInstance object)
    throws BuildException {

    // prevent bogus input
    if ((this.allow != null && this.allow.contains(object)) || (this.denied != null && this.denied.contains(object)))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    this.denied.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of a role like catogory and additional parameters. If
   ** it's <code>false</code> only the name of the role has to be present.
   **
   ** @param  operation          the operational mode of validation.
   **                            If it's set to <code>create</code> the
   **                            validation is extended to check for all the
   **                            mandatory parameters of a role like displayName
   **                            etc. If it's something else only the name of
   **                            the role has to be present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation)
    throws BuildException {

    // ensure inheritance
    super.validate();

    // enforce validation of mandatory attributes if requested
    switch (operation) {
      case create : if (CollectionUtility.empty(this.allow) && CollectionUtility.empty(this.denied))
                      handleElementMissing("provision and/or deny");
                    // intentionally fall through
      case modify : if (StringUtility.isEmpty(this.description))
                      handleAttributeMissing("description");
                    if (StringUtility.isEmpty(this.ownerName))
                      handleAttributeMissing("ownerName");
                    break;
      case delete : if (this.allow.size() > 0)
                      handleElementUnexpected("provision", ServiceOperation.revoke.id());
                    if (this.denied.size() > 0)
                      handleElementUnexpected("deny", ServiceOperation.revoke.id());
    }

    for (Provision cursor : this.allow)
      cursor.validate();

    for (ProvisioningInstance cursor : this.denied)
      cursor.validate();
  }
}