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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   LDAPSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDAPSearch.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;

import javax.naming.ldap.Control;
import javax.naming.ldap.SortKey;
import javax.naming.ldap.SortControl;
import javax.naming.ldap.LdapContext;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.service.DirectoryException;
import oracle.jdeveloper.connection.iam.service.DirectoryService;
import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class LDAPSearch
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** <code>LDAPSearch</code> provides the capabilities to control a LDAP search.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public abstract class LDAPSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  public static final String       SCOPE_OBJECT      = "object";
  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  public static final String       SCOPE_ONELEVEL    = "one";
  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  public static final String       SCOPE_SUBTREE     = "sub";

  private static final Map<String, Integer> control  = new HashMap<String, Integer>(3);

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    control.put(SCOPE_OBJECT,   new Integer(SearchControls.OBJECT_SCOPE));
    control.put(SCOPE_ONELEVEL, new Integer(SearchControls.ONELEVEL_SCOPE));
    control.put(SCOPE_SUBTREE,  new Integer(SearchControls.SUBTREE_SCOPE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // instane attributes
  //////////////////////////////////////////////////////////////////////////////

  protected byte[]                   id;
  protected Control[]                request;
  protected SearchControls           search;

  protected final LdapContext        context;
  protected final String             base;
  protected final String             filter;
  protected final String[]           order;
  protected final int                batch;

  protected final Collection<String> binaries  = CollectionUtility.caseInsensitiveSet();
  protected final Collection<String> excludes  = CollectionUtility.caseInsensitiveSet();
  protected final Collection<String> includes  = CollectionUtility.caseInsensitiveSet();

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
   **                            <br>
   **                            Allowed object is {@link LdapContext}.
   ** @param  base               the base DN to search from
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  filter             the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp; (objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  order              the keys the result set will be ordered.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  search             the search controls that control the search. If
   **                            <code>null</code>, the default search controls
   **                            are used (equivalent to
   **                            <code>(new SearchControls())</code>).
   **                            <br>
   **                            Allowed object is {@link SearchControls}.
   ** @param  batch             the size of a batch to be returned.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public LDAPSearch(final LdapContext context, final String base, final String filter, final String[] order, final SearchControls search, final int batch) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.id      = StringUtility.EMPTY.getBytes();
    this.context = context;
    this.base    = base;
    this.filter  = filter;
    this.order   = order;
    this.search  = search;
    this.batch   = batch;

    // ensure that the objectClass is always part of the includes
    this.includes.add(DirectoryService.OBJECTCLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   control
  /**
   ** Returns the predefined search scope control for the given name.
   **
   ** @param  searchScope        the scope of search to obtain for the
   **                            predefined objects.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the predefined search scope for the given name.
   **                            <br>
   **                            Possible object is {@link SearchControls}.
   */
  public static final SearchControls control(final String searchScope) {
    final Integer ccode = control.get(searchScope);
    return control(ccode.intValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   control
  /**
   ** Returns the predefined search scope for the given enum.
   **
   ** @param  scope              the scope of search to obtain for the
   **                            predefined objects.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the predefined search scope for the given name.
   **                            <br>
   **                            Possible object is {@link SearchControls}.
   */
  public static final SearchControls control(final int scope) {
    return new SearchControls(scope, 0, 0, null, false, false);
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
   ** @throws DirectoryException if it's not possible t determine the state of
   **                            of server side result set by parsing the
   **                            response controls returned by the LDAP service.
   */
  public abstract boolean hasMore()
    throws DirectoryException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next
  /**
   ** Positions the LDAP context for the next batch
   **
   ** @return                    an enumeration of <code>SearchResult</code>s of
   **                            the objects that satisfy the filter; never
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link NamingEnumeration}
   **                            where each element is of type
   **                            {@link SearchResult}.
   **
   ** @throws DirectoryException if the operation fails
   */
   public NamingEnumeration<SearchResult> next()
    throws DirectoryException {

    try {
      // set the request controls in the context
      this.context.setRequestControls(this.request);
      // perform the next batch
      return this.context.search(this.base, this.filter, this.search);
    }
    catch (NamingException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, e.getLocalizedMessage(), e));
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
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    an array of {@link SortKey}s transformed from
   **                            the passed sort string by applying the rules.
   **                            <br>
   **                            Possible object is array of {@link SortKey}.
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