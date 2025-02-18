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
    Subsystem   :   Common Shared Utility

    File        :   TestUnchecked.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestUnchecked.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.junit.collect;

import java.io.IOException;

import java.io.UncheckedIOException;

import java.util.function.Function;

import oracle.hst.platform.core.Unchecked;

import oracle.hst.platform.core.function.CheckedRunnable;

import oracle.hst.platform.core.function.CheckedSupplier;
import oracle.hst.platform.junit.TestBase;

import org.junit.Test;
import org.junit.Assert;

import org.junit.runner.JUnitCore;

import oracle.hst.platform.junit.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class RangeTest
// ~~~~~ ~~~~~~~~~
/**
 ** Test coverage of {@link Unchecked}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestUnchecked extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestUnchecked</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestUnchecked() {
   // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = { TestUnchecked.class.getName() };
    JUnitCore.main(parameter);
  }

  @Test
  public void testWrapRunnable1() {
    // cannot use assertThrows() here
    try {
      Unchecked.wrap((CheckedRunnable)() -> { throw new IOException(); });
      failed("Expected UncheckedIOException");
    }
    catch (UncheckedIOException ex) {
      // success
    }
  }

  @Test
  public void testWrapRunnable2() {
    // cannot use assertThrows() here
    try {
      Unchecked.wrap((CheckedRunnable<Exception>)() -> { throw new Exception(); });
      failed("Expected RuntimeException");
    }
    catch (RuntimeException ex) {
      // success
    }
  }

  @Test
  public void testWrapRunnableFailed1() {
    Runnable a = Unchecked.runnable(() -> { throw new IOException(); });
    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> a.run());
  }

  @Test
  public void testWrapRunnableFailed2() {
    Runnable a = Unchecked.runnable(() -> { throw new Exception(); });
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> a.run());
  }

  @Test
  public void testWrapSupplier1() {
    // cannot use assertThrows() here
    try {
      Unchecked.wrap((CheckedSupplier<String, IOException>)() -> { throw new IOException(); });
      failed("Expected UncheckedIOException");
    }
    catch (UncheckedIOException ex) {
      // success
    }
  }

  @Test
  public void testWrapSupplier2() {
    // cannot use assertThrows() here
    try {
      Unchecked.wrap((CheckedSupplier<String, Exception>)() -> { throw new Exception(); });
      failed("Expected RuntimeException");
    }
    catch (RuntimeException ex) {
      // success
    }
  }

  @Test
  public void testFunctionSuccess() {
    Function<String, String> a = Unchecked.function((t) -> t);
    assertThat(a.apply("A")).isEqualTo("A");
  }

  @Test
  public void testFunctionFaild1() {
    Function<String, String> a = Unchecked.function((t) -> { throw new IOException(); });
    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> a.apply("A"));
  }

  @Test
  public void testFunctionFaild2() {
    Function<String, String> a = Unchecked.function((t) -> { throw new Exception(); });
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> a.apply("A"));
  }
}
