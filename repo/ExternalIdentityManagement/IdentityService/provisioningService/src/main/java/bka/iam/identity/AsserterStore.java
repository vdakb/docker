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

    File        :   AsserterStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AsserterStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity;

import java.util.logging.Logger;

import javax.enterprise.inject.Typed;

import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import javax.security.enterprise.identitystore.IdentityStore;

import bka.iam.identity.jmx.TokenAsserterConfiguration;
import bka.iam.identity.jmx.BearerAsserterConfiguration;

import java.util.logging.Level;

////////////////////////////////////////////////////////////////////////////////
// class AsserterStore
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A literal representation of the {@code AssertionStoreDefinition}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Typed(AsserterStore.class)
public class AsserterStore extends BearerAssertionStore {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String         THIS             = AsserterStore.class.getName();
  private static final Logger         LOGGER           = Logger.getLogger(THIS);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4166133183178088523")
  private static final long           serialVersionUID = -5561717844469434375L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private BearerAsserterConfiguration config           = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link IdentityStore} that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public AsserterStore() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config (AssertionStore)
  /**
   ** Retrieve the configuration singleton.
   **
   ** @return                    the configuration singleton.
   **                            <br>
   **                            Possible object is
   **                            {@link TokenAsserterConfiguration}.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected synchronized TokenAsserterConfiguration config() {
    if (this.config == null) {
      final String method = "config";
      LOGGER.entering(THIS, method);
      synchronized(THIS) {
        final BeanManager                       mgr  = CDI.current().getBeanManager();
        final Bean<BearerAsserterConfiguration> bean = (Bean<BearerAsserterConfiguration>)mgr.getBeans(BearerAsserterConfiguration.class).iterator().next();
        this.config = (BearerAsserterConfiguration)mgr.getReference(bean, BearerAsserterConfiguration.class, mgr.createCreationalContext(bean));
      }
      if (LOGGER.isLoggable(Level.CONFIG))
        LOGGER.config(this.config.toString());
      LOGGER.exiting(THIS, method);
    }
    return this.config;
  }
}