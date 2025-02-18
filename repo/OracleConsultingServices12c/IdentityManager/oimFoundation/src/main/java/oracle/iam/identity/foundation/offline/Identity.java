/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   Identity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Identity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;

import oracle.hst.foundation.collection.Filter;
import oracle.hst.foundation.collection.FilteringIterator;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class Identity
// ~~~~~ ~~~~~~~~
/**
 ** An <code>Identity</code> aggregates all attributes and permissions an
 ** identity should be provisoned to and/or removed from.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 **  &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="identity" maxOccurs="unbounded" minOccurs="0"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;extension base="{http://www.oracle.com/schema/oim/offline}entity"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="roles" minOccurs="0"&gt;
 **                      &lt;complexType&gt;
 **                       &lt;complexContent&gt;
 **                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                           &lt;sequence&gt;
 **                             &lt;element name="role" type="{http://www.oracle.com/schema/oim/offline}entitlement" maxOccurs="unbounded" minOccurs="0"/&gt;
 **                           &lt;/sequence&gt;
 **                         &lt;/restriction&gt;
 **                       &lt;/complexContent&gt;
 **                     &lt;/complexType&gt;
 **                   &lt;/element&gt;
 **                   &lt;element name="applications" minOccurs="0"&gt;
 **                     &lt;complexType&gt;
 **                       &lt;complexContent&gt;
 **                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                           &lt;sequence&gt;
 **                             &lt;element name="application" maxOccurs="unbounded" minOccurs="0"&gt;
 **                               &lt;complexType&gt;
 **                                 &lt;complexContent&gt;
 **                                   &lt;extension base="{http://www.oracle.com/schema/oim/offline}entity"&gt;
 **                                     &lt;sequence&gt;
 **                                       &lt;element name="entitlements" maxOccurs="unbounded" minOccurs="0"&gt;
 **                                         &lt;complexType&gt;
 **                                           &lt;complexContent&gt;
 **                                             &lt;extension base="{http://www.oracle.com/schema/oim/offline}entity"&gt;
 **                                               &lt;sequence&gt;
 **                                                 &lt;element name="entitlement" type="{http://www.oracle.com/schema/oim/offline}entitlement" maxOccurs="unbounded" minOccurs="0"/&gt;
 **                                               &lt;/sequence&gt;
 **                                             &lt;/extension&gt;
 **                                           &lt;/complexContent&gt;
 **                                         &lt;/complexType&gt;
 **                                       &lt;/element&gt;
 **                                     &lt;/sequence&gt;
 **                                     &lt;attribute name="action" default="assign"&gt;
 **                                       &lt;simpleType&gt;
 **                                         &lt;restriction base="{http://www.oracle.com/schema/oim/offline}token"&gt;
 **                                           &lt;enumeration value="assign"/&gt;
 **                                           &lt;enumeration value="revoke"/&gt;
 **                                           &lt;enumeration value="enable"/&gt;
 **                                           &lt;enumeration value="disable"/&gt;
 **                                         &lt;/restriction&gt;
 **                                       &lt;/simpleType&gt;
 **                                     &lt;/attribute&gt;
 **                                   &lt;/extension&gt;
 **                                 &lt;/complexContent&gt;
 **                               &lt;/complexType&gt;
 **                             &lt;/element&gt;
 **                           &lt;/sequence&gt;
 **                         &lt;/restriction&gt;
 **                       &lt;/complexContent&gt;
 **                     &lt;/complexType&gt;
 **                   &lt;/element&gt;
 **                 &lt;/sequence&gt;
 **               &lt;/extension&gt;
 **             &lt;/complexContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **       &lt;/sequence&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @see     Entity
 ** @see     RoleEntity
 ** @see     EntitlementEntity
 ** @see     ApplicationEntity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class Identity extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  public static final String            MULTIPLE            = "identities";
  public static final String            SINGLE              = "identity";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4215417896789095432")
  private static final long             serialVersionUID    = 1546301739867664839L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////
  
  private Action action = Action.create;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Action
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants of {@link Action}s.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   **   &lt;simpleType name="action"&gt;
   **     &lt;restriction base="{http://www.oracle.com/schema/oim/offline}token"&gt;
   **       &lt;enumeration value="create"/&gt;
   **       &lt;enumeration value="modify"/&gt;
   **       &lt;enumeration value="delete"/&gt;
   **       &lt;enumeration value="enable"/&gt;
   **       &lt;enumeration value="disable"/&gt;
   **     &lt;/restriction&gt;
   **   &lt;/simpleType&gt;
   ** </pre>
   */
  public static enum Action {
      /** the encoded action values that can by applied on entitlements. */
      create("create")
    , modify("modify")
    , delete("delete")
    , enable("enable")
    , disable("disable")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attribute
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the textual expression of this <code>Action</code>. */
    private final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Action</code> with a single state.
     **
     ** @param  resourceKey      the textual expression of this
     **                          <code>Action</code>.
     */
    Action(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Action</code> from the given
     ** string value.
     **
     ** @param  value              the string value the <code>Action</code>
     **                            should be returned for.
     **
     ** @return                    the <code>Action</code>.
     */
    public static Action from(final String value) {
      // create is the default action id nothing else hence if an empty value is
      // passed in we have to assume the attribute value isn't specified hence
      // the default behavior have to be applied
      if (StringUtility.isEmpty(value))
        return create;

      for (Action cursor : Action.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  /**
   ** The container what {@link RoleEntity}'s the identity should have or lose.
   */
  private final List<RoleEntity>        role                = new LinkedList<RoleEntity>();

  /** The container what {@link RoleEntity}'s the identity already have. */
  private final List<RoleEntity>        roleAssigned        = new LinkedList<RoleEntity>();

  /**
   ** The container what {@link ApplicationEntity}'s the identity should have
   ** or lose.
   */
  private final List<ApplicationEntity> application         = new LinkedList<ApplicationEntity>();

  /**
   ** The container what {@link ApplicationEntity}'s the identity already have.
   */
  private final List<ApplicationEntity> applicationAssigned = new LinkedList<ApplicationEntity>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Identity</code> with the specified name but without
   ** an valid identifier.
   ** <p>
   ** The identifier the <code>Identity</code> belongs to has to be populated
   ** manually.
   **
   ** @param  name               the identifying name of the
   **                            <code>Identity</code>.
   */
  public Identity(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Identity</code> from the specified key and with the
   ** specified name.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>Identity</code> to load.
   ** @param  name               the identifying name of the
   **                            <code>Identity</code>.
   */
  public Identity(final long identifier, final String name) {
    // ensure inheritance
    super(identifier, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} that determines how to handle this
   ** <code>Identity</code> during reconsiliation.
   **
   ** @param  value              the {@link Action} that determines how to
   **                            handle this <code>Identity</code> during
   **                            reconsiliation.
   */
  public final void action(final String value) {
    action(Action.from(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} that determines how to handle this
   ** <code>Identity</code> during reconsiliation.
   **
   ** @param  value              the {@link Action} that determines how to
   **                            handle this <code>Identity</code> during
   **                            reconsiliation.
   */
  public final void action(final Action value) {
    this.action = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the {@link Action} that determines how to handle this
   ** <code>Identity</code> during reconsiliation.
   **
   ** @return                    the {@link Action} that determines how to
   **                            handle this <code>Identity</code> during
   **                            reconsiliation.
   */
  public final Action action() {
    return this.action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the {@link List} of {@link RoleEntity}'s that should be assigned
   ** to or revoked from an identity in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link RoleEntity}'s that
   **                            should be assigned to or revoked from an
   **                            identity in Oracle Identity Manager.
   */
  public final List<RoleEntity> role() {
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleAssigned
  /**
   ** Returns <code>true</code> if the specified {@link RoleEntity} is already
   ** assigned to this identity.
   **
   ** @param  role               the {@link RoleEntity} to check.
   **
   ** @return                    <code>true</code> if the {@link RoleEntity} is
   **                            enlisted in the already assigned roles of this
   **                            <code>Identity</code>.
   */
  public final boolean roleAssigned(final RoleEntity role) {
    return (this.roleAssigned == null || this.roleAssigned.size() == 0) ? false : this.roleAssigned.contains(role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleToAssign
  /**
   ** Returns the {@link List} of {@link RoleEntity}'s that has to be assigned
   ** to this <code>Identity</code> in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link RoleEntity}'s that
   **                            has to be provisioned to this
   **                            <code>Identity</code> in Oracle Identity
   **                            Manager.
   */
  public final List<RoleEntity> roleToAssign() {
    if (CollectionUtility.empty(this.role))
      return new LinkedList<RoleEntity>();
    else
      return CollectionUtility.list(
        new FilteringIterator<RoleEntity>(
          this.role.iterator()
        , new Filter<RoleEntity>() {
            public boolean accept(final RoleEntity test) {
              return RoleEntity.Action.assign == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleToRevoke
  /**
   ** Returns the {@link List} of {@link RoleEntity}'s that has to be revoked
   ** from this <code>Identity</code> in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link RoleEntity}'s that
   **                            has to be revoked from this
   **                            <code>Identity</code> in Oracle Identity
   **                            Manager.
   */
  public final List<RoleEntity> roleToRevoke() {
    if (CollectionUtility.empty(this.role))
      return new LinkedList<RoleEntity>();
    else
      return CollectionUtility.list(
        new FilteringIterator<RoleEntity>(
          this.role.iterator()
        , new Filter<RoleEntity>() {
            public boolean accept(final RoleEntity test) {
              return RoleEntity.Action.revoke == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that should be
   ** provisioned to, revoked from, enabled or disabled for an identity in
   ** Oracle Identity Manager..
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that should be provisioned to, revoked from,
   **                            enabled or disabled for an identity in Oracle
   **                            Identity Manager.
   */
  public final List<ApplicationEntity> application() {
    return this.application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationAssigned
  /**
   ** Returns <code>true</code> if the specified {@link ApplicationEntity} is
   ** already provisioned.
   **
   ** @param  application        the {@link ApplicationEntity} to check.
   ** @param  status             the array of {@link Application} states that
   **                            the {@link ApplicationEntity} must have to be
   **                            evaluated as assigned.
   **
   ** @return                    <code>true</code> if the {@link Application} is
   **                            enlisted in the already provisioned objects of
   **                            this <code>Identity</code>.
   */
  public final boolean applicationAssigned(final ApplicationEntity application, final String[] status) {
    boolean match = false;
    if (!this.applicationAssigned(application))
      return match;

    for (Application cursor : this.applicationAssigned) {
      if (cursor.equals(application)) {
        for (int i = 0; i < status.length; i++) {
          if (status[i].equalsIgnoreCase(cursor.status())) {
            match = true;
            break;
          }
        }
        break;
      }
    }
    return match;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationToAssign
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that has to be
   ** provisioned to the identity in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that has to be provisioned to the identity in
   **                            Oracle Identity Manager.
   */
  public final List<ApplicationEntity> applicationToAssign() {
    if (CollectionUtility.empty(this.application))
      return new LinkedList<ApplicationEntity>();
    else
      return CollectionUtility.list(
        new FilteringIterator<ApplicationEntity>(
          this.application.iterator()
        , new Filter<ApplicationEntity>() {
            public boolean accept(final ApplicationEntity test) {
              return Application.Action.assign == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationToRevoke
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that has to be
   ** revoked from the identity in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that has to be revoked from the identity in
   **                            Oracle Identity Manager.
   */
  public final List<ApplicationEntity> applicationToRevoke() {
    if (CollectionUtility.empty(this.application))
      return new LinkedList<ApplicationEntity>();
    else
      return CollectionUtility.list(
        new FilteringIterator<ApplicationEntity>(
          this.application.iterator()
        , new Filter<ApplicationEntity>() {
            public boolean accept(final ApplicationEntity test) {
              return Application.Action.revoke == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationToEnable
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that has to be
   ** enabled for the identity in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that has to be enabled for the identity in
   **                            Oracle Identity Manager.
   */
  public final List<ApplicationEntity> applicationToEnable() {
    if (CollectionUtility.empty(this.application))
      return new LinkedList<ApplicationEntity>();
    else
      return CollectionUtility.list(
        new FilteringIterator<ApplicationEntity>(
          this.application.iterator()
        , new Filter<ApplicationEntity>() {
            public boolean accept(final ApplicationEntity test) {
              return Application.Action.enable == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationToDisable
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that has to be
   ** disabled for the identity in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that has to be disabled for the identity in
   **                            Oracle Identity Manager.
   */
  public final List<ApplicationEntity> applicationToDisable() {
    if (CollectionUtility.empty(this.application))
      return new LinkedList<ApplicationEntity>();
    else
      return CollectionUtility.list(
        new FilteringIterator<ApplicationEntity>(
          this.application.iterator()
        , new Filter<ApplicationEntity>() {
            public boolean accept(final ApplicationEntity test) {
              return Application.Action.disable == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationAssigned
  /**
   ** Returns <code>true</code> if the specified {@link ApplicationEntity} is
   ** already provisioned.
   **
   ** @param  application        the {@link ApplicationEntity} to check.
   **
   ** @return                    <code>true</code> if the
   **                            {@link ApplicationEntity} is enlisted in the
   **                            already provisioned objects of this
   **                            <code>Identity</code>.
   */
  public final boolean applicationAssigned(final Application application) {
    return (this.applicationAssigned == null || this.applicationAssigned.size() == 0) ? false : this.applicationAssigned.contains(application);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationAssigned
  /**
   ** Returns <code>true</code> if the specified {@link Application} is already
   ** provisioned.
   **
   ** @param  application        the {@link Application} to check.
   ** @param  status             the array of {@link ApplicationEntity}
   **                            states that the {@link ApplicationEntity}
   **                            must have to be evaluated as assigned.
   **
   ** @return                    <code>true</code> if the {@link Application} is
   **                            enlisted in the already provisioned objects of
   **                            this <code>Identity</code>.
   */
  public final boolean applicationAssigned(final Application application, final String[] status) {
    boolean match = false;
    if (!this.applicationAssigned(application))
      return match;

    for (Application cursor : this.applicationAssigned) {
      if (cursor.equals(application)) {
        for (int i = 0; i < status.length; i++) {
          if (status[i].equalsIgnoreCase(cursor.status())) {
            match = true;
            break;
          }
        }
        break;
      }
    }
    return match;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elements (XMLSerializable)
  /**
   ** Returns the XML element tag to marshal/unmarshal multiple entities of
   ** this type.
   **
   ** @return                    the XML element tag to marshal/unmarshal
   **                            multiple entity of this type.
   */
  @Override
  public final String elements() {
    return MULTIPLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (XMLSerializable)
  /**
   ** Returns the XML element tag to marshal/unmarshal a single entity of this
   ** type.
   **
   ** @return                    the XML element tag to marshal/unmarshal a
   **                            single entity of this type.
   */
  @Override
  public final String element() {
    return SINGLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity (Entity)
  /**
   ** Returns the XML name used for informational purpose with an end user.
   **
   ** @return                    the XML name used for informational purpose
   **                            with an end user.
   */
  @Override
  public final String entity() {
    return TaskBundle.string(TaskMessage.ENTITY_IDENTITY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of the object.
   ** <p>
   ** In general, the <code>toString</code> method returns a string that
   ** "textually represents" this object. The result is a concise but
   ** informative representation that is easy for a person to read.
   ** <p>
   ** The <code>toString</code> method for class <code>Entity</code> returns a
   ** string consisting of the name of the class of which the object is an
   ** instance.
   **
   ** @return                    a string representation of the object.
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(super.toString());
    if (!CollectionUtility.empty(this.role)) {
      for (RoleEntity role : this.role)
        builder.append(role.toString());
    }
    if (!CollectionUtility.empty(this.application)) {
      for (Application application : this.application)
        builder.append(application.toString());
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRole
  /**
   ** Adds the specified {@link RoleEntity} to the list of object should be
   ** assigned to or revoked from this <code>Identity</code>.
   **
   ** @param  role               the {@link RoleEntity} of that should be
   **                            assigned to or revoked from this
   **                            <code>Identity</code>.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addRole(final RoleEntity role) {
    return this.role.add(role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRole
  /**
   ** Adds the specified {@link RoleEntity}'s to the list of object should be
   ** assigned to this <code>Identity</code>.
   **
   ** @param  role               the {@link Collection} of {@link RoleEntity}'s
   **                            that should be assigned to this
   **                            <code>Identity</code>.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final boolean addRole(final Collection<RoleEntity> role) {
    return this.role.addAll(role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeRole
  /**
   ** Removes the specified {@link RoleEntity} from the list of object that
   ** should be assigned to or revoked from this <code>Identity</code>.
   **
   ** @param  role               the {@link RoleEntity} that should no longer be
   **                            assigned to or revoked from this
   **                            <code>Identity</code>.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final boolean removeRole(final RoleEntity role) {
    return this.role.remove(role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addApplication
  /**
   ** Adds the specified {@link ApplicationEntity} to the list of objects
   ** that should be provisioned to, revoked from, enabled or disabled for this
   ** <code>Identity</code>.
   **
   ** @param  instance           the {@link ApplicationEntity} that should be
   **                            provisioned to, revoked from, enabled or
   **                            disabled for this <code>Identity</code>.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addApplication(final ApplicationEntity instance) {
    boolean changed = false;
    // add all parents of the requested resource first recursively
    if (instance.hasParents())
      changed = addApplication(instance.parents);

    return (this.application.add(instance) && changed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addApplication
  /**
   ** Adds the specified {@link ApplicationEntity}s to the list of object
   ** that should be provisioned to, revoked from, enabled or disabled for this
   ** <code>Identity</code>.
   **
   ** @param  instance           the {@link Collection} of
   **                            {@link ApplicationEntity}'s that should be
   **                            provisioned to, removed from enabled or
   **                            disabled for this <code>Identity</code>.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addApplication(final Collection<ApplicationEntity> instance) {
    // we cannot use the addAll(Collection) method here because each resource
    // the is member of the specified collection can have parents that has to be
    // added also
    boolean changed = false;
    for (ApplicationEntity entity : instance) {
      if (addApplication(entity))
        changed = true;
    }
    return changed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeApplication
  /**
   ** Removes the specified {@link ApplicationEntity} from the list of object
   ** that should be provisioned to, revoked from, enabled or disabled for this
   ** <code>Identity</code>.
   **
   ** @param  instance           the {@link ApplicationEntity} that should no
   **                            longer be provisioned to, revoked from, enabled
   **                            or disabled for this <code>Identity</code>.
   **
   ** @return                    <code>true</code> if the list contained the
   **                            specified {@link ApplicationEntity}.
   */
  public final boolean removeApplication(final ApplicationEntity instance) {
    return this.application.remove(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAssignedApplication
  /**
   ** Adds the specified {@link ApplicationEntity} to the list of objects
   ** that already provisioned.
   **
   ** @param  resource           the {@link ApplicationEntity} that already
   **                            provisioned to this <code>Identity</code>.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addAssignedApplication(final ApplicationEntity resource) {
    // add the resource themselve
    if (this.applicationAssigned.contains(resource))
      return false;
    else
      return this.applicationAssigned.add(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAssignedApplication
  /**
   ** Adds the specified {@link ApplicationEntity} to the list of objects
   ** that already provisioned.
   **
   ** @param  instance           the {@link ApplicationEntity} that already
   **                            provisioned to this <code>Identity</code>.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addAssignedApplication(final Collection<ApplicationEntity> instance) {
    boolean changed = false;
    for (ApplicationEntity entry : instance) {
      if (addAssignedApplication(entry))
        changed = true;
    }
    return changed;
  }
}