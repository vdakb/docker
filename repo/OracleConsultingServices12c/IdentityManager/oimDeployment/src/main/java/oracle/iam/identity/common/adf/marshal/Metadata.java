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

import java.io.File;
import java.io.IOException;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;
import oracle.hst.foundation.xml.XMLCodecQuote;

////////////////////////////////////////////////////////////////////////////////
// abstract class Metadata
// ~~~~~~~~ ~~~~~ ~~~~~~~~
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
public abstract class Metadata {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  PROLOG    = "<?xml version='1.0' encoding='%s'?>";

  private static final String CUST      = "mdssys/cust/site/site";

  private static final String NAMESPACE = "http://xmlns.oracle.com/mds";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** Helper class to convert Identity Mananeger form attribute types into
   ** ADF compliant type definitions.
   */
  enum Attribute {
      TEXT("TextField",                 "Text",     "VARCHAR2(255)", "VARCHAR", "java.lang.String")
    , DATE("DateFieldDlg",              "Date",     "DATE",          "DATE",    "oracle.jbo.domain.Date")
    , NUMBER("TextField",               "Number",   "NUMBER",        "NUMERIC", "oracle.jbo.domain.Number")
    , LOOKUP("LookupField",             "Text",     "VARCHAR2(255)", "VARCHAR", "java.lang.String")
    , ENDPOINT("ITResourceLookupField", "Number",   "NUMBER",        "NUMERIC", "oracle.jbo.domain.Number")
    , READONLY("DOField",               "Text",     "VARCHAR2(255)", "VARCHAR", "java.lang.String")
    , PASSWORD("PasswordField",         "Text",     "VARCHAR2(255)", "VARCHAR", "java.lang.String")
    , CHECKBOX("CheckBox",              "Checkbox", "NUMBER",        "BIT",     "java.lang.Boolean")
    , TEXTAREA("TextArea",              "Text",     "VARCHAR2(255)", "VARCHAR", "java.lang.String")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /* all of the below stay as packge protected */
    String id;
    String clazz;
    String domain;
    String ansi;
    String type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Attribute(final String id, final String type, final String domain, final String ansi, final String clazz) {
      this.id = id;
      this.clazz = clazz;
      this.domain = domain;
      this.ansi = ansi;
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper type from the given string
     ** value.
     **
     ** @param  value            the string value the type should be returned
     **                          for.
     **
     ** @return                  the type property.
     */
    public static Attribute from(final String value) {
      for (Attribute cursor : Attribute.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Path
  // ~~~~~ ~~~~~
  /**
   ** Helper class, holds pathes and the nested customization pathes.
   */
  public static class Path extends File {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-7632544312616712541")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new <code>Path</code> instance by converting the given path
     ** name string into an abstract pathname. If the given string is the empty
     ** string, then the result is the empty abstract pathname.
     **
     ** @param  path             a pathname string
     */
    Path(final String base) {
      // ensure inheritance
      this(null, base);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new <code>Path</code> instance from a base abstract pathname
     ** and a path string.
     ** <p>
     ** If <code>base</code> is <code>null</code> then the new <code>Path</code>
     ** instance is created as like invoking the single-argument
     ** <code>Path</code> constructor on the given <code>path</code> name
     ** string.
     ** <p>
     ** Otherwise the <code>base</code> abstract pathname is taken to denote a
     ** directory, and the <code>path</code> name string is taken to denote
     ** either a directory or a file. If the <code>path</code> name string is
     ** absolute then it is converted into a relative pathname in a
     ** system-dependent way. If <code>base</code> is the empty abstract
     ** pathname then the new <code>Path</code> instance is created by
     ** converting <code>path</code> into an abstract pathname and resolving the
     ** result against a system-dependent default directory. Otherwise each
     ** <code>path</code> name string is converted into an abstract pathname and
     ** the child abstract pathname is resolved against the base.
     **
     ** @param  base             the parent abstract pathname.
     ** @param  path             the child pathname string.
     **
     */
    Path(final File base, final String path) {
      // ensure inheritance
      super(base, path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: ensureExists
    /**
     ** Verify if the path where we want to operate exists.
     ** <br>
     ** If the abstract path is not existing it will be created.
     **
     ** @return                  the <code>Path</code> for method chaining
     **                          purpose.
     **
     ** @throws IOException      if the abstract path could not be verified or
     **                          created.
     */
    public Path ensureExists()
      throws IOException {

      if (!exists())
        mkdirs();
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Cust
  // ~~~~~ ~~~~
  /**
   ** Helper class, holds pathes and the nested customization pathes.
   */
  public static class Cust extends Path {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-4428509970640752765")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the nested abstract pathname for the customizations */
    final Path path;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new <code>Cust</code> instance from a base abstract pathname
     ** and a path string.
     ** <p>
     ** If <code>base</code> is <code>null</code> then the new <code>Cust</code>
     ** instance is created as like invoking the single-argument
     ** <code>Cust</code> constructor on the given <code>path</code> name
     ** string.
     ** <p>
     ** Otherwise the <code>base</code> abstract pathname is taken to denote a
     ** directory, and the <code>path</code> name string is taken to denote
     ** either a directory or a file. If the <code>path</code> name string is
     ** absolute then it is converted into a relative pathname in a
     ** system-dependent way. If <code>base</code> is the empty abstract
     ** pathname then the new <code>Path</code> instance is created by
     ** converting <code>path</code> into an abstract pathname and resolving the
     ** result against a system-dependent default directory. Otherwise each
     ** <code>path</code> name string is converted into an abstract pathname and
     ** the child abstract pathname is resolved against the base.
     **
     ** @param  base             the parent abstract pathname.
     ** @param  path             the child pathname string.
     **
     */
    Cust(final File base, final String path) {
      // ensure inheritance
      super(base, path);

      this.path = Metadata.path(this, CUST);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   path
    /**
     ** Returns the customization path extension.
     **
     ** @return                  the customization path extension.
     */
    public Path path() {
      return this.path;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: ensureExists (overridden)
    /**
     ** Verify if the path where we want to operate exists.
     ** <br>
     ** If the abstract path is not existing it will be created.
     **
     ** @return                  the <code>Cust</code> for method chaining
     **                          purpose.
     **
     ** @throws IOException      if the abstract path could not be verified or
     **                          created.
     */
    @Override
    public Cust ensureExists()
      throws IOException {

      // ensure inheritance
      super.ensureExists();
      if (!path.exists())
        path.mkdirs();
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Creates a new <code>Path</code> instance by converting the given path name
   ** string into an abstract pathname. If the given string is the empty string,
   ** then the result is the empty abstract pathname.
   **
   ** @param  path               a pathname string.
   **
   ** @return                    the created {@link Path}.
   */
  public static Path path(final String path) {
    return new Path(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Creates a new {@link Path} instance from a base abstract pathname and a
   ** child pathname string.
   ** <p>
   ** If <code>base</code> is <code>null</code> then the new {@link Path}
   ** instance is created as like invoking the single-argument {@link Path}
   ** constructor on the given <code>path</code> name string.
   ** <p>
   ** Otherwise the <code>base</code> abstract pathname is taken to denote a
   ** directory, and the <code>path</code> name string is taken to denote either
   ** a directory or a file. If the <code>path</code> name string is absolute
   ** then it is converted into a relative pathname in a system-dependent way.
   ** If <code>base</code> is the empty abstract pathname then the new
   ** {@link File} instance is created by converting <code>path</code> into an
   ** abstract pathname and resolving the result against a system-dependent
   ** default directory. Otherwise each pathname string is converted into an
   ** abstract pathname and the child abstract pathname is resolved against the
   ** base.
   **
   ** @param  base               the parent abstract pathname.
   ** @param  path               the child pathname string.
   **
   ** @return                    the created {@link Path}.
   */
  public static Path path(final File base, final String path) {
    return new Path(base, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cust
  /**
   ** Creates a new {@link Cust} instance from a base abstract pathname and a
   ** child pathname string.
   ** <p>
   ** If <code>base</code> is <code>null</code> then the new {@link Cust}
   ** instance is created as like invoking the single-argument {@link Cust}
   ** constructor on the given <code>path</code> name string.
   ** <p>
   ** Otherwise the <code>base</code> abstract pathname is taken to denote a
   ** directory, and the <code>path</code> name string is taken to denote either
   ** a directory or a file. If the <code>path</code> name string is absolute
   ** then it is converted into a relative pathname in a system-dependent way.
   ** If <code>base</code> is the empty abstract pathname then the new
   ** {@link File} instance is created by converting <code>path</code> into an
   ** abstract pathname and resolving the result against a system-dependent
   ** default directory. Otherwise each pathname string is converted into an
   ** abstract pathname and the child abstract pathname is resolved against the
   ** base.
   **
   ** @param  base               the parent abstract pathname.
   ** @param  path               the child pathname string.
   **
   ** @return                    the created {@link Cust}.
   */
  public static Cust cust(final File base, final String path) {
    return new Cust(base, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalSandbox
  /**
   ** Marshals the sandbox metadata descriptor.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  file               the abstrcat pathname to create the file.
   ** @param  localName          the name of the customiztion.
   ** @param  nameSpace          the name space the customization belongs to.
   ** @param  version            the verison indicator of the sandbox.
   **
   ** @return                    the prepared {@link XMLOutputNode} to marshal
   **                            further.
   **
   ** @throws XMLException       if the an error occurs.
   */
  static XMLOutputNode marshalSandbox(final Loggable loggable, final File file, final String localName, final String nameSpace, final String version)
    throws XMLException {

    final XMLFormat     format = new XMLFormat(String.format(PROLOG, "UTF-8")).codec(XMLCodecQuote.instance());
    final XMLOutputNode node = XMLProcessor.marshal(loggable, file, format).element(localName);
    node.attribute(XMLProcessor.ATTRIBUTE_XMLNS, nameSpace);
    node.attribute("version",                    version);
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalCustomization
  /**
   ** Marshals the sandbox customization.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  file               the abstrcat pathname to create the file.
   ** @param  version            the version indicator of the customization.
   ** @param  localName          the name of the customiztion.
   ** @param  nameSpace          the name space the customization belongs to.
   **
   ** @return                    the prepared {@link XMLOutputNode} to marshal
   **                            further.
   **
   ** @throws XMLException       if the an error occurs.
   */
  static XMLOutputNode marshalCustomization(final Loggable loggable, final File file, final String version, final String localName, final String nameSpace)
    throws XMLException {

    final XMLFormat     format = new XMLFormat(String.format(PROLOG, "UTF-8")).codec(XMLCodecQuote.instance());
    final XMLOutputNode node = XMLProcessor.marshal(loggable, file, format).element("mds:customization");
    node.attribute("version",           version);
    node.attribute("xmlns:mds",         NAMESPACE);
    node.attribute("motype_local_name", localName);
    node.attribute("motype_nsuri",      nameSpace);
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalInsert
  /**
   ** Marshals the sandbox customization insert operation.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  id                 the identifier of the request data set.
   **
   ** @return                    the {@link XMLOutputNode} containing the XML
   **                            fragments for MDS insert operation.
   **
   ** @throws XMLException       if the an error occurs.
   */
  static XMLOutputNode marshalInsert(final XMLOutputNode parent, final String id)
    throws XMLException {

    final XMLOutputNode node = parent.element("mds:insert");
    node.attribute("parent",    id);
    node.attribute("position", "last");
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalModify
  /**
   ** Marshals the sandbox customization modify operation.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  id                 the identifier of the element to modify.
   **
   ** @return                    the {@link XMLOutputNode} containing the XML
   **                            fragments for MDS modify operation.
   **
   ** @throws XMLException       if the an error occurs.
   */
  static XMLOutputNode marshalModify(final XMLOutputNode parent, final String id)
    throws XMLException {

    final XMLOutputNode node = parent.element("mds:modify");
    node.attribute("element", id);
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAttribute
  /**
   ** Marshals the sandbox customization attribute.
   **
   ** @param  parent             the {@link XMLOutputNode} to marshal.
   ** @param  name               the name of the property.
   ** @param  value              the value of the property.
   **
   ** @throws XMLException       if the an error occurs.
   */
  static void marshalAttribute(final XMLOutputNode parent, final String name, final String value)
    throws XMLException {

    XMLOutputNode node = parent.element("mds:attribute");
    node.attribute("name", name);
    node.attribute("value", value);
  }
}