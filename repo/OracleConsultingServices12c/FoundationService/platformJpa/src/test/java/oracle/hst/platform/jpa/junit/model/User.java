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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   JPA Unit Testing

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.junit.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import oracle.hst.platform.jpa.converter.Boolean2Integer;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
/**
 ** The <code>User</code> database resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name=User.NAME)
@Table(name=User.ENTITY)
@SuppressWarnings({ "oracle.jdeveloper.java.annotation-callback", "oracle.jdeveloper.ejb.entity-class-auto-id-gen" })
public class User implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity */
  public static final String NAME             = "User";

  /** The name of the entity */
  public static final String ENTITY           = "igt_users";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2162933026777271383")
  private static final long serialVersionUID = 3032127227485592468L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Id
  @GeneratedValue(generator="uid")
  @SequenceGenerator(name="uid", sequenceName="igs_adm_seq", allocationSize=1)
  @Column(name="id", nullable=false, unique=true, updatable=false)
  private Long             id;

  @Convert(converter=Boolean2Integer.class)
  @Column(name="active", nullable=false)
  protected Boolean        active;

  @Column(name="username", unique=true, nullable=false, length=32)
  protected String         userName;

  @Column(name="credential", nullable=false, length=64)
  protected String         credential;

  @Column(name="lastname", nullable=false, length=128)
  protected String         lastName;

  @Column(name="firstname", length=128)
  protected String         firstName;

  @Column(name="language", nullable=false, length=2)
  protected String         language;

  @Column(name="email", unique=true, nullable=false, length=512)
  protected String         email;

  @Column(name="mobile", length=64)
  protected String         mobile;

  @Column(name="phone", length=64)
  protected String         phone;

  protected Metadata       metadata;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** Attribute descriptor of the database entity.
   */
  public enum Attribute {
      /** The name of the identifying attribute */
      ID("id"),

    /** The visibilty status attribute */
    ACTIVE("active"),

    /** The login name of a user */
    USERNAME("userName"),

    /** The last name of a user */
    LASTNAME("lastName"),

    /** The first name of a user */
    FIRSTNAME("firstName"),

    /** The preferred language of a user */
    LANGUAGE("preferredLanguage"),

    /** The e-mail Address of a user */
    EMAIL("mail"),

    /** The mobile phone number of a user */
    MOBIL("mobile"),

    /** The telephone number of a user */
    PHONE("telephoneNumber")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String  id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Attribute(final String id) {
      // initailize instance attributes
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Attribute</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Attribute</code>
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Attribute</code> mapped at
     **                          <code>id</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Attribute</code>.
     */
    public static Attribute from(final String id) {
      for (Attribute cursor : Attribute.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>User</code> database resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  public User() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>User</code> database resource.
   **
   ** @param  id                 the primary identifier of the <code>User</code>
   **                            database resource.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  private User(final Long id) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>User</code> database resource.
   **
   ** @param  userName           the unique identifier of the <code>User</code>
   **                            database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private User(final String userName) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.userName = userName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId
  /**
   ** Sets the <code>id</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>id</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public final void setId(final Long value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
   ** Returns the <code>id</code> property of the <code>User</code>.
   **
   ** @return                    the <code>id</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long getId() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setActive
  /**
   ** Sets the <code>active</code> property of the <code>User</code> entity.
   **
   ** @param  value              the <code>active</code> property of the
   **                            <code>User</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public final void setActive(final Boolean value) {
    this.active = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActive
  /**
   ** Returns the <code>active</code> property of the <code>User</code> entity.
   **
   ** @return                    the <code>active</code> property of the
   **                            <code>User</code> entity.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean getActive() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserName
  /**
   ** Sets the <code>userName</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>userName</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setUserName(final String value) {
    this.userName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserName
  /**
   ** Returns the <code>userName</code> property of the <code>User</code>.
   **
   ** @return                    the <code>userName</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getUserName() {
    return this.userName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCredential
  /**
   ** Sets the <code>credential</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>credential</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setCredential(final String value) {
    this.credential = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCredential
  /**
   ** Returns the <code>credential</code> property of the <code>User</code>.
   **
   ** @return                    the <code>credential</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getCredential() {
    return this.credential;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLastName
  /**
   ** Sets the <code>lastName</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>lastName</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setLastName(final String value) {
    this.lastName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLastName
  /**
   ** Returns the <code>lastName</code> property of the <code>User</code>.
   **
   ** @return                    the <code>lastName</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getLastName() {
    return this.lastName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFirstName
  /**
   ** Sets the <code>firstName</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>firstName</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setFirstName(final String value) {
    this.firstName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFirstName
  /**
   ** Returns the <code>firstName</code> property of the <code>User</code>.
   **
   ** @return                    the <code>firstName</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getFirstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLanguage
  /**
   ** Sets the <code>language</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>language</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setLanguage(final String value) {
    this.language = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLanguage
  /**
   ** Returns the <code>language</code> property of the <code>User</code>.
   **
   ** @return                    the <code>language</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getLanguage() {
    return this.language;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEmail
  /**
   ** Sets the <code>email</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>email</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setEmail(final String value) {
    this.email = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEmail
  /**
   ** Returns the <code>email</code> property of the <code>User</code>.
   **
   ** @return                    the <code>email</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getEmail() {
    return this.email;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPhone
  /**
   ** Sets the <code>phone</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>phone</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setPhone(final String value) {
    this.phone = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPhone
  /**
   ** Returns the <code>phone</code> property of the <code>User</code>.
   **
   ** @return                    the <code>phone</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getPhone() {
    return this.phone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMobile
  /**
   ** Sets the <code>mobile</code> property of the <code>User</code>.
   **
   ** @param  value              the <code>mobile</code> property of the
   **                            <code>User</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setMobile(final String value) {
    this.mobile = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMobile
  /**
   ** Returns the <code>mobile</code> property of the <code>User</code>.
   **
   ** @return                    the <code>mobile</code> property of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getMobile() {
    return this.mobile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>User</code> database resource.
   **
   ** @param  id                 the primary identifier of the <code>User</code>
   **                            database resource.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>User</code> populated with the given
   **                            <code>id</code>.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public static User build(final Long id) {
    return new User(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>User</code> database resource.
   **
   ** @param  id                 the unique identifier of the <code>User</code>
   **                            database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> populated with the given
   **                            <code>id</code>.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public static User build(final String id) {
    return new User(id);
  }
}