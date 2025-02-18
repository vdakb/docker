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

    File        :   ValueChangeConverter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ValueChangeConverter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.converter;

import java.util.Objects;

import javax.faces.context.FacesContext;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;

////////////////////////////////////////////////////////////////////////////////
// abstract class ValueChangeConverter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A {@link Converter} is an interface describing a Java class that can perform
 ** Object-to-String and String-to-Object conversions between model data objects
 ** and a String representation of those objects that is suitable for rendering.
 ** <p>
 ** By default, JSF converters run on every request, regardless of whether the
 ** submitted value has changed or not. In case of conversion against the
 ** persitence layer on complex objects which are already stored in the model in
 ** a broader scope, such as the view scope, this may result in unnecessarily
 ** expensive service calls.  In such case, you'd like to perform the expensive
 ** service call only when the submitted value is really changed as compared to
 ** the model value.
 ** <p>
 ** This converter offers you a template to do it transparently. To use it, just
 ** change your converters from:
 ** <pre>
 **   public class MyConverter implements Converter&lt;MyEntity&gt; {
 **     public MyEntity getAsObject(final FacesContext context, final UIComponent component, final String submitted) {
 **       // ...
 **     }
 **   }
 ** </pre>
 ** to
 ** <pre>
 **   public class MyConverter extends ValueChangeConverter&lt;MyEntity&gt; {
 **     public MyEntity getAsChanged(final FacesContext context, final UIComponent component, final String submitted) {
 **       // ...
 **     }
 **   }
 ** </pre>
 ** So, essentially, just replace <code>implements Converter</code> by
 ** <code>extends ValueChangeValidator</code> and rename the method from
 ** <code>getAsString</code> to <code>getAsChanged</code>.
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
public abstract class ValueChangeConverter<T> implements Converter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ValueChangeConverter</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected ValueChangeConverter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAsObject (Converter)
  /**
   ** Convert the specified string value, which is associated with the specified
   ** {@link UIComponent}, into a model data object that is appropriate for
   ** being stored during the <i>Apply Request Values</i> phase of the request
   ** processing lifecycle.
   **
   ** @param  context            the {@link FacesContext} for the request being
   **                            processed.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** @param  component          the {@link UIComponent} with which this model
   **                            object value is associated.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param  submitted          the string representation of the value to be
   **                            converted.
   **                            <br>
   **                            May be <code>null</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>null</code> if the value to convert is
   **                            <code>null</code>, otherwise the result of the
   **                            conversion.
   **                            <br>
   **                            Possible object is {@link Object}.
   **
   ** @throws NullPointerException if context or component is <code>null</code>.
   ** @throws ConverterException   if conversion cannot be successfully
   **                              performed.
   */
  @Override
  @SuppressWarnings({"unchecked", "cast"})
  public T getAsObject(final FacesContext context, final UIComponent component, final String submitted) {
    if (component instanceof EditableValueHolder) {
      final T      value  = (T)((EditableValueHolder)component).getValue();
      final String origin = getAsString(context, component, value);
      if (Objects.equals(submitted, origin)) {
        return value;
      }
    }
    return getAsChanged(context, component, submitted);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAsChanged
	/**
	 ** The method to use instead of
   ** {@link #getAsObject(FacesContext, UIComponent, String)} if its intended to
   ** perform the conversion only when the submitted value is really changed as
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
   ** @param  submitted          the string representation of the value to be
   **                            converted.
   **                            <br>
   **                            May be <code>null</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   **
	 ** @return                    the converted value, exactly like as when you
   **                            use {@link #getAsObject(FacesContext, UIComponent, String)}
   **                            the usual way.
   **                            <br>
   **                            Possible object is <code>T</code>.
	 */
	public abstract T getAsChanged(final FacesContext context, final UIComponent component, final String submitted);
}