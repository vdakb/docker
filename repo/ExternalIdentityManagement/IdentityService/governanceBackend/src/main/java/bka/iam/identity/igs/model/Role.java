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

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Backend

    File        :   Role.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Role.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.model;

import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import oracle.hst.platform.jpa.converter.Boolean2Integer;

import bka.iam.identity.jpa.provider.Base;

////////////////////////////////////////////////////////////////////////////////
// class Role
// ~~~~~ ~~~~
/**
 ** The <code>Role</code> database resources.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** <i>CascadeType.REMOVE</i> applied at the collection of {@link User}s is the
 ** way to delete or child entity or entities whenever the deletion of the
 ** <code>Role</code> itself happens. More important is to annotate the
 ** relation with <i>orphanRemoval=true</i> to delete orphaned entities from the
 ** persistence layer. An entity that is no longer attached to its parent is the
 ** definition of being an orphan.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** The counterpart of the relation declared in {@link UserRole} must not be
 ** annotated with any <i>CascadeType</i> or <i>orphanRemoval</i>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name=Role.NAME)
@Table(name=Role.ENTITY)
public class Role extends Base<String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity */
  public static final String NAME             = "Role";

  /** The name of the entity */
  public static final String ENTITY           = "igt_roles";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6573965107748804658")
  private static final long  serialVersionUID = 1894944966188781913L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Id
  @Column(name=ID, nullable=false, unique=true, updatable=false, length=30)
  private String           id;

  @Convert(converter=Boolean2Integer.class)
  @Column(name=ACTIVE, nullable=false)
  protected Boolean        active;

  @Column(name="display_name", unique=true, nullable=false, length=128)
  protected String         displayName;

  @Column(name="description", length=512)
  protected String         description;
  
  @OneToMany(mappedBy="role", fetch=FetchType.LAZY, orphanRemoval=true, cascade={CascadeType.REMOVE, CascadeType.PERSIST})
  protected List<UserRole> user;

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
      /** The name of the identifying attribute. */
      ID("id")
      /** The visibilty status attribute. */
    , ACTIVE("active")
      /** The human readable naming attribute. */
    , DISPLAYNAME("displayName")
      /** The human readable description attribute. */
    , DESCRIPTION("description")
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
     **
     ** @param  id               the value representation of the
     **                          <code>Attribute</code> identifier.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Attribute(final String id) {
      // initailize instance attributes
      this.id = id;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Role</code> database resource that allows use as
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
  public Role() {
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
   **                            Allowed object is {@link String}.
   */
  private Role(final String id) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId
  /**
   ** Sets the <code>id</code> property of the <code>Role</code>.
   **
   ** @param  value              the <code>id</code> property of the
   **                            <code>Role</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setId(final String value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
   ** Returns the <code>id</code> property of the <code>Role</code>.
   **
   ** @return                    the <code>id</code> property of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getId() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setActive
  /**
   ** Sets the <code>active</code> property of the <code>Role</code> entity.
   **
   ** @param  value              the <code>active</code> property of the
   **                            <code>Role</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public final void setActive(final Boolean value) {
    this.active = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActive
  /**
   ** Returns the <code>active</code> property of the <code>Role</code> entity.
   **
   ** @return                    the <code>active</code> property of the
   **                            <code>Role</code> entity.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean getActive() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayName
  /**
   ** Sets the <code>displayName</code> property of the <code>Role</code>.
   **
   ** @param  value              the <code>displayName</code> property of the
   **                            <code>Role</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setDisplayName(final String value) {
    this.displayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayName
  /**
   ** Returns the <code>displayName</code> property of the <code>Role</code>.
   **
   ** @return                    the <code>displayName</code> property of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getDisplayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the <code>description</code> property of the <code>Role</code>.
   **
   ** @param  value              the <code>description</code> property of the
   **                            <code>Role</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the <code>description</code> property of the
   ** <code>Role</code>.
   **
   ** @return                    the <code>description</code> property of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUser
  /**
   ** Sets the user role set.
   **
   ** @param  value              the user result set.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link UserRole}.
   */
  public final void setUser(final List<UserRole> value) {
    this.user = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUser
  /**
   ** Returns the user role set.
   ** <p>
   ** This accessor method returns a reference to the live {@link List}, not a
   ** snapshot. Therefore any modification you make to the returned list will be
   ** present inside the JPA entity.
   **
   ** @return                    the user result set.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link UserRole}.
   */
  public final List<UserRole> getUser() {
    return this.user;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>Role</code> database resource.
   **
   ** @param  id                 the primary identifier of the <code>Role</code>
   **                            database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> populated with the given
   **                            <code>id</code>.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public static Role build(final String id) {
    return new Role(id);
  }
}