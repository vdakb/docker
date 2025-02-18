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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ANTFileHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ANTFileHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access changed from java.io.file
                                               to java.net.URL.
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.parser;

import java.net.URL;

import oracle.jdeveloper.workspace.iam.Bundle;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class ANTFileHandler
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** ANT source file Descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class ANTFileHandler extends XMLFileHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ATTRIBUTE_NAME        = "name";
  public static final String ATTRIBUTE_BASEDIR     = "basedir";
  public static final String ATTRIBUTE_DEFAULT     = "default";
  public static final String ATTRIBUTE_DESCRIPTION = "description";

  public static final String ANT_PROJECT           = "ant.project";
  public static final String ANT_BASEDIR           = "ant.basedir";
  public static final String ANT_DEFAULT           = "ant.default";
  public static final String ANT_DESCRIPTION       = "ant.description";

  public static final String ELEMENT_ROOT          = "project";

  public static final String ROOT_PATH             = "//" + ELEMENT_ROOT;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8910844582934906869")
  private static final long  serialVersionUID      = 4866977795876202283L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              name             = null;
  private String              basedir          = null;
  private String              target           = null;
  private String              description      = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>ANTFileHandler</code> instance by converting the
   ** given pathname string into an abstract pathname.
   ** <p>
   ** If the given string is the empty string, then the result is the empty
   ** abstract pathname.
   **
   ** @param  file               the {@link URL} pathname name to the
   **                            {@link ANTFileHandler}.
   **
   ** @throws NullPointerException if the <code>file</code> argument is
   **                              <code>null</code>.
   */
  public ANTFileHandler(final URL file) {
    // ensure inheritance
    super(file.getPath());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>ANTFileHandler</code> instance from a parent
   ** abstract <code>ANTFileHandler</code> and a child pathname string.
   **<p>
   ** If <code>parent</code> is <code>null</code> then the new
   ** <code>ANTFileHandler</code> instance is created as if by invoking the
   ** single-argument <code>ANTFileHandler</code> constructor on the given
   ** <code>child</code> pathname string.
   ** <p>
   ** Otherwise the <code>parent</code> abstract pathname is taken to denote a
   ** directory, and the <code>child</code> pathname string is taken to denote
   ** either a directory or a file. If the <code>child</code> pathname string is
   ** absolute then it is converted into a relative pathname in a
   ** system-dependent way. If <code>parent</code> is the empty abstract
   ** pathname then the new <code>ANTFileHandler</code> instance is created by
   ** converting <code>child</code> into an abstract pathname and resolving the
   ** result against a system-dependent default directory. Otherwise each
   ** pathname string is converted into an abstract pathname and the child
   ** abstract pathname is resolved against the parent.
   **
   ** @param parent              the parent abstract pathname
   ** @param  name               the name of the {@link XMLFileHandler}.
   **
   ** @throws NullPointerException if the <code>name</code> argument is
   **                              <code>null</code>.
   */
  public ANTFileHandler(final ANTFileHandler parent, final String name) {
    // ensure inheritance
    super(parent, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of this descriptor.
   **
   ** @param  name               the name of this descriptor.
   */
  public final void name(final String name) {
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of this descriptor.
   **
   ** @return                    the name of this descriptor.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   basedir
  /**
   ** Sets the basedir of this descriptor.
   **
   ** @param  basedir            the basedir of this descriptor.
   */
  public final void basedir(final String basedir) {
    this.basedir = basedir;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   basedir
  /**
   ** Returns the basedir of this descriptor.
   **
   ** @return                    the basedir of this descriptor.
   */
  public final String basedir() {
    return this.basedir;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Sets the default target of this descriptor.
   **
   ** @param  target             the default target of this descriptor.
   */
  public final void target(final String target) {
    this.target = target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Returns the default target of this descriptor.
   **
   ** @return                    the default target of this descriptor.
   */
  public final String target() {
    return this.target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the description of this descriptor.
   **
   ** @param  description        the description of this descriptor.
   */
  public final void description(final String description) {
    this.description = description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of this descriptor.
   **
   ** @return                    the description of this descriptor.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Load the importable element definitions from the file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public void load()
    throws XMLFileHandlerException {

    // obtain the attributes from the root element to make it possible to handle
    // it properly in any GUI
    load(ROOT_PATH);

    this.name = Reader.attribute(this.rootElement(), ATTRIBUTE_NAME);
    if (StringUtility.empty(this.name))
      throw new IllegalArgumentException(Bundle.format(Bundle.PARSER_ATTRIBUTE_MISSING, ATTRIBUTE_NAME, ELEMENT_ROOT));

    this.basedir = Reader.attribute(this.rootElement(), ATTRIBUTE_BASEDIR);
    if (StringUtility.empty(this.basedir))
      throw new IllegalArgumentException(Bundle.format(Bundle.PARSER_ATTRIBUTE_MISSING, ATTRIBUTE_BASEDIR, ELEMENT_ROOT));

    this.target      = Reader.attribute(this.rootElement(), ATTRIBUTE_DEFAULT);
    this.description = Reader.attribute(this.rootElement(), ATTRIBUTE_DESCRIPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   targetDescription
  /**
   ** Fetchs the xpath specified by <code>name</code> from the document.
   **
   ** @param  name               the mapped attribute name of the xpath to read.
   **
   ** @return                    the property value obtained from the ANT file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public String targetDescription(final String name)
    throws XMLFileHandlerException {

    return elementAttributeValue(name, ATTRIBUTE_DESCRIPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerProperty
  /**
   ** Registers a path translation for a property name.
   ** <p>
   ** We assume that the name of parameter is  the same as the property in the
   ** ANT file.
   **
   ** @param  parameter          the name of the attribute the xpath has to be
   **                            registered for.
   */
  protected void registerProperty(final String parameter) {
    this.registerProperty(parameter, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerProperty
  /**
   ** Registers a path expression for a property name.
   **
   ** @param  parameter          the name of the parameter the xpath has to be
   **                            registered for.
   ** @param  name               the name of the property that represents the
   **                            <code>parameter</code> directive in the ANT
   **                            file.
   */
  protected void registerProperty(final String parameter, final String name) {
    this.register(parameter, propertyPath(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyPath
  /**
   ** Create the xpath expression for a name that represents a ANT property.
   **
   ** @param  name               the name of the property that represents the
   **                            <code>parameter</code> directive in the ANT
   **                            file.
   **
   ** @return                    the xpath expression for a name that represents
   **                            a ANT property.
   */
  protected String propertyPath(final String name) {
    return "//property[@name='" + name + "']";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerPathID
  /**
   ** Registers a path expression for a path id.
   **
   ** @param  parameter          the name of the path id the xpath has to be
   **                            registered for.
   ** @param  name               the name of the path id that represents the
   **                            <code>path</code> directive in the ANT
   **                            file.
   */
  protected void registerPathID(final String parameter, final String name) {
    this.register(parameter, registerPathID(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerPathID
  /**
   ** Create the xpath expression for an id that represents a ANT path.
   **
   ** @param  name               the path of the element that represents the
   **                            <code>path</code> directive in the ANT file.
   **
   ** @return                    the xpath expression for a name that represents
   **                            a ANT path.
   */
  protected String registerPathID(final String name) {
    return "//path[@id='" + name + "']/pathelement";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerTargetName
  /**
   ** Registers a path expression for a target name.
   **
   ** @param  parameter          the name of the parameter the xpath has to be
   **                            registered for.
   ** @param  name               the name of the target that represents the
   **                            <code>target</code> directive in the ANT
   **                            file.
   */
  protected void registerTargetName(final String parameter, final String name) {
    this.register(parameter, registerTargetName(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerTargetName
  /**
   ** Create the xpath expression for a name that represents a ANT target.
   **
   ** @param  name               the path of the element that represents the
   **                            <code>target</code> directive in the ANT file.
   **
   ** @return                    the xpath expression for a name that represents
   **                            a ANT target.
   */
  protected String registerTargetName(final String name) {
    return "//target[@name='" + name + "']";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerTargetDescription
  /**
   ** Registers a path expression for the description of the <code>all</code>
   ** ANT target.
   **
   ** @param  parameter          the name of the parameter the xpath has to be
   **                            registered for.
   ** @param  name               the name of the target that represents the
   **                            <code>target</code> directive in the ANT
   **                            file.
   */
  protected void registerTargetDescription(final String parameter, final String name) {
    this.register(parameter, registerTargetDescription(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerTargetDescription
  /**
   ** Create the xpath expression for the description of the <code>all</code>
   ** ANT target.
   **
   ** @param  name               the path of the element that represents the
   **                            <code>target</code> description directive in
   **                            the ANT file.
   **
   ** @return                    the xpath expression for the description of the
   **                            <code>all</code> ANT target.
   */
  protected String registerTargetDescription(final String name) {
    return "//target[@name='all' and @description='" + name + "']";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers all path translation this instance can handle.
   */
  @Override
  protected void register() {
    // ensure inheritance
    super.register();

    // register local defined substitution placeholder
    registerTargetDescription(ANT_DESCRIPTION, ATTRIBUTE_DESCRIPTION);
  }
}