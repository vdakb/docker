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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Java Server Faces Feature

    File        :   LabelInterpolator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LabelInterpolator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.validator;

import java.util.Locale;

import javax.validation.Validation;
import javax.validation.MessageInterpolator;

import oracle.hst.platform.jsf.Faces;
import oracle.hst.platform.jsf.Component;

////////////////////////////////////////////////////////////////////////////////
// class LabelInterpolator
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Interpolates a given constraint violation message. 
 ** <p>
 ** Unlike native JSF validation error messages, in a bean validation message by
 ** default the label of the component where a validation constraint violation
 ** originated from can not be displayed in the middle of a message. Using the
 ** <code>javax.faces.validator.BeanValidator.MESSAGE</code> bundle key such
 ** label can be put in front or behind the message, but that's it.
 ** <br>
 ** With this <code>LabelInterpolator</code> a label can appear in the middle of
 ** a message, by using the special placeholder <code>{jsf.label}</code> in bean
 ** validation messages.
 ** <p>
 ** <b>Note</b>
 ** <br>
 ** That Bean Validation is not only called from within JSF, and as such JSF
 ** might not be available. If JSF is not available occurrences of
 ** <code>{jsf.label}</code> will be replaced by an empty string. The user
 ** should take care that messages are compatible with both situations if
 ** needed.
 ** <p>
 ** This message interpolator is <strong>not</strong> needed for putting a
 ** component label before or after a bean validation message. That
 ** functionality is already provided by JSF itself via the
 ** <code>javax.faces.validator.BeanValidator.MESSAGE</code> key in any resource
 ** bundle known to JSF.
 ** <h3>Installation</h3>
 ** <p>
 ** Create a <code>/META-INF/validation.xml</code> file in WAR with the
 ** following contents:
 ** <pre>
 **   &lt;?xml version="1.0" encoding="US-ASCII"?&gt;
 **   &lt;validation-config xmlns             ="http://jboss.org/xml/ns/javax/validation/configuration"
 **                         xmlns:xsi         ="http://www.w3.org/2001/XMLSchema-instance"
 **                         xsi:schemaLocation="http://jboss.org/xml/ns/javax/validation/configuration validation-configuration-1.0.xsd"&gt;
 **      &lt;message-interpolator&gt;oracle.hst.platform.jsf.validator.LabelInterpolator&lt;/message-interpolator&gt;
 **    &lt;/validation-config&gt;
 ** </pre>
 ** <h3>Usage</h3>
 ** As an example, the customization of <code>@Size</code> in
 ** <code>Messages.properties</code>:
 ** <pre>
 **   javax.validation.constraints.Size.message = The size of {jsf.label} must be between {min} and {max} characters
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class LabelInterpolator implements MessageInterpolator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final MessageInterpolator delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LabelInterpolator</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LabelInterpolator() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.delegate = Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   interpolate (MessageInterpolator)
  /**
   ** Interpolates the message template based on the constraint validation
   ** context.
   ** <p>
   ** The locale is defaulted according to the {@link MessageInterpolator}
   ** implementation.
   **
   ** @param  template           the message to interpolate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the contextual information related to the
   **                            interpolation.
   **                            <br>
   **                            Allowed object is
   **                            {@link MessageInterpolator.Context}.
   **
   ** @return                    the interpolated error message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String interpolate(final String template, final Context context) {
    return interpolate(template, context, Faces.context() != null ? Faces.currentLocale() : Faces.defaultLocale());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   interpolate (MessageInterpolator)
  /**
   ** Interpolates the message template based on the constraint validation
   ** context.
   ** <p>
   ** The {@link Locale} used is provided as a parameter.
   **
   ** @param  template           the message to interpolate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the contextual information related to the
   **                            interpolation.
   **                            <br>
   **                            Allowed object is
   **                            {@link MessageInterpolator.Context}.
   ** @param  locale             the locale targeted for the message.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    the interpolated error message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String interpolate(final String template, final Context context, final Locale locale) {
    String message = this.delegate.interpolate(template, context, locale);
    if (message.contains("{jsf.label}")) {
      String label = "";
      if (Faces.context() != null) {
        label = Component.label(Component.current());
      }
      message = message.replace("{jsf.label}", label);
    }
    return message;
  }
}