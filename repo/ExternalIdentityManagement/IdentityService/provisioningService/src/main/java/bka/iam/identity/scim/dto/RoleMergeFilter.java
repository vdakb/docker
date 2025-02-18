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

    File        :   RoleMergeFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoleMergeFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.dto;

import java.util.List;

import java.util.stream.Collectors;

import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.hst.platform.rest.ServiceBundle;
import oracle.hst.platform.rest.ServiceError;

import oracle.hst.platform.jpa.PersistenceException;

import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.NotFoundException;
import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ProcessingException;

import oracle.iam.platform.scim.entity.Or;
import oracle.iam.platform.scim.entity.Not;
import oracle.iam.platform.scim.entity.And;
import oracle.iam.platform.scim.entity.Filter;
import oracle.iam.platform.scim.entity.Equals;
import oracle.iam.platform.scim.entity.Presence;
import oracle.iam.platform.scim.entity.Contains;
import oracle.iam.platform.scim.entity.EndsWith;
import oracle.iam.platform.scim.entity.LessThan;
import oracle.iam.platform.scim.entity.StartsWith;
import oracle.iam.platform.scim.entity.GreaterThan;
import oracle.iam.platform.scim.entity.ComplexFilter;
import oracle.iam.platform.scim.entity.LessThanOrEqual;
import oracle.iam.platform.scim.entity.GreaterThanOrEqual;

import oracle.iam.platform.scim.request.PatchOperation;

import oracle.iam.platform.scim.v2.GroupResource;

import bka.iam.identity.igs.model.Role;
import bka.iam.identity.igs.model.User;
import bka.iam.identity.igs.model.UserRole;

import bka.iam.identity.igs.api.UserFacade;

import oracle.hst.platform.core.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class RoleMergeFilter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A class that add/replace/remove attributes at {@link Role} entity
 ** representations from JAX-RS {@link PatchOperation}s or {@link GroupResource}
 ** representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleMergeFilter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Role       current;
  private final UserFacade facade;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Member
  // ~~~~~ ~~~~~~
  /**
   ** A small wrapper to resolve members to add to a role.
   */
  public static class Member {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty(value="value", required=true)
    private Long   value;
    @JsonProperty(value="type")
    private String type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Member</code> SCIM Resource that allows use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Member() {
 	    // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Visitor
  // ~~~~~ ~~~~~~~
  /**
   ** A filter evaluator applicable on {@link UserRole}.
   */
  public static class Visitor implements Filter.Visitor<Boolean, UserRole> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The one and only instance of the <code>Evaluator</code>
     ** <p>
     ** Singleton Pattern
     */
    private static final Visitor instance = new Visitor();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Visitor</code> the will detect.
     */
    private Visitor() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits an <code>and</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For the purposes of matching, an empty sub-filters should always
     ** evaluate to <code>true</code>.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link And}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final And filter, final UserRole entity)
      throws ProcessingException {

      for (Filter cursor : filter.filter()) {
        if (!cursor.accept(this, entity)) {
          return false;
        }
      }
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits an <code>or</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For the purposes of matching, an empty sub-filters should always
     ** evaluate to <code>false</code>.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Or}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Or filter, final UserRole entity)
      throws ProcessingException {

      for (Filter cursor : filter.filter()) {
        if (cursor.accept(this, entity)) {
          return true;
        }
      }
      return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>not</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Not}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Not filter, final UserRole entity)
      throws ProcessingException {

      return !filter.filter().accept(this, entity);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>present</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Presence}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Role}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Presence filter, final UserRole entity)
      throws ProcessingException {

      // draft-ietf-scim-core-schema section 2.5 states "Unassigned attributes,
      // the null value, or empty array (in the case of a multi-valued
      // attribute) SHALL be considered to be equivalent in "state"
      return entity.getUser() != null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits an <code>equality</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Equals}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Equals filter, final UserRole entity)
      throws ProcessingException {

      if (filter.value().isNull() && entity.getUser() != null) {
        // draft-ietf-scim-core-schema section 2.4 states "Unassigned attributes,
        // the null value, or empty array (in the case of a multi-valued
        // attribute) SHALL be considered to be equivalent in "state"
        return false;
      }
      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      return filter.value().asLong() == entity.getUser().getId().longValue();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>greater than</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link GreaterThan}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final GreaterThan filter, final UserRole entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      return entity.getUser().getId().longValue() > filter.value().asLong();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>greater than or equal to</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link GreaterThanOrEqual}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final GreaterThanOrEqual filter, final UserRole entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      return entity.getUser().getId().longValue() >= filter.value().asLong();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>less than</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link LessThan}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final LessThan filter, final UserRole entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      return filter.value().asLong() < entity.getUser().getId().longValue();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>less than or equal to</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link LessThanOrEqual}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Role}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final LessThanOrEqual filter, final UserRole entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      return entity.getUser().getId().longValue() <= filter.value().asLong();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>starts with</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link StartsWith}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final StartsWith filter, final UserRole entity)
      throws ProcessingException {

      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_TARGET_MATCH, "members", filter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>ends with</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link EndsWith}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final EndsWith filter, final UserRole entity)
      throws ProcessingException {

      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_TARGET_MATCH, "members", filter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>contains</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Contains}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Contains filter, final UserRole entity)
      throws ProcessingException {

      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_TARGET_MATCH, "members", filter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>complex</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link ComplexFilter}.
     ** @param  entity           a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final ComplexFilter filter, final UserRole entity)
      throws ProcessingException {

      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_TARGET_MATCH, "members", filter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionallity
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: evaluate
    /**
     ** Evaluate the provided filter against the provided {@link UserRole}.
     **
     ** @param  filter             the filter to evaluate.
     **                            <br>
     **                            Allowed object is {@link Filter}.
     ** @param  entity             the {@link UserRole} to evaluate the filter
     **                            against.
     **                            <br>
     **                            Allowed object is {@link UserRole}.
     **
     ** @return                    <code>true</code> if the {@link UserRole}
     **                            matches the filter or <code>false</code>
     **                            otherwise.
     **                            <br>
     **                            Possible object is <code>boolean</code>.
     **
     ** @throws ProcessingException if the filter is not valid for matching.
     */
    public static boolean evaluate(final Filter filter, final UserRole entity)
      throws ProcessingException {

      return filter.accept(instance, entity);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleMergeFilter</code> the will add/replace/remove
   ** attributes from the given entity.
   **
   ** @param  facade             the {@link UserFacade} to validate the users.
   **                            <br>
   **                            Allowed object is {@link UserFacade}.
   ** @param  entity             the {@link Role} entity to manipulate.
   **                            <br>
   **                            Allowed object is {@link Role}.
   */
  private RoleMergeFilter(final UserFacade facade, final Role entity) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.facade  = facade;
    this.current = entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Returns the manipulated entity.
   **
   ** @return                    the manipulated {@link Role} entity.
   **                            <br>
   **                            Possible object is {@link Role}.
   */
  public final Role entity() {
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>RoleMergeFilter</code> to manipulate the
   ** given JPA {@link Role} entity.
   **
   ** @param  facade             the {@link UserFacade} to validate the users.
   **                            <br>
   **                            Allowed object is {@link UserFacade}.
   ** @param  entity             the {@link Role} entity to manipulate.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @return                    the <code>RoleMergeFilter</code> created with
   **                            the specified {@link Role} entity attached.
   **                            <br>
   **                            Possible object is
   **                            <code>RoleMergeFilter</code>.
   */
  public static RoleMergeFilter build(final UserFacade facade, final Role entity) {
    return new RoleMergeFilter(facade, entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Merge a JAX-RS {@link GroupResource} representation into the JPA
   ** {@link Role} entity representation by transfering only those values that
   ** are not <code>null</code> in the JAX-RS representation.
   **
   ** @param  op                       the patch operation to apply.
   **                                  <br>
   **                                  Allowed object is {@link PatchOperation}.
   **
   ** @throws ProcessingException      if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  public void apply(final PatchOperation op)
    throws ProcessingException {

    // RFC 7644
    // if "path" is unspecified, the operation fails with HTTP status code 400
    // and a "scimType" error code of "noTarget".
    if (op.path() == null || op.path().size() == 0)
      throw BadRequestException.noTarget(ServiceBundle.string(ServiceError.PATCH_OPERATION_REMOVE_PATH));

    try {
      switch (op.type()) {
        case ADD     : applyAdd(op);
                       break;
        case REPLACE : applyReplace(op);
                       break;
        case REMOVE  : applyRemove(op);
                       break;
      }
    }
    catch (JsonProcessingException e) {
      throw BadRequestException.invalidValue(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge
  /**
   ** Merge a JAX-RS {@link GroupResource} representation into the JPA
   ** {@link Role} entity representation by transfering only those values that
   ** are not <code>null</code> in the JAX-RS representation.
   **
   ** @param  source             the JAX-RS {@link GroupResource} representation
   **                            providing the data to merge into
   **                            <code>origin</code>.
   **                            <br>
   **                            Allowed object is {@link GroupResource}.
   **
   ** @return                    the JPA {@link Role} entity representation
   **                            containing the merged data of
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Role}.
   */
  public final Role merge(final GroupResource source) {
    if (source.displayName() != null) {
      this.current.setDisplayName(source.displayName());
    }
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyAdd
  /**
   ** Add the attributes of a JAX-RS {@link GroupResource} representation
   ** to the JPA {@link Role} entity representation.
   ** <p>
   ** This method impelemnts partially behavior of
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2.1">RFC 7644 3.5.2.1. Add Operation</a>.
   ** <br>
   ** The operation performs the following functions, depending on the target
   ** location specified by "path":
   ** <ul>
   **   <li>The operation MUST contain a "value" member whose content specifies
   **       the value to be replaced. The value MAY be a quoted value, or it may
   **       be a JSON object containing the sub-attributes of the complex
   **       attribute specified in the operation's "path".
   **   <li>If the target location specifies a complex attribute, a set of
   **       sub-attributes SHALL be specified in the "value" parameter.
   **   <li>If the target location specifies a single-valued attribute, the
   **       existing value is replaced.
   **   <li>If the target location specifies a multi-valued attribute, a new
   **       value is added to the attribute.
   **   <li>If the target location specifies an attribute that does not exist
   **       (has no value), the attribute is added with the new value.
   **   <li>If the target location exists, the value is replaced.
   **   <li>If the target location already contains the value specified, no
   **       changes SHOULD be made to the resource, and a success response
   **       SHOULD be returned.  Unless other operations change the resource,
   **       this operation SHALL NOT change the modify timestamp of the
   **       resource.
   ** <ul>
   **
   ** @param  op                       the patch operation to apply.
   **                                  <br>
   **                                  Allowed object is {@link PatchOperation}.
   **
   ** @throws ProcessingException      if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  private void applyAdd(final PatchOperation op)
    throws ProcessingException
    ,      JsonProcessingException {

    if (op.node().isNull())
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_OPERATION_VALUE_NOTNULL));

    final RoleSchema.Attribute attribute = RoleSchema.instance.permitted(op.path());
    switch(attribute) {
      case NAME   : final String newValue = op.value(String.class);
                    final String oldValue = this.current.getDisplayName();
                    if (!StringUtility.equal(oldValue, newValue))
                      this.current.setDisplayName(newValue);
                    break;
      case MEMBER : assign(op.node());
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyReplace
  /**
   ** Replace the attributes of a JAX-RS {@link GroupResource} representation
   ** in the JPA {@link Role} entity representation.
   ** <p>
   ** This method impelemnts partially behavior of
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2.3">RFC 7644 3.5.2.3. Replace Operation</a>.
   ** <br>
   ** The operation performs the following functions, depending on the target
   ** location specified by "path":
   ** <ul>
   **   <li>The operation MUST contain a "value" member whose content specifies
   **       the value to be replaced. The value MAY be a quoted value, or it may
   **       be a JSON object containing the sub-attributes of the complex
   **       attribute specified in the operation's "path".
   **   <li>If the target location is a single-value attribute, the attributes
   **       value is replaced.
   **   <li>If the target location specifies a complex attribute, a set of
   **        sub-attributes SHALL be specified in the "value" parameter, which
   **        replaces any existing values or adds where an attribute did not
   **        previously exist.  Sub-attributes that are not specified in the
   **        "value" parameter are left unchanged.
   **   <li>If the target location is a multi-valued attribute and no filter is
   **       specified, the attribute and all values are removed, and the
   **       attribute SHALL be considered unassigned.
   ** </ul>
   **
   ** @param  op                       the patch operation to apply.
   **                                  <br>
   **                                  Allowed object is {@link PatchOperation}.
   **
   ** @throws ProcessingException      if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  private void applyReplace(final PatchOperation op)
    throws ProcessingException
    ,      JsonProcessingException {

    if (op.node().isNull())
      throw BadRequestException.noTarget(ServiceBundle.string(ServiceError.PATCH_OPERATION_VALUE_NOTNULL));

    final RoleSchema.Attribute attribute = RoleSchema.instance.permitted(op.path());
    switch(attribute) {
      case NAME   : this.current.setDisplayName(op.value(String.class));
                    break;
      case MEMBER : break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyRemove
  /**
   ** Removes the attributes of a JAX-RS {@link GroupResource} representation
   ** from the JPA {@link Role} entity representation.
   ** <p>
   ** This method impelemnts partially behavior of
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2.2">RFC 7644 3.5.2.2. Remove Operation</a>.
   ** <br>
   ** The operation performs the following functions, depending on the target
   ** location specified by "path":
   ** <ul>
   **   <li>If "path" is unspecified, the operation fails with HTTP status code
   **       400 and a "scimType" error code of "noTarget".
   **   <li>If the target location is a single-value attribute, the attribute
   **       and its associated value is removed, and the attribute SHALL be
   **       considered unassigned.
   ** </ul>
   **
   ** @param  op                       the patch operation to apply.
   **                                  <br>
   **                                  Allowed object is {@link PatchOperation}.
   **
   ** @throws ProcessingException      if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  private void applyRemove(final PatchOperation op)
    throws ProcessingException
    ,      JsonProcessingException {

    final RoleSchema.Attribute attribute = RoleSchema.instance.permitted(op.path());
    switch(attribute) {
      case NAME   : this.current.setDisplayName(null);
                    break;
      case MEMBER : revoke(op.path().element(0).filter());
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Assignes the users contained in the patch operation as member of the role.
   **
   ** @param  op                       the {@link JsonNode} providing the
   **                                  user values to assign to the role.
   **
   ** @throws ProcessingException      if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  private void assign(final JsonNode node)
    throws ProcessingException {

    if (!node.isArray())
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_OPERATION_VALUE_NOTNULL));

    final List<UserRole> target = this.current.getUser();
    try {
      for (int i = 0; i < node.size(); i++) {
        JsonNode value = node.get(i);
        if (!value.isObject())
          throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_OPERATION_ADD_OBJECT));

        final Member  member = Support.objectMapper().treeToValue(node.get(0), Member.class);
        if (member.type != null && !"User".equals(member.type))
          throw BadRequestException.invalidValue("Member must be of type [User].");

        final UserRole role = UserRole.build(lookup(member.value), this.current);
        // only add if its not already there
        if (!target.contains(role))
          target.add(role);
      }
    }
    catch (JsonProcessingException e) {
      throw BadRequestException.invalidValue(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revokes the users contained in the patch operation as member of the role.
   **
   ** @param  op                 the {@link Filter} providing the user values to
   **                            assign to the role.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @throws ProcessingException  if the filter evaluation fails.
   */
  private void revoke(final Filter filter)
    throws ProcessingException {

    // RFC 7644 3.5.2.2. Remove Operation (3rd bullet)
    // If the target location is a multi-valued attribute and no filter is
    // specified, the attribute and all values are removed, and the attribute
    // SHALL be considered unassigned.
    if (filter == null)
      this.current.getUser().clear();
    else {
      final Predicate<UserRole> visitor = (entity) -> {
        try {
          return Visitor.evaluate(filter, entity);
        }
        catch (ProcessingException e) {
          throw new javax.ws.rs.ProcessingException(e.getLocalizedMessage());
        }
      };

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed.
      this.current.getUser().removeAll(this.current.getUser().stream().filter(visitor).collect(Collectors.toList()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to retrieve a certain {@link User} by its identifier from the
   ** persistence layer.
   **
   ** @param  id                 the identifier of the {@link User} to lookup.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link User} mapped at <code>id</code> at
   **                            the persistence layer.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws NotFoundException    if no user entity is mapped with identifier
   **                              <code>id</code> at the Service Provider or
   **                              the request fails in general.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  private User lookup(final Long id)
    throws NotFoundException
    ,      PersistenceException {

    final User entity = this.facade.lookup(id);
    if (entity == null)
      throw NotFoundException.of(ResourceContext.RESOURCE_TYPE_USER, id);

    return entity;
  }
}