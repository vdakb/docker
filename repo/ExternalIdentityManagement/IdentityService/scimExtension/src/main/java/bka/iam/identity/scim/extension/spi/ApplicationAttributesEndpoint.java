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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   ApplicationEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    ApplicationEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-25  JLakic     First release version
*/
package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.model.SingularComplexAttribute;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.resource.ApplicationAttribute;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scimold.extension.utils.WLSUtil;
import bka.iam.identity.zero.api.AppSchemaFacade;
import bka.iam.identity.zero.model.SchemaAttribute;

import java.net.URI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import oracle.hst.foundation.SystemMessage;

/**
 * SCIM endpoint for managing application schemas.
 */
@Path("/ApplicationAttributes")
public class ApplicationAttributesEndpoint extends AbstractEndpoint{

    @EJB(name = "AppSchemaFacade")
    AppSchemaFacade appSchemaFacade;
    
    /**
     * Retrieves a application attributes based on the provided application name.
     *
     * @param application   The application name of the application to retrieve.
     * @param emit         Set of attributes to include in the response.
     * @param omit         Set of attributes to exclude from the response.
     * @param  sortOrder   the sorting order (ascending or descending).
     * @param  startIndex  the index of the first result to return.
     * @param  count        the number of results to return.
     * @param uriInfo       UriInfo for URI-related information.
     * @param httpRequest   HttpServletRequest for HTTP-related information.
     * @param sc            SecurityContext for security-related information.
     * @param request       Request for handling HTTP request.
     * @return Response containing the application.
     * @throws Exception If an error occurs during the operation.
     */
    @GET
    @Path("/{application}")
    @Consumes({ "application/scim+json" })
    @Produces({ "application/scim+json" })
    public Response getApplicationSchemas(final @PathParam("application") String application,
                                   final @QueryParam("attributes") Set<String> emit,
                                   final @QueryParam("excludedAttributes") Set<String> omit,
                                   @QueryParam("startIndex") Integer startIndex,
                                   @QueryParam("count") Integer count,
                                   @Context UriInfo uriInfo, @Context HttpServletRequest httpRequest,
                                   @Context SecurityContext sc, @Context Request request) throws Exception, Exception {
      final String method = "getApplicationSchemas";
      entering(method);
      final List<ApplicationAttribute> resources = new LinkedList<ApplicationAttribute>();
      
      List<SchemaAttribute> schemaAttributes = appSchemaFacade.lookup(application);
      
      if (schemaAttributes.size() == 0) {
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.APPLICATION_NOTFOUND, application));
      }
      
      List<SchemaAttribute> subList = null;

      if (count == null && startIndex == null) {
        subList = schemaAttributes;
        for (SchemaAttribute schemaAttribute : subList) {
          resources.add(getApplicationAttribute(schemaAttribute));
        }
        final ListResponse<ApplicationAttribute> attributes = new ListResponse<ApplicationAttribute>(resources, Long.valueOf(schemaAttributes.size()), startIndex, count);
        exiting(method);
        return ResponseBuilder.response(200, attributes, omit, emit, null, null);
      }
      
      // Prevent bogus state
      if (startIndex > schemaAttributes.size())
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.string(ScimMessage.INVALID_STARTINDEX));
      
      if (count + startIndex >= schemaAttributes.size()) {
        count = schemaAttributes.size() - startIndex;
        subList = schemaAttributes.subList(startIndex, schemaAttributes.size());
      } else {
        subList = schemaAttributes.subList(startIndex, count + startIndex);
      }
      
      for (SchemaAttribute schemaAttribute : subList) {
          resources.add(getApplicationAttribute(schemaAttribute));
      }

      final ListResponse<ApplicationAttribute> attributes = new ListResponse<ApplicationAttribute>(resources, Long.valueOf(schemaAttributes.size()), startIndex, count);

      exiting(method);
      return ResponseBuilder.response(200, attributes, omit, emit, null, null);
    }
    
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplicationSchemasAttribute
  /**
   ** Retrieves the attribute of an application based on the provided
   ** application name.
   **
   ** @param  application        the name of the application to retrieve.
   ** @param  attribute          the attribute of an application to retrieve.
   ** @param  emit               the {@link Set} of attributes to include in the
   **                            response.
   ** @param  omit               the {@link Set} of attributes to exclude from
   **                            the response.
   ** @param  uriInfo            the {@link UriInfo} for URI-related
   **                            information.
   ** @param  httpRequest        the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param  sc                 the {@link SecurityContext} for
   **                            security-related information.
   ** @param  request            the {@link Request} for handling HTTP request.
   **
   ** @return                    the {@link Response} containing the application
   **                            entitlements per namespace.
   **
   ** @throws ScimException      if an error occurs during the operation.
   */
   @GET
   @Path("/{application}/{attribute}")
  public Response getApplicationSchemasAttribute(final @PathParam("application") String application, final @PathParam("attribute") String attribute, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException {

    final String method = "getApplicationSchemas";
    entering(method);
    ApplicationAttribute resourcesAttribute = null;
      
    List<SchemaAttribute> schemaAttributes = appSchemaFacade.lookup(application); 
    
    if (schemaAttributes.isEmpty()) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.USER_NOTFOUND, application));          
    }
    
    for (SchemaAttribute schemaAttribute : schemaAttributes) {
        if (schemaAttribute.name().equals(attribute))
          resourcesAttribute = getApplicationAttribute(schemaAttribute);
    }
    
    if (schemaAttributes.isEmpty()) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.USER_NOTFOUND, attribute));          
    }
        
    exiting(method);
    return ResponseBuilder.response(200, resourcesAttribute, omit, emit);
  }
    
    private ApplicationAttribute getApplicationAttribute(final SchemaAttribute schemaAttribute) {
      final String method = "getApplicationAttribute";
      trace(method, SystemMessage.METHOD_ENTRY);
      
      final List<Attribute> attribute = new ArrayList<>();
      final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(ApplicationAttribute.class);
      attribute.add(ScimResource.createID(schemaAttribute.name()));
      attribute.add(ScimResource.createSchema(ApplicationAttribute.SCHEMAS));
      attribute.add(ScimResource.createMeta(null, null, URI.create(WLSUtil.getServerURL() + OIMScimContext.OIM_EXTENTION_ENDPOINT_SCIM + "/" + OIMScimContext.ENDPOINT_APPLICATION_ATTRIBUTES + "/" + schemaAttribute.name()), OIMSchema.APPLICATION_ATTRIBUTE));
      if (schemaAttribute.label() != null)
        attribute.add(new SingularSimpleAttribute("label", new AttributeValue(schemaAttribute.label())));
      if (schemaAttribute.required() != null)
        attribute.add(new SingularSimpleAttribute("required", new AttributeValue(schemaAttribute.required())));
      if (schemaAttribute.entitlement() != null)
        attribute.add(new SingularSimpleAttribute("entitlement", new AttributeValue(schemaAttribute.entitlement())));
      if (schemaAttribute.type() != null)
        attribute.add(new SingularSimpleAttribute("type", new AttributeValue(schemaAttribute.type())));
      if (schemaAttribute.variantType() != null)
        attribute.add(new SingularSimpleAttribute("variantType", new AttributeValue(schemaAttribute.variantType())));
      if (schemaAttribute.length() != null)
        attribute.add(new SingularSimpleAttribute("length", new AttributeValue(schemaAttribute.length())));
      if (schemaAttribute.lookupName() != null)
        attribute.add(new SingularSimpleAttribute("lookupName", new AttributeValue(schemaAttribute.lookupName())));
      final List<Attribute> lookupAttribute = new ArrayList<>();
      final List<Map<String, String>> lookupValues = schemaAttribute.lookupValues();
      List<AttributeValue> lookupAttributeValue = new LinkedList<AttributeValue>();
      if (lookupValues != null) {
        
        for (Map<String, String> lookupValue : lookupValues) {
          final List<Attribute> attributes = new LinkedList<>();
          for (Map.Entry<String, String> pair : lookupValue.entrySet()) {
            if (pair != null) {
              final String key = pair.getKey();
              final String value = pair.getValue();
              if (key != null && value != null) {
                attributes.add(new SingularSimpleAttribute(key, new AttributeValue(value)));
              }
            }
          }
          lookupAttributeValue.add(new AttributeValue(attributes.toArray(new Attribute[0])));
        }
      }
      if (!lookupAttributeValue.isEmpty())
        attribute.add(new MultiValueComplexAttribute("lookupValues", lookupAttributeValue.toArray(new AttributeValue[0])));
      
      final List<Attribute> propertyAttributeValue = new LinkedList<Attribute>();
      if (schemaAttribute.properties() != null) {
        for (Map.Entry<String, Object> pair : schemaAttribute.properties().entrySet()) {
          final String key = pair.getKey();
          final Object value = pair.getValue();
          if (key != null && value != null)
            propertyAttributeValue.add(new SingularSimpleAttribute(pair.getKey(), new AttributeValue(pair.getValue())));
        }
        
        attribute.add(new SingularComplexAttribute("properties", new AttributeValue(propertyAttributeValue.toArray(new Attribute[0]))));
      }
      
      if (schemaAttribute.attrRef() != null) {
        List<AttributeValue> refAttributeValue = new LinkedList<AttributeValue>();
        
        for (SchemaAttribute referenceAttribute : schemaAttribute.attrRef()) {
          final List<Attribute> applicationAttributes = new ArrayList<Attribute>();
          Iterator<Attribute> iterator = getApplicationAttribute(referenceAttribute).iterator();
          while (iterator.hasNext()) {
            applicationAttributes.add(iterator.next());
          }
          refAttributeValue.add(new AttributeValue(applicationAttributes.toArray(new Attribute[0])));
        }
        attribute.add(new MultiValueComplexAttribute("attributeReference", refAttributeValue.toArray(new AttributeValue[0])));
      }

      trace(method, SystemMessage.METHOD_EXIT);
      
      return new ApplicationAttribute(resourceDescriptor, attribute);
    }
    
    
    
}
