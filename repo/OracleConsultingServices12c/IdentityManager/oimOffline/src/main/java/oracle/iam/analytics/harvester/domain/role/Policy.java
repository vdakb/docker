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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Analytics Extension Library
    Subsystem   :   Offline Target Connector

    File        :   Policy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Policy.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.domain.role;

import java.util.List;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Policy
// ~~~~~ ~~~~~~
/**
 ** Java class for policy complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://service.api.oia.iam.ots}role"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="resource" maxOccurs="unbounded"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="ownerShip" type="{http://service.api.oia.iam.ots/base}token" maxOccurs="unbounded" minOccurs="0"/&gt;
 **                   &lt;element name="entitlements" maxOccurs="unbounded"&gt;
 **                     &lt;complexType&gt;
 **                       &lt;complexContent&gt;
 **                         &lt;extension base="{http://service.api.oia.iam.ots/role}entity"&gt;
 **                           &lt;sequence&gt;
 **                             &lt;element name="entitlement" maxOccurs="unbounded"&gt;
 **                               &lt;complexType&gt;
 **                                 &lt;complexContent&gt;
 **                                   &lt;extension base="{http://service.api.oia.iam.ots/role}entity"&gt;
 **                                     &lt;attribute name="ownerShip" type="{http://service.api.oia.iam.ots/base}token" /&gt;
 **                                   &lt;/extension&gt;
 **                                 &lt;/complexContent&gt;
 **                               &lt;/complexType&gt;
 **                             &lt;/element&gt;
 **                           &lt;/sequence&gt;
 **                         &lt;/extension&gt;
 **                       &lt;/complexContent&gt;
 **                     &lt;/complexType&gt;
 **                   &lt;/element&gt;
 **                 &lt;/sequence&gt;
 **                 &lt;attribute name="namespace" use="required" type="{http://service.api.oia.iam.ots/base}token"/&gt;
 **                 &lt;attribute name="endpoint"  use="required" type="{http://service.api.oia.iam.ots/base}token"/&gt;
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
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name=Policy.LOCAL, propOrder={Policy.Resource.LOCAL})
public class Policy extends Role {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String      LOCAL    = "policy";
  public static final QName       PORT     = new QName(ObjectFactory.NAMESPACE, LOCAL);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true, name=Policy.Resource.LOCAL, namespace=ObjectFactory.NAMESPACE, nillable=false)
  protected List<Policy.Resource> resource;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Resource
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
   **       &lt;sequence&gt;
   **         &lt;element name="entitlements" maxOccurs="unbounded"&gt;
   **           &lt;complexType&gt;
   **             &lt;complexContent&gt;
   **               &lt;extension base="{http://service.api.oia.iam.ots/role}entity"&gt;
   **                 &lt;sequence&gt;
   **                   &lt;element name="entitlement" type="{http://service.api.oia.iam.ots/role}entity" maxOccurs="unbounded"/&gt;
   **                 &lt;/sequence&gt;
   **               &lt;/extension&gt;
   **             &lt;/complexContent&gt;
   **           &lt;/complexType&gt;
   **         &lt;/element&gt;
   **       &lt;/sequence&gt;
   **       &lt;attribute name="namespace" use="required" type="{http://service.api.oia.iam.ots/base}token"/&gt;
   **       &lt;attribute name="endpoint"  use="required" type="{http://service.api.oia.iam.ots/base}token"/&gt;
   **     &lt;/restriction&gt;
   **   &lt;/complexContent&gt;
   ** &lt;/complexType&gt;
   ** </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name=Policy.Resource.LOCAL, propOrder={"ownerShip",Policy.Resource.Entitlements.LOCAL})
  public static class Resource {

    ////////////////////////////////////////////////////////////////////////////
    // static final
    ////////////////////////////////////////////////////////////////////////////

    static final String LOCAL = "resource";

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @XmlAttribute(required=true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String                             namespace;
    @XmlAttribute(required=true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String                             endpoint;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected List<String>                       ownerShip;
    @XmlElement(required=true, name=Policy.Resource.Entitlements.LOCAL, namespace=ObjectFactory.NAMESPACE, nillable=false)
    protected List<Policy.Resource.Entitlements> entitlements;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Entitlements
    // ~~~~~ ~~~~~~~~~~~~
    /**
     ** Java class for anonymous complex type.
     ** <p>
     ** The following schema fragment specifies the expected content contained
     ** within this class.
     ** <pre>
     ** &lt;complexType&gt;
     **   &lt;complexContent&gt;
     **     &lt;extension base="{http://service.api.oia.iam.ots/role}entity"&gt;
     **       &lt;sequence&gt;
     **         &lt;element name="entitlement" maxOccurs="unbounded"&gt;
     **           &lt;complexType&gt;
     **             &lt;complexContent&gt;
     **               &lt;extension base="{http://service.api.oia.iam.ots/role}entity"&gt;
     **                 &lt;attribute name="ownerShip" type="{http://service.api.oia.iam.ots/base}token" /&gt;
     **               &lt;/extension&gt;
     **             &lt;/complexContent&gt;
     **           &lt;/complexType&gt;
     **         &lt;/element&gt;
     **       &lt;/sequence&gt;
     **     &lt;/extension&gt;
     **   &lt;/complexContent&gt;
     ** &lt;/complexType&gt;
     ** </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name=Policy.Resource.Entitlements.LOCAL, propOrder={Policy.Resource.Entitlements.Entitlement.LOCAL})
    public static class Entitlements extends Entity {

      //////////////////////////////////////////////////////////////////////////
      // static final
      //////////////////////////////////////////////////////////////////////////

      static final String LOCAL = "entitlements";

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      @XmlElement(required=true, name=Policy.Resource.Entitlements.Entitlement.LOCAL, namespace=ObjectFactory.NAMESPACE, nillable=false)
      protected List<Entitlement> entitlement;

      //////////////////////////////////////////////////////////////////////////
      // Member classes
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // class Entitlement
      // ~~~~~ ~~~~~~~~~~~
      /**
       ** Java class for anonymous complex type.
       ** <p>
       ** The following schema fragment specifies the expected content contained within this class.
       **
       ** <pre>
       ** &lt;complexType&gt;
       **   &lt;complexContent&gt;
       **     &lt;extension base="{http://service.api.oia.iam.ots/role}entity"&gt;
       **       &lt;attribute name="ownerShip" type="{http://service.api.oia.iam.ots/base}token" /&gt;
       **     &lt;/extension&gt;
       **   &lt;/complexContent&gt;
       ** &lt;/complexType&gt;
       ** </pre>
       */
      @XmlType(name=Policy.Resource.Entitlements.Entitlement.LOCAL)
      @XmlAccessorType(XmlAccessType.FIELD)
      public static class Entitlement extends Entity {

        ////////////////////////////////////////////////////////////////////////
        // static final
        ////////////////////////////////////////////////////////////////////////

        static final String LOCAL = "entitlement";

        ////////////////////////////////////////////////////////////////////////
        // instance attributes
        ////////////////////////////////////////////////////////////////////////

        @XmlAttribute
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String ownerShip;

        ////////////////////////////////////////////////////////////////////////
        // Constructors
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: Ctor
        /**
         ** Constructs a empty <code>Entitlement</code> that allows use as a
         ** JavaBean.
         ** <p>
         ** Zero argument constructor required by the framework.
         ** <p>
         ** Default Constructor
         */
        public Entitlement() {
          // ensure inheritance
          super();
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: Ctor
        /**
         ** Constructs an <code>Entitlement</code> container with the specified
         ** id.
         **
         ** @param  id             the id of this <code>Entitlement</code>.
         */
        public Entitlement(final String id) {
          // ensure inheritance
          super(id);
        }

        ////////////////////////////////////////////////////////////////////////
        // Accessor and Mutator methods
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: setOwnerShip
        /**
         ** Sets the value of the ownerShip property.
         **
         ** @param  value        allowed object is {@link String}.
         */
        public void setOwnerShip(String value) {
          this.ownerShip = value;
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: getOwnerShip
        /**
         ** Returns the value of the ownerShip property.
         **
         ** @return              possible object is {@link String}.
         */
        public String getOwnerShip() {
          return this.ownerShip;
        }
      }

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a empty <code>Entitlements</code> that allows use as a
       ** JavaBean.
       ** <p>
       ** Zero argument constructor required by the framework.
       ** <p>
       ** Default Constructor
       */
      public Entitlements() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs an <code>Entitlements</code> container with the specified
       ** id.
       **
       ** @param  id             the id of this <code>Entitlements</code>
       **                        container.
       */
      public Entitlements(final String id) {
        // ensure inheritance
        super(id);
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor and Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: getEntitlement
      /**
       ** Returns the value of the entitlement property.
       ** <p>
       ** This accessor method returns a reference to the live list, not a
       ** snapshot. Therefore any modification you make to the returned list
       ** will be present inside the JAXB object.
       ** <br>
       ** This is why there is not a <code>set</code> method for the entitlement
       ** property.
       ** <pre>
       **    getEntitlement().add(newItem);
       ** </pre>
       ** <p>
       ** Objects of the following type(s) are allowed in the list
       ** {@link Entitlement}.
       **
       ** @return                the associated {@link List} of
       **                        {@link Entitlement} interpreted as
       **                        entitlements.
       **                        Returned value is never <code>null</code>.
       */
      public List<Entitlement> getEntitlement() {
        if (this.entitlement == null)
          this.entitlement = new ArrayList<Entitlement>();

        return this.entitlement;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Resource</code> that allows use as a JavaBean.
     ** <p>
     ** Zero argument constructor required by the framework.
     ** <p>
     ** Default Constructor
     */
    public Resource() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Resource</code> with the specified namespace and
     ** endpoint.
     **
     ** @param  namespace        the name of the namespace of this
     **                          <code>Resource</code>.
     ** @param  endpoint         the name of the endpoint of this
     **                          <code>Resource</code>.
     */
    public Resource(final String namespace, final String endpoint) {
      // ensure inheritance
      super();

      // ensure inheritance
      this.namespace = namespace;
      this.endpoint  = endpoint;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setNamespace
    /**
     ** Sets the value of the namespace property.
     **
     ** @param  value            allowed object is {@link String}.
     */
    public void setNamespace(String value) {
      this.namespace = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getNamespace
    /**
     ** Returns the value of the namespace property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String getNamespace() {
      return this.namespace;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setEndpoint
    /**
     ** Sets the value of the endpoint property.
     **
     ** @param  value            allowed object is {@link String}.
     */
    public void setEndpoint(String value) {
      this.endpoint = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getEndpoint
    /**
     ** Returns the value of the endpoint property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String getEndpoint() {
      return this.endpoint;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getOwnerShip
    /**
     ** Returns the value of the ownerShip property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the JAXB object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the entitlements
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **    getOwnerShip().add(newItem);
     ** </pre>
     ** <p>
     ** Objects of the following type(s) are allowed in the list {@link String}.
     **
     ** @return                  the associated {@link List} of {@link String}.
     **                          Returned value is never <code>null</code>.
     */
    public List<String> getOwnerShip() {
      if (this.ownerShip == null)
        this.ownerShip = new ArrayList<String>();

      return this.ownerShip;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getEntitlements
    /**
     ** Returns the value of the entitlements property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the JAXB object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the entitlements
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **    getEntitlements().add(newItem);
     ** </pre>
     ** <p>
     ** Objects of the following type(s) are allowed in the list
     ** {@link Policy.Resource.Entitlements}.
     **
     ** @return                  the associated {@link List} of
     **                          {@link Policy.Resource.Entitlements}.
     **                          Returned value is never <code>null</code>.
     */
    public List<Policy.Resource.Entitlements> getEntitlements() {
      if (this.entitlements == null)
        this.entitlements = new ArrayList<Policy.Resource.Entitlements>();

      return this.entitlements;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Policy</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Policy() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Policy</code> with the specified id.
   **
   ** @param  id                 the id of this <code>Entity</code>.
   */
  public Policy(final String id) {
    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResource
  /**
   ** Returns the value of the resource property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the resource
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getResource().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list
   ** {@link Policy.Resource}.
   **
   ** @return                    the value of the resource property.
   */
  public List<Policy.Resource> getResource() {
    if (this.resource == null)
      this.resource = new ArrayList<Policy.Resource>();

    return this.resource;
  }
}