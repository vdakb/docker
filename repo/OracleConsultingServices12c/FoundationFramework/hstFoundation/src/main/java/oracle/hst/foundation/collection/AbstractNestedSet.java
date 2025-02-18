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

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Collection Utility

    File        :   AbstractNestedSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractNestedSet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-21-03  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.io.Serializable;

import java.util.Iterator;
import java.util.Set;
import java.util.AbstractSet;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractNestedSet
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** This class provides a skeletal implementation of the <code>SetOfSets</code>
 ** interface to minimize the effort required to implement this interface.
 **
 ** @see Set
 ** @see AbstractSet
 ** @see NestedSet
 */
public abstract class AbstractNestedSet<E> extends    AbstractSet<E>
                                           implements NestedSet<E>
                                           ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8447894187431485018")
  private static final long serialVersionUID = 1428578709299987531L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default constructor.
   */
  public AbstractNestedSet() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsAtom (NestedSet)
  /**
   ** Returns <code>true</code> if this nested set contains a set in which the
   ** specified element is present.
   **
   ** @param  element            the element whose presence in any elementary
   **                            set within this nested set is tested for.
   **
   ** @return                    <code>true</code> if any set within this nested
   **                            set contains the specified element,
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean containsAtom(final E element) {
    for (Iterator<E> i = this.iterator(); i.hasNext(); ) {
      if (((Set)i.next()).contains(element))
        return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns the hash code value for this nested set. To get the hash code of
   ** this nested set, new hash code values for every element of this nested set
   ** are calculated from a polynomial of 3rd order and finally summed up.
   ** <p>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two nested set
   ** <code>s1</code> and <code>s2</code>, as required by the general contract
   ** of <code>Object.hashCode()</code>.
   **
   ** @return                    the hash code value for this nested set.
   */
  @Override
  public int hashCode() {
    int hash = -1;
    for (Iterator<E> i = this.iterator(); i.hasNext(); ) {
      final int elementHash = i.next().hashCode();
      hash += -1 + (3 + elementHash) * (7 + elementHash) * (11 + elementHash);
    }
    return hash;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this nested set. The string
   ** representation consists of a list of the set's elementary sets in the
   ** order they are returned by its iterator, enclosed in curly brackets
   ** (<code>"{}"</code>). Adjacent sets are separated by the characters
   ** <code>", "</code> (comma and space). Elementary sets are converted to
   ** strings as by <code>Object.toString()</code>.
   **
   ** @return                    a string representation of this nested set.
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    output.append("{");
    for (Iterator<E> i = this.iterator(); i.hasNext(); ) {
      output.append(i.next().toString());
      if (i.hasNext())
        output.append(", ");
    }
    output.append("}");
    return output.toString();
  }
}