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

    File        :   AuthenticationPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AuthenticationPage.


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
// class AuthenticationPage
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A flat editor page suitable to display authentication capabilities of a
 ** Directory Service RootDSE in a editor page.
 ** <p>
 ** Supported authentication password schemes is an attribute defined in RFC
 ** 3112 with an OID of 1.3.6.1.4.1.4203.1.3.3.
 ** <p>
 ** The values of this attribute are names of supported authentication
 ** password storage schemes which the service supports. The syntax of a
 ** password storage schemes names are described in
 ** <code>authPasswordSyntax</code>.
 ** <p>
 ** Supported SASL Mechanisms will appear in the RootDSE of Directory Service
 ** Implementations and the values of this attribute are the names of
 ** supported SASL mechanisms which the server supports.
 ** <p>
 ** If the service does not support any mechanisms the
 ** <code>supportedSASLMechanisms</code> attribute will be absent. 
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AuthenticationPage extends AbstractPage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3644078954613209277")
  private static final long serialVersionUID = -2671936412065229348L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private AttributePanel schemes;
  private AttributePanel mechanism;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AuthenticationPage</code> responsible to visualize the
   ** table UI of authentication capability view that populates its data from
   ** the specified {@link Namespace}.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            data.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  private AuthenticationPage(final Namespace data) {
    // ensure inheritance
    super(RootEditor.KEY, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeView (AbstractPage)
  /**
   ** The data that the <code>AuthenticationPage</code> should use to populate
   ** specific components comes from the {@link Namespace} passed to this page.
   */
  @Override
  protected final void initializeView() {
    // the header of the panel and the populated data
    this.schemes = AttributePanel.build(Bundle.string(Bundle.ROOT_AUTHN_SCHEME_PANEL_HEADER), RootEditor.SCHEMES, this.data);
    // the title of the page usually the name of the connection
    initializeHeaderPanel(Bundle.string(Bundle.ROOT_AUTHN_SCHEME_PANEL_TITLE), this.schemes);
    // the header of the panel and the populated data
    this.mechanism = AttributePanel.build(Bundle.string(Bundle.ROOT_AUTHN_MECHANISM_PANEL_HEADER), RootEditor.MECHANISMS, this.data);
    // the title of the page usually the name of the connection
    initializeHeaderPanel(Bundle.string(Bundle.ROOT_AUTHN_MECHANISM_PANEL_TITLE), this.mechanism);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateView (AbstractPage)
  /**
   ** Called after the {@link Namespace} data that the
   ** <code>AuthenticationPage</code> use to populate its specific components
   ** was updated.
   */
  @Override
  public void updateView() {
    updatePage();
    this.schemes.updateView();
    this.mechanism.updateView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>AuthenticationPage</code> that populates
   ** its data from the specified {@link Namespace}.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the validated <code>AuthenticationPage</code>.
   **                            <br>
   **                            Possible object
   **                            <code>AuthenticationPage</code>.
   */
  public static AuthenticationPage build(final Namespace data) {
    return new AuthenticationPage(data);
  }
}