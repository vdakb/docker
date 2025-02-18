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

    File        :   SignatureHeaderFilter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SignatureHeaderFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;

////////////////////////////////////////////////////////////////////////////////
// interface SignatureHeaderFilter
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** JSON Web Signature (JWS) header filter.
 ** <p>
 ** Specifies accepted JWS algorithms and header parameters.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SignatureHeaderFilter {

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   acceptedAlgorithm
  /**
   ** Sets the names of the accepted JWS algorithms.
   ** <p>
   ** These correspond to the <code>alg</code> JWS header parameter.
   **
   ** @param  value              the accepted JWS algorithms.
   **                            <br>
   **                            Must be a subset of the supported algorithms.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Algorithm}.
   */
  void acceptedAlgorithm(final Set<Algorithm> value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   acceptedAlgorithm
  /**
   ** Returns the names of the accepted JWS algorithms.
   ** <p>
   ** These correspond to the <code>alg</code> JWS header parameter.
   **
   ** @return                    the accepted JWS algorithms as a read-only set,
   **                            empty set if none.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Algorithm}.
   */
  Set<Algorithm> acceptedAlgorithm();
}