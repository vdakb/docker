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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Enterprise User Security

    File        :   RoleReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2011-03-01  TSebo       First release version
*/

package oracle.iam.identity.eus.service.reconciliation;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;

import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;

import javax.naming.ldap.LdapName;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.AbstractLookup;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;

import oracle.iam.identity.gds.service.reconciliation.TargetReconciliation;

////////////////////////////////////////////////////////////////////////////////
// class RoleReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>RoleReconciliation</code> act as the service end point for the
 ** Oracle Identity Manager to reconcile role memberships from an Enterprise
 ** User Security Realm.
 **
 ** @author  Tomas.Sebo@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class RoleReconciliation extends TargetReconciliation {

  /**
   ** Attribute tag which must be defined on this task to specify which Lookup
   ** Definion is used for list of all Domains
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String ROLE_LOOKUP      = "Role Lookup";

  /**
   ** Attribute based on which will be sorter result set
   */
  protected static final String MEMBER_ATTRIBUTE = "uniquemember";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,      TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,        TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values are OneLevel | SubTree | Object
     */
  , TaskAttribute.build(SEARCHSCOPE,      DirectoryConstant.SCOPE_SUBTREE)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCHFILTER,     SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,      SystemConstant.TRUE)
    /**Name of the Lookup definition where are listed all domains */
  , TaskAttribute.build(ROLE_LOOKUP, TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RoleReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBase (overridden)
  /**
   ** Returns the releative distinguished name of the search base.
   **
   ** @return                    the releative distinguished name of the search
   **                            base.
   */
  @Override
  public final String searchBase() {
    return DirectoryConstant.ENTERPRISE_DOMAIN_CONTAINER_DEFAULT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchScope
  /**
   ** Returns the search scope this task is performing on the LDAP server.
   **
   ** @return                    the search scope this task is performing on the
   **                            LDAP server.
   */
  @Override
  public String searchScope() {
    return DirectoryConstant.SCOPE_SUBTREE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (overridden)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected final TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** Disable all lookup evaluation.
   **
   ** @throws TaskException      if something goes wrong with the build
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (overridden)
  /**
   ** Retriev all users and particular enterprise roles.
   ** <p>
   ** During processing convert a roleDN to roleName.
   **
   ** @throws TaskException      if something goes wrong with the build
   */
  protected void onExecution()
    throws TaskException {

    String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);

    final AbstractLookup            lookup    = new AbstractLookup(this, stringValue(ROLE_LOOKUP));
    final List<SearchResult>        resultSet = connector().search(searchBase(), stringValue(SEARCHFILTER), searchScope(), new String[] { MEMBER_ATTRIBUTE });
    // Get all users with
    final Map<String, List<String>> data      = new HashMap<String, List<String>>();
    for (SearchResult result : resultSet) {
      try {
        final String roleName = transforToRoleName(lookup, result.getNameInNamespace());
        // Convert to the roleName
        final Attribute attribute = result.getAttributes().get(MEMBER_ATTRIBUTE);
        if (attribute != null) {
          for (int i = 0; i < attribute.size(); i++) {
            String userDN = (String)attribute.get(i);
            List<String> roles = data.get(userDN);
            if (roles == null) {
              roles = new ArrayList<String>();
            }
            roles.add(roleName);
            data.put(userDN, roles);
          }
        }
      }
      catch (NamingException e) {
        throw new TaskException(e);
      }
    }

    info("Number of account for which will be events created is: " + data.size());
    // Process all entries one by one
    for (Map.Entry<String, List<String>> entry : data.entrySet()) {
      String userName = entry.getKey();
      List<String> roles = entry.getValue();
      processSubject(userName, roles);
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  // Prepare a data for recon events
  private void processSubject(String userName, final List<String> roles)
    throws TaskException {

    String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    if (connector().isDistinguishedNameRelative())
      userName = normalizePath(userName);
    if (!connector().distinguishedNameCaseSensitive())
      userName = userName.toLowerCase();

    // Create a master data
    Map<String, Object> masterData = new HashMap<String, Object>();
    try {
      LdapName userDN = new LdapName(userName);
      String accountName = (String)userDN.getRdn(userDN.size() - 1).getValue();
      String directoryHierarchy = userDN.getPrefix(userDN.size() - 1).toString();
      masterData.put("Account",             accountName);
      masterData.put("Directory Hierarchy", directoryHierarchy);
      masterData.put("IT Resource",         stringValue(IT_RESOURCE));
    }
    catch (InvalidNameException e) {
      error(method, e.getMessage());
    }
    debug(method, "Master Data: " + masterData);

    // Create child data
    List<Map<String, Object>> roleNames = new ArrayList<Map<String, Object>>();
    for (String role : roles) {
      Map<String, Object> roleName = new HashMap<String, Object>();
      roleName.put("Role Name", role);
      roleNames.add(roleName);
    }
    Map<String, List<Map<String, Object>>> childData = new HashMap<String, List<Map<String, Object>>>();
    childData.put("Enterprise Role", roleNames);

    debug(method, "Child Data: " + childData);

    // Create recon event
    processChangeLogEvent(masterData, childData);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  /**
   * Convert Role DN to the role name
   * @param lookup
   * @param roleDN
   * @return
   */
  private String transforToRoleName(AbstractLookup lookup, String roleDN) {
    if (connector().isDistinguishedNameRelative()) {
      roleDN = normalizePath(roleDN);
    }
    if (connector().distinguishedNameCaseSensitive()) {
      roleDN = roleDN.toLowerCase();
    }
    return lookup.stringValue(roleDN);
  }
}