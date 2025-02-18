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

    Copyright © 2024 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   OAuth Registration

    File        :   ADFFacesUtils.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.utils;

import java.io.IOException;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControlFrame;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCControlBinding;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.AttributeBinding;
import oracle.binding.ControlBinding;

import oracle.iam.ui.platform.utils.TaskFlowUtils;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.jbo.uicli.binding.JUCtrlActionBinding;
import oracle.jbo.uicli.binding.JUCtrlListBinding;
import oracle.jbo.uicli.binding.JUEventBinding;

////////////////////////////////////////////////////////////////////////////////
// class ADFFacesUtils
// ~~~~~ ~~~~~~~~
/**
 ** List of ADF Faces Utils method used by TaskFlow Managed Beans
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ADFFacesUtils {

  private static final String className = ADFFacesUtils.class.getName();
  private static Logger       logger = Logger.getLogger(className);  

  private ADFFacesUtils() {
    // do not instantiate
    throw new AssertionError();
  }


  //////////////////////////////////////////////////////////////////////////////  
  // ADF Faces util methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getUIComponent 
  /**
   ** Find JSF component based on the component name
   ** @param name Name of the JSF component
   ** @return JSF Component
   */
  public static UIComponent getUIComponent(String name) {
    String methodName = "getUIComponent";
    logger.entering(className, methodName);
    
    FacesContext facesCtx = FacesContext.getCurrentInstance();
    
    logger.exiting(className, methodName);
    return facesCtx.getViewRoot().findComponent(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: partialRender 
  /**
   ** Re-render the component in the browser
   ** @param component JSF Component
   */
  public static void partialRender(UIComponent component) {
    String methodName = "partialRender";
    logger.entering(className, methodName);
    
    if (component != null) {
      AdfFacesContext.getCurrentInstance().addPartialTarget(component);
    }
    
    logger.exiting(className, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: partialRender
  /**
   ** Re-render the component in the browser based on the component id
   ** @param id Componet id
   */
  public static void partialRender(String id) {
    String methodName = "partialRender";
    logger.entering(className, methodName);
    
    UIComponent component = getUIComponent(id);
    if (component != null) {
      AdfFacesContext.getCurrentInstance().addPartialTarget(component);
    }
    
    logger.exiting(className, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: setAttributeBindingValue
  /**
   ** Sets attribute value through attribute binding.
   ** @param attributeName Binding attribute name
   ** @param value Attribute value
   */
  public static void setAttributeBindingValue(String attributeName, Object value) {
    String methodName = "setAttributeBindingValue";
    logger.entering(className, methodName);
    
    AttributeBinding binding = getAttributeBinding(attributeName);
    if (binding != null) {
      binding.setInputValue(value);
    } else {
      throw new IllegalArgumentException("Binding " + attributeName + " does not exist.");
    }
    
    logger.exiting(className, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getAttributeBindingValue
  /**
   ** Get binding attriubte value
   ** @param attributeName Binding attribute name
   ** @param clazz Type of the class which is going to be returned
   ** @return Binding attribute name
   */
  public static <T> T getAttributeBindingValue(String attributeName, Class<T> clazz) {
    String methodName = "getAttributeBindingValue";
    logger.entering(className, methodName);
    
    AttributeBinding binding = getAttributeBinding(attributeName);
    if (binding != null) {
      logger.exiting(className, methodName);
      return (T) binding.getInputValue();
    } else {
      logger.exiting(className, methodName);
      throw new IllegalArgumentException("Binding " + attributeName + " does not exist.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getListBindingValue
  /**
   ** Gets attribute value using list binding.
   ** @param attributeName Binding attribute name
   ** @param clazz Type of the class which is going to be returned
   ** @return Binding attribute value
   */
  public static <T> T getListBindingValue(String attributeName, Class<T> clazz) {
    String methodName = "launchCreateClient";
    logger.entering(className, methodName);
    
    ControlBinding ctrlBinding = getControlBinding(attributeName);
    if (ctrlBinding instanceof JUCtrlListBinding) {
      JUCtrlListBinding listBinding = (JUCtrlListBinding) ctrlBinding;
      logger.exiting(className, methodName);
      return (T) listBinding.getAttributeValue();
    } else {
      logger.exiting(className, methodName);
      throw new IllegalArgumentException("Binding " + attributeName + " is not list binding.");
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getControlBinding
  /**
   ** Get ADF Control Binding
   ** @param name Binding name
   ** @return ADF Control Binding
   */
  public static ControlBinding getControlBinding(String name) {
    String methodName = "getControlBinding";
    logger.entering(className, methodName);
    
    ControlBinding crtlBinding = getBindings().getControlBinding(name);
    if (crtlBinding == null) {
      throw new IllegalArgumentException("Control Binding '" + name + "' not found");
    }
    
    logger.exiting(className, methodName);
    return crtlBinding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getAttributeBinding
  /**
   ** Get ADF Attribute Binding
   ** @param name ADF Attribute Binding name
   ** @return ADF Attribute Binding
   */
  public static AttributeBinding getAttributeBinding(String name) {
    String methodName = "getAttributeBinding";
    logger.entering(className, methodName);
    
    ControlBinding ctrlBinding = getControlBinding(name);
    AttributeBinding attributeBinding = null;
    if (ctrlBinding != null) {
      if (ctrlBinding instanceof AttributeBinding) {
        attributeBinding = (AttributeBinding) ctrlBinding;
      }
    }
    
    logger.exiting(className, methodName);
    return attributeBinding;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getBindings  
  /**
   ** Get ADF DC Binding Container
   ** @return DC Binding Container
   */
  public static DCBindingContainer getBindings() {
    String methodName = "getBindings";
    logger.entering(className, methodName);
    
    FacesContext fc = FacesContext.getCurrentInstance();
    ExpressionFactory exprfactory = fc.getApplication().getExpressionFactory();
    ELContext elctx = fc.getELContext();
    ValueExpression valueExpression = exprfactory.createValueExpression(elctx, "#{bindings}", Object.class);
    DCBindingContainer dcbinding = (DCBindingContainer) valueExpression.getValue(elctx);
    
    logger.exiting(className, methodName);
    return dcbinding;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getValueFromELExpression
  /**
   ** Evaluates EL expression and returns value.
   ** @param expression EL expression
   ** @param clazz Type of returned data
   ** @return Data from EL Expression
   */
  public static <T> T getValueFromELExpression(String expression, Class<T> clazz) {
    String methodName = "getValueFromELExpression";
    logger.entering(className, methodName);
    
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application app = facesContext.getApplication();
    ExpressionFactory elFactory = app.getExpressionFactory();
    ELContext elContext = facesContext.getELContext();
    ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, clazz);
    
    logger.exiting(className, methodName);
    return (T) valueExp.getValue(elContext);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getMethodExpressionFromEL  
  /**
   ** Gets MethodExpression based on the EL expression. MethodExpression can then be used to invoke the method.
   ** @param expression EL expression
   ** @param returnType Return type
   ** @param paramTypes Parameters types
   ** @return Return method data
   */
  public static MethodExpression getMethodExpressionFromEL(String expression, Class<?> returnType, Class[] paramTypes) {
    String methodName = "getMethodExpressionFromEL";
    logger.entering(className, methodName);
    
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application app = facesContext.getApplication();
    ExpressionFactory elFactory = app.getExpressionFactory();
    ELContext elContext = facesContext.getELContext();
    MethodExpression methodExp = elFactory.createMethodExpression(elContext, expression, returnType, paramTypes);
    
    logger.exiting(className, methodName);
    return methodExp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getELContext  
  /**
   ** Get Expression Laguage Context
   ** @return Expression Laguage Context
   */
  public static ELContext getELContext() {
    return FacesContext.getCurrentInstance().getELContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: showFacesMessage
  /**
   ** Shows FacesMessage. The message will not be bound to any component.
   ** @param fm Faces Message
   */
  public static void showFacesMessage(FacesMessage fm) {
    FacesContext.getCurrentInstance().addMessage(null, fm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: launchTaskFlow
  /**
   ** Launch bounded taskFlow based on provided parameters.
   ** @param id Id of the taskflow instance
   ** @param taskFlowId Taskflow definition id
   ** @param name TaskFLow name
   ** @param icon TaskFlow icon
   ** @param description TaskFlow description
   ** @param helpTopicId TaskFlow help information
   ** @param inDialog If set to true task flow is opened in dialog otherwise as tab
   ** @param params Map of the input parameters
   */
  public static void launchTaskFlow(String id, String taskFlowId, String name, String icon, String description,
                                    String helpTopicId, boolean inDialog, Map<String, Object> params) {
    String methodName = "launchTaskFlow";
    logger.entering(className, methodName);
    
    // create JSON payload for the contextual event
    logger.fine("Lanching taskflow with id: "+taskFlowId +", and parameters: "+params);
    String jsonPayLoad = TaskFlowUtils.createContextualEventPayLoad(id, 
                                                                    taskFlowId, 
                                                                    name, 
                                                                    icon, 
                                                                    description, 
                                                                    helpTopicId, 
                                                                    inDialog,
                                                                    params);

    // create and enqueue contextual event
    DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
    DCControlBinding ctrlBinding = bc.findCtrlBinding(TaskFlowUtils.RAISE_TASK_FLOW_LAUNCH_EVENT);
    // support both bindings - using eventBinding as well as methodAction
    if (ctrlBinding instanceof JUEventBinding) {
      JUEventBinding eventProducer = (JUEventBinding) ctrlBinding;
      bc.getEventDispatcher().queueEvent(eventProducer, jsonPayLoad);
    } else if (ctrlBinding instanceof JUCtrlActionBinding) {
      JUCtrlActionBinding actionBinding = (JUCtrlActionBinding) ctrlBinding;
      bc.getEventDispatcher().queueEvent(actionBinding.getEventProducer(), jsonPayLoad);
    } else {
      throw new IllegalArgumentException("Incorrect binding for " + TaskFlowUtils.RAISE_TASK_FLOW_LAUNCH_EVENT);
    }
    bc.getEventDispatcher().processContextualEvents();
    
    logger.exiting(className, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: redirect
  /**
   ** Redirect to a provided url.
   ** @param url URL where we want to be redirected
   */
  public static void redirect(String url) {
    try {
      FacesContext fctx = FacesContext.getCurrentInstance();
      fctx.getExternalContext().redirect(url);
      fctx.responseComplete();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: saveAndContinue
  /**
   ** When a bounded task flow manages a transaction (marked as requires-transaction,.
   ** requires-new-transaction, or requires-existing-transaction), then the
   ** task flow must issue any commits or rollbacks that are needed. This is
   ** essentially to keep the state of the transaction that the task flow understands
   ** in synch with the state of the transaction in the ADFbc layer.
   **
   ** Use this method to issue a commit in the middle of a task flow while staying
   ** in the task flow.
   */
  public static void saveAndContinue() {
    String methodName = "saveAndContinue";
    logger.entering(className, methodName);
    
    Map sessionMap = FacesContext.getCurrentInstance()
                                 .getExternalContext()
                                 .getSessionMap();
    BindingContext context = (BindingContext) sessionMap.get(BindingContext.CONTEXT_ID);
    String currentFrameName = context.getCurrentDataControlFrame();
    DataControlFrame dcFrame = context.findDataControlFrame(currentFrameName);

    dcFrame.commit();
    dcFrame.beginTransaction(null);
    
    logger.exiting(className, methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: evaluateEL
  /**
   ** Programmatic evaluation of EL.
   **
   ** @param el EL to evaluate
   ** @return Result of the evaluation
   */
  public static Object evaluateEL(String el) {
    String methodName = "evaluateEL";
    logger.entering(className, methodName);
    
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ELContext elContext = facesContext.getELContext();
    ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
    ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
    
    logger.exiting(className, methodName);
    return exp.getValue(elContext);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getAttributeBinding
  /**
   ** Programmatic invocation of a method that an EL evaluates to.
   ** The method must not take any parameters.
   **
   ** @param el EL of the method to invoke
   ** @return Object that the method returns
   */
  public static Object invokeEL(String el) {
    return invokeEL(el, new Class[0], new Object[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: invokeEL
  /**
   ** Programmatic invocation of a method that an EL evaluates to.
   **
   ** @param el EL of the method to invoke
   ** @param paramTypes Array of Class defining the types of the parameters
   ** @param params Array of Object defining the values of the parametrs
   ** @return Object that the method returns
   */
  public static Object invokeEL(String el, Class[] paramTypes, Object[] params) {
    String methodName = "invokeEL";
    logger.entering(className, methodName);
    
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ELContext elContext = facesContext.getELContext();
    ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
    MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);
    
    logger.exiting(className, methodName);
    return exp.invoke(elContext, params);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: setEL
  /**
   ** Sets a value into an EL object. Provides similar functionality to
   ** the {@code <af:setActionListener>} tag, except the from is
   ** not an EL. You can get similar behavior by using the following...
   **
   ** setEL(to, evaluateEL(from))
   **
   ** @param el EL object to assign a value
   ** @param val Value to assign
   */
  public static void setEL(String el, Object val) {
    String methodName = "setEL";
    logger.entering(className, methodName);
    
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ELContext elContext = facesContext.getELContext();
    ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
    ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
    exp.setValue(elContext, val);
    
    logger.exiting(className, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getBundleValue
  /**
   ** Retrieve value in ressource bundle
   ** @param key
   ** @return
   */
  public static String getBundleValue(String bundleName, String key) {
    String methodName = "getBundleValue";
    logger.entering(className, methodName);
    FacesContext ctx = FacesContext.getCurrentInstance();
    try {
      ResourceBundle bundle = BundleFactory.getBundle(bundleName, ctx.getViewRoot().getLocale());
      return bundle.getString(key);
    }
    catch (Exception e) {
      return "";
    }
    finally {
      logger.exiting(className, methodName);
    }
  }
}
