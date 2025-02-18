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
    Subsystem   :   Generic REST Library

    File        :   Translator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Translator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.entity;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import oracle.hst.platform.core.entity.Or;
import oracle.hst.platform.core.entity.And;
import oracle.hst.platform.core.entity.Not;
import oracle.hst.platform.core.entity.Filter;
import oracle.hst.platform.core.entity.CompositeFilter;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.ProcessingException;

////////////////////////////////////////////////////////////////////////////////
// abstract class Translator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Base class to make it easier to implement a search.
 ** <p>
 ** A search filter may contain operators (such as 'contains' or 'in') or may
 ** contain logical operators (such as 'AND', 'OR' or 'NOT') that a connector
 ** cannot implement using the native API of the target system or application. A
 ** connector developer should subclass <code>FilterTranslator</code> in order
 ** to declare which filter operations the connector does support. This allows
 ** the <code>FilterTranslator</code> instance to analyze a specified search
 ** filter and reduce the filter to its most efficient form. The default (and
 ** worst-case) behavior is to return a <code>null</code> expression, which
 ** means that the connector should return "everything" (that is, should return
 ** all values for every requested attribute) and rely on the common code in the
 ** framework to perform filtering. This "fallback" behavior is good (in that it
 ** ensures consistency of search behavior across connector implementations) but
 ** it is obviously better for performance and scalability if each connector
 ** performs as much filtering as the native API of the target can support.
 ** <p>
 ** A subclass should override each of the following methods where possible:
 ** <ol>
 **   <li>{@link #createOR}
 **   <li>{@link #createAND}
 **   <li>{@link #createPR(Filter, boolean)}
 **   <li>{@link #createEQ(Filter, boolean)}
 **   <li>{@link #createGT(Filter, boolean)}
 **   <li>{@link #createGE(Filter, boolean)}
 **   <li>{@link #createSW(Filter, boolean)}
 **   <li>{@link #createEW(Filter, boolean)}
 **   <li>{@link #createCO(Filter, boolean)}
 ** </ol>
 ** <p>
 ** Translation can then be performed using {@link #translate(Filter)}.
 **
 ** @param  <T>                  the result type of the translator.
 **                              Commonly this will be a string, but there are
 **                              cases where you might need to return a more
 **                              complex data structure. For example if you are
 **                              building a SQL query, you will need not
 **                              *just* the base WHERE clause but a list of
 **                              tables that need to be joined together.
 */
public class Translator<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Construct a new filter translator.
   */
  protected Translator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translate
  /**
   ** Main method to be called to translate a filter.
   **
   ** @param  filter              the {@link Filter} to translate.
   **                             <br>
   **                             Allowed object is {@link Filter}.
   **
   ** @return                     the list of queries to be performed.
   **                             The list <code>size()</code> may be one of the
   **                             following:
   **                             <ol>
   **                               <li>0 - This signifies <b>fetch everything</b>.
   **                                   This may occur if your filter was
   **                                   <code>null</code> or one of your
   **                                   <code>create*</code> methods returned
   **                                   <code>null</code>.
   **                               <li>1 - List contains a single query that
   **                                   will return the results from the filter.
   **                                   Note that the results may be a
   **                                   <b>superset</b> of those specified by
   **                                   the filter in the case that one of your
   **                                   <code>create*</code> methods returned
   **                                   <code>null</code>. However it is
   **                                   undesirable from a performance
   **                                   standpoint.
   **                                <li>&lt;1 - List contains multiple queries
   **                                    that must be performed in order to
   **                                    meet the filter that was passed in.
   **                                    Note that this only occurs if your
   **                                    {@link #createOR} method can return
   **                                    <code>null</code>. If this happens, it
   **                                    is the responsibility of the
   **                                    implementor to perform each query and
   **                                    combine the results. In order to
   **                                    eliminate duplicates, the
   **                                    implementation must keep an in-memory
   **                                    <code>HashSet</code> of those UID that
   **                                    have been visited thus far. This will
   **                                    not scale well if your result sets are
   **                                    large. Therefore it is
   **                                    <b>recommended</b> that if at all
   **                                    possible you implement
   **                                    {@link #createOR}
   **                             </ol>
   **                             <br>
   **                             Allowed object is {@link List} where each
   **                             element is of type <code>T</code>.
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  public final List<T> translate(Filter<T> filter)
    throws ProcessingException {

    if (filter == null)
      return new ArrayList<T>();

    // this must come first
    filter = simplify(normalize(filter));
    // might have simplified it to the everything filter
    if (filter == null)
      return new ArrayList<T>();

    List<T> result = translateInternal(filter);
    // now "optimize" - we can eliminate exact matches at least
    Set<T> set = new HashSet<T>();
    List<T> optimized = new ArrayList<T>(result.size());
    for (T obj : result) {
      if (set.add(obj)) {
        optimized.add(obj);
      }
    }
    return optimized;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOR
  /**
   ** Should be overridden by subclasses to create an OR expression if the
   ** native resource supports OR.
   **
   ** @param  lhs                the left expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the right expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the OR expression.
   **                            A return value of <code>null</code> means a
   **                            native OR query cannot be created for the
   **                            given expressions. In this case,
   **                            {@link #translate} may return multiple queries,
   **                            each of which must be run and results combined.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  protected T createOR(@SuppressWarnings("unused") final T lhs, @SuppressWarnings("unused") final T rhs) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAND
  /**
   ** Should be overridden by subclasses to create an AND expression if the
   ** native resource supports AND.
   **
   ** @param  lhs                the left expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the right expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the AND expression.
   **                            A return value of <code>null</code> means a
   **                            native AND query cannot be created for the
   **                            given expressions. In this case, the resulting
   **                            query will consist of the leftExpression only.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  protected T createAND(@SuppressWarnings("unused") final T lhs, @SuppressWarnings("unused") final T rhs) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPR
  /**
   ** Should be overridden by subclasses to create an PRESENT expression if the
   ** native resource supports PRESENCE.
   **
   **
   ** @param  filter             the presence filter.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} fot type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            PRESENT.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the PRESENT expression.
   **                            A return value of <code>null</code> means a
   **                            native PRESENT query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createPR(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEQ
  /**
   ** Should be overridden by subclasses to create an EQUALS expression if the
   ** native resource supports EQUALS.
   **
   **
   ** @param  filter             the equlas filter.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            EQUALS.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the EQUALS expression.
   **                            A return value of <code>null</code> means a
   **                            native EQUALS query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createEQ(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGT
  /**
   ** Should be overridden by subclasses to create a GREATER-THAN expression if
   ** the native resource supports GREATER-THAN.
   **
   ** @param  filter             the greater than filter.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the GREATER-THAN expression.
   **                            A return value of <code>null</code> means a
   **                            native GREATER-THAN query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createGT(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGE
  /**
   ** Should be overridden by subclasses to create a GREATER-THAN-EQUAL
   ** expression if the native resource supports GREATER-THAN-EQUAL.
   **
   ** @param  filter             the greater than or equal filter.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN-EQUAL.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the GREATER-THAN-EQUAL expression.
   **                            A return value of <code>null</code> means a
   **                            native GREATER-THAN-EQUAL query cannot be
   **                            created for the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createGE(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLT
  /**
   ** Should be overridden by subclasses to create a LESS-THAN expression if
   ** the native resource supports LESS-THAN.
   **
   ** @param  filter             the less than filter.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LESS-THAN expression.
   **                            A return value of <code>null</code> means a
   **                            native LESS-THAN query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createLT(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLE
  /**
   ** Should be overridden by subclasses to create a LESS-THAN-EQUAL expression
   ** if the native resource supports LESS-THAN-EQUAL.
   **
   ** @param  filter             the less than or equal filter.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN-EQUAL.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LESS-THAN-EQUAL expression.
   **                            A return value of <code>null</code> means a
   **                            native LESS-THAN-EQUAL query cannot be created
   **                            for the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createLE(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSW
  /**
   ** Should be overridden by subclasses to create an STARTS-WITH expression if
   ** the native resource supports STARTS-WITH.
   **
   ** @param  filter             the starts with filter.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            STARTS-WITH.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the STARTS-WITH expression.
   **                            A return value of <code>null</code> means a
   **                            native STARTS-WITH query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createSW(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEW
  /**
   ** Should be overridden by subclasses to create an ENDS-WITH expression if
   ** the native resource supports ENDS-WITH.
   **
   ** @param  filter             the ends with filter.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            ENDS-WITH.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the ENDS-WITH expression.
   **                            A return value of <code>null</code> means a
   **                            native ENDS-WITH query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createEW(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCO
  /**
   ** Should be overridden by subclasses to create an CONTAINS expression if the
   ** native resource supports CONTAINS.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            CONTAINS.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the CONTAINS expression.
   **                            A return value of <code>null</code> means a
   **                            native CONTAINS query cannot be created for the
   **                            given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T createCO(final Filter<T> filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Pushes Not's so that they are just before the leaves of the tree
   **
   ** @param  filter             a {@link Filter}.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   **
   ** @return                    a mormalized filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  private Filter<T> normalize(final Filter<T> filter) {
    switch (filter.type()) {
      case OR  : return Filter.or(normalize(((CompositeFilter<T>)filter).lhs()), normalize(((CompositeFilter<T>)filter).rhs()));
      case AND : return Filter.and(normalize(((CompositeFilter<T>)filter).lhs()), normalize(((CompositeFilter<T>)filter).rhs()));
      case NOT : return negate(normalize(((Not<T>)filter).filter()));
      default  : return filter;
    }  
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   negate
  /**
   ** Given a filter, create a filter representing its negative.
   ** <br>
   ** This is used by normalize.
   **
   ** @param  filter             a {@link Filter} (normalized, simplified, and
   **                            distibuted).
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   **
   ** @return                    a negated filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  private Filter<T> negate(final Filter<T> filter) {
    switch (filter.type()) {
      case OR  : return Filter.and(negate(((CompositeFilter<T>)filter).lhs()), negate(((CompositeFilter<T>)filter).rhs()));
      case AND : return Filter.or(negate(((CompositeFilter<T>)filter).lhs()), negate(((CompositeFilter<T>)filter).rhs()));
      case NOT : return ((Not<T>)filter).filter();
      default  : return Filter.not(filter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simplify
  /**
   ** Simultaneously prunes those portions of the filter than cannot be
   ** implemented and distributes Ands over Ors where needed if the resource
   ** does not implement Or.
   **
   ** @param  filter             {@link Not}s must already be normalized.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   **
   ** @return                    a simplified filter or <code>null</code> to
   **                            represent the "everything" filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            <code>T</code>.
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  @SuppressWarnings("unchecked")
  private Filter<T> simplify(final Filter<T> filter)
    throws ProcessingException {

    if (filter instanceof And) {
      final And<T>    af  = (And<T>)filter;
      final Filter<T> lhs = simplify(af.lhs());
      final Filter<T> rhs = simplify(af.rhs());
      if (lhs == null) {
        // left is "everything" - just return the right
        return rhs;
      }
      else if (rhs == null) {
        // right is "everything" - just return the left
        return lhs;
      }
      else {
        // simulate translation of the left and right to see where we end up
        final List<T> lex = translateInternal(lhs);
        final List<T> rex = translateInternal(rhs);
        if (lex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (lhs should have been
          // null in the previous 'if' above)
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, lex));
        }
        if (rex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (rhs should have been
          // null in the previous 'if' above)
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, rex));
        }

        // simulate ANDing each pair(lhs, rhs)
        // if all of them return null (i.e., "everything"), then the request
        // cannot be filtered
        boolean possibleAnd = false;
        for (T l : lex) {
          for (T r : rex) {
            T test = createAND(l, r);
            if (test != null) {
              possibleAnd = true;
              break;
            }
          }
          if (possibleAnd) {
            break;
          }
        }
        // if no AND filtering is possible, return whichever of lhs or rhs
        // contains the fewest expressions
        if (!possibleAnd) {
          if (lex.size() <= rex.size()) {
            return lhs;
          }
          else {
            return rhs;
          }
        }
        // since AND filtering is possible for at least one expression, let's
        // distribute
        if (lex.size() > 1) {
          // the lhs can contain more than one expression only if the left-hand
          // side is an unimplemented OR; distribute our AND to the left
          final Or<T> left      = (Or<T>)lhs;
          final Or<T> newFilter = new Or<T>(new And<T>(left.lhs(), rhs), new And<T>(left.rhs(), rhs));
          return simplify(newFilter);
        }
        else if (rex.size() > 1) {
          // the rhs can contain more than one expression only if the right-hand
          // side is an unimplemented OR; distribute our AND to the right
          final Or<T> right     = (Or<T>)rhs;
          final Or<T> newFilter = new Or<T>(new And<T>(lhs, right.lhs()), new And<T>(lhs, right.rhs()));
          return simplify(newFilter);
        }
        else {
          // each side contains exactly one expression and the translator does
          // implement AND (possibleAnd must be true for them to have hit this
          // branch)
          assert possibleAnd;
          return new And<T>(lhs, rhs);
        }
      }
    }
    else if (filter instanceof Or) {
      final Or<T> of = (Or<T>)filter;
      final Filter<T> lhs = simplify(of.lhs());
      final Filter<T> rhs = simplify(of.rhs());
      // if either lhs or rhs reduces to "everything", then simplify the OR
      // to "everything"
      if (lhs == null || rhs == null) {
        return null;
      }
      // otherwise
      return new Or<T>(lhs, rhs);
    }
    else {
      // otherwise, it's a NOT(LEAF) or a LEAF; simulate creating it
      T exp = createLeafExpression(filter);
      if (exp == null) {
        // if the expression cannot be implemented, return the "everything"
        // filter
        return null;
      }
      else {
        // otherwise, return the filter
        return filter;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translateInternal
  /**
   ** Translates the filter into a list of expressions.
   ** <p>
   ** The filter must have already been transformed using
   ** {@link #normalize(Filter)} followed by a {@link #simplify(Filter)}.
   **
   ** @param  filter             a {@link Filter} (normalized, simplified, and
   **                            distibuted).
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   **
   ** @return                    a {@link List} of expressions or empty
   **                            {@link List} for everything.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  private List<T> translateInternal(final Filter<T> filter)
    throws ProcessingException {

    if (filter instanceof And) {
      final T       result = translateAnd((And<T>)filter);
      final List<T> rv     = new ArrayList<T>();
      if (result != null) {
        rv.add(result);
      }
      return rv;
    }
    else if (filter instanceof Or) {
      return translateOr((Or<T>)filter);
    }
    else {
      // otherwise it's either a leaf or a NOT (leaf)
      T expr = createLeafExpression(filter);
      List<T> exprs = new ArrayList<T>();
      if (expr != null) {
        exprs.add(expr);
      }
      return exprs;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translateAnd
  /**
   ** Translates the {@link And} filter into a list of expressions.
   ** <p>
   ** The filter must have already been transformed using
   ** {@link #normalize(Filter)} followed by a {@link #simplify(Filter)}.
   **
   ** @param  filter             an {@link And} filter (normalized, simplified,
   **                            and distibuted).
   **                            <br>
   **                            Allowed object is {@link And} for type
   **                            <code>T</code>.
   **
   ** @return                    a filter expression.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  @SuppressWarnings("unchecked")
  private T translateAnd(final And<T> filter)
    throws ProcessingException {

    final List<T> lhs = translateInternal(filter.lhs());
    final List<T> rhs = translateInternal(filter.rhs());
    if (lhs.size() != 1)
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed)
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, lhs));

    if (rhs.size() != 1) {
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed)
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, rhs));
    }
    T rv = createAND(lhs.get(0), rhs.get(0));
    if (rv == null) {
      // this could happen only if we're inconsistent since the simplify logic
      // already should have removed any expression that cannot be filtered
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, "createAND"));
    }
    return rv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translateOr
  /**
   ** Translates the {@link Or} filter into a list of expressions.
   ** <p>
   ** The filter must have already been transformed using
   ** {@link #normalize(Filter)} followed by a {@link #simplify(Filter)}.
   **
   ** @param  filter             an {@link Or} filter (normalized, simplified,
   **                            and distibuted).
   **                            <br>
   **                            Allowed object is {@link Or} for type
   **                            <code>T</code>.
   **
   ** @return                    a {@link List} of expressions or empty
   **                            {@link List} for everything.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  @SuppressWarnings("unchecked")
  private List<T> translateOr(final Or<T> filter)
    throws ProcessingException {

    final List<T> lhs = translateInternal(filter.lhs());
    final List<T> rhs = translateInternal(filter.rhs());
    if (lhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed)
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, lhs));

    if (rhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed)
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, rhs));

    if (lhs.size() == 1 && rhs.size() == 1) {
      // if each side contains exactly one expression, try to create a combined
      // expression
      T val = createOR(lhs.get(0), rhs.get(0));
      if (val != null) {
        List<T> rv = new ArrayList<T>();
        rv.add(val);
        return rv;
      }
      // otherwise, fall through
    }

    // return a list of queries from the left and from the right
    List<T> rv = new ArrayList<T>(lhs.size() + rhs.size());
    rv.addAll(lhs);
    rv.addAll(rhs);
    return rv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLeafExpression
  /**
   ** Creates an expression for a LEAF or a NOT(leaf)
   **
   ** @param  filter             must be either a leaf or a NOT(leaf).
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   **
   ** @return                    the expression.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  private T createLeafExpression(final Filter<T> filter) {
    Filter<T> leaf;
    boolean not;
    if (filter instanceof Not) {
      leaf = ((Not<T>)filter).filter();
      not = true;
    }
    else {
      leaf = filter;
      not = false;
    }
    return createLeafExpression(leaf, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLeafExpression
  /**
   ** Creates a Leaf expression
   **
   ** @param  filter             must be either a leaf expression.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>T</code>.
   ** @param  not                whether ! to be applied to the leaf expression.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the expression or <code>null</code> (for
   **                            everything)
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  private T createLeafExpression(final Filter<T> filter, boolean not) {
    switch (filter.type()) {
      case EQ : return createEQ(filter, not);
      case GT : return createGT(filter, not);
      case GE : return createGE(filter, not);
      case LT : return createLT(filter, not);
      case LE : return createLE(filter, not);
      case PR : return createPR(filter, not);
      case SW : return createSW(filter, not);
      case EW : return createEW(filter, not);
      case CO : return createCO(filter, not);
      // unrecognized expression - nothing we can do
      default : return null;
    }
  }
}