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
    Subsystem   :   System Provisioning Management

    File        :   ReconciliationEventHistoryAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationEventHistoryAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class ReconciliationEventHistoryAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by Reconciliation Event customization.
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
public class ReconciliationEventHistoryAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK                = "historyKey";
  public static final String EVENT_KEY         = "eventKey";
  public static final String FK                = "Bind_" + EVENT_KEY;
  public static final String ACTION_PERFORMED  = "actionPerformed";
  public static final String STATUS            = "status";
  public static final String NOTE              = "not";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8403743394174927591")
  private static final long  serialVersionUID  = -4881672989226617387L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               historyKey;
  @ModelAttr
  private Long               eventKey;
  @ModelAttr
  private String             actionPerformed;
  @ModelAttr
  private String             status;
  @ModelAttr
  private String             note;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReconciliationEventHistoryAdapter</code> values
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private ReconciliationEventHistoryAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReconciliationEventHistoryAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public ReconciliationEventHistoryAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReconciliationEventHistoryAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public ReconciliationEventHistoryAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setHistoryKey
  /**
   ** Sets the value of the historyKey property.
   **
   ** @param  value              the value of the historyKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setHistoryKey(final Long value) {
    this.historyKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getHistoryKey
  /**
   ** Returns the value of the historyKey property.
   **
   ** @return                    the value of the historyKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getHistoryKey() {
    return this.historyKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEventKey
  /**
   ** Sets the value of the eventKey property.
   **
   ** @param  value              the value of the eventKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setEventKey(final Long value) {
    this.eventKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEventKey
  /**
   ** Returns the value of the eventKey property.
   **
   ** @return                    the value of the eventKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getEventKey() {
    return this.eventKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setActionPerformed
  /**
   ** Sets the value of the actionPerformed property.
   **
   ** @param  value              the value of the actionPerformed property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setActionPerformed(final String value) {
    this.actionPerformed = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getActionPerformed
  /**
   ** Returns the value of the actionPerformed property.
   **
   ** @return                    the value of the actionPerformed property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getActionPerformed() {
    return this.actionPerformed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStatus
  /**
   ** Sets the value of the status property.
   **
   ** @param  value              the value of the status property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setStatus(final String value) {
    this.status = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getStatus
  /**
   ** Returns the value of the status property.
   **
   ** @return                    the value of the status property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getStatus() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setNote
  /**
   ** Sets the value of the note property.
   **
   ** @param  value              the value of the note property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getNote() {
    return this.note;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create an empty
   ** <code>ReconciliationEventHistoryAdapter</code> with the given profile name.
   **
   ** @return                    the model adapter bean.
   **                            <br>
   **                            Possible object is
   **                            <code>ReconciliationEventHistoryAdapter</code>.
   */
  public static ReconciliationEventHistoryAdapter build() {
    return new ReconciliationEventHistoryAdapter();
  }
}