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

    System      :   Foundation Service Extension
    Subsystem   :   OSGI Container Interface

    File        :   ServiceException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.osgi;

import java.util.List;
import java.util.Collections;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import oracle.hst.platform.osgi.function.ExceptionHandler;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class ServiceException
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Base class for all used exception across tiers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceException extends RuntimeException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2038885738850992677")
  private static final long serialVersionUID = -2310003183049987108L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the resource key to create the localized exception */
  private String code;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Skippable
  // ~~~~~~~~~ ~~~~~~~~~
  /**
   ** Tagging interface to make exceptions dicovery easier.
   */
  public interface Skippable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Method: accept
    /**
     ** Whether this should be skipped in the given context.
     ** <p>
     ** In particular for exceptions, indicates whether it can be omitted when
     ** collapsing text for the given caller (such as a task reporting an
     ** exception should not report a redundant description of a task.)
     **
     ** @param  context          the context to test.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if and only if exception
     **                          should skipped in <code>context</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    boolean accept(final Object context);
  }
  //////////////////////////////////////////////////////////////////////////////
  // class Fatal
  // ~~~~~ ~~~~~
  /**
   ** Exception indicating a fatal error, typically used in CLI routines.
   ** <br>
   ** The message supplied here should be suitable for display in a CLI response
   ** (without stack trace / exception class).
   */
  public static class Fatal extends ServiceException {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-8243133863175088963")
    private static final long serialVersionUID = 2846573076671926317L;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message.
     ** <br>
     ** The cause is not initialized, and may subsequently be initialized by a
     ** call to {@link #initCause}.
     **
     ** @param  message          the detail message saved for later retrieval by
     **                          the {@link #getMessage()} method.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public Fatal(String message) {
      // ensure inheritance
      super(message);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message and {@link Throwable} <code>cause</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The detail message associated with <code>cause</code> is <i>not</i>
     ** automatically incorporated in this runtime exception's detail message.
     **
     ** @param  message          the detail message (which is saved for later
     **                          retrieval by the {@link #getMessage()} method).
     **                          Allowed object is {@link String}.
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     */
    public Fatal(final String message, final Throwable cause) {
      // ensure inheritance
      super(message, cause);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Interrupted
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** A {@link RuntimeException} that is thrown when a Thread is interrupted.
   ** <p>
   ** This exception is useful if a Thread needs to be interrupted, but the
   ** {@link InterruptedException} can't be thrown because it is checked.
   ** <p>
   ** When the <code>Interrupt</code> exception is created, it will
   ** automatically set the interrupt status on the calling thread.
   */
  public static class Interrupted extends ServiceException {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-8682774773984381570")
    private static final long serialVersionUID = -6555476045520046321L;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** {@link InterruptedException} <code>cause</code> and a detail message of
     ** <code>(cause == null ? null : cause.toString())</code> (which typically
     ** contains the class and detail message of <code>cause</code>).
     **
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link InterruptedException}.
     */
    public Interrupted(final InterruptedException cause) {
      // ensure inheritance
      super(cause);

      // do waht's promissed
      Thread.currentThread().interrupt();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message and {@link InterruptedException} <code>cause</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The detail message associated with <code>cause</code> is <i>not</i>
     ** automatically incorporated in this runtime exception's detail message.
     **
     ** @param  message          the detail message (which is saved for later
     **                          retrieval by the {@link #getMessage()} method).
     **                          Allowed object is {@link String}.
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link InterruptedException}.
     */
    public Interrupted(final String message, final InterruptedException cause) {
      // ensure inheritance
      super(message, cause);

      // do waht's promissed
      Thread.currentThread().interrupt();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Propagated
  // ~~~~~ ~~~~~~~~~~
  public static class Propagated extends ServiceException {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-7633347486249191754")
    private static final long serialVersionUID = 626895565340579382L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final boolean embed;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** {@link Throwable} <code>cause</code> and a detail message of
     ** <code>(cause == null ? null : cause.toString())</code> (which typically
     ** contains the class and detail message of <code>cause</code>).
     **
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     */
    public Propagated(final Throwable cause) {
      // ensure inheritance
      this("" /* do not use default message as that destroys the toString */, cause);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message and {@link Throwable} <code>cause</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The detail message associated with <code>cause</code> is <i>not</i>
     ** automatically incorporated in this runtime exception's detail message.
     **
     ** @param  message          the detail message (which is saved for later
     **                          retrieval by the {@link #getMessage()} method).
     **                          Allowed object is {@link String}.
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     */
    public Propagated(final String message, final Throwable cause) {
      // ensure inheritance
      super(message, cause);

      // initailaize instance attributes
      this.embed = causeEmbedded();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message and {@link Throwable} <code>cause</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The detail message associated with <code>cause</code> is <i>not</i>
     ** automatically incorporated in this runtime exception's detail message.
     **
     ** @param  message          the detail message (which is saved for later
     **                          retrieval by the {@link #getMessage()} method).
     **                          Allowed object is {@link String}.
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     ** @param  embed            whether the cause of the {@link Throwable} is
     **                          initialized.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public Propagated(final String message, final Throwable cause, final boolean embed) {
      // ensure inheritance
      super(message, cause);

      // initailaize instance attributes
//      warnIfWrapping(cause);
      this.embed = embed;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Method: embedded
    /**
     ** Whether the cause of this exception is initialized.
     **
     ** @return                  <code>true</code> if the cause of this
     **                          exception is initialized; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean embedded() {
      return this.embed;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this {@link RuntimeException}.
     ** <p>
     ** The string representation consists of a list of the set's elements in
     ** the order they are returned by its iterator, enclosed in curly brackets
     ** (<code>"{}"</code>). Adjacent elements are separated by the characters
     ** <code>","</code> (comma). Elements are converted to strings as by
     ** <code>Object.toString()</code>.
     **
     ** @return                  a string representation of this
     **                          <code>Inserted</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return (this.embed) ? super.toString() : StringUtility.join(super.toString(), ExceptionHandler.collapseText(getCause()));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: causeEmbedded
    /**
     ** Determines if the cause of this exception is initialized.
     **
     ** @return                  <code>true</code> if the cause of this
     **                          exception is initialized; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    private boolean causeEmbedded() {
      final String causal = ExceptionHandler.collapseText(getCause());
      if (StringUtility.empty(causal))
        return false;
      return getMessage().endsWith(causal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Compound
  // ~~~~~ ~~~~~~~~~~
  public static class Compound extends ServiceException {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-6538756113029540824")
    private static final long serialVersionUID = 5216081037931837266L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final List<Throwable> causes;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message.
     ** <br>
     ** The cause is not initialized, and may subsequently be initialized by a
     ** call to {@link #initCause}.
     **
     ** @param  message          the detail message saved for later retrieval by
     **                          the {@link #getMessage()} method.
     */
    public Compound(final String message) {
      // ensure inheritance
      super(message);

      // initialize instance attributes
      this.causes = Collections.emptyList();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** {@link Throwable} <code>cause</code> and a detail message of
     ** <code>(cause == null ? null : cause.toString())</code> (which typically
     ** contains the class and detail message of <code>cause</code>).
     **
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     */
    public Compound(final Throwable cause) {
      // ensure inheritance
      super(cause);

      // initialize instance attributes
      this.causes = (cause == null) ? Collections.<Throwable>emptyList() : Collections.singletonList(cause);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message and {@link Throwable} <code>cause</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The detail message associated with <code>cause</code> is <i>not</i>
     ** automatically incorporated in this runtime exception's detail message.
     **
     ** @param  message          the detail message (which is saved for later
     **                          retrieval by the {@link #getMessage()} method).
     **                          Allowed object is {@link String}.
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     */
    public Compound(final String message, final Throwable cause) {
      // ensure inheritance
      super(message, cause);

      // initialize instance attributes
      this.causes = (cause == null) ? Collections.<Throwable>emptyList() : Collections.singletonList(cause);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message and {@link Throwable} <code>cause</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The detail message associated with <code>cause</code> is <i>not</i>
     ** automatically incorporated in this runtime exception's detail message.
     **
     ** @param  message          the detail message (which is saved for later
     **                          retrieval by the {@link #getMessage()} method).
     **                          Allowed object is {@link String}.
     ** @param  cause            the cause (which is saved for later retrieval
     **                          by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     */
    public Compound(final String message, final Iterable<? extends Throwable> cause) {
      // ensure inheritance
      this(message, CollectionUtility.empty(cause) ? null : CollectionUtility.at(cause, 0), cause);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link RuntimeException} exception with the specified
     ** detail message and {@link Throwable} <code>cause</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The detail message associated with <code>cause</code> is <i>not</i>
     ** automatically incorporated in this runtime exception's detail message.
     **
     ** @param  message          the detail message (which is saved for later
     **                          retrieval by the {@link #getMessage()} method).
     **                          Allowed object is {@link String}.
     ** @param  primary          the primary cause (which is saved for later
     **                          retrieval by the {@link #getCause()} method).
     **                          <br>
     **                          (A <code>null</code> value is permitted, and
     **                          indicates that the cause is nonexistent or
     **                          unknown.)
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     ** @param  secondary        the collection of additional causes.
     **                          <br>
     **                          Allowed object is {@link Iterable}.
     */
    public Compound(final String message, final Throwable primary, final Iterable<? extends Throwable> secondary) {
      // ensure inheritance
      super(message, primary);

      // initialize instance attributes
      this.causes = StreamSupport.stream(secondary.spliterator(), false).collect(Collectors.toList());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@link RuntimeException} exception with <code>null</code>
   ** as its detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   */
  protected ServiceException() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@link RuntimeException} exception with the specified
   ** detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the detail message saved for later retrieval by
   **                            the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected ServiceException(final String message) {
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@link RuntimeException} exception with the specified
   ** detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
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
  protected ServiceException(final String message, final Throwable cause) {
    // ensure inheritance
    super(message, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@link RuntimeException} exception with the specified
   ** {@link Throwable} <code>cause</code> and a detail message of
   ** <code>(cause == null ? null : cause.toString())</code> (which typically
   ** contains the class and detail message of <code>cause</code>). This
   ** constructor is useful for runtime exceptions that are little more than
   ** wrappers for other throwables.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected ServiceException(final Throwable cause) {
    // ensure inheritance
    super(cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Acccessor and Mutator method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Sets the code this exception is related to.
   ** <p>
   ** Makes the exception interpretable by the associated code without parsing
   ** the message.
   **
   ** @param  value              the code this exception is related to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  protected final ServiceException code(final String value) {
    this.code = value;
    return this;
  }

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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** specified detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the detail message (which is saved for later
   **                            retrieval by the {@link #getMessage()} method).
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException error(String message) {
    return new ServiceException(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** specified detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the detail message (which is saved for later
   **                            retrieval by the {@link #getMessage()} method).
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException error(String code, String message) {
    return new ServiceException(message).code(code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Factory method to create a new <code>ServiceException</code> exception
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
   ** @return                    the <code>ServiceException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException error(final String message, final Throwable cause) {
    return new ServiceException(message, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Factory method to create a new <code>ServiceException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   ** @return                    the <code>ServiceException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException error(final String code, final String message, final Throwable cause) {
    return new ServiceException(message, cause).code(code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Factory method to create a new <code>ServiceException</code> exception
   ** with the specified detail message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  <T>                the expected type of the exceeption.
   ** @param  clazz              the spcific type of the
   **                            <code>ServiceException</code> to create.
   **                            Allowed object is {@link Class}.
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
   ** @return                    the <code>ServiceException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static <T extends ServiceException> T error(final Class<T> clazz, final String message, final Throwable cause) {
    T exception = null;
    try {
      exception = clazz.getConstructor(String.class, Throwable.class)
        .newInstance(message, cause);
    }
    catch (Exception e) {
      throw new ServiceException("Invalid Exception Type, " + e.getMessage(), e);
    }
    return exception;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** <code>code</code> keyword.
   **
   ** @param  <T>                the expected type of the exceeption.
   ** @param  clazz              the spcific type of the
   **                            <code>ServiceException</code> to create.
   **                            Allowed object is {@link Class}.
   ** @param  code               the code this exception is related to.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   ** @return                    the <code>ServiceException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static <T extends ServiceException> T error(final Class<T> clazz, final String code, final String message, final Throwable cause) {
    T t = null;
    try {
      t = clazz.getConstructor(String.class, String.class, Throwable.class).newInstance(code, message, cause);
    }
    catch (Exception e) {
      throw new ServiceException("Invalid Exception Type, " + e.getMessage(), e);
    }
    return t;
  }
}