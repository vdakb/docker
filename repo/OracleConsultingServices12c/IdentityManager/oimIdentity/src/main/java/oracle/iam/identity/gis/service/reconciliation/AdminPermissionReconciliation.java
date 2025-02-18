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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Identity Manager Connector

    File        :   AdminPermissionReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AdminPermissionReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.service.reconciliation;

import java.util.List;

import java.util.Set;

import oracle.iam.platform.authopss.vo.AdminRole;

import oracle.iam.platformservice.api.AdminRoleService;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class AdminPermissionReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AdminPermissionReconciliation</code> acts as the service end point
 ** for the Oracle Identity Manager to reconcile admin roles information from
 ** Identity Manager itself.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class AdminPermissionReconciliation extends LookupReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** type of permissions should be reconciled.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String          GLOBAL_PERMISSION = "Global Permission";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  protected static final TaskAttribute[] attribute         = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,       TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,  TaskAttribute.MANDATORY)
    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Refresh | Update
     */
  , TaskAttribute.build(OPERATION,         TaskAttribute.MANDATORY)
    /** the task attribute to resolve the name of the Lookup Group */
  , TaskAttribute.build(LOOKUPGROUP,       TaskAttribute.MANDATORY)
    /** the task attribute to resolve the encoded value */
  , TaskAttribute.build(ENCODEDVALUE,      TaskAttribute.MANDATORY)
    /** the task attribute to resolve the decoded value */
  , TaskAttribute.build(DECODEDVALUE,      TaskAttribute.MANDATORY)
    /** the task attribute to resolve the type of permissions */
  , TaskAttribute.build(GLOBAL_PERMISSION, TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  public enum Attribute {
    /**
     ** Attribute value which specifies the name of the attribute of the
     ** unique id of an AdminRole.
     */
      ID(RoleManagerConstants.ROLE_KEY),

    /**
     ** Attribute value which specifies the name of the attribute of the
     ** unique name of an AdminRole.
     */
    NAME(RoleManagerConstants.ROLE_NAME),

    /**
     ** Attribute value which specifies the name of the attribute of the
     ** dislplay name of an AdminRole.
     */
    DISPLAY_NAME(RoleManagerConstants.ROLE_DISPLAY_NAME),

    /**
     ** Attribute value which specifies the name of the attribute of the
     ** description of an AdminRole.
     */
    DESCRIPTION(RoleManagerConstants.ROLE_DESCRIPTION)
    ;

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:3773632799545324647")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Attribute</code> that allows use as a JavaBean.
     **
     ** @param  value            the name of the attribute.
     */
    Attribute(final String value) {
      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the name of the attribute.
     **
     ** @return                    the name of the attribute possible object is
     **                            {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper Attribute from the given string value.
     **
     ** @param  value               the name the Attribute should be returned
     **                            for.
     **
     ** @return                    the severity.
     */
    public static Attribute fromValue(final String value) {
      for (Attribute cursor : Attribute.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AdminPermissionReconciliation</code> scheduled task
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AdminPermissionReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateChanges (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation by fetching the
   ** data from the target system.
   **
   ** @param  bulkSize           the size of a block processed in a thread
   ** @param  returning          the attributes whose values have to be returned.
   **                            Set it to <code>null</code>, if all attribute
   **                            values have to be returned
   **
   ** @throws TaskException      if the operation fails
   */
  @Override
  protected void populateChanges(final int bulkSize, final Set<String> returning)
    throws TaskException {

    // check if a request to stop the execution is pending and return without
    // further actions if it evaluates to true
    if (isStopped())
      return;

    final String method = "populateChanges";
    trace(method, SystemMessage.METHOD_ENTRY);
    // create a task timer to gather performance metrics
    timerStart(method);

    // set the current date as the timestamp on which this task was last
    // reconciled at start
    // setting it at this time that we have next time this scheduled task will
    // run the changes made during the execution of this task
    lastReconciled(this.server.systemTime());

    final Batch            batch      = new Batch(batchSize());
    final boolean          globalOnly = booleanValue(GLOBAL_PERMISSION);
    // set up the attribute names to be returned from the search
    final String[]         attribute  = returning.toArray(new String[0]);
    final AdminRoleService facade     = this.targetService.getService(AdminRoleService.class);
    try {
      List<AdminRole> result = null;
      do {
        // check if a request to stop the execution is pending and return without
        // further actions if it evaluates to true
        if (isStopped())
          break;

        info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
        result = facade.getAdminRoles();
        info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
        // if the result set is empty and the request batch is still on start
        // position we have no changes
        if (result.size() == 0 && batch.start() == 1)
          info(TaskBundle.string(TaskMessage.NOTHING_TO_CHANGE));
        else if (result.size() > 0) {
          info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(result.size())));
          // if we should really do reconcile ?
          if (gatherOnly()) {
            info(TaskBundle.string(TaskMessage.RECONCILE_SKIP));
            incrementIgnored(result.size());
          }
          else {
            for (AdminRole role : result) {
              // check if a request to stop the execution is pending and
              // return without further actions if it evaluates to true
              if (isStopped())
                break;

              // merge the permisssion to the correspondend lookup definition
              // only if the properties of the role itself match the required
              // scope
              final boolean scoped = role.isScoped();
              if ((scoped && !globalOnly) || (!scoped && globalOnly))
                mergeLookup(extractValue(Attribute.fromValue(attribute[0]), role), extractValue(Attribute.fromValue(attribute[1]), role));
              else
                incrementIgnored();
            }
            info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
          }
        }
      } while (batch.size() <= result.size());
    }
    finally {
      this.server.disconnect();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extractValue
  /**
   ** Extracts the value specified by {@link Attribute} from the
   ** {@link AdminRole} specified by <code>role</code>.
   **
   ** @param  attribute          the encoded descriptor of the attribute we are
   **                            interrested in.
   ** @param  role               the {@link AdminRole} providing the values.
   **
   ** @return                    the value of the attribute which name match.
   */
  private String extractValue(final Attribute attribute, final AdminRole role) {
    String  entityValue = null;
    switch (attribute) {
      case ID           : entityValue = role.getRoleId().toString();
                          break;
      case NAME         : entityValue = role.getRoleName();
                          break;
      case DISPLAY_NAME : entityValue = role.getRoleDisplayName();
                          break;
      case DESCRIPTION  : entityValue = role.getRoleDescription();
                          break;
    }
    return entityValue;
  }
}