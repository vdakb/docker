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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Structural.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Structural.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.schema.Attribute;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.connector.keycloak.marshal.AttributeSerializer;
import oracle.iam.identity.icf.connector.keycloak.marshal.AttributeDeserializer;

////////////////////////////////////////////////////////////////////////////////
// abstract class Entity
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** The structural REST entity.
 ** <p>
 ** <code>Entity</code> is used when the domain is known ahead of time. In that
 ** case a developer can derive a class from <code>Embed</code> and annotate
 ** the class. The class should be a Java bean. This will make it easier to work
 ** with the REST object since you will just have plain old getters and setters
 ** for core attributes.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Structural<T extends Structural> extends Entity<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The collection of attribute properties.
   */
  @JsonProperty("attributes")
  @JsonSerialize(using=AttributeSerializer.class)
  @JsonDeserialize(using=AttributeDeserializer.class)
  @Attribute(multiValueClass=Pair.class)
  protected List<Pair<String, String>> attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Structural</code> REST representation that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Structural() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets the collection of attributes belonging to the
   ** <code>Structural</code> entity.
   **
   ** @param  value              the collection of attributes belonging to the
   **                            <code>Structural</code> entity.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair} with type
   **                            {@link String} for the tag and {@link String}
   **                            as the value.
   **
   ** @return                    the <code>Structural</code> entity to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T attribute(final List<Pair<String, String>> value) {
    this.attribute = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the collection of attributes belonging to the <code>Entity</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of attributes belonging to the
   **                            <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Pair} with type
   **                            {@link String} for the tag and {@link String}
   **                            as the value.
   */
  public final List<Pair<String, String>> attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Adds an attribute to the collection of custom attributes of this
   ** structural entity.
   **
   ** @param  name               the unique name of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the named-value pair.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void attribute(final String name, final String value) {
    attribute(Pair.of(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Adds an attribute to the collection of custom attributes of this
   ** structural entity.
   **
   ** @param  value              the named-value pair to add.
   **                            <br>
   **                            Allowed object is {@link Pair} where the key
   **                            is of type  {@link String} and the value is of
   **                            type  {@link String}.
   */
  public final void attribute(final Pair<String, String> value) {
    if (this.attribute == null)
      this.attribute = new ArrayList<Pair<String, String>>();

    this.attribute.removeIf(attr -> StringUtility.equal(attr.getKey(), value.getKey()));
    this.attribute.add(value);
  }
}