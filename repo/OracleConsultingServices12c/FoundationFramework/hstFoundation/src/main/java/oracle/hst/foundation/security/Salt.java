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

    File        :   Salt.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Salt.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

////////////////////////////////////////////////////////////////////////////////
// interface Salt
// ~~~~~~~~~ ~~~~
/**
 ** Common interface for all salt generators which can be applied in digest or
 ** encryption operations.
 ** <p>
 ** <b>Every implementation of this interface must be thread-safe</b>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface Salt {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** This method will be called for requesting the generation of a new salt of
   ** the specified length.
   **
   ** @param  length             the requested length for the salt.
   **
   ** @return                    the generated salt.
   */
  byte[] generate(final int length);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   includePlain
  /**
   ** Determines if the digests and encrypted messages created with a specific
   ** salt generator will include (prepended) the unencrypted salt itself, so
   ** that it can be used for matching and decryption operations.
   ** <p>
   ** Generally, including the salt unencrypted in encryption results will be
   ** mandatory for randomly generated salts, or for those generated in a
   ** non-predictable manner. Otherwise, digest matching and decryption
   ** operations will always fail.
   ** <p>
   ** For fixed salts, inclusion will be optional (and in fact undesirable if we
   ** want to hide the salt value).
   **
   ** @return                    whether the plain (unencrypted) salt has to be
   **                            included in encryption results or not.
   */
  boolean includePlain();
}