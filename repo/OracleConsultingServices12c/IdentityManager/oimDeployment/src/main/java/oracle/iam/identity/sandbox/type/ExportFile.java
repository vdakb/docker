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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Sandbox Service Utilities 11g

    File        :   ExportFile.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ExportFile.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ExportFile
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>ExportFile</code> is a special {@link ImportFile} that will be export
 ** a <code>Sandbox</code> from Identity Manager to the local file system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExportFile extends ImportFile {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Sandbox sandbox = new Sandbox();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExportFile</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExportFile() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSandbox
  /**
   ** Sets the name of the sandbox to export.
   **
   ** @param  sandbox            the name of the sandbox to export.
   */
  public void setSandbox(final String sandbox) {
    // prevent bogus input
    checkAttributesAllowed();

    if (StringUtility.isEmpty(sandbox))
      throw new BuildException(FeatureResourceBundle.string(FeatureError.SANDBOX_MANDATORY));

    this.sandbox.setName(sandbox);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sandbox
  /**
   ** Returns the nested {@link Sandbox} to handle.
   **
   ** @return                    the nested {@link Sandbox} to handle.
   */
  public String sandbox() {
    return this.sandbox.name();
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
    return this.sandbox.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>ExportFile</code> object that represents
   ** the same <code>sandbox</code>  as this instance.
   **
   ** @param other               the object to compare this
   **                            <code>ExportFile</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>ExportFile</code>s sandbox name are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof ExportFile))
      return false;

    final ExportFile another = (ExportFile)other;
    return this.sandbox.equals(another.sandbox);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to perform.
   **
   ** @throws BuildException   in case an error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    // ensure inheritance
    super.validate();

    if (StringUtility.isEmpty(this.sandbox.name()))
      throw new BuildException(FeatureResourceBundle.string(FeatureError.SANDBOX_MANDATORY));
  }
}