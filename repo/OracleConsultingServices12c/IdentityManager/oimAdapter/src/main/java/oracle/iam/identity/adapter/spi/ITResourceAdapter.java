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
    Subsystem   :   Common Shared Adapter

    File        :   ITResourceAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ITResourceAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.GenericAdapter;

import oracle.iam.identity.foundation.naming.ITResource;
import oracle.iam.identity.foundation.naming.ITResourceType;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class ITResourceAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Adpapter dedicated to operate on the Oracle Identity Manager IT Resource
 ** object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class ITResourceAdapter extends GenericAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ITResourceAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public ITResourceAdapter(tcDataProvider provider) {
    // ensure inheritance
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instanceKeyAsString
  /**
   ** Returns the specified IT Resource instance key as a String.
   **
   ** @param  instanceKey        the internal system identifier of an IT
   **                            Resource.
   **
   ** @return                    the specified internal identifier converted to
   **                            a String.
   */
  public static String instanceKeyAsString(final long instanceKey) {
    return String.valueOf(instanceKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupDefintion
  /**
   ** Obtains the IT Resource Definition Type  from Oracle Identity Manager.
   **
   ** @param  definitionName     the name of the Oracle Identity Manager
   **                            IT Resource Definition Type where this wrapper
   **                            will return the internal system identifier for.
   **
   ** @return                    the desired internal identifier for the
   **                            specified definition name.
   **
   ** @throws TaskException      if IT Resource Type Definition could not be
   **                            found or is defined ambiguously.
   */
  public long lookupDefintion(final String definitionName)
    throws TaskException {

    final String method = "lookupDefintion";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String[] parameter  = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_SERVERTYPE);
    parameter[1] = definitionName;

    tcITResourceDefinitionOperationsIntf facade = service(tcITResourceDefinitionOperationsIntf.class);
    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ITResourceType.NAME, definitionName);
    debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));
    try {
      final tcResultSet resultSet =  facade.getITResourceDefinition(filter);
      if (resultSet.isEmpty())
        throw new TaskException(TaskError.ITRESOURCE_NOT_FOUND, definitionName);

      if (resultSet.getRowCount() != 1)
        throw new TaskException(TaskError.ITRESOURCE_AMBIGUOUS, definitionName);

      resultSet.goToRow(0);
      parameter[2] = resultSet.getStringValue(ITResourceType.KEY);
    }
    catch (tcColumnNotFoundException e) {
      error(method, TaskBundle.format(TaskError.COLUMN_NOT_FOUND, parameter));
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
    facade.close();
    debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));
    trace(method, SystemMessage.METHOD_EXIT);
    return Long.parseLong(parameter[2]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupInstance
  /**
   ** Obtains the IT Resource definition from Oracle Identity Manager.
   **
   **
   ** @param  definitionKey      the internal system identifier of an IT
   **                            Resource Type.
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            IT Resource Instance where this wrapper
   **                            belongs to.
   **
   ** @return                    the desired internal identifier for the
   **                            specified resource name.
   **
   ** @throws TaskException      if IT Resource Instance could not be found or
   **                            is defined ambiguously.
   */
  public long lookupInstance(final Long definitionKey, final String instanceName)
    throws TaskException {

    final String method = "lookupInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String[] parameter  = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_SERVER);
    parameter[1] = instanceName;

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ITResourceType.KEY, definitionKey.toString());
    filter.put(ITResource.NAME,    instanceName);
    debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));
    try {
      tcResultSet resultSet =  resourceFacade().findITResourceInstances(filter);
      if (resultSet .getRowCount() != 1)
        throw new TaskException(resultSet.isEmpty() ? TaskError.ITRESOURCE_NOT_FOUND : TaskError.ITRESOURCE_AMBIGUOUS, parameter[1]);

      resultSet.goToRow(0);
      parameter[2] = resultSet.getStringValue(ITResource.KEY);
    }
    catch (tcColumnNotFoundException e) {
      error(method, TaskBundle.format(TaskError.COLUMN_NOT_FOUND, parameter));
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
    resourceFacade().close();
    debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));
    trace(method, SystemMessage.METHOD_EXIT);
    return Long.parseLong(parameter[2]);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefixValue
  /**
   ** Prefix a given value with the key and the name of an IT Resource instance.
   **
   ** @param  instance           the system identifier of a Oracle Identity
   **                            Manager IT Resource Instance where this wrapper
   **                            belongs to.
   ** @param  value              the entitlement value to prefix with the key
   **                            and the name of an IT Resource instance.
   **
   ** @return                    the full qualified value.
   */
  public String prefixValue(final String instance, final String value) {
    final String method = "prefixValue";
    trace(method, SystemMessage.METHOD_ENTRY);
 
    String result = String.format("%s~%s~%s", instance, "Unknown", value);
    try {
      final Map<String, String> filter = new HashMap<String, String>();
      filter.put(ITResource.KEY, instance);
      final tcResultSet resultSet = resourceFacade().findITResourceInstances(filter);
      if (resultSet.getRowCount() != 1)
        error(method, TaskBundle.format(resultSet.isEmpty() ? TaskError.ITRESOURCE_NOT_FOUND : TaskError.ITRESOURCE_AMBIGUOUS, instance));
      else {
        resultSet.goToRow(0);
        result = String.format("%s~%s~%s", instance, resultSet.getStringValue(ITResource.NAME), value);
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    return result;
  }
}