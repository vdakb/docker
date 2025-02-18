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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   OAuth Registration

    File        :   TokenAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.model;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
 // class TokenAttribute
 // ~~~~~ ~~~~~~~~
 /**
  ** Java Bean represents OAM OAuth <code>TokenAttribute</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class TokenAttribute implements Serializable{
  private String attrName;
  private String attrValue;
  private String attrType;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TokenAttribute</code> java bean used as OAuth <code>Token Attribute</code> value object
   ** <br>
   ** Default Constructor
   */
  public TokenAttribute(){
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TokenAttribute</code> java bean used as OAuth <code>Token Attribute</code> value object
   ** @param attrName Attribute Name
   ** @param attrValue Attriute Value
   ** @param attrType Attribute Type
   */
  public TokenAttribute(String attrName, String attrValue, String attrType) {
    super();
    this.attrName = attrName;
    this.attrValue = attrValue;
    this.attrType = attrType;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Getter and Setter method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  public void setAttrName(String attrName) {
    this.attrName = attrName;
  }

  public String getAttrName() {
    return attrName;
  }

  public void setAttrValue(String attrValue) {
    this.attrValue = attrValue;
  }

  public String getAttrValue() {
    return attrValue;
  }

  public void setAttrType(String attrType) {
    this.attrType = attrType;
  }

  public String getAttrType() {
    return attrType;
  }
  
  //////////////////////////////////////////////////////////////////////////////  
  // Override method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append("attrName=").append(this.attrName).append(", ");
    sb.append("attrValue=").append(this.attrValue).append(", ");
    sb.append("attrType=").append(this.attrType);
    sb.append("]");
    return sb.toString();
  }
  

}
