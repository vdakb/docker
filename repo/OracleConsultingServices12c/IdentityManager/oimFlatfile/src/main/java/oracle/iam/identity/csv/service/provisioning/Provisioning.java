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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   CSV Flatfile Connector

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Provisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.service.provisioning;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import com.thortech.xl.crypto.tcCryptoUtil;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.naming.User;
import oracle.iam.identity.foundation.naming.Group;
import oracle.iam.identity.foundation.naming.Organization;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.utility.file.CSVError;
import oracle.iam.identity.utility.file.CSVWriter;
import oracle.iam.identity.utility.file.CSVProcessor;
import oracle.iam.identity.utility.file.CSVException;
import oracle.iam.identity.utility.file.CSVDescriptor;

import oracle.iam.identity.utility.dataaccess.QueryBuilder;
import oracle.iam.identity.utility.dataaccess.AccessAttribute;
import oracle.iam.identity.utility.dataaccess.UserFormQueryBuilder;
import oracle.iam.identity.utility.dataaccess.GroupFormQueryBuilder;
import oracle.iam.identity.utility.dataaccess.ProcessFormQueryBuilder;
import oracle.iam.identity.utility.dataaccess.OrganizationFormQueryBuilder;

import oracle.iam.identity.csv.service.Controller;
import oracle.iam.identity.csv.service.ControllerMessage;

import oracle.iam.identity.csv.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Provisioning
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>Provisioning</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Scheduler which handles data
 ** delivered to a CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class Provisioning extends Controller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the format of the date used in queries */
  protected static final String DATEPATTERN       = "YYYYMMDDHH24MISS";

  /**
   ** Attribute tag which must be defined on this task to specify the threshold
   ** of the difference between the creation date and the update date of an
   ** entity or the difference between the update date and the last execution
   ** time of this task will be treated as an update on the entity.
   */
  protected static final String THRESHOLD         = "Update Threshold";

  /** the template of the quers for diract dataaccess */
  private static final String   QUERYTEMPLATE     = "SELECT {0}{1} FROM {2} WHERE {3}";

  /** the JNDI name of the datasource used to populate the data */
  private static final String   DATASOURCE        = "jdbc/operationsDB";

  /** the alias of the encryption key in the key store */
  private static final String   CRYPTO_KEYALIAS   =  "DBSecretKey";

  /** the character encoding for encryption/decryption */
  private static final String   CRYPTO_ENCODING   = "UTF-8";

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY   = "OCS.CSV.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstract file which contains the data */
  private File                  dataFile             = null;

  /** the processor of the CSV structure */
  private CSVProcessor          processor            = null;

  /** the processor of the CSV structure */
  private CSVWriter             writer               = null;

  private QueryBuilder          queryBuilder;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Provisioning</code> task adapter that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Provisioning() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processor
  /**
   ** Returns the {@link CSVProcessor} used by this <code>Connector</code> to
   ** create a <code>CSVRecord</code>.
   **
   ** @return           the {@link CSVProcessor} used by this scheduled task.
   */
  public final CSVProcessor processor() {
    return this.processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writer
  /**
   ** Returns the {@link CSVWriter} used by this scheduled task.
   **
   ** @return                    the {@link CSVWriter} used by this scheduled
   **                            task.
   */
  protected final CSVWriter writer() {
    return this.writer;
  }

  /////////////////////////////////////////////////////////////////////////////
  // Method:   queryBuilder
  /**
   ** Returns the {@link QueryBuilder} of this scheduled task.
   **
   ** @return                    the {@link QueryBuilder} of this scheduled task.
   */
  protected final QueryBuilder queryBuilder() {
    return this.queryBuilder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFile
  /**
   ** Returns the abstract {@link File} handle of the file which is used to read
   ** or write raw data.
   **
   ** @return                    the abstract {@link File} handle of the file
   **                            which is used to read or write raw data.
   */
  private final File dataFile()
    throws TaskException {

    if (this.dataFile == null)
      createDataFile(dataFolder());

    return this.dataFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the reconciliation task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    final String method = "onExecution";
    String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(TaskBundle.format(TaskMessage.PROVISIONING_BEGIN, parameter));

    SimpleDateFormat    formatter     = new SimpleDateFormat("yyyyMMddHHmmss");
    final long          taskThreshold = Long.parseLong(parameter(THRESHOLD));
    final Date          lastExecution = lastReconciled();

    final CSVDescriptor descriptor    = dataDescriptor();

    Exception           exception     = null;
    Connection          connection    = null;
    PreparedStatement   statement     = null;
    ResultSet           resultSet     = null;

    try {
      final Object[] template = {
        this.queryBuilder.status()
      , this.queryBuilder.select(this.descriptor.attributeMapping().keySet())
      , this.queryBuilder.from()
      , this.queryBuilder.filter()
      };
      // conversion is needed due to database don't understand milliseconds
      // in a select statement
      final String time  = formatter.format(lastExecution);
      final String query = MessageFormat.format(QUERYTEMPLATE, template);
      debug(method, ControllerBundle.format(ControllerMessage.STATEMENT, time, query));

      connection = DatabaseConnection.aquire(DatabaseConnection.dataSource(DATASOURCE));
      statement  = DatabaseStatement.createPreparedStatement(connection, query);

      // delegate the prepare of the statement to the subclass
      prepareStatement(statement, time);
      resultSet = statement.executeQuery();

      // create the container to transfer the data read from the result set and
      // push to the sub classes for further processing
      Map<String, Object> entity = new HashMap<String, Object>(descriptor.size());

      // create a CVS writer leverage a FileWriter
      // the FileOutputStream will always append to the existing file regardless
      // to the state of the provisioning task
      this.writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(dataFile(), true), parameter(FILE_ENCODING)), 1, this.singleValueSeparator, this.enclosingCharacter, true);
      // write the header if we have a new file
      if ((this.dataFile().length() < 1)) {
        this.writer.put(CSVDescriptor.TRANSACTION);
        this.dataDescriptor().write(writer);
      }
      // get the data and put in in the mapping
      while (resultSet.next()) {
        String objectStatus    = resultSet.getString("object_status");
        Date   statusCreate    = resultSet.getDate("status_create");
        long   statusThreshold = resultSet.getLong("status_threshold");
        Date   objectCreate    = resultSet.getDate("object_create");
        long   objectThreshold = resultSet.getLong("object_threshold");

        String transaction = CSVDescriptor.NOTHING;
        if (objectStatus.equalsIgnoreCase("Revoked")||objectStatus.equalsIgnoreCase("Deleted"))
          transaction = CSVDescriptor.DELETE;
        else if (lastExecution.compareTo(statusCreate) <= 0)
          transaction = CSVDescriptor.CREATE;
        else if (lastExecution.compareTo(objectCreate) <= 0)
          transaction = CSVDescriptor.CREATE;
        else if (statusThreshold > taskThreshold)
          transaction = CSVDescriptor.UPDATE;
        else if (objectThreshold > taskThreshold)
          transaction = CSVDescriptor.UPDATE;

        entity.clear();
        for (String label : this.descriptor.attributeMapping().keySet()) {
          final Iterator<AccessAttribute> field = this.queryBuilder.fieldIterator();
          debug(method, label);
          while (field.hasNext()) {
            final AccessAttribute item = field.next();
            if (label.equalsIgnoreCase(item.name())) {
              String value = resultSet.getString(item.name());
              if (item.encrypted())
                value = tcCryptoUtil.decrypt(value, CRYPTO_KEYALIAS, CRYPTO_ENCODING);
              entity.put(label, value);
            }
          }
        }
        debug(method, entity.toString());
        entity = this.descriptor.attributeMapping().filterByEncoded(entity);
        if (entity.isEmpty())
          throw new TaskException(TaskError.ATTRIBUTE_MAPPING_EMPTY);

        // produce the logging output only if the logging level is enabled for
        if (this.logger != null && this.logger.debugLevel()) {
          debug(method, TaskBundle.format(TaskMessage.ENTITY_RECONCILE, this.descriptor.identifier(), entity.get(this.descriptor.identifier())));
          // produce the logging output only if the logging level is enabled for
          debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(entity)));
        }

        processSubject(transaction, entity);
      }
      this.updateTimestamp(TIMESTAMP);
      info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, Long.toString(this.writer.linesProceed()), "0", "0"));
    }
    catch (Exception e) {
      // notify user about the problem
      warning(TaskBundle.format(TaskMessage.PROVISIONING_STOPPED, parameter));
      exception = e;
    }
    finally {
      if (resultSet != null)
        DatabaseStatement.closeResultSet(resultSet);
      if (statement != null)
        DatabaseStatement.closeStatement(statement);
      if (connection != null)
        DatabaseConnection.release(connection);
    }

    try {
      if (this.writer != null)
        this.writer.close();
    }
    catch (IOException e) {
      exception = e;
    }
    info(TaskBundle.format(TaskMessage.PROVISIONING_COMPLETE, parameter));

    if (exception != null) {
      if (exception instanceof TaskException)
        throw (TaskException)exception;
      else
        throw new TaskException(exception);
    }
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

    // create a CVS processor
    this.processor = new CSVProcessor(dataDescriptor());
    this.processor.logger(logger());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** The call back method just invoked before reconciliation takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    final String provisioningObject = reconcileObject();
    if (User.RESOURCE.equals(provisioningObject))
      this.queryBuilder = new UserFormQueryBuilder(getDataBase(), LOGGER_CATEGORY);
    else if (Organization.RESOURCE.equals(provisioningObject))
      this.queryBuilder = new OrganizationFormQueryBuilder(getDataBase(), LOGGER_CATEGORY);
    else if (Group.RESOURCE.equals(provisioningObject))
      this.queryBuilder = new GroupFormQueryBuilder(getDataBase(), LOGGER_CATEGORY);
    else
      this.queryBuilder = new ProcessFormQueryBuilder(getDataBase(), LOGGER_CATEGORY, provisioningObject);

    // ensure inheritance
    super.beforeExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDataFile
  /**
   ** Creates the abstract {@link File} to access the file to export.
   **
   ** @return                    the abstract {@link File} to access file to
   **                            export.
   **
   ** @throws TaskException      if the abtract file handle could not be
   **                            created.
   */
  private File createDataFile(final File dataFolder)
    throws TaskException {

    if (this.dataFile == null) {
      final String fileName = dataFileName();
      // create the file handle
      this.dataFile = createFile(dataFolder, fileName);

      // check if we have read and write access to the file
      if (this.dataFile().exists()) {
        final String[] values = { DATA_FILE, fileName};
        // check if we have read access to the file
        if (!this.dataFile().canRead())
          throw new CSVException(CSVError.NOTREADABLE, values);

        // check if we have write access to the file
        if (!this.dataFile().canWrite())
          throw new CSVException(CSVError.NOTWRITABLE, values);
      }
      else {
        try {
          this.dataFile().createNewFile();
        }
        catch (IOException e) {
          throw new CSVException(CSVError.NOTCREATABLE, fileName, e);
        }
      }
    }

    return this.dataFile();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareStatement
  /**
   ** Callback method invoked through the execution of this task to prepare the
   ** bind values to the statement.
   **
   ** @param  statement          the JDBC {@link PreparedStatement} to prepare
   ** @param  lastExecution      the String representation of the date this task
   **                            was last executed.
   **
   ** @throws TaskException      in case an error does occur processing the
   **                            statement.
   */
  protected abstract void prepareStatement(final PreparedStatement statement, final String lastExecution)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for provisioning for a particular
   ** subject.
   **
   ** @param  transaction        the transaction mark how the entity should
   **                            handled.
   ** @param  subject            the {@link Map} to provision.
   *
   ** @throws TaskException      in case an error does occur processing the
   **                            file.
   */
  protected abstract void processSubject(final String transaction, final Map<String, Object> subject)
    throws TaskException;
}