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

    File        :   JsonTrimmer.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    JsonTrimmer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.core.marshal;

import java.util.Set;
import java.util.Map;
import java.util.Collections;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.entity.Path;

////////////////////////////////////////////////////////////////////////////////
// class JsonTrimmer
// ~~~~~ ~~~~~~~~~~~
/**
 ** Utility implementing the REST standard for returning attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JsonTrimmer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final boolean   excluded;
  private final Set<Path> request;
  private final Set<Path> attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>JsonTrimmer</code> for trimming returned
   ** attributes for a REST operation.
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
   */
  private JsonTrimmer(final Set<Path> request, final Set<Path> attribute, final boolean excluded) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.request   = request;
    this.attribute = attribute;
    this.excluded  = excluded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Returns the <code>JsonTrimmer</code> based on the passed value.
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
  public static JsonTrimmer build(final Set<Path> attribute, final boolean excluded) {
    return build(Collections.<Path>emptySet(), attribute, excluded);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Returns the <code>JsonTrimmer</code> based on the passed value.
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
  public static JsonTrimmer build(final Set<Path> request, final Set<Path> attribute, final boolean excluded) {
    return new JsonTrimmer(request, attribute, excluded);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Trim attributes of the object node to return.
   **
   ** @param  node               the object node to return.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the trimmed object node ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public JsonObject trim(final JsonObject node) {
    return trim(node, Path.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Trim attributes of an inner object node to return.
   **
   ** @param  node               the object node to return.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  parentPath         the parent path of attributes in the object.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the trimmed object node ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  private JsonObject trim(final JsonObject node, final Path parentPath) {
    final JsonObjectBuilder trimmed = Json.createObjectBuilder();
    for (Map.Entry<String, JsonValue> cursor : node.entrySet()) {
      Path path;
      if (parentPath.root()) {
        path = Path.build(cursor.getKey());
      }
      else {
        path = parentPath.path(cursor.getKey());
      }
      if (path.root() || returned(path)) {
        switch(cursor.getValue().getValueType()) {
          case ARRAY  : final JsonArray a = trim((JsonArray)cursor.getValue(), path);
                        if (a.size() > 0) {
                          trimmed.add(cursor.getKey(), a);
                        }
                        break;
          case OBJECT : final JsonObject o = trim((JsonObject)cursor.getValue(), path);
                        if (o.size() > 0) {
                          trimmed.add(cursor.getKey(), o);
                        }
                        break;
          default     : trimmed.add(cursor.getKey(), cursor.getValue());
        }
      }
    }
    return trimmed.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Trim attributes of the values in the array node to return.
   *
   ** @param  node               the array node to return.
   **                            <br>
   **                            Allowed object is {@link JsonArray}.
   ** @param  parentPath         the parent path of attributes in the array.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the trimmed array node ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link JsonArray}.
   */
  private JsonArray trim(final JsonArray node, final Path parentPath) {
    final JsonArrayBuilder result = Json.createArrayBuilder();
    for (JsonValue value : node) {
      switch (value.getValueType()) {
        case ARRAY  : final JsonArray a = trim((JsonArray)value, parentPath);
                      if (a.size() > 0)
                       result.add(a);
                      break;
        case OBJECT : final JsonObject o = trim((JsonObject)value, parentPath);
                      if (o.size() > 0)
                       result.add(o);
                      break;
        default     : result.add(value);
      }
    }
    return result.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returned
  /**
   ** Determine if the attribute specified by <code>path</code> should be
   ** returned.
   **
   ** @param  path               the path for the attribute.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    <code>true</code> to return the attribute or
   **                            <code>false</code> to remove the attribute from
   **                            the returned resource..
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean returned(final Path path) {
    // return if it is not one of the excluded query attributes and no override
    // query attributes are provided.
    // if override query attributes are provided, only return if it is one of
    // them.
    if (this.excluded) {
      return !match(this.attribute, path);
    }
    else {
      return this.attribute == null || this.attribute.isEmpty() || match(this.attribute, path);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  private boolean match(final Set<Path> paths, final Path path) {
    // exact path match
    if (paths.contains(path))
      return true;

    if (!this.excluded) {
      // see if a sub-attribute of the given path is included in the list
      // ie. include name if name.givenName is in the list.
      for (Path p : paths) {
        if (p.size() > path.size() && path.equals(p.sub(path.size()))) {
          return true;
        }
      }
    }
    // see if the parent attribute of the given path is included in the list
    // ie. include name.{anything} if name is in the list.
    for (Path p = path; p.size() > 0; p = p.sub(p.size() - 1)) {
      if (paths.contains(p)) {
        return true;
      }
    }
    return false;
  }
}