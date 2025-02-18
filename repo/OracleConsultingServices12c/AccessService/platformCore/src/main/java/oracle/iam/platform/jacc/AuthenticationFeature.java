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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   AuthenticationFeature.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AuthenticationFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.jacc;

import java.lang.annotation.Annotation;

import java.util.Optional;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ContainerRequestFilter;

import org.glassfish.jersey.InjectionManagerProvider;

import org.glassfish.jersey.server.model.AnnotatedMethod;

import org.glassfish.jersey.internal.inject.InjectionManager;

////////////////////////////////////////////////////////////////////////////////
// class AuthenticationFeature
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** A {@link DynamicFeature} that registers the provided authentication filter
 ** to resource methods annotated with the {@code RolesAllowed},
 ** {@code PermitAll} and {@code DenyAll} annotations.
 ** <p>
 ** In conjunction with {@code RolesAllowedDynamicFeature} it enables
 ** authorization <i>AND</i> authentication of requests on the annotated
 ** methods.
 ** <p>
 ** If authorization is not a concern, then {@code RolesAllowedDynamicFeature}
 ** could be omitted. But to enable authentication, the {@code PermitAll}
 ** annotation should be placed on the corresponding resource methods.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AuthenticationFeature implements Feature
                                   ,          DynamicFeature {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ContainerRequestFilter                  filter;
  private final Class<? extends ContainerRequestFilter> clazz;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthenticationFeature</code> to use for request
   ** authentication provider.
   **
   ** @param  filter             the {@link ContainerRequestFilter} used.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestFilter}.
   */
  public AuthenticationFeature(final ContainerRequestFilter filter) {
    // ensure inheritance
    super();

    // initialize instance attributs
    this.clazz  = null;
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthenticationFeature</code> to use for request
   ** authentication provider class.
   **
   ** @param  clazz              the {@link ContainerRequestFilter} class used.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link ContainerRequestFilter}.
   */
  public AuthenticationFeature(final Class<? extends ContainerRequestFilter> clazz) {
    // ensure inheritance
    super();

    this.clazz  = clazz;
    this.filter = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interafces
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   configure (Feature)
  /**
   ** A call-back method called when the feature is to be enabled in a given
   ** runtime configuration scope.
   ** <br>
   ** The responsibility of the feature is to properly update the supplied
   ** runtime configuration context and return true if the feature was
   ** successfully enabled or false otherwise.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Under some circumstances the feature may decide not to enable itself,
   ** which is indicated by returning <code>false</code>. In such case the
   ** configuration context does not add the feature to the collection of
   ** enabled features and a subsequent call to
   ** {@code Configuration.isEnabled(Feature)} or
   ** {@code Configuration.isEnabled(Class)} method would return
   ** <code>false</code>.
   **
   ** @param  context            the configurable resource or sub-resource
   **                            method-level runtime context associated with
   **                            the resourceInfo in which the feature.
   **                            <br>
   **                            Allowed object is {@link FeatureContext}.
   */
  @Override
  public boolean configure(final FeatureContext context) {
    try {
      if (this.filter != null) {
        final InjectionManager manager = InjectionManagerProvider.getInjectionManager(context);
        if (manager != null) {
          inject(manager, this.filter);
        }
      }
      return true;
    }
    catch (IllegalArgumentException illegalArgumentException) {
      return false;
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   configure (DynamicFeature)
  /**
   ** A callback method called by the JAX-RS runtime during the application
   ** deployment to register provider instances or classes in a runtime
   ** configuration scope of a particular resource or sub-resource method; i.e.
   ** the providers that should be dynamically bound to the method.
   ** <p>
   ** Conceptually, this callback method is called during a resource or
   ** sub-resource method discovery phase (typically once per each discovered
   ** resource or sub-resource method) to register provider instances or classes
   ** in a configuration scope of each particular method identified by the
   ** supplied resource information. The responsibility of the feature is to
   ** properly update the supplied configuration context. 
   **
   ** @param  resource           the resource class and method information.
   **                            <br>
   **                            Allowed object is {@link ResourceInfo}.
   ** @param  context            the configurable resource or sub-resource
   **                            method-level runtime context associated with
   **                            the resourceInfo in which the feature.
   **                            <br>
   **                            Allowed object is {@link FeatureContext}.
   */
  @Override
  public void configure(final ResourceInfo resource, final FeatureContext context) {
    final AnnotatedMethod method    = new AnnotatedMethod(resource.getResourceMethod());
    if (method.getAnnotation(Authenticated.class) != null) {
      if (this.filter != null)
        registerFilter(context, this.filter);
    }
    final Annotation[][]  parameter = method.getParameterAnnotations();
    final Class<?>[]      type      = method.getParameterTypes();
    // first, check for any @Authentication annotations on the method
    for (int i = 0; i < parameter.length; i++) {
      if (contained(parameter[i])) {
        // optional authentication requires that a concrete AuthenticationFilter
        // be provided.
        if (type[i].equals(Optional.class) && this.filter != null) {
          registerFilter(context, new ExceptionFilter(this.filter));
        }
        else {
          registerFilter(context);
        }
        return;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inject
  /**
   ** Analyzes the given {@link ContainerRequestFilter} and inject into its fields and methods.
   ** The object injected in this way will not be managed by HK2
   **
   ** @param  filter             the request filter to be analyzed and injected.
   */
  static void inject(final InjectionManager manager, final ContainerRequestFilter filter) {
    manager.inject(filter);
    // if instance is AuthenticationFilter, inject Authenticator and Authorizer
    // instances as well
    if (filter instanceof AuthenticationFilter) {
      AuthenticationFilter<?, ?> authn = (AuthenticationFilter<?, ?>)filter;
      if (authn.authenticator != null) {
        manager.inject(authn.authenticator);
      }
      if (authn.authorizer != null) {
        manager.inject(authn.authorizer);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contained
  /**
   ** Verifies if the required annotations availabale.
   **
   ** @param  annotation         the annotations to test.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}.
   **
   ** @return                    <code>true</code> if the annotation
   **                            {@link Authenticated} is contained in the
   **                            provided collection; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean contained(final Annotation[] annotations) {
    for (final Annotation annotation : annotations) {
      if (annotation instanceof Authenticated) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerFilter
  /**
   ** Registers the authentication filter in the {link FeatureContext}
   ** specified.
   **
   ** @param  context            the {link FeatureContext} to register the.
   **                            authentication filter within.
   **                            <br>
   **                            Allowed object is {@link FeatureContext}.
   */
  private void registerFilter(final FeatureContext context) {
    registerFilter(context, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerFilter
  /**
   ** Registers the {@link ContainerRequestFilter} provided in the
   ** {link FeatureContext} specified.
   ** <br>
   ** If the {@link ContainerRequestFilter} provided evaluates to
   ** <code>null</code> the {@link ContainerRequestFilter} this instance belongs
   ** to is registred instead.
   **
   ** @param  context            the {link FeatureContext} to register the.
   **                            {@link ContainerRequestFilter} within.
   **                            <br>
   **                            Allowed object is {@link FeatureContext}.
   ** @param  filter             the {link ContainerRequestFilter} to register.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestFilter}.
   */
  private void registerFilter(final FeatureContext context, final ContainerRequestFilter filter) {
    if (filter != null) {
      context.register(filter);
    }
    else if (this.filter != null) {
      context.register(this.filter);
    }
    else if (this.clazz != null) {
      context.register(this.clazz);
    }
  }
}