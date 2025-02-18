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

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Resource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.model;

import java.util.Iterator;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.offline.AccessPolicy;
import oracle.iam.identity.foundation.offline.ProcessForm;

////////////////////////////////////////////////////////////////////////////////
// abstract class Resource
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** A <code>Resource Object</code> that wrappes the custom level type
 ** <code>Portal</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Resource extends Accessor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link TreeSet} of <code>Resource Object</code>s that are designed as
   ** {@link Role}s and has a dependendy on this <code>Resource Object</code>
   */
	protected TreeSet<Resource>   parents         = new TreeSet<Resource>();

  /**
   ** The {@link Map} the stores the depended Resources in the way
   ** <ul>
   **   <li> keyEntry is the internal identifier of Oracle Identity Manager
   **                 for the <code>Resource Object</code>
   **   <li> value    is the public name of Oracle Identity Manager for the
   **                 <code>Resource Object</code>
   ** </ul>
   */
  private Map<String, Resource> forward         = new TreeMap<String, Resource>();

  /**
   ** The {@link Map} the stores the depended Resources in the way
   ** <ul>
   **   <li> keyEntry is the public name of Oracle Identity Manager for the
   **                 <code>Resource Object</code>
   **   <li> value    is the internal identifier of Oracle Identity Manager
   **                 for the <code>Resource Object</code>
   ** </ul>
   */
  private Map<String, Resource> reverse         = new TreeMap<String, Resource>();

  private String                status;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // abstract class OrganizationResource
  // ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
  /**
   ** A <code>Resource Object</code> that's orderable for organization.
   */
  public static abstract class OrganizationResource extends Resource {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Resource Object</code> which is associated with the
     ** specified task.
     **
     ** @param  objectKey        the internal system identifier of the
     **                          <code>Resource Object</code> to load.
     ** @param  objectName       the name of the <code>Resource Object</code>.
     */
    private OrganizationResource(final long objectKey, final String objectName) {
      // ensure inheritance
      super(objectKey, objectName);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   orderableFor (Resource)
    /**
     ** Returns the String that is passed to the filter to retrieve the requested
     ** order for of a <code>Resource Object</code>.
     **
     ** @return                  the String that is passed to the filter to
     **                          retrieve the requested order for of a
     **                          <code>Resource Object</code>.
     */
    public final String orderableFor() {
      return ResourceObject.ORDER_FOR_ORGANIZATION;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // abstract class UserResource
  // ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
  /**
   ** A <code>Resource Object</code> that's orderable for users.
   */
  public static abstract class UserResource extends Resource {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Resource Object</code> which is associated with the
     ** specified task.
     ** <p>
     ** The instances created by this constructor need not be assigned to a
     ** organization or user profile
     **
     ** @param  objectKey        the internal system identifier of the
     **                          <code>Resource Object</code> to load.
     ** @param  objectName       the name of the <code>Resource Object</code>.
     */
    private UserResource(final long objectKey, final String objectName) {
      // ensure inheritance
      super(objectKey, objectName);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Resource Object</code> which is associated with the
     ** specified task.
     ** <p>
     ** The instances created by this constructor must be assigned to a
     ** organization or user profile.
     **
     ** @param  objectKey        the internal system identifier of the
     **                          <code>Resource Object</code> to load.
     ** @param  objectName       the name of the <code>Resource Object</code>.
     ** @param  objectStatus     the status of the <code>Resource Object</code>
     */
    public UserResource(final long objectKey, final String objectName, final String objectStatus) {
      // ensure inheritance
      super(objectKey, objectName, objectStatus);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   orderableFor (Resource)
    /**
     ** Returns the String that is passed to the filter to retrieve the requested
     ** order for of a <code>Resource Object</code>.
     **
     ** @return                  the String that is passed to the filter to
     **                          retrieve the requested order for of a
     **                          <code>Resource Object</code>.
     */
    public final String orderableFor() {
      return ResourceObject.ORDER_FOR_USER;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** A <code>Resource Object</code> that wrappes the custom level type
   ** <code>Server</code>.
   */
  public static class Server extends UserResource {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

  	private ProcessForm           proccessParentForm;
  	private ProcessForm[]         proccessChildForm;

    /**
     ** the {@link TreeSet} of <code>Access Policies</code> that are
     ** provisioning this <code>Resource Object</code>
     */
	  private TreeSet<AccessPolicy> policy          = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Resource Object</code> which is associated with the
     ** specified task.
     ** <p>
     ** The custom level type is that to <code>LEVEL_TYPE_SERVER</code>
     **
     ** @param  objectKey        the internal system identifier of the
     **                          <code>Resource Object</code> to load.
     ** @param  objectName       the name of the <code>Resource Object</code>.
     */
    public Server(final long objectKey, final String objectName) {
      // ensure inheritance
      super(objectKey, objectName);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   proccessParentForm
    /**
     ** Sets a the internal system identifier of the process form that is used
     ** to provision this <code>Resource Object</code>s.
     **
     ** @param  proccessParentForm the internal system identifier of the process
     **                            form that is used to provision this
     **                            <code>Resource Object</code>s.
     */
    public final void proccessParentForm(final ProcessForm proccessParentForm) {
      this.proccessParentForm = proccessParentForm;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   proccessParentForm
    /**
     ** Returns the internal system identifier of the process form that is used
     ** to provision this <code>Resource Object</code>s.
     **
     ** @return                    the internal system identifier of the process
     **                            form that is used to provision this
     **                            <code>Resource Object</code>s.
     */
    public final ProcessForm proccessParentForm() {
      return this.proccessParentForm;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   proccessChildForm
    /**
     ** Sets a the internal system identifier of the process form that is used
     ** to provision details of this <code>Resource Object</code>s.
     **
     ** @param  proccessChildForm  the internal system identifier of the process
     **                            forms that is used to provision details of
     **                            this <code>Resource Object</code>s.
     */
    public final void proccessChildForm(final ProcessForm[] proccessChildForm) {
      this.proccessChildForm = proccessChildForm;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   proccessChildForm
    /**
     ** Returns the internal system identifier of the process form that is used
     ** to provision details of this <code>Resource Object</code>s.
     **
     ** @return                    the internal system identifier of the process
     **                            form that is used to provision details of
     **                            this <code>Resource Object</code>s.
     */
    public final ProcessForm[] proccessChildForm() {
      return this.proccessChildForm;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   policy
    /**
     ** Returns the <code>Access Policies</code> for
     ** this <code>Resource Object</code>.
     **
     ** @return                  the <code>Access Policies</code> for thie
     **                          <code>Resource Object</code>.
     */
    public final TreeSet<AccessPolicy> policy() {
      return this.policy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   provisionedBy
    /**
     ** Returns <code>true</code> if the specified {@link AccessPolicy}
     ** provisions this <code>Resource Object</code>s.
     **
     ** @param  policy           the {@link AccessPolicy} that provisions this
     **                          <code>Resource Object</code> to test.
     **
     ** @return                  <code>true</code> if the specified
     **                          {@link AccessPolicy} provisions this
     **                          <code>Resource Object</code>s.
     */
    public final boolean provisionedBy(final AccessPolicy policy) {
      return (this.policy == null) ? false : this.policy.contains(policy);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   type (Resource)
    /**
     ** Returns the String that is passed to the filter to retrieve the requested
     ** type of a <code>Resource Object</code>.
     **
     ** @return                  the String that is passed to the filter to
     **                          retrieve the requested type of a
     **                          <code>Resource Object</code>.
     */
    public final String type() {
      return ResourceObject.TYPE_APPLICATION;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addPolicy
    /**
     ** Adds the {@link AccessPolicy} that provisions this
     ** <code>Resource Object</code>.
     **
     ** @param  policy           the {@link AccessPolicy} that provisions this
     **                          <code>Resource Object</code> to add.
     **
     ** @return                    <code>true</code> if this collection did not
     **                            already contain the specified
     **                            {@link AccessPolicy}; otherwise
     **                            <code>false</code>.
     */
    public final boolean addPolicy(final AccessPolicy policy) {
      if (this.policy == null)
        this.policy = new TreeSet<AccessPolicy>();

      return this.policy.add(policy);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   removePolicy
    /**
     ** Adds the {@link AccessPolicy} that provisions this
     ** <code>Resource Object</code>.
     **
     ** @param  policy             the {@link AccessPolicy} that provisions this
     **                            <code>Resource Object</code> to add.
     **
     ** @return                    <code>true</code> if this collection
     **                            contained the specified {@link AccessPolicy};
     **                            otherwise <code>false</code>.
     */
    public final boolean removePolicy(final AccessPolicy policy) {
      if (this.policy == null)
        return false;

      return this.policy.remove(policy);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Role
  // ~~~~~ ~~~~
  /**
   ** A <code>Resource Object</code> that wrappes the custom level type
   ** <code>Application Role</code>.
   */
  public static class Role extends UserResource {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Resource Object</code> which is associated with the
     ** specified task.
     ** <p>
     ** The instances created by this constructor need not be assigned to a
     ** organization or user profile
     **
     ** @param  roleKey          the internal system identifier of the
     **                          <code>Resource Object</code> to load.
     ** @param  roleName         the name of the <code>Resource Object</code>.
     */
    public Role(final long roleKey, final String roleName) {
      this(roleKey, roleName, SystemConstant.EMPTY);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Resource Object</code> which is associated with the
     ** specified task.
     ** <p>
     ** The instances created by this constructor must be assigned to a
     ** organization or user profile.
     **
     ** @param  roleKey          the internal system identifier of the
     **                          <code>Resource Object</code> to load.
     ** @param  roleName         the name of the <code>Resource Object</code>.
     ** @param  roleStatus       the status of the <code>Resource Object</code>
     */
    public Role(final long roleKey, final String roleName, final String roleStatus) {
      // ensure inheritance
      super(roleKey, roleName, roleStatus);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   type (Resource)
    /**
     ** Returns the String that is passed to the filter to retrieve the requested
     ** type of a <code>Resource Object</code>.
     **
     ** @return                  the String that is passed to the filter to
     **                          retrieve the requested type of a
     **                          <code>Resource Object</code>.
     */
    public final String type() {
      return ResourceObject.TYPE_APPLICATION;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource Object</code> which is associated with the
   ** specified task.
   **
   ** @param  objectKey          the internal system identifier of the
   **                            <code>Resource Object</code> to load.
   ** @param  objectName         the name of the <code>Resource Object</code>.
   */
  protected Resource(final long objectKey, final String objectName) {
    // ensure inheritance
    super(objectKey, objectName);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource Object</code> which is associated with the
   ** specified task.
   **
   ** @param  objectKey          the internal system identifier of the
   **                            <code>Resource Object</code> to load.
   ** @param  objectName         the name of the <code>Resource Object</code>.
   ** @param  objectStatus       the status of the <code>Resource Object</code>
   */
  protected Resource(final long objectKey, final String objectName, final String objectStatus) {
    // ensure inheritance
    super(objectKey, objectName);
    // initialize instannce attributes
    this.status = objectStatus;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status of the <code>Resource Object</code>.
   **
   ** @return                    the status of the
   **                            <code>Resource Object</code>.
   */
  public final String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasParents
  /**
   ** Returns <code>true</code> if this resource dependends on other
   ** <code>Resource Object</code>s.
   **
   ** @return                    <code>true</code> if this resource dependends
   **                            on other <code>Resource Object</code>s.
   */
  public final boolean hasParents() {
    return this.parents.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependsOn
  /**
   ** Returns <code>true</code> if this resource dependends on the specified
   ** <code>Resource Object</code>.
   **
   ** @param  parent             the <code>Resource Object</code> to detect if a
   **                            dependency exists.
   **
   ** @return                    <code>true</code> if this resource dependends
   **                            on the specified <code>Resource Object</code>.
   */
  public final boolean dependsOn(final Resource parent) {
    return hasParents() ? this.parents.contains(parent) : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isOwner
  /**
   ** Returns <code>true</code> if this resource has dependend
   ** <code>Resource Object</code>s.
   **
   ** @return                    <code>true</code> if this resource has
   **                            dependend <code>Resource Object</code>s.
   */
  public final boolean isOwner() {
    return !this.forward.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   owns
  /**
   ** Returns <code>true</code> if the specified resource is enlisted in
   ** dependend <code>Resource Object</code>s.
   **
   ** @param  resource           the name of the <code>Resource Object</code>
   **                            to lookup.
   **
   ** @return                    <code>true</code> if the specified resource is
   **                            enlisted in dependend
   **                            <code>Resource Object</code>s; otherwise
   **                            <code>false</code>.
   */
  public final boolean owns(final long resource) {
    return this.forward.containsKey(String.valueOf(resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   owns
  /**
   ** Returns <code>true</code> if the specified resource is enlisted in
   ** dependend <code>Resource Object</code>s.
   **
   ** @param  resource           the name of the <code>Resource Object</code>
   **                            to lookup.
   **
   ** @return                    <code>true</code> if the specified resource is
   **                            enlisted in dependend
   **                            <code>Resource Object</code>s; otherwise
   **                            <code>false</code>.
   */
  public final boolean owns(final String resource) {
    return this.reverse.containsKey(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependency
  /**
   ** Returns the <code>Resource Object</code> if the specified resource is
   ** enlisted in dependend <code>Resource Object</code>s.
   **
   ** @param  resource           the <code>Resource Object</code> instance or
   **                            <code>null</code> if the specified name does
   **                            not match to a depended
   **                            <code>Resource Object</code>.
   **
   ** @return                    the <code>Resource Object</code> if the
   **                            specified resource is enlisted in dependend
   **                            <code>Resource Object</code>s.
   */
  public final Resource dependency(final long resource) {
    return this.forward.get(String.valueOf(resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependency
  /**
   ** Returns the <code>Resource Object</code> if the specified resource is
   ** enlisted in dependend <code>Resource Object</code>s.
   **
   ** @param  resource           the <code>Resource Object</code> instance or
   **                            <code>null</code> if the specified name does
   **                            not match to a depended
   **                            <code>Resource Object</code>.
   **
   ** @return                    the <code>Resource Object</code> if the
   **                            specified resource is enlisted in dependend
   **                            <code>Resource Object</code>s.
   */
  public final Resource dependency(final String resource) {
    return this.reverse.get(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the String that is passed to the filter to retrieve the requested
   ** type of a <code>Resource Object</code>.
   **
   ** @return                    the String that is passed to the filter to
   **                            retrieve the requested type of a
   **                            <code>Resource Object</code>.
   */
  public abstract String type();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orderableFor
  /**
   ** Returns the String that is passed to the filter to retrieve the requested
   ** order for of a <code>Resource Object</code>.
   **
   ** @return                    the String that is passed to the filter to
   **                            retrieve the requested order for of a
   **                            <code>Resource Object</code>.
   */
  public abstract String orderableFor();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParent
  /**
   ** Adds the specified <code>Resource Object</code> to the list of parent
   ** object that where this  <code>Resource Object</code> depends on.
   **
   ** @param  resource           the <code>Resource Object</code> that should be
   **                            added to the set of parents.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addParent(final Collection<Resource> resource) {
    Iterator<Resource> i = resource.iterator();
    while (i.hasNext())
      addParent(i.next());
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParent
  /**
   ** Adds the specified <code>Resource Object</code> to the list of parent
   ** object where this <code>Resource Object</code> depends on.
   **
   ** @param  resource           the <code>Resource Object</code> that should be
   **                            added to the set of parents.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean addParent(final Resource resource) {
    if (this.parents == null)
      this.parents = new TreeSet<Resource>();

    // add the resource themselve
    if (!parents.contains(resource)) {
      resource.forward.put(String.valueOf(this.key), this);
      resource.reverse.put(this.name, this);
      return this.parents.add(resource);
    }
    else
      return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeParent
  /**
   ** Removes the specified <code>Resource Object</code>s from the list of
   ** parent object where this <code>Resource Object</code> depends on.
   **
   ** @param  resource           the <code>Resource Object</code> that should be
   **                            added to the set of parents.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean removeParent(final Collection<Resource> resource) {
    Iterator<Resource> i = resource.iterator();
    while (i.hasNext())
      removeParent(i.next());
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeParent
  /**
   ** Removes the specified <code>Resource Object</code> from the list of parent
   ** object where this <code>Resource Object</code> depends on.
   **
   ** @param  resource           the <code>Resource Object</code> that should be
   **                            removed from the set of parents.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean removeParent(final Resource resource) {
    if (this.parents == null)
      return false;

    // remove the resource themselve
    if (parents.contains(resource)) {
      resource.forward.remove(String.valueOf(this.key));
      resource.reverse.remove(this.name);
      return this.parents.remove(resource);
    }
    else
      return false;
  }
}