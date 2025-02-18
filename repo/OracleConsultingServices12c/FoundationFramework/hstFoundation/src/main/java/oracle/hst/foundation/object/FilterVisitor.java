package oracle.hst.foundation.object;

import oracle.hst.foundation.object.filter.Or;
import oracle.hst.foundation.object.filter.And;
import oracle.hst.foundation.object.filter.Not;
import oracle.hst.foundation.object.filter.Equals;
import oracle.hst.foundation.object.filter.Presence;
import oracle.hst.foundation.object.filter.Contains;
import oracle.hst.foundation.object.filter.EndsWith;
import oracle.hst.foundation.object.filter.LessThan;
import oracle.hst.foundation.object.filter.StartsWith;
import oracle.hst.foundation.object.filter.ContainsAll;
import oracle.hst.foundation.object.filter.GreaterThan;
import oracle.hst.foundation.object.filter.LessThanOrEqual;
import oracle.hst.foundation.object.filter.GreaterThanOrEqual;

////////////////////////////////////////////////////////////////////////////////
// interface FilterVisitor
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** A visitor of {@link Filter}s, in the style of the visitor design pattern.
 ** <p>
 ** Classes implementing this interface can query filters in a type-safe manner.
 ** When a visitor is passed to a filter's accept method, the corresponding
 ** visit method most applicable to that filter is invoked.
 **
 ** @param  <R>                  the return type of this visitor's methods. Use
 **                              {@link java.lang.Void} for visitors that do not
 **                              need to return results.
 ** @param  <P>                  the type of the additional parameter to this
 **                              visitor's methods. Use {@link java.lang.Void}
 **                              for visitors that do not need an additional
 **                              parameter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public interface FilterVisitor<R, P> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Visits an <code>and</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always evaluate
   ** to <code>true</code>.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R and(final P parameter, final And filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Visits an <code>or</code> filter.
   * <p>
   * <b>Implementation note</b>: for the purposes of matching, an empty
   * sub-filters should always evaluate to <code>false</code>.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R or(final P parameter, final Or filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   not
  /**
   ** Visits an <code>not</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R not(final P parameter, final Not filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   presence
  /**
   ** Visits an <code>presence</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R presence(final P parameter, final Presence filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Visits an <code>equality</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R equal(final P parameter, final Equals filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extendedFilter
  /**
   ** Visits an <code>comparison</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R extendedFilter(final P parameter, final Filter filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   greaterThan
  /**
   ** Visits an <code>greater than</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R greaterThan(final P parameter, final GreaterThan filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   greaterThanOrEqual
  /**
   ** Visits an <code>greater than or equal to</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R greaterThanOrEqual(final P parameter, final GreaterThanOrEqual filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lessThan
  /**
   ** Visits an <code>less than</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R lessThan(final P parameter, final LessThan filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lessThanOrEqual
  /**
   ** Visits a <code>less than or equal to</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R lessThanOrEqual(final P parameter, final LessThanOrEqual filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWith
  /**
   ** Visits a <code>starts with</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R startsWith(final P parameter, final StartsWith filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWith
  /**
   * Visits a <code>ends with</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R endsWith(final P parameter, final EndsWith filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Visits an <code>contains</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R contains(final P parameter, final Contains filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsAll
  /**
   ** Visits an <code>containsAll</code> filter.
   **
   ** @param  parameter         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  R containsAll(final P parameter, final ContainsAll filter);
}