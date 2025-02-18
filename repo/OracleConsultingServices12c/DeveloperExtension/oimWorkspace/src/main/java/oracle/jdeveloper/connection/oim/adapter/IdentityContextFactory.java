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

    File        :   IdentityContextFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityContextFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.adapter;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Reference;
import javax.naming.NamingException;

import oracle.adf.rc.util.CatalogUtils;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.adf.rc.connection.ConnectionManager;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;
import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

import oracle.jdeveloper.connection.oim.model.IdentityServer;

import oracle.jdeveloper.connection.iam.adapter.EndpointContextFactory;

////////////////////////////////////////////////////////////////////////////////
// class IdentityContextFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties stored in the <code>IdentityServer</code> model.
 ** <p>
 ** This data might also been used to display a specific node in the Resource
 ** Catalog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityContextFactory extends EndpointContextFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String TAG = IdentityContextFactory.class.getName();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>IdentityContextFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityContextFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInitialContext (InitialContextFactory)
  /**
   ** Creates an Initial Context for beginning name resolution.
   ** <br>
   ** Special requirements of this context are supplied using
   ** <code>environment</code>.
   ** <p>
   ** The environment parameter is owned by the caller.
   ** <br>
   ** The implementation will not modify the object or keep a reference to it,
   ** although it may keep a reference to a clone or copy.
   **
   ** @param environment         the possibly <code>null</code> environment
   **                            specifying information to be used in the
   **                            creation of the initial context.
   **
   ** @return                    a non-<code>null</code> initial context object
   **                            that implements the {@link Context} interface.
   **
   ** @throws NamingException    if cannot create an initial context.
   */
  @Override
  public Context getInitialContext(final Hashtable environment)
    throws NamingException {

    final ConnectionManager manager = (ConnectionManager)environment.get("oracle.adf.rc.connectionManager");
    CatalogUtils.checkNotNull(TAG, "getInitialContext()", "oracle.adf.rc.connectionManager", manager);
    final RepositoryReference repository = (RepositoryReference)environment.get("oracle.adf.rc.repositoryReference");
    CatalogUtils.checkNotNull(TAG, "getInitialContext()", "oracle.adf.rc.repositoryReference", repository);
    final IdentityServer provider = lookup(manager.getConnections(), repository.getRepositoryId());
    CatalogUtils.checkNotNull(TAG, "getInitialContext()", "OIG connection", provider);
    final CatalogReference catalog  = (CatalogReference)environment.get("oracle.adf.rc.catalogReference");
    return new IdentityContext(provider, repository, catalog, environment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns the {@link IdentityServer} associated with the Resource
   ** Palette that matches the specified class name and name.
   ** <p>
   ** The class itself cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   **
   ** @param  context            the context to been searched.
   ** @param  name               the name of {@link IdentityServer} to match.
   **
   ** @return                    the {@link IdentityServer} associated with
   **                            the Resource Palette that matches the specified
   **                            class and name.
   */
  public IdentityServer lookup(final AdfJndiContext context, final String name) {
    if ((context == null) || StringUtility.empty(name)) {
      return null;
    }
    final List<IdentityServer> descriptor = lookup(context, IdentityServer.class);
    if (CollectionUtility.empty(descriptor)) {
      return null;
    }

    for (IdentityServer cursor : descriptor) {
      if (name.equals(cursor.name())) {
        return cursor;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns a {@link List} of {@link IdentityServer}s from the specified
   ** {@link AdfJndiContext} that match the specified class name.
   ** <p>
   ** The class itself cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   ** 
   **
   ** @param  context            the {@link AdfJndiContext} providing the
   **                            configured.
   ** @param  clazz              the {@link Class} of an {@link IdentityServer}
   **                            subclass to match.
   **
   ** @return                    the {@link List} of {@link IdentityServer}s
   **                            matching the requirements.
   */
  public List<IdentityServer> lookup(final AdfJndiContext context, final Class<?> clazz) {
    final List<IdentityServer> endpoint = new ArrayList<IdentityServer>(0);
    // prevent bogus input
    if (context == null) {
      return endpoint;
    }

    try {
      final Map<String, Reference> reference = context.getReferenceMap();
      if (reference != null) {
        for (Map.Entry<String, Reference> cursor : reference.entrySet()) {
          if (cursor.getValue().getClassName().equals(clazz.getName())) {
            final Object entry = context.lookup(cursor.getKey());
            endpoint.add((IdentityServer)entry);
          }
        }
      }
    }
    catch (NamingException e) {
      // intentionally left blank
      ;
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
    return endpoint;
  }
}