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

    File        :   Feature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Feature.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.64  2015-02-14  DSteding    Extended to support the custom
                                               content provider set for build
                                               and unit test
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam;

////////////////////////////////////////////////////////////////////////////////
// class Feature
// ~~~~~ ~~~~~~~
/**
 ** Addin for application and project functionality within Oracle
 ** JDeveloper to handle Identity and Access Management artifacts.
 ** <p>
 ** The {@link AbstractAddin} provides the mechanism for this JDeveloper
 ** extensions to carry out programmatic initialization during the startup
 ** sequence of the JDeveloper IDE.
 ** <br>
 ** The initialize() method of this {@link AbstractAddin} is called prior to
 ** the display of the main window.
 ** <br>
 ** The registration is done in the <code>addins</code> section of the extension
 ** manifest. For more information on the extension manifest, see the
 ** documentation in jdev\doc\extension.
 ** <br>
 ** Care should be taken when implementing the initialize() method to minimize
 ** the work carried out and the classes loaded. If it is possible to defer some
 ** initialization until a later point, it will be deferred.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Feature extends AbstractAddin {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Feature</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Feature() {
    // ensure inheritance
    super(Manifest.string(Manifest.FEATURE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (Addin)
  /**
   ** Invoked by the super class after the instance of the
   ** {@link AbstractAddin} is instantiated by the <code>AddinManager</code>.
   ** <br>
   ** The method is called prior to the display of the main window.
   **
   ** @see  AbstractAddin#initialize
   */
  @Override
  public void initialize() {
    registerContentProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerContentProvider
  /**
   ** Registers the all <code>ContentSetProvider</code>s.
   ** <br>
   ** Once registered, the information from the <code>ContentSetProvider</code>s
   ** will be unioned with other <code>ContentSetProvider</code>s to establish
   ** the contents of a project.
   */
  protected void registerContentProvider() {
    // register our content set provider property with Project
    oracle.jdeveloper.workspace.iam.project.content.site.Settings.register();
    oracle.jdeveloper.workspace.iam.project.content.build.Settings.register();
    oracle.jdeveloper.workspace.iam.project.content.junit.Settings.register();
  }
}