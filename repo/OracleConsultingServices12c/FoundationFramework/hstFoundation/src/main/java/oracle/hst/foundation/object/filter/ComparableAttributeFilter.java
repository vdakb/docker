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

    File        :   ComparableAttributeFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ComparableAttributeFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.util.Map;

import oracle.hst.foundation.object.Entity;
import oracle.hst.foundation.object.Attribute;

////////////////////////////////////////////////////////////////////////////////
// abstract class ComparableAttributeFilter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Filter for an attribute value that is comparable.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public abstract class ComparableAttributeFilter<T> extends SingleValueAttributeFilter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ComparableAttributeFilter</code> w/ the
   ** {@link Attribute} to compare.
   **
   ** @param  attribute          the {@link Attribute} this
   **                            {@link SingleValueAttributeFilter} belongs to.
   */
  public ComparableAttributeFilter(final Attribute attribute) {
    // ensure inheritance
    super(attribute);

    // determine if this attribute value is comparable..
    if (!(value() instanceof Comparable))
      throw new IllegalArgumentException("Must be a comparable value!");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Call compareTo on the attribute values. If the attribute is not present in
   ** the {@link Entity} return -1.
   **
   ** @param  entity             the {@link Entity} containinf the attributes to
   **                            compare,
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as the attribute values are less than, equal
   **                            to, or greater than the specified object.
   */
  public int compare(final Entity entity) {
    int ret = -1;
    final Attribute attr = entity.get(name());
    if (attr != null && attr.value().size() == 1) {
      // it must be a comparable because that's were testing against
      if (!(attr.value().get(0) instanceof Comparable))
        throw new IllegalArgumentException("Attribute value " + name() + " must be comparable! Found" + attr.value().get(0).getClass());

      // grab this value and the on from the attribute an compare..
      @SuppressWarnings("unchecked")
      final Comparable t1 = (Comparable)attr.value().get(0);
      @SuppressWarnings("unchecked")
      final Comparable t2 = (Comparable)value();
      ret = t1.compareTo(t2);
    }
    return ret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Call compareTo on the attribute values. If the attribute is not present in
   ** the {@link Entity} return -1.
   **
   ** @param  entity             the attribute mapping containinf the attributes
   **                            to compare.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as the attribute values are less than, equal
   **                            to, or greater than the specified object.
   */
  public int compare(final Map<String, Object> entity) {
    int ret = -1;
    final Object attr = entity.get(name());
    if (attr != null) {
      // it must be a comparable because that's were testing against
      if (!(attr instanceof Comparable))
        throw new IllegalArgumentException("Attribute value must be comparable!");

      // grab this value and the on from the attribute an compare..
      @SuppressWarnings("unchecked")
      final Comparable t1 = (Comparable)attr;
      @SuppressWarnings("unchecked")
      final Comparable t2 = (Comparable)value();
      ret = t1.compareTo(t2);
    }
    return ret;
  }
}