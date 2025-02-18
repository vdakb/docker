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

    File        :   XMLError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

////////////////////////////////////////////////////////////////////////////////
// interface XMLError
// ~~~~~~~~~ ~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface XMLError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                        = "XML-";

  // 00071 - 00080 schema load related errors
  static final String ENCODING_UNSUPORTED           = PREFIX + "00061";

  // 00071 - 00080 schema load related errors
  static final String SCHEMA_LOAD                   = PREFIX + "00071";
  static final String SCHEMA_FATAL                  = PREFIX + "00072";
  static final String SCHEMA_FAILED                 = PREFIX + "00073";
  static final String NAMESPACE_INVALID             = PREFIX + "00074";

  // 00081 - 00090 parsing related errors
  static final String PARSING_FATAL                 = PREFIX + "00081";
  static final String PARSING_ERROR                 = PREFIX + "00082";
  static final String PARSING_WARNING               = PREFIX + "00083";
  static final String PARSING_UNEXPECTED_TYPE       = PREFIX + "00084";
  static final String PARSING_UNEXPECTED_NODE       = PREFIX + "00085";
  static final String PARSING_UNEXPECTED_END        = PREFIX + "00086";
  static final String PARSING_DOCUMENT_END          = PREFIX + "00087";
  static final String PARSING_ELEMENT_START         = PREFIX + "00088";
  static final String PARSING_ELEMENT_TEXT          = PREFIX + "00089";
  static final String PARSING_ELEMENT_STARTEND      = PREFIX + "00090";

  // 00091 - 00100 parsing related errors
  static final String REQUIRED_EVENTTYPE_MISMATCH   = PREFIX + "00091";
  static final String REQUIRED_LOCALNAME_EXPECTED   = PREFIX + "00092";
  static final String REQUIRED_LOCALNAME_MISMATCH   = PREFIX + "00093";
  static final String REQUIRED_NAMESPACE_EXPECTED   = PREFIX + "00094";
  static final String REQUIRED_NAMESPACE_UNEXPECTED = PREFIX + "00095";
  static final String REQUIRED_NAMESPACE_MISMATCH   = PREFIX + "00096";

  // 00101 - 00111 input processing related errors
  static final String INPUT_TRANSISTION             = PREFIX + "00101";
  static final String INPUT_ELEMENT_UNKNOWN         = PREFIX + "00102";
  static final String INPUT_ELEMENT_MISSING         = PREFIX + "00103";
  static final String INPUT_ELEMENT_EMPTY           = PREFIX + "00104";
  static final String INPUT_ELEMENT_AMBIGUOUS       = PREFIX + "00105";
  static final String INPUT_ELEMENT_VALUE           = PREFIX + "00106";
  static final String INPUT_ATTRIBUTE_UNKNOWN       = PREFIX + "00107";
  static final String INPUT_ATTRIBUTE_MISSING       = PREFIX + "00108";
  static final String INPUT_ATTRIBUTE_EMPTY         = PREFIX + "00109";
  static final String INPUT_ATTRIBUTE_AMBIGUOUS     = PREFIX + "00110";
  static final String INPUT_ATTRIBUTE_VALUE         = PREFIX + "00111";

  // 00121 - 00130 output processing related errors
  static final String OUTPUT_ROOT                   = PREFIX + "00121";
  static final String OUTPUT_NODE_NAME              = PREFIX + "00122";
  static final String OUTPUT_NODE_INVALID           = PREFIX + "00123";
  static final String OUTPUT_NODE_REMOVE            = PREFIX + "00124";
  static final String OUTPUT_START_TAG              = PREFIX + "00125";

  // 00131 - 00141 output processing related errors
  static final String TRANSFORMATION_UNSUPPORTED    = PREFIX + "00131";
  static final String TRANSFORMATION_LOCALE         = PREFIX + "00132";
}