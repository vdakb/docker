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

    File        :   OIMSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class OIMSchema.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-13-01  SBernet     First release version
*/
package bka.iam.identity.scim.extension.rest;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.SchemaDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.resource.Application;
import bka.iam.identity.scim.extension.resource.ApplicationAttribute;
import bka.iam.identity.scim.extension.resource.Entitlement;
import bka.iam.identity.scim.extension.resource.Group;
import bka.iam.identity.scim.extension.resource.Policy;
import bka.iam.identity.scim.extension.resource.ResourceType;
import bka.iam.identity.scim.extension.resource.Schema;
import bka.iam.identity.scim.extension.resource.ServiceProviderConfig;
import bka.iam.identity.scim.extension.resource.ServiceProviderVersion;
import bka.iam.identity.scim.extension.resource.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import oracle.hst.foundation.logging.Loggable;
////////////////////////////////////////////////////////////////////////////////
// class OIMSchema
// ~~~~~ ~~~~~~~~
/**
 ** Represents the SCIM schema management component for Oracle Identity Manager.
 ** <p>
 ** This class provides mechanisms to initialize and manage SCIM schemas
 ** for various resource types, ensuring compliance with SCIM standards.
 ** </p>
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OIMSchema {
  
  //////////////////////////////////////////////////////////////////////////////
  // Static Attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /** Service Provider Version Resource type **/
  public final static String SERVICE_PROVIDER_VERSION = "ServiceProviderVersion";
  /** User Resource type **/
  public final static String USER                     = "User";
  /** Group Resource type **/
  public final static String GROUP                    = "Group";
  /** Application Resource type **/
  public final static String APPLICATION              = "Application";
  /** Application Attribute Resource type **/
  public final static String APPLICATION_ATTRIBUTE    = "ApplicationAttribute";
  /** Entitlement Resource type **/
  public final static String ENTITLEMENT              = "Entitlement";
  /** Policy Resource type **/
  public final static String POLICY                   = "Policy";
  
  /** Singleton instance **/
  private static OIMSchema  instance;
  /** Initialization state */
  private static volatile boolean initialized = false;
  /** Supported SCIM resource types **/
  private static final Set<Class<? extends ScimResource>> resourceType = new HashSet<>(Arrays.asList(
      ServiceProviderConfig.class,
      ServiceProviderVersion.class,
      Schema.class,
      PatchRequest.class,
      User.class,
      Group.class,
      ResourceType.class,
      ApplicationAttribute.class,
      Application.class,
      Entitlement.class,
      Policy.class
  ));
  /** Local schema file paths **/
  public static final String[] localSchemaPath = {
      "/resources/scim-schema-schema.json",
      "/resources/scim-version-schema.json",
      "/resources/scim-user-application-schema.json",
      "/resources/scim-user-core-schema.json",
      "/resources/scim-application-schema.json",
      "/resources/scim-application-attributes-schema.json",
      "/resources/scim-entitlement-schema.json",
      "/resources/scim-policy-schema.json"
  };
  
  /** Local resource type file paths **/
  public static final String[] localResourceTypePath = {
      //Resource type: SCIM Version
      "/resources/scim-version-resource.json",
      //Resource type: Application
      "/resources/scim-application-resource.json",
      //Resource type: Entitlement
      "/resources/scim-entitlement-resource.json",
      //Resource type: Application Attribute
      "/resources/scim-application-attributes-resource.json",
      //Resource type: Policy
      "/resources/scim-policy-resource.json",
  };
  
  //////////////////////////////////////////////////////////////////////////////
  // Instance Attributes
  //////////////////////////////////////////////////////////////////////////////
  /** Logger instance **/
  private Loggable           loggable;
  /** HTTP request instance **/
  private HttpServletRequest httpRequest;
  /** Map to store resource descriptors **/
  private final Map<Class<? extends ScimResource>, ResourceDescriptor> mapResourceDescriptor = new HashMap<>();
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Private constructor OIMSchema to enforce singleton pattern
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private OIMSchema() {
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInstance
  /**
   ** Returns the singleton instance of OIMSchema.
   **
   ** @return            The singleton instance of {@link OIMSchema}.
   */
  public static OIMSchema getInstance() {
     if (instance == null) {
       synchronized (OIMSchema.class) {
         if (instance == null) {
           instance = new OIMSchema();
         }
       }
    }
    return instance;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initializes the SCIM schema management system with local and external
   ** descriptors.
   **
   ** @param loggable       The logger instance for logging messages.
   **                       Allowed object is {@link Loggable}.
   ** @param httpRequest    The HTTP request instance.
   **                       Allowed object is {@link HttpServletRequest}.
   **
   ** @throws ScimException    If a SCIM-related error occurs.
   */
  public void initialize(final Loggable loggable, final HttpServletRequest httpRequest)
    throws ScimException {
    if (initialized) {
      return; // Skip if already initialized
    }
    synchronized (OIMSchema.class) {
      if (initialized) {
        return; // Double-check within synchronized block
      }
      this.loggable    = loggable;
      this.httpRequest = httpRequest;
      this.loggable.info("Initializing SCIM Extension service");
      
      final Map<String,SchemaDescriptor> localSchemaDescriptors = loadLocalDescriptor();
      for (Map.Entry<String, SchemaDescriptor> entry : localSchemaDescriptors.entrySet()) {
        this.loggable.info("URI loaded: " + entry.getKey());
      }
      
      for (Class<? extends ScimResource> scimResource : this.resourceType) {
        try {
          final ScimResource instance = scimResource.getConstructor(ResourceDescriptor.class)
                                                    .newInstance(new ResourceDescriptor());
          
          this.mapResourceDescriptor.put(scimResource, getResourceDescriptor(instance.getSchemaURIs(), localSchemaDescriptors));
        }
        catch (Exception e) {
          this.loggable.info("Failed to initialize resource: " + scimResource.getName() + " - " + e.getMessage());
        }
      }
      initialized = true; // Mark as initialized
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceDescriptorByResourceType
  /**
   ** Retrieves the resource descriptor for the specified SCIM resource type.
   **
   ** @param scimResource   The class of the SCIM resource.
   **                       Allowed object is {@link Class}.
   **
   ** @return               The {@link ResourceDescriptor} for the specified
   **                       resource type.
   */
  public ResourceDescriptor getResourceDescriptorByResourceType(Class<? extends ScimResource> scimResource) {
    final String method = "getResourceDescriptorByResourceType";
    this.loggable.trace(method, "ENTRY");
    if (!mapResourceDescriptor.containsKey(scimResource))
      this.loggable.error(method, String.format("No schema found for %1$s", scimResource));
    this.loggable.trace(method, "EXIT");
    return mapResourceDescriptor.get(scimResource);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllSchemaURIs
  /**
   ** Retrieves all schema URIs known from the system.
   **
   ** @return            A set of schema URIs as {@link Set}.
   */
  public Set<String> getAllSchemaURIs() {   
    final Set<String> schemaURIs = new HashSet<>();
    for (ResourceDescriptor descriptor : mapResourceDescriptor.values()) {
      for (SchemaDescriptor schemaDescriptor : descriptor) {
        schemaURIs.add(schemaDescriptor.getURI());
      }
    }
    return schemaURIs;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Private Methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadLocalDescriptor
  /**
   ** Loads schema descriptors from local JSON files.
   **
   ** @return            A map of schema URIs to {@link SchemaDescriptor}
   **                    instances.
   */
  private Map<String, SchemaDescriptor> loadLocalDescriptor() {
    final String method = "getResourceDescriptor";
    this.loggable.trace(method, "ENTRY");
    
    final Map<String,SchemaDescriptor> localSchemaDescriptors = new HashMap<String,SchemaDescriptor>();
    this.loggable.info("Loading local descriptors");
    for (String schemaFilePath : localSchemaPath) {
      this.loggable.info(String.format("Loading %1$s", schemaFilePath));
      final File           is = new File(httpRequest.getServletContext().getRealPath(schemaFilePath));
      try {
        final JsonNode         jsonNode         = new ObjectMapper().readTree(new FileInputStream(is));
        final SchemaDescriptor schemadescriptor = Unmarshaller.jsonNodetoSchema(jsonNode);
        
        localSchemaDescriptors.put(schemadescriptor.getURI(), schemadescriptor);
      }
      catch (IOException e) {
        this.loggable.error(method, String.format("Loading %1$s failed: %2$s", schemaFilePath, e.getMessage()));
      }
    }
    this.loggable.trace(method, "EXIT");
    return localSchemaDescriptors;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceDescriptor
  /**
   ** Retrieves the resource descriptor for the given schema URIs. If the
   ** descriptor is not found in the local store then, it loads the schema on
   ** OIM server.
   **
   ** @param schemaURIs             The URIs of the schemas to retrieve.
   **                               Allowed object is {@link String[]}.
   ** @param localSchemaDescriptor  A map of local schema descriptors.
   **                               Allowed object is {@link Map}.
   **
   ** @return                       The {@link ResourceDescriptor} containing
   **                               the schemas.
   **
   ** @throws ScimException         If a SCIM-related error occurs.
   */
  private ResourceDescriptor getResourceDescriptor(final String[] schemaURIs, final Map<String, SchemaDescriptor> localSchemaDescriptor) throws ScimException {
    final String method = "getResourceDescriptor";
    this.loggable.trace(method, "ENTRY");
    ResourceDescriptor resourceDescriptor = new ResourceDescriptor();

    for (String uri : schemaURIs) {
      try {
        if (!localSchemaDescriptor.containsKey(uri)) {
          this.loggable.info(String.format("URI: %1$s not found in local. Trying to load externaly...", uri));
          final SchemaDescriptor schema = OIMScimContext.build(this.loggable, httpRequest).lookupSchema(uri);
          resourceDescriptor.addSchema(schema);
          this.loggable.info(String.format("URI: %1$s found and loaded from OIM server.", uri));
        }
        else {
          resourceDescriptor.addSchema(localSchemaDescriptor.get(uri));
        }
      } catch (ScimException e) {
        this.loggable.error(method, String.format("Cannot find schema: %1$s in OIM server", uri));
      }
    }

    loggable.trace(method, "EXIT");
    return resourceDescriptor;
  }
}
