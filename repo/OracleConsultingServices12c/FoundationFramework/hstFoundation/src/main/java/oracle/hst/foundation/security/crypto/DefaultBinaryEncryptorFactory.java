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

    File        :   DefaultBinaryEncryptorFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DefaultBinaryEncryptorFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.crypto;

import oracle.hst.foundation.security.BinaryEncryptor;

public class DefaultBinaryEncryptorFactory extends BinaryEncryptorFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final BinaryEncryptor defaultEncryptor;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DefaultBinaryEncryptorFactory</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DefaultBinaryEncryptorFactory() {
    // ensure inheritance
    super();

    // initialize instance
    this.defaultEncryptor = new AESBinaryEncryptor(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultEncryptor (BinaryEncryptorFactory)
  /**
   ** Returns the default encryptor that encrypts/descrypts using a default key.
   **
   ** @return                    the default encryptor that encrypts/descrypts
   **                            using a default key.
   */
  @Override
  public BinaryEncryptor defaultEncryptor() {
    return this.defaultEncryptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   randomEncryptor (BinaryEncryptorFactory)
  /**
   ** Returns a new encryptor initialized with a random encryption key.
   **
   ** @return                    the new encryptor initialized with a random
   **                            encryption key.
   */
  @Override
  public BinaryEncryptor randomEncryptor() {
    return new AESBinaryEncryptor(false);
  }
}