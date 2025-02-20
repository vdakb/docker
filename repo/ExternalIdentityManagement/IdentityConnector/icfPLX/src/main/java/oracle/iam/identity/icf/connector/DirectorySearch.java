/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PAfRTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   DirectorySearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectorySearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import com.sun.jndi.ldap.ctl.VirtualListViewControl;
import com.sun.jndi.ldap.ctl.VirtualListViewResponseControl;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import javax.naming.ldap.SortControl;
import javax.naming.ldap.SortKey;
import javax.naming.ldap.SortResponseControl;

import oracle.iam.identity.icf.foundation.OperationContext;
import oracle.iam.identity.icf.foundation.SystemConstant;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.resource.Connector;
import oracle.iam.identity.icf.resource.ConnectorBundle;

import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;
////////////////////////////////////////////////////////////////////////////////
// abstract class DirectorySearch
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>DirectorySearch</code> provides the capabilities to control a LDAP
 ** search.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DirectorySearch extends DirectoryOperation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Map<String, Integer> SCOPE = new HashMap<String, Integer>(3);

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    SCOPE.put(OperationOptions.SCOPE_OBJECT,    new Integer(SearchControls.OBJECT_SCOPE));
    SCOPE.put(OperationOptions.SCOPE_ONE_LEVEL, new Integer(SearchControls.ONELEVEL_SCOPE));
    SCOPE.put(OperationOptions.SCOPE_SUBTREE,   new Integer(SearchControls.SUBTREE_SCOPE));
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final int               limit;
  protected final int               scope;
  protected final String            filter;
  protected final DirectoryName     base;
  protected final SearchControls    control;
  protected final Set<String>       emit     = new HashSet<String>();

  protected final Set<ObjectClass>  groups   = new HashSet<ObjectClass>();

  protected final String            entryIdentifier;

  protected byte[]                  id;
  protected Control[]               request;
  
  final Map<ObjectClass, List<SearchResult>>    permissionCache;
  


  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class Paginated
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Paginated</code> provides the capabilities to control a query based
   ** on the Paged Result Request Control.
   */
  static class Paginated extends DirectorySearch {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a <code>Paginated</code> Search that provides the capabilities
     ** to control the query base on the {@link PagedResultsControl}.
     **
     ** @param  endpoint         the Directory Service endpoint connection which
     **                          is used to by this {@link DirectoryOperation}.
     **                          <br>
     **                          Allowed object is {@link DirectoryEndpoint}.
     ** @param  type             the logical object class to search the entry in
     **                          the Directory Service that belong to
     **                          <code>context</code>.
     **                          <br>
     **                          Allowed object is {@link ObjectClass}
     ** @param  base             the base DN to search from
     **                          <br>
     **                          Allowed object is {@link DirectoryName}
     ** @param  scope            the search scope that control the search. If
     **                          <code>0</code>, the default search controls
     **                          are used (equivalent to
     **                          <code>(new SearchControls())</code>).
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  limit            the size limit of the result set to be
     **                          returned in a batch.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  filter           the filter expression to use for the search;
     **                          must not be null, e.g.
     **                          "(&amp;(objectclass=*)(cn=abraham))"
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  emit             the identifiers of the attributes to return
     **                          along with the entry. If <code>null</code>,
     **                          return all attributes. If empty return no
     **                          attributes.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param  order            the keys the result set will be ordered at
     **                          server side.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     **
     ** @throws SystemException  if the appropriate Request Controls cannot be
     **                          initialized.
     */
    private Paginated(final DirectoryEndpoint endpoint, final ObjectClass type, final DirectoryName base, final int scope, final int limit, final String filter, final Set<String> emit, final Set<String> order)
      throws SystemException {

      // ensure inheritance
      super(endpoint, type, base, scope, limit, filter, emit);

      // initialize instance
      this.request = new Control[order != null && order.size() > 0 ? 2 : 1];
      try {
        // request a paginated result from the Directory Service
        // each loop iteration will re-activate paged results
        this.request[0] = new PagedResultsControl(this.limit, this.id, Control.CRITICAL);
        if (order != null && order.size() > 0)
          // request a result set that is sorted by the specified values
          // the request control is created with the same criticality as the
          // result set. If the LDAP server doesn't support the control it will
          // fail afterwards
          this.request[1] = new SortControl(sortBy(order), Control.CRITICAL);
      }
      catch (IOException e) {
        throw SystemException.unhandled(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hasMore (DirectorySearch)
    /**
     ** Extract the paging controls from the {@link Control}s returned from the
     ** Directory Service after a search operation was performed.
     **
     ** @return                    <code>true</code> if EOF is not reached by
     **                            the server side result set; otherwise
     **                            <code>false</code>.
     **                            <br>
     **                            Possible object is <code>boolean</code>.
     **
     ** @throws SystemException    if it's not possible to determine the state
     **                            of server side result set by parsing the
     **                            response controls returned by the Directory
     **                            Service.
     */
    @Override
    public boolean hasMore()
      throws SystemException {

      // reset
      this.id = null;
      try {
        final Control[] response = this.endpoint.unwrap().getResponseControls();
        if (response != null) {
          for (int i = 0; i < response.length; i++) {
            final String  oid = response[i].getID();
            if (PagedResultsResponseControl.OID.equals(oid)) {
              this.id         = new PagedResultsResponseControl(oid, response[i].isCritical(), response[i].getEncodedValue()).getCookie();
              this.request[0] = new PagedResultsControl(this.limit, this.id, Control.CRITICAL);
            }
          }
        }
      }
      catch (IOException e) {
        throw SystemException.unhandled(e);
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
      return this.id != null && this.id.length != 0;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class VirtualList
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** <code>VirtualList</code> provides the capabilities to control a query
   ** based on the Virtual List Request Control.
   */
  private static class VirtualList extends DirectorySearch {

    ////////////////////////////////////////////////////////////////////////////
    // instane attributes
    ////////////////////////////////////////////////////////////////////////////

    protected int after;
    protected int before;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
   /**
     ** Creates a <code>VirtualList</code> Search that provides the capabilities
     ** to control the query base on the {@link VirtualListViewControl}.
     **
     ** @param  endpoint         the Directory Service endpoint connection which
     **                          is used to by this {@link DirectoryOperation}.
     **                          <br>
     **                          Allowed object is {@link DirectoryEndpoint}.
     ** @param  type             the logical object class to search the entry in
     **                          the Directory Service that belong to
     **                          <code>context</code>.
     **                          <br>
     **                          Allowed object is {@link ObjectClass}
     ** @param  base             the base DN to search from
     **                          <br>
     **                          Allowed object is {@link DirectoryName}
     ** @param  scope            the search scope that control the search. If
     **                          <code>0</code>, the default search controls
     **                          are used (equivalent to
     **                          <code>(new SearchControls())</code>).
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  limit            the size limit of the result set to be
     **                          returned in a batch.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  filter           the filter expression to use for the search;
     **                          must not be null, e.g.
     **                          "(&amp;(objectclass=*)(cn=abraham))"
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  emit             the identifiers of the attributes to return
     **                          along with the entry. If <code>null</code>,
     **                          return all attributes. If empty return no
     **                          attributes.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param  order            the keys the result set will be ordered at
     **                          server side.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     **
     ** @throws SystemException  if the appropriate Request Controls cannot be
     **                          initialized.
     */
    private VirtualList(final DirectoryEndpoint endpoint, final ObjectClass type, final DirectoryName base, final int scope, final int limit, final String filter, final Set<String> emit, final Set<String> order)
      throws SystemException {

      // ensure inheritance
      super(endpoint, type, base, scope, limit, filter, emit);

      // initialize instance
      this.before  = 1;
      this.after   = 0;
      this.request = new Control[2];
      try {
        // request a virtual liset view result from the Directory Service
        // each loop iteration will re-activate paged results
        this.request[0] = new VirtualListViewControl(this.before, this.after, 0, this.limit - 1, Control.CRITICAL);
        // request a result set that is sorted by the specified values
        // the request control is created with the same Control.CRITICAL because
        // the Virtual View List control requires that the result set is sorted.
        // If the LDAP server doesn't support the control it will fail afterwards
        this.request[1] = new SortControl(sortBy(order), Control.CRITICAL);
      }
      catch (IOException e) {
        throw SystemException.unhandled(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   hasMore (DirectorySearch)
    /**
     ** Extract the list controls from the {@link Control}s returned from the
     ** Directory Service after a search operation was performed.
     **
     ** @return                  <code>true</code> if EOF is not reached by the
     **                          server side result set; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @throws SystemException  if it's not possible to determine the state of
     **                          server side result set by parsing the response
     **                          controls returned by the Directory Service.
     */
    @Override
    public boolean hasMore()
      throws SystemException {

      try {
        for (Control response : this.endpoint.unwrap().getResponseControls()) {
          if (SortResponseControl.OID.equals(response.getID())) {
  					final SortResponseControl control = (SortResponseControl)response;
					  if (!control.isSorted() || (control.getResultCode() != 0))
				  		throw SystemException.abort(control.getException());
			  	}
		  		else if (VirtualListViewResponseControl.OID.equals(response.getID())) {
	  				final VirtualListViewResponseControl control = (VirtualListViewResponseControl)response;
  					if (control.getResultCode() == 0) {
					  	this.before += this.limit;
				  		this.after   = control.getListSize();
              // request a virtual liset view result from the Directory Service
              // each loop iteration will re-activate paged results
              this.request[0] = new VirtualListViewControl(this.before, this.after, 0, this.limit - 1, Control.CRITICAL);
  					}
            else
				  		throw SystemException.abort(control.getException());
          }
        }
      }
      catch (IOException e) {
        throw SystemException.unhandled(e);
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
      return this.before <= this.after;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Simple
  // ~~~~~ ~~~~~~
  /**
   ** <code>Simple</code> provides the capabilities to control a query based
   ** on the Paged Result Request Control.
   */
  static class Simple extends DirectorySearch {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a <code>Simple</code> Search that provides no further
     ** capabilities to control the query.
     **
     ** @param  endpoint         the Directory Service endpoint connection which
     **                          is used to by this {@link DirectoryOperation}.
     **                          <br>
     **                          Allowed object is {@link DirectoryEndpoint}.
     ** @param  type             the logical object class to search the entry in
     **                          the Directory Service that belong to
     **                          <code>context</code>.
     **                          <br>
     **                          Allowed object is {@link ObjectClass}
     ** @param  base             the base DN to search from
     **                          <br>
     **                          Allowed object is {@link DirectoryName}
     ** @param  scope            the search scope that control the search. If
     **                          <code>0</code>, the default search controls
     **                          are used (equivalent to
     **                          <code>(new SearchControls())</code>).
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  limit            the size limit of the result set to be
     **                          returned in a batch.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  filter           the filter expression to use for the search;
     **                          must not be null, e.g.
     **                          "(&amp;(objectclass=*)(cn=abraham))"
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  emit             the identifiers of the attributes to return
     **                          along with the entry. If <code>null</code>,
     **                          return all attributes. If empty return no
     **                          attributes.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param  order            the keys the result set will be ordered at
     **                          server side.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     **
     ** @throws SystemException  if the appropriate Request Controls cannot be
     **                          initialized.
     */
    protected Simple(final DirectoryEndpoint endpoint, final ObjectClass type, final DirectoryName base, final int scope, final int limit, final String filter, final Set<String> emit, final Set<String> order)
      throws SystemException {

      // ensure inheritance
      super(endpoint, type, base, scope, limit, filter, emit);

      // change the limit of the search control
      // this.control.setCountLimit(limit);
      // initialize instance
      try {
        if (order != null && order.size() > 0) {
          // request a result set that is sorted by the specified values
          // the request control is created with the same criticality as the
          // result set. If the LDAP server doesn't support the control it will
          // fail afterwards
          this.request    = new Control[1];
          this.request[0] = new SortControl(sortBy(order), Control.CRITICAL);
        }
      }
      catch (IOException e) {
        throw SystemException.unhandled(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hasMore (DirectorySearch)
    /**
     ** Extract the paging controls from the {@link Control}s returned from the
     ** Directory Service after a search operation was performed.
     **
     ** @return                  <code>true</code> if EOF is not reached by the
     **                          server side result set; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws SystemException  if it's not possible to determine the state of
     **                          server side result set by parsing the response
     **                          controls returned by the Directory Service.
     */
    @Override
    public boolean hasMore()
      throws SystemException {

      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>DirectorySearch</code> that provides the capabilities to
   ** control a LDAP search.
   **
   ** @param  endpoint           the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to search the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  base               the base DN to search from
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  scope              the search scope that control the search.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  limit              the size limit of the result set to be
   **                            returned in a batch.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  filter             the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  emit               the identifiers of the attributes to return
   **                            along with the entry. If <code>null</code>,
   **                            return all attributes. If empty return no
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  private DirectorySearch(final DirectoryEndpoint endpoint, final ObjectClass type, final DirectoryName base, final int scope, final int limit, final String filter, final Set<String> emit)
    throws SystemException {
    // ensure inheritance
    super(endpoint, type);
    
    // initialize instance attributes
    this.id              = SystemConstant.EMPTY.getBytes();
    this.base            = base;
    this.scope           = scope;
    this.filter          = filter;
    this.limit           = limit;
    this.entryIdentifier = endpoint.entryIdentifierAttribute();
    this.permissionCache = new HashMap<ObjectClass, List<SearchResult>>();

    this.emit.add(this.entryIdentifier);
    for (String cursor : emit) {
      // handle special attributes appropriatly
      if (Uid.NAME.equals(cursor) || Name.NAME.equals(cursor) || OperationalAttributes.ENABLE_NAME.equals(cursor) || OperationalAttributes.PASSWORD_NAME.equals(cursor))
        continue;

      // go an extra mile to exclude the embedded object like groups, tenants
      // or spaces from the query
      else if (ObjectClass.GROUP.getObjectClassValue().equals(cursor)) {
        this.groups.add(ObjectClass.GROUP);
      }
      else if (DirectorySchema.PROXY.getObjectClassValue().equals(cursor)) {
        this.groups.add(DirectorySchema.PROXY);
      }
      else {
        this.emit.add(cursor);
      }
    }
    this.control = new SearchControls(scope, 0, 0, this.emit.toArray(new String[0]), false, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a proper search strategy for entries in a
   ** Directory Service.
   **
   ** @param  endpoint           the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to search the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  option             the options that affect the way this operation
   **                            is run.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   ** @param  filter             the filter criteria to apply on the search.
   **                            <br>
   **                            Allowed object is {@link DirectoryFilter}.
   **
   ** @return                    an instance of <code>Paginated</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectorySearch</code>.
   **
   ** @throws SystemException   if the appropriate Request Controls cannot be
   **                           initialized.
   */
  public static DirectorySearch build(final DirectoryEndpoint endpoint, final ObjectClass type, final OperationOptions option, final DirectoryFilter filter)
    throws SystemException {

    // initialize the operation context configuration
    final OperationContext control = OperationContext.build(option);

    // This is a bit tricky. If the DirectoryFilter has an entry DN, we only
    // need to look at that entry and check whether it matches the native
    // filter.
    // Moreover, when looking at the entry DN we must not throw exceptions if
    // the entry DN does not exist or is not valid -- just as no exceptions
    // are thrown when the native filter doesn't return any values.
    //
    // In the simple case when the DirectoryFilter has no entryDN, we will just
    // search over our base DN's looking for entries matching the native filter.
    DirectoryName   base        = DirectoryName.ROOT;
    String          entryFilter = filter != null ? filter.entryDN() : null;
    int             scope       = SCOPE.containsKey(control.scope) ? SCOPE.get(control.scope) : SCOPE.get(OperationOptions.SCOPE_SUBTREE);
    if (entryFilter != null) {
      // would be good to check that entryFilter is under the configured base
      // contexts.
      // However, the adapter is likely to pass entries outside the base
      // contexts, so not checking in order to be on the safe side.
      base  = DirectoryName.build(entryFilter);
      scope = SCOPE.get(OperationOptions.SCOPE_OBJECT);
    }
    else {
      if (control.base != null) {
        base = DirectoryName.build(control.base.getUid().getUidValue());
      }
    }
    String incrementalFilter = null;
    if (control.incremental) {
      incrementalFilter = String.format("(|(%1$s>=%3$s)(%2$s>=%3$s))",  endpoint.entryCreatedAttribute(), endpoint.entryModifiedAttribute(), control.synchronizationToken);
    }
    
    String nativeFilter = filter != null ? filter.expression() : null;
    if (endpoint.virtualListControl() && endpoint.supported(VirtualListViewControl.OID)) {
      return new VirtualList(endpoint, type, base, scope, control.limit, searchFilter(endpoint, type, entryFilter, nativeFilter, control.filter, incrementalFilter), control.emit, control.order);
    }
    else if (endpoint.simplePageControl() && endpoint.supported(PagedResultsControl.OID)) {
      return new Paginated(endpoint, type, base, scope, control.limit, searchFilter(endpoint, type, entryFilter, nativeFilter, control.filter, incrementalFilter), control.emit, control.order);
    }
    // if nothing fits fall back to a simple result set search
    else {
      return new Simple(endpoint, type, base, scope, control.limit, searchFilter(endpoint, type, entryFilter, nativeFilter, control.filter, incrementalFilter), control.emit, control.order);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simple
  /**
   ** Factory method to create a simple search strategy for entries in a
   ** Directory Service usually applied on the <code>ChnageLog</code>.
   **
   ** @param  endpoint           the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to search the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  option             the options that affect the way this operation
   **                            is run.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   ** @param  filter             the filter criteria to apply on the search.
   **                            <br>
   **                            Allowed object is {@link DirectoryFilter}.
   ** @param  emit               the identifiers of the attributes to return
   **                            along with the entry. If <code>null</code>,
   **                            return all attributes. If empty return no
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    an instance of <code>Paginated</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectorySearch</code>.
   **
   ** @throws SystemException   if the appropriate Request Controls cannot be
   **                           initialized.
   */
  static DirectorySearch simple(final DirectoryEndpoint endpoint, final int limit, final String filter, final Set<String> emit)
    throws SystemException {

    return new Simple(endpoint, DirectorySchema.ANY, DirectoryName.build(endpoint.changeLogContainer()), SearchControls.ONELEVEL_SCOPE, limit, filter, emit, null);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   strategy
  /**
   ** Creates a <code>DirectorySearch</code> that provides the capabilities to
   ** control a LDAP search.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to search the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  base               the base DN to search from
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  scope              the search scope that control the search.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  limit              the size limit of the result set to be
   **                            returned in a batch.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  filter             the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  order              the keys the result set will be ordered at
   **                            server side.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  static DirectorySearch strategy(final DirectoryEndpoint context, final ObjectClass type, final DirectoryName base, final int scope, final int limit, final String filter, final Set<String> emit, final Set<String> order)
    throws SystemException {
    if (context.virtualListControl() && context.supported(VirtualListViewControl.OID)) {
      return new VirtualList(context, type, base, scope, limit, filter, emit, order);
    }
    else if (context.simplePageControl() && context.supported(PagedResultsControl.OID)) {
      return new Paginated(context, type, base, scope, limit, filter, emit, order);
    }
    // if nothing fits fall back to a simple result set search
    else {
      return new Simple(context, type, base, scope, limit, filter, emit, order);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void execute(final ResultsHandler handler)
    throws SystemException {

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // connect to the Directory Service
      this.endpoint.connect(this.endpoint.base());
      do {
        final List<SearchResult> result = next();
        for (SearchResult entry : result) {
          final ConnectorObject object = connectorObject(entry);
          if (!handler.handle(object))
            break;
        }
      } while (hasMore());
    }
    finally {
      // relase the connection
      this.endpoint.disconnect();
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** @return                    a <code>List</code> of representing
   **                            the object that satisfy the filter criteria.
   **                            <br>
   **                            Never <code>null</code>.
   **                            <br>
   **                            Possible object is {@link List}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public List<SearchResult> list()
    throws SystemException {

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    final List<SearchResult> result = new ArrayList<SearchResult>();
    try {
      // connect to the Directory Service
      this.endpoint.connect(this.endpoint.base());
      do {
          result.addAll(next());
      } while (hasMore());
    }
    finally {
      // relase the connection
      this.endpoint.disconnect();
      trace(method, Loggable.METHOD_EXIT);
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasMore
  /**
   ** Extract the paging controls from the {@link Control}s returned from the
   ** LDAP server after a search operation was performed.
   **
   ** @return                    <code>true</code> if EOF is not reached by the
   **                            server side result set; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if it's not possible t determine the state of
   **                            of server side result set by parsing the
   **                            response controls returned by the LDAP service.
   */
  public abstract boolean hasMore()
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next
  /**
   ** Positions the LDAP context for the next batch.
   **
   ** @return                    a collection of <code>SearchResult</code>s
   **                            rpresenting the objects that satisfies the
   **                            filter criteria.
   **                            <br>
   **                            Never <code>null</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link SearchResult}.
   **
   ** @throws SystemException    in case the search operation cannot be
   **                            performed.
   */
   public List<SearchResult> next()
    throws SystemException {

    try {
      // apply the evaluated request controls
      this.endpoint.unwrap().setRequestControls(this.request);
      // perform the next batch
      return search(this.base, this.filter, this.control);
    }
    catch (NamingException e) {
      throw SystemException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches in the specified context or object for entries that satisfy the
   ** given search filter. Performs the search as specified by the search
   ** controls.
   ** <p>
   ** The format and interpretation of <code>filter</code> fullows RFC 2254 with
   ** the fullowing interpretations for <code>attr</code> and <code>value</code>
   ** mentioned in the RFC.
   ** <p>
   ** <code>attr</code> is the attribute's identifier.
   ** <p>
   ** <code>value</code> is the string representation the attribute's value.
   ** The translation of this string representation into the attribute's value
   ** is directory-specific.
   ** <p>
   ** For the assertion "someCount=127", for example, <code>attr</code>
   ** is "someCount" and <code>value</code> is "127". The provider determines,
   ** based on the attribute ID ("someCount") (and possibly its schema), that
   ** the attribute's value is an integer. It then parses the string "127"
   ** appropriately.
   ** <p>
   ** Any non-ASCII characters in the filter string should be represented by the
   ** appropriate Java (Unicode) characters, and not encoded as UTF-8 octets.
   ** Alternately, the "backslash-hexcode" notation described in RFC 2254 may be
   ** used.
   ** <p>
   ** If the directory does not support a string representation of some or all
   ** of its attributes, the form of <code>search</code> that accepts filter
   ** arguments in the form of Objects can be used instead. The service provider
   ** for such a directory would then translate the filter arguments to its
   ** service-specific representation for filter evaluation.
   ** See <code>search(Name, String, Object[], SearchControls)</code>.
   ** <p>
   ** RFC 2254 defines certain operators for the filter, including substring
   ** matches, equality, approximate match, greater than, less than.  These
   ** operators are mapped to operators with corresponding semantics in the
   ** underlying directory. For example, for the equals operator, suppose
   ** the directory has a matching rule defining "equality" of the
   ** attributes in the filter. This rule would be used for checking
   ** equality of the attributes specified in the filter with the attributes
   ** of objects in the directory. Similarly, if the directory has a
   ** matching rule for ordering, this rule would be used for
   ** making "greater than" and "less than" comparisons.
   ** <p>
   ** Not all of the operators defined in RFC 2254 are applicable to all
   ** attributes. When an operator is not applicable, the exception
   ** <code>InvalidSearchFilterException</code> is thrown.
   ** <p>
   ** The result is returned in an enumeration of <code>SearchResult</code>s.
   ** Each <code>SearchResult</code> contains the name of the object and other
   ** information about the object (see SearchResult). The name is either
   ** relative to the target context of the search (which is named by the
   ** <code>name</code> parameter), or it is a URL string. If the target context
   ** is included in the enumeration (as is possible when <code>controls</code>
   ** specifies a search scope of <code>SearchControls.OBJECT_SCOPE</code> or
   ** <code>SearchControls.SUBSTREE_SCOPE</code>), its name is the empty string.
   ** The <code>SearchResult</code> may also contain attributes of the matching
   ** object if the <code>controls</code> argument specified that attributes be
   ** returned.
   ** <p>
   ** If the object does not have a requested attribute, that nonexistent
   ** attribute will be ignored. Those requested attributes that the object does
   ** have will be returned.
   ** <p>
   ** A directory might return more attributes than were requested (see
   ** <strong>Attribute Type Names</strong> in the class description) but is not
   ** allowed to return arbitrary, unrelated attributes.
   ** <p>
   ** See also <strong>Operational Attributes</strong> in the class description.
   **
   ** @param  searchBase         the base DN to search from
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  searchFilter       the filter expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  searchControls     the search controls that control the search. If
   **                            <code>null</code>, the default search controls
   **                            are used (equivalent to
   **                            <code>(new SearchControls())</code>).
   **                            <br>
   **                            Allowed object is {@link SearchControls}.
   **
   ** @return                    the {@link List} of <code>SearchResult</code>s of
   **                            the objects that satisfy the filter; never
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link SearchResult}.
   **
   ** @throws SystemException    in case the search operation cannot be
   **                            performed.
   */
  public List<SearchResult> search(final DirectoryName searchBase, final String searchFilter, final SearchControls searchControls)
    throws SystemException {

    // seems to be we are ok; transform the SearchResult contained in the
    // NamingEnumeration in a list of strings to return this list to the caller
    final List<SearchResult> result = new ArrayList<SearchResult>();
    try {
      // The NamingEnumeration that results from context.search() using
      // SearchControls contains elements of objects from the subtree (including
      // the named context) that satisfy the search filter specified in
      // context.search().
      // The names of elements in the NamingEnumeration are either relative to the
      // named context or is a URL string.
      // If the named context satisfies the search filter, it is included in the
      // enumeration with the empty string as its name.
      final NamingEnumeration<SearchResult> names = this.endpoint.unwrap().search(searchBase, searchFilter, searchControls);
      while(names.hasMoreElements())
        result.add(names.nextElement());
      DirectoryEndpoint.close(names);
    }
    // this exception is thrown when an attempt is made to add or modify an
    // attribute set that has been specified incompletely or incorrectly.
    // This could happen, for example, when attempting to add or modify a
    // binding, or to create a new subcontext without specifying all the
    // mandatory attributes required for creation of the object.
    // Another situation in which this exception is thrown is by specification
    // of incompatible attributes within the same attribute set, or attributes
    // in conflict with that specified by the object's schema. 
    catch (InvalidAttributesException e) {
      throw DirectoryException.attributeInvalidData(e, searchFilter);
    }
    // this exception is thrown when an attempt is made to add to create an
    // attribute with an invalid attribute identifier.
    // the validity of an attribute identifier is directory-specific. 
    catch (InvalidAttributeIdentifierException e) {
      throw DirectoryException.attributeInvalidType(e, searchFilter);
    }
    catch (NamingException e) {
      throw SystemException.abort(e.getLocalizedMessage());
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortBy
  /**
   ** Transform the sort rule to {@link SortKey}s.
   ** <p>
   ** The transformation accepts the order how the attribute will be placed in
   ** the resulting {@link SortControl} by prefixing the attribute name either
   ** by <code>+</code> or <code>-</code>. A <code>+</code> in this context
   ** means ascending order is requested. A <code>-</code> on the opposite means
   ** descending order.
   **
   ** @param  sort               an array of string specifying the oredering of
   **                            the result set.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    an array of {@link SortKey}s transformed from
   **                            the passed sort string by applying the rules.
   **                            <br>
   **                            Possible object is array of {@link SortKey}.
   */
   protected SortKey[] sortBy(final Set<String> sort) {
    final SortKey[] key = new SortKey[sort.size()];
    int i = 0;
    for (String cursor : sort) {
      final char c = cursor.charAt(0);
      if (c == '-')
        key[i++] = new SortKey(cursor.substring(1), false, null);
      else if (c == '+')
        key[i++] = new SortKey(cursor.substring(1), true, null);
      else
        key[i++] = new SortKey(cursor);
    }
    return key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Directory Service and wrapped in the
   ** specified {@link SearchResult} <code>searchResult</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  result             the collection of {@link SearchResult}s
   **                            providing the data.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link SearchResult}.
   **
   ** @return                    the data transfer object to pass back.
   **                            <br>
   **                            Possible object is array of
   **                            {@link ConnectorObject}.
   **
   ** @throws SystemException    if the {@link ConnectorObject} could not build
   **                            from the given {@link SearchResult}
   **                            <code>result</code>.
   */
  protected ConnectorObject connectorObject(final SearchResult result)
    throws SystemException {

    final ConnectorObjectBuilder builder    = new ConnectorObjectBuilder().setObjectClass(this.type);
    final Attributes             properties = result.getAttributes();
    // verify that we have a valid result set
    if (properties.size() == 0 || this.emit.size() == 0) {
      return builder.build();
    }
    try {
      // always add the unique identifier to the connector object
      builder.setUid(createIdentifier(result));
      // always add the public identifier to the connector object depending
      builder.setName(result.getNameInNamespace());
      for (String cursor : this.emit) {
        // skip naming attribute
        if (Uid.NAME.equals(cursor) || Name.NAME.equals(cursor))
          continue;

        final Attribute attribute = properties.get(cursor);
        // if the requested attribute isn't in the search result omit this value
        if (attribute != null) {
          final AttributeBuilder value = new AttributeBuilder().setName(cursor);
          for (int i = 0; i < attribute.size(); i++) {
            value.addValue(attribute.get(i));
          }
          builder.addAttribute(value.build());
        }
      }
      if (this.type.equals(ObjectClass.ACCOUNT)) {
        if (!this.groups.isEmpty() && this.permissionCache.isEmpty()) {
          final Set<String> proxyCandidate = proxyCandidate(this.endpoint.proxyOrganizationDN());
          final String      memberOfExp    = DirectoryEntry.composeFilter(this.endpoint.distinguishedNameAttr(), proxyCandidate);
          final OperationOptionsBuilder factory = new OperationOptionsBuilder();
          factory.setAttributesToGet(this.endpoint().groupMember());
          factory.setContainer(new QualifiedUid(ObjectClass.GROUP, new Uid(DirectoryEntry.removeRootContext(this.endpoint().homeOrganizationDN(), this.endpoint.rootContext()))));
          
          for (ObjectClass group : this.groups) {
            if (group.equals(ObjectClass.GROUP)) {
              final DirectoryFilter groupFilter = DirectoryFilter.not(DirectoryFilter.or(memberOfExp));
              this.permissionCache.put(group, DirectorySearch.build(this.endpoint, group, factory.build(), groupFilter).list());
            }
            else if (group.equals(DirectorySchema.PROXY)) {
              final DirectoryFilter proxyFilter = DirectoryFilter.or(memberOfExp);
              this.permissionCache.put(group, DirectorySearch.build(this.endpoint, group, factory.build(), proxyFilter).list());
            }
            else {
              this.permissionCache.put(group, DirectorySearch.build(this.endpoint, group, factory.build(), null).list());
            }
          }
        }
        final Attribute attributeMemberOf = properties.get(this.endpoint().groupMemberOf());
        if (attributeMemberOf != null) {
          for (ObjectClass group : this.groups) {
            builder.addAttribute(group.getObjectClassValue(), assignPermission(group, attributeMemberOf));
          }
        }
      }
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    return builder.build();
  } 
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyCandidate
  /**
   ** Get each member attribute from the subentries of the provided root
   ** organization dn entry.
   ** <p>
   ** This method can be use to identify group vs proxy organization. This
   ** connector operating on an openldap. Unfortunatly this Directory Server
   ** does not check the consistency between DN members in its attibute. To
   ** validate if the returned DN is a proxy organization, the existence of
   ** returned DN must be checked!
   **
   ** @param  rootOrganizationEntry the DN that contains the root organization. 
   **                               Must <b>not</b> be <code>null</code>.
   **                               <br>
   **                               Allowed object is {@link String}.
   **                            
   ** @return                       the <code>Set</code> of DNs candidate to be
   **                               a proxy organization.
   **                               <br>
   **                               If nothing is found, it returns an empty
   **                               Set.
   **                               <br>
   **                               Possible object is {@link Set}.
   ** @throws SystemException       if the appropriate Request Controls cannot be
   **                               initialized.
   */
  public Set<String> proxyCandidate(final String rootOrganizationEntry)
    throws SystemException{
    final String method = "proxyCandidate";
    trace(method, Loggable.METHOD_ENTRY);
    
    if (rootOrganizationEntry == null)
      SystemException.propertyRequired(ConnectorBundle.string(Connector.Feature.HOME_ORGANIZATION_DN_LABEL));
    
    final Set<String>             candidates    = new HashSet<String>();
    final String                  searchBase    = DirectoryEntry.removeRootContext(rootOrganizationEntry, this.endpoint.rootContext());
    final OperationOptionsBuilder opBuilder     = new OperationOptionsBuilder().setAttributesToGet(this.endpoint.groupMember())
                                                                               .setContainer(new QualifiedUid(ObjectClass.GROUP, new Uid(searchBase)));
    final DirectorySearch         search        = DirectorySearch.build(this.endpoint, ObjectClass.GROUP, opBuilder.build(), null);
    
    final List<SearchResult>      result        = search.list();
    try {
      for (SearchResult entry : result) {
        final javax.naming.directory.Attribute attribute =  entry.getAttributes().get(this.endpoint.groupMember());
        if (attribute == null)
          continue;
        for (int i = 0; i < attribute.size(); i++) {
          candidates.add((String)attribute.get(i));
        }
      }
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return candidates;
  }
  
  public List<EmbeddedObject> assignPermission(final ObjectClass type, final Attribute attribute)
    throws NamingException {
    final List<EmbeddedObject> permission  = new ArrayList<EmbeddedObject>();
    final List<SearchResult>   groupResult = this.permissionCache.get(type);
    
    for (int i = 0; i < attribute.size(); i++) {
      for (SearchResult entry : groupResult) {
        if (entry.getNameInNamespace().equals(attribute.get(i))) {
          permission.add(new EmbeddedObjectBuilder().setObjectClass(type).addAttribute(Uid.NAME, entry.getNameInNamespace()).addAttribute(Name.NAME, entry.getNameInNamespace()).build());
         }
       }
    }
    return permission;
  }
  

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  private Uid createIdentifier(final SearchResult result)
    throws SystemException {

    if (this.endpoint.distinguishedName().contains(this.entryIdentifier)) {
      return new Uid(result.getNameInNamespace());
    }
    else {
     return createIdentifier(this.entryIdentifier, result.getAttributes());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  private Uid createIdentifier(final String entryIdentifier, final Attributes attributes)
    throws SystemException {

    String value = "";
    if (this.endpoint.binary().contains(entryIdentifier)) {
//      value = LdapUtil.getHexStringByteArrayAttrValue(attributes, entryIdentifier, conn);
    }
    else {
      value = DirectoryEntry.value(attributes, entryIdentifier);
    }
    return (value == null) ? null : new Uid(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetRequestControl
  /**
   ** This method restes all RequestControl in the context.
   **
   ** @param  context            the {@link LdapContext} the {@link Control}s
   **                            will be assigned to.
   **                            <br>
   **                            Allowed object is {@link LdapContext}.
   **
   ** @throws SystemException    if the passed context is <code>null</code> or
   **                            if an error was encountered while encoding the
   **                            supplied arguments into a control or if an
   **                            error occurred while setting the request
   **                            control.
   */
  public void resetRequestControl(final LdapContext context)
    throws SystemException {

    // prevent bogus input
    if (context == null)
      throw SystemException.argumentNull("context");

    try {
      // reset the request controls in the context
      context.setRequestControls(null);
    }
    catch (NamingException e) {
      // throw a wrapped unhandled exception
      throw SystemException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchFilter
  /**
   ** Creates a search filter which will filter to a given {@link ObjectClass}.
   ** <br>
   ** It will be composed of an optional filter to be applied before the object
   ** class filters, the filters for all LDAP object classes for the given
   ** {@link ObjectClass}, and an optional filter to be applied before the
   ** object class filters.
   */
  private static String searchFilter(final DirectoryEndpoint endpoint, final ObjectClass type, String... optionalFilters)
    throws SystemException {

    String        filter   = objectClassFilter(endpoint, type);
    StringBuilder builder  = new StringBuilder();
    int           nonBlank = StringUtility.blank(filter) ? 0 : 1;
    for (String optionalFilter : optionalFilters) {
      nonBlank += (StringUtility.blank(optionalFilter) ? 0 : 1);
    }
    if (nonBlank > 1) {
      builder.append("(&");
    }
    appendFilter(filter, builder);
    for (String optionalFilter : optionalFilters) {
      appendFilter(optionalFilter, builder);
    }
    if (nonBlank > 1) {
      builder.append(')');
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassFilter
  private static String objectClassFilter(final DirectoryEndpoint endpoint, final ObjectClass type)
    throws SystemException {
    
    final Set<String>   classes = endpoint.objectClass(type);
    final StringBuilder builder = new StringBuilder();
    boolean and = classes.size() > 1;
    if (and) {
      builder.append("(&");
    }
    for (String clazz : classes) {
      builder.append("(objectClass=").append(clazz).append(')');
    }
    if (and) {
      builder.append(')');
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appendFilter
  private static void appendFilter(final String filter, final StringBuilder collector) {
    if (!StringUtility.blank(filter)) {
      String  trimmed = filter.trim();
      boolean enclose = filter.charAt(0) != '(';
      if (enclose) {
        collector.append('(');
      }
      collector.append(trimmed);
      if (enclose) {
        collector.append(')');
      }
    }
  }
}