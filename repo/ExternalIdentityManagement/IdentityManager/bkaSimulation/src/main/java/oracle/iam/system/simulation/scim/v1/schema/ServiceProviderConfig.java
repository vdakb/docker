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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   ServiceProviderConfig.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceProviderConfig.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.v1.schema;

import java.util.List;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Schema;
import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

import oracle.iam.system.simulation.scim.schema.Entity;
import oracle.iam.system.simulation.scim.schema.BulkConfig;
import oracle.iam.system.simulation.scim.schema.ETagConfig;
import oracle.iam.system.simulation.scim.schema.SortConfig;
import oracle.iam.system.simulation.scim.schema.PatchConfig;
import oracle.iam.system.simulation.scim.schema.FilterConfig;
import oracle.iam.system.simulation.scim.schema.ChangePasswordConfig;
import oracle.iam.system.simulation.scim.schema.AuthenticationScheme;

////////////////////////////////////////////////////////////////////////////////
// class ServiceProviderConfig
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** SCIM provides a schema for representing the service provider's configuration
 ** identified using the following schema URI:
 ** <code>urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig</code>
 ** <p>
 ** The Service Provider configuration resource enables a Service Provider to
 ** discover SCIM specification features in a standardized form as well as
 ** provide additional implementation details to clients.
 ** <br>
 ** All attributes have a mutability of "<code>readOnly</code>". Unlike other
 ** core resources, the "<code>id</code>" attribute is not required for the
 ** Service Provider configuration resource.
 **/
@Schema(id="urn:scim:schemas:core:1.0", name="Service Provider Config", description="SCIM 1.0 Service Provider Config Resource")
public class ServiceProviderConfig extends Entity {

  @Attribute(description="An HTTP addressable URI pointing to the service provider's human consumable help documentation.", mutability=Definition.Mutability.READ_ONLY)
  private final String                     documentationURI;

  @Attribute(description="A complex type that specifies PATCH configuration options.", mutability=Definition.Mutability.READ_ONLY, required=true)
  private final PatchConfig                patch;

  @Attribute(description="A complex type that specifies Bulk configuration options.", mutability=Definition.Mutability.READ_ONLY, required=true)
  private final BulkConfig                 bulk;

  @Attribute(description="A complex type that specifies FILTER options.", mutability=Definition.Mutability.READ_ONLY, required=true)
  private final FilterConfig               filter;

  @Attribute(description="A complex type that specifies Change Password configuration options.", mutability=Definition.Mutability.READ_ONLY, required=true)
  private final ChangePasswordConfig       changePassword;

  @Attribute(description="A complex type that specifies Sort configuration options.", mutability=Definition.Mutability.READ_ONLY, required=true)
  private final SortConfig                 sort;

  @Attribute(description="A complex type that specifies Etag configuration options.", mutability=Definition.Mutability.READ_ONLY, required=true)
  private final ETagConfig                 etag;

  @Attribute(description="A complex type that specifies supported Authentication Scheme properties.", mutability=Definition.Mutability.READ_ONLY, required=true, multiValueClass=AuthenticationScheme.class)
  private final List<AuthenticationScheme> authentication;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>ServiceProviderConfig</code>.
   **
   ** @param documentationURI    an HTTP addressable URI pointing to the service
   **                            provider's human consumable help documentation.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  patch              a complex type that specifies PATCH
   **                            configuration options.
   **                            <br>
   **                            Allowed object is {@link PatchConfig}.
   ** @param  bulk               a complex type that specifies Bulk
   **                            configuration options.
   **                            <br>
   **                            Allowed object is {@link BulkConfig}.
   ** @param  filter             a complex type that specifies FILTER options.
   **                            <br>
   **                            Allowed object is {@link FilterConfig}.
   ** @param  changePassword     a complex type that specifies Change Password
   **                            configuration options.
   **                            <br>
   **                            Allowed object is {@link ChangePasswordConfig}.
   ** @param  sort               a complex type that specifies Sort
   **                            configuration options.
   **                            <br>
   **                            Allowed object is {@link SortConfig}.
   ** @param  etag               a complex type that specifies Etag
   **                            configuration options.
   **                            <br>
   **                            Allowed object is {@link ETagConfig}.
   ** @param authentication      a complex type that specifies supported
   **                            Authentication Scheme properties.
   **                            <br>
   **                            Allowed object is {@link AuthenticationScheme}.
   */
  @JsonCreator
  public ServiceProviderConfig(@JsonProperty(value="documentationUri") final String documentationURI, @JsonProperty(value="patch", required=true) final PatchConfig patch, @JsonProperty(value="bulk", required=true) final BulkConfig bulk, @JsonProperty(value="filter", required=true) final FilterConfig filter, @JsonProperty(value="changePassword", required=true) final ChangePasswordConfig changePassword, @JsonProperty(value="sort", required=true) final SortConfig sort, @JsonProperty(value="etag", required=true) final ETagConfig etag, @JsonProperty(value="authenticationSchemes", required=true) final List<AuthenticationScheme> authentication) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.documentationURI = documentationURI;
    this.patch            = patch;
    this.bulk             = bulk;
    this.filter           = filter;
    this.changePassword   = changePassword;
    this.sort             = sort;
    this.etag             = etag;
    this.authentication   = authentication == null ? null : Collections.unmodifiableList(authentication);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentationURI
  /**
   ** Returns the HTTP addressable URI pointing to the service provider's human
   ** consumable help documentation.
   **
   ** @return                    the HTTP addressable URI pointing to the
   **                            service provider's human consumable help documentation.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String documentationURI() {
    return this.documentationURI;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patch
  /**
   ** Returns the complex type that specifies PATCH configuration options.
   **
   ** @return                    the complex type that specifies PATCH
   **                            configuration options.
   **                            <br>
   **                            Possible object is {@link PatchConfig}.
   */
  public PatchConfig patch() {
    return this.patch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulk
  /**
   ** Returns the complex type that specifies BULK configuration options.
   **
   ** @return                    the complex type that specifies BULK
   **                            configuration options.
   **                            <br>
   **                            Possible object is {@link BulkConfig}.
   */
  public BulkConfig bulk() {
    return this.bulk;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the complex type that specifies FILTER configuration options.
   **
   ** @return                    the complex type that specifies FILTER
   **                            configuration options.
   **                            <br>
   **                            Possible object is {@link FilterConfig}.
   */
  public FilterConfig filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changePassword
  /**
   ** Returns the complex type that specifies Change Password configuration
   ** options.
   **
   ** @return                    the complex type that specifies Change Password
   **                            configuration options.
   **                            <br>
   **                            Possible object is
   **                            {@link ChangePasswordConfig}.
   */
  public ChangePasswordConfig changePassword() {
    return this.changePassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sort
  /**
   ** Returns the complex type that specifies SORT configuration options.
   **
   ** @return                    the complex type that specifies SORT
   **                            configuration options.
   **                            <br>
   **                            Possible object is {@link SortConfig}.
   */
  public SortConfig sort() {
    return this.sort;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   etag
  /**
   ** Returns the complex type that specifies Etag configuration options.
   **
   ** @return                    the complex type that specifies Etag
   **                            configuration options.
   **                            <br>
   **                            Possible object is {@link ETagConfig}.
   */
  public ETagConfig etag() {
    return this.etag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authentication
  /**
   ** Returns the complex type that specifies supported Authentication Scheme
   ** properties
   **
   ** @return                    the complex type that specifies supported
   **                            Authentication Scheme properties
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link AuthenticationScheme}.
   */
  public List<AuthenticationScheme> authentication() {
    return this.authentication;
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
   */
  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (this.documentationURI != null ? this.documentationURI.hashCode() : 0);
    result = 31 * result + (this.patch != null            ? this.patch.hashCode() : 0);
    result = 31 * result + (this.bulk != null             ? this.bulk.hashCode() : 0);
    result = 31 * result + (this.filter != null           ? this.filter.hashCode() : 0);
    result = 31 * result + (this.changePassword != null   ? this.changePassword.hashCode() : 0);
    result = 31 * result + (this.sort != null             ? this.sort.hashCode() : 0);
    result = 31 * result + (this.etag != null             ? this.etag.hashCode() : 0);
    result = 31 * result + (this.authentication != null   ? this.authentication.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>ServiceProviderConfig</code>s are considered equal if and only
   ** if they represent the same properties. As a consequence, two given
   ** <code>ServiceProviderConfig</code>s may be different even though they
   ** contain the same set of names with the same values, but in a different
   ** order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final ServiceProviderConfig that = (ServiceProviderConfig)other;
    if (this.documentationURI != null ? !this.documentationURI.equals(that.documentationURI) : that.documentationURI != null)
      return false;

    if (this.patch != null ? !this.patch.equals(that.patch) : that.patch != null)
      return false;

    if (this.bulk != null ? !this.bulk.equals(that.bulk) : that.bulk != null)
      return false;

    if (this.changePassword != null ? !this.changePassword.equals(that.changePassword) : that.changePassword != null)
      return false;

    if (this.filter != null ? !this.filter.equals(that.filter) : that.filter != null)
      return false;

    if (this.sort != null ? !this.sort.equals(that.sort) : that.sort != null)
      return false;

    if (this.etag != null ? !this.etag.equals(that.etag) : that.etag != null)
      return false;

    return (this.authentication == null ? authentication.equals(that.authentication) : that.authentication == null);
  }
}