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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Result.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Result.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.connector.pcf.rest.schema.Role;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Embed;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Entity;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Account;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Metadata;

////////////////////////////////////////////////////////////////////////////////
// abstract class Result
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** The resource payload for a REST request.
 **
 ** @param  <T>                  the type of result entity contained within the
 **                              request.
 ** @param  <E>                  the type of payload entity contained within the
 **                              request.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Result<T extends Result, E extends Entity> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // Note that "metadata" is a complex attribute, so it is represented by the
  // Metadata class.
  private Metadata metadata;

  //////////////////////////////////////////////////////////////////////////////
  // Member clases
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Tenant
  // ~~~~~ ~~~~~~
  /**
   ** A <code>Tenant</code> (aka Org) is a development account that an
   ** individual or multiple collaborators can own and use.
   ** <br>
   ** All collaborators access a <code>Tenant</code> with user accounts, which
   ** have roles such as <code>OrgManager</code>, <code>OrgAuditor</code>, and
   ** <code>BillingManager</code>.
   ** <br>
   ** Collaborators in a <code>Tenant</code> share a resource quota plan, apps,
   ** services availability, and custom domains.
   ** <p>
   ** By default, a <code>Tenant</code> has the status of active.
   ** <br>
   ** An administrator can set the status of a <code>Tenant</code> to suspended
   ** for various reasons such as failure to provide payment or misuse. When a
   ** <code>Tenant</code> is suspended, users cannot perform certain activities
   ** within the <code>Tenant</code>, such as push apps, modify spaces, or bind
   ** services.
   */
  public static class Tenant extends Result<Tenant, Embed.Tenant> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // Note that "entity" is a complex attribute, so it is represented by the
    // embedded Tenant class.
    private Embed.Tenant entity;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Tenant</code> <code>Result</code> REST
     ** Resource that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Tenant() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name (Result)
    /**
     ** Sets the name of the <code>Tenant</code>.
     **
     ** @param  value              the name of the <code>Tenant</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>Tenant</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>Tenant</code>.
     */
    @Override
    public final Tenant name(final String value) {
      if (this.entity == null)
        this.entity = new Embed.Tenant();
      this.entity.name(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: entity (Result)
    /**
     ** Returns the resource's entity.
     **
     ** @return                  the the resource's entity.
     **                          <br>
     **                          Possible object is {@link Embed.Tenant}.
     */
    @Override
    public final Embed.Tenant entity() {
      return this.entity;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Space
  // ~~~~~ ~~~~~
  /**
   ** A <code>Space</code> provides users with access to a shared location for
   ** application development, deployment, and maintenance.
   ** <br>
   ** A <code>Tenant</code> can contain multiple spaces.
   ** <br>
   ** Every app, service, and route is scoped to a space. Roles provide access
   ** control for these resources and each <code>Space</code> role applies only to
   ** a particular <code>Space</code>.
   **
   */
  public static class Space extends Result<Space, Embed.Space> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // Note that "entity" is a complex attribute, so it is represented by the
    // embedded Space class.
    private Embed.Space entity;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Space</code> <code>Result</code> REST
     ** Resource that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Space() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name (Result)
    /**
     ** Sets the name of the <code>Space</code>.
     **
     ** @param  value              the name of the <code>Space</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>Space</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>Space</code>.
     */
    @Override
    public final Space name(final String value) {
      if (this.entity == null)
        this.entity = new Embed.Space();
      this.entity.name(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: entity (Result)
    /**
     ** Returns the resource's entity.
     **
     ** @return                  the the resource's entity.
     **                          <br>
     **                          Possible object is {@link Embed.Tenant}.
     */
    @Override
    public final Embed.Space entity() {
      return this.entity;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class User
  // ~~~~~ ~~~~
  /**
   ** A <code>User</code> provides users with access to a shared location for
   ** application development, deployment, and maintenance.
   ** <br>
   ** A <code>User</code> can contain multiple organizations and spaces.
   */
  public static class User extends Result<User, Account> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // Note that "entity" is a complex attribute, so it is represented by the
    // Account class.
    private Account entity;

    ////////////////////////////////////////////////////////////////////////////
    // Method of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name (Result)
    /**
     ** Sets the name of the <code>User</code>.
     **
     ** @param  value              the name of the <code>User</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>User</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>User</code>.
     */
    @Override
    public final User name(final String value) {
      if (this.entity == null)
        this.entity = new Account();
      this.entity.name(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: entity (Result)
    /**
     ** Returns the resource's entity.
     **
     ** @return                  the the resource's entity.
     **                          <br>
     **                          Possible object is {@link Account}.
     */
    @Override
    public final Account entity() {
      return this.entity;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Permission
  // ~~~~~ ~~~~~~~~~~
  /**
   ** A <code>Entitlement</code> provides users with access to a shared location
   ** for application development, deployment, and maintenance.
   ** <br>
   ** A <code>Permission</code> can contain multiple organizations and spaces.
   */
  public static class Permission extends Result<Permission, Role> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // Note that "entity" is a complex attribute, so it is represented by the
    // Role class.
    private Role entity;

    ////////////////////////////////////////////////////////////////////////////
    // Method of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name (Result)
    /**
     ** Sets the name of the <code>Permission</code>.
     **
     ** @param  value              the name of the <code>Permission</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>User</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>Permission</code>.
     */
    @Override
    public final Permission name(final String value) {
      if (this.entity == null)
        this.entity = new Role();
      this.entity.name(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: entity (Result)
    /**
     ** Returns the resource's entity.
     **
     ** @return                  the the resource's entity.
     **                          <br>
     **                          Possible object is {@link Permission}.
     */
    @Override
    public final Role entity() {
      return this.entity;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Result</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Result() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Autator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   guid
  /**
   ** Sets the guid of the REST object.
   **
   ** @param  value              the guid of the REST object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Result</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T guid(final String value) {
    if (this.metadata == null)
      this.metadata = new Metadata();

    this.metadata.guid(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   guid
  /**
   ** Returns the guid of the REST object.
   **
   ** @return                    the guid of the REST object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String guid() {
    return (this.metadata == null) ? null : this.metadata.guid();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the <code>Result</code>.
   **
   ** @param  value              the name of the <code>Result</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Result</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public abstract T name(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Result</code>.
   **
   ** @return                    the name of the <code>Result</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return (entity() == null) ? null : entity().name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata
  /**
   ** Returns the metadata for the object.
   **
   ** @return                    the <code>Metadata</code> about the entity.
   **                            <br>
   **                            Possible object is {@link Metadata}.
   */
  @JsonProperty(Metadata.PREFIX)
  public final Metadata metadata()  {
    return this.metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Returns the resource's entity.
   **
   ** @return                    the the resource's entity.
   **                            <br>
   **                            Possible object is <code>E</code>.
   */
  @JsonProperty(Entity.PREFIX)
  public abstract E entity();
}