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

    File        :   PasswordReset.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    PasswordReset.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The PasswordReset class is a Java Bean to hold the values of the PWR database table.
 */
public class PasswordReset implements Serializable {

  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long serialVersionUID = -7192525394674617114L;

  /** ID of the entry */
  private Long id;

  /** pwrId, UUID to identify the request */
  private String requestId;

  /** when the request was created */
  private LocalDateTime created;

  /** the e-mail address where the notification was sent */
  private String email;

  /**
   * Returns the id of the entry.
   * @return the id of the entry.
   */
  public Long id() {
    return id;
  }

  /**
   * Sets the id of the entry.
   * @param id the id of the entry.
   */
  public void id(Long id) {
    this.id = id;
  }

  /**
   * Returns the pwrId of the entry.
   * @return the pwrId of the entry.
   */
  public String requestId() {
    return requestId;
  }

  /**
   * Sets the pwrId of the entry.
   * @param requestId the pwrId of the entry.
   */
  public void requestId(String requestId) {
    this.requestId = requestId;
  }

  /**
   * Returns the created attribute of the entry.
   * @return the created attribute of the entry.
   */
  public LocalDateTime created() {
    return created;
  }

  /**
   * Sets the created attribute of the entry.
   * @param created the created attribute of the entry.
   */
  public void created(LocalDateTime created) {
    this.created = created;
  }

  /**
   * Returns the email attribute of the entry.
   * @return the email attribute of the entry.
   */
  public String email() {
    return this.email;
  }

  /**
   * Sets the email attribute of the entry.
   * @param email the email attribute of the entry.
   */
  public void email(final String email) {
    this.email = email;
  }

  /**
   * Returns a string value of the fields <em>requestId</em>, <em>created</em>, <em>email</em>.
   */
  @Override
  public String toString() {
    return "PasswordReset{" +
        "requestId='" + requestId + '\'' +
        ", created=" + created +
        ", email='" + email + '\'' +
        '}';
  }
}
