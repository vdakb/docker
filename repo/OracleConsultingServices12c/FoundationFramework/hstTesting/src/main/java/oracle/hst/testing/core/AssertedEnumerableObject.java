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

    File        :   AssertedEnumerableObject.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    AssertedEnumerableObject.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.testing.core;

////////////////////////////////////////////////////////////////////////////////
// interface AssertedEnumerableObject
// ~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Assertions methods applicable to groups of objects (e.g. arrays or
 ** collections.)
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
public interface AssertedEnumerableObject<T extends AssertedEnumerableObject<T, E>, E> extends AssertedEnumerable<T, E>{

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Verifies thatthat the actual group contains the given values, in any
   ** order.
   ** <p>
   ** Example:
   ** <pre>
   **   Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   **
   **   // assertions will pass
   **   assertThat(abc).contains("b", "a");
   **   assertThat(abc).contains("b", "a", "b");
   **
   **   // assertion will fail
   **   assertThat(abc).contains("d");
   ** </pre>
   ** If you want to specify the elements to check with an {@link Iterable}, use 
   ** @link #containsAll(Iterable) containsAll(Iterable)} instead.
   **
   ** @param  values             the given values.
   **                            <br>
   **                            Allowed object is array of <code>E</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerableObject</code>.
   **
   ** @throws AssertionError           if the actual group is <code>null</code>
   **                                  or if the actual group does not contain
   **                                  the given values.
   ** @throws NullPointerException     if the given argument is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the given argument is an empty array.
   */
  @SuppressWarnings("unchecked")
  T contains(final E... values);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsOnly
  /**
   ** Verifies that the actual group contains only the given values and nothing
   ** else, in any order and ignoring duplicates (i.e. once a value is found,
   ** its duplicates are also considered found).
   ** <p>
   ** If you need to check exactly the elements and their duplicates use:
   ** <ul>
   **   <li>{@link #containsExactly(Object...) containsExactly(Object...)} if the order does matter</li>
   **   <li>{@link #containsExactlyInAnyOrder(Object...) containsExactlyInAnyOrder(Object...)} if the order does not matter</li>
   ** </ul>
   ** Example:
   ** <pre>
   **   Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   **
   **   // assertions will pass as order does not matter
   **   assertThat(abc).containsOnly("c", "b", "a");
   **   // duplicates are ignored
   **   assertThat(abc).containsOnly("a", "a", "b", "c", "c");
   **   // ... on both actual and expected values
   **   assertThat(asList("a", "a", "b")).containsOnly("a", "b")
   **                                    .containsOnly("a", "a", "b", "b");
   **
   **   // assertion will fail because "c" is missing in the given values
   **   assertThat(abc).containsOnly("a", "b");
   **   // assertion will fail because "d" is missing in abc (use isSubsetOf if
   **   // you want this assertion to pass)
   **   assertThat(abc).containsOnly("a", "b", "c", "d");
   ** </pre>
   ** If you need to check that actual is a subset of the given values, use
   ** {@link #isSubsetOf(Object...)}.
   ** <p>
   ** If you want to specify the elements to check with an {@link Iterable}, use
   ** {@link #containsOnlyElementsOf(Iterable) containsOnlyElementsOf(Iterable)}
   ** instead.
   **
   ** @param  values             the given values.
   **                            <br>
   **                            Allowed object is array of <code>E</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerableObject</code>.
   **
   ** @throws AssertionError           if the actual group is <code>null</code>
   **                                  or if the actual group does not contain
   **                                  the given values, i.e. the actual group
   **                                  contains some or none of the given
   **                                  values, or the actual group contains more
   **                                  values than the given ones.
   ** @throws NullPointerException     if the given argument is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the given argument is an empty array.
   */
  @SuppressWarnings("unchecked")
  T containsOnly(final E... values);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsOnlyNulls
  /**
   ** Verifies that the actual group contains only null elements and
   ** nothing else.
   ** <p>
   ** Example:
   ** <pre>
   **   // assertion will pass
   **   Iterable&lt;String&gt; items = Arrays.asList(null, null, null);
   **   assertThat(items).containsOnlyNulls();
   **
   **   // assertion will fail because items2 contains a not null element
   **   Iterable&lt;String&gt; items2 = Arrays.asList(null, null, "notNull");
   **   assertThat(items2).containsOnlyNulls();
   **
   **   // assertion will fail since an empty iterable does not contain any
   **   // elements and therefore no null ones.
   **   Iterable&lt;String&gt; empty = new ArrayList&lt;&gt;();
   **   assertThat(empty).containsOnlyNulls();
   ** </pre>
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerableObject</code>.
   **
   ** @throws AssertionError     if the actual group is <code>null</code> or if
   **                            the actual group is empty or contains
   **                            non-<code>null</code> elements.
   */
  T containsOnlyNulls();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsOnlyOnce
  /**
   ** Verifies that the actual group contains the given values only once.
   ** <p>
   ** Example:
   ** <pre>
   **   // lists are used in the examples but it would also work with arrays
   **
   **   // assertions will pass
   **   assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;winter&quot;);
   **   assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;coming&quot;, &quot;winter&quot;);
   **
   **   // assertions will fail
   **   assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;Lannister&quot;);
   **   assertThat(newArrayList(&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnce(&quot;Stark&quot;);
   **   assertThat(newArrayList(&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnce(&quot;Stark&quot;, &quot;Lannister&quot;, &quot;Arya&quot;);
   ** </pre>
   **
   ** @param  values             the given values.
   **                            <br>
   **                            Allowed object is array of <code>E</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerableObject</code>.
   **
   ** @throws AssertionError           if the actual group is <code>null</code>
   **                                  or if the actual group does not contain
   **                                  the given values, i.e. the actual group
   **                                  contains some or none of the given
   **                                  values, or the actual group contains more
   **                                  than once these values.
   ** @throws NullPointerException     if the given argument is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the given argument is an empty array.
   */
  @SuppressWarnings("unchecked")
  T containsOnlyOnce(final E... values);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsOnlyNulls
  /**
   ** Verifies that the actual group contains exactly the given values and
   ** nothing else, <b>in order</b>.
   ** <br>
   ** This assertion should only be used with groups that have a consistent
   ** iteration order (i.e. don't use it with {@link HashSet}, prefer
   ** {@link #containsOnly(Object...)} in that case).
   ** <p>
   ** Example:
   ** <pre>
   **   // an Iterable is used in the example but it would also work with an array
   **   Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   **
   **   // assertion will pass
   **   assertThat(elvesRings).containsExactly(vilya, nenya, narya);
   **
   **   // assertion will fail as actual and expected order differ
   **   assertThat(elvesRings).containsExactly(nenya, vilya, narya);
   ** </pre>
   ** If you want to specify the elements to check with an {@link Iterable}, use
   ** {@link #containsExactlyElementsOf(Iterable) containsExactlyElementsOf(Iterable)}
   ** instead.
   **
   ** @param  values             the given values.
   **                            <br>
   **                            Allowed object is array of <code>E</code>.
   **
   ** @return                    this assertion object for method chaining
   **                            purpose.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertedEnumerableObject</code>.
   **
   ** @throws AssertionError       if the actual group is <code>null</code> or
   **                              if the actual group does not contain the
   **                              given values with same order, i.e. the actual
   **                              group contains some or none of the given
   **                              values, or the actual group contains more
   **                              values than the given ones or values are the
   **                              same but the order is not.
   ** @throws NullPointerException if the given argument is <code>null</code>.
   */
  @SuppressWarnings("unchecked")
  T containsExactly(final E... values);
}