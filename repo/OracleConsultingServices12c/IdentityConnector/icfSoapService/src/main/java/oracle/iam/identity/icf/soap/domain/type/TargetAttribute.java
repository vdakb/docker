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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SOAP Library

    File        :   TargetAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TargetAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.soap.domain.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class TargetAttribute
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Stores metadata about a SOAP attribute.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetAttribute extends AbstractTarget {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5359142176134521693")
  private static final long serialVersionUID = 3488435555448108979L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name of the attribute.
   */
  protected String   name      = null;

  /**
   ** The type of the attribute:
   ** <ul>
   **  <li>URI
   **  <li>File
   **  <li>Date
   **  <li>Long
   **  <li>Float
   **  <li>String
   **  <li>Double
   **  <li>Integer
   **  <li>Boolean
   **  <li>Character
   **  <li>GuardedString
   **  <li>GuardedByteArray
   ** </ul>
   */
  protected String   type      = "String";

  /**
   ** Specifies if the attribute is a key identifier.
   */
  protected boolean identifier = false;

  /**
   ** Specifies if the attribute is sensitive, means needs to be encrypted on
   ** transit.
   */
  protected boolean sensitive  = false;

  /**
   ** Specifies if the attribute is nullable.
   */
  protected boolean nullable   = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty SOAP <code>TargetAttribute</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TargetAttribute() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a SOAP <code>TargetAttribute</code> with the specified name.
   **
   ** @param  name               the value of the <code>TargetAttribute</code>
   **                            name.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public TargetAttribute(final String name) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a SOAP <code>TargetAttribute</code> with the specified name and
   ** type.
   **
   ** @param  name               the value of the <code>TargetAttribute</code>
   **                            name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the value of the <code>TargetAttribute</code>
   **                            type.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public TargetAttribute(final String name, final String type) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.name = name;
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the value of the <code>TargetAttribute</code> name.
   **
   ** @param  value              the value of the <code>TargetAttribute</code>
   **                            name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>TargetAttribute</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>TargetAttribute</code>.
   */
  public final TargetAttribute name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the value of the <code>TargetAttribute</code> name.
   **
   ** @return                    the value of the <code>TargetAttribute</code>
   **                            name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the value of the <code>TargetAttribute</code> type.
   **
   ** @param  value              the value of the <code>TargetAttribute</code>
   **                            type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>TargetAttribute</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>TargetAttribute</code>.
   */
  public final TargetAttribute type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the value of the <code>TargetAttribute</code> type.
   **
   ** @return                    the value of the <code>TargetAttribute</code>
   **                            type.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Sets the identifier flag of the <code>TargetAttribute</code>.
   **
   ** @param  value              the identifier flag of the
   **                            <code>TargetAttribute</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>TargetAttribute</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final TargetAttribute identifier(final boolean value) {
    this.identifier = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Returns the identifier flag of the <code>TargetAttribute</code>.
   **
   ** @return                    the identifier flag of the
   **                            <code>TargetAttribute</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean identifier() {
    return this.identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sensitive
  /**
   ** Sets the sensitive flag of the <code>TargetAttribute</code>.
   **
   ** @param  value              the sensitive flag of the
   **                            <code>TargetAttribute</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>TargetAttribute</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final TargetAttribute sensitive(final boolean value) {
    this.sensitive = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sensitive
  /**
   ** Returns the sensitive flag of the <code>TargetAttribute</code>.
   **
   ** @return                    the sensitive flag of the
   **                            <code>TargetAttribute</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean sensitive() {
    return this.sensitive;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullable
  /**
   ** Sets the nullable flag of the <code>TargetAttribute</code>.
   **
   ** @param  value              the nullable flag of the
   **                            <code>TargetAttribute</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>TargetAttribute</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final TargetAttribute nullable(final boolean value) {
    this.nullable = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullable
  /**
   ** Returns the nullable flag of the <code>TargetAttribute</code>.
   **
   ** @return                    the nullable flag of the
   **                            <code>TargetAttribute</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean nullable() {
    return this.nullable;
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
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.name != null ? this.name.hashCode() : 0;
    result = PRIME * result + (this.type  != null ? this.type.hashCode()  : 0);
    result = PRIME * result + (this.nullable   ? 1 : 0);
    result = PRIME * result + (this.sensitive  ? 1 : 0);
    result = PRIME * result + (this.identifier ? 1 : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>TargetAttribute</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>TargetAttribute</code>s may be different even though they contain
   ** the same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final TargetAttribute that = (TargetAttribute)other;
    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    return((this.sensitive  == that.sensitive) && (this.nullable  == that.nullable) &&  (this.identifier == that.identifier));
  }
}