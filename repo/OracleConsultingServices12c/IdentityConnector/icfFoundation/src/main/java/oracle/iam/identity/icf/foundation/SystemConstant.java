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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   SystemConstant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SystemConstant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation;

////////////////////////////////////////////////////////////////////////////////
// interface SystemConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** ToDO: describe here
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SystemConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                  = "OCF-";

  static final String EMPTY                   = "";
  static final String NULL                    = "[null]";
  static final String LINEBREAK               = "\n";
  static final String PATHBREAK               = "/";

  static final char   BLANK                   = ' ';
  static final char   BRACEOPEN               = '(';
  static final char   BRACECLOSE              = ')';
  static final char   COLON                   = ':';
  static final char   SEMICOLON               = ';';
  static final char   COMMA                   = ',';
  static final char   DELIMITER               = '|';
  static final char   EQUAL                   = '=';
  static final char   PERIOD                  = '.';
  static final char   QUOTE                   = '\"';
  static final char   SLASH                   = '/';
  static final char   QUESTION                = '?';
  static final char   LINEFEED                = '\n';
  static final char   CARRIGERETURN           = '\r';
  static final char   TABULATOR               = '\t';

  static final char   LIST_OPENLIST           = '[';
  static final char   LIST_CLOSELIST          = ']';

  static final String TRUE                    = "true";
  static final String FALSE                   = "false";
  static final String ON                      = "on";
  static final String OFF                     = "off";
  static final String YES                     = "yes";
  static final String NO                      = "no";

  /**
   ** The column at which to wrap long lines of output in the command-line
   ** tools.
   */
  static final int    TERMINAL_LINE_WIDTH_MAX = 80;
}