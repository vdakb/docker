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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Provisioning Management

    File        :   ReconciliationEventStatus.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationEventStatus.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

////////////////////////////////////////////////////////////////////////////////
// enum ReconciliationEventStatus
// ~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by Reconciliation Event customization.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public enum ReconciliationEventStatus {
    INITIAL("Event Received")
  , DATA("Data Received")
  , DATA_NOTVALID("Data Validation Failed")
  , DATA_VALID("Data Validation Succeeded")
  , EVENT_REEVAL("Being Re-evaluated")
  , EVENT_RETRY("Being Re-tried")
  , CREATE_SUCCESS("Creation Succeeded")
  , CREATE_FAILURE("Creation Failed")
  , UPDATE_SUCCESS("Update Succeeded")
  , UPDATE_FAILURE("Update Failed")
  , DELETE_SUCCESS("Delete Succeeded")
  , DELETE_FAILURE("Delete Failed")
  , MATCH_NOT_FOUND("No User Match Found")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String       value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ReconciliationEventStatus</code> that allows use
   ** as a JavaBean.
   **
   ** @param  value            the value.
   */
  ReconciliationEventStatus(final String value) {
    // initialize instance attributes
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the value property.
   **
   ** @return                  the value of the value property.
   **                          Possible object is {@link String}.
   */
  public String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromValue
  /**
   ** Factory method to create a proper OwnerType from the given string value.
   **
   ** @param  value            the string value the severity should be
   **                          returned for.
   **
   ** @return                  the owner type.
   */
  public static ReconciliationEventStatus from(final String value) {
    for (ReconciliationEventStatus cursor : ReconciliationEventStatus.values()) {
      if (cursor.value.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}