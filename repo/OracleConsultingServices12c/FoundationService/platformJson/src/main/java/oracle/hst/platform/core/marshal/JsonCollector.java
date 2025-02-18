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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Marshalling Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   JsonCollector.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    JsonCollector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.core.marshal;

import java.util.Map;
import java.util.Collection;

import java.util.stream.Collector;

import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

////////////////////////////////////////////////////////////////////////////////
// abstract class JsonCollector
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Helper class containing convenience methods for collection objects into
 ** {@link JsonObjectBuilder}. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class JsonCollector {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JsonCollector</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new JsonCollector()" and enforces use of the public method below.
   */
  private JsonCollector() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ofArray
  /**
   ** Returns a new {@link Collector} described by the given {@link Map.Entry}.
   ** {@code accumulator}, and {@code combiner} functions.
   ** <br>
   ** The resulting {@link Collector} has the
   ** {@link Collector.Characteristics#IDENTITY_FINISH} characteristic.
   **
   ** @return                    the new {@link Collector} wrapping a
   **                            {@link JsonArrayBuilder}.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static Collector<JsonArray, ?, JsonArrayBuilder> ofArray() {
    return Collector.of(Json::createArrayBuilder, JsonArrayBuilder::add, (c, e) -> {c.add(e); return c;});
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   array
  /**
   ** Returns a new {@link Collector} described by the given {@link Map.Entry}.
   ** {@code accumulator}, and {@code combiner} functions.
   ** <br>
   ** The resulting {@link Collector} has the
   ** {@link Collector.Characteristics#IDENTITY_FINISH} characteristic.
   **
   ** @return                    the new {@link Collector} wrapping a
   **                            {@link JsonArrayBuilder}.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static Collector<JsonObject, ?, JsonArrayBuilder> array() {
    return Collector.of(Json::createArrayBuilder, JsonArrayBuilder::add, (c, e) -> {c.add(e); return c;});
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   object
  /**
   ** Returns a new {@link Collector} described by the given {@link Map.Entry}.
   ** <br>
   ** The resulting {@link Collector} has the
   ** {@link Collector.Characteristics#IDENTITY_FINISH} characteristic.
   **
   ** @param <K>                 the type of keys maintained by the resulting
   **                            map
   **                            <br>
   **                            Possible object is <code>&lt;K&gt;</code>.
   ** @param <V>                 the type of mapped values.
   **                            <br>
   **                            Possible object is <code>&lt;V&gt;</code>.
   **
   ** @return                    the new {@link Collector} wrapping a
   **                            {@link JsonObjectBuilder}.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static <K, V> Collector<Map.Entry<K, V>, ?, JsonObjectBuilder> object() {
    return Collector.of(Json::createObjectBuilder, (builder, entry) -> {builder.add(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));}, JsonCollector::merge);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge
  /**
   ** Merge the current state of two {@link JsonObjectBuilder}s into a new one.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the {@link JsonObjectBuilder}s passed in after this
   ** method was invoked has no effect on the returned
   ** {@link JsonObjectBuilder}.
   ** <b>Note</b>
   ** <br>
   ** The right-hand-side {@link JsonObjectBuilder} take precedence of the
   ** merged result. Any property contained in the left-hand-side
   ** {@link JsonObjectBuilder} with the same name will overridden by the value
   ** mapping of this {@link JsonObjectBuilder}
   **
   ** @param  lhs                the left-hand-side to merge.
   **                            <br>
   **                            Allowed object is {@link JsonObjectBuilder}.
   ** @param  rhs                the right-hand-side to merge.
   **                            <br>
   **                            Allowed object is {@link JsonObjectBuilder}.
   **
   ** @return                    the {@link JsonObjectBuilder} containing the
   **                            merged result.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  public static JsonObjectBuilder merge(final JsonObjectBuilder lhs, final JsonObjectBuilder rhs) {
    final JsonObjectBuilder b = Json.createObjectBuilder();
    collect(b, lhs.build());
    collect(b, rhs.build());
    return b;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Merge the current state of a {@link JsonObject}s into the provided
   ** {@link JsonObjectBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the {@link JsonObject} passed in after this method
   ** was invoked has no effect on the returned {@link JsonObjectBuilder}.
   **
   ** @param  source             the {@link JsonObject} to collect in the
   **                            {@link JsonObjectBuilder collector}.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the {@link JsonObjectBuilder} populeted with
   **                            the elements of the {@link JsonObject source}
   **                            specified.
   **                            <br>
   **                            Allowed object is {@link JsonObjectBuilder}.
   */
  public static JsonObjectBuilder collect(final JsonObject source) {
    final JsonObjectBuilder b = Json.createObjectBuilder();
    collect(b, source);
    return b;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Merge the current state of a {@link JsonObject}s into the provided
   ** {@link JsonObjectBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the {@link JsonObject} passed in after this method
   ** was invoked has no effect on the returned {@link JsonObjectBuilder}.
   **
   ** @param  collector          the {@link JsonObjectBuilder} reciveiving the
   **                            elements of the {@link JsonObject source}
   **                            specified.
   **                            <br>
   **                            Allowed object is {@link JsonObjectBuilder}.
   ** @param  source             the {@link JsonObject} to collect in the
   **                            {@link JsonObjectBuilder collector}.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   */
  public static void collect(final JsonObjectBuilder collector, final JsonObject source) {
    source.keySet().stream().forEach(key -> collector.add(key, source.get(key)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge
  /**
   ** Merge the current state of two {@link JsonArrayBuilder}s into a new one.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the {@link JsonArrayBuilder}s passed in after this
   ** method was invoked has no effect on the returned
   ** {@link JsonArrayBuilder}.
   ** <b>Note</b>
   ** <br>
   ** The right-hand-side {@link JsonArrayBuilder} take precedence of the
   ** merged result. Any property contained in the left-hand-side
   ** {@link JsonArrayBuilder} with the same name will overridden by the value
   ** mapping of this {@link JsonArrayBuilder}
   **
   ** @param  lhs                the left-hand-side to merge.
   **                            <br>
   **                            Allowed object is {@link JsonArrayBuilder}.
   ** @param  rhs                the right-hand-side to merge.
   **                            <br>
   **                            Allowed object is {@link JsonArrayBuilder}.
   **
   ** @return                    the {@link JsonArrayBuilder} containing the
   **                            merged result.
   **                            <br>
   **                            Possible object is {@link JsonArrayBuilder}.
   */
  public static JsonArrayBuilder merge(final JsonArrayBuilder lhs, final JsonArrayBuilder rhs) {
    final JsonArrayBuilder a = Json.createArrayBuilder();
    collect(a, lhs.build());
    collect(a, rhs.build());
    return a;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Merge the current state of a {@link JsonArray}s into the provided
   ** {@link JsonArrayBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the {@link JsonArray} passed in after this method
   ** was invoked has no effect on the returned {@link JsonObjectBuilder}.
   **
   ** @param  collector          the {@link JsonArrayBuilder} reciveiving the
   **                            elements of the {@link JsonArray source}
   **                            specified.
   **                            <br>
   **                            Allowed object is {@link JsonArrayBuilder}.
   ** @param  source             the {@link JsonArray} to collect in the
   **                            {@link JsonArrayBuilder collector}.
   **                            <br>
   **                            Allowed object is {@link JsonArray}.
   */
  public static void collect(final JsonArrayBuilder collector, final JsonArray source) {
    source.stream().forEach(e -> collector.add(e));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectInteger
  /**
   ** Collects the current state of an array of {@link Integer}s into a newly
   ** created {@link JsonArrayBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the array passed in after this method was invoked
   ** has no effect on the returned {@link JsonArrayBuilder}.
   **
   ** @param  source             the rray of {@link Integer}s to collect in the
   **                            {@link JsonArrayBuilder collector} created.
   **                            <br>
   **                            Allowed object is rray of {@link Integer}.
   **
   ** @return                    a {@link JsonArrayBuilder} populated with the
   **                            elements of the rray of {@link Integer}s
   **                            specified.
   **                            <br>
   **                            Possible object is {@link JsonArrayBuilder}.
   */
  public static JsonArrayBuilder collectInteger(final Integer... source) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    Stream.of(source).forEach(e -> collector.add(e));
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectInteger
  /**
   ** Collects the current state of a {@link Collection} of {@link Integer}s
   ** into a {@link JsonArrayBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the {@link Collection} passed in after this method
   ** was invoked has no effect on the returned {@link JsonArrayBuilder}.
   **
   ** @param  source             the {@link Collection} to collect in the
   **                            {@link JsonArrayBuilder collector}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Integer}.
   **
   ** @return                    a {@link JsonArrayBuilder} populated with the
   **                            elements of the {@link Collection source}
   **                            specified.
   **                            <br>
   **                            Possible object is {@link JsonArrayBuilder}.
   */
  public static JsonArrayBuilder collectInteger(final Collection<Integer> source) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    source.stream().forEach(e -> collector.add(e));
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectLong
  /**
   ** Collects the current state of an array of {@link Long}s into a newly
   ** created {@link JsonArrayBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the array passed in after this method was invoked
   ** has no effect on the returned {@link JsonArrayBuilder}.
   **
   ** @param  source             the rray of {@link Long}s to collect in the
   **                            {@link JsonArrayBuilder collector} created.
   **                            <br>
   **                            Allowed object is rray of {@link Long}.
   **
   ** @return                    a {@link JsonArrayBuilder} populated with the
   **                            elements of the rray of {@link Long}s
   **                            specified.
   **                            <br>
   **                            Possible object is {@link JsonArrayBuilder}.
   */
  public static JsonArrayBuilder collectLong(final Long... source) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    Stream.of(source).forEach(e -> collector.add(e));
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectLong
  /**
   ** Collects the current state of a {@link Collection} of {@link Long}s into
   ** a {@link JsonArrayBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the {@link Collection} passed in after this method
   ** was invoked has no effect on the returned {@link JsonArrayBuilder}.
   **
   ** @param  source             the {@link Collection} to collect in the
   **                            {@link JsonArrayBuilder collector}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Integer}.
   **
   ** @return                    a {@link JsonArrayBuilder} populated with the
   **                            elements of the {@link Collection source}
   **                            specified.
   **                            <br>
   **                            Possible object is {@link JsonArrayBuilder}.
   */
  public static JsonArrayBuilder collectLong(final Collection<Long> source) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    source.stream().forEach(e -> collector.add(e));
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectDouble
  /**
   ** Collects the current state of an array of {@link Double}s into a newly
   ** created {@link JsonArrayBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the array passed in after this method was invoked
   ** has no effect on the returned {@link JsonArrayBuilder}.
   **
   ** @param  source             the rray of {@link Double}s to collect in the
   **                            {@link JsonArrayBuilder collector} created.
   **                            <br>
   **                            Allowed object is rray of {@link Double}.
   **
   ** @return                    a {@link JsonArrayBuilder} populated with the
   **                            elements of the rray of {@link Double}s
   **                            specified.
   **                            <br>
   **                            Possible object is {@link JsonArrayBuilder}.
   */
  public static JsonArrayBuilder collectDouble(final Double... source) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    Stream.of(source).forEach(e -> collector.add(e));
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectDouble
  /**
   ** Collects the current state of a {@link Collection} of {@link Double}s into
   ** a {@link JsonArrayBuilder}.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Any change applied on the {@link Collection} passed in after this method
   ** was invoked has no effect on the returned {@link JsonArrayBuilder}.
   **
   ** @param  source             the {@link Collection} to collect in the
   **                            {@link JsonArrayBuilder collector}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Double}.
   **
   ** @return                    a {@link JsonArrayBuilder} populated with the
   **                            elements of the {@link Collection source}
   **                            specified.
   **                            <br>
   **                            Possible object is {@link JsonArrayBuilder}.
   */
  public static JsonArrayBuilder collectDouble(final Collection<Double> source) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    source.stream().forEach(e -> collector.add(e));
    return collector;
  }
}