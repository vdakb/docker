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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   SchemaResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.v1.schema;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.scim.schema.Entity;

import oracle.iam.system.simulation.scim.annotation.Schema;
import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class SchemaResource
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This represents a SCIM schema.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id="urn:scim:schemas:core:1.0", name="Schema", description="SCIM 1.0 Schema Resource")
public class SchemaResource extends Entity<SchemaResource> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("name")
  @Attribute(description="The schema's human readable name.", mutability=Definition.Mutability.READ_ONLY)
  private final String                 name;

  @JsonProperty("description")
  @Attribute(description="The schema's human readable description.", mutability=Definition.Mutability.READ_ONLY)
  private final String                 description;

  @JsonProperty("attributes")
  @Attribute(description="Attributes of the object described by this schema.", mutability=Definition.Mutability.READ_ONLY, multiValueClass=Definition.class)
  private final Collection<Definition> attribute;

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
  public SchemaResource(@JsonProperty(value="id", required=true) final String id, @JsonProperty(value="name") final String name, @JsonProperty(value="description") final String description, @JsonProperty(value="attributes", required=true) final Collection<Definition> attribute) {
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
   **                            {@link Definition}s.
   */
  public final Collection<Definition> attribute() {
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
    int result = super.hashCode();
    result = 31 * result + (this.name        != null ? this.name.hashCode()        : 0);
    result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
    result = 31 * result + (this.attribute   != null ? this.attribute.hashCode()   : 0);
    return result;
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

    // ensure inheritance
    if (!super.equals(other))
      return false;

    final SchemaResource that = (SchemaResource)other;
    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.description != null ? !this.description.equals(that.description) : that.description != null)
      return false;

     if (this.attribute != null ? !this.attribute.equals(that.attribute) : that.attribute != null)
       return false;

    return true;
  }
}