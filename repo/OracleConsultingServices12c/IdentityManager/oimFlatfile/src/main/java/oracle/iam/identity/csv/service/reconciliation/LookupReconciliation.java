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

    File        :   LookupReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.1      2013-03-19  DSteding    Fixed DE-000078
                                         Reconciliation does not check if a
                                         key/value pair is really changed.
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.service.reconciliation;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.sql.Connection;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcInvalidAttributeException;

import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.dataobj.util.tcProperties;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.Property;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

import oracle.iam.identity.utility.file.CSVAttribute;
import oracle.iam.identity.utility.file.CSVDescriptor;

import oracle.iam.identity.csv.service.ControllerMessage;

import oracle.iam.identity.csv.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// class LookupReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupReconciliation</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** lookup definition data provided by CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class LookupReconciliation extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** type of reconciliation operation should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String OPERATION         = "Reconciliation Operation";

  /**
   ** Attribute tag which must be defined on this task to specify the name of
   ** the Lookup Group the Lookup Definition will be assigned to if the Lookup
   ** Definition has to be created during the reconciliation.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String LOOKUPGROUP       = "Lookup Group";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the encoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ENCODEDVALUE      = "Encoded Value";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the decoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String DECODEDVALUE      = "Decoded Value";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(RECONCILE_OBJECT,        TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,               TaskAttribute.MANDATORY)
    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Refresh | Update
     */
  , TaskAttribute.build(OPERATION,               TaskAttribute.MANDATORY)
    /** the task attribute to resolve the name of the Lookup Group */
  , TaskAttribute.build(LOOKUPGROUP,             TaskAttribute.MANDATORY)
    /** the task attribute to resolve the encoded value */
  , TaskAttribute.build(ENCODEDVALUE,            TaskAttribute.MANDATORY)
    /** the task attribute to resolve the decoded value */
  , TaskAttribute.build(DECODEDVALUE,            TaskAttribute.MANDATORY)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,             TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,            TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are successfully proceeded */
  , TaskAttribute.build(WORKING_FOLDER,          TaskAttribute.MANDATORY)
    /** the fullqualified filename which are specifying the mapping for import  */
  , TaskAttribute.build(DATA_DESCRIPTOR,         TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATA_FILE,               TaskAttribute.MANDATORY)
    /** the filename of the proceeded data  */
  , TaskAttribute.build(PROCEED_FILE,            TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,           TaskAttribute.MANDATORY)
    /** the character to separate single values */
  , TaskAttribute.build(SINGLE_VALUED_SEPARATOR, TaskAttribute.MANDATORY)
    /** the flag that indicates that the files should be compared  */
  , TaskAttribute.build(INCREMENTAL,             TaskAttribute.MANDATORY)
    /** the character to enclose values */
  , TaskAttribute.build(ENCLOSING_CHARACTER,     "\"")
  };

  private static final DatabaseEntity   LOOKUP = DatabaseEntity.build(null, "lku",     "lku_key");
  private static final DatabaseEntity   VALUE  = DatabaseEntity.build(null, "lku,lkv", "lkv_key");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Operation                     operation;
  private String                        defaultLanguage;
  private String                        defaultCountry;

  private tcLookupOperationsIntf        facade;
  private DatabaseFilter                lookupCode;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Operation
  // ~~~~ ~~~~~~~~~
  /**
   ** This enum store the grammar's constants for the operational codes of this
   ** Lookup Reconciliation task.
   */
  enum Operation {
    /**
     ** Attribute value which may be defined on this task to specify that the
     ** <code>Lookup Definition</code> reconciled by this task has to be
     ** refreshed completley.
     ** <p>
     ** This operation will remove any mapping at first and build the
     ** <code>Lookup Definition</code> from scratch.
     ** <b>Note</b>:
     ** <br>
     ** Any reference that this <code>Lookup Definition</code> use and referes
     ** to a mapping that no longer exists this type of operation is performed
     ** will be lost. The operation does not take care about if a mapping that
     ** does no longer exists is used somewhere.
     */
    Refresh,

    /**
     ** Attribute value which may be defined on this task to specify that the
     ** <code>Lookup Definition</code> reconciled by this task has to be
     ** updated with the changes.
     ** <p>
     ** This operation will not remove any mapping that's no longer exists.
     ** Only changed and/or new values are reconciled by this operation.
     */
    Update;

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:807553311918926953")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: typeString
    /**
     ** Returns a string with all the possible types a catalog has.
     **
     ** @return                  a string with all the possible types a catalog
     **                          has.
     */
    public static final String typeString() {
      return String.format("%s | %s", Operation.Refresh.toString(), Operation.Update.toString());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupReconciliation</code> task adapter
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

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

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, reconcileObject(), getName() , dataFile().getAbsolutePath()));
    if (this.dataFileAvailable(lastReconciled())) {
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, reconcileObject(), getName() , dataFile().getAbsolutePath()));
      try {
        this.createWorkingContext();
        this.createWorkingFile();
        if (booleanValue(INCREMENTAL)) {
          // extend the descriptor to fetch the transaction code from the
          // created working file with a readonly attribute
          CSVAttribute transaction = new CSVAttribute(CSVDescriptor.TRANSACTION, CSVAttribute.STRING, true, null);
          dataDescriptor().addAttribute(transaction);
        }

        int pass = 0;
        processFile(pass);
        // copy the new file over the oldfile
        // we let the new file where it is, so the next time the scheduled
        // task is running we have the same file or a new one
        FileSystem.copy(dataFile(), proceedFile());

        this.updateLastReconciled();
        info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, reconcileObject(), getName() , dataFile().getAbsolutePath()));
      }
      catch (SystemException e) {
        // copy the data file to the error folder
        // we let the new file where it is, so the next time the scheduled
        // task is running we have the same file or a new one
        try {
          FileSystem.copy(dataFile(), errorFile());
        }
        catch (SystemException ex) {
          warning(ex.getLocalizedMessage());
        }
        throw new TaskException(e);
      }
      finally {
        if (this.processor() != null) {
          // close processor this will also free the resources associated with the
          // processor
          this.processor().close();
        }
        // remove temporary files
        deleteWorkingFile();

        // inform the observing user about the overall result of this task
        if (isStopped())
          warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, reconcileObject(), getName() , dataFile().getAbsolutePath()));
        else
          info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName() , dataFile().getAbsolutePath()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do trusted reconciliation of an Oracle Identity Manager Lookup
   ** Definition.
   **
   ** @param  encoded            the composed identifier of the {@link Map} to
   **                            reconcile.
   ** @param  transaction        the transaction code of the {@link Map} to
   **                            reconcile.
   ** @param  data               the {@link Map} to reconcile.
   ** @param  pass               the pass the subjec ist loaded from the file.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected final void processSubject(final String encoded, final String transaction, final Map<String, Object> data, final int pass)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = {TaskBundle.string(TaskMessage.ENTITY_LOOKUP), encoded};
    info(TaskBundle.format(TaskMessage.ENTITY_RECONCILE, parameter));

    try {
      // handle deletion at first
      if (CSVDescriptor.DELETE.equalsIgnoreCase(transaction))
        try {
          // delet the event from lookup definition
          this.facade.removeLookupValue(reconcileObject(), encoded);
        }
        catch (tcAPIException e) {
          warning(method, ControllerBundle.format(ControllerMessage.REMOVE_VALUE, parameter));
          this.incrementFailed();
        }
      else {
        // extracts the decoded value by checking if the distinguished name is
        // requested or an attribute
        String decoded = (String)data.get(LookupValue.DECODED);
        // set language code to default if no one specified
        if (StringUtility.isEmpty((String)data.get(LookupValue.LANGUAGE)))
          data.put(LookupValue.LANGUAGE, this.defaultLanguage);

        // set region code to default if no one specified
        if (StringUtility.isEmpty((String)data.get(LookupValue.COUNTRY)))
          data.put(LookupValue.COUNTRY,  this.defaultCountry);

        // set status to default if no one specified
        if (StringUtility.isEmpty((String)data.get(LookupValue.DISABLED)))
          data.put(LookupValue.DISABLED, LookupValue.STATUS_ENABLED);

        // reconcile data
        final String current = exists(encoded);
        if (StringUtility.isEmpty(current))
          try {
            // we are not know the value in the passed result set, means we have
            // to create one
            this.facade.addLookupValue(reconcileObject(), encoded, decoded, this.defaultLanguage, this.defaultCountry);
            this.incrementSuccess();
          }
          catch (tcAPIException e) {
            error(method, ControllerBundle.format(ControllerMessage.CREATE_VALUE, parameter));
            this.incrementFailed();
          }
        // Fix Defect DE-000078
        // Reconciliation does not check if a key/value pair is really changed.
        else if (current.equals(decoded)) {
          warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, TaskBundle.string(TaskMessage.ENTITY_LOOKUP), encoded));
          this.incrementIgnored();
        }
        else
          try {
            // we are know the value in the passed result set already,
            // means we have to update
            this.facade.updateLookupValue(reconcileObject(), encoded, data);
            this.incrementSuccess();
          }
          catch (tcAPIException e) {
            warning(method, ControllerBundle.format(ControllerMessage.DUPLICATE_VALUE, parameter));
            this.incrementFailed();
          }
      }
    }
    catch (tcInvalidLookupException e) {
      this.incrementFailed();
      stopExecution();
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, reconcileObject());
    }
    catch (tcInvalidAttributeException e) {
      this.incrementFailed();
      stopExecution();
      throw new TaskException(TaskError.LOOKUP_INVALID_ATTRIBUTE, reconcileObject());
    }
    catch (tcInvalidValueException e) {
      this.incrementFailed();
      stopExecution();
      throw new TaskException(TaskError.LOOKUP_INVALID_VALUE, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
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

    // initialize the database filter to lookup values from the Lookup
    // Definition this task is reconciling
    this.lookupCode = DatabaseFilter.build(
      DatabaseFilter.build("lku.lku_type_string_key", this.reconcileObject(), DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build("lkv.lku_key",             LOOKUP,                 DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );

    // check if the specified value provided for the reconciliation operation
    // is in range
    this.operation = Operation.valueOf(stringValue(OPERATION));
    if (this.operation == null) {
      final String[] parameter = {OPERATION, Operation.typeString() };
      throw new TaskException(TaskError.TASK_ATTRIBUTE_NOT_INRANGE, parameter);
    }

    this.defaultLanguage = tcProperties.getValue(Property.LANGUAGE, Property.DEFAULT_LANGUAGE);
    this.defaultCountry  = tcProperties.getValue(Property.REGION,   Property.DEFAULT_REGION);

    // create the reconciliation descriptor on the fly to meet the requirement
    // of providing an attribute and transformation mapping for the values to
    // reconcile
    this.descriptor = new Descriptor(this);
    this.descriptor.identifier(stringValue(ENCODEDVALUE));
    this.descriptor.natively(false);
    this.descriptor.transformationEnabled(false);
    this.descriptor.attributeMapping().put(stringValue(ENCODEDVALUE), stringValue(ENCODEDVALUE));
    this.descriptor.attributeMapping().put(stringValue(DECODEDVALUE), stringValue(DECODEDVALUE));
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

    // ensure inheritance
    super.beforeExecution();

    this.facade = this.lookupFacade();
    try {
      try {
        // check the operation mode for override this will remove the lookup
        // afterwards it will be recreated and all values will be fetch from the
        // source regardless how the timestamp value is currently set
        if (this.operation == Operation.Refresh) {
          // delete the Lookup Definition at first ...
          this.facade.removeLookupCode(reconcileObject());
          // ... add the Lookup Definition ...
          this.facade.addLookupCode(reconcileObject());
          // .. and update the group field
          final Map<String, String> attribute = new HashMap<String, String>();
          // unfortunately we facing a bug if we try in the standard way to map
          // the pseudo column code specified Lookup.GROUP
          // A wrong value is mapped in the lookup definition
          // Lookup Definition.Group for the field information. Instead of
          // LKU_GROUP the mispelled value LKU_TYPE_GROUP is specified.
          // Therefore we are mapping the native column name
          attribute.put("LKU_GROUP", stringValue(LOOKUPGROUP));
          this.facade.updateLookupCode(reconcileObject(), attribute);
        }
      }
      catch (tcInvalidLookupException e) {
        // we are not able to find any value, means we will create a new
        // lookup definition with the passed name
        this.facade.addLookupCode(reconcileObject());
        // .. and update the group field
        final Map<String, String> attribute = new HashMap<String, String>();
        // unfortunately we facing a bug if we try in the standard way to
        // map the pseudo column code specified Lookup.GROUP
        // A wrong value is mapped in the lookup definition
        // Lookup Definition.Group for the field information.
        // Instead of LKU_GROUP the mispelled value LKU_TYPE_GROUP is
        // specified.
        // Therefore we are mapping the native column name
        attribute.put("LKU_GROUP", stringValue(LOOKUPGROUP));
        this.facade.updateLookupCode(reconcileObject(), attribute);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterExecution (overridden)
  /**
   ** The call back method just invoked after reconciliation finished.
   ** <br>
   ** Close all resources requested before reconciliation takes place.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void afterExecution()
    throws TaskException {

    if (this.facade != null) {
      this.facade.close();
      this.facade = null;
    }

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Check if the specified <code>encoded</code> value exists in the given
   ** result set.
   **
   ** @param  encoded            <code>encoded</code> value to check.
   **
   ** @return                    the decoded value for the passed
   **                            <code>encoded</code> value or <code>null</code>
   **                            if no value is mapped for <code>encoded</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private String exists(final String encoded)
    throws TaskException  {

    final String method  = "exists";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final DatabaseFilter    filter     = DatabaseFilter.build(
      this.lookupCode
    , DatabaseFilter.build("lkv.lkv_encoded", encoded, DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect statement  = DatabaseSelect.build(this, VALUE, filter, CollectionUtility.list(Pair.of("lkv.lkv_decoded", stringValue(DECODEDVALUE))));

    String found = null;
    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire();
      final List<Map<String, Object>> existingSet = statement.execute(connection);
      if (existingSet.size() > 1)
        throw new TaskException(TaskError.ENTITY_AMBIGUOUS);

      if (existingSet.size() == 1)
        found = (String)existingSet.get(0).get(stringValue(DECODEDVALUE));
    }
    finally {
      DatabaseConnection.release(connection);
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return found;
  }
}