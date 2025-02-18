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

    File        :   AssertedEnumerable.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    AssertedEnumerable.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.testing.core;

import java.util.Comparator;

////////////////////////////////////////////////////////////////////////////////
// interface AssertedEnumerable
// ~~~~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Assertions applicable to groups of values that can be enumerated (e.g.
 ** arrays, collections or strings.)
 **
 ** @param  <T>                  the "self" type of this assertion class.
 **                              Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 **                              for more details.
 **                              <br>
 **                              Allowed object is <code>&lt;T<&gt;</code>.
 ** @param <E>                   the type of elements of the "actual" value.
 **                              <br>
 **                              Allowed object is <code>&lt;E<&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface AssertedEnumerable<T extends AssertedEnumerable<T, E>, E> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullOrEmpty
  /**
   ** Verifies that the actual group of values is <code>null</code> or empty.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   List&lt;String&gt; strings = new ArrayList&lt;&gt;();
   **   assertThat(strings).nullOrEmpty();
   **   assertThat(new int[] { }).nullOrEmpty();
   **
   **   // assertions will fail
   **   assertThat(new String[] { &quot;a&quot;, &quot;b&quot;}).nullOrEmpty();
   **   assertThat(Arrays.asList(1, 2, 3)).nullOrEmpty();
   ** </pre>
   **
   ** @throws AssertionError     if the actual group of values is not
   **                            <code>null</code> or not empty.
   */
  void nullOrEmpty();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Verifies that the actual group of values is empty.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   assertThat(new ArrayList()).empty();
   **   assertThat(new int[] { }).empty();
   **
   **   // assertions will fail
   **   assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).empty();
   **   assertThat(Arrays.asList(1, 2, 3)).empty();
   ** </pre>
   **
   ** @throws AssertionError     if the actual group of values is not empty.
   */
  void empty();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notEmpty
  /**
   ** Verifies that the actual group of values is not empty.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).notEmpty();
   **   assertThat(Arrays.asList(1, 2, 3)).notEmpty();
   **
   **   // assertions will fail
   **   assertThat(new ArrayList()).notEmpty();
   **   assertThat(new int[] { }).notEmpty();
   ** </pre>
    **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the actual group of values is empty.
   */
  T notEmpty();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ofSize
  /**
   ** Verifies that the number of values in the actual group is equal to the
   ** given one.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).ofSize(2);
   **   assertThat(Arrays.asList(1, 2, 3)).ofSize(3);
   **
   **   // assertions will fail
   **   assertThat(new ArrayList()).ofSize(1);
   **   assertThat(new int[] { 1, 2, 3 }).ofSize(2);
   ** </pre>
   **
   ** @param  expected           the expected number of values in the actual
   **                            group.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the number of values of the actual group is
   **                            not equal to the given one.
   */
  T ofSize(final int expected);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ofSize
  /**
   ** Verifies that the number of values in the actual group is less than the
   ** given <code>limit</code>.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).sizeLessThan(5);
   **   assertThat(Arrays.asList(1, 2, 3)).sizeLessThan(4);
   **
   **   // assertions will fail
   **   assertThat(Arrays.asList(1, 2, 3)).sizeLessThan(3);
   **   assertThat(new int[] { 1, 2, 3 }).sizeLessThan(2);
   ** </pre>
   **
   ** @param  limit              the given value to compare the actual size to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the number of values of the actual group is
   **                            not less than the <code>limit</code>.
   */
  T sizeLessThan(final int limit);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sizeLessThanOrEqualTo
  /**
   ** Verifies that the number of values in the actual group is less than or
   ** equal to the given <code>limit</code>.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).sizeLessThanOrEqualTo(3);
   **   assertThat(Arrays.asList(1, 2, 3)).sizeLessThanOrEqualTo(5)
   **                                     .sizeLessThanOrEqualTo(3);
   **
   **   // assertions will fail
   **   assertThat(Arrays.asList(1, 2, 3)).sizeLessThanOrEqualTo(2);
   **   assertThat(new int[] { 1, 2, 3 }).sizeLessThanOrEqualTo(1);
   ** </pre>
   **
   ** @param  limit              the given value to compare the actual size to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the number of values of the actual group is
   **                            not less than or equal to the
   **                            <code>limit</code>.
   */
  T sizeLessThanOrEqualTo(final int limit);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sizeGreaterThan
  /**
   ** Verifies that the number of values in the actual group is greater than the
   ** given <code>limit</code>.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).sizeGreaterThan(1);
   **   assertThat(Arrays.asList(1, 2, 3)).sizeGreaterThan(2);
   **
   **   // assertions will fail
   **   assertThat(Arrays.asList(1, 2, 3)).sizeGreaterThan(3);
   **   assertThat(new int[] { 1, 2, 3 }).sizeGreaterThan(6);
   ** </pre>
   **
   ** @param  limit              the given value to compare the actual size to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the number of values of the actual group is
   **                            not greater than the <code>limit</code>.
   */
  T sizeGreaterThan(final int limit);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sizeGreaterThanOrEqualTo
  /**
   ** Verifies that the number of values in the actual group is greater than or
   ** equal to the given <code>limit</code>.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   assertThat(Arrays.asList(1, 2, 3)).sizeGreaterThanOrEqualTo(3);
   **   assertThat(Arrays.asList(1, 2)).sizeGreaterThanOrEqualTo(1);
   **
   **   // assertions will fail
   **   assertThat(Arrays.asList(1, 2)).sizeGreaterThanOrEqualTo(3);
   **   assertThat(new int[] { 1, 2, 3 }).sizeGreaterThanOrEqualTo(4);
   ** </pre>
   **
   ** @param  limit              the given value to compare the actual size to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the number of values of the actual group is
   **                            not greater than or equal to the
   **                            <code>limit</code>.
   */
  T sizeGreaterThanOrEqualTo(final int limit);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sizeBetween
  /**
   ** Verifies that the number of values in the actual group is between the given boundaries (inclusive).
   ** <p>
   ** Example:
   ** <pre>
   **   // assertions will pass
   **   assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).sizeBetween(0, 4);
   **   assertThat(Arrays.asList(1, 2, 3)).sizeBetween(2, 3)
   **                                     .sizeBetween(3, 4)
   **                                     .sizeBetween(3, 3);
   **
   **   // assertions will fail
   **   assertThat(new ArrayList()).sizeBetween(1, 3);
   **   assertThat(new int[] { 1, 2, 3 }).sizeBetween(4, 6);
   **   assertThat(new int[] { 1, 2, 3, 4 }).sizeBetween(0, 2);
   ** </pre>
   **
   ** @param  lower              the lower boundary compared to which actual
   **                            size should be greater than or equal to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  upper              the higher boundary compared to which actual
   **                            size should be less than or equal to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the number of values of the actual group is
   **                            not between the boundaries.
   */
  T sizeBetween(final int lower, final int upper);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sizeSameAs
  /**
   ** Verifies that the actual group has the same size as given
   ** {@link Iterable}.
   ** <p>
   ** Example:
   ** <pre>
   **   Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   **   Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   **
   **   // assertion will pass
   **   assertThat(elvesRings).sizeSameAs(abc);
   **
   **   // assertions will fail
   **   assertThat(elvesRings).sizeSameAs(Arrays.asList(1, 2));
   **   assertThat(elvesRings).sizeSameAs(Arrays.asList(1, 2, 3, 4));
   ** </pre>
   **
   ** @param  other              the {@link Iterable} to compare size with
   **                            actual group.
   **                            <br>
   **                            Allowed object is {@link Iterable}.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the actual group is <code>null</code> or if.
   **                            the other {@link Iterable} is
   **                            <code>null</code> or if actual group and given
   **                            {@link Iterable} don't have the same size.
   */
  T sizeSameAs(Iterable<?> other);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sizeSameAs
  /**
   ** Verifies that the actual group has the same size as given array.
   ** <p>
   ** Parameter is declared as Object to accept both Object[] and primitive
   ** arrays (e.g. int[]).
   ** <p>
   ** Example:
   ** <pre>
   **   int[] oneTwoThree = {1, 2, 3};
   **   Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   **
   **   // assertion will pass
   **   assertThat(elvesRings).sizeSameAs(oneTwoThree);
   **
   **   // assertions will fail
   **   assertThat(elvesRings).sizeSameAs(new int[] { 1, 2});
   **   assertThat(elvesRings).sizeSameAs(new int[] { 1, 2, 3, 4});
   ** </pre>
   **
   ** @param  array              the array to compare size with actual group.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   ** @throws AssertionError     if the actual group is <code>null</code> or if
   **                            the array parameter is <code>null</code> or is
   **                            not a true array or if actual group and given
   **                            array don't have the same size.
   */
  T sizeSameAs(final Object array);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   withComparator
  /**
   ** Use given custom comparator instead of relying on actual type E
   ** <code>equals</code> method to compare group elements for incoming
   ** assertion checks.
   ** <p>
   ** Custom comparator is bound to assertion instance, meaning that if a new
   ** assertion is created, it will use default comparison strategy.
   ** <p>
   ** Example:
   ** <pre>
   **   // compares invoices by payee
   **   assertThat(invoiceList).withComparator(invoicePayeeComparator).isEqualTo(expectedInvoiceList);
   **
   **   // compares invoices by date, doesNotHaveDuplicates and contains both use the given invoice date comparator
   **   assertThat(invoiceList).withComparator(invoiceDateComparator).doesNotHaveDuplicates().contains(may2010Invoice);
   **
   **   // as assertThat(invoiceList) creates a new assertion, it falls back to
   **   // standard comparison strategy based on Invoice's equal method to
   **   // compare invoiceList elements to lowestInvoice.
   **   assertThat(invoiceList).contains(lowestInvoice);
   **
   **   // standard comparison : the fellowshipOfTheRing includes Gandalf but
   **   // not Sauron (believe me) ...
   **   assertThat(fellowshipOfTheRing).contains(gandalf)
   **                                  .doesNotContain(sauron);
   **
   **   // ... but if we compare only races, Sauron is in fellowshipOfTheRing
   **   // because he's a Maia like Gandalf.
   **   assertThat(fellowshipOfTheRing).usingElementComparator(raceComparator)
   **                                  .contains(sauron);
   ** </pre>
   **
   ** @param  comparator         the comparator to use for incoming assertion
   **                            checks.
   **                            <br>
   **                            Allowed object is {@link Comparator}.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   **
   * @throws NullPointerException if the given comparator is <code>null</code>.
   */
  T withComparator(final Comparator<? super E> comparator);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   withDefaultComparator
  /**
   ** Revert to standard comparison for incoming assertion group element checks.
   ** <p>
   ** This method should be used to disable a custom comparison strategy set by
   ** calling {@link #withComparator(Comparator)}.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerable</code>.
   */
  T withDefaultComparator();
}