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

    File        :   Message.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Message.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

import javax.faces.context.FacesContext;

import javax.faces.application.FacesMessage;

////////////////////////////////////////////////////////////////////////////////
// abstract class Message
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Simplified reporting of errors, warning or informations to the user in a JSF
 ** application.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Message {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String MISSING_RESOURCE         = "???%s???";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Message</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Message()" and enforces use of the public method below.
   */
  private Message() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Factory method to create a {@link FacesMessage} for error purpose from an
   ** entry in the {@link ResourceBundle} of the Faces application.
   **
   ** @param  summary            the message text to display as the message
   **                            summary.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>parameter</code>.
   **                            <br>
   **                            Allowed object is {@link FacesMessage}.
   ** @param  parameter          the parameters to go into the message.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   **                            <br>
   **                            Possible object is {@link FacesMessage}.
   */
  public static FacesMessage error(final String summary, final String pattern, final Object... parameter) {
    return message(FacesMessage.SEVERITY_ERROR, summary, pattern, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Factory method to create a {@link FacesMessage} for warning purpose from
   ** an entry in the {@link ResourceBundle} of the Faces application.
   **
   ** @param  summary            the message text to display as the message
   **                            summary.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>parameter</code>.
   **                            <br>
   **                            Allowed object is {@link FacesMessage}.
   ** @param  parameter          the parameters to go into the message.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   **                            <br>
   **                            Possible object is {@link FacesMessage}.
   */
  public static FacesMessage warning(final String summary, final String pattern, final Object... parameter) {
    return message(FacesMessage.SEVERITY_WARN, summary, pattern, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   information
  /**
   ** Factory method to create a {@link FacesMessage} for information purpose
   ** from an entry in the {@link ResourceBundle} of the Faces application.
   **
   ** @param  summary            the message text to display as the message
   **                            summary.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>parameter</code>.
   **                            <br>
   **                            Allowed object is {@link FacesMessage}.
   ** @param  parameter          the parameters to go into the message.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   **                            <br>
   **                            Possible object is {@link FacesMessage}.
   */
  public static FacesMessage information(final String summary, final String pattern, final Object... parameter) {
    return message(FacesMessage.SEVERITY_INFO, summary, pattern, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Factory method to create a {@link FacesMessage} from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  severity           the severity of the message.
   **                            <br>
   **                            Allowed object is
   **                            {@link FacesMessage.Severity}.
   ** @param  summary            the message text to display as the message
   **                            summary.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>parameter</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameters to go into the message.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   **                            <br>
   **                            Possible object is {@link FacesMessage}.
   */
  public static FacesMessage message(final FacesMessage.Severity severity, final String summary, String pattern, final Object... parameter) {
    // substitute the palceholderns in the pattern if they are provided
    if (parameter != null && parameter.length > 0)
      pattern = String.format(pattern, parameter);

    final FacesMessage message = new FacesMessage();
    message.setSeverity(severity);
    message.setSummary(summary);
    message.setDetail(pattern);
    return message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   show
  /**
   ** Append a {@link FacesMessage} to the set of messages.
   ** <br>
   ** The message will not be bound to any component.
   **
   ** @param  message            the {@link FacesMessage} to append to te set
   **                            of messages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   **                            <br>
   **                            Possible object is {@link FacesMessage}.
   */
  public static FacesMessage show(final FacesMessage message) {
    return show(null, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   show
  /**
   ** Append a {@link FacesMessage} to the set of messages associated with the
   ** specified component identifier, if componentId is not <code>null</code>.
   ** If componentId is <code>null</code>, this {@link FacesMessage} is assumed
   ** to not be associated with any specific component instance.
   **
   ** @param  componentId        the ID of the component to attach the message
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to be appended.
   **                            <br>
   **                            Allowed object is {@link FacesMessage}.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   **                            <br>
   **                            Possible object is {@link FacesMessage}.
   */
  public static FacesMessage show(final String componentId, final FacesMessage message) {
    Faces.context().addMessage(componentId, message);
    return message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceValue
  /**
   ** Pulls a String resource from the property bundle that is defined under the
   ** application &lt;resource-bundle&gt; element in the faces config.
   **
   ** @param  name               the string representing the
   **                            <code>&lt;resource-bundle&gt;&lt;var&gt;</code>
   **                            element in <i>faces-config.xml</i>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  key                string message key.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    resource choice or placeholder error String
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String resourceValue(final String name, final String key) {
    return stringSafely(bundle(name), key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bundle
  /**
   ** Returns the {@link ResourceBundle} of the current JSF context.
   **
   ** @param  name               the string representing the
   **                            <code>&lt;resource-bundle&gt;&lt;var&gt;</code>
   **                            element in <i>faces-config.xml</i>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ResourceBundle} of the current
   **                            context.
   **                            <br>
   **                            Possible object is {@link ResourceBundle}.
   */
  public static ResourceBundle bundle(final String name) {
    final FacesContext context = Faces.context();
    // load the resource bundle using the JSF application which would under the
    // covers use UIViewRoot#getLocale() (with a fallback to
    // Locale#getDefault())
    return context.getApplication().getResourceBundle(context, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringSafely
  /**
   ** Internal method to proxy for resource keys that don't exist.
   **
   ** @param  bundle             the {@link ResourceBundle} the desired resource
   **                            is provided by.
   **                            <br>
   **                            Allowed object is {@link ResourceBundle}.
   ** @param  key                string message key.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    resource choice or placeholder error String
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String stringSafely(final ResourceBundle bundle, final String key) {
    return stringSafely(bundle, key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringSafely
  /**
   ** Internal method to proxy for resource keys that don't exist.
   **
   ** @param  bundle             the {@link ResourceBundle} the desired resource
   **                            is provided by.
   **                            <br>
   **                            Allowed object is {@link ResourceBundle}.
   ** @param  key                string message key.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       placeholder string if the key isn't mapped in
   **                            the resource bundle.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    resource choice or placeholder error String
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String stringSafely(final ResourceBundle bundle, final String key, final String defaultValue) {
    String resource = null;
    try {
      resource = bundle.getString(key);
    }
    catch (MissingResourceException e) {
      resource = (defaultValue != null) ? defaultValue : String.format(MISSING_RESOURCE, key);
    }
    return resource;
  }
}