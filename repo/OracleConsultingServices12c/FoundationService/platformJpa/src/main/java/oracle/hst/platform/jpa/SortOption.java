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
    Subsystem   :   Generic Persistence Interface

    File        :   SortOption.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SortOption.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

import java.util.stream.Collectors;

////////////////////////////////////////////////////////////////////////////////
// class SortOption
// ~~~~~ ~~~~~~~~~~
/**
 ** Sort option for queries.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SortOption implements Iterable<SortOption.Order> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final SortOption    NONE = SortOption.by(new Order[0]);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<Order> order;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Direction
  // ~~~~ ~~~~~~~~~
  /**
   ** The direction in which the sortBy parameter is applied.
   */
  public static enum Direction {
      /** the ascending sort direction */
      ASCENDING("ascending")
      /** the descending sort direction */
    , DESCENDING("descending")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Direction</code> with a constraint value.
     **
     ** @param  value            the constraint name (used in entity schemas) of
     **                          the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Direction(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: ascending
    /**
     ** Whether the direction is ascending.
     **
     ** @return                  <code>true</code> if the direction is
     **                          ascending; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean ascending() {
      return this.equals(ASCENDING);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: descending
    /**
     ** Whether the direction is descending.
     **
     ** @return                  <code>true</code> if the direction is
     **                          descending; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean descending() {
      return this.equals(DESCENDING);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Direction</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Direction</code> constraint.
     **                          <br>
     **                          Possible object is <code>Direction</code>.
     */
    public static Direction from(final String value) {
      for (Direction cursor : Direction.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Order
  // ~~~~ ~~~~~~
  /**
   ** Implements the pairing of an {@link Direction} and a property.
   ** <br>
   ** It is used to provide input for {@link SortOption}
   */
  public static class Order {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String    property;
    public final Direction direction;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new <code>Order</code> instance.
     ** <br>
     ** If direction is <code>null</code> then order defaults to
     ** {@link Direction#ASCENDING}.
     **
     ** @param  property         the property name to apply the sort on.
     **                          <br>
     **                          Must not be <code>null</code> or empty.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  direction        the direction of the sort to apply.
     **                          <br>
     **                          Can be <code>null</code>, will default than to
     **                          {@link Direction#ASCENDING}.
     **                          <br>
     **                          Allowed object is {@link Direction}.
     */
    private Order(final String property, final Direction direction) {
      // ensure inheritance
      super();

      // initilaize instance attributes
      this.property  = property;
      this.direction = direction == null ? Direction.ASCENDING : direction;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: property
    /**
     ** Returns the property of the order constraint.
     **
     ** @return                  the property of the order constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String property() {
      return this.property;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: direction
    /**
     ** Returns the sort direction of the order constraint.
     **
     ** @return                  the sort direction of the order constraint.
     **                          <br>
     **                          Possible object is {@link Direction}.
     */
    public final Direction direction() {
      return this.direction;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: ascending
    /**
     ** Whether the direction is ascending.
     **
     ** @return                  <code>true</code> if the direction is
     **                          ascending; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean ascending() {
      return this.direction.ascending();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: descending
    /**
     ** Whether the direction is descending.
     **
     ** @return                  <code>true</code> if the direction is
     **                          descending; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean descending() {
      return this.direction.descending();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: by
    /**
     ** Creates a new <code>Order</code> instance by taking a single property.
     ** <br>
     ** If either <code>property</code> or <code>direction</code> is
     ** <code>null</code> the <code>Order</code> will not be used.
     **
     ** @param  property         the property name to apply the sort on.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  direction        the sort direction of the order constraint.
     **                          <br>
     **                          Allowed object is {@link Direction}.
     **
     ** @return                  the <code>Order</code> criteria to apply.
     **                          <br>
     **                          Possible object is <code>Order</code>.
     */
    public static Order by(final String property, final Direction direction) {
      return (direction == null) ? null : direction == Direction.ASCENDING ? ascending(property) : descending(property);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: by
    /**
     ** Creates a new <code>Order</code> instance by taking a single property.
     ** <br>
     ** If <code>property</code> is <code>null</code> the <code>Order</code>
     ** will not be used.
     ** <br>
     ** Direction is {@link Direction#ASCENDING}.
     **
     ** @param  property         the property name to apply the sort on.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Order</code> criteria to apply.
     **                          <br>
     **                          Possible object is <code>Order</code>.
     */
    public static Order by(final String property) {
      return ascending(property);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: ascending
    /**
     ** Creates a new <code>Order</code> instance by taking a single property.
     ** <br>
     ** If <code>property</code> is <code>null</code> the <code>Order</code>
     ** will not be used.
     ** <br>
     ** Direction is {@link Direction#ASCENDING}.
     **
     ** @param  property         the property name to apply the sort on.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Order</code> criteria to apply.
     **                          <br>
     **                          Possible object is <code>Order</code>.
     */
    public static Order ascending(final String property) {
      return property == null ? null : new Order(property, Direction.ASCENDING);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: descending
    /**
     ** Creates a new <code>Order</code> instance by taking a single property.
     ** <br>
     ** If <code>property</code> is <code>null</code> the <code>Order</code>
     ** will not be used.
     ** <br>
     ** Direction is {@link Direction#DESCENDING}.
     **
     ** @param  property         the property name to apply the sort on.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Order</code> criteria to apply.
     **                          <br>
     **                          Possible object is <code>Order</code>.
     */
    public static Order descending(final String property) {
      return property == null ? null : new Order(property, Direction.DESCENDING);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SortOption</code> with the elements applicable on a
   ** search operation.
   **
   ** @param  order              the collection of {@link Order} criteria to
   **                            apply.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Order}.
   */
  private SortOption(final List<Order> order) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.order = order;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of order criterias in this <code>SortOption</code>.
   ** <br>
   ** If this <code>SortOption</code> contains more than
   ** <code>Integer.MAX_VALUE</code> order criterias, returns
   ** <code>Integer.MAX_VALUE</code>.
   **
   ** @return                    the number of order criterias in this
   **                            <code>SortOption</code>.
   **                            <br>
   **                            Possible object is <code>int</code>
   */
  public final int size() {
    return this.order == null ? 0 : this.order.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   criteria
  /**
   ** Returns the collection of {@link Order} criteria.
   ** <p>
   ** This accessor method returns a reference to the live {@link List}, not a
   ** snapshot. Therefore any modification you make to the returned list will be
   ** present inside the object instance.
   **
   ** @return                    the collection of {@link Order} criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Order}.
   */
  public final List<Order> criteria() {
    return this.order;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iterable)
  /**
   ** Returns an {@link Iterator} over the collection of {@link Order} criteria.
   **
   ** @return                    an {@link Iterator} over the collection of
   **                            {@link Order} criteria.
   **                            <br>
   **                            Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<SortOption.Order> iterator() {
    return this.order.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   by
  /**
   ** Creates a new <code>SortOption</code> for the given properties.
   **
   ** @param  properties         the properties to apply the sort on.
   **                            <br>
   **                            Must not be <code>null</code> or empty.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Order} criteria to apply.
   **                            <br>
   **                            Possible object is <code>SortOption</code>.
   */
  public static SortOption by(final String... properties) {
    return properties.length == 0 ? SortOption.NONE : by(Arrays.asList(properties).stream().map(p -> new Order(p, Direction.ASCENDING)).collect(Collectors.toList()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   by
  /**
   ** Creates a new <code>SortOption</code> for the given {@link Order}s.
   **
   ** @param  orders             the {@link Order} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Order}.
   **
   ** @return                    the {@link Order} criteria to apply.
   **                            <br>
   **                            Possible object is <code>SortOption</code>.
   */
  public static SortOption by(final Order... orders) {
    return by(Arrays.asList(orders));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   by
  /**
   ** Creates a new <code>SortOption</code> for the given {@link Order}s.
   **
   ** @param  order              the {@link Order} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Order}.
   **
   ** @return                    the {@link Order} criteria to apply.
   **                            <br>
   **                            Possible object is <code>SortOption</code>.
   */
  public static SortOption by(final List<Order> order) {
    return (order == null || order.isEmpty()) ? SortOption.NONE : new SortOption(order);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overidden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of
   **       the two objects must produce distinct integer results. However,
   **       the programmer should be aware that producing distinct integer
   **       results for unequal objects may improve the performance of hash
   **       tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int hashCode() {
    return this.order.hashCode();
  }
}