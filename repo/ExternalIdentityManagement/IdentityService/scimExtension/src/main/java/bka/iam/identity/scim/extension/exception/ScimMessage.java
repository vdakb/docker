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

    File        :   ScimExceptionMapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the ScimMessage interface.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-20-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.exception;

import bka.iam.identity.scim.extension.exception.resource.ScimBundle;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// interface ScimMessage
// ~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The ScimMessage interface defines a collection of error codes and their
 ** associated messages for the SCIM service. It provides a mechanism for
 ** localizing error messages using a resource bundle.
 ** 
 ** Error codes are grouped into categories for better organization:
 ** - 00001 - 00100: General SCIM-related errors
 ** - 00101 - 00200: User-related errors
 ** - 00201 - 00210: Group-related errors
 ** - 01001 - 01500: Operation-related errors (GET, PATCH, POST, PUT, DELETE)
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ScimMessage {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX                           = "SCIM-";
  
  // 00001 - 00100 General SCIM related errors
  static final String GENERAL                          = PREFIX + "00001";
  static final String SCHEMA_NOTFOUND                  = PREFIX + "00002";
  static final String ATTRIBUTE_SCHEMA_NOTFOUND        = PREFIX + "00003";
  static final String ATTRIBUTE_MANDATORY              = PREFIX + "00004";
  static final String INVALID_JSON_STRUCTURE_ARRAY     = PREFIX + "00005";
  static final String INCORECT_SCHEMA                  = PREFIX + "00006";
  static final String INCORECT_ENTITLEMENT_ACTION      = PREFIX + "00007";
  static final String ATTRIBUTE_EXPECTED_SINGLE_VALUE  = PREFIX + "00008";
  static final String ATTRIBUTE_EXPECTED_MULTI_VALUE   = PREFIX + "00009";
  static final String ATTRIBUTE_TYPE_MISMATCH          = PREFIX + "000010";
  static final String SUBATTRIBUTE_NOT_FOUND           = PREFIX + "000011";
  static final String INVALID_QUERY_PARAMETERS         = PREFIX + "000012";
  static final String INVALID_STARTINDEX               = PREFIX + "000013";
  static final String DUPLICATE_CANONICAL_VALUE        = PREFIX + "000014";
  static final String VALUE_NOT_ALLOWED                = PREFIX + "000015";
  static final String VALUE_ALREADY_EXIST              = PREFIX + "000016";
  
  // 00101 - 00201 User related errors
  static final String USER_NOTFOUND                    = PREFIX + "00101";
  static final String USER_NOT_AUTHORIZED              = PREFIX + "00102";
  
  // 00201 - 00300 Group related errors
  static final String GROUP_NOTFOUND                   = PREFIX + "00201";
  static final String GROUP_OPERATION_ONLY_MEMBER      = PREFIX + "00202";
  static final String GROUP_CANNOT_DELETE              = PREFIX + "00203";
  
  // 00301 - 00400 Group related errors
  static final String POLICY_NOTFOUND                  = PREFIX + "00301";
  static final String POLICY_CANT_REMOVE_ALL           = PREFIX + "00302";
  // 00401 - 00410 Application related errors
  static final String APPLICATION_NOTFOUND             = PREFIX + "00401";
  static final String NAMESPACE_NOTFOUND               = PREFIX + "00402";
  static final String NAMESPACE_ENTITLEMENT_NOTFOUND   = PREFIX + "00403";
  
  // 00401 - 00410 Entitlement related errors
  static final String ENTITLEMENT_NOTFOUND             = PREFIX + "00501"; 
  // 01001 - 01100 Get Operation related errors
  static final String GET_RESOURCE_FAILED              = PREFIX + "01001";
  
  // 01201 - 01200 Patch Operation related errors
  static final String REPLACE_RESOURCE_FAILED          = PREFIX + "01201";
  static final String PATCH_ATTRIBUTE_MISSING          = PREFIX + "01202";
  static final String PATCH_UNSUPPORTED_OPERATION      = PREFIX + "01203";
  static final String PATCH_DUPLICATE_VALUE            = PREFIX + "01204";
  static final String PATCH_PATH_NOT_EXIST             = PREFIX + "01205";
  static final String PATCH_RESOURCE_WIHTOUT_ATTRIBUTE = PREFIX + "01206";
  
  // 01301 - 01300 Post Operation related errors
  static final String CREATE_RESOURCE_FAILED           = PREFIX + "01301";
  
  // 01401 - 01400 Put Operation related errors
  static final String MODIFY_RESOURCE_FAILED           = PREFIX + "01401";
  
  // 01501 - 01600 Delete Operation related errors
  static final String DELETE_RESOURCE_FAILED           = PREFIX + "01501";
  
  
  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    ScimBundle.class.getName()
  , Locale.getDefault()
  , ScimBundle.class.getClassLoader()
  );

}
