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

    File        :   DirectoryFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
////////////////////////////////////////////////////////////////////////////////
// class DirectoryFilter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>DirectoryFilter</code> provides the encapsulates an LDAP filter.
 ** <p>
 ** An instance of this class is consists of an optional entry DN and an
 ** optional native LDAP filter. The semantics of such an instance is
 ** "an LDAP entry with this entry DN (if specified) and matching this native
 ** filter (if specified)".
 ** <p>
 ** The problem this class solves is the following.
 ** <br>
 ** When an attribute, for instance <code>Name</code>, is mapped to an entry's
 ** DN, and the user constructs an <code>equal</code> for that attribute, the
 ** connector needs to translate that <code>Filter</code> into a native LDAP
 ** filter. The connector could use the <code>entryDN</code> attribute in the
 ** native filter, but some servers might not support that attribute. Instead,
 ** such a <code>Filter</code> is translated to an <code>DirectoryFilter</code>
 ** with that entry DN. A composed filter, for instance:
 ** <pre>
 **   Name name = new Name("uid=foo,dc=example,dc=com");
 **   Attribute attr = AttributeBuilder.build("foo", "bar");
 **   FilterFactory.and(FilterFactory.eq(name), FilterFactory.equalTo(attr));
 ** </pre>
 ** can be translated to a single <code>DirectoryFilter</code> whose entry DN
 ** corresponds to <code>aName</code> and whose filter string is
 ** <code>(foo=bar)</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryFilter {

  ////////////////////////////////////////////////////////////////////////////////
  // instane attributes
  ////////////////////////////////////////////////////////////////////////////////

  private final String entryDN;
  private final String expression;

  ////////////////////////////////////////////////////////////////////////////////
  // Constructors
  ////////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>DirectoryFilter</code> that provides the syntax
   ** capabilities for a LDAP search.
   **
   ** @param  expression         the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entryDN            the ... ???
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private DirectoryFilter(final String expression, final String entryDN) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.expression = expression;
    this.entryDN    = entryDN;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  ////////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   expression
  /**
   ** Returns the expression of this LDAP search filter.
   **
   ** @return                    the expression of this LDAP search filter.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String expression() {
    return this.expression;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   entryDN
  /**
   ** Returns the entry DN of this LDAP search filter.
   **
   ** @return                    the entry DN of this LDAP search filter.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryDN() {
    return this.entryDN;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   with
  /**
   ** Creates a new <code>DirectoryFilter</code> to apply another expression on
   ** the same <code>entryDN</code> as this <code>DirectoryFilter</code>.
   **
   ** @param  expression         the filter expression to apply on
   **                            {@link #entryDN}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the evaluated <code>DirectoryFilter</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryFilter</code>.
   */
  public DirectoryFilter with(final String expression) {
    return new DirectoryFilter(expression, this.entryDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   expression
  /**
   ** Factory method to create a <code>DirectoryFilter</code> with the
   ** specified expression.
   **
   ** @param  expression         the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>DirectoryFilter</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public static DirectoryFilter expression(final String expression) {
    return new DirectoryFilter(expression, null);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   entry
  /**
   ** Factory method to create a <code>DirectoryFilter</code> with the
   ** specified entryDN.
   **
   ** @param  entryDN            the ... ???
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>DirectoryFilter</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public static DirectoryFilter entry(final String entryDN) {
    return new DirectoryFilter(null, entryDN);
  }

  ////////////////////////////////////////////////////////////////////////////////
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
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of
   **       the two objects must produce distinct integer results. However,
   **       the programmer should be aware that producing distinct integer
   **       results for unequal objects may improve the performance of hash
   **       tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return (this.expression != null ? this.expression.hashCode() : 0) ^ (this.entryDN != null ? this.entryDN.hashCode() : 0);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>DirectoryFilter</code> object that
   ** represents the same <code>name</code> and value as this object.
   **
   ** @param  other              the object to compare this
   **                            <code>DirectoryFilter</code> against.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the
   **                            <code>DirectoryFilter</code>s are
   **                            equal; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean equals(final Object other) {
    if ((other instanceof DirectoryFilter)) {
      final DirectoryFilter that = (DirectoryFilter)other;
      return StringUtility.equal(this.expression, that.expression) && StringUtility.equal(this.entryDN, that.entryDN);
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Logically <code>AND</code>s together this expression with another
   ** expression.
   ** If at most one of the two expression has an entry DN, the result is an
   ** expression with that entry DN (if any) and a native expression whos
   ** value is the native expressions of the two expressions
   ** <code>AND</code>ed together using the LDAP <code>&amp;</code> operator.
   **
   ** Build a <code>and</code> conjunction from this
   ** <code>DirectoryFilter</code> and the <code>DirectoryFilter</code>
   ** specified.
   ** <p>
   ** Otherwise, the method returns <code>null</code>.
   **
   ** @param  other              the <code>DirectoryFilter</code> to build the
   **                            <code>and</code> conjunction with.
   **                            <br>
   **                            Allowed object is <code>DirectoryFilter</code>.
   **
   ** @return                    the evaluated <code>DirectoryFilter</code> as
   **                            an <code>and</code> conjunction.
   **                            <br>
   **                            Possible objext is
   **                            <code>DirectoryFilter</code>.
   */
  public DirectoryFilter and(final DirectoryFilter other) {
    if ((this.entryDN == null) || (other.entryDN == null)) {
      return new DirectoryFilter(compose(this.expression, other.expression, '&'), this.entryDN != null ? this.entryDN : other.entryDN);
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Build a <code>or</code> conjunction from this <code>DirectoryFilter</code>
   ** and the <code>DirectoryFilter</code> specified.
   **
   ** @param  other              the <code>DirectoryFilter</code> to build the
   **                            <code>or</code> conjunction with.
   **                            <br>
   **                            Allowed object is <code>DirectoryFilter</code>.
   **
   ** @return                    the evaluated <code>DirectoryFilter</code> as
   **                            an <code>or</code> conjunction.
   **                            <br>
   **                            Possible objext is
   **                            <code>DirectoryFilter</code>.
   */
  public DirectoryFilter or(final DirectoryFilter other) {
    if ((this.entryDN == null) || (other.entryDN == null)) {
      return new DirectoryFilter(compose(this.expression, other.expression, '|'), this.entryDN != null ? this.entryDN : other.entryDN);
    }
    return null;
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Build a <code>or</code> conjunction from this <code>DirectoryFilter</code>
   ** and the <code>DirectoryFilter</code> specified.
   **
   ** @param  expression         the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the evaluated <code>DirectoryFilter</code> as
   **                            an <code>or</code> conjunction.
   **                            <br>
   **                            Possible objext is
   **                            <code>DirectoryFilter</code>.
   */
  public static DirectoryFilter or(final String expression) {
    if (expression != null) {
      return new DirectoryFilter(compose(expression, '|').toString(), null);
    }
    return null;
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Build a <code>or</code> conjunction from this <code>DirectoryFilter</code>
   ** and the <code>DirectoryFilter</code> specified.
   **
   ** @param  expression         the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the evaluated <code>DirectoryFilter</code> as
   **                            an <code>or</code> conjunction.
   **                            <br>
   **                            Possible objext is
   **                            <code>DirectoryFilter</code>.
   */
  public static DirectoryFilter and(final String expression) {
    if (expression != null) {
      return new DirectoryFilter(compose(expression, '&').toString(), null);
    }
    return null;
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Build a <code>or</code> conjunction from this <code>DirectoryFilter</code>
   ** and the <code>DirectoryFilter</code> specified.
   **
   ** @param  expression         the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the evaluated <code>DirectoryFilter</code> as
   **                            an <code>or</code> conjunction.
   **                            <br>
   **                            Possible objext is
   **                            <code>DirectoryFilter</code>.
   */
  public static DirectoryFilter not(final String expression) {
    if (expression != null) {
      return new DirectoryFilter(compose(expression, '!').toString(), null);
    }
    return null;
  }
  
    ////////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Build a <code>or</code> conjunction from this <code>DirectoryFilter</code>
   ** and the <code>DirectoryFilter</code> specified.
   **
   ** @param  expression         the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the evaluated <code>DirectoryFilter</code> as
   **                            an <code>or</code> conjunction.
   **                            <br>
   **                            Possible objext is
   **                            <code>DirectoryFilter</code>.
   */
  public static DirectoryFilter not(final DirectoryFilter expression) {
    if (expression != null) {
      return new DirectoryFilter(compose(expression.expression(), '!').toString(), expression.entryDN != null ? expression.entryDN : null);
    }
    return null;
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** Combines two LDAP search filter expressions by honoring the specified
   ** operator.
   **
   ** @param  lhs                the left hand side expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the right hand side expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operator           the operator to combine <code>lhs</code> and
   **                            <code>rhs</code> to an filter expresssion.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    the evaluated filter expression,
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String compose(final String expression, final char operator) {
    if (expression != null) {
      final StringBuilder builder = new StringBuilder();
      builder.append('(').append(operator).append(expression).append(')');
      return builder.toString();
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** Combines two LDAP search filter expressions by honoring the specified
   ** operator.
   **
   ** @param  lhs                the left hand side expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the right hand side expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operator           the operator to combine <code>lhs</code> and
   **                            <code>rhs</code> to an filter expresssion.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    the evaluated filter expression,
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String compose(final String lhs, final String rhs, final char operator) {
    if (lhs != null) {
      if (rhs != null) {
        final StringBuilder builder = new StringBuilder();
        builder.append('(').append(operator).append(lhs).append(rhs).append(')');
        return builder.toString();
      }
      return lhs;
    }
    return rhs;
  }
}