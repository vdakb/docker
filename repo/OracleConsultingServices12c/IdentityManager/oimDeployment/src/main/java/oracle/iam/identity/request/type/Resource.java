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

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Resource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import java.io.File;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Resource
// ~~~~~ ~~~~~~~~
/**
 ** <code>Resource</code> encapsulate a Identity Manager
 ** <code>Request DataSet</code> which belongs to a
 ** <code>Resource Object</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resource extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              name;

  /**
   ** A valid absolute path to a directory in the file system to which the
   ** selected documents will be exported. This location must be accessible from
   ** the local machine where this task is started.
   */
  private File                folder;

  private final List<Process> process = new ArrayList<Process>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resource() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the <code>Resource Object</code> in
   **                            Identity Manager.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Resource Object</code> instance of Identity
   ** Manager.
   **
   ** @return                    the name of the <code>Resource Object</code>
   **                            in Identity Manager.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFolder
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>datasetFolder</code>.
   **
   ** @param  folder             the path of the file where the generated files
   **                            has to be written to.
   */
  public void setFolder(final File folder) {
    this.folder = folder;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   folder
  /**
   ** Returns the {@link File} where the export of this set has to be written
   ** to.
   **
   ** @return                    the path of the file where the generated files
   **                            has to be written to.
   */
  public final File folder() {
    return this.folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processIterator
  /**
   ** Returns the processes assigned to this <code>Resource Object</code>
   ** in Identity Manager.
   **
   ** @return                    the processes assigned to this
   **                            <code>Resource Object</code> in Identity
   **                            Manager.
   */
  public List<Process> process() {
    return this.process;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processIterator
  /**
   ** Returns the processes assigned to this <code>Resource Object</code> in
   ** Identity Manager as an {link Iterator}.
   **
   ** @return                    the processes assigned to this
   **                            <code>Resource Object</code> in Identity
   **                            Manager as an {link Iterator}.
   */
  public Iterator<Process> processIterator() {
    return this.process.iterator();
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
   ** <code>null</code> and is a <code>Resource</code> object that
   ** represents the same <code>name</code> as this instance.
   **
   ** @param other               the object to compare this
   **                            <code>Resource</code> against.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Resource</code>s are
   **                            equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Resource))
      return false;

    final Resource another = (Resource)other;
    return this.name.equals(another.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: validate (overridden)
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** <b>Note:</b>
   ** We are not calling the validation method on the super class to prevent
   ** the validation of the <code>parameter</code> mapping.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (StringUtility.isEmpty(this.name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "name"));

    if (!this.process.isEmpty()) {
      Iterator<Process> i = processIterator();
      while (i.hasNext())
        i.next().validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredProcess
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Process}.
   **
   ** @param  process            the subject of substitution.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Process</code>
   */
  public void addConfiguredProcess(final Process process)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    if (this.process.contains(process))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_PROCESS_ONLYONCE, name));

    this.process.add(process);
  }
}