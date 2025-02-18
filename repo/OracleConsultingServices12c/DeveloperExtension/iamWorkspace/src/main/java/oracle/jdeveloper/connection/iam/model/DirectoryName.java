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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryName.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryName.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

import javax.swing.Icon;

import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.InvalidNameException;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;

import javax.naming.ldap.Rdn;
import javax.naming.ldap.LdapName;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryName
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Represents a distinguished name in LDAP.
 ** <p>
 ** An objects of this class can be used to split a distinguished name (DN) into
 ** its individual components. You can also escape the characters in a DN.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryName extends LdapName {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Array of the characters that may be escaped in a DN or RDN.
   ** From RFC 2253 and the / character for JNDI
   */
  static final char[]       ESCAPED_CHAR     = {0x00, ',', '+', '"', '*', '\\', '/', '<', '>', '(', ')', ';'};

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8847750113467435700")
  private static final long serialVersionUID = 3103880017217634630L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient Icon    symbol;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryName</code> object from the specified
   ** distinguished name.
   ** <p>
   **
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  dn                 string representation of the distinguished
   **                            name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws InvalidNameException if a name syntax violation is detected.
   */
  private DirectoryName(final String dn)
    throws InvalidNameException {

    // ensure inheritance
    super(dn);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryName</code> given its parsed RDN components.
   ** <p>
   ** The indexing of RDNs in the list follows the numbering of RDNs described
   ** in the class description.
   **
   ** @param  rdn                the non-null list of {@link Rdn}s forming this
   **                            LDAP name.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Rdn}.
   */
  private DirectoryName(final List<Rdn> rdn) {
    // ensure inheritance
    super(rdn);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Stes the classify icon that can be used when rendering the current item. 
   **
   ** @param  symbol             the classify icon that can be used when
   **                            rendering the current item.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   **
   ** @return                    the <code>DirectoryName</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   */
  public final DirectoryName symbol(final Icon symbol) {
    this.symbol = symbol;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the classify icon that can be used when rendering the current
   ** item. 
   **
   ** @return                    the classify icon that can be used when
   **                            rendering the current item.
   **                            <br>
   **                            Possible object is {@link Icon}.
   */
  public final Icon symbol() {
    return this.symbol;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Creates a name whose components consist of a prefix of the components of
   ** this <code>DirectoryName</code>.
   ** <p>
   ** Subsequent changes to this name do not affect the name that is returned
   ** and vice versa.
   **
   ** @return                    an instance of <code>DirectoryName</code>
   **                            consisting of the components at indexes in the
   **                            range [0,1).
   **                            Possible object <code>DirectoryName</code>.
   */
  public DirectoryName prefix() {
    return prefix(getRdns().size());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Creates a name whose components consist of a prefix of the components of
   ** this <code>DirectoryName</code>.
   ** <p>
   ** Subsequent changes to this name do not affect the name that is returned
   ** and vice versa.
   **
   ** @param  pos                the 0-based index of the component at which to
   **                            stop.
   **                            Must be in the range [0,size()].
   **                            Allowed object <code>int</code>.
   **
   ** @return                    an instance of <code>DirectoryName</code>
   **                            consisting of the components at indexes in the
   **                            range [0,<code>pos</code>).
   **                            If <code>pos</code> is zero, an empty name is
   **                            returned.
   **                            Possible object <code>DirectoryName</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>pos</code> is outside the
   **                                   specified range.
   */
  public DirectoryName prefix(final int pos) {
    final List<Rdn> rdn = getRdns();
    try {
      return build(rdn.subList(pos - 1, rdn.size()));
    }
    catch (IllegalArgumentException e) {
      throw new IndexOutOfBoundsException("Pos: " + pos + ", Size: " + rdn.size());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suffix
  /**
   ** Creates a name whose components consist of a suffix of the components in
   ** this <code>DirectoryName</code>.
   ** <p>
   ** Subsequent changes to this name do not affect the name that is returned
   ** and vice versa.
   **
   ** @return                    an instance of <code>DirectoryName</code>
   **                            consisting of the components at indexes in the
   **                            range [1,size()).
   **                            If <code>size()</code> is equal to
   **                            <code>1</code>, an empty name is returned.
   **                            Possible object <code>DirectoryName</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>pos</code> is outside the
   **                                   specified range.
   */
  public DirectoryName suffix() {
    return suffix(getRdns().size() - 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suffix
  /**
   ** Creates a name whose components consist of a suffix of the components in
   ** this <code>DirectoryName</code>.
   ** <p>
   ** Subsequent changes to this name do not affect the name that is returned
   ** and vice versa.
   **
   ** @param  pos                the 0-based index of the component at which to
   **                            start.
   **                            Must be in the range [0,size()].
   **                            Allowed object <code>int</code>.
   **
   ** @return                    an instance of <code>DirectoryName</code>
   **                            consisting of the components at indexes in the
   **                            range [<code>pos</code>,size()).
   **                            If <code>pos</code> is equal to size(), an
   **                            empty name is returned.
   **                            Possible object <code>DirectoryName</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>pos</code> is outside the
   **                                   specified range.
   */
  public DirectoryName suffix(final int pos) {
    final List<Rdn> rdn = getRdns();
    try {
      return build(rdn.subList(0, pos));
    }
    catch (IllegalArgumentException e) {
      throw new IndexOutOfBoundsException("Pos: " + pos + ", Size: " + rdn.size());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryName</code> object given its
   ** <code>prefix</code> and <code>name</code>.
   **
   ** @param  prefix             the name prefix, e.g <code>cn</code>.
   **                            Allowed object {@link String}.
   ** @param  name               the name
   **                            Allowed object {@link String}.
   **
   ** @return                    the composed name
   **                            Possible object <code>DirectoryName</code>.
   **
   ** @throws NamingException    if a name syntax violation is detected.
   */
  public static DirectoryName build(final String prefix, final String name)
    throws NamingException {

    final String[] parameter = {prefix, escape(name)};
    return build(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  component          the attribute prefix, e.g <code>cn</code> and
   **                            the value
   **                            Allowed object array of {@link Object}.
   **
   ** @return                    the composed name
   **                            Allowed object {@link String}.
   **
   ** @throws NamingException    if a name syntax violation is detected.
   */
  public static DirectoryName build(final Object[] component)
    throws NamingException {

    return build(String.format("%s=%s", component));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryName</code> object from the
   ** specified distinguished name.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  dn                 string representation of the distinguished
   **                            name.
   **                            Allowed object {@link String}.
   **
   ** @return                    the validated distingusihed name.
   **                            Possible object <code>DirectoryName</code>.
   **
   ** @throws NamingException    if a name name syntax violation is detected.
   */
  public static DirectoryName build(final String dn)
    throws NamingException {

    return new DirectoryName(dn);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryName</code> object given its
   ** parsed RDN components.
   ** <p>
   ** The indexing of RDNs in the list follows the numbering of RDNs described
   ** in the class description.
   **
   ** @param  rdn                the non-null list of {@link Rdn}s forming this
   **                            LDAP name.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Rdn}.
   **
   ** @return                    the validated distingusihed name.
   **                            Possible object <code>DirectoryName</code>.
   */
  public static DirectoryName build(final List<Rdn> rdn) {
    return new DirectoryName(rdn);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** Factory method to create a <code>DirectoryName</code> from the individual
   ** component contained in the specified {@link List} of strings.
   ** <p>
   ** The {@link List} of components is expected as an array of strings, where
   ** each element is ["object , class", "object name"], e.g.
   ** ["ou", "Oracle Consulting Services"]
   **
   ** @param  component          contains the elements in the tree, deepest one
   **                            first. The String array must be of format
   **                            <code>["object prefix", "object value"]</code>
   **                            where:
   **                            <ul>
   **                              <li><code>object prefix</code> is the objects
   **                                   class type ["CN" | "OU" | ...)
   **                              <li><code>object value</code> is the LDAP
   **                                  objects attribute value
   **                                  ("DSteding" | "finance group" |  ... ).
   **                                  Basically whatever is assigned to the
   **                                  mandatory property "cn" or "ou"
   **                            </ul>
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the distinguished name of the canonical path
   **                            (including the root context), e.g.
   **                            OU=Users,OU=abc,OU=Companies,DC=oracle,DC=com
   **                            Possible object <code>DirectoryName</code>.
   **
   ** @throws NamingException    if a name syntax violation is detected.
   */
  public static DirectoryName compose(final List<String[]> component)
    throws NamingException {

    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < component.size(); i++) {
      if (i > 0)
        buffer.append(',');

      final String[] element = component.get(i);
      buffer.append(build(element[0], element[1]));
    }
    return build(buffer.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
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
   ** @throws NamingException    if the operation fails.
   */
  public static String stringValue(final Attributes attributes, final String attributeName)
    throws NamingException {

    final Attribute attribute = attributes.get(attributeName);
    return (attribute != null) ? (String)attribute.get() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringCollection
  /**
   ** Collects all occurences of a string value from the given collection of
   ** object attributes.
   **
   ** @param  attributes         the collection of {@link Attributes}s.
   **                            Allowed object {@link Attributes}.
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            Allowed object {@link String}.
   **
   ** @return                    the {@link List} where the attribute values
   **                            discovered are collected in.
   **                            Possible object {@link List} where each element
   **                            is of type {@link String}.
   **
   ** @throws NamingException    if the operation fails.
   */
  public static Set<String> stringCollection(final Attributes attributes, final String attributeName)
    throws NamingException {

    final Set<String>   collection = new LinkedHashSet<>();
    final BasicAttribute attribute = (BasicAttribute)attributes.get(attributeName);
    if (attribute == null)
      return collection;

    final NamingEnumeration cursor = attribute.getAll();
    while (cursor.hasMore()) {
      Object obj = cursor.next();
      if ((obj instanceof String)) {
        collection.add((String)obj);
      }
      else if ((obj instanceof BasicAttribute)) {
        BasicAttribute ba = (BasicAttribute)obj;
        final NamingEnumeration baEnum = ba.getAll();
        while (baEnum.hasMore())
          collection.add((String)baEnum.next());
      }
      else {
        collection.add(obj.toString());
      }
    }
    return collection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rdn
  /**
   ** Returns the relative name of a distinguished name (DN).
   **
   ** @param distinguishedName   distinguished name of which you want to get the
   **                            rdn components in the format "cn=Admin".
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a String array representing the relative name
   **                            as ["object prefix", "object value"],
   **                            e.g. ["ou", "Oracle Consulting Services"]
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  public static String[] rdn(final String distinguishedName) {
    return explode(distinguishedName).get(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   explode
  /**
   ** Returns the individual components of a distinguished name (DN).
   ** <p>
   ** The returned hierarchy is a {@link List} of 2-element string arrays.
   ** <p>
   ** <b>Important</b>:
   ** <br>
   ** Remember, that since the distinguished name was probably retrieved through
   ** a search which returns RDNs (like getUsers, getGroup, etc), to get the
   ** hierarchy relative to the root context, you must pass in as the second
   ** parameter the hierarchy below which the, search was done.
   **
   ** @param distinguishedName   distinguished name of which you want to get the
   **                            components in the format
   **                            "cn=Admin,ou=Groups,dc=pxed,dc=pfizer,dc=com,o=PXED-DEV"
   **                            <br>
   **                            <b>Important</b>:
   **                            <br>
   **                            deepest first
   **                            Allowed object {@link String}.
   **
   ** @return                    a {@link List} of String arrays, where each
   **                            element is ["object prefix", "object value"],
   **                            e.g. ["ou", "Oracle Consulting Services"]
   **                            Possible object {@link List} where each
   **                            element is of type {@link String[]}.
   */
  public static List<String[]> explode(final String distinguishedName) {
    return explode(distinguishedName, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   explode
  /**
   ** Returns the individual components of a distinguished name (DN).
   ** <p>
   ** The returned hierarchy is a {@link List} of 2-element string arrays.
   ** <p>
   ** <b>Important</b>:
   ** <br>
   ** Remember, that since the distinguished name was probably retrieved through
   ** a search which returns RDNs (like getUsers, getGroup, etc), to get the
   ** hierarchy relative to the root context, you must pass in as the second
   ** parameter the hierarchy below which the, search was done.
   **
   ** @param distinguishedName   distinguished name of which you want to get the
   **                            components in the format
   **                            "cn=Admin,ou=Groups,dc=pxed,dc=pfizer,dc=com,o=PXED-DEV"
   **                            <br>
   **                            <b>Important</b>:
   **                            <br>
   **                            deepest first
   **                            Allowed object {@link String}.
   ** @param contextRoot         the hierarchy that the
   **                            <code>distinguishedName</code> is relative to,
   **                            in case it needs to be appended.
   **                            Allowed object array of {@link String}.
   **
   ** @return                    a {@link List} of String arrays, where each
   **                            element is ["object prefix", "object value"],
   **                            e.g. ["ou", "Oracle Consulting Services"]
   **                            Possible object {@link List} where each
   **                            element is of type {@link String[]}.
   */
  public static List<String[]> explode(final String distinguishedName, final List<String[]> contextRoot) {
    List<String[]> component      = new ArrayList<String[]>();
    int            firstComponent = distinguishedName.indexOf("=");
    int            lastComponent  = distinguishedName.lastIndexOf("=");
    // in case the given Distinguished Name contains only on component
    if (firstComponent == lastComponent) {
      if (firstComponent != -1)
        component.add(new String[] { distinguishedName.substring(0, firstComponent), distinguishedName.substring(firstComponent + 1).trim() });
    }
    else {
      int             comma       = 0;
      StringTokenizer tokenizer   = new StringTokenizer(distinguishedName, "=", false);
      String          objectType  = tokenizer.nextToken();
      String          objectValue = null;
      while (tokenizer.hasMoreTokens()) {
        objectValue = tokenizer.nextToken();
        comma       = objectValue.lastIndexOf(",");

        objectType  = objectType.trim();
        if (comma == -1) {
          component.add(new String[] { objectType, objectValue });
        }
        else {
          component.add(new String[] { objectType, objectValue.substring(0, comma).trim() });
          objectType = objectValue.substring(comma + 1);
        }
      }
    }
    if (contextRoot != null && contextRoot.size() > 0)
      component.addAll(contextRoot);

    return component;
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
    return compose(prefix, '=', value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeFilter
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
   **
   ** @return                    the escaped RDN.
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
   **                            Allowed obect {@link Object}.
   ** @param  builder            the value holder receiving the transformed
   **                            data.
   **                            Allowed obect {@link StringBuilder}.
   **
   ** @return                    <code>true</code> id the provided value was
   **                            escaped.
   **                            Possible object <code>boolean</code>.
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
   **                            Allowed obect {@link Object}.
   ** @param  builder            the value holder receiving the transformed
   **                            data.
   **                            Allowed obect {@link StringBuilder}.
   **
   ** @return                    <code>true</code> id the provided value was
   **                            escaped.
   **                            Possible object <code>boolean</code>.
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
   **                            Allowed obect {@link Object}.
   ** @param  builder            the value holder receiving the transformed
   **                            data.
   **                            Allowed obect {@link StringBuilder}.
   **
   ** @return                    <code>true</code> id the provided value was
   **                            escaped.
   **                            Possible object <code>boolean</code>.
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
   **
   ** @return                    the unescaped RDN.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #escape(java.lang.String)
   */
  public static String unescape(final String value) {
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Gets the parent DN for this DN.
   ** <br>
   ** For example, the following section of code gets the parent DN of
   ** "uid=bjensen, ou=People, o=Airius.com."
   ** <pre>
   **    DN dn = new DN("uid=bjensen, ou=People, o=Airius.com");
   **    DN parent = dn.getParent();
   ** </pre>
   ** The parent DN in this example is "ou=People, o=Airius.com".
   **
   ** @return                    DN of the parent of this DN.
   **
   ** @throws InvalidNameException if a name syntax violation is detected.
   */
  public DirectoryName parent()
    throws InvalidNameException {

    DirectoryName newdn = new DirectoryName(getRdns());
    for (int i = this.size() - 1; i > 0; i--)
      newdn.add(this.get(i));

    return newdn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descendantOf
  /**
   ** Determines if this DN is a descendant of the given DN.
   ** <br>
   ** For example, the following section of code determines if the DN specified
   ** by <code>dn1</code> is a descendant of the DN specified by
   ** <code>dn2</code>.
   ** <pre>
   **    DN dn1 = new DN("uid=bjensen, ou=People, o=Airius.com");
   **    DN dn2 = new DN("ou=People, o=Airius.com");
   **
   **    boolean isDescendant = dn1.isDescendantOf(dn2)
   ** </pre>
   ** In this case, since "uid=bjensen, ou=People, o=Airius.com" is an entry
   ** under the subtree "ou=People, o=Airius.com", the value of
   ** <code>isDescendant</code> is true.
   ** <br>
   ** In the case where the given DN is equal to this DN it returns false.
   **
   ** @param  other              the DN of a subtree to check
   **
   ** @return                    <code>true</code> if the current DN is a
   **                            descendant of the DN specified by
   **                            <code>dn</code>.
   */
  public boolean descendantOf(final DirectoryName other) {
    List<Rdn> rdns1 = other.getRdns();
    List<Rdn> rdns2 = this.getRdns();

    int i = rdns1.size() - 1;
    int j = rdns2.size() - 1;

    if ((j < i) || (equals(other) == true))
      return false;

    for (; i >= 0 && j >= 0; i--, j--) {
      Rdn rdn1 = rdns1.get(i);
      Rdn rdn2 = rdns2.get(j);
      if (!rdn2.equals(rdn1))
        return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   explode
  /**
   ** Returns an array of the individual components that make up the current
   ** distinguished name.
   **
   ** @param  noTypes            specify <code>true</code> to remove the
   **                            attribute type and equals sign (for example,
   **                            "cn=") from each component.
   **
   ** @return                    an array of the individual components that
   **                            make up the current distinguished name.
   */
  public String[] explode(final boolean noTypes) {
    if (this.size() == 0)
      return new String[0];

    String str[] = new String[this.size()];
    for (int i = this.size(); i > 0; i--) {
      if (noTypes)
        str[i] = (String)this.getRdn(i).getValue();
      else
        str[i] = this.getRdn(i).toString();
    }
    return str;
  }
}