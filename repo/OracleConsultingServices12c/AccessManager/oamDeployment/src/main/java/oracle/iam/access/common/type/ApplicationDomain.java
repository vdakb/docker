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

    File        :   ApplicationDomain.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationDomain.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.spi.schema.SuccessResponse;
import oracle.iam.access.common.spi.schema.SuccessResponseList;
import oracle.iam.access.common.spi.schema.RregApplicationDomain;
import oracle.iam.access.common.spi.schema.RregAuthorizationPolicy;
import oracle.iam.access.common.spi.schema.RregAuthenticationPolicy;
import oracle.iam.access.common.spi.schema.RregAuthorizationPolicies;
import oracle.iam.access.common.spi.schema.RregAuthenticationPolicies;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationDomain
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Access Manager enables the control who can access resources based on
 ** policies defined within an <code>Application Domain</code>. Users attempt to
 ** access a protected resource by entering a URL in a browser, by running an
 ** application, or by calling some other external business logic. When a user
 ** requests access to a protected resource, the request is evaluated according
 ** to policies that discriminate between authenticated users who are authorized
 ** and those who are not authorized for access to a particular resource.
 ** <p>
 ** <code>Application Domain</code>s do not have any hierarchical relationship
 ** to one another. Each <code>Application Domain</code> can be made to contain
 ** policy elements related to an entire application deployment, a particular
 ** tier of the deployment, or a single host.
 ** <p>
 ** Within each <code>Application Domain</code>, specific resources are
 ** identified for protection by specific policies that govern access.
 ** Authentication and authorization policies include Administrator-configured
 ** responses that are applied upon successful evaluation. Authorization
 ** policies include Administrator-configured conditions and rules that define
 ** how evaluation is performed, and responses to be applied upon successful
 ** evaluation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ApplicationDomain extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final RregApplicationDomain delegate = factory.createRregApplicationDomain();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Response
  // ~~~~~ ~~~~~~~~
  public static class Response extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final SuccessResponseList delegate = factory.createSuccessResponseList();


    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Success
    // ~~~~~ ~~~~~~~
    public static class Success extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final SuccessResponse delegate = factory.createSuccessResponse();

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Success</code> type that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public Success() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: setName
      /**
       ** Sets the value of the <code>name</code> property.
       **
       ** @param  value          the value of the <code>name</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setName(final String value) {
        this.delegate.setName(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setType
      /**
       ** Sets the value of the <code>type</code> property.
       **
       ** @param  value          the value of the <code>type</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setType(final String value) {
        this.delegate.setType(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setValue
      /**
       ** Sets the value of the <code>value</code> property.
       **
       ** @param  value          the value of the <code>value</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setValue(final String value) {
        this.delegate.setValue(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Response</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Response() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredResponseSuccess
    /**
     ** Call by the ANT deployment to inject the argument for adding a success
     ** response.
     **
     ** @param  success          the {@link Success} instance to set.
     **                          Allowed object is {@link Success}.
     **
     ** @throws BuildException   if an instance is referencing an already set.
     */
    public void addConfiguredResponseSuccess(final Success success)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (success == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "response"));

      // assign the correspondending policy property
      this.delegate.getSuccessResponse().add(success.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Authentication
  // ~~~~~ ~~~~~~~~~~~~~~
  public static class Authentication extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final RregAuthenticationPolicies delegate = factory.createRregAuthenticationPolicies();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Policy
    // ~~~~~ ~~~~~~~
    /**
     ** <code>Authentication Policy</code> are applied to specific resources
     ** within an <code>Application Domain</code>.
     ** <br>
     ** Each authentication policy:
     ** <ul>
     **   <li>Identifies the specific resources covered by this policy, which
     **       must be defined on the Resources tab of this policy and in the
     **       Resources container for the <code>Application Domain</code>
     **   <li>Specifies the authentication scheme that provides the challenge
     **       method to be used to authenticate the user
     **   <li>Specifies the Success URL (and the failure URL) that redirects the
     **       user based on the results of this policy evaluation
     **   <li>Defines optional Responses that identify post-authentication
     **       actions to be carried out by the Agent.
     **       <br>
     **       Policy responses provide the ability to insert information into a
     **       session and pull it back out at any later point. This is more
     **       robust and flexible than Oracle Access Manager 10g, which provided
     **       data passage to (and between) applications by redirecting to URLs
     **       in a specific sequence.
     **       <br>
     **       Policy responses are optional. These must be configured by an
     **       Administrator and are applied to specific resources defined within
     **       the <code>Application Domain</code>.
     ** </ul>
     */
    public static class Policy extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final RregAuthenticationPolicy delegate = factory.createRregAuthenticationPolicy();

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Policy</code> type that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public Policy() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: setName
      /**
       ** Sets the value of the <code>name</code> property.
       **
       ** @param  value          the value of the <code>name</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setName(final String value) {
        this.delegate.setName(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setDescription
      /**
       ** Sets the value of the <code>description</code> property.
       **
       ** @param  value          the value of the <code>description</code>
       **                        property.
       **                        Allowed object is {@link String}.
       */
      public void setDescription(final String value) {
        this.delegate.setDescription(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setAuthenticationScheme
      /**
       ** Sets the value of the <code>authenticationScheme</code> property.
       **
       ** @param  value          the value of the
       **                        <code>authenticationScheme</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setAuthenticationScheme(final String value) {
        this.delegate.setAuthenticationSchemeName(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredAddedURI
      /**
       ** Call by the ANT deployment to inject the argument for adding an uri.
       **
       ** @param  list           the {@link URI.Added} instance to set.
       **                        Allowed object is {@link URI.Added}.
       **
       ** @throws BuildException if an instance is referencing an already set.
       */
      public void addConfiguredAddedURI(final URI.Added list)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (list == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "uriList"));

        // prevent bogus state
        if (this.delegate.getUriList() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "uriList"));

        // assign the correspondending policy property
        this.delegate.setUriList(list.delegate);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredDeletedURI
      /**
       ** Call by the ANT deployment to inject the argument for adding an uri to
       ** delete.
       **
       ** @param  list           the {@link URI.Deleted} instance to set.
       **                        Allowed object is {@link URI.Deleted}.
       **
       ** @throws BuildException if an instance is referencing an already
       **                        set.
       */
      public void addConfiguredDeletedURI(final URI.Deleted list)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (list == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "deletedUri"));

        // prevent bogus state
        if (this.delegate.getUriList() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "deletedUri"));

        // assign the correspondending policy property
        this.delegate.setDeletedUriList(list.delegate);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredResponse
      /**
       ** Call by the ANT deployment to inject the argument for setiing the
       ** success response.
       **
       ** @param  response         the {@link Response} instance to set.
       **                          Allowed object is {@link Policy}.
       **
       ** @throws BuildException   if an instance is referencing an already set.
       */
      public void addConfiguredResponse(final Response response)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (response == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "response"));

        // prevent bogus state
        if (this.delegate.getSuccessResponseList() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "response"));

        // assign the correspondending policy property
        this.delegate.setSuccessResponseList(response.delegate);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Authentication</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Authentication() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredAuthenticationPolicy
    /**
     ** Call by the ANT deployment to inject the argument for adding a policy.
     **
     ** @param  policy           the {@link Policy} instance to set.
     **                          Allowed object is {@link Policy}.
     **
     ** @throws BuildException   if an instance is referencing an already set.
     */
    public void addConfiguredAuthenticationPolicy(final Policy policy)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (policy == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "authenticationPolicy"));

      // assign the correspondending policy property
      this.delegate.getRregAuthenticationPolicy().add(policy.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Authorization
  // ~~~~~ ~~~~~~~~~~~~~~
  public static class Authorization extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final RregAuthorizationPolicies delegate = factory.createRregAuthorizationPolicies();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Policy
    // ~~~~~ ~~~~~~
    /**
     ** <code>Authorization Policy</code> protects access to one or more
     ** resources based on attributes of an authenticated user or the
     ** environment. The authorization policy provides the sole authorization
     ** protection for resources included in the policy.
     ** <p>
     ** A single policy can be defined to protect one or more resources in the
     ** <code>Application Domain</code>. However, each resource can be protected
     ** by only one authorization policy.
     */
    public static class Policy extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final RregAuthorizationPolicy delegate = factory.createRregAuthorizationPolicy();

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Policy</code> type that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public Policy() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: setName
      /**
       ** Sets the value of the <code>name</code> property.
       **
       ** @param  value          the value of the <code>name</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setName(final String value) {
        this.delegate.setName(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setDescription
      /**
       ** Sets the value of the <code>description</code> property.
       **
       ** @param  value          the value of the <code>description</code>
       **                        property.
       **                        Allowed object is {@link String}.
       */
      public void setDescription(final String value) {
        this.delegate.setDescription(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredAddedURI
      /**
       ** Call by the ANT deployment to inject the argument for adding an uri.
       **
       ** @param  list           the {@link URI.Added} instance to set.
       **                        Allowed object is {@link URI.Added}.
       **
       ** @throws BuildException if an instance is referencing an already set.
       */
      public void addConfiguredAddedURI(final URI.Added list)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (list == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "addedURI"));

        // prevent bogus state
        if (this.delegate.getUriList() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "addedURI"));

        // assign the correspondending policy property
        this.delegate.setUriList(list.delegate);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredDeletedURI
      /**
       ** Call by the ANT deployment to inject the argument for adding an uri to
       ** delete.
       **
       ** @param  list           the {@link URI.Deleted} instance to set.
       **                        Allowed object is {@link URI.Deleted}.
       **
       ** @throws BuildException if an instance is referencing an already
       **                        set.
       */
      public void addConfiguredDeletedURI(final URI.Deleted list)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (list == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "deletedURI"));

        // prevent bogus state
        if (this.delegate.getUriList() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "deletedURI"));

        // assign the correspondending policy property
        this.delegate.setDeletedUriList(list.delegate);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredResponse
      /**
       ** Call by the ANT deployment to inject the argument for setiing the
       ** success response.
       **
       ** @param  response         the {@link Response} instance to set.
       **                          Allowed object is {@link Policy}.
       **
       ** @throws BuildException   if an instance is referencing an already set.
       */
      public void addConfiguredResponse(final Response response)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (response == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "response"));

        // prevent bogus state
        if (this.delegate.getSuccessResponseList() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "response"));

        // assign the correspondending policy property
        this.delegate.setSuccessResponseList(response.delegate);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredConditions
      /**
       ** Call by the ANT deployment to inject the argument for adding
       ** conditions.
       **
       ** @param  list           the {@link Conditions} instance to set.
       **                        Allowed object is {@link Conditions}.
       **
       ** @throws BuildException if an instance is referencing an already set.
       */
      public void addConfiguredConditions(final Conditions list)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (list == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "conditions"));

        // prevent bogus state
        if (this.delegate.getConditionsList() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "conditions"));

        // assign the correspondending policy property
        this.delegate.setConditionsList(list.delegate);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredRules
      /**
       ** Call by the ANT deployment to inject the argument for adding
       ** rules.
       **
       ** @param  list           the {@link Rules} instance to set.
       **                        Allowed object is {@link Rules}.
       **
       ** @throws BuildException if an instance is referencing an already set.
       */
      public void addConfiguredRules(final Rules list)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (list == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "rules"));

        // prevent bogus state
        if (this.delegate.getRules() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "rules"));

        // assign the correspondending policy property
        this.delegate.setRules(list.delegate);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Authorization</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Authorization() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredAuthorizationPolicy
    /**
     ** Call by the ANT deployment to inject the argument for adding a policy.
     **
     ** @param  policy           the {@link Policy} instance to set.
     **                          Allowed object is {@link Policy}.
     **
     ** @throws BuildException   if an instance is referencing an already set.
     */
    public void addConfiguredAuthorizationPolicy(final Policy policy)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (policy == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "authenticationPolicy"));

      // assign the correspondending policy property
      this.delegate.getRregAuthorizationPolicy().add(policy.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationDomain</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationDomain() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for attribute <code>name</code>.
   **
   ** @param  name               the name of the <code>Application domain</code>
   **                            in Oracle Access Manager to handle.
   **                            Allowed object is {@link String}.
   */
  public final void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.setName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Application domain</code> of the instance in
   ** Oracle Access Manager to handle.
   **
   ** @return                    the name of the <code>Application domain</code>
   **                            of the instance in Oracle Access Manager to
   **                            handle.
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.delegate.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Called to inject the argument for attribute <code>description</code>.
   **
   ** @param  description        the description of the
   **                            <code>Application domain</code> in Oracle
   **                            Access Manager to handle.
   **                            Allowed object is {@link String}.
   */
  public final void setDescription(final String description) {
    checkAttributesAllowed();
    this.delegate.setDescription(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the <code>Application domain</code> of the
   ** instance in Oracle Access Manager to handle.
   **
   ** @return                    the description of the
   **                            <code>Application domain</code> of the instance
   **                            in Oracle Access Manager to handle.
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.delegate.getDescription();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  delegate
  /**
   ** Returns the {@link RregApplicationDomain} delegate of configured
   ** <code>Application Domain</code>.
   **
   ** @return                    the {@link RregApplicationDomain} delegate.
   */
  public final RregApplicationDomain delegate() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredURI
  /**
   ** Call by the ANT deployment to inject the argument for adding an uri.
   **
   ** @param  list               the {@link URI.Added} instance to set.
   **                            Allowed object is {@link URI.Added}.
   **
   ** @throws BuildException     if an instance is referencing an already set.
   */
  public void addConfiguredURL(final URI.Added list)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (list == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "uriList"));

    // prevent bogus state
    if (this.delegate.getUriList() != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "uriList"));

    // assign the correspondending domain property
    this.delegate.setUriList(list.delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredDeletedURL
  /**
   ** Call by the ANT deployment to inject the argument for adding an uri to
   ** delete.
   **
   ** @param  list               the {@link URI.Deleted} instance to set.
   **                            Allowed object is {@link URI.Deleted}.
   **
   ** @throws BuildException     if an instance is referencing an already set.
   */
  public void addConfiguredDeletedURL(final URI.Deleted list)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (list == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "deletedUri"));

    // prevent bogus state
    if (this.delegate.getUriList() != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "deletedUri"));

    // assign the correspondending domain property
    this.delegate.setDeletedUriList(list.delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAuthentication
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** <code>Authentication Policy</code>.
   **
   ** @param  policy             the {@link Authentication} instance to
   **                            set.
   **                            Allowed object is {@link Authentication}.
   **
   ** @throws BuildException     if an instance is referencing an already set.
   */
  public void addConfiguredAuthentication(final Authentication policy)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (policy == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "authentication"));

    // prevent bogus state
    if (this.delegate.getRregAuthenticationPolicies() != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "authentication"));

    // assign the correspondending domain property
    this.delegate.setRregAuthenticationPolicies(policy.delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAuthorization
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** <code>Authorization Policy</code>.
   **
   ** @param  policy             the {@link Authorization} instance to
   **                            set.
   **                            Allowed object is {@link Authorization}.
   **
   ** @throws BuildException     if an instance is referencing an already set.
   */
  public void addConfiguredAuthorization(final Authorization policy)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (policy == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "authorization"));

    // prevent bogus state
    if (this.delegate.getRregAuthorizationPolicies() != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "authorization"));

    // assign the correspondending domain property
    this.delegate.setRregAuthorizationPolicies(policy.delegate);
  }
}