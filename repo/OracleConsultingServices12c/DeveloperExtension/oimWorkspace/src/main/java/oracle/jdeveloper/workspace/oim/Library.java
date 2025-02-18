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
    Subsystem   :   Identity Manager Facility

    File        :   Library.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Library.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.27  2012-08-31  DSteding    Release 2 of IAM Suite adopted
    11.1.1.3.37.60.34  2013-05-25  DSteding    PatchSet 1 of IAM Suite Release 2
                                               adopted
    11.1.1.3.37.60.55  2014-01-25  DSteding    PatchSet 2 of IAM Suite Release 2
                                               adopted
    11.1.1.3.37.60.62  2014-11-23  DSteding    Extended ANT classpath by
                                               adding adf-share-support.jar,
                                               adf-share-ca.jar,
                                               javatools-nodeps.jar,
                                               resourcebundle.jar, which are
                                               needed by the sandbox operations.
    11.1.1.3.37.60.67  2015-12-27  DSteding    PatchSet 3 of IAM Suite Release 2
                                               adopted and Relase 1 removed
    11.1.1.3.37.60.69  2015-12-27  DSteding    Removed spaces in directory names
                                               to avoid code generation issues.
                                               Removed Oracle from the product
                                               names and libraries because its
                                               clear where we are.
                                               Removed deprecated R2 Release.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.85  2019-11-22  DSteding    PatchSet 4 of IAM Suite 12c
                                               Release 2 adopted
    12.2.1.3.42.60.93  2021-02-14  DSteding    PatchSet 4 of IAM Suite 12c
                                               Release 2 extended
*/

package oracle.jdeveloper.workspace.oim;

import java.util.Map;
import java.util.LinkedHashMap;

import java.net.URL;

import oracle.jdeveloper.workspace.iam.builder.LibraryBuilder;

////////////////////////////////////////////////////////////////////////////////
// final class Library
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** Utility used by maintain the product and framework libraries of Oracle
 ** Identity Manager in Oracle JDeveloper.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.93
 ** @since   11.1.1.3.37.56.13
 */
public final class Library extends LibraryBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String              DEFAULT_VIEW                = "OIM Library Builder";
  public static final String              DEFAULT_FOLDER              = "IdentityManager";
  public static final String              DEFAULT_RELEASE             = "12.2.1.4";
  public static final String              DEFAULT_DESCRIPTOR          = "oim-library";

  public static final String              OIM_PLATFORM_ID             = "oracle-oim-12c-platform";
  public static final String              OIM_PLATFORM_NAME           = "Identity Manager 12c PLatform";
  public static final String              OIM_CLIENT_ID               = "oracle-oim-12c-client";
  public static final String              OIM_CLIENT_NAME             = "Identity Manager 12c Client";
  public static final String              OIM_ADAPTER_ID              = "oracle-oim-12c-adapter";
  public static final String              OIM_ADAPTER_NAME            = "Identity Manager 12c Adapter";
  public static final String              OIM_SCHEDULER_ID            = "oracle-oim-12c-scheduler";
  public static final String              OIM_SCHEDULER_NAME          = "Identity Manager 12c Scheduler";
  public static final String              OIM_FRONTEND_ID             = "oracle-oim-12c-view";
  public static final String              OIM_FRONTEND_NAME           = "Identity Manager 12c View";
  public static final String              OIM_BACKEND_ID              = "oracle-oim-12c-model";
  public static final String              OIM_BACKEND_NAME            = "Identity Manager 12c Model";
  public static final String              HST_FOUNDATION_ID           = "oracle-hst-12c-foundation";
  public static final String              HST_FOUNDATION_NAME         = "Consulting Headstart 12c Foundation";
  public static final String              HST_FACES_ID                = "oracle-hst-12c-faces";
  public static final String              HST_FACES_NAME              = "Consulting Headstart 12c Faces";
  public static final String              OIM_FOUNDATION_ID           = "oracle-oim-12c-foundation";
  public static final String              OIM_FOUNDATION_NAME         = "Identity Manager 12c Foundation";
  public static final String              OIM_UTILITY_ID              = "oracle-oim-12c-utility";
  public static final String              OIM_UTILITY_NAME            = "Identity Manager 12c Utility";
  public static final String              OIM_ADAPTER_FOUNDATION_ID   = "oracle-oim-12c-adapter-foundation";
  public static final String              OIM_ADAPTER_FOUNDATION_NAME = "Identity Manager 12c Adapter Foundation";

  public static final String              MDS_RUNTIME_ID              = "MDS Runtime";
  public static final String              MDS_RUNTIME_NAME            = MDS_RUNTIME_ID;

  public static final String              SOA_DESIGNTIME_ID           = "SOA Designtime";
  public static final String              SOA_DESIGNTIME_NAME         = SOA_DESIGNTIME_ID;
  public static final String              SOA_RUNTIME_ID              = "SOA Runtime";
  public static final String              SOA_RUNTIME_NAME            = SOA_RUNTIME_ID;
  public static final String              BPEL_RUNTIME_ID             = "BPEL Runtime";
  public static final String              BPEL_RUNTIME_NAME           = BPEL_RUNTIME_ID;
  public static final String              MEDIATOR_RUNTIME_ID         = "Mediator Runtime";
  public static final String              MEDIATOR_RUNTIME_NAME       = MEDIATOR_RUNTIME_ID;

  public static final Map<String, String> RELEASE                     = new LinkedHashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    RELEASE.put("12.2.1.3", "Identity Manager 12.2.1.3");
    RELEASE.put("12.2.1.4", "Identity Manager 12.2.1.4");
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Library</code> that will maintain the specified
   ** {@link URL}.
   **
   ** @param  searchBase         the path to the file system to search for the
   **                            files specified  by <code>descriptor</code>.
   ** @param  release            the path to the Java Archives specific for a
   **                            releass of a featured component.
   */
  private Library(final URL searchBase, final String release) {
    // ensure inhertitance
    super(DEFAULT_VIEW, DEFAULT_DESCRIPTOR, release, searchBase);
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
   ** @return                    the {@link LibraryBuilder} to populate the
   **                            user libraries in Oracle JDeveloper.
   */
  public static LibraryBuilder instance(final URL searchBase, final String release) {
    return new Library(searchBase, release);
  }
}