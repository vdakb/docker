/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Provisioning

    File        :   RoleSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoleSchema.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.dto;

import java.util.Map;
import java.util.TreeMap;

import oracle.hst.platform.core.function.CheckedFunction;

import oracle.hst.platform.jpa.Identifiable;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.BadRequestException;

import oracle.iam.platform.scim.entity.Path;

import bka.iam.identity.igs.model.Role;

import bka.iam.identity.jpa.provider.Base;

////////////////////////////////////////////////////////////////////////////////
// final class RoleSchema
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** This represents the mapping between the SCIM schemas representing groups
 ** and the persistence layer.
 ** <p>
 ** <b>Note</b>
 ** <br>
 ** RFC 7643 [<a href="https://datatracker.ietf.org/doc/html/rfc7643#section-2.1">Section 2.1</a>
 ** Attribute names are case insensitive and are often "camel-cased"
 ** (e.g., "camelCase").
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class RoleSchema implements CheckedFunction<Path, String, ProcessingException> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The one and only instance of the <code>TenantSchema</code>
   ** <p>
   ** Singleton Pattern
   */
  public static final RoleSchema              instance    = new RoleSchema();

  /**
   ** The collection of outbound attributes that are permitted as a subject of
   ** change.
   */
  private static final Map<String, Attribute> operational = operational();

  /**
   ** The collection of outbound attributes that are operational attributes.
   */
  private static final Map<String, Attribute> permitted   = permitted();

  /**
   ** The collection of all outbound attributes that can be placed in a search
   ** filter or sort control.
   */
  private static final Map<String, String>   searchable   = searchable();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** Attribute descriptor of the database entity.
   */
  public enum Attribute {
      ID     ("id",                Identifiable.ID)
    , VERSION("meta.version",      Base.Attribute.VERSION.id)
    , CREATED("meta.created",      Base.Attribute.CREATEDON.id)
    , UPDATED("meta.lastModified", Base.Attribute.UPDATEDON.id)
    , ACTIVE ("active",            Role.Attribute.ACTIVE.id)
    , NAME   ("displayName",       Role.Attribute.DISPLAYNAME.id)
    , MEMBER ("members",           "members")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String inbound;
    public final String outbound;

     ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Attribute(final String outbound, final String inbound) {
      // initailize instance attributes
      this.inbound  = inbound;
      this.outbound = outbound;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleSchema</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private RoleSchema() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operational
  /**
   ** Returns an {@link Attribute} that is permitted to be a operational
   ** attribute.
   **
   ** @param  name               the name of the desired {@link Attribute}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Attribute} mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public final Attribute operational(final String name) {
    return operational.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permitted
  /**
   ** Verifies that the {@link Path} references a permitted attribute of an
   ** entity.
   **
   ** @param  path               the {@link Path} of the desired
   **                            {@link Attribute}.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the permitted entity attribute.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   **
   ** @throws BadRequestException if {@link Path} references an attribute that
   **                             is not a subject of change.
   */
  public final Attribute permitted(final Path path)
    throws BadRequestException {

    // any path in a patch operation belongs to one path therefore the operation
    // contains exactly one and only one element
    final Attribute attribute = permitted(path.normalized());
    // verify if the path is valid
    if (attribute == null)
      throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_TOKEN, path.toString()));

    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permitted
  /**
   ** Returns an {@link Attribute} that is permitted to be a subject of change.
   **
   ** @param  name               the name of the desired {@link Attribute}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Attribute} mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public final Attribute permitted(final String name) {
    return permitted.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (CheckedFunction)
  /**
   ** Returns an internal attribute name that is searchable.
   **
   ** @param  path               the {@link Path} of the desired
   **                            {@link Attribute}.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the internal attribute name mapped at
   **                            <code>path</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ProcessingException if the specified {@link Path} did not yield an
   **                             attribute that could be operated on.
   */
  @Override
  public final String apply(final Path path)
    throws ProcessingException {

    final String attribute = searchable.get(path.normalized());
    // validate if the attribute is valid
    if (attribute == null)
      throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_TOKEN, path.normalized()));

    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Returns an internal attribute name that is searchable.
   ** <br>
   ** <p>
   ** Convenience method to shortens the access to the attribute avoiding
   ** creation of a {@link Path} argument.
   **
   ** @param  path               the path representation of the desired
   **                            {@link Attribute}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the internal attribute name mapped at
   **                            <code>path</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ProcessingException if the specified path representation did not
   **                             yield an attribute that could be operated on.
   */
  public final String apply(final String path)
    throws ProcessingException {

    return apply(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operational
  /**
   ** Initialize the static attributes and cover each extension.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** RFC 7643 Section 2.1  Attribute names are case insensitive
   */
  private static synchronized Map<String, Attribute> operational() {
    final Map<String, Attribute> mapping = new TreeMap<String, Attribute>(String.CASE_INSENSITIVE_ORDER);
    mapping.put(Attribute.VERSION.outbound, Attribute.VERSION);
    mapping.put(Attribute.CREATED.outbound, Attribute.CREATED);
    mapping.put(Attribute.UPDATED.outbound, Attribute.UPDATED);
    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permitted
  /**
   ** Initialize the static attributes and cover each extension.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** RFC 7643 Section 2.1  Attribute names are case insensitive
   */
  private static synchronized Map<String, Attribute> permitted() {
    final Map<String, Attribute> mapping = new TreeMap<String, Attribute>(String.CASE_INSENSITIVE_ORDER);
    mapping.put(Attribute.ID.outbound,      Attribute.ID);
    mapping.put(Attribute.NAME.outbound,    Attribute.NAME);
    mapping.put(Attribute.MEMBER.outbound,  Attribute.MEMBER);
    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchable
  /**
   ** Initialize the static attributes and cover each extension.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** RFC 7643 Section 2.1  Attribute names are case insensitive
   */
  private static synchronized Map<String, String> searchable() {
    final Map<String, String> mapping = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    mapping.put(Attribute.ID.outbound,      Attribute.ID.inbound);
    mapping.put(Attribute.VERSION.outbound, Attribute.VERSION.inbound);
    mapping.put(Attribute.CREATED.outbound, Attribute.CREATED.inbound);
    mapping.put(Attribute.UPDATED.outbound, Attribute.UPDATED.inbound);
    mapping.put(Attribute.NAME.outbound,    Attribute.NAME.inbound);
    mapping.put(Attribute.MEMBER.outbound,  Attribute.MEMBER.inbound);
    return mapping;
  }
}