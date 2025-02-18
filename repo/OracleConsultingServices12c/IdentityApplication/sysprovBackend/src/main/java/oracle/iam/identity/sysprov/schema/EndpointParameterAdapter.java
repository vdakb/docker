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

    File        :   EndpointParameterAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointParameterAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class EndpointParameterAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>IT Resource</code> instance customization.
 ** <p>
 ** Implementing the data transfer functionalities to retrieve and manage
 ** <code>IT Resource</code> parameters.
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
public class EndpointParameterAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String SVD_PK           = "parameterKey";
  public static final String SVR_PK           = "endpointKey";
  public static final String SVR_FK           = "Bind_" + SVR_PK;
  public static final String NAME             = "name";
  public static final String VALUE            = "value";

  public static final String ACTION           = "pendingAction";

  public static final String MOD              = "mod";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1656597034820645698")
  private static final long  serialVersionUID = -7380333644486177592L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               parameterKey;
  @ModelAttr
  private Long               endpointKey;
  @ModelAttr
  private String             name;
  @ModelAttr
  private String             value;
  @ModelAttr
  private Boolean            encrypted;
  @ModelAttr
  private String             pendingAction;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointParameterAdapter</code> values object which use
   ** the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public EndpointParameterAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointParameterAdapter</code> values object which use
   ** the specified {@link Row} to populate its value.
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
  public EndpointParameterAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>EndpointParameterAdapter</code> values object that
   ** belongs to the specified {@link EndpointAdapter} identifier.
   **
   ** @param  endpointKey        the {@link EndpointAdapter} identifier the
   **                            parameter to build belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  private EndpointParameterAdapter(final Long endpointKey) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.endpointKey = endpointKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setParameterKey
  /**
   ** Sets the value of the parameterKey property.
   **
   ** @param  value              the value of the parameterKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setParameterKey(final Long value) {
    this.parameterKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getParameterKey
  /**
   ** Returns the value of the parameterKey property.
   **
   ** @return                    the value of the parameterKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getParameterKey() {
    return this.parameterKey;
  }

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
  // Methode:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setValue
  /**
   ** Sets the value of the value property.
   **
   ** @param  value              the value of the value property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setValue(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getValue
  /**
   ** Returns the value of the value property.
   **
   ** @return                    the value of the value property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEncrypted
  /**
   ** Sets the value of the encrypted property.
   **
   ** @param  encrypted          the value of the encrypted property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setEncrypted(final Boolean encrypted) {
    this.encrypted = encrypted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEncrypted
  /**
   ** Returns the value of the encrypted property.
   **
   ** @return                    the value of the encrypted property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getEncrypted() {
    return this.encrypted;
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
   **                            parameter to build belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the model adapter bean populated from the
   **                            specfied <code>resultSet</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link EndpointParameterAdapter}.
   */
  public static EndpointParameterAdapter build(final Long endpointKey) {
    return new EndpointParameterAdapter(endpointKey);
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
    result = 31 * result + (this.parameterKey != null ? this.parameterKey.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>EndpointParameterAdapter</code>s are considered equal if and
   ** only if they represent the same scope. As a consequence, two given
   ** <code>EndpointParameterAdapter</code>s may be different even though they
   ** contain the same set of names with the same values, but in a different
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

    final EndpointParameterAdapter that = (EndpointParameterAdapter)other;
    return !(this.parameterKey != null ? !this.parameterKey.equals(that.parameterKey) : that.parameterKey != null);
  }
}