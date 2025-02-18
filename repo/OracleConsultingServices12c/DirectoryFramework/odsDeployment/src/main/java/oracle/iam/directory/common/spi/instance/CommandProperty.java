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

    File        :   CommandProperty.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CommandProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.spi.instance;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class CommandProperty
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>CommandProperty</code> represents an command wrapper
 ** <code>ConfigurationHandler</code> use to configure a directory.
 ** <p>
 ** Subclasses of this classes providing the data model an implementation of
 ** <code>ConfigurationHandler</code> needs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CommandProperty extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////
  
  private Action action;
  
  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // enum Action
  // ~~~~ ~~~~~~
  /**
   ** The <code>Action</code> defines the behavior of properties
   */
  public enum Action {

    /**
     ** Adds a single value to a property, where property is the name of the
     ** property and value is the single value to be added.
     */
    add("add"),
    /**
     ** Assigns a value to a property, where property is the name of the
     ** property and value is the single value to be assigned.
     ** <br>
     ** Specify the same property multiple times to assign more than one value to it.
     */
    set("set"),
    /**
     ** Removes a single value from a property, where property is the name of
     ** the property and value is the single value to be removed.
     */
    remove("remove"),
    /**
     ** Resets a property back to its default values, where property is the name
     ** of the property to be reset.
     */
    reset ("reset")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Action</code>
     **
     ** @param  id               the <code>ConfigurationHandler</code> accepts
     **                          an option in either its short form (for
     **                          example, <code>-h hostname</code>) or its long
     **                          form equivalent (for example,
     **                          <code>--hostname hostname</code>).
     **                          <br>
     **                          Per design only the long form equivalent will
     **                          be accepted.
     **                          Allowed object is {@link String}.
     */
    Action(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value of the id property.
     **
     ** @return                  the value of the id property.
     **                          Possible object is {@link String}.
     */
    public String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper input/output option from the given
     ** string value.
     **
     ** @param  id                 the string value the input/output option
     **                            should be returned for.
     **
     ** @return                    input/output option.
     **                            Possible object is <code>Option</code>.
     */
    public static Action from(final String id) {
      for (Action cursor : Action.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argument
    /**
     ** Returns the string representation of input/output option.
     **
     ** @return                  the string representation of input/output option.
     **                          Nothing else the id prepended with two dashes.
     */
    public String argument() {
      return String.format("--%s", this.id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CommandProperty</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CommandProperty() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name context to handle in Oracle Weblogic
   **                            Domain server entity instance.
   **
   ** @return                    the <code>CommandProperty</code> for method
   **                            chaining purpose.
   */
  public final CommandProperty name(final String name) {
    this.action = Action.from(name);

    // ensure inheritance
    super.name(name);
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   parameterArguments
  /**
   ** Returns the value of the input/output option property as a list of command
   ** line arguments.
   **
   ** @return                   the value of the input/output option property as
   **                           a list of command line arguments.
   */
  public List<String> parameterArguments() {
    final List<String> arguments = new ArrayList<String>();
    for (Map.Entry<String, Object> cursor : parameter().entrySet()) {
      arguments.add(String.format("--%s", this.action.id));
      if (this.action == Action.reset)
        arguments.add(cursor.getKey());
      else
        arguments.add(String.format("%s:%s", cursor.getKey(), cursor.getValue().toString()));
    }
    return arguments;
  }
}