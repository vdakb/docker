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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Authorization Management

    File        :   Navigation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Navigation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.module.state;

import java.util.Map;
import java.util.HashMap;

import java.io.Serializable;

import oracle.hst.foundation.faces.ADF;

////////////////////////////////////////////////////////////////////////////////
// class Navigation
// ~~~~~ ~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** modules of the <code>Dashboard</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Navigation implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Map<String, String> truncate;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3993896185400871503")
  private static final long                serialVersionUID = -5960302935688661326L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private int                              pollInterval   = 2000;
  private String                           truncateLength = "0";

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    truncate = new HashMap<String, String>();
    truncate.put("ru", "45");
    truncate.put("fr", "80");
    truncate.put("de", "60");
    truncate.put("it", "80");
    truncate.put("fi", "80");
    truncate.put("el", "80");
    truncate.put("sv", "80");
    truncate.put("da", "80");
    truncate.put("no", "60");
    truncate.put("hu", "70");
    truncate.put("pl", "74");
    truncate.put("ro", "80");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Navigation</code> state bean that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Navigation() {
    // ensure inheritance
    super();

    // initialize instance
    truncateLengthForLocale();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTruncateLength
  /**
   ** Returns the value of the truncateLength property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the truncateLength property.
   **                            Possible object is {@link String}.
   */
  public String getTruncateLength() {
    return this.truncateLength;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPollInterval
  /**
   ** Sets the value of the pollInterval property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  value              the value of the parentGrid property.
   **                            Allowed object is <code>int</code>.
   */
  public void setPollInterval(final int value) {
    this.pollInterval = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPollInterval
  /**
   ** Returns the value of the pollInterval property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the parentKey property.
   **                            Possible object is <code>int</code>.
   */
  public int getPollInterval() {
    return this.pollInterval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   truncateLengthForLocale
  /**
   ** Returns the length of truncation specific to the current local of the
   ** logged in user.
   **
   ** @return                    the length of truncation specific to the
   **                            current local of the logged in user.
   */
  private void truncateLengthForLocale() {
    final String lang  = ADF.locale().getLanguage();
    final String trunc = truncate.get(lang);
    if (trunc != null)
      this.truncateLength = trunc;
    else
      this.truncateLength = "0";
  }
}