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

    File        :   AbstractSyncManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    AbstractSyncManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/
package bka.iam.identity.service.synchronization;

import com.sun.jndi.ldap.ctl.TreeDeleteControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryListSearch;
import oracle.iam.identity.foundation.ldap.DirectoryPageSearch;
import oracle.iam.identity.foundation.ldap.DirectorySearch;
import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class AbstractSyncManager
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractSyncManager</code> is a abstract class wich implemen main
 ** process of LDAP object synchronization between a two LDAP server.
 ** Classes which extend this class can modify a predifinide process via overwriding of the following methods:
 ** 
 ** <ul>
 **   <li><code>beforeSynch</code>  In this method can be initialized a data which are used later on in the sych process</li>
 **   <li><code>searchSourceEntriesPostProcessing</code> source search dataset can be modified in this method </li>
 **   <li><code>searchTargetEntriesPostProcessing</code> target search dataset can be modified in thid method</li>
 **   <li><code>comparePostProcessing</code>  compared dataset can be modified in this method</li>
 **   <li><code>attributeTransformation</code> tranform a attribute valus, for example leave user DNs on member attribute </li>
 **   <li><code>afterSynch</code>  if needed in this method can be closed connection to the third party systems</li>
 ** </ul>
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractSyncManager {
  
  protected AbstractSchedulerTask scheduler;

  protected String searchBaseSource;
  protected String searchBaseTarget;
  protected Set<String> objecclassSource;
  protected Set<String> objecclassTarget;
  protected SyncConfiguration config;
  protected AttributeMapping attributeMapping;
  protected Map<EntryTag, Map<String, Attributes>> compared;



  public AbstractSyncManager(AbstractSchedulerTask scheduler, 
                             String searchBaseSource, String searchBaseTarget, 
                             Set<String> objecclassSource, Set<String> objecclassTarget,
                             AttributeMapping attributeMapping,
                             SyncConfiguration syncConfiguration
                             ) {
    this.scheduler = scheduler;
    this.searchBaseSource = searchBaseSource;
    this.searchBaseTarget = searchBaseTarget;
    this.objecclassSource = objecclassSource;
    this.objecclassTarget = objecclassTarget;
    this.attributeMapping = attributeMapping;
    this.config = syncConfiguration;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSearchBaseSource
  /**
   **
   ** @param searchBaseSource
   */
  public void setSearchBaseSource(String searchBaseSource) {
    this.searchBaseSource = searchBaseSource;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSearchBaseSource
  /**
   **
   ** @return
   */
  public String getSearchBaseSource() {
    return searchBaseSource;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSearchBaseTarget
  /**
   ** Set Search Base in the Target LDAP server
   ** @param searchBaseTarget Search Base in the Target LDAP server
   */
  public void setSearchBaseTarget(String searchBaseTarget) {
    this.searchBaseTarget = searchBaseTarget;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSearchBaseTarget
  /**
   ** Get Search Base in the Target LDAP server
   ** @return Search Base in the Target LDAP server
   */
  public String getSearchBaseTarget() {
    return searchBaseTarget;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setObjecclassSource
  /**
   ** Set objectclass in source LDAP server
   ** @param sourceObjecclass objectclass in source LDAP server
   */
  public void setObjecclassSource(Set<String> sourceObjecclass) {
    this.objecclassSource = sourceObjecclass;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getObjecclassSource
  /**
   ** Get objectclass in source LDAP server
   ** @return objectclass in source LDAP server
   */
  public Set<String> getObjecclassSource() {
    return objecclassSource;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setObjecclassTarget
  /**
   ** Set objectclass in target LDAP server
   ** @param targetObjecclass objectclass in target LDAP server
   */
  public void setObjecclassTarget(Set<String> targetObjecclass) {
    this.objecclassTarget = targetObjecclass;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getObjecclassTarget
  /**
   ** Get objectclass in target LDAP server
   ** @return objectclass in target LDAP server
   */
  public Set<String> getObjecclassTarget() {
    return objecclassTarget;
  }



  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeTransformation
  /**
   ** Framework method is executed when a MODIFY operation is needed on target system. Attributes can be 
   ** transformed in this mehtod to needed format.
   ** @param sourceAttributes source LDAP attributes
   ** @param targetAttributes target LDAP attributes
   ** @return transformed source attributes
   */
  protected Attributes attributeTransformation(Attributes sourceAttributes, Attributes targetAttributes) {
    return sourceAttributes;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Framework methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeSynch
  /**
   ** Framework method is executed before synch is executed. For example it can read extra objects from LDAP, 
   ** which are used later in calculation
   */
  protected void beforeSynch() throws TaskException {
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterSynch
  /**
   ** Framework method is executed after synch is executed. For example it can clear connections if needed 
   ** which are used later in calculation
   */
  protected void afterSynch() throws TaskException {
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   comparePostProcessing
  /**
   ** Framework method where compared dataset can be modified. For example LDAP entires
   ** for operation ADD or REMOVE can be exluded.
   ** @param compared Map of LDAP objects which needs to be modified
   */
  protected void comparePostProcessing(Map<EntryTag, Map<String, Attributes>> compared){
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSourceEntriesPostProcessing
  /**
   ** Framework method where target entries can be modified once are read from the LDAP server
   ** @param sourceEntries
   ** @return
   */
  protected Map<String, Attributes> searchSourceEntriesPostProcessing(Map<String, Attributes> sourceEntries) {
    return sourceEntries;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchTargetEntriesPostProcessing
  /**
   ** Framework method where source entries can be modified once are read from the LDAP server
   ** @param targetEntries source LDAP entries
   ** @return modified source LDAP entries
   */
  protected Map<String, Attributes> searchTargetEntriesPostProcessing(Map<String, Attributes> targetEntries) {
    return targetEntries;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeSourceDN
  /**
   ** Framework method where can be defined list of the LDAP DNs which should be excluded 
   ** from the search on source LDAP server
   ** @return
   */
  protected Set<String> excludeSourceDN(){
    return null;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeTargetDN
  /**
   ** Framework method where can be defined list of the LDAP DNs which should be excluded 
   ** from the search on target LDAP server
   ** @return
   */
  protected Set<String> excludeTargetDN(){
    return null;
  }


  //////////////////////////////////////////////////////////////////////////////
  // enum entryTag
  // ~~~~ ~~~~~~~~
  /**
   ** This enum store tags for updating entries in the target system
   */
  protected enum EntryTag{
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
  // Method:   synch
  /**
   ** This is the main method which trigger synchronization process. Synchronizatin process do following:<BR>
   ** 1.  Call <code>beforeSynch</code> method. In this method can be established connection to third party systems.
   ** 2.  Search LDAP entries in the source LDAP server
   ** 3.  Call <code>searchSourceEntriesPostProcessing</code> method with sourceEntries as inpput parameter<br>
   ** 4.  Apply LDAP Attribute mapping on the source attributes<br>
   ** 5.  Search LDAP entries in the source LDAP server
   ** 6.  Call <code>searchTargetEntriesPostProcessing</code> method with targetEntries as inpput parameter<br>
   ** 7.  Compare source and target entries <br>
   ** 8.  Call <code>comparePostProcessing</code> method with compared information<br>
   ** 9.  Update data in the target system
   ** 10. Call <code>afterSynch</code> method. In this method can be closed all connections to third party systems
   ** @throws TaskException In case there is problem with search or modify LDAP entries
   */
  protected void synch() throws TaskException{
    
    final String method  = "synch";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    // In this method can be initialized a data which are used later on in the sych process
    beforeSynch();
    
    // Retrieves entries from source and target directory.
    // Then compare source and the target list with proceedCompare
    // Finally update the entry on the target.
    Map<String, Attributes> sourceEntries = null;
    Map<String, Attributes> targetEntries = null;
    Map<String, Attributes> newTargetEntries = null;
    
    sourceEntries = searchEntries(config.getSourceDirectory(),  getSearchBaseSource(), getObjecclassSource(), null, getSourceAttributesNames(this.attributeMapping),excludeSourceDN());
    scheduler.debug(method,"sourceEntries"+sourceEntries);

    sourceEntries = searchSourceEntriesPostProcessing(sourceEntries);
    scheduler.debug(method,"sourceEntries after PostProcessed"+sourceEntries);
    
    sourceEntries = applyMapping(sourceEntries, this.attributeMapping);
    scheduler.debug(method,print("SourceEntries after mapping",sourceEntries));
    
    targetEntries = searchEntries(config.getTargetDirectory(), getSearchBaseTarget(), getObjecclassTarget(), null, getTargetAttributesNames(this.attributeMapping),excludeTargetDN());
    scheduler.info("targetEntries"+targetEntries);
    
    targetEntries = searchTargetEntriesPostProcessing(targetEntries);
    scheduler.debug(method,print("TargetEntries after PostProcessing",targetEntries));
    
    
    compared = compare(sourceEntries, targetEntries);
    scheduler.debug(method,"compared --> "+compared);
    comparePostProcessing(compared);
    
    updateTarget(config.getTargetDirectory(), compared,getSearchBaseTarget(),getObjecclassTarget());   
    
    // if needed in this method can be closed connection to the third party systems
    afterSynch();
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
  }
  

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
  protected Map<String, Attributes> searchEntries(final DirectoryConnector connector, 
                                                final String searchBaseDN, 
                                                final Set<String> objectClasses, 
                                                final String additionalFilter, 
                                                Set<String> returnAttributes,
                                                Set<String> exludedObjects)
    throws TaskException {
    
    final String method  = "searchEntries";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    // create the search request control on the entire subtree.
    final SearchControls controls      = DirectoryConnector.searchScope(SearchControls.SUBTREE_SCOPE);
    // An entry needs at least a RDN and one object class but the administrator
    // can choose others attributes that can be synchronize between the two
    // systems

    if (returnAttributes == null){
      returnAttributes = new HashSet<String>();
    }
    
    //returnAttributes.add(connector.objectClassName());
    controls.setReturningAttributes(returnAttributes.toArray(new String[0]));

    //Build the search criteria that contains the object classes that we are
    //looking for and also add a potential filter requested by the administrator
    
    final String         filter        = buildFilter(connector, objectClasses, additionalFilter);    
    
    final Map<String, Attributes>   result   = new HashMap<String, Attributes>();
    LdapContext context = null;
    try {
      scheduler.info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
      // Remove root context as the search base is concatinated to the
      // root context.
      final String rdnEntry = removeRootContext(connector.rootContext(), searchBaseDN);
    
      scheduler.debug(method,"filter --> "+filter);
      scheduler.debug(method,"rdnEntry -->"+rdnEntry);
      scheduler.debug(method,"returnAttributes --> "+returnAttributes);
      scheduler.debug(method,"getBatchSize --> "+config.getBatchSize());
    
      // connect to the directory system with a specific base dn
      context = connector.connect();
      
      // dispatch the retrieval of the result set accordingly to the configuration
      final DirectorySearch search = (connector.paginationControl().equals(DirectoryConstant.PAGINATION_CONTROL_VIRTUALLIST))
        ? new DirectoryListSearch(context, rdnEntry, filter, null, controls, config.getBatchSize())
        : new DirectoryPageSearch(context, rdnEntry, filter, null, controls, config.getBatchSize());
      // This while loop is used to read the LDAP entries in blocks.
     // This should decrease memory usage and help with server load.
      do {
        // Insert only the RDN without the root and and the search base context.
        // Later it will be more easy to compare entrie as tree structure might
        // be different on the source / target.
        result.putAll(populateBatch(searchBaseDN, search.next()));
      } while (search.hasMore());
      connector.disconnect();
      scheduler.info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
    }
    finally {
      // Don't need to compare the parent DN
      result.remove(searchBaseDN);
      
      // remove exluded DNs, all child objects  of excluded DN are exluced as well
      if(exludedObjects != null && exludedObjects.size() >0){
        Iterator<Map.Entry<String,Attributes>> i = result.entrySet().iterator();
        while(i.hasNext()){
          Map.Entry<String,Attributes> entryIterator = i.next();
          for(String exludedObject : exludedObjects){
            String rdn = entryIterator.getKey();
            if(rdn != null && rdn.endsWith(exludedObject)){
              i.remove(); 
              //scheduler.debug(method, "Removing exluded DN from result --> " + rdn);
              break;
            }
          }
        }
      }
      
      try {
        // In case something wrong happened during the search
        // disconnect from the context
        context.close();
      }
      catch (Exception e) {
        // Return nothing
        ;
      }
      scheduler.debug(method, SystemMessage.METHOD_EXIT);
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
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    // prepare the container for the iteration
    final Map<String, Attributes> result  = new HashMap<String, Attributes>(config.getBatchSize());
    
    // loop through the results and populate in a Map
    while ((results != null) && results.hasMoreElements()) {
      final SearchResult entry = results.nextElement();
      // extracts the relative distinguished name from the entry at put it as
      // the key of the Map value
      final String entryRDN = removeRootContext(rootContextDN, entry.getNameInNamespace());
      result.put(entryRDN, entry.getAttributes());
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return result;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
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
  private Map<EntryTag, Map<String, Attributes>> compare(final Map<String, Attributes> sourceEntries, final Map<String, Attributes> targetEntries) {
    
    final String method  = "compare";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    final Map<String, Attributes> addEntries    = new HashMap<String, Attributes>();
    final Map<String, Attributes> modifyEntries = new HashMap<String, Attributes>();
    final Map<String, Attributes> deleleEntries = new HashMap<String, Attributes>();
    
    // Clone the target entries in the list in order to remove every entry that
    // exists from the source. The result gave us the entries that should be
    // removed in the target system.
    deleleEntries.putAll(targetEntries);
    
    for (Map.Entry<String, Attributes> entry : sourceEntries.entrySet()) {
      final String     sourceEntryRDN   = entry.getKey();
      Attributes sourceAttributes = entry.getValue();   
      Attributes targetAttributes = targetEntries.get(sourceEntryRDN);
      
      sourceAttributes = attributeTransformation(sourceAttributes,targetAttributes);
      
      if (!targetEntries.containsKey(sourceEntryRDN)) {
        addEntries.put(sourceEntryRDN, sourceAttributes);
      }
      else if (areAttributesModified(sourceAttributes,targetAttributes)) {
        modifyEntries.put(sourceEntryRDN, sourceAttributes);
      }
      deleleEntries.remove(sourceEntryRDN);
    }
    
    // Build update list
    final Map<EntryTag, Map<String, Attributes>>  updateList = new HashMap<EntryTag, Map<String, Attributes>>();
    if (!addEntries.isEmpty())
      updateList.put(EntryTag.ADD, addEntries);
    if (!modifyEntries.isEmpty())
      updateList.put(EntryTag.MODIFY, modifyEntries);
    if (!deleleEntries.isEmpty())
      updateList.put(EntryTag.DELETE, deleleEntries);
    
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return updateList;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTarget
  /**
   ** Update the provided entries in the LDAP target system. Then, if the search
   ** base intermediate DN has been set, it also updates subentries inside of
   ** it.
   **
   ** @param  targetDirectory  the Generic Directory connector object where
   **                          the entry will be create.
   **                          Allowed object is {@link DirectoryConnector}.
   ** @param  taggedEntries    the map of entries
   **                          Allowed object is {@link Map}.
   ** 
   ** @throws TaskException    if the operation fails
   */
  private void updateTarget(final DirectoryConnector targetDirectory, final Map<EntryTag, Map<String, Attributes>> taggedEntries, final String contextDN, final Set<String> targetObjectClasses )
    throws TaskException {
    
    final String method  = "updateTarget";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    // Handle entries add, modify, delete on the target
    for (Map.Entry<EntryTag, Map<String, Attributes>> entry: taggedEntries.entrySet()) {
      switch (entry.getKey()) {
        case ADD:
          createEntries(targetDirectory, taggedEntries.get(entry.getKey()), contextDN,targetObjectClasses);
          break;
        case MODIFY:
          modifyEntries(targetDirectory, taggedEntries.get(entry.getKey()), contextDN);
          break;
        case DELETE:
          deleteEntries(targetDirectory, taggedEntries.get(entry.getKey()), contextDN, false);
          break;
      }
    }
    
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntries
  /**
   ** Create the provided entries in the LDAP target system..
   **
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
  private void createEntries(final DirectoryConnector targetConnector, final Map<String, Attributes> entries, final String contextDN, final Set<String> targetObjectClassName)
  throws TaskException {
    
    final String method  = "createEntry";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    try {
      targetConnector.connect(removeRootContext(targetConnector.rootContext(), contextDN));
      List<String> entryRDNs = new ArrayList<String>(entries.keySet());
      
      // Sort entryRDNs on hierarchy deepness. This garantees that parent element is created before a child element
      entryRDNs.sort(( s1,  s2) -> Integer.compare((int)s1.chars().filter(ch -> ch == ',').count(),(int)s2.chars().filter(ch -> ch == ',').count()));
    
      for(String entryRDN : entryRDNs){
        final Attributes attributes = entries.get(entryRDN);

        // Populated target objectclass
        Attribute objectClass = new BasicAttribute("objectClass");
        for(String objectClassName: targetObjectClassName){
          objectClass.add(objectClassName);
        }
        attributes.put(objectClass);
        
        // Remove empty attributes. LDAP is not able to create entry with attributes which are empty
        removeEmptyAttributes(attributes);
        scheduler.debug(method,"entryRDN --> " + entryRDN + " attributes size--> " + attributes.size());
        // Create LDAP entry
        targetConnector.createEntry(entryRDN, attributes);
      }
      targetConnector.disconnect();
    }
    finally {
      scheduler.debug(method, SystemMessage.METHOD_EXIT);
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
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
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
      scheduler.debug(method, SystemMessage.METHOD_EXIT);
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
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    try {
      final LdapContext context        = connector.connect(removeRootContext(connector.rootContext(), contextDN));
      final boolean     deleteLeafNode = connector.isTreeDeleteControlSupported(context);
      
      List<String> entryRDNs = new ArrayList<String>(entries.keySet());
      
      // Sort entryRDNs on hierarchy deepness. This garantees that chaild element is deleted before a parent element
      entryRDNs.sort(( s1,  s2) -> -1*Integer.compare((int)s1.chars().filter(ch -> ch == ',').count(),(int)s2.chars().filter(ch -> ch == ',').count()));
      //for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
      for(String entryRDN : entryRDNs){
        if (deleteMember) {
          final Attribute members = entries.get(entryRDN).get(DirectoryConstant.GROUP_MEMBERSHIP_DEFAULT);
          final Attributes attributes = new BasicAttributes();
          if (members != null) {
            attributes.put(members);
            connector.attributesDelete(entryRDN, attributes);
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
      scheduler.debug(method, SystemMessage.METHOD_EXIT);
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
  protected String removeRootContext(final String rootContextDN, final String dn) {
    final char dnSeparator = ',';
    return dn.replace(dnSeparator + rootContextDN, SystemConstant.EMPTY);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceStartDN
  /**
   * 
   * @param attribute
   * @param from
   * @param to
   * @return
   */
  protected Attribute replaceStartDN(Attribute attribute, String from, String to)  {
    final String method  = "replaceEndDN";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    if(attribute != null){
      Attribute modifiedAttribute = new BasicAttribute(attribute.getID()); 
      try{
        for(int i=0; i < attribute.size(); i++){
          String member = (String)attribute.get(i);
          String replacedMember = member.replaceFirst(from, to);
          // Apply Object Prefixes on the member attributes
          replacedMember = applyObjectPrefix(replacedMember);
          modifiedAttribute.add(replacedMember);  
          //scheduler.debug(method, "ReplacedEndDN from --> "+member+" to --> "+replacedMember);
        }
      }
      catch (NamingException e) {
        scheduler.fatal(method, e);
      }
      scheduler.debug(method, SystemMessage.METHOD_EXIT);
      return modifiedAttribute;
    } 
    else{
      scheduler.debug(method, SystemMessage.METHOD_EXIT);
      return null;
    }
  }
  
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   areAttributesModified
  /**
   ** Check if the attributes are modified, this method can be overwritten in the implamentation class to fit
   ** requirements for particular process. For example a member attributes can be handle differently 
   ** for different process
   ** @param sourceAttributes Source LDAP attributes
   ** @param targetAttributes Target LDAP attributes
   ** @return true in case the attributes doesn't match
   **/
  protected boolean areAttributesModified(Attributes sourceAttributes, Attributes targetAttributes){
    final String method  = "areAttributesModified";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    boolean modified = false;
    
    if (sourceAttributes == null && targetAttributes == null){
      modified = false;
    }
    else if(sourceAttributes != null){
      // Remove attributes with empty values
      Attributes emptyRemoved = (Attributes) sourceAttributes.clone();
      removeEmptyAttributes(emptyRemoved);
      // Check if the number or attributes is the same
      if(emptyRemoved.size() == targetAttributes.size()){
        // Check if attributes has the same values
        NamingEnumeration e = emptyRemoved.getAll();
        while (e.hasMoreElements()){
          Attribute sourceAttribute = (Attribute)e.nextElement();
          Attribute targetAttribute = targetAttributes.get(sourceAttribute.getID());
          if(targetAttribute == null || !sourceAttribute.equals(targetAttribute)){
            modified = true;
            break;
          }
        }
      }
      else{
        modified = true;
      }
    }
    else{
      modified = true;
    }
    
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return modified;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyObjectPrefix
  /**
   ** Object prefixes can differ in the source and target directory server. 
   ** For examle in the open ldap for organizationalUnit is used ou but in the AD is used OU.
   ** This method read object prefix from connector descriptor and apply the changes for 
   ** group and organizationoalUnits.
   ** @param entryDN Entry Distinguish Name
   ** @return LDAP Distinguished Name with applied changes object prefixes
   **/
  protected String applyObjectPrefix(String entryDN){
    String surceGroupObjectPrefix =  config.getSourceDirectory().feature().groupObjectPrefix();
    String targetGroupObjectPrefix = config.getTargetDirectory().feature().groupObjectPrefix();
    String sourceOUObjectPrefix = config.getSourceDirectory().feature().organizationalUnitObjectPrefix();
    String targetOUObjectPrefix = config.getTargetDirectory().feature().organizationalUnitObjectPrefix();
    if(!surceGroupObjectPrefix.equals(targetGroupObjectPrefix)){
      entryDN = entryDN.replaceAll(surceGroupObjectPrefix+"=", targetGroupObjectPrefix+"=");
    }
    if(!sourceOUObjectPrefix.equals(targetOUObjectPrefix)){
      entryDN = entryDN.replaceAll(sourceOUObjectPrefix+"=", targetOUObjectPrefix+"=");
    }
    return entryDN;
  }

  protected Set<String> getSourceAttributesNames(AttributeMapping attributeMapping){
    final String method  = "getSourceAttributesNames";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    Set<String> sourceAttibuteNames = new HashSet<String>();
    if(attributeMapping != null){
      AbstractAttribute[] keys = attributeMapping.attributes();
      for(AbstractAttribute key : keys){
        sourceAttibuteNames.add(key.id());
      }
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return sourceAttibuteNames;
  }
  
  protected Set<String> getTargetAttributesNames(AttributeMapping attributeMapping){
    final String method  = "getTargetAttributesNames";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    Set<String> targetAttibuteNames = new HashSet<String>();
    if(attributeMapping != null){
      AbstractAttribute[] keys = attributeMapping.attributes();
      for(AbstractAttribute key : keys){
        targetAttibuteNames.addAll(Arrays.asList(attributeMapping.stringValue(key.id()).split("\\|") ));
      }
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return targetAttibuteNames;
  }
  
  protected String getTargetAttributeName(String sourceAttributeName){
    return this.attributeMapping.stringValue(sourceAttributeName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeStartDN
  /**
   * Remove beginning of the LDAP DN. This method can be used when we wanted to remove root context DN
   * @param fullDN Complete LDAP DN
   * @param startDN LDAP DN which should be removed from the fullDN
   * @return LDAP DN without the start DN
   */
  protected static String removeStartDN(String fullDN, String startDN){
    String dn = null;
    
    try {
      LdapName full = new LdapName(fullDN);
      LdapName start = new LdapName(startDN);
    
      if(full.startsWith(start)){
        for(int i=0 ; i < start.size() ; i++){
          full.remove(0);
        }
        dn = full.toString();
      }
    } 
    catch (InvalidNameException e) {
      e.printStackTrace();
    }
    return dn;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWith
  /**
   ** Check if the @param fullDN starts with @param starDN.<br>
   ** It returns true in this exampe:<br>
   ** fullDN: cn=test,dc=example,dc=com<BR>
   ** startDN: dc=example,dc=com<BR>
   ** @param fullDN testing DN
   ** @param startDN start DN
   ** @return true in case @param fullDN starts with @param starDN.
   */
  protected static boolean startsWith(String fullDN, String startDN){
    boolean startWith = false;    
    try {
      LdapName full = new LdapName(fullDN);
      LdapName start = new LdapName(startDN);
    
      if(full.startsWith(start)){
        startWith =  true;
      }
    } 
    catch (InvalidNameException e) {
      e.printStackTrace();
    }
    return startWith;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addExcluded
  /**
   ** Loop over all values on @param fromAttribute and if the value DOESN"T end with @param dnFilter 
   ** copy that value to the @param toAttribute
   ** @param toAttribute LDAP attribute where data will be copied
   ** @param fromAttribute source LDAP attribute on which are data searched
   ** @param dnFilter LDAP DN string used as filter
   */
  protected void addExcluded(Attribute toAttribute, Attribute fromAttribute,String dnFilter){
    if(dnFilter != null){
      Set<String> dnFilters = new HashSet<String>();
      dnFilters.add(dnFilter);
      addExcluded(toAttribute,fromAttribute,dnFilters);
    }
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addExcluded
  /**
   ** Loop over all values on @param fromAttribute and if the value DOESN"T end with @param dnFilter 
   ** copy that value to the @param toAttribute
   ** @param toAttribute LDAP attribute where data will be copied
   ** @param fromAttribute source LDAP attribute on which are data searched
   ** @param dnFilters LDAP DN string used as filter
   */
  protected void addExcluded(Attribute toAttribute, Attribute fromAttribute, Set<String> dnFilters){
    final String method  = "addExcluded";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    if(toAttribute !=null && fromAttribute != null && dnFilters != null){
      try{
        for(int i=0; i < fromAttribute.size(); i++){
          String member = (String)fromAttribute.get(i);
          boolean meetFilter = true;
          for(String dn : dnFilters){
            if(member.endsWith(dn.trim())){
              meetFilter = false;
              break;
            }
          }
          if(meetFilter){
            toAttribute.add(member);
            scheduler.debug(method, "Added member: "+member);    
          }
        }
      }
      catch (NamingException e) {
        scheduler.fatal(method, e);
      }
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addIncluded
  /**
   ** Loop over all values on @param fromAttribute and if the value ends with @param dnFilter 
   ** copy that value to the @param toAttribute
   ** @param toAttribute LDAP attribute where data will be copied
   ** @param fromAttribute source LDAP attribute on which are data searched
   ** @param dnFilter LDAP DN string used as filter
   */
  protected void addIncluded(Attribute toAttribute, Attribute fromAttribute,String dnFilter){
    if(dnFilter != null){
      Set<String> dnFilters = new HashSet<String>();
      dnFilters.add(dnFilter);
      addIncluded(toAttribute,fromAttribute,dnFilters);
    }
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addIncluded
  /**
   ** Loop over all values on @param fromAttribute and if the value ends with any value defined @param dnFilter 
   ** copy that value to the @param toAttribute
   ** @param toAttribute LDAP attribute where data will be copied
   ** @param fromAttribute source LDAP attribute on which are data searched
   ** @param dnFilter LDAP DN string used as filter
   */
  protected void addIncluded(Attribute toAttribute, Attribute fromAttribute, Set<String> dnFilter){
    final String method  = "addIncluded";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    if(toAttribute !=null && fromAttribute != null && dnFilter != null){
      try{
        for(int i=0; i < fromAttribute.size(); i++){
          String member = (String)fromAttribute.get(i);
          for(String dn : dnFilter){
            if(member.endsWith(dn.trim())){
              toAttribute.add(member);
              scheduler.debug(method, "Added member: "+member);    
            }
          }
        }
      }
      catch (NamingException e) {
        scheduler.fatal(method, e);
      }
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchReport
  /**
   * Generate synchronization report
   * @return String representing synchronization report
   */
  protected String synchReport(){
    StringBuffer sb = new StringBuffer();
    sb.append("Process ").append(this.getClass().getName()).append("\n");
    sb.append("----------------------\n");
    if(compared == null || compared.size() == 0){
      sb.append(" no synchronization was needed\n");
    }
    else{
      for (Map.Entry<EntryTag, Map<String, Attributes>> entry: compared.entrySet()) {
        switch (entry.getKey()) {
          case ADD:
            sb.append(print("ADD",compared.get(entry.getKey())));
            break;
          case MODIFY:
            sb.append(print("MODIFY",compared.get(entry.getKey())));
            break;
          case DELETE:
            sb.append(print("DELETE",compared.get(entry.getKey())));
            break;
        }
      }
    }
    sb.append("\n");
    return sb.toString();
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyMapping
  /**
   * Apply attribute mapping. Attribte mapping is defined on Scheduled job parameter
   * @param sourceEntries Source LDAP entries
   * @param attributeMapping Attribute mapping
   * @return Mapped source entries
   * @throws TaskException if mapping has failed
   */
  private Map<String, Attributes> applyMapping(Map<String, Attributes> sourceEntries,
                                               AttributeMapping attributeMapping) throws TaskException {
    final String method  = "applyMapping";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    Map<String, Attributes> mappedEntries = new HashMap<String, Attributes>();
    try {
      if(attributeMapping == null || sourceEntries == null){
        mappedEntries = sourceEntries;
      }
      else{
        // Loop over all entries
        for (Map.Entry<String, Attributes> entry : sourceEntries.entrySet()) {
          String     entryRDN   = entry.getKey();
          Attributes sourceAttributes = entry.getValue();
          
          if(sourceAttributes != null){
            Attributes mappedAttributes = new BasicAttributes();
            Set<String> srcAttNames = attributeMapping.keySet();
            // Loop over source mapping attribute names
            for(String srcAttrName : srcAttNames ){
              Attribute srcAttr = sourceAttributes.get(srcAttrName);
              String mapping = (String)attributeMapping.get(srcAttrName);
              String[] mapAttributeIDs = mapping.split("\\|");
              // Loop over target mappings attribute names
              for(String mapAttributeID: mapAttributeIDs){
                Attribute mappedAttribute = new BasicAttribute(mapAttributeID);  
                // Copy all values from source to the mapped attribute
                if(srcAttr != null){
                  NamingEnumeration attrEnum = srcAttr.getAll();
                  while(attrEnum.hasMore()){
                    mappedAttribute.add(attrEnum.nextElement());
                  }
                }
                mappedAttributes.put(mappedAttribute);
              }
            }
            
            // Adapt entryRDN to object prefix, ou and cn can be replaced to OU and CN
            entryRDN = applyObjectPrefix(entryRDN);

            // Add mapped entry to return map
            mappedEntries.put(entryRDN, mappedAttributes);
          }
        }
      }
    } catch (NamingException e) {
      scheduler.fatal(method, e);
      throw new TaskException(e);
    }
    
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return mappedEntries;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Print LDAP entries in user readable format
   ** @param header Header information
   ** @param data LDAP entries
   ** @return LDAP enries in user readable format
   */
  private String print(String header, Map<String, Attributes> data){
    final String method  = "print";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    StringBuffer sb = new StringBuffer();
    if(data != null){
      sb.append(" "+header+"\n");
      sb.append("-----\n");
      for (Map.Entry<String, Attributes> entry : data.entrySet()) {
        
        Attributes attributes = entry.getValue();
        sb.append("\tdn: "+entry.getKey()).append(" [");
        NamingEnumeration e = attributes.getAll();
        while (e.hasMoreElements()){
          Attribute attr = (Attribute)e.nextElement();
          NamingEnumeration attrEnum;
          try {
            attrEnum = attr.getAll();
            sb.append(attr.getID()+": ");
            boolean flag = false;
            while(attrEnum.hasMore()){
              if(flag){
                sb.append(" | ");
              }
              sb.append(attrEnum.nextElement());
              flag=true;
            }
          } catch (NamingException f) {
            scheduler.fatal(method, f);
          }
          sb.append(", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("]\n");
      }
    }
    return sb.toString();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeEmptyAttributes
  /**
   * Remove all empty LDAP attributes from input @param attributes.
   * LDAP Create API throws a exeption in case LDAP entry contains empty attributes. This method fix this problem.
   * @param attributes
   */
  private void removeEmptyAttributes(Attributes attributes){
    NamingEnumeration<String> e = attributes.getIDs();
    while(e.hasMoreElements()){
      String attriubteName = e.nextElement();
      Attribute attribute = attributes.get(attriubteName);
      if(attribute.size() == 0){
        attributes.remove(attriubteName);
      }
    }
  }

}
