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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Federated Identity Management
    Subsystem   :   Federal Criminal Police Office Frontend Customizations

    File        :   Console.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Console.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package bka.iam.identity.resource;

////////////////////////////////////////////////////////////////////////////////
// interface Console
// ~~~~~~~~~ ~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Console {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Constant used to demarcate user entity related messages. */
  static final String USR_PREFIX             = "USR";
  static final String USR_ANONYMIZED_LABEL   = USR_PREFIX + "_ANONYMIZED_LABEL";
  static final String USR_ANONYMIZED_HINT    = USR_PREFIX + "_ANONYMIZED_HINT";
  static final String USR_UNIFIED_LABEL      = USR_PREFIX + "_UNIFIED_LABEL";
  static final String USR_UNIFIED_HINT       = USR_PREFIX + "_UNIFIED_HINT";
  static final String USR_PARTICIPANT_TITLE  = USR_PREFIX + "_PARTICIPANT_TITEL";
  static final String USR_PARTICIPANT_LABEL  = USR_PREFIX + "_PARTICIPANT_LABEL";
  static final String USR_PARTICIPANT_HINT   = USR_PREFIX + "_PARTICIPANT_HINT";
  static final String USR_ORGANIZATION_LABEL = USR_PREFIX + "_ORGANIZATION_LABEL";
  static final String USR_ORGANIZATION_HINT  = USR_PREFIX + "_ORGANIZATION_HINT";
  static final String USR_DIVISION_LABEL     = USR_PREFIX + "_DIVISION_LABEL";
  static final String USR_DIVISION_HINT      = USR_PREFIX + "_DIVISION_HINT";
  static final String USR_DEPARTMENT_LABEL   = USR_PREFIX + "_DEPARTMENT_LABEL";
  static final String USR_DEPARTMENT_HINT    = USR_PREFIX + "_DEPARTMENT_HINT";
  static final String USR_GENERATION_LABEL   = USR_PREFIX + "_GENERATION_LABEL";
  static final String USR_GENERATION_HINT    = USR_PREFIX + "_GENERATION_HINT";
  static final String USR_GENERATED_LABEL    = USR_PREFIX + "_GENERATED_LABEL";
  static final String USR_GENERATED_HINT     = USR_PREFIX + "_GENERATED_HINT";
  static final String USR_JOBROLE_LABEL      = USR_PREFIX + "_JOBROLE_LABEL";
  static final String USR_JOBROLE_HINT       = USR_PREFIX + "_JOBROLE_HINT";
  static final String USR_QUALIFIED_LABEL    = USR_PREFIX + "_QUALIFIED_LABEL";
  static final String USR_QUALIFIED_HINT     = USR_PREFIX + "_QUALIFIED_HINT";

  /** Constant used to demarcate authorization entity related messages. */
  static final String ACC_PREFIX             = "ACC";
  static final String ACC_BADGE_TITLE        = ACC_PREFIX + "_BADGE_TITLE";
  static final String ACC_BADGE_HINT         = ACC_PREFIX + "_BADGE_HINT";
  static final String ACC_APPLICATION_MENU   = ACC_PREFIX + "_APPLICATION_MENU";
  static final String ACC_ENTITLEMENT_MENU   = ACC_PREFIX + "_ENTITLEMENT_MENU";

  /** Constant used to demarcate catalog entity related messages. */
  static final String CAT_PREFIX             = "CAT";
  static final String CAT_BADGE_TITLE        = CAT_PREFIX + "_BADGE_TITLE";
  static final String CAT_BADGE_HINT         = CAT_PREFIX + "_BADGE_HINT";

  /** Constant used to demarcate eFBS entity related messages. */
  static final String FBS_PREFIX             = "FBS";
  static final String FBS_TITLE              = FBS_PREFIX + "_TITLE";
  static final String FBS_ACTION_LABEL       = FBS_PREFIX + "_ACTION_LABEL";
  static final String FBS_ACTION_HINT        = FBS_PREFIX + "_ACTION_HINT";

  /** Constant used to demarcate access policy evaluation related messages. */
  static final String EVAL_PREFIX            = "EVAL";
  static final String EVAL_ACTION_LABEL      = EVAL_PREFIX + "ACTION_LABEL";
  static final String EVAL_ACTION_HINT       = EVAL_PREFIX + "ACTION_HINT";

}