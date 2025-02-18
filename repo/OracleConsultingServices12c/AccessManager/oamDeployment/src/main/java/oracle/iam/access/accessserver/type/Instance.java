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

package oracle.iam.access.accessserver.type;

import org.apache.tools.ant.BuildException;

import oracle.iam.access.common.type.FeatureInstance;

import oracle.iam.access.common.spi.AccessServerInstance;
import oracle.iam.access.common.spi.AccessServerProperty;

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

  final AccessServerInstance delegate = new AccessServerInstance();

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
  // Method:   delegate
  /**
   ** Returns the {@link AccessServerInstance} delegate of Access Server
   ** object to handle.
   **
   ** @return                    the {@link AccessServerInstance} delegate.
   */
  public final AccessServerInstance delegate() {
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

    // assign the correspondending access server property
    this.delegate.add(AccessServerProperty.from(parameter.getValue()), parameter.name());
  }
}