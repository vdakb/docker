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

    File        :   Function.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Function.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoFunctionTemplate;

import com.sap.conn.jco.JCoRuntimeException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.security.GuardedString;

import oracle.iam.identity.sap.service.resource.ConnectionBundle;

////////////////////////////////////////////////////////////////////////////////
// class Function
// ~~~~~ ~~~~~~~~
/**
 ** The <code>Function</code> ....
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class Function {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String       UPDATE_TOKEN  = "X";
  static final String       REPLACE_TOKEN = "R";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the connection wrapper of target specific parameters where this module is
   ** attached to
   */
  private final Connection  connection;
  private final String      functionName;

  /**
   ** the wrapper of target specific parameters where this module is attached to
   */
  private JCoFunction       delegate;

  /** the potential account this function module belongs to */
  private String            account;

  /** operational indicatore if the function is being recreated */
  private boolean           update;

  /** needed to determine update token for some fields */
  private boolean           enableCUA = false;

  /**
   ** Cache for the assigned values in case we have to recreate the function.
   ** <p>
   ** Each element of cache is a String array object
   **  String[] values = {
   **    structOrTable
   **  , attrName
   **  , attrValue
   **  , (update ? UPDATE_TOKEN : null)
   **  };
   */
  private List<Object[]>    cache;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Function</code> which is associated with the
   ** specified {@link Connection}.
   **
   ** @param  connection         the {@link Connection} where this
   **                            <code>Function</code> module belongs to.
   ** @param  functionName       the name of the {@link JCoFunction} module
   **                            to be obtained and exceuted.
   **
   ** @throws ConnectionException if the feature configuration cannot be created.
   */
  public Function(final Connection connection, final String functionName, final String account)
    throws ConnectionException {

    // ensure inheritance
    this(connection, functionName);
    accountValue(account);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Function</code> which is associated with the
   ** specified {@link Connection}.
   **
   ** @param  connection         the {@link Connection} where this
   **                            <code>Function</code> module belongs to.
   ** @param  functionName       the name of the {@link JCoFunction} module
   **                            to be obtained and exceuted.
   **
   ** @throws ConnectionException if the feature configuration cannot be created.
   */
  public Function(final Connection connection, final String functionName)
    throws ConnectionException {

    // ensure inheritance
    this(connection, functionName, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Function</code> which is associated with the
   ** specified {@link Connection}.
   **
   ** @param  connection         the {@link Connection} where this
   **                            <code>Function</code> module belongs to.
   ** @param  functionName       the name of the {@link JCoFunction} module
   **                            to be obtained and exceuted.
   **
   ** @throws ConnectionException if the feature configuration cannot be created.
   */
  public Function(final Connection connection, final String functionName, final boolean retryConnection)
    throws ConnectionException {

    // ensure inheritance
    this(connection, functionName, retryConnection, connection.cuaManaged());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Function</code> which is associated with the
   ** specified {@link Connection}.
   **
   ** @param  connection         the {@link Connection} where this
   **                            <code>Function</code> module belongs to.
   ** @param  functionName       the name of the {@link JCoFunction} module
   **                            to be obtained and exceuted.
   **
   ** @throws ConnectionException if the feature configuration cannot be created.
   */
  public Function(final Connection connection, final String functionName, final boolean retryConnection, final boolean cuaMannaged)
    throws ConnectionException {

    // ensure inheritance
    super();

    // create the property mapping for the connection control
    this.connection    = connection;
    this.functionName  = functionName;
    this.enableCUA     = cuaMannaged;
    try {
      create();
    }
    catch (ConnectionException e) {
      Throwable t = e.getCause();
      if (t instanceof JCoException) {
        final int group = ((JCoException)t).getGroup();
        if ((retryConnection) && ((group == JCoException.JCO_ERROR_COMMUNICATION) || (group == JCoException.JCO_ERROR_PROTOCOL) || (group == JCoException.JCO_ERROR_SYSTEM_FAILURE))) {
          try {
            this.connection.disconnect();
          }
          catch (Exception x) {
            t = x;
            if ((t != null) && (t instanceof JCoException))
              throw new ConnectionException(e);

            this.connection.logger().error("<init>", "Non jco error thrown during connection : {0}", x.getMessage());
          }
        }
      }
      else {
        throw e;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the function name.
   **
   ** @return                    the function name.
   */
  public final String name() {
    return this.delegate.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toXML
  /**
   ** Returns the function as an XML string.
   ** <p>
   ** Its format is depending on the used interface implementation, and is not
   ** guaranteed to remain stable. This method is meant to be used as a more
   ** powerful toString() variant, which allows to view the function within XML
   ** viewing tools thus making it easier to have a closer look at single
   ** parameter values.
   ** <p>
   ** Please note, that only an XML fragment is returned, which does not include
   ** an XML header.
   **
   ** @return                    the function serialized in XML format.
   */
  public final String toXML() {
    return this.delegate.toXML();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeTable
  /**
   ** Checks whether the field with the specified name is a table parameter.
   **
   ** @param  metaData           the {@link JCoMetaData} descriptor providing
   **                            access to the descriptor data.
   ** @param  name               the name of the field to verify.
   **
   ** @return                    <code>true</code> if the specified field is a
   **                            table parameter, <code>false</code> otherwise.
   */
  public boolean typeTable(final JCoMetaData metaData, final String name) {
    boolean result = false;
    try {
      result = (metaData != null) && (metaData.isTable(name));
    }
    catch (JCoRuntimeException e) {
      // die silently
      ;
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeStructure
  /**
   ** Checks whether the field with the specified name is a structure parameter.
   **
   ** @param  metaData           the {@link JCoMetaData} descriptor providing
   **                            access to the descriptor data.
   ** @param  name               the name of the field to verify.
   **
   ** @return                    <code>true</code> if the specified field is a
   **                            structure parameter, <code>false</code>
   **                            otherwise.
   */
  public boolean typeStructure(final JCoMetaData metaData, final String name) {
    boolean result = false;
    try {
      result = (metaData != null) && (metaData.isStructure(name));
    }
    catch (JCoRuntimeException e) {
      // die silently
      ;
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returning
  /**
   ** Returns the value of the hard coded attribute name <code>RETURN</code> as
   ** a {@link JCoStructure}.
   **
   ** @return                    the desired {@link JCoStructure} mapped to hard
   **                            coded value <code>RETURN</code>.
   */
  public JCoStructure returning() {
    return structure("RETURN");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   structure
  /**
   ** Returns the table with the specified name.
   ** <p>
   ** <b>WARNING</b>:
   ** <br>
   ** The returned table should only be used to read data from the table. If
   ** writing data to the table, use 'appendTableRow' in order to have the
   ** function call recreated correctly if the connection to the SAP/R3 System
   ** fails.
   **
   ** @param  name               the name of the export structure segment to
   **                            return as a {@link JCoStructure}.
   **
   ** @return                    the desired {@link JCoTable} mapped to the
   **                            given name.
   */
  public JCoTable getTable(final String name) {
    final JCoParameterList parameter = this.delegate.getTableParameterList();
    return (parameter != null) ? parameter.getTable(name) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   structure
  /**
   ** Returns the export structure with the specified name.
   **
   ** @param  name               the name of the export structure segment to
   **                            return as a {@link JCoStructure}.
   **
   ** @return                    the desired {@link JCoStructure} mapped to the
   **                            given name.
   */
  public JCoStructure structure(final String name) {
    final JCoParameterList export = exportParameter();
    return (export == null) ? null : export.getStructure(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tableParameter
  /**
   ** Returns the list of table parameters associated with this
   ** {@link JCoFunction} module.
   **
   ** @return                    the list of table parameters associated with
   **                            this {@link JCoFunction} module.
   */
  public JCoParameterList tableParameter() {
    return this.delegate.getTableParameterList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportParameter
  /**
   ** Returns the list of export parameters associated with this
   ** {@link JCoFunction} module.
   **
   ** @return                    the list of export parameters associated with
   **                            this {@link JCoFunction} module.
   */
  public JCoParameterList exportParameter() {
    return this.delegate.getExportParameterList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importParameter
  /**
   ** Sets the import parameter associated with this {@link JCoFunction} module.
   **
   ** @param  fieldName
   ** @param  value
   */
  public void importParameter(final String fieldName, final Object value) {
    this.delegate.getImportParameterList().setValue(fieldName, value);
    updateCache(null, fieldName, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importParameter
  /**
   ** Returns the list of import parameters associated with this
   ** {@link JCoFunction} module.
   **
   ** @return                    the list of import parameters associated with
   **                            this {@link JCoFunction} module.
   */
  public JCoParameterList importParameter() {
    return this.delegate.getImportParameterList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importValue
  /**
   ** Set attribute name and value.
   **
   ** @param  name               the name of the attribute to set the value in
   **                            the import segment for.
   ** @param  value              the value to set in the import segment for for
   **                            atribute.
   * @throws JCoException
   */
  public void importValue(final String name, final Object value)
    throws JCoException {

    final String method = "importValue";
    if (name.equals("PASSWORD")) {
      this.connection.debug(method, ConnectionBundle.format(ConnectionMessage.FUNCTION_IMPORT_ATTRIBUTE, name, "********"));
    }
    else {
      this.connection.debug(method, ConnectionBundle.format(ConnectionMessage.FUNCTION_IMPORT_ATTRIBUTE, name, value));
    }
    importParameter().setValue(name, value);
    updateCache(null, name, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountValue
  /**
   ** Sets the account attribute in the import parameters of the wrapped
   ** {@link JCoFunction}.
   ** <p>
   ** A convenience method which sets the USERNAME field for this object. Some
   ** {@link JCoFunction} objects may not contain a USERNAME field, but most of
   ** them that are used in this connector do.
   **
   ** @param  value              the value to set for the account attribute in
   **                            the import parameters.
   */
  public void accountValue(final String value) {
    if (this.update)
      this.account = value;

    this.delegate.getImportParameterList().setValue("USERNAME", value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importTableParameter
  /**
   ** Sets the object as the value for the named attribute in the list of table
   ** parameters.
   **
   ** @param  name               the name of the attribute to set.
   ** @param  value              the value to set for the attribute.
   */
  public void importTableParameter(final String name, final Object value) {
    tableParameter().setValue(name, value);
    updateCache(null, name, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importSegmentValue
  /**
   ** Sets the object as the value for the named attribute in the list of
   ** parameters in the specified <code>segment</code>.
   **
   ** @param  segment            the name of the structural segment the
   **                            value-pair needs to set.
   ** @param  name               the name of the attribute to set.
   ** @param  value              the value to set for the attribute.
   */
  public void importSegmentValue(final String segment, final String name, final Object value, final boolean update)
    throws ConnectionException {

    importSegmentValue(segment, name, value, update, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importSegmentValue
  /**
   ** Sets the object as the value for the named attribute in the list of
   ** parameters in the specified <code>segment</code>.
   **
   ** @param  segment            the name of the structural segment the
   **                            value-pair needs to set.
   ** @param  name               the name of the attribute to set.
   ** @param  value              the value to set for the attribute.
   */
  public void importSegmentValue(final String segment, final String name, Object value, final boolean update, final Map<String, String> formats)
    throws ConnectionException {

    final String method = "importValue";
    this.connection.trace(method, SystemMessage.METHOD_ENTRY);

    // do not want passwords to show in the trace
    if ((name.equals("PASSWORD")) || (name.equals("NEW_PASSWORD")) || (name.equals("BAPIPWD")) || (name.equals("ZXLOLD_PASSWORD")) || (name.equals("ZXLNEW_PASSWORD"))) {
      final char[] plain = new char[50];
      ((GuardedString)value).access(new GuardedString.Accessor() {
          public void access(final char[] clearChars) {
            try {
              System.arraycopy(clearChars, 0, plain, 0, clearChars.length);
            }
            catch (Exception e) {
              Function.this.connection.error(method, e.getMessage());
            }
          }
        });
      value = new String(plain).trim();
      this.connection.debug(method, ConnectionBundle.format(ConnectionMessage.FUNCTION_IMPORT_PARAMTER, new Object[]{segment, name, "********", update ? "true" : "false" }));
    }
    else {
      this.connection.debug(method, ConnectionBundle.format(ConnectionMessage.FUNCTION_IMPORT_PARAMTER, new Object[]{segment, name, value, update ? "true" : "false" }));
    }

    if (!StringUtility.isEmpty(segment)) {
      boolean          structure = false;
      boolean          table     = false;
      JCoParameterList parameter = this.delegate.getImportParameterList();
      if (typeStructure(parameter.getListMetaData(), segment)) {
        structure = true;
      }
      else {
        parameter = this.delegate.getTableParameterList();
        if ((parameter != null) && (typeTable(parameter.getListMetaData(), segment))) {
          table = true;
        }
      }

      if (structure) {
        final JCoStructure segmentData = parameter.getStructure(segment);
        segmentData.setValue(name, value);
        // when updating a user you must also set the corresponding update table.
        // For example if you are updating the value FIRSTNAME in the ADDRESS
        // table, you must also set FIRSTNAME to 'X' in the ADDRESSX table.
        // This tells SAP that you really do want to change this user.
        // Note: this is the same for table updates
        if (update) {
          final JCoStructure updateStructure = this.delegate.getImportParameterList().getStructure(segment + UPDATE_TOKEN);
          // special case USERALIAS otherwise we'll throw:
          // JCO_ERROR_FIELD_NOT_FOUND: Field USERALIAS not a member of
          // BAPIALIASX
          // Since we get BAPIALIASX back from SAP we have to assume that this
          // is a special case.
          if (name.equalsIgnoreCase("useralias"))
            updateStructure.setValue("BAPIALIAS", UPDATE_TOKEN);
          else if (updateStructure.getMetaData().getName().equalsIgnoreCase("BAPIUCLASSX")) {
            if (this.enableCUA)
              updateStructure.setValue("UCLASSSYS", REPLACE_TOKEN);
            else
              updateStructure.setValue("UCLASS", UPDATE_TOKEN);
          }
          else {
            updateStructure.setValue(name, UPDATE_TOKEN);
          }  // if .. else attributeName
        }
      }
      else if (table) {
        final JCoTable segmentData = parameter.getTable(segment);
        String format = null;
        // "->TABLE" is used to denote an attribute is a table this handles
        // groups manually
        if ((segment.equals("GROUPS")) && (!name.equals("TABLE"))) {
          format = "Y|:" + name;
        }
        else if (formats != null) {
          // we still want to be able to set normal tables formats should only
          // contain values of tables defined in the schema as
          // <TABLENAME>->TABLE
          format = formats.get(segment);
        }

        if (format != null) {
          final StringTokenizer st = new StringTokenizer(format, "|:");
          boolean updateTable = false;
          final int count = st.countTokens();
          if (count > 0) {
            updateTable = st.nextToken().equals("Y");
          }

          JCoStructure updateStruct = null;
          if ((update) && (updateTable)) {
            updateStruct = this.delegate.getImportParameterList().getStructure(segment + UPDATE_TOKEN);
          }
          if ((value != null) && ((value instanceof List)) && (!((List)value).isEmpty())) {
            segmentData.clear();
            segmentData.firstRow();

            // put columns in list so that we can parse easier
            final List<String> columnList = new ArrayList<String>();
            while (st.hasMoreTokens()) {
              columnList.add(st.nextToken());
            }

            StringTokenizer formatT = null;
            for (String cursor : (List<String>)value) {
              formatT = new StringTokenizer(cursor, "|:");
              // make sure format and string have the same number of tokens
              if (formatT.countTokens() != columnList.size()) {
                this.connection.trace(method, SystemMessage.METHOD_EXIT);
                throw new ConnectionException(ConnectionError.PARAMETER_TABLE_FORMAT_INVALID, segment);
              }
              segmentData.appendRow();
              for (String column : columnList) {
                if (!segmentData.getMetaData().hasField(column)) {
                  this.connection.trace(method, SystemMessage.METHOD_EXIT);
                  final String[] arguments = {column, segment};
                  throw new ConnectionException(ConnectionError.PARAMETER_TABLE_NAME_INVALID, arguments);
                }
                segmentData.setValue(column, formatT.nextToken());
                if (updateStruct != null)
                  updateStruct.setValue(column, UPDATE_TOKEN);
              }
            }
          }
          else if (!(value instanceof List)) {
            this.connection.trace(method, SystemMessage.METHOD_EXIT);
            throw new ConnectionException(ConnectionError.PARAMETER_VALUE_LIST_EXPECTED, name);
          }
          else if (updateStruct != null) {
            // empty value so we need to clear the values in table by setting
            // the update field
            for (int i = 0; i < segmentData.getFieldCount(); i++)
              updateStruct.setValue(segmentData.getMetaData().getName(i), UPDATE_TOKEN);
          }
        }
        else {
          handleValue(segmentData, name, value);
        }
      }
    }
    // try it as a field
    else {
      if ((value instanceof Boolean))
        this.delegate.getImportParameterList().setValue(name, ((Boolean)value).booleanValue() ? 1 : 0);
      else {
        this.delegate.getImportParameterList().setValue(name, value);
      }
      if (update)
        this.delegate.getImportParameterList().setValue(name + UPDATE_TOKEN, UPDATE_TOKEN);
    }
    this.connection.trace(method, SystemMessage.METHOD_EXIT);
    updateCache(segment, name, value, update);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importSegmentValue
  /**
   ** Sets the object as the value for the named attribute in the specified
   ** {@link JCoTable} <code>segment</code>.
   **
   ** @param  name               the name of the attribute value to set.
   ** @param  value              the value to set for the attribute.
   */
  public void tableRowValue(final JCoTable segment, final String name, final Object value) {
    if (segment != null) {
      segment.setValue(name, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Executes the the wrapped {@link JCoFunction} leveraging the passed in
   ** {@link Connection} handler.
   **
   ** @throws ConnectionException if an exception occurred during the call
   **                             execution.
   */
  public void execute()
    throws ConnectionException {

    // prevent bogus state of instance
    if (this.connection == null)
      throw new ConnectionException(ConnectionError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    // prevent bogus state of instance
    if ((this.delegate == null) && (this.connection.destination() != null))
      throw new ConnectionException(ConnectionError.INSTANCE_ATTRIBUTE_IS_NULL, "delegate");

    final String method = "execute";
    this.connection.trace(method, SystemMessage.METHOD_ENTRY);

    if (!StringUtility.isEmpty(this.account))
      this.connection.debug(method, ConnectionBundle.format(ConnectionMessage.FUNCTION_EXECUTE_ACCOUNT, this.delegate.getName(), this.account));
    else {
      this.connection.debug(method, ConnectionBundle.format(ConnectionMessage.FUNCTION_EXECUTE_GENERIC, this.delegate.getName()));
    }
    trace("before");
    this.connection.execute(this.delegate);
    trace("after");
    this.connection.trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Executes the the wrapped {@link JCoFunction} leveraging the passed in
   ** {@link Connection} handler.
   **
   ** @param  retryCount         the number of consecutive attempts to recreate
   **                            the function module in case an established
   **                            connection is broken.
   ** @param  retryInterval      the interval (in milliseconds) between
   **                            consecutive attempts to recreate the function
   **                            module in case an established connection is
   **                            broken.
   **
   ** @throws ConnectionException if an exception occurred during the call
   **                             execution.
   */
  public void execute(final int retryCount, final int retryInterval)
    throws ConnectionException {

    // prevent bogus state of instance
    if (this.connection == null)
      throw new ConnectionException(ConnectionError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    // prevent bogus state of instance
    if ((this.delegate == null) && (this.connection.destination() != null))
      throw new ConnectionException(ConnectionError.INSTANCE_ATTRIBUTE_IS_NULL, "delegate");

    final String method = "execute";
    this.connection.trace(method, SystemMessage.METHOD_ENTRY);

    trace("before");
    int retryAttempt = 0;
    do
      try {
        if (retryAttempt > 0) {
          this.connection.disconnect();
          this.connection.connect();
          // recreate the function object again.
          recreate();
        }
        this.connection.execute(this.delegate);
        retryAttempt = retryCount;
      }
      catch (ConnectionException e) {
        // the ConnectionException wrappes the causing JCoException
        int group = ((JCoException)e.getCause()).getGroup();
        this.connection.warning("Exception type: "  + Integer.valueOf(group));
        if ((group == JCoException.JCO_ERROR_COMMUNICATION) || (group == JCoException.JCO_ERROR_PROTOCOL) || (group == JCoException.JCO_ERROR_SYSTEM_FAILURE)) {
          this.connection.warning(method, ConnectionBundle.string(ConnectionError.FUNCTION_EXECUTE_RETRY));
          try {
            Thread.sleep(retryInterval);
          }
          catch (InterruptedException ee) {
            this.connection.warning(method, ee.getMessage());
            this.connection.trace(method, SystemMessage.METHOD_EXIT);
          }
        }
        else {
          this.connection.error(method, ConnectionBundle.string(ConnectionError.FUNCTION_EXECUTE_ABORT));
          this.connection.trace(method, SystemMessage.METHOD_EXIT);
          throw e;
        }
      }
      catch (Throwable t) {
        this.connection.fatal(method, t);
        this.connection.trace(method, SystemMessage.METHOD_EXIT);
      }
    while (retryAttempt++ < retryCount);
    trace("after");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verify
  /**
   ** Retrieves the messages from the RETURN table and throws an exception if an
   ** error message exists.
   **
   ** @throws ConnectionException if an error is detected.
   */
  public void verify()
    throws ConnectionException {

    final String method = "verify";
    this.connection.trace(method, SystemMessage.METHOD_ENTRY);

    try {
      JCoParameterList parameter = exportParameter();
      if ((parameter != null) && (typeStructure(parameter.getListMetaData(), "RETURN"))) {
        final JCoStructure segment = parameter.getStructure("RETURN");
        if (segment != null) {
          final String type    = segment.getString("TYPE");
          final String message = segment.getString("MESSAGE");
          if (type.equals("E")) {
            throw new ConnectionException(message);
          }
          // TODO: store results message if its not an error?
          this.connection.info(message);
        }
      }
      else {
        parameter = tableParameter();
        if ((parameter != null) && (typeTable(parameter.getListMetaData(), "RETURN"))) {
          final JCoTable segment = parameter.getTable("RETURN");
          if (segment != null) {
            StringBuffer messages = null;
            for (int i = 0; i < segment.getNumRows(); i++) {
              segment.setRow(i);
              int number     = segment.getInt("NUMBER");
              String type    = segment.getString("TYPE");
              String message = segment.getString("MESSAGE");
              if ((type.equals("E")) && (number != 124)) {
                if (messages == null) {
                  messages = new StringBuffer();
                }
                messages.append(message).append("\n");
                this.connection.error(method, message);
              }
              else {
                // TODO: store results message if its not an error?
                this.connection.info(message);
              }
            } // for each returned table row
            if (messages != null)
              throw new ConnectionException(ConnectionError.ABORT, messages.toString());
          }
        }
      }
    }
    finally {
      this.connection.trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountNotExist
  /**
   ** In some cases you want to know whether or not you need to throw an
   ** Unknown UID.
   */
  public void accountNotExist() {
    final JCoParameterList parameter = this.tableParameter();
    if ((parameter != null) && typeTable(parameter.getListMetaData(), "RETURN")) {
      final JCoTable table = parameter.getTable("RETURN");
      if (table != null) {
        for (int i = 0; table != null && i < table.getNumRows(); i++) {
          table.setRow(i);
          final int    number  = table.getInt("NUMBER");
          final String type    = table.getString("TYPE");
          if (type.equals("E") && number == 124) {
            throw new RuntimeException(table.getString("MESSAGE"));
          }
        } // for each returned table row
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a {@link JCoRepository} object from the template for the passed
   ** function.
   **
   ** @throws ConnectionException if another error occurred during the retrieval
   **                             of the function interface with the group field
   **                             set appropiately.
   */
  private void create()
    throws ConnectionException {

    JCoRepository repository = this.connection.repository();
    // try to reconnect to see if we can get the IRepository
    if (repository == null) {
      this.connection.disconnect();
      this.connection.connect();
      repository = this.connection.repository();
    }

    // a JCO IRepository object can only be obtained with a valid connection.
    // So, by implication, if the repository is null, the connection is invalid.
    if (repository == null)
      throw new ConnectionException(ConnectionError.FUNCTION_CREATE, this.functionName);

    // if we can, then build
    try {
      final JCoFunctionTemplate template = repository.getFunctionTemplate(this.functionName.toUpperCase());
      // The only way to know if the function is actually available on the SAP
      // system is to check to see if the template is non-null.
      if (template == null)
        throw new ConnectionException(ConnectionError.FUNCTION_NOT_FOUND, this.functionName);

      this.delegate = template.getFunction();
    }
    catch (JCoException e) {
      throw new ConnectionException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   recreate
  /**
   ** Recreates a {@link JCoFunction} object from the template for the passed
   ** function.
   ** <p>
   ** This typically occurs when the connection to the SAP gateway (or system)
   ** is lost and a new connection is restored.
   **
   ** @throws ConnectionException if another error occurred during the retrieval
   **                             of the function interface with the group field
   **                             set appropiately.
   */
  private synchronized void recreate()
    throws ConnectionException {

    final String method = "recreate";
    this.connection.trace(method, SystemMessage.METHOD_ENTRY);

    this.connection.debug(method, ConnectionBundle.format(ConnectionMessage.FUNCTION_EXECUTE_RECREATE, this.functionName));
    // don't update the cached values
    this.update = false;
    try {
      create();
      if (this.account != null) {
        accountValue(this.account);
      }
      if (this.cache != null) {
        final Iterator<Object[]> i = this.cache.iterator();
        while (i.hasNext()) {
          final Object[] element = i.next();
          importSegmentValue((String)element[0], (String)element[1], element[2], element[3] != null);
        }
      }
    }
    finally {
      this.update = true;
      this.connection.trace(method, SystemMessage.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleValue
  /**
   ** Sets the value of the specified attribute to be the given {@link JCoTable}.
   ** <p>
   ** Used to suppress compiler warnings.
   **
   ** @param  table              the {@link JCoTable} to manipulate.
   ** @param  attribute          the name of the attribute to set the value in
   **                            the given {@link JCoTable} for.
   ** @param  value              the value to set in the given {@link JCoTable}
   **                            for atribute.
   */
  private void handleValue(final JCoTable table, final String attribute, final Object value) {
    if ((value instanceof List)) {
      for (Object cursor : (List<?>)value) {
        // appends a new row at the end of the table and moves the row pointer
        // such that it points to the newly appended row.
        table.appendRow();
        // sets the value of the specified attriubute
        table.setValue(attribute, cursor);
      }
    }
    else {
      // appends a new row at the end of the table and moves the row pointer
      // such that it points to the newly appended row.
      table.appendRow();
      // sets the value of the specified attriubute
      table.setValue(attribute, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateCache
  /**
   ** Updates the cache with attribute name and value.
   **
   ** @param  segment            the segment name of the structure/table the
   **                            cache entry belongs to.
   ** @param  name               the name of the attribute to set the value in
   **                            the given segment for.
   ** @param  value              the value to set in the given segment for for
   **                            atribute.
   * @param update
   */
  private synchronized void updateCache(final String segment, final String name, final Object value, final boolean update) {
    // update the cache
    if (this.update) {
      if (this.cache == null)
        this.cache = new ArrayList<Object[]>();

      final Object[] values = {segment, name, value, update ? UPDATE_TOKEN : null};
      this.cache.add(values);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Dumps the function as an XML string to a computed file name.
   ** <p>
   ** The XML format is depending on the used interface implementation, and is
   ** not guaranteed to remain stable. This method is meant to be used as a
   ** more powerful toString() variant, which allows to view the function within
   ** XML viewing tools thus making it easier to have a closer look at single
   ** parameter values.
   ** <p>
   ** Please note, that only an XML fragment is returned, which does not include
   ** an XML header.
   **
   ** @param  timing             the discriminator for the file name to genarate
   **                            to specify the phase at which the dump of the
   **                            function is taken.
   */
  private void trace(final String timing) {
    final String method = "trace";
    if (JCo.getTraceLevel() > 0) {
      final File trace  = new File(JCo.getTracePath());
      FileWriter writer = null;
      try {
        writer = new FileWriter(File.createTempFile(timing + "-", ".xml", trace));
        writer.write(this.delegate.toXML());
      }
      catch (IOException e) {
        this.connection.error("traceFunction", ConnectionBundle.format(ConnectionError.FUNCTION_FILETRACE_WRITE, e.getMessage()));
      }
      finally {
        if (writer != null)
          try {
            writer.close();
          }
          catch (IOException e) {
            this.connection.error("traceFunction", ConnectionBundle.format(ConnectionError.FUNCTION_FILETRACE_CLOSE, e.getMessage()));
          }
      }
    }
  }
}