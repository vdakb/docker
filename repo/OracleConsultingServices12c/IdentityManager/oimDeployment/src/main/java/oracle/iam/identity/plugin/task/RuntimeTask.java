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

    File        :   ResourceTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.plugin.task;

import java.util.Set;
import java.util.LinkedHashSet;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;

import oracle.iam.OIMMigration.api.OIMMigrationService;

import oracle.hst.deployment.ServiceException;

import oracle.iam.identity.common.FeaturePlatformTask;

import oracle.iam.identity.common.spi.RuntimeRegistry;

import oracle.iam.identity.plugin.type.Bundle;
import oracle.iam.identity.plugin.type.Library;
import oracle.iam.identity.plugin.type.LocationSet;

////////////////////////////////////////////////////////////////////////////////
// class RuntimeTask
// ~~~~~ ~~~~~~~~~~~
/**
 ** Provides basic implementations to handle resources like JAR's,
 ** ResourceBundles etc. stored in the database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RuntimeTask extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the service provider executing the task operation */
  protected RuntimeRegistry registry;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RuntimeTask</code> which use the specified
   ** {@link RuntimeRegistry}
   */
  public RuntimeTask() {
    // ensure inheritance
    super();

    // initialize instance
    this.registry = new RuntimeRegistry(this);
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

    final OIMMigrationService facade = service(OIMMigrationService.class);
    this.registry.upload(facade);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredLibrary
  /**
   ** Call by the ANT deployment to inject the nested "fileset" element.
   **
   ** @param  locationSet        the names of the file where the upload has to
   **                            be done for.
   **
   ** @throws BuildException     if one of the resulting {@link File}s
   **                            evaluated from the {@link LocationSet} is
   **                            already part of this upload operation.
   */
  public void addConfiguredLibrary(final Library locationSet)
    throws BuildException {

    addLocation(RuntimeRegistry.LIBARY, locationSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredBundle
  /**
   ** Call by the ANT deployment to inject the nested "fileset" element.
   **
   ** @param  locationSet        the names of the file where the upload has to
   **                            be done for.
   **
   ** @throws BuildException     if one of the resulting {@link File}s
   **                            evaluated from the {@link LocationSet} is
   **                            already part of this upload operation.
   */
  public void addConfiguredBundle(final Bundle locationSet)
    throws BuildException {

    addLocation(RuntimeRegistry.BUNDLE, locationSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addLocation
  /**
   ** Called internally to inject the nested "fileset" element.
   **
   ** @param  tag                the tag of the files to upload.
   ** @param  locationSet        the names of the file where the upload has to
   **                            be done for.
   **
   ** @throws BuildException     if one of the resulting {@link File}s
   **                            evaluated from the {@link LocationSet} is
   **                            already part of this upload operation.
   */
  protected void addLocation(final String tag, final LocationSet locationSet)
    throws BuildException {

    final DirectoryScanner scanner = locationSet.getDirectoryScanner(getProject());
    final File             folder = scanner.getBasedir();
    final String[]         file = scanner.getIncludedFiles();

    final Set<RuntimeRegistry.Item> set = new LinkedHashSet<RuntimeRegistry.Item>(file.length);
    for (int i = 0; i < file.length; i++) {
      final RuntimeRegistry.Item item = new RuntimeRegistry.Item(folder, file[i], locationSet.location());
      set.add(item);
    }
    this.registry.add(tag, set);
  }
}