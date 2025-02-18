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
    Subsystem   :   Foundation Shared Library

    File        :   SchemaUtility.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.utility;

import java.util.Set;
import java.util.List;

import oracle.iam.identity.icf.connector.AbstractConnector;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;

import oracle.iam.identity.icf.foundation.SystemError;

import oracle.iam.identity.icf.foundation.resource.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class SchemaUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Miscellaneous schema utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class SchemaUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the identifier name attribute. */
  public static final String UID      = "__UID__";

  /** The name of the uniue name attribute. */
  public static final String NAME     = "__NAME__";

  /** The name of the password attribute. */
  public static final String PASSWORD = "__PASSWORD__";

  /** The name of the password attribute. */
  public static final String STATUS   = "__ENABLE__";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>SchemaUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new StringUtility()" and enforces use of the public method below.
   */
  private SchemaUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Obtains a single boolean value from the specified {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} providing values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the attribute value.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public static Boolean booleanValue(final Attribute attribute) {
		return singleValue(attribute, Boolean.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Obtains a single integer value from the specified {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} providing values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the attribute value.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public static Integer integerValue(final Attribute attribute) {
		return singleValue(attribute, Integer.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Obtains a single long value from the specified {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} providing values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the attribute value.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public static Long longValue(final Attribute attribute) {
		return singleValue(attribute, Long.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Obtains a single double value from the specified {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} providing values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the attribute value.
   **                            <br>
   **                            Possible object is {@link Double}.
   */
  public static Double doubleValue(final Attribute attribute) {
		return singleValue(attribute, Double.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   numberValue
  /**
   ** Obtains a single number value from the specified {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} providing values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the attribute value.
   **                            <br>
   **                            Possible object is {@link Number}.
   */
  public static Number numberValue(final Attribute attribute) {
		return singleValue(attribute, Number.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNullStringValue
  /**
   ** Obtains a single value from the specified {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} providing values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the attribute value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String notNullStringValue(final Attribute attribute) {
		String value = stringValue(attribute);
		if (value == null)
			AbstractConnector.propagate(SystemError.SCHEMA_ATTRIBUTE_IS_NULL, attribute.getName());

		if (StringUtility.blank(value))
			AbstractConnector.propagate(SystemError.SCHEMA_ATTRIBUTE_IS_BLANK, attribute.getName());

		return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Obtains a single string value from the specified {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} providing values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the attribute value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String stringValue(final Attribute attribute) {
		return singleValue(attribute, String.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleValue
  /**
   ** Obtains a single value from the specified {@link Attribute}.
   **
   ** @param  <T>                the type of the attribute value.
   **                            <br>
   **                            Allowed object is <code>&lt;T</code>.
   ** @param  attribute          the {@link Attribute} providing values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  expected           the expected type of the attribute value.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   **
   ** @return                    the attribute value as of type
   **                            <code>&lt;T&gt;</code>.
   **                            <br>
   **                            Possible object is <code>&lt;T&gt;</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T singleValue(final Attribute attribute, final Class<? super T> expected) {
    final List<?> values = attribute.getValue();
    if (values == null)
      return null;

    if (values.size() > 1)
			AbstractConnector.propagate(SystemError.SCHEMA_ATTRIBUTE_SINGLE, attribute.getName());

    final Object value = values.get(0);
    if (!expected.isAssignableFrom(value.getClass()))
			AbstractConnector.propagate(SystemError.SCHEMA_ATTRIBUTE_TYPE, attribute.getName(), expected, value.getClass());

    return (T)expected.cast(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookup an {@link AttributeInfo} form the given {@link ObjectClassInfo} by
   ** its name.
   **
   ** @param  classInfo          the descriptor of an object class providing the
   **                            attributes ffor lokking up.
   **                            <br>
   **                            Allowed object is {@link ObjectClassInfo}.
   ** @param  attributeName      the name of the desired {@link AttributeInfo}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>AttributeInfo</code>.
   **                            <br>
   **                            Possible object is {@link AttributeInfo}.
   */
  public static AttributeInfo find(final ObjectClassInfo classInfo, final String attributeName) {
    for (AttributeInfo attributeInfo : classInfo.getAttributeInfo()) {
      if (attributeInfo.is(attributeName)) {
        return attributeInfo;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameAttribute
  /**
   ** Lookup the name {@link Attribute} form the given {@link Set} of
   ** attributes by.
   **
   ** @param  attributes         the collection of {@link Attribute}s to be
   **                            search.
   **                            <br>
   **                            Allowed object is {@link Set} where each elemment
   **                            is of type {@link AttributeInfo.Flags}.
   **
   ** @return                    an instance of <code>Attribute</code>.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public static Name nameAttribute(final Set<Attribute> attributes) {
    final Name name = (Name)find(Name.NAME, attributes);
    if (name == null)
			throw new NullPointerException(SystemBundle.string(SystemError.SCHEMA_NAME_ENTRY_MISSING));
    
    return name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookup an {@link Attribute} form the given {@link Set} of attributes by
   ** its name.
   **
   ** @param  name               the name of the attribute to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attributes         the collection of {@link Attribute}s to be
   **                            search.
   **                            <br>
   **                            Allowed object is {@link Set} where each elemment
   **                            is of type {@link AttributeInfo.Flags}.
   **
   ** @return                    an instance of <code>Attribute</code>.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public static Attribute find(final String name, final Set<Attribute> attributes) {
    for (Attribute cursor : CollectionUtility.notNull(attributes)) {
      if (cursor.is(name)) {
        return cursor;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSpecialName
  /**
   ** Factory method to create a special name used by the connector framework.
   ** <p>
   ** A special name is enclosed in <code>__</code> (two underscore charcater).
   ** @param  name               a string as the base of the special name to
   **                            build.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the passed in name enclosed with
   **                            <code>__</code> (two underscore charcater).
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String createSpecialName(final String name) {
    if (StringUtility.blank(name))
      throw new IllegalArgumentException(SystemBundle.string(SystemError.ARGUMENT_BAD_VALUE, "name"));

    final StringBuilder builder = new StringBuilder();
    return builder.append("__").append(name.toUpperCase()).append("__").toString();
  }
}