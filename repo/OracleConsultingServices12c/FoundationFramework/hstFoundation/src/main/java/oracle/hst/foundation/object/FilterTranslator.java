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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared collection facilities

    File        :   FilterTranslator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterTranslator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import oracle.hst.foundation.object.filter.Or;
import oracle.hst.foundation.object.filter.Not;
import oracle.hst.foundation.object.filter.And;
import oracle.hst.foundation.object.filter.Equals;
import oracle.hst.foundation.object.filter.Chained;
import oracle.hst.foundation.object.filter.Contains;
import oracle.hst.foundation.object.filter.EndsWith;
import oracle.hst.foundation.object.filter.LessThan;
import oracle.hst.foundation.object.filter.ContainsAll;
import oracle.hst.foundation.object.filter.GreaterThan;
import oracle.hst.foundation.object.filter.StartsWith;
import oracle.hst.foundation.object.filter.LessThanOrEqual;
import oracle.hst.foundation.object.filter.GreaterThanOrEqual;
import oracle.hst.foundation.object.filter.Presence;

////////////////////////////////////////////////////////////////////////////////
// abstract class FilterTranslator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
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
 **   <li>{@link #createOr}
 **   <li>{@link #createAnd}
 **   <li>{@link #createPresence(Presence, boolean)}
 **   <li>{@link #createEquals(Equals, boolean)}
 **   <li>{@link #createGreaterThan(GreaterThan, boolean)}
 **   <li>{@link #createGreaterThanOrEqual(GreaterThanOrEqual, boolean)}
 **   <li>{@link #createStartsWith(StartsWith, boolean)}
 **   <li>{@link #createEndsWith(EndsWith, boolean)}
 **   <li>{@link #createContains(Contains, boolean)}
 **   <li>{@link #createContainsAll(ContainsAll, boolean)}
 ** </ol>
 ** <p>
 ** Translation can then be performed using {@link #translate(Filter)}.
 ** <p>
 **
 ** @param  <T>                  the result type of the translator.
 **                              Commonly this will be a string, but there are
 **                              cases where you might need to return a more
 **                              complex data structure. For example if you are
 **                              building a SQL query, you will need not
 **                              *just* the base WHERE clause but a list of
 **                              tables that need to be joined together.
 */
public abstract class FilterTranslator<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Construct a new filter translator.
   */
  protected FilterTranslator() {
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
   **                                    {@link #createOr} method can return
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
   **                                    {@link #createOr}
   **                               </ol>
   */
  public final List<T> translate(Filter filter) {
    if (filter == null)
      return new ArrayList<T>();

    // this must come first
    filter = eliminateChainedFilter(filter);
    filter = normalizeNot(filter);
    filter = simplifyAndDistribute(filter);
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
  // Method:   createOr
  /**
   ** Should be overridden by subclasses to create an OR expression if the
   ** native resource supports OR.
   **
   ** @param  lhs                the left expression. Will never be
   **                            <code>null</code>.
   ** @param  rhs                the right expression. Will never be
   **                            <code>null</code>.
   **
   ** @return                    the OR expression.
   **                            A return value of <code>null</code> means a
   **                            native OR query cannot be created for the
   **                            given expressions. In this case,
   **                            {@link #translate} may return multiple queries,
   **                            each of which must be run and results combined.
   */
  protected T createOr(final T lhs, final T rhs) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAnd
  /**
   ** Should be overridden by subclasses to create an AND expression if the
   ** native resource supports AND.
   **
   ** @param  lhs                the left expression. Will never be
   **                            <code>null</code>.
   ** @param  rhs                the right expression. Will never be
   **                            <code>null</code>.
   **
   ** @return                    the AND expression.
   **                            A return value of <code>null</code> means a
   **                            native AND query cannot be created for the
   **                            given expressions. In this case, the resulting
   **                            query will consist of the leftExpression only.
   */
  protected T createAnd(final T lhs, final T rhs) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPresence
  /**
   ** Should be overridden by subclasses to create an PRESENT expression if the
   ** native resource supports PRESENCE.
   **
   **
   ** @param  filter             the presence filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            PRESENT.
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
   */
  protected T createPresence(final Presence filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEquals
  /**
   ** Should be overridden by subclasses to create an EQUALS expression if the
   ** native resource supports EQUALS.
   **
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            EQUALS.
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
   */
  protected T createEquals(final Equals filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGreaterThan
  /**
   ** Should be overridden by subclasses to create a GREATER-THAN expression if
   ** the native resource supports GREATER-THAN.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN.
   **
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
   */
  protected T createGreaterThan(final GreaterThan filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGreaterThanOrEqual
  /**
   ** Should be overridden by subclasses to create a GREATER-THAN-EQUAL
   ** expression if the native resource supports GREATER-THAN-EQUAL.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN-EQUAL.
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
   */
  protected T createGreaterThanOrEqual(final GreaterThanOrEqual filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLessThan
  /**
   ** Should be overridden by subclasses to create a LESS-THAN expression if
   ** the native resource supports LESS-THAN.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN.
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
   */
  protected T createLessThan(final LessThan filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLessThanOrEqual
  /**
   ** Should be overridden by subclasses to create a LESS-THAN-EQUAL expression
   ** if the native resource supports LESS-THAN-EQUAL.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN-EQUAL.
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
   */
  protected T createLessThanOrEqual(final LessThanOrEqual filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStartsWith
  /**
   ** Should be overridden by subclasses to create an STARTS-WITH expression if
   ** the native resource supports STARTS-WITH.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            STARTS-WITH.
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
   */
  protected T createStartsWith(final StartsWith filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEndsWith
  /**
   ** Should be overridden by subclasses to create an ENDS-WITH expression if
   ** the native resource supports ENDS-WITH.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            ENDS-WITH.
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
   */
  protected T createEndsWith(final EndsWith filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContains
  /**
   ** Should be overridden by subclasses to create an CONTAINS expression if the
   ** native resource supports CONTAINS.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            CONTAINS.
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
   */
  protected T createContains(final Contains filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContainsAll
  /**
   ** Should be overridden by subclasses to create an CONTAINS-ALL expression if
   ** the native resource supports a CONTAINS-ALL.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            CONTAINS-ALL.
   **
   ** @return                    the CONTAINS-ALL expression.
   **                            A return value of <code>null</code> means a
   **                            native CONTAINS-ALL query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  protected T createContainsAll(final ContainsAll filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eliminateChainedFilter
  private Filter eliminateChainedFilter(Filter filter) {
    while (filter instanceof Chained) {
      filter = ((Chained)filter).filter();
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizeNot
  /**
   ** Pushes Not's so that they are just before the leaves of the tree
   */
  private Filter normalizeNot(final Filter filter) {
    if (filter instanceof And) {
      And af = (And)filter;
      return new And(normalizeNot(af.lhs()), normalizeNot(af.rhs()));
    }
    else if (filter instanceof Or) {
      Or of = (Or)filter;
      return new Or(normalizeNot(of.lhs()), normalizeNot(of.rhs()));
    }
    else if (filter instanceof Not) {
      Not nf = (Not)filter;
      return negate(normalizeNot(nf.filter()));
    }
    else {
      return filter;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   negate
  /**
   ** Given a filter, create a filter representing its negative. This is used
   ** by normalizeNot.
   */
  private Filter negate(final Filter filter) {
    if (filter instanceof And) {
      And af = (And)filter;
      return new Or(negate(af.lhs()), negate(af.rhs()));
    }
    else if (filter instanceof Or) {
      Or of = (Or)filter;
      return new And(negate(of.lhs()), negate(of.rhs()));
    }
    else if (filter instanceof Not) {
      Not nf = (Not)filter;
      return nf.filter();
    }
    else {
      return new Not(filter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simplifyAndDistribute
  /**
   ** Simultaneously prunes those portions of the filter than cannot be
   ** implemented and distributes Ands over Ors where needed if the resource
   ** does not implement Or.
   **
   ** @param  filter              {@link Not}s must already be normalized.
   **
   ** @return                     a simplified filter or <code>null</code> to
   **                             represent the "everything" filter.
   */
  private Filter simplifyAndDistribute(final Filter filter) {
    if (filter instanceof And) {
      final And af = (And)filter;
      final Filter simplifiedLeft = simplifyAndDistribute(af.lhs());
      final Filter simplifiedRight = simplifyAndDistribute(af.rhs());
      if (simplifiedLeft == null) {
        // left is "everything" - just return the right
        return simplifiedRight;
      }
      else if (simplifiedRight == null) {
        // right is "everything" - just return the left
        return simplifiedLeft;
      }
      else {
        // simulate translation of the left and right
        // to see where we end up
        final List<T> leftExprs  = translateInternal(simplifiedLeft);
        final List<T> rightExprs = translateInternal(simplifiedRight);
        if (leftExprs.isEmpty()) {
          // This can happen only when one of the create* methods
          // is inconsistent from one invocation to the next
          // (simplifiedLeft should have been null
          // in the previous 'if' above).
          throw new IllegalStateException("Translation method is inconsistent: " + leftExprs);
        }
        if (rightExprs.isEmpty()) {
          // This can happen only when one of the create* methods
          // is inconsistent from one invocation to the next
          // (simplifiedRight should have been null
          // in the previous 'if' above).
          throw new IllegalStateException("Translation method is inconsistent: " + rightExprs);
        }

        // Simulate ANDing each pair(left,right).
        // If all of them return null (i.e., "everything"),
        // then the request cannot be filtered.
        boolean anyAndsPossible = false;
        for (T leftExpr : leftExprs) {
          for (T rightExpr : rightExprs) {
            T test = createAnd(leftExpr, rightExpr);
            if (test != null) {
              anyAndsPossible = true;
              break;
            }
          }
          if (anyAndsPossible) {
            break;
          }
        }

        // If no AND filtering is possible,
        // return whichever of left or right
        // contains the fewest expressions.
        if (!anyAndsPossible) {
          if (leftExprs.size() <= rightExprs.size()) {
            return simplifiedLeft;
          }
          else {
            return simplifiedRight;
          }
        }

        // Since AND filtering is possible for at least one expression, let's
        // distribute.
        if (leftExprs.size() > 1) {
          // the left can contain more than one expression only if the left-hand
          // side is an unimplemented OR.
          // distribute our AND to the left.
          final Or left      = (Or)simplifiedLeft;
          final Or newFilter = new Or(new And(left.lhs(), simplifiedRight), new And(left.rhs(), simplifiedRight));
          return simplifyAndDistribute(newFilter);
        }
        else if (rightExprs.size() > 1) {
          // the right can contain more than one expression only if the
          // right-hand side is an unimplemented OR.
          // distribute our AND to the right.
          final Or right     = (Or)simplifiedRight;
          final Or newFilter = new Or(new And(simplifiedLeft, right.lhs()), new And(simplifiedLeft, right.rhs()));
          return simplifyAndDistribute(newFilter);
        }
        else {
          // each side contains exactly one expression and the translator does
          // implement AND (anyAndsPossible must be true for them to have hit
          // this branch).
          assert anyAndsPossible;
          return new And(simplifiedLeft, simplifiedRight);
        }
      }
    }
    else if (filter instanceof Or) {
      final Or of = (Or)filter;
      final Filter simplifiedLeft  = simplifyAndDistribute(of.lhs());
      final Filter simplifiedRight = simplifyAndDistribute(of.rhs());
      // If either left or right reduces to "everything",
      // then simplify the OR to "everything".
      if (simplifiedLeft == null || simplifiedRight == null) {
        return null;
      }
      // otherwise
      return new Or(simplifiedLeft, simplifiedRight);
    }
    else {
      // Otherwise, it's a NOT(LEAF) or a LEAF.
      // Simulate creating it.
      T expr = createLeafExpression(filter);
      if (expr == null) {
        // If the expression cannot be implemented,
        // return the "everything" filter.
        return null;
      }
      else {
        // Otherwise, return the filter.
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
   ** {@link #normalizeNot(Filter)} followed by a
   ** {@link #simplifyAndDistribute(Filter)}.
   **
   ** @param  filter              a {@link Filter} (normalized, simplified, and
   **                             distibuted).
   **
   ** @return                     a {@link List} of expressions or empty
   **                             {@link List} for everything.
   */
  private List<T> translateInternal(final Filter filter) {
    if (filter instanceof And) {
      final T       result = translateAnd((And)filter);
      final List<T> rv     = new ArrayList<T>();
      if (result != null) {
        rv.add(result);
      }
      return rv;
    }
    else if (filter instanceof Or) {
      return translateOr((Or)filter);
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
   ** {@link #normalizeNot(Filter)} followed by a
   ** {@link #simplifyAndDistribute(Filter)}.
   **
   ** @param  filter              an {@link And} filter (normalized, simplified,
   **                             and distibuted).
   **
   ** @return                     a {@link List} of expressions or empty
   **                             {@link List} for everything.
   */
  private T translateAnd(final And filter) {
    final List<T> leftExprs  = translateInternal(filter.lhs());
    final List<T> rightExprs = translateInternal(filter.rhs());
    if (leftExprs.size() != 1)
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw new IllegalStateException("Translation method is inconsistent: " + leftExprs);

    if (rightExprs.size() != 1) {
      // this can happen only if one of the create* methods
      // is inconsistent from one invocation to the next
      // (at this point we've already been simplified and
      // distributed).
      throw new IllegalStateException("Translation method is inconsistent: " + rightExprs);
    }
    T rv = createAnd(leftExprs.get(0), rightExprs.get(0));
    if (rv == null) {
      // This could happen only if we're inconsistent
      // (since the simplify logic already should have removed
      // any expression that cannot be filtered).
      throw new IllegalStateException("createAndExpression is inconsistent");
    }
    return rv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translateOr
  /**
   ** Translates the {@link Or} filter into a list of expressions.
   ** <p>
   ** The filter must have already been transformed using
   ** {@link #normalizeNot(Filter)} followed by a
   ** {@link #simplifyAndDistribute(Filter)}.
   **
   ** @param  filter              an {@link And} filter (normalized, simplified,
   **                             and distibuted).
   **
   ** @return                     a {@link List} of expressions or empty
   **                             {@link List} for everything.
   */
  private List<T> translateOr(final Or filter) {
    final List<T> leftExprs  = translateInternal(filter.lhs());
    final List<T> rightExprs = translateInternal(filter.rhs());
    if (leftExprs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw new IllegalStateException("Translation method is inconsistent: " + leftExprs);

    if (rightExprs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw new IllegalStateException("Translation method is inconsistent: " + rightExprs);

    if (leftExprs.size() == 1 && rightExprs.size() == 1) {
      // If each side contains exactly one expression,
      // try to create a combined expression.
      T val = createOr(leftExprs.get(0), rightExprs.get(0));
      if (val != null) {
        List<T> rv = new ArrayList<T>();
        rv.add(val);
        return rv;
      }
      // Otherwise, fall through
    }

    // Return a list of queries from the left and from the right
    List<T> rv = new ArrayList<T>(leftExprs.size() + rightExprs.size());
    rv.addAll(leftExprs);
    rv.addAll(rightExprs);
    return rv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLeafExpression
  /**
   ** Creates an expression for a LEAF or a NOT(leaf)
   **
   ** @param  filter             must be either a leaf or a NOT(leaf)
   **
   ** @return                    the expression
   */
  private T createLeafExpression(final Filter filter) {
    Filter leaf;
    boolean not;
    if (filter instanceof Not) {
      leaf = ((Not)filter).filter();
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
   *
   * @param filter
   *            Must be a leaf expression
   * @param not
   *            Is ! to be applied to the leaf expression
   * @return The expression or null (for everything)
   */
  private T createLeafExpression(final Filter filter, boolean not) {
    if (filter instanceof Contains) {
      return createContains((Contains)filter, not);
    }
    else if (filter instanceof ContainsAll) {
      return createContainsAll((ContainsAll)filter, not);
    }
    else if (filter instanceof EndsWith) {
      return createEndsWith((EndsWith)filter, not);
    }
    else if (filter instanceof StartsWith) {
      return createStartsWith((StartsWith)filter, not);
    }
    else if (filter instanceof Presence){
      return createPresence((Presence)filter, not);
    }
    else if (filter instanceof Equals){
      return createEquals((Equals)filter, not);
    }
    else if (filter instanceof GreaterThan) {
      return createGreaterThan((GreaterThan)filter, not);
    }
    else if (filter instanceof GreaterThanOrEqual) {
      return createGreaterThanOrEqual((GreaterThanOrEqual)filter, not);
    }
    else if (filter instanceof LessThan) {
      return createLessThan((LessThan)filter, not);
    }
    else if (filter instanceof LessThanOrEqual) {
      return createLessThanOrEqual((LessThanOrEqual)filter, not);
    }
    else {
      // unrecognized expression - nothing we can do
      return null;
    }
  }
}