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

    File        :   StringFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StringFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.util.Map;

import oracle.hst.foundation.object.Entity;
import oracle.hst.foundation.object.Attribute;

////////////////////////////////////////////////////////////////////////////////
// abstract class StringFilter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Filter based on strings.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
abstract class StringFilter extends SingleValueAttributeFilter<String> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean caseIgnore = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>StringFilter</code> w/ the {@link Attribute} to
   ** compare.
   **
   ** @param  attribute          the {@link Attribute} this
   **                            {@link SingleValueAttributeFilter} belongs to.
   ** @param  caseIgnore         advice that the comparison of the string value
   **                            ignores case sensitivity.
   */
  public StringFilter(final Attribute attribute, final boolean caseIgnore) {
    // ensure inheritance
    super(attribute);

    // determine if this attribute value is a string.
    if (!(super.value() instanceof String))
      throw new IllegalArgumentException("Value must be a string!");

    // initialize instance attributes.
    this.caseIgnore = caseIgnore;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the <code>Attribute</code>.
   **
   ** @return                    the value of the <code>Attribute</code>.
   */
  @Override
  public String value() {
    return super.value();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Determines whether the specified {@link Map} contains an attribute that
   ** has the same name and contains a value that is less than or equals the
   ** value of the attribute that <code>FilterBuilder</code> placed into this
   ** filter.
   **
   ** @param  entity             the specified {@link Map}.
   **
   ** @return                    <code>true</code> if the entity matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final Map<String, Object> entity) {
    boolean ret = false;
    final String value = (String)entity.get(name());
    if (value != null) {
      ret = accept(value);
    }
    return ret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Determines whether the specified {@link Entity} contains an attribute that
   ** has the same name and contains a value that is less than or equals the
   ** value of the attribute that <code>FilterBuilder</code> placed into this
   ** filter.
   **
   ** @param  entity             the specified {@link Entity}.
   **
   ** @return                    <code>true</code> if the entity matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final Entity entity) {
    boolean ret = false;
    final Attribute that = entity.get(name());
    if (that != null) {
      ret = accept((String)that.value().get(0));
    }
    return ret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept
  /**
   ** Determines whether the specified <code>value</code> matches this filter.
   **
   ** @param  value              the value to match.
   **
   ** @return                    <code>true</code> if the value matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  public abstract boolean accept(final String value);
}