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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   ObjectFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ObjectFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-10-01  DSteding    First release version
*/

package oracle.iam.identity.offline.schema;

import javax.xml.bind.JAXBElement;

import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlElementDecl;

////////////////////////////////////////////////////////////////////////////////
// class ObjectFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java content interface and
 ** Java element interface generated in the oracle.iam.identity.offline
 ** package.
 ** <p>
 ** An ObjectFactory allows you to programatically construct new instances of
 ** the Java representation for XML content. The Java representation of XML
 ** content can consist of schema derived interfaces and classes representing
 ** the binding of schema type definitions, element declarations and model
 ** groups. Factory methods for each of these are provided in this class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
@XmlRegistry
public class ObjectFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String NAMESPACE = "http://xmlns.oracle.com/bpel/workflow/task";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectFactory</code> that can be used to create new
   ** instances of schema derived classes for package:
   ** <code>oracle.iam.identity.offline</code>
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ObjectFactory() {
    // ensure inheritance
    super();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPayload
  /**
   ** Create an instance of {@link Payload}.
   **
   ** @return                    an instance of {@link Payload}.
   */
  public Payload createPayload() {
    return new Payload();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPayload
  /**
   ** Create an instance of {@link JAXBElement}{@code}
   ** &lt;{@link Payload}&gt;{@code}}.
   **
   ** @param  value              the {@link Payload}.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&lt;{@link Payload}&gt;{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Payload.LOCAL)
  public JAXBElement<Payload> createPayload(final Payload value) {
    return new JAXBElement<Payload>(Payload.QNAME, Payload.class, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentity
  /**
   ** Create an instance of {@link Identity}.
   **
   ** @return                    an instance of {@link Identity}.
   */
  public Identity createIdentity() {
    return new Identity();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentity
  /**
   ** Create an instance of {@link JAXBElement}{@code}
   ** &lt;{@link Identity}&gt;{@code}}.
   **
   ** @param  value              the name of the identity object.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&lt;{@link Identity}&gt;{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Identity.LOCAL)
  public JAXBElement<Object> createIdentity(final Object value) {
    return new JAXBElement<Object>(Identity.QNAME, Object.class, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOperation
  /**
   ** Create an instance of {@link Operation}.
   **
   ** @return                    an instance of {@link Operation}.
   */
  public Operation createOperation() {
    return new Operation();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOperation
  /**
   ** Create an instance of {@link JAXBElement}{@code}
   ** &lt;{@link Operation}&gt;{@code}}.
   **
   ** @param  value              the name of the operation.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&lt;{@link Operation}&gt;{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Operation.LOCAL)
  public JAXBElement<Object> createOperation(final Object value) {
    return new JAXBElement<Object>(Operation.QNAME, Object.class, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCatalog
  /**
   ** Create an instance of {@link Catalog}.
   **
   ** @return                    an instance of {@link Catalog}.
   */
  public Catalog createCatalog() {
    return new Catalog();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCatalog
  /**
   ** Create an instance of {@link JAXBElement}{@code}
   ** &lt;{@link Catalog}&gt;{@code}}.
   **
   ** @param  value              the name of the catalog item.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&lt;{@link Catalog}c{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Catalog.LOCAL)
  public JAXBElement<Object> createCatalog(Object value) {
    return new JAXBElement<Object>(Catalog.QNAME, Object.class, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResource
  /**
   ** Create an instance of {@link Resource}.
   **
   ** @return                    an instance of {@link Resource}.
   */
  public Resource createResource() {
    return new Resource();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResource
  /**
   ** Create an instance of {@link JAXBElement}{@code}
   ** &lt;{@link Resource}c{@code}}.
   **
   ** @param  value              the name of the resource object.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&lt;{@link Resource}&gt;{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Resource.LOCAL)
  public JAXBElement<Object> createResource(final Object value) {
    return new JAXBElement<Object>(Resource.QNAME, Object.class, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntity
  /**
   ** Create an instance of {@link Entity}.
   **
   ** @return                    an instance of {@link Entity}.
   */
  public Entity createEntity() {
    return new Entity();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntity
  /**
   ** Create an instance of {@link Entity} with the provided data.
   **
   ** @return                    an instance of {@link Entity}.
   */

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntity
  /**
   ** Create an instance of {@link JAXBElement}{@code}
   ** &lt;{@link Entity}&gt;{@code}}.
   **
   ** @param  value              the name of the entity object.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&lt;{@link Entity}&gt;{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Entity.LOCAL)
  public JAXBElement<Object> createEntity(Object value) {
    return new JAXBElement<Object>(Entity.QNAME, Object.class, null, value);
  }
}