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

    File        :   EndpointAdministratorAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointAdministratorAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class EndpointAdministratorAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>IT Resource</code> instance customization.
 ** <p>
 ** Implementing the data transfer functionalities to retrieve and manage
 ** <code>IT Resource</code> administrators.
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
public class EndpointAdministratorAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String SVR_PK           = "endpointKey";
  public static final String SVR_FK           = "Bind_" + SVR_PK;
  public static final String UGP_PK           = "groupKey";
  public static final String UGP_FK           = "Bind_" + UGP_PK;
  public static final String NAME             = "groupName";

  public static final String ACTION           = "pendingAction";

  public static final String ADD              = "add";
  public static final String DEL              = "del";
  public static final String MOD              = "mod";
  public static final String NIL              = "nil";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5452168954282927035")
  private static final long  serialVersionUID = -6888858618358877531L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               endpointKey;
  @ModelAttr
  private Long               groupKey;
  @ModelAttr
  private String             groupName;
  @ModelAttr
  private boolean            readAccess;
  @ModelAttr
  private boolean            writeAccess;
  @ModelAttr
  private boolean            deleteAccess;
  @ModelAttr
  private String             pendingAction;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointAdministratorAdapter</code> values object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EndpointAdministratorAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointAdministratorAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public EndpointAdministratorAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointAdministratorAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
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
  public EndpointAdministratorAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>EndpointAdministratorAdapter</code> values object that
   ** belongs to the specified {@link EndpointAdapter} identifier.
   **
   ** @param  endpointKey        the {@link EndpointAdapter} identifier the
   **                            parameter to build belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  private EndpointAdministratorAdapter(final Long endpointKey) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.endpointKey = endpointKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointKey
  /**
   ** Sets the value of the endpointKey property.
   **
   ** @param  value              the value of the endpointKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setEndpointKey(final Long value) {
    this.endpointKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointKey
  /**
   ** Returns the value of the endpointKey property.
   **
   ** @return                    the value of the endpointKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getEndpointKey() {
    return this.endpointKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setGroupKey
  /**
   ** Sets the value of the groupKey property.
   **
   ** @param  value              the value of the groupKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setGroupKey(final Long value) {
    this.groupKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getGroupKey
  /**
   ** Returns the value of the groupKey property.
   **
   ** @return                    the value of the groupKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getGroupKey() {
    return this.groupKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setGroupName
  /**
   ** Sets the value of the groupName property.
   **
   ** @param  value              the value of the groupName property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setGroupName(final String value) {
    this.groupName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getGroupName
  /**
   ** Returns the value of the groupName property.
   **
   ** @return                    the value of the groupName property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getGroupName() {
    return this.groupName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setReadAccess
  /**
   ** Sets the value of the readAccess property.
   **
   ** @param  value              the value of the readAccess property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setReadAccess(final Boolean value) {
    this.readAccess = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getReadAccess
  /**
   ** Returns the value of the readAccess property.
   **
   ** @return                    the value of the readAccess property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getReadAccess() {
    return this.readAccess;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setWriteAccess
  /**
   ** Sets the value of the writeAccess property.
   **
   ** @param  value              the value of the writeAccess property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setWriteAccess(final Boolean value) {
    this.writeAccess = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getWriteAccess
  /**
   ** Returns the value of the writeAccess property.
   **
   ** @return                    the value of the writeAccess property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getWriteAccess() {
    return this.writeAccess;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDeleteAccess
  /**
   ** Sets the value of the deleteAccess property.
   **
   ** @param  value              the value of the deleteAccess property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setDeleteAccess(final Boolean value) {
    this.deleteAccess = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDeleteAccess
  /**
   ** Returns the value of the deleteAccess property.
   **
   ** @return                    the value of the deleteAccess property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getDeleteAccess() {
    return this.deleteAccess;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPendingAction
  /**
   ** Sets the value of the pendingAction property.
   **
   ** @param  value              the value of the pendingAction property.
   **                            <br>
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
   **                            <br>
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
   ** Factory method to create a <code>EndpointParameterAdapter</code> with
   ** an identifier.
   **
   ** @param  endpointKey        the {@link EndpointTypeAdapter} identifier the
   **                            administrator to build belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the model adapter bean populated from the
   **                            specfied <code>resultSet</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link EndpointParameterAdapter}.
   */
  public static EndpointAdministratorAdapter build(final Long endpointKey) {
    return new EndpointAdministratorAdapter(endpointKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (this.endpointKey != null ? this.endpointKey.hashCode() : 0);
    result = 31 * result + (this.groupKey    != null ? this.groupKey.hashCode()    : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>EndpointAdministratorAdapter</code>s are considered equal if and
   ** only if they represent the same scope. As a consequence, two given
   ** <code>EndpointAdministratorAdapter</code>s may be different even though
   ** they contain the same set of names with the same values, but in a different
   ** order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final EndpointAdministratorAdapter that = (EndpointAdministratorAdapter)other;
    if (this.endpointKey != null ? !this.endpointKey.equals(that.endpointKey) : that.endpointKey != null)
      return false;

    return !(this.groupKey != null ? !this.groupKey.equals(that.groupKey) : that.groupKey != null);
  }
}