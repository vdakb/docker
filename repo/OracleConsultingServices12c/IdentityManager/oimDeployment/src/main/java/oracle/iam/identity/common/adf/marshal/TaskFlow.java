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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   TaskFlow.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskFlow.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.io.File;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.xml.XMLCodecQuote;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

////////////////////////////////////////////////////////////////////////////////
// abstract class TaskFlow
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The marshaller to spool out the task flow descriptor.
 ** <p>
 ** The class is kept abstract due to it expose only static methods.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** The tag names of the created XML file are not declared by constants. This
 ** violations is an exception regarding the coding guidelines but is acceptable
 ** due to those tags are only used inside of this class and occurs mostly only
 ** once.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TaskFlow {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PATH = "oracle/iam/ui/runtime/form/view/tfs";

  private static final String ROOT = "adfc-config";
  private static final String NAMESPACE = "http://xmlns.oracle.com/adf/controller";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static int block = 0;
  static int sequence = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccount
  /**
   ** Marshals the task flow of an account page definition.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalAccount(final Loggable loggable, final File path, final String dataSet)
    throws XMLException {

    reset();

    final File file = new File(path, String.format("%s.xml", dataSet));
    // the default codec will be overriden to prevent escaping of single quotes
    // in the mashalled output.
    // Escaping those XML entities isn't mandatory
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8")).codec(XMLCodecQuote.instance());
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element(ROOT);

    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("id", nextValue());
    XMLOutputNode flow = root.element("task-flow-definition");
    flow.attribute("id", dataSet);
    XMLOutputNode node = flow.element("display-name");
    node.attribute("id", nextValue());
    node.value("Runtime Generated OIM Task Flow");
    XMLOutputNode ref = flow.element("template-reference");
    node = ref.element("document");
    node.attribute("id", nextValue());
    node.value("/WEB-INF/oracle/iam/ui/platform/common/templates/account-form-template.xml");
    node = ref.element("id");
    node.attribute("id", nextValue());
    node.value("account-form-template");
    ref = flow.element("default-activity");
    ref.attribute("id", nextValue());
    ref.value("pgRouter");
    ref = flow.element("data-control-scope");
    ref.attribute("id", nextValue());
    ref.element("shared");

    // the taskflow input parameter
    nextBlock();
    marshalInputParameter(flow, "headerText",         "#{pageFlowScope.headerText}");
    marshalInputParameter(flow, "entityType",         "#{pageFlowScope.entityType}");
    marshalInputParameter(flow, "entitySubType",      "#{pageFlowScope.entitySubType}");
    marshalInputParameter(flow, "entityId",           "#{pageFlowScope.entityId}");
    marshalInputParameter(flow, "requestId",          "#{pageFlowScope.requestId}");
    marshalInputParameter(flow, "operation",          "#{pageFlowScope.operation}");
    marshalInputParameter(flow, "isBulkOperation",    "#{pageFlowScope.isBulkOperation}");
    marshalInputParameter(flow, "requestAction",      "#{pageFlowScope.requestAction}");
    marshalInputParameter(flow, "refresh",            "#{pageFlowScope.refresh}");
    marshalInputParameter(flow, "requestFormContext", "#{pageFlowScope.requestFormContext}", "oracle.iam.ui.platform.view.RequestFormContext");

    // the taskflow managed beans
    nextBlock();
    node = flow.element("managed-bean");
    node.attribute("id", nextValue());
    marshalManagedBean(node, "cartDetailStateBean", "oracle.iam.ui.catalog.view.backing.CartDetailStateBean", "pageFlow");

    // the taskflow router
    nextBlock();
    node = flow.element("router");
    node.attribute("id", "pgRouter");
    marshalRouterCase(node, "BULK", "#{pageFlowScope.isBulkOperation eq 'TRUE'}");
    marshalRouterCase(node, "CREATE", "#{pageFlowScope.operation eq 'CREATE' and pageFlowScope.isBulkOperation ne 'TRUE'}");
    marshalRouterCase(node, "MODIFY", "#{pageFlowScope.operation eq 'MODIFY' and pageFlowScope.isBulkOperation ne 'TRUE'}");
    marshalRouterCase(node, "BULK", "#{pageFlowScope.operation eq 'BULK'}");
    node = node.element("default-outcome");
    node.attribute("id", nextValue());
    node.value("CREATE");

    // the taskflow activities
    nextBlock();
    marshalView(flow, "createView", PageView.pathCreate(dataSet));
    marshalView(flow, "modifyView", PageView.pathModify(dataSet));
    marshalView(flow, "bulkView",   PageView.pathBulk(dataSet));

    // the taskflow control flow
    nextBlock();
    node = flow.element("control-flow-rule");
    node.attribute("id", nextValue());
    XMLOutputNode from = node.element("from-activity-id");
    from.attribute("id", nextValue());
    from.value("pgRouter");
    marshalControlFlow(node, "BULK", "bulkView");
    marshalControlFlow(node, "CREATE", "createView");
    marshalControlFlow(node, "MODIFY", "modifyView");

    node = flow.element("use-page-fragments");

    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlement
  /**
   ** Marshals the task flow of an entitlement page definition.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalEntitlement(final Loggable loggable, final File path, final String dataSet)
    throws XMLException {

    reset();
    final File file = new File(path, String.format("%s.xml", dataSet));

    // the default codec will be overriden to prevent escaping of single quotes
    // in the mashalled output.
    // Escaping those XML entities isn't mandatory
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8")).codec(XMLCodecQuote.instance());
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element(ROOT);

    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("id", nextValue());
    XMLOutputNode flow = root.element("task-flow-definition");
    flow.attribute("id", dataSet);
    XMLOutputNode node = flow.element("display-name");
    node.attribute("id", nextValue());
    node.value("Runtime Generated OIM Task Flow");
    XMLOutputNode ref = flow.element("template-reference");
    node = ref.element("document");
    node.attribute("id", nextValue());
    node.value("/WEB-INF/oracle/iam/ui/platform/common/templates/entitlement-form-template.xml");
    node = ref.element("id");
    node.attribute("id", nextValue());
    node.value("entitlement-form-template");
    ref = flow.element("default-activity");
    ref.attribute("id", nextValue());
    ref.value("pgRouter");
    ref = flow.element("data-control-scope");
    ref.attribute("id", nextValue());
    ref.element("shared");

    // the taskflow input parameter
    nextBlock();
    marshalInputParameter(flow, "headerText",         "#{pageFlowScope.headerText}");
    marshalInputParameter(flow, "entityType",         "#{pageFlowScope.entityType}");
    marshalInputParameter(flow, "entitySubType",      "#{pageFlowScope.entitySubType}");
    marshalInputParameter(flow, "entityId",           "#{pageFlowScope.entityId}");
    marshalInputParameter(flow, "requestId",          "#{pageFlowScope.requestId}");
    marshalInputParameter(flow, "operation",          "#{pageFlowScope.operation}");
    marshalInputParameter(flow, "isBulkOperation",    "#{pageFlowScope.isBulkOperation}");
    marshalInputParameter(flow, "requestAction",      "#{pageFlowScope.requestAction}");
    marshalInputParameter(flow, "refresh",            "#{pageFlowScope.refresh}");
    marshalInputParameter(flow, "requestFormContext", "#{pageFlowScope.requestFormContext}", "oracle.iam.ui.platform.view.RequestFormContext");

    // the taskflow managed beans
    nextBlock();
    node = flow.element("managed-bean");
    node.attribute("id", nextValue());
    marshalManagedBean(node, "cartDetailStateBean", "oracle.iam.ui.catalog.view.backing.CartDetailStateBean", "pageFlow");

    // the taskflow router
    nextBlock();
    node = flow.element("router");
    node.attribute("id", "pgRouter");
    marshalRouterCase(node, "BULK", "#{pageFlowScope.isBulkOperation eq 'TRUE'}");
    marshalRouterCase(node, "CREATE", "#{pageFlowScope.operation eq 'CREATE' and pageFlowScope.isBulkOperation ne 'TRUE'}");
    marshalRouterCase(node, "MODIFY", "#{pageFlowScope.operation eq 'MODIFY' and pageFlowScope.isBulkOperation ne 'TRUE'}");
    marshalRouterCase(node, "BULK", "#{pageFlowScope.operation eq 'BULK'}");
    node = node.element("default-outcome");
    node.attribute("id", nextValue());
    node.value("CREATE");

    // the taskflow activities
    nextBlock();
    marshalView(flow, "createView", PageView.pathCreate(dataSet));
    marshalView(flow, "modifyView", PageView.pathModify(dataSet));
    marshalView(flow, "bulkView",   PageView.pathBulk(dataSet));

    // the taskflow control flow
    nextBlock();
    node = flow.element("control-flow-rule");
    node.attribute("id", nextValue());
    XMLOutputNode from = node.element("from-activity-id");
    from.attribute("id", nextValue());
    from.value("pgRouter");
    marshalControlFlow(node, "BULK", "bulkView");
    marshalControlFlow(node, "CREATE", "createView");
    marshalControlFlow(node, "MODIFY", "modifyView");

    node = flow.element("use-page-fragments");

    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalInputParameter
  /**
   ** Marshals an input parameter of a task flow.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the name of the parameter.
   ** @param  value              the value of the parameter.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalInputParameter(final XMLOutputNode parent, final String name, final String value)
    throws XMLException {

    marshalInputParameter(parent, name, value, "java.lang.String");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalInputParameter
  /**
   ** Marshals an input parameter of a task flow.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the name of the parameter.
   ** @param  value              the value of the parameter.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalInputParameter(final XMLOutputNode parent, final String name, final String value, final String clazz)
    throws XMLException {

    XMLOutputNode param = parent.element("input-parameter-definition");
    param.attribute("id", nextValue());
    XMLOutputNode node = param.element("name");
    node.attribute("id", nextValue());
    node.value(name);
    node = param.element("value");
    node.attribute("id", nextValue());
    node.value(value);
    node = param.element("class");
    node.attribute("id", nextValue());
    node.value(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalManagedBean
  /**
   ** Marshals a managed bean of a task flow.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the name of the managed bean.
   ** @param  clazz              the implementing class of the managed bean.
   ** @param  scope              the scope.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalManagedBean(final XMLOutputNode parent, final String name, final String clazz, final String scope)
    throws XMLException {

    XMLOutputNode node = parent.element("managed-bean-name");
    node.attribute("id", nextValue());
    node.value(name);
    node = parent.element("managed-bean-class");
    node.attribute("id", nextValue());
    node.value(clazz);
    node = parent.element("managed-bean-scope");
    node.attribute("id", nextValue());
    node.value(scope);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalManagedBean
  /**
   ** Marshals a rounting rule of a task flow.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  outcome            the outcome of the rule.
   ** @param  expression         the expression to evaluate.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalRouterCase(final XMLOutputNode parent, final String outcome, final String expression)
    throws XMLException {

    XMLOutputNode route = parent.element("case");
    route.attribute("id", nextValue());
    XMLOutputNode node = route.element("expression");
    node.attribute("id", nextValue());
    node.value(expression);
    node = route.element("outcome");
    node.attribute("id", nextValue());
    node.value(outcome);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalControlFlow
  /**
   ** Marshals a control flow rule of a task flow.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  fromOutcome        the outcome of the rule.
   ** @param  toActivity         the navigation target.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalControlFlow(final XMLOutputNode parent, final String fromOutcome, final String toActivity)
    throws XMLException {

    XMLOutputNode flow = parent.element("control-flow-case");
    flow.attribute("id", nextValue());
    XMLOutputNode node = flow.element("from-outcome");
    node.attribute("id", nextValue());
    node.value(fromOutcome);
    node = flow.element("to-activity-id");
    node.attribute("id", nextValue());
    node.value(toActivity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalView
  /**
   ** Marshals a view activity of a task flow.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  fromOutcome        the identifier of the view.
   ** @param  path               the path to the page view.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalView(final XMLOutputNode parent, final String id, final String path)
    throws XMLException {

    XMLOutputNode node = parent.element("view");
    node.attribute("id", id);
    node = node.element("page");
    node.attribute("id", nextValue());
    node.value(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  private static void reset() {
    block = 0;
    sequence = 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextValue
  private static String nextValue() {
    return String.format("__%d", ++sequence + block);
  }

  private static void nextBlock() {
    block += ((sequence / 10) + (sequence % 10 > 0 ? 1 : 0)) * 10;
    sequence = 0;
  }
}