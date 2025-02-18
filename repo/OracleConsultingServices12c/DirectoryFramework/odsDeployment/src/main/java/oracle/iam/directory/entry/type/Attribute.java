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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   Value.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Value.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.entry.type;

import java.util.Collection;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.type.Value;

import oracle.iam.directory.common.spi.handler.EntryHandler;

////////////////////////////////////////////////////////////////////////////////
// class Attribute
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Entry</code> represents an attribute of an entry in the Directory
 ** Information Tree (DIT) that might be created, updated or deleted after or
 ** during an operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Attribute extends Container {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> handler that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Attribute() {
    // ensure inheritance
    super(new EntryHandler.AttributeInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> with the specified name.
   **
   ** @param  name               the value set for the name property.
   */
  protected Attribute(final String name) {
    // ensure inheritance
    super(new EntryHandler.AttributeInstance(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> with the specified name.
   **
   ** @param  name               the value set for the name property.
   ** @param  value              the value object to associate with the name.
   */
  protected Attribute(final String name, final String value) {
    // ensure inheritance
    super(new EntryHandler.AttributeInstance(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>value</code>.
   **
   ** @param  value              the value to set.
   */
  public void setValue(final String value) {
    this.delegate.parameter().put(value, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value instance.
   **
   ** @return                    the value instance.
   */
  public final Collection<Object> value() {
    return this.delegate.parameter().values();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link EntryHandler.AttributeInstance} delegate of Directory
   ** Service Attribute to handle.
   **
   ** @return                    the {@link EntryHandler.AttributeInstance}
   **                            delegate of Directory Service Attribute to
   **                            handle.
   */
  public final EntryHandler.AttributeInstance instance() {
    if (isReference())
      return ((Attribute)getCheckedRef()).instance();

    return (EntryHandler.AttributeInstance)this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addValue
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Value}.
   **
   ** @param  value              the value to add.
   */
  public final void addValue(final String value)
    throws BuildException {

    addConfiguredValue(new Value(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredValue
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Value}.
   **
   ** @param  value              the {@link Value} to add.
   */
  public final void addConfiguredValue(final Value value) {
    this.delegate.addParameter(value.value(), value.value());
  }
}