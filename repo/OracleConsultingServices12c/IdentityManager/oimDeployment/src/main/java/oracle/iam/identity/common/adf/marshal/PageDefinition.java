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

    File        :   PageDefinition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PageDefinition.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.util.Collection;

import java.io.File;

import java.util.Map;

import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.provisioning.vo.FormField;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

////////////////////////////////////////////////////////////////////////////////
// abstract class PageDefinition
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The marshaller to spool out the page definition descriptors.
 ** <br>
 ** Those descriptors includes the bindings for a certain page like iterators,
 ** executables or events and the customization of those definitions uploaded to
 ** MDS later on by publishing the <code>Sandbox</code>.
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
public abstract class PageDefinition {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PATH        = "pageDefs/oracle/iam/ui/runtime/form/view/pages";

  public static final String BULK        = "%sBulkFormPageDef";
  public static final String CREATE      = "%sCreateFormPageDef";
  public static final String MODIFY      = "%sModifyFormPageDef";

  private static final String ROOT       = "pageDefinition";
  private static final String NAMESPACE  = "http://xmlns.oracle.com/adfm/uimodel";

  private static final String BINDING    = "(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:bindings";
  private static final String EXECUTABLE = "(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:executables";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalDefinition
  /**
   ** Marshals the page definition.
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
  public static void marshalDefinition(final Loggable loggable, final File path, final String dataSet)
    throws XMLException {

    final File          file = new File(path, String.format("%s.xml", dataSet));
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8"));
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element(ROOT);
    root.attribute("id", dataSet);
    root.attribute("Package", "pageDefs.oracle.iam.ui.runtime.form.view.pages");
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.element("parameters");
    root.element("executables");
    root.element("bindings");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalCustomization
  /**
   ** Marshals the page definition customization.
   ** <p>
   ** The page definition usually build a master/detail relation ship.
   ** <br>
   ** The provided {@link FormInfo} <code>master</code> takes the role of the
   ** master and each {@link FormInfo} element of the {@link Collection}
   ** <code>deatil</code> ist a detail of the  <code>master</code>
   ** {@link FormInfo} as the names suggest.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the files within.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  viewObject         the name of the <code>ViewObject</code>
   **                            associated with the
   **                            <code>RequestDataSet</code>.
   ** @param  master             the {@link FormInfo} acting as the master in
   **                            the master/detail relationship; usually the
   **                            form to maintain the account data.
   ** @param  action             the {@link Map} of {@link Integer}s
   **                            acting as the action index in the master/detail
   **                            relationship; usually the entitlement or
   **                            multi-value forms to maintain the account
   **                            permission data.
   ** @param  other              the {@link Collection} of {@link FormInfo}s
   **                            acting as the details in the master/detail
   **                            relationship; usually the entitlement or
   **                            multi-value forms to maintain the account
   **                            permission data.
   ** @param  operation          the operations type of the page definition to
   **                            generated; usually one of:
   **                            <ul>
   **                              <li>{@link PageDefinition#BULK}
   **                              <li>{@link PageDefinition#CREATE}
   **                              <li>{@link PageDefinition#MODIFY}
   **                            </ul>
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalCustomization(final Loggable loggable, final File path, final String version, final String dataSet, final String viewObject, final FormInfo master, final Map<String, Integer> action, final Collection<FormInfo> other, final String operation)
    throws XMLException {

    final String        view = String.format("%sVO", viewObject);
    final File          file = new File(path, String.format("%s.xml.xml", dataSet));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, version, ROOT, NAMESPACE);
    if (BULK.equals(operation)) {
      // start binding section for value attributes
      marshalBinding(root, dataSet);
    }
    else {
      marshalExecutable(root, dataSet);
      // bind the master iterator
      marshalIterator(root, dataSet, view);
      // start binding section for value attributes
      marshalBinding(root, dataSet);
      for (FormField cursor : master.getFormFields()) {
        // skip the IT Resource
        if (Metadata.Attribute.ENDPOINT.id.equals(cursor.getType()))
          continue;
        // skip any Lookup due to its done separatley
        if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType()))
          continue;

        marshalAttributeValue(root, dataSet, view, cursor, MODIFY.equals(operation));
      }

      // bind the lookups
      for (FormField cursor : master.getFormFields()) {
        if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType()))
          marshalLookup(root, dataSet, view, cursor.getName());
      }

      // bind any detail form
      for (FormInfo cursor : other) {
        marshalTreeIterator(root, dataSet, viewObject, cursor, MODIFY.equals(operation));
      }
      // bind any detail action
      for (FormInfo cursor : other) {
        marshalActionBinding(root, dataSet, viewObject, cursor.getName(), action.get(cursor.getName()));
      }
    }
    marshalEventBinding(root, dataSet, "refresh", "oracle.iam.ui.platform.view.event.RefreshEvent");
    marshalEventBinding(root, dataSet, "event",   "oracle.iam.ui.platform.view.event.ContextualEvent");
    if (BULK.equals(operation)) {
      marshalExecutable(root, dataSet);
    }
    marshalPasswordTaskflow(root, dataSet);
    marshalMethodAction(root, dataSet);
    root.close();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalCustomization
  /**
   ** Marshals the page definition customization.
   ** <p>
   ** The page definition usually build a master/detail relation ship.
   ** <br>
   ** The provided {@link FormInfo} <code>master</code> takes the role of the
   ** master and each {@link FormInfo} element of the {@link Collection}
   ** <code>deatil</code> ist a detail of the  <code>master</code>
   ** {@link FormInfo} as the names suggest.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the files within.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  viewObject         the name of the <code>ViewObject</code>
   **                            associated with the
   **                            <code>RequestDataSet</code>.
   ** @param  form               the {@link FormInfo}; usually the form to
   **                            maintain the entitlement data.
   ** @param  operation          the operations type of the page definition to
   **                            generated; usually one of:
   **                            <ul>
   **                              <li>{@link PageDefinition#BULK}
   **                              <li>{@link PageDefinition#CREATE}
   **                              <li>{@link PageDefinition#MODIFY}
   **                            </ul>
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalCustomization(final Loggable loggable, final File path, final String version, final String dataSet, final String viewObject, final FormInfo form, final String operation)
    throws XMLException {

    final String        view = String.format("%sVO", viewObject);
    final File          file = new File(path, String.format("%s.xml.xml", dataSet));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, version, ROOT, NAMESPACE);
    if (BULK.equals(operation)) {
      // start binding section for value attributes
      marshalBinding(root, dataSet);
    }
    else {
      marshalExecutable(root, dataSet);
      // bind the master iterator
      marshalIterator(root, dataSet, view);
      // start binding section for value attributes
      marshalBinding(root, dataSet);
      for (FormField cursor : form.getFormFields()) {
        // skip the IT Resource
        if (Metadata.Attribute.ENDPOINT.id.equals(cursor.getType()))
          continue;
        // skip any Lookup due to its done separatley
        if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType()))
          continue;
        // avoid rendering of the entitlement it self
        final Object entitlement = cursor.getProperty("Entitlement");
        if (entitlement != null && "true".equalsIgnoreCase(entitlement.toString()))
          continue;

        marshalAttributeValue(root, dataSet, view, cursor, MODIFY.equals(operation));
      }
      // bind the lookups
      for (FormField cursor : form.getFormFields()) {
        // avoid marshallling of the entitlement it self
        final Object entitlement = cursor.getProperty("Entitlement");
        if (entitlement != null && "true".equalsIgnoreCase(entitlement.toString()))
          continue;

        if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType()))
          marshalLookup(root, dataSet, view, cursor.getName());
      }
    }
    marshalEventBinding(root, dataSet, "refresh", "oracle.iam.ui.platform.view.event.RefreshEvent");
    marshalEventBinding(root, dataSet, "event",   "oracle.iam.ui.platform.view.event.ContextualEvent");
    if (BULK.equals(operation)) {
      marshalExecutable(root, dataSet);
    }
    marshalPasswordTaskflow(root, dataSet);
    marshalMethodAction(root, dataSet);
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalExecutable
  /**
   ** Marshals the executable binding of a page definition.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalExecutable(final XMLOutputNode parent, final String dataSet)
    throws XMLException {

    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet);
    node.element("executables").attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalIterator
  /**
   ** Marshals the iterator binding of a page definition.
   ** <br>
   ** The marshaled fragement looks like:
   ** <pre>
   **   &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:executables" position="last"&gt;
   **     &lt;iterator Binds="%lt;viewObject%gt;VO" RangeSize="25" DataControl="CatalogAMDataControl" id="%lt;viewObject%gt;VOIterator" xmlns="http://xmlns.oracle.com/adfm/uimodel"/><iterator Binds="%lt;viewObject%gt;VO" RangeSize="25" DataControl="CatalogAMDataControl" id="%lt;viewObject%gt;VOIterator" xmlns="http://xmlns.oracle.com/adfm/uimodel"/&gt;
   **   &lt;/mds:insert&gt;
   ** </pre>
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalIterator(final XMLOutputNode parent, final String dataSet, final String viewObject)
    throws XMLException {

    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + EXECUTABLE);
    node = node.element("iterator");
    node.attribute("Binds", viewObject);
    node.attribute("RangeSize", "25");
    node.attribute("DataControl", "CatalogAMDataControl");
    node.attribute("id", viewObject + "Iterator");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalBinding
  /**
   ** Marshals the data binding of a page definition.
   ** <br>
   ** The marshaled fragement looks like:
   ** <pre>
   **   &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef" position="last"&gt;
   **     &lt;bindings xmlns="http://xmlns.oracle.com/adfm/uimodel/&gt;
   **   &lt;/mds:insert&gt;
   ** </pre>
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalBinding(final XMLOutputNode parent, final String dataSet)
    throws XMLException {

    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet);
    node.element("bindings").attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAttributeValue
  /**
   ** Marshals the attribute value binding of a page definition.
   ** <br>
   ** The marshald fragement looks like:
   ** <pre>
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:bindings" position="last"&gt;
   **   &lt;attributeValues IterBinding="%lt;viewObject%gt;VOIterator" id="UD_EFBSE_ID__c" xmlns="http://xmlns.oracle.com/adfm/uimodel"&gt;
   **     &lt;AttrNames&gt;
   **       &lt;Item Value="UD_EFBSE_ID__c"/&gt;
   **     &lt;/AttrNames&gt;
   **   &lt;/attributeValues&gt;
   ** &lt;/mds:insert&gt;
   ** </pre>
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalAttributeValue(final XMLOutputNode parent, final String dataSet, final String viewObject, final FormField attribute, final boolean modify)
    throws XMLException {

    if (Metadata.Attribute.CHECKBOX.id.equals(attribute.getType())) {
      // the service account flag is not modifiable
      if ("serviceaccount".equalsIgnoreCase(attribute.getName()) && modify)
        return;
      // add the checkbox account attribute to the account form
      marshalCheckBox(parent, dataSet, viewObject, attribute.getName());
    }
    else {
      final String name = String.format("%s__c", attribute.getName());
      XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + BINDING);
      node = node.element("attributeValues");
      node.attribute("IterBinding", viewObject + "Iterator");
      node.attribute("id", name);
      node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
      node.element("AttrNames").element("Item").attribute("Value", name);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalCheckBox
  /**
   ** Marshals a <code>CheckBox</code> attribute value binding of a page
   ** definition.
   ** <br>
   ** for example the marshaled fragement for the service account attributes
   ** looks like:
   ** <pre>
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:bindings" position="last"&gt;
   **   &lt;button IterBinding="%lt;viewObject%gt;VOIterator" id="%lt;attribute%gt;__c" DTSupportsMRU="false" xmlns="http://xmlns.oracle.com/adfm/uimodel"&gt;
   **     &lt;AttrNames&gt;
   **       &lt;Item Value="%lt;attribute%gt;__c"/&gt;
   **     &lt;/AttrNames&gt;
   **     &lt;ValueList&gt;
   **       &lt;Item Value="false"/&gt;
   **       &lt;Item Value="true"/&gt;
   **     &lt;/ValueList&gt;
   **   &lt;/butten&gt;
   ** &lt;/mds:insert&gt;
   ** </pre>
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  viewObject         the name of the <code>ViewObject</code> to
   **                            marshall.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The name <b>must</b> be have the postfix
   **                            </code>VO</code>.
   ** @param  attribute          the name of the attribute to marshall.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The name <b>must not</b> be have the postfix
   **                            </code>__c</code>.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalCheckBox(final XMLOutputNode parent, final String dataSet, final String viewObject, final String attribute)
    throws XMLException {

    final String        name = String.format("%s__c", attribute);
    final XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + BINDING).element("button");
    node.attribute("IterBinding", viewObject + "Iterator");
    node.attribute("id", name);
    node.attribute("DTSupportsMRU", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node.element("AttrNames").element("Item").attribute("Value", name);
    final XMLOutputNode list = node.element("ValueList");
    list.element("Item").attribute("Value", "true");
    list.element("Item").attribute("Value", "false");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalLookup
  /**
   ** Marshals a <code>InputListValue</code> attribute value binding of a page
   ** definition.
   ** <br>
   ** for example the marshaled fragement for the service account attributes
   ** looks like:
   ** <pre>
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:bindings" position="last"&gt;
   **   &lt;listOfValues id="%lt;attribute%gt;__c" StaticList="false" IterBinding="%lt;viewObject%gt;VOIterator" Uses="LOV_For_%lt;attribute%gt;__c"/%gt;
   ** &lt;/mds:insert&gt;
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:bindings" position="last"&gt;
   ** &lt;/mds:insert&gt;

   **     &lt;AttrNames&gt;
   **       &lt;Item Value="%lt;attribute%gt;"/&gt;
   **     &lt;/AttrNames&gt;
   **     &lt;ValueList&gt;
   **       &lt;Item Value="false"/&gt;
   **       &lt;Item Value="true"/&gt;
   **     &lt;/ValueList&gt;
   **   &lt;/butten&gt;
   ** &lt;/mds:insert&gt;
   ** </pre>
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  viewObject         the name of the <code>ViewObject</code> to
   **                            marshall.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The name <b>must</b> be have the postfix
   **                            </code>VO</code>.
   ** @param  attribute          the name of the attribute to marshall.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The name <b>must not</b> be have the postfix
   **                            </code>__c</code>.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalLookup(final XMLOutputNode parent, final String dataSet, final String viewObject, final String attribute)
    throws XMLException {

    final String  name = String.format("%s__c", attribute);
    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + BINDING).element("listOfValues");
    node.attribute("id",          name);
    node.attribute("StaticList",  "false");
    node.attribute("IterBinding", String.format("%sIterator", viewObject));
    node.attribute("Uses",        String.format("LOV_For_%s",   name));
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = Metadata.marshalInsert(parent, dataSet + BINDING).element("attributeValues");
    node.attribute("id",                         name + "_Id__c");
    node.attribute("IterBinding",                String.format("%sIterator", viewObject));
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("AttrNames");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "");
    node = node.element("Item").attribute("Value", name + "_Id__c");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalTreeIterator
  /**
   ** Marshals the binding for the search region of a detail form in a page
   ** definition.
   ** <br>
   ** The marshaled fragement looks like:
   ** <pre>
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:executables" position="last"&gt;
   **     &lt;iterator Binds="%lt;viewObject%gt;UD_EFBSE_MVO" RangeSize="25" DataControl="CatalogAMDataControl" id="%lt;viewObject%gt;VOIterator" xmlns="http://xmlns.oracle.com/adfm/uimodel"/><iterator Binds="%lt;viewObject%gt;VO" RangeSize="25" DataControl="CatalogAMDataControl" id="%lt;viewObject%gt;VOIterator" xmlns="http://xmlns.oracle.com/adfm/uimodel"/&gt;
   ** &lt;/mds:insert&gt;
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:executables" position="last"&gt;
   **   &lt;tree IterBinding="%lt;viewObject%gt;UD_EFBSE_MVOIterator" id="%lt;viewObject%gt;UD_EFBSE_MVO" xmlns="http://xmlns.oracle.com/adfm/uimodel"&gt;
   **     &lt;nodeDefinition DefName="sessiondef.oracle.iam.ui.runtime.form.model.%lt;viewObject%gt;.view.%lt;viewObject%gt;UD_EFBSE_MVO" Name="%lt;viewObject%gt;UD_EFBSE_MVO0"&gt;
   **       &lt;AttrNames&gt;
   **         &ltItem Value="UD_EFBSE_M_VALUE__c"/&gt;
   **       &lt;/AttrNames&gt;
   **     &lt;/nodeDefinitions&gt;
   **   &lt;/tree&gt;
   ** &lt;/mds:insert&gt;
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:executables" position="last"&gt;
   **   &lt;searchRegion Binds="%lt;viewObject%gt;UD_EFBSE_MVOIterator" Criteria="" Customizer="oracle.jbo.uicli.binding.JUSearchBindingCustomizer" id="%lt;viewObject%gt;UD_EFBSE_MVOQuery" xmlns="http://xmlns.oracle.com/adfm/uimodel"&gt;
   ** &lt;/mds:insert&gt;
   ** </pre>
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  viewObject         the name of the <code>ViewObject</code> to
   **                            marshall.
   ** @param  other              the process form being marshalled.
   ** @param  modify             <code>true</code> if the modify page definition
   **                            being marshalled.
   **
   ** @return                    the {@link XMLOutputNode} containing the XML
   **                            fragments for MDS insert operation.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static XMLOutputNode marshalTreeIterator(final XMLOutputNode parent, final String dataSet, final String viewObject, final FormInfo other, final boolean modify)
    throws XMLException {

    final String treeObject = String.format("%s%sVO", viewObject, other.getName());
    marshalIterator(parent, dataSet, treeObject);

    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + BINDING);
    node = node.element("tree");
    node.attribute("IterBinding", treeObject + "Iterator");
    node.attribute("id", treeObject);
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("nodeDefinition");
    node.attribute("DefName", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%s.view.%s", viewObject, treeObject));
    node.attribute("Name", String.format("%s0", treeObject));
    node = node.element("AttrNames");

    // bind the attributes to the tree iterator
    if (modify)
      node.element("Item").attribute("Value", "action");

    for (FormField field : other.getFormFields()) {
      node.element("Item").attribute("Value", String.format("%s__c", field.getName()));
    }
    // bind searchRegion for the detail form
    node = Metadata.marshalInsert(parent, dataSet + EXECUTABLE);
    node = node.element("searchRegion");
    node.attribute("Binds", treeObject + "Iterator");
    node.attribute("Criteria", "");
    node.attribute("Customizer", "oracle.jbo.uicli.binding.JUSearchBindingCustomizer");
    node.attribute("id", treeObject + "Query");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEventBinding
  /**
   ** Marshals the event binding of a page definition.
   ** <br>
   ** The marshaled fragement looks like:
   ** <pre>
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:bindings" position="last"&gt;
   **   &lt;eventBinding id="refresh"&gt;
   **     &lt;events xmlns="http://xmlns.oracle.com/adfm/contextualEvent"&gt;
   **        &lt;event name="oracle.iam.ui.platform.view.event.RefreshEvent" xmlns=""/&gt;
   **     &lt;/events&gt;
   **   &lt;/eventBinding&gt;
   ** &lt;/mds:insert&gt;
   ** </pre>
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  event              the identifier of the event binding.
   ** @param  name               the name of the event.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalEventBinding(final XMLOutputNode parent, final String dataSet, final String event, final String name)
    throws XMLException {

    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + BINDING);
    node = node.element("eventBinding");
    node.attribute("id", event);
    node = node.element("events");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adfm/contextualEvent");
    node = node.element("event");
    node.attribute("name", name);
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalPasswordTaskflow
  /**
   ** Marshals the execution binding for the default password policy taskflow of
   ** a page definition.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalPasswordTaskflow(final XMLOutputNode parent, final String dataSet)
    throws XMLException {

    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + EXECUTABLE);
    node = node.element("taskFlow");
    node.attribute("id", "pwdpolicyinfo");
    node.attribute("taskFlowId", "/WEB-INF/oracle/iam/ui/common/tfs/pwdpolicy-info-tf.xml#pwdpolicy-info-tf");
    node.attribute("activation", "conditional");
    node.attribute("active", "#{pageFlowScope.entityType eq 'ApplicationInstance'}");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adf/controller/binding");
    node = node.element("parameters");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "");
    XMLOutputNode parameter = node.element("parameter");
    parameter.attribute("id", "userLogin");
    parameter.attribute("value", "#{pageFlowScope.cartDetailStateBean.userLogin}");
    parameter = node.element("parameter");
    parameter.attribute("id", "accountType");
    parameter.attribute("value", "#{pageFlowScope.entityId}");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalMethodAction
  /**
   ** Marshals the execution binding for the application module method to obtain
   ** the user information.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalMethodAction(final XMLOutputNode parent, final String dataSet)
    throws XMLException {

    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + BINDING);
    node = node.element("methodAction");
    node.attribute("id", "getUserLogin");
    node.attribute("RequiresUpdateModel", "true");
    node.attribute("Action", "invokeMethod");
    node.attribute("MethodName", "getUserLogin");
    node.attribute("IsViewObjectMethod", "false");
    node.attribute("DataControl", "CatalogAMDataControl");
    node.attribute("InstanceName", "CatalogAMDataControl.dataProvider");
    node.attribute("ReturnName", "data.CatalogAMDataControl.methodResults.getUserLogin_CatalogAMDataControl_dataProvider_getUserLogin_result");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalActionBinding
  /**
   ** Marshals the action binding for a detail form .
   ** <br>
   ** The marshaled fragement looks like:
   ** <pre>
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:bindings" position="last"&gt;
   **   &lt;action id="CreateInsert1" RequiresUpdateModel="false" IterBinding="%lt;viewObject%gt;UD_EFBSE_MVOIterator" Action="createWithParams" xmlns="http://xmlns.oracle.com/adfm/uimodel"&gt;
   **     &lt;NamedData NDName="ID" NDType="java.lang.String" NDValue="#{pageFlowScope.cartDetailStateBean.childFormId}" xmlns=""/&gt;
   **        &lt;event name="oracle.iam.ui.platform.view.event.RefreshEvent" xmlns=""/&gt;
   **     &lt;/action&gt;
   ** &lt;/mds:insert&gt;
   ** &lt;mds:insert parent="%lt;viewObject%gt;CreateFormPageDef(xmlns(mds_ns1=http://xmlns.oracle.com/adfm/uimodel))/mds_ns1:bindings" position="last"&gt;
   **   &lt;action id="Delete1" RequiresUpdateModel="false" IterBinding="%lt;viewObject%gt;UD_EFBSE_MVOIterator" Action="removeCurrentRow" xmlns="http://xmlns.oracle.com/adfm/uimodel"/&gt;
   ** &lt;/mds:insert&gt;
   ** </pre>
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  sequence           the action sequence name of the
   **                            <code>RequestDataSet</code> associated with the
   **                            sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalActionBinding(final XMLOutputNode parent, final String dataSet, final String viewObject, final String other, final int sequence)
    throws XMLException {

    final String  iterator = String.format("%s%sVOIterator", viewObject, other);
    XMLOutputNode node = Metadata.marshalInsert(parent, dataSet + BINDING);
    node = node.element("action");
    node.attribute("id", String.format("CreateInsert%d", sequence));
    node.attribute("RequiresUpdateModel", "false");
    node.attribute("IterBinding", iterator);
    node.attribute("Action", "createWithParams");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("NamedData");
    node.attribute("NDName", "ID");
    node.attribute("NDType", "java.lang.String");
    node.attribute("NDValue", "#{pageFlowScope.cartDetailStateBean.childFormId}");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "");
    node = Metadata.marshalInsert(parent, dataSet + BINDING);
    node = node.element("action");
    node.attribute("id", String.format("Delete%d", sequence));
    node.attribute("RequiresUpdateModel", "false");
    node.attribute("IterBinding", iterator);
    node.attribute("Action", "removeCurrentRow");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
  }
}