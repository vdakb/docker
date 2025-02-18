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

    File        :   TenantResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TenantResource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v2.schema;

import java.net.URI;

import java.util.Set;
import java.util.Objects;
import java.util.HashSet;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.identity.icf.scim.annotation.Schema;
import oracle.iam.identity.icf.scim.annotation.Attribute;

import static oracle.iam.identity.icf.scim.annotation.Definition.Mutability;
import static oracle.iam.identity.icf.scim.annotation.Definition.Returned;
import static oracle.iam.identity.icf.scim.annotation.Definition.Uniqueness;

import oracle.iam.identity.icf.scim.schema.Support;
import oracle.iam.identity.icf.scim.schema.Metadata;
import oracle.iam.identity.icf.scim.schema.Resource;
import oracle.iam.identity.icf.scim.schema.GenericResource;

////////////////////////////////////////////////////////////////////////////////
// class TenantResource
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** SCIM provides a resource type for <code>Tenant</code> resources.
 ** <br>
 ** The core schema for <code>Tenant</code> is identified using the URI
 ** <code>urn:p20:scim:schemas:uid:1.0:Tenant</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@JsonPropertyOrder({"schemas", TenantResource.PRIMARY, TenantResource.STATUS, TenantResource.UNIQUE})
@Schema(id=TenantResource.SCHEMA, name=TenantResource.NAME, description="Identity Governance Services Tenant")
public class TenantResource implements Resource<TenantResource> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource. */
  @JsonIgnore(true)
  public static final String PREFIX  = "tenant";

  /** The name to identitify this resource. */
  @JsonIgnore(true)
  public static final String NAME    = "Tenant";

  /** The identifier of the schema extension. */
  @JsonIgnore(true)
  public static final String SCHEMA  = "urn:p20:scim:schemas:uid:1.0:Tenant";

  /** The name of the primary identifier property. */
  @JsonIgnore(true)
  public static final String PRIMARY = "id";

  /** The name of the unique identifier property. */
  @JsonIgnore(true)
  public static final String UNIQUE  = "displayName";

  /** The name of the administrative state property. */
  @JsonIgnore(true)
  public static final String STATUS  = "active";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(PRIMARY)
  @Attribute(description="The primary identifier for a tenant typically used to identify the tenant at the service provider.", required=true, caseExact=true, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.SERVER)
  private String             identifier;

  @JsonProperty(STATUS)
  @Attribute(description="A Boolean value indicating a tenant's administrative status.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private Boolean            active;

  @JsonProperty(UNIQUE)
  @Attribute(description="The unqiue identifier for a tenant typically used as a human-readable name for a tenant.", required=true, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String             displayName;

  @JsonProperty(Role.PREFIX)
  @Attribute(description="A collection of roles claimed by users in a Tenant.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, multiValueClass=Role.class)
  private Collection<Role>   role;

  // Note that "meta" is a complex attribute, so it is represented by the
  // Metadata class.
  @JsonProperty(Metadata.PREFIX)
  @Attribute(description="A complex attribute containing resource metadata", mutability = Mutability.READ_ONLY)
  private Metadata           metadata;

  // mutability = Mutability.READ_ONLY,
  // This should not be READ_ONLY as the spec says, ie. if upon creation only
  // the default schema is provided and then via an update a custom attribute is
  // specified, the schemas attributes needs to be updated!
  @JsonProperty("schemas")
  @Attribute(description="The schemas attribute is a REQUIRED attribute and is an array of Strings containing URIs that are used to indicate the namespaces of the SCIM schemas that define the attributes present in the current JSON structure", required=true, returned=Returned.ALWAYS, multiValueClass=String.class)
  private Set<String>        namespace;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Role
  // ~~~~~ ~~~~~ ~~~~
  /**
   ** Stores roles for users in a tenant.
   */
  public static final class Role {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The prefix to identitify this resource */
    @JsonIgnore(true)
    public static final String PREFIX = "roles";

    /** The canonical value of a membership to indicate a user. */
    @JsonIgnore(true)
    public static final String USER   = "User";

    /** The canonical value of a membership to indicate a group. */
    @JsonIgnore(true)
    public static final String GROUP  = "Group";

    /** The name of the user property. */
    @JsonIgnore(true)
    public static final String VALUE  = "value";

    /** The name of the permission property. */
    @JsonIgnore(true)
    public static final String SCOPE  = "scope";

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty(value=VALUE, required=true)
    @Attribute(description="The identifier of the user.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private Long   value;

    @JsonProperty(value="type", required=true)
    @Attribute(description="A label indicating the attribute's membership; e.g., 'User' or 'Group'.", required=true, canonical={USER, GROUP}, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private String type;

    @JsonProperty(value=SCOPE, required=true)
    @Attribute(description="The scope that a user belongs to in a tenant.", required=true, caseExact=false, returned=Returned.DEFAULT)
    private String scope;

    @JsonProperty("display")
    @Attribute(description="A human readable name, primarily used for display purposes.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private String display;

    @JsonProperty("$ref")
    @Attribute(description="The URI of the user resource in the tenant's claim.", required=false, reference={ "Tenant", "Claim" }, mutability=Mutability.IMMUTABLE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
    private URI    ref;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Role</code> SCIM Resource that allows use as
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

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Role</code> SCIM Resource with the properties
     ** specified.
     **
     ** @param type              the label indicating the attribute's
     **                          membership; e.g., <code>User</code> or
     **                          <code>Group</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the identifier of the user resource.
     **                          <br>
     **                          Allowed object is {@link Long}.
     */
    private Role(final String type, final Long value) {
 	    // ensure inheritance
      super();

   	  // initalize instance attributes
      this.type  = type;
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Sets label indicating the attribute's membership; e.g.,
     ** <code>User</code> or <code>Group</code>.
     **
     ** @param  value            the label indicating the attribute's
     **                          membership; e.g., <code>User</code> or
     **                          <code>Group</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public final Role type(final String value) {
      this.type = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns label indicating the attribute's membership; e.g.,
     ** <code>User</code> or <code>Group</code>.
     **
     ** @return                  the label indicating the attribute's
     **                          membership; e.g., <code>User</code> or
     **                          <code>Group</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the identifier of the User's claim.
     **
     ** @param  value            the identifier of the User's claim.
     **                          <br>
     **                          Allowed object is {@link Long}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public final Role value(final Long value) {
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
     **                          Possible object is {@link Long}.
     */
    public final Long value() {
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
     **                          Possible object is <code>Role</code>.
     */
    public final Role display(final String value) {
      this.display = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Sets the scope a claim member resource claims in a tenant.
     **
     ** @param  value            the role a member resource claims in a tenant.
     **                          <br>
     **                          Allowed object is{@link String}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public final Role scope(final String value) {
      this.scope = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Returns the scope a claim member resource claims in a tenant.
     **
     ** @return                  the role a member resource claims in a tenant.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String scope() {
      return this.scope;
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
     **                          Possible object is <code>Role</code>.
     */
    public final Role ref(final URI value) {
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
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a new <code>Role</code> with the properties
     ** specified.
     **
     ** @param type              the label indicating the attribute's
     **                          membership; e.g., <code>User</code> or
     **                          <code>Group</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the identifier of the user resource.
     **                          <br>
     **                          Allowed object is {@link Long}.
     **
     ** @return                  the <code>Role</code> populated with the
     **                          specified properties.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public static Role of(final String type, final Long value) {
      return new Role(type, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TenantResource</code> SCIM Resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TenantResource() {
  	// ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TenantResource</code> SCIM Resource with the
   ** properties specified.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>TenantResource</code> that is being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  displayName        the unique identifier of the
   **                            <code>TenantResource</code> that is being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value indicating the administrative status
   **                            of the <code>TenantResource</code> that is
   **                            being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  private TenantResource(final String identifier, final String displayName, final Boolean active) {
  	// ensure inheritance
    super();

  	// initialize instance attributes
    this.identifier  = identifier;
    this.displayName = displayName;
    this.active      = active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Sets the value indicating the Tenant's administrative status.
   **
   ** @param  value              the value indicating the Tenant's
   **                            administrative status.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>TenantResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  public final TenantResource active(final Boolean value) {
    this.active = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Returns the value indicating the Tenant's administrative status.
   **
   ** @return                    the value indicating the Tenant's
   **                            administrative status.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean active() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the name of the <code>TenantResource</code>, suitable for display to
   ** end-users.
   **
   ** @param  value              the name of the <code>TenantResource</code>,
   **                            suitable for display to end-users.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>TenantResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  public TenantResource displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the name of the Tenant, suitable for display to end-users.
   **
   ** @return                    the name of the Tenant, suitable for display to
   **                            end-users.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String displayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Sets the name of the {@link Role}s, suitable for display to end-users.
   **
   ** @param  value              the collection of {@link Role}s.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Role}.
   **
   ** @return                    the <code>TenantResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  public TenantResource role(final Collection<Role> value) {
    this.role = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the name of the {@link Role}s, suitable for display to end-users.
   **
   ** @return                    the collection of {@link Role}s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Role}.
   */
  public Collection<Role> role() {
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (Resource)
  /**
   ** Sets the identifier of the object.
   **
   ** @param  id                 the identifier of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>TenantResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  @Override
  @JsonSetter("id")
  public TenantResource id(final String id) {
    this.identifier = id;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (Resource)
  /**
   ** Returns the identifier of the object.
   **
   ** @return                    the identifier of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String id() {
    return this.identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalId (Resource)
  /**
   ** Sets the external identifier of the object.
   **
   ** @param  id                 the external identifier of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>TenantResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  @Override
  @JsonSetter("externalId")
  public TenantResource externalId(final String id) {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalId (Resource)
  /**
   ** Returns the external identifier of the object.
   **
   ** @return                    the external identifier of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String externalId() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace (Resource)
  /**
   ** Sets the schema namespaces for this object.
   ** <br>
   ** This set should contain all schema namespaces including the one for this
   ** object and all extensions.
   **
   ** @param  namespace          a set containing the schema namespaces for this
   **                            object.
   **                            <br>
   **                            Allowed object is {@link Collection} of
   **                            {@link String}.
   **
   ** @return                    the <code>TenantResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  @Override
  @JsonSetter("schemas")
  public TenantResource namespace(final Collection<String> namespace) {
    this.namespace = new HashSet<String>(namespace);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace (Resource)
  /**
   ** Returns the schema namespaces for this object.
   ** <br>
   ** This includes the one for the class that extends this class (taken from
   ** the annotation), as well as any that are present in the extensions.
   **
   ** @return                    a set containing the schema namespaces for this
   **                            object.
   **                            <br>
   **                            Possible object is {@link Collection} of
   **                            {@link String}.
   */
  @Override
  @JsonGetter("schemas")
  public Collection<String> namespace() {
    if(this.namespace == null)
      this.namespace = new HashSet<String>();

    return this.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata (Resource)
  /**
   ** Sets metadata for the object.
   **
   ** @param  metadata           the {@link Metadata} about the object.
   **                            <br>
   **                            Allowed object is {@link Metadata}.
   **
   ** @return                    the <code>Tenant</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  @Override
  @JsonSetter("meta")
  public final TenantResource metadata(final Metadata metadata) {
    this.metadata = metadata;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata (Resource)
  /**
   ** Returns metadata about the object.
   **
   ** @return                    the {@link Metadata} about the object.
   **                            <br>
   **                            Possible object is {@link Metadata}.
   */
  @Override
  @JsonGetter(Metadata.PREFIX)
  public final Metadata metadata() {
    return this.metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extension (Resource)
  /**
   ** Returns the {@link ObjectNode} that contains all extension attributes.
   **
   ** @return                    an {@link ObjectNode} providing access to all
   **                            extension attributes.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  @Override
  @JsonIgnore
  public ObjectNode extension() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Resource)
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
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generic (Resource)
  /**
   ** Returns the {@link GenericResource} representation of this
   ** <code>Resource</code>.
   ** <br>
   ** If this <code>Resource</code> is already a {@link GenericResource}, this
   ** same instance will be returned.
   **
   ** @return                    the {@link GenericResource} representation of
   **                            this <code>Resource</code>.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   */
  @Override
  public final GenericResource generic() {
    return new GenericResource(Support.valueToNode(this));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (Resource)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Tenant</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Tenant</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final TenantResource that = (TenantResource)other;
    return super.equals(other) && Objects.equals(this.identifier, that.identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>TenantResource</code> with the
   ** properties specified.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The administrative status of the instance created is always
   ** <code>true</code>.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>TenantResource</code> that is being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  displayName        the unique identifier of the
   **                            <code>TenantResource</code> that is being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a <code>TenantResource</code> populated with
   **                            the properties specified and the administrative
   **                            status <code>active=true</code>.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  public static TenantResource of(final String identifier, final String displayName) {
    return of(identifier, displayName, Boolean.TRUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>TenantResource</code> with the
   ** properties specified.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>TenantResource</code> that is being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  displayName        the unique identifier of the
   **                            <code>TenantResource</code> that is being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  active             the value indicating the administrative status
   **                            of the <code>TenantResource</code> that is
   **                            being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    a <code>TenantResource</code> populated with
   **                            the properties specified.
   **                            <br>
   **                            Possible object is <code>TenantResource</code>.
   */
  public static TenantResource of(final String identifier, final String displayName, final Boolean active) {
    return new TenantResource(identifier, displayName, active);
  }
}