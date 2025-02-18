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
    Subsystem   :   Common Shared Data Access Facilities

    File        :   DataAccessException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DataAccessException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.utility.resource.DataAccessBundle;

////////////////////////////////////////////////////////////////////////////////
// class DataAccessException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>DataAccess</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class DataAccessException extends TaskException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2876777013807702084")
  private static final long serialVersionUID = -61872902687738707L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DataAccessException</code> from a message code.
   **
   ** @param  code               the resource key for the exception message.
   */
  public DataAccessException(String code) {
    // ensure inheritance
    super(DataAccessBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DataAccessException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public DataAccessException(final String code, final String parameter) {
    // ensure inheritance
    super(DataAccessBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DataAccessException</code> from a messageCode.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public DataAccessException(String code, String[] parameter) {
    // ensure inheritance
    super(DataAccessBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DataAccessException</code> and passes it the parent
   ** exception.
   **
   ** @param  causing            the causing exception.
   */
  public DataAccessException(final Throwable causing) {
    // ensure inheritance
    super(DataAccessBundle.RESOURCE, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DataAccessException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public DataAccessException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(DataAccessBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DataAccessException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public DataAccessException(final String code, final String[] parameter, final Throwable causing) {
    // ensure inheritance
    super(DataAccessBundle.RESOURCE, code, parameter, causing);
  }
}