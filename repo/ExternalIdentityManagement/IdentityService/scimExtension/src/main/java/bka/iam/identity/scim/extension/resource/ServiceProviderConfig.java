/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information"). You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2022. All Rights reserved.

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   ServiceProviderConfig.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class ServiceProviderConfig.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-13-01  SBernet      First release version
*/

package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// class ServiceProviderConfig
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Represents the SCIM Service Provider Configuration resource.
 ** <p>
 ** This resource provides information about the SCIM service provider’s
 ** capabilities, including supported features and extensions as defined in
 ** <a href="https://datatracker.ietf.org/doc/html/rfc7643#section-5">RFC 7643 Section 5</a>.
 ** </p>
 **
 ** <p>
 ** Key capabilities include:
 ** <ul>
 **   <li>PATCH support</li>
 **   <li>Bulk operations</li>
 **   <li>ETag support</li>
 **   <li>Filtering</li>
 **   <li>Sorting</li>
 **   <li>Password changes</li>
 **   <li>Authentication schemes</li>
 ** </ul>
 ** </p>
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceProviderConfig extends ScimResource {

  //////////////////////////////////////////////////////////////////////////////
  // Static Attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Patch operation support */
  static final String PATCH                  = "patch";

  /** Supported authentication schemes */
  static final String AUTHENTICATION_SCHEMES = "authenticationSchemes";

  /** Bulk operations support */
  static final String BULK                   = "bulk";

  /** ETag support for versioning */
  static final String ETAG                   = "etag";

  /** Filtering support */
  static final String FILTER                 = "filter";

  /** Sorting support */
  static final String SORT                   = "sort";

  /** Password change support */
  static final String CHANGE_PASSWORD        = "changePassword";

  /** Service provider documentation URL */
  static final String DOCUMENTATION_URL      = "documentationUrl";

  /** The URN for the SCIM Service Provider Configuration schema */
  public static final String[] SCHEMAS = new String[] {
      "urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig"
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new ServiceProviderConfig instance with the given resource
   ** descriptor.
   **
   ** @param descriptor     The {@link ResourceDescriptor} used for schema
   **                       validation and control.
   **                       Allowed object is {@link ResourceDescriptor}.
   */
  public ServiceProviderConfig(final ResourceDescriptor descriptor) {
    super(descriptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new ServiceProviderConfig instance with the provided
   ** resource descriptor and a list of attributes.
   **
   ** @param descriptor     The {@link ResourceDescriptor} used for schema
   **                       validation and control.
   **                       Allowed object is {@link ResourceDescriptor}.
   ** @param attributes     A list of attributes to initialize the
   **                       ServiceProviderConfig.
   **                       Allowed object is {@link List}.
   */
  public ServiceProviderConfig(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }


  /////////////////////////////////////////////////////////////////////////////
  // Abstract Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSchema
  /**
   ** Returns the schema URN for the ServiceProviderConfig resource.
   **
   ** @return             The schema URN as a String array.
   */
  @Override
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPatch
  /**
   ** Retrieves the PATCH capability attribute from the ServiceProviderConfig.
   **
   ** @return             The PATCH support attribute. Allowed object is 
   **                     {@link Attribute}.
   */
  public Attribute getPatch() {
    return get(PATCH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthenticationSchemes
  /**
   ** Retrieves the supported authentication schemes from the
   ** ServiceProviderConfig.
   **
   ** @return             The supported authentication schemes. Allowed object
   **                     is {@link Attribute}.
   */
  public Attribute getAuthenticationSchemes() {
    return get(AUTHENTICATION_SCHEMES);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBulk
  /**
   ** Retrieves the bulk operations capability attribute from the
   ** ServiceProviderConfig.
   **
   ** @return            The bulk support attribute. Allowed object is 
   **                    {@link Attribute}.
   */
  public Attribute getBulk() {
    return get(BULK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEtag
  /**
   ** Retrieves the ETag support capability attribute from the
   ** ServiceProviderConfig.
   **
   ** @return             The ETag support attribute. Allowed object is
   **                     {@link Attribute}.
   */
  public Attribute getEtag() {
    return get(ETAG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFilter
  /**
   ** Retrieves the filter capability attribute from the ServiceProviderConfig.
   **
   ** @return             The filter support attribute. Allowed object is
   **                     {@link Attribute}.
   */
  public Attribute getFilter() {
    return get(FILTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPatch
  /**
   ** Retrieves the sorting capability attribute from the ServiceProviderConfig.
   **
   ** @return             The sorting support attribute. Allowed object is
   **                     {@link Attribute}.
   */
  public Attribute getSort() {
    return get(SORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getChangePassword
  /**
   ** Retrieves the password change capability attribute from the ServiceProviderConfig.
   **
   ** @return             The password change support attribute. Allowed object
   **                     is {@link Attribute}.
   */
  public Attribute getChangePassword() {
    return get(CHANGE_PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDocumentationUrl
  /**
   ** Retrieves the documentation URL for the SCIM service provider.
   **
   ** @return             The documentation URL as a String.
   */
  public String getDocumentationUrl() {
    return getAttributeValue(DOCUMENTATION_URL);
  }

  @Override
  public Resource clone() {
    return new ServiceProviderConfig(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
