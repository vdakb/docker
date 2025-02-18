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

    File        :   CompositeInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CompositeInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.bpel.services.workflow.IWorkflowConstants;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.spi.AbstractInstance;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class CompositeInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>CompositeInstance</code> represents the common parts a composite in
 ** Identity Manager and a Composite in Oracle SOA Suite that might be
 ** registered, enabled, disabled, deployed, undeployed after or during an
 ** import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CompositeInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String partition = IWorkflowConstants.DEFAULT_PARTITION_NAME;
  private String revision = null;

  private String canonical = null;

  /** a single file to deploy. */
  private File location = null;
  private File plan = null;

  private boolean keepOnRedeploy = false;
  private boolean overwrite = false;
  private boolean forceDefault = true;

  private List<Task> humanTask = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Flow
  // ~~~~~ ~~~~
  /**
   ** <code>Flow</code> represents a task flow in Identity Manager that might be
   ** assinged to a Human Task of a SOA Composite during a deployment operation.
   */
  public static class Flow extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String  host = null;
    private int     port = -1;
    private boolean ssl = false;
    private String  uri = null;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Flow</code> with the specified name JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Flow() {
      // ensure inheritance
      this(IWorkflowConstants.WORKLIST_TASK_DISPLAY_APP);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Flow</code> with the specified name.
     **
     ** @param  name             the value set for the name property.
     */
    public Flow(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   host
    /**
     ** Called to inject the <code>host</code> of the workflow display related
     ** to Identity Manager.
     **
     ** @param  host             the <code>host</code> of the task flow
     **                          display related to Identity Manager.
     */
    public void host(final String host) {
      this.host = host;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   host
    /**
     ** Returns the <code>host</code> of the task flow display related to
     ** Identity Manager.
     **
     ** @return                  the <code>host</code> of the task flow
     **                          display related to Identity Manager.
     */
    public final String host() {
      return this.host;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   port
    /**
     ** Called to inject the <code>port</code> of the task flow display related
     ** to Identity Manager.
     **
     ** @param  port             the <code>http</code> port of the task flow
     **                          display related to Identity Manager.
     */
    public void port(final int port) {
      this.port = port;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   port
    /**
     ** Returns the <code>port</code> of the task flow display related to
     ** Identity Manager.
     **
     ** @return                  the <code>http</code> port of the task flow
     **                          display related to Identity Manager.
     */
    public final int port() {
      return this.port;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   ssl
    /**
     ** Called to inject the <code>ssl</code> of the task flow display related
     ** to Identity Manager.
     **
     ** @param  ssl              <code>true</code> if the <code>https</code>
     **                          port is required instead of <code>http</code>
     **                          to display the task flow related to Oracle
     **                          Identity Manager: otherwise <code>false</code>.
     */
    public void ssl(final boolean ssl) {
      this.ssl = ssl;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   ssl
    /**
     ** Returns <code>true</code> if the <code>https</code> port is required
     ** instead of <code>http</code> to display the task flow related to
     ** Identity Manager.
     **
     ** @return                  <code>true</code> if the <code>https</code>
     **                          port is required instead of <code>http</code>
     **                          to display the task flow related to Identity
     **                          Manager: otherwise <code>false</code>.
     */
    public final boolean ssl() {
      return this.ssl;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   uri
    /**
     ** Called to inject the <code>uri</code> of the task flow display related
     ** to Identity Manager.
     **
     ** @param  uri              the <code>uri</code> of the task flow display
     **                          related to Identity Manager.
     */
    public void uri(final String uri) {
      this.uri = uri;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   uri
    /**
     ** Returns the <code>uri</code> of the task flow display related to
     ** Identity Manager.
     **
     ** @return                  the <code>uri</code> of the task flow display
     **                          related to Identity Manager.
     */
    public final String uri() {
      return this.uri;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate (overriden)
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException   in case the instance does not meet the
     **                          requirements.
     */
    @Override
    public void validate()
      throws BuildException {

      // ensure inheritance
      super.validate();

      if (StringUtility.isEmpty(this.host))
        handleAttributeMissing("host");

      if (this.port == -1)
        handleAttributeMissing("port");

      if (StringUtility.isEmpty(this.uri))
        handleAttributeMissing("uri");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Task
  // ~~~~~ ~~~~
  /**
   ** <code>Task</code> represents a Human Task definition in an Oracle SOA
   ** Composite that may need configuration.
   */
  public static class Task extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private List<Flow> flow = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Task</code> with the specified name JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Task() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Task</code> with the specified name.
     **
     ** @param  name             the value set for the name property.
     */
    public Task(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: taskFlow
    /**
     ** Returns the {@link List} of <code>Task Flow</code>s of the
     ** <code>Human Task</code> related to Oracle SOA Suite.
     **
     ** @return                  the {@link List} of <code>Task Flow</code>s of
     **                          the <code>Human Task</code> related to Oracle
     **                          SOA Suite.
     */
    public final List<Flow> taskFlow() {
      return this.flow;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   addFlow
    /**
     ** Add the specified value pair to the parameters that has to be applied
     ** after a deployment operation.
     **
     ** @param  flow          the {@link Flow} to be assigned.
     */
    public void addFlow(final Flow flow) {

      // prevent bogus input
      if (flow == null)
        handleAttributeMissing("flow");

      if (this.flow == null)
        this.flow = new ArrayList<Flow>();

      // check if we have this task flow already
      if (this.flow.contains(flow)) {
        throw new BuildException();
      }

      this.flow.add(flow);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate (overriden)
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException   in case the instance does not meet the
     **                          requirements.
     */
    @Override
    public void validate()
      throws BuildException {

      // ensure inheritance
      super.validate();

      if (!CollectionUtility.empty(this.flow))
        for (Flow flow : this.flow)
          flow.validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CompositeInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CompositeInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Called to inject the <code>partition</code> of the composite related to
   ** Identity Manager and/or SOA Suite.
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
  public void partition(final String partition) {
    this.partition = partition;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the <code>partition</code> of the composite related to Identity
   ** Manager and/or SOA Suite.
   ** <p>
   ** The name of the partition in which to deploy the SOA composite
   ** application. The default value is <code>default</code>. If you do not
   ** specify a partition, the composite is automatically deployed into the
   ** default partition.
   **
   ** @return                    the name of the partition in which to deploy
   **                            the SOA composite application. The default
   **                            value is <code>default</code>.
   */
  public final String partition() {
    return this.partition;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revision
  /**
   ** Called to inject the <code>revision</code> of the composite related to
   ** Identity Manager and/or Oracle SOA Suite.
   **
   ** @param  revision           the revision ID of the composite related to
   **                            Identity Manager and/or Oracle SOA Suite.
   */
  public void revision(final String revision) {
    this.revision = revision;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   revision
  /**
   ** Returns the <code>revision</code> of the composite related to Identity
   ** Manager and/or Oracle SOA Suite.
   **
   ** @return                    the revision ID of the composite related to
   **                            Identity Manager and/or Oracle SOA
   **                            Suite.
   */
  public final String revision() {
    return this.revision;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Called to inject the <code>file</code> of the composite related to Oracle
   ** SOA Suite.
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
  public void location(final File location) {
    // we cannot allow to deploy a complete directory
    if (location.isDirectory())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_FILE_ISDIRECTORY, location.getAbsolutePath()));

    // check if we are able to deploy the file
    if (!location.exists())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_FILE_NOTEXISTS, location.getAbsolutePath()));

    // we need at least read permissions on the file to add
    if (!location.canRead())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_FILE_NOPERMISSION, location.getAbsolutePath()));

    this.location = location;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Returns the <code>file</code> of the composite related to Oracle SOA
   ** Suite.
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
   ** @return                    the <code>file</code> of the composite related
   **                            to Oracle SOA Suite.
   */
  public final File location() {
    return this.location;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   plan
  /**
   ** Called to inject the <code>plan</code> of the composite related to Oracle
   ** SOA Suite.
   ** <p>
   ** This property specifies the absolute path of a configuration plan to be
   ** applied to a specified SAR file or to all SAR files included in the ZIP
   ** file.
   **
   ** @param  plan               the <code>plan</code> of the composite related
   **                            to Oracle SOA Suite.
   */
  public void plan(final File plan) {
    this.plan = plan;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   plan
  /**
   ** Returns the <code>plan</code> of the composite related to Oracle SOA
   ** Suite.
   ** <p>
   ** This property specifies the absolute path of a configuration plan to be
   ** applied to a specified SAR file or to all SAR files included in the ZIP
   ** file.
   **
   ** @return                    the <code>plan</code> of the composite related
   **                            to Oracle SOA Suite.
   */
  public final File plan() {
    return this.plan;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   overwrite
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
  public void overwrite(final boolean overwrite) {
    this.overwrite = overwrite;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   overwrite
  /**
   ** Returns the <code>overwrite</code> of the composite related to Oracle
   ** SOA Suite.
   ** <p>
   ** This property indicates whether to overwrite an existing SOA composite
   ** application file.
   ** <ul>
   **   <li><code>false</code> (default) - does not overwrite the file.
   **   <li><code>true</code>            - overwrites the file.
   ** </ul>
   **
   ** @return                    the <code>overwrite</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public final boolean overwrite() {
    return this.overwrite;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keepOnRedeploy
  /**
   ** Called to inject the <code>keepOnRedeploy</code> of the composite related
   ** to Oracle SOA Suite.
   **
   ** @param  keepOnRedeploy     the <code>keepOnRedeploy</code> of the
   **                            composite related to Oracle SOA Suite.
   */
  public void keepOnRedeploy(final boolean keepOnRedeploy) {
    this.keepOnRedeploy = keepOnRedeploy;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   keepOnRedeploy
  /**
   ** Returns the <code>keepOnRedeploy</code> of the composite related to Oracle
   ** SOA Suite.
   **
   ** @return                    the <code>keepOnRedeploy</code> of the
   **                            composite related to Oracle SOA Suite.
   */
  public final boolean keepOnRedeploy() {
    return this.keepOnRedeploy;
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
    this.forceDefault = forceDefault;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   forceDefault
  /**
   ** Returns the <code>forceDefault</code> of the composite related to Oracle
   ** SOA Suite.
   ** <p>
   ** This property indicates whether to set the new composite as the default.
   ** <ul>
   **   <li><code>true</code> (default) - makes it the default composite.
   **   <li><code>false</code>          - does not make it the default
   **                                     composite.
   ** </ul>
   **
   ** @return                    the <code>forceDefault</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public final boolean forceDefault() {
    return this.forceDefault;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   humanTask
  /**
   ** Returns the {@link List} of <code>Human Task</code>s of the composite
   ** related to Oracle SOA Suite.
   **
   ** @return                    the {@link List} of <code>Human Task</code>s of
   **                            the composite related to Oracle SOA Suite.
   */
  public final List<Task> humanTask() {
    return this.humanTask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of the object.
   ** <p>
   ** In general, the <code>toString</code> method returns a string that
   ** "textually represents" this object. The result is a concise but
   ** informative representation that is easy for a person to read.
   **
   ** @return                    the <code>canonical</code> name of the composite
   **                            related to Identity Manager and/or
   **                            Oracle SOA Suite.
   */
  @Override
  public String toString() {
    if (StringUtility.isEmpty(this.canonical))
      this.canonical = String.format("%s/%s!%s", this.partition, this.name(), this.revision);

    return this.canonical;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>CompositeInstance</code> object that
   ** represents the same <code>canonical</code> name as this instance.
   **
   ** @param other               the object to compare this
   **                            <code>CompositeInstance</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>CompositeInstance</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof CompositeInstance))
      return false;

    final CompositeInstance another = (CompositeInstance)other;
    return this.toString().equals(another.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overriden)
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate()
    throws BuildException {

    // ensure inheritance
    super.validate();

    if (StringUtility.isEmpty(this.partition))
      handleAttributeMissing("partition");

    if (StringUtility.isEmpty(this.revision))
      handleAttributeMissing("revision");

    if (!CollectionUtility.empty(this.humanTask)) {
      for (Task task : this.humanTask)
        task.validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addHumanTask
  /**
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  task               the {@link Task} where a configuration is
   **                            requested for.
   */
  public void addHumanTask(final Task task) {
    // prevent bogus input
    if (task == null)
      handleAttributeMissing("task");

    if (this.humanTask == null)
      this.humanTask = new ArrayList<Task>();

    // check if we have this task already
    if (this.humanTask.contains(task)) {
      throw new BuildException();
    }

    this.humanTask.add(task);
  }
}