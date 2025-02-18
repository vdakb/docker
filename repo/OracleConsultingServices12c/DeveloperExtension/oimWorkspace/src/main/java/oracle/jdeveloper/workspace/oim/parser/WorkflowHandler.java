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

    File        :   WorkflowHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    WorkflowHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.parser;

import java.net.URL;

////////////////////////////////////////////////////////////////////////////////
// class WorkflowHandler
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Oracle Identity Manager build file descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class WorkflowHandler extends ProjectHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PARTITION        = "oim.workflow.partition";
  public static final String WORKFLOW         = "oim.workflow.name";
  public static final String REVISION         = "oim.workflow.revision";
  public static final String SERVICE          = "oim.workflow.service";
  public static final String CATEGORY         = "oim.workflow.category";
  public static final String PROVIDER         = "oim.workflow.provider";
  public static final String OPERATION        = "oim.workflow.operation";
  public static final String PAYLOAD          = "oim.workflow.payload";
  public static final String PLAN             = "oim.workflow.plan";
  public static final String PLAN_OVERWRITE   = "oim.workflow.plan.overwrite";

  public static final String PACKAGEPATH      = "oim.packagepath";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6773032266317650925")
  private static final long  serialVersionUID = 2L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>PreferenceHandler</code> instance by converting the
   ** given pathname string into an abstract pathname.
   ** <p>
   ** If the given string is the empty string, then the result is the empty
   ** abstract pathname.
   **
   ** @param  file               the {@link URL} pathname name to the
   **                            <code>ANTFileHandler</code>.
   **
   ** @throws NullPointerException if the <code>name</code> argument is
   **                              <code>null</code>.
   */
  public WorkflowHandler(final URL file) {
    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers all path translation this instance can handle.
   */
  @Override
  protected void register() {
    // ensure inheritance
    super.register();

    // register local defined substitution placeholder
    registerProperty(PARTITION,      "workflow.partition");
    registerProperty(WORKFLOW,       "workflow.name");
    registerProperty(REVISION,       "workflow.revision");
    registerProperty(SERVICE,        "workflow.service");
    registerProperty(CATEGORY,       "workflow.category");
    registerProperty(PROVIDER,       "workflow.provider");
    registerProperty(OPERATION,      "workflow.operation");
    registerProperty(PAYLOAD,        "workflow.payload");
    registerProperty(PLAN,           "workflow.plan");
    registerProperty(PLAN_OVERWRITE, "workflow.plan.overwrite");
    registerProperty(PACKAGEPATH,    "packagepath.composite");
  }
}