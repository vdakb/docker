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
    Subsystem   :   TXT Flatfile Connector

    File        :   LookupReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-18  DSteding    First release version
*/

package oracle.iam.identity.txt.service.reconciliation;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.sql.Connection;

import java.io.EOFException;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidAttributeException;

import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.dataobj.util.tcProperties;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.object.Pair;
import oracle.hst.foundation.object.Filter;
import oracle.hst.foundation.object.FilterBuilder;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractResource;

import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.naming.Property;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.utility.file.FlatFileAttribute;
import oracle.iam.identity.utility.file.FlatFileDescriptor;

import oracle.iam.identity.txt.service.ControllerMessage;

import oracle.iam.identity.txt.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// class LookupReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupReconciliation</code> acts as the service end point for
 ** Oracle Identity Manager to reconcile lookup values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LookupReconciliation extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify which type of
   ** reconciliation operation should be performed.
   ** <br>
   ** This attribute is mandatory.
  */
  protected static final String       OPERATION     = "Reconciliation Operation";

  /**
   ** Attribute tag which must be defined on this task to specify the name of
   ** the Lookup Group the Lookup Definition will be assigned to if the Lookup
   ** Definition has to be created during the reconciliation.
   ** <br>
   ** This attribute is mandatory.
  */
  protected static final String       LOOKUP_GROUP  = "Lookup Group";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the encoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
  */
  protected static final String       ENCODED_VALUE = "Encoded Value";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the decoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
  */
  protected static final String       DECODED_VALUE = "Decoded Value";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(APPLICATION_INSTANCE, TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,     TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Refresh | Update
     **
     ** @see Operation
    */
  , TaskAttribute.build(OPERATION,              TaskAttribute.MANDATORY)
    /** the task attribute to resolve the name of the Lookup Group */
  , TaskAttribute.build(LOOKUP_GROUP,           TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,     TaskAttribute.MANDATORY)
    /** the task attribute to resolve the encoded value */
  , TaskAttribute.build(ENCODED_VALUE,          TaskAttribute.MANDATORY)
    /** the task attribute to resolve the decoded value */
  , TaskAttribute.build(DECODED_VALUE,          TaskAttribute.MANDATORY)
    /** the task attribute to resolve the entity filter */
  , TaskAttribute.build(SEARCH_FILTER,          TaskAttribute.OPTIONAL)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,            TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,           TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are successfully proceeded */
  , TaskAttribute.build(WORKING_FOLDER,         TaskAttribute.MANDATORY)
    /** the fullqualified filename which are specifying the mapping for import */
  , TaskAttribute.build(DATA_DESCRIPTOR,        TaskAttribute.MANDATORY)
    /** the filename of the raw data */
  , TaskAttribute.build(DATA_FILE,              TaskAttribute.MANDATORY)
    /** the filename of the proceeded data */
  , TaskAttribute.build(PROCEED_FILE,           TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,          TaskAttribute.MANDATORY)
  /** report the non-existing as an exception */
  , TaskAttribute.build(MISSING_FILE_EXCEPTION, TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
    */
  , TaskAttribute.build(INCREMENTAL,            TaskAttribute.MANDATORY)
  };

  private static final DatabaseEntity LOOKUP        = DatabaseEntity.build(null, "lku",     "lku_key");
  private static final DatabaseEntity VALUE         = DatabaseEntity.build(null, "lku,lkv", "lkv_key");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Operation                   operation;

  private boolean                     entitlementPrefixRequired;

  private String                      defaultLanguage;
  private String                      defaultCountry;

  private tcLookupOperationsIntf      facade;
  private Filter                      entityFilter;
  private DatabaseFilter              lookupCode;

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
    @SuppressWarnings("compatibility:-7381647671670379180")
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
   ** Constructs an empty <code>LookupReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
  */
  protected LookupReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
  */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Company Phonebook.
   **
   ** @throws TaskException      in case an error does occur.
  */
  @Override
  protected void onExecution()
    throws TaskException {

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, reconcileObject(), getName(), resourceName()));
    if (this.dataFileAvailable(lastReconciled())) {
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, reconcileObject(), getName() , dataFile().getAbsolutePath()));
      try {
        createWorkingContext();
        createWorkingFile();
        processFile();
        if (!isStopped()) {
          // copy the new file over the oldfile
          // we let the new file where it is, so the next time the scheduled
          // task is running we have the same file or a new one
          FileSystem.copy(dataFile(), proceedFile());
          this.updateLastReconciled();
          info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, reconcileObject(), getName() , dataFile().getAbsolutePath()));
        }
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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
  */
  public void initialize()
    throws TaskException {

    // ensure inheritance
    super.initialize();

    final String method  = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
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

      // evaluate the filter expression as provided
      final String expression = stringValue(SEARCH_FILTER);
      if (!StringUtility.isEmpty(expression)) {
        this.entityFilter = FilterBuilder.build(stringValue(SEARCH_FILTER));
      }

      // initialize the naming convention for the lookup values by assuming that
      // the any entry needs to be prefixed if the parameter is not specified
      this.entitlementPrefixRequired = booleanValue(ENTITLEMENT_PREFIX, true);

      this.defaultLanguage = tcProperties.getValue(Property.LANGUAGE, Property.DEFAULT_LANGUAGE);
      this.defaultCountry  = tcProperties.getValue(Property.REGION,   Property.DEFAULT_REGION);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
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

    final String method  = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.facade = super.lookupFacade();
      try {
        // check the operation mode for refresh
        // this will remove the lookup and recreates an empty lookup definition
        // all values will be fetch afterwards from the source regardless how
        // the timestamp value is currently set
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
          attribute.put("LKU_GROUP", stringValue(LOOKUP_GROUP));
          this.facade.updateLookupCode(reconcileObject(), attribute);
        }
      }
      catch (tcInvalidLookupException e) {
        // we are not able to find any value, means we will create a new
        // lookup definition with the passed name
        this.facade.addLookupCode(reconcileObject());
        // .. and update the group field
        final Map<String, String> attribute = new HashMap<String, String>();
        // unfortunately we facing a bug if we try to map the pseudo column
        // code Lookup.GROUP in the standard way
        // A wrong value is mapped in the lookup definition
        // Lookup Definition.Group for the field information.
        // Instead of LKU_GROUP the mispelled value LKU_TYPE_GROUP is
        // specified thus we are mapping the native column name
        attribute.put("LKU_GROUP", stringValue(LOOKUP_GROUP));
        this.facade.updateLookupCode(reconcileObject(), attribute);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
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

    final String method  = "afterExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    if (this.facade != null) {
      this.facade.close();
      this.facade = null;
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFile
  /**
   ** Do all action which should take place for reconciliation for the working
   ** file.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void processFile()
    throws TaskException {

    if (booleanValue(INCREMENTAL)) {
      // extend the descriptor to fetch the transaction code from the
      // created working file with a readonly attribute
      FlatFileAttribute transaction = new FlatFileAttribute(FlatFileDescriptor.TRANSACTION, FlatFileAttribute.STRING, -1, -1);
      dataDescriptor().addAttribute(transaction);
    }
    final String encoded = stringValue(ENCODED_VALUE);
    final String decoded = stringValue(DECODED_VALUE);
    while (!isStopped()) {
      try {
        final Map<String, Object> entity = this.processor().readEntity(true);
        final String encodedValue = (String)entity.get(encoded);
        final String decodedValue = (String)entity.get(decoded);
        // apply filter criteria if available
        if (this.entityFilter != null) {
          if (this.entityFilter.accept(entity))
            mergeLookup(encodedValue, decodedValue);
          else {
            warning(TaskBundle.format(TaskMessage.LOOKUP_FILTER_NOTACCEPTED, this.entityFilter.toString(), encodedValue));
            this.incrementIgnored();
          }
        }
        else {
          mergeLookup(encodedValue, decodedValue);
        }
      }
      catch (EOFException e) {
        // don't close processor here but break the loop due to EOF reached
        break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeLookup
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** key/value pair.
   **
   ** @param  encoded            the key of the value pair to reconcile.
   ** @param  decoded            the meaning value of the pair to reconcile.
   **
   ** @throws TaskException      if the reconciliation of the value pair as a
   **                            lookup value fails.
   */
  protected void mergeLookup(String encoded, String decoded)
    throws TaskException {

    final String method  = "mergeLookup";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // normalize the vallue mapping
    if (StringUtility.isEmpty(decoded))
      decoded = encoded;

    // extracts the encoded value
    String lookupName = reconcileObject();
    if (this.entitlementPrefixRequired) {
      final AbstractResource resource = this.resource();
      encoded = String.format(ENTITLEMENT_ENCODED_PREFIX, resource.instance(), encoded);
      decoded = String.format(ENTITLEMENT_DECODED_PREFIX, resource.name(),     decoded);
    }
    try {
      if (!isStopped()) {
        info(TaskBundle.format(TaskMessage.ENTITY_RECONCILE, TaskBundle.string(TaskMessage.ENTITY_LOOKUP), encoded));

        final String current = exists(encoded);
        if (StringUtility.isEmpty(current))
          try {
            // we are not know the value in the passed result set, means we have
            // to create one
            this.facade.addLookupValue(lookupName, encoded, decoded, this.defaultLanguage, this.defaultCountry);
            this.incrementSuccess();
          }
          catch (tcAPIException e) {
            this.incrementFailed();
            error(method, ControllerBundle.format(ControllerMessage.CREATE_VALUE, lookupName, encoded));
          }
        else if (current.equals(decoded)) {
          this.incrementIgnored();
          warning(method, ControllerBundle.format(ControllerMessage.DUPLICATE_VALUE, reconcileObject(), decoded));
        }
        else
          try {
            // we are know the value in the passed result set already,
            // means we have to update
            final Map<String, String> data = new HashMap<String, String>();
            data.put(LookupValue.DECODED,  decoded);
            data.put(LookupValue.LANGUAGE, this.defaultLanguage);
            data.put(LookupValue.COUNTRY,  this.defaultCountry);
            this.facade.updateLookupValue(reconcileObject(), encoded, data);
            this.incrementSuccess();
          }
          catch (tcAPIException e) {
            this.incrementFailed();
            warning(method, ControllerBundle.format(ControllerMessage.DUPLICATE_VALUE, lookupName, encoded));
          }
      }
    }
    catch (tcInvalidLookupException e) {
      this.incrementFailed();
      stopExecution();
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, lookupName);
    }
    catch (tcInvalidAttributeException e) {
      this.incrementFailed();
      stopExecution();
      throw new TaskException(TaskError.LOOKUP_INVALID_ATTRIBUTE, reconcileObject());
    }
    catch (tcInvalidValueException e) {
      this.incrementFailed();
      stopExecution();
      String parameter[] = { lookupName, encoded };
      throw new TaskException(TaskError.LOOKUP_INVALID_VALUE, parameter);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
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
  @SuppressWarnings("unchecked")
  private String exists(final String encoded)
    throws TaskException  {

    final String method  = "exists";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final DatabaseFilter filter    = DatabaseFilter.build(this.lookupCode, DatabaseFilter.build("lkv_encoded", encoded, DatabaseFilter.Operator.EQUAL), DatabaseFilter.Operator.AND);
    final DatabaseSelect statement = DatabaseSelect.build(this, VALUE, filter, CollectionUtility.list(Pair.of("lkv_decoded", "lkv_decoded")));

    String found = null;
    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire();
      final List<Map<String, Object>> existingSet = statement.execute(connection);
      if (existingSet.size() > 1)
        throw new TaskException(TaskError.ENTITY_AMBIGUOUS);

      if (existingSet.size() == 1)
        found = (String)existingSet.get(0).get("lkv_decoded");
    }
    finally {
      DatabaseConnection.release(connection);
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return found;
  }
}