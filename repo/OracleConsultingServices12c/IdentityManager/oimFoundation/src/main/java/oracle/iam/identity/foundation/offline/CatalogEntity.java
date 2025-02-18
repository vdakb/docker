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

    File        :   CatalogEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogEntity.


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

import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class CatalogEntity
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** An <code>CatalogEntity</code> comprise all methods to deal with catalog
 ** elements.
 ** <p>
 ** The name of this class looks a little bit confusing but we have to choose a
 ** name that will not collide with standard API's provided by Oracle Identity
 ** Manager.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;all&gt;
 **         &lt;element name="roles" minOccurs="0"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="role" type="{http://www.oracle.com/schema/oim/catalog}entitlement" maxOccurs="unbounded" minOccurs="0"/&gt;
 **                 &lt;/sequence&gt;
 **               &lt;/restriction&gt;
 **             &lt;/complexContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **         &lt;element name="applications" minOccurs="0"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="application" maxOccurs="unbounded" minOccurs="0"&gt;
 **                     &lt;complexType&gt;
 **                       &lt;complexContent&gt;
 **                         &lt;extension base="{http://www.oracle.com/schema/oim/catalog}entity"&gt;
 **                           &lt;sequence&gt;
 **                             &lt;element name="entitlements" maxOccurs="unbounded" minOccurs="0"&gt;
 **                               &lt;complexType&gt;
 **                                 &lt;complexContent&gt;
 **                                   &lt;extension base="{http://www.oracle.com/schema/oim/catalog}entity"&gt;
 **                                     &lt;sequence&gt;
 **                                       &lt;element name="entitlement" type="{http://www.oracle.com/schema/oim/catalog}entitlement" maxOccurs="unbounded" minOccurs="0"/&gt;
 **                                     &lt;/sequence&gt;
 **                                   &lt;/extension&gt;
 **                                 &lt;/complexContent&gt;
 **                               &lt;/complexType&gt;
 **                             &lt;/element&gt;
 **                           &lt;/sequence&gt;
 **                           &lt;attribute name="action" default="assign"&gt;
 **                             &lt;simpleType&gt;
 **                               &lt;restriction base="{http://www.oracle.com/schema/oim/catalog}token"&gt;
 **                                 &lt;enumeration value="assign"/&gt;
 **                                 &lt;enumeration value="revoke"/&gt;
 **                                 &lt;enumeration value="enable"/&gt;
 **                                 &lt;enumeration value="disable"/&gt;
 **                               &lt;/restriction&gt;
 **                             &lt;/simpleType&gt;
 **                           &lt;/attribute&gt;
 **                           &lt;attribute name="risk" default="none"&gt;
 **                             &lt;simpleType&gt;
 **                               &lt;restriction base="{http://www.oracle.com/schema/oim/catalog}token"&gt;
 **                                 &lt;enumeration value="none"/&gt;
 **                                 &lt;enumeration value="low"/&gt;
 **                                 &lt;enumeration value="medium"/&gt;
 **                                 &lt;enumeration value="high"/&gt;
 **                               &lt;/restriction&gt;
 **                             &lt;/simpleType&gt;
 **                           &lt;/attribute&gt;
 **                         &lt;/extension&gt;
 **                       &lt;/complexContent&gt;
 **                     &lt;/complexType&gt;
 **                   &lt;/element&gt;
 **                 &lt;/sequence&gt;
 **               &lt;/restriction&gt;
 **             &lt;/complexContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **       &lt;/all&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class CatalogEntity extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  public static final String           MULTIPLE         = "catalogs";
  public static final String           SINGLE           = "catalog";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6095319352493560185")
  private static final long            serialVersionUID = -3656943490051915241L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The container what {@link RoleEntity}'s the catalog should have or lose.
   */
  private final List<RoleEntity>        role            = new LinkedList<RoleEntity>();

  /**
   ** The container what {@link ApplicationEntity}'s the catalog should have or
   ** lose.
   */
  private final List<ApplicationEntity> application     = new LinkedList<ApplicationEntity>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>CatalogEntity</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public CatalogEntity() {
    super(SINGLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the {@link List} of {@link RoleEntity}'s that should be assigned
   ** to or revoked from the catalog in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link RoleEntity}'s that
   **                            should be assigned to or revoked from the
   **                            catalog in Oracle Identity Manager.
   */
  public final List<RoleEntity> role() {
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleToAssign
  /**
   ** Returns the {@link List} of {@link EntitlementEntity}'s that has to be
   ** assigned to this <code>CatalogEntity</code> in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link EntitlementEntity}'s
   **                            that has to be provisioned to this
   **                            <code>CatalogEntity</code> in Oracle Identity
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
              return EntitlementEntity.Action.assign == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleToRevoke
  /**
   ** Returns the {@link List} of {@link EntitlementEntity}'s that has to be
   ** revoked from this <code>CatalogEntity</code> in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link EntitlementEntity}'s
   **                            that has to be revoked from this
   **                            <code>CatalogEntity</code> in Oracle Identity
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
              return EntitlementEntity.Action.revoke == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that should be
   ** provisioned to, revoked from, enabled or disabled for a catalog in
   ** Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that should be provisioned to, revoked from,
   **                            enabled or disabled for a catalog in Oracle
   **                            Identity Manager.
   */
  public final List<ApplicationEntity> application() {
    return this.application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationToAssign
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that has to be
   ** provisioned to the catalog in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that has to be provisioned to the catalog in
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
              return ApplicationEntity.Action.assign == test.action();
            }
          }
        )
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationToRevoke
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that has to be
   ** revoked from the catalog in Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that has to be revoked from the catalog in
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
              return ApplicationEntity.Action.revoke == test.action();
            }
          }
        )
      );
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
    return TaskBundle.string(TaskMessage.ENTITY_CATALOG);
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
    StringBuilder builder =  new StringBuilder(super.toString());

    if (!CollectionUtility.empty(this.role))
      for (RoleEntity role : this.role)
        builder.append(role.toString());

    if (!CollectionUtility.empty(this.application))
      for (ApplicationEntity instance : this.application)
        builder.append(instance.toString());

    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRole
  /**
   ** Adds the specified {@link RoleEntity} to the list of object should be
   ** assigned to or revoked from this <code>CatalogEntity</code>.
   **
   ** @param  role               the {@link RoleEntity} of that should be
   **                            assigned to or revoked from this
   **                            <code>CatalogEntity</code>.
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
   ** assigned to this <code>CatalogEntity</code>.
   **
   ** @param  role               the {@link Collection} of {@link RoleEntity}'s
   **                            that should be assigned to this
   **                            <code>CatalogEntity</code>.
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
   ** should be assigned to or revoked from this <code>CatalogEntity</code>.
   **
   ** @param  role               the {@link RoleEntity} that should no longer be
   **                            assigned to or revoked from this
   **                            <code>CatalogEntity</code>.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final boolean removeRole(final RoleEntity role) {
    if (this.role == null)
      return false;

    return this.role.remove(role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addApplication
  /**
   ** Adds the specified {@link ApplicationEntity} to the list of objects
   ** that should be provisioned to, revoked from, enabled or disabled for this
   ** <code>CatalogEntity</code>.
   **
   ** @param  instance           the {@link ApplicationEntity} that should be
   **                            provisioned to, revoked from, enabled or
   **                            disabled for this <code>CatalogEntity</code>.
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
   ** <code>CatalogEntity</code>.
   **
   ** @param  instance           the {@link Collection} of
   **                            {@link ApplicationEntity}s that should be
   **                            provisioned to, removed from enabled or
   **                            disabled for this <code>CatalogEntity</code>.
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
   ** <code>CatalogEntity</code>.
   **
   ** @param  instance           the {@link ApplicationEntity} that should no
   **                            longer be provisioned to, revoked from, enabled
   **                            or disabled for this <code>CatalogEntity</code>.
   **
   ** @return                    <code>true</code> if the list contained the
   **                            specified {@link ApplicationEntity}.
   */
  public final boolean removeApplication(final ApplicationEntity instance) {
    return this.application.remove(instance);
  }
}