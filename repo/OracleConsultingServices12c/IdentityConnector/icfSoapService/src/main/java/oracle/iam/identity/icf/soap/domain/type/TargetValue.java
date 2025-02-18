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

    File        :   TargetValue.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TargetValue.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.soap.domain.type;

import java.io.File;

import java.net.URI;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class TargetValue
// ~~~~~ ~~~~~~~~~~~
/**
 ** Stores metadata about a SOAP attribute value.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetValue extends TargetAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5981860599530620514")
  private static final long serialVersionUID = 5886294098476463256L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The collection of values of the attribute.
   */
  transient List<Object> value = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty SOAP <code>TargetValue</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TargetValue() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a SOAP <code>TargetValue</code> with the specified name.
   **
   ** @param  name               the value of the <code>TargetAttribute</code>
   **                            name.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public TargetValue(final TargetAttribute attribute) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    if (attribute != null) {
      this.name       = attribute.name;
      this.type       = attribute.type;
      this.nullable   = attribute.nullable;
      this.sensitive  = attribute.sensitive;
      this.identifier = attribute.identifier;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a {@link Boolean} value from the value collection.
   **
   ** @return                    {@link Boolean} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link Boolean}.
   */
  public Boolean booleanValue() {
    if (this.type == null || !"Boolean".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    Boolean value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (Boolean)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a {@link Integer} value from the value collection.
   **
   ** @return                    {@link Integer} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link Integer}.
   */
  public Integer integerValue() {
    if (this.type == null || !"Integer".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    Integer value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (Integer)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a {@link Long} value from the value collection.
   **
   ** @return                    {@link Long} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link Long}.
   */
  public Long longValue() {
    if (this.type == null || !"Long".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    Long value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (Long)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   floatValue
  /**
   ** Returns a {@link Float} value from the value collection.
   **
   ** @return                    {@link Float} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link Float}.
   */
  public Float floatValue() {
    if (this.type == null || !"Float".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    Float value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (Float)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Returns a {@link Double} value from the value collection.
   **
   ** @return                    {@link Double} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link Double}.
   */
  public Double doubleValue() {
    if (this.type == null || !"Double".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    Double value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (Double)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Returns a {@link Date} value from the value collection.
   **
   ** @return                    {@link Date} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link Date}.
   */
  public Date dateValue() {
    if (this.type == null || !"Date".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    Date value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (Date)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   characterValue
  /**
   ** Returns a {@link Character} value from the value collection.
   **
   ** @return                    {@link Character} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link Character}.
   */
  public Character characterValue() {
    if (this.type == null || !"Character".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    Character value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (Character)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a {@link String} value from the value collection.
   **
   ** @return                    {@link String} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link String}.
   */
  public String stringValue() {
    if (this.type == null || !"String".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    String value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (String)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fileValue
  /**
   ** Returns a {@link File} value from the value collection.
   **
   ** @return                    {@link File} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link File}.
   */
  public File fileValue() {
    if (this.type == null || !"File".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    File value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (File)this.value.iterator().next();
    }
    return value;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   uriValue
  /**
   ** Returns a {@link URI} value from the value collection.
   **
   ** @return                    {@link URI} value from the value
   **                            collection.
   **
   ** @throws ClassCastException if the object returned by the operation is not
   **                            castable to a {@link URI}.
   */
  public URI uriValue() {
    if (this.type == null || !"URI".equals(this.type))
      throw new IllegalArgumentException("Invalid type declaration");

    URI value;
    if (this.value == null || this.value.isEmpty() || this.value.iterator().next() == null) {
      value = null;
    }
    else {
      value = (URI)this.value.iterator().next();
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the collection of values that the SOAP <code>TargetValue</code>
   ** belongs to.
   **
   ** @param  value              the collection of values that the SOAP
   **                            <code>TargetValue</code> belongs to.
   **                            <br>
   **                            Allowed  object is {@link List} where each
   **                            element is of type {@link Object}.
   **
   ** @return                    the <code>TargetValue</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>TargetValue</code>.
   */
  public final TargetValue value(final List<Object> value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the list of roles that the user belongs to through direct
   ** membership.
   ** <p>
   ** This accessor method returns a reference to the live list, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the SOAP object.
   **
   ** @return                    the collection of values that the SOAP
   **                            <code>TargetValue</code> belongs to.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  public final List<Object> value() {
    return this.value;
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
  public final int hashCode() {
    int result = super.hashCode();
    result = PRIME * result + (this.value != null ? this.value.hashCode()  : 0);
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
    // ensure inheritance
    if (!super.equals(other))
      return false;

    final TargetValue that = (TargetValue)other;
    return !(this.value != null ? !this.value.equals(that.value) : that.value != null);
  }
}