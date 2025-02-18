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

    File        :   ValueTrimmerConverter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ValueTrimmerConverter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/
package oracle.hst.platform.jsf.converter;

import javax.faces.context.FacesContext;

import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.ConverterException;

import javax.faces.component.UIComponent;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class ValueTrimmerConverter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** A {@link Converter} is an interface describing a Java class that can perform
 ** Object-to-String and String-to-Object conversions between model data objects
 ** and a String representation of those objects that is suitable for rendering.
 ** <p>
 ** The <code>ValueTrimmerConverter</code> is intented to trim any whitespace
 ** from submitted {@link String} values. This keeps the data store free of
 ** whitespace pollution.
 ** <p>
 ** This converter does by design no conversion in <code>getAsString()</code>.
 ** <h2>Usage</h2>
 ** This converter is available by converter ID
 ** <code>faces.platform.Trimmer</code>. Just specify it in the
 ** <code>converter</code> attribute of the component referring the
 ** <code>String</code> property. For example:
 ** <pre>
 **   &lt;h:inputText value="#{bean.username}" converter="faces.platform.Trimmer"/&gt;
 ** </pre>
 ** <p>
 ** You can also configure it application wide via below entry in
 ** <code>faces-config.xml</code> without the need to specify it in every single
 ** input component:
 ** <pre>
 **   &lt;converter&gt;
 **     &lt;converter-for-class&gt;java.lang.String&lt;/converter-for-class&gt;
 **     &lt;converter-class&gt;oracle.hst.platform.jsf.converter.ValueTrimmerConverter&lt;/converter-class&gt;
 **   &lt;/converter&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FacesConverter("faces.platform.Trimmer")
public class ValueTrimmerConverter implements Converter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ValueTrimmerConverter</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ValueTrimmerConverter() {
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
	public String getAsObject(final FacesContext context, final UIComponent component, final String submitted) {
    // prevent bogus input
		if (StringUtility.empty(submitted))
			return null;

		final String trimmed = submitted.trim();
		return StringUtility.empty(trimmed) ? null : trimmed;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAsString (Converter)
  /**
   ** Convert the specified model object value, which is associated with the
   ** specified {@link UIComponent}, into a string representation that is
   ** suitable for being included in the response generated during the
   ** <i>Render Response</i> phase of the request processing lifeycle.
   **
   ** @param  context            the {@link FacesContext} for the request being
   **                            processed.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** @param  component          the {@link UIComponent} with which this model
   **                            object value is associated.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   ** @param  value              the model object value to be converted.
   **                            <br>
   **                            May be <code>null</code>
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    zero-length String if value is
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
  public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
    return CollectionUtility.coalesce((String)value, "");
  }
}