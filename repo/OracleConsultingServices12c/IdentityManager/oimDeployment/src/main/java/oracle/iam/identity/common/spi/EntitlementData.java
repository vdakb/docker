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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   EntitlementData.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementData.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import oracle.hst.deployment.ServiceError;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.deployment.type.Publication;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementData
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>EntitlementData</code> represents an application instance in Oracle
 ** Identity Manager that might be created, deleted or changed after or during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EntitlementData extends CatalogElement {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the parent <code>Application Instance</code> in Identity Manager this
   ** <code>Application Instance</code> is depended on.
   */
  private String parent = null;

  private List<Publication.Recipient> enabled = new ArrayList<Publication.Recipient>();
  private List<Publication.Recipient> disabled = new ArrayList<Publication.Recipient>();

  private List<ProvisioningInstance> direct = new ArrayList<ProvisioningInstance>();
  private List<ProvisioningInstance> request = new ArrayList<ProvisioningInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementData</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntitlementData() {
    // ensure inheritance
    this(OIMType.Entitlement);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementData</code> for a specific type
   **
   ** @param  type               the <code>Catalog Type</code> an
   **                            <code>Catalog Element</code> belongs to.
   */
  protected EntitlementData(final OIMType type) {
    // ensure inheritance
    super(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Called to inject the parent <code>Application Instance</code> an
   ** <code>Entitlement</code> depends on in Identity Manager.
   **
   ** @param  parent             the parent <code>Application Instance</code> an
   **                            an <code>Entitlement</code> depends on in
   **                            Identity Manager.
   */
  public void parent(final String parent) {
    this.parent = parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Returns the parent <code>Application Instance</code> an
   ** <code>Entitlement</code> depends on in Identity Manager.
   **
   ** @return                    the parent <code>Application Instance</code> an
   **                            an <code>Entitlement</code> depends on in
   **                            Identity Manager.
   */
  public String parent() {
    return this.parent;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   enabled
  /**
   ** Returns the {@link List} of recipients in Identity Manager to be assigned
   ** to this <code>Entitlement</code>.
   **
   ** @return                    the {@link List} of recipients in Identity
   **                            Manager to be assigned to this
   **                            <code>Entitlement</code>.
   */
  public final List<Publication.Recipient> enabled() {
    return this.enabled;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   disabled
  /**
   ** Returns the {@link List} of recipients in Identity Manager to be revoked
   ** from this <code>Entitlement</code>.
   **
   ** @return                    the {@link List} of recipients in Identity
   **                            Manager to be revoked from this
   **                            <code>Entitlement</code>.
   */
  public final List<Publication.Recipient> disabled() {
    return this.disabled;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   direct
  /**
   ** Returns the {@link List} of benefiaries in Identity Manager to be assigned
   ** to or revoked from this <code>Application Instance</code>.
   **
   ** @return                    the {@link List} of recipients in Identity
   **                            Manager to be assigned to or revoked from this
   **                            <code>Application Instance</code>.
   */
  public final List<ProvisioningInstance> direct() {
    return this.direct;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Returns the {@link List} of requests in Identity Manager to be assigned to
   ** or revoked from this <code>Entitlement</code>.
   **
   ** @return                    the {@link List} of requests in Identity
   **                            Manager to be assigned to or revoked from this
   **                            <code>Entitlement</code>.
   */
  public final List<ProvisioningInstance> request() {
    return this.request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of an user profile like type. If it's
   ** <code>false</code> only the login name of the user profile has to be
   ** present.
   **
   ** @param  operation          the operational mode of validation.
   **                            If it's set to <code>create</code> the
   **                            validation is extended to check for all the
   **                            mandatory parameters of an entitlement like
   **                            displayName etc. If it's something else only
   **                            the name of the entitlement has to be present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation)
    throws BuildException {

    // ensure inheritance
    super.validate(operation);

    switch (operation) {
      case create : 
      case modify : if (this.parameter().isEmpty() && this.enabled.isEmpty() && this.disabled.isEmpty() && this.direct.isEmpty() && this.request.isEmpty())
                      handleElementMissing("parameter and/or publication and/or request and/or provision");
                    break;
      default     : if (this.request.size() != 0)
                      handleElementUnexpected("request", operation.id());
                    if (this.direct.size() != 0)
                      handleElementUnexpected("direct", operation.id());
    }

    if (!this.direct.isEmpty())
      for (ProvisioningInstance cursor : this.direct) {
        cursor.validate();
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPublication
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  action             the operation to apply on the specified
   **                            <code>recipient</code>s; either
   **                            {@link ServiceOperation#enable} or
   **                            {@link ServiceOperation#disable}.
   ** @param  recipient          the {@link List} of organizational recipients
   **                            in Identity Manager to become access of the
   **                            entitlement.
   */
  public void addPublication(final ServiceOperation action, final List<Publication.Recipient> recipient) {
    if (ServiceOperation.enable == action)
      this.enabled.addAll(recipient);
    else if (ServiceOperation.disable == action)
      this.disabled.addAll(recipient);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProvision
  /**
   ** Add the specified value {@link ProvisioningInstance} to the collection of
   ** beneficiaries to be treated as assign or revoke after an import operation.
   **
   ** @param  object             the {@link ProvisioningInstance} to add.
   **
   ** @throws BuildException     if the specified {@link ProvisioningInstance}
   **                            is already assigned to this task.
   */
  public void addProvision(final ProvisioningInstance object) {
    // prevent bogus input
    if (this.direct.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.direct.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRequest
  /**
   ** Add the specified value {@link ProvisioningInstance} to the collection of
   ** requests to be treated as assign, revoke, enable, disable, modify after an
   ** import operation.
   **
   ** @param  object             the {@link ProvisioningInstance} to add.
   **
   ** @throws BuildException     if the specified {@link ProvisioningInstance}
   **                            is already assigned to this task.
   */
  public void addRequest(final ProvisioningInstance object) {
    // prevent bogus input
    if (this.request.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.request.add(object);
  }
}