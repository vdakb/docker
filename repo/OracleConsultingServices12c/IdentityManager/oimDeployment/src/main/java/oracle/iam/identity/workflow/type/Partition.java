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

    File        :   Composite.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Composite.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceCommand;

import oracle.iam.identity.common.spi.PartitionInstance;

////////////////////////////////////////////////////////////////////////////////
// class Partition
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Partition</code> represents a partition in Oracle SOA Suite that might
 ** be created, destroyed, retired or activated after or during a deployment
 ** operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Partition extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the allowed server types in alphabetical order
  private static final String[] registry = {
    ServiceCommand.activate.id()
  , ServiceCommand.create.id()
  , ServiceCommand.destroy.id()
  , ServiceCommand.retire.id()
  , ServiceCommand.start.id()
  , ServiceCommand.stop.id()
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final PartitionInstance delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Operation
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Operation</code> defines the attribute values that can be passed to
   ** a partition control operation.
   */
  public static class Operation extends EnumeratedAttribute {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Operation</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Operation() {
      // ensure inheritance
      super();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the value of the oparation a task will execute.
     **
     ** @return                    the value of the oparation a task will
     **                            execute.
     */
    public final String value() {
      return super.getValue();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   getValues (EnumeratedAttribute)
    /**
     ** The only method a subclass needs to implement.
     **
     ** @return                  an array holding all possible values of the
     **                          enumeration. The order of elements must be
     **                          fixed so that indexOfValue(String) always
     **                          return the same index for the same value.
     */
    @Override
    public String[] getValues() {
      return registry;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Partition</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Partition() {
    // ensure inheritance
    this(new PartitionInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Partition</code> task that use the passed
   ** {@link PartitionInstance} as the value wrapper.
   **
   ** @param  instance           the {@link PartitionInstance} receiving the
   **                            instance properties.
   */
  protected Partition(final PartitionInstance instance) {
    // ensure inheritance
    super();

    // initialize instance
    this.delegate = instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another <code>Role</code>
   ** instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(this.delegate.name()))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the name of the partition.
   **
   ** @param  name               the name of the partition.
   */
  public final void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperation
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>oparation</code>.
   **
   ** @param  operation          the operation to execute in Oracle SOA Suite.
   */
  public void setOperation(final Operation operation) {
    checkAttributesAllowed();
    this.delegate.command(PartitionInstance.Command.valueOf(operation.value()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link PartitionInstance} delegate of Oracle SOA Suite to
   ** handle.
   **
   ** @return                    the {@link PartitionInstance} delegate of
   **                            Oracle SOA Suite object.
   */
  public PartitionInstance instance() {
    if (isReference())
      return ((Partition)getCheckedRef()).instance();

    return this.delegate;
  }
}