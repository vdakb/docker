/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   ProcessForm.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProcessForm.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.List;
import java.util.ArrayList;

import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ProcessDefinition;

////////////////////////////////////////////////////////////////////////////////
// abstract class ProcessForm
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** A <code>Form Definition</code> value holder.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProcessForm extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  /** the column suffix for the internal identifier column */
  public static final String KEY              = "KEY";

  /** the column suffix for the data level column */
  public static final String DATA_LEVEL       = "DATA_LEVEL";

  /** the column suffix for the version column */
  public static final String VERSION          = "VERSION";

  /** the column suffix for the rowversion column */
  public static final String ROW_VERSION      = "ROWVER";

  /** the column suffix for the last updated column */
  public static final String CREATE           = "CREATE";

  /** the column suffix for the last updated by column */
  public static final String CREATE_BY        = "CREATEBY";

  /** the column suffix for the last updated column */
  public static final String UPDATE           = "UPDATE";

  /** the column suffix for the last updated by column */
  public static final String UPDATE_BY        = "UPDATEBY";

  /** the column suffix for the note column */
  public static final String NOTE             = "NOTE";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7217373666297542328")
  private static final long  serialVersionUID = 655971243105253373L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /** the {@link List} of default form fields like key, creared and updated etc. */
  private final List<String> standard         = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Form Definition</code> which is associated with the
   ** specified task.
   ** <p>
   ** Convinience Constructor if the information provided by a <code>Map</code>.
   **
   ** @param  formKey            the internal system identifier of the
   **                            <code>Form Definition</code> to load.
   ** @param  formName           the name of the <code>Form Definition</code>.
   */
  public ProcessForm(final String formKey, final String formName) {
    this(Long.parseLong(formKey), formName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Form Definition</code> which is associated with the
   ** specified task.
   **
   ** @param  formKey            the internal system identifier of the
   **                            <code>Form Definition</code> to load.
   ** @param  formName           the name of the <code>Form Definition</code>.
   */
  public ProcessForm(final long formKey, final String formName) {
    // ensure inheritance
    super(formKey, formName);

    // initialize the list default fields
    this.standard.add(formName + "_" + KEY);
    this.standard.add(formName + "_" + DATA_LEVEL);
    this.standard.add(formName + "_" + VERSION);
    this.standard.add(formName + "_" + ROW_VERSION);
    this.standard.add(formName + "_" + CREATE);
    this.standard.add(formName + "_" + CREATE_BY);
    this.standard.add(formName + "_" + UPDATE);
    this.standard.add(formName + "_" + UPDATE_BY);
    this.standard.add(formName + "_" + NOTE);

    // the following filed names are not in the naming schema
    this.standard.add(ProcessDefinition.INSTANCE_KEY);
    this.standard.add(AccessPolicy.KEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orderableFor
  /**
   ** Whether the passed field name is in the {@link List} of default columns or
   ** not.
   **
   ** @param  columnName         the field name of the process form to verify if
   **                            its a standard filed.
   **
   ** @return                    <code>true</code> if the passed column name is
   **                            a default column; otherwise <code>false</code>
   */
  public final boolean isDefault(final String columnName) {
    return this.standard.contains(columnName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldName
  /**
   ** Returns a qualified field name of the process form.
   ** <p>
   ** The naming convention of a field name is:
   ** <pre>
   **   <code>form_name</code>_<code>field_name</code>.
   ** </pre>.
   ** The qualifier <code>form_name</code> is passed to the constructor of this
   ** instance and constant during the lifetime.
   **
   ** @param  suffix             the string to be appended to build a qualified
   **                            field name of the process form.
   **
   ** @return                    a qualified field name of the process form.
   */
  public final String fieldName(final String suffix) {
    return this.name  + "_" + suffix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elements (XMLSerializable)
  /**
   ** Returns the XML element tag to marshal/unmarshal multiple entities of
   ** this type.
   **
   ** @return                    the XML element tag to marshal/unmarshal
   **                            multiple entity of this type.
   */
  @Override
  public final String elements() {
    return "Forms";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (XMLSerializable)
  /**
   ** Returns the XML element tag to marshal/unmarshal a single entity of this
   ** type.
   **
   ** @return                    the XML element tag to marshal/unmarshal a
   **                            single entity of this type.
   */
  @Override
  public final String element() {
    return "Form";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity (Entity)
  /**
   ** Returns the XML name used for informational purpose with an end user.
   **
   ** @return                    the XML name used for informational purpose
   **                            with an end user.
   */
  @Override
  public final String entity() {
    return TaskBundle.string(TaskMessage.ENTITY_FORM);
  }
}