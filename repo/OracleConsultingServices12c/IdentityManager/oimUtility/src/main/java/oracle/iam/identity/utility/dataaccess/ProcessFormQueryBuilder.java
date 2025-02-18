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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Data Access Facilities

    File        :   ProcessFormQueryBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ProcessFormQueryBuilder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.naming.FormDefinition;
import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.utility.resource.DataAccessBundle;

////////////////////////////////////////////////////////////////////////////////
// class ProcessFormQueryBuilder
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ProcessFormQueryBuilder</code> implements the base functionality
 ** to access a generic process form directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class ProcessFormQueryBuilder extends AbstractQueryBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private long objectKey         = -1L;
  private long formDefinitionKey = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ProcessFormQueryBuilder</code> which use the specified
   ** Service Locator.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category for the Logger.
   ** @param  entityName         the name of a Oracle Identity Manager
   **                            Resource Object
   **
   ** @throws TaskException      if the process form associated with the entity
   **                            name could be found.
   */
  public ProcessFormQueryBuilder(final tcDataProvider provider, final String loggerCategory, final String entityName)
    throws TaskException {

    // ensure inheritance
    super(provider, loggerCategory, entityName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectKey (QueryBuilder)
  /**
   ** Return the internal system identifier of the Resource Object handeld by
   ** this QueryBuilder instance.
   **
   ** @return                    the internal system identifier of the Resource
   **                            Object handeld by this QueryBuilder instance.
   */
  @Override
  public long objectKey() {
    return this.objectKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formDefinitionKey (overridden)
  /**
   ** Return the internal system identifier of the Resource Object Process Form
   ** handeld by this QueryBuilder instance.
   **
   ** @return                    the internal system identifier of the Resource
   **                            Object Process Form handeld by this
   **                            QueryBuilder instance.
   */
  @Override
  public long formDefinitionKey() {
    return this.formDefinitionKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status (QueryBuilder)
  /**
   ** Return the select clause for the status of the database object.
   **
   ** @return                    the select clause for the status of the
   **                            database object.
   */
  @Override
  public String status() {
    final StringBuilder buffer = new StringBuilder();
    buffer.append(AccessDescriptor.qualifiedColumnName(AccessDescriptor.USR, AccessDescriptor.LOGIN));
    buffer.append(SystemConstant.COMMA);
    buffer.append(AccessDescriptor.qualifiedColumnName(AccessDescriptor.OBJ, AccessDescriptor.NAME));
    buffer.append(SystemConstant.COMMA);
    buffer.append(AccessDescriptor.qualifiedColumnName(AccessDescriptor.OST, AccessDescriptor.STATUS));
    buffer.append(" AS object_status");
    buffer.append(SystemConstant.COMMA);
    buffer.append(statusTimestamp(AccessDescriptor.OIU));
    buffer.append(SystemConstant.COMMA);
    buffer.append(objectTimestamp());

    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from (QueryBuilder)
  /**
   ** Return the part of the select clause of the database object that bring up
   ** the database objects included in the reaulting querx.
   **
   ** @return                    the from clause to query the database object.
   */
  @Override
  public String from() {
    final StringBuilder buffer = new StringBuilder();
    buffer.append("obj,pkg,oast,orc,oiu,usr,").append(this.descriptor.fromClause());
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (QueryBuilder)
  /**
   ** Returns a string representation of this collection. The string
   ** representation consists of filter conditions that can be applied to a
   ** query to restricts the returned result..
   **
   ** @return                    a string representation of this collection as
   **                            selectable objects.
   */
  @Override
  public String filter() {
    final StringBuilder buffer = new StringBuilder();
    buffer.append(AccessDescriptor.OBJ.keyField());
    buffer.append(" = ? AND pkg.obj_key = obj.obj_key AND orc.pkg_key = pkg.pkg_key AND ost.obj_key = obj.obj_key AND oiu.ost_key = ost.ost_key AND oiu.orc_key = orc.orc_key AND usr.usr_key = oiu.usr_key AND ");
    buffer.append(this.descriptor.name());
    buffer.append(".orc_key = ");
    buffer.append(AccessDescriptor.ORC.keyField());
    buffer.append(" AND (");
    buffer.append(timestamp(AccessDescriptor.OIU.updateField()));
    buffer.append(" OR ");
    buffer.append(timestamp(this.descriptor.updateField()));
    buffer.append(")");

    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find (QueryBuilder)
  /**
   ** Find the entity definition in the Oracle Identity Manager database
   ** repository.
   */
  @Override
  public void find()
    throws TaskException {

    final String method  = "find";
    trace(method, SystemMessage.METHOD_ENTRY);

    tcObjectOperationsIntf objectFacade = objectFacade();
    String[]               parameter    = { DataAccessBundle.string(DataAccessMessage.OBJECT), this.entityName , SystemConstant.EMPTY};
    try {
      debug(method, DataAccessBundle.format(DataAccessMessage.NAME_TORESOLVE, parameter));
      final Map<String, String> filter = new HashMap<String, String>();
      filter.put(ResourceObject.NAME, this.entityName);
      tcResultSet resultSet = objectFacade.findObjects(filter);
      if (resultSet.getRowCount() == 0)
        throw new DataAccessException(DataAccessError.NORESOURCE, this.entityName);
      if (resultSet.getRowCount() > 1)
        throw new DataAccessException(DataAccessError.RESOURCE_AMBIGUOUS, this.entityName);

      resultSet.goToRow(0);
      this.objectKey = resultSet.getLongValue(ResourceObject.KEY);
      parameter[2] = Long.toString(this.objectKey);
      debug(method, DataAccessBundle.format(DataAccessMessage.NAME_RESOLVED, parameter));

      parameter[0] = DataAccessBundle.string(DataAccessMessage.FORMDEFINITION);
      debug(method, DataAccessBundle.format(DataAccessMessage.KEY_TORESOLVE, parameter));
      resultSet = objectFacade.getProcessesForObject(this.objectKey);
      if (resultSet.getRowCount() == 0)
        throw new DataAccessException(DataAccessError.NORESOURCEPROCESS, this.entityName);
      if (resultSet.getRowCount() > 1)
        throw new DataAccessException(DataAccessError.RESOURCEPROCESS_AMBIGUOUS, this.entityName);

      resultSet.goToRow(0);
      this.formDefinitionKey = resultSet.getLongValue(FormDefinition.KEY);
      parameter[2] = Long.toString(this.formDefinitionKey);
      debug(method, DataAccessBundle.format(DataAccessMessage.KEY_RESOLVED, parameter));
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      if (objectFacade != null)
        objectFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describe
  /**
   ** Load the form object description.
   */
  @Override
  public void describe()
    throws TaskException {

    final String method  = "describe";
    trace(method, SystemMessage.METHOD_ENTRY);

    tcFormDefinitionOperationsIntf formFacade = service(tcFormDefinitionOperationsIntf.class);
    String[]                       parameter  = { DataAccessBundle.string(DataAccessMessage.FORMDEFINITION), Long.toString(this.formDefinitionKey), SystemConstant.EMPTY};
    try {
      debug(method, DataAccessBundle.format(DataAccessMessage.KEY_TORESOLVE, parameter));
      final Map<String, String> filter = new HashMap<String, String>(1);
      filter.put(FormDefinition.KEY, Long.toString(this.formDefinitionKey));
      tcResultSet resultSet = formFacade.findForms(filter);
      if (resultSet.getRowCount() == 0)
        throw new DataAccessException(DataAccessError.NOFORMDEFINITION, Long.toString(this.formDefinitionKey));

      resultSet.goToRow(0);
      parameter[2] = resultSet.getStringValue(FormDefinition.NAME);
      debug(method, DataAccessBundle.format(DataAccessMessage.KEY_RESOLVED, parameter));

      resultSet = formFacade.getFormVersions(this.formDefinitionKey);
      if (resultSet.getRowCount() == 0)
        throw new DataAccessException(DataAccessError.NOFORMVERSION, Long.toString(this.formDefinitionKey));
      if (resultSet.getRowCount() > 1)
        throw new DataAccessException(DataAccessError.FORMVERSION_AMBIGUOUS, Long.toString(this.formDefinitionKey));

      resultSet.goToRow(0);
      resultSet = formFacade.getFormFields(this.formDefinitionKey, resultSet.getIntValue(FormDefinition.VERSION_ACTIVE));
      int size = resultSet.getRowCount();
      if (size == 0)
        throw new DataAccessException(DataAccessError.NOFORMACTIVATED, Long.toString(this.formDefinitionKey));

      this.descriptor = new AccessDescriptor<AccessAttribute>(parameter[2], resultSet.getStringValue(FormDefinition.DESCRIPTION), size);
      for (int i = 0; i < size; i++) {
        resultSet.goToRow(i);
        this.descriptor.add(new AccessAttribute(
            resultSet.getStringValue(FormDefinition.COLUMN_NAME)
          , resultSet.getBooleanValue(FormDefinition.COLUMN_ENCRYPTED)
        ));
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      if (formFacade != null)
        formFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}