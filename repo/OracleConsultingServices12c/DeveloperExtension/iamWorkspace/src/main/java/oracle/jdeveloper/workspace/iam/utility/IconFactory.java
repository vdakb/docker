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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   IconFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IconFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

import java.lang.reflect.Field;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.awt.Color;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Transparency;
import java.awt.RenderingHints;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.GrayFilter;
import javax.swing.SwingConstants;

import javax.imageio.ImageIO;

////////////////////////////////////////////////////////////////////////////////
// class IconFactory
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>IconFactory</code> provides a consistent way to access icon resource
 ** in any application.
 ** <p>
 ** It also the factory class for vending various <code>Image</code> or
 ** <code>ImageIcon</code> objects.
 ** <p>
 ** Any application usually need to access image files. One way to do it is to
 ** put those image files in the installation and access them use direct file
 ** access. However this is not a good way because you have to know the full
 ** path to the image file. So a better way that most Java applications take is
 ** to bundle the image files with in the jar and use class loader to load them.
 ** <p>
 ** For example, if a class Foo needs to access image files foo.gif and bar.png,
 ** we put the image files right below the source code under icons subfolder.
 ** See an example directory structure below.
 ** <pre>
 ** /src/oracle/jdeveloper/workspace/Foo.java
 **                  /icons/foo.gif
 **                  /icons/bar.png
 ** </pre>
 ** When you compile the java class, you copy those images to class output
 ** directory like this.
 ** <pre>
 ** /bim/oracle/jdeveloper/workspace/Foo
 **                      /icons/foo.gif
 **                      /icons/bar.png
 ** </pre>
 ** <pre>
 ** ImageIcon icon = IconFactory.get(Foo.class, "icons/foo.gif");
 ** </pre>
 ** IconFactory will cache the icon for you. So next time if you get the same
 ** icon, it will get from cache instead of reading from disk again.
 ** <p>
 ** There are a few methods on IconFactory to create difference variation from
 ** the original icon. For example, {@link #disabled(Class,String)} will get the
 ** imaage icon with disabled effect.
 ** <p>
 ** We also suggest you to use the template below to create a number of
 ** IconFactory classes in your application. The idea is that you should have
 ** one for each functional area so that all your image files can be grouped
 ** into each functional area. All images used in that functional area should be
 ** put under the folder where this IconFactory is. Here is an template.
 ** <pre>
 ** class TemplateIconFactory {
 **   public static class Group1 {
 **     public final static String IMAGE1 = "icons/image11.png";
 **     public final static String IMAGE2 = "icons/image12.png";
 **     public final static String IMAGE3 = "icons/image13.png";
 **   }
 **
 **   public static class Group2 {
 **     public final static String IMAGE1 = "icons/image21.png";
 **     public final static String IMAGE2 = "icons/image22.png";
 **     public final static String IMAGE3 = "icons/image23.png";
 **   }
 **
 **   public static ImageIcon getImageIcon(String name) {
 **     if (name != null)
 **       return IconFactory.getImageIcon(TemplateIconFactory.class, name);
 **     else
 **       return null;
 **   }
 **
 **   public static void main(String[] argv) {
 **     IconFactory.generateHTML(TemplateIconFactory.class);
 **   }
 ** }
 ** </pre>
 ** In your own IconFactory, you can further divide images into different
 ** groups. The example above has two groups. There is also a convenient method
 ** getImageIcon() which takes just the icon name.
 ** <p>
 ** In the template, we defined the image names as constants. When you have a
 ** lot of images, it's hard to remember all of them when writing code. If using
 ** the IconFactory above, you can use
 ** <pre>
 ** ImageIcon icon = TemplateIconFactory.getImageIcon(TemplateIconFactory.Group1.IMAGE1);
 ** </pre>
 ** without saying the actual image file name. With the help of code completion
 ** feature, you will find it is much easier to find the icons you want.
 ** <p>
 ** You probably also notice this is a main() method in this template. You can
 ** run it. When you run, you will see a message printed out like this.
 ** <pre>
 ** "File is generated at "... some directory ...\oracle.jdeveloper.workspace.icons.TemplateIconFactory.html".
 ** Please copy it to the same directory as TemplateIconFactory.java"
 ** </pre>
 ** if you follow the instruction and copy the html file to the same location as
 ** the source code and open the html, you will see the all image files defined
 ** in this IconFactory are listed nicely in the page.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class IconFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Color     TRANSPERENT = new Color( 0, 0, 0, 0 );
  public final static ImageIcon EMPTY       = new ImageIcon() {

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-7521275182664307135")
    private static final long   serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by funtionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getIconWidth (Icon)
    /**
     ** Returns the icon's width.
     **
     ** @return                  an int specifying the fixed width of the icon.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int getIconWidth() {
      return 16;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getIconHeight (Icon)
    /**
     ** Returns the icon's height.
     **
     ** @return                  an int specifying the fixed height of the icon.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int getIconHeight() {
      return 16;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: paintIcon (Icon)
    /**
     ** Draw the icon at the specified location.
     ** <p>
     ** Icon implementations may use the Component argument to get properties
     ** useful for  painting, e.g. the foreground or background color.
     **
     ** @param  component        the component to be used as the observer if
     **                          this icon has no image observer.
     **                          <br>
     **                          Allowed object is {@link Component}.
     ** @param  graphics         the graphics context.
     **                          <br>
     **                          Allowed object is {@link Graphics}.
     ** @param  x                the X coordinate of the icon's top-left corner.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  y                the Y coordinate of the icon's top-left corner.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    @Override
    public synchronized void paintIcon(final Component component, final Graphics graphics, final int x, final int y) {
      // intentionally left blank
    }
  };

  static final double                   DEGREE_90     = 90.0 * Math.PI / 180.0;

  static final Map<CacheKey, ImageIcon> icons         = new HashMap<CacheKey, ImageIcon>();
  static final Map<CacheKey, ImageIcon> disableIcons  = new HashMap<CacheKey, ImageIcon>();
  static final Map<CacheKey, ImageIcon> enhancedIcons = new HashMap<CacheKey, ImageIcon>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class CacheKey
  // ~~~~~ ~~~~~~~~
  /**
   ** Implementation of key that is unique across all subclasses
   ** flow.
   */
  private static final class CacheKey {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String name;
    private final String key;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Default Constructor.
     */
    CacheKey(final String name, final String key) {
      this.name = name;
      this.key  = key;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code for this instance.
     **
     ** @return                  a hash code value for this instance.
     */
    @Override
    public int hashCode() {
      return this.name.hashCode() + this.key.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one.
     **
     ** @param  object           the reference object with which to compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **
     ** @see    Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object) {
      if (object == this)
        return true;

      if (object instanceof CacheKey) {
        CacheKey other = (CacheKey)object;
        if (other.name.equals(this.name) && other.key.equals(this.key))
          return true;
      }
      return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   toString (overridden)
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                  the string representation of this instance.
     */
    @Override
    public String toString() {
      return this.name + ":" + this.key;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class DrawOptions
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** Image option builder.
   */
  public static class DrawOptions implements Cloneable {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Color           fill;
    private Color           draw;
    private Color           background;
    private Stroke          stroke;
    private Dimension       size;
    private Insets          margin;
    private AffineTransform transform;

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: clone
    @Override
    public DrawOptions clone ()
      throws CloneNotSupportedException {

      return (DrawOptions)super.clone();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions fill(final Color fill) {
      this.fill = fill;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions draw(final Color draw) {
      this.draw = draw;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions background(final Color background) {
      this.background = background;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions stroke(final float stroke) {
      return stroke(new BasicStroke(stroke));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions stroke(final Stroke stroke) {
      this.stroke = stroke;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions size(final Dimension size) {
      this.size = size;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions size(final int width, final int height) {
      return size(new Dimension(width, height));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions margin(final int margin) {
      return margin(margin, margin, margin, margin);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions margin(final int top, final int left, final int bottom, final int right) {
      return margin(new Insets(top, left, bottom, right));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions margin(final Insets margin) {
      this.margin = margin;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fill
    public DrawOptions transform(final AffineTransform transform) {
      this.transform = transform;
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Returns {@link ImageIcon} by passing class and a relative image file path.
   ** <p>
   ** Please note, create will print out error message to stderr if image is not
   ** found. The reason we did so is because we want you to make sure all image
   ** files are there in your application. If you ever see the error message,
   ** you should correct it before shipping the product. But if you just want to
   ** test if the image file is there, you don't want any error message print
   ** out. If so, you can use {@link #find(Class,String)} method. It will throw
   ** {@link IOException} when image is not found.
   **
   ** @param  clazz              the Class
   ** @param  fileName           the relative file name to <code>clazz</code>.
   **
   ** @return                    the {@link ImageIcon}
   */
  public static ImageIcon create(final Class<?> clazz, final String fileName) {
    final CacheKey id = new CacheKey(clazz.getName(), fileName);
    ImageIcon icon = icons.get(id);
    if (icon != null)
      return icon;

    icon = createImage(clazz, fileName);
    icons.put(id, icon);
    return icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns {@link ImageIcon} by passing class and a relative image file path.
   **
   ** @param  clazz              the Class
   ** @param  fileName           relative file name
   **
   ** @return                    the {@link ImageIcon}
   **
   ** @throws IOException        when image file is not found.
   */
  public static ImageIcon find(Class<?> clazz, String fileName)
    throws IOException {

    final CacheKey id = new CacheKey(clazz.getName(), fileName);
    ImageIcon icon = icons.get(id);
    if (icon != null)
      return icon;

    icon = createImageWithException(clazz, fileName);
    icons.put(id, icon);
    return icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disabled
  /**
   ** Returns a disabled version of {@link ImageIcon} by passing class and a
   ** relative image file path.
   **
   ** @param  clazz              the Class
   ** @param  fileName           the relative file name to <code>clazz</code>.
   **
   ** @return                    the {@link ImageIcon}
   */
  public static ImageIcon disabled(final Class<?> clazz, final String fileName) {
    final CacheKey id = new CacheKey(clazz.getName(), fileName);
    ImageIcon icon = disableIcons.get(id);
    if (icon != null)
      return icon;

    icon = grayed(create(clazz, fileName));
    disableIcons.put(id, icon);
    return icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Returns a brighter {@link ImageIcon} by passing class and a relative image
   ** file path.
   **
   ** @param  clazz              the Class
   ** @param  fileName           the relative file name to <code>clazz</code>.
   **
   ** @return                    the {@link ImageIcon}
   */
  public static ImageIcon brighter(Class<?> clazz, String fileName) {
    final CacheKey id = new CacheKey(clazz.getName(), fileName);
    ImageIcon icon = enhancedIcons.get(id);
    if (icon != null)
      return icon;

    icon = brighter(create(clazz, fileName));
    enhancedIcons.put(id, icon);
    return icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Returns a brighter {@link ImageIcon} by passing class, a relative image
   ** file path and a percentage of brightness.
   **
   ** @param  clazz              the Class
   ** @param  fileName           the relative file name to <code>clazz</code>.
   ** @param  percent            the percentage of brightness
   **
   ** @return                    the {@link ImageIcon}
   */
  public static ImageIcon brighter(Class<?> clazz, String fileName, int percent) {
    final CacheKey id = new CacheKey(clazz.getName(), fileName);
    ImageIcon icon = enhancedIcons.get(id);
    if (icon != null)
      return icon;

    icon = brighter(create(clazz, fileName), percent);
    enhancedIcons.put(id, icon);
    return icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Returns a brighter {@link ImageIcon} from an input {@link Image}.
   ** <p>
   ** If input image is <code>null</code>, a blank {@link ImageIcon} will be
   ** returned.
   **
   ** @param  origin             the original image
   **
   ** @return                    a dimmed version of the supplied {@link Image}.
   */
  public static ImageIcon brighter(final Image origin) {
    if (origin == null)
      return EMPTY;

    return new ImageIcon(ColorFilter.brighter(origin));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Returns a brighter {@link ImageIcon} from an input {@link Image} with a
   ** given percentage of brightness.
   ** <p>
   ** If input image is <code>null</code>, a blank {@link ImageIcon} will be
   ** returned.
   **
   ** @param  origin             the original image
   ** @param  percent            the percentage of brightness
   **
   ** @return                    a dimmed version of the supplied {@link Image}.
   */
  public static ImageIcon brighter(final Image origin, final int percent) {
    if (origin == null)
      return EMPTY;

    return new ImageIcon(ColorFilter.brighter(origin, percent));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Returns a brighter {@link ImageIcon} from an input {@link Icon}. Usually
   ** gray icon indicates disabled.
   ** <p>
   ** If input icon is <code>null</code>, a blank {@link ImageIcon} will be
   ** returned.
   **
   ** @param  component          the component to get properties useful for
   **                            painting, e.g. the foreground or background
   **                            color.
   ** @param  origin             the original image
   **
   ** @return                    a dimmed version of the supplied {@link Icon}.
   */
  public static ImageIcon brighter(final Component component, final Icon origin) {
    if (origin == null)
      return EMPTY;

    BufferedImage image = new BufferedImage(origin.getIconWidth(), origin.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    origin.paintIcon(component, image.getGraphics(), 0, 0);
    return new ImageIcon(ColorFilter.brighter(image));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Returns a brighter {@link ImageIcon} from an input {@link Icon} with a
   ** given percentage of brightness. Usually gray icon indicates disabled.
   ** <p>
   ** If input icon is <code>null</code>, a blank {@link ImageIcon} will be
   ** returned.
   **
   ** @param  component          the component to get properties useful for
   **                            painting, e.g. the foreground or background
   **                            color.
   ** @param  origin             the original image
   ** @param  percent            the percentage of brightness
   **
   ** @return                    a dimmed version of the supplied {@link Icon}.
   */
  public static ImageIcon brighter(final Component component, final Icon origin, final int percent) {
    if (origin == null)
      return EMPTY;

    BufferedImage image = new BufferedImage(origin.getIconWidth(), origin.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    origin.paintIcon(component, image.getGraphics(), 0, 0);
    return new ImageIcon(ColorFilter.brighter(image, percent));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighterImage
  /**
   ** Returns a brighter {@link ImageIcon} from an input {@link ImageIcon}.
   ** <p>
   ** If input icon is <code>null</code>, a blank {@link ImageIcon} will be
   ** returned.
   **
   ** @param  origin             the original image
   **
   ** @return                    a dimmed version of the supplied
   **                            {@link ImageIcon}.
   */
  public static ImageIcon brighter(final ImageIcon origin) {
    if (origin == null)
      return EMPTY;

    return new ImageIcon(ColorFilter.brighter(origin.getImage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Returns a brighter {@link ImageIcon} from an input {@link ImageIcon} with
   ** a given percentage of brightness.
   ** <p>
   ** If input icon is <code>null</code>, a blank {@link ImageIcon} will be
   ** returned.
   **
   ** @param  origin             the original image
   ** @param  percent            the percentage of brightness
   **
   ** @return                    a dimmed version of the supplied
   **                            {@link ImageIcon}.
   */
  public static ImageIcon brighter(final ImageIcon origin, final int percent) {
    if (origin == null)
      return EMPTY;

    return new ImageIcon(ColorFilter.brighter(origin.getImage(), percent));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grayed
  /**
   ** Returns a gray version from an input {@link ImageIcon}.
   ** <p>
   ** Usually gray icon indicates disabled. If input icon is null, a blank
   ** {@link ImageIcon} will be returned.
   **
   ** @param  origin             the original image
   **
   ** @return                    a grayed version of the supplied
   **                            {@link ImageIcon}.
   */
  public static ImageIcon grayed(final ImageIcon origin) {
    return grayed(origin.getImage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grayed
  /**
   ** Returns a gray version from an input {@link Image}.
   ** <p>
   ** Usually gray icon indicates disabled. If input image is null, a blank
   ** ImageIcon will be returned.
   **
   ** @param  origin             the original image
   **
   ** @return                    a grayed version of the supplied {@link Image}.
   */
  public static ImageIcon grayed(final Image origin) {
    if (origin == null)
      return EMPTY;

    return new ImageIcon(GrayFilter.createDisabledImage(origin));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grayed
  /**
   ** Returns a gray version from an input {@link Icon}.
   ** <p>
   ** Usually gray icon indicates disabled. If input image is null, a blank
   ** ImageIcon will be returned.
   **
   ** @param  component          the component to get properties useful for
   **                            painting, e.g. the foreground or background
   **                            color.
   ** @param  origin             the original image
   **
   ** @return                    a grayed version of the supplied {@link Icon}.
   */
  public static ImageIcon grayed(final Component component, final Icon origin) {
    if (origin == null)
      return EMPTY;

    int w = origin.getIconWidth(), h = origin.getIconHeight();
    if ((w == 0) || (h == 0))
      return EMPTY;

    BufferedImage image = new BufferedImage(origin.getIconWidth(), origin.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    origin.paintIcon(component, image.getGraphics(), 0, 0);
    return new ImageIcon(GrayFilter.createDisabledImage(image));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invert
  /**
   ** Returns a inverted version from an input {@link Image}black image which
   ** basically replaces black pixel with white pixel.
   **
   ** @param  origin             the original image
   **
   ** @return                    a inverted version of the supplied {@link Icon}.
   */
  public static ImageIcon invert(final Image origin) {
    if (origin == null)
      return EMPTY;

    return new ImageIcon(MaskFilter.invert(origin));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invert
  /**
   ** Returns a inverted version from an input {@link ImageIcon}black image which
   ** basically replaces black pixel with white pixel.
   **
   ** @param  origin             the original image
   **
   ** @return                    a inverted version of the supplied {@link Icon}.
   */
  public static ImageIcon invertImage(final ImageIcon origin) {
    return new ImageIcon(MaskFilter.invert(origin.getImage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invert
  /**
   ** Returns a inverted version from an input {@link Icon} black image which
   ** basically replaces black pixel with white pixel.
   **
   ** @param  component          the component to get properties useful for
   **                            painting, e.g. the foreground or background
   **                            color.
   ** @param  origin             the original image
   **
   ** @return                    a inverted version of the supplied {@link Icon}.
   */
  public static ImageIcon invert(final Component component, final Icon origin) {
    BufferedImage image = new BufferedImage(origin.getIconWidth(), origin.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    origin.paintIcon(component, image.getGraphics(), 0, 0);
    return new ImageIcon(MaskFilter.invert(image));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mask
  /**
   ** Returns a version from an input {@link Icon} which replaces one color
   ** with another color.
   *
   ** @param  component          the component to get properties useful for
   **                            painting, e.g. the foreground or background
   **                            color.
   ** @param  origin             the original image
   ** @param  oldColor           old color in exist image that needs to be
   **                            replaced by new color.
   ** @param  newColor           new color to replace the old color.
   **
   ** @return                    an image after replacing the color.
   */
  public static ImageIcon mask(final Component component, final Icon origin, final Color oldColor, final Color newColor) {
    BufferedImage image = new BufferedImage(origin.getIconWidth(), origin.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    origin.paintIcon(component, image.getGraphics(), 0, 0);
    return new ImageIcon(MaskFilter.create(image, oldColor, newColor));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrowIcon
  /**
   ** Creates an arrow icon
   ** @param direction           the Compass-direction of the arrow as one of
   **                            the following constants defined in
   **                            <code>SwingConstants</code>:
   **                            <ul>
   **                              <li><code>CENTER</code>
   **                              <li><code>NORTH</code>
   **                              <li><code>NORTH_WEST</code>
   **                              <li><code>NORTH_EAST</code>
   **                              <li><code>WEST</code>
   **                              <li><code>SOUTH</code>
   **                              <li><code>SOUTH_EAST</code>
   **                              <li><code>SOUTH_WEST</code>
   **                            </ul>.
   ** @param  length             the length of the arrow to draw.
   ** @param  base               the vertical coordinate.
   ** @param  option             the option how to drwa the arrow shape
   **
   ** @return                    the {@link ImageIcon} with an arrow shape.
   **
   ** @see #arrowImage(int, int, int, DrawOptions)
   */
  public static ImageIcon arrowIcon(final int direction, final int length, final int base, final DrawOptions option) {
    return new ImageIcon(arrowImage(direction, length, base, option));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrowImage
  /**
   ** Creates an arrow icon
   **
   ** @param  direction          the Compass-direction of the arrow as one of
   **                            the following constants defined in
   **                            <code>SwingConstants</code>:
   **                            <ul>
   **                              <li><code>CENTER</code>
   **                              <li><code>NORTH</code>
   **                              <li><code>NORTH_WEST</code>
   **                              <li><code>NORTH_EAST</code>
   **                              <li><code>WEST</code>
   **                              <li><code>SOUTH</code>
   **                              <li><code>SOUTH_EAST</code>
   **                              <li><code>SOUTH_WEST</code>
   **                            </ul>.
   ** @param  length             the length of the arrow to draw.
   ** @param  base               the vertical coordinate.
   ** @param  option             the option how to drwa the arrow shape
   **
   ** @return                    the {@link Image} with an arrow shape.
   */
  public static BufferedImage arrowImage(final int direction, final int length, final int base, final DrawOptions option) {
    return shapeImage(createArrow(direction, length, base), option);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createArrow
  /**
   ** Creates an arrow shape.
   **
   ** @param  direction          the Compass-direction of the arrow as one of
   **                            the following constants defined in
   **                            <code>SwingConstants</code>:
   **                            <ul>
   **                              <li><code>CENTER</code>
   **                              <li><code>NORTH</code>
   **                              <li><code>NORTH_WEST</code>
   **                              <li><code>NORTH_EAST</code>
   **                              <li><code>WEST</code>
   **                              <li><code>SOUTH</code>
   **                              <li><code>SOUTH_EAST</code>
   **                              <li><code>SOUTH_WEST</code>
   **                            </ul>.
   ** @param  length             the length of the arrow to draw.
   ** @param  base               the vertical coordinate.
   **
   ** @return                    an arrow shape.
   */
  public static Shape createArrow(final int direction, final int length, final int base) {
    GeneralPath path = new GeneralPath();
    path.moveTo(0, 0);
    path.lineTo(length - 1, base / 2);
    path.lineTo(0, base - 1);
    path.closePath();

    AffineTransform affine = null;
    switch (direction) {
      case SwingConstants.NORTH      : affine = new AffineTransform();
                                       affine.rotate(-Math.PI / 2, (length - 1) / 2.0, (base - 1) / 2.0);
                                       //tx.translate( -length + 1, 0 );
                                       break;
      case SwingConstants.NORTH_WEST : affine = new AffineTransform();
                                       affine.rotate(-Math.PI * 3 / 4);
                                       affine.translate(-length - 1, -base / 2.0);
                                       break;
      case SwingConstants.NORTH_EAST : affine = new AffineTransform();
                                       affine.rotate(-Math.PI / 4);
                                       affine.translate(-(length - 1) / 4.0, (base - 1) / 4.0);
                                       break;
      case SwingConstants.WEST       : affine = new AffineTransform();
                                       affine.scale(-1, 1);
                                       affine.translate(-length + 1, 0);
                                       break;
      case SwingConstants.SOUTH      : affine = new AffineTransform();
                                       affine.rotate(Math.PI / 2, (length - 1) / 2.0, (base - 1) / 2.0);
                                       //tx.translate( 0, -base + 1 );
                                       break;
      case SwingConstants.SOUTH_EAST : affine = new AffineTransform();
                                       affine.rotate(Math.PI / 4);
                                       affine.translate(length / 2.0, -(base - 1) / 2.0);
                                       break;
      case SwingConstants.SOUTH_WEST : affine = new AffineTransform();
                                       affine.rotate(Math.PI * 3 / 4);
                                       affine.translate(-length / 4.0, -base * 4 / 3);
                                       break;
      case SwingConstants.CENTER     : return new Ellipse2D.Float(0, 0, length, base);
    }

    if (affine != null)
      path.transform(affine);

    return path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shapeIcon
  /**
   ** Creates an arrow icon
   **
   ** @param  shape              the {@link Shape} to draw.
   ** @param  option             the option how to drwa the arrow shape
   **
   ** @return                    the {@link ImageIcon} with an shape.
   **
   ** @see #arrowImage(int, int, int, DrawOptions)
   */
  public static ImageIcon shapeIcon(final Shape shape, final DrawOptions option) {
    return new ImageIcon(shapeImage(shape, option));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shapeImage
  /**
   ** Creates an arrow icon
   **
   ** @param  shape              the {@link Shape} to draw.
   ** @param  option             the option how to drwa the arrow shape
   **
   ** @return                    the {@link ImageIcon} with an shape.
   **
   ** @see #arrowImage(int, int, int, DrawOptions)
   */
  public static BufferedImage shapeImage(final Shape shape, DrawOptions option) {
    if (shape == null)
      throw new IllegalArgumentException("Shape cannot be NULL.");

    if (option == null)
      option = new DrawOptions();

    // image size
    Rectangle rect = shape.getBounds();
    Dimension dim = option.size == null ? new Dimension(rect.width + 1, rect.height + 1) : new Dimension(option.size);

    // margins
    if (option.margin != null) {
      dim.width  += option.margin.left + option.margin.right;
      dim.height += option.margin.top  + option.margin.bottom;
    }

    // buffered image
    BufferedImage image  = createTranslucentImage(dim.width, dim.height);
    Graphics2D    canvas = image.createGraphics();

    // background
    if (option.background != null) {
      canvas.setBackground(option.background);
      canvas.clearRect(0, 0, dim.width, dim.height);
    }

    // stroke
    if (option.stroke != null)
      canvas.setStroke(option.stroke);

    // affine transform
    if (option.transform != null)
      canvas.setTransform(option.transform);

    // Colors
    Color draw = option.draw == null ? (option.fill == null ? Color.black : option.fill) : option.draw;
    Color fill = option.fill == null ? draw : option.fill;

    // Draw
    canvas.translate((dim.width - rect.width) / 2, (dim.height - rect.height) / 2);
    canvas.setColor(fill);
    canvas.fill(shape);
    canvas.setColor(draw);
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    canvas.draw(shape);
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
    canvas.dispose();

    return image;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaled
  /**
   ** Returns a scaled version of the existing {@link ImageIcon}.
   *
   ** @param  component          the component where the returned icon will be
   **                            used. The component is used as the
   **                            ImageObserver. It could be <code>null</code>.
   ** @param  origin             the original image
   ** @param  width              the indented width of the generated image.
   ** @param  height             the indented height of the generated image.
   **
   ** @return                    an image after scaling.
   */
  public static ImageIcon scaled(final Component component, final ImageIcon origin, final int width, final int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D    canvas = image.createGraphics();
    if (width >= origin.getIconWidth() / 2) {
      canvas.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      canvas.drawImage(origin.getImage(), 0, 0, image.getWidth(), image.getHeight(), component);
      canvas.dispose();
      return new ImageIcon(image);
    }
    else {
      canvas.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      canvas.drawImage(origin.getImage(), 0, 0, image.getWidth(), image.getHeight(), component);
      canvas.dispose();
      return new ImageIcon(scaleImage(image, width, height, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rotate
  /**
   ** Returns a rotated version of the input image.
   *
   ** @param  component          the component to get properties useful for
   **                            painting, e.g. the foreground or background
   **                            color.
   ** @param  origin             the original image
   ** @param  rotatedAngle       the rotated angle, in degree, clockwise.
   **                            It could be any double but we will mod it with
   **                            360 before using it.
   **
   ** @return                    an image after rotating.
   */
  public static ImageIcon rotate(final Component component, final Icon origin, final double rotatedAngle) {
    // convert rotatedAngle to a value from 0 to 360
    double originalAngle = rotatedAngle % 360;
    if (rotatedAngle != 0 && originalAngle == 0)
      originalAngle = 360.0;

    // convert originalAngle to a value from 0 to 90
    double angle = originalAngle % 90;
    if (originalAngle != 0.0 && angle == 0.0)
      angle = 90.0;

    double radian = Math.toRadians(angle);

    int w;
    int h;
    int iw = origin.getIconWidth();
    int ih = origin.getIconHeight();
    if ((originalAngle >= 0 && originalAngle <= 90) || (originalAngle > 180 && originalAngle <= 270)) {
      w = (int)Math.round((iw * Math.sin(DEGREE_90 - radian) + ih * Math.sin(radian)));
      h = (int)Math.round((iw * Math.sin(radian) + ih * Math.sin(DEGREE_90 - radian)));
    }
    else {
      w = (int)(ih * Math.sin(DEGREE_90 - radian) + iw * Math.sin(radian));
      h = (int)(ih * Math.sin(radian) + iw * Math.sin(DEGREE_90 - radian));
    }

    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = image.getGraphics();
    Graphics2D g2d = (Graphics2D)g.create();

    // calculate the center of the icon.
    int cx = iw / 2;
    int cy = ih / 2;

    // account for images that have a center point in the middle of a pixel.
    // for these images (not divisible by two) we need to account for the
    // "down and to the right" bias of the graphics context.
    int xOffset = iw % 2 != 0 && originalAngle >= 90 && originalAngle <= 180 ? 1 : 0;
    int yOffset = iw % 2 != 0 && originalAngle >= 180 && originalAngle < 360 ? 1 : 0;

    // move the graphics center point to the center of the icon.
    g2d.translate(w / 2 + xOffset, h / 2 + yOffset);

    // rotate the graphcis about the center point of the icon
    g2d.rotate(Math.toRadians(originalAngle));

    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

    origin.paintIcon(component, g2d, -cx, -cy);

    g2d.dispose();
    return new ImageIcon(image);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createImage
  /**
   ** Create an opaque compatible image of specified width and height.
   **
   ** @param  width              width of the new image
   ** @param  height             height of the new image
   **
   ** @return                    a opaque compatible {@link BufferedImage}.
   **
   ** @see    Transparency#OPAQUE
   */
  public static BufferedImage createImage(final int width, final int height) {
    return graphicsConfiguration().createCompatibleImage(width, height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTransparentImage
  /**
   ** Creata a transparent compatible image.
   ** <p>
   ** <i>A transparent image is one in which each pixel is either fully
   ** transparent or fully opaque</i>.
   **
   ** @param  width              width of the new image
   ** @param  height             height of the new image
   **
   ** @return                    a transparent {@link BufferedImage}.
   **
   ** @see    Transparency#BITMASK
   */
  public static BufferedImage createTransparentImage(final int width, final int height) {
    return graphicsConfiguration().createCompatibleImage(width, height, Transparency.BITMASK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTranslucentImage
  /**
   ** Creata a translucent compatible image.
   ** <p>
   ** <i>A translucent image is one in which each pixel can have a varying level
   ** of opacity, ranging from fully transparent to fully opaque or anything in
   ** between</i>.
   **
   ** @param  width              width of the new image.
   ** @param  height             height of the new image.
   **
   ** @return                    a translucent compatible {@link BufferedImage}.
   **
   ** @see    Transparency#TRANSLUCENT
   */
  public static BufferedImage createTranslucentImage(int width, int height) {
    return graphicsConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compatibleImage
  /**
   ** Convert specified image to a 'compatible image'.
   ** <p>
   ** If the image already is compatible then same image is returned.
   **
   ** @param  image              Image to convert
   ** @return Compatible image
   */
  public static BufferedImage compatibleImage(final BufferedImage image) {

    GraphicsConfiguration configuration = graphicsConfiguration();
    if (image.getColorModel().equals(configuration.getColorModel()))
      return image;

    BufferedImage newImage = configuration.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
    Graphics2D    contect  = newImage.createGraphics();
    contect.drawImage(image, 0, 0, null);
    contect.dispose();

    return newImage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns part of the image from input image icon. It basically takes a
   ** snapshot of the input image at {x, y} location and the size is width x
   ** height.
   **
   ** @param  component          the component where the returned icon will be
   **                            used. The component is used as the
   **                            ImageObserver. It could be <code>null</code>.
   ** @param  origin             the original icon.
   **                            This is the larger icon where a sub-image will
   **                            be created using this method.
   ** @param  x                  the x location of the sub-image, relative to
   **                            the original icon.
   ** @param  y                  the y location of the sub-image, relative to
   **                            the original icon.
   ** @param  width              the width of the sub-image.
   **                            It should be less than the width of the
   **                            original icon.
   ** @param  height             the height of the sub-image.
   **                            It should be less than the height of the
   **                            original icon.
   **
   ** @return                    an new image icon that was part of the input
   **                            image icon.
   */
  public static ImageIcon icon(final Component component, final ImageIcon origin, final int x, final int y, final int width, final int height) {
    return icon(component, origin, x, y, width, height, width, height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns part of the image from input image icon. It basically takes a
   ** snapshot of the input image at {x, y} location and the size is width x
   ** height, then resize it to a size of destWidth x destHeight.
   **
   ** @param  component          the component where the returned icon will be
   **                            used. The component is used as the
   **                            ImageObserver. It could be <code>null</code>.
   ** @param  origin             the original icon.
   **                            This is the larger icon where a sub-image will
   **                            be created using this method.
   ** @param  x                  the x location of the sub-image, relative to
   **                            the original icon.
   ** @param  y                  the y location of the sub-image, relative to
   **                            the original icon.
   ** @param  width              the width of the sub-image.
   **                            It should be less than the width of the
   **                            original icon.
   ** @param  height             the height of the sub-image.
   **                            It should be less than the height of the
   **                            original icon.
   ** @param  destinationWidth   the width of the returned icon.
   **                            The sub-image will be resize if the destWidth
   **                            is not the same as the width.
   ** @param destinationHeight   the height of the returned icon.
   **                            The sub-image will be resize if the destHeight
   **                            is not the same as the height.
   **
   ** @return                    an new image icon that was part of the input
   **                            image icon.
   */
  public static ImageIcon icon(final Component component, final ImageIcon origin, final int x, final int y, final int width, final int height, final int destinationWidth, final int destinationHeight) {
    return icon(component, origin, x, y, width, height, BufferedImage.TYPE_INT_ARGB, destinationWidth, destinationHeight);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns part of the image from input image icon. It basically takes a
   ** snapshot of the input image at {x, y} location and the size is width x
   ** height.
   *
   **
   ** @param  component          the component where the returned icon will be
   **                            used. The component is used as the
   **                            ImageObserver. It could be <code>null</code>.
   ** @param  origin             the original icon.
   **                            This is the larger icon where a sub-image will
   **                            be created using this method.
   ** @param  x                  the x location of the sub-image, relative to
   **                            the original icon.
   ** @param  y                  the y location of the sub-image, relative to
   **                            the original icon.
   ** @param  width              the width of the sub-image.
   **                            It should be less than the width of the
   **                            original icon.
   ** @param  height             the height of the sub-image.
   **                            It should be less than the height of the
   **                            original icon.
   ** @param imageType           image type is defined in {@link BufferedImage},
   **                            such as {@link BufferedImage#TYPE_INT_ARGB},
   **                            {@link BufferedImage#TYPE_INT_RGB} etc.
   **
   ** @return                    an new image icon that was part of the input
   **                            image icon.
   */
  public static ImageIcon icon(final Component component, final ImageIcon origin, final int x, final int y, final int width, final int height, final int imageType) {
    return icon(component, origin, x, y, width, height, imageType, width, height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns part of the image from input image icon. It basically takes a
   ** snapshot of the input image at {x, y} location and the size is width x
   ** height, then resize it to a size of destinationWidth x destinationHeight.
   ** If the original icon is <code>null</code> or the specified location is
   ** outside the original icon, {@link #EMPTY} will be returned.
   **
   ** @param  component          the component where the returned icon will be
   **                            used. The component is used as the
   **                            ImageObserver. It could be <code>null</code>.
   ** @param  origin             the original icon.
   **                            This is the larger icon where a sub-image will
   **                            be created using this method.
   ** @param  x                  the x location of the sub-image, relative to
   **                            the original icon.
   ** @param  y                  the y location of the sub-image, relative to
   **                            the original icon.
   ** @param  width              the width of the sub-image.
   **                            It should be less than the width of the
   **                            original icon.
   ** @param  height             the height of the sub-image.
   **                            It should be less than the height of the
   **                            original icon.
   ** @param imageType           image type is defined in {@link BufferedImage},
   **                            such as {@link BufferedImage#TYPE_INT_ARGB},
   **                            {@link BufferedImage#TYPE_INT_RGB} etc.
   ** @param  destinationWidth   the width of the returned icon.
   **                            The sub-image will be resize if the destWidth
   **                            is not the same as the width.
   ** @param destinationHeight   the height of the returned icon.
   **                            The sub-image will be resize if the destHeight
   **                            is not the same as the height.
   **
   ** @return                    an new image icon that was part of the input
   **                            image icon.
   */
  public static ImageIcon icon(final Component component, final ImageIcon origin, final int x, final int y, final int width, final int height, final int imageType, final int destinationWidth, final int destinationHeight) {
     // outside the original icon.
    if (origin == null || x < 0 || x + width > origin.getIconWidth() || y < 0 || y + height > origin.getIconHeight())
      return EMPTY;

    BufferedImage image = new BufferedImage(destinationWidth, destinationHeight, imageType);
    image.getGraphics().drawImage(origin.getImage(), 0, 0, destinationWidth, destinationHeight, x, y, x + width, y + height, component);
    return new ImageIcon(image);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   overlay
  /**
   ** Returnsa new icon with the overlay paints over the original icon.
   *
   ** @param  component          the component where the returned icon will be
   **                            used. The component is used as the
   **                            ImageObserver. It could be <code>null</code>.
   ** @param  origin             the original icon.
   ** @param  overlay            the overlay icon.
   ** @param  location           the location as defined in SwingConstants
   **                            CENTER, NORTH, SOUTH, WEST, EAST, NORTH_EAST,
   **                            NORTH_WEST, SOUTH_WEST and SOUTH_EAST.
   **
   ** @return                    the new icon.
   */
  public static ImageIcon overlay(final Component component, final ImageIcon origin, final ImageIcon overlay, final int location) {
    return overlay(component, origin, overlay, location, new Insets(0, 0, 0, 0));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   overlay
  /**
   ** Returnsa new icon with the overlay paints over the original icon.
   *
   ** @param  component          the component where the returned icon will be
   **                            used. The component is used as the
   **                            ImageObserver. It could be <code>null</code>.
   ** @param  origin             the original icon.
   ** @param  overlay            the overlay icon.
   ** @param  location           the location as defined in SwingConstants
   **                            CENTER, NORTH, SOUTH, WEST, EAST, NORTH_EAST,
   **                            NORTH_WEST, SOUTH_WEST and SOUTH_EAST.
   ** @param  insets             the insets to the border.
   **                            This parameter has no effect if the location is
   **                            CENTER. For example, if the location is WEST,
   **                            insets.left will be the gap of the left side of
   **                            the original icon and the left side of the
   **                            overlay icon.
   **
   ** @return                    the new icon.
   */
  public static ImageIcon overlay(final Component component, final ImageIcon origin, final ImageIcon overlay, final int location, final Insets insets) {
    int x  = -1;
    int y  = -1;
    int w  = origin.getIconWidth();
    int h  = origin.getIconHeight();
    int sw = overlay.getIconWidth();
    int sh = overlay.getIconHeight();
    switch (location) {
      case SwingConstants.CENTER     : x = (w - sw) / 2;
                                       y = (h - sh) / 2;
                                       break;
      case SwingConstants.NORTH      : x = (w - sw) / 2;
                                       y = insets.top;
                                       break;
      case SwingConstants.SOUTH      : x = (w - sw) / 2;
                                       y = h - insets.bottom - sh;
                                       break;
      case SwingConstants.WEST       : x = insets.left;
                                       y = (h - sh) / 2;
                                       break;
      case SwingConstants.EAST       : x = w - insets.right - sw;
                                       y = (h - sh) / 2;
                                       break;
      case SwingConstants.NORTH_EAST : x = w - insets.right - sw;
                                       y = insets.top;
                                       break;
      case SwingConstants.NORTH_WEST : x = insets.left;
                                       y = insets.top;
                                       break;
      case SwingConstants.SOUTH_WEST : x = insets.left;
                                       y = h - insets.bottom - sh;
                                       break;
      case SwingConstants.SOUTH_EAST : x = w - insets.right  - sw;
                                       y = h - insets.bottom - sh;
                                       break;
    }
    return overlay(component, origin, overlay, x, y);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   overlay
  /**
   ** Returns a new icon with the overlay paints over the original icon.
   **
   ** @param  component          the component where the returned icon will be
   **                            used. The component is used as the
   **                            ImageObserver. It could be <code>null</code>.
   ** @param  origin             the original icon.
   ** @param  overlay            the overlay icon.
   ** @param  x                  the x location of the overlay, relative to
   **                            the original icon.
   ** @param  y                  the y location of the overlay, relative to
   **                            the original icon.
   **
   ** @return                    the new icon.
   */
  public static ImageIcon overlay(final Component component, final ImageIcon origin, final ImageIcon overlay, final int x, final int y) {
    int w  = origin == null ? overlay.getIconWidth() : origin.getIconWidth();
    int h  = origin == null ? overlay.getIconHeight() : origin.getIconHeight();
    int sw = overlay.getIconWidth();
    int sh = overlay.getIconHeight();
    if (x != -1 && y != -1) {
      BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      if (origin != null)
        image.getGraphics().drawImage(origin.getImage(), 0, 0, w, h, component);

      image.getGraphics().drawImage(overlay.getImage(), x, y, sw, sh, component);
      return new ImageIcon(image);
    }
    else
      return origin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   graphicsConfiguration
  /**
   ** Get <code>GraphicsConfiguration</code> instance.
   */
  /**
   ** Returns the default {@link GraphicsConfiguration} associated with the
   ** <code>DefaultScreenDevice</code>.
   **
   ** @return                    the default {@link GraphicsConfiguration} of
   **                            the <code>DefaultScreenDevice</code>.
   */
  public static GraphicsConfiguration graphicsConfiguration() {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateHTML
  /**
   ** Generates HTML that lists all icons in IconFactory.
   **
   ** @param  clazz              the IconFactory class
   */
  public static void generateHTML(final Class<?> clazz) {
    String fullClassName = clazz.getName();
    String className     = getClassName(fullClassName);
    File file = new File(fullClassName + ".html");

    try {
      FileWriter writer = new FileWriter(file);
      writer.write("<html>\n<body>\n<p><b><font size=\"5\" face=\"Verdana\">Icons in " + fullClassName + "</font></b></p>");
      writer.write("<p><b><font size=\"3\" color=\"#AAAAAA\" face=\"Verdana\">1. If you cannot view the images in this page, make sure the file is at the same directory as " + className + ".java</font></b></p>");
      writer.write("<p><b><font size=\"3\" color=\"#AAAAAA\" face=\"Verdana\">2. To get a particular icon in your code, call " + className + ".getImageIcon(FULL_CONSTANT_NAME). Replace FULL_CONSTANT_NAME with the actual full constant name as in the table below</font></b></p>");
      generate(clazz, writer, className);
      writer.write("\n</body>\n</html>");
      writer.close();
      System.out.println("File is generated at \"" + file.getAbsolutePath() + "\". Please copy it to the same directory as " + className + ".java");
    }
    catch (IOException e) {
      System.err.println(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates HTML that lists recusrivly all icons in a
   ** <code>IconFactory</code>.
   **
   ** @param  root               the <code>IconFactory</code> class to inspaect.
   */
  private static void generate(final Class<?> root, final FileWriter writer, final String prefix)
    throws IOException {

    Class<?>[] classes = root.getDeclaredClasses();

    // don't know why but the order is exactly the reverse of the order of
    // definitions.
    for (int i = classes.length - 1; i >= 0; i--) {
      Class<?> clazz = classes[i];
      generate(clazz, writer, getClassName(clazz.getName()));
    }

    Field[] fields = root.getFields();
    writer.write("<p><font face=\"Verdana\"><b>" + prefix + "</b></font></p>");
    writer.write("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#CCCCCC\" width=\"66%\">");
    writer.write("<tr>\n");
    writer.write("<td width=\"24%\" align=\"center\"><b><font face=\"Verdana\" color=\"#003399\">Name</font></b></td>\n");
    writer.write("<td width=\"13%\" align=\"center\"><b><font face=\"Verdana\" color=\"#003399\">Image</font></b></td>\n");
    writer.write("<td width=\"32%\" align=\"center\"><b><font face=\"Verdana\" color=\"#003399\">File Name</font></b></td>\n");
    writer.write("<td width=\"31%\" align=\"center\"><b><font face=\"Verdana\" color=\"#003399\">Full Constant Name</font></b></td>\n");
    writer.write("</tr>\n");
    for (Field field : fields) {
      try {
        Object name = field.getName();
        Object value = field.get(root);
        writer.write("<tr>\n");
        writer.write("<td align=\"left\"><font face=\"Verdana\">" + name + "</font></td>\n");
        writer.write("<td align=\"center\"><font face=\"Verdana\"><img border=\"0\" src=\"" + value + "\"></font></td>\n");
        writer.write("<td align=\"left\"><font face=\"Verdana\">" + value + "</font></td>\n");
        writer.write("<td align=\"left\"><font face=\"Verdana\">" + prefix + "." + name + "</font></td>\n");
        writer.write("</tr>\n");
      }
      catch (IllegalArgumentException e) {
        e.printStackTrace();
      }
      catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    writer.write("</table><br><p>\n");
  }

  private static String getClassName(String fullName) {
    int last = fullName.lastIndexOf(".");
    if (last != -1)
      fullName = fullName.substring(last + 1);

    StringTokenizer tokenizer = new StringTokenizer(fullName, "$");
    StringBuffer buffer = new StringBuffer();
    while (tokenizer.hasMoreTokens()) {
      buffer.append(tokenizer.nextToken());
      buffer.append(".");
    }
    return buffer.substring(0, buffer.length() - 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createImage
  /**
   ** Returns {@link ImageIcon} by passing class and a relative image file path.
   ** <p>
   ** Please note, createImage will print out error message to stderr if image
   ** is not found. The reason we did so is because we want you to make sure all
   ** image files are there in your application. If you ever see the error
   ** message, you should correct it before shipping the product. But if you
   ** just want to test if the image file is there, you don't want any error
   ** message print out. If so, you can use {@link #find(Class,String)}
   ** method. It will throw IOException when image is not found.
   **
   ** @param  clazz              the Class
   ** @param  fileName           the relative file name to <code>clazz</code>.
   **
   ** @return                    the {@link ImageIcon}
   */
  private static ImageIcon createImage(final Class<?> clazz, final String fileName) {
    try {
      return createImageWithException(clazz, fileName);
    }
    catch (IOException e) {
      System.err.println(e.getLocalizedMessage());
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createImageWithException
  /**
   ** Returns {@link ImageIcon} by passing class and a relative image file path.
   ** <p>
   ** Please note, createImage will print out error message to stderr if image
   ** is not found. The reason we did so is because we want you to make sure all
   ** image files are there in your application. If you ever see the error
   ** message, you should correct it before shipping the product. But if you
   ** just want to test if the image file is there, you don't want any error
   ** message print out. If so, you can use {@link #find(Class,String)}
   ** method. It will throw IOException when image is not found.
   **
   ** @param  clazz              the Class
   ** @param  fileName           the relative file name to <code>clazz</code>.
   **
   ** @return                    the {@link ImageIcon}
   */
  private static ImageIcon createImageWithException(final Class<?> clazz, final String fileName)
    throws IOException {

    InputStream resource = clazz.getResourceAsStream(fileName);
    if (resource == null) {
      System.err.println("Image file " + fileName + " is missing");
      return null;
    }

    return new ImageIcon(ImageIO.read(resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaleImage
  /**
   ** Convenience method that returns a scaled instance of the provided
   ** {|link BufferedImage}.
   **
   ** @param  origin             the original image to be scaled
   ** @param  targetWidth        the desired width of the scaled instance, in
   **                            pixels
   ** @param  targetHeight       the desired height of the scaled instance, in
   **                            pixels
   ** @param hint                one of the rendering hints that corresponds to
   **                            RenderingHints.KEY_INTERPOLATION (e.g.
   **                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
   **                            RenderingHints.VALUE_INTERPOLATION_BILINEAR,
   **                            RenderingHints.VALUE_INTERPOLATION_BICUBIC)
   ** @param progressive         if <code>true</code>, this method will use a
   **                            multi-step scaling technique that provides
   **                            higher quality than the usual one-step
   **                            technique (only useful in down-scaling cases,
   **                            where targetWidth or targetHeight is smaller
   **                            than the original dimensions).
   **
   ** @return                    a scaled version of the original BufferedImage
   */
  private static BufferedImage scaleImage(final BufferedImage origin, final int targetWidth, final int targetHeight, final Object hint, final boolean progressive) {
    int scaleWidth;
    int scaleHeight;
    if (progressive) {
      // Use multi-step technique: start with original size, then
      // scale down in multiple passes with drawImage()
      // until the target size is reached
      scaleWidth  = origin.getWidth();
      scaleHeight = origin.getHeight();
    }
    else {
      // Use one-step technique: scale directly from original
      // size to target size with a single drawImage() call
      scaleWidth  = targetWidth;
      scaleHeight = targetHeight;
    }

    final boolean translucent = origin.getTransparency() != Transparency.OPAQUE;
    final int     type        = translucent ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB ;
    BufferedImage scaled      = origin;
    BufferedImage scratch     = null;
    Graphics2D    canvas      = null;

    int           lastWidth   = scaled.getWidth();
    int           lastHeight  = scaled.getHeight();
    do {
      if (progressive && scaleWidth > targetWidth) {
        scaleWidth /= 2;
        if (scaleWidth < targetWidth)
          scaleWidth = targetWidth;
      }

      if (progressive && scaleHeight > targetHeight) {
        scaleHeight /= 2;
        if (scaleHeight < targetHeight)
          scaleHeight = targetHeight;
      }

      if (scratch == null || translucent) {
        // Use a single scratch buffer for all iterations
        // and then copy to the final, correctly-sized image
        // before returning
        scratch = new BufferedImage(lastWidth, scaleHeight, type);
        canvas = scratch.createGraphics();
      }
      canvas.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
      canvas.drawImage(scaled, 0, 0, lastWidth, scaleHeight, 0, 0, lastWidth, lastHeight, null);
      lastWidth  = scaleWidth;
      lastHeight = scaleHeight;

      scaled = scratch;
    } while (scaleWidth != targetWidth || scaleHeight != targetHeight);

    // free up resources
    if (canvas != null)
      canvas.dispose();

    // If we used a scratch buffer that is larger than our target size,
    // create an image of the right size and copy the results into it
    if (targetWidth != scaled.getWidth() || targetHeight != scaled.getHeight()) {
      scratch = new BufferedImage(targetWidth, targetHeight, type);
      canvas = scratch.createGraphics();
      canvas.drawImage(scaled, 0, 0, null);
      canvas.dispose();
      scaled = scratch;
    }

    return scaled;
  }
}