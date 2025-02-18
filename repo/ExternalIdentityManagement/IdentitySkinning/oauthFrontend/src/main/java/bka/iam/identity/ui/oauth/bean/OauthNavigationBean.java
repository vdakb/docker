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

    File        :   OauthNavigationBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.bean;

import bka.iam.identity.ui.oauth.utils.ADFFacesUtils;

import java.io.Serializable;

import java.util.logging.Logger;

import javax.faces.event.ActionEvent;

////////////////////////////////////////////////////////////////////////////////
// class OauthNavigationBean
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attribute
 ** values of <code>Oauth Navigation</code>.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OauthNavigationBean implements Serializable {
  
  // Logger related static attributes
  private static final String className = OauthNavigationBean.class.getName();
  private static Logger       logger = Logger.getLogger(className);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateOAMServers
  /**
   ** Lanch OAM Servers taskflow, this method is called from cutom ADF Tile
   ** @param evt
   */
  public void navigateOAMServers(ActionEvent evt) {
    String methodName = "navigateOAMServers";
    logger.entering(className, methodName);
    
    ADFFacesUtils.launchTaskFlow("OAM", 
                                 "/WEB-INF/bka/iam/identity/ui/oauth/tfs/oam-servers-tf.xml#oam-servers-tf",
                                 "OAM", 
                                 null, null, null, false, null);
    
    logger.exiting(className, methodName);
  }


  public void navigateIdentityDomain(ActionEvent evt) {
    navigateOAMServers(evt);
  }


}


