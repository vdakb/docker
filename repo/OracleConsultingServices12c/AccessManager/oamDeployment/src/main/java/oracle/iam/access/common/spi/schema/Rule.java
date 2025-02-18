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

    File        :   Rule.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Rule.


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
// class Rule
// ~~~~~ ~~~~
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
 **         &lt;element name="ruleEffect" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element ref="{}conditionCombiner" minOccurs="0"/&gt;
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
@XmlRootElement(name=Rule.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"ruleEffect", "conditionCombiner"})
public class Rule {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "rule";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String            ruleEffect;
  protected ConditionCombiner conditionCombiner;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Rule</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Rule() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRuleEffect
  /**
   ** Sets the value of the <code>ruleEffect</code> property.
   **
   ** @param  value              the value of the <code>ruleEffect</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setRuleEffect(final String value) {
    this.ruleEffect = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRuleEffect
  /**
   ** Returns the value of the <code>ruleEffect</code> property.
   **
   ** @return                    the value of the <code>ruleEffect</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getRuleEffect() {
    return this.ruleEffect;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConditionCombiner
  /**
   ** Sets the value of the <code>conditionCombiner</code> property.
   **
   ** @param  value              the value of the <code>conditionCombiner</code>
   **                            property.
   **                            Allowed object is {@link ConditionCombiner}.
   */
  public void setConditionCombiner(final ConditionCombiner value) {
    this.conditionCombiner = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConditionCombiner
  /**
   ** Returns the value of the <code>conditionCombiner</code> property.
   **
   ** @return                    the value of the <code>conditionCombiner</code>
   **                            property.
   **                            Possible object is {@link ConditionCombiner}.
   */
  public ConditionCombiner getConditionCombiner() {
    return this.conditionCombiner;
  }
}