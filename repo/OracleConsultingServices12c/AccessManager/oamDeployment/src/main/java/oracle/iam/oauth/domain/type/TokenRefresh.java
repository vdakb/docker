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

    File        :   TokenRefresh.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TokenRefresh.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.domain.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.oauth.common.spi.DomainInstance;

////////////////////////////////////////////////////////////////////////////////
// class TokenRefresh
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>TokenRefresh</code> represents refresh properties on
 ** <code>Token</code>s of an <code>Identity Domain</code> instance which itself
 ** might be created, deleted or configured in Oracle Access Manager that during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TokenRefresh extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final DomainInstance.TokenLifeCycle delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TokenRefresh</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TokenRefresh() {
    // ensure inheritance
    this(new DomainInstance.TokenLifeCycle());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TokenRefresh</code> type that backed by the specified
   ** {@link DomainInstance.TokenLifeCycle} as the backing bean.
   **
   ** @param  delegate           a {@link DomainInstance.TokenLifeCycle} backing
   **                            bean.
   */
  protected TokenRefresh(final DomainInstance.TokenLifeCycle delegate) {
    // ensure inheritance
    super();

    this.delegate = delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExpiry
  /**
   ** Called to inject the argument for parameter <code>expiry</code>.
   **
   ** @param  expiry             the expiry time of the <code>Token</code>.
   */
  public final void setExpiry(final int expiry) {
    checkAttributesAllowed();
    this.delegate.expiry(expiry);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLifeCycle
  /**
   ** Called to inject the argument for parameter <code>lifeCycle</code>.
   **
   ** @param  state              <code>true</code> if the life cycle of the
   **                            <code>Token</code> is enabled; otherwise
   **                            <code>false</code>.
   */
  public final void setLifeCycle(final boolean state) {
    checkAttributesAllowed();
    this.delegate.enabled(state);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link DomainInstance.TokenLifeCycle} delegate of Identity
   ** Domain object to handle.
   **
   ** @return                    the {@link DomainInstance.TokenLifeCycle}
   **                            delegate.
   */
  final DomainInstance.TokenLifeCycle delegate() {
    return this.delegate;
  }
}