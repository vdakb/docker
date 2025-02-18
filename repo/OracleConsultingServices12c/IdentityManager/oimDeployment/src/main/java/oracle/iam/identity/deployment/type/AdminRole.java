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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AdminRole.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AdminRole.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import java.util.List;
import java.util.ArrayList;

import oracle.hst.deployment.ServiceError;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.spi.AdminRoleInstance;

import oracle.iam.platform.authopss.api.PolicyConstants;

////////////////////////////////////////////////////////////////////////////////
// class AdminRole
// ~~~~~ ~~~~~~~~~
/**
 ** <code>AdminRole</code> represents aa admin role in Identity Manager that
 ** might be created, updated or deleted after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AdminRole extends Entitlement {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the exportable/importableobject definitions in alphabetical order
  private static String[] OPERATION = { ServiceOperation.assign.id(), ServiceOperation.revoke.id() };

  // the names of the exportable/importable capability definitions in alphabetical
  // order
  private static final String[] RESOURCE;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    final PolicyConstants.Resources[] resources = PolicyConstants.Resources.values();
    RESOURCE = new String[resources.length];
    for (int i = 0; i < resources.length; i++)
      RESOURCE[i] = resources[i].getId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Action
  // ~~~~~ ~~~~~~
  /**
   ** <code>Action</code> defines the possible values of a resource action.
   */
  public static class Action extends DataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String name = null;

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setName
    /**
     ** Sets the name of the action.
     **
     ** @param  name               the name of the action.
     */
    public void setName(final String name) {
      this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of this admin role action.
     **
     ** @return                    the name of this admin role action.
     */
    public String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public int hashCode() {
      return this.name.hashCode();
    }

    /////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** The result is <code>true</code> if and only if the argument is not
     ** <code>null</code> and is a <code>Recipient</code> object that
     ** represents the same <code>name</code> and value as this object.
     **
     ** @param other             the object to compare this
     **                          <code>Recipient</code> against.
     **
     ** @return                  <code>true</code> if the
     **                          <code>Recipient</code>s are
     **                          equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Action))
        return false;

      final Action another = (Action)other;
      return this.name.equals(another.name);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Resource
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>Resource</code> defines the possible values of a resources.
   */
  public static class Resource extends EnumeratedAttribute {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private List<String> action = null;

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    public List<String> action() {
      return this.action;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getValues (EnumeratedAttribute)
    /**
     ** The only method a subclass needs to implement.
     **
     ** @return                  an array holding all possible values of the
     **                          enumeration. The order of elements must be
     **                          fixed so that indexOfValue(String) always
     **                          return the same index for the same value.
     */
    @Override
    public String[] getValues() {
      return RESOURCE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       an execution of a Java application, the <code>hashCode</code>
     **       method must consistently return the same integer, provided no
     **       information used in <code>equals</code> comparisons on the object
     **       is modified. This integer need not remain consistent from one
     **       execution of an application to another execution of the same
     **       application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public int hashCode() {
      return this.value.hashCode();
    }

    /////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** The result is <code>true</code> if and only if the argument is not
     ** <code>null</code> and is a <code>Recipient</code> object that
     ** represents the same <code>name</code> and value as this object.
     **
     ** @param other             the object to compare this
     **                          <code>Recipient</code> against.
     **
     ** @return                  <code>true</code> if the
     **                          <code>Recipient</code>s are
     **                          equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Capability))
        return false;

      final Resource another = (Resource)other;
      return this.value.equals(another.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredAction
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** {@link Action}.
     **
     ** @param  action           the action to add.
     **
     ** @throws BuildException   if the specified action is already
     **                          part of the resource.
     */
    public void addConfiguredAction(final Action action)
      throws BuildException {

      if (this.action == null)
        this.action = new ArrayList<String>();

      this.action.add(action.name);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Capability
  // ~~~~~ ~~~~~~~~~~
  /**
   ** <code>Capability</code> defines the possible values of a capability.
   */
  public static class Capability extends EnumeratedAttribute {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private List<Resource> resource = null;

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the name of the type of this operation.
     **
     ** @return                  the name of the type of this operation.
     */
    public final ServiceOperation value() {
      return ServiceOperation.from(super.getValue());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getValues (EnumeratedAttribute)
    /**
     ** The only method a subclass needs to implement.
     **
     ** @return                  an array holding all possible values of the
     **                          enumeration. The order of elements must be
     **                          fixed so that indexOfValue(String) always
     **                          return the same index for the same value.
     */
    @Override
    public String[] getValues() {
      return OPERATION;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       an execution of a Java application, the <code>hashCode</code>
     **       method must consistently return the same integer, provided no
     **       information used in <code>equals</code> comparisons on the object
     **       is modified. This integer need not remain consistent from one
     **       execution of an application to another execution of the same
     **       application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public int hashCode() {
      return this.value.hashCode();
    }

    /////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** The result is <code>true</code> if and only if the argument is not
     ** <code>null</code> and is a <code>Recipient</code> object that
     ** represents the same <code>name</code> and value as this object.
     **
     ** @param other             the object to compare this
     **                          <code>Recipient</code> against.
     **
     ** @return                  <code>true</code> if the
     **                          <code>Recipient</code>s are
     **                          equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Capability))
        return false;

      final Capability another = (Capability)other;
      return this.value.equals(another.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredResource
    /**
     ** Call by the ANT deployment to inject the argument for adding a
     ** {@link Resource}.
     **
     ** @param  resource         the {@link Resource} to add.
     **
     ** @throws BuildException   if the specified {@link Capability} is already
     **                          part of the capabilities.
     */
    public void addConfiguredResource(final Resource resource)
      throws BuildException {

      if (this.resource == null)
        this.resource = new ArrayList<Resource>();

      if (this.resource.contains(resource))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, resource.getValue()));

      this.resource.add(resource);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AdminRole</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AdminRole() {
    // ensure inheritance
    super(new AdminRoleInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  public final void setScoped(final boolean value) {
    checkAttributesAllowed();
    ((AdminRoleInstance)this.delegate).scoped(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCapability
  /**
   ** Add the specified {@link Capability} to add.
   **
   ** @param  capability         the {@link Capability} to add.
   */
  public void addConfiguredCapability(final Capability capability) {
    checkChildrenAllowed();
    ((AdminRoleInstance)this.delegate).addResource(capability.value(), capability.resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredMemberShip
  /**
   ** Add the specified {@link MemberShip} rule to add.
   **
   ** @param  membership         the {@link MemberShip} rule to add.
   */
  public void addConfiguredMemberShip(final MemberShip membership) {
    checkChildrenAllowed();
    ((AdminRoleInstance)this.delegate).addMemberShip(membership.value(), membership.recipient());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredPublication
  /**
   ** Add the specified {@link Publication} rule to add.
   **
   ** @param  publication        the {@link Publication} rule to add.
   */
  public void addConfiguredPublication(final Publication publication) {
    checkChildrenAllowed();
    ((AdminRoleInstance)this.delegate).addPublication(publication.value(), publication.recipient());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredScope
  /**
   ** Add the specified {@link Publication} rule to add.
   **
   ** @param  publication        the {@link Publication} rule to add.
   */
  public void addConfiguredScope(final Publication publication) {
    checkChildrenAllowed();
    ((AdminRoleInstance)this.delegate).addScope(publication.value(), publication.recipient());
  }
}