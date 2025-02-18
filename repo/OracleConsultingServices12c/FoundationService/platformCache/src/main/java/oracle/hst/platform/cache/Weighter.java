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
    Subsystem   :   Common Shared Utility

    File        :   Weighter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Weighter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.cache;

////////////////////////////////////////////////////////////////////////////////
// interface Weighter
// ~~~~~~~~~ ~~~~~~~~
/**
 ** Calculates the weights of cache entries.
 ** <br>
 ** The total weight threshold is used to determine when an eviction is required.
 **
 ** @param  <K>                  the most general key type this
 **                              <code>Weighter</code> will be able to compute.
 **                              This is normally {@link Object}.
 ** @param  <V>                  the most general value type this
 **                              <code>Weighter</code> will be able to compute.
 **                              This is normally {@link Object}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 **
 */
public interface Weighter<K extends Object, V extends Object> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // enum Singleton
  // ~~~~ ~~~~~~~~~
  /**
   ** A <code>Weighter</code> where an entry has a weight of <code>1</code>.
   */
  enum Singleton implements Weighter<Object, Object> {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////
  
    ////////////////////////////////////////////////////////////////////////////
    // Method: weight (Weighter)
    /**
     ** Returns the weight of a cache entry.
     ** <br>
     ** There is no unit for entry weights; rather they are simply relative to
     ** each other.
     **
     ** @param  key              the key represented by the entry to weight.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  value            the value represented by the entry to weight.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     **
     ** @return                  the weight of the entry; must be non-negative.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int weight(Object key, Object value) {
      return 1;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Bounded
  // ~~~~ ~~~~~~~~
  /**
   ** A <code>Weighter</code> that enforces that the weight is non-negative.
   */
  final class Bounded<K, V> implements Weighter<K, V> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Weighter<? super K, ? super V> delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Bounded</code> <code>Weighter</code>.
     **
     ** @param  delegate         the weighter to weights the entry.
     **                          <br>
     **                          Allowed object is <code>Weighter</code>.
     */
    private Bounded(final Weighter<? super K, ? super V> delegate) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.delegate = delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////
  
    ////////////////////////////////////////////////////////////////////////////
    // Method: weight (Weighter)
    /**
     ** Returns the weight of a cache entry.
     ** <br>
     ** There is no unit for entry weights; rather they are simply relative to
     ** each other.
     **
     ** @param  key              the key represented by the entry to weight.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  value            the value represented by the entry to weight.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     **
     ** @return                  the weight of the entry; must be non-negative.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws AssertionError   if a weight is already been set.
     */
    @Override
    public int weight(final K key, final V value) {
      int weight = this.delegate.weight(key, value);
      assert (weight >= 0);
      return weight;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   weight
  /**
   ** Returns the weight of a cache entry.
   ** <br>
   ** There is no unit for entry weights; rather they are simply relative to
   ** each other.
   **
   ** @param  key                the key represented by the entry to weight.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              the value represented by the entry to weight.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the weight of the entry; must be non-negative.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  int weight(final K key, final V value);
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleton
  /**
   ** Factory method to create a <code>Weighter</code> where an entry has a
   ** weight of <code>1</code>.
   **
   ** @param  <K>                the most general key type the
   **                            <code>Weighter</code> will be able to compute.
   **                            This is normally {@link Object}.
   ** @param  <V>                the most general value type the
   **                            <code>Weighter</code> will be able to compute.
   **                            This is normally {@link Object}.
   **
   ** @return                    a <code>Weighter</code> where an entry has a
   **                            weight of <code>1</code>
   **                            <br>
   **                            Possible object is <code>Weighter</code>.
   */
  static <K, V> Weighter<K, V> singleton() {
    @SuppressWarnings("unchecked")
    final Weighter<K, V> self = (Weighter<K, V>)Singleton.INSTANCE;
    return self;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   bounded
  /**
   ** Factory method to create a <code>Weighter</code> that enforces that the
   ** weight is non-negative.
   **
   ** @param  <K>                the most general key type the
   **                            <code>Weighter</code> will be able to compute.
   **                            This is normally {@link Object}.
   ** @param  <V>                the most general value type the
   **                            <code>Weighter</code> will be able to compute.
   **                            This is normally {@link Object}.
   ** @param  delegate           the weighter to weights the entry.
   **                            <br>
   **                            Allowed object is <code>Weighter</code>.
   **
   ** @return                    a <code>Weighter</code> that enforces that the
   **                            weight is non-negative.
   **                            <br>
   **                            Possible object is <code>Weighter</code>.
   */
  static <K, V> Weighter<K, V> bounded(final Weighter<K, V> delegate) {
    return new Bounded<>(delegate);
  }
}