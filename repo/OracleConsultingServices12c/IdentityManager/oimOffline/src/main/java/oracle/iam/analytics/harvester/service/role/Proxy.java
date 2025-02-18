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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   Proxy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Proxy.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.analytics.harvester.service.role;

import javax.xml.namespace.QName;

import oracle.iam.analytics.harvester.request.role.ObjectFactory;

////////////////////////////////////////////////////////////////////////////////
// interface Proxy
// ~~~~~~~~~ ~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public interface Proxy {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PATH                    = "roleHarvesterService";
  static final QName  NAME                    = new QName(ObjectFactory.NAMESPACE, "RoleHarvesterService");
  static final QName  PORT                    = new QName(ObjectFactory.NAMESPACE, "RoleHarvesterServicePort");

  /**
   ** the XML element that identifies the request to create a role and policies
   ** in Oracle Identity Analytics.
   */
  static final String ELEMENT_CREATE_REQUEST  = "create";

  /**
   ** the XML element that identifies the response of an role create request
   ** in Oracle Identity Analytics.
   */
  static final String ELEMENT_CREATE_RESPONSE = "createResponse";

  /**
   ** the XML element that identifies the request to modify a role in Oracle
   ** Identity Analytics.
   */
  static final String ELEMENT_MODIFY_REQUEST  = "modify";

  /**
   ** the XML element that identifies the response of an role modify request
   ** in Oracle Identity Analytics.
   */
  static final String ELEMENT_MODIFY_RESPONSE = "modifyResponse";

  /**
   ** the XML element that identifies the request to export a role and policies
   ** from Oracle Identity Analytics.
   */
  static final String ELEMENT_EXPORT_REQUEST  = "export";

  /**
   ** the XML element that identifies the response of a role export request
   ** from Oracle Identity Analytics.
   */
  static final String ELEMENT_EXPORT_RESPONSE = "exportResponse";

  /**
   ** the XML element that identifies a the exists flag in Oracle Identity
   ** Analytics.
   */
  static final String ELEMENT_EXISTS_REQUEST  = "exists";

  /**
   ** the XML element that identifies a the response of an role exists request
   ** in Oracle Identity Analytics.
   */
  static final String ELEMENT_EXISTS_RESPONSE = "existsResponse";

  /**
   ** the XML element that identifies a the result in a response of an role
   ** exists request in Oracle Identity Analytics.
   */
  static final String ELEMENT_EXISTS          = "exists";
  static final String ELEMENT_ROLE            = "role";
  static final String ELEMENT_POLICY          = "policy";
  static final String ELEMENT_ENTITLEMENTS    = "entiltements";
  static final String ELEMENT_ENTITLEMENT     = "entiltement";

  /**
   ** the XML element that identifies a custom property of a role exported to
   ** Oracle Identity Analytics.
   */
  static final String ELEMENT_PROPERTY        = "customProperty";

  /**
   ** the XML element that identifies a ownership of a role exported to Oracle
   ** Identity Analytics.
   */
  static final String ELEMENT_OWNERSHIP       = "ownerShip";

  /**
   ** the XML element that identifies a business unit of a role exported to
   ** Oracle Identity Analytics.
   */
  static final String ELEMENT_BUSINESSUNIT    = "businessUnit";

  /**
   ** the XML element that identifies the resource type and endpoint of a policy
   ** associated with the role and needs to be created in Oracle Identity
   ** Analytics.
   */
  static final String ELEMENT_RESOURCE        = "resource";

  static final String ELEMENT_VALIDATE_ONLY   = "validateOnly";

  static final String ATTRIBUTE_ID            = "id";

  /**
   ** the XML attribute that identifies the resource type of a policy associated
   ** with the role and needs to be created in Oracle Identity Analytics.
   */
  static final String ATTRIBUTE_NAMESPACE     = "namespace";
  static final String ATTRIBUTE_ENDPOINT      = "endpoint";
  static final String ATTRIBUTE_DISPLAYNAME   = "displayName";
  static final String ATTRIBUTE_DESCRIPTION   = "description";
  static final String ATTRIBUTE_COMMENT       = "comment";
  static final String ATTRIBUTE_DEPARTMENT    = "department";
  static final String ATTRIBUTE_JOBCODE       = "jobCode";
  static final String ATTRIBUTE_TYPE          = "type";
  static final String ATTRIBUTE_RISK          = "risk";
  static final String ATTRIBUTE_STARTDATE     = "startDate";
  static final String ATTRIBUTE_ENDDATE       = "endDate";

  /**
   ** the XML attribute that identifies a the overall status of a request in
   ** Oracle Identity Analytics.
   */
  static final String ATTRIBUTE_STATUS        = "status";
}