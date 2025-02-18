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

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


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
// class User
// ~~~~~ ~~~~
/**
 ** The <code>User</code> database resource in Openfire.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public class User extends Entity<User> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The alias name of the email attribute */
  public static final String EMAIL              = "email";

  /** The alias name of the status attribute */
  public static final String LOCKED             = "locked";

  /** The alias name of the admin flag attribute */
  public static final String ADMIN              = "administrator";

  /** The alias name of the encrypted password attribute. */
  public static final String PASSWORD_ENCRYPTED = "encryptedPassword";

  /** The alias name of the plain text password attribute. */
  public static final String PASSWORD_PLAINTEXT = "plainPassword";

  /** The alias name of the stored attribute. */
  public static final String STOREDKEY          = "storedKey";

  /** The alias name of the serverkey attribute. */
  public static final String SERVERKEY          = "serverKey";

  /** The alias name of the iterations attribute. */
  public static final String ITERATION          = "iteration";

  /** The alias name of the salt attribute. */
  public static final String SALT               = "salt";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Collection of flags.
   ** <br>
   ** Property is a key-value pair.
   ** <br>
   ** The key must to be per user unique.
   */
  protected List<Flag>          flag;

  /**
   ** Collection of properties.
   ** <br>
   ** Property is a collection of named-value pairs.
   ** <br>
   ** The key must to be per user unique.
   */
  protected List<Property> property;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Flag
  // ~~~~~ ~~~~
  /**
   ** The <code>Flag</code> belonging to an <code>Entity</code>.
   */
  public static class Flag extends Entity<Flag> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    /////////////////////////////////////////////////////////////////////////////

    /** The alias name of the lockout status. */
    public static final String LOCK  = "lockout";

    /** The alias name of the start time attribute. */
    public static final String START = "startTime";

    /** The alias name of the end time attribute. */
    public static final String END   = "endTime";

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Flag</code> for the specified key-value pair.
     **
     ** @param  name             the name of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  start            the start time value of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  end              the end time value of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Flag(final String name, final String start, final String end) {
      // ensure inheritance
      this(name);

      // initialize instance attributes by converting the timestamp for the
      // period of validity 
      attribute(START, start);
      attribute(END,   end);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Flag</code> for the specified key-value pair.
     **
     ** @param  name             the name of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Flag(final String name) {
      // ensure inheritance
      super();

      // initialize instance attributes
      attribute(NAME,  name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Flag</code> target resource with the values supplied
     ** by the given mapping
     **
     ** @param  data             the mapping to be stored in the attribute map.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Object} as the value.
     */
    private Flag(final Map<String, Object> data) {
      // ensure inheritance
      super(data);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name of the <code>Flag</code>.
     **
     ** @param  value            the name of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Flag</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Flag</code>.
     */
    public final Flag name(final String value) {
      attribute(NAME,  value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:  name
    /**
     ** Returns the name of the <code>Flag</code>.
     **
     ** @return                  the name of the <code>Flag</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return attribute(NAME);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: start
    /**
     ** Sets the start time of the <code>Flag</code>.
     **
     ** @param  value            the start time of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Flag</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Flag</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final Flag start(final String value) {
      attribute(START,  value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:  start
    /**
     ** Returns the start time of the <code>Flag</code>.
     **
     ** @return                  the start time of the <code>Flag</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String start() {
      return attribute(START);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: end
    /**
     ** Sets the end time of the <code>Flag</code>.
     **
     ** @param  value            the end time of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Flag</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Flag</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final Flag end(final String value) {
      attribute(END,  value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:  end
    /**
     ** Returns the end time of the <code>Flag</code>.
     **
     ** @return                  the end time of the <code>Flag</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String end() {
      return attribute(END);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (Resource)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return name();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: primary (Entity)
    /**
     ** Returns the value array of the uniquely identifying attribute.
     ** <br>
     ** Such a value is mainly used in join operations with other entities.
     **
     ** @return                  the value array of the uniquely identifying
     **                          attribute.
     **                          <br>
     **                          Possible object is array of {@link Object}.
     */
    @Override
    public final Object[] primary() {
      return new Object[]{attribute(NAME)};
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a <code>Flag</code> that populates its values
     ** from the given properties.
     **
     ** @param  name             the name of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  start            the start time value of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  end              the end time value of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Flag</code> instance populated.
     **                          <br>
     **                          Possible object is <code>Date</code>.
     */
    public static Flag build(final String name, final String start, final String end) {
      return new Flag(name, start, end);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a <code>Flag</code> for thr givrn name.
     **
     ** @param  name             the name of the <code>Flag</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Flag</code> instance populated.
     **                          <br>
     **                          Possible object is <code>Date</code>.
     */
    public static Flag build(final String name) {
      return new Flag(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a <code>Flag</code> that populates its values
     ** from the given attribute mapping.
     **
     ** @param  data             the mapping to be stored in the attribute map.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Object} as the value.
     **
     ** @return                  the <code>Flag</code> instance populated.
     **                          <br>
     **                          Possible object is <code>Flag</code>.
     */
    public static Flag build(final Map<String, Object> data) {
      return new Flag(data);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>User</code> target resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private User() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>User</code> target resource with the values supplied
   ** by the given mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  private User(final Map<String, Object> data) {
    // ensure inheritance
    super(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Sets the uid of the <code>User</code>.
   **
   ** @param  value              the uid of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User uid(final String value) {
    attribute(UID, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Returns the uid of the <code>User</code>.
   **
   ** @return                    the uid of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String uid() {
    return attribute(UID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordEncrypted
  /**
   ** Sets the encrypted password of the <code>User</code>.
   **
   ** @param  value              the encrypted password of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User passwordEncrypted(final String value) {
    attribute(PASSWORD_ENCRYPTED, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordEncrypted
  /**
   ** Returns the encrypted password of the <code>User</code>.
   **
   ** @return                    the encrypted password of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String passwordEncrypted() {
    return attribute(PASSWORD_ENCRYPTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordPlaintext
  /**
   ** Sets the plain text password of the <code>User</code>.
   **
   ** @param  value              the encrypted password of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User passwordPlaintext(final String value) {
    attribute(PASSWORD_PLAINTEXT, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordPlaintext
  /**
   ** Returns the plain text password of the <code>User</code>.
   **
   ** @return                    the plain text password of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String passwordPlaintext() {
    return attribute(PASSWORD_PLAINTEXT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the display name of the <code>User</code>.
   **
   ** @param  value            the display name of the <code>User</code>.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the <code>User</code> to allow method
   **                          chaining.
   **                          <br>
   **                          Possible object is <code>User</code>.
   */
  public final User name(final String value) {
    attribute(NAME, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    name
  /**
   ** Returns the display name of the <code>User</code>.
   **
   ** @return                  the display name of the <code>User</code>.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String name() {
    return attribute(NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Sets the email of the <code>User</code>.
   **
   ** @param  value              the email of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User email(final String value) {
    attribute(EMAIL, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Returns the email of the <code>User</code>.
   **
   ** @return                    the email of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String email() {
    return attribute(EMAIL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locked
  /**
   ** Sets the locked flag of the <code>User</code>.
   ** <p>
   ** Locking a user is a special operation in openfire due to its maintained by
   ** a flag in a specific database table.
   ** <br>
   ** The flag exists only once at a certain time or is vanished. if the flag
   ** exists and the end date of the flag is either <code>null</code> or in the
   ** future the user account is locked
   **
   ** @param  value              <code>true</code> if the <code>User</code>
   **                            account is locked; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User locked(final Boolean value) {
    attribute(LOCKED, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locked
  /**
   ** Wheter the <code>User</code> is locked.
   **
   ** @return                    <code>true</code> if the <code>User</code>
   **                            account is locked; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean locked() {
    return booleanValue(LOCKED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   administrator
  /**
   ** Sets the administrator flag of the <code>User</code>.
   **
   ** @param  value              the administrator flag of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User administrator(final Boolean value) {
    attribute(ADMIN, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   administrator
  /**
   ** Returns the administrator flag of the <code>User</code>.
   **
   ** @return                    the administrator flag of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean administrator() {
    return booleanValue(ADMIN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Sets the collection of properties the <code>User</code> belongs to.
   **
   ** @param  value              the collection of properties.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Property}.
   **
   ** @return                    the <code>Room</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User property(final List<Property> value) {
    this.property = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the collection of properties belonging to the <code>User</code>.
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
  // Method:   flag
  /**
   ** Sets the {@link List} of {@link Flag}s the <code>User</code> belongs to.
   **
   ** @param  value              the {@link List} of {@link Flag}s.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Flag}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User flag(final List<Flag> value) {
    this.flag = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flag
  /**
   ** Returns the {@link List} of {@link Flag}s the <code>User</code> belongs
   ** to.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link List} of {@link Flag}s.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Flag}.
   */
  public final List<Flag> flag() {
    return this.flag;
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
    return attribute(UID);
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
    return new Object[]{attribute(UID)};
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>User</code> instance.
   **
   ** @param  uid                the uid of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> instance created.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public static User build(final String uid) {
    return build().uid(uid);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>User</code> instance.
   **
   ** @return                    the <code>User</code> instance created.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public static User build() {
    return new User();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>User</code> that populates its values
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
  public static User build(final Map<String, Object> data) {
    return new User(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Convenience method to add a {@link Entity.Property} to the poperties of
   ** this <code>User</code>.
   ** <p>
   ** The same is achivable by implementing:
   ** <pre>
   **   User.build().add(Entity.Property.build(&lt;code&gt;name&lt;/code&gt;, &lt;code&gt;value&lt;/code&gt;));
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
  public User add(final String name, final String value) {
    return add(Entity.Property.build(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add a {@link Entity.Property} to the poperties of this <code>User</code>.
   **
   ** @param  property           {@link Entity.Property} to the poperties of
   **                            this <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Entity.Property}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User add(final Entity.Property property) {
    // lazy initialize collection if necessary
    if (this.property == null)
      this.property = new ArrayList<Entity.Property>();

    this.property.add(property);
    return this;
  }
}