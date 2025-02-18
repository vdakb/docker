/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Unique Identifier Administration 

    File        :   Life.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Life.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.converter;

import java.io.Serializable;

import javax.inject.Inject;

import javax.faces.context.FacesContext;

import javax.faces.component.UIComponent;

import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.ConverterException;

import javax.faces.application.FacesMessage;

import bka.iam.identity.uid.state.Domain;

import bka.iam.identity.igs.model.Status;

////////////////////////////////////////////////////////////////////////////////
// class Life
// ~~~~~ ~~~~
/**
 ** The <code>Life</code> converts an {@link Integer} object to its string
 ** representation and vice versa to populate a selection component with complex
 ** Java model objects (entities).
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FacesConverter(value="statusConverter")
public class Life implements Converter
                  ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8795365892760250760")
  private static final long serialVersionUID = -6107695056803102870L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Inject
  private Domain            domain;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Life</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Life() {
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
   ** @param  value              the string representation of the value to be
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
  public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
    if (value != null && value.trim().length() > 0) {
      try {
        return this.domain.getStatusMapping().get(Integer.valueOf(value));
      }
      catch (NumberFormatException e) {
        throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid status."));
      }
    }
    else {
      return null;
    }
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
    return (value instanceof Integer) ? (value != null) ? String.valueOf(((Status)value).getState()) : null : null;
  }
}