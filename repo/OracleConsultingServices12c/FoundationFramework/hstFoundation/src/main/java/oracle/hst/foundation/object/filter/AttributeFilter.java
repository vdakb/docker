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

    File        :   AttributeFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.util.Map;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.object.Filter;
import oracle.hst.foundation.object.Entity;
import oracle.hst.foundation.object.Attribute;

import oracle.hst.foundation.resource.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AttributeFilter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
abstract class AttributeFilter<T> implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the {@link Attribute} this {@link Filter} belongs to. */
  private final Attribute attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeFilter</code> which belongs to the specified
   ** {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} this {@link Filter}
   **                            belongs to.
   */
  public AttributeFilter(final Attribute attribute) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (attribute == null)
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_IS_NULL, "attribute"));

    // initialize instance attributes
    this.attribute = attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link Attribute} this {@link Filter} belongs to.
   **
   ** @return                    the {@link Attribute} this {@link Filter}
   **                            belongs to.
   */
  public Attribute attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the {@link Attribute} this {@link Filter} belongs to.
   **
   ** @return                    the name of the {@link Attribute} this
   **                            {@link Filter} belongs to.
   */
  public String name() {
    return this.attribute.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this {@link StringFilter}.
   ** <p>
   ** The string representation consists of a list of the filter's elements in
   ** the order they are returned by its iterator, enclosed in curly brackets
   ** (<code>"{}"</code>). Adjacent elements are separated by the characters
   ** <code>", "</code> (comma and space). Elements are converted to strings as
   ** by <code>Object.toString()</code>.
   **
   ** @return                    a string representation of this {@link Filter}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("{");
    builder.append(tag()).append(":").append(attribute().toString());
    return builder.append("}").toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   present
  /**
   ** Determines if the attribute provided is present in the {@link Entity}.
   **
   ** @param  entity             the {@link Entity} to test for presence of the
   **                            {@link Attribute} this {@link Filter} belongs
   **                            to.
   **
   ** @return                    <code>true</code> if the {@link Attribute} this
   **                            {@link Filter} belongs to is present in the
   **                            specified {@link Entity}; otherwise
   **                            <code>false</code>.
   */
  public boolean present(final Entity entity) {
    return entity.get(name()) != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   present
  /**
   ** Determines if the attribute provided is present in the {@link Entity}.
   **
   ** @param  entity             the {@link Entity} to test for presence of the
   **                            {@link Attribute} this {@link Filter} belongs
   **                            to.
   **
   ** @return                    <code>true</code> if the {@link Attribute} this
   **                            {@link Filter} belongs to is present in the
   **                            specified {@link Entity}; otherwise
   **                            <code>false</code>.
   */
  public boolean present(final Map<String, Object> entity) {
    return entity.get(name()) != null;
  }
}