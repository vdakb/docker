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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   FlatFileBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileBundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.utility.file.FlatFileError;
import oracle.iam.identity.utility.file.FlatFileMessage;

////////////////////////////////////////////////////////////////////////////////
// class FlatFileBundle_de
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
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public class FlatFileBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // TXT-00001 - 00010 General error (Undefined)
    { FlatFileError.GENERAL,                 "Genereller Fehler: " }
  , { FlatFileError.UNHANDLED,               "Eine unbahandlete Ausnahme ist aufgetreten:"}
  , { FlatFileError.ABORT,                   "Verarbeitung abgebrochen: %1$s"}

     // TXT-00011 - 00020 file system naming and lookup related errors
  , { FlatFileError.FILENAME_MISSING,        "File missing: [%1$s] erwartet einen einzelnene Dateinamen in der Kommandozeile!"}
  , { FlatFileError.NOTAFOLDER,              "Verwendung von [%2$s] als [%1$s] ist unzlässige, es ist kein Verzeichnis" }
  , { FlatFileError.NOTAFILE,                "Verwendung von [%2$s] als [%1$s] ist unzlässige, es ist keine Datei" }
  , { FlatFileError.NOTREADABLE,             "[%2$s] kann nicht als [%1$s] verwendet werden, es kann nicht gelesen werden" }
  , { FlatFileError.NOTWRITABLE,             "[%2$s] kann nicht als [%1$s] verwendet werden, es kann nicht geschriben werden" }
  , { FlatFileError.NOTCREATABLE,            "[%1$s] kann nicht erzeugt werden. Grund: %2$s" }
  , { FlatFileError.NOTCLOSEDINPUT,          "Eingabestrom [%1$s] konnte nicht geschlossen werden" }
  , { FlatFileError.NOTCLOSEDOUTPUT,         "Ausgabestrom [%1$s] konnte nicht geschlossen werden" }

     // TXT-00021 - 00030 parsing related errors
  , { FlatFileError.PARSER_ERROR,            "Unexpected exception %1$s at line %2$s!\n%3$s"}
  , { FlatFileError.EXECUTION_FAILED,        "%1$s failed to %2$s %3$s !"}
  , { FlatFileError.CONTENT_UNKNOWN,         "Feld [%1$s] hat keinen Inhalt!"}
  , { FlatFileError.CONTENT_NOT_FOUND,       "Feld [%1$s] ist nicht bekannt!"}
  , { FlatFileError.NOT_ENLISTED_ATTRIBUTE,  "Attribut [%1$s] ist nicht als Attribute registiert."}
  , { FlatFileError.NOT_ENLISTED_IDENTIFIER, "Attribut [%1$s] ist nicht als Attribute registiert."}

     // TXT-01000 generic message template
  , { FlatFileMessage.MESSAGE,               "%1$s" }

     // TXT-01001 - 01010 processing messages
  , { FlatFileMessage.VALIDATING,            "Validating FlatFile descriptor" }
  , { FlatFileMessage.LOADING,               "Loading ..." }
  , { FlatFileMessage.PROCESSING,            "Processing ..." }
  , { FlatFileMessage.COMPLETED,             "End of differences." }
  , { FlatFileMessage.IDENTICALLY,           "Dateien sind identisch." }
  , { FlatFileMessage.LINECOUNT,             "Line count of %1$s file: %2$s" }
  , { FlatFileMessage.ENDOFFILE,             "End-Of-File reached for %1$s" }
  , { FlatFileMessage.EMPTYFILE,             "Datei [%1$s] ist leer" }
  , { FlatFileMessage.CHUNKSIZE,             "%1$s entities written to %2$s" }
  , { FlatFileMessage.CHUNKMERGED,           "Merge of chunk %1$s completed" }
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