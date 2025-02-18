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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Identity Manager Connector

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
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.sql.Connection;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidAttributeException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.dataobj.util.tcProperties;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.naming.Property;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.rmi.IdentityServerResource;

import oracle.iam.identity.gis.service.ManagedServer;

import oracle.iam.identity.gis.resource.ReconciliationBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class LookupReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile information from Identity Manager
 ** itself.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
abstract class LookupReconciliation extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** type of reconciliation operation should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String       OPERATION    = "Reconciliation Operation";

  /**
   ** Attribute tag which must be defined on this task to specify the name of
   ** the Lookup Group the Lookup Definition will be assigned to if the Lookup
   ** Definition has to be created during the reconciliation.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String       LOOKUPGROUP  = "Lookup Group";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the encoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String       ENCODEDVALUE = "Encoded Value";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** content of the decoded value of the lookup definition.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String       DECODEDVALUE = "Decoded Value";

  private static final DatabaseEntity LOOKUP       = DatabaseEntity.build(null, "lku",     "lku_key");
  private static final DatabaseEntity VALUE        = DatabaseEntity.build(null, "lku,lkv", "lkv_key");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

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
    @SuppressWarnings("compatibility:3982613884508042073")
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
  // Method:   returningAttributes (Reconciliation)
  /**
   ** Build the array of attribute names that this task will be reconcile from
   ** the source system.
   **
   ** @return                    the array of attribute names that this task
   **                            will be reconcile.
   */
  @Override
  protected final Set<String> returningAttributes() {
    return CollectionUtility.set(stringValue(ENCODEDVALUE), stringValue(DECODEDVALUE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do target reconciliation of Oracle Identity Manager Users.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected final void processSubject(final Map<String, Object> subject)
    throws TaskException {

    final String method  = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
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

    this.resource      = IdentityServerResource.build(this, this.resourceName());
    this.server        = new ManagedServer(this, this.resource);
    this.targetService = this.server.connect();

    if (this.logger.debugLevel())
      debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.server.toString()));

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
   ** @param  encoded            the encoded value to reconcile.
   ** @param  decoded            the decoded value to reconcile.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected final void mergeLookup(String encoded, String decoded)
    throws TaskException {

    // check if the current thread is able to execute or a stop signal is
    // pending
    if (isStopped())
      return;

    final String method  = "mergeLookup";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    if (this.server.entitlementPrefixRequired()) {
      encoded = String.format(ENTITLEMENT_ENCODED_PREFIX, this.resource.instance(), encoded);
      decoded = String.format(ENTITLEMENT_DECODED_PREFIX, this.resource.name(), decoded);
    }
    try {
      info(TaskBundle.format(TaskMessage.ENTITY_RECONCILE, TaskBundle.string(TaskMessage.ENTITY_LOOKUP), decoded));

      final String current = exists(encoded);
      if (StringUtility.isEmpty(current))
        try {
          // we are not know the value in the passed result set, means we have
          // to create one
          this.facade.addLookupValue(reconcileObject(), encoded, decoded, this.defaultLanguage, this.defaultCountry);
          this.incrementSuccess();
        }
        catch (tcAPIException e) {
          this.incrementFailed();
          error(method, ReconciliationBundle.format(ReconciliationMessage.CREATE_VALUE, reconcileObject(), encoded));
        }
      // Fix Defect DE-000080
      // Reconciliation does not check if a key/value pair is really changed.
      else if (current.equals(decoded)) {
        this.incrementIgnored();
        warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, TaskBundle.string(TaskMessage.ENTITY_LOOKUP), decoded));
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
          warning(method, ReconciliationBundle.format(ReconciliationMessage.DUPLICATE_VALUE, reconcileObject(), decoded));
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
      String parameter[] = { reconcileObject(), encoded };
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

    final DatabaseFilter    filter     = DatabaseFilter.build(
      this.lookupCode
    , DatabaseFilter.build("lkv.lkv_encoded", encoded, DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect statement  = DatabaseSelect.build(this, VALUE, filter, CollectionUtility.list(Pair.of("lkv.lkv_decoded", "x")));
    String found = null;
    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire();
      final List<Map<String, Object>> existingSet = statement.execute(connection);
      if (existingSet.size() > 1)
        throw new TaskException(TaskError.ENTITY_AMBIGUOUS);

      if (existingSet.size() == 1)
        found = (String)existingSet.get(0).get("x");
    }
    finally {
      DatabaseConnection.release(connection);
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return found;
  }
}