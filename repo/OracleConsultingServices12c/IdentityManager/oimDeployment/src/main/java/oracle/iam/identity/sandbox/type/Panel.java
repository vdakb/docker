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

    System      :   Identity Manager Library
    Subsystem   :   Sandbox Service Utilities 11g

    File        :   Panel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Panel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.type;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.FormInstance;

////////////////////////////////////////////////////////////////////////////////
// class Panel
// ~~~~~ ~~~~~~~
/**
 ** <code>Panel</code> defines the accumulated attribute of a form
 ** be passed as a nested structural layouts used by account form information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Panel extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected FormInstance.Panel     delegate  = new FormInstance.Panel();
  protected List<Attribute>        attribute = new ArrayList<Attribute>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Panel</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Panel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another <code>Sandbox</code>
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
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the sandbox in Identity Manager to
   **                            handle.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHeader
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>label</code>.
   **
   ** @param  value              the label expression for the panel header to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setHeader(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.HEADER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRow
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>row</code>.
   **
   ** @param  value              the rows a panel spawns in the UI.
   */
  public void setRow(final int value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.ROW, String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setColumn
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>column</code>.
   **
   ** @param  value              the columns a panel spawns in the UI.
   */
  public void setColumn(final int value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.COLUMN, String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisclosed
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>disclosed</code>.
   **
   ** @param  value              <code>true</code> if the initial rendering
   **                            state is disclosed.
   */
  public void setDisclosed(final boolean value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.DISCLOSED, String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSize
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>size</code>.
   **
   ** @param  value              the header size a panel spawns in the UI.
   */
  public void setSize(final int value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.SIZE, String.valueOf(value));
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFieldWidth
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>fieldWidth</code>.
   **
   ** @param  value              the field width a panel spawns in the UI.
   */
  public void setFieldWidth(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.FIELD_WIDTH, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLabelWidth
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>labelWidth</code>.
   **
   ** @param  value              the label width a panel spawns in the UI.
   */
  public void setLabelWidth(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.LABEL_WIDTH, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAttribute
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Attribute}.
   **
   ** @param  instance           the {@link Attribute} instance to take in
   **                            account during generation.
   **
   ** @throws BuildException     if the {@link Attribute} already contained in
   **                            the collection of this generation operation.
   */
  public void addConfiguredAttribute(final Attribute instance)
    throws BuildException {

    this.attribute.add(instance);
  }
}