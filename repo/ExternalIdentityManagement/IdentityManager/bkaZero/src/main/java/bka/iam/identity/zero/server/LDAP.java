package bka.iam.identity.zero.server;

import bka.iam.identity.zero.ZeroMessage;
import bka.iam.identity.zero.resources.ZeroBundle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.logging.AbstractLoggable;
import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.utility.StringUtility;

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

public class LDAP extends AbstractLoggable {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  final protected DirectoryFeature    feature;
  final protected DirectoryResource   resource;
  final protected DirectoryConnector  directory;
  
  protected int batchSize  = 1000;
  
  
  ////////////////////////////////////////////////////////////////////////////
  // Constructors
  ////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs an empty <code>LDAP</code> instance that allows use as a
   ** JavaBean.
   ** 
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>AbstractLoggable</code>.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  resourceName       the name of the <code>IT Resource</code> where
   **                            the LDAP connection information is hold.
   **                            Allowed object is {@link String}.
   **                            
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries or one or
   **                            more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   **                            
   */
  public LDAP(final Loggable loggable, final String resourceName)
    throws TaskException {
    // ensure inheritance
    super(loggable);
    
    this.feature   = DirectoryFeature.build(this);
    this.resource  = new DirectoryResource(this, resourceName);
    this.directory = new DirectoryConnector(this, this.resource, feature);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBatchSize
  /**
   ** Returns the batch size of for the  synchronization source and target.
   ** 
   ** @param  batchSize          the value of the batch size to set.
   **                            <br>
   **                            Allowed object is {@link int}.
   */
  public void setBatchSize(final int batchSize) {
    this.batchSize = batchSize;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBatchSize
  /**
   ** Returns the batch size of for the  synchronization source and target.
   ** 
   ** @return                    the batch size value.
   */
  public int getBatchSize() {
    return this.batchSize;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildFilter
  /**
   ** Builds a LDAP filter with the provided object classes and an additionnal
   ** filter 
   **
   ** @param  objectClasses      The list of object classes that will be
   **                            retrieved.
   **                            Allowed object is {@link String}.
   ** @param  additionnalFilter  the additional filter criteria.
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link String} with the search filter
   **                            criteria.
   */
  protected String buildFilter(final Set<String> objectClasses, final String additionnalFilter) {
    final String objectClassNameAttribute = directory.objectClassName();

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
  // Method:   populateBatch
  /**
   ** Extracts the entries from the given result set <code>result</code>.
   **
   ** @param  results            the {@link NamingEnumeration} that contains the
   **                            entries fetched from the LDAP server.
   **                            Each entry is an instance of
   **                            {@link SearchResult}.
   **                            
   ** @return                    The list of entries found in the result
   **
   */
  private Map<String, Attributes> populateBatch(final NamingEnumeration<SearchResult> results) {
    
    final String method  = "populateBatch";
    debug(method, SystemMessage.METHOD_ENTRY);
    
    // prepare the container for the iteration
    final Map<String, Attributes> result  = new HashMap<String, Attributes>(this.batchSize);
    
    // loop through the results and populate in a Map
    while ((results != null) && results.hasMoreElements()) {
      final SearchResult entry = results.nextElement();
      // extracts the relative distinguished name from the entry at put it as
      // the key of the Map value
      final String entryRDN = removeRootContext(entry.getNameInNamespace());
      result.put(entryRDN, entry.getAttributes());
    }
    debug(method, SystemMessage.METHOD_EXIT);
    return result;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeRootContext
  /**
   ** Remove the root context from the provided dn entry.
   **
   ** @param  dn                 the dn entry
   **                            Allowed object is {@link String}.
   **
   ** @return                    the dn entry whitout the root context.
   */
  private String removeRootContext(final String dn) {
    final char dnSeparator = ',';
    return dn.replace(dnSeparator + this.directory.rootContext(), SystemConstant.EMPTY);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupEntries
  /**
   ** Lookup a specific entries from the LDAP instance and return its
   ** attributes.
   **
   ** @param dn                  Dn of the entry
   **                            Allowed object is {@link String}.
   ** @param objectClasses       The set of object classes that will be
   **                            retrieved. If null any object class is applied.
   **                            Allowed object is {@link Set}.
   ** @param returnAttributes    The Set of attributes in the entries that may 
   **                            be retruned.
   **                            Allowed object is {@link Set}.
   **                            
   ** @return                    The list of entries found with the provided
   **                            parameters.
   **                            
   ** @throws DirectoryException if a error occured when retrieving or
   **                            manipulated ldap entries.
   */
  public Attributes lookupEntries(final String dn, final Set<String> objectClasses, final Set<String> returnAttributes)
    throws DirectoryException {
    
    final String method  = "lookupEntries";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    Map<String, Attributes> entries = searchEntries(dn, objectClasses, null, SearchControls.OBJECT_SCOPE, returnAttributes);
    
    trace(method, SystemMessage.METHOD_EXIT);
    return entries.get(removeRootContext(dn));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchEntries
  /**
   ** Extracts the entries on the given connector from the LDAP instance.
   **
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
   ** @throws DirectoryException if a error occured when retrieving or
   **                            manipulated ldap entries.
   */
  public Map<String, Attributes> searchEntries(final String searchBaseDN, final Set<String> objectClasses, final String additionalFilter, final Set<String> returnAttributes)
    throws DirectoryException {
    
    return searchEntries(searchBaseDN, objectClasses, additionalFilter, SearchControls.SUBTREE_SCOPE, returnAttributes);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchEntries
  /**
   ** Extracts the entries on the given connector from the LDAP instance.
   **
   ** @param searchBaseDN        Dn location where the entries should be pick
   **                            up.
   **                            Allowed object is {@link Set}.
   ** @param objectClasses       The set of object classes that will be
   **                            retrieved. If null any object class is applied.
   **                            Allowed object is {@link Set}.
   ** @param additionalFilter    The additional LDAP filter. Must be a valid
   **                            LDAP Filter.
   **                            Allowed object is {@link String}.
   ** @param  searchScope        the scope of search to obtain for the
   **                            predefined objects.
   ** @param returnAttributes    The Set of attributes in the entries that may 
   **                            be retruned.
   **                            Allowed object is {@link Set}.
   **                            
   ** @return                    The list of entries found with the provided
   **                            parameters.
   **                            
   ** @throws DirectoryException if a error occured when retrieving or
   **                            manipulated ldap entries.
   */
  public Map<String, Attributes> searchEntries(final String searchBaseDN, final Set<String> objectClasses, final String additionalFilter, final int searchScope, Set<String> returnAttributes)
    throws DirectoryException {
    
    final String method  = "searchEntries";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    if (returnAttributes == null)
      returnAttributes = new HashSet<String>();

     // create the search request control on the entire subtree.
    final SearchControls controls      = DirectoryConnector.searchScope(searchScope);
    controls.setReturningAttributes(returnAttributes.toArray(new String[0]));
    
    final String                    filter   = buildFilter(objectClasses, additionalFilter);
    final Map<String, Attributes>   result   = new HashMap<String, Attributes>();  
    try {
      
      // connect to the directory system
      final LdapContext context = directory.connect();
      trace(method, ZeroBundle.format(ZeroMessage.LDAP_SEARCH_QUERY, filter, searchBaseDN));
      info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
      
      // dispatch the retrieval of the result set accordingly to the configuration
      final DirectorySearch search = (directory.paginationControl().equals(DirectoryConstant.PAGINATION_CONTROL_VIRTUALLIST))
        ? new DirectoryListSearch(context, removeRootContext(searchBaseDN), filter, null, controls, this.batchSize)
        : new DirectoryPageSearch(context, removeRootContext(searchBaseDN), filter, null, controls, this.batchSize);
      // This while loop is used to read the LDAP entries in blocks.
     // This should decrease memory usage and help with server load.
      do {
        // Insert only the RDN without the root and and the search base context.
        // Later it will be more easy to compare entrie as tree structure might
        // be different on the source / target.
        result.putAll(populateBatch(search.next()));
      } while (search.hasMore());
      
      directory.disconnect();
      trace(method, ZeroBundle.format(ZeroMessage.LDAP_SEARCH_RESULT, result));
      info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
    }
    finally {
      // Don't need to compare the parent DN
      result.remove(searchBaseDN);
      try {
        // In case something wrong happened during the search
        // disconnect from the context
        directory.disconnect();
      }
      catch (Exception e) {
        // Return nothing
        ;
      }
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result; 
  }
}
