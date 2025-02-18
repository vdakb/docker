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

    File        :   DBSServerPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DBSServerPage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    The network protocol to connect
                                               to the database the metadata
                                               store is deployed is now maneged
                                               by this wizard page.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Cloning of an existing file
                                               implemented.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;
import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class DBSServerPage
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the New Application Wizard dialog for
 ** entering the connection data to a database server.
 ** <p>
 ** In general, wizard pages are not supposed to be published APIs, so we enforce
 ** that. Even though the panel is constructed by the IDE framework
 ** using reflection, the IDE framework does not require that it is public (only
 ** that it has a no-argument constructor).
 ** <p>
 ** This panel implements the operations that must be supported by a GUI
 ** component added to the project-from-template and application-from-template
 ** wizards.
 ** <p>
 ** The GUI component is associated with a particular technology scope and is
 ** registered declaratively using the <code>technology-hook</code> hook in the
 ** extension manifest.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class DBSServerPage extends TemplateFeaturePage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String                ORACLE           = "oracle";
  protected static final String                DB2UDB           = "udb";

  protected static final Map<String, String>   DATABASE         = new LinkedHashMap<String, String>();
  protected static final Map<String, Integer>  VENDOR           = new HashMap<String, Integer>();
  protected static final Map<String, String[]> NETWORK          = new LinkedHashMap<String, String[]>();

  private static final String[]                ORCL             = { "thin", "oci" };
  private static final String[]                IBM              = { StringUtility.EMPTY, "database" };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8697232375876984313")
  private static final long                    serialVersionUID = 3008812791790627798L;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    DATABASE.put(ORACLE, "Oracle Database");
    DATABASE.put(DB2UDB, "Universal Database");

    VENDOR.put(ORACLE, ComponentBundle.VENDOR_ORACLE);
    VENDOR.put(DB2UDB, ComponentBundle.VENDOR_IBM);

    NETWORK.put(ORACLE,  ORCL);
    NETWORK.put(DB2UDB,  IBM);
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DBSServerPage</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected DBSServerPage() {
    // ensure inheritance
    super();
  }
}