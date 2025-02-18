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

    File        :   Registry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Registry.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.crypto;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import oracle.hst.foundation.security.StringEncryptor;
import oracle.hst.foundation.security.PropertiesEncryptor;

////////////////////////////////////////////////////////////////////////////////
// final class Registry
// ~~~~~ ~~~~~ ~~~~~~~~
/**
 ** This class is as a classloader-wide in-memory registry for encryptors, so
 ** that {@link Registry} instances can be safely serialized (encryptors are not
 ** serializable).
 ** <p>
 ** This means that an {@link Registry} instance will be de-serializable  only
 ** by the same virtual machine that serialized it.
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public final class Registry {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Registry instance = new Registry();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Map<Integer, StringEncryptor> string = Collections.synchronizedMap(new HashMap<Integer, StringEncryptor>());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Registry</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Registry()" and enforces use of the public factory method
   ** below.
   */
  private Registry() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the sole instance of this of the registry.
   **
   ** @return                     the sole instance of this of the registry.
   */
  public static Registry instance() {
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Register a {@link StringEncryptor} for a {@link PropertiesEncryptor}.
   **
   ** @param  properties         the {@link PropertiesEncryptor} the
   **                            {@link StringEncryptor} has to be regsitered
   **                            for.
   ** @param  cryptor            the {@link StringEncryptor} to register for the
   **                            specified {@link PropertiesEncryptor}; may be
   **                            <code>null</code>.
   */
  public void register(final PropertiesEncryptor properties, final StringEncryptor cryptor) {
    this.register(properties.identifier(), cryptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Register a {@link StringEncryptor} for a generic identifier.
   **
   ** @param  identifier         the identifier the {@link StringEncryptor} has
   **                            to be regsitered for.
   ** @param  cryptor            the {@link StringEncryptor} to register for the
   **                            specified {@link PropertiesEncryptor}; may be
   **                            <code>null</code>.
   */
  public void register(final String identifier, final StringEncryptor cryptor) {
    this.string.put(identifier.hashCode(), cryptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Register a {@link StringEncryptor} for a generic identifier.
   **
   ** @param  identifier         the identifier the {@link StringEncryptor} has
   **                            to be regsitered for.
   ** @param  cryptor            the {@link StringEncryptor} to register for the
   **                            specified {@link PropertiesEncryptor}; may be
   **                            <code>null</code>.
   */
  public void register(final Integer identifier, final StringEncryptor cryptor) {
    this.string.put(identifier, cryptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cryptor
  /**
   ** Returns the registered {@link StringEncryptor} for a
   ** {@link PropertiesEncryptor}.
   **
   ** @param  properties         the {@link PropertiesEncryptor} the
   **                            {@link StringEncryptor} has to returned for.
   **
   ** @return                    the registred {@link StringEncryptor}; may be
   **                            <code>null</code>.
   */
  public StringEncryptor cryptor(final PropertiesEncryptor properties) {
    return cryptor(properties.identifier());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cryptor
  /**
   ** Returns the registered {@link StringEncryptor} for a
   ** {@link PropertiesEncryptor}.
   **
   ** @param  identifier         the identifier the {@link StringEncryptor} has
   **                            to returned for.
   **
   ** @return                    the registred {@link StringEncryptor}; may be
   **                            <code>null</code>.
   */
  public StringEncryptor cryptor(final String identifier) {
    return cryptor(identifier.hashCode());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cryptor
  /**
   ** Returns the registered {@link StringEncryptor} for a
   ** {@link PropertiesEncryptor}.
   **
   ** @param  identifier         the identifier the {@link StringEncryptor} has
   **                            to returned for.
   **
   ** @return                    the registred {@link StringEncryptor}; may be
   **                            <code>null</code>.
   */
  public StringEncryptor cryptor(final Integer identifier) {
    return this.string.get(identifier);
  }
}