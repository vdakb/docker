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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   ZeRo Backend

    File        :   PolicyFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    PolicyFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.api;

import bka.iam.identity.filter.SqlStringBuilder;
import bka.iam.identity.helper.Helper;
import bka.iam.identity.helper.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.security.auth.login.LoginException;

import javax.sql.DataSource;

import oracle.hst.platform.core.entity.Filter;
import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.response.ListResponse;

import oracle.iam.accesspolicy.api.AccessPolicyService;
import oracle.iam.accesspolicy.exception.AccessPolicyServiceException;
import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.accesspolicy.vo.AccessPolicyElement;
import oracle.iam.accesspolicy.vo.ChildAttribute;
import oracle.iam.accesspolicy.vo.ChildRecord;
import oracle.iam.accesspolicy.vo.DefaultData;
import oracle.iam.accesspolicy.vo.Record;
import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.AdditionalAttributeEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.NamespaceEntity;
import oracle.iam.identity.igs.model.PolicyEntity;
import oracle.iam.identity.igs.model.Risk;
import oracle.iam.identity.igs.model.Schema;
import oracle.iam.platform.OIMInternalClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.FormField;
import oracle.iam.provisioning.vo.FormInfo;

////////////////////////////////////////////////////////////////////////////////
// class PolicyFacade
// ~~~~~ ~~~~~~~~~~~~
/**
 * The session facade to manage {@link PolicyEntity} entity.
 *
 * @author adrien.farkas@oracle.com
 * @version 1.0.0.0
 * @since 1.0.0.0
 */
@Stateless(name = PolicyFacade.NAME)
public class PolicyFacade
{

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String  NAME         = "PolicyFacade";

  private static final String CLASS        = PolicyFacade.class.getName();
  private static final Logger LOGGER       = Logger.create(CLASS);
  
  // Establish some reasonable defaults
  public static final int     START        = 1;
  public static final int     ITEMS        = 25;

  private static final String SQL_TOTAL              = "SELECT COUNT(pol_key) AS total" +
                                                       " FROM pol," +
                                                           " usr" +
                                                       " WHERE pol.pol_owner = usr.usr_key";
  private static final String SQL_QUERY              = "SELECT pol.pol_key AS key," +
                                                             " pol.pol_name AS name," +
                                                             " pol.pol_description AS description," +
                                                             " pol.pol_priority AS priority," +
                                                             " usr.usr_login AS owner_name," +
                                                             " pol.pol_owner_type AS owner_type," +
                                                             " LISTAGG(app.app_instance_name, ';') WITHIN GROUP (ORDER BY pol.pol_name) AS apps";
  private static final String SQL_FROM_WHERE_CLAUSE  = " FROM pol," +
                                                            " usr," +
                                                            " pop," +
                                                            " app_instance app" +
                                                       " WHERE pol.pol_owner = usr.usr_key" +
                                                         " AND pol.pol_key = pop.pol_key" +
                                                         " AND pop.app_instance_key = app.app_instance_key";
  private static final String SQL_FILTER_SPECIAL_ON  = " AND usr.usr_login = ?";
  private static final String SQL_FILTER_SPECIAL_OFF = " AND usr.usr_login != ?";
  private static final String SQL_GROUP_BY_CLAUSE    = " GROUP BY pol.pol_key," +
                                                               " pol.pol_name," +
                                                               " pol.pol_description," +
                                                               " pol.pol_priority," +
                                                               " usr.usr_login," +
                                                               " pol.pol_owner_type";
  private static final String SQL_ORDER_BY_CLAUSE    = " ORDER BY ";
  private static final String SQL_PAGING_CLAUSE      = " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

  private static final String DEFAULT_SORTING_ATTR   = "NAME";
  
  //////////////////////////////////////////////////////////////////////////////
  // non-static final attributes
  //////////////////////////////////////////////////////////////////////////////

  
  private String              specialUser  = "xelsysadm";
  private DataSource          operationsDS = null;
  private Connection          connection   = null;
  private OIMInternalClient   client       = new OIMInternalClient(new Hashtable<String, String>());
  private Map<String, String> columnMap    = new TreeMap<>();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2063532653579549662")
  private static final long  serialVersionUID = 6164211711658242785L;

  //////////////////////////////////////////////////////////////////////////////
  // enum Result
  // ~~~~ ~~~~~~
  /**
   ** This enum store the grammar's constants of {@link Result}s.
   ** <p>
   ** The following schema fragment specifies the expected result returned by
   ** method(s) within this class.
   ** <pre>
   **   &lt;simpleType name="Result"&gt;
   **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
   **       &lt;enumeration value="ok"/&gt;
   **       &lt;enumeration value="other"/&gt;
   **       &lt;enumeration value="nontFound"/&gt;
   **     &lt;/restriction&gt;
   **   &lt;/simpleType&gt;
   ** </pre>
   */
  public static enum Result {
      /** The encoded action values that can by applied on accounts. */
      ok("ok")
    , other("other")
    , notFound("notFound")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The human readable state value for this <code>Action</code>. */
    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Action</code> with a single state.
     **
     ** @param  id               the human readable state value for this
     **                          <code>Action</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    Result(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Result</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Result</code> should
     **                          be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Result</code> mapped at
     **                          <code>id</code>.
     **                          <br>
     **                          Possible object is <code>Result</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Result</code>.
     */
    public static Result from(final String id) {
      for (Result cursor : Result.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PolicyFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PolicyFacade() {
    // ensure inheritance
    super();
    final String method = "PolicyFacade";
    LOGGER.entering(CLASS, method);

    try {
      this.operationsDS = (DataSource) new InitialContext().lookup("jdbc/operationsDB");
    } catch (NamingException e) {
      // Should not actually happen, re-throw
      final String message = e.getMessage();
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    
    // Fill the column mapper
    this.columnMap.put("KEY", "pol.pol_key");
    this.columnMap.put("NAME", "pol.pol_name");
    this.columnMap.put("DESCRIPTION", "pol.pol_description");
    this.columnMap.put("PRIORITY", "pol.pol_priority");
    this.columnMap.put("OWNERLOGIN", "usr.usr_login");
    this.columnMap.put("OWNERTYPE", "pol.pol_owner_type");
    this.columnMap.put("APPS", "apps");
    
    LOGGER.exiting(CLASS, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Request to retrieve a certain {@link PolicyEntity} by its <code>name</code>.
   **
   ** @param  start              requested paging start item.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @param  requestedItems     requested paging number of items. Can be lower
   **                            if there are less results than start + items.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @param  filter             SCIM-like filter for filtering resulting
   **                            objects to be returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  sortBy             name of the attribute(s) to use to sort the resulting
   **                            data.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  sortOrder          ordering to use for the attributes names. Possible
   **                            values are "ascending" (default when not supplied) and
   **                            "descending".
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param isSpecial           flag indicating whether to list special template
   **                            Access Policies (when true) or regular ones
   **                            (when false).
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    list of the {@link PolicyEntity} mapped at <code>name</code>.
   **                            <br>
   **                            Possible object is {@link ListResponse} of {@link PolicyEntity}
   **                            objects.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public ListResponse<PolicyEntity> list(int start, int requestedItems, String filter, String sortBy, String sortOrder, Boolean isSpecial)
    throws IllegalArgumentException {
    final String method = "list";
    LOGGER.entering(CLASS, method,
                    "start=", start,
                    "requestedItems=", requestedItems,
                    "filter", filter,
                    "sortBy", sortBy,
                    "sortOrder", sortOrder,
                    "isSpecial=", isSpecial);

    long total = -1;
    int actualItems = 0;
    List<PolicyEntity> result = new ArrayList<>();
    
    // Check arguments first
    if (sortBy != null && this.columnMap.get(sortBy.toUpperCase()) == null) {
      final String message = "Invalid sort attribute specified";
      LOGGER.error(CLASS, method, message);
      throw new IllegalArgumentException(message);      
    }
    if (sortBy == null) {
      sortBy = DEFAULT_SORTING_ATTR;
    }
    LOGGER.trace(CLASS, method, "Sorting by: " + sortBy);

    if (!"ascending".equalsIgnoreCase(sortOrder) && !"descending".equalsIgnoreCase(sortOrder)) {
      final String message = "Invalid sort order specified";
      LOGGER.error(CLASS, method, message);
      throw new IllegalArgumentException(message);      
    }
    if (sortOrder == null) 
      sortOrder = "ascending";
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

    // arguments to SQL commands - need to be created now because the first entry is the special user name
    List<Object> arguments = new ArrayList<>();
    try {
      this.client.loginAsOIMInternal();
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    String specialOwner = Helper.getUser(client, specialUser, true).getLogin();
    arguments.add(specialOwner);

    // And on to SQL queries
    // Retrieve total first
    StringBuilder sql = new StringBuilder()
      .append(SQL_TOTAL)
      .append(isSpecial ? SQL_FILTER_SPECIAL_ON : SQL_FILTER_SPECIAL_OFF)
    ;

    StringBuilder sqlFilterBuilder = new StringBuilder();
    // Just a flag indicating whether or not we should append filtering clause
    boolean filtering = false;
    // We use the same arguments objects - for total it only contains the filter values,
    // for the actual retrieval start and items are added.
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
          LOGGER.trace(CLASS, method, "Filtering clause arguments: " + arguments);
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
    
    // And now the actual data
    sql = new StringBuilder()
      .append(SQL_QUERY)
      .append(SQL_FROM_WHERE_CLAUSE)
      .append(isSpecial ? SQL_FILTER_SPECIAL_ON : SQL_FILTER_SPECIAL_OFF)
      .append(sqlFilterBuilder)
      .append(SQL_GROUP_BY_CLAUSE)
      .append(SQL_ORDER_BY_CLAUSE)
      .append(sortBy)
      .append(sortOrder.equals("ascending") ? " ASC" : " DESC")
      .append(SQL_PAGING_CLAUSE)
    ;
    LOGGER.trace(CLASS, method, "Final SQL statement: " + sql.toString());
    // Add start and items to the set
    arguments.add(start - 1);
    arguments.add(requestedItems);

    List<String> policyIds = new ArrayList<>();
    try (
      PreparedStatement statement = SqlHelper.createPreparedStatement(connection, sql.toString(), arguments);
      ResultSet resultSet = statement.executeQuery();
    ) {
      while (resultSet.next()) {
        actualItems++;
        policyIds.add(resultSet.getString("key"));
      }
      LOGGER.debug(CLASS, method, "Final policy IDs list: " + policyIds);
    }
    catch (SQLException e) {
      final String message = "Exception while retrieving policies";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
    
    try {
      AccessPolicyService policyService = this.client.getService(AccessPolicyService.class);
      for (String policyId : policyIds) {
        AccessPolicy policy = policyService.getAccessPolicy(policyId, true);
        PolicyEntity outputEntity = buildPolicy(policy);
        result.add(outputEntity);
      }
    } catch (AccessPolicyServiceException e) {
      final String message = "AccessPolicy service exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } finally {
      this.client.logout();
    }
    
    LOGGER.exiting(CLASS, method);
    return new ListResponse<>(total, start, actualItems, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Request to retrieve a certain {@link PolicyEntity} by its <code>name</code>.
   **
   ** @param name                      name of the Access Policy to retrieve.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param isSpecial                 flag indicating whether the Access Policy is special
   **                                  one or not.
   **                                  <br>
   **                                  Allowed object is {@link Boolean}.
   **
   ** @return                          the {@link PolicyEntity} mapped at <code>name</code>.
   **                                  <br>
   **                                  Possible object is {@link PolicyEntity}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public PolicyEntity lookup(final String name, Boolean isSpecial)
    throws IllegalArgumentException {
    final String method = "lookup";
    LOGGER.entering(CLASS, method, "name=", name, "isSpecial=", isSpecial);

    PolicyEntity entity = null;
    try {
      this.client.loginAsOIMInternal();
      AccessPolicy policyObject = getAccessPolicy(name);
      LOGGER.debug(CLASS, method, "Retrieved AccessPolicy instance: " + policyObject);
      if (policyObject != null) {
        String specialOwner = Helper.getUser(client, specialUser, true).getId();
        if ((isSpecial && !(policyObject.getOwnerId().equalsIgnoreCase(specialOwner))) ||
            (!isSpecial && policyObject.getOwnerId().equalsIgnoreCase(specialOwner))) {
          LOGGER.debug(CLASS, method, "AccessPolicy not owned by the appropriate user, returning null");
          return null;
        }
        entity = buildPolicy(policyObject);
        LOGGER.debug(CLASS, method, "Populated PolicyEntity: " + Schema.marshalPolicy(entity));
      }
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } catch (Exception e) {
      final String message = "Exception caught: " + e.getCause();
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } finally {
      this.client.logout();
    }
    LOGGER.exiting(CLASS, method);
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPolicy
  /**
   ** Used to create {@link PolicyEntity} object from {@link AccessPolicy}.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param inputObject               the input {@link AccessPolicy} object.
   **                                  <br>
   **                                  Allowed object is {@link AccessPolicy}.
   **                                  
   ** @return                          the constructed {@link PolicyEntity} object.
   **                                  <br>
   **                                  Possible object is {@link AccountEntity}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private PolicyEntity buildPolicy(final AccessPolicy inputObject)
    throws IllegalArgumentException {
    final String method = "buildPolicy(AccessPolicy)";
    LOGGER.entering(CLASS, method, "inputObject=", inputObject);
    
    PolicyEntity outputEntity = Entity.policy(inputObject.getName());
      outputEntity.ownerType(inputObject.getOwnerType().getID());
      outputEntity.ownerId(Helper.getUser(client, inputObject.getOwnerId(), false).getLogin());
      outputEntity.type(inputObject.getEntityType());
      outputEntity.description(inputObject.getDescription());
      outputEntity.createDate(inputObject.getCreate());
      outputEntity.updateDate(inputObject.getUpdate());
      outputEntity.priority(inputObject.getPriority())
    ;
    List<AccountEntity> accounts = new ArrayList<>();
    // Add list of accounts to the PolicyEntity
    outputEntity.account(accounts);

    LOGGER.debug(CLASS, method, "List of Policy Elements: " + inputObject.getPolicyElements());
    for (AccessPolicyElement element : inputObject.getPolicyElements()) {
      LOGGER.debug(CLASS, method, "Got element: " + element);
      LOGGER.debug(CLASS, method, "Element attributes: " + element.getAttributes());
      try {
        LOGGER.debug(CLASS, method, "Policy Element application instance ID: " + element.getApplicationInstanceID());
        ApplicationInstance appInstance = this.client
                                     .getService(ApplicationInstanceService.class)
                                     .findApplicationInstanceByKey(element.getApplicationInstanceID());
        String appInstanceName = appInstance.getApplicationInstanceName();
        FormInfo formInfo = appInstance.getAccountForm();
        String itResourceColumn = "";
        for (FormField ff : formInfo.getFormFields()) {
          // Don't like this, isn't this available as some static field?!
          Object itResourceProperty = ff.getProperty("ITResource");
          if (itResourceProperty != null)
            itResourceColumn = ff.getName();
        }
        String accountFormName = formInfo.getName();
        // Just prepare mapping between formID -> formName for later use
        Map<Long, String> childFormMap = new HashMap<>();
        Map<String, String> childFormPrimaryFieldMap = new HashMap<>();
        for (FormInfo childForm : appInstance.getChildForms()) {
          childFormMap.put(childForm.getFormKey(), childForm.getName());
          for (FormField childFormField : childForm.getFormFields()) {
            if ("true".equals(childFormField.getProperty("Entitlement"))) {
              childFormPrimaryFieldMap.put(childForm.getName(), childFormField.getName());
              break;
            }
          }
        }
        // If the PolicyElement is a denial only create dummy AccountEntity to revoke (delete) the account
        if (element.isDenial()) {
          AccountEntity accEnt = Entity.account(appInstanceName)
                                    .accountForm(accountFormName)
                                    .type("")
                                    .status("")
                                    .action(AccountEntity.Action.delete);
          accounts.add(accEnt);
          continue;
        };
        Map<String, Object> attrs = new HashMap<>();
        DefaultData d = element.getDefaultData();
        List<ChildAttribute> ca = d.getChildAttributes();
        if (d != null) {
          List<Record> dataList = d.getData();
          // Store the attributes for lates use
          for (Record rec : dataList) {
            if (!itResourceColumn.equals(rec.getAttributeName()))
              // if the attribute name does not equal to IT Resource reference emit it
              attrs.put(rec.getAttributeName(), rec.getAttributeValue());
          }

          // One account per-application instance (i.e. one per-AccessPolicyElement)
          AccountEntity accEnt = Entity.account(appInstanceName)
                                    .accountForm(accountFormName)
                                    .type("")
                                    .status("")
                                    .action(AccountEntity.Action.create);
          // Add the AccountEntity to the list already linked with PolicyEntity
          accounts.add(accEnt);
          // Fill in the account attributes (parent form attributes)
          accEnt.putAll(attrs);
          
          // On to the child data
          List<ChildRecord> childRecords = d.getChildData();
          // Should not happen, but still
          if (childRecords != null ) {
            // Map containin mapping for namespace names to namespaces. Streams would be more nice.
            Map<String, NamespaceEntity> nsMap = new TreeMap<>();
            for (ChildRecord cr : childRecords) {
              List<FormInfo> x = appInstance.getChildForms();
              EntitlementEntity entEnt = Entity.entitlement(EntitlementEntity.Action.assign, Risk.none).status("");
              String childFormName = null;
              Map<String, String> entAttrs = new HashMap<>();
              AdditionalAttributeEntity additionalAttribute = Entity.additionalAttribute();
              entEnt.addAdditionalAttribute(additionalAttribute);
              for (ChildAttribute childAttr : cr.getAttributes()) {
                childFormName = childFormMap.get(childAttr.getFormID());
                LOGGER.debug(CLASS, method, "Child Form name: " + childFormName);
                entEnt.id(String.valueOf(childAttr.getRecordNumber()));
                String attrName = childAttr.getAttributeName();
                String attrValue = childAttr.getAttributeValue();
                LOGGER.debug(CLASS, method, "Retrieved child attribute (k=v): " + attrName + " = " + attrValue);
                if (attrName.equals(childFormPrimaryFieldMap.get(childFormName))) {
                  // Encode? Decode?
//                  String decodedValue = Helper.decodeLookupValue(this.client, attrName, attrValue);
//                  if (decodedValue != null) {
//                    entEnt.put(attrName, decodedValue);
//                  } else {
//                    entEnt.put(attrName, attrValue);
//                  }
                  entEnt.put(attrName, attrValue);
                } else {
                  // Encode? Decode?
//                  String decodedValue = Helper.decodeLookupValue(this.client, attrName, attrValue);
//                  if (decodedValue != null) {
//                    additionalAttribute.put(attrName, decodedValue);
//                  } else {
//                    additionalAttribute.put(attrName, attrValue);
//                  }
                  additionalAttribute.put(attrName, attrValue);
                }
              }
              NamespaceEntity ns = nsMap.remove(childFormName);
              if (ns == null) {
                // First time we came across this namespace, let's create it and store to the list
                ns = Entity.namespace(childFormName);
              }
              ns.add(entEnt);
              nsMap.put(childFormName, ns);
            }
            // Finally, iterate through namespace map and add each namespace to this account
            for (Map.Entry<String, NamespaceEntity> ns: nsMap.entrySet()) {
              accEnt.namespace(ns.getValue());
            }
          }
        }
      } catch (ApplicationInstanceNotFoundException | GenericAppInstanceServiceException e) {
        // Should not actually happen, re-throw
        final String message = e.getMessage();
        LOGGER.error(CLASS, method, message);
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message, e);
      }
    }
    LOGGER.exiting(CLASS, method, "outputEntity=", outputEntity);
    return outputEntity;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Request to delete referenced by <code>policyName</code>.
   **
   ** @param policyName                Name of the access policy to delete.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param isSpecial                 flag indicating whether the Access Policy is special
   **                                  one or not.
   **                                  <br>
   **                                  Allowed object is {@link Boolean}.
   **
   ** @return                          the operation {@link Result}.
   **                                  <br>
   **                                  Possible object is {@link Result}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public Result delete(final String policyName, Boolean isSpecial)
    throws IllegalArgumentException {
    final String method = "delete";
    LOGGER.entering(CLASS, method, "policyName=", policyName, "isSpecial=", isSpecial);
    Result result = Result.other;
    
    try {
      this.client.loginAsOIMInternal();
      AccessPolicy policyObject = getAccessPolicy(policyName);
      if (policyObject == null) {
        result = Result.notFound;
      } else {
        String specialOwner = Helper.getUser(client, specialUser, true).getId();
        if ((isSpecial && !(policyObject.getOwnerId().equalsIgnoreCase(specialOwner))) ||
            (!isSpecial && policyObject.getOwnerId().equalsIgnoreCase(specialOwner))) {
          // Although technically found, the owner and special flag do not correspond,
          // let's pretend it does not exist
          result = Result.notFound;
        } else {
          AccessPolicyService policyService = this.client.getService(AccessPolicyService.class);
          policyService.deleteAccessPolicy(policyObject.getEntityId(), true);
          result = Result.ok;
        }
      }
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } catch (AccessPolicyServiceException e) {
      final String message = e.getMessage();
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } finally {
      this.client.logout();
    }
    LOGGER.exiting(CLASS, method);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Request to create {@link PolicyEntity} by its <code>name</code>.
   **
   ** @param input                     {@link PolicyEntity} to create.
   **                                  <br>
   **                                  Allowed object is {@link PolicyEntity}.
   **                                  
   ** @param isSpecial                 flag indicating whether the Access Policy is special
   **                                  one or not.
   **                                  <br>
   **                                  Allowed object is {@link Boolean}.
   **
   ** @return                          the {@link PolicyEntity} mapped at <code>name</code>.
   **                                  <br>
   **                                  Possible object is {@link PolicyEntity}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public PolicyEntity create(final PolicyEntity input, Boolean isSpecial)
    throws IllegalArgumentException {
    final String method = "create";
    LOGGER.entering(CLASS, method, "input=", input);

    PolicyEntity output = null;
    try {
      this.client.loginAsOIMInternal();
      String specialOwner = Helper.getUser(client, specialUser, true).getLogin();
      if ((isSpecial && !(input.ownerId().equalsIgnoreCase(specialOwner))) ||
           (!isSpecial && input.ownerId().equalsIgnoreCase(specialOwner))) {
        String message = "AccessPolicy not owned by the appropriate user, returning null";
        LOGGER.error(CLASS, method, message);
//        return null;
        throw new IllegalArgumentException(message, BadRequestException.invalidValue(message));
      }
      AccessPolicy policyObject = buildPolicy(input, null);
      AccessPolicyService policyService = this.client.getService(AccessPolicyService.class);
      String policyId = policyService.createAccessPolicy(policyObject);
      // Fetch and build the newly created object
      LOGGER.debug(CLASS, method, "Successfuly created policy, ID: " + policyId);
      policyObject = getAccessPolicy(input.id());
      output = buildPolicy(policyObject);
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } catch (AccessPolicyServiceException e) {
      final String message = e.getMessage();
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } finally {
      this.client.logout();
    }
    LOGGER.exiting(CLASS, method, output);
    return output;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Request to modify an existing Access Policy object to correspond to the
   ** supplied {@link PolicyEntity}.
   **
   ** @param policyName                {@link String} name of the policy to modify. This
   **                                  is supplied separately in order to support
   **                                  renaming Access Policies.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param input                     {@link PolicyEntity} to modify. This is
   **                                  full object, since the model required most
   **                                  of the attributes.
   **                                  <br>
   **                                  Allowed object is {@link PolicyEntity}.
   **                                  
   ** @param isSpecial                 flag indicating whether the Access Policy is special
   **                                  one or not.
   **                                  <br>
   **                                  Allowed object is {@link Boolean}.
   **
   ** @return                          the {@link PolicyEntity} mapped at <code>name</code>.
   **                                  <br>
   **                                  Possible object is {@link PolicyEntity}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public PolicyEntity modify(final String policyName, final PolicyEntity input, Boolean isSpecial)
    throws IllegalArgumentException {
    final String method = "modify";
    LOGGER.entering(CLASS, method, "policyName=", policyName,
                                   "input=", input,
                                   "isSpecial=", isSpecial);

    PolicyEntity output = null;
    try {
      this.client.loginAsOIMInternal();

      AccessPolicyService policyService = this.client.getService(AccessPolicyService.class);
      AccessPolicy origPolicyObject = getAccessPolicy(policyName);
      if (origPolicyObject == null) {
        // Not found, most probably, return null
        return null;
      }
      String specialOwner = Helper.getUser(client, specialUser, true).getLogin();
      if ((isSpecial && !(input.ownerId().equalsIgnoreCase(specialOwner))) ||
           (!isSpecial && input.ownerId().equalsIgnoreCase(specialOwner))) {
        LOGGER.debug(CLASS, method, "AccessPolicy not owned by the appropriate user, returning null");
        return null;
      }
      LOGGER.debug(CLASS, method, "Retrieved existing policy ID: " + origPolicyObject.getEntityId());
      AccessPolicy policyObject = buildPolicyChanges(policyName, input);
      // TODO: Not sure what needs to be supplied for AccessPolicyService.updateAccessPolicy()
      // Still investigating, sorry :-(
//      AccessPolicy policy = new AccessPolicy(origPolicy.getEntityId(), false);
      policyService.updateAccessPolicy(policyObject);
      LOGGER.debug(CLASS, method, "Successfuly updated policy: " + input.id());
      // Return the updated object
      policyObject = getAccessPolicy(input.id());
      output = buildPolicy(policyObject);
    } catch (LoginException e) {
      final String message = "Login exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } catch (AccessPolicyServiceException e) {
      final String message = e.getMessage();
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } catch (Exception e) {
      // Catch-all, other exceptions are thrown from the updateAccessPolicy() method
      final String message = e.getMessage();
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    } finally {
      this.client.logout();
    }
    LOGGER.exiting(CLASS, method, output);
    return output;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPolicy
  /**
   ** Used to create {@link PolicyEntity} object from {@link AccessPolicy}.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param inputEntity               the input {@link PolicyEntity} object.
   **                                  <br>
   **                                  Allowed object is {@link PolicyEntity}.
   **                                  
   ** @param id                        if present, use this {@link String}
   **                                  object ID to create the skeleton object.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the constructed {@link AccessPolicy} object.
   **                                  <br>
   **                                  Possible object is {@link AccessPolicy}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private AccessPolicy buildPolicy(final PolicyEntity inputEntity, String id)
    throws IllegalArgumentException {
    final String method = "buildPolicy(PolicyEntity)";
    LOGGER.entering(CLASS, method, "inputEntity=", inputEntity
                                 , "id=", id
                    );
    
    AccessPolicy outputObject = new AccessPolicy(id, false);
    outputObject.setName(inputEntity.id());
    outputObject.setDescription(inputEntity.description());
    outputObject.setPriority(Long.valueOf(inputEntity.priority()));
    outputObject.setOwnerType(AccessPolicy.OwnerType.valueOf(inputEntity.ownerType()));
    outputObject.setOwnerId(Helper.getUser(client, inputEntity.ownerId(), true).getId());

    List<AccessPolicyElement> policyElements = new ArrayList<>();
    for (AccountEntity accEnt : inputEntity.account()) {
      String appInstanceName = accEnt.id();
      LOGGER.debug(CLASS, method, "Obtained appInstanceName: " + appInstanceName);
      long appInstanceId = 0L;
      long accountFormId = 0L;
      try {
        ApplicationInstanceService appInstService = this.client.getService(ApplicationInstanceService.class);
        ApplicationInstance appInstance = appInstService.findApplicationInstanceByName(appInstanceName);
        appInstanceId = appInstance.getApplicationInstanceKey();
        accountFormId = appInstance.getAccountForm().getFormKey();
        
        // Just prepare mapping between formName -> formID for later use
        Map<String, Long> childFormMap = new HashMap<>();
        for (FormInfo childForm : appInstance.getChildForms()) {
          childFormMap.put(childForm.getName(), childForm.getFormKey());
        }
      } catch (ApplicationInstanceNotFoundException | GenericAppInstanceServiceException e) {
        // Should not actually happen, re-throw
        final String message = e.getMessage();
        LOGGER.error(CLASS, method, message);
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message, e);
      }
      LOGGER.debug(CLASS, method, "Obtained appInstanceId: " + appInstanceId);
      // POJO preparation
      AccessPolicyElement policyElement = new AccessPolicyElement(appInstanceId,
                                                         accEnt.is(AccountEntity.Action.delete),
                                                         AccessPolicyElement.ACTION_IF_NOT_APPLICABLE.REVOKE
                                                        );
      policyElements.add(policyElement);
      if (!accEnt.is(AccountEntity.Action.delete)) {
        // Only assign elements if the account is not denied (i.e. Action.delete)
        outputObject.setPolicyElements(policyElements);
        DefaultData data = new DefaultData();
        policyElement.setDefaultData(data);
        
        // Attributes first
        if (accEnt.attribute() != null) {
          Map<String, String> attributes = new HashMap<>();
          for (Map.Entry<String, Object> attr : accEnt.attribute().entrySet()) {
            attributes.put(attr.getKey(), (String) attr.getValue());
          }
          data.addData(accountFormId, attributes);
        }
        if (accEnt.namespace() != null) {
          for (String namespaceKey : accEnt.namespace().keySet()) {
            for (EntitlementEntity ent : accEnt.toAssign(namespaceKey)){
              Map<String, String> childData = new HashMap<>();
              for (Map.Entry<String, Object> attr : ent.attribute().entrySet()) {
                // Encode? Decode?
//                // Primary attributes is always a lookup, we can safely encode it
//                String encodedValue = Helper.encodeLookupValue(this.client, attr.getKey(), (String) attr.getValue());
//                if (encodedValue != null) {
//                  childData.put(attr.getKey(), encodedValue);
//                } else {
//                  childData.put(attr.getKey(), (String) attr.getValue());
//                }
                childData.put(attr.getKey(), (String) attr.getValue());
              }
              for (AdditionalAttributeEntity additionalAttr : ent.additionalAttributes()) {
                for (Map.Entry<String, Object> attr : additionalAttr.attribute().entrySet()) {
                  // Encode? Decode?
//                  // Additional attributes can also be a free test value, not a lookup.
//                  // If null is returned (a catchall value for all the Thor.API.Exceptions.tc*Exceptions we add it directly
//                  String encodedValue = Helper.encodeLookupValue(this.client, attr.getKey(), (String) attr.getValue());
//                  if (encodedValue != null) {
//                    childData.put(attr.getKey(), encodedValue);
//                  } else {
//                    childData.put(attr.getKey(), (String) attr.getValue());
//                  }
                  childData.put(attr.getKey(), (String) attr.getValue());

                }
              }
              data.addChildData(namespaceKey, childData);
            }
          }
        }
      }
    }
    LOGGER.exiting(CLASS, method, "outputObject=", outputObject);
    return outputObject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPolicyChanges
  /**
   ** Used to create {@link AccessPolicy} object from input {@link PolicyEntity}
   ** containin only changes identified for the
   ** AccessPolicyService.updateAccessPolicy() method call.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param originalPolicyName        {@link String} name of the policy to modify. This
   **                                  is supplied separately in order to support
   **                                  renaming Access Policies.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @param newPolicyEntity           the input {@link PolicyEntity} object.
   **                                  <br>
   **                                  Allowed object is {@link PolicyEntity}.
   **                                  
   ** @return                          the constructed {@link AccessPolicy} object
   **                                  containing the differences identified.
   **                                  <br>
   **                                  Possible object is {@link AccessPolicy}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private AccessPolicy buildPolicyChanges(final String originalPolicyName, final PolicyEntity newPolicyEntity)
    throws IllegalArgumentException {
    final String method = "buildPolicyChanges";
    LOGGER.entering(CLASS, method, "originalPolicyName=", originalPolicyName);
    LOGGER.entering(CLASS, method, "newPolicyEntity=", newPolicyEntity);
    
    AccessPolicy originalPolicyObject = getAccessPolicy(originalPolicyName);
    String policyId = originalPolicyObject.getEntityId();
    PolicyEntity originalPolicyEntity = buildPolicy(originalPolicyObject);
    
    List<AccessPolicyElement> finalElementList = new ArrayList<>();

    // This is the resulting AccessPolicy object
    AccessPolicy newPolicyObject = buildPolicy(newPolicyEntity, policyId);
    // And here we go - identity attribute changes first
    if (!newPolicyEntity.id().equals(originalPolicyEntity.id())) {
      newPolicyObject.setName(newPolicyEntity.id());
      originalPolicyObject.setName(newPolicyEntity.id());
    }
    if (!newPolicyEntity.description().equals(originalPolicyEntity.description())) {
      newPolicyObject.setDescription(newPolicyEntity.description());
      originalPolicyObject.setDescription(newPolicyEntity.description());
    }
    newPolicyObject.setPriority(Long.valueOf(newPolicyEntity.priority()));
    originalPolicyObject.setPriority(Long.valueOf(newPolicyEntity.priority()));
    if (!newPolicyEntity.ownerType().equals(originalPolicyEntity.ownerType())) {
      newPolicyObject.setOwnerType(AccessPolicy.OwnerType.valueOf(newPolicyEntity.ownerType()));
      originalPolicyObject.setOwnerType(AccessPolicy.OwnerType.valueOf(newPolicyEntity.ownerType()));
    }
    if (!newPolicyEntity.ownerId().equals(originalPolicyEntity.ownerId())) {
      newPolicyObject.setOwnerId(Helper.getUser(client, newPolicyEntity.ownerId(), true).getId());
      originalPolicyObject.setOwnerId(Helper.getUser(client, newPolicyEntity.ownerId(), true).getId());
    }
    
    // Prepare maps to easily retrieve the whole elements and associated data
    Map<Long, AccessPolicyElement> origElementMap = new HashMap<>();
    Map<Long, AccessPolicyElement> newElementMap = new HashMap<>();
    for (AccessPolicyElement newElement : newPolicyObject.getPolicyElements()) {
      newElementMap.put(newElement.getApplicationInstanceID(), newElement);
    }
    for (AccessPolicyElement origElement : originalPolicyObject.getPolicyElements()) {
      origElementMap.put(origElement.getApplicationInstanceID(), origElement);
    }
    
    // Identity the overlapping application IDs (present in both new and original
    // object - these are potential candidates for attribute/child data change
    Set<Long> commonKeys = new HashSet<>(newElementMap.keySet());
    commonKeys.retainAll(origElementMap.keySet());
    
    // Let's do addition/removal of the whole applications first, that's easier to start with
    for (Long addedId : newElementMap.keySet()) {
      if (!origElementMap.keySet().contains(addedId)) {
        // Add the newly-built element to the final list
        finalElementList.add(newElementMap.get(addedId));
      }
    }
    for (Long removedId : origElementMap.keySet()) {
      if (!newElementMap.keySet().contains(removedId)) {
        AccessPolicyElement removedElement = origElementMap.get(removedId);
        removedElement.markForDelete();
        finalElementList.add(removedElement);
      }
    }

    // And now iterate  the common application IDs to build elements and identify changes
    for (Long retainedAppId : commonKeys) {
      // We need to get everything from the original policy elements to keep the IDs and rowVers
      AccessPolicyElement origElement = origElementMap.get(retainedAppId);
      List<Record> origRecords = origElement.getDefaultData().getData();
      List<ChildRecord> origChildRecords = origElement.getDefaultData().getChildData();

      // And similiarly retrieve everything from the new policy elements to keep the IDs and rowVers
      AccessPolicyElement newElement = newElementMap.get(retainedAppId);
      List<Record> newRecords = newElement.getDefaultData().getData();
      List<ChildRecord> newChildRecords = newElement.getDefaultData().getChildData();
      
      // Construct new AccessPolicyElement and DefaultData to be used
      AccessPolicyElement finalElement = new AccessPolicyElement(origElement.getEntityId(),
                                                                 origElement.isChildEntity());
      finalElement.setApplicationInstanceID(retainedAppId);
      
      finalElement.setDenial(newElement.isDenial());
      finalElement.setActionIfNotApplicable(origElement.getActionIfNotApplicable());
      finalElementList.add(finalElement);
      DefaultData finalDefaultData = finalElement.getDefaultData();
      
      // And now on to the fun
      List<Record> finalData = mergeRecords(origRecords, newRecords);
      finalDefaultData.setData(finalData);
      // the mergeChildRecords() now directly modifies DefaultData object
      mergeChildRecords(retainedAppId, origChildRecords, newChildRecords, finalDefaultData);
    }

    // After processing of added/deleted elements the newElementList contains all policy elements that should be
    // set for the new object
    newPolicyObject.setPolicyElements(finalElementList);

    LOGGER.exiting(CLASS, method, "newPolicyObject=", newPolicyObject);
    return newPolicyObject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccessPolicy
  /**
   ** Used to retrieve {@link AccessPolicy} object identified by policyName.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param policyName                {@link String} name of the policy to
   **                                  retrieve.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **                                  
   ** @return                          the retrieved {@link AccessPolicy} object.
   **                                  <br>
   **                                  Possible object is {@link AccessPolicy}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/  private AccessPolicy getAccessPolicy(final String policyName)
    throws IllegalArgumentException {
    final String method = "getAccessPolicy";
    LOGGER.entering(CLASS, method, "policyName=", policyName);

    try {
      SearchCriteria crit = new SearchCriteria(AccessPolicy.ATTRIBUTE.NAME.getID(), policyName, SearchCriteria.Operator.EQUAL);
      AccessPolicyService policyService = this.client.getService(AccessPolicyService.class);
      List<AccessPolicy> policies = policyService.findAccessPolicies(crit, null);
      if (policies.size() > 1) {
        LOGGER.debug(CLASS, method, "Should not happen, found more than one policy with name: " + policyName);
        return null;
      } else if (policies.size() < 1) {
        LOGGER.debug(CLASS, method, "Access policy not found, looked for name: " + policyName);
        return null;
      }
      AccessPolicy policy = policyService.getAccessPolicy(policies.get(0).getEntityId(), true);
      LOGGER.exiting(CLASS, method, "policy=", policy);
      return policy;
    } catch (AccessPolicyServiceException e) {
      final String message = "AccessPolicy service exception caught";
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeRecords
  /**
   ** Used to merge {@link AccessPolicy} records data (not child data, that's
   ** far more complicated), i.e. identify differences between original records
   ** (retrieved from the existing policy object) and new records (constructed
   ** from the {@link PolicyEntity} supplied to the calling method.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param oldRecords                {@link List} of {@link Record}s from the
   **                                  original (existing) policy.
   **                                  <br>
   **                                  Allowed object is {@link List} of
   **                                  {@link Record}s.
   **                                  
   ** @param newRecords                {@link List} of {@link Record}s from the
   **                                  modified policy supplied as an argument.
   **                                  <br>
   **                                  Allowed object is {@link List} of
   **                                  {@link Record}s.
   **                                  
   ** @return                          the constructed {@link List} of
   **                                  {@link Record}s containing the
   **                                  differences identified.
   **                                  <br>
   **                                  Possible object is {@link List} of 
   **                                  {@link Record}s.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private List<Record> mergeRecords(List<Record> oldRecords, List<Record> newRecords) {
    final String method = "mergeRecords";
    LOGGER.entering(CLASS, method, "oldRecords=", oldRecords,
                                   "newRecords=", newRecords);
    List<Record> retRecords = new ArrayList<>();
    // Will add records from the orig list if it's present in new list (these will include changes, if any)
    // Adding it to the final list will also remove it from the original list to identify removals
    
    // Iterate new records, look up name in the old records, set value from new record and add it to the list
    // Also, remove from old records
    for (Record newRec : newRecords) {
      String newName = newRec.getAttributeName();
      String newVal = newRec.getAttributeValue();
      Boolean found = false;
      Long formId = 0L;
      for (Record oldRec : oldRecords) {
        formId = oldRec.getFormID();
        if (newName.equals(oldRec.getAttributeName())) {
          oldRec.setAttributeValue(newVal);
          retRecords.add(oldRec);
          oldRecords.remove(oldRec);
          found = true;
          break;
        }
      }
      if (!found) {
        retRecords.add(new Record(formId, newName, newVal));
      }
    }
    // Part of it is already modified - these are those one not present in new records
    for (Record remainingOldRec : oldRecords) {
      remainingOldRec.markForDelete();
      retRecords.add(remainingOldRec);
    }

    LOGGER.debug(CLASS, method, "Dumping records after merge:");
    for (Record rec : retRecords) {
      LOGGER.debug(CLASS, method, rec.toString());
    }
    LOGGER.exiting(CLASS, method, "retRecords=", retRecords);
    return retRecords;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeChildRecords
  /**
   ** Used to merge {@link AccessPolicy} records data (not child data, that's
   ** far more complicated), i.e. identify differences between original records
   ** (retrieved from the existing policy object) and new records (constructed
   ** from the {@link PolicyEntity} supplied to the calling method.
   ** Used internally, assuming the OIMClient is logged in.
   **
   ** @param oldChildRecords           {@link List} of {@link ChildRecord}s from the
   **                                  original (existing) policy.
   **                                  <br>
   **                                  Allowed object is {@link List} of
   **                                  {@link ChildRecord}s.
   **                                  
   ** @param newChildRecords           {@link List} of {@link ChildRecord}s from the
   **                                  modified policy supplied as an argument.
   **                                  <br>
   **                                  Allowed object is {@link List} of
   **                                  {@link ChildRecord}s.
   **                                  
   ** @param data                      {@link DefaultData} to modify as part of
   **                                  processing the differences. Already linked
   **                                  to the new policy object, therefore no
   **                                  return argument is needed.
   **                                  <br>
   **                                  Allowed object is {@link DefaultData}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  private void mergeChildRecords(long appInstanceId, List<ChildRecord> oldChildRecords, List<ChildRecord> newChildRecords, DefaultData data) {
    final String method = "mergeChildRecords";
    LOGGER.entering(CLASS, method, "appInstanceId=", appInstanceId,
                                   "oldChildRecords=", oldChildRecords,
                                   "newChildRecords=", newChildRecords);
    Map<Long, String> childFormMapById = new HashMap<>();
    Map<String, Long> childFormMapByName = new HashMap<>();
    try {
      ApplicationInstance appInstance = this.client
                                   .getService(ApplicationInstanceService.class)
                                   .findApplicationInstanceByKey(appInstanceId);
      String appInstanceName = appInstance.getApplicationInstanceName();
      // Just prepare mapping between formID -> formName for later use
      for (FormInfo childForm : appInstance.getChildForms()) {
        childFormMapById.put(childForm.getFormKey(), childForm.getName());
        childFormMapByName.put(childForm.getName(), childForm.getFormKey());
      }
    } catch (ApplicationInstanceNotFoundException | GenericAppInstanceServiceException e) {
      // Should not actually happen, re-throw
      final String message = e.getMessage();
      LOGGER.error(CLASS, method, message);
      LOGGER.throwing(CLASS, method, e);
      throw new IllegalArgumentException(message, e);
    }
  
    // Will add records from the orig list if it's present in new list (these will include changes, if any)
    // If there are more records supplied then are in the original policy new ones will be created (i.e. no rowVer)
    // Adding it to the final list will also remove it from the original list to identify removals
    
    // The following is a list of maps containing namespaces and attribute name/value pair
    // corresponding to each namespace.
    // Note: The namespace can be present multiple times if more rows are present in AP
    List<Map<String, Map<String, String>>> oldAttributes = new ArrayList<>();
    List<Map<String, Map<String, String>>> newAttributes = new ArrayList<>();

    for (ChildRecord rec : newChildRecords) {
      Map<String, Map<String, String>> recordAttributes = new HashMap<>();
      newAttributes.add(recordAttributes);
      List<ChildAttribute> attrs = rec.getAttributes();
      Map<String, String> attrMap = new HashMap<>();
      String formName = "";
      for (ChildAttribute attr : attrs) {
        formName = attr.getFormName();
        long formId = childFormMapByName.get(formName);
        String name = attr.getAttributeName();
        String value = attr.getAttributeValue();
        attrMap.put(name, value);
      }
      recordAttributes.put(formName, attrMap);
    }
    LOGGER.debug(CLASS, method, "Dumping new: newAttributes = " + newAttributes);
    
    // Another list for easier retrieval of whole records to be updated during comparison
    List<Map<String, ChildRecord>> oldRecordList = new ArrayList<>();
    // Map to retrieve record numbers to use
    Map<String, Long> recordNumbers = new HashMap<>();
    
    for (ChildRecord rec : oldChildRecords) {
      Map<String, Map<String, String>> recordAttributes = new HashMap<>();
      oldAttributes.add(recordAttributes);
      List<ChildAttribute> attrs = rec.getAttributes();
      Map<String, String> attrMap = new HashMap<>();
      String formName = "";
      List<String> keyList = new ArrayList<>();
      for (ChildAttribute attr : attrs) {
        long formId = attr.getFormID();
        formName = childFormMapById.get(formId);
        String name = attr.getAttributeName();
        String value = attr.getAttributeValue();
        attrMap.put(name, value);
        keyList.add(name);

        // Update the recordNumbers map, need to do it here because record numbers
        // are only stored on attributes, not the records themseves
        Long recordNumber = recordNumbers.get(formName);
        if (recordNumber == null || attr.getRecordNumber() > recordNumber) {
          recordNumbers.put(formName, attr.getRecordNumber());
        }
      }
      Collections.sort(keyList);
      // Prepend the key by form name - insert to beginning into list already sorted
      keyList.add(0, formName);
      String key = String.join("-", keyList);
      recordAttributes.put(formName, attrMap);
      Map<String, ChildRecord> recMap = new HashMap<>();
      recMap.put(key, rec);
      oldRecordList.add(recMap);
    }
    LOGGER.debug(CLASS, method, "Dumping old: oldAttributes = " + oldAttributes);
    LOGGER.debug(CLASS, method, "Dumping old: oldRecordList = " + oldRecordList);

 //   // This maximizes usage of existing records but, for some reason, throws
//    // "java.sql.SQLException: Result set after last row" in some cases. Weird
//    // enough, the same exception is also thrown from the GUI upon similiar
//    // operations. Maybe a PS3 bug?
//
//    // Records to be added later - after marking for deletion
//    List<Map<String, Map<String, String>>> toBeAdded = new ArrayList<>();
//
//    // For each new child record (already stored in the list of maps), find the old
//    // record with the same attributes, update attribute values, add record to DefaultData
//    // and remove the old one from the list.
//    for (Map<String, Map<String, String>> newRec : newAttributes) {
//      // One particular record entry:
//      // {UD_AJS_PRJ={UD_AJS_PRJ_PID=27~Test Project 1, UD_AJS_PRJ_RID=10102}}
//      LOGGER.debug(CLASS, method, "Processing new attribute map: " + newRec);
//
//      // Construct key to look up
//      String key = "";
//      // To be used later
//      Map<String, String> newRecAttrs = new HashMap<>();
//      
//      // Record
//      // Retrieve attribute names to construct the lookup key
//      // Alas, although the newRec map only has a single entry we need to use a map
//      // to keep the formName
//      for (Map.Entry<String, Map<String, String>> newRecEntry : newRec.entrySet()) {
//        String formName = newRecEntry.getKey();
//        newRecAttrs = newRecEntry.getValue();
//        List<String> attrNamesList = new ArrayList<>(newRecAttrs.keySet());
//        Collections.sort(attrNamesList);
//        // Prepend the key by form name - insert to beginning into list already sorted
//        attrNamesList.add(0, formName);
//        key = String.join("-", attrNamesList);
//        LOGGER.debug(CLASS, method, "Constructed key for " + newRec + ": " + key);
//      }
//      if ("".equals(key)) {
//        // Should not happen
//        LOGGER.debug(CLASS, method, "Did not construct key!");
//      } else {
//        Boolean found = false;
//        for (Map<String, ChildRecord> oldRecordMap : oldRecordList) {
//          LOGGER.debug(CLASS, method, "Analyzing old record map: " +  oldRecordMap);
//          ChildRecord oldRecord = oldRecordMap.get(key);
//          LOGGER.debug(CLASS, method, "Old record retrieved: " + oldRecord);
//          if (oldRecord != null) {
//            LOGGER.debug(CLASS, method, "Old record found");
//            List<ChildAttribute> oldAttrs = oldRecord.getAttributes();
//            for (ChildAttribute oldAttr : oldAttrs) {
//              LOGGER.debug(CLASS, method, "Old attribute name: " + oldAttr.getAttributeName());
//              LOGGER.debug(CLASS, method, "Old attribute value: " + oldAttr.getAttributeValue());
//              LOGGER.debug(CLASS, method, "New attribute value: " + newRecAttrs.get(oldAttr.getAttributeName()));
//              oldAttr.setAttributeValue(newRecAttrs.get(oldAttr.getAttributeName()));
//            }
//            data.addChildData(oldRecord);
//            LOGGER.debug(CLASS, method, "Old record removal success: " +  oldRecordList.remove(oldRecordMap));
//            LOGGER.debug(CLASS, method, "Old record list after removal: oldRecordList = " + oldRecordList);
//            found = true;
//            break;
//          } else {
//            LOGGER.debug(CLASS, method, "Old record not found!");
//          }
//        }
//        if (!found) {
//          for (Map.Entry<String, Map<String, String>> newRecEntry : newRec.entrySet()) {
//            LOGGER.debug(CLASS, method, "Storing new child data for later addition: " + newRecEntry.getKey() + " = " + newRecEntry.getValue());
//            Map<String, Map<String, String>> entry = new HashMap<>();
//            entry.put(newRecEntry.getKey(), newRecEntry.getValue());
//            toBeAdded.add(entry);
//          }
//        }
//      }
//    }
//    
//    // For all the remaining old records, just delete them
//    // Part of it is already modified - these are those one not present in new records
//    for (Map<String, ChildRecord> remainingOldRecMap : oldRecordList) {
//      for (ChildRecord remainingOldRec : remainingOldRecMap.values()) {
//        String formName = "";
//        for (ChildAttribute remainingOldAttr : remainingOldRec.getAttributes()) {
//          formName = remainingOldAttr.getFormName();
//          LOGGER.debug(CLASS, method, "Marking for delete: " + remainingOldAttr);
//          remainingOldAttr.markForDelete();
//        }
//        data.addChildData(remainingOldRec);
//      }
//    }
//    
//    // And finally, add all the new records
//    for (Map<String, Map<String, String>> toBeAddedRecord : toBeAdded) {
//      for (Map.Entry<String, Map<String, String>> toBeAddeRecorddEntry : toBeAddedRecord.entrySet()) {
//        LOGGER.debug(CLASS, method, "Adding new child data: " + toBeAddeRecorddEntry.getKey() + " = " + toBeAddeRecorddEntry.getValue());
////        Long recordNumber = recordNumbers.get(toBeAddeRecorddEntry.getKey());
////        if (recordNumber == null) {
////          recordNumber = 0L;
////        }
////        LOGGER.debug(CLASS, method, "Inserting new record: " + ++recordNumber);
////        ChildRecord x = new ChildRecord(toBeAddeRecorddEntry.getKey(), recordNumber, toBeAddeRecorddEntry.getValue());
////        LOGGER.debug(CLASS, method, "Is record new? " + x.isNewRecord());
////        data.addChildData(x);
////        // This replaces the previous value, if any
////        recordNumbers.put(toBeAddeRecorddEntry.getKey(), recordNumber);
////        LOGGER.debug(CLASS, method, "Record numbers map: " + recordNumbers);
//        data.addChildData(toBeAddeRecorddEntry.getKey(), toBeAddeRecorddEntry.getValue());
//      }
//    }
//    
//    // Renumber the records
//    LOGGER.debug(CLASS, method, "Renumbering records");
//    Map<String, Long> recNums = new HashMap<>();
//    for (ChildRecord rec : data.getChildData()) {
//      LOGGER.debug(CLASS, method, "Record: " + rec.toString());
//      if (rec.isNewRecord()) continue;
//      String formName = "";
//      Long recordNumber = -1L;
//      for (ChildAttribute attr : rec.getAttributes()) {
//        if (attr.isDelete()) continue;
//        formName = attr.getFormName();
//        if (formName == null) {
//          formName = childFormMapById.get(attr.getFormID());
//        }
//        recordNumber = recNums.get(formName);
//        if (recordNumber == null) {
//          recordNumber = 1L;
//        }
//        attr.setRecordNumber(recordNumber);
//        LOGGER.debug(CLASS, method, "Attribute: " + attr.toString());
//        LOGGER.debug(CLASS, method, "Attribute deletion flag: " + attr.isDelete());
//      }
//      recNums.put(formName, ++recordNumber);
//      LOGGER.debug(CLASS, method, "RecordNumbers: " + recNums);
//    }

    /* Fuck it, remove all and build it anew... */
    for (ChildRecord oldRec : oldChildRecords) {
      // Clear all existing attributes
      for (ChildAttribute oldAttr : oldRec.getAttributes()) {
        oldAttr.markForDelete();
      }
      data.addChildData(oldRec);
    }
    for (Map<String, Map<String, String>> attrMap : newAttributes) {
      for (Map.Entry<String, Map<String, String>> attr: attrMap.entrySet()) {
        data.addChildData(attr.getKey(), attr.getValue());
      }
    }
    
    LOGGER.debug(CLASS, method, "Dumping child records after merge:");
    for (ChildRecord rec : data.getChildData()) {
      LOGGER.debug(CLASS, method, "Record: " + rec.toString());
      for (ChildAttribute attr : rec.getAttributes()) {
        LOGGER.debug(CLASS, method, "Attribute: " + attr.toString());
        LOGGER.debug(CLASS, method, "Attribute deletion flag: " + attr.isDelete());
      }
    }
    LOGGER.exiting(CLASS, method);
  }
}