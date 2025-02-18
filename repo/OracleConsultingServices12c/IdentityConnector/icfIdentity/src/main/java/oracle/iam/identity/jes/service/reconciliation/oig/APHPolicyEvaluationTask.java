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
    Subsystem   :   BKA Access Policy Holder

    File        :   APHPolicyEvaluationTask.java

    Compiler    :   JDK 1.8

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file extends the class
                    AbstractSchedulerTask.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.09.2024  TSebo    First release version
*/

package oracle.iam.identity.jes.service.reconciliation.oig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.jes.integration.oig.aph.PolicyError;
import oracle.iam.identity.jes.integration.oig.aph.PolicyException;
import oracle.iam.identity.jes.integration.oig.aph.service.APHPolicyEvaluation;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMInternalClient;
import oracle.iam.platform.context.ContextAware;
import oracle.iam.platform.context.ContextAwareNumber;
import oracle.iam.platform.context.ContextAwareString;
import oracle.iam.platform.context.ContextManager;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

////////////////////////////////////////////////////////////////////////////////
// class APHPolicyEvaluationTask
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>APHPolicyEvaluationTask</code> is a scheduled task which trigger APH Policy Evaluation for users.
 ** In the parameter "Users List" can be provided list of the user logins separated by comma. In case this parameter
 ** is empty all users are evaluated. This task support multithreading in parameter "Number of Threads" is defined 
 ** how many thread should be used for user evaluation.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class APHPolicyEvaluationTask extends AbstractSchedulerTask {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  final static String className =  APHPolicyEvaluationTask.class.getName();
  
  /**
   ** Attribute tag which may be defined on this task to specify which
   ** OIM Users list, it defines comma separated list of user login names
   ** for which APH policy evaluation needs to be executed. In case this list
   ** is empty. APH policy evaluation is executed for all users.
   ** <br>
   ** This attribute is optional.
   */
  private static final String USERS_LIST                = "Users List";
  
  /**
   ** Attribute tag which may be defined on this task to specify 
   ** the total number of threads that will process re-evaluation
   ** <br>
   ** This attribute is mandatory
   */
  private static final String NUMBER_OF_THREADS         = "Number of Threads";
  
  /**
   ** Attribute tag which may be defined on this task to specify 
   ** time in minutes, after which the schedule task will stop
   ** <br>
   ** This attribute is mandatory
   */
  private static final String TIME_LIMIT_IN_MINS        = "Time Limit in mins";
  
  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {

    /** the task attribute to resolve list of user logins */
     TaskAttribute.build(USERS_LIST, TaskAttribute.OPTIONAL)
    /** the task attribute to resolve number of evaluation threads*/
   , TaskAttribute.build(NUMBER_OF_THREADS, TaskAttribute.MANDATORY)
    /** the task attribute to resolve maximum duration of the evaluation */
    , TaskAttribute.build(TIME_LIMIT_IN_MINS, TaskAttribute.OPTIONAL)
  };
  
  /**
   ** Queue of the users which needs to be evaluated 
   */
  Queue<String> evalUsers;
  
  /**
   ** OIM API for used to get information about users 
   */
  private UserManager userManager;
  
  /**
   ** Schedule job APHPolicyEvaluationTask constructor
   */
  public APHPolicyEvaluationTask() {
    super(className);
    userManager = service(UserManager.class);
    evalUsers   = new LinkedList<String>();
  }
  
  /**
   ** Return a array of Schduled task attributes.
   ** @return array of task attributes
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Method onExecution is the main method where the APH policy evaluation is executed. 
   ** Policy evaluation run in multiple threads, number of threads come from input parameter "Number of Threads"
   ** @throws TaskException in case OIM APIs throws a exception
   */
  @Override
  protected void onExecution() throws TaskException, TaskException {
    final String method  = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    
    int numberOfThreads  = integerValue(NUMBER_OF_THREADS);
    long timeLimitsInMins = integerValue(TIME_LIMIT_IN_MINS,0);
    
    // Load users from input parameter USERS_LIST or via all active users via API
    loadUsers(stringValue(USERS_LIST));
    
    // Initialize threads and executed them
    long jobStart = System.currentTimeMillis();
    
    long contextJobHistoryId = (Long)ContextManager.getValue("JOBHISTORYID");
    String contextJobName    = (String)ContextManager.getValue("JOBNAME");
    String contectTaskName   = (String)ContextManager.getValue("TASKNAME");
    
    // Multi thread version of code
    ExecutorService service = null;
    try {
      service = Executors.newFixedThreadPool(numberOfThreads);
      for (int i = 0; i < numberOfThreads; i++){
        service.submit( () -> {
          OIMInternalClient oimClient = null;
          try{
            // In multithreadin approach a new client needs to be created. 
            // Original context is lost.
            oimClient = new OIMInternalClient();
            // Login as OIM Administrator
            // NOTE: in case the oimClient uses OIMInternal then provisioning reason in ENT_ASSING would be RECONCILIATION
            oimClient.loginAsAdmin();             
            
            // Set OIM Context.
            // This is needed because it is running in the separated threads
            ContextManager.pushContext(null, ContextManager.ContextTypes.ADMIN, null);
            ContextManager.setValue("JOBHISTORYID", (ContextAware)new ContextAwareNumber(contextJobHistoryId));
            ContextManager.setValue("JOBNAME", (ContextAware)new ContextAwareString(contextJobName)); 
            ContextManager.setValue("TASKNAME", (ContextAware)new ContextAwareString(contectTaskName)); 
            ContextManager.setValue("operationInitiator", (ContextAware)new ContextAwareString("scheduler"));

            APHPolicyEvaluation aph = new APHPolicyEvaluation(oimClient);
            
            // Execute policy evaluation for all users.
            List<String> assingnedUsers = new ArrayList<String>();
            while (true) {
              String usr_key = getNextUserKey();
              if (usr_key != null) {
                trace(method,"Start Evaluation Policy for user usr_key: "+usr_key);
                long startTime = System.currentTimeMillis();
                aph.evaluatePolicyForUser(usr_key);
                long endTime = System.currentTimeMillis();
                trace(method,"End Evaluation Policy for user usr_key: "+usr_key+", it took "+(endTime-startTime)+" [ms]");
                assingnedUsers.add(usr_key);
              } 
              else {
                break;
              }
            }
            trace(method,getName()+": "+Thread.currentThread().getName() +": "+ assingnedUsers);
            } catch (Exception e) {
              error(method,"Exception in APH policy evalution in thread: "+ e);
            } finally {
              if (oimClient != null){
                oimClient.logout();
              }
            } 
        });
        
      }
    }
    finally {
      if (service != null) service.shutdown();
    }
    
    try{
      // Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs, or the current thread is interrupted, whichever happens first.
      if (!service.awaitTermination((timeLimitsInMins>0?timeLimitsInMins:Integer.MAX_VALUE), TimeUnit.MINUTES)) {
        service.shutdownNow();
      }
    }
    catch (InterruptedException e) {
      warning("Waiting for thread compleation was terminated: "+e);
    }
    
    info(this.getName() + ": APHPolicyEvaluation took "+(System.currentTimeMillis()-jobStart)+" [ms]");

    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Private methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNextUserKey
  /**
   ** Get next value from user queue. Returned value is removed from queue.
   ** @return Next user key (USR.USR_KEY) value. In case there is not value null is returned.
   */
  private synchronized String getNextUserKey(){
    return this.evalUsers.poll();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadUsers
  /**
   ** Load a users which needs to be evaluated. List of the users can come as input paramter @param userList. In case
   ** the input parameter is empty all active OIM users are loaded.
   ** @param userList Command separated list of user login where policy evaluation needs to be executed.
   ** @throws TaskException
   */
  private void loadUsers(String userList) throws TaskException {
    final String method  = "getUsers";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    if(userList != null && userList.trim().length()>0){
      // Split users from input parameter and add to users variable
      String[] slitUsers = userList.split(",");
      if(slitUsers != null &&  slitUsers.length >0){
        for(String user : slitUsers){
          evalUsers.add(getUserKey(user));
        }
      }
    }
    else{
      // Read all active users and add to the users variable
      List<User> activeUsers = getAllActiveUsers();
      if(activeUsers != null){
        for(User user : activeUsers){
          evalUsers.add(user.getId());
        }
      }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserKey
  /**
   ** Get OIM user key from OIM user login
   ** @param userLogin OIM user Login
   ** @return OIM user key
   ** @throws TaskException
   */
  private String getUserKey(String userLogin) throws TaskException {
    final String method  = "getUserKey";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    
    String usrKey = null;
    if(userLogin != null){
      userLogin = userLogin.trim();
      try {
        Set attrs = new HashSet();
        attrs.add(UserManagerConstants.AttributeName.USER_KEY.getId());
        User user = userManager.getDetails(userLogin, attrs, true);
        if(user != null){
          usrKey = user.getId();
        }
      } catch (NoSuchUserException | UserLookupException e) {
        throw new PolicyException(PolicyError.USER_NOTFOUND,userLogin);
      }
      finally{
        timerStop(method);
        trace(method, SystemMessage.METHOD_EXIT); 
      }
    }
    return usrKey;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllActiveUsers  
  /**
   ** Return all Active OIM users key. Disabled of Revoked users are not returned
   ** @return all active OIM users key
   ** @throws TaskException
   */
  private List<User> getAllActiveUsers() throws TaskException{
    final String method  = "getAllActiveUsers";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    
    List<User> activeUsers = null;
    // Search criteria for active users
    SearchCriteria sc = new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(), 
                                           UserManagerConstants.AttributeValues.USER_STATUS_ACTIVE.getId(),
                                           SearchCriteria.Operator.EQUAL);
    
    // Return values will contains OIM User Key (URS.USR_KEY)
    Set attrs = new HashSet();
    attrs.add(UserManagerConstants.AttributeName.USER_KEY.getId());

    try {
      activeUsers = this.userManager.search(sc, attrs, null);
    } catch (UserSearchException e) {
      throw new PolicyException(PolicyError.ACTIVE_USER_NOTFOUND);
    }
    finally{
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);  
    }
    return activeUsers;
  } 
}