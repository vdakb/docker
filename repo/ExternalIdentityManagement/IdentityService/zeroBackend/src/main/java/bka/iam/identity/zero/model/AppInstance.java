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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
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
public final class AppInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String RESOURCE_NAME = "AppInstance";
  
  // Names of the JSON attributes
  public static final String ATTR_KEY              = "key";
  public static final String ATTR_NAME             = "name";
  public static final String ATTR_DISPLAY_NAME     = "displayName";
  public static final String ATTR_DESCRIPTION      = "description";
  public static final String ATTR_IT_RESOURCE_NAME = "itResourceName";
  public static final String ATTR_CREATE_DATE      = "createDate";
  public static final String ATTR_UPDATE_DATE      = "updateDate";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(ATTR_KEY)
  private Integer key;

  @JsonProperty(ATTR_NAME)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String name;

  @JsonProperty(ATTR_DISPLAY_NAME)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String displayName;

  @JsonProperty(ATTR_DESCRIPTION)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String description;

  @JsonProperty(ATTR_IT_RESOURCE_NAME)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String itResourceName;

  @JsonProperty(ATTR_CREATE_DATE)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonSerialize(using = CustomDateSerializer.class)
  private Date createDate;

  @JsonProperty(ATTR_UPDATE_DATE)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonSerialize(using = CustomDateSerializer.class)
  private Date updateDate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AppInstance</code> object that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AppInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Sets the key value of an application instance.
   **
   ** @param  value              the key of an application instance.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the {@link AppInstance} instance to allow method
   **                            chaining.
   */
  public AppInstance key(final Integer value) {
    this.key = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the key value of an application instance.
   **
   ** @return                    the key of an application instance.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer key() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of an application instance.
   **
   ** @param  value              the name of an application instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AppInstance</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AppInstance</code>.
   */
  public AppInstance name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of an application instance.
   **
   ** @return                    the name of an application instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the display name of an application instance.
   **
   ** @param  value              the display name of an application instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AppInstance</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AppInstance</code>.
   */
  public AppInstance displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the display name of an application instance.
   **
   ** @return                    the display name of an application instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String displayName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the description of an application instance.
   **
   ** @param  value              the description of an application instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AppInstance</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AppInstance</code>.
   */
  public AppInstance description(final String value) {
    this.description = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of an application instance.
   **
   ** @return                    the description of an application instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String description() {
    return this.description;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   itResourceName
  /**
   ** Sets the name of associated IT resource.
   **
   ** @param  value              the name of associated IT resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AppInstance</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AppInstance</code>.
   */
  public AppInstance itResourceName(final String value) {
    this.itResourceName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the name of associated IT resource.
   **
   ** @return                    the name of associated IT resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String itResourceName() {
    return this.itResourceName;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDate
  /**
   ** Sets the create date of an application instance.
   **
   ** @param  value              the create date of an application instance.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the <code>AppInstance</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AppInstance</code>.
   */
  public AppInstance createDate(final Date value) {
    this.createDate = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDate
  /**
   ** Returns the create date of an application instance.
   **
   ** @return                    the create date of an application instance.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public Date createDate() {
    return this.createDate;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateDate
  /**
   ** Sets the update date of an application instance.
   **
   ** @param  value              the update date of an application instance.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the <code>AppInstance</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AppInstance</code>.
   */
  public AppInstance updateDate(final Date value) {
    this.updateDate = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateDate
  /**
   ** Returns the update date of an application instance.
   **
   ** @return                    the update date of an application instance.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public Date updateDate() {
    return this.updateDate;
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
    StringBuffer result = new StringBuffer("AppInstance object:");
    result.append("\" key=\"").append(this.key);
    result.append("\" name=\"").append(this.name);
    result.append("\" displayName=\"").append(this.displayName);
    result.append("\" description=\"").append(this.description);
    result.append("\" createDate=\"").append(this.createDate);
    result.append("\" updateDate=\"").append(this.updateDate);
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
                        this.name,
                        this.displayName,
                        this.description,
                        this.createDate,
                        this.updateDate);
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

    final AppInstance that = (AppInstance)other;
    return Objects.equals(this.key,         that.key)
        && Objects.equals(this.name,        that.name)
        && Objects.equals(this.description, that.description)
        && Objects.equals(this.displayName, that.displayName)
        && Objects.equals(this.createDate,  that.createDate)
        && Objects.equals(this.updateDate,  that.updateDate)
      ;
  }
}