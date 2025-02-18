/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtaul Resource Management

    File        :   ResolverError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ResolverError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

////////////////////////////////////////////////////////////////////////////////
// interface ResolverError
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ResolverError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the mining message prefix. */
  static final String PREFIX                    = "POL-";

  // 00031 - 00040 Resource Object related errors
  static final String RESOURCE_RECONFIELD      = PREFIX + "00043";

  // 00071 - 00080 mapping related errors
  static final String NO_INBOUND_MAPPING       = PREFIX + "00071";
  static final String NO_OUTBOUND_MAPPING      = PREFIX + "00072";

  // 00081 - 00090 object resolving related errors
  static final String TYPE_MISMATCH            = PREFIX + "00082";
  static final String ORDER_MISMATCH           = PREFIX + "00083";
  static final String POLICY_FORMSIZE_MISMATCH = PREFIX + "00084";
  static final String POLICY_FORM_MISMATCH     = PREFIX + "00085";
  static final String NOT_PROVISIONED          = PREFIX + "00086";
  static final String AMBIGUOS_PROVISIONED     = PREFIX + "00087";
}