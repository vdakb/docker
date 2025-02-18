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
    Subsystem   :   Metadata Service Utilities 11g

    File        :   Upload.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Upload.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.task.AbstractMetadataTask;
import oracle.hst.deployment.task.MetadataProvider;

import oracle.iam.identity.common.spi.MetadataImport;

import oracle.iam.identity.metadata.type.NameSpace;
import oracle.iam.identity.metadata.type.ImportSet;

////////////////////////////////////////////////////////////////////////////////
// class Upload
// ~~~~~ ~~~~~~
/**
 ** Uploads a artifacts to Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Upload extends AbstractMetadataTask {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Upload</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Upload() {
    // ensure inheritance
    super();
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
    this.provider.forceOverride(forceOverride);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredNamespace
  /**
   ** Call by the ANT deployment to inject the nested "namespace" element.
   **
   ** @param  namespace          the names of the file where the upload has to
   **                            be written to.
   **
   ** @throws BuildException     if the on of the resulting {@link File}s
   **                            evaluated from the {@link NameSpace} is already
   **                            part of this import operation.
   */
  public void addConfiguredNamespace(final NameSpace namespace) {
    MetadataImport provider = (MetadataImport)this.provider;
    final String   name = namespace.list()[0];
    for (ImportSet importSet : namespace.importSet()) {
      String[] file = importSet.getFiles(getProject());
      for (int j = 0; j < file.length; j++)
        provider.addFile(name, importSet.path(), new File(importSet.getDir(getProject()), file[j]));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProvider (AbstractMetadatTask)
  /**
   ** Factory method to create the appropriate {@link MetadataProvider}.
   **
   ** @return                    the appropriate {@link MetadataProvider}.
   */
  @Override
  protected final MetadataProvider createProvider() {
    return new MetadataImport(this);
  }
}