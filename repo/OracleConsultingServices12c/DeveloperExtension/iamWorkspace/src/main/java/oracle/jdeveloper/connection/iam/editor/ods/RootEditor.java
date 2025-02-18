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

    File        :   RootEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    RootEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods;

import oracle.ide.Context;

import oracle.ide.model.Node;
import oracle.ide.model.Element;

import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.editor.EndpointContent;
import oracle.jdeveloper.connection.iam.editor.EndpointEditor;

import oracle.jdeveloper.connection.iam.model.DirectoryEntry;
import oracle.jdeveloper.connection.iam.model.DirectoryValue;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPage;

import oracle.jdeveloper.connection.iam.navigator.ods.DirectoryServiceRoot;

import oracle.jdeveloper.connection.iam.editor.ods.panel.root.GeneralPage;
import oracle.jdeveloper.connection.iam.editor.ods.panel.root.SupportPage;
import oracle.jdeveloper.connection.iam.editor.ods.panel.root.ControlPage;
import oracle.jdeveloper.connection.iam.editor.ods.panel.root.AuthenticationPage;

////////////////////////////////////////////////////////////////////////////////
// class RootEditor
// ~~~~~ ~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class RootEditor extends EndpointEditor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String        KEY          = "ods/dse";
  public static final String        GENERAL      = KEY + ".general";
  /**
   ** NamingContext is an attribute found in the RootDSE which specifies values
   ** of this attribute correspond to naming contexts which this DSA masters or
   ** shadows.
   ** <p>
   ** If the DSA does not master any information (e.g. it is an LDAP gateway to
   ** a public X.500 directory) the NamingContext attribute will be absent.
   ** <p>
   ** If the DSA believes it contains the entire Directory Information Base, the
   ** attribute will have a single value, and that value will be the empty
   ** string indicating the null DN of the top of the Directory Information
   ** Tree.
   ** <p>
   ** NamingContext attribute will allow a client to choose suitable BaseDN for
   ** searching when it has contacted a DSA. 
   */
  public static final String        CONTEXTS     = KEY + ".contexts";
  /**
   ** An LDAP control is an element that may be included in an LDAP Message. If
   ** it is included in a request message, it can be used to provide additional
   ** information about the way that the operation should be processed. If it is
   ** included in the response message, it can be used to provide additional
   ** information about the way the operation was processed.
   */
  public static final String        CONTROL      = KEY + ".control";
  /**
   ** Supported features is a mechanism for identifying optional capabilities
   ** that the Directory Service supports.
   ** <p>
   ** Supported features supported by the server may be listed in the
   ** <code>supportedFeatures</code> attribute of the server's RootDSE, which
   ** lists the OIDs of the supported features. 
   */
  public static final String        FEATURES     = KEY + ".features";
  /**
   ** Supported authentication password schemes is an attribute defined in RFC
   ** 3112 with an OID of 1.3.6.1.4.1.4203.1.3.3.
   ** <p>
   ** The values of this attribute are names of supported authentication
   ** password storage schemes which the service supports. The syntax of a
   ** password storage schemes names are described in
   ** <code>authPasswordSyntax</code>. 
   */
  public static final String        SCHEMES      = KEY + ".schemes";
  /**
   ** Supported SASL Mechanisms will appear in the RootDSE of Directory Service
   ** Implementations and the values of this attribute are the names of
   ** supported SASL mechanisms which the server supports.
   ** <p>
   ** If the service does not support any mechanisms the
   ** <code>supportedSASLMechanisms</code> attribute will be absent. 
   */
  public static final String        MECHANISMS   = KEY + ".mechanisms";
  /**
   ** A supported extension is a mechanism for identifying the Extended Request
   ** supported by the Directory Service. The OIDs of these extended operations
   ** are listed in the <code>supportedExtension</code> attribute of the
   ** server's RootDSE. 
   */
  public static final String        EXTENSION    = KEY + ".extension";
  /**
   ** The supported LDAP Version is found in the RootDSE and defined in RFC
   ** 4512.
   ** <p>
   ** The values of this attribute are the integer versions of the LDAP protocol
   ** which the service implements. Typical values will be "2" or "3" .
   */
  public static final String        VERSION      = KEY + ".version";
  /**
   ** Vendor name is an attribute found in the RootDSE which specifies a single
   ** string, which represents the name of the LDAP server implementer and is
   ** Defined in RCF 3045.
   ** <br>
   ** All LDAP Server Implementations SHOULD maintain a <code>vendorName</code>,
   ** which is generally the name of the company that wrote the LDAP Server code
   ** like "Oracle Corporation" 
   */
  public static final String        VENDOR       = KEY + ".vendor";
  /**
   ** Vendor version is an attribute found in the RootDSE which specifies a
   ** single string, which represents the version of the LDAP server
   ** implementation and is Defined in RCF 3045.
   ** <br>
   ** All LDAP server implementers SHOULD maintain a <code>vendorVersion</code>.
   ** Note that this value is typically a release value -- comprised of a string
   ** and/or a string of numbers -- used by the developer of the LDAP server
   ** product (as opposed to the <code>supportedLDAPVersion</code>, which
   ** specifies the version of the LDAP protocol supported by this server). 
   */
  public static final String        VENDOR_VERSION = VENDOR + ".version";

  private static final DirectoryAttribute namingContext  = DirectoryAttribute.build("namingContexts");
  private static final DirectoryAttribute control        = DirectoryAttribute.build("supportedControl");
  private static final DirectoryAttribute features       = DirectoryAttribute.build("supportedFeatures");
  private static final DirectoryAttribute extension      = DirectoryAttribute.build("supportedExtension");
  private static final DirectoryAttribute authnSchema    = DirectoryAttribute.build("supportedAuthPasswordSchemes");
  private static final DirectoryAttribute authnMechanism = DirectoryAttribute.build("supportedSASLMechanisms");
  private static final DirectoryAttribute ldapVersion    = DirectoryAttribute.build("supportedLDAPVersion");
  private static final DirectoryAttribute vendorName     = DirectoryAttribute.build("vendorName");
  private static final DirectoryAttribute vendorVersion  = DirectoryAttribute.build("vendorVersion");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             label          = null;
  private AbstractPage       general        = null;
  private AbstractPage       controls       = null;
  private AbstractPage       supported      = null;
  private AbstractPage       authentication = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RootEditor</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RootEditor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContext (overridden)
  /**
   ** Set the context behind this editor.
   **
   ** @param  context            the current editor context.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  @Override
  public synchronized void setContext(final Context context) {
    // ensure inheritance to avoid NPE
    super.setContext(context);
    if (context != null) {
      final Node node = context.getNode();
      // Do some sanity checking in case something goes wrong in the context or
      // element - don't blow up bringing up the editor.
      // This will bring up a blank tab (no editor) instead of blowing up.
      if (node instanceof DirectoryServiceRoot) {
        if (this.content == null) {
          // first time initialization for our editor.
          // Go ahead and create all our UI components based on this context.
          this.label = ((DirectoryServiceRoot)node).manageable().service().resource().name();
          initializeEditor();
        }
        else {
          updateEditor();
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeEditor
  /**
   ** This method is reponsible for initializing the view.
   ** <br>
   ** It should be called at the end of the view constructor.
   */
  private void initializeEditor() {
    // *** DON"T DO ANY INITIALIZATION WORK UNTIL AFTER COMMENT BELOW **
    //
    // Reason: initialization maybe aborted up to that point

    // Following detector can abort initialization, so don't do any set up
    // before this.
    populate();

    // *** INITIALIZATION FROM HERE ONWARDS ***
    this.content        = new EndpointContent();
    this.general        = GeneralPage.build(this.data);
    this.controls       = ControlPage.build(this.data);
    this.supported      = SupportPage.build(this.data);
    this.authentication = AuthenticationPage.build(this.data);
    addContent(Bundle.string(Bundle.ROOT_GENERAL_PAGE_TITLE), this.general);
    addContent(Bundle.string(Bundle.ROOT_AUTHN_PAGE_TITLE),   this.authentication);
    addContent(Bundle.string(Bundle.ROOT_CONTROL_PAGE_TITLE), this.controls);
    addContent(Bundle.string(Bundle.ROOT_SUPPORT_PAGE_TITLE), this.supported);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateEditor
  /**
   ** Update the context associated with this entry editor and editor component.
   */
  private void updateEditor() {
    populate();
    this.general.updateView();
    this.controls.updateView();
    this.supported.updateView();
    this.authentication.updateView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Puts the objects to the data container in the cache.
   */
  void populate() {
    // the node being edited by this editor
    final Element element = getContext().getNode();
    this.data.put(KEY, element.getShortLabel());
    final DirectoryEntry context = ((DirectoryServiceRoot)element).manageable().entry();
    transfer(CONTEXTS,       context.remove(namingContext));
    transfer(CONTROL,        context.remove(control));
    transfer(FEATURES,       context.remove(features));
    transfer(EXTENSION,      context.remove(extension));
    transfer(SCHEMES,        context.remove(authnSchema));
    transfer(MECHANISMS,     context.remove(authnMechanism));
    transfer(VERSION,        context.remove(ldapVersion));
    transfer(VENDOR,         context.remove(vendorName));
    transfer(VENDOR_VERSION, context.remove(vendorVersion));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Puts an object to the data container in the cache mapped to the specified
   ** <code>path</code>.
   **
   ** @param  path               the key of the path the {@link Object} is
   **                            mapped to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  source             the {@link Object} to transfer to the cache.
   **                            <br>
   **                            Allowed object is {@link DirectoryValue}.
   */
  private void transfer(final String path, final DirectoryValue source) {
    if (source != null)
      this.data.put(path, source);
  }
}