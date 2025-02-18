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

    File        :   TenantMergeFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TenantMergeFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.dto;

import java.util.List;

import java.util.stream.Collectors;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.hst.platform.core.utility.StringUtility;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.hst.platform.jpa.PersistenceException;

import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.NotFoundException;
import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ProcessingException;

import oracle.iam.platform.scim.entity.Or;
import oracle.iam.platform.scim.entity.Not;
import oracle.iam.platform.scim.entity.And;
import oracle.iam.platform.scim.entity.Path;
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

import bka.iam.identity.scim.v2.TenantResource;

import bka.iam.identity.igs.model.User;
import bka.iam.identity.igs.model.Claim;
import bka.iam.identity.igs.model.Tenant;

import bka.iam.identity.igs.api.UserFacade;

////////////////////////////////////////////////////////////////////////////////
// class TenantMergeFilter
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A class that add/replace/remove attributes at {@link Tenant} entity
 ** representations from JAX-RS {@link PatchOperation}s or
 ** {@link TenantResource} representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TenantMergeFilter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Tenant     current;
  private final UserFacade facade;

  //////////////////////////////////////////////////////////////////////////////
  // class Visitor
  // ~~~~~ ~~~~~~~
  /**
   ** A filter evaluator applicable on {@link Claim}.
   */
  public static class Visitor implements Filter.Visitor<Boolean, Claim> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The one and only instance of the <code>Evaluator</code>
     ** <p>
     ** Singleton Pattern
     */
    private static final Visitor instance = new Visitor();
    
    private static final Path    value    = Path.build(null, "value");
    private static final Path    scope    = Path.build(null, "scope");

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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final And filter, final Claim entity)
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Or filter, final Claim entity)
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Not filter, final Claim entity)
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Presence filter, final Claim entity)
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Equals filter, final Claim entity)
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
      // The accepted filter attributes can be either value or scope hence we
      // have to figure out which on is ment
      if (value.equals(filter.path()))
        return filter.value().asLong() == entity.getUser().getId().longValue();
      else if (scope.equals(filter.path()))
        return StringUtility.equal(filter.value().asText(), entity.getRole());
      else
        return false;
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final GreaterThan filter, final Claim entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      // The accepted filter attributes can be either value or scope hence we
      // have to figure out which on is ment
      System.err.println(filter);
      if (value.equals(filter.path()))
        return entity.getUser().getId().longValue() > filter.value().asLong();
      else if (scope.equals(filter.path()))
        return StringUtility.compare(entity.getRole(), filter.value().asText()) > 0;
      else
        return false;
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final GreaterThanOrEqual filter, final Claim entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      // The accepted filter attributes can be either value or scope hence we
      // have to figure out which on is ment
      System.err.println(filter);
      if (value.equals(filter.path()))
        return entity.getUser().getId().longValue() >= filter.value().asLong();
      else if (scope.equals(filter.path()))
        return StringUtility.compare(entity.getRole(), filter.value().asText()) >= 0;
      else
        return false;
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final LessThan filter, final Claim entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      // The accepted filter attributes can be either value or scope hence we
      // have to figure out which on is ment
      System.err.println(filter);
      if (value.equals(filter.path()))
        return entity.getUser().getId().longValue() < filter.value().asLong();
      else if (scope.equals(filter.path()))
        return StringUtility.compare(entity.getRole(), filter.value().asText()) < 0;
      else
        return false;
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final LessThanOrEqual filter, final Claim entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      // The accepted filter attributes can be either value or scope hence we
      // have to figure out which on is ment
      if (value.equals(filter.path()))
        return entity.getUser().getId().longValue() <= filter.value().asLong();
      else if (scope.equals(filter.path()))
        return StringUtility.compare(entity.getRole(), filter.value().asText()) <= 0;
      else
        return false;
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final StartsWith filter, final Claim entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      // The type of filter can only applied on scope.
      if (scope.equals(filter.path()))
        return StringUtility.startsWith(entity.getRole(), filter.value().asText());
      else
        return false;
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final EndsWith filter, final Claim entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      // The type of filter can only applied on scope.
      if (scope.equals(filter.path()))
        return StringUtility.endsWith(entity.getRole(), filter.value().asText());
      else
        return false;
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final Contains filter, final Claim entity)
      throws ProcessingException {

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed. If no other values remain after removal of the selected
      // values, the multi-valued attribute SHALL be considered unassigned.
      // The type of filter can only applied on scope.
      if (scope.equals(filter.path()))
        return StringUtility.contains(entity.getRole(), filter.value().asText());
      else
        return false;
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
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if the filter is not applicable.
     */
    @Override
    public final Boolean apply(final ComplexFilter filter, final Claim entity)
      throws ProcessingException {

      // the filter attribute can be either value or scope hence we have to
      // figure out which on is ment
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_TARGET_MATCH, TenantResource.Role.PREFIX, filter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionallity
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: evaluate
    /**
     ** Evaluate the provided filter against the provided {@link Claim}.
     **
     ** @param  path               the {@link Path} with filter to evaluate.
     **                            <br>
     **                            Allowed object is {@link Path}.
     ** @param  entity             the {@link Claim} to evaluate the filter
     **                            against.
     **                            <br>
     **                            Allowed object is {@link Claim}.
     **
     ** @return                    <code>true</code> if the {@link Claim}
     **                            matches the filter or <code>false</code>
     **                            otherwise.
     **                            <br>
     **                            Possible object is <code>boolean</code>.
     **
     ** @throws ProcessingException if the filter is not valid for matching.
     */
    public static boolean evaluate(final Path path, final Claim entity)
      throws ProcessingException {

      for (Path.Element e : path) {
        // if one filter does not accept the criteria no entity attribute needs
        // to match further
        if (!e.filter().accept(instance, entity)) {
          return false;
        }
      }
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TenantMergeFilter</code> the will add/replace/remove
   ** attributes from the given entity.
   **
   ** @param  facade             the {@link UserFacade} to validate the users.
   **                            <br>
   **                            Allowed object is {@link UserFacade}.
   ** @param  entity             the {@link Tenant} entity to manipulate.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   */
  private TenantMergeFilter(final UserFacade facade, final Tenant entity) {
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
   ** @return                    the manipulated {@link Tenant} entity.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   */
  public final Tenant entity() {
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>TenantMergeFilter</code> to manipulate
   ** the given JPA {@link Tenant} entity.
   **
   ** @param  facade             the {@link UserFacade} to validate the users.
   **                            <br>
   **                            Allowed object is {@link UserFacade}.
   ** @param  entity             the {@link Tenant} entity to manipulate.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the <code>TenantMergeFilter</code> created with
   **                            the specified {@link Tenant} entity attached.
   **                            <br>
   **                            Possible object is
   **                            <code>TenantMergeFilter</code>.
   */
  public static TenantMergeFilter build(final UserFacade facade, final Tenant entity) {
    return new TenantMergeFilter(facade, entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Merge a JAX-RS {@link TenantResource} representation into the JPA
   ** {@link Tenant} entity representation by transfering only those values that
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
   ** Merge a JAX-RS {@link TenantResource} representation into the JPA
   ** {@link Tenant} entity representation by transfering only those values that
   ** are not <code>null</code> in the JAX-RS representation.
   **
   ** @param  source             the JAX-RS {@link TenantResource}
   **                            representation providing the data to merge into
   **                            <code>origin</code>.
   **                            <br>
   **                            Allowed object is {@link TenantResource}.
   **
   ** @return                    the JPA {@link Tenant} entity representation
   **                            containing the merged data of
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   */
  public final Tenant merge(final TenantResource source) {
    if (source.active() != null) {
      this.current.setActive(source.active());
    }
    if (source.displayName() != null) {
      this.current.setName(source.displayName());
    }
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyAdd
  /**
   ** Add the attributes of a JAX-RS {@link TenantResource} representation
   ** to the JPA {@link Tenant} entity representation.
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
      throw BadRequestException.noTarget(ServiceBundle.string(ServiceError.PATCH_OPERATION_VALUE_NOTNULL));

    String newValue = null;
    String oldValue = null;
    final TenantSchema.Attribute attribute = TenantSchema.instance.permitted(op.path());
    switch(attribute) {
      case ACTIVE : this.current.setActive(op.value(Boolean.class));
                    break;
      case NAME   : newValue = op.value(String.class);
                    oldValue = this.current.getName();
                    if (!StringUtility.equal(oldValue, newValue))
                      this.current.setName(op.value(String.class));
                    break;
      case ROLE   : assign(op.node());
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyReplace
  /**
   ** Replace the attributes of a JAX-RS {@link TenantResource} representation
   ** in the JPA {@link Tenant} entity representation.
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

    final TenantSchema.Attribute attribute = TenantSchema.instance.permitted(op.path());
    switch(attribute) {
      case ACTIVE : this.current.setActive(op.value(Boolean.class));
                    break;
      case NAME   : this.current.setName(op.value(String.class));
                    break;
      case ROLE   : // RFC 7644 3.5.2.3. Replace Operation (3rd bullet)
                    // If the target location is a multi-valued attribute and no
                    // filter is specified, the attribute and all values are
                    // replaced.
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyRemove
  /**
   ** Removes the attributes of a JAX-RS {@link TenantResource} representation
   ** from the JPA {@link Tenant} entity representation.
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

    final TenantSchema.Attribute attribute = TenantSchema.instance.permitted(op.path());
    switch(attribute) {
      case ACTIVE : this.current.setActive(null);
                    break;
      case NAME   : this.current.setName(null);
                    break;
      case ROLE   : revoke(op.path());
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Assignes the users contained in the patch operation as member of the role.
   **
   ** @param  op                 the {@link JsonNode} providing the values to
   **                            assign to the role.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @throws ProcessingException if the operation contains more than one
   **                             value, in which case, the values method
   **                             should be used to retrieve all values.
   */
  private void assign(final JsonNode node)
    throws ProcessingException {

    if (!node.isArray())
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_OPERATION_ADD_ARRAY));

    final List<Claim> target = this.current.getClaim();
    try {
      for (int i = 0; i < node.size(); i++) {
        JsonNode value = node.get(i);
        if (!value.isObject())
          throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_OPERATION_ADD_ARRAY));

        final TenantResource.Role member = Support.objectMapper().treeToValue(node.get(0), TenantResource.Role.class);
        if (!TenantResource.Role.USER.equals(member.type()))
          throw BadRequestException.invalidValue("Member must be of type [User].");
        
        final Claim claim = Claim.build(this.current, lookup(member.value()), member.scope());
        // only add if its not already there
        if (!target.contains(claim))
          target.add(claim);
      }
    }
    catch (JsonProcessingException e) {
      throw BadRequestException.invalidValue(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revokes the users contained in the patch operation as member of the
   ** tenant.
   **
   ** @param  path               the {@link Path} providing the values to
   **                            revoke to the role.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @throws ProcessingException if the operation contains more than one
   **                             value, in which case, the values method
   **                             should be used to retrieve all values.
   */
  private void revoke(final Path path)
    throws ProcessingException {

    // RFC 7644 3.5.2.2. Remove Operation (3rd bullet)
    // If the target location is a multi-valued attribute and no filter is
    // specified, the attribute and all values are removed, and the attribute
    // SHALL be considered unassigned.

    if (path == null || path.size() == 0)
      this.current.getClaim().clear();
    else {
      final Predicate<Claim> visitor = (entity) -> {
        try {
          return Visitor.evaluate(path, entity);
        }
        catch (ProcessingException e) {
          throw new javax.ws.rs.ProcessingException(e.getLocalizedMessage());
        }
      };

      // RFC 7644 3.5.2.2. Remove Operation (4th bullet)
      // If the target location is a multi-valued attribute and a complex filter
      // is specified comparing a "value", the values matched by the filter are
      // removed.
      this.current.getClaim().removeAll(this.current.getClaim().stream().filter(visitor).collect(Collectors.toList()));
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