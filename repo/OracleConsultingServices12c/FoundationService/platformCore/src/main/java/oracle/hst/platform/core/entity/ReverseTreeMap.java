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

    File        :   ReverseTreeMap.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReverseTreeMap.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.util.Comparator;
import java.util.TreeMap;

////////////////////////////////////////////////////////////////////////////////
// class ReverseTreeMap
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A tree hash map with a revers ordering.
 **
 ** @param  <K>                  the type of the key implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              extending this class (loggables can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 ** @param  <V>                  the type of the value implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              extending this class (loggables can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 ** @see    TreeMap
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class ReverseTreeMap<K extends Comparable<? super K>, V> extends TreeMap<K, V> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6767964717868040485")
  private static final long serialVersionUID = -1775713433116634126L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new, empty tree map, using the reverse natural ordering of
   ** its keys.  All keys inserted into the map must implement the
   ** {@link Comparable} interface. Furthermore, all such keys must be
   ** <em>mutually comparable</em>: {@code k1.compareTo(k2)} must not throw a
   ** <code>ClassCastException</code> for any keys <code>k1</code> and
   ** <code>k2</code> in the map. If the user attempts to put a key into the
   ** map that violates this constraint (for example, the user attempts to put a
   ** string key into a map whose keys are integers), the
   ** <code>put(Object key, Object value)</code> call will throw a
   ** <code>ClassCastException</code>.
   */
  public ReverseTreeMap() {
    // ensure inheritance
    super(Comparator.reverseOrder());
  }
}
