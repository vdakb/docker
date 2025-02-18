/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   ServiceException.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    ServiceException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.error;

/**
 * The ServiceException to wrap and localize OIM exceptions.
 */
public class ServiceException extends Exception {

  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long serialVersionUID = -3526037670697482371L;

  /** code that refers to the error what happened */
  private final String code;

  /** parameters of the message */
  private final Object[] params;

  /**
   * Constructs a {@link ServiceException} instance.
   * @param code the code that refers to the error
   */
  public ServiceException(final String code) {
    this(code, null, new Object[]{});
  }

  /**
   * Constructs a {@link ServiceException} instance.
   * @param code the code that refers to the error
   * @param params parameters of the message
   */
  public ServiceException(final String code, Object... params) {
    this(code, null, params);
  }

  /**
   * Constructs a {@link ServiceException} instance.
   * @param code the code that refers to the error
   * @param cause the cause of the exception
   */
  public ServiceException(final String code, final Throwable cause) {
    this(code, cause, new Object[]{});
  }

  /**
   * Constructs a {@link ServiceException} instance.
   * @param code the code that refers to the error
   * @param cause the cause of the exception
   * @param params parameters of the message
   */
  public ServiceException(final String code, final Throwable cause, Object... params) {
    super(cause);
    this.code = code;
    this.params = params;
  }

  /**
   * Returns the error code of the exception.
   * @return the error code of the exception.
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the parameters of the message.
   * @return the parameters of the message.
   */
  public Object[] getParams() {
    return params;
  }
}
