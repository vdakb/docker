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

    File        :   SchemaResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaResource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.v2;

import java.util.Objects;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.AttributeDefinition;

import oracle.iam.platform.scim.schema.Entity;
import oracle.iam.platform.scim.schema.Resource;

import oracle.iam.platform.scim.annotation.Schema;
import oracle.iam.platform.scim.annotation.Attribute;

////////////////////////////////////////////////////////////////////////////////
// final class SchemaResource
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This represents a SCIM schema.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id=SchemaResource.ID, name=ResourceContext.RESOURCE_TYPE_SCHEMA, description="SCIM 2.0 Schema Resource")
public final class SchemaResource extends Entity<SchemaResource> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String                    ID = "urn:ietf:params:scim:schemas:core:2.0:Schema";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("name")
  @Attribute(description="The schema's human readable name.", mutability=Attribute.Mutability.READ_ONLY)
  private final String                          name;

  @JsonProperty("description")
  @Attribute(description="The schema's human readable description.", mutability=Attribute.Mutability.READ_ONLY)
  private final String                          description;

  @JsonProperty("attributes")
  @Attribute(description="Attributes of the object described by this schema.", mutability=Attribute.Mutability.READ_ONLY, multiValueClass=Attribute.class)
  private final Collection<AttributeDefinition> attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new SCIM <code>SchemaResource</code>, and sets the namespace
   ** if the class extending this one is annotated.
   **
   ** @param  id                 the ID fo the SCIM <code>SchemaResource</code>.
   ** @param  name               the schema's display name.
   ** @param  description        the schema's human readable description.
   ** @param  attribute          the schema's attributes.
   */
  @JsonCreator
  public SchemaResource(@JsonProperty(value=Resource.IDENTIFIER, required=true) final String id, @JsonProperty(value="name") final String name, @JsonProperty(value="description") final String description, @JsonProperty(value="attributes", required=true) final Collection<AttributeDefinition> attribute) {
    // ensure inheritance
    super(id);

    // initialize instance attributes
    this.name        = name;
    this.description = description;
    this.attribute   = CollectionUtility.unmodifiableList(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the SCIM <code>SchemaResource</code>.
   **
   ** @return                    the name of the SCIM
   **                            <code>SchemaResource</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the SCIM <code>SchemaResource</code>.
   **
   ** @return                    the description of the SCIM
   **                            <code>SchemaResource</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the attributes of the SCIM <code>SchemaResource</code>.
   **
   ** @return                    the attributes of the SCIM
   **                            <code>SchemaResource</code>.
   **                            <br>
   **                            Possible object {@link Collection} of
   **                            {@link AttributeDefinition}s.
   */
  public final Collection<AttributeDefinition> attribute() {
    return this.attribute;
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
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.name, this.description, this.attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>SchemaResource</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>SchemaResource</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
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

    final SchemaResource that = (SchemaResource)other;
    // ensure inheritance
    return super.equals(that)
        && Objects.equals(this.name,        that.name)
        && Objects.equals(this.description, that.description)
        && Objects.equals(this.attribute,   that.attribute)
    ;
  }
}