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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   ShellConfig.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ShellConfig.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class TaskFlowType
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Java class for taskflow complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="taskflow"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://xmlns.oracle.com/hst/adf/shell}config-data"&gt;
 **       &lt;attribute name="taskFlow"         type="{http://www.w3.org/2001/XMLSchema}string"  use="required"/&gt;
 **       &lt;attribute name="closeable"        type="{http://www.w3.org/2001/XMLSchema}boolean" use="required"/&gt;
 **       &lt;attribute name="dialog"           type="{http://www.w3.org/2001/XMLSchema}boolean" use="required"/&gt;
 **       &lt;attribute name="dirty"            type="{http://www.w3.org/2001/XMLSchema}string"  use="required"/&gt;
 **       &lt;attribute name="topic"            type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="product"          type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="enableLinking"    type="{http://www.w3.org/2001/XMLSchema}boolean" default="false"/&gt;
 **       &lt;attribute name="allowedURLParams" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlType(name=TaskFlowType.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskFlowType extends ConfigData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String LOCAL = "taskflow";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute(required=true)
  protected String  taskFlow;
  @XmlAttribute(required=true)
  protected boolean closeable;
  @XmlAttribute(required=true)
  protected boolean dialog;
  @XmlAttribute(required=true)
  protected String  dirty;
  @XmlAttribute
  protected String  topic;
  @XmlAttribute
  protected String  product;
  @XmlAttribute
  protected Boolean enableLinking;
  @XmlAttribute
  protected String  allowedURLParams;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>TaskFlowType</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public TaskFlowType() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTaskFlow
  /**
   ** Sets the value of the taskFlow property.
   **
   ** @param  value              the value of the taskFlow property.
   **                            Allowed object is {@link String}.
   */
  public void setTaskFlow(final String value) {
    this.taskFlow = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTaskFlow
  /**
   ** Returns the value of the taskFlow property.
   **
   ** @return                    the value of the taskFlow property.
   **                            Possible object is {@link String}.
   */
  public String getTaskFlow() {
    return this.taskFlow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCloseable
  /**
   ** Sets the value of the closeable property.
   **
   ** @param  value              the value of the closeable property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setCloseable(final boolean value) {
    this.closeable = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCloseable
  /**
   ** Returns the value of the closeable property.
   **
   ** @return                    the value of the closeable property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isCloseable() {
    return this.closeable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDialog
  /**
   ** Sets the value of the dialog property.
   **
   ** @param  value              the value of the dialog property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setDialog(final boolean value) {
    this.dialog = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDialog
  /**
   ** Returns the value of the dialog property.
   **
   ** @return                    the value of the dialog property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isDialog() {
    return this.dialog;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDirty
  /**
   ** Sets the value of the dirty property.
   **
   ** @param  value              the value of the dirty property.
   **                            Allowed object is {@link String}.
   */
  public void setDirty(final String value) {
    this.dirty = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDirty
  /**
   ** Returns the value of the dirty property.
   **
   ** @return                    the value of the dirty property.
   **                            Possible object is {@link String}.
   */
  public String getDirty() {
    return this.dirty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTopic
  /**
   ** Sets the value of the topic property.
   **
   ** @param  value              the value of the topic property.
   **                            Allowed object is {@link String}.
   */
  public void setTopic(final String value) {
    this.topic = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTopic
  /**
   ** Returns the value of the topic property.
   **
   ** @return                    the value of the topic property.
   **                            Possible object is {@link String}.
   */
  public String getTopic() {
    return this.topic;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProduct
  /**
   ** Sets the value of the product property.
   **
   ** @param  value              the value of the product property.
   **                            Allowed object is {@link String}.
   */
  public void setProduct(final String value) {
    this.product = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProduct
  /**
   ** Returns the value of the product property.
   **
   ** @return                    the value of the product property.
   **                            Possible object is {@link String}.
   */
  public String getProduct() {
    return this.product;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnableLinking
  /**
   ** Sets the value of the enableLinking property.
   **
   ** @param  value              the value of the enableLinking property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setEnableLinking(final Boolean value) {
    this.enableLinking = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnableLinking
  /**
   ** Returns the value of the enableLinking property.
   **
   ** @return                    the value of the enableLinking property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean getEnableLinking() {
    return (this.enableLinking == null) ? false : this.enableLinking.booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowedURLParams
  /**
   ** Sets the value of the allowedURLParams property.
   **
   ** @param  value              the value of the allowedURLParams property.
   **                            Allowed object is {@link String}.
   */
  public void setAllowedURLParams(final String value) {
    this.allowedURLParams = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowedURLParams
  /**
   ** Returns the value of the allowedURLParams property.
   **
   ** @return                    the value of the allowedURLParams property.
   **                            Possible object is {@link String}.
   */
  public String getAllowedURLParams() {
    return this.allowedURLParams;
  }
}