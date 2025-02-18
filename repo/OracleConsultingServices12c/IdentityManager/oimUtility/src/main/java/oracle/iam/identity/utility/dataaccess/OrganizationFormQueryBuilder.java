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

    File        :   OrganizationFormQueryBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    OrganizationFormQueryBuilder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationFormQueryBuilder
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrganizationFormQueryBuilder</code> implements the base
 ** functionality to access the Oracle Identity Manager Organization form
 ** directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class OrganizationFormQueryBuilder extends StandardFormQueryBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String ENTITY            = "Organization";
  private static final long   OBJECTKEY         = 3L;
  private static final long   FORMDEFINITIONKEY = 2L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>OrganizationFormQueryBuilder</code> which use the
   ** specified Service Locator.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category for the Logger.
   **
   ** @throws TaskException      if the process form associated with the entity
   **                            name could be found.
   */
  public OrganizationFormQueryBuilder(final tcDataProvider provider, final String loggerCategory)
    throws TaskException {

    // ensure inheritance
    super(provider, loggerCategory, ENTITY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectKey (overridden)
  /**
   ** Return the internal system identifier of the Resource Object handeld by
   ** this QueryBuilder instance.
   **
   ** @return                    the internal system identifier of the Resource
   **                            Object handeld by this QueryBuilder instance.
   */
  @Override
  public long objectKey() {
    return OBJECTKEY;
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
    return FORMDEFINITIONKEY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formName (StandardFormQueryBuilder)
  /**
   ** Returns the internal name of a predefined Process Form.
   **
   ** @return                    the internal name of a predefined Process Form.
   */
  @Override
  protected String formName() {
    return AccessDescriptor.ACT.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   standardFields (StandardFormQueryBuilder)
  /**
   ** Creates the standard field mappings that describe this accessor.
   */
  @Override
  protected void standardFields() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   statusField (StandardFormQueryBuilder)
  /**
   ** Returns the column name that represents the status of the object.
   */
  @Override
  protected String statusField() {
    return AccessDescriptor.qualifiedColumnName(AccessDescriptor.ACT, AccessDescriptor.STATUS);
  }
}