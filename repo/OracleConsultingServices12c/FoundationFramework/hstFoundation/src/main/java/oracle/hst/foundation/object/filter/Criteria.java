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

    File        :   Criteria.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Criteria.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Criteria
// ~~~~~ ~~~~~~~~
/**
 ** The <code>Criteria</code> provides the description of a filter that
 ** can be applied on filterable entities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class Criteria implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A database filter declaration that is a NOP-filter
   */
  private final Conjunction     CONJUNCTION_DEFAULT = Conjunction.AND;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1331720758246550710")
  private static final long     serialVersionUID    = 9193348998643520153L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Conjunction           conjunction         = CONJUNCTION_DEFAULT;
  private final List<Criterion> criterions          = new ArrayList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Conjunction
  // ~~~~~ ~~~~~~~~~~
  /**
   ** This enum store the Conjunction combinations of <code>Criteria</code>s.
   */
  public static enum Conjunction {
      AND
    , OR
    , NOT
    ;
    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Criteria</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Criteria() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Criteria</code> which populates its properties from the
   ** specified <code>criteria</code>.
   ** <br>
   ** Copy Constructor
   **
   ** @param  other              the template <code>Criteria</code> to populate
   **                            the instance attributes from.
   */
  public Criteria(final Criteria other) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.conjunction = other.conjunction;
    for (Criterion cursor : other.criterions)
      this.criterions.add(new Criterion(cursor));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Criteria</code> which put the provided
   ** {@link Criterion}s in conjunction.
   **
   ** @param  criterions         the template {@link Criterion}s to populate the
   **                            instance attributes from.
   */
  public Criteria(final Criterion[] criterions) {
    for (Criterion c : criterions)
      this.criterions.add(c);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Returns <code>true</code> if thme number of {@link Criterion}s in this
   ** <code>Criteria</code> is <code>0</code> (Zero).
   **
   ** @return                    <code>true</code> if thme number of
   **                           {@link Criterion}s in this <code>Criteria</code>
   **                           is <code>0</code> (Zero); otherwise
   **                           <code>false</code>.
   */
  public boolean empty() {
    return size() == 0;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of {@link Criterion}s in this <code>Criteria</code>.
   **
   ** @return                    the number of {@link Criterion}s in this
   **                            <code>Criteria</code>.
   */
  public int size() {
    return this.criterions.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   conjunction
  /**
   ** Sets the conjunction this <code>Criteria</code>.
   **
   ** @param  value              the conjunction to set for this
   **                            <code>Criteria</code>.
   */
  public void conjunction(final Conjunction value) {
    this.conjunction = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   conjunction
  /**
   ** Returns the conjunction this <code>Criteria</code>.
   **
   ** @return                    the conjunction this <code>Criteria</code>.
   */
  public Conjunction conjunction() {
    return this.conjunction;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   criterions
  /**
   ** Returns the value of the criterions property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the resource
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    criterions().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list
   ** {@link Criterion}.
   **
   ** @return                    the {@link List} of {@link Criterion}s.
   */
  public List<Criterion> criterions() {
    return this.criterions;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + toString().hashCode();
    return hash;
  }

  /////////////////////////////////////////////////////////////////////////////
  // Method: equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Role</code> object that represents
   ** the same <code>name</code>.
   **
   ** @param other             the object to compare this <code>Role</code>
   **                          with.
   **
   ** @return                  <code>true</code> if the <code>Role</code>s
   **                          are equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other == this) {
      return true;
    }
    else if (!(other instanceof Criteria))
      return false;

    final Criteria that = (Criteria)other;
    return toString().equals(that.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Return a string representation of the filter for debugging
   **
   ** @return                    a string representation of the filter.
   */
  @Override
  public String toString() {
     final StringBuilder buffer = new StringBuilder();
     buffer.append(conjunction);
     for (Criterion cursor : criterions)
       buffer.append(cursor);
     return buffer.toString();
  }
}