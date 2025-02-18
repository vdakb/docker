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

    File        :   FlatFileException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.utility.resource.FlatFileBundle;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileException
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from flat file operations if instantiating of
 ** the reader/writer fails.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public final class FlatFileException extends TaskException {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FlatFileException</code> from a message code.
   **
   ** @param  code               the resource key for the exception message.
   */
  public FlatFileException(final String code) {
    // ensure inheritance
    super(FlatFileBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FlatFileException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public FlatFileException(final String code, final String parameter) {
    // ensure inheritance
    super(FlatFileBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FlatFileException</code> from a code.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public FlatFileException(final String code, final String[] parameter) {
    // ensure inheritance
    super(FlatFileBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FlatFileException</code> and passes it the causing
   ** exception.
   **
   ** @param  causing            the causing exception.
   */
  public FlatFileException(final Throwable causing) {
    // ensure inheritance
    super(FlatFileBundle.RESOURCE, FlatFileError.UNHANDLED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FlatFileException</code> and passes it the causing
   ** exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  causing            the causing exception.
   */
  public FlatFileException(final String code, final Throwable causing) {
    // ensure inheritance
    super(FlatFileBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FlatFileException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public FlatFileException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(FlatFileBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FlatFileException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
    public FlatFileException(final String code, final String[] parameter, final Throwable causing) {
    // ensure inheritance
    super(FlatFileBundle.RESOURCE, code, parameter, causing);
  }
}