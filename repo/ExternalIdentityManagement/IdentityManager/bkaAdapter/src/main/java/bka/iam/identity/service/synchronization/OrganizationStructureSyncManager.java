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

    File        :   OrganizationStructureSyncManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    OrganizationStructureSyncManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/
package bka.iam.identity.service.synchronization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.AttributeMapping;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationStructureSyncManager
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrganizationStructureSyncManager</code> synchronize LDAP OUs in the Organization branch 
 ** between sorce and target LDAP Server. In the source LDAP server are OUs under organizational branch 
 ** modeled as LDAP Groups. When a new LDAP Groups is added/removed from the source Organization branch.
 ** A new OU is added/removed in the target the organizational branch.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OrganizationStructureSyncManager extends AbstractSyncManager {
  
  private String sourceGroupObjectPrefix = "cn";
  private String sourceGroupPrefix       = "cn";
  private String sourceOUPrefix          = "ou"; // ou
  private String targetOUPrefix          = "ou"; // OU
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>OrganizationStructureSyncManager</code> LDAP Synchronization Manager
   ** which allows to synchronize organizational units between a two different LDAPs.
   ** <br>
   ** Default Constructor
   */
  public OrganizationStructureSyncManager(AbstractSchedulerTask scheduler, 
                                String searchBaseSource, String searchBaseTarget, 
                                Set<String> objecclassSource, Set<String> objecclassTarget,
                                AttributeMapping attributeMapping,
                                SyncConfiguration config ) {
    
    super(scheduler, searchBaseSource, searchBaseTarget, objecclassSource, objecclassTarget,attributeMapping,config);
    
    sourceGroupObjectPrefix = config.getSourceDirectory().feature().groupObjectPrefix();        // cn
    sourceGroupPrefix       = config.getSourceDirectory().feature().groupObjectPrefix();        // cn
    
    sourceOUPrefix = config.getSourceDirectory().feature().organizationalUnitObjectPrefix(); // ou
    targetOUPrefix = config.getTargetDirectory().feature().organizationalUnitObjectPrefix(); // OU
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSourceAttributesNames
  /**
   ** Add group object prefix (cn) to the list of the source attributes. Organization unit is converted from groups,
   ** for this reason group object prefix needs to be addded to the list of the returned attributes.
   ** @param attributeMapping
   ** @return
   */
  @Override
  protected Set<String> getSourceAttributesNames(AttributeMapping attributeMapping){
    Set<String> srcAttributes = super.getSourceAttributesNames(attributeMapping);
    srcAttributes.add(sourceGroupObjectPrefix);  //cn
    return srcAttributes; 
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSourceEntriesPostProcessing
  /**
   ** OrganizationalUnits (OU) are created from from groups. In the source LDAP are defined groups under
   ** organizational container. Those groups are converted to OUs in the target LDAP server. <BR>
   ** For example:<BR>
   **    
   ** Source group:<BR>
   **   cn=DS110000,ou=TN-Orgs,ou=P20,dc=example,dc=org<BR>
   ** Target organizational unit:<BR>
   **   OU=DS110000,OU=Poliks Test,OU=TN1-PLX,dc=example,dc=org<BR>
   ** @param sourceEntries Result from source search
   ** @return Modified source search, groups are converted to organizational units.
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
        if(dn != null && dn.startsWith(sourceGroupPrefix)){
          // Convert a group to the OrganizationalUnit
          String cn = (String)attributes.get(sourceGroupPrefix).get();
          Attributes attr = new BasicAttributes(sourceOUPrefix,cn,false);
          transformedEntries.put(dn.replaceFirst(sourceGroupPrefix, targetOUPrefix), attr);
        }
        // This part will allow to creat a deep organization structure
        else if(dn != null && dn.startsWith(sourceOUPrefix)){     
          transformedEntries.put(dn,attributes);
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