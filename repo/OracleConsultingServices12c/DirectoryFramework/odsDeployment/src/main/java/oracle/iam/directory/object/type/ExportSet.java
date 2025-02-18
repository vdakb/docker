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

    File        :   ExportSet.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ExportSet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.object.type;

import java.io.File;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.directory.common.FeatureConstant;

import oracle.iam.directory.common.type.Search;


import oracle.iam.directory.common.spi.instance.ExportInstance;

////////////////////////////////////////////////////////////////////////////////
// class ExportSet
// ~~~~~ ~~~~~~~~~
/**
 ** <code>ExportSet</code> is a group of attributes that will be exported to the
 ** same file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExportSet extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final private ExportInstance delegate = new ExportInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExportSet</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExportSet(){
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>ExportSet</code> instance.
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

    if (!StringUtility.isEmpty(this.delegate.description()) || this.delegate.file() != null || this.delegate.search() != null)
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExportFile
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>exportFile</code>.
   **
   ** @param  file             the {@link File} where the export has to be
   **                          written to.
   */
  public void setExportFile(final File file) {
    this.delegate.exportFile(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFormat
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>format</code>.
   **
   ** @param  format             the output format either
   **                            <ul>
   **                              <li>{@link FeatureConstant#FORMAT_LDIF}
   **                              <li>{@link FeatureConstant#FORMAT_DSML}
   **                              <li>{@link FeatureConstant#FORMAT_JSON}
   **                             </ul>
   */
  public void setFormat(final Format format) {
    this.delegate.format(format.type());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description      the description in the created deployment.
   */
  public void setDescription(final String description) {
    this.delegate.description(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>version</code>.
   **
   ** @param  version            the version in the created deployment.
   */
  public void setVersion(final String version) {
    this.delegate.version(version);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setForceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride      <code>true</code> to override the existing file
   **                            without to aks for user confirmation.
   */
  public void setForceOverride(final boolean forceOverride) {
    this.delegate.forceOverride(forceOverride);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAttributesOnly
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>attributesOnly</code>.
   **
   ** @param  attributesOnly     <code>true</code> to export attribute names
   **                            only.
   */
  public void setAttributesOnly(final boolean attributesOnly) {
    this.delegate.attributesOnly(attributesOnly);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link ExportInstance} delegate of Directory Service object to
   ** handle.
   **
   ** @return                    the {@link ExportInstance} delegate.
   */
  public final ExportInstance instance() {
    if (isReference())
      return ((ExportSet)getCheckedRef()).instance();

    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredSearch
  /**
   ** Called to inject the argument to specify the search context of the export
   ** set.
   **
   ** @param  search             the the search context of the export to use
   **
   ** @throws BuildException     if this instance is referencing a search
   **                            context.
   */
  public void addConfiguredSearch(final Search search)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    this.delegate.add(search.instance());
  }
}