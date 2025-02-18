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

    File        :   ContextConfig.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ContextConfig.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.visual;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import oracle.hst.platform.core.utility.SystemProperty;

import oracle.iam.platform.captcha.core.type.Size;
import oracle.iam.platform.captcha.core.type.Margin;

import oracle.iam.platform.captcha.visual.type.Renderer;

////////////////////////////////////////////////////////////////////////////////
// final class ContextConfig
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The configuration of the visual captcha generator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class ContextConfig {

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
  private static final SystemProperty CONFIG           = SystemProperty.Builder.of(String.class)
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
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The size of the visual captcha to genrate.
   ** <br>
   ** Default Value: 160x70
   */
  private Size     size     = new Size(160.0, 70.0);

  /**
   ** The clear area around the challenge text.
   ** <br>
   ** The margin is filled with the configure canvas factory.
   ** <br>
   ** Default Value: 5
   */
  private Margin   margin = new Margin();

  /** The visual captcha renderer. */
  private Renderer renderer = new Renderer();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ContextConfig</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argumment constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  private ContextConfig() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Sets the size of the visual captcha to generate.
   **
   ** @param  value              the size of the visual captcha to generate.
   **                            <br>
   **                            Allowed object is <code>Size</code>.
   **
   ** @return                    the <code>ContextConfig</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ContextConfig</code>.
   */
  public ContextConfig size(final Size value) {
    this.size = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the size of the visual captcha to generate.
   **
   ** @return                    the size of the visual captcha to generate.
   **                            <br>
   **                            Possible object is <code>Size</code>.
   */
  public Size size() {
    return this.size;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   margin
  /**
   ** Returns the clear area around the challenge text.
   **
   ** @return                    the clear area around the challenge text.
   **                            <br>
   **                            Possible object is {@link Margin}.
   */
  public Margin margin() {
    return this.margin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renderer
  /**
   ** Sets the visual captcha renderer properties.
   **
   ** @param  value              the visual captcha renderer properties.
   **                            <br>
   **                            Allowed object is {@link Renderer}.
   **
   ** @return                    the <code>ContextConfig</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ContextConfig</code>.
   */
  public ContextConfig renderer(final Renderer value) {
    this.renderer = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renderer
  /**
   ** Returns the visual captcha renderer properties.
   **
   ** @return                    the visual captcha renderer properties.
   **                            <br>
   **                            Allowed object is {@link Renderer}.
   */
  public Renderer renderer() {
    return this.renderer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>ContextConfig</code>.
   **
   ** @return                    an newly created instance of
   **                            <code>ContextConfig</code>.
   **                            <br>
   **                            Possible object is <code>ContextConfig</code>.
   */
  public static ContextConfig build() {
    return new ContextConfig();
  }
}