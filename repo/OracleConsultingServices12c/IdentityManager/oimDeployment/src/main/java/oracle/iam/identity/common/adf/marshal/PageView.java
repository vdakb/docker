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

    File        :   PageView.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PageView.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.util.Map;
import java.util.Collection;

import java.io.File;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.provisioning.vo.FormField;

import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLCodecQuote;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.common.spi.FormInstance;
import oracle.iam.identity.common.spi.SandboxInstance;

import org.apache.tools.ant.BuildException;

////////////////////////////////////////////////////////////////////////////////
// abstract class PageView
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The marshaller to spool out the page view descriptors.
 ** <br>
 ** Those descriptors includes the JSP page definition for several operations
 ** like create, modify or modify in bulk and the customization of those page
 ** definitions uploaded to MDS later on by publishing the <code>Sandbox</code>.
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
public abstract class PageView {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PATH   = "oracle/iam/ui/runtime/form/view/pages";

  public static final String BULK   = "%sBulkForm.jsff";
  public static final String CREATE = "%sCreateForm.jsff";
  public static final String MODIFY = "%sModifyForm.jsff";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static int sequence_xg = 0;
  private static int sequence_xc = 0;
  private static int sequence_tg = 0;
  private static int sequence_tc = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   customizePage
  /**
   ** Marshals the page view descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given <code>dataSet</code>.
   ** <p>
   ** The generated page view customization belongs to modify operation
   ** specified by <code>operation</code>.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the abstract pathname to marshal the artifacts
   **                            to.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @return                    the {@link XMLOutputNode} containing the XML
   **                            fragments for MDS customizations.
   **
   ** @throws  XMLException      if the XML operations failed.
   */
  public static XMLOutputNode customizePage(final Loggable loggable, final File path, final String version, final String dataSet)
    throws XMLException {

    final File file = new File(path, String.format("%s.xml", dataSet));
    return Metadata.marshalCustomization(loggable, file, version, "root", "http://java.sun.com/JSP/Page");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalDefinition
  /**
   ** Marshals the page view definition.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  bundle             the collection of resource bundles to bind
   **                            within the page view definition,
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalDefinition(final Loggable loggable, final File path, final String dataSet, final Collection<SandboxInstance.Bundle> bundle)
    throws XMLException {

    final File file = new File(path, dataSet);
    // the default codec will be overriden to prevent escaping of single quotes
    // in the mashalled output.
    // Escaping those XML entities isn't mandatory
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8")).codec(XMLCodecQuote.instance());
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element("jsp:root");
    root.attribute("xmlns:jsp", "http://java.sun.com/JSP/Page");
    root.attribute("version",   "2.1");
    root.attribute("xmlns:f",   "http://java.sun.com/jsf/core");
    root.attribute("xmlns:af",  "http://xmlns.oracle.com/adf/faces/rich");
    if (!CollectionUtility.empty(bundle)) {
      root.attribute("xmlns:c", "http://java.sun.com/jsp/jstl/core");
      for (SandboxInstance.Bundle cursor : bundle) {
        final XMLOutputNode node = root.element("c:set");
        node.attribute("var",   cursor.scope());
        node.attribute("value", String.format("#{adfBundle['%s']}", cursor.clazz()));
      }
    }
    XMLOutputNode node = root.element("af:panelGroupLayout");
    node.attribute("id",            "pgl0");
    node.attribute("layout",        "vertical");
    node.attribute("styleClass",    "AFStretchWidth");
    XMLOutputNode hdr = node.element("af:showDetailHeader");
    hdr.attribute("text",           "#{pageFlowScope.headerText eq null ? 'Details' : pageFlowScope.headerText}");
    hdr.attribute("disclosed",      "true");
    hdr.attribute("id",             "sdh4");
    hdr.attribute("size",           "2");
    XMLOutputNode pop = hdr.element("af:popup");
    pop.attribute("id",              "p1");
    pop.attribute("launcherVar",     "source");
    pop.attribute("contentDelivery", "lazyUncached");
    pop.attribute("eventContext",    "launcher");
    XMLOutputNode win = pop.element("af:noteWindow");
    win.attribute("id",              "nw1");
    XMLOutputNode frm = win.element("af:panelFormLayout");
    frm.attribute("id",              "pfl1");

    XMLOutputNode plm = frm.element("af:panelLabelAndMessage");
    plm.attribute("label",           "Old value");
    plm.attribute("id",              "plam1");
    plm = plm.element("af:outputText");
    plm.attribute("value",           "#{viewScope.value}");
    plm.attribute("id",              "ot5");

    win = pop.element("af:setPropertyListener");
    win.attribute("from",            "#{source.attributes.label}");
    win.attribute("to",              "#{viewScope.label}");
    win.attribute("type",            "popupFetch");

    win = pop.element("af:setPropertyListener");
    win.attribute("from",            "#{source.attributes.value}");
    win.attribute("to",              "#{viewScope.value}");
    win.attribute("type",            "popupFetch");

    pop = hdr.element("af:popup");
    pop.attribute("id",              "pwdpolicyInfo");
    pop.attribute("contentDelivery", "lazyUncached");

    win = pop.element("af:noteWindow");
    win.attribute("id",              "nw2");
    // extend height to cover complex policies
    win.attribute("inlineStyle",     "width:320px;height:320px");

    XMLOutputNode rgn = win.element("af:region");
    rgn.attribute("value",           "#{bindings.pwdpolicyinfo.regionModel}");
    rgn.attribute("id",              "r4");

    pop = hdr.element("af:panelGroupLayout");
    pop.attribute("id",              "sdh1");
    pop.attribute("layout",          "vertical");
    hdr = node.element("af:spacer");
    hdr.attribute("id",              "sp1");
    hdr.attribute("height",          "10");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalCustomization
  /**
   ** Marshals the account form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  row                the number of rows after which
   **                            to start a new column.
   **                            <br>
   **                            The number of rows actually rendered depends
   **                            also on the <code>col</code> attribute. When
   **                            the number of children rendered equals the rows
   **                            value, the next child is rendered in the next
   **                            column. If the children will not fit in the
   **                            given number of rows and columns, the number of
   **                            rows will increase to accommodate the children.
   **                            <br>
   **                            When left blank, rows defaults to the maximum
   **                            integer value.
   ** @param  col                the maximum number of columns to show.
   **                            <br>
   **                            This attribute defaults to 2 and 1 on PDAs. If
   **                            this panelFormLayout is inside of another
   **                            panelFormLayout, this will always be 1.
   **
   ** @return                    the {@link XMLOutputNode} containing the XML
   **                            fragments for MDS customizations.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static XMLOutputNode marshalCustomization(final XMLOutputNode parent, final String row, final String col)
    throws XMLException {

    XMLOutputNode node = Metadata.marshalInsert(marshalCustomization(parent), "sdh1");
    node = node.element("panelFormLayout");
    node.attribute("id",         nextXG());
    node.attribute("rows",       row);
    node.attribute("maxColumns", col);
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/faces/rich");
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalCustomization
  /**
   ** Marshals the account form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   **
   ** @return                    the {@link XMLOutputNode} containing the XML
   **                            fragments for MDS customizations.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static XMLOutputNode marshalCustomization(final XMLOutputNode parent)
    throws XMLException {

    sequence_xg = 0;
    sequence_xc = 0;
    sequence_tg = 0;
    sequence_tc = 0;
    return parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalPanel
  /**
   ** Marshals the account panel of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  panel              the {@link FormInstance.Panel} to render.
   **
   ** @return                    the {@link XMLOutputNode} containing the XML
   **                            fragments for MDS customizations.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static XMLOutputNode marshalPanel(final XMLOutputNode parent, final FormInstance.Panel panel)
    throws XMLException {

    XMLOutputNode node = Metadata.marshalInsert(parent, "sdh1");
    node = node.element("af:showDetailHeader");
    node.attribute("id",                                   nextXG());
    node.attribute(FormInstance.Panel.Hint.HEADER.id(),    panel.stringParameter(FormInstance.Panel.Hint.HEADER.id(),     panel.name()));
    node.attribute(FormInstance.Panel.Hint.DISCLOSED.id(), panel.stringParameter(FormInstance.Panel.Hint.DISCLOSED.id(), "true"));
    node.attribute(FormInstance.Panel.Hint.SIZE.id(),      panel.stringParameter(FormInstance.Panel.Hint.SIZE.id(),      "3"));
    node.attribute("xmlns:af",                             "http://xmlns.oracle.com/adf/faces/rich");

    node = node.element("af:panelFormLayout");
    node.attribute("id",                                   nextXG());
    
    node.attribute(FormInstance.Panel.Hint.ROW.id(),       panel.stringParameter(FormInstance.Panel.Hint.ROW.id(),       "5"));
    node.attribute(FormInstance.Panel.Hint.COLUMN.id(),    panel.stringParameter(FormInstance.Panel.Hint.COLUMN.id(),    "2"));
    if (panel.parameter().containsKey(FormInstance.Panel.Hint.LABEL_WIDTH.id()))
      node.attribute(FormInstance.Panel.Hint.LABEL_WIDTH.id(), panel.stringParameter(FormInstance.Panel.Hint.LABEL_WIDTH.id()));
    if (panel.parameter().containsKey(FormInstance.Panel.Hint.FIELD_WIDTH.id()))
      node.attribute(FormInstance.Panel.Hint.FIELD_WIDTH.id(), panel.stringParameter(FormInstance.Panel.Hint.FIELD_WIDTH.id()));
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/faces/rich");
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountBulk
  /**
   ** Marshals the account and entitlement forms of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalAccountBulk(final XMLOutputNode parent, final SandboxInstance sandbox)
    throws XMLException {

    marshalAccount(parent, sandbox.account(), sandbox.metadata().account(), false);
    marshalOther(parent, sandbox.version(), sandbox.dataSet(), sandbox.account().other(), sandbox.metadata().actions(), sandbox.metadata().others(), false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountCreate
  /**
   ** Marshals the account and entitlement forms of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalAccountCreate(final XMLOutputNode parent, final SandboxInstance sandbox)
    throws XMLException {

    marshalAccount(parent, sandbox.account(), sandbox.metadata().account(), false);
    marshalOther(parent, sandbox.version(), sandbox.dataSet(), sandbox.account().other(), sandbox.metadata().actions(), sandbox.metadata().others(), false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountModify
  /**
   ** Marshals the account form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalAccountModify(final XMLOutputNode parent, final SandboxInstance sandbox)
    throws XMLException {

    marshalAccount(parent, sandbox.account(), sandbox.metadata().account(), true);
    marshalOther(parent, sandbox.version(), sandbox.dataSet(), sandbox.account().other(), sandbox.metadata().actions(), sandbox.metadata().others(), true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementBulk
  /**
   ** Marshals the entitlement bulk request forms of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  entitlement        the entitlement form associated with the page
   **                            view.
   ** @param  origin             the process form belonging to the account
   **                            discovered from the model definition of the
   **                            provisioning process.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>dataSet</code> of a process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  // FIXME: Post-Poned until we know what it means
  @SuppressWarnings("unused")
  public static void marshalEntitlementBulk( final XMLOutputNode parent, final FormInstance entitlement, final FormInfo origin, final Map<String, FormInfo> other)
    throws XMLException {

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementCreate
  /**
   ** Marshals the entitlement request forms of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  entitlement        the entitlement form associated with the page
   **                            view.
   ** @param  origin             the process form belonging to the account
   **                            discovered from the model definition of the
   **                            provisioning process.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalEntitlementCreate(final XMLOutputNode parent, final FormInstance entitlement, final FormInfo origin)
    throws XMLException {

    marshalEntitlement(parent, entitlement, origin, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementModify
  /**
   ** Marshals the account form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  entitlement        the entitlement form associated with the page
   **                            view.
   ** @param  origin             the process form belonging to the account
   **                            discovered from the model definition of the
   **                            provisioning process.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalEntitlementModify(final XMLOutputNode parent, final FormInstance entitlement, final FormInfo origin)
    throws XMLException {

    marshalEntitlement(parent, entitlement, origin, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pathBulk
  /**
   ** Return the path to the page that belongs to bulk operations.
   **
   ** @param  dataSet            the name of the data set the page belongs to.
   */
  static String pathBulk(final String dataSet) {
    return String.format("/%s/%s", PATH, pageBulk(dataSet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageBulk
  /**
   ** Return the file name to the page that belongs to bulk operations.
   **
   ** @param  dataSet            the name of the data set the page belongs to.
   */
  static String pageBulk(final String dataSet) {
    return String.format(BULK, dataSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pathCreate
  /**
   ** Return the path to the page that belongs to create operations.
   **
   ** @param  dataSet            the name of the data set the page belongs to.
   */
  static String pathCreate(final String dataSet) {
    return String.format("/%s/%s", PATH, pageCreate(dataSet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageCreate
  /**
   ** Return the file name to the page that belongs to create operations.
   **
   ** @param  dataSet            the name of the data set the page belongs to.
   */
  static String pageCreate(final String dataSet) {
    return String.format(CREATE, dataSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pathModify
  /**
   ** Return the path to the page that belongs to modify operations.
   **
   ** @param  dataSet            the name of the data set the page belongs to.
   */
  static String pathModify(final String dataSet) {
    return String.format("/%s/%s", PATH, pageModify(dataSet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pageModify
  /**
   ** Return the file name to the page that belongs to modify operations.
   **
   ** @param  dataSet            the name of the data set the page belongs to.
   */
  static String pageModify(final String dataSet) {
    return String.format(MODIFY, dataSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccount
  /**
   ** Marshals the account form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  account            the account form associated with the page view.
   ** @param  origin             the process form belonging to the account
   **                            discovered from the model definition of the
   **                            provisioning process.
   ** @param  modify             whether the marshalling is applicable for
   **                            account modify operation.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalAccount(final XMLOutputNode parent, final FormInstance.Account account, final FormInfo origin, final boolean modify)
    throws XMLException {

    final boolean plain  = account.panel().isEmpty();
    final String  row    = account.stringParameter(FormInstance.Panel.Hint.ROW.id(),    "5");
    final String  col    = account.stringParameter(FormInstance.Panel.Hint.COLUMN.id(), "2");
    XMLOutputNode layout = plain ? marshalCustomization(parent, row, col) : marshalCustomization(parent);
    // marshall all attributes that belongs to the root panel of the account
    // form
    if (account.root().attribute().size() > 0) {
      for (String name : account.root().attribute()) {
        final FormField cursor = origin.getFormField(name);
        if (cursor != null) {
          // skip modifications of the service account flag
          if (modify && "serviceaccount".equals(cursor.getName()))
            continue;

          final Metadata.Attribute type = Metadata.Attribute.from(cursor.getType());
          switch (type) {
            case TEXT     :
            case NUMBER   :
            case TEXTAREA :
            case READONLY : marshalInputText(layout, account.attribute(name), modify, false);
                            break;
            case LOOKUP   : marshalListOfValue(layout, account.attribute(name), false);
                            break;
            case PASSWORD : marshalPassword(layout, account.attribute(name), false);
                            break;
            case CHECKBOX : marshalCheckBox(layout, account.attribute(name), false);
                            break;
            case DATE     : marshalInputDate(layout, account.attribute(name), false);
                            break;
            default       : throw new IllegalArgumentException("type");
          }
        }
      }
    }
    // marshall all attributes that belongs to the additional panels of the
    // account form if there are any
    for (FormInstance.Panel panel : account.panel()) {
      layout = marshalPanel(parent, panel);
      for (String name : panel.attribute()) {
        final FormField cursor = origin.getFormField(name);
        if (cursor != null) {
          // skip modifications of the service account flag
          if (modify && "serviceaccount".equals(cursor.getName()))
            continue;

          final Metadata.Attribute type = Metadata.Attribute.from(cursor.getType());
          switch (type) {
            case TEXT     :
            case NUMBER   :
            case TEXTAREA :
            case READONLY : marshalInputText(layout, account.attribute(name), modify, false);
                            break;
            case LOOKUP   : marshalListOfValue(layout, account.attribute(name), false);
                            break;
            case PASSWORD : marshalPassword(layout, account.attribute(name), false);
                            break;
            case CHECKBOX : marshalCheckBox(layout, account.attribute(name), false);
                            break;
            case DATE     : marshalInputDate(layout, account.attribute(name), false);
                            break;
            default       : throw new IllegalArgumentException("type");
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlement
  /**
   ** Marshals the entitlement form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  form               the entitlement form associated with the page
   **                             view.
   ** @param  origin             the process form belonging to the entitlement
   **                            discovered from the model definition of the
   **                            provisioning process.
   ** @param  modify             whether the marshalling is applicable for
   **                            account modify operation.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalEntitlement(final XMLOutputNode parent, final FormInstance form, final FormInfo origin, final boolean modify)
    throws XMLException {

    final String  row   = form.stringParameter(FormInstance.Panel.Hint.ROW.id(),    "1");
    final String  col   = form.stringParameter(FormInstance.Panel.Hint.COLUMN.id(), "2");
    XMLOutputNode pfl = marshalCustomization(parent, row, col);
    // marshall all attributes that belongs to the root panel of the account
    // form
    for (String name : form.attribute()) {
      final FormField cursor = origin.getFormField(name);
      if (cursor != null) {
        // avoid marshallling of the entitlement it self
        final Object entitlement = cursor.getProperty("Entitlement");
        if (entitlement != null && "true".equalsIgnoreCase(entitlement.toString()))
          continue;

        // render the remaining attributes
        final Metadata.Attribute type = Metadata.Attribute.from(cursor.getType());
        switch (type) {
          case TEXT     :
          case NUMBER   :
          case TEXTAREA :
          case READONLY : marshalInputText(pfl, form.attribute(name), modify, false);
                          break;
          case LOOKUP   : marshalListOfValue(pfl, form.attribute(name), false);
                          break;
          case PASSWORD : marshalPassword(pfl, form.attribute(name), false);
                          break;
          case CHECKBOX : marshalCheckBox(pfl, form.attribute(name), false);
                          break;
          case DATE     : marshalInputDate(pfl, form.attribute(name), false);
                          break;
          default       : throw new IllegalArgumentException("type");
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalOther
  /**
   ** Marshals the entitement forms of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>dataSet</code> of a process form.
   ** @param  modify             whether the marshalling is applicable for
   **                            account modify operation.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalOther(final XMLOutputNode parent, final SandboxInstance.Version version, final String dataSet, final Collection<FormInstance> other, final Map<String, Integer> action, final Map<String, FormInfo> origin, final boolean modify)
    throws XMLException {

    XMLOutputNode pg = Metadata.marshalInsert(parent, "pgl0");
    XMLOutputNode pt = pg.element("panelTabbed");
    pt.attribute("styleClass",                 "AFStretchWidth");
    pt.attribute("id",                         nextXGC());
    pt.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/faces/rich");

    for (FormInstance cursor : other) {
      if (cursor.parameter().containsKey(FormInstance.Hint.RENDERED.id())) {
        pt.attribute("rendered", cursor.stringParameter(FormInstance.Hint.RENDERED.id()));
      }
      marshalOther(pt, version, dataSet, cursor, action.get(cursor.name()), origin.get(cursor.name()), modify);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalOther
  /**
   ** Marshals the account form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  other              the subordinated process form associated with
   **                            the <code>dataSet</code> of a process form.
   ** @param  origin             the subordinated process form belonging to the
   **                            other discovered from the model definition of
   **                            the provisioning process.
   ** @param  modify             whether the marshalling is applicable for
   **                            account modify operation.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalOther(final XMLOutputNode parent, final SandboxInstance.Version version, final String dataSet, final FormInstance other, final int action, final FormInfo origin, final boolean modify)
    throws XMLException {

    XMLOutputNode sd = parent.element("showDetailItem");
    sd.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/faces/rich");
    sd.attribute("text",                       other.stringParameter(FormInstance.Hint.HEADER.id(), String.format("%s%s", dataSet, other.name())));
    sd.attribute("styleClass",                 "AFStretchWidth");
    sd.attribute("id",                         nextXGC());

    XMLOutputNode pc = sd.element("panelCollection");
    pc.attribute("featuresOff",                "viewMenu statusBar detach");
    pc.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/faces/rich");
    pc.attribute("styleClass",                 "AFStretchWidth");
    pc.attribute("id",                         nextXGC());

    final String tableID  = nextXGTB();
    final String createID = nextXCTB();
    final String deleteID = nextXCTB();

    XMLOutputNode f = pc.element("facet");
    f.attribute("name", "toolbar");
    f.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://java.sun.com/jsf/core");

    f = f.element("toolbar");
    f.attribute("partialTriggers",            String.format("%s %s", createID, deleteID));
    f.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/faces/rich");
    f.attribute("id",                         nextXGC());

    XMLOutputNode b = f.element("commandToolbarButton");
    b.attribute("id",                         createID);
    b.attribute("icon",                       "/images/func_create_16_ena.png");
    b.attribute("actionListener",             String.format("#{bindings.CreateInsert%d.execute}", action));
    b.attribute("inlineStyle",                "min-width: 22px;");
    b.attribute("partialSubmit",              "true");
    b.attribute("disabledIcon",               "/images/func_create_16_dis.png");
    b.attribute("partialTriggers",            tableID);
    b.attribute("disabled",                   "#{pageFlowScope.cartDetailStateBean.buttonEnabled}");
    b.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/faces/rich");

    b = f.element("commandToolbarButton");
    b.attribute("id",                         deleteID);
    b.attribute("actionListener",             String.format("#{bindings.Delete%d.execute}", action));
    b.attribute("icon",                       "/images/func_delete_16_ena.png");
    b.attribute("partialSubmit",              "true");
    b.attribute("inlineStyle",                "min-width: 22px;");
    b.attribute("partialTriggers",            tableID);
    b.attribute("disabledIcon",               "/images/func_delete_16_dis.png");
    b.attribute("immediate",                  "true");
    b.attribute("disabled",                   version == SandboxInstance.Version.PS4 ? String.format("#{bindings.%s%sVOIterator.currentRow == null}", dataSet, other.name()) : "#{pageFlowScope.cartDetailStateBean.buttonEnabled}");
    b.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/faces/rich");

    final String viewObject = String.format("bindings.%s%sVO", dataSet, other.name());
    f = pc.element("table");
    f.attribute("value",                      String.format("#{%s.collectionModel}", viewObject));
    f.attribute("var",                        "row");
    f.attribute("rows",                       String.format("#{%s.rangeSize}", viewObject));
    f.attribute("emptyText",                  String.format("#{%s.viewable ? 'No data to display.' : 'Access Denied.'}", viewObject));
    f.attribute("rowBandingInterval",         other.rowBanding() ? "1" : "0");
    f.attribute("selectedRowKeys",            String.format("#{%s.collectionModel.selectedRow}", viewObject));
    f.attribute("selectionListener",          String.format("#{%s.collectionModel.makeCurrent}", viewObject));
    f.attribute("rowSelection",               "single");
    f.attribute("fetchSize",                  String.format("#{%s.rangeSize}", viewObject));
    f.attribute("filterModel",                String.format("#{%sQuery.queryDescriptor}", viewObject));
    f.attribute("filterVisible",              Boolean.toString(other.filterVisible()));
    f.attribute("queryListener",              String.format("#{%sQuery.processQuery}",   viewObject));
    f.attribute("varStatus",                  "vs");
    f.attribute("id",                         tableID);
    f.attribute("contentDelivery",            "immediate");
    f.attribute("partialTriggers",            String.format("::%s ::%s", createID, deleteID));
    f.attribute("columnStretching",           other.columnStretching());

    if (modify)
      marshalTableAction(f, viewObject);

    for (String cursor : other.attribute())
      marshalTableAttribute(f, viewObject, other.attribute(cursor), origin.getFormField(cursor));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalTableAttribute
  /**
   ** Marshals the an attribute detail form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  attribute          the attribute metadata.
   ** @param  origin             the <code>FormField</code> providing the
   **                            properties of the attribute to marshal.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalTableAttribute(final XMLOutputNode parent, final String dataSet, final FormInstance.Attribute attribute, final FormField origin)
    throws XMLException {

    // FIXME: use resource bundle for exception message
    if (origin == null)
      throw new BuildException(String.format("Attribute %s is not mapped in %s", attribute.name(), dataSet));

    final String             binding = String.format("%s.hints.%s__c", dataSet, attribute.name());
    final XMLOutputNode      column  = marshalTableColumn(parent, binding, attribute);
    final Metadata.Attribute type    = Metadata.Attribute.from(origin.getType());
    switch (type) {
      case TEXT     :
      case NUMBER   :
      case TEXTAREA :
      case READONLY : marshalInputText(column, attribute, true, true);
                      break;
      case LOOKUP   : marshalListOfValue(column, attribute, true);
                      break;
      case PASSWORD : marshalPassword(column, attribute, true);
                      break;
      case CHECKBOX : marshalCheckBox(column,attribute, true);
                      break;
      case DATE     : marshalInputDate(column, attribute, true);
                      break;
      default       : throw new IllegalArgumentException("type");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalTableColumn
  /**
   ** Marshals the account form of a page view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  binding            the model binding of the table column to
   **                            marshal.
   ** @param  attribute          the {@link FormInstance.Attribute} providing
   **                            the metadata of the table column to marshal.
   **
   ** @return                    the {@link XMLOutputNode} containing the XML
   **                            fragments for an ADF table column.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static XMLOutputNode marshalTableColumn(final XMLOutputNode parent, final String binding, final FormInstance.Attribute attribute)
    throws XMLException {

    XMLOutputNode node = parent.element("column");
    node.attribute("sortProperty", String.format("#{%s.name}", binding));
    node.attribute("filterable",   attribute.stringParameter(FormInstance.Attribute.Hint.FILTERABLE.id(), "true"));
    node.attribute("sortable",     attribute.stringParameter(FormInstance.Attribute.Hint.SORTABLE.id(),   "true"));
    node.attribute("headerText",   attribute.stringParameter(FormInstance.Attribute.Hint.LABEL.id(),      String.format("#{%s.label}", binding)));
    node.attribute("id",           nextXGC());
    if (attribute.parameter().containsKey(FormInstance.Attribute.Hint.DISPLAY_WIDTH.id()))
      node.attribute("width",      (String)attribute.parameter().get(FormInstance.Attribute.Hint.DISPLAY_WIDTH.id()));
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalTableAction
  private static void marshalTableAction(final XMLOutputNode parent, final String viewObject)
    throws XMLException {

    String        binding = String.format("%s.hints.%s", viewObject, "action");
    XMLOutputNode c       = parent.element("column");
    c.attribute("sortProperty",  String.format("#{%s.name}",  binding));
    c.attribute("filterable",    "true");
    c.attribute("sortable",      "true");
    c.attribute("headerText",    String.format("#{%s.label}", binding));
    c.attribute("visible",       "#{!pageFlowScope.cartDetailStateBean.readyToSubmitButtonEnabled}");
    c.attribute("id",            nextXGC());

    c = adfElement(c, "af:inputText");
    c.attribute("value",         "#{row.bindings.action.inputValue}");
    c.attribute("label",         String.format("#{%s.label}",        binding));
    c.attribute("required",      String.format("#{%s.mandatory}",    binding));
    c.attribute("columns",       String.format("#{%s.displayWidth}", binding));
    c.attribute("maximumLength", String.format("#{%s.precision}",    binding));
    c.attribute("shortDesc",     String.format("#{%s.tooltip}",      binding));
    c.attribute("changed",       "#{not empty bindings.action.hints.tooltip}");
    c.attribute("changedDesc",   "#{bindings.action.hints.tooltip}");
    c.attribute("disabled",      "true");
    c.attribute("autoSubmit",    "true");
    c.attribute("autoComplete",  "off");
    c.attribute("id",            nextXGC());
    marshalValidatorText(c, "row.bindings.action");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalPanelPassword
  /**
   ** Marshals the element for password in a form panel.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the {@link FormInstance.Attribute} containing
   **                            the properties.
   ** @param  iterator           <code>true</code> in the binding of the element
   **                            is based on an iterator. 
  **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalPassword(final XMLOutputNode parent, final FormInstance.Attribute attribute, final boolean iterator)
    throws XMLException {

    final String  binding = valueBinding(attribute.name(),iterator);
    XMLOutputNode it      = adfElement(parent, "af:panelLabelAndMessage");
    it.attribute("rendered",    "#{pageFlowScope.requestFormContext.actionType != 'SUMMARY' and pageFlowScope.requestFormContext.actionType != 'APPROVAL'}");
    it.attribute("label",       attribute.stringParameter(FormInstance.Attribute.Hint.LABEL.id(), String.format("#{%s.hints.label}", binding)));
    it.attribute("id",          nextXGC());
    marshalPasswordText(it,   attribute, iterator);
    it = it.element("f:facet");
    it.attribute("xmlns:f",     "http://java.sun.com/jsf/core");
    it.attribute("name",        "end");

    it = it.element("af:commandImageLink");
    it.attribute("xmlns:af",    "http://xmlns.oracle.com/adf/faces/rich");
    it.attribute("icon",        "/images/func_info_16_ena.png");
    it.attribute("immediate",   "true");
    it.attribute("id",          iterator ? nextXGC() :nextXG());

    it = it.element("af:showPopupBehavior");
    it.attribute("xmlns:af",    "http://xmlns.oracle.com/adf/faces/rich");
    it.attribute("popupId",     "pwdpolicyInfo");
    it.attribute("triggerType", "action");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalInputText
  /**
   ** Marshals the element for input text panel form.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the {@link FormInstance.Attribute} containing
   **                            the properties.
   ** @param  modify             <code>true</code> in the binding of the element
   **                            is based on a modify action. 
   ** @param  iterator           <code>true</code> in the binding of the element
   **                            is based on an iterator. 
   ** @param  panel              <code>true</code> if the converter is
   **                            marshalled for a component thats placed inside
   **                            a form panel.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalInputText(final XMLOutputNode parent, final FormInstance.Attribute attribute, final boolean modify, final boolean iterator)
    throws XMLException {

    final String        binding = valueBinding(attribute.name(), iterator);
    final XMLOutputNode it      = adfElement(parent, "af:inputText");
    it.attribute("value",         String.format("#{%s.inputValue}",        binding));
    it.attribute("label",         attribute.stringParameter(FormInstance.Attribute.Hint.LABEL.id(), String.format("#{%s.hints.label}", binding)));
    it.attribute("required",      String.format("#{%s.hints.mandatory}",    binding));
    it.attribute("columns",       String.format("#{%s.hints.displayWidth}", binding));
    it.attribute("maximumLength", String.format("#{%s.hints.precision}",    binding));
    it.attribute("shortDesc",     attribute.stringParameter(FormInstance.Attribute.Hint.TOOLTIP.id(), String.format("#{%s.hints.tooltip}", binding)));
    if (attribute.parameter().containsKey(FormInstance.Attribute.Hint.READONLY.id()))
      it.attribute("readOnly",    attribute.stringParameter(FormInstance.Attribute.Hint.READONLY.id(), String.format("#{%s.hints.tooltip}", binding)));
    if (modify) {
      it.attribute("changed",       String.format("#{not empty %s.hints.tooltip}", binding));
      it.attribute("changedDesc",   String.format("#{%s.hints.tooltip}", binding));
    }
    if (attribute.parameter().containsKey(FormInstance.Attribute.Hint.RENDERED.id()))
      it.attribute("rendered",    attribute.stringParameter(FormInstance.Attribute.Hint.RENDERED.id(), "true"));
    it.attribute("id",            iterator ? nextXGC() :nextXG());
    it.attribute("autoSubmit",    "true");
    it.attribute("autoComplete",  "off");
    marshalValidatorText(it, binding);
    if (attribute.parameter().containsKey(FormInstance.Attribute.Hint.NUMBER.id()))
      marshalConvertNumber(it, binding, iterator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalPasswordText
  /**
   ** Marshals the element for password text in a form panel.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the {@link FormInstance.Attribute} containing
   **                            the properties.
   ** @param  iterator           <code>true</code> in the binding of the element
   **                            is based on an iterator. 
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalPasswordText(final XMLOutputNode parent, final FormInstance.Attribute attribute, final boolean iterator)
    throws XMLException {

    final String        binding = valueBinding(attribute.name(), iterator);
    final XMLOutputNode it      = adfElement(parent, "af:inputText");
    it.attribute("value",         String.format("#{%s.inputValue}",        binding));
    it.attribute("required",      String.format("#{%s.hints.mandatory}",    binding));
    it.attribute("columns",       String.format("#{%s.hints.displayWidth}", binding));
    it.attribute("maximumLength", String.format("#{%s.hints.precision}",    binding));
    it.attribute("shortDesc",     attribute.stringParameter(FormInstance.Attribute.Hint.TOOLTIP.id(), String.format("#{%s.hints.tooltip}", binding)));
    if (attribute.parameter().containsKey(FormInstance.Attribute.Hint.READONLY.id()))
      it.attribute("readOnly",    attribute.stringParameter(FormInstance.Attribute.Hint.READONLY.id(), String.format("#{%s.hints.tooltip}", binding)));
    if (attribute.parameter().containsKey(FormInstance.Attribute.Hint.RENDERED.id()))
      it.attribute("rendered",    attribute.stringParameter(FormInstance.Attribute.Hint.RENDERED.id(), "true"));
    it.attribute("autoSubmit",    "true");
    it.attribute("simple",        "true");
    it.attribute("secret",        "true");
    it.attribute("id",            iterator ? nextXGC() :nextXG());
    marshalValidatorText(it, binding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalCheckBox
  /**
   ** Marshals the element for checkbox in a table view.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the {@link FormInstance.Attribute} containing
   **                            the properties.
   ** @param  iterator           <code>true</code> in the binding of the element
   **                            is based on an iterator. 
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalCheckBox(final XMLOutputNode parent, final FormInstance.Attribute attribute, final boolean iterator)
    throws XMLException {

    final String        binding = valueBinding(attribute.name(),iterator);
    final XMLOutputNode cb      = adfElement(parent, "af:selectBooleanCheckbox");
    cb.attribute("value",       String.format("#{%s.inputValue}", binding));
    cb.attribute("label",       attribute.stringParameter(FormInstance.Attribute.Hint.LABEL.id(), String.format("#{%s.hints.label}", binding)));
    cb.attribute("shortDesc",   attribute.stringParameter(FormInstance.Attribute.Hint.TOOLTIP.id(), String.format("#{%s.hints.tooltip}", binding)));
    cb.attribute("changed",     String.format("#{not empty %s.hints.tooltip}", String.format(iterator ? "bindings.%s__c" : "bindings.%s__c", attribute.name())));
    cb.attribute("changedDesc", String.format("#{%s.hints.tooltip}", String.format(iterator ? "bindings.%s__c" : "bindings.%s__c", attribute.name())));
    cb.attribute("id",          iterator ? nextXGC() :nextXG());
    cb.attribute("autoSubmit",  "true");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalInputDate
  /**
   ** Marshals the element for input date.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the {@link FormInstance.Attribute} containing
   **                            the properties.
   ** @param  iterator           <code>true</code> in the binding of the element
   **                            is based on an iterator. 
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalInputDate(final XMLOutputNode parent, final FormInstance.Attribute attribute, final boolean iterator)
    throws XMLException {

    final String        binding = valueBinding(attribute.name(),iterator);
    final XMLOutputNode it      = adfElement(parent, "af:inputDate");
    it.attribute("value",      String.format("#{%s.inputValue}",        binding));
    it.attribute("label",      attribute.stringParameter(FormInstance.Attribute.Hint.LABEL.id(), String.format("#{%s.hints.label}", binding)));
    it.attribute("required",   String.format("#{%s.hints.mandatory}",    binding));
    it.attribute("columns",    String.format("#{%s.hints.displayWidth}", binding));
    it.attribute("shortDesc",  attribute.stringParameter(FormInstance.Attribute.Hint.TOOLTIP.id(), String.format("#{%s.hints.tooltip}", binding)));
    it.attribute("id",         iterator ? nextXGC() :nextXG());
    it.attribute("autoSubmit", "true");
    marshalConvertDateTime(it, binding, iterator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalListOfValue
  /**
   ** Marshals the element for list of value.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the {@link FormInstance.Attribute} containing
   **                            the properties.
   ** @param  iterator           <code>true</code> in the binding of the element
   **                            is based on an iterator. 
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalListOfValue(final XMLOutputNode parent, final FormInstance.Attribute attribute, final boolean iterator)
    throws XMLException {

    final String        binding = valueBinding(attribute.name(), iterator);
    final XMLOutputNode lov     = adfElement(parent, "af:inputListOfValues");
    lov.attribute("popupTitle",    String.format("#{resBundle.SEARCH_AND_SELECT}: %s", attribute.stringParameter(FormInstance.Attribute.Hint.TITLE_LOV.id(), String.format("#{%s.hints.label}", binding))));
    lov.attribute("model",         String.format("#{%s.listOfValuesModel}",  binding));
    lov.attribute("value",         String.format("#{%s.inputValue}",         binding));
    lov.attribute("label",         attribute.stringParameter(FormInstance.Attribute.Hint.LABEL.id(), String.format("#{%s.hints.label}", binding)));
    lov.attribute("required",      String.format("#{%s.hints.mandatory}",    binding));
    lov.attribute("columns",       String.format("#{%s.hints.displayWidth}", binding));
    lov.attribute("maximumLength", String.format("#{%s.hints.precision}",    binding));
    lov.attribute("shortDesc",     attribute.stringParameter(FormInstance.Attribute.Hint.TOOLTIP.id(), String.format("#{%s.hints.tooltip}", binding)));
    lov.attribute("changed",       String.format("#{not empty %s.hints.tooltip}", String.format(iterator ? "bindings.%s__c" : "bindings.%s__c", attribute.name())));
    lov.attribute("changedDesc",   String.format("#{%s.hints.tooltip}", String.format(iterator ? "bindings.%s__c" : "bindings.%s__c", attribute.name())));
    lov.attribute("autoSubmit",    "true");
    lov.attribute("id",            iterator ? nextXGC() :nextXG());
    marshalValidatorText(lov, binding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalConvertNumber
  /**
   ** Marshals the converter element for input text.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  binding            the value binding the validator has to be
   **                            applied on.
   ** @param  iterator           <code>true</code> in the binding of the element
   **                            is based on an iterator. 
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalConvertNumber(final XMLOutputNode parent, final String binding, final boolean iterator)
    throws XMLException {

    final XMLOutputNode it = adfElement(parent, "af:convertNumber");
    it.attribute("groupingUsed", "false");
    it.attribute("pattern",      String.format("#{%s.format}", binding));
    it.attribute("id",           iterator ? nextXGC() :nextXGC());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalConvertDateTime
  /**
   ** Marshals the validator element for input text.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  binding            the value binding the validator has to be
   **                            applied on.
   ** @param  iterator           <code>true</code> in the binding of the element
   **                            is based on an iterator. 
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalConvertDateTime(final XMLOutputNode parent, final String binding, final boolean iterator)
    throws XMLException {

    marshalValidatorText(parent, binding);
    final XMLOutputNode it = adfElement(parent, "af:convertDateTime");
    it.attribute("pattern", String.format("#{%s.format}", binding));
    it.attribute("id",      iterator ? nextXGC() :nextXGC());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalValidatorText
  /**
   ** Marshals the validator element for input text.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  binding            the value binding the validator has to be
   **                            applied on.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalValidatorText(final XMLOutputNode parent, final String binding)
    throws XMLException {

    final XMLOutputNode it = jsfElement(parent, "f:validator");
    it.attribute("binding", String.format("#{%s.validator}", binding));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueBinding
  private static String valueBinding(final String name, final boolean iterator) {
    return String.format(iterator ? "row.bindings.%s__c" : "bindings.%s__c", name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextXG
  private static String nextXG() {
    return String.format("_xg_%d", sequence_xg++);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextXGC
  private static String nextXGC() {
    return String.format("_xgc_%d", sequence_xc++);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextXGTB
  private static String nextXGTB() {
    return String.format("_xgtb_%d", sequence_tg++);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextXCTB
  private static String nextXCTB() {
    return String.format("_xgctb_%d", sequence_tc++);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adfElement
  private static XMLOutputNode adfElement(final XMLOutputNode parent, final String name)
    throws XMLException {

    final XMLOutputNode af = parent.element(name);
    af.attribute("xmlns:af", "http://xmlns.oracle.com/adf/faces/rich");
    return af;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsfElement
  private static XMLOutputNode jsfElement(final XMLOutputNode parent, final String name)
    throws XMLException {

    final XMLOutputNode f = parent.element(name);
    f.attribute("xmlns:f", "http://java.sun.com/jsf/core");
    return f;
  }
}