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

    File        :   Application.java

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
 // class Application
 // ~~~~~ ~~~~~~~~
 /**
  ** Java Bean represents OAM OAuth <code>Application</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class Application implements Serializable{
  
  private String resourceServerId;
  private String idDomain;
  private String name;
  private String description;
  private String resServerType;
  private String resourceServerNameSpacePrefix;
  private List<Scope> scopes;
  private List<TokenAttribute> tokenAttributes;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> java bean used as OAuth <code>Application</code> value object
   ** <br>
   ** Default Constructor
   */
  public Application() {
    super();
    
    scopes = new ArrayList<Scope>();
    tokenAttributes = new ArrayList<TokenAttribute>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> java bean used as OAuth <code>Application</code> value object
   ** @param idDomain OAuth Identity Domain ID
   ** @param name OAuth Identity Domain Name
   ** @param description OAuth Identity Domain Description
   */
  public Application(String idDomain, String name, String description) {
    this(idDomain,name, description,
         new ArrayList<Scope>(), 
         new ArrayList<TokenAttribute>());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> java bean used as OAuth <code>Application</code> value object
   ** @param idDomain OAuth Identity Domain ID
   ** @param name OAuth Identity Domain Name
   ** @param description OAuth Identity Domain Description
   ** @param scopes OAuth Identity Domain Scopes
   ** @param tokenAttributes OAuth Identity Domain Token Attributes
   */
  public Application(String idDomain, String name, String description, List<Scope> scopes,
                     List<TokenAttribute> tokenAttributes) {
    super();
    this.idDomain = idDomain;
    this.name = name;
    this.description = description;
    this.scopes = scopes;
    this.tokenAttributes = tokenAttributes;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Getter and Setter method of Java Bean
  //////////////////////////////////////////////////////////////////////////////
  
  public void setResourceServerId(String resourceServerId) {
    this.resourceServerId = resourceServerId;
  }

  public String getResourceServerId() {
    return resourceServerId;
  }

  public void setIdDomain(String idDomain) {
    this.idDomain = idDomain;
  }

  public String getIdDomain() {
    return idDomain;
  }

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

  public void setResServerType(String resServerType) {
    this.resServerType = resServerType;
  }

  public String getResServerType() {
    return resServerType;
  }

  public void setResourceServerNameSpacePrefix(String resourceServerNameSpacePrefix) {
    this.resourceServerNameSpacePrefix = resourceServerNameSpacePrefix;
  }

  public String getResourceServerNameSpacePrefix() {
    return resourceServerNameSpacePrefix;
  }

  public void setScopes(List<Scope> scopes) {
    this.scopes = scopes;
  }

  public List<Scope> getScopes() {
    return scopes;
  }

  public void setTokenAttributes(List<TokenAttribute> tokenAttributes) {
    this.tokenAttributes = tokenAttributes;
  }

  public List<TokenAttribute> getTokenAttributes() {
    return tokenAttributes;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Override method of Java Bean
  //////////////////////////////////////////////////////////////////////////////
  
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append("resourceServerId=").append(this.resourceServerId).append(", ");
    sb.append("name=").append(this.name).append(", ");
    sb.append("description=").append(this.description).append(", ");
    sb.append("idDomain=").append(this.idDomain).append(", ");
    sb.append("resServerType=").append(this.resServerType).append(", ");
    sb.append("resourceServerNameSpacePrefix=").append(this.resourceServerNameSpacePrefix).append(", ");    
    sb.append("scopes=").append(this.scopes).append(", ");
    sb.append("tokenAttributes=").append(this.tokenAttributes);
    sb.append("]");
    return sb.toString();
  }

}
