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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   EndpointBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    EndpointBundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.api;

import java.util.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class EndpointBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~
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
public class EndpointBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // UID-00011 - 00020 method argument related errors
    { EndpointError.ARGUMENT_IS_NULL,              "Übergebenes Argument [%1$s] darf nicht null sein." }
  , { EndpointError.ARGUMENT_BAD_TYPE,             "Übergebenes Argument [%1$s] ist vom falschen Typ." }
  , { EndpointError.ARGUMENT_BAD_VALUE,            "Übergebenes Argument [%1$s] enthält einen ungültigen Wert." }
  , { EndpointError.ARGUMENT_LENGTH_MISMATCH,      "Die Länge des übergebenen Werts für Argument [%1$s] stimmt nicht mit der erwarteten Länge überein." }
  , { EndpointError.ARGUMENT_SIZE_MISMATCH,        "Die Arraygröße für das übergebene Argument [%1$s] stimmt nicht mit der erwarteten Größe überein." }

     // UID-00021 - 00030 method invokation related errors
  , { EndpointError.METHOD_NOT_PERMITTED,          "Sie erhalten diese Fehlermeldung, weil Sie eine Methode aufgerufen haben, die für diesen Endpunkt nicht zulässig ist." }

     // UID-00031 - 00040 operation related errors
  , { EndpointError.OPERATION_NOT_PERMITTED,       "Sie erhalten diese Fehlermeldung, weil Sie eine Operation aufgerufen haben, die für den aktuellen Zustand der Entität nicht zulässig ist." }

     // 00041 - 00050 tenant related errors
  , { EndpointError.TENANT_SEARCH_NOT_PERMITTED,   "Es ist Ihnen nicht gestattet, innerhalb von Mandanten [%1$s] nach eindeutigen Kennung zu suchen." }
  , { EndpointError.TENANT_LOOKUP_NOT_PERMITTED,   "Es ist Ihnen nicht gestattet, im Mandanten [%1$s] nach einer eindeutigen Kennung zu suchen." }
  , { EndpointError.TENANT_MODIFY_NOT_PERMITTED,   "Es ist Ihnen nicht gestattet, eindeutige Kennung im Mandanten [%1$s] zu ändern." }
  , { EndpointError.TENANT_GENERATE_NOT_PERMITTED, "Es ist Ihnen nicht gestattet, eine eindeutige Kennung im Mandanten [%1$s] zu generieren." }
  , { EndpointError.TENANT_REGISTER_NOT_PERMITTED, "Es ist Ihnen nicht gestattet, eine eindeutige Kennung im Mandanten [%1$s] zu registrieren." }
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