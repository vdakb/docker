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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared security functions

    File        :   StringDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    StringDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

////////////////////////////////////////////////////////////////////////////////
// interface StringDigester
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Common interface for all digesters which receive a String message and return
 ** a String digest.
 ** <p>
 ** For a default implementation, see {@link DefaultStringDigester}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface StringDigester extends Encryptor {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest
  /**
   ** Create a digest of the input message.
   **
   ** @param  message            the message to be digested.
   **
   ** @return                    the digest
   */
  String digest(final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Check whether a message matches a digest, managing aspects like salt,
   ** hashing iterations, etc. (if applicable).
   **
   ** @param  message            the message to be compared to the digest.
   ** @param  digest             the digest to be compared to the digest.
   **
   ** @return                    <code>true</code> if the message matches the
   **                            digest, <code>false</code> otherwise.
   */
  boolean match(final String message, final String digest);
}