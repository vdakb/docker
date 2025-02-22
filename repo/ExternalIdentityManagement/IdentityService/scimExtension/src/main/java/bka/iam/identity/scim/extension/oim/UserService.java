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

    Copyright © 2025. All Rights reserved.

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   UserService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class UserService.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-02-21  SBernet     First release version
*/
package bka.iam.identity.scim.extension.oim;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.oim.Option.SortOrder;
import bka.iam.identity.scim.extension.option.AttributePathVisitor;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scim.extension.rest.RequestContext;

import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserAlreadyExistsException;
import oracle.iam.identity.exception.UserCreateException;
import oracle.iam.identity.exception.UserDeleteException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.UserManagerException;
import oracle.iam.identity.exception.UserMembershipException;
import oracle.iam.identity.exception.UserModifyException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
////////////////////////////////////////////////////////////////////////////////
// class UserService
// ~~~~~ ~~~~~~~~~~~
/**
 ** This class is responsible for managing OIM User operations.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UserService extends OIMServiceContext {
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
 
  private static final SearchCriteria DEFAULT_SEARCH_CRITERIA =  new SearchCriteria(UserManagerConstants.AttributeName.USER_KEY.getId(), "*", SearchCriteria.Operator.EQUAL);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserService</code> with zero argument allows use 
   ** as a JavaBean.
   ** <br>
   ** Default Constructor
   */
  private UserService() {
    super();
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserService</code> instance with search parameters.
   **
   ** @param  count                 The number of results to return.
   ** @param  startIndex            The index of the first result.
   ** @param  sortOrder             The sorting order (ascending or descending).
   ** @param  requiredAttributeName Set of attributes to include.
   ** @param  excludedAttributeName Set of attributes to exclude.
   */
  private UserService(final Integer count, final Integer startIndex, final String sortBy, final SortOrder sortOrder, final Set<String> requiredAttributeName, final Set<String> excludedAttributeName) {
    super(count, startIndex, sortBy, sortOrder, requiredAttributeName, excludedAttributeName);
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserService</code> instance with attribute filtering.
   **
   ** @param  requiredAttributeName Set of attributes to include.
   ** @param  excludedAttributeName Set of attributes to exclude.
   */
  private UserService(final Set<String> requiredAttributeName, final Set<String> excludedAttributeName) {
    super(requiredAttributeName, excludedAttributeName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Builder Methods
  //////////////////////////////////////////////////////////////////////////////
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Creates a new instance of <code>UserService</code> with search parameters.
   **
   ** @param  count                 The number of results to return.
   ** @param  startIndex            The index of the first result.
   ** @param  sortOrder             The sorting order (ascending or descending).
   ** @param  requiredAttributeName Set of attributes to include.
   ** @param  excludedAttributeName Set of attributes to exclude.
   **
   ** @return                       A new instance of {@link UserService}.
   */
  public static UserService build(final Integer count, final Integer startIndex, final String sortBy, final SortOrder sortOrder, final Set<String> requiredAttributeName, final Set<String> excludedAttributeName)
    throws ScimException {
    String oimSortByAttribute = null;
    if (sortBy != null) {
      // Spcial handling as ID must be hidden
      if (sortBy != null &&  sortBy.equals("id")) {
        oimSortByAttribute = UserManagerConstants.AttributeName.USER_KEY.getId();
      } else {
        final List<String> oimAttributes = OIMScimMapping.getOimAttribute(sortBy);
        if (oimAttributes.size() == 0) {
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_FILTER, ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, sortBy));
        }
        else {
          oimSortByAttribute = oimAttributes.get(0);
        }
      }
    }
    
    
    
    return new UserService(count, startIndex, oimSortByAttribute, sortOrder, requiredAttributeName, excludedAttributeName);
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Creates a new instance of <code>UserService</code> with attribute filtering.
   **
   ** @param  requiredAttributeName Set of attributes to include.
   ** @param  excludedAttributeName Set of attributes to exclude.
   **
   ** @return                       A new instance of {@link UserService}.
   */
  public static UserService build(final Set<String> requiredAttributeName, final Set<String> excludedAttributeName) {
    return new UserService(requiredAttributeName, excludedAttributeName);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Creates a default instance of <code>UserService</code>.
   **
   ** @return  A new instance of {@link UserService}.
   */ 
  public static UserService build() {
    return new UserService();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Inherited Methods
  //////////////////////////////////////////////////////////////////////////////
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   getDefaultSearchCriteria
  /**
   ** Provides the default search criteria for OIM user queries.
   **
   ** @return                    The default search criteria.
   **                            Allowed object is {@link SearchCriteria}.
   */
  @Override
  protected SearchCriteria getDefaultSearchCriteria() {
    final SearchCriteria nonDeletedUserCriteria = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(), "Deleted", SearchCriteria.Operator.NOT_EQUAL);
    return new SearchCriteria(DEFAULT_SEARCH_CRITERIA, nonDeletedUserCriteria, SearchCriteria.Operator.AND);
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceAttributeList
  /**
   ** Retrieves the list of SCIM attributes for the User resource.
   **
   ** @return                    A list of SCIM attribute names.
   **                            Allowed object is {@link List}.
   */
  @Override
  protected List<String> getResourceAttributeList() {
    List<String> resourceAttribute =  new ArrayList<String>();
    
    for (OIMScimMapping mapping : OIMScimMapping.values()) {
      resourceAttribute.add(mapping.getScimAttribute());
    }
    
    return resourceAttribute;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a SCIM User resource in OIM.
   **
   ** @param  resource           The SCIM User resource to create.
   **                            Allowed object is {@link ScimResource}.
   **
   ** @return                    The created SCIM Userresource.
   **                            Allowed object is {@link ScimResource}.
   **
   ** @throws ScimException      If the resource creation fails.
   */
  @Override
  protected bka.iam.identity.scim.extension.resource.User create(ScimResource resource)
    throws ScimException {
    final String method = "create";
    this.entering(method);
    
    User oimUser = new User(null);
    String  oimPassword = null;
    Boolean usrStatus   = null;
    try {
      for (OIMScimMapping mapping : OIMScimMapping.values()) {
          String oimAttribute = mapping.getOimAttribute();
          Object scimValue = resource.getAttribute(mapping.getScimAttribute());
          
            if (scimValue != null && mapping.equals(OIMScimMapping.STATUS)) {
              if (scimValue.equals(false))
                usrStatus = false;
              continue;
            }
            else if (scimValue != null && (mapping.equals(OIMScimMapping.PARTICIPANT))) {
              oimUser.setAttribute(oimAttribute, scimValue);
              oimAttribute = UserManagerConstants.AttributeName.USER_ORGANIZATION.getId();
              scimValue = getOrganizationId((String) scimValue);
            }
            else if (scimValue != null && (mapping.equals(OIMScimMapping.PASSWORD))) {
              oimPassword = (String) scimValue;
              continue;
            }
            oimUser.setAttribute(oimAttribute, scimValue);
      }
      if (!oimUser.getAttributes().containsKey(UserManagerConstants.AttributeName.USERTYPE.getId())) {
        oimUser.setAttribute(UserManagerConstants.AttributeName.EMPTYPE.getId(), "Full-Time");
      }
      if (!oimUser.getAttributes().containsKey(UserManagerConstants.AttributeName.USER_ORGANIZATION.getId())) {
        // Schema required the attribute participan so he should never failed
        String participantValue = (String) resource.getAttribute(OIMScimMapping.PARTICIPANT.getScimAttribute());
        oimUser.setAttribute(UserManagerConstants.AttributeName.USER_ORGANIZATION.getId(), getOrganizationId(participantValue) );
      }
    
      UserManagerResult oimUserResult = getUserService().create(oimUser);
      
      if (oimUserResult.getStatus().equalsIgnoreCase("COMPLETED")) {
        if (oimPassword != null && !oimPassword.isEmpty()) {
          getUserService().changePassword(oimUserResult.getEntityId(), oimPassword.toCharArray(), false);
        }
        if (usrStatus != null && usrStatus == false)
            getUserService().disable(oimUserResult.getEntityId(), false);
        return lookupResource(oimUserResult.getEntityId(), UserManagerConstants.AttributeName.USER_KEY.getId());
      }
    }
    catch (UserAlreadyExistsException e) {
      throw new ScimException(HTTPContext.StatusCode.CONFLICT,  e.getMessage());
    }
    catch (UserCreateException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,  e.getMessage());
    }
    catch (ValidationFailedException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,  e.getMessage());
    }
    catch (AccessDeniedException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  e.getMessage());
    }
    catch (NoSuchUserException e) {
      throw new ScimException(HTTPContext.StatusCode.PARTIAL_CONTENT,  e.getMessage());
    }
    catch (UserManagerException e) {
      throw new ScimException(HTTPContext.StatusCode.PARTIAL_CONTENT,  e.getMessage());
    }
    finally {
      this.entering(method);
    }
    throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR,  ScimBundle.string(ScimMessage.USER_CANNOT_CREATE));
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches for SCIM User resources in OIM.
   **
   ** @return                    A list of SCIM User resources matching the
   **                            criteria.
   **                            Allowed object is {@link List}.
   **
   ** @throws ScimException      If the search operation fails.
   */
  @Override
  protected List<bka.iam.identity.scim.extension.resource.User> search()
    throws ScimException {
    final String method = "search";
    this.entering(method);
    
    final List<bka.iam.identity.scim.extension.resource.User> scimUsers = new ArrayList<bka.iam.identity.scim.extension.resource.User>();
    
    try {
      final List<User> users = getUserService().search(this.options.getSearchCriteria(), new HashSet<String>(), getSearchOptions());
            
      for (User user : users) {
        scimUsers.add(buildScimUser(user));
      }
    }
    catch (UserSearchException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    finally {
      this.entering(method);
    }
    return scimUsers;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Replaces an existing SCIM User resource in OIM.
   **
   ** @param  entityID           The unique identifier of the resource to replace.
   **                            Allowed object is {@link String}.
   ** @param  resource           The new SCIM User resource to replace the
   **                            existing one.
   **                            Allowed object is {@link ScimResource}.
   **
   ** @return                    The updated SCIM User resource.
   **                            Allowed object is {@link ScimResource}.
   **
   ** @throws ScimException      If the replacement operation fails.
   */
  @Override
  protected <T extends ScimResource> T replace(final String entityID, T resource)
    throws ScimException {
    final String method = "replace";
    this.entering(method);
    
    try {
      final String usrKey = getUserKey(entityID);
      final User oimUser = new User(usrKey);
      
      String  usrPassword = null;
      Boolean usrStatus   = null;
      AttributePathVisitor visitor = new AttributePathVisitor();
      Set<String> attributesToModify = visitor.visit(resource);
      Set<String> oimAttributeToModify = new HashSet<String>();
      this.debug(method, "SCIM attribute to replace: " + attributesToModify);
      for (String attributePath : attributesToModify) {
        oimAttributeToModify.addAll(OIMScimMapping.getOimAttribute(attributePath));
      }
      this.debug(method, "OIM attribute to replace: " + oimAttributeToModify);
      for (OIMScimMapping mapping : OIMScimMapping.values()) {
        if (oimAttributeToModify.contains(mapping.getOimAttribute())) {
          Object scimValue = resource.getAttribute(mapping.getScimAttribute());
          if (mapping.equals(OIMScimMapping.UPN)) {
            if (!getUserService().getDetails(usrKey, null, false).getAttribute(mapping.getOimAttribute()).equals(scimValue))
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.MUTABILITY, "User Principale Name is immutable");
          }
          if (scimValue != null) {
            if ((mapping.equals(OIMScimMapping.PASSWORD))) {
                usrPassword = (String) scimValue;
                continue;
            }
            if (mapping.equals(OIMScimMapping.STATUS)) {
              if (scimValue.equals(true))
                usrStatus = true;
              else
                usrStatus = false;
              continue;
            }
          }
          oimUser.setAttribute(mapping.getOimAttribute(), scimValue);
        }
      }
      this.debug(method, "User is modified with the following values: " + oimUser.getAttributes());
      
      UserManagerResult oimUserResult = getUserService().modify(oimUser);
      if (oimUserResult.getStatus().equalsIgnoreCase("COMPLETED")) {
        if (usrPassword != null && !usrPassword.isEmpty()) {
          getUserService().changePassword(oimUserResult.getEntityId(), usrPassword.toCharArray(), false);
        }
        if (usrStatus != null) {
          if (usrStatus == false)
            getUserService().disable(oimUserResult.getEntityId(), false);
          else
            getUserService().enable(oimUserResult.getEntityId(), false);
        }
        return lookupResource(oimUserResult.getEntityId(), UserManagerConstants.AttributeName.USER_KEY.getId());
      }
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR,  oimUserResult.getStatus());
    }
    catch (UserModifyException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (ValidationFailedException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (NoSuchUserException e) {
       throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (UserLookupException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (UserManagerException e) {
      throw new ScimException(HTTPContext.StatusCode.PARTIAL_CONTENT,  e.getMessage());
    }
    finally {
      this.exiting(method);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes a SCIM User resource from OIM.
   **
   ** @param  entityID           The unique identifier of the resource to delete.
   **                            Allowed object is {@link String}.
   **
   ** @throws ScimException      If the deletion operation fails.
   */
  @Override
  protected void delete(String entityID)
    throws ScimException {
    final String method = "delete";
    this.entering(method);
    
    try {
      String userKey = getUserKey(entityID);
      UserManagerResult oimResult = getUserService().delete(userKey, false);
      if (oimResult != null && !"COMPLETED".equalsIgnoreCase(oimResult.getStatus())) {
        throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, ScimBundle.format(ScimMessage.USER_CANNOT_DELETE, entityID));
      }
    }
    catch (NoSuchUserException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    catch (UserDeleteException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (ValidationFailedException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,  e.getMessage());
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupUserLogin
  /**
   ** Looks up a user in OIM based on the login.
   **
   ** @param userLogin           The login ID of the user to retrieve.
   **                            Allowed object is {@link String}.
   **
   ** @return                    The retrieved SCIM user object.
   **                            Allowed object is {@link bka.iam.identity.scim.extension.resource.User}.
   **
   ** @throws ScimException      If the user cannot be found or an error occurs.
   */
  public bka.iam.identity.scim.extension.resource.User lookupUserLogin(final String userLogin)
    throws ScimException {
      return lookupResource(userLogin, UserManagerConstants.AttributeName.USER_LOGIN.getId());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserKey
  /**
   ** Retrieves the unique key of a user in OIM based on the user login.
   **
   ** @param userLogin           The login ID of the user.
   **                            Allowed object is {@link String}.
   **
   ** @return                    The unique user key.
   **                            Allowed object is {@link String}.
   **
   ** @throws ScimException      If the user cannot be found or an error occurs.
   */
  public String getUserKey(final String userLogin)
    throws ScimException {
    final String method = "getUserKey";
    this.entering(method);
    
    try {
      return getUserService().getDetails(userLogin, new HashSet<String>(), true).getId();
    }
    catch (NoSuchUserException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    catch (UserLookupException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (AccessDeniedException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  e.getMessage());
    }
    finally {
      this.exiting(method);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   getOrganizationId
  /**
   ** Retrieves the unique identifier of an organization in OIM based on its
   ** name.
   ** <p>
   ** This method searches for an organization by name in OIM and returns its 
   ** corresponding unique identifier.
   ** </p>
   **
   ** @param  orgName             The name of the organization to retrieve.
   **                             Allowed object is {@link String}.
   **
   ** @return                     The unique identifier of the organization.
   **                             Allowed object is {@link Long}.
   **
   ** @throws ScimException       If the organization is not found or an error occurs.
   */
  private Long getOrganizationId(final String orgName)
    throws ScimException {
    
    try {
      Organization orgnization = getOrganizationService().getDetails(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), orgName, null);
      return  Long.parseLong(orgnization.getEntityId());
    }
    catch (AccessDeniedException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  e.getMessage());
    }
    catch (OrganizationManagerException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,  e.getMessage());
    }
    catch (SearchKeyNotUniqueException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR,  e.getMessage());
    }
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   buildScimUserGroup
  /**
   ** Builds and assigns SCIM group memberships for a user in OIM.
   ** <p>
   ** This method retrieves the user's role memberships from OIM, determines 
   ** whether each membership is direct or indirect, and adds the appropriate 
   ** group references to the SCIM user object.
   ** </p>
   **
   ** @param  entityKey           The unique identifier of the user.
   **                             Allowed object is {@link String}.
   ** @param  scimUser            The SCIM user object to which group 
   **                             memberships should be added.
   **                             Allowed object is {@link bka.iam.identity.scim.extension.resource.User}.
   **
   ** @throws ScimException       If an error occurs while retrieving role memberships.
   */
  private void buildScimUserGroup(final String entityKey, final bka.iam.identity.scim.extension.resource.User scimUser)
    throws ScimException {
    final RoleManager roleMng = getRoleService();
    try {
      List<Role> roles = roleMng.getUserMemberships(entityKey, true);
      
      for (int i = 0; i < roles.size(); i++) {
        final String groupRef = String.format("%s%s/%s", RequestContext.getUriInfo().getBaseUri().toString(), OIMScimContext.ENDPOINT_GROUPS, roles.get(i).getEntityId());
        Boolean directProvisionning = roles.get(i).getAttributes().get("User Membership Rule") == null ? true : false;
        scimUser.addGroup(i, directProvisionning ? "direct" : "indirect", roles.get(i).getEntityId(), groupRef);
      }
    }
    catch (UserMembershipException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   buildScimUser
  /**
   ** Converts an OIM user object into a SCIM-compliant user resource.
   ** <p>
   ** This method extracts user attributes from OIM, applies necessary 
   ** transformations, and maps them to SCIM attributes.
   **
   ** @param  user                The OIM user object to be converted.
   **                             Allowed object is {@link User}.
   **
   ** @return                     The SCIM representation of the user.
   **                             Allowed object is {@link bka.iam.identity.scim.extension.resource.User}.
   **
   ** @throws ScimException       If an error occurs during attribute mapping
   **                             or encoding.
   */
  private bka.iam.identity.scim.extension.resource.User buildScimUser(final User user)
    throws ScimException {
    
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(bka.iam.identity.scim.extension.resource.User.class);
    final bka.iam.identity.scim.extension.resource.User userScim = new bka.iam.identity.scim.extension.resource.User(resourceDescriptor);
    
    final Map<String, Object> userAttribute = user.getAttributes();
    
    final Date createdDate  = userAttribute.containsKey("usr_create") ? (Date) userAttribute.get("usr_create") : null;
    final Date modifiedDate = userAttribute.containsKey("usr_update") ? (Date) userAttribute.get("usr_update") : null;
    try {
      String encodedUserName = URLEncoder.encode(user.getLogin(), StandardCharsets.UTF_8.toString());
      userScim.add(ScimResource.createSchema(bka.iam.identity.scim.extension.resource.User.SCHEMAS));
      userScim.add(ScimResource.createMeta(createdDate, modifiedDate, new URI(RequestContext.getUriInfo().getBaseUri().toString() + OIMScimContext.ENDPOINT_USERS + "/" + encodedUserName), "User"));
      userScim.setId(user.getLogin());
      for (OIMScimMapping mapping : OIMScimMapping.values()) {
        Object value = user.getAttributes().get(mapping.getOimAttribute());

        if (value != null) {
          if (mapping.equals(OIMScimMapping.STATUS)) {
            if (value.equals("Active"))
              value = true;
            else
              value = false;
          }
          if (mapping.equals(OIMScimMapping.EMAILWORK)) {
            userScim.addAttribute("emails[work].primary", true);
          }
          userScim.addAttribute(mapping.getScimAttribute(), value);
        }
      }
      buildScimUserGroup(user.getEntityId(), userScim);
      
    }
    catch (URISyntaxException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (UnsupportedEncodingException e) {
       throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    return userScim;
  }
  
  public enum OIMScimMapping {
    
    //Maping belonging to the User core,
    USERNAME("userName", "User Login"),
    
    FAMILYNAME("name.familyName", "Last Name"),
    GIVENNAME("name.givenName","First Name"),
    MIDDLENAME("name.middleName", "Middle Name"),
    HONOPREFIX("name.honorificPrefix", "honorificPrefix"),
    HONORSUFFIX("name.honorificSuffix", "honorificSuffix"),
    
    DISPLAYNAME("displayName", "Display Name"),
    
    TITLE("title", "Title"),
    
    USERTYPE("userType", "Role"),

    PREFERREDLANGUAGE("preferredLanguage", "usr_locale"),
    
    
    STATUS("active", "Status"),
    
    PASSWORD("password", "usr_password"),
    
    EMAILWORK("emails[work].value", "Email"),
    
    PHONEWORK("phoneNumbers[work].value", "Telephone Number"),
    PHONECNP("phoneNumbers[cnp].value", "policeCnpTelephoneNumber"),

    COUNTRYWORK("addresses[work].country", "Country"),
    STREETWORK("addresses[work].streetAddress", "Street"),
    POSTALCODEWORK("addresses[work].postalCode","Postal Code"),
    LOCALITYWORK("addresses[work].locality", "Locality Name"),
    STATEWORK("addresses[work].region", "State"),
    
    
    //Maping belonging to the User enterprise
    DIVISION("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:division", "division"),
    DEPARTMENT("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:department", "department"),
    ORGANIZATIONUNIT("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:organization", "organizationalUnit"),
    
    
    //Maping belonging to the User police
    UPN("urn:ietf:params:scim:schemas:extension:police:2.0:User:userPrincipalName", "Generation Qualifier"),
    IDENTIFIERGENERATED("urn:ietf:params:scim:schemas:extension:police:2.0:User:idpUserId", "identifierGenerated"),
    UNIQUEIDENTIFIER("urn:ietf:params:scim:schemas:extension:police:2.0:User:uniqueIdentifier", "uniqueIdentifier"),
    PARTICIPANT("urn:ietf:params:scim:schemas:extension:police:2.0:User:idp", "participant"),
    XCATALOGOU("urn:ietf:params:scim:schemas:extension:police:2.0:User:officeIdentifier", "xcatalogOU"),
    NAMESUFFIX("urn:ietf:params:scim:schemas:extension:police:2.0:User:nameSuffix", "nameSuffix"),
    TITLEKEY("urn:ietf:params:scim:schemas:extension:police:2.0:User:policeTitleKey", "policeTitleKey");
    
    
    private final String scimAttribute;
    private final String oimAttribute;

    OIMScimMapping(String scimAttribute, String oimAttribute) {
        this.scimAttribute = scimAttribute;
        this.oimAttribute = oimAttribute;
    }

    public String getScimAttribute() {
        return scimAttribute;
    }

    public String getOimAttribute() {
        return oimAttribute;
    }

    public static List<String> getOimAttribute(String scimAttr) {
      List<String> oimAttributes = new ArrayList<String>();
        for (OIMScimMapping mapping : values()) {
            if (mapping.getScimAttribute().equals(scimAttr) || mapping.getScimAttribute().startsWith(scimAttr)) {
                oimAttributes.add(mapping.getOimAttribute());
            }
        }
        return oimAttributes;
    }
  }


}
