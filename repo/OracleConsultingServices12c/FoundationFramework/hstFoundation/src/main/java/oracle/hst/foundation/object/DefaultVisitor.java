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

    File        :   DefaultVisitor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DefaultVisitor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object;

import java.util.List;

import oracle.hst.foundation.SystemLocale;
import oracle.hst.foundation.object.filter.Or;
import oracle.hst.foundation.object.filter.And;
import oracle.hst.foundation.object.filter.Not;
import oracle.hst.foundation.object.filter.Equals;
import oracle.hst.foundation.object.filter.Contains;
import oracle.hst.foundation.object.filter.EndsWith;
import oracle.hst.foundation.object.filter.LessThan;
import oracle.hst.foundation.object.filter.StartsWith;
import oracle.hst.foundation.object.filter.ContainsAll;
import oracle.hst.foundation.object.filter.GreaterThan;
import oracle.hst.foundation.object.filter.LessThanOrEqual;
import oracle.hst.foundation.object.filter.GreaterThanOrEqual;

import oracle.hst.foundation.object.filter.Presence;
import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class DefaultVisitor
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A DefaultVisitor can accept the {@link Entity}. It can be used for
 ** case-sensitive and case-ignore mode to accept the {@link java.lang.String}
 ** and {@link java.lang.Character} values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class DefaultVisitor implements FilterVisitor<DefaultVisitor.Result, Entity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final DefaultVisitor CASESENSITIVE = new DefaultVisitor(false);
  public static final DefaultVisitor CASEIGNORE    = new DefaultVisitor(true);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean caseIgnore = false;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Result
  // ~~~~~ ~~~~~~
  /**
   ** A logger that writes all to the console.
   */
  public static enum Result {
      FALSE
    , TRUE
    , UNDEFINED
    ;

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: valueOf
    static Result valueOf(final boolean value) {
      return value ? TRUE : FALSE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toBoolean
    boolean toBoolean() {
      return this == TRUE; // UNDEFINED collapses to FALSE.
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DefaultVisitor</code> with the specified case
   ** sensitivity.
   **
   ** @param  caseIgnore         ...
   */
  public DefaultVisitor(final boolean caseIgnore) {
    this.caseIgnore = caseIgnore;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and (FilterVisitor)
  /**
   ** Visits an <code>and</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always evaluate
   ** to <code>true</code>.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result and(final Entity entity, final And filter) {
    Result result = Result.TRUE;
    for (final Filter cursor : filter.filter()) {
      final Result r = cursor.accept(this, entity);
      if (r.ordinal() < result.ordinal()) {
        result = r;
      }
      if (result == Result.FALSE) {
        break;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or (FilterVisitor)
  /**
   ** Visits an <code>or</code> filter.
   * <p>
   * <b>Implementation note</b>: for the purposes of matching, an empty
   * sub-filters should always evaluate to <code>false</code>.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result or(final Entity entity, final Or filter) {
    Result result = Result.FALSE;
    for (final Filter subFilter : filter.filter()) {
      final Result r = subFilter.accept(this, entity);
      if (r.ordinal() > result.ordinal()) {
        result = r;
      }
      if (result == Result.TRUE) {
        break;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   not (FilterVisitor)
  /**
   ** Visits an <code>not</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result not(final Entity entity, final Not filter) {
    switch (filter.filter().accept(this, entity)) {
      case FALSE     : return Result.TRUE;
      case UNDEFINED : return Result.UNDEFINED;
      default        : return Result.FALSE;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   presence (FilterVisitor)
  /**
   ** Visits an <code>present</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result presence(final Entity entity, final Presence filter) {
    Result result = Result.FALSE;
    Attribute attribute = entity.get(filter.name());
    if (null != attribute) {
      Result.valueOf(null != entity.get(filter.name()));
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal (FilterVisitor)
  /**
   ** Visits an <code>equality</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result equal(final Entity entity, final Equals filter) {
    Result result = Result.UNDEFINED;
    Attribute attribute = entity.get(filter.name());
    if (null != attribute) {
      final List<Object> attributeValues = attribute.value();
      final List<Object> filterValues    = filter.attribute().value();
      result = Result.valueOf(CollectionUtility.equals(attributeValues, filterValues, this.caseIgnore));
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extendedFilter (FilterVisitor)
  /**
   ** Visits an <code>comparison</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result extendedFilter(final Entity entity, final Filter filter) {
    Result result = Result.UNDEFINED;
    if (filter instanceof Presence) {
      result = Result.valueOf(null != entity.get(((Presence)filter).name()));
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   greaterThan (FilterVisitor)
  /**
   ** Visits an <code>greater than</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result greaterThan(final Entity entity, final GreaterThan filter) {
    Result result = Result.UNDEFINED;
    final Object valueAssertion = expectSingleValue(entity, filter.name());
    if (null != valueAssertion) {
      if (!(valueAssertion instanceof Comparable)) {
        throw new IllegalArgumentException("Attribute value " + filter.name() + " must be comparable! Found" + valueAssertion.getClass());
      }
      final Object filterValue = filter.value();
      if (this.caseIgnore && filterValue instanceof String) {
        if (valueAssertion instanceof String) {
          result = Result.valueOf(((String)valueAssertion).compareToIgnoreCase((String)filterValue) > 0);
        }
      }
      else if (this.caseIgnore && filterValue instanceof Character) {
        if (valueAssertion instanceof Character) {
          result = Result.valueOf((Character.toLowerCase((Character)valueAssertion)) - (Character.toLowerCase((Character)filterValue)) > 0);
        }
      }
      else {
        result = Result.valueOf(forceCompare(valueAssertion, filterValue) > 0);
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   greaterThanOrEqual (FilterVisitor)
  /**
   ** Visits an <code>greater than or equal to</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result greaterThanOrEqual(final Entity entity, final GreaterThanOrEqual filter) {
    Result result = Result.UNDEFINED;
    final Object valueAssertion = expectSingleValue(entity, filter.name());
    if (null != valueAssertion) {
      if (!(valueAssertion instanceof Comparable)) {
        throw new IllegalArgumentException("Attribute value " + filter.name() + " must be comparable! Found" + valueAssertion.getClass());
      }
      final Object filterValue = filter.value();
      if (this.caseIgnore && filterValue instanceof String) {
        if (valueAssertion instanceof String) {
          result = Result.valueOf(((String)valueAssertion).compareToIgnoreCase((String)filterValue) >= 0);
        }
      }
      else if (this.caseIgnore && filterValue instanceof Character) {
        if (valueAssertion instanceof Character) {
          result = Result.valueOf((Character.toLowerCase((Character)valueAssertion)) - (Character.toLowerCase((Character)filterValue)) >= 0);
        }
      }
      else {
        result = Result.valueOf(forceCompare(valueAssertion, filterValue) >= 0);
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lessThan (FilterVisitor)
  /**
   ** Visits an <code>less than</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result lessThan(final Entity entity, final LessThan filter) {
    Result result = Result.UNDEFINED;
    final Object valueAssertion = expectSingleValue(entity, filter.name());
    if (null != valueAssertion) {
      if (!(valueAssertion instanceof Comparable)) {
        throw new IllegalArgumentException("Attribute value " + filter.name() + " must be comparable! Found" + valueAssertion.getClass());
      }
      final Object filterValue = filter.value();
      if (this.caseIgnore && filterValue instanceof String) {
        if (valueAssertion instanceof String) {
          result = Result.valueOf(((String)valueAssertion).compareToIgnoreCase((String)filterValue) < 0);
        }
      }
      else if (this.caseIgnore && filterValue instanceof Character) {
        if (valueAssertion instanceof Character) {
          result = Result.valueOf((Character.toLowerCase((Character)valueAssertion)) - (Character.toLowerCase((Character)filterValue)) < 0);
        }
      }
      else {
        result = Result.valueOf(forceCompare(valueAssertion, filterValue) < 0);
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lessThanOrEqual (FilterVisitor)
  /**
   ** Visits a <code>less than or equal to</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result lessThanOrEqual(final Entity entity, final LessThanOrEqual filter) {
    Result result = Result.UNDEFINED;
    final Object valueAssertion = expectSingleValue(entity, filter.name());
    if (null != valueAssertion) {
      if (!(valueAssertion instanceof Comparable)) {
        throw new IllegalArgumentException("Attribute value " + filter.name() + " must be comparable! Found" + valueAssertion.getClass());
      }
      final Object filterValue = filter.value();
      if (this.caseIgnore && filterValue instanceof String) {
        if (valueAssertion instanceof String) {
          result = Result.valueOf(((String)valueAssertion).compareToIgnoreCase((String)filterValue) <= 0);
        }
      }
      else if (this.caseIgnore && filterValue instanceof Character) {
        if (valueAssertion instanceof Character) {
          result = Result.valueOf((Character.toLowerCase((Character)valueAssertion)) - (Character.toLowerCase((Character)filterValue)) <= 0);
        }
      }
      else {
        result = Result.valueOf(forceCompare(valueAssertion, filterValue) <= 0);
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWith
  /**
   ** Visits a <code>starts with</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  public Result startsWith(final Entity entity, final StartsWith filter) {
    Result result = Result.UNDEFINED;
    String valueAssertion = expectSingleValue(entity, filter.name(), String.class);
    if (null != valueAssertion) {
      if (this.caseIgnore) {
        result = Result.valueOf(valueAssertion.toLowerCase(SystemLocale.get()).startsWith(filter.value().toLowerCase(SystemLocale.get())));
      }
      else {
        result = Result.valueOf(valueAssertion.startsWith(filter.value()));
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWith
  /**
   * Visits a <code>ends with</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  public Result endsWith(final Entity entity, final EndsWith filter) {
    Result result = Result.UNDEFINED;
    String valueAssertion = expectSingleValue(entity, filter.name(), String.class);
    if (null != valueAssertion) {
      if (this.caseIgnore) {
        result = Result.valueOf(valueAssertion.toLowerCase(SystemLocale.get()).endsWith(filter.value().toLowerCase(SystemLocale.get())));
      }
      else {
        result = Result.valueOf(valueAssertion.endsWith(filter.value()));
      }
    }
    return result;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains (FilterVisitor)
  /**
   ** Visits an <code>containsAll</code> filter.
   **
   ** @param  entity            a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  @Override
  public Result contains(final Entity entity, final Contains filter) {
    Result result = Result.UNDEFINED;
    String valueAssertion = expectSingleValue(entity, filter.name(), String.class);
    if (null != valueAssertion) {
      if (this.caseIgnore) {
        result = Result.valueOf(valueAssertion.toLowerCase(SystemLocale.get()).contains(filter.value().toLowerCase(SystemLocale.get())));
      }
      else {
        result = Result.valueOf(valueAssertion.contains(filter.value()));
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsAll (FilterVisitor)
  /**
   ** Visits an <code>containsAll</code> filter.
   **
   ** @param  entity         a visitor specified parameter.
   ** @param  filter            the visited filter.
   **
   ** @return                   a visitor specified result.
   */
  public Result containsAll(final Entity entity, final ContainsAll filter) {
    Result result = Result.UNDEFINED;
    Attribute attribute = entity.get(filter.name());
    List<Object> attributeValues = null;
    if (null != attribute && null != (attributeValues = attribute.value())) {
      final List<Object> filterValues = filter.attribute().value();
      if (filterValues.isEmpty()) {
        result = Result.TRUE;
      }
      else if (attributeValues.isEmpty()) {
        result = Result.FALSE;
      }
      else if (this.caseIgnore) {
        boolean stillContains = true;
        for (Object o : filter.attribute().value()) {
          boolean found = false;
          if (o instanceof String) {
            for (Object c : attributeValues) {
              if (c instanceof String && ((String)c).equalsIgnoreCase((String)o)) {
                found = true;
                break;
              }
            }
          }
          else if (o instanceof Character) {
            for (Object c : attributeValues) {
              if (c instanceof Character && Character.toUpperCase((Character)c) != Character.toUpperCase((Character)o)) {
                found = true;
                break;
              }
            }
          }
          else {
            result = Result.valueOf(attributeValues.containsAll(filter.attribute().value()));
            break;
          }
          if (!(stillContains = stillContains && found)) {
            break;
          }
        }
        result = Result.valueOf(stillContains);
      }
      else {
        result = Result.valueOf(attributeValues.containsAll(filterValues));
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceCompare
  /**
   ** Forces the compare of two comparable objects and removes any warnings
   ** generated by the compiler.
   **
   ** @param  <T>                the expected class type of the operation.
   ** @param  o1                 the left-hand-side object to compare.
   ** @param  o2                 the right-hand-side object to compare.
   **
   ** @return                    the integer value of o1.compareTo(o2).
   */
  public static <T> int forceCompare(final Object o1, final Object o2) {
    @SuppressWarnings("unchecked")
    final Comparable<T> t1 = (Comparable<T>)o1;
    @SuppressWarnings("unchecked")
    final T t2 = (T)o2;
    return t1.compareTo(t2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expectSingleValue
  protected <T> T expectSingleValue(final Entity entity, final String attributeName, final Class<T> expect) {
    T result = null;
    final Object o = expectSingleValue(entity, attributeName);
    if (null != o && expect.isAssignableFrom(o.getClass())) {
      result = expect.cast(o);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expectSingleValue
  protected Object expectSingleValue(final Entity entity, final String attributeName) {
    final Attribute attr = entity.get(attributeName);
    if (null != attr && null != attr.value() && attr.value().size() == 1) {
      return attr.value().get(0);
    }
    return null;
  }
}