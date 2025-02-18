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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   TargetReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TargetReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.reconciliation;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.AbstractLookup;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.ldap.DirectoryName;
import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.ldap.DirectoryReference;

////////////////////////////////////////////////////////////////////////////////
// abstract class TargetReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TargetReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile target system information from a
 ** Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class TargetReconciliation extends ObjectReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,          TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,     TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values are OneLevel | SubTree | Object
     */
  , TaskAttribute.build(SEARCHSCOPE,          TaskAttribute.MANDATORY)
    /** the task attribute that specifies the search base of the query */
  , TaskAttribute.build(SEARCHBASE,           SystemConstant.EMPTY)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCHFILTER,         SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,          SystemConstant.TRUE)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<String, AbstractLookup> lookupCache = new HashMap<String, AbstractLookup>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TargetReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   */
  protected TargetReconciliation() {
    // ensure inheritance
    super(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TargetReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   **
   ** @param  multiValueFeature  the entry in the <code>Metadata Descriptor</code>
   **                            Server Feature that declares the multi-valued
   **                            attributes.
   */
  protected TargetReconciliation(final String multiValueFeature) {
    // ensure inheritance
    super(multiValueFeature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hierarchy
  /**
   ** Returns the name of the field that receives the directory hierarchy of an
   ** entry.
   **
   ** @return                    the name of the field that receives the
   **                            directory hierarchy of an entry.
   */
  protected String hierarchy() {
    return this.descriptor.hierarchy();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupCache
  /**
   ** Returns the {@link Map} containing all <code>Lookup Definition</code>s
   ** defined for this scheduled task.
   **
   ** @return                    the {@link Map} containing all
   **                            <code>Lookup Definition</code>s defined for
   **                            this scheduled task.
   */
  protected final Map<String, AbstractLookup> lookupCache() {
    return this.lookupCache;
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
  protected void processSubject(final Map<String, Object> subject)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // obtain the distinguished name from the subject
    final String entryDN = entryDN(subject);
    try {
      // hmmmm, should we really chain
      final Map<String, Object> master = transformMaster(createMaster(subject));
      // create a container for the account child data
      // all the multi-valued attributes and entry references the account have
      // will be assembled here
      final Map<String, List<Map<String, Object>>> childdata = new HashMap<String, List<Map<String, Object>>>();
      populateMultiValued(master, childdata);
      populateReferences(entryDN, master, childdata);
      processChangeLogEvent(master, childdata);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method groupd by functionality
  //////////////////////////////////////////////////////////////////////////////

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

    // ToDO: We should use the new interfaces to discover the reconciliation
    // capabilities
    // check out:
    //   ConfigUtils.getDefaultProfileNameFromObjName(reconcileObject())
    //   ProfileManager profilemgr = Platform.getBean(ProfileManager.class);
    //   Profile profile = profilemgr.lookupProfile(profileName);
    //   ArrayList<ReconAttribute> list = profile.getAllReconAttributes();

    // iterate over the reconciliation field and check for multi-valued
    // attributes
    String columnName = null;
    String fieldName  = null;
    // get the reconciliation field definition from the Resource Object handled
    // be this task
    Map filter = new HashMap(1);
    filter.put(ResourceObject.NAME, reconcileObject());
    try {
      tcResultSet resultSet = null;
      // retrieve the reconciliation field definition of the Resource Object
      // that this task will reconciling
      try {
        resultSet = this.objectFacade().findObjects(filter);
        if (resultSet.getRowCount() == 0)
          throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
        if (resultSet.getRowCount() > 1)
          throw new TaskException(TaskError.RESOURCE_AMBIGUOUS, reconcileObject());

        resultSet = this.objectFacade().getReconciliationFields(resultSet.getLongValue(ResourceObject.KEY));
        // if no fields defined abort the execution of this task by throwing an
        // appropritate exception
        if (resultSet.getRowCount() == 0)
          throw new TaskException(TaskError.RESOURCE_RECONFIELD, reconcileObject());
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, ResourceObject.KEY);
      }
      catch (tcObjectNotFoundException e) {
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
      }

      try {
        for (int i = 0; i < resultSet.getRowCount(); i++) {
          resultSet.goToRow(i);
          columnName  = ResourceObject.RECON_FIELD_TYPE;
          if (DirectoryReference.MULTI_VALUE_TYPE.equals(resultSet.getStringValue(columnName))) {
            columnName = ResourceObject.RECON_FIELD_NAME;
            fieldName  = resultSet.getStringValue(columnName);

            if (!(this.descriptor.entitlement().containsKey(fieldName) || this.descriptor.multivalued().containsKey(fieldName))) {
              String[] arguments = { reconcileObject(), fieldName };
              warning(TaskBundle.format(TaskError.RESOURCE_RECON_MULTIVALUE, arguments));
            }
          }
        }
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, columnName);
      }
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMaster
  /**
   ** Do all action which should take place to create account master data.
   **
   ** @param  subject            the {@link Map} providing the raw data to
   **                            reconcile.
   **
   ** @return                    the {@link Map} with requested data.
   **
   ** @throws TaskException      if data process fails.
   */
  protected Map<String, Object> createMaster(final Map<String, Object> subject)
    throws TaskException {

    // extracts the distinguished name from the subject
    String entryDN = (String)subject.remove(this.connector().distinguishedNameAttribute());
    // ensure inheritance
    final Map<String, Object> master    = super.createMaster(subject, true);
    final List<String[]>      hierarchy = DirectoryName.explode(entryDN);
    hierarchy.remove(0);
    entryDN = normalizePath(DirectoryName.compose(hierarchy));
    if (this.connector().entitlementPrefixRequired()) {
      final DirectoryResource resource = this.resource();
      entryDN = String.format(ENTITLEMENT_ENCODED_PREFIX, resource.instance(), entryDN);
    }
    // put always the attribute with the directory hierarchy in the mapping
    // regardless what the attribute mapping specifies, the organizationalDN
    // will not be reconciled if the mapping filter mechanism is executed after
    master.put(hierarchy(), entryDN);
    return master;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateMultiValued
  /**
   ** Do all action which should take place for pupulated the multi-valued
   ** attributes for a particular master.
   **
   ** @param  master             the {@link Map} to reconcile.
   ** @param  childdata          the {@link Map} receiving the child data.
   **
   ** @throws TaskException      if the operation fails for any reason
   */
  protected void populateMultiValued(final Map<String, Object> master, final Map<String, List<Map<String, Object>>> childdata)
    throws TaskException {

    final String method = "processMultiValued";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    // try to resolve all references that the specified entry can have
    try {
      // for each attribute object in the map, check whether any one that is
      // multi-valued and remove all references from the provided subject that is
      // multi-valued to the owning object
      if (this.multivalueMapping() != null) {
        Iterator i = this.multivalueMapping().keySet().iterator();
        while (i.hasNext()) {
          final String                    name    = (String)i.next();
          final List<Object>              value   = (List<Object>)master.remove(name);
          final int                       size    = (value == null) ? 0 : value.size();
          final List<Map<String, Object>> details = new ArrayList<Map<String, Object>>(size);
          for (int j = 0; j < size; j++) {
            // create a new map with exactly one key-value pair
            final Map<String, Object> record = new HashMap<String, Object>(1);
            record.put((String)this.multivalueMapping().get(name), value.get(j));
            details.add(record);
          }
          childdata.put(name, details);
        }
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
  // Method:   populateReferences
  /**
   ** Do all action which should take place for populating roles and/or groups
   ** (entry references) where the specified <code>entryDN</code> is assigned
   ** to.
   **
   ** @param  entryDN            the distinguished name used as the filter
   **                            expression to use for the search; must not be
   **                            <code>null</code>.
   ** @param  master             the {@link Map} containing all data to be
   **                            reconcile so far.
   ** @param  childdata          the {@link Map} receiving the child data.
   **
   ** @throws TaskException      if the operation fails for any reason
   */
  protected void populateReferences(final String entryDN, final Map<String, Object> master, final Map<String, List<Map<String, Object>>> childdata)
    throws TaskException {

    final String method = "populateReferences";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    // try to resolve all references that the specified entry can have
    try {
      if (!this.descriptor.entitlement().isEmpty()) {
        for (String objectName : this.descriptor.entitlement().keySet()) {
          final DirectoryReference descriptor = (DirectoryReference)this.descriptor.entitlement().get(objectName);
          childdata.put(objectName, populateReferences(entryDN, master, descriptor));
        }
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
  // Method:   populateReferences
  /**
   ** This method can be overwritten and entry DN or descriptor can be modified
   ** before a populateReference is executed.
   **
   ** @param  entryDN            the distinguished name used as the filter
   **                            expression to use for the search; must not be
   **                            <code>null</code>.
   ** @param  master             the {@link Map} containing all data to be
   **                            reconcile so far.
   **                            The argument is not used by default but may be
   **                            a subclass needs the entire context.
   ** @param  descriptor         the descriptor to handle a particular object
   **                            reference for the specified
   **                            <code>entryDN</code>.
   **
   ** @return                    a {@link List} where each entry is a {link Map}.
   **                            The contained {@link Map} has always a size of
   **                            <code>1</code>. The key is mapped name of the
   **                            server attribute name. The value associated
   **                            with the key is the value assigned to the
   **                            server attribute.
   **
   ** @throws TaskException      if the operation fails for any reason
   */
  protected List<Map<String, Object>> populateReferences(final String entryDN, final Map<String, Object> master, final DirectoryReference descriptor)
    throws TaskException {

    // populate the details of the entry by performing a search starting
    // at the context defined by entrySearchBase() in descriptor
    final List<Map<String, Object>> content = super.populateReferences(entryDN, descriptor);
    if (content != null && descriptor.transformationEnabled()) {
      for (int j = 0; j < content.size(); j++) {
        final Map<String, Object> record = descriptor.transformationMapping().transform(content.get(j));
        if (record.isEmpty())
          throw new TaskException(TaskError.TRANSFORMATION_MAPPING_EMPTY);
        content.set(j, record);
      }
    }
    return content;
  }
}