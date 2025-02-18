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

    File        :   Instance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Instance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.type;

import java.io.File;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.common.spi.SandboxInstance;

////////////////////////////////////////////////////////////////////////////////
// class Instance
// ~~~~~ ~~~~~~~
/**
 ** <code>Instance</code> defines the attribute restriction on values that can
 ** be passed as a nested parameter used for sandbox generation information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Instance extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final SandboxInstance delegate = new SandboxInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Sandbox</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Instance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another <code>Sandbox</code>
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

    if (!StringUtility.isEmpty(this.delegate.name()) || !StringUtility.isEmpty(this.delegate.resource()))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPath
  /**
   ** Sets the abstract working {@link File}.
   **
   ** @param  path               the abstract working {@link File}.
   */
  public final void setPath(final File path) {
    // prevent bogus input
    checkAttributesAllowed();

    if (path.isFile())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.SANDBOX_DIRECTORY_ISFILE));

    this.delegate.path(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>version</code>.
   **
   ** @param  value              the version of the sandbox in Identity Manager
   **                            to handle.
   */
  public void setVersion(final String value) {
    checkAttributesAllowed();
    this.delegate.version(SandboxInstance.Version.from(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the sandbox in Identity Manager to
   **                            handle.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description        the description of the sandbox in Identity
   **                            Manager to handle.
   */
  public void setDescription(final String description) {
    checkAttributesAllowed();
    this.delegate.description(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDataSet
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>dataSet</code>.
   **
   ** @param  name               the name of the <code>Request DataSet</code>
   **                            to generate the form based on the
   **                            <code>Process Form</code>.
   */
  public void setDataSet(final String name) {
    checkAttributesAllowed();
    this.delegate.dataSet(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResource
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>resource</code>.
   **
   ** @param  name               the name of the <code>Resource Object</code>
   **                            which will be used to investigate the
   **                            <code>Process Form</code> the form to generate
   **                            belongs to.
   */
  public void setResource(final String name) {
    checkAttributesAllowed();
    this.delegate.resource(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanup
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>cleanup</code>.
   **
   ** @param  cleanup           <code>true</code> to delete the working directory.
   */
  public void setCleanup(final boolean cleanup) {
    checkAttributesAllowed();
    this.delegate.cleanup(cleanup);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minor
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>minor</code>.
   **
   ** @param  minor           <code>int</code> to delete the working directory.
   */
  public void setMinor(final int minor) {
    checkAttributesAllowed();
    this.delegate.minor(minor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride      <code>true</code> to override the existing
   **                            files without to aks for user confirmation.
   */
  public void setForceOverride(final String forceOverride) {
    checkAttributesAllowed();
    this.delegate.forceOverride(forceOverride);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link SandboxInstance} this ANT type wrappes.
   **
   ** @return                    the {@link SandboxInstance} this ANT type
   **                            wrappes.
   */
  public SandboxInstance delegate() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredBundle
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Bundle}.
   **
   ** @param  instance           the sandbox to generate.
   **
   ** @throws BuildException     if the {@link Instance} already contained in
   **                            the collection of this generation operation.
   */
  public void addConfiguredBundle(final Bundle instance)
    throws BuildException {

    this.delegate.add(instance.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAccount
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Account}.
   **
   ** @param  instance           the sandbox to generate.
   **
   ** @throws BuildException     if the {@link Instance} already contained in
   **                            the collection of this generation operation.
   */
  public void addConfiguredAccount(final Account instance)
    throws BuildException {

    this.delegate.account(instance.delegate());
  }
}