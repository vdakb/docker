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

    File        :   Instance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Instance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessagent.type;

import org.apache.tools.ant.BuildException;

import oracle.iam.access.common.type.Resource;
import oracle.iam.access.common.type.Variation;
import oracle.iam.access.common.type.AccessServer;
import oracle.iam.access.common.type.FeatureInstance;

import oracle.iam.access.common.spi.AccessAgentProperty;
import oracle.iam.access.common.spi.AccessAgentInstance;

////////////////////////////////////////////////////////////////////////////////
// class Instance
// ~~~~~ ~~~~~~~~
/**
 ** <code>Instance</code> represents an <code>Access Server</code>
 ** instance in Oracle Access Manager that might be created, deleted or
 ** configured during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Instance extends FeatureInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final AccessAgentInstance delegate = new AccessAgentInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Instance</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Instance() {
    // ensure inheritance
    super();

    // initialize instance delegate
    this.delegate.version(AccessAgentProperty.Version.AGENT11);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for attribute <code>name</code>.
   **
   ** @param  name               the name of the instance in Oracle Access
   **                            Manager to handle.
   */
  public final void setName(final String name) {
    checkAttributesAllowed();
    this.name = name;
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the name of the instance in Oracle Access Manager
   ** to handle.
   **
   ** @return                    the name of the instance in Oracle Access
   **                            Manager to handle.
   */
  public final String name() {
    return this.delegate.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Called to inject the argument for attribute <code>version</code>.
   **
   ** @param  version            the version of the agent instance in Oracle
   **                            Access Manager to handle.
   */
  public void setVersion(final Version version) {
    checkAttributesAllowed();
    this.delegate.version(AccessAgentProperty.Version.from(version.getValue()));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link AccessAgentInstance} delegate of Access Agent
   ** object to handle.
   **
   ** @return                    the {@link AccessAgentInstance} delegate.
   */
  public final AccessAgentInstance delegate() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredParameter
  /**
   ** Call by the ANT deployment to inject the argument for adding a parameter.
   **
   ** @param  parameter          the parameter to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Parameter} with the same name.
   */
  public void addConfiguredParameter(final Parameter parameter)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.add(AccessAgentProperty.from(parameter.getValue()), parameter.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredServerPrimary
  /**
   ** Call by the ANT deployment to inject the argument for adding a primary
   ** <code>Access Server</code> list.
   **
   ** @param  value              the {@link AccessServer.Primary} instance to
   **                            add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of primary <code>Access Server</code>.
   */
  public void addConfiguredServerPrimary(final AccessServer.Primary value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.primaryServer(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredServerSecondary
  /**
   ** Call by the ANT deployment to inject the argument for adding a secondary
   ** <code>Access Server</code> list.
   **
   ** @param  value              the {@link AccessServer.Secondary} instance to
   **                            add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of secondary <code>Access Server</code>.
   */
  public void addConfiguredServerSecondary(final AccessServer.Secondary value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.secondaryServer(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredResourceProtected
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Protected Resource</code> list.
   **
   ** @param  value              the {@link Resource.Protected} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of protected <code>Resource</code>s.
   */
  public void addConfiguredResourceProtected(final Resource.Protected value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.protectedResource(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredResourcePublic
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Public Resource</code> list.
   **
   ** @param  value              the {@link Resource.Public} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of public <code>Resource</code>s.
   */
  public void addConfiguredResourcePublic(final Resource.Public value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.publicResource(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredResourceExcluded
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Excluded Resource</code> list.
   **
   ** @param  value              the {@link Resource.Excluded} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of public <code>Resource</code>s.
   */
  public void addConfiguredResourceExcluded(final Resource.Excluded value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.excludedResource(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredResourceNotEnforced
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** host/port <code>NotEnforcedURL</code> list.
   **
   ** @param  value              the {@link Resource.NotEnforced} instance to
   **                            add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of <code>LogoutURL</code>s.
   */
  public void addConfiguredResourceNotEnforced(final Resource.NotEnforced value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access policy resources
    this.delegate.notEnforcedResource(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredValidationException
  /**
   ** Call by the ANT deployment to inject the argument for adding a IP
   ** validation exception list.
   **
   ** @param  value              the IP validation exception list.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link ValidationException} list.
   */
  public void addConfiguredValidationException(final ValidationException value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.validationException(value.delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredUserDefined
  /**
   ** Call by the ANT deployment to inject the argument for setting a list of
   ** user defined parameters.
   **
   ** @param  value              the list of user defined parameters to set.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link UserProperty} list.
   */
  public void addConfiguredUserDefined(final UserProperty value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.userProperty(value.delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredMiscellaneous
  /**
   ** Call by the ANT deployment to inject the argument for setting a list of
   ** miscellaneous properties.
   **
   ** @param  value              the list of miscellaneous properties to set.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Miscellaneous} list.
   */
  public void addConfiguredMiscellaneous(final Miscellaneous value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access agent property
    this.delegate.miscellaneousProperty(value.delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredLoginURL
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** host/port <code>LoginURL</code> list.
   **
   ** @param  value              the {@link Resource.Login} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of <code>LoginURL</code>s.
   */
  public void addConfiguredLoginURL(final Resource.Login value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access policy resources
    this.delegate.loginURL(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredLogoutURL
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** host/port <code>LogoutURL</code> list.
   **
   ** @param  value              the {@link Resource.Logout} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of <code>LogoutURL</code>s.
   */
  public void addConfiguredLogoutURL(final Resource.Logout value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access policy resources
    this.delegate.logoutURL(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredVariations
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** host/port <code>Variation</code> list.
   **
   ** @param  value              the {@link Variation} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            list of <code>Variation</code>
   **                            <code>Server</code>s.
   */
  public void addConfiguredVariations(final Variation value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access policy resources
    this.delegate.variations(value.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredProfileAttributeMapping
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** host/port <code>Profile Attribute Mapping</code>.
   **
   ** @param  value              the {@link Mapping.Profile} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            <code>Profile Attribute Mapping</code>.
   */
  public void addConfiguredProfileAttributeMapping(final Mapping.Profile value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access policy resources
    this.delegate.attributeMapping(value.delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredResponseAttributeMapping
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** host/port <code>Response Attribute Mapping</code>.
   **
   ** @param  value              the {@link Mapping.Response} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            <code>Response Attribute Mapping</code>.
   */
  public void addConfiguredResponseAttributeMapping(final Mapping.Response value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access policy resources
    this.delegate.attributeMapping(value.delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredSessionAttributeMapping
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** host/port <code>Session Attribute Mapping</code>.
   **
   ** @param  value              the {@link Mapping.Session} instance to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            <code>Session Attribute Mapping</code>.
   */
  public void addConfiguredSessionAttributeMapping(final Mapping.Session value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending access policy resources
    this.delegate.attributeMapping(value.delegate);
  }
}