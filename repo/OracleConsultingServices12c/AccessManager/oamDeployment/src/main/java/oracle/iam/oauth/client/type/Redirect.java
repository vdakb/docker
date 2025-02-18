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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Redirect.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Redirect.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.client.type;

import oracle.hst.deployment.ServiceDataType;

////////////////////////////////////////////////////////////////////////////////
// class Redirect
// ~~~~~ ~~~~~~~~
/**
 ** <code>Redirect</code> represents redirect URL's of a
 ** <code>Resource Client</code> instance which itself might be created, deleted
 ** or configured in Oracle Access Manager that during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Redirect extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String  url;
  private boolean tls;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Redirect() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setURL
  /**
   ** Called to inject the argument for attribute <code>url</code>.
   **
   ** @param  url                the url of the <code>Redirect</code>.
   */
  public final void setURL(final String url) {
    checkAttributesAllowed();
    this.url = url;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Returns the {@link String} delegate of <code>Resource Client</code>
   ** redirect URL to handle.
   **
   ** @return                    the {@link String} delegate of
   **                            <code>Resource Client</code> redirect URL to
   **                            handle.
   */
  final String url() {
    return this.url;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecure
  /**
   ** Called to inject the argument for attribute <code>tls</code>.
   **
   ** @param  state                the transport layer security of the
   **                            <code>Redirect</code>.
   */
  public final void setSecure(final boolean state) {
    checkAttributesAllowed();
    this.tls = state;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   secure
  /**
   ** Returns the <code>boolean</code> state of the transport layer security to
   ** apply at the <code>Resource Client</code> redirect URL to handle.
   **
   ** @return                    the {@link String} delegate of
   **                            <code>Resource Client</code> redirect URL to
   **                            handle.
   */
  final boolean secure() {
    return this.tls;
  }

}