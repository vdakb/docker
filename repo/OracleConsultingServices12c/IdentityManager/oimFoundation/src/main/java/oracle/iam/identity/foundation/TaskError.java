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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   TaskError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TaskError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

////////////////////////////////////////////////////////////////////////////////
// interface TaskError
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface TaskError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                       = "OIM-";

  // 00001 - 00010 task related errors
  static final String UNHANDLED                    = PREFIX + "00001";
  static final String GENERAL                      = PREFIX + "00002";
  static final String ABORT                        = PREFIX + "00003";
  static final String NOTIMPLEMENTED               = PREFIX + "00004";
  static final String CLASSNOTFOUND                = PREFIX + "00005";
  static final String CLASSNOTCREATE               = PREFIX + "00006";
  static final String CLASSNOACCESS                = PREFIX + "00007";
  static final String CLASSINVALID                 = PREFIX + "00008";
  static final String CLASSCONSTRUCTOR             = PREFIX + "00009";

  // 00011 - 00016 method argument related errors
  static final String ARGUMENT_IS_NULL             = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE            = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE           = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH       = PREFIX + "00014";
  static final String ARGUMENT_IS_NULLOREMPTY      = PREFIX + "00015";
  static final String ARGUMENT_VALUE_MISSING       = PREFIX + "00016";

  // 00017 - 00020 task and task attribute related errors
  static final String TASK_NOTFOUND                = PREFIX + "00017";
  static final String TASK_ATTRIBUTE_MISSING       = PREFIX + "00018";
  static final String TASK_ATTRIBUTE_NOT_INRANGE   = PREFIX + "00019";
  static final String TASK_ATTRIBUTE_NOT_MAPPED    = PREFIX + "00020";

  // 00021 - 00030 IT Resource related errors
  static final String ITRESOURCE_NOT_FOUND         = PREFIX + "00021";
  static final String ITRESOURCE_AMBIGUOUS         = PREFIX + "00022";
  static final String ITRESOURCE_ATTRIBUTE_MISSING = PREFIX + "00023";
  static final String ITRESOURCE_ATTRIBUTE_ISNULL  = PREFIX + "00024";

  // 00031 - 00040 Lookup Definition related errors
  static final String LOOKUP_NOT_FOUND             = PREFIX + "00031";
  static final String LOOKUP_ENCODED_VALUE         = PREFIX + "00032";
  static final String LOOKUP_INVALID_ATTRIBUTE     = PREFIX + "00033";
  static final String LOOKUP_INVALID_VALUE         = PREFIX + "00034";
  static final String LOOKUP_ATTRIBUTE_MISSING     = PREFIX + "00035";
  static final String LOOKUP_ATTRIBUTE_ISNULL      = PREFIX + "00036";

  // 00041 - 00050 Resource Object related errors
  static final String RESOURCE_NOT_FOUND           = PREFIX + "00041";
  static final String RESOURCE_AMBIGUOUS           = PREFIX + "00042";
  static final String RESOURCE_RECONFIELD          = PREFIX + "00043";
  static final String RESOURCE_RECON_MULTIVALUE    = PREFIX + "00044";
  static final String PROCESSDEFINITION_NOT_FOUND  = PREFIX + "00045";
  static final String PROCESSDEFINITION_DEFAULT    = PREFIX + "00046";
  static final String PROCESSFORM_NOT_FOUND        = PREFIX + "00047";
  static final String PROCESSFORM_AMBIGUOUS        = PREFIX + "00048";

  // 00051 - 00060 Entity ResultSet related errors
  static final String COLUMN_NOT_FOUND             = PREFIX + "00051";
  static final String ENTITY_NOT_EXISTS            = PREFIX + "00052";
  static final String ENTITY_NOT_FOUND             = PREFIX + "00053";
  static final String ENTITY_AMBIGUOUS             = PREFIX + "00054";

  // 00061 - 00070 Reconciliation Event errors
  static final String EVENT_NOT_FOUND              = PREFIX + "00061";
  static final String EVENT_RECEIVE_ERROR          = PREFIX + "00062";
  static final String EVENT_ILLEGAL_INPUT          = PREFIX + "00063";
  static final String SCHEDULE_JOB_NOT_FOUND       = PREFIX + "00064";
  static final String SCHEDULE_JOB_RUNNING         = PREFIX + "00065";

  // 00071 - 00080 mapping related errors
  static final String ATTRIBUTE_MAPPING_EMPTY      = PREFIX + "00071";
  static final String TRANSFORMATION_MAPPING_EMPTY = PREFIX + "00072";
  static final String ATTRIBUTE_KEY_NOTMAPPED      = PREFIX + "00073";
  static final String ATTRIBUTE_VALUE_ISNULL       = PREFIX + "00074";
  static final String ATTRIBUTE_NOT_MAPPABLE       = PREFIX + "00075";
  static final String ATTRIBUTE_NOT_SINGLEVALUE    = PREFIX + "00075";

  // 00081 - 00090 dependency analyzer errors
  static final String DEPENDENCY_PARENT_CONFLICT   = PREFIX + "00081";

  // 00091 - 00100 EntityManager related errors
  static final String NOSUCH_ENTITY                = PREFIX + "00091";
  static final String STALE_ENTITY                 = PREFIX + "00092";
  static final String UNKNOW_ATTRIBUTE             = PREFIX + "00093";
  static final String INVALID_DATATYPE             = PREFIX + "00094";
  static final String INVALID_DATAFORMAT           = PREFIX + "00095";
  static final String UNSUPPORTED_OPERATION        = PREFIX + "00096";
  static final String PROVIDER_EXCEPTION           = PREFIX + "00097";

  // 00101 - 00110 Authorization related errors
  static final String ACCESS_DENIED                = PREFIX + "00101";

  // 00111 - 00120 metadata connection related errors
  static final String METADATA_INSTANCE_CREATE     = PREFIX + "00111";
  static final String METADATA_INSTANCE_CLOSE      = PREFIX + "00112";
  static final String METADATA_SESSION_CREATE      = PREFIX + "00113";
  static final String METADATA_SESSION_COMMIT      = PREFIX + "00114";

  // 00121 - 00130 metadata object related errors
  static final String METADATA_OBJECT_EMPTY        = PREFIX + "00121";
  static final String METADATA_OBJECT_NOTFOUND     = PREFIX + "00122";

  // 00131 - 00140 system property related errors
  static final String PROPERTY_NOTEXISTS           = PREFIX + "00131";
  static final String PROPERTY_VALUE_CONFIGURATION = PREFIX + "00132";

  // 00141 - 00150 application instance related errors
  static final String INSTANCE_NOT_FOUND           = PREFIX + "00141";
  static final String INSTANCE_AMBIGUOUS           = PREFIX + "00142";
}