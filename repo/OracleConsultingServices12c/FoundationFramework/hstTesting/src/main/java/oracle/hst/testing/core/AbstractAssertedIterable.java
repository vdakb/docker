package oracle.hst.testing.core;

import java.util.Comparator;
import java.util.SortedSet;

////////////////////////////////////////////////////////////////////////////////
// class AbstractAssertedIterable
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Base class for implementations of {@link AssertedEnumerableObject} whose
 ** actual value type is <code>{@link Collection}</code>.
 **
 ** @param  <T>                  the "self" type of this assertion class.
 **                              Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 **                              for more details.
 **                              <br>
 **                              Allowed object is <code>&lt;T<&gt;</code>.
 ** @param <V>                   the type of the "actual" value.
 **                              <br>
 **                              Allowed object is <code>&lt;V<&gt;</code>
 ** @param <E>                   the type of elements of the "actual" value.
 **                              <br>
 **                              Allowed object is <code>&lt;E<&gt;</code>
 ** @param <A>                   used for navigational assertions to return the
 **                              right assert type.
 **                              <br>
 **                              Allowed object is <code>&lt;A<&gt;</code>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractAssertedIterable <T extends AbstractAssertedIterable<T, V, E, A>, V extends Iterable<? extends E>, E, A extends AbstractAsserted<A, E>> extends    AbstractAsserted<T, V> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // visibility is protected to allow us write custom assertions that need
  // access to iterables
  protected Iterables iterables = Iterables.instance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractAssertedIterable</code> with the
   ** <code>actual</code> value refering to <code>self</code>.
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
  protected AbstractAssertedIterable(final V actual, final Class<?> self) {
    // ensure inheritance
    super(actual, self);

    if (actual instanceof SortedSet) {
      @SuppressWarnings("unchecked") SortedSet<E> sortedSet = (SortedSet<E>)actual;
      Comparator<? super E>                       comparator = sortedSet.comparator();
      if (comparator != null)
        withComparator(sortedSet.comparator());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
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
}
