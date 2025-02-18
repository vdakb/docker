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

    File        :   OrgEntitlement.java

    Compiler    :   JDK 1.8

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file impoment the class
                    OrgEntitlement.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.09.2024  TSebo    First release version
*/
package oracle.iam.identity.jes.integration.oig.aph.model;

import oracle.iam.provisioning.vo.Entitlement;

////////////////////////////////////////////////////////////////////////////////
// class OrgEntitlement
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrgEntitlement</code> is a data model holder classs.
 ** It holds entitlement with organization name, used in APHPolicyEvaluation.java
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OrgEntitlement {
  
  private Entitlement entitlement;
  private String orgName;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>OrgEntitlement</code>  object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrgEntitlement() {
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Create a OrgEntitlement object and set OIM Entitlement and Organizational Name
   ** @param entitlement OIM Entitlement
   ** @param orgName Organizational Name
   */
  public OrgEntitlement(Entitlement entitlement, String orgName) {
    super();
    this.entitlement = entitlement;
    this.orgName = orgName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  setEntitlement
  /**
   ** Set Entitlement
   ** @param entitlement OIM Entitlement
   */
  public void setEntitlement(Entitlement entitlement) {
    this.entitlement = entitlement;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  getEntitlement
  /**
   ** Get Entitlement
   ** @return OIM Entitlement
   */
  public Entitlement getEntitlement() {
    return entitlement;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  setOrgName
  /**
   ** Set Organization Name
   ** @param orgName Organization Name
   */
  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  getOrgName
  /**
   ** Get Organization Name
   ** @return Organization Name
   */
  public String getOrgName() {
    return orgName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Override)
  /**
   ** String representation of the object
   ** @return
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    if(this.entitlement != null){
      sb.append("EntitlementCode:").append(entitlement.getEntitlementCode()).append(",");
      sb.append("OrganizationName:").append(orgName);
    }
    return sb.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (Override)
  /**
   ** Compare the objects, if they are equals it return true, otherwise false
   ** @param object pompared object
   ** @return
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof OrgEntitlement)) {
      return false;
    }
    final OrgEntitlement other = (OrgEntitlement) object;
    if (!(entitlement == null ? other.entitlement == null : entitlement.getEntitlementCode().equals(other.entitlement.getEntitlementCode()))) {
      return false;
    }
    if (!(orgName == null ? other.orgName == null : orgName.equals(other.orgName))) {
      return false;
    }
    return true;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Override)
  /**
   ** Object has code, calculated based on the object attributes values
   ** @return ojbect has code
   */
  @Override
  public int hashCode() {
    final int PRIME = 37;
    int result = 1;
    result = PRIME * result + ((entitlement == null) ? 0 : entitlement.hashCode());
    result = PRIME * result + ((orgName == null) ? 0 : orgName.hashCode());
    return result;
  }
}