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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   FileSystemNode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FileSystemNode.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.awt.datatransfer.DataFlavor;

import java.awt.datatransfer.Transferable;

import java.util.Map;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import javax.swing.Icon;

import javax.naming.NamingException;

import javax.naming.directory.DirContext;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import javax.naming.directory.InvalidAttributeIdentifierException;

import oracle.adf.rc.resource.Resource;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.adf.rc.attribute.AttributeConstants;

import oracle.adf.rc.exception.CatalogException;

import oracle.adf.rc.resource.ResourceType;
import oracle.adf.rc.resource.ResourceTypeManager;
import oracle.adf.rc.spi.jndi.InMemoryContext;

import oracle.adf.rc.transfer.LazyTransferable;

import oracle.ide.Ide;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// class FileSystemNode
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>Direc~FileSystemNode</code> represents a structural elemente in a
 ** file system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.104
 ** @since   12.2.1.3.42.60.104
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class FileSystemNode extends    InMemoryContext
                            implements Resource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[]     ATTRIBUTES     = {
    AttributeConstants.TITLE
  , AttributeConstants.TYPE
  , AttributeConstants.IDENTIFIER
  , AttributeConstants.SOURCE_IDENTIFIER
  , AttributeConstants.CREATION_DATE_ATTR
  , AttributeConstants.LAST_UPDATE_DATE_ATTR
  };

  private static final String      SOURCE          = "InputStream";
  private static final String      MIMETYPE        = "text/xml";
  private static final DataFlavor  FLAVOR          = new DataFlavor(FileSystemNode.class, MIMETYPE);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final File                path;
  protected final RepositoryReference repository;

  protected Attributes                attributes   = null;
  protected Transferable              transferable = null;
  protected ResourceType              resourceType = null;

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

    private final File resource;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Transfer</code> for the specified
     ** {@link File}.
     **
     ** @param  resource           the {@link File} as subject of the transfer.
     **
     ** @throws CatalogException   if the flavors are not accepted.
     */
    public Transfer(final File resource)
      throws CatalogException {

      // ensure inheritance
      this(resource, null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Transfer</code> for the specified
     ** {@link File}.
     **
     ** @param  resource           the {@link File} as subject of the transfer.
     ** @param  flavor             an array of data flavors in which this data
     **                            can be transferred.
     **
     ** @throws CatalogException   if the flavors are not accepted.
     */
    public Transfer(final File resource, final DataFlavor[] flavor)
       throws CatalogException {

      // ensure inheritance
      super(flavor);

      // initialize instance
      this.resource = resource;
      addDataFlavor(FLAVOR);
      addDataFlavor(getFlavor(SOURCE));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: isCachable (LazyTransferable)
    protected boolean isCachable(final DataFlavor flavor) {
      return MIMETYPE.equals(flavor.getMimeType());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: generateTransferData (LazyTransferable)
    protected Object generateTransferData(final DataFlavor flavor) {
      Object data = null;
      String name = flavor.getHumanPresentableName();
      if (SOURCE.equals(name)) {
        try {
          data = new FileInputStream(this.resource);
        }
        catch (FileNotFoundException e) {
          ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle.format(oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle.RESOURCE_FOLDER_FAILED, e.getMessage()));
        }
      }
      return data;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FileSystemNode</code> for the specified
   ** properties.
   **
   ** @param  path               the abstrcat path name to access.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  repository         the {@link RepositoryReference} this file node
   **                            references in the repository.
   **                            <br>
   **                            Allowed object is {@link RepositoryReference}.
   ** @param  catalog            the {@link CatalogReference} this file node
   **                            references in the resource catalog.
   **                            <br>
   **                            Allowed object is {@link CatalogReference}.
   **
   ** @throws NamingException    if the flavors are not accepted.
   */
  FileSystemNode(final File path, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    super(null, catalog, env);

    // initialize instance attribute
    this.path       = path;
    this.repository = repository;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (overridden)
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
    return this.path.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLongLabel (overridden)
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
    return this.path.getPath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getToolTipText (overridden)
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
    return this.path.getPath();
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
    return this.path.isFile() ? Bundle.icon(Bundle.FILESYSTEM_ICON_XML) : ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON);
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
      this.transferable = new Transfer(this.path);
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
    if (this.attributes == null) {
      this.attributes = new BasicAttributes();
      this.attributes.put(new BasicAttribute("Title",            this.path));
      this.attributes.put(new BasicAttribute("Identifier",       getCatalogReference().toString()));
      this.attributes.put(new BasicAttribute("SourceIdentifier", getRepositoryReference().toString()));
    }
    return this.attributes;
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

    // list all files
    final File[] files = this.path.listFiles();
    if (files != null && files.length > 0) {
      final Hashtable                   env        = getEnvInternal();
      final CatalogReference            catalog    = getCatalogReference();
      final RepositoryReference         repository = getRepositoryReference();
      final Map<File, FileSystemNode> postponed  = new LinkedHashMap<>();
      for (File cursor : files) {
        final String         path = cursor.getAbsolutePath();
        final FileSystemNode node = new FileSystemNode(cursor, new RepositoryReference(repository, path), new CatalogReference(catalog, path), env);
        if (cursor.isDirectory())
          cacheObject(path, node, null);
        else
          postponed.put(cursor, node);
      }
      if (postponed.size() > 0) {
        for (Map.Entry<File, FileSystemNode> cursor : postponed.entrySet())
          cacheObject(cursor.getKey().getAbsolutePath(), cursor.getValue(), null);
      }
    }
  }

  private String attribute(final String name)
    throws NamingException {

    // prevent bogus input
    if (StringUtility.empty(name))
      throw new InvalidAttributeIdentifierException(name);

    if (StringUtility.equal(AttributeConstants.TITLE, name)) {
      return getName();
    }
    if (StringUtility.equal(AttributeConstants.IDENTIFIER, name)) {
      return getCatalogReference().toString();
    }
    if (StringUtility.equal(AttributeConstants.SOURCE_IDENTIFIER, name)) {
      return getRepositoryReference().toString();
    }
    if (StringUtility.equal(AttributeConstants.CREATION_DATE_ATTR, name)) {
      return getRepositoryReference().toString();
    }
    if (StringUtility.equal(AttributeConstants.TYPE, name)) {
      final ResourceType type = getType();
      return type == null ? null : type.getId();
    }
    // prevent bogus input
    throw new InvalidAttributeIdentifierException(name);
  }
}