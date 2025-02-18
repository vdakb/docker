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

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   AbstractModuleNavigation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractModuleNavigation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.context;

import java.io.Serializable;

import javax.faces.event.ActionListener;

import oracle.hst.foundation.faces.shell.model.Module;
import oracle.hst.foundation.faces.shell.model.TaskFlow;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractModuleNavigation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>ModuleNavigationItem</code> represents an UI navigation component that
 ** appears in the application navigation region usualy a button bar.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractModuleNavigation implements Serializable
                                               ,          ActionListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1155904209335767993")
  private static final long serialVersionUID = 5974951216696969642L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Module      module;
  private boolean           selected;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractModuleNavigation</code> which wrappes the
   ** specified {@link Module} configuration.
   **
   ** @param  module             the configuration properties of this
   **                            navigation item.
   */
  protected AbstractModuleNavigation(final Module module) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (module == null)
      throw new NullPointerException("module");

    // initialize instance attributes
    this.module = module;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   module
  /**
   ** Returns the {@link Module} this wrapper belongs too.
   **
   ** @return                    the {@link Module} this wrapper belongs too.
   */
  public final Module module() {
    return this.module;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMultipleRegionModel
  /**
   ** Returns <code>true</code> if the wrapped module allows to create multiple
   ** regions in the page.
   **
   ** @return                    <code>true</code> if the wrapped module allows
   **                            to create multiple regions; otherwise
   **                            <code>false</code>.
   **                            Allowed object is <code>boolean</code>.
   */
  public final boolean isMultipleRegionModel() {
    return module().isMultipleRegionModel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected
  /**
   ** Mark this module as selected or not.
   **
   ** @param  value              <code>true</code> if this module is marked as
   **                            selected; otherwise <code>false</code>.
   **                            Allowed object is <code>boolean</code>.
   */
  public final void selected(final boolean value) {
    this.selected = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected
  /**
   ** Returns <code>true</code> if this module is marked as selected.
   **
   ** @return                    <code>true</code> if this module is marked as
   **                            selected; otherwise <code>false</code>.
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean selected() {
    return this.selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the value of the name property from the {@link Module} wrapped by
   ** this navigation item.
   **
   ** @return                    the value of the name property from the
   **                            {@link Module} wrapped by this navigation item.
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.module.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the value of the description property from the {@link Module}
   ** wrapped by this navigation item.
   **
   ** @return                    the value of the description property from the
   **                            {@link Module} wrapped by this navigation item.
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.module.getDescription();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the value of the icon property from the {@link Module} wrapped by
   ** this navigation item.
   **
   ** @return                    the value of the icon property from the
   **                            {@link Module} wrapped by this navigation item.
   **                            Possible object is {@link String}.
   */
  public final String icon() {
    return this.module.getIcon();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hoverIcon
  /**
   ** Returns the value of the hover icon property from the {@link Module}
   ** wrapped by this navigation item.
   **
   ** @return                    the value of the hover icon property from the
   **                            {@link Module} wrapped by this navigation item.
   **                            Possible object is {@link String}.
   */
  public final String hoverIcon() {
    return this.module.getHoverIcon();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   depressedIcon
  /**
   ** Returns the value of the depressed icon property from the {@link Module}
   ** wrapped by this navigation item.
   **
   ** @return                    the value of the depressed icon property from
   **                            the {@link Module} wrapped by this navigation
   **                            item.
   **                            Possible object is {@link String}.
   */
  public final String depressedIcon() {
    return this.module.getDepressedIcon();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disabledIcon
  /**
   ** Returns the value of the disabled icon property from the {@link Module}
   ** wrapped by this navigation item.
   **
   ** @return                    the value of the disabled icon property from
   **                            the {@link Module} wrapped by this navigation
   **                            item.
   **                            Possible object is {@link String}.
   */
  public final String disabledIcon() {
    return this.module.getDisabledIcon();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasToolBar
  /**
   ** Returns <code>true</code> if the {@link Module} wrapped by this navigation
   ** item has a toolbar task flow.
   **
   ** @return                    <code>true</code> if the {@link Module} wrapped
   **                            by this navigation item has a toolbar task
   **                            flow; <code>false</code> otherwise.
   */
  public final boolean hasToolBar() {
    return toolBarTaskFlow() != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toolBarTaskFlow
  /**
   ** Returns the value of the toolBarTaskFlow property from the {@link Module}
   ** wrapped by this navigation item.
   **
   ** @return                    the value of the toolBarTaskFlow property from
   **                            the {@link Module} wrapped by this navigation
   **                            item.
   **                            Possible object is {@link TaskFlow}.
   */
  public final TaskFlow toolBarTaskFlow() {
    return this.module.toolBarTaskFlow();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNavigationInstance
  /**
   ** Returns <code>true</code> if the {@link Module} wrapped by this navigation
   ** item has a navigation task flow.
   **
   ** @return                    <code>true</code> if the {@link Module} wrapped
   **                            by this navigation item has a navigation task
   **                            flow; <code>false</code> otherwise.
   */
  public final boolean hasNavigationInstance() {
    return this.module.hasNavigationInstance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigationInstance
  /**
   ** Returns the value of the taskFlowId property of the navigation module
   ** region.
   **
   ** @return                    the value of the taskFlowId property of the
   **                            navigation module navigation area.
   **                            Possible object is {@link String}.
   */
  public final String navigationInstance() {
    return this.module.navigationInstance().getInstance();
  }
}