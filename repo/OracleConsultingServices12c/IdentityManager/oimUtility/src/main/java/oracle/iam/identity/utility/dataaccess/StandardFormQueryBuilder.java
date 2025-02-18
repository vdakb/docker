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

    File        :   StandardFormQueryBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    StandardFormQueryBuilder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

import java.util.Set;
import java.util.Map;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.platform.entitymgr.NoSuchEntityException;
import oracle.iam.platform.entitymgr.EntityManager;

import oracle.iam.platform.entitymgr.vo.EntityDefinition;
import oracle.iam.platform.entitymgr.vo.AttributeDefinition;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.utility.resource.DataAccessBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class StandardFormQueryBuilder
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>StandardFormQueryBuilder</code> implements the base functionality
 ** to access the standard Oracle Identity Manager forms directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class StandardFormQueryBuilder extends ProcessFormQueryBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StandardFormQueryBuilder</code> which use the specified
   ** Service Locator.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category for the Logger.
   ** @param  entityName         the name of a Oracle Identity Manager
   **                            Resource Object.
   **
   ** @throws TaskException      if the process form associated with the entity
   **                            name could be found.
   */
  public StandardFormQueryBuilder(final tcDataProvider provider, final String loggerCategory, final String entityName)
    throws TaskException {

    // ensure inheritance
    super(provider, loggerCategory, entityName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formName
  /**
   ** Returns the internal name of a predefined Process Form.
   **
   ** @return                    the internal name of a predefined Process Form.
   */
  protected abstract String formName();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   standardFields
  /**
   ** Creates the standard field mappings that describe this accessor.
   */
  protected abstract void standardFields();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   statusField
  /**
   ** Returns the column name that represents the status of the object.
   **
   ** @return                    the column name that represents the status of
   **                            the object.
   */
  protected abstract String statusField();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status (overridden)
  /**
   ** Return the select clause for the status of the database object.
   **
   ** @return                    the select clause for the status of the
   **                            database object.
   */
  @Override
  public String status() {
    final StringBuilder buffer = new StringBuilder();

    buffer.append(statusField());
    buffer.append(" AS object_status");
    buffer.append(SystemConstant.COMMA);
    buffer.append(statusTimestamp(this.descriptor));
    buffer.append(SystemConstant.COMMA);
    buffer.append(objectTimestamp());

    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from (overriden)
  /**
   ** Return the part of the select clause of the database object that bring up
   ** the database objects included in the reaulting querx.
   **
   ** @return                    the from clause to query the database object.
   */
  @Override
  public String from() {
    return this.descriptor.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (overriden)
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
    return timestamp(this.descriptor.updateField());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findEntity (overriden)
  /**
   ** Load the form definition for the given Resource Object.
   */
  @Override
  public void find()
    throws TaskException {

    final String method  = "findEntity";
    trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = { DataAccessBundle.string(DataAccessMessage.OBJECT), this.entityName , Long.toString(objectKey())};
    debug(method, DataAccessBundle.format(DataAccessMessage.NAME_TORESOLVE, parameter));
    debug(method, DataAccessBundle.format(DataAccessMessage.NAME_RESOLVED, parameter));

    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describe (overriden)
  /**
   ** Load the form object description.
   **
   ** @throws TaskException      if the resource object cannot be found.
   */
  @Override
  public void describe()
    throws TaskException {

    final String method  = "loadForm";
    trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter  = { DataAccessBundle.string(DataAccessMessage.FORMDEFINITION), Long.toString(formDefinitionKey()) , this.entityName};
    debug(method, DataAccessBundle.format(DataAccessMessage.KEY_TORESOLVE, parameter));
    final String formName = formName();
    debug(method, DataAccessBundle.format(DataAccessMessage.KEY_RESOLVED, parameter));

    this.descriptor = new AccessDescriptor<AccessAttribute>(formName, this.entityName);
    EntityManager    manager    = service(EntityManager.class);
    Set<String>      attributes = null;
    EntityDefinition definition = null;
    try {
      definition = manager.getEntityDefinition(this.entityName);
      attributes = manager.getTargetFields(this.entityName, definition.getAttributeNames());
    }
    catch (NoSuchEntityException e) {
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, this.entityName);
    }
    for(String name : attributes) {
      final AttributeDefinition attribute  = definition.getAttributeDefinition(name);
      if (attribute == null) {
        this.descriptor.add(new AccessAttribute(name, false));
      }
      else {
        final Map<String, String> properties = attribute.getAttachedMetadata("properties");
        final String              encryption = properties.get("encryption");
        this.descriptor.add(new AccessAttribute(name, StringUtility.isEmpty(encryption) ? false : encryption.equalsIgnoreCase("encrypt")));
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }
}