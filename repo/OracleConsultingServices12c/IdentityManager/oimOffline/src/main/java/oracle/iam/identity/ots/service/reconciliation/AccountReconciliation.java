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

    File        :   AccountReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountReconciliation.


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
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskDescriptor;
import oracle.iam.identity.foundation.AbstractResource;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.ApplicationAccount;

import oracle.iam.identity.utility.file.XMLEntityFactory;

////////////////////////////////////////////////////////////////////////////////
// class AccountReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountReconciliation</code> implements the base functionality of
 ** a service end point for the Oracle Identity Manager Scheduler which handles
 ** account data provided by XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class AccountReconciliation extends EntityReconciliation<ApplicationAccount> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

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
   ** the array with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(APPLICATION_INSTANCE, TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,   TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
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

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean entitlementPrefixRequired;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountReconciliation</code> scheduled job that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (EntityListener)
  /**
   ** Reconciles a particular bulk of {@link ApplicationAccount}s.
   **
   ** @param  bulk               the {@link Collection} of
   **                            {@link ApplicationAccount}s to reconcile.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void process(final Collection<ApplicationAccount> bulk)
    throws TaskException {

    final String method = "process";
    trace(method, SystemMessage.METHOD_ENTRY);

    // validate the effort to do
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(bulk.size())));
    try {
      if (gatherOnly()) {
        incrementIgnored(bulk.size());
        info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
      }
      else {
        for (ApplicationAccount account : bulk) {
          if (isStopped())
            break;
          reconcile(account);
        }
      }
    }
    finally {
      info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractSchedulerBaseTask)
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
  // Method:   beforeReconcile (Reconciliation)
  /**
   ** The call back method just invoked before reconciliation finished.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeReconcile()
    throws TaskException {

    // intentionally left blank
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

    // intentionally left blank
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

    // initialize the naming convention for the lookup values by assuming that
    // the any entry needs to be prefixed if the parameter is not specified
    this.entitlementPrefixRequired = booleanValue(ENTITLEMENT_PREFIX, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconcile
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** {@link ApplicationAccount}.
   ** <br>
   ** This will do target reconciliation of Oracle Identity Manager Accounts.
   **
   ** @param  account            the {@link ApplicationAccount} to handle in
   **                            the namespace of an <code>Application</code>.
   **
   ** @throws TaskException      if the operation fails for any reason
   */
  protected void reconcile(final ApplicationAccount account)
    throws TaskException {

    final String method = "reconcile";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final Map<String, Object> attribute = account.attribute();
      // put the name of the account as the id in the attributes to ensure that
      // the master mapping picks it up correctly
      attribute.put(XMLEntityFactory.ATTRIBUTE_ID, account.name());
      // hmmmm, should we really chain
      final Map<String, Object> master = transformMaster(createMaster(account.attribute(), true));
      // put the proper account status in the attribute mapping
      switch (account.action()) {
        case enable  : master.put("Status", "Enabled");
                       break;
        case disable : master.put("Status", "Disabled");
                       break;
        default      : master.remove("Status");
      }

      if (account.action() == ApplicationAccount.Action.revoke)
        // create the reconciliation event for deletion
        deleteEvent(master);
      else {
        // create a container for the account child data
        // all the multi-valued attributes and entry references the account has
        // will be assembled here
        final Map<String, List<Map<String, Object>>> childdata = new HashMap<String, List<Map<String, Object>>>();
        for (String entitlement : this.descriptor.entitlement().keySet()) {
          // the DescriptorFactory has unmarshalled an EntitlementDescriptor
          // containing the attribute and transformation mapping used by this
          // descriptor
          final TaskDescriptor.Entitlement descriptor = (TaskDescriptor.Entitlement)this.descriptor.entitlement().get(entitlement);
          // the EntitlementDescriptor obtained above has an identifier
          // (systemID) that reflects the raw name of the entitlements delivered
          // by the target system and needed to fetch the data from the enlisted
          // entitlements on the account
          final List<Map<String, Object>> entries = populateEntitlement(account.entitlement(descriptor.systemID()), descriptor);
          childdata.put(entitlement, entries);
        }
        // process a event for creation or modification
        processChangeLogEvent(master, childdata);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateEntitlement
  /**
   ** This method can be overwritten and entry DN or descriptor can be modified
   ** before a populateEntitlement is executed.
   **
   ** @param  entitlement        the {@link Collection} containing all data to
   **                            be reconcile so far.
   ** @param  descriptor         the descriptor to handle a particular object
   **                            reference for the specified
   **                            <code>identifier</code>.
   **
   ** @return                    a {@link List} where each entry is a {link Map}.
   **                            The contained {@link Map} has always a size of
   **                            <code>1</code>. The key is mapped name of the
   **                            server attribute name. The value associated
   **                            with the key is the value assigned to the
   **                            server attribute.
   */
  private List<Map<String, Object>> populateEntitlement(final Collection<EntitlementEntity> entitlement, final TaskDescriptor.Entitlement descriptor)
    throws TaskException {

    final List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
    if (!CollectionUtility.empty(entitlement)) {
      for (EntitlementEntity cursor : entitlement) {
        Map<String, Object> record = cursor.attribute();

        // put the name of the entitlement as the id in the attributes to ensure
        // that the master mapping picks it up correctly
        if (this.entitlementPrefixRequired) {
          final AbstractResource resource = this.resource();
          final String           encoded  = String.format(ENTITLEMENT_ENCODED_PREFIX, resource.instance(), cursor.name());
          record.put(XMLEntityFactory.ATTRIBUTE_ID, encoded);
        }
        else
          record.put(XMLEntityFactory.ATTRIBUTE_ID, cursor.name());

        record = descriptor.attributeMapping().filterByEncoded(record);
        if (descriptor.transformationEnabled()) {
          record = descriptor.transformationMapping().transform(record);
          if (record.isEmpty())
            throw new TaskException(TaskError.TRANSFORMATION_MAPPING_EMPTY);
        }
        content.add(record);
      }
    }
    return content;
  }
}