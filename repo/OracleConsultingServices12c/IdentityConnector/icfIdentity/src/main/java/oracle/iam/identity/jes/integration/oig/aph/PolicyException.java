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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   BKA Access Policy Holder

    File        :   PolicyException.java

    Compiler    :   JDK 1.8

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file extends the class
                    TaskException.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.09.2024  TSebo    First release version
*/
package oracle.iam.identity.jes.integration.oig.aph;

import oracle.iam.identity.jes.integration.oig.aph.resource.PolicyBundle;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// final class PolicyException
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>APH Policy Evaluation</code> operations if
 ** unexpected behaviour occourse.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PolicyException extends TaskException {
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1318516732453048380")
  private static final long serialVersionUID = 5145131519521277804L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessException</code> from a message code.
   **
   ** @param  code               the resource key for the exception message.
   */
  public PolicyException(final String code) {
    // ensure inheritance
    super(PolicyBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public PolicyException(final String code, final String parameter) {
    // ensure inheritance
    super(PolicyBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessException</code> from a code.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public PolicyException(final String code, final String... parameter) {
    // ensure inheritance
    super(PolicyBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessException</code> and passes it the causing
   ** exception.
   **
  ** @param  code               the resource key for the exception message.
   ** @param  causing            the causing exception.
   */
  public PolicyException(final String code, final Throwable causing) {
    // ensure inheritance
    super(PolicyBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public PolicyException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(PolicyBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public PolicyException(final String code, final String[] parameter, final Throwable causing) {
    // ensure inheritance
    super(PolicyBundle.RESOURCE, code, parameter, causing);
  }
}
