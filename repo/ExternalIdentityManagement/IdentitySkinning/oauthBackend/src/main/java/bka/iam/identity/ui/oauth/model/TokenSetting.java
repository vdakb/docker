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

    File        :   TokenSetting.java

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
 // class TokenSetting
 // ~~~~~ ~~~~~~~~
 /**
  ** Java Bean represents OAM OAuth <code>TokenSetting</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class TokenSetting implements Serializable{
  
  private String  tokenType;
  private int  tokenExpiry;
  private boolean  lifeCycleEnabled;
  
  private boolean refreshTokenEnabled;
  private int refreshTokenExpiry;
  private boolean refreshTokenLifeCycleEnabled;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TokenSetting</code> java bean used as OAuth <code>Token Setting</code> value object
   ** <br>
   ** Default Constructor
   */
  public TokenSetting() {
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> java bean used as OAuth <code>Application</code> value object
   ** @param tokenType Token Type
   ** @param tokenExpiry Token expire in seconds
   ** @param lifeCycleEnabled Token Life Cycle Enabled
   */
  public TokenSetting(String tokenType, 
                      int tokenExpiry, 
                      boolean lifeCycleEnabled) {
    this(tokenType,tokenExpiry,lifeCycleEnabled,false,0,false);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> java bean used as OAuth <code>Application</code> value object
   ** @param tokenType Token Type
   ** @param tokenExpiry Token expire in seconds
   ** @param lifeCycleEnabled Token Life Cycle Enabled
   ** @param refreshTokenEnabled Refresh Token Enabled
   ** @param refreshTokenExpiry Refresh Token expire in seconds
   ** @param refreshTokenLifeCycleEnabled Refresh Token  Life Cycle Enabled
   */
  public TokenSetting(String tokenType, 
                      int tokenExpiry, 
                      boolean lifeCycleEnabled,
                      boolean refreshTokenEnabled,
                      int refreshTokenExpiry, 
                      boolean refreshTokenLifeCycleEnabled) {
    super();
    this.tokenType = tokenType;
    this.tokenExpiry = tokenExpiry;
    this.lifeCycleEnabled = lifeCycleEnabled;
    this.refreshTokenEnabled = refreshTokenEnabled;
    this.refreshTokenExpiry = refreshTokenExpiry;
    this.refreshTokenLifeCycleEnabled = refreshTokenLifeCycleEnabled;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Getter and Setter method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenExpiry(int tokenExpiry) {
    this.tokenExpiry = tokenExpiry;
  }

  public int getTokenExpiry() {
    return tokenExpiry;
  }

  public void setLifeCycleEnabled(boolean lifeCycleEnabled) {
    this.lifeCycleEnabled = lifeCycleEnabled;
  }

  public boolean isLifeCycleEnabled() {
    return lifeCycleEnabled;
  }

  public void setRefreshTokenEnabled(boolean refreshTokenEnabled) {
    this.refreshTokenEnabled = refreshTokenEnabled;
  }

  public boolean isRefreshTokenEnabled() {
    return refreshTokenEnabled;
  }

  public void setRefreshTokenExpiry(int refreshTokenExpiry) {
    this.refreshTokenExpiry = refreshTokenExpiry;
  }

  public int getRefreshTokenExpiry() {
    return refreshTokenExpiry;
  }

  public void setRefreshTokenLifeCycleEnabled(boolean refreshTokenLifeCycleEnabled) {
    this.refreshTokenLifeCycleEnabled = refreshTokenLifeCycleEnabled;
  }

  public boolean isRefreshTokenLifeCycleEnabled() {
    return refreshTokenLifeCycleEnabled;
  }
  
  //////////////////////////////////////////////////////////////////////////////  
  // Override method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append("tokenType=").append(this.tokenType).append(", ");
    sb.append("tokenExpiry=").append(this.tokenExpiry).append(", ");
    sb.append("lifeCycleEnabled=").append(this.lifeCycleEnabled).append(", ");
    sb.append("refreshTokenEnabled=").append(this.refreshTokenEnabled).append(", ");
    sb.append("refreshTokenExpiry=").append(this.refreshTokenExpiry).append(", ");
    sb.append("refreshTokenLifeCycleEnabled=").append(this.refreshTokenLifeCycleEnabled);
    sb.append("]");
    return sb.toString();
  }
}
