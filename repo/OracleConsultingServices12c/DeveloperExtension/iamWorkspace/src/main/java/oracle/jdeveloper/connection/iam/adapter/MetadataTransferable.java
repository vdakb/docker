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

    File        :   MetadataTransferable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataTransferable.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.awt.datatransfer.DataFlavor;

import oracle.adf.rc.transfer.LazyTransferable;

import oracle.adf.rc.exception.CatalogException;

import oracle.ide.Ide;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle;

import oracle.jdeveloper.connection.iam.service.MetadataException;

////////////////////////////////////////////////////////////////////////////////
// class MetadataTransferable
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Implements the interface for classes that can be used to provide data for a
 ** transfer operation.
 ** <p>
 ** For information on using data transfer with Swing, see
 ** <a href="https://docs.oracle.com/javase/tutorial/uiswing/dnd/index.html">
 ** How to Use Drag and Drop and Data Transfer</a>,
 ** a section in <em>The Java Tutorial</em>, for more information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class MetadataTransferable extends LazyTransferable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String      MDS_RESOURCE        = "MDS Resource";
  private static final String     MDS_RESOURCE_STREAM = "InputStream";
  private static final DataFlavor MDS_RESOURCE_FLAVOR = new DataFlavor(MetadataDocument.class, MDS_RESOURCE);

  private final MetadataDocument  resource;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MetadataTransferable</code> for the specified
   ** {@link MetadataDocument}.
   **
   ** @param  resource           the {@link MetadataDocument} as subject of the
   **                            transfer.
   **
   ** @throws CatalogException   if the flavors are not accepted.
   */
  public MetadataTransferable(final MetadataDocument resource)
    throws CatalogException {

    // ensure inheritance
    this(resource, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MetadataTransferable</code> for the specified
   ** {@link MetadataDocument}.
   **
   ** @param  resource           the {@link MetadataDocument} as subject of the
   **                            transfer.
   ** @param  flavor             an array of data flavors in which this data can
   **                            be transferred.
   **
   ** @throws CatalogException   if the flavors are not accepted.
   */
  public MetadataTransferable(final MetadataDocument resource, final DataFlavor[] flavor)
     throws CatalogException{

    // ensure inheritance
    super(flavor);

    // initialize instance
    this.resource = resource;
    addDataFlavor(getFlavor(MDS_RESOURCE_STREAM));
    addDataFlavor(MDS_RESOURCE_FLAVOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateTransferData (LazyTransferable)
  protected Object generateTransferData(final DataFlavor flavor) {
    Object data = null;
    String name = flavor.getHumanPresentableName();
    if (MDS_RESOURCE_STREAM.equals(name)) {
      try {
        data = this.resource.provider().documentStream(this.resource.name());
      }
      catch (MetadataException e) {
        ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, Bundle.format(Bundle.RESOURCE_FOLDER_FAILED, e.getMessage()));
      }
    }
    else if (MDS_RESOURCE.equals(name)) {
      data = this.resource;
    }
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCachable (LazyTransferable)
  protected boolean isCachable(final DataFlavor flavor) {
    return !MDS_RESOURCE_STREAM.equals(flavor.getHumanPresentableName());
  }
}