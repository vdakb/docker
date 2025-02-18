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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Service Library
    Subsystem   :   ZeRo Backend

    File        :   OIMEntitlement.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    OIMEntitlement.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;
import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// final class OIMEntitlement
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Stores OIM Entitlement object.
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class OIMEntitlement {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String RESOURCE_NAME = "OIMEntitlement";
  
  // Names of the JSON attributes
  private final String JSON_ATTR_KEY = "key";
  private final String JSON_ATTR_CODE = "code";
  private final String JSON_ATTR_NAME = "name";
  private final String JSON_ATTR_ACTIVE = "active";
  private final String JSON_ATTR_DISPLAY_NAME = "displayName";
  private final String JSON_ATTR_DESCRIPTION = "description";
  private final String JSON_ATTR_CREATE_DATE = "createDate";
  private final String JSON_ATTR_UPDATE_DATE = "updateDate";
  private final String JSON_ATTR_IT_RESOURCE_NAME = "itResourceName";
  private final String JSON_ATTR_APP_INSTANCE_NAME = "appInstanceName";
  private final String JSON_ATTR_APP_INSTANCE_DISPLAY_NAME = "appInstanceDisplayName";
  private final String JSON_ATTR_NAMESPACE = "namespace";
  private final String JSON_ATTR_FIELD_NAME = "fieldName";
  private final String JSON_ATTR_FIELD_LABEL = "fieldLabel";
  private final String JSON_ATTR_ASSIGNEES = "assignees";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(JSON_ATTR_KEY)
  private String key;

  @JsonProperty(JSON_ATTR_CODE)
  private String code;

  @JsonProperty(JSON_ATTR_NAME)
  private String name;

  @JsonProperty(JSON_ATTR_ACTIVE)
  private Boolean active;

  @JsonProperty(JSON_ATTR_DISPLAY_NAME)
  private String displayName;

  @JsonProperty(JSON_ATTR_DESCRIPTION)
  private String description;

  @JsonProperty(JSON_ATTR_CREATE_DATE)
  //  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm:ss", timezone="CET")
  @JsonSerialize(using = CustomDateSerializer.class)
  private Date createDate;

  @JsonProperty(JSON_ATTR_UPDATE_DATE)
//  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm:ss", timezone="CET")
  @JsonSerialize(using = CustomDateSerializer.class)
  private Date updateDate;

  @JsonProperty(JSON_ATTR_IT_RESOURCE_NAME)
  private String itResourceName;

  @JsonProperty(JSON_ATTR_APP_INSTANCE_NAME)
  private String appInstanceName;

  @JsonProperty(JSON_ATTR_APP_INSTANCE_DISPLAY_NAME)
  private String appInstanceDisplayName;

  @JsonProperty(JSON_ATTR_NAMESPACE)
  private String namespace;

  @JsonProperty(JSON_ATTR_FIELD_NAME)
  private String fieldName;

  @JsonProperty(JSON_ATTR_FIELD_LABEL)
  private String fieldLabel;

  @JsonProperty(JSON_ATTR_ASSIGNEES)
  private List<EntitlementAssignee> assignees;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OIMEntitlement</code> Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OIMEntitlement() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Sets the key of the entitlement.
   **
   ** @param  value              the 'key' of the entitlement.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement key(final String value) {
    this.key = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the key of the entitlement.
   **
   ** @return                    the key of the entitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String key() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Sets the code of the entitlement.
   **
   ** @param  value              the 'code' of the entitlement.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement code(final String value) {
    this.code = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Returns the code of the entitlement.
   **
   ** @return                    the code of the entitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String code() {
    return this.code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of an entitlement.
   **
   ** @param  value              the name of an entitlement.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of an entitlement.
   **
   ** @return                    the name of an entitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.name;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Sets the name of an entitlement.
   **
   ** @param  value              the status of an entitlement.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement active(final Boolean value) {
    this.active = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the name of an entitlement.
   **
   ** @return                    the status of an entitlement.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean active() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the display name, primarily used for display purposes.
   **
   ** @param  value              the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the display name, primarily used for display purposes.
   **
   ** @return                    the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String displayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the description, primarily used for display purposes.
   **
   ** @param  value              the description, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement description(final String value) {
    this.description = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description, primarily used for display purposes.
   **
   ** @return                    the description, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDate
  /**
   ** Sets the create date of the entitlement.
   **
   ** @param  value              the create date of the entitlement.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement createDate(final Date value) {
    this.createDate = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDate
  /**
   ** Returns the create date of the entitlement.
   **
   ** @return                    the create date of the entitlement.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public Date createDate() {
    return this.createDate;
  }
  public Date getCreateDate() {
    return this.createDate;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateDate
  /**
   ** Sets the update date of the entitlement.
   **
   ** @param  value              the update date of the entitlement.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement updateDate(final Date value) {
    this.updateDate = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateDate
  /**
   ** Returns the update date of the entitlement.
   **
   ** @return                    the update date of the entitlement.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public Date updateDate() {
    return this.updateDate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   itResourceName
  /**
   ** Sets the associated IT Resource name.
   **
   ** @param  value              the IT Resource name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement itResourceName(final String value) {
    this.itResourceName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   itResourceName
  /**
   ** Returns the name of the associated IT Resource.
   **
   ** @return                    the name of the associated IT Resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String itResourceName() {
    return this.itResourceName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appInstanceName
  /**
   ** Sets the associated Application Instance name.
   **
   ** @param  value              the Application Instance name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement appInstanceName(final String value) {
    this.appInstanceName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appInstanceName
  /**
   ** Returns the name of the associated Application Instance.
   **
   ** @return                    the name of the associated Application Instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String appInstanceName() {
    return this.appInstanceName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appInstanceDisplayName
  /**
   ** Sets the associated Application Instance display name.
   **
   ** @param  value              the Application Instance display name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public OIMEntitlement appInstanceDisplayName(final String value) {
    this.appInstanceDisplayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appInstanceDisplayName
  /**
   ** Returns the display name of the associated Application Instance.
   **
   ** @return                    the display name of the associated Application
   **                            Instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String appInstanceDisplayName() {
    return this.appInstanceDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Sets the namespace for this OIMEntitlement.
   **
   ** @param  value              the Application Instance display name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>OIMEntitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>OIMEntitlement</code>.
   */
  public OIMEntitlement namespace(final String value) {
    this.namespace = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the namespace of the entitlement.
   **
   ** @return                    the namespace of the OIMEntitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String namespace() {
    return this.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldName
  /**
   ** Sets the name of the corresponding field for this OIMEntitlement.
   **
   ** @param  value              the associated field name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>OIMEntitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>OIMEntitlement</code>.
   */
  public OIMEntitlement fieldName(final String value) {
    this.fieldName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldName
  /**
   ** Returns the corresponding field name of the entitlement.
   **
   ** @return                    the associated field name for this OIMEntitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String fieldName() {
    return this.fieldName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldLabel
  /**
   ** Sets the label of the corresponding field for this OIMEntitlement.
   **
   ** @param  value              the associated field label.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>OIMEntitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>OIMEntitlement</code>.
   */
  public OIMEntitlement fieldLabel(final String value) {
    this.fieldLabel = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldLabel
  /**
   ** Returns the corresponding field lanel of the entitlement.
   **
   ** @return                    the associated field label for the OIMEntitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String fieldLabel() {
    return this.fieldLabel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignees
  /**
   ** Sets the assignees for this OIMEntitlement.
   **
   ** @param  value              the Application Instance display name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>OIMEntitlement</code>.
   */
  public OIMEntitlement assignees(final List<EntitlementAssignee> value) {
    this.assignees = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignees
  /**
   ** Returns the list of entitlement assignees.
   **
   ** @return                    the list of assignees for this OIMEntitlement.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link EntitlementAssignee}s.
   */
  public List<EntitlementAssignee> assignees() {
    return this.assignees;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this object.
   **
   ** @return                    a string representation of this object.
   **                            <br>
   **                            Possible object is <code>String</code>.
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer("OIMEntitlement object: ");
    result.append(" key=\"").append(this.key);
    result.append("\" code=\"").append(this.code);
    result.append("\" name=\"").append(this.name);
    result.append("\" displayName=\"").append(this.displayName);
    result.append("\" description=\"").append(this.description);
    result.append("\" createDate=\"").append(this.createDate);
    result.append("\" updateDate=\"").append(this.updateDate);
    result.append("\" itResourceName=\"").append(this.itResourceName);
    result.append("\" appInstanceName=\"").append(this.appInstanceName);
    result.append("\" appInstanceDisplayName=\"").append(this.appInstanceDisplayName);
    result.append("\" namespace=\"").append(this.namespace);
    result.append("\" fieldName=\"").append(this.fieldName);
    result.append("\" fieldLabel=\"").append(this.fieldLabel);
    result.append("\" assignees=\"").append(this.assignees);
    result.append("\"");
    return result.toString();
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
    return Objects.hash(this.key,
                        this.code,
                        this.name,
                        this.active,
                        this.itResourceName,
                        this.description,
                        this.createDate,
                        this.updateDate,
                        this.itResourceName,
                        this.appInstanceName,
                        this.appInstanceDisplayName,
                        this.assignees);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entitlement</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Entitlement</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
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

    final OIMEntitlement that = (OIMEntitlement)other;
    return Objects.equals(this.key,                    that.key)
        && Objects.equals(this.code,                   that.code)
        && Objects.equals(this.name,                   that.name)
        && Objects.equals(this.displayName,            that.displayName)
        && Objects.equals(this.description,            that.description)
        && Objects.equals(this.createDate,             that.createDate)
        && Objects.equals(this.updateDate,             that.updateDate)
        && Objects.equals(this.itResourceName,         that.itResourceName)
        && Objects.equals(this.appInstanceName,        that.appInstanceName)
        && Objects.equals(this.appInstanceDisplayName, that.appInstanceDisplayName)
        && Objects.equals(this.assignees             , that.assignees);
  }
}