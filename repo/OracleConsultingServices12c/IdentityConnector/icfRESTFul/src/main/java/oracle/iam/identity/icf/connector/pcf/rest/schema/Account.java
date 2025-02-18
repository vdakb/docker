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

    File        :   Account.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Account.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

////////////////////////////////////////////////////////////////////////////////
// class Account
// ~~~~~ ~~~~~~~
public class Account implements Entity<Account> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("username")
  private String username;

  @JsonProperty("admin")
  private boolean admin;

  @JsonProperty("active")
  private boolean active;

  @JsonProperty("default_space_guid")
  private String  defaultSpace;

  @JsonProperty("organizations_url")
  private String  endpointOrganization;

  @JsonProperty("managed_organizations_url")
  private String  endpointOrganizationManaged;
  
  @JsonProperty("audited_organizations_url")
  private String  endpointOrganizationAudited;

  @JsonProperty("billing_managed_organizations_url")
  private String  endpointOrganizationBilling;

  @JsonProperty("spaces_url")
  private String  endpointSpace;

  @JsonProperty("managed_spaces_url")
  private String  endpointSpaceManaged;

  @JsonProperty("audited_spaces_url")
  private String  endpointSpaceAudited;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Account</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Account() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   admin
  /**
   ** Sets the admin flag of the <code>Account</code>.
   **
   ** @param  value              the admin flag of the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final Account admin(final boolean value) {
    this.admin = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   admin
  /**
   ** Returns the admin flag of the <code>Account</code>.
   **
   ** @return                    the admin flag of the <code>Account</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean admin() {
    return this.admin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Sets the active flag of the <code>Account</code>.
   **
   ** @param  value              the active flag of the <code>Account</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account active(final boolean value) {
    this.active = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Returns the active flag of the <code>Account</code>.
   **
   ** @return                    the active flag of the <code>Account</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean active() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultSpace
  /**
   ** Sets the default space URI of the <code>Account</code>.
   **
   ** @param  value              the default space URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account defaultSpace(final String value) {
    this.defaultSpace = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  defaultSpace
  /**
   ** Returns the default space URI of the <code>Account</code>.
   **
   ** @return                    the default space URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String defaultSpace() {
    return this.defaultSpace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointOrganization
  /**
   ** Sets the organization endpoint URI of the <code>Account</code>.
   **
   ** @param  value              the organization endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account endpointOrganization(final String value) {
    this.endpointOrganization = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointOrganization
  /**
   ** Returns the organization endpoint URI of the <code>Account</code>.
   **
   ** @return                    the organization endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointOrganization() {
    return this.endpointOrganization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointOrganizationManaged
  /**
   ** Sets the managed organization endpoint URI of the <code>Account</code>.
   **
   ** @param  value              the managed organization endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account endpointOrganizationManaged(final String value) {
    this.endpointOrganizationManaged = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointOrganization
  /**
   ** Returns the managed organization endpoint URI of the <code>Account</code>.
   **
   ** @return                    the managed organization endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointOrganizationManaged() {
    return this.endpointOrganizationManaged;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointOrganizationAudited
  /**
   ** Sets the audited organization endpoint URI of the <code>Account</code>.
   **
   ** @param  value              the audited organization endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account endpointOrganizationAudited(final String value) {
    this.endpointOrganizationAudited = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointOrganizationAudited
  /**
   ** Returns the audited organization endpoint URI of the <code>Account</code>.
   **
   ** @return                    the audited organization endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointOrganizationAudited() {
    return this.endpointOrganizationAudited;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointOrganizationBilling
  /**
   ** Sets the billing organization endpoint URI of the <code>Account</code>.
   **
   ** @param  value              the billing organization endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account endpointOrganizationBilling(final String value) {
    this.endpointOrganizationBilling = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointOrganizationBilling
  /**
   ** Returns the billing organization endpoint URI of the <code>Account</code>.
   **
   ** @return                    the billing organization endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointOrganizationBilling() {
    return this.endpointOrganizationBilling;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointSpace
  /**
   ** Sets the space endpoint URI of the <code>Account</code>.
   **
   ** @param  value              the space endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account endpointSpace(final String value) {
    this.endpointSpace = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointSpace
  /**
   ** Returns the space endpoint URI of the <code>Account</code>.
   **
   ** @return                    the space endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointSpace() {
    return this.endpointSpace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointSpaceManaged
  /**
   ** Sets the managed space endpoint URI of the <code>Account</code>.
   **
   ** @param  value              the managed space endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account endpointSpaceManaged(final String value) {
    this.endpointSpaceManaged = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointSpaceManaged
  /**
   ** Returns the managed space endpoint URI of the <code>Account</code>.
   **
   ** @return                    the managed space endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointSpaceManaged() {
    return this.endpointSpaceManaged;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointSpaceAudited
  /**
   ** Sets the audited space endpoint URI of the <code>Account</code>.
   **
   ** @param  value              the audited space endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account endpointSpaceAudited(final String value) {
    this.endpointSpaceAudited = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointSpaceAudited
  /**
   ** Returns the audited space endpoint URI of the <code>Account</code>.
   **
   ** @return                    the audited space endpoint URI of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointSpaceAudited() {
    return this.endpointSpaceAudited;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (Entity)
  /**
   ** Sets the name of the <code>Account</code>.
   **
   ** @param  value              the name of the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final Account name(final String value) {
    this.username = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (Entity)
  /**
   ** Returns the name of the <code>Account</code>.
   **
   ** @return                    the name of the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String name() {
    return this.username;
  }
}