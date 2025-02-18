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

    File        :   Export.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Export.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceException;

import oracle.iam.identity.common.FeaturePlatformTask;

import oracle.iam.identity.common.spi.ObjectExport;

import oracle.iam.identity.deployment.type.ExportSet;

////////////////////////////////////////////////////////////////////////////////
// class Export
// ~~~~~ ~~~~~~
/**
 ** Exports Identity Manager objects to file.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 ** <p>
 ** The objects to be exported are can be listed in an input property file in
 ** the following format:
 ** <pre>
 **  [category1]=[object-names1]
 **  [category2]=[object-names2]
 ** </pre>
 ** If the Apache Ant task is used the object to be exported are specified by
 ** the nested element <code>object:category</code>. For example:
 ** <pre>
 **   &lt;object:category value="category1" name="names1"/&gt;
 **   &lt;object:category value="category2" name="names2"/&gt;
 ** </pre>
 ** Categories for Identity Manager 11g/12c are in alphabetical order:
 ** <ol>
 **   <li>AccessPolicy
 **   <li>DataObjectDef
 **   <li>CatalogDefinition <b>new</b> since <i>11.1.2.0</i>
 **   <li>CertificationDefinition <b>new</b> since <i>11.1.2.2</i>
 **   <li>CertificationConfiguration <b>new</b> since <i>11.1.2.2</i>
 **   <li>CustomResourceBundle <b>new</b> since <i>11.1.2.0</i>
 **   <li>EmailDef
 **   <li>EntityAdapter
 **   <li>EntityPublication <b>new</b> since <i>11.1.2.0</i>
 **   <li>ErrorCode
 **   <li>eventhandlers
 **   <li>GenericConnector
 **   <li>ITResource
 **   <li>ITResourceDef
 **   <li>Jar <b>new</b> since <i>11.1.2.0</i>
 **   <li>Job
 **   <li>Lookup
 **   <li>LOCALTEMPLATE
 **   <li>NOTIFICATIONTEMPLATE
 **   <li>OESPolicy
 **   <li>Organization
 **   <li>Org Metadata <b>new</b> since <i>11.1.2.0</i>
 **   <li>PasswordPolicy
 **   <li>Plugin <b>new</b> since <i>11.1.2.0</i>
 **   <li>PrepopAdapter
 **   <li>Process
 **   <li>Process Form
 **   <li>RequestDataset
 **   <li>RequestTemplate
 **   <li>Resource
 **   <li>Resource Form
 **   <li>RiskConfiguration <b>new</b> since <i>11.1.2.2</i>
 **   <li>Role and Orgs UDF
 **   <li>Role Metadata <b>new</b> since <i>11.1.2.0</i>
 **   <li>Rule
 **   <li>scheduleTasks
 **   <li>SystemProperties
 **   <li>TaskAdapter
 **   <li>Trigger
 **   <li>User Metadata <b>new</b> since <i>11.1.2.0</i>
 **   <li>User UDF <b>deprecated</b> since <i>11.1.2.0</i>
 **   <li>UserGroup
 **   <li>WorkFlowDefinition
 ** </ol>
 ** <b>Note</b>: The names of the categories are case-sensitive.
 ** <p>
 ** Example input property file:
 ** # Exports the Generic Directory Service task adapters
 ** TaskAdapter = adpGDS*
 ** #Exports the GDS prepopulate adapters
 ** PrepopAdapter = adpGDS*
 ** <p>
 ** Wild cards ('*') in object names are accepted.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Export extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the service provider executing the task operation */
  private final ObjectExport delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Export</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Export() {
    // ensure inheritance
    super();

    // initialize the service provider instance
    this.delegate = new ObjectExport(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setForceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride    <code>true</code> to override the existing file
   **                          without to aks for user confirmation.
   */
  public void setForceOverride(final boolean forceOverride) {
    this.delegate.forceOverride(forceOverride);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIndent
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>indent</code>.
   ** <p>
   ** If this will be evaluated to <code>true</code> the output will be
   ** indtended; otherwise the output is not intendet.
   **
   ** @param  indent           <code>true</code> to intend the output.
   */
  public void setIndent(final boolean indent) {
    this.delegate.indent(indent);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIndentNumber
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>indentNumber</code>.
   ** <p>
   ** The indent specifiies how many spaces will be used to indent a child node
   ** in the produced XML-file
   **
   ** @param  indentNumber     the number of spaces to indent a child node in
   **                          the created file.
   */
  public void setIndentNumber(final int indentNumber) {
    this.delegate.indentNumber(indentNumber);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredExportSet
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link ExportSet}.
   **
   ** @param  exportSet         the set of categories to export.
   **
   ** @throws BuildException     if the file the {@link ExportSet} or a
   **                            <code>Category</code> contained in the set is
   **                            already part of this export operation.
   */
  public void addConfiguredExportSet(final ExportSet exportSet)
    throws BuildException {

    this.delegate.addExportSet(exportSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
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

    this.delegate.execute(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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

    this.delegate.validate();

    // ensure inheritance
    super.validate();
  }
}