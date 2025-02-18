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

    File        :   Token.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Token.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.domain.type;

import org.apache.tools.ant.BuildException;

import oracle.iam.oauth.common.spi.DomainInstance;

////////////////////////////////////////////////////////////////////////////////
// class Token
// ~~~~~ ~~~~~
/**
 ** <code>Token</code> represents an <code>Identity Domain</code>
 ** instance in Oracle Access Manager which itself might be created, deleted or
 ** configured during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Token extends TokenRefresh {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Token</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Token() {
    // ensure inheritance
    super( new DomainInstance.Token());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Called to inject the argument for attribute <code>type</code>.
   **
   ** @param  type               the type name of the <code>Token</code>.
   */
  public final void setType(final TokenType type) {
    checkAttributesAllowed();
    ((DomainInstance.Token)this.delegate).type(type.getValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefreshToken
  /**
   ** Called to inject the argument for attribute <code>refreshEnabled</code>.
   **
   ** @param  state              <code>true</code> if the refresh token is
   **                            enabled; otherwise <code>false</code>.
   */
  public final void setRefreshToken(final boolean state) {
    checkAttributesAllowed();
    ((DomainInstance.Token)this.delegate).refreshEnabled(state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredTokenRefresh
  /**
   ** Call by the ANT deployment to inject the argument for adding a refresh
   ** properties.
   **
   ** @param  refresh            the refresh properties to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            {@link TokenRefresh}.
   */
  public void addConfiguredTokenRefresh(final TokenRefresh refresh)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending token settings
    ((DomainInstance.Token)this.delegate).refreshLifeCyle(refresh.delegate);
  }
}