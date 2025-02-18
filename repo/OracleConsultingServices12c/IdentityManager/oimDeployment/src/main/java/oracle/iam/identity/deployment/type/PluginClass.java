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

    File        :   PluginClass.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    PluginClass.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class PluginClass
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>PluginClass</code> represents a class name of a plugin that needs to
 ** be unregistred in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PluginClass extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String name;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PluginClass</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PluginClass() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PluginClass</code> with the specified class name.
   **
   ** @param  name               the name of the class in Identity Manager to
   **                            unregister.
   */
  public PluginClass(final String name) {
    // ensure inheritance
    super();

    // intilaize instance attributes
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another <code>Role</code>
   ** instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(name))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the class in Identity Manager to
   **                            unregister.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.name = name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the class of the Identity Manager plugin.
   **
   ** @return                    the name of the class of the Identity Manager
   **                            plugin.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    return this.name.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>PluginClass</code> object that
   ** represents the same <code>name</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>PluginClass</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>PluginClass</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof PluginClass))
      return false;

    return this.name.equals(((PluginClass)other).name);
  }
}