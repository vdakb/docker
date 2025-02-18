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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities

    File        :   ExportInstance.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ExportInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.spi.instance;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureConstant;
import oracle.iam.directory.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ExportInstance
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>ExportInstance</code> represents a data instance wrapper a
 ** <code>ExportHandler</code> use to control the spool file.
 ** <p>
 ** Subclasses of this classes providing the data model an implementation of
 ** <code>ExportHandler</code> needs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExportInstance extends FileInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String               description;

  private boolean              forceOverride  = false;
  private boolean              attributesOnly = false;

  private List<SearchInstance> search;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExportInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExportInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExportInstance</code> with the specified properties.
   **
   ** @param  file               the {@link File} where the the import has get
   **                            from.
   ** @param  format             the format descriptor one of
   **                            <ul>
   **                              <li>{@link FeatureConstant#FORMAT_LDIF}
   **                              <li>{@link FeatureConstant#FORMAT_DSML}
   **                              <li>{@link FeatureConstant#FORMAT_JSON}
   **                             </ul>
   ** @param  version             the version of the file format in the created
   **                             deployment.
   */
  public ExportInstance(final File file, final String format, final int version) {
    // ensure inheritance
    super(file, format, version);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportFile
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>exportFile</code>.
   **
   ** @param  file             the {@link File} where the export has to be
   **                          written to.
   */
  public void exportFile(final File file) {
    this.file = file;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description      the description in the created deployment.
   */
  public void description(final String description) {
    this.description = description;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description in the created deployment.
   **
   ** @return                    the description in the created deployment.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride      <code>true</code> to override the existing file
   **                            without to aks for user confirmation.
   */
  public void forceOverride(final boolean forceOverride) {
    this.forceOverride = forceOverride;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Returns the how to handle existing files.
   **
   ** @return                    <code>true</code> if the existing file will be
   **                            overridden without any further confirmation.
   */
  public final boolean forceOverride() {
    return this.forceOverride;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesOnly
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>attributesOnly</code>.
   **
   ** @param  attributesOnly     <code>true</code> to export attribute names
   **                            only.
   */
  public void attributesOnly(final boolean attributesOnly) {
    this.attributesOnly = attributesOnly;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   attributesOnly
  /**
   ** Returns <code>true</code> if attribute names only will be exported.
   ** to.
   **
   ** @return                    <code>true</code> to export attribute names
   **                            only.
   */
  public final boolean attributesOnly() {
    return this.attributesOnly;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Returns the {@link Collection} searches grouped by this export.
   **
   ** @return                    the {@link Collection} searches grouped by
   **                            this export.
   */
  public final Collection<SearchInstance> search() {
    return this.search;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified search context to the parameters that has to be applied.
   **
   ** @param  object             the {@link SearchInstance} to add.
   */
  public void add(final SearchInstance object) {
    if (this.search == null)
      this.search = new ArrayList<SearchInstance>();

    // add the instance to the object to handle
    this.search.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  public void validate()
    throws BuildException {

    if (this.search == null)
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_SEARCH));

    if (this.file == null)
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_MANDATORY));

    if (this.file.isDirectory())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_ISDIRECTORY));

    if (this.file.exists() && !this.file.canWrite())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.EXPORT_FILE_NOPERMISSION, this.file.getName()));
  }
}
