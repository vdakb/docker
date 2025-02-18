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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DirectoryMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

////////////////////////////////////////////////////////////////////////////////
// interface DirectoryMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface DirectoryMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                        = "GDS-";

  // 01001 - 01010 connection messages
  static final String CONNECTING_BEGIN              = PREFIX + "01001";
  static final String CONNECTING_SUCCESS            = PREFIX + "01002";
  static final String AUTHENTICATION_BEGIN          = PREFIX + "01003";
  static final String AUTHENTICATION_SUCCESS        = PREFIX + "01004";

  // 01011 - 01020 operation related messages
  static final String ATTRIBUTE_ADDED               = PREFIX + "01011";
  static final String ATTRIBUTE_DELETED             = PREFIX + "01012";
  static final String ATTRIBUTE_TOMODIFY            = PREFIX + "01013";
  static final String ATTRIBUTE_MODIFIED            = PREFIX + "01014";

  // 01021 - 01030 changeLog related messages
  static final String CHANGELOG_LOGENTRY_BEGIN      = PREFIX + "01021";
  static final String CHANGELOG_TARGETDN_NULL       = PREFIX + "01022";
  static final String CHANGELOG_TARGETDN_SKIP       = PREFIX + "01023";
  static final String CHANGELOG_TARGETDN_BASE       = PREFIX + "01024";
  static final String CHANGELOG_TARGETDN_SCOPE      = PREFIX + "01025";
  static final String CHANGELOG_TARGETDN_NOTEXISTS  = PREFIX + "01026";
  static final String CHANGELOG_TARGETRDN_NOTEXISTS = PREFIX + "01027";
  static final String CHANGELOG_FILTER_OBJECTCLASS  = PREFIX + "01028";
  static final String CHANGELOG_FILTER_ATTRIBUTE    = PREFIX + "01029";
  static final String CHANGELOG_FILTER_MODIFIER     = PREFIX + "01030";
  static final String CHANGELOG_FILTER_DISABLED     = PREFIX + "01031";
  static final String CHANGELOG_FILTER_NOTSET       = PREFIX + "01032";
  static final String CHANGELOG_FILTER_SWITCH       = PREFIX + "01033";
  static final String CHANGELOG_CHGTYPE_DELETE      = PREFIX + "01034";
  static final String CHANGELOG_CHGTYPE_ATTRIBUTE   = PREFIX + "01035";
}