/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Identity Governance Services Provisioning

    File        :   AccountResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountResource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v2.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.schema.User;

import oracle.iam.identity.icf.scim.annotation.Schema;
import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

import static oracle.iam.identity.icf.scim.annotation.Definition.Returned;
import static oracle.iam.identity.icf.scim.annotation.Definition.Mutability;
import static oracle.iam.identity.icf.scim.annotation.Definition.Uniqueness;

////////////////////////////////////////////////////////////////////////////////
// class AccountResource
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** SCIM provides a resource type for <code>User</code> resources.
 ** <br>
 ** The core schema for <code>User</code> is identified using the URI:
 ** <code>urn:ietf:params:scim:schemas:core:2.0:User</code>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id = NewUserResource.SCHEMA, name=User.NAME, description="The core schema for \"User\" provides the common attributes of a user resource. . The user core schema is identified using the following schema URI: \"urn:ietf:params:scim:schemas:core:2.0:User\".")
public class AccountResource extends NewUserResource<AccountResource> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(Tenant.PREFIX)
  @Attribute(description="A list of permissions that the user claims into a tenant.", mutability=Definition.Mutability.READ_ONLY, multiValueClass=Tenant.class)
  private List<Tenant> tenant;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Tenant
  // ~~~~~ ~~~~~ ~~~~~~
  /**
   ** Stores roles for users in a tenant.
   */
  public static final class Tenant {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The prefix to identitify this resource */
    public static final String PREFIX = "tenants";

    /** The name of the user property. */
    @JsonIgnore(true)
    public static final String VALUE  = "value";

    /** The name of the permission property. */
    @JsonIgnore(true)
    public static final String SCOPE  = "scope";


    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty(VALUE)
    @Attribute(description="The identifier of the tenant.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private String value;

    @JsonProperty("display")
    @Attribute(description="A human readable name, primarily used for display purposes.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private String display;

    @JsonProperty("$ref")
    @Attribute(description="The URI of the user resource in the tenant's claim.", required=false, reference={ "Tenant", "Claim" }, mutability=Mutability.IMMUTABLE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private URI    ref;

    @JsonProperty(SCOPE)
    @Attribute(description="The scopes that a user belongs to in a tenant.", required=true, caseExact=false, returned=Returned.DEFAULT, multiValueClass=String.class)
    private String scope;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Tenant</code> SCIM Resource that allows use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Tenant() {
   	  // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Role</code> SCIM Resource with the properties
     ** specified.
     **
     ** @param  value            the identifier of the user resource.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Tenant(final String value) {
 	    // ensure inheritance
      super();

   	  // initalize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the identifier of the User's claim.
     **
     ** @param  value            the identifier of the User's claim.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant value(final String value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the identifier of the User's claim.
     **
     ** @return                  the identifier of the User's claim.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: display
    /**
     ** Sets the display name, primarily used for display purposes.
     **
     ** @param  value            the display name, primarily used for display
     **                          purposes.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant display(final String value) {
      this.display = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: display
    /**
     ** Returns the display name, primarily used for display purposes.
     **
     ** @return                  the display name, primarily used for display
     **                          purposes.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String display() {
      return this.display;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: ref
    /**
     ** Sets the URI of the SCIM resource corresponding to this Tenant's Claim.
     **
     ** @param  value            the URI of the SCIM resource corresponding to
     **                          this Tenant's Claim.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant ref(final URI value) {
      this.ref = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: ref
    /**
     ** Returns the URI of the SCIM resource corresponding to this Tenant's Claim.
     **
     ** @return                  the URI of the SCIM resource corresponding to
     **                          this Tenant's Claim.
     **                          <br>
     **                          Possible object is {@link URI}.
     */
    public final URI ref() {
      return this.ref;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Sets the scope a claim member resource claims in a tenant.
     **
     ** @param  value            the roles a claim member resource claims in a
     **                          tenant.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant scope(final String value) {
      this.scope = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Returns the scope a member resource claims in a tenant.
     **
     ** @return                  the scope a member resource claims in a tenant.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String scope() {
      return this.scope;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a new <code>Role</code> with the properties
     ** specified.
     **
     ** @param  value            the identifier of the user resource.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Role</code> populated with the
     **                          specified properties.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public static Tenant of(final String value) {
      return new Tenant(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Account</code> SCIM Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountResource() {
  	// ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Sets the collection of permissions the user belongs to in a tenant.
   **
   ** @param  value              the collection of permissions the user belongs
   **                            to in a tenant.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Tenant}.
   **
   ** @return                    the <code>AccountResource</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AccountResource</code> .
   */
  public final AccountResource tenant(final List<Tenant> value) {
    this.tenant = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Returns the collection of permissions the user belongs to in a tenant.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of permissions the user belongs
   **                            to in a tenant.
   **                            <br>
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Tenant}.
   */
  public final List<Tenant> tenant() {
    return this.tenant;
  }
}