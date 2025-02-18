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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Resource Facility

    File        :   RuntimeSystem.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RuntimeSystem.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import oracle.hst.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// abstract class RuntimeSystem
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>RuntimeSystem</code> supplies a set of static methods for getting
 ** Operating System dependant settings.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class RuntimeSystem {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static int    JDK1_0                = 10;
  public final static int    JDK1_1                = 11;
  public final static int    JDK1_2                = 12;
  public final static int    JDK1_3                = 13;
  public final static int    JDK1_4                = 14;
  public final static int    JDK1_5                = 15;
  public final static int    JDK1_6                = 16;
  public final static int    JDK1_7                = 17;
  public final static int    JDK1_8                = 18;
  public final static int    JDK1_9                = 19;

  public static final String OS_NAME               = "os.name";
  public static final String OS_VERSION            = "os.version";
  public static final String OS_WINDOWS            = "Windows";
  public static final String OS_WINDOW_VERSION_NT  = "4.0";
  public static final String OS_WINDOW_VERSION_XP  = "5.1";
  public static final String OS_MAC                = "Mac OS";
  public static final String OS_UNIX               = "Unix";

  public static final String JAVA_VERSION          = "java.version";
  public static final String JAVA_CLASSPATH        = "java.class.path";

  public static final String USER_NAME             = "user.name";
  public static final String USER_COUNTRY          = "user.country";
  public static final String USER_LANGUAGE         = "user.language";
  public static final String USER_HOME             = "user.home";
  public static final String USER_DIRECTORY        = "user.dir";
  public static final String USER_TEMP             = "java.io.tempdir";

  public static final OS     OS                    = new OS();
  public static final Java   JAVA                  = new Java();
  public static final User   USER                  = new User();

  //////////////////////////////////////////////////////////////////////////////
  // Memeber classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class OS
  // ~~~~~ ~~
  public static final class OS {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String name      = property(OS_NAME);
    private final String version   = property(OS_VERSION);

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor (private)
    /**
     ** Default constructor
     ** <br>
     ** Access modifier private prevents other classes using
     ** "new OS()"
     */
    private OS() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isWindows
    /**
     ** Returns <code>true</code> whether we're on Microsoft Windows
     **
     ** @return                <code>true</code> if this a system from
     **                        Microsoft.
     */
    public boolean windows() {
      return this.name.startsWith(OS_WINDOWS);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   olderWindows
    /**
     ** Returns <code>true</code> if we are on Windows 95 or NT.
     **
     ** @return                  <code>true</code> if this a system from
     **                          Microsoft.
     */
    public boolean olderWindows() {
      return windows() && this.version.startsWith(OS_WINDOW_VERSION_NT);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   modernWindows
    /**
     ** Returns <code>true</code> if we are on Windows 98/ME/2000/XP.
     **
     ** @return                  <code>true</code> if this a system from
     **                          Microsoft.
     */
    public boolean modernWindows() {
      return windows() && !this.version.startsWith(OS_WINDOW_VERSION_NT);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   windowsXP
    /**
     ** Returns <code>true</code> if we are on XP.
     **
     ** @return                  <code>true</code> if this a system from
     **                          Microsoft.
     */
    public boolean windowsXP() {
      return windows() && this.version.startsWith(OS_WINDOW_VERSION_XP);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   macOS
    /**
     ** Returns <code>true</code> if this a system from Apple.
     **
     ** @return                  <code>true</code> if this a system from
     **                          Apple.
     */
    public boolean macOS() {
      return this.name.startsWith(OS_MAC);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   unix
    /**
     ** Returns <code>true</code> if this a unix derivate.
     **
     ** @return                  <code>true</code> if this a unix derivate.
     */
    public boolean unix() {
      return this.name.startsWith(OS_UNIX);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Java
  // ~~~~~ ~~~~
  public static final class Java {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int    version;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor (private)
    /**
     ** Default constructor
     ** <br>
     ** Access modifier private prevents other classes using
     ** "new OS()"
     */
    private Java() {
      final String version = property(JAVA_VERSION);
      if (version.startsWith("1.9.")) {
        this.version = JDK1_9;
      }
      else if (version.startsWith("1.8.")) {
        this.version = JDK1_8;
      }
      else if (version.startsWith("1.7.")) {
        this.version = JDK1_7;
      }
      else if (version.startsWith("1.6.")) {
        this.version = JDK1_6;
      }
      else if (version.startsWith("1.5.")) {
        this.version = JDK1_5;
      }
      else if (version.startsWith("1.4.")) {
        this.version = JDK1_4;
      }
      else if (version.startsWith("1.3.")) {
        this.version = JDK1_3;
      }
      else if (version.startsWith("1.2.")) {
        this.version = JDK1_2;
      }
      else if (version.startsWith("1.1.")) {
        this.version = JDK1_1;
      }
      else if (version.startsWith("1.0.")) {
        this.version = JDK1_0;
      }
      else {
        // unknown version, assume 1.3
        this.version = JDK1_3;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   classPath
    /**
     ** Returns the classpath of the virtual machine.
     **
     ** @return                  the classpath of the virtual machine.
     */
    public String classPath() {
      return property(JAVA_CLASSPATH);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOrLater
    /**
     ** Returns <code>true</code> whether we're on a JVM where the version is
     ** greater or equel to the specified version.
     **
     ** @param  version          the actual version  of a JVM to verify.
     **
     ** @return                  <code>true</code> if the requested JVM version
     **                          is greater or equal to the current version
     **                          Microsoft.
     */
    public boolean isOrLater(final int version) {
      return this.version >= version;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotOne
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.1.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.1.
     */
    public boolean isOneDotOne() {
      return this.version == JDK1_1;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotTwo
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.2.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.2.
     */
    public boolean isOneDotTwo() {
      return this.version == JDK1_2;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotThree
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.3.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.3.
     */
    public boolean isOneDotThree() {
      return this.version == JDK1_3;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotFour
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.4.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.4.
     */
    public boolean isOneDotFour() {
      return this.version == JDK1_4;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotFive
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.5.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.5.
     */
    public boolean isOneDotFive() {
      return this.version == JDK1_5;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotSix
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.6.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.6.
     */
    public boolean isOneDotSix() {
      return this.version == JDK1_6;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotSeven
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.7.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.7.
     */
    public boolean isOneDotSeven() {
      return this.version == JDK1_7;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotEight
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.8.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.8.
     */
    public boolean isOneDotEight() {
      return this.version == JDK1_8;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   isOneDotNine
    /**
     ** Returns <code>true</code> whether we're on a JVM 1.9.
     **
     ** @return                  <code>true</code> if  we're on a JVM 1.9.
     */
    public boolean isOneDotNine() {
      return this.version == JDK1_9;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class User
  // ~~~~~ ~~~~
  public static final class User {

    ////////////////////////////////////////////////////////////////////////////
    // Method:   name
    /**
     ** Returns user name.
     **
     ** @return                  the name of the user.
     */
    public String name() {
      return property(USER_NAME);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   language
    /**
     ** Returns user's language.
     **
     ** @return                    the user's language
     */
    public String language() {
      return property(USER_LANGUAGE);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   country
    /**
     ** Returns user's country.
     **
     ** @return                    the user's home directory
     */
    public String country() {
      return property(USER_COUNTRY);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   home
    /**
     ** Returns user's home directory.
     **
     ** @return                    the user's home directory
     */
    public String home() {
      return property(USER_HOME);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   directory
    /**
     ** Returns user working directory.
     **
     ** @return                    the user's directory
     */
    public String directory() {
      return property(USER_DIRECTORY);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   temp
    /**
     ** Returns user temporary directory.
     **
     ** @return                    the user's directory
     */
    public String temp() {
      return property(USER_TEMP);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RuntimeSystem</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new RuntimeSystem()" and enforces use of the public method below.
   */
  private RuntimeSystem() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Tries to look up the System property for the given key.
   ** <br>
   ** In untrusted environments this may throw a <code>SecurityException</code>.
   ** In this case, we catch the exception and return an empty string.
   **
   ** @param key                 the name of the system property
   **
   ** @return                     the system property's String value, or null if
   **                             there's no such value, or an empty String when
   **                             a <code>SecurityException</code> has been
   **                             catched
   */
  public static String property(String key) {
   try {
     return System.getProperty(key);
    }
    catch (SecurityException e) {
      return SystemConstant.EMPTY;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Tries to look up the System property for the given key.
   ** <br>
   ** In untrusted environments this may throw a <code>SecurityException</code>.
   ** In this case, we catch the exception and return an empty string.
   **
   ** @param key                 the name of the system property
   ** @param defaultValue        the default value if no property exists.
   **
   ** @return                     the system property's String value, or null if
   **                             there's no such value, or an empty String when
   **                             a <code>SecurityException</code> has been
   **                             catched
   */
  public static String property(String key, String defaultValue) {
   try {
      return System.getProperty(key, defaultValue);
    }
    catch (SecurityException e) {
      return defaultValue;
    }
  }
}