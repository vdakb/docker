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
    Subsystem   :   Identity Manager Facility

    File        :   IdentityContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityContext.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.adapter;

import java.util.Hashtable;

import javax.naming.NamingException;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import javax.naming.directory.DirContext;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.adf.rc.spi.jndi.InMemoryContext;

import oracle.jdeveloper.connection.oim.model.IdentityServer;

////////////////////////////////////////////////////////////////////////////////
// class IdentityContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>IdentityContext</code> represents a structural elemente in the
 ** Identity Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class IdentityContext extends InMemoryContext {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected IdentityServer      provider   = null;
  protected RepositoryReference repository = null;
  protected Attributes          attributes = null;
  protected String              name       = null;

  IdentityContext(final IdentityServer provider, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    this("/", provider, repository, catalog, env);
  }

  protected IdentityContext(final String name, final IdentityServer provider, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    super(null, catalog, env);

    // initialize instance attribute
    this.provider   = provider;
    this.repository = repository;
    this.name       = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRepositoryReference (InMemoryContext)
  @Override
  public RepositoryReference getRepositoryReference()
    throws NamingException {

    return this.repository;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocalSchema (InMemoryContext)
  @Override
  protected DirContext getLocalSchema() throws NamingException {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocalAttributes (InMemoryContext)
  @Override
  protected Attributes getLocalAttributes(final String[] attrIds)
    throws NamingException {

    if (this.attributes == null) {
      this.attributes = new BasicAttributes();
      this.attributes.put(new BasicAttribute("Title",            this.name));
      this.attributes.put(new BasicAttribute("Identifier",       getCatalogReference().toString()));
      this.attributes.put(new BasicAttribute("SourceIdentifier", getRepositoryReference().toString()));
    }
    return this.attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadCache (InMemoryContext)
  @Override
  protected void loadCache()
    throws NamingException {

    RepositoryReference repository = new RepositoryReference(getRepositoryReference(), IdentityProcessContext.LOCAL);
    CatalogReference    catalog    = new CatalogReference(getCatalogReference(),       IdentityProcessContext.LOCAL);
    IdentityContext     context    = new IdentityProcessContext(this.provider, repository, catalog,  getEnvInternal());
    cacheObject(IdentityProcessContext.LOCAL, context, null);

    repository = new RepositoryReference(getRepositoryReference(), IdentityAdministrationContext.LOCAL);
    catalog    = new CatalogReference(getCatalogReference(),       IdentityAdministrationContext.LOCAL);
    context    = new IdentityAdministrationContext(this.provider, repository, catalog,  getEnvInternal());
    cacheObject(IdentityAdministrationContext.LOCAL, context, null);

    repository = new RepositoryReference(getRepositoryReference(), IdentityDevelopmentContext.LOCAL);
    catalog    = new CatalogReference(getCatalogReference(),       IdentityDevelopmentContext.LOCAL);
    context    = new IdentityDevelopmentContext(this.provider, repository, catalog,  getEnvInternal());
    cacheObject(IdentityDevelopmentContext.LOCAL, context, null);
  }
}