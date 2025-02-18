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

    File        :   LookupValues.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupValues.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class LookupValues
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>LookupValues</code> is a container representing the values of a
 ** value restriction on an attribute in a Request DataSet.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="lookup-values"&gt;
 **  &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;attribute name="encoded-value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="decoded-value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name="lookup-values")
public class LookupValues {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute(name="encoded-value")
  protected String encodedValue;

  @XmlAttribute(name="decoded-value")
  protected String decodedValue;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupValues</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupValues() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setEncodedValue
  /**
   ** Sets the value of the encodedValue property.
   **
   ** @param  encodedValue       the value of the encodedValue property to set.
   */
  public void setEncodedValue(final String encodedValue) {
    this.encodedValue = encodedValue;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getEncodedValue
  /**
   ** Returns the value of the encodedValue property.
   **
   ** @return                    the value of the encodedValue property.
   */
  public final String getEncodedValue() {
    return this.encodedValue;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setDecodedValue
  /**
   ** Sets the value of the decodedValue property.
   **
   ** @param  decodedValue       the value of the decodedValue property to set.
   */
  public void setDecodedValue(final String decodedValue) {
    this.decodedValue = decodedValue;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getDecodedValue
  /**
   ** Returns the value of the decodedValue property.
   **
   ** @return                    the value of the decodedValue property.
   */
  public final String getDecodedValue() {
    return this.decodedValue;
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

    if (StringUtility.isEmpty(this.encodedValue))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "encodedValue"));

    if (StringUtility.isEmpty(this.decodedValue))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "decodedValue"));
  }
}