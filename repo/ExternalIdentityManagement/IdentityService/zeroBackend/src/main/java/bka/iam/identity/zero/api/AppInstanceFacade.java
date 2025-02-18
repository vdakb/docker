/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   ZeRo Backend

    File        :   AppInstanceFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    AppInstanceFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.api;

import bka.iam.identity.filter.SqlStringBuilder;
import bka.iam.identity.helper.Helper;
import bka.iam.identity.helper.SqlHelper;
import bka.iam.identity.zero.model.AppInstance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import oracle.hst.platform.core.entity.Filter;
import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.rest.response.ListResponse;

import oracle.iam.platform.OIMInternalClient;

////////////////////////////////////////////////////////////////////////////////
// class AppInstanceFacade
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 * The session facade to manage {@link AppInstance} entity.
 *
 * @author adrien.farkas@oracle.com
 * @version 1.0.0.0
 * @since 1.0.0.0
 */
@Stateless(name = AppInstanceFacade.NAME)
public class AppInstanceFacade {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String  NAME     = "AppInstanceFacade";
  
  // Establish some reasonable defaults
  // Don't forger that SQL SELECT command indexes from 0!
  public static final int    START = 1;
  public static final int    ITEMS = 25;

  private static final String CLASS = AppInstanceFacade.class.getName();
  private static final Logger LOGGER   = Logger.create(CLASS);
  
  private static final String SQL_TOTAL             = "SELECT COUNT(*) AS total";
  private static final String SQL_QUERY             = "SELECT app_instance.app_instance_key AS key," +
                                                     " app_instance.app_instance_name AS name," +
                                                     " app_instance.app_instance_display_name AS displayName," +
                                                     " app_instance.create_date AS createDate," +
                                                     " app_instance.last_update_date AS updateDate," +
                                                     " app_instance.app_instance_description AS description," + 
                                                     " svr.svr_name AS itResourceName";
  private static final String SQL_FROM_WHERE_CLAUSE = " FROM app_instance," +
                                                    " svr" +
                                               " WHERE app_instance.itresource_key = svr.svr_key";
  private static final String SQL_PAGING_CLAUSE     = " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
  private static final String SQL_WHERE_ONE_CLAUSE  = " AND app_instance.app_instance_key = ?";
  private static final String SQL_ORDER_BY_CLAUSE   = " ORDER BY 1";

  private DataSource operationsDS            = null;
  private Connection connection              = null;
  
  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2063532653579549662")
  private static final long  serialVersionUID = 6164211711658242785L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  // Used to lookup field and codes/values for resticting particular applications listing
  private OIMInternalClient client           = new OIMInternalClient(new Hashtable<String, String>());
  
  Map<String, String> columnMap              = new HashMap<>();
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AppInstanceFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AppInstanceFacade() {
    // ensure inheritance
    super();

    // initialize instance
    final String method = "<init>";
    LOGGER.entering(CLASS, method);
    try {
      this.operationsDS = (DataSource)new InitialContext().lookup("jdbc/operationsDB");
    }
    catch (NamingException e) {
      LOGGER.throwing(CLASS, method, e);
    }

    // Fill the column mapper
    columnMap.put("KEY",            "app_instance.app_instance_key");
    columnMap.put("NAME",           "app_instance.app_instance_name");
    columnMap.put("DISPLAYNAME",    "app_instance.app_instance_display_name");
    columnMap.put("CREATEDATE",     "app_instance.create_date");
    columnMap.put("UPDATEDATE",     "app_instance.last_update_date");
    columnMap.put("DESCRIPTION",    "app_instance.app_instance_description");
    columnMap.put("ITRESOURCENAME", "svr.svr_name");
    
    LOGGER.exiting(CLASS, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Search for entities of type {@link AppInstance} and returns
   ** a partial result based on given paging input criteria.
   ** <p>
   ** To ensure that the pagination approach works as expected, it have to
   ** ensured that the query always returns the result in the same order. This
   ** is only the case if the query contains an ORDER BY clause. Otherwise, the
   ** order of the result set is undefined and might change.
   **
   ** @param  start                    the starting index for the search.
   **                                  <br>
   **                                  Allowed object is {@link int}.
   **
   ** @param  requestedItems           start the starting index for the search.
   **                                  <br>
   **                                  Allowed object is {@link int}.
   **
   ** @param  filter                   filter to apply on the search.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the collection of entities.
   **                                  <br>
   **                                  Possible object is {@link ListResponse} with
   **                                  paging information and list of
   **                                  {@link AppInstance} entities.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public ListResponse<AppInstance> list(int start, int requestedItems, String filter)
    throws IllegalArgumentException {

    final String method = "list";
    LOGGER.entering(CLASS, method, "start=", start, "requestedItems=", requestedItems, "filter=", filter);

    long total = -1;
    int actualItems = 0;
    final List<AppInstance> listResult = new ArrayList<>();

    try {
      if (this.connection == null) {
        this.connection = this.operationsDS.getConnection();
      }
    }
    catch (SQLException e) {
      final String message = "Exception caught while retrieving database connection";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }

    StringBuilder sql = new StringBuilder().append(SQL_TOTAL).append(SQL_FROM_WHERE_CLAUSE);
    StringBuilder sqlFilterBuilder = new StringBuilder();

    // We use the same arguments objects - for total it only contains the filter values,
    // for the actual retrieval start and items are added.
    List<Object> arguments = new ArrayList<>();
    if (filter != null) {
      try {
        sqlFilterBuilder.append(" AND ");
        Filter f = Filter.from(filter);
        LOGGER.trace(CLASS, method, "Filter parsed: " + f);
        SearchFilter searchFilter = bka.iam.identity.filter.Filter.translate(f);
        LOGGER.trace(CLASS, method, "SearchFilter parsed: " + searchFilter);
        if (searchFilter != null) {
          SqlStringBuilder.filter(sqlFilterBuilder, searchFilter, arguments, columnMap);
          LOGGER.trace(CLASS, method, "Filtering clause: " + sqlFilterBuilder.toString());
          sql.append(sqlFilterBuilder);
        }
      }
      catch (NullPointerException | ParseException | IllegalArgumentException e) {
        final String message = "Exception caught while parsing filter" + ((e instanceof IllegalArgumentException) ? ": " + e.getMessage() : "");
        LOGGER.error(CLASS, method, message);
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message, e);
      }
    }

    LOGGER.trace(CLASS, method, "Final SQL statement to retrieve total: " + sql.toString());
    // First, populate the "total"
    try (PreparedStatement statement = SqlHelper.createPreparedStatement(this.connection, sql.toString(), arguments);
      ResultSet resultSet = statement.executeQuery()) {
      if (resultSet.next()) {
        total = resultSet.getLong("total");
        LOGGER.trace(CLASS, method, "Counted " + total + " results");
      }
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving total number of application instances";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }

    sql = new StringBuilder()
      .append(SQL_QUERY)
      .append(SQL_FROM_WHERE_CLAUSE)
      .append(sqlFilterBuilder)
      .append(SQL_ORDER_BY_CLAUSE)
      .append(SQL_PAGING_CLAUSE);
    
    LOGGER.trace(CLASS, method, "Final SQL statement to retrieve application instances: " + sql.toString());
    // Add start and items to the set
    arguments.add(start - 1);
    arguments.add(requestedItems);
    
    List<String> ignoredApps = Helper.getIgnoredApplications(client);
    LOGGER.trace(CLASS, method, "Ignored applications: " + ignoredApps);
    try (
      PreparedStatement statement = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
      ResultSet         resultSet = statement.executeQuery();) {
      while (resultSet.next()) {
        String appName = resultSet.getString("name");
        if (!ignoredApps.contains(appName)) {
          actualItems++;
          listResult.add(
            new AppInstance()
              .key(resultSet.getInt("key"))
              .name(appName)
              .displayName(resultSet.getString("displayName"))
              .description(resultSet.getString("description"))
              .createDate(resultSet.getDate("createDate"))
              .updateDate(resultSet.getDate("updateDate"))
              .itResourceName(resultSet.getString("itResourceName"))
          );
          }
        else {
          LOGGER.trace(CLASS, method, "Ignoring app " + appName + " as per Lookup configuration");
        }
      }
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving application instances";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    return new ListResponse<>(total, start, actualItems, listResult);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Request to retrieve a certain {@link AppInstance} by its <code>key</code>.
   **
   ** @param name                      the name of the {@link AppInstance} to lookup.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the {@link AppInstance} mapped at <code>key</code>.
   **                                  <br>
   **                                  Can be <code>null</code>.
   **                                  <br>
   **                                  Possible object is {@link AppInstance}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   */
  public AppInstance lookup(final String name)
    throws IllegalArgumentException {
    final String method = "lookup";
    LOGGER.entering(CLASS, method, "name=", name);
    AppInstance result = null;
    try {
      if (this.connection == null) {
        this.connection = this.operationsDS.getConnection();
      }
    }
    catch (SQLException e) {
      final String message = "Exception caught while retrieving database connection";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }

    StringBuilder sql = new StringBuilder()
      .append(SQL_QUERY)
      .append(SQL_FROM_WHERE_CLAUSE)
      .append(SQL_WHERE_ONE_CLAUSE)
      .append(SQL_ORDER_BY_CLAUSE)
    ;
    int i = 0;
    List<Object> arguments = new ArrayList<>();
    arguments.add(name);
    try (
      PreparedStatement statement = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
      ResultSet resultSet = statement.executeQuery();
    ) {
      while (resultSet.next()) {
        if (i > 0) {
          final String message = "More than one entry found, this should not happen";
          LOGGER.error(CLASS, method, message);
          throw new IllegalArgumentException(message);
        }
        String appName = resultSet.getString("name");
        if (!Helper.getIgnoredApplications(client).contains(appName)) {
          result = new AppInstance()
            .key(resultSet.getInt("key"))
            .name(appName)
            .displayName(resultSet.getString("displayName"))
            .description(resultSet.getString("description"))
            .createDate(resultSet.getDate("createDate"))
            .updateDate(resultSet.getDate("updateDate"))
            .itResourceName(resultSet.getString("itResourceName"))
          ;
          i++;
        }
        else {
          LOGGER.trace(CLASS, method, "Ignoring app " + appName + " as per Lookup configuration");
        }
      }
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving application instances";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    if (result == null) {
      LOGGER.exiting(CLASS, method, "result=", null);
    }
    else {
      LOGGER.exiting(CLASS, method, "result=", result.toString());
    }
    return result;
  }
}