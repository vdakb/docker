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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   Secret.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Secret.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.security;

import java.util.Locale;
import java.util.Random;
import java.util.Objects;

import java.security.SecureRandom;

////////////////////////////////////////////////////////////////////////////////
// class Secret
// ~~~~~ ~~~~~~~~~~~
/**
 ** Provides static methods for generating randomized string to exchange with
 ** relying parties.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Secret {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String upper    = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String lower    = upper.toLowerCase(Locale.ROOT);
  private static final String digits   = "0123456789";
  private static final String special  = "!§$%&()[]{}#+-";
  private static final String alphabet = upper + lower + digits + special;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Random        random;
  private final char[]        symbols;
  private final char[]        buffer;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a string <code>Secret</code> with the default size of
   ** <code>21</code> characters.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Secret() {
    // ensure inheritance
    this(21);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a string <code>Secret</code> with the spezified size of
   ** characters.
   **
   ** @param  length             the amount of characters to generate.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public Secret(final int length) {
    // ensure inheritance
    this(length, new SecureRandom());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a string <code>Secret</code> with the spezified size of
   ** characters unsing the specified {@link Random} <code>random</code>.
   **
   ** @param  length             the amount of characters to generate.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  random             the ...
   **                            <br>
   **                            Allowed object is {@link Random}.
   */
  public Secret(final int length, final Random random) {
    // ensure inheritance
    this(length, random, alphabet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a string <code>Secret</code> with the spezified size of
   ** characters unsing the specified {@link Random} <code>random</code>.
   **
   ** @param  length             the amount of characters to generate.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  random             the ...
   **                            <br>
   **                            Allowed object is {@link Random}.
   ** @param  symbols            the alphabet allowed.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public Secret(final int length, final Random random, final String symbols) {
    // prevent bogus input
    if (length < 1)
      throw new IllegalArgumentException();

    // prevent bogus input
    if (symbols.length() < 2)
      throw new IllegalArgumentException();

    this.random  = Objects.requireNonNull(random);
    this.symbols = symbols.toCharArray();
    this.buffer  = new char[length];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generate a random string.
   **
   ** @return                    a randomized string acording to the configured
   **                            capabilities.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String generate() {
    for (int i = 0; i < this.buffer.length; ++i)
      this.buffer[i] = this.symbols[random.nextInt(this.symbols.length)];
    return new String(this.buffer);
  }
}