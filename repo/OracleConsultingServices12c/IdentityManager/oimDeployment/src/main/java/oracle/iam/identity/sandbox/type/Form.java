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

    File        :   Account.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Account.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.DataType;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.FormInstance;

////////////////////////////////////////////////////////////////////////////////
// class Form
// ~~~~~ ~~~~
/**
 ** <code>Form</code> defines the attribute restriction on values that can
 ** be passed as a nested parameter used for multi-valued form information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Form extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected FormInstance delegate = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Form</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Form() {
    // ensure inheritance
    this(new FormInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Form</code> either account or multi-valued.
   **
   ** @param  instance           the specific subclass of the form to keep the
   **                            values provided on the ANT type.
   */
  protected Form(final FormInstance instance) {
    // ensure inheritance
    super();

    // initilaize instance attributes
    this.delegate = instance;
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
   ** <code>header</code>.
   **
   ** @param  value              the header expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setHeader(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Hint.HEADER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRendered
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>rendered</code> to specify the expression under
   ** which condition a form is rendered or not.
   **
   ** @param  value              the rendered expression for the form to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setRendered(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Hint.RENDERED, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRows
  /**
   ** Call by the ANT deployment to inject the number of rows after which to
   ** start a new column.
   **
   ** @param  value              the number of rows after which
   **                            to start a new column.
   **                            <br>
   **                            The number of rows actually rendered depends
   **                            also on the <code>col</code> attribute. When
   **                            the number of children rendered equals the rows
   **                            value, the next child is rendered in the next
   **                            column. If the children will not fit in the
   **                            given number of rows and columns, the number of
   **                            rows will increase to accommodate the children.
   **                            <br>
   **                            When left blank, rows defaults to the maximum
   **                            integer value.
   */
  public final void setRows(final int value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.ROW.id(), String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxColumns
  /**
   ** Call by the ANT deployment to inject the maximum number of columns to
   ** show.
   **
   ** @param  value              the maximum number of columns to show.
   **                            <br>
   **                            This attribute defaults to 2 and 1 on PDAs. If
   **                            this panelFormLayout is inside of another
   **                            panelFormLayout, this will always be 1.
   */
  public final void setMaxColumns(final int value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Panel.Hint.COLUMN.id(), String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntitlement
  /**
   ** Called to inject the <code>entitlement</code> to indicate that a separate
   ** page is needed for a <code>Process Form</code> belonging to the request of
   ** an <code>Entitlement</code> in Identity Manager.
   **
   ** @param  entitlement        the <code>entitlement</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void setEntitlement(final boolean entitlement) {
    checkAttributesAllowed();
    this.delegate.entitlement(entitlement);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFilterVisible
  /**
   ** Called to inject the <code>entitlement</code> to indicate that the
   ** filter criteria are visible.
   **
   ** @param  filterVisible      the <code>filterVisible</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void setFilterVisible(final boolean filterVisible) {
    checkAttributesAllowed();
    this.delegate.filterVisible(filterVisible);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRowBanding
  /**
   ** Called to inject the <code>rowBanding</code> to indicate that row banding
   ** in a table view is needed for a <code>Process Form</code>.
   **
   ** @param  rowBanding         the <code>rowBanding</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void setRowBanding(final boolean rowBanding) {
    checkAttributesAllowed();
    this.delegate.rowBanding(rowBanding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setColumnStretching
  /**
   ** Called to inject the <code>columnStretching</code> to indicate how the
   ** columns are strechted in a table view for a <code>Process Form</code>.
   **
   ** @param  columnStretching   the <code>columnStretching</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void setColumnStretching(final String columnStretching) {
    checkAttributesAllowed();
    this.delegate.columnStretching(columnStretching);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link FormInstance} this ANT type wrappes.
   **
   ** @return                    the {@link FormInstance} this ANT type
   **                            wrappes.
   */
  public FormInstance delegate() {
    return this.delegate;
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

    this.delegate.add(instance.delegate());
  }
}