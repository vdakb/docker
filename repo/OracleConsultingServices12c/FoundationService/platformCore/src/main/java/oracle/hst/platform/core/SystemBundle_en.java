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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   SystemBundle_en.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SystemBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class SystemBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OSF-00001 - 00010 system related errors
    { SystemError.UNHANDLED,                       "An unhandled exception has occured: %1$s" }
  , { SystemError.GENERAL,                         "General error: %1$s" }
  , { SystemError.ABORT,                           "Execution aborted due to reason: %1$s" }
  , { SystemError.NOTIMPLEMENTED,                  "Feature is not yet implemented." }
  , { SystemError.CLASSNOTFOUND,                   "Class %1$s was not found in the classpath." }
  , { SystemError.CLASSNOTCREATE,                  "Class %1$s has not been created." }
  , { SystemError.CLASSNOACCESS,                   "Access to class %1$s forbidden." }
  , { SystemError.CLASSINVALID,                    "Class %1$s must be a subclass of %s." }
  , { SystemError.CLASSMETHOD,                     "Class %1$s don't accept method parameter %2$s." }
  , { SystemError.CLASSCONSTRUCTORDEFAULT,         "Class %1$s don't have a no-args constructor." }

     // OSF-00011 - 00020 method argument related errors
  , { SystemError.ARGUMENT_IS_NULL,                "Passed argument %s must not be null." }
  , { SystemError.ARGUMENT_BAD_TYPE,               "Passed argument %s has a bad type." }
  , { SystemError.ARGUMENT_BAD_VALUE,              "Passed argument %s contains an invalid value." }
  , { SystemError.ARGUMENT_SIZE_MISMATCH,          "Passed argument array size doesn't match expected length." }

     // OSF-00021 - 00030 instance attribute related errors
  , { SystemError.ATTRIBUTE_IS_NULL,               "State of attribute %s must not be null." }
  , { SystemError.INSTANCE_STATE,                  "Invalide state of instance: Attribute %1$s already initialized."}

      // OSF-00031 - 00040 system property errors
  , { SystemError.PROPERTY_TYPE_INVALID,           "Cannot create a SystemProperty of type [%1$s]." }
  , { SystemError.PROPERTY_NAME_REQUIRED,          "The property name has not been set." }
  , { SystemError.PROPERTY_NAME_EXISTS,            "A property already exists with a name of [%1$s]." }
  , { SystemError.PROPERTY_DEFAULT_REQUIRED,       "The properties default value has not been set." }
  , { SystemError.PROPERTY_MINIMUM_VALUE,          "Configured value of [%1$s] is less than the minimum value of \"%2$d\" for the property [%3$s] - will use default value of [%4$s] instead." }
  , { SystemError.PROPERTY_MINIMUM_DEFAULT,        "The minimum value cannot be greater than the default value." }
  , { SystemError.PROPERTY_MINIMUM_COMPARABLE,     "A minimum value can only be applied to properties that implement Comparable." }
  , { SystemError.PROPERTY_MAXIMUM_VALUE,          "Configured value of [%1$s] is more than the maximum value of \"%2$d\" for the property [%3$s] - will use default value of [%4$s] instead." }
  , { SystemError.PROPERTY_MAXIMUM_DEFAULT,        "The maximum value cannot be less than the default value." }
  , { SystemError.PROPERTY_MAXIMUM_COMPARABLE,     "A maximum value can only be applied to properties that implement Comparable." }

    // OSF-00041 - 00050 reflection/inrospection errors
  , { SystemError.INTROSPECT_EXCEPTION_IGNORE,     "Ignoring thrown exception; the sole intent is to return null instead.\n%1$s" }

     // OSF-00051 - 00060 security context related errors
  , { SystemError.SECURITY_INITIALIZE,             "Error initializing key manager factory (operation failed)." }
  , { SystemError.SECURITY_UNRECOVERABLE,          "Error initializing key manager factory (unrecoverable key)." }
  , { SystemError.SECURITY_PROVIDER,               "Error initializing key store (provider not registered)." }
  , { SystemError.SECURITY_ALGORITHM,              "Error initializing key manager factory (algorithm not supported)." }
  , { SystemError.SECURITY_KEYSTORE_PASSWORD,      "Neither key password nor key store password has been set for %1$s key store.\nIgnoring the key store configuration and skipping the key manager factory initialization.\nKey manager factory will not be configured in the current SSL context." }

     // OSF-00061 - 00070 trusted key store related errors
  , { SystemError.TRUSTED_IMPLEMENTATION,          "Error initializing Trusted Store (implementation [%1$s] not available)." }
  , { SystemError.TRUSTED_PROVIDER,                "Error initializing Trusted Store (provider [%1$s] not registered)." }
  , { SystemError.TRUSTED_ALGORITHM,               "Error initializing Trusted Store (algorithm [%1$s] to check key store integrity not found)." }
  , { SystemError.TRUSTED_FILE_NOTFOUND,           "Cannot find Trusted Store file [%1$s]." }
  , { SystemError.TRUSTED_FILE_NOTLOADED,          "Error loading Trusted Store from file [%1$s]." }
  , { SystemError.TRUSTED_CERT_NOTLOADED,          "Cannot load Trusted Store certificates." }

     // OSF-00071 - 00080 identity key store related errors
  , { SystemError.IDENTITY_IMPLEMENTATION,         "Error initializing Identity Store (implementation [%1$s] not available)." }
  , { SystemError.IDENTITY_PROVIDER,               "Error initializing Identity Store (provider [%1$s] not registered)." }
  , { SystemError.IDENTITY_ALGORITHM,              "Error initializing Identity Store (algorithm [%1$s] to check key store integrity not found)." }
  , { SystemError.IDENTITY_FILE_NOTFOUND,          "Cannot find Identity Store file [%1$s]." }
  , { SystemError.IDENTITY_FILE_NOTLOADED,         "Error loading Identity Store from file [%1$s]." }
  , { SystemError.IDENTITY_CERT_NOTLOADED,         "Cannot load Identity Store certificates." }

     // OSF-00081 - 00090 configuration related errors
  , { SystemError.PROPERTY_REQUIRED,               "The configuration property [%1$s] is required." }

     // OSF-00091 - 00090 connectivity errors
  , { SystemError.CONNECTION_UNKNOWN_HOST,         "Host to Service Provider Server %1$s is unknown." }
  , { SystemError.CONNECTION_CREATE_SOCKET,        "Could create network connection to host [%1$s] on port [%2$s]." }
  , { SystemError.CONNECTION_SECURE_SOCKET,        "Could create secure network connection to host [%1$s] on port [%2$s]." }
  , { SystemError.CONNECTION_CERTIFICATE_PATH,     "Unable to find valid certification path to requested target [%1$s]." }
  , { SystemError.CONNECTION_ERROR,                "Error encountered while connecting to Service Provider." }
  , { SystemError.CONNECTION_TIMEOUT,              "Connection to Service Provider got timed out; [%1$s]." }
  , { SystemError.CONNECTION_UNAVAILABLE,          "The problem may be with physical connectivity or Service Provider is not alive." }
  , { SystemError.CONNECTION_AUTHENTICATION,       "Principal Name [%1$s] or Password is incorrect, system failed to get access with supplied credentials." }
  , { SystemError.CONNECTION_AUTHORIZATION,        "Principal Name [%1$s] is not authorized." }
  , { SystemError.CONNECTION_ENCODING_UNSUPPORTED, "URL Encoding [%1$s] not supported." }

     // OSF-00101 - 00110 path parsing errors
  , { SystemError.PATH_UNEXPECTED_EOF_STRING,      "Unexpected end of path string." }
  , { SystemError.PATH_UNEXPECTED_EOF_FILTER,      "Unexpected end of filter string." }
  , { SystemError.PATH_UNEXPECTED_CHARACTER,       "Unexpected character '%s' at position %d for token starting at %d." }
  , { SystemError.PATH_UNRECOGNOIZED_OPERATOR,     "Unrecognized attribute operator '%s' at position %d. Expected: eq,ne,co,sw,ew,pr,gt,ge,lt,le!" }
  , { SystemError.PATH_INVALID_FILTER,             "Invalid value filter: %s!" }
  , { SystemError.PATH_EXPECT_PARENTHESIS,         "Expected '(' at position %d!" }
  , { SystemError.PATH_INVALID_PARENTHESIS,        "No opening parenthesis matching closing parenthesis at position %d!" }
  , { SystemError.PATH_EXPECT_ATTRIBUTE_PATH,      "Attribute path expected at position %d!" }
  , { SystemError.PATH_EXPECT_ATTRIBUTE_NAME,      "Attribute name expected at position %d!" }
  , { SystemError.PATH_INVALID_ATTRIBUTE_PATH,     "Invalid attribute path at position %d: %s!" }
  , { SystemError.PATH_INVALID_ATTRIBUTE_NAME,     "Invalid attribute name starting at position %d: %s!" }
  };

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
}
