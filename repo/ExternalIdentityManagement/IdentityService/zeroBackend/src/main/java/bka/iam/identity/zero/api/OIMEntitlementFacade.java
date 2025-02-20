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

    File        :   OIMEntitlementFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    EntitlementFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
    1.1.0.0     2025-01-28  AFarkas     Added EntitlementEntity usage
*/

package bka.iam.identity.zero.api;

import bka.iam.identity.filter.SqlStringBuilder;
import bka.iam.identity.helper.Helper;
import bka.iam.identity.helper.SqlHelper;
import bka.iam.identity.zero.model.AppEntitlements;
import bka.iam.identity.zero.model.EntitlementAssignee;
import bka.iam.identity.zero.model.OIMEntitlement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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

import oracle.iam.identity.igs.model.AdditionalAttributeEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.Risk;
import oracle.iam.platform.OIMInternalClient;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.FormField;
import oracle.iam.provisioning.vo.FormInfo;

////////////////////////////////////////////////////////////////////////////////
// class OIMEntitlementFacade
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 * The session facade to manage {@link OIMEntitlement} entity.
 *
 * @author adrien.farkas@oracle.com
 * @version 1.1.0.0
 * @since 1.0.0.0
 */
@Stateless(name = OIMEntitlementFacade.NAME)
public class OIMEntitlementFacade {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  NAME   = "entitlementFacade";
  
  // Establish some reasonable defaults
  // Don't forger that SQL SELECT command indexes from 0!
  public static final int     START  = 1;
  public static final int     ITEMS  = 25;
  
  private static final String CLASS  = OIMEntitlementFacade.class.getName();
  private static final Logger LOGGER    = Logger.create(CLASS);

  private static final String SQL_TOTAL = "SELECT COUNT(*) AS total";
  private static final String SQL_QUERY = "SELECT ent_list.ent_list_key AS key," +
                                          " ent_list.ent_code AS code," +
                                          " ent_list.ent_value AS name," +
                                          " ent_list.ent_valid AS active," +
                                          " ent_list.ent_display_name AS displayName," +
                                          " ent_list.ent_list_create AS createDate," +
                                          " ent_list.ent_list_update AS updateDate," +
                                          " ent_list.ent_description AS description," +
                                          " svr.svr_name AS itResourceName," +
                                          " app_instance.app_instance_name AS appInstanceName," +
                                          " app_instance.app_instance_display_name AS appInstanceDisplayName," +
                                          " sdk.sdk_name as sdkName," +
                                          " sdc.sdc_name as sdcName," +
                                          " sdc.sdc_label as sdcLabel";

  private static final String SQL_FROM_WHERE_CLAUSE = " FROM ent_list," +
                                                      " svr," +
                                                      " app_instance," +
                                                      " sdc," +
                                                      " sdk" +
                                                      " WHERE ent_list.svr_key = svr.svr_key" +
                                                      " AND ent_list.svr_key = app_instance.itresource_key" +
                                                      " AND ent_list.sdc_key = sdc.sdc_key" +
                                                      " AND ent_list.sdk_key = sdk.sdk_key";
  private static final String SQL_PAGING_CLAUSE     = " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
  private static final String SQL_WHERE_ONE_CLAUSE  = " AND ent_list.ent_list_key = ?";
  private static final String SQL_ORDER_BY_CLAUSE   = " ORDER BY 1";

  private static final String SQL_QUERY_ASSIGNEES             = "SELECT u.usr_login AS login," +
                                                                " a.act_name AS org_name";
  private static final String SQL_FROM_WHERE_CLAUSE_ASSIGNEES = " FROM usr u," +
                                                                " act a," +
                                                                " ent_assign e" +
                                                                " WHERE e.usr_key = u.usr_key" + 
                                                                " AND u.act_key = a.act_key" + 
                                                                " AND e.ent_list_key=?";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2063532653579549662")
  private static final long  serialVersionUID = 6164211711658242785L;

  //////////////////////////////////////////////////////////////////////////////
  // non-static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private DataSource        operationsDS     = null;
  private Connection        connection       = null;

  // Used to lookup field and codes/values for resticting particular applications listing
  private OIMInternalClient client           = new OIMInternalClient(new Hashtable<String, String>());

  Map<String, String> columnMap              = new HashMap<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default constructor that also fills the columnMap.
   */
  public OIMEntitlementFacade() {
    // ensure inheritance
    super();
    final String method = "OIMEntitlementFacade";
    LOGGER.entering(CLASS, method);

    try {
      this.operationsDS = (DataSource)new InitialContext().lookup("jdbc/operationsDB");
    }
    catch (NamingException e) {
      LOGGER.throwing(CLASS, method, e);
    }

    // Fill the column mapper
    this.columnMap.put("KEY", "ent_list.ent_list_key");
    this.columnMap.put("CODE", "ent_list.ent_code");
    this.columnMap.put("NAME", "ent_list.ent_value");
    this.columnMap.put("ACTIVE", "ent_list.ent_valid");
    this.columnMap.put("DISPLAYNAME", "ent_list.ent_display_name");
    this.columnMap.put("CREATEDATE", "ent_list.ent_list_create");
    this.columnMap.put("UPDATEDATE", "ent_list.ent_list_update");
    this.columnMap.put("DESCRIPTION", "ent_list.ent_description");
    this.columnMap.put("ITRESOURCENAME", "svr.svr_name");
    this.columnMap.put("APPINSTANCENAME", "app_instance.app_instance_name");
    this.columnMap.put("APPINSTANCEDISPLAYNAME", "app_instance.app_instance_display_name");
    this.columnMap.put("NAMESPACE", "sdk.sdk_name");
    this.columnMap.put("FIELDNAME", "sdc.sdc_name");
    this.columnMap.put("FIELDLABEL", "sdc.sdc_label");

    LOGGER.exiting(CLASS, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Search for entities of type {@link OIMEntitlement} and returns
   ** a partial result based on given paging input criteria.
   ** <br>
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
   **                                  Possible object is {@link ListResponse} with paging
   **                                  information and {@link List} of {@link OIMEntitlement}
   **                                  entities.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public ListResponse<AppEntitlements> list(int start, int requestedItems, String filter)
    throws IllegalArgumentException {

    final String method = "list";
    LOGGER.entering(CLASS, method, "start=", start, "requestedItems=", requestedItems, "filter=", filter);

  /*
  * Maybe one day...
    final Collection<OIMEntitlementResource> entitlements = entitlements();
    final Collection<Generic> prepared = new ArrayList<Generic>(entitlements.size());
    final Preparer<Generic> preparer =
      Preparer.<Generic>build(TYPE, control.attribute(), control.exclude(), ResourceService.location(TYPE, context));
    for (OIMEntitlementResource cursor : entitlements) {
      final Generic resource = cursor.generic();
      preparer.locate(resource);
      prepared.add(resource);
    }
    return new oracle.hst.platform.rest.response.ListResponse<Generic>(prepared);
  */

    long total = -1;
    int actualItems = 0;

    // map of list of entitlements keyed by appInstanceName
    final Map<String, List<OIMEntitlement>> mapResult = new HashMap<>();
    // map of list of entitlements keyed by namespace
    final Map<String, List<String>>  nsMap = new HashMap<>();

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
      .append(SQL_TOTAL)
      .append(SQL_FROM_WHERE_CLAUSE)
    ;
    StringBuilder sqlFilterBuilder = new StringBuilder();

    // Just a flag indicating whether or not we should append filtering clause
    boolean filtering = false;
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
          filtering = true;
        }
      }
      catch (NullPointerException | ParseException | IllegalArgumentException e) {
        final String message = "Exception caught while parsing filter" + ((e instanceof IllegalArgumentException) ? ": " + e.getMessage() : "");
        LOGGER.error(CLASS, method, message);
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message, e);
      }
    }
    List<String> ignoredApps = Helper.getIgnoredApplications(client);
    if (ignoredApps.size() > 0) {
      // Explicitly add to filter exclusion for ignored applications
      sqlFilterBuilder.append(" AND ").append(columnMap.get("APPINSTANCENAME")).append(" NOT IN (");
      Iterator<String> ignoredAppsIterator = ignoredApps.iterator();

      while (ignoredAppsIterator.hasNext()) {
        sqlFilterBuilder.append('?');
        arguments.add(ignoredAppsIterator.next());
        if (ignoredAppsIterator.hasNext()) {
          sqlFilterBuilder.append(", ");
        }
      }
      sqlFilterBuilder.append(")");
      LOGGER.trace(CLASS, method, "Filtering clause arguments: " + arguments);
      LOGGER.trace(CLASS, method, "Filtering clause after ignored apps: " + sqlFilterBuilder.toString());
      filtering = true;
    }

    if (filtering) {
      sql.append(sqlFilterBuilder);
    }
    LOGGER.trace(CLASS, method, "Final SQL statement to retrieve total: " + sql.toString());
    // First, populate the "total"
    try (
      PreparedStatement statement = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
      ResultSet resultSet = statement.executeQuery();
    ) {
      if (resultSet.next()) {
        total = resultSet.getLong("total");
        LOGGER.trace(CLASS, method, "Counted " + total + " results");
      }
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving total number of entitlements";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }

    sql = new StringBuilder()
      .append(SQL_QUERY)
      .append(SQL_FROM_WHERE_CLAUSE)
      .append(sqlFilterBuilder)
      .append(SQL_ORDER_BY_CLAUSE)
      .append(SQL_PAGING_CLAUSE)
    ;
    
    LOGGER.trace(CLASS, method, "Final SQL statement to retrieve entitlements: " + sql.toString());
    // Add start and items to the set
    arguments.add(start - 1);
    arguments.add(requestedItems);
    LOGGER.trace(CLASS, method, "Ignored applications: " + ignoredApps);
    
//    Map <String,List<SchemaAttribute>> schemaAttrMap = new HashMap<>();
//    Map <String,Map<String,String>> attributeMap = new HashMap<>();

    try (
      PreparedStatement statement = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
      ResultSet resultSet = statement.executeQuery();
    ) {
      while (resultSet.next()) {
        actualItems++;
        String appName = resultSet.getString("appInstanceName");
        String namespace = resultSet.getString("sdkName");
        String fieldName = resultSet.getString("sdcName");
        String fieldLabel = resultSet.getString("sdcLabel");
        if (!ignoredApps.contains(appName)) {
          OIMEntitlement entitlement = new OIMEntitlement()
            .key(resultSet.getString("key"))
            .code(resultSet.getString("code"))
            .name(resultSet.getString("name"))
            .active("1".equals(resultSet.getString("active")) ? true : false)
            .displayName(resultSet.getString("displayName"))
            .description(resultSet.getString("description"))
            .createDate(resultSet.getDate("createDate"))
            .updateDate(resultSet.getDate("updateDate"))
            .itResourceName(resultSet.getString("itResourceName"))
            .appInstanceName(appName)
            .appInstanceDisplayName(resultSet.getString("appInstanceDisplayName"))
            .namespace(resultSet.getString("sdkName"))
            .fieldName(resultSet.getString("sdcName"))
            .fieldLabel(resultSet.getString("sdcLabel"))
//            .fieldLabel(attributeMap.get(entNamespace).get("fieldLabel"))
          ;
          LOGGER.debug(CLASS, method, "Constructed entitlement: " + entitlement);
          arguments = new ArrayList<>();
          sql = new StringBuilder()
            .append(SQL_QUERY_ASSIGNEES)
            .append(SQL_FROM_WHERE_CLAUSE_ASSIGNEES)
            .append(SQL_ORDER_BY_CLAUSE)
          ;
          
          LOGGER.trace(CLASS, method, "Final SQL statement to retrieve entitlement assignees: " + sql.toString());
          // Add start and items to the set
          arguments.add(entitlement.key());
          try (
            PreparedStatement assigneesStmt = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
            ResultSet assigneesResultSet = assigneesStmt.executeQuery();
          ) {
            List<EntitlementAssignee> assignees = new ArrayList<>();
            while (assigneesResultSet.next()) {
              EntitlementAssignee assignee = new EntitlementAssignee(
                assigneesResultSet.getString("login")
              , assigneesResultSet.getString("org_name")
              );
              assignees.add(assignee);
            }
            entitlement.assignees(assignees);
          }
          LOGGER.debug(CLASS, method, "Updated entitlement with assignees: " + entitlement);
          List<OIMEntitlement> tmpAppList = mapResult.get(appName);
          if (tmpAppList == null) {
            tmpAppList = new ArrayList<>();
          }
          tmpAppList.add(entitlement);
          mapResult.put(appName, tmpAppList);
        }
        else {
          LOGGER.trace(CLASS, method, "Ignoring app " + appName + " as per Lookup configuration");
        }
      }
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving entitlements";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    // Re-format the results to use the per-AppInstance model POJO so that
    // appplication instance name is an attribute value, not attribute name
    final List<AppEntitlements> result = new ArrayList<>();
    for (String key : mapResult.keySet()) {
      AppEntitlements appEntitlement = new AppEntitlements(key, mapResult.get(key));
      result.add(appEntitlement);
    }
    return new ListResponse<>(total, start, actualItems, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Search for entities of type {@link OIMEntitlement} and returns
   ** a partial result based on given paging input criteria.
   ** <br>
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
   ** @param newWay                    a {@link String} signature distinguisher to use the new
   **                                  return format.
   **                                  Not actually used.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the collection of entities.
   **                                  <br>
   **                                  Possible object is {@link ListResponse} with paging
   **                                  information and {@link List} of {@link OIMEntitlement}
   **                                  entities.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public ListResponse<EntitlementEntity> list(int start, int requestedItems, String filter, String newWay)
    throws IllegalArgumentException {

    final String method = "list";
    LOGGER.entering(CLASS, method, "start=", start, "requestedItems=", requestedItems, "filter=", filter);

    final List<EntitlementEntity> result = new ArrayList<>();
    long total = -1;
    int actualItems = 0;

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
      .append(SQL_TOTAL)
      .append(SQL_FROM_WHERE_CLAUSE)
    ;
    StringBuilder sqlFilterBuilder = new StringBuilder();

    // Just a flag indicating whether or not we should append filtering clause
    boolean filtering = false;
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
          filtering = true;
        }
      }
      catch (NullPointerException | ParseException | IllegalArgumentException e) {
        final String message = "Exception caught while parsing filter" + ((e instanceof IllegalArgumentException) ? ": " + e.getMessage() : "");
        LOGGER.error(CLASS, method, message);
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message, e);
      }
    }
    List<String> ignoredApps = Helper.getIgnoredApplications(client);
    if (ignoredApps.size() > 0) {
      // Explicitly add to filter exclusion for ignored applications
      sqlFilterBuilder.append(" AND ").append(columnMap.get("APPINSTANCENAME")).append(" NOT IN (");
      Iterator<String> ignoredAppsIterator = ignoredApps.iterator();

      while (ignoredAppsIterator.hasNext()) {
        sqlFilterBuilder.append('?');
        arguments.add(ignoredAppsIterator.next());
        if (ignoredAppsIterator.hasNext()) {
          sqlFilterBuilder.append(", ");
        }
      }
      sqlFilterBuilder.append(")");
      LOGGER.trace(CLASS, method, "Filtering clause arguments: " + arguments);
      LOGGER.trace(CLASS, method, "Filtering clause after ignored apps: " + sqlFilterBuilder.toString());
      filtering = true;
    }

    if (filtering) {
      sql.append(sqlFilterBuilder);
    }
    LOGGER.trace(CLASS, method, "Final SQL statement to retrieve total: " + sql.toString());
    // First, populate the "total"
    try (
      PreparedStatement statement = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
      ResultSet resultSet = statement.executeQuery();
    ) {
      if (resultSet.next()) {
        total = resultSet.getLong("total");
        LOGGER.trace(CLASS, method, "Counted " + total + " results");
      }
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving total number of entitlements";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }

    sql = new StringBuilder()
      .append(SQL_QUERY)
      .append(SQL_FROM_WHERE_CLAUSE)
      .append(sqlFilterBuilder)
      .append(SQL_ORDER_BY_CLAUSE)
      .append(SQL_PAGING_CLAUSE)
    ;
    
    LOGGER.trace(CLASS, method, "Final SQL statement to retrieve entitlements: " + sql.toString());
    // Add start and items to the set
    arguments.add(start - 1);
    arguments.add(requestedItems);
    LOGGER.trace(CLASS, method, "Ignored applications: " + ignoredApps);
    
  //    Map <String,List<SchemaAttribute>> schemaAttrMap = new HashMap<>();
  //    Map <String,Map<String,String>> attributeMap = new HashMap<>();

    try (
      PreparedStatement statement = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
      ResultSet resultSet = statement.executeQuery();
    ) {
      while (resultSet.next()) {
        actualItems++;
        String appName = resultSet.getString("appInstanceName");
        if (!ignoredApps.contains(appName)) {
          result.add(lookup(resultSet.getString("key"), ""));
        }
        else {
          LOGGER.trace(CLASS, method, "Ignoring app " + appName + " as per Lookup configuration");
        }
      }
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving entitlements";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    return new ListResponse<>(total, start, actualItems, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Request to retrieve a certain {@link OIMEntitlement} by its <code>key</code>.
   **
   ** @param key                       the identifier of the {@link OIMEntitlement} to lookup.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the {@link OIMEntitlement} mapped at <code>key</code>.
   **                                  <br>
   **                                  Can be <code>null</code>.
   **                                  <br>
   **                                  Possible object is {@link OIMEntitlement}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public OIMEntitlement lookup(final String key)
    throws IllegalArgumentException {

    final String method = "lookup";
    // Check is key only contains digits, if it does not log a message and return a "not found" right away
    if (!key.matches("[0-9]+")) {
      final StringBuilder message = new StringBuilder("Entitlement ID is not numeric-only: ").append(key);
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.exiting(CLASS, method, "entitlement=", null);
      return null;
    }
    LOGGER.entering(CLASS, method, "key=", key);

    OIMEntitlement entitlement = null;
    try {
      if (connection == null) {
        connection = operationsDS.getConnection();
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
    List<String> ignoredApps = Helper.getIgnoredApplications(client);
    List<Object> arguments = new ArrayList<>();
    arguments.add(key);
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
        String appName = resultSet.getString("appInstanceName");
        if (!ignoredApps.contains(appName)) {

          entitlement = new OIMEntitlement()
            .key(resultSet.getString("key"))
            .code(resultSet.getString("code"))
            .name(resultSet.getString("name"))
            .active("1".equals(resultSet.getString("active")) ? true : false)
            .displayName(resultSet.getString("displayName"))
            .description(resultSet.getString("description"))
            .createDate(resultSet.getDate("createDate"))
            .updateDate(resultSet.getDate("updateDate"))
            .itResourceName(resultSet.getString("itResourceName"))
            .appInstanceName(appName)
            .appInstanceDisplayName(resultSet.getString("appInstanceDisplayName"))
            .namespace(resultSet.getString("sdkName"))
            .fieldName(resultSet.getString("sdcName"))
            .fieldLabel(resultSet.getString("sdcLabel"))
          ;
          i++;
          arguments = new ArrayList<>();
          sql = new StringBuilder()
            .append(SQL_QUERY_ASSIGNEES)
            .append(SQL_FROM_WHERE_CLAUSE_ASSIGNEES)
            .append(SQL_ORDER_BY_CLAUSE)
          ;
          LOGGER.trace(CLASS, method, "Final SQL statement to retrieve entitlement assignees: " + sql.toString());
          // Add start and items to the set
          arguments.add(entitlement.key());
          try (
            PreparedStatement assigneesStmt = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
            ResultSet assigneesResultSet = assigneesStmt.executeQuery();
          ) {
            List<EntitlementAssignee> assignees = new ArrayList<>();
            while (assigneesResultSet.next()) {
              EntitlementAssignee assignee = new EntitlementAssignee(
                assigneesResultSet.getString("login")
              ,assigneesResultSet.getString("org_name")
              );
              assignees.add(assignee);
            }
            entitlement.assignees(assignees);
          }
          LOGGER.debug(CLASS, method, "Updates entitlement with assignees: " + entitlement);
        }
        else {
          LOGGER.trace(CLASS, method, "Ignoring app " + appName + " as per Lookup configuration");
        }
      }
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving entitlement";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    if (entitlement == null) {
      LOGGER.exiting(CLASS, method, "entitlement=", null);
    }
    else {
      LOGGER.exiting(CLASS, method, "entitlement=", entitlement.toString());
    }
    return entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Request to retrieve a certain {@link EntitlementEntity} by its <code>key</code>.
   **
   ** @param key                       the identifier of the {@link EntitlementEntity} to lookup.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @param newWay                    a {@link String} signature distinguisher to use the new
   **                                  return format.
   **                                  Not actually used.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the {@link EntitlementEntity} mapped at <code>key</code>.
   **                                  <br>
   **                                  Can be <code>null</code>.
   **                                  <br>
   **                                  Possible object is {@link EntitlementEntity}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public EntitlementEntity lookup(final String key, String newWay)
    throws IllegalArgumentException {

    final String method = "lookup(newWay)";
    // Check is key only contains digits, if it does not log a message and return a "not found" right away
    if (!key.matches("[0-9]+")) {
      final StringBuilder message = new StringBuilder("Entitlement ID is not numeric-only: ").append(key);
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.exiting(CLASS, method, "entitlement=", null);
      return null;
    }
    LOGGER.entering(CLASS, method, "key=", key);
    
    EntitlementEntity entEnt = Entity.entitlement(EntitlementEntity.Action.assign, Risk.none);
    try {
      if (connection == null) {
        connection = operationsDS.getConnection();
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
    List<String> ignoredApps = Helper.getIgnoredApplications(client);
    List<Object> arguments = new ArrayList<>();
    arguments.add(key);
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
        String appName = resultSet.getString("appInstanceName");
        String childFormName = resultSet.getString("sdkName");
        String fieldName = resultSet.getString("sdcName");
        String fieldValue = resultSet.getString("code");
        if (!ignoredApps.contains(appName)) {
          entEnt.id(resultSet.getString("key"))
                // Encode? Decode?
//                .value(fieldName, resultSet.getString("name"))
                .value(fieldName, resultSet.getString("code"))
                .displayName(resultSet.getString("displayName"))
                .createDate(resultSet.getDate("createDate"))
                .updateDate(resultSet.getDate("updateDate"));
          i++;

          ApplicationInstance appInst = Helper.getApplicationInstance(client, appName);
          FormInfo accountForm = appInst.getAccountForm();
          String formName = accountForm.getName();
          String namingField = "";
          for (FormField formField : accountForm.getFormFields()) {
            if ("true".equals(formField.getProperty("AccountName"))) {
              namingField = formField.getName();
              break;
            }
          }
          List<FormInfo> childFormList = appInst.getChildForms();
          List<String> childFields = new ArrayList<>();
          for (FormInfo childForm : childFormList) {
            if (!childFormName.equals(childForm.getName())) {
              continue;
            }
            for (FormField childField : childForm.getFormFields()) {
              if (fieldName.equals(childField.getName())) {
                continue;
              }
              childFields.add(childField.getName());
            }
            break;
          }

          arguments = new ArrayList<>();
          sql = new StringBuilder("SELECT NULL, ");
          if (childFields.size() > 0) {
            sql.append(Helper.convertToCsv(childFields));
            sql.append(",");
          }
          sql.append(" LISTAGG(u.").append(namingField).append(", ';') WITHIN GROUP (ORDER BY NULL");
          if (childFields.size() > 0) {
            sql.append(",");
            sql.append(Helper.convertToCsv(childFields));
          }
          sql.append(") AS users ");
          sql.append("FROM ");
          sql.append(formName).append(" u, ");
          sql.append(childFormName).append(" p ");
          sql.append("WHERE ").append(fieldName).append(" = ?");
          arguments.add(fieldValue);
          sql.append(" AND ");
          sql.append("p.").append(formName).append("_KEY");
          sql.append(" = ");
          sql.append("u.").append(formName).append("_KEY ");
          sql.append("GROUP BY NULL");
          if (childFields.size() > 0) {
            sql.append(",");
            sql.append(Helper.convertToCsv(childFields));
          }
          try (
            PreparedStatement assigneesStmt = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
            ResultSet assigneesResultSet = assigneesStmt.executeQuery();
          ) {
            while (assigneesResultSet.next()) {
              AdditionalAttributeEntity addAttr = Entity.additionalAttribute();
              entEnt.addAdditionalAttribute(addAttr);
              for (String additionalColumn : childFields) {
                String value = assigneesResultSet.getString(additionalColumn);
                if (value != null) {
                  // Encode? Decode?
//                  String decodedValue = Helper.decodeLookupValue(this.client, additionalColumn, value);
//                  if (decodedValue != null) {
//                    addAttr.put(additionalColumn, decodedValue);
//                  } else {
//                    addAttr.put(additionalColumn, value);
//                  }
                  addAttr.put(additionalColumn, value);
                }
              }
              addAttr.members(Arrays.asList(assigneesResultSet.getString("users").split(";")));
            }
          }
        }
        else {
          LOGGER.trace(CLASS, method, "Ignoring app " + appName + " as per Lookup configuration");
        }
      }
    }
    catch (SQLException e) {
      final StringBuilder message = new StringBuilder("Exception while retrieving entitlement: ");
      message.append(e.getMessage());
      LOGGER.error(CLASS, method, message.toString());
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message.toString(), e);
    } finally {
      this.client.logout();
    }

    LOGGER.exiting(CLASS, method, "entitlement=", entEnt.toString());
    return entEnt;
  }
}