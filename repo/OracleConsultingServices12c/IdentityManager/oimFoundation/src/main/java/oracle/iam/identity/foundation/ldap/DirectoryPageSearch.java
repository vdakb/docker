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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryPageSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryPageSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
    1.0.0.0.1    2013-18-01  DSteding    Fixed DE-000068
                                         Abstract class DirectorySearch is not
                                         visible outside of the package.
*/

package oracle.iam.identity.foundation.ldap;

import java.io.IOException;

import javax.naming.NamingException;

import javax.naming.directory.SearchControls;

import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.SortControl;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryPageSearch
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DirectoryPageSearch</code> provides the capabilities to control a
 ** LDAP query based on the Paged Result Request Control.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0.1
 ** @since   0.0.0.2
 */
public class DirectoryPageSearch extends DirectorySearch {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
 /**
   ** Creates a <code>DirectoryPageSearch</code> that provides the capabilities
   ** to control the LDAP query base on the {@link PagedResultsControl}.
   **
   ** @param  context             the {@link LdapContext} to perform the search.
   ** @param  base                the base DN to search from
   ** @param  filter              the filter expression to use for the search;
   **                             must not be null, e.g.
   **                             "(&amp;(objectclass=*)(cn=abraham))"
   ** @param  order               the keys the result set will be ordered.
   ** @param  search              the search controls that control the search. If
   **                             <code>null</code>, the default search controls
   **                             are used (equivalent to
   **                             <code>(new SearchControls())</code>).
   ** @param  batch               the size of a batch to be returned.
   **
   ** @throws DirectoryException  if the appropriate Request Controls cannot be
   **                             initialized.
   */
  public DirectoryPageSearch(final LdapContext context, final String base, final String filter, final String[] order, final SearchControls search, final int batch)
    throws DirectoryException {

    // ensure inheritance
    super(context, base, filter, order, search, batch);

    // initialize instance
    this.request  = new Control[this.order != null && this.order.length > 0 ? 2 : 1];
    try {
      // request a paginated result from the Directory Service
      // each loop iteration will re-activate paged results
      this.request[0] = new PagedResultsControl(this.batch, this.id, Control.CRITICAL);
      if (this.order != null && this.order.length > 0)
        // request a result set that is sorted by the specified values
        // the request control is created with the same criticality as the
        // result set. If the LDAP server doesn't support the control it will
        // fail afterwards
        this.request[1] = new SortControl(sortBy(this.order), Control.CRITICAL);
    }
    catch (IOException e) {
      throw new DirectoryException(DirectoryError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasMore (DirectorySearch)
  /**
   ** Extract the paging controls from the {@link Control}s returned from the
   ** LDAP server after a search operation was performed.
   **
   ** @return                    <code>true</code> if EOF is not reached by the
   **                            server side result set; otherwise
   **                            <code>false</code>.
   **
   ** @throws DirectoryException if it's not possible t determine the state of
   **                            of server side result set by parsing the
   **                            response controls returned by the LDAP service.
   */
  @Override
  public boolean hasMore()
    throws DirectoryException {

    try {
      final Control[] response = this.context.getResponseControls();
      for (Control control : response) {
        if (PagedResultsResponseControl.OID.equals(control.getID())) {
          this.id         = ((PagedResultsResponseControl)control).getCookie();
          // request a paginated result from the Directory Service
          // each loop iteration will re-activate paged results
          this.request[0] = new PagedResultsControl(this.batch, this.id, Control.CRITICAL);
          break;
        }
      }
    }
    catch (NamingException e) {
      throw new DirectoryException(DirectoryError.GENERAL, e);
    }
    catch (IOException e) {
      throw new DirectoryException(DirectoryError.UNHANDLED, e);
    }
    return this.id != null && this.id.length != 0;
  }
}