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

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Identity Provider Discovery

    File        :   Provider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Provider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.idp.type;

import java.util.Comparator;

////////////////////////////////////////////////////////////////////////////////
// class Provider
// ~~~~~ ~~~~~~~~
/**
 ** A class that performs provides the configuration of a certain Identity
 ** Provider.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 **   &lt;complexType name="provider"&gt;
 **     &lt;complexContent&gt;
 **       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **         &lt;sequence&gt;
 **           &lt;element name="id"      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **           &lt;element name="name"    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **           &lt;element name="symbol"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **           &lt;element name="partner" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;/sequence&gt;
 **       &lt;/restriction&gt;
 **     &lt;/complexContent&gt;
 **   &lt;/complexType&gt;
 * </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Provider {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String id;
  protected String name;
  protected String symbol;
  protected String partner;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // class Name
  // ~~~~~ ~~~~
  /**
   ** A comparison function, which imposes a <i>total ordering</i> on some
   ** collection of <code>Provider</code>s.
   ** <p>
   ** Comparators can be passed to a sort method (such as
   ** {@link Collections#sort(List,Comparator) Collections.sort} or
   ** {@link Arrays#sort(Object[],Comparator) Arrays.sort}) to allow precise
   ** control over the sort order.
   ** <br>
   ** Comparators can also be used to control the order of certain data
   ** structures (such as {@link SortedSet sorted sets} or
   ** {@link SortedMap sorted maps}), or to provide an ordering for collections
   ** of objects that don't have a {@link Comparable natural ordering}.
   */
  public static class Name implements Comparator<Provider> {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Name</code> {@link Comparator} that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Name() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compare
    /**
     ** Compares its two arguments for order.
     ** Returns a negative integer, zero, or a positive integer as the first
     ** argument is less than, equal to, or greater than the second.
     ** <p>
     ** In the foregoing description, the notation
     ** <code>sgn(</code><i>expression</i><code>)</code> designates the
     ** mathematical <i>signum</i> function, which is defined to return one of
     ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
     ** the value of <i>expression</i> is negative, zero or positive.
     ** <p>
     ** It is generally the case, but <i>not</i> strictly required that
     ** <code>(compare(x, y) == 0) == (x.equals(y))</code>. Generally speaking,
     ** any comparator that violates this condition should clearly indicate this
     ** fact. The recommended language is "Note: this comparator imposes
     ** orderings that are inconsistent with equals."
     **
     ** @param  lhs              the first object to be compared.
     ** @param  rhs              the second object to be compared.
     **
     ** @return                  a negative integer, zero, or a positive integer
     **                          as the first argument is less than, equal to,
     **                          or greater than the second.
     ** @throws NullPointerException if an argument is <code>null</code> and
     **                              this comparator does not permit
     **                              <code>null</code> arguments.
     ** @throws ClassCastException   if the arguments' types prevent them from
     **                              being compared by this comparator.
     */
    public int compare(final Provider lhs, final Provider rhs) {
      return lhs.name.compareTo(rhs.name);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Provider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
   ** Returns the identifier of the Identity Provider to identify the provider
   ** internally.
   ** <p>
   ** Getter is required to access the property inside of a JSP.
   **
   ** @return                    the identifier of the Identity Provider to
   **                            identify the provider internally.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String getId() {
    return id();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the Identity Provider to identify the provider
   ** internally.
   **
   ** @return                    the identifier of the Identity Provider to
   **                            identify the provider internally.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the name of the Identity Provider to identify the provider
   ** public.
   ** <p>
   ** Getter is required to access the property inside of a JSP.
   **
   ** @return                    the name of the Identity Provider to
   **                            identify the provider public.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String getName() {
    return name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the Identity Provider to identify the provider
   ** public.
   **
   ** @return                    the name of the Identity Provider to
   **                            identify the provider public.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSymbol
  /**
   ** Returns the name of a symbol to identify the provider by end-users.
   ** <p>
   ** Getter is required to access the property inside of a JSP.
   **
   ** @return                    the name of a symbol to identify the provider
   **                            by end-users.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String getSymbol() {
    return symbol();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   symbol
  /**
   ** Returns the name of a symbol to identify the provider by end-users.
   **
   ** @return                    the name of a symbol to identify the provider
   **                            by end-users.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String symbol() {
    return this.symbol;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPartner
  /**
   ** Returns the name contain the IdP's ProviderID/Issuer onfiguration when
   ** redirecting the user back to the SP.
   ** <p>
   ** Getter is required to access the property inside of a JSP.
   **
   ** @return                    the name contain the IdP's ProviderID/Issuer
   **                            onfiguration when redirecting the user back to
   **                            the SP.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String getPartner() {
    return partner();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partner
  /**
   ** Returns the partner contain the IdP's ProviderID/Issuer configuration when
   ** redirecting the user back to the SP.
   **
   ** @return                    the partner contain the IdP's ProviderID/Issuer
   **                            configuration when redirecting the user back to
   **                            the SP.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String partner() {
    return this.partner;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Provider</code>.
   **
   ** @return                    an newly created instance of
   **                            <code>Provider</code>.
   **                            Possible object <code>Provider</code>.
   */
  public static Provider build() {
    return new Provider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Provider</code> that takes an id, a
   ** symbol and a partner.
   **
   ** @param  id                 the identifier of the Identity Provider to
   **                            identify the provider internally.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  name               the name of the Identity Provider to identify
   **                            the provider public.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  symbol             the name of a symbol to identify the provider
   **                            by end-users.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  partner            the partner contain the IdP's ProviderID/Issuer
   **                            configuration when redirecting the user back to
   **                            the SP.
   **                            <br>
   **                            Allowed object {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>Provider</code>.
   **                            Possible object <code>Provider</code>.
   */
  public static Provider of(final String id, final String name, final String symbol, final String partner) {
    final Provider provider = new Provider();
    provider.id      = id;
    provider.name    = name;
    provider.symbol  = symbol;
    provider.partner = partner;
    return provider;
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
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Pair</code>s are considered equal if and only if they represent
   ** the same encoded, decoded and template value. As a consequence, two given
   ** <code>Pair</code>s may be different even though they contain the same
   ** attribute value.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    // test identity
    if (this == other)
      return true;

    // test for null and exact class matches
    if (other == null || getClass() != other.getClass())
      return false;

    final Provider that = (Provider)other;
    return this.id.equals(that.id);
  }
}