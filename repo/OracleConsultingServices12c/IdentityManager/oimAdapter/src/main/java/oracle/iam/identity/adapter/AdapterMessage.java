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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   AdapterMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AdapterMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-05  DSteding    First release version
*/

package oracle.iam.identity.adapter;

////////////////////////////////////////////////////////////////////////////////
// interface AdapterMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface AdapterMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PREFIX            = "ADP-";

  static final String REQUEST           = PREFIX + "00001";

  static final String IS_MEMBER         = PREFIX + "00010";
  static final String NOT_MEMBER        = PREFIX + "00011";
  static final String ADD_MEMBER        = PREFIX + "00012";
  static final String ADDED_MEMBER      = PREFIX + "00013";
  static final String REMOVE_MEMBER     = PREFIX + "00014";
  static final String REMOVED_MEMBER    = PREFIX + "00015";

  static final String CHECK_RESOURCE    = PREFIX + "00021";

  static final String STATUS_NOCHANGES  = PREFIX + "00031";
  static final String STATUS_CHANGINGTO = PREFIX + "00032";
}
