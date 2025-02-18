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

    File        :   Entry.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Entry.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.entry.type;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.spi.handler.EntryHandler;

////////////////////////////////////////////////////////////////////////////////
// class Entry
// ~~~~~ ~~~~~
/**
 ** <code>Entry</code> represents an Entry in the Directory Information Tree
 ** (DIT) that might be created, updated or deleted after or during an
 ** operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Entry extends Container {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entry</code> handler that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Entry() {
    // ensure inheritance
    super(new EntryHandler.EntryInstance());
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
    ((EntryHandler.EntryInstance)this.delegate).value(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link EntryHandler.EntryInstance} delegate of Directory
   ** Service Entry to handle.
   **
   ** @return                    the {@link EntryHandler.EntryInstance} delegate of
   **                            Directory Service Entry to handle.
   */
  public final EntryHandler.EntryInstance instance() {
    if (isReference())
      return ((Entry)getCheckedRef()).instance();

    return (EntryHandler.EntryInstance)this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Attribute}.
   **
   ** @param  name               the name of the attribute.
   ** @param  value              the value for <code>name</code> to set on the
   **                            entry.
   **
   ** @throws BuildException     if the specified value pair is already
   **                            part of the parameter mapping.
   */
  public final void addAttribute(final String name, final String value)
    throws BuildException {

    addConfiguredAttribute(new Attribute(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAttribute
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} to add.
   **
   ** @throws BuildException     if the specified {@link Attribute} is already
   **                            part of the parameter mapping.
   */
  public final void addConfiguredAttribute(final Attribute attribute)
    throws BuildException {

    checkAttributesAllowed();
    final Collection<Object>      collection = attribute.value();
    final HashMap<String, Object> parameter  = this.delegate.parameter();
    if (parameter.containsKey(attribute.name())) {
      Object instance = parameter.get(attribute.name());
      if (instance instanceof List) {
        ((List)instance).addAll(collection);
      }
      else {
        final List<Object> summary = new ArrayList<Object>();
        summary.add(parameter.get(attribute.name()));
        summary.addAll(collection);
        parameter.put(attribute.name(), summary);
      }
    }
    else {
      parameter.put(attribute.name(), collection);
    }
  }
}
