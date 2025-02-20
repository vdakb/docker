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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Frontend Extension
    Subsystem   :   Web Service

    File        :   Bundle_de.java

    Compiler    :   JDK 1.8

    Author      :   sylver.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    Bundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-02  SBernet    First release version
*/
package bka.iam.identity.service.resource;

import bka.iam.identity.service.ServiceError;

import oracle.hst.foundation.resource.ListResourceBundle;

///////////////////////////////////////////////////////////////////////////////
// class Bundle_en
// ~~~~~ ~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
 ** </ul>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bundle_de extends ListResourceBundle {

  private static final String[][] CONTENT = {
     // UID-00001 - 00010 system related errors
    { ServiceError.GENERAL,                       "Allgemeiner Fehler: %1$s" }
  , { ServiceError.UNHANDLED,                     "Eine unbehandelte Ausnahme is aufgetreten: %1$s"}
  , { ServiceError.ABORT,                         "Die Verarbeitung wird abgebrochen: %1$s"}
  , { ServiceError.NOTIMPLEMENTED,                "Funktionalität ist nicht implementiert"}
    
    // 00011 - 00020 service configuration related errors
  , { ServiceError.ITRESOURCENOTFOUND,            "IT Resource: %1$s not found"}
  , { ServiceError.ITRESOURCE_MISCONFIGURED,      "IT Resource: %1$s is not configured properly"}
    
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
  public Object[][] getContents() {
    return CONTENT;
  }
}
