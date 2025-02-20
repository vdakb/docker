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

    File        :   ProcessForm.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ProcessForm.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ProcessForm
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>ProcessForm</code> encapsulate a Identity Manager process form which
 ** belongs to a provisioning process.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProcessForm {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PROPERTY_REQUIRED       = "Required";
  public static final String PROPERTY_VISIBLE        = "Visible Field";
  public static final String PROPERTY_LOOKUP_CODE    = "Lookup Code";
  public static final String PROPERTY_LOOKUP_QUERY   = "Lookup Query";
  public static final String PROPERTY_LOOKUP_COLUMN  = "Column Names";
  public static final String PROPERTY_LOOKUP_DISPLAY = "Lookup Column Name";
  public static final String PROPERTY_LOOKUP_TYPE    = "Type";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final long                 key;
  final int                  version;
  final String               physicalName;
  final String               logicalName;
  final List<Field>          fieldSet                = new ArrayList<Field>();
  final List<ProcessForm>    childSet                = new ArrayList<ProcessForm>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Field
  // ~~~~~ ~~~~~
  /**
   ** Helper class, holds the nested <code>fieldelement</code> values.
   */
  public class Field {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final long                key;
    final String              name;
    final String              label;
    final String              type;
    final String              variant;
    final int                 length;
    final Map<String, String> properties = new HashMap<String, String>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     **
     ** @param  key              the system identifier of the process form field
     **                          to add.
     ** @param  name             the physical name of the process form field
     **                          to add.
     ** @param  label            the logical name of the process form field
     **                          to add.
     ** @param  type             the physical type of the process form field
     **                          to add.
     ** @param  variant          the logical type of the process form field
     **                          to add.
     ** @param  length           the storage length of the process form field
     **                          to add.
     */
    public Field(final long key, final String name, final String label, final String type, final String variant, final int length) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.key     = key;
      this.name    = name;
      this.label   = label;
      this.type    = type;
      this.variant = variant;
      this.length  = length;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: key
    /**
     ** Returns the system identifier of the process form field.
     **
     ** @return                  the system identifier of the process form
     **                          field.
     */
    public long key() {
      return this.key;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the physical name of the process form field.
     **
     ** @return                  the physical name of the process form field.
     */
    public String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: label
    /**
     ** Returns the logical name of the process form field.
     **
     ** @return                  the logical name of the process form field.
     */
    public String label() {
      return this.label;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the physical type of the process form field.
     **
     ** @return                  the physical type of the process form field.
     */
    public String type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: variant
    /**
     ** Returns the logical type of the process form field.
     **
     ** @return                  the logical type of the process form field.
     */
    public String variant() {
      return this.variant;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: length
    /**
     ** Returns the physical length of the process form field.
     **
     ** @return                  the physical length of the process form field.
     */
    public int length() {
      return this.length;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   propertySize
    /**
     ** Returns the numbers of properties assigned to this Process Form Field of
     ** Identity Manager as an {@link Iterator}.
     **
     ** @return                    the numbers of properties assigned to
     **                            this Process Form instance of Identity
     **                            Manager as an {@link Iterator}.
     */
    public int propertySize() {
      return this.properties.size();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   propertyKeyIterator
    /**
     ** Returns the properties assigned to this Process Form Field of Oracle
     ** Identity Manager as an {@link Iterator}.
     **
     ** @return                    the fields assigned to this Process Form
     **                            instance of Identity Manager as an
     **                            {@link Iterator}.
     */
    public Iterator<String> propertyKeyIterator() {
      return this.properties.keySet().iterator();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   propertyValue
    /**
     ** Returns the property value for name assigned to this Process Form Field
     ** of Identity Manager as an {@link Iterator}.
     **
     ** @param   name              the name of the property the valaue has to
     **                            returnd for.
     **
     ** @return                    the property value for name assigned to this
     **                            Process Form instance of Identity Manager as
     **                            an {@link Iterator}.
     */
    public String propertyValue(final String name) {
      return this.properties.get(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addProperty
    /**
     ** Adds a named value pair as a property to this process form field.
     **
     ** @param  name             the system identifier of the property to add to
     **                          this process form field.
     ** @param  value            the physical value for name of the property to
     **                          add to this process form field.
     */
    public void addProperty(final String name, final String value)
      throws BuildException {

      if (this.properties.containsKey(name))
        throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_FIELDPROPERTY_ONLYONCE, name));

      this.properties.put(name, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ProcessForm</code> with the specified properties.
   **
   ** @param  key               the system identifier of the process form.
   ** @param  version           the version number of the process form.
   ** @param  physicalName      the physical name of the process form.
   ** @param  logicalName       the logical name of the process form.
   */
  public ProcessForm(final long key, final int version, final String physicalName, final String logicalName) {
    // ensure inheritance
    super();

    this.key          = key;
    this.version      = version;
    this.physicalName = physicalName;
    this.logicalName  = logicalName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the physical name of the process form.
   **
   ** @return                    the system identifier of the process form.
   */
  public long key() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the physical name of the process form.
   **
   ** @return                    the system version of the process form.
   */
  public int version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   physicalName
  /**
   ** Returns the physical name of the process form.
   **
   ** @return                    the physical name of the process form
   */
  public String physicalName() {
    return this.physicalName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logicalName
  /**
   ** Returns the logical name of the process form.
   **
   ** @return                    the logical name of the process form
   */
  public String logicalName() {
    return this.logicalName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   field
  /**
   ** Returns the fields assigned to this Process Form instance of Oracle
   ** Identity Manager.
   **
   ** @return                    the fields assigned to this Process Form
   **                            instance of Identity Manager.
   */
  public List<Field> field() {
    return this.fieldSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldIterator
  /**
   ** Returns the fields assigned to this Process Form instance of Oracle
   ** Identity Manager as an {@link Iterator}.
   **
   ** @return                    the fields assigned to this Process Form
   **                            instance of Identity Manager as an
   **                            {@link Iterator}.
   */
  public Iterator<Field> fieldIterator() {
    return this.fieldSet.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   child
  /**
   ** Returns the child forms assigned to this Process Form instance of Oracle
   ** Identity Manager.
   **
   ** @return                    the childs forms assigned to this Process Form
   **                            instance of Identity Manager.
   */
  public List<ProcessForm> child() {
    return this.childSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   childIterator
  /**
   ** Returns the childs assigned to this Process Form instance of Oracle
   ** Identity Manager as an {@link Iterator}.
   **
   ** @return                    the childs forms assigned to this Process Form
   **                            instance of Identity Manager as an
   **                            {@link Iterator}.
   */
  public Iterator<ProcessForm> childIterator() {
    return this.childSet.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProcessForm
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link ProcessForm} as a child form of this form.
   **
   ** @param  form              the subject of substitution.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>ProcessForm</code>
   */
  public void addProcessForm(final ProcessForm form)
    throws BuildException {

    if (this.childSet.contains(form.physicalName))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_PROCESSFORM_ONLYONCE, form.physicalName));

    this.childSet.add(form);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProcessField
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Field}.
   **
   ** @param  field              the subject of substitution.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Field</code>
   */
  public void addProcessField(final Field field)
    throws BuildException {

    if (this.fieldSet.contains(field.name))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_PROCESSFIELD_ONLYONCE, field.name));

    this.fieldSet.add(field);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProcessField
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Field}.
   **
   ** @param  key                the system identifier of the process form field
   **                            to add.
   ** @param  name               the physical name of the process form field
   **                            to add.
   ** @param  label              the logical name of the process form field
   **                            to add.
   ** @param  type               the physical type of the process form field
   **                            to add.
   ** @param  variant            the logical type of the process form field
   **                            to add.
   ** @param  length             the storage length of the process form field
   **                            to add.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Field</code>
   */
  public void addProcessField(final long key, final String name, final String label, final String type, final String variant, final int length)
    throws BuildException {

    if (this.fieldSet.contains(name))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_PROCESSFIELD_ONLYONCE, name));

    this.fieldSet.add(createProcessField(key, name, label, type, variant, length));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessField
  /**
   ** Factory method to create a Process Form {@link Field}.
   **
   ** @param  key               the system identifier of the process form field
   **                           to add.
   ** @param  name              the physical name of the process form field
   **                           to add.
   ** @param  label             the logical name of the process form field
   **                           to add.
   ** @param  type              the physical type of the process form field
   **                           to add.
   ** @param  variant           the logical type of the process form field
   **                           to add.
   ** @param  length            the storage length of the process form field
   **                           to add.
   **
   ** @return                   the {@link Field} instance wraping the specified
   **                           parameters.
   */
  public Field createProcessField(final long key, final String name, final String label, final String type, final String variant, final int length) {
    return new Field(key, name, label, type, variant, length);
  }
}