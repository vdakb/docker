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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Nextcloud Connector

    File        :   TestFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    TestFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.nextcloud.integration;

import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.Comparator;

import java.io.File;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

////////////////////////////////////////////////////////////////////////////////
// class TestModel
// ~~~~~ ~~~~~~~~~
/**
 ** The emulation of a {@link tcFormInstanceOperations} to mimic the behavior of
 ** fetching form values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestModel extends TestBaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /** The main data model. */
  @JsonProperty("main")
  @SuppressWarnings("oracle.jdeveloper.java.unrestricted-field-access")
  public Data        account = null;

  /** The entitlement data model. */
  @JsonProperty("multi")
  @SuppressWarnings("oracle.jdeveloper.java.unrestricted-field-access")
  public List<Data> entitlement = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Data
  // ~~~~~ ~~~~
  /**
   ** The emulation of a {@link tcResultSet} to mimic the behavior of fetching
   ** form values.
   ** <p>
   ** This class represents a table-like data structure. Each row represents data
   ** about one record in the database. The columns represent individual
   ** attributes of that record.
   */
  public static class Data implements tcResultSet {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
  
    /** Pointer to the current row in the dataset. */
    @JsonIgnore
    private int                       index = 0;

    /** The dataset itself. */
    @JsonProperty("source")
    @SuppressWarnings("oracle.jdeveloper.java.unrestricted-field-access")
    public String                     name;

    @JsonProperty("values")
    private List<Map<String, String>> dataSet;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Data</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Data() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getColumnNames (tcResultSet)
    /**
     ** Returns a list of the field codes for all the fields that exist in this
     ** data record.
     **
     ** @return                  a string array, each array element holding the
     **                          column code.
     **                          <br>
     **                          Possible object is array of {@link String}.
     */
    @Override
    public String[] getColumnNames() {
     return this.dataSet == null ? new String[0] : this.dataSet.get(0).keySet().toArray(new String[0]);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getColumnType (tcResultSet)
    /**
     ** Returns the column type of the specified column.
     **
     ** @param  columnCode       the meta data code for the column.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the int value of the column type.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public int getColumnType(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      if (isEmpty() || !this.dataSet.get(0).containsKey(columnCode))
        throw new tcColumnNotFoundException("Specified column not defined in result set");

      return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isEmpty (tcResultSet)
    /**
     ** Specifies whether the result set has any rows of data in it.
     **
     ** @return                  <code>true</code> if the result set has no data
     **                          in it, <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws tcAPIException
     */
    @Override
    public boolean isEmpty()
      throws tcAPIException {

      return this.dataSet == null ? true : this.dataSet.size() == 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getRowCount (tcResultSet)
    /**
     ** Returns a list of the field codes for all the fields that exist in this
     ** data record.
     **
     ** @return                  an int holding the number of rows.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws tcAPIException
     */
    @Override
    public int getRowCount()
      throws tcAPIException {

      return this.dataSet == null ? 0 : this.dataSet.size();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTotalRowCount (tcResultSet)
    /**
     ** Retrieves the total number of rows (records) in the result set.
     **
     ** @return                  an int holding the number of rows.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws tcAPIException
     */
    public int getTotalRowCount()
      throws tcAPIException {

      return this.dataSet == null ? 0 : this.dataSet.size();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: goToRow (tcResultSet)
    /**
     ** Moves the current row pointer to the specified row in the result set.
     **
     ** @param  row              the number of the row to move to.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @throws tcAPIException
     */
    public void goToRow(final int row)
      throws tcAPIException {

      this.index = row;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getRow (tcResultSet)
    /**
     ** Returns a string representation of the specified row.
     **
     ** @param  row              the number of the row to get the row data for.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  a string array holding all the values of the
     **                          row.
     **                          <br>
     **                          Possible object is array of {@link String}.
     **
     ** @throws tcAPIException
     */
    @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
    public String[] getRow(final int row)
      throws tcAPIException {

      return this.dataSet.get(this.index).values().toArray(new String[0]);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isNull (tcResultSet)
    /**
     ** Checks if the specified column has a value.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  <code>true</code> if the specified column has a
     **                          value, <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    public boolean isNull(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return getStringValue(columnCode) != null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getBooleanValue (tcResultSet)
    /**
     ** Retrieves the boolean value of a particular field from the data record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the boolean representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public boolean getBooleanValue(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return Boolean.valueOf(getStringValue(columnCode));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getIntValue (tcResultSet)
    /**
     ** Retrieves the value of a particular field from the data record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the int representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public int getIntValue(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return Integer.valueOf(getStringValue(columnCode));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getLongValue (tcResultSet)
    /**
     ** Retrieves the value of a particular field from the data record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the long representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is <code>long</code>.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public long getLongValue(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      if (isEmpty() || !this.dataSet.get(0).containsKey(columnCode))
        throw new tcColumnNotFoundException("Specified column not defined in result set");

      return Long.valueOf(this.dataSet.get(this.index).get(columnCode));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getDoubleValue (tcResultSet)
    /**
     ** Retrieves the value of a particular field from the data record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the double representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is <code>double</code>.
     **
     ** @throws tcAPIException
     ** @throws tcColumnNotFoundException
     */
    @Override
    public double getDoubleValue(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return Double.valueOf(getStringValue(columnCode));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getFloatValue (tcResultSet)
    /**
     ** Retrieves the value of a particular field from the data record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the float representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is <code>float</code>.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public float getFloatValue(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return Float.valueOf(getStringValue(columnCode));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getDate (tcResultSet)
    /**
     ** Retrieves the date value of a particular field from the data record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the date representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is {@link Date}.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public Date getDate(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return Date.class.cast(getStringValue(columnCode));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTime (tcResultSet)
    /**
     ** Retrieves the time value of a particular field from the data record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the time representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is {@link Time}.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public Time getTime(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return Time.class.cast(getStringValue(columnCode));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTimestamp (tcResultSet)
    /**
     ** Retrieves the timestamp value of a particular field from the data
     ** record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the timestamp representation of the value of
     **                          the specified field.
     **                          <br>
     **                          Possible object is {@link Timestamp}.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public Timestamp getTimestamp(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return Timestamp.class.cast(getStringValue(columnCode));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getStringValue (tcResultSet)
    /**
     ** Retrieves the value of a particular field from the data record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the string representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public String getStringValue(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      if (isEmpty() || !this.dataSet.get(this.index).containsKey(columnCode))
        throw new tcColumnNotFoundException("Specified column not defined in result set");

      return this.dataSet.get(this.index).get(columnCode);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getByteArrayValue (tcResultSet)
    /**
     ** Retrieves the value of a particular column number from the data record.
     **
     ** @param  columnNumber     the number of the field in the sequence of
     **                          columns.
     **
     ** @return                  the string representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public String getStringValueFromColumn(final int columnNumber)
      throws tcColumnNotFoundException, tcAPIException {

      int columnCount = 0;
      try {
        columnCount = this.dataSet.get(0).keySet().size();
      }
      catch (Exception e) {
        throw new tcAPIException("Error occurred inspecting result set");
      }

      if (columnNumber >= columnCount) {
        throw new tcColumnNotFoundException("Column Index out of bounds");
      }
      return "";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getByteArrayValue (tcResultSet)
    /**
     ** Retrieves the byte array value of a particular field from the data
     ** record.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the string representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is array of <code>byte</code>.
     **
     ** @throws tcAPIException            if the column cannot be retrieved.
     ** @throws tcColumnNotFoundException if the column does not exist.
     */
    @Override
    public byte[] getByteArrayValue(final String columnCode)
      throws tcColumnNotFoundException, tcAPIException {

      return new byte[0];
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sort (tcResultSet)
    /**
     ** Sorts on a column in ascending or descending order.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  ascending        whether its an ascending or descending search.
     **                          Should be set to <code>true</code> for
     **                          ascending searches. Should be set to
     **                          <code>false</code> in the case of descending
     **                          searches.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @throws tcAPIException   if there is an error sorting the resultset.
     */
    @Override
    public void sort(final String columnCode, final boolean ascending)
      throws tcColumnNotFoundException, tcAPIException {

    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sort (tcResultSet)
    /**
     ** Sorts on a column in ascending or descending order
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  ascending        whether its an ascending or descending search.
     **                          Should be set to <code>true</code> for
     **                          ascending searches. Should be set to
     **                          <code>false</code> in the case of descending
     **                          searches.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  comparator       the {@link Comparator} to apply while sorting.
     **                          <br>
     **                          Allowed object is {@link Comparator}.
     **
     ** @throws tcAPIException   if there is an error sorting the resultset.
     */
    @Override
    public void sort(final String columnCode, final boolean pbAscending, final Comparator comparator)
      throws tcColumnNotFoundException, tcAPIException {

    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updateLocalizedValues (tcResultSet)
    /**
     ** Translates the values in the resultSet for the given {@link Locale}.
     **
     ** @param  locale         the locale.
     **                        <br>
     **                        Allowed object is {@link Locale}.
     **
     ** @throws tcAPIException
     ** @throws tcColumnNotFoundException
     */
    @Override
    public void updateLocalizedValues(final Locale locale)
      throws tcColumnNotFoundException, tcAPIException {

    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updateLocalizedValues (tcResultSet)
    /**
     ** Translates the values in the resultSet for the given {@link Locale}.
     **
     ** @param  locale           the locale.
     **                          <br>
     **                          Allowed object is {@link Locale}.
     ** @param  startIndex       the index from where we localized the dataset
     **                          values.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  endIndex         the index till we do the localization of the
     **                          dataset values.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @throws tcAPIException
     ** @throws tcColumnNotFoundException
     */
    @Override
    public void updateLocalizedValues(final Locale locale, final int startIndex, final int endIndex)
      throws tcColumnNotFoundException, tcAPIException {

    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: booleanValue
    /**
     ** Returns a <code>boolean</code> from the attribute mapping of this wrapper.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the boolean representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws ClassCastException if the object returned by the get operation on
     **                            the mappping is not castable to a
     **                            <code>String</code>.
     */
    public boolean booleanValue(final String form, final String columnCode)
      throws TaskException {

      try {
        return getBooleanValue(columnCode);
      }
      catch (tcColumnNotFoundException e) {
        throw TaskException.general(e);
      }
      catch (tcAPIException e) {
        throw TaskException.unhandled(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: integerValue
    /**
     ** Returns a <code>int</code> from the attribute mapping of this wrapper.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the int representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws ClassCastException if the object returned by the get operation on
     **                            the mappping is not castable to a
     **                            <code>String</code>.
     */
    public int integerValue(final String columnCode)
      throws TaskException {

      try {
        return getIntValue(columnCode);
      }
      catch (tcColumnNotFoundException e) {
        throw TaskException.general(e);
      }
      catch (tcAPIException e) {
        throw TaskException.unhandled(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: longValue
    /**
     ** Returns a <code>long</code> from the attribute mapping of this wrapper.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the long representation of the value of the
     **                          specified field.
     **                          <br>
     **                          Possible object is <code>long</code>.
     **
     ** @throws ClassCastException if the object returned by the get operation on
     **                            the mappping is not castable to a
     **                            <code>String</code>.
     */
    public long longValue(final String columnCode)
      throws TaskException {

      try {
        return getLongValue(columnCode);
      }
      catch (tcColumnNotFoundException e) {
        throw TaskException.general(e);
      }
      catch (tcAPIException e) {
        throw TaskException.unhandled(e);
      }
    }

    /////////////////////////////////////////////////////////////////////////////
    // Method: stringValue
    /**
     ** Returns a <code>String</code> from the attribute mapping of this wrapper.
     **
     ** @param  columnCode       the code of the field as defined in the
     **                          Xellerate meta data entries.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>String</code> for the given key.
     **
     ** @throws ClassCastException if the object returned by the get operation on
     **                            the mappping is not castable to a
     **                            <code>String</code>.
     */
    public String stringValue(final String columnCode)
      throws TaskException {

      try {
        return getStringValue(columnCode);
      }
      catch (tcColumnNotFoundException e) {
        throw TaskException.general(e);
      }
      catch (tcAPIException e) {
        throw TaskException.unhandled(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestModel</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestModel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Load a json file’s content and convert it into a {@link TestModel}.
   **
   ** @param  path               the relative path to the file to load from
   **                            the resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link TestModel} providing the
   **                            configuration key-value pairs.
   **
   ** @throws TaskException      if an I/O error occurs.
   */
  public static TestModel build(final String path)
    throws TaskException {

    return build(new File(RESOURCES, path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Load a json file’s content and convert it into a {@link TestModel}.
   **
   ** @param  path               the relative path to the file to load from
   **                            the resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link TestModel} providing the
   **                            configuration key-value pairs.
   **
   ** @throws TaskException      if an I/O error occurs.
   */
  public static TestModel build(final File path)
    throws TaskException {

    try {
      return MapperFactory.instance.readValue(path, TestModel.class);
    }
    catch (Exception e) {
      throw TaskException.unhandled(e);
    }
  }
}