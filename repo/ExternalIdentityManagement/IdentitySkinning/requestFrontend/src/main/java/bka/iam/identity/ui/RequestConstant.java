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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Special Account Request

    File        :   RequestConstant.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RequestConstant.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package bka.iam.identity.ui;

////////////////////////////////////////////////////////////////////////////////
// interface RequestConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares global visible identifier used for user information purpose.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface RequestConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                 = "FIM-";

  static final String FIM_HEADER             = "FIM_HEADER";
  static final String FIM_SYMBOL             = "FIM_SYMBOL";
  static final String FIM_INSTRUCTION        = "FIM_INSTRUCTION";
  static final String FIM_ENVIRONMENT_LABEL  = "FIM_ENVIRONMENT_LABEL";
  static final String FIM_ENVIRONMENT_HINT   = "FIM_ENVIRONMENT_HINT";
  static final String FIM_TEMPLATE_LABEL     = "FIM_TEMPLATE_LABEL";
  static final String FIM_TEMPLATE_HINT      = "FIM_TEMPLATE_HINT";
  static final String FIM_APPLICATION_INFO   = "FIM_APPLICATION_INFO";
  static final String FIM_ENTITLEMENT_INFO   = "FIM_ENTITLEMENT_INFO";
  static final String FIM_ENTITLEMENT_SINGLE = "FIM_ENTITLEMENT_SINGLE";

  /** Constants that are belonging to the evaluation. */
  static final String EVAL_PREFIX            = "EVAL-";

}