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

    File        :   Attribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Attribute.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import java.math.BigDecimal;
import java.math.BigInteger;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class Attribute
// ~~~~~ ~~~~~~~~~
/**
 ** Represents a named collection of values within a target object, although the
 ** simplest case is a name-value pair (e.g., email, employeeID). Values can be
 ** empty, <code>null</code>, or set with various types. Empty and
 ** <code>null</code> are supported because it makes a difference on some
 ** resources (in particular database resources).
 ** <p>
 ** The precise meaning of an instance of <code>Attribute</code> depends on the
 ** context in which it occurs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Attribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Supported type for the attributes. */
  private static final Set<Class<?>> TYPES = new HashSet<Class<?>>();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    TYPES.add(String.class);
    TYPES.add(long.class);
    TYPES.add(Long.class);
    TYPES.add(char.class);
    TYPES.add(Character.class);
    TYPES.add(double.class);
    TYPES.add(Double.class);
    TYPES.add(float.class);
    TYPES.add(Float.class);
    TYPES.add(int.class);
    TYPES.add(Integer.class);
    TYPES.add(boolean.class);
    TYPES.add(Boolean.class);
    TYPES.add(byte.class);
    TYPES.add(Byte.class);
    TYPES.add(byte[].class);
    TYPES.add(BigDecimal.class);
    TYPES.add(BigInteger.class);
    TYPES.add(Map.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the <code>Attribute</code>. */
  private final String name;

  /** the values of the <code>Attribute</code>. */
  private List<Object> value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> with the specified name and values.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Attribute()" and enforces use of the public factory method
   ** below.
   **
   ** @param  name               the unique name of the attribute.
   ** @param  value              the  variable number of arguments that are used
   **                            as values for the attribute.
   */
  protected Attribute(final String name, final Object... value) {
    // ensure inheritance
    this(name, Arrays.asList(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> with the specified name and values.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Attribute()" and enforces use of the public factory method
   ** below.
   **
   ** @param  name               the unique name of the attribute.
   ** @param  value              the  variable number of arguments that are used
   **                            as values for the attribute.
   */
  protected Attribute(final String name, final Iterable<Object> value) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (StringUtility.isBlank(name))
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_IS_NULL, "name"));

    // initialize instance attributes
    this.name  = name;
    addInternal(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Attribute</code>.
   **
   ** @return                    the name of the <code>Attribute</code>.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the <code>Attribute</code>.
   **
   ** @return                    the value of the <code>Attribute</code>.
   */
  public List<Object> value() {
    return (this.value == null) ? null : CollectionUtility.unmodifiable(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   is
  /**
   ** Determines if the specified <code>name</code> matches this attribute's
   ** name.
   **
   ** @param  name               case insensitive string representation of the
   **                            attribute's name.
   **
   ** @return                    <code>true</code> if the case insentitive name
   **                            is equal to that of the one in
   **                            <code>Attribute</code>.
   */
  public boolean is(String name) {
    return StringUtility.isCaseInsensitiveEqual(this.name, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an attribute with the specified name and a
   ** <code>null</code> value.
   **
   ** @param  name               the unique name of the attribute.
   **
   ** @return                    an instance of <code>Attribute</code> with a
   **                            <code>null</code> value.
   */
  public static Attribute build(final String name) {
    return new Attribute(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an attribute with the specified name and the
   ** values provided.
   **
   ** @param  name               the unique name of the attribute.
   ** @param  value              the  variable number of arguments that are used
   **                            as values for the attribute.
   **
   ** @return                    an instance of <code>Attribute</code> with the
   **                            specified name and a value that includes the
   **                            arguments provided.
   */
  public static Attribute build(final String name, final Object... value) {
    return new Attribute(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to creates an attribute with the specified name and the
   ** values provided.
   **
   ** @param  name               the unique name of the attribute.
   ** @param  value              the  variable number of arguments that are used
   **                            as values for the attribute.
   **
   ** @return                    an instance of <code>Attribute</code> with the
   **                            specified name and a value that includes the
   **                            arguments provided.
   */
  public static Attribute build(final String name, final Collection<Object> value) {
    return new Attribute(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkAttributeValue
  /**
   ** Determines if the class of the object is a supported attribute type. If
   ** not it throws an {@link IllegalArgumentException}.
   **
   ** @param  value              the value to check or <code>null</code>.
   **
   ** @throws IllegalArgumentException if the class of the object is not a
   **                                  supported attribute type.
   */
  public static void checkAttributeValue(final Object value) {
    if (value != null) {
      checkAttributeValue((String)null, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toMap
  /**
   ** Transform a <code>Collection</code> of {@link Attribute} instances into a
   ** {@link Map}.
   ** <p>
   ** The key to each element in the map is the <i>name</i> of an
   ** <code>Attribute</code>. The value of each element in the map is the
   ** <code>Attribute</code> instance with that name.
   **
   ** @param  attributes         the set of attribute to transform to a map.
   **
   ** @return                    a map of string and attribute.
   **
   ** @throws NullPointerException if the parameter <b>attributes</b> is
   **                              <code>null</code>.
   */
  public static Map<String, Attribute> toMap(final Collection<? extends Attribute> attributes) {
    final Map<String, Attribute> ret = CollectionUtility.<Attribute>caseInsensitiveMap();
    for (Attribute cursor : attributes) {
      ret.put(cursor.name(), cursor);
    }
    return CollectionUtility.unmodifiable(ret);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkAttributeValue
  /**
   ** Find the {@link Attribute} of the given name in the {@link Set}.
   **
   ** @param  name               the {@link Attribute}'s name to search for.
   ** @param  attrs              the {@link Set} of {@link Attribute}'s to
   **                            search.
   **
   ** @return                    the {@link Attribute} with the specified name;
   **                            otherwise <code>null</code>.
   */
  public static Attribute find(final String name, final Set<Attribute> attrs) {
    // prevent bogus input
    if (StringUtility.isBlank(name))
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_IS_NULL, "name"));

    final Set<Attribute> set = CollectionUtility.notNull(attrs);
    for (Attribute cursor : set) {
      if (cursor.is(name)) {
        return cursor;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkAttributeValue
  /**
   ** Determines if the class of the object is a supported attribute type. If
   ** not it throws an {@link IllegalArgumentException}.
   **
   ** @param  name               the name of the attribute to check.
   ** @param  value              the value to check or <code>null</code>.
   **
   ** @throws IllegalArgumentException if the class of the object is not a
   **                                  supported attribute type.
   */
  public static void checkAttributeValue(final String name, final Object value) {
    if (value instanceof Map) {
      checkAttributeValue(new StringBuilder(name == null ? "?" : name), value);
    }
    else if (value != null) {
      if (name == null) {
        checkAttributeType(value.getClass());
      }
      else if (!supported(value.getClass()))
        throw new IllegalArgumentException(String.format("Attribute ''%s'' type ''%s'' is not supported.", name, value.getClass()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkAttributeType
  /**
   ** Determines if the class is a supported attribute type. If not it throws
   ** an {@link IllegalArgumentException}.
   ** <ul>
   **   <li>String.class
   **   <li>long.class
   **   <li>Long.class
   **   <li>char.class
   **   <li>Character.class
   **   <li>double.class
   **   <li>Double.class
   **   <li>float.class
   **   <li>Float.class
   **   <li>int.class
   **   <li>Integer.class
   **   <li>boolean.class
   **   <li>Boolean.class
   **   <li>byte.class
   **   <li>Byte.class
   **   <li>byte[].class
   **   <li>BigDecimal.class
   **   <li>BigInteger.class
   **   <li>Map.class</li>
   ** </ul>
   **
   ** @param  clazz              the type to check against the support list of
   **                            types.
   **
   ** @throws IllegalArgumentException if the type is not on the supported list.
   */
  public static void checkAttributeType(final Class<?> clazz) {
    if (!supported(clazz))
      throw new IllegalArgumentException(String.format("Attribute type ''%s'' is not supported.", clazz));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supported
  /**
   ** Determines if the class is a supported attribute type.
   **
   ** @param  clazz              the type to check against a supported list of
   **                            types.
   **
   ** @return                    <code>true</code> if the type is on the
   **                            supported list otherwise <code>false</code>.
   */
  public static boolean supported(final Class<?> clazz) {
    return TYPES.contains(clazz) || Map.class.isAssignableFrom(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   byteValue
  /**
   ** Returns the byte value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the byte value.
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
  public static Byte byteValue(final Attribute attribute) {
    return attribute.byteValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   byteArrayValue
  /**
   ** Returns the byte array value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the byte array value.
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
  public static Byte[] byteArrayValue(final Attribute attribute) {
    return attribute.byteArrayValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns the boolean value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the boolean value.
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
  public static Boolean booleanValue(final Attribute attribute) {
    return attribute.booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns the integer value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the integer value.
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
  public static Integer integerValue(final Attribute attribute) {
    return attribute.integerValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns the long value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the long value.
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
  public static Long longValue(final Attribute attribute) {
    return attribute.longValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   floatValue
  /**
   ** Returns the float value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the float value.
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
  public static Float floatValue(final Attribute attribute) {
    return attribute.floatValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Returns the double value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the double value.
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
  public static Double doubleValue(final Attribute attribute) {
    return attribute.doubleValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigDecimalValue
  /**
   ** Returns the big integer value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the big integer value.
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
  public static BigInteger bigIntegerValue(final Attribute attribute) {
    return attribute.bigIntegerValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigDecimalValue
  /**
   ** Returns the big decimal value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the big decimal value.
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
  public static BigDecimal bigDecimalValue(final Attribute attribute) {
    return attribute.bigDecimalValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Returns the date value from the specified (single-valued) attribute that
   ** contains a long.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the date value.
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
  public static Date dateValue(final Attribute attribute) {
    return attribute.dateValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   characterValue
  /**
   ** Returns the character value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the character value.
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
  public static Character characterValue(final Attribute attribute) {
    return attribute.characterValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns the string value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the string value.
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
  public static String stringValue(final Attribute attribute) {
    return attribute.stringValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asStringValue
  /**
   ** Returns the string value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the string value.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the string value
   **                            for the attribute.
   **
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public static String asStringValue(final Attribute attribute) {
    return attribute.asStringValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapValue
  /**
   ** Returns the map value from the specified (single-valued) attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the map value.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the map value for
   **                            the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  {@link Map}.
   **
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public static Map<String, Object> mapValue(final Attribute attribute) {
    return attribute.mapValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleValue
  /**
   ** Returns the <code>Object</code> value from the specified (single-valued)
   ** attribute.
   **
   ** @param  attribute          the {@link Attribute} from which to retrieve
   **                            the value.
   **
   ** @return                    <code>null</code> if the attribute's list of
   **                            values is <code>null</code> or empty.
   */
  public static Object singleValue(final Attribute attribute) {
    return attribute.singleValue();
  }

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
    return StringUtility.caseInsensitiveHash(this.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Attribute</code>s are considered equal if and only if they
   ** represent the same JSON text. As a consequence, two given
   ** <code>Attribute</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  object             the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object object) {
    // test identity
    if (this == object) {
      return true;
    }
    // test for null..
    if (object == null) {
      return false;
    }
    // test that the exact class matches
    if (!(getClass().equals(object.getClass()))) {
      return false;
    }
    // test name field..
    final Attribute other = (Attribute)object;
    if (!is(other.name)) {
      return false;
    }

    if (!CollectionUtility.equals(this.value, other.value)) {
      return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this <code>Attribute</code>.
   ** <p>
   ** The string representation consists of a list of the set's elements in the
   ** order they are returned by its iterator, enclosed in curly brackets
   ** (<code>"{}"</code>). Adjacent elements are separated by the characters
   ** <code>", "</code> (comma and space). Elements are converted to strings as
   ** by <code>Object.toString()</code>.
   **
   ** @return                    a string representation of this
   **                            <code>Attribute</code>.
   */
  @Override
  public String toString() {
    // poor man's consistent toString impl..
    final StringBuilder builder = new StringBuilder("{Attribute:{");
    builder.append("Name:").append("\"").append(name()).append("\"");
    builder.append(",Value:").append(value());
    return builder.append("}}").toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add each of the specified objects as a values for the attribute that is
   ** being built.
   **
   ** @param  value              the  variable number of arguments to add
   **                            as values for this <code>Attribute</code>.
   **
   ** @return                    this <code>Attribute</code> for chaining
   **                            invocations.
   **
   ** @throws NullPointerException if any of the values is <code>null</code>.
   */
  public Attribute add(final Object... value) {
    if (value != null)
      addInternal(Arrays.asList(value));

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add each of the specified objects as a values for the attribute that is
   ** being built.
   **
   ** @param  value              the  variable number of arguments to add
   **                            as values for this <code>Attribute</code>.
   **
   ** @return                    this <code>Attribute</code> for chaining
   **                            invocations.
   **
   ** @throws NullPointerException if any of the values is <code>null</code>.
   */
  public Attribute add(final Collection<Object> value) {
    addInternal(value);
    return this;
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
    final Object obj = singleValue();
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
    final Object obj = singleValue();
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
    final Object obj = singleValue();
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
    final Object obj = singleValue();
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
    final Object obj = singleValue();
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
    final Object obj = singleValue();
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
    Object obj = singleValue();
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
    final Object obj = singleValue();
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
    final Object obj = singleValue();
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
    final Long value = longValue();
    return value == null ? null : new Date(value.longValue());
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
    final Object obj = singleValue();
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
    final Object value = singleValue();
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
    final Object obj = singleValue();
    return obj == null ? null : obj.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapValue
  /**
   ** Returns the map value from the specified (single-valued) attribute.
   **
   ** @return                    <code>null</code> if the value is
   **                            <code>null</code> otherwise the map value for
   **                            the attribute.
   **
   ** @throws ClassCastException       if the object in the attribute is not a
   **                                  {@link Map}.
   **
   ** @throws IllegalArgumentException if the attribute is a multi-valued
   **                                  (rather than single-valued).
   */
  public Map<String, Object> mapValue() {
    final Object obj = singleValue();
    return obj == null ? null : (Map<String, Object>)obj;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleValue
  /**
   ** Returns the <code>Object</code> value from the specified (single-valued)
   ** attribute.
   **
   ** @return                    <code>null</code> if the attribute's list of
   **                            values is <code>null</code> or empty.
   */
  public Object singleValue() {
    Object ret = null;
    final List<Object> val = value();
    if (val != null && !val.isEmpty()) {
      // make sure this only called for single value..
      if (val.size() > 1)
        throw new IllegalArgumentException(String.format("Attribute ''%s'' is not single value attribute.", this.name));

      ret = val.get(0);
    }
    return ret;
  }

  @SuppressWarnings("unchecked")
  private static void checkAttributeValue(final StringBuilder name, final Object value) {
    if (value instanceof Map) {
      for (Map.Entry<Object, Object> entry : ((Map<Object, Object>)value).entrySet()) {
        final Object key = entry.getKey();
        final Object entryValue = entry.getValue();
        if (key instanceof String) {
          StringBuilder nameBuilder = new StringBuilder(name).append('/').append(key);
          if (entryValue instanceof Collection) {
            nameBuilder.append("[*]");
            for (Object item : ((Collection)entryValue)) {
              checkAttributeValue(nameBuilder, item);
            }
          }
          else {
            checkAttributeValue(nameBuilder, entryValue);
          }
        }
        else {
          throw new IllegalArgumentException(String.format("Map Attribute ''%s'' must have String key, type ''%s'' is not supported.", name, null != key ? key.getClass() : "null"));
        }
      }
    }
    else if (value != null) {
      if (!supported(value.getClass())) {
        throw new IllegalArgumentException(String.format("Attribute ''%s'' type ''%s'' is not supported.", name, value.getClass()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInternal
  /**
   ** Add each of the specified objects as a values for the attribute that is
   ** being built.
   **
   ** @param  value              the  variable number of arguments to add
   **                            as values for this <code>Attribute</code>.
   **
   ** @throws NullPointerException if any of the values is <code>null</code>.
   */
  private void addInternal(final Iterable<Object> value) {
    if (value != null) {
      // make sure the list is ready to receive values.
      if (this.value == null) {
        this.value = new ArrayList<Object>();
      }
      // add each value checking to make sure its correct
      for (Object cursor : value) {
        checkAttributeValue(this.name, cursor);
        this.value.add(cursor);
      }
    }
  }
}