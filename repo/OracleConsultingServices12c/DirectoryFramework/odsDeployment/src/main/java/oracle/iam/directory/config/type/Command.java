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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities

    File        :   Command.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Command.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.config.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.directory.common.type.Parameter;

import oracle.iam.directory.common.spi.instance.CommandInstance;

////////////////////////////////////////////////////////////////////////////////
// class Command
// ~~~~~ ~~~~~~~
/**
 ** <code>Command</code> defines the attribute restriction on values that can be
 ** passed to an {@link CommandInstance} to specify input/output options.
 ** <p>
 ** These input/output options are used in interaction with the directory server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Command extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final CommandInstance delegate = new CommandInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Command</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Command() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT kernel to inject the argument for parameter name.
   **
   ** @param  name             the name of the Identity Manager object this
   **                          category wraps.
   */
  public void setName(final String name) {
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link CommandInstance} delegate of Directory Service command
   ** to handle.
   **
   ** @return                    the {@link CommandInstance} delegate.
   */
  public final CommandInstance instance() {
    if (isReference())
      return ((Command)getCheckedRef()).instance();

    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredOption
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Option}.
   **
   ** @param  option             the subject of configure.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>ImportSet</code>
   */
  public void addConfiguredOption(final Option option)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    this.delegate.option(option.getValue(), option.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredParameter
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Parameter}.
   **
   ** @param  property           the subject of configure.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Command</code>
   */
  public void addConfiguredParameter(final Parameter property)
    throws BuildException {

    this.delegate.addParameter(property.name(), property.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredProperty
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Property}.
   **
   ** @param  property           the subject of configure.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Command</code>
   */
  public void addConfiguredProperty(final Property property)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    this.delegate.add(property.instance());
  }
}