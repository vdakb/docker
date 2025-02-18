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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   Library.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Library.


    Revisions          Date        Editor      Comment
    -------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13   2011-05-14  DSteding    First release version
    11.1.1.3.37.60.27   2012-08-31  DSteding    Release 2 of IAM Suite adopted
    11.1.1.3.37.60.37   2013-06-24  DSteding    Create foundation faces and
                                                foundation deployment libraries
    11.1.1.3.37.60.69   2015-12-27  DSteding    Removed Oracle from the product
                                                names and libraries because its
                                                clear where we are.
    11.1.1.3.37.60.71   2017-12-27  DSteding    Removed Oracle from the product
                                                names and libraries because its
                                                clear where we are.
    11.1.1.3.37.60.71   2017-10-27  DSteding    Release Dependency of Platform
                                                services in library generation
                                                honored.
    12.2.1.3.42.60.74   2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.85   2019-11-22  DSteding    PatchSet 4 of IAM Suite 12c
                                                Release 2 adopted
    12.2.1.3.42.60.93   2021-02-14  DSteding    PatchSet 4 of IAM Suite 12c
                                                Release 2 extended
    12.2.1.3.42.60.101  2022-06-11  DSteding    Support for Foundation Services
                                                Library Generation
*/

package oracle.jdeveloper.workspace.iam;

import java.util.Map;
import java.util.LinkedHashMap;

import java.net.URL;

import java.io.IOException;

import oracle.jdeveloper.workspace.iam.builder.LibraryBuilder;

////////////////////////////////////////////////////////////////////////////////
// final class Library
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** Utility used by maintain the product and framework libraries of Oracle
 ** Identity and Access Management in Oracle JDeveloper.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.101
 ** @since   11.1.1.3.37.56.13
 */
public final class Library extends LibraryBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String              DEFAULT_VIEW         = "IAM Library Builder";
  public static final String              HEADSTART            = "FoundationFramework";
  public static final String              SERVICES             = "FoundationServices";
  public static final String              PLATFORM             = "PlatformServices";
  public static final String              DEFAULT_RELEASE      = "12.2.1.4";
  public static final String              HEADSTART_DESCRIPTOR = "hst-library";
  public static final String              PLATFORM_DESCRIPTOR  = "hst-platform";
  public static final String              SECURITY_DESCRIPTOR  = "ops-library";
  public static final String              METADATA_DESCRIPTOR  = "mds-library";

  public static final Map<String, String> RELEASE              = new LinkedHashMap<String, String>();

  public static final String              HST_FOUNDATION_ID    = "oracle-hst-12c-foundation";
  public static final String              HST_FOUNDATION_NAME  = "Consulting Headstart 12c Foundation";
  public static final String              HST_FACES_ID         = "oracle-hst-12c-faces";
  public static final String              HST_FACES_NAME       = "Consulting Headstart 12c Faces";
  public static final String              OCS_CORE_ID          = "platform-hst-12c-core";
  public static final String              OCS_CORE_NAME        = "OCS Platform 12c Core";
  public static final String              OCS_JCA_ID           = "platform-hst-12c-jca";
  public static final String              OCS_JCA_NAME         = "OCS Platform 12c Security";
  public static final String              OCS_JPA_ID           = "platform-hst-12c-jpa";
  public static final String              OCS_JPA_NAME         = "OCS Platform 12c Persistence";
  public static final String              OCS_JSON_ID          = "platform-hst-12c-json";
  public static final String              OCS_JSON_NAME        = "OCS Platform 12c Json";
  public static final String              OCS_REST_ID          = "platform-hst-12c-rest";
  public static final String              OCS_REST_NAME        = "OCS Platform 12c Rest";
  public static final String              OCS_FACES_ID         = "platform-hst-12c-faces";
  public static final String              OCS_FACES_NAME       = "OCS Platform 12c Faces";
  public static final String              OPS_SECURITY_ID      = "oracle-jps-12c-common";
  public static final String              OPS_SECURITY_NAME    = "Platform Service 12c Security";
  public static final String              OPS_IDENTITY_ID      = "oracle-jps-12c-identity";
  public static final String              OPS_IDENTITY_NAME    = "Platform Service 12c Identity";
  public static final String              MDS_METADATA_ID      = "oracle-mds-12c-metadata";
  public static final String              MDS_METADATA_NAME    = "Platform Service 12c Metadata";

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    RELEASE.put("12.2.1.3", "Oracle Platform Service 12.2.1.3");
    RELEASE.put("12.2.1.4", "Oracle Platform Service 12.2.1.4");
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Library</code> that will maintain the libraries.
   **
   ** @param  searchBase         the path to the file system to search for the
   **                            files specified  by <code>descriptor</code>.
   ** @param  release            the path to the Java Archives specific for a
   **                            releass of a featured component.
   **
   ** @throws IOException        if the library descriptor couldn't be opened as
   **                            an <code>InputStream</code>.
   */
  private Library(final URL searchBase, final String release)
    throws IOException {

    // ensure inhertitance
    this(HEADSTART_DESCRIPTOR, searchBase, release);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Library</code> that will maintain the libraries.
   **
   ** @param  descriptor         the file name of the descriptor to maintain.
   ** @param  searchBase         the path to the file system to search for the
   **                            files specified  by <code>descriptor</code>.
   ** @param  release            the path to the Java Archives specific for a
   **                            releass of a featured component.
   **
   ** @throws IOException        if the library descriptor couldn't be opened as
   **                            an <code>InputStream</code>.
   */
  private Library(final String descriptor, final URL searchBase, final String release)
    throws IOException {

    // ensure inhertitance
    super(DEFAULT_VIEW, descriptor, release, searchBase);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link LibraryBuilder}.
   **
   ** @param  searchBase         the {@link URL} to the directory to search for
   **                            the files the Oracle JDeveloper User library
   **                            comprise.
   ** @param  release            the path to the Java Archives specific for a
   **                            releass of a featured component.
   **
   ** @return                    the configured <code>Library</code>.
   **
   ** @throws IOException        if the library descriptor couldn't be opened as
   **                            an <code>InputStream</code>.
   */
  public static LibraryBuilder instance(final URL searchBase, final String release)
    throws IOException {

    return new Library(searchBase, release);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link LibraryBuilder}.
   **
   ** @param  descriptor         the file name of the descriptor to maintain.
   ** @param  searchBase         the {@link URL} to the directory to search for
   **                            the files the Oracle JDeveloper User library
   **                            comprise.
   ** @param  release            the path to the Java Archives specific for a
   **                            releass of a featured component.
   **
   ** @return                    the configured <code>Library</code>.
   **
   ** @throws IOException        if the library descriptor couldn't be opened as
   **                            an <code>InputStream</code>.
   */
  public static LibraryBuilder instance(final String descriptor, final URL searchBase, final String release)
    throws IOException {

    return new Library(descriptor, searchBase, release);
  }
}