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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AbstractAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractAttribute
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractAttribute</code> implements the base functionality
 ** for describing an attribute object in Oracle Identity Manager.
 ** <br>
 ** An object may have attriutes or not. Some of them are mandatory for
 ** the functionality.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AbstractAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final boolean OPTIONAL  = false;
  public static final boolean MANDATORY = true;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final boolean mandatory;
  private final String  id;

  private String        defaultValue;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractAttribute</code> which has a name and is
   ** optional for a specific task.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   */
  public AbstractAttribute(final String id) {
    // ensure inheritance
    this(id, OPTIONAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractAttribute</code> which has a name and a default
   ** value which this attribute provides in case it is not defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   ** @param  defaultValue       the default value for this attribute.
   **                            Allowed object {@link String}.
   */
  public AbstractAttribute(final String id, final String defaultValue) {
    // ensure inheritance
    this(id, OPTIONAL);

    // initialize instance attributes
    this.defaultValue = defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractAttribute</code> which has a name and is
   ** optional/mandatory according to the passed parameter.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   ** @param  mandatory          <code>true</code> the attribute must be
   **                            declared for the task; otherwise false.
   */
  public AbstractAttribute(final String id, final boolean mandatory) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (StringUtility.isEmpty(id))
      throw new IllegalArgumentException(TaskBundle.format(TaskError.ARGUMENT_IS_NULL, "id"));

    // initialize instance attributes
    this.id        = id;
    this.mandatory = mandatory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the label of this attribute.
   **
   ** @return                    the identifier of the attribute.
   **                            Possible object {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optional
  /**
   ** Returns <code>true</code> if this attribute is optional.
   **
   ** @return                    <code>true</code> if this attribute is
   **                            optional; otherwise <code>false</code>
   */
  public final boolean optional() {
    return !this.mandatory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mandatory
  /**
   ** Returns <code>true</code> if this attribute is mandatory.
   **
   ** @return                    <code>true</code> if this attribute is
   **                            mandatory; otherwise <code>false</code>
   */
  public final boolean mandatory() {
    return this.mandatory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultValue
  /**
   ** Returns the default value for this attribute.
   **
   ** @return                    the default value for this attribute.
   */
  public final String defaultValue() {
    return this.defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
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
    return this.id.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>AbstractAttribute</code> object that
   ** represents the same <code>id</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>AbstractAttribute</code> against.
   **
   ** @return                    <code>true</code> if the
   **                            <code>AbstractAttribute</code>s are
   **                            equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if ((other instanceof AbstractAttribute)) {
      final AbstractAttribute that = (AbstractAttribute)other;
      return StringUtility.isEqual(this.id, that.id);
    }
    return false;
  }
}