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

    File        :   Pair.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Pair.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object;

import java.util.Map;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Pair
// ~~~~~ ~~~~
/**
 ** An arbitrary pair of objects. Convenient implementation of Map.Entry.
 */
public class Pair<T1 extends Serializable, T2 extends Serializable> implements Map.Entry<T1, T2>
                                                                    ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2284640159456809994")
  private static final long serialVersionUID = 1916953090029129208L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the key corresponding to this value pair */
  public T1 tag;

  /** the value corresponding to this value pair */
  public T2 value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Pair</code>.
   */
  public Pair() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Pair</code> with the specified tag name and value.
   **
   ** @param  tag                the unique name of the attribute.
   ** @param  value              the for the attribute.
   */
  private Pair(final T1 tag, final T2 value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.tag   = tag;
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getKey (Map.Entry)
  /**
   ** Returns the key corresponding to this entry.
   **
   ** @return                    the key corresponding to this entry.
   **
   ** @throws IllegalStateException implementations may, but are not required
   **                               to, throw this exception if the entry has
   **                               been removed from the backing map.
   */
  public final T1 getKey() {
    return this.tag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue (Map.Entry)
  /**
   ** Replaces the value corresponding to this value pair with the specified
   ** value (optional operation).  (Writes through to the map.)
   ** <p>
   ** The behavior of this call is undefined if the mapping has already been
   ** removed from the map (by the iterator's <code>remove</code> operation).
   **
   ** @param  value              the new value to be stored in this value pair.
   **
   ** @return                    the old value corresponding to the entry.
   */
  public T2 setValue(final T2 value) {
    final T2 old = this.value;
    this.value = value;
    return old;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValue (Map.Entry)
  /**
   ** Returns the value corresponding to this entry.
   ** <p>
   ** If the mapping has been removed from the backing map (by the iterator's
   ** <code>remove</code> operation), the results of this call are undefined.
   **
   ** @return                    the value corresponding to this entry.
   **
   ** @throws IllegalStateException implementations may, but are not required
   **                               to, throw this exception if the entry has
   **                               been removed from the backing map.
   */
  public final T2 getValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Obtains a mutable pair of two objects inferring the generic types.
   ** <p>
   ** This factory allows the pair to be created using inference to obtain the
   ** generic types.
   **
   ** @param  <L>                the left element type.
   ** @param  <R>                the right element type.
   ** @param  left               the left element, may be <code>null</code>.
   ** @param  right              the right element, may be <code>null</code>.
   **
   ** @return                    a pair formed from the two parameters, never
   **                            <code>null</code>.
   */
  public static <L extends Serializable, R extends Serializable> Pair<L, R> of(final L left, final R right) {
    return new Pair<L, R>(left, right);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int result = this.tag != null ? this.tag.hashCode() : 0;
    result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Pair</code>s are considered equal if and only if they represent
   ** the same encoded, decoded and template value. As a consequence, two given
   ** <code>Pair</code>s may be different even though they contain the same
   ** attribute value.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    // test identity
    if (this == other)
      return true;

    // test for null and exact class matches
    if (other == null || getClass() != other.getClass())
      return false;

    final Pair<?, ?> that = (Pair<?, ?>)other;
    return (equals(this.tag, that.tag) && equals(this.value, that.value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                   the string representation for this instance in
   **                           its minimal form.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("Pair{");
    builder.append(String.format("%s=%s", this.tag.toString(), this.value.toString()));
    builder.append("}");
    return builder.toString();
  }

  private static boolean equals(final Object o1, final Object o2) {
    if (o1 == null) {
      return o2 == null;
    }
    else if (o2 == null) {
      return false;
    }
    else {
      return o1.equals(o2);
    }
  }
}