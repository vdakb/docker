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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   AccountTargetReconciliation.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountTargetReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.service.reconciliation;

import java.util.Map;
import java.util.HashMap;

import java.io.File;

import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;

import oracle.hst.foundation.SystemConsole;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AttributeMapping;

import oracle.iam.identity.sap.control.Feature;
import oracle.iam.identity.sap.control.Resource;
import oracle.iam.identity.sap.control.Connection;

////////////////////////////////////////////////////////////////////////////////
// class AccountTargetReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountTargetReconciliation</code> acts as the service end point
 ** for the Oracle Identity Manager to reconcile account information from a
 ** SAP/R3 User Management Service.
 ** <p>
 ** This class provides also the callback interface for operations that are
 ** returning one or more results. Currently used only by Search, but may be
 ** used by other operations in the future.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class AccountTargetReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static String   timestamp;
  static String   timezone;
  static File     propertyFile;
  static File     featureFile;

  private final HashMap<String, String> resource         = new HashMap<String, String>();

  private AccountTargetReconciliation() {
    // ensure inheritance
    super();

    // simulate the creation of an IT Resource by creating an appropriate
    // mapping
    resource.put(Resource.APPLICATION_SERVER_HOST,  "sap-f3d.fs01.vwf.vwfs-ad");
    resource.put(Resource.APPLICATION_SERVER_GROUP, "public");
    resource.put(Resource.MESSAGE_SERVER_HOST,      "sap-f3d.fs01.vwf.vwfs-ad");
    resource.put(Resource.CLIENT_LOGON,             "103");
    resource.put(Resource.PRINCIPAL_NAME,           "AIM_USER");
    resource.put(Resource.PRINCIPAL_PASSWORD,       "Zgd.umw.oim.2013");
    resource.put(Resource.SYSTEM_NAME,              "F3D");
    resource.put(Resource.SYSTEM_NUMBER,            "05");
    resource.put(Resource.SYSTEM_LANGUAGE,          "EN");
    resource.put(Resource.SYSTEM_TIMEZONE,          "GMT+01:00");
    resource.put(Resource.SECURE_SOCKET,            "no");
    resource.put("Unicode mode",                    "no");
  }

  public static void main(String[] args)
    throws Exception {

    final AccountTargetReconciliation test = new AccountTargetReconciliation();
    final SystemConsole console  = new SystemConsole("Test");
    final Resource      resource = new Resource(console, test.resource);
    final Feature       feature  = Connection.unmarshal(console, new File("properties/feature.xml"));

    final String  timeStamp = "201410151000000";
    final boolean isUM      = true;

    final String date      = timeStamp.substring(0, 8);
    final String time      = timeStamp.substring(8);

    Descriptor    descriptor = new Descriptor(console);
    try {
      DescriptorFactory.configure(descriptor, new File("metadata/account-reconciliation.xml"));
    }
    catch (TaskException e) {
      e.printStackTrace();

    }

    final Map<String, Map<String, Object>> segmentMapping = new HashMap<String, Map<String, Object>>();
    final AttributeMapping attributeMapping = descriptor.attributeMapping();
    for (Map.Entry<String, Object> cursor : attributeMapping.entrySet()) {
      // string split use regular expression hence we need to escape the dot
      String[] segments = cursor.getKey().split("\\.");
      Map<String, Object> xxxxx = segmentMapping.get(segments[0]);
      if (xxxxx == null) {
        xxxxx = new HashMap<String, Object>();
        segmentMapping.put(segments[0], xxxxx);
      }
      xxxxx.put(segments[1], cursor.getValue());
    }

    final Connection connection = new Connection(console, resource, feature);
    try {
      connection.connect();

      final Descriptor.ChangeLog changeLog = (Descriptor.ChangeLog)descriptor.changeLog();
      final JCoFunction          changes   = connection.function("RFC_READ_TABLE");

      JCoParameterList parameter = changes.getImportParameterList();
      parameter.setValue("QUERY_TABLE", changeLog.value());
      parameter.setValue("ROWSKIPS",    "0");
      parameter.setValue("ROWCOUNT",    "500");

      JCoTable table = changes.getTableParameterList().getTable("OPTIONS");
      table.appendRow();
      if (isUM)
        table.setValue("TEXT", String.format("MODDA GE '%s'AND MODTI GT '%s'", date, time));
      else
        table.setValue("TEXT", "MODDATE GE '" + date + "'AND MODTIME GT '" + time + "'");

      JCoTable fields = changes.getTableParameterList().getTable("FIELDS");
      fields.appendRow();
      fields.setValue("FIELDNAME", "BNAME");
      if (changeLog == Descriptor.ChangeLog.USH02 || changeLog == Descriptor.ChangeLog.USR02) {
        fields.appendRow();
        fields.setValue("FIELDNAME", "UFLAG");
      }
      changes.execute(connection.destination());
      JCoTable result = changes.getTableParameterList().getTable("FIELDS");
      JCoTable data   = changes.getTableParameterList().getTable("DATA");

      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      int resultSize = result.getNumRows();
      int dataSize   = data.getNumRows();
      String name  = null;
      String value = null;
      final Map<String, Object> batch = new HashMap<String, Object>();
      if (resultSize != 0) {
        for (int resultRow = 0; resultRow < resultSize; resultRow++) {
          result.setRow(resultRow);
          name = result.getString("FIELDNAME");
          if (name.equals("BNAME")) {
            i = Integer.parseInt(result.getString("OFFSET"));
            j = Integer.parseInt(result.getString("LENGTH"));
            j += i;
          }
          if (!(changeLog == Descriptor.ChangeLog.USH02) && !(changeLog == Descriptor.ChangeLog.USR02))
            continue;
          k = Integer.parseInt(result.getString("OFFSET"));
          m = Integer.parseInt(result.getString("LENGTH"));
          m += k;
        }
        if (dataSize != 0)
          for (int dataRow = 0; dataRow < dataSize; dataRow++) {
            data.setRow(dataRow);
            String str4 = data.getString("WA");
            if (changeLog == Descriptor.ChangeLog.USH02 || changeLog == Descriptor.ChangeLog.USR02) {
              name  = str4.substring(i, j).trim();
              value = str4.substring(k).trim();
            }
            else {
              name  = str4.substring(i).trim();
              value = "NONE";
            }
            batch.put(name, value);
          }
      }
      final JCoFunction detail = connection.function("BAPI_USER_GET_DETAIL");
      parameter = detail.getImportParameterList();
      if (batch.size() > 0) {
        final Map<String, Object> subject = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : batch.entrySet()) {
          parameter.setValue("USERNAME", entry.getKey());
          detail.execute(connection.destination());
          JCoTable returning = detail.getTableParameterList().getTable("RETURN");
          final String message = returning.getString("MESSAGE");
          if (message.endsWith("does not exist"))
            continue;

          for (Map.Entry<String, Map<String, Object>> mapping : segmentMapping.entrySet()) {
            final String segment = mapping.getKey();
            if (!segment.equalsIgnoreCase("UCLASSSYS")) {
              final JCoStructure structure = detail.getExportParameterList().getStructure(segment);
              final Map<String, Object> attribute = segmentMapping.get(segment);
              for (Map.Entry<String, Object> cursor : attribute.entrySet()) {
                final Object reconValue = structure.getValue(cursor.getKey());
                subject.put(cursor.getValue().toString(), reconValue);
              }
            }
          }
          System.out.println(StringUtility.formatCollection(subject));
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      connection.disconnect();
    }
  }
}