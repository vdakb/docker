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

    File        :   NotificationTemplateAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    NotificationTemplateAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import oracle.jbo.Row;

import oracle.iam.notification.vo.NotificationTemplate;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class NotificationTemplateAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by Notification Template customization.
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
public class NotificationTemplateAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "id";
  public static final String NAME             = "name";
  public static final String EVENT            = "eventname";
  public static final String DESCRIPTION      = "description";
  public static final String STATUS           = "status";
  public static final String SNMP             = "snmpsupported";
  public static final String DATALEVEL        = "dataLevel";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4363310932072938625")
  private static final long  serialVersionUID = -8931948300732097382L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private NotificationTemplate instance;

  @ModelAttr
  private Long                 id;
  @ModelAttr
  private String               name;
  @ModelAttr
  private String               event;
  @ModelAttr
  private String               description;
  @ModelAttr
  private String               status;
  @ModelAttr
  private Boolean              snmp;
  @ModelAttr
  private String               dataLevel;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>NotificationTemplateAdapter</code> values object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public NotificationTemplateAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NotificationTemplateAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public NotificationTemplateAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setInstance
  /**
   ** Sets the value of the instance property.
   **
   ** @param  value              the value of the instance property.
   **                            Allowed object is {@link NotificationTemplate}.
   */
  public void setInstance(final NotificationTemplate value) {
    this.instance = value;
    transfer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setId
  /**
   ** Sets the value of the id property.
   **
   ** @param  value              the value of the id property.
   **                            Allowed object is {@link Long}.
   */
  public void setId(final Long value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getId
  /**
   ** Returns the value of the id property.
   **
   ** @return                    the value of the id property.
   **                            Allowed object is {@link Long}.
   */
  public Long getId() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getName
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   **                            Allowed object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEvent
  /**
   ** Sets the value of the event property.
   **
   ** @param  value              the value of the event property.
   **                            Allowed object is {@link String}.
   */
  public void setEvent(final String value) {
    this.event = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEvent
  /**
   ** Returns the value of the event property.
   **
   ** @return                    the value of the event property.
   **                            Allowed object is {@link String}.
   */
  public String getEvent() {
    return this.event;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              the value of the description property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDescription
  /**
   ** Returns the value of the description property.
   **
   ** @return                    the value of the description property.
   **                            Allowed object is {@link String}.
   */
  public String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStatus
  /**
   ** Sets the value of the status property.
   **
   ** @param  value              the value of the status property.
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
   **                            Allowed object is {@link String}.
   */
  public String getStatus() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSnmp
  /**
   ** Sets the value of the snmp property.
   **
   ** @param  value              the value of the snmp property.
   **                            Allowed object is {@link String}.
   */
  public void setSnmp(final String value) {
    this.snmp = "1".equals(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSnmp
  /**
   ** Sets the value of the snmp property.
   **
   ** @param  value              the value of the snmp property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setSnmp(final Boolean value) {
    this.snmp = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSnmp
  /**
   ** Returns the value of the snmp property.
   **
   ** @return                    the value of the snmp property.
   **                            Allowed object is {@link Boolean}.
   */
  public Boolean getSnmp() {
    return this.snmp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDataLevel
  /**
   ** Sets the value of the dataLevel property.
   **
   ** @param  value              the value of the dataLevel property.
   **                            Allowed object is {@link String}.
   */
  public void setDataLevel(final String value) {
    this.dataLevel = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDataLevel
  /**
   ** Returns the value of the dataLevel property.
   **
   ** @return                    the value of the dataLevel property.
   **                            Allowed object is {@link String}.
   */
  public String getDataLevel() {
    return this.dataLevel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  private void transfer() {
    // set all attributes required.
    setId(Long.valueOf(this.instance.getId()));
    setName(this.instance.getTemplatename());
    setEvent(this.instance.getEventname());
    setDescription(this.instance.getDescription());
    setStatus(this.instance.getStatus());
    setSnmp(this.instance.getSnmpsupported() == null ? Boolean.FALSE : Boolean.TRUE);
    setDataLevel(this.instance.getDatalevel().toString());
  }
}