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
    Subsystem   :   Connector Bundle Framework

    File        :   DescriptorTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DescriptorTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;

import java.io.File;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

import java.sql.Timestamp;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.common.security.GuardedByteArray;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.connectors.icfcommon.exceptions.IntegrationException;

import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class DescriptorTransformer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DescriptorTransformer</code> implements the base functionality of
 ** transform attribute mappings from Identity Manager notations to Identity
 ** Connector friendly notation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DescriptorTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String SPLIT                     = "\\s*,\\s*";
  private static final int    JAVA_UTIL_DATE_LENGTH     = 28;
  private static final int    JAVA_SQL_DATE_LENGTH      = 10;
  private static final int    JAVA_SQL_TIMESTAMP_LENGTH = 21;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DescriptorTransformer</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new DescriptorTransformer()" and enforces use of the public method below.
   */
  private DescriptorTransformer() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClass
  /**
   ** Type to {@link ObjectClass} conversion, two predefined conversions:
   ** <ol>
   **   <li> Account  --&lt; __ACCOUNT__
   **   <li>Group     --&lt; __GROUP__
   ** </ol>
   ** All other provided string wrapped as it is in an {@link ObjectClass}.
   **
   ** @param  type               the object type in Identity Manager
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ObjectClass} belonging to Identity
   **                            Connector.
   **                            <br>
   **                            Possible object is {@link ObjectClass}.
   **
   ** @throws IllegalArgumentException if provided parameter is null or empty
   */
  public static ObjectClass objectClass(final String type) {
    if (ObjectClass.ACCOUNT_NAME.equalsIgnoreCase(type)) {
      return ObjectClass.ACCOUNT;
    }
    else if (ObjectClass.GROUP_NAME.equalsIgnoreCase(type)) {
      return ObjectClass.GROUP;
    }
    else {
      return new ObjectClass(specialName(type));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specialName
  /**
   ** Factory method to create a special name used by the connector framework.
   ** <p>
   ** A special name is enclosed in <code>__</code> (two underscore charcater).
   ** @param  name               a string as the base of the special name to
   **                            build.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the passed in name enclosed with
   **                            <code>__</code> (two underscore charcater).
   **                            Possible object {@link String}.
   */
  public static String specialName(final String name) {
    if (StringUtility.isBlank(name))
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_BAD_VALUE, "name"));

    final StringBuilder builder = new StringBuilder();
    return builder.append("__").append(name.toUpperCase()).append("__").toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertValueType
  /**
   ** @param  <T>                the expected class type of the returned
   **                            object.
   **                            <br>
   **                            Allowed object is  <code>&lt;T&gt;</code>.
   ** @param  original           the original value to convert.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the class of the value to create.
   **                            Typically it will be of the sort:
   **                            <code>String.class</code>.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the converted value.
   **                            <br>
   **                            Possible object <code>T</code>.
   */
  @SuppressWarnings({"unchecked","cast"}) 
  public static <T> T convertValueType(final Object original, final Class<T> type) {
    Object result = null;
    if (original == null) {
      result = null;
    }
    final String temp = original.toString();
    if ((type == java.sql.Timestamp.class) || (type == java.util.Date.class) || (type == java.sql.Date.class)) {
      result = timestampValue(temp);
    }
    else if (type == String.class) {
      result = temp;
    }
    else if (type == java.io.File.class) {
      result = new java.io.File(temp);
    }
    else if (type == java.math.BigInteger.class) {
      result = new java.math.BigInteger(temp);
    }
    else if (type == java.math.BigDecimal.class) {
      result = new java.math.BigDecimal(temp);
    }
    else if (type == byte[].class) {
      result = temp.getBytes();
    }
    else if (type == boolean.class || type == Boolean.class) {
      result = Boolean.valueOf(parseBoolean(temp));
    }
    else if (type == int.class || type == Integer.class) {
      result = StringUtility.isBlank(temp) ? null : Integer.valueOf(temp);
    }
    else if ((type == long.class || type == Long.class)) {
      result = Long.valueOf(temp);
    }
    else if ((type == float.class || type == Float.class)) {
      result = Float.valueOf(temp);
    }
    else if (type == Double.class) {
      result = Double.valueOf(temp);
    }
    else if (type == Double.class) {
      result = Double.valueOf(temp);
    }
    else if (type == Character.class || type == char.class) {
      char[] charArray = temp.toCharArray();
      result = Character.valueOf(charArray[0]);
    }
    else if (type == char[].class) {
      result = temp.toCharArray();
    }
    else if (type == java.lang.Character[].class) {
      char[] charArray = temp.toCharArray();
      Character[] characterArray = new Character[charArray.length];
      for (int i = 0; i < charArray.length; i++) {
        characterArray[i] = Character.valueOf(charArray[i]);
      }
      result = characterArray;
    }
    else if (type == URL.class) {
      try {
        result = new URI(temp);
      }
      catch (URISyntaxException e) {
        throw new IntegrationException(e);
      }
    }
    else if (type == URL.class) {
      if (StringUtility.isEmpty(temp))
        return null;
      try {
        result = new URL(temp);
      }
      catch (MalformedURLException e) {
        throw new IntegrationException(e);
      }
    }
    else if (type == GuardedByteArray.class) {
      result = new GuardedByteArray(temp.getBytes());
    }
    else if (type == GuardedString.class) {
      result = new GuardedString(temp.toCharArray());
    }
    else if (String[].class.isAssignableFrom(type)
          || GuardedString[].class.isAssignableFrom(type)
          || GuardedByteArray[].class.isAssignableFrom(type)
          || URI[].class.isAssignableFrom(type)
          || File[].class.isAssignableFrom(type)
          ) {
      result = convertValueType(arrayStringValue(temp), type.getComponentType());
    }
    else if (java.lang.Object.class.isAssignableFrom(type)) {
      result = convertValueType(temp.split(SPLIT), type.getComponentType());
    }
    else {
      throw new IllegalArgumentException(new StringBuilder().append("Invalid target type: ").append(type.getName()).toString());
    }
    return (T)result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseBoolean
  /**
   ** Returns a <code>boolean</code> for the specified string value.
   **
   ** @param  value              the string value to convert.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the converted value for the specified string
   **                            value.
   **                            <br>
   **                            Possible object <code>boolean</code>.
   */
  public static boolean parseBoolean(String value) {
    return StringUtility.stringToBool(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrayStringValue
  /**
   ** Converts String value to Array of String in the following way:
   ** <br>
   ** \"one\",\"two\" --&lt; ["one","two"]
   ** <br>
   ** one --&lt; ["one"]
   ** <br>
   ** \"one\\\",one\",\"two\" --&lt; ["one\",one","two"]
   ** <br>
   **
   ** @param  value              the string to be converted to array.
   **                            Allowed object is {@link String}.
   **
   ** @return                    array of Strings.
   **
   ** @throws IllegalArgumentException in case it is not possible to parse the
   **                                  value
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-array-return")
  public static String[] arrayStringValue(final String value) {
    if (value == null) {
      return null;
    }
    String inValue = value.trim();
    boolean inQuotes = false;
    boolean noQuotes = true;
    final List<String>          values = new ArrayList<String>();
    final LinkedList<Character> chars  = new LinkedList<Character>();
    for (char c : inValue.toCharArray()) {
      switch (c) {
        case '"' : if (!inQuotes) {
                     if (chars.size() > 0) {
                       // some value already present, that's wrong
                       throw new IllegalArgumentException(new StringBuilder().append("Invalid value [").append(value).append("]").toString());
                      }
                     inQuotes = true;
                     noQuotes = false;
                   }
                   else if (chars.peekLast().charValue() == '\\') {
                     // escaped quote - remove the escape char
                     chars.removeLast();
                     chars.add(Character.valueOf(c));
                   }
                   else {
                     values.add(stringValue(chars));
                     chars.clear();
                     inQuotes = false;
                   }
                   break;
        case ',' : if (inQuotes)
                     chars.add(c);
                   break;
        default  : chars.add(c);
      }
    }
    // check there is nothing left
    if (inQuotes) {
      throw new IllegalArgumentException(new StringBuilder().append("Invalid value [").append(value).append("]").toString());
    }
    if ((noQuotes) && (!StringUtility.isBlank(value)))
      values.add(value);
    else if (chars.size() > 0) {
      throw new IllegalArgumentException(new StringBuilder().append("Invalid value [").append(value).append("]").toString());
    }
    return values.toArray(new String[values.size()]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Build all attributes based on mapping provided to constructor.
   ** <p>
   ** Intended to use for create operation.
   **
   ** @param  descriptor         the {@link Descriptor} providing the mapping of
   **                            attribute name and their transformation for
   **                            provisioning.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  provider           the {@link Map} containing the values for
   **                            attribute as they are obtained from the process
   **                            form.
   **                            <br>
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @return                    the collection of {@link Attribute}s or an
   **                            empty collection if none mapped.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   */
  public static Set<Attribute> build(final Descriptor descriptor, final Map<String, Object> provider) {
    final Set<Attribute> collector = new HashSet<Attribute>();
    for (Descriptor.Attribute cursor : descriptor.attribute) {
      // ignore Uid and IGNORE flagged fields and such that are not in the
      // provider mapping
      if (cursor.uid() || cursor.ignore() || !provider.containsKey(cursor.source))
        continue;

      // single attribute
      Object value = provider.get(cursor.source);
      value = value == null ? value : convertType(value, cursor.type);
      if (OperationalAttributes.PASSWORD_NAME.equalsIgnoreCase(cursor.id) || cursor.sensitive()) {
        // special attention has to take in account if sensitive data detected
        // but a null value is provided
        collector.add(AttributeBuilder.build(cursor.id, new GuardedString(value == null ? "".toCharArray() : ((String)value).toCharArray())));
      }
      else {
        collector.add(value instanceof Collection ? AttributeBuilder.build(cursor.id, (Collection)value) : AttributeBuilder.build(cursor.id, value));
      }
    }
    // evaluate any template attribute
    if (descriptor.template != null && descriptor.template.size() > 0) {
      final DescriptorShell shell = DescriptorShell.instance();
      for (Descriptor.Template cursor : descriptor.template) {
        // single attribute
        final Object value = shell.execute(cursor.source, provider);
        collector.add(AttributeBuilder.build(cursor.id, value));
      }
    }
    // produce the logging output only if the logging level is enabled for
    if (descriptor.logger() != null && descriptor.logger().debugLevel()) {
      descriptor.debug("build", formatCollection(collector));
    }
    // build the attributes
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Build all attributes based on mapping provided to constructor.
   ** <p>
   ** Intended to use for create operation.
   **
   ** @param  type               the entry type to delete.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  descriptor         the {@link Descriptor} providing the mapping of
   **                            attribute name and their transformation for
   **                            provisioning.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  provider           the {@link Map} containing the values for
   **                            attribute as they are obtained from the process
   **                            form.
   **                            <br>
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @return                    the collection of {@link Attribute}s or an
   **                            empty collection if none mapped.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   */
  public static Set<Attribute> build(final ObjectClass type, final Descriptor descriptor, final Map<String, Object> provider) {
    final EmbeddedObjectBuilder tupel = new EmbeddedObjectBuilder().setObjectClass(type);
    for (Descriptor.Attribute cursor : descriptor.attribute) {
      // ignore flagged fields
      if (cursor.ignore())
        continue;

      // single attribute
      final Object value = convertType(provider.get(cursor.source), cursor.type);
      // should not really necessary but to be save do it right here again
      if (OperationalAttributes.PASSWORD_NAME.equalsIgnoreCase(cursor.id) || cursor.sensitive()) {
        // special attention has to take in account if sensitive data detected
        // but a null value is provided
        tupel.addAttribute(cursor.id, new GuardedString(value == null ? "".toCharArray() : ((String)value).toCharArray()));
      }
      else if (Uid.NAME.equalsIgnoreCase(cursor.id)) {
        // special attention has to take in account if the identifier attribute
        // is about to transfer it some case it isn't part of a connector
        // transaction due to it's build by the Service Provider
        tupel.addAttribute(cursor.id, (String)value);
      }
      else if (Name.NAME.equalsIgnoreCase(cursor.id)) {
        // special attention has to take in account if the name attribute is
        // about to transfer it some case it isn't part of a connector
        // transaction due to it's build by the Service Provider
        tupel.addAttribute(cursor.id, (String)value);
      }
      else {
        if (value instanceof Collection) {
          tupel.addAttribute(cursor.id, (Collection)value);
        }
        else {
          tupel.addAttribute(cursor.id, value);
        }
      }
    }
    // evaluate any template attribute
    if (descriptor.template != null && descriptor.template.size() > 0) {
      final DescriptorShell shell = DescriptorShell.instance();
      for (Descriptor.Template cursor : descriptor.template) {
        tupel.addAttribute(cursor.id, shell.execute(cursor.source, provider)).build();
      }
    }
    // build the attributes
    final AttributeBuilder builder   = new AttributeBuilder().setName(type.getObjectClassValue()).addValue(tupel.build());
    final Set<Attribute>   collector = CollectionUtility.set(builder.build());

    // produce the logging output only if the logging level is enabled for
    if (descriptor.logger() != null && descriptor.logger().debugLevel()) {
      descriptor.debug("build", formatCollection(collector));
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertType
  /**
   ** Converts the given value to Identity Connector Framework friendly type,
   ** introduced because Identity Cconnector Framework doens't support
   ** {@link Date} as a transferable type, so we need to convert it to
   ** {@link Long}
   **
   ** @param  value              the value to convert.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the target type.
   **                            <br>
   **                            Allowed object is
   **                            {@link Descriptor.Attribute.Type}.
   **
   ** @return                    the converted Identity Connector Framework
   **                            friendly value.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  private static Object convertType(final Object value, final Descriptor.Attribute.Type type) {
    final Object intermediate = convertValueType(value, type.clazz);
    return ((intermediate instanceof Date)) ? Long.valueOf(((Date)intermediate).getTime()) : intermediate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  private static String stringValue(final Collection<Character> chars) {
    final StringBuilder builder = new StringBuilder();
    for (Character character : chars) {
      builder.append(character);
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampValue
  private static Object timestampValue(final String original) {
    Object value = null;
    if (original.length() == JAVA_UTIL_DATE_LENGTH)
      value = new Timestamp(new java.util.Date(original).getTime());
    else if (original.length() == JAVA_SQL_DATE_LENGTH)
      value = new Timestamp(java.sql.Date.valueOf(original).getTime());
    else if (original.length() == JAVA_SQL_TIMESTAMP_LENGTH) {
      value = Timestamp.valueOf(original);
    }
    else {
      try {
        // During removeAttributeValues() the date fields in child forms
        // are being returned by oim API's in a different format than the ones
        // above. We will try parsing with Date.parse() which is currently able
        // to handle it
        value = new Timestamp(java.sql.Date.parse(original));
      }
      catch (Exception e) {
        // intentionally ignored
        // returnValue will still have null which will be returned like it was
        // being done before
        ;
      }
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatCollection
  /**
   ** Formats a {@link Set} of {@link Attribute}s as an output for debugging
   ** purpose.
   **
   ** @param  mapping            the {@link Set} of {@link Attribute}s to format
   **                            for debugging output.
   **
   ** @return                    the formatted string representation
   */
  private static String formatCollection(final Set<Attribute> mapping) {
    final TableFormatter table  = new TableFormatter()
    .header("Attribute")
    .header("Value")
    ;
    for (Attribute cursor : mapping) {
      StringUtility.formatValuePair(table, cursor.getName(), StringUtility.listToString(cursor.getValue()));
    }
    final StringBuilder buffer = new StringBuilder();
    table.print(buffer);
    return TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, buffer.toString());
  }
}