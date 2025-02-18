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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared junit testing functions

    File        :   AbstractAsserted.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractAsserted.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.testing.core;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractAsserted
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Base class for all assertions.
 **
 ** @param  <T>                  the "self" type of this assertion class.
 **                              Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 **                              for more details.
 **                              <br>
 **                              Allowed object is <code>&lt;T<&gt;</code>.
 ** @param  <V>                  the type of the "actual" value.
 **                              <br>
 **                              Allowed object is <code>&lt;V<&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractAsserted<T extends AbstractAsserted<T, V>, V> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // visibility is protected to allow us write custom assertions that need
  // access to actual
  protected final T            self;
  protected final V            actual;
  
  private final   ErrorCreator errorCreator = new ErrorCreator();

  public static class ErrorCreator {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs an empty <code>ErrorCreator</code>.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new ErrorCreator()" and enforces use of the public method below.
     */
    private ErrorCreator() {
      // ensure inheritance
      super();
    }

    public AssertionError assertionError(final String message) {
      return new AssertionError(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractAsserted</code> with the <code>actual</code>
   ** value refering to <code>self</code>.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** We prefer not to use Class<? extends T> self because it would force
   ** inherited constructor to cast with a compiler warning let's keep compiler
   ** warning internal (when we can) and not expose them to our end users.
   **
   ** @param  actual             the actual value to kept.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  self               the referencing {@link Class}.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   */
  @SuppressWarnings("unchecked")
  protected AbstractAsserted(final V actual, final Class<?> self) {
    // ensure inheritance
    super();

    // initalize instance attributes
    this.self   = (T)self.cast(this); 
    this.actual = actual;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failWithMessage
  /**
   ** Throw an assertion error based on information in this assertion.
   ** <br>
   ** Equivalent to:
   ** <pre>
   **   throw failure(errorMessage, arguments);
   ** </pre>
   ** This method is a thin wrapper around
   ** {@link #failure(String, Object...) failure()} - see that method for a more
   ** detailed description.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Generally speaking, using {@link #failure(String, Object...) failure()}
   ** directly is preferable to using this wrapper method, as the compiler and
   ** other code analysis tools will be able to tell that the statement will
   ** never return normally and respond appropriately.
   **
   ** @param  format             the error message to format.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the arguments referenced by the format
   **                            specifiers in <code>format</code> string.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @see    #failure(String, Object...)
   ** @see    #failWithActualExpectedAndMessage(Object, Object, String, Object...)
   */
  protected void failWithMessage(final String format, final Object... arguments) {
    throw failure(format, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failWithMessage
  /**
   ** Throw an assertion error based on information in this assertion.
   ** <br>
   ** Equivalent to:
   ** <pre>
   **   throw failureWithActualExpected(actual, expected, format, arguments);
   ** </pre>
   ** This method is a thin wrapper around
   ** {@link #failureWithActualExpected(Object, Object, String, Object...) failureWithActualExpected()}
   ** - see that method for a more detailed description.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Generally speaking, using {@link #failureWithActualExpected(Object, Object, String, Object...) failureWithActualExpected()}
   ** directly is preferable to using this wrapper method, as the compiler and
   ** other code analysis tools will be able to tell that the statement will
   ** never return normally and respond appropriately.
   **
   ** @param  actual             the actual object that was found during the
   **                            test.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  expected           the object that was expected.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  format             the error message to format.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the arguments referenced by the format
   **                            specifiers in <code>format</code> string.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @see    #failWithMessage(String, Object...)
   ** @see    #failureWithActualExpected(Object, Object, String, Object...)
   */
  protected void failWithActualExpectedAndMessage(final Object actual, final Object expected, final String format, Object... arguments) {
    throw failureWithActualExpected(actual, expected, format, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failWithMessage
  /**
   ** Generate a custom assertion error using the information in this assertion,
   ** using the given actual and expected values.
   ** <p>
   ** This is a utility method to ease writing custom assertions classes using
   ** {@link String#format(String, Object...)} specifiers in error message with
   ** actual and expected values.
   ** <p>
   ** Moreover, this method honors any description set with
   ** {@link #as(String, Object...)} or overridden error message defined by the
   ** user with {@link #overridingErrorMessage(String, Object...)}.
   ** <p>
   ** This method also sets the "actual" and "expected" fields of the assertion
   ** if available (eg, if OpenTest4J is on the path). This aids IDEs to produce
   ** visual diffs of the resulting values.
   ** <p>
   ** Example:
   ** <pre>
   **   public TolkienCharacterAssert hasName(String name) {
   **     // check that actual TolkienCharacter we want to make assertions on is not null.
   **     isNotNull();
   **
   **     // check condition
   **     if (!actual.getName().equals(name)) {
   **       throw failureWithActualExpected(actual.getName(), name, &quot;Expected character's name to be &lt;%s&gt; but was &lt;%s&gt;&quot;, name, actual.getName());
   **     }
   **
   **     // return the current assertion for method chaining
   **     return this;
   **   }
   ** </pre>
   **
   ** @param  actual             the actual object that was found during the
   **                            test.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  expected           the object that was expected.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  format             the error message to format.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the arguments referenced by the format
   **                            specifiers in <code>format</code> string.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the generated assertion error.
   **                            <br>
   **                            Allowed object is {@link AssertionError}.
   **
   ** @see    #failure(String, Object...)
   ** @see    #failWithActualExpectedAndMessage(Object, Object, String, Object...)
   */
  protected AssertionError failureWithActualExpected(final Object actual, final Object expected, final String format, final Object... arguments) {
    return this.errorCreator.assertionError(String.format(format, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failure
  /**
   ** Generate a custom assertion error using the information in this assertion.
   ** <p>
   ** This is a utility method to ease writing custom assertions classes using
   ** {@link String#format(String, Object...)} specifiers in error message.
   ** <p>
   ** Moreover, this method honors any description set with
   ** {@link #as(String, Object...)} or overridden error message defined by the
   ** user with {@link #overridingErrorMessage(String, Object...)}.
   ** <p>
   ** Example:
   ** <pre>
   **   public TolkienCharacterAssert hasName(String name) {
   **     // check that actual TolkienCharacter we want to make assertions on is
   **     // not null.
   **     isNotNull();
   **
   **     // check condition
   **     if (!actual.getName().equals(name)) {
   **       failWithMessage(&quot;Expected character's name to be &lt;%s&gt; but was &lt;%s&gt;&quot;, name, actual.getName());
   **     }
   **
   **     // return the current assertion for method chaining
   **     return this;
   **   }
   ** </pre>
   **
   **
   ** @param  message            the error message to format.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the arguments referenced by the format
   **                            specifiers in <code>message</code> string.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the generated assertion error.
   **                            <br>
   **                            Allowed object is {@link AssertionError}.
   **
   ** @see    #failWithMessage(String, Object...)
   ** @see    #failureWithActualExpected(Object, Object, String, Object...)
   */
  protected AssertionError failure(final String message, final Object... arguments) {
    return new AssertionError(String.format(message, arguments));
  }
}