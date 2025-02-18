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

    File        :   QueryBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    QueryBuilder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

import java.util.Iterator;
import java.util.Set;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// interface QueryBuilder
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>QueryBuilder</code> defines the method to access dataobject
 ** directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface QueryBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectKey
  /**
   ** Return the internal system identifier of the Resource Object handeld by
   ** this QueryBuilder instance.
   **
   ** @return                    the internal system identifier of the Resource
   **                            Object handeld by this QueryBuilder instance.
   */
  long objectKey();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formDefinitionKey
  /**
   ** Return the internal system identifier of the Resource Object Process Form
   ** handeld by this QueryBuilder instance.
   **
   ** @return                    the internal system identifier of the Resource
   **                            Object Process Form handeld by this
   **                            QueryBuilder instance.
   */
  long formDefinitionKey();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Return the name of the database object.
   **
   ** @return                    the name of the database object.
   */
  String name();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Return the label of the database object.
   **
   ** @return                    the label of the database object.
   */
  String label();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualifiedColumnName
  /**
   ** Build a full qualified column name for a field.
   **
   ** @param  columnnName        the name of the column.
   **
   ** @return                    the name of the form field.
   */
  String qualifiedColumnName(final String columnnName);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldIterator
  /**
   ** Return an {@link Iterator} for {@link AccessAttribute}s.
   **
   ** @return                    an {@link Iterator} for {@link AccessAttribute}s.
   */
  Iterator<AccessAttribute> fieldIterator();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameIterator
  /**
   ** Return an {@link Iterator} for field names.
   **
   ** @return                    an {@link Iterator} for field names.
   */
  Iterator<String> nameIterator();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Return the select clause for the status of the database object.
   **
   ** @return                    the select clause for the status of the
   **                            database object.
   */
  String status();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Return the select clause of the database object.
   **
   ** @param  filter             the {@link Set} that specifies the attributes
   **                            to include in the query.
   **
   ** @return                    the select clause of the database object.
   */
  String select(final Set<String> filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectTimestamp
  /**
   ** Return the part of the filter clause of the database object that bring up
   ** the timestamp columns of the underlying dataobject and the difference
   ** between these two columns in seconds.
   **
   ** @return                    the select filter of the database object.
   */
  String objectTimestamp();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   statusTimestamp
  /**
   ** Return the part of the filter clause of the database object that holding
   ** the status bring up the timestamp columns of the status object and the
   ** difference between these two columns in seconds.
   **
   ** @param   statusObject      the {@link AccessDescriptor} providing the
   **                            status information for a provisioned Resource Object
   **                            instance.
   **
   ** @return                    the select filter of the database object.
   */
  String statusTimestamp(final AccessDescriptor<AccessAttribute> statusObject);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Return the part of the select clause of the database object that bring up
   ** the database objects included in the reaulting querx.
   **
   ** @return                    the from clause to query the database object.
   */
  String from();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Return the filter clause.
   **
   ** @return                    the filter clause.
   */
  String filter();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Find the entity definition in the Oracle Identity Manager database
   ** repository.
   **
   ** @throws TaskException      if the process form associated with the entity
   **                            name could be found.
   */
  void find()
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describe
  /**
   ** Load the form object description.
   **
   ** @throws TaskException      if the process form associated with the entity
   **                            name could be found.
   */
  void describe()
    throws TaskException;
}