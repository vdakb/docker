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

    File        :   Generate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Generate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.task;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.type.MDSServerContext;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureException;
import oracle.iam.identity.common.FeaturePlatformTask;

import oracle.iam.identity.common.spi.SandboxGenerator;

import oracle.iam.identity.sandbox.type.Instance;

import org.apache.tools.ant.types.Reference;

////////////////////////////////////////////////////////////////////////////////
// class Generate
// ~~~~~ ~~~~~~~~
/**
 ** Generates a sandbox to be published to Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Generate extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final SandboxGenerator generator;
  
  /**
   ** the MDS coonection required to populate the catalog application module
   ** from the metadata service
   */
  protected MDSServerContext       context = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Generate</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Generate() {
    // ensure inheritance
    super();

    // call the factory method of the subclasses initialize the provider
    // instance
    this.generator = new SandboxGenerator(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCatalogPath
  /**
   ** Sets the path to the catalog application module.
   **
   ** @param  path              the path to the catalog application module.
   */
  public final void setCatalogPath(final File path) {
    this.generator.path(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setReset
  /**
   ** Sets the flag to reset the catalog application module.
   ** <p>
   ** An initalized catalog application module looks like
   ** <pre>
   **   &lt;?xml version='1.0' encoding='ISO-8859-1'?&gt;
   **   &lt;PDefApplicationModule xmlns="http://xmlns.oracle.com/bc4j" Name="CatalogAM"/&gt;
   ** </pre>
   ** There is no view usage registered at this module.
   **
   ** @param  value            the value to set for resetting the catalog
   **                          application module.
   **                          <br>
   **                          Default: <code>false</code>.
   */
  public final void setReset(final boolean value) {
    this.generator.reset(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMetadataRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>context</code> as a {@link Reference} to a declared
   ** {@link MDSServerContext} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted to a
   **                            {@link MDSServerContext}.
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link MDSServerContext}.
   */
  public void setMetadataRef(final Reference reference)
    throws BuildException {

    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof MDSServerContext))
      handleReferenceError(reference, MDSServerContext.CONTEXT_TYPE, object.getClass());

    this.context = (MDSServerContext)object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   MDSContext
  /**
   ** Returns the metadata service context object of this task.
   **
   ** @return                    the metadata service context object of this
   **                            task.
   */
  protected final MDSServerContext MDSContext() {
    return this.context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (overridden)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  public void onExecution()
    throws ServiceException {

    // final check if we are connected so far
    if (!this.server.established())
      throw new FeatureException(FeatureError.METADATA_INSTANCE_ERROR);

    this.generator.execute(this, this.context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredInstance
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Instance}.
   **
   ** @param  instance           the sandbox to generate.
   **
   ** @throws BuildException     if the {@link Instance} already contained in
   **                            the collection of this generation operation.
   */
  public void addConfiguredInstance(final Instance instance)
    throws BuildException {

    this.generator.addSandboxInstance(instance.delegate());
  }
}