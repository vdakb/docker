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

    File        :   GeneralPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    GeneralPage.


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
// class GeneralPage
// ~~~~~ ~~~~~~~~~~~
/**
 ** A flat editor page suitable to display general capabilities of a Directory
 ** Service RootDSE in a editor page.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class GeneralPage extends AbstractPage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5216728536872636477")
  private static final long        serialVersionUID = -5655032969727061175L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  transient PropertyPanel.Property vendor;
  transient PropertyPanel.Property version;
  transient AttributePanel         support;
  transient AttributePanel         context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>GeneralPage</code> responsible to visualize the
   ** table UI of general capability view that populates its data from the
   ** specified {@link Namespace}.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            data.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  private GeneralPage(final Namespace data) {
    // ensure inheritance
    super(RootEditor.KEY, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeView (AbstractPage)
  /**
   ** The data that the <code>GeneralPage</code> should use to populate
   ** specific components comes from the {@link Namespace} passed to this page.
   */
  @Override
  protected final void initializeView() {
    final PropertyPanel panel = PropertyPanel.build();
    this.vendor  = panel.create(Bundle.ROOT_GENERAL_VENDOR_NAME_LABEL,    singleValue(RootEditor.VENDOR,         this.data));
    this.version = panel.create(Bundle.ROOT_GENERAL_VENDOR_VERSION_LABEL, singleValue(RootEditor.VENDOR_VERSION, this.data));
    this.support = AttributePanel.build(Bundle.string(Bundle.ROOT_SUPPORTED_VERSION_PANEL_HEADER), RootEditor.VERSION,  this.data);
    this.context = AttributePanel.build(Bundle.string(Bundle.ROOT_NAMING_CONTEXT_PANEL_HEADER),    RootEditor.CONTEXTS, this.data);
    // the panel to display editbale properties
    initializeHeaderPanel(Bundle.string(Bundle.ROOT_GENERAL_PROPERTY_PANEL_TITLE), panel);
    // the panel to display the supported LDAP version of the Directory Service
    initializeHeaderPanel(Bundle.string(Bundle.ROOT_SUPPORTED_VERSION_PANEL_TITLE), this.support);
    // the panel to display the naming contexts of the Directory Service
    initializeHeaderPanel(Bundle.string(Bundle.ROOT_NAMING_CONTEXT_PANEL_TITLE), this.context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateView (AbstractPage)
  /**
   ** Called after the {@link Namespace} data that the <code>GeneralPage</code>
   ** use to populate its specific components was updated.
   */
  @Override
  public void updateView() {
    updatePage();
    this.vendor.update(stringValue(RootEditor.VENDOR,          this.data));
    this.version.update(stringValue(RootEditor.VENDOR_VERSION, this.data));
    this.support.updateView();
    this.context.updateView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>GeneralPage</code> that populates its
   ** data from the specified {@link Namespace}.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the validated <code>GeneralPage</code>.
   **                            <br>
   **                            Possible object <code>GeneralPage</code>.
   */
  public static GeneralPage build(final Namespace data) {
    return new GeneralPage(data);
  }
}