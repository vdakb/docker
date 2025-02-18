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

    File        :   ModuleType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ModuleType.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model.schema;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class ModuleType
// ~~~~~ ~~~~~~~~~~
/**
 ** Java class for module complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="module"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://xmlns.oracle.com/hst/adf/shell}config-data"&gt;
 **       &lt;choice&gt;
 **         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;sequence&gt;
 **           &lt;element name="toolbar-area" minOccurs="0"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence&gt;
 **                     &lt;element name="taskflow"&gt;
 **                       &lt;complexType&gt;
 **                         &lt;complexContent&gt;
 **                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                             &lt;attribute name="refId"  type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
 **                             &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
 **                           &lt;/restriction&gt;
 **                         &lt;/complexContent&gt;
 **                       &lt;/complexType&gt;
 **                     &lt;/element&gt;
 **                   &lt;/sequence&gt;
 **                   &lt;attribute name="rendered" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                 &lt;/restriction&gt;
 **               &lt;/complexContent&gt;
 **             &lt;/complexType&gt;
 **           &lt;/element&gt;
 **           &lt;element name="rhs-area" minOccurs="0"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence&gt;
 **                     &lt;element name="taskflow"&gt;
 **                       &lt;complexType&gt;
 **                         &lt;complexContent&gt;
 **                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                             &lt;attribute name="refId" type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
 **                             &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                           &lt;/restriction&gt;
 **                         &lt;/complexContent&gt;
 **                       &lt;/complexType&gt;
 **                     &lt;/element&gt;
 **                   &lt;/sequence&gt;
 **                 &lt;/restriction&gt;
 **               &lt;/complexContent&gt;
 **             &lt;/complexType&gt;
 **           &lt;/element&gt;
 **           &lt;choice&gt;
 **             &lt;element name="lhs-area" minOccurs="0"&gt;
 **               &lt;complexType&gt;
 **                 &lt;complexContent&gt;
 **                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                     &lt;sequence&gt;
 **                       &lt;element name="taskflow"&gt;
 **                         &lt;complexType&gt;
 **                           &lt;complexContent&gt;
 **                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                               &lt;attribute name="refId" type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
 **                             &lt;/restriction&gt;
 **                           &lt;/complexContent&gt;
 **                         &lt;/complexType&gt;
 **                       &lt;/element&gt;
 **                     &lt;/sequence&gt;
 **                     &lt;attribute name="rendered" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                   &lt;/restriction&gt;
 **                 &lt;/complexContent&gt;
 **               &lt;/complexType&gt;
 **             &lt;/element&gt;
 **             &lt;element name="no-lhs-area" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 **           &lt;/choice&gt;
 **           &lt;choice&gt;
 **             &lt;element name="default-taskflow" minOccurs="0"&gt;
 **               &lt;complexType&gt;
 **                 &lt;complexContent&gt;
 **                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                     &lt;attribute name="refId" type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
 **                   &lt;/restriction&gt;
 **                 &lt;/complexContent&gt;
 **               &lt;/complexType&gt;
 **             &lt;/element&gt;
 **             &lt;element name="default-taskflow-list" minOccurs="0"&gt;
 **               &lt;complexType&gt;
 **                 &lt;complexContent&gt;
 **                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                     &lt;sequence maxOccurs="unbounded"&gt;
 **                       &lt;element name="taskflow"&gt;
 **                         &lt;complexType&gt;
 **                           &lt;complexContent&gt;
 **                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                               &lt;attribute name="refId" type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
 **                             &lt;/restriction&gt;
 **                           &lt;/complexContent&gt;
 **                         &lt;/complexType&gt;
 **                       &lt;/element&gt;
 **                     &lt;/sequence&gt;
 **                   &lt;/restriction&gt;
 **                 &lt;/complexContent&gt;
 **               &lt;/complexType&gt;
 **             &lt;/element&gt;
 **           &lt;/choice&gt;
 **         &lt;/sequence&gt;
 **       &lt;/choice&gt;
 **       &lt;attribute name="model" default="default"&gt;
 **         &lt;simpleType&gt;
 **           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 **             &lt;enumeration value="default"/&gt;
 **             &lt;enumeration value="single"/&gt;
 **           &lt;/restriction&gt;
 **         &lt;/simpleType&gt;
 **       &lt;/attribute&gt;
 **       &lt;attribute name="rendered" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="topic"    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="product"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name=ModuleType.LOCAL, propOrder={"url", "toolbarArea", "rhsArea", "lhsArea", "noLhsArea", "defaultTaskflow", "defaultTaskflowList"})
public class ModuleType extends ConfigData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String LOCAL = "module";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String                         url;
  @XmlElement(name="toolbar-area")
  protected ModuleType.ToolbarArea         toolbarArea;
  @XmlElement(name="rhs-area")
  protected ModuleType.RhsArea             rhsArea;
  @XmlElement(name="lhs-area")
  protected ModuleType.LhsArea             lhsArea;
  @XmlElement(name="no-lhs-area")
  protected Object                         noLhsArea;
  @XmlElement(name="default-taskflow")
  protected ModuleType.DefaultTaskflow     defaultTaskflow;
  @XmlElement(name="default-taskflow-list")
  protected ModuleType.DefaultTaskflowList defaultTaskflowList;
  @XmlAttribute
  protected String                         model;
  @XmlAttribute
  protected String                         rendered;
  @XmlAttribute
  protected String                         topic;
  @XmlAttribute
  protected String                         product;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ModuleType</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ModuleType() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUrl
  /**
   ** Sets the value of the url property.
   **
   ** @param  value              the value of the url property.
   **                            Allowed object is {@link String}.
   */
  public void setUrl(final String value) {
    this.url = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUrl
  /**
   ** Returns the value of the url property.
   **
   ** @return                    the value of the url property.
   **                            Possible object is {@link String}.
   */
  public String getUrl() {
    return this.url;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setToolbarArea
  /**
   ** Sets the value of the toolbarArea property.
   **
   ** @param  value              the value of the toolbarArea property.
   **                            Allowed object is
   **                            {@link ModuleType.ToolbarArea}.
   */
  public void setToolbarArea(final ToolbarArea value) {
    this.toolbarArea = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getToolbarArea
  /**
   ** Returns the value of the toolbarArea property.
   **
   ** @return                    the value of the toolbarArea property.
   **                            Possible object is
   **                            {@link ModuleType.ToolbarArea}.
   */
  public ToolbarArea getToolbarArea() {
    return this.toolbarArea;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLhsArea
  /**
   ** Sets the value of the lhsArea property.
   **
   ** @param  value              the value of the lhsArea property.
   **                            Allowed object is
   **                            {@link ModuleType.LhsArea}.
   */
  public void setLhsArea(ModuleType.LhsArea value) {
    this.lhsArea = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLhsArea
  /**
   ** Returns the value of the lhsArea property.
   **
   ** @return                    the value of the lhsArea property.
   **                            Possible object is
   **                            {@link ModuleType.LhsArea}.
   */
  public ModuleType.LhsArea getLhsArea() {
    return this.lhsArea;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRhsArea
  /**
   ** Sets the value of the rhsArea property.
   **
   ** @param  value              the value of the rhsArea property.
   **                            Allowed object is
   **                            {@link ModuleType.RhsArea}.
   */
  public void setRhsArea(final RhsArea value) {
    this.rhsArea = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRhsArea
  /**
   ** Returns the value of the rhsArea property.
   **
   ** @return                    the value of the rhsArea property.
   **                            Possible object is
   **                            {@link ModuleType.RhsArea}.
   */
  public RhsArea getRhsArea() {
    return this.rhsArea;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNoLhsArea
  /**
   ** Sets the value of the noLhsArea property.
   **
   ** @param  value              the value of the rhsArea property.
   **                            Allowed object is {@link Object}.
   */
  public void setNoLhsArea(final Object value) {
    this.noLhsArea = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNoLhsArea
  /**
   ** Returns the value of the noLhsArea property.
   **
   ** @return                    the value of the noLhsArea property.
   **                            Possible object is {@link Object}.
   */
  public Object getNoLhsArea() {
    return this.noLhsArea;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDefaultTaskflow
  /**
   ** Sets the value of the defaultTaskflow property.
   **
   ** @param  value              the value of the defaultTaskflow property.
   **                            Allowed object is
   **                            {@link ModuleType.DefaultTaskflow}.
   */
  public void setDefaultTaskflow(ModuleType.DefaultTaskflow value) {
    this.defaultTaskflow = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDefaultTaskflow
  /**
   ** Returns the value of the defaultTaskflow property.
   **
   ** @return                    the value of the defaultTaskflow property.
   **                            Possible object is
   **                            {@link ModuleType.DefaultTaskflow}.
   */
  public ModuleType.DefaultTaskflow getDefaultTaskflow() {
    return this.defaultTaskflow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDefaultTaskflowList
  /**
   ** Sets the value of the defaultTaskflowList property.
   **
   ** @param  value              the value of the defaultTaskflowList property.
   **                            Allowed object is
   **                            {@link ModuleType.DefaultTaskflowList}.
   */
  public void setDefaultTaskflowList(ModuleType.DefaultTaskflowList value) {
    this.defaultTaskflowList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDefaultTaskflowList
  /**
   ** Returns the value of the defaultTaskflowList property.
   **
   ** @return                    the value of the defaultTaskflowList property.
   **                            Possible object is
   **                            {@link ModuleType.DefaultTaskflowList}.
   */
  public ModuleType.DefaultTaskflowList getDefaultTaskflowList() {
    return this.defaultTaskflowList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModel
  /**
   ** Sets the value of the model property.
   **
   ** @param  value              the value of the model property.
   **                            Allowed object is {@link String}.
   */
  public void setModel(final String value) {
    this.model = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModel
  /**
   ** Returns the value of the model property.
   **
   ** @return                    the value of the model property.
   **                            Possible object is {@link String}.
   */
  public String getModel() {
    return (this.model == null) ? "default" : this.model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRendered
  /**
   ** Sets the value of the rendered property.
   **
   ** @param  value              the value of the rendered property.
   **                            Allowed object is {@link String}.
   */
  public void setRendered(final String value) {
    this.rendered = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRendered
  /**
   ** Returns the value of the rendered property.
   **
   ** @return                    the value of the rendered property.
   **                            Possible object is {@link String}.
   */
  public String getRendered() {
    return this.rendered;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTopic
  /**
   ** Sets the value of the topic property.
   **
   ** @param  value              the value of the topic property.
   **                            Allowed object is {@link String}.
   */
  public void setTopic(String value) {
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
  public void setProduct(String value) {
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

  /**
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="")
  public static class DefaultTaskflow {

    @XmlAttribute(required=true)
    protected String refId;

    /**
     * Gets the value of the refId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRefId() {
      return refId;
    }

    /**
     * Sets the value of the refId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRefId(String value) {
      this.refId = value;
    }

  }


  /**
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence maxOccurs="unbounded"&gt;
   *         &lt;element name="taskflow"&gt;
   *           &lt;complexType&gt;
   *             &lt;complexContent&gt;
   *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *                 &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *               &lt;/restriction&gt;
   *             &lt;/complexContent&gt;
   *           &lt;/complexType&gt;
   *         &lt;/element&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="", propOrder = { "taskflow" })
  public static class DefaultTaskflowList {

    @XmlElement(required=true)
    protected List<ModuleType.DefaultTaskflowList.Taskflow> taskflow;

    /**
     * Gets the value of the taskflow property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <code>set</code> method for the taskflow property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaskflow().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ModuleType.DefaultTaskflowList.Taskflow }
     *
     * @return                   the value of the taskflow property.
     */
    public List<ModuleType.DefaultTaskflowList.Taskflow> getTaskflow() {
      if (this.taskflow == null) {
        this.taskflow = new ArrayList<ModuleType.DefaultTaskflowList.Taskflow>();
      }
      return this.taskflow;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name="")
    public static class Taskflow {

      @XmlAttribute(required=true)
      protected String refId;

      /**
       * Gets the value of the refId property.
       *
       * @return
       *     possible object is
       *     {@link String }
       *
       */
      public String getRefId() {
        return refId;
      }

      /**
       * Sets the value of the refId property.
       *
       * @param value
       *     allowed object is
       *     {@link String }
       *
       */
      public void setRefId(String value) {
        this.refId = value;
      }

    }

  }


  /**
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence&gt;
   *         &lt;element name="taskflow"&gt;
   *           &lt;complexType&gt;
   *             &lt;complexContent&gt;
   *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *                 &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *               &lt;/restriction&gt;
   *             &lt;/complexContent&gt;
   *           &lt;/complexType&gt;
   *         &lt;/element&gt;
   *       &lt;/sequence&gt;
   *       &lt;attribute name="rendered" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="", propOrder = { "taskflow" })
  public static class LhsArea {

    @XmlElement(required=true)
    protected ModuleType.LhsArea.Taskflow taskflow;
    @XmlAttribute
    protected String rendered;

    /**
     * Gets the value of the taskflow property.
     *
     * @return
     *     possible object is
     *     {@link ModuleType.LhsArea.Taskflow }
     *
     */
    public ModuleType.LhsArea.Taskflow getTaskflow() {
      return this.taskflow;
    }

    /**
     * Sets the value of the taskflow property.
     *
     * @param value
     *     allowed object is
     *     {@link ModuleType.LhsArea.Taskflow }
     *
     */
    public void setTaskflow(ModuleType.LhsArea.Taskflow value) {
      this.taskflow = value;
    }

    /**
     * Gets the value of the rendered property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRendered() {
      return rendered;
    }

    /**
     * Sets the value of the rendered property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRendered(String value) {
      this.rendered = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name="")
    public static class Taskflow {

      @XmlAttribute(required=true)
      protected String refId;

      /**
       * Gets the value of the refId property.
       *
       * @return
       *     possible object is
       *     {@link String }
       *
       */
      public String getRefId() {
        return this.refId;
      }

      /**
       * Sets the value of the refId property.
       *
       * @param value
       *     allowed object is
       *     {@link String }
       *
       */
      public void setRefId(String value) {
        this.refId = value;
      }

    }

  }


  /**
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence&gt;
   *         &lt;element name="taskflow"&gt;
   *           &lt;complexType&gt;
   *             &lt;complexContent&gt;
   *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *                 &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *                 &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *               &lt;/restriction&gt;
   *             &lt;/complexContent&gt;
   *           &lt;/complexType&gt;
   *         &lt;/element&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="", propOrder = { "taskflow" })
  public static class RhsArea {

    @XmlElement(required=true)
    protected ModuleType.RhsArea.Taskflow taskflow;

    /**
     * Gets the value of the taskflow property.
     *
     * @return
     *     possible object is
     *     {@link ModuleType.RhsArea.Taskflow }
     *
     */
    public ModuleType.RhsArea.Taskflow getTaskflow() {
      return taskflow;
    }

    /**
     * Sets the value of the taskflow property.
     *
     * @param value
     *     allowed object is
     *     {@link ModuleType.RhsArea.Taskflow }
     *
     */
    public void setTaskflow(ModuleType.RhsArea.Taskflow value) {
      this.taskflow = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name="")
    public static class Taskflow {

      @XmlAttribute(required=true)
      protected String refId;
      @XmlAttribute
      protected String width;

      /**
       * Gets the value of the refId property.
       *
       * @return
       *     possible object is
       *     {@link String }
       *
       */
      public String getRefId() {
        return refId;
      }

      /**
       * Sets the value of the refId property.
       *
       * @param value
       *     allowed object is
       *     {@link String }
       *
       */
      public void setRefId(String value) {
        this.refId = value;
      }

      /**
       * Gets the value of the width property.
       *
       * @return
       *     possible object is
       *     {@link String }
       *
       */
      public String getWidth() {
        return width;
      }

      /**
       * Sets the value of the width property.
       *
       * @param value
       *     allowed object is
       *     {@link String }
       *
       */
      public void setWidth(String value) {
        this.width = value;
      }

    }

  }


  /**
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence&gt;
   *         &lt;element name="taskflow"&gt;
   *           &lt;complexType&gt;
   *             &lt;complexContent&gt;
   *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *                 &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *                 &lt;attribute name="height" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *               &lt;/restriction&gt;
   *             &lt;/complexContent&gt;
   *           &lt;/complexType&gt;
   *         &lt;/element&gt;
   *       &lt;/sequence&gt;
   *       &lt;attribute name="rendered" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="", propOrder = { "taskflow" })
  public static class ToolbarArea {

    @XmlElement(required=true)
    protected ModuleType.ToolbarArea.Taskflow taskflow;
    @XmlAttribute
    protected String rendered;

    /**
     * Gets the value of the taskflow property.
     *
     * @return
     *     possible object is
     *     {@link ModuleType.ToolbarArea.Taskflow }
     *
     */
    public ModuleType.ToolbarArea.Taskflow getTaskflow() {
      return taskflow;
    }

    /**
     * Sets the value of the taskflow property.
     *
     * @param value
     *     allowed object is
     *     {@link ModuleType.ToolbarArea.Taskflow }
     *
     */
    public void setTaskflow(ModuleType.ToolbarArea.Taskflow value) {
      this.taskflow = value;
    }

    /**
     * Gets the value of the rendered property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRendered() {
      return rendered;
    }

    /**
     * Sets the value of the rendered property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRendered(String value) {
      this.rendered = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;attribute name="height" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name="")
    public static class Taskflow {

      @XmlAttribute(required=true)
      protected String refId;
      @XmlAttribute(required=true)
      protected String height;

      /**
       * Gets the value of the refId property.
       *
       * @return
       *     possible object is
       *     {@link String }
       *
       */
      public String getRefId() {
        return refId;
      }

      /**
       * Sets the value of the refId property.
       *
       * @param value
       *     allowed object is
       *     {@link String }
       *
       */
      public void setRefId(String value) {
        this.refId = value;
      }

      /**
       * Gets the value of the height property.
       *
       * @return
       *     possible object is
       *     {@link String }
       *
       */
      public String getHeight() {
        return height;
      }

      /**
       * Sets the value of the height property.
       *
       * @param value
       *     allowed object is
       *     {@link String }
       *
       */
      public void setHeight(String value) {
        this.height = value;
      }

    }

  }

}
