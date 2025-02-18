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

    System      :   Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Namespace.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Namespace.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.1.0.0     2014-11-29  DSteding    First release version
*/

package oracle.hst.deployment.mds.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class Namespace
// ~~~~~ ~~~~~~~~~
/**
 ** The environment wrapper of a specific path in a Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Namespace extends Resource {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String path = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Namespace</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Namespace() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Namespace</code> with the specified <code>path</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  path               the namespace path.
   */
  public Namespace(final String path) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.path = path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>Namespace</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof Namespace) {
      final Namespace that = (Namespace)other;
      this.path = that.path;
      // ensure inheritance
      super.setRefid(reference);
    }
    else {
      final Object[] parameter = {reference.getRefId(), "namespace", reference.getRefId(), other.getClass().getName() };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter), getLocation());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPath
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>path</code>.
   **
   ** @param  path               the folder where the import of this set
   **                            has to be written to.
   */
  public void setPath(final String path) {
    if (path == null)
      AbstractInstance.handleAttributeMandatory("path");

    if (!path.startsWith("/"))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.METADATA_NAMESPACE_ABSOLUE, path));

    this.path = path;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the folder where the import of this set has to be written
   ** to.
   **
   ** @return                    the folder where the import of this set
   **                            has to be written to.
   */
  public final String path() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns the hash code value for this <code>Repository Object</code>.
   ** <p>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two instances
   ** <code>s1</code> and <code>s2</code>, as required by the general contract
   ** of <code>Object.hashCode()</code>.
   **
   ** @return                    the hash code value for this
   **                            <code>Repository Object</code>.
   */
  @Override
  public int hashCode() {
    return this.path.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Namespace</code> object that
   ** represents the same <code>path</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>Namespace</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>Namespace</code>s are equal;
   **                           <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other instanceof Namespace) {
      return (this.hashCode() == ((Namespace)other).hashCode());
    }

    final Namespace namespace = (Namespace)other;
    return this.path.equals(namespace.path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   */
  @Override
  public String toString() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (this.path == null)
      AbstractInstance.handleAttributeMissing("path");
  }
}