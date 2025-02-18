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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Process Form migration

    File        :   TustedDeleteReconciliation.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    TustedDeleteReconciliation.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/
package bka.iam.identity.lds;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.SQLException;

import Thor.API.tcResultSet;
import Thor.API.tcMetaDataSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcIDNotFoundException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcDataNotProvidedException;
import Thor.API.Exceptions.tcMultipleMatchesFoundException;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.dataobj.util.XLDatabase;

import oracle.iam.connectors.icfcommon.Lookup;

import oracle.iam.connectors.icfcommon.extension.Validation;
import oracle.iam.connectors.icfcommon.exceptions.OIMException;
import oracle.iam.connectors.icfcommon.extension.Transformation;
import oracle.iam.connectors.icfcommon.extension.ResourceExclusion;
import oracle.iam.connectors.icfcommon.extension.ValidationException;

import oracle.iam.connectors.icfcommon.recon.ReconEvent;
import oracle.iam.connectors.icfcommon.recon.AbstractReconTask;

import oracle.iam.connectors.icfcommon.service.ReconciliationService;

import org.identityconnectors.common.logging.Log;

import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import org.identityconnectors.framework.api.ConnectorFacade;

import oracle.iam.platform.Platform;

import oracle.iam.reconciliation.api.ReconOperationsService;

import oracle.iam.reconciliation.config.api.ProfileManager;
import oracle.iam.reconciliation.config.vo.Type;
import oracle.iam.reconciliation.config.vo.Profile;
import oracle.iam.reconciliation.config.vo.TargetAttribute;

import oracle.iam.reconciliation.exception.ReconciliationException;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.iam.reconciliation.resources.LRB;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

////////////////////////////////////////////////////////////////////////////////
// class TustedDeleteReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TustedDeleteReconciliation</code> invokes reconciliation in order
 ** to delete identities no longer provided by the trusted source.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TustedDeleteReconciliation extends AbstractReconTask {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute to advice that the data should only be gathered from the
   ** reconciliation source.
   ** <br>
   ** This attribute is optional and defaults to <code>no</code>.
   */
  public static final String GATHERONLY                 = "Gather Only";

  private static final Log   LOG                        = Log.getLog(TustedDeleteReconciliation.class);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Service
  // ~~~~~ ~~~~~~~
  /**
   ** Deletion detection service, which can be used to delete the objects in
   ** Identity Governance, it needs to be used in the following way:
   ** <ul>
   **   <li>Add all object on target resource by using
   **      {@link #addObjectOnResource(ReconEvent)}method.
   **   <li>Call {@link #deleteUnmatched()} method to delete accounts which are
   **       present in Identity Governance, but were not added by
   **       #addObjectOnResource method.
   ** </ul>
   */
  public static class Service implements ReconciliationService.DeletionDetectionService {
  
    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final boolean                   dryRun;
    private final String                    resource;
    private final Map<String, Serializable> scope;
    private final ReconOperationsService    facade;

    private final Set<Serializable>         matched = new HashSet<>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Service</code> executer that allows use as a
     ** JavaBean.
     **
     ** @param  endpoint         the technical enpoint the data are feeded from.
     ** @param  resource         the target object of the operation.
     ** @param  scope            the scope used to detect deleted identities.
     ** @param  facade           the service facade to detect deletions.
     */
    private Service(final boolean dryRun, final String resource, final Lookup scope, final ReconOperationsService facade) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.dryRun   = dryRun;
      this.resource = resource;
      this.facade   = facade;
      this.scope    = new HashMap<String, Serializable>(scope.toMap());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addObjectOnResource (ReconciliationService.DeletionDetectionService)
    /**
     ** Use this method to add all existing object on the target resource
     **
     ** @param  event            the {@link ReconEvent} representing the object
     **                          on the resource in scope.
     */
    @Override
    public void addObjectOnResource(final ReconEvent event) {
      final Map[] data  = new Map[] { event.getSingleFields() };
      try {
        final Set<Serializable> match = this.facade.provideDeletionDetectionData(this.resource, data, this.scope);
        this.matched.addAll(match);
      }
      catch (tcIDNotFoundException e) {
        throw new OIMException(e);
      }
      catch (tcMultipleMatchesFoundException e) {
        throw new OIMException(e);
      }
      catch (tcAPIException e) {
        throw new OIMException(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: deleteUnmatched (ReconciliationService.DeletionDetectionService)
    /**
     ** Deletes all accounts in Identity Governance which were not added by
     ** #addObjectOnResource method.
     */
    @Override
    public void deleteUnmatched() {
      TustedDeleteReconciliation.LOG.ok("Matched objects [{0}]", new Object[] { this.matched });
      try {
        tcResultSet missing = obsolete(this.resource, this.matched, this.scope);
        if (!missing.isEmpty()) {
          if (this.dryRun) {
            final TableFormatter table  = new TableFormatter().header("Users.Key").header("Users.User ID");
            for (int i = 0; i < missing.getRowCount(); i++) {
              missing.goToRow(i);
              table.row().column(missing.getStringValue("Users.Key")).column(missing.getStringValue("Users.User ID"));
            }
            final StringBuilder buffer = new StringBuilder("Candidates for deletion:\n");
            table.print(buffer);
            TustedDeleteReconciliation.LOG.info(buffer.toString());
          }
          else {
            final long[] event = this.facade.deleteDetectedAccounts(missing);
            final TableFormatter table  = new TableFormatter().header("Event ID");
            for (long id : event) {
              table.row().column(id);
            }
            final StringBuilder buffer = new StringBuilder("Created delete events:\n");
            table.print(buffer);
            TustedDeleteReconciliation.LOG.info(buffer.toString());
          }
        }
        else {
          TustedDeleteReconciliation.LOG.info("No accounts to be deleted.", new Object[0]);
        }
      }
      catch (tcColumnNotFoundException e) {
        throw new OIMException(e);
      }
      catch (tcDataNotProvidedException e) {
        throw new OIMException(e);
      }
      catch (tcIDNotFoundException e) {
        throw new OIMException(e);
      }
      catch (tcAPIException e) {
        throw new OIMException(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: obsolete
    /**
     ** This API retrieves all USER or ACCOUNT keys from Identity Governance and
     ** compares them with the keys present in the set returned by the
     ** provideDeletionDetectionData method.
     **
     ** @param  objectName       the name of the resource Object or profile
     **                          Name.
     **                          <br>
     **                          For user and account reconciliation setup the
     **                          resource object name is used as the default
     **                          profile name.
     ** @param  matchedAccounts  the collection which contains keys of matched
     **                          data info returned by the
     **                          provideDeletionDetectionData method.
     **
     ** @return                  a {@link tcResultSet} which contains all the
     **                          users or accounts which are found in Identity
     **                          Governance but missing from the target.
     **
     ** @throws tcAPIException
     ** @throws tcIDNotFoundException
     ** @throws tcDataNotProvidedException
     */
    // oracle.iam.reconciliation.api.ReconOperationsServiceImpl.getMissingAccounts
    private tcResultSet obsolete(final String objectName, final Set<Serializable> found, final Map<String, Serializable> scope)
      throws tcAPIException
      ,      tcIDNotFoundException
      ,      tcDataNotProvidedException {

      final Profile profile = profile(objectName);
      final String query = "select '' as obj_name, usr.usr_key,usr.usr_login from usr usr where usr.usr_status!='Deleted' and ((usr.usr_data_level!=1 and usr.usr_data_level!=2) or usr.usr_data_level is null) and NOT exists (SELECT 1 FROM TABLE(?) tab WHERE tab.COLUMN_VALUE=usr.usr_key)" + apply(profile, scope);
      tcDataSet dataSet = null;
      try {
        dataSet = executeQuery(objectName, found, query);
      }
      catch (Exception e) {
//        TustedDeleteReconciliation.LOG.log(Level.SEVERE, "IAM-5010000", e);
        throw new tcAPIException(LRB.DEFAULT.getString("IAM-5012226", new Object[] { e.getMessage() }), e);
      }
      return new tcMetaDataSet(dataSet, XLDatabase.getInstance());
    }

    // oracle.iam.reconciliation.impl.BaseEntityTypeHandler.getScopeQuery
    protected String apply(final Profile profile, final Map<String, Serializable> scope) {
      if (scope == null)
        return "";
      final StringBuilder query = new StringBuilder(0);
      for (TargetAttribute attr : profile.getForm().getTargetAttributes()) {
         // that is what we have to change to map
        final String key = attr.getName();
        if (scope.keySet().contains(key)) {
          if (query.length() > 0)
            // that is what we have to change to make it happens
            query.append(" and ");

          Object value = scope.get(key);
          if (attr.getOIMAttribute().getFieldType() == Type.String)
            value = "'" + value + "'";
          query.append(attr.getOIMAttribute().getFieldName()).append("=").append(value);
        }
      }
      if (query.length() > 0)
        query.insert(0, " and (").append(")");
      return query.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: profile
    /**
     ** Returns the reconciliation profile for the specified profile name.
     **
     ** @param  name             the name of the desired {@link Profile}.
     **
     ** @return                  the 
     */
    // oracle.iam.reconciliation.api.ReconOperationsServiceImpl.getProfile
    private Profile profile(final String name) {
      Profile profile = null;
      final ProfileManager mgr = Platform.getBean(ProfileManager.class);
      try {
        profile = mgr.getProfile(name);
      }
      catch (Exception e) {
        throw new ReconciliationException(e.getMessage(), e);
      }
      if (null == profile)
        throw new ReconciliationException(LRB.DEFAULT.getString("IAM-5012200", name));

      return profile;
    }

    // oracle.iam.reconciliation.api.ReconOperationsServiceImpl.executeMissingAccountsQuery (simplified)
    private tcDataSet executeQuery(final String objectName, final Set<Serializable> found, final String query)
      throws Exception {

      tcDataSet             dataSet;
      Connection            connection = Platform.getOperationalDS().getConnection().unwrap(oracle.jdbc.OracleConnection.class);
      PreparedStatementUtil statement  = new PreparedStatementUtil();
      try {
        statement.setStatement(XLDatabase.getInstance(), query);
        // the deprecated API is required to avoid unsuported feature exception
        @SuppressWarnings("deprecation") final ArrayDescriptor array = ArrayDescriptor.createDescriptor("OIM_TYP_NUMBERARR", connection);
        @SuppressWarnings("deprecation") final ARRAY           value = new ARRAY(array, connection, found.toArray());
        statement.setArray(1, value);
        statement.execute();
        dataSet = statement.getDataSet();
        dataSet.setString("obj_name", objectName);
        return dataSet;
      }
      finally {
        if (null != connection)
          try {
            connection.close();
          }
          catch (SQLException e) {
            // intentionally left blank (but why)
            ;
          }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Delete
  // ~~~~~ ~~~~~~
  /**
   ** <code>Delete</code> reconciliation service, which can be used to delete
   ** objects in Identity Manager, it needs to be used in the
   ** following way:
   ** <ul>
   **   <li>Add all reconciliation events representing the objects on the source
   **       resource by using {@link #handle(ConnectorObject)} method.
   **   <li>Call {@link #finish()} method to delete identities which are present
   **       in Identity Manager but where not added by
   **       {@link #handle(ConnectorObject)}.
   ** </ul>
   */
  private class Delete implements ResultsHandler {
  
    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final ReconciliationService.DeletionDetectionService service;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Delete</code> executer that allows use as a
     ** JavaBean.
     **
     ** @param  service          the api to detect deletions.
     **                          <br>
     **                          Allowed object is
     **                          {@link ReconciliationService.DeletionDetectionService}.
     */
    private Delete(final ReconciliationService.DeletionDetectionService service) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.service = service;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object           the object return from the search.
     **
     ** @return                  <code>true</code> if we should keep processing;
     **                          otherwise <code>false</code> to cancel.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      TustedDeleteReconciliation.LOG.ok("Handling object with UID [{0}]", new Object[] { object.getUid().getUidValue() });
      try {
        final ReconEvent event     = new ReconEvent(object, TustedDeleteReconciliation.this.getReconFieldMapping(), TustedDeleteReconciliation.this.getResourceConfig().getITResource(), TustedDeleteReconciliation.this.getReconciliationService().getDefaultDateFormat());
        boolean          exclusion = ResourceExclusion.newInstance(TustedDeleteReconciliation.this.getObjectType(), TustedDeleteReconciliation.this.getResourceConfig()).isMarkedForExclusion(event);
        if (exclusion) {
          TustedDeleteReconciliation.LOG.warn("Object with UID [{0}] ignored as its listed for exclusion", new Object[] { object.getUid().getUidValue() });
          return true;
        }
        Validation.newInstance(TustedDeleteReconciliation.this.getObjectType(),     TustedDeleteReconciliation.this.getResourceConfig()).validate(event);
        Transformation.newInstance(TustedDeleteReconciliation.this.getObjectType(), TustedDeleteReconciliation.this.getResourceConfig()).transform(event);
        this.service.addObjectOnResource(event);
      }
      catch (ValidationException e) {
        TustedDeleteReconciliation.LOG.warn("Object with UID [{0}] is ignored as validation failed [{1}]", new Object[] { object.getUid().getUidValue(), e.getMessage() });
      }
      return !TustedDeleteReconciliation.this.isStopped();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TustedDeleteReconciliation</code> jod that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TustedDeleteReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (AbstractReconTask)
  /**
   ** The entry point of the scheduler task thread.
   */
  @Override
  protected final void execute() {
    final boolean         dryRun  = Boolean.valueOf(getParameter(GATHERONLY, true));
    final Lookup          scope   = getResourceConfig().getObjectTypeLookup("User", "Recon Identity Scope");
    final Service         service = new Service(dryRun, getResourceObjectName(), scope, Platform.getService(ReconOperationsService.class));
    final Delete          handler = new Delete(service);
    final ConnectorFacade facade  = getConnectorFacade();
    facade.search(getObjectClass(), getFilter(),   handler, buildAttributesToGet());
    service.deleteUnmatched();
  }
}