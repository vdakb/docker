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

    File        :   UserMergeFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserMergeFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.dto;

import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.hst.platform.core.utility.StringUtility;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ProcessingException;

import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Filter;
import oracle.iam.platform.scim.entity.TypeEvaluator;

import oracle.iam.platform.scim.schema.Name;
import oracle.iam.platform.scim.schema.Email;
import oracle.iam.platform.scim.schema.PhoneNumber;

import oracle.iam.platform.scim.request.PatchOperation;

import oracle.iam.platform.scim.v2.UserResource;

import bka.iam.identity.igs.model.User;

////////////////////////////////////////////////////////////////////////////////
// class UserMergeFilter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A class that add/replace/remove attributes at {@link User} entity
 ** representations from JAX-RS {@link PatchOperation}s or {@link UserResource}
 ** representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UserMergeFilter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final User current;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserMergeFilter</code> the will add/replace/remove
   ** attributes from the given entity.
   **
   ** @param  entity             the {@link User} entity to manipulate.
   **                            <br>
   **                            Allowed object is {@link User}.
   */
  private UserMergeFilter(final User entity) {
    // ensure inheritance
    super();

    // initialize instance attributes
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
   ** @return                    the manipulated {@link User} entity.
   **                            <br>
   **                            Possible object is {@link User}.
   */
  public final User entity() {
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>UserMergeFilter</code> to manipulate the
   ** given JPA {@link User} entity.
   **
   ** @param  entity             the {@link User} entity to manipulate.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the <code>UserMergeFilter</code> created with
   **                            the specified {@link User} entity attached.
   **                            <br>
   **                            Possible object is
   **                            <code>UserMergeFilter</code>.
   */
  public static UserMergeFilter build(final User entity) {
    return new UserMergeFilter(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Merge a JAX-RS {@link UserResource} representation into the JPA
   ** {@link User} entity representation by transfering only those values that
   ** are not <code>null</code> in the JAX-RS representation.
   **
   ** @param  op                       the patch operation to apply.
   **                                  <br>
   **                                  Allowed object is {@link PatchOperation}.
   **
   ** @return                          the updated {@link User} resource.
   **                                  <br>
   **                                  Possible object is {@link User}.
   **
   ** @throws ProcessingException      if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  public User apply(final PatchOperation op)
    throws ProcessingException {

    try {
      switch (op.type()) {
        case ADD     : applyAdd(op);
                       break;
        case REPLACE : applyReplace(op);
                       break;
        case REMOVE  : applyRemove(op);
      }
    }
    catch (JsonProcessingException e) {
      throw BadRequestException.invalidValue(e.getLocalizedMessage());
    }
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge
  /**
   ** Merge a JAX-RS {@link UserResource} representation into the JPA
   ** {@link User} entity representation by transfering only those values that
   ** are not <code>null</code> in the JAX-RS representation.
   **
   ** @param  source             the JAX-RS {@link UserResource}
   **                            representation providing the data to merge into
   **                            <code>origin</code>.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @return                    the JPA {@link User} entity representation
   **                            containing the merged data of
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  public final User merge(final UserResource source)
    throws ProcessingException {

    if (source.userName() != null) {
      this.current.setUserName(source.userName());
    }
    if (source.active() != null) {
      this.current.setActive(source.active());
    }
    if (source.password() != null) {
      this.current.setCredential(source.password());
    }
    if (source.name() != null) {
      final Name name = source.name();
      if (name.familyName() != null) {
        this.current.setLastName(name.familyName());
      }
      if (name.givenName() != null) {
        this.current.setFirstName(name.givenName());
      }
    }
    if (source.email() != null) {
      mergeEmail(source.email());
    }
    if (source.phoneNumber() != null) {
      mergePhone(source.phoneNumber());
    }
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyAdd
  /**
   ** Add the attributes of a JAX-RS {@link UserResource} representation
   ** to the JPA {@link User} entity representation.
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
    final UserSchema.Attribute attribute = UserSchema.instance.permitted(op.path());
    switch(attribute) {
      case ACTIVE      : this.current.setActive(op.value(Boolean.class));
                         break;
      case USERNAME    : newValue = op.value(String.class);
                         oldValue = this.current.getUserName();
                         if (!StringUtility.equal(oldValue, newValue))
                         this.current.setUserName(op.value(String.class));
                         break;
      case CREDENTIAL : newValue = op.value(String.class);
                        oldValue = this.current.getCredential();
                        if (!StringUtility.equal(oldValue, newValue))
                          this.current.setCredential(op.value(String.class));
                        break;
      case FIRSTNAME   : newValue = op.value(String.class);
                         oldValue = this.current.getFirstName();
                         if (!StringUtility.equal(oldValue, newValue))
                           this.current.setFirstName(op.value(String.class));
                         break;
      case LASTNAME    : newValue = op.value(String.class);
                         oldValue = this.current.getLastName();
                         if (!StringUtility.equal(oldValue, newValue))
                           this.current.setLastName(op.value(String.class));
                         break;
      case LANGUAGE    : newValue = op.value(String.class);
                         oldValue = this.current.getLanguage();
                         if (!StringUtility.equal(oldValue, newValue))
                           this.current.setLanguage(op.value(String.class));
                         break;
      case EMAIL       : replaceEmail(op.path(), op.node());
                         break;
      case PHONE       : replacePhone(op.path());
                         break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyReplace
  /**
   ** Replace the attributes of a JAX-RS {@link UserResource} representation
   ** in the JPA {@link User} entity representation.
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

    final UserSchema.Attribute attribute = UserSchema.instance.permitted(op.path());
    switch(attribute) {
      case ACTIVE     : this.current.setActive(op.value(Boolean.class));
                        break;
      case USERNAME   : this.current.setUserName(op.value(String.class));
                        break;
      case CREDENTIAL : this.current.setCredential(op.value(String.class));
                        break;
      case FIRSTNAME  : this.current.setFirstName(op.value(String.class));
                        break;
      case LASTNAME   : this.current.setLastName(op.value(String.class));
                        break;
      case LANGUAGE   : this.current.setLanguage(op.value(String.class));
                        break;
      case EMAIL      : replaceEmail(op.path(), op.node());
                        break;
      case PHONE      : replacePhone(op.path());//, op.node());
                        break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyRemove
  /**
   ** Removes the attributes of a JAX-RS {@link UserResource} representation
   ** from the JPA {@link User} entity representation.
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

    final UserSchema.Attribute attribute = UserSchema.instance.permitted(op.path());
    switch(attribute) {
                         
      case ACTIVE     : this.current.setActive(null);
                        break;
      case USERNAME   : this.current.setUserName(null);
                        break;
      case CREDENTIAL : this.current.setCredential(null);
                        break;
      case FIRSTNAME  : this.current.setFirstName(null);
                        break;
      case LASTNAME   : this.current.setLastName(null);
                        break;
      case LANGUAGE   : this.current.setLanguage(null);
                        break;
      case EMAIL      : removeEmail(op.path());
                        break;
      case PHONE      : removePhoneNumber(op.path());
                        break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeEmail
  /**
   ** Merge the modify request for e-Mail Addresses.
   **
   ** @param  source             the e-Mail Addresses to modify.
   **                            <br>
   **                            Allowed object is {@link Colloection} where
   **                            each element is of type {@link Email}.
   **
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  private void mergeEmail(final Collection<Email> source)
    throws ProcessingException {

    final Filter filter = Filter.eq("type", Email.WORK);
    for (Email cursor : source) {
      if (TypeEvaluator.evaluate(filter, cursor.type())) {
        this.current.setEmail(cursor.value());
        break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergePhone
  /**
   ** Merge the modify request for phone numbers.
   **
   ** @param  source             the phone numbers to modify.
   **                            <br>
   **                            Allowed object is {@link Colloection} where
   **                            each element is of type {@link PhoneNumber}.
   **
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  private void mergePhone(final Collection<PhoneNumber> source)
    throws ProcessingException {

    final Filter work   = Filter.eq("type", PhoneNumber.WORK);
    final Filter mobile = Filter.eq("type", PhoneNumber.MOBILE);
    for (PhoneNumber cursor : source) {
      if (TypeEvaluator.evaluate(work, cursor.type())) {
        this.current.setPhone(cursor.value());
        break;
      }
      if (TypeEvaluator.evaluate(mobile, cursor.type())) {
        this.current.setMobile(cursor.value());
        break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceEmail
  /**
   ** Replace an e-Mail Addresses.
   **
   ** @param  path               the resource path of the e-Mail address to
   **                            replace.
   **                            <br>
   **                            Allowed object istype {@link Path}.
   ** @param  value              the json representation of the value to replace
   **                            the actual value with.
   **                            <br>
   **                            Allowed object istype {@link JsonNode}.
   **
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  private void replaceEmail(final Path path, final JsonNode value)
    throws ProcessingException {

    // RFC 7644
    // invalidPath
    // The specified filter syntax was invalid (does not comply with Figure 1),
    // or the specified attribute and filter comparison combination is not
    // supported. 
    // PATCH (Section 3.5.2)

    // invalidFilter
    // The "path" attribute was invalid or malformed (see Figure 7).
    // PATCH (Section 3.5.2)

    // noTarget
    // The specified "path" did not yield an attribute or attribute value that
    // could be operated on. This occurs when the specified "path" value
    // contains a filter that yields no match.
    // PATCH (Section 3.5.2)

    // if we not got a path the entire e-Mail Address have to be replaced if the
    // type of the value match "work"
    if (path.size() == 0)  {
      // RFC 7644
      // A required value was missing, or the value specified was not compatible
      // with the operation or attribute type (see Section 2.2 of [RFC7643]), or
      // resource (see Section 4 of [RFC7643]).
      if (!value.isObject())
        throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_OPERATION_REPLACE_VALUE));
      // check if we have an e-Mail object
      if (value.hasNonNull(Email.PREFIX)) {
        // TODO: more than one e-mail can be provided but we have only one
        // TypeEvaluator.evaluate(filter, Email.WORK)
      }
    }
    else {
      final Filter filter = path.element(0).filter();
      if (filter == null) {
        // RFC 7644
        // A required value was missing, or the value specified was not compatible
        // with the operation or attribute type (see Section 2.2 of [RFC7643]), or
        // resource (see Section 4 of [RFC7643]).
        if (!value.isTextual())
          throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_OPERATION_REPLACE_VALUE));
  
        this.current.setEmail(value.textValue());
      }
      else if(TypeEvaluator.evaluate(filter, Email.WORK))
        this.current.setEmail(value.textValue());
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   replacePhone
  /**
   ** Replace a phone number.
   **
   ** @param  path               the resource path of the phone number to
   **                            replace.
   **                            <br>
   **                            Allowed object istype {@link Path}.
   **
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  private void replacePhone(final Path path)
    throws ProcessingException {

    final Filter filter = path.element(0).filter();
    if (filter == null) {
      this.current.setPhone(filter.value().textValue());
      this.current.setMobile(filter.value().textValue());
    }
    else {
      if (TypeEvaluator.evaluate(filter, PhoneNumber.WORK))
        this.current.setPhone(filter.value().textValue());
      else if (TypeEvaluator.evaluate(filter, PhoneNumber.MOBILE))
        this.current.setMobile(filter.value().textValue());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeEmail
  /**
   ** Remove an e-Mail Addresses.
   **
   ** @param  path               the resource path of the e-Mail address to
   **                            remove.
   **                            <br>
   **                            Allowed object istype {@link Path}.
   **
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  private void removeEmail(final Path path)
    throws ProcessingException {

    final Filter filter = path.element(0).filter();
    if (filter == null) {
      this.current.setEmail(null);
    }
    else if(TypeEvaluator.evaluate(filter, Email.WORK))
      this.current.setEmail(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePhoneNumber
  /**
   ** Remove an phone number.
   **
   ** @param  path               the resource path of thephone number to remove.
   **                            <br>
   **                            Allowed object istype {@link Path}.
   */
  private void removePhoneNumber(final Path path)
    throws ProcessingException {

    final Filter filter = path.element(0).filter();
    if (filter == null) {
      this.current.setPhone(null);
      this.current.setMobile(null);
    }
    else {
      if (PhoneNumber.WORK.equals(filter.value().textValue())) {
        this.current.setPhone(null);
      }
      else if (PhoneNumber.MOBILE.equals(filter.value().textValue())) {
        this.current.setMobile(null);
      }
    }
  }
}