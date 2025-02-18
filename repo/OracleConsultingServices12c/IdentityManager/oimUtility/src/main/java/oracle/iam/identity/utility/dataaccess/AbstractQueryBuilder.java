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

    File        :   AbstractQueryBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractQueryBuilder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

import java.util.Iterator;
import java.util.Set;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.TransformationIterator;

import oracle.iam.identity.foundation.AbstractTask;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAdapterTask;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractQueryBuilder
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractQueryBuilder</code> implements the base functionality to
 ** access data object directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractQueryBuilder extends    AbstractAdapterTask
                                           implements QueryBuilder
                                           ,          AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String                      entityName;
  protected AccessDescriptor<AccessAttribute> descriptor      = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractQueryBuilder</code> which use the
   ** specified category for logging purpose.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category for the Logger.
   ** @param  entityName         the name of a Oracle Identity Manager
   **                            Resource Object
   **
   ** @throws TaskException      if the resource object cannot be found.
   */
  public AbstractQueryBuilder(final tcDataProvider provider, final String loggerCategory, final String entityName)
    throws TaskException {

    // ensure inheritance
    super(provider, loggerCategory);

    // initialize instance attributes
    this.entityName = entityName;
    describe();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityName
  /**
   ** Return the name of the Resource Object handeld by this QueryBuilder
   ** instance.
   **
   ** @return                    the name of the Resource Object handeld by this
   **                            QueryBuilder instance.
   */
  public final String entityName() {
    return this.entityName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (QueryBuilder)
  /**
   ** Return the name of the database object.
   **
   ** @return                    the name of the database object.
   */
  @Override
  public final String name() {
    return this.descriptor.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label (QueryBuilder)
  /**
   ** Return the label of the database object.
   **
   ** @return                    the label of the database object.
   */
  @Override
  public final String label() {
    return this.descriptor.label();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualifiedColumnName (QueryBuilder)
  /**
   ** Build a full qualified column name for a field.
   **
   ** @param  columnnName        the name of the column.
   **
   ** @return                    the name of the form field.
   */
  @Override
  public String qualifiedColumnName(String columnnName) {
    return AccessDescriptor.qualifiedColumnName(this.descriptor, columnnName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldIterator (QueryBuilder)
  /**
   ** Return an {@link Iterator} for {@link AccessAttribute}s.
   **
   ** @return                    an {@link Iterator} for {@link AccessAttribute}s.
   */
  @Override
  public Iterator<AccessAttribute> fieldIterator() {
    return this.descriptor.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameIterator (QueryBuilder)
  /**
   ** Return an {@link Iterator} for field names.
   **
   ** @return                    an {@link Iterator} for field names.
   */
  @Override
  public Iterator<String> nameIterator() {
    final Iterator<AccessAttribute> iterator = this.descriptor.iterator();
    return new TransformationIterator(iterator) {
       protected Object transform(final AccessAttribute origin) {
        return origin.name();
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectTimestamp (QueryBuilder)
  /**
   ** Return the part of the filter clause of the database object that bring up
   ** the timestamp columns of the underlying dataobject and the difference
   ** between these two columns in seconds.
   **
   ** @return                    the select filter of the database object.
   */
  @Override
  public String objectTimestamp() {
    return timestamp(this.descriptor.createField(), "object_create", this.descriptor.updateField(), "object_update", "object_threshold");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   statusTimestamp (QueryBuilder)
  /**
   ** Return the part of the filter clause of the database object that holding
   ** the status bring up the timestamp columns of the status object and the
   ** difference between these two columns in seconds.
   **
   ** @param   statusObject      the {@link AccessDescriptor} providing the status
   **                            information for a provisioned Resource Object
   **                            instance.
   **
   ** @return                    the select filter of the database object.
   */
  @Override
  public String statusTimestamp(final AccessDescriptor<AccessAttribute> statusObject) {
    return timestamp(statusObject.createField(), "status_create", statusObject.updateField(), "status_update", "status_threshold");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select (QueryBuilder)
  /**
   ** Return the select clause of the database object.
   **
   ** @return                    the select clause of the database object.
   */
  @Override
  public String select(final Set<String> filter) {
    final StringBuilder buffer = new StringBuilder();
    if (filter != null && filter.size() > 0) {
      for (int i = 0; i < this.descriptor.size(); i++) {
        final String name = this.descriptor.get(i).name();
        if (filter.contains(name)) {
          buffer.append(SystemConstant.COMMA);
          buffer.append(name);
        }
      }
    }
    else {
      for (int i = 0; i < this.descriptor.size(); i++) {
        buffer.append(SystemConstant.COMMA);
        buffer.append(this.descriptor.get(i).name());
      }
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualifiedColumnName
  /**
   ** Build a full qualified column name for a field.
   **
   ** @param  objectName         the name data object.
   ** @param  columnnName        the name of the column.
   **
   ** @return                    the name of the form field.
   */
  public static String qualifiedColumnName(String objectName, String columnnName) {
    Object[] parameter = {objectName, objectName, columnnName};
    return String.format("%1$s.%2$s%3$s", parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestamp
  /**
   ** Return a filter criterion bases on the provided column name.
   ** <br>
   ** The resulting String contains the column name that is equalized with a
   ** <code>to_data</code> conversion. The <code>to_data</code> conversion
   ** contains to bind variables.
   ** <pre>
   **   usr_update = to_data(?, ?)
   ** </pre>
   ** The first bind variable is the date value to compare against. The secoond
   ** bind variable is the format of the string the first bind variable will be
   ** provided at execution time.
   ** <pre>
   **   Prepared statement = DataSourceManager.createPreparedStatement(connection, query);
   **   statement.set(1, theDate);
   **   statement.set(2, theFormat);
   ** </pre>
   **
   ** @param  updateColumnName   the name of the database column the filter
   **                            criterion should be based on
   **
   ** @return                    a filter criterion bases on the provided column
   **                            name.
   */
  protected String timestamp(final String updateColumnName) {
    Object[] parameter = {updateColumnName};
    return String.format("%1$s >= to_date(?, ?)", parameter);
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestamp
  private String timestamp(final String createColumnName, final String createColumnAlias, final String updateColumnName, final String updateColumnAlias, final String thresholdColumnAlias) {
    StringBuilder buffer = new StringBuilder();
    buffer.append(createColumnName);
    buffer.append(" AS ");
    buffer.append(createColumnAlias);
    buffer.append(SystemConstant.COMMA);
    buffer.append(updateColumnName);
    buffer.append(" AS ");
    buffer.append(updateColumnAlias);
    buffer.append(SystemConstant.COMMA);
    buffer.append("ROUND(TO_NUMBER(");
    buffer.append(updateColumnName);
    buffer.append("-");
    buffer.append(createColumnName);
    buffer.append(")*86400,0)"); // 86400 --> 24*60*60
    buffer.append(" AS ");
    buffer.append(thresholdColumnAlias);

    return buffer.toString();
  }
}