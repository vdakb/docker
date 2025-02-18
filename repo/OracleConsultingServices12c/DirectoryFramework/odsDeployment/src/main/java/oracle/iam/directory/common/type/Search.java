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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;

import oracle.iam.directory.common.spi.instance.SearchInstance;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** <code>Search</code> wrappes the context to specify a LDAP search.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Search extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final SearchInstance delegate = new SearchInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Search</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Search() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBase
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>base</code>.
   **
   ** @param  base             the name of the search base where the export has
   **                          to obtain the entries from,
   **                          e.g. "dc=vm,dc=oracle,dc=com".
   */
  public void setBase(final String base) {
    this.delegate.base(base);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFilter
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>filter</code>.
   **
   ** @param  filter             the expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp; (objectclass=*)(cn=abraham))"
   */
  public void setFilter(final String filter) {
    this.delegate.filter(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setScope
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>scope</code>.
   **
   ** @param  scope        the search scope.
   */
  public void setScope(final Scope scope) {
    this.delegate.scope(scope.getValue());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link SearchInstance} delegate of Directory Service object to
   ** handle.
   **
   ** @return                    the {@link SearchInstance} delegate.
   */
  public final SearchInstance instance() {
    if (isReference())
      return ((Search)getCheckedRef()).instance();

    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredBinaries
  /**
   ** Called to inject the argument for adding a collection of names where the
   ** content will be provided as base64 encoded.
   **
   ** @param  collection         the collection of names where the content will
   **                            be provided as base64 encoded.
   **
   ** @throws BuildException     if this instance is referencing an already
   **                            added attribute name.
   */
  public void addConfiguredBinaries(final NameCollection collection)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    this.delegate.registerBinaries(collection.names());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredExcluded
  /**
   ** Called to inject the argument for adding a collection of names where the
   ** content will be excluded during parse.
   **
   ** @param  collection         the collection of names where the content will
   **                            be excluded during parse.
   **
   ** @throws BuildException     if this instance is referencing an already
   **                            added attribute name.
   */
  public void addConfiguredExcludes(final NameCollection collection)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    this.delegate.registerExcludes(collection.names());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredIncludes
  /**
   ** Called to inject the argument for adding collection of names where the
   ** content will be included during parse.
   **
   ** @param  collection         the collection of names where the content will
   **                            be exported during parse.
   **
   ** @throws BuildException     if this instance is referencing an already
   **                            added attribute name.
   */
  public void addConfiguredIncludes(final NameCollection collection)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    this.delegate.registerIncludes(collection.names());
  }
}