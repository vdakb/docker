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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   Workflow.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Workflow.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.model;

import oracle.ide.Ide;

import oracle.javatools.data.HashStructure;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.oim.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class Workflow
// ~~~~~ ~~~~~~~~
/**
 ** The model to support the configuration wizard panel for creating the
 ** OIM Approval Workflows.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Workflow extends Composite {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String FILE                = "oim-workflow-ant";
  private static final String TEMPLATE            = Manifest.BUILDFILE_PACKAGE + "." + FILE;

  private static final String DEFAULT_FILE        = FILE + "." + Manifest.CONFIG_FILE_TYPE;
  private static final String DEFAULT_APPLICATION = "ocs-workflow";

  private static final String DEFAULT_PARTITION   = "default";
  private static final String DEFAULT_CATEGORY    = "Approval";
  private static final String DEFAULT_PROVIDER    = "BPEL";
  private static final String DEFAULT_OPERATION   = "process";
  private static final String DEFAULT_PAYLOAD     = "payload";
  private static final String DEFAULT_PLAN        = "";

  private static final String NAMESPACE           = "namespace";
  private static final String PARTITION           = "partition";
  private static final String CATEGORY            = "category";
  private static final String PROVIDER            = "provider";
  private static final String OPERATION           = "operation";
  private static final String PAYLOAD             = "payload";
  private static final String PLAN                = "plan";
  private static final String PLAN_OVERWRITE      = "overwrite";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Workflow</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Workflow()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  public Workflow(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   file (overridden)
  /**
   ** Returns the file name tag used by the component configuration to store the
   ** bulld file in the local filesystem.
   **
   ** @return                    the file name tag used by the component
   **                            configuration to store the bulld file in the
   **                            local filesystem.
   */
  @Override
  public String file() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(FILE, DEFAULT_FILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   project (overridden)
  /**
   ** Returns the project name tag used by the component configuration.
   **
   ** @return                    the project name tag used by the component
   **                            configuration.
   */
  @Override
  public String project() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PROJECT, Manifest.string(Manifest.WORKFLOW_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationNamespace
  /**
   ** Sets the name of the workspace used to classify the namespace of the
   ** composite to create.
   **
   ** @param  namespace          the name of the workspace used to classify the
   **                            namespace of the composite to create.
   */
  public final void applicationNamespace(final String namespace) {
    this._hash.putString(NAMESPACE, namespace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationNamespace
  /**
   ** Returns the name of the workspace used to classify the namespace of the
   ** composite to create.
   **
   ** @return                    the name of the workspace used to classify the
   **                            namespace of the composite to create.
   */
  public String applicationNamespace() {
    return this._hash.getString(NAMESPACE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Sets the name of the partition the workflow will be deployed on Oracle
   ** SOA server.
   **
   ** @param  partition          the name of the partition the workflow will be
   **                            deployed in Oracle SOA server.
   */
  public void partition(final String partition) {
    this._hash.putString(PARTITION, partition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the name of the partition the workflow will be deployed on Oracle
   ** SOA server.
   **
   ** @return                    the name of the partition the workflow will be
   **                            deployed in Oracle SOA server.
   */
  public String partition() {
    return string(PARTITION, DEFAULT_PARTITION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Sets the category of the workflow to be deployed in Oracle SOA server.
   **
   ** @param  category           the category of the workflow to be deployed in
   **                            Oracle SOA server.
   */
  public void category(final String category) {
    this._hash.putString(CATEGORY, category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the category of the workflow to be deployed in Oracle SOA server.
   **
   ** @return                    the category of the workflow to be deployed in
   **                            Oracle SOA server.
   */
  public String category() {
    return string(CATEGORY, DEFAULT_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Sets the provider of the workflow to be deployed in Oracle SOA server.
   **
   ** @param  provider           the provider of the workflow to be deployed in
   **                            Oracle SOA server.
   */
  public void provider(final String provider) {
    this._hash.putString(PROVIDER, provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the provider of the workflow to be deployed in Oracle SOA server.
   **
   ** @return                    the provider of the workflow to be deployed in
   **                            Oracle SOA server.
   */
  public String provider() {
    return string(PROVIDER, DEFAULT_PROVIDER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Sets the operation of the workflow to be deployed in Oracle SOA server.
   **
   ** @param  operation          the operation of the workflow to be deployed in
   **                            Oracle SOA server.
   */
  public void operation(final String operation) {
    this._hash.putString(OPERATION, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Returns the operation of the workflow to be deployed in Oracle SOA server.
   **
   ** @return                    the operation of the workflow to be deployed in
   **                            Oracle SOA server.
   */
  public String operation() {
    return string(OPERATION, DEFAULT_OPERATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payload
  /**
   ** Sets the payload of the workflow to be deployed in Oracle SOA server.
   **
   ** @param  payload            the payload of the workflow to be deployed in
   **                            Oracle SOA server.
   */
  public void payload(final String payload) {
    this._hash.putString(PAYLOAD, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payload
  /**
   ** Returns the payload of the workflow to be deployed in Oracle SOA server.
   **
   ** @return                    the payload of the workflow to be deployed in
   **                            Oracle SOA server.
   */
  public String payload() {
    return string(PAYLOAD, DEFAULT_PAYLOAD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   plan
  /**
   ** Sets the plan to deployed the workflow in Oracle SOA server.
   **
   ** @param  plan               the plan to deployed the workflow in Oracle SOA
   **                            server.
   */
  public void plan(final String plan) {
    this._hash.putString(PLAN, plan);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   plan
  /**
   ** Returns the plan to deployed the workflow in Oracle SOA server.
   **
   ** @return                    the plan to deployed the workflow in Oracle SOA
   **                            server.
   */
  public String plan() {
    return string(PLAN, DEFAULT_PLAN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   planOverwrite
  /**
   ** Sets the plan to deployed the workflow in Oracle SOA server.
   **
   ** @param  overwrite          the plan to deployed the workflow in Oracle SOA
   **                            server.
   */
  public void plan(final boolean overwrite) {
    this._hash.putBoolean(PLAN_OVERWRITE, overwrite);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   planOverwrite
  /**
   ** Returns the plan to deployed the workflow in Oracle SOA server.
   **
   ** @return                    the plan to deployed the workflow in Oracle SOA
   **                            server.
   */
  public boolean planOverwrite() {
     return this._hash.getBoolean(PLAN_OVERWRITE, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template (BuildPropertyAdapter)
  /**
   ** Returns the template of this descriptor the generation is based on.
   **
   ** @return                    the template of this descriptor the generation
   **                            is based on.
   */
  @Override
  public final String template() {
    return TEMPLATE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application (BuildArtifact)
  /**
   ** Returns the application tag for the project.
   **
   ** @return                    the application tag for the project.
   */
  @Override
  public String application() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(APPLICATION, DEFAULT_APPLICATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link Composite}.
   ** <p>
   ** This method tries to find an existing instance of a
   ** {@link Composite} using {@link Manifest#WORKFLOW_COMPOSITE} in the design
   ** time objects of the specified {@link TraversableContext}.
   ** <p>
   ** If none can be found, this method will create a new {@link HashStructure},
   ** first attempting to wire that into the specified name in the
   ** {@link TraversableContext} or, failing that, leave the new
   ** {@link HashStructure} disconnected.
   ** <p>
   ** This method is guaranteed to return a <code>non-null</code>.
   ** Factory method takes a <code>PropertyStorage</code> (instead of
   ** {@link HashStructure} directly). This decouples the origin of the
   ** {@link HashStructure} and allows the future possibility of resolving
   ** preferences through multiple layers of {@link HashStructure}.
   ** <p>
   ** Classes/methods that currently implement/return PropertyStorage:
   ** <ul>
   **   <li>oracle.ide.config.Preferences
   **   <li>oracle.ide.model.Project
   **   <li>oracle.ide.model.Workspace
   **   <li>oracle.ide.panels.TraversableContext.getPropertyStorage()
   ** </ul>
   **
   ** @param  context            the data provider to initialize the instance.
   **
   ** @return                    the <code>Composite</code> instance obtained
   **                            from the specifed {@link TraversableContext}.
   */
  public static Workflow instance(final TraversableContext context) {
    Workflow instance = (Workflow)context.getDesignTimeObject(Manifest.WORKFLOW_COMPOSITE);
    if (instance == null) {
      instance = new Workflow(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.WORKFLOW_COMPOSITE, instance);
      // create a namespace context that is suffient for namespace declaration
      // in SOA Composite
      // the namespace context must not contains any whitespace or other special
      // characters like spaces, dots or hyphens etc.
      instance.applicationNamespace(Ide.getActiveWorkspace().getShortLabel().replaceAll("[\\s\\-()\\.+]", "_"));
    }
    return instance;
  }
}