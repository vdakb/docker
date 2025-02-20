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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Translator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Translator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.object;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.BadRequestException;

import oracle.iam.system.simulation.resource.ServiceBundle;

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
 **   <li>{@link #createPR(Presence, boolean)}
 **   <li>{@link #createEQ(Equals, boolean)}
 **   <li>{@link #createGT(GreaterThan, boolean)}
 **   <li>{@link #createGE(GreaterThanOrEqual, boolean)}
 **   <li>{@link #createSW(StartsWith, boolean)}
 **   <li>{@link #createEW(EndsWith, boolean)}
 **   <li>{@link #createCO(Contains, boolean)}
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
  public final List<T> translate(Filter filter)
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
   **                            Allowed object is {@link Presence}.
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
  protected T createPR(@SuppressWarnings("unused") final Presence filter, @SuppressWarnings("unused") final boolean not) {
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
   **                            Allowed object is {@link Equals}.
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
  protected T createEQ(@SuppressWarnings("unused") final Equals filter, @SuppressWarnings("unused") final boolean not) {
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
   **                            Allowed object is {@link GreaterThan}.
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
  protected T createGT(@SuppressWarnings("unused") final GreaterThan filter, @SuppressWarnings("unused") final boolean not) {
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
   **                            Allowed object is
   **                            {@link GreaterThanOrEqual}.
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
  protected T createGE(@SuppressWarnings("unused") final GreaterThanOrEqual filter, @SuppressWarnings("unused") final boolean not) {
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
   **                            Allowed object is {@link LessThan}.
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
  protected T createLT(@SuppressWarnings("unused") final LessThan filter, @SuppressWarnings("unused") final boolean not) {
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
   **                            Allowed object is {@link LessThanOrEqual}.
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
  protected T createLE(@SuppressWarnings("unused") final LessThanOrEqual filter, @SuppressWarnings("unused") final boolean not) {
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
   **                            Allowed object is {@link StartsWith}.
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
  protected T createSW(@SuppressWarnings("unused") final StartsWith filter, @SuppressWarnings("unused") final boolean not) {
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
   **                            Allowed object is {@link EndsWith}.
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
  protected T createEW(@SuppressWarnings("unused") final EndsWith filter, @SuppressWarnings("unused") final boolean not) {
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
   **                            Allowed object is {@link Contains}.
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
  protected T createCO(@SuppressWarnings("unused") final Contains filter, @SuppressWarnings("unused") final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Pushes Not's so that they are just before the leaves of the tree
   */
  private Filter normalize(final Filter filter) {
    if (filter instanceof And) {
      And af = (And)filter;
      return new And(normalize(af.lhs()), normalize(af.rhs()));
    }
    else if (filter instanceof Or) {
      Or of = (Or)filter;
      return new Or(normalize(of.lhs()), normalize(of.rhs()));
    }
    else if (filter instanceof Not) {
      Not nf = (Not)filter;
      return negate(normalize(nf.filter()));
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
  // Method:   simplify
  /**
   ** Simultaneously prunes those portions of the filter than cannot be
   ** implemented and distributes Ands over Ors where needed if the resource
   ** does not implement Or.
   **
   ** @param  filter              {@link Not}s must already be normalized.
   **
   ** @return                     a simplified filter or <code>null</code> to
   **                             represent the "everything" filter.
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  private Filter simplify(final Filter filter)
    throws ProcessingException {

    if (filter instanceof And) {
      final And    af  = (And)filter;
      final Filter lhs = simplify(af.lhs());
      final Filter rhs = simplify(af.rhs());
      if (lhs == null) {
        // left is "everything" - just return the right
        return rhs;
      }
      else if (rhs == null) {
        // right is "everything" - just return the left
        return lhs;
      }
      else {
        // simulate translation of the left and right
        // to see where we end up
        final List<T> lex = translateInternal(lhs);
        final List<T> rex = translateInternal(rhs);
        if (lex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedLeft should
          // have been null in the previous 'if' above).
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, lex));
        }
        if (rex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedRight
          // should have been null in the previous 'if' above).
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, rex));
        }

        // simulate ANDing each pair(left,right).
        // if all of them return null (i.e., "everything"), then the request
        // cannot be filtered.
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

        // if no AND filtering is possible, return whichever of left or right
        // contains the fewest expressions.
        if (!possibleAnd) {
          if (lex.size() <= rex.size()) {
            return lhs;
          }
          else {
            return rhs;
          }
        }

        // since AND filtering is possible for at least one expression, let's
        // distribute.
        if (lex.size() > 1) {
          // the left can contain more than one expression only if the left-hand
          // side is an unimplemented OR.
          // distribute our AND to the left.
          final Or left      = (Or)lhs;
          final Or newFilter = new Or(new And(left.lhs(), rhs), new And(left.rhs(), rhs));
          return simplify(newFilter);
        }
        else if (rex.size() > 1) {
          // the right can contain more than one expression only if the
          // right-hand side is an unimplemented OR.
          // distribute our AND to the right.
          final Or right     = (Or)rhs;
          final Or newFilter = new Or(new And(lhs, right.lhs()), new And(lhs, right.rhs()));
          return simplify(newFilter);
        }
        else {
          // each side contains exactly one expression and the translator does
          // implement AND (anyAndsPossible must be true for them to have hit
          // this branch).
          assert possibleAnd;
          return new And(lhs, rhs);
        }
      }
    }
    else if (filter instanceof Or) {
      final Or of = (Or)filter;
      final Filter lhs = simplify(of.lhs());
      final Filter rhs = simplify(of.rhs());
      // if either left or right reduces to "everything", then simplify the OR
      // to "everything".
      if (lhs == null || rhs == null) {
        return null;
      }
      // otherwise
      return new Or(lhs, rhs);
    }
    else {
      // otherwise, it's a NOT(LEAF) or a LEAF.
      // simulate creating it.
      T exp = createLeafExpression(filter);
      if (exp == null) {
        // if the expression cannot be implemented,
        // return the "everything" filter.
        return null;
      }
      else {
        // otherwise, return the filter.
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
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  private List<T> translateInternal(final Filter filter)
    throws ProcessingException {

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
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  private T translateAnd(final And filter)
    throws ProcessingException {

    final List<T> lhs = translateInternal(filter.lhs());
    final List<T> rhs = translateInternal(filter.rhs());
    if (lhs.size() != 1)
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, lhs));

    if (rhs.size() != 1) {
      // this can happen only if one of the create* methods
      // is inconsistent from one invocation to the next
      // (at this point we've already been simplified and
      // distributed).
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
   ** {@link #normalizeNot(Filter)} followed by a
   ** {@link #simplifyAndDistribute(Filter)}.
   **
   ** @param  filter              an {@link And} filter (normalized, simplified,
   **                             and distibuted).
   **
   ** @return                     a {@link List} of expressions or empty
   **                             {@link List} for everything.
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  private List<T> translateOr(final Or filter)
    throws ProcessingException {

    final List<T> lhs = translateInternal(filter.lhs());
    final List<T> rhs = translateInternal(filter.rhs());
    if (lhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, lhs));

    if (rhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INCONSISTENT_METHOD, rhs));

    if (lhs.size() == 1 && rhs.size() == 1) {
      // If each side contains exactly one expression,
      // try to create a combined expression.
      T val = createOR(lhs.get(0), rhs.get(0));
      if (val != null) {
        List<T> rv = new ArrayList<T>();
        rv.add(val);
        return rv;
      }
      // Otherwise, fall through
    }

    // Return a list of queries from the left and from the right
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
    switch (filter.type()) {
      case EQ : return createEQ((Equals)filter, not);
      case GT : return createGT((GreaterThan)filter, not);
      case GE : return createGE((GreaterThanOrEqual)filter, not);
      case LT : return createLT((LessThan)filter, not);
      case LE : return createLE((LessThanOrEqual)filter, not);
      case PR : return createPR((Presence)filter, not);
      case SW : return createSW((StartsWith)filter, not);
      case EW : return createEW((EndsWith)filter, not);
      case CO : return createCO((Contains)filter, not);
      // unrecognized expression - nothing we can do
      default : return null;
    }
  }
}