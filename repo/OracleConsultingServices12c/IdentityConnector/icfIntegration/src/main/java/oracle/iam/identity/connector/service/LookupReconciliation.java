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
    Subsystem   :   Connector Bundle Framework

    File        :   LookupReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LookupReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidAttributeException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.dataobj.util.tcProperties;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.sql.Connection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.object.Pair;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.Property;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;
import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.resource.TaskBundle;

import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;

////////////////////////////////////////////////////////////////////////////////
// abstract class LookupReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupReconciliation</code> acts as the service end point for
 ** Identity Manager to reconcile <code>Lookup Definition</code>s from a
 ** Service Provider.
 ** <p>
 ** This class provides also the callback interface for operations that are
 ** returning one or more results. Currently used only by Search, but may be
 ** used by other operations in the future.
 ** <p>
 ** The following scheduled task parameters are expected to be defined:
 ** <ul>
 **   <li>IT Resource              - The IT Resource used to establish the
 **                                  connection to the target system.
 **   <li>Reconciliation Object    - the name of the Lookup Definition to
 **                                  reconcile.
 **   <li>Reconciliation Source    - connector <code>ObjectClass</code> name
 **   <li>Reconciliation Operation - The operation to perform on the object to
 **                                  reconcile. Has to be either Refresh or
 **                                  Update
 **   <li>Lookup Group             - The value written to Lookup Group in case
 **                                  the operation on a particular Lookup
 **                                  Definition has to create it
 **   <li>Encoded Value            - The name of the attribute that has to be
 **                                  stored as the encoded value
 **                                  (eg: <code>__UID__</code>,
 **                                  <code>__NAME__</code>, ...)
 **   <li>Decode Value             - The name of the attribute that has to be
 **                                  stored as the encoded value
 **                                  (eg: <code>__NAME__</code>,
 **                                  groupDescription, ...)
 **   <li>Batch Size               - Specifies the size of a block read from the
 **                                  reconciliation source
 ** </ul>
 ** The implementation will execute connector search with parameters provided by
 ** the following methods:
 ** <ul>
 **   <li>{@link LookupReconciliation#objectClass()}
 **   <li>{@link LookupReconciliation#filter()}
 **   <li>{@link LookupReconciliation#operationOptions()}</li>
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class LookupReconciliation extends    Reconciliation
                                           implements ResultsHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** type of reconciliation operation should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String OPERATION              = "Reconciliation Operation";

  /**
   ** Attribute tag which must be defined on this task to specify the name of
   ** the Lookup Group the Lookup Definition will be assigned to if the Lookup
   ** Definition has to be created during the reconciliation.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String LOOKUP_GROUP           = "Lookup Group";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the encoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String ENCODED_VALUE          = "Encoded Value";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the decoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String DECODED_VALUE          = "Decoded Value";

  /**
   ** Attribute tag which must be defined on a <code>Scheduled Task</code>
   ** to specify if the entitlement loaded from a file needs to be prefixed with
   ** the internal system identifier and/or the name of the
   ** <code>IT Resource</code>.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String ENTITLEMENT_PREFIX    = "Entitlement Prefix Required";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  protected static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,             TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,        TaskAttribute.MANDATORY)
    /** the task attribute to specify the identifier of the object type */
  , TaskAttribute.build(RECONCILE_SOURCE,        TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,               TaskAttribute.MANDATORY)
    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Refresh | Update
     */
  , TaskAttribute.build(OPERATION,              TaskAttribute.MANDATORY)
    /** the task attribute to resolve the name of the Lookup Group */
  , TaskAttribute.build(LOOKUP_GROUP,           TaskAttribute.MANDATORY)
    /** the task attribute to resolve the encoded value */
  , TaskAttribute.build(ENCODED_VALUE,          TaskAttribute.MANDATORY)
    /** the task attribute to resolve the decoded value */
  , TaskAttribute.build(DECODED_VALUE,          TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,     TaskAttribute.MANDATORY)
    /** the task attribute with search filter */
  , TaskAttribute.build(SEARCH_FILTER,          TaskAttribute.OPTIONAL)
    /**
     ** the task attribute that specifies the search conatainer object class of
     ** the query
     */
  , TaskAttribute.build(SEARCH_CONTAINER,       SystemConstant.EMPTY)
    /** the task attribute that specifies the search base of the query */
  , TaskAttribute.build(SEARCH_BASE,            SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,            SystemConstant.TRUE)
  };

  private static final DatabaseEntity LOOKUP   = DatabaseEntity.build(null, "lku",     "lku_key");
  private static final DatabaseEntity VALUE    = DatabaseEntity.build(null, "lku,lkv", "lkv_key");

  /** the category of the logging facility to use */
  private static final String         CATEGORY = "OCS.ICF.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                     entitlementPrefixRequired;
  private Operation                   operation;
  private String                      defaultLanguage;
  private String                      defaultCountry;

  private tcLookupOperationsIntf      facade;
  private DatabaseFilter              lookupCode;

  // initialize the expressions used for encoded and decoded values
  private Set<String>                 returning;
  private final Binding               binding = new Binding();
  private final GroovyShell           shell   = new GroovyShell(this.binding);

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
       ** Any reference that the recociled <code>Lookup Definition</code> use
       ** and referes to a mapping that no longer exists after the operation was
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
    @SuppressWarnings("compatibility:1827267480662195426")
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
    super(CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupName
  /**
   ** Returns the name of the <code>Lookup Definition</code> supposed to be
   ** filled by this reconciliation.
   ** <br>
   ** The value is fetched from {@link #RECONCILE_OBJECT} job parameter.
   ** <br>
   ** Convenience method to shortens the access to the job parameter
   ** {@link #RECONCILE_OBJECT}.
   **
   ** @return                    the name of the <code>Lookup Definition</code>
   **                            supposed to be filled by this reconciliation.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String lookupName() {
    return this.reconcileObject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoded
  /**
   ** Returns the name of connector attribute whose value is supposed to be used
   ** as encoded value.
   **
   ** @return                    the {@link Set} of names to build the encoded
   **                            attribute value.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws RuntimeException   if the value for the attribute
   **                            {@link #ENCODED_VALUE} cannot be parsed.
   */
  protected final Set<String> encoded() {
    return ExpressionFactory.parse(stringValue(ENCODED_VALUE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decoded
  /**
   ** Returns the name of connector attribute whose value is supposed to be used
   ** as dencoded value.
   **
   ** @return                    the {@link Set} of names to build the encoded
   **                            attribute value.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws RuntimeException   if the value for the attribute
   **                            {@link #ENCODED_VALUE} cannot be parsed.
   */
  protected final Set<String> decoded() {
    return ExpressionFactory.parse(stringValue(DECODED_VALUE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handle (ResultsHandler)
  /**
   ** Call-back method to do whatever it is the caller wants to do with each
   ** {@link ConnectorObject} that is returned in the result of
   ** <code>SearchApiOp</code>.
   **
   ** @param  object             each object return from the search.
   **
   ** @return                    <code>true</code> if we should keep processing;
   **                            otherwise <code>false</code> to cancel.
   */
  @Override
  public boolean handle(final ConnectorObject object) {
    final String method = "handle";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      for (String cursor : this.returning) {
        // GroovyShell is stupid because its not understanding dotted syntax of
        // a variable therefore we need to replace any dot with a different
        // character so that GroovyShell is able to compile and evaluate
        this.binding.setProperty(groovify(cursor), singleValue(object.getAttributeByName(cursor)));
      }
      processPair(evaluate(stringValue(ENCODED_VALUE)), evaluate(stringValue(DECODED_VALUE)));
      return !stopped();
    }
    catch (TaskException e) {
      fatal(method, e);
      return false;
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Identity Manager.
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
    // obtain the state of the job before any of the attributes are subject of
    // change
    final OperationOptions options = operationOptions();
    // set the current date as the timestamp on which this task has been last
    // reconciled at the start of execution
    // setting it here to have it the next time this scheduled task will
    // run the changes made during the execution of this task
    // updating this attribute will not perform to write it back to the
    // scheduled job attributes it's still in memory; updateLastReconciled()
    // will persist the change that we do here only if the job completes
    // successful
    lastReconciled(systemTime());
    try {
      this.connector.search(DescriptorTransformer.objectClass(reconcileSource()), filter(), this, options);
    }
    finally {
      // inform the observing user about the overall result of this task
      if (isStopped()) {
        final String[] arguments = { reconcileObject(), getName(), resourceName(), "Veto"};
        warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      }
      // if an exception occured
      else if (getResult() != null) {
        final String[] arguments = { reconcileObject(), getName(), resourceName(), getResult().getLocalizedMessage()};
        error("onExecution", TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      }
      // complete with success and write back timestamp
      else {
        // update the timestamp on the task
        updateLastReconciled();
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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

    final String method  = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // ensure inheritance
      super.initialize();

      // initialize the database filter to lookup values from the Lookup
      // Definition this task is reconciling
      this.lookupCode = DatabaseFilter.build(
        DatabaseFilter.build("lku.lku_type_string_key", lookupName(), DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.build("lkv.lku_key",             LOOKUP,       DatabaseFilter.Operator.EQUAL)
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

      this.defaultLanguage           = tcProperties.getValue(Property.LANGUAGE, Property.DEFAULT_LANGUAGE);
      this.defaultCountry            = tcProperties.getValue(Property.REGION,   Property.DEFAULT_REGION);
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
      // ensure inheritance
      super.beforeExecution();
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
  protected void processPair(String encoded, String decoded)
    throws TaskException {

    if ((encoded == null) || (decoded == null))
      throw new NullPointerException("Code encoded and decode value cannot be null");

    final String method  = "processPair";
    trace(method, SystemMessage.METHOD_ENTRY);

    // extracts the encoded value
    String lookupName = reconcileObject();
    if (this.entitlementPrefixRequired) {
      encoded = String.format(PATTREN_ENCODED_VALUE, this.resource.instance(), encoded);
      decoded = String.format(PATTREN_DECODED_VALUE, this.resource.name(),     decoded);
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
  // Method:   evaluate
  /**
   ** Returns the value used for encoded/decoded value, the value should be
   ** based on the provided {@link ConnectorObject} returned by the main search.
   **
   ** @param  expression         the expression to evaluate.
   **                            <br>
   **                            Allowed object is {@link ConnectorObject}.
   **
   ** @return                    the value to store as the encoded value in the
   **                            <code>Lookup Definition</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String evaluate(final String expression) {
    // GroovyShell is stupid because its not understanding dotted syntax of a
    // variable therefore we need to replace any dot with a different character
    // so that GroovyShell is able to compile and evaluate
    return (String)this.shell.evaluate(groovify(expression));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationBuilder
  /**
   ** Factory method used in the lookup reconciliation search to create the
   ** {@link OperationOptionsBuilder} from the default parameters of a job like
   ** <code>Incremental</code>.
   ** <p>
   ** The default implementation builds {@link OperationOptions} with
   ** ATTRS_TO_GET set to {@link #ENCODED_VALUE} and {@link #DECODED_VALUE}.
   **
   **
   ** @return                    the {@link OperationOptionsBuilder} containing
   **                            the defaults to query the target system.
   **                            <br>
   **                            Possible object is
   **                            {@link OperationOptionsBuilder}.
   */
  protected OperationOptionsBuilder operationBuilder() {
    // add Filter attributes if Filter is specified
    Set<String> returning = filterAttributes();
    if (returning == null) {
      returning = new HashSet<String>();
    }
    // lazy init returned attribute filtering
    this.returning = CollectionUtility.union(encoded(), decoded());
    returning.addAll(this.returning);
    // build it
    final OperationOptionsBuilder factory = new OperationOptionsBuilder();
    factory.setAttributesToGet(returning);
    if (this.conatinerClass != null) {
      factory.setContainer(new QualifiedUid(this.conatinerClass, new Uid(stringValue(SEARCH_BASE))));
    }
    final Map<String, Object> option = factory.getOptions();
    option.put(BATCH_SIZE, batchSize());
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Check if the specified <code>encoded</code> value exists in the given
   ** result set.
   **
   ** @param  encoded            <code>encoded</code> value to check.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the decoded value for the passed
   **                            <code>encoded</code> value or <code>null</code>
   **                            if no value is mapped for <code>encoded</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @SuppressWarnings("unchecked") 
  private String exists(final String encoded)
    throws TaskException  {

    final String method  = "exists";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final DatabaseFilter filter = DatabaseFilter.build(
      this.lookupCode
    , DatabaseFilter.build("lkv.lkv_encoded", encoded, DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect statement = DatabaseSelect.build(this, VALUE, filter, CollectionUtility.list(Pair.of("lkv.lkv_decoded", "decoded")));

    String     found      = null;
    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire();
      final List<Map<String, Object>> existingSet = statement.execute(connection);
      if (existingSet.size() > 1)
        throw new TaskException(TaskError.ENTITY_AMBIGUOUS);

      if (existingSet.size() == 1)
        found = (String)existingSet.get(0).get("decoded");
    }
    finally {
      DatabaseConnection.release(connection);
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return found;
  }
}