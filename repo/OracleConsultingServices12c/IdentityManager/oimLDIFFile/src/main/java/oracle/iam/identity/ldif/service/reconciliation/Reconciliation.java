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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   LDIF Flatfile Connector

    File        :   Reconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Reconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed Defect DE-000125
                                         Batch Size is an optional argument but
                                         if its isn't defined the job loops
                                         infinite.
                                         Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2012-31-05  DSteding    First release version
*/

package oracle.iam.identity.ldif.service.reconciliation;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.GregorianCalendar;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;

import javax.naming.Binding;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;

import javax.naming.directory.Attributes;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;

import oracle.iam.identity.foundation.ldap.DirectoryFileReader;
import oracle.iam.identity.utility.file.LDAPError;
import oracle.iam.identity.utility.file.LDAPException;
import oracle.iam.identity.utility.file.LDAPRecord;
import oracle.iam.identity.utility.file.LDIFReader;

import oracle.iam.identity.ldif.service.Controller;

import oracle.iam.identity.utility.file.CSVDescriptor;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** data provided by a LDIF file.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class Reconciliation extends Controller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String       LOGGER_CATEGORY = "OCS.LDF.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the all purpose container to search and post objects
  protected HashMap<String, String> filter          = new HashMap<String, String>();

  /** the abstract file which contains the data */
  private File                      importFile      = null;

  /** the abstract file which contains the produced errors */
  private File                      errorFile       = null;

  /** the processor of the LDIF structure */
  private DirectoryFileReader       processor       = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> task adapter that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Reconciliation() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processor
  /**
   ** Returns the {@link DirectoryFileReader} used by this
   ** <code>Reconciliation</code> to create a <code>LDIFRecord</code>.
   **
   ** @return                    the {@link DirectoryFileReader} used by this
   **                            scheduled task.
   */
  protected final DirectoryFileReader processor() {
    return this.processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValuedAttribute
  /**
   ** Checks if the attribute is in the multi-valued attribute of an entry.
   **
   ** @param  attribute          the name of the attribute to check.
   **
   ** @return                    <code>true</code> if the passed
   **                            <code>attribute</code> is in the list of
   **                            multi-valued attributes; otherwise
   **                            <code>false</code>
   */
  private final boolean multiValuedAttribute(final String attribute) {
    return (this.descriptor != null && this.descriptor.multivalued().containsKey(attribute));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    createDataFolder();
    createErrorFolder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFile
  /**
   ** Do all action which should take place for reconciliation for the working
   ** file.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected final void processFile()
    throws TaskException {

    final String method = "processFile";

    // connect to the source system
    this.processor = new LDIFReader(dataFile());
    // This while loop is used to read the file in blocks.
    // This should decrease memory usage and help with server load.
    while (!isStopped()) {
      List<Map<String, Object>> entries = populateBatch(batchSize());
      // if the collection is empty we had done the job
      if (entries == null || entries.size() == 0)
        break;

      // validate the effort to do
      info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(entries.size())));
      if (gatherOnly())
        info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
      else {
        for (Map<String, Object> subject : entries) {
          if (isStopped())
            break;

          // get the transaction code if available
          // in case the direct path operation was configured at this scheduled
          // task the resulting string will be null, means the subject will
          // never deleted
          final String transaction = (String)subject.get(CSVDescriptor.TRANSACTION);

          // filter data in a new mapping
          subject = this.descriptor.attributeMapping().filterByEncoded(subject);
          if (subject.isEmpty())
            throw new TaskException(TaskError.ATTRIBUTE_MAPPING_EMPTY);

          // produce the logging output only if the logging level is enabled for
          if (this.logger != null && this.logger.debugLevel())
            debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(subject)));

          // apply transformation if needed
          if (this.descriptor.transformationEnabled()) {
            subject = this.descriptor.transformationMapping().transform(subject);
            if (subject.isEmpty())
              throw new TaskException(TaskError.TRANSFORMATION_MAPPING_EMPTY);

            // produce the logging output only if the logging level is enabled for
            if (this.logger != null && this.logger.debugLevel())
              this.debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(subject)));
          }
          processSubject(transaction, subject);
        }
        info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   **
   ** @param  transaction        the transaction code of the {@link Map} to
   **                            reconcile.
   ** @param  data               the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected abstract void processSubject(final String transaction, final Map<String, Object> data)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFileAvailable
  /**
   ** Check if a file is in the transfer folder.
   **
   ** @param  timeStamp          the timestamp this task was last time executed.
   **
   ** @return                    <code>true</code> the abstract {@link File} to
   **                            access is touched since the last successful
   **                            import operation; otherwise <code>false</code>.
   **
   ** @throws TaskException      if the abstract file handle could not be
   **                            created.
   */
  protected final boolean dataFileAvailable(final Date timeStamp)
    throws TaskException {

    final String method ="dataFileAvailable";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean exists = dataFile().exists();
    if (exists) {
      long lastModified = dataFile().lastModified();
      if (lastModified < timeStamp.getTime()) {
        exists = false;
        info(TaskBundle.format(TaskMessage.NOTCHANGED, this.importFile.getName()));
      }
      // set the current date as the timestamp on which this task has last
      // reconciled at start
      // setting it at this time that we have next time this scheduled task
      // will run the changes made during the execution of this task
      lastReconciled(new Date(lastModified));
    }
    else {
      final String[] values = { DATA_FOLDER, this.importFile.getName() };
      info(TaskBundle.format(TaskMessage.NOTAVAILABLE, values));
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return exists;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFile
  /**
   ** Creates the abstract {@link File} to access the file to import.
   **
   ** @return                    the abstract {@link File} to access file to
   **                            import.
   **
   ** @throws TaskException      if the abstract file handle could not be
   **                           created.
   */
  protected final File dataFile()
    throws TaskException {

    if (this.importFile == null) {
      final String fileName = stringValue(DATA_FILE);
      // create the file handle
      this.importFile = createFile(dataFolder(), fileName);

      // check if we have read access to the file
      if (!this.importFile.canRead()) {
        final String[] values = { DATA_FILE, fileName};
        throw new LDAPException(LDAPError.NOTREADABLE, values);
      }
    }
    return this.importFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorFile
  /**
   ** Creates the abstract {@link File} to access the file which is produced
   ** if an error of this scheduled task occurs.
   **
   ** @return                    the abstract {@link File} to access the error
   **                            file.
   **
   ** @throws TaskException      if the abstract file handle could not be
   **                            created.
   */
  protected final File errorFile()
    throws TaskException {

    if (this.errorFile == null) {
      // create a timestamp that makes the file unique in the file system
      final GregorianCalendar now = new GregorianCalendar();
      final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.SSSZ");

      String fileName = this.dataFile().getName();
      // search for the last period in the file name
      int pos = fileName.lastIndexOf(SystemConstant.PERIOD);
      if (pos != -1) {
        fileName = fileName.substring(0, pos) + "-" + fmt.format(now.getTime()) + fileName.substring(pos);
      }
      else {
       fileName = fileName + "-" + fmt.format(now.getTime());
      }

      // create the file handle
      this.errorFile = createFile(this.errorFolder(), fileName);
      try {
        // create always the file in the file system
        errorFile.createNewFile();
      }
      catch (IOException e) {
        throw TaskException.general(e);
      }

      // check if we have read and write access to the file
      if (this.errorFile.exists()) {
        final String[] values = { DATA_FILE, fileName};
        // check if we have read access to the file
        if (!this.errorFile.canRead())
          throw new LDAPException(LDAPError.NOTREADABLE, values);

        // check if we have write access to the file
        if (!this.errorFile.canWrite())
          throw new LDAPException(LDAPError.NOTWRITABLE, values);
      }
    }

    return this.errorFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateBatch
  /**
   ** Populates the entries which are changed since the last execution of the
   ** scheduled task.
   **
   ** @param  bulkSize           number of lines to read.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  private List<Map<String, Object>> populateBatch(int bulkSize)
    throws TaskException {

    final String method ="populate";
    trace(method, SystemMessage.METHOD_ENTRY);
    // create a task timer to gather performance metrics
    timerStart(method);

    final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    try {
      // fetch all entities which are contained in the working file
      while (bulkSize-- > 0) {
        final Binding binding = this.processor.nextRecord();
        // if we get a valid binding from the processor we can safly assume that
        // we reached the end f file
        if (binding == null)
          break;
        // extracts the distinguished name from the binding and put it in the
        // subject container
        final Map<String, Object> subject = new HashMap<String, Object>();
        // we using method getNameInNamespace to get the fullqualified DN
        // and follow the configuration to put the correct name in the mapping
        subject.put(DirectoryConstant.DN, normalizePath(binding.getName()));
        // this LDAPRecord instance will have all the values from the
        // source system that requested by setting returning attributes
        // in SearchControls instance passed to the server or all if
        // null was passed by the caller to this method
        final LDAPRecord record = (LDAPRecord)binding.getObject();
        // this Attributes instance will have all the values from the
        // source system that requested by setting returning attributes
        // in SearchControls instance passed to the server or all if
        // null was passed by the caller to this method
        final Attributes attributes = record.attributes();
        // iterate over all attribute ID's
        final NamingEnumeration<String> id = attributes.getIDs();
        while (id.hasMoreElements()) {
          final String name = id.nextElement();
          if (this.multiValuedAttribute(name)) {
            final NamingEnumeration<?> object = attributes.get(name).getAll();
            List<Object> value = new ArrayList<Object>();
            while (object.hasMoreElements())
              value.add(object.nextElement());
            subject.put(name, value);
          }
          else {
            // just keeping a check on the attributes value if null then add
            // something
            final Object object = attributes.get(name).get();
            String value = (object instanceof byte[]) ? DirectoryConnector.hexString((byte[])object) : (String)object;
            subject.put(name, value);
          }
        }
        result.add(subject);
      }
    }
    catch (NamingException e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   denormalizePath
  /**
   ** Forms the basis of building the hierarchical tree to the LDAP object.
   ** Used by connect to build the correct distinguished name.
   **
   ** @param  entryRDN           Contains the elements in the tree, deepest one
   **                            first. The String must be of format
   **                            "Class Type=Object CN,Class Type=Object CN"
   **                            where:
   **                            <ul>
   **                              <li>Class Type is the objects class type ("CN", "OU", ...)
   **                              <li>Object CN is the LDAP objects common name ("Dumbo", "finance group", ... )
   **                            </ul>
   **                            Basically whatever is assigned to the
   **                            mandatory property "cn" or "ou". e.g.
   **                            <code>CN=Dumbo,OU=Leaders,OU=Elephants</code>
   **
   ** @return                    String of the canonical path (including the
   **                            root context), e.g.
   **                            OU=Users,OU=abc,OU=Companies,DC=thordev,DC=com
   */
  protected String denormalizePath(final String entryRDN) {
    return this.connector().denormalizePath(entryRDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizePath
  /**
   ** Remove the root context name from the distinguished name.
   **
   ** @param  distinguishedName  the path of the object (relative or absolute to
   **                            the root context hierarchy)
   **
   ** @return                    the relative distinguished name without the
   **                            root context
   */
  protected String normalizePath(final String distinguishedName) {
    return this.connector().normalizePath(distinguishedName);
  }
}