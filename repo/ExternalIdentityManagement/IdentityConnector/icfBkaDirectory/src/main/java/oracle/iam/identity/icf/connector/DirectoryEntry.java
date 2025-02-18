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
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryEntry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryEntry.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

import javax.naming.NamingException;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.directory.InvalidAttributeIdentifierException;

import oracle.iam.identity.icf.foundation.SystemConstant;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryEntry
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This class provides miscelanious method to handle LDAP entries
 ** <p>
 ** An entry contains a distinguished name (DN) and a set of attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryEntry {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Array of the characters that may be escaped in a DN or RDN.
   ** From RFC 2253 and the / character for JNDI
   */
  static final char[]      ESCAPED_CHAR = {0x00, ',', '+', '"', '*', '\\', '/', '<', '>', '(', ')', ';'};
  
  static final Set<String> ENTRY_DN     = CollectionUtility.unmodifiableSet("dn", "entryDN", "distinguishedName");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the name is fix over the lifetime of the instance
  final DirectoryName      name;
  // attribute are flexible but only in the package itself
  Attributes               attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (protected)
  /**
   ** Creates a <code>DirectoryEntry</code> with the specified <code>name</code>
   ** and attributes.
   **
   ** @param  name               the distinguished name of the entry.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  attribute          the collections of attribuets of this entry.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   */
  private DirectoryEntry(final DirectoryName name, final Attributes attribute) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name      = name;
    this.attribute = attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the {@link DirectoryName} this entry belongs to in the Directory
   ** Service.
   **
   ** @return                    the {@link DirectoryName} this entry belongs to
   **                            in the Directory Service.
   **                            <br>
   **                            Possible object is {@link DirectoryName}.
   */
  public final DirectoryName name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link Attributes} this entry have in the Directory Service.
   **
   ** @return                    the {@link DirectoryName} this entry have in
   **                            the Directory Service.
   **                            <br>
   **                            Possible object is {@link Attributes}.
   */
  public final Attributes attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns a string value from a particular object attribute.
   **
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value for the attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the requested attribute could not be
   **                            obtained.
   */
  public final String value(final String attributeName)
    throws SystemException {
    
    return value(this.attribute, attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values
  /**
   ** Returns a collection of string values from a particular object attribute.
   **
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Set} of values for the attribute.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if the requested attribute could not be
   **                            obtained.
   */
  public final Set<String> values(final String attributeName)
    throws SystemException {

    return values(this.attribute, attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryEntry</code> from the specified
   ** <code>result</code>.
   **
   ** @param  result             an entry obtained from the Directory Service.
   **                            <br>
   **                            Allowed object is {@link SearchResult}.
   **
   ** @return                    an newly created instance of
   **                            <code>DirectoryEntry</code> populate with the
   **                            properties provided.
   **                            <br>
   **                            Possible object is <code>DirectoryEntry</code>.
   */
  public static DirectoryEntry build(final SearchResult result) {
    return new DirectoryEntry(DirectoryName.quiet(result.getNameInNamespace()), result.getAttributes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryEntry</code> with the specified
   ** <code>name</code> and attributes.
   **
   ** @param  name               the distinguished name of the entry.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  attribute          the collections of attribuets of this entry.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @return                    an newly created instance of
   **                            <code>DirectoryEntry</code> populate with the
   **                            properties provided.
   **                            <br>
   **                            Possible object is <code>DirectoryEntry</code>.
   */
  public static DirectoryEntry build(final DirectoryName name, final Attributes attribute) {
    return new DirectoryEntry(name, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedName
  /**
   ** returns <code>true</code> if the given string is an attribute name that
   ** maps to a distinguished name.
   **
   ** @param  attributeName      the string to verify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the given string is an
   **                            attribute name that maps to a distinguished
   **                            name; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public static boolean distinguishedName(final String attributeName) {
    return ENTRY_DN.contains(attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns a string value from a particular search result.
   **
   ** @param  result             the {@link SearchResult} to inspect.
   **                            <br>
   **                            Allowed object is {@link SearchResult}.
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value for the attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the requested attribute could not be
   **                            obtained.
   */
  public static String value(final SearchResult result, final String attributeName)
    throws SystemException {

    final Attributes attribute = result.getAttributes();
    if (attribute != null) {
      try {
        return (String)attribute.get(attributeName).get(0);
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns a string value from a particular object attribute.
   **
   ** @param  attributes         the collection of {@link Attribute}s.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value for the attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the requested attribute could not be
   **                            obtained.
   */
  public static String value(final Attributes attributes, final String attributeName)
    throws SystemException {

    final javax.naming.directory.Attribute attribute = attributes.get(attributeName);
    if (attribute != null) {
      try {
        return (String)attribute.get();
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values
  /**
   ** Returns a collection of string values from a particular object attribute.
   **
   ** @param  attributes         the collection of {@link Attribute}s.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Set} of values for the attribute.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if the requested attribute could not be
   **                            obtained.
   */
  public static Set<String> values(final Attributes attributes, final String attributeName)
    throws SystemException {

    final Set<String> collector = new HashSet<String>();
    values(attributes, attributeName, collector);
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values
  /**
   ** Returns a collection of string values from a particular object attribute.
   **
   ** @param  attributes         the collection of {@link Attribute}s.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  collector          the {@link Set} of values for the attribute to
   **                            collect.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if the requested attribute could not be
   **                            obtained.
   */
  public static void values(final Attributes attributes, final String attributeName, final Collection<String> collector)
    throws SystemException {

    final Attribute attribute = attributes.get(attributeName);
    if (attribute != null) {
      for (int i = 0; i < attribute.size(); i++)
        try {
          collector.add((String)attribute.get(i));
        }
        // this exception is thrown when an attempt is made to add or modify an
        // attribute set that has been specified incompletely or incorrectly.
        // This could happen, for example, when attempting to add or modify a
        // binding, or to create a new subcontext without specifying all the
        // mandatory attributes required for creation of the object.
        // Another situation in which this exception is thrown is by
        // specification of incompatible attributes within the same attribute
        // set, or attributes in conflict with that specified by the object's
        // schema. 
        catch (InvalidAttributesException e) {
          throw DirectoryException.attributeInvalidData(e, attributeName);
        }
        // this exception is thrown when an attempt is made to add to create an
        // attribute with an invalid attribute identifier.
        // the validity of an attribute identifier is directory-specific. 
        catch (InvalidAttributeIdentifierException e) {
          throw DirectoryException.attributeInvalidType(e, attributeName);
        }
        catch (NamingException e) {
          throw SystemException.unhandled(e);
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeFilter
  /**
   ** This method gives the filter condition value for the given prefix and
   ** value string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the filter condition
   */
  public static String composeFilter(final String prefix, final String value) {
    return compose(prefix, SystemConstant.EQUAL, value);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeFilter
  /**
   ** This method gives the filter condition value for the given prefix and
   ** set of values string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            Allowed object array of {@link String}.
   ** @param  values             the set of values
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the filter condition
   */
  public static String composeFilter(final String prefix, final Set<String> values) {
    StringBuilder filter = new StringBuilder();
    for (String value : values) {
      filter.append(compose(prefix, SystemConstant.EQUAL, value));
    }
    return filter.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** This method gives the filter condition value for the given prefix and
   ** value string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   ** @param  predicate          the ....
   ** @param  value              the value
   **
   ** @return                    the filter condition
   */
  public static String compose(final String prefix, final char predicate, final String value) {
    return String.format("(%s%s%s)", prefix, predicate, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escape
  /**
   ** Returns the RDN after escaping any escaped characters.
   ** <p>
   ** For a list of characters that are typically escaped in a DN, see
   ** {@link #ESCAPED_CHAR}.
   **
   ** @param  value              the RDN path of the object (relative to the
   **                            connected hierarchy), usually it is just the
   **                            objects name (e.g. cn=Admin).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the escaped RDN.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #unescape(java.lang.String)
   */
  public static String escape(final String value) {
    final StringBuilder buffer = new StringBuilder();
    escapeValue(value, buffer);
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escapeValue
  /**
   ** Returns a string value after escaping any character is escaped.
   ** <p>
   ** For a list of characters that are typically escaped in a value, see
   ** {@link #ESCAPED_CHAR}.
   **
   ** @param  value              the object value to escape.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  builder            the value holder receiving the transformed
   **                            data.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **
   ** @return                    <code>true</code> id the provided value was
   **                            escaped.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #unescape(java.lang.String)
   */
  public static boolean escapeValue(final Object value, final StringBuilder builder) {
    // prevent bogus input
   if (value == null)
      return false;

   return (value instanceof byte[]) ? escapeByte((byte[])value, builder) : escapeString(value.toString(), builder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escapeByte
  /**
   ** Returns a byte array value after escaping any byte is escaped .
   ** <p>
   ** For a list of characters that are typically escaped in a DN, see
   ** {@link #ESCAPED_CHAR}.
   **
   ** @param  value              the object value to escape.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   ** @param  builder            the value holder receiving the transformed
   **                            data.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **
   ** @return                    <code>true</code> id the provided value was
   **                            escaped.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #unescape(java.lang.String)
   */
  public static boolean escapeByte(final byte[] value, final StringBuilder builder) {
    // prevent bogus input
    if (value.length == 0)
      return false;

    for (byte b : value) {
      builder.append('\\');
      String hex = Integer.toHexString(b & 0xFF);
      if (hex.length() < 2) {
        builder.append('0');
      }
      builder.append(hex);
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escapeString
  /**
   ** Returns a string value after escaping any byte is escaped .
   ** <p>
   ** For a list of characters that are typically escaped in a DN, see
   ** {@link #ESCAPED_CHAR}.
   **
   ** @param  value              the object value to escape.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  builder            the value holder receiving the transformed
   **                            data.
   **                            Allowed object is {@link StringBuilder}.
   **
   ** @return                    <code>true</code> id the provided value was
   **                            escaped.
   **                            Possible object is <code>boolean</code>.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #unescape(java.lang.String)
   */
  public static boolean escapeString(final String value, final StringBuilder builder) {
    // prevent bogus input
    if (StringUtility.empty(value))
      return false;

    // Positional characters - see RFC 2253
    String escaped = value.replaceAll("^#", "\\\\#");
    escaped = escaped.replaceAll("^ | $", "\\\\ ");

    for (int i = 0; i < escaped.length(); i++) {
      for (int j = 0; j < ESCAPED_CHAR.length; j++) {
        if (escaped.charAt(i) == ESCAPED_CHAR[j])
          builder.append('\\');
      }
      builder.append(escaped.charAt(i));
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unescape
  /**
   ** Returns the RDN after unescaping any escaped characters.
   ** <p>
   ** For a list of characters that are typically escaped in a DN, see
   ** {@link #ESCAPED_CHAR}.
   **
   ** @param  value              the value of the RDN to unescape
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the unescaped RDN.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #escape(java.lang.String)
   */
  public static String unescape(String value) {
    final StringBuilder buffer = new StringBuilder();
    if (!StringUtility.empty(value)) {
      for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        if (c  == '\\') {
          buffer.setCharAt(i, 'x');
          if (i < buffer.length() - 1)
            buffer.setCharAt(i + 1, 'x');
          else
            return null;
        }
      }
    }

    // second pass, disable quoted sequences
    boolean quoteOn = false;
    for (int i = 0; i < buffer.length(); i++) {
      if (buffer.charAt(i) == '"') {
        quoteOn = !quoteOn;
        continue;
      }
      if (quoteOn)
        buffer.setCharAt(i, 'x');
    }
    return quoteOn ? null : buffer.toString();
  }
}