/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   BearerAssertionVerifier.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BearerAssertionVerifier.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity;

import java.util.Set;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Collections;

import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.proc.ClockSkewAware;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;

import com.nimbusds.jose.proc.SecurityContext;

import oracle.hst.platform.core.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// class BearerAssertionVerifier
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A {@link JWTClaimsSetVerifier JWT claims verifier} implementation.
 ** <p>
 ** Configurable checks:
 ** <ol>
 **   <li>Specify JWT claims that must be present and which values must match
 **       exactly, for example the expected JWT issuer ("iss").
 **   <li>Specify JWT claims that must be present, for example expiration
 **       ("exp") and not-before ("nbf") times. If the "exp" or "nbf" claims
 **       are marked as required they will be automatically checked against
 **       the current time.
 **   <li>Specify JWT claims that are prohibited, for example to prevent
 **       cross-JWT confusion in situations when explicit JWT typing via the
 **       type ("typ") header is not used.
 ** </ol>
 ** Performs the following time validity checks:
 ** <ol>
 **   <li>If an expiration time ("exp") claim is present, makes sure it is ahead
 **       of the current time, else the JWT claims set is rejected.
 **   <li>If a not-before-time ("nbf") claim is present, makes sure it is before
 **       the current time, else the JWT claims set is rejected.
 ** </ol>
 ** <b>Note</b>:
 ** <br>
 ** To enforce a time validity check the claim ("exp" and / or "nbf" ) must be
 ** set as required.
 ** <p>
 ** Example verifier with exact matches for "iss" and "aud", and setting the
 ** "exp", "nbf" and "jti" claims as required to be present:
 ** <pre>
 **   BearerAssertionVerifier&lt;?&gt; verifier = new BearerAssertionVerifier&lt;&gt;(
 ** 	  new JWTClaimsSet.Builder()
 ** 		  .issuer("https://issuer.example.com")
 **   		.audience("https://client.example.com")
 ** 		  .build()
 ** 	, new HashSet&lt;&gt;(Arrays.asList("exp", "nbf", "jti"))
 **   );
 **   verifier.verify(jwtClaimsSet, null);
 ** </pre>
 ** The {@link #currentTime()} method can be overridden to use an alternative
 ** time provider for the "exp" (expiration time) and "nbf" (not-before time)
 ** verification, or to disable "exp" and "nbf" verification entirely.
 ** <p>
 ** This class may be extended to perform additional checks.
 ** <p>
 ** This class is thread-safe.
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class BearerAssertionVerifier implements JWTClaimsSetVerifier<SecurityContext>
                                     ,          ClockSkewAware {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

	/** The default maximum acceptable clock skew, in seconds (60). */
	public static final int   CLOCK_SKEW_MAX_DEFAULT = 60;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private int                clockSkewMax           = CLOCK_SKEW_MAX_DEFAULT;

	/**
	 ** The names of the JWT claims that must be present, empty set if none.
	 */
	private final Set<String>  required;

	/**
	 ** The JWT claims that must match exactly, empty set if none.
	 */
	private final JWTClaimsSet exactMatch;

	/**
	 ** The accepted audience values, <code>null</code> if not specified.
   ** A <code>null</code> value present in the set allows JWTs with no audience.
	 */
	private final Set<String>  audience;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link BearerAssertionMechanism} that allows use as a
   ** JavaBean.
   **
	 ** @param  exactMatch         the JWT claims that must match exactly,
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link JWTClaimsSet}.
	 ** @param  required           the names of the JWT claims that must be
   **                            present, empty set or <code>null</code> if
   **                            none.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of typr {@link String}.
	 ** @param  audience           the audience claims that must be
   **                            present, empty set or <code>null</code> if
   **                            none.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   */
  private BearerAssertionVerifier(final JWTClaimsSet exactMatch, final Set<String> required, final List<String> audience) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.audience   = audience != null   ? new HashSet<String>(audience) : null;
    this.exactMatch = exactMatch != null ? exactMatch : new JWTClaimsSet.Builder().build();
    
    final Set<String> copy = new HashSet<>(required);
		if (!this.exactMatch.getClaims().isEmpty()) {
			copy.addAll(this.exactMatch.getClaims().keySet());
		}
		this.required = Collections.unmodifiableSet(copy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   audience
	/**
	 ** Returns the accepted audience values.
	 **
   ** @return                    the accepted JWT audience values,
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            A <code>null</code> value in the set allows
   **                            JWTs with no audience.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            elementis of type {@link String}.
	 */
	public Set<String> audience() {
		return this.audience;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
	/**
	 ** Returns the names of the JWT claims that must be present, including the
   ** name of those that must match exactly.
	 **
   ** @return                    the names of the JWT claims that must be
   **                            present, empty set if none.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            elementis of type {@link String}.
	 */
	public Set<String> required() {
		return this.required;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   currentTime
	/**
	 ** Returns the current time for the purpose of "exp" (expiration time) and
   ** "nbf" (not-before time) claim verification.
   ** <br>
   ** This method can be overridden to inject an alternative time provider (e.g.
   ** for testing purposes) or to disable "exp" and "nbf" verification.
	 **
   ** @return                    the current time or <code>null</code> to
   **                            disable "exp" and "nbf" claim verification
   **                            entirely.
   **                            <br>
   **                            Possible object is {@link Date}.
	 */
	protected Date currentTime() {
		return new Date();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxClockSkew (ClockSkewAware)
	/**
	 ** Sets the maximum acceptable clock skew.
	 **
   ** @param  value              the maximum acceptable clock skew, in seconds.
   **                            <br>
   **                            Zero if none.
   **                            <br>
   **                            Allowed object is <code>int</code>.
	 */
	@Override
	public void setMaxClockSkew(final int value) {
		this.clockSkewMax = value;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxClockSkew (ClockSkewAware)
	/**
	 ** Returns the maximum acceptable clock skew.
	 **
   ** @return                    the maximum acceptable clock skew, in seconds.
   **                            <br>
   **                            Zero if none.
   **                            <br>
   **                            Possible object is <code>int</code>.
	 */
	@Override
	public int getMaxClockSkew() {
		return this.clockSkewMax;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxClockSkew (ClockSkewAware)
	/**
	 ** Verifies selected or all claims from the specified JWT claims set.
   **
   ** @param  claim              the JWT claims set.
   **                            <br>
   **                            Allowed object is {@link JWTClaimsSet}.
	 ** @param  context            the optional context.
   **                            <br>
   **                            Allowed object is {@link SecurityContext}.
	 **
   ** @throws AssertionException if the JWT claims set is rejected.
	 */
  @Override
  public void verify(final JWTClaimsSet claim, final SecurityContext context)
    throws AssertionException {

    // check if all required claims are present
    if (!claim.getClaims().keySet().containsAll(this.required)) {
			final Set<String> missing = new HashSet<>(this.required);
			missing.removeAll(claim.getClaims().keySet());
      throw AssertionException.requireClaim(missing);
    }

    // check audience
		if (this.audience != null) {
			final List<String> audience = claim.getAudience();
			if (audience != null && ! audience.isEmpty()) {
				if (!audience.containsAll(this.audience)) {
					throw AssertionException.rejectAudience(audience);
				}
			}
      else if (!this.audience.isEmpty()) {
				throw AssertionException.requireAudience();
			}
    }

    // check time window
		final Date now = currentTime();
		if (now != null) {
			final Date exp = claim.getExpirationTime();
			if (exp != null) {
				if (!DateUtility.after(exp, now, this.clockSkewMax)) {
					throw AssertionException.tokenExpired();
				}
			}
			final Date nbf = claim.getNotBeforeTime();
			if (nbf != null) {
				if (!DateUtility.before(nbf, now, this.clockSkewMax)) {
					throw AssertionException.tokenUsedBefore();
				}
			}
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>BearerAssertionVerifier</code> with the
   ** JWT claims that must match exactly and the names of the JWT claims that
   ** must be present.
   **
	 ** @param  exactMatch         the JWT claims that must match exactly,
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link JWTClaimsSet}.
	 ** @param  required           the names of the JWT claims that must be
   **                            present, empty set or <code>null</code> if
   **                            none.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of typr {@link String}.
   **
   ** @return                    the <code>BearerAssertionVerifier</code>
   **                            populated with the given values.
   **                            <br>
   **                            Possible object is
   **                            <code>BearerAssertionVerifier</code>.
   */
  public static BearerAssertionVerifier build(final JWTClaimsSet exactMatch, final Set<String> required) {
    return new BearerAssertionVerifier(exactMatch, required, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>BearerAssertionVerifier</code> with the
   ** JWT claims that must match exactly, the names of the JWT claims that must
   ** be present and the audiencs claims that must be present.
   **
	 ** @param  exactMatch         the JWT claims that must match exactly,
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link JWTClaimsSet}.
	 ** @param  required           the names of the JWT claims that must be
   **                            present, empty set or <code>null</code> if
   **                            none.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of typr {@link String}.
	 ** @param  audience           the audience claims that must be
   **                            present, empty set or <code>null</code> if
   **                            none.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of typr {@link String}.
   **
   ** @return                    the <code>BearerAssertionVerifier</code>
   **                            populated with the given values.
   **                            <br>
   **                            Possible object is
   **                            <code>BearerAssertionVerifier</code>.
   */
  public static BearerAssertionVerifier build(final JWTClaimsSet exactMatch, final Set<String> required, final String audience) {
    return build(exactMatch, required, Collections.<String>singletonList(audience));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>BearerAssertionVerifier</code> with the
   ** JWT claims that must match exactly, the names of the JWT claims that must
   ** be present and the audiencs claims that must be present.
   **
	 ** @param  exactMatch         the JWT claims that must match exactly,
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link JWTClaimsSet}.
	 ** @param  required           the names of the JWT claims that must be
   **                            present, empty set or <code>null</code> if
   **                            none.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of typr {@link String}.
	 ** @param  audience           the audience claims that must be
   **                            present, empty set or <code>null</code> if
   **                            none.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of typr {@link String}.
   **
   ** @return                    the <code>BearerAssertionVerifier</code>
   **                            populated with the given values.
   **                            <br>
   **                            Possible object is
   **                            <code>BearerAssertionVerifier</code>.
   */
  public static BearerAssertionVerifier build(final JWTClaimsSet exactMatch, final Set<String> required, final List<String> audience) {
    return new BearerAssertionVerifier(exactMatch, required, audience);
  }
}