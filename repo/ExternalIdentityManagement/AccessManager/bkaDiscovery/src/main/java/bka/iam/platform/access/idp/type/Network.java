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
    Subsystem   :   Identity Network Discovery

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Network.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.idp.type;

////////////////////////////////////////////////////////////////////////////////
// class Network
// ~~~~~ ~~~~~~~~
/**
 ** A class that provides the configuration of a certain Identity Network.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 **   &lt;complexType name="network"&gt;
 **     &lt;complexContent&gt;
 **       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **         &lt;sequence&gt;
 **           &lt;element name="subnet" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **           &lt;element name="id"     type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;/sequence&gt;
 **       &lt;/restriction&gt;
 **     &lt;/complexContent&gt;
 **   &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Network {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String cidr;
  protected String partner;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Network</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Network() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cidr
  /**
   ** Returns the identifier of the <code>Network</code>.
   **
   ** @return                    the identifier of the <code>Network</code>.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public String cidr() {
    return this.cidr;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partner
  /**
   ** Returns the IdP's NetworkID/Issuer configuration when redirecting the user
   ** back to the SP.
   **
   ** @return                    the IdP's NetworkID/Issuer configuration when
   **                            redirecting the user back to the SP.
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
   ** Factory method to create an empty <code>Network</code>.
   **
   ** @return                    an newly created instance of
   **                            <code>Network</code>.
   **                            Possible object <code>Network</code>.
   */
  public static Network build() {
    return new Network();
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
    return this.cidr.hashCode();
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

    final Network that = (Network)other;
    return this.cidr.equals(that.cidr);
  }
}