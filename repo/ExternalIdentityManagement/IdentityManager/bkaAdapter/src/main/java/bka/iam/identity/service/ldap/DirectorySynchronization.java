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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   SynchronizeDirectory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    DirectoryHousekeeping.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-30-11  SBernet     First release version
*/
package bka.iam.identity.service.ldap;

import bka.iam.identity.ProcessError;
import bka.iam.identity.ProcessException;

import com.sun.jndi.ldap.ctl.TreeDeleteControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryException;
import oracle.iam.identity.foundation.ldap.DirectoryFeature;
import oracle.iam.identity.foundation.ldap.DirectoryListSearch;
import oracle.iam.identity.foundation.ldap.DirectoryPageSearch;
import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.ldap.DirectorySearch;
import oracle.iam.identity.foundation.resource.TaskBundle;
////////////////////////////////////////////////////////////////////////////////
// class DirectoryHousekeeping
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryHousekeeping</code> job synchronizes three subentries in
 ** two directories. These subentries are:
 ** - Role
 ** - Organization
 ** - Entitlement
 ** If differences have been detected with the provided criteria on these
 ** entries, then they are updated on the target system.
 ** This job also detects deleted entries and updates subentries called
 ** "Intermediate entry" which may contain some of the previously the deleted
 ** entrie.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectorySynchronization extends AbstractSchedulerTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /**
   ** Attribute to advise which named IT Resource should be use for retriving
   ** entries from the source directory.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String IT_RESOURCE_SOURCE            = "IT Resource Source";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve Organization entries from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ORGANIZATION_SOURCE = "Search Base Organization Source";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve role entries from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ROLE_SOURCE         = "Search Base Role Source";
  
  /**
   ** Attribute tag which may be defined on this task to specify where the
   ** DN of the intermediate OU from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_INTERMEDIATE_SOURCE = "Search Base Intermediate Source";
  
  /**
   ** Attribute to advise which named IT Resource should be use to copy source
   ** directory entries.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String IT_RESOURCE_TARGET             = "IT Resource Target";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve role entries and update on the target system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ORGANIZATION_TARGET = "Search Base Organization Target";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve role entries update on the target system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ROLE_TARGET         = "Search Base Role Target";
  
  /**
   ** Attribute tag which may be defined on this task to specify where the
   ** DN of the intermediate OU.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_INTERMEDIATE_TARGET = "Search Base Intermediate Target";
  
  /**
   ** Attribute tag which may be defined on this task to specify which kind of
   ** object classes should be synchronize. Multiple values must be separated
   ** by the special character '|'.
   ** <br>
   ** This attribute is optional.
   */
  private static final String OBJECT_CLASSES                 = "Object Classes";
  
  /**
   ** Attribute tag which must be defined on this task to specify which filter
   ** criteria has to be applied to retrieve directory entries on the source and
   ** the target.
   ** <br>
   ** This attribute is optional.
   */
  private static final String SEARCHFILTER                   = "Search Filter";
  
  /**
   ** Attribute tag which may be defined on this task to specify which attribute
   ** of the entries should be copied. Multiple values must be separated
   ** by the special character '|'.
   ** <br>
   ** This attribute is optional.
   */
  private static final String ATTRIBUTES                     = "Attributes";
  
  /**
   ** Attribute tag which can be defined on this task to specify the size of a
   ** block read from the working file.
   ** <br>
   ** This attribute is optional and defaults to {@link #BATCH_SIZE_DEFAULT}.
   */
  private static final String BATCH_SIZE                   = "Batch Size";

  /** Default value for {@link #BATCH_SIZE} */
  private static final int    BATCH_SIZE_DEFAULT           = 1000;
  
  /** The category of the logging facility to use */
  private static final String LOGGER_CATEGORY              = "BKA.DIRECTORY.SYNC";
  
  /** The separator used for multivalued attributes */
  final private static String SEPARATOR = "\\|";
  
  /**
   ** The vector with attributes which are valid to define on the task
   ** definition
   */
  protected static final TaskAttribute[] attribute = {
    /** The task attribute IT Resource for the source directory */
    TaskAttribute.build(IT_RESOURCE_SOURCE,             TaskAttribute.MANDATORY)
    /** 
     ** The task attribute that specifies the DN of the organizations in the
     ** source
     */
  , TaskAttribute.build(SEARCHBASE_ORGANIZATION_SOURCE, TaskAttribute.MANDATORY)
    /** The task attribute that specifies the DN of the roles in the source */
  , TaskAttribute.build(SEARCHBASE_ROLE_SOURCE,         TaskAttribute.MANDATORY)
    /** The task attribute IT Resource for the target directory */
  , TaskAttribute.build(IT_RESOURCE_TARGET,             TaskAttribute.MANDATORY)
    /** 
     ** The task attribute that specifies the DN of the organizations in the
     ** target
     */
  , TaskAttribute.build(SEARCHBASE_ORGANIZATION_TARGET, TaskAttribute.MANDATORY)
    /** The task attribute that specifies the DN of the roles in the target */
  , TaskAttribute.build(SEARCHBASE_ROLE_TARGET,         TaskAttribute.MANDATORY)
    /**
     ** The task attribute that specifies the DN of the intermediate OU in the
     ** target
     */
  , TaskAttribute.build(SEARCHBASE_INTERMEDIATE_TARGET, SystemConstant.EMPTY)
    /** 
     ** The task attribute specifying which object class in the sub entries
     ** should be synchronized
     **/
  , TaskAttribute.build(OBJECT_CLASSES,               SystemConstant.EMPTY)
    /** The task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCHFILTER,                 SystemConstant.EMPTY)
    /** The task attribute that specifies which attribute to synchronize */
  , TaskAttribute.build(ATTRIBUTES,                   SystemConstant.EMPTY)
  , TaskAttribute.build(BATCH_SIZE,                   String.valueOf(BATCH_SIZE_DEFAULT))
  };
  
  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // enum entryTag
  // ~~~~ ~~~~~~~~
  /**
   ** This enum store tags for updating entries in the target system
   */
  enum EntryTag{
     /**
     ** Attribute value which may be defined on an entry that must be create on
     ** the LDAP target.
     ** <p>
     ** This operation will add the entry and its attributes at the same
     ** location as defined in the source
     */
    ADD,
    /**
     ** Attribute value which may be defined on a entry that must be updated on
     ** the LDAP target. The reason of this tags could be a change of an
     ** attribute.
     ** <p>
     ** This operation will modify the entry at the same location as defined
     ** in the source
     */
    MODIFY,
    /**
     ** Attribute value which may be defined on a entry that must be deleted on
     ** the LDAP target.
     ** <p>
     ** This operation will delete the same entry defined in the source
     */
    DELETE;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccessPolicyHouseKeeping</code> scheduler
   ** instance that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectorySynchronization() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceNameSource
  /**
   ** Returns the name of the IT ressource used as authoritative data that will
   ** be used to synchronize entries in the directory target.
   **
   ** @return                    the name of the IT ressource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String resourceNameSource() {
    return stringValue(IT_RESOURCE_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseOrganizationSource
  /**
   ** Returns the Organization search base this task is performing on the source
   ** LDAP server.
   **
   ** @return                    the organization search base this task is
   **                            performing on the source LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseOrganizationSource() {
    return stringValue(SEARCHBASE_ORGANIZATION_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseRoleSource
  /**
   ** Returns the role search base this task is performing on the source
   ** LDAP server.
   **
   ** @return                    the role search base this task is
   **                            performing on the source LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseRoleSource() {
    return stringValue(SEARCHBASE_ROLE_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseIntermediateTarget
  /**
   ** Returns the intermediate search base this task is performing on the source
   ** LDAP server.
   **
   ** @return                    the intermediate search base this task is
   **                            performing on the source LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseIntermediateSource() {
    return stringValue(SEARCHBASE_INTERMEDIATE_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceNameTarget
  /**
   ** Returns the name of the IT resource used as target data that will
   ** be used to synchronize entries with the authoritative directory.
   **
   ** @return                    the name of the IT ressource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String resourceNameTarget() {
    return stringValue(IT_RESOURCE_TARGET);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseOrganizationTarget
  /**
   ** Returns the organization search base this task is performing on the target
   ** LDAP server.
   **
   ** @return                    the organization search base this task is
   **                            performing on the target LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseOrganizationTarget() {
    return stringValue(SEARCHBASE_ORGANIZATION_TARGET);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseRoleTarget
  /**
   ** Returns the role search base this task is performing on the target
   ** LDAP server.
   **
   ** @return                    the role search base this task is
   **                            performing on the target LDAP server.
   **                            <br>
  **                            Possible object is {@link String}.
   */
  public String searchBaseRoleTarget() {
    return stringValue(SEARCHBASE_ROLE_TARGET);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseIntermediateTarget
  /**
   ** Returns the intermediate search base this task is performing on the target
   ** LDAP server.
   **
   ** @return                    the intermediate search base this task is
   **                            performing on the target LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseIntermediateTarget() {
    return stringValue(SEARCHBASE_INTERMEDIATE_TARGET);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClasses
  /**
   ** Returns the list of object classes where the task should pick up in the
   ** subentries.
   **
   ** @return                    the list of object classes where the task
   **                            should pick up in the subentries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String objectClasses() {
    return stringValue(OBJECT_CLASSES);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchFilter
  /**
   ** Returns the additional filter criteria that has to be applied to
   ** retrieve directory entries.
   **
   ** @return                    the additional filter criteria that has to be
   **                            applied to retrieve directory entries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchFilter() {
    return stringValue(SEARCHFILTER);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   syncAttributes
  /**
   ** Returns the additional list of the attributes that has to be synchronized
   ** between the source and the target directory.
   **
   ** @return                    the additional list of the attributes that has
   **                            to be synchronized between the source and the
   **                            target directory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String syncAttributes() {
    return stringValue(ATTRIBUTES);
  }  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   batchSize
  /**
   ** Returns the batch size of for the  synchronization source and target.
   **
   ** @return                    the batch size for the reconciliation source.
   */
  public int batchSize() {
    final int batchSize = integerValue(BATCH_SIZE);
    // Fixed Defect DE-000126
    // Batch Size is an optional argument but if its isn't defined the job
    // loops infinite hence we need to fallback to a default value
    return (batchSize == Integer.MIN_VALUE) ? BATCH_SIZE_DEFAULT : batchSize;
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
   **                            <br>
   **                            Possible object is array of
   **                            {@link TaskAttribute}.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution
  /**
   ** The entry point of the scheduled task to perform.
   ** Retrieve entries from the source then compare entries on the target.
   ** The following change are made after the comparison:
   ** <ul>
   **   <li>Add:    If a new entry has been detected, this entry is copied on the
   **               target</li>
   **   <li>Modify: If an attribute has been modify, then this change is copied
   **               on the target</li>
   **   <li>Delete: If an attribut has been deleted, this entry is also deleted
   **               on the target</li>
   ** </ul>
   ** 
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {
    
    final String method  = "onExecution";
    debug(method, SystemMessage.METHOD_ENTRY);
    
    // Check if passed arguments exist like IT Resource exists etc.
    try {
      final DirectoryFeature    feature         = DirectoryFeature.build(this);
      final DirectoryResource   sourceResource  = new DirectoryResource(this, resourceNameSource());
      final DirectoryResource   targetResource  = new DirectoryResource(this, resourceNameTarget());
      final DirectoryConnector  sourceDirectory = new DirectoryConnector(this, sourceResource, feature);
      final DirectoryConnector  targetDirectory = new DirectoryConnector(this, targetResource, feature);
      
      final Set<String> objectClasses    = StringUtility.isEmpty(objectClasses())    ? null : new HashSet<String>(Arrays.asList(objectClasses().split(SEPARATOR)));
      final Set<String> returnAttributes = StringUtility.isEmpty(syncAttributes())   ? null : new HashSet<String>(Arrays.asList(syncAttributes().split(SEPARATOR)));
      
      // Retrieves entries from source and target directory.
      // Then compare source and the target list with proceedCompare
      // Finally update the entry on the target.
      
      // Role synchronization
      final  Map<String, Attributes> roleSrcEntries = searchEntries(sourceDirectory, searchBaseRoleSource(), objectClasses, searchFilter(), returnAttributes);
      final  Map<String, Attributes> roleDstEntries = searchEntries(targetDirectory, searchBaseRoleTarget(), objectClasses, searchFilter(), returnAttributes);
      
      updateTarget(sourceDirectory, targetDirectory, proceedCompare(roleSrcEntries, roleDstEntries), searchBaseRoleTarget());
      
      // Organization synchronization
      final  Map<String, Attributes> orgSrcEntries = searchEntries(sourceDirectory, searchBaseOrganizationSource(), objectClasses, searchFilter(), returnAttributes);
      final  Map<String, Attributes> orgDstEntries = searchEntries(targetDirectory, searchBaseOrganizationTarget(), objectClasses, searchFilter(), returnAttributes);
      
      updateTarget(sourceDirectory, targetDirectory, proceedCompare(orgSrcEntries, orgDstEntries), searchBaseOrganizationTarget());
      
      // Intermediate synchronization
      final  Map<String, Attributes> intSrcEntries = searchEntries(sourceDirectory, searchBaseIntermediateSource(), objectClasses, searchFilter(), returnAttributes);
      final  Map<String, Attributes> intDstEntries = searchEntries(targetDirectory, searchBaseIntermediateTarget(), objectClasses, searchFilter(), returnAttributes);
      
      // Before synchronization, check data consistency on Intermediate OU with
      // role & organisation entries
      checkConsistency(intSrcEntries.keySet(), orgSrcEntries.keySet());
      checkConsistency(intSrcEntries.keySet(), roleSrcEntries.keySet());
      
      updateTarget(sourceDirectory, targetDirectory, proceedCompare(intSrcEntries, intDstEntries), searchBaseIntermediateTarget());
      
    }
    finally {
      debug(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchEntries
  /**
   ** Extracts the entries on the given connector.
   **
   ** @param  connector          the OIM object that represent a Generic
   **                            Directory connector.
   **                            Allowed object is {@link DirectoryConnector}.
   ** @param searchBaseDN        Dn location where the entries should be pick
   **                            up.
   **                            Allowed object is {@link Set}.
   ** @param objectClasses       The set of object classes that will be
   **                            retrieved. If null any object class is applied.
   **                            Allowed object is {@link Set}.
   ** @param additionalFilter    The additional LDAP filter. Must be a valid
   **                            LDAP Filter.
   **                            Allowed object is {@link String}.
   ** @param returnAttributes    The Set of attributes in the entries that may 
   **                            be retruned.
   **                            Allowed object is {@link Set}.
   **                            
   ** @return                    The list of entries found with the provided
   **                            parameters.
   **
   ** @throws TaskException      if the operation fails
   */
  private Map<String, Attributes> searchEntries(final DirectoryConnector connector, final String searchBaseDN, final Set<String> objectClasses, final String additionalFilter, Set<String> returnAttributes)
    throws TaskException {
    
    final String method  = "searchEntries";
    debug(method, SystemMessage.METHOD_ENTRY);
    
    // create the search request control on the entire subtree.
    final SearchControls controls      = DirectoryConnector.searchScope(SearchControls.SUBTREE_SCOPE);
    // An entry needs at least a RDN and one object class but the administrator
    // can choose others attributes that can be synchronize between the two
    // systems
    if (returnAttributes == null)
      returnAttributes = new HashSet<String>();
    
    returnAttributes.add(connector.objectClassName());
    controls.setReturningAttributes(returnAttributes.toArray(new String[0]));
    //Build the search criteria that contains the object classes that we are
    //looking for and also add a potential filter requested by the administrator
    
    final String         filter        = buildFilter(connector, objectClasses, additionalFilter);
    
    final Map<String, Attributes>   result   = new HashMap<String, Attributes>();
    LdapContext context = null;
    try {
      logger.info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
      // Remove root context as the search base is concatinated to the
      // root context.
      final String rdnEntry = removeRootContext(connector.rootContext(), searchBaseDN);
      // connect to the directory system with a specific base dn
      context = connector.connect();
      
      // dispatch the retrieval of the result set accordingly to the configuration
      final DirectorySearch search = (connector.paginationControl().equals(DirectoryConstant.PAGINATION_CONTROL_VIRTUALLIST))
        ? new DirectoryListSearch(context, rdnEntry, filter, null, controls, batchSize())
        : new DirectoryPageSearch(context, rdnEntry, filter, null, controls, batchSize());
      // This while loop is used to read the LDAP entries in blocks.
     // This should decrease memory usage and help with server load.
      do {
        // Insert only the RDN without the root and and the search base context.
        // Later it will be more easy to compare entrie as tree structure might
        // be different on the source / target.
        result.putAll(populateBatch(searchBaseDN, search.next()));
      } while (search.hasMore());
      connector.disconnect();
      logger.info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
    }
    catch (DirectoryException e) {
      throw new ProcessException(ProcessError.SEARCH_ENTRY_FAILED, searchBaseDN, e.getMessage());
    }
    finally {
      // Don't need to compare the parent DN
      result.remove(searchBaseDN);
      try {
        // In case something wrong happened during the search
        // disconnect from the context
        context.close();
      }
      catch (Exception e) {
        // Return nothing
        ;
      }
      debug(method, SystemMessage.METHOD_EXIT);
    }
    return result; 
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateBatch
  /**
   ** Extracts the entries from the given result set <code>result</code>.
   **
   ** @param  rootContextDN      The root context DN where the entries is
   **                            comming from.
   **                            Allowed object is {@link String}.
   ** @param  results            the {@link NamingEnumeration} that contains the
   **                            entries fetched from the LDAP server.
   **                            Each entry is an instance of
   **                            {@link SearchResult}.
   **                            
   ** @return                    The list of entries found in the result
   **
   ** @throws TaskException      if the operation fails
   */
  private Map<String, Attributes> populateBatch(final String rootContextDN, final NamingEnumeration<SearchResult> results)
    throws TaskException {
    
    final String method  = "populateBatch";
    debug(method, SystemMessage.METHOD_ENTRY);
    
    // prepare the container for the iteration
    final Map<String, Attributes> result  = new HashMap<String, Attributes>(batchSize());
    
    // loop through the results and populate in a Map
    while ((results != null) && results.hasMoreElements()) {
      final SearchResult entry = results.nextElement();
      // extracts the relative distinguished name from the entry at put it as
      // the key of the Map value
      final String entryRDN = removeRootContext(rootContextDN, entry.getNameInNamespace());
      result.put(entryRDN, entry.getAttributes());
    }
    debug(method, SystemMessage.METHOD_EXIT);
    return result;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   proceedCompare
  /**
   ** Extracts and compares the entries from the given list of
   ** <code>sourceEntries</code> and the given list <code>targetEntries</code>.
   **
   ** @param  sourceEntries      the {@link Map} that contains the entries of
   **                            the LDAP source
   **                            Each entry is an instance of
   **                            {@link Map}.
   ** @param  targetEntries      the {@link Map} that contains the entries of
   **                            the LDAP target
   **                            Each entry is an instance of
   **                            {@link Map}.
   **                            
   ** @return                    a {@link Map} that tags entries for addition,
   **                            modification or deletion.
   */
  private Map<EntryTag, Map<String, Attributes>> proceedCompare(final Map<String, Attributes> sourceEntries, final Map<String, Attributes> targetEntries) {
    
    final String method  = "proceedCompare";
    debug(method, SystemMessage.METHOD_ENTRY);
    
    final Map<String, Attributes> addEntries    = new HashMap<String, Attributes>();
    final Map<String, Attributes> modifyEntries = new HashMap<String, Attributes>();
    final Map<String, Attributes> deleleEntries = new HashMap<String, Attributes>();
    
    // Clone the target entries in the list in order to remove every entry that
    // exists from the source. The result gave us the entries that should be
    // removed in the target system.
    deleleEntries.putAll(targetEntries);
    
    for (Map.Entry<String, Attributes> entry : sourceEntries.entrySet()) {
      final String     sourceEntryRDN   = entry.getKey();
      final Attributes sourceattributes = entry.getValue();
      if (!targetEntries.containsKey(sourceEntryRDN)) {
        addEntries.put(sourceEntryRDN, sourceattributes);
      }
      else if (!sourceattributes.equals(deleleEntries.get(sourceEntryRDN))) {
        modifyEntries.put(sourceEntryRDN, sourceattributes);
      }
      deleleEntries.remove(sourceEntryRDN);
    }
    
    final Map<EntryTag, Map<String, Attributes>>  updateList = new HashMap<EntryTag, Map<String, Attributes>>();
    if (!addEntries.isEmpty())
      updateList.put(EntryTag.ADD, addEntries);
    if (!modifyEntries.isEmpty())
      updateList.put(EntryTag.MODIFY, modifyEntries);
    if (!deleleEntries.isEmpty())
      updateList.put(EntryTag.DELETE, deleleEntries);
    
    debug(method, SystemMessage.METHOD_EXIT);
    return updateList;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTarget
  /**
   ** Update the provided entries in the LDAP target system. Then, if the search
   ** base intermediate DN has been set, it also updates subentries inside of
   ** it.
   **
   ** @param  sourceDirectory  the Generic Directory connector object where
   **                          the entries is comming from.
   **                          Allowed object is {@link DirectoryConnector}.
   ** @param  targetDirectory  the Generic Directory connector object where
   **                          the entry will be create.
   **                          Allowed object is {@link DirectoryConnector}.
   ** @param  taggedEntries    the map of entries
   **                          Allowed object is {@link Map}.
   ** 
   ** @throws TaskException    if the operation fails
   */
  private void updateTarget(final DirectoryConnector sourceDirectory, final DirectoryConnector targetDirectory, final Map<EntryTag, Map<String, Attributes>> taggedEntries, final String contextDN)
    throws TaskException {
    
    final String method  = "updateTarget";
    debug(method, SystemMessage.METHOD_ENTRY);
    try {
    // Handle entries add, modify, delete on the target
      for (Map.Entry<EntryTag, Map<String, Attributes>> entry: taggedEntries.entrySet()) {
        switch (entry.getKey()) {
          case ADD:
            createEntries(sourceDirectory, targetDirectory, taggedEntries.get(entry.getKey()), contextDN);
            break;
          case MODIFY:
            modifyEntries(targetDirectory, taggedEntries.get(entry.getKey()), contextDN);
            break;
          case DELETE:
            deleteEntries(targetDirectory, taggedEntries.get(entry.getKey()), contextDN, false);
            break;
        }
      }
    }
    catch (TaskException e) {
      throw new ProcessException(ProcessError.UPDATE_ENTRY_FAILED, e.getMessage());
    }
    
    debug(method, SystemMessage.METHOD_EXIT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntries
  /**
   ** Create the provided entries in the LDAP target system..
   **
   ** @param  sourceConnector  the Generic Directory connector object where
   **                          the entries is comming from.
   **                          Allowed object is {@link DirectoryConnector}.
   ** @param  targetConnector  the Generic Directory connector object where
   **                          the entry will be create.
   **                          Allowed object is {@link DirectoryConnector}.
   ** @param  entries          the map of entries
   **                          Allowed object is {@link Map}.
   ** @param  contextDN        the context parent dn where the entries will be
   **                          created
   **                          Allowed object is {@link String}.
   ** 
   ** @throws TaskException    if the operation fails
   */
  private void createEntries(final DirectoryConnector sourceConnector, final DirectoryConnector targetConnector, final Map<String, Attributes> entries, final String contextDN)
  throws TaskException {
    
    final String method  = "createEntry";
    debug(method, SystemMessage.METHOD_ENTRY);
    
    final String[] attributes = StringUtility.isEmpty(syncAttributes()) ? new String[0] : syncAttributes().split(SEPARATOR);
    try {
      targetConnector.connect(removeRootContext(targetConnector.rootContext(), contextDN));
      for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
        final String     entryRDN         = entry.getKey();
        final Attributes sourceAttributes = entry.getValue();
        final Attributes targetAttributes = new BasicAttributes();
        final Object     objectClasses    = sourceAttributes.get(sourceConnector.objectClassName());
        
        targetAttributes.put((Attribute) objectClasses);
        for (String attributeName : attributes) {
          final Attribute attribute = sourceAttributes.get(attributeName);
          if (attribute == null)
            continue;
        }
        // RFC4519: groupOfUniqueNames cannot be created without member inside.
        // Insert creator DN of the group as a member.
        try {
          NamingEnumeration objectClassValues = ((Attribute)objectClasses).getAll();
          do {
            Object objectClass =  objectClassValues.next();
            if (objectClass.equals(sourceConnector.groupObjectClass())) {
              targetAttributes.put(DirectoryConstant.GROUP_MEMBERSHIP_DEFAULT, targetConnector.principalName());
              break;
            }
          } while (objectClassValues.hasMoreElements());
        }
        catch (NamingException e) {
          // Do nothing
        }
        
        targetConnector.createEntry(entryRDN, targetAttributes);
      }
      targetConnector.disconnect();
    }
    finally {
      debug(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyEntryTarget
  /**
   ** Modify the provided entries in the LDAP target system..
   **
   ** @param  connector        the Generic Directory connector object where
   **                          the entries will be modify.
   **                          Allowed object is {@link DirectoryConnector}.
   ** @param  entries          the {@link Map} of the entries to modify
   ** @param  contextDN        the context parent dn where the entries will be
   **                          modified.
   **                          Allowed object is {@link String}.
   ** 
   ** @throws TaskException    if the operation fails
   */
  private void modifyEntries(final DirectoryConnector connector, final Map<String, Attributes> entries, final String contextDN)
    throws TaskException {
    
    final String method  = "modifyEntryTarget";
    debug(method, SystemMessage.METHOD_ENTRY);
    
    try {
      connector.connect(removeRootContext(connector.rootContext(), contextDN));
      for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
        final String     entryRDN    = entry.getKey();
        final Attributes attributes = entry.getValue();
        connector.attributesModify(entryRDN, attributes);
      }
      connector.disconnect();
    }
    finally {
      debug(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteEntries
  /**
   ** Delete the provided entries in the LDAP target system.
   **
   ** @param  connector          the Generic Directory connector object where
   **                            the entry will be deleted.
   **                            Allowed object is {@link DirectoryConnector}.
   ** @param  entries            the {@link Map} of the entries to delete
   ** @param  contextDN          the context parent dn where the entries will be
   **                            deleted.
   **                            Allowed object is {@link String}.
   ** @param  deleteMember       Indicate if the entry that will be deleted
   **                            contains membership entries
   ** 
   ** @throws TaskException      if the operation fails
   */
  private void deleteEntries(final DirectoryConnector connector, final Map<String, Attributes> entries, final String contextDN, final boolean deleteMember)
    throws TaskException {
    
    final String method  = "deleteEntries";
    debug(method, SystemMessage.METHOD_ENTRY);
    
    try {
      final LdapContext context        = connector.connect(removeRootContext(connector.rootContext(), contextDN));
      final boolean     deleteLeafNode = connector.isTreeDeleteControlSupported(context);
      for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
        final String entryRDN = entry.getKey();
        if (deleteMember) {
          final Attribute members = entry.getValue().get(DirectoryConstant.GROUP_MEMBERSHIP_DEFAULT);
          final Attributes attributes = new BasicAttributes();
          if (members != null) {
            attributes.put(members);
            connector.attributesDelete(entry.getKey(), attributes);
          }
        }
        if (!deleteLeafNode) {
          connector.deleteEntry(entryRDN, null);
        }
        else {
          Control control[] = { new TreeDeleteControl() };
          connector.deleteEntry(entryRDN, control);
        }
      }
    }
    finally {
        debug(method, SystemMessage.METHOD_EXIT);
        connector.disconnect();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildFilter
  /**
   ** Builds a LDAP filter with the provided object classes and an additionnal
   ** filter 
   **
   ** @param  connector          the Generic Directory connector object where
   **                            the filter will be applied.
   **                            Allowed object is {@link DirectoryConnector}.
   ** @param  objectClasses      The list of object classes that will be
   **                            retrieved.
   **                            Allowed object is {@link String}.
   ** @param  additionnalFilter  the additional filter criteria.
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link String} with the search filter
   **                            criteria.
   */
  protected String buildFilter(final DirectoryConnector connector, final Set<String> objectClasses, final String additionnalFilter) {
    final String objectClassNameAttribute = connector.objectClassName();

    final StringBuilder searchFilter      = new StringBuilder();
    final StringBuilder objectClassFilter = new StringBuilder();
    
    if (objectClasses != null && objectClasses.size() > 0) {
      objectClassFilter.append("(|");
      for (String objectClass : objectClasses) {
        objectClassFilter.append("(");
        objectClassFilter.append(objectClassNameAttribute);
        objectClassFilter.append("=");
        objectClassFilter.append(objectClass);
        objectClassFilter.append(")");
      }
      objectClassFilter.append(")");
    }
    else {
      objectClassFilter.append("(");
      objectClassFilter.append(objectClassNameAttribute);
      objectClassFilter.append("=");
      objectClassFilter.append("*");
      objectClassFilter.append(")");
    }

    if (StringUtility.isEmpty(additionnalFilter))
      searchFilter.append(objectClassFilter);
    else
      searchFilter.append("(&").append(objectClassFilter).append(additionnalFilter).append(")");

    return searchFilter.toString();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkConsistency
  /**
   ** Checks if the intermediate entries contains the provided DN entries.
   **
   ** @param  intermediatesDN   the {@link List} of the DN entries in the
   **                           intermediate
   **                           Allowed object is {@link List}.
   ** @param  entriesDN         the {@link List} of the DN entries to compare
   **                           Allowed object is {@link List}.
   **
   ** @throws TaskException     if an entry is missing in the intermediate
   */
  private void checkConsistency(final Set<String> intermediatesDN, final Set<String> entriesDN)
    throws TaskException {
    final Set<String> compareList = new HashSet<String>();
    for (String cursor : entriesDN) {
      final String rdnValue = DirectoryConnector.entryRDN(cursor).split("=")[1];
      compareList.addAll(intermediatesDN.stream()
                                        .filter(rdn -> rdn.contains(rdnValue))
                                        .collect(Collectors.toList()));
    }
    if (!compareList.equals(intermediatesDN)) {
      throw new TaskException("GDS-00001", "Wrong Source concistency");
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeRootContext
  /**
   ** Remove the root context from the provided dn entry.
   **
   ** @param  rootContextDN      the DN root context.
   **                            Allowed object is {@link String}.
   ** @param  dn                 the dn entry
   **                            Allowed object is {@link String}.
   **
   ** @return                    the dn entry whitout the root context.
   */
  private String removeRootContext(final String rootContextDN, final String dn) {
    final char dnSeparator = ',';
    return dn.replace(dnSeparator + rootContextDN, SystemConstant.EMPTY);
  }
}