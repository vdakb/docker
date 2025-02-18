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

    File        :   ServiceProviderVersion.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class ServiceProviderVersion.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-28-01  SBernet      First release version
*/

package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// class ServiceProviderVersion
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Represents the SCIM Service Provider Version resource.
 ** <p>
 ** This resource provides metadata about the implementation version, build
 ** date, and vendor information.
 ** </p>
 **
 ** <p>
 ** The schema is defined as an extension under:
 ** <ul>
 **   <li>urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:version</li>
 ** </ul>
 ** </p>
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceProviderVersion extends ScimResource {

  //////////////////////////////////////////////////////////////////////////////
  // Static Attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The implementation version of the service provider. */
  public static final String VERSION                  = "version";

   /** The vendor of the service provider implementation. */
  public static final String VENDOR                   = "vendor";

  /** The URN for the SCIM Service Provider Configuration schema */
  public static final String[] SCHEMAS = new String[] {
      "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:version"
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new ServiceProviderVersion instance with the given resource
   ** descriptor.
   **
   ** @param descriptor     The {@link ResourceDescriptor} used for schema
   **                       validation and control.
   **                       Allowed object is {@link ResourceDescriptor}.
   */
  public ServiceProviderVersion(final ResourceDescriptor descriptor) {
    super(descriptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new ServiceProviderVersion instance with the provided
   ** resource descriptor and a list of attributes.
   **
   ** @param descriptor     The {@link ResourceDescriptor} used for schema
   **                       validation and control.
   **                       Allowed object is {@link ResourceDescriptor}.
   ** @param attributes     A list of attributes to initialize the
   **                       ServiceProviderVersion.
   **                       Allowed object is {@link List}.
   */
  public ServiceProviderVersion(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }


  /////////////////////////////////////////////////////////////////////////////
  // Abstract Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSchemaURIs
  /**
   ** Returns the schema URN for the ServiceProviderVersion resource.
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
  // Method:   getVersion
  /**
   ** Retrieves the version attribute of the ServiceProviderVersion resource.
   **
   ** @return             The version attribute. Allowed object is 
   **                     {@link Attribute}.
   */
  public Attribute getVersion() {
    return get(VERSION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVendor
  /**
   ** Retrieves the vendor attribute of the ServiceProviderVersion resource.
   **
   ** @return            The vendor attribute. Allowed object is 
   **                    {@link Attribute}.
   */
  public Attribute getVendor() {
    return get(VENDOR);
  }

  @Override
  public Resource clone() {
    return new ServiceProviderVersion(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
