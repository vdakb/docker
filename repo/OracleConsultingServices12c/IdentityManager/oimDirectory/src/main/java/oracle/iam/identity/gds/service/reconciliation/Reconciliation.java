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

    File        :   Reconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Reconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000126
                                         Batch Size is an optional argument but
                                         if its isn't defined the job loops
                                         infinite.
                                         Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.1.0.0      2014-05-07  DSteding    Made populateChanges, populateBatch and
                                         populateEntryReferences protected to
                                         allow overriding in subclasses.
    1.0.0.2      2013-18-01  DSteding    Fixed DE-000067
                                         Second run of reconciliation jobs reset
                                         timestamp to ZULU if no changes found
                                         in the source system.
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import javax.naming.NamingException;
import javax.naming.NameNotFoundException;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.SearchControls;

import javax.naming.ldap.LdapContext;

import javax.naming.NamingEnumeration;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AbstractAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.ldap.DirectoryName;
import oracle.iam.identity.foundation.ldap.DirectoryError;
import oracle.iam.identity.foundation.ldap.DirectorySearch;
import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryReference;
import oracle.iam.identity.foundation.ldap.DirectoryException;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryListSearch;
import oracle.iam.identity.foundation.ldap.DirectoryPageSearch;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.gds.resource.ReconciliationBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> acts as the service end point for the Oracle
 ** Identity Manager to reconcile accounts, organizations, roles and group
 ** information from a Directory Service.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
abstract class Reconciliation extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve objects from target system.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String SEARCHBASE          = "Search Base";

  /**
   ** Attribute tag which must be defined on this task to specify the scope of
   ** the search to retrieve objects from target system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String SEARCHSCOPE         = "Search Scope";

  /**
   ** Attribute tag which must be defined on this task to specify which filter
   ** criteria has to be applied to retrieve directory entries.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String SEARCHFILTER        = "Search Filter";

  /**
   ** Attribute tag which may be defined on this task to specify the sort of the
   ** result returned by the search.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String SEARCHORDER         = "Search Order";

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY     = "OCS.GDS.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the connection to the target system */
  protected DirectoryResource   resource;

  /** the abstraction layer to communicate with the target system */
  protected DirectoryConnector  connector;

  /** the size of a block processed in a thread */
  private int                   bulkSize            = Integer.MIN_VALUE;

  /** the base DN to search from */
  protected String              searchBase;

  /**
   ** search scope either
   ** <ul>
   **   <li>OneLevel
   **   <li>SubTree
   **   <li>Object
   ** </ul>
   */
  protected String              searchScope;

  /**
   ** the values to contruct the sort control to ensures that the results of a
   ** search operation be sorted by the LDAP server before being returned.
   */
  protected String              searchOrder;

  /**
   ** the {@link List} of reconciliation attributes names that needs to be
   ** handled as multi-valued
   */
  private List<String>          multivalueAttribute = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> task adpater that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Reconciliation() {
    // ensure inheritance
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> with the specified logging
   ** category.
   **
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  protected Reconciliation(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBase
  /**
   ** Returns the search base this task is performing on the LDAP server.
   **
   ** @return                    the search base this task is performing on the
   **                            LDAP server.
   */
  public String searchBase() {
    return this.searchBase;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchScope
  /**
   ** Returns the search scope this task is performing on the LDAP server.
   **
   ** @return                    the search scope this task is performing on the
   **                            LDAP server.
   */
  public String searchScope() {
    return this.searchScope;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchOrder (overridden)
  /**
   ** Returns the search order this task will request from the LDAP server.
   **
   ** @return                    the search order this task will request from
   **                            the LDAP server.
   */
  @Override
  public String searchOrder() {
    return this.searchOrder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the {@link DirectoryResource} this task is using to describe the
   ** connection parameter to the LDAP server.
   **
   ** @return                    the {@link DirectoryResource} this task is
   **                            using to describe the connection parameter to
   **                            the LDAP server.
   */
  protected final DirectoryResource resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connector
  /**
   ** Returns the {@link DirectoryConnector} this task is using to connect and
   ** perform operations on the LDAP server.
   **
   ** @return                    the {@link DirectoryConnector} this task is
   **                            using to connect and perform operations on the
   **                            LDAP server.
   */
  protected final DirectoryConnector connector() {
    return this.connector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multivalueMapping
  /**
   ** Returns or creates the {@link AttributeMapping} used by this scheduled
   ** task that maps the multi-valued attributes.
   **
   ** @return                    the {@link AttributeMapping} of this scheduled
   **                            task.
   */
  protected final AttributeMapping multivalueMapping() {
    return this.descriptor.multivalued();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValuedAttribute
  /**
   ** Checks if this attribute is in the multi-valued attribute
   ** {@link AttributeMapping} of an entry.
   **
   ** @param  attribute          the name of the attribute to check
   ** @return                    <code>true</code> if the passed
   **                            <code>attribute</code> is in the list of
   **                            multi-valued attributes; otherwise
   **                            <code>false</code>
   */
  protected final boolean multiValuedAttribute(final String attribute) {
    return (this.descriptor != null && this.descriptor.multivalued().containsKey(attribute));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulkSize
  /**
   ** Returns the size of on bulk the reconciliation process will send to the
   ** service configured for further processing.
   **
   ** @return                    the size of on bulk the reconciliation process
   **                            will send to the service configured for further
   **                            processing.
   */
  protected int bulkSize() {
    this.bulkSize = batchSize();
    // Fixed Defect DE-000126
    // Batch Size is an optional argument but if its isn't defined the job
    // loops infinite hence we need to fallback to a default value
    if (this.bulkSize == Integer.MIN_VALUE)
      this.bulkSize = BATCH_SIZE_DEFAULT;
    return this.bulkSize;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

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
    try {
      populateChanges(returningAttributes());
    }
    // in any case of an unhandled exception
    catch (TaskException e) {
      final String[] parameter = { reconcileObject(), getName(), resourceName(), e.getLocalizedMessage() };
      warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, parameter));
      throw e;
    }
    info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    this.searchBase  = stringValue(SEARCHBASE);
    this.searchOrder = stringValue(SEARCHORDER);
    this.searchScope = stringValue(SEARCHSCOPE);
    // validate if the specified value provided for the search scope is in range
    if (!(DirectoryConstant.SCOPE_ONELEVEL.equalsIgnoreCase(this.searchScope) || DirectoryConstant.SCOPE_SUBTREE.equalsIgnoreCase(this.searchScope)|| DirectoryConstant.SCOPE_OBJECT.equalsIgnoreCase(this.searchScope))) {
      final String[] parameter = {SEARCHSCOPE, DirectoryConstant.SCOPE_ONELEVEL + " | " + DirectoryConstant.SCOPE_SUBTREE + " | " + DirectoryConstant.SCOPE_OBJECT };
      throw new TaskException(TaskError.TASK_ATTRIBUTE_NOT_INRANGE, parameter);
    }
    initializeConnector();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeConnector
  /**
   ** Initalize the connector capabilities.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected void initializeConnector()
    throws TaskException {

    final String method = "initializeConnector";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.resource  = new DirectoryResource(this, this.resourceName());
      this.connector = new DirectoryConnector(this, this.resource);
      // produce the logging output only if the logging level is enabled for
      if (this.logger.debugLevel())
        debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.connector.toString()));
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeMultivalueAttribute
  /**
   ** Based on the strings that contains attributes from the Directory Service
   ** that are "multi-valued" the containers are created.
   ** <p>
   ** The attribute specificaition is delegated to the subclass that has to
   ** provide the name of the server feature attribute
   ** <p>
   ** We use a string tokenizer and parse it adding the tokens into multi-valued
   ** {@link List}.
   **
   ** @param  featureName        the key of the feature mapping for the desired
   **                            {@link List} of <code>String</code>.
   **
   ** @return                    the <code>String</code> for the given
   **                            <code>featureName</code>.
   */
  protected List<String> initializeMultivalueAttribute(final String featureName) {
    // the list is created here because testing whether it's null is a way of
    // "knowing" that there are no multi-valued attributes
    if (this.multivalueAttribute == null) {
      this.multivalueAttribute = this.connector.feature().stringList(featureName);
      // if still the list is empty create a dummy
      this.multivalueAttribute = Collections.EMPTY_LIST;
    }
    return this.multivalueAttribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   denormalizePath
  /**
   ** Forms the basis of building the hierarchical tree to the LDAP object.
   ** Used by connect to build the correct distinguished name.
   **
   ** @param  entryRDN           Contains the elements in the tree, deepest one
   **                            first. The String must be of format
   **                            "Class Type=Object CN,Class Type=Object CN"
   **                            where:
   **                            <ul>
   **                              <li>Class Type is the objects class type ("CN", "OU", ...)
   **                              <li>Object CN is the LDAP objects common name ("Dumbo", "finance group", ... )
   **                            </ul>
   **                            Basically whatever is assigned to the
   **                            mandatory property "cn" or "ou". e.g.
   **                            <code>CN=Dumbo,OU=Leaders,OU=Elephants</code>
   **
   ** @return                    String of the canonical path (including the
   **                            root context), e.g.
   **                            OU=Users,OU=abc,OU=Companies,DC=thordev,DC=com
   */
  protected String denormalizePath(final String entryRDN) {
    return this.connector.denormalizePath(entryRDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizePath
  /**
   ** Remove the root context name from the distinguished name.
   **
   ** @param  distinguishedName  the path of the object (relative or absolute to
   **                            the root context hierarchy)
   **
   ** @return                    the relative distinguished name without the
   **                            root context
   */
  protected String normalizePath(final String distinguishedName) {
    return this.connector.normalizePath(distinguishedName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes
  /**
   ** Returns the array of attribute names that will be passed to a Directory
   ** Server search operation to specify which attributes the Directory Server
   ** should return.
   **
   ** @return                   the array of attribute names that will be passed
   **                           to a Directory Server search operation to
   **                           specify which attributes the Directory Server
   **                           should return.
   */
  protected abstract Set<String> returningAttributes();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected abstract void processSubject(Map<String, Object> subject)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFilter
  /**
   ** Builds a filter that can by applied on a search in the change log.
   **
   ** @param  filter             the filter criteria that should be extended by
   **                            the criteria of the timestamp.
   ** @param  changesOnly        <code>true</code> if only changes based on the
   **                            modify timestamp should be returned by the
   **                            search.
   **
   ** @return                    a {@link String} with the search filter
   **                            criteria.
   */
  protected String timestampFilter(final String filter, final boolean changesOnly) {
    final String objectCreatedAttribute = this.connector.objectCreatedAttribute();
    final String objectModifyAttribute  = this.connector.objectModifiedAttribute();

    StringBuilder searchFilter = new StringBuilder();
    if (changesOnly) {
      // adjust the timestamp of the last run by creating a new timestamp where
      // one millisecond is added on to skip the entries that will be fetched by
      // the search filter but match exactly the timestamp of the last run
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(lastReconciled());
      calendar.add(Calendar.MILLISECOND, 1);
      // build the timestamp to use as the filter criterion
      final String  timeValue  = this.timestampFormatter().format(calendar.getTime());
      // with the timestamp builded above a filter condition is formed to fetch
      // only values that are changed after
      final StringBuilder timeFilter = new StringBuilder();
      timeFilter.append("(|");
      // build the filter to catch entries where create and modify are present
      // and are younger in their life time as the timeValue
      // this will fetch entries that are created and/or modified
      // Since OID 11.1.1.6 it looks like that a timestamp based query has also
      // to check for the presences of each timestamp attributes otherwise
      // LDAP Error 53 - Operation not supported will be thrown hence we compose
      // for each attribute a combined filter with the presence check and the
      // condition of the timestamp
      timeFilter.append("(&");
      timeFilter.append(DirectoryConnector.composeFilter(objectCreatedAttribute, "=", "*"));
      timeFilter.append(DirectoryConnector.composeFilter(objectCreatedAttribute, ">=", timeValue));
      timeFilter.append(")(&");
      timeFilter.append(DirectoryConnector.composeFilter(objectModifyAttribute,  "=", "*"));
      timeFilter.append(DirectoryConnector.composeFilter(objectModifyAttribute,  ">=", timeValue));
      timeFilter.append("))");
      if (StringUtility.isEmpty(filter))
        searchFilter.append(timeFilter);
      else
        searchFilter.append("(&").append(filter).append(timeFilter).append(")");
    }
    else
      searchFilter.append(filter);

    return searchFilter.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateReferences
  /**
   ** Do all action which should take place for reconciliation by fetching the
   ** data from the target system.
   **
   ** @param  entryDN            the relative distinguished name used as the
   **                            filter expression to use for the search;
   **                            may not be <code>null</code>.
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
  protected final List<Map<String, Object>> populateReferences(final String entryDN, final DirectoryReference descriptor)
    throws TaskException {

    List<Map<String, Object>> details = null;
    if (!isStopped()) {
      // check if the object references should be resolve by an attribute at the
      // passed distinguished name
      if (descriptor.objectClassName().equals("this"))
        details = populateSelfReferences(entryDN, descriptor);
      else
        details = populateEntryReferences(entryDN, descriptor);
    }
    return details;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateSelfReferences
  /**
   ** Do all action which should take place for reference reconciliation by
   ** fetching the data from the target system.
   **
   ** @param  entryDN            the distinguished name used as the filter
   **                            expression to use for the search; may not be
   **                            <code>null</code>.
   ** @param  descriptor         the {@link DirectoryReference} that describes
   **                            the linkage.
   **
   ** @return                    an array where each entry is a {link Map}.
   **                            The contained {@link Map} has always a size of
   **                            <code>1</code>. The key is mapped name of the
   **                            server attribute name. The value associated
   **                            with the key is the value assigned to the
   **                            server attribute.
   **
   ** @throws DirectoryException if the operation fails for any reason
   */
  protected final List<Map<String, Object>> populateSelfReferences(final String entryDN, final DirectoryReference descriptor)
    throws DirectoryException {

    final String method = "populateSelfReferences";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String              dnName   = this.connector.distinguishedNameAttribute();
    // extracts the relative distinguished names from the passed
    // distinguished name
    final String              superior = normalizePath(DirectoryConnector.superiorDN(entryDN));
    final AbstractAttribute[] encoded  = descriptor.attributeMapping().attributes();
    // create a new array with is big enough to hold all requested
    // attributes
    final String[][]          mapping  = new String[encoded.length][2];
    for (int i = 0; i < encoded.length; i++) {
      mapping[i][0] = encoded[i].id();
      mapping[i][1] = (String)descriptor.attributeMapping().get(mapping[i][0]);
    }
    // create an array that holds the linkage attribute
    String[]                  returning = { descriptor.entryLinkAttribute() };
    List<Map<String, Object>> details   = null;

    // create a new unpooled connection to the target system to fetch the
    // requested details
    LdapContext               context   = this.connector.connect(this.connector.environment(connector.contextURL(superior), false));
    try {
      Attributes response = context.getAttributes(DirectoryConnector.entryRDN(superior), returning);
      // check if we got a result from the server
      if (response == null)
        throw new DirectoryException(DirectoryError.OBJECT_NOT_EXISTS);

      // get the attribute the we had requested from the result set
      BasicAttribute attribute = (BasicAttribute)response.get(descriptor.entryLinkAttribute());
      // nothing to do if the set is emmpty
      if (attribute == null)
        return details;

      details = new ArrayList<Map<String, Object>>(attribute.size());
      for (int i = 0; i < attribute.size(); i++) {
        Object object = attribute.get(i);
        String value  = (object instanceof byte[]) ? DirectoryConnector.hexString((byte[])object) : (String)object;
        if (this.connector.isDistinguishedNameRelative())
          value = this.connector.normalizePath(value);
        Map<String, Object> data = new HashMap<String, Object>(mapping.length);
        for (int j = 0; j < mapping.length; j++) {
          // if the mapping specifies the distinguished name as an attribut put
          // the entire value in the data mapping
          if (descriptor.attributeMapping().containsKey(dnName))
            data.put((String)descriptor.attributeMapping().get(dnName), value);
          // in all other cases explode the value to a component list and put
          // only the specified parts of the attribute value in the mapping
          else {
            List<String[]> entry = DirectoryName.explode(value);
            for (int k = 0; k < entry.size(); k++) {
              final String name = ((String[])entry.get(k))[0];
              if (!name.equalsIgnoreCase(dnName))
                if (mapping[j][0].equals(name)) {
                  data.put(mapping[j][1], ((String[])entry.get(k))[1]);
                  // ToDO: I have absolute no idea in the moment how to handle
                  //       an entry like
                  //          cn=UserAdmins,cn=Groups,cn=MetaRole,cn=TargetSystems,...
                  //       the loop can only pickup exactly the first part
                  //          cn=UserAdmins
                  //       all others parts will be ignored
                  break;
                }
                else
                  data.put(mapping[j][1], value);
            }
          }
        }
        details.add(data);
      }
    }
    catch (NameNotFoundException e) {
      throw new DirectoryException(e);
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
    finally {
      this.connector.disconnect(context);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return details;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateEntryReferences
  /**
   ** Do all action which should take place for reference reconciliation by
   ** fetching the data from the target system.
   **
   ** @param  entryDN            the relative distinguished name used as the
   **                            filter expression to use for the search;
   **                            may not be null.
   ** @param  descriptor         the {@link DirectoryReference} that describes
   **                            the linkage.
   **
   ** @return                    an array where each entry is a {link Map}.
   **                            The contained {@link Map} has always a size of
   **                            <code>1</code>. The key is mapped name of the
   **                            server attribute name. The value associated
   **                            with the key is the value assigned to the
   **                            server attribute.
   **
   ** @throws TaskException      if the operation fails for any reason
   */
  protected final List<Map<String, Object>> populateEntryReferences(final String entryDN, final DirectoryReference descriptor)
    throws TaskException {

    final String method = "populateEntryReferences";
    trace(method, SystemMessage.METHOD_ENTRY);

    final AbstractAttribute[] encoded = descriptor.attributeMapping().attributes();
    // create a new array with is big enough to hold all requested
    // attributes
    String[] returning = new String[descriptor.attributeMapping().containsKey(this.connector.distinguishedNameAttribute()) ? encoded.length - 1 : encoded.length];
    for (int i = 0; i < encoded.length; i++) {
      final String name = encoded[i].id();
      if (!name.equalsIgnoreCase(this.connector.distinguishedNameAttribute()))
        returning[i] = encoded[i].id();
    }

    List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
    // build the LDAP search filter
    Object[] parameter = { this.connector.objectClassName(), descriptor.objectClassName(), descriptor.entryLinkAttribute(), denormalizePath(entryDN) };
    final String searchFilter = String.format("(&(%1$s=%2$s)(%3$s=%4$s))", parameter);

    // setup the search controls
    SearchControls controls = DirectoryConnector.searchScope(SearchControls.SUBTREE_SCOPE);
    controls.setReturningAttributes(returning);

    // create a new pooled connection to the target system to fetch the
    // requested details
    LdapContext context = this.connector.connect(this.connector.environment(this.connector.contextURL(descriptor.entrySearchBase()), true));
    try {
      // search for entries that meets the evaluated search filter
      NamingEnumeration<SearchResult> results = context.search(SystemConstant.EMPTY, searchFilter, controls);
      if (results != null) {
        // loop through the results and transfer the SearchResults in a List
        while (results.hasMoreElements()) {
          Map<String, Object> data  = new HashMap<String, Object>(encoded.length);
          final SearchResult  entry = results.nextElement();
          // this attributes will have all the values from the target system
          // just keeping a check on the attributes value if null then add
          // something
          final Attributes attributes = entry.getAttributes();
          // extracts the data from the search results
          for (int k = 0; k < encoded.length; k++) {
            final String id = encoded[k].id();

            String value = null;
            if (id.equalsIgnoreCase(this.connector.distinguishedNameAttribute()))
              value = normalizePath(entry.getNameInNamespace());
            else
              value = (String)attributes.get(id).get();

            if (this.connector.entitlementPrefixRequired())
              value = String.format(ENTITLEMENT_ENCODED_PREFIX, this.resource.instance(), value);

            data.put((String)descriptor.attributeMapping().get(id), value);
          }
          if (descriptor.transformationEnabled()) {
            data = descriptor.transformationMapping().transform(data);
            if (data.isEmpty()) {
              // TODO: what happens here
            }
          }
          details.add(data);
        }
      }
    }
    catch (NameNotFoundException e) {
      throw new TaskException(e);
    }
    catch (NamingException e) {
      throw new TaskException(e);
    }
    finally {
      this.connector.closeContext(context);
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return details;
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
      // if no sorting attributes are defined if the VirtualListView Control is
      // configured for the searches fake one by using the operational timestamp
      // attributes
      if (StringUtility.isEmpty(this.searchOrder))
        this.searchOrder=String.format("%s%s%s", this.connector.objectCreatedAttribute(), this.connector.multiValueSeparator(), this.connector.objectModifiedAttribute());

      order = this.searchOrder.split(this.connector.multiValueSeparator());
    }
    else {
      if (!StringUtility.isEmpty(this.searchOrder))
        order = this.searchOrder.split(this.connector.multiValueSeparator());
    }

    if (StringUtility.isEmpty(this.searchOrder)) {
      String[] parameter = { searchBase(), searchScope(), filter};
      debug(method, ReconciliationBundle.format(ReconciliationMessage.SEARCH_CRITERIA, parameter));
    }
    else {
      String[] parameter = { searchBase(), searchScope(), filter, this.searchOrder};
      debug(method, ReconciliationBundle.format(ReconciliationMessage.SEARCH_CRITERIA_SORTED, parameter));
    }

    // connect to the source system
    // any exception raised by this invocation will stop the job
    final LdapContext context = this.connector.connect();
    try {
      // dispatch the retrieval of the result set accordingly to the configuration
      final DirectorySearch search = (this.connector.paginationControl().equals(DirectoryConstant.PAGINATION_CONTROL_VIRTUALLIST))
      ? new DirectoryListSearch(context, searchBase(), filter, order, controls, bulkSize())
      : new DirectoryPageSearch(context, searchBase(), filter, order, controls, bulkSize())
      ;
      // This while loop is used to read the LDAP entries in blocks.
      // This should decrease memory usage and help with server load.
      do {
        populateBatch(search.next());
      } while (search.hasMore());
      // update the timestamp on the task only if it was requested
      updateLastReconciled();
      info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
    }
    finally {
      // disconnect from the context
      this.connector.disconnect();
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateBatch
  /**
   ** Extracts the entries from the given result set <code>result</code>.
   **
   ** @param  results            the {@link NamingEnumeration} that contains the
   **                            entries fetched from the LDAP server.
   **                            Each entry is an instance of
   **                            {@link SearchResult}.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void populateBatch(final NamingEnumeration<SearchResult> results)
    throws TaskException {

      // prepare the container for the iteration
    final List<Map<String, Object>> result  = new ArrayList<Map<String, Object>>(bulkSize());

    final String objectCreatedAttribute     = this.connector.objectCreatedAttribute();
    final String objectModifiedAttribute    = this.connector.objectModifiedAttribute();
    final String distinguishedNameAttribute = this.connector.distinguishedNameAttribute();

    try {
      // loop through the results and
      while (!isStopped() && (results != null) && results.hasMoreElements()) {
        final SearchResult entry = results.nextElement();
        // extracts the distinguished name from the entry at put it in the
        // subject container
        Map<String, Object> subject = new HashMap<String, Object>();
        // we using method getNameInNamespace to get the fullqualified DN
        // and follow the configuration to put the correct name in the mapping
        subject.put(distinguishedNameAttribute, normalizePath(entry.getNameInNamespace()));
        // this Attributes instance will have all the values from the
        // source system that requested by setting returning attributes
        // in SearchControls instance passed to the server or all if
        // null was passed by the caller to this method
        final Attributes attributes = entry.getAttributes();
        // iterate over all attribute ID's
        final NamingEnumeration<String> id = attributes.getIDs();
        while (id.hasMoreElements()) {
          final String name = id.nextElement();
          if (this.multiValuedAttribute(name)) {
            final NamingEnumeration<?> object = attributes.get(name).getAll();
            List<Object> value = new ArrayList<Object>();
            while (object.hasMoreElements())
              value.add(object.nextElement());
            subject.put(name, value);
          }
          else {
            // just keeping a check on the attributes value if null then add
            // something
            final Object object = attributes.get(name).get();
            String value = (object instanceof byte[]) ? DirectoryConnector.hexString((byte[])object) : (String)object;

            if (name.equalsIgnoreCase(objectModifiedAttribute) || name.equalsIgnoreCase(objectCreatedAttribute)) {
              final Date timeStamp = this.connector.transformToDate(value);
              if (lastReconciled().before(timeStamp))
                // setting it at this time that we have next time this scheduled
                // task will run the changes made during the execution of this
                // task target current time is different from this
                // system time
                lastReconciled(timeStamp);
            }
            else
              subject.put(name, value);
          }
        }
        result.add(subject);
      }

      if (result.size() == 0)
        info(TaskBundle.string(TaskMessage.NOTHING_TO_CHANGE));
      else {
        info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(result.size())));
        // if we should really do reconcile ?
        if (gatherOnly())
          info(TaskBundle.string(TaskMessage.RECONCILE_SKIP));
        else {
          if (!isStopped())
            processBatch(result);
          info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
        }
      }
    }
    catch (NamingException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processBatch
  /**
   ** Reconciles the changed entries.
   **
   ** @param  batch              the {@link List} with the entries fetched from
   **                            the LDAP server.
   **                            Each entry is an instance of
   **                            {@link SearchResult}.
   **
   ** @throws TaskException      if the operation fails
   */
  private void processBatch(final List<Map<String, Object>> batch)
    throws TaskException {

    final String method = "processBatch";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, this.reconcileObject()));
      for (int i = 0; i < batch.size(); i++) {
        processSubject(batch.get(i));
        // check if there is a stop signal pending
        if (isStopped())
          break;
      }
    }
    finally {
      info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, this.reconcileObject()));
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}