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

    File        :   ValueChangeValidator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ValueChangeValidator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.validator;

import java.util.Objects;

import javax.faces.context.FacesContext;

import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

////////////////////////////////////////////////////////////////////////////////
// abstract class ValueChangeValidator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A {@link Validator} implementation is a class that can perform validation
 ** (correctness checks) on a {@link EditableValueHolder}. Zero or more
 ** {@link Validator}s can be associated with each
 ** {@link EditableValueHolder} in the view, and are called during the
 ** <i>Process Validations</i> phase of the request processing lifecycle.
 ** <p>
 ** By default, JSF validators run on every request, regardless of whether the
 ** submitted value has changed or not. In case of validation against the
 ** persitence layer on complex objects which are already stored in the model in
 ** a broader scope, such as the view scope, this may result in unnecessarily
 ** expensive service calls. In such case, you'd like to perform the expensive
 ** service call only when the submitted value is really changed as compared to
 ** the model value.
 ** <p>
 ** This validator offers a template to do it transparently. To use it, just
 ** change the validators from:
 ** <pre>
 **   public class MyValidator implements Validator&lt;MyEntity&gt; {
 **     public void validate(final FacesContext context, final UIComponent component, final MyEntity submitted) {
 **       // ...
 **     }
 **   }
 ** </pre>
 ** to
 ** <pre>
 **   public class MyValidator extends ValueChangeValidator&lt;MyEntity&gt; {
 **     public void validateChanged(final FacesContext context, final UIComponent component, final MyEntity submitted) {
 **       // ...
 **     }
 **   }
 ** </pre>
 ** So, essentially, just replace <code>implements Validator</code> by
 ** <code>extends ValueChangeValidator</code> and rename the method from
 ** <code>validate</code> to <code>validateChanged</code>.
 **
 ** @param  <T>                  the type of the entity implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              implementing this interface (loggables can
 **                              return their own specific type instead of type
 **                              defined by this interface only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ValueChangeValidator<T> implements Validator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ValueChangeValidator</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected ValueChangeValidator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (Validator)
  /**
   ** Perform the correctness checks implemented by this {@link Validator}
   ** against the specified UIComponent. If any violations are found, a
   ** {@link ValidatorException} will be thrown containing the
   ** {@code FacesMessage} describing the failure. 
   ** <p>
	 ** If the component is an instance of {@link EditableValueHolder} and its old
   ** object value is equal to the submitted value, then return immediately from
   ** the method and don't perform any validation. Otherwise, invoke
   ** {@link #validateChanged(FacesContext, UIComponent, Object)} which may in
   ** turn do the necessary possibly expensive service operations.
   **
   ** @param  context            the {@link FacesContext} for the request we are
   **                            processing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** @param  component          the {@link UIComponent} we are checking for
   **                            correctness.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param  submitted          the submitted value.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @throws ValidatorException   if validation fails.
   ** @throws NullPointerException if <code>context</code> or
   **                              <code>component</code> is <code>null</code>.
   */
	@Override
  @SuppressWarnings({"unchecked", "cast"})
	public void validate(final FacesContext context, final UIComponent component, final Object submitted)
    throws ValidatorException {

    // prevent bogus input
		if (Objects.requireNonNull(component) instanceof EditableValueHolder) {
			Object newValue = submitted;
			Object oldValue = ((EditableValueHolder)component).getValue();
			if (Objects.equals(newValue, oldValue)) {
				return;
			}
		}
    // ensure non-null
		validateChanged(Objects.requireNonNull(context), component, (T)submitted);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateChanged
	/**
	 ** The method to use instead of
   ** {@link #validate(FacesContext, UIComponent, Object)} if its intended to
   ** perform the validation only when the submitted value is really changed as
   ** compared to the model value.
   **
   ** @param  context            the {@link FacesContext} for the request we are
   **                            processing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** @param  component          the {@link UIComponent} we are checking for
   **                            correctness.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param  submitted          the submitted value.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @throws ValidatorException   if validation fails.
	 */
	public abstract void validateChanged(final FacesContext context, final UIComponent component, final T submitted);
}