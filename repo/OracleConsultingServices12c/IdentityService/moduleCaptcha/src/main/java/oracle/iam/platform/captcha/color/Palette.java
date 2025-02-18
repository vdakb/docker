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

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Space.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Space.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.color;

import java.util.List;
import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////
// abstract class Palette
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** <code>Palette</code> represents a base class for creating different types of
 ** Palettes.
 ** <br>
 ** Palettes are based on abstractions in color theory. More information can be
 ** found here:
 ** <a href="https://en.wikipedia.org/wiki/Color_theory" target="_blank">https://en.wikipedia.org/wiki/Color_theory</a>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Palette {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int AMOUNT = 1;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

	protected Space          base;
	protected List<Space>    colors;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Complementary
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Palette subclass used to generate a monochromatic color palette.
   ** <p>
   ** A monochromatic color palette takes any color and uses colors with lighter
   ** or darker shades.
   */
  static class Complementary extends Palette {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Complementary</code> color palette.
     ** <p>
     ** When invoked, a complementary palette is created with respect to the
     ** origin in HSL space.
     */
    public Complementary() {
      // ensure inheritance
      super();

      // initialize instance
      this.generate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Complementary</code> color palette.
     ** <p>
     ** When invoked, a complementary palette is created with respect to the
     ** origin in HSL space.
     **
     ** @param  color            the {@link Space} reference used to create a
     **                          new color palette.
     **                          <br>
     **                          Allowed object is {@link Space}.
     */
    public Complementary(final Space color) {
      // ensure inheritance
      super(color);

      // initialize instance
      generate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: generate (Palette)
    /**
     ** Generate any color palette given the base color.
     ** <br>
     ** If no base color is specified, the base color is given as the ordered
     ** triplet (0, 0, 0) in HSL space.
	   */
    @Override
    protected void generate() {
      add(complementary());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: complementary
    /**
     ** Returns a {@link Space} color object with the opposite hue of the base
     ** color.
     ** <<br>
     ** Opposite hue is given as the maximal distance in HSL space between the
     ** base color and any color with the same saturation and lightness. In
     ** other words, the complementary color is given by the color with a
     ** difference of 180 degrees in hue, while retaining the same saturation
     ** and lightness.
     **
     ** @return                  the {@link Space} color object containing the
     **                          complementary color of the base color.
     **                          <br>
     **                          Possible object is {@link Space}.
     */
    private Space complementary() {
      Space.HSL base          = Space.hsl(this.base);
      int       hue           = base.hue();
      double    saturation    = base.saturation();
      double    lightness     = base.lightness();
      Space.HSL complementary = Space.hsl(hue, saturation, lightness);
      complementary.incrementHue(180);
      return complementary;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Analogous
  // ~~~~~ ~~~~~~~~~
  /**
   ** Palette subclass used to generate a analogous color palette.
   ** <p>
   ** An analogous color palette takes any color and uses colors with hues that
   ** are similar.
   */
  static class Analogous extends Palette {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int offset;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Analogous</code> derivated from a base color
     ** and the offset of adjacent colors for the analogous color palette.
     **
     ** @param  color            the {@link Space} reference used to create a
     **                          new color palette.
     **                          <br>
     **                          Allowed object is {@link Space}.
	   ** @param  offset           the offset of the two adjacent Colors in the
     **                          palette.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    private Analogous(final Space color, int offset) {
      // ensure inheritance
      super(color);

      // initialize instance attributes
      if (offset < 0) {
        offset *= -1;
      }
      this.offset = (offset > 120) ? 120 : offset;

      // initialize instance
      generate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: generate (Palette)
    /**
     ** Generate any color palette given the base color.
     ** <br>
     ** If no base color is specified, the base color is given as the ordered
     ** triplet (0, 0, 0) in HSL space.
	   */
    @Override
    protected void generate() {
		  Space.HSL h1 = Space.hsl(Space.hsl(base()));
			Space.HSL h2 = Space.hsl(Space.hsl(base()));
		  h1.incrementHue(this.offset);
		  h2.incrementHue(-this.offset);
  		add(h1);
		  add(h2);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MonoChromatic
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Palette subclass used to generate a monochromatic color palette.
   ** <p>
   ** A monochromatic color palette takes any color and uses colors with lighter
   ** or darker shades.
   */
  static class MonoChromatic extends Palette {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

	  private int amount;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>MonoChromatic</code> color palette given the number
     ** of values and offset of each value.
     **
     ** @param  color            the {@link Space} reference used to create a
     **                          new color palette.
     **                          <br>
     **                          Allowed object is {@link Space}.
     ** @param  amount           the number of values to use.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    private MonoChromatic(final Space color, final int amount) {
      // ensure inheritance
      super(color);

      // initialize instance attributes
      this.amount = (amount < 0) ? 0 : amount;

      // initialize instance
      this.generate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: generate (Palette)
    /**
     ** Generate any color palette given the base color.
     ** <br>
     ** If no base color is specified, the base color is given as the ordered
     ** triplet (0, 0, 0) in HSL space.
	   */
    @Override
    protected void generate() {
      Space.HSL b = Space.hsl(base());
      double    l = b.lightness();
      // create all other aside from base color
      for (int index = 0; index < this.amount; index++) {
        l += ((double)1 / (this.amount + 1) * .5);
        // make sure lightness is in range of 0 to 1, inclusive
        l %= 1;
        add(Space.hsl(b.hue(), b.saturation(), l));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Triadic
  // ~~~~~ ~~~~~~~
  /**
   ** Palette subclass used to generate a triadic color palette.
   ** <p>
   ** A triadic color palette takes the two colors that are equidistant from a
   ** base color's hue, where each color is a maximal 120 degrees away from each
   ** other in hue.
   */
  static class Triadic extends Analogous{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Triadic</code> derivated from a base color for the
     ** triadic color palette.
     **
     ** @param  color            the {@link Space} reference used to create a
     **                          new color palette.
     **                          <br>
     **                          Allowed object is {@link Space}.
     */
    public Triadic(final Space color) {
      // ensure inheritance
      super(color, 120);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Palette</code> that allows use as a JavaBean.
   ** <p>
	 ** When invoked, the color palette is instantiated with respect to the origin
   ** in HSL space.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Palette() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a color <code>Palette</code> with respect to the given
   ** {@link Space} reference.
   **
   ** @param  value              the {@link Space} reference containing the
   **                            color with respect to which the palette should
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Space}.
	 */
	protected Palette(final Space value) {
    // ensure inheritance
    super();

    // initialize instance attributes
		this.base   = value;
		this.colors = new ArrayList<>();
		this.colors.add(this.base);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of {@link Space}s being used in the palette.
   **
   ** @return                     the number of {@link Space}s being used in the
   **                             palette.
	 */
	public final int size() {
		return this.colors.size();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   color
  /**
   ** Returns a shallow copy of the {@link Space} at the specified index.
   **
   ** @param  index                the value containing the {@link Space} object
   **                              to return.
   **
   ** @return                      the {@link Space} object corresponding to the
	 **                              specified index.
	 */
	public final Space color(final int index) {
		return this.colors.get(index);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   color
  /**
   ** Returns a shallow copy of all the colors in the current palette.
   **
   ** @return                    the colection of basetype {@link Space}s
   **                            containing all of the colors in the current
   **                            palette.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Space}.
   */
  public final List<Space> color() {
    return this.colors;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   base
  /**
   ** Returns the base color of the palette.
   **
   ** @return                    the base {@link Space} object that the palette
   **                            is based around.
   **                            <br>
   **                            Possible object is {@link Space}.
   */
  public final Space base() {
    return this.base;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   analogous
  /**
   ** Analogous color schemes use colors that are next to each other on the
   ** color wheel. They usually match well and create serene and comfortable
   ** designs.
   ** <p>
   ** Analogous color schemes are often found in nature and are harmonious
   ** and pleasing to the eye.
   ** <p>
   ** Make sure you have enough contrast when choosing an analogous color
   ** scheme.
   ** <p>
   ** Choose one color to dominate, a second to support. The third color is used
   ** (along with black, white or gray) as an accent.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the collection on complementary colors.
   **                            <br>
   **                            Possible object is array of
   **                            <code>Palette</code>.
   */
  public static Palette analogous(final Space base) {
    return analogous(base, 60);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   analogous
  /**
   ** Analogous color schemes use colors that are next to each other on the
   ** color wheel. They usually match well and create serene and comfortable
   ** designs.
   ** <p>
   ** Analogous color schemes are often found in nature and are harmonious
   ** and pleasing to the eye.
   ** <p>
   ** Make sure you have enough contrast when choosing an analogous color
   ** scheme.
   ** <p>
   ** Choose one color to dominate, a second to support. The third color is used
   ** (along with black, white or gray) as an accent.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   ** @param  offset             the offset of the two adjacent Colors in the
   **                            palette.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the collection on complementary colors.
   **                            <br>
   **                            Possible object is array of
   **                            <code>Palette</code>.
   */
  public static Palette analogous(final Space base, final int offset) {
    return new Analogous(base, offset);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   monochromatic
  /**
   ** Monochromatic color schemes are derived from a single base hue and
   ** extended using its shades, tones and tints. Tints are achieved by adding
   ** white and shades and tones are achieved by adding a darker color, grey or
   ** black.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>Space</code>.
   **
   ** @return                    the collection on monochromatic colors.
   **                            <br>
   **                            Possible object is array of
   **                            <code>Palette</code>.
   */
  public static Palette monochromatic(final Space base) {
    return monochromatic(base, AMOUNT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   monochromatic
  /**
   ** Monochromatic color schemes are derived from a single base hue and
   ** extended using its shades, tones and tints. Tints are achieved by adding
   ** white and shades and tones are achieved by adding a darker color, grey or
   ** black.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>Space</code>.
   ** @param  amount             the number of values to use.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the collection on monochromatic colors.
   **                            <br>
   **                            Possible object is array of
   **                            <code>Palette</code>.
   */
  public static Palette monochromatic(final Space base, final int amount) {
    return new MonoChromatic(base, amount);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complementary
  /**
   ** Colors that are opposite each other on the color wheel are considered to
   ** be complementary colors (example: red and green).
   ** <p>
   ** The high contrast of complementary colors creates a vibrant look
   ** especially when used at full saturation. This color scheme must be managed
   ** well so it is not jarring.
   ** <p>
   ** Complementary color schemes are tricky to use in large doses, but work
   ** well when you want something to stand out.
   ** <p>
   ** Complementary colors are really bad for text.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>Space</code>.
   **
   ** @return                    the collection on complementary colors.
   **                            <br>
   **                            Possible object is array of
   **                            <code>Palette</code>.
   */
  public static Palette complementary(final Space base) {
    return new Complementary(base);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   triadic
  /**
   ** A triadic color scheme uses colors that are evenly spaced around the color
   ** wheel.
   ** <p>
   ** Triadic color schemes tend to be quite vibrant, even if use pale or
   ** unsaturated versions of hues.
   ** <p>
   ** To use a triadic harmony successfully, the colors should be carefully
   ** balanced - let one color dominate and use the two others for accent.
   ** <p>
   ** Burger King uses this color scheme quite successfully.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the collection on complementary colors.
   **                            <br>
   **                            Possible object is array of
   **                            <code>Palette</code>.
   */
  public static Palette triadic(final Space base) {
    return new Triadic(base);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generate any color palette given the base color.
   ** <br>
   ** If no base color is specified, the base color is given as the ordered
   ** triplet (0, 0, 0) in HSL space.
   */
  protected abstract void generate();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add a new {@link Space} reference to the instance's list of
   ** {@link Space}s.
   **
   ** @param  value              the {@link Space} reference to add to the
   **                            instance's list of {@link Space}s.
   **                            <br>
   **                            Allowed object is {@link Space}.
   */
  protected void add(final Space value) {
    this.colors.add(value);
  }
}