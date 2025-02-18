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

    File        :   Attribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Attribute.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.FormInstance;

////////////////////////////////////////////////////////////////////////////////
// class Attribute
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Attribute</code> represents a particular field of a form in Oracle
 ** Identity Manager that is used to create forms.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Attribute extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final FormInstance.Attribute delegate = new FormInstance.Attribute();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Attribute() {
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
  // Method:   setLabel
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>label</code>.
   **
   ** @param  value              the label expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setLabel(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.LABEL, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHint
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>hint</code>.
   **
   ** @param  value              the hint expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setHint(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.TOOLTIP, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTitle
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>title</code>.
   **
   ** @param  value              the title expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setTitle(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.TITLE_LOV, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setReadonly
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>readonly</code>.
   **
   ** @param  value              the readonly expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setReadonly(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.READONLY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRendered
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>rendered</code>.
   **
   ** @param  value              the rendered expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setRendered(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.RENDERED, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSortable
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>sortable</code>.
   **
   ** @param  value              the sortable expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setSortable(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.SORTABLE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFilterable
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>filterable</code>.
   **
   ** @param  value              the filterable expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setFilterable(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.FILTERABLE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFilterable
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>displayWidth</code>.
   **
   ** @param  value              the width expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setDisplayWidth(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.DISPLAY_WIDTH, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNumber
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>number</code>.
   **
   ** @param  value              the numeric expression for the attribute to
   **                            marshal it to the sandbox in Identity Manager.
   */
  public void setNumber(final String value) {
    checkAttributesAllowed();
    this.delegate.addParameter(FormInstance.Attribute.Hint.NUMBER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link FormInstance.Attribute} this ANT type wrappes.
   **
   ** @return                    the {@link FormInstance.Attribute} this ANT
   **                            type wrappes.
   */
  public FormInstance.Attribute delegate() {
    return this.delegate;
  }
}