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

    File        :   LookupProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.provisioning;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collection;

import java.sql.Connection;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.object.Pair;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.iam.identity.foundation.AbstractServiceTask.Batch;
import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSearch;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.offline.LookupEntity;
import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.EntitlementEntity;

import oracle.iam.identity.utility.file.XMLLookupFactory;

import oracle.iam.identity.ots.persistence.Repository;

import oracle.iam.identity.ots.persistence.dialect.NamespaceValue;

import oracle.iam.identity.ots.resource.ProvisioningBundle;

////////////////////////////////////////////////////////////////////////////////
// class LookupProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupProvisioning</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** lookup definition data spooled to a XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class LookupProvisioning extends Provisioning<EntitlementEntity> {

  /**
   ** Attribute tag which must be defined on a <code>Scheduled Task</code>
   ** to specify if the lookup identifier spooled out to a XML file.
   ** <br>
   ** This attribute is mandatory.
   */
   private static final String PROVISIONING_OBJECT = "Provisioning Object";

  /**
   ** Attribute tag which must be defined on a <code>Scheduled Task</code>
   ** to specify if the entitlement spooled out to a file needs to be stripped
   ** form the encoded and/or decoded values.
   ** <br>
   ** This attribute is mandatory.
   */
   private static final String ENTITLEMENT_PREFIX = "Entitlement Prefix Stripped";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** element name of the encoded value written to the XML file.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ENCODED_NAME        = "Encoded Name";

  /**
   ** Attribute tag which must be defined on a Scheduled Task to specify the
   ** element name of the decoded value written to the XML file..
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String DECODED_NAME        = "Decoded Name";

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
  , TaskAttribute.build(PROVISIONING_OBJECT,  TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(INCREMENTAL,          TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,   TaskAttribute.MANDATORY)
    /** the task attribute to resolve the encoded value */
  , TaskAttribute.build(ENCODED_NAME,         TaskAttribute.MANDATORY)
    /** the task attribute to resolve the decoded value */
  , TaskAttribute.build(DECODED_NAME,         TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATAFILE,             TaskAttribute.MANDATORY)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,          TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,         TaskAttribute.MANDATORY)
    /** the class name of the entity factory  */
  , TaskAttribute.build(MARSHALLER,           TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                       entitlementPrefixStripped;

  private Repository                    repository        = null;

  /** the lookup element of the XML file to produce */
  private XMLOutputNode                 lookup            = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupProvisioning</code> task adapter
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupProvisioning() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (EntityListener)
  /**
   ** Do all action which should take place for marshal for a particular
   ** bulk of {@link LookupEntity}'s.
   **
   ** @param  bulk               the {@link Collection} of
   **                            {@link LookupEntity}'s to marshal.
   **
   ** @throws TaskException      if the marshalling of the value pair as a
   **                            lookup value fails.
   */
  @Override
  public void process(final Collection<EntitlementEntity> bulk)
    throws TaskException {

    final String method = "process";
    trace(method, SystemMessage.METHOD_ENTRY);

    // validate the effort to do
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(bulk.size())));
    for (EntitlementEntity entity : bulk) {
      XMLOutputNode entitlement = null;
      try {
        // create the enclosing element of an entitlement
        entitlement = this.lookup.element(EntitlementEntity.SINGLE);
        entitlement.attribute(XMLLookupFactory.ATTRIBUTE_ID, entity.name());

        // create the enclosing element of an entitlement
        final XMLOutputNode attribute = entitlement.element(EntityFactory.ELEMENT_ATTRIBUTE);
        attribute.attribute(XMLLookupFactory.ATTRIBUTE_ID, stringValue(DECODED_NAME));
        attribute.value((String)entity.attribute().get(stringValue(DECODED_NAME)));
        incrementSuccess();
      }
      catch (XMLException e) {
        // TODO: improve error handling
        error(method, TaskBundle.format(TaskMessage.PROVISIONING_ERROR, entity.name(), reconcileObject()));
        incrementFailed();
      }
    }
    info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
    trace(method, SystemMessage.METHOD_EXIT);
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
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the reconciliation task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    final String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, parameter));

    info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, parameter));

    final Date lastExceution = lastReconciled();

    ensureLookup(stringValue(PROVISIONING_OBJECT));

    // initialize the database filter to lookup values from a Lookup Definition
    // this task provisions
    DatabaseFilter filter = this.repository.namespaceValueFilter(this.reconcileObject());
    // extend the filter if incremental approach is requested by the
    // configutaion parameter
    if (booleanValue(INCREMENTAL)) {
      filter = DatabaseFilter.build(
        filter
      , DatabaseFilter.build(
          DatabaseFilter.build(NamespaceValue.CREATE, lastExceution, DatabaseFilter.Operator.GREATER_EQUAL)
        , DatabaseFilter.build(NamespaceValue.MODIFY, lastExceution, DatabaseFilter.Operator.GREATER_EQUAL)
        , DatabaseFilter.Operator.OR
        )
      , DatabaseFilter.Operator.AND
      );
    }

    final List<Pair<String, String>> returning = CollectionUtility.list(
      Pair.of(NamespaceValue.ENCODED, stringValue(ENCODED_NAME))
    , Pair.of(NamespaceValue.DECODED, stringValue(DECODED_NAME))
    );
    final Batch          batch      = new Batch(batchSize());
    final DatabaseSearch search     = new DatabaseSearch(this, this.repository.entity(Repository.Entity.NAMESPACEVALUE), filter, returning);
    Connection           connection = null;
    try {
      connection = DatabaseConnection.aquire();
      search.prepare(connection);
      List<Map<String, Object>> result = null;
      do {
        ProvisioningBundle.string(ProvisioningMessage.COLLECTING_BEGIN);
        result = search.execute(batch.start(), batch.end());
        ProvisioningBundle.string(ProvisioningMessage.COLLECTING_COMPLETE);

        // check a pending stop signal
        if (isStopped())
          break;

        if (result != null && result.size() > 0) {
          final List<EntitlementEntity> bulk = new ArrayList<EntitlementEntity>(result.size());
          for (Map<String, Object> entry : result) {
            String encoded = (String)entry.get(stringValue(ENCODED_NAME));
            String decoded = (String)entry.get(stringValue(DECODED_NAME));
            if (this.entitlementPrefixStripped) {
              encoded = encoded.substring(encoded.lastIndexOf('~'));
              decoded = decoded.substring(encoded.lastIndexOf('~'));
            }
            final EntitlementEntity entitlement = new EntitlementEntity(encoded);
            entitlement.addAttribute(DECODED_NAME, decoded);
            bulk.add(entitlement);
          }
          listener().process(bulk);
        }
        batch.next();
      } while (!isStopped() && result != null && result.size() == batch.size());

      this.updateLastReconciled();
      info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE,      parameter));
      info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
    }
    catch (TaskException e) {
      final String[] arguments = { reconcileObject(), getName(), dataFile().getAbsolutePath(), e.getLocalizedMessage()};
      // notify user about the problem
      warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      throw e;
    }
    finally {
      search.close();
      DatabaseConnection.release(connection);
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
  public void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter and create
    // the abstract file paths to the data and error directories
    super.initialize();

    final String method  = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    // initialize the naming convention for the lookup values by assuming that
    // the any entry needs to be prefixed if the parameter is not specified
    this.entitlementPrefixStripped = booleanValue(ENTITLEMENT_PREFIX, true);
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureLookup
  /**
   ** This is used to create the lookup element within the XML document.
   **
   ** @param  name               the name of the root element to create.
   */
  private void ensureLookup(final String name)
    throws TaskException {

    // prevent bogus input
    if (StringUtility.isEmpty(name))
      throw TaskException.argumentIsNull("name");

    final XMLOutputNode application = root();
    try {
      // first if we are doing this for the first time
      if (this.lookup == null) {
        // this will implictly commit the previous lookup created but should
        // never happens due to the condition checked above
        this.lookup = application.element(name);
      }
      // nevertheless how the current state of the lookup element if the name
      // of the current lookup marshalled to the XML file does not match the
      // name specified in the arguments it's required to create a new one
      if (!name.equals(this.lookup.name()))
        // this will implictly commit the previous lookup created and all
        // depended XML nodes in the buffer
        this.lookup = application.element(name);
    }
    catch (XMLException e) {
      throw new TaskException(e);
    }
  }
}