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

    File        :   ConsoleType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConsoleType.


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
// class ConsoleType
// ~~~~~ ~~~~~~~~~~~
/**
 ** Java class for console complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="console"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://xmlns.oracle.com/hst/adf/shell}config-data"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="modules"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="module" maxOccurs="unbounded"&gt;
 **                     &lt;complexType&gt;
 **                       &lt;complexContent&gt;
 **                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                           &lt;attribute name="refId" type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
 **                         &lt;/restriction&gt;
 **                       &lt;/complexContent&gt;
 **                     &lt;/complexType&gt;
 **                   &lt;/element&gt;
 **                 &lt;/sequence&gt;
 **               &lt;/restriction&gt;
 **             &lt;/complexContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **       &lt;/sequence&gt;
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
@XmlType(name=ConsoleType.LOCAL, propOrder={"modules"})
public class ConsoleType extends ConfigData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String LOCAL = "console";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected Modules modules;

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
   **         &lt;element name="module" maxOccurs="unbounded"&gt;
   **           &lt;complexType&gt;
   **             &lt;complexContent&gt;
   **               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   **                 &lt;attribute name="refId" type="{http://www.w3.org/2001/XMLSchema}string" use="required"/&gt;
   **               &lt;/restriction&gt;
   **             &lt;/complexContent&gt;
   **           &lt;/complexType&gt;
   **         &lt;/element&gt;
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
    protected List<ConsoleType.Modules.Module> module;

    ////////////////////////////////////////////////////////////////////////////
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
     **       &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     **     &lt;/restriction&gt;
     **   &lt;/complexContent&gt;
     ** &lt;/complexType&gt;
     ** </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name="")
    public static class Module {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      @XmlAttribute(required=true)
      protected String refId;

      //////////////////////////////////////////////////////////////////////////
    // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a empty <code>Module</code> that allows use as a JavaBean.
       ** <p>
       ** Zero argument constructor required by the framework.
       ** <p>
       ** Default Constructor
       */
      public Module() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor and Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method:   setRefId
      /**
       ** Sets the value of the refId property.
       **
       ** @param  value          the value of the refId property.
       **                        Allowed object is {@link String}.
       */
      public void setRefId(final String value) {
        this.refId = value;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: getRefId
      /**
       ** Returns the value of the refId property.
       **
       ** @return                the value of the refId property.
       **                        Possible object is {@link String}.
       */
      public String getRefId() {
        return refId;
      }
    }

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
     ** This is why there is not a <code>set</code> method for the resource
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **    getRole().add(newItem);
     ** </pre>
     ** Objects of the following type(s) are allowed in the list
     ** {@link Modules.Module}.
     **
     ** @return                  the colection of modules belonging to the
     **                          console.
     */
    public List<ConsoleType.Modules.Module> getModule() {
      if (this.module == null)
        this.module = new ArrayList<ConsoleType.Modules.Module>();

      return this.module;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ConsoleType</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ConsoleType() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModules
  /**
   ** Sets the value of the modules property.
   **
   ** @param  value              the value of the modules property.
   **                            Allowed object is {@link ConsoleType.Modules}.
   */
  public void setModules(final Modules value) {
    this.modules = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModules
  /**
   ** Returns the value of the modules property.
   **
   ** @return                    the value of the modules property.
   **                            Possible object is {@link ConsoleType.Modules}.
   */
  public Modules getModules() {
    return this.modules;
  }
}