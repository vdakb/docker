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
    Subsystem   :   Oracle Database Account Connector

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
    1.0.0.1      2013-03-19  DSteding    Fixed DE-000079
                                         Reconciliation does not check if a
                                         key/value pair is really changed.
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.service.reconciliation;

import java.util.Set;
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
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.naming.Property;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.dbs.persistence.Catalog;

import oracle.iam.identity.dbs.resource.ReconciliationBundle;

////////////////////////////////////////////////////////////////////////////////
// class LookupReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile lookup information from a Database.
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
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the encoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String ENCODEDVALUE = "Encoded Value";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the decoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String DECODEDVALUE = "Decoded Value";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** type of reconciliation operation should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String OPERATION   = "Reconciliation Operation";

  /**
   ** Attribute tag which must be defined on this task to specify the name of
   ** the Lookup Group the Lookup Definition will be assigned to if the Lookup
   ** Definition has to be created during the reconciliation.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String LOOKUPGROUP = "Lookup Group";

  /**
   ** The vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute that holds the value of the last run */
    TaskAttribute.build(TIMESTAMP,        TaskAttribute.MANDATORY)
    /** the task attribute IT Resource */
  , TaskAttribute.build(IT_RESOURCE,      TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT, TaskAttribute.MANDATORY)
    /** the identifier of the catalog to reconcile */
  , TaskAttribute.build(RECONCILE_SOURCE, TaskAttribute.MANDATORY)
    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Override | Refresh
     */
  , TaskAttribute.build(OPERATION,        TaskAttribute.MANDATORY)
    /** the task attribute to resolve the name of the Lookup Group */
  , TaskAttribute.build(LOOKUPGROUP,      TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,      SystemConstant.TRUE)
  };

  private static final DatabaseEntity LOOKUP = DatabaseEntity.build(null, "lku",     "lku_key");
  private static final DatabaseEntity VALUE  = DatabaseEntity.build(null, "lku,lkv", "lkv_key");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Operation                   operation;
  private Catalog.Type                catalogType;
  private String                      defaultLanguage;
  private String                      defaultCountry;

  private tcLookupOperationsIntf      facade;
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
    @SuppressWarnings("compatibility:6163683008185320117")
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

    final String[] parameter = { stringValue(RECONCILE_OBJECT), getName(), resourceName() };
    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, parameter));
    // open the connection as late as possible to avoid that any exception is
    // thrown during the phases of initialization and preparation will bypass
    // the close operation to release the aquired connection back to the pool
    this.connection.connect();
    // set the current date as the timestamp on which this task has been last
    // reconciled at the start of execution
    // setting it here to have it the next time this scheduled task will
    // run the changes made during the execution of this task
    // updating this attribute will not perform to write it back to the
    // scheduled job attributes it's still in memory; updateLastReconciled()
    // will persist the change that we do here only if the job completes
    // successful
    lastReconciled(this.connection.systemTime());
    try {
      // we don't need to validate the catalog type again if a wrong value was
      // passed it was already rejected in the initialization of the job
      mergeLookup(reconcileObject(), this.connection.lookupCatalog(this.catalogType));
      updateLastReconciled();
    }
    finally {
      // close the connection early to avoid that any exception is thrown during
      // the phases of cleanup will bypass the close operation to give the
      // aquired connection back to the pool
      this.connection.disconnect();

      // inform the observing user about the overall result of this task
      if (isStopped())
        warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, parameter));
      else
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
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
      this.catalogType = Catalog.Type.valueOf(reconcileSource());
      if (this.catalogType == null) {
        final String[] parameter = {reconcileSource(), Catalog.typeString()};
        throw new TaskException(TaskError.TASK_ATTRIBUTE_NOT_INRANGE, parameter);
      }

       // check if the specified value provided for the reconciliation operation
       // is in range
      this.operation = Operation.valueOf(stringValue(OPERATION));
      if (this.operation == null) {
        final String[] parameter = {OPERATION, Operation.typeString() };
        throw new TaskException(TaskError.TASK_ATTRIBUTE_NOT_INRANGE, parameter);
      }

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
   ** @throws TaskException    in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    // ensure inheritance
    super.beforeExecution();

    final String method  = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    this.facade = this.lookupFacade();
    try {
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
          Map<String, String> attribute = new HashMap<String, String>();
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
        // unfortunately we facing a bug if we try to map the pseudo column
        // code Lookup.GROUP in the standard way
        // A wrong value is mapped in the lookup definition
        // Lookup Definition.Group for the field information.
        // Instead of LKU_GROUP the mispelled value LKU_TYPE_GROUP is
        // specified thus we are mapping the native column name
        attribute.put("LKU_GROUP", stringValue(LOOKUPGROUP));
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

    if (this.facade != null) {
      this.facade.close();
      this.facade = null;
    }

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeLookup
  /**
   ** Merge the existing rows of a lookup definition with the values provide by
   ** the passed <code>Iterator</code>.
   **
   ** @param  lookupName         the name of the lookup definition to merge
   ** @param  entries            the {@link List} with the current values.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private final void mergeLookup(final String lookupName, final Set<String> entries)
    throws TaskException {

    final String method  = "mergeLookup";
    trace(method, SystemMessage.METHOD_ENTRY);

    int size = entries.size();
    if (size == 0) {
      info(TaskBundle.string(TaskMessage.NOTHING_TO_CHANGE));
      trace(method, SystemMessage.METHOD_EXIT);
      return;
    }
    // if we should really do reconcile ?
    if (gatherOnly()) {
      info(TaskBundle.string(TaskMessage.RECONCILE_SKIP));
      trace(method, SystemMessage.METHOD_EXIT);
      return;
    }

    // merge the passed values in the existing lookup definition
    info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, lookupName));
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(size)));
    timerStart(method);
    String encoded = null;
    String decoded = null;
    try {
      // process with the entries to add what's missing
      for (String cursor : entries) {
        // check if the current thread is able to execute or a stop signal is
        // pending
        if (isStopped())
          break;

        encoded = cursor;
        decoded = cursor;
        if (this.connection.entitlementPrefixRequired()) {
          encoded = String.format(ENTITLEMENT_ENCODED_PREFIX, this.connection.resourceInstance(), encoded);
          decoded = String.format(ENTITLEMENT_DECODED_PREFIX, this.connection.resourceName(),    decoded);
        }
        info(TaskBundle.format(TaskMessage.ENTITY_RECONCILE, TaskBundle.string(TaskMessage.ENTITY_LOOKUP), decoded));

        final String current = exists(encoded);
        // Fix Defect DE-000079
        // Reconciliation does not check if a key/value pair is really changed.
        if (StringUtility.isEmpty(current))
          try {
            // we are not know the value in the passed result set, means we have
            // to create one
            this.facade.addLookupValue(lookupName, encoded, decoded, this.defaultLanguage, this.defaultCountry);
            this.incrementSuccess();
          }
          catch (tcAPIException e) {
            this.incrementFailed();
            error(method, ReconciliationBundle.format(ReconciliationMessage.CREATE_VALUE, lookupName, encoded));
          }
        else if (current.equals(decoded)) {
          this.incrementIgnored();
          warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, TaskBundle.string(TaskMessage.ENTITY_LOOKUP), encoded));
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
            incrementSuccess();
          }
          catch (tcAPIException e) {
            this.incrementFailed();
            warning(method, ReconciliationBundle.format(ReconciliationMessage.DUPLICATE_VALUE, lookupName, encoded));
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
      info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
    }
    info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, lookupName));
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

    final DatabaseFilter filter    = DatabaseFilter.build(this.lookupCode, DatabaseFilter.build("lkv.lkv_encoded", encoded, DatabaseFilter.Operator.EQUAL), DatabaseFilter.Operator.AND);
    final DatabaseSelect statement = DatabaseSelect.build(this, VALUE, filter, CollectionUtility.list(Pair.of("lkv.lkv_decoded", this.catalogType.name())));

    String found = null;
    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire();
      final List<Map<String, Object>> existingSet = statement.execute(connection);
      if (existingSet.size() > 1)
        throw new TaskException(TaskError.ENTITY_AMBIGUOUS);

      if (existingSet.size() == 1)
        found = (String)existingSet.get(0).get("tt");
    }
    finally {
      DatabaseConnection.release(connection);
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return found;
  }
}