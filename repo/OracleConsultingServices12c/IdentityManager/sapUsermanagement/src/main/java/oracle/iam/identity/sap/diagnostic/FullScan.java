package oracle.iam.identity.sap.diagnostic;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Enumeration;

import java.io.File;
import java.io.FileInputStream;

import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.sap.control.Feature;
import oracle.iam.identity.sap.control.Resource;
import oracle.iam.identity.sap.control.Connection;
import oracle.iam.identity.sap.control.ConnectionException;

public class FullScan extends AbstractUseCase {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static String              rfcTable     = "USR02";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Convenience Constructor.
   **
   ** @param  resource           {@link File} source of property configuration
   **                            to read.
   */
  public FullScan(final Resource resource, final Feature feature) {
    // ensure inheritance
    super(resource, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** The main method for ChangeLog tool.
   **
   ** @param  args               the command-line arguments provided to this
   **                            program.
   */
  public static void main(String[] args) {
    parseCommandLine(args);
    try {
      // simulate the creation of an IT Resource by reading the properties from
      // the specified input stream
      final Properties properties = new Properties();
      properties.load(new FileInputStream(configFile));

      final Resource       resource = new Resource(console);
      final Enumeration<?> cursor   = properties.propertyNames();
      while (cursor.hasMoreElements()) {
        final String key   = (String)cursor.nextElement();
        final String value = properties.getProperty(key);
        resource.put(translate.get(key), value);
      }

      final Feature   feature = oracle.iam.identity.sap.control.Connection.unmarshal(console, featureFile);
      final FullScan service = new FullScan(resource, feature);
      service.execute();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(3);
    }
  }

  private void execute()
    throws ConnectionException {

    final String method = "execute";

    final Connection connection = new Connection(ChangeLog.console, this.resource, this.feature);
    console.debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, connection.toString()));
    try {
      connection.connect();

      final JCoFunction      function  = connection.function("RFC_READ_TABLE");
      final JCoParameterList parameter = function.getImportParameterList();
      parameter.setValue("QUERY_TABLE", rfcTable);
      parameter.setValue("ROWSKIPS",    "0");
      parameter.setValue("ROWCOUNT",    "500");

      final JCoTable fields = function.getTableParameterList().getTable("FIELDS");
      fields.appendRow();
      fields.setValue("FIELDNAME", "BNAME");
      if (rfcTable.equalsIgnoreCase("USH02") || rfcTable.equalsIgnoreCase("USR02")) {
        fields.appendRow();
        fields.setValue("FIELDNAME", "UFLAG");
      }
      function.execute(connection.destination());
      JCoTable result = function.getTableParameterList().getTable("FIELDS");
      JCoTable data   = function.getTableParameterList().getTable("DATA");
      int resultSize = result.getNumRows();
      int dataSize   = data.getNumRows();

      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      String name    = null;
      String value   = null;

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
          if ((!rfcTable.equalsIgnoreCase("USH02")) && (!rfcTable.equalsIgnoreCase("USR02")))
            continue;
          k = Integer.parseInt(result.getString("OFFSET"));
          m = Integer.parseInt(result.getString("LENGTH"));
          m += k;
        }
        if (dataSize != 0)
          for (int dataRow = 0; dataRow < dataSize; dataRow++) {
            data.setRow(dataRow);
            String str4 = data.getString("WA");
            if (rfcTable.equalsIgnoreCase("USH02") || rfcTable.equalsIgnoreCase("USR02")) {
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
      System.out.println("-- ResultSet for " + timeStamp + " ------------");
      if (batch.size() > 0) {
        System.out.println(StringUtility.formatCollection(batch));
      }
      else {
        System.out.println("Batch is empty");
      }
      System.out.println("-- " + batch.size() + "-----------------------------------------");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      connection.disconnect();
    }
  }
}