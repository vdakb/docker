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

    Copyright 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   EFBS Connector

    File        :   EFBSMultiValueHandler.java

    Compiler    :   JDK 1.8

    Author      :   richard.x.rutter@oracle.com

    Purpose     :   The EFBS requirement is for a user's telephone number and 
                    email to be provisioned to EFBS. In EFBS these are 
                    single-valued although they are modelled in the SCIM 
                    specification as multi-valued which means they must be 
                    modelled as child table entries in OIM in order for the Oracle 
                    Generic SCIM connector to be used. This presents the first
                    difficulty and the purpose of this adapter class: how to 
                    map the user's email and phone numbers, which are stored 
                    on the user profile as single valued attributes, to
                    child table entries? This is achieved by modelling the 
                    single valued email and phone numbers as entries on the 
                    connector parent process form and by linking them to
                    the values on the user profile using 
                    'Lookup.USR_PROCESS_TRIGGERS' entries and 'Change XXX' 
                    type process tasks in the normal manner. Standard 
                    'XXX Updated' process tasks are then used to map the entries 
                    on the process form to child table entries. These tasks are
                    integrated with task adapters which utilise the 
                    processChildTableChanges method of this class to 
                    manage the child table entries.

The fact that 
                    these attributes appear on the parent form and in child 
                    form entries does not affect provisioning which is 
                    ultimately governed by the EFBS SCIM schema in terms of 
                    which attributes values are provisioned, the source 
                    (parent or child process form entries) and the form of the
                    resulting SCIM request. 
                    
                    Notes:
 
                    1. This adapter is not triggered during create. Refer to 
                    class EFBSConnector for details of how email and telephone 
                    number are managed during create.

                    2. The adapter uses 2 deprecated methods of OIM legacy API
                    in order to manage child tables. There are no alternative
                    methods in the current OIM API but the information obtained 
                    through these APIs could be obtained directly from the OIM
                    DB should these methods ever disappear without being replaced.

                    3. One quirk of the target SCIM service from Rola is that is 
                    does not support SCIM PATCH remove operations for multi-valued 
                    entries, just add and replace. This has implications for the 
                    OIM Generic SCIM connector which unfortunately implements child
                    table entry removal as a SCIM PATCH remove operation. So, task 
                    adapters triggered by remove child table entry events cannot 
                    result in SCIM requests for remove. The solution to this has been
                    to implement remove child table entry events as 'no-ops', 
                    removing the child table entries in OIM but not making any 
                    request to the SCIM service. The EFBS requirement is that a 
                    user should have one and only one child table entry so, 
                    accordingly, this adapter is triggered on a child table entry 
                    add event the result of such an event is a SCIM PATCH replace 
                    operation whereby the newly added entry replaces any/all 
                    previous entries.  

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      01.11.2018  rirutter    First release version
    1.0.0.1      15.02.2019  rirutter    Updated to incorporate changes to 
                                         work around Rola SCIM service failure
                                         to support SCIM PATCH remove.
*/
package bka.iam.identity.scim.adapter;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcFormNotFoundException;
import Thor.API.Exceptions.tcProcessNotFoundException;
import Thor.API.Exceptions.tcNotAtomicProcessException;
import Thor.API.Exceptions.tcVersionNotFoundException;
import Thor.API.Exceptions.tcVersionNotDefinedException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcFormEntryNotFoundException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcRequiredDataMissingException;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.identity.foundation.provisioning.AbstractProvisioningTask;

////////////////////////////////////////////////////////////////////////////////
// class EFBSMultiValueHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** OIM process task adapter.
 ** <br>
 ** This adapter will be triggered when a field on the process form changes
 ** where the field denotes a value for a multi-valued attribute for SCIM i.e.
 ** email or telephone number. For the EFBS SCIM connector, the user has single
 ** values for email and telephone number but the SCIM interface specification
 ** only provides for multi-values for these fields. In OIM when using the OIM
 ** Generic SCIM connector, such fields are modelled using child tables so
 ** custom code and configuration is required to map data from the user profile
 ** to these child tables.
 ** <br>
 ** Thankfully, the data does not have to be mapped back to the user profile
 ** following reconciliation.
 **
 ** @author  richard.x.rutter@oracle.com
 ** @version 1.0.0.1
 ** @since   1.0.0.0
 */
public class EFBSMultiValueHandler extends AbstractProvisioningTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the category of the logging facility to use
  private static final String LOGGER_CATEGORY  = EFBSMultiValueHandler.class.getName();
  private static final String RESPONSE_ERROR   = "ERROR";   // response to return on error for process task integration
  private static final String RESPONSE_SUCCESS = "SUCCESS"; // response to return on successful completion for process task integration

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** EFBSMultiValueHandler constructor to set uo logging and ref for API
   ** access.
   **
   ** @param  provider           the OIM reference to enable generic API access.
   */
  public EFBSMultiValueHandler(tcDataProvider provider) {
    // ensure inheritance
    super(provider, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processChildTableChanges
 /**
   ** Called when the value on the parent process form changes.
   **
   ** @param  newValue           the modified value to be processed.
   ** @param  processKey         the key of the processs instance for obtaining
   **                            and updating data.
   ** @param  tableName          which child table to update.
   ** @param  columnName         which child table column to update.
   **
   ** @return                    an appropiate response code.
   */
  // childTableName = UD_EFBS_M or UD_EFBS_PN
  // childTableValueColumnName = UD_EFBS_M_VALUE or UD_EFBS_PN_VALUE
  public String processChildTableChanges(final String newValue, final long processKey, final String tableName, final String columnName) {
    final String methodName = "processChildTableChanges";
    String       response   = RESPONSE_SUCCESS;
    debug(methodName, "BEGIN, newValue=" + newValue + ", processKey=" + processKey);

    tcFormInstanceOperationsIntf formInstanceAPI = formInstanceFacade();
    try {
      long        parentProcessFormDefinitionKey = formInstanceAPI.getProcessFormDefinitionKey(processKey);
      int         processFormVersion             = formInstanceAPI.getProcessFormVersion(processKey);
      tcResultSet childProcessFormsDefinition    = formInstanceAPI.getChildFormDefinition(parentProcessFormDefinitionKey, processFormVersion);
      if (logger().debugLevel()) {
        debug(methodName, "Child form definitions=" + getLogString(childProcessFormsDefinition));
      }
      String childProcessFormDefinitionKeyString = null;
      int    numChildForms = childProcessFormsDefinition.getRowCount();
      // there should be 2 child forms, 1 for email and the other for telephone number
      if (numChildForms == 2) {
        for (int i = 0; i < numChildForms; i++) {
          childProcessFormsDefinition.goToRow(i);
          String foundChildTableName = childProcessFormsDefinition.getStringValue("Structure Utility.Table Name");
          if (foundChildTableName.equals(tableName.trim())) {
            debug(methodName, "Found matching child table" + foundChildTableName);
            childProcessFormDefinitionKeyString = childProcessFormsDefinition.getStringValue("Structure Utility.Child Tables.Child Key");
            break;
          }
          else {
            continue;
          }
        }
        if (childProcessFormDefinitionKeyString != null) {
          debug(methodName, "childProcessFormDefinitionKeyString=" + childProcessFormDefinitionKeyString);
          long childProcessFormDefinitionKey = Long.parseLong(childProcessFormDefinitionKeyString);
          /*
           * Check the child table entries and align with the parent process form i.e. a single entry
           * corresponding to the entry on the parent process form or no entries if that is empty or null
           */
          HashMap<String, Long> recordToReplace                 = new HashMap<String, Long>();
          tcResultSet           tcresultSetProcessFormChildData = formInstanceAPI.getProcessFormChildData(childProcessFormDefinitionKey, processKey);
          if (logger().debugLevel()) {
            debug(methodName, "tcresultSetProcessFormChildData=" + getLogString(tcresultSetProcessFormChildData));
          }
          // starting point is we need to add a new entry, unless we find it already exists in
          // the child tables
          boolean addNew = true;
          // now remove the old and add the new one if not already there
          for (int i = 0; i < tcresultSetProcessFormChildData.getRowCount(); i++) {
            tcresultSetProcessFormChildData.goToRow(i);
            String existingValue = tcresultSetProcessFormChildData.getStringValue(columnName);
            // check there is a valid new value to add and that it is not already present on the child form
            // (perhaps through reconciliation or manual addition)
            if (
              // newValue == null || newValue.isEmpty() || RR - allow setting empty values
              (existingValue != null && !existingValue.isEmpty() && (newValue.trim().equals(existingValue.trim())))) {
              debug(methodName, "found existing child table entry for new value, continuing" + existingValue);
              addNew = false;
            }
            else {
              debug(methodName, "found existing child table entry different to new email value. Will remove entry from child table. existingValue=" + existingValue);
              Long childFormPrimaryKey = tcresultSetProcessFormChildData.getLongValue(tableName + "_KEY");
              recordToReplace.put(existingValue, childFormPrimaryKey);
            }
          }
          // now remove all the extra email addresses child form entries
          // note these are implemented as a no-op i.e. the entry will be removed
          // but this will not trigger a SCIM request to ROLA service because
          // this will fail because ROLA doesn't support remove patch op.
          // So just remove the child table entry to clean up OIM record and
          // the new value will be added as a SCIM replace op which ROLA does support.
          if (!recordToReplace.isEmpty()) {
            debug(methodName, "replacing child table entries=" + recordToReplace);
            Iterator<Map.Entry<String, Long>> i = recordToReplace.entrySet().iterator();
            while (i.hasNext()) {
              Map.Entry<String, Long> valueToReplace = i.next();
              HashMap<String, String> valueData      = new HashMap<String, String>();
              // Row [0:UD_EFBS_M_VALUE =junit_bp@rola.com, UD_EFBS_M_VALUE =junit_bp@rola.com, UD_EFBS_M_CREATE =2018-09-20, UD_EFBS_M_CREATEBY =4, UD_EFBS_M_DATA_LEVEL =, UD_EFBS_M_KEY =61, UD_EFBS_M_NOTE =, UD_EFBS_M_REVOKE =, UD_EFBS_M_ROWVER =^@^@^@^@^@^@^@^@, UD_EFBS_M_UPDATE =, UD_EFBS_M_UPDATEBY =, UD_EFBS_M_VERSION =0, Process Instance.Key =341, Access Policies.Key =, REQUEST_KEY =
              valueData.put(columnName, newValue != null ? newValue.trim() : ""); // TODO SCIM schema requires a value email/telephone, but could be empty, TBC
              try {
                debug(methodName, "replacing child table entry=" + valueToReplace + ". Note this does not send remove request because this is not supported by the Rola SCIM service.");
                formInstanceAPI.updateProcessFormChildData(childProcessFormDefinitionKey, valueToReplace.getValue().longValue(), valueData);
              }
              catch (tcInvalidValueException e) {
                logger().error(methodName + " Failed to remove child form entry because entry not found, continuing. childProcessFormDefinitionKey=" + childProcessFormDefinitionKey + ", valueToRemove=" + recordToReplace, e);
              }
              catch (tcFormEntryNotFoundException e) {
                logger().error(methodName + " Failed to remove child form entry because entry not found, continuing. childProcessFormDefinitionKey=" + childProcessFormDefinitionKey + ", valueToRemove=" + recordToReplace, e);
              }
            }
          }
          // now add the new child table entry
          if (addNew) {
            HashMap<String, String> valueToAdd = new HashMap<String, String>();
            // Row [0:UD_EFBS_M_VALUE =junit_bp@rola.com, UD_EFBS_M_VALUE =junit_bp@rola.com, UD_EFBS_M_CREATE =2018-09-20, UD_EFBS_M_CREATEBY =4, UD_EFBS_M_DATA_LEVEL =, UD_EFBS_M_KEY =61, UD_EFBS_M_NOTE =, UD_EFBS_M_REVOKE =, UD_EFBS_M_ROWVER =^@^@^@^@^@^@^@^@, UD_EFBS_M_UPDATE =, UD_EFBS_M_UPDATEBY =, UD_EFBS_M_VERSION =0, Process Instance.Key =341, Access Policies.Key =, REQUEST_KEY =
            valueToAdd.put(columnName, newValue != null ? newValue.trim() : ""); // TODO SCIM schema requires a value email/telephone, but could be empty, TBC
            try {
              debug(methodName, "adding child table entry for new valueToAdd=" + valueToAdd);
              formInstanceAPI.addProcessFormChildData(childProcessFormDefinitionKey, processKey, valueToAdd);
            }
            catch (tcInvalidValueException | tcRequiredDataMissingException e) {
              logger().error(methodName + " Failed to add child form entry but entry not found, continuing. childProcessFormDefinitionKey=" + childProcessFormDefinitionKey + ", valueToAdd=" + valueToAdd, e);
              response = RESPONSE_ERROR;
            }
          }
        }
        else {
          // should not happen unless task has been configured with invalid child table name
          error(methodName, "Invalid child table name, unable to process changes. childTableName=" + tableName);
          response = RESPONSE_ERROR;
        }
      }
      else {
        // should not happen, system should be configured for eFBS Connector to have 2 child forms, one for email and the
        // other for telephone number
        error(methodName, "Invalid child forms configured, unable to process changes. There should be 2 child forms, one for email and one for phone number. Found numChildForms=" + numChildForms);
        response = RESPONSE_ERROR;
      }
    }
    catch (tcColumnNotFoundException e) {
      logger.error(methodName + " Invalid table metadata. Unable to process changes. childTableName = " + tableName + ", childTableValueColumnName=" + columnName + ", processKey=" + processKey, e);
      response = RESPONSE_ERROR;
    }
    catch (tcVersionNotDefinedException e) {
      logger.error(methodName + " Invalid table version. Unable to process changes. childTableName = " + tableName + ", childTableValueColumnName=" + columnName + ", processKey=" + processKey, e);
      response = RESPONSE_ERROR;
    }
    catch (tcVersionNotFoundException e) {
      logger.error(methodName + " Invalid table version. Unable to process changes. childTableName = " + tableName + ", childTableValueColumnName=" + columnName + ", processKey=" + processKey, e);
      response = RESPONSE_ERROR;
    }
    catch (tcNotAtomicProcessException e) {
      logger.error(methodName + " Unable to process changes. childTableName = " + tableName + ", childTableValueColumnName=" + columnName + ", processKey=" + processKey, e);
      response = RESPONSE_ERROR;
    }
    catch (tcProcessNotFoundException e) {
      logger.error(methodName + " Unable to process changes. processKey=" + processKey, e);
      response = RESPONSE_ERROR;
    }
    catch (tcFormNotFoundException e) {
      logger.error(methodName + " Unable to process changes. childTableName = " + tableName + ", childTableValueColumnName=" + columnName + ", processKey=" + processKey, e);
      response = RESPONSE_ERROR;
    }
    catch (tcAPIException e) {
      logger.error(methodName + " Unable to process changes. processKey=" + processKey, e);
      response = RESPONSE_ERROR;
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogString
  /**
   ** Utility method to translate an OIM {@link tcResultSet} object into a
   ** printable string.
   **
   ** @param  resultSet          the {@link tcResultSet} object to convert to a
   **                            string.
   **
   ** @return                    a string representation of the contents of the
   **                            ResultSet
   */
  String getLogString(final tcResultSet resultSet) {
    final String        method = "getLogString";
    final StringBuilder buffer = new StringBuilder();
    try {
      if (resultSet != null && resultSet.getRowCount() > 0) {
        final String[] columnName = resultSet.getColumnNames();
        for (int i = 0; i < resultSet.getRowCount(); i++) {
          resultSet.goToRow(i);
          buffer.append(getLogString(columnName, resultSet, i));
        }
      }
      else {
        buffer.append("Null or empty result set");
      }
    }
    catch (Throwable t) {
      logger().error(method, t);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogString
  /**
   ** Utility Utility method to translate a row of an OIM {@link tcResultSet}
   ** object into a printable string.
   **
   ** @param  columnName         the names of the columns in the psecified
   **                            {@link tcResultSet}.
   ** @param  resultSet          the {@link tcResultSet} object to convert to a
   **                            string.
   ** @param i
   **
   ** @return                    a string representation of the contents of the
   **                            {@link tcResultSet}.
   * @throws tcAPIException
   * @throws tcColumnNotFoundException
   */
  static String getLogString(final String[] columnName, final tcResultSet resultSet, final int i)
    throws tcAPIException
    ,      tcColumnNotFoundException {

    StringBuffer sb = new StringBuffer();
    sb.append("\r\nRow [" + i + ":");
    boolean first = true;
    for (int j = 0; j < columnName.length; j++) {
      if (first) {
        first = false;
        sb.append(columnName[j] + " =" + resultSet.getStringValue(columnName[j]));
      }
      sb.append(", " + columnName[j] + " =" + resultSet.getStringValue(columnName[j]));
    }
    sb.append("]");
    return sb.toString();
  }
}
