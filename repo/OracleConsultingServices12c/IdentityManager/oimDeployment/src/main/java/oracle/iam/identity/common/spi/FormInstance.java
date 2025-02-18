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

    File        :   FormInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FormInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.tools.ant.BuildException;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class FormInstance
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>FormInstance</code> represents the a form in Identity Manager that is
 ** used to create forms.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FormInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The attributes of the form to identify. */
  private Map<String, Attribute> attribute        = new LinkedHashMap<String, Attribute>();

  /**
   ** indicates marshalling the ADF artifacts to be able to request entitlements
   ** that needs a form to enter additional data before the request of those
   ** entitlements can be submitted.
   */
  private boolean                entitlement      = false;

  /**
   ** indicates marshalling the ADF artifacts to display the filter visibility
   */
  private boolean                filterVisible    = false;

  /**
   ** indicates marshalling the ADF artifacts to display row banding in a table
   ** view is needed for a <code>Process Form</code>.
   */
  private boolean                rowBanding       = true;

  /**
   ** indicates how the columns are strechted in a table view
   */
  private String                 columnStretching = "last";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Hint
  // ~~~~~ ~~~~
  /**
   ** Enum to restrict the parameters to certain mappings keys
   */
  public static enum Hint {

    HEADER("hint.header"),
    RENDERED("hint.rendered");

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Hint</code> that allows use as a JavaBean.
     **
     ** @param  id             the identifier of the resource type.
     */
    Hint(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value of the id property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String id() {
      return this.id;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Attribute</code> represents a particular field of a form in Oracle
   ** Identity Manager that is used to create forms.
   */
  public static class Attribute extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Hint
    // ~~~~~ ~~~~
    /**
     ** Enum to restrict the parameters to certain mappings keys
     */
    public static enum Hint {
      LABEL("label"),
      NUMBER("number"),
      TOOLTIP("tooltip"),
      RENDERED("rendered"),
      READONLY("readonly"),
      SORTABLE("sortable"),
      FILTERABLE("filterable"),
      TITLE_LOV("lovTitle"),
      DISPLAY_WIDTH("displayWidth");

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Hint</code> that allows use as a JavaBean.
       **
       ** @param  id             the identifier of the resource type.
       */
      Hint(final String id) {
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: id
      /**
       ** Returns the value of the id property.
       **
       ** @return                the value of the id property.
       */
      public String id() {
        return this.id;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Property
    // ~~~~~ ~~~~~~~~
    /**
     ** Enum to restrict the parameters to certain mappings keys
     */
    public static enum Property {
      ITRESOURCE("property.ITResource"),
      EXTNCUSTOM("property.ExtnCustom"),
      LOOKUPTYPE("property.LookupType"),
      ENCRYPTED("property.oimEncrypted"),
      BULK_UPDATABLE("property.oimBulkUpdate"),
      CERTIFIABLE("property.oimCertifiable"),
      MULTI_VALUED(ConstantsDefinition.EO_ATTR_PROPERTY_OIM_MULTI_VALUED),
      ENTITLEMENT("property.oimEntitlement"),
      REF_ATTR_NAME("property.oimRefAttrName"),
      ATTR_INTERNAL_USE("property.ATTR_INTERNAL_USE"),
      ATTRIBUTE_TYPE("property.AttributeType"),
      SECRET("property.SECRET"),
      CONTROLTYPE("property.CONTROLTYPE"),
      DISPLAYHINT("property.DISPLAYHINT"),
      DISPLAYWIDTH("property.DISPLAYWIDTH"),
      TOOLTIPID("property.TOOLTIP_ResId");

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Property</code> that allows use as a JavaBean.
       **
       ** @param  id             the identifier of the resource type.
       */
      Property(final String id) {
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: id
      /**
       ** Returns the value of the id property.
       **
       ** @return                possible object is {@link String}.
       */
      public String id() {
        return this.id;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Attribute</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Attribute() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     */
    public Attribute(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addParameter (overridden)
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  name             the name of the parameter to create a mapping
     **                          for on this instance.
     ** @param  value            the value for <code>name</code> to set on this
     **                          instance.
     **
     ** @return                  the <code>Attribute</code> for method
     **                          chaining purpose.
     **
     ** @throws BuildException   if the specified name is already part of the
     **                          parameter mapping.
     */
    @Override
    public Attribute addParameter(final String name, final boolean value)
      throws BuildException {

      // ensure inheritance;
      super.addParameter(name, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addParameter (overridden)
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  name             the name of the parameter to create a mapping
     **                          for on this instance.
     ** @param  value            the value for <code>name</code> to set on this
     **                          instance.
     **
     ** @return                  the <code>Attribute</code> for method
     **                          chaining purpose.
     **
     ** @throws BuildException   if the specified name is already part of the
     **                          parameter mapping.
     */
    @Override
    public Attribute addParameter(final String name, final int value)
      throws BuildException {

      // ensure inheritance;
      super.addParameter(name, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addParameter
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  hint             the enum of the parameter to create a mapping
     **                          for on this instance.
     ** @param  value            the value for <code>name</code> to set on this
     **                          instance.
     **
     ** @return                  the <code>Attribute</code> for method
     **                          chaining purpose.
     **
     ** @throws BuildException   if the specified name is already part of the
     **                          parameter mapping.
     */
    public Attribute addParameter(final Hint hint, final Object value)
      throws BuildException {

      // ensure inheritance;
      super.addParameter(hint.id, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addParameter (overridden)
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  name               the name of the parameter to create a mapping
     **                            for on this instance.
     ** @param  value              the value for <code>name</code> to set on this
     **                            instance.
     **
     ** @return                    the <code>Attribute</code> for method
     **                            chaining purpose.
     **
     ** @throws BuildException     if the specified name is already part of the
     **                            parameter mapping.
     */
    @Override
    public Attribute addParameter(final String name, final Object value)
      throws BuildException {

      // ensure inheritance;
      super.addParameter(name, value);
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Panel
  // ~~~~~ ~~~~~
  /**
   ** <code>Panel</code> represents a structural elememet of an account form in
   ** Oracle Identity Manager that is used to create forms.
   */
  public static class Panel extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final List<String> ref = new ArrayList<String>();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Hint
    // ~~~~~ ~~~~
    /**
     ** Enum to restrict the parameters to certain mappings keys
     */
    public static enum Hint {
        ROW("rows")
      , SIZE("size")
      , COLUMN("maxColumns")
      , HEADER("text")
      , DISCLOSED("disclosed")
      , FIELD_WIDTH("fieldWidth")
      , LABEL_WIDTH("labelWidth")
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final String       id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Hint</code> that allows use as a JavaBean.
       **
       ** @param  id             the identifier of the resource type.
       */
      Hint(final String id) {
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: id
      /**
       ** Returns the value of the id property.
       **
       ** @return                the value of the id property.
       */
      public String id() {
        return this.id;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Panel</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Panel() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Panel</code> with the specified name.
     **
     ** @param  name             te name of the panel.
     */
    private Panel(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: attribute
    /**
     ** Returns the {@link List} of {@link Attribute} references of a panel.
     **
     ** @return                    the {@link List} of {@link Attribute}s of a
     **                            panel.
     */
    public List<String> attribute() {
      return this.ref;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addReference
    /**
     ** Add the specified attribute reference to be placed in this panel.
     **
     ** @param  value            the reference of an attribute  to be placed in
     **                          this panel.
     **
     ** @throws BuildException   if the specified name is already part of the
     **                          parameter mapping.
     */
    public void addReference(final String value) {
      this.ref.add(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addParameter
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  hint             the enum of the parameter to create a mapping
     **                          for on this instance.
     ** @param  value            the value for <code>name</code> to set on this
     **                          instance.
     **
     ** @return                  the <code>Panel</code> for method chaining
     **                          purpose.
     **
     ** @throws BuildException   if the specified name is already part of the
     **                          parameter mapping.
     */
    public Panel addParameter(final Hint hint, final Object value)
      throws BuildException {

      // ensure inheritance;
      super.addParameter(hint.id, value);
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Account
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Main</code> represent the process form which provides the
   ** data to manage an account in a target system.
   */
  public static class Account extends FormInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Panel                     root  = new FormInstance.Panel("root");
    private Map<String, FormInstance>       other = new LinkedHashMap<String, FormInstance>();
    private Map<String, FormInstance.Panel> panel = new LinkedHashMap<String, FormInstance.Panel>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Account</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Account() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Account</code> with the specified name.
     **
     ** @param  name             the value set for the name property.
     */
    public Account(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: root
    /**
     ** Returns the value of the root property.
     **
     ** @return                  the value of the root property.
     */
    public FormInstance.Panel root() {
      return this.root;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: other
    /**
     ** Returns the value of the other property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside object. This is why there is not a <code>set</code>
     ** method for the attribute property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **   form().add(newItem);
     ** </pre>
     ** Objects of the following type(s) are allowed in the list
     ** {@link FormInstance}
     **
     ** @return                  the value of the attribute property.
     */
    public Collection<FormInstance> other() {
      return this.other.values();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: panel
    /**
     ** Returns the value of the panel property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside object. This is why there is not a <code>set</code>
     ** method for the attribute property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **   panel().add(newItem);
     ** </pre>
     ** Objects of the following type(s) are allowed in the list
     ** {@link FormInstance.Panel}
     **
     ** @return                  the value of the attribute property.
     */
    public Collection<FormInstance.Panel> panel() {
      return this.panel.values();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: add (overridden)
    /**
     ** Add the specified {@link Attribute} to the attributes that have to be
     ** handled during the generation process.
     **
     ** @param  attribute          the {@link Attribute} that have to be added.
     **
     ** @return                    the <code>Account</code> for method chaining
     **                            purpose.
     **
     ** @throws BuildException     if the specified {@link Attribute} is already
     **                            assigned to this task.
     */
    @Override
    public Account add(final Attribute attribute)
      throws BuildException {

      // ensure inheritance
      return add(this.root, attribute);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: add (overridden)
    /**
     ** Add the specified {@link Attribute} to the attributes that have to be
     ** handled during the generation process.
     **
     ** @param  panel              the {@link Panel} that contains the
     **                            attribute.
     ** @param  attribute          the {@link Attribute} that have to be added.
     **
     ** @return                    the <code>Account</code> for method chaining
     **                            purpose.
     **
     ** @throws BuildException     if the specified {@link Attribute} is already
     **                            assigned to this task.
     */
    public Account add(final Panel panel, final Attribute attribute)
      throws BuildException {

      // ensure inheritance
      super.add(attribute);
      panel.addReference(attribute.name());
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add the specified {@link FormInstance} to the forms that have to be
     ** handled during the generation process.
     **
     ** @param  form             the {@link FormInstance} that have to be
     **                          added.
     **
     ** @return                  the <code>Account</code> for method chaining
     **                          purpose.
     **
     ** @throws BuildException   if the specified {@link FormInstance} is
     **                          already assigned to this task.
     */
    public Account add(final FormInstance form) {
      // prevent bogus input
      if (form == null)
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      // prevent bogus state
      if (this.other.containsKey(form.name()))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, form.name()));

      this.other.put(form.name(), form);
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add the specified {@link FormInstance} to the forms that have to be
     ** handled during the generation process.
     **
     ** @param  panel            the {@link FormInstance.Panel} that have to be
     **                          added.
     **
     ** @return                  the <code>Account</code> for method chaining
     **                          purpose.
     **
     ** @throws BuildException   if the specified {@link FormInstance} is
     **                          already assigned to this task.
     */
    public Account add(final FormInstance.Panel panel) {
      // prevent bogus input
      if (panel == null)
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      // prevent bogus state
      if (this.panel.containsKey(panel.name()))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, panel.name()));

      this.panel.put(panel.name(), panel);
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>FormInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FormInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FormInstance</code> with the specified name.
   **
   ** @param  name               the value set for the name property.
   */
  public FormInstance(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link Set} of {@link Attribute}s of a form.
   **
   ** @return                    the {@link Set} of {@link Attribute}s of a
   **                            form.
   */
  public Set<String> attribute() {
    return this.attribute.keySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link Attribute}s mapped to the specified name.
   **
   ** @param  name               the name of the desired {@link Attribute}.
   **
   ** @return                    the {@link Attribute} mapped to the specified
   **                            name.
   */
  public Attribute attribute(final String name) {
    return this.attribute.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Called to inject the <code>entitlement</code> to indicate that a separate
   ** page is needed for a <code>Process Form</code> belonging to the request of
   ** an <code>Entitlement</code> in Identity Manager.
   **
   ** @param  entitlement        the <code>entitlement</code> an
   **                            <code>Application Instance</code> belongs to.
   **
   ** @return                    the <code>FormInstance</code> for method
   **                            chaining purpose.
   */
  public FormInstance entitlement(final boolean entitlement) {
    this.entitlement = entitlement;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Returns the <code>entitlement</code> property that indicates that a
   ** separate page is needed for a <code>Process Form</code> belonging to the
   ** request of an <code>Entitlement</code> in Identity Manager.
   **
   ** @return                    the <code>entitlement</code> property.
   */
  public boolean entitlement() {
    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterVisible
  /**
   ** Called to inject the <code>filterVisible</code> to indicate that the
   ** filter criteria are visible.
   **
   ** @param  filterVisible      the <code>filterVisible</code> an
   **                            <code>Application Instance</code> belongs to.
   **
   ** @return                    the <code>FormInstance</code> for method
   **                            chaining purpose.
   */
  public FormInstance filterVisible(final boolean filterVisible) {
    this.filterVisible = filterVisible;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterVisible
  /**
   ** Returns the <code>filterVisible</code> property that indicates that the
   ** filter criteria are visible.
   **
   ** @return                    the <code>filterVisible</code> property.
   */
  public boolean filterVisible() {
    return this.filterVisible;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowBanding
  /**
   ** Called to inject the <code>rowBanding</code> to indicate that row banding
   ** in a table view is needed for a <code>Process Form</code>.
   **
   ** @param  rowBanding         the <code>rowBanding</code> an
   **                            <code>Application Instance</code> belongs to.
   **
   ** @return                    the <code>FormInstance</code> for method
   **                            chaining purpose.
   */
  public FormInstance rowBanding(final boolean rowBanding) {
    this.rowBanding = rowBanding;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowBanding
  /**
   ** Returns the <code>rowBanding</code> property that indicates that row
   ** banding in a table view is needed for a <code>Process Form</code>.
   **
   ** @return                    the <code>rowBanding</code> property.
   */
  public boolean rowBanding() {
    return this.rowBanding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   columnStretching
  /**
   ** Called to inject the <code>columnStretching</code> to indicate how the
   ** columns are strechted in a table view for a <code>Process Form</code>.
   **
   ** @param  columnStretching   the <code>columnStretching</code> an
   **                            <code>Application Instance</code> belongs to.
   **
   ** @return                    the <code>FormInstance</code> for method
   **                            chaining purpose.
   */
  public FormInstance columnStretching(final String columnStretching) {
    this.columnStretching = columnStretching;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   columnStretching
  /**
   ** Returns the <code>columnStretching</code> property that indicates how the
   ** columns are strechted in a table view for a <code>Process Form</code>.
   **
   ** @return                    the <code>columnStretching</code> property.
   */
  public String columnStretching() {
    return this.columnStretching;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: addParameter (overridden
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  name               the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>name</code> to set on this
   **                            instance.
   **
   ** @return                    the <code>FormInstance</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public FormInstance addParameter(final String name, final boolean value)
    throws BuildException {

    // ensure inheritance;
    super.addParameter(name, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: addParameter (overridden
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  name               the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>name</code> to set on this
   **                            instance.
   **
   ** @return                    the <code>Attribute</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public FormInstance addParameter(final String name, final int value)
    throws BuildException {

    // ensure inheritance;
    super.addParameter(name, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: addParameter
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  hint                 the enum of the parameter to create a mapping
   **                              for on this instance.
   ** @param  value                the value for <code>name</code> to set on this
   **                              instance.
   **
   ** @return                      the <code>Attribute</code> for method
   **                              chaining purpose.
   **
   ** @throws BuildException       if the specified name is already part of the
   **                              parameter mapping.
   */
  public FormInstance addParameter(final Hint hint, final Object value)
    throws BuildException {

    // ensure inheritance;
    super.addParameter(hint.id, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: addParameter (overridden
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  name                 the name of the parameter to create a mapping
   **                              for on this instance.
   ** @param  value                the value for <code>name</code> to set on this
   **                              instance.
   **
   ** @return                      the <code>Attribute</code> for method
   **                              chaining purpose.
   **
   ** @throws BuildException       if the specified name is already part of the
   **                              parameter mapping.
   */
  @Override
  public FormInstance addParameter(final String name, final Object value)
    throws BuildException {

    // ensure inheritance;
    super.addParameter(name, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified {@link Attribute} to the attributes that have to be
   ** handled during the generation process.
   **
   ** @param  attribute          the {@link Attribute} that have to be added.
   **
   ** @return                    the <code>FormInstance</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the specified {@link Attribute} is already
   **                            assigned to this task.
   */
  public FormInstance add(final Attribute attribute)
    throws BuildException {

    // prevent bogus input
    if (attribute == null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    if (this.attribute.keySet().contains(attribute.name()))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, attribute.name()));

    this.attribute.put(attribute.name(), attribute);
    return this;
  }
}