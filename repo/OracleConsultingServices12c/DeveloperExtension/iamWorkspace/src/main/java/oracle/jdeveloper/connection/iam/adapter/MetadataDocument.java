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

    File        :   MetadataDocument.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataDocument.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.awt.datatransfer.Transferable;

import javax.swing.Icon;

import javax.naming.NamingException;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InvalidAttributeIdentifierException;

import oracle.adf.rc.resource.Resource;
import oracle.adf.rc.resource.ResourceType;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.adf.rc.exception.CatalogException;

import oracle.adf.rc.resource.ResourceTypeManager;

import oracle.adf.rc.attribute.AttributeConstants;

import oracle.mds.naming.DocumentName;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.service.MetadataService;

////////////////////////////////////////////////////////////////////////////////
// class MetadataDocument
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>MetadataDocument</code> represents the name of any document within
 ** a Metadata Service. Specifically, it can represent the absolute name
 ** reference of either a Metadata Document (or base document) or a special
 ** document.
 ** <h3>Terminologies used:</h3>
 ** The following terminologies are used in the various parts of API
 ** documentation in this class.
 ** <ul>
 **   <li>Absolute document name: This represents the complete document name
 **                               along with package name(also called as
 **                               absolute document name).
 **                               E.g. "/oracle/apps/ak/page1.xml"
 **   <li>package name:           This represents the package part of the
 **                               absolute documentname.
 **                               <br>
 **                               E.g. Absolute document name = "/oracle/apps/ak/page1.xml"
 **                               <br>
 **                               =&gt; package name = "/oracle/apps/ak"
 **   <li>name:                   This represents the name of the document alone.
 **                               <br>
 **                               E.g. Absolute document name = "/oracle/apps/ak/page1.xml"
 **                               <br>
 **                               =&gt; package name = "/oracle/apps/ak"
 **                               <br>
 **                               =&gt; name = "page1.xml"
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class MetadataDocument implements Resource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[]     ATTRIBUTES = {
    AttributeConstants.TITLE
  , AttributeConstants.TYPE
  , AttributeConstants.IDENTIFIER
  , AttributeConstants.SOURCE_IDENTIFIER
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final MetadataService     provider;

  private final DocumentName        document;
  private final RepositoryReference repository;
  private final CatalogReference    catalog;

  private       Transferable        transferable;
  private       ResourceType        resourceType = null;
  private       Attributes          attributes   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MetadataDocument</code> for the specified
   ** properties.
   **
   ** @param  document           the {@link DocumentName} to access.
   ** @param  repository         the {@link RepositoryReference} this package
   **                            references in the repository.
   ** @param  catalog            the {@link CatalogReference} this package
   **                            references in the resource catalog.
   **
   ** @throws NamingException    if the flavors are not accepted.
   */
  MetadataDocument(final DocumentName document, final MetadataService provider, final RepositoryReference repository, final CatalogReference catalog) {
    // ensure inheritance
    super();

    // initialize instance attribute
    this.provider   = provider;
    this.document   = document;
    this.repository = repository;
    this.catalog    = catalog;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the {@link MetadataService} this document belongs to.
   **
   ** @return                    the {@link MetadataService} this document
   **                            belongs to.
   */
  public MetadataService provider() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the {@link DocumentName} of this document.
   **
   ** @return                    the {@link DocumentName} of this document.
   */
  public DocumentName name() {
    return this.document;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (CatalogItem)
  /**
   ** Provides the label that represents the catalog element.
   **
   ** @return                    the human readable label of the catalog
   **                            element.
   */
  @Override
  public String getShortLabel() {
    return this.document.getLocalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLongLabel (CatalogItem)
  /**
   ** Provides the long label that represents the catalog element.
   **
   ** @return                    the human readable long label of the catalog
   **                            element.
   */
  @Override
  public String getLongLabel() {
    return this.document.getAbsoluteName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getToolTipText (CatalogItem)
  /**
   ** Returns the string to be used as the tooltip for the document.
   **
   ** @return                    the string to be used as the tooltip for the
   **                            document.
   */
  @Override
  public String getToolTipText() {
    return this.document.getAbsoluteName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (CatalogItem)
  /**
   ** Returns an icon that can be used when rendering the current item. 
   **
   ** @return                     an icon that can be used when rendering the
   **                             current item.
   */
  @Override
  public Icon getIcon() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCatalogReference (CatalogItem)
  /**
   ** Sets the value of the {@link CatalogReference} for this {@link Resource}.
   ** <p>
   ** A {@link CatalogReference} identifies a catalog item within the context of
   ** a  specific catalog. When a {@link Resource} is linked into a catalog, the
   ** RC will  notify it of its {@link CatalogReference} within that catalog by
   ** calling this method. If a parent context is renamed at any point, this
   ** method will be  called again to notify the {@link Resource} of its new
   ** name within the catalog.
   ** <p>
   ** <b>IMPORTANT</b>
   ** <br>
   ** All objects within a catalog must have a {@link CatalogReference}. It is
   ** the responsibility of the <code>CatalogContext</code> creating or renaming
   ** a child <code>CatalogItem</code> (<code>CatalogContext</code> or
   ** {@link Resource}) to notify that object of its {@link CatalogReference}
   ** based on its own reference and the relative location of the item being
   ** created/renamed.
   **
   ** @param  catalog            the unique reference for this object within the
   **                            context of a specific catalog.
   **
   ** @throws NamingException    if an exception occurs setting the reference or
   **                            notifying children of changes to their
   **                            references.
   */
  @Override
  public void setCatalogReference(final CatalogReference catalog)
    throws NamingException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCatalogReference (CatalogItem)
  /**
   ** Returns the {@link CatalogReference} that uniquely identifies this item
   ** within the  context of the catalog in which it is being used.
   ** <p>
   ** This method is somewhat similar to <code>getNameInNamespace()</code> but
   ** instead of returning the fullname of the object within its own namespace,
   ** the value that should be returned is a {@link CatalogReference} that
   ** uniquely identifies this object within the scope of the current catalog.
   ** <p>
   ** All {@link Resource}s should be notified of their catalog identifier
   ** via the <code>setCatalogIdentifier()</code> method when they are created
   ** or a  parent <code>CatalogContext</code> is renamed.
   ** <p>
   ** It is the responsibility of the <code>CatalogContext</code> creating or
   ** renaming a child object to notify the child object of its
   ** {@link CatalogReference} based on its {@link CatalogReference} and the
   ** relative location of the object being created/renamed.
   **
   ** @return                    the {@link CatalogReference} for this object.
   */
  @Override
  public CatalogReference getCatalogReference()
    throws NamingException {

    return this.catalog;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRepositoryReference (CatalogItem)
  @Override
  public RepositoryReference getRepositoryReference() {
    return this.repository;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName (Resource)
  /**
   ** Provides the label that represents the catalog element.
   **
   ** @return                    the human readable label of the catalog
   **                            element.
   */
  @Override
  public String getName() {
    return getShortLabel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType (Resource)
  /**
   ** Provides the type that represents the catalog element.
   **
   ** @return                    the type of the catalog element.
   */
  @Override
  public ResourceType getType()
    throws CatalogException {

    if (this.resourceType == null) {
      final ResourceTypeManager mgr = ResourceTypeManager.getInstance();
      this.resourceType = mgr.findResourceType(this);
    }
    return this.resourceType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributes (Resource)
  @Override
  public Attributes getAttributes()
    throws CatalogException {

    if (this.attributes == null) {
      try {
        this.attributes = new BasicAttributes();
        for (int i = 0; i < ATTRIBUTES.length; i++) {
          String attrId = ATTRIBUTES[i];
          String value = attribute(attrId);
          if (value != null)
            this.attributes.put(new BasicAttribute(attrId, value));
        }
      }
      catch (Exception e) {
        throw new CatalogException(e);
      }
    }
    return this.attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTransferable (Resource)
  @Override
  public Transferable getTransferable()
    throws CatalogException {

    if (this.transferable == null) {
      this.transferable = new MetadataTransferable(this);
    }
    return this.transferable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  private String attribute(final String attrName)
    throws NamingException {

    // prevent bogus input
    if (StringUtility.empty(attrName))
      throw new InvalidAttributeIdentifierException(attrName);

    if (StringUtility.equal(AttributeConstants.TITLE, attrName)) {
      return getName();
    }
    if (StringUtility.equal(AttributeConstants.IDENTIFIER, attrName)) {
      return getCatalogReference().toString();
    }
    if (StringUtility.equal(AttributeConstants.SOURCE_IDENTIFIER, attrName)) {
      return getRepositoryReference().toString();
    }
    if (StringUtility.equal(AttributeConstants.TYPE, attrName)) {
      final ResourceType type = getType();
      return type == null ? null : type.getId();
    }
    // prevent bogus input
    throw new InvalidAttributeIdentifierException(attrName);
  }
}