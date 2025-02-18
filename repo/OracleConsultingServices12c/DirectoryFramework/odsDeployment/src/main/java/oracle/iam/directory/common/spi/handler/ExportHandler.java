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

    File        :   DirectoryScope.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryScope.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.spi.handler;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import java.io.File;
import java.io.IOException;

import javax.naming.NamingEnumeration;

import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;

import javax.naming.ldap.LdapContext;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.SystemDialog;

import oracle.hst.deployment.ServiceFrontend;

import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceResourceBundle;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;
import oracle.iam.directory.common.FeatureMessage;
import oracle.iam.directory.common.FeatureResourceBundle;

import oracle.iam.directory.common.task.FeatureDirectoryTask;

import oracle.iam.directory.common.spi.instance.ExportInstance;
import oracle.iam.directory.common.spi.instance.SearchInstance;

import oracle.iam.directory.common.spi.support.LDAPRecord;
import oracle.iam.directory.common.spi.support.LDAPSearch;
import oracle.iam.directory.common.spi.support.LDAPWriter;
import oracle.iam.directory.common.spi.support.LDAPPageSearch;

////////////////////////////////////////////////////////////////////////////////
// class ExportHandler
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Exports Directory Service objects to file.
 ** <p>
 ** The <code>ExportHandler</code> enables you to convert to LDIF all or part of
 ** the information residing in a Directory Service. Once you have converted the
 ** information, you can load it into a new node in a replicated directory or
 ** another node for backup storage.
 ** <p>
 ** <code>ExportHandler</code> performs a subtree search, including all entries
 ** below the specified DN, including the DN itself.
 ** <p>
 ** <code>ExportHandler</code> output does not include operational data of the
 ** directory itself for example, cn=subschemasubentry, cn=catalogs, and
 ** cn=changelog entries. To export these entries into LDIF or DSML format, use
 ** SearchHandler with the -L flag.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExportHandler extends ObjectHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                          forceOverride = false;
  private final Collection<File>           flattenSet    = new HashSet<File>();
  private final Collection<ExportInstance> exportSet     = new ArrayList<ExportInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ExportHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ExportHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

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
  // Method:   exportSet
  /**
   ** Returns the {@link Collection} with {@link ExportInstance}s used.
   **
   ** @return                    the {@link Collection} with
   **                           {@link ExportInstance}s used.
   */
  public final Collection<ExportInstance> exportSet() {
    return this.exportSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.exportSet.size() == 0)
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_MANDATORY));

    try {
      for (ExportInstance cursor : this.exportSet)
        cursor.validate();
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link ExportInstance}.
   **
   ** @param  instance           the set of ldap queries to export.
   **
   ** @throws BuildException     if the file, the {@link ExportInstance} or an
   **                            attribute name contained in the set is
   **                            already part of this export operation.
   */
  public void add(final ExportInstance instance) {
    // check if we have this file already
    if (this.flattenSet.contains(instance.file()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.EXPORT_FILE_ONLYONCE, instance.file().getAbsolutePath()));

    this.flattenSet.add(instance.file());
    this.exportSet.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Exports all object definition from a Directory Service through the
   ** discovered {@link LdapContext}.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  public void execute()
    throws FeatureException {

    // initialize the business logic layer to operate on
    this.facade = ((FeatureDirectoryTask)frontend()).context().unwrap();
    for (ExportInstance cursor : this.exportSet)
      executeSet(cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeSet
  /**
   ** Export the object metadata for the specfied {@link ExportInstance}.
   **
   ** @param  instance           the {@link ExportInstance} to export.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void executeSet(final ExportInstance instance)
    throws FeatureException {

    if (exportDocument(instance) > 0)
      return;

    final LDAPWriter writer = LDAPWriter.create(instance.format(), instance.version(), instance.file());
    writer.attributesOnly(instance.attributesOnly());
    for (SearchInstance cursor : instance.search())
      executeSearch(writer, cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeSearch
  /**
   ** Export the object definition for the specfied {@link SearchInstance}.
   **
   ** @param  search             the {@link ExportInstance} to export.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void executeSearch(final LDAPWriter writer, final SearchInstance search)
    throws FeatureException {

    final SearchControls controls = search.control();
    // see if there were any user-defined sets of include attributes or filters.
    // If so, then process them.
    if (search.includes().size() > 0) {
      final String[] names = search.includes().toArray(new String[0]);
      controls.setReturningAttributes(names);
      writer.registerIncludes(CollectionUtility.unmodifiableSet(names));
    }
    // see if there were any user-defined sets of ommitted attributes
    // If so, then process them.
    if (search.excludes().size() > 0)
      writer.registerExcludes(CollectionUtility.unmodifiableSet(search.excludes().toArray(new String[0])));
    // see if there were any user-defined sets of binary attributes
    // If so, then process them.
    if (search.binaries().size() > 0)
      writer.registerBinaries(CollectionUtility.unmodifiableSet(search.binaries().toArray(new String[0])));

    try {
      writer.printPrologue();
      // initialize PagedResultControl method to set the request
      // controls here we requesting a paginated result set
      final LDAPSearch process = new LDAPPageSearch(this.facade, search.base(), search.filter(), null, controls, 1000);
      // This while loop is used to read the LDAP entries in blocks.
      // This should decrease memory usage and help with server load.
      do {
        NamingEnumeration<SearchResult> results = process.next();
        // loop through the results and
        while (results != null && results.hasMoreElements()) {
          final SearchResult result = results.nextElement();
          warning(FeatureResourceBundle.format(FeatureMessage.EXPORT_OBJECT, contextName(), result.getNameInNamespace()));
          // the Attributes instance will have all the values from the source
          // system that requested by setting returning attributes in
          // SearchControls instance passed to the server or all if null was
          // passed by the caller to this method
          final LDAPRecord record = new LDAPRecord(result.getNameInNamespace(), result.getAttributes());
          record.toStream(writer);
        }
      } while (process.hasMore());
    }
    finally {
      writer.printEpilogue();
      try {
       writer.flush();
      }
      catch (IOException e) {
        ;
      }
      writer.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportDocument
  /**
   ** Writes the XML document to the local file system
   **
   ** @param  instance           the {@link ExportInstance} to write as an XML
   **                            document to the local file system.
   */
  private int exportDocument(final ExportInstance instance) {
    int response = 0;
    if (!this.forceOverride && instance.file().exists()) {
      if (!SystemDialog.Prompt.confirmNotification(
        ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CONFIRMATION_TITLE,   instance.description())
      , ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CONFIRMATION_MESSAGE, instance.file().getName(), instance.file().getParent())
      ))
        response = 1;
    }
    return response;
  }
}