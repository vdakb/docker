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

    File        :   DataSetValidator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DataSetValidator.


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
// class DataSetValidator
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>DataSetValidator</code> is a container representing the validator
 ** that can be applied on a Request DataSet.
 ** <p>
 ** The following schema fragment specifies the expected content contained within this class.
 ** <pre>
 ** &lt;complexType name="data-set-validator"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="classname" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name="data-set-validator")
public class DataSetValidator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** This attribute specifies a logical name of the DataSetValidator plug-in.
   */
  @XmlAttribute
  protected String name;

  /**
    ** This attribute specifies the fully qualified name of the implemented
    ** plug-in class.
    */
  @XmlAttribute(required=true)
  protected String classname;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupValues</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DataSetValidator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the logical name of the <code>DataSetValidator</code> plug-in.
   **
   ** @param  name               the logical name of the
   **                            <code>DataSetValidator</code> plug-in.
   */
  public void setName(final String name) {
    this.name = name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the logical name of the <code>DataSetValidator</code> plug-in.
   **
   ** @return                    the logical name of the
   **                            <code>DataSetValidator</code> plug-in.
   */
  public final String getName() {
    return this.name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setClassName
  /**
   ** Sets the fully qualified name of the implemented plug-in class.
   **
   ** @param  classname          the fully qualified name of the implemented
   **                            plug-in class to set.
   */
  public void setClassName(final String classname) {
    this.classname = classname;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getClassName
  /**
   ** Returns the fully qualified name of the implemented plug-in class.
   **
   ** @return                    the fully qualified name of the implemented
   **                            plug-in class .
   */
  public final String getClassName() {
    return this.classname;
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
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (StringUtility.isEmpty(this.name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "name"));

    if (StringUtility.isEmpty(this.classname))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "classname"));
  }
}