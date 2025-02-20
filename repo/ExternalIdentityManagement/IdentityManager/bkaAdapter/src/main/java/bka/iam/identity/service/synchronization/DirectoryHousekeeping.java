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

    File        :   DirectoryHousekeeping.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    DirectoryHousekeeping.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/
package bka.iam.identity.service.synchronization;


import java.util.HashSet;
import java.util.Set;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.provisioning.Descriptor;
import oracle.iam.identity.foundation.provisioning.DescriptorFactory;
import oracle.iam.identity.foundation.resource.TaskBundle;
import oracle.iam.platform.Platform;

import oracle.mds.core.IsolationLevel;
import oracle.mds.core.MDSSession;
import oracle.mds.core.SessionOptions;
import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;


////////////////////////////////////////////////////////////////////////////////
// class DirectoryHousekeeping
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryHousekeeping</code> job synchronizes three subentries in
 ** two directories. These subentries are:
 ** - Role
 ** - Organization
 ** - Entitlement
 ** If differences have been detected with the provided criteria on these
 ** entries, then they are updated on the target system.
 ** This job also detects deleted entries and updates subentries called
 ** "Intermediate entry" which may contain some of the previously the deleted
 ** entrie.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryHousekeeping extends AbstractSchedulerTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /**
   ** Attribute to advise which named IT Resource should be use for retriving
   ** entries from the source directory.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String IT_RESOURCE_SOURCE            = "IT Resource Source";
 
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve entitlement entries from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ENTITLEMENT_SOURCE = "Search Base Entitlement Source";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve Organization entries from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ORGANIZATION_SOURCE = "Search Base Organization Source";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve role entries from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ROLE_SOURCE         = "Search Base Role Source";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve special role entries from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_SPECIAL_ROLE_SOURCE = "Search Base Special Role Source";
  
  /**
   ** Attribute to advise which named IT Resource should be use to copy source
   ** directory entries.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String IT_RESOURCE_TARGET             = "IT Resource Target";

  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve entitlement entries and update on the target
   ** system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ENTITLEMENT_TARGET  = "Search Base Entitlement Target";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve role entries and update on the target system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ORGANIZATION_TARGET = "Search Base Organization Target";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve role entries update on the target system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_ROLE_TARGET         = "Search Base Role Target";
  
  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve special role entries update on the target system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_SPECIAL_ROLE_TARGET = "Search Base Special Role Target";
  
  /**
   ** Attribute tag which may be defined on this task to specify where the
   ** DN of the intermediate OU.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String SEARCHBASE_INTERMEDIATE_SOURCE = "Search Base Intermediate Source";
  
  /**
   ** Attribute tag which may be defined on this task to specify which kind of
   ** Attribute mapping between source and target Organization Unit objectClass
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String GROUP_DESCRIPTOR               = "Group Descriptor";
  
  /**
   ** Organizational Unit Descriptor tag which must be defined on this task to specify 
   ** Attribute mapping between source and target Organization Unit objectClass
   ** 
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ORGANIZATIONAL_UNIT_DESCRIPTOR = "Organizational Unit Descriptor";
  
  /**
   ** Attribute tag which can be defined on this task to specify the size of a
   ** block read from the working file.
   ** <br>
   ** This attribute is optional and defaults to {@link #BATCH_SIZE_DEFAULT}.
   */
  private static final String BATCH_SIZE                     = "Batch Size";

  /** Default value for {@link #BATCH_SIZE} */
  private static final int    BATCH_SIZE_DEFAULT             = 1000;
  
  /** The category of the logging facility to use */
  private static final String LOGGER_CATEGORY                = "BKA.DIRECTORY.SYNC";
  
  /** The separator used for multivalued attributes */
  final private static String SEPARATOR = "\\|";
  
  /**
   ** The vector with attributes which are valid to define on the task
   ** definition
   */
  protected static final TaskAttribute[] attribute = {
    /** The task attribute IT Resource for the source directory */
    TaskAttribute.build(IT_RESOURCE_SOURCE,             TaskAttribute.MANDATORY)
    /** 
     ** The task attribute that specifies the DN of the entitlements in the
     ** source
     */
  , TaskAttribute.build(SEARCHBASE_ENTITLEMENT_SOURCE,  TaskAttribute.MANDATORY)
    /** 
     ** The task attribute that specifies the DN of the organizations in the
     ** source
     */
  , TaskAttribute.build(SEARCHBASE_ORGANIZATION_SOURCE,   TaskAttribute.MANDATORY)
    /** The task attribute that specifies the DN of the roles in the source */
  , TaskAttribute.build(SEARCHBASE_ROLE_SOURCE,           TaskAttribute.MANDATORY)
    /** The task attribute that specifies the DN of the specialroles in the source 
    ** which are not created, only group membership is maintained */
  , TaskAttribute.build(SEARCHBASE_SPECIAL_ROLE_SOURCE,   TaskAttribute.MANDATORY)
    /** The task attribute IT Resource for the target directory */
  , TaskAttribute.build(IT_RESOURCE_TARGET,               TaskAttribute.MANDATORY)
    /** 
     ** The task attribute that specifies the DN of the entitlements in the
     ** target
     */
  , TaskAttribute.build(SEARCHBASE_ENTITLEMENT_TARGET,    TaskAttribute.MANDATORY)
    /** 
     ** The task attribute that specifies the DN of the organizations in the
     ** target
     */
  , TaskAttribute.build(SEARCHBASE_ORGANIZATION_TARGET,   TaskAttribute.MANDATORY)
    /** The task attribute that specifies the DN of the roles in the target */
  , TaskAttribute.build(SEARCHBASE_ROLE_TARGET,           TaskAttribute.MANDATORY)
    /** The task attribute that specifies the DN of the specialroles in the target 
     ** which are not created, only group membership is maintained */
    , TaskAttribute.build(SEARCHBASE_SPECIAL_ROLE_TARGET, TaskAttribute.MANDATORY)
    /**
     ** The task attribute that specifies the DN of the intermediate OU in the
     ** target
     */
  , TaskAttribute.build(SEARCHBASE_INTERMEDIATE_SOURCE,   SystemConstant.EMPTY)
    /**
     ** The task attribute that specifies the path on MDS to the group descriptor
     */
  , TaskAttribute.build(GROUP_DESCRIPTOR,                 TaskAttribute.MANDATORY)
    /**
     ** The task attribute that specifies the path on MDS to Organizational Unit descriptor
     ** target
     */
  , TaskAttribute.build(ORGANIZATIONAL_UNIT_DESCRIPTOR,   TaskAttribute.MANDATORY)
  
  , TaskAttribute.build(BATCH_SIZE,                       String.valueOf(BATCH_SIZE_DEFAULT))
  };
  
  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // enum entryTag
  // ~~~~ ~~~~~~~~
  /**
   ** This enum store tags for updating entries in the target system
   */
  enum EntryTag {
     /**
     ** Attribute value which may be defined on an entry that must be create on
     ** the LDAP target.
     ** <p>
     ** This operation will add the entry and its attributes at the same
     ** location as defined in the source
     */
    ADD,
    /**
     ** Attribute value which may be defined on a entry that must be updated on
     ** the LDAP target. The reason of this tags could be a change of an
     ** attribute.
     ** <p>
     ** This operation will modify the entry at the same location as defined
     ** in the source
     */
    MODIFY,
    /**
     ** Attribute value which may be defined on a entry that must be deleted on
     ** the LDAP target.
     ** <p>
     ** This operation will delete the same entry defined in the source
     */
    DELETE;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccessPolicyHouseKeeping</code> scheduler
   ** instance that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryHousekeeping() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceNameSource
  /**
   ** Returns the name of the IT ressource used as authoritative data that will
   ** be used to synchronize entries in the directory target.
   **
   ** @return                    the name of the IT ressource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String resourceNameSource() {
    return stringValue(IT_RESOURCE_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseEntitlementSource
  /**
   ** Returns the entitlements search base this task is performing on the source
   ** LDAP server.
   **
   ** @return                    the entitlements search base this task is
   **                            performing on the source LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseEntitlementSource() {
    return stringValue(SEARCHBASE_ENTITLEMENT_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseOrganizationSource
  /**
   ** Returns the Organization search base this task is performing on the source
   ** LDAP server.
   **
   ** @return                    the organization search base this task is
   **                            performing on the source LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseOrganizationSource() {
    return stringValue(SEARCHBASE_ORGANIZATION_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseRoleSource
  /**
   ** Returns the role search base this task is performing on the source
   ** LDAP server.
   **
   ** @return                    the role search base this task is
   **                            performing on the source LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseRoleSource() {
    return stringValue(SEARCHBASE_ROLE_SOURCE);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseSpecialRoleSource
  /**
   ** Returns the role search base this task is performing on the source
   ** LDAP server.
   **
   ** @return                    the special role search base this task is
   **                            performing on the source LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseSpecialRoleSource() {
    return stringValue(SEARCHBASE_SPECIAL_ROLE_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceNameTarget
  /**
   ** Returns the name of the IT resource used as target data that will
   ** be used to synchronize entries with the authoritative directory.
   **
   ** @return                    the name of the IT ressource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String resourceNameTarget() {
    return stringValue(IT_RESOURCE_TARGET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseEntitlementTarget
  /**
   ** Returns the entitlement search base this task is performing on the target
   ** LDAP server.
   **
   ** @return                    the entitlement search base this task is
   **                            performing on the target LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseEntitlementTarget() {
    return stringValue(SEARCHBASE_ENTITLEMENT_TARGET);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseOrganizationTarget
  /**
   ** Returns the organization search base this task is performing on the target
   ** LDAP server.
   **
   ** @return                    the organization search base this task is
   **                            performing on the target LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseOrganizationTarget() {
    return stringValue(SEARCHBASE_ORGANIZATION_TARGET);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseRoleTarget
  /**
   ** Returns the role search base this task is performing on the target
   ** LDAP server.
   **
   ** @return                    the role search base this task is
   **                            performing on the target LDAP server.
   **                            <br>
  **                            Possible object is {@link String}.
   */
  public String searchBaseRoleTarget() {
    return stringValue(SEARCHBASE_ROLE_TARGET);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseSpecialRoleTarget
  /**
   ** Returns the role search base this task is performing on the target
   ** LDAP server.
   **
   ** @return                    the special role search base this task is
   **                            performing on the target LDAP server.
   **                            <br>
  **                            Possible object is {@link String}.
   */
  public String searchBaseSpecialRoleTarget() {
    return stringValue(SEARCHBASE_SPECIAL_ROLE_TARGET);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBaseIntermediateTarget
  /**
   ** Returns the intermediate search base this task is performing on the target
   ** LDAP server.
   **
   ** @return                    the intermediate search base this task is
   **                            performing on the target LDAP server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String searchBaseIntermediateSource() {
    return stringValue(SEARCHBASE_INTERMEDIATE_SOURCE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupDescriptor
  /**
   ** Returns the path to the MDS where is located Group Descriptor file
   **
   ** @return                    path to the Group Descriptor file.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String groupDescriptor() {
    return stringValue(GROUP_DESCRIPTOR);
  }
  
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationalUnitDescriptor
  /**
   ** Returns the path to the MDS where is located OrganizationalUnit Descriptor file
   **
   ** @return                    OrganizationalUnit Descriptor file.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String organizationalUnitDescriptor() {
    return stringValue(ORGANIZATIONAL_UNIT_DESCRIPTOR);
  }
  

  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   batchSize
  /**
   ** Returns the batch size of for the  synchronization source and target.
   **
   ** @return                    the batch size for the reconciliation source.
   */
  public int batchSize() {
    final int batchSize = integerValue(BATCH_SIZE);
    // Fixed Defect DE-000126
    // Batch Size is an optional argument but if its isn't defined the job
    // loops infinite hence we need to fallback to a default value
    return (batchSize == Integer.MIN_VALUE) ? BATCH_SIZE_DEFAULT : batchSize;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   **                            <br>
   **                            Possible object is array of
   **                            {@link TaskAttribute}.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution
  /**
   ** The entry point of the scheduled task to perform.
   ** Retrieve entries from the source then compare entries on the target.
   ** The following change are made after the comparison:
   ** <ul>
   **   <li>Add:    If a new entry has been detected, this entry is copied on the
   **               target</li>
   **   <li>Modify: If an attribute has been modify, then this change is copied
   **               on the target</li>
   **   <li>Delete: If an attribut has been deleted, this entry is also deleted
   **               on the target</li>
   ** </ul>
   ** 
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {
    
    final String method  = "onExecution";
    debug(method, "SourceGroupObjectClass");
    debug(method, SystemMessage.METHOD_ENTRY);
    System.out.println("DirectoryHousekeeping version 1.0.0");
    // Check if passed arguments exist like IT Resource exists etc.
    try {
      
      final DirectoryResource   sourceResource  = new DirectoryResource(this, resourceNameSource());
      final DirectoryResource   targetResource  = new DirectoryResource(this, resourceNameTarget());
      
      
      final DirectoryConnector  sourceDirectory = new DirectoryConnector(this, sourceResource);
      final DirectoryConnector  targetDirectory = new DirectoryConnector(this, targetResource);
      
      
      debug(method, "Source and Target GroupObjectClass --> " + sourceDirectory.feature().groupObjectClass() +", "+targetDirectory.feature().groupObjectClass());
      debug(method, "Source and Target GroupObjectPrefix --> " + sourceDirectory.feature().groupObjectPrefix() +", "+targetDirectory.feature().groupObjectPrefix());
      
      debug(method, "Source and Target OrganizationalUnitObjectClass --> " + sourceDirectory.feature().organizationalUnitObjectClass() +", "+targetDirectory.feature().organizationalUnitObjectClass());
      debug(method, "Source and Target OrganizationalUnitObjectPrefix --> " + sourceDirectory.feature().organizationalUnitObjectPrefix() +", "+targetDirectory.feature().organizationalUnitObjectPrefix());


      Descriptor grpDescriptor = parseDecriptor(groupDescriptor());
      Descriptor orgDescriptor = parseDecriptor(organizationalUnitDescriptor());
      
      debug(method, "Group Descriptor: "+ grpDescriptor.attributeMapping());
      debug(method, "OU Descriptor: "   + orgDescriptor.attributeMapping());
      
      final Set<String> sourceGroupObjectClasses  = new HashSet<String>();
      sourceGroupObjectClasses.add(sourceDirectory.feature().groupObjectClass());
      
      final Set<String> targetGroupObjectClasses  = new HashSet<String>();
      targetGroupObjectClasses.add(targetDirectory.feature().groupObjectClass());
      
      Set<String> sourceOrgObjectClasses = new HashSet<String>();
      sourceOrgObjectClasses.add(sourceDirectory.feature().organizationalUnitObjectClass());
      
      Set<String> targetOrgObjectClasses = new HashSet<String>();
      targetOrgObjectClasses.add(targetDirectory.feature().organizationalUnitObjectClass());
      
      SyncConfiguration config = new SyncConfiguration( sourceDirectory, targetDirectory, 
                                                searchBaseIntermediateSource(),
                                                searchBaseOrganizationSource(), searchBaseOrganizationTarget(),
                                                searchBaseEntitlementSource(), searchBaseEntitlementTarget(),
                                                searchBaseRoleSource(), searchBaseRoleTarget(),
                                                searchBaseSpecialRoleSource(),searchBaseSpecialRoleTarget(),
                                                batchSize());
      
      // In the source system are organization OUs modeled as groups
      OrganizationStructureSyncManager orgStructure = new OrganizationStructureSyncManager(this
                                                                                           , searchBaseOrganizationSource(), searchBaseOrganizationTarget()
                                                                                           , sourceGroupObjectClasses,targetOrgObjectClasses
                                                                                           , orgDescriptor.attributeMapping()
                                                                                           , config);
      orgStructure.synch();
      
      
      OrganizationGroupSyncManager orgGroups = new OrganizationGroupSyncManager(this
                                                                                , searchBaseOrganizationSource(), searchBaseOrganizationTarget()
                                                                                , sourceGroupObjectClasses,targetGroupObjectClasses
                                                                                , grpDescriptor.attributeMapping()
                                                                                ,config);
      orgGroups.synch();

   

      RoleStructureSyncManager roleStructure = new RoleStructureSyncManager(this
                                                                            , searchBaseRoleSource(), searchBaseRoleTarget()
                                                                            , sourceOrgObjectClasses,targetOrgObjectClasses
                                                                            , orgDescriptor.attributeMapping()
                                                                            , config );
      roleStructure.synch();
     
      
      RoleGroupSyncManager roleGroup = new RoleGroupSyncManager(this
                                                                , searchBaseRoleSource(), searchBaseRoleTarget()
                                                                , sourceGroupObjectClasses,targetGroupObjectClasses
                                                                , grpDescriptor.attributeMapping()
                                                                , config );
      roleGroup.synch();
      

      SpecialRoleSyncManager specialRoleGroup = new SpecialRoleSyncManager(this
                                                                           , searchBaseSpecialRoleSource(), searchBaseSpecialRoleTarget()
                                                                           , sourceGroupObjectClasses,targetGroupObjectClasses
                                                                           , grpDescriptor.attributeMapping()
                                                                           , config );
      specialRoleGroup.synch();
        
      EntitlementSyncManager entitlementGroup = new EntitlementSyncManager(this
                                                                           , searchBaseEntitlementSource(), searchBaseEntitlementTarget()
                                                                           , sourceGroupObjectClasses,targetGroupObjectClasses
                                                                           , grpDescriptor.attributeMapping()
                                                                           , config);
      entitlementGroup.synch();
      
      StringBuffer report = new StringBuffer();
      report.append("\n------------------------------------------------------\n");
      report.append("LDAP Synch Report\n");
      report.append("------------------------------------------------------\n\n");
      report.append(orgStructure.synchReport());
      report.append(orgGroups.synchReport());
      report.append(roleStructure.synchReport());
      report.append(roleGroup.synchReport());
      report.append(specialRoleGroup.synchReport());
      report.append(entitlementGroup.synchReport());
      info(report.toString());

    }
    finally {
      debug(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseDecriptor
  /**
   ** Factory method to create a {@link Descriptor} from a path.
   **
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected Descriptor parseDecriptor(final String path)
    throws TaskException {

    final String method = "parseDecriptor";
    trace(method, SystemMessage.METHOD_ENTRY);
    // create the task descriptor that provides the attribute mapping and
    // transformations to be applied on the mapped attributes
    Descriptor descriptor = new Descriptor(this);

    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
    
      final SessionOptions option = new SessionOptions(IsolationLevel.READ_COMMITTED, null, null);
      MDSSession  session = Platform.getMDSInstance().createSession(option, null);
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      DescriptorFactory.configure(descriptor, document);
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
      return descriptor;
    }
  }
}