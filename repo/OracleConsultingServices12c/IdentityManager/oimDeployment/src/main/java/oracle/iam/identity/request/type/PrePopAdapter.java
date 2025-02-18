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

    File        :   PrePopAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PrePopAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class PrePopAdapter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>PrePopAdapter</code> is a container representing the pre-populate
 ** rules of an attribute in a Request DataSet.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="pre-pop-adapter"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="classname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
@XmlType(name="pre-pop-adapter")
public class PrePopAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute
  protected String name;

  @XmlAttribute(required=true)
  protected String classname;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PrePopAdapter</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PrePopAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  name               the value of the name property to set.
   */
  public void setName(final String name) {
    this.name = name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   */
  public final String getName() {
    return this.name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setClassName
  /**
   ** Sets the value of the className property.
   **
   ** @param  classname          the value of the className property to set.
   */
  public void setClassName(final String classname) {
    this.classname = classname;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getClassName
  /**
   ** Returns the value of the className property.
   **
   ** @return                    the value of the className property.
   */
  public final String getClassName() {
    return this.classname;
  }
}