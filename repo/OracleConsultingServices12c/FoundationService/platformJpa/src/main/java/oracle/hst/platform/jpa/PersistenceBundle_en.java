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

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Common Shared Utility

    File        :   PersistenceBundle_en.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class PersistenceBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class PersistenceBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // JPA-00001 - 00010 system related errors
    { PersistenceError.UNHANDLED,       "An unhandled exception has occured"}
  , { PersistenceError.GENERAL,         "General error" }
  , { PersistenceError.ABORT,           "Execution aborted"}

    // JPA-00011 - 00020 operational errors
  , { PersistenceError.EXISTS,          "Entity [%1$s] with identifier [%2$s] already exists!"}
  , { PersistenceError.NOT_EXISTS,      "Entity [%1$s] with identifier [%2$s] does not exists!"}
  , { PersistenceError.AMBIGUOUS,       "Entity [%1$s] with identifier [%2$s] is ambiguously defined!"}
  , { PersistenceError.NOT_CREATABLE,   "Create operation is not allowed for entity [%1$s]!"}
  , { PersistenceError.NOT_CREATED,     "Create operation failed for entity [%1$s]!"}
  , { PersistenceError.NOT_MODIFIABLE,  "Modify operation is not allowed for entity [%1$s]!"}
  , { PersistenceError.NOT_MODIFIED,    "Modify operation failed for entity [%1$s]!"}
  , { PersistenceError.NOT_DELETABLE,   "Delete operation is not allowed for entity [%1$s]!"}
  , { PersistenceError.NOT_DELETED,     "Delete operation failed for entity [%1$s]!"}

    // JPA-00021 - 00030 attribute validation errors
  , { PersistenceError.NOT_NULL,        "Attribute [%2$s] must not be null or empty for entity [%1$s]!"}

    // JPA-00031 - 00040 search criteria errors
  , { PersistenceError.CRITERIA_SPEC,   "The query condition does not meet the specification!"}
  , { PersistenceError.CRITERIA_NESTED, "You cannot nest [%2$s] in [%1$s]!"}
  };

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  @Override
  public Object[][] getContents() {
    return CONTENT;
  }
}