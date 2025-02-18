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

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   dieter.steding@icloud.com.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    dieter.steding@icloud.com.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/

package bka.iam.access.oidc.extension;

import bka.iam.access.oidc.extension.model.ErrorMessage;

////////////////////////////////////////////////////////////////////////////////
// class ProcessingException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from operations interacting with the Access
 ** Manager REST API's.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProcessingException extends Exception {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("oracle.jdeveloper.java.serialversionuid-stale")
  private static final long serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient final ErrorMessage error;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessingException</code> from an error response.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  error              the error response.
   **                            <br>
   **                            Allowed object is {@link ErrorMessage}.
   */
  private ProcessingException(final ErrorMessage error) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.error = error;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Returns the error for this exception.
   **
   ** @return                    the error for this exception.
   **                            <br>
   **                            Possible object is {@link ErrorMessage}.
   */
  public final ErrorMessage error() {
    return this.error;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>ProcessingException</code> from an
   ** {@link ErrorMessage}.
   **
   ** @param  type               the REST detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  description        the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ProcessingException</code> populated
   **                            with the provided {@link ErrorMessage}.
   **                            <br>
   **                            Possible object is 
   **                            <code>ProcessingException</code>.
   */
  public static ProcessingException of(final String type, final String description) {
    return of(ErrorMessage.of(type, description));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>ProcessingException</code> from an
   ** {@link ErrorMessage}.
   **
   ** @param  error              the error of the error response.
   **                            <br>
   **                            Allowed object is {@link ErrorMessage}.
   **
   ** @return                    the <code>ProcessingException</code> populated
   **                            with the provided {@link ErrorMessage}.
   **                            <br>
   **                            Possible object is 
   **                            <code>ProcessingException</code>.
   */
  public static ProcessingException of(final ErrorMessage error) {
    return new ProcessingException(error);
  }
}