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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wls.type;

import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.ServerSecurityHandler;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
public class User extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ServerSecurityHandler.User delegate = new ServerSecurityHandler.User();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>User</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public User() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>User</code> with the specified name.
   **
   ** @param  name               the name of the user.
   */
  protected User(final String name) {
    // ensure inheritance
    super();

    // ensure inheritance
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAction
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>action</code>.
   **
   ** @param  action             the action to apply on the credential mapping
   **                            in the Oracle WebLogic Server Domain.
   */
  public void setAction(final Action action) {
    checkAttributesAllowed();
    this.delegate.action(ServiceOperation.from(action.getValue()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the bean to manage in Oracle
   **                            WebLogic Server Domain.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>password</code>.
   **
   ** @param  password           the password of the bean to manage in Oracle
   **                            WebLogic Server Domain.
   */
  public void setPassword(final String password) {
    checkAttributesAllowed();
    this.delegate.password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description        the description of the bean to manage in Oracle
   **                            WebLogic Server Domain.
   */
  public void setDescription(final String description) {
    checkAttributesAllowed();
    this.delegate.description(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link ServerSecurityHandler.User} delegate.
   **
   ** @return                    the {@link ServerSecurityHandler.User}
   **                            delegate.
   */
  public final ServerSecurityHandler.User instance() {
    return this.delegate;
  }

  /////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  /////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return this.delegate.name().hashCode();
  }

  /////////////////////////////////////////////////////////////////////////////
  // Method:    equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>User</code> object that represents
   ** the same <code>name</code>.
   **
   ** @param other             the object to compare this <code>User</code>
   **                          with.
   **
   ** @return                  <code>true</code> if the <code>User</code>s
   **                          are equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof User))
      return false;

    final User another = (User)other;
    return another.delegate.name().equals(this.delegate.name());
  }
}