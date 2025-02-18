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

    File        :   Payload.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Payload.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class Payload
// ~~~~~ ~~~~~~~
/**
 ** The base REST request payload entity.
 ** <br>
 ** This object contains all of the attributes required of REST objects.
 ** <p>
 ** <code>Payload</code> is used when the domain is known ahead of time. In that
 ** case a developer can derive a class from <code>Payload</code> and annotate
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
public class Payload<T extends Payload> implements Resource<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The unique name of a tenant or space a request belongs to.
   */
  public static final String NAME           = "name";

  /** The organiztion guid of the space belongs to. */
  public static final String TENANT         = "organization_guid";

  /** Whether or not billing is enabled for a tenant. */
  public static final String BILLING        = "billing_enabled";

  /** Whether or not Space Developers can enable ssh on apps in the space. */
  public static final String SECURE_SHELL   = "allow_ssh";

  /** The list of the associated domains */
  public static final String DOMAIN         = "domain_guids";

  /** The list of the associated security groups of a space */
  public static final String SECURITY       = "security_group_guids";

  /** The Status of the tenant */
  public static final String STATUS         = "status";

  /** The canonical value for status active of the tenant */
  public static final String STATUS_ACTIVE  = "active";

  /** The canonical value for status suspended of the tenant */
  public static final String STATUS_SUSPEND = "suspended";

  /** The guid of quota to associate with a tenant */
  public static final String QUOTA_TENANT   = "quota_definition_guid";

  /** The guid of the associated space quota definition */
  public static final String QUOTA_SPACE    = "space_quota_definition_guid";

  /** The list of the associated managers of a tenant or space */
  public static final String MANAGER        = "manager_guids";

  /** The list of the associated auditors of a tenant or space */
  public static final String AUDITOR        = "auditor_guids";

  /** The list of the associated developers of a space */
  public static final String DEVELOPER      = "developer_guids";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the space */
  @JsonProperty(NAME)
  private String       name;

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
   ** <code>BillinkManager</code>.
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
  public static class Tenant extends Payload<Tenant> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the status of the tenant either <code>active</code> or
     ** <code>supended</code>
     */
    @JsonProperty(STATUS)
    private String  status;

    /** whether or not billing is enabled for this tenant */
    @JsonProperty(BILLING)
    private Boolean billing;

    /** the guid of quota to associate with this tenant */
    @JsonProperty(QUOTA_TENANT)
    private String  quota;

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
     **       <code>equals(Object)</code> method, then callink the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then callink the <code>hashCode</code> method on each of
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
      result = 31 * result + (this.status  != null ? this.status.hashCode()  : 0);
      result = 31 * result + (this.billing != null ? this.billing.hashCode() : 0);
      result = 31 * result + (this.quota   != null ? this.quota.hashCode()   : 0);
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
   **
   */
  public static class Space extends Payload<Space> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the name of the space */
    @JsonProperty(TENANT)
    private String      tenant;

    /** whether or not Space Developers can enable ssh on apps in the space. */
    @JsonProperty(SECURE_SHELL)
    private Boolean      secureShell;

    /** the guid of the associated space quota definition */
    @JsonProperty(QUOTA_SPACE)
    private String       quota;

    /** the list of the associated domains */
    @JsonProperty(DOMAIN)
    private List<String> domain;

    /** the list of the associated security groups */
    @JsonProperty(SECURITY)
    private List<String> securityGroup;

    /** the list of the associated managers */
    @JsonProperty(MANAGER)
    private List<String> manager;

    /** the list of the associated auditors */
    @JsonProperty(AUDITOR)
    private List<String> auditor;

    /** the list of the associated developers */
    @JsonProperty(DEVELOPER)
    private List<String> developer;

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
    // Method: domain
    /**
     ** Sets the list of the associated domains.
     **
     ** @param  value            the list of the associated domains.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Space</code>.
     */
    public final Space domain(final List<String> value) {
      this.domain = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: domain
    /**
     ** Returns the list of the associated domains.
     **
     ** @return                  the list of the associated domains.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public final List<String> domain() {
      return this.domain;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: securityGroup
    /**
     ** Sets the list of the associated security groups.
     **
     ** @param  value            the list of the associated security groups.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Space</code>.
     */
    public final Space securityGroup(final List<String> value) {
      this.securityGroup = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: securityGroup
    /**
     ** Returns the list of the associated security groups.
     **
     ** @return                  the list of the associated security groups.
     **                          <br>
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public final List<String> securityGroup() {
      return this.securityGroup;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: manager
    /**
     ** Sets the list of the associated managers.
     **
     ** @param  value            the list of the associated managers.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Space</code>.
     */
    public final Space manager(final List<String> value) {
      this.manager = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: manager
    /**
     ** Returns the list of the associated managers.
     **
     ** @return                  the list of the associated managers.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public final List<String> manager() {
      return this.manager;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: auditor
    /**
     ** Sets the list of the associated auditors.
     **
     ** @param  value            the list of the associated auditors.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Space</code>.
     */
    public final Space auditor(final List<String> value) {
      this.auditor = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: auditor
    /**
     ** Returns the list of the associated auditors.
     **
     ** @return                  the list of the associated auditors.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public final List<String> auditor() {
      return this.auditor;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: developer
    /**
     ** Sets the list of the associated developers.
     **
     ** @param  value            the list of the associated developers.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     **
     ** @return                  the <code>Space</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Space</code>.
     */
    public final Space developer(final List<String> value) {
      this.developer = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: developer
    /**
     ** Returns the list of the associated developers.
     **
     ** @return                  the list of the associated developers.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public final List<String> developer() {
      return this.developer;
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
     **       <code>equals(Object)</code> method, then callink the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then callink the <code>hashCode</code> method on each of
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
      result = 31 * result + (this.secureShell   != null ? this.secureShell.hashCode()   : 0);
      result = 31 * result + (this.tenant        != null ? this.tenant.hashCode()        : 0);
      result = 31 * result + (this.quota         != null ? this.quota.hashCode()         : 0);
      result = 31 * result + (this.securityGroup != null ? this.securityGroup.hashCode() : 0);
      result = 31 * result + (this.domain        != null ? this.domain.hashCode()        : 0);
      result = 31 * result + (this.manager       != null ? this.manager.hashCode()       : 0);
      result = 31 * result + (this.auditor       != null ? this.auditor.hashCode()       : 0);
      result = 31 * result + (this.developer     != null ? this.developer.hashCode()     : 0);
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Payload</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Payload() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the tenant.
   **
   ** @param  value              the name of the tenant.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Payload</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T name(final String value) {
    this.name = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the tenant.
   **
   ** @return                    the name of the tenant.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
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
   **       <code>equals(Object)</code> method, then callink the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then callink the <code>hashCode</code> method on each of the
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
    result = 31 * result + (this.name                     != null ? this.name.hashCode()                     : 0);
    return result;
  }
}