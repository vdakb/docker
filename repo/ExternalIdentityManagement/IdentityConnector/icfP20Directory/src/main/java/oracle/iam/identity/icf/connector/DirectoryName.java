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

    File        :   DirectoryName.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryName.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.List;
import java.util.ArrayList;

import javax.naming.InvalidNameException;

import javax.naming.ldap.Rdn;
import javax.naming.ldap.LdapName;

import oracle.iam.identity.icf.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryName
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This class provides a data structure for holding information about an LDAP
 ** distinguished name (DN).
 ** <br>
 ** A DN consists of a comma-delimited list of zero or more RDN components.
 ** <br>
 ** See <a href="http://www.ietf.org/rfc/rfc4514.txt">RFC 4514</a> for more
 ** information about representing DNs and RDNs as strings.
 ** <p>
 ** Examples of valid DNs (excluding the quotation marks, which are provided for
 ** clarity) include:
 ** <ul>
 **   <li>""
 **       <br>
 **       This is the zero-length DN (also called the <code>null</code> DN),
 **       which may be used to refer to the directory server root DSE.
 **   <li>"<code>o=example.com</code>"
 **       <br>
 **       This is a DN with a single, single-valued RDN. The RDN attribute is
 **       "<code>o</code>" and the RDN value is "<code>example.com</code>".
 **   <li>"<code>givenName=John+sn=Doe,ou=People,dc=example,dc=com</code>"
 **       This is a DN with four different RDNs
 **       ("<code>givenName=John+sn=Doe"</code>, "<code>ou=People</code>",
 **       "<code>dc=example</code>", and "<code>dc=com</code>". The first RDN is
 **       multivalued with attribute-value pairs of
 **       "<code>givenName=John</code>" and "<code>sn=Doe</code>".
 ** </ul>
 ** <b>Note</b>:
 ** <br>
 ** That there is some inherent ambiguity in the string representations of
 ** distinguished names. In particular, there may be differences in spacing
 ** (particularly around commas and equal signs, as well as plus signs in
 ** multivalued RDNs), and also differences in capitalization in attribute names
 ** and/or values.
 ** <br>
 ** For example, the strings
 ** "<code>uid=john.doe,ou=people,dc=example,dc=com</code>" and
 ** "<code>UID = JOHN.DOE , OU = PEOPLE , DC = EXAMPLE , DC = COM</code>"
 ** actually refer to the same distinguished name.
 ** <br>
 ** To deal with these differences, the normalized representation may be used.
 ** <br>
 ** The normalized representation is a standardized way of representing a DN,
 ** and it is obtained by eliminating any unnecessary spaces and converting all
 ** non-case-sensitive characters to lowercase.
 ** <br>
 ** The normalized representation of a DN may be obtained using the
 ** {@link #toString()} method, and two DNs may be compared to determine if they
 ** are equal using the standard {@link #equals} method.
 ** <p>
 ** Distinguished names are hierarchical.
 ** <br>
 ** The rightmost RDN refers to the root of the directory information tree
 ** (DIT), and each successive RDN to the left indicates the addition of another
 ** level of hierarchy.
 ** <br>
 ** For example, in the DN "<code>uid=john.doe,ou=People,o=example.com</code>",
 ** the entry "<code>o=example.com</code>" is at the root of the DIT, the entry
 ** "<code>ou=People,o=example.com</code>" is an immediate descendant of the
 ** "<code>o=example.com</code>" entry, and the
 ** "<code>uid=john.doe,ou=People,o=example.com</code>" entry is an immediate
 ** descendant of the "<code>ou=People,o=example.com</code>" entry.
 ** <br>
 ** Similarly, the entry "<code>uid=jane.doe,ou=People,o=example.com</code>"
 ** would be considered a peer of the
 ** "<code>uid=john.doe,ou=People,o=example.com</code>" entry because they have
 ** the same parent.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** That in some cases, the root of the DIT may actually contain a DN with
 ** multiple RDNs.
 ** <br>
 ** For example, in the DN
 ** "<code>uid=john.doe,ou=People,dc=example,dc=com</code>", the Directory
 ** Service may or may not actually have a "<code>dc=com</code>" entry.
 ** <br>
 ** In many such cases, the base entry may actually be just
 ** "<code>dc=example,dc=com</code>".
 ** <br>
 ** The DN's of the entries that are at the base of the directory information
 ** tree are called "naming contexts" or "suffixes" and they are generally
 ** available in the <code>namingContexts</code> attribute of the root DSE.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryName extends LdapName {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A pre-allocated DirectoryName object equivalent to the <code>null</code>
   ** DirectoryName.
   ** <p>
   ** The Root DSE Backend contains the Directory Server root DSE.
   ** <p>
   ** This is a special meta-backend that dynamically generates the root DSE
   ** entry for base-level searches and simply redirects to other backends for
   ** operations in other scopes.
   */
  static final DirectoryName ROOT             = new DirectoryName(new ArrayList<Rdn>());

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1188150903696008577")
  private static final long  serialVersionUID = -9194507940636809135L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryName</code> object from the specified
   ** distinguished name.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  dn                 string representation of the distinguished
   **                            name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws InvalidNameException if a syntax violation is detected.
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
  // Method:   valid
  /**
   ** Determines if the given string is an distinguished name or not.
   **
   ** @param  dn                 the distinguished name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> or <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean valid(final String dn) {
    if (dn.equals(SystemConstant.EMPTY))
      return true;

    try {
      DirectoryName newdn = build(dn);
      return (newdn.size() > 0);
    }
    catch (DirectoryException e) {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suffix
  /**
   ** Creates a name whose components consist of a suffix of the components of
   ** this <code>DirectoryName</code>.
   ** <p>
   ** Subsequent changes to this name do not affect the name that is returned
   ** and vice versa.
   **
   ** @return                    an instance of <code>DirectoryName</code>
   **                            consisting of the components at indexes in the
   **                            range [0,1).
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   */
  public DirectoryName suffix() {
    return suffix(getRdns().size() - 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suffix
  /**
   ** Creates a name whose components consist of a suffix of the components of
   ** this <code>DirectoryName</code>.
   ** <p>
   ** Subsequent changes to this name do not affect the name that is returned
   ** and vice versa.
   **
   ** @param  pos                the 0-based index of the component at which to
   **                            stop.
   **                            Must be in the range [0,size()].
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    an instance of <code>DirectoryName</code>
   **                            consisting of the components at indexes in the
   **                            range [0,<code>pos</code>).
   **                            If <code>pos</code> is zero, an empty name is
   **                            returned.
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>pos</code> is outside the
   **                                   specified range.
   */
  public DirectoryName suffix(final int pos) {
    final List<Rdn> rdn = getRdns();
    try {
      return new DirectoryName(rdn.subList(0, pos));
    }
    catch (IllegalArgumentException e) {
      throw new IndexOutOfBoundsException("Pos: " + pos + ", Size: " + rdn.size());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Creates a name whose components consist of a prefix of the components in
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
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>pos</code> is outside the
   **                                   specified range.
   */
  public DirectoryName prefix() {
    return prefix(getRdns().size() - 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Creates a name whose components consist of a prefix of the components in
   ** this <code>DirectoryName</code>.
   ** <p>
   ** Subsequent changes to this name do not affect the name that is returned
   ** and vice versa.
   **
   ** @param  pos                the 0-based index of the component at which to
   **                            start.
   **                            Must be in the range [0,size()].
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    an instance of <code>DirectoryName</code>
   **                            consisting of the components at indexes in the
   **                            range [<code>pos</code>,size()).
   **                            If <code>pos</code> is equal to size(), an
   **                            empty name is returned.
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>pos</code> is outside the
   **                                   specified range.
   */
  public DirectoryName prefix(final int pos) {
    final List<Rdn> rdn = getRdns();
    try {
      return new DirectoryName(rdn.subList(pos, rdn.size()));
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
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the distinguished name of the canonical path
   **                            (including the root context), e.g.
   **                            OU=Users,OU=abc,OU=Companies,DC=oracle,DC=com
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  public static DirectoryName build(final List<String[]> component)
    throws DirectoryException {

    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < component.size(); i++) {
      if (i > 0)
        buffer.append(SystemConstant.COMMA);

      final String[] element = component.get(i);
      buffer.append(build(element[0], element[1]));
    }
    return build(buffer.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the composed name
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  public static DirectoryName build(final String prefix, final String value)
    throws DirectoryException {

    return build(String.format("%s=%s", prefix, DirectoryEntry.escape(value)));
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
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the validated distingusihed name.
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  public static DirectoryName build(final String dn)
    throws DirectoryException {

    try {
      return new DirectoryName(dn == null ? "" : dn);
    }
    catch (InvalidNameException e) {
      throw DirectoryException.invalidName(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   move
  /**
   ** This method "moves" the given <code>DirectoryName</code> as a subentry
   ** to the given <code>superior</code> <code>DirectoryName</code>.
   **
   ** @param  origin             the entry DN to move.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  superior           the soperior DN to move <code>origin</code> to.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the composed name.
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  public static DirectoryName move(final DirectoryName origin, final DirectoryName superior)
    throws DirectoryException {

    DirectoryName target = null;
    if (superior == null || ROOT.equals(superior)) {
      target= origin.prefix();
    }
    else {
      
    }
    target.add(origin.getRdn(origin.size() -1));
    return target;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   quiet
  /**
   ** Factory method to create a <code>DirectoryName</code> object from the
   ** specified distinguished name.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  dn                 string representation of the distinguished
   **                            name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the validated distingusihed name.
   **                            <br>
   **                            Possible object is <code>DirectoryName</code>.
   */
  public static DirectoryName quiet(final String dn) {
    DirectoryName name = null;
    try {
      name = build(dn);
    }
    catch (DirectoryException e) {
      e.printStackTrace();
    }
    return name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ancestorOf
  /**
   ** Indicates whether the DN represented by the first string is an ancestor of
   ** the DN represented by the second string.
   ** <br>
   ** The first DN will be considered an ancestor of the second DN if the list
   ** of RDN components for the first DN ends with the elements that comprise
   ** the list of RDN components for the second DN (i.e., if the first DN is
   ** subordinate to, or optionally equal to, the second DN).
   ** <br>
   ** The null DN will be considered an ancestor for all other DN's.
   **
   ** @param  lhs                the string representation of the first DN for
   **                            which to make the determination.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the string representation of the second DN for
   **                            which to make the determination.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the first DN may be
   **                            considered an ancestor of the second DN.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  public static boolean ancestorOf(final String lhs, final String rhs)
    throws DirectoryException {

    return build(lhs).ancestorOf(build(rhs));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ancestorOf
  /**
   ** Indicates whether this DN is an ancestor of the provided DN.
   ** <br>
   ** It will be considered an ancestor of the provided DN if the list of RDN
   ** components for the provided DN ends with the elements that comprise the
   ** list of RDN components for this DN (i.e., if the provided DN is
   ** subordinate to, or optionally equal to, this DN).
   ** <br>
   ** The <code>null</code> DN will be considered an ancestor for all other.
   **
   ** @param  other              the DN for which to make the determination.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if this DN may be considered
   **                            an ancestor of the provided DN, or
   **                            <code>false</code> if not.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  public boolean ancestorOf(final String other)
    throws DirectoryException {

    return ancestorOf(build(other));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ancestorOf
  /**
   ** Indicates whether this DN is an ancestor of the provided DN.
   ** <br>
   ** It will be considered an ancestor of the provided DN if the list of RDN
   ** components for the provided DN ends with the elements that comprise the
   ** list of RDN components for this DN (i.e., if the provided DN is
   ** subordinate to, or optionally equal to, this DN).
   ** <br>
   ** The <code>null</code> DN will be considered an ancestor for all other.
   **
   ** @param  other              the DN for which to make the determination.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    <code>true</code> if this DN may be considered
   **                            an ancestor of the provided DN, or
   **                            <code>false</code> if not.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean ancestorOf(final DirectoryName other) {
    int thatSize = other.getRdns().size();
    int thisSize = this.getRdns().size();
    if (thisSize == 0) {
      // this DN must be the null DN, which is an ancestor for all other DNs
      // (and equal to the null DN, which we may still classify as being an
      // ancestor).
      return thatSize == 0;
    }
    if (thisSize >= thatSize) {
      // this DN has more RDN components than the provided DN, so it can't
      // possibly be an ancestor, or has the same number of components and equal
      // DN's shouldn't be considered ancestors.
      return false;
    }
    while (thisSize > 0) {
      if (!getRdn(--thisSize).equals(other.getRdn(--thatSize))) {
        return false;
      }
    }
    // if we've gotten here, then we can consider this DN to be an ancestor of
    // the provided DN.
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descendantOf
  /**
   ** Indicates whether the DN represented by the first string is a descendant
   ** of the DN represented by the second string.
   ** <br>
   ** The first DN will be considered a descendant of the second DN if the list
   ** of RDN components for first DN ends with the elements that comprise the
   ** RDN components for the second DN (i.e., if this DN is subordinate to, or
   ** optionally equal to, the provided DN).
   ** <br>
   ** The null DN will not be considered a descendant for any other DNs.
   ** <br>
   ** For example, the following section of code determines if the DN specified
   ** by <code>dn1</code> is a descendant of the DN specified by
   ** <code>dn2</code>.
   ** <pre>
   **    DN dn1 = new DN("uid=bjensen, ou=People, o=Airius.com");
   **    DN dn2 = new DN("ou=People, o=Airius.com");
   **
   **    boolean descendantOf = dn1.descendantOf(dn2)
   ** </pre>
   ** In this case, since "uid=bjensen, ou=People, o=Airius.com" is an entry
   ** under the subtree "ou=People, o=Airius.com", the value of
   ** <code>descendantOf</code> is true.
   ** <br>
   ** In the case where the given DN is equal to this DN it returns
   ** <code>false</code>.
   **
   ** @param  lhs                the string representation of the first DN for
   **                            which to make the determination.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the string representation of the second DN for
   **                            which to make the determination.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the first DN may be
   **                            considered a descendant of the second DN.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  public static boolean descendantOf(final String lhs, final String rhs)
    throws DirectoryException {

    return build(lhs).descendantOf(build(rhs));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descendantOf
  /**
   ** Indicates whether this DN is a descendant of the provided DN.
   ** <br>
   ** It will be considered a descendant of the provided DN if the list of RDN
   ** components for this DN ends with the elements that comprise the RDN
   ** components for the provided DN (i.e., if this DN is subordinate to, or
   ** optionally equal to, the provided DN).
   ** <br>
   ** The null DN will not be considered a descendant for any other DNs.
   ** <br>
   ** For example, the following section of code determines if the DN specified
   ** by <code>dn1</code> is a descendant of the DN specified by
   ** <code>dn2</code>.
   ** <pre>
   **    DN dn1 = new DN("uid=bjensen, ou=People, o=Airius.com");
   **    DN dn2 = new DN("ou=People, o=Airius.com");
   **
   **    boolean descendantOf = dn1.descendantOf(dn2)
   ** </pre>
   ** In this case, since "uid=bjensen, ou=People, o=Airius.com" is an entry
   ** under the subtree "ou=People, o=Airius.com", the value of
   ** <code>descendantOf</code> is true.
   ** <br>
   ** In the case where the given DN is equal to this DN it returns
   ** <code>false</code>.
   **
   ** @param  other              the DN of a subtree to check
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the current DN is a
   **                            descendant of the DN specified by
   **                            <code>dn</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  public boolean descendantOf(final String other)
    throws DirectoryException {

    return descendantOf(build(other));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descendantOf
  /**
   ** Indicates whether this DN is a descendant of the provided DN.
   ** <br>
   ** It will be considered a descendant of the provided DN if the list of RDN
   ** components for this DN ends with the elements that comprise the RDN
   ** components for the provided DN (i.e., if this DN is subordinate to, or
   ** optionally equal to, the provided DN).
   ** <br>
   ** The null DN will not be considered a descendant for any other DNs.
   ** <br>
   ** For example, the following section of code determines if the DN specified
   ** by <code>dn1</code> is a descendant of the DN specified by
   ** <code>dn2</code>.
   ** <pre>
   **    DN dn1 = new DN("uid=bjensen, ou=People, o=Airius.com");
   **    DN dn2 = new DN("ou=People, o=Airius.com");
   **
   **    boolean descendantOf = dn1.descendantOf(dn2)
   ** </pre>
   ** In this case, since "uid=bjensen, ou=People, o=Airius.com" is an entry
   ** under the subtree "ou=People, o=Airius.com", the value of
   ** <code>descendantOf</code> is true.
   ** <br>
   ** In the case where the given DN is equal to this DN it returns
   ** <code>false</code>.
   **
   ** @param  other              the DN of a subtree to check
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    <code>true</code> if the current DN is a
   **                            descendant of the DN specified by
   **                            <code>dn</code>.
   */
  public boolean descendantOf(final DirectoryName other) {
    int thatSize = other.getRdns().size() - 1;
    int thisSize = this.getRdns().size()  - 1;
    if (thatSize < 0) {
      // the provided DN must be the null DN, which will be considered an
      // ancestor for all other DNs (and equal to the null DN), making this DN
      // considered a descendant for that DN.
      return thisSize >= 0;
    }
    if ((thisSize < thatSize) || (thisSize == thatSize)) {
      // this DN has fewer DN components than the provided DN, so it can't
      // possibly be a descendant, or it has the same number of components and
      // equal DNs shouldn't be considered descendants.
      return false;
    }
    while (thatSize >= 0) {
      if (!getRdn(thisSize--).equals(other.getRdn(thatSize--))) {
        return false;
      }
    }
    // if we've gotten here, then we can consider this DN to be a descendant of
    // the provided DN.
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absolute
  /**
   ** Appand the root context name to this <code>DirectoryName</code>.
   **
   ** @param  base               the base context <code>DirectoryName</code>.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}
   **
   ** @return                    the relative distinguished name without the
   **                            root context
   **                            <br>
   **                            Possible object is {@link DirectoryName}
   */
  public DirectoryName absolute(final DirectoryName base) {
    // if this name is empty
    if (this.size() == 0)
      return base;

    // if all distinguished names has to be handled absolute or the root context
    // is empty return the passed value immediatly
    if (base == null || base.size() == 0)
      return this;
    // if the root context is not contained in the FQDN return the name as it is
    // if the root context is defined then remove this part from the
    // distinguished name
    return (startsWith(base)) ? suffix(base.size()) : this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relative
  /**
   ** Remove the root context name from this <code>DirectoryName</code>.
   **
   ** @param  base               the base context <code>DirectoryName</code>.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}
   **
   ** @return                    the relative distinguished name without the
   **                            root context
   **                            <br>
   **                            Possible object is {@link DirectoryName}
   */
  public DirectoryName relative(final DirectoryName base) {
    // if this name is empty
    if (this.size() == 0)
      return base;

    // if all distinguished names has to be handled absolute or the root context
    // is empty return the passed value immediatly
    if (base == null || base.size() == 0)
      return this;

    // if the root context is not contained in the FQDN return the name as it is
    // if the root context is defined then remove this part from the
    // distinguished name
    return (startsWith(base)) ? prefix(base.size()) : this;
  }
}