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
    Subsystem   :   Common Shared Workflow Facility

    File        :   ServiceError.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow;

////////////////////////////////////////////////////////////////////////////////
// interface ServiceError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ServiceError extends ServiceConstant {

  // 00001 - 00010 naming and lookup related errors
  static final String CONTEXT_CONNECTION    = ServiceConstant.PREFIX + "00001";
  static final String CONTEXT_MANDATORY     = ServiceConstant.PREFIX + "00002";
  static final String CONTEXT_INITIALIZE    = ServiceConstant.PREFIX + "00003";
  static final String CONTEXT_ACCESS_DENIED = ServiceConstant.PREFIX + "00004";
  static final String CONTEXT_CLOSE         = ServiceConstant.PREFIX + "00004";
  static final String CONTEXT_ENVIRONMENT   = ServiceConstant.PREFIX + "00005";

  // 00011 - 00030 instance attribute related errors
  static final String ATTRIBUTE_MISSING   = ServiceConstant.PREFIX + "00021";
}