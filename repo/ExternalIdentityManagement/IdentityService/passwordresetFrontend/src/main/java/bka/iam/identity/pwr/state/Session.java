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

    File        :   Session.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    Session.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.state;

import oracle.hst.platform.jsf.Faces;
import oracle.hst.platform.jsf.state.ManagedBean;
import java.util.Locale;

/**
 ** The <code>Session</code> bean to handle the Password Reset.
 ** <p>
 **
 ** The goal of this class is to facilitate access to the current user locale,
 ** and provide it to the pages.
 */
public class Session extends ManagedBean<Session> {

  /** the official serial version ID which says cryptically which version we're
   ** compatible with
   */
  private static final long   serialVersionUID = -3692155340653206257L;
  /** Name of the resource bundle to resolve messages from the controllers */
  public static final String  BUNDLE           = "pwr$bundle";
  /** The locale grabbed from the browser settings. */
  private final Locale        locale;

  /**
   ** Constructs a managed <code>Session</code> bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Session() {
    super();

    this.locale = Faces.externalContext().getRequestLocale();
  }

  /**
   ** Returns the <code>Session</code> {@link Locale}.
   **
   ** @return                    the <code>Session</code> {@link Locale}.
   **                            <br>
   **                            Possible object is {@link Locale}.
   */
  public Locale getLocale() {
    return this.locale;
  }

  /**
   ** Returns the language code of the <code>Session</code> obtained from the
   ** <code>User Agent</code> locale.
   **
   ** @return                    the language code of the <code>Session</code>
   **                            obtained from the <code>User Agent</code>
   **                            locale.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getLanguage() {
    return this.locale.getLanguage();
  }

}