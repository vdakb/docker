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

    File        :   Entitlement.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Entitlement.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.EntitlementData;

////////////////////////////////////////////////////////////////////////////////
// class Entitlement
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>Entitlement</code> represents a entitlement in Identity Manager that
 ** might be created, updated or deleted after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Entitlement extends Element {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entitlement</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Entitlement() {
    // ensure inheritance
    this(new EntitlementData());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entitlement</code> task use the specified
   ** {@link EntitlementData} to store the ANT attributes.
   ** <p>
   ** Commonly used by sub classes to pass in their own class of an
   ** {@link EntitlementData}.
   **
   ** @param  instance           the {@link EntitlementData} to store the ANT
   **                            attributes.
   */
  protected Entitlement(final EntitlementData instance) {
    // ensure inheritance
    super(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another <code>Role</code>
   ** instance.
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

    if (!StringUtility.isEmpty(this.delegate.name()))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setApplication
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>application</code>.
   **
   ** @param  application        the <code>application</code> presented in the
   **                            user interface for an <code>Entitlement</code>.
   */
  public void setApplication(final String application) {
    checkAttributesAllowed();
    ((EntitlementData)this.delegate).parent(application);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>displayName</code>.
   **
   ** @param  displayName        the <code>displayName</code> presented in the
   **                            user interface for an <code>Entitlement</code>.
   */
  public void setDisplayName(final String displayName) {
    checkAttributesAllowed();
    ((EntitlementData)this.delegate).displayName(displayName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description        the <code>displayName</code> presented in the
   **                            user interface for an <code>Entitlement</code>.
   */
  public void setDescription(final String description) {
    checkAttributesAllowed();
    ((EntitlementData)this.delegate).description(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredPublication
  /**
   ** Add the specified {@link Publication} rule to add.
   **
   ** @param  publication        the {@link Publication} rule to add.
   */
  public void addConfiguredPublication(final Publication publication) {
    checkAttributesAllowed();
    ((EntitlementData)this.delegate).addPublication(publication.value(), publication.recipient());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRequest
  /**
   ** Add the specified {@link Request} to add.
   **
   ** @param  request            the {@link Request} to add.
   */
  public void addConfiguredRequest(final Request request) {
    checkAttributesAllowed();
    ((EntitlementData)this.delegate).addRequest(request.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredProvision
  /**
   ** Add the specified {@link Provisioning} to the collection of beneficiaries
   ** to be treated as assign.
   **
   ** @param  provisioning       the {@link Provisioning} to add.
   */
  public void addConfiguredProvision(final Provisioning provisioning) {
    checkAttributesAllowed();
    ((EntitlementData)this.delegate).addProvision(provisioning.instance());
  }
}