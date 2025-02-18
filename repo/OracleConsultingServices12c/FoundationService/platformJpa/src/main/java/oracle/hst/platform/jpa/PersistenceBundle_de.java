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

    File        :   PersistenceBundle_de.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class PersistenceBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PersistenceBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // JPA-00001 - 00010 system related errors
    { PersistenceError.UNHANDLED,       "Eine unbehandelte Ausnahme ist aufgetreten"}
  , { PersistenceError.GENERAL,         "Allgemeiner Fehler"}
  , { PersistenceError.ABORT,           "Die Verarbeitung wurde abgebrochen"}

    // JPA-00011 - 00020 operational errors
  , { PersistenceError.EXISTS,          "Entität [%1$s] with identifier [%2$s] already exists!"}
  , { PersistenceError.NOT_EXISTS,      "Entität [%1$s] with identifier [%2$s] does not exists!"}
  , { PersistenceError.AMBIGUOUS,       "Entität [%1$s] with identifier [%2$s] is ambiguously defined!"}
  , { PersistenceError.NOT_CREATABLE,   "Operation Anlegen ist für Entität [%1$s] nicht erlaubt!"}
  , { PersistenceError.NOT_CREATED,     "Operation Anlegen ist für Entität [%1$s] fehlgeschlagen!"}
  , { PersistenceError.NOT_MODIFIABLE,  "Operation Ändern ist für Entität [%1$s] nicht erlaubt!"}
  , { PersistenceError.NOT_MODIFIED,    "Operation Ändern ist für Entität [%1$s] fehlgeschlagen!"}
  , { PersistenceError.NOT_DELETABLE,   "Operation Löschen ist für Entität [%1$s] nicht erlaubt!"}
  , { PersistenceError.NOT_DELETED,     "Operation Löschen ist für Entität [%1$s] fehlgeschlagen!"}

    // JPA-00021 - 00030 attribute validation errors
  , { PersistenceError.NOT_NULL,        "Attribut [%2$s] darf für Entität [%1$s] nicht null oder leer sein!"}

    // JPA-00031 - 00040 search criteria errors
  , { PersistenceError.CRITERIA_SPEC,   "Die Abfragebedingung entspricht nicht der Spezifikation!"}
  , { PersistenceError.CRITERIA_NESTED, "Sie können [%2$s] nicht in [%1$s] verschachteln!"}
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