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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Role.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Role.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.schema;

import java.util.Set;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class Role
// ~~~~~ ~~~~
/**
 ** <code>Role</code> encapsulate the permissions an Pvivotal Cloud Foundry
 ** account can have in a resource like tenant or spaces.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Role implements Entity<Role> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The resource path for tenant users assigned to an account */
  public static final String TENANT_USER    = "organizations";

  /** The resource path for managed tenants assigned to an account */
  public static final String TENANT_MANAGER = "managed_organizations";

  /** The resource path for audited tenants assigned to an account */
  public static final String TENANT_AUDITOR = "audited_organizations";

  /** The resource path for audited tenants assigned to an account */
  public static final String TENANT_BILLING = "billing_managed_organizations";

  /** The resource path for space users assigned to an account */
  public static final String SPACE_USER     = "spaces";

  /** The resource path for managed spaces assigned to an account */
  public static final String SPACE_MANAGER  = "managed_spaces";

  /** The resource path for audited spaces assigned to an account */
  public static final String SPACE_AUDITOR  = "audited_spaces";

  public static final Set<String> TENANT    = CollectionUtility.set(TENANT_USER, TENANT_MANAGER, TENANT_AUDITOR, TENANT_BILLING);
  public static final Set<String> SPACE     = CollectionUtility.set(SPACE_USER, SPACE_MANAGER, SPACE_AUDITOR);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String rolename;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Role</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Role() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (Entity)
  /**
   ** Sets the name of the <code>Role</code>.
   **
   ** @param  value              the name of the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  @Override
  public final Role name(final String value) {
    this.rolename = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (Entity)
  /**
   ** Returns the name of the <code>Role</code>.
   **
   ** @return                    the name of the <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String name() {
    return this.rolename;
  }
}