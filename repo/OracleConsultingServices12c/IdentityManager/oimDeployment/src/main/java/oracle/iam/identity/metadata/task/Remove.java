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

    File        :   Remove.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Remove.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.task.AbstractMetadataTask;
import oracle.hst.deployment.task.MetadataProvider;

import oracle.iam.identity.common.spi.MetadataDelete;

import oracle.iam.identity.metadata.type.FolderSet;
import oracle.iam.identity.metadata.type.NameSpace;

////////////////////////////////////////////////////////////////////////////////
// class Remove
// ~~~~~ ~~~~~~
/**
 ** Removes a artifacts from Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Remove extends AbstractMetadataTask {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Remove</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Remove() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

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
  public void addConfiguredNamespace(final NameSpace namespace)
    throws BuildException {

    MetadataDelete provider = (MetadataDelete)this.provider;
    final String   name = namespace.list()[0];
    for (FolderSet folderSet : namespace.folderSet()) {
      String[] file = folderSet.getFiles(getProject());
      for (int j = 0; j < file.length; j++)
        // we not build a full qualified path to the file do to it's remote to
        // our installation
        provider.addFile(name, folderSet.path(), new File(file[j]));
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
    return new MetadataDelete(this);
  }
}