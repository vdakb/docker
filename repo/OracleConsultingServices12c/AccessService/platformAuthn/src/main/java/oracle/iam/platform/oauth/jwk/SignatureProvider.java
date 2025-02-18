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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   SignatureProvider.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SignatureProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.Collections;

import oracle.hst.platform.core.annotation.ThreadSafety;

////////////////////////////////////////////////////////////////////////////////
// abstract class SignatureProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The base class for JSON Web Signature (JWS) signers and verifiers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class SignatureProvider implements SignatureInstance.Provider {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The supported algorithms. */
  private Set<Algorithm> alg;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Filter
  // ~~~~~ ~~~~~~
  /**
   ** JSON Web Signature (JWS) header filter implementation.
   ** <p>
   ** This class is thread-safe.
   */
  @ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
  protected class Filter implements SignatureHeaderFilter {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The supported algorithms. Used to bound the subset of the accepted ones.
     */
    private Set<Algorithm> supported;

    /** The accepted algorithms. */
    private Set<Algorithm> accepted;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Contructs a a new JWS header filter.
     ** <br>
     ** The accepted algorithms are set to equal the specified supported ones.
     **
     ** @param  supported        the supported JWS algorithms.
     **                          <br>
     **                          Used to bound the
     **                          {@link #accepted accepted algorithms}.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link Algorithm}.
     */
    public Filter(final Set<Algorithm> supported) {
      // ensure inheritance
      super();

      // prevent bogus input
      if (supported == null)
        throw new IllegalArgumentException("The supported JWS algorithm set must not be null");

      this.supported = Collections.unmodifiableSet(supported);
      this.accepted  = this.supported;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: acceptedAlgorithm (SignatureHeaderFilter)
    /**
     ** Sets the names of the accepted JWS algorithms.
     ** <p>
     ** These correspond to the <code>alg</code> JWS header parameter.
     **
     ** @param  value            the accepted JWS algorithms.
     **                          <br>
     **                          Must be a subset of the supported algorithms.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link Algorithm}.
     */
    @Override
    public final void acceptedAlgorithm(final Set<Algorithm> value) {
      if (value == null)
        throw new IllegalArgumentException("The accepted JWS algorithm set must not be null");

      if (!this.acceptedAlgorithm().containsAll(value))
        throw new IllegalArgumentException("One or more of the algorithms is not in the supported JWS algorithm set");

      this.accepted = Collections.unmodifiableSet(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: acceptedAlgorithm (SignatureHeaderFilter)
    /**
     ** Returns the names of the accepted JWS algorithms.
     ** <p>
     ** These correspond to the <code>alg</code> JWS header parameter.
     **
     ** @return                  the accepted JWS algorithms as a read-only set,
     **                          empty set if none.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Algorithm}.
     */
    @Override
    public final Set<Algorithm> acceptedAlgorithm() {
      return this.supported;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a JWS provider.
   **
   ** @param  algorithm          the supported JWS algorithms.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Algorithm}.
   **
   ** @throws IllegalArgumentException if <code>secret</code> is
   **                                  <code>null</code> or empty.
   */
  public SignatureProvider(final Set<Algorithm> algorithm) {
    // ensure inheritance
    super();

    // prvent bogus input
    if (algorithm == null)
      throw new IllegalArgumentException("The supported JWS algorithm set must not be null");

    // initialize instance attributes
    this.alg = Collections.unmodifiableSet(algorithm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supported (Signature.Provider)
  /**
   ** Returns the names of the supported JWS algorithms.
   ** <br>
   ** These correspond to the <code>alg</code> JSON Web Signature header
   ** parameter.
   **
   ** @return                  the collection of supported JSON Web Signature
   **                          algorithms, empty set if none.
   **                          <br>
   **                          Possible object is {@link Set} where each
   **                          element is of type {@link Algorithm}.
   */
  public final Set<Algorithm> supported() {
    return this.alg;
  }
}