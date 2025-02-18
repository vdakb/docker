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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   FeatureConstant.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureConstant.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common;

////////////////////////////////////////////////////////////////////////////////
// interface FeatureConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface FeatureConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                   = "ODS-";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** output format should take place.
   */
  static final String FORMAT_LDIF              = "ldif";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** output format should take place.
   */
  static final String FORMAT_DSML              = "dsml";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** output format should take place.
   */
  static final String FORMAT_JSON              = "json";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  static final String SEARCH_SCOPE_OBJECT      = "object";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  static final String SEARCH_SCOPE_ONELEVEL    = "one";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** search scope should take place.
   */
  static final String SEARCH_SCOPE_SUBTREE     = "sub";

  /**
   ** Default value of the distinguished name attibute of an entry in a V3
   ** compliant LDAP server.
   */
  static final String DN                       = "dn";

  static final String CONTROL                  = "control";
  /**
   ** Default value of the name of the generic object class of an entry in a V3
   ** compliant LDAP server.
   */
  static final String OBJECT_CLASS             = "objectClass";

  static final String VERSION                  = "version";

  /**
   ** Default value to access the type of the entry in the changelog.
   */
  static final String CHANGE_TYPE              = "changeType";

  /**
   ** The name to specify that the type of a change in the directory is an add
   */
  static final String CHANGE_TYPE_ADD          = "add";

  /**
   ** The name to specify that the type of a change in the directory is a
   ** modification
   */
  static final String CHANGE_TYPE_MODIFY       = "modify";

  /**
   ** The name to specify that the type of a change in the directory is a
   ** delete
   */
  static final String CHANGE_TYPE_DELETE       = "delete";

  static final String CHANGE_TYPE_RENAME_DN    = "moddn";
  static final String CHANGE_TYPE_RENAME_RDN   = "modrdn";

  static final String CHANGE_OPERATION_ADD     = "add";
  static final String CHANGE_OPERATION_REPLACE = "replace";
  static final String CHANGE_OPERATION_REMOVE  = "remove";

  static final String RDNNEW                   = "newrdn";
  static final String RDNOLD_DELETE            = "deleteoldrdn";
  static final String PARENTNEW                = "newparent";
  static final String SUPERIORNEW              = "newsuperior";
}