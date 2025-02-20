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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   Special Account Request

    File        :   AccountFlow.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    AccountFlow.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-03-03  SBernet    First release version
*/

package bka.iam.identity.ui.request.backing;

import java.util.Map;
import java.util.HashMap;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.AbstractManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class AccountFlow
// ~~~~~ ~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to request account
 ** across custom task flows.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccountFlow extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the Resource Bundle provided by the module specific implementation */
  private static final String BUNDLE            = "bka.iam.identity.resource.ConsoleBundle";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7380902549313089045")
  private static final long   serialVersionUID = -5529122077737264740L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountFlow</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountFlow() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchRequest
  /**
   ** Raise popup in order to request a specific special account.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   */
  public void launchRequest() {
    final Map                 scope = ADF.pageFlowScope();
    final Map<String, Object> param = new HashMap<>();
    param.put("beneficiaryKey", scope.get("userLogin"));
    raiseTaskFlowLaunchEvent("efbs-account", "/WEB-INF/oracle/iam/ui/custom/request/flow/efbs-account-tf.xml#efbs-account-tf", ADF.resourceBundleValue(BUNDLE, "FBS_TITLE"), "/images/func_add_16_ena.png", null, null, true, param);
  }
}