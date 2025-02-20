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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   ResourceContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ResourceContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim;

///////////////////////////////////////////////////////////////////////////////
// interface ResourceContext
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 ** <p>
 ** The class covvers common action required to perform activities regrading
 ** SCIM request/reply synchronous invocations to the SCIM API of a SCIM
 ** <code>Service Provider</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ResourceContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The SCIM media type string */
  static final String MEDIA_TYPE                       = "application/scim+json";

  static final String RESOURCE_TYPE_USER               = "User";
  static final String RESOURCE_TYPE_GROUP              = "Group";
  static final String RESOURCE_TYPE_SCHEMA             = "Schema";
  static final String RESOURCE_TYPE_RESOURCE           = "ResourceType";
  static final String RESOURCE_TYPE_CONFIGURATION      = "ServiceProviderConfig";

  /**
   ** The "<code>/Me</code>" authenticated subject URI alias for the User or
   ** other resource associated with the currently authenticated subject for any
   ** SCIM operation.
   */
  static final String ENDPOINT_ME                      = "Me";
  /**
   ** An HTTP GET to this endpoint is used to discover the types of resources
   ** available on a SCIM <code>Service Provider</code> (for example, Users and
   ** Groups).
   */
  static final String ENDPOINT_RESOURCE_TYPES          = "ResourceTypes";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** resource schemas supported by a SCIM <code>Service Provider</code>.
   */
  static final String ENDPOINT_SCHEMAS                 = "Schemas";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** of the resource type <code>User</code>.
   ** <p>
   ** SCIM provides a resource type for <code>User</code>s resources. The core
   ** schema for <code>User</code> is identified using the following schema URI:
   ** <ul>
   **   <li>Version 1: <code>urn:scim:schemas:core:1.0</code>.
   **   <li>Version 2: <code>urn:ietf:params:scim:schemas:core:2.0:User</code>,
   **                  urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.
   ** </ul>
   */
  static final String ENDPOINT_USERS                   = "Users";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** of the resource type <code>Group</code> provided by a SCIM
   ** <code>Service Provider</code>.
   ** <p>
   ** SCIM provides a schema for representing groups. The core schema for
   ** resource type <code>Group</code> is identified using the following schema
   ** URI:
   ** <ul>
   **   <li>Version 1: <code>urn:scim:schemas:core:1.0</code>.
   **   <li>Version 2: <code>urn:ietf:params:scim:schemas:core:2.0:Group</code>,
   **                  urn:ietf:params:scim:schemas:extension:enterprise:2.0:Group.
   ** </ul>
   */
  static final String ENDPOINT_GROUPS                   = "Groups";
  /**
   ** An HTTP GET to this endpoint will return a JSON structure that describes
   ** the SCIM specification features available on a
   ** <code>Service Provider</code>.
   */
  static final String ENDPOINT_SERVICE_PROVIDER_CONFIG = "ServiceProviderConfig";
}