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

    File        :   Captcha.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Captcha.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core;

import java.io.Serializable;

import java.awt.image.BufferedImage;

////////////////////////////////////////////////////////////////////////////////
// class Captcha
// ~~~~~ ~~~~~~~
/**
 ** The <code>Captcha</code> challenge state.
 ** <br>
 ** An instance of this class will be set in a session scope of an application.
 ** <br>
 ** Unfortunately it cannot failover correctly in clustered mode due to a
 ** {@link BufferedImage} isn't serializable.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Captcha implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-9067257838148633712")
  private static final long serialVersionUID = 5453066395029186140L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String        challenge;
  final BufferedImage image;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Captcha</code> with the specified challenge text and
   ** an distorted image with the challenge text.
   **
   ** @param  challenge          the challenge text.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @aram   image              the visual captcha text as an image.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   */
  private Captcha(final String challenge, final BufferedImage image) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.challenge = challenge;
    this.image     = image;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   image
  /**
   ** Returns the <code>Captcha</code> challenge image.
   **
   ** @return                    the <code>Captcha</code> challenge image.
   **                            <br>
   **                            Possible object is {@link BufferedImage}.
   */
  public final BufferedImage image() {
    return this.image;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   challenge
  /**
   ** Returns the <code>Captcha</code> challenge text.
   **
   ** @return                    the <code>CAPTCHA</code> challenge text.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String challenge() {
    return this.challenge;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Captcha</code>.
   **
   ** @param  challenge          the challenge text.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  image              the visual captcha text as an image.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   **
   ** @return                    the created <code>Captcha</code>.
   **                            <br>
   **                            Possible object is <code>Captcha</code>.
   */
  public static Captcha build(final String challenge, final BufferedImage image) {
    return new Captcha(challenge, image);
  }
}