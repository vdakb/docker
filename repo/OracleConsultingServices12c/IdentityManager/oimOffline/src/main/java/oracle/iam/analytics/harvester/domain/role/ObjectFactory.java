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

    File        :   ObjectFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ObjectFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.domain.role;

import javax.xml.bind.JAXBElement;

import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlElementDecl;

////////////////////////////////////////////////////////////////////////////////
// class ObjectFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java content interface and
 ** Java element interface generated in the
 ** oracle.iam.analytics.harvester.domain.role package.
 ** <p>
 ** An ObjectFactory allows you to programatically construct new instances of
 ** the Java representation for XML content. The Java representation of XML
 ** content can consist of schema derived interfaces and classes representing
 ** the binding of schema type definitions, element declarations and model
 ** groups. Factory methods for each of these are provided in this class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlRegistry
public class ObjectFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String NAMESPACE = "http://service.api.oia.iam.ots/role";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectFactory</code> that can be used to create new
   ** instances of schema derived classes for package:
   ** <code>oracle.iam.analytics.harvester.domain</code>
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
  // Method:   createContext
  /**
   ** Create an instance of {@link Context}
   **
   ** @return                    an instance of {@link Context}.
   */
  public Context createContext() {
    return new Context();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContext
  /**
   ** Create an instance of {@link Context} with the specified properties.
   **
   ** @param  uniqueName         the unique name strategy.
   ** @param  relationShip       the policy build strategy.
   ** @param  exactMatch         the exact match strategy.
   ** @param  validateOnly       <code>true</code> if the data should only be
   **                            validated by the request; otherwise
   **                            <code>false</code>.
   ** @param  ignoreWarnings     <code>true</code> if any warning detected by
   **                            the validation of the request should be
   **                            ignored; otherwise <code>false</code>.
   **
   ** @return                    the {@link Context} populated with the
   **                            specified properties.
   */
  public Context createContext(final String uniqueName, final String relationShip, final String exactMatch, final boolean validateOnly, final boolean ignoreWarnings) {
    return new Context(uniqueName, relationShip, exactMatch, validateOnly, ignoreWarnings);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContext
  /**
   ** Create an instance of {@link Context} with the specified properties.
   **
   ** @param  uniqueName         the unique name strategy.
   ** @param  relationShip       the policy build strategy.
   ** @param  exactMatch         the exact match strategy.
   ** @param  validateOnly       <code>true</code> if the data should only be
   **                            validated by the request; otherwise
   **                            <code>false</code>.
   ** @param  ignoreWarnings     <code>true</code> if any warning detected by
   **                            the validation of the request should be
   **                            ignored; otherwise <code>false</code>.
   **
   ** @return                    the {@link Context} populated with the
   **                            specified properties.
   */
  public Context createContext(final Unique uniqueName, final RelationShip relationShip, final Match exactMatch, final boolean validateOnly, final boolean ignoreWarnings) {
    return new Context(uniqueName, relationShip, exactMatch, validateOnly, ignoreWarnings);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContext
  /**
   ** Create an instance of {@link JAXBElement}{@code}&lt;{@link Context}&gt;{@code}}.
   **
   ** @param  value               the {@link Context} wrap in a
   **                            {@link JAXBElement}.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&lt;{@link Context}&gt;{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Context.LOCAL)
  public JAXBElement<Context> createContext(final Context value) {
    return new JAXBElement<Context>(Context.PORT, Context.class, null, value);
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
   ** Create an instance of {@link Entity} with the specified identifier
   ** <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Entity}.
   **
   ** @return                    an instance of {@link Entity}.
   */
  public Entity createEntity(final String id) {
    return new Entity(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRole
  /**
   ** Create an instance of {@link Role}
   **
   ** @return                    an instance of {@link Role}.
   */
  public Role createRole() {
    return new Role();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRole
  /**
   ** Create an instance of {@link Role} with the specified identifier
   ** <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Role}.
   **
   ** @return                    an instance of {@link Role}.
   */
  public Role createRole(final String id) {
    return new Role(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRole
  /**
   ** Create an instance of {@link JAXBElement}{@code}&lt;{@link Role}&gt;{@code}}.
   **
   ** @param  value               the {@link Role} wrap in a
   **                            {@link JAXBElement}.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&lt;{@link Role}&gt;{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Role.LOCAL)
  public JAXBElement<Role> createRole(final Role value) {
    return new JAXBElement<Role>(Role.PORT, Role.class, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRoleType
  /**
   ** Factory method to create a proper type from the given string value.
   **
   ** @param  value              the string value the type should be returned
   **                            for.
   **
   ** @return                    the type.
   */
  public Role.Type createRoleType(final String value) {
    return Role.Type.from(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRoleType
  /**
   ** Factory method to create a proper type from the given Long value.
   **
   ** @param  value              the string value the type should be returned
   **                            for.
   **
   ** @return                    the type.
   */
  public Role.Type createRoleType(final Long value) {
    return Role.Type.from(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRoleRisk
  /**
   ** Factory method to create a proper risk from the given string value.
   **
   ** @param  value              the string value the risk should be returned
   **                            for.
   **
   ** @return                    the risk.
   */
  public Role.Risk createRoleRisk(final String value) {
    return Role.Risk.from(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRoleRisk
  /**
   ** Factory method to create a proper risk from the given Integer value.
   **
   ** @param  value              the string value the risk should be returned
   **                            for.
   **
   ** @return                    the risk.
   */
  public Role.Risk createRoleRisk(final Integer value) {
    return Role.Risk.from(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicy
  /**
   ** Create an instance of {@link Policy}
   **
   ** @return                    an instance of {@link Policy}.
   */
  public Policy createPolicy() {
    return new Policy();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicy
  /**
   ** Create an instance of {@link Policy} with the specified identifier
   ** <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Policy}.
   **
   ** @return                    an instance of {@link Policy}.
   */
  public Policy createPolicy(final String id) {
    return new Policy(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicy
  /**
   ** Create an instance of {@link JAXBElement}{@code}&gt;{@link Policy}&gt;{@code}}.
   **
   ** @param  value               the {@link Policy} wrap in a
   **                            {@link JAXBElement}.
   **
   ** @return                    an instance of {@link JAXBElement}
   **                            {@code}&gt;{@link Policy}&gt;{@code}}.
   */
  @XmlElementDecl(namespace=NAMESPACE, name=Policy.LOCAL)
  public JAXBElement<Policy> createPolicy(final Policy value) {
    return new JAXBElement<Policy>(Policy.PORT, Policy.class, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyResource
  /**
   ** Create an instance of {@link Policy.Resource}.
   **
   ** @return                    an instance of {@link Policy.Resource}.
   */
  public Policy.Resource createPolicyResource() {
    return new Policy.Resource();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyResource
  /**
   ** Create an instance of {@link Policy.Resource} that refers to the specified
   ** namespace and endpoint.
   **
   ** @param  namespace          the name of the namespace of this
   **                            <code>Resource</code>.
   ** @param  endpoint           the name of the endpoint of this
   **                            <code>Resource</code>.
   **
   ** @return                    an instance of {@link Policy.Resource}.
   */
  public Policy.Resource createPolicyResource(final String namespace, final String endpoint) {
    return new Policy.Resource(namespace, endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyResourceEntitlements
  /**
   ** Create an instance of {@link Policy.Resource.Entitlements}.
   **
   ** @return                    an instance of
   **                            {@link Policy.Resource.Entitlements}.
   */
  public Policy.Resource.Entitlements createPolicyResourceEntitlements() {
    return new Policy.Resource.Entitlements();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyResourceEntitlements
  /**
   ** Create an instance of {@link Policy.Resource.Entitlements} with the
   ** specified identifier <code>id</code>.
   **
   ** @param  id                 the identifier of the
   **                            {@link Policy.Resource.Entitlements}.
   **
   ** @return                    an instance of
   **                            {@link Policy.Resource.Entitlements}.
   */
  public Policy.Resource.Entitlements createPolicyResourceEntitlements(final String id) {
    return new Policy.Resource.Entitlements(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyResourceEntitlementsEntitlement
  /**
   ** Create an instance of {@link Policy.Resource.Entitlements.Entitlement}
   **
   ** @return                    an instance of
   **                            {@link Policy.Resource.Entitlements.Entitlement}.
   */
  public Policy.Resource.Entitlements.Entitlement createPolicyResourceEntitlementsEntitlement() {
    return new Policy.Resource.Entitlements.Entitlement();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyResourceEntitlementsEntitlement
  /**
   ** Create an instance of {@link Policy.Resource.Entitlements.Entitlement}
   ** with the specified identifier <code>id</code>.
   **
   ** @param  id                 the identifier of the
   **                            {@link Policy.Resource.Entitlements.Entitlement}.
   **
   ** @return                    an instance of
   **                            {@link Policy.Resource.Entitlements.Entitlement}.
   */
  public Policy.Resource.Entitlements.Entitlement createPolicyResourceEntitlementsEntitlement(final String id) {
    return new Policy.Resource.Entitlements.Entitlement(id);
  }
}