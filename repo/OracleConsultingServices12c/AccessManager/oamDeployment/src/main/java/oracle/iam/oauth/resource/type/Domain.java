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

    File        :   Domain.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Domain.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.resource.type;

import oracle.hst.deployment.ServiceDataType;

import org.apache.tools.ant.BuildException;

import oracle.iam.oauth.common.spi.DomainInstance;

////////////////////////////////////////////////////////////////////////////////
// class Domain
// ~~~~~ ~~~~~~
/**
 ** <code>Domain</code> represents an existing <code>Identity Domain</code>
 ** instance in Oracle Access Manager were a <code>Resource Server</code> might
 ** be created, deleted or configured within during an import operation.
 ** <p>
 ** A <code>Identity Domain</code> is a entity that contain all artifacts
 ** required to provide standard OAuth Services .
 ** <p>
 ** Each <code>Identity Domain</code> is an independent entity.
 ** <br>
 ** One of the primary use cases of the <code>Identity Domain</code> is for
 ** multi tenants deployments. Each <code>Identity Domain</code> will correspond
 ** to a tenant. This can apply to different departments in an organization if
 ** there is a need for independence. This will also be useful for cloud
 ** deployments where each <code>Identity Domain</code> can correspond to a
 ** separate tenant or entity. The following artifacts are just some of the
 ** components configured within an OAuth Services <code>Identity Domain</code>.
 ** <ul>
 **   <li>One or more <code>Resource Client</code>s
 **   <li>One or more <code>Resource Server</code>s
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Domain extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final DomainInstance delegate = new DomainInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Domain</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Domain() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for attribute <code>name</code>.
   **
   ** @param  name               the name of the <code>Identity Domain</code> in
   **                            Oracle Access Manager to handle.
   */
  public final void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link DomainInstance} delegate of Identity Domain
   ** object to handle.
   **
   ** @return                    the {@link DomainInstance} delegate.
   */
  public final DomainInstance delegate() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredInstance
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Resource Server</code>.
   **
   ** @param  instance           the resource to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Instance} with the same name.
   */
  public void addConfiguredInstance(final Instance instance)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending identity domain property
    this.delegate.add(instance.delegate);
  }
}