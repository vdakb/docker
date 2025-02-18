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

    File        :   SyncConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    SyncConfiguration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-06-02  TSebo     First release version
*/
package bka.iam.identity.service.synchronization;

import oracle.iam.identity.foundation.ldap.DirectoryConnector;

/**
 ** The <code>SyncConfiguration</code> holds scheduled job input parameters.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SyncConfiguration {
  
  
  private DirectoryConnector sourceDirectory;
  private DirectoryConnector targetDirectory;
  
  private String sourceIntermediatesSearchBase;
  
  private String sourceOrganizationsSearchBase;
  private String targetOrganizationsSearchBase;
  
  private String sourceEntitlementsSearchBase;
  private String targetEntitlementsSearchBase;
  
  private String sourceRolesSearchBase;
  private String targetRolesSearchBase;
  
  private String sourceSpecialRolesSearchBase;
  private String targetSpecialRolesSearchBase;
     
  private int batchSize;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SyncConfiguration</code> configuration for LDAP Synchronization Manager
   ** which allows to share job input parameters
   ** <br>
   ** Default Constructor
   */
  public SyncConfiguration() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SyncConfiguration</code> configuration for LDAP Synchronization Manager
   ** which allows to share job input parameters
   */
  public SyncConfiguration(DirectoryConnector sourceDirectory, DirectoryConnector targetDirectory,
                           String sourceIntermediatesSearchBase, 
                           String sourceOrganizationsSearchBase, String targetOrganizationsSearchBase, 
                           String sourceEntitlementsSearchBase,  String targetEntitlementsSearchBase, 
                           String sourceRolesSearchBase,         String targetRolesSearchBase, 
                           String sourceSpecialRolesSearchBase,  String targetSpecialRolesSearchBase,
                           int batchSize) {
    this.sourceDirectory = sourceDirectory;
    this.targetDirectory = targetDirectory;
    this.sourceIntermediatesSearchBase = sourceIntermediatesSearchBase;
    this.sourceOrganizationsSearchBase = sourceOrganizationsSearchBase;
    this.targetOrganizationsSearchBase = targetOrganizationsSearchBase;
    this.sourceEntitlementsSearchBase = sourceEntitlementsSearchBase;
    this.targetEntitlementsSearchBase = targetEntitlementsSearchBase;
    this.sourceRolesSearchBase = sourceRolesSearchBase;
    this.targetRolesSearchBase = targetRolesSearchBase;
    this.sourceSpecialRolesSearchBase = sourceSpecialRolesSearchBase;
    this.targetSpecialRolesSearchBase = targetSpecialRolesSearchBase;
    this.batchSize = batchSize;
  }


  public void setSourceDirectory(DirectoryConnector sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public DirectoryConnector getSourceDirectory() {
    return sourceDirectory;
  }

  public void setTargetDirectory(DirectoryConnector targetDirectory) {
    this.targetDirectory = targetDirectory;
  }

  public DirectoryConnector getTargetDirectory() {
    return targetDirectory;
  }

  public void setSourceIntermediatesSearchBase(String sourceIntermediatesSearchBase) {
    this.sourceIntermediatesSearchBase = sourceIntermediatesSearchBase;
  }

  public String getSourceIntermediatesSearchBase() {
    return sourceIntermediatesSearchBase;
  }

  public void setSourceOrganizationsSearchBase(String sourceOrganizationsSearchBase) {
    this.sourceOrganizationsSearchBase = sourceOrganizationsSearchBase;
  }

  public String getSourceOrganizationsSearchBase() {
    return sourceOrganizationsSearchBase;
  }

  public void setTargetOrganizationsSearchBase(String targetOrganizationsSearchBase) {
    this.targetOrganizationsSearchBase = targetOrganizationsSearchBase;
  }

  public String getTargetOrganizationsSearchBase() {
    return targetOrganizationsSearchBase;
  }

  public void setSourceEntitlementsSearchBase(String sourceEntitlementsSearchBase) {
    this.sourceEntitlementsSearchBase = sourceEntitlementsSearchBase;
  }

  public String getSourceEntitlementsSearchBase() {
    return sourceEntitlementsSearchBase;
  }

  public void setTargetEntitlementsSearchBase(String targetEntitlementsSearchBase) {
    this.targetEntitlementsSearchBase = targetEntitlementsSearchBase;
  }

  public String getTargetEntitlementsSearchBase() {
    return targetEntitlementsSearchBase;
  }

  public void setSourceRolesSearchBase(String sourceRolesSearchBase) {
    this.sourceRolesSearchBase = sourceRolesSearchBase;
  }

  public String getSourceRolesSearchBase() {
    return sourceRolesSearchBase;
  }

  public void setTargetRolesSearchBase(String targetRolesSearchBase) {
    this.targetRolesSearchBase = targetRolesSearchBase;
  }

  public String getTargetRolesSearchBase() {
    return targetRolesSearchBase;
  }

  public void setSourceSpecialRolesSearchBase(String sourceSpecialRolesSearchBase) {
    this.sourceSpecialRolesSearchBase = sourceSpecialRolesSearchBase;
  }

  public String getSourceSpecialRolesSearchBase() {
    return sourceSpecialRolesSearchBase;
  }

  public void setTargetSpecialRolesSearchBase(String targetSpecialRolesSearchBase) {
    this.targetSpecialRolesSearchBase = targetSpecialRolesSearchBase;
  }

  public String getTargetSpecialRolesSearchBase() {
    return targetSpecialRolesSearchBase;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public int getBatchSize() {
    return batchSize;
  }

}
