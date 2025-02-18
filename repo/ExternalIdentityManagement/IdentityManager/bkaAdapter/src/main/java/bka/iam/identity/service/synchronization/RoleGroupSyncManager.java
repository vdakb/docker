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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   RoleGroupSyncManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    RoleGroupSyncManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/

package bka.iam.identity.service.synchronization;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class RoleGroupSyncManager
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>RoleGroupSyncManager</code> synchronize LDAP groups in the Roles branch 
 ** between sorce and target LDAP Server. In the source LDAP server special role branch is ignored 
 ** (Special Roles are handled by SpecialRoleSynchManager). 
 ** In the target LDAP Server entitlements and special roles branch is ignored.
 ** LDAP Groups on the source and target synchronize only group membership. In case there are users DNs assigned in the 
 ** source or target group, those members values are not moved from source to target or deleted on the target LDAP server.
 ** 
 ** Source LDAP attribute member contains different values:
 ** <ul>
 **  <li>User DN - in this case memer value is ignored</li>
 **  <li>Role DN - in this case DN is converted to target format and synchronized</li>
 **  <li>Intermediate DN - in this case DN is converted to target format based on group member on organization group and synchronized</li>
 ** </ul>
 ** 
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleGroupSyncManager extends AbstractSyncManager {
  
  private Map<String, Attributes>  organizationSourceGroups;
  private Map<String, Attributes>  organizationTargetGroups;
  
  private String                   sourceGroupObjectPrefix = "cn";
  private String                   sourceOUObjectPrefix = "ou";
  
  private String                   sourceGroupObjectMemberAttribute = "member";
  private String                   targetGroupObjectMemberAttribute = "member";
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>RoleGroupSyncManager</code> LDAP Synchronization Manager
   ** which allows to synchronize roles between a two different LDAPs.
   ** <br>
   ** Default Constructor
   */
  public RoleGroupSyncManager(AbstractSchedulerTask scheduler, 
                                String searchBaseSource, String searchBaseTarget, 
                                Set<String> objecclassSource, Set<String> objecclassTarget,
                                AttributeMapping attributeMapping,
                                SyncConfiguration config ) {
    
    super(scheduler, searchBaseSource, searchBaseTarget, objecclassSource, objecclassTarget,attributeMapping,config);
    
    sourceGroupObjectMemberAttribute = config.getSourceDirectory().feature().groupObjectMemberAttribute(); // member
    targetGroupObjectMemberAttribute = config.getTargetDirectory().feature().groupObjectMemberAttribute(); // member
    sourceGroupObjectPrefix          = config.getSourceDirectory().feature().groupObjectPrefix();              //cn
    sourceOUObjectPrefix             = config.getSourceDirectory().feature().organizationalUnitObjectPrefix(); // ou
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeSourceDN
  /**
   ** Exlude Special Role from the source search result
   ** @return Special Role DN
   */
  @Override
  protected Set<String> excludeSourceDN(){ 
    Set<String> excludeObjectsSource = new HashSet<String>();
    String specialRoleDN = removeStartDN(config.getSourceSpecialRolesSearchBase(), config.getSourceRolesSearchBase());
    if(specialRoleDN != null){
      excludeObjectsSource.add(specialRoleDN);
    }
    return excludeObjectsSource;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeTargetDN
  /**
   ** Exlude Special Role and Entitlement from the target search result
   ** @return Special Role and Entitlement DN
   */
  @Override
  protected Set<String> excludeTargetDN(){ 
    Set<String> excludeObjectsTarget = new HashSet<String>();
    
    
    String specialRoleRelativeDN = removeStartDN(config.getTargetSpecialRolesSearchBase(), config.getTargetRolesSearchBase());
    if(specialRoleRelativeDN != null){
      excludeObjectsTarget.add(specialRoleRelativeDN);  
    }
    
    String entitlementsRelativeDN = removeStartDN(config.getTargetEntitlementsSearchBase(), config.getTargetRolesSearchBase());
    if(entitlementsRelativeDN != null){
      excludeObjectsTarget.add(entitlementsRelativeDN);  
    }
    
    return excludeObjectsTarget;
  }
  
  

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeSynch
  /**
   ** Organization Groups are retrieved from the Source LDAP.
   ** Organization Groups are retrieved from the Target LDAP.
   ** Both this data are used in the member attribute calculation in the method attributeTransformation
   ** @throws TaskException
   */
  @Override
  protected void beforeSynch() throws TaskException {
    final String method  = "proceedComparePreProcessing";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    // This data is used to calculated intermidiates mapping
    organizationSourceGroups = searchEntries(config.getSourceDirectory(),
                                        config.getSourceOrganizationsSearchBase(),
                                        getObjecclassSource(),
                                        null, 
                                        getSourceAttributesNames(this.attributeMapping),
                                        null);
    
    scheduler.debug(method,"organizationSourceGroups --> "+organizationSourceGroups);
    
    // This data is used to check if the member is group  under the organization branch
    organizationTargetGroups = searchEntries(config.getTargetDirectory(),
                                        config.getTargetOrganizationsSearchBase(),
                                        getObjecclassTarget(),
                                        null, 
                                        null,
                                        null);
    
    scheduler.debug(method,"organizationTargetGroups --> "+organizationTargetGroups);
    
    
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeTransformation
  /**
   ** In this transformation is tranformed only member attribute, rest of the attributes are not touched.
   ** Source member attribute can be:
   ** <ul>
   **  <li>User DN - in this case memer value is ignored</li>
   **  <li>Role DN - in this case DN is converted to target format and synchronized</li>
   **  <li>Intermediate DN - in this case DN is converted to target format based on group member on organization group and synchronized</li>
   ** </ul>
   ** Target role member are synchronized from source LDAP, but users DN are not touched
   ** @param sourceAttributes Source Group LDAP attributes
   ** @param targetAttributes Target Group LDAP attributes
   ** @return modified LDAP attributes based on the source member attributes
   */
  @Override
  protected Attributes attributeTransformation(Attributes sourceAttributes, Attributes targetAttributes)  {
    final String method  = "attributeTransformation";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);

    scheduler.debug(method,"sourceAttributes --> "+sourceAttributes);
    scheduler.debug(method,"targetAttributes --> "+targetAttributes);
    
    // RoleSynchManager modify only members attribute
    if(sourceAttributes != null){
      Attribute sourceMembers = sourceAttributes.get(sourceGroupObjectMemberAttribute);  
      Attribute targetMembers = null;
      if(targetAttributes != null){
        targetMembers = targetAttributes.get(targetGroupObjectMemberAttribute);  
      }
      
      Attribute modifiedMembers = new BasicAttribute(targetGroupObjectMemberAttribute); 
      if(sourceMembers != null){        
        // Search on organization
        for(int i=0; i< sourceMembers.size(); i++){
          String member;
          try {
            member = (String) sourceMembers.get(i);
            if(member != null){
              scheduler.debug(method,"transforming member value --> " + member);
              if(member.endsWith(config.getSourceIntermediatesSearchBase())){
                // Member is from Intermediate container
                // Calculater group membmership based on the organizaton membershiop of the member value
                Set<String> newMembers = calculateGroupMember(member,organizationSourceGroups);
                for(String newMember: newMembers){
                  modifiedMembers.add(applyObjectPrefix(newMember));
                }
              }
              else if(member.endsWith(config.getSourceRolesSearchBase())){
                // Member is from Roles container, change source role container to target role container
                String newMember = member.replace(config.getSourceRolesSearchBase(), config.getTargetRolesSearchBase());
                modifiedMembers.add(applyObjectPrefix(newMember));
              }
            }
          } catch (NamingException e) {
            scheduler.fatal(method, e);
          }
        }
        sourceAttributes.put(modifiedMembers);
      }
      // Copy all member values where target member is not group. Only group members are maintain by LDAP synch
      // LDAP users must not be deleted from member attribute in the target system. LDAP users are maintained by OIM
      if(targetMembers != null){        
        // Loop over all members attributes
        for(int i=0; i< targetMembers.size(); i++){
          String member;
          try {
            member = (String) targetMembers.get(i);
            if(member != null && !startsWith(member,config.getTargetRolesSearchBase()) && !isGroup(member)){
              modifiedMembers.add(member);
            }
          } catch (NamingException e) {
            scheduler.fatal(method, e);
          }
        }
        sourceAttributes.put(modifiedMembers);
      }
    }
    
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return sourceAttributes;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   calculateGroupMember
  /**
   ** Calculate group member based on the group membership on the organization group and intermediate membership
   ** @param  member Group DN membership on Organization group
   ** @param  orgEntries List of the organizations groups
   ** @return   List of the LDAP DNs where a group shold be member
   ** @throws   NamingException in case provided DN is not valid LDAP DN
   */
  private Set<String> calculateGroupMember(String member, Map<String, Attributes>  orgEntries) throws NamingException {
    final String method  = "calculateGroupMember";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    scheduler.debug(method, "member --> "+member+", orgEntries --> "+orgEntries);
    Set<String> members = new HashSet<String>();
    
    if(member != null && orgEntries != null){
      // Loop over all organizationEntries and search for member
      for(Map.Entry<String,Attributes> org :orgEntries.entrySet()){
        String orgName       = org.getKey().replace(sourceGroupObjectPrefix+"=", "");
        Attributes orgAttributes = org.getValue();
        if(orgAttributes != null && orgAttributes.get(sourceGroupObjectMemberAttribute) != null){
          Attribute orgMembers = orgAttributes.get(sourceGroupObjectMemberAttribute);
          // Loop over all members
          for(int i=0; i<orgMembers.size(); i++){
            String orgMember = (String)orgMembers.get(i);
            scheduler.debug(method, "Org member: "+orgMember);
            if(member.equals(orgMember)){
              scheduler.debug(method, "Intermediate member found: "+member);
              // Create a new group membership based on the Org Name and Intermediate membership
              String newGroupMember = (orgMember.replace(config.getSourceIntermediatesSearchBase(), "")) + sourceOUObjectPrefix+"="+orgName+","+ config.getTargetOrganizationsSearchBase();
              scheduler.debug(method,"newGroupMember --> " + newGroupMember);
              members.add(newGroupMember);
            }
          }
        }
      }
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return members;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   isGroup
  /**
   ** Check if the input DN is a group DN from the organization branch
   ** @param member LDAP DN
   ** @return Return true in case the input string is group in organization branch, otherwise return false
   */
  private boolean isGroup(String member) {
    boolean isGroup = false;
    if(member != null && startsWith(member,config.getTargetOrganizationsSearchBase())){
      for(Map.Entry<String,Attributes> groups :organizationTargetGroups.entrySet()){
        String groupDN =groups.getKey() + "," + config.getTargetOrganizationsSearchBase();
        if(member.equals(groupDN)){
          isGroup = true;
          break;
        }
      }
    }
    return isGroup;
  }
}
