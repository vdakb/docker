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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Policy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Policy.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wsm.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.hst.deployment.spi.WebServicePolicyHandler;

import org.apache.tools.ant.BuildException;

////////////////////////////////////////////////////////////////////////////////
// class Policy
// ~~~~~ ~~~~~~
/**
 ** The Oracle WebService Manager policy to attach to or detach from a
 ** WebService
 */
public class Policy extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final WebServicePolicyHandler.Policy delegate = new WebServicePolicyHandler.Policy();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
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

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the bean in Oracle WebLogic Domain
   **                            to handle.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCategory
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>category</code>.
   **
   ** @param  category           the category of the bean in Oracle WebLogic
   **                            Domain to handle.
   **                            Allowed object is {@link String}.
   */
  public void setCategory(final String category) {
    checkAttributesAllowed();
    this.delegate.category(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOverrides
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Overrides}.
   **
   ** @param  name               the name of the override type of the Oracle
   **                            WebService Manager Policy.
   ** @param  value              the value for <code>name</code> to set on the
   **                            WebService Manager Policy Reference.
   **
   ** @throws BuildException     if the specified value pair is already
   **                            part of the override mapping.
   */
  public final void addOverrides(final String name, final String value)
    throws BuildException {

    addConfiguredOverrides(new Overrides(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredOverrides
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Override}s.
   **
   ** @param  parameter          the {@link Override} to add.
   **
   ** @throws BuildException     if the specified value pair is already
   **                            part of the override mapping.
   */
  public final void addConfiguredOverrides(final Overrides parameter)
    throws BuildException {

    checkAttributesAllowed();
    this.delegate.addParameter(parameter.name(), parameter.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link WebServicePolicyHandler.Policy} delegate.
   **
   ** @return                    the {@link WebServicePolicyHandler.Policy}
   **                            delegate.
   **                            Possible object is
   **                            {@link WebServicePolicyHandler.Policy}.
   */
  public final WebServicePolicyHandler.Policy instance() {
    return this.delegate;
  }
}