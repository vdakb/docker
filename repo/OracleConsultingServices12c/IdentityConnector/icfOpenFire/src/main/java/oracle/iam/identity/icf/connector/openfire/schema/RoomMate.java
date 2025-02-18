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

    File        :   RoomMate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoomMate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.schema;

import java.util.Map;

////////////////////////////////////////////////////////////////////////////////
// class RoomMate
// ~~~~~ ~~~~~~~~
/**
 ** The <code>RoomMate</code> database resource in Openfire.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoomMate extends Entity<RoomMate> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The alias name of the room identifier attribute. */
  public static final String RID       = "roomId";

  /** The alias name of the user name attribute. */
  public static final String JID       = "jid";

  /** The alias name of the nick name attribute. */
  public static final String NICKNAME  = "nickName";

  /** The alias name of the last name attribute. */
  public static final String LASTNAME  = "lastName";

  /** The alias name of the first name attribute. */
  public static final String FIRSTNAME = "firstName";

  /** The alias name of the e-Mail Address attribute. */
  public static final String EMAIL     = "email";

  /** The alias name of the URL attribute. */
  public static final String URL       = "url";

  /** The alias name of the FAQ attribute. */
  public static final String FAQ       = "faq";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RoomMate</code> target resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private RoomMate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoomMate</code> target resource with the values
   ** supplied by the given mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  private RoomMate(final Map<String, Object> data) {
    // ensure inheritance
    super(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rid
  /**
   ** Sets the rid of the <code>RoomMate</code>.
   **
   ** @param  value              the rid of the <code>RoomMate</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RoomMate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public final RoomMate rid(final String value) {
    attribute(RID, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rid
  /**
   ** Returns the rid of the <code>RoomMate</code>.
   **
   ** @return                    the rid of the <code>RoomMate</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String rid() {
    return attribute(RID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jid
  /**
   ** Sets the jid of the <code>RoomMate</code>.
   **
   ** @param  value              the jid of the <code>RoomMate</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RoomMate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public final RoomMate jid(final String value) {
    attribute(JID, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jid
  /**
   ** Returns the jid of the <code>RoomMate</code>.
   **
   ** @return                    the jid of the <code>RoomMate</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String jid() {
    return attribute(JID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nickName
  /**
   ** Sets the nick name of the <code>RoomMate</code>.
   **
   ** @param  value            the nick name of the <code>RoomMate</code>.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the <code>RoomMate</code> to allow method
   **                          chaining.
   **                          <br>
   **                          Possible object is <code>RoomMate</code>.
   */
  public final RoomMate nickName(final String value) {
    attribute(NICKNAME, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    nickName
  /**
   ** Returns the nick name of the <code>RoomMate</code>.
   **
   ** @return                  the nick name of the <code>RoomMate</code>.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String nickName() {
    return attribute(NICKNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Sets the last name of the <code>RoomMate</code>.
   **
   ** @param  value            the last name of the <code>RoomMate</code>.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the <code>RoomMate</code> to allow method
   **                          chaining.
   **                          <br>
   **                          Possible object is <code>RoomMate</code>.
   */
  public final RoomMate lastName(final String value) {
    attribute(LASTNAME, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    lastName
  /**
   ** Returns the last name of the <code>RoomMate</code>.
   **
   ** @return                  the last name of the <code>RoomMate</code>.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String lastName() {
    return attribute(LASTNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Sets the first name of the <code>RoomMate</code>.
   **
   ** @param  value            the first name of the <code>RoomMate</code>.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the <code>RoomMate</code> to allow method
   **                          chaining.
   **                          <br>
   **                          Possible object is <code>RoomMate</code>.
   */
  public final RoomMate firstName(final String value) {
    attribute(FIRSTNAME, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    firstName
  /**
   ** Returns the first name of the <code>RoomMate</code>.
   **
   ** @return                  the first name of the <code>RoomMate</code>.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String firstName() {
    return attribute(FIRSTNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Sets the email of the <code>RoomMate</code>.
   **
   ** @param  value              the email of the <code>RoomMate</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RoomMate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public final RoomMate email(final String value) {
    attribute(EMAIL, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Returns the email of the <code>RoomMate</code>.
   **
   ** @return                    the email of the <code>RoomMate</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String email() {
    return attribute(EMAIL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Sets the url of the <code>RoomMate</code>.
   **
   ** @param  value              the url of the <code>RoomMate</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RoomMate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public final RoomMate url(final String value) {
    attribute(URL, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Returns the url of the <code>RoomMate</code>.
   **
   ** @return                    the url of the <code>RoomMate</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String url() {
    return attribute(URL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   faq
  /**
   ** Sets the faq of the <code>RoomMate</code>.
   **
   ** @param  value              the faq of the <code>RoomMate</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RoomMate</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public final RoomMate faq(final String value) {
    attribute(FAQ, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   faq
  /**
   ** Returns the faq of the <code>RoomMate</code>.
   **
   ** @return                    the faq of the <code>RoomMate</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String faq() {
    return attribute(FAQ);
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
    return new Object[]{attribute(JID)};
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
    return rid() + "#" + jid();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>RoomMate</code> instance.
   **
   ** @param  rid                the uid of the <code>RoomMate</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  jid                the gid of the <code>RoomMate</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RoomMate</code> instance created.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public static RoomMate build(final String rid, final String jid) {
    return build().rid(rid).jid(jid);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>RoomMate</code> instance from the
   ** specified {@link User}.
   **
   ** @param  entity             the {@link User} entity to populate the
   **                            required values from.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the <code>RoomMate</code> instance created.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public static RoomMate build(final User entity) {
    return build().jid(entity.uid());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>RoomMate</code> instance from the
   ** specified {@link Room}.
   **
   ** @param  entity             the {@link Room} entity to populate the
   **                            required values from.
   **                            <br>
   **                            Allowed object is {@link Room}.
   **
   ** @return                    the <code>RoomMate</code> instance created.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public static RoomMate build(final Room entity) {
    return build().rid(entity.rid());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>RoomMate</code> instance.
   **
   ** @return                    the <code>RoomMate</code> instance created.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public static RoomMate build() {
    return new RoomMate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RoomMate</code> that populates its
   ** values from the given attribute mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the <code>RoomMate</code> instance populated.
   **                            <br>
   **                            Possible object is <code>RoomMate</code>.
   */
  public static RoomMate build(final Map<String, Object> data) {
    return new RoomMate(data);
  }
}