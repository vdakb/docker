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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   InvalidSignaturenException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    InvalidSignaturenException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

////////////////////////////////////////////////////////////////////////////////
// class InvalidSignaturenException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception throws if the JSON Web Token was properly constructed but the
 ** signature is invalid.
 ** <p>
 ** This token should not be trusted.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class InvalidSignaturenException extends TokenException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1657773724977246470")
  private static final long serialVersionUID = -4585621262816394884L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new runtime exception with <code>null</code> as its detail
   ** message. The cause is not initialized, and may subsequently be initialized
   ** by a call to {@link #initCause}.
   */
  public InvalidSignaturenException() {
    // ensure inheritance
    super();
  }
}