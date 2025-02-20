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

    File        :   UserEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    UserEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-25-05  JLakic     First release version
*/

package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.Operation;
import bka.iam.identity.scim.extension.model.Operation.OperationType;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.SearchRequest;
import bka.iam.identity.scim.extension.parser.Marshaller;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.parser.UserParser;
import bka.iam.identity.scim.extension.resource.User;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scim.extension.rest.PATCH;
import bka.iam.identity.scim.extension.utils.PatchUtil;
import bka.iam.identity.scimold.extension.utils.ScimConstants;
import bka.iam.identity.zero.api.AccountsFacade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import oracle.hst.platform.core.logging.Logger;
////////////////////////////////////////////////////////////////////////////////
// class UserEndpoint
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A JAX-RS resource implementation of /Users endpoint.
 ** <br>
 ** Methods here are intercepted and/or decorated.
 ** <p>
 ** <a name="emit"></a>
 ** <br>
 ** <b>Notes:</b>
 ** <br>
 ** The parameters <i>emit</i> and <i>omit</i> found in methods of this
 ** implementation are aimed at specifying the "attributes" and
 ** "excludedAttributes" query params regarded in section 3.9 of SCIM spec
 ** protocol document (RFC 7644).
 ** <p>
 ** Every SCIM service operation that returns resources (e.g Users, Groups,
 ** etc.) offers the possibility to specify which attributes can be included for
 ** every resource part of the response. The default behavior is returning those
 ** attributes that according to the resource Schema have returnability =
 ** "always" in addition to those with returnability = "default".
 ** <p>
 ** <i><b>emit</b></i> is used to override the default attribute set, so
 ** when supplying a not-null or not empty String, the attributes included in
 ** the resource(s) of the response will be those with returnability = "always"
 ** as well as those specified by <i>emit</i>.
 ** <p>
 ** This parameter consists of  a comma-separated list of attribute names. An
 ** example of a valid value for <i>emit</i> when the resource of interest
 ** is User, could be: <code>userName, active, name.familyName, addresses,
 ** emails.value, emails.type, urn:ietf:params:scim:schemas:extension:hcm:2.0:User:myCustomAttribute</code>
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Attributes marked with returnability = "never" (such as a User password)
 ** will always be filtered out from the output, so including such attributes in
 ** <i>emit</i> has no effect.
 ** <p>
 ** <i><b>omit</b></i> is used to specify the set of attributes that should be
 ** excluded from the default attribute set. In this sense, the resources found
 ** in the response will include the attributes whose returnability = "always"
 ** in addition to those with returnability = "default" except for those
 ** included in <i>omit</i>. As with <i>emit</i>, this parameter must be in the
 ** form of a comma-separated list of attribute names.
 ** <p>
 ** <i>emit</i> and <i>omit</i> are mutually exclusive: if both are provided
 ** only <i>emit</i> will be taken into account to compute the output attribute
 ** set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/Users")
public class UserEndpoint extends AbstractEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Class name captured for logging purpose
   */
  private static final String CLASS  = UserEndpoint.class.getName();
  /**
   ** Logger created based on the class name
   */
  private static final Logger LOGGER = Logger.create(CLASS);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB(name="AccountsFacade")
  AccountsFacade facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   * REST service search request for SCIM {@link User} resources using GET (as
   * per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.2">section 3.4.2 of RFC 7644</a>)
   *
   * @param emit the {@link Set} of attributes to include in the
   * response.
   * @param omit the {@link Set} of attributes to exclude from
   * the response.
   * @param filter the filter criteria for user retrieval.
   * @param sortBy the attribute by which the results should be
   * sorted.
   * @param sortOrder the sorting order (ascending or descending).
   * @param startIndex the index of the first result to return.
   * @param count the number of results to return.
   * @param uriInfo the {@link UriInfo} for URI-related
   * information.
   * @param httpRequest the {@link HttpServletRequest} for HTTP-related
   * information.
   * @param sc the {@link SecurityContext} for
   * security-related information.
   * @param request the {@link Request} for handling HTTP request.
   *
   * @return the {@link Response} containing the list of
   * SCIM {@link User} resources matching the search
   * criteria.
   *
   * @throws Exception if an error occurs during the operation.
   */
  @GET
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response search(final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @QueryParam("filter") String filter, final @QueryParam("sortBy") String sortBy, final @QueryParam("sortOrder") String sortOrder, final @QueryParam("startIndex") Integer startIndex, final @QueryParam("count") Integer count, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws Exception {

    final String method = "search";
    this.entering(method);

    bka.iam.identity.scim.extension.model.ListResponse<User> listResponse = null;
    boolean            excludeApplicationsAttribute = excludeApplicationsAttribute(omit, emit);
    
    final String requesterId = httpRequest.getRemoteUser();
    
    
    final Map<String, Object> option = getQueryParameter(httpRequest, OIMScimContext.ATTRIBUTE_QUERY_PARAM,
                                                                      OIMScimContext.EXCLUDE_ATTRIBUTE_QUERY_PARAM);
    
    
    
    // Scope the search from the requester
    if (requesterId != null && !requesterId.equals("xelsysadm"))
      option.put(OIMScimContext.FILTER_ATTRIBUTE_QUERY_PARAM, "");
    
    listResponse = OIMScimContext.build(this, httpRequest).searchUsers(option)
                                                          .invokeList(User.class);;
    
    for (User user : listResponse.getResources()) {
      if (user.getUserName() != null)
        user.setId(user.getUserName());
      if (!excludeApplicationsAttribute)
        UserParser.apppendApplicationExtension(user, sc.getUserPrincipal().getName(), this.facade);
    }

    this.exiting(method, listResponse);
    return bka.iam.identity.scim.extension.parser.ResponseBuilder.response(200, listResponse, omit, emit, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   * REST service search request for SCIM {@link User} resources using POST (as
   * per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.3">section 3.4.3 of RFC 7644</a>)
   *
   * @param is the {@link InputStream} containing the search
   * criteria in SCIM format.
   * @param uriInfo the {@link UriInfo} for URI-related
   * information.
   * @param httpRequest the {@link HttpServletRequest} for HTTP-related
   * information.
   * @param sc the {@link SecurityContext} for
   * security-related information.
   * @param request the {@link Request} for handling HTTP request.
   *
   * @return the {@link Response} containing the list of
   * SCIM {@link User} resources matching the search
   * criteria.
   *
   * @throws Exception if an error occurs during the operation.
   */
  @POST
  @Path("/.search")
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response search(final InputStream is, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws Exception {

    final String method = "search";
    LOGGER.entering(CLASS, method);

      SearchRequest searchRequest = Unmarshaller.jsonNodeToResource((JsonNode)new ObjectMapper().readTree(is), null, SearchRequest.class);
      System.out.println(searchRequest);
      boolean       listAccounts = false;
      boolean       addUsernameToAttributes = false;
      if (searchRequest.getAttributes() == null) {
        listAccounts = true;
      }
      // TODO: Complete with new SCIM code
      /*else if (searchRequest.getAttributes().contains(ScimConstants.USER_APPLICATION_SCHEMA_URN + ":applications")) {
        searchRequest.getAttributes().remove(ScimConstants.USER_APPLICATION_SCHEMA_URN + ":applications");
        if (!searchRequest.getAttributes().contains("userName")) {
          searchRequest.getAttributes().add("userName");
          addUsernameToAttributes = true;
        }
        listAccounts = true;
      }*/
      
       final ListResponse listResponse  = OIMScimContext.build(this, httpRequest).postSearchUsers().invoke(User.class, is);
      
      this.exiting(method);
      return ResponseBuilder.response(200, listResponse, null, null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   * REST service request to retrieve a certain SCIM {@link User} resource by
   * its <code>id</code> using GET (as per
   * <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.1">section 3.4.1 of RFC 7644</a>)
   *
   * @param id the identifier of the user resource etrieve.
   * @param emit the {@link Set} of attributes to include in the
   * response.
   * @param omit the {@link Set} of attributes to exclude from
   * the response.
   * @param uriInfo the {@link UriInfo} for URI-related
   * information.
   * @param httpRequest the {@link HttpServletRequest} for HTTP-related
   * information.
   * @param sc the {@link SecurityContext} for
   * security-related information.
   * @param request the {@link Request} for handling HTTP request.
   *
   * @return the {@link Response} containing the SCIM
   * {@link User} resource.
   *
   * @throws ScimException if an error occurs during the operation.
   */
  @GET
  @Path("/{id}")
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response lookup(final @PathParam("id") String id, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException {

    final String method = "lookup";
    this.entering(method);
    bka.iam.identity.scim.extension.resource.User   user = null;
    String userKey = null;
    
    try {
      userKey = this.facade.getUserKey(id);
      
    }
    catch (Exception e) {
    }
    
    if (userKey == null) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.USER_NOTFOUND, id));          
    }
    
    final Map<String, Object> option = getQueryParameter(httpRequest, OIMScimContext.ATTRIBUTE_QUERY_PARAM,
                                                                      OIMScimContext.EXCLUDE_ATTRIBUTE_QUERY_PARAM);
    
    //boolean excludeApplicationsAttribute = excludeApplicationsAttribute(omit, emit);
    
    user = bka.iam.identity.scim.extension.rest.OIMScimContext.build(this, httpRequest).lookupUser(userKey, option);
    // Override resource ID with the userlogin  
    user.setId(id);
    
    //if (!excludeApplicationsAttribute) {
      UserParser.apppendApplicationExtension(user, sc.getUserPrincipal().getName(), this.facade);
    //}
    
    this.exiting(method, user);
    return bka.iam.identity.scim.extension.parser.ResponseBuilder.response(200, user, omit, emit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   * REST service request to create a certain SCIM {@link User} resource using
   * POST (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.3">section 3.3 of RFC 7644</a>)
   *
   * @param is the {@link InputStream} containing the data in
   * SCIM format.
   * @param emit the {@link Set} of attributes to include in the
   * response.
   * @param omit the {@link Set} of attributes to exclude from
   * the response.
   * @param uriInfo the {@link UriInfo} for URI-related
   * information.
   * @param httpRequest the {@link HttpServletRequest} for HTTP-related
   * information.
   * @param sc the {@link SecurityContext} for
   * security-related information.
   * @param request the {@link Request} for handling HTTP request.
   *
   * @return the {@link Response} containing the created
   * user.
   */
  @POST
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response create(final InputStream is, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException {
    final String method = "create";
    entering(method);
    
    final Map<String, Object> option = getQueryParameter(httpRequest);
    User user = null;
    try {
      final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(User.class);
      
      user = Unmarshaller.jsonNodeToResource((JsonNode)new ObjectMapper().readTree(is), resourceDescriptor , User.class);

      final String missingRequiredAttr = getMissingRequiredAttribute(user);
      // Check with the schemaDescriptor of the provided resource contains all required attributes
      if (missingRequiredAttr != null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, missingRequiredAttr));  
      }
      
      
      // Special requirement from BKA even if the schema allows it.
      if (user.getUserName() == null || user.getUserName().isEmpty()) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, "userName"));
      }
      if (user.getEmails() == null || user.getEmails().getValues().length == 0) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, "eMail"));
      }
      
      if (user.getGivenName() == null || user.getGivenName().isEmpty()) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, "givenName"));
      }
      
      user = OIMScimContext.build(this, httpRequest).createUser(user, option);
      user.setId(user.getUserName());

      return ResponseBuilder.response(201, user, null, null);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()); 
    }
    finally {
      exiting(method, user);
    }
  }
  
  private String getMissingRequiredAttribute(final User user) {
    List<String> requiredAttribute = user.getRegisterResourceDescriptor().getRequiredAttributeName();
    
    for (String requiredAttributeName : requiredAttribute) {
      if (user.getAttributeValue(requiredAttributeName) == null) {
        final int pos = requiredAttributeName.lastIndexOf(".");
        if (pos == -1)
          return requiredAttributeName;
        if (user.getAttributeValue(requiredAttributeName.substring(0, pos)) == null && !requiredAttribute.contains(requiredAttributeName.substring(0, pos))) {
          return null;
        }
        else {
          return requiredAttributeName;
        }
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   * REST service request to modify a certain SCIM {@link User} resource using
   * PUT (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.1">section 3.5.1 of RFC 7644</a>).
   *
   * @param is the {@link InputStream} containing the data in
   * SCIM format.
   * @param id the identifier of the application resource
   * replace.
   * @param emit the {@link Set} of attributes to include in the
   * response.
   * @param omit the {@link Set} of attributes to exclude from
   * the response.
   * @param uriInfo the {@link UriInfo} for URI-related
   * information.
   * @param httpRequest the {@link HttpServletRequest} for HTTP-related
   * information.
   * @param sc the {@link SecurityContext} for
   * security-related information.
   * @param request the {@link Request} for handling HTTP request.
   *
   * @return the {@link Response} containing the replaced
   * SCIM {@link User} resource.
   */
  @PUT
  @Path("/{id}")
  public Response replace(final InputStream is, final @PathParam("id") String id, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException, IOException {

    final String method = "replace";
    entering(method);
    
    User user = null;
    String userKey = null;
    
    try {
      userKey = this.facade.getUserKey(id);
    }
    catch (Exception e) {
    }
  
    if (userKey == null) {
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.USER_NOTFOUND, id));          
    }
    
    try {
      final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(User.class);
      
      if (userKey == null) {
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.USER_NOTFOUND, id));          
      }
      
      user = Unmarshaller.jsonNodeToResource((JsonNode)new ObjectMapper().readTree(is), resourceDescriptor, User.class);
      //user.setId(userKey);
      user = OIMScimContext.build(this, httpRequest).modifyUser(userKey, user);
     }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    finally {
      this.exiting(method, user);
    }
    
    return ResponseBuilder.response(HTTPContext.StatusCode.OK.getStatusCode(), user, omit, emit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** REST service request to modify a certain SCIM {@link User} resource using
   ** PATCH (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2">section 3.5.2 of RFC 7644</a>).
   **  
   ** This method bypasses certain limitations in the out-of-the-box (OOTB) SCIM 
   ** interface for patching user resources. These limitations include:
   ** <ul>
   **   <li> PATCH for complex multi-valued attributes is not supported.</li>
   **   <li> Atomic operations are not supported.</li>
   ** </ul>
   ** 
   ** To work around these limitations:
   ** <ul>
   **  <li> The PATCH operation is converted into a PUT operation.</li>
   **   <li> Before executing the operation, the original state of the user is
   **       saved. If any issue arises during the operation, the original state is restored.</li>
   ** </ul>
   ** 
   ** @param is the {@link InputStream} containing the data in SCIM format.
   ** @param id the identifier of the application resource to modify.
   ** @param emit the {@link Set} of attributes to include in the response.
   ** @param omit the {@link Set} of attributes to exclude from the response.
   ** @param uriInfo the {@link UriInfo} for URI-related information.
   ** @param httpRequest the {@link HttpServletRequest} for HTTP-related information.
   ** @param sc the {@link SecurityContext} for security-related information.
   ** @param request the {@link Request} for handling the HTTP request.
   ** 
   ** @return the {@link Response} containing the modified SCIM {@link User} resource.
   */
  @PATCH
  @Path("/{id}")
  public Response modify(final InputStream is, final @PathParam("id") String id, final @QueryParam("attributes") Set<String> emit,
                       final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo,
                       final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
        throws ScimException, Exception {
    final String method = "modify";
    this.entering(method);
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(User.class);
    String userKey = null;
    
    try {
      userKey = this.facade.getUserKey(id);
    }
    catch (Exception e) {
    }
  
    if (userKey == null) {
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.USER_NOTFOUND, id));          
    }
  
    PatchRequest patchRequest;
    User user = bka.iam.identity.scim.extension.rest.OIMScimContext.build(this, httpRequest).lookupUser(userKey, new HashMap<String, Object>());
    
    List<String> requiredAttribute = resourceDescriptor.getRequiredReadWriteName();
    List<String> specialAttributeHandling = Arrays.asList("urn:ietf:params:scim:schemas:extension:oracle:2.0:IDM:User:locked", "urn:ietf:params:scim:schemas:core:2.0:User:active", "active");
    
    // Schema violation
    requiredAttribute = requiredAttribute.stream().filter(attribute ->   !specialAttributeHandling.stream().anyMatch(special -> attribute.startsWith(special))).collect(Collectors.toList());
    
    User originalUserState = user.copyResourceWithAttribute(requiredAttribute, User.class);
  
    try {
        patchRequest = Unmarshaller.jsonNodeToPatchRequest(new ObjectMapper().readTree(is));
        for (final Operation operation : patchRequest.getOperations()) {          
          if (operation.getOperationType().equals(OperationType.REMOVE)) {
            // Escape double quotes in the path if needed
            Operation excapedOperation = new Operation(operation.getOperationType(), operation.getPath().replace("\"", "\\\"").replace("\'", "\\\""), operation.getValue());
            OIMScimContext.build(this, httpRequest).replaceUser(userKey, new PatchRequest(OIMSchema.getInstance().getResourceDescriptorByResourceType(PatchRequest.class), Arrays.asList(excapedOperation)));
          }
          else if (operation.getOperationType().equals(OperationType.REPLACE) && specialAttributeHandling.stream().anyMatch(special -> operation.getPath().startsWith(special))) {
            OIMScimContext.build(this, httpRequest).replaceUser(userKey, new PatchRequest(OIMSchema.getInstance().getResourceDescriptorByResourceType(PatchRequest.class), Arrays.asList(operation)));
          }
          else {
            user = user.copyResourceWithAttribute(requiredAttribute, User.class);
            JsonNode userJson = Marshaller.resourceToJsonNode(user, null, null);
            JsonNode patchedNode = PatchUtil.applyPatch(userJson, operation);  
            user = Unmarshaller.jsonNodeToResource(patchedNode, resourceDescriptor , User.class);
            user = OIMScimContext.build(this, httpRequest).modifyUser(userKey, user);
          }
        }
                
    } catch (ScimException e) {
      // Revert to original state if needed
      OIMScimContext.build(this, httpRequest).modifyUser(userKey, originalUserState);
      throw e;
    } 
    catch (IOException e) {
      // Revert to original state if needed
      OIMScimContext.build(this, httpRequest).modifyUser(userKey, originalUserState);
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, e.getMessage());
    }
    user.setId(id);
    this.exiting(method);
    return ResponseBuilder.response(200, user, omit, emit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   * REST service request to remove a certain SCIM {@link User} resource by its
   * <code>id</code> using PATCH (as per
   * <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.6">section 3.6 of RFC 7644</a>).
   *
   * @param resource The resource path.
   * @param id the identifier of the user resource to remove.
   * @param uriInfo the {@link UriInfo} for URI-related
   * information.
   * @param httpRequest the {@link HttpServletRequest} for HTTP-related
   * information.
   * @param sc the {@link SecurityContext} for
   * security-related information.
   * @param request the {@link Request} for handling HTTP request.
   *
   * @return the {@link Response} indicating the success of
   * the delete operation.
   *
   * @throws Exception if an error occurs during the operation.
   */
  @DELETE
  @Path("/{id}")
  public Response delete(final @PathParam("resource") String resource, final @PathParam("id") String id, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
  throws ScimException {

    final String method = "delete";
    entering(method);
    String userKey = null;
    
    try {
      userKey = this.facade.getUserKey(id);
    }
    catch (Exception e) {
      // Do nothing as the exception is thrown later
    }
    
    if (userKey == null) {
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.USER_NOTFOUND, id));          
    }
    
    Response response = OIMScimContext.build(this, httpRequest).deleteUser(userKey);
    exiting(method);
    return response;
  }

  @OPTIONS
  @Path("/{id}")
  public Response handlePreflightCheckWithID(@Context HttpServletRequest httpRequest)
    throws Exception {
    return null;
  }

  @OPTIONS
  public Response
  handlePreflightCheckWithUser(@Context HttpServletRequest httpRequest)
    throws Exception {
    return null;
  }

  private boolean excludeApplicationsAttribute(final Set<String> requested, final Set<String> excluded) {
    String  appsAttr = ScimConstants.USER_APPLICATION_SCHEMA_URN + ":" + "applications";
    boolean emit = true;
    for (String cursor : requested) {
      if (cursor.contains(appsAttr)) {
        cursor.replace(appsAttr, "");
        break;
      }
      emit = false;
    }

    boolean omit = false;
    for (String cursor : excluded) {
      if (cursor.contains(appsAttr)) {
        omit = true;
        cursor.replace(appsAttr, "");
        break;
      }
    }
    return (emit && !omit) ? false : true;
  }
}