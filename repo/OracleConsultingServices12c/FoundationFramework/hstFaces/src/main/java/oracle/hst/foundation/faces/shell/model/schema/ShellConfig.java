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

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class ShellConfig
// ~~~~~ ~~~~~~~~~~~
/**
 ** Java class for shell-config element declaration.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;element name="shell-config"&gt;
 **   &lt;complexType&gt;
 **     &lt;complexContent&gt;
 **       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **         &lt;sequence&gt;
 **           &lt;element name="taskflows"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence maxOccurs="unbounded"&gt;
 **                     &lt;element name="taskflow" type="{http://xmlns.oracle.com/hst/adf/shell}taskflow"/&gt;
 **                   &lt;/sequence&gt;
 **                 &lt;/restriction&gt;
 **               &lt;/complexContent&gt;
 **             &lt;/complexType&gt;
 **           &lt;/element&gt;
 **           &lt;element name="modules"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence&gt;
 **                     &lt;element name="module" type="{http://xmlns.oracle.com/hst/adf/shell}module" maxOccurs="unbounded"/&gt;
 **                   &lt;/sequence&gt;
 **                 &lt;/restriction&gt;
 **               &lt;/complexContent&gt;
 **             &lt;/complexType&gt;
 **           &lt;/element&gt;
 **           &lt;element name="consoles"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence maxOccurs="unbounded"&gt;
 **                     &lt;element name="console" type="{http://xmlns.oracle.com/hst/adf/shell}console"/&gt;
 **                   &lt;/sequence&gt;
 **                 &lt;/restriction&gt;
 **               &lt;/complexContent&gt;
 **             &lt;/complexType&gt;
 **           &lt;/element&gt;
 **         &lt;/sequence&gt;
 **         &lt;attribute name="resourceBundle"     type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;attribute name="locale"             type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;attribute name="detailAreaTabWidth" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **       &lt;/restriction&gt;
 **     &lt;/complexContent&gt;
 **   &lt;/complexType&gt;
 ** &lt;/element&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"taskflows", "modules", "consoles"})
@XmlRootElement(name=ShellConfig.LOCAL, namespace=ObjectFactory.NAMESPACE)
public class ShellConfig {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String LOCAL = "shell-config";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required = true)
  protected ShellConfig.Taskflows taskflows;
  @XmlElement(required = true)
  protected ShellConfig.Modules   modules;
  @XmlElement(required = true)
  protected ShellConfig.Consoles  consoles;
  @XmlAttribute
  protected String                resourceBundle;
  @XmlAttribute
  protected String                locale;
  @XmlAttribute
  protected Integer               detailAreaTabWidth;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Consoles
  // ~~~~~ ~~~~~~~~
  /**
   ** Java class for anonymous complex type.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   ** &lt;complexType&gt;
   **   &lt;complexContent&gt;
   **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   **       &lt;sequence maxOccurs="unbounded"&gt;
   **         &lt;element name="console" type="{http://xmlns.oracle.com/hst/adf/shell}console"/&gt;
   **       &lt;/sequence&gt;
   **     &lt;/restriction&gt;
   **   &lt;/complexContent&gt;
   ** &lt;/complexType&gt;
   ** </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="", propOrder={"console"})
  public static class Consoles {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @XmlElement(required=true)
    protected List<ConsoleType> console;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Consoles</code> that allows use as a JavaBean.
     ** <p>
     ** Zero argument constructor required by the framework.
     ** <p>
     ** Default Constructor
     */
    public Consoles() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getConsole
    /**
     ** Returns the value of the console property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the JAXB object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the custom
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **    getConsole().add(newItem);
     ** </pre>
     ** <p>
     ** Objects of the following type(s) are allowed in the list
     ** {@link ConsoleType}s.
     **
     ** @return                    the associated {@link List} of
     **                            {@link ConsoleType}s.
     **                            Returned value is never <code>null</code>.
     */
    public List<ConsoleType> getConsole() {
      if (this.console == null)
        this.console = new ArrayList<ConsoleType>();

      return this.console;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Modules
  // ~~~~~ ~~~~~~~
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
   **         &lt;element name="module" type="{http://xmlns.oracle.com/hst/adf/shell}module" maxOccurs="unbounded"/&gt;
   **       &lt;/sequence&gt;
   **     &lt;/restriction&gt;
   **   &lt;/complexContent&gt;
   ** &lt;/complexType&gt;
   ** </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="", propOrder={"module"})
  public static class Modules {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @XmlElement(required=true)
    protected List<ModuleType> module;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Modules</code> that allows use as a JavaBean.
     ** <p>
     ** Zero argument constructor required by the framework.
     ** <p>
     ** Default Constructor
     */
    public Modules() {
      // ensure inheritance
      super();
    }


    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getModule
    /**
     ** Returns the value of the module property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the JAXB object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the custom
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **    getModule().add(newItem);
     ** </pre>
     ** <p>
     ** Objects of the following type(s) are allowed in the list
     ** {@link ModuleType}s.
     **
     ** @return                    the associated {@link List} of
     **                            {@link ModuleType}s.
     **                            Returned value is never <code>null</code>.
     */
    public List<ModuleType> getModule() {
      if (this.module == null)
        this.module = new ArrayList<ModuleType>();

      return this.module;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Taskflows
  // ~~~~~ ~~~~~~~~~
  /**
   ** Java class for anonymous complex type.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   ** &lt;complexType&gt;
   **   &lt;complexContent&gt;
   **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   **       &lt;sequence maxOccurs="unbounded"&gt;
   **         &lt;element name="taskflow" type="{http://xmlns.oracle.com/hst/adf/shell}taskflow"/&gt;
   **       &lt;/sequence&gt;
   **     &lt;/restriction&gt;
   **   &lt;/complexContent&gt;
   ** &lt;/complexType&gt;
   ** </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="", propOrder={"taskflow"})
  public static class Taskflows {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @XmlElement(required=true)
    protected List<TaskFlowType> taskflow;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Taskflows</code> that allows use as a JavaBean.
     ** <p>
     ** Zero argument constructor required by the framework.
     ** <p>
     ** Default Constructor
     */
    public Taskflows() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTaskflow
    /**
     ** Returns the value of the taskflow property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the JAXB object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the custom
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **    getTaskflow().add(newItem);
     ** </pre>
     ** <p>
     ** Objects of the following type(s) are allowed in the list
     ** {@link TaskFlowType}s.
     **
     ** @return                    the associated {@link List} of
     **                            {@link TaskFlowType}s.
     **                            Returned value is never <code>null</code>.
     */
    public List<TaskFlowType> getTaskflow() {
      if (this.taskflow == null)
        this.taskflow = new ArrayList<TaskFlowType>();

      return this.taskflow;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ShellConfig</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ShellConfig() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResourceBundle
  /**
   ** Sets the value of the resourceBundle property.
   **
   ** @param  value              the value of the resourceBundle property.
   **                            Allowed object is {@link String}.
   */
  public void setResourceBundle(final String value) {
    this.resourceBundle = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceBundle
  /**
   ** Returns the value of the resourceBundle property.
   **
   ** @return                    the value of the resourceBundle property.
   **                            Possible object is {@link String}.
   */
  public String getResourceBundle() {
    return this.resourceBundle;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocale
  /**
   ** Sets the value of the locale property.
   **
   ** @param  value              the value of the locale property.
   **                            Allowed object is {@link String}.
   */
  public void setLocale(final String value) {
    this.locale = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocale
  /**
   ** Returns the value of the locale property.
   **
   ** @return                    the value of the locale property.
   **                            Possible object is {@link String}.
   */
  public String getLocale() {
    return this.locale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDetailAreaTabWidth
  /**
   ** Sets the value of the detailAreaTabWidth property.
   **
   ** @param  value              the value of the detailAreaTabWidth property.
   **                            Allowed object is {@link Integer}.
   */
  public void setDetailAreaTabWidth(final Integer value) {
    this.detailAreaTabWidth = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDetailAreaTabWidth
  /**
   ** Returns the value of the detailAreaTabWidth property.
   **
   ** @return                    the value of the detailAreaTabWidth property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getDetailAreaTabWidth() {
    return this.detailAreaTabWidth;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConsoles
  /**
   ** Sets the value of the consoles property.
   **
   ** @param  value              the value of the consoles property.
   **                            Allowed object is
   **                            {@link ShellConfig.Consoles}.
   */
  public void setConsoles(final ShellConfig.Consoles value) {
    this.consoles = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConsoles
  /**
   ** Returns the value of the consoles property.
   **
   ** @return                    the value of the consoles property.
   **                            Possible object is
   **                            {@link ShellConfig.Consoles}.
   */
  public ShellConfig.Consoles getConsoles() {
    return this.consoles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModules
  /**
   ** Sets the value of the modules property.
   **
   ** @param  value              the value of the modules property.
   **                            Allowed object is
   **                            {@link ShellConfig.Modules}.
   */
  public void setModules(final ShellConfig.Modules value) {
    this.modules = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModules
  /**
   ** Returns the value of the modules property.
   **
   ** @return                    the value of the modules property.
   **                            Possible object is
   **                            {@link ShellConfig.Modules}.
   */
  public ShellConfig.Modules getModules() {
    return this.modules;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTaskflows
  /**
   ** Sets the value of the taskflows property.
   **
   ** @param  value              the value of the taskflows property.
   **                            Allowed object is
   **                            {@link ShellConfig.Taskflows}.
   */
  public void setTaskflows(final ShellConfig.Taskflows value) {
    this.taskflows = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTaskflows
  /**
   ** Returns the value of the taskflows property.
   **
   ** @return                    the value of the taskflows property.
   **                            Possible object is
   **                            {@link ShellConfig.Taskflows}.
   */
  public ShellConfig.Taskflows getTaskflows() {
    return this.taskflows;
  }
}