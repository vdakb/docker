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

    File        :   DynamicModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DynamicModel.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.util.Collection;

import java.io.File;
import java.io.IOException;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.provisioning.vo.FormInfo;

import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.iam.identity.common.spi.SandboxInstance;

////////////////////////////////////////////////////////////////////////////////
// abstract class DynamicModel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The marshaller to spool out the ADF entity objects and view objects for
 ** building definitions at runtime.
 ** <br>
 ** Those descriptors ...
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
public abstract class DynamicModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  PATH = "sessiondef/oracle/iam/ui/runtime/form/model";
  private static final String NAMESPACE = "http://xmlns.oracle.com/bc4j";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Marshals the dynamic ADF entity objects and view objects for building
   ** definitions at runtime.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>account</code> process form.
   **
   ** @throws IOException        if the additonal paths cannot be created.
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshal(final Loggable loggable, final File path, final SandboxInstance instance, final Collection<FormInfo> other)
    throws IOException
    ,      XMLException {

    final Metadata.Cust entity = Metadata.cust(path, "entity").ensureExists();
    final Metadata.Cust assoc  = Metadata.cust(path, "assoc").ensureExists();
    final Metadata.Cust view   = Metadata.cust(path, "view").ensureExists();
    final Metadata.Cust link   = Metadata.cust(path, "link").ensureExists();
    marshalEntity(loggable, entity, instance.version(), instance.dataSet());
    marshalEntity(loggable, entity.path, instance, other);
    marshalView(loggable, view, instance.version(), instance.dataSet());
    marshalView(loggable, view.path, instance, other);
    for (FormInfo cursor : other) {
      final String surrogate = String.format("%s%s", instance.dataSet(), cursor.getName());
      marshalView(loggable, view, instance.version(), surrogate);
      marshalView(loggable, view.path, instance, surrogate);
      marshalEntity(loggable, entity, instance.version(), surrogate);
      marshalEntity(loggable, entity.path, instance, cursor);
      marshalLink(loggable, link, instance, surrogate);
      marshalLink(loggable, link.path, instance, cursor);
      marshalAssociation(loggable, assoc, instance, surrogate);
      marshalAssociation(loggable, assoc.path, instance, cursor);
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
   ** @param  version            the version of the sandbox to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalEntity(final Loggable loggable, final File path, final SandboxInstance.Version version, final String dataSet)
    throws XMLException {

    final String        name = String.format("%sEO", dataSet);
    final File          file = new File(path, String.format("%s.xml", name));
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8"));
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element("Entity");
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("Name", name);
    root.attribute("EmptyDoc", "true");
    if (version == SandboxInstance.Version.PS3) {
      root.attribute("IsPersistent", "true");
      root.attribute("IsUpdatable", "true");
      root.attribute("IsRefPKBased", "false");
      root.attribute("IsLibraryPrivate", "false");
      root.attribute("AssociationAccessorRetained", "false");
      root.attribute("UpdateChangedColumns", "true");
      root.attribute("UseGlueCode", "true");
      root.attribute("HasDataSecurity", "false");
      root.attribute("EffectiveDateType", "None");
    }
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntity
  /**
   ** Marshals the ADF entity customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalEntity(final Loggable loggable, final Metadata.Path path, final SandboxInstance instance, final Collection<FormInfo> other)
    throws XMLException {

    final String        name = String.format("%sEO", instance.dataSet());
    final File          file = new File(path, String.format("%s.xml.xml", name));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, instance.version().customization, "Entity", NAMESPACE);

    XMLOutputNode node = Metadata.marshalInsert(root, name);
    node = node.element("Properties");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Property");
    node.attribute("Name", "USE_ALLOCATION_MANAGER");
    node.attribute("Value", "false");
    // Primary Key definition
    node = Metadata.marshalInsert(root, name);
    node = node.element("Attribute");
    node.attribute("Name", "ID");
    node.attribute("IsPersistent", "false");
    node.attribute("ColumnName", "ID");
    node.attribute("PrimaryKey", "true");
    node.attribute("Type", "java.lang.String");
    node.attribute("ColumnType", "VARCHAR2(255)");
    node.attribute("SQLType", "VARCHAR");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Properties").element("Property");
    node.attribute("Name", "ATTR_INTERNAL_USE");
    node.attribute("Value", "Y");
    // Row Type definition
    node = Metadata.marshalInsert(root, name);
    node = node.element("Attribute");
    node.attribute("Name", "rowType");
    node.attribute("IsPersistent", "false");
    node.attribute("ColumnName", "rowType");
    node.attribute("PrimaryKey", "true");
    node.attribute("Type", "java.lang.String");
    node.attribute("ColumnType", "VARCHAR2(255)");
    node.attribute("SQLType", "VARCHAR");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Properties").element("Property");
    node.attribute("Name", "ATTR_INTERNAL_USE");
    node.attribute("Value", "Y");
    // Request Id definition
    node = Metadata.marshalInsert(root, name);
    node = node.element("Attribute");
    node.attribute("Name", "requestId");
    node.attribute("IsPersistent", "false");
    node.attribute("ColumnName", "requestId");
    node.attribute("PrimaryKey", "true");
    node.attribute("Type", "java.lang.String");
    node.attribute("ColumnType", "VARCHAR2(255)");
    node.attribute("SQLType", "VARCHAR");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Properties").element("Property");
    node.attribute("Name", "ATTR_INTERNAL_USE");
    node.attribute("Value", "Y");
    // Entity Accessors
    for (FormInfo cursor : other) {
      final String accessor = String.format("%s%sAccessor", instance.dataSet(), cursor.getName());
      node = Metadata.marshalInsert(root, name);
      node = node.element("AccessorAttribute");
      node.attribute("Name", accessor);
      node.attribute("Association", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%1$s.assoc.%1$sEOTo%1$s%2$sEOAssoc", instance.dataSet(), cursor.getName()));
      node.attribute("AssociationEnd", accessor);
      node.attribute("Type", "oracle.jbo.RowIterator");
      node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    }

    node = Metadata.marshalModify(root, name);
    Metadata.marshalAttribute(node, "MajorVersion", "1");
    Metadata.marshalAttribute(node, "MinorVersion", "0");
    Metadata.marshalAttribute(node, "EmptyDoc", "false");
    Metadata.marshalAttribute(node, "Version", instance.version().persistence);
    if (instance.version() == SandboxInstance.Version.PS4)
      Metadata.marshalAttribute(node, "publishToBusinessEditor" ,"true");
    Metadata.marshalAttribute(node, "DBObjectName", "OIM_RUNTIME_CUSTOM_ENTITY");
    Metadata.marshalAttribute(node, "DBObjectType", "table");
    Metadata.marshalAttribute(node, "RowClass", "oracle.iam.ui.platform.model.common.OIMResourceEO");
    Metadata.marshalAttribute(node, "AliasName", name);
    Metadata.marshalAttribute(node, "BindingStyle", "OracleName");
    if (instance.version() == SandboxInstance.Version.PS3)
      Metadata.marshalAttribute(node, "UseGlueCode", "false");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntity
  /**
   ** Marshals the ADF entity customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalEntity(final Loggable loggable, final Metadata.Path path, final SandboxInstance instance, final FormInfo other)
    throws XMLException {

    final String        name = String.format("%s%sEO", instance.dataSet(), other.getName());
    final File          file = new File(path, String.format("%s.xml.xml", name));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file,instance.version().customization, "Entity", NAMESPACE);
    XMLOutputNode       node = Metadata.marshalInsert(root, name);
    node = node.element("Properties");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Property");
    node.attribute("Name", "USE_ALLOCATION_MANAGER");
    node.attribute("Value", "false");
    // Primary Key definition
    node = Metadata.marshalInsert(root, name);
    node = node.element("Attribute");
    node.attribute("Name", "ID");
    node.attribute("IsPersistent", "false");
    node.attribute("ColumnName", "ID");
    node.attribute("PrimaryKey", "true");
    node.attribute("Type", "java.lang.String");
    node.attribute("ColumnType", "VARCHAR2(255)");
    node.attribute("SQLType", "VARCHAR");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Properties").element("Property");
    node.attribute("Name", "ATTR_INTERNAL_USE");
    node.attribute("Value", "Y");
    // Row Type definition
    node = Metadata.marshalInsert(root, name);
    node = node.element("Attribute");
    node.attribute("Name", "rowType");
    node.attribute("IsPersistent", "false");
    node.attribute("ColumnName", "rowType");
    node.attribute("PrimaryKey", "true");
    node.attribute("Type", "java.lang.String");
    node.attribute("ColumnType", "VARCHAR2(255)");
    node.attribute("SQLType", "VARCHAR");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Properties").element("Property");
    node.attribute("Name", "ATTR_INTERNAL_USE");
    node.attribute("Value", "Y");
    // Request Id definition
    node = Metadata.marshalInsert(root, name);
    node = node.element("Attribute");
    node.attribute("Name", "requestId");
    node.attribute("IsPersistent", "false");
    node.attribute("ColumnName", "requestId");
    node.attribute("PrimaryKey", "true");
    node.attribute("Type", "java.lang.String");
    node.attribute("ColumnType", "VARCHAR2(255)");
    node.attribute("SQLType", "VARCHAR");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Properties").element("Property");
    node.attribute("Name", "ATTR_INTERNAL_USE");
    node.attribute("Value", "Y");
    // Parent Id definition
    node = Metadata.marshalInsert(root, name);
    node = node.element("Attribute");
    node.attribute("Name", "parentID");
    node.attribute("IsPersistent", "false");
    node.attribute("ColumnName", "parentID");
    node.attribute("PrimaryKey", "true");
    node.attribute("Type", "java.lang.String");
    node.attribute("ColumnType", "VARCHAR2(255)");
    node.attribute("SQLType", "VARCHAR");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("Properties").element("Property");
    node.attribute("Name", "ATTR_INTERNAL_USE");
    node.attribute("Value", "Y");
    // Parent Id definition
    node = Metadata.marshalInsert(root, name);
    node = node.element("Attribute");
    node.attribute("Name", "action");
    node.attribute("IsPersistent", "false");
    node.attribute("ColumnName", "action");
    node.attribute("PrimaryKey", "true");
    node.attribute("Type", "java.lang.String");
    node.attribute("ColumnType", "VARCHAR2(255)");
    node.attribute("SQLType", "VARCHAR");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    XMLOutputNode prop = node.element("Properties");
    node = prop.element("Property");
    node.attribute("Name", "ATTR_INTERNAL_USE");
    node.attribute("Value", "Y");
    node = prop.element("Property");
    node.attribute("Name", "LABEL_ResId");
    node.attribute("Value", String.format("${adfBundle['oracle.adf.businesseditor.model.util.BaseRuntimeResourceBundle']['sessiondef.oracle.iam.ui.runtime.form.model.%s.entity.%s.action_LABEL']}", instance.dataSet(), name));

    node = Metadata.marshalModify(root, name);
    Metadata.marshalAttribute(node, "MajorVersion", "1");
    Metadata.marshalAttribute(node, "MinorVersion", "0");
    Metadata.marshalAttribute(node, "EmptyDoc", "false");
    Metadata.marshalAttribute(node, "Version", instance.version().persistence);
    if (instance.version() == SandboxInstance.Version.PS4)
      Metadata.marshalAttribute(node, "publishToBusinessEditor" ,"true");
    Metadata.marshalAttribute(node, "DBObjectName", "OIM_RUNTIME_CUSTOM_ENTITY");
    Metadata.marshalAttribute(node, "DBObjectType", "table");
    Metadata.marshalAttribute(node, "RowClass", "oracle.iam.ui.platform.model.common.OIMResourceEO");
    Metadata.marshalAttribute(node, "AliasName", name);
    Metadata.marshalAttribute(node, "BindingStyle", "OracleName");
    if (instance.version() == SandboxInstance.Version.PS3)
      Metadata.marshalAttribute(node, "UseGlueCode", "false");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAssociation
  /**
   ** Marshals the ADF entity association definition.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalAssociation(final Loggable loggable, final File path, final SandboxInstance instance, final String surrogate)
    throws XMLException {

    final String        name = String.format("%sEOTo%sEOAssoc", instance.dataSet(), surrogate);
    final File          file = new File(path, String.format("%s.xml", name));
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8"));
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element("Association");
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("Name", name);
    root.attribute("EmptyDoc", "true");
    if (instance.version() == SandboxInstance.Version.PS3)
      root.attribute("IsLibraryPrivate", "false");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAssociation
  /**
   ** Marshals the ADF view link customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalAssociation(final Loggable loggable, final Metadata.Path path, final SandboxInstance instance, final FormInfo other)
    throws XMLException {

    final String master = String.format("%sEO",   instance.dataSet());
    final String detail = String.format("%s%sEO", instance.dataSet(), other.getName());
    final String parent = String.format("(xmlns(mds_ns1=http://xmlns.oracle.com/bc4j))/mds_ns1:Association[@Name='%sTo%sAssoc']", master, detail);

    final File          file = new File(path, String.format("%sTo%sAssoc.xml.xml", master, detail));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, instance.version().customization, "Association", NAMESPACE);

    // Master side
    XMLOutputNode node = Metadata.marshalInsert(root, parent);
    node = node.element("AssociationEnd");
    node.attribute("Name", master);
    node.attribute("Cardinality", "1");
    node.attribute("Source", "true");
    node.attribute("Owner", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%s.entity.%s", instance.dataSet(), master));
    if (instance.version() == SandboxInstance.Version.PS4)
      node.attribute("IsEffectiveDatedDuringJoin", "true");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("AttrArray");
    node.attribute("Name", "Attributes");
    node = node.element("Item").attribute("Value", "ID");
    // Detail side
    node = Metadata.marshalInsert(root, parent);
    node = node.element("AssociationEnd");
    node.attribute("Name", detail);
    node.attribute("Cardinality", "-1");
    node.attribute("Owner", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%s.entity.%s", instance.dataSet(), detail));
    if (instance.version() == SandboxInstance.Version.PS4)
      node.attribute("IsEffectiveDatedDuringJoin", "true");

    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);

    node = node.element("AttrArray");
    node.attribute("Name", "Attributes");
    node = node.element("Item").attribute("Value", "parentID");

    node = Metadata.marshalModify(root, parent);
    Metadata.marshalAttribute(node, "MajorVersion", "1");
    Metadata.marshalAttribute(node, "MinorVersion", "0");
    Metadata.marshalAttribute(node, "EmptyDoc", "false");
    Metadata.marshalAttribute(node, "Version", instance.version().persistence);
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalView
  /**
   ** Marshals the ADF view definition.
   **
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  version            the version of the sandbox to marshal.
   ** @param  dataSet            the name of the <code>RequestDataSet</code>
   **                            associated with the sandbox.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalView(final Loggable loggable, final File path, final SandboxInstance.Version version, final String dataSet)
    throws XMLException {

    final File          file = new File(path, String.format("%sVO.xml", dataSet));
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8"));
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element("ViewObject");
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("Name", String.format("%sVO", dataSet));
    root.attribute("EmptyDoc", "true");
    if (version == SandboxInstance.Version.PS3) {
      root.attribute("Passivate", "NoTransient");
      root.attribute("PageIterMode", "Full");
      root.attribute("ViewLinkAccessorRetained", "false");
      root.attribute("LBThrowOnMisMatch", "false");
      root.attribute("UseGlueCode", "true");
      root.attribute("IsLibraryPrivate", "false");
      root.attribute("SdoGen", "false");
      root.attribute("IsFinderView", "false");
      root.attribute("IsEffectiveDated", "false");
      root.attribute("SelectAllAttributes", "false");
    }
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalView
  /**
   ** Marshals the ADF view customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalView(final Loggable loggable, final Metadata.Path path, final SandboxInstance instance, final Collection<FormInfo> other)
    throws XMLException {

    final String        name = String.format("%sVO", instance.dataSet());
    final String        parent = String.format("(xmlns(mds_ns1=http://xmlns.oracle.com/bc4j))/mds_ns1:ViewObject[@Name='%s']", name);
    final File          file = new File(path, String.format("%s.xml.xml", name));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, instance.version().customization, "ViewObject", NAMESPACE);

    // Entity Usage
    XMLOutputNode node = Metadata.marshalInsert(root, parent);
    node = node.element("EntityUsage");
    node.attribute("Name", String.format("%sEO", instance.dataSet()));
    node.attribute("Entity", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%1$s.entity.%1$sEO", instance.dataSet()));
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // primaryKey definition
    node = Metadata.marshalInsert(root, parent);
    node = node.element("ViewAttribute");
    node.attribute("Name", "ID");
    node.attribute("EntityUsage", String.format("%sEO", instance.dataSet()));
    node.attribute("EntityAttrName", "ID");
    node.attribute("IsPersistent", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // rowType definition
    node = Metadata.marshalInsert(root, parent);
    node = node.element("ViewAttribute");
    node.attribute("Name", "rowType");
    node.attribute("EntityUsage", String.format("%sEO", instance.dataSet()));
    node.attribute("EntityAttrName", "rowType");
    node.attribute("IsPersistent", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // requestId definition
    node = Metadata.marshalInsert(root, parent);
    node = node.element("ViewAttribute");
    node.attribute("Name", "requestId");
    node.attribute("EntityUsage", String.format("%sEO", instance.dataSet()));
    node.attribute("EntityAttrName", "requestId");
    node.attribute("IsPersistent", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // entity accessors
    for (FormInfo cursor : other) {
      final String accessor = String.format("%s%sAccessor", instance.dataSet(), cursor.getName());
      node = Metadata.marshalInsert(root, parent);
      node = node.element("ViewLinkAccessor");
      node.attribute("Name", accessor);
      node.attribute("ViewLink", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%1$s.link.%1$sVOTo%1$s%2$sVOLink", instance.dataSet(), cursor.getName()));
      node.attribute("Type", "oracle.jbo.Row");
      node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    }

    node = Metadata.marshalModify(root, parent);
    Metadata.marshalAttribute(node, "MajorVersion", "1");
    Metadata.marshalAttribute(node, "MinorVersion", "0");
    Metadata.marshalAttribute(node, "EmptyDoc", "false");
    Metadata.marshalAttribute(node, "Version", instance.version().persistence);
    Metadata.marshalAttribute(node, "ComponentClass", "oracle.iam.ui.platform.model.common.OIMResourceVO");
    if (instance.version() == SandboxInstance.Version.PS4)
      Metadata.marshalAttribute(node, "publishToBusinessEditor", "true");
    Metadata.marshalAttribute(node, "BindingStyle", "OracleName");
    Metadata.marshalAttribute(node, "MaxRowsPerNode", "70");
    Metadata.marshalAttribute(node, "MaxActiveNodes", "30");
    Metadata.marshalAttribute(node, "SelectListFlags", "1");
    Metadata.marshalAttribute(node, "FromListFlags", "1");
    Metadata.marshalAttribute(node, "RowClass", "oracle.iam.ui.platform.model.common.OIMResourceVORowImpl");
    if (instance.version() == SandboxInstance.Version.PS3)
      Metadata.marshalAttribute(node, "UseGlueCode", "false");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalView
  /**
   ** Marshals the ADF view customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalView(final Loggable loggable, final Metadata.Path path, final SandboxInstance instance, final String other)
    throws XMLException {

    final String        parent = String.format("(xmlns(mds_ns1=http://xmlns.oracle.com/bc4j))/mds_ns1:ViewObject[@Name='%sVO']", other);
    final File          file = new File(path, String.format("%sVO.xml.xml", other));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, instance.version().customization, "ViewObject", NAMESPACE);

    // Entity Usage
    XMLOutputNode node = Metadata.marshalInsert(root, parent);
    node = node.element("EntityUsage");
    node.attribute("Name", String.format("%sEO", other));
    node.attribute("Entity", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%s.entity.%sEO", instance.dataSet(), other));
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // primaryKey definition
    node = Metadata.marshalInsert(root, parent);
    node = node.element("ViewAttribute");
    node.attribute("Name", "ID");
    node.attribute("EntityUsage", String.format("%sEO", other));
    node.attribute("EntityAttrName", "ID");
    node.attribute("IsPersistent", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // rowType definition
    node = Metadata.marshalInsert(root, parent);
    node = node.element("ViewAttribute");
    node.attribute("Name", "rowType");
    node.attribute("EntityUsage", String.format("%sEO", other));
    node.attribute("EntityAttrName", "rowType");
    node.attribute("IsPersistent", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // requestId definition
    node = Metadata.marshalInsert(root, parent);
    node = node.element("ViewAttribute");
    node.attribute("Name", "requestId");
    node.attribute("EntityUsage", String.format("%sEO", other));
    node.attribute("EntityAttrName", "requestId");
    node.attribute("IsPersistent", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // parentID definition
    node = Metadata.marshalInsert(root, parent);
    node = node.element("ViewAttribute");
    node.attribute("Name", "parentID");
    node.attribute("EntityUsage", String.format("%sEO", other));
    node.attribute("EntityAttrName", "parentID");
    node.attribute("IsPersistent", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    // action definition
    node = Metadata.marshalInsert(root, parent);
    node = node.element("ViewAttribute");
    node.attribute("Name", "action");
    node.attribute("EntityUsage", String.format("%sEO", other));
    node.attribute("EntityAttrName", "action");
    node.attribute("IsPersistent", "false");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);

    node = Metadata.marshalModify(root, parent);
    Metadata.marshalAttribute(node, "MajorVersion", "1");
    Metadata.marshalAttribute(node, "MinorVersion", "0");
    Metadata.marshalAttribute(node, "EmptyDoc", "false");
    Metadata.marshalAttribute(node, "Version", instance.version().persistence);
    Metadata.marshalAttribute(node, "ComponentClass", "oracle.iam.ui.platform.model.common.OIMChildResourceVO");
    if (instance.version() == SandboxInstance.Version.PS4)
      Metadata.marshalAttribute(node, "publishToBusinessEditor" ,"true");
    Metadata.marshalAttribute(node, "BindingStyle", "OracleName");
    Metadata.marshalAttribute(node, "MaxRowsPerNode", "70");
    Metadata.marshalAttribute(node, "MaxActiveNodes", "30");
    Metadata.marshalAttribute(node, "SelectListFlags", "1");
    Metadata.marshalAttribute(node, "FromListFlags", "1");
    Metadata.marshalAttribute(node, "RowClass", "oracle.iam.ui.platform.model.common.OIMResourceVORowImpl");
    if (instance.version() == SandboxInstance.Version.PS3)
      Metadata.marshalAttribute(node, "UseGlueCode", "false");
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalLink
  /**
   ** Marshals the ADF view link association definition.
   **
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalLink(final Loggable loggable, final File path, final SandboxInstance instance, final String surrogate)
    throws XMLException {

    final String        name = String.format("%sVOTo%sVOLink", instance.dataSet(), surrogate);
    final File          file = new File(path, String.format("%s.xml", name));
    final XMLFormat     format = new XMLFormat(String.format(Metadata.PROLOG, "UTF-8"));
    final XMLOutputNode root = XMLProcessor.marshal(loggable, file, format).element("ViewLink");
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    root.attribute("Name", name);
    root.attribute("EmptyDoc", "true");
    if (instance.version() == SandboxInstance.Version.PS3) {
      root.attribute("EntityAssociationReversed", "false");
      root.attribute("IsLibraryPrivate", "false");
    }
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalLink
  /**
   ** Marshals the ADF view link customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  instance           the sandbox to marshal.
   ** @param  other              the subordinated process forms associated with
   **                            the <code>account</code> process form.
   **
   ** @throws XMLException       if the an error occurs.
   */
  private static void marshalLink(final Loggable loggable, final Metadata.Path path, final SandboxInstance instance, final FormInfo other)
    throws XMLException {

    final String        master = String.format("%sVO",   instance.dataSet());
    final String        detail = String.format("%s%sVO", instance.dataSet(), other.getName());
    final File          file = new File(path, String.format("%sTo%sLink.xml.xml", master, detail));
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, instance.version().customization, "ViewLink", NAMESPACE);

    // Master side
    XMLOutputNode node = Metadata.marshalInsert(root, "(xmlns(mds_ns1=http://xmlns.oracle.com/bc4j))/mds_ns1:ViewLink");
    node = node.element("ViewLinkDefEnd");
    node.attribute("Name",        master);
    node.attribute("Cardinality", "1");
    node.attribute("Source",      "true");
    node.attribute("Owner",       String.format("sessiondef.oracle.iam.ui.runtime.form.model.%s.view.%s", instance.dataSet(), master));
    if (instance.version() == SandboxInstance.Version.PS4)
      node.attribute("IsEffectiveDatedDuringJoin", "true");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("AttrArray");
    node.attribute("Name", "Attributes");
    node = node.element("Item").attribute("Value", "ID");
    // Detail side
    node = Metadata.marshalInsert(root, "(xmlns(mds_ns1=http://xmlns.oracle.com/bc4j))/mds_ns1:ViewLink");
    node = node.element("ViewLinkDefEnd");
    node.attribute("Name",        detail);
    node.attribute("Cardinality", "-1");
    node.attribute("Owner",       String.format("sessiondef.oracle.iam.ui.runtime.form.model.%s.view.%s", instance.dataSet(), detail));
    if (instance.version() == SandboxInstance.Version.PS4)
      node.attribute("IsEffectiveDatedDuringJoin", "true");
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    node = node.element("AttrArray");
    node.attribute("Name", "Attributes");
    node = node.element("Item").attribute("Value", "parentID");

    node = Metadata.marshalModify(root, "(xmlns(mds_ns1=http://xmlns.oracle.com/bc4j))/mds_ns1:ViewLink");
    Metadata.marshalAttribute(node, "MajorVersion", "1");
    Metadata.marshalAttribute(node, "MinorVersion", "0");
    Metadata.marshalAttribute(node, "EmptyDoc", "false");
    Metadata.marshalAttribute(node, "Version", instance.version().persistence);
    Metadata.marshalAttribute(node, "EntityAssociation", String.format("sessiondef.oracle.iam.ui.runtime.form.model.%1$s.assoc.%1$sEOTo%1$s%2$sEOAssoc", instance.dataSet(), other.getName()));
    root.close();
  }
}