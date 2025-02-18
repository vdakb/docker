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
    Subsystem   :   Anonymous Identifier Service

    File        :   Schema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Schema.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.pid.marshal;

import bka.iam.identity.jpa.provider.Base;

import java.util.Set;
import java.util.Calendar;
import java.util.Collections;

import java.time.ZoneId;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import javax.json.Json;
import javax.json.JsonObject;

import oracle.hst.platform.jpa.SearchResult;

import oracle.hst.platform.core.entity.Path;

import oracle.hst.platform.core.marshal.JsonTrimmer;
import oracle.hst.platform.core.marshal.JsonCollector;

import bka.iam.identity.pid.model.Identifier;

import bka.iam.identity.jpa.provider.Metadata;

import javax.swing.plaf.nimbus.State;

public class Schema {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final JsonTrimmer trimmer;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>Schema</code> marshaller for trimming returned
   ** attributes for a REST operation.
   **
   ** @param trimmer             the {@link JsonTrimmer} to apply.
   **                            <br>
   **                            Allowed object is {@link JsonTrimmer}.
   */
  private Schema(final JsonTrimmer trimmer) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.trimmer = trimmer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Returns the <code>Schema</code> marshaller based on the passed value.
   **
   ** @param attribute           the attributes from the <code>emit</code> or
   **                            <code>omit</code> query parameter.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param excluded            <code>true</code> if the <code>attribute</code>
   **                            came from the <code>omit</code> query
   **                            parameter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>JsonTrimmer</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public static Schema build(final Set<Path> attribute, final boolean excluded) {
    return build(Collections.<Path>emptySet(), attribute, excluded);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Returns the <code>Schema</code> marshaller based on the passed value.
   **
   ** @param request             the attributes in the request object or
   **                            <code>null</code> for other requests.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param attribute           the attributes from the <code>emit</code> or
   **                            <code>omit</code> query parameter.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param excluded            <code>true</code> if the <code>attribute</code>
   **                            came from the <code>omit</code> query
   **                            parameter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>JsonTrimmer</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public static Schema build(final Set<Path> request, final Set<Path> attribute, final boolean excluded) {
    return build(JsonTrimmer.build(request, attribute, excluded));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Returns the <code>Schema</code> marshaller leveraging the specified
   ** {@link JsonTrimmer}.
   **
   ** @param trimmer             the {@link JsonTrimmer} to apply.
   **                            <br>
   **                            Allowed object is {@link JsonTrimmer}.
   **
   ** @return                    the <code>Schema</code>.
   **                            <br>
   **                            Possible object is <code>Schema</code>.
   */
  public static Schema build(final JsonTrimmer trimmer) {
    return new Schema(trimmer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Converts a {@link SearchResult} of {@link Identifier} entities to a
   ** {@link JsonObject} representation.
   **
   ** @param  schema             the {@link Schema} converter.
   **                            <br>
   **                            Allowed object is type {@link Schema}.
   ** @param  start              the 1-based start index of the
   **                            {@link SearchResult} collection.
   **                            <br>
   **                            Allowed object is type {@link Integer}.
   ** @param  result             the {@link SearchResult} of {@link Identifier}
   **                            entities to convert.
   **                            <br>
   **                            Allowed object is {@link SearchResult} of
   **                            type {@link Identifier}.
   **
   ** @return                    the {@link JsonObject} representation of the
   **                            given {@link Identifier} entity.
   */
  public static JsonObject identifier(final Schema schema, final Integer start, SearchResult<Identifier> result) {
    return Json.createObjectBuilder()
      .add("total", Long.valueOf(result.total()))
      .add("start", start)
      .add("items", result.size())
      .add("result", result.stream().map(e -> schema.convert(e)).collect(JsonCollector.array()).build())
    .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Merge a {@link JsonObject} representation into a {@link Identifier}
   ** entity.
   **
   ** @param  entity             the  {@link Identifier} entity as the target of
   **                            the operation.
   **                            <br>
   **                            Allowed object is {@link Identifier}.
   ** @param  resource           the {@link JsonObject} representation of a
   **                            {@link State} entity.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the {@link Identifier} merged with the
   **                            {@link JsonObject} representation.
   **                            <br>
   **                            Possible object is {@link Identifier}.
   */
  public static Identifier identifier(final Identifier entity, final JsonObject resource) {
    entity.setId(resource.getString(Identifier.Attribute.ID.id,          entity.getId()));
    entity.setUsedBy(resource.getString(Identifier.Attribute.USEDBY.id,  entity.getUsedBy()));
    entity.setActive(resource.getBoolean(Identifier.Attribute.ACTIVE.id, entity.getActive()));
    // reset any metadata associated with the entity
    entity.setVersion(null);
    entity.setCreatedBy(null);
    entity.setCreatedOn(null);
    entity.setUpdatedBy(null);
    entity.setUpdatedOn(null);
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Converts a {@link Identifier} entity to a {@link JsonObject}
   ** representation.
   **
   ** @param  pid                the {@link Identifier} entity to convert.
   **                            <br>
   **                            Allowed object is {@link Identifier}.
   **
   ** @return                    the {@link JsonObject} representation of the
   **                            given {@link Identifier} entity.
   */
  public JsonObject convert(final Identifier pid) {
    return this.trimmer.trim(
      Json.createObjectBuilder()
        .add(Identifier.Attribute.ID.id,     pid.getId())
        .add(Identifier.Attribute.ACTIVE.id, pid.getActive())
        .add(Identifier.Attribute.USEDBY.id, pid.getUsedBy())
        .add("meta", metadata(pid))
      .build()
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Converts a {@link Metadata} attachment to a {@link JsonObject}
   ** representation.
   **
   ** @param  entity             the {@link Metadata} attachment to convert.
   **                            <br>
   **                            Allowed object is {@link Metadata}.
   **
   ** @return                    the {@link JsonObject} representation of the
   **                            given {@link Metadata} attachment.
   */
  private JsonObject metadata(final Base entity) {
    return Json.createObjectBuilder()
      .add(Base.Attribute.VERSION.id,   entity.getVersion())
      .add(Base.Attribute.CREATEDBY.id, entity.getCreatedBy())
      .add(Base.Attribute.CREATEDON.id, convert(entity.getCreatedOn()).format(DateTimeFormatter.ISO_DATE_TIME))
      .add(Base.Attribute.UPDATEDBY.id, entity.getUpdatedBy())
      .add(Base.Attribute.UPDATEDON.id, convert(entity.getUpdatedOn()).format(DateTimeFormatter.ISO_DATE_TIME))
    .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Converts a {@link Date} value to a {@link LocalDateTime} representation.
   **
   ** @param  entity             the {@link Date} attachment to value.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the {@link LocalDateTime} representation of the
   **                            given {@link Date} value.
   */
  private static LocalDateTime convert(final Calendar value) {
    return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
