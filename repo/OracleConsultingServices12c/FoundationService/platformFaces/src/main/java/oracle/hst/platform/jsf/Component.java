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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Java Server Faces Feature

    File        :   Component.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Component.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf;

import java.util.Map;
import java.util.Optional;

import java.util.regex.Pattern;

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UICommand;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UINamingContainer;

import oracle.hst.platform.core.reflect.Discovery;
import oracle.hst.platform.jsf.utility.ScopedEvaluator;

////////////////////////////////////////////////////////////////////////////////
// abstract class Component
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Collection of utility methods for the JSF API that are mainly used to work
 ** with {@link UIComponent}.
 ** <br>
 ** There are several traversal/lookup methods, there are several {@link UIForm}
 ** and {@link UIInput} related methods which makes it easier to deal with forms
 ** and inputs.
 ** <h3>Usage</h3>
 ** <p>
 ** Some examples:
 ** <pre>
 **   // get the label of the given UIInput component as JSF uses for validation
 **   // messages
 **   final String label = Components.label(component);
 * </pre>
 ** <pre>
 **   // get currently invoked command, useful for logging actions in a phase
 **   // listener
 **   final UICommand command = Components.currentCommand();
 ** </pre>
 ** <pre>
 **   // get closest parent of given type.
 **   final UIForm form = Components.closestParent(component, UIForm.class);
 ** </pre>
 ** <pre>
 **   // get currently submitted form.
 **   final UIForm form = Components.currentForm();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Component {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

	/** The name of the label attribute. */
	public static final String LABEL_ATTRIBUTE             = "label";

	/** The name of the value attribute. */
	public static final String VALUE_ATTRIBUTE             = "value";

  /**
   ** JSF 2.3 will introduce several new constants as shown in the table below:
   ** but we arn't there
   */
  
  /** The Resource name of JSF script resource. */
  public static final String JSF_SCRIPT_RESOURCE_NAME   = "jsf.js";

  /** The Resource name of JSF script resource. */
  public static final String JSF_SCRIPT_LIBRARY_NAME    = "javax.faces";

  /**
   ** The request parameter name whose request parameter value identifies the
   ** source component of behavior event.
   */
  public static final String BEHAVIOR_SOURCE_PARAM_NAME = "javax.faces.source";

  /**
   ** The request parameter name whose request parameter value identifies the
   ** type of behavior event.
   */
  public static final String BEHAVIOR_EVENT_PARAM_NAME  = "javax.faces.event";

  /**
   ** The request parameter name whose request parameter value identifies the
   ** type of partial event.
   */
  public static final String PARTIAL_EVENT_PARAM_NAME   = "javax.faces.partial.event";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Component</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Component()" and enforces use of the public method below.
   */
  private Component() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   current
	/**
   ** Returns the current UI component from the EL context.
   **
   ** @param  <T>                the expected component type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    the current UI component from the EL context.
   **                            <br>
   **                            Possible object is {@link UIComponent} for type
   **                            <code>T</code>.
   **
   ** @throws ClassCastException if <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#getCurrentComponent(FacesContext)
   */
  @SuppressWarnings({"unchecked", "cast"}) 
  public static <T extends UIComponent> T current() {
    return (T)UIComponent.getCurrentComponent(Faces.context());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   currentCommand
  /**
   ** Returns the currently invoked UI command component, or <code>null</code>
   ** if there is none, which may happen when the current request is not a
   ** postback request at all, or when the view has been changed by for example
   ** a successful navigation.
   ** <br>
   ** If the latter is the case, you'd better invoke this method before
   ** navigation.
   **
   ** @return                    the currently invoked UI command component.
   **                            <br>
   **                            Possible object is {@link UIComponent} for type
   **                            <code>T</code>.
	 */
	public static UICommand currentCommand() {
		final UIComponent source = currentActionSource();
		return source instanceof UICommand ? (UICommand) source : null;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   currentActionSource
  /**
   ** Returns the source of the currently invoked action, or <code>null</code>
   ** if there is none, which may happen when the current request is not a
   ** postback request at all, or when the view has been changed by for example
   ** a successful navigation.
   ** <br>
   ** If the latter is the case, you'd better invoke this method before
   ** navigation.
   **
   ** @param  <T>                the expected component type to lookup.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    the source of the currently invoked action.
   **                            <br>
   **                            Possible object is {@link UIComponent} for type
   **                            <code>T</code>.
	 */
  @SuppressWarnings({"unchecked", "cast"}) 
	public static <T extends UIComponent> T currentActionSource() {
		final FacesContext context = Faces.context();
    return (!context.isPostback()) ? null : (T)currentActionSource(context, context.getViewRoot());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rendered
  /**
   ** Whether the given UI component and all of its parents are rendered.
   ** <br>
   ** This thus not only checks the component's own <code>rendered</code>
   ** attribute, but also of all of its parents.
   **
   ** @param  component          the component to be checked.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   **
   ** @return                    <code>true</code> if the given UI component
   **                            and all of its parents is rendered.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean rendered(final UIComponent component) {
    for (UIComponent current = component; current.getParent() != null; current = current.getParent()) {
      if (!current.isRendered()) {
        return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editable
  /**
   ** Whether the given UI input component is editable.
   ** <br>
   ** That is when it is rendered, not disabled and not readonly.
   **
   ** @param  component          the component to be checked.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   **
   ** @return                    <code>true</code> if the given UI input
   **                            component is editable.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean editable(final UIInput component) {
    return component.isRendered()
        && !Boolean.TRUE.equals(component.getAttributes().get("disabled"))
        && !Boolean.TRUE.equals(component.getAttributes().get("readonly"))
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the attribute of the given component on the given name.
   **
   ** @param  <T>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  component          the UI component to return the attribute of the
   **                            given name for.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
	 ** @param  name               the name of the attribute of the given
   **                            component to be returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
	 ** @return                    the attribute of the given component on the
   **                            given name.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ClassCastException if <code>T</code> is of wrong type.
	 */
  @SuppressWarnings({"unchecked", "cast"})
	public static <T> T attribute(final UIComponent component, final String name) {
		return (T)component.getAttributes().get(name);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Sets the <code>label</code> attribute of the given UI component with the
   ** given value.
   **
   ** @param  component          the UI component for which the label is to be
   **                            set.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
	 ** @param  label              the label to be set on the given UI component.
   **                            <br>
   **                            Allowed object is {@link Object}.
	 */
	public static void label(final UIComponent component, final Object label) {
		if (label instanceof ValueExpression) {
			component.setValueExpression(LABEL_ATTRIBUTE, (ValueExpression) label);
		}
		else if (label != null) {
			component.getAttributes().put(LABEL_ATTRIBUTE, label);
		}
		else {
			component.getAttributes().remove(LABEL_ATTRIBUTE);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the value of the <code>label</code> attribute associated with the
   ** given UI component if any, else the client ID.<br>
   ** It never returns <code>null</code>.
   **
   ** @param  component          the UI component for which the label is to be
   **                            retrieved.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   **
   ** @return                    the value of the <code>label</code> attribute
   **                            associated with the given UI component if any,
   **                            else the client ID.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String label(final UIComponent component) {
    String label = labelOptional(component);
    return (label != null) ? label : component.getClientId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionalLabel
  /**
   ** Returns the value of the <code>label</code> attribute associated with the
   ** given UI component if any, else <code>null</code>.
   **
   ** @param  component          the UI component for which the label is to be
   **                            retrieved.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   **
   ** @return                    the value of the <code>label</code> attribute
   **                            associated with the given UI component if any,
   **                            else <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
	 */
	public static String labelOptional(final UIComponent component) {
		final Object[] result = new Object[1];
		ScopedEvaluator.of(Faces.context()).with("cc", UIComponent.getCompositeComponentParent(component)).invoke(() -> {
      Object label = component.getAttributes().get(LABEL_ATTRIBUTE);
      if (Discovery.empty(label)) {
				final ValueExpression labelExpression = component.getValueExpression(LABEL_ATTRIBUTE);
				if (labelExpression != null) {
					label = labelExpression.getValue(Faces.expressionContext());
				}
			}
			result[0] = label;
		});
		return (result[0] != null) ? result[0].toString() : null;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns the UI component matching the given client ID search expression.
   **
   ** @param  <T>                the expected component type to lookup.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clientId           the client ID search expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the UI component matching the given client ID
   **                            search expression.
   **                            <br>
   **                            Possible object is {@link UIComponent} for type
   **                            <code>T</code>.
   **
   ** @throws ClassCastException if <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
   */
  @SuppressWarnings({"unchecked", "cast"}) 
  public static <T extends UIComponent> T find(final String clientId) {
    return (T)viewRoot().findComponent(clientId);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findRelative
  /**
   ** Returns the UI component matching the given client ID search expression
   ** relative to the point in the component tree of the given component.
   ** <br>
   ** For this search both parents and children are consulted, increasingly
   ** moving further away from the given component. Parents are consulted first,
   ** then children.
   **
   ** @param  <T>                the expected expected to lookup.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
	 ** @param  component          the component from which the relative search is
   **                            started.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param  clientId           the client ID search expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the UI component matching the given client ID
   **                            search expression.
   **                            <br>
   **                            Possible object is {@link UIComponent} for type
   **                            <code>T</code>.
   **
   ** @throws ClassCastException if <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
   */
  @SuppressWarnings({"unchecked", "cast"}) 
	public static <T extends UIComponent> T findRelative(final UIComponent component, String clientId) {
    // prevent bogus input
		if (Discovery.empty(clientId))
			return null;

		// search first in the naming container parents of the given component
		UIComponent result = findUpStairs(component, clientId);
		if (result == null) {
			// if not in the parents, search from the root
			result = findDownStairs(viewRoot(), clientId);
		}
		return (T)result;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findUpStairs
  /**
   ** Returns the UI component matching the given client ID search expression
   ** relative to the point in the component tree of the given component,
   ** searching only in its parents.
   **
   ** @param  <T>                the expected expected to lookup.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
	 ** @param  component          the component from which the relative search is
   **                            started.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param  clientId           the client ID search expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the UI component matching the given client ID
   **                            search expression.
   **                            <br>
   **                            Possible object is {@link UIComponent} for type
   **                            <code>T</code>.
   **
   ** @throws ClassCastException if <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
   */
  @SuppressWarnings({"unchecked", "cast"}) 
  public static <T extends UIComponent> T findUpStairs(final UIComponent component, final String clientId) {
    // prevent bogus input
    if (Discovery.empty(clientId))
      return null;

    for (UIComponent parent = component; parent != null; parent = parent.getParent()) {
      if (parent instanceof NamingContainer || parent instanceof UIViewRoot) {
        final UIComponent result = findSafe(parent, clientId);
        if (result != null) {
          return (T)result;
        }
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findDownStairs
  /**
   ** Returns the UI component matching the given client ID search expression
   ** relative to the point in the component tree of the given component,
   ** searching only in its children.
   **
   ** @param  <T>                the expected expected to lookup.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
	 ** @param  component          the component from which the relative search is
   **                            started.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param  clientId           the client ID search expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the UI component matching the given client ID
   **                            search expression.
   **                            <br>
   **                            Possible object is {@link UIComponent} for type
   **                            <code>T</code>.
   **
   ** @throws ClassCastException if <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
   */
  @SuppressWarnings({"unchecked", "cast"}) 
  public static <T extends UIComponent> T findDownStairs(final UIComponent component, final String clientId) {
    // prevent bogus input
    if (Discovery.empty(clientId))
      return null;

    for (UIComponent child : component.getChildren()) {
      UIComponent result = null;
      if (child instanceof NamingContainer)
        result = findSafe(child, clientId);

      // go deeper
      if (result == null)
        result = findDownStairs(child, clientId);

      if (result != null) {
        return (T)result;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   currentForm
  /**
   ** Returns the currently submitted UI form component, or <code>null</code> if
   ** there is none, which may happen when the current request is not a postback
   ** request at all, or when the view has been changed by for example a
   ** successful navigation.
   ** <br>
   ** If the latter is the case, you'd better invoke this method before
   ** navigation.
   **
   ** @return                    the currently submitted UI form component.
   **                            Possible object is {@link UIForm}.
   **
   ** @see    UIForm#isSubmitted()
	 */
	public static UIForm currentForm() {
		FacesContext context = Faces.context();
		if (!context.isPostback()) {
			return null;
		}

		UIViewRoot viewRoot = context.getViewRoot();
		if (viewRoot == null)
			return null;

		// the initial implementation has visited the tree for UIForm components
    // which returns true on isSubmitted() but with testing it turns out to
    // return false on ajax requests where the form is not included in execute!
		// the current implementation just walks through the request parameter map
    // instead.
		for (String name : context.getExternalContext().getRequestParameterMap().keySet()) {
			if (name.startsWith("javax.faces."))
        // quick skip.
				continue; 

			final UIComponent component = findSafe(viewRoot, name);
			if (component instanceof UIForm)
				return (UIForm) component;
			else if (component != null) {
				final UIForm form = closestParent(component, UIForm.class);
        if (form != null)
					return form;
			}
		}
		return null;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closestParent
  /**
   ** Finds from the given component the closest parent of the given parent
   ** type.
   **
   ** @param  <T>                the generic component type of the parent to
   **                            lookup.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  component          the component to return the closest parent of
   **                            the given parent type for.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param type                the parent component type to lookup.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    starting from the given component the closest
   **                            parent of the given parent type.
   **                            <br>
   **                            Possible object is {@link Optional} for type
   **                            <code>T</code>.
	 */
	public static <T extends UIComponent> Optional<T> closestParentFind(final UIComponent component, final Class<T> type) {
		return Optional.ofNullable(closestParent(component, type));
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closestParent
  /**
   ** Returns from the given component the closest parent of the given parent
   ** type, or <code>null</code> if none is found.
   **
   ** @param  <T>                the generic component type of the parent to
   **                            lookup.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  component          the component to return the closest parent of
   **                            the given parent type for.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param type                the parent component type to lookup.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    starting from the given component the closest
   **                            parent of the given parent type, or
   **                            <code>null</code> if none is found.
   **                            <br>
   **                            Possible object is {@link UIComponent} for type
   **                            <code>T</code>.
   */
  public static <T extends UIComponent> T closestParent(final UIComponent component, final Class<T> type) {
    UIComponent parent = component.getParent();
    while (parent != null && !type.isInstance(parent)) {
      parent = parent.getParent();
    }
    return type.cast(parent);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewRootId
  /**
   ** Returns identifier of the current view root.
   **
   ** @return                    the identifier of current view root.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    FacesContext#getViewRoot()
   */
  public static String viewRootId() {
    return viewRoot().getId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewRoot
  /**
   ** Returns the current view root.
   **
   ** @return                    the current view root.
   **
   ** @see    FacesContext#getViewRoot()
   */
  public static UIViewRoot viewRoot() {
    return Faces.context().getViewRoot();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   currentActionSource
  /**
   ** Helper method for {@link #currentActionSource()}.
   **
   ** @param  component          the component from which the relative search is
   **                            started.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
	 */
	static UIComponent currentActionSource(final FacesContext context, final UIComponent parent) {
    // prevent bogus input
		if (parent == null)
			return null;

		final Map<String, String> parameter = context.getExternalContext().getRequestParameterMap();
		if (context.getPartialViewContext().isAjaxRequest()) {
			final String sourceId = parameter.get(BEHAVIOR_SOURCE_PARAM_NAME);
			if (sourceId != null) {
				final UIComponent source = findSafe(parent, sourceId);
				if (source != null)
					return source;
			}
		}

		for (String name : parameter.keySet()) {
			if (name.startsWith("javax.faces."))
        // quick skip.
				continue;

			final UIComponent source = findSafe(parent, name);
			if (source instanceof UICommand)
				return source;
		}

    // if still not found and parent is UIViewRoot, then it can happen when
    // prependId="false" is set on form
    // hopefully it will be deprecated one day
    return (parent instanceof UIViewRoot) ? currentActionSource(context, currentForm()) : null;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findSafe
  /**
   ** Use {@link UIComponent#findComponent(String)} and ignore the potential
   ** {@link IllegalArgumentException} by returning null instead.
   **
	 ** @param  component          the component from which the relative search is
   **                            started.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param  clientId           the client ID search expression.
   **                            <br>
   **                            Allowed object is {@link String}.
	 */
	private static UIComponent findSafe(final UIComponent parent, final String clientId) {
		try {
			return parent.findComponent(stripIterationIndex(clientId));
		}
		catch (IllegalArgumentException ignore) {
//			logger.log(FINEST, "Ignoring thrown exception; this may occur when view has changed by for example a successful navigation.", ignore);
			return null;
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stripIterationIndex
  /**
   ** Strip UIData/UIRepeat iteration index in pattern <code>:[0-9+]:</code>
   ** from given component client ID.
   **
   ** @param  clientId           the client ID search expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the the client ID any UIData/UIRepeat iteration
   **                            index stripped off.
   **                            <br>
   **                            Possible object is {@link String}.
	 */
	private static String stripIterationIndex(final String clientId) {
		String separatorChar = Character.toString(UINamingContainer.getSeparatorChar(Faces.context()));
		return clientId.replaceAll(Pattern.quote(separatorChar) + "[0-9]+" + Pattern.quote(separatorChar), separatorChar);
	}
}