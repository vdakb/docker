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
import java.util.ArrayList;
import java.util.Collection;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class BusinessRoleEntity
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A <code>BusinessRoleEntity</code> comprise all methods to deal with roles
 ** and access policies.
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
 **       &lt;sequence&gt;
 **         &lt;element name="role" maxOccurs="unbounded"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;extension base="{http://www.oracle.com/schema/oim/policy}entity"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="applications"&gt;
 **                     &lt;complexType&gt;
 **                       &lt;complexContent&gt;
 **                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                           &lt;sequence&gt;
 **                             &lt;element name="application" maxOccurs="unbounded"&gt;
 **                               &lt;complexType&gt;
 **                                 &lt;complexContent&gt;
 **                                   &lt;extension base="{http://www.oracle.com/schema/oim/policy}entity"&gt;
 **                                     &lt;sequence&gt;
 **                                       &lt;element name="entitlements" maxOccurs="unbounded"&gt;
 **                                         &lt;complexType&gt;
 **                                           &lt;complexContent&gt;
 **                                             &lt;extension base="{http://www.oracle.com/schema/oim/policy}entity"&gt;
 **                                               &lt;sequence&gt;
 **                                                 &lt;element name="entitlement" type="{http://www.oracle.com/schema/oim/policy}entitlement" maxOccurs="unbounded"/&gt;
 **                                               &lt;/sequence&gt;
 **                                             &lt;/extension&gt;
 **                                           &lt;/complexContent&gt;
 **                                         &lt;/complexType&gt;
 **                                       &lt;/element&gt;
 **                                     &lt;/sequence&gt;
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
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class BusinessRoleEntity extends RoleEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("compatibility:-5670371181962973386")
  private static final long             serialVersionUID = -6793075495134246830L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The container what {@link ApplicationEntity}'s the role should have or
   ** lose.
   */
  private final List<ApplicationEntity> application      = new ArrayList<ApplicationEntity>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BusinessRoleEntity</code> with the specified name but
   ** without a valid identifier.
   **
   ** @param  name               the identifying name of the
   **                            <code>BusinessRoleEntity</code>.
   */
  public BusinessRoleEntity(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Sets the internal system identifier of this <code>Entity</code>.
   **
   ** @param  key                internal system identifier of this
   **                            <code>Entity</code>.
   */
  public final void key(final String key) {
    key(Long.valueOf(key));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Sets the internal system identifier of this <code>Entity</code>.
   **
   ** @param  key                internal system identifier of this
   **                            <code>Entity</code>.
   */
  public final void key(final Long key) {
    key(key.longValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Sets the internal system identifier of this <code>Entity</code>.
   **
   ** @param  key                internal system identifier of this
   **                            <code>Entity</code>.
   */
  public final void key(final long key) {
    this.key = key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the {@link List} of {@link ApplicationEntity}'s that should be
   ** provisioned to, revoked from, enabled or disabled for an organization in
   ** Oracle Identity Manager.
   **
   ** @return                    the {@link List} of {@link ApplicationEntity}'s
   **                            that should be provisioned to, revoked from,
   **                            enabled or disabled for an organization in
   **                            Oracle Identity Manager.
   */
  public final List<ApplicationEntity> application() {
    return this.application;
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

    if (!CollectionUtility.empty(this.application))
      for (ApplicationEntity instance : this.application)
        builder.append(instance.toString());

    return builder.toString();
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