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

    File        :   FileSystemNodeFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FileSystemNodeFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

import java.io.File;

import javax.naming.Context;
import javax.naming.Reference;
import javax.naming.NamingException;

import oracle.adf.rc.EnvironmentConstants;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.adf.rc.connection.ConnectionManager;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.jdeveloper.connection.iam.model.FileSystem;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class FileSystemNodeFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties stored in the <code>FileSystem</code> model.
 ** <p>
 ** This data might also been used to display a specific node in the Resource
 ** Catalog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.104
 ** @since   12.2.1.3.42.60.104
 */
public class FileSystemNodeFactory extends EndpointContextFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FileSystemNodeFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FileSystemNodeFactory() {
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
    return new FileSystemNode(new File(((FileSystem)connection).path()), repository, catalog, environment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns the {@link FileSystem} associated with the Resource Palette that
   ** matches the specified class name and name.
   ** <p>
   ** The class itself cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   **
   ** @param  context            the context to been searched.
   ** @param  name               the name of {@link FileSystem} to match.
   **
   ** @return                    the {@link FileSystem} associated with the
   **                            Resource Palette that matches the specified
   **                            class and name.
   */
  public FileSystem lookup(final AdfJndiContext context, final String name) {
    // prevent bogus input
    if ((context == null) || StringUtility.empty(name)) {
      return null;
    }
    try {
      return context.getReferenceMap().containsKey(name) ? (FileSystem)context.lookup(name) : null;
    }
    catch (NamingException e) {
      // intentionally left blank
      e.printStackTrace();
      return null;
    }
    catch (Throwable t) {
      t.printStackTrace();
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns a {@link List} of {@link FileSystem}s from the specified
   ** {@link AdfJndiContext} that match the specified class name.
   ** <p>
   ** The class itself cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   ** 
   **
   ** @param  context            the {@link AdfJndiContext} providing the
   **                            configured.
   ** @param  clazz              the {@link Class} of a {@link FileSystem}
   **                            subclass to match.
   **
   ** @return                    the {@link List} of {@link FileSystem}s
   **                            matching the requirements.
   */
  public List<FileSystem> lookup(final AdfJndiContext context, final Class<?> clazz) {
    final List<FileSystem> endpoint = new ArrayList<FileSystem>(0);
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
            endpoint.add((FileSystem)entry);
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