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

    File        :   EntitlementSyncManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    EntitlementSyncManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/
package bka.iam.identity.service.synchronization;

import java.util.Map;
import java.util.Set;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.AttributeMapping;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementSyncManager
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>EntitlementSyncManager</code> synchronize entitlements between sorce and target LDAP Server.
 ** Entitlemnts LDAP Groups needs to be cread in the target system manualy. 
 ** In case the LDAP Group is missing in the target LDAP server groups are not created. Also in case there 
 ** are LDPA groups in the target LDAP server which are not presented in the source LDAP server,
 ** LDAP groups are not removed from the target LDAP server. 
 ** Entitlement synchronization modify only <code>member</code> attribute, rest of the attributes are not 
 ** added/removed/modified.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EntitlementSyncManager extends AbstractSyncManager {
  
  private String sourceObjectMemberAttribute;
  private String targetObjectMemberAttribute;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>EntitlementSyncManager</code> LDAP Synchronization Manager
   ** which allows to synchronize entitlements between a two different LDAPs.
   ** <br>
   ** Default Constructor
   */
  public EntitlementSyncManager(AbstractSchedulerTask scheduler, 
                                String searchBaseSource, String searchBaseTarget, 
                                Set<String> objecclassSource, Set<String> objecclassTarget,
                                AttributeMapping attributeMapping,
                                SyncConfiguration config ) {
    
    super(scheduler, searchBaseSource, searchBaseTarget, objecclassSource, objecclassTarget,attributeMapping,config);
    this.sourceObjectMemberAttribute = config.getSourceDirectory().feature().groupObjectMemberAttribute();  // member
    this.targetObjectMemberAttribute = config.getTargetDirectory().feature().groupObjectMemberAttribute();  // member
  }
  
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   areAttributesModified
  /**
   ** 1. Exclude source non group members DN from transformation process <BR>
   ** 2. Tranform source group members DN to the target DN format<BR>
   ** 3. Copy all target non groups DN (like user DN) to target member attribute
   ** @param sourceAttributes Source LDAP attributes
   ** @param targetAttributes Target LDAP attributes
   ** @return Source member attributes are converted to the target attributes
   */
  @Override
  protected Attributes attributeTransformation(Attributes sourceAttributes, Attributes targetAttributes)  {
    final String method  = "attributeTransformation";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    scheduler.debug(method,"sourceAttributes--> "+sourceAttributes);
    scheduler.debug(method,"targetAttributes --> "+targetAttributes);
    
    Attributes transformedAttibutes = sourceAttributes;
  
    if(sourceAttributes != null && targetAttributes != null){
      // All attributes are copied from target attributes. We manage only member attribute
      transformedAttibutes = (Attributes) targetAttributes.clone();
      
      Attribute sourceMembers = sourceAttributes.get(sourceObjectMemberAttribute);
      Attribute targetMembers = targetAttributes.get(targetObjectMemberAttribute);
      
      if(sourceMembers == null){
        Attribute member = new BasicAttribute(targetObjectMemberAttribute);
        // Add all target members which are not ending with  TargetRolesSearchBase
        addExcluded(member, targetMembers, config.getTargetRolesSearchBase());
        transformedAttibutes.put(member);
      }
      else{
        Attribute member = new BasicAttribute(targetObjectMemberAttribute);
        // Filter source members only for roles DN, Users members from source are exluded
        addIncluded(member, sourceMembers,config.getSourceRolesSearchBase());
        Attribute modifiedMembers = replaceStartDN(member, config.getSourceRolesSearchBase(), config.getTargetRolesSearchBase() );
       
        // Add all target members which are not ending with  TargetRolesSearchBase
        // This assure that users provisioned to this entitlement is not removed
        addExcluded(modifiedMembers, targetMembers,config.getTargetRolesSearchBase());
        transformedAttibutes.put(modifiedMembers);
      }
    }
    scheduler.info("transformedAttibute --> "+transformedAttibutes);
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return transformedAttibutes;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   areAttributesModified
  /**
   ** Entity is considered to modified, only in case the attribute member is differnt.
   ** Rest of the attributes are ignored
   ** @param sourceAttributes Source LDAP attributes
   ** @param targetAttributes Target LDAP attributes
   ** @return true in case LDAP object needs to be modified, otherwise return false
   */
  @Override
  protected boolean areAttributesModified(Attributes sourceAttributes, Attributes targetAttributes){
    final String method  = "areAttributesModified";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    boolean modified = true;
    
    // Check only member attribute, rest of the attributes are ignored
    if(sourceAttributes != null || targetAttributes != null){

      // Compare only member attribute
      Attribute sourceMember = sourceAttributes.get(sourceObjectMemberAttribute);
      Attribute targetMember = targetAttributes.get(targetObjectMemberAttribute);
      if(sourceMember == null && targetMember == null){
        modified = false;
      }
      else if(sourceMember == null && targetMember != null){
        modified = true;
      }
      else if(sourceMember != null && targetMember == null){
        modified = true;
      }
      else{
        scheduler.debug(method,"sourceMember--> "+ sourceMember);
        scheduler.debug(method,"targetMember--> "+ targetMember);
        modified =  !targetMember.equals(sourceMember);
      }
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
    return modified;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   comparePostProcessing
  /**
   ** Entitlements synchronize only LDAP member attribute. 
   ** In case there are extra groups in the source or target directory. Those LDAP objects are ignored.
   ** @param compared LDAP data which needs to be updated in the target sytem
   **/
  @Override
  protected void comparePostProcessing(Map<EntryTag, Map<String, Attributes>> compared){
    final String method  = "proceedComparePostProcessing";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    // EntitlementSynchManager will do only modifications
    Map<String,Attributes> addEntries = compared.get(EntryTag.ADD);
    Map<String,Attributes> delEntries = compared.get(EntryTag.DELETE);
    
    if( addEntries != null && addEntries.size() > 0 ){
      Set<String> dns = addEntries.keySet();
      this.scheduler.warning("EntitlementSyncManager: Following objects are missing in the TARGET --> "+dns);  
      compared.remove(EntryTag.ADD);
    }
    
    if( delEntries != null && delEntries.size() > 0 ){
      Set<String> dns = delEntries.keySet();
      this.scheduler.warning("EntitlementSyncManager: Following objects are redundant in the TARGET --> "+dns);  
      compared.remove(EntryTag.DELETE);
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
  }
  
}
