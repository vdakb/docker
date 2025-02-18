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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Analytics Extension Library
    Subsystem   :   Offline Target Connector

    File        :   Role.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Role.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.domain.role;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import javax.xml.datatype.XMLGregorianCalendar;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

import oracle.hst.foundation.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// class Role
// ~~~~~ ~~~~
/**
 ** Java class for role complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="role"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://service.api.oia.iam.ots/role}entity"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="customProperty" type="{http://service.api.oia.iam.ots/base}token" maxOccurs="10"/&gt;
 **         &lt;element name="ownerShip"      type="{http://service.api.oia.iam.ots/base}token" maxOccurs="unbounded" minOccurs="0"/&gt;
 **         &lt;element name="businessUnit"   type="{http://service.api.oia.iam.ots/base}token" maxOccurs="unbounded" minOccurs="0"/&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="displayName"    type="{http://service.api.oia.iam.ots/base}token" use="required"/&gt;
 **       &lt;attribute name="description"    type="{http://service.api.oia.iam.ots/base}token" use="required"/&gt;
 **       &lt;attribute name="comment"        type="{http://service.api.oia.iam.ots/base}token"/&gt;
 **       &lt;attribute name="department"     type="{http://service.api.oia.iam.ots/base}token"/&gt;
 **       &lt;attribute name="jobCode"        type="{http://service.api.oia.iam.ots/base}token"/&gt;
 **       &lt;attribute name="type" default="Provisioning Role"&gt;
 **         &lt;simpleType&gt;
 **           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 **             &lt;enumeration value="Provisioning Role"/&gt;
 **             &lt;enumeration value="Access Control Role"/&gt;
 **             &lt;enumeration value="Organizational Role"/&gt;
 **           &lt;/restriction&gt;
 **         &lt;/simpleType&gt;
 **       &lt;/attribute&gt;
 **       &lt;attribute name="risk" default="low"&gt;
 **         &lt;simpleType&gt;
 **           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 **             &lt;enumeration value="low"/&gt;
 **             &lt;enumeration value="medium"/&gt;
 **             &lt;enumeration value="high"/&gt;
 **           &lt;/restriction&gt;
 **         &lt;/simpleType&gt;
 **       &lt;/attribute&gt;
 **       &lt;attribute name="startDate"   type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 **       &lt;attribute name="endDate"     type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlSeeAlso({Policy.class})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name=Role.LOCAL, propOrder={"customProperty", "ownerShip", "businessUnit"})
public class Role extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String     LOCAL               = "role";
  public final static QName      PORT                = new QName(ObjectFactory.NAMESPACE, LOCAL);

  public static final String     TYPE_PROVISIONING   = "Provisioning Role";
  public static final String     TYPE_ACCESS_CONTROL = "Access Control Role";
  public static final String     TYPE_ORGANIZATIONAL = "Organizational";

  public static final String     RISK_LEVEL_LOW      = "low";
  public static final String     RISK_LEVEL_MEDIUM   = "medium";
  public static final String     RISK_LEVEL_HIGH     = "high";

  public static final String     BUSINESSUNIT_SYSTEM = "$$RbacxSystem$$";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute(required=true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String               displayName;
  @XmlAttribute(required=true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String               description;
  @XmlAttribute
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String               comment;
  @XmlAttribute
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String               department;
  @XmlAttribute
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String               jobCode;
  @XmlAttribute
  protected String               type      = Type.PROVISIONING.value();
  @XmlAttribute
  protected String               risk      = Risk.LOW.value();
  @XmlAttribute
  @XmlSchemaType(name="dateTime")
  protected XMLGregorianCalendar startDate;
  @XmlAttribute
  @XmlSchemaType(name="dateTime")
  protected XMLGregorianCalendar endDate;
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlElement(required=true, name="customProperty", namespace=ObjectFactory.NAMESPACE, nillable=false)
  protected List<String>         customProperty;
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlElement(required=false, name="ownerShip", namespace=ObjectFactory.NAMESPACE, nillable=true)
  protected List<String>         ownerShip;
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlElement(required=false, name="businessUnit", namespace=ObjectFactory.NAMESPACE, nillable=true)
  protected List<String>         businessUnit;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  public enum Type {
      PROVISIONING(TYPE_PROVISIONING,     new Long(1L))
    , ACCESSCONTROL(TYPE_ACCESS_CONTROL,  new Long(2L))
    , ORGANIZATIONAL(TYPE_ORGANIZATIONAL, new Long(3L))
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-11955890473978460")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Long   encoded;
    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code>
     **
     ** @param  value            the string value.
     ** @param  encoded          the encoded value.
     */
    Type(final String value, final Long encoded) {
      this.value   = value;
      this.encoded = encoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: encoded
    /**
     ** Returns the value of the encoded property.
     **
     ** @return                    possible object is {@link Long}.
     */
    public Long encoded() {
      return this.encoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper type from the given string value.
     **
     ** @param  value              the string value the type should be returned
     **                            for.
     **
     ** @return                    the risk.
     */
    public static Type from(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper type from the given Integer value.
     **
     ** @param  value              the Integer value the type should be returned
     **                            for.
     **
     ** @return                    the risk.
     */
    public static Type from(final Long value) {
      for (Type cursor : Type.values()) {
        if (cursor.encoded.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(String.valueOf(value));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Risk
  // ~~~~ ~~~~
  public enum Risk {

      HIGH(RISK_LEVEL_HIGH,     new Integer(1))
    , MEDIUM(RISK_LEVEL_MEDIUM, new Integer(2))
    , LOW(RISK_LEVEL_LOW,       new Integer(3))
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:2779666324613049387")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Integer encoded;
    private final String  value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Risk</code>
     **
     ** @param  value              the string value.
     ** @param  encoded            the encoded value.
     */
    Risk(final String value, final Integer encoded) {
      this.value   = value;
      this.encoded = encoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: encoded
    /**
     ** Returns the value of the encoded property.
     **
     ** @return                    possible object is {@link Integer}.
     */
    public Integer encoded() {
      return this.encoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper risk from the given string value.
     **
     ** @param  value              the string value the risk should be returned
     **                            for.
     **
     ** @return                    the risk.
     */
    public static Risk from(final String value) {
      for (Risk cursor : Risk.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper risk from the given Integer value.
     **
     ** @param  value              the Integer value the risk should be returned
     **                            for.
     **
     ** @return                    the risk.
     */
    public static Risk from(final Integer value) {
      for (Risk cursor : Risk.values()) {
        if (cursor.encoded.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(String.valueOf(value));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Role</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Role() {
	// ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Role</code> with the specified id.
   **
   ** @param  id                 the id of this <code>Entity</code>.
   */
  public Role(final String id) {
    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayName
  /**
   ** Sets the value of the displayname property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public void setDisplayName(final String value) {
    this.displayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayName
  /**
   ** Returns the value of the displayname property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String getDisplayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the value of the description property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setComment
  /**
   ** Sets the value of the comment property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public void setComment(final String value) {
    this.comment = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getComment
  /**
   ** Returns the value of the comment property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String getComment() {
    return this.comment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDepartment
  /**
   ** Sets the value of the department property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public void setDepartment(final String value) {
    this.department = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDepartment
  /**
   ** Returns the value of the department property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String getDepartment() {
    return this.department;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setJobCode
  /**
   ** Sets the value of the jobcode property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public void setJobCode(final String value) {
    this.jobCode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getJobCode
  /**
   ** Returns the value of the jobcode property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String getJobCode() {
    return this.jobCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Sets the value of the type property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public void setType(final String value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType
  /**
   ** Returns the value of the type property.
   **
   ** @return                    possible object is {@link Type}.
   */
  public String getType() {
    return (this.type == null) ? TYPE_PROVISIONING : this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRisk
  /**
   ** Sets the value of the type property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public void setRisk(final String value) {
    this.risk = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRisk
  /**
   ** Returns the value of the risk property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String getRisk() {
    return (this.risk == null) ? RISK_LEVEL_LOW : this.risk;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setStartDate
  /**
   ** Sets the value of the startdate property.
   **
   ** @param  value              possible object is {@link Date}.
   */
  public void setStartDate(final Date value) {
    this.setStartDate(DateUtility.toXMLGregorianCalendar(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setStartDate
  /**
   ** Sets the value of the startdate property.
   **
   ** @param  value              possible object is {@link XMLGregorianCalendar}.
   */
  public void setStartDate(final XMLGregorianCalendar value) {
    this.startDate = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStartDate
  /**
   ** Returns the value of the startdate property.
   **
   ** @return                    possible object is {@link XMLGregorianCalendar}.
   */
  public XMLGregorianCalendar getStartDate() {
    return this.startDate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEndDate
  /**
   ** Sets the value of the enddate property.
   **
   ** @param  value              possible object is {@link Date}.
   */
  public void setEndDate(final Date value) {
    this.setEndDate(DateUtility.toXMLGregorianCalendar(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEndDate
  /**
   ** Sets the value of the enddate property.
   **
   ** @param  value              possible object is {@link XMLGregorianCalendar}.
   */
  public void setEndDate(final XMLGregorianCalendar value) {
    this.endDate = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEndDate
  /**
   ** Returns the value of the enddate property.
   **
   ** @return                    possible object is {@link XMLGregorianCalendar}.
   */
  public XMLGregorianCalendar getEndDate() {
    return this.endDate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCustomProperty
  /**
   ** Returns the value of the custom property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <CODE>set</CODE> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getProperty().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the list {@link String}.
   **
   ** @return                    the associated {@link List} of {@link String}.
   **                            Returned value is never <code>null</code>.
   */
  public List<String> getCustomProperty() {
    if (this.customProperty == null)
      this.customProperty = new ArrayList<String>();

    return this.customProperty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOwnerShip
  /**
   ** Returns the value of the owner property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <CODE>set</CODE> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getOwner().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the list {@link String}.
   **
   ** @return                    the associated {@link List} of {@link String}.
   **                            Returned value is never <code>null</code>.
   */
  public List<String> getOwnerShip() {
    if (this.ownerShip == null)
      this.ownerShip = new ArrayList<String>();

    return this.ownerShip;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessUnit
  /**
   ** Returns the value of the whitelist property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <CODE>set</CODE> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getWhitelist().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the list {@link String}.
   **
   ** @return                    the associated {@link List} of {@link String}.
   **                            Returned value is never <code>null</code>.
   */
  public List<String> getBusinessUnit() {
    if (this.businessUnit == null)
      this.businessUnit = new ArrayList<String>();

    return this.businessUnit;
  }
}