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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   GenericResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GenericResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.ServiceException;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.utility.GenericResourceSerializer;
import oracle.iam.system.simulation.scim.utility.GenericResourceDeserializer;

////////////////////////////////////////////////////////////////////////////////
// class GenericResource
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A generic SCIM object.
 ** <p>
 ** This object can be used if you have no Java object representing the SCIM
 ** object being returned.
 ** <p>
 ** This object can be used when the exact structure of the SCIM object that
 ** will be received as JSON text is not known. This will provide methods that
 ** can read attributes from those objects without needing to know the schema
 ** ahead of time. Another way to work with SCIM objects is when you know ahead
 ** of time what the schema will be. In that case you could still use this
 ** object, but {@link Entity} might be a better choice.
 ** <p>
 ** If you have a Entity derived object, you can always get a
 ** {@link GenericResource} by calling {@link #generic()}. You could also go
 ** the other way by calling {@link GenericResource#objectNode()}, followed by
 ** {@link Support#nodeToValue(JsonNode, Class)}.
 */
@JsonSerialize(using=GenericResourceSerializer.class)
@JsonDeserialize(using=GenericResourceDeserializer.class)
public class GenericResource<T extends GenericResource> implements Resource<T> {

  private static final Path ID          = Path.build().attribute("id");
  private static final Path META        = Path.build().attribute("meta");
  private static final Path SCHEMAS     = Path.build().attribute("schemas");
  private static final Path EXTERNAL_ID = Path.build().attribute("externalId");

  private final ObjectNode objectNode;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>GenericResource</code> SCIM Resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GenericResource() {
	  // ensure inheritance
    this(Support.jsonNodeFactory().objectNode());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>GenericResource</code> SCIM Resource backed by
   ** an {@link ObjectNode}.
   **
   ** @param  objectNode         the ObjectNode that backs this object.
   **                            <br>
   **                            Allowed object is {@link objectNode}.
   */
  public GenericResource(final ObjectNode objectNode) {
	  // ensure inheritance
    super();

	  // initialize instance attributs
    this.objectNode = objectNode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectNode
  /**
   ** Returns the {@link ObjectNode}.
   **
   ** @return                    the {@link ObjectNode}.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  public final ObjectNode objectNode() {
    return this.objectNode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a Boolean value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Boolean.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":true}
   ** </pre>
   ** then
   ** <pre>
   **   gr.booleanValue("path1")
   ** </pre>
   ** returns true and
   ** <pre>
   **   gr.booleanValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public Boolean booleanValue(final String path)
    throws ServiceException {

    return booleanValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a Boolean value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Boolean.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":true}
   ** </pre>
   ** then
   ** <pre>
   **   gr.booleanValue("path1")
   ** </pre>
   ** returns true and
   ** <pre>
   **   gr.booleanValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public Boolean booleanValue(final Path path)
    throws ServiceException {

    final JsonNode node = value(path);
    return node.isNull() ? null : node.booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanList
  /**
   ** Returns a list of Boolean from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Boolean.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": [true, false]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.booleanValue("path1")
   ** </pre>
   ** returns a list containing true, false and
   ** <pre>
   **   gr.booleanValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Boolean}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<Boolean> booleanList(final String path)
    throws ServiceException {

    return booleanList(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanList
  /**
   ** Returns a list of Boolean from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Boolean.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": [true, false]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.booleanValue("path1")
   ** </pre>
   ** returns a list containing true, false and
   ** <pre>
   **   gr.booleanValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Boolean}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public List<Boolean> booleanList(final Path path)
    throws ServiceException {

    final JsonNode           node     = value(path);
    final List<Boolean>      value    = new ArrayList<Boolean>();
    final Iterator<JsonNode> iterator = node.iterator();
    while (iterator.hasNext()) {
      value.add(iterator.next().booleanValue());
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a Integer value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Integer.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":13}
   ** </pre>
   ** then
   ** <pre>
   **   gr.integerValue("path1")
   ** </pre>
   ** returns 13 and
   ** <pre>
   **   gr.integerValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public Integer integerValue(final String path)
    throws ServiceException {

    return integerValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a Integer value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Integer.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":13}
   ** </pre>
   ** then
   ** <pre>
   **   gr.integerValue("path1")
   ** </pre>
   ** returns 13 and
   ** <pre>
   **   gr.integerValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public Integer integerValue(final Path path)
    throws ServiceException {

    final JsonNode node = value(path);
    return node.isNull() ? null : node.intValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerList
  /**
   ** Returns a list of Integer from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Integer.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": [17, 13]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.integerValue("path1")
   ** </pre>
   ** returns a list containing 17, 13 and
   ** <pre>
   **   gr.integerValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Integer}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<Integer> integerList(final String path)
    throws ServiceException {

    return integerList(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerList
  /**
   ** Returns a list of Integer from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Integer.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": [17, 13]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.integerValue("path1")
   ** </pre>
   ** returns a list containing 17, 13 and
   ** <pre>
   **   gr.integerValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Integer}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public List<Integer> integerList(final Path path)
    throws ServiceException {

    final JsonNode           node     = value(path);
    final List<Integer>      value    = new ArrayList<Integer>();
    final Iterator<JsonNode> iterator = node.iterator();
    while (iterator.hasNext()) {
      value.add(iterator.next().intValue());
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a Long value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Long.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":13}
   ** </pre>
   ** then
   ** <pre>
   **   gr.longValue("path1")
   ** </pre>
   ** returns 13 and
   ** <pre>
   **   gr.longValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Long}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public Long longValue(final String path)
    throws ServiceException {

    return longValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a Long value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Long.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":13}
   ** </pre>
   ** then
   ** <pre>
   **   gr.longValue("path1")
   ** </pre>
   ** returns 13 and
   ** <pre>
   **   gr.longValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Long}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public Long longValue(final Path path)
    throws ServiceException {

    final JsonNode node = value(path);
    return node.isNull() ? null : node.longValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longList
  /**
   ** Returns a list of Long from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Long.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": [17, 13]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.longValue("path1")
   ** </pre>
   ** returns a list containing 17, 13 and
   ** <pre>
   **   gr.longValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<Long> longList(final String path)
    throws ServiceException {

    return longList(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longList
  /**
   ** Returns a list of Long from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Long.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": [17, 13]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.longValue("path1")
   ** </pre>
   ** returns a list containing 17, 13 and
   ** <pre>
   **   gr.longValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public List<Long> longList(final Path path)
    throws ServiceException {

    final JsonNode           node     = value(path);
    final List<Long>         value    = new ArrayList<Long>();
    final Iterator<JsonNode> iterator = node.iterator();
    while (iterator.hasNext()) {
      value.add(iterator.next().longValue());
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Returns a Double value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Double.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":2.0}
   ** </pre>
   ** then
   ** <pre>
   **   gr.doubleValue("path1")
   ** </pre>
   ** returns 2.0 and
   ** <pre>
   **   gr.doubleValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Double}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public Double doubleValue(final String path)
    throws ServiceException {

    return doubleValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Returns a Double value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Double.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":2.0}
   ** </pre>
   ** then
   ** <pre>
   **   gr.doubleValue("path1")
   ** </pre>
   ** returns 2.0 and
   ** <pre>
   **   gr.doubleValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Double}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public Double doubleValue(final Path path)
    throws ServiceException {

    final JsonNode node = value(path);
    return node.isNull() ? null : node.doubleValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleList
  /**
   ** Returns a list of Double from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Double.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": [2.1, 2.2]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.doubleValue("path1")
   ** </pre>
   ** returns a list containing 2.1, 2.2 and
   ** <pre>
   **   gr.doubleValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Double}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<Double> doubleList(final String path)
    throws ServiceException {

    return doubleList(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleList
  /**
   ** Returns a list of Double from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Double.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": [2.1, 2.2]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.doubleValue("path1")
   ** </pre>
   ** returns a list containing 2.1, 2.2 and
   ** <pre>
   **   gr.doubleValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Double}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public List<Double> doubleList(final Path path)
    throws ServiceException {

    final JsonNode           node     = value(path);
    final List<Double>       value    = new ArrayList<Double>();
    final Iterator<JsonNode> iterator = node.iterator();
    while (iterator.hasNext()) {
      value.add(iterator.next().doubleValue());
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Returns a Date value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Date.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"1990-06-20T17:54:47.542Z"}
   ** </pre>
   ** then
   ** <pre>
   **   gr.dateValue("path1")
   ** </pre>
   ** returns "1990-06-20T17:54:47.542Z" and
   ** <pre>
   **   gr.dateValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public Date dateValue(final String path)
    throws ServiceException {

    return dateValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Returns a Date value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a Date.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"1990-06-20T17:54:47.542Z"}
   ** </pre>
   ** then
   ** <pre>
   **   gr.dateValue("path1")
   ** </pre>
   ** returns "1990-06-20T17:54:47.542Z" and
   ** <pre>
   **   gr.dateValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @throws ServiceException   thrown if an error occurs.
   */
  public Date dateValue(final Path path)
    throws ServiceException {

    final JsonNode node = value(path);
    return node.isNull() ? null : dateValue(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateList
  /**
   ** Returns a list of Date from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Date.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": ["1990-06-20T17:54:47.542Z", "1958-01-14T17:54:47.542Z"]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.dateValue("path1")
   ** </pre>
   ** returns a list containing "1990-06-20T17:54:47.542Z", "1958-01-14T17:54:47.542Z" and
   ** <pre>
   **   gr.dateValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Date}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<Date> dateList(final String path)
    throws ServiceException {

    return dateList(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateList
  /**
   ** Returns a list of Date from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of Date.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": ["1990-06-20T17:54:47.542Z", "1958-01-14T17:54:47.542Z"]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.dateValue("path1")
   ** </pre>
   ** returns a list containing "1990-06-20T17:54:47.542Z", "1958-01-14T17:54:47.542Z" and
   ** <pre>
   **   gr.dateValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Date}.
   **
   ** @throws ServiceException   thrown if an error occurs.
   */
  public List<Date> dateList(final Path path)
    throws ServiceException {

    final JsonNode           node     = value(path);
    final List<Date>         value    = new ArrayList<Date>();
    final Iterator<JsonNode> iterator = node.iterator();
    while (iterator.hasNext()) {
      value.add(dateValue(iterator.next()));
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a String value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a String.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"stringValue1"}
   ** </pre>
   ** then
   ** <pre>
   **   gr.stringValue("path1")
   ** </pre>
   ** returns "stringValue1" and
   ** <pre>
   **   gr.stringValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public String stringValue(final String path)
    throws ServiceException {

    return stringValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a String value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a String.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"stringValue1"}
   ** </pre>
   ** then
   ** <pre>
   **   gr.stringValue("path1")
   ** </pre>
   ** returns "stringValue1" and
   ** <pre>
   **   gr.stringValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public String stringValue(final Path path)
    throws ServiceException {

    final JsonNode node = value(path);
    return node.isNull() ? null : node.textValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringList
  /**
   ** Returns a list of String from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of String.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": ["stringValue1", "stringValue2"]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.stringValue("path1")
   ** </pre>
   ** returns a list containing "stringValue1", "stringValue2" and
   ** <pre>
   **   gr.stringValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<String> stringList(final String path)
    throws ServiceException {

    return stringList(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringList
  /**
   ** Returns a list of String from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of String.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": ["stringValue1", "stringValue2"]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.stringValue("path1")
   ** </pre>
   ** returns a list containing "stringValue1", "stringValue2" and
   ** <pre>
   **   gr.stringValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public List<String> stringList(final Path path)
    throws ServiceException {

    final JsonNode           node     = value(path);
    final List<String>       value    = new ArrayList<String>();
    final Iterator<JsonNode> iterator = node.iterator();
    while (iterator.hasNext()) {
      value.add(iterator.next().textValue());
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryValue
  /**
   ** Returns a byte[] value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a byte[].
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"AjIzLg=="}
   ** </pre>
   ** then
   ** <pre>
   **   gr.binaryValue("path1")
   ** </pre>
   ** returns "AjIzLg==" and
   ** <pre>
   **   gr.binaryValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link byte[]}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public byte[] binaryValue(final String path)
    throws ServiceException {

    return binaryValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryValue
  /**
   ** Returns a byte[] value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a byte[].
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"AjIzLg=="}
   ** </pre>
   ** then
   ** <pre>
   **   gr.binaryValue("path1")
   ** </pre>
   ** returns "AjIzLg==" and
   ** <pre>
   **   gr.binaryValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link byte[]}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public byte[] binaryValue(final Path path)
    throws ServiceException {

    final JsonNode node = value(path);
    try {
      return node.isNull() ? null : node.binaryValue();
    }
    catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryList
  /**
   ** Returns a list of byte[] from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of byte[].
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": ["AjIzLg==", "AjNjLp=="]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.binaryValue("path1")
   ** </pre>
   ** returns a list containing "AjIzLg==", "AjNjLp==" and
   ** <pre>
   **   gr.binaryValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link byte[]}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<byte[]> binaryList(final String path)
    throws ServiceException {

    return binaryList(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryList
  /**
   ** Returns a list of byte[] from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of byte[].
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": ["AjIzLg==", "AjNjLp=="]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.binaryValue("path1")
   ** </pre>
   ** returns a list containing "AjIzLg==", "AjNjLp==" and
   ** <pre>
   **   gr.binaryValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link byte[]}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<byte[]> binaryList(final Path path)
    throws ServiceException {

    final JsonNode           node     = value(path);
    final List<byte[]>       value    = new ArrayList<byte[]>();
    final Iterator<JsonNode> iterator = node.iterator();
    try {
      while (iterator.hasNext()) {
        byte[] bytes = iterator.next().binaryValue();
        if(bytes == null)
          // this is not a binary or text node.
          throw new ServiceException(ServiceError.PATH_INVALID_VALUE_ENCODING, path.toString(), "base64");

        value.add(bytes);
      }
    }
    catch (IOException e) {
      throw new ServiceException(e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uriValue
  /**
   ** Returns a URI value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a URI.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"http://localhost:8080/uri/One"}
   ** </pre>
   ** then
   ** <pre>
   **   gr.uriValue("path1")
   ** </pre>
   ** returns "http://localhost:8080/uri/One" and
   ** <pre>
   **   gr.uriValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   ** <p>
   ** This parses the given path exactly as specified by the grammar in
   ** <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>,
   ** Appendix&nbsp;A, <b><i>except for the following deviations:</i></b>
   ** <ul>
   **   <li>An empty authority component is permitted as long as it is followed
   **       by a non-empty path, a query component, or a fragment component.
   **       This allows the parsing of URIs such as
   **       "<code>file:///foo/bar</code>", which seems to be the intent of
   **       RFC&nbsp;2396 although the grammar does not permit it. If the
   **       authority component is empty then the user-information, host, and
   **       port components are undefined.
   **   <li>Empty relative paths are permitted; this seems to be the intent of
   **       RFC&nbsp;2396 although the grammar does not permit it. The primary
   **       consequence of this deviation is that a standalone fragment such as
   **       "<code>#foo</code>" parses as a relative URI with an empty path and
   **       the given fragment, and can be usefully
   **       <a href="#resolve-frag">resolved</a> against a base URI.
   **   <li>IPv4 addresses in host components are parsed rigorously, as
   **       specified by
   **       <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>:
   **       Each element of a dotted-quad address must contain no more than
   **       three decimal digits. Each element is further constrained to have a
   **       value no greater than 255.
   **   <li>Hostnames in host components that comprise only a single domain
   **       label are permitted to start with an <i>alphanum</i> character. This
   **       seems to be the intent of
   **       <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
   **       section&nbsp;3.2.2 although the grammar does not permit it. The
   **       consequence of this deviation is that the authority component of a
   **       hierarchical URI such as <code>s://123</code>, will parse as a
   **       server-based authority.
   **   <li>IPv6 addresses are permitted for the host component. An IPv6 address
   **       must be enclosed in square brackets (<code>'['</code> and
   **       <code>']'</code>) as specified by
   **       <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>. The
   **       IPv6 address itself must parse according to
   **       <a href="http://www.ietf.org/rfc/rfc2373.txt">RFC&nbsp;2373</a>.
   **       IPv6 addresses are further constrained to describe no more than
   **       sixteen bytes of address information, a constraint implicit in
   **       RFC&nbsp;2373 but not expressible in the grammar.
   **   <li>Characters in the <i>other</i> category are permitted wherever
   **       RFC&nbsp;2396 permits <i>escaped</i> octets, that is, in the
   **       user-information, path, query, and fragment components, as well as
   **       in the authority component if the authority is registry-based. This
   **       allows URIs to contain Unicode characters beyond those in the
   **       US-ASCII character set.
   ** </ul>
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link URI}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public URI uriValue(final String path)
    throws ServiceException {

    return uriValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uriValue
  /**
   ** Returns a URI value from a generic SCIM resource.
   ** <br>
   ** If the path exists, the JSON node at the path must be a URI.
   ** <br>
   ** If the path does not exist, <code>null</code> will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"http://localhost:8080/uri/One"}
   ** </pre>
   ** then
   ** <pre>
   **   gr.uriValue("path1")
   ** </pre>
   ** returns "http://localhost:8080/uri/One" and
   ** <pre>
   **   gr.uriValue("bogusPath")
   ** </pre>
   ** returns <code>null</code>.
   ** <p>
   ** This parses the given path exactly as specified by the grammar in
   ** <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>,
   ** Appendix&nbsp;A, <b><i>except for the following deviations:</i></b>
   ** <ul>
   **   <li>An empty authority component is permitted as long as it is followed
   **       by a non-empty path, a query component, or a fragment component.
   **       This allows the parsing of URIs such as
   **       "<code>file:///foo/bar</code>", which seems to be the intent of
   **       RFC&nbsp;2396 although the grammar does not permit it. If the
   **       authority component is empty then the user-information, host, and
   **       port components are undefined.
   **   <li>Empty relative paths are permitted; this seems to be the intent of
   **       RFC&nbsp;2396 although the grammar does not permit it. The primary
   **       consequence of this deviation is that a standalone fragment such as
   **       "<code>#foo</code>" parses as a relative URI with an empty path and
   **       the given fragment, and can be usefully
   **       <a href="#resolve-frag">resolved</a> against a base URI.
   **   <li>IPv4 addresses in host components are parsed rigorously, as
   **       specified by
   **       <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>:
   **       Each element of a dotted-quad address must contain no more than
   **       three decimal digits. Each element is further constrained to have a
   **       value no greater than 255.
   **   <li>Hostnames in host components that comprise only a single domain
   **       label are permitted to start with an <i>alphanum</i> character. This
   **       seems to be the intent of
   **       <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
   **       section&nbsp;3.2.2 although the grammar does not permit it. The
   **       consequence of this deviation is that the authority component of a
   **       hierarchical URI such as <code>s://123</code>, will parse as a
   **       server-based authority.
   **   <li>IPv6 addresses are permitted for the host component. An IPv6 address
   **       must be enclosed in square brackets (<code>'['</code> and
   **       <code>']'</code>) as specified by
   **       <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>. The
   **       IPv6 address itself must parse according to
   **       <a href="http://www.ietf.org/rfc/rfc2373.txt">RFC&nbsp;2373</a>.
   **       IPv6 addresses are further constrained to describe no more than
   **       sixteen bytes of address information, a constraint implicit in
   **       RFC&nbsp;2373 but not expressible in the grammar.
   **   <li>Characters in the <i>other</i> category are permitted wherever
   **       RFC&nbsp;2396 permits <i>escaped</i> octets, that is, in the
   **       user-information, path, query, and fragment components, as well as
   **       in the authority component if the authority is registry-based. This
   **       allows URIs to contain Unicode characters beyond those in the
   **       US-ASCII character set.
   ** </ul>
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or <code>null</code>.
   **                            <br>
   **                            Possible object is {@link URI}.
   **
   ** @throws ServiceException   thrown if an error occurs.
   */
  public URI uriValue(final Path path)
    throws ServiceException {

    final JsonNode node = value(path);
    try {
      return node.isNull() ? null : new URI(node.textValue());
    }
    catch (URISyntaxException e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uriList
  /**
   ** Returns a list of URI from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of URI.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": ["http://localhost:8080/uri/One", "http://localhost:8080/uri/Two"]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.uriValue("path1")
   ** </pre>
   ** returns a list containing "http://localhost:8080/uri/One", "http://localhost:8080/uri/Two" and
   ** <pre>
   **   gr.uriValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link URI}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public List<URI> uriList(final String path)
    throws ServiceException {

    return uriList(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uriList
  /**
   ** Returns a list of URI from a generic SCIM resource.
   ** If the path exists, the JSON node at the path must be a list of URI.
   ** <br>
   ** If the path does not exist, an empty list will be returned.
   ** <p>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1": ["http://localhost:8080/uri/One", "http://localhost:8080/uri/Two"]}
   ** </pre>
   ** then
   ** <pre>
   **   gr.uriValue("path1")
   ** </pre>
   ** returns a list containing "http://localhost:8080/uri/One", "http://localhost:8080/uri/Two" and
   ** <pre>
   **   gr.uriValue("bogusPath")
   ** </pre>
   ** returns an empty list
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the value at the path, or an empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link URI}.
   **
   ** @throws ServiceException   thrown if an error occurs.
   */
  public List<URI> uriList(final Path path)
    throws ServiceException{

    final JsonNode           node     = value(path);
    final List<URI>          value    = new ArrayList<URI>();
    final Iterator<JsonNode> iterator = node.iterator();
    try {
      while (iterator.hasNext()) {
        String uriString = iterator.next().textValue();
        value.add(new URI(uriString));
      }
    }
    catch (URISyntaxException e) {
      throw new ServiceException(e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns a single {@link JsonNode} from a generic SCIM resource.
   ** <br>
   ** This value may be an {@link ArrayNode}.
   ** <p>
   ** For example:
   ** <br>
   ** With a generic SCIM resource representing the folowing JSON:
   ** <pre>
   **   { "name" : "Bob"
   **   , "friends" : [ "Amy", "Beth", "Carol" ]
   **   }
   ** </pre>
   ** then
   ** <pre>
   **   gr.value("name");
   ** </pre>
   ** would return a <code>TextNode</code> containing "Bob" and
   ** <pre>
   **   gr.value("friends");
   ** </pre>
   ** would return an {@link ArrayNode} containing 3 <code>TextNode</code> with
   ** the values "Amy", "Beth", and "Carol".
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link JsonNode} at the path, or a
   **                            <code>NullNode</code> if nothing is found.
   **                            <br>
   **                            Possible object is {@link JsonNode}.
   **
   ** @throws ServiceException   thrown if an error occurs.
   */
  public JsonNode value(final String path)
    throws ServiceException {

    return value(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns a single {@link JsonNode} from a generic SCIM resource.
   ** <br>
   ** This value may be an {@link ArrayNode}.
   ** <p>
   ** For example:
   ** <br>
   ** With a generic SCIM resource representing the folowing JSON:
   ** <pre>
   **   { "name" : "Bob"
   **   , "friends" : [ "Amy", "Beth", "Carol" ]
   **   }
   ** </pre>
   ** then
   ** <pre>
   **   gr.value("name");
   ** </pre>
   ** would return a <code>TextNode</code> containing "Bob" and
   ** <pre>
   **   gr.value("friends");
   ** </pre>
   ** would return an {@link ArrayNode} containing 3 <code>TextNode</code> with
   ** the values "Amy", "Beth", and "Carol".
   **
   ** @param  path               the path to get the value from.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the {@link JsonNode} at the path, or a
   **                            <code>NullNode</code> if nothing is found.
   **                            <br>
   **                            Possible object is {@link JsonNode}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public JsonNode value(final Path path)
    throws ServiceException {

    return Support.value(path, this.objectNode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (Resource)
  /**
   ** Sets the identifier of the object.
   **
   ** @param  id                 the identifier of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T id(final String id) {
    try {
      Support.replaceValue(ID, this.objectNode, Support.valueToNode(id));
    }
    catch (Exception e) {
      // this should never happen.
      throw new RuntimeException(e);
    }
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (Resource)
  /**
   ** Returns the identifier of the object.
   **
   ** @return                    the identifier of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String id() {
    try {
      JsonNode value = Support.value(ID, this.objectNode);
      if (value.isNull()) {
        return null;
      }
      return Support.nodeToValue(value, String.class);
    }
    catch (Exception e) {
      // this should never happen.
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalId (Resource)
  /**
   ** Sets the external identifier of the object.
   **
   ** @param  id                 the external identifier of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T externalId(final String id) {
    try {
      Support.replaceValue(EXTERNAL_ID, objectNode, Support.valueToNode(id));
    }
    catch (Exception e) {
      // this should never happen.
      throw new RuntimeException(e);
    }
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalId (Resource)
  /**
   ** Returns the external identifier of the object.
   **
   ** @return                    the external identifier of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String externalId() {
    try {
      JsonNode value = Support.value(EXTERNAL_ID, objectNode);
      if (value.isNull()) {
        return null;
      }
      return Support.nodeToValue(value, String.class);
    }
    catch (Exception e) {
      // this should never happen.
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace (Resource)
  /**
   ** Sets the schema namespaces for this object.
   ** <br>
   ** This set should contain all schema namespaces including the one for this
   ** object and all extensions.
   **
   ** @param  namespace          a set containing the schema namespaces for this
   **                            object.
   **                            <br>
   **                            Allowed object is {@link Collection} of
   **                            {@link String}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T namespace(final Collection<String> namespace) {
    try {
      Support.replaceValue(SCHEMAS, this.objectNode, Support.valueToNode(namespace));
    }
    catch (Exception e) {
      // This should never happen.
      throw new RuntimeException(e);
    }
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace (Resource)
  /**
   ** Returns the schema namespaces for this object.
   ** <br>
   ** This includes the one for the class that extends this class (taken from
   ** the annotation), as well as any that are present in the extensions.
   **
   ** @return                    a set containing the schema namespaces for this
   **                            object.
   **                            <br>
   **                            Possible object is {@link Collection} of
   **                            {@link String}.
   */
  @Override
  public final Collection<String> namespace() {
    try {
      JsonNode value = Support.value(SCHEMAS, objectNode);
      if (value.isNull() || !value.isArray()) {
        return Collections.emptyList();
      }
      return Support.nodeToValue((ArrayNode)value, String.class);
    }
    catch (Exception e) {
      // this should never happen.
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata (Resource)
  /**
   ** Sets metadata for the object.
   **
   ** @param  meta               <code>Meta</code> containing metadata about the
   **                            object.
   **                            <br>
   **                            Allowed object is {@link Metadata}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T metadata(final Metadata meta) {
    try {
      Support.replaceValue(META, this.objectNode, Support.valueToNode(meta));
    }
    catch (Exception e) {
      // This should never happen.
      throw new RuntimeException(e);
    }
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata (Resource)
  /**
   ** Returns metadata about the object.
   **
   ** @return                    <code>Meta</code> containing metadata about the
   **                            object.
   **                            <br>
   **                            Possible object is {@link Metadata}.
   */
  @Override
  public final Metadata metadata() {
    try {
      List<JsonNode> values = Support.matchPath(META, objectNode);
      if (values.isEmpty()) {
        return null;
      }
      return Support.nodeToValue(values.get(0), Metadata.class);
    }
    catch (Exception e) {
      // This should never happen.
      throw new RuntimeException(e);
    }
  }

  /**
   ** {@inheritDoc}
   */
  public GenericResource generic() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Returns a the date represented by the supplied JsonNode.
   **
   ** @param  node               the {@link JsonNode} representing the date.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the date represented by the JsonNode.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @throws ServiceException   thrown if an error occurs.
   */
  public static Date dateValue(final JsonNode node)
    throws ServiceException {

    try {
    return Support.objectReader().forType(Date.class).readValue(node);
    }
    catch (JsonProcessingException e) {
      throw new ServiceException(e);
    }
    catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character " " (space).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                   the string representation of this instance.
   */
  @Override
  public String toString() {
    try {
      return Support.objectWriter().withDefaultPrettyPrinter().writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBoolean
  /**
   ** Adds Boolean values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ true, false ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addBoolean(Path.from("path1"), path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Boolean, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addBoolean(Path.from("path2"), path2values)
   ** </pre>
   ** where path2values is a {@link List} of Boolean, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Boolean}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource addBoolean(final String path, final List<Boolean> values)
    throws ServiceException {

    return addBoolean(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBoolean
  /**
   ** Adds Boolean values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ true, false ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addBooleanValues("path1", path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Boolean, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addBooleanValues("path2", path2values)
   ** </pre>
   ** where path2values is a {@link List} of Boolean, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Boolean}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource addBoolean(final Path path, final List<Boolean> values)
    throws ServiceException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for(Boolean value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInteger
  /**
   ** Adds Integer values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ 13, 17 ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addInteger(Path.from("path1"), path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Integer, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addInteger(Path.from("path2"), path2values)
   ** </pre>
   ** where path2values is a {@link List} of Integer, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Integer}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource addInteger(final String path, final List<Integer> values)
    throws ServiceException {

    return addInteger(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInteger
  /**
   ** Adds Integer values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ 13, 17 ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addIntegerValues("path1", path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Integer, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addIntegerValues("path2", path2values)
   ** </pre>
   ** where path2values is a {@link List} of Integer, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Integer}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource addInteger(final Path path, final List<Integer> values)
    throws ServiceException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for(Integer value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addLong
  /**
   ** Adds Long values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ 13, 17 ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addLong(Path.from("path1"), path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Long, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addLong(Path.from("path2"), path2values)
   ** </pre>
   ** where path2values is a {@link List} of Long, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource addLong(final String path, final List<Long> values)
    throws ServiceException {

    return addLong(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addLong
  /**
   ** Adds Long values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ 13, 17 ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addLongValues("path1", path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Long, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addLongValues("path2", path2values)
   ** </pre>
   ** where path2values is a {@link List} of Long, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource addLong(final Path path, final List<Long> values)
    throws ServiceException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for(Long value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDouble
  /**
   ** Adds Double values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ 2.1, 2.2 ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addDouble(Path.from("path1"), path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Double, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addDouble(Path.from("path2"), path2values)
   ** </pre>
   ** where path2values is a {@link List} of Double, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Double}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource addDouble(final String path, final List<Double> values)
    throws ServiceException {

    return addDouble(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDouble
  /**
   ** Adds Double values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ 2.1, 2.2 ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addDoubleValues("path1", path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Double, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addDoubleValues("path2", path2values)
   ** </pre>
   ** where path2values is a {@link List} of Double, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Double}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource addDouble(final Path path, final List<Double> values)
    throws ServiceException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for(Double value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDate
  /**
   ** Adds Date values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ "1990-06-20T17:54:47.542Z", "1958-01-14T17:54:47.542Z" ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addDate(Path.from("path1"), path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Date, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addDate(Path.from("path2"), path2values)
   ** </pre>
   ** where path2values is a {@link List} of Date, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Date}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource addDate(final String path, final List<Date> values)
    throws ServiceException {

    return addDate(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDate
  /**
   ** Adds Date values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ "1990-06-20T17:54:47.542Z", "1958-01-14T17:54:47.542Z" ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addDateValues("path1", path1values)
   ** </pre>
   ** where path1Value is a {@link List} of Date, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addDateValues("path2", path2values)
   ** </pre>
   ** where path2values is a {@link List} of Date, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Date}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource addDate(final Path path, final List<Date> values)
    throws ServiceException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for(Date value : values) {
      node.add(Support.valueToNode(value));
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addString
  /**
   ** Adds String values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ "stringValue1", "stringValue2" ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addString(Path.from("path1"), path1values)
   ** </pre>
   ** where path1Value is a {@link List} of String, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addString(Path.from("path2"), path2values)
   ** </pre>
   ** where path2values is a {@link List} of String, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource addString(final String path, final List<String> values)
    throws ServiceException {

    return addString(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addString
  /**
   ** Adds String values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ "stringValue1", "stringValue2" ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addStringValues("path1", path1values)
   ** </pre>
   ** where path1Value is a {@link List} of String, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addStringValues("path2", path2values)
   ** </pre>
   ** where path2values is a {@link List} of String, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource addString(final Path path, final List<String> values)
    throws ServiceException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for(String value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBinary
  /**
   ** Adds byte[] values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ "AjIzLg==", "AjNjLp==" ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addBinary(Path.from("path1"), path1values)
   ** </pre>
   ** where path1Value is a {@link List} of byte[], would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addBinary(Path.from("path2"), path2values)
   ** </pre>
   ** where path2values is a {@link List} of byte[], create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link byte[]}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource addBinary(final String path, final List<byte[]> values)
    throws ServiceException {

    return addBinary(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBinary
  /**
   ** Adds byte[] values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ "AjIzLg==", "AjNjLp==" ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addBinaryValues("path1", path1values)
   ** </pre>
   ** where path1Value is a {@link List} of byte[], would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addBinaryValues("path2", path2values)
   ** </pre>
   ** where path2values is a {@link List} of byte[], create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link byte[]}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource addBinary(final Path path, final List<byte[]> values)
    throws ServiceException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for(byte[] value : values) {
      node.add(Support.jsonNodeFactory().binaryNode(value));
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addURI
  /**
   ** Adds URI values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ "http://localhost:8080/uri/One", "http://localhost:8080/uri/Two" ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addURI(Path.from("path1"), path1values)
   ** </pre>
   ** where path1Value is a {@link List} of URI, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addURI(Path.from("path2"), path2values)
   ** </pre>
   ** where path2values is a {@link List} of URI, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link URI}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource addURI(final String path, final List<URI> values)
    throws ServiceException {

    return addURI(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addURI
  /**
   ** Adds URI values to an array node.
   ** <br>
   ** If no array node exists at the specified path, a new array node will be
   ** created.
   ** <br>
   ** For example:
   ** <br>
   ** In a GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":[ "http://localhost:8080/uri/One", "http://localhost:8080/uri/Two" ]}
   ** </pre>
   ** then
   ** <pre>
   **   gsr.addURIValues("path1", path1values)
   ** </pre>
   ** where path1Value is a {@link List} of URI, would add each of the items
   ** in the path1values list to the "path1" list in the generic SCIM resource.
   ** <pre>
   **   gsr.addURIValues("path2", path2values)
   ** </pre>
   ** where path2values is a {@link List} of URI, create a new array called
   ** "path2"
   ** <p>
   ** <b>Note</b>:
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path to add the list to.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  values             the {@link List} containing the new values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link URI}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource addURI(final Path path, final List<URI> values)
    throws ServiceException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for(URI value : values) {
      node.add(value.toString());
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add new values at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#addValue(Path, ObjectNode, JsonNode)} method:
   ** Support.addValue(path, objectNode(), values).
   ** <p>
   ** The {@link Support#valueToNode(Object)} method may be used to convert
   ** the given value instance to a JSON node.
   **
   ** @param  path               the path to the attribute whose values to add.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link ArrayNode}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource add(final String path, final ArrayNode value)
    throws ServiceException {

    return add(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add new values at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#addValue(Path, ObjectNode, JsonNode)} method:
   ** Support.addValue(path, objectNode(), values).
   ** <p>
   ** The {@link Support#valueToNode(Object)} method may be used to convert
   ** the given value instance to a JSON node.
   **
   ** @param  path               the path to the attribute whose values to add.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link ArrayNode}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource add(final Path path, final ArrayNode value)
    throws ServiceException {

    Support.addValue(path, this.objectNode, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Boolean value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":true}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace("path1", path1value)
   ** </pre>
   ** where path1value is a Boolean would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace("path2", path2value)
   ** </pre>
   ** where path2value is a Boolean would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final Boolean value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Boolean value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":true}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace(Path.from("path1"), path1value)
   ** </pre>
   ** where path1value is a Boolean would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace(Path.from("path2"), path2value)
   ** </pre>
   ** where path2value is a Boolean would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final Path path, final Boolean value)
    throws ServiceException {

    return replace(path, Support.jsonNodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Integer value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":13}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace("path1", path1value)
   ** </pre>
   ** where path1value is a Integer would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace("path2", path2value)
   ** </pre>
   ** where path2value is a Integer would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final Integer value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Integer value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":13}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace(Path.from("path1"), path1value)
   ** </pre>
   ** where path1value is a Integer would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace(Path.from("path2"), path2value)
   ** </pre>
   ** where path2value is a Integer would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final Path path, final Integer value)
    throws ServiceException {

    return replace(path, Support.jsonNodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Long value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":13}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace("path1", path1value)
   ** </pre>
   ** where path1value is a Long would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace("path2", path2value)
   ** </pre>
   ** where path2value is a Long would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final Long value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Long value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":13}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace(Path.from("path1"), path1value)
   ** </pre>
   ** where path1value is a Long would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace(Path.from("path2"), path2value)
   ** </pre>
   ** where path2value is a Long would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final Path path, final Long value)
    throws ServiceException {

    return replace(path, Support.jsonNodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Double value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":2.0}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace("path1", path1value)
   ** </pre>
   ** where path1value is a Double would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace("path2", path2value)
   ** </pre>
   ** where path2value is a Double would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final Double value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Double value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":2.0}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace(Path.from("path1"), path1value)
   ** </pre>
   ** where path1value is a Double would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace(Path.from("path2"), path2value)
   ** </pre>
   ** where path2value is a Double would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final Path path, final Double value)
    throws ServiceException {

    return replace(path, Support.jsonNodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Date value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"1990-06-20T17:54:47.542Z"}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace("path1", path1value)
   ** </pre>
   ** where path1value is a Date would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace("path2", path2value)
   ** </pre>
   ** where path2value is a Date would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final Date value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a Date value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"1990-06-20T17:54:47.542Z"}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace(Path.from("path1"), path1value)
   ** </pre>
   ** where path1value is a Date would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace(Path.from("path2"), path2value)
   ** </pre>
   ** where path2value is a Date would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final Path path, final Date value)
    throws ServiceException {

    return replace(path, Support.valueToNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a String value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"stringValue"}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace("path1", path1value)
   ** </pre>
   ** where path1value is a String would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace("path2", path2value)
   ** </pre>
   ** where path2value is a String would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final String value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a String value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"stringValue"}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace(Path.from("path1"), path1value)
   ** </pre>
   ** where path1value is a String would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace(Path.from("path2"), path2value)
   ** </pre>
   ** where path2value is a String would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final Path path, final String value)
    throws ServiceException {

    return replace(path, Support.jsonNodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a byte[] value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"AjIzLg=="}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace("path1", path1value)
   ** </pre>
   ** where path1value is a byte[] would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace("path2", path2value)
   ** </pre>
   ** where path2value is a byte[] would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link byte[]}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final byte[] value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a byte[] value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"AjIzLg=="}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace(Path.from("path1"), path1value)
   ** </pre>
   ** where path1value is a byte[] would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace(Path.from("path2"), path2value)
   ** </pre>
   ** where path2value is a byte[] would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link byte[]}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final Path path, final byte[] value)
    throws ServiceException {

    return replace(path, Support.jsonNodeFactory().binaryNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a URI value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"http://localhost:8080/uri/One"}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace("path1", path1value)
   ** </pre>
   ** where path1value is a URI would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace("path2", path2value)
   ** </pre>
   ** where path2value is a URI would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final URI value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Adds or replaces a URI value at the provided path.
   ** <br>
   ** For example:
   ** A GenericResource (gr) representing the following resource:
   ** <pre>
   **   {"path1":"http://localhost:8080/uri/One"}
   ** </pre>
   ** then
   ** <pre>
   **    gr.replace(Path.from("path1"), path1value)
   ** </pre>
   ** where path1value is a URI would change the "path1" field to the value
   ** of the path1value variable.
   ** <pre>
   **    gr.replace(Path.from("path2"), path2value)
   ** </pre>
   ** where path2value is a URI would add a field called "path2" with the
   ** value of the path2value variable.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** In a case where multiple paths match (for example a path with a filter),
   ** all paths that match will be affected.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final Path path, final URI value)
    throws ServiceException {

    return replace(path, Support.jsonNodeFactory().textNode(value.toString()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Update the value at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#replaceValue(Path, ObjectNode, JsonNode)} method:
   ** Support.replaceValues(Path.from(path), objectNode(), value).
   ** <p>
   ** The {@link Support#valueToNode(Object)} method may be used to convert the
   ** given value instance to a JSON node.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public GenericResource replace(final String path, final JsonNode value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Update the value at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#replaceValue(Path, ObjectNode, JsonNode)} method:
   ** Support.replaceValues(Path.from(path), objectNode(), value).
   ** <p>
   ** The {@link Support#valueToNode(Object)} method may be used to convert the
   ** given value instance to a JSON node.
   **
   ** @param  path               the path targeted to replace the value for.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the {@link GenericResource} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public GenericResource replace(final Path path, final JsonNode value)
    throws ServiceException {

    Support.replaceValue(path, this.objectNode, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes values at the provided path.
   ** <br>
   ** Equivalent to using the {@link Support#removeValue(Path, ObjectNode)}
   ** method:
   ** <br>
   ** Support.removeValue(Path.from(path), objectNode(), values).
   **
   ** @param  path               the path to the attribute whose values to
   **                            remove.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @return                    whether one or more values where removed.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public boolean remove(final String path)
    throws ServiceException {
    return remove(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes values at the provided path.
   ** <br>
   ** Equivalent to using the {@link Support#removeValue(Path, ObjectNode)} method:
   ** Support.removeValue(Path.from(path), objectNode(), values).
   **
   ** @param  path               the path to the attribute whose values to
   **                            remove.
   **                            The path must not be <code>null</code>.
   **                            Allowed object is {@link Path}.
   ** @return                    whether one or more values where removed.
   **
   ** @throws ServiceException   if the filter derived from the path isn't valid
   **                            for matching.
   */
  public boolean remove(final Path path)
    throws ServiceException {

    final List<JsonNode> nodes = Support.removeValue(path, this.objectNode);
    return !nodes.isEmpty();
  }
}