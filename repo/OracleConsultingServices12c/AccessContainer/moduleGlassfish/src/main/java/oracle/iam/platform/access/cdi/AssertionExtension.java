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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Authentication

    File        :   AssertionExtension.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionExtension.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.platform.access.cdi;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.event.Observes;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;

import javax.security.enterprise.identitystore.IdentityStore;

import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;

import org.glassfish.soteria.cdi.CdiUtils;
import org.glassfish.soteria.cdi.CdiProducer;

////////////////////////////////////////////////////////////////////////////////
// class AssertionExtension
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This CDI extension installs the {@link AssertionStore} along the
 ** {@link AssertionMechanism} when the
 ** <code>LoginConfig</code>
 ** annotation is encountered (MP-JWT 1.0 5).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AssertionExtension implements Extension {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String       PREFIX        = "IGS ";
  private static final Logger       LOGGER        = Logger.getLogger(AssertionExtension.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Bean<HttpAuthenticationMechanism> mechanism;
  private List<Bean<IdentityStore>>         identityStore = new ArrayList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AssertionExtension</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AssertionExtension() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  public void register(final @Observes BeforeBeanDiscovery event, final BeanManager manager) {
    event.addAnnotatedType(manager.createAnnotatedType(AssertionStore.class),     PREFIX + AssertionStore.class.getName());
    event.addAnnotatedType(manager.createAnnotatedType(AssertionMechanism.class), PREFIX + AssertionMechanism.class.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processBean
  public <T> void processBean(final @Observes ProcessBean<T> event, final BeanManager manager) {
    // create the bean being proccessed.
    final Class<?> clazz = event.getBean().getBeanClass();
    // obtain the assertion store definition from the annotation (if it exists)
    Optional<AssertionStoreDefinition> store = CdiUtils.getAnnotation(manager, event.getAnnotated(), AssertionStoreDefinition.class);
    store.ifPresent(
      definition -> {
        LOGGER.log(Level.INFO, "Activating {0} identity store annotated at {1} class", new String[]{AssertionStoreDefinition.class.getName(), clazz.getName()});
        this.identityStore.add(
          new CdiProducer<IdentityStore>()
          .scope(ApplicationScoped.class)
          .beanClass(IdentityStore.class)
          .types(Object.class, IdentityStore.class)
          .addToId(AssertionStoreDefinition.class)
          .create(e -> new AssertionStore())
        );
      }
    );
    // obtain the authentication mechanism definition from the annotation (if it
    // exists)
    Optional<AssertionMechanismDefinition> assertion = CdiUtils.getAnnotation(manager, event.getAnnotated(), AssertionMechanismDefinition.class);
    assertion.ifPresent(
      definition -> {
        LOGGER.log(Level.INFO, "Activating {0} authentication mechanism annotated at {1} class", new String[]{AssertionMechanismDefinition.class.getName(), clazz.getName()});
        this.mechanism = new CdiProducer<HttpAuthenticationMechanism>()
          .scope(ApplicationScoped.class)
          .beanClass(HttpAuthenticationMechanism.class)
          .types(Object.class, HttpAuthenticationMechanism.class)
          .addToId(AssertionMechanismDefinition.class)
          .create(e -> new AssertionMechanism())
        ;  
      }
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterBean
  /**
   ** Capture the second event fired by the container when it has fully
   ** completed the bean discovery process, validated that there are no
   ** definition errors relating to the discovered beans, and registered Bea
   ** and ObserverMethod objects for the discovered beans, but before detecting
   ** deployment problems.
   ** <p>
   ** If an exception is thrown, the exception is treated as a definition error
   ** by the container. 
   */
  @SuppressWarnings("unused") 
  public void afterBean(final @Observes AfterBeanDiscovery event, final BeanManager manager) {
    if (!this.identityStore.isEmpty()) {
      for (Bean<IdentityStore> store : this.identityStore) {
        event.addBean(store);
      }
    }
    if (this.mechanism != null) {
      event.addBean(this.mechanism);
    }
  }
}