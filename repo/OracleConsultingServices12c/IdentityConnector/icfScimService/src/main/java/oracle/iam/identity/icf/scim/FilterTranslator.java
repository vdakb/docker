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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Library

    File        :   FilterTranslator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterTranslator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.OrFilter;
import org.identityconnectors.framework.common.objects.filter.AndFilter;
import org.identityconnectors.framework.common.objects.filter.NotFilter;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanOrEqualFilter;

////////////////////////////////////////////////////////////////////////////////
// abstract class FilterTranslator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Base class to make it easier to implement a search.
 ** <p>
 ** A search filter may contain operators (such as 'contains' or 'in') or may
 ** contain logical operators (such as 'AND', 'OR' or 'NOT') that a connector
 ** cannot implement using the native API of the target system or application. A
 ** connector developer should subclass <code>Translator</code> in order to
 ** declare which filter operations the connector does support. This allows
 ** the <code>Translator</code> instance to analyze a specified search filter
 ** and reduce the filter to its most efficient form. The default (and
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
 **   <li>{@link #createEQ(EqualsFilter, boolean)}
 **   <li>{@link #createGT(GreaterThanFilter, boolean)}
 **   <li>{@link #createGE(GreaterThanOrEqualFilter, boolean)}
 **   <li>{@link #createLT(LessThanFilter, boolean)}
 **   <li>{@link #createLE(LessThanOrEqualFilter, boolean)}
 **   <li>{@link #createSW(StartsWithFilter, boolean)}
 **   <li>{@link #createEW(EndsWithFilter, boolean)}
 **   <li>{@link #createCO(ContainsFilter, boolean)}
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
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class FilterTranslator<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String NE = " ne ";
  public static final String EQ = " eq ";
  public static final String GT = " gt ";
  public static final String GE = " ge ";
  public static final String LT = " lt ";
  public static final String LE = " le ";
  public static final String EW = " ew ";
  public static final String CO = " co ";
  public static final String SW = " sw ";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Switcher
  // ~~~~ ~~~~~~~~
  /**
   ** enum type that stores the class names as field
   */
  public static enum Switcher {
      EQ(EqualsFilter.class)
    , GE(GreaterThanOrEqualFilter.class)
    , GT(GreaterThanFilter.class)
    , LE(LessThanOrEqualFilter.class)
    , LT(LessThanFilter.class)
    , EW(EndsWithFilter.class)
    , SW(StartsWithFilter.class)
    , CO(ContainsFilter.class)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final Map<String, Switcher> classEnum = new HashMap<String, Switcher>();

    ////////////////////////////////////////////////////////////////////////////
    // static init bloack
    ////////////////////////////////////////////////////////////////////////////

    static {
      for (Switcher sw : values()) {
        classEnum.put(sw.className, sw);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String className;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Switcher</code> with a constraint value.
     **
     ** @param  value            the class type of the object.
     **                          <br>
     **                          Allowed object is {@link Class}.
     */
    Switcher(Class<?> clazz) {
      this.className = clazz.getName();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Switcher</code> constraint from
     ** the given string value.
     **
     ** @param  className        the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Switcher</code> constraint.
     **                          <br>
     **                          Possible object is <code>Switcher</code>.
     */
    public static Switcher from(final String className) {
      return classEnum.get(className);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Construct a new filter translator.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
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
   **                               <li>&lt;1 - List contains multiple queries
   **                                   that must be performed in order to meet
   **                                   the filter that was passed in.
   **                                   Note that this only occurs if your
   **                                   {@link #createOR} method can return
   **                                   <code>null</code>. If this happens, it
   **                                   is the responsibility of the
   **                                   implementor to perform each query and
   **                                   combine the results. In order to
   **                                   eliminate duplicates, the
   **                                   implementation must keep an in-memory
   **                                   <code>HashSet</code> of those UID that
   **                                   have been visited thus far. This will
   **                                   not scale well if your result sets are
   **                                   large. Therefore it is
   **                                   <b>recommended</b> that if at all
   **                                   possible you implement
   **                                   {@link #createOR}.
   **                             </ol>
   **
   ** @throws ServiceException   if filter syntax becomes invalid.
   */
  public final List<T> translate(Filter filter)
    throws ServiceException {

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
   ** @param  lhs                the left hand side filter expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the right hand side filter expression.
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
  protected abstract T createOR(final T lhs, final T rhs);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAND
  /**
   ** Should be overridden by subclasses to create an AND expression if the
   ** native resource supports AND.
   **
   ** @param  lhs                the left hand side filter expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the right hand side filter expression.
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
  protected abstract T createAND(final T lhs, final T rhs);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEQ
  /**
   ** Should be overridden by subclasses to create an EQUALS expression if the
   ** native resource supports EQUALS.
   **
   **
   ** @param  filter             the ICF <code>equal</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EqualsFilter}.
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
  protected abstract T createEQ(final EqualsFilter filter, final boolean not);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGT
  /**
   ** Should be overridden by subclasses to create a GREATER-THAN expression if
   ** the native resource supports GREATER-THAN.
   **
   ** @param  filter             the ICF <code>greater than</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link GreaterThanFilter}.
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
  protected abstract T createGT(final GreaterThanFilter filter, final boolean not);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGE
  /**
   ** Should be overridden by subclasses to create a GREATER-THAN-EQUAL
   ** expression if the native resource supports GREATER-THAN-EQUAL.
   **
   ** @param  filter             the ICF <code>greater than or equal</code>
   **                            filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link GreaterThanOrEqualFilter}.
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
  protected abstract T createGE(final GreaterThanOrEqualFilter filter, final boolean not);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLT
  /**
   ** Should be overridden by subclasses to create a LESS-THAN expression if
   ** the native resource supports LESS-THAN.
   **
   ** @param  filter             the ICF <code>less than</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link LessThanFilter}.
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
  protected abstract T createLT(final LessThanFilter filter, final boolean not);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLE
  /**
   ** Should be overridden by subclasses to create a LESS-THAN-EQUAL expression
   ** if the native resource supports LESS-THAN-EQUAL.
   **
   ** @param  filter             the ICF <code>less than or equal</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link LessThanOrEqualFilter}.
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
  protected abstract T createLE(final LessThanOrEqualFilter filter, final boolean not);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSW
  /**
   ** Should be overridden by subclasses to create an STARTS-WITH expression if
   ** the native resource supports STARTS-WITH.
   **
   ** @param  filter             the ICF <code>starts with</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link StartsWithFilter}.
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
  protected abstract T createSW(final StartsWithFilter filter, final boolean not);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEW
  /**
   ** Should be overridden by subclasses to create an ENDS-WITH expression if
   ** the native resource supports ENDS-WITH.
   **
   ** @param  filter             the ICF <code>ends with</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EndsWithFilter}.
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
  protected abstract T createEW(final EndsWithFilter filter, final boolean not);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCO
  /**
   ** Should be overridden by subclasses to create an CONTAINS expression if the
   ** native resource supports CONTAINS.
   **
   ** @param  filter             the ICF <code>contains</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ContainsFilter}.
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
  protected abstract T createCO(final ContainsFilter filter, final boolean not);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Pushes Not's so that they are just before the leaves of the tree
   */
  private Filter normalize(final Filter filter) {
    if (filter instanceof AndFilter) {
      AndFilter af = (AndFilter)filter;
      return new AndFilter(normalize(af.getLeft()), normalize(af.getRight()));
    }
    else if (filter instanceof OrFilter) {
      OrFilter of = (OrFilter)filter;
      return new OrFilter(normalize(of.getLeft()), normalize(of.getRight()));
    }
    else if (filter instanceof NotFilter) {
      NotFilter nf = (NotFilter)filter;
      return negate(normalize(nf.getFilter()));
    }
    else {
      return filter;
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
   ** @throws ServiceException    if filter syntax becomes invalid.
   */
  private Filter simplify(final Filter filter)
    throws ServiceException {

    if (filter instanceof AndFilter) {
      final AndFilter af  = (AndFilter)filter;
      final Filter    lhs = simplify(af.getLeft());
      final Filter    rhs = simplify(af.getRight());
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
          throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, lex));
        }
        if (rex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedRight
          // should have been null in the previous 'if' above).
          throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, rex));
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
          final OrFilter left      = (OrFilter)lhs;
          final OrFilter newFilter = new OrFilter(new AndFilter(left.getLeft(), rhs), new AndFilter(left.getRight(), rhs));
          return simplify(newFilter);
        }
        else if (rex.size() > 1) {
          // the right can contain more than one expression only if the
          // right-hand side is an unimplemented OR.
          // distribute our AND to the right.
          final OrFilter right     = (OrFilter)rhs;
          final OrFilter newFilter = new OrFilter(new AndFilter(lhs, right.getLeft()), new AndFilter(lhs, right.getRight()));
          return simplify(newFilter);
        }
        else {
          // each side contains exactly one expression and the translator does
          // implement AND (anyAndsPossible must be true for them to have hit
          // this branch).
          assert possibleAnd;
          return new AndFilter(lhs, rhs);
        }
      }
    }
    else if (filter instanceof OrFilter) {
      final OrFilter of = (OrFilter)filter;
      final Filter   lhs = simplify(of.getLeft());
      final Filter   rhs = simplify(of.getRight());
      // if either left or right reduces to "everything", then simplify the OR
      // to "everything".
      if (lhs == null || rhs == null) {
        return null;
      }
      // otherwise
      return new OrFilter(lhs, rhs);
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
  // Method:   negate
  /**
   ** Given a filter, create a filter representing its negative. This is used
   ** by normalizeNot.
   */
  private Filter negate(final Filter filter) {
    if (filter instanceof AndFilter) {
      AndFilter af = (AndFilter)filter;
      return new OrFilter(negate(af.getLeft()), negate(af.getRight()));
    }
    else if (filter instanceof OrFilter) {
      OrFilter of = (OrFilter)filter;
      return new AndFilter(negate(of.getLeft()), negate(of.getRight()));
    }
    else if (filter instanceof NotFilter) {
      NotFilter nf = (NotFilter)filter;
      return nf.getFilter();
    }
    else {
      return new NotFilter(filter);
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
   ** @throws ServiceException    if filter syntax becomes invalid.
   */
  private List<T> translateInternal(final Filter filter)
    throws ServiceException {

    if (filter instanceof AndFilter) {
      final T       result = translateAnd((AndFilter)filter);
      final List<T> rv     = new ArrayList<T>();
      if (result != null) {
        rv.add(result);
      }
      return rv;
    }
    else if (filter instanceof OrFilter) {
      return translateOr((OrFilter)filter);
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
   ** @throws ServiceException    if filter syntax becomes invalid.
   */
  private T translateAnd(final AndFilter filter)
    throws ServiceException {

    final List<T> lhs = translateInternal(filter.getLeft());
    final List<T> rhs = translateInternal(filter.getRight());
    if (lhs.size() != 1)
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, lhs));

    if (rhs.size() != 1) {
      // this can happen only if one of the create* methods
      // is inconsistent from one invocation to the next
      // (at this point we've already been simplified and
      // distributed).
      throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, rhs));
    }
    T rv = createAND(lhs.get(0), rhs.get(0));
    if (rv == null) {
      // this could happen only if we're inconsistent since the simplify logic
      // already should have removed any expression that cannot be filtered
      throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_EXPRESSION_INCONSISTENT, "createAND"));
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
   ** @throws ServiceException   if filter syntax becomes invalid.
   */
  private List<T> translateOr(final OrFilter filter)
    throws ServiceException {

    final List<T> lhs = translateInternal(filter.getLeft());
    final List<T> rhs = translateInternal(filter.getRight());
    if (lhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, lhs));

    if (rhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, rhs));

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
    if (filter instanceof NotFilter) {
      leaf = ((NotFilter)filter).getFilter();
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
    switch (Switcher.from(filter.getClass().getName())) {
      case EQ : return createEQ((EqualsFilter)filter, not);
      case GT : return createGT((GreaterThanFilter)filter, not);
      case GE : return createGE((GreaterThanOrEqualFilter)filter, not);
      case LT : return createLT((LessThanFilter)filter, not);
      case LE : return createLE((LessThanOrEqualFilter)filter, not);
      case SW : return createSW((StartsWithFilter)filter, not);
      case EW : return createEW((EndsWithFilter)filter, not);
      case CO : return createCO((ContainsFilter)filter, not);
      // unrecognized expression - nothing we can do
      default : return null;
    }
  }
}