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

    System      :   Identity Service Library
    Subsystem   :   ZeRo Backend

    File        :   Helper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file contains various SQL helper methods.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-05-25  AFarkas     First release version
*/

package bka.iam.identity.helper;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidColumnException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcNoLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.tcResultSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.hst.platform.core.logging.Logger;

import oracle.iam.accesspolicy.vo.ChildAttribute;
import oracle.iam.accesspolicy.vo.Record;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.OIMInternalClient;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.vo.ApplicationInstance;

////////////////////////////////////////////////////////////////////////////////
// final class Helper
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** Contains various SQL helper methods.
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Helper {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String  NAME           = "SqlHelper";

  private static final String CLASS          = Helper.class.getName();
  private static final Logger LOGGER         = Logger.create(CLASS);
  
  private static final String lookupName     = "IDS.Configuration";
  private static final String lookupNameCode = "ignoredApplicationInstances";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Helper() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIgnoredApplications
  /**
   ** Helper method to allow using try-with-resource.
   **
   ** @param  client             the {@link OIMInternalClient} to use for lookup
   **                            operations.
   **                            <br>
   **                            Allowed object is {@link OIMInternalClient}.
   **
   ** @return                    the {@link List} of {@link String}s containing
   **                            list of application instances to ignore, trimmed.
   */
  public static List<String> getIgnoredApplications(OIMInternalClient client) {
    final String method = "getIgnoredApplications";
    LOGGER.entering(CLASS, method);
    
    List<String> ignoredAppList = new ArrayList<>();
    try {
      tcLookupOperationsIntf lookupService = client.getService(tcLookupOperationsIntf.class);
      tcResultSet values = lookupService.getLookupValues(lookupName);
      int j = 0;
      while (j < values.getRowCount()) {
        values.goToRow(j++);
        String key = values.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
        String decode = values.getStringValue("Lookup Definition.Lookup Code Information.Decode");
        if (lookupNameCode.equals(key)) {
          Arrays.asList(decode.split(",")).forEach(ignoredApp -> ignoredAppList.add(ignoredApp.trim()));
          break;
        }
      }
    }
    catch (tcAPIException | tcInvalidLookupException | tcColumnNotFoundException e) {
      LOGGER.throwing(CLASS, method, e);
    }
    catch (Exception e) {
      LOGGER.error(CLASS, method, "Exception caught: " + e.getMessage());
      LOGGER.throwing(CLASS, method, e);
    }
    LOGGER.exiting(CLASS, method, ignoredAppList);
    return (ignoredAppList);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUser
  /**
   ** Used to retrieve {@link User} object from {@link String} user login.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param client                    the client to use to retrieve the user. It's
   **                                  expected to be logged in.
   **                                  <br>
   **                                  Allowed object is {@link OIMInternalClient}.
   **                                  
   ** @param userNameOrId              the {@link String} representation of user name or
   **                                  user entity ID.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param isItUserName              indication whether user name (value "true")
   **                                  or id (value "false") is used.
   **                                  <br>
   **                                  Allowed object is {@link Boolean}.
   **                                  
   ** @return                          the retrieved {@link User} object for the input
   **                                  user name.
   **                                  <br>
   **                                  Possible object is {@link User}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public static User getUser(OIMInternalClient client, String userNameOrId, Boolean isItUserName)
    throws IllegalArgumentException {
    final String method = "getUser";
    LOGGER.entering(CLASS, method, "userNameOrId=", userNameOrId);
    try {
      UserManager userService = client.getService(UserManager.class);
      User user = userService.getDetails(userNameOrId, new HashSet<String>(), isItUserName);
      if (user == null) {
        throw new IllegalArgumentException();
      }
      LOGGER.exiting(CLASS, method, user);
      return user;
    } catch (NoSuchUserException e) {
      final StringBuilder message = new StringBuilder("User ").append(userNameOrId).append(" not found");
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } catch (UserLookupException e) {
      final StringBuilder message = new StringBuilder("User Lookup exception occurred: ");
      if (e.getCause() != null) {
        message.append(e.getCause().getMessage());
      } else {
        message.append(e.getMessage());
      }
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrieveAttributes
  /**
   ** Helper method to iterate through child attributes and retrieve name/value pairs.
   **
   ** @param  values             the array of {@link ChildAttribute} objects to
   **                            retrieve the attributes from.
   **                            <br>
   **                            Allowed object is array of {@link ChildAttribute}s.
   **
   ** @return                    the {@link Map} containing attribute name/value pairs.
   */
  public static Map<String, String> retrieveAttributes(ChildAttribute ... values) {
    final String method = "retrieveAttributes";
    LOGGER.entering(CLASS, method);
    
    Map<String, String> result = new HashMap<>();
    if (values != null) {
      for (ChildAttribute attr : values) {
        result.put(attr.getAttributeName(), attr.getAttributeValue());
      }
    }
    return result;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrieveAttributesFromRecords
  /**
   ** Helper method to iterate through records and retrieve name/value pairs.
   **
   ** @param  values             the array of {@link Record} objects to
   **                            retrieve the attributes from.
   **                            <br>
   **                            Allowed object is array of {@link Record}s.
   **
   ** @return                    the {@link Map} containing attribute name/value pairs.
   */
  public static Map<String, String> retrieveAttributes(Record ... values) {
    final String method = "retrieveAttributes";
    LOGGER.entering(CLASS, method);
    
    Map<String, String> result = new HashMap<>();
    if (values != null) {
      for (Record rec : values) {
        result.put(rec.getAttributeName(), rec.getAttributeValue());
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplicationInstance
  /**
   ** Used to retrieve and {@link ApplicationInstance} object based on the {@link String}
   ** application instance name.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param client                    the {@link OIMClient} object used to create
   **                                  and query the service. It's assumed to be
   **                                  logged in.
   **                                  <br>
   **                                  Allowed object is {@link OIMClient}.
   **                                  
   ** @param appInstanceName           the input {@link String} object identifying
   **                                  the name of the application instance to return.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the retrieved {@link ApplicationInstance} object
   **                                  or null if there is a problem.
   **                                  <br>
   **                                  Possible object is {@link ApplicationInstance}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public static ApplicationInstance getApplicationInstance(OIMClient client, String appInstanceName)
    throws IllegalArgumentException {
    final String method = "getApplicationInstance";
    LOGGER.entering(CLASS, method, "appInstanceName=", appInstanceName);
    try {
      ApplicationInstance appInstance = client.getService(ApplicationInstanceService.class).findApplicationInstanceByName(appInstanceName);
      LOGGER.trace(CLASS, method, "Application instance object: " + appInstance);
      LOGGER.exiting(CLASS, method, appInstance);
      return appInstance;
    } catch (ApplicationInstanceNotFoundException e) {
      final StringBuilder message = new StringBuilder("Application ").append(appInstanceName).append(" not found");
      LOGGER.error(CLASS, method, message.toString());
  //      LOGGER.throwing(CLASS, method, e);
  //      throw new IllegalArgumentException(message.toString(), e);
    } catch (GenericAppInstanceServiceException e) {
      final StringBuilder message = new StringBuilder("Generic Application Instance exception occurred ");
      if (e.getCause() != null) {
        message.append(e.getCause().getMessage());
      } else {
        message.append(e.getMessage());
      }
      LOGGER.error(CLASS, method, message.toString());
  //      LOGGER.throwing(CLASS, method, e);
  //      throw new IllegalArgumentException(message.toString(), e);
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertToCsv
  /**
   ** Converts {@link List} of {@link String}s to a comma-separated {@link String}
   ** of list elements.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param inputList                 the input {@link List} of {@link String}s
   **                                  to be converted.
   **                                  <br>
   **                                  Allowed object is {@link List} of
   **                                  {@link String}s.
   **                                  
   ** @return                          the constructed {@link String} CSV object.
   **                                  <br>
   **                                  Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public static String convertToCsv(List<String> inputList) {
    return convertToCsv("", inputList);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertToCsv
  /**
   ** Converts {@link List} of {@link String}s to a comma-separated {@link String}
   ** of list elements, where each of the elements is prefixed by a {@link String}
   ** constant.
   **
   ** @param prefix                    the input {@link List} of {@link String}s
   **                                  to be converted.
   **                                  <br>
   **                                  Allowed object is {@link List} of
   **                                  {@link String}s.
   **                                  
   ** @param inputList                 the input {@link List} of {@link String}s
   **                                  to be converted.
   **                                  <br>
   **                                  Allowed object is {@link List} of
   **                                  {@link String}s.
   **                                  
   ** @return                          the constructed {@link String} CSV object.
   **                                  <br>
   **                                  Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public static String convertToCsv(String prefix, List<String> inputList) {
    StringBuilder result = new StringBuilder();
    Iterator it = inputList.iterator();
    while (it.hasNext()) {
      result.append(prefix).append(it.next());
      if (it.hasNext()) {
        result.append(",");
      }
    }
    return result.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeLookupValue
  /**
   ** Method to identify the lookup associated with the input column name and return
   ** the decoded value based on input encoded value.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param client                    the {@link OIMClient} object used to create
   **                                  and query the service. It's assumed to be
   **                                  logged in.
   **                                  <br>
   **                                  Allowed object is {@link OIMClient}.
   **                                  
   ** @param columnName                the {@link String} column name used to retrieve
   **                                  the lookup.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param encodedValue              encoded value from the lookup.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the decoded value from the lookup associated with
   **                                  specified column name corresponding to the encoded
   **                                  value supplied or null if no such encoded value is
   **                                  present.
   **                                  <br>
   **                                  Possible object is {@link String}.
   **/
  public static String decodeLookupValue(OIMClient client, String columnName, String encodedValue) {
    final String method = "decodeLookupValue";
    LOGGER.entering(CLASS, method, "columnName=", columnName, "encodedValue=", encodedValue);
    
    String decodedValue = null;
    try {
      tcLookupOperationsIntf lookupService = client.getService(tcLookupOperationsIntf.class);
      String lookupName = lookupService.getLookupCodeForColumn(columnName);
      decodedValue = lookupService.getDecodedValueForEncodedValue(lookupName, encodedValue);
    } catch (tcAPIException | tcNoLookupException | tcInvalidColumnException e) {
      LOGGER.throwing(CLASS, method, e);
      return null;
    }
    LOGGER.exiting(CLASS, method, decodedValue);
    return decodedValue;
  }
  

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeLookupValue
  /**
   ** Method to identify the lookup associated with the input column name and return
   ** the decoded value based on input encoded value.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param client                    the {@link OIMClient} object used to create
   **                                  and query the service. It's assumed to be
   **                                  logged in.
   **                                  <br>
   **                                  Allowed object is {@link OIMClient}.
   **                                  
   ** @param columnName                the {@link String} column name used to retrieve
   **                                  the lookup.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param decodedValue              decoded value from the lookup.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the encoded value from the lookup associated with
   **                                  specified column name corresponding to the decoded
   **                                  value supplied or null if no such decoded value is
   **                                  present.
   **                                  <br>
   **                                  Possible object is {@link String}.
   **/
  public static String encodeLookupValue(OIMClient client, String columnName, String decodedValue) {
    final String method = "encodeLookupValue";
    LOGGER.entering(CLASS, method, "columnName=", columnName, "decodedValue=", decodedValue);
    
    String encodedValue = null;
    try {
      tcLookupOperationsIntf lookupService = client.getService(tcLookupOperationsIntf.class);
      String lookupName = lookupService.getLookupCodeForColumn(columnName);
      tcResultSet values = lookupService.getLookupValues(lookupName);
      int j = 0;
      while (j < values.getRowCount()) {
        values.goToRow(j++);
        if (values.getStringValue("Lookup Definition.Lookup Code Information.Decode").equals(decodedValue)) {
          encodedValue = values.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
          break;
        }
      }
    } catch (tcAPIException | tcNoLookupException | tcInvalidLookupException | tcInvalidColumnException | tcColumnNotFoundException e) {
      LOGGER.error(CLASS, method, "Exception caught: " + e.getMessage());
      return null;
    }
    LOGGER.exiting(CLASS, method, encodedValue);
    return encodedValue;
  }
}