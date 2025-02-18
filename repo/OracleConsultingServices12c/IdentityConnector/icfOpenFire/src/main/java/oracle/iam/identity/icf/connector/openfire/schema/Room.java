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

    File        :   Room.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Room.


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
// class Room
// ~~~~~ ~~~~~
/**
 ** The <code>Room</code> database resource in Openfire.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Marshaller.ROOM_NAME)
public class Room extends Entity<Room> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The alias name of the identifier attribute. */
  public static final String SID                = "serviceId";

  /** The alias name of the identifier attribute. */
  public static final String RID                = "roomId";

  /** The alias name of the name attribute. */
  public static final String NATURALNAME        = "naturalName";

  /** The alias name of the description attribute. */
  public static final String DESCRIPTION        = "description";

  /** The alias name of the subject attribute. */
  public static final String SUBJECT            = "subject";

  /** The alias name of the canChangeSubject attribute. */
  public static final String SUBJECT_CAN_CHANGE = "subjectcanChange";

  /** The alias name of the public room attribute. */
  public static final String PUBLIC_ROOM        = "publicRoom";

  /** The alias name of the members only attribute. */
  public static final String MEMBER_ONLY        = "memberOnly";

  /** The alias name of the can register attribute. */
  public static final String CAN_REGISTER       = "canRegister";

  /** The alias name of the can invite attribute. */
  public static final String CAN_INVITE         = "canInvite";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Collection of properties.
   ** <br>
   ** Property is a collection of named-value pair.
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
   ** Constructs an empty <code>Room</code> target resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Room() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Room</code> target resource with the values supplied
   ** by the given mapping
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  private Room(final Map<String, Object> data) {
    // ensure inheritance
    super(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sid
  /**
   ** Sets the service identifier of the <code>Room</code>.
   **
   ** @param  value              the service identifier of the
   **                            <code>Room</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room sid(final String value) {
    attribute(SID, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sid
  /**
   ** Returns the service identifier of the <code>Room</code>.
   **
   ** @return                    the service identifier of the
   **                            <code>Room</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String sid() {
    return attribute(SID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rid
  /**
   ** Sets the room identifier of the <code>Room</code>.
   **
   ** @param  value              the room identifier of the <code>Room</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room rid(final String value) {
    attribute(RID, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rid
  /**
   ** Returns the room identifier of the <code>Room</code>.
   **
   ** @return                    the room identifier of the <code>Room</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String rid() {
    return attribute(RID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the <code>Room</code>.
   **
   ** @param  value              the name of the <code>Room</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room name(final String value) {
    attribute(NAME, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Room</code>.
   **
   ** @return                    the name of the <code>Room</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return attribute(NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   naturalName
  /**
   ** Sets the natural name of the <code>Room</code>.
   **
   ** @param  value              the natural name of the <code>Room</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room naturalName(final String value) {
    attribute(NATURALNAME, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   naturalName
  /**
   ** Returns the natural name of the <code>Room</code>.
   **
   ** @return                    the natural name of the <code>Room</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String naturalName() {
    return attribute(NATURALNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the description of the <code>Room</code>.
   **
   ** @param  value              the description of the <code>Room</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room description(final String value) {
    attribute(DESCRIPTION, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the <code>Room</code>.
   **
   ** @return                    the description of the <code>Room</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return attribute(DESCRIPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subject
  /**
   ** Sets the subject of the <code>Room</code>.
   **
   ** @param  value              the subject of the <code>Room</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room subject(final String value) {
    attribute(SUBJECT, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subject
  /**
   ** Returns the subject of the <code>Room</code>.
   **
   ** @return                    the subject of the <code>Room</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String subject() {
    return attribute(SUBJECT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subjectCanChange
  /**
   ** Whether the subject of the <code>Room</code> can change.
   **
   ** @param  value              <code>true</code> if the subject of the
   **                            <code>Room</code> can change; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room subjectCanChange(final Boolean value) {
    attribute(SUBJECT_CAN_CHANGE, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subjectCanChange
  /**
   ** Whether the subject of the <code>Room</code> can change.
   **
   ** @return                    <code>true</code> if the subject of the
   **                            <code>Room</code> can change; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean subjectCanChange() {
    return booleanValue(SUBJECT_CAN_CHANGE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicRoom
  /**
   ** Whether the <code>Room</code> is public.
   **
   ** @param  value              <code>true</code> if the <code>Room</code> is
   **                            public; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room publicRoom(final Boolean value) {
    attribute(PUBLIC_ROOM, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicRoom
  /**
   ** Whether the <code>Room</code> is public.
   **
   ** @return                    <code>true</code> if the <code>Room</code> is
   **                            public; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean publicRoom() {
    return booleanValue(PUBLIC_ROOM);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOnly
  /**
   ** Whether the <code>Room</code> is accessible by members only.
   **
   ** @param  value              <code>true</code> if the <code>Room</code> is
   **                            accessible by members only; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room memberOnly(final Boolean value) {
    attribute(MEMBER_ONLY, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOnly
  /**
   ** Whether the <code>Room</code> is accessible by members only.
   **
   ** @return                    <code>true</code> if the <code>Room</code> is
   **                            accessible by members only; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean memberOnly() {
    return booleanValue(MEMBER_ONLY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canRegister
  /**
   ** Whether registration at the <code>Room</code> can be performed.
   **
   ** @param  value              <code>true</code> registration at the
   **                            <code>Room</code> can be performed; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room canRegister(final Boolean value) {
    attribute(CAN_REGISTER, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canRegister
  /**
   ** Whether registration at the <code>Room</code> can be performed.
   **
   ** @return                    <code>true</code> registration at the
   **                            <code>Room</code> can be performed; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean canRegister() {
    return booleanValue(CAN_REGISTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canInvite
  /**
   ** Whether invitation in the <code>Room</code> can be performed.
   **
   ** @param  value              <code>true</code> invitation in the
   **                            <code>Room</code> can be performed; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room canInvite(final Boolean value) {
    attribute(CAN_INVITE, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canInvite
  /**
   ** Whether invitations in the <code>Room</code> can be performed.
   **
   ** @return                    <code>true</code> invitations in the
   **                            <code>Room</code> can be performed; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean canInvite() {
    return booleanValue(CAN_INVITE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Sets the collection of properties belonging to the <code>Room</code>.
   **
   ** @param  value              the collection of properties.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Property}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public final Room property(final List<Property> value) {
    this.property = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the collection of properties belonging to the <code>Room</code>.
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
    return attribute(RID);
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
    return new Object[]{attribute(RID)};
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Room</code> instance.
   **
   ** @param  rid                the gid of the <code>Room</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Room</code> instance created.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public static Room build(final String rid) {
    return build().rid(rid);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Room</code> instance.
   **
   ** @return                    the <code>Room</code> instance created.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public static Room build() {
    return new Room();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Room</code> that populates its values
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
  public static Room build(final Map<String, Object> data) {
    return new Room(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Convenience method to add a {@link Entity.Property} to the poperties of
   ** this <code>Room</code>.
   ** <p>
   ** The same is achivable by implementing:
   ** <pre>
   **   Room.build().add(Entity.Property.build(&lt;code&gt;name&lt;/code&gt;, &lt;code&gt;value&lt;/code&gt;));
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
  public Room add(final String name, final String value) {
    return add(Entity.Property.build(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add a {@link Entity.Property} to the poperties of this <code>Room</code>.
   **
   ** @param  property           {@link Entity.Property} to the poperties of
   **                            this <code>Room</code>.
   **                            <br>
   **                            Allowed object is {@link Entity.Property}.
   **
   ** @return                    the <code>Room</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Room</code>.
   */
  public Room add(final Entity.Property property) {
    // lazy initialize collection if necessary
    if (this.property == null)
      this.property = new ArrayList<Entity.Property>();

    this.property.add(property);
    return this;
  }
}