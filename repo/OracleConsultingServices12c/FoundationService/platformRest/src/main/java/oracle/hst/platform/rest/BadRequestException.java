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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   BadRequestException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    BadRequestException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest;

////////////////////////////////////////////////////////////////////////////////
// class BadRequestException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>REST</code> operations.
 ** <br>
 ** Signals an error while looking up resources and attributes.
 ** <p>
 ** This exception corresponds to HTTP response code 400 BAD REQUEST.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class BadRequestException extends ProcessingException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The error keyword that indicates the specified filter syntax was invalid.
   */
  public static final String INVALID_FILTER   = "invalidFilter";

  /**
   ** The error keyword that indicates the request body message structure was
   ** invalid or did not conform to the request schema.
   */
  public static final String INVALID_SYNTAX   = "invalidSyntax";

  /**
   ** The error keyword that indicates the the path attribute was invalid or
   ** malformed.
   */
  public static final String INVALID_PATH     = "invalidPath";

  /**
   ** The error keyword that indicates the specified path did not yield an
   ** attribute or attribute value that could be operated on.
   */
  public static final String INVALID_TARGET   = "invalidTarget";

  /**
   ** The error keyword that indicates a required value was missing, or the
   ** value specified was not compatible with the operation or attribute type.
   */
  public static final String INVALID_VALUE    = "invalidValue";

  /**
   ** The error keyword that indicates the specified REST protocol version is
   ** not supported.
   */
  public static final String INVALID_VERSION  = "invalidVersion";

  /**
   ** The error keyword that indicates the URL encoding is not supported.
   */
  public static final String INVALID_ENCODING  = "invalidEncoding";

  /**
   ** The error keyword that indicates an entity is empty.
   */
  public static final String ENTITY_EMPTY      = "entityEmpty";

  /**
   ** The error keyword that indicates the specified filter yields many more
   ** results than the server is willing to calculate or process.
   */
  public static final String TOO_MANY         = "tooMany";

  /**
   ** The error keyword that indicates the attempted modification is not
   ** compatible with the target attributes mutability or current state.
   */
  public static final String MUTABILITY       = "mutability";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2310724544724321857")
  private static final long  serialVersionUID = 559538147210889423L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>BadRequestException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  type               the REST detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private BadRequestException(final String type, final String message) {
    // ensure inheritance
    super(400, type, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidFilter
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** invalidFilter error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException invalidFilter(final String message) {
    return new BadRequestException(INVALID_FILTER, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidSyntax
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** invalidSyntax error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException invalidSyntax(final String message) {
    return new BadRequestException(INVALID_SYNTAX, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidPath
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** invalidPath error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException invalidPath(final String message) {
    return new BadRequestException(INVALID_PATH, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidTarget
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** invalidTarget error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException invalidTarget(final String message) {
    return new BadRequestException(INVALID_TARGET, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidValue
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** invalidValue error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException invalidValue(final String message) {
    return new BadRequestException(INVALID_VALUE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidVersion
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** invalidVersion error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException invalidVersion(final String message) {
    return new BadRequestException(INVALID_VERSION, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidEncoding
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** invalidEncoding error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException invalidEncoding(final String message) {
    return new BadRequestException(INVALID_ENCODING, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityEmpty
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** entityEmpty error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException entityEmpty(final String message) {
    return new BadRequestException(ENTITY_EMPTY, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tooMany
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** tooMany error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException tooMany(final String message) {
    return new BadRequestException(TOO_MANY, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutability
  /**
   ** Factory method to create a new <code>BadRequestException</code> with the
   ** mutability error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>BadRequestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>BadRequestException</code>.
   */
  public static BadRequestException mutability(final String message) {
    return new BadRequestException(MUTABILITY, message);
  }
}