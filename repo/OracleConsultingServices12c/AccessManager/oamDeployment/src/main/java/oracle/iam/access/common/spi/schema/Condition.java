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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Condition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Condition.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class Condition
// ~~~~~ ~~~~~~~~~
/**
 ** Java class for anonymous complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="name"               type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="conditionClassType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element ref="{}ip4RangeList" minOccurs="0"/&gt;
 **         &lt;element ref="{}temporal" minOccurs="0"/&gt;
 **         &lt;element ref="{}identity" minOccurs="0" maxOccurs="unbounded"/&gt;
 **         &lt;element ref="{}attributeCondition" minOccurs="0" maxOccurs="unbounded"/&gt;
 **       &lt;sequence&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name=Condition.LOCAL)
@XmlType(name="", propOrder={"name", "conditionClassType", "conditionCombiner", "ip4RangeList", "temporal", "identity", "attributeCondition"})
public class Condition {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "condition";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String                   name;
  @XmlElement(required=true)
  protected String                   conditionClassType;
  protected String                   conditionCombiner;
  protected Ip4RangeList             ip4RangeList;
  protected Temporal                 temporal;
  protected List<Identity>           identity;
  protected List<AttributeCondition> attributeCondition;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Condition</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Condition() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the value of the <code>name</code> property.
   **
   ** @param  value              the value of the <code>name</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the value of the <code>name</code> property.
   **
   ** @return                    the value of the <code>name</code> property.
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConditionClassType
  /**
   ** Sets the value of the <code>conditionClassType</code> property.
   **
   ** @param  value              the value of the
   **                            <code>conditionClassType</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setConditionClassType(final String value) {
    this.conditionClassType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConditionClassType
  /**
   ** Returns the value of the <code>conditionClassType</code> property.
   **
   ** @return                    the value of the
   **                            <code>conditionClassType</code> property.
   **                            Possible object is {@link String}.
   */
  public String getConditionClassType() {
    return this.conditionClassType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConditionCombiner
  /**
   ** Sets the value of the <code>conditionCombiner</code> property.
   **
   ** @param  value              the value of the <code>conditionCombiner</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setConditionCombiner(final String value) {
    this.conditionCombiner = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConditionCombiner
  /**
   ** Returns the value of the <code>conditionCombiner</code> property.
   **
   ** @return                    the value of the <code>conditionCombiner</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getConditionCombiner() {
    return this.conditionCombiner;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIp4RangeList
  /**
   ** Sets the value of the <code>ip4RangeList</code> property.
   **
   ** @param  value              the value of the <code>ip4RangeList</code>
   **                            property.
   **                            Allowed object is {@link Ip4RangeList}.
   */
  public void setIp4RangeList(final Ip4RangeList value) {
    this.ip4RangeList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIp4RangeList
  /**
   ** Returns the value of the <code>ip4RangeList</code> property.
   **
   ** @return                    the value of the <code>ip4RangeList</code>
   **                            property.
   **                            Possible object is {@link Ip4RangeList}.
   */
  public Ip4RangeList getIp4RangeList() {
    return this.ip4RangeList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTemporal
  /**
   ** Sets the value of the <code>temporal</code> property.
   **
   ** @param  value              the value of the <code>temporal</code>
   **                            property.
   **                            Allowed object is {@link Temporal}.
   */
  public void setTemporal(final Temporal value) {
    this.temporal = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTemporal
  /**
   ** Returns the value of the <code>temporal</code> property.
   **
   ** @return                    the value of the <code>temporal</code>
   **                            property.
   **                            Possible object is {@link Temporal}.
   */
  public Temporal getTemporal() {
    return this.temporal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIdentity
  /**
   ** Returns the value of the <code>identity</code> property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getIdentity().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the {@link List}:
   ** {@link Identity}s.
   **
   ** @return                    the associated {@link List} of
   **                            {@link Identity}s.
   **                            Returned value is never <code>null</code>.
   */
  public List<Identity> getIdentity() {
    if (this.identity == null)
      this.identity = new ArrayList<Identity>();

    return this.identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeCondition
  /**
   ** Returns the value of the <code>attributeCondition</code> property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getAttributeCondition().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the {@link List}:
   ** {@link AttributeCondition}s.
   **
   ** @return                    the associated {@link List} of
   **                            {@link AttributeCondition}s.
   **                            Returned value is never <code>null</code>.
   */
  public List<AttributeCondition> getAttributeCondition() {
    if (this.attributeCondition == null)
      this.attributeCondition = new ArrayList<AttributeCondition>();

    return this.attributeCondition;
  }
}