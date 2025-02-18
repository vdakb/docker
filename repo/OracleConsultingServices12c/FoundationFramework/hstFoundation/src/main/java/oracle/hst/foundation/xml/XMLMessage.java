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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared XML Stream Facilities

    File        :   XMLMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

////////////////////////////////////////////////////////////////////////////////
// interface XMLMessage
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface XMLMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                  = "XML-";

  // 01000 - 01010 generic message template
  static final String MESSAGE                = PREFIX + "01000";
  static final String ELEMENT                = PREFIX + "01001";
  static final String ATTRIBUTE              = PREFIX + "01002";

  // 01011 - 01020 processing messages
  static final String VALIDATING             = PREFIX + "01011";
  static final String LOADING                = PREFIX + "01012";
  static final String PROCESSING             = PREFIX + "01013";

  // 01021 - 01030 parsing messages
  static final String EVENT_DISPATCH         = PREFIX + "01021";
  static final String EVENT_DISPATCH_ELEMENT = PREFIX + "01022";
  static final String EVENT_UNKNOWN          = PREFIX + "01023";
  static final String EVENT_DOCUMENT_START   = PREFIX + "01024";
  static final String EVENT_DOCUMENT_END     = PREFIX + "01025";
  static final String EVENT_ELEMENT_START    = PREFIX + "01026";
  static final String EVENT_ELEMENT_END      = PREFIX + "01027";
  static final String EVENT_TEXT             = PREFIX + "01028";
}