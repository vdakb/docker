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

    File        :   RoleService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class RoleService.

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
import bka.iam.identity.scim.extension.resource.Group;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scim.extension.rest.RequestContext;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.RoleGrantException;
import oracle.iam.identity.exception.RoleGrantRevokeException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.RoleSearchException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
////////////////////////////////////////////////////////////////////////////////
// class RoleService
// ~~~~~ ~~~~~~~~~~~
/**
 ** This class is responsible for managing OIM Role operations.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleService extends OIMServiceContext {
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
 
  private static final SearchCriteria DEFAULT_SEARCH_CRITERIA =  new SearchCriteria(RoleManagerConstants.RoleAttributeName.KEY.getId(), "*", SearchCriteria.Operator.EQUAL);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleService</code> with zero argument allows use 
   ** as a JavaBean.
   ** <br>
   ** Default Constructor
   */
  private RoleService() {
    super();
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleService</code> instance with search parameters.
   ** <br>
   ** Method are private to avoid to initiate an <code>RoleService</code>,
   ** please use the builder.
   **
   ** @param  count                 The number of results to return.
   ** @param  startIndex            The index of the first result.
   ** @param  sortOrder             The sorting order (ascending or descending).
   ** @param  requiredAttributeName Set of attributes to include.
   ** @param  excludedAttributeName Set of attributes to exclude.
   */
  private RoleService(final Integer count, final Integer startIndex, final String sortBy, final SortOrder sortOrder, final Set<String> requiredAttributeName, final Set<String> excludedAttributeName) {
    super(count, startIndex, sortBy, sortOrder, requiredAttributeName, excludedAttributeName);
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleService</code> instance with attribute filtering.
   ** Method are private to avoid to initiate an <code>RoleService</code>,
   ** please use the builder.
   **
   ** @param  requiredAttributeName Set of attributes to include.
   ** @param  excludedAttributeName Set of attributes to exclude.
   */
  private RoleService(final Set<String> requiredAttributeName, final Set<String> excludedAttributeName) {
    super(requiredAttributeName, excludedAttributeName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Builder Methods
  //////////////////////////////////////////////////////////////////////////////
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Creates a new instance of <code>RoleService</code> with search parameters.
   **
   ** @param  count                 The number of results to return.
   ** @param  startIndex            The index of the first result.
   ** @param  sortOrder             The sorting order (ascending or descending).
   ** @param  requiredAttributeName Set of attributes to include.
   ** @param  excludedAttributeName Set of attributes to exclude.
   **
   ** @return                       A new instance of {@link RoleService}.
   */
  public static RoleService build(final Integer count, final Integer startIndex, final String sortBy, final SortOrder sortOrder, final Set<String> requiredAttributeName, final Set<String> excludedAttributeName)
    throws ScimException {
    String oimSortByAttribute = null;
    // Spcial handling as ID must be hidden
    if (sortBy != null) {
      if (sortBy.equals("id")) {
        oimSortByAttribute = RoleManagerConstants.RoleAttributeName.KEY.getId();
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
    
    return new RoleService(count, startIndex, oimSortByAttribute, sortOrder, requiredAttributeName, excludedAttributeName);
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Creates a new instance of <code>RoleService</code> with attribute filtering.
   **
   ** @param  requiredAttributeName Set of attributes to include.
   ** @param  excludedAttributeName Set of attributes to exclude.
   **
   ** @return                       A new instance of {@link RoleService}.
   */
  public static RoleService build(final Set<String> requiredAttributeName, final Set<String> excludedAttributeName) {
    return new RoleService(requiredAttributeName, excludedAttributeName);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Creates a default instance of <code>RoleService</code>.
   **
   ** @return  A new instance of {@link RoleService}.
   */
  public static RoleService build() {
    return new RoleService();
  }
  
  public bka.iam.identity.scim.extension.resource.Group lookupRoleName(final String roleName)
    throws ScimException {
      return lookupResource(roleName, RoleManagerConstants.RoleAttributeName.NAME.getId());
  }
  
  public bka.iam.identity.scim.extension.resource.Group lookupRoleID(final String roleID)
    throws ScimException {
      return lookupResource(roleID, RoleManagerConstants.RoleAttributeName.KEY.getId());
  }
  
  private String getRoleKey(final String roleName)
    throws ScimException {
    final String method = "getRoleKey";
    this.entering(method);
    
    try {
      return getRoleService().getDetails(roleName, new HashSet<String>()).getEntityId();
    }
    catch (NoSuchRoleException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    catch (RoleLookupException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (AccessDeniedException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  e.getMessage());
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Inherited Methods
  //////////////////////////////////////////////////////////////////////////////
  
  @Override
  protected SearchCriteria getDefaultSearchCriteria() {
    return DEFAULT_SEARCH_CRITERIA;
  }

  @Override
  protected Group create(ScimResource resource)
    throws ScimException {
    final String method = "create";
    this.entering(method);
    
    throw new ScimException(HTTPContext.StatusCode.METHOD_NOT_ALLOWED,  ScimBundle.string(ScimMessage.GROUP_CREATE_NOT_SUPPORTED));
    /*HashMap<String, Object> roleAttrs = new HashMap<String, Object>();
    //Role oimRole =;
    List<String> members = null;
    try {
      for (OIMScimMapping mapping : OIMScimMapping.values()) {
          String oimAttribute = mapping.getOimAttribute();
          Object scimValue = resource.getAttribute(mapping.getScimAttribute());
          
          if (scimValue != null && mapping.equals(OIMScimMapping.MEMBERS)) {
            members = ((Group) resource).getMembersAsListString();
            continue;
          }
          roleAttrs.put(oimAttribute, scimValue);
      }
      
      this.debug(method, "New Role" + roleAttrs);
    
      RoleManagerResult oimRoleResult = getRoleService().create(new Role(roleAttrs));
      
      if (oimRoleResult.getStatus().equalsIgnoreCase("COMPLETED")) {
        if (members != null && !members.isEmpty()) {
          getRoleService().grantRole(oimRoleResult.getEntityId(), new HashSet<String>(members));
        }
        return lookupResource(oimRoleResult.getEntityId(), UserManagerConstants.AttributeName.USER_KEY.getId());
      }
    }
    catch (RoleAlreadyExistsException e) {
      throw new ScimException(HTTPContext.StatusCode.CONFLICT,  e.getMessage());
    }
    catch (RoleCreateException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,  e.getMessage());
    }
    catch (ValidationFailedException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,  e.getMessage());
    }
    catch (AccessDeniedException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  e.getMessage());
    }
    catch (RoleGrantException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  e.getMessage());
    }

    finally {
      this.entering(method);
    }
    throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR,  ScimBundle.string(ScimMessage.GROUP_CANNOT_CREATE));
    */
  }

  @Override
  protected List<Group> search()
    throws ScimException {
    final String method = "search";
    this.entering(method);
    
    final List<Group> scimGroups = new ArrayList<Group>();
    
    try {
      final List<Role> roles = getRoleService().search(this.options.getSearchCriteria(), null, getSearchOptions());
            
      for (Role role : roles) {
        scimGroups.add(buildScimGroup(role, getRoleService().getRoleMembers(role.getEntityId(), true)));
      }
    }
    catch (RoleSearchException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    catch (RoleMemberException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    finally {
      this.entering(method);
    }
    
    return scimGroups;
  }

  @Override
  protected <T extends ScimResource> T replace(final String entityID, T resource)
    throws ScimException {
    final String method = "replace";
    this.entering(method);
    try {
      // Check is opperation is supported before doing anything else
      
      for (OIMScimMapping mapping : OIMScimMapping.values()) {
        Object attrValue = resource.get(mapping.getOimAttribute());
        if (attrValue != null) {
          if (attrValue.equals(OIMScimMapping.ID) || attrValue.equals(OIMScimMapping.DISPLAYNAME)) {
            throw new ScimException(HTTPContext.StatusCode.NOT_IMPLEMENTED, "Only members can be modified");
          }
        }
      }
      
      final Object memberAttrValue = resource.get(OIMScimMapping.MEMBERS.getScimAttribute());
      
      if (memberAttrValue != null) {
        
        final List<String> currentUsers = getUserIDFromOIMUserList(getRoleService().getRoleMembers(entityID, true));
      
        List<String> newUsers     = new ArrayList<String>();
        for (String userLogin : ((Group) resource).getMembersAsListString()) {
          newUsers.add(UserService.build(new HashSet<>(), new HashSet<>()).getUserKey(userLogin));
        }
        
        Set<String> userToRevoke = new HashSet<String>();
        
        for (String user : currentUsers) {
          if (!newUsers.contains(user)) {
            userToRevoke.add(user);
          }
          newUsers.remove(user);
        }
        
        
        if (!userToRevoke.isEmpty())
          getRoleService().revokeRoleGrant(entityID, userToRevoke);
        if (!newUsers.isEmpty())
          getRoleService().grantRole(entityID, new HashSet<>(newUsers));
      }
      
      return (T) lookupRoleID(entityID);
      
    }
    catch (RoleMemberException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    catch (RoleGrantException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, e.getMessage());
    }
    catch(RoleGrantRevokeException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch(ValidationFailedException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.UNIQUENESS, e.getMessage());
    }
    catch (AccessDeniedException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  ScimBundle.string(ScimMessage.USER_NOT_AUTHORIZED));
    }
    finally {
      this.exiting(method);
    }

  }

  @Override
  protected void delete(String entityID)
    throws ScimException {
    final String method = "delete";
    this.entering(method);
    
    throw new ScimException(HTTPContext.StatusCode.NOT_IMPLEMENTED, "Group delete not supported");
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  protected Group lookupRole(final String roleID)
    throws ScimException {

    
    RoleManager roleMng = getRoleService();
    
    Role role = null;
    
    try {
      role = roleMng.getDetails(roleID, null);
    }
    catch (NoSuchRoleException | RoleLookupException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    catch (AccessDeniedException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  e.getMessage());
    }
    
    Group group = null;
    
    try {
      group = buildScimGroup(role, roleMng.getRoleMembers(roleID, true));
    }
    catch (RoleMemberException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    
    return group;
  }
  
  protected Group buildScimGroup(final Role role, final List<User> members)
    throws ScimException {
    
    UserManager usrMng = getUserService();
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Group.class);
    final Group group = new Group(resourceDescriptor);
    
    final Map<String, Object> roleAttribute = role.getAttributes();
    final Date createdDate  = roleAttribute.containsKey("ugp_create") ? (Date) roleAttribute.get("ugp_create") : null;
    final Date modifiedDate = roleAttribute.containsKey("ugp_update") ? (Date) roleAttribute.get("ugp_update") : null;
    try {
      group.add(ScimResource.createSchema(Group.SCHEMAS));
      group.add(ScimResource.createMeta(createdDate, modifiedDate, new URI(RequestContext.getUriInfo().getRequestUri().toString() + "/" + OIMScimContext.ENDPOINT_GROUPS + "/" + role.getEntityId()), "Group"));
      group.setId(String.valueOf(role.getEntityId()));
      group.setDisplayName(role.getName());
      for (User user :  members) {
        try {
          if (usrMng.getDetails(user.getEntityId(), new HashSet<String>(), false) != null)
            group.addMember(user.getLogin());
        }
        catch (NoSuchUserException | UserLookupException | AccessDeniedException e) {
          // Do nothing. Do not display user where is not in the scope of the requester
        }
      }
    }
    catch (URISyntaxException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    
    return group;
  }
  
  protected List<String> getUserIDFromOIMUserList(final List<User> oimUser) {
    List<String> userLoginList = new ArrayList<String>();
    
    for (User user : oimUser) {
      userLoginList.add(user.getEntityId());
    }
    
    return userLoginList;
  }
  
  public enum OIMScimMapping {
    
    //Maping belonging to the User core
    ID("id", RoleManagerConstants.RoleAttributeName.KEY.getId()),
    
    DISPLAYNAME("displayName", RoleManagerConstants.RoleAttributeName.DISPLAY_NAME.getId()),
    MEMBERS("members", "members");
    
    
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

  @Override
  protected List<String> getResourceAttributeList() {
    List<String> resourceAttribute =  new ArrayList<String>();
    
    for (OIMScimMapping mapping : OIMScimMapping.values()) {
      resourceAttribute.add(mapping.getScimAttribute());
    }
    
    return resourceAttribute;
  }
}
