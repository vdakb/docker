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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   CatalogQuery.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CatalogQuery.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.persistence;

//import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoStructure;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.reconciliation.PairHandler;
//import oracle.iam.identity.foundation.reconciliation.ResultHandler;

import oracle.iam.identity.foundation.schema.Pair;
import oracle.iam.identity.sap.control.Feature;
import oracle.iam.identity.sap.control.Function;
import oracle.iam.identity.sap.control.Connection;
import oracle.iam.identity.sap.control.ConnectionException;

import oracle.iam.identity.sap.persistence.schema.Catalog;

////////////////////////////////////////////////////////////////////////////////
// class CatalogQuery
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>CatalogQuery</code> retrieves metadata informations about lookup
 ** values like entitlements and/or value constraints belonging to an account
 ** from a SAP/R3 Usermanagement Service into Oracle Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class CatalogQuery extends AbstractOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CatalogQuery</code> which is associated with the
   ** specified logging provider <code>loggable</code> and belongs to the
   ** {@link Connection} specified by <code>connection</code>.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code> wrapper.
   ** @param  connection         the {@link Connection} this operation belongs
   **                            to.
   */
  protected CatalogQuery(final Loggable loggable, final Connection connection, final Feature parameter) {
    // ensure inheritance
    super(loggable, connection, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  public static void execute(final Loggable loggable, final Connection connection, final Feature parameter, final Catalog catalog, final PairHandler handler)
    throws ConnectionException {

    final CatalogQuery thread = new CatalogQuery(loggable, connection, parameter);
    thread.execute(catalog, handler);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  private void execute(final Catalog catalog, final PairHandler handler)
    throws ConnectionException {

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String[]             returning    = new String[2];
    List<Pair<String, String>> searchResult = null;
    try {
      String[] detail = null;
      switch (catalog) {
        case ROLE                  : detail = Catalog.ROLE.detail();
                                     final boolean  single    = this.parameter.booleanValue(Catalog.ROLE_SINGLE);
                                     final boolean  composite = this.parameter.booleanValue(Catalog.ROLE_SINGLE);
                                     if (single && !composite) {
                                       detail[6] = "AGR_SINGLE";
                                       detail[7] = "SH";
                                     }
                                     else if (composite && !single) {
                                       detail[6] = "AGR_COLL";
                                       detail[7] = "SH";
                                     }
                                     returning[0] = detail[8];
                                     returning[1] = detail[9];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case CUA_ROLE              : detail = Catalog.CUA_ROLE.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesRFC(detail);
                                     break;
        case TITLE                 : detail = Catalog.TITLE.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case PROFILE               : detail = Catalog.PROFILE.detail();
                                     returning[0] = detail[6];
                                     returning[1] = detail[7];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case CUA_PROFILE           : detail = Catalog.CUA_PROFILE.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesRFC(detail);
                                     break;
        case COMPANY               : detail = Catalog.COMPANY.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case TIMEZONE              : detail = Catalog.TIMEZONE.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case PARAMETER             : detail = Catalog.PARAMETER.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case USERTYPE              : detail = Catalog.USERTYPE.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case USERGROUP             : detail = Catalog.USERGROUP.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case DATEFORMAT            : detail = Catalog.DATEFORMAT.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case DECIMALNOTATION       : detail = Catalog.DECIMALNOTATION.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case COMMUNICATIONTYPE     : detail = Catalog.COMMUNICATIONTYPE.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case CONTRACTUALUSERTYPE   : detail = Catalog.CONTRACTUALUSERTYPE.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case LOGONLANGUAGE         : detail = Catalog.LOGONLANGUAGE.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesBAPI(detail);
                                     break;
        case SYSTEM                : detail = Catalog.SYSTEM.detail();
                                     returning[0] = detail[2];
                                     returning[1] = detail[3];
                                     searchResult.add(Pair.of(this.connection.masterSystemName(), this.connection.masterSystemName()));
                                     break;
        case CUA_SYSTEM            : detail = Catalog.CUA_SYSTEM.detail();
                                     returning[0] = detail[4];
                                     returning[1] = detail[5];
                                     searchResult = lookupValuesRFC(detail);
        default                    : break;
      }

      if (searchResult != null) {
        notifyCallback(searchResult, handler);
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupValuesBAPI
  /**
  /**
   ** This method determines the valid help values (F4) for a parameter or field
   ** of a structured BAPI parameter.
   ** <p>
   ** The help values are based on check tables, fixed domain values, or value
   ** helps defined in a data element. If no search help of this type is
   ** defined, no help values can be specified.
   ** <br>
   ** This method can only be used for BAPI parameters. A check is made to see
   ** whether the field exists in the named parameter, in the method, and in the
   ** business object. You must send the name or object type name of the
   ** business object, the method, and the parameter with the name defined in
   ** the Business Object Repository (BOR), to the Helpvalues.GetList method. If
   ** you have a field from a structured parameter, you also need to specify its
   ** name. You cannot obtain the total value help for all fields of a structure
   ** by not explicitly specifying a field when calling the method.
   ** <br>
   ** As an alternative, you can use details in the parameter ExplicitShlp to
   ** send specific search helps. The parameters ObjType, ObjName, Method,
   ** Parameter and Field must still be filled, so that the BAPI authorization
   ** checks can be made.
   ** <br>
   ** <b>Note</b> that this method only takes elementary search helps into
   ** account. For collective search helps, read the documentation on the method
   ** Helpvalues.GetSearchHelp.
   ** <br>
   ** The help values - together with other useful information - are returned in
   ** the parameter Helpvalues. The data is arranged in the left column by
   ** default.
   **
   ** @param  mapping            array of strings which contains the parameters
   **                            <code>BAPI Function</code> executes.
   **
   ** @return                    the {@link List} containing lookup values
   **                            {@link Pair}s to be added to the lookup table.
   **
   ** @throws ConnectionException
   */
  protected List<Pair<String, String>> lookupValuesBAPI(final String[] mapping)
    throws ConnectionException {

    final String method = "lookupValuesBAPI";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Pair<String, String>> searchResult = null;
    try {
      final Function         function  = new Function(this.connection, mapping[0]);
      // TODO: verify if we have a dedicated method which does not require
      //       the specification of a null value for segment
      function.importSegmentValue(null, "OBJTYPE",   "USER",     false);
      function.importSegmentValue(null, "METHOD",    mapping[1], false);
      function.importSegmentValue(null, "PARAMETER", mapping[2], false);
      function.importSegmentValue(null, "FIELD",     mapping[3], false);

      if ((mapping[3].equalsIgnoreCase("TITLE_P")) || (mapping[3].equalsIgnoreCase("COMPANY")) || (mapping[3].equalsIgnoreCase("AGR_NAME"))) {
        final JCoStructure structure = function.importParameter().getStructure("EXPLICIT_SHLP");
        structure.setValue("SHLPNAME", mapping[6]);
        structure.setValue("SHLPTYPE", mapping[7]);
        if (mapping[3].equalsIgnoreCase("TITLE_P")) {
          final JCoTable table = function.tableParameter().getTable("SELECTION_FOR_HELPVALUES");
          table.appendRow();
          table.setValue("SELECT_FLD", mapping[8]);
          table.setValue("SIGN",       mapping[9]);
          table.setValue("OPTION",     mapping[10]);
          table.setValue("LOW",        mapping[11]);
        }
      }
      else if (mapping[3].equalsIgnoreCase("LIC_TYPE")) {
        final JCoTable table = function.tableParameter().getTable("SELECTION_FOR_HELPVALUES");
        table.appendRow();
        table.setValue("SELECT_FLD", mapping[6]);
        table.setValue("SIGN",       mapping[7]);
        table.setValue("OPTION",     mapping[8]);
        table.setValue("LOW",        mapping[9]);
      }

      function.execute(this.connection.retryConnectionCount(), this.connection.retryConnectionCount());
      searchResult = populateValuesBAPI(mapping, function);
    }
    catch (ConnectionException e) {
      throw e;
    }
    catch (Exception e) {
      throw new ConnectionException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return searchResult;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupValuesRFC
  /**
   ** Returns the lookup values executed using <code>RFC_READ_TABLE</code>.
   **
   ** @param  mapping            array of strings which contains the parameters
   **                            <code>RFC_READ_TABLE</code> executes.
   **
   ** @return                    the {@link List} containing lookup values
   **                            {@link Pair}s to be added to the lookup table.
   **
   ** @throws ConnectionException
   */
  protected List<Pair<String, String>> lookupValuesRFC(final String[] mapping)
    throws ConnectionException {

    final String method = "lookupValuesRFC";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Pair<String, String>> returning = null;
    try {
      // get the JCo Function by using the BAPI Name passed in and set the
      // fields to be executed for that BAPI
      final Function function = new Function(this.connection, mapping[0]);
      // TODO: verify if we have a dedicated method which does not require
      //       the specification of a null value for segment
      function.importSegmentValue(null, "QUERY_TABLE", mapping[1], false);

      if (mapping.length > 4) {
        final JCoTable returnOption = function.tableParameter().getTable("OPTIONS");
        returnOption.appendRow();
        returnOption.setValue("TEXT", "LANGU = '" + this.connection.systemLanguage() + "'");
      }
      function.execute(this.connection.retryConnectionCount(), this.connection.retryConnectionInterval());
      returning = populateValuesRFC(mapping, function);
    }
    catch (ConnectionException e) {
      throw e;
    }
    catch (Exception e) {
      throw new ConnectionException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateValuesBAPI
  /**
   ** Returns the lookup values executed using <code>RFC_READ_TABLE</code>.
   **
   ** @param  mapping            the Connection Object
   ** @param  function           String Array which contains the parameters RFC_READ_TABLE executes
   **
   ** @return                    the {@link List} containing lookup values
   **                            {@link Pair}s to be added to the lookup table.
   */
  protected List<Pair<String, String>> populateValuesBAPI(final String[] mapping, final Function function) {
    final String method = "populateValuesBAPI";
    trace(method, SystemMessage.METHOD_ENTRY);

    int encodedOffset = 0;
    int encodedLength = 0;
    int decodedOffset = 0;
    int decodedLength = 0;

    // loop through segments and get the corresponding encode and decode
    // values to be put in the Map
    final JCoTable segment     = function.tableParameter().getTable(("DESCRIPTION_FOR_HELPVALUES"));
    final int      segmentSize = segment.getNumRows();
    if (segmentSize != 0) {
      for (int i = 0; i < segmentSize; i++) {
        segment.setRow(i);
        final String field = segment.getString("FIELDNAME");

        if (field.equals(mapping[4].toString())) {
          encodedOffset  = Integer.parseInt(segment.getString("OFFSET"));
          encodedLength  = Integer.parseInt(segment.getString("LENG"));
          encodedLength += encodedOffset;
        }

        if (field.equals(mapping[5].toString())) {
          decodedOffset  = Integer.parseInt(segment.getString(("OFFSET")));
          decodedLength  = Integer.parseInt(segment.getString(("LENG")));
          decodedLength += decodedOffset;
        }
      }
    }
    // hit list matching selection criteria
    final JCoTable                   criteria     = function.tableParameter().getTable("HELPVALUES");
    final int                        criteriaSize = criteria.getNumRows();
    final List<Pair<String, String>> returning    = new ArrayList<Pair<String, String>>(criteriaSize);
    if (criteriaSize > 0) {
      String encoded = null;
      String decoded = null;
      int    i2      = -1;
      int    i3      = -1;
      for (int i = 0; i < criteriaSize; i++) {
        criteria.setRow(i);
        final String raw = criteria.getString("HELPVALUES");
        if (mapping[3].toString().equalsIgnoreCase("DCPFM")) {
          i2 = raw.indexOf('N');
          i3 = raw.indexOf('1');
        }
        if ((raw != null) && (raw.length() == 0)) {
          encoded = null;
          decoded = null;
        }
        else if (raw.length() < encodedLength) {
          encoded = raw.substring(encodedOffset);
          decoded = null;
        }
        else if ((raw.length() > encodedLength) && (raw.length() < decodedLength)) {
          encoded = raw.substring(encodedOffset, encodedLength);
          if (i2 != -1) {
            decoded = raw.substring(i2);
          }
          else if (i3 != -1) {
            decoded = raw.substring(i3);
          }
          else if ((decodedOffset > 0) && (raw.length() > decodedOffset)) {
            decoded = raw.substring(decodedOffset);
          }
        }
        else if (raw.length() > decodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);

          if (i2 != -1) {
            decoded = raw.substring(i2);
          }
          else if (i3 != -1) {
            decoded = raw.substring(i3);
          }
          else {
            decoded = raw.substring(decodedOffset, decodedLength);
          }
        }
        else if (raw.length() == encodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);
          decoded = null;
        }
        else if (raw.length() == decodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);

          if (i2 != -1) {
            decoded = raw.substring(i2);
          }
          else if (i3 != -1) {
            decoded = raw.substring(i3);
          }
          else {
            decoded = raw.substring(decodedOffset);
          }
        }

        if (!StringUtility.isEmpty(encoded)) {
          if (StringUtility.isEmpty(decoded)) {
            decoded = encoded;
          }
          if (this.connection.masterSystemName() != null) {
            if (!this.connection.cuaManaged() && ((mapping[3].equalsIgnoreCase("AGR_NAME") || (mapping[3].equalsIgnoreCase(("BAPIPROF")))))) {
              encoded = this.connection.masterSystemName() + "~" + encoded;
            }
          }
          returning.add(Pair.of(encoded.trim(), decoded.trim()));
        }
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateValuesRFC
  /**
   ** Returns lookup values executed using <code>RFC_READ_TABLE</code>
   **
   ** @param  mapping            the Connection Object
   ** @param  function           String Array which contains the parameters
   **                            RFC_READ_TABLE executes.
   **
   ** @return                    the {@link List} containing lookup values
   **                            {@link Pair}s to be added to the lookup table.
   **
   ** @throws ConnectionException
   */
  protected List<Pair<String, String>> populateValuesRFC(final String[] mapping, final Function function) {

    final String method = "populateValuesRFC";
    trace(method, SystemMessage.METHOD_ENTRY);
    int encodedOffset   = 0;
    int encodedLength   = 0;
    int decodedOffset   = 0;
    int decodedLength   = 0;
    int subsystemOffset = 0;
    int subsystemLength = 0;

    // loop through JCoTables and get the corresponding encode and decode
   // values to be put in Map
    final JCoTable segment     = function.tableParameter().getTable("FIELDS");
    final int      segmentSize = segment.getNumRows();
    if (segmentSize != 0) {
      for (int i = 0; i < segmentSize; i++) {
        segment.setRow(i);
        final String outboundName = segment.getString("FIELDNAME");
        if (outboundName.equals(mapping[2].toString())) {
          encodedOffset  = Integer.parseInt(segment.getString("OFFSET"));
          encodedLength  = Integer.parseInt(segment.getString("LENGTH"));
          encodedLength += encodedOffset;
        }

        if (outboundName.equals(mapping[3].toString())) {
          decodedOffset  = Integer.parseInt(segment.getString("OFFSET"));
          decodedLength  = Integer.parseInt(segment.getString("LENGTH"));
          decodedLength += decodedOffset;
        }

        if (!mapping[1].equalsIgnoreCase("USZBVLNDRC")) {
          if (outboundName.equals(mapping[4].toString())) {
            subsystemOffset  = Integer.parseInt(segment.getString("OFFSET"));
            subsystemLength  = Integer.parseInt(segment.getString("LENGTH"));
            subsystemLength += subsystemOffset;
          }
        }
      }
    }

    final JCoTable                   criteria     = function.tableParameter().getTable("DATA");
    final int                        criteriaSize = criteria.getNumRows();
    final List<Pair<String, String>> returning    = new ArrayList<Pair<String, String>>(criteriaSize);
    if (criteriaSize != 0) {
      for (int i = 0; i < criteriaSize; i++) {
        String encoded = null;
        String decoded = null;
        criteria.setRow(i);
        final String raw = criteria.getString("WA");
        if ((raw != null) && (raw.length() == 0)) {
          encoded = null;
          decoded = null;
        }
        else if (raw.length() < encodedLength) {
          encoded = raw.substring(encodedOffset);
          decoded = null;
        }
        else if ((raw.length() > encodedLength) && (raw.length() < decodedLength)) {
          encoded = raw.substring(encodedOffset, encodedLength);
          if ((decodedOffset > 0) && (raw.length() > decodedOffset)) {
            decoded = raw.substring(decodedOffset);
          }
        }
        else if (raw.length() > decodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);
          decoded = raw.substring(decodedOffset, decodedLength);
        }
        else if (raw.length() == encodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);
          decoded = null;
        }
        else if (raw.length() == decodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);
          encoded = raw.substring(decodedOffset);
        }
        if (StringUtility.isEmpty(decoded)) {
          decoded = encoded;
        }
        String sSubSystem = null;
        if (this.connection.masterSystemName() != null) {
          if (!StringUtility.isEmpty(encoded)) {
            if (!mapping[1].equalsIgnoreCase("USZBVLNDRC")) {
              sSubSystem = raw.substring(subsystemOffset, subsystemLength).trim();
              encoded = sSubSystem + "~" + encoded;
            }
            if (!mapping[1].equalsIgnoreCase("USZBVLNDRC")) {
              decoded = sSubSystem + "~" + decoded;
            }
          }
        }
        returning.add(Pair.of(encoded.trim(), decoded.trim()));
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notifyCallback
  /**
   ** Callback the {@link PairHandler} which is attached to the operator.
   */
  private void notifyCallback(final List<Pair<String, String>> data, final PairHandler handler) {
    final String method = "notifyCallback";
    trace(method, SystemMessage.METHOD_ENTRY);

    for (Pair<String, String> cursor : data)
      handler.handle(cursor);

    trace(method, SystemMessage.METHOD_EXIT);
  }
}