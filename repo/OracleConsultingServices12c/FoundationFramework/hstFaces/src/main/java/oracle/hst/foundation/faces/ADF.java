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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   ADF.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ADF.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces;

import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.MissingResourceException;

import java.text.MessageFormat;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;

import javax.faces.context.FacesContext;

import javax.faces.component.UIComponent;

import org.apache.myfaces.trinidad.util.Service;

import org.apache.myfaces.trinidad.context.RequestContext;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;

import oracle.binding.ControlBinding;
import oracle.binding.OperationBinding;
import oracle.binding.AttributeBinding;

import oracle.jbo.Row;
import oracle.jbo.ApplicationModule;

import oracle.jbo.uicli.binding.JUEventBinding;
import oracle.jbo.uicli.binding.JUCtrlListBinding;
import oracle.jbo.uicli.binding.JUCtrlActionBinding;

import oracle.adf.share.ADFContext;

import oracle.adf.model.BindingContext;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCBindingContainer;

import oracle.adf.model.events.EventDispatcher;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.help.HelpTopic;
import oracle.adf.view.rich.help.HelpProvider;

import oracle.adf.controller.ViewPortContext;
import oracle.adf.controller.TaskFlowContext;
import oracle.adf.controller.ControllerContext;
////////////////////////////////////////////////////////////////////////////////
// class ADF
// ~~~~~ ~~~
/**
 ** Collection of utility methods for the ADF API that are mainly shortcuts for
 ** obtaining stuff from the thread local {@link AdfFacesContext}. In effects,
 ** it 'flattens' the hierarchy of nested objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ADF {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  EXECUTE_OPERATION_FAILED = "EXECUTE_OPERATION_BINDING_FAILED";

  private static final String ITERATOR_NOT_FOUND       = "Iterator Binding \"%s\" not found";
  private static final String OPERATION_NOT_FOUND      = "Operation Binding \"%s\" not found";
  private static final String BINDING_NOT_FOUND        = "Control Binding \"%s\" not found";
  private static final String BINDING_NOT_LIST         = "Binding \"%s\" is not a list binding";
  private static final String BINDING_NOT_EXISTS       = "Binding \"%s\" does not exist.";

  private static final String MISSING_RESOURCE         = "???%s???";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ADF</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private ADF() {
    // do not instantiate
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokePopup
  /**
   ** Shows the specified popup component and its contents.
   **
   ** @param  popupId            is the clientId of the popup to be shown.
   **                            clientId is derived from backing bean for the
   **                            af:popup using getClientId method.
   */
  public static void invokePopup(String popupId) {
	invokePopup(popupId, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokePopup
  /**
   ** Shows the specified popup and uses the specified hints to align the popup.
   ** <p>
   ** There are nine predefined align enumerations represented by constants:
   ** <ol>
   **  <li>AdfRichPopup.ALIGN_AFTER_START -The popup appears underneath the
   **                                      element with the popup's upper-left
   **                                      corner aligned with the lower-left
   **                                      corner of the element.
   **                                      The left edges of the element and the
   **                                      popup are aligned.
   **  <li>AdfRichPopup.ALIGN_AFTER_END   -The popup appears underneath the
   **                                      element with the popup's upper-right
   **                                      corner aligned with the lower-right
   **                                      corner of the element.
   **                                      The right edges of the element and
   **                                      the popup are aligned.
   **  <li>AdfRichPopup.ALIGN_BEFORE_START-The popup appears above the element
   **                                      with the popup's lower-left corner
   **                                      aligned with the upper-left corner
   **                                      of the element.
   **                                      The left edges of the element and the
   **                                      popup are aligned.
   **  <li>AdfRichPopup.ALIGN_BEFORE_END  -The popup appears above the element
   **                                      with the popup's lower-right corner
   **                                      aligned with the upper-right corner
   **                                      of the element.
   **                                      The right edges of the element and
   **                                      the popup are aligned.
   **  <li>AdfRichPopup.ALIGN_END_AFTER   -The popup appears to the right of the
   **                                      element with the popup's lower-left
   **                                      corner aligned with the lower-right
   **                                      corner of the element.
   **                                      The bottom edges of the element and
   **                                      the popup are aligned.
   **  <li>AdfRichPopup.ALIGN_END_BEFORE  -The popup appears to the right of the
   **                                      element with the popup's upper-left
   **                                      corner aligned with the upper-right
   **                                      corner of the element.
   **                                      The top edges of the element and the
   **                                      popup are aligned.
   **  <li>AdfRichPopup.ALIGN_START_AFTER -The popup appears to the left of the
   **                                      element with the popup's lower-right
   **                                      corner aligned with the lower-left
   **                                      corner of the element.
   **                                      The bottom edges of the element and
   **                                      the popup are aligned.
   **  <li>AdfRichPopup.ALIGN_START_BEFORE-The popup appears to the left of the
   **                                      element with the popup's upper-right
   **                                      corner aligned with the upper-left
   **                                      corner of the element.
   **                                      The top edges of the element and the
   **                                      popup are aligned.
   **  <li>AdfRichPopup.ALIGN_OVERLAP     -The popup appears over top of the
   **                                      element with the upper-left corners
   **                                      aligned.
   ** </ol>
   ** The third popup hint doesn't have to do with positioning but rather the
   ** context that the popup fetch event is invoked.
   ** <br>
   ** The AdfRichPopup.HINT_LAUNCH_ID hint defines the component that launched the
   ** popup. The value should be set with the clientId of the launcher component.
   ** If the <code>eventContext</code> property is set to "launcher", this popup
   ** hint is required in order for the popup fetch event to be fired from the
   ** context of the launcher component.
   **
   ** @param  popupId            is the clientId of the popup to be shown.
   **                            clientId is derived from backing bean for the
   **                            af:popup using getClientId method.
   ** @param  align              is a hint for the popup display.
   **                            Check AdfRichPopup js javadoc for valid values.
   **                            Supported value includes:
   **                            <ul>
   **                              <li>AdfRichPopup.ALIGN_START_AFTER
   **                              <li>AdfRichPopup.ALIGN_BEFORE_START
   **                              <li>AdfRichPopup.ALIGN_END_BEFORE
   **                            </ul>
   ** @param  alignId           is the clientId of the component the popup should
   **                           align to
   **                           - clientId is derived from backing bean for the
   **                           component using getClientId method.
   **                           align and alignId need to be specified together
   **                           - specifying null for either of them will have no
   **                           effect.
   */
  public static void invokePopup(final String popupId, final String align, final String alignId) {
    if (popupId != null) {
      final StringBuffer showPopup = new StringBuffer();
      showPopup.append("var hints = new Object();");
      // add hints only if specified - see javadoc for AdfRichPopup js for details on valid values and behavior
      if (align != null && alignId != null) {
        showPopup.append("hints[AdfRichPopup.HINT_ALIGN] = " + align + ";");
        showPopup.append("hints[AdfRichPopup.HINT_ALIGN_ID] ='" + alignId + "';");
      }
      showPopup.append("var popupObj=AdfPage.PAGE.findComponent('" + popupId + "'); popupObj.show(hints);");
      addScript(showPopup.toString());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hidePopup
  /**
   ** Hides the specified popup.
   **
   ** @param  popupId            is the clientId of the popup to be shown.
   **                            clientId is derived from backing bean for the
   **                            af:popup using getClientId method.
   */
  public static void hidePopup(final String popupId) {
    if (popupId != null) {
      final String hidePopup = "var popupObj=AdfPage.PAGE.findComponent('" + popupId + "'); popupObj.hide();";
      addScript(hidePopup);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addScript
  /**
   ** Adds a script for execution during rendering.
   **
   ** @param script              the path to the script to add for execution.
   */
  public static void addScript(final String script) {
    final ExtendedRenderKitService service = Service.getRenderKitService(JSF.context(), ExtendedRenderKitService.class);
    service.addScript(JSF.context(), script);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the specified resource bundle.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Bundle" or {PACKAGE_NAME} + "Bundle".
   ** @param  key                string message key.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String clazzName, final String key) {
    return stringSafely(resourceBundle(clazzName), key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the specified resource bundle.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Bundle" or {PACKAGE_NAME} + "Bundle".
   ** @param  key                string message key.
   ** @param  arguments          the objects to be formatted and substituted.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String clazzName, final String key, final Object... arguments) {
    final String pattern = stringSafely(resourceBundle(clazzName), key, null);
    return arguments.length == 0 ? pattern : MessageFormat.format(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the specified resource bundle.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Bundle" or {PACKAGE_NAME} + "Bundle".
   ** @param  key                string message key.
   ** @param locale              the locale to look for.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String clazzName, final String key, Locale locale) {
    return stringSafely(resourceBundle(clazzName, locale), key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the specified resource bundle.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Bundle" or {PACKAGE_NAME} + "Bundle".
   ** @param  key                string message key.
   ** @param locale              the locale to look for.
   ** @param  arguments          the objects to be formatted and substituted.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String clazzName, final String key, Locale locale, final Object... arguments) {
    final String pattern = stringSafely(resourceBundle(clazzName, locale), key, null);
    return arguments.length == 0 ? pattern : MessageFormat.format(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle
  /**
   ** Method for fetching a {@link ResourceBundle}.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Bundle" or {PACKAGE_NAME} + "Bundle".
   **
   ** @return                    the {@link ResourceBundle}.
   */
  public static ResourceBundle resourceBundle(final String clazzName) {
    return resourceBundle(clazzName, locale());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle
  /**
   ** Method for fetching a {@link ResourceBundle}.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Bundle" or {PACKAGE_NAME} + "Bundle".
   ** @param locale              the locale to look for.
   **
   ** @return                    the {@link ResourceBundle}.
   */
  public static ResourceBundle resourceBundle(final String clazzName, final Locale locale) {
    // load the resource bundle
    return resourceBundle(clazzName, locale, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle
  /**
   ** Method for fetching a {@link ResourceBundle}.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Bundle" or {PACKAGE_NAME} + "Bundle".
   ** @param locale              the locale to look for.
   ** @param  timeZone           the time zone used in formatting.
   **
   ** @return                    the {@link ResourceBundle}.
   */
  public static ResourceBundle resourceBundle(final String clazzName, final Locale locale, final TimeZone timeZone) {
    // load the resource bundle
    return ResourceBundle.instance(clazzName, locale, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Returns a locale for the ADF context of the current thread.
   ** <p>
   ** This locale will only be returned if an environment has not been defined
   ** for the {@link ADFContext}. The environment may not be defined if the
   ** {@link ADFContext} is acquired from a java application.
   **
   ** @return                    the current locale.
   */
  public static Locale locale() {
    return current().getLocale();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   current
  /**
   ** Returns the ADF context for the current thread.
   ** <p>
   ** <code>current</code> will automatically initialize a
   ** <code>DefaultContext</code> if one does not exist. This may result in
   ** memory leaks if the calling thread is reused before
   ** #resetADFContext(Object) is invoked.
   ** <p>
   ** An improper {@link ADFContext} is initialized if that context
   ** implementation does not match the environment that is using it. For
   ** example, if a caller is executing in a web environment than the proper
   ** {@link ADFContext} implementation would be <code>ServletADFContext</code>.
   ** The <code>DefaultContext</code> implementation would be improper for use
   ** in the web environment. The proper context may be initialized by
   ** invoking <code>initADFContext</code> with the required external context
   ** (the <code>ServletContext</code>, etc. when used in a web environment).
   ** <p>
   ** The use of an improper {@link ADFContext} could result in memory leaks if
   ** the {@link ADFContext} is not properly reset when the calling thread is
   ** done using it. An improper use could also lead to unexpected application
   ** behaviour if an {@link ADFContext} client initializes an
   ** {@link ADFContext} state that is required by another thread which is using
   ** the proper {@link ADFContext}. Using <code>initADFContext</code> with
   ** <code>findCurrent</code> can help diagnose/avoid these potential issues.
   **
   ** @return                    the ADF context for the current thread.
   */
  public static ADFContext current() {
    return ADFContext.getCurrent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeBindingValue
  /**
   ** Sets attribute value through attribute binding.
   ** <br>
   ** A convenience method for setting the value of a bound attribute in the
   ** context of the current page.
   **
   ** @param  attributeName      the name of the {@link AttributeBinding} to
   **                            set.
   ** @param  attributeValue     the value to set.
   **
   ** @throws IllegalArgumentException if no {@link AttributeBinding} exists for
   **                                  name.
   */
  public static void attributeBindingValue(final String attributeName, final Object attributeValue) {
    final AttributeBinding binding = attributeBinding(attributeName);
    if (binding != null)
      binding.setInputValue(attributeValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeBindingValue
  /**
   ** Returns attribute value using attribute binding.
   ** <br>
   ** A convenience method for getting the value of a bound attribute in the
   ** current page context programatically.
   **
   ** @param  <T>                the type of the binding attribute.
   ** @param  attributeName      the name of the attribute the value has to be
   **                            returned from the {@link AttributeBinding}
   **                            container.
   **
   ** @return                    the type attribute value from the binding found
   **                            for name.
   **
   ** @throws IllegalArgumentException if name is not found.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T attributeBindingValue(final String attributeName) {
    final AttributeBinding binding = attributeBinding(attributeName);
    if (binding == null)
      throw new IllegalArgumentException(String.format(BINDING_NOT_EXISTS, attributeName ));

    return (T)binding.getInputValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listBindingValue
  /**
   ** Returns a unformatted attribute value of the first attribute value using
   ** list binding.
   **
   ** @param  <T>                the type of the binding attribute.
   ** @param  name               the name of the {@link JUCtrlListBinding}  to
   **                            lookup.
   **
   ** @return                    the type attribute value from the binding found
   **                            for name.
   **
   ** @throws IllegalArgumentException if no {@link JUCtrlListBinding} exists
   **                                  for name or the binding found for name is
   **                                  not a {@link JUCtrlListBinding}.
   */
  @SuppressWarnings({"unchecked", "cast"})
 public static <T> T listBindingValue(final String name) {
    final JUCtrlListBinding binding = listBinding(name);
    return (T)binding.getAttributeValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeBinding
  /**
   ** Returns a attribute binding with the given name.
   **
   ** @param  name               the name of the {@link AttributeBinding}  to
   **                            lookup.
   **
   ** @return                    the {@link AttributeBinding} that matches the
   **                            given name.
   **
   ** @throws IllegalArgumentException if no {@link AttributeBinding} exists for
   **                                  name,
   */
  public static AttributeBinding attributeBinding(final String name) {
    final ControlBinding control = controlBinding(name);
    AttributeBinding     binding = null;
    if (control != null && (control instanceof AttributeBinding))
      binding = (AttributeBinding)control;

    return binding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listBinding
  /**
   ** Returns a list binding with the given name.
   **
   ** @param  name               the name of the {@link JUCtrlListBinding}  to
   **                            lookup.
   **
   ** @return                    the {@link JUCtrlListBinding} that matches the
   **                            given name.
   **
   ** @throws IllegalArgumentException if no {@link JUCtrlListBinding} exists
   **                                  for name or the binding found for name is
   **                                  not a {@link JUCtrlListBinding}.
   */
  public static JUCtrlListBinding listBinding(final String name) {
    final ControlBinding binding = controlBinding(name);
    if (!(binding instanceof JUCtrlListBinding))
      throw new IllegalArgumentException(String.format(BINDING_NOT_LIST, name));

    return (JUCtrlListBinding)binding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publishEvent
  /**
   ** Publish an <code>Event</code> programatically.
   **
   ** @param  eventName          the name of the {@link JUEventBinding}  to
   **                            lookup.
   ** @param  payload            the event payload.
   **
   ** @throws IllegalArgumentException if no {@link JUEventBinding} exists
   **                                  for <code>eventName</code> or the binding
   **                                  found for <code>eventName</code> is not a
   **                                  {@link JUEventBinding}.
   */
  public static final void publishEvent(final String eventName, final Object payload) {
    final DCBindingContainer binding    = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
    final JUEventBinding     action     = (JUEventBinding)binding.findCtrlBinding(eventName);
    final EventDispatcher    dispatcher = binding.getEventDispatcher();
    dispatcher.queueEvent(action, payload);
    dispatcher.processContextualEvents();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publishContextualEvent
  /**
   ** Publish a <code>Contextaul Event</code> programatically.
   **
   ** @param  eventName          the name of the {@link JUCtrlActionBinding}  to
   **                            lookup.
   ** @param  payload            the event payload.
   **
   ** @throws IllegalArgumentException if no {@link JUCtrlActionBinding} exists
   **                                  for <code>eventName</code> or the binding
   **                                  found for <code>eventName</code> is not a
   **                                  {@link JUCtrlActionBinding}.
   */
  public static final void publishContextualEvent(final String eventName, final String payload) {
    final DCBindingContainer  binding    = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
    final JUCtrlActionBinding action     = (JUCtrlActionBinding)binding.getControlBinding(eventName);
    final EventDispatcher     dispatcher = binding.getEventDispatcher();
    dispatcher.queueEvent(action.getEventProducer(), payload);
    dispatcher.processContextualEvents();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controlBinding
  /**
   ** Returns a control binding with the given name.
   **
  ** @param  name               the name of the {@link ControlBinding}  to
   **                            lookup.
   **
   ** @return                    the {@link ControlBinding} that matches the
   **                            given name or <code>null</code> if name is not
   **                            found.
    **
   ** @throws IllegalArgumentException if name is not found.
   */
  public static ControlBinding controlBinding(final String name) {
    final ControlBinding binding = binding().getControlBinding(name);
    if (binding == null)
      throw new IllegalArgumentException(String.format(BINDING_NOT_FOUND, name));

    return binding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteratorBindingValue
  /**
   ** Returns the value for attribute with name <code>attribute</code> from a
   ** iterator binding with the given name.
   **
   ** @param  <T>                the type of the iterator attribute.
   ** @param  name               the name of the {@link DCIteratorBinding} to
   **                            lookup.
   ** @param  attribute          the name of the attributes to fetch form the
   **                            <code>iterator</code>.
   ** @param  clazz              the type of the result the attribute value will
   **                            be coerced to after evaluation.
   **
   ** @return                    the value for attribute with name
   **                            <code>attribute</code> from a iterator binding
   **                            with the given name.
   **
   ** @throws IllegalArgumentException if iteratorName is not found.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T iteratorBindingValue(final String name, final String attribute, final Class<T> clazz) {
	  final DCIteratorBinding iterator = iteratorBinding(name);
    final Row               row = iterator.getCurrentRow();
    return (row == null) ? null : (T)row.getAttribute(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteratorBinding
  /**
   ** Returns a iterator binding with the given name.
   **
   ** @param  name               the name of the {@link DCIteratorBinding}  to
   **                            lookup.
   **
   ** @return                    the {@link DCIteratorBinding} that matches the
   **                            given name or <code>null</code> if name is not
   **                            found.
   **
   ** @throws IllegalArgumentException if name is not found.
   */
  public static DCIteratorBinding iteratorBinding(final String name) {
    final DCIteratorBinding binding = binding().findIteratorBinding(name);
    if (binding == null)
      throw new IllegalArgumentException(String.format(ITERATOR_NOT_FOUND, name));

    return binding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeActionSucceeded
  /**
   ** Validates if the execution of an {@link OperationBinding} was succeed.
   **
   ** @param  value              the value to test.
   **
   ** @return                    <code>true</code> if the result of the
   **                            previously executed operation isn't the
   **                            passed value; otherwise <code>false</code>.
   */
  public static boolean executeActionSucceeded(final Object value) {
    if ((value instanceof String) && EXECUTE_OPERATION_FAILED.equals(value))
      return false;

    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeAction
  /**
   ** Executes an {@link OperationBinding}, for example, as a result of a UI
   ** button being pressed.
   ** <br>
   ** This is translated into invoking some operation on the underlying data
   ** control.
   ** <p>
   ** If there are exceptions within the invoke operation, the exceptions is
   ** reported.
   **
   ** @param  bindingName        the name of the {@link OperationBinding} to
   **                            execute.
   **
   ** @return                    the result of the operation (<code>null</code>
   **                            if the operation has no return type (void).
   */
  public static Object executeAction(final String bindingName) {
    return executeAction(bindingName, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeAction
  /**
   ** Executes an {@link OperationBinding}, for example, as a result of a UI
   ** button being pressed.
   ** <br>
   ** This is translated into invoking some operation on the underlying data
   ** control.
   ** <p>
   ** If there are exceptions within the invoke operation, the exceptions is
   ** reported.
   **
   ** @param  bindingName        the name of the {@link OperationBinding} to
   **                            execute.
   ** @param  parameter          a {@link Map} of name-value pairs of arguments
   **                            and argument values to be passed to the bound
   **                            operation.
   **
   ** @return                    the result of the operation (<code>null</code>
   **                            if the operation has no return type (void).
   */
  public static Object executeAction(final String bindingName, final Map parameter) {
    Object result = executeOperation(bindingName, parameter);
    final DCBindingContainer binding = binding();
    final List errors = binding.getExceptionsList();
    if ((errors != null) && (!errors.isEmpty())) {
      final Object exception = errors.get(0);
      if ((exception instanceof Throwable)) {
        final DCBindingContainer current = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        current.reportException((Throwable)exception);
        result = EXECUTE_OPERATION_FAILED;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeOperation
  /**
   ** Executes an {@link OperationBinding}, for example, as a result of a UI
   ** button being pressed.
   ** <br>
   ** This is translated into invoking some operation on the underlying data
   ** control.
   ** <p>
   ** If there are exceptions within the invoke operation, the exceptions is
   ** accessible via getErrors().
   **
   ** @param  bindingName        the name of the {@link OperationBinding} to
   **                            execute.
   **
   ** @return                    the result of the operation (<code>null</code>
   **                            if the operation has no return type (void).
   */
  public static Object executeOperation(final String bindingName) {
    return executeOperation(bindingName, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeOperation
  /**
   ** Executes an {@link OperationBinding}, for example, as a result of a UI
   ** button being pressed.
   ** <br>
   ** This is translated into invoking some operation on the underlying data
   ** control.
   ** <p>
   ** If there are exceptions within the invoke operation, the exceptions is
   ** accessible via getErrors().
   **
   ** @param  bindingName        the name of the {@link OperationBinding} to
   **                            execute.
   ** @param  parameter          a {@link Map} of name-value pairs of arguments
   **                            and argument values to be passed to the bound
   **                            operation.
   **
   ** @return                    the result of the operation (<code>null</code>
   **                            if the operation has no return type (void).
   */
  @SuppressWarnings("unchecked")
  public static Object executeOperation(final String bindingName, final Map parameter) {
    final OperationBinding operationBinding = operationBinding(bindingName);
    if (parameter != null)
      operationBinding.getParamsMap().putAll(parameter);

    return operationBinding.execute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationBinding
  /**
   ** Returns a operation binding with the given name.
   **
  ** @param  name                the name of the {@link OperationBinding} to
   **                            lookup.
   **
   ** @return                    the {@link OperationBinding} that matches the
   **                            given name or <code>null</code> if name is not
   **                            found.
    **
   ** @throws IllegalArgumentException if name is not found.
   */
  public static OperationBinding operationBinding(final String name) {
    final OperationBinding binding = binding().getOperationBinding(name);
    if (binding == null)
      throw new IllegalArgumentException(String.format(OPERATION_NOT_FOUND, name));

    return binding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binding
  /**
   ** Returns the binding container singleton.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the binding container singleton.
   **
   ** @see    FacesContext#getApplication()
   */
  public static DCBindingContainer binding() {
    final ExpressionFactory factory    = JSF.expressionFactory();
    final ELContext         context    = JSF.expressionContext();
    final ValueExpression   expression = factory.createValueExpression(context, "#{bindings}", Object.class);
    return (DCBindingContainer)expression.getValue(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationModule
  /**
   ** Returns the application module for an application module data control by
   ** name.
   **
   ** @param  name               the application module data control name.
   **
   ** @return                    the [@link ApplicationModule} for
   **                            <code>name</code>
   */
  public static ApplicationModule applicationModule(final String name) {
    return JSF.valueFromExpression("#{data." + name + ".dataProvider}", ApplicationModule.class);

  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   helpUrl
  /**
   ** Returns the URL to an external help page.
   ** <br>
   ** This is used to give the user extensive details about the current
   ** help-topic. This is usually a link to an external help framework, like
   ** OHW.
   **
   ** @param  component          the component that this HelpTopic is for.
   **                            This may be <code>null</code>.
   **                            Help implementors may provide different help
   **                            depending on the component state, eg: when the
   **                            component is disabled.
   ** @param  helpTopicId        the help topic referencing the URL to return.
   **
   ** @return                    the URL to an external help page.
   */
  public static String helpUrl(final UIComponent component, final String helpTopicId) {
    if (helpTopicId == null) {
      return null;
    }
    final HelpProvider provider = context().getHelpProvider();
    if (provider == null) {
      return null;
    }
    final HelpTopic helpTopic = provider.getHelpTopic(JSF.context(), component, helpTopicId);
    return helpTopic != null ? helpTopic.getExternalUrl() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partialRender
  /**
   ** Add a component as a partial target.
   ** <p>
   ** In response to a partial event, only components registered as partial
   ** targets are re-rendered. For a component to be successfully re-rendered
   ** when it is manually added with this API, it should have the
   ** "clientComponent" attribute set to <code>true</code>. If not, partial
   ** re-rendering may or may not work depending on the component.
   **
   ** @param  id                 the identifier of the {@link UIComponent} to
   **                            re-render.
   */
  public static void partialRender(final String id) {
    final UIComponent component = JSF.find(id);
    if (component != null)
      partialRender(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partialRender
  /**
   ** Add a component as a partial target.
   ** <p>
   ** In response to a partial event, only components registered as partial
   ** targets are re-rendered. For a component to be successfully re-rendered
   ** when it is manually added with this API, it should have the
   ** "clientComponent" attribute set to <code>true</code>. If not, partial
   ** re-rendering may or may not work depending on the component.
   **
   ** @param  base               the component from which the relative search is
   **                            started.
   ** @param  id                 the identifier of the {@link UIComponent} to
   **                            re-render.
   */
  public static void partialRender(final UIComponent base, final String id) {
    partialRender(base.findComponent(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partialRender
  /**
   ** Add a component as a partial target.
   ** <p>
   ** In response to a partial event, only components registered as partial
   ** targets are re-rendered. For a component to be successfully re-rendered
   ** when it is manually added with this API, it should have the
   ** "clientComponent" attribute set to <code>true</code>. If not, partial
   ** re-rendering may or may not work depending on the component.
   **
   ** @param  component          the {@link UIComponent} to re-render.
   */
  public static void partialRender(final UIComponent component) {
    if (component != null) {
      AdfFacesContext.getCurrentInstance().addPartialTarget(component);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeBooleanValue
  /**
   ** Returns the boolean value from the {@link Map} of objects at "view"
   ** scope for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated string value is to be
   **                            returned.
   **
   ** @return                    the string value from the {@link Map} of
   **                            objects at "view" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static Boolean viewScopeBooleanValue(final String name) {
    return (Boolean)viewScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeBooleanValue
  /**
   ** Sets the boolean value in the {@link Map} of objects at "view" scope
   ** for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key with which the specified
   **                            <code>value</code> is to be associated.
   ** @param  value              the value to be associated with the specified
   **                            <code>name</code>.
   **
   ** @return                    the previous value associated with
   **                            <code>name</code> in the {@link Map} of objects
   **                            at "view" scope, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   **                            (A <code>null</code> return can also indicate
   **                            that the {@link Map} previously associated
   **                            <code>null</code> with <code>name</code>, if
   **                            the implementation supports <code>null</code>
   **                            values.)
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static Boolean viewScopeBooleanValue(final String name, final Boolean value) {
    return (Boolean)viewScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeIntegerValue
  /**
   ** Returns the integer value from the {@link Map} of objects at "view"
   ** scope for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated string value is to be
   **                            returned.
   **
   ** @return                    the string value from the {@link Map} of
   **                            objects at "view" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static Integer viewScopeIntegerValue(final String name) {
    return (Integer)viewScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeIntegerValue
  /**
   ** Sets the integer value in the {@link Map} of objects at "view" scope
   ** for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key with which the specified
   **                            <code>value</code> is to be associated.
   ** @param  value              the value to be associated with the specified
   **                            <code>name</code>.
   **
   ** @return                    the previous value associated with
   **                            <code>name</code> in the {@link Map} of objects
   **                            at "view" scope, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   **                            (A <code>null</code> return can also indicate
   **                            that the {@link Map} previously associated
   **                            <code>null</code> with <code>name</code>, if
   **                            the implementation supports <code>null</code>
   **                            values.)
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static Integer viewScopeIntegerValue(final String name, final Integer value) {
    return (Integer)viewScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeLongValue
  /**
   ** Returns the long value from the {@link Map} of objects at "view"
   ** scope for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated string value is to be
   **                            returned.
   **
   ** @return                    the string value from the {@link Map} of
   **                            objects at "view" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static Long viewScopeLongValue(final String name) {
    return (Long)viewScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeLongValue
  /**
   ** Sets the long value in the {@link Map} of objects at "view" scope
   ** for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key with which the specified
   **                            <code>value</code> is to be associated.
   ** @param  value              the value to be associated with the specified
   **                            <code>name</code>.
   **
   ** @return                    the previous value associated with
   **                            <code>name</code> in the {@link Map} of objects
   **                            at "view" scope, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   **                            (A <code>null</code> return can also indicate
   **                            that the {@link Map} previously associated
   **                            <code>null</code> with <code>name</code>, if
   **                            the implementation supports <code>null</code>
   **                            values.)
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static Long viewScopeLongValue(final String name, final Long value) {
    return (Long)viewScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeStringValue
  /**
   ** Returns the string value from the {@link Map} of objects at "view"
   ** scope for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated string value is to be
   **                            returned.
   **
   ** @return                    the string value from the {@link Map} of
   **                            objects at "view" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static String viewScopeStringValue(final String name) {
    return (String)viewScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeStringValue
  /**
   ** Sets the string value in the {@link Map} of objects at "view" scope
   ** for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key with which the specified
   **                            <code>value</code> is to be associated.
   ** @param  value              the value to be associated with the specified
   **                            <code>name</code>.
   **
   ** @return                    the previous value associated with
   **                            <code>name</code> in the {@link Map} of objects
   **                            at "view" scope, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   **                            (A <code>null</code> return can also indicate
   **                            that the {@link Map} previously associated
   **                            <code>null</code> with <code>name</code>, if
   **                            the implementation supports <code>null</code>
   **                            values.)
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static String viewScopeStringValue(final String name, final String value) {
    return (String)viewScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScopeValue
  /**
   ** Returns the value from the {@link Map} of objects at "view" scope for
   ** the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value from the {@link Map} of objects at
   **                            "view" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static Object viewScopeValue(final String name) {
    return viewScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewScope
  /**
   ** Returns a {@link Map} of objects at "view" scope.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    a {@link Map} of objects at "view" scope.
   **
   ** @see    AdfFacesContext#getViewScope()
   */
  public static Map<String, Object> viewScope() {
    return context().getViewScope();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeBooleanValue
  /**
   ** Returns the boolean value from the {@link Map} of objects at "pageFlow"
   ** scope for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated string value is to be
   **                            returned.
   **
   ** @return                    the string value from the {@link Map} of
   **                            objects at "pageFlow" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static Boolean pageFlowScopeBooleanValue(final String name) {
    return (Boolean)pageFlowScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeBooleanValue
  /**
   ** Sets the boolean value in the {@link Map} of objects at "pageFlow" scope
   ** for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key with which the specified
   **                            <code>value</code> is to be associated.
   ** @param  value              the value to be associated with the specified
   **                            <code>name</code>.
   **
   ** @return                    the previous value associated with
   **                            <code>name</code> in the {@link Map} of objects
   **                            at "pageFlow" scope, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   **                            (A <code>null</code> return can also indicate
   **                            that the {@link Map} previously associated
   **                            <code>null</code> with <code>name</code>, if
   **                            the implementation supports <code>null</code>
   **                            values.)
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static Boolean pageFlowScopeBooleanValue(final String name, final Boolean value) {
    return (Boolean)pageFlowScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeIntegerValue
  /**
   ** Returns the integer value from the {@link Map} of objects at "pageFlow"
   ** scope for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated string value is to be
   **                            returned.
   **
   ** @return                    the string value from the {@link Map} of
   **                            objects at "pageFlow" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static Integer pageFlowScopeIntegerValue(final String name) {
    return (Integer)pageFlowScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeIntegerValue
  /**
   ** Sets the integer value in the {@link Map} of objects at "pageFlow" scope
   ** for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key with which the specified
   **                            <code>value</code> is to be associated.
   ** @param  value              the value to be associated with the specified
   **                            <code>name</code>.
   **
   ** @return                    the previous value associated with
   **                            <code>name</code> in the {@link Map} of objects
   **                            at "pageFlow" scope, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   **                            (A <code>null</code> return can also indicate
   **                            that the {@link Map} previously associated
   **                            <code>null</code> with <code>name</code>, if
   **                            the implementation supports <code>null</code>
   **                            values.)
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static Integer pageFlowScopeIntegerValue(final String name, final Integer value) {
    return (Integer)pageFlowScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeLongValue
  /**
   ** Returns the long value from the {@link Map} of objects at "pageFlow"
   ** scope for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated string value is to be
   **                            returned.
   **
   ** @return                    the string value from the {@link Map} of
   **                            objects at "pageFlow" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static Long pageFlowScopeLongValue(final String name) {
    return (Long)pageFlowScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeLongValue
  /**
   ** Sets the long value in the {@link Map} of objects at "pageFlow" scope
   ** for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key with which the specified
   **                            <code>value</code> is to be associated.
   ** @param  value              the value to be associated with the specified
   **                            <code>name</code>.
   **
   ** @return                    the previous value associated with
   **                            <code>name</code> in the {@link Map} of objects
   **                            at "pageFlow" scope, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   **                            (A <code>null</code> return can also indicate
   **                            that the {@link Map} previously associated
   **                            <code>null</code> with <code>name</code>, if
   **                            the implementation supports <code>null</code>
   **                            values.)
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static Long pageFlowScopeLongValue(final String name, final Long value) {
    return (Long)pageFlowScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeStringValue
  /**
   ** Returns the string value from the {@link Map} of objects at "pageFlow"
   ** scope for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated string value is to be
   **                            returned.
   **
   ** @return                    the string value from the {@link Map} of
   **                            objects at "pageFlow" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static String pageFlowScopeStringValue(final String name) {
    return (String)pageFlowScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeStringValue
  /**
   ** Sets the string value in the {@link Map} of objects at "pageFlow" scope
   ** for the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key with which the specified
   **                            <code>value</code> is to be associated.
   ** @param  value              the value to be associated with the specified
   **                            <code>name</code>.
   **
   ** @return                    the previous value associated with
   **                            <code>name</code> in the {@link Map} of objects
   **                            at "pageFlow" scope, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   **                            (A <code>null</code> return can also indicate
   **                            that the {@link Map} previously associated
   **                            <code>null</code> with <code>name</code>, if
   **                            the implementation supports <code>null</code>
   **                            values.)
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static String pageFlowScopeStringValue(final String name, final String value) {
    return (String)pageFlowScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScopeValue
  /**
   ** Returns the value from the {@link Map} of objects at "pageFlow" scope for
   ** the given <code>name</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  name               the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value from the {@link Map} of objects at
   **                            "pageFlow" to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the {@link Map} contains
   **                            no mapping for <code>name</code>.
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static Object pageFlowScopeValue(final String name) {
    return pageFlowScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageFlowScope
  /**
   ** Returns a {@link Map} of objects at "pageFlow" scope.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    a {@link Map} of objects at "pageFlow" scope.
   **
   ** @see    AdfFacesContext#getPageFlowScope()
   */
  public static Map<String, Object> pageFlowScope() {
    return context().getPageFlowScope();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the {@link AdfFacesContext} active for the current thread.
   **
   ** @return                    the {@link AdfFacesContext} active for the
   **                            current thread.
   **
   ** @see    AdfFacesContext#getCurrentInstance()
   */
  public static AdfFacesContext context() {
    return AdfFacesContext.getCurrentInstance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Returns the default TimeZone used for interpreting and formatting date
   ** values.
   **
   ** @return                    the default TimeZone used for interpreting and
   **                            formatting date values.
   */
  public static TimeZone timeZone() {
    return requestContext().getTimeZone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestContext
  /**
   ** Returns the {@link RequestContext} active for the current thread.
   **
   * @return                     the {@link RequestContext} active for the
   *                             current thread.
   */
  public static final RequestContext requestContext() {
    return RequestContext.getCurrentInstance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskflowContext
  /**
   ** Returns the {@link TaskFlowContext} for the task flow on top of the stack
   ** corresponding to the view port.
   ** <br>
   ** If the view port is not currently executing a task flow, throws a
   ** RuntimeException.
   **
   ** @return                    the current {@link TaskFlowContext}.
   **
   ** @see    ViewPortContext#getTaskFlowContext()
   */
  public static TaskFlowContext taskflowContext() {
    return viewPort().getTaskFlowContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewPath
  /**
   ** Set the view activity to be displayed in the view port.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This method may only be called during the <i>INVOKE_APPLICATION</i> phase
   ** of the request lifecycle.
   **
   ** @param  path                the ID of a view activity within the view
   **                             port's current task flow.
   */
  public static void viewPath(final String path) {
    viewPort().setViewId(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewPath
  /**
   ** Returns the View Id corresponding to the current view activity.
   ** <br>
   ** If the current activity is not a view activity, returns <code>null</code>.
   **
   ** @return                    current View Id or <code>null</code>
   */
  public static String viewPath() {
    return viewPort().getViewId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootPort
  /**
   ** Returns a {@link ViewPortContext} corresponding to the current root view
   ** port.
   ** <br>
   ** The root view port is the view port for the current browser window/tab.
   **
   ** @return                    the current root ViewPortContext
   */
  public static ViewPortContext rootPort() {
    return controllerContext().getCurrentRootViewPort();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewPort
  /**
   ** Returns a {@link ViewPortContext} corresponding to the current view port.
   ** <br>
   ** If called from the context of a region the view port will represent the
   ** region, if called from the context of the root page the view port will
   ** represent the browser window/tab.
   **
   ** @return                    the current {@link ViewPortContext}.
   */
  public static ViewPortContext viewPort() {
    return controllerContext().getCurrentViewPort();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controllerContext
  /**
   ** Returns the {@link ControllerContext} active for the current thread.
   **
   ** @return                    the {@link ControllerContext} active for the
   **                            current thread.
   **
   ** @see    ControllerContext#getInstance()
   */
  public static ControllerContext controllerContext() {
    return ControllerContext.getInstance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringSafely
  /**
   ** Internal method to proxy for resource keys that don't exist.
   **
   ** @param  bundle             the {@link ResourceBundle} the desired resource
   **                            is provided by.
   ** @param  key                string message key.
   ** @param  defaultValue       placeholder string if the key isn't mapped in
   **                            the resource bundle.
   **
   ** @return                    resource choice or placeholder error String
   */
  private static String stringSafely(final ResourceBundle bundle, final String key, final String defaultValue) {
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