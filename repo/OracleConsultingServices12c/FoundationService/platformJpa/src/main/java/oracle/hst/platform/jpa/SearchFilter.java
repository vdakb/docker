/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   SearchFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import java.util.List;
import java.util.Calendar;
import java.util.LinkedList;

import oracle.hst.platform.core.entity.Range;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// interface SearchFilter
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The primary intention of this interface is to provide a general model to
 ** transfer search filter metadata to a JPA implementation.
 ** <br>
 ** Upstream services based on JSF or JAX-RS have their own way of applying
 ** filter. This includes parsing of expressions or interpreting criteria for a
 ** search. Therfore such technologies have their own syntax and semantics what
 ** a search filter is.
 ** <br>
 ** On the other hand, such technologies should not (cannot) operate directly on
 ** the persistence layer (JPA) to apply search algorithms, since a
 ** <code>CriteriaBuilder</code> is required for such purpose. A
 ** <code>CriteriaBuilder</code> itself needs access to the implementing entity
 ** the search criteria are applied on.
 ** <br>
 ** An upstream service is not aware of the entity datamodel due to its behind
 ** of an appropriate session facade.
 **
 ** @param  <T>                  the type of the contained filter value.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the filters
 **                              implementing this interface (compare operations
 **                              can use their own specific types instead of
 **                              types defined by this interface only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SearchFilter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static SearchFilter<?> NOP = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** Defines the set of possible filter types that may be used for query
   ** filters.
   */
  public enum Type {
      /** The type for <code>and</code> filters */
      AND
      /** The type for <code>or</code> filters */
    , OR
      /** The type for <code>not</code> filters */
    , NOT
      /** The type for <code>equal</code> filters */
    , EQ
      /** The type for <code>not equal</code> filters */
    , NE
      /** The type for <code>contains</code> filters */
    , CO
      /** The type for <code>starts with</code> filters */
    , SW
      /** The type for <code>starts ends</code> filters */
    , EW
      /** The type for <code>present</code> filters */
    , PR
      /** The type for <code>greater than</code> filters */
    , GT
      /** The type for <code>greater or equal</code> filters */
    , GE
      /** The type for <code>less than</code> filters */
    , LT
      /** The type for <code>less or equal</code> filters */
    , LE
      /** The type for <code>between</code> filters */
    , BW
      /** The type for <code>enumerated</code> (in) filters */
    , IN
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Comparison
  // ~~~~~~~~ ~~~~~ ~~~~~~~~~~
  /**
   ** Useful for the AND, OR, XOR, etc..
   **
   ** @param  <T>                the type of the contained filter value.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the filters
   **                            implementing this interface (compare operations
   **                            can use their own specific types instead of
   **                            types defined by this interface only).
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   */
  static abstract class Comparison<T> implements SearchFilter<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The {@link Type} of this {@link Comparison}. */
    private final Type   type;
    /** The entity property path this {@link Comparison} will applied. */
    private final String path;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
 
    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Comparison</code>  which belongs to the specified
     ** <code>property</code>.
     **
     ** @param  type             the {@link Type} this {@link SearchFilter}
     **                          belongs to.
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  path             the entity property path to apply the
     **                          comparison on.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    protected Comparison(final Type type, final String path) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.type  = type;
      this.path  = path;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type (SearchFilter)
    /**
     ** Returns the type of this filter.
     **
     ** @return                    the type of this filter.
     **                            <br>
     **                            Possible object is {@link Type}.
     */
    @Override
    public final Type type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: path (SearchFilter)
    /**
     ** Returns the path to the attribute to filter by, or <code>null</code> if
     ** this filter is not a comparison filter.
     **
     ** @return                  the path to the attribute to filter by, or
     **                          <code>null</code> if this filter is not a
     **                          comparison filter.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String path() {
      return this.path;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isComplex (SearchFilter)
    /**
     ** Whether this filter is a complex multi-valued attribute value filter.
     **
     ** @return                  <code>true</code> if this filter is a complex
     **                          multi-valued attribute value filter or
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean isComplex() {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Junction
  // ~~~~~~~~ ~~~~~ ~~~~~~~~
  /**
   ** Useful for the AND, OR, XOR, etc.
   */
  static abstract class Junction implements SearchFilter<SearchFilter> {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    /** the {@link Type} of this {@link SearchFilter}. */
    private final Type                  type;

    /** the left side of a composite based filter. */
    private LinkedList<SearchFilter<?>> filter;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
 
    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Junction</code> w/ the left and right
     ** {@link SearchFilter}s provided.
     **
     ** @param  type             the {@link Type} this {@link SearchFilter}
     **                          belongs to.
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  lhs              the left side of the <code>Junction</code>.
     **                          <br>
     **                          Allowed object is {@link SearchFilter} of type
     **                          <code>T</code>.
     ** @param  rhs              the right side of the <code>Junction</code>.
     **                          <br>
     **                          Allowed object is {@link SearchFilter} of type
     **                          <code>T</code>.
     */
    @SuppressWarnings("unchecked")
    protected Junction(final Type type, final SearchFilter<?> lhs, final SearchFilter<?> rhs) {
      // ensure inheritance
      this(type, CollectionUtility.list(lhs, rhs));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Junction</code> w/ the left and right
     ** {@link SearchFilter}s provided.
     **
     ** @param  type               the {@link Type} this {@link SearchFilter}
     **                            belongs to.
     **                            <br>
     **                            Allowed object is {@link Type}.
     ** @param  filter             the collection of a composite based filter.
     **                            <br>
     **                            Allowed object is {@link List} where each
     **                            element is of type {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    public Junction(final Type type, final List<SearchFilter<?>> filter) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.filter = new LinkedList<SearchFilter<?>>(filter);
      this.type   = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: lhs
    /**
     ** Returns the left side of a composite based filter.
     **
     ** @return                    the left side of a composite based filter.
     **                            <br>
     **                            Possible object is {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    public final SearchFilter<?> lhs() {
      return this.filter.getFirst();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: rhs
    /**
     ** Returns the right side of a composite based filter.
     **
     ** @return                    the right side of a composite based filter.
     **                            <br>
     **                            Possible object is {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    @SuppressWarnings("unchecked")
    public final SearchFilter rhs() {
      if (this.filter.size() > 2) {
        final LinkedList<SearchFilter<?>> right = new LinkedList<SearchFilter<?>>(this.filter);
        right.removeFirst();
        return new And(right);
      }
      else if (this.filter.size() == 2) {
        return this.filter.getLast();
      }
      else {
        return null;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter
    /**
     ** Returns the internal filter that is being merged.
     **
     ** @return                    the internal filter that is being merged.
     **                            <br>
     **                            Possible object is {@link List} where
     **                            each element is of type
     **                            {@link SearchFilter} for type <code>T</code>.
     */
    public final List<SearchFilter<?>> filter() {
      return CollectionUtility.unmodifiable(this.filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type (SearchFilter)
    /**
     ** Returns the type of this filter.
     **
     ** @return                    the type of this filter.
     **                            <br>
     **                            Possible object is {@link Type}.
     */
    @Override
    public final Type type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: path (SearchFilter)
    /**
     ** Returns the path to the attribute to filter by, or <code>null</code> if
     ** this filter is not a comparison filter.
     **
     ** @return                  the path to the attribute to filter by, or
     **                          <code>null</code> if this filter is not a
     **                          comparison filter.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String path() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (SearchFilter)
    /**
     ** Returns the comparison value, or <code>null</code> if this filter is not
     ** a comparison filter.
     **
     ** @return                  the comparison value, or <code>null</code> if
     **                          this filter is not a comparison filter.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    @Override
    public SearchFilter value() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isComplex (SearchFilter)
    /**
     ** Whether this filter is a complex multi-valued attribute value filter.
     **
     ** @return                  <code>true</code> if this filter is a complex
     **                          multi-valued attribute value filter or
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean isComplex() {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Bool
  // ~~~~~ ~~~~
  /**
   ** Creates <code>path = true|false</code> search criteria.
   ** <p>
   ** The operational type at this filter type are:
   ** <ul>
   **   <li>{@link Type#EQ}
   ** </ul>
   */
  static class Bool extends Comparison<Boolean> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Boolean value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new <code>Bool</code> criteria based on a given value.
     **
     ** @param  type             the search type operator to apply.
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  path             the entity property path to apply the
     **                          criteria on.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the criteria value.
     **                          <br>
     **                          Allowed object is {@link Boolean}.
     */
    private Bool(final Type type, final String path, final Boolean value) {
      // ensure inheritance
      super(type, path);

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (SearchFilter)
    /**
     ** Returns the comparison value, or <code>null</code> if this filter is not
     ** a comparison filter.
     **
     ** @return                  the comparison value, or <code>null</code> if
     **                          this filter is not a comparison filter.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     */
    @Override
    public Boolean value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Numeric
  // ~~~~~ ~~~~~~~
  /**
   ** Creates <code>path = number</code> search criteria.
   ** <p>
   ** The operational type at this filter type are:
   ** <ul>
   **   <li>{@link Type#EQ}
   **   <li>{@link Type#LT}
   **   <li>{@link Type#LE}
   **   <li>{@link Type#GT}
   **   <li>{@link Type#GE}
   ** </ul>
   */
  static class Numeric extends Comparison<Number> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Number value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new <code>Temporal</code> criteria based on a given value.
     **
     ** @param  type             the search type operator to apply.
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  path             the entity property path to apply the
     **                          criteria on.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the criteria value.
     **                          <br>
     **                          Allowed object is {@link Number}.
     */
    private Numeric(final Type type, final String path, final Number value) {
      // ensure inheritance
      super(type, path);

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (SearchFilter)
    /**
     ** Returns the comparison value, or <code>null</code> if this filter is not
     ** a comparison filter.
     **
     ** @return                  the comparison value, or <code>null</code> if
     **                          this filter is not a comparison filter.
     **                          <br>
     **                          Possible object is {@link Number}.
     */
    @Override
    public Number value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Temporal
  // ~~~~~ ~~~~~~~~
  /**
   ** Creates <code>path = date</code> search criteria.
   ** <p>
   ** The operational type at this filter type are:
   ** <ul>
   **   <li>{@link Type#LT}
   **   <li>{@link Type#LE}
   **   <li>{@link Type#GT}
   **   <li>{@link Type#GE}
   ** </ul>
   */
  static class Temporal extends Comparison<Calendar> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Calendar value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new <code>Temporal</code> criteria based on a given value.
     **
     ** @param  type             the search type operator to apply.
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  path             the entity property path to apply the
     **                          criteria on.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the criteria value.
     **                          <br>
     **                          Allowed object is {@link Calendar}.
     */
    private Temporal(final Type type, final String path, final Calendar value) {
      // ensure inheritance
      super(type, path);

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (SearchFilter)
    /**
     ** Returns the comparison value, or <code>null</code> if this filter is not
     ** a comparison filter.
     **
     ** @return                  the comparison value, or <code>null</code> if
     **                          this filter is not a comparison filter.
     **                          <br>
     **                          Possible object is {@link Calendar}.
     */
    @Override
    public Calendar value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Literal
  // ~~~~~ ~~~~~~~
  /**
   ** Creates <code>path = string</code> search criteria.
   ** <p>
   ** The operational type at this filter type are:
   ** <ul>
   **   <li>{@link Type#EQ}
   **   <li>{@link Type#LT}
   **   <li>{@link Type#LE}
   **   <li>{@link Type#GT}
   **   <li>{@link Type#GE}
   **   <li>{@link Type#SW}
   **   <li>{@link Type#EW}
   **   <li>{@link Type#CO}
   ** </ul>
   */
  static class Literal extends Comparison<String> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new <code>Literal</code> criteria based on a given value.
     **
     ** @param  type             the search type operator to apply.
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  path             the entity property path to apply the
     **                          criteria on.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the criteria value.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Literal(final Type type, final String path, final String value) {
      // ensure inheritance
      super(type, path);

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (SearchFilter)
    /**
     ** Returns the comparison value, or <code>null</code> if this filter is not
     ** a comparison filter.
     **
     ** @return                  the comparison value, or <code>null</code> if
     **                          this filter is not a comparison filter.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Between
  // ~~~~~ ~~~~~~~
  /**
   ** Creates <code>path BETWEEN range.min AND range.max</code> search criteria.
   **
   ** @param  <T>                the type of the range entity representations.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   */
  static class Between<T> extends Comparison<Range<T>> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Range<T> value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new <code>Between</code> criteria based on a given value.
     **
     ** @param  path             the entity property path to apply the
     **                          criteria on.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the criteria value.
     **                          <br>
     **                          Allowed object is {@link Range} for type
     **                          <code>T</code>.
     */
    private Between(final String path, final Range<T> value) {
      // ensure inheritance
      super(Type.BW, path);

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (SearchFilter)
    /**
     ** Returns the comparison value, or <code>null</code> if this filter is not
     ** a comparison filter.
     **
     ** @return                  the comparison value, or <code>null</code> if
     **                          this filter is not a comparison filter.
     **                          <br>
     **                          Possible object is {@link Range} for type
     **                          <code>T</code>.
     */
    @Override
    public Range<T> value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class And
  // ~~~~~ ~~~
  /**
   ** Logically "ANDs" together the two specified instances of
   ** {@link SearchFilter}.
   ** <br>
   ** The resulting <i>conjunct</i> {@link SearchFilter} is <code>true</code>
   ** if and only if both of the specified predicates are <code>true</code>.
   */
  static class And extends Junction {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>And</code> w/ the left and right
     ** {@link SearchFilter}s provided.
     **
     ** @param  lhs                the left side of the <code>And</code>
     **                            {@link SearchFilter}.
     **                            <br>
     **                            Allowed object is {@link SearchFilter} of
     **                            type <code>T</code>.
     ** @param  rhs                the right side of the <code>And</code>
     **                            {@link SearchFilter}.
     **                            <br>
     **                            Allowed object is {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    private And(final SearchFilter<?> lhs, final SearchFilter<?> rhs) {
      // ensure inheritance
      super(Type.AND, lhs, rhs);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>And</code> w/ the left and right
     ** {@link SearchFilter}s provided.
     **
     ** @param  filter             the left side of a composite based filter.
     **                            <br>
     **                            Allowed object is {@link List} where each
     **                            element is of type {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    private And(final List<SearchFilter<?>> filter) {
      // ensure inheritance
      super(Type.AND, filter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Or
  // ~~~~~ ~~
  /**
   ** Logically "OR" together the two specified instances of
   ** {@link SearchFilter}s.
   ** <br>
   ** The resulting <i>disjunct</i> {@link SearchFilter} is <code>true</code>
   ** if and only if at least one of the specified predicates is
   ** <code>true</code>.
   */
  static class Or extends Junction {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Or</code> w/ the left and right
     ** {@link SearchFilter}s provided.
     **
     ** @param  lhs                the left side of the <code>And</code>
     **                            {@link SearchFilter}.
     **                            <br>
     **                            Allowed object is {@link SearchFilter} of
     **                            type <code>T</code>.
     ** @param  rhs                the right side of the <code>Or</code>
     **                            {@link SearchFilter}.
     **                            <br>
     **                            Allowed object is {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    private Or(final SearchFilter<?> lhs, final SearchFilter<?> rhs) {
      // ensure inheritance
      super(Type.OR, lhs, rhs);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Or</code> w/ the left and right
     ** {@link SearchFilter}s provided.
     **
     ** @param  filter             the left side of a composite based filter.
     **                            <br>
     **                            Allowed object is {@link List} where each
     **                            element is of type {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    private Or(final List<SearchFilter<?>> filter) {
      // ensure inheritance
      super(Type.OR, filter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Not
  // ~~~~~ ~~~
  /**
   ** Proxy the filter to return the negative of the value.
   **
   ** @param  <T>                the type of the contained predicate value.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the filters
   **                            implementing this interface (compare operations
   **                            can use their own specific types instead of
   **                            types defined by this interface only).
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   */
  static class Not<T> implements SearchFilter<T> {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    /**
     ** The {@link SearchFilter} this negative {@link SearchFilter} belongs
     ** to.
     */
    private final SearchFilter<T> filter;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Not</code> which negates the specified
     ** {@link SearchFilter}.
     **
     ** @param  filter             the {@link SearchFilter} that is being
     **                            negated.
     **                            <br>
     **                            Allowed object is {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    private Not(final SearchFilter<T> filter) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.filter = filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter
    /**
     ** Returns the internal filter that is being negated.
     **
     ** @return                    the internal filter that is being negated.
     **                            <br>
     **                            Possible object is {@link SearchFilter} of
     **                            type <code>T</code>.
     */
    public SearchFilter<T> filter() {
      return this.filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type (SearchFilter)
    /**
     ** Returns the type of this filter.
     **
     ** @return                    the type of this filter.
     **                            <br>
     **                            Possible object is {@link Type}.
     */
    @Override
    public final Type type() {
      return Type.NOT;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: path (SearchFilter)
    /**
     ** Returns the path to the attribute to filter by, or <code>null</code> if
     ** this filter is not a comparison filter.
     **
     ** @return                  the path to the attribute to filter by, or
     **                          <code>null</code> if this filter is not a
     **                          comparison filter.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String path() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (SearchFilter)
    /**
     ** Returns the comparison value, or <code>null</code> if this filter is not
     ** a comparison filter.
     **
     ** @return                  the comparison value, or <code>null</code> if
     **                          this filter is not a comparison filter.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    @Override
    public T value() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isComplex (SearchFilter)
    /**
     ** Whether this filter is a complex multi-valued attribute value filter.
     **
     ** @return                  <code>true</code> if this filter is a complex
     **                          multi-valued attribute value filter or
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean isComplex() {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Presence
  // ~~~~~ ~~~~~~~~
  /**
   ** A presence {@link SearchFilter} determines if the attribute provided is
   ** present in the <code>Entity</code>.
   **
   ** @param  <T>                the type of the contained predicate value.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the filters
   **                            implementing this interface (compare operations
   **                            can use their own specific types instead of
   **                            types defined by this interface only).
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   */
  static class Presence<T> implements SearchFilter<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The path this {@link SearchFilter} belongs to. */
    private final String path;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Presence</code> which belongs to the specified
     ** path.
     **
     ** @param  path             the entity property path to apply the
     **                          comparison on.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Presence(final String path) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.path = path;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type (SearchFilter)
    /**
     ** Returns the type of this filter.
     **
     ** @return                    the type of this filter.
     **                            <br>
     **                            Possible object is {@link Type}.
     */
    @Override
    public Type type() {
      return Type.PR;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: path (SearchFilter)
    /**
     ** Returns the path to the attribute to filter by, or <code>null</code> if
     ** this filter is not a comparison filter.
     **
     ** @return                  the path to the attribute to filter by, or
     **                          <code>null</code> if this filter is not a
     **                          comparison filter.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String path() {
      return this.path;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (SearchFilter)
    /**
     ** Returns the comparison value, or <code>null</code> if this filter is not
     ** a comparison filter.
     **
     ** @return                  the comparison value, or <code>null</code> if
     **                          this filter is not a comparison filter.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    @Override
    public T value() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isComplex (SearchFilter)
    /**
     ** Whether this filter is a complex multi-valued attribute value filter.
     **
     ** @return                  <code>true</code> if this filter is a complex
     **                          multi-valued attribute value filter or
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean isComplex() {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of this filter.
   **
   ** @return                    the type of this filter.
   **                            <br>
   **                            Possible object is {@link Type}.
   */
  Type type();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the path to the attribute to filter by, or <code>null</code> if
   ** this filter is not a comparison filter.
   **
   ** @return                    the path to the attribute to filter by, or
   **                            <code>null</code> if this filter is not a
   **                            comparison filter.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String path();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the comparison value, or <code>null</code> if this filter is not a
   ** comparison filter or a value filter for complex multi-valued attributes.
   **
   ** @return                    the comparison value, or <code>null</code> if
   **                            this filter is not a comparison filter or a
   **                            value filter for complex multi-valued
   **                            attributes.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  T value();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isComplex
  /**
   ** Whether this filter is a complex multi-valued attribute value filter.
   **
   ** @return                    <code>true</code> if this filter is a complex
   **                            multi-valued attribute value filter or
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean isComplex();

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to <code>logically and</code>'s together the two specified
   ** instances of <code>SearchFilter</code>s
   ** <p>
   ** The resulting <i>conjunct</i> <code>SearchFilter</code> is
   ** <code>true</code> if and only if at both of the specified predicates are
   ** <code>true</code>.
   **
   ** @param  lhs                the left-hand-side filter.
   **                            <br>
   **                            Allowed object is <code>SearchFilter</code> for
   **                            type <code>T</code>.
   ** @param  rhs                the right-hand-side filter.
   **                            <br>
   **                            Allowed object is <code>SearchFilter</code> for
   **                            type <code>T</code>.
   **
   ** @return                    the result of <code>(lhs &amp;&amp; rhs)</code>
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type <code>T</code>.
   */
  public static SearchFilter<?> and(final SearchFilter<?> lhs, final SearchFilter<?> rhs) {
    return new And(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to create <code>logically and</code>
   ** <code>SearchFilter</code> using the provided list of
   ** <code>SearchFilter</code>s.
   ** <p>
   ** Creating a new <code>logically and</code> <code>SearchFilter</code> with a
   ** <code>null</code> or empty list of <code>SearchFilter</code>s is
   ** equivalent to <em>alwaysTrue</em>.
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of
   **                            <code>SearchFilter</code> for type
   **                            <code>T</code>.
   **
   ** @return                    the newly created "AND" filter.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static SearchFilter<?> and(final SearchFilter<?>... filters) {
    return and(CollectionUtility.list(filters));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to create <code>logically and</code>
   ** <code>SearchFilter</code> using the provided list of
   ** <code>SearchFilter</code>s.
   ** <p>
   ** Creating a new <code>logically and</code> <code>SearchFilter</code>
   ** with a <code>null</code> or empty list of <code>SearchFilter</code>s is
   ** equivalent to <em>alwaysTrue</em>.
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>SearchFilter</code>.
   **
   ** @return                    the newly created "AND" filter.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type <code>T</code>.
   */
  public static SearchFilter<?> and(final List<SearchFilter<?>> filters) {
    switch (filters.size()) {
      case 0  : return null;
      case 1  : return filters.iterator().next();
      default : return new And(CollectionUtility.list(filters));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to <code>logically or</code>'s together the two specified
   ** instances of <code>SearchFilter</code>s.
   ** <p>
   ** The resulting <i>disjunct</i> <code>SearchFilter</code> is
   ** <code>true</code> if and only if at least one of the specified predicates
   ** is <code>true</code>.
   **
   ** @param  lhs                the left-hand-side filter.
   **                            <br>
   **                            Allowed object is <code>SearchFilter</code>.
   ** @param  rhs                the right-hand-side filter.
   **                            <br>
   **                            Allowed object is <code>SearchFilter</code>.
   **
   ** @return                    the result of <code>(lhs || rhs)</code>
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type <code>T</code>.
   */
  public static SearchFilter<?> or(final SearchFilter<?> lhs, final SearchFilter<?> rhs) {
    return new Or(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to create <code>logically or</code>
   ** <code>SearchFilter</code> using the provided array of
   ** <code>SearchFilter</code>s.
   ** <p>
   ** Creating a new <code>logically or</code> <code>SearchFilter</code> with a
   ** <code>null</code> or empty list of <code>SearchFilter</code>s is
   ** equivalent to <em>alwaysTrue</em>.
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of
   **                            <code>SearchFilter</code>.
   **
   ** @return                    the newly created "OR" filter.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static SearchFilter<?> or(final SearchFilter<?>... filters) {
    return or(CollectionUtility.list(filters));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to create <code>logically or</code>
   ** <code>SearchFilter</code> using the provided list of
   ** <code>SearchFilter</code>s.
   ** <p>
   ** Creating a new <code>logically or</code> <code>SearchFilter</code> with
   ** a <code>null</code> or empty list of <code>SearchFilter</code>s is
   ** equivalent to <em>alwaysTrue</em>.
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>SearchFilter</code>.
   **
   ** @return                    the newly created "OR" filter.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type <code>T</code>.
   */
  public static SearchFilter<?> or(final List<SearchFilter<?>> filters) {
    switch (filters.size()) {
      case 0  : return null;
      case 1  : return filters.iterator().next();
      default : return new Or(CollectionUtility.list(filters));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   not
  /**
   ** Factory method to create <code>logically negate</code>
   ** <code>SearchFilter</code>.
   ** <br>
   ** The resulting <code>SearchFilter</code> is <code>true</code> if and
   ** only if the specified filter is <code>false</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  filter             the <code>SearchFilter</code> to negate.
   **                            <br>
   **                            Allowed object is <code>SearchFilter</code>.
   **
   ** @return                    the result of <code>(!filter)</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type <code>T</code>.
   */
  public static <T> SearchFilter<T> not(final SearchFilter<T> filter) {
    return new Not<T>(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to create <code>presence</code>
   ** <code>SearchFilter</code> that select only a path if its exists.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the newly created <code>presence</code> filter.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type <code>T</code>.
   */
  public static SearchFilter pr(final String path) {
    return new Presence(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>lexically equal to</em> of the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Boolean}.
   */
  public static SearchFilter<Boolean> eq(final String path, final Boolean value) {
    return new Bool(Type.EQ, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>lexically equal to</em> of the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Number}.
   */
  public static SearchFilter<Number> eq(final String path, final Number value) {
    return new Numeric(Type.EQ, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>lexically equal to</em> of the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Calendar}.
   */
  public static SearchFilter<Calendar> eq(final String path, final Calendar value) {
    return new Temporal(Type.EQ, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>lexically equal to</em> of the specified <code>value</code>.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified <code>value</code> were
   ** <code>{"hairColor": "brown"}</code>, this would match any path with a
   ** value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "BROWN"}</code>
   ** <br>
   ** but would <em>not</em> match any path that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any path that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   ** <p>
   ** <b>NOTE:</b> <i>Lexical</i> comparison of two string values compares the
   ** characters of each value, even if the string values could be interpreted
   ** as numeric. The values <code>"01"</code> and <code>"1"</code> are unequal
   ** lexically, although they would be equivalent arithmetically.
   ** <p>
   ** Two attributes with binary syntax are equal if and only if their
   ** constituent bytes match.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link String}.
   */
  public static SearchFilter<String> eq(final String path, final String value) {
    return new Literal(Type.EQ, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that is
   ** <em>less than</em> the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Number}.
   */
  static SearchFilter<Number> lt(final String path, final Number value) {
    return new Numeric(Type.LT, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that is
   ** <em>less than</em> the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Calendar}.
   */
  static SearchFilter<Calendar> lt(final String path, final Calendar value) {
    return new Temporal(Type.LT, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that is
   ** <em>less than</em> the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link String}.
   */
  static SearchFilter<String> lt(final String path, final String value) {
    return new Literal(Type.LT, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that is
   ** <em>less than or equal to</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Number}.
   */
  static SearchFilter<Number> le(final String path, final Number value) {
    return new Numeric(Type.LE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that is
   ** <em>less than or equal to</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Calendar}.
   */
  static SearchFilter<Calendar> le(final String path, final Calendar value) {
    return new Temporal(Type.LE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that is
   ** <em>less than or equal to</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link String}.
   */
  static SearchFilter<String> le(final String path, final String value) {
    return new Literal(Type.LE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is greater
   ** than the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Number}.
   */
  static SearchFilter<Number> gt(final String path, final Number value) {
    return new Numeric(Type.GT, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is greater
   ** than the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Calendar}.
   */
  static SearchFilter<Calendar> gt(final String path, final Calendar value) {
    return new Temporal(Type.GT, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is greater
   ** than the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link String}.
   */
  static SearchFilter<String> gt(final String path, final String value) {
    return new Literal(Type.GT, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than or equal to</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Number}.
   */
  static SearchFilter<Number> ge(final String path, final Number value) {
    return new Numeric(Type.GE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than or equal to</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Calendar}.
   */
  static SearchFilter<Calendar> ge(final String path, final Calendar value) {
    return new Temporal(Type.GE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than or equal to</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link String}.
   */
  static SearchFilter<String> ge(final String path, final String value) {
    return new Literal(Type.GE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>starts with</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Number}.
   */
  static SearchFilter<Number> sw(final String path, final Number value) {
    return new Numeric(Type.SW, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>starts with</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Calendar}.
   */
  static SearchFilter<Calendar> sw(final String path, final Calendar value) {
    return new Temporal(Type.LE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>initial substring</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link String}.
   */
  static SearchFilter<String> sw(final String path, final String value) {
    return new Literal(Type.SW, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>ends with</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Number}.
   */
  static SearchFilter<Number> ew(final String path, final Number value) {
    return new Numeric(Type.SW, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>ends with</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Calendar}.
   */
  static SearchFilter<Calendar> ew(final String path, final Calendar value) {
    return new Temporal(Type.LE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>contains as a final substring</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link String}.
   */
  static SearchFilter<String> ew(final String path, final String value) {
    return new Literal(Type.EW, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Number}.
   */
  static SearchFilter<Number> co(final String path, final Number value) {
    return new Numeric(Type.SW, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link Calendar}.
   */
  static SearchFilter<Calendar> co(final String path, final Calendar value) {
    return new Temporal(Type.LE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>contains as any substring</em> the specified <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for type {@link String}.
   */
  static SearchFilter<String> co(final String path, final String value) {
    return new Literal(Type.CO, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bw
  /**
   ** Factory method to create <code>between</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>between</em> of the specified {@link Range} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  min                the lower limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  max                the upper limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if the value of the
   **                            corresponding attribute of the path
   **                            <em>between</em> the lower limit (inclusive)
   **                            and upper limit (exclusive) value range;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for {@link Range} for type {@link Integer}.
   */
  public static SearchFilter<Range<Integer>> bw(final String path, final Integer min, final Integer max) {
    return new Between<Integer>(path, Range.<Integer>immutable(min, max));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bw
  /**
   ** Factory method to create <code>between</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>between</em> of the specified {@link Range} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  min                the lower limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  max                the upper limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if the value of the
   **                            corresponding attribute of the path
   **                            <em>between</em> the lower limit (inclusive)
   **                            and upper limit (exclusive) value range;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for {@link Range} for type {@link Long}.
   */
  public static SearchFilter<Range<Long>> bw(final String path, final Long min, final Long max) {
    return new Between<Long>(path, Range.<Long>immutable(min, max));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bw
  /**
   ** Factory method to create <code>between</code> input path
   ** <code>SearchFilter</code> that select only a path with a value that
   ** <em>between</em> of the specified {@link Range} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  min                the lower limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  max                the upper limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    an instance of <code>SearchFilter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if the value of the
   **                            corresponding attribute of the path
   **                            <em>between</em> the lower limit (inclusive)
   **                            and upper limit (exclusive) value range;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>SearchFilter</code>
   **                            for {@link Range} for type {@link Calendar}.
   */
  public static SearchFilter<Range<Calendar>> bw(final String path, final Calendar min, final Calendar max) {
    return new Between<Calendar>(path, Range.<Calendar>immutable(min, max));
  }
}