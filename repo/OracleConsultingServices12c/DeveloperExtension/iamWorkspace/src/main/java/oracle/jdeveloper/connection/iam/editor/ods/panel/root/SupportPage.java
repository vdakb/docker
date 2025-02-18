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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   SupportPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SupportPage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.root;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.connection.iam.editor.ods.RootEditor;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPage;

////////////////////////////////////////////////////////////////////////////////
// class SupportPage
// ~~~~~ ~~~~~~~~~~~
/**
 ** A {@link AbstractPage} to display the supported capabilities like supported
 ** features and/or supported extension of a Directory Service RootDSE in a
 ** editor page.
 ** <p>
 ** A supported extension is a mechanism for identifying the Extended Request
 ** supported by the Directory Service. The OIDs of these extended operations
 ** are listed in the <code>supportedExtension</code> attribute of the
 ** server's RootDSE.
 ** <p>
 ** Supported features is a mechanism for identifying optional capabilities
 ** that the Directory Service supports.
 ** <p>
 ** Supported features supported by the server may be listed in the
 ** <code>supportedFeatures</code> attribute of the server's RootDSE, which
 ** lists the OIDs of the supported features. 
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class SupportPage extends AbstractPage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6346133073934292448")
  private static final long serialVersionUID = -6297636043223151261L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private AttributePanel feature;
  private AttributePanel extension;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AuthenticationPage</code> responsible to visualize the
   ** table UI of supported capability view that populates its data from the
   ** specified {@link Namespace}.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            data.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  private SupportPage(final Namespace data) {
    // ensure inheritance
    super(RootEditor.KEY, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeView (AbstractPage)
  /**
   ** The data that the <code>SupportPage</code> should use to populate specific
   ** components comes from the {@link Namespace} passed to this page.
   */
  @Override
  protected final void initializeView() {
    // the header of the panel and the populated data
    this.extension = AttributePanel.build(Bundle.string(Bundle.ROOT_SUPPORTED_EXTENSION_PANEL_HEADER), RootEditor.EXTENSION, this.data);
    // the title of the page usually the name of the connection
    initializeHeaderPanel(Bundle.string(Bundle.ROOT_SUPPORTED_EXTENSION_PANEL_TITLE), this.extension);
    // the header of the panel and the populated data
    this.feature = AttributePanel.build(Bundle.string(Bundle.ROOT_SUPPORTED_FEATURE_PANEL_HEADER), RootEditor.FEATURES, this.data);
    // the title of the page usually the name of the connection
    initializeHeaderPanel(Bundle.string(Bundle.ROOT_SUPPORTED_FEATURE_PANEL_TITLE), this.feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateView (AbstractPage)
  /**
   ** Called after the {@link Namespace} data that the <code>SupportPage</code>
   ** use to populate its specific components was updated.
   */
  @Override
  public void updateView() {
    updatePage();
    this.feature.updateView();
    this.extension.updateView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SupportPage</code> that populates its
   ** data from the specified {@link Namespace}.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the validated <code>SupportPage</code>.
   **                            <br>
   **                            Possible object <code>SupportPage</code>.
   */
  public static SupportPage build(final Namespace data) {
    return new SupportPage(data);
  }
}