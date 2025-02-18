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
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

////////////////////////////////////////////////////////////////////////////////
// interface CSVError
// ~~~~~~~~~ ~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface CSVError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX                   = "CSV-";

  // 00061 - 00070 file system naming and lookup related errors
  static final String FILENAME_MISSING         = PREFIX + "00061";
  static final String FILEEXTENSION_IS_BAD     = PREFIX + "00062";
  static final String NOTAFOLDER               = PREFIX + "00063";
  static final String NOTAFILE                 = PREFIX + "00064";
  static final String NOTREADABLE              = PREFIX + "00065";
  static final String NOTWRITABLE              = PREFIX + "00066";
  static final String NOTCREATABLE             = PREFIX + "00067";
  static final String NOTCLOSEDINPUT           = PREFIX + "00068";
  static final String NOTCLOSEDOUTPUT          = PREFIX + "00069";

  // 00071 - 00080 parsing related errors
  static final String INVALID_ELEMENT          = PREFIX + "00071";
  static final String INVALID_STATE            = PREFIX + "00072";
  static final String INVALID_OPERATION        = PREFIX + "00073";
  static final String NOT_ENLISTED_ATTRIBUTE   = PREFIX + "00074";
  static final String NOT_ENLISTED_IDENTIFIER  = PREFIX + "00075";

  // 00081 - 00090 processing related errors
  static final String PARSER_ERROR             = PREFIX + "00081";
  static final String MISSING_HEADER           = PREFIX + "00082";
  static final String MISSING_SEPARATOR        = PREFIX + "00083";
  static final String MISSING_QUOTE_OPEN       = PREFIX + "00084";
  static final String MISSING_QUOTE_CLOSE      = PREFIX + "00085";
  static final String EXECUTION_FAILED         = PREFIX + "00086";
  static final String HEADER_UNKNOWN           = PREFIX + "00087";
  static final String CONTENT_UNKNOWN          = PREFIX + "00088";
  static final String CONTENT_NOT_FOUND        = PREFIX + "00089";
}