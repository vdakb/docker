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

    File        :   OrganizationGroupSyncManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    OrganizationGroupSyncManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/
package bka.iam.identity.service.synchronization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.AttributeMapping;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationGroupSyncManager
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrganizationGroupSyncManager</code> synchronize LDAP Groups in the Organization branch 
 ** between sorce and target LDAP Server. In the source LDAP server are groups under organizational branch 
 ** modeled as members of Intermediate group. When a new intermediate member is added/removed from organization group.
 ** A new group is added/removed in the target the organizational branch.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OrganizationGroupSyncManager extends AbstractSyncManager {
  
  private String groupObjectMemberAttribute = "member";
  private String sourceGroupPrefix          = "cn";
  private String targetOUPrefix             = "ou";
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>OrganizationGroupSyncManager</code> LDAP Synchronization Manager
   ** which allows to synchronize organizational groups between a two different LDAPs.
   ** <br>
   ** Default Constructor
   */
  public OrganizationGroupSyncManager(AbstractSchedulerTask scheduler, 
                                String searchBaseSource, String searchBaseTarget, 
                                Set<String> objecclassSource, Set<String> objecclassTarget,
                                AttributeMapping attributeMapping,
                                SyncConfiguration config ) {
    
    super(scheduler, searchBaseSource, searchBaseTarget, objecclassSource, objecclassTarget,attributeMapping,config);
    
    groupObjectMemberAttribute = config.getSourceDirectory().feature().groupObjectMemberAttribute();     // member
    sourceGroupPrefix          = config.getSourceDirectory().feature().groupObjectPrefix();              // cn
    targetOUPrefix             = config.getTargetDirectory().feature().organizationalUnitObjectPrefix(); // OU
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTargetAttributesNames
  /**
   ** Remove LDAP attribute member from target attributes.
   ** LDAP attribute member is not synchronized with this OrganizationGroupSyncManager
   ** @param attributeMapping
   ** @return
   */
  @Override
  protected Set<String> getTargetAttributesNames(AttributeMapping attributeMapping){
    final String method  = "getTargetAttributesNames";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    Set<String> targetAttibuteNames = super.getTargetAttributesNames(attributeMapping);
    if(targetAttibuteNames != null){
      targetAttibuteNames.remove("member");
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return targetAttibuteNames;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSourceEntriesPostProcessing
  /**
   ** All intermediated group members are converted to the group under organizational container.
   ** For example:
   ** Source group
   ** DN: cn=DS110000,ou=TN-Orgs,ou=P20,dc=example,dc=org
   ** member: cn=DS110000_Sachbearbeiter,ou=Intermediate,ou=Anwendung3,ou=P20-Anwendungen,ou=P20,dc=example,dc=org
   ** member: cn=DS110000_Sachbearbeiter-QS,ou=Intermediate,ou=Anwendung3,ou=P20-Anwendungen,ou=P20,dc=example,dc=org
   ** member: cn=DS110000_SB-Leiter,ou=Intermediate,ou=Anwendung3,ou=P20-Anwendungen,ou=P20,dc=example,dc=org
   ** 
   ** Is converted to three groups:
   ** DN: cn=DS110000_Sachbearbeiter,ou=DS110000,ou=Poliks Test,ou=TN1-PLX,dc=example,dc=org
   ** cn: DS110000_Sachbearbeiter
   ** 
   ** DN: cn=DS110000_Sachbearbeiter-QS,ou=DS110000,ou=Poliks Test,ou=TN1-PLX,dc=example,dc=org
   ** cn: DS110000_Sachbearbeiter-QS
   ** 
   ** DN: cn=DS110000_SB-Leiter,ou=DS110000,ou=Poliks Test,ou=TN1-PLX,dc=example,dc=org
   ** cn: DS110000_SB-Leiter
   ** 
   ** @param sourceEntries
   ** @return
   */
  @Override
  protected Map<String, Attributes> searchSourceEntriesPostProcessing(Map<String, Attributes> sourceEntries) {
    String method = "searchSourceEntriesPostProcessing";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
  
    Map<String, Attributes> transformedEntries = new HashMap<String, Attributes>();
    for(Map.Entry<String,Attributes> entry :sourceEntries.entrySet()){
      String             dn = entry.getKey();
      Attributes attributes = entry.getValue();
      try{
        if(dn != null && dn.startsWith(sourceGroupPrefix+"=")){     
          // Convert every intermediate group membership to groups under orgranization OUs
          Attribute members = attributes.get(groupObjectMemberAttribute);
          for(int i=0; i < members.size(); i++){
            String member = (String)members.get(i);
            // Convert intermedite member DN to group under organization container 
            if(startsWith(member, config.getSourceIntermediatesSearchBase())){
              String groupDN = removeStartDN(member, config.getSourceIntermediatesSearchBase());
              LdapName group = new LdapName(groupDN);  
              String groupCN = (String)group.getRdn(group.size()-1).getValue();
              Attributes groupAttrs = new BasicAttributes(sourceGroupPrefix,groupCN,false);
              // Change RND from cn to ou for example change from cn=DS110000 to ou=DS110000
              String ouDN= dn.replaceFirst(sourceGroupPrefix, targetOUPrefix);
              // Create a group DN like CN=DS110000_Sachbearbeiter,OU=DS110000 where first part is stored 
              // in group variable and second part of the DN in the ouDN value
              transformedEntries.put(group.add(0,ouDN).toString(), groupAttrs);              
            }
          }   
        }
      }
      catch(NamingException e){
        this.scheduler.fatal(method, e);    
      } 
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return transformedEntries;
  }
}
