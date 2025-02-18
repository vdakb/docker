package oracle.jdeveloper.connection.oim.adapter;

import java.util.Hashtable;

import javax.naming.NamingException;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.jdeveloper.connection.oim.model.IdentityServer;

@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class IdentityDevelopmentContext extends IdentityContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String LOCAL = "Development";

  IdentityDevelopmentContext(final IdentityServer provider, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    super(LOCAL, provider, repository, catalog, env);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by finctionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadCache (InMemoryContext)
  @Override
  protected void loadCache()
    throws NamingException {
  }
}