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

    File        :   GUIDRandom.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    GUIDRandom.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.security;

import java.util.Random;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

////////////////////////////////////////////////////////////////////////////////
// final class GUIDRandom
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** In the multitude of java GUID generators, I found none that guaranteed
 ** randomness. GUIDs are guaranteed to be globally unique by using ethernet
 ** MACs, IP addresses, time elements, and sequential numbers. GUIDs are not
 ** expected to be random and most often are  easy/possible to guess given a
 ** sample from a given generator. SQL Server, for example generates GUID that
 ** are unique but sequencial within a given instance.
 ** <p>
 ** GUIDs can be used as security devices to hide things such as files within a
 ** filesystem where listings are unavailable (e.g. files  that are served up
 ** from a Web server with indexing turned off).
 ** <br>
 ** This may be desireable in cases where standard authentication is not
 ** appropriate. In this scenario, the GUIDRandoms are used as directories.
 ** <br>
 ** Another example is the use of GUIDs for primary keys in a database where you
 ** want to ensure that the keys are secret. Random GUIDs can then be used in a
 ** URL to prevent hackers (or users) from accessing records by guessing or
 ** simply by incrementing sequential numbers.
 ** <p>
 ** There are many other possiblities of using GUIDs in the realm of security
 ** and encryption where the element of randomness is important.
 ** <br>
 ** This class was written for these purposes but can also be used as a general
 ** purpose GUID generator as well.
 ** <p>
 ** GUIDRandom generates truly random GUIDs by using the system's IP address
 ** (name/IP), system time in milliseconds (as an integer), and a very large
 ** random number joined together in a single String that is passed through an
 ** MD5 hash. The IP address and system time make the MD5 seed globally unique
 ** and the random number guarantees that the generated GUIDs will have no
 ** discernable pattern and cannot be guessed given any number of previously
 ** generated GUIDs.
 ** <br>
 ** It is generally not possible to access the seed information (IP, time,
 ** random number) from the resulting GUIDs as the MD5 hash algorithm provides
 ** one way encryption.
 ** <p>
 ** ----&gt; Security of GUIDRandom: &lt;-----
 ** <br>
 ** GUIDRandom can be called one of two ways -- with the basic java Random
 ** number generator or a cryptographically strong random generator
 ** (SecureRandom). The choice is offered because the secure random generator
 ** takes about 3.5 times longer to generate its random numbers and this
 ** performance hit may not be worth the added security especially considering
 ** the basic generator is seeded with a cryptographically strong random seed.
 ** <p>
 ** Seeding the basic generator in this way effectively decouples the random
 ** numbers from the time component making it virtually impossible to predict
 ** the random number component even if one had absolute knowledge of the
 ** system time. Thanks to Ashutosh Narhari for the suggestion of using the
 ** static method to prime the basic random generator.
 ** <p>
 ** Using the secure random option, this class compies with the statistical
 ** random number generator tests specified in FIPS 140-2, Security
 ** Requirements for Cryptographic Modules, secition 4.9.1.
 ** <p>
 ** I converted all the pieces of the seed to a String before handing it over to
 ** the MD5 hash so that you could print it out to make sure it contains the
 ** data you expect to see and to give a nice warm fuzzy. If you need better
 ** performance, you may want to stick to byte[] arrays.
 ** <p>
 ** I believe that it is important that the algorithm for generating random
 ** GUIDs be open for inspection and modification.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
 public final class GUIDRandom {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static String       id;
  private static Random       rand;
  private static SecureRandom secureRand;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  String                      beforeMD5 = "";
  String                      afterMD5  = "";

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  /*
   * Static block to take care of one time secureRandom seed.
   * It takes a few seconds to initialize SecureRandom. You mightwant to
   * consider removing this static block or replacing it with a "time since
   * first loaded" seed to reduce this time.
   * This block will run only once per JVM instance.
   */
  static {
    secureRand = new SecureRandom();
    long secureInitializer = secureRand.nextLong();
    rand = new Random(secureInitializer);
    try {
      id = InetAddress.getLocalHost().toString();
    }
    catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GUIDRandom</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GUIDRandom() {
    // ensure inheritance
    this(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GUIDRandom</code> with security option.
   ** <br>
   ** Setting secure <code>true</code> enables each random number generated to
   ** be cryptographically strong. Secure <code>false</code> defaults to the
   ** standard Random function seeded with a single cryptographically strong
   ** random number.
   **
   ** @param  secure             <code>true</code> enables random number
   **                            generated to be cryptographically strong;
   **                            otherwise <code>false</code>.
   */
  public GUIDRandom(boolean secure) {
    // ensure inheritance
    super();

    // initialize instance
    guid(secure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Convert to the standard format for GUID (Useful for SQL Server
   ** UniqueIdentifiers, etc.)
   ** <br>
   ** Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
   **
   ** @return                    the string representation of this instance.
   */
  @Override
  public String toString() {
    return toString(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Convert to the standard format for GUID (Useful for SQL Server
   ** UniqueIdentifiers, etc.)
   ** <br>
   ** Examples:
   ** <br>
   ** uppercase true:  C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
   ** uppercase false: c2feeeac-cfcd-11d1-8b05-00600806d9b6
   **
   ** @param  upperCase          <code>true</code> if the generated GUID needs
   **                            to be in uppercase form.
   **
   ** @return                    the string representation of this instance.
   */
  public String toString(final boolean upperCase) {
    final String       raw = upperCase ? this.afterMD5.toUpperCase() : this.afterMD5.toLowerCase();
    final StringBuffer out = new StringBuffer();
    out.append(raw.substring(0, 8));
    out.append("-");
    out.append(raw.substring(8, 12));
    out.append("-");
    out.append(raw.substring(12, 16));
    out.append("-");
    out.append(raw.substring(16, 20));
    out.append("-");
    out.append(raw.substring(20));
    return out.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   guid
  /**
   ** Generate the random GUID.
   ** <br>
   ** Setting secure <code>true</code> enables each random number generated to
   ** be cryptographically strong. Secure <code>false</code> defaults to the
   ** standard Random function seeded with a single cryptographically strong
   ** random number.
   **
   ** @param  secure             <code>true</code> enables random number
   **                            generated to be cryptographically strong;
   **                            otherwise <code>false</code>.
   */
  private synchronized void guid(final boolean secure) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException e) {
      System.out.println("Error: " + e);
    }

    try {
      long time   = System.currentTimeMillis();
      long random = 0;

      if (secure) {
        random = secureRand.nextLong();
      }
      else {
        random = rand.nextLong();
      }
      // this StringBuffer can be a long as you need; the MD5 hash will always
      // return 128 bits. You can change the seed to include anything you want
      // here.
      // You could even stream a file through the MD5 making the odds of
      // guessing it at least as great as that of guessing the contents of the file!
      StringBuffer sbValueBeforeMD5 = new StringBuffer();

      sbValueBeforeMD5.append(id);
      sbValueBeforeMD5.append(":");
      sbValueBeforeMD5.append(Long.toString(time));
      sbValueBeforeMD5.append(":");
      sbValueBeforeMD5.append(Long.toString(random));

      this.beforeMD5 = sbValueBeforeMD5.toString();
      md5.update(this.beforeMD5.getBytes());

      byte[] array = md5.digest();
      StringBuffer sb = new StringBuffer();
      for (int j = 0; j < array.length; ++j) {
        int b = array[j] & 0xFF;
        if (b < 0x10)
          sb.append('0');
        sb.append(Integer.toHexString(b));
      }
      this.afterMD5 = sb.toString();
    }
    catch (Exception e) {
      System.out.println("Error:" + e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generate a random  <code>GUID</code> without security option.
   **
   ** @return                    the string representation of the generated GUID
   **                            e.g.: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
   */
  public static String generate() {
    return generate(false, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generate a random  <code>GUID</code> with security option.
   ** <br>
   ** Setting secure <code>true</code> enables each random number generated to
   ** be cryptographically strong. Secure <code>false</code> defaults to the
   ** standard Random function seeded with a single cryptographically strong
   ** random number.
   **
   ** @param  secure             <code>true</code> enables random number
   **                            generated to be cryptographically strong;
   **                            otherwise <code>false</code>.
   **
   ** @return                    the string representation of the generated GUID
   **                            e.g.: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
   */
  public static String generate(final boolean secure) {
    return generate(secure, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generate a random  <code>GUID</code> with security option.
   ** <br>
   ** Setting secure <code>true</code> enables each random number generated to
   ** be cryptographically strong. Secure <code>false</code> defaults to the
   ** standard Random function seeded with a single cryptographically strong
   ** random number.
   **
   ** @param  secure             <code>true</code> enables random number
   **                            generated to be cryptographically strong;
   **                            otherwise <code>false</code>.
   ** @param  upperCase          <code>true</code> if the generated GUID needs
   **                            to be in uppercase form.
   **
   ** @return                    the string representation of the generated GUID
   **                            e.g.: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
   */
  public static String generate(final boolean secure, final boolean upperCase) {
    final GUIDRandom guid = new GUIDRandom(secure);
    return guid.toString(upperCase);
  }
}