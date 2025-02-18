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

    File        :   Composite.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Composite.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.type;

import java.io.File;

import oracle.hst.deployment.ServiceException;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.DataType;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.CompositeInstance;

////////////////////////////////////////////////////////////////////////////////
// class Composite
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Composite</code> represents a workflow definition in Identity Manager
 ** that might be registered, enabled or disable after or during a deployment
 ** operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Composite extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final CompositeInstance delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Task
  // ~~~~~ ~~~~
  /**
   ** <code>Task</code> represents a Human Task definition in an Oracle SOA
   ** Composite that may need configuration.
   */
  public static class Task extends DataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final CompositeInstance.Task delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Task</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Task() {
      // ensure inheritance
      super();

      // initialize instance
      this.delegate = new CompositeInstance.Task();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setRefid
    /**
     ** Call by the ANT kernel to inject the argument for parameter refid.
     ** <p>
     ** Makes this instance in effect a reference to another <code>Task</code>
     ** instance.
     ** <p>
     ** You must not set another attribute or nest elements inside this element
     ** if you make it a reference.
     **
     ** @param  reference        the id of this instance.
     **
     ** @throws BuildException   if any other instance attribute is already set.
     */
    public void setRefid(final Reference reference)
      throws BuildException {

      if (!StringUtility.isEmpty(this.delegate.name()))
        throw tooManyAttributes();

      // ensure inheritance
      super.setRefid(reference);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   setName
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>name</code>.
     **
     ** @param  name               the name of the SOA composite human task.
     */
    public void setName(final String name) {
      checkAttributesAllowed();
      this.delegate.name(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: instance
    /**
     ** Returns the {@link CompositeInstance.Task} delegate of Identity Manager
     ** to handle.
     **
     ** @return                  the {@link CompositeInstance.Task} delegate of
     **                          Identity Manager.
     */
    protected final CompositeInstance.Task instance() {
      if (isReference())
        return ((Task)getCheckedRef()).instance();

      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredFlow
    /**
     ** Call by the ANT deployment to inject the argument for nested element
     ** <code>taskflow</code>.
     **
     ** @param  taskflow           the {@link Flow} definition to add.
     **
     ** @throws ServiceException   if the specified {@link Workflow} object is
     **                            already part of this operation.
     */
    public void addConfiguredFlow(final Flow taskflow)
      throws ServiceException {

      this.delegate.addFlow(taskflow.instance());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Flow
  // ~~~~~ ~~~~~
  /**
   ** <code>Flow</code> represents a taskflow definition in Identity Manager
   ** that might be added to a Human Task of a SOA composite during deployment
   ** operation.
   */
  public static class Flow extends DataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final CompositeInstance.Flow delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Flow</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Flow() {
      // ensure inheritance
      super();

      // initialize instance
      this.delegate = new CompositeInstance.Flow();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setRefid
    /**
     ** Call by the ANT kernel to inject the argument for parameter refid.
     ** <p>
     ** Makes this instance in effect a reference to another <code>Flow</code>
     ** instance.
     ** <p>
     ** You must not set another attribute or nest elements inside this element
     ** if you make it a reference.
     **
     ** @param  reference        the id of this instance.
     **
     ** @throws BuildException   if any other instance attribute is already set.
     */
    public void setRefid(final Reference reference)
      throws BuildException {

      if (!StringUtility.isEmpty(this.delegate.host()) || this.delegate.port() != -1 || !StringUtility.isEmpty(this.delegate.uri()))
        throw tooManyAttributes();

      // ensure inheritance
      super.setRefid(reference);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setApplication
    /**
     ** Called to inject the <code>application</code> of the task flow belonging
     ** to Identity Manager.
     **
     ** @param  application      the <code>application</code> of the task flow
     **                          belonging to Identity Manager.
     */
    public void setApplication(final String application) {
      checkAttributesAllowed();
      this.delegate.name(application);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: setHost
    /**
     ** Called to inject the <code>host</code> of the task flow belonging to
     ** Identity Manager.
     **
     ** @param  host             the <code>hostname</code> of the task flow
     **                          belonging to Identity Manager.
     */
    public void setHost(final String host) {
      checkAttributesAllowed();
      this.delegate.host(host);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: port
    /**
     ** Called to inject the <code>port</code> port of the task flow belonging
     ** to Identity Manager.
     **
     ** @param  port             the <code>http</code> port of the task flow
     **                          belonging to Identity Manager.
     */
    public void setPort(final int port) {
      checkAttributesAllowed();
      this.delegate.port(port);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setSsl
    /**
     ** Called to inject the <code>ssl</code> property if the <code>https</code>
     ** port is required instead of <code>http</code> to display the task flow
     ** related to Identity Manager.
     **
     ** @param  ssl              <code>true</code> if the <code>https</code>
     **                          port is required instead of <code>http</code>
     **                          to display the task flow related to Identity
     **                          Manager: otherwise <code>false</code>.
     */
    public void setSsl(final boolean ssl) {
      checkAttributesAllowed();
      this.delegate.ssl(ssl);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setUri
    /**
     ** Called to inject the <code>uri</code> of the task flow belonging to
     ** Identity Manager.
     **
     ** @param  uri              the <code>URI</code> of the task flow belonging
     **                          to Identity Manager.
     */
    public void setUri(final String uri) {
      checkAttributesAllowed();
      this.delegate.uri(uri);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: instance
    /**
     ** Returns the {@link CompositeInstance.Flow} delegate of Identity Manager
     ** to handle.
     **
     ** @return                  the {@link CompositeInstance.Flow} delegate of
     **                          Identity Manager.
     */
    public final CompositeInstance.Flow instance() {
      if (isReference())
        return ((Flow)getCheckedRef()).instance();

      return this.delegate;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Composite</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Composite() {
    // ensure inheritance
    this(new CompositeInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Partition</code> task that use the passed
   ** {@link CompositeInstance} as the value wrapper.
   **
   ** @param  instance           the {@link CompositeInstance} receiving the
   **                            instance properties.
   */
  protected Composite(final CompositeInstance instance) {
    // ensure inheritance
    super();

    // initialize instance
    this.delegate = instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
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

    if (!StringUtility.isEmpty(this.delegate.partition()) || !StringUtility.isEmpty(this.delegate.name()) || !StringUtility.isEmpty(this.delegate.revision()))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPartition
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>partition</code>.
   ** <p>
   ** The name of the partition in which to deploy the SOA composite
   ** application. The default value is <code>default</code>. If you do not
   ** specify a partition, the composite is automatically deployed into the
   ** default partition.
   **
   ** @param  partition          the name of the partition in which to deploy
   **                            the SOA composite application. The default
   **                            value is <code>default</code>.
   */
  public void setPartition(final String partition) {
    checkAttributesAllowed();
    this.delegate.partition(partition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the SOA composite application.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRevision
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>version</code>.
   **
   ** @param  revision           the revision ID of the composite related to
   **                            Oracle Identity Manager and/or Oracle SOA
   **                            Suite.
   */
  public void setRevision(final String revision) {
    checkAttributesAllowed();
    this.delegate.revision(revision);
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
    checkAttributesAllowed();
    this.delegate.location(location);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPlan
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>paln</code>.
   ** <p>
   ** This propery specifies the absolute path to the config plan to apply
   ** during the deployment of the composite.
   **
   ** @param  plan               the absolute path to the config plan to apply
   **                            during the deployment of the composite.
   */
  public void setPlan(final File plan) {
    checkAttributesAllowed();
    this.delegate.plan(plan);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverwrite
  /**
   ** Called to inject the <code>overwrite</code> of the composite related
   ** to Oracle SOA Suite.
   **
   ** @param  overwrite          the <code>overwrite</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public void setOverwrite(final boolean overwrite) {
    this.delegate.overwrite(overwrite);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceDefault
  /**
   ** Called to inject the <code>forceDefault</code> of the composite related
   ** to Oracle SOA Suite.
   ** <p>
   ** This property indicates whether to set the new composite as the default.
   ** <ul>
   **   <li><code>true</code> (default) - makes it the default composite.
   **   <li><code>false</code>          - does not make it the default
   **                                     composite.
   ** </ul>
   **
   ** @param  forceDefault       the <code>forceDefault</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public void forceDefault(final boolean forceDefault) {
    this.delegate.forceDefault(forceDefault);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link CompositeInstance} delegate of Identity Manager to
   ** handle.
   **
   ** @return                    the {@link CompositeInstance} delegate of
   **                            Identity Manager.
   */
  public CompositeInstance instance() {
    if (isReference())
      return ((Composite)getCheckedRef()).instance();

    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredTask
  /**
   ** Call by the ANT deployment to inject the argument for nested element
   ** <code>task</code>.
   **
   ** @param  task               the {@link Task} definition to add.
   **
   ** @throws ServiceException   if the specified {@link Task} object is already
   **                            part of this operation.
   */
  public void addConfiguredTask(final Task task)
    throws ServiceException {

    this.delegate.addHumanTask(task.instance());
  }
}