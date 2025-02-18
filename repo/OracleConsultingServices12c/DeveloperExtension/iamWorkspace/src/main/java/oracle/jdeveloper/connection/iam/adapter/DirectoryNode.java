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

    File        :   DirectoryNode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryNode.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.util.List;
import java.util.Hashtable;

import javax.swing.Icon;

import javax.naming.NamingException;

import javax.naming.directory.DirContext;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.BasicAttributes;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.adf.rc.resource.ResourceType;

import oracle.adf.rc.attribute.AttributeConstants;

import oracle.adf.rc.exception.CatalogException;

import oracle.adf.rc.resource.Resource;
import oracle.adf.rc.resource.ResourceTypeManager;

import oracle.adf.rc.spi.jndi.InMemoryContext;

import oracle.adf.rc.transfer.LazyTransferable;

import oracle.jdeveloper.connection.iam.model.DirectoryName;
import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.service.DirectoryService;
import oracle.jdeveloper.connection.iam.service.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryNode
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>DirectoryNode</code> represents a structural elemente in the
 ** Directory Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class DirectoryNode extends    InMemoryContext
                           implements Resource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String     RESOURCE        = "ODS Resource";

  private static final String     ENTRY           = "Entry";
  private static final DataFlavor FLAVOR          = new DataFlavor(DirectoryDocument.class, RESOURCE);
  private static final String[]   ATTRIBUTES      = {
    AttributeConstants.TITLE
  , AttributeConstants.TYPE
  , AttributeConstants.IDENTIFIER
  , AttributeConstants.SOURCE_IDENTIFIER
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DirectoryName       name;
  protected final DirectoryService    provider;
  protected final RepositoryReference repository;

  private         Transferable        transferable;
  private         ResourceType        resourceType = null;
  private         Attributes          attributes   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Transfer
  // ~~~~~ ~~~~~~~~
  /**
   ** Implements the interface for classes that can be used to provide data for a
   ** transfer operation.
   ** <p>
   ** For information on using data transfer with Swing, see
   ** <a href="https://docs.oracle.com/javase/tutorial/uiswing/dnd/index.html">
   ** How to Use Drag and Drop and Data Transfer</a>,
   ** a section in <em>The Java Tutorial</em>, for more information.
   */
  public static class Transfer extends LazyTransferable {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final DirectoryNode resource;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Transferable</code> for the specified
     ** {@link DirectoryNode}.
     **
     ** @param  resource         the {@link DirectoryNode} as subject of the
     **                          transfer.
     **                          <br>
     **                          Allowed object is {@link DirectoryNode}.
     **
     ** @throws CatalogException if the flavors are not accepted.
     */
    public Transfer(final DirectoryNode resource)
      throws CatalogException {

      // ensure inheritance
      this(resource, null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     **
     ** @param  resource         the {@link DirectoryNode} as subject of the
     **                          transfer.
     **                          <br>
     **                          Allowed object is {@link DirectoryNode}.
     ** @param  flavor           an array of data flavors in which this data can
     **                          be transferred.
     **                          <br>
     **                          Allowed object is array of {@link DataFlavor}.
     **
     ** @throws CatalogException if the flavors are not accepted.
     */
    public Transfer(final DirectoryNode resource, final DataFlavor[] flavor)
      throws CatalogException {

      // ensure inheritance
      super(flavor);

      // initialize instance
      this.resource = resource;
      addDataFlavor(getFlavor(ENTRY));
      addDataFlavor(FLAVOR);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: generateTransferData (LazyTransferable)
    protected Object generateTransferData(final DataFlavor flavor) {
      Object data = null;
      String name = flavor.getHumanPresentableName();
      if (ENTRY.equals(name)) {
      /*
        try {
          data = this.resource.provider().documentStream(this.resource.name());
        }
        catch (MetadataException e) {
          ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, Bundle.format(Bundle.RESOURCE_FOLDER_FAILED, e.getMessage()));
        }
      */
      }
      else if (RESOURCE.equals(name)) {
        data = this.resource;
      }
      return data;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isCachable (LazyTransferable)
    protected boolean isCachable(final DataFlavor flavor) {
      return !ENTRY.equals(flavor.getHumanPresentableName());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryNode</code> for the specified
   ** {@link DirectoryService}.
   **
   ** @param  provider           the {@link DirectoryService} the
   **                            <code>DirectoryNode</code> belongs to.
   **                            <br>
   **                            Allowed object is {@link DirectoryService}.
   ** @param  name               the distinguished name of the
   **                            <code>DirectoryNode</code>.
   **                            <br>
   **                            Allowed object is {@link DirectoryService}.
   **
   ** @throws NamingException   if the flavors are not accepted.
   */
  DirectoryNode(final DirectoryService provider, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    this(provider, DirectoryName.build(provider.serverContext()), repository, catalog, env);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryNode</code> for the specified
   ** {@link DirectoryService}.
   **
   ** @param  provider           the {@link DirectoryService} the
   **                            <code>DirectoryName</code> belongs to.
   **                            <br>
   **                            Allowed object is {@link DirectoryService}.
   ** @param  name               the distinguished name of the
   **                            <code>DirectoryNode</code>.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @throws NamingException   if the flavors are not accepted.
   */
  DirectoryNode(final DirectoryService provider, final DirectoryName name, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    super(null, catalog, env);

    // initialize instance attribute
    this.name       = name;
    this.provider   = provider;
    this.repository = repository;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the {@link DirectoryService} this context belongs to.
   **
   ** @return                    the {@link DirectoryService} this context
   **                            belongs to.
   **                            <br>
   **                            Possible object is {@link DirectoryService}.
   */
  public DirectoryService provider() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entry
  /**
   ** Returns the {@link DirectoryName} of this context.
   **
   ** @return                    the {@link DirectoryName} of this context.
   **                            <br>
   **                            Possible object is {@link DirectoryName}.
   */
  public DirectoryName name() {
    return this.name;
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getShortLabel() {
    return this.name.prefix().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLongLabel (CatalogItem)
  /**
   ** Provides the long label that represents the catalog element.
   **
   ** @return                    the human readable long label of the catalog
   **                            element.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getLongLabel() {
    return this.name.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getToolTipText (CatalogItem)
  /**
   ** Returns the string to be used as the tooltip for the document.
   **
   ** @return                    the string to be used as the tooltip for the
   **                            document.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getToolTipText() {
    return this.name.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (CatalogItem)
  /**
   ** Returns an icon that can be used when rendering the current item. 
   **
   ** @return                    an icon that can be used when rendering the
   **                            current item.
   **                            <br>
   **                            Possible object is {@link Icon}.
   */
  @Override
  public Icon getIcon() {
    return this.name.symbol();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName (Resource)
  /**
   ** Provides the label that represents the catalog element.
   **
   ** @return                    the human readable label of the catalog
   **                            element.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Possible object is {@link ResourceType}.
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
          /*
          String attrId = ATTRIBUTES[i];
          String value = attribute(attrId);
          if (value != null)
            this.attributes.put(new BasicAttribute(attrId, value));
          */
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
      this.transferable = new Transfer(this);
      ;
    }
    return this.transferable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRepositoryReference (InMemoryContext)
  @Override
  public RepositoryReference getRepositoryReference() {
    return this.repository;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocalSchema (InMemoryContext)
  /**
   ** Returns a {@link DirContext} describing the schema supported by this
   ** context. 
   ** <p>
   ** The context should include a sub-context named "attributes" that contains
   ** an {@link oracle.adf.rc.attribute.AttributeDescriptor} for each attribute
   ** supported by items bound to this context.
   **
   ** @return                    a {@link DirContext} describing the schema
   **                            supported by this context.
   **                            <br>
   **                            Possible object is {@link DirContext}.
   */
  @Override
  protected DirContext getLocalSchema() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocalAttributes (InMemoryContext)
  /**
   ** Returns the attributes of this context. 
   ** <p>
   ** This method will be called when a client invokes
   ** {@link javax.naming.directory.DirContext#getAttributes} using a name that
   ** identifies this context.
   **
   ** @param  attrIds            subset of attributes to be returned.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the attributes of this context.
   **                            <br>
   **                            Possible object is {@link Attributes}.
   */
  @Override
  protected Attributes getLocalAttributes(final String[] attrIds) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadCache (InMemoryContext)
  /**
   ** Fully populates the cache of this context, adding any items that have not
   ** previously been loaded via {@link #loadCache(String)}.
   ** <p>
   ** <code>loadCache</code> is called when a method is invoked on this context 
   ** that requires access to all the items bound to this context such as
   ** list(), listBindings() or search().
   ** <p>
   ** Sub-contexts, or folders, must be represented by an instance of
   ** {@link oracle.adf.rc.core.CatalogContext} and individual resources must be
   ** represented by an instance of {@link oracle.adf.rc.resource.Resource}. It
   ** is possible for a bound object to be both a Resource and a folder.
   **
   ** @throws NamingException    if an error occurs loading the cache.
   **
   ** @see    #loadCache(String)
   */
  @Override
  protected void loadCache()
    throws NamingException {
    
    try {
      List<SearchResult> result = this.provider.search(this.name);
      for (SearchResult cursor : result) {
        final String         plain = cursor.getNameInNamespace();
        final DirectoryName  name  = DirectoryName.build(plain).symbol(DirectorySchema.classify(cursor));
        final DirectoryNode  node  = new DirectoryNode(this.provider, name, new RepositoryReference(getRepositoryReference(), plain), new CatalogReference(getCatalogReference(), plain), getEnvInternal());
        cacheObject(plain, node, null);
      }
    }
    catch (DirectoryException e) {
      throw new NamingException(e.getLocalizedMessage());
    }
  }
}