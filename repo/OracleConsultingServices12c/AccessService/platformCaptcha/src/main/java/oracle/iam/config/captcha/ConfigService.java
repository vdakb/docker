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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Captcha Platform Feature

    File        :   ConfigService.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigService.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.io.Reader;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import oracle.hst.platform.core.utility.SystemProperty;

import oracle.iam.platform.core.captcha.color.Space;

import oracle.iam.platform.core.captcha.filter.FilterFactory;

import oracle.iam.config.captcha.type.Limit;
import oracle.iam.config.captcha.type.Margin;
import oracle.iam.config.captcha.type.Canvas;
import oracle.iam.config.captcha.type.Filter;
import oracle.iam.config.captcha.type.Renderer;
import oracle.iam.config.captcha.type.ColorScheme;

////////////////////////////////////////////////////////////////////////////////
// class ConfigService
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** MXBean implementation for managing Visual Captcha Service configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ConfigService {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name of the MBean exposed in the JNDI tree of the Fusion Middleware
   ** Runtime configuration.
   */
  public static final ObjectName      CAPTCHA;

  /**
   ** The name of the system property a domain stores the Fusion Middleware
   ** Runtime configuration.
   */
  private static final SystemProperty PATH           = SystemProperty.Builder.of(String.class)
   .name("captchaConfig")
   .defaultValue("oracle.domain.config.dir")
   .build()
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      CAPTCHA = new ObjectName("ocs.iam.platform:Name=config,Type=Captcha");
    }
    catch (MalformedObjectNameException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ConfigService</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ConfigService() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  public static Config config() {
    return Config.build();
  }

  public static Config config(final InputStream stream) {
    final JsonObject root = Json.createReader(stream).readObject();
    return Config.build(canvas(root), renderer(root)).schema(colorScheme(root)).filter(filter(root));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Creates a JSON parser from a character strea and parse a payload in JSON
   ** format and return the appropriate key/value mapping.
   **
   **
   ** @param  stream             an i/o stream from which JSON is to be read.
   **                            <br>
   **                            Allowed object is {@link Reader}.
   **
   ** @return                    the appropriate key/value mapping.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject parse(final Reader stream) {
    return Json.createReader(stream).readObject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canvas
  /**
   ** Factory method to create a {@link Canvas} by parsing the specified
   ** {@link JsonObject}.
   **
   ** @param  config             the canvas configuration as JSON
   **                            representation.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the parsed {@link Canvas} configuration.
   **                            <br>
   **                            Possible object is {@link Canvas}.
   */
  public static Canvas canvas(final JsonObject config) {
    // setup a canvas with default values populated
    final Canvas entity = Canvas.build();
    JsonObject   cursor = config.getJsonObject(Canvas.ENTITY);
    // if there isn't a value with the entity name skip any effort to parse
    if (cursor != null) {
      entity.width(cursor.getInt(Canvas.WIDTH, entity.size().width())).height(cursor.getInt(Canvas.HEIGHT, entity.size().height()));
      cursor = cursor.getJsonObject(Margin.ENTITY);
      if (cursor != null) {
        entity.margin(Margin.build(cursor.getInt("top", entity.margin().top()), cursor.getInt("left", entity.margin().left()), cursor.getInt("bottom", entity.margin().bottom()), cursor.getInt("right", entity.margin().right())));
      }
    }
    return entity;
  }

  private static Renderer renderer(final JsonObject config) {
    // setup a renderer with default values populated
    final Renderer entity = Renderer.build();
    JsonObject     cursor = null;
    JsonObject     value  = config.getJsonObject("renderer");
    if (value != null) {
      cursor  = value.getJsonObject("length");
      if (cursor != null) {
        entity.length(Limit.build(cursor.getInt("lower", entity.length().lower()), cursor.getInt("upper", entity.length().upper())));
      }
      cursor = value.getJsonObject("font");
      if (cursor != null) {
        cursor = cursor.getJsonObject("family");
//        if (value != null) {
//          JsonArray family = value.asArray();
//        }
        cursor = cursor.getJsonObject("size");
        if (cursor != null) {
          entity.fontSize(Limit.build(cursor.getInt("lower", entity.fontSize().lower()), cursor.getInt("upper", entity.fontSize().upper())));
        }
      }
    }
    return entity;
  }

  private static Filter filter(final JsonObject config) {
    final Filter entity = Filter.build();
    JsonArray    filter  = config.getJsonArray("filter");
    if (filter != null) {
      final FilterFactory[] factory = new FilterFactory[filter.size()];
      for (int i = 0; i  < filter.size(); i++) {
        try {
          final Class<?>      clazz    = ConfigService.class.getClassLoader().loadClass(filter.get(i).toString());
          final Method        method   = clazz.getMethod("build", null);
          factory[i] = (FilterFactory)method.invoke(null, null);
        }
        catch (InvocationTargetException e) {
          // intentionally left blank
          e.printStackTrace();
        }
        catch (IllegalAccessException e) {
          // intentionally left blank
          e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
          // intentionally left blank
          e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
          // intentionally left blank
          e.printStackTrace();
        }
      }
      entity.factory(factory);
    }
    return entity;
  }

  private static ColorScheme colorScheme(final JsonObject config) {
    final ColorScheme entity  = ColorScheme.build();
    final JsonObject  palette = config.getJsonObject("palette");
    if (palette != null) {
      JsonArray cursor = palette.getJsonArray("foreground");
      if (cursor != null)
        entity.foreground(color(cursor));
      cursor = palette.getJsonArray("background");
      if (cursor != null)
        entity.background(color(cursor));
    }
    return entity;
  }

  private static Space[] color(final JsonArray config) {
    final Space[] color = new Space[config.size()];
    for (int i = 0; i  < config.size(); i++) {
      final JsonObject item = config.getJsonObject(i);
      color[i] = Space.rgb(item.getInt("r"), item.getInt("g"), item.getInt("b"));
    }
    return color;
  }
}
