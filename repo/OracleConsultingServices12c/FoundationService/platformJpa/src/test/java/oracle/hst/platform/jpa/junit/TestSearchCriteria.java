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

    System      :   Presistence Foundation Shared Library
    Subsystem   :   JPA Unit Testing

    File        :   TestSearchCriteria.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestSearchCriteria.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.junit;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import javax.persistence.criteria.Predicate;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.hst.platform.core.entity.Or;
import oracle.hst.platform.core.entity.And;
import oracle.hst.platform.core.entity.Not;
import oracle.hst.platform.core.entity.Filter;

import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.jpa.PersistenceFunction;

import oracle.hst.platform.jpa.junit.model.User;

////////////////////////////////////////////////////////////////////////////////
// class TestSearchCriteria
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Test Cases to validate and enforce the filter syntax on JPA objects
 ** representing REST resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestSearchCriteria extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestSearchCriteria</code> instance that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestSearchCriteria() {
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
    final String[] parameter = {TestSearchCriteria.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testValidExpression
  /**
   ** Tests the parsing method with a valid filter string.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testValidExpression()
    throws Exception {

    valid("(userName sw \"bk\" or id eq -1)", Filter.or(Filter.sw("userName", "bk"), Filter.eq("id", "-1")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testFilter2SearchFilter
  /**
   ** Tests the translation method with a valid filter string to a
   ** {@link SearchFilter}.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testFilter2SearchFilter()
    throws Exception {

    final String expression = "(userName sw \"bk\" or id eq -1) and firstName ne \"XXXXX\"";
    final Filter filter     = Filter.from(expression);
    valid(expression, filter);
    
    final List<User> result = this.provider.readonly(
      new PersistenceFunction<List<User>>() {
        public List<User> apply(final EntityManager em) {
          final CriteriaBuilder     builder  = em.getCriteriaBuilder();
          final CriteriaQuery<User> query    = builder.createQuery(User.class);
          final Root<User>          entity   = query.from(query.getResultType());
          final CriteriaQuery<User> select   = query.select(entity);
          select.where(
            translate(builder, entity, translate(filter))
          );
          final TypedQuery<User> typed  = em.createQuery(select);
          return typed.getResultList();
        }
      }
    );
    equals(2, result.size());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testApplyFormalSearchFilter
  /**
   ** Tests the translation method with a valid filter string.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testApplyFormalSearchFilter()
    throws Exception {

    final SearchFilter filter = SearchFilter.and(
      SearchFilter.not(SearchFilter.eq("firstName", "XXXXX"))                      
    , SearchFilter.or(
        SearchFilter.sw("userName", "bk")
      , SearchFilter.eq("id", -1)
      )
    );

    final List<User> result = this.provider.readonly(
      new PersistenceFunction<List<User>>() {
        public List<User> apply(final EntityManager em) {
          final CriteriaBuilder     builder  = em.getCriteriaBuilder();
          final CriteriaQuery<User> query    = builder.createQuery(User.class);
          final Root<User>          entity   = query.from(query.getResultType());
          final CriteriaQuery<User> select   = query.select(entity);
          select.where(
            translate(builder, entity, filter)
          );
          final TypedQuery<User> typed  = em.createQuery(select);
          return typed.getResultList();
        }
      }
    );
    equals(2, result.size());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valid
  /**
   ** Tests the parsing method with a valid filter string.
   **
   ** @param  expression         the string representation of the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  expected           the expected parsed filter instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws Exception          if an error occurs.
   */
  private void valid(final String expression, final Filter expected)
    throws Exception {

    final Filter parsed = Filter.from(expression);
    equals(parsed, expected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translate
  private static SearchFilter translate(final Filter filter) {
    final Object value = filter.value();
    switch (filter.type()) {
      case OR  : return disjunction(((Or)filter).filter());
      case AND : return conjunction(((And)filter).filter());
      case NOT : return SearchFilter.not(translate(((Not)filter).filter()));

      case SW  : return SearchFilter.sw(filter.path().segment(0).attribute(), (String)value);
      case EW  : return SearchFilter.ew(filter.path().segment(0).attribute(), (String)value);
      case CO  : return SearchFilter.co(filter.path().segment(0).attribute(), (String)value);
      case LE  : if (value instanceof Number) {
                  return SearchFilter.le(filter.path().segment(0).attribute(), (Number)value);
                 }
                 throw new IllegalArgumentException();
      case LT  : if (value instanceof Number) {
                   return SearchFilter.lt(filter.path().segment(0).attribute(), (Number)value);
                 }
                 throw new IllegalArgumentException();
      case GE  : if (value instanceof Number) {
                   return SearchFilter.ge(filter.path().segment(0).attribute(), (Number)value);
                 }
                 throw new IllegalArgumentException();
      case GT  : if (value instanceof Number) {
                   return SearchFilter.gt(filter.path().segment(0).attribute(), (Number)value);
                 }
                 throw new IllegalArgumentException();
      default  : if (value instanceof Boolean) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (Boolean)value);
                 }
                 else if (value instanceof Number) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (Number)value);
                 }
                 else if (value instanceof String) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (String)value);
                 }
      throw new IllegalArgumentException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translate
  private final Predicate translate(final CriteriaBuilder builder, final Root<User> entity, final SearchFilter filter) {
    final SearchFilter.Type type = filter.type();
    switch (type) {
      case OR  : return builder.or (junction (builder, entity, ((SearchFilter.Or)filter).filter()));
      case AND : return builder.and(junction (builder, entity, ((SearchFilter.And)filter).filter()));
      case NOT : return builder.not(translate(builder, entity, ((SearchFilter.Not)filter).filter()));

      case EQ  : return builder.equal(entity.get(filter.path()), filter.value());
      case SW  :
      case EW  :
      case CO  : final String pattern = (type == SearchFilter.Type.SW ? "" : "%") + filter.value() + (type == SearchFilter.Type.EW ? "" : "%");
                 return builder.like(entity.get(filter.path()), pattern);
      case LT  : if (filter instanceof SearchFilter.Numeric) {
                   return builder.lt(entity.get(filter.path()), (Number)filter.value());
                 }
                 throw new IllegalArgumentException();
      case LE  : if (filter instanceof SearchFilter.Numeric) {
                   return builder.le(entity.get(filter.path()), (Number)filter.value());
                 }
                 throw new IllegalArgumentException();
      case GT  : if (filter instanceof SearchFilter.Numeric) {
                   return builder.gt(entity.get(filter.path()), (Number)filter.value());
                 }
                 throw new IllegalArgumentException();
      case GE  : if (filter instanceof SearchFilter.Numeric) {
                   return builder.ge(entity.get(filter.path()), (Number)filter.value());
                 }
                 throw new IllegalArgumentException();
    }
    throw new IllegalArgumentException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   conjunction
  /**
   ** Logically "ANDs" together the instances of an {@link And} {@link Filter}.
   ** <br>
   ** The resulting <i>conjunct</i> {@link Predicate} is <code>true</code> if
   ** and only if all of the specified filters are <code>true</code>.
   **
   ** @param  filter             the {@link And} {@link Filter} to transform to
   **                            a {@link Predicate}.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the resulting collection of <i>conjunct</i>
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   */
  private static SearchFilter<?> conjunction(final List<Filter> filter) {
    // FIXME: not compilable ???
    // return SearchFilter.and(Streams.stream(filter).filter(f -> f != null).map(f -> translate(f)).collect(Collectors.toList()));
    final List<SearchFilter<?>> collector = new ArrayList<SearchFilter<?>>(filter.size());
    for (Filter cursor : filter) {
      collector.add(translate(cursor));
    }
    return SearchFilter.and(collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disjunction
  /**
   ** Logically "ORs" together the instances of an {@link Or} {@link Filter}.
   ** <br>
   ** The resulting <i>disjunct</i> {@link Predicate} is <code>true</code> if
   ** and only if at least one of the specified filters is <code>true</code>.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link Predicate}.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the resulting collection of <i>disjunct</i>
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   */
  private static SearchFilter disjunction(final List<Filter> filter) {
    // FIXME: not compilable ???
    // return SearchFilter.or(Streams.stream(filter).filter(f -> f != null).map(f -> translate(f)).collect(Collectors.toList()));
    final List<SearchFilter<?>> collector = new ArrayList<SearchFilter<?>>(filter.size());
    for (Filter cursor : filter) {
      collector.add(translate(cursor));
    }
    return SearchFilter.or(collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   junction
  /**
   ** Logically combines together the instances of a collection of
   ** {@link SearchFilter}.
   **
   ** @param  builder            the {@link CriteriaQuery} used to build the
   **                            resulting {@link Predicate}.
   **                            <br>
   **                            Allowed object is {@link CriteriaQuery}.
   ** @param  query
   ** @param  entity
   ** @param  filter             the {@link SearchFilter} collection to
   **                            transform to a collection of {@link Predicate}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link SearchFilter}.
   **
   ** @return                    the resulting collection of {@link Predicate}s.
   **                            <br>
   **                            Possible object is array of {@link Predicate}.
   */
  private Predicate[] junction(final CriteriaBuilder builder, final Root<User> entity, final List<SearchFilter<?>> filter) {
    final List<Predicate> collector   = new ArrayList<Predicate>();
    for (SearchFilter<?> cursor : filter) {
      collector.add(translate(builder, entity, cursor));
    }
    return collector.toArray(new Predicate[0]);              
  }
}