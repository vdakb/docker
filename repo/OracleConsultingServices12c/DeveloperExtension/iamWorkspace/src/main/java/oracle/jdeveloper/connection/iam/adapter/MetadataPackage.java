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

    File        :   MetadataPackage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataPackage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.util.Iterator;
import java.util.Hashtable;

import javax.naming.NamingException;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import javax.naming.directory.DirContext;

import javax.swing.Icon;

import oracle.mds.naming.PackageName;
import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ResourceName;

import oracle.mds.query.QueryResult;
import oracle.mds.query.DocumentResult;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.adf.rc.spi.jndi.InMemoryContext;

import oracle.adf.rc.attribute.AttributeConstants;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.service.MetadataService;
import oracle.jdeveloper.connection.iam.service.MetadataException;

import oracle.mds.query.PackageResult;
import oracle.mds.query.ResourceQuery;

////////////////////////////////////////////////////////////////////////////////
// class MetadataPackage
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>MetadataPackage</code> represents a structural elemente in the
 ** Metadata Repository.
 ** <p>
 ** A package is a resource which can contain other resources such as
 ** MetadataObject documents, customization documents and other packages (sub
 ** packages). Packages help organize the metadata content in hierarchical
 ** structure.
 ** <p>
 ** <code>MetadataPackage</code> represents the absolute name of any package.
 ** <br>
 ** Examples of valid packages:
 ** <pre>
 **   /
 **   /mypkg
 **   /mypkg/subpkg
 **   /mypkg/mdssys
 **   /mypkg/mdssys/cust/user/x
 **   /mypkg/mdssys/trans/fr-FR/
 **   /mypkg/mdssys/cust/user/x/trans/fr-FR
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class MetadataPackage extends InMemoryContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** seeded protected namespace **/
  static final String                DB           = "/db";
  /** seeded protected namespace **/
  static final String                FILE         = "/file";
  /** seeded protected namespace **/
  static final String                CUSTOM       = "/custom";
  /** seeded protected namespace **/
  static final String                MDS          = "/md2";
  /** seeded protected namespace **/
  static final String                METADATA     = "/metadata";
  /** seeded protected namespace **/
  static final String                ORACLE       = "/oracle";
  /** seeded protected namespace **/
  static final String                META_INF     = "/META-INF";
  /** seeded protected namespace **/
  static final String                WEB_INF      = "/WEB-INF";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final MetadataService     provider;
  protected final RepositoryReference repository;

  protected PackageName               name        = null;
  protected Attributes                attributes  = null;

  private   Boolean                   reserved;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MetadataPackage</code> for the specified
   ** properties.
   **
   ** @param  provider           the {@link MetadataService} to access.
   ** @param  repository         the {@link RepositoryReference} this package
   **                            references in the repository.
   ** @param  catalog            the {@link CatalogReference} this package
   **                            references in the resource catalog.
   **
   ** @throws NamingException    if the flavors are not accepted.
   */
  MetadataPackage(final MetadataService provider, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    super(null, catalog, env);

    // initialize instance attribute
    this.provider   = provider;
    this.repository = repository;
    try {
      this.name = PackageName.createPackageName("/");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MetadataPackage</code> for the specified
   ** {@link MetadataDocument}.
   **
   ** @param  name               the {@link PackageName} to access.
   ** @param  provider           the {@link MetadataService} to access.
   ** @param  repository         the {@link RepositoryReference} this package
   **                            references in the repository.
   ** @param  catalog            the {@link CatalogReference} this package
   **                            references in the resource catalog.
   **
   ** @throws NamingException    if the flavors are not accepted.
   */
  private MetadataPackage(final PackageName name, final MetadataService provider, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    super(null, catalog, env);

    // initialize instance attribute
    this.provider   = provider;
    this.repository = repository;
    this.name       = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  public MetadataService provider() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  public PackageName name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reserved
  public boolean reserved() {
    if (this.reserved == null) {
      this.reserved = Boolean.valueOf(reserved(this.name));
    }
    return this.reserved.booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (overridden)
  /**
   ** Returns the graphic image (glyph, icon) that the label displays.
   **
   ** @return                     the graphic image (glyph, icon) that the label
   **                             displays.
   */
  @Override
  public Icon getIcon() {
    return Bundle.icon(Bundle.METADATA_PACKAGE_ICON);
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
   **
   ** @return                    the attributes of this context.
   **
   ** @throws NamingException    if an error occurs returning the attributes.
   */
  @Override
  protected Attributes getLocalAttributes(final String[] attrIds)
    throws NamingException {

    if (this.attributes == null) {
      this.attributes = new BasicAttributes();
      this.attributes.put(new BasicAttribute(AttributeConstants.TITLE,             this.name.getLocalName()));
      this.attributes.put(new BasicAttribute(AttributeConstants.IDENTIFIER,        getCatalogReference().toString()));
      this.attributes.put(new BasicAttribute(AttributeConstants.SOURCE_IDENTIFIER, getRepositoryReference().toString()));
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

    ResourceQuery query = null;
    try {
      query = this.provider.resourceQuery(this.name);
      final Iterator<QueryResult> contents = query.execute();
      while (contents.hasNext()) {
        final QueryResult result = contents.next();
        final String name = result.getResourceName().getLocalName();
        if (result.getResultType() == QueryResult.ResultType.PACKAGE_RESULT) {
          // skip access to the META-INF folder to prevent incidents
//          if ("META-INF".equals(name))
//            continue;
          final MetadataPackage context = new MetadataPackage(((PackageResult)result).getPackageName(), this.provider, new RepositoryReference(getRepositoryReference(), name), new CatalogReference(getCatalogReference(), name), getEnvInternal());
          cacheObject(name, context, null);
        }
        else {
          final MetadataDocument context = new MetadataDocument(((DocumentResult)result).getDocumentName(), this.provider, new RepositoryReference(getRepositoryReference(), name), new CatalogReference(getCatalogReference(), name));
          cacheObject(name, context, null);
        }
      }
    }
    catch (MetadataException e) {
      throw new NamingException(e.getLocalizedMessage());
    }
    finally {
      if (query != null)
        query.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteable
  /**
   ** Returns <code>true</code> if the specified {@link ResourceName}
   ** <code>resourceName</code> can be deleted.
   ** <br>
   ** General speaking the specified {@link ResourceName}
   ** <code>resourceName</code> isn't contained in the set of reserved packages.
   **
   ** @param  resourceName       the {@link ResourceName} to verify.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link ResourceName} <code>resourceName</code>
   **                            can be deleted; otherwise <code>false</code>.
   */
  public static boolean deleteable(final ResourceName resourceName) {
    return (!reservedNamespace(resourceName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reserved
  /**
   ** Returns <code>true</code> if the specified {@link PackageName}
   ** <code>packageName</code> is a protected namespace.
   **
   ** @param  packageName        the {@link PackageName} to verify.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link PackageName} <code>packageName</code> is
   **                            protected; otherwise <code>false</code>.
   */
  public static boolean reserved(final PackageName packageName) {
    return reservedNamespace(packageName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reserved
  /**
   ** Returns <code>true</code> if the specified {@link DocumentName}
   ** <code>documentName</code> is a protected namespace.
   **
   ** @param  documentName       the {@link DocumentName} to verify.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link DocumentName} <code>documentName</code>
   **                            is protected; otherwise <code>false</code>.
   */
  public static boolean reserved(final DocumentName documentName) {
    return reservedNamespace(documentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reserved
  /**
   ** Returns <code>true</code> if the specified {@link ResourceName}
   ** <code>resourceName</code> is a protected namespace.
   **
   ** @param  resourceName       the {@link ResourceName} to verify.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link ResourceName} <code>resourceName</code>
   **                            is protected; otherwise <code>false</code>.
   */
  private static boolean reservedNamespace(final ResourceName resourceName) {
    final String  absoluteName = resourceName.getAbsoluteName();
    boolean reserved = absoluteName.equals(DB);
    reserved = (reserved) || (absoluteName.equals(MDS));
    reserved = (reserved) || (absoluteName.equals(FILE));
    reserved = (reserved) || (absoluteName.equals(CUSTOM));
    reserved = (reserved) || (absoluteName.equals(ORACLE));
    reserved = (reserved) || (absoluteName.equals(METADATA));
    reserved = (reserved) || (absoluteName.startsWith(META_INF));
    reserved = (reserved) || (absoluteName.startsWith(WEB_INF));
    return reserved;
  }
}