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

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Version.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Version.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

////////////////////////////////////////////////////////////////////////////////
// final class Version
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** A type safe SAML version enumeration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Version {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Major version number. */
  private int                  major;

  /** Minor version number. */
  private int                  minor;

  /** String representation of the version. */
  private String               version;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Version</code> with the specified {@link Builder}.
   **
   ** @param  major              the SAML major version number.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  minor              the SAML minor version number
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  private Version(final int major, final int minor) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.major   = major;
    this.minor   = minor;
    this.version = this.major + "." + this.minor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   major
  /**
   ** Returns the major version of the SAML version.
   **
   ** @return                    the major version of the SAML version.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int major() {
    return this.major;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minor
  /**
   ** Returns the minor version of the SAML version.
   **
   ** @return                    the minor version of the SAML version.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int minor() {
    return this.minor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to creates a <code>Version</code> for a given version
   ** string, such as "2.0".
   **
   ** @param  value              the SAML version string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Version</code> for the given string.
   **                            <br>
   **                            Possible object is <code>Version</code>.
   */
  public static final Version of(final String value) {
    final String[] components = value.split("\\.");
    return of(Integer.valueOf(components[0]), Integer.valueOf(components[1]));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to creates a <code>Version</code> given the major and
   ** minor version number.
   **
   ** @param  major              the SAML major version number.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  minor              the SAML minor version number
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>Version</code>.
   **                            <br>
   **                            Possible object is <code>Version</code>.
   */
  public static Version of(final int major, final int minor) {
    return new Version(major, minor);
  }
}