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

    File        :   Embed.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Embed.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

////////////////////////////////////////////////////////////////////////////////
// class Embed
// ~~~~~ ~~~~~
/**
 ** The base REST result embed entity.
 ** <br>
 ** This object contains all of the attributes required of PCF REST objects.
 ** <p>
 ** <code>Embed</code> is used when the domain is known ahead of time. In that
 ** case a developer can derive a class from <code>Embed</code> and annotate
 ** the class. The class should be a Java bean. This will make it easier to work
 ** with the REST object since you will just have plain old getters and setters
 ** for core attributes.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Embed<T extends Embed> implements Entity<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonIgnore
  /** The status of the resource */
  public static final String STATUS = "status";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(ID)
  private String name;

  @JsonProperty("domains_url")
  private String endpointDomain;

  @JsonProperty("app_events_url")
  private String endpointApplicationEvent;

  @JsonProperty("managers_url")
  private String endpointManager;

  @JsonProperty("auditors_url")
  private String endpointAuditor;

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
  @JsonPropertyOrder({"name", "status", "billing_enabled", "quota_definition_guid"})
  public static class Tenant<T extends Tenant> extends Embed<Tenant> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty(STATUS)
    private String  status;

    @JsonProperty("billing_enabled")
    private Boolean billing;

    @JsonProperty("quota_definition_guid")
    private String  quota;

    @JsonProperty("default_isolation_segment_guid")
    private String  isolation;

    @JsonProperty("quota_definition_url")
    private String  endpointQuota;

    @JsonProperty("spaces_url")
    private String  endpointSpace;

    @JsonProperty("space_quota_definitions_url")
    private String  endpointSpaceQuota;

    @JsonProperty("private_domains_url")
    private String  endpointDomainPrivate;

    @JsonProperty("users_url")
    private String  endpointUser;

    @JsonProperty("billing_managers_url")
    private String  endpointBillingManager;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Tenant</code> <code>Embed</code> REST
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
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: status
    /**
     ** Sets status of the <code>Tenant</code>.
     ** <br>
     ** Status may be either <code>active</code> or <code>suspended</code>.
     **
     ** @param  value            the status of the <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant status(final String value) {
    this.status = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: status
    /**
     ** Returns status of the <code>Tenant</code>.
     ** <br>
     ** Status may be either <code>active</code> or <code>suspended</code>.
     **
     ** @return                  the status of the <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String status() {
      return this.status;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: billing
    /**
     ** Sets the value indicating the <code>Tenant</code> billing status.
     **
     ** @param  value            the value indicating the <code>Tenant</code>
     **                          billing status.
     **                          <br>
     **                          Allowed object is {@link Boolean}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant billing(final Boolean value) {
      this.billing = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: billing
    /**
     ** Returns the value indicating the <code>Tenant</code> billing status.
     **
     ** @return                  the value indicating the <code>Tenant</code>
     **                          billing status.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     */
    public final Boolean billing() {
      return this.billing;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: quota
    /**
     ** Sets the quota resource guid of the <code>Tenant</code>.
     **
     ** @param  value            the quota resource guid of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant quota(final String value) {
      this.quota = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: quota
    /**
     ** Returns the quota resource guid of the <code>Tenant</code>.
     **
     ** @return                  the quota resource guid of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String quota() {
      return this.quota;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isolation
    /**
     ** Sets the isolation segment guid of the <code>Tenant</code>.
     **
     ** @param  value            the isolation segment guid of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant isolation(final String value) {
      this.isolation = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isolation
    /**
     ** Returns the isolation segment guid of the <code>Tenant</code>.
     **
     ** @return                  the isolation segment guid of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String isolation() {
      return this.isolation;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointQuota
    /**
     ** Sets the quota endpoint URI of the <code>Tenant</code>.
     **
     ** @param  value            the quota endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant endpointQuota(final String value) {
      this.endpointQuota = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointQuota
    /**
     ** Returns the quota endpoint URI of the <code>Tenant</code>.
     **
     ** @return                  the quota endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointQuota() {
      return this.endpointQuota;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointSpace
    /**
     ** Sets the space endpoint URI of the <code>Tenant</code>.
     **
     ** @param  value            the space endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant endpointSpace(final String value) {
      this.endpointSpace = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointSpace
    /**
     ** Returns the space endpoint URI of the <code>Tenant</code>.
     **
     ** @return                  the space endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointSpace() {
      return this.endpointSpace;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointSpaceQuota
    /**
     ** Sets the space quota endpoint URI of the <code>Tenant</code>.
     **
     ** @param  value            the space quota endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant endpointSpaceQuota(final String value) {
      this.endpointSpaceQuota = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointSpaceQuota
    /**
     ** Returns the space quota endpoint URI of the <code>Tenant</code>.
     **
     ** @return                  the space quota endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointSpaceQuota() {
      return this.endpointSpaceQuota;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointDomainPrivate
    /**
     ** Sets the private domain endpoint URI of the <code>Tenant</code>.
     **
     ** @param  value            the private domain endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant endpointDomainPrivate(final String value) {
      this.endpointDomainPrivate = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointDomainPrivate
    /**
     ** Returns the private domain endpoint URI of the <code>Tenant</code>.
     **
     ** @return                  the private domain endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointDomainPrivate() {
      return this.endpointDomainPrivate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointUser
    /**
     ** Sets the user endpoint URI of the <code>Tenant</code>.
     **
     ** @param  value            the user endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant endpointUser(final String value) {
      this.endpointUser = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointUser
    /**
     ** Returns the user endpoint URI of the <code>Tenant</code>.
     **
     ** @return                  the user endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointUser() {
      return this.endpointUser;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointBillingManager
    /**
     ** Sets the billing manager endpoint URI of the <code>Tenant</code>.
     **
     ** @param  value            the billing manager endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Tenant</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Tenant</code>.
     */
    public final Tenant endpointBillingManager(final String value) {
      this.endpointBillingManager = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointBillingManager
    /**
     ** Returns the billing manager endpoint URI of the <code>Tenant</code>.
     **
     ** @return                  the billing manager endpoint URI of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointBillingManager() {
      return this.endpointBillingManager;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                    a hash code value for this object.
     **                            <br>
     **                            Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + (this.status                 != null ? this.status.hashCode()                 : 0);
      result = 31 * result + (this.billing                != null ? this.billing.hashCode()                : 0);
      result = 31 * result + (this.quota                  != null ? this.quota.hashCode()                  : 0);
      result = 31 * result + (this.endpointQuota          != null ? this.endpointQuota.hashCode()          : 0);
      result = 31 * result + (this.endpointSpace          != null ? this.endpointSpace.hashCode()          : 0);
      result = 31 * result + (this.endpointSpaceQuota     != null ? this.endpointSpaceQuota.hashCode()     : 0);
      result = 31 * result + (this.endpointDomainPrivate  != null ? this.endpointDomainPrivate.hashCode()  : 0);
      result = 31 * result + (this.endpointUser           != null ? this.endpointUser.hashCode()           : 0);
      result = 31 * result + (this.endpointBillingManager != null ? this.endpointBillingManager.hashCode() : 0);
      return result;
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
   */
  public static class Space<T extends Space> extends Embed<Space> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty("allow_ssh")
    private Boolean secureShell;

    @JsonProperty("organization_guid")
    private String  tenant;

    @JsonProperty("space_quota_definition_guid")
    private String  quota;

    @JsonProperty("organization_url")
    private String  endpointTenant;

    @JsonProperty("routes_url")
    private String  endpointRoute;

    @JsonProperty("apps_url")
    private String  endpointApplication;

    @JsonProperty("service_instances_url")
    private String  endpointService;

    @JsonProperty("events_url")
    private String  endpointEvent;

    @JsonProperty("security_groups_url")
    private String  endpointSecurityGroup;

    @JsonProperty("staging_security_groups_url")
    private String  endpointSecurityGroupStaging;

    @JsonProperty("developers_url")
    private String  endpointDeveloper;

    @JsonProperty("isolation_segment_guid")
    private String  isolationSegment;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Space</code> <code>Embed</code> REST resource
     ** that allows use as a JavaBean.
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
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: secureShell
    /**
     ** Sets the value indicating the <code>Space</code> allowing access by a
     ** secure shell.
     **
     ** @param  value            the value indicating the <code>Space</code>
     **                          allowing access by a secure shell.
     **                          <br>
     **                          Allowed object is {@link Boolean}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Space</code>.
     */
    public final Space secureShell(final Boolean value) {
      this.secureShell = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: secureShell
    /**
     ** Returns the value indicating the <code>Space</code> allowing access by a
     ** secure shell.
     **
     ** @return                  the value indicating the <code>Space</code>
     **                          allowing access by a secure shell.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     */
    public final Boolean secureShell() {
      return this.secureShell;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tenant
    /**
     ** Sets the tenant resource guid the <code>Space</code> belongs to.
     **
     ** @param  value            the tenant resource guid the <code>Space</code>
     **                          belongs to.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Space</code>.
     */
    public final Space tenant(final String value) {
      this.tenant = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tenant
    /**
     ** Returns the tenant resource guid the <code>Space</code> belongs
     ** to.
     **
     ** @return                  the tenant resource guid the <code>Space</code>
     **                          belongs to.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String tenant() {
      return this.tenant;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: quota
    /**
     ** Sets the quota resource guid of the <code>Space</code>.
     **
     ** @param  value            the quota resource guid of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Space</code>.
     */
    public final Space quota(final String value) {
      this.quota = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: quota
    /**
     ** Returns the quota resource guid of the <code>Tenant</code>.
     **
     ** @return                  the quota resource guid of the
     **                          <code>Tenant</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String quota() {
      return this.quota;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointTenant
    /**
     ** Sets the tenant endpoint URI of the <code>Space</code>.
     **
     ** @param  value            the tenant endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space endpointTenant(final String value) {
      this.endpointTenant = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointTenant
    /**
     ** Returns the tenant endpoint URI of the <code>Space</code>.
     **
     ** @return                  the tenant endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointTenant() {
      return this.endpointTenant;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointRoute
    /**
     ** Sets the route endpoint URI of the <code>Space</code>.
     **
     ** @param  value            the route endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space endpointRoute(final String value) {
      this.endpointRoute = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointRoute
    /**
     ** Returns the route endpoint URI of the <code>Space</code>.
     **
     ** @return                  the route endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointRoute() {
      return this.endpointRoute;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointApplication
    /**
     ** Sets the application endpoint URI of the <code>Space</code>.
     **
     ** @param  value            the application endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space endpointApplication(final String value) {
      this.endpointApplication = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointApplication
    /**
     ** Returns the application endpoint URI of the <code>Space</code>.
     **
     ** @return                  the application endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointApplication() {
      return this.endpointApplication;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointService
    /**
     ** Sets the service endpoint URI of the <code>Space</code>.
     **
     ** @param  value            the service endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space endpointService(final String value) {
      this.endpointService = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointService
    /**
     ** Returns the service endpoint URI of the <code>Space</code>.
     **
     ** @return                  the service endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointService() {
      return this.endpointService;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointEvent
    /**
     ** Sets the event endpoint URI of the <code>Space</code>.
     **
     ** @param  value            the event endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space endpointEvent(final String value) {
      this.endpointEvent = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointEvent
    /**
     ** Returns the event endpoint URI of the <code>Space</code>.
     **
     ** @return                  the event endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointEvent() {
      return this.endpointEvent;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointSecurityGroup
    /**
     ** Sets the security group endpoint URI of the <code>Space</code>.
     **
     ** @param  value            the security group endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space endpointSecurityGroup(final String value) {
      this.endpointSecurityGroup = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointSecurityGroup
    /**
     ** Returns the security group endpoint URI of the <code>Space</code>.
     **
     ** @return                  the security group endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointSecurityGroup() {
      return this.endpointSecurityGroup;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointSecurityGroupStaging
    /**
     ** Sets the staging security group endpoint URI of the <code>Space</code>.
     **
     ** @param  value            the staging security group endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space endpointSecurityGroupStaging(final String value) {
      this.endpointSecurityGroupStaging = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointSecurityGroupStaging
    /**
     ** Returns the staging security group endpoint URI of the
     ** <code>Space</code>.
     **
     ** @return                  the staging security group endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointSecurityGroupStaging() {
      return this.endpointSecurityGroupStaging;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointDeveloper
    /**
     ** Sets the developer endpoint URI of the <code>Space</code>.
     **
     ** @param  value            the developer endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space endpointDeveloper(final String value) {
      this.endpointDeveloper = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endpointDeveloper
    /**
     ** Returns the developer endpoint URI of the <code>Space</code>.
     **
     ** @return                  the developer endpoint URI of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String endpointDeveloper() {
      return this.endpointDeveloper;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isolationSegment
    /**
     ** Sets the isolation segment GUID of the <code>Space</code>.
     **
     ** @param  value            the isolation segment GUID of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    public final Space isolationSegment(final String value) {
      this.isolationSegment = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isolationSegment
    /**
     ** Returns the isolation segment GUID of the <code>Space</code>.
     **
     ** @return                  the isolation segment GUID of the
     **                          <code>Space</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String isolationSegment() {
      return this.isolationSegment;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                    a hash code value for this object.
     **                            <br>
     **                            Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + (this.secureShell                  != null ? this.secureShell.hashCode()                  : 0);
      result = 31 * result + (this.tenant                       != null ? this.tenant.hashCode()                       : 0);
      result = 31 * result + (this.quota                        != null ? this.quota.hashCode()                        : 0);
      result = 31 * result + (this.endpointTenant               != null ? this.endpointTenant.hashCode()               : 0);
      result = 31 * result + (this.endpointRoute                != null ? this.endpointRoute.hashCode()                : 0);
      result = 31 * result + (this.endpointApplication          != null ? this.endpointApplication.hashCode()          : 0);
      result = 31 * result + (this.endpointService              != null ? this.endpointService.hashCode()              : 0);
      result = 31 * result + (this.endpointEvent                != null ? this.endpointEvent.hashCode()                : 0);
      result = 31 * result + (this.endpointSecurityGroup        != null ? this.endpointSecurityGroup.hashCode()        : 0);
      result = 31 * result + (this.endpointSecurityGroupStaging != null ? this.endpointSecurityGroupStaging.hashCode() : 0);
      result = 31 * result + (this.endpointDeveloper            != null ? this.endpointDeveloper.hashCode()            : 0);
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Embed</code> REST Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Embed() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointDomain
  /**
   ** Sets the domain endpoint URI of the <code>Embed</code>.
   **
   ** @param  value              the domain endpoint URI of the
   **                            <code>Embed</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Embed</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T endpointDomain(final String value) {
    this.endpointDomain = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointDomain
  /**
   ** Returns the domain endpoint URI of the <code>Embed</code>.
   **
   ** @return                    the domain endpoint URI of the
   **                            <code>Embed</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointDomain() {
    return this.endpointDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointApplicationEvent
  /**
   ** Sets the application event endpoint URI of the <code>Embed</code>.
   **
   ** @param  value              the application event endpoint URI of the
   **                            <code>Embed</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Embed</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T endpointApplicationEvent(final String value) {
    this.endpointApplicationEvent = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  endpointApplicationEvent
  /**
   ** Returns the application event endpoint URI of the <code>Embed</code>.
   **
   ** @return                    the application event endpoint URI of the
   **                            <code>Embed</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointApplicationEvent() {
    return this.endpointApplicationEvent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointManager
  /**
   ** Sets the manager endpoint URI of the <code>Embed</code>.
   **
   ** @param  value              the manager endpoint URI of the
   **                            <code>Embed</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Embed</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T endpointManager(final String value) {
    this.endpointManager = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointManager
  /**
   ** Returns the manager endpoint URI of the <code>Embed</code>.
   **
   ** @return                    the manager endpoint URI of the
   **                            <code>Embed</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointManager() {
    return this.endpointManager;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointAuditor
  /**
   ** Sets the auditor endpoint URI of the <code>Embed</code>.
   **
   ** @param  value              the auditor endpoint URI of the
   **                            <code>Embed</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Embed</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T endpointAuditor(final String value) {
    this.endpointAuditor = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointAuditor
  /**
   ** Returns the auditor endpoint URI of the <code>Embed</code>.
   **
   ** @return                    the auditor endpoint URI of the
   **                            <code>Embed</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String endpointAuditor() {
    return this.endpointAuditor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (Entity)
  /**
   ** Sets the name of the <code>Embed</code>.
   **
   ** @param  value              the name of the <code>Embed</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Embed</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T name(final String value) {
    this.name = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (Entity)
  /**
   ** Returns the name of the <code>Embed</code>.
   **
   ** @return                    the name of the <code>Embed</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
    result = 31 * result + (this.endpointDomain           != null ? this.endpointDomain.hashCode()           : 0);
    result = 31 * result + (this.endpointApplicationEvent != null ? this.endpointApplicationEvent.hashCode() : 0);
    result = 31 * result + (this.endpointManager          != null ? this.endpointManager.hashCode()          : 0);
    result = 31 * result + (this.endpointAuditor          != null ? this.endpointAuditor.hashCode()          : 0);
    return result;
  }
}