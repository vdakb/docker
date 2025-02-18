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

    File        :   SingleColorFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SingleColorFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.color;

import java.awt.Color;

////////////////////////////////////////////////////////////////////////////////
// class SingleColorFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SingleColorFactory implements ColorFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Color color;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SingleColorFactory</code> that keep a single color
   ** value
   **
   ** @param  color              the {@link Color} property to set.
   **                            <br>
   **                            Allowed object is {@link Color}.
   */
  private SingleColorFactory(final Color color) {
    // ensure inheritance
    super();

    // initialize intstance attributes
    this.color = color;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get (ColorFactory)
  /**
   ** Returns the {@link Color} mutation at the specified <code>step</code> for
   ** a gradient element.
   **
   ** @param  index              the index for the desired {@link Color}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the step {@link Color} in the gradient.
   **                            <br>
   **                            Possible object is {@link Color}.
   */
  @Override
  public final Color get(final int index) {
    return this.color;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>SingleColorFactory</code>.
   ** <p>
   ** The color returned by this factory is defaulted to {@link Color#BLACK}.
   **
   ** @return                    the created <code>SingleColorFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SingleColorFactory</code>.
   */
  public static SingleColorFactory build() {
    return build(Color.BLACK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>SingleColorFactory</code>.
   **
   ** @param  color              the {@link Color} property to set.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    the created <code>SingleColorFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SingleColorFactory</code>.
   */
  public static SingleColorFactory build(final Color color) {
    return new SingleColorFactory(color);
  }
}