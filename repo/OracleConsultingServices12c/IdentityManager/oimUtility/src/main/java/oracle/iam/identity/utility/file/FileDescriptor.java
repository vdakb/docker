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

    File        :   FileDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FileDescriptor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

////////////////////////////////////////////////////////////////////////////////
// abstract class FileDescriptor
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This is the superclass for the meta information about queryable and mutable
 ** attributes in a common file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
abstract class FileDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the code for a transaction marker in the flatfile after the comparator has
   ** proceed the incomming file.
   */
  public static final String TRANSACTION = "TRX";

  /** the code to mark a create transaction. */
  public static final String NOTHING      = "NIL";

  /** the code to mark a create transaction. */
  public static final String CREATE      = "CRE";

  /** the code to mark a update transaction. */
  public static final String UPDATE      = "UPD";

  /** the code to mark a delete transaction. */
  public static final String DELETE      = "DEL";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FileDscriptor</code>.
   */
  protected FileDescriptor() {
    // ensure inheritance
    super();
  }
}