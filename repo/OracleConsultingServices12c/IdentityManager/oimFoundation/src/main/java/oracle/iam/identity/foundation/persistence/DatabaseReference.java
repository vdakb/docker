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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DatabaseReference.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseReference.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.Set;

import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskDescriptor;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AttributeTransformation;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseReference
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The descriptor to handle object references.
 */
public class DatabaseReference extends TaskDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to specify
   ** that the query to execute in the database to retrieve the information.
   */
  public static final String CATALOG_QUERY     = "Catalogue Query";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to specify
   ** that anotehr Metadata Descriptor that provides the entries that has to be
   ** filtered out.
   */
  public static final String EXCLUSION_CONTROL = "Exclusion Control";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link Set} with the names of identifiers which should not be
   ** contained in a returning {@link Set} pupulated from this reference
   ** descriptor.
   */
  private Set<String>        exclusion = null;

  /**
   ** ths {@link PreparedStatement} used to execute this descriptor as a query
   ** in the database.
   */
  private PreparedStatement  statement = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseReference</code> which is
   ** associated the specified Logging Provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   */
  public DatabaseReference(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseReference</code> which is
   ** associated the specified Logging Provider <code>loggable</code>.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   ** @param  mapping            the {@link AttributeMapping} of varaiables
   **                            provided by this reference descriptor.
   ** @param  constant           the {@link AttributeMapping} of constants
   **                            provided by this reference descriptor.
   ** @param  transformation     the {@link AttributeTransformation} of this
   **                            reference descriptor.
   */
  public DatabaseReference(final Loggable loggable, final AttributeMapping mapping, final AttributeMapping constant, final AttributeTransformation transformation) {
    // ensure inheritance
    super(loggable, mapping, constant, transformation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   */
  @Override
  public String toString() {
    return super.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogQuery
  /**
   ** Sets the resource key of the statement used to retrieve the information
   ** from the database.
   **
   ** @param  catalogQuery       the resource key of the statement used to
   **                            retrieve the information from the database.
   */
  public final void catalogueQuery(final String catalogQuery) {
    this.put(CATALOG_QUERY, catalogQuery);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogueQuery
  /**
   ** Returns the resource key of the statement used to retrieve the information
   ** from the database.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #CATALOG_QUERY}.
   **
   ** @return                    the resource key of the statement used to
   **                            retrieve the information from the database.
   */
  public final String catalogueQuery() {
    return (String)this.get(CATALOG_QUERY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exclusionControl
  /**
   ** Sets the name of the <code>Metadata Descriptor</code> used by this
   ** reference descriptor that provides the exclusions.
   **
   ** @param  exclusion          the name of the <code>Metadata Descriptor</code>
   **                            used by this descrptor that provides the
   **                            exclusions.
   */
  public final void exclusionControl(final String exclusion) {
    this.put(EXCLUSION_CONTROL, exclusion);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exclusionControl
  /**
   ** Returns the name of the <code>Metadata Descriptor</code> used by this
   ** descriptor that provides the exclusions.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #EXCLUSION_CONTROL}.
   **
   ** @return                    the name of the <code>Metadata Descriptor</code>
   **                            used by this descrptor that provides the
   **                            exclusions.
   */
  public final String exclusionControl() {
    return (String)this.get(EXCLUSION_CONTROL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exclusion
  /**
   ** Sets the {@link Set} containing exclusions.
   **
   ** @param  exclusion          the {@link Set} with the names of identifiers
   **                            which should not be contained in a returning
   **                            {@link Set} pupulated from this reference
   **                            descriptor.
   */
  public final void exclusion(Set<String> exclusion) {
    this.exclusion = exclusion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exclusion
  /**
   ** Returns the {@link Set} containing exclusions.
   **
   ** @return                    the {@link Set} with the names of identifiers
   **                            which should not be contained in a returning
   **                            {@link Set} pupulated from this reference
   **                            descriptor.
   */
  public final Set<String> exclusion() {
    return this.exclusion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   statement
  /**
   ** Returns the {@link PreparedStatement} to operate.
   **
   ** @return                    the {@link PreparedStatement} to operate.
   */
  public final PreparedStatement statement() {
    return this.statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** Prepares this descriptor to work with the database.
   **
   ** @param  connection         the JDBC connection to create a statement for.
   ** @param  string             the SQL Statment String.
   **
   ** @throws TaskException      if this descriptor is already prepared or the
   **                            prepare step fails overall.
   */
  public final void open(final Connection connection, final String string)
    throws TaskException {

    if (this.statement != null)
      throw new DatabaseException(DatabaseError.INSTANCE_ILLEGAL_STATE, "statement");

    this.statement = DatabaseStatement.createPreparedStatement(connection, string);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Unprepares this descriptor to work with the database.
   **
   ** @throws TaskException      if this descriptor is not prepared or the
   **                            step fails overall.
   */
  public final void close()
    throws TaskException {

    DatabaseStatement.closeStatement(this.statement);
    this.statement = null;
  }
}