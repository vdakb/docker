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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   Extractor.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Extractor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.reflect;

import java.lang.reflect.Method;

import java.lang.invoke.SerializedLambda;

import java.util.Arrays;
import java.util.Objects;

import java.util.function.Function;

import java.io.Serializable;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;

//////////////////////////////////////////////////////////////////////////////
// interface Extractor
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** Usage example:
 ** <pre>
 **   Map&lt;Extractor&lt;MyEntity&gt;, Object&gt; criteria = new HashMap&lt;&gt;();
 **   criteria.put(MyEntity::getName, Like.startsWith(searchNameStartsWith));
 **   criteria.put(MyEntity::getCreated, Order.greaterThanOrEqualTo(searchStartDate));
 **   criteria.put(MyEntity::getType, searchTypes);
 **   criteria.put(MyEntity::isDeleted, false);
 ** </pre>
 ** And then later on in "the backend":
 ** <pre>
 **   criteria.forEach((getter, value) -&gt; required.put(getter.propertyName(), value));
 ** </pre>
 ** This allows a type safe way of defining property names.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Requires Java 8u60 and later.
 **
 ** @param  <T>                  the generic base type.
 **                              <br>
 **                              Allowd object is <code>&lt;T&gt;</code>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Extractor<T> extends Function<T, Object>
                              ,       Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returnType
  /**
   ** Returns the extected type of the extracted value.
   **
   ** @return                    the extected type of the extracted value.
   **                            <br>
   **                            Possible object is {@link Class} of any type.
   */
  default Class<?> returnType() {
    return method().getReturnType();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyName
  /**
   ** Returns the name of the property to extract the value from.
   **
   ** @return                    the name of the property to extract the value
   **                            from.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  default String propertyName() {
    BeanInfo beanInfo;
    try {
      beanInfo = Introspector.getBeanInfo(baseType());
    }
    catch (IntrospectionException e) {
      throw new IllegalStateException(e);
    }
    final Method method = method();
    return Arrays.stream(beanInfo.getPropertyDescriptors()).filter(property -> property.getReadMethod() != null && Objects.equals(property.getReadMethod().getName(), method.getName())).findFirst().orElseThrow(IllegalStateException::new).getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Returns the {@link Method} responsible to extract the value from the
   ** property.
   **
   ** @return                    the {@link Method} responsible to extract the
   **                            value from the property.
   **                            <br>
   **                            Possible object is {@link Method}.
   */
  default Method method() {
    final String name = serialized().getImplMethodName();
    return Arrays.stream(baseType().getDeclaredMethods()).filter(method -> Objects.equals(method.getName(), name)).findFirst().orElseThrow(IllegalStateException::new);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   baseType
  /**
   ** Returns the implementation {@link Class} of the object instance to extract
   ** the value from.
   **
   ** @return                    the implementation {@link Class} of the object
   **                            instance to extract the value from.
   **                            <br>
   **                            Possible object is {@link Class} of type
   **                            <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  default Class<T> baseType() {
    final String name = serialized().getImplClass().replace("/", ".");
    try {
      return (Class<T>)Class.forName(name, true, Thread.currentThread().getContextClassLoader());
    }
    catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialized
  /**
   ** Serialize a this.
   **
   ** @return                    the serialized form of the given lambda.
   **                            <br>
   **                            Possible object is {@link SerializedLambda}.
   */
  default SerializedLambda serialized() {
    return serialized(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialized
  /**
   ** Serialize a {@link Serializable} lambda.
   **
   ** @param  lambda             the {@link Serializable} lambda to serialize.
   **                            <br>
   **                            Allowed object is {@link Serializable}.
   **
   ** @return                    the serialized form of the given lambda.
   **                            <br>
   **                            Possible object is {@link SerializedLambda}.
   */
  static SerializedLambda serialized(final Serializable lambda) {
    try {
      final Method replace = lambda.getClass() .getDeclaredMethod("writeReplace");
      // ensure that the field is accessible regardless which modifier the
      // field is assinged to
      replace.setAccessible(true);
      return (SerializedLambda)replace.invoke(lambda);
    }
    catch (Throwable T) {
      throw new UnsupportedOperationException("Reflection failed.", T);
    }
  }
}