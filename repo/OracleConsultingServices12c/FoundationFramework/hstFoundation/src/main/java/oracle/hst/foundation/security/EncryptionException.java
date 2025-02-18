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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared security functions

    File        :   EncryptionException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EncryptionException.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

////////////////////////////////////////////////////////////////////////////////
// final class EncryptionException
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** General exception thrown when any errors are raised during encryption, etc.
 ** <p>
 ** It is intended to provide very little information (if any) of the error
 ** causes, so that encryption internals are not revealed through error
 ** messages.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public final class EncryptionException extends RuntimeException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6671782929659858267")
  private static final long serialVersionUID = -2166063571341010143L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EncryptionException</code> with <code>null</code> as
   ** its detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   ** <p>
   ** Default Constructor
   */
  public EncryptionException() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EncryptionException</code> with <code>reason</code> as
   ** its detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  reason             the detail message.
   **                            <br>
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   */
  public EncryptionException(final String reason) {
    // ensure inheritance
    super(reason);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EncryptionException</code> and passes it the causing
   ** exception.
   **
   ** @param  causing            the causing exception.
   */
  public EncryptionException(final Throwable causing) {
    // ensure inheritance
    super(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EncryptionException</code> with <code>reason</code> as
   ** the detail message and the causing exception.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this exception's detail message.
   **
   ** @param  reason             the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   ** @param  causing            the causing exception.
   */
  public EncryptionException(final String reason, final Throwable causing) {
    super(reason, causing);
  }
}