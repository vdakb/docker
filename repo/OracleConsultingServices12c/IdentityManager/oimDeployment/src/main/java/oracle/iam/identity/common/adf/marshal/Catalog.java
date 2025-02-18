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

    File        :   Catalog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Catalog.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.util.Map;
import java.util.LinkedHashMap;

import java.io.File;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLOutputNode;
import javax.print.attribute.standard.Severity;

////////////////////////////////////////////////////////////////////////////////
// abstract class Catalog
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** The marshaller to spool out the ADF application module for building
 ** definitions at runtime.
 ** <br>
 ** Those descriptors includes the bindings for a certain view usages and the
 ** customization the taskflow binding of those usages uploaded to MDS
 ** later on by publishing the <code>Sandbox</code>.
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
public class Catalog {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String NAME           = "CatalogAM";
  public static final String PATH           = "/persdef/oracle/iam/ui/catalog/model/am/mdssys/cust/site/site/";
  public static final String FILE           = NAME + ".xml";
  public static final String NAMESPACE      = "http://xmlns.oracle.com/bc4j";

  public static final String CUST           = FILE + ".xml";
  public static final String CUST_URI       = "motype_nsuri";
  public static final String CUST_NAME      = "motype_local_name";
  public static final String CUST_VERSION   = "version";

  public static final String VIEW_USAGE     = "ViewUsage";
  public static final String LINK_USAGE     = "ViewLinkUsage";

  public static final String USAGE_NAME     = "Name";
  public static final String USAGE_VIEW     = "ViewObjectName";
  public static final String USAGE_LINK     = "ViewLinkObjectName";
  public static final String USAGE_LINK_DST = "DstViewUsageName";
  public static final String USAGE_LINK_SRC = "SrcViewUsageName";

  public static final String MODULE_NAME    = "StaticDef";
  public static final String MODULE_EMPTY   = "EmptyDoc";
  public static final String MODULE_MINOR   = "MinorVersion";
  public static final String MODULE_MAJOR   = "Version";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String            uri;
  private String            name;
  private String            version;

  private Module            module;
  private Map<String, View> usage;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class View
  // ~~~~~ ~~~~
  /**
   ** Helper class, holds configuration data about <code>View Usage</code>s and
   ** <code>View Link Usage</code>s.
   */
  public static abstract class View {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Type   type;
    final String name;
    final String object;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Type
    // ~~~~~ ~~~~
    /**
     ** Helper class, holds type information about <code>View Usage</code>s and
     ** <code>View Link Usage</code>s.
     */
    public static enum Type {
      USAGE, LINK;
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Usage
    // ~~~~~ ~~~~~
    /**
     ** Helper class, holds <code>View Usage</code>s.
     */
    public static class Usage extends View {

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a {@link View} <code>Usage</code> iew object that allows
       ** use as a JavaBean.
       **
       ** @param  name           the name of the view.
       ** @param  object         the object of the view.
       */
      protected Usage(final String name, final String object) {
        // ensure inheritance
        super(Type.USAGE, name, object);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Link
    // ~~~~~ ~~~~
    /**
     ** Helper class, holds <code>View Link</code>s.
     */
    public static class Link extends View {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final String source;
      final String target;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a {@link View} <code>Usage</code> iew object that allows
       ** use as a JavaBean.
       **
       ** @param  name           the name of the link.
       ** @param  object         the object of the link.
       ** @param  source         the source of the link.
       ** @param  target         the target of the link.
       */
      protected Link(final String name, final String object, final String source, final String target) {
        // ensure inheritance
        super(Type.LINK, name, object);

        // initialize instance attributes
        this.source = source;
        this.target = target;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Abstract</code> iew object that allows use as a
     ** JavaBean.
     **
     ** @param  type             the type of the view or link.
     ** @param  name             the name of the view or link.
     ** @param  object           the object of the view or link.
     */
    protected View(final Type type, final String name, final String object) {
      // ensure inheritance
      super();

      // initialize intance attributes
      this.type   = type;
      this.name   = name;
      this.object = object;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Module
  // ~~~~~ ~~~~~~
  /**
   ** Helper class, holds <code>Application Module</code>.
   */
  public static class Module {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private boolean empty;
    private String  name;
    private String  major;
    private int     minor;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Module</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Module() {
      // ensure inheritance
      super();
    }

    /**
     ** Constructs a <code>Module</code> type that allows use as a JavaBean.
     ** <br>
     ** @param  name         the value of the StaticDef property of the CatalogAM.
     ** @param  major        the major version of the CatalogAM.
     ** @param  minor        the minor version of the CatalogAM.
     ** @param  empty        the value of the EmptyDoc property of the CatalogAM.
     */
    public Module(final String name, final String major, final int minor, final boolean empty) {
      this.name = name;
      this.major = major;
      this.minor = minor;
      this.empty = empty;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: empty
    /**
     ** Called to inject the argument for parameter <code>empty</code>.
     **
     ** @param  empty            <code>true</code> if empty documents are
     **                          allowed; otherwise <code>false</code>.
     */
    public void empty(final boolean empty) {
      this.empty = empty;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: empty
    /**
     ** Returns <code>true</code> if empty documents are allowed in the
     ** application module definition.
     **
     ** @return                  <code>true</code> if empty documents are
     **                          allowed; otherwise <code>false</code>.
     */
    public final boolean empty() {
      return this.empty;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Called to inject the <code>name</code> of the <code>Catalog</code>
     ** application module related to Identity Manager.
     **
     ** @param  name             the name of the <code>Catalog</code>
     **                          application module related to Identity Manager.
     */
    public void name(final String name) {
      this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the <code>name</code> of the <code>Catalog</code> application
     ** module related to Identity Manager.
     **
     ** @return                  the name of the <code>Catalog</code>
     **                          application module related to Identity Manager.
     */
    public String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: major
    /**
     ** Called to inject the <code>major</code> version of the
     ** <code>Catalog</code> application module related to Identity Manager.
     **
     ** @param  major            the major version of the <code>Catalog</code>
     **                          application module related to Identity Manager.
     */
    public void major(final String major) {
      this.major = major;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: major
    /**
     ** Returns the <code>major</code> version of the <code>Catalog</code>
     ** application module related to Identity Manager.
     **
     ** @return                  the major version of the <code>Catalog</code>
     **                          application module related to Identity Manager.
     */
    public String major() {
      return this.major;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minor
    /**
     ** Called to inject the <code>minor</code> version of the
     ** <code>Catalog</code> application module related to Identity Manager.
     **
     ** @param  minor            the minor version of the <code>Catalog</code>
     **                          application module related to Identity Manager.
     */
    public void minor(final int minor) {
      this.minor = minor;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minor
    /**
     ** Returns the <code>minor</code> version of the <code>Catalog</code>
     ** application module related to Identity Manager.
     **
     ** @return                  the minor version of the <code>Catalog</code>
     **                          application module related to Identity Manager.
     */
    public int minor() {
      return this.minor;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Catalog</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Catalog() {
    // ensure inheritance
    super();
  }

  /**
   ** Constructs a <code>Catalog</code> type that allows use as a JavaBean.
   ** <br>
   ** @param  name         the value of the StaticDef property of the CatalogAM.
   ** @param  major        the major version of the CatalogAM.
   ** @param  minor        the minor version of the CatalogAM.
   ** @param  empty        the value of the EmptyDoc property of the CatalogAM.
   */
  public Catalog(final String name, final String major, final int minor, final boolean empty) {
    module = new Module(name, major, minor, empty);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Called to inject the <code>name</code> of the <code>Catalog</code>
   ** application module related to Identity Manager.
   **
   ** @param  name               the name of the <code>Catalog</code>
   **                            application module related to Identity Manager.
   */
  public void name(final String name) {
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the <code>name</code> of the <code>Catalog</code> application
   ** module related to Identity Manager.
   **
   ** @return                    the name of the <code>Catalog</code>
   **                            application module related to Identity Manager.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Called to inject the <code>version</code> property of the
   ** <code>Catalog</code> application module customization related to
   ** Identity Manager.
   **
   ** @param  version            the <code>version</code> property of the
   **                            <code>Catalog</code> application module
   **                            customization related to Identity Manager.
   */
  public void version(final String version) {
    this.version = version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the <code>version</code> version of the <code>Catalog</code>
   ** application module customization related to Identity Manager.
   **
   ** @return                    the <code>version</code> property of the
   **                            <code>Catalog</code> application module
   **                            customization related to Identity Manager.
   */
  public String version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uri
  /**
   ** Called to inject the <code>uri</code> of the <code>Catalog</code>
   ** application module related to Identity Manager.
   **
   ** @param  uri                the uri of the <code>Catalog</code>
   **                            application module related to Identity Manager.
   */
  public void uri(final String uri) {
    this.uri = uri;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uri
  /**
   ** Returns the <code>uri</code> of the <code>Catalog</code> application
   ** module related to Identity Manager.
   **
   ** @return                    the uri of the <code>Catalog</code>
   **                            application module related to Identity Manager.
   */
  public String uri() {
    return this.uri;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   module
  /**
   ** Returns the value of the <code>definition</code> property.
   ** <p>
   ** This accessor method returns a reference to the live instance, not a
   ** snapshot. Therefore any modification you make to the returned set will
   ** be present inside the object.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    definition().major("12.2.1.3");
   **    definition().minor(1);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list {@link Module}.
   **
   ** @param  module           the live instance of the <code>Module</code>
   **                          to assign.
   */
  public void module(final Module module) {
    this.module = module;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   module
  /**
   ** Returns the <code>Catalog</code> <code>Application Module</code> related
   ** to Identity Manager.
   **
   ** @return                    the <code>Catalog</code>
   **                            <code>Application Module</code> related to
   **                            Identity Manager.
   */
  public Module module() {
    return this.module;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: usage
  /**
   ** Called to inject the <code>mapping</code> of the <code>Catalog</code>
   ** view usage related to Identity Manager.
   **
   ** @param  dataSet          the name of the <code>Catalog</code> view
   **                          usage related to Identity Manager.
   **
   ** @return                  the <code>Catalog</code> for method chaining
   **                          purpose.
   */
  public Catalog usage(final String dataSet) {
    // <ViewUsage Name="IDSRequestVO" ViewObjectName="sessiondef.oracle.iam.ui.runtime.form.model.IDSRequest.view.IDSRequestVO" xmlns="http://xmlns.oracle.com/bc4j"/>
    return usage(
      new View.Usage(
        String.format("%1$sVO", dataSet)
      , String.format("sessiondef.oracle.iam.ui.runtime.form.model.%1$s.view.%1$sVO", dataSet)
      )
    );
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: usage
  /**
   ** Called to inject the <code>mapping</code> of the <code>Catalog</code>
   ** view usage related to Identity Manager.
   **
   ** @param  dataSet          the name of the <code>Catalog</code> view
   **                          usage related to Identity Manager.
   ** @param  viewObject       the name of the <code>Catalog</code> view
   **                          object related to Identity Manager.
   **
   ** @return                  the <code>Catalog</code> for method chaining
   **                          purpose.
   */
  public Catalog usage(final String dataSet, final String viewObject) {
    // <ViewUsage Name="IDSRequestUD_IDS_GRPVO" ViewObjectName="sessiondef.oracle.iam.ui.runtime.form.model.IDSRequest.view.IDSRequestUD_IDS_GRPVO" xmlns="http://xmlns.oracle.com/bc4j"/>
    return usage(
      new View.Usage(
        String.format("%1$s%2$sVO", dataSet, viewObject)
      , String.format("sessiondef.oracle.iam.ui.runtime.form.model.%1$s.view.%1$s%2$sVO", dataSet, viewObject)
      )
    );
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: usage
  /**
   ** Called to inject the <code>mapping</code> of the <code>Catalog</code>
   ** view usage related to Identity Manager.
   **
   ** @param  view             the name of the <code>Catalog</code> view
   **                          usage related to Identity Manager.
   **
   ** @return                  the <code>Catalog</code> for method chaining
   **                          purpose.
   */
  public Catalog usage(final View.Usage view) {
    if (this.usage == null)
      this.usage = new LinkedHashMap<String, View>();

    this.usage.put(view.name, view);
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: link
  /**
   ** Called to inject the <code>mapping</code> of the <code>Catalog</code>
   ** view usage related to Identity Manager.
   **
   ** @param  dataSet          the name of the <code>Catalog</code> view
   **                          link usage related to Identity Manager.
   ** @param  viewObject       the name of the <code>Catalog</code> view
   **                          link usage object related to Identity Manager.
   **
   ** @return                  the <code>Catalog</code> for method chaining
   **                          purpose.
   */
  public Catalog link(final String dataSet, final String viewObject) {
    // <ViewLinkUsage Name="IDSRequestVOToIDSRequestUD_IDS_GRPVOLink" ViewLinkObjectName="sessiondef.oracle.iam.ui.runtime.form.model.IDSRequest.link.IDSRequestVOToIDSRequestUD_IDS_GRPVOLink" SrcViewUsageName="IDSRequestVO" DstViewUsageName="IDSRequestUD_IDS_GRPVO" xmlns="http://xmlns.oracle.com/bc4j"/>
    return link(new View.Link(
      String.format("%1$sVOTo%1$s%2$sVOLink", dataSet, viewObject)
    , String.format("sessiondef.oracle.iam.ui.runtime.form.model.%1$s.link.%1$sVOTo%1$s%2$sVOLink", dataSet, viewObject)
    , dataSet + "VO"
    , dataSet + viewObject + "VO")
    );
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: link
  /**
   ** Called to inject the <code>mapping</code> of the <code>Catalog</code>
   ** view usage related to Identity Manager.
   **
   ** @param  view             the <code>Catalog</code> view link usage related
   **                          to Identity Manager.
   **
   ** @return                  the <code>Catalog</code> for method chaining
   **                          purpose.
   */
  public Catalog link(final View.Link view) {
    if (this.usage == null)
      this.usage = new LinkedHashMap<String, View>();

    this.usage.put(view.name, view);
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: usage
  /**
   ** Returns the value of the <code>usage</code> property.
   ** <p>
   ** This accessor method returns a reference to the live mapping, not a
   ** snapshot. Therefore any modification you make to the returned set will
   ** be present inside the object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the flag
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    usage().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list {@link String}.
   **
   ** @return                  the live list of the assigned
   **                          <code>Usage</code>s.
   */
  public Map<String, View> usage() {
    if (this.usage == null)
      this.usage = new LinkedHashMap<String, View>();

    return this.usage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalDefinitions
  /**
   ** Marshals the view objects of an application module customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the files within.
   ** @param  version            the version indicator of the customization.
   ** @param  catalog            the catalog application module.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalDefinitions(final Loggable loggable, final Metadata.Path path, final String version, final Catalog catalog)
    throws XMLException {

    final File          file = new File(path, "adfmcatalog.xml.xml");
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, version, "Definitions", "http://xmlns.oracle.com/adfmcatalog");
    for (Catalog.View cursor : catalog.usage.values()) {
      if (cursor.type == Catalog.View.Type.LINK)
        continue;

      XMLOutputNode node = Metadata.marshalInsert(root, "(xmlns(mds_ns1=http://xmlns.oracle.com/adfmcatalog))/mds_ns1:Definitions");
      node = node.element("ViewObject");
      node.attribute("fullName", cursor.object);
      node.attribute("publishToBusinessEditor", "true");
      node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, "http://xmlns.oracle.com/adfmcatalog");
    }
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalModule
  /**
   ** Marshals the view object usages of an application module customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the files within.
   ** @param  version            the version indicator of the customization.
   ** @param  catalog            the catalog application module.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshalModule(final Loggable loggable, final Metadata.Path path, final String version, final Catalog catalog)
    throws XMLException {

    final File          file = new File(path, CUST);
    final XMLOutputNode root = Metadata.marshalCustomization(loggable, file, version, catalog.name, NAMESPACE);
    for (View cursor : catalog.usage.values()) {
      if (cursor.type == View.Type.USAGE) {
        final View.Usage    view = (View.Usage)cursor;
        final XMLOutputNode node = Metadata.marshalInsert(root, NAME).element(VIEW_USAGE);
        node.attribute(USAGE_NAME,                   view.name);
        node.attribute(USAGE_VIEW,                   view.object);
        node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
      }
      else if (cursor.type == View.Type.LINK) {
        final View.Link     view = (View.Link)cursor;
        final XMLOutputNode node = Metadata.marshalInsert(root, NAME).element(LINK_USAGE);
        node.attribute(USAGE_NAME,                   view.name);
        node.attribute(USAGE_LINK,                   view.object);
        node.attribute(USAGE_LINK_SRC,               view.source);
        node.attribute(USAGE_LINK_DST,               view.target);
        node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
      }
    }

    // increment the minor version in memory before the module metadata are
    // persisted
    catalog.module.minor++;
    final XMLOutputNode node = Metadata.marshalModify(root, NAME);
    Metadata.marshalAttribute(node, Catalog.MODULE_NAME,  catalog.module.name);
    Metadata.marshalAttribute(node, Catalog.MODULE_MINOR, String.valueOf(catalog.module.minor));
    Metadata.marshalAttribute(node, Catalog.MODULE_EMPTY, String.valueOf(catalog.module.empty));
    Metadata.marshalAttribute(node, Catalog.MODULE_MAJOR, String.valueOf(catalog.module.major));
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  /**
   ** Reset the catalog module to its initiale state.
   ** <p>
   ** An initalized application module looks like
   ** <pre>
   **   &lt;?xml version='1.0' encoding='ISO-8859-1'?&gt;
   **   &lt;PDefApplicationModule xmlns="http://xmlns.oracle.com/bc4j" Name="CatalogAM"/&gt;
   ** </pre>
   */
  public void reset() {
    if (this.usage != null)
      this.usage.clear();
  }
}