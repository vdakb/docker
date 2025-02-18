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

    File        :   Authorizer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Authorizer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.jacc;

import java.security.Principal;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.container.ContainerRequestContext;

////////////////////////////////////////////////////////////////////////////////
// interface Authorizer
// ~~~~~~~~~ ~~~~~~~~~~
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
public interface Authorizer<P extends Principal> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class PermitAll
  // ~~~~~ ~~~~~~~~~
  /**
   ** An {@link Authorizer} decorator that grants access for any principal in
   ** any role.
   **
   ** @param  <P>                the type of principals the authorizer grants
   **                            access to.
   **                            <br>
   **                            Allowed object is <code>&lt;P&gt;</code>.
   */
  static class PermitAll<P extends Principal> implements Authorizer<P> {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>PermitAll</code> allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    PermitAll() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: authorize
    /**
     ** Decides if access is granted for the given principal in the given role.
     **
     ** @param  principal        a {@link Principal} object, representing a
     **                          user.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     ** @param  role             a role a {@link Principal} <b>must</b> have to
     **                          be authorized.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  <code>true</code>, if the access is granted;
     **                          otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean authorize(final P principal, final String role) {
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class DenyAll
  // ~~~~~ ~~~~~~~
  /**
   ** An {@link Authorizer} decorator that denaies access for any principal in
   ** any role.
   **
   ** @param  <P>                the type of principals the authorizer grants
   **                            access to.
   **                            <br>
   **                            Allowed object is <code>&lt;P&gt;</code>.
   */
  static class DenyAll<P extends Principal> implements Authorizer<P> {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>DenyAll</code> allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    DenyAll() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: authorize
    /**
     ** Decides if access is granted for the given principal in the given role.
     **
     ** @param  principal        a {@link Principal} object, representing a
     **                          user.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     ** @param  role             a role a {@link Principal} <b>must</b> have to
     **                          be authorized.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  <code>true</code>, if the access is granted;
     **                          otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean authorize(final P principal, final String role) {
      return false;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Cached
  // ~~~~~ ~~~~~~
  /**
   ** An {@link Authorizer} decorator which uses a cache to temporarily cache
   ** principals' role associations.
   ** <p>
   ** Cache entries include both inclusion and exclusion of a principal
   ** within a given role.
   **
   ** @param  <P>                the type of principals the authorizer grants
   **                            access to.
   **                            <br>
   **                            Allowed object is <code>&lt;P&gt;</code>.
   */
  static class Cached<P extends Principal> implements Authorizer<P> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Authorizer<P>            delegate;

    // A cache which maps (principal, role, uriInfo) to boolean authorization
    // states.
    //
    // A cached value of true indicates that the key's principal is authorized
    // to assume the given role. False values indicate the principal is not
    // authorized to assume the role.
    //
    // null cache values are interpreted as cache misses, and will thus result
    // in read through to the underlying Authorizer.
    //
    // Field is package-private to be visible for unit tests
    //final Loading<AuthorizationContext<P>, Boolean> cache;
    final ConcurrentMap<AuthorizationContext<P>, Boolean> cache;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Cached</code> {@link Authorizer}.
     **
     ** @param  delegate         the delegated {@link Authorizer}.
     **                          Allowed object is {@link Authorizer}.
     */
    Cached(final Authorizer<P> delegate) {
      // ensure inheritance
      super();

      // initalialize instance attributes
      this.delegate = delegate;
      this.cache    = new ConcurrentHashMap<>();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: authorize
    /**
     ** Decides if access is granted for the given principal in the given role.
     **
     ** @param  principal        a {@link Principal} object, representing a
     **                          user.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     ** @param  role             a role a {@link Principal} <b>must</b> have to
     **                          be authorized.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  <code>true</code>, if the access is granted;
     **                          otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean authorize(final P principal, final String role) {
      return authorize(principal, role, null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////
 
    ////////////////////////////////////////////////////////////////////////////
    // Method: invalidate
    /**
     ** Discards any cached role associations for the given principal and role.
      **
     ** @param  principal        a {@link Principal} object, representing a
     **                          user.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     ** @param  role             a role a {@link Principal} <b>must</b> have to
     **                          be authorized.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  request          the context of the incomming request.
     **                          <br>
     **                          Allowed object is
     **                          {@link ContainerRequestContext}.
     */
    public void invalidate(final P principal, final String role, final ContainerRequestContext request) {
//      this.cache.invalidate(context(principal, role, request));
      this.cache.remove(context(principal, role, request));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Factory method to create an {@link AuthorizationContext} object, to be
   ** used in {@link Authorizer.Cached} as cache key.
   **
   ** @param  principal          a {@link Principal} object, representing a
   **                            user.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  role               a role a {@link Principal} <b>must</b> have to
   **                            be authorized.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   **
   ** @return                    {@link AuthorizationContext} object, to be
   **                            used in {@link Authorizer.Cached}.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  default AuthorizationContext<P> context(final P principal, final String role, final ContainerRequestContext request) {
    return new AuthorizationContext.Default<P>(principal, role, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorize
  /**
   ** Decides if access is granted for the given principal in the given role.
   **
   ** @param  principal          a {@link Principal} object, representing a
   **                            user.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  role               a role a {@link Principal} <b>must</b> have to
   **                            be authorized.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   **
   ** @return                    <code>true</code>, if the access is granted;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  default boolean authorize(final P principal, final String role, final ContainerRequestContext request) {
     return authorize(principal, role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorize
  /**
   ** Decides if access is granted for the given principal in the given role.
   **
   ** @param  principal          a {@link Principal} object, representing a
   **                            user.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  role               a role a {@link Principal} <b>must</b> have to
   **                            be authorized.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code>, if the access is granted;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean authorize(final P principal, final String role);
}