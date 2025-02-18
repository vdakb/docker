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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Configuration Management

    File        :   LookupAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LookupAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class LookupAdapter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Data Access Object used by Lookup customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the Adapter and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class LookupAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "lookupKey";
  public static final String FK               = "Bind_" + PK;

  public static final String NAME             = "name";
  public static final String MEANING          = "meaning";
  public static final String GROUP            = "group";
  public static final String TYPE             = "type";
  public static final String FIELD            = "field";
  public static final String REQUIRED         = "required";
  public static final String NOTE             = "note";

  public static final String TYPE_LOOKUP      = "l";
  public static final String TYPE_FIELD       = "f";

  public static final String REQUIRED_TRUE    = "1";
  public static final String REQUIRED_FALSE   = "0";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:583295358257907729")
  private static final long  serialVersionUID = -9197039686718481787L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String             lookupKey;
  @ModelAttr
  private String             name;
  @ModelAttr
  private String             meaning;
  @ModelAttr
  private String             group;
  @ModelAttr
  private String             type;
  @ModelAttr
  private String             field;
  @ModelAttr
  private String             required;
  @ModelAttr
  private String             note;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupAdapter</code> values object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public LookupAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setLookupKey
  /**
   ** Sets the value of the lookupKey property.
   **
   ** @param  value              the value of the lookupKey property.
   **                            Allowed object is {@link String}.
   */
  public void setLookupKey(final String value) {
    this.lookupKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLookupKey
  /**
   ** Returns the value of the lookupKey property.
   **
   ** @return                    the value of the lookupKey property.
   **                            Allowed object is {@link String}.
   */
  public String getLookupKey() {
    return this.lookupKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setName
  /**
   ** Sets the value of the code property.
   **
   ** @param  value              the value of the code property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getName
  /**
   ** Returns the value of the code property.
   **
   ** @return                    the value of the code property.
   **                            Allowed object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setMeaning
  /**
   ** Sets the value of the meaning property.
   **
   ** @param  value              the value of the meaning property.
   **                            Allowed object is {@link String}.
   */
  public void setMeaning(final String value) {
    this.meaning = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getMeaning
  /**
   ** Returns the value of the meaning property.
   **
   ** @return                    the value of the meaning property.
   **                            Allowed object is {@link String}.
   */
  public String getMeaning() {
    return this.meaning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setGroup
  /**
   ** Sets the value of the group property.
   **
   ** @param  value              the value of the group property.
   **                            Allowed object is {@link String}.
   */
  public void setGroup(final String value) {
    this.group = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getGroup
  /**
   ** Returns the value of the group property.
   **
   ** @return                    the value of the group property.
   **                            Allowed object is {@link String}.
   */
  public String getGroup() {
    return this.group;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setType
  /**
   ** Sets the value of the type property.
   **
   ** @param  value              the value of the type property.
   **                            Allowed object is {@link String}.
   */
  public void setType(final String value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getType
  /**
   ** Returns the value of the type property.
   **
   ** @return                    the value of the type property.
   **                            Allowed object is {@link String}.
   */
  public String getType() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setField
  /**
   ** Sets the value of the field property.
   **
   ** @param  value              the value of the field property.
   **                            Allowed object is {@link String}.
   */
  public void setField(final String value) {
    this.field = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getField
  /**
   ** Returns the value of the field property.
   **
   ** @return                    the value of the field property.
   **                            Allowed object is {@link String}.
   */
  public String getField() {
    return this.field;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRequired
  /**
   ** Sets the value of the required property.
   **
   ** @param  value              the value of the required property.
   **                            Allowed object is {@link String}.
   */
  public void setRequired(final String value) {
    this.required = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRequired
  /**
   ** Returns the value of the required property.
   **
   ** @return                    the value of the required property.
   **                            Allowed object is {@link String}.
   */
  public String getRequired() {
    return this.required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setNote
  /**
   ** Sets the value of the note property.
   **
   ** @param  value              the value of the note property.
   **                            Allowed object is {@link String}.
   */
  public void setNote(final String value) {
    this.note = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getNote
  /**
   ** Returns the value of the note property.
   **
   ** @return                    the value of the note property.
   **                            Allowed object is {@link String}.
   */
  public String getNote() {
    return this.note;
  }
}