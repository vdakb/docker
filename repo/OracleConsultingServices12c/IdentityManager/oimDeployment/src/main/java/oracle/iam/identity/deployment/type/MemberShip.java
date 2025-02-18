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

    File        :   MemberShip.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MemberShip.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.iam.accesspolicy.vo.AccessPolicy;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class MemberShip
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>MemberShip</code> defines the attribute restriction on values that can
 ** be passed as a nested parameter to deployment grants or revocations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MemberShip extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the exportable/importableobject definitions in alphabetical order
  private static String[] OPERATION = { ServiceOperation.assign.id(), ServiceOperation.revoke.id() };

  // the names of the exportable/importableobject definitions in alphabetical order
  private static String[] RECIPIENT = { RoleManagerConstants.ROLE_ENTITY_NAME, UserManagerConstants.USER_ENTITY, AccessPolicy.ENTITY_TYPE };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private List<Recipient> recipient = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Recipient
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Recipient</code> defines the type of a membership operation
   */
  public static class Recipient extends EnumeratedAttribute {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String  name = null;
    private boolean hierarchy = false;

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setName
    /**
     ** Call by the ANT kernel to inject the argument for parameter name.
     **
     ** @param  name             the name of the Identity Manager object this
     **                          category wraps.
     */
    public void setName(final String name) {
      this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the object to be exported or imported by this
     ** category.
     **
     ** @return                  the name of the object to be exported or
     **                          imported by this category.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setHierarchy
    /**
     ** Call by the ANT kernel to inject the argument for parameter hierarchy.
     **
     ** @param  hierarchy        <code>true</code> if the enablements is
     **                          hierarchy aware; otherwise <code>false</code>.
     */
    public void setHierarchy(final boolean hierarchy) {
      this.hierarchy = hierarchy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: hierarchy
    /**
     ** Returns <code>true</code> if the enablements is hierarchy aware;
     ** otherwise <code>false</code>.
     **
     ** @return                  <code>true</code> if the enablements is
     **                          hierarchy aware; otherwise <code>false</code>.
     */
    public final boolean hierarchy() {
      return this.hierarchy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   getValues (EnumeratedAttribute)
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
      return RECIPIENT;
    }

    /////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    /////////////////////////////////////////////////////////////////////////////

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
     ** @param other               the object to compare this
     **                            <code>Recipient</code> against.
     **
     ** @return                   <code>true</code> if the
     **                           <code>Recipient</code>s are
     **                           equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Recipient))
        return false;

      final Recipient another = (Recipient)other;
      return this.name.equals(another.name) && this.value.equals(another.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MemberShip</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MemberShip() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the name of the type of this operation.
   **
   ** @return                    the name of the type of this operation.
   */
  public final ServiceOperation value() {
    return ServiceOperation.from(super.getValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   recipient
  /**
   ** Returns the recipients of the object to be assigned or revoked by this
   ** membership.
   **
   ** @return                    the recipients of the object to be assigned or
   **                            revoked by this membership.
   */
  public final List<Recipient> recipient() {
    return this.recipient;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return OPERATION;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRecipient
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Recipient}.
   **
   ** @param  recipient          the {@link Recipient} to add.
   **
   ** @throws BuildException     if the specified {@link Recipient} is already
   **                            part of the parameter mapping.
   */
  public void addConfiguredRecipient(final Recipient recipient)
    throws BuildException {

    if (this.recipient == null)
      this.recipient = new ArrayList<Recipient>();

    if (this.recipient.contains(recipient))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, String.format("%s %s", recipient.getValue(), recipient.name())));

    this.recipient.add(recipient);
  }
}