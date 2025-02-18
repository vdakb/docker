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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   Configuration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Configuration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model;

import java.io.Serializable;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

////////////////////////////////////////////////////////////////////////////////
// class Entity
// ~~~~~ ~~~~~~
/**
 ** Displays version informations of this module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Entity implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String                  ID          = "id";
  public static final String                  NAME        = "name";
  public static final String                  ICON        = "icon";
  public static final String                  HOVER       = "hoverIcon";
  public static final String                  DISABLED    = "disabledIcon";
  public static final String                  DEPRESSED   = "depressedIcon";
  public static final String                  DESCRIPTION = "description";
  public static final String                  TOPIC       = "topic";
  public static final String                  PRODUCT     = "product";

  private static final DocumentBuilderFactory factory          = DocumentBuilderFactory.newInstance();
  private static final DocumentBuilder        builder;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8592962725627570114")
  private static final long                   serialVersionUID = -2969782397173180138L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                              id;
  private String                              name;
  private String                              description;
  private String                              icon;
  private String                              hoverIcon;
  private String                              depressedIcon;
  private String                              disabledIcon;
  private String                              topic;
  private String                              product;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      builder = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entity</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Entity() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entity</code> with properties defined by the specified
   ** {@link Element}.
   **
   ** @param  element            the {@link Element} providing the values for
   **                            the properties.
   */
  public Entity(final Element element) {
    // ensure inheritance
    super();

    this.id            = element(element, ID,          String.class);
    this.name          = element(element, NAME,        String.class);
    this.icon          = element(element, ICON,        String.class);
    this.disabledIcon  = element(element, DISABLED,    String.class);
    this.depressedIcon = element(element, DEPRESSED,   String.class);
    this.description   = element(element, DESCRIPTION, String.class);
    this.topic         = element(element, TOPIC,       String.class);
    this.product       = element(element, PRODUCT,     String.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId
  /**
   ** Sets the value of the id property.
   **
   ** @param  value              the value of the id property.
   **                            Allowed object is {@link String}.
   */
  public void setId(final String value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
   ** Returns the value of the id property.
   **
   ** @return                    the value of the id property.
   **                            Possible object is {@link String}.
   */
  public String getId() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              the value of the description property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the value of the description property.
   **
   ** @return                    the value of the description property.
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIcon
  /**
   ** Sets the value of the icon property.
   **
   ** @param  value              the value of the icon property.
   **                            Allowed object is {@link String}.
   */
  public void setIcon(final String value) {
    this.icon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon
  /**
   ** Returns the value of the icon property.
   **
   ** @return                    the value of the icon property.
   **                            Possible object is {@link String}.
   */
  public String getIcon() {
    return this.icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHoverIcon
  /**
   ** Sets the value of the hoverIcon property.
   **
   ** @param  value              the value of the hoverIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setHoverIcon(final String value) {
    this.hoverIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHoverIcon
  /**
   ** Returns the value of the hoverIcon property.
   **
   ** @return                    the value of the hoverIcon property.
   **                            Possible object is {@link String}.
   */
  public String getHoverIcon() {
    return this.hoverIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDepressedIcon
  /**
   ** Sets the value of the depressedIcon property.
   **
   ** @param  value              the value of the depressedIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setDepressedIcon(final String value) {
    this.depressedIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDepressedIcon
  /**
   ** Returns the value of the depressedIcon property.
   **
   ** @return                    the value of the depressedIcon property.
   **                            Possible object is {@link String}.
   */
  public String getDepressedIcon() {
    return this.depressedIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisabledIcon
  /**
   ** Sets the value of the disabledIcon property.
   **
   ** @param  value              the value of the disabledIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setDisabledIcon(final String value) {
    this.disabledIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisabledIcon
  /**
   ** Returns the value of the disabledIcon property.
   **
   ** @return                    the value of the disabledIcon property.
   **                            Possible object is {@link String}.
   */
  public String getDisabledIcon() {
    return this.disabledIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTopic
  /**
   ** Sets the value of the topic property.
   **
   ** @param  value              the value of the topic property.
   **                            Allowed object is {@link String}.
   */
  public void setTopic(final String value) {
    this.topic = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTopic
  /**
   ** Returns the value of the topic property.
   **
   ** @return                    the value of the topic property.
   **                            Possible object is {@link String}.
   */
  public String getTopic() {
    return this.topic;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProduct
  /**
   ** Sets the value of the product property.
   **
   ** @param  value              the value of the product property.
   **                            Allowed object is {@link String}.
   */
  public void setProduct(final String value) {
    this.product = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProduct
  /**
   ** Returns the value of the product property.
   **
   ** @return                    the value of the product property.
   **                            Possible object is {@link String}.
   */
  public String getProduct() {
    return this.product;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Indicates whether both objects are "equal to" the other one.
   **
   ** @param  object1           the firs reference object with which to compare.
   ** @param  object2           the firs reference object with which to compare.
   **
   ** @return                   <code>true</code> if the objects are the same;
   **                           <code>false</code> otherwise.
   */
  public static final boolean equal(final Object object1, final Object object2) {
    if (object1 == object2)
      return true;

    if (object1 == null || object2 == null)
      return false;

    return object1.equals(object2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extrcat
  public static <T> T element(final Element element, final Class<T> clazz, final T dephault) {
    final T object = element(element, clazz);
    return (object == null) ? object : dephault;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  public static <T> T element(final Element element, final Class<T> clazz) {
    if (element == null)
      return null;

    if (clazz == null)
      throw new NullPointerException("clazz");

    return element(element, name(clazz), clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  public static <T> T extrcat(final Element element, final String name, final Class<T> clazz, final T dephault) {
    final T object = element(element, name, clazz);
    return (object == null) ? object : dephault;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  public static <T> T element(Element element, String name, Class<T> clazz) {
    if (element == null)
      return null;

    if (name == null)
      throw new NullPointerException("name");
    if (clazz == null)
      throw new NullPointerException("clazz");

    Node child = element.getFirstChild();
    while (child != null) {
      if ((child.getNodeType() == 1) && (name.equals(((Element)child).getTagName())))
        return toObject((Element)child, clazz);

      child = child.getNextSibling();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  public static String element(final Element element, final String name, final String dephault) {
    final String string = element(element, name);
    return (string == null) ? string : dephault;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  public static String element(final Element element, final String name) {
    if (element == null)
      return null;

    if (name == null)
      throw new NullPointerException("name");

    Node child = element.getFirstChild();
    while (child != null) {
      if ((child.getNodeType() == 1) && (name.equals(((Element)child).getTagName())))
        return child.getTextContent();

      child = child.getNextSibling();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  public static <T> T attribute(final Element element, final Class<T> clazz, final T dephault) {
    final T object = attribute(element, clazz);
    return (object == null) ? object : dephault;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  public static <T> T attribute(final Element element, final Class<T> clazz) {
    if (element == null)
      return null;

    if (clazz == null)
      throw new NullPointerException("clazz");

    return attribute(element, name(clazz), clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  public static <T> T attribute(final Element element, final String name, final Class<T> clazz, final T dephault) {
    final T object = attribute(element, name, clazz);
    return (object == null) ? object : dephault;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  public static <T> T attribute(final Element element, final String name, final Class<T> clazz) {
    if (element == null)
      return null;

    if (name == null)
      throw new NullPointerException("name");


    if (clazz == null)
      throw new NullPointerException("clazz");

    return (!element.hasAttribute(name)) ? null : toObject(element.getAttribute(name), clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  public static String attribute(final Element element, final String name, final String dephault) {
    final String string = attribute(element, name);
    return (string == null) ? string : dephault;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  public static String attribute(final Element element, final String name) {
    if (element == null)
      return null;

    if (name == null)
      throw new NullPointerException("name");

    return (!element.hasAttribute(name)) ? null : element.getAttribute(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  public static String name(final Object object) {
    return (object == null) ? null : name(object.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  public static String name(final Class<?> clazz) {
    if (clazz == null)
      return null;

    final StringBuffer name = new StringBuffer(clazz.getName());
    name.delete(0, name.lastIndexOf(".") + 1);
    if (name.charAt(name.length() - 1) == ';') {
      name.setLength(name.length() - 1);
      switch (name.charAt(name.length() - 1)) {
        case 'S' :
        case 'X' :
        case 'Z' :
        case 's' :
        case 'x' :
        case 'z' : name.append("es");
                   break;
        default  : name.append("s");
      }
    }
    for (int index = 0; index < name.length(); index++)
      if (name.charAt(index) == '$')
        name.setCharAt(index, '-');
    return name.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toObject
  public static <T> T toObject(final Element element, final Class<T> clazz) {
    if (element == null)
      return null;

    if (clazz == null)
      throw new NullPointerException("clazz");

    if (clazz.isInstance(element))
      return (T)element;

    try {
      Constructor<T> constructor = clazz.getConstructor(new Class<?>[] { Element.class });
      return constructor.newInstance(new Object[] { element });
    }
    catch (NoSuchMethodException nsme) {
      // intentionally left blank
      ;
    }
    catch (Exception e) {
      throw new RuntimeException("cannot construct object from " + Element.class + " for class " + clazz, e);
    }

    try {
      final Method method = clazz.getMethod("valueOf", new Class<?>[] { Element.class });
      return (T)method.invoke(null, new Object[] { element });
    }
    catch (NoSuchMethodException nsme) {
      // intentionally left blank
      ;
    }
    catch (Exception e) {
      throw new RuntimeException("cannot construct object from " + Element.class + " for class " + clazz, e);
    }
    return toObject(element.getTextContent(), clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toObject
  public static <T> T toObject(final String string, final Class<T> clazz) {
    if (string == null)
      return null;

    if (clazz == null)
      throw new NullPointerException("clazz");

    if (clazz.isInstance(string))
      return (T)string;

    try {
      final Method method = clazz.getMethod("valueOf", new Class<?>[] { String.class });
      return (T)method.invoke(null, new Object[] { string });
    }
    catch (NoSuchMethodException nsme) {
      // intentionally left blank
      ;
    }
    catch (Exception e) {
      throw new RuntimeException("cannot construct object from " + String.class + " for class " + clazz, e);
    }

    try {
      final Constructor<T> constructor = clazz.getConstructor(new Class<?>[] { String.class });
      return constructor.newInstance(new Object[] { string });
    }
    catch (NoSuchMethodException nsme) {
      // intentionally left blank
      ;
    }
    catch (Exception e) {
      throw new RuntimeException("cannot construct object from " + String.class + " for class " + clazz, e);
    }
    throw new RuntimeException("cannot construct object from " + String.class + " for class " + clazz);
  }
}