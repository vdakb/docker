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

    File        :   IdentityDomain.java

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

import java.util.ArrayList;
import java.util.List;

////////////////////////////////////////////////////////////////////////////////
 // class IdentityDomain
 // ~~~~~ ~~~~~~~~
 /**
  ** Java Bean represents OAM OAuth <code>Identity Domain</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class IdentityDomain implements Serializable{
  
  private String name;
  private String description;
  private String consentPageURL;
  private String errorPageURL;
  private String identityProvider;

  private List<Attribute> customAttrs;
  private List<TokenSetting> tokenSettings;
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Identity Domain</code> java bean used as OAuth <code>Identity Domain</code> value object
   ** <br>
   ** Default Constructor
   */
  public IdentityDomain() {
    this(null,null,null,null,null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Identity Domain</code> java bean used as OAuth <code>Identity Domain</code> value object
   ** @param name OAuth Identity Domain Name  
   ** @param description OAuth Identity Domain Description
   ** @param consentPageURL OAuth Identity Domain Consent Page URL
   ** @param errorPageURL OAuth Identity Domain Error Page URL
   ** @param identityProvider OAuth Identity Domain Identity Provider
   */
  public IdentityDomain(String name, 
                        String description, 
                        String consentPageURL, 
                        String errorPageURL,
                        String identityProvider) {
    this(name,description, consentPageURL, errorPageURL, identityProvider,null,null );
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Identity Domain</code> java bean used as OAuth <code>Identity Domain</code> value object
   ** @param name OAuth Identity Domain Name  
   ** @param description OAuth Identity Domain Description
   ** @param consentPageURL OAuth Identity Domain Consent Page URL
   ** @param errorPageURL OAuth Identity Domain Error Page URL
   ** @param identityProvider OAuth Identity Domain Identity Provider
   ** @param customAttrs OAuth Custom Attributes
   ** @param tokenSettings OAuth Token Setting
   */
  public IdentityDomain(String name, 
                        String description, 
                        String consentPageURL, 
                        String errorPageURL,
                        String identityProvider, 
                        List<Attribute>  customAttrs, 
                        List<TokenSetting> tokenSettings) {
    super();
    this.name = name;
    this.description = description;
    this.consentPageURL = consentPageURL;
    this.errorPageURL = errorPageURL;
    this.identityProvider = identityProvider;
    this.customAttrs = (customAttrs == null? new ArrayList<Attribute> () : customAttrs);
    if(this.tokenSettings == null){
      List<TokenSetting> defaultTokenSettings;
      defaultTokenSettings = new ArrayList<TokenSetting>();
      defaultTokenSettings.add(new TokenSetting("ACCESS_TOKEN"  ,3600,false,true,86400,false));
      defaultTokenSettings.add(new TokenSetting("AUTHZ_CODE"    ,3600,false,true,86400,false));
      defaultTokenSettings.add(new TokenSetting("SSO_LINK_TOKEN",3600,false,true,86400,false));
      this.tokenSettings = tokenSettings;
    }

  }

  //////////////////////////////////////////////////////////////////////////////  
  // Getter and Setter method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setConsentPageURL(String consentPageURL) {
    this.consentPageURL = consentPageURL;
  }

  public String getConsentPageURL() {
    return consentPageURL;
  }

  public void setErrorPageURL(String errorPageURL) {
    this.errorPageURL = errorPageURL;
  }

  public String getErrorPageURL() {
    return errorPageURL;
  }

  public void setIdentityProvider(String identityProvider) {
    this.identityProvider = identityProvider;
  }

  public String getIdentityProvider() {
    return identityProvider;
  }

  public void setCustomAttrs(List<Attribute>  customAttrs) {
    this.customAttrs = customAttrs;
  }

  public List<Attribute>  getCustomAttrs() {
    return customAttrs;
  }

  public void setTokenSettings(List<TokenSetting> tokenSettings) {
    this.tokenSettings = tokenSettings;
  }

  public List<TokenSetting> getTokenSettings() {
    return tokenSettings;
  }

  @Override
  public boolean equals(Object obj) {
    boolean eq = false;    
    if(obj != null 
       && obj instanceof IdentityDomain
       && this.name.equals(((IdentityDomain)obj).getName())){
      eq = true;
    }
    return eq;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Override method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append("name=").append(this.name).append(", ");
    sb.append("description=").append(this.description).append(", ");
    sb.append("consentPageURL=").append(this.consentPageURL).append(", ");
    sb.append("errorPageURL=").append(this.errorPageURL).append(", ");
    sb.append("customAttrs=").append(this.customAttrs).append(", ");
    sb.append("tokenSettings=").append(this.tokenSettings).append(", ");
    sb.append("]");
    return sb.toString();
  }


}
