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

    File        :   Encoder.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Encoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

////////////////////////////////////////////////////////////////////////////////
// class Encoder
// ~~~~~ ~~~~~~~
/**
 ** The encoder to generate an image.
 */
public class Encoder {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Encoder</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Encoder() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a {@link Captcha} containing a random character sequence from an
   ** alphabet and writes an image using an arbitrary <code>ImageWriter</code>
   ** that supports the given <code>format</code> to an
   ** <code>OutputStream</code>.
   **
   ** @param  challenge          the {@link Challenge} to generate the captcha
   **                            text and the resulting image.
   **                            <br>
   **                            Allowed object is {@link Challenge}.
   ** @param  format             a string containing the informal name of the
   **                            format, like <code>png</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  output             an {@link OutputStream} to be written to.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   **
   ** @return                    the generated captcha challenge text.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IOException        if an error occurs during writing the image
   **                            data.
   */
  public static String generate(final Challenge challenge, final String format, final OutputStream output)
    throws IOException {

    final Captcha captcha = challenge.captcha();
    ImageIO.write(captcha.image, format, output);
    return captcha.challenge();
  }
}
