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

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Provisioning

    File        :   ClaimResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ClaimResource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.v2;

import java.util.LinkedList;
import java.util.Collection;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import oracle.iam.platform.scim.annotation.Schema;
import oracle.iam.platform.scim.annotation.Attribute;

import static oracle.iam.platform.scim.annotation.Attribute.Returned;
import static oracle.iam.platform.scim.annotation.Attribute.Uniqueness;
import static oracle.iam.platform.scim.annotation.Attribute.Mutability;

////////////////////////////////////////////////////////////////////////////////
// final class ClaimResource
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** This represents a SCIM schema extension commonly used in representing
 ** roles that belong to users.
 ** <br>
 ** The core schema for <code>ClaimResource</code> is identified using the URI
 ** <code>urn:ietf:params:scim:schemas:extension:p20:1.0:Claim</code>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id=ClaimResource.ID, name=ClaimResource.NAME, description="Identity Governance Services Tenant Claim")
public class ClaimResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ID     = "urn:ietf:params:scim:schemas:extension:p20:1.0:Claim";
  public static final String NAME   = "Claim";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(Tenant.PREFIX)
  @Attribute(description="A collection of permissions claimed for users in tenants.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, multiValueClass=Tenant.class)
  private Collection<Tenant> tenant;

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

    /** The prefix to identitify the role resource in a tenant */
    public static final String PREFIX = "tenants";

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty("value")
    @Attribute(description="The identifier of a tenant in which permissions are claimed for a user.", required=true, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private String value;

    @JsonProperty("display")
    @Attribute(description="A human readable name of that tenant, primarily used for display purposes.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private String display;

    @JsonProperty("$ref")
    @Attribute(description="The URI of a tenant in which permissions are claimed for a user.", required=false, reference={ "User", "Claim" }, mutability=Mutability.IMMUTABLE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private URI    ref;

    @JsonProperty("scope")
    @Attribute(description="The scope claimed for a user in a tenant.", required=true, caseExact=false, returned=Returned.DEFAULT)
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
     ** Constructs a <code>Tenant</code> SCIM Resource with the properties
     ** specified.
     **
     ** @param  value            the identifier of the user resource.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  display          the display name, primarily used for display
     **                          purposes.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Tenant(final String value, final String display) {
 	    // ensure inheritance
      super();

   	  // initalize instance attributes
      this.value   = value;
      this.display = display;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the identifier of the tenant claimed by s user.
     **
     ** @param  value            the identifier of the tenant claimed by s user.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
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
     ** Returns the identifier of the tenant claimed by s user.
     **
     ** @return                  the identifier of the tenant claimed by s user.
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
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
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
     ** Sets the URI of the SCIM resource corresponding to this claim.
     **
     ** @param  value            the URI of the SCIM resource corresponding to
     **                          this claim.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
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
     ** Returns the URI of the SCIM resource corresponding to this claim.
     **
     ** @return                  the URI of the SCIM resource corresponding to
     **                          this claim.
     **                          <br>
     **                          Possible object is {@link URI}.
     */
    public final URI ref() {
      return this.ref;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Sets the scope a users claims in a tenant.
     **
     ** @param  value            the scope a users claims in a tenant.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
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
     ** Returns the scope a users claims in a tenant.
     **
     ** @return                  the scope a users claims in a tenant.
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
     ** Factory method to create a new <code>Claim</code> with the properties
     ** specified.
     **
     ** @param  value            the identifier of the user resource.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  display          the display name, primarily used for display
     **                          purposes.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> populated with the
     **                          specified properties.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public static Tenant of(final String value, final String display) {
      return new Tenant(value, display);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      return Objects.hash(this.value, this.display, this.ref, this.scope);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Tenant</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Tenant</code>s may be different even though they contain the same
     ** set of names with the same values, but in a different order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Tenant that = (Tenant)other;
      return Objects.equals(this.value,   that.value)
          && Objects.equals(this.ref,     that.ref)
          && Objects.equals(this.display, that.display)
          && Objects.equals(this.scope,   that.scope);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ClaimResource</code> SCIM Resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ClaimResource() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Sets the collection of tenants for the User that collectively the
   ** permissions the User claims on that tenants.
   **
   ** @param  value              the collection of tenants for the User.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Tenant}.
   **
   ** @return                    the <code>ClaimResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ClaimResource</code>.
   */
  public final ClaimResource tenant(final Collection<Tenant> value) {
    this.tenant = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Returns the collection of tenants for the User that collectively the
   ** permissions the User claims on that tenants.
   **
   ** @return                    the collection of tenants for the User.
   **                            <br>
   **                            Possible object is {@link Collection} of where
   **                            each element is of type {@link Tenant}.
   */
  public final Collection<Tenant> tenant() {
    return this.tenant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add a tenant to the collection of permissions
   **
   ** @param  value              the system identifier of the user the claim
   **                            belongs too.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  display            the unique identifier of the user the claim
   **                            belongs too.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  scope              the scope the user the claims in the tenant.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void add(final String value, final String display, final String scope) {
    if (this.tenant == null)
      this.tenant = new LinkedList<>();

    this.tenant.add(Tenant.of(value, display).scope(scope));
  }
}