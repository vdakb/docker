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

    File        :   Deployment.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Deployment.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.task;

import java.io.File;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.task.AbstractCompositeTask;

import oracle.iam.identity.common.spi.CompositeHandler;

import oracle.iam.identity.workflow.type.Composite;

import org.apache.tools.ant.BuildException;

////////////////////////////////////////////////////////////////////////////////
// abstract class Deployment
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Base class to operate on the Workflow Repository of Oracle SOA Server.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Normaly the class will not be public. Unfortunately ANT has a problem in
 ** introspection if the class is package protected hence it will be exposed as
 ** public.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Deployment extends AbstractCompositeTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the service provider executing the task operation */
  private final CompositeHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Deployment</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Deployment() {
    // ensure inheritance
    super();

    // initialize the service provider instance
    this.handler = new CompositeHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPartition
  /**
   ** Sets the name of the partition of the composite.
   **
   ** @param  partition          the name of the partition of the composite.
   */
  public final void setPartition(final String partition) {
    this.handler.partition(partition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the name of the workflow.
   **
   ** @param  name               the name of the workflow.
   */
  public final void setName(final String name) {
    this.handler.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRevision
  /**
   ** Sets the version of the workflow.
   **
   ** @param  revision           the revision ID of the composite related to
   **                            Identity Manager.
   */
  public final void setRevision(final String revision) {
    this.handler.revision(revision);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>file</code>.
   ** <p>
   ** This propery specifies the absolute path to one the following:
   ** <ul>
   **   <li>SOA archive (SAR) file.
   **       <br>
   **       A SAR file is a special JAR file that requires a prefix of
   **       <code>sca_</code> (for example,
   **       <code>sca_HelloWorld_rev1.0.jar</code>). The SAR file can be
   **       deployed with the deployment commands, but a regular
   **       <code>.jar</code> file is not treated as a special SAR file.
   **   <li>ZIP file that includes multiple SARs, metadata archives (MARs), or
   **       both.
   **   <li>Enterprise archive (EAR) file that contains a SAR file.
   ** </ul>
   **
   ** @param  location           the packed archive of the composite related
   **                            to Oracle SOA Suite.
   */
  public void setLocation(final File location) {
    this.handler.location(location);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPlan
  /**
   ** Called to inject the <code>plan</code> of the composite related to Oracle
   ** SOA Suite.
   **
   ** @param  plan               the <code>plan</code> of the composite related
   **                            to Oracle SOA Suite.
   */
  public void setPlan(final File plan) {
    this.handler.plan(plan);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverwrite
  /**
   ** Called to inject the <code>overwrite</code> of the composite related
   ** to Oracle SOA Suite.
   ** <p>
   ** This property indicates whether to overwrite an existing SOA composite
   ** application file.
   ** <ul>
   **   <li><code>false</code> (default) - does not overwrite the file.
   **   <li><code>true</code>            - overwrites the file.
   ** </ul>
   **
   ** @param  overwrite          the <code>overwrite</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public void setOverwrite(final boolean overwrite) {
    this.handler.overwrite(overwrite);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setForceDefault
  /**
   ** Called to inject the <code>forceDefault</code> of the composite related
   ** to Oracle SOA Suite.
   **
   ** @param  forceDefault       the <code>forceDefault</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public void setForceDefault(final boolean forceDefault) {
    this.handler.forceDefault(forceDefault);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredComposite
  /**
   ** Call by the ANT deployment to inject the argument for nested element
   ** <code>composite</code>.
   **
   ** @param  composite          the {@link Composite} definition to add.
   **
   ** @throws ServiceException   if the specified {@link Composite} object is
   **                            already part of this operation.
   */
  public void addConfiguredComposite(final Composite composite)
    throws ServiceException {

    this.handler.addInstance(composite.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    this.handler.validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deploy
  /**
   ** Deploys all composites to Oracle SOA Suite.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  protected void deploy()
    throws ServiceException {

    this.handler.deploy(this.server());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   undeploy
  /**
   ** Undeploys all composites from Oracle SOA Suite.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  protected void undeploy()
    throws ServiceException {

    this.handler.undeploy(this.server());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redeploy
  /**
   ** Redeploys all composites to Oracle SOA Suite.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  protected void redeploy()
    throws ServiceException {

    this.handler.redeploy(this.server());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures all composites in Oracle SOA Suite.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  protected void configure()
    throws ServiceException {

    this.handler.configure(this.server());
  }
}