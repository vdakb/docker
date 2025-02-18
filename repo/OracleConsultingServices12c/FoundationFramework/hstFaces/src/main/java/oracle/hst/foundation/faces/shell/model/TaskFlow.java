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

    File        :   TaskFlow.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TaskFlow.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model;

import java.util.Map;
import java.util.HashMap;

import java.io.Serializable;

import org.w3c.dom.Element;

import oracle.adf.controller.TaskFlowId;

////////////////////////////////////////////////////////////////////////////////
// class TaskFlow
// ~~~~~ ~~~~~~~~
/**
 ** Displays version informations of this module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TaskFlow extends    Entity
                      implements Cloneable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String        ID                = "id";
  public static final String        NEWID             = "newId";
  public static final String        INSTANCE          = "taskFlow";
  public static final String        DIRTYICON         = "dirtyIcon";
  public static final String        CLOSEABLE         = "closeable";
  public static final String        DIALOG            = "dialog";
  public static final String        PARAMETER         = "parammeter";

  private static final Boolean      DEFAULT_CLOSEABLE = Boolean.TRUE;
  private static final Boolean      DEFAULT_IN_DIALOG = Boolean.FALSE;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1566995105792110649")
  private static final long         serialVersionUID = 4935154351263537028L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                    instance;
  private Boolean                   closeable;
  private Boolean                   dialog;
  private Boolean                   enableLinking      = Boolean.FALSE;
  private String                    dirtyIcon;
  private Map<String, Serializable> parameter;
  private String[]                  allowedURLParams;

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
  public TaskFlow() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskFlow</code> with properties defined by the
   ** specified {@link Element}.
   **
   ** @param  element            the {@link Element} providing the values for
   **                            the properties.
   */
  public TaskFlow(final Element element) {
    // ensure inheritance
    super(element);

    this.instance         = element(element,   INSTANCE,           String.class);
    this.dirtyIcon        = element(element,   DIRTYICON,          String.class);
    this.closeable        = attribute(element, CLOSEABLE,          Boolean.class);
    this.dialog           = attribute(element, DIALOG,             Boolean.class);
    this.enableLinking    = attribute(element, "enableLinking",    Boolean.class);
//    this.allowedURLParams = attribute(element, "allowedURLParams", String.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInstance
  /**
   ** Sets the value of the instance property.
   **
   ** @param  value              the value of the instance property.
   **                            Allowed object is {@link String}.
   */
  public void setInstance(final String value) {
    this.instance = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInstance
  /**
   ** Returns the value of the instance property.
   **
   ** @return                    the value of the instance property.
   **                            Possible object is {@link String}.
   */
  public String getInstance() {
    return this.instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDirtyIcon
  /**
   ** Sets the value of the dirtyIcon property.
   **
   ** @param  value              the value of the dirtyIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setDirtyIcon(final String value) {
    this.dirtyIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDirtyIcon
  /**
   ** Returns the value of the closeable property.
   **
   ** @return                    the value of the closeable property.
   **                            Possible object is {@link String}.
   */
  public String getDirtyIcon() {
    return this.dirtyIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCloseable
  /**
   ** Sets the value of the closeable property.
   **
   ** @param  value              the value of the closeable property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setCloseable(final Boolean value) {
    this.closeable = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCloseable
  /**
   ** Returns the value of the closeable property.
   **
   ** @return                    the value of the closeable property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isCloseable() {
    return (this.closeable == null) ? DEFAULT_CLOSEABLE.booleanValue() : this.closeable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDialog
  /**
   ** Sets the value of the dialog property.
   **
   ** @param  value              the value of the dialog property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setDialog(final Boolean value) {
    this.dialog = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDialog
  /**
   ** Returns the value of the closeable property.
   **
   ** @return                    the value of the closeable property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isDialog() {
    return (this.dialog == null) ? DEFAULT_IN_DIALOG.booleanValue() : this.dialog.booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnableLinking
  /**
   ** Sets the value of the enableLinking property.
   **
   ** @param  value              the value of the enableLinking property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setEnableLinking(final Boolean value) {
    this.enableLinking = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEnableLinking
  /**
   ** Returns the value of the enableLinking property.
   **
   ** @return                    the value of the enableLinking property.
   **                            Possible object is <code>boolean</code>.
   */
  public Boolean isEnableLinking() {
    return this.enableLinking;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowedURLParams
  /**
   ** Sets the value of the allowedURLParams property.
   **
   ** @param  value              the value of the allowedURLParams property.
   **                            Allowed object is {@link String}[].
   */
  public void setAllowedURLParams(final String value) {
    if ((value != null) && (value.trim().length() > 0))
      setAllowedURLParams(value.split(","));
    else
      this.allowedURLParams = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowedURLParams
  /**
   ** Sets the value of the allowedURLParams property.
   **
   ** @param  value              the value of the allowedURLParams property.
   **                            Allowed object is {@link String}[].
   */
  public void setAllowedURLParams(final String[] value) {
    this.allowedURLParams = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowedURLParams
  /**
   ** Returns the value of the allowedURLParams property.
   **
   ** @return                    the value of the allowedURLParams property.
   **                            Possible object is {@link String}[].
   */
  public String[] getAllowedURLParams() {
    return this.allowedURLParams;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setParameter
  /**
   ** Sets the value of the parameter property.
   **
   ** @param  value              the value of the parameter property.
   **                            Allowed object is {@link Map}&lt;String, Serializable&gt;
   */
  public void setParameter(final Map<String, Serializable> value) {
    this.parameter = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getParameter
  /**
   ** Returns the value of the parameter property.
   **
   ** @return                    the value of the parameter property.
   **                            Possible object is {@link Map}&lt;String, Serializable&gt;.
   */
  public Map<String, Serializable> getParameter() {
    if (this.parameter == null)
      this.parameter = new HashMap<String, Serializable>();

    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone (Clonable)
  /**
   ** Creates and returns a copy of this object.
   ** <p>
   ** By convention, the returned object is obtained by calling super.clone.
   ** If a class and all of its superclasses (except Object) obey this
   ** convention, it will be the case that x.clone().getClass() == x.getClass().
   ** <p>
   ** By convention, the object returned by this method is independent of this
   ** object (which is being cloned).
   **
   ** @return                    a clone of this instance.
   */
  @Override
  public TaskFlow clone() {
    try {
      return (TaskFlow)super.clone();
    }
    catch (CloneNotSupportedException e) {
      throw new InternalError(e.getMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone
  /**
   ** Creates and returns a copy of this object.
   ** <p>
   ** By convention, the returned object is obtained by calling super.clone.
   ** If a class and all of its superclasses (except Object) obey this
   ** convention, it will be the case that x.clone().getClass() == x.getClass().
   ** <p>
   ** By convention, the object returned by this method is independent of this
   ** object (which is being cloned).
   **
   ** @param  recursive          <code>true</code> if the cloning should be done
   **                            recursivly; otherwise <code>false</code>.
   **
   ** @return                    a clone of this instance.
   */
  public TaskFlow clone(final boolean recursive) {
    try {
      return (TaskFlow)super.clone();
    }
    catch (CloneNotSupportedException e) {
      throw new InternalError(e.getMessage());
    }
  }

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
    long   hc = getClass().hashCode();
    final String id = getId();
    if (id != null) {
      hc += id.hashCode();
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
    if (other == null || (!(other instanceof TaskFlow)))
      return false;

    final TaskFlow that = (TaskFlow)other;
    return (equal(getClass(), that.getClass())) && (equal(getId(), that.getId()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Construct a task flow ID from the instance representation of this task
   ** flow.
   ** <p>
   ** Expects the format to be: &lt;document#&lt;id&gt;.
   ** <br>
   ** For example: /WEB-INF/mytask.xml#taskA specifies that the document is
   ** /WEB-INF/mytask.xml and the task flow id is taskA.
   **
   ** @return                    the constructed {@link TaskFlowId} from the
   **                            instance representation.
   */
  public TaskFlowId parse() {
    return TaskFlowId.parse(getInstance());
  }
}