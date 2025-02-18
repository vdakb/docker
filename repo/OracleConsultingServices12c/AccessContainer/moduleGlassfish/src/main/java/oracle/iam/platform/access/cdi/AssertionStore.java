/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Authentication

    File        :   AssertionStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.platform.access.cdi;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;

import java.io.Serializable;

import java.net.URL;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.enterprise.inject.Typed;

import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import javax.enterprise.context.spi.CreationalContext;

import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.AuthenticationException;

import javax.security.enterprise.credential.Credential;

import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.CredentialValidationResult;

import com.nimbusds.jose.JWSAlgorithm;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;

import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.RestrictedResourceRetriever;

import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;

import oracle.iam.platform.access.jmx.AssertionConfiguration;

////////////////////////////////////////////////////////////////////////////////
// class AssertionStore
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A literal representation of the {@link AssertionStoreDefinition}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Typed(AssertionStore.class)
public class AssertionStore implements Serializable
                            ,          IdentityStore {
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7506028953662641804")
  private static final long serialVersionUID = 600339919837178668L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private AssertionConfiguration                    config     = null;

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private DataSource                                dataSource = null;

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private ConfigurableJWTProcessor<SecurityContext> processor  = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an {@link AssertionStore} obtaining the configuration from the
   ** specified {@link AssertionStoreDefinition}.
   **
   ** @param  config             the {@link AssertionMXBean} providing the
   **                            configuration properties.
   **                            <br>
   **                            Allowed object is {@link AssertionMXBean}.
   */
  public AssertionStore() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interafces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (IdentityStore)
  @Override
  public CredentialValidationResult validate(final Credential credential) {
    if (!(credential instanceof AssertionCredential))
      return CredentialValidationResult.NOT_VALIDATED_RESULT;

    final AssertionCredential assertion = (AssertionCredential)credential;
    try {
      final CallerPrincipal principal = authenticate(assertion);
      return new CredentialValidationResult(config().getRealm(), principal, principal.getName(), "", authorize(assertion));
    }
    catch (AuthenticationException e) {
      e.printStackTrace(System.err);
      return CredentialValidationResult.INVALID_RESULT;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticate
  /**
   ** Given a user-provided token credential., return an optional principal.
   ** <p>
   ** If the credentials are valid and map to a principal, returns an
   ** {@link Optional#of(Object)}.
   **
   ** @param  credential         a user-provided token credential.
   **                            <br>
   **                            Allowed object is {@link AssertionCredential}.
   **
   ** @return                    an authenticated principal.
   **                            <br>
   **                            Possible object is {@link CallerPrincipal}.
   **
   ** @throws AuthenticationException if the credentials cannot be authenticated
   **                                 due to an underlying error.
   */
  public final CallerPrincipal authenticate(final AssertionCredential credential)
    throws AuthenticationException {

    // optional context parameter, not required here
    final SecurityContext ctx = null;
    try {
      // process the token
      final JWTClaimsSet claim = processor().process(credential.token, ctx);
      return authenticate(claim.getSubject()) ? new CallerPrincipal(claim.getSubject()) : null;
    }
    catch (Exception e) {
      throw new AuthenticationException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorize
  /**
   ** Populates the permissions assigned to the given principal username.
   **
   ** @param  credential         a user-provided token credential.
   **                            <br>
   **                            Allowed object is {@link AssertionCredential}.
   **
   ** @return                    the collected autorization names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> authorize(final AssertionCredential credential)
    throws AuthenticationException {

    ResultSet         resultSet  = null;
    Connection        connection = null;
    PreparedStatement statement  = null;
    final Set<String> collector  = new HashSet<String>();
    collector.add("authenticated-user");
    try {
      connection = aquire();
      statement  = connection.prepareStatement(config().getPermissionQuery());
      statement.setString(1, credential.token);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        // populate the permissions fetched from the database as appropriate
        collector.add(resultSet.getString(1));
      }
      return collector;
    }
    catch (SQLException e) {
      throw new AuthenticationException(e);
    }
    finally {
      if (statement != null)
        close(statement);
      if (connection != null)
        release(connection);
    }
  }

  private ConfigurableJWTProcessor<SecurityContext> processor ()
    throws AuthenticationException {

    if (this.processor != null)
      return processor;

    // create a JWT processor for the access tokens
    this.processor = new DefaultJWTProcessor<>();
    // set the required JWT claims for access tokens issued by the Access
    // Manager server, may differ with other servers
    this.processor.setJWTClaimsSetVerifier(
      new DefaultJWTClaimsVerifier<SecurityContext>(
        new JWTClaimsSet.Builder().issuer(config().getIssuer()).build(),
        new HashSet<>(Arrays.asList("iss", "sub", "iat", "jti"))
      )
    );
    // configure the JWT processor with a key selector to feed matching public
    // RSA keys sourced from the JWK set URL with the expected JWS algorithm
    // of the access tokens
    this.processor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, serverSource()));
    return this.processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSource
  private JWKSource<SecurityContext> serverSource()
    throws AuthenticationException {

    try {
      return new RemoteJWKSet<SecurityContext>(new URL(config().getLocation()), retriever());
    }
    catch (Exception e) {
      throw new AuthenticationException(e);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   retriever
  /**
   ** The public RSA keys to validate the signatures will be sourced from the
   ** OAuth 2.0 server's JWK set, published at a well-known URL.
   ** <br>
   ** A {@link RemoteJWKSet} caches the retrieved keys to speed up subsequent
   ** look-ups and can also handle key-rollover.
   **
   ** @return                    the retriever to lookup the public RSA keys
   **                            to validate the signatures.
   **                            <br>
   **                            Possible object is
   **                            {@link RestrictedResourceRetriever}.
   */
  private RestrictedResourceRetriever retriever() {
    // the public RSA keys to validate the signatures will be sourced from the
    // OAuth 2.0 server's JWK set, published at a well-known URL
    // the RemoteJWKSet object caches the retrieved keys to speed up subsequent
    // look-ups and can also handle key-rollover
    final Map<String, List<String>> vendor = new HashMap<>();
    vendor.put("x-oauth-identity-domain-name", Arrays.asList(config().getTenant()));
    final RestrictedResourceRetriever retriever = new DefaultResourceRetriever();
    retriever.setHeaders(vendor);
    return retriever;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticate
  /**
   ** Populates the permissions assigned to the given principal username.
   **
   ** @param  username           the login name of the user principal the
   **                            permissions are populated for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collected autorization names.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  private boolean authenticate(final String username)
    throws AuthenticationException {

    ResultSet          resultSet  = null;
    Connection         connection = null;
    PreparedStatement  statement  = null;
    try {
      connection = aquire();
      statement  = connection.prepareStatement(config().getPrincipalQuery());
      statement.setString(1, username);
      resultSet = statement.executeQuery();
      if (!resultSet.next())
        throw new AuthenticationException();
    }
    catch (SQLException e) {
      throw new AuthenticationException(e);
    }
    finally {
      if (statement != null)
        close(statement);
      if (connection != null)
        release(connection);
    }
    return true;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   config
  /**
   ** Retrieve the configuration singleton.
   **
   ** @return                    the configuration singleton.
   **                            <br>
   **                            Possible object is
   **                            {@link AssertionConfiguration}.
   **
   ** @throws AuthenticationException if the configuration could not be aquired.
   */
  @SuppressWarnings("unchecked") 
  private AssertionConfiguration config() {
    if (this.config == null) {
      final BeanManager          mgr  = CDI.current().getBeanManager();
      final Bean<AssertionConfiguration>              bean = (Bean<AssertionConfiguration>)mgr.getBeans(AssertionConfiguration.class).iterator().next();
      final CreationalContext<AssertionConfiguration> ctx  = mgr.createCreationalContext(bean);
      this.config = (AssertionConfiguration)mgr.getReference(bean, AssertionConfiguration.class, ctx);
    }
    return this.config;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Aquire a connection from the JDBC DataSoure.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @return                    the JDBC connection aquired.
   **                            <br>
   **                            Possible object is {@link Connection}.
   **
   ** @throws AuthenticationException if the JDBC {@link Connection} could not
   **                                 be aquired.
   */
  private Connection aquire()
    throws AuthenticationException {

    try {
      return lookup().getConnection();
    }
    catch (SQLException e) {
      throw new AuthenticationException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release the given JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param  connection         the JDBC connection to close.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   */
  private void release(final Connection connection) {
    if (connection != null) {
      try {
        // make sure that we will commit our unit of work if neccessary
        connection.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close a previous prepared statement
   ** <br>
   ** If the statement has a dependence result set, the result set will also be
   ** closed.
   **
   ** @param  statement          the {@link PreparedStatement} to close.
   **                            <br>
   **                            Allowed object is {@link PreparedStatement}.
   */
  private void close(final PreparedStatement statement) {
    // prevent bogus input
    if (statement == null)
      return;

    try {
      close(statement.getResultSet());
    }
    catch (SQLException e) {
      // handle silenlty
      e.printStackTrace(System.err);
    }
    finally {
      try {
        statement.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close a JDBC resultset.
   **
   ** @param  resultSet          the {@link ResultSet} to close.
   **                            <br>
   **                            Allowed object is {@link ResultSet}.
   */
  private void close(final ResultSet resultSet) {
    // prevent bogus input
    if (resultSet != null)
      try {
        resultSet.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Initialize the JDBC DataSource.
   **
   ** @return                    the {@link DataSource} looked-up from the
   **                            JNDI tree.
   **                            <br>
   **                            Allowed object is {@link DataSource}.
   **
   ** @throws AuthenticationException if the JDBC {@link DataSource} could not
   **                                 be retrieved.
   */
  private DataSource lookup()
    throws AuthenticationException {

    if (this.dataSource == null) {
      try {
        InitialContext context = null;
        try {
          context = new InitialContext();
        }
        catch (NamingException e) {
          throw new AuthenticationException(e);
        }
        this.dataSource = (DataSource)context.lookup(config().getDataSource());
      }
      catch (NamingException e) {
        throw new AuthenticationException(e);
      }
    }
    return this.dataSource;
  }
}