package bka.iam.identity.scim.extension.exception.resource;

import bka.iam.identity.scim.extension.exception.ScimMessage;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

public class ScimBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
  // 00001 - 00100 General SCIM related errors
    { ScimMessage.GENERAL,                          "General Error"},
    { ScimMessage.SCHEMA_NOTFOUND,                  "Provided schema %1$s not found."},
    { ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND,        "Attribute %1$s not defined in the schema."},
    { ScimMessage.ATTRIBUTE_MANDATORY,              "Attribute %1$s is mandatory"},
    { ScimMessage.INVALID_JSON_STRUCTURE_ARRAY,     "Invalid JSON structure: missing or invalid '%1$s' array"},
    { ScimMessage.INCORECT_SCHEMA,                  "Incorect schema: %1$s"},
    { ScimMessage.INCORECT_ENTITLEMENT_ACTION,      "Entitlement action is not from supported lists of values [assign|modify|revoke]."},
    { ScimMessage.ATTRIBUTE_EXPECTED_SINGLE_VALUE,  "Attribute '%1$s' expected to be a single value"},
    { ScimMessage.ATTRIBUTE_EXPECTED_MULTI_VALUE,   "Attribute '%1$s' expected to be a multi value"},
    { ScimMessage.ATTRIBUTE_TYPE_MISMATCH,          "Attribute '%1$s' expected to be a '%2$s'"},
    { ScimMessage.SUBATTRIBUTE_NOT_FOUND,           "Attribute '%1$s' should contains sub attribute"},
    { ScimMessage.INVALID_QUERY_PARAMETERS,         "Invalid combination of query parameters"},
    { ScimMessage.INVALID_STARTINDEX,               "The 'startIndex' query parameter exceeds the number of available resources."},
    { ScimMessage.DUPLICATE_CANONICAL_VALUE,        "Duplicate [%1$s] with value [%2$s] on attribute [%3$s] is not allowed."},
    { ScimMessage.VALUE_NOT_ALLOWED,                "Value [%1$s] for attribute [%2$s] not allowed."},
    { ScimMessage.VALUE_ALREADY_EXIST,              "The [%1$s] attribute already exists. Cannot be added."},
  
  // 00101 - 00200 User related errors
    { ScimMessage.USER_NOTFOUND,                    "Cannot find user with key(s) [%1$s]"},
    { ScimMessage.USER_NOT_AUTHORIZED,              "You do not have permission to operate on this user."},
  
  // 00201 - 00300 Group related errors
    { ScimMessage.GROUP_NOTFOUND,                   "Cannot find group with key(s) [%1$s]"},
    { ScimMessage.GROUP_CANNOT_DELETE,              "Cannot delete group with key(s) [%1$s]"},
    { ScimMessage.GROUP_OPERATION_ONLY_MEMBER,      "You are not allowed to modify a group resource except the member attribute"},
    
  // 00301 - 00310 Application related errors
    { ScimMessage.APPLICATION_NOTFOUND,             "Cannot find Application with key [%1$s]"},
    { ScimMessage.NAMESPACE_NOTFOUND,               "Cannot find namespace with key [%1$s]"},
    { ScimMessage.NAMESPACE_ENTITLEMENT_NOTFOUND,   "Cannot find entitlement with key [%2$s] in namespace [%1$s]"},
    
  // 00401 - 00410 Application related errors
    { ScimMessage.ENTITLEMENT_NOTFOUND,             "Cannot find Entitlement with key [%1$s]"},
  
  // 00301 - 00400 Policy related errors
    { ScimMessage.POLICY_NOTFOUND,                  "Cannot find policy with key(s) [%1$s]"},
    { ScimMessage.POLICY_CANT_REMOVE_ALL,           "Cannot remove all access policy elements from an access policy (id: [%1$s])."},
    
  // 01001 - 01100 Get Operation related errors
    { ScimMessage.GET_RESOURCE_FAILED,              "Resource with key."},
  
  // 01201 - 01200 Patch Operation related errors
    { ScimMessage.REPLACE_RESOURCE_FAILED,          "Patch request failed"},
    { ScimMessage.PATCH_ATTRIBUTE_MISSING,          "Invalid Patch request. Attribute %1$s is mandatory."},
    { ScimMessage.PATCH_UNSUPPORTED_OPERATION,      "Patch unsupported operation: $1s. Supported operations are [add|replace|remove]."},
    { ScimMessage.PATCH_DUPLICATE_VALUE,            "Attribute already exists at $1s. Use 'replace' instead of 'add'."},
    { ScimMessage.PATCH_PATH_NOT_EXIST,             "Path $1s does not exist or is read only."},
    { ScimMessage.PATCH_RESOURCE_WIHTOUT_ATTRIBUTE, "Resource must contain a list of one or more attribute"},
  
  // 01301 - 01300 Post Operation related errors 
    { ScimMessage.CREATE_RESOURCE_FAILED,           "Create request failed"},
  
  // 01401 - 01400 Put Operation related errors
    { ScimMessage.MODIFY_RESOURCE_FAILED,           "Modify request failed"},
  
  // 01501 - 01500 Delete Operation related errors
    { ScimMessage.DELETE_RESOURCE_FAILED,           "Delete request failed"}
  };
  
  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    ScimBundle.class.getName()
  , Locale.getDefault()
  , ScimBundle.class.getClassLoader()
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
   ** @param  argument            the subsitution value for <code>%1$s</code>.
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
   ** @param  argument1           the subsitution value for <code>%1$s</code>.
   ** @param  argument2           the subsitution value for <code>%2$s</code>.
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
   ** @param  argument1           the subsitution value for <code>%1$s</code>.
   ** @param  argument2           the subsitution value for <code>%2$s</code>.
   ** @param  argument3           the subsitution value for <code>%3$s</code>.
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
