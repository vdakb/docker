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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   LookupReconciliation.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.service.reconciliation;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.sql.Connection;

import com.thortech.xl.dataobj.util.tcProperties;

import Thor.API.Operations.tcLookupOperationsIntf;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcInvalidAttributeException;
import Thor.API.Exceptions.tcInvalidLookupException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.naming.Property;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.reconciliation.PairHandler;

import oracle.iam.identity.foundation.schema.Pair;
import oracle.iam.identity.sap.control.Resource;

import oracle.iam.identity.sap.persistence.CatalogQuery;
import oracle.iam.identity.sap.persistence.schema.Catalog;

import oracle.iam.identity.sap.service.resource.ReconciliationBundle;

////////////////////////////////////////////////////////////////////////////////
// class LookupReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile metadata information from a SAP/R3
 ** User Management Service.
 ** <p>
 ** This class provides also the callback interface for operations that are
 ** returning one or more results. Currently used only by Search, but may be
 ** used by other operations in the future.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class LookupReconciliation extends    Reconciliation
                                  implements PairHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** type of reconciliation operation should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String OPERATION           = "Reconciliation Operation";

  /**
   ** Attribute tag which must be defined on this task to specify the name of
   ** the Lookup Group the Lookup Definition will be assigned to if the Lookup
   ** Definition has to be created during the reconciliation.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String LOOKUP_GROUP        = "Lookup Group";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the encoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ENCODED_VALUE       = "Encoded Value";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the decoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String DECODED_VALUE       = "Decoded Value";

  /**
   ** Attribute tag which must be defined on a <code>Scheduled Task</code>
   ** to specify if the entitlement loaded from a file needs to be prefixed with
   ** the internal system identifier and/or the name of the
   ** <code>IT Resource</code>.
   ** <br>
   ** This attribute is mandatory.
   */
   private static final String ENTITLEMENT_PREFIX = "Entitlement Prefix Required";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  protected static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    new TaskAttribute(IT_RESOURCE,       TaskAttribute.MANDATORY)

    /** the task attribute with reconciliation target */
  , new TaskAttribute(RECONCILE_OBJECT,  TaskAttribute.MANDATORY)

    /** the task attribute that holds the value of the last run */
  , new TaskAttribute(TIMESTAMP,         TaskAttribute.MANDATORY)

    /** the task attribute with catalog has to be reconciled */
  , new TaskAttribute(CATALOG,           TaskAttribute.MANDATORY)

    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Refresh | Update
     */
  , new TaskAttribute(OPERATION,         TaskAttribute.MANDATORY)

    /** the task attribute to resolve the name of the Lookup Group */
  , new TaskAttribute(LOOKUP_GROUP,      TaskAttribute.MANDATORY)

    /** the task attribute to resolve the encoded value */
  , new TaskAttribute(ENCODED_VALUE,     TaskAttribute.MANDATORY)

    /** the task attribute to resolve the decoded value */
  , new TaskAttribute(DECODED_VALUE,     TaskAttribute.MANDATORY)

    /** the task attribute with entitlement prefix option */
  , new TaskAttribute(ENTITLEMENT_PREFIX, TaskAttribute.MANDATORY)
  };

  private static final DatabaseEntity LOOKUP = new DatabaseEntity(null, "lku",     "lku_key");
  private static final DatabaseEntity VALUE  = new DatabaseEntity(null, "lku,lkv", "lkv_key");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                     entitlementPrefixRequired;
  private Operation                   operation;
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
     ** Any reference that the recociled <code>Lookup Definition</code> use and
     ** referes to a mapping that no longer exists after the operation was
     ** performed will be lost. The operation does not take care about if a
     ** mapping that does no longer exists is used somewhere.
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
    @SuppressWarnings("compatibility:2957838898449828019")
    private static final long serialVersionUID = 1L;

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
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handle (ResultsHandler)
  /**
   ** Call-back method to do whatever it is the caller wants to do with each
   ** entry that is returned in the result of SearchApiOp.
   **
   ** @param  data             each object return from the search.
   **
   ** @return                  <code>true</code> if we should keep processing
   **                          else <code>false</code> to cancel.
   **
   ** @throws RuntimeException which wrapes any native exception (or that
   **                          describes any other problem during execution)
   **                          that is serious enough to stop the iteration.
   */
  public boolean handle(final Pair<String, String> data) {
    final String method = "handle";
    trace(method, SystemMessage.METHOD_ENTRY);
    // validate the effort to do
    if (gatherOnly()) {
      incrementIgnored();
      info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
    }
    else {
      try {
        processPair(data.getKey(), data.getValue());
      }
      catch (TaskException e) {
        trace(method, SystemMessage.METHOD_EXIT);
        throw new RuntimeException(e);
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return !LookupReconciliation.this.isStopped();
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
   ** Reconciles the changed entries in the Directory Service.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, reconcileObject(), getName(), resourceName()));

    Catalog lookup = Catalog.fromValue(stringValue(CATALOG));

    //    new Query(this.connector, this._configuration, this.filteredAccounts, this.accountAttributes, this.parameter).executeQuery(objClass, query, this, options);
    CatalogQuery.execute(this, this.connector, null, lookup, this);
    info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes (Reconciliation)
  /**
   ** Build the array of attribute names that this task will be reconcile from
   ** the source system.
   **
   ** @return                    the array of attribute names that this task
   **                            will be reconcile.
   */
//  @Override
  protected final String[] returningAttributes() {
    final String[] attributes = { stringValue(ENCODED_VALUE), stringValue(DECODED_VALUE) };
    return attributes;
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

    final String method  = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // ensure inheritance
      super.initialize();

      // initialize the database filter to lookup values from the Lookup
      // Definition this task is reconciling
      this.lookupCode = new DatabaseFilter(
        new DatabaseFilter("lku.lku_type_string_key", this.reconcileObject(), DatabaseFilter.Operator.EQUAL)
      , new DatabaseFilter("lkv.lku_key",             LOOKUP,                 DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.Operator.AND
      );

      // check if the specified value provided for the reconciliation operation
      // is in range
      this.operation = Operation.valueOf(stringValue(OPERATION));
      if (this.operation == null) {
        final String[] parameter = {OPERATION, Operation.typeString() };
        throw new TaskException(TaskError.TASK_ATTRIBUTE_NOT_INRANGE, parameter);
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
        // unfortunately we facing a bug if we try in the standard way to
        // map the pseudo column code specified Lookup.GROUP
        // A wrong value is mapped in the lookup definition
        // Lookup Definition.Group for the field information.
        // Instead of LKU_GROUP the mispelled value LKU_TYPE_GROUP is
        // specified.
        // Therefore we are mapping the native column name
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

    if (this.facade != null) {
      this.facade.close();
      this.facade = null;
    }

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoded
  protected String encoded(final Map<String, Object> subject) {
    // obtain the attribute value for the encoded attribute if it exists else
    // null
    return (String)subject.get(stringValue(ENCODED_VALUE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decoded
  protected String decoded(final Map<String, Object> subject) {
    // obtain the attribute value for the decoded attribute if it exists else
    // null
    return (String)subject.get(stringValue(DECODED_VALUE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processPair
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
  protected  void processPair(String encoded, String decoded)
    throws TaskException {

    if ((encoded == null) || (decoded == null))
      throw new NullPointerException("Code encoded and decode value cannot be null");

    final String method  = "processPair";
    trace(method, SystemMessage.METHOD_ENTRY);

    // extracts the encoded value
    String lookupName = reconcileObject();
    if (this.entitlementPrefixRequired) {
      final Resource resource = this.connector.resource();
      encoded = String.format(ENTITLEMENT_ENCODED_PREFIX, resource.instance(), resource.name(), encoded);
      decoded = String.format(ENTITLEMENT_DECODED_PREFIX, resource.name(), decoded);
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
            error(method, ReconciliationBundle.format(ReconciliationMessage.CREATE_VALUE, reconcileObject(), encoded));
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
            this.incrementSuccess();
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
  private String exists(final String encoded)
    throws TaskException  {

    final String method  = "exists";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final DatabaseFilter filter    = new DatabaseFilter(this.lookupCode, new DatabaseFilter("lkv.lkv_encoded", encoded, DatabaseFilter.Operator.EQUAL), DatabaseFilter.Operator.AND);
    final DatabaseSelect statement = new DatabaseSelect(this, VALUE, filter, new String[][] {{"lkv.lkv_decoded", stringValue(DECODED_VALUE)}});

    String found = null;
    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire();
      final List<Map<String, Object>> existingSet = statement.execute(connection);
      if (existingSet.size() > 1) {
        final String[] arguments = { this.reconcileObject(), "lkv.lkv_encoded", encoded };
        throw new TaskException(TaskError.ENTITY_AMBIGUOUS, arguments);
      }

      if (existingSet.size() == 1)
        found = (String)existingSet.get(0).get(stringValue(DECODED_VALUE));
    }
    finally {
      DatabaseConnection.release(connection);
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return found;
  }
}