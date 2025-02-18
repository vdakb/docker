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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   FeatureException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common;

import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class FeatureException
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>AbstractContext</code> if any goes
 ** wrong during execution of a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FeatureException extends ServiceException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3870016773785711404")
  private static final long serialVersionUID = -2508323827867188183L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FeatureException</code> from a message code.
   **
   ** @param  code               the resource key for the exception message.
   */
  public FeatureException(final String code) {
    // ensure inheritance
    super(FeatureResourceBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FeatureException</code> from a message code.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substiturion for placholder contained in
   **                            the message regarding to <code>code</code>.
   */
  public FeatureException(final String code, final String parameter) {
    // ensure inheritance
    super(FeatureResourceBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FeatureException</code> from a message code.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substiturions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public FeatureException(final String code, final String[] parameter) {
    // ensure inheritance
    super(FeatureResourceBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FeatureException</code> and passes it the causing
   ** exception.
   **
   ** @param  causing            the causing exception.
   */
  public FeatureException(final Throwable causing) {
    super(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FeatureException</code> and passes it the causing
   ** exception.
   **
   ** @param  code               the resource key for the detail message.
   ** @param  causing            the causing exception.
   */
  public FeatureException(final String code, final Throwable causing) {
    // ensure inheritance
    super(FeatureResourceBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FeatureException</code> and passes it the causing
   ** exception.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public FeatureException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(FeatureResourceBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FeatureException</code> and passes it the causing
   ** exception.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public FeatureException(final String code, final String[] parameter, final Throwable causing) {
    // ensure inheritance
    super(FeatureResourceBundle.RESOURCE, code, parameter, causing);
  }
}