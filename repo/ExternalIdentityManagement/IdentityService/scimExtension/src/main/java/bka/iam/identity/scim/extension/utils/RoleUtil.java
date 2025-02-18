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

    File        :   RoleUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.s.bernet@oracle.com

    Purpose     :   This file implements the class
                    RoleUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-02-12  SBernet      First release version
*/
package bka.iam.identity.scim.extension.utils;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.resource.Group;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.zero.api.AccountsFacade;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;

import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.RoleDeleteException;
import oracle.iam.identity.exception.RoleGrantException;
import oracle.iam.identity.exception.RoleGrantRevokeException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.RoleSearchException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.OIMInternalClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.spi.entity.Searchable;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
////////////////////////////////////////////////////////////////////////////////
// class PatchUtil
// ~~~~~ ~~~~~~~~~
/**
 ** The PatchUtil class allows SCIM client to patch data.
 ** Supported operations are add, replace, remove.
 ** For more information refer to PATCH (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2">section 3.5.2 of RFC 7644</a>)
 ** 
 ** @author  sylvert.s.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleUtil {
  
  private static OIMClient client           = (OIMClient) new OIMInternalClient();
  
  @EJB(name="AccountsFacade")
  AccountsFacade facade;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Avoid to initiate an <code>PatchUtil</code>, all method are static
   */
  private RoleUtil() {
    super();
  }
  
  public static Long countRole()
    throws ScimException {
    final RoleManager roleMng = client.getService(RoleManager.class);
    
    final SearchCriteria searchcriteria = new SearchCriteria(RoleManagerConstants.RoleAttributeName.KEY.getId(), "*", SearchCriteria.Operator.EQUAL);
    try {
      final List<Role> roles = roleMng.search(searchcriteria, null, null);
      return new Long(roles.size());
    }
    catch (RoleSearchException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
  }
  
  public static List<Group> searchRole(final Integer startIndex, final Integer count, final String sortOrder)
    throws ScimException {
    
    
    final List<Group> groups = new ArrayList<Group>();
    final RoleManager roleMng = client.getService(RoleManager.class);
    
    try {
      SearchCriteria searchcriteria = new SearchCriteria(RoleManagerConstants.RoleAttributeName.KEY.getId(), "*", SearchCriteria.Operator.EQUAL);
      HashMap<String, Object> options = getSearchOptions(startIndex, count, sortOrder);
      List<Role> roles = roleMng.search(searchcriteria, null, options);
      
      for (Role role : roles) {
        groups.add(buildScimGroup(role, roleMng.getRoleMembers(role.getEntityId(), true)));
      }
    }
    catch (RoleSearchException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
     catch (RoleMemberException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    
    return groups;
  }
  
  public static void delete(final String roleID)
    throws ScimException {
    final RoleManager roleMng = client.getService(RoleManager.class);
    try {
      RoleManagerResult oimResult = roleMng.delete(roleID, false);
      if (oimResult != null && !"COMPLETED".equalsIgnoreCase(oimResult.getStatus())) {
        throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, ScimBundle.format(ScimMessage.GROUP_CANNOT_DELETE, roleID));
      }
    }
    catch (NoSuchRoleException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    catch (RoleDeleteException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (ValidationFailedException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, e.getMessage());
    }
  }
  
  public static Group modify(final Group group, final AccountsFacade facade)
    throws ScimException {
    RoleManager roleMng = client.getService(RoleManager.class);
    
    Role role = null;
    
    try {
      role = roleMng.getDetails(group.getId(), null);
      List<String> currentUsers = getUserIDFromOIMUserList(roleMng.getRoleMembers(group.getId(), true));
      
      List<String> newUsers     = new ArrayList<String>();
      for (String userLogin : group.getMembersAsListString()) {
        newUsers.add(facade.getUserKey(userLogin));
      }
      
      Set<String> userToRevoke = new HashSet<String>();
      
      for (String user : currentUsers) {
        if (!newUsers.contains(user)) {
          userToRevoke.add(user);
        }
        newUsers.remove(user);
      }
      
      System.out.println("User to grant: " + userToRevoke );
      System.out.println("User to revoke: " + newUsers);
      
      if (!userToRevoke.isEmpty())
        roleMng.revokeRoleGrant(group.getId(), userToRevoke);
      
      if (!newUsers.isEmpty())
        roleMng.grantRole(group.getId(), new HashSet<>(newUsers));
    }
    catch (NoSuchRoleException e) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getMessage());
    }
    catch (RoleLookupException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch(ValidationFailedException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.UNIQUENESS, e.getMessage());
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
    catch (AccessDeniedException e) {
      throw new ScimException(HTTPContext.StatusCode.UNAUTHORIZED,  ScimBundle.string(ScimMessage.USER_NOT_AUTHORIZED));
    }
    
    return group;
  }
  
  
  
  public static Group lookupRole(final String roleID)
    throws ScimException {

    
    RoleManager roleMng = client.getService(RoleManager.class);
    
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
  
  private static Group buildScimGroup(final Role role, final List<User> members)
    throws ScimException {
    
    UserManager usrMng = client.getService(UserManager.class);
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Group.class);
    final Group group = new Group(resourceDescriptor);
    
    final Map<String, Object> roleAttribute = role.getAttributes();
    final Date createdDate  = roleAttribute.containsKey("ugp_create") ? (Date) roleAttribute.get("ugp_create") : null;
    final Date modifiedDate = roleAttribute.containsKey("ugp_update") ? (Date) roleAttribute.get("ugp_update") : null;
    try {
      group.add(ScimResource.createSchema(Group.SCHEMAS));
      group.add(ScimResource.createMeta(createdDate, modifiedDate, new URI(WLSUtil.getOIMUrl() + OIMScimContext.OIM_EXTENTION_ENDPOINT_SCIM + "/" + OIMScimContext.ENDPOINT_GROUPS + "/" + role.getEntityId()), "Group"));
      group.setId(String.valueOf(role.getEntityId()));
      group.setDisplayName(role.getName());
      for (User user :  members) {
        group.addMember(user.getLogin());
      }
    }
    catch (URISyntaxException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    
    return group;
  }
  
  private static List<String> getUserIDFromOIMUserList(final List<User> oimUser) {
    List<String> userLoginList = new ArrayList<String>();
    
    for (User user : oimUser) {
      userLoginList.add(user.getEntityId());
    }
    
    return userLoginList;
  }
  
  public static HashMap<String, Object> getSearchOptions(final Integer startIndex, final Integer count, final String sortOrder) {
    HashMap<String, Object> searchOption = new HashMap<>();
    
    if (startIndex != null)
    searchOption.put("STARTROW", startIndex);
    
    if (sortOrder != null) {
      if (sortOrder.equals("ascending")) {
        searchOption.put("SORTORDER", Searchable.SortOrder.ASCENDING);
      } else {
        searchOption.put("SORTORDER", Searchable.SortOrder.DESCENDING);
      }
    }
      
    if (startIndex != null) {
      searchOption.put("STARTROW", startIndex);
      if (count != -1L)
      searchOption.put("ENDROW", startIndex + count - 1L); 
    }
      
    return searchOption;
  }
}
