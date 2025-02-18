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
    Subsystem   :   Connector Bundle Integration

    File        :   AttributeFactory.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.resource.SystemBundle;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

////////////////////////////////////////////////////////////////////////////////
// abstract class AttributeFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Simplifies constructing instances of {@link Attribute}.
 ** <p>
 ** The interface <code>Attribute</code> is not need to implement. The factory
 ** returns an instance of an implementation of {@link Attribute} that overrides
 ** the methods <code>equals()</code>, <code>hashcode()</code> and
 ** <code>toString()</code> to provide a uniform and robust class. This
 ** implementation of an {@link Attribute} is backed by an
 ** <code>ArrayList</code> that contains the values and preserves the order of
 ** those values (in case the order of values is significant to the Service
 ** Provider endpoint.
 ** <p>
 ** The factory use internally the {@link AttributeBuilder} functionality to
 ** create the desired {@link Attribute}s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AttributeFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AttributeFactory</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new AttributeFactory()" and enforces use of the public method below.
   */
  private AttributeFactory() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Factory method to create a {@link Uid} attribute.
   ** <br>
   ** The value of the created instance is <code>null</code>.
   **
   ** @return                    an empty a {@link Uid} attribute.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public static Attribute uid() {
    return AttributeBuilder.build(Uid.NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Factory method to create a {@link Uid} attribute.
   ** <br>
   ** The value of the created instance is <code>value</code>.
   **
   ** @param  value              the single argument that is used as value for
   **                            the {@link Uid} attribute to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link Uid} attribute with the value
   **                            specified.
   **                            <br>
   **                            Possible object is {@link Uid}.
   */
  public static Uid uid(final String value) {
    return new Uid(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Factory method to create a {@link Name} attribute.
   ** <br>
   ** The value of the created instance is <code>null</code>.
   **
   ** @return                    an empty a {@link Name} attribute.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public static Attribute name() {
    return AttributeBuilder.build(Name.NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Factory method to create a {@link Name} attribute.
   ** <br>
   ** The value of the created instance is <code>value</code>.
   **
   ** @param  value              the single argument that is used as value for
   **                            the {@link Name} attribute to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link Name} attribute with the value
   **                            specified.
   **                            <br>
   **                            Possible object is {@link Name}.
   */
  public static Name name(final String value) {
    return new Name(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty attribute with the specified name.
   ** <br>
   ** The value of the created instance is <code>null</code>.
   **
   ** @param  name               the unique name of the attribute to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Attribute} with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public static Attribute build(final String name) {
    // prevent bogus input
    if (StringUtility.isBlank(name))
      throw new IllegalArgumentException(SystemBundle.stringFormat(SystemError.ARGUMENT_IS_NULL, "name"));

    return AttributeBuilder.build(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an attribute with the specified name and value.
   **
   ** @param  <T>                the Java type of the attribute values.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  name               the unique name of the attribute to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the variable number of arguments that are used
   **                            as values for the attribute to create.
   **                            <br>
   **                            Allowed object is array of <code>T</code>.
   **
   ** @return                    an instance of {@link Attribute} with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  @SuppressWarnings("unchecked")
  public static <T> Attribute build(final String name, final T... value) {
    // prevent bogus input
    if (StringUtility.isBlank(name))
      throw new IllegalArgumentException(SystemBundle.stringFormat(SystemError.ARGUMENT_IS_NULL, "name"));

    return AttributeBuilder.build(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an attribute with the specified name and
   ** collection of value.
   **
   ** @param  <T>                the Java type of the attribute values.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  name               the unique name of the attribute to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the collection used as values for the attribute
   **                            to create.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    an instance of {@link Attribute} with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public static <T>  Attribute build(final String name, final Collection<T> value) {
    // prevent bogus input
    if (StringUtility.isBlank(name))
      throw new IllegalArgumentException(SystemBundle.stringFormat(SystemError.ARGUMENT_IS_NULL, "name"));

    return AttributeBuilder.build(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an attribute with the specified name and
   ** collection of value probided by an {@link Iterator}.
   **
   ** @param  <T>                the type of the {@link Iterator} to convert.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  name               the unique name of the attribute to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  iterator           the {@link Iterator} where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Iterator} returning
   **                            elements of type <code>T</code>.
   **
   ** @return                    the {@link Attribute} with the values obtained
   **                            from the specified {@link Iterator}.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public static <T>  Attribute build(final String name, final Iterator<T> iterator) {
    // prevent bogus input
    if (StringUtility.isBlank(name))
      throw new IllegalArgumentException(SystemBundle.stringFormat(SystemError.ARGUMENT_IS_NULL, "name"));

    return AttributeBuilder.build(name, iterator);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Factory method to create a modifiable {@link Set} of {@link Attribute}s
   ** with the elements obtained from the specified array. The order of the
   ** elemants remains as they are provided by the elements in the specified
   ** array.
   **
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified typed array.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   */
  public static Set<Attribute> set(final String[] array) {
    final Set<Attribute> set = new LinkedHashSet<Attribute>();
    for (int i = 0; array != null && i < array.length; i+=2) {
      set.add(AttributeBuilder.build(array[i], array[i + 1]));
    }
    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Factory method to create a modifiable {@link Set} of {@link Attribute}s
   ** with the elements obtained from the specified array. The order of the
   ** elemants remains as they are provided by the elements in the specified
   ** array.
   **
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified typed array.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   */
  public static Set<Attribute> set(final String[][] array) {
    final Set<Attribute> set = new LinkedHashSet<Attribute>();
    for (int i = 0; array != null && i < array.length; i++) {
      set.add(AttributeBuilder.build(array[i][0], array[i][1]));
    }
    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Factory method to create a modifiable {@link Set} of {@link Attribute}s
   ** with the elements obtained from the specified arrays.
   ** <p>
   ** The order is important here because each key will map to one value.
   **
   ** @param  name               the array providing the name values of
   **                            the created {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  value              the array providing the values mapped to
   **                            the names of the created {@link Set} of
   **                            {@link Attribute}s.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the {@link Set} of {@link Attribute}s with the
   **                            elements obtained from the specified arrays.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   */
  public static Set<Attribute> set(final String[] name, final Object[] value) {
    // throw if there's invalid input..
    if (name.length != value.length)
      throw new IllegalArgumentException();

    final Set<Attribute> set = new LinkedHashSet<Attribute>();
    for (int i = 0; i < name.length; i++)
      set.add(AttributeBuilder.build(name[i], value[i]));

    return set;
  }
}