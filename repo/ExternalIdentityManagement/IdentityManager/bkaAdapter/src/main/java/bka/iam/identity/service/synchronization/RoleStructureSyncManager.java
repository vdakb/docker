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

    File        :   RoleStructureSyncManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    RoleStructureSyncManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/
package bka.iam.identity.service.synchronization;

import java.util.HashSet;
import java.util.Set;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.AttributeMapping;

////////////////////////////////////////////////////////////////////////////////
// class RoleStructureSyncManager
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>RoleStructureSyncManager</code> synchronize LDAP OUs in the Roles branch 
 ** between sorce and target LDAP Server. In the source LDAP server special role branch is ignored 
 ** (Special Roles are handled by SpecialRoleSynchManager). 
 ** In the target LDAP Server entitlements and special roles branch is ignored.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleStructureSyncManager extends AbstractSyncManager {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>RoleStructureSyncManager</code> LDAP Synchronization Manager
   ** which allows to synchronize organizational units between a two different LDAPs.
   ** <br>
   ** Default Constructor
   */
  public RoleStructureSyncManager(AbstractSchedulerTask scheduler, 
                                String searchBaseSource, String searchBaseTarget, 
                                Set<String> objecclassSource, Set<String> objecclassTarget,
                                AttributeMapping attributeMapping,
                                SyncConfiguration config ) {
    
    super(scheduler, searchBaseSource, searchBaseTarget, objecclassSource, objecclassTarget,attributeMapping,config);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeSourceDN
  /**
   ** Exlude Special Role from the source search result
   ** @return     Special Role DN
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
   ** @return     Special Role and Entitlement DN
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

}
