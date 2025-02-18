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

    File        :   LookupValueAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LookupValueAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import Thor.API.tcResultSet;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class LookupValueAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~
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
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class LookupValueAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOOKUP_KEY       = "lookupKey";
  public static final String VALUE_KEY        = "valueKey";
  public static final String ENCODED          = "encoded";
  public static final String DECODED          = "decoded";
  public static final String DISABLED         = "disabled";
  public static final String NOTE             = "note";
  public static final String ACTION           = "pendingAction";

  public static final String NAME             = "lookupName";

  public static final String NIL              = "nil";
  public static final String ADD              = "add";
  public static final String DEL              = "del";
  public static final String MOD              = "mod";

  public static final String DISABLED_TRUE    = "1";
  public static final String DISABLED_FASLE   = "0";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5765667466621247076")
  private static final long  serialVersionUID = -9197039686718481787L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String             valueKey;
  @ModelAttr
  private String             lookupKey;
  @ModelAttr
  private String             encoded;
  @ModelAttr
  private String             decoded;
  @ModelAttr
  private String             disabled;
  @ModelAttr
  private String             note;
  @ModelAttr
  private String             pendingAction;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupValueAdapter</code> values object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupValueAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupValueAdapter</code> values object which
   ** belongs to the specified <code>lookupKey</code>.
   **
   ** @param  lookupKey          the <code>Lookup Definition</code> this
   **                            <code>LookupValueAdapter</code> belongs to.
   ** @param  valueKey           the <code>Lookup Value</code> this
   **                            <code>LookupValueAdapter</code> belongs to.
   */
  public LookupValueAdapter(final String lookupKey, final String valueKey) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.lookupKey     = lookupKey;
    this.valueKey      = valueKey;
    this.disabled      = "0";
    this.pendingAction = ADD;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupValueAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public LookupValueAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setValueKey
  /**
   ** Sets the value of the value key property.
   **
   ** @param  value              the value of the value key property.
   **                            Allowed object is {@link String}.
   */
  public void setValueKey(final String value) {
    this.valueKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getValueKey
  /**
   ** Returns the value of the value key property.
   **
   ** @return                    the value of the value key property.
   **                            Possible object is {@link String}.
   */
  public String getValueKey() {
    return this.valueKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setLookupKey
  /**
   ** Sets the value of the lookup key property.
   **
   ** @param  value              the value of the lookup key property.
   **                            Allowed object is {@link String}.
   */
  public void setLookupKey(final String value) {
    this.lookupKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLookupKey
  /**
   ** Returns the value of the lookup key property.
   **
   ** @return                    the value of the lookup key property.
   **                            Possible object is {@link String}.
   */
  public String getLookupKey() {
    return this.lookupKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEncoded
  /**
   ** Sets the value of the encoded property.
   **
   ** @param  value              the value of the encoded property.
   **                            Allowed object is {@link String}.
   */
  public void setEncoded(final String value) {
    this.encoded = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEncoded
  /**
   ** Returns the value of the encoded property.
   **
   ** @return                    the value of the encoded property.
   **                            Possible object is {@link String}.
   */
  public String getEncoded() {
    return this.encoded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDecoded
  /**
   ** Sets the value of the decoded property.
   **
   ** @param  value              the value of the decoded property.
   **                            Possible object is {@link String}.
   */
  public void setDecoded(final String value) {
    this.decoded = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDecoded
  /**
   ** Returns the value of the decoded property.
   **
   ** @return                    the value of the decoded property.
   **                            Possible object is {@link String}.
   */
  public String getDecoded() {
    return this.decoded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDisabled
  /**
   ** Sets the value of the disabled property.
   **
   ** @param  value              the value of the disabled property.
   **                            Allowed object is {@link String}.
   */
  public void setDisabled(final String value) {
    this.disabled = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDisabled
  /**
   ** Returns the value of the disabled property.
   **
   ** @return                    the value of the disabled property.
   **                            Possible object is {@link String}.
   */
  public String getDisabled() {
    return this.disabled;
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
   **                            Possible object is {@link String}.
   */
  public String getNote() {
    return this.note;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPendingAction
  /**
   ** Sets the value of the pendingAction property.
   **
   ** @param  value              the value of the pendingAction property.
   **                            Allowed object is {@link String}.
   */
  public void setPendingAction(final String value) {
    this.pendingAction = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPendingAction
  /**
   ** Returns the value of the pendingAction property.
   **
   ** @return                    the value of the pendingAction property.
   **                            Possible object is {@link String}.
   */
  public String getPendingAction() {
    return this.pendingAction;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>LookupValueAdapter</code> from a
   ** {@link tcResultSet}.
   ** <p>
   ** <b>Note</b>:
   ** The {@link tcResultSet} pointer needs to be positioned correctly before
   ** this method is called.
   **
   ** @param  resultSet          the {@link tcResultSet} provider to obtain the
   **                            attribute values from.
   **                            Allowed object is {@link tcResultSet}.
   **
   ** @return                    the {@link LookupValueAdapter} constructed
   **                            from the specfied <code>resultSet</code>.
   */
  public static LookupValueAdapter build(final tcResultSet resultSet) {
    final LookupValueAdapter mab = new LookupValueAdapter();
    return mab;
  }
}