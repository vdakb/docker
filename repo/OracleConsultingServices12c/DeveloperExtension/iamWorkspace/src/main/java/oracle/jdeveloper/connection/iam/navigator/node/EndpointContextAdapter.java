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

    File        :   EndpointContextAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointContextAdapter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import java.util.Hashtable;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.adf.rc.EnvironmentConstants;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.jdeveloper.connection.iam.model.Endpoint;

////////////////////////////////////////////////////////////////////////////////
// class EndpointContextAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class EndpointContextAdapter implements EnvironmentConstants {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String ENDPOINT_CONNECTION_FACTORY_ENV = Endpoint.class.getName();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Hashtable<String, Object> env;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>EndpointContextAdapter</code>.
   ** Special requirements of this adapter are supplied using <code>env</code>.
   **
   ** @param  env                the {@link Hashtable} with special requirements
   **                            of this adapter.
   */
  private EndpointContextAdapter(final Hashtable<String, Object> env) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.env = env;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Returns the {@link Hashtable} environment this adapter covers.
   **
   ** @return                    the environment context this adapter covers.
   */
  public final Hashtable<String, Object> environment() {
    return this.env;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionId
  /**
   ** Sets the connection identifier associated with the Resource Palette.
   **
   ** @param  identifier         the connection identifier associated with the
   **                            Resource Palette.
   */
  public void connectionId(String identifier) {
    this.env.put(PROVIDER_ID_ENV, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionId
  /**
   ** Returns the connection identifier associated with the Resource Palette.
   **
   ** @return                    the connection identifier associated with the
   **                            Resource Palette.
   */
  public String connectionId() {
    return (String)this.env.get(PROVIDER_ID_ENV);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogReference
  /**
   ** Sets the {@link CatalogReference} connections associated with the
   ** Resource Palette.
   **
   ** @param  catalog            the {@link CatalogReference} connections
   **                            associated with the Resource Palette.
   */
  public void catalogReference(final CatalogReference catalog) {
    this.env.put(CATALOG_REFERENCE_ENV, catalog);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogReference
  /**
   ** Returns the {@link CatalogReference} connections associated with the
   ** Resource Palette.
   **
   ** @return                    the {@link CatalogReference} connections
   **                            associated with the Resource Palette.
   */
  public CatalogReference catalogReference() {
    return (CatalogReference)this.env.get(CATALOG_REFERENCE_ENV);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repositoryReference
  /**
   ** Sets the {@link RepositoryReference} connections associated with the
   ** Resource Palette.
   **
   ** @param  repository         the {@link RepositoryReference} connections
   **                            associated with the Resource Palette.
   */
  public void repositoryReference(final RepositoryReference repository) {
    this.env.put(REPOSITORY_REFERENCE_ENV, repository);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repositoryReference
  /**
   ** Returns the {@link RepositoryReference} connections associated with the
   ** Resource Palette.
   **
   ** @return                    the {@link RepositoryReference} connections
   **                            associated with the Resource Palette.
   */
  public RepositoryReference repositoryReference() {
    return (RepositoryReference)this.env.get(REPOSITORY_REFERENCE_ENV);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionContext
  /**
   ** Sets the {@link AdfJndiContext} connections associated with the
   ** Resource Palette.
   **
   ** @param  context            the {@link AdfJndiContext} connections
   **                            associated with the Resource Palette.
   */
  public void connectionContext(final AdfJndiContext context) {
    this.env.put(CONNECTION_CONTEXT_ENV, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionContext
  /**
   ** Returns the {@link AdfJndiContext} connections associated with the
   ** Resource Palette.
   **
   ** @return                    the {@link AdfJndiContext} connections
   **                            associated with the Resource Palette.
   */
  public AdfJndiContext connectionContext() {
    return (AdfJndiContext)this.env.get(CONNECTION_CONTEXT_ENV);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionFactory
  /**
   ** Sets the {@link AdfJndiContext} connections associated with the
   ** Resource Palette.
   **
   ** @param  factory            the {@link EndpointConnectionFactory}
   **                            connections associated with the Resource
   **                            Palette.
   */
  public void connectionFactory(final EndpointConnectionFactory factory) {
    this.env.put(ENDPOINT_CONNECTION_FACTORY_ENV, factory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionFactory
  /**
   ** Returns the {@link EndpointConnectionFactory} connections associated with
   ** the Resource Palette.
   **
   ** @return                    the {@link EndpointConnectionFactory}
   **                            connections associated with the Resource
   **                            Palette.
   */
  public EndpointConnectionFactory connectionFactory() {
    return (EndpointConnectionFactory)this.env.get(ENDPOINT_CONNECTION_FACTORY_ENV);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** The <code>EndpointContextAdapter</code> is a singleton class.
   ** This method gets this manager's single instance.
   **
   ** @return                    the <code>EndpointContextAdapter</code>
   **                            instance.
   */
  public static EndpointContextAdapter instance() {
    return instance(new Hashtable<String, Object>());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** The <code>EndpointContextAdapter</code> is a singleton class.
   ** This method gets this manager's single instance.
   **
   ** @param  env                the {@link Hashtable} with special requirements
   **                            of the adapter.
   **
   ** @return                    the <code>EndpointContextAdapter</code>
   **                            instance.
   */
  public static EndpointContextAdapter instance(final Hashtable<String, Object> env) {
    return new EndpointContextAdapter(env);
  }
}
