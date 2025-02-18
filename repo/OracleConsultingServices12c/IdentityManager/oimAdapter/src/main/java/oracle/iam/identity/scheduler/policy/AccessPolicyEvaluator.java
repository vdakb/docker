/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   AccessPolicyEvaluator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyEvaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import java.util.Iterator;
import java.util.Enumeration;
import java.util.Vector;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Hashtable;

import com.thortech.xl.dataaccess.tcDataSetException;
import com.thortech.xl.dataaccess.tcDataSet;
import com.thortech.xl.dataaccess.tcDataProvider;

import com.thortech.xl.server.tcOrbServerObject;

import com.thortech.xl.audit.engine.AuditEngine;

import com.thortech.xl.dataobj.tcUPH;
import com.thortech.xl.dataobj.tcUPP;
import com.thortech.xl.dataobj.tcUHD;
import com.thortech.xl.dataobj.tcUPD;
import com.thortech.xl.dataobj.tcUDProcess;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.AbstractServiceTask;

import oracle.iam.identity.foundation.offline.Account;
import oracle.iam.identity.foundation.offline.AccessPolicy;
import oracle.iam.identity.foundation.offline.ApplicationEntity;

public class AccessPolicyEvaluator implements tcOrbServerObject {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final AbstractServiceTask task;

  private Hashtable                 allow;
  private Hashtable                 allowExtra;
  private Hashtable                 deny;
  private Hashtable                 orgDeny;
  private Hashtable                 policies;
  private Hashtable                 allPoliciesThatApply;

  private Hashtable                 reqObjects;
  private Hashtable                 dirObjects;

  private Vector<String>            orgObjects;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccessPolicyEvaluator</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  task                          the session provider connection
   */
  public AccessPolicyEvaluator(final AbstractServiceTask task) {
    // ensure inheritance
    super();

    this.task = task;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeInstance (tcOrbServerObject)
  public void removeInstance() {

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataBase (tcOrbServerObject)
  public tcDataProvider getDataBase() {
    return this.task.provider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evaluate
  /**
   ** Evaluates the specified <code>Access Policy</code> for the specified
   ** <code>Account</code>.
   ** <p>
   ** The method adjust the <code>Access Policies</code> assigned to the
   ** <code>Account</code> by extending the repository table <code>UPP</code>.
   ** This table controls the handling of the <code>Access Policies</code> were
   ** the <code>Account</code> belongs to.
   **
   ** @param  account            the <code>Account</code> as the subject of the
   **                            evaluation.
   ** @param  policy             the <code>Access Policy</code> as the subject
   **                            of the evaluation.
   **
   ** @throws ResolverException  in case an error occurs
   */
  public void evaluate(final Account account, final AccessPolicy policy)
    throws ResolverException {

    this.allow                = new Hashtable();
    this.allowExtra           = new Hashtable();
    this.deny                 = new Hashtable();
    this.orgDeny              = new Hashtable();
    this.policies             = new Hashtable();
    this.allPoliciesThatApply = new Hashtable();

    this.reqObjects           = new Hashtable();
    this.dirObjects           = new Hashtable();

    this.orgObjects           = getAllowListforOrg(account);

    tcDataSet  tcdataset = new tcDataSet();
    StringBuilder query = new StringBuilder();
    query.append("select distinct pol.pol_key, pol.pol_name, pol.pol_request, pol.pol_priority ");
    query.append("from usg usg, pog pog, pol pol where usg.ugp_key=pog.ugp_key and pog.pol_key=pol.pol_key ");
    query.append("and usg.usr_key=");
    query.append(account.key());
    tcdataset.setQuery(this.task.provider(), query.toString());
    try {
      tcdataset.executeQuery();
      int size = tcdataset.getRowCount();
      for (int j = 0; j < size; j++) {
        tcdataset.goToRow(j);
        String dataPolicyKey      = tcdataset.getString("pol_key");
        String dataPolicyName     = tcdataset.getString("pol_name");
        String dataPolicyRequest  = tcdataset.getString("pol_request");
        long   dataPolicyPriority = tcdataset.getLong("pol_priority");
        String xxx = "select obj.obj_key, obj.obj_name, obj.obj_allow_multiple, obj.obj_allowall, pop.pop_denial, pop.pop_revoke_object from pop pop, obj obj where pop.pol_key = " + dataPolicyKey + " and pop.obj_key = obj.obj_key and obj.obj_key in ";
        xxx = xxx + "(select obj.obj_key from pop pop, obj obj where pop.pol_key = " + policy.key() + " and pop.obj_key = obj.obj_key)";

        tcDataSet tcdataset1 = new tcDataSet();
        tcdataset1.setQuery(this.task.provider(), xxx);
        tcdataset1.executeQuery();
        for (int k = 0; k < tcdataset1.getRowCount(); k++) {
          tcdataset1.goToRow(k);
          String policyObjectKey    = tcdataset1.getString("obj_key").trim();
          String policyObjectName   = tcdataset1.getString("obj_name").trim();
          String policyDenial       = tcdataset1.getString("pop_denial").trim();
          String policyObjectRevoke = tcdataset1.getString("pop_revoke_object");
          if (policyObjectRevoke == null || policyObjectRevoke.equals(""))
            policyObjectRevoke = "0";
          else
            policyObjectRevoke       = policyObjectRevoke.trim();
          String objectAllowAll      = tcdataset1.getString("obj_allowall").trim();
          String objectAllowMultiple = tcdataset1.getString("obj_allow_multiple").trim();
          addObject(dataPolicyKey, dataPolicyName, dataPolicyPriority, policyObjectKey, policyObjectName, policyDenial, policyObjectRevoke, objectAllowAll, objectAllowMultiple, dataPolicyRequest);
        }
      }

      for (Enumeration enumeration = this.deny.keys(); enumeration.hasMoreElements(); this.allow.remove(enumeration.nextElement()));

      updateAccessPolicyProfile(account);
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   joinEntitlementWithPolicy
  public void join(final long policyKey, final ApplicationEntity server, long objectInstance)
    throws Exception {

    final String instance = String.valueOf(objectInstance);
    tcUDProcess  dataHandler= new tcUDProcess(this, "UD_LDS_UGP", instance, "UD_LDS_UGP_KEY", "", new String[] { "orc_key" }, new String[] { instance }, new byte[0]);
    save(dataHandler, policyKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   joinEntitlementWithPolicy
  public void joinEntitlementWithPolicy(final long policyKey, final ApplicationEntity server, long objectInstance, final long key)
    throws Exception {

    StringBuilder query = new StringBuilder();
/*
    query.append("select distinct sdc.sdk_key, poc_record_number, sdk.sdk_name, ");
    query.append("sdc_name, sdc_variant_type, sdc_version, poc_field_value, ");
    query.append("parent_sdk.sdk_key as parent_sdk_key, ");
    query.append("parent_sdk.sdk_name as parent_sdk_name, ");
    query.append("parent_sdk.sdk_active_version as parent_sdk_active_version ");
    query.append("from sdh,sdc,poc,sdk,sdk parent_sdk ");
    query.append("where sdc.sdk_key=sdh_child_key and ");
    query.append("sdk.sdk_type='P' and poc.pol_key=");
    query.append(policyKey);
    query.append(" and sdc.sdk_key=sdk.sdk_key ");
    query.append("and sdh_parent_version=parent_sdk.sdk_active_version ");
    query.append("and sdh_child_version = sdk.sdk_active_version ");
    query.append("and sdc_version=sdh_child_version ");
    query.append("and sdh.sdh_parent_key=poc.poc_parent_sdk_key ");
    query.append("and parent_sdk.sdk_key=poc.poc_parent_sdk_key ");
    query.append("and poc_field_name=sdc_name and poc.obj_key=");
    query.append(server[0].key());
    query.append(" order by sdc.sdk_key, poc_record_number");
*/
    query.append("select poc.poc_record_number, sdk.sdk_name ");
    query.append("from sdh sdh,poc poc,sdk sdk, sdk sdp ");
    query.append("where sdk.sdk_type='P' and poc.pol_key=");
    query.append(policyKey);
    query.append("and poc.sdk_key=sdk.sdk_key");
    query.append("and sdh_parent_version=sdp.sdk_active_version ");
    query.append("and sdh_child_version=sdk.sdk_active_version ");
    query.append("and sdh.sdh_parent_key=poc.poc_parent_sdk_key ");
    query.append("and sdp.sdk_key=poc.poc_parent_sdk_key ");
    query.append("and poc.obj_key=");
    query.append(server.key());
    query.append(" order by poc_record_number");

    tcDataSet childDataSet = new tcDataSet();
    childDataSet.setQuery(this.task.provider(), query.toString());
    childDataSet.executeQuery();
    if (childDataSet.getRowCount() > 0) {
      String       currentFormName  = SystemConstant.EMPTY;
      String       currentPocRecord = SystemConstant.EMPTY;
      tcUDProcess  dataHandler      = null;
      for (int i = 0; i < childDataSet.getRowCount(); i++) {
        childDataSet.goToRow(i);
        String childFormName  = childDataSet.getString("sdk_name");
        String pocRecord      = childDataSet.getString("poc_record_number");
        if (!childFormName.equalsIgnoreCase(currentFormName) || !pocRecord.equalsIgnoreCase(currentPocRecord)) {
          String childFormKey     = childFormName + "_key";
          // if we detect a change in the name of the form the we have to save
          // the current state at first before we can go forward with the next
          // child form
          if (dataHandler != null)
            save(dataHandler, policyKey);

          // create a data handler
          dataHandler = new tcUDProcess(this, childFormName, String.valueOf(objectInstance), childFormKey, "", new String[] { "orc_key" }, new String[] { String.valueOf(objectInstance) }, new byte[0]);
          // ensure that we write the data without triggering any per/post
          // processing
          dataHandler.setReconciliationFlag(true);
          dataHandler.setLong("pol_key", policyKey);
          dataHandler.setChildTableFlag(true);

          // prepare the control variables for the next iteration step
          currentFormName  = childFormName;
          currentPocRecord = pocRecord;
        }
        if (!pocRecord.equalsIgnoreCase(currentPocRecord))
          continue;
      }
      if (dataHandler != null)
        save(dataHandler, policyKey);
    }
  }

  private static tcDataSet getUDInfo(String parentFormName, final long objectInstance, final long policyKey, tcDataProvider provider)
    throws tcDataSetException {

    StringBuilder query = new StringBuilder();
    query.append("select ").append(parentFormName + "_key");
    query.append(',').append(parentFormName + "_rowver");
    query.append(',').append(parentFormName + "_version");
    query.append(" from ").append(parentFormName);
    query.append(" where orc_key = ").append(objectInstance);
    if (policyKey != -1L)
      query.append(" and pol_key = ").append(policyKey);

    tcDataSet dataSet = null;
    dataSet = new tcDataSet();
    dataSet.setQuery(provider, query.toString());
    dataSet.executeQuery();
    return dataSet;
  }

  private void updateAccessPolicyProfile(final Account account)
    throws ResolverException {

    tcDataSet dataSet = new tcDataSet();
    dataSet.setQuery(this.task.provider(), "select * from upp where usr_key = " + account.key());
    try {
      dataSet.executeQuery();
      if (dataSet.getRowCount() == 0) {
        if (this.policies.size() == 0)
          return;

        saveUpp(account);
        dataSet.setQuery(this.task.provider(), "select * from upp where usr_key = " + account.key());
        dataSet.executeQuery();
        addUpdEntries(dataSet.getString("upp_key"));
        return;
      }

      tcUPH tcuph = new tcUPH(this, "", String.valueOf(account.key()), new byte[0]);
      tcuph.setString("uph_allow_list",         dataSet.getString("upp_allow_list"));
      tcuph.setString("uph_deny_list",          dataSet.getString("upp_deny_list"));
      tcuph.setString("uph_org_not_allow_list", dataSet.getString("upp_org_not_allow_list"));
      tcuph.save();
      copyUpdToUhd(dataSet.getString("upp_key"), tcuph.getString("uph_key"));
      deleteUpdEntries(dataSet.getString("upp_key"));
      addUpdEntries(dataSet.getString("upp_key"));
      saveUpp(account);
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
  }

  private void saveUpp(final Account account)
    throws ResolverException {

    tcDataSet dataSet = new tcDataSet();
    dataSet.setQuery(this.task.provider(), "select upp_key, usr_key, upp_rowver from upp where usr_key = " + account.key());
    try {
      dataSet.executeQuery();
      tcUPP upp;
      if (dataSet.getRowCount() == 0)
        upp = new tcUPP(this, "", String.valueOf(account.key()), new byte[0]);
      else
        upp = new tcUPP(this, dataSet.getString("upp_key"), dataSet.getString("usr_key"), dataSet.getByteArray("upp_rowver"));
      Enumeration enumeration = this.allow.elements();
      Vector<String> vector = new Vector<String>();
      StringBuilder stringbuffer = new StringBuilder();
      for (; enumeration.hasMoreElements(); stringbuffer.delete(0, stringbuffer.length())) {
        String as[] = (String[])enumeration.nextElement();
        stringbuffer.append(as[0]);
        stringbuffer.append(" [");
        stringbuffer.append(as[5]);
        if (this.allowExtra.containsKey(as[0])) {
          HashMap hashmap = (HashMap)this.allowExtra.get(as[0]);
          String as2[];
          for (Iterator iterator1 = hashmap.keySet().iterator(); iterator1.hasNext(); stringbuffer.append(as2[1])) {
            String s = (String)iterator1.next();
            as2 = (String[])hashmap.get(s);
            stringbuffer.append(':');
          }
        }
        stringbuffer.append(']');
        vector.add(stringbuffer.toString());
      }

      HashSet<String> hashset = new HashSet<String>();
      String as1[];
      for (Iterator enumeration1 = this.deny.entrySet().iterator(); enumeration1.hasNext(); hashset.add(as1[0]))
        as1 = (String[])enumeration1.next();

      for (Iterator enumeration2 = this.orgDeny.entrySet().iterator(); enumeration2.hasNext(); hashset.add((String)enumeration2.next()))
        ;
      Iterator<String> iterator = hashset.iterator();
      Vector<String> vector1 = new Vector<String>();
      for (; iterator.hasNext(); vector1.add(iterator.next()))
        ;
      upp.setString("upp_allow_list",         createList(vector.elements()));
      upp.setString("upp_deny_list",          createList(vector1.elements()));
      upp.setString("upp_org_not_allow_list", createList(this.orgDeny.elements()));
      upp.save();
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
  }

  private void copyUpdToUhd(final String groupKey, String historyKey)
    throws ResolverException {

    tcDataSet dataSet = new tcDataSet();
    dataSet.setQuery(this.task.provider(), "select * from upd where upp_key = " + groupKey);
    try {
      dataSet.executeQuery();
      for (int i = 0; i < dataSet.getRowCount(); i++) {
        dataSet.goToRow(i);
        tcUHD uhd = new tcUHD(this, historyKey, dataSet.getString("pol_key"), new byte[0]);
        uhd.setString("uhd_allow_list", dataSet.getString("upd_allow_list"));
        uhd.setString("uhd_deny_list",  dataSet.getString("upd_deny_list"));
        uhd.save();
      }
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
  }

  private void deleteUpdEntries(String groupKey)
    throws ResolverException {

    tcDataSet tcdataset = new tcDataSet();
    tcdataset.setQuery(this.task.provider(), "select * from upd where upp_key = " + groupKey);
    try {
      tcdataset.executeQuery();
      for (int i = 0; i < tcdataset.getRowCount(); i++) {
        tcdataset.goToRow(i);
        String s1 = tcdataset.getString("pol_key");
        tcUPD upd = new tcUPD(this, tcdataset.getString("upp_key"), s1, tcdataset.getByteArray("upd_rowver"));
        AuditEngine auditengine = AuditEngine.getAuditEngine(this.task.provider());
        auditengine.pushReason("Access Policy", Long.parseLong(s1));
        upd.delete();
        auditengine.popReason();
      }
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
  }

  private void addUpdEntries(final String groupKey) {
    AuditEngine auditengine;
    Iterator i = this.allPoliciesThatApply.keySet().iterator();
    while (i.hasNext()) {
      String policyKey = (String)i.next();
      String as[] = (String[])this.allPoliciesThatApply.get(policyKey);
      tcUPD upd = new tcUPD(this, groupKey, policyKey, new byte[0]);
      upd.setString("upd_allow_list", as[0]);
      upd.setString("upd_deny_list",  as[1]);
      auditengine = AuditEngine.getAuditEngine(this.task.provider());
      auditengine.pushReason("Access Policy", Long.parseLong(policyKey));
      upd.save();
      auditengine.popReason();
    }
  }

  private void addObject(final String policyKey, final String policyName, long l, String objectKey, String objectName, String s4, String s5, String s6, String s7, String s8) {
    updatePolicyList(policyKey, s4, objectName);
    updateDirectProvisionList(objectKey, s8);
    if (s4.equals("1")) {
      this.deny.put(objectKey, new String[] { objectName, policyKey, policyName });
    }
    else {
      if (!this.orgObjects.contains(objectKey) && !s6.equals("1")) {
        this.orgDeny.put(objectKey, objectName);
        return;
      }
      if (this.allow.containsKey(objectKey)) {
        String as[] = (String[])this.allow.get(objectKey);
        String s9 = as[1];
        String s10 = as[4];
        String s11 = as[5];
        String s12 = as[6];
        long l1 = Long.parseLong(as[6]);
        if (s9.equals("0") || s5.equals("0"))
          as[1] = "0";
        this.allow.put(objectKey, as);
        if (l < l1) {
          if (this.policies != null && this.policies.containsKey(s10))
            this.policies.remove(s10);
          this.allow.put(objectKey, new String[] { objectName, s5, s6, s7, policyKey, policyName, Long.toString(l) });
          if (this.allowExtra.containsKey(objectName)) {
            HashMap hashmap = (HashMap)this.allowExtra.get(objectName);
            String as3[] = { s10, s11, s12 };
            hashmap.put(s10, as3);
            this.allowExtra.put(objectName, hashmap);
          }
          else {
            String as1[] = { s10, s11, s12 };
            HashMap hashmap2 = new HashMap();
            hashmap2.put(s10, as1);
            this.allowExtra.put(objectName, hashmap2);
          }
        }
        else {
          this.policies.remove(policyKey);
          if (this.allowExtra.containsKey(objectKey)) {
            HashMap hashmap1 = (HashMap)this.allowExtra.get(objectName);
            String as4[] = { policyKey, policyName, Long.toString(l) };
            hashmap1.put(s10, as4);
            this.allowExtra.put(objectName, hashmap1);
          }
          else {
            String as2[] = { policyKey, policyName, Long.toString(l) };
            HashMap hashmap3 = new HashMap();
            hashmap3.put(s10, as2);
            this.allowExtra.put(objectName, hashmap3);
          }
        }
      }
      else {
        if (s5 == null || s5.equals(""))
          s5 = "0";
        this.allow.put(objectKey, new String[] { objectName, s5, s6, s7, policyKey, policyName, Long.toString(l) });
      }
    }
  }

  private void updatePolicyList(String policyKey, String s1, String s2) {
    String s3;
    String s4;
    String as[];
    if (this.policies.containsKey(policyKey)) {
      as = (String[])this.policies.get(policyKey);
      s3 = as[0];
      s4 = as[1];
    }
    else {
      as = new String[2];
      s3 = "";
      s4 = "";
    }
    if (!s1.equals("1")) {
      if (s3.equals(""))
        s3 = s2;
      else
        s3 = s3 + "," + s2;
    }
    else if (s4.equals(""))
      s4 = s2;
    else
      s4 = s4 + "," + s2;
    as[0] = s3;
    as[1] = s4;
    this.policies.put(policyKey, as);
    this.allPoliciesThatApply.put(policyKey, as);
  }

  private void updateDirectProvisionList(String s, String s1) {
    String s2;
    if (s1.equals("1"))
      s2 = "R";
    else
      s2 = "D";
    Vector vector;
    if (!this.dirObjects.containsKey(s)) {
      vector = new Vector();
      vector.addElement(s2);
    }
    else {
      vector = (Vector)this.dirObjects.get(s);
      vector.addElement(s2);
    }
    this.dirObjects.put(s, vector);
  }

  private Vector<String> getAllowListforOrg(final Account account)
    throws ResolverException {

    tcDataSet tcdataset = new tcDataSet();
    tcdataset.setQuery(this.task.provider(), "select obj.obj_key, obj.obj_allowall from usr usr, acp acp, obj obj where usr.act_key = acp.act_key and acp.obj_key = obj.obj_key and usr.usr_key = " + account.key());
    try {
      tcdataset.executeQuery();
      Vector<String> list = new Vector<String>();
      for (int i = 0; i < tcdataset.getRowCount(); i++) {
        tcdataset.goToRow(i);
        list.addElement(tcdataset.getString("obj_key"));
      }
      return list;
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
  }


 public static void save(final tcUDProcess dataHandler, final long policyKey) {

    dataHandler.setReconciliationFlag(true);
    dataHandler.setLong("pol_key", policyKey);
    dataHandler.setChildTableFlag(true);

    AuditEngine auditengine = AuditEngine.getAuditEngine(dataHandler.getDataBase());
    auditengine.pushReason("Access Policy", policyKey);
    dataHandler.save();
    auditengine.popReason();
  }

  private static String createList(Enumeration iterator) {
    String s = SystemConstant.EMPTY;
    while (iterator.hasMoreElements())
      if (s.equals(SystemConstant.EMPTY))
        s = (String)iterator.nextElement();
      else
        s = s + "," + (String)iterator.nextElement();
    return s;
  }
}