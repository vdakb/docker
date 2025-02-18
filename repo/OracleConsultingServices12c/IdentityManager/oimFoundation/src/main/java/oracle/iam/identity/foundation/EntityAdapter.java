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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Provisioning Facilities

    File        :   EntityAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractEntityAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcNotAtomicProcessException;
import Thor.API.Exceptions.tcProcessNotFoundException;
import Thor.API.Exceptions.tcFormNotFoundException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.Organization;

////////////////////////////////////////////////////////////////////////////////
// abstract class EntityAdapter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Adpapter dedicated to operate on the Oracle Identity Manager objects like
 ** users, organizations and roles
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class EntityAdapter extends GenericAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntityAdapter</code> task adpater that allows
   ** use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public EntityAdapter(final tcDataProvider provider) {
    // ensure inheritance
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntityAdapter</code> task adpater that allows
   ** use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public EntityAdapter(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationName
  /**
   ** Return the name of an organization.
   **
   ** @param  organizationKey    the key for the desired organization name.
   **
   ** @return                    the name of the organization for
   **                            <code>organizationKey</code> or
   **                            <code>null</code> if there is no organization
   **                            with key <code>organizationKey</code>.
   */
  public String organizationName(final String organizationKey) {
    return organizationName(Long.valueOf(organizationKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationName
  /**
   ** Return the name of an organization.
   **
   ** @param  organizationKey    the key for the desired organization name.
   **
   ** @return                    the name of the organization for
   **                            <code>organizationKey</code> or
   **                            <code>null</code> if there is no organization
   **                            with key <code>organizationKey</code>.
   */
  public String organizationName(final Long organizationKey) {
    return organizationValue(organizationKey, Organization.NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationValue
  /**
   ** Return the value of a certain attribute of an organization.
   **
   ** @param  organizationKey    the key for the desired organization attribute.
   ** @param  attributeName      the name of the attribute of an organization to
   **                            return the value for.
   **
   ** @return                    the value of an attribute of an organization
   **                            belonging to <code>organizationKey</code> or
   **                            <code>null</code> if there is no organization
   **                            with key <code>organizationKey</code>.
   */
  public String organizationValue(final String organizationKey, final String attributeName) {
    return organizationValue(Long.valueOf(organizationKey), attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationValue
  /**
   ** Return the value of a certain attribute of an organization.
   **
   ** @param  organizationKey    the key for the desired organization attribute.
   ** @param  attributeName      the name of the attribute of an organization to
   **                            return the value for.
   **
   ** @return                    the value of an attribute of an organization
   **                            belonging to <code>organizationKey</code> or
   **                            <code>null</code> if there is no organization
   **                            with key <code>organizationKey</code>.
   */
  public String organizationValue(final Long organizationKey, final String attributeName) {
    final String method = "organizationValue";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map<String, Object> search = new HashMap<String, Object>();
    search.put(Organization.KEY, organizationKey.toString());

    String result = null;
    try {
      tcResultSet resultSet = organizationFacade().findOrganizations(search);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_ORGANIZATION), organizationKey.toString() };
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      }
      else if (resultSet.getRowCount() > 1) {
        String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_ORGANIZATION), organizationKey.toString() };
        error(method, TaskBundle.format(TaskError.RESOURCE_AMBIGUOUS, parameter));
      }
      else
        result = resultSet.getStringValue(attributeName);
    }
    catch (tcColumnNotFoundException e) {
      error(method, TaskBundle.format(TaskError.COLUMN_NOT_FOUND, Organization.NAME));
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   triggerProcessTasks
  /**
   ** Triggers a specific tasks of other processes.
   **
   ** @param  userName           the user name (global profile).
   ** @param  taskName           the task to generate.
   **
   ** @return                    the response code of the triggered task.
   */
  public String triggerProcessTasks(final String userName, final String taskName) {
    final String method = "triggerProcessTasks";
    trace(method, SystemMessage.METHOD_ENTRY);

    String response = SystemConstant.EMPTY;
    try {
      ;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processInstanceValue
  /**
   ** Return the value of a process form instance field specified by
   ** <code>fieldName</code>
   **
   ** @param  processInstance    the instance key of a provisioning process.
   ** @param  fieldName          the logical field name (the label defined on the
   **                            process form).
   **
   ** @return                    the value of a process form instance field
   **                            specified by <code>fieldName</code>; may be
   **                            <code>null</code>.
   */
  public final String processInstanceValue(final Long processInstance, final String fieldName) {
    final String method = "processInstanceValue";
    trace(method, SystemMessage.METHOD_ENTRY);

    String result = null;
    try {
      tcResultSet resultSet = formInstanceFacade().getProcessFormData(processInstance.longValue());
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_PROCESS), processInstance.toString() };
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      }
      else if (resultSet.getRowCount() > 1) {
        String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_PROCESS), processInstance.toString() };
        error(method, TaskBundle.format(TaskError.RESOURCE_AMBIGUOUS, parameter));
      }
      else
        result = resultSet.getStringValue(fieldName);
    }
    catch (tcNotAtomicProcessException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_PROCESS), processInstance.toString() };
      error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
    }
    catch (tcProcessNotFoundException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_PROCESS), processInstance.toString() };
      error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
    }
    catch (tcFormNotFoundException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_FORM), processInstance.toString() };
      error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
    }
    catch (tcColumnNotFoundException e) {
      error(method, TaskBundle.format(TaskError.COLUMN_NOT_FOUND, fieldName));
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }
}