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
    Subsystem   :   Unique Identifier Service

    File        :   SurrogateSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SurrogateSchema.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid;

import javax.json.Json;
import javax.json.JsonObject;

import oracle.hst.platform.rest.BadRequestException;

import bka.iam.identity.igs.api.EndpointError;
import bka.iam.identity.igs.api.EndpointBundle;

import bka.iam.identity.uid.model.Type;
import bka.iam.identity.uid.model.State;
import bka.iam.identity.uid.model.Country;
import bka.iam.identity.uid.model.Participant;
import bka.iam.identity.uid.model.Surrogate;
import bka.iam.identity.uid.model.ParticipantType;

////////////////////////////////////////////////////////////////////////////////
// final class SurrogateSchema
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>SurrogateSchema</code> definition of an Unique Identifier.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class SurrogateSchema {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The minimum length a surrogate must have */
  public static final int             MIN   = 15;
  /** The maximum length a surrogate can have */
  public static final int             MAX   = 36;

  /** The character sequence to separate the segments of any identifier */
  public static final String          SEP   = "-";

  /** The pattern used to compose a full qualified tenant identifier */
  public static final String          TNT   = "%s-%s-%s-%s";

  /** The pattern used to compose a full qualified unique identifier */
  public static final String          FQN   = TNT + "-%s-%s";

  public static final String          UID   = "uid";
  /**
   ** The regular expression to validate if an argmunts contains only digits.
   */
  public static final String          DIGIT = "^[0-9]+$";

  /**
   ** The regular expression to validate if an argmunts contains only digits
   ** and lowercase letters.
   */
  public static final String          ALPHA = "^[0-9A-Za-z]+$";

  /** The segment specifiying the type of the participant. */
  public static final SurrogateSchema PTT   = build("ptt", 0,  1,  1, "participantType");
  /** The segment specifiying the country the participant belongs. */
  public static final SurrogateSchema CID   = build("cid", 1,  1,  3, "country");
  /** The segment specifiying the state the participant belongs. */
  public static final SurrogateSchema SID   = build("sid", 2,  1,  2, "state");
  /** The segment specifiying the identifier of a participant. */
  public static final SurrogateSchema PTS   = build("pts", 3,  2, 11, "participant");
  /** The segment specifiying the account type of the identifier to generate. */
  public static final SurrogateSchema TID   = build("tid", 4,  3,  3, "type");
  /** The segment specifiying the external ID of the identifier to generate. */
  public static final SurrogateSchema EID   = build("eid", 5,  5, 11, "externalId");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  public final String json;
  public final String name;
  public final int    index;
  public final int    minimum;
  public final int    maximum;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SurrogateSchema</code> with the property values
   ** specified.
   **
   ** @param  json               the attribute name in the json payload.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  index              the 0-based index of the segment.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  minimum            the minimum string length of the segment.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  maximum            the maximum string length of the segment.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  name               the name of attribute of a slice.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private SurrogateSchema(final String json, final int index, final int minimum, final int maximum, final String name) {
    // ensure inheritance
    super();

    // initailize instance attributes
    this.json    = json;
    this.name    = name;
    this.index   = index;
    this.minimum = minimum;
    this.maximum = maximum;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** Factory method to create a {@link Surrogate} resource from a
   ** {@link JsonObject} by concatenating the segments of the
   ** {@link JsonObject} in the correct order.
   **
   ** @param  resource           the {@link JsonObject}  providing the segments
   **                            to concatenate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  eid                the segment for an external identifier of a
   **                            surrogate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Surrogate} populated with the
   **                            given values.
   **                            <br>
   **                            Possible object is {@link Surrogate}.
   */
  public static Surrogate compose(final JsonObject resource, final String eid) {
    return Surrogate.build(tenant(resource), resource.getString(TID.json), eid);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Factory method to create a tenant identifier resource from a
   ** {@link JsonObject} by concatenating the required segments of the
   ** {@link JsonObject} in the correct order.
   **
   ** @param  value              the {@link JsonObject} providing the segments
   **                            to concatenate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the {@link String} representing the identifier
   **                            of a tenant.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String tenant(final JsonObject value) {
    return String.format(TNT, value.getString(PTT.json), value.getString(CID.json), value.getString(SID.json), value.getString(PTS.json));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   slice
  /**
   ** Factory method to create a {@link JsonObject} resource from a
   ** {@link String} by slicing the value accordingly to the schema definition.
   **
   ** @param  value              the identifier to slice.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link JsonObject} populated with the
   **                            given values.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject slice(final String value) {
    final String[] segment = value.split(SEP);
    return Json.createObjectBuilder()
      .add(PTT.json, segment[PTT.index])
      .add(CID.json, segment[CID.index])
      .add(SID.json, segment[SID.index])
      .add(PTS.json, segment[PTS.index])
      .add(TID.json, segment[TID.index])
      .add(EID.json, segment[EID.index])
    .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a {@link JsonObject} REST resource from a
   ** {@link Surrogate}.
   ** <br>
   ** A surrogate aggregates the identifier of:
   ** <ol>
   **   <li>Tenant (e.g. T-36-0-20}
   **   <li>Account Type (e.g. 101}
   **   <li>External Id (e.g. 4123457}
   ** </ol>
   ** All of the segments are concatenated in the.
   **
   ** @param  value          the {@link Surrogate} property of the
   **                            {@link JsonObject} to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link JsonObject} populated with the given
   **                            values.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject from(final Surrogate value) {
    return Json.createObjectBuilder().add(UID, String.format("%s-%s-%s", value.getTenant(), value.getType(), value.getExternal())).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validate if the given {@link JsonObject} <code>resource</code> satisfies
   ** the requirements to be a valid resource.
   **
   ** @param  resource           the {@link JsonObject} to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @throws BadRequestException if the rules of an UID are violated.
   */
  public static void validate(final JsonObject resource)
    throws BadRequestException {

    // drill down to the resource part if they are valid options
    // the validation is proceed in the order of the segments of an UID
    validateParticipantType(resource);
    validateCountry(resource);
    validateState(resource);
    validateParticipant(resource);
    validateAccountType(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the limits of a participant type.
   **
   ** @param  entity             the {@link ParticipantType} entity to validate.
   **                            <br>
   **                            Allowed object is {@link ParticipantType}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             participant type have been violated.
   */
  public static void validate(final ParticipantType entity)
    throws BadRequestException {

    validateParticipantType(entity.getId());
    validateRequired(ParticipantType.Attribute.NAME.id, entity.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateParticipantType
  /**
   ** Validates the existence and limits of a participant type segment of the
   ** given {@link JsonObject} resource.
   **
   ** @param  resource           the {@link JsonObject} resource to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             participant type have been violated.
   */
  public static void validateParticipantType(final JsonObject resource)
    throws BadRequestException {

    validateParticipantType(resource.getString(PTT.json, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateParticipantType
  /**
   ** Validates the existence and limits of a participant type segment of the
   ** given {@link JsonObject} resource.
   **
   ** @param  value              the string representation to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             participant type have been violated.
   */
  public static void validateParticipantType(final String value)
    throws BadRequestException {

    if (value == null)
      throw notNull(SurrogateSchema.PTT.name);
    
    if (value.length() < PTT.minimum || value.length() > PTT.maximum)
      throw invalidLength(SurrogateSchema.PTT.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the limits of a country.
   **
   ** @param  entity             the {@link Country} entity to validate.
   **                            <br>
   **                            Allowed object is {@link Country}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             country have been violated.
   */
  public static void validate(final Country entity)
    throws BadRequestException {

    validateCountry(entity.getId());
    validateRequired(Country.Attribute.NAME.id, entity.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateCountry
  /**
   ** Validates the existence and limits of a country segment of the given
   ** {@link JsonObject} resource.
   **
   ** @param  resource           the {@link JsonObject} resource to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             country have been violated.
   */
  public static void validateCountry(final JsonObject resource)
    throws BadRequestException {

    validateCountry(resource.getString(CID.json, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateCountry
  /**
   ** Validates the existence and limits of a country segment of the given
   ** {@link JsonObject} resource.
   **
   ** @param  value              the string representation to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             country have been violated.
   */
  public static void validateCountry(final String value)
    throws BadRequestException {

    if (value == null)
      throw notNull(SurrogateSchema.CID.name);
    
    if (value.length() < CID.minimum || value.length() > CID.maximum)
      throw invalidLength(SurrogateSchema.CID.name);

    if (!value.matches(DIGIT))
      throw invalidCharacter(SurrogateSchema.CID.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the limits of a state.
   **
   ** @param  entity             the {@link State} entity to validate.
   **                            <br>
   **                            Allowed object is {@link State}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             state have been violated.
   */
  public static void validate(final State entity)
    throws BadRequestException {

    validateState(entity.getId());
    validateRequired(State.Attribute.NAME.id, entity.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateState
  /**
   ** Validates the existence and limits of a state segment of the given
   ** {@link JsonObject} resource.
   **
   ** @param  resource           the {@link JsonObject} resource to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             state have been violated.
   */
  public static void validateState(final JsonObject resource)
    throws BadRequestException {

    validateState(resource.getString(SID.json, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateState
  /**
   ** Validates the existence and limits of a state segment of the given
   ** {@link JsonObject} resource.
   **
   ** @param  value              the string representation to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             state have been violated.
   */
  public static void validateState(final String value)
    throws BadRequestException {

    if (value == null)
      throw notNull(SurrogateSchema.SID.name);
    
    if (value.length() < SID.minimum || value.length() > SID.maximum)
      throw invalidLength(SurrogateSchema.SID.name);

    if (!value.matches(DIGIT))
      throw invalidCharacter(SurrogateSchema.SID.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the limits of a participant.
   **
   ** @param  entity             the {@link Participant} entity to validate.
   **                            <br>
   **                            Allowed object is {@link Participant}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             participant have been violated.
   */
  public static void validate(final Participant entity)
    throws BadRequestException {

    validateParticipant(entity.getId());
    validateRequired(Participant.Attribute.NAME.id, entity.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateParticipant
  /**
   ** Validates the existence and limits a participant segment of the given
   ** {@link JsonObject} resource.
   **
   ** @param  resource           the {@link JsonObject} resource to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             participant have been violated.
   */
  public static void validateParticipant(final JsonObject resource)
    throws BadRequestException {

    validateParticipant(resource.getString(PTS.json, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateParticipant
  /**
   ** Validates the existence and limits a participant segment of the given
   ** {@link JsonObject} resource.
   **
   ** @param  value              the string representation to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for a
   **                             participant have been violated.
   */
  public static void validateParticipant(final String value)
    throws BadRequestException {

    if (value == null)
      throw notNull(PTS.json);
    
    if (value.length() < PTS.minimum || value.length() > PTS.maximum)
      throw invalidLength(PTS.json);

    if (!value.matches(ALPHA))
      throw invalidCharacter(PTS.json);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the limits of an account type.
   **
   ** @param  entity             the {@link Type} entity to validate.
   **                            <br>
   **                            Allowed object is {@link Type}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for
   **                             an account type have been violated.
   */
  public static void validate(final Type entity)
    throws BadRequestException {

    validateAccountType(entity.getId());
    validateRequired(Type.Attribute.NAME.id, entity.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateAccountType
  /**
   ** Validates the existence and limits an account type segment of the given
   ** {@link JsonObject} resource.
   **
   ** @param  resource           the {@link JsonObject} resource to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for
   **                             an identity type have been violated.
   */
  public static void validateAccountType(final JsonObject resource)
    throws BadRequestException {

    validateAccountType(resource.getString(TID.json, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateAccountType
  /**
   ** Validates the existence and limits an account type segment of the given
   ** {@link JsonObject} resource.
   **
   ** @param  value              the string representation to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for
   **                             an identity type have been violated.
   */
  public static void validateAccountType(final String value)
    throws BadRequestException {

    if (value == null)
      throw notNull(SurrogateSchema.TID.name);
    
    if (value.length() < TID.minimum || value.length() > TID.maximum)
      throw invalidLength(SurrogateSchema.TID.name);

    if (!value.matches(DIGIT))
      throw invalidCharacter(SurrogateSchema.TID.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateExternalId
  /**
   ** Validates the existence and limits an external identifier segment of the
   ** given {@link JsonObject} resource.
   **
   ** @param  resource           the {@link JsonObject} resource to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for
   **                             an identity type have been violated.
   */
  public static void validateExternalId(final JsonObject resource)
    throws BadRequestException {

    validateExternalId(resource.getString(EID.json, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateExternalId
  /**
   ** Validates the existence and limits an external identifier segment of the
   ** given {@link JsonObject} resource.
   **
   ** @param  value              the string representation to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for
   **                             an identity type have been violated.
   */
  public static void validateExternalId(final String value)
    throws BadRequestException {

    if (value == null)
      throw notNull(SurrogateSchema.EID.name);
    
    if (value.length() < EID.minimum || value.length() > EID.maximum)
      throw invalidLength(SurrogateSchema.EID.name);

    if (!value.matches(ALPHA))
      throw invalidCharacter(SurrogateSchema.EID.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateTenant
  /**
   ** Validates the existence and limits a tenant of the given
   ** {@link JsonObject} resource.
   **
   ** @param  resource           the {@link JsonObject} resource to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @throws BadRequestException if either the syntactic or sematic rules for
   **                             an identity type have been violated.
   */
  public static void validateTenant(final JsonObject resource)
    throws BadRequestException {

    validateRequired("tenant", tenant(resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateRequired
  /**
   ** Validates the existence and limits a tenant of the given
   ** {@link JsonObject} resource.
   **
   ** @param  schema             the schema segment that violates the not-null
   **                            constraint.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the string representation to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BadRequestException if value is <code>null</code>.
   */
  public static void validateRequired(final String schema, final String value)
    throws BadRequestException {

    if (value == null)
      throw notNull(schema);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SurrogateSchema</code> with the
   ** properties specified.
   **
   ** @param  json               the attribute name in the json payload.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  index              the 0-based index of the segment.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  minimum            the minimum string length of the segment.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  maximum            the maximum string length of the segment.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  name               the name of attribute of a slice.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SurrogateSchema</code> populated with
   **                            the given values.
   **                            <br>
   **                            Possible object is
   **                            <code>SurrogateSchema</code>.
   */
  private final static SurrogateSchema build(final String json, final int index, final int minimum, final int maximum, final String name) {
    return new SurrogateSchema(json, index, minimum, maximum,  name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Factory method to create a {@link BadRequestException} for the specified
   ** schema segment if no value is provided for the segment.
   **
   ** @param  schema             the schema segment that violates the not-null
   **                            constraint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link BadRequestException} to be thrown.
   **                            <br>
   **                            Possible object is {@link BadRequestException}.
   */
  private static BadRequestException notNull(final String segment) {
    return BadRequestException.invalidValue(EndpointBundle.string(EndpointError.ARGUMENT_IS_NULL, segment));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidCharacter
  /**
   ** Factory method the create a {@link BadRequestException} for the specified
   ** schema segment if the value of the segment contains invalid characters.
   **
   ** @param  segment            the schema segment that violates the character
   **                            constraint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link BadRequestException} to be thrown.
   **                            <br>
   **                            Possible object is {@link BadRequestException}.
   */
  private static BadRequestException invalidCharacter(final String segment) {
    return BadRequestException.invalidValue(EndpointBundle.string(EndpointError.ARGUMENT_BAD_VALUE, segment));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidLength
  /**
   ** Factory method the create a {@link BadRequestException} for the specified
   ** schema segment if the value of the segment is not of the expected length.
   **
   ** @param  schema             the schema segment that violates the length
   **                            constraint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link BadRequestException} to be thrown.
   **                            <br>
   **                            Possible object is {@link BadRequestException}.
   */
  private static BadRequestException invalidLength(final String segment) {
    return BadRequestException.invalidValue(EndpointBundle.string(EndpointError.ARGUMENT_LENGTH_MISMATCH, segment));
  }
}