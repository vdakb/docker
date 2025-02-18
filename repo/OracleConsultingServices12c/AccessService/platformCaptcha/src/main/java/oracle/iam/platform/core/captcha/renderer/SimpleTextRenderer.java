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

    File        :   SimpleTextRenderer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SimpleTextRenderer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.renderer;

////////////////////////////////////////////////////////////////////////////////
// class SimpleTextRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>SimpleTextRenderer</code> renders a character sequence onto a
 ** {@code BufferedImage}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SimpleTextRenderer extends AbstractTextRenderer<SimpleTextRenderer> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SimpleTextRenderer</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private SimpleTextRenderer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrange (TextRenderer)
  /**
   ** Calulates the position of each character in the sequence <code>text</code>
   ** so that its fits in the rectange defined in in the underlying
   ** {@code BufferedImage}.
   **
   ** @param  challenge          the {@link Challenge} text to render.
   **                            <br>
   **                            Allowed object is {@link Challenge}.
   ** @param  width              the width of the {@link Challenge}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  height             the height of the {@link Challenge}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @Override
  protected void arrange(final Challenge challenge, final int width, final int height) {
    double x = 0;
    for (Challenge.Letter l : challenge.sequence()) {
      l.x = x;
      l.y = (height + l.ascent * 0.7) / 2;
      x += l.w;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to a <code>SimpleTextRenderer</code> that renders a
   ** {@link Challenge} text in the remaining area of an image enclosed by the
   ** specified margins.
   **
   ** @param  marginTop          the top margin inside of the
   **                            {@code BufferedImage}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  marginLeft         the left margin inside of the
   **                            {@code BufferedImage}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  marginBottom       the bottom margin inside of the
   **                            {@code BufferedImage}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
    ** @param  marginRight       the right margin inside of the
   **                            {@code BufferedImage}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the created <code>SimpleTextRenderer</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SimpleTextRenderer</code>.
   */
  public static SimpleTextRenderer build(final int marginTop, final int marginLeft, final int marginBottom, final int marginRight) {
    return new SimpleTextRenderer();
  }
}