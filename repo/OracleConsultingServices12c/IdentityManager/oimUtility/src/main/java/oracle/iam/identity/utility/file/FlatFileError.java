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

    File        :   FlatFileError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

////////////////////////////////////////////////////////////////////////////////
// interface FlatFileError
// ~~~~~~~~~ ~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public interface FlatFileError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX                  = "TXT-";

  // 00001 - 00010 task related errors
  static final String UNHANDLED               = PREFIX + "00001";
  static final String GENERAL                 = PREFIX + "00002";
  static final String ABORT                   = PREFIX + "00003";

  // 00011 - 00020 file system naming and lookup related errors
  static final String FILENAME_MISSING        = PREFIX + "00011";
  static final String NOTAFOLDER              = PREFIX + "00012";
  static final String NOTAFILE                = PREFIX + "00013";
  static final String NOTREADABLE             = PREFIX + "00014";
  static final String NOTWRITABLE             = PREFIX + "00015";
  static final String NOTCREATABLE            = PREFIX + "00016";
  static final String NOTCLOSEDINPUT          = PREFIX + "00017";
  static final String NOTCLOSEDOUTPUT         = PREFIX + "00028";

  // 00021 - 00030 processing related errors
  static final String PARSER_ERROR            = PREFIX + "00021";
  static final String EXECUTION_FAILED        = PREFIX + "00022";
  static final String CONTENT_UNKNOWN         = PREFIX + "00023";
  static final String CONTENT_NOT_FOUND       = PREFIX + "00024";
  static final String NOT_ENLISTED_ATTRIBUTE  = PREFIX + "00025";
  static final String NOT_ENLISTED_IDENTIFIER = PREFIX + "00026";
}