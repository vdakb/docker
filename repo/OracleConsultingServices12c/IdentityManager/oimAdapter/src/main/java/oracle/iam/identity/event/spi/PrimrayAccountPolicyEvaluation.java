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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Plugin Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   AccessPolicyProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.event.spi;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collections;
import java.util.StringTokenizer;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.sql.Timestamp;

import com.thortech.xl.dataobj.tcUHD;
import com.thortech.xl.dataobj.tcUPD;
import com.thortech.xl.dataobj.tcUPP;
import com.thortech.xl.dataobj.tcUPH;
import com.thortech.xl.dataobj.APIUtils;
import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.cache.CacheUtil;

import com.thortech.xl.dataobj.util.tcDateFormatter;

import com.thortech.xl.dataaccess.tcDataSetException;

import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import com.thortech.xl.util.StringUtil;

import com.thortech.xl.audit.engine.AuditEngine;

import java.io.Serializable;

import oracle.iam.provisioning.vo.Account;

import oracle.iam.platform.context.ContextManager;

import oracle.iam.platform.utils.ChildTableRecord;

import oracle.iam.platform.utils.logging.SuperLogger;

import oracle.iam.accesspolicy.resources.LRB;

import oracle.iam.accesspolicy.vo.AccountChange;
import oracle.iam.accesspolicy.vo.TargetIdentifier;
import oracle.iam.accesspolicy.vo.PolicyObjectDetails;
import oracle.iam.accesspolicy.vo.AccountProfileChanges;

import oracle.iam.accesspolicy.impl.util.ListBatcher;
import oracle.iam.accesspolicy.impl.util.AccessPolicyUtil;

import oracle.iam.accesspolicy.utils.AccessPolicyUtils;

import oracle.iam.accesspolicy.exception.AccessPolicyEvaluationException;

import static oracle.iam.accesspolicy.utils.Constants.DATE;
import static oracle.iam.accesspolicy.utils.Constants.REVOKE;
import static oracle.iam.accesspolicy.utils.Constants.DISABLE;
import static oracle.iam.accesspolicy.utils.Constants.DISABLED;
import static oracle.iam.accesspolicy.utils.Constants.BATCH_SIZE;
import static oracle.iam.accesspolicy.utils.Constants.ACCESS_POLICY;
import static oracle.iam.accesspolicy.utils.Constants.JOB_HISTROY_ID;
import static oracle.iam.accesspolicy.utils.Constants.ALLOW_AP_HARVESTING;

import static oracle.iam.accesspolicy.utils.Constants.USER_KEY;
import static oracle.iam.accesspolicy.utils.Constants.USER_ACCOUNT_KEY;
import static oracle.iam.accesspolicy.utils.Constants.USER_DEPROVISIONED_DATE;

import static oracle.iam.accesspolicy.utils.Constants.OBJECT_KEY;
import static oracle.iam.accesspolicy.utils.Constants.OBJECT_NAME;
import static oracle.iam.accesspolicy.utils.Constants.OBJECT_STATUS;
import static oracle.iam.accesspolicy.utils.Constants.OBJECT_INSTANCE_KEY;
import static oracle.iam.accesspolicy.utils.Constants.OBJECT_DENIED_BY_POLICY;

import static oracle.iam.accesspolicy.utils.Constants.OIU_PROV_MECHANISM;
import static oracle.iam.accesspolicy.utils.Constants.OIU_PROV_MECHANISM_APHARVESTED;
import static oracle.iam.accesspolicy.utils.Constants.OIU_PROV_MECHANISM_BULKLOAD;
import static oracle.iam.accesspolicy.utils.Constants.OIU_PROV_MECHANISM_RECONCILIATION;

import static oracle.iam.accesspolicy.utils.Constants.ACCOUNT_KEY;
import static oracle.iam.accesspolicy.utils.Constants.ACCOUNT_TYPE;

import static oracle.iam.accesspolicy.utils.Constants.POLICY_KEY;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_NAME;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_RETROFIT;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_PRIORITY;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_ALLOWED_USER_ACCOUNT;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROVISIONED_WITH_REQUEST;

import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROFILE_KEY;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROFILE_ROWVER;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROFILE_DENY_LIST;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROFILE_ALLOW_LIST;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROFILE_ORG_DENY_LIST;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROFILE_DETAILS_ALLOW_LIST;

import static oracle.iam.accesspolicy.utils.Constants.OBJACTION_INAPPLICABLE;
import static oracle.iam.accesspolicy.utils.Constants.OBJACTION_INAPPLICABLE_ACCOUNT;

import static oracle.iam.accesspolicy.utils.Constants.ALLOW_OBJECT_FOR_ALL_ORGS;
import static oracle.iam.accesspolicy.utils.Constants.ALLOW_MULTIPLE_OBJECT_INSTANCES;

import static oracle.iam.accesspolicy.utils.Constants.CHILD_SDK_KEY;
import static oracle.iam.accesspolicy.utils.Constants.CHILD_FORM_KEY;
import static oracle.iam.accesspolicy.utils.Constants.CHILD_FORM_NAME;
import static oracle.iam.accesspolicy.utils.Constants.CHILD_FORM_FIELD_VALUE;
import static oracle.iam.accesspolicy.utils.Constants.CHILD_FORM_RECORD_NUMBER;

import static oracle.iam.accesspolicy.utils.Constants.FORM_KEY;
import static oracle.iam.accesspolicy.utils.Constants.FORM_NAME;
import static oracle.iam.accesspolicy.utils.Constants.FORM_FIELD_NAME;
import static oracle.iam.accesspolicy.utils.Constants.FORM_FIELD_TYPE;
import static oracle.iam.accesspolicy.utils.Constants.FORM_FIELD_VERSION;
import static oracle.iam.accesspolicy.utils.Constants.FORM_ACTIVE_VERSION;

import static oracle.iam.accesspolicy.utils.Constants.PARENT_FORM_NAME;
import static oracle.iam.accesspolicy.utils.Constants.PARENT_FORM_FIELD_NAME;
import static oracle.iam.accesspolicy.utils.Constants.PARENT_FORM_FIELD_VALUE;

import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROFILE_DETAILS_DENY_LIST;
import static oracle.iam.accesspolicy.utils.Constants.POLICY_PROFILE_DETAILS_ROWVER;

import static oracle.iam.accesspolicy.utils.Constants.ARCHIVED_POLICY_PROFILE_KEY;
import static oracle.iam.accesspolicy.utils.Constants.ARCHIVED_POLICY_PROFILE_DETAILS_ALLOW_LIST;
import static oracle.iam.accesspolicy.utils.Constants.ARCHIVED_POLICY_PROFILE_DETAILS_DENY_LIST;

////////////////////////////////////////////////////////////////////////////////
// class PrimrayAccountPolicyEvaluation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>PrimrayAccountPolicyEvaluation</code> implements the functionality
 ** to evaluate account provisioning based on <code>Access Policies</code> for a
 ** primary account only.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PrimrayAccountPolicyEvaluation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String  KEY_SUFFIX                               = "_KEY";
  static final String  ROWVER_SUFFIX                            = "_ROWVER";
  static final String  VERSION_SUFFIX                           = "_VERSION";
  static final String  ACTION_SUFFIX                            = "_REVOKE";

  static final String  ACCOUNTS_TO_REQUEST                      = "ACCOUNTS_TO_REQUEST";
  static final String  ACCOUNTS_TO_PROVISION                    = "ACCOUNTS_TO_PROVISION";

  static final String  childDataChangesSubQuery                 =
                               "SELECT DISTINCT SDC.SDK_KEY, POC.POC_RECORD_NUMBER, SDK.SDK_NAME," +
                               " SDC.SDC_NAME, SDC.SDC_VARIANT_TYPE, SDC.SDC_VERSION, POC.POC_FIELD_VALUE," +
                               " PARENT_SDK.SDK_KEY AS PARENT_SDK_KEY," +
                               " PARENT_SDK.SDK_NAME AS PARENT_SDK_NAME," +
                               " PARENT_SDK.SDK_ACTIVE_VERSION AS PARENT_SDK_ACTIVE_VERSION" +
                               " FROM SDH SDH, SDC SDC, POC POC, SDK SDK, SDK PARENT_SDK" +
                               " WHERE SDH.SDH_PARENT_VERSION = PARENT_SDK.SDK_ACTIVE_VERSION" +
                               " AND SDH.SDH_CHILD_VERSION = SDK.SDK_ACTIVE_VERSION" +
                               " AND POC.POC_PARENT_SDK_KEY = PARENT_SDK.SDK_KEY" +
                               " AND SDC.SDC_VERSION = SDH.SDH_CHILD_VERSION" +
                               " AND SDC.SDC_NAME = POC.POC_FIELD_NAME" +
                               " AND SDK.SDK_TYPE = 'P'" +
                               " AND SDC.SDK_KEY = SDK.SDK_KEY" +
                               " AND SDH.SDH_CHILD_KEY = SDC.SDK_KEY" +
                               " AND POC.POC_PARENT_SDK_KEY = SDH.SDH_PARENT_KEY";

  static final boolean DEBUG                                    = true;
  static final Logger  LOGGER                                   = SuperLogger.getLogger();

  // This set holds the prov mechanism that are considered for policy harvestation. 
  // Currently Bulk Loaded and Reconciled accounts are considered for linking applicable policy to them. 
  private final static Set<String> policySupportedProvMechanismSet = createPolicySupportedProvMechanismSet();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // Collection of all objects that are allowed for user's organization
  Vector<String>                                 objectsAllowedForOrg = new Vector<String>();
  // Collection of objects that are denied for the user by applicable policies
  final Hashtable<String, String>                deniedObjects        = new Hashtable<String, String>();
  // Collection of objects that are denied by user organization
  final Hashtable<String, String>                objectsDeniedForOrg  = new Hashtable<String, String>();
  // ???
  final Hashtable<String, String[]>              ihPolicies           = new Hashtable<String, String[]>();
  // policy profile for the user
  final Hashtable<String, String[]>              policyProfile        = new Hashtable<String, String[]>();
  // Collection of all objects that are allowed for the user (based on the
  // user's organization and group membership) and information on the highest
  // priority policy that allows the object
  final Hashtable<String, PolicyObjectDetails>   allowedObjects       = new Hashtable<String, PolicyObjectDetails>();
  // Collection of all objects that are allowed for the user (based on the
  // user's organization and group membership) and list of other policies that
  // allow the object
  final Hashtable<String, Map<String, String[]>> supplementalPolicies = new Hashtable<String, Map<String, String[]>>();
  // Collection of provisioning choices for each allowed object (specified by
  // the policy definition)
  final Hashtable<String, Vector<String>>        provisioningOptions  = new Hashtable<String, Vector<String>>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PrimaryAccountEvaluationPolicy</code> service that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PrimrayAccountPolicyEvaluation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   determineAccountProfileChanges
  /**
   ** Determinse the changes happend due to assigned or removed roles for an
   ** identity that belongs to the given <code>userKey</code>.
   **
   ** @param  userKey            the internal system identifier of an identity.
   **
   ** @return                    the changes applicable at accounts assocaited
   **                            with identity belonging to
   **                            <code>userKey</code>.
   **
   ** @throws AccessPolicyEvaluationException if an error occurs.
   */
  public AccountProfileChanges determineAccountProfileChanges(String userKey)
    throws AccessPolicyEvaluationException {

    AccountProfileChanges accountProfileChanges = null;
    try {
      if (isValidProvisioningWindow(userKey)) {
        if (AccessPolicyUtil.isRoleHierarchySupported()) {
          LOGGER.log(Level.INFO, "Started role hierarchy policy evaluation for userKey: ", userKey);
          final List<Long> rids = AccessPolicyUtil.getRoleIDSet(userKey);
          for (ListBatcher<Long> cursor = new ListBatcher<Long>(rids, BATCH_SIZE); cursor.hasValues(); cursor.next()) {
            final List<Map<String, String>> m = AccessPolicyUtil.getApplicablePoliciesList(cursor.getValues());
            buildUserPolicyProfile(userKey, m);
          }
        }
        else {
          LOGGER.log(Level.INFO, "Started policy evaluation for userKey: ", userKey);
          buildUserPolicyProfile(userKey);
        }
        // Compare the newly build user policy profile with the existing policy
        // profile and get a list of all policies that don't apply any more
        final Map<String, String> inapplicablePolicies = getPoliciesThatNoLongerApply(userKey);
        // Update the user's policy profile in the database
        updateUserPolicyProfile(userKey);

        // Provision objects which are in the allow list and are not
        // currently provisioned
        long starttime = System.currentTimeMillis();
        accountProfileChanges = getAccountChanges(userKey, inapplicablePolicies);
        long endtime = System.currentTimeMillis();
        LOGGER.log(Level.FINE, "PolicyEvaluationUtil.determineAccountProfileChanges Time taken to compute account changes is " + (endtime - starttime)); //Moved to debug level as time is already getting computed for AccessPolicyUtil.
      }
    }
    catch (tcDataAccessException e) {
      LOGGER.log(Level.SEVERE, "IAM-4030052", userKey);
      LOGGER.log(Level.SEVERE, "IAM-4030000", getPrintableStackTrace(e));
      throw new AccessPolicyEvaluationException(LRB.DEFAULT.getString("IAM-4030052", userKey), e);
    }
    catch (tcDataSetException e) {
      LOGGER.log(Level.SEVERE, "IAM-4030052", userKey);
      LOGGER.log(Level.SEVERE, "IAM-4030000", getPrintableStackTrace(e));
      throw new AccessPolicyEvaluationException(LRB.DEFAULT.getString("IAM-4030052", userKey), e);
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, "IAM-4030052", userKey);
      LOGGER.log(Level.SEVERE, "IAM-4030000", getPrintableStackTrace(e));
      throw new AccessPolicyEvaluationException(LRB.DEFAULT.getString("IAM-4030052", userKey), e);
    }

    return accountProfileChanges;
  }

  /**
   *
   * @param userKey
   * @return
   */
  private boolean isValidProvisioningWindow(String userKey) {
    try {
      PreparedStatementUtil pstmt = new PreparedStatementUtil();
      pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_PROVISIONING_WINDOW"));
      pstmt.setString(1, userKey);
      pstmt.execute();
      tcDataSet userProvWindowDS = pstmt.getDataSet();

      Date deprovisionedDate = userProvWindowDS.getDate(USER_DEPROVISIONED_DATE);

      // return false if user deprovisioned date is already set for the user
      if (deprovisionedDate.getTime() != 0) {
        return false;
      }
    }
    catch (Exception e) {
      return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildUserPolicyProfile
  /**
   *
   * @param userKey
   * @throws tcDataSetException
   * @throws tcDataAccessException
   * @throws Exception
   */
  private void buildUserPolicyProfile(String userKey)
    throws tcDataAccessException
    ,      tcDataSetException {

    // get list of objects that are allowed for this user's organization
    this.objectsAllowedForOrg = getAllowListforOrg(userKey);

    // get all policies which apply to this user
    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_APPLICABLE_POLICIES"));
    pstmt.setString(1, userKey);
    pstmt.execute();
    tcDataSet applicablePoliciesDS = pstmt.getDataSet();

    // get all objects that are associated with each policy
    for (int i = 0; i < applicablePoliciesDS.getRowCount(); i++) {
      applicablePoliciesDS.goToRow(i);
      String policyKey        = applicablePoliciesDS.getString(POLICY_KEY);
      String policyName       = applicablePoliciesDS.getString(POLICY_NAME);
      String approvalRequired = applicablePoliciesDS.getString(POLICY_PROVISIONED_WITH_REQUEST);
      String priority         = applicablePoliciesDS.getString(POLICY_PRIORITY);
      String retrofit         = applicablePoliciesDS.getString(POLICY_RETROFIT);
      // Get all objects that are associated with this policy
      pstmt = new PreparedStatementUtil();
      pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_OBJECTS_ASSOCIATED_WITH_POLICY"));
      pstmt.setString(1, policyKey);
      pstmt.execute();
      tcDataSet associatedObjectsDS = pstmt.getDataSet();

      // Analyze object properties and determine if the object is allowed or denied.
      // Add to allowedObjects, deniedObjects, objectsDeniedForOrg and policyProfile
      for (int j = 0; j < associatedObjectsDS.getRowCount(); j++) {
        associatedObjectsDS.goToRow(j);
        String objectKey             = associatedObjectsDS.getString("OBJ_KEY").trim();
        String objectName            = associatedObjectsDS.getString(OBJECT_NAME).trim();
        String objectDeniedByPolicy  = associatedObjectsDS.getString(OBJECT_DENIED_BY_POLICY).trim();
        String objActionInapplicable = associatedObjectsDS.getString(OBJACTION_INAPPLICABLE);
        String allowAllOrgs          = associatedObjectsDS.getString(ALLOW_OBJECT_FOR_ALL_ORGS).trim();
        String allowMultipleInstance = associatedObjectsDS.getString(ALLOW_MULTIPLE_OBJECT_INSTANCES).trim();

        objActionInapplicable = isNullOrEmpty(objActionInapplicable) ? "0" : objActionInapplicable.trim();
        PolicyObjectDetails policyObjectDetails = new PolicyObjectDetails(policyKey, policyName, priority, approvalRequired,
          objActionInapplicable, objectKey, objectName, allowAllOrgs, allowMultipleInstance, objectDeniedByPolicy);
        policyObjectDetails.setRetrofit(retrofit);
        processObject(policyObjectDetails);
      }
    }

    // Remove objects that are denied from the list of allowed objects
    for (String objKey : this.deniedObjects.keySet()) {
      this.allowedObjects.remove(objKey);
    }
    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.buildUserPolicyProfile()..printing HashTable allowedObjects: " + this.allowedObjects + " for userKey: " + userKey);
    // FOLLOW UP: Shouldn't objects that are denied for the user's organization
    // also be removed from the allowed objects list?
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildUserPolicyProfile
  //copy of method below on tcDataSet is passed in.
  private void buildUserPolicyProfile(final String userKey, final List<Map<String, String>> l)
    throws tcDataAccessException
    ,       tcDataSetException {

    // get list of objects that are allowed for this user's organization
    this.objectsAllowedForOrg = getAllowListforOrg(userKey);
    // get all objects that are associated with each policy
    for (Map<String, String> m : l) {
      String policyKey        = m.get(POLICY_KEY);
      String policyName       = m.get(POLICY_NAME);
      String approvalRequired = m.get(POLICY_PROVISIONED_WITH_REQUEST);
      String priority         = m.get(POLICY_PRIORITY);
      String retrofit         = m.get(POLICY_RETROFIT);
      // get all objects that are associated with this policy
      String objectsAssociatedWithPolicyQuery = "SELECT OBJ.OBJ_KEY, OBJ.OBJ_NAME, OBJ.OBJ_ALLOW_MULTIPLE, OBJ.OBJ_ALLOWALL, POP.POP_DENIAL, POP.POP_REVOKE_OBJECT FROM POP POP, OBJ OBJ WHERE POP.POL_KEY=" + policyKey + " AND POP.OBJ_KEY = OBJ.OBJ_KEY";
      tcDataSet associatedObjectsDS           = CacheUtil.getSetCachedQuery(AccessPolicyUtil.getDatabase(), objectsAssociatedWithPolicyQuery, "PolicyEvaluation.oawp." + policyKey, CacheUtil.ACCESS_POLICIES);
      // analyze object properties and determine if the object is allowed or
      // denied.
      // add to allowedObjects, deniedObjects, objectsDeniedForOrg and
      // policyProfile
      for (int j = 0; j < associatedObjectsDS.getRowCount(); j++) {
        associatedObjectsDS.goToRow(j);
        String objectKey             = associatedObjectsDS.getString("OBJ_KEY").trim();
        String objectName            = associatedObjectsDS.getString(OBJECT_NAME).trim();
        String objectDeniedByPolicy  = associatedObjectsDS.getString(OBJECT_DENIED_BY_POLICY).trim();
        String objActionInapplicable = associatedObjectsDS.getString(OBJACTION_INAPPLICABLE);
        String allowAllOrgs          = associatedObjectsDS.getString(ALLOW_OBJECT_FOR_ALL_ORGS).trim();
        String allowMultipleInstance = associatedObjectsDS.getString(ALLOW_MULTIPLE_OBJECT_INSTANCES).trim();
        objActionInapplicable        = isNullOrEmpty(objActionInapplicable) ? DISABLE : objActionInapplicable.trim();
        PolicyObjectDetails policyObjectDetails = new PolicyObjectDetails(policyKey, policyName, priority, approvalRequired, objActionInapplicable, objectKey, objectName, allowAllOrgs, allowMultipleInstance, objectDeniedByPolicy);
        policyObjectDetails.setRetrofit(retrofit);
        processObject(policyObjectDetails);
      }
    }
    // Remove objects that are denied from the list of allowed objects
    for (String objKey : this.deniedObjects.keySet()) {
      this.allowedObjects.remove(objKey);
    }
    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.buildUserPolicyProfile()..printing HashTable allowedObjects: " + allowedObjects + " for userKey: " + userKey);
    // FOLLOW UP: Shouldn't objects that are denied for the user's organization
    // also be removed from the allowed objects list?
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saveUserPolicyProfile
  /**
   ** Save user's new policy profile in the database (UPP)
   **
   ** @param  userKey
   **
   ** @throws tcDataSetException
   ** @throws tcDataAccessException
   */
  private void saveUserPolicyProfile(String userKey)
    throws tcDataAccessException
    ,      tcDataSetException {

    tcUPP uppDO = null;
    // Retrieve the current policy profile entry for this user from UPP
    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_USER_POLICY_PROFILE"));
    pstmt.setString(1, userKey);
    pstmt.execute();
    tcDataSet currentPolicyProfileDS = pstmt.getDataSet();
    if (currentPolicyProfileDS.getRowCount() == 0) {
      uppDO = new tcUPP(AccessPolicyUtil.getDatabase(), "", userKey, new byte[0]);
    }
    else {
      uppDO = new tcUPP(AccessPolicyUtil.getDatabase(), currentPolicyProfileDS.getString(POLICY_PROFILE_KEY), currentPolicyProfileDS.getString(USER_KEY), currentPolicyProfileDS.getByteArray(POLICY_PROFILE_ROWVER));
    }

    // get vector containing object names from the allow list
    Enumeration<String> allowedObjectKeys = this.allowedObjects.keys();
    Vector<String>      allowList         = new Vector<String>();
    StringBuffer        buffer            = new StringBuffer();
    while (allowedObjectKeys.hasMoreElements()) {
      String              allowedObjectKey     = allowedObjectKeys.nextElement();
      PolicyObjectDetails allowedObjectDetails = this.allowedObjects.get(allowedObjectKey);
      String              objectName           = allowedObjectDetails.getObjectName();
      String              policyName           = allowedObjectDetails.getPolicyName();

      buffer.append(objectName); // Object name
      buffer.append(" [");
      buffer.append(policyName); // policy name

      // check if there are other policies that allow the same resource
      if (this.supplementalPolicies.containsKey(objectName)) {
        Map<String, String[]> supplementalPoliciesDetails = this.supplementalPolicies.get(objectName);
        Iterator<String>      iter = supplementalPoliciesDetails.keySet().iterator();
        while (iter.hasNext()) {
          String policyKey = iter.next();
          String supplementalPolicyDetails[] = supplementalPoliciesDetails.get(policyKey);
          buffer.append(':');
          buffer.append(supplementalPolicyDetails[1]);
        }
      }
      buffer.append(']');
      allowList.addElement(buffer.toString());
      buffer.delete(0, buffer.length()); // ready for the next string
    }
    // make a deny list which is a union of the objects denied by policy and
    // objects denied for org
    HashSet<String>     denySet           = new HashSet<String>();
    Enumeration<String> deniedObjectsName = this.deniedObjects.elements();
    while (deniedObjectsName.hasMoreElements()) {
      denySet.add(deniedObjectsName.nextElement());
    }

    Enumeration<String> orgDeniedObjectsName = this.objectsDeniedForOrg.elements();
    while (orgDeniedObjectsName.hasMoreElements()) {
      denySet.add(orgDeniedObjectsName.nextElement());
    }

    Iterator<String> moIterator = denySet.iterator();
    Vector<String>   denyList   = new Vector<String>();
    while (moIterator.hasNext()) {
      denyList.addElement(moIterator.next());
    }
    uppDO.setString(POLICY_PROFILE_ALLOW_LIST,    AccessPolicyUtil.getCommaSeparatedstring(allowList.elements()));
    uppDO.setString(POLICY_PROFILE_DENY_LIST,     AccessPolicyUtil.getCommaSeparatedstring(denyList.elements()));
    uppDO.setString(POLICY_PROFILE_ORG_DENY_LIST, AccessPolicyUtil.getCommaSeparatedstring(objectsDeniedForOrg.elements()));
    uppDO.save();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateUserPolicyProfile
  /**
   ** Update the User's Policy Profile. The policy profile is a record of all
   ** current and historical policy constraints for this user.
   **
   ** @param  userKey
   **
   ** @throws tcDataSetException
   ** @throws tcDataAccessException
   */
  private void updateUserPolicyProfile(String userKey)
    throws tcDataAccessException
    ,      tcDataSetException {

    // Retrieve the current policy profile entry for this user from UPP
    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_USER_POLICY_PROFILE"));
    pstmt.setString(1, userKey);
    pstmt.execute();
    tcDataSet currentPolicyProfileDS = pstmt.getDataSet();

    // Check if the user currently has a policy profile
    if (currentPolicyProfileDS.getRowCount() == 0) {
      // User doesn't have a policy profile yet (in UPP)
      // If the current policy profile is empty there is nothing to do
      if (this.ihPolicies.size() == 0) {
        return;
      }
      // Save user's new policy profile (in UPP)
      saveUserPolicyProfile(userKey);
      // read the new policy profile key for this user by querying the database
      pstmt = new PreparedStatementUtil();
      pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_USER_POLICY_PROFILE_KEY"));
      pstmt.setString(1, userKey);
      pstmt.execute();
      String uppKey = pstmt.getDataSet().getString(POLICY_PROFILE_KEY);
      addUserPolicyProfileDetails(uppKey);
    }
    else {
      // User has a policy profile (in UPP)
      String uppKey = currentPolicyProfileDS.getString(POLICY_PROFILE_KEY);
      // Archive user's current policy profile (from UPP to UPH)
      String uphKey = archiveUserPolicyProfile(userKey, currentPolicyProfileDS);
      // Archive user's policy profile details (from UPD to UHD)
      archiveUserPolicyProfileDetails(uppKey, uphKey);
      // Delete user's current policy profile details (from UPD)
      deleteUserPolicyProfileDetails(uppKey);
      // Add user's new policy profile details (to UPD)
      addUserPolicyProfileDetails(uppKey);
      // Save user's new policy profile in the database (UPP)
      saveUserPolicyProfile(userKey);
    }
    long jobID = (Long)ContextManager.getValue(JOB_HISTROY_ID);
    LOGGER.log(Level.INFO, "AccessPolicy : AbstractEvaluationUtility.updateUserPolicyProfile(): JOBID : "+ jobID +" Done updating access policy profile for userKey: "+userKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountChanges (AbstractEvaluationUtility)
  private AccountProfileChanges getAccountChanges(String userKey, Map<String, String> inapplicablePolicies)
    throws tcDataAccessException
    ,      tcDataSetException
    ,      AccessPolicyEvaluationException {

    final AccountProfileChanges  accountProfileChanges     = new AccountProfileChanges();
    // obtain list of resources that are currently provisioned for this user
    tcDataSet                    currentAccountsDS         = getCurrentAccounts(userKey);
    // identify objects to provision i.e. objects which are allowed but are not
    // currently provisioned to the user. This includes that are to be direct
    // provisioned and objects that are to be requested.
    Map<String, Map<TargetIdentifier, PolicyObjectDetails>> newAccounts = getNewAccounts(currentAccountsDS);
    accountProfileChanges.addAccountsToRequest(newAccounts.get(ACCOUNTS_TO_REQUEST));
    accountProfileChanges.addAccountsToProvision(newAccounts.get(ACCOUNTS_TO_PROVISION));

    if (!AccessPolicyUtil.isUserDisabled(userKey)) {
      // identity accounts that are disabled and need to be re-enabled
      accountProfileChanges.addAccountsToEnable(getDisabledAccounts(currentAccountsDS));
    }

    // identify accounts that need to be updated
    accountProfileChanges.addAccountsToModify(getAccountsToModify(userKey, currentAccountsDS, inapplicablePolicies));
    // identify accounts that need to be revoked
    // this will be objects that are in the deny list
    accountProfileChanges.addAccountsToRevoke(getDeniedAccounts(currentAccountsDS));
    // identify accounts that need to be revoked
    // this will be accounts/objects that are neither in the allow list or deny
    // list but were provisioned because of access policies
    accountProfileChanges.addAccountsToRevoke(getAccountsThatNoLongerApplyMarkedForRevoke(currentAccountsDS));
    // identify accounts that need to be disabled
    // this will be accounts/objects that are neither in the allow list or deny
    // list but were provisioned because of access policies.
    accountProfileChanges.addAccountsToDisable(getAccountsThatNoLongerApplyMarkedForDisable(currentAccountsDS));
    // consolidate the compiled account profile changes into as few operations
    // as possible
    consolidateAccountProfileChanges(accountProfileChanges);
    return accountProfileChanges;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountChanges
  /**
   **
   ** @param  getCurrentAccounts
   ** @return                    the {@link DataSet} with all accounts
   **                            provisoned to the identity identified by the
   **                            given key <code>userKey</code>
   **
   ** @throws tcDataSetException
   ** @throws tcDataAccessException
   ** @throws Exception             if anything else goes wrong.
   */
  private tcDataSet getCurrentAccounts(final String userKey)
    throws tcDataSetException
    ,      tcDataAccessException {

    final PreparedStatementUtil pstmt = new PreparedStatementUtil();
    final StringBuilder builder = new StringBuilder(AccessPolicyUtil.getQuery("GET_PROVISIONED_RESOURCES"));
    builder.append(" AND oiu.account_type = ?");
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), builder.toString());
    pstmt.setString(1, userKey);
    pstmt.setString(1, "primary");
    pstmt.execute();
    return pstmt.getDataSet();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowListforOrg
  /**
   *
   * @param userKey
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   */
  private Map<String, String> getPoliciesThatNoLongerApply(String userKey)
    throws tcDataAccessException
    ,      tcDataSetException {

    Map<String, String> inapplicablePolicies = new HashMap<String, String>();
    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_USER_POLICY_PROFILE_WITH_POLICY_DETAILS"));
    pstmt.setString(1, userKey);
    pstmt.execute();
    tcDataSet userPolicyProfileWithDetails = pstmt.getDataSet();

    for (int i = 0; i < userPolicyProfileWithDetails.getRowCount(); i++) {
      userPolicyProfileWithDetails.goToRow(i);
      String policyKey = userPolicyProfileWithDetails.getString(POLICY_KEY);
      if (!policyProfile.containsKey(policyKey)) {
        inapplicablePolicies.put(policyKey, userPolicyProfileWithDetails.getString(POLICY_PROFILE_DETAILS_ALLOW_LIST));
      }
    }
    LOGGER.log(Level.FINE, "PolicyEvaluationUtil.getPoliciesThatNoLongerApply()..prinitng inapplicablePolicies: " + printMap(inapplicablePolicies) + " for userKey: " + userKey); //Moved log level from info to fine

    return inapplicablePolicies;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowListforOrg
  /**
   *
   * @param userKey
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   * @throws Exception
   */
  private Vector<String> getAllowListforOrg(String userKey)
    throws tcDataAccessException, tcDataSetException {
    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_OBJECTS_ALLOWED_FOR_USERS_ORG"));
    pstmt.setString(1, userKey);
    pstmt.execute();
    tcDataSet objsAllowedForOrgDS = pstmt.getDataSet();

    Vector<String> objectsAllowedForOrg = new Vector<String>();

    for (int k = 0; k < objsAllowedForOrgDS.getRowCount(); k++) {
      objsAllowedForOrgDS.goToRow(k);
      objectsAllowedForOrg.addElement(objsAllowedForOrgDS.getString(OBJECT_KEY));
    }

    return objectsAllowedForOrg;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processObject (AbstractEvaluationUtility)
  /**
   *
   * @param policyObjectDetails
   */
  private void processObject(PolicyObjectDetails policyObjectDetails) {
    String policyKey               = policyObjectDetails.getPolicyKey();
    String policyName              = policyObjectDetails.getPolicyName();
    long   priority                = Long.parseLong(policyObjectDetails.getPolicyPriority());
    String approvalRequired        = policyObjectDetails.getApprovalRequired();
    String objActionInapplicable   = policyObjectDetails.getObjActionInapplicable();
    String objectKey               = policyObjectDetails.getObjectKey();
    String objectName              = policyObjectDetails.getObjectName();
    String objectAllowedForAllOrgs = policyObjectDetails.getObjectAllowedForAllOrgs();
    //String allowMultipleInstance = policyObjectDetails.getAllowMultipleObjectInstances();
    String deniedByPolicy          = policyObjectDetails.getDeniedByPolicy();

    updatePolicyProfile(policyKey, deniedByPolicy, objectName);
    updateProvisioningChoices(objectKey, approvalRequired);

    if (deniedByPolicy.equals("1")) {
      this.deniedObjects.put(objectKey, objectName);
    }
    else {
      // Check if the object is allowed for the user's organization
      if (!(this.objectsAllowedForOrg.contains(objectKey)) && !(objectAllowedForAllOrgs.equals("1"))) {
        this.objectsDeniedForOrg.put(objectKey, objectName);
        //deniedObjects.put(psObjKey, psObjName);
        return;
      }

      // If object is already allowed by a policy then update revoke flag & policy info
      // in allowedObjects if necessary (as dictated by the priority)
      if (this.allowedObjects.containsKey(objectKey)) {
        // Set revokeInapplicable to 0 if either current policy or new policy says so
        PolicyObjectDetails currentPolicyObjectDetails    = this.allowedObjects.get(objectKey);
        String              currentObjActionNotApplicable = currentPolicyObjectDetails.getObjActionInapplicable();
        String              currentPolicyKey              = currentPolicyObjectDetails.getPolicyKey();
        String              currentPolicyName             = currentPolicyObjectDetails.getPolicyName();
        String              currentPolicyPriority         = currentPolicyObjectDetails.getPolicyPriority();
        //sdey - Here update logic to set the currentpolicydetails flag appropriately if DNLA policy is in place - clarify logic to handle priority
        if (objActionInapplicable.equals("2")) {
          currentPolicyObjectDetails.setObjActionInapplicable(DISABLE);
        }
        else if (currentObjActionNotApplicable.equals("1") || objActionInapplicable.equals("1")) {
          currentPolicyObjectDetails.setObjActionInapplicable(REVOKE);
        }
        this.allowedObjects.put(objectKey, currentPolicyObjectDetails);

        // if new policy is of higher priority (lower value) then replace the current policy
        // for this object in allowedObjects with the information of the new policy
        // also remove the current policy from ihPolicies
        // FOLLOW UP: Is this correct???
        // ER 8529350 : Make -1 as the lowest priority for Entitlements related AP
        if (priority < Long.parseLong(currentPolicyPriority) && priority != -1) {
          if ((this.ihPolicies != null) && this.ihPolicies.containsKey(currentPolicyKey)) {
            this.ihPolicies.remove(currentPolicyKey);
          }

          if (currentObjActionNotApplicable.equals(DISABLE)) {
            policyObjectDetails.setObjActionInapplicable(DISABLE);
          }
          this.allowedObjects.put(objectKey, policyObjectDetails);

          // add current policy information to supplementalPolicies
          if (this.supplementalPolicies.containsKey(objectName)) {
            Map<String, String[]> allOtherApplicablePoliciesForObject = this.supplementalPolicies.get(objectName);
            String                policyInfo[]                        = new String[] { currentPolicyKey, currentPolicyName, currentPolicyPriority };
            allOtherApplicablePoliciesForObject.put(currentPolicyKey, policyInfo);
            this.supplementalPolicies.put(objectName, allOtherApplicablePoliciesForObject);
          }
          else {
            Map<String, String[]> allOtherApplicablePoliciesForObject = new HashMap<String, String[]>();
            String                policyInfo[] = new String[] { currentPolicyKey, currentPolicyName, currentPolicyPriority };
            allOtherApplicablePoliciesForObject.put(currentPolicyKey, policyInfo);
            this.supplementalPolicies.put(objectName, allOtherApplicablePoliciesForObject);
          }
        }
        else {
          // remove new policy from ihPolicies
          // FOLLOW UP: Is this correct???
          this.ihPolicies.remove(policyKey);

          // add new policy information to supplementalPolicies
          if (this.supplementalPolicies.containsKey(objectKey)) {
            Map<String, String[]> allOtherApplicablePoliciesForObject = this.supplementalPolicies.get(objectName);
            String                policyInfo[]                        = new String[] { policyKey, policyName, Long.toString(priority) };
            allOtherApplicablePoliciesForObject.put(policyKey, policyInfo);
            this.supplementalPolicies.put(objectName, allOtherApplicablePoliciesForObject);
          }
          else {
            Map<String, String[]> allOtherApplicablePoliciesForObject = new HashMap<String, String[]>();
            String                policyInfo[]                        = new String[] { policyKey, policyName, Long.toString(priority) };
            allOtherApplicablePoliciesForObject.put(policyKey, policyInfo);
            this.supplementalPolicies.put(objectName, allOtherApplicablePoliciesForObject);
          }
        }
      }
      else {
        this.allowedObjects.put(objectKey, policyObjectDetails);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePolicyProfile
  /**
   *
   * @param policyKey
   * @param objectDeniedByPolicy
   * @param objectName
   */
  private void updatePolicyProfile(String policyKey, String objectDeniedByPolicy, String objectName) {
    String   allowedList;
    String   deniedList;
    String[] allowedDeniedList;

    if (this.ihPolicies.containsKey(policyKey)) {
      allowedDeniedList = this.ihPolicies.get(policyKey);
      allowedList = allowedDeniedList[0];
      deniedList = allowedDeniedList[1];
    }
    else {
      allowedDeniedList = new String[2];
      allowedList = "";
      deniedList  = "";
    }

    if (objectDeniedByPolicy.equals("1") == false) {
      allowedList = allowedList.equals("") ? objectName : allowedList + "," + objectName;
    }
    else {
      deniedList = deniedList.equals("") ? objectName : deniedList + "," + objectName;
    }
    // Save allowed, denied information by policy key
    allowedDeniedList[0] = allowedList;
    allowedDeniedList[1] = deniedList;
    this.ihPolicies.put(policyKey, allowedDeniedList);
    this.policyProfile.put(policyKey, allowedDeniedList);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isObjectAllowed
  /**
   ** Determines if the <code>Resource Object</code> specified by the given
   ** <code>objectKey</code> is contained in the allowed
   ** <code>Resource Object</code>s to be provisioned.
   **
   ** @param  objectKey          the <code>Resource Object</code> to lookup.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Resource Object</code> specified by the
   **                            given <code>objectKey</code> is contained in
   **                            the allowed <code>Resource Object</code>s to be
   **                            provisioned; otherwise <code>false</code>.
   **
   */
  private boolean isObjectAllowed(String objectKey) {
    return allowedObjects.containsKey(objectKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNewAccounts
  /**
   ** Evaluates the accounts that needs to be provisioned and requested.
   ** <p>
   ** Returned is a {@link Map} fro both types of provisioning actions.
   **
   ** @param  currentAccountsDS  the {@link DataSet} with accounts currently
   **                            provisioned.
   **
   ** @return                    the mapping for accounts to be provisioned and
   **                            requested.
   **
   ** @throws AccessPolicyEvaluationException
   ** @throws tcDataAccessException
   ** @throws Exception             if anything else goes wrong.
   */
  private Map<String, Map<TargetIdentifier, PolicyObjectDetails>> getNewAccounts(final tcDataSet currentAccountsDS)
    throws tcDataSetException
    ,      AccessPolicyEvaluationException  {

    Map<TargetIdentifier, PolicyObjectDetails> newAccountsToRequest   = new HashMap<TargetIdentifier, PolicyObjectDetails>();
    Map<TargetIdentifier, PolicyObjectDetails> newAccountsToProvision = new HashMap<TargetIdentifier, PolicyObjectDetails>();
    Map<String, Map<TargetIdentifier, PolicyObjectDetails>> newAccounts = new HashMap<String, Map<TargetIdentifier, PolicyObjectDetails>>();
    newAccounts.put(ACCOUNTS_TO_REQUEST, newAccountsToRequest);
    newAccounts.put(ACCOUNTS_TO_PROVISION, newAccountsToProvision);

    for (String objectKey : this.allowedObjects.keySet()) {
      if (isNewAccount(currentAccountsDS, objectKey)) {
        if (useDirectProvisioning(objectKey)) {
          newAccountsToProvision.put(new TargetIdentifier(objectKey, new HashMap<String, String>()), this.allowedObjects.get(objectKey));
        }
        else {
          newAccountsToRequest.put(new TargetIdentifier(objectKey, new HashMap<String, String>()), this.allowedObjects.get(objectKey));
        }
      }
    }

    return newAccounts;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isNewAccount
  /**
   ** Determines if the <code>Resource Object</code> specified by the given
   ** <code>objectKey</code> is contained in the accounts currently provisioned.
   **
   ** @param  currentAccountsDS  the {@link DataSet} containing the accounts
   **                            currently provisioned.
   ** @param  objectKey          the <code>Resource Object</code> to lookup.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Resource Object</code> specified by the
   **                            given <code>objectKey</code> is <b>NOT</b>
   **                            contained in the accounts currently
   **                            provisioned; otherwise <code>false</code>.
   **
   ** @throws tcDataSetException
   */
  private boolean isNewAccount(final tcDataSet currentAccountsDS, final String objectKey)
    throws tcDataSetException {

    for (int i = 0; i < currentAccountsDS.getRowCount(); i++) {
      currentAccountsDS.goToRow(i);
     if (currentAccountsDS.getString(OBJECT_KEY).trim().equals(objectKey)) {
        return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAddable
  private boolean isAddable(AccountChange accountChange, HashMap<String, Object> modifiedChildRecord, String formName, String revokeInapplicable) {
    boolean isAddable = false;
    int     index = accountChange.indexOfAddedChildDataChange(formName, modifiedChildRecord);
    if (index > -1) {
      if (isEntitlementRevokeIfNoLongerAppliesSupported()) {
        ChildTableRecord childTableRecord = accountChange.getChildFormRecord(formName, index);
        String           revokeInapplicabletoAdd = (String)childTableRecord.getChildData().get(formName + "_REVOKE");
        if (revokeInapplicabletoAdd.equalsIgnoreCase("1") && !revokeInapplicabletoAdd.equals(revokeInapplicable) ? true : false) {
          accountChange.removeChildFormRecord(formName, index);
          isAddable = true;
        }
      }
    }
    else {
      isAddable = true;
    }
    return isAddable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isUpdatable
  private boolean isUpdatable(AccountChange accountChange, HashMap<String, Object> modifiedChildRecord, String formName, String revokeInapplicable, String existingRevokeInapplicable, String policyKey, String existingPolicyKey) {
    boolean isUpdatable = false;
    if (isEntitlementRevokeIfNoLongerAppliesSupported()) {
      if (!policyKey.equalsIgnoreCase(existingPolicyKey)) {
        if (existingRevokeInapplicable.equalsIgnoreCase("0") && !existingRevokeInapplicable.equals(revokeInapplicable) ? true : false) {
          modifiedChildRecord.put(formName + "_REVOKE", "0");
        }
        isUpdatable = true;
      }
      else {
        if (existingRevokeInapplicable.equalsIgnoreCase("1") && !existingRevokeInapplicable.equals(revokeInapplicable) ? true : false) {
          isUpdatable = true;
        }
      }
      int index = accountChange.indexOfAddedChildDataChange(formName, modifiedChildRecord);
      if (index > -1) {
        ChildTableRecord childTableRecord = accountChange.getChildFormRecord(formName, index);
        String           revokeInapplicabletoAdd = (String)childTableRecord.getChildData().get(formName + "_REVOKE");
        if (revokeInapplicabletoAdd.equalsIgnoreCase("1") && !revokeInapplicabletoAdd.equals(revokeInapplicable) ? true : false) {
          accountChange.removeChildFormRecord(formName, index);
          isUpdatable = true;
        }
        else {
          isUpdatable = false;
        }
      }
    }
    return isUpdatable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRevokable
  private boolean isRevokable(String revoke) {
    if (isEntitlementRevokeIfNoLongerAppliesSupported()) {
      if (revoke != null && revoke.equalsIgnoreCase("1")) {
        return true;
      }
      else {
        return false;
      }
    }

    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isObjActionInapplicableUpdated
  /**
   *
   * @param currentAccountsDS
   * @param revokeInapplicable
   * @return
   * @throws tcDataSetException
   */
  private boolean isObjActionInapplicableUpdated(tcDataSet currentAccountsDS, String objActionInapplicable)
    throws tcDataSetException {

    String currentObjActionInapplicable = currentAccountsDS.getString(OBJACTION_INAPPLICABLE_ACCOUNT);
    // FOLLOW UP: Is it correct to update it only if the value changes from 1 to 0?
    return (!currentObjActionInapplicable.equals(DISABLE) && !currentObjActionInapplicable.equals(objActionInapplicable)) ? true : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAPHarvestingSupported
  private boolean isAPHarvestingSupported() {
    AccessPolicyUtils accessPolicyUtils = new AccessPolicyUtils();
    return accessPolicyUtils.isAPHarvestingSupported();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPolicyAllowedUserAccount
  /**
   *
   * @param currentAccountsDS
   * @return
   * @throws tcDataSetException
   */
  private boolean isPolicyAllowedUserAccount(tcDataSet currentAccountsDS)
    throws tcDataSetException {

    return currentAccountsDS.getString(POLICY_ALLOWED_USER_ACCOUNT).equals("1") ? true : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isHarvestableUserAccountBasedOnProvMechanism
  /**
   *
   * @param currentAccountsDS
   * @return
   * @throws tcDataSetException
   */
  private boolean isHarvestableUserAccountBasedOnProvMechanism(tcDataSet currentAccountsDS)
    throws tcDataSetException {

    // check if this account is reconciled/bulk loaded by looking into oiu_prov_mechanism
    return policySupportedProvMechanismSet.contains(currentAccountsDS.getString(OIU_PROV_MECHANISM).toUpperCase());
    //    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isProvMechanismPolicyHarvested
  /**
   *
   * @param currentAccountsDS
   * @return
   * @throws tcDataSetException
   */
  private boolean isProvMechanismPolicyHarvested(tcDataSet currentAccountsDS)
    throws tcDataSetException {

    // check if this account is reconciled/bulk loaded by looking into oiu_prov_mechanism
    return OIU_PROV_MECHANISM_APHARVESTED.equals(currentAccountsDS.getString(OIU_PROV_MECHANISM).toUpperCase());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEntitlementMatchingAttribute
  /**
   *
   * @param entitlementMatchingAttributes
   * @param formName
   * @param attributeName
   * @return
   */
  private boolean isEntitlementMatchingAttribute(Map<String, Set<String>> entitlementMatchingAttributes, String formName, String attributeName) {
    return entitlementMatchingAttributes.get(formName) != null ? entitlementMatchingAttributes.get(formName).contains(attributeName.toUpperCase()) : true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDeniedAccounts
  /**
   **Revoke any objects which are in the Deny list.
   **
   ** @param  currentAccountsDS  the objects which are already provisioned for
   **                            this user
   ** @throws tcDataSetException
   */
  private HashMap<String, AccountChange> getDeniedAccounts(tcDataSet currentAccountsDS)
    throws tcDataSetException {

    HashMap<String, AccountChange> deniedAccounts = new HashMap<String, AccountChange>();
    for (int i = 0; i < currentAccountsDS.getRowCount(); i++) {
      currentAccountsDS.goToRow(i);
      if (this.deniedObjects.containsKey(currentAccountsDS.getString(OBJECT_KEY))) {
        deniedAccounts.put(currentAccountsDS.getString(USER_ACCOUNT_KEY), null);
      }
    }
    return deniedAccounts;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisabledAccounts
  /**
   ** Determines if any <code>Resource Object</code> is disabled.
   **
   ** @param  currentAccountsDS  the {@link DataSet} containing the accounts
   **                            currently provisioned.
   **
   ** @return
   **
   ** @throws tcDataSetException if anything went wrong
   */
  private HashMap<String, AccountChange> getDisabledAccounts(tcDataSet currentAccountsDS)
    throws tcDataSetException {

    HashMap<String, AccountChange> disabledAccounts = new HashMap<String, AccountChange>();
    for (String objectKey : allowedObjects.keySet()) {
      String oiuKey = getDisabledAccountInstance(currentAccountsDS, objectKey);
      if (oiuKey != null) {
        disabledAccounts.put(oiuKey, null);
      }
    }
    return disabledAccounts;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountsThatNoLongerApplyMarkedForRevoke
  /**
   ** Revoke any objects which are neither in the Allow or Deny list. Before a
   ** revoking the object, check to see if it is policy based and the flag
   ** "OIU_POLICY_REVOKE", otherwise knows as "revoke-if-no-longer-applies" is
   ** true.
   **
   ** @param  currentAccountsDS  the {@link DataSet} containing the accounts
   **                            which are already provisioned for this user.
   **
   ** @return
   **
   ** @throws tcDataSetException if anything went wrong
   */
  private HashMap<String, AccountChange> getAccountsThatNoLongerApplyMarkedForRevoke(tcDataSet currentAccountsDS)
    throws tcDataSetException {

    HashMap<String, AccountChange> inapplicableAccounts = new HashMap<String, AccountChange>();
    for (int i = 0; i < currentAccountsDS.getRowCount(); i++) {
      currentAccountsDS.goToRow(i);
      String userAccountKey = currentAccountsDS.getString(USER_ACCOUNT_KEY); //OIU_KEY
      String objectKey      = currentAccountsDS.getString(OBJECT_KEY).trim();
      if ((!this.deniedObjects.containsKey(objectKey)) && (!this.allowedObjects.containsKey(objectKey)) && currentAccountsDS.getString(POLICY_ALLOWED_USER_ACCOUNT).equals("1") && currentAccountsDS.getString(OBJACTION_INAPPLICABLE_ACCOUNT).equals(REVOKE) && (!currentAccountsDS.getString(ACCOUNT_TYPE).equalsIgnoreCase(Account.ACCOUNT_TYPE.ServiceAccount.getId()))) {
        inapplicableAccounts.put(userAccountKey, null);
      }
    }
    return inapplicableAccounts;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountsThatNoLongerApplyMarkedForDisable
  /**
   ** Return list of accounts which satisfy Disable-if-no-longer-applies.
   **
   ** @param  currentAccountsDS  the objects which are already provisioned for
   **                            this user.
   **
   ** @throws tcDataSetException if something isn't accessible in the given
   **                            {@link tcDataSet}.
   */
  private HashMap<String, AccountChange> getAccountsThatNoLongerApplyMarkedForDisable(tcDataSet currentAccountsDS)
    throws tcDataSetException {

    HashMap<String, AccountChange> inapplicableAccounts = new HashMap<String, AccountChange>();
    for (int i = 0; i < currentAccountsDS.getRowCount(); i++) {
      currentAccountsDS.goToRow(i);
      String userAccountKey = currentAccountsDS.getString(USER_ACCOUNT_KEY); //OIU_KEY
      String objectKey = currentAccountsDS.getString(OBJECT_KEY).trim();

      if ((!this.deniedObjects.containsKey(objectKey)) && (!this.allowedObjects.containsKey(objectKey)) && currentAccountsDS.getString(POLICY_ALLOWED_USER_ACCOUNT).equals("1") && currentAccountsDS.getString(OBJACTION_INAPPLICABLE_ACCOUNT).equals(DISABLE) && (!currentAccountsDS.getString(ACCOUNT_TYPE).equalsIgnoreCase(Account.ACCOUNT_TYPE.ServiceAccount.getId()))) {
        inapplicableAccounts.put(userAccountKey, null);
      }
    }
    return inapplicableAccounts;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   consolidateAccountProfileChanges
  /**
   ** Consolidate the profile changes
   **
   ** @param  accountProfileChanges the chnages discovered so far.
   */
  protected void consolidateAccountProfileChanges(AccountProfileChanges accountProfileChanges) {
    // get accounts to modify
    HashMap<String, AccountChange> accountsToModify = accountProfileChanges.getAccountsToModify();
    Set<String> accountsToModifyKeySet = new HashSet<String>();
    accountsToModifyKeySet.addAll(accountsToModify.keySet());

    // get accounts to revoke
    HashMap<String, AccountChange> accountsToRevoke = accountProfileChanges.getAccountsToRevoke();
    Set<String> accountsToRevokeKeySet = new HashSet<String>();
    accountsToRevokeKeySet.addAll(accountsToRevoke.keySet());

    // get accounts to disable
    HashMap<String, AccountChange> accountsToDisable = accountProfileChanges.getAccountsToDisable();
    Set<String> accountsToDisableKeySet = new HashSet<String>();
    accountsToDisableKeySet.addAll(accountsToDisable.keySet());

    // get accounts to enable
    HashMap<String, AccountChange> accountsToEnable = accountProfileChanges.getAccountsToEnable();
    Set<String> accountsToEnableKeySet = new HashSet<String>();
    accountsToEnableKeySet.addAll(accountsToEnable.keySet());

    // identify accounts that are to be modified and revoked
    // update the accountsToRevoke list with the appropriate changes to be made
    // to the account and remove these accounts from the accountsToModify list
    accountsToRevokeKeySet.retainAll(accountsToModifyKeySet);

    for (String userAccountKey  : accountsToRevokeKeySet) {
      accountsToRevoke.put(userAccountKey, accountsToModify.get(userAccountKey));
      accountsToModify.remove(userAccountKey);
    }

    // identify accounts that are to be modified and disabled
    // update the accountsToDisable list with appropriate changes to the account
    // and remove the accounts from the accountsToModify list
    accountsToDisableKeySet.retainAll(accountsToModifyKeySet);
    for (String userAccountKey : accountsToDisableKeySet) {
      accountsToDisable.put(userAccountKey, accountsToModify.get(userAccountKey));
      accountsToModify.remove(userAccountKey);
    }

    // identify accounts that are to be modified and disabled
    // update the accountsToDisable list with appropriate changes to the account
    // and remove the accounts from the accountsToModify list
    accountsToEnableKeySet.retainAll(accountsToModifyKeySet);
    for (String userAccountKey : accountsToEnableKeySet) {
      accountsToEnable.put(userAccountKey, accountsToModify.get(userAccountKey));
      accountsToModify.remove(userAccountKey);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountsToModify
  /**
   *
   * @param userKey
   * @param currentAccountsDS
   * @param inapplicablePolicies
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   */
  private HashMap<String, AccountChange> getAccountsToModify(String userKey, tcDataSet currentAccountsDS, Map<String, String> inapplicablePolicies)
    throws tcDataSetException
    ,      tcDataAccessException {

    LOGGER.log(Level.FINE, "#### START: SingleAccountEvaluationUtility.getAccountsToModify(): for userKey: " + userKey); //Moving log level from info to debug
    HashMap<String, AccountChange> accountsToModify = new HashMap<String, AccountChange>();
    // identify accounts that are associated with the policies that do not apply
    // any longer. Any child records added because of these inapplicable
    // policies need to be removed
    // sdey - need to check if we need to do something to undo ilao changes for
    // entitlement
    LOGGER.log(Level.FINE, "SingleAccountEvaluationUtility.getAccountsToModify():for userKey: " + userKey + " inapplicablePolicies: " + printMap(inapplicablePolicies)); //Moving log level from info to debug
    compileChildDataChangesRelatedToInapplicablePolicies(userKey, inapplicablePolicies, accountsToModify);

    for (int i = 0; i < currentAccountsDS.getRowCount(); i++) {
      currentAccountsDS.goToRow(i);
      String objectKey = currentAccountsDS.getString(OBJECT_KEY);
      LOGGER.log(Level.FINE, "SingleAccountEvaluationUtility.getAccountsToModify():for userKey: " + userKey + " evaluating for objectKey: " + objectKey); //Moving log level from info to debug

      if (isObjectAllowed(objectKey)) {
        // retrieve attributes of the object that is to be provisioned and
        // properties of the highest priority policy that allows this object
        // sdey - is this the highest-level policy associated wiht allowed
        // object ?
        PolicyObjectDetails objectPolicyProperties = this.allowedObjects.get(objectKey);
        String              objActionInapplicable  = objectPolicyProperties.getObjActionInapplicable();
        String              policyKey              = objectPolicyProperties.getPolicyKey();
        String              policyRetrofit         = objectPolicyProperties.getRetrofit();
        String              policyName             = objectPolicyProperties.getPolicyName();

        String              currentPolicyKey       = currentAccountsDS.getString(POLICY_KEY);
        String              accountKey             = currentAccountsDS.getString(ACCOUNT_KEY);
        String              objectInstanceKey      = currentAccountsDS.getString(OBJECT_INSTANCE_KEY);
        String              userAccountKey         = currentAccountsDS.getString(USER_ACCOUNT_KEY);

        AccountChange accountChange = getAccountChange(accountKey, userAccountKey, accountsToModify);
        if (isPolicyAllowedUserAccount(currentAccountsDS)) {
          // update revoke-if-inapplicable flag for this user account if it has
          // changed
          // sdey - update to rnla or dnla as appropriate - q when to decide to
          // update this flag to rnla - if already dnla
          if (isObjActionInapplicableUpdated(currentAccountsDS, objActionInapplicable)) {
            accountChange.addUserAccount(objectInstanceKey, userAccountKey, objectKey, userKey, policyKey);
            accountChange.addUserAccountChange(OBJACTION_INAPPLICABLE_ACCOUNT, objActionInapplicable);

            // update the provisioned object data set revoke flag to
            // reflect the above changes
            currentAccountsDS.setString(OBJACTION_INAPPLICABLE_ACCOUNT, objActionInapplicable);
          }
        }

        // if ORC_KEY is empty then provisioning has been instantiated but the
        // resource is in Ready state. This happens when the Object form is set
        // to not auto-save and form is not saved yet
        if (!isNull(accountKey)) {
          // if object was provisioned because of a policy and that policy is no
          // longer the highest priority policy then update form data
          if (!isNullOrEmpty(currentPolicyKey) && !currentPolicyKey.equalsIgnoreCase(policyKey)) {
            // update user account in accountDelta (OIU) with the new applicable
            // policy information
            if (!accountChange.hasUserAccount()) {
              accountChange.addUserAccount(objectInstanceKey, userAccountKey, objectKey, userKey, policyKey);
            }
            accountChange.addUserAccountChange(POLICY_KEY, policyKey);
            // update the new policy information in the currentAccountsDS
            currentAccountsDS.setString(POLICY_KEY, policyKey);
          }

          boolean currentPolicyKeyNull       = isNullOrEmpty(currentPolicyKey); // checks if account has policy associated to it.
          boolean harvestingSupported        = isAPHarvestingSupported(); // checks if XL.AllowAPHarvesting = true
          boolean accountDiscrDataAvailable  = true; // check for AP Default values for policy discriminator
          boolean harvestableProvByMechanism = isHarvestableUserAccountBasedOnProvMechanism(currentAccountsDS); // considers only RECONILED/BULK LOADED Accounts
          boolean retrofiPolicy              = "1".equals(policyRetrofit); // considers only policy that are marked Retrofit = true

          Serializable[] params = new Serializable[] { policyName, userKey, userAccountKey };
          if (currentPolicyKeyNull && retrofiPolicy && harvestingSupported && accountDiscrDataAvailable && harvestableProvByMechanism) {
            LOGGER.log(Level.WARNING, "IAM-40302013", params);
            if (!accountChange.hasUserAccount()) {
              accountChange.addUserAccount(objectInstanceKey, userAccountKey, objectKey, userKey, policyKey);
            }
            accountChange.addUserAccountChange(POLICY_KEY, policyKey);
            accountChange.addUserAccountChange(OBJACTION_INAPPLICABLE_ACCOUNT, objActionInapplicable);
            accountChange.addUserAccountChange(POLICY_ALLOWED_USER_ACCOUNT, "1");
            accountChange.addUserAccountChange(OIU_PROV_MECHANISM, OIU_PROV_MECHANISM_APHARVESTED);
            // mark AccountChange as policy harvestable, this is referenced
            // later while compiling parent and child data.
            accountChange.setPolicyHavestable(true);
            // update the new policy information in the currentAccountsDS
            currentAccountsDS.setString(POLICY_KEY,                     policyKey);
            currentAccountsDS.setString(OBJACTION_INAPPLICABLE_ACCOUNT, objActionInapplicable);
            currentAccountsDS.setString(POLICY_ALLOWED_USER_ACCOUNT,    "1");
            currentAccountsDS.setString(OIU_PROV_MECHANISM,             OIU_PROV_MECHANISM_APHARVESTED);
          }
          else {
            LOGGER.log(Level.WARNING, "IAM-4030207", params);
            if (!currentPolicyKeyNull) {
              params = new Serializable[] { userAccountKey, userKey, currentPolicyKey };
              LOGGER.log(Level.WARNING, "IAM-4030208", params);
            }
            else if (!harvestingSupported) {
              params = new Serializable[] { ALLOW_AP_HARVESTING, String.valueOf(harvestingSupported) };
              LOGGER.log(Level.WARNING, "IAM-4030209", params);
            }
            else if (!accountDiscrDataAvailable) {
              params = new Serializable[] { policyKey, policyName };
              LOGGER.log(Level.WARNING, "IAM-40302010", params);
            }
            else if (!harvestableProvByMechanism) {
              params = new Serializable[] { userAccountKey, userKey, currentAccountsDS.getString(OIU_PROV_MECHANISM) };
              LOGGER.log(Level.WARNING, "IAM-40302011", params);
            }
            else if (!retrofiPolicy) {
              params = new Serializable[] { policyKey, policyName, String.valueOf(retrofiPolicy), userAccountKey, userKey };
              LOGGER.log(Level.WARNING, "IAM-40302012", params);
            }
          }
          // get Access Policy Defaults for Parent Form
          tcDataSet parentPolicyDefaultsDS = getParentPolicyDefaults(policyKey, objectKey);
          // Policy has default values --> this is existing condition
          // New condition --> Current policy is null, i.e not policy based
          // provisioned && harvesting is not applicable due to some other
          // flags, then don't proceed with compilation of parent data.
          if (hasParentPolicyDefaults(parentPolicyDefaultsDS) && !(currentPolicyKeyNull && !accountChange.isPolicyHavestable())) {
            // check if parent data is been modified
            String    formName = parentPolicyDefaultsDS.getString(FORM_NAME);
            tcDataSet provisionedDataDS = AccessPolicyUtil.getProvisionedDataForPolicBasedAccount(formName, accountKey, policyKey);
            boolean   parentDataChanged = AccessPolicyUtil.compareParentDataWithProvisionedData(parentPolicyDefaultsDS, provisionedDataDS);
            if (parentDataChanged) {
              compileParentDataChanges(accountKey, currentPolicyKey, policyKey, accountChange, parentPolicyDefaultsDS);
            }
          }
          if (accountChange.isPolicyHavestable() || (isProvMechanismPolicyHarvested(currentAccountsDS) && currentPolicyKey != null)) {
            accountChange.setChildPolicyHavestable(true);
          }
          LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.getAccountsToModify():for userKey: " + userKey + " now compiling child data changes for :objectKey: " + objectKey + " accountKey: " + accountKey + " \n" + " accountChange: " + accountChange);
          compileChildDataChangesOnly(objectKey, accountKey, accountChange);
        }
        setAccountChange(accountChange, accountsToModify);
      }
    }
    return accountsToModify;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisabledAccountInstance
  /**
   *
   * @param currentAccountsDS
   * @param objectKey
   * @return oiu_key if account is disabled - else return null
   * @throws tcDataSetException
   * @throws Exception
   */
  private String getDisabledAccountInstance(tcDataSet currentAccountsDS, String objectKey)
    throws tcDataSetException {

    for (int i = 0; i < currentAccountsDS.getRowCount(); i++) {
      currentAccountsDS.goToRow(i);
      if (currentAccountsDS.getString(OBJECT_KEY).trim().equals(objectKey) && currentAccountsDS.getString(OBJECT_STATUS).equals(DISABLED)) {
        return currentAccountsDS.getString(USER_ACCOUNT_KEY).trim();
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   useDirectProvisioning
  /**
   ** Determines if direct provisioning is applicable to the
   ** <code>Resource Object</code> specified by the given
   ** <code>objectKey</code>.
   **
   ** @param  objectKey          the identifier of the
   **                            <code>Resource Object</code> to verify.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Resource Object</code> hs to be
   **                            provisioned directly; <code>false</code>
   **                            otherwise which leads to create a request.
   **
   ** @throws AccessPolicyEvaluationException
   */
  private boolean useDirectProvisioning(final String objectKey)
    throws AccessPolicyEvaluationException {

    if (this.provisioningOptions.containsKey(objectKey)) {
      Vector<String> provisioningOptionsForObject = this.provisioningOptions.get(objectKey);
      boolean        requestProvisioning          = false;
      boolean        directProvisioning           = false;
      // FOLLOW UP: Is tcServerProperties the correct way to get XL.DirectProvision?
      if (provisioningOptionsForObject.contains("R")) {
        requestProvisioning = true;
      }
      if (provisioningOptionsForObject.contains("D")) {
        directProvisioning = true;
      }
      if (!directProvisioning) {
        return false;
      }
      if (!requestProvisioning) {
        return true;
      }

//      boolean systemDirectProvision = (new Boolean(tcServerProperties.getValue("XL.DirectProvision", "TRUE"))).booleanValue();
      boolean systemDirectProvision = true;
      if (systemDirectProvision == true) {
        return true;
      }
      else {
        return false;
      }
    }
    else {
      LOGGER.log(Level.SEVERE, "IAM-4030053", objectKey);
      throw new AccessPolicyEvaluationException("IAM-4030053");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compileParentDataChanges
  /**
   *
   * @param accountKey
   * @param currentPolicyKey
   * @param policyKey
   * @param accountChange
   * @param parentPolicyDefaultsDS
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   */
  private void compileParentDataChanges(String accountKey, String currentPolicyKey, String policyKey, AccountChange accountChange, tcDataSet parentPolicyDefaultsDS)
    throws tcDataSetException, tcDataAccessException {
    // FOLLOW UP: Is this collection for different parent forms really needed?
    // Determine when would there be more than one parent form
    HashMap<String, HashMap<String, String>> parentFormsWithData = new HashMap<String, HashMap<String, String>>();

    // Blank out all fields
    /* Commented out the following block because blanking out all fields would
           mean that data that was added by alternative sources like prepopulate
           adapter or manual additions would also be blanked out.

           Commenting out the following block would also mean that some parent form
           fields could potentially have residual data that was set by the previous
           high priority policy. That is, fields for which the previous high priority
           policy had defaults while the new high priority policy doesn't have any
           defaults for.
         */
    /*PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_PARENT_FORM_DEFINITION"));
    pstmt.setString(1, accountKey);
    pstmt.execute();
    tcDataSet parentFormDefinitionDS = pstmt.getDataSet();

        for (int j = 0; j < parentFormDefinitionDS.getRowCount(); j++) {
          parentFormDefinitionDS.goToRow(j);

            HashMap<String, String> parentData = null;
            String formName = parentFormDefinitionDS.getString(FORM_NAME);

            if (parentFormsWithData.containsKey(formName)) {
              parentData = parentFormsWithData.get(formName);
            } else {
                parentData = new HashMap<String, String>();
                parentFormsWithData.put(formName, parentData);
                parentData.put(FORM_KEY, parentFormDefinitionDS.getString(FORM_KEY));
            }
          parentData.put(parentFormDefinitionDS.getString(FORM_FIELD_NAME),"");
        }*/

    // Prepare policy defaults for parent form fields from the definition
    // of the new high priority policy
    for (int j = 0; j < parentPolicyDefaultsDS.getRowCount(); j++) {
      parentPolicyDefaultsDS.goToRow(j);
      // Access Policy defaults for Parent Form - POF entry
      HashMap<String, String> parentData = null;
      String                  formName = parentPolicyDefaultsDS.getString(FORM_NAME);

      if (parentFormsWithData.containsKey(formName)) {
        parentData = parentFormsWithData.get(formName);
      }
      else {
        parentData = new HashMap<String, String>();
        parentFormsWithData.put(formName, parentData);
        parentData.put(FORM_KEY, parentPolicyDefaultsDS.getString(FORM_KEY));
      }
      parentData.put(parentPolicyDefaultsDS.getString(PARENT_FORM_FIELD_NAME), parentPolicyDefaultsDS.getString(PARENT_FORM_FIELD_VALUE));
    }

    // Compile parent data changes for each form
    // FOLLOW UP: There shouldn't be more than one form so all this code
    // needs to be removed.
    Iterator<String> parentForms = parentFormsWithData.keySet().iterator();

    while (parentForms.hasNext()) {
      String[] foreignKeyNames = null;
      String[] foreignKeyValues = null;

      String parentFormName = parentForms.next();

      String parentFormKeyName     = getFormKeyName(parentFormName);
//      String parentFormVersionName = getFormVersionName(parentFormName);

      HashMap<String, String> parentData = parentFormsWithData.get(parentFormName);
      String                  formKey    = parentData.get(FORM_KEY);

      tcDataSet currentFormData = readFormData(parentFormName, accountKey, currentPolicyKey);

      // Get details about the version of the form that was used to provision the
      // resource. Policy defaults for only these fields need to be gathered.

      // If there is no current data in the form for this account then it means that
      // the parent data has not been saved. This will happen when the object hasn't
      // been saved yet.
      // FOLLOW UP: Couldn't absence of data in the form for this account mean
      // something else. For instance, if the resource was provisioned because of a
      // policy and that policy didn't have any defaults?
      // FOLLOW UP: Shouldn't we update the form data even if there isn't any data
      // already?

      //Parent form fields will be retrieved based on the active version of the parent form.
      //The below query will return the parent form fields associated with the latest version of the parent form
      if (currentFormData.getRowCount() != 0) {

        PreparedStatementUtil pstmt = new PreparedStatementUtil();
        pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_VERSION_SPECIFIC_FORM_FIELDS_DETAILS"));
        pstmt.setString(1, formKey);
        pstmt.execute();
        tcDataSet formFieldsDetailsDS = pstmt.getDataSet();

        // Compile parent data changes
        accountChange.addParentData(parentFormName, parentFormKeyName, currentFormData.getString(parentFormKeyName), foreignKeyNames, foreignKeyValues, policyKey);

        // for every column in the same version as the
        // UD table, update that column
        for (int j = 0; j < formFieldsDetailsDS.getRowCount(); j++) {
          formFieldsDetailsDS.goToRow(j);

          String formFieldName = formFieldsDetailsDS.getString(FORM_FIELD_NAME);
          String formFieldType = formFieldsDetailsDS.getString(FORM_FIELD_TYPE);

          if (parentData.containsKey(formFieldName)) {
            //Date field
            if (formFieldType.equalsIgnoreCase(DATE)) {
              accountChange.addParentDataChange(formFieldName, Timestamp.valueOf(parentData.get(formFieldName)));
            }
            else {
              accountChange.addParentDataChange(formFieldName, parentData.get(formFieldName));
            }
          }
        }
        accountChange.addParentDataChange(POLICY_KEY, policyKey);
      }
    }
    return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compileChildDataChangesOnly
  private void compileChildDataChangesOnly(String objectKey, String accountKey, AccountChange accountChange)
    throws tcDataSetException
    ,      tcDataAccessException {

    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesOnly():accountKey: " + accountKey + " START");
    if (!AccessPolicyUtil.isServiceAccount(accountKey)) {
      LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesOnly():accountKey: " + accountKey + " is not a service account.");

      PreparedStatementUtil pstmt = new PreparedStatementUtil();
      pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_ACCOUNT_SPECIFIC_FORM_DETAILS"));
      pstmt.setString(1, accountKey);
      pstmt.execute();
      tcDataSet parentFormDetailsDS = pstmt.getDataSet();

      String parentFormName = parentFormDetailsDS.getString(FORM_NAME);
      if (!isNullOrEmpty(parentFormName)) {
        String parentDataQuery = "SELECT " + getFormKeyName(parentFormName) + " FROM " + parentFormName + " WHERE ORC_KEY = ?";
        LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesOnly(): execute parentDataQuery: " + parentDataQuery);

        pstmt = new PreparedStatementUtil();
        pstmt.setStatement(AccessPolicyUtil.getDatabase(), parentDataQuery);
        pstmt.setString(1, accountKey);
        pstmt.execute();
        tcDataSet parentDataDS = pstmt.getDataSet();

        // check if parent data already exists for this form
        if (parentDataDS.getRowCount() > 0) {
          LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesOnly(): parent data exists, so compute child data for all applicable policies.");
          Enumeration<String> allApplicablePolicies = this.policyProfile.keys();
          LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesOnly(): allApplicablePolicies: " + allApplicablePolicies);
          while (allApplicablePolicies.hasMoreElements()) {
            String policyKey = allApplicablePolicies.nextElement();
            LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesOnly(): compiling child data for " + "policyKey: " + policyKey + " accountKey: " + accountKey + " objectKey: " + objectKey + " parentFormName: " + parentFormName + " accountChange: " + accountChange);
            //compileChildDataChanges(policyKey, accountKey, objectKey, accountChange);
            //bug fix. #8755133
            compileChildDataChangesForPolicy(policyKey, accountKey, objectKey, parentFormName, accountChange);
          }
        }
      }
    }
    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesOnly():accountKey: " + accountKey + " END");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compileChildDataChangesForPolicy
  private void compileChildDataChangesForPolicy(String policyKey, String accountKey, String objectKey, String parentSdkName, AccountChange accountChange)
    throws tcDataAccessException
    ,      tcDataSetException {

    // modifying the implementation of this method to be more performant when
    // policy definition has lots of entitlements and has been modified (or
    // retrofit).
    // the implementation is performant only if recon field mapping key fields
    // are defined for entitlement fields.
    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesForPolicy():accountKey: " + accountKey + " policyKey:" + policyKey + " START");
    // get the child form information based on pol_key, obj_key and
    // parentSdkName from POC
    tcDataSet childFormDetailsDS = AccessPolicyUtil.getChildFormDetails(policyKey, objectKey, parentSdkName);

    // get the value that has to be populated in the _REVOKE column of the child
    // form
    String revokeInapplicable = AccessPolicyUtil.getRevokeInapplicable(policyKey, objectKey);

    // find the reconciliation field mapping for the child forms associated to a
    // given policy.
    Map<String, Set<String>> reconFieldAttrs = AccessPolicyUtil.getEntitlementMatchingAttributes(childFormDetailsDS);
    accountChange.addEntitlementMatchingAttributes(reconFieldAttrs);
    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesForPolicy():ReconFieldAttrs is " + reconFieldAttrs);

    for (int i = 0; i < childFormDetailsDS.getRowCount(); i++) {
      childFormDetailsDS.goToRow(i);
      String chdFormName          = childFormDetailsDS.getString(FORM_NAME);
      String parentFormName       = childFormDetailsDS.getString(PARENT_FORM_NAME);
      String parentFormPrimaryKey = childFormDetailsDS.getString("PARENT_SDK_KEY");
      String chdFormKey           = childFormDetailsDS.getString(FORM_KEY);
      String childFormVersion     = childFormDetailsDS.getString(FORM_FIELD_VERSION);
      if (!reconFieldAttrs.isEmpty() && reconFieldAttrs.containsKey(chdFormName) && !reconFieldAttrs.get(chdFormName).isEmpty()) {
        /*if recon key fields are defined then construct queries to find out child table records to be
            * a) added
            * b) updated
            * c) deleted
           */
        HashMap<String, StringBuffer> existingRecordInfo = new HashMap<String, StringBuffer>();
        String                        pocViewName   = "POC_NORM1";
        Set<String>                   keyAttributes = reconFieldAttrs.get(chdFormName);

        LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesForPolicy():Recon key fields for child form " + chdFormName + " and with childFormKey: " + chdFormKey + " is " + keyAttributes);
        // get all the distinct form field names for a given child form from POC
        ArrayList<String> childFormFieldsinPOC = AccessPolicyUtil.getChildFormFieldsinPOC(policyKey, objectKey, chdFormKey, parentFormPrimaryKey);
        // get the childFormFieldsinPOC as a set to compare with keyAttributes
        // set
        Set<String> childFormFieldsinPOCAsSet = new HashSet<String>(childFormFieldsinPOC);
        // check if all the keyAttributes have default data defined in the
        // policy definition
        if (!childFormFieldsinPOCAsSet.containsAll(keyAttributes)) {
          // if a key attributes does not have access policy default default
          // data defined, then ignore the attribute(s) while making
          // comparisions with the UD table
          // keyAttributes.retainAll(childFormFieldsinPOCAsSet) will update
          // keyAttributes with only those elements that are contained in both
          // the sets. So keyAttributes will be updated with only those key
          // fields that have policy defaults defined.
          keyAttributes.retainAll(childFormFieldsinPOCAsSet);
        }
        // construct a query to retrieve the entries in POC in a horizontal
        // format
        String childFormKeyColumnsToRetrieve = AccessPolicyUtil.getFormFieldNameToRetrieveFromPOC(pocViewName, childFormFieldsinPOC);
        // policyChildDefaults gives an inline view of the data from poc for a
        // given child table
        StringBuffer policyChildDefaults = AccessPolicyUtil.getInLineViewOfPOCData(pocViewName, childFormFieldsinPOC);
        // policyChildDefaults now contains the query to convert the entries in
        // POC to a horizontal view for a given policy key, object key and
        // parent and child form keys

        // FIND THE RECORDS TO BE ADDED : get the list of records in poc which
        // are not in the UD table. Use the policyChildDefaults query to find
        // entries which does not exist in the UD table for a given orc_key
        StringBuffer modifiedChildDataQuery = AccessPolicyUtil.getQueryToFindRecordsToBeAdded(chdFormName, pocViewName, policyChildDefaults, childFormKeyColumnsToRetrieve, keyAttributes.toArray());
        LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesForPolicy(): Query to find records to be added is " + modifiedChildDataQuery);
        // update account change VO with the records to be added.
        AccessPolicyUtil.updateAccountChangeWithChildDataToBeAdded(policyKey, objectKey, accountKey, accountChange, revokeInapplicable, chdFormName, parentFormPrimaryKey, chdFormKey, childFormVersion, childFormFieldsinPOC, modifiedChildDataQuery, parentFormName);

        // FIND THE RECORDS TO BE MODIFIED: get the records that exist in both the tables POC and UD
        // Build the query to be executed
        String       childFormPrimaryKeyName = getFormKeyName(chdFormName);
        StringBuffer queryToRetrieveCommonRecords = AccessPolicyUtil.getQueryToFindCommonToBeModified(chdFormName, pocViewName, policyChildDefaults, childFormKeyColumnsToRetrieve, keyAttributes.toArray(), childFormPrimaryKeyName);
        LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesForPolicy(): Query to find records to be updated is " + queryToRetrieveCommonRecords);
        // queryToRetrieveCommonRecords will contain the query that will return
        // the common records between POC and UD.
        // Use the query to modify existing child table data
        AccessPolicyUtil.updateAccountChangeWithRecordsToBeModified(policyKey, accountKey, objectKey, parentSdkName, accountChange, revokeInapplicable, chdFormName, parentFormPrimaryKey, chdFormKey, childFormVersion, existingRecordInfo, childFormFieldsinPOC, childFormPrimaryKeyName, queryToRetrieveCommonRecords, parentFormName); //end of loop that compares common records
        // identify & remove child records from all child forms which were
        // previously added by the same policy but are no longer applicable
        // (possibly because the definition of policy changed)
        LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChangesForPolicy(): Child table keys of child data to be deleted is " + existingRecordInfo);
        //FIND RECORDS TO BE DELETED
        deleteSpecificChildData(policyKey, accountKey, objectKey, accountChange, existingRecordInfo, Long.valueOf(chdFormKey));
      }
      // if recon fields are not defined, then revert to the method that was
      // being called earlier - compileChildDataChanges()
      else {
        compileChildDataChanges(policyKey, accountKey, objectKey, parentSdkName, accountChange);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compileChildDataChanges
  /**
   *
   * @param policyKey
   * @param accountKey
   * @param objectKey
   * @param accountChange
   * @return
   * @throws tcDataAccessException
   * @throws tcDataSetException
   */
  private void compileChildDataChanges(String policyKey, String accountKey, String objectKey, String parentSdkName, AccountChange accountChange)
    throws tcDataAccessException
    ,      tcDataSetException {

    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges():accountKey: " + accountKey + " policyKey:" + policyKey + " START");
    HashMap<String, StringBuffer> existingRecords = new HashMap<String, StringBuffer>();
    HashMap<String, StringBuffer> recordsToBeRemoved = new HashMap<String, StringBuffer>();
    PreparedStatementUtil         pstmt = null;

    // Read default child data for all child forms for the given object & policy (from POC)
    // Bug 22150399 - caching the two queries below to improve performance.
    String childDataChangesQuery = childDataChangesSubQuery + " AND POC.POL_KEY=" + policyKey + " AND POC.OBJ_KEY=" + objectKey + " AND PARENT_SDK.SDK_NAME='" + parentSdkName + "'" + " ORDER BY SDC.SDK_KEY, POC.POC_RECORD_NUMBER";
    tcDataSet childPolicyDefaultsDS = CacheUtil.getSetCachedQuery(AccessPolicyUtil.getDatabase(), childDataChangesQuery, "PolicyEvaluation.cdc." + policyKey + "." + objectKey + "." + parentSdkName, CacheUtil.ACCESS_POLICIES);

    String objectsAssociatedWithPolicyQuery = "SELECT OBJ.OBJ_KEY, OBJ.OBJ_NAME, OBJ.OBJ_ALLOW_MULTIPLE," + " OBJ.OBJ_ALLOWALL, POP.POP_DENIAL, POP.POP_REVOKE_OBJECT" + " FROM POP POP, OBJ OBJ WHERE POP.POL_KEY=" + policyKey + " AND POP.OBJ_KEY = OBJ.OBJ_KEY";
    tcDataSet associatedObjectsDS = CacheUtil.getSetCachedQuery(AccessPolicyUtil.getDatabase(), objectsAssociatedWithPolicyQuery, "PolicyEvaluation.oawp." + policyKey, CacheUtil.ACCESS_POLICIES);

    associatedObjectsDS.goToRow(0);
    String revokeInapplicable = associatedObjectsDS.getString(OBJACTION_INAPPLICABLE);
    if (childPolicyDefaultsDS.getRowCount() == 0) {
      LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges(): there is no default child data for policyKey: " + policyKey + " accountKey:" + accountKey + " objectKey:  " + objectKey);
      // There is no default child data for the given object & policy
      // So, delete any data stored in the child forms for this
      // object & policy (as this data has now become obsolete)
      deleteChildData(policyKey, accountKey, objectKey, accountChange);
      LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges(): After deleteChildData() for policyKey: " + policyKey + " accountKey:" + accountKey + " objectKey:  " + objectKey);
    }
    else if (childPolicyDefaultsDS.getRowCount() > 0) {
      String                  lastChildFormName = "";
      String                  lastChildRecordNumber = "";
      StringBuffer            checkDuplicateChildRecordFilter = new StringBuffer();
      HashMap<String, Object> modifiedChildRecord = new HashMap<String, Object>();
      HashMap<String, Object> modifiedChildData = new HashMap<String, Object>();

      Map<String, Set<String>> entitlementMatchingAttributes = AccessPolicyUtil.getEntitlementMatchingAttributes(childPolicyDefaultsDS);
      accountChange.addEntitlementMatchingAttributes(entitlementMatchingAttributes);
      for (int j = 0; j < childPolicyDefaultsDS.getRowCount(); j++) {
        childPolicyDefaultsDS.goToRow(j);

        String  parentFormName = childPolicyDefaultsDS.getString(PARENT_FORM_NAME);
        String  parentFormPrimaryKeyName = getFormKeyName(parentFormName);
        String  childFormName = childPolicyDefaultsDS.getString(FORM_NAME);
        String  childRecordNumber = childPolicyDefaultsDS.getString(CHILD_FORM_RECORD_NUMBER);
        boolean matchingAttribute = isEntitlementMatchingAttribute(entitlementMatchingAttributes, childFormName, childPolicyDefaultsDS.getString(FORM_FIELD_NAME));

        // Child default data is stored in POC which is a vertical table. So, a single logical
        // child form record is stored as many records in POC. childPolicyDefaults has data in the
        // same vertical structure.
        if (!childFormName.equalsIgnoreCase(lastChildFormName) || !childRecordNumber.equalsIgnoreCase(lastChildRecordNumber)) {
          // When the childFormName or childFormRecordNumber changes it marks the end of a logical
          // record. All the field names & values that are part of this logical record are collected
          // in modifiedChildRecord. So, if it is not null then it stored in accountDelta if it
          // represents a new record. Then modifiedChildRecord is reset to start collecting data
          // that pertains to the next logical child record from the policy definition.
          String childFormVersionName = getFormVersionName(childFormName);

          /*if (childFormName.equalsIgnoreCase(lastChildFormName)
              && !childFormRecordNumber.equalsIgnoreCase(lastChildRecordNum)
              && !modifiedChildRecord.isEmpty()) {*/
          if (!modifiedChildRecord.isEmpty()) {
            boolean      hasDuplicateChildRecord = false;
            boolean      hasRevokeFlagUpdated = false;
            String       lastChildFormPrimaryKeyName = getFormKeyName(lastChildFormName);
            String       lastChildFormRevokeColName = getFormRevokeName(lastChildFormName);
            StringBuffer checkDuplicateChildRecordQuery = new StringBuffer();
            checkDuplicateChildRecordQuery.append("SELECT ");
            checkDuplicateChildRecordQuery.append(lastChildFormPrimaryKeyName);
            checkDuplicateChildRecordQuery.append(" , ");
            checkDuplicateChildRecordQuery.append(lastChildFormRevokeColName);
            checkDuplicateChildRecordQuery.append(" , ");
            checkDuplicateChildRecordQuery.append("POL_KEY");
            checkDuplicateChildRecordQuery.append(" FROM ");
            checkDuplicateChildRecordQuery.append(lastChildFormName);
            checkDuplicateChildRecordQuery.append(" WHERE ORC_KEY = ?");
            checkDuplicateChildRecordQuery.append(checkDuplicateChildRecordFilter.toString());
            modifiedChildRecord.put(getFormRevokeName(lastChildFormName), revokeInapplicable);
            LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges(): checkDuplicateChildRecordQuery SQL: " + checkDuplicateChildRecordQuery.toString());
            pstmt = new PreparedStatementUtil();
            pstmt.setStatement(AccessPolicyUtil.getDatabase(), checkDuplicateChildRecordQuery.toString());
            pstmt.setString(1, accountKey);
            pstmt.execute();
            tcDataSet checkDuplicateChildRecordDS = pstmt.getDataSet();
            String    existingRecordKey = null;

            if (checkDuplicateChildRecordDS.getRowCount() == 0) {
              if (isAddable(accountChange, modifiedChildRecord, lastChildFormName, revokeInapplicable)) {
                accountChange.addChildDataChanges(lastChildFormName, lastChildFormPrimaryKeyName, "", new String[] { ACCOUNT_KEY }, new String[] { accountKey }, lastChildRecordNumber, ChildTableRecord.ACTION.Add, modifiedChildRecord, policyKey);
              }
            }
            else {
              hasDuplicateChildRecord = true;
              checkDuplicateChildRecordDS.goToRow(0);
              existingRecordKey = checkDuplicateChildRecordDS.getString(lastChildFormPrimaryKeyName);
              int indexOfDataMarkedForDelete = accountChange.indexOfChildDataChangeMarkedForDelete(existingRecordKey, lastChildFormName, modifiedChildRecord, parentSdkName);
              if (indexOfDataMarkedForDelete > -1) {
                accountChange.removeChildFormRecord(lastChildFormName, indexOfDataMarkedForDelete);
                accountChange.addChildDataChanges(lastChildFormName, lastChildFormPrimaryKeyName, existingRecordKey, null, null, "", ChildTableRecord.ACTION.Modify, modifiedChildRecord, policyKey);
                hasDuplicateChildRecord = false;
              }

              if (isUpdatable(accountChange, modifiedChildRecord, lastChildFormName, revokeInapplicable, checkDuplicateChildRecordDS.getString(lastChildFormRevokeColName), policyKey, checkDuplicateChildRecordDS.getString("POL_KEY"))) {
                accountChange.addChildDataChanges(lastChildFormName, lastChildFormPrimaryKeyName, "", new String[] { ACCOUNT_KEY }, new String[] { accountKey }, lastChildRecordNumber, ChildTableRecord.ACTION.Add, modifiedChildRecord, policyKey);
                hasRevokeFlagUpdated = true;
              }
            }

            if (hasDuplicateChildRecord) {
              if (existingRecords.containsKey(lastChildFormName)) {
                StringBuffer existingRecordKeys = existingRecords.get(lastChildFormName);
                existingRecordKeys.append(',');
                existingRecordKeys.append(existingRecordKey);
              }
              else {
                StringBuffer existingRecordKeys = new StringBuffer();
                existingRecordKeys.append(existingRecordKey);
                existingRecords.put(lastChildFormName, existingRecordKeys);
              }
            }
            if (hasRevokeFlagUpdated) {
              if (recordsToBeRemoved.containsKey(lastChildFormName)) {
                StringBuffer existingRecordKeys = recordsToBeRemoved.get(lastChildFormName);
                existingRecordKeys.append(',');
                existingRecordKeys.append(existingRecordKey);
              }
              else {
                StringBuffer existingRecordKeys = new StringBuffer();
                existingRecordKeys.append(existingRecordKey);
                recordsToBeRemoved.put(lastChildFormName, existingRecordKeys);
              }
            }
          }

          // Reset modifiedChildRecord to start collecting data that pertains to the next logical
          // child record from the policy definition. Also reset, checkDuplicateChildRecordFilter
          // so that the filter associated with next logical record can be built.
          checkDuplicateChildRecordFilter.delete(0, checkDuplicateChildRecordFilter.length());
          modifiedChildRecord = new HashMap<String, Object>();
          modifiedChildData = new HashMap<String, Object>();
          //modifiedChildRecord.clear();

          tcDataSet parentFormDataDS = readFormData(parentFormName, accountKey, null);

          // Add basic information related to the next logical record to modifiedChildRecord
          modifiedChildRecord.put(POLICY_KEY, policyKey);
          modifiedChildRecord.put(childFormVersionName, childPolicyDefaultsDS.getString(FORM_FIELD_VERSION));
          modifiedChildRecord.put(parentFormPrimaryKeyName, parentFormDataDS.getString(parentFormPrimaryKeyName));

          lastChildFormName = childFormName;
          lastChildRecordNumber = childRecordNumber;
        }

        if (matchingAttribute) {
          checkDuplicateChildRecordFilter.append(" AND ");
          checkDuplicateChildRecordFilter.append(childPolicyDefaultsDS.getString(FORM_FIELD_NAME));
        }

        if (childPolicyDefaultsDS.getString(FORM_FIELD_TYPE).equalsIgnoreCase(DATE)) {
          String dateString = childPolicyDefaultsDS.getString(CHILD_FORM_FIELD_VALUE);
          if (!isNullOrEmpty(dateString)) {
            Timestamp date = null;
            try {
              date = Timestamp.valueOf(dateString);
            }
            catch (IllegalArgumentException ex) {
              date = Timestamp.valueOf(dateString + " 00:00:00.000000000");
            }

            String formattedDateString = tcDateFormatter.format(date, tcDateFormatter.SHORT, tcDateFormatter.LONGDAY, AccessPolicyUtil.getDatabase());
            modifiedChildRecord.put(childPolicyDefaultsDS.getString(FORM_FIELD_NAME), date);
            modifiedChildData.put(childPolicyDefaultsDS.getString(FORM_FIELD_NAME), childPolicyDefaultsDS.getString(CHILD_FORM_FIELD_VALUE));
            if (matchingAttribute) {
              if (AccessPolicyUtil.getDatabase().getDatabaseProductName().equals("Oracle")) {
                checkDuplicateChildRecordFilter.append(" = TO_DATE('");
                checkDuplicateChildRecordFilter.append(formattedDateString);
                checkDuplicateChildRecordFilter.append("', 'MM/DD/YY HH24:MI:SS')");
              }
              else {
                checkDuplicateChildRecordFilter.append("  = '");
                checkDuplicateChildRecordFilter.append(formattedDateString);
                checkDuplicateChildRecordFilter.append('\'');
              }
            }
          }
        }
        else {
          modifiedChildRecord.put(childPolicyDefaultsDS.getString(FORM_FIELD_NAME), childPolicyDefaultsDS.getString(CHILD_FORM_FIELD_VALUE));
          modifiedChildData.put(childPolicyDefaultsDS.getString(FORM_FIELD_NAME), childPolicyDefaultsDS.getString(CHILD_FORM_FIELD_VALUE));
          if (matchingAttribute) {
            checkDuplicateChildRecordFilter.append(" = '");
            checkDuplicateChildRecordFilter.append(substituteStr(childPolicyDefaultsDS.getString(CHILD_FORM_FIELD_VALUE), "'", "''"));
            checkDuplicateChildRecordFilter.append('\'');
          }
        }
      }

      if (!modifiedChildRecord.isEmpty()) {
        boolean      hasDuplicateChildRecord = false;
        boolean      hasRevokeFlagUpdated = false;
        String       lastChildFormPrimaryKeyName = getFormKeyName(lastChildFormName);
        String       lastChildFormRevokeColName = getFormRevokeName(lastChildFormName);
        StringBuffer checkDuplicateChildRecordQuery = new StringBuffer();
        checkDuplicateChildRecordQuery.append("SELECT ");
        checkDuplicateChildRecordQuery.append(lastChildFormPrimaryKeyName);
        checkDuplicateChildRecordQuery.append(" , ");
        checkDuplicateChildRecordQuery.append(lastChildFormRevokeColName);
        checkDuplicateChildRecordQuery.append(" , ");
        checkDuplicateChildRecordQuery.append("POL_KEY");
        checkDuplicateChildRecordQuery.append(" FROM ");
        checkDuplicateChildRecordQuery.append(lastChildFormName + " ");
        checkDuplicateChildRecordQuery.append(" WHERE ORC_KEY = ?");
        checkDuplicateChildRecordQuery.append(checkDuplicateChildRecordFilter.toString());
        modifiedChildRecord.put(getFormRevokeName(lastChildFormName), revokeInapplicable);
        LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges(): checkDuplicateChildRecordQuery SQL: " + checkDuplicateChildRecordQuery.toString());
        pstmt = new PreparedStatementUtil();
        pstmt.setStatement(AccessPolicyUtil.getDatabase(), checkDuplicateChildRecordQuery.toString());
        pstmt.setString(1, accountKey);
        pstmt.execute();
        tcDataSet checkDuplicateChildRecordDS = pstmt.getDataSet();
        String    existingRecordKey = null;

        if (checkDuplicateChildRecordDS.getRowCount() == 0) {
          if (isAddable(accountChange, modifiedChildRecord, lastChildFormName, revokeInapplicable)) {
            accountChange.addChildDataChanges(lastChildFormName, lastChildFormPrimaryKeyName, "", new String[] { ACCOUNT_KEY }, new String[] { accountKey }, lastChildRecordNumber, ChildTableRecord.ACTION.Add, modifiedChildRecord, policyKey);
          }
        }
        else {
          hasDuplicateChildRecord = true;
          checkDuplicateChildRecordDS.goToRow(0);
          existingRecordKey = checkDuplicateChildRecordDS.getString(lastChildFormPrimaryKeyName);
          int indexOfDataMarkedForDelete = accountChange.indexOfChildDataChangeMarkedForDelete(existingRecordKey, lastChildFormName, modifiedChildRecord, parentSdkName);
          if (indexOfDataMarkedForDelete > -1) {
            accountChange.removeChildFormRecord(lastChildFormName, indexOfDataMarkedForDelete);
            accountChange.addChildDataChanges(lastChildFormName, lastChildFormPrimaryKeyName, existingRecordKey, null, null, "", ChildTableRecord.ACTION.Modify, modifiedChildRecord, policyKey);
            hasDuplicateChildRecord = false;
          }

          if (isUpdatable(accountChange, modifiedChildRecord, lastChildFormName, revokeInapplicable, checkDuplicateChildRecordDS.getString(lastChildFormRevokeColName), policyKey, checkDuplicateChildRecordDS.getString("POL_KEY"))) {
            accountChange.addChildDataChanges(lastChildFormName, lastChildFormPrimaryKeyName, "", new String[] { ACCOUNT_KEY }, new String[] { accountKey }, lastChildRecordNumber, ChildTableRecord.ACTION.Add, modifiedChildRecord, policyKey);
            hasRevokeFlagUpdated = true;
          }
        }

        if (hasDuplicateChildRecord) {
          if (existingRecords.containsKey(lastChildFormName)) {
            StringBuffer existingRecordKeys = existingRecords.get(lastChildFormName);
            existingRecordKeys.append(',');
            existingRecordKeys.append(existingRecordKey);
          }
          else {
            StringBuffer existingRecordKeys = new StringBuffer();
            existingRecordKeys.append(existingRecordKey);
            existingRecords.put(lastChildFormName, existingRecordKeys);
          }
        }
        if (hasRevokeFlagUpdated) {
          if (recordsToBeRemoved.containsKey(lastChildFormName)) {
            StringBuffer existingRecordKeys = recordsToBeRemoved.get(lastChildFormName);
            existingRecordKeys.append(',');
            existingRecordKeys.append(existingRecordKey);
          }
          else {
            StringBuffer existingRecordKeys = new StringBuffer();
            existingRecordKeys.append(existingRecordKey);
            recordsToBeRemoved.put(lastChildFormName, existingRecordKeys);
          }
        }
      }

      // Identify & remove child records from all child forms which were previously added by the
      // same policy but are no longer applicable (possibly because the definition of policy changed)
      deleteSpecificChildData(policyKey, accountKey, objectKey, accountChange, existingRecords);
      LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges(): # of records to be removed: " + recordsToBeRemoved.size());
      if (recordsToBeRemoved.size() > 0) {
        Iterator<String> forms = recordsToBeRemoved.keySet().iterator();
        while (forms.hasNext()) {
          StringBuffer recordsToDeleteQuery = new StringBuffer();
          String       formName = forms.next();
          String       existingRecordKeys = recordsToBeRemoved.get(formName).toString();

          String formPrimaryKeyName = getFormKeyName(formName);
          String formRowverName = getFormRowverName(formName);

          // identify child records from all child forms which were previously
          // added by the same policy but are no longer applicable
          recordsToDeleteQuery.append("SELECT ");
          recordsToDeleteQuery.append(formPrimaryKeyName);
          recordsToDeleteQuery.append(", ");
          recordsToDeleteQuery.append(formRowverName);
          recordsToDeleteQuery.append(" FROM ");
          recordsToDeleteQuery.append(formName);
          recordsToDeleteQuery.append(" WHERE ORC_KEY = ?");

          if (existingRecordKeys != null && !existingRecordKeys.trim().equals("")) {
            recordsToDeleteQuery.append(APIUtils.getInClause(existingRecordKeys, formPrimaryKeyName, true)); // IN-CLAUSE with AND
          }

          LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges(): recordsToDeleteQuery: " + recordsToDeleteQuery);

          pstmt = new PreparedStatementUtil();
          pstmt.setStatement(AccessPolicyUtil.getDatabase(), recordsToDeleteQuery.toString());
          pstmt.setString(1, accountKey);
          pstmt.execute();
          tcDataSet recordsToDeleteDS = pstmt.getDataSet();
          LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges(): records to be deleted count: " + recordsToDeleteDS.getRowCount());

          for (int i = 0; i < recordsToDeleteDS.getRowCount(); i++) {
            recordsToDeleteDS.goToRow(i);
            LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges(): delete from child table " + formName + " a record with " + formPrimaryKeyName + " = " + recordsToDeleteDS.getString(formPrimaryKeyName));
            accountChange.addChildDataChanges(formName, formPrimaryKeyName, recordsToDeleteDS.getString(formPrimaryKeyName), null, null, "", ChildTableRecord.ACTION.Delete, null, policyKey);
          }
        }
      }
    }
    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.compileChildDataChanges():accountKey: " + accountKey + " policyKey:" + policyKey + " START");
    return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compileChildDataChangesRelatedToInapplicablePolicies
  /**
   ** Determinse the inapplicable changes happend due to assigned or removed
   ** roles for an identity that belongs to the given <code>userKey</code>.
   **
   ** @param  userKey            the internal system identifier of an identity.
   ** @param  inapplicable       the mappink of policies which are inapplicable.
   ** @param  accountsToModify   the accounts to modify
   **
   ** @throws tcDataSetException    ...
   ** @throws tcDataAccessException ...
   */
  protected void compileChildDataChangesRelatedToInapplicablePolicies(String userKey, Map<String, String> inapplicable, HashMap<String, AccountChange> accountsToModify)
    throws tcDataAccessException
    ,      tcDataSetException {

    final Iterator<String> policyKeys = inapplicable.keySet().iterator();
    while (policyKeys.hasNext()) {
      StringBuffer query     = new StringBuffer(AccessPolicyUtil.getQuery("GET_CHILD_FORM_INFO_FOR_USERS_ACTIVE_RESOURCES"));
      String       policyKey = policyKeys.next();
      String       resources = getQuotedString(StringUtil.escapeSingleQuoteForDBUse(inapplicable.get(policyKey)));
      if (!isNullOrEmpty(resources)) {
        query.append(" AND OBJ.OBJ_NAME IN (");
        query.append((resources));
        query.append(')');
      }
      query.append(" ORDER BY ORC.ORC_KEY");

      PreparedStatementUtil pstmt = new PreparedStatementUtil();
      pstmt.setStatement(AccessPolicyUtil.getDatabase(), query.toString());
      pstmt.setString(1, userKey);
      pstmt.execute();
      tcDataSet childFormInfoForUsersActiveResourcesDS = pstmt.getDataSet();

      AccountChange accountChange = null;
      for (int i = 0; i < childFormInfoForUsersActiveResourcesDS.getRowCount(); i++) {
        childFormInfoForUsersActiveResourcesDS.goToRow(i);
        String    childFormName           = childFormInfoForUsersActiveResourcesDS.getString(CHILD_FORM_NAME);
        String    childFormPrimaryKeyName = getFormKeyName(childFormName);
        String    accountKey              = childFormInfoForUsersActiveResourcesDS.getString(ACCOUNT_KEY);
        String    userAccountKey          = childFormInfoForUsersActiveResourcesDS.getString(USER_ACCOUNT_KEY);
        tcDataSet childFormFieldNames     = AccessPolicyUtil.getChildFormFields(childFormInfoForUsersActiveResourcesDS.getString(CHILD_SDK_KEY));
        tcDataSet childFormData           = AccessPolicyUtil.readChildFormData(childFormName, accountKey, policyKey, childFormFieldNames);
        if (childFormData.getRowCount() > 0) {
          accountChange = getAccountChange(accountKey, userAccountKey, accountsToModify);
          for (int j = 0; j < childFormData.getRowCount(); j++) {
            childFormData.goToRow(j);

            if (isRevokable(childFormData.getString(getFormRevokeName(childFormName)))) {
              HashMap<String, Object> childFormValues = new HashMap<String, Object>();
              for (int k = 0; k < childFormFieldNames.getRowCount(); k++) {
                String columnName = null;
                String columnValue = null;
                childFormFieldNames.goToRow(k);
                columnName = childFormFieldNames.getString("sdc_name");
                if (childFormData.getString(columnName) != null)
                  columnValue = childFormData.getString(columnName);
                if (!isNullOrEmpty(columnValue))
                  childFormValues.put(columnName, columnValue);
              }
              accountChange.addChildDataChanges(childFormName, childFormPrimaryKeyName, childFormData.getString(childFormPrimaryKeyName), null, null, "", ChildTableRecord.ACTION.Delete, childFormValues, policyKey);
            }
          }
          setAccountChange(accountChange, accountsToModify);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccountChange
  /**
   *
   * @param accountChange
   * @param accountsToModify
   */
  private void setAccountChange(AccountChange accountChange, HashMap<String, AccountChange> accountsToModify) {
    if (accountChange.isModified()) {
      accountsToModify.put(accountChange.getUserAccountKey(), accountChange);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountChange
  /**
   *
   * @param accountsToModify
   * @return
   */
  private AccountChange getAccountChange(String accountKey, String userAccountKey, HashMap<String, AccountChange> accountsToModify) {
    AccountChange accountChange = accountsToModify.get(userAccountKey);
    if (accountChange == null) {
      accountChange = new AccountChange(accountKey, userAccountKey);
      //accountsToModify.put(accountKey, accountChange);
    }
    return accountChange;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteChildData
  /**
   *
   * @param policyKey
   * @param accountKey
   * @param objectKey
   * @param accountChange
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   * @throws Exception
   */
  private void deleteChildData(String policyKey, String accountKey, String objectKey, AccountChange accountChange)
    throws tcDataAccessException, tcDataSetException {
    // Get form information for the parent form associated with the specified account
    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_ACCOUNT_SPECIFIC_FORM_DETAILS"));
    pstmt.setString(1, accountKey);
    pstmt.execute();
    tcDataSet formDetailsDS = pstmt.getDataSet();

    long formKey = formDetailsDS.getLong(FORM_KEY);
    long formActiveVersion = formDetailsDS.getLong(FORM_ACTIVE_VERSION);

    // Get keys of child forms associated with the parent form from above
    pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_CHILD_FORM_KEYS"));
    pstmt.setLong(1, formKey);
    pstmt.setLong(2, formActiveVersion);
    pstmt.execute();
    tcDataSet childFormKeysDS = pstmt.getDataSet();

    // for each child form identify existing data associated with the given account & policy
    for (int i = 0; i < childFormKeysDS.getRowCount(); i++) {
      childFormKeysDS.goToRow(i);
      long childFormKey = childFormKeysDS.getLong(CHILD_FORM_KEY);

      // Get form name for the given child form key
      pstmt = new PreparedStatementUtil();
      pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_FORM_NAME"));
      pstmt.setLong(1, childFormKey);
      pstmt.execute();
      tcDataSet childFormNameDS = pstmt.getDataSet();

      childFormNameDS.goToRow(0);

      String childFormName = childFormNameDS.getString(FORM_NAME);
      String childFormPrimaryKeyName = getFormKeyName(childFormName);
      String childFormRowverName = getFormRowverName(childFormName);

      // Get existing data from the child form associated with the given account & policy
      StringBuffer currentChildDataQuery = new StringBuffer();
      currentChildDataQuery.append("SELECT ");
      currentChildDataQuery.append(childFormPrimaryKeyName);
      currentChildDataQuery.append(',');
      currentChildDataQuery.append(childFormRowverName);
      currentChildDataQuery.append(" FROM ");
      currentChildDataQuery.append(childFormName);
      currentChildDataQuery.append(" WHERE ORC_KEY = ? and POL_KEY = ?");

      pstmt = new PreparedStatementUtil();
      pstmt.setStatement(AccessPolicyUtil.getDatabase(), currentChildDataQuery.toString());
      pstmt.setLong(1, Long.parseLong(accountKey));
      pstmt.setLong(2, Long.parseLong(policyKey));
      pstmt.execute();
      tcDataSet currentChildDataDS = pstmt.getDataSet();

      for (int j = 0; j < currentChildDataDS.getRowCount(); j++) {
        currentChildDataDS.goToRow(j);
        String childFormPrimaryKeyValue = currentChildDataDS.getString(childFormPrimaryKeyName);
        accountChange.addChildDataChanges(childFormName, childFormPrimaryKeyName, childFormPrimaryKeyValue, null, null, "", ChildTableRecord.ACTION.Delete, null, policyKey);
      }
    }
    return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSpecificChildData
  private void deleteSpecificChildData(String policyKey, String accountKey, String objectKey, AccountChange accountChange,
      HashMap<String, StringBuffer> existingRecords)
      throws tcDataAccessException, tcDataSetException {

    deleteSpecificChildData(policyKey, accountKey, objectKey, accountChange, existingRecords, null);
    return;
  }

  /**
   *
   * @param policyKey
   * @param accountKey
   * @param objectKey
   * @param accountChange
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   * @throws Exception
   */
  private void deleteSpecificChildData(String policyKey, String accountKey, String objectKey, AccountChange accountChange, HashMap<String, StringBuffer> existingRecords, Long childFormKeyToBeProcessed)
    throws tcDataAccessException
    ,      tcDataSetException {

    LOGGER.log(Level.INFO, "#### START: SingleAccountEvaluationUtility.deleteSpecificChildData(): ####");
    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.deleteSpecificChildData() " + " policyKey: " + policyKey + " accountKey: " + accountKey + " objectKey: " + objectKey + " accountChange: " + accountChange + " existingRecords: " + printMap(existingRecords));

    // Get form information for the parent form associated with the specified account
    String strGetAccountSpecificFormDetail = " SELECT SDK.SDK_KEY, SDK.SDK_NAME, SDK.SDK_ACTIVE_VERSION " + " FROM SDK SDK, TOS TOS, ORC ORC " + " WHERE TOS.SDK_KEY = SDK.SDK_KEY " + " AND ORC.TOS_KEY = TOS.TOS_KEY " + " AND ORC.ORC_KEY = " + accountKey;
    tcDataSet formDetailsDS = CacheUtil.getSetCachedQuery(AccessPolicyUtil.getDatabase(), strGetAccountSpecificFormDetail, strGetAccountSpecificFormDetail, CacheUtil.ACCESS_POLICIES);

    long formKey = formDetailsDS.getLong(FORM_KEY);
    long formActiveVersion = formDetailsDS.getLong(FORM_ACTIVE_VERSION);

    // Get keys of child forms associated with the parent form from above
    String strGetChildFormKeys = " SELECT SDH.SDH_CHILD_KEY FROM SDH WHERE SDH.SDH_PARENT_KEY = " + formKey + " AND SDH.SDH_PARENT_VERSION = " + formActiveVersion;
    LOGGER.log(Level.INFO, "#### SingleAccountEvaluationUtility.deleteSpecificChildData(): strGetChildFormKeys SQL: " + strGetChildFormKeys);
    tcDataSet childFormKeysDS = CacheUtil.getSetCachedQuery(AccessPolicyUtil.getDatabase(), strGetChildFormKeys, strGetChildFormKeys, CacheUtil.ACCESS_POLICIES);

    LOGGER.log(Level.INFO, "#### SingleAccountEvaluationUtility.deleteSpecificChildData(): # of Records: " + childFormKeysDS.getRowCount());

    // for each child form identify existing data associated with the given account & policy
    for (int i = 0; i < childFormKeysDS.getRowCount(); i++) {
      childFormKeysDS.goToRow(i);
      long childFormKey = childFormKeysDS.getLong(CHILD_FORM_KEY);
      //do not delete the form records if it is not the child form to be processed
      if (childFormKeyToBeProcessed != null && !childFormKeyToBeProcessed.equals(childFormKey)) {
        continue;
      }

      // Get form name for the given child form key
      String    strGetFormName = "SELECT SDK.SDK_NAME FROM SDK WHERE SDK.SDK_KEY= " + childFormKey;
      tcDataSet childFormNameDS = CacheUtil.getSetCachedQuery(AccessPolicyUtil.getDatabase(), strGetFormName, strGetFormName, CacheUtil.ACCESS_POLICIES);
      childFormNameDS.goToRow(0);
      String childFormName = childFormNameDS.getString(FORM_NAME);
      String childFormPrimaryKeyName = getFormKeyName(childFormName);
      String childFormRowverName = getFormRowverName(childFormName);

      String existingKeys = null;
      //Get existing keys that should not be deleted
      if (existingRecords != null && existingRecords.get(childFormName) != null) {
        existingKeys = existingRecords.get(childFormName).toString().trim();
      }

      // Get existing data from the child form associated with the given account & policy
      StringBuffer currentChildDataQuery = new StringBuffer();
      currentChildDataQuery.append("SELECT ");
      currentChildDataQuery.append(childFormPrimaryKeyName);
      currentChildDataQuery.append(',');
      currentChildDataQuery.append(childFormRowverName);
      currentChildDataQuery.append(" FROM ");
      currentChildDataQuery.append(childFormName);

      currentChildDataQuery.append(" WHERE ORC_KEY = ");
      currentChildDataQuery.append(accountKey);
      currentChildDataQuery.append("and POL_KEY = ");
      currentChildDataQuery.append(policyKey);

      if (existingKeys != null && !existingKeys.trim().equals("")) {
        // Records in UD that are also in POC are retained, else they are deleted
        currentChildDataQuery.append(APIUtils.getInClause(existingKeys, childFormPrimaryKeyName, true, false)); // NOT-IN CLAUSE WITH AND
      }

      String strCurrentChildDataQuery = currentChildDataQuery.toString();

      LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.deleteSpecificChildData(): strCurrentChildDataQuery: " + strCurrentChildDataQuery);
      tcDataSet currentChildDataDS = CacheUtil.getSetCachedQuery(AccessPolicyUtil.getDatabase(), strCurrentChildDataQuery, strCurrentChildDataQuery, CacheUtil.ACCESS_POLICIES);
      LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.deleteSpecificChildData(): records to be deleted: " + currentChildDataDS.getRowCount());

      for (int j = 0; j < currentChildDataDS.getRowCount(); j++) {
        currentChildDataDS.goToRow(j);

        String childFormPrimaryKeyValue = currentChildDataDS.getString(childFormPrimaryKeyName);
        LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.deleteSpecificChildData():childFormPrimaryKeyValue: " + childFormPrimaryKeyValue);
        accountChange.addChildDataChanges(childFormName, childFormPrimaryKeyName, childFormPrimaryKeyValue, null, null, "", ChildTableRecord.ACTION.Delete, null, policyKey);
      }
    }
    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.deleteSpecificChildData():accountChange: " + accountChange);
    LOGGER.log(Level.INFO, "#### END: SingleAccountEvaluationUtility.deleteSpecificChildData(): ####");
    return;
  }

  /**
   * Read data from the database for the given form name,
   * @param formName
   * @param accountKey
   * @param policyKey
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   */
  private tcDataSet readFormData(String formName, String accountKey, String policyKey)
    throws tcDataSetException, tcDataAccessException {
    boolean      hasPolicyKey = false;
    StringBuffer query = new StringBuffer();
    String       formKeyName = getFormKeyName(formName);
    String       formRowverName = getFormRowverName(formName);
    String       formVersionName = getFormVersionName(formName);
    String       formRevokeName = getFormRevokeName(formName);

    query.append("SELECT ");
    query.append(formKeyName);
    query.append(", ");
    query.append(formVersionName);
    query.append(", ");
    query.append(formRevokeName);
    query.append(", ");
    query.append(formRowverName);
    query.append(" FROM ");
    query.append(formName);
    query.append(" WHERE ORC_KEY=?");

    if (policyKey != null) {
      hasPolicyKey = true;
      query.append(" AND POL_KEY=?");
    }

    LOGGER.log(Level.INFO, "SingleAccountEvaluationUtility.readFormData:query: " + query);

    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), query.toString());
    pstmt.setString(1, accountKey);
    if (hasPolicyKey) {
      pstmt.setString(2, policyKey);
    }
    pstmt.execute();
    return pstmt.getDataSet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateProvisioningChoices
  /**
   *
   * @param objectKey
   * @param requestBasedProvisioning
   */
  private void updateProvisioningChoices(String objectKey, String requestBasedProvisioning) {
    // Provisioning will always be direct irrespective of POL_REQUEST value
    String provisioningType = "D";
    Vector<String> objProvisioningOptions;
    if (!this.provisioningOptions.containsKey(objectKey)) {
      objProvisioningOptions = new Vector<String>();
      objProvisioningOptions.addElement(provisioningType);
    }
    else {
      objProvisioningOptions = this.provisioningOptions.get(objectKey);
      objProvisioningOptions.addElement(provisioningType);
    }

    // Put the vector in the hash table
    this.provisioningOptions.put(objectKey, objProvisioningOptions);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   addUserPolicyProfileDetails
  /**
   * Adds entries to the User Policy Profile Details table.
   *
   * @param uppKey User Policy Profile Key
   */
  private void addUserPolicyProfileDetails(String uppKey) throws tcDataAccessException, tcDataSetException{
    // bug fix#8550220
    //Since in deleteUpdEntries, didn't delete all UPD entries for the input psUppkey.
    //so, don't need to read all the upd rows back. Only add the new additions if there have some.
    List<String> currentPolicies = new ArrayList<String>();
    Map<String, String[]> currentPolicyAllowDenyLists = new HashMap<String, String[]>();
    Map<String, byte[]> currentPolicyRowvers = new HashMap<String, byte[]>();

    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_USER_POLICY_PROFILE_DETAILS"));
    pstmt.setString(1, uppKey);
    pstmt.execute();
    tcDataSet currentPolicyProfileDetailsDS = pstmt.getDataSet();

    for (int i = 0; i < currentPolicyProfileDetailsDS.getRowCount(); i++) {
      currentPolicyProfileDetailsDS.goToRow(i);
      String policyKey = currentPolicyProfileDetailsDS.getString(POLICY_KEY);

      String[] allowDenyList = new String[2];
      allowDenyList[0] = currentPolicyProfileDetailsDS.getString(POLICY_PROFILE_DETAILS_ALLOW_LIST);
      allowDenyList[1] = currentPolicyProfileDetailsDS.getString(POLICY_PROFILE_DETAILS_DENY_LIST);

      currentPolicyAllowDenyLists.put(policyKey,allowDenyList);
      currentPolicyRowvers.put(policyKey, currentPolicyProfileDetailsDS.getByteArray(POLICY_PROFILE_DETAILS_ROWVER));
      currentPolicies.add(policyKey);
    }

    Enumeration<String> allApplicablePolicyKeys = this.policyProfile.keys();
    while (allApplicablePolicyKeys.hasMoreElements()) {
      boolean addPolicyDetails = false;
      boolean updatePolicyDetails = false;
      String policyKey = allApplicablePolicyKeys.nextElement();
      String[] allowDenyList = this.policyProfile.get(policyKey);

      if(!currentPolicies.contains(policyKey)) {
        addPolicyDetails = true;
      }
      else {
        String currentAllowList = currentPolicyAllowDenyLists.get(policyKey)[0];
        String currentDenyList  = currentPolicyAllowDenyLists.get(policyKey)[1];
        String newAllowList     = allowDenyList[0];
        String newDenyList      = allowDenyList[1];
        if(!currentAllowList.equalsIgnoreCase(newAllowList) || !currentDenyList.equalsIgnoreCase(newDenyList)){
          updatePolicyDetails = true;
        }
      }

      if (addPolicyDetails || updatePolicyDetails){
        String allowList = allowDenyList[0];
        String denyList  = allowDenyList[1];
        tcUPD updDAO = null;
        if (addPolicyDetails) {
          updDAO = new tcUPD(AccessPolicyUtil.getDatabase(), uppKey, policyKey, new byte[0]);
        }
        else if(updatePolicyDetails) {
          updDAO = new tcUPD(AccessPolicyUtil.getDatabase(), uppKey, policyKey, currentPolicyRowvers.get(policyKey));
        }
        updDAO.setString(POLICY_PROFILE_DETAILS_ALLOW_LIST, allowList);
        updDAO.setString(POLICY_PROFILE_DETAILS_DENY_LIST, denyList);
        AuditEngine engine = AuditEngine.getAuditEngine(AccessPolicyUtil.getDatabase());
        engine.pushReason(AccessPolicyUtil.getContextInfo(ContextManager.ContextTypes.POLICY, ACCESS_POLICY), Long.parseLong(policyKey));
        updDAO.save();
        engine.popReason();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   archiveUserPolicyProfile
  /**
   *
   * @param userKey
   * @param currentPolicyProfileDS
   * @throws tcDataSetException
   */
  private String archiveUserPolicyProfile(String userKey, tcDataSet currentPolicyProfileDS)
      throws tcDataSetException {

    tcUPH uphDO = new tcUPH(AccessPolicyUtil.getDatabase(), "", userKey, new byte[0]);
    uphDO.save();
    return uphDO.getString(ARCHIVED_POLICY_PROFILE_KEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   archiveUserPolicyProfileDetails
  /**
   * Copy User Policy Profile Details to User Policy Profile History Details.
   *
   * @param psUppKey
   *            The User Policy Profile Key
   * @param uphKey
   *            The User Policy Profile History Key
   * @throws tcDataSetException
   * @throws tcDataAccessException
   *
   * @throws Exception
   *             DOCUMENT ME!
   */
  private void archiveUserPolicyProfileDetails(String uppKey, String uphKey)
    throws tcDataAccessException
    ,      tcDataSetException {

    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_USER_POLICY_PROFILE_DETAILS"));
    pstmt.setString(1, uppKey);
    pstmt.execute();
    tcDataSet currentPolicyProfileDetailsDS = pstmt.getDataSet();

    for (int i=0; i<currentPolicyProfileDetailsDS.getRowCount(); i++) {
      currentPolicyProfileDetailsDS.goToRow(i);
      tcUHD uhdDAO = new tcUHD(AccessPolicyUtil.getDatabase(), uphKey, currentPolicyProfileDetailsDS.getString(POLICY_KEY), new byte[0]);
      uhdDAO.setString(ARCHIVED_POLICY_PROFILE_DETAILS_ALLOW_LIST, currentPolicyProfileDetailsDS.getString(POLICY_PROFILE_DETAILS_ALLOW_LIST));
      uhdDAO.setString(ARCHIVED_POLICY_PROFILE_DETAILS_DENY_LIST,  currentPolicyProfileDetailsDS.getString(POLICY_PROFILE_DETAILS_DENY_LIST));
      uhdDAO.save();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteUserPolicyProfileDetails
  /**
   * Delete entries from the User Policy Profile Details table.
   *
   * @param uppKey
   *            The User Policy Profile Key
   * @throws tcDataSetException
   * @throws tcDataAccessException
   *
   * @throws Exception
   *             DOCUMENT ME!
   */
  private void deleteUserPolicyProfileDetails(String uppKey)
    throws tcDataAccessException
    ,      tcDataSetException {

    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_USER_POLICY_PROFILE_DETAILS"));
    pstmt.setString(1, uppKey);
    pstmt.execute();
    tcDataSet currentPolicyProfileDetailsDS = pstmt.getDataSet();

    for (int i = 0; i < currentPolicyProfileDetailsDS.getRowCount(); i++) {
      currentPolicyProfileDetailsDS.goToRow(i);
      String policyKey = currentPolicyProfileDetailsDS.getString(POLICY_KEY);
      // bug fix#8550220
      if (!this.policyProfile.containsKey(policyKey)){
        tcUPD updDAO = new tcUPD(AccessPolicyUtil.getDatabase(), currentPolicyProfileDetailsDS.getString(POLICY_PROFILE_KEY), policyKey, currentPolicyProfileDetailsDS.getByteArray(POLICY_PROFILE_DETAILS_ROWVER));
        AuditEngine engine = AuditEngine.getAuditEngine(AccessPolicyUtil.getDatabase());
        engine.pushReason(AccessPolicyUtil.getContextInfo(ContextManager.ContextTypes.POLICY, ACCESS_POLICY), Long.parseLong(policyKey));
        updDAO.delete();
        engine.popReason();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getParentPolicyDefaults
  /**
   *
   * @param policyKey
   * @param objectKey
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   */
  private tcDataSet getParentPolicyDefaults(String policyKey, String objectKey)
    throws tcDataSetException
    ,      tcDataAccessException {

    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_PARENT_DATA_FROM_POLICY_DEFINITION"));
    pstmt.setString(1, policyKey);
    pstmt.setString(2, objectKey);
    pstmt.setString(3, objectKey);
    pstmt.execute();
    return pstmt.getDataSet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasParentPolicyDefaults
  /**
   *
   * @param parentPolicyDefaultsDS
   * @return
   * @throws tcDataSetException
   */
  private boolean hasParentPolicyDefaults(tcDataSet parentPolicyDefaultsDS)
    throws tcDataSetException {

    return (parentPolicyDefaultsDS.getRowCount() > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFormKeyName
  /**
   *
   * @param formName
   * @return
   */
  private String getFormKeyName(String formName) {
    return formName + KEY_SUFFIX;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFormRowverName
  /**
   *
   * @param parentFormName
   * @return
   */
  private String getFormRowverName(String formName) {
    return formName + ROWVER_SUFFIX;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFormVersionName
  /**
   *
   * @param parentFormName
   * @return
   */
  private String getFormVersionName(String formName) {
    return formName + VERSION_SUFFIX;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFormRevokeName
  /**
   *
   * @param parentFormName
   * @return
   */
  private String getFormRevokeName(String formName) {
    return formName + ACTION_SUFFIX;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEntitlementRevokeIfNoLongerAppliesSupported
  private boolean isEntitlementRevokeIfNoLongerAppliesSupported() {
    return false;
    //Remove RNLA-unchecked enhancement for entitlement data
    /*AccessPolicyUtils accessPolicyUtils = new AccessPolicyUtils();
    return accessPolicyUtils.isEntitlementRevokeIfNoLongerAppliesSupported();*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isNullOrEmpty
  /**
   *
   * @param value
   * @return
   */
  private boolean isNullOrEmpty(String value) {
    if (value != null && (value.equals("") == false)) {
      return false;
    } else {
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isNull
  /**
   *
   * @param string
   * @return
   */
  private boolean isNull(Object value) {
    return value == null ? true : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrintableStackTrace
  /**
   *
   * @param e
   * @return
   */
  private static String getPrintableStackTrace(Exception e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    return (sw.toString());
  }

  private final StringBuffer printMap(Map map) {
    StringBuffer buf = new StringBuffer();
    if (map != null) {
      Set      keys = map.keySet();
      Iterator keyIter = keys.iterator();

      while (keyIter.hasNext()) {
        Object key = keyIter.next();
        Object value = map.get(key);
        buf.append(key + "===>\n" + "[" + value + "]");
      }
    }
    return buf;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   substituteStr
  /**
   *
   * @param text
   * @param textToFind
   * @param replacement
   * @return
   */
  private static String substituteStr(String text, String textToFind, String replacement) {
    int          tagIndex = text.indexOf(textToFind);
    int          lastTag = 0;
    StringBuffer buff = new StringBuffer();

    while (tagIndex != -1) {
      buff.append(text.substring(lastTag, tagIndex));
      buff.append(replacement);
      lastTag = tagIndex + textToFind.length();
      tagIndex = text.indexOf(textToFind, lastTag);
    }

    if (buff.length() == 0) {
      return text;
    }
    else {
      return buff.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getQuotedString
  /**
   * Returns the same string but quoted
   *
   * @param commaSeparatedStr
   *            comma separated string to get an array
   * @return the same string with the values quoted. Empty if the string is
   *         null or empty.
   * @throws tcDataAccessException
   */
  private String getQuotedString(String commaSeparatedStr)
    throws tcDataSetException
    ,      tcDataAccessException {

    String retval = null;
    if ((commaSeparatedStr != null) || (commaSeparatedStr.length() > 0)) {
      StringTokenizer tok = new StringTokenizer(commaSeparatedStr, ",");
      StringBuffer    buff = new StringBuffer();
      String          curRes = null;
      int             policyIndex = -1;
      int             tokens = tok.countTokens();
      if (tokens > 0) {
        Map<String, String> allPolicies = getAllPolicyNames();
        for (int i = 0; i < tokens; i++) {
          curRes = tok.nextToken();
          policyIndex = curRes.lastIndexOf("[");
          if (i != 0) {
            buff.append(',');
          }
          buff.append('\'');
          if (policyIndex != -1) { // there is a policy

            int endPolicyIndex = curRes.indexOf("]", policyIndex);

            if (endPolicyIndex != -1) {
              String policyName = curRes.substring(policyIndex + 1, endPolicyIndex).trim();
              int    colonIndex = policyName.indexOf(":");
              if (colonIndex != -1) {
                policyName = policyName.substring(0, colonIndex);
              }
              if (allPolicies.containsKey(policyName)) {
                buff.append(curRes.substring(0, policyIndex).trim());
              }
              else {
                buff.append(curRes.trim());
              }
            }
            else {
              buff.append(curRes.trim());
            }
          }
          else {
            buff.append(curRes.trim());
          }
          buff.append('\'');
        }
        retval = buff.toString();
      }
    }
    else {
      retval = "";
    }
    return retval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllPolicyNames
  /**
   *
   * @return
   * @throws tcDataSetException
   * @throws tcDataAccessException
   */
  private Map<String, String> getAllPolicyNames()
    throws tcDataSetException
    ,      tcDataAccessException {

    // FOLLOW UP: Cache allPoliciesDS below
    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    pstmt.setStatement(AccessPolicyUtil.getDatabase(), AccessPolicyUtil.getQuery("GET_ALL_POLICIES"));
    pstmt.execute();
    tcDataSet allPoliciesDS = pstmt.getDataSet();

    Map<String, String> allPolicies = new HashMap<String, String>();
    for (int i = 0; i < allPoliciesDS.getRowCount(); i++) {
      allPoliciesDS.goToRow(i);
      allPolicies.put(allPoliciesDS.getString(POLICY_NAME), allPoliciesDS.getString(POLICY_KEY));
    }
    return allPolicies;
  }

  private static Set<String> createPolicySupportedProvMechanismSet() {
    Set<String> provMechanismSet = new HashSet<String>();
    provMechanismSet.add(OIU_PROV_MECHANISM_RECONCILIATION);
    provMechanismSet.add(OIU_PROV_MECHANISM_BULKLOAD);
    return Collections.unmodifiableSet(provMechanismSet);
  }   
}