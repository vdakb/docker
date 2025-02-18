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

    File        :   MetadataContextFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataContextFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;

import oracle.adf.rc.EnvironmentConstants;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.adf.rc.connection.ConnectionManager;

import oracle.jdeveloper.connection.iam.model.MetadataServer;

import oracle.jdeveloper.connection.iam.service.MetadataService;

////////////////////////////////////////////////////////////////////////////////
// class MetadataContextFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties stored in the <code>MetadataServer</code> model.
 ** <p>
 ** This data might also been used to display a specific node in the Resource
 ** Catalog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class MetadataContextFactory extends EndpointContextFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MetadataContextFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MetadataContextFactory() {
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

    final ConnectionManager   manager    = (ConnectionManager)environment.get(EnvironmentConstants.CONNECTION_MANAGER_ENV);
    final CatalogReference    catalog    = (CatalogReference)environment.get(EnvironmentConstants.CATALOG_REFERENCE_ENV);
    final RepositoryReference repository = (RepositoryReference)environment.get(EnvironmentConstants.REPOSITORY_REFERENCE_ENV);
    final Object              connection = manager.lookup(repository.getRepositoryId());
    final MetadataService     provider   = new MetadataService((MetadataServer)connection);
    return new MetadataPackage(provider, repository, catalog, environment);
  }
}