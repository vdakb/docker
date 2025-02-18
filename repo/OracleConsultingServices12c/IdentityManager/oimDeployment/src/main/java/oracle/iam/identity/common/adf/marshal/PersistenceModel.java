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

    File        :   PersistenceModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceModel.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collection;

import java.io.File;
import java.io.IOException;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.provisioning.vo.FormField;

import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.iam.identity.common.spi.SandboxInstance;

////////////////////////////////////////////////////////////////////////////////
// abstract class PersistenceModel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**persistence
 ** The marshaller to spool out the persistence model definition objects.
 ** <br>
 ** Oracle ADF Business Components use persistence definition objects to
 ** customize the business component definitions at runtime. ADF uses
 ** <code>oracle.jbo.server.PDefEntityObject</code>,
 ** <code>oracle.jbo.server.PDefViewObject</code>, and
 ** <code>oracle.jbo.server.PDefApplicationModule</code> to store the runtime
 ** customization for an entity object, view objects, and application modules
 ** respectively.
 ** <br>
 ** When the framework loads the application module definition, view definition,
 ** or entity definition, it checks whether there is a corresponding
 ** persistence definition object present and, if present, wears the PDef
 ** like a transparent overcoat on top of all of the existing features of that
 ** object. It provides a context on which to pin additional attributes and
 ** validators. The end user sees a definition that is the union of the base
 ** definition plus the additional persistence. The runtime changes are
 ** stored in the Meta Data Service (MDS) store.
 ** <br>
 ** All the structural changes are stored at site level, reflecting changes across
 ** all user sessions.
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
public abstract class PersistenceModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static  final String       PATH      = "persdef/sessiondef/oracle/iam/ui/runtime/form/model";
  private static final String       NAMESPACE = "http://xmlns.oracle.com/bc4j";
  // lower case letters chosen to avoid name clash with java.util.UUID
  private static final List<String> uuid      = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Marshals the ADF personalized definition.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  account            the account model of a process form.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>account</code> process form.
   **
   ** @throws IOException        if the operations at the file system fails.
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshal(final Loggable loggable, final File path, final SandboxInstance.Version version, final String dataSet, final FormInfo account, final Collection<FormInfo> other)
    throws IOException
    ,      XMLException {

    // reset uuid list
    uuid.clear();
    
    final Metadata.Cust entity = Metadata.cust(Metadata.path(path, dataSet), "entity").ensureExists();
    final Metadata.Cust view   = Metadata.cust(Metadata.path(path, dataSet), "view").ensureExists();

    marshalEntity(loggable, entity, dataSet);
    marshalEntity(loggable, entity.path, version, dataSet, account, true);
    marshalView(loggable, view, dataSet);
    marshalView(loggable, view.path, version, dataSet, dataSet, account, true);
    if (version == SandboxInstance.Version.PS3) {
      for (FormField cursor : account.getFormFields()) {
        if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType())) {
          marshalViewOperation(loggable, view, dataSet);
          marshalViewOperation(loggable, view.path, version, dataSet, account, true);
          break;
        }
      }
    }

    for (FormInfo cursor : other) {
      final String surrogate = String.format("%s%s", dataSet, cursor.getName());
      marshalEntity(loggable, entity, surrogate);
      marshalEntity(loggable, entity.path, version, dataSet, cursor, false);
      marshalView(loggable, view, surrogate);
      marshalView(loggable, view.path, version, dataSet, surrogate, cursor, false);
      if (version == SandboxInstance.Version.PS3) {
        for (FormField attr : cursor.getFormFields()) {
          if (Metadata.Attribute.LOOKUP.id.equals(attr.getType())) {
            marshalViewOperation(loggable, view, surrogate);
            marshalViewOperation(loggable, view.path, version, surrogate, cursor, false);
            break;
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntity
  /**
   ** Marshals the ADF entity definition.
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
  private static void marshalEntity(final Loggable loggable, final File path, final String dataSet)
    throws XMLException {

    final File          file = new File(path, String.format("%sEO.xml", dataSet));
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8"));
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element("PDefEntityObject");
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("Name",     String.format("%sEO", dataSet));
    root.attribute("EmptyDoc", "true");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntity
  /**
   ** Marshals the ADF entity customizations.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  form               the process forms associated with the
   **                            <code>dataSet</code>.
   ** @param  acoount            whether the specified <code>form</code> is the
   **                            <code>Account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalEntity(final Loggable loggable, final File path, final SandboxInstance.Version version, final String dataSet, final FormInfo form, final boolean account)
    throws XMLException {

    final String        name = account ? String.format("%sEO", dataSet) : String.format("%s%sEO", dataSet, form.getName());
    final File          file = new File(path, String.format("%s.xml.xml", name));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, version.customization, "PDefEntityObject", NAMESPACE);
    // regular attributes
    for (FormField cursor : form.getFormFields()) {
      marshalEntityAttribute(root, dataSet, name, cursor, account);
    }
    // marshal entity definition
    final XMLOutputNode node = Metadata.marshalModify(root, name);
    Metadata.marshalAttribute(node, "StaticDef", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%s.entity.%s", dataSet, name));
    Metadata.marshalAttribute(node, "MajorVersion", "1");
    Metadata.marshalAttribute(node, "MinorVersion", "0");
    Metadata.marshalAttribute(node, "EmptyDoc",     "false");
    Metadata.marshalAttribute(node, "Version",      version.persistence);
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntityAttribute
  /**
   ** Marshals the attributes for an ADF entity attribute customizations.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  entityObject       the name of the <code>Entity</code> object
   **                            associated with the sandbox.
   ** @param  attribute          the <code>FormField</code> providing the
   **                            properties of the attribute to marshal.
   ** @param  acoount            whether the specified <code>form</code> is the
   **                            <code>Account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalEntityAttribute(final XMLOutputNode parent, final String dataSet, final String entityObject, final FormField attribute, final boolean account)
    throws XMLException {

    final boolean            numa = attribute.getVariantType() != null && attribute.getVariantType().equals("long");
    final Metadata.Attribute type = Metadata.Attribute.from(attribute.getType());
    String                   name = String.format("%s__c", attribute.getName());
    XMLOutputNode            root = Metadata.marshalInsert(parent, entityObject);
    XMLOutputNode            attr = root.element("Attribute");
    attr.attribute("Name", name);
    final boolean readOnly = (attribute.getProperties().containsKey("AccountId") || type == Metadata.Attribute.READONLY);
    if (readOnly)
      attr.attribute("IsUpdateable", "false");
    attr.attribute("IsPersistent", "false");
    if (name.equalsIgnoreCase("serviceaccount__c"))
      attr.attribute("IsQueriable", "false");
    switch (type) {
      case DATE     : break;
      case CHECKBOX : attr.attribute("Precision",    "10");
                      attr.attribute("Scale",        "0");
                      break;
      case ENDPOINT : attr.attribute("DefaultValue", "0");
                      break;
      case LOOKUP   : attr.attribute("IsQueriable",  account ? "false" : "true");
                      if (!readOnly) {
                        attr.attribute("DefaultValue", attribute.getDefaultValue());
                      }
                      attr.attribute("Precision",    String.valueOf(attribute.getLength()));
                      attr.attribute("Scale",        "0");
                      break;
      case TEXT     :
      case NUMBER   :
      case TEXTAREA :
      case PASSWORD :
      case READONLY : if (!readOnly) {
                        attr.attribute("DefaultValue", attribute.getDefaultValue());
                      }
                      if (!numa) {
                        attr.attribute("Precision",    String.valueOf(attribute.getLength()));
                        attr.attribute("Scale",        "0");
                      }
                      break;
      default       : throw new IllegalArgumentException("type");
    }

    attr.attribute("Expression", name.toUpperCase());
    if (numa) {
      attr.attribute("Type",       "oracle.jbo.domain.Number");
      attr.attribute("ColumnType", "NUMBER");
      attr.attribute("SQLType",    "NUMERIC");
    }
    else {
      attr.attribute("Type",       type.clazz);
      attr.attribute("ColumnType", type.domain);
      attr.attribute("SQLType",    type.ansi);
    }
    attr.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    XMLOutputNode node = attr.element("Properties");

    marshalAttributeProperty(node, "AttributeType", numa ? "Number" : type.type);
    if (type == Metadata.Attribute.CHECKBOX) {
      marshalAttributeProperty(node, "CONTROLTYPE", "check_box");
    }
    if (!"serviceaccount__c".equalsIgnoreCase(name))
      marshalAttributeProperty(node, "description_ResId", String.format("${adfBundle['oracle.adf.businesseditor.model.util.BaseRuntimeResourceBundle']['persdef.sessiondef.oracle.iam.ui.runtime.form.model.user.entity.userEO.%s_description']}", name));
    if ((type == Metadata.Attribute.TEXT) || (type == Metadata.Attribute.TEXTAREA) || (type == Metadata.Attribute.NUMBER) || (type == Metadata.Attribute.READONLY) || (type == Metadata.Attribute.PASSWORD) || (type == Metadata.Attribute.LOOKUP)) {
      if (!numa)
        marshalAttributeProperty(node, "DISPLAYWIDTH", "40");// TODO: get a customized value from the ANT consfiguration
    }

    marshalAttributeProperty(node, "ExtnCustom", "Y");
    if (type == Metadata.Attribute.ENDPOINT)
      marshalAttributeProperty(node, "ITResource", "Y");
    marshalAttributeProperty(node, "LABEL_ResId",  String.format("${adfBundle['oracle.adf.businesseditor.model.util.BaseRuntimeResourceBundle']['sessiondef.oracle.iam.ui.runtime.form.model.%1$s.entity.%2$s.%3$s_LABEL']}", dataSet, entityObject, name));
    if (type == Metadata.Attribute.LOOKUP) {
      marshalAttributeProperty(node, "LookupType", (String)attribute.getProperty("Lookup Code"));
      final Object test = attribute.getProperty("Entitlement");
      if (test != null && "true".equalsIgnoreCase(test.toString()))
        marshalAttributeProperty(node, "oimEntitlement", "Y");
    }
    if (type == Metadata.Attribute.PASSWORD) {
      marshalAttributeProperty(node, "oimEncrypted", "Y");
    }
    marshalAttributeProperty(node, "oimRefAttrName", attribute.getLabel());
    if (type == Metadata.Attribute.PASSWORD) {
      marshalAttributeProperty(node, "SECRET", "Y");
    }
    if (type == Metadata.Attribute.LOOKUP) {
      marshalAttributeProperty(node, "searchablePicklist", "Y");
      if (!account) {
        marshalAttributeProperty(node, "oimBulkUpdate", "N");
        final Object test = attribute.getProperty("Entitlement");
        if (test == null || (test != null && ("false".equalsIgnoreCase(test.toString()))))
          marshalAttributeProperty(node, "oimEntitlement", "N");
        }
    }

    switch (type) {
      case ENDPOINT : marshalOperatorNumeric(attr);
                      break;
      case TEXT     :
      case NUMBER   :
      case TEXTAREA :
      case PASSWORD :
      case READONLY : if (numa)
                        marshalOperatorNumeric(attr);
                      else
                        marshalOperatorString(attr);
                      break;
      case LOOKUP   : marshalOperatorLookup(attr);
                      break;
      case DATE     : marshalOperatorDate(attr);
                      break;
      case CHECKBOX : marshalOperatorBoolean(attr);
                      break;
      default       : throw new IllegalArgumentException("type");
    }
    node = attr.element("CompOper");
    node.attribute("Name",           "Dummy");
    node.attribute("ToDo",           "-2");
    node.attribute("Oper",           "Dummy");
    node.attribute("MinCardinality", "1");
    node.attribute("MaxCardinality", "1");
    if (type == Metadata.Attribute.LOOKUP) {
      name = String.format("%s__c_Id__c", attribute.getName());
      root = Metadata.marshalInsert(parent, entityObject);
      attr = root.element("Attribute");
      attr.attribute("Name",          name);
      attr.attribute("IsPersistent",  "false");
      attr.attribute("Expression",    name.toUpperCase());
      attr.attribute("Type",          type.clazz);
      attr.attribute("ColumnType",    type.domain);
      attr.attribute("SQLType",       type.ansi);
      attr.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);

      node = attr.element("Properties");
      marshalAttributeProperty(node, "ATTR_INTERNAL_USE", "Y");
      marshalAttributeProperty(node, "DISPLAYHINT",       "Hide");
      marshalAttributeProperty(node, "ExtnCustom",        "Y");
      marshalAttributeProperty(node, "oimRefAttrName",    attribute.getLabel());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAttributeProperty
  /**
   ** Marshals a property for the attributes of an ADF entity customizations.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   ** @param  name               the name of the property.
   ** @param  value              the value of the property.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalAttributeProperty(final XMLOutputNode parent, final String name, final String value)
    throws XMLException {

    final XMLOutputNode node = parent.element("Property");
    node.attribute("Name",  name);
    node.attribute("Value", value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalOperatorBoolean
  /**
   ** Marshals the operators which are appropriate for a boolean attribute.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalOperatorBoolean(final XMLOutputNode parent)
    throws XMLException {

    marshalAttributeComperator(parent, "=");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalOperatorDate
  /**
   ** Marshals the operators which are appropriate for a date attribute.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalOperatorDate(final XMLOutputNode parent)
    throws XMLException {

    marshalAttributeComperator(parent, "=");
    marshalAttributeComperator(parent, "ONORAFTER");
    marshalAttributeComperator(parent, "ONORBEFORE");
    marshalAttributeComperator(parent, "BEFORE");
    marshalAttributeComperator(parent, "AFTER");
    marshalAttributeComperator(parent, "BETWEEN");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalOperatorString
  /**
   ** Marshals the operators which are appropriate for a string attribute.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalOperatorString(final XMLOutputNode parent)
    throws XMLException {

    marshalAttributeComperator(parent, "=");
    marshalAttributeComperator(parent, "STARTSWITH");
    marshalAttributeComperator(parent, "ENDSWITH");
    marshalAttributeComperator(parent, "<>");
    marshalAttributeComperator(parent, "CONTAINS");
    marshalAttributeComperator(parent, "DOESNOTCONTAIN");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalOperatorLookup
  /**
   ** Marshals the operators which are appropriate for a lookup attribute.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalOperatorLookup(final XMLOutputNode parent)
    throws XMLException {

    marshalAttributeComperator(parent, "=");
    marshalAttributeComperator(parent, "<>");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalOperatorNumeric
  /**
   ** Marshals the operators which are appropriate for a numeric attribute.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalOperatorNumeric(final XMLOutputNode parent)
    throws XMLException {

    marshalAttributeComperator(parent, "=");
    marshalAttributeComperator(parent, "<");
    marshalAttributeComperator(parent, "<=");
    marshalAttributeComperator(parent, ">");
    marshalAttributeComperator(parent, ">=");
    marshalAttributeComperator(parent, "BETWEEN");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalView
  /**
   ** Marshals the comparison root element for an attribute.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   ** @param  name               the comparison operator name.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalAttributeComperator(final XMLOutputNode parent, final String name)
    throws XMLException {

    final XMLOutputNode node = parent.element("CompOper");
    node.attribute("Name",           name);
    node.attribute("ToDo",           "2");
    node.attribute("Oper",           name);
    node.attribute("MinCardinality", "1");
    node.attribute("MaxCardinality", "1");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalView
  /**
   ** Marshals the ADF view definition.
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
  private static void marshalView(final Loggable loggable, final File path, final String dataSet)
    throws XMLException {

    final File          file = new File(path, String.format("%sVO.xml", dataSet));
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8"));
    final XMLOutputNode root   = XMLProcessor.marshal(loggable, file, format).element("PDefViewObject");
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("Name",     String.format("%sVO", dataSet));
    root.attribute("EmptyDoc", "true");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalView
  /**
   ** Marshals the ADF view definition.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  viewObject         the name of the <code>ViewObject</code>
   **                            associated with the dataSet.
   ** @param  form               the process form associated with the
   **                            <code>dataSet</code> process form.
   ** @param  acoount            whether the specified <code>form</code> is the
   **                            <code>Account</code> process form.
   **
   ** @throws IOException        if the operations at the file system fails.
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalView(final Loggable loggable, final File path, final SandboxInstance.Version version, final String dataSet, final String viewObject, final FormInfo form, final boolean account)
    throws IOException
    ,      XMLException {

    final File          file = new File(path, String.format("%sVO.xml.xml", viewObject));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, version.customization, "PDefViewObject", NAMESPACE);

    XMLOutputNode head = Metadata.marshalInsert(root, String.format("%sVO", viewObject));
    head = head.element("Properties");
    head.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    XMLOutputNode prop = head.element("Property");
    prop.attribute("Name",  "oimDescription");
    prop.attribute("Value", form.getDescription());
    prop = head.element("Property");
    prop.attribute("Name",  "oimDisplayName");
    prop.attribute("Value", account ? dataSet : form.getName().substring(3));
    // Attributes
    for (FormField cursor : form.getFormFields()) {
      marshalViewAttribute(root, version, account ? dataSet : viewObject, viewObject, cursor, account);
    }

    // View Accessor Attributes
    for (FormField cursor : form.getFormFields()) {
      if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType())) {
        // avoid marshallling of the transient expression
        final Object  entitlement = cursor.getProperty("Entitlement");
        final String  lookup      = version == SandboxInstance.Version.PS4 ? (String)cursor.getProperty("Lookup Code") : null;
        final boolean xxxxx       = (entitlement != null && "true".equalsIgnoreCase(entitlement.toString()) && form.getFormFields().size() > 1);
        marshalViewAccessorLookup(root, version, account ? dataSet : viewObject, cursor.getName(), lookup, account, xxxxx);
      }
    }

    // Account Form Checkboxes View Accessor
    if (account) {
      for (FormField cursor : form.getFormFields()) {
        if (Metadata.Attribute.CHECKBOX.id.equals(cursor.getType()))
          marshalViewAccessorCheckBox(root, dataSet, cursor.getName());
      }
    }

    // List Bindings
    for (FormField cursor : form.getFormFields()) {
      if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType()))
        marshalListBindingLookup(root, account ? dataSet : viewObject, cursor.getName());
    }

    // Account Form Checkboxes List Binding
    if (account) {
      for (FormField cursor : form.getFormFields()) {
        if (Metadata.Attribute.CHECKBOX.id.equals(cursor.getType()))
          marshalListBindingCheckBox(root, dataSet, cursor.getName());
      }
    }

    final XMLOutputNode node = Metadata.marshalModify(root, String.format("%sVO", viewObject));
    Metadata.marshalAttribute(node, "StaticDef",       String.format("sessiondef.oracle.iam.ui.runtime.form.model.%s.view.%s", dataSet, String.format("%sVO", viewObject)));
    Metadata.marshalAttribute(node, "MajorVersion",    "1");
    Metadata.marshalAttribute(node, "MinorVersion",    "0");
    Metadata.marshalAttribute(node, "EmptyDoc",        "false");
    Metadata.marshalAttribute(node, "Version",         version.persistence);
    if (!account && version == SandboxInstance.Version.PS3) {
      Metadata.marshalAttribute(node, "SelectListFlags", "1");
      Metadata.marshalAttribute(node, "FromListFlags",   "1");
    }
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalViewAcessor
  /**
   ** Marshals the ADF view accessor attributes.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   ** @param  version            the version indicator of the customization.
   ** @param  viewObject         the name of the <code>DataSet</code> to
   **                            marshal.
   ** @param  name               the name of the <code>FormField</code> to
   **                            marshal.
   ** @param  lookup             the name of the <code>Lookup Definition</code>
   **                            to bind.
   ** @param  account            whether the specified <code>name</code> is
   **                            marshalled for the <code>Account</code> process
   **                            form.
   ** @param  entitlement        whether the specified <code>name</code> is
   **                            marshalled for an <code>Entitlement</code>
   **                            process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalViewAccessorLookup(final XMLOutputNode parent, final SandboxInstance.Version version, final String dataSet, final String name, final String lookup, final boolean account, final boolean entitlement)
    throws XMLException {
    
    final String lovva = String.format("LOVVA_For_%s__c", name);
    final String bind  = String.format("%sBindVariable",  lovva);
    XMLOutputNode node = Metadata.marshalInsert(parent, String.format("%sVO", dataSet)).element("ViewAccessor");
    node.attribute("Name",                       lovva);
    node.attribute("ViewObjectName",             "oracle.iam.ui.common.model.lookupcode.lookup.view.Lookups");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    XMLOutputNode prm = node.element("ParameterMap");
    XMLOutputNode map = prm.element("PIMap");
    map.attribute("Name",          "Bind_LookupType");
    map.attribute("Variable",      "Bind_LookupType");
    map = map.element("TransientExpression");
    map.attribute("Name",           bind);
    if (version == SandboxInstance.Version.PS3) {
      map.attribute("CodeSourceName", "PersonalizationCodeSource");
      if (!entitlement) {
        map.data(true);
        map.value("");
      }

      map = prm.element("PIMap");
      map.attribute("Name",          "Bind_EntityKey");
      map.attribute("Variable",      "Bind_EntityKey");
      map = map.element("TransientExpression");
      map.attribute("Name",           bind);
      map.attribute("CodeSourceName", "PersonalizationCodeSource");
      if (!entitlement) {
        map.data(true);
        map.value("");
      }
      if (!account) {
        map = prm.element("PIMap");
        map.attribute("Name",           "Bind_CurrentFieldValue");
        map.attribute("Variable",       "Bind_CurrentFieldValue");
        map = map.element("TransientExpression");
        map.attribute("Name",           bind);
        map.attribute("CodeSourceName", "PersonalizationCodeSource");
        if (!entitlement) {
          map.data(true);
          map.value("");
        }
      }
    } else if (version == SandboxInstance.Version.PS4) {
        map.data(true);
        map.value(lookup == null ? "" : String.format("'%s'", lookup));

        map = prm.element("PIMap");
        map.attribute("Name",          "Bind_EntityKey");
        map.attribute("Variable",      "Bind_EntityKey");
        map = map.element("TransientExpression");
        map.attribute("Name",           bind);
        map.data(true);
        map.value("ID");
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalViewAccessorCheckBox
  private static void marshalViewAccessorCheckBox(final XMLOutputNode parent, final String dataSet, final String name)
    throws XMLException {
    
    final XMLOutputNode node = Metadata.marshalInsert(parent, String.format("%sVO", dataSet)).element("ViewAccessor");
    node.attribute("Name",           String.format("LOVVA_For_%s__c", name));
    node.attribute("ViewObjectName", "oracle.adf.businesseditor.model.views.CheckBoxValues");
    node.attribute("RowLevelBinds",  "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalListBindingLookup
  private static void marshalListBindingLookup(final XMLOutputNode parent, final String viewObject, String name)
    throws XMLException {

    name = name + "__c";
    final XMLOutputNode node = Metadata.marshalInsert(parent, String.format("%sVO", viewObject)).element("ListBinding");
    node.attribute("Name",          "LOV_For_"   + name);
    node.attribute("ListVOName",    "LOVVA_For_" + name);
    node.attribute("NullValueFlag", "start");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    XMLOutputNode attr = node.element("AttrArray");
    attr.attribute("Name", "DerivedAttrNames");
    attr.element("Item").attribute("Value", name + "_Id__c");
    attr = node.element("AttrArray");
    attr.attribute("Name", "AttrNames");
    attr.element("Item").attribute("Value", name);
    attr = node.element("AttrArray");
    attr.attribute("Name", "ListAttrNames");
    attr.element("Item").attribute("Value", "Meaning");
    attr.element("Item").attribute("Value", "LookupCode");
    attr = node.element("AttrArray");
    attr.attribute("Name", "ListDisplayAttrNames");
    attr.element("Item").attribute("Value", "Meaning");
    attr = node.element("DisplayCriteria");
    attr.attribute("Name", "LookupsCriteria");
    attr.attribute("Hint", "showAndExecute");
  }

  private static void marshalListBindingCheckBox(final XMLOutputNode parent, final String viewObject, String name)
    throws XMLException {

    name = name + "__c";
    final XMLOutputNode node = Metadata.marshalInsert(parent, String.format("%sVO", viewObject)).element("ListBinding");
    node.attribute("Name",       "LOV_For_" + name);
    node.attribute("ListVOName", "LOVVA_For_" + name);
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    XMLOutputNode attr = node.element("AttrArray");
    attr.attribute("Name", "AttrNames");
    attr.element("Item").attribute("Value", name);
    attr = node.element("AttrArray");
    attr.attribute("Name", "ListAttrNames");
    attr.element("Item").attribute("Value", "value");
    attr = node.element("AttrArray");
    attr.attribute("Name", "ListDisplayAttrNames");
    attr.element("Item").attribute("Value", "name");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalViewOperation
  /**
   ** Marshals the ADF view operation definition.
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
  private static void marshalViewOperation(final Loggable loggable, final File path, final String dataSet)
    throws XMLException {

    final File          file   = new File(path, String.format("%sVOOperations.xml", dataSet));
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8"));
    final XMLOutputNode root   = XMLProcessor.marshal(loggable, file, format).element("PDefOperationDefinitions");
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("Name", String.format("%sVOOperations", dataSet));
    root.attribute("EmptyDoc", "true");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalViewOperation
  /**
   ** Marshals the LOV attribute Groovy expression to bind the Lookups of a
   ** form.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  version            the version indicator of the customization.
   ** @param  viewObject         the name of the <code>ViewObject</code>
   **                            associated with the sandbox.
   ** @param  form               the process form associated with the
   **                            <code>dataSet</code> process form.
   ** @param  account            <code>true</code> if the marshalling belongs to
   **                            the account process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalViewOperation(final Loggable loggable, final File path, final SandboxInstance.Version version, final String viewObject, final FormInfo form, final boolean account)
    throws XMLException {

    uuid.add(UUID.randomUUID().toString());
    final File          file = new File(path, String.format("%sVOOperations.xml.xml", viewObject));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, version.customization, "PDefOperationDefinitions", NAMESPACE);;
    XMLOutputNode       head = Metadata.marshalInsert(root, String.format("(xmlns(mds_ns1=http://xmlns.oracle.com/bc4j))/mds_ns1:PDefOperationDefinitions[@Name='%sVOOperations']", viewObject));
    XMLOutputNode       expr = head.element("ExpressionCodeSource");
    expr.attribute("Name",                       "PersonalizationCodeSource");
    expr.attribute("Type",                       "Personalization");
    expr.attribute("Content-Type",               "application/x-adf-groovy");
    expr.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // it isn't understandable why more than one caching id is part of the
    // marshalled files but we mimic the same behavior to keep the files as
    // comparable as possible
    XMLOutputNode node = expr.element("Caching");
    for (String cursor : uuid) {
      node.element("Key").attribute("Id", cursor);
    }
    // why there always form field size + 1 caching id is part of the marshalled
    // files but we mimic the same behavior to keep the files as comparable as
    // possible
    //node.element("Key").attribute("Id", UUID.randomUUID().toString());
    // Attributes
    for (FormField cursor : form.getFormFields()) {
      if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType())) {
        String        name = String.format("%s__c", cursor.getName());
        node = expr.element("MethodSource");
        node.attribute("Name", String.format("Bind_LookupType_LOVVA_For_%sBindVariable_BoundParameter", name));
        node = node.element("Code");
        node.data(true);
        // not sure if the line breaks in the cdata section are really necessary,
        // but we keep them to keep the files as comparable as possible
        node.value(String.format("@AccessorParameterExpression(accessorName=\"LOVVA_For_%1$s\", name=\"Bind_LookupType\")\ndef Bind_LookupType_LOVVA_For_%1$sBindVariable_BoundParameter() {\n\"\"\"%2$s\"\"\"\n}\n", name, cursor.getProperty("Lookup Code")));
        node = expr.element("MethodSource");
        node.attribute("Name", String.format("Bind_EntityKey_LOVVA_For_%sBindVariable_BoundParameter", name));
        node = node.element("Code");
        node.data(true);
        node.value(String.format("@AccessorParameterExpression(accessorName=\"LOVVA_For_%1$s\", name=\"Bind_EntityKey\")\ndef Bind_EntityKey_LOVVA_For_%1$sBindVariable_BoundParameter() {\nID\n}\n", name));
      }
    }
    if (!account) {
      // looks like the curent field value are marshalled in reverse order
      // usually it should not matters in which sequence the method sources are
      // marshalled but we mimic the same behavior to keep the files as comparable
      // as possible
      for (int i = form.getFormFields().size(); i > 0; i--) {
        final FormField cursor = form.getFormFields().get(i - 1);
        if (Metadata.Attribute.LOOKUP.id.equals(cursor.getType())) {
          String        name = String.format("%s__c", cursor.getName());
          node = expr.element("MethodSource");
          node.attribute("Name", String.format("Bind_CurrentFieldValue_LOVVA_For_%sBindVariable_BoundParameter", name));
          node = node.element("Code");
          node.data(true);
          node.value(String.format("@AccessorParameterExpression(accessorName=\"LOVVA_For_%1$s\", name=\"Bind_CurrentFieldValue\")\ndef Bind_CurrentFieldValue_LOVVA_For_%1$sBindVariable_BoundParameter() {\n%1$s\n}\n", name));
        }
      }
    }
    head = Metadata.marshalModify(root, String.format("(xmlns(mds_ns1=http://xmlns.oracle.com/bc4j))/mds_ns1:PDefOperationDefinitions[@Name='%sVOOperations']", viewObject));
    Metadata.marshalAttribute(head, "EmptyDoc", "false");
    Metadata.marshalAttribute(head, "Version", version.persistence);
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalViewAttribute
  /**
   ** Marshals the attributes for an ADF view attribute customizations.
   **
   ** @param  parent             the {@link XMLOutputNode} being marshalled.
   ** @param  version            the version indicator of the customization.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   ** @param  entityObject       the name of the <code>Entity</code> object
   **                            associated with the sandbox.
   ** @param  viewObject         the name of the <code>View</code> object
   **                            associated with the sandbox.
   ** @param  attribute          the <code>FormField</code> providing the
   **                            properties of the attribute to marshal.
   ** @param  acoount            whether the specified <code>form</code> is the
   **                            <code>Account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalViewAttribute(final XMLOutputNode parent, final SandboxInstance.Version version, final String entityObject, final String viewObject, final FormField attribute, final boolean account)
    throws XMLException {

    String        name = String.format("%s__c", attribute.getName());
    XMLOutputNode root = Metadata.marshalInsert(parent, String.format("%sVO", viewObject));
    XMLOutputNode attr = root.element("ViewAttribute");
    if (Metadata.Attribute.LOOKUP.id.equals(attribute.getType())) {
      attr.attribute("Name",                       name + "_Id__c");
      attr.attribute("EntityUsage",                String.format("%sEO", entityObject));
      attr.attribute("EntityAttrName",             name + "_Id__c");
      attr.attribute("SDOHidden",                  "true");
      attr.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
      attr = attr.element("Properties");
      marshalAttributeProperty(attr, "ExtnCustom", "Y");
      root = Metadata.marshalInsert(parent, String.format("%sVO", viewObject));
      attr = root.element("ViewAttribute");
    }

    attr.attribute("Name",           name);
    attr.attribute("EntityUsage",    String.format("%sEO", entityObject));
    attr.attribute("EntityAttrName", name);
    if (!Metadata.Attribute.LOOKUP.id.equals(attribute.getType())) {
      attr.attribute("AliasName",      name);
    }
    
    if (attribute.getProperties().containsKey("AccountId"))
      attr.attribute("IsUpdateable", "false");

    // a hack to avoid marshalling the persistence attribute for the checkbox
    // attributes
    // usually it should be generated as ("IsPersistent", "false") but the
    // product doesn't hence we do also not
    if (!Metadata.Attribute.CHECKBOX.id.equals(attribute.getType()))
      attr.attribute("IsPersistent", "false");

    if (Metadata.Attribute.LOOKUP.id.equals(attribute.getType())) {
      attr.attribute("LOVName", String.format("LOV_For_%s", name));
    }

    attr.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    attr = attr.element("Properties");
    if (Metadata.Attribute.LOOKUP.id.equals(attribute.getType())) {
      marshalAttributeProperty(attr, "AttributeType", "Picklist");
    }
    marshalAttributeProperty(attr, "AUTOSUBMIT", "true");
    if (Metadata.Attribute.LOOKUP.id.equals(attribute.getType())) {
      marshalAttributeProperty(attr, "CONTROLTYPE", "input_text_lov");
    }
    marshalAttributeProperty(attr, "ExtnCustom", "Y");
    if (Metadata.Attribute.LOOKUP.id.equals(attribute.getType())) {
      marshalAttributeProperty(attr, "IsMultiselectPicklist", "N");
      marshalAttributeProperty(attr, "Searchable",            "true");
      if (!account && version == SandboxInstance.Version.PS3)
        marshalAttributeProperty(attr, "searchablePicklist",    "Y");
    }
  }
}