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
    Subsystem   :   Identity Governance Service

    File        :   AssertionException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity;

import java.util.List;
import java.util.Set;

import javax.ws.rs.ForbiddenException;

////////////////////////////////////////////////////////////////////////////////
// class AssertionException
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception used for all autentication errors across tiers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AssertionException extends ForbiddenException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The error keyword that something went wrong. */
  private static final String SEVER           = "fatal";

  /** The error keyword that indicates authorization failed. */
  private static final String AUTHZ           = "unauthorized";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1824233674045969061")
  private static final long  serialVersionUID = -7681388553683833717L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@link ForbiddenException} exception with the specified
   ** detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  type               the REST detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the detail message saved for later retrieval by
   **                            the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private AssertionException(final String type, final String message) {
    // ensure inheritance
    super(message);

    // initialize instance
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@link ForbiddenException} exception with the specified
   ** detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  type               the REST detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the detail message (which is saved for later
   **                            retrieval by the {@link #getMessage()} method).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  private AssertionException(final String type, final String message, final Throwable cause) {
    // ensure inheritance
    super(message, cause);

    // initialize instance
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@link ForbiddenException} exception with the specified
   ** {@link Throwable} <code>cause</code> and a detail message of
   ** <code>(cause == null ? null : cause.toString())</code> (which typically
   ** contains the class and detail message of <code>cause</code>). This
   ** constructor is useful for runtime exceptions that are little more than
   ** wrappers for other throwables.
   **
   ** @param  type               the REST detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  private AssertionException(final String type, final Throwable cause) {
    // ensure inheritance
    super(cause);

    // initialize instance
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type kexword for this exception.
   **
   ** @return                    the type kexword for this exception.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new <code>AssertionException</code> with the
   ** specified detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the detail message (which is saved for later
   **                            retrieval by the {@link #getMessage()} method).
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException unhandled(final String message) {
    return new AssertionException(SEVER, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new <code>AssertionException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException unhandled(final Throwable cause) {
    return new AssertionException(SEVER, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new <code>AssertionException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  message            the detail message (which is saved for later
   **                            retrieval by the {@link #getMessage()} method).
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException unhandled(final String message, final Throwable cause) {
    return new AssertionException(SEVER, message, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>AssertionException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException abort(final Throwable cause) {
    return new AssertionException(AUTHZ, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenExpired
  /**
   ** Factory method to create a new <code>AssertionException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException tokenExpired() {
    return new AssertionException(AUTHZ, "Token expired.");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenUsedBefore
  /**
   ** Factory method to create a new <code>AssertionException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException tokenUsedBefore() {
    return new AssertionException(AUTHZ, "Token before use time.");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requireClaim
  /**
   ** Factory method to create a new <code>AssertionException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  required           the claims required for token verification.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException requireClaim(final Set<String> required) {
    return new AssertionException(AUTHZ, "Token missing required claims: " + required);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requireAudience
  /**
   ** Factory method to create a new <code>AssertionException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException requireAudience() {
    return new AssertionException(AUTHZ, "Token missing required audience claim");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rejectAudience
  /**
   ** Factory method to create a new <code>AssertionException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  rejected           the audience claims rejected for token
   **                            verification.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException rejectAudience(final List<String> rejected) {
    return new AssertionException(AUTHZ, "Token audience rejected: " + rejected);
  }
}