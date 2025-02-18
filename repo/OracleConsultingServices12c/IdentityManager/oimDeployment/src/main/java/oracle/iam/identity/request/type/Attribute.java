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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Attribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Attribute.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import java.util.List;
import java.util.ArrayList;

import java.math.BigInteger;

import org.apache.tools.ant.BuildException;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Attribute
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Attribute</code> is similar to the {@link AttributeReference} with a
 ** difference. <code>Attribute</code> element data values are available only in
 ** context of request and cannot take part in dataflow. All other properties
 ** available with {@link AttributeReference} are also available with
 ** <code>Attribute</code>, except attr-ref attribute.
 ** <p>
 ** The following schema fragment specifies the expected content contained within this class.
 ** <pre>
 ** &lt;complexType name="attribute"&gt;
 **   &lt;complexContent&gt;
 **    &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **      &lt;sequence&gt;
 **        &lt;element name="Attribute" type="{http://www.oracle.com/schema/oim/request}attribute" maxOccurs="unbounded" minOccurs="0"/&gt;
 **        &lt;element name="PrePopulationAdapter" type="{http://www.oracle.com/schema/oim/request}pre-pop-adapter" minOccurs="0"/&gt;
 **        &lt;element name="lookupValues" type="{http://www.oracle.com/schema/oim/request}lookup-values" maxOccurs="unbounded" minOccurs="0"/&gt;
 **      &lt;/sequence&gt;
 **      &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **      &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **      &lt;attribute name="widget" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **      &lt;attribute name="length" use="required" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 **      &lt;attribute name="hidden" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **      &lt;attribute name="masked" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **      &lt;attribute name="read-only" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **      &lt;attribute name="approver-only" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **      &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **      &lt;attribute name="available-in-bulk" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **      &lt;attribute name="entity-type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **      &lt;attribute name="lookup-code" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **    &lt;/restriction&gt;
 **  &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="attribute", propOrder={ "attribute", "prePopulationAdapter", "lookupValues"})
public class Attribute {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(name="Attribute")
  protected List<Attribute>    attribute;

  @XmlElement(name="PrePopulationAdapter")
  protected PrePopAdapter      prePopulationAdapter;

  protected List<LookupValues> lookupValues;

  @XmlAttribute(required=true)
  protected String             name;

  @XmlAttribute(required=true)
  protected String             type;

  @XmlAttribute(name="entity-type")
  protected String             entityType;

  @XmlAttribute(required=true)
  protected BigInteger         length;

  @XmlAttribute(name="available-in-bulk", required=true)
  protected boolean            availableInBulk;

  @XmlAttribute
  protected Boolean            required;

  @XmlAttribute(name="read-only")
  protected Boolean            readOnly;

  @XmlAttribute
  protected Boolean            masked;

  @XmlAttribute
  protected Boolean            hidden;

  @XmlAttribute(name="approver-only")
  protected Boolean            approverOnly;

  @XmlAttribute(required=true)
  protected String             widget;

  @XmlAttribute(name="lookup-code")
  protected String             lookupCode;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Attribute() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the name of the attribute.
   **
   ** @param  name               the name of the attribute.
   */
  public void setName(final String name) {
    this.name = name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the name of the attribute.
   **
   ** @return                    the name of the attribute.
   */
  public final String getName() {
    return this.name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Sets the type of the attribute.
   **
   ** @param  type               the type of the attribute.
   */
  public void setType(final String type) {
    this.type = type;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getType

  /**
   ** Returns the type of the attribute.
   **
   ** @return                    the type of the attribute.
   */
  public final String getType() {
    return this.type;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setEntityType
  /**
   ** Sets the entity type of the attribute.
   ** <p>
   ** The entity type represents the backend type of Identity Manager.
   **
   ** @param  entityType               the entity type of the attribute.
   */
  public void setEntityType(final String entityType) {
    this.entityType = entityType;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getEntityType
  /**
   ** Returns the entity type of the attribute.
   ** <p>
   ** The entity type represents the backend type of Identity Manager.
   **
   ** @return                    the entity type of the attribute.
   */
  public final String getEntityType() {
    return this.entityType;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setLength
  /**
   ** Sets the physical length of the attribute.
   **
   ** @param  length             the physical length of the attribute.
   */
  public void setLength(final BigInteger length) {
    this.length = length;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getLength
  /**
   ** Returns the physical length of the attribute.
   **
   ** @return                    the physical length of the attribute.
   */
  public final BigInteger getLength() {
    return this.length;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRequired
  /**
   ** Whether the attribute is mandatory in the request or not.
   **
   ** @param  required           <code>true</code> the attribute is mandatory in
   **                            the request; otherwise <code>false</code>.
   */
  public void setRequired(final Boolean required) {
    this.required = required;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   isRequired
  /**
   ** Returns <code>true</code> the attribute is mandatory in the request.
   **
   ** @return                    <code>true</code> the attribute is mandatory in
   **                            the request; otherwise <code>false</code>.
   */
  public final Boolean isRequired() {
    return this.required;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setMasked
  /**
   ** Whether the attribute has to be masked in the UI of the request or not.
   **
   ** @param  masked             the attribute has to be masked in the UI of
   **                            the request or not.
   */
  public void setMasked(final Boolean masked) {
    this.masked = masked;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   isMasked
  /**
   ** Returns <code>true</code> the attribute is mandatory in the request.
   **
   ** @return                    <code>true</code> the attribute has to be
   **                            masked in the request; otherwise
   **                            <code>false</code>.
   */
  public final Boolean isMasked() {
    return this.masked;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setHidden
  /**
   ** Whether the attribute is hidden in the UI of the request or not.
   **
   ** @param  hidden             the attribute is hidden in the UI of the
   **                            request or not.
   */
  public void setHidden(final Boolean hidden) {
    this.hidden = hidden;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   isHidden
  /**
   ** Returns <code>true</code> the attribute is mandatory in the request.
   **
   ** @return                    <code>true</code> the attribute is hidden in
   **                            in the request; otherwise <code>false</code>.
   */
  public final Boolean isHidden() {
    return this.hidden;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setReadOnly
  /**
   ** Whether the attribute is read only in the request or not.
   **
   ** @param  readOnly           <code>true</code> the attribute is read only in
   **                            the request; otherwise <code>false</code>.
   */
  public void setReadOnly(final Boolean readOnly) {
    this.readOnly = readOnly;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   isReadOnly
  /**
   ** Returns <code>true</code> the attribute is read only in the request.
   **
   ** @return                    <code>true</code> the attribute is read only in
   **                            the request; otherwise <code>false</code>.
   */
  public final Boolean isReadOnly() {
    return this.readOnly;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setApproverOnly
  /**
   ** Whether the attribute is accessible in the request by the approver only
   ** or not.
   **
   ** @param  approverOnly       the attribute is accessible in the request by
   **                            the approver only or not.
   */
  public void setApproverOnly(final Boolean approverOnly) {
    this.approverOnly = approverOnly;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   isApproverOnly
  /**
   ** Returns <code>true</code> the attribute is accessible in the request by
   ** the approver only.
   **
   ** @return                    <code>true</code> the attribute is approver
   **                            only in the request; otherwise
   **                            <code>false</code>.
   */
  public final Boolean isApproverOnly() {
    return this.approverOnly;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setAvailableInBulk
  /**
   ** Whether the attribute can be modified in bulk operations on the request or
   ** not.
   **
   ** @param  availableInBulk    <code>true</code> if the attribute can be
   **                            modified in bulk operations on the request;
   **                            otherwise <code>false</code>.
   ** not.
   */
  public void setAvailableInBulk(final boolean availableInBulk) {
    this.availableInBulk = availableInBulk;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   isAvailableInBulk
  /**
   ** Returns <code>true</code> if the attribute can be modified in bulk
   ** operations on the request.
   **
   ** @return                    <code>true</code> if the attribute can be
   **                            modified in bulk operations on the request;
   **                            otherwise <code>false</code>.
   */
  public final boolean isAvailableInBulk() {
    return this.availableInBulk;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setWidget
  /**
   ** Sets the widget (UI representation) of the attribute.
   **
   ** @param  widget             the widget (UI representation) of the attribute.
   */
  public void setWidget(final Widget widget) {
    setWidget(widget.getValue());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setWidget
  /**
   ** Sets the widget (UI representation) of the attribute.
   **
   ** @param  widget             the widget (UI representation) of the attribute.
   */
  public void setWidget(final String widget) {
    this.widget = widget;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getWidget
  /**
   ** Returns the widget (UI representation) of the attribute.
   **
   ** @return                    the widget (UI representation) of the attribute.
   */
  public final String getWidget() {
    return this.widget;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setLookupCode
  /**
   ** Sets the lookup code (<code>Lookup Definition</code>) to restrict values
   ** on the attribute.
   **
   ** @param  lookupCode         the <code>Lookup Definition</code> used to
   **                            restrict values on the attribute.
   */
  public void setLookupCode(final String lookupCode) {
    this.lookupCode = lookupCode;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getLookupCode
  /**
   ** Returns the lookup code (<code>Lookup Definition</code>) to restrict
   ** values on the attribute.
   **
   ** @return                    the <code>Lookup Definition</code> used to
   **                            restrict values on the attribute.
   */
  public final String getLookupCode() {
    return this.lookupCode;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getAttribute
  /**
   ** Returns the value of the attribute property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object. This is why there is not a <code>set</code> method
   ** for the attribute property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   getAttribute().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list {@link Attribute}
   **
   ** @return                   the value of the attribute property.
   */
  public List<Attribute> getAttribute() {
    if (this.attribute == null)
      this.attribute = new ArrayList<Attribute>();

    return this.attribute;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getLookupValues
  /**
   ** Returns the value of the lookupValues property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object. This is why there is not a <code>set</code> method
   ** for the lookupValues property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   getLookupValues().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list
   ** {@link LookupValues}
   **
   ** @return                   the value of the lookupValues property.
   */
  public List<LookupValues> getLookupValues() {
    if (this.lookupValues == null)
      this.lookupValues = new ArrayList<LookupValues>();

    return this.lookupValues;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrePopulationAdapter
  /**
   ** Sets the pre-population adapter assigned to this attribute.
   **
   ** @param  adapter            the pre-population adapter assigned to this
   **                            attribute.
   */
  public void setPrePopulationAdapter(final PrePopAdapter adapter) {
    this.prePopulationAdapter = adapter;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getPrePopulationAdapter
  /**
   ** Returns the pre-population adapter assigned to this attribute.
   **
   ** @return                    the pre-population adapter assigned to this
   **                            attribute.
   */
  public PrePopAdapter getPrePopulationAdapter() {
    return this.prePopulationAdapter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: validate (overridden)
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** <b>Note:</b>
   ** We are not calling the validation method on the super class to prevent
   ** the validation of the <code>parameter</code> mapping.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (StringUtility.isEmpty(this.name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "name"));

    if (StringUtility.isEmpty(this.type))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "type"));

    if (StringUtility.isEmpty(this.widget))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "widget"));

    if (this.length == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "length"));
  }
}