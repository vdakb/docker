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

    File        :   Challenge.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Challenge.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.renderer;

import java.util.List;
import java.util.ArrayList;

import java.text.AttributedString;
import java.text.AttributedCharacterIterator;

import java.awt.Font;
import java.awt.Color;

import java.awt.font.TextAttribute;

////////////////////////////////////////////////////////////////////////////////
// class Challenge
// ~~~~~ ~~~~~~~~~
/**
 ** ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Challenge {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<Letter> sequence = new ArrayList<Letter>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class Letter
  // ~~~~~ ~~~~~~
  /**
   ** The character sequence of a challenge.
   */
  public static class Letter {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The character of the <code>Letter</code>. */
    char   c;

    /** The X coordinate of the <code>Letter</code>. */
    double x;

    /** The Y coordinate of the <code>Letter</code>. */
    double y;

    /** The width of the <code>Letter</code>. */
    double w;

    /** The height of the <code>Letter</code>. */
    double h;

    /** The ascent of the <code>Letter</code>. */
    double ascent;

    /** The descent of the <code>Letter</code>. */
    double descent;

    /** The {@link Font} of the <code>Letter</code>. */
    Font   font;

    /** The {@link Color} of the <code>Letter</code>. */
    Color  color;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Letter</code> with the properties specified.
     **
     ** @param  c                the character of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is <code>char</code>.
     ** @param  x                the X coordinate of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     ** @param  y                the Y coordinate of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     ** @param  w                the width of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     ** @param  h                the height of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     ** @param  a                the ascent of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     ** @param  d                the descent of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     ** @param  font             the {@link Font} of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is {@link Font}.
     ** @param  color            the {@link Color} of the <code>Letter</code>.
     **                          <br>
     **                          Allowed object is {@link Color}.
     */
    private Letter(final char c, final double x, final double y, final double w, final double h, final double a, final double d, final Font font, final Color color) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.c       = c;
      this.x       = x;
      this.y       = y;
      this.w       = w;
      this.h       = h;
      this.ascent  = a;
      this.descent = d;
      this.font    = font;
      this.color   = color;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: iterator
    /**
     ** Creates an {@link AttributedCharacterIterator} that provides access to
     ** the character.
     **
     ** @return                  the {@link AttributedCharacterIterator} that
     **                           provides access to the character.
     **                          <br>
     **                          Possible object is
     **                          {@link AttributedCharacterIterator}.
     */
    public AttributedCharacterIterator iterator() {
      final AttributedString s = new AttributedString(String.valueOf(this.c));
      s.addAttribute(TextAttribute.FONT, this.font, 0, 1);
      return s.getIterator();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Challenge</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Challenge() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sequence
  /**
   ** Returns the character sequence.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the {@link List} of {@link Letter}s the
   **                            <code>Challenge</code> belongs to.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Letter}.
   */
  public List<Letter> sequence() {
    return this.sequence;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   width
  /**
   ** Calculates the width of the entire text based on the properties of its
   ** {@link Letter}s.
   **
   ** @return                    the width of the entire text.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double width() {
    double  min   = 0;
    double  max   = 0;
    boolean first = true;
    for (Letter l : this.sequence) {
      if (first) {
        min   = l.x;
        max   = l.x + l.w;
        first = false;
      }
      else {
        if (min > l.x) {
          min = l.x;
        }
        if (max < l.x + l.w) {
          max = l.x + l.w;
        }
      }
    }
    return max - min;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Calculates the height of the entire text based on the properties of its
   ** {@link Letter}s.
   **
   ** @return                    the height of the entire text.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double height() {
    double  min   = 0;
    double  max   = 0;
    boolean first = true;
    for (Letter l : this.sequence) {
      if (first) {
        min   = l.y;
        max   = l.y + l.h;
        first = false;
      }
      else {
        if (min > l.y) {
          min = l.y;
        }
        if (max < l.y + l.h) {
          max = l.y + l.h;
        }
      }
    }
    return max - min;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Challenge</code>.
   **
   ** @return                    the created <code>Challenge</code>.
   **                            <br>
   **                            Possible object is <code>Challenge</code>.
   */
  public static Challenge build() {
    return new Challenge();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Removes all of the characters from the sequence (optional operation).
   ** <br>
   ** The character sequence will be empty after this call returns.
   **
   ** @return                    the <code>Challenge</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code>.
   */
  public Challenge clear() {
    this.sequence.clear();
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends the specified character to the end of the character sequence.
   **
   ** @param  c                  the character of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   ** @param  x                  the X coordinate of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  y                  the Y coordinate of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  w                  the width of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  h                  the height of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  a                  the ascent of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  d                  the descent of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  font               the {@link Font} of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is {@link Font}.
   ** @param  color              the {@link Color} of the <code>Letter</code>.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    the <code>Challenge</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code>.
   */
  public Challenge add(final char c, final double x, final double y, final double w, final double h, final double a, final double d, final Font font, final Color color) {
    this.sequence.add(new Letter(c, x, y, w, h, a, d, font, color));
    return this;
  }
}