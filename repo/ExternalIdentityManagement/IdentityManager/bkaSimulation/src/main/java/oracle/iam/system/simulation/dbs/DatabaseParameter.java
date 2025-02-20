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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   DatabaseParameter.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseParameter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.util.Date;

import java.math.BigDecimal;
import java.math.BigInteger;

import oracle.jdbc.OracleTypes;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseParameter
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The database parameter value holder.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseParameter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final int    type;
  private final Object value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** The database parameter is a pair of value and its type.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new DatabaseParameter()" and enforces use of the public factory method
   ** below.
   **
   ** @param  value              the value of the parameter.
   */
  private DatabaseParameter(final Object value) {
    // ensure inheritance
    this(value, OracleTypes.NULL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** The bind parameter is a pair of value and its sqlType
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new DatabaseParameter()" and enforces use of the public factory method
   ** below.
   **
   ** @param  value              the value of the parameter.
   ** @param  type               the type of the parameter.
   */
  private DatabaseParameter(final Object value, final int type) {
    // ensure inheritance
    super();

    this.value = value;
    this.type  = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the bind parameter value.
   **
   ** @return                    the bind parameter value.
   */
  public Object value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the bind parameter type.
   **
   ** @return                    the bind parameter type.
   */
  public int type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   byteValue
  /**
   ** Returns the byte value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the byte value for
   **                            the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  byte.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Byte byteValue() {
    final Object obj = value();
    return obj == null ? null : (Byte)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   byteArrayValue
  /**
   ** Returns the byte array value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the byte array
   **                            value for the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  byte array.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Byte[] byteArrayValue() {
    final Object obj = value();
    if (obj instanceof byte[]) {
      Byte[] copy = new Byte[((byte[])obj).length];
      for (int idx = 0; idx < ((byte[])obj).length; ++idx) {
        copy[idx] = ((byte[])obj)[idx];
      }
      return copy;
    }
    else {
      return obj == null ? null : (Byte[])obj;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns the boolean value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the boolean value
   **                            for the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  {@link Boolean}.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Boolean booleanValue() {
    final Object obj = value();
    return obj == null ? null : (Boolean)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns the integer value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the integer value
   **                            for the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  integer.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Integer integerValue() {
    final Object obj = value();
    return obj == null ? null : (Integer)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns the long value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the long value for
   **                            the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  long.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Long longValue() {
    final Object obj = value();
    if (obj instanceof Date)
      return Long.valueOf(((Date)obj).getTime());
    else
      return obj == null ? null : (Long)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   floatValue
  /**
   ** Returns the float value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the float value for
   **                            the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  float.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Float floatValue() {
    final Object obj = value();
    return obj == null ? null : (Float)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Returns the double value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the double value
   **                            for the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  double.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Double doubleValue() {
    Object obj = value();
    return obj != null ? (Double)obj : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigDecimalValue
  /**
   ** Returns the big integer value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the big integer
   **                            value for the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  big integer.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public BigInteger bigIntegerValue() {
    final Object obj = value();
    return obj == null ? null : (BigInteger)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigDecimalValue
  /**
   ** Returns the big decimal value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the big decimal
   **                            value for the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  big decimal.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public BigDecimal bigDecimalValue() {
    final Object obj = value();
    return obj == null ? null : (BigDecimal)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Returns the date value from the specified (single-valued) attribute that
   ** contains a long.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the date value for
   **                            the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  long.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Date dateValue() {
    final Object obj = value();
    return obj == null ? null : (Date)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   characterValue
  /**
   ** Returns the character value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the character value
   **                            for the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  character.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Character characterValue() {
    final Object obj = value();
    return obj == null ? null : (Character)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns the string value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the string value
   **                            for the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  string.
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public String stringValue() {
    final Object value = value();
    return value == null ? null : (String)value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asStringValue
  /**
   ** Returns the string value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the string value
   **                            for the attribute.
   **
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public String asStringValue() {
    final Object obj = value();
    return obj == null ? null : obj.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final Object value) {
    if ((value instanceof Boolean))
      return build((Boolean)value);
    else if ((value instanceof Character))
      return build((Character)value);
    else if ((value instanceof Integer))
      return build((Integer)value);
    else if ((value instanceof Long))
      return build((Long)value);
    else if ((value instanceof Double))
      return build((Double)value);
    else if ((value instanceof Float))
      return build((Float)value);
    else if ((value instanceof String))
      return build((String)value);
    else
      return build((String)value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final Boolean value) {
    return build(value, OracleTypes.BOOLEAN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final Character value) {
    return build(value, OracleTypes.CHAR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final Integer value) {
    return build(value, OracleTypes.INTEGER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final Long value) {
    return build(value, OracleTypes.NUMERIC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final Double value) {
    return build(value, OracleTypes.DOUBLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final Float value) {
    return build(value, OracleTypes.FLOAT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final Date value) {
    return build(value, OracleTypes.DATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value.
   **
   ** @param  value              the value of the parameter.
   **
   ** @return                    an instance of <code>DatabaseParameter</code>
   **                            with the value provided.
   */
  public static DatabaseParameter build(final String value) {
    return build(value, OracleTypes.VARCHAR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a parameter with the specified value with the
   ** dedicated type.
   **
   ** @param  value              the value of the parameter.
   ** @param  type               the type of the parameter.
   **
   ** @return                    an instance of <code>Attribute</code> with the
   **                            specified name and a value that includes the
   **                            arguments provided.
   */
  public static DatabaseParameter build(final Object value, final int type) {
    return new DatabaseParameter(value, type);
  }
}