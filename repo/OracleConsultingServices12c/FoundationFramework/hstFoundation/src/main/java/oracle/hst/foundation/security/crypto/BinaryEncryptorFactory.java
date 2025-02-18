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

    File        :   BinaryEncryptorFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    BinaryEncryptorFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.crypto;

import oracle.hst.foundation.security.BinaryEncryptor;

public abstract class BinaryEncryptorFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // at some point we might make this pluggable, but for now, hard-code
  private static final String   FACTORY = "oracle.hst.foundation.security.crypto.DefaultBinaryEncryptorFactory";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static BinaryEncryptorFactory instance;

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the singleton instance of the {@link BinaryEncryptorFactory}.
   **
   ** @return                    the singleton instance of the
   **                            {@link BinaryEncryptorFactory}.
   */
  public static synchronized BinaryEncryptorFactory instance() {
    if (instance == null) {
      try {
        final Class<?> clazz  = Class.forName(FACTORY);
        final Object   object = clazz.newInstance();
        instance = BinaryEncryptorFactory.class.cast(object);
      }
      catch (RuntimeException e) {
        throw e;
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultEncryptor
  /**
   ** Returns the default encryptor that encrypts/descrypts using a default key.
   **
   ** @return                    the default encryptor that encrypts/descrypts
   **                            using a default key.
   */
  public abstract BinaryEncryptor defaultEncryptor();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   randomEncryptor
  /**
   ** Returns a new encryptor initialized with a random encryption key.
   **
   ** @return                    the new encryptor initialized with a random
   **                            encryption key.
   */
  public abstract BinaryEncryptor randomEncryptor();
}