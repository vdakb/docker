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
    Subsystem   :   Foundation Shared Library

    File        :   Translator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Translator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.object.filter;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.Set;

import oracle.iam.identity.icf.foundation.SystemError;

import oracle.iam.identity.icf.foundation.resource.SystemBundle;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.OrFilter;
import org.identityconnectors.framework.common.objects.filter.AndFilter;
import org.identityconnectors.framework.common.objects.filter.NotFilter;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.AttributeFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsAllValuesFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanOrEqualFilter;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

////////////////////////////////////////////////////////////////////////////////
// class Translator
// ~~~~~ ~~~~~~~~~~
/**
 ** Base class to make it easier to implement Search.
 ** <br>
 ** A search filter may contain operators (such as 'contains' or 'in') or may
 ** contain logical operators (such as 'AND', 'OR' or 'NOT') that a connector
 ** cannot implement using the native API of the target system or application.
 ** <br>
 ** A connector developer should subclass <code>Translator</code> in order to
 ** declare which filter operations the connector does support.
 ** <br>
 ** This allows the <code>Translator</code> instance to analyze a specified
 ** search filter and reduce the filter to its most efficient form. The default
 ** (and worst-case) behavior is to return a <code>null</code> expression, which
 ** means that the connector should return "everything" (that is, should return
 ** all values for every requested attribute) and rely on the common code in the
 ** framework to perform filtering.
 ** <br>
 ** This "fallback" behavior is good (in that it ensures consistency of search
 ** behavior across connector implementations) but it is obviously better for
 ** performance and scalability if each connector performs as much filtering as
 ** the native API of the target can support.
 ** <p>
 ** A subclass should override each of the following methods where possible:
 ** <ol>
 **   <li>{@link #and}
 **   <li>{@link #or}
 **   <li>{@link #co(Attribute, boolean)}
 **   <li>{@link #ew(Attribute, boolean)}
 **   <li>{@link #eq(Attribute, boolean)}
 **   <li>{@link #lt(Attribute, boolean)}
 **   <li>{@link #le(Attribute, boolean)}
 **   <li>{@link #gt(Attribute, boolean)}
 **   <li>{@link #ge(Attribute, boolean)}
 **   <li>{@link #sw(Attribute, boolean)}
 **   <li>{@link #ca(Attribute, boolean)}
 ** </ol>
 ** Translation can then be performed using {@link #translate(Filter)}.
 **
 ** @param <T>                   The result type of the translator.
 **                              <br>
 **                              Commonly this will be a string, but there are
 **                              cases where you might need to return a more
 **                              complex data structure.
 **                              <br>
 **                              For example if you are building a SQL query,
 **                              you will need not *just* the base WHERE clause
 **                              but a list of tables that need to be joined
 **                              together.
 */
public abstract class Translator<T> implements FilterTranslator<T> {

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
    , CA(ContainsAllValuesFilter.class)
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
   ** Constructs a default directory filter <code>Translator</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Translator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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
   **                                   {@link #or} method can return
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
   **                                   possible you implement {@link #or}.
   **                             </ol>
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws ConnectorException if filter syntax becomes invalid.
   */
  public final List<T> translate(Filter filter)
    throws ConnectorException {

    if (filter == null)
      return new ArrayList<T>();

    // this must come first
    filter = simplify(normalize(filter));
    // might have simplified it to the everything filter
    if (filter == null)
      return new ArrayList<T>();

    List<T> result    = translateInternal(filter);
    // now "optimize" - we can eliminate exact matches at least
    Set<T>  set       = new HashSet<T>();
    List<T> optimized = new ArrayList<T>(result.size());
    for (T exp : result) {
      if (set.add(exp)) {
        optimized.add(exp);
      }
    }
    return optimized;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to creates an <code>AND</code> filter expression composed
   ** by both provided filter expressions.
   ** <br>
   ** Should be overridden by subclasses to create an AND expression if the
   ** native resource supports <code>AND</code>.
   **
   ** @param  lhs                the left hand side sytanx of an attribute
   **                            filter expression.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the right hand side sytanx of an attribute
   **                            filter expression.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the sytanx of a <code>AND</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>AND</code> query cannot be created
   **                            for the given expressions.
   **                            <br>
   **                            In this case, the resulting query will consist
   **                            of the left hand side expression only.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T and(final T lhs, final T rhs) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to creates an <code>OR</code> filter expression composed by
   ** both provided filter expressions.
   ** <br>
   ** Should be overridden by subclasses to create an <code>OR</code> expression
   ** if the native resource supports <code>OR</code>.
   **
   ** @param  lhs                the left hand side sytanx of an attribute
   **                            filter expression.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the right hand side sytanx of an attribute
   **                            filter expression.
   **                            <br>
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the sytanx of a <code>OR</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>OR</code> query cannot be created
   **                            for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return
   **                            multiple queries, each of which must be run and
   **                            results combined.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T or(final T lhs, final T rhs) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates a <code>EQUALS</code> filter expression that
   ** might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create an <code>EQUALS</code>
   ** expression if the native resource supports <code>EQUALS</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>NOT-EQUALS</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>EQUALS</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T eq(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to creates a <code>LESS-THAN</code> filter expression that
   ** might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a <code>LESS-THAN</code>
   ** expression if the native resource supports <code>LESS-THAN</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>NOT-LESS-THAN</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>LESS-THAN</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>LESS-THAN</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T lt(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to creates a <code>LESS-THAN-OR-EQUALS</code> filter
   ** expression the might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a
   ** <code>LESS-THAN-OR-EQUALS</code> expression if the native resource
   ** supports <code>LESS-THAN-OR-EQUALS</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>NOT-LESS-THAN-OR-EQUALS</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   **
   ** @return                    the sytanx of a <code>LESS-THAN</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>LESS-THAN</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T le(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to creates a <code>GREATER-THAN</code> filter expression
   ** that might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a <code>GREATER-THAN</code>
   ** expression if the native resource supports <code>GREATER-THAN</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>NOT-GREATER-THAN</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>GREATER-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>GREATER-THAN</code> query cannot
   **                            be created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T gt(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to creates a <code>GREATER-THAN-OR-EQUALS</code> filter
   ** expression that might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a
   ** <code>GREATER-THAN-OR-EQUALS</code> expression if the native resource
   ** supports <code>GREATER-THAN-OR-EQUALS</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be
   **                            <code>NOT-GREATER-THAN-OR-EQUALS</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a
   **                            <code>GREATER-THAN-OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>GREATER-THAN-OR-EQUALS</code>
   **                            query cannot be created for the given
   **                            expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T ge(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to creates a <code>STARTS-WITH</code> filter expression
   ** that might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a <code>STARTS-WITH</code>
   ** expression if the native resource supports <code>STARTS-WITH</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be
   **                            <code>NOT-STARTS-WITH</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>STARTS-WITH</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>STARTS-WITH</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T sw(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to creates a <code>ENDS-WITH</code> filter expression that
   ** might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a <code>ENDS-WITH</code>
   ** expression if the native resource supports <code>ENDS-WITH</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be
   **                            <code>NOT-ENDS-WITH</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>ENDS-WITH</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>ENDS-WITH</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T ew(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to creates a <code>CONTAINS</code> filter expression that
   ** might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a <code>CONTAINS</code>
   ** expression if the native resource supports <code>CONTAINS</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be
   **                            <code>NOT-CONTAINS</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>CONTAINS</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>CONTAINS</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T co(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ca
  /**
   ** Factory method to creates a <code>CONTAINS-ALL</code> filter expression
   ** that might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a <code>CONTAINS-ALL</code>
   ** expression if the native resource supports <code>CONTAINS-ALL</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be
   **                            <code>NOT-CONTAINS-ALL</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>CONTAINS-ALL</code>
   **                            filter expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>CONTAINS-ALL</code> query cannot
   **                            be created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unused")
  protected T ca(final Attribute filter, final boolean not) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Pushes Not's so that they are just before the leaves of the tree
   **
   ** @param  filter             a {@link Filter}.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the normalized filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  private Filter normalize(final Filter filter) {
    if (filter instanceof AndFilter) {
      final AndFilter af = (AndFilter)filter;
      return new AndFilter(normalize(af.getLeft()), normalize(af.getRight()));
    }
    else if (filter instanceof OrFilter) {
      final OrFilter of = (OrFilter)filter;
      return new OrFilter(normalize(of.getLeft()), normalize(of.getRight()));
    }
    else if (filter instanceof NotFilter) {
      final NotFilter nf = (NotFilter)filter;
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
   ** @param  filter             {@link Not} filters must already be normalized.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    a simplified filter or <code>null</code> to
   **                            represent the "everything" filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ConnectorException if filter syntax becomes invalid.
   */
  private Filter simplify(final Filter filter)
    throws ConnectorException {

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
        if (lex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedLeft should
          // have been null in the previous 'if' above).
          throw invalidFilter(SystemBundle.string(SystemError.FILTER_INCONSISTENT_METHOD, lex));
        }
        final List<T> rex = translateInternal(rhs);
        if (rex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedRight
          // should have been null in the previous 'if' above).
          throw invalidFilter(SystemBundle.string(SystemError.FILTER_INCONSISTENT_METHOD, rex));
        }

        // simulate ANDing each pair(left,right).
        // if all of them return null (i.e., "everything"), then the request
        // cannot be filtered.
        boolean possibleAnd = false;
        for (T l : lex) {
          for (T r : rex) {
            T test = and(l, r);
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
          // implement AND (possibleAnd must be true for them to have hit
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
      final T leaf = createLeaf(filter);
      if (leaf == null) {
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
   **
   ** @param  filter             a filter to reverse.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    a reversed filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
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
   ** {@link #normalize(Filter)} followed by a {@link #simplify(Filter)}.
   **
   ** @param  filter             a {@link Filter} (normalized, simplified, and
   **                            distibuted).
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    a {@link List} of expressions or an empty
   **                            {@link List} for everything.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @throws ConnectorException if filter syntax becomes invalid.
   */
  private List<T> translateInternal(final Filter filter)
    throws ConnectorException {

    if (filter instanceof AndFilter) {
      final T       leaf = translateAnd((AndFilter)filter);
      final List<T> expr = new ArrayList<T>();
      if (leaf != null) {
        expr.add(leaf);
      }
      return expr;
    }
    else if (filter instanceof OrFilter) {
      return translateOr((OrFilter)filter);
    }
    else {
      // otherwise it's either a leaf or a NOT (leaf)
      final T       leaf = createLeaf(filter);
      final List<T> expr = new ArrayList<T>();
      if (leaf != null) {
        expr.add(leaf);
      }
      return expr;
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
   ** @param  filter             an {@link AndFilter} (normalized, simplified,
   **                            and distibuted).
   **                            <br>
   **                            Allowed object is {@link AndFilter}.
   **
   ** @return                    a {@link List} of expressions or empty
   **                            {@link List} for everything.
   **
   ** @throws ConnectorException if filter syntax becomes invalid.
   */
  private T translateAnd(final AndFilter filter)
    throws ConnectorException {

    final List<T> lhs = translateInternal(filter.getLeft());
    final List<T> rhs = translateInternal(filter.getRight());
    if (lhs.size() != 1)
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw invalidFilter(SystemBundle.string(SystemError.FILTER_INCONSISTENT_METHOD, lhs));

    if (rhs.size() != 1) {
      // this can happen only if one of the create* methods
      // is inconsistent from one invocation to the next
      // (at this point we've already been simplified and
      // distributed).
      throw invalidFilter(SystemBundle.string(SystemError.FILTER_INCONSISTENT_METHOD, rhs));
    }
    T rv = and(lhs.get(0), rhs.get(0));
    if (rv == null) {
      // this could happen only if we're inconsistent since the simplify logic
      // already should have removed any expression that cannot be filtered
      throw invalidFilter(SystemBundle.string(SystemError.FILTER_INCONSISTENT_EXPRESSION, "translateAnd"));
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
   ** @param  filter             an {@link OrFilter} (normalized, simplified,
   **                            and distibuted).
   **                            <br>
   **                            Allowed object is {@link OrFilter}.
   **
   ** @return                    a {@link List} of expressions or an empty
   **                            {@link List} for everything.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @throws ConnectorException if filter syntax becomes invalid.
   */
  private List<T> translateOr(final OrFilter filter)
    throws ConnectorException {

    final List<T> lhs = translateInternal(filter.getLeft());
    final List<T> rhs = translateInternal(filter.getRight());
    if (lhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw invalidFilter(SystemBundle.string(SystemError.FILTER_INCONSISTENT_METHOD, lhs));

    if (rhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw invalidFilter(SystemBundle.string(SystemError.FILTER_INCONSISTENT_METHOD, rhs));

    if (lhs.size() == 1 && rhs.size() == 1) {
      // If each side contains exactly one expression,
      // try to create a combined expression.
      T val = or(lhs.get(0), rhs.get(0));
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
   **                            expression.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the expression or <code>null</code> (for
   **                            everything)
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  private T createLeaf(final Filter filter) {
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
    return createLeaf(leaf, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLeaf
  /**
   ** Creates a Leaf expression.
   **
   ** @param  filter             must be either a leaf expression.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  not                Is ! to be applied to the leaf expression.
   **
   ** @return                    the expression or <code>null</code> (for
   **                            everything)
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  private T createLeaf(final Filter filter, final boolean not) {
    switch (Switcher.from(filter.getClass().getName())) {
      case EQ : return eq(((AttributeFilter)filter).getAttribute(), not);
      case GT : return gt(((AttributeFilter)filter).getAttribute(), not);
      case GE : return ge(((AttributeFilter)filter).getAttribute(), not);
      case LT : return lt(((AttributeFilter)filter).getAttribute(), not);
      case LE : return le(((AttributeFilter)filter).getAttribute(), not);
      case SW : return sw(((AttributeFilter)filter).getAttribute(), not);
      case EW : return ew(((AttributeFilter)filter).getAttribute(), not);
      case CO : return co(((AttributeFilter)filter).getAttribute(), not);
      // unrecognized expression - nothing we can do
      default : return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidFilter
  /**
   ** Factory method to create a new {@link ConnectorException} with the
   ** {@link SystemError#INVALID_FILTER} error keyword.
   ** <p>
   ** The specified filter syntax was invalid or the specified attribute and
   ** filter comparison combination is not supported.
   **
   ** @param  message            the error message for exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ConnectorException} wrapping the
   **                            details.
   **                            <br>
   **                            Possible object is {@link ConnectorException}.
   */
  private static ConnectorException invalidFilter(final String message) {
    return new ConnectorException(String.format("%1$s::%2$s", SystemError.FILTER_INVALID, message));
  }
}