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

    File        :   SearchInstance.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SearchInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.spi.instance;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import javax.naming.directory.SearchControls;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.directory.common.FeatureConstant;

////////////////////////////////////////////////////////////////////////////////
// class SearchInstance
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>SearchInstance</code> represents a data instance wrapper a
 ** {@link ExportInstance} use to apply searches on a Directory Information Tree
 ** (DIT) and spool the results of the search to a file.
 ** <p>
 ** Subclasses of this classes providing the data model an implementation of
 ** {@link ExportInstance} needs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SearchInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Map<String, Integer> control  = new HashMap<String, Integer>(3);

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    control.put(FeatureConstant.SEARCH_SCOPE_OBJECT,   new Integer(SearchControls.OBJECT_SCOPE));
    control.put(FeatureConstant.SEARCH_SCOPE_ONELEVEL, new Integer(SearchControls.ONELEVEL_SCOPE));
    control.put(FeatureConstant.SEARCH_SCOPE_SUBTREE,  new Integer(SearchControls.SUBTREE_SCOPE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                     base;
  private String                     filter;
  private String                     scope     = FeatureConstant.SEARCH_SCOPE_SUBTREE;

  protected final Collection<String> binaries  = CollectionUtility.caseInsensitiveSet();
  protected final Collection<String> excludes  = CollectionUtility.caseInsensitiveSet();
  protected final Collection<String> includes  = CollectionUtility.caseInsensitiveSet();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SearchInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SearchInstance() {
    // ensure inheritance
    super();

    // ensure that the objectClass is always part of the includes
    this.includes.add(FeatureConstant.OBJECT_CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   base
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>base</code>.
   **
   ** @param  base             the name of the search base where the export has
   **                          to obtain the entries from,
   **                          e.g. "dc=vm,dc=oracle,dc=com".
   */
  public void base(final String base) {
    this.base = base;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   base
  /**
   ** Returns the name of the search base where the export has to obtain the
   ** entries from.
   **
   ** @return                  the name of the search base where the export has
   **                          to obtain the entries from,
   **                          e.g. "dc=vm,dc=oracle,dc=com".
   */
  public final String base() {
    return this.base;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>filter</code>.
   **
   ** @param  filter             the expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp; (objectclass=*)(cn=abraham))"
   */
  public void filter(final String filter) {
    this.filter = filter;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the filter expression to apply on the search.
   **
   ** @return                    the filter expression to use for the search;
   **                            e.g. "(&amp; (objectclass=*)(cn=abraham))"
   */
  public final String filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scope
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>scope</code>.
   **
   ** @param  scope        the search scope.
   */
  public void scope(final String scope) {
    this.scope = scope;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   scope
  /**
   ** Returns the search scope of the search.
   **
   ** @return                    the search scope.
   */
  public final String scope() {
    return this.scope;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   control
  /**
   ** Returns the search scope of the search.
   **
   ** @return                    the search scope.
   */
  public final SearchControls control() {
    return control(this.scope);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   binaries
  /**
   ** Returns the {@link Collection} for attributes that are treated as binary
   ** and their content will be provided as base64 encoded.
   **
   ** @return                    the {@link Collection} for attributes that are
   **                            treated as binary and their content will be
   **                            provided as base64 encoded.
   */
  public final Collection<String> binaries() {
    return this.binaries;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   excludes
  /**
   ** Returns the {@link Collection} for attributes that are excluded during
   ** spool out.
   **
   ** @return                    the {@link Collection} for attributes that
   **                            excluded during spool out.
   */
  public final Collection<String> excludes() {
    return this.excludes;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   includes
  /**
   ** Returns the {@link Collection} for attributes that are included during
   ** spool out.
   **
   ** @return                    the {@link Collection} for attributes that
   **                            included during spool out.
   */
  public final Collection<String> includes() {
    return this.includes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerBinary
  /**
   ** Register a {@link Collection} of attribute names where the content will be
   ** provided as base64 encoded.
   **
   ** @param  collection         the {@link Collection} of attribute names where
   **                            the content will be provided as base64 encoded
   **                            to register.
   */
  public void registerBinaries(final Collection<String> collection) {
    for (String cursor : collection)
      registerBinaries(cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerBinaries
  /**
   ** Register an attribute name where the content will be provided as base64
   ** encoded.
   **
   ** @param  name               the attribute name where the content will be
   **                            provided as base64 encoded to register.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerBinaries(final String name) {
    // prevent bogus state
    if (this.binaries.contains(name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, name));

    return this.binaries.add(name.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterBinaries
  /**
   ** Unregister an attribute name where the content will be provided as base64
   ** encoded
   **
   ** @param  name               the attribute name where the content will be
   **                            provided as base64 encoded to unregister.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            unregistered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean unregisterBinaries(final String name) {
    return this.binaries.remove(name.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerExcludes
  /**
   ** Register a {@link Collection} of attribute names where the content will be
   ** skipped during parse.
   **
   ** @param  collection         the {@link Collection} of attribute names that
   **                            has to be skipped.
   */
  public void registerExcludes(final Collection<String> collection) {
    for (String cursor : collection)
      registerExcludes(cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerExcludes
  /**
   ** Register an attribute name where the content will be skipped during parse.
   **
   ** @param  name               the attribute name that has to be skipped.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            unregistered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerExcludes(final String name) {
    // prevent bogus state
    if (this.excludes.contains(name.trim()))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, name));

    if (this.includes.contains(name.trim()))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, "Encludes", name));

    return this.excludes.add(name.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterExcludes
  /**
   ** Unregister an attribute name where the content will no longer be skipped
   ** during parse.
   **
   ** @param  name               the attribute name that has no longer to be
   **                            skipped.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            unregistered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean unregisterExcludes(final String name) {
    return this.excludes.remove(name.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerIncludes
  /**
   ** Register a {@link Collection} of attribute names where the content will be
   ** fetched during parse.
   **
   ** @param  collection         the {@link Collection} of attribute names that
   **                            has to be fetched during parse.
   */
  public void registerIncludes(final Collection<String> collection) {
    for (String cursor : collection)
      registerIncludes(cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerIncludes
  /**
   ** Register an attribute name where the content will be fetched during parse.
   **
   ** @param  name               the attribute name that has to be fetched.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerIncludes(final String name) {
    if (this.includes.contains(name.trim()))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, name));

    if (this.excludes != null && this.excludes.contains(name.trim()))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, "Includes", name));

    return this.includes.add(name.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterIncludes
  /**
   ** Unregister an attribute name where the content will no longer be fetched
   ** during parse.
   **
   ** @param  name               the attribute name that has no longer to be
   **                            fetched.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            unregistered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean unregisterIncludes(final String name) {
    return this.includes.remove(name.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   control
  /**
   ** Returns the predefined search scope control for the given name.
   **
   ** @param  searchScope        the scope of search to obtain for the
   **                            predefined objects.
   **
   ** @return                    the predefined search scope for the given name.
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
   **
   ** @return                    the predefined search scope for the given name.
   */
  public static final SearchControls control(final int scope) {
    return new SearchControls(scope, 0, 0, null, false, false);
  }
}