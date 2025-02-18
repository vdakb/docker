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

    File        :   Encoder.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Encoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import oracle.iam.service.captcha.core.service.CaptchaService;

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
   ** @param  service            the {@link CaptchaService} to generate the
   **                            captcha challenge text and the resulting
   **                            image.
   **                            <br>
   **                            Allowed object is {@link CaptchaService}.
   ** @param  format             a string containing the informal name of the
   **                            format, like <code>png</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  output             an {@link OutputStream} to be written to.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   **
   ** @return                    the generated captcha chellenge text.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IOException        if an error occurs during writing the image
   **                            data.
   */
  public static String generate(final CaptchaService service, final String format, final OutputStream output)
    throws IOException {

    final Captcha captcha = service.captcha();
    ImageIO.write(captcha.image, format, output);
    return captcha.challenge();
  }
}