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
    Subsystem   :   Offline Target Connector

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
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.reconciliation;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import java.sql.Connection;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidAttributeException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcInvalidLookupException;

import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.dataobj.util.tcProperties;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.AbstractResource;

import oracle.iam.identity.foundation.naming.Property;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.LookupEntity;
import oracle.iam.identity.foundation.offline.EntitlementEntity;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.ots.service.ControllerMessage;

import oracle.iam.identity.ots.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// class LookupReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupReconciliation</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** lookup definition data provided by XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class LookupReconciliation extends Reconciliation<LookupEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** type of reconciliation operation should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String OPERATION          = "Reconciliation Operation";

  /**
   ** Attribute tag which must be defined on this task to specify the name of
   ** the Lookup Group the Lookup Definition will be assigned to if the Lookup
   ** Definition has to be created during the reconciliation.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String LOOKUPGROUP        = "Lookup Group";

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
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the encoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ENCODED_VALUE      = "Encoded Value";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the decoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String DECODED_VALUE      = "Decoded Value";

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
     */
  , TaskAttribute.build(OPERATION,            TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,   TaskAttribute.MANDATORY)
    /** the task attribute to resolve the name of the Lookup Group */
  , TaskAttribute.build(LOOKUPGROUP,          TaskAttribute.MANDATORY)
    /** the task attribute to resolve the encoded value */
  , TaskAttribute.build(ENCODED_VALUE,        TaskAttribute.MANDATORY)
    /** the task attribute to resolve the decoded value */
  , TaskAttribute.build(DECODED_VALUE,        TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATAFILE,             TaskAttribute.MANDATORY)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,          TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,         TaskAttribute.MANDATORY)
    /** the class name of the entity factory  */
  , TaskAttribute.build(UNMARSHALLER,         TaskAttribute.MANDATORY)
    /** the validation required before unmarshalling  */
  , TaskAttribute.build(VALIDATE_SCHEMA,      TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
  };

  private static final DatabaseEntity   LOOKUP = DatabaseEntity.build(null, "lku",     "lku_key");
  private static final DatabaseEntity   VALUE  = DatabaseEntity.build(null, "lku,lkv", "lkv_key");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                       entitlementPrefixRequired;
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
    @SuppressWarnings("compatibility:7556218218026775840")
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
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (EntityListener)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** bulk of {@link LookupEntity}'s.
   **
   ** @param  bulk               the {@link Collection} of
   **                            {@link LookupEntity}'s to reconcile.
   **
   ** @throws TaskException      if the reconciliation of the value pair as a
   **                            lookup value fails.
   */
  @Override
  public void process(final Collection<LookupEntity> bulk)
    throws TaskException {

    final String method = "process";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      // validate the effort to do
      if (gatherOnly()) {
        incrementIgnored(bulk.size());
        info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
      }
      else {
        for (LookupEntity lookup : bulk) {
          if (isStopped())
            break;
          reconcile(lookup);
        }
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
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
  protected TaskAttribute[] attributes() {
    return attribute;
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
  public void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter and create
    // the abstract file paths to the data and error directories
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

      // initialize the naming convention for the lookup values by assuming that
      // the any entry needs to be prefixed if the parameter is not specified
      this.entitlementPrefixRequired = booleanValue(ENTITLEMENT_PREFIX, true);

      // obtain the default properties of the server need to maintain lookups
      this.defaultLanguage = tcProperties.getValue(Property.LANGUAGE, Property.DEFAULT_LANGUAGE);
      this.defaultCountry  = tcProperties.getValue(Property.REGION,   Property.DEFAULT_REGION);
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
  // Method:   beforeReconcile (Reconciliation)
  /**
   ** The call back method just invoked before reconciliation finished.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeReconcile()
    throws TaskException {

    final String method  = "beforeReconcile";
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
        // ... and update the group field
        Map<String, String> attribute = new HashMap<String, String>();
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
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterReconcile (Reconciliation)
  /**
   ** The call back method just invoked after reconciliation finished.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void afterReconcile()
    throws TaskException {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconcile (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** {@link LookupEntity}.
   **
   ** @param  lookup             the {@link LookupEntity} to reconcile.
   **
   ** @throws TaskException      if the reconciliation of the value pair as a
   **                            lookup value fails.
   */
  protected void reconcile(final LookupEntity lookup)
    throws TaskException {

    final List<EntitlementEntity> collection = lookup.entitlement();
    if (collection.size() == 0)
      info(TaskBundle.string(TaskMessage.NOTHING_TO_CHANGE));
    else {
      // validate the effort to do
      info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(collection.size())));
      // if we should really do reconcile ?
      if (gatherOnly())
        info(TaskBundle.string(TaskMessage.RECONCILE_SKIP));
      else {
        for (EntitlementEntity entitlement : collection) {
          if (isStopped())
            break;
          // extracts the decoded value
          final String decoded = (String)entitlement.attribute().get(stringValue(DECODED_VALUE));
          processPair(entitlement.name(), decoded);
        }
        info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
      }
    }
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

    final String method  = "processPair";
    trace(method, SystemMessage.METHOD_ENTRY);

    // extracts the encoded value
    String lookupName = reconcileObject();
    if (this.entitlementPrefixRequired) {
      final AbstractResource resource = this.resource();
      encoded = String.format(ENTITLEMENT_ENCODED_PREFIX, resource.instance(), encoded);
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
            error(method, ControllerBundle.format(ControllerMessage.CREATE_VALUE, lookupName, encoded));
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

    final DatabaseFilter filter    = DatabaseFilter.build(this.lookupCode, DatabaseFilter.build("lkv.lkv_encoded", encoded, DatabaseFilter.Operator.EQUAL), DatabaseFilter.Operator.AND);
    final DatabaseSelect statement = DatabaseSelect.build(this, VALUE, filter, CollectionUtility.list(Pair.of("lkv.lkv_decoded", stringValue(DECODED_VALUE))));

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