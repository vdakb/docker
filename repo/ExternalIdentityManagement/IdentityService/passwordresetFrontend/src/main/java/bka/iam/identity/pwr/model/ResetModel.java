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

    File        :   ResetModel.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    ResetModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.model;

/**
 * A model class to hold the data bindings for the page <em>reset.jsf</em>.
 */
public class ResetModel extends BaseModel {

  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long serialVersionUID = 1114887067496704907L;

  /** To hold the binding for input <em>e-Mail address</em>. */
  private String email;

  /** To hold the binding for input <em>Login</em>. */
  private String login;

  /** To hold the binding for input <em>Password</em>. */
  private String password;

  /** To hold the binding for input <em>Confirmation</em>. */
  private String confirmation;

  /** Defines if form <em>reset-form</em> should be enabled or not.*/
  private boolean valid = false;

  private String passwordHint;

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
   * Returns the value of input <em>Login</em>.
   * @return the value of input <em>Login</em>
   */
  public String getLogin() {
    return login;
  }

  /**
   * Sets the value for input <em>Login</em>.
   * @param login the value of the input field.
   */
  public void setLogin(String login) {
    this.login = login;
  }

  /**
   * Returns the value of input <em>Password</em>.
   * @return the value of input <em>Password</em>
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the value for input <em>Password</em>.
   * @param password the value of the input field.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the value of input <em>Confirmation</em>.
   * @return the value of input <em>Confirmation</em>
   */
  public String getConfirmation() {
    return confirmation;
  }

  /**
   * Sets the value for input <em>Confirmation</em>.
   * @param confirmation the value of the input field.
   */
  public void setConfirmation(String confirmation) {
    this.confirmation = confirmation;
  }

  /**
   * Returns the disability of <em>reset-form</em>.
   * @return disability of <em>reset-form</em>.
   */
  public boolean isValid() {
    return valid;
  }

  /**
   * Sets the disability of <em>reset-form</em>.
   * @param valid form is disabled or not.
   */
  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public String getPasswordHint() {
    return passwordHint;
  }

  public void setPasswordHint(String passwordHint) {
    this.passwordHint = passwordHint;
  }
}
