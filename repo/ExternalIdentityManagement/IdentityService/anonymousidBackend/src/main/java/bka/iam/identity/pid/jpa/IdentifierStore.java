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
    Subsystem   :   Identity Governance Provisioning

    File        :   IdentifierStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentifierStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.pid.jpa;

import javax.annotation.PostConstruct;

import javax.ejb.Startup;
import javax.ejb.Singleton;

import javax.persistence.Persistence;

import oracle.hst.platform.jpa.injection.EntityManagerStore;

////////////////////////////////////////////////////////////////////////////////
// class IdentifierStore
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A store for entity managers handling transactions belonging to anonymous
 ** identifiers.
 ** <br>
 ** It is basically a ThreadLocal which stores the entity manager.
 ** <br>
 ** The {@link oracle.hst.platform.jpa.injection.TransactionInterceptor} is
 ** expected to register entity manager. The application code can get the
 ** current entity manager either by injecting the store or the
 ** {@link oracle.hst.platform.jpa.injection.EntityManagerDelegate}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Startup
@Singleton
public class IdentifierStore extends EntityManagerStore {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3864325330690173227")
  private static final long serialVersionUID = -1637996944550986984L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentifierStore</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentifierStore() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startup
  /**
   ** Callback notification to signal dependency injection is done to perform
   ** any initialization and the instance becomes to put in service.
   */
  @PostConstruct
  public void startup() {
    // intialize instance attributes
    initialize(Persistence.createEntityManagerFactory("pid"));
  }
}