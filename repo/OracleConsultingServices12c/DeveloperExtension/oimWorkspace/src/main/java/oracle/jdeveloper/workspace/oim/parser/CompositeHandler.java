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

    File        :   CompositeHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CompositeHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.31  2012-04-18  DSteding    Adopted the changes required by
                                               Release 2 of Oracle Identity
                                               Manager.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.parser;

import java.net.URL;

////////////////////////////////////////////////////////////////////////////////
// class CompositeHandler
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Oracle Identity Manager build file descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class CompositeHandler extends ProjectHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String WSDL            = "wsdl";
  public static final String BPEL            = "bpel";
  public static final String TYPE            = "componentType";
  public static final String DECS            = "decs";

  // dont worry about the names of the templates they are different from the
  // target filename to avoid that JDeveloper compiles this project like a
  // composite which must fail always
  public static final String CALLBACK        = "CallbackServiceDefinitions";
  public static final String COMPOSITE       = "composite";
  public static final String PROCESS         = "ApprovalProcess";
  public static final String RULE            = "ApprovalRules";
  public static final String RULE_BASE       = "ApprovalRulesBase";
  public static final String TASK            = "ApprovalTask";

  public static final String DECISION        = "DecisionPoint";
  public static final String DOCUMENT        = "DocumentPackage";
  public static final String ELEMENT         = "CallbackElements";
  public static final String PAYLOAD         = "ApprovalTaskPayload";
  public static final String REQUEST         = "RequestDetails";
  public static final String ROUTING         = "RoutingSlipList";
  public static final String MACHINE         = "TaskStateMachine";
  public static final String WSADDR          = "wsaddr";

  public static final String WORKSPACE       = "sca.workspace.name";
  public static final String SERVICE         = "sca.workflow.service";
  public static final String WORKFLOW        = "sca.workflow.name";
  public static final String REVISION        = "sca.workflow.revision";
  public static final String LABEL           = "sca.workflow.label";
  public static final String MODE            = "sca.workflow.mode";
  public static final String STATE           = "sca.workflow.state";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5126014660899431225")
  private static final long  serialVersionUID = 8232001978925523244L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>CompositeHandler</code> instance by converting the
   ** given pathname string into an abstract pathname.
   ** <p>
   ** If the given string is the empty string, then the result is the empty
   ** abstract pathname.
   **
   ** @param  file               the {@link URL} pathname name to the
   **                            {@link ProjectHandler}.
   **
   ** @throws NullPointerException if the <code>name</code> argument is
   **                              <code>null</code>.
   */
  public CompositeHandler(final URL file) {
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
    register(WORKFLOW,  "//composite/@name");
    register(REVISION,  "//composite/@revision");
    register(LABEL,     "//composite/@label");
    register(MODE,      "//composite/@mode");
    register(STATE,     "//composite/@state");
    register(SERVICE,   "//composite/service/binding.adf/@serviceName");
  }
}