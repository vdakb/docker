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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Self Service Portal
    Subsystem   :   Vehicle Administration

    File        :   MemberState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    MemberState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-07-09  SBernet     First release version
*/
package bka.employee.portal.vehicle.vmb.backing;

import bka.employee.portal.train.state.AbstractSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.jbo.Row;

////////////////////////////////////////////////////////////////////////////////
// class AttributeState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage member
 ** of <code>Vehicle Brand Manufactor</code>s.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MemberState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String           DETAIL_ITERATOR  = "VehiculeIterator";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4088657162863695297")
  private static final long            serialVersionUID = 7223658026511031118L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MemberState</code> backing bean that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MemberState() {
    // ensure inheritance
    super();
  }
  
  public String userDetail() {
    List<Row> list = selectedRow(getSearchTable());
    if (list == null || list.size() == 0)
      return null; 
    
    final Row                 row         = list.get(0);
    final String              userKey     = row.getAttribute("usrKey").toString();
    final String              taskFlowId  = "user_detail" + userKey;
    final Map                 hashMap     = new HashMap<>();
    hashMap.put("usr_key", userKey);
    hashMap.put("confirmMessage", Boolean.valueOf(false));
    
    raiseTaskFlowLaunchEvent(taskFlowId, "/WEB-INF/oracle/iam/ui/manageusers/tfs/user-details-tf.xml#user-details-tf", row.getAttribute("carPlateNumber").toString(), null, null, ConstantsDefinition.HELP_TOPIC_ID_SELF_IA_MANGUSERS, false, hashMap);
    return null;
  }
}
