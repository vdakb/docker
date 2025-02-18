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
    Subsystem   :   System Authorization Management

    File        :   EntityPublicationAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntityPublicationAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class EntityPublicationAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Entity Publication</code> customization.
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
public class EntityPublicationAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String KEY              = "publicationId";
  public static final String ACTION           = "pendingAction";
  public static final String ENTITY_ID        = "entityId";
  public static final String ENTITY_TYPE      = "entityName";
  public static final String SCOPE_ID         = "scopeId";
  public static final String SCOPE_NAME       = "scopeName";
  public static final String SCOPE_TYPE       = "scopeType";
  public static final String SCOPE_STATUS     = "scopeStatus";
  public static final String HIERARCHY        = "hierarchicalScope";

  public static final String NIL              = "nil";
  public static final String ADD              = "add";
  public static final String MOD              = "mod";
  public static final String DEL              = "del";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3550084026416238187")
  private static final long  serialVersionUID = -6519666533849980771L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private long               publicationId;
  @ModelAttr
  private long               entityId;
  @ModelAttr
  private String             entityType;
  @ModelAttr
  private String             scopeId;
  @ModelAttr
  private String             scopeName;
  @ModelAttr
  private String             scopeType;
  @ModelAttr
  private String             scopeStatus;
  @ModelAttr
  private Boolean            hierarchicalScope;
  @ModelAttr
  private String             pendingAction;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccessPolicyActionAdapterBean</code> value
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntityPublicationAdapter() {
    // ensure inheritance
    super();

    // initialize instance
    this.pendingAction = NIL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntityPublicationAdapter</code> value object which use
   ** the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public EntityPublicationAdapter(final Row row) {
    // ensure inheritance
    super(row);

    // initialize instance
    this.pendingAction = (String)row.getAttribute(ACTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntityPublicationAdapter</code> value object which use
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
  public EntityPublicationAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);

    // initialize instance
    this.pendingAction = (String)row.getAttribute(ACTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPublicationId
  /**
   ** Sets the value of the publicationId property.
   **
   ** @param  value              the value of the publicationId property.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   */
  public void setPublicationId(final long value) {
    this.publicationId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPublicationId
  /**
   ** Returns the value of the publicationId property.
   **
   ** @return                    the value of the publicationId property.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long getPublicationId() {
    return this.publicationId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityId
  /**
   ** Sets the value of the entityId property.
   **
   ** @param  value              the value of the entityId property.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   */
  public void setEntityId (final long value) {
    this.entityId  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityId
  /**
   ** Returns the value of the entityId property.
   **
   ** @return                    the value of the entityId property.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long getEntityId () {
    return this.entityId ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityType
  /**
   ** Sets the value of the entityType property.
   **
   ** @param  value              the value of the entityType property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setEntityType(final String value) {
    this.entityType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityType
  /**
   ** Returns the value of the entityType property.
   **
   ** @return                    the value of the entityType property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getEntityType() {
    return this.entityType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setScopeId
  /**
   ** Sets the value of the scopeId property.
   **
   ** @param  value              the value of the scopeId property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setScopeId (final String value) {
    this.scopeId  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getScopeId
  /**
   ** Returns the value of the scopeId property.
   **
   ** @return                    the value of the scopeId property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getScopeId () {
    return this.scopeId ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setScopeName
  /**
   ** Sets the value of the scopeName property.
   **
   ** @param  value              the value of the scopeName property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setScopeName (final String value) {
    this.scopeName  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getScopeName
  /**
   ** Returns the value of the scopeName property.
   **
   ** @return                    the value of the scopeName property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getScopeName () {
    return this.scopeName ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setScopeType
  /**
   ** Sets the value of the scopeType property.
   **
   ** @param  value              the value of the scopeType property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setScopeType (final String value) {
    this.scopeType  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getScopeType
  /**
   ** Returns the value of the scopeType property.
   **
   ** @return                    the value of the scopeType property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getScopeType () {
    return this.scopeType ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setScopeStatus
  /**
   ** Sets the value of the scopeStatus property.
   **
   ** @param  value              the value of the scopeStatus property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setScopeStatus (final String value) {
    this.scopeStatus  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getScopeStatus
  /**
   ** Returns the value of the scopeStatus property.
   **
   ** @return                    the value of the scopeStatus property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getScopeStatus () {
    return this.scopeStatus ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setHierarchicalScope
  /**
   ** Sets the value of the hierarchicalScope property.
   **
   ** @param  value              the value of the hierarchicalScope property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setHierarchicalScope (final Boolean value) {
    this.hierarchicalScope  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getHierarchicalScope
  /**
   ** Returns the value of the hierarchicalScope property.
   **
   ** @return                    the value of the hierarchicalScope property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getHierarchicalScope () {
    return this.hierarchicalScope ;
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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    return this.scopeId != null ? this.scopeId.hashCode() : 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>EntityPublicationAdapter</code>s are considered equal if and
   ** only if they represent the same scope. As a consequence, two given
   ** <code>EntityPublicationAdapter</code>s may be different even though they
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

    final EntityPublicationAdapter that = (EntityPublicationAdapter)other;
    return !(this.scopeId != null ? !this.scopeId.equals(that.scopeId) : that.scopeId != null);
  }
}