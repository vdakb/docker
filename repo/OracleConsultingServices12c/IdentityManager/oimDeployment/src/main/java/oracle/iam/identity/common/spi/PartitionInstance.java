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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   PartitionInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PartitionInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/
package oracle.iam.identity.common.spi;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class PartitionInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>PartitionInstance</code> represents the common parts a partition in
 ** Oracle SOA Suite that might be listed, created or deleted during an import
 ** operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PartitionInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Command command = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  public static enum Command {
    /** command string used to create a partition */
    create,
    /** command string used to destroy a partition */
    destroy,
    /** command string used to start all composites in a partition */
    start,
    /** command string used to stop all composites in a partition */
    stop,
    /** command string used to activate all composites in a partition */
    activate,
    /** command string used to retire all composites in a partition */
    retire;
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PartitionInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PartitionInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   command
  /**
   ** Called to inject the argument for parameter
   ** <code>command</code>.
   **
   ** @param  command            the command context to handle in Oracle SOA
   **                            Suite.
   */
  public final void command(final Command command) {
    this.command = command;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   command
  /**
   ** Returns the command to handle in Oracle SOA Suite.
   **
   ** @return                    the command context to handle in Oracle SOA
   **                            Suite.
   */
  public final Command command() {
    return this.command;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException   in case an error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    // ensure inheritance
    super.validate();

    // enforce validation of mandatory attributes if requested
    if (this.command == null)
      handleAttributeMissing("command");
  }
}