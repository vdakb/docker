/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   Accessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Accessor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.model;

////////////////////////////////////////////////////////////////////////////////
// abstract class Accessor methods
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The <code>Accessor</code> act as generic wrapper for a object instance of
 ** Oracle Identity Manager.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class Accessor implements Comparable<Accessor> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The internal system identifier of this <code>RepositoryObject</code>.
   */
  protected final long   key;

  /**
   ** The public name of this <code>RepositoryObject</code>.
   */
  protected final String name;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Accessor</code> which is associated with the specified
   ** task.
   **
   ** @param  key                the internal system identifier of the
   **                            <code>Accessor</code> to load.
   ** @param  name               the name of the <code>Accessor</code>.
   */
  protected Accessor(final long key, final String name) {
    // ensure inheritance
    super();

    this.key  = key;
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the internal system identifier of this
   ** <code>Repository Object</code>.
   **
   ** @return                    the public name of this
   **                            <code>Repository Object</code>.
   */
  public final long key() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the public name of this <code>Repository Object</code>.
   **
   ** @return                    the public name of this
   **                            <code>Repository Object</code>.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (Comparable)
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   **
   ** @param  other              the Object to be compared.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **
   ** @throws ClassCastException if the specified object's type is not
   **                            an instance of <code>Accessor</code>.
   */
  public int compareTo(final Accessor other) {
    if (this.key < other.key)
      return -1;
    else if(this.key > other.key)
      return 1;
    else
      return 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns the hash code value for this <code>Repository Object</code>.
   ** <p>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two instances
   ** <code>s1</code> and <code>s2</code>, as required by the general contract
   ** of <code>Object.hashCode()</code>.
   **
   ** @return                    the hash code value for this
   **                            <code>Repository Object</code>.
   */
  public int hashCode() {
    return Long.toString(this.key).hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Compares the specified object with this nested set for equality.
   ** Returns <code>true</code> if the specified object is also a set, the two
   ** sets have the same size, and every element of the specified set is
   ** contained in this set.
   ** <p>
   ** This implementation first checks if the given object is a
   ** <code>Accessor</code>. If so, the hash code values of this nested set
   ** and the specified <code>HashSetOfSets</code> are compared. Since the hash
   ** code values are being cached, this represents a quick solution if the sets
   ** aren't equal. However, if the hash code values are equal, it cannot be
   ** assumed that the sets themselves are equal, since different sets can have
   ** the same hash code value. In this case, the result of the super method
   ** <code>equals()</code> is returned.
   **
   ** @param  other              object to be compared for equality with this
   **                            nested set.
   **
   ** @return                    <code>true</code> if the specified object is
   **                            equal to this nested set, <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Accessor) {
      return (this.hashCode() == ((Accessor)other).hashCode());
    }
    return super.equals(other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of the object.
   ** <p>
   ** In general, the <code>toString</code> method returns a string that
   ** "textually represents" this object. The result is a concise but
   ** informative representation that is easy for a person to read.
   ** <p>
   ** The <code>toString</code> method for class <code>Accessor</code> returns a
   ** string consisting of the name of the class of which the object is an
   ** instance.
   **
   ** @return                    a string representation of the object.
   */
  @Override
  public String toString() {
    return name;
  }
}