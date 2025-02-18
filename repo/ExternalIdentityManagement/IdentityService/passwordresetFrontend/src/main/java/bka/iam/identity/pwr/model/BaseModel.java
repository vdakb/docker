/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   BaseModel.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    BaseModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.model;

import java.io.Serializable;

/**
 * A base class holds the bindings that are common on the pages.
 */
public abstract class BaseModel implements Serializable {

  /** The official serial version ID which says cryptically which version we're compatible with */
  static final long serialVersionUID = 8227872128143019419L;

  /** Jmx extension, a symbol refers an image to show based on the user's location. */
  private String symbol;

  /** Jmx extension, a requestIp refers the caller user's ip address. */
  private String requestIp;

  /**
   * Return the ip address of the client.
   * @return requestIp
   */
  public String getRequestIp() {
    return requestIp;
  }

  /**
   * Sets the ip address of the client.
   * @param requestIp the ip address
   */
  public void setRequestIp(String requestIp) {
    this.requestIp = requestIp;
  }

  /**
   * Returns the name of the image what has to be shown based on the client's location.
   * @return the name of the image.
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Sets the name of the image to show on the UIs, based on the client's location.
   * @param symbol the name of the image.
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
}
