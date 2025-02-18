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
    Subsystem   :   Foundation Shared Library

    File        :   LoggingBundle.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LoggingBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemMessage;

////////////////////////////////////////////////////////////////////////////////
// class SystemBundle
// ~~~~~ ~~~~~~~~~~~~
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
 ** @since   1.0.0.0
 */
public class SystemBundle extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // OCF-00001 - 00010 system related errors
    { SystemError.UNHANDLED,                       "An unhandled exception has occured: %1$s"}
  , { SystemError.GENERAL,                         "General error: %1$s" }
  , { SystemError.ABORT,                           "Execution aborted due to reason: %1$s"}
  , { SystemError.NOTIMPLEMENTED,                  "Feature is not yet implemented."}
  , { SystemError.CLASSNOTFOUND,                   "Class %1$s was not found in the classpath."}
  , { SystemError.CLASSNOTCREATE,                  "Class %1$s has not been created."}
  , { SystemError.CLASSINVALID,                    "Class %1$s must be a subclass of %2$s."}
  , { SystemError.CLASSMETHOD,                     "Class %1$s don't accept method parameter %2$s."}

     // OCF-00011 - 00020 method argument related errors
  , { SystemError.ARGUMENT_IS_NULL,                "Passed argument %1$s must not be null." }
  , { SystemError.ARGUMENT_BAD_TYPE,               "Passed argument %1$s has a bad type." }
  , { SystemError.ARGUMENT_BAD_VALUE,              "Passed argument %1$s contains an invalid value." }
  , { SystemError.ARGUMENT_SIZE_MISMATCH,          "Passed argument array size doesn't match expected length." }

     // OCF-00021 - 00030 instance attribute lated errors
  , { SystemError.ATTRIBUTE_IS_NULL,               "State of attribute %1$s must not be null." }
  , { SystemError.INSTANCE_STATE,                  "Invalide state of instance: Attribute %1$s already initialized."}

     // OCF-00031 - 00040 schema entry and attribute related errors
  , { SystemError.SCHEMA_ATTRIBUTE_IS_NULL,        "Attribute %1$s cannot not be null." }
  , { SystemError.SCHEMA_ATTRIBUTE_IS_BLANK,       "Attribute %1$s cannot not be blank." }
  , { SystemError.SCHEMA_ATTRIBUTE_SINGLE,         "Attribute %1$s cannot have multiple values." }
  , { SystemError.SCHEMA_ATTRIBUTE_TYPE,           "Expected that value of attribute [%1$s] will be of [%2$s], but it was [%3$s]." }
  , { SystemError.SCHEMA_NAME_ENTRY_MISSING,       "No name attribute provided in the attributes." }

     // OCF-00041 - 00050 filter related errors
  , { SystemError.FILTER_ATTRIBUTE_COMPARABLE,     "Filter attribute must be comparable." }
  , { SystemError.FILTER_INCONSISTENT_METHOD,      "Translation method is inconsistent: [%1$s]." }
  , { SystemError.FILTER_INCONSISTENT_EXPRESSION,  "Translation expresssion is inconsistent: [%1$s]." }

     // OCF-00051 - 00060 security context related errors
  , { SystemError.SECURITY_INITIALIZE,             "Error initializing key manager factory (operation failed)." }
  , { SystemError.SECURITY_UNRECOVERABLE,          "Error initializing key manager factory (unrecoverable key)." }
  , { SystemError.SECURITY_PROVIDER,               "Error initializing key store (provider not registered)." }
  , { SystemError.SECURITY_ALGORITHM,              "Error initializing key manager factory (algorithm not supported)." }
  , { SystemError.SECURITY_KEYSTORE_PASSWORD,      "Neither key password nor key store password has been set for %1$s key store.\nIgnoring the key store configuration and skipping the key manager factory initialization.\nKey manager factory will not be configured in the current SSL context." }

     // OCF-00061 - 00070 trusted key store related errors
  , { SystemError.TRUSTED_IMPLEMENTATION,          "Error initializing Trusted Store (implementation [%1$s] not available)." }
  , { SystemError.TRUSTED_PROVIDER,                "Error initializing Trusted Store (provider [%1$s] not registered)." }
  , { SystemError.TRUSTED_ALGORITHM,               "Error initializing Trusted Store (algorithm [%1$s] to check key store integrity not found)." }
  , { SystemError.TRUSTED_FILE_NOTFOUND,           "Cannot find Trusted Store file [%1$s]." }
  , { SystemError.TRUSTED_FILE_NOTLOADED,          "Error loading Trusted Store from file [%1$s]." }
  , { SystemError.TRUSTED_CERT_NOTLOADED,          "Cannot load Trusted Store certificates." }

     // OCF-00071 - 00080 identity key store related errors
  , { SystemError.IDENTITY_IMPLEMENTATION,         "Error initializing Identity Store (implementation [%1$s] not available)." }
  , { SystemError.IDENTITY_PROVIDER,               "Error initializing Identity Store (provider [%1$s] not registered)." }
  , { SystemError.IDENTITY_ALGORITHM,              "Error initializing Identity Store (algorithm [%1$s] to check key store integrity not found)." }
  , { SystemError.IDENTITY_FILE_NOTFOUND,          "Cannot find Identity Store file [%1$s]." }
  , { SystemError.IDENTITY_FILE_NOTLOADED,         "Error loading Identity Store from file [%1$s]." }
  , { SystemError.IDENTITY_CERT_NOTLOADED,         "Cannot load Identity Store certificates." }

     // OCF-00081 - 00090 configuration related errors
  , { SystemError.PROPERTY_REQUIRED,               "The configuration property [%1$s] is required." }

     // OCF-00091 - 00090 connectivity errors
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

     // OCF-00101 - 00110 operational state errors
  , { SystemError.OBJECT_CLASS_UNSUPPORTED,        "Connector unwilling to perform: [%1$s] is not supported for operation [%2$s]." }
  , { SystemError.OBJECT_CLASS_REQUIRED,           "Connector unwilling to perform: ObjectClass is missing for operation." }
  , { SystemError.OBJECT_VALUES_REQUIRED,          "Connector unwilling to perform: Attribute values required for operation." }
  , { SystemError.UNIQUE_IDENTIFIER_REQUIRED,      "Connector unwilling to perform: Unique Identifier is missing for operation." }
  , { SystemError.NAME_IDENTIFIER_REQUIRED,        "Connector unwilling to perform: [%1$s] Identifier required for operation." }

     // OCF-01001 - 01010 attribute mapping related messages
  , { SystemMessage.ATTRIBUTE_NAME,                "Name"}
  , { SystemMessage.ATTRIBUTE_VALUE,               "Value" }
  , { SystemMessage.ATTRIBUTE_MAPPING,             "\nAttributes:\n-----------\n%1$s" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    SystemBundle.class.getName()
  , Locale.getDefault()
  , SystemBundle.class.getClassLoader()
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
  @Override
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
  // Method:   string
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
  public static String string(final String key, final Object... arguments) {
    return RESOURCE.stringFormat(key, arguments);
  }
}