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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   PersistenceException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    PersistenceException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import javax.ejb.ApplicationException;

////////////////////////////////////////////////////////////////////////////////
// class PersistenceException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>JPA</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ApplicationException(rollback=true)
public class PersistenceException extends javax.persistence.PersistenceException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6069747507058142305")
  private static final long serialVersionUID = -5626707476036644788L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The resource key to create the localized exception. */
  private final String            code;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>PersistenceException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the array of substitution parameters.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  protected PersistenceException(final String code, final Object... arguments) {
    // ensure inheritance
    this(code, PersistenceBundle.string(code, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>PersistenceException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the error message for this JDBC exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected PersistenceException(final String code, final String message) {
    // ensure inheritance
    super(message);

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>PersistenceException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
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
   ** @param  arguments          the array of substitution parameters.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  protected PersistenceException(final String code, final Throwable cause, final Object... arguments) {
    // ensure inheritance
    super(PersistenceBundle.string(code, arguments), cause);

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>PersistenceException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the error message for this JDBC exception.
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
  protected PersistenceException(final String code, final String message, final Throwable cause) {
    // ensure inheritance
    super(message, cause);

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Acccessor and Mutator method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Return the resource key this exception is related to.
   ** <p>
   ** Makes the exception interpretable by the associated code without parsing
   ** the message
   **
   ** @return                    the resource key this exception is related to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String code() {
    return this.code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Return the description this exception is related to.
   ** <p>
   ** Makes the exception interpretable by the associated code without parsing
   ** the message
   **
   ** @return                    the description this exception is related to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return PersistenceBundle.string(this.code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** internal error keyword.
   **
   ** @param  message            the error message for this JPA exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException internal(final String message) {
    return new PersistenceException(PersistenceError.UNHANDLED, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** internal error keyword.
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
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException internal(final Throwable cause) {
    return internal(cause.getLocalizedMessage(), cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** internal error keyword.
   **
   ** @param  message            the error message for this JPA exception.
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
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException internal(final String message, final Throwable cause) {
    cause.printStackTrace(System.err);
    return new PersistenceException(PersistenceError.UNHANDLED, message, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** abort error keyword.
   **
   ** @param  message            the error message for this JPA exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException abort(final String message) {
    return new PersistenceException(PersistenceError.ABORT, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** abort error keyword.
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
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException abort(final Throwable cause) {
    return abort(cause.getLocalizedMessage(), cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** abort error keyword.
   **
   ** @param  message            the error message for this JPA exception.
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
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException abort(final String message, final Throwable cause) {
    cause.printStackTrace(System.err);
    return new PersistenceException(PersistenceError.ABORT, message, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** general error keyword.
   **
   ** @param  message            the error message for this JPA exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException general(final String message) {
    return new PersistenceException(PersistenceError.GENERAL, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** general error keyword.
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
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException general(final Throwable cause) {
    return general(cause.getLocalizedMessage(), cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** general error keyword.
   **
   ** @param  message            the error message for this JPA exception.
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
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException general(final String message, final Throwable cause) {
    cause.printStackTrace(System.err);
    return new PersistenceException(PersistenceError.GENERAL, message, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notCreatable
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-creatable error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notCreatable(final Class<?> entity) {
    return notCreatable(entity, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notCreated
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-creatable error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the create operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notCreated(final Class<?> entity, final Integer id) {
    return notCreated(entity, String.valueOf(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notCreated
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-created error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the create operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notCreated(final Class<?> entity, final Long id) {
    return notCreated(entity, String.valueOf(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notCreated
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-created error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the create operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notCreated(final Class<?> entity, final String id) {
    return new PersistenceException(PersistenceError.NOT_CREATED, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notCreatable
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-created error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  message            the error message for this JPA exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notCreatable(final Class<?> entity, final String message) {
    return new PersistenceException(PersistenceError.NOT_CREATABLE, entity, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notModifiable
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-modifiable error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notModifiable(final Class<?> entity) {
    return notModifiable(entity, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notModifiable
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-modifiable error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  message            the error message for this JPA exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notModifiable(final Class<?> entity, final String message) {
    return new PersistenceException(PersistenceError.NOT_MODIFIABLE, entity, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notModified
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-modified error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the modify operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notModified(final Class<?> entity, final Integer id) {
    return notModified(entity, String.valueOf(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notModified
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-modified error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the modify operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notModified(final Class<?> entity, final Long id) {
    return notModified(entity, String.valueOf(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notModified
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-modified error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the modify operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notModified(final Class<?> entity, final String id) {
    return new PersistenceException(PersistenceError.NOT_MODIFIED, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notDeletable
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-deletable error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notDeletable(final Class<?> entity) {
    return notDeletable(entity, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notDeletable
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-deletable error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  message            the error message for this JPA exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notDeletable(final Class<?> entity, final String message) {
    return new PersistenceException(PersistenceError.NOT_DELETABLE, entity, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notDeleted
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-deleted error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the delete operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notDeleted(final Class<?> entity, final Integer id) {
    return notDeleted(entity, String.valueOf(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notDeleted
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-deleted error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the delete operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notDeleted(final Class<?> entity, final Long id) {
    return notDeleted(entity, String.valueOf(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notDeleted
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-deleted error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the delete operation
   **                            failed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notDeleted(final Class<?> entity, final String id) {
    return new PersistenceException(PersistenceError.NOT_DELETED, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** already-exists error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the operation failed.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException exists(final Class<?> entity, final Integer id) {
    return new PersistenceException(PersistenceError.EXISTS, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** already-exists error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the operation failed.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException exists(final Class<?> entity, final Long id) {
    return new PersistenceException(PersistenceError.EXISTS, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** already-exists error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the operation failed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException exists(final Class<?> entity, final String id) {
    return new PersistenceException(PersistenceError.EXISTS, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notExists
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-exists error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the operation failed.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notExists(final Class<?> entity, final Integer id) {
    return new PersistenceException(PersistenceError.NOT_EXISTS, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notExists
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-exists error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the operation failed.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notExists(final Class<?> entity, final Long id) {
    return new PersistenceException(PersistenceError.NOT_EXISTS, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notExists
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-exists error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the operation failed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notExists(final Class<?> entity, final String id) {
    return new PersistenceException(PersistenceError.NOT_EXISTS, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ambiguous
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** ambiguous error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the operation failed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException ambiguous(final Class<?> entity, final String id) {
    return new PersistenceException(PersistenceError.AMBIGUOUS, entity, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Factory method to create a new <code>PersistenceException</code> with the
   ** not-null error keyword.
   **
   ** @param  entity             the entity {@link Class} this exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  id                 the entity identifier the operation failed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PersistenceException} wrapping the
   **                            JPA status.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceException</code>.
   */
  public static PersistenceException notNull(final Class<?> entity, final String id) {
    return new PersistenceException(PersistenceError.NOT_NULL, entity, id);
  }
}