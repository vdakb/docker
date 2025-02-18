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

    File        :   Module.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Module.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model;

import java.util.List;
import java.util.ArrayList;

import org.w3c.dom.Element;

////////////////////////////////////////////////////////////////////////////////
// class Module
// ~~~~~ ~~~~~~
/**
 ** Displays version informations of this module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Module extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String      DEFAULT_INVISIBLE_HEIGHT = "0";
  private static final String      DEFAULT_TOOLBAR_HEIGHT   = "30";
  private static final RegionModel DEFAULT_TAB_MODEL        = RegionModel.DEFAULT;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1715078172978257633")
  private static final long        serialVersionUID         = -7487822993310388814L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                   url;
  private RegionModel              regionModel;
  private String                   toolBarTaskFlowHeight;
  private TaskFlow                 toolBarTaskFlow;
  private TaskFlow                 navigationTaskFlow;
  private List<TaskFlow>           detailTaskFlow;

  public static enum RegionModel {
    SINGLE
  , DEFAULT;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("compatibility:-1149679859799843283")
    private static final long      serialVersionUID         = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Module</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Module() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Module</code> with properties defined by the specified
   ** {@link Element}.
   **
   ** @param  element            the {@link Element} providing the values for
   **                            the properties.
   */
  public Module(final Element element) {
    // ensure inheritance
    super(element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Sets the value of the url property.
   **
   ** @param  value              the value of the url property.
   **                            Allowed object is {@link String}.
   */
  public void url(final String value) {
    this.url = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Returns the value of the url property.
   **
   ** @return                    the value of the url property.
   **                            Possible object is {@link String}.
   */
  public String url() {
    return this.url;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasUrl
  /**
   ** Verifies if thie Module has valid url property.
   **
   ** @return                    <code>true</code> if this <code>Module</code>
   **                            as a valid url property; otherwise
   **                            <code>false</code>.
   */
  public boolean hasUrl() {
    return (this.url != null) && (!this.url.trim().equals(""));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toolBarTaskFlowHeight
  /**
   ** Sets the value of the toolBarTaskFlowHeight property.
   **
   ** @param  value              the value of the toolBarTaskFlowHeight
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void toolBarTaskFlowHeight(final String value) {
    this.toolBarTaskFlowHeight = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toolBarTaskFlowHeight
  /**
   ** Returns the value of the toolBarTaskFlowHeight property.
   **
   ** @return                    the value of the toolBarTaskFlowHeight
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String toolBarTaskFlowHeight() {
    if (this.toolBarTaskFlowHeight == null) {
      return hasToolBar() ? DEFAULT_TOOLBAR_HEIGHT : DEFAULT_INVISIBLE_HEIGHT;
    }
    return this.toolBarTaskFlowHeight;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toolBarTaskFlow
  /**
   ** Sets the value of the toolBarTaskFlow property.
   **
   ** @param  value              the value of the toolBarTaskFlow property.
   **                            Allowed object is {@link TaskFlow}.
   */
  public void toolBarTaskFlow(final TaskFlow value) {
    this.toolBarTaskFlow = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasToolBar
  /**
   ** Returns <code>true</code> if this module has a toolbar task flow.
   **
   ** @return                    <code>true</code> if this module has a toolbar
   **                            task flow; <code>false</code> otherwise.
   */
  public boolean hasToolBar() {
    return toolBarTaskFlow() != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toolBarTaskFlow
  /**
   ** Returns the value of the toolBarTaskFlow property.
   **
   ** @return                    the value of the toolBarTaskFlow property.
   **                            Possible object is {@link TaskFlow}.
   */
  public TaskFlow toolBarTaskFlow() {
    return this.toolBarTaskFlow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigationTaskFlow
  /**
   ** Sets the value of the navigationTaskFlow property.
   **
   ** @param  value              the value of the navigationTaskFlow property.
   **                            Allowed object is {@link TaskFlow}.
   */
  public void navigationTaskFlow(final TaskFlow value) {
    this.navigationTaskFlow = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNavigationInstance
  /**
   ** Returns <code>true</code> if this module has a navigation task flow.
   **
   ** @return                    <code>true</code> if this module has a
   **                            navigation task flow; <code>false</code>
   **                            otherwise.
   */
  public boolean hasNavigationInstance() {
    return navigationInstance() != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigationInstance
  /**
   ** Returns the value of the navigation TaskFlow property.
   **
   ** @return                    the value of the navigation TaskFlow property.
   **                            Possible object is {@link TaskFlow}.
   */
  public TaskFlow navigationInstance() {
    return this.navigationTaskFlow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   regionModel
  /**
   ** Sets the value of the regionModel property.
   **
   ** @param  value              the value of the tabModel property.
   **                            Allowed object is {@link RegionModel}.
   */
  public void regionModel(final RegionModel value) {
    this.regionModel = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   regionModel
  /**
   ** Returns the value of the tabModel property.
   **
   ** @return                    the value of the tabModel property.
   **                            Possible object is {@link RegionModel}.
   */
  public RegionModel regionModel() {
    return (this.regionModel == null) ? DEFAULT_TAB_MODEL : this.regionModel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMultipleRegionModel
  /**
   ** Returns <code>true</code> if this module allows to create multiple tabs.
   **
   ** @return                    <code>true</code> if this module allows to
   **                            create multiple tabs; otherwise
   **                            <code>false</code>.
   **                            Allowed object is {@link String}.
   */
  public boolean isMultipleRegionModel() {
    return regionModel() != RegionModel.SINGLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detailTaskFlow
  /**
   ** Returns the value of the detailTaskFlow property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the object. This is why there is not a <code>set</code> method for
   ** the detailTaskFlow property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   getDetailAreaTaskFlow().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list {@link TaskFlow}.
   **
   ** @return                    the value of the detailTaskFlow property.
   */
  public List<TaskFlow> detailTaskFlow() {
    if (this.detailTaskFlow == null) {
      this.detailTaskFlow = new ArrayList<TaskFlow>();
    }
    return this.detailTaskFlow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one.
   ** <p>
   ** The <code>equals</code> method implements an equivalence relation on
   ** non-<code>null</code> object references:
   ** <ul>
   **   <li>It is <i>reflexive</i>: for any non-<code>null</code> reference
   **       value <code>x</code>, <code>x.equals(x)</code> should return
   **       <code>true</code>.
   **   <li>It is <i>symmetric</i>: for any non-<code>null</code> reference
   **       values <code>x</code> and <code>y</code>, <code>x.equals(y)</code>
   **       should return <code>true</code> if and only if
   **       <code>y.equals(x)</code> returns <code>true</code>.
   **   <li>It is <i>transitive</i>: for any non-<code>null</code> reference
   **       values <code>x</code>, <code>y</code>, and <code>z</code>, if
   **       <code>x.equals(y)</code> returns <code>true</code> and
   **       <code>y.equals(z)</code> returns <code>true</code>, then
   **       <code>x.equals(z)</code> should return <code>true</code>.
   **   <li>It is <i>consistent</i>: for any non-<code>null</code> reference
   **       values <code>x</code> and <code>y</code>, multiple invocations of
   **       <code>x.equals(y)</code> consistently return <code>true</code> or
   **       consistently return <code>false</code>, provided no information used
   **       in <code>equals</code> comparisons on the objects is modified.
   **   <li>For any non-<code>null</code> reference value <code>x</code>,
   **       <code>x.equals(null)</code> should return <code>false</code>.
   ** </ul>
   ** <p>
   ** Note that it is generally necessary to override the <code>hashCode</code>
   ** method whenever this method is overridden, so as to maintain the general
   ** contract for the <code>hashCode</code> method, which states that equal
   ** objects must have equal hash codes.
   **
   ** @param  other             the reference object with which to compare.
   **
   ** @return                   <code>true</code> if this object is the same as
   **                           the other argument; <code>false</code>
   **                           otherwise.
   **
   ** @see    #hashCode()
   */
  @Override
  public boolean equals(final Object other) {
    if (other == null || (!(other instanceof Module)))
      return false;

    final Module that = (Module)other;
    return (equal(getClass(), that.getClass())) && (equal(getId(), that.getId()));
  }
}