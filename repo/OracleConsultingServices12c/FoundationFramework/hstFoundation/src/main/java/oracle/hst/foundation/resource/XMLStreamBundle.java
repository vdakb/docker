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
    Subsystem   :   Common Shared MDS Store Facilities

    File        :   XMLStreamBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLStreamBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    0.0.0.1      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.XMLMessage;


////////////////////////////////////////////////////////////////////////////////
// class XMLStreamBundle
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code common
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class XMLStreamBundle extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // OHF-00001 - OHF-00005 General error (Undefined)
    { SystemError.GENERAL,                     "General error: %1$s" }
  , { SystemError.UNHANDLED,                   "An unhandled exception has occured: %1$s" }
  , { SystemError.ABORT,                       "Execution aborted: %1$s" }
  , { SystemError.NOTIMPLEMENTED,              "Feature is not yet implemented!" }
  , { SystemError.CLASSNOTFOUND,               "Class %1$s was not found in the classpath!" }
  , { SystemError.CLASSNOTCREATE,              "Class %1$s has not been created!" }

     // OHF-00006 - OHF-00010 method argument related errors
  , { SystemError.ARGUMENT_IS_NULL,            "Passed argument %1$s must not be null!" }
  , { SystemError.ARGUMENT_BAD_TYPE,           "Passed argument %1$s has a bad type!" }
  , { SystemError.ARGUMENT_BAD_VALUE,          "Passed argument %1$s contains an invalid value!" }
  , { SystemError.ARGUMENT_SIZE_MISMATCH,      "Passed argument array size dont match expected length!" }

     // OHF-00011 - OHF-00020 instance attribute related errors
  , { SystemError.ATTRIBUTE_IS_NULL,           "State of attribute %1$s must not be null!" }

     // OHF-00021 - OHF-00030 file related errors
  , { SystemError.FILE_MISSING,                "Encountered problems to find file %1$s!" }
  , { SystemError.FILE_NOT_FILE,               "%1$s is not a file!" }
  , { SystemError.FILE_OPEN,                   "Encountered problems to open file %1$s!" }
  , { SystemError.FILE_CLOSE,                  "Encountered problems to close file %1$s!" }
  , { SystemError.FILE_READ,                   "Encountered problems reading file %1$s!" }
  , { SystemError.FILE_WRITE,                  "Encountered problems writing file %1$s!" }

     // XML-00061 - XML-00070 file encoding related errors
  , { XMLError.ENCODING_UNSUPORTED,            "Error while loading XML Schema %1$s: %2$s" }

     // XML-00071 - XML-00080 schema validation related errors
  , { XMLError.SCHEMA_LOAD,                    "Error while loading XML Schema %1$s: %2$s" }
  , { XMLError.SCHEMA_FATAL,                   "Validation failed before starting because of unexpected error: %1$s" }
  , { XMLError.SCHEMA_FAILED,                  "Validation failed at (line %1$s, column %2$s): %3$s" }
  , { XMLError.NAMESPACE_INVALID,              "Namespace [%4$s] is not valid in the scope of [%5$s] at (line %1$s, column %2$s): %3$s" }

     // XML-00081 - XML-00090 parsing related errors
  , { XMLError.PARSING_FATAL,                  "Fatal Error while parsing XML: %1$s" }
  , { XMLError.PARSING_ERROR,                  "Error while parsing XML: %1$s" }
  , { XMLError.PARSING_WARNING,                "Warning while parsing XML: %1$s" }
  , { XMLError.PARSING_UNEXPECTED_TYPE,        "Unexpected event type [%1$s]; at line %1$s, column %2$s!" }
  , { XMLError.PARSING_UNEXPECTED_NODE,        "Unexpected node type [%1$s]; at line %1$s, column %2$s!" }
  , { XMLError.PARSING_UNEXPECTED_END,         "End of the document reached!" }
  , { XMLError.PARSING_DOCUMENT_END,           "Unexpected end of document when reading element text content!" }
  , { XMLError.PARSING_ELEMENT_START,          "Parser must be on START_ELEMENT to read next text; at line %1$s, column %2$s!" }
  , { XMLError.PARSING_ELEMENT_TEXT,           "Element text content may not contain START_ELEMENT; at line %1$s, column %2$s!" }
  , { XMLError.PARSING_ELEMENT_STARTEND,       "Parser must be on START_ELEMENT or END_ELEMENT; at line %1$s, column %2$s!" }

     // XML-00091 - XML-00100 parsing related errors
  , { XMLError.REQUIRED_EVENTTYPE_MISMATCH,    "Required type [%1$s], actual type [%2$s]; at line %3$s, column %4$s!" }
  , { XMLError.REQUIRED_LOCALNAME_EXPECTED,    "Required a non-null local name, but current token not a START_ELEMENT, END_ELEMENT or ENTITY_REFERENCE (was [%1$s]); at line %2$s, column %3$s!" }
  , { XMLError.REQUIRED_LOCALNAME_MISMATCH,    "Required local name [%1$s], current local name [%2$s]; at line %3$s, column %4$s!" }
  , { XMLError.REQUIRED_NAMESPACE_EXPECTED,    "Required non-null namespace URI, but current token not a START_ELEMENT or END_ELEMENT (was [%1$s]); at line %2$s, column %3$s!" }
  , { XMLError.REQUIRED_NAMESPACE_UNEXPECTED,  "Required empty namespace, instead have [%1$s]); at line %2$s, column %3$s!" }
  , { XMLError.REQUIRED_NAMESPACE_MISMATCH,    "Required namespace URI [%1$s]; have [%2$s]); at line %3$s, column %4$s!" }

     // XML-00101 - XML-00110 input processing related errors
  , { XMLError.INPUT_TRANSISTION,              "Encountered illegal state transition in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nInvalid state transition: [%3$s] -> [%4$s]!" }
  , { XMLError.INPUT_ELEMENT_UNKNOWN,          "Encountered illegal element in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nInvalid element: [%3$s]!" }
  , { XMLError.INPUT_ELEMENT_MISSING,          "Encountered missing element in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nElement: [%3$s] must be defined!" }
  , { XMLError.INPUT_ELEMENT_EMPTY,            "Encountered empty element in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nElement: [%3$s] must not be empty!" }
  , { XMLError.INPUT_ELEMENT_AMBIGUOUS,        "Encountered ambigously element in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nElement [%3$s] [%4$s] must not occur than once!" }
  , { XMLError.INPUT_ELEMENT_VALUE,            "Encountered illegal element value in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nInvalid value [%3$s] for element [%4$s]!" }
  , { XMLError.INPUT_ATTRIBUTE_UNKNOWN,        "Encountered illegal attribute in parser, suspect malformed XML.\nAt line %1$s, column %2$s: [%3$s]!" }
  , { XMLError.INPUT_ATTRIBUTE_MISSING,        "Encountered missing attribute in parser, suspect malformed XML.\nAt line %1$s, column %2$s:\nAttribute: [%3$s] must be defined!" }
  , { XMLError.INPUT_ATTRIBUTE_EMPTY,          "Encountered empty attribute in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nAttribute: [%3$s] must not be empty!" }
  , { XMLError.INPUT_ATTRIBUTE_AMBIGUOUS,      "Encountered ambigously element in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nAttribute: [%3$s] must not occur than once!" }
  , { XMLError.INPUT_ATTRIBUTE_VALUE,          "Encountered invalid attribute value in parser, suspect malformed XML.\nAt line %1$s, column %2$s.\nInvalid value [%3$s] for attribute [%4$s]!" }

     // XML-00111 - XML-00120 output processing related errors
  , { XMLError.OUTPUT_ROOT,                    "Root element required!" }
  , { XMLError.OUTPUT_NODE_NAME,               "Name of a node connecot be null or empty!" }
  , { XMLError.OUTPUT_NODE_INVALID,            "Either value or child nodes are allowed only!" }
  , { XMLError.OUTPUT_NODE_REMOVE,             "Cannot remove node [%1$s]!" }
  , { XMLError.OUTPUT_START_TAG,               "Start element required!" }

     // XML-00121 - XML-00130 output processing related errors
  , { XMLError.TRANSFORMATION_UNSUPPORTED,     "Transform of [%1$s] not supported!" }
  , { XMLError.TRANSFORMATION_LOCALE,          "Invalid locale [%1$s]!" }

     // XML-01001 - XML-01010 generic message template
  , { XMLMessage.MESSAGE,                      "%1$s" }
  , { XMLMessage.ELEMENT,                      "Element [%1$s]" }
  , { XMLMessage.ATTRIBUTE,                    "Attribute [%1$s]=[%2$s]" }

     // XML-01011 - XML-01020 processing messages
  , { XMLMessage.VALIDATING,                   "Validating XML descriptor" }
  , { XMLMessage.LOADING,                      "Loading ..." }
  , { XMLMessage.PROCESSING,                   "Processing ..." }

    // XML-01021 - XML-01030 parsing messages
  , { XMLMessage.EVENT_DISPATCH,               "Dispatching %1$s to state %2$s" }
  , { XMLMessage.EVENT_DISPATCH_ELEMENT,       "Dispatching %1$s to state %2$s (element: %3$s)" }
  , { XMLMessage.EVENT_UNKNOWN,                "Unknown" }
  , { XMLMessage.EVENT_DOCUMENT_START,         "Start document" }
  , { XMLMessage.EVENT_DOCUMENT_END,           "End document" }
  , { XMLMessage.EVENT_ELEMENT_START,          "Element %1$s start" }
  , { XMLMessage.EVENT_ELEMENT_END,            "Element %1$s end" }
  , { XMLMessage.EVENT_TEXT,                   "Text" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    XMLStreamBundle.class.getName()
  , Locale.getDefault()
  , XMLStreamBundle.class.getClassLoader()
  );

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  public Object[][] getContents() {
    return CONTENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a String from this {@link ListResourceBundle}.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                key into the resource array.
   **
   ** @return                    the String resource
   */
  public static String string(final String key) {
    return RESOURCE.getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument            the subsitution value for %1$s
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for %1$s
   ** @param  argument2           the subsitution value for %2$s
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for %1$s
   ** @param  argument2           the subsitution value for %2$s
   ** @param  argument3           the subsitution value for %3$s
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2, final Object argument3) {
    return RESOURCE.formatted(key, argument1, argument2, argument3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object[] arguments) {
    return RESOURCE.formatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringFormat
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String stringFormat(final String key, final Object... arguments) {
    return RESOURCE.stringFormatted(key, arguments);
  }
}