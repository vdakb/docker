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

    File        :   AbstractTaskflowNavigation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractTaskflowNavigation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.context;

import java.util.Map;

import java.io.Serializable;

import javax.faces.event.ActionListener;

import oracle.hst.foundation.faces.shell.model.TaskFlow;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractTaskflowNavigation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractTaskflowNavigation</code> represents an UI navigation
 ** component that appears in the application navigation region usualy a button
 ** bar.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractTaskflowNavigation implements Serializable
                                                ,          ActionListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4829980465702296396")
  private static final long serialVersionUID = 2831765892216928874L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private TaskFlow          taskFlow;
  private int               region;
  private boolean           selected = false;
  private Boolean           dirty;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractTaskflowNavigation</code> which wrappes the
   ** specified {@link TaskFlow} configuration.
   **
   ** @param  taskFlow           the configuration properties of this
   **                            navigation item.
   */
  protected AbstractTaskflowNavigation(final TaskFlow taskFlow) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (taskFlow == null)
      throw new NullPointerException("taskFlow");

    // initialize instance attributes
    this.taskFlow = taskFlow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   region
  /**
   ** Sets the index of the region this item occupies.
   **
   ** @param  value              the index of the region this item occupies.
   **                            Allowed object is <code>int</code>.
   */
  public void region(final int value) {
    this.region = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   region
  /**
   ** Returns the index of the region this item occupies.
   **
   ** @return                    the index of the region this item occupies.
   **                            Possible object is <code>int</code>.
   */
  public int region() {
    return this.region;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected
  /**
   ** Mark this taskflow as selected or not.
   **
   ** @param  value              <code>true</code> if this taskflow is marked
   **                            as selected; otherwise false.
   **                            Allowed object is {@link String}.
   */
  public void selected(final boolean value) {
    this.selected = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected
  /**
   ** Returns <code>true</code> if this taskflow is marked as selected.
   **
   ** @return                    <code>true</code> if this taskflow is marked
   **                            as selected; otherwise false.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean selected() {
    return this.selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dirty
  /**
   ** Mark this taskflow as changed or not.
   **
   ** @param  value              <code>true</code> if this taskflow has
   **                            changes; otherwise false.
   **                            Allowed object is {@link Boolean}.
   */
  public void setDirty(final Boolean value) {
    this.dirty = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dirty
  /**
   ** Returns <code>true</code> if this taskflow has changes.
   **
   ** @return                    <code>true</code> if this taskflow has
   **                            changes; otherwise false.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean dirty() {
    return (this.dirty == null) ? false : this.dirty.booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the value of the name property from the {@link TaskFlow} wrapped
   ** by this navigation item.
   **
   ** @return                    the value of the name property from the
   **                            {@link TaskFlow} wrapped by this navigation
   **                            item.
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.taskFlow.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the value of the description property from the {@link TaskFlow}
   ** wrapped by this navigation item.
   **
   ** @return                    the value of the description property from the
   **                            {@link TaskFlow} wrapped by this navigation
   **                            item.
   **                            Possible object is {@link String}.
   */
  public String description() {
    return this.taskFlow.getDescription();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the value of the icon property from the {@link TaskFlow} wrapped
   ** by this navigation item.
   **
   ** @return                    the value of the icon property from the
   **                            {@link TaskFlow} wrapped by this navigation
   **                            item.
   **                            Possible object is {@link String}.
   */
  public String icon() {
    return this.taskFlow.getIcon();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskFlow
  /**
   ** Sets the {@link TaskFlow} property.
   **
   ** @param  value              the {@link TaskFlow} to set.
   */
  public void taskFlow(final TaskFlow value) {
    this.taskFlow = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskFlowInstance
  /**
   ** Returns the instance identifier of the wrapped {@link TaskFlow}.
   **
   ** @return                    the instance identifier of the wrapped
   **                            {@link TaskFlow}.
   */
  public final String taskFlowInstance() {
    return taskFlow().getInstance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskFlowParameter
  /**
   ** Returns the parameters of the wrapped {@link TaskFlow}.
   **
   ** @return                    the parameters of the wrapped {@link TaskFlow}.
   */
  public final Map<String, Serializable> taskFlowParameter() {
    return taskFlow().getParameter();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskFlow
  /**
   ** Returns the {@link TaskFlow}.
   **
   ** @return                    the {@link TaskFlow}.
   */
  public final TaskFlow taskFlow() {
    return this.taskFlow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    long     hc = getClass().hashCode();
    TaskFlow tf = taskFlow();
    if (tf != null) {
      hc += tf.hashCode();
    }
    return (int)hc;
  }

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
    if ((other == null) || (!(other instanceof AbstractTaskflowNavigation)))
      return false;

    final AbstractTaskflowNavigation that = (AbstractTaskflowNavigation)other;
    return taskFlow().equals(that.taskFlow());
  }
}