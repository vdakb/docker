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

    File        :   Authenticator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Authenticator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.jacc;

import java.util.Optional;

import java.security.Principal;

////////////////////////////////////////////////////////////////////////////////
// interface Authenticator
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** An interface for classes which authenticate user-provided credentials and
 ** return principal objects.
 **
 ** @param  <C>                  the type of credentials the authenticator can
 **                              authenticate.
 **                              <br>
 **                              Allowed object is <code>&lt;C&gt;</code>.
 ** @param  <P>                  the type of principals the authenticator
 **                              returns.
 **                              <br>
 **                              Allowed object is <code>&lt;P&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Authenticator<C, P extends Principal> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticate
  /**
   ** Given a set of user-provided credentials, return an optional principal.
   ** <p>
   ** If the credentials are valid and map to a principal, returns an
   ** {@link Optional#of(Object)}.
   ** <p>
   ** If the credentials are invalid, returns an {@link Optional#empty()}.
   **
   ** @param  credentials        a set of user-provided credentials.
   **                            <br>
   **                            Allowed object is {@link Credential}.
   **
   ** @return                    either an authenticated principal or an absent
   **                            optional.
   **                            <br>
   **                            Possible object is {@link Optional} wrapping
   **                            type <code>P</code>.
   **
   ** @throws AuthenticationException if the credentials cannot be authenticated
   **                                 due to an underlying error.
   */
  Optional<P> authenticate(final Credential credentials)
    throws AuthenticationException;
}