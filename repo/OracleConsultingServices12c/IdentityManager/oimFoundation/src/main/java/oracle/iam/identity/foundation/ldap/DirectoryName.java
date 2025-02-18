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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryName.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryName.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.naming.InvalidNameException;

import javax.naming.ldap.Rdn;
import javax.naming.ldap.LdapName;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

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
 ** @version 1.0.0.0
 */
public class DirectoryName extends LdapName {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Type specifying a DN in the RFC format. */
  public static final int   RFC              = 0;

  /** Type specifying a DN in the OSF format. */
  public static final int   OSF              = 1;

  /**
   ** Array of the characters that may be escaped in a DN or RDN.
   ** From RFC 2253 and the / character for JNDI
   */
  static final char[]       ESCAPED_CHAR     = {',', '+', '"', '\\', '/', '<', '>', ';'};

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3193226680301542829")
  private static final long serialVersionUID = 4963046813081137477L;

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
   **
   ** @throws InvalidNameException if a syntax violation is detected.
   */
  public DirectoryName(final String dn)
    throws InvalidNameException {

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
   */
  public DirectoryName(List<Rdn> rdn) {
    super(rdn);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valid
  /**
   ** Determines if the given string is an distinguished name or not.
   **
   ** @param  dn                 distinguished name
   **
   ** @return                    <code>true</code> or <code>false</code>.
   */
  public static boolean valid(final String dn) {
    if (dn.equals(SystemConstant.EMPTY))
      return true;

    try {
      DirectoryName newdn = new DirectoryName(dn);
      return (newdn.size() > 0);
    }
    catch (InvalidNameException e) {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rdn
  /**
   ** Returns the relative name of a distinguished name (DN).
   **
   ** @param distinguishedName   distinguished name of which you want to get the
   **                            rdn components in the format "cn=Admin".
   **
   ** @return                    a String array representing the relative name
   **                            as ["object prefix", "object value"],
   **                            e.g. ["ou", "Oracle Consulting Services"]
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
   **
   ** @return                    a {@link List} of String arrays, where each
   **                            element is ["object prefix", "object value"],
   **                            e.g. ["ou", "Oracle Consulting Services"]
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
   ** @param contextRoot         the hierarchy that the
   **                            <code>distinguishedName</code> is relative to,
   **                            in case it needs to be appended.
   **
   ** @return                    a {@link List} of String arrays, where each
   **                            element is ["object prefix", "object value"],
   **                            e.g. ["ou", "Oracle Consulting Services"]
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
  // Method:   compose
  /**
   ** Composits a distinguished name from the individual component contained in
   ** the specified {@link List}.
   ** <p>
   ** The {@link List} of components is expected as an array of Strings, where
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
   **
   ** @return                    the distinguished name of the canonical path
   **                           (including the root context), e.g.
   **                           OU=Users,OU=abc,OU=Companies,DC=oracle,DC=com
   */
  public static String compose(final List<String[]> component) {
    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < component.size(); i++) {
      if (i > 0)
        buffer.append(SystemConstant.COMMA);

      final String[] element = component.get(i);
      buffer.append(composeName(element[0], element[1]));
    }

    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeName
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   ** @param  value              the value
   **
   ** @return                    the composed name
   */
  public static String composeName(String prefix, String value) {
    final String[] parameter = { prefix, escape(value) };
    return composeName(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeName
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  component          the attribute prefix, e.g <code>cn</code> and
   **                            the value
   **
   ** @return                    the composed name
   */
  public static String composeName(final Object[] component) {
    return String.format("%s=%s", component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeFilter
  /**
   ** This method gives the filter condition value for the given prefix and
   ** value string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   ** @param  value              the value
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
   ** @return                    the unescaped RDN.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #unescape(java.lang.String)
   */
  public static String escape(final String value) {
    final StringBuilder buffer = new StringBuilder();
    if (!StringUtility.isEmpty(value)) {
      // Positional characters - see RFC 2253
      String escaped = value.replaceAll("^#", "\\\\#");
      escaped = escaped.replaceAll("^ | $", "\\\\ ");

      for (int i = 0; i < escaped.length(); i++) {
        for (int j = 0; j < ESCAPED_CHAR.length; j++) {
          if (escaped.charAt(i) == ESCAPED_CHAR[j])
            buffer.append('\\');
        }
        buffer.append(escaped.charAt(i));
      }
    }
    return buffer.toString();
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
  public static String unescape(String value) {
    final StringBuilder buffer = new StringBuilder();
    if (!StringUtility.isEmpty(value)) {
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
   ** @throws InvalidNameException if a syntax violation is detected.
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