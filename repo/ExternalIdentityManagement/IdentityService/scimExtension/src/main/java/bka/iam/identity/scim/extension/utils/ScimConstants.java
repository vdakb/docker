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

    File        :   ScimConstants.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    ScimConstants.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-25-05  JLakic     First release version
*/
package bka.iam.identity.scim.extension.utils;


public class ScimConstants {
    

  public static final String OIG_SCIM_SERVICE_ENDPOINT = "/iam/governance/scim/v1";
  public static final String SCIM_EXTENSION_SERVICE_ENDPOINT = "/igs/scim/v2";

  public static final String LIST_SCHEMA_URN                = "urn:ietf:params:scim:api:messages:2.0:ListResponse";
  public static final String USER_APPLICATION_SCHEMA_URN    = "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:UserApplication";
  public static final String APPLICATION_SCHEMA_URN         = "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:Application";
  public static final String APPLICATION_ATTRIBUTES_URN     = "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:ApplicationAttributes";
  public static final String APPLICATION_ACCOUNT_SCHEMA_URN = "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:ApplicationAccount";
  public static final String ENTITLEMENTS_SCHEMA_URN        = "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:Entitlement";
  public static final String POLICY_SCHEMA_URN              = "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:Policy";
  public static final String ERROR_SCHEMA_URN               = "urn:ietf:params:scim:api:messages:2.0:Error";
    
    
  public static final String OPATCH_ADD                   = "add";
  public static final String OPATCH_REMOVE                = "remove";
  public static final String OPATCH_REPLACE               = "replace";



  public static final String[] RESOURCE_TYPES = new String[] {
    "User"
  };
    
  public static final String[] SCHEMAS = new String[] {
   "urn:ietf:params:scim:schemas:core:2.0:Schema", 
   "urn:ietf:params:scim:schemas:core:2.0:ResourceType",
   "urn:ietf:params:scim:api:messages:2.0:PatchOp",
   "urn:ietf:params:scim:schemas:core:2.0:User",
   "urn:ietf:params:scim:schemas:extension:oracle:2.0:IDM:User",
   "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User",
   "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:User",
    USER_APPLICATION_SCHEMA_URN,
    APPLICATION_ACCOUNT_SCHEMA_URN,
    ENTITLEMENTS_SCHEMA_URN,
    POLICY_SCHEMA_URN
  };
}
