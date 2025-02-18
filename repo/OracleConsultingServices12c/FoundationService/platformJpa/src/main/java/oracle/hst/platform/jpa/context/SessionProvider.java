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

    System      :   Foundation Shared Library
    System      :   Presistence Foundation Shared Library

    File        :   SessionProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SessionProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.context;

import java.util.Map;
import java.util.HashMap;

////////////////////////////////////////////////////////////////////////////////
// class SessionProvider
// ~~~~~ ~~~~~~~~~~~~~~~
public class SessionProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Thread Local Map */
  private static final ThreadLocal<Map<String, Object>> MAP = new ThreadLocal<Map<String, Object>>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SessionProvider</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private SessionProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Removes all mappings from the attribute map (optional operation).
   **
   ** @throws UnsupportedOperationException if clear is not supported by the
   **                                       attribute map.
   */
  public static void clear() {
    if (MAP.get() != null) {
      MAP.get().clear();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Returns the value to which the thread local map maps the specified key.
   ** Returns <code>null</code> if the map contains no mapping for this key. A
   ** return value of <code>null</code> does not <i>necessarily</i> indicate
   ** that the map contains no mapping for the key; it's also possible that the
   ** attribute map explicitly maps the key to <code>null</code>. The
   ** <code>containsKey</code> operation may be used to distinguish these two
   ** cases.
   ** <p>
   ** More formally, if the attribute map contains a mapping from a key
   ** <code>k</code> to a value <code>v</code> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <code>v</code>; otherwise it returns <code>null</code>.
   ** (There can be at most one such mapping.)
   **
   ** @param  key                key whose associated value is to be returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value to which the attribute map maps the
   **                            specified key, or <code>null</code> if the map
   **                            contains no mapping for this key.
   **                            <br>
   **                            Possible object is {@link Object}.
   **
   ** @throws ClassCastException   if the key is of an inappropriate type for
   **                              the attribute map (optional).
   ** @throws NullPointerException if the key is <code>null</code> and the
   **                              attribute map does not permit
   **                              <code>null</code> keys (optional).
   */
  public static Object get(final String key) {
    return MAP.get() == null ? null : MAP.get().get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put
  /**
   ** Associates the specified value with the specified key in the thread local
   ** map (optional operation). If the attribute map previously contained a
   ** mapping for the key, the old value is replaced by the specified value.
   ** (A map <code>m</code> is said to contain a mapping for a key
   ** <code>k</code> if and only if {@code #containsKey(Object) m.containsKey(k)}
   ** would return <code>true</code>.))
   **
   ** @param  key                key with which the specified value is to be
   **                            associated.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              value to be associated with the specified key.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    previous value associated with specified key,
   **                            or <code>null</code> if there was no mapping
   **                            for key. A <code>null</code> return can also
   **                            indicate that the map previously associated
   **                            <code>null</code> with the specified key, if
   **                            the implementation supports <code>null</code>
   **                            values.
   **
   ** @throws UnsupportedOperationException if the <code>put</code> operation is
   **                                        not supported by thattribute
   **                                       attribute map.
   ** @throws ClassCastException            if the class of the specified key or
   **                                       value prevents it from being stored
   **                                       in the attribute map.
   ** @throws IllegalArgumentException      if some aspect of this key or value
   **                                        prevents it from being stored in the
   **                                       attribute map.
   ** @throws NullPointerException          if the attribute map does not permit
   **                                       <code>null</code> keys or values,
   **                                       and the specified key or value is
   **                                       <code>null</code>.
   */
  public static Object put(final String key, final Object value) {
    // lazy initialize the map at the current thread
    if (MAP.get() == null)
      MAP.set(new HashMap<String, Object>());

    return MAP.get().put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes the mapping for this key from the thread local map if it is present
   ** (optional operation).
   ** <p>
   ** More formally, if the attribute map contains a mapping from key
   ** <code>k</code> to value <code>v</code> such that
   ** <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is
   ** removed. (The map can contain at most one such mapping.)
   ** <p>
   ** Returns the value to which the map previously associated the key, or
   ** <code>null</code> if the map contained no mapping for this key. (A
   ** <code>null</code> return can also indicate that the map previously
   ** associated <code>null</code> with the specified key if the implementation
   ** supports <code>null</code> values.) The map will not contain a mapping for
   ** the specified  key once the call returns.
   **
   ** @param  key                key whose mapping is to be removed from the
   **                            map.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return previous           value associated with specified key, or
   **                            <code>null</code> if there was no mapping for
   **                            key.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @throws ClassCastException            if the key is of an inappropriate
   **                                       type for the attribute map
   **                                       (optional).
   ** @throws NullPointerException          if the key is <code>null</code> and
   **                                       the attribute map does not permit
   **                                       <code>null</code> keys (optional).
   ** @throws UnsupportedOperationException if the <code>remove</code> method is
   **                                       not supported by the attribute map.
   */
  public static Object remove(final Object key) {
    return (MAP.get() == null) ? null : MAP.get().remove(key);
  }
}