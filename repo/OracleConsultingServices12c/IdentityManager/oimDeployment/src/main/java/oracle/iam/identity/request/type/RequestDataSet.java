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

    File        :   RequestDataSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RequestDataSet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class RequestDataSet
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>RequestDataSet</code> is a container representing a Request DataSet
 ** importable in Identity Manager.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="DataSetValidator"    type="{http://www.oracle.com/schema/oim/request}data-set-validator" minOccurs="0"/&gt;
 **         &lt;element name="AttributeReference"  type="{http://www.oracle.com/schema/oim/request}attribute-reference" maxOccurs="unbounded" minOccurs="0"/&gt;
 **         &lt;element name="Attribute"           type="{http://www.oracle.com/schema/oim/request}attribute" maxOccurs="unbounded" minOccurs="0"/&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="entity"              type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="operation"           type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name="", propOrder={ "dataSetValidator", "attributeReference", "attribute"})
@XmlRootElement(name="request-data-set")
public class RequestDataSet {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(name="DataSetValidator")
  protected DataSetValidator         dataSetValidator;

  @XmlElement(name="AttributeReference")
  protected List<AttributeReference> attributeReference;

  @XmlElement(name="Attribute")
  protected List<Attribute>          attribute;

  @XmlAttribute(required=true)
  protected String                   name;

  @XmlAttribute
  protected String                   entity;

  @XmlAttribute
  protected String                   operation;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RequestDataSet</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RequestDataSet() {
    // ensure inheritance
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
  // Method:   setEntity
  /**
   ** Sets the entity of the attribute.
   **
   ** @param  entity             the entity  of the attribute.
   */
  public void setEntity(final String entity) {
    this.entity = entity;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getEntity
  /**
   ** Returns the entity of the attribute.
   **
   ** @return                    the entity  of the attribute.
   */
  public final String getEntity() {
    return this.entity;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setOperation
  /**
   ** Sets the operation of the attribute.
   **
   ** @param  operation          the operation  of the attribute.
   */
  public void setOperation(final String operation) {
    this.operation = operation;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getOperation
  /**
   ** Returns the operation of the attribute.
   **
   ** @return                    the operation  of the attribute.
   */
  public final String getOperation() {
    return this.operation;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getAttribute
  /**
   ** Returns ...
   **
   ** @return                    ...
   */
  public List<Attribute> getAttribute() {
    if (this.attribute == null)
      this.attribute = new ArrayList<Attribute>();

    return this.attribute;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeReference
  /**
   ** Returns ...
   **
   ** @return                    ...
   */
  public List<AttributeReference> getAttributeReference() {
    if (this.attributeReference == null)
      this.attributeReference = new ArrayList<AttributeReference>();

    return this.attributeReference;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setDataSetValidator
  /**
   ** Sets the {@link DataSetValidator} associated with this request data set.
   **
   ** @param  dataSetValidator   the {@link DataSetValidator} used by this
   **                            request data set.
   */
  public void setDataSetValidator(final DataSetValidator dataSetValidator) {
    this.dataSetValidator = dataSetValidator;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getDataSetValidator
  /**
   ** Returns the {@link DataSetValidator} associated with this request data set.
   **
   ** @return                    the {@link DataSetValidator} associated with
   **                            this request data set.
   */
  public DataSetValidator getDataSetValidator() {
    return this.dataSetValidator;
  }
}