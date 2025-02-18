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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   Group.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Group.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.schema;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import oracle.iam.identity.icf.schema.Schema;

////////////////////////////////////////////////////////////////////////////////
// class Group
// ~~~~~ ~~~~~
/**
 ** The <code>Group</code> database resource in Openfire.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.GROUP)
public class Group extends Entity<Group> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The alias name of the identifier attribute. */
  public static final String GID         = "groupName";

  /** The alias name of the description attribute. */
  public static final String DESCRIPTION = "description";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Collection of properties.
   ** <br>
   ** Property is a collection of named-value pairs.
   ** <br>
   ** The key must to be per user unique.
   */
  protected List<Property> property;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Group</code> target resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Group() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Group</code> target resource with the values supplied
   ** by the given mapping
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  private Group(final Map<String, Object> data) {
    // ensure inheritance
    super(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gid
  /**
   ** Sets the gid of the <code>Group</code>.
   **
   ** @param  value              the gid of the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group gid(final String value) {
    attribute(GID, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gid
  /**
   ** Returns the gid of the <code>Group</code>.
   **
   ** @return                    the gid of the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String gid() {
    return attribute(GID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the description of the <code>Group</code>.
   **
   ** @param  value              the description of the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group description(final String value) {
    attribute(DESCRIPTION, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the <code>Group</code>.
   **
   ** @return                    the description of the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return attribute(DESCRIPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Sets the collection of properties the <code>Group</code> belongs to.
   **
   ** @param  value              the collection of properties.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Property}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group property(final List<Property> value) {
    this.property = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the collection of properties belonging to the <code>Group</code>.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the this object.
   **
   ** @return                    the the collection of properties.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Property}.
   */
  public final List<Property> property() {
    return this.property;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Resource)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                      the string representation for this instance in
   **                              its minimal form.
   **                              <br>
   **                              Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return attribute(GID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary (Entity)
  /**
   ** Returns the value array of the uniquely identifying attributes.
   ** <br>
   ** Such a value is mainly used in join operations with other entities.
   **
   ** @return                    the value array of the uniquely identifying
   **                            attribute.
   **                            <br>
   **                            Possible object is array of {@link Object}.
   */
  @Override
  public final Object[] primary() {
    return new Object[]{attribute(GID)};
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Group</code> instance.
   **
   ** @param  gid                the gid of the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> instance created.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public static Group build(final String gid) {
    return build().gid(gid);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Group</code> instance.
   **
   ** @return                    the <code>Group</code> instance created.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public static Group build() {
    return new Group();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Group</code> that populates its values
   ** from the given attribute mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the <code>User</code> instance populated.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public static Group build(final Map<String, Object> data) {
    return new Group(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Convenience method to add a {@link Entity.Property} to the poperties of
   ** this <code>Group</code>.
   ** <p>
   ** The same is achivable by implementing:
   ** <pre>
   **   Group.build().add(Entity.Property.build(&lt;code&gt;name&lt;/code&gt;, &lt;code&gt;value&lt;/code&gt;));
   ** </pre>
   **
   ** @param  name               the name of the <code>Property</code> to add.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the initial value of the <code>Property</code>
   **                            to add.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public Group add(final String name, final String value) {
    return add(Entity.Property.build(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add a {@link Entity.Property} to the poperties of this <code>Group</code>.
   **
   ** @param  property           {@link Entity.Property} to the poperties of
   **                            this <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link Entity.Property}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public Group add(final Entity.Property property) {
    // lazy initialize collection if necessary
    if (this.property == null)
      this.property = new ArrayList<Entity.Property>();

    this.property.add(property);
    return this;
  }
}