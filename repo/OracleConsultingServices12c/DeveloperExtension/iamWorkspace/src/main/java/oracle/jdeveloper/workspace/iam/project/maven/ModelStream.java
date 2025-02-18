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

    Copyright © 2021. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ModelStream.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ModelStream.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ComboBoxModel;

import oracle.jdeveloper.workspace.iam.model.Property;
import oracle.jdeveloper.workspace.iam.model.AbstractProperty;

import oracle.jdeveloper.workspace.iam.swing.widget.FileParameter;
import oracle.jdeveloper.workspace.iam.swing.widget.ListParameter;
import oracle.jdeveloper.workspace.iam.swing.widget.StringParameter;

////////////////////////////////////////////////////////////////////////////////
// class ModelStream
// ~~~~~ ~~~~~~~~~~~
/**
 ** Utility used to maintain our custom Maven Project Object Model build file
 ** hierarchy.
 ** <p>
 ** The <code>pom.xml</code> file is the core of a project's configuration in
 ** Maven. It is a single configuration file that contains the majority of
 ** information required to build a project in just the way you want.
 ** <br>
 ** The POM is huge and can be daunting in its complexity, but it is not
 ** necessary to understand all of the intricacies.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class ModelStream extends ModelData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2915231691379386895")
  private static final long serialVersionUID = 1713789133338749332L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient Map<String, ModelData> include  = new HashMap<String, ModelData>();
  private transient List<AbstractProperty> property = new ArrayList<AbstractProperty>();
  private transient List<AbstractProperty> distance = new ArrayList<AbstractProperty>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ModelStream</code> with the specified project name and
   ** base dir.
   **
   ** @param  folder             the abstract pathname the folder where the
   **                            build file should be exist and/or will be
   **                            created.
   ** @param  name               the name of the file
   */
  public ModelStream(final ModelFolder folder, final String name) {
    // ensure inheritance
    super(folder, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ModelStream</code> with the specified file name and
   ** extension in the specified base dir.
   **
   ** @param  folder             the abstract pathname the folder where the
   **                            build file should be exist and/or will be
   **                            created.
   ** @param  name               the name of the file
   ** @param  extension          the extension of the file
   */
  public ModelStream(final ModelFolder folder, final String name, final String extension) {
    // ensure inheritance
    super(folder, name + "." + extension);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the String value that is bound at the specified name in the
   ** substitution {@link List}. If the value is <code>null</code>,
   ** <code>null</code> is returned. If the value is non-<code>null</code> and
   ** is not a String, the result of calling toString() on the value is
   ** returned. If the name is not bound, <code>null</code> is returned.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   ** @param  value              the {@link File} value to be associated with
   **                            the specified key.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or <code>null</code> if the parameters
   **                            contains no mapping for the key.
   */
  public final Property property(final String key, final File value) {
    Property found = property(key);
    if (found != null)
      found.value(value);
    else
      found = add(key, value);
    return found;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **
   ** @return                    this {@link Property} instance for method
   **                            chaining purpose.
   */
  public final Property add(final String key, final String value) {
    return add(key, key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  label              the string which represents the property in a
   **                            UI.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **
   ** @return                    this {@link Property} instance for method
   **                            chaining purpose.
   */
  public final Property add(final String key, final String label, final String value) {
    return add(key, label, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  icon               the symbol to prefix the label
   **                            <code>Property</code>.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **
   ** @return                    this {@link Property} instance for method
   **                            chaining purpose.
   */
  public final Property add(final String key, final Icon icon, final String value) {
    return add(new StringParameter(key, key, icon, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  label              the string which represents the property in a
   **                            UI.
   ** @param  icon               the symbol to prefix the label
   **                            <code>Property</code>.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **
   ** @return                    this {@link Property} instance for method
   **                            chaining purpose.
   */
  public final Property add(final String key, final String label, final Icon icon, final String value) {
    return add(new StringParameter(key, label, icon, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  property           the {@link AbstractProperty} to bound.
   **
   ** @return                    this {@link AbstractProperty} for method
   **                            chaining purpose.
   */
  public final AbstractProperty add(final AbstractProperty property) {
    Property exists = property(property);
    if (exists == null)
      this.property.add(property);
    else {
      exists.value(property.value());
    }
    return property;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  value              the {@link File} value to be associated with
   **                            the specified key.
   **
   ** @return                    this {@link Property} instance for chaining
   **                            invocations.
   */
  public final Property add(final String key, final File value) {
    return add(key, key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  icon               the symbol to prefix the label
   **                            <code>Property</code>.
   ** @param  value              the {@link File} value to be associated with
   **                            the specified key.
   **
   ** @return                    this {@link Property} instance for chaining
   **                            invocations.
   */
  public final Property add(final String key, final Icon icon, final File value) {
    return add(new FileParameter(key, key, icon, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  label              the string which represents the property in a
   **                            UI.
   ** @param  value              the {@link File} value to be associated with
   **                            the specified key.
   **
   ** @return                    this {@link Property} instance for chaining
   **                            invocations.
   */
  public final Property add(final String key, final String label, final File value) {
    return add(key, label, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  label              the string which represents the property in a
   **                            UI.
   ** @param  icon               the symbol to prefix the label
   **                            <code>Property</code>.
   ** @param  value              the {@link File} value to be associated with
   **                            the specified key.
   **
   ** @return                    this {@link Property} instance for chaining
   **                            invocations.
   */
  public final Property add(final String key, final String label, final Icon icon, final File value) {
    return add(new FileParameter(key, label, icon, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  label              the string which represents the property in a
   **                            UI.
   ** @param  value              the {@link ComboBoxModel} value to be
   **                            associated with the specified key.
   **
   ** @return                    this {@link Property} instance for chaining
   **                            invocations.
   */
  public final Property add(final String key, final String label, final ComboBoxModel<String> value) {
    return add(key, label, null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  icon               the symbol to prefix the label
   **                            <code>Property</code>.
   ** @param  value              the {@link ComboBoxModel} value to be
   **                            associated with the specified key.
   **
   ** @return                    this {@link Property} instance for chaining
   **                            invocations.
   */
  public final Property add(final String key, final Icon icon, final ComboBoxModel<String> value) {
    return add(new ListParameter(key, key, icon, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Binds the specified name to the specified value in the value substitution
   ** {@link List}.
   ** <p>
   ** If the name is already bound to a value, the new value replaces the old
   ** value. If the value is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  label              the string which represents the property in a
   **                            UI.
   ** @param  icon               the symbol to prefix the label
   **                            <code>Property</code>.
   ** @param  value              the {@link ComboBoxModel} value to be
   **                            associated with the specified key.
   **
   ** @return                    this {@link Property} instance for chaining
   **                            invocations.
   */
  public final Property add(final String key, final String label, final Icon icon, final ComboBoxModel<String> value) {
    return add(new ListParameter(key, label, icon, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the String value that is bound at the specified name in the
   ** substitution {@link List}. If the value is <code>null</code>,
   ** <code>null</code> is returned. If the value is non-<code>null</code> and
   ** is not a String, the result of calling toString() on the value is
   ** returned. If the name is not bound, <code>null</code> is returned.
   **
   ** @param  property           the {@link Property} to check for existance.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or <code>null</code> if the substitutions
   **                            contains no mapping for the key.
   **
   */
  public final Property property(final Property property) {
    return property(property.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the String value that is bound at the specified name in the
   ** substitution {@link List}. If the value is <code>null</code>,
   ** <code>null</code> is returned. If the value is non-<code>null</code> and
   ** is not a String, the result of calling toString() on the value is
   ** returned. If the name is not bound, <code>null</code> is returned.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or <code>null</code> if the parameters
   **                            contains no mapping for the key.
   */
  public final Property property(final String key, final String value) {
    Property found = property(key);
    if (found != null)
      found.value(value);
    else
      found = add(key, value);
    return found;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the String value that is bound at the specified name in the
   ** substitution {@link List}. If the value is <code>null</code>,
   ** <code>null</code> is returned. If the value is non-<code>null</code> and
   ** is not a String, the result of calling toString() on the value is
   ** returned. If the name is not bound, <code>null</code> is returned.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or <code>null</code> if the parameters
   **                            contains no mapping for the key.
   */
  public final Property property(final String key) {
    Property found = null;
    for (Property property : this.property) {
      if (property.id().equals(key)) {
        found = property;
        break;
      }
    }
    // if we didn't found the mapping in the embedded properties try to find it
    // in the includes
    if (found == null)
      found = distance(key);

    return found;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Sets the substitution of the Maven Project Object Model build file.
   **
   ** @param  property           the properties of the Maven Project Object
   **                            Model build file.
   */
  public final void property(final List<AbstractProperty> property) {
    this.property.clear();
    this.property.addAll(property);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the substitution of the Maven Project Object Model build file.
   **
   ** @return                    the properties of the Maven Project Object
   **                            Model build file.
   */
  public final List<AbstractProperty> property() {
    return this.property;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distance
  /**
   ** Returns the String value that is bound at the specified name in the
   ** distance {@link List}. If the value is <code>null</code>,
   ** <code>null</code> is returned. If the value is non-<code>null</code> and
   ** is not a String, the result of calling toString() on the value is
   ** returned. If the name is not bound, <code>null</code> is returned.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or <code>null</code> if the distance
   **                            contains no mapping for the key.
   */
  protected final Property distance(final String key) {
    Property found = null;
    for (Property distance : this.distance) {
      if (distance.id().equals(key)) {
        found = distance;
        break;
      }
    }
    return found;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   include
  /**
   ** Returns the included nodes of the Maven Project Object Model build file.
   **
   ** @return                    the included nodes of the Maven Project Object
   **                            Model build file.
   */
  public final Map<String, ModelData> include() {
    return this.include;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   include
  /**
   ** Binds the specified name to the specified node in the import {@link List}.
   ** <p>
   ** If the name is already bound to a node, the new node replaces the old
   ** node. If the node is <code>null</code>, then the name is bound to
   ** <code>null</code>.
   **
   ** @param  key                the key with which the specified node is to be
   **                            associated.
   ** @param  node               the releative or absolute path to include.
   **
   ** @return                    this <code>ModelData</code> to enable
   **                            method concatanation.
   */
  public final ModelData include(final String key, final ModelData node) {
    this.include.put(key, node);
    // we don't need to be specific for file handling doe to the includes are
    // not editable but we have to ensure that regular expression is working on
    // Windows file paths too
    this.distance.add(new StringParameter(key, key, null, relativePath(node.file()).replaceAll("\\\\", "/")));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evaluateIncludes
  /**
   ** Evaluates the distances to all imported data.
   */
  public void evaluateIncludes() {
    for (String key : this.include.keySet()) {
      final ModelData node     = this.include.get(key);
      final Property  property = this.distance(key);
      // we don't need to be specific for file handling doe to the includes are
      // not editable but we have to ensure that regular expression is working
      // on Windows file paths too
      property.value(relativePath(node.file()).replaceAll("\\\\", "/"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon (ModelData)
  /**
   ** Returns the icon that represents an Maven Project Object Model file.
   **
   ** @return                    the {@link Icon} identifiying the displayed
   **                            node as an Maven Project Object Model file.
   */
  @Override
  public final Icon icon() {
    return ModelBundle.icon(ModelBundle.NODE_BUILDFILE_ICON);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Reads the importable element definitions from the template.
   */
  public void read() {
    read(ModelStream.class.getResourceAsStream(template()));
  }
}