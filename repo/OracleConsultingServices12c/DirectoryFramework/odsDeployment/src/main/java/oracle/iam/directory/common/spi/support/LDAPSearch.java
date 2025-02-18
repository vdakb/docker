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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   LDAPSearch.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;

import javax.naming.ldap.Control;
import javax.naming.ldap.SortKey;
import javax.naming.ldap.SortControl;
import javax.naming.ldap.LdapContext;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// abstract class LDAPSearch
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** <code>LDAPSearch</code> provides the capabilities to control a LDAP search.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class LDAPSearch {

  //////////////////////////////////////////////////////////////////////////////
  // instane attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final LdapContext context;
  protected final String      base;
  protected final String      filter;
  protected final String[]    order;
  protected final int         batch;

  protected byte[]            id;
  protected Control[]         request;
  protected SearchControls    search;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>LDAPSearch</code> that provides the capabilities to
   ** control a LDAP search.
   **
   ** @param  context            the {@link LdapContext} to perform the search.
   ** @param  base               the base DN to search from
   ** @param  filter             the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp; (objectclass=*)(cn=abraham))"
   ** @param  order             the keys the result set will be ordered.
   ** @param  search            the search controls that control the search. If
   **                           <code>null</code>, the default search controls
   **                           are used (equivalent to
   **                           <code>(new SearchControls())</code>).
   ** @param  batch             the size of a batch to be returned.
   */
  public LDAPSearch(final LdapContext context, final String base, final String filter, final String[] order, final SearchControls search, final int batch) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.id      = SystemConstant.EMPTY.getBytes();
    this.context = context;
    this.base    = base;
    this.filter  = filter;
    this.order   = order;
    this.search  = search;
    this.batch   = batch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasMore
  /**
   ** Extract the paging controls from the {@link Control}s returned from the
   ** LDAP server after a search operation was performed.
   **
   ** @return                    <code>true</code> if EOF is not reached by the
   **                            server side result set; otherwise
   **                            <code>false</code>.
   **
   ** @throws FeatureException   if it's not possible t determine the state of
   **                            of server side result set by parsing the
   **                            response controls returned by the LDAP service.
   */
  public abstract boolean hasMore()
    throws FeatureException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next (DirectorySearchContext)
  /**
   ** Positions the LDAP context for the next batch
   **
   ** @return                    an enumeration of <code>SearchResult</code>s of
   **                            the objects that satisfy the filter; never
   **                            <code>null</code>.
   **
   ** @throws FeatureException   if the operation fails
   */
   public NamingEnumeration<SearchResult> next()
    throws FeatureException {

    try {
      // set the request controls in the context
      this.context.setRequestControls(this.request);
      // perform the next batch
      return this.context.search(this.base, this.filter, this.search);
    }
    catch (NamingException e) {
      throw new FeatureException(FeatureError.GENERAL, e);
    }
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
   **
   ** @return                    an array of {@link SortKey}s transformed from
   **                            the passed sort string by applying the rules.
   */
   protected SortKey[] sortBy(final String[] sort) {
    final SortKey[] key = new SortKey[sort.length];
    for (int i = 0; i < sort.length; i++) {
      final char c = sort[i].charAt(0);
      if (c == '-')
        key[i] = new SortKey(sort[i].substring(1), false, null);
      else if (c == '+')
        key[i] = new SortKey(sort[i].substring(1), true, null);
      else
        key[i] = new SortKey(sort[i]);
    }
    return key;
  }
}