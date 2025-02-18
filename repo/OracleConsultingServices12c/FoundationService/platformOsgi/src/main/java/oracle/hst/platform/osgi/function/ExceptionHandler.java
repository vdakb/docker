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

    File        :   ExceptionHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ExceptionHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.osgi.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import java.util.function.Supplier;

import java.util.concurrent.ExecutionException;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.osgi.ServiceException;
import oracle.hst.platform.osgi.ServiceException.Compound;
import oracle.hst.platform.osgi.ServiceException.Skippable;
import oracle.hst.platform.osgi.ServiceException.Propagated;
import oracle.hst.platform.osgi.ServiceException.Interrupted;

////////////////////////////////////////////////////////////////////////////////
// abstract class ExceptionHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Static utility methods pertaining to instances of {@link Throwable}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ExceptionHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int RECURSIVE_DEPTH = 100;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Suppress
  // ~~~~~~~~~ ~~~~~~~~
  static interface Suppress {

    //////////////////////////////////////////////////////////////////////////
    // static final attributes
    //////////////////////////////////////////////////////////////////////////

    /**
     ** {@link Throwable} types whose existence is annoying in a
     ** <b>message</b>.
     */
    static final List<Class<? extends Throwable>> Always  = Collections.<Class<? extends Throwable>>unmodifiableList(
      Arrays.asList(
        ExecutionException.class
      , InvocationTargetException.class
      , UndeclaredThrowableException.class
      )
    );

    /**
     ** Same as {@link #Type} but might carry an interesting message.
     */
    static final List<Class<? extends Throwable>> Message = Collections.<Class<? extends Throwable>>unmodifiableList(
      Arrays.asList(
        Propagated.class
      )
    );

    static final List<Class<? extends Throwable>> Prefix  = Collections.<Class<? extends Throwable>>unmodifiableList(
      Arrays.asList(
        ClassCastException.class
      , Compound.class
      , Propagated.class
      )
    );

    static final List<Class<? extends Throwable>> Runtime = Collections.<Class<? extends Throwable>>unmodifiableList(
      Arrays.asList(
        RuntimeException.class
      , Exception.class
      , Throwable.class
      , IllegalStateException.class
      , IllegalArgumentException.class
      )
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Collapse
  // ~~~~~ ~~~~~~~~
  public static class Collapse implements Supplier<String> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Throwable cause;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Collapse</code> that a one-line message suitable for
     ** logging without traces leveraing the provided {@link Throwable}.
     **
     ** @param  cause            the {@link Throwable} this {@link Supplier} use
     **                          as the source.
     **                          <br>
     **                          Allowed object is {@link Throwable}.
     */
    public Collapse(final Throwable cause) {
      // ensure inheritance
      super();

      //initalize instance attributes
      this.cause = cause;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: cause
    /**
     ** Returns the source of this {@link Supplier}.
     **
     ** @return                  the source of this {@link Supplier}.
     **                          <br>
     **                          possible object is {@link Throwable}.
     */
    public final Throwable cause() {
      return this.cause;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: get (Supplier)
    /**
     ** Returns the result of the promise.
     **
     ** @return                  the result of the promise.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
     public String get() {
      return ExceptionHandler.collapseText(cause);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ExceptionHandler</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ExceptionHandler()" and enforces use of the public method below.
   */
  private ExceptionHandler() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isFatal
  /**
   ** Determines whether the {@link Throwable} is "fatal" - i.e. in normal
   ** programming, should not be caught but should instead be propagating so
   ** the call-stack fails.
   ** <br>
   ** For example, an interrupt should cause the task to abort rather than
   ** catching and ignoring (or "handling" incorrectly).
   **
   ** @param  throwable          the {@link Throwable} to verify.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    <code>true</code> is {@link Throwable} is one
   **                            of
   **                            <ul>
   **                              <li>Error
   **                              <li>Interrupted
   **                              <li>InterruptedException
   **                            </ul>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean isFatal(final Throwable throwable) {
    return (throwable instanceof InterruptedException) || (throwable instanceof Interrupted) || (throwable instanceof Error);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Propagate exceptions which are fatal.
   ** <p>
   ** Propagates only those exceptions which one rarely (if ever) wants to
   ** capture, such as {@link InterruptedException} and {@link Error}s.
   **
   ** @param  cause              the {@link Throwable} <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public static void fatal(final Throwable cause) {
    interrupted(cause);
    if (cause instanceof Error) {
      throw (Error)cause;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   interrupted
  /**
   ** Propagate exceptions which are interrupts (be it
   ** {@link InterruptedException} or {@link Interrupted}.
   **
   ** @param  cause              the {@link Throwable} <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public static void interrupted(final Throwable cause) {
    if (cause instanceof InterruptedException) {
      throw new Interrupted((InterruptedException)cause);
    }
    else if (cause instanceof Interrupted) {
      Thread.currentThread().interrupt();
      throw (Interrupted)cause;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Propagate a {@link RuntimeException} derived from a bulk of
   ** {@link Throwable}s.
   **
   ** @param  bulk               the {@link Throwable}s <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link Throwable}.
   */
  public static void propagate(final Iterable<? extends Throwable> bulk) {
    throw propagate(create(bulk));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Propagate a {@link RuntimeException} derived from a bulk of
   ** {@link Throwable}s.
   **
   ** @param  prefix             the prefix for detail message (which is saved
   **                            for later retrieval by the
   **                            {@link Throwable#getMessage()} method).
   **                            Allowed object is {@link String}.
   ** @param  bulk               the {@link Throwable}s <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link Throwable}.
   **
   ** @return                    the {@link RuntimeException} ready for
   **                            propagation.
   **                            <br>
   **                            Allowed object is {@link RuntimeException}.
   */
  public static RuntimeException propagate(final String prefix, final Iterable<? extends Throwable> bulk) {
    throw propagate(create(prefix, bulk));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Propagate a {@link RuntimeException} derived from a {@link Throwable}.
   ** <p>
   ** Like Guava <code>Throwables#propagate(Throwable)</code> but:
   ** <ul>
   **   <li> throws {@link Interrupted} to handle {@link InterruptedException}s
   **   <li> wraps as {@link Propagated} for easier filtering
   ** </ul>
   **
   ** @param  cause              the {@link Throwable} <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link RuntimeException} ready for
   **                            propagation.
   **                            <br>
   **                            Allowed object is {@link RuntimeException}.
   */
  public static RuntimeException propagate(final Throwable cause) {
    if (cause instanceof InterruptedException) {
      throw new Interrupted((InterruptedException)cause);
    }
    else if (cause instanceof Interrupted) {
      Thread.currentThread().interrupt();
      throw (Interrupted)cause;
    }
/*
    Throwables.propagateIfPossible(checkNotNull(throwable));
*/
    throw new Propagated(cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Propagate a {@link RuntimeException} derived from a {@link Throwable}.
   ** <p>
   ** The given message is included <b>only</b> if the given {@link Throwable}
   ** needs to be wrapped; otherwise the message is not used.
   ** <br>
   ** To always include the message, use {@link #propagateAnnotated(String, Throwable)}.
   **
   ** @param  message            the detail message saved for later retrieval by
   **                            the {@link Throwable#getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the {@link Throwable} <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link RuntimeException} ready for
   **                            propagation.
   **                            <br>
   **                            Allowed object is {@link RuntimeException}.
   **
   ** @see    #propagate(Throwable)
   */
  public static RuntimeException propagate(final String message, final Throwable cause) {
    return propagate(message, cause, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagateAnnotated
  /**
   ** Propagate a {@link Throwable} as a {@link RuntimeException}.
   ** <p>
   ** Same as {@link #propagate(String, Throwable)} but unlike earlier
   ** deprecated version this always re-wraps including the given message, until
   ** semantics of that method change to match this.
   ** <br>
   ** See {@link #propagate(String, Throwable)} if the message should be omitted
   ** and the given throwable preserved if it can already be propagated.
   **
   ** @param  message            the detail message saved for later retrieval by
   **                            the {@link Throwable#getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the {@link Throwable} <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link RuntimeException} ready for
   **                            propagation.
   **                            <br>
   **                            Allowed object is {@link RuntimeException}.
   **
   ** @see    #propagate(String, Throwable)
   */
  public static RuntimeException propagateAnnotated(final String message, final Throwable cause) {
    return propagate(message, cause, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create a {@link RuntimeException} from the given bulk of
   ** {@link Throwable}s, but without propagating it, for use when caller will
   ** be wrapping
   **
   ** @param  bulk               the {@link Throwable}s <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link Throwable}.
   **
   ** @return                    the {@link RuntimeException} ready for
   **                            propagation.
   **                            <br>
   **                            Allowed object is {@link RuntimeException}.
   */
  public static Throwable create(final Iterable<? extends Throwable> bulk) {
    return create(null, bulk);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create a {@link RuntimeException} from the given bulk of
   ** {@link Throwable}s, but without propagating it, for use when caller will
   ** be wrapping
   **
   ** @param  prefix             the prefix for detail message (which is saved
   **                            for later retrieval by the
   **                            {@link Throwable#getMessage()} method).
   **                            Allowed object is {@link String}.
   ** @param  bulk               the {@link Throwable}s <code>cause</code> to
   **                            propagate.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link Throwable}.
   **
   ** @return                    the {@link RuntimeException} ready for
   **                            propagation.
   **                            <br>
   **                            Allowed object is {@link RuntimeException}.
   */
  public static RuntimeException create(final String prefix, final Iterable<? extends Throwable> bulk) {
    if (CollectionUtility.size(bulk) == 1) {
      Throwable e = bulk.iterator().next();
      return (StringUtility.empty(prefix)) ? new Propagated(e) : new Propagated(prefix + ": " + collapseText(e), e);
    }
    if (CollectionUtility.empty(bulk)) {
      return (StringUtility.empty(prefix)) ? new Compound("(empty compound exception)", bulk) : new Compound(prefix, bulk);
    }
    if (StringUtility.empty(prefix))
      return new Compound(CollectionUtility.size(bulk) + " errors, including: " + collapseText(bulk.iterator().next()), bulk);

    return new Compound(prefix + "; " + CollectionUtility.size(bulk) + " errors including: " + collapseText(bulk.iterator().next()), bulk);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   skippable
  /**
   ** Whether the given exception is skippable in any of the supplied contexts.
   **
   ** @param  cause              the {@link Throwable} to test in
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  context            the context to test.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if and only if exception
   **                            should skipped in <code>context</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public static boolean skippable(final Throwable cause, final Object... context) {
    if (!(cause instanceof Skippable))
      return false;

    for (Object c : context) {
      if (((Skippable)cause).accept(c)) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suppressed
  /**
   ** Returns whether the prefix is throwable either known to be useless or to
   ** have an annoying type name (prefix) which should be suppressed in
   ** <b>messages</b>. (They may be important in stack traces.)
   ** <p>
   ** <code>null</code> is accepted but treated as not useless.
   **
   ** @param  cause              the {@link Throwable} to test.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    <code>true</code> if and only if exception
   **                            should skipped.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public static boolean suppressed(final Throwable cause) {
    if (cause == null)
      return false;
    if (suppressMessage(cause))
      return true;
    if (cause instanceof ServiceException)
      return true;

    for (Class<? extends Throwable> type : Suppress.Runtime)
      if (cause.getClass().equals(type))
        return true;
    for (Class<? extends Throwable> type : Suppress.Prefix)
      if (type.isInstance(cause))
        return true;

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collapseText
  /**
   ** Same as {@link #collapseText(Throwable)} but returning a one-line message
   ** suitable for logging without traces.
   **
   ** @param  cause              the {@link Throwable} as the message provider.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the detail message string of the
   **                            {@code Throwable} instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static String collapseText(final Throwable cause) {
    return collapseText(cause, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collapseContext
  /**
   ** Same as {@link #collapseText(Throwable)} but skipping any throwables which
   ** implement {@link Skippable} and indicate they should be skipped any of the
   ** given context.
   **
   ** @param  cause              the {@link Throwable} as the message provider.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  context            the event context.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the detail message string of the
   **                            {@code Throwable} instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static String collapseContext(final Throwable cause, final Object... context) {
    return collapseText(cause, false, Collections.emptySet(), context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collapseCausal
  /**
   ** Normally {@link #collapseText(Throwable)} will stop following causal
   ** chains when encountering an interesting exception with a message.
   ** <br>
   ** This variant will continue to follow such causal chains, showing all
   ** messages.
   ** <br>
   ** For use e.g. when verbose is desired in the single-line message.
   **
   ** @param  cause              the {@link Throwable} as the message provider.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the detail message string of the
   **                            {@code Throwable} instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static String collapseCausal(final Throwable cause) {
    return collapseText(cause, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefixText
  /**
   ** Same as Like {@link Throwable#toString()} except suppresses useless
   ** prefixes and replaces prefixes with sensible messages where required.
   **
   ** @param  cause              the {@link Throwable} as the message provider.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the composed string derived from the detailed
   **                            message of the {@link Throwable}.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String prefixText(final Throwable cause) {
    return StringUtility.join(defaultText(cause), cause.getMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   causalChain
  /**
   ** Returns a {@link Throwable} cause chain as a collection that's safe in the
   ** face of perverse classes which return themselves as their cause or
   ** otherwise have a recursive causal chain.
   ** <br>
   ** The first entry in the list will be throwable followed by its cause
   ** hierarchy.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This is a snapshot of the cause chain and will not reflect any subsequent
   ** changes to the cause chain.
   **
   ** @param  cause              the {@link Throwable} to explode.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the collection of {@link Throwable} building
   **                            the cause chain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Throwable}.
   */
  public static List<Throwable> causalChain(Throwable cause) {
    final List<Throwable> result = new ArrayList<>();
    while (cause != null) {
      if (!result.add(cause))
        break;
      cause = cause.getCause();
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  private static RuntimeException propagate(final String message, final Throwable throwable, final boolean annotate) {
    if (throwable instanceof InterruptedException) {
      throw new Interrupted(message, (InterruptedException)throwable);
    }
    else if (throwable instanceof Interrupted) {
      Thread.currentThread().interrupt();
      if (annotate) {
        throw new Interrupted(message, (InterruptedException)throwable);
      }
      else {
        throw (Interrupted)throwable;
      }
    }
    if (throwable == null) {
      throw new Propagated(message, new NullPointerException("No throwable supplied."));
    }
//    if (!alwaysAnnotate) {
//      Throwables.propagateIfPossible(checkNotNull(throwable));
//    }
    throw new Propagated(message, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collapse
  private static Throwable collapse(final Throwable source, final boolean chaining, final boolean include, final Set<Throwable> visited, final Object... context) {
    if (visited.isEmpty()) {
      if (depthExceeded(source, RECURSIVE_DEPTH)) {
        // do fast check above, then do deeper check which survives recursive
        // causes
        List<Throwable> chain = causalChain(source);
        if (chain.size() > RECURSIVE_DEPTH) {
          // if it's an OOME or other huge stack, shrink it so we don't spin
          // huge cycles processing the trace and printing it (sometimes
          // generating subsequent OOME's in logback that mask the first!)
          // coarse heuristic for how to reduce it, but that's better than
          // killing cpu, causing further errors, and suppressing the root cause
          // altogether!
          String msg = chain.get(0).getMessage();
          if (msg.length() > 512)
            msg = msg.substring(0, 500) + "...";
          return new Propagated("Huge stack trace (size " + chain.size() + ", removing all but last few), starting: " + chain.get(0).getClass().getName() + ": " + msg + "; ultimately caused by: ", chain.get(chain.size() - 10));
        }
      }
    }
    String    message       = "";
    Throwable collapsed     = source;
    int       collapseCount = 0;
    boolean   messageFinal  = false;
    // remove boring exceptions at the head; if message is interesting append it
    while ((suppressMessage(collapsed) || skippable(collapsed, context)) && !messageFinal) {
      collapseCount++;
      final Throwable cause = collapsed.getCause();
      if (cause == null) {
        // everything in the tree is useless...
        return source;
      }
      if (!visited.add(collapsed)) {
        // there is a recursive loop
        break;
      }
      if (collapsed instanceof Propagated && ((Propagated)collapsed).embedded()) {
        message      = collapsed.getMessage();
        messageFinal = true;
      }
      collapsed = cause;
    }
    // if no messages so far (ie we will be the toString) then remove useless
    // prefixes from the message
    Throwable cursor = collapsed;
    while (cursor != null && suppressed(cursor) && StringUtility.blank(message)) {
      collapseCount++;
      if (!StringUtility.blank(cursor.getMessage())) {
        message = cursor.getMessage();
        cursor  = cursor.getCause();
        break;
      }
      visited.add(cursor);
      cursor = cursor.getCause();
    }
    if (collapseCount == 0 && !include)
      return source;

    if (collapseCount == 0 && cursor != null) {
      message = prefixText(cursor);
      cursor = cursor.getCause();
    }
    if (cursor != null && !messageFinal) {
      String extraMessage = collapseText(cursor, include, visited, context);
      message = StringUtility.join(message, extraMessage);
    }
    return new Propagated(message, chaining ? collapsed : source, StringUtility.blank(message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collapseText
  /**
   **
   ** @param  cause              the {@link Throwable} as the message provider.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  include            <code>true</code> if message text of
   **                            {@link Throwable} should be included in the
   **                            output.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the detail message string of the
   **                            {@code Throwable} instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private static String collapseText(final Throwable cause, final boolean include) {
    return collapseText(cause, include, Collections.emptySet(), new Object[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collapseText
  /**
   **
   ** @param  cause              the {@link Throwable} as the message provider.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  include            <code>true</code> if message text of
   **                            {@link Throwable} should be included in the
   **                            output.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  visited            the collection of {@link Throwables} that are
   **                            visited so far.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Throwable}.
   ** @param  context            ...
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the detail message string of the
   **                            {@code Throwable} instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private static String collapseText(final Throwable cause, final boolean include, Set<Throwable> visited, final Object... contexts) {
    // prevent bogus input
    if (cause == null)
      return null;

    if (visited.contains(cause)) {
      // if a boring-prefix class has no message it will render as
      // multiply-visited
      // additionally IllegalStateException sometimes refers to itself as its
      // cause
      // in both cases, don't stack overflow!
      if (!StringUtility.blank(cause.getMessage()))
        return cause.getMessage();

      return (cause.getCause() != null) ? cause.getCause().getClass().getName() : cause.getClass().getName();
    }
    Throwable t = collapse(cause, true, include, visited, contexts);
    visited.add(cause);
    visited.add(t);
    if (t instanceof Propagated) {
      if (((Propagated)t).embedded())
        // normally
        return t.getMessage();
      else if (t.getCause() != null)
        return collapseText(t.getCause(), include, Collections.unmodifiableSet(visited), contexts);
      return "" + t.getClass();
    }
    String result = prefixText(t);
    if (!include) {
      return result;
    }
    t = t.getCause();
    if (t != null) {
      String causeResult = ExceptionHandler.collapseContext(new Propagated(t), contexts);
      return (result.indexOf(causeResult) >= 0) ? result : result + "; caused by " + causeResult;
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultText
  /**
   ** For {@link Throwable} instances where know
   ** {@link #isPrefixRequiredForMessageToMakeSense(Throwable)}, this returns a
   ** pretty message for use as a prefix.
   ** <p>
   ** Returns empty string if {@link #isPrefixBoring(Throwable)} is
   ** <code>true</code>; otherwise this returns the simplified class name.
   **
   ** @param  cause              the {@link Throwable} as the message provider.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  private static String defaultText(final Throwable cause) {
    return (cause instanceof NoClassDefFoundError) ? "Invalid java type" : (suppressed(cause)) ? "" : cause.getClass().getSimpleName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suppressMessage
  /**
   ** Useful for stack trace, e.g. {@link ExecutionException}
   **
   ** @param  cause              the {@link Throwable} to test.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    <code>true</code> if and only if exception
   **                            useless.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private static boolean suppressMessage(final Throwable t) {
    for (Class<? extends Throwable> type : Suppress.Always)
      if (type.isInstance(t))
        return true;
    if (StringUtility.blank(t.getMessage())) {
      for (Class<? extends Throwable> type : Suppress.Message)
        if (type.isInstance(t))
          return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   depthExceeded
  /**
   ** Recursivly traverse the {@link Throwable} <code>cause</code> to verify the
   ** depth of the stack.
   **
   ** @param  cause              the root {@link Throwable} <code>cause</code>
   **                            to check.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  permitted          the permitted size of the remaining stack.
   **                            <br>
   **                            A value lesser than zero indicates that the
   **                            stack size is exeeded.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    <code>true</code> if the stack size reached the
   **                            limits; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private static boolean depthExceeded(final Throwable t, final int permitted) {
    return (permitted < 0) ? true : (t == null) ? false : depthExceeded(t.getCause(), permitted - 1);
  }
}