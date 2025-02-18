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

    File        :   ConditionCombiner.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ConditionCombiner.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class ConditionCombiner
// ~~~~~ ~~~~~~~~~~~~~~~~~
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
 **         &lt;element name="combinerType"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element ref="{}combinerMode"                                                 minOccurs="0"/&gt;
 **         &lt;element name="expression"    type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlRootElement(name=ConditionCombiner.LOCAL)
@XmlType(name="", propOrder={"combinerType", "combinerMode", "expression"})
public class ConditionCombiner {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "conditionCombiner";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String       combinerType;
  protected CombinerMode combinerMode;
  protected String       expression;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ConditionCombiner</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ConditionCombiner() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCombinerType
  /**
   ** Sets the value of the <code>combinerType</code> property.
   **
   ** @param  value              the value of the <code>combinerType</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setCombinerType(final String value) {
    this.combinerType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCombinerType
  /**
   ** Returns the value of the <code>combinerType</code> property.
   **
   ** @return                    the value of the <code>combinerType</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getCombinerType() {
    return this.combinerType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCombinerMode
  /**
   ** Sets the value of the <code>combinerMode</code> property.
   **
   ** @param  value              the value of the <code>combinerMode</code>
   **                            property.
   **                            Allowed object is {@link CombinerMode}.
   */
  public void setCombinerMode(final CombinerMode value) {
    this.combinerMode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCombinerMode
  /**
   ** Returns the value of the <code>combinerMode</code> property.
   **
   ** @return                    the value of the <code>combinerMode</code>
   **                            property.
   **                            Possible object is {@link CombinerMode}.
   */
  public CombinerMode getCombinerMode() {
    return this.combinerMode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExpression
  /**
   ** Sets the value of the <code>expression</code> property.
   **
   ** @param  value              the value of the <code>expression</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setExpression(final String value) {
    this.expression = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExpression
  /**
   ** Returns the value of the <code>expression</code> property.
   **
   ** @return                    the value of the <code>expression</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getExpression() {
    return this.expression;
  }
}