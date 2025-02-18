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

    File        :   SpecialRoleSyncManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    SpecialRoleSyncManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/
package bka.iam.identity.service.synchronization;

import java.util.Map;
import java.util.Set;

import javax.naming.directory.Attributes;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.AttributeMapping;

/**
 ** The <code>SpecialRoleSyncManager</code> synchronize Special Roles in the Roles branch
 ** between sorce and target LDAP Server. Special Roles are handled like Entitlements,
 ** roles must be creted before the synchronization is started.
 ** Special role objects are not created or deleted in the target system. Synchonization update only attributes.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SpecialRoleSyncManager extends RoleGroupSyncManager {


  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SpecialRoleSyncManager</code> LDAP Synchronization Manager
   ** which allows to synchronize special roles (roles are not created only attributes are updated)
   ** between a two different LDAPs.
   ** <br>
   ** Default Constructor
   */
  public SpecialRoleSyncManager(AbstractSchedulerTask scheduler, 
                                String searchBaseSource, String searchBaseTarget, 
                                Set<String> objecclassSource, Set<String> objecclassTarget,
                                AttributeMapping attributeMapping,
                                SyncConfiguration config ) {
    
    super(scheduler, searchBaseSource, searchBaseTarget, objecclassSource, objecclassTarget,attributeMapping,config);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   comparePostProcessing
  /**
   ** SpecialRole synchronize only LDAP attributes.
   ** In case there are extra groups in the source or target directory. Those LDAP objects are removed from compared
   ** data structure.
   ** @param      compared updated LDAP data which needs to be updated in the target sytem
   **/
  @Override
  protected void comparePostProcessing(Map<EntryTag, Map<String, Attributes>> compared){
    final String method  = "proceedComparePostProcessing";
    scheduler.debug(method, SystemMessage.METHOD_ENTRY);
    
    // EntitlementSynchManager will do only modifications
    Map<String,Attributes> addEntries = compared.get(EntryTag.ADD);
    Map<String,Attributes> delEntries = compared.get(EntryTag.DELETE);
    
    if( addEntries!= null && addEntries.size() > 0 ){
      Set<String> dns = addEntries.keySet();
      this.scheduler.warning("SpecialRoleSyncManager: Following objects are missing in the TARGET --> "+dns);  
      compared.remove(EntryTag.ADD);
    }
    
    if( delEntries != null && delEntries.size() > 0 ){
      Set<String> dns = delEntries.keySet();
      this.scheduler.warning("SpecialRoleSyncManager: Following objects are redundant in the TARGET --> "+dns);  
      compared.remove(EntryTag.DELETE);
    }
    scheduler.debug(method, SystemMessage.METHOD_EXIT);
  }
   
}