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

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   AuthorizationContext.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AuthorizationContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.jacc;

import java.util.Objects;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class AuthorizationContext
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** An interface for classes which authorize principal objects.
 **
 ** @param  <P>                  the type of principals the authorizer grants
 **                              access to.
 **                              <br>
 **                              Allowed object is <code>&lt;P&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AuthorizationContext<P extends Principal> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String                  role;
  private final P                       principal;
  private final ContainerRequestContext request;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Default
  // ~~~~~ ~~~~~~~
  /**
   ** The default implementation of {@link AuthorizationContext}, which uses a
   ** {@link Principal}, a role and a {@link ContainerRequestContext} to
   ** temporarily cache principals' role associations.
   **
   ** @param  <P>                the type of principals the authorizer grants
   **                            access to.
   **                            <br>
   **                            Allowed object is <code>&lt;P&gt;</code>.
   */
  public static class Default<P extends Principal> extends AuthorizationContext<P> {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Default</code> {@link AuthorizationContext}.
     **
     ** @param  principal        a {@code Principal} object, representing a
     **                          user.
     **                          Allowed object is {@code Principal}.
     ** @param  role             a role a {@code Principal} <b>must</b> have to
     **                          be authorized.
     **                          Allowed object is {@link String}.
     ** @param  request          the context of the incomming request.
     **                          <br>
     **                          Allowed object is
     **                          {@link ContainerRequestContext}.
     */
    Default(final P principal, final String role, final ContainerRequestContext request) {
      // ensure inheritance
      super(principal, role, request);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthorizationContext</code>
   **
   ** @param  principal          a {@code Principal} object, representing a
   **                            user.
   **                            Allowed object is {@code Principal}.
   ** @param  role               a role a {@code Principal} <b>must</b> have to
   **                            be authorized.
   **                            Allowed object is {@link String}.
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   */
  protected AuthorizationContext(final P principal, final String role, final ContainerRequestContext request) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.role      = role;
    this.request   = request;  
    this.principal = principal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principal
  /**
   ** Returns the <code>principal</code> property of the
   ** <code>AuthorizationContext</code>.
   **
   ** @return                    the <code>principal</code> property of the
   **                            <code>AuthorizationContext</code>.
   **                            <br>
   **                            Possible object is <code>P</code>.
   */
  public final P principal() {
    return this.principal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the <code>role</code> property of the
   ** <code>AuthorizationContext</code>.
   **
   ** @return                    the <code>role</code> property of the
   **                            <code>AuthorizationContext</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String role() {
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Returns the {@link ContainerRequestContext} property of the
   ** <code>AuthorizationContext</code>.
   **
   ** @return                    the {@link ContainerRequestContext} property of
   **                            the <code>AuthorizationContext</code>.
   **                            <br>
   **                            Possible object is {@link ContainerRequestContext}.
   */
  public final ContainerRequestContext request() {
    return this.request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Persistable)

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode
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
   ** The implementation use the identifier of the persisted entity if available
   ** only. This is sufficient because the identifier is the primary key of the
   ** entity.
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.principal, this.role, this.request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>AuthorizationContext</code>s are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>AuthorizationContext</code>s may be different even though they
   ** contain the same set of names with the same values, but in a different order.
   **
   ** @param  other            the reference object with which to compare.
   **                          <br>
   **                          Allowed object is {@link Object}.
   **
   ** @return                  <code>true</code> if this object is the same as
   **                          the object argument; <code>false</code>
   **                          otherwise.
   **                          <br>
   **                          Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final AuthorizationContext that = (AuthorizationContext)other;
    return Objects.equals(this.principal, that.principal)
        && Objects.equals(this.role,      that.role)
        && Objects.equals(this.request,   that.request)
    ;
  }
}