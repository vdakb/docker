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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   XMLOutputMode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    XMLOutputMode.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.xml;

////////////////////////////////////////////////////////////////////////////////
// enum XMLOutputMode
// ~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>XMLOutputMode</code> enumeration is used to specify the output
 ** mode for XML text. This is used by the <code>XMLOutputNode</code> to
 ** describe if element text will be escaped or wrapped in a CDATA block. The
 ** mode is a three state object, the third of the states indicates whether an
 ** explicit state has been set or not.
 ** <p>
 ** If a specific state has not been set then the node will inherit its output
 ** mode from the last parent to have it set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public enum XMLOutputMode {

    /** indicates that data written will be within a CDATA block. */
    DATA
    /** indicates that data written will be escaped if required.*/
  , ESCAPE
    /** indicates that the mode will be inherited from its parent. */
  , INHERIT;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:937183657255083622")
  private static final long serialVersionUID = -1L;
}