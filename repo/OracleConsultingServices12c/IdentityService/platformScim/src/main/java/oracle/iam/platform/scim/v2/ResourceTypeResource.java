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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   ResourceTypeResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceTypeResource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.v2;

import java.util.Objects;
import java.util.Collection;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.platform.scim.ResourceContext;

import oracle.iam.platform.scim.schema.Entity;
import oracle.iam.platform.scim.schema.Resource;

import oracle.iam.platform.scim.annotation.Schema;
import oracle.iam.platform.scim.annotation.Attribute;

import static oracle.iam.platform.scim.annotation.Attribute.Returned;
import static oracle.iam.platform.scim.annotation.Attribute.Mutability;
import static oracle.iam.platform.scim.annotation.Attribute.Uniqueness;

////////////////////////////////////////////////////////////////////////////////
// final class ResourceTypeResource
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The "ResourceType" schema specifies the meta-data about a resource type.
 ** <br>
 ** Resource type resources are READ-ONLY and identified using the following
 ** schema URI:  <code>urn:ietf:params:scim:schemas:core:2.0:ResourceType</code>.
 ** Unlike other core resources, all attributes are REQUIRED unless otherwise
 ** specified.
 ** <br>
 ** The <code>id</code>attribute is not required for the resource type resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id=ResourceTypeResource.ID, name=ResourceContext.RESOURCE_TYPE_RESOURCE, description="SCIM 2.0 Resource Type Resource")
@JsonPropertyOrder({Resource.SCHEMA, Resource.IDENTIFIER, Resource.EXTERNAL})
public class ResourceTypeResource extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String          ID = "urn:ietf:params:scim:schemas:core:2.0:ResourceType";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("name")
  @Attribute(description="The resource type name.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final String                name;

  @JsonProperty("description")
  @Attribute(description="The resource type's human readable description.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final String                description;

  @JsonProperty("endpoint")
  @Attribute(description="The resource type's HTTP addressable endpoint relative to the Base URL; e.g., \"/Users\".", reference={"uri"}, required=true, caseExact=true, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final URI                   endpoint;

  @JsonProperty("schema")
  @Attribute(description="The resource types primary/base schema URI.", reference={"uri"}, required=true, caseExact=true, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final URI                   schema;

  @JsonProperty("schemaExtensions")
  @Attribute(description="A list of URIs of the resource type's schema extensions.", required=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, multiValueClass=Extension.class)
  private final Collection<Extension> extensions;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Extension
  // ~~~~~ ~~~~~~~~~
  /**
   ** This class holds information about schema extensions for resource types.
   ** <br>
   ** It contains a namespace urn and a boolean indicating if it's required or
   ** optional.
   */
  public static class Extension {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty("required")
    @Attribute(description="A Boolean value that specifies whether the schema extension is required for the resource type. If true, a resource of this type MUST include this schema extension and include any attributes declared as required in this schema extension. If false, a resource of this type MAY omit this schema extension", required=true, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT)
    private final boolean required;

    @JsonProperty("schema")
    @Attribute(description="The URI of a schema extension.", required=true, caseExact=true, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, reference={"uri"} )
    private final URI     schema;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Extension</code> with a constraint value.
     **
     ** @param  schema           the schema namespace urn for the extension.
     **                          <br>
     **                          Allowed object is {@link URI}.
     ** @param  required         <code>true</code> if this extension schema is
     **                          required or not.
     */
    @JsonCreator
    public Extension(@JsonProperty(value="schema", required=true) final URI schema, @JsonProperty(value="required", required=true) final boolean required) {
  	  // ensure inheritance
      super();

  	  // initialize instance attributes
      this.schema   = schema;
      this.required = required;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: schema
    /**
     ** Returns the extension schema's namespace urn.
     **
     ** @return                  the extension schema's namespace urn.
     **                          <br>
     **                          Possible object is {@link URI}.
     */
    public URI schema() {
      return this.schema;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: required
    /**
     ** Returns <code>true</code> if the schema is required for this schema
     ** extension (for a the resource type this schema extension is part of).
     **
     ** @return                  <code>true</code> if the schema is required for
     **                          this schema extension; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean required() {
      return this.required;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

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
      return Objects.hash(this.schema, this.required);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new SCIM <code>ResourceTypeResource</code>, and sets the
   ** namespace if the class extending this one is annotated.
   **
   ** @param  id                 the ID fo the SCIM <code>SchemaResource</code>.
   ** @param  name               the name of the resource type.
   ** @param  description        the description of the resource type.
   ** @param  endpoint           the endpoint for the resource type.
   ** @param  schema             the schema for the resource type.
   ** @param  extensions         a list of schema extensions for this resource
   */
  @JsonCreator
  public ResourceTypeResource(@JsonProperty(value=Resource.IDENTIFIER, required=true) final String id, @JsonProperty(value="name", required=true) final String name, final @JsonProperty(value="description") String description, @JsonProperty(value="endpoint", required=true) final URI endpoint, @JsonProperty(value="schema", required=true) final URI schema, @JsonProperty(value="schemaExtensions") final Collection<Extension> extensions) {
	  // ensure inheritance
    super(id);

    // initialize instance attributes
    this.name        = name;
    this.description = description;
    this.endpoint    = endpoint;
    this.schema      = schema;
    this.extensions  = extensions == null ? null : CollectionUtility.unmodifiableList(extensions);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the SCIM <code>ResourceTypeResource</code>.
   **
   ** @return                    the name of the SCIM
   **                            <code>ResourceTypeResource</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the SCIM <code>ResourceTypeResource</code>.
   **
   ** @return                    the description of the SCIM
   **                            <code>ResourceTypeResource</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the endpoint of the SCIM <code>ResourceTypeResource</code>.
   **
   ** @return                    the endpoint of the SCIM
   **                            <code>ResourceTypeResource</code>.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI endpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Returns the schema of the SCIM <code>ResourceTypeResource</code>.
   **
   ** @return                    the schema of the SCIM
   **                            <code>ResourceTypeResource</code>.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI schema() {
    return this.schema;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   extensions
  /**
   ** Returns the schema extensions of the SCIM
   ** <code>ResourceTypeResource</code>.
   **
   ** @return                    the schema extensions of the SCIM
   **                            <code>ResourceTypeResource</code>.
   **                            <br>
   **                            Possible object is {@link Collection} of
   **                            {@link Extension}s.
   */
  public final Collection<Extension> extensions() {
    return this.extensions;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.name, this.description, this.endpoint, this.schema, this.extensions);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>ResourceTypeResource</code>s filter are considered equal if and
   ** only if they represent the same properties. As a consequence, two given
   ** <code>ResourceTypeResource</code>s filter may be different even though they
   ** contain the same set of names with the same values, but in a different
   ** order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final ResourceTypeResource that = (ResourceTypeResource)other;
    // ensure inheritance
    return super.equals(that)
        && Objects.equals(this.name,        that.name)
        && Objects.equals(this.schema,      that.schema)
        && Objects.equals(this.endpoint,    that.endpoint)
        && Objects.equals(this.extensions,  that.extensions)
        && Objects.equals(this.description, that.description)
     ;
  }
}