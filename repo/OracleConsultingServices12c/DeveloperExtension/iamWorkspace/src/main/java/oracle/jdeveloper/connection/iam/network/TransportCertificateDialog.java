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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   TransportCertificateDialog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TransportCertificateDialog.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.network;

import java.util.Set;

import java.security.cert.X509Certificate;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import java.security.MessageDigest;

import javax.swing.JLabel;

import oracle.bali.ewt.dialog.DialogHeader;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.bali.ewt.text.MultiLineLabel;

import oracle.ide.Ide;

import oracle.ide.panels.DefaultTraversablePanel;

import oracle.ide.panels.TDialogLauncher;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.connection.iam.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class TransportCertificateDialog
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the confirmation dialog to accept untrusted certificates.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
class TransportCertificateDialog extends DefaultTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String       CERTIFICATE = "certificate";
  static final String       SERVER_NAME = "server-name";
  static final String       SERVER_PORT = "server-port";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5899847873194053695")
  private static final long serialVersionUID = -3407107820939474196L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             serverName;
  private X509Certificate[]  certificate;

  private MultiLineLabel     introduction;
  private JList<String>      certificateView;
  private JTextArea          descriptionView;
  private JScrollPane        certificatePane;
  private JScrollPane        descriptionPane;
  private JLabel             issuedTO;
  private JLabel             issuedFrom;
  private JLabel             validFrom;
  private JLabel             validTo;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class DescriptionViewer
  // ~~~~~ ~~~~~~~~~~~~~~~~~~
  /**
   ** Implementation of data classes which can be integrated with the IDE
   ** framework.
   */
  class DescriptionViewer implements ListSelectionListener {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>DescriptionViewer</code>.
     */
    DescriptionViewer() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: popupMenuWillBecomeVisible (PopupMenuListener)
    /**
     ** Called whenever the value of the selection changes.
     **
     ** @param  event            the event that characterizes the change.
     **                          <br>
     **                          Allowed object is {@link ListSelectionEvent}.
     */
    @Override
    public final void valueChanged(final ListSelectionEvent event) {
      TransportCertificateDialog.this.showDescription(TransportCertificateDialog.this.certificateView.getSelectedIndex());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TransportCertificatePanel</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private TransportCertificateDialog(final Set<X509Certificate> certificate, final String serverName) {
    // ensure inheritance
    super();
    
    // initialize instance attributes
    this.serverName  = serverName;
    this.certificate = certificate.toArray(new X509Certificate[0]);

    // initialize UI components
    initializeComponent();
    localizeComponent();
    initializeLayout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launch
  /**
   ** Launches the dialog dialog with <code>OK</code> and <code>Cancel</code>
   ** buttons.
   **
   ** @param  certificate        the certificates to confirm.
   ** @param  serverName         the name of the server involved in the
   **                            handshake.
   **
   ** @return                    <code>true</code> if <code>OK</code> was
   **                            selected; <code>false</code> if
   **                            <code>Cancel</code> was selected or if the
   **                            Traversable associated with the commit action
   **                            is <code>null</code>.
   */
  public static boolean launch(final Set<X509Certificate> certificate, final String serverName) {
    final TDialogLauncher launcher = new TDialogLauncher(Ide.getMainWindow(), Bundle.string(Bundle.CERTIFICATE_DIALOG_TITLE), new TransportCertificateDialog(certificate, serverName), null);
    launcher.setDialogHeader(new DialogHeader(Bundle.string(Bundle.CERTIFICATE_DIALOG_HEADER), Bundle.image(Bundle.CERTIFICATE_DIALOG_IMAGE).getImage()));
    launcher.setPackDialog(true);
    final JEWTDialog dialog = launcher.initDialog();
    dialog.setOKButtonText(Bundle.string(Bundle.CERTIFICATE_ACCEPT_LABEL));
    dialog.setCancelButtonText(Bundle.string(Bundle.CERTIFICATE_NOTACCEPT_LABEL));
    // Shows dialog with OK, Cancel, and Help buttons.
    // The specified Namespace must not be null, or else an
    // IllegalArgumentException is thrown.
    return launcher.showDialog();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onEntry (overridden)
  /**
   ** This method is called when the <code>Traversable</code> is being entered.
   ** <p>
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the specified {@link TraversableContext}.
   ** <p>
   ** When the same <code>Traversable</code> is entered more than once in the
   ** course of interacting with the user, the <code>Traversable</code> needs to
   ** reload the data directly from the {@link TraversableContext} rather than
   ** caching data objects. Some reasons for this include:
   ** <ul>
   **   <li>Other <code>Traversable</code> may edit the data objects or even
   **       replace them.
   **   <li>The same <code>Traversable</code> instance may be used for editing
   **       multiple different instances of the same object type.
   ** </ul>
   ** Loading data directly from the {@link TraversableContext} is the best way
   ** to ensure that the <code>Traversable</code> will not be editing the wrong
   ** data.
   ** <p>
   ** The <code>Traversable</code> should not even cache references to data
   ** objects between invocations of onEntry and
   ** {@link #onExit(TraversableContext)} because the UI container is not
   ** required to guarantee that the references will be identical.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   */
  @Override
  public void onEntry(final TraversableContext context) {
    this.introduction.setText(Bundle.format(Bundle.CERTIFICATE_TEXT_LABEL, this.serverName));
    final DefaultListModel<String> model = (DefaultListModel<String>)this.certificateView.getModel();
    for (X509Certificate cursor : this.certificate) {
      model.addElement(cursor.getSubjectDN().getName());
    }
    this.certificateView.setSelectedIndex(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  protected void initializeComponent() {
    this.introduction    = new MultiLineLabel();

    this.certificateView = new JList<String>(new DefaultListModel<String>());
    this.descriptionView = new JTextArea();
    this.certificatePane = new JScrollPane(this.certificateView);
    this.descriptionPane = new JScrollPane(this.descriptionView);

    this.certificateView.addListSelectionListener(new DescriptionViewer());

    this.certificatePane.setPreferredSize(new Dimension(300,  50));

    this.descriptionPane.setPreferredSize(new Dimension(300, 150));
    this.descriptionPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.descriptionPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the panel.
   */
  protected void initializeLayout() {
    final FormLayout         layout     = new FormLayout(
    //   |     1     |
      "4dlu, pref, 4dlu"
    , "6dlu, pref, 6dlu, pref, 3dlu, pref, 6dlu, pref, 3dlu, pref, 6dlu"
    //   |    1     |     2     |     3     |      4    |      5     |
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.add(this.introduction, constraint.xy(2, row));

    // the 2nd logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.CERTIFICATE_LIST_LABEL), constraint.xy(2, row));

    // the 3rd logical row of the layout
    row += 2;
    builder.add(this.certificatePane, constraint.xy(2, row));

    // the 4th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.CERTIFICATE_DESCRIPTION_LABEL), constraint.xy(2, row));

    // the 5th logical row of the layout
    row += 2;
    builder.add(this.descriptionPane, constraint.xy(2, row));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  protected final void localizeComponent() {
  }

  private void showDescription(int index) {
    if ((index >= this.certificateView.getModel().getSize()) || (index < 0))
      this.descriptionView.setText(null);
    else {
      final X509Certificate cert = this.certificate[index];
      final StringBuilder   bldr = new StringBuilder(" " + (index + 1) + " Subject " + cert.getSubjectDN());
      try {
      bldr.append('\n');
      bldr.append('\n');
        final MessageDigest   sha1 = MessageDigest.getInstance("SHA1");
        final MessageDigest   md5  = MessageDigest.getInstance("MD5");
        bldr.append("   CN      " + TransportSocketFactory.commonName(cert));
        bldr.append('\n');
        bldr.append('\n');
        bldr.append("   Issuer  " + cert.getIssuerDN());
        bldr.append('\n');
        bldr.append('\n');
        sha1.update(cert.getEncoded());
        bldr.append("   sha1    " + toHexString(sha1.digest()));
        bldr.append('\n');
        bldr.append('\n');
        md5.update(cert.getEncoded());
        bldr.append("   md5     " + toHexString(md5.digest()));
        bldr.append('\n');
      }
      catch (Exception e) {
        bldr.append('\n');
        bldr.append(e.getLocalizedMessage());
        bldr.append('\n');
      }
      this.descriptionView.setText(bldr.toString());
    }
  }

  static String toHexString(final byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 3);
    for (int b : bytes) {
      sb.append(String.format("%02x ", b & 0xff));
    }
    return sb.toString();
  }
}
