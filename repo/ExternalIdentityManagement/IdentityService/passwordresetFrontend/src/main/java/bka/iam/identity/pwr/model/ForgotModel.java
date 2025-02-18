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

    File        :   ForgotModel.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    ForgotModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.model;

/**
 * A model class to hold the data bindings for the page forgot.jsf.
 */
public class ForgotModel extends BaseModel {

  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long serialVersionUID = 3906337579465931686L;

  /** To hold the binding for input <em>e-Mail address</em>. */
  private String email;

  /** To hold the binding for input <em>Captcha</em>. */
  private String captcha;

  private boolean audio;

  /**
   * Returns the value of input <em>e-Mail address</em>.
   * @return the value of input <em>e-Mail address</em>
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the value for input <em>e-Mail address</em>.
   * @param email the value of the input field.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the value of input <em>Captcha</em>.
   * @return the value of input <em>Captcha</em>.
   */
  public String getCaptcha() {
    return captcha;
  }

  /**
   * Sets the value for input <em>Captcha</em>.
   * @param captcha the value of input <em>Captcha</em>.
   */
  public void setCaptcha(String captcha) {
    this.captcha = captcha;
  }

  public boolean isAudio() {
    return audio;
  }

  public void setAudio(boolean audio) {
    this.audio = audio;
  }
}

