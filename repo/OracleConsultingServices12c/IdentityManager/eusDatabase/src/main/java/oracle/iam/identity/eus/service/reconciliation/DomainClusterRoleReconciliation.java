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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Enterprise User Security

    File        :   RoleLookupReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleLookupReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2011-03-01  TSebo       First release version
*/

package oracle.iam.identity.eus.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;

import javax.naming.InvalidNameException;

import javax.naming.directory.SearchControls;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.LdapContext;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.AbstractLookup;

import oracle.iam.identity.foundation.ldap.DirectorySearch;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryListSearch;
import oracle.iam.identity.foundation.ldap.DirectoryPageSearch;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.gds.resource.ReconciliationBundle;

import oracle.iam.identity.gds.service.reconciliation.LookupReconciliation;
import oracle.iam.identity.gds.service.reconciliation.ReconciliationMessage;

////////////////////////////////////////////////////////////////////////////////
// class DomainClusterRoleReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DomainClusterRoleReconciliation</code> acts as the service end
 ** point for the Oracle Identity Manager to reconcile Enterprise Role
 ** information from many Enterprise Domains in a Directory Service.
 ** <p>
 ** The Enterprise Domains the Enterprise Roles needs to be reconciled for are
 ** provided by a <code>Loookup Definition</code>. This
 ** <code>Loookup Definition</code> must specify the Enterprise Domains in the
 ** following manner:
 ** <pre>
 **   +------------------------------------------------+---------------------------+
 **   | Encoded Value                                  | Decoded Value             |
 **   +------------------------------------------------+---------------------------+
 **   | &lt;ITResource Key&gt;~&lt;ITResource Name&gt;~&lt;Domain DN&gt; | any value but never nuill |
 **   +------------------------------------------------+---------------------------+
 ** </pre>
 ** All Enterprise Domains that are part of such a
 ** <code>Loookup Definition</code> building up what we define as a Domain
 ** Cluster.
 ** <br>
 ** Each Enterprise Role for that will be seached are reconciled into the same
 ** <code>Loookup Definition</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DomainClusterRoleReconciliation extends LookupReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify which Lookup
   ** Definion is used for the Domain Cluster
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String   DOMAIN_CLUSTER = "Domain Cluster";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  protected static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,      TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,        TaskAttribute.MANDATORY)
    /** Name of the Lookup definition where are enlisted the domain cluster */
  , TaskAttribute.build(DOMAIN_CLUSTER,   TaskAttribute.MANDATORY)
    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Refresh | Update
     */
  , TaskAttribute.build(OPERATION,        TaskAttribute.MANDATORY)
    /** the task attribute to resolve the name of the Lookup Group */
  , TaskAttribute.build(LOOKUPGROUP,      TaskAttribute.MANDATORY)
    /** the task attribute to resolve the encoded value */
  , TaskAttribute.build(ENCODEDVALUE,     TaskAttribute.MANDATORY)
    /** the task attribute to resolve the decoded value */
  , TaskAttribute.build(DECODEDVALUE,     TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values One | Subtree
     */
  , TaskAttribute.build(SEARCHSCOPE,      TaskAttribute.MANDATORY)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCHFILTER,     TaskAttribute.MANDATORY)
    /** the task attribute that specifies the search base of the query */
  , TaskAttribute.build(SEARCHBASE,       SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,      SystemConstant.TRUE)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DomainClusterRoleReconciliation</code> scheduled
   ** task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DomainClusterRoleReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateChanges
  /**
   ** Do all action which should take place for reconciliation by fetching the
   ** data from the target system.
   **
   ** @param  returning          the attributes whose values have to be returned.
   **                            Set it to <code>null</code>, if all attribute
   **                            values have to be returned
   **
   ** @throws TaskException      if the operation fails
   */
  @Override
  protected void populateChanges(final Set<String> returning)
    throws TaskException {

    // check if a request to stop the execution is pending and return without
    // further actions if it evaluates to true
    if (isStopped())
      return;

    final String method = "populateChanges";
    trace(method, SystemMessage.METHOD_ENTRY);
    // create a task timer to gather performance metrics
    timerStart(method);
    info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));

    final Set<String> attribute = new LinkedHashSet<String>(returning);
    // createTimeStamp and modifyTimeStamp are operational attributes and are
    // not returned in searches and lookups per default.
    // In order to have them returned always we need to explicitly ask for them
    // using one of the search methods that take returning attributes as an
    // argument.
    attribute.add(this.connector.objectCreatedAttribute());
    attribute.add(this.connector.objectModifiedAttribute());

    // create the search request control accordingly to the requested search
    // scope
    final SearchControls controls = DirectoryConnector.searchScope(searchScope());
    // set the evaluated attributes to be returned by the search performd later
    controls.setReturningAttributes(attribute.toArray(new String[0]));

    final String         filter   = timestampFilter(stringValue(SEARCHFILTER), booleanValue(INCREMENTAL));
    String[]             order    = null;
    if (this.connector.paginationControl().equals(DirectoryConstant.PAGINATION_CONTROL_VIRTUALLIST)) {
      // if noe sorting attributes are defined if the VirtualListView Control is
      // configured for the searches fake one by using the operational timestamp
      // attributes
      if (StringUtility.isEmpty(this.searchOrder))
        this.searchOrder=String.format("%s%s%s", this.connector.objectCreatedAttribute(), this.connector.multiValueSeparator(), this.connector.objectModifiedAttribute());

      order = searchOrder().split(this.connector.multiValueSeparator());
    }
    else {
      if (!StringUtility.isEmpty(this.searchOrder))
        order = searchOrder().split(this.connector.multiValueSeparator());
    }

    if (StringUtility.isEmpty(this.searchOrder)) {
      String[] parameter = { searchBase(), searchScope(), filter};
      debug(method, ReconciliationBundle.format(ReconciliationMessage.SEARCH_CRITERIA, parameter));
    }
    else {
      String[] parameter = { searchBase(), searchScope(), filter, this.searchOrder};
      debug(method, ReconciliationBundle.format(ReconciliationMessage.SEARCH_CRITERIA_SORTED, parameter));
    }

    final AbstractLookup cluster = new AbstractLookup(this, stringValue(DOMAIN_CLUSTER));
    try {
      // connect to the source system
      // any exception raised by this invocation will stop the job
      final LdapContext context = this.connector.connect();
      for (String domain : cluster.keySet()) {
        if (this.connector().entitlementPrefixRequired())
          domain = DirectoryConnector.unescapePrefix(domain);
        // dispatch the retrieval of the result set accordingly to the configuration
        final DirectorySearch search = (this.connector.paginationControl().equals(DirectoryConstant.PAGINATION_CONTROL_VIRTUALLIST))
        ? new DirectoryListSearch(context, domain, filter, order, controls, bulkSize())
        : new DirectoryPageSearch(context, domain, filter, order, controls, bulkSize())
        ;
        // This while loop is used to read the LDAP entries in blocks.
        // This should decrease memory usage and help with server load.
        do {
          populateBatch(search.next());
        } while (search.hasMore());
      }
      // disconnect from the current allocated context
      this.connector.disconnect(context);
    }
    finally {
      // update the timestamp on the task only if it was requested
      updateLastReconciled();
      info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (overridden)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do reconciliation of Oracle Identity Manager Entitlements.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected final void processSubject(final Map<String, Object> subject)
    throws TaskException {

    final String method = "processSubject";
    // extracts the encoded value by checking if the distinguished name is
    // requested or an attribute
    String encoded = (String)subject.get(stringValue(ENCODEDVALUE));
    String decoded = (String)subject.get(stringValue(DECODEDVALUE));
    try {
      if (encoded != null) {
        LdapName dn = new LdapName(encoded);
        // Create decoded value based on the patter: role@domain
        decoded += "@" + dn.getRdn(dn.size() - 2).getValue();
      }
      else {
        error(method, "Encoded value is null");
      }
    }
    catch (InvalidNameException e) {
      error(method, "Cannot parse decoded value as LDAP DN: " + decoded);
    }
    subject.put(stringValue(DECODEDVALUE), decoded);

    super.processSubject(subject);
  }
}