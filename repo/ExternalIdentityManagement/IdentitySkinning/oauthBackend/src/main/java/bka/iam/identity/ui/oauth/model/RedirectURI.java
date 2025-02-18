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

    File        :   RedirectURI.java

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
 // class RedirectURI
 // ~~~~~ ~~~~~~~~
 /**
  ** Java Bean represents OAM OAuth <code>RedirectURI</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class RedirectURI implements Serializable{
  
  private String url;
  private boolean isHttps;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RedirectURI</code> java bean used as OAuth <code>Redirect URI</code> value object
   ** <br>
   ** Default Constructor
   */
  public RedirectURI(){
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RedirectURI</code> java bean used as OAuth <code>Redirect URI</code> value object
   ** @param url Redirect URL
   ** @param isHttps Flag if the URL is secure on not, true mean that https protocol is used
   */
  public RedirectURI(String url, boolean isHttps) {
    this.url = url;
    this.isHttps = isHttps;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Getter and Setter method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setIsHttps(boolean isHttps) {
    this.isHttps = isHttps;
  }

  public boolean isIsHttps() {
    return isHttps;
  }
  
  //////////////////////////////////////////////////////////////////////////////  
  // Override method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append("url=").append(this.url).append(", ");
    sb.append("isHttps=").append(this.isHttps);
    sb.append("]");
    return sb.toString();
  }
}
