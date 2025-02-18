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

    File        :   ConfigData.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigData.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class ConfigData
// ~~~~~ ~~~~~~~~~~
/**
 ** Java class for config-data complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="config-data"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="name"                type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="description"         type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="id"                  type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
 **       &lt;attribute name="icon"                type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="hoverIcon"           type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="disabledIcon"        type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="depressedIcon"       type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="selectedModuleRefId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name="config-data", propOrder={"name", "description"})
@XmlSeeAlso({ConsoleType.class, ModuleType.class, TaskFlowType.class})
public abstract class ConfigData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String        LOCAL       = "config-data";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute(required=true)
  protected String id;
  @XmlAttribute
  protected String icon;
  @XmlAttribute
  protected String hoverIcon;
  @XmlAttribute
  protected String depressedIcon;
  @XmlAttribute
  protected String disabledIcon;
  @XmlAttribute
  protected String selectedModuleRefId;
  @XmlElement(required=true)
  protected String name;
  @XmlElement
  protected String description;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ConfigData</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ConfigData() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              the value of the description property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the value of the description property.
   **
   ** @return                    the value of the description property.
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId
  /**
   ** Sets the value of the id property.
   **
   ** @param  value              the value of the id property.
   **                            Allowed object is {@link String}.
   */
  public void setId(String value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
   ** Returns the value of the id property.
   **
   ** @return                    the value of the id property.
   **                            Possible object is {@link String}.
   */
  public String getId() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIcon
  /**
   ** Sets the value of the icon property.
   **
   ** @param  value              the value of the icon property.
   **                            Allowed object is {@link String}.
   */
  public void setIcon(final String value) {
    this.icon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon
  /**
   ** Returns the value of the icon property.
   **
   ** @return                    the value of the icon property.
   **                            Possible object is {@link String}.
   */
  public String getIcon() {
    return this.icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHoverIcon
  /**
   ** Sets the value of the hoverIcon property.
   **
   ** @param  value              the value of the hoverIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setHoverIcon(final String value) {
    this.hoverIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHoverIcon
  /**
   ** Returns the value of the hoverIcon property.
   **
   ** @return                    the value of the hoverIcon property.
   **                            Possible object is {@link String}.
   */
  public String getHoverIcon() {
    return this.hoverIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDepressedIcon
  /**
   ** Sets the value of the depressedIcon property.
   **
   ** @param  value              the value of the depressedIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setDepressedIcon(final String value) {
    this.depressedIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDepressedIcon
  /**
   ** Returns the value of the depressedIcon property.
   **
   ** @return                    the value of the depressedIcon property.
   **                            Possible object is {@link String}.
   */
  public String getDepressedIcon() {
    return this.depressedIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisabledIcon
  /**
   ** Sets the value of the depressedIcon property.
   **
   ** @param  value              the value of the depressedIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setDisabledIcon(final String value) {
    this.depressedIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisabledIcon
  /**
   ** Returns the value of the depressedIcon property.
   **
   ** @return                    the value of the depressedIcon property.
   **                            Possible object is {@link String}.
   */
  public String getDisabledIcon() {
    return this.depressedIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSelectedModuleRefId
  /**
   ** Returns the value of the selectedModuleRefId property.
   **
   ** @return                    the value of the selectedModuleRefId property.
   **                            Possible object is {@link String}.
   */
  public String getSelectedModuleRefId() {
    return this.selectedModuleRefId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSelectedModuleRefId
  /**
   ** Sets the value of the selectedModuleRefId property.
   **
   ** @param  value              the value of the selectedModuleRefId property.
   **                            Allowed object is {@link String}.
   */
  public void setSelectedModuleRefId(final String value) {
    this.selectedModuleRefId = value;
  }
}