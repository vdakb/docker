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

    Copyright © 2024 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   OAuth Registration

    File        :   OAuthEventHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.event;

import bka.iam.identity.ui.oauth.bean.ClientManagedBean;
import bka.iam.identity.ui.oauth.model.Scope;

import java.io.Serializable;

import java.util.List;
import java.util.logging.Logger;

import oracle.iam.ui.platform.utils.FacesUtils;
import oracle.iam.ui.platform.view.event.TargetSelectionEvent;

////////////////////////////////////////////////////////////////////////////////
// class OAuthEventHandler
// ~~~~~ ~~~~~~~~
/**
 ** ADF Event Handler used for OAuth Scope picker
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OAuthEventHandler {
  
  private static final String className = OAuthEventHandler.class.getName();
  private static Logger       logger = Logger.getLogger(className);
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: handleSelectTargets   
  /**
   ** ADF Event Handler method triggerd by ADF when a Scope picker is used
   ** @param targetSelectionEvent Event data.
   */
  public void handleSelectTargets(TargetSelectionEvent targetSelectionEvent) {
    String methodName = "handleSelectTargets";
    logger.entering(className, methodName);
    
    String distinguisher = targetSelectionEvent.getEventDistinguisher();
    logger.finer("distinguister="+distinguisher );
    
    if("pick_scopes".equals(distinguisher)){
      ClientManagedBean bean =  (ClientManagedBean) FacesUtils.getValueFromELExpression("#{pageFlowScope.clientMB}");
      List<Serializable> selectedTargets = targetSelectionEvent.getSelectedTargets();
      for(Serializable data : selectedTargets){
        bean.handleAddScope((Scope)data);
      }
    }
    else if("pick_scope".equals(distinguisher)){
      ClientManagedBean bean =  (ClientManagedBean) FacesUtils.getValueFromELExpression("#{pageFlowScope.clientMB}");
      List<Serializable> selectedTargets = targetSelectionEvent.getSelectedTargets();
      if(selectedTargets != null && selectedTargets.size() > 0){
        bean.handleAddDefaultScope((Scope)selectedTargets.get(0));
      }
    }
    else{
      logger.warning("Unexpected distinguisher: "+distinguisher);
    }
    
    logger.exiting(className, methodName);
  }
}
