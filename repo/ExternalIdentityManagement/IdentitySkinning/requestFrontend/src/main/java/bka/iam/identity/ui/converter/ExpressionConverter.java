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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   Special Account Request

    File        :   ExpressionConverter.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    ExpressionConverter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-03-03  SBernet    First release version
*/

package bka.iam.identity.ui.converter;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;

import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;

import javax.faces.component.UIComponent;

////////////////////////////////////////////////////////////////////////////////
// class ExpressionConverter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>ExpressionConverter</code> takes an expression and converts it to a
 ** String.
 ** 
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExpressionConverter implements Converter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the standard converter id for this converter. */
  static final String CONVERTER_ID = "bka.iam.identity.ui.converter.Expression";

  /**
   ** The message identifier of the {@link javax.faces.application.FacesMessage}
   ** to be created if the conversion to <code>String</code> fails.
   ** <p>
   ** The message format string for this message may optionally include the
   ** following placeholders:
   ** <ul>
   **   <li><code>%1s</code> replaced by the unconverted value.
   **   <li><code>%2s</code> replaced by a <code>String</code> whose value is
   **                        the label of the input component that produced this
   **                        message.
   **   <li><code>%3s</code> the localized message of the exception catched
   **                        during convert.
   ** </ul>
   */
  static final String LABEL_ID = "javax.faces.converter.LabelConverter.LABEL";

  /**
   ** The message identifier of the {@link javax.faces.application.FacesMessage}
   ** to be created if the conversion of the <code>String</code> value to
   ** <code>String</code> fails.
   ** <br>
   ** The message format string for this message may optionally include the
   ** following placeholders:
   ** <ul>
   **   <li><code>%1s</code> replaced by the unconverted value.
   **   <li><code>%2s</code> replaced by a <code>String</code> whose value is
   **                        the label of the input component that produced this
   **                        message.
   **   <li><code>%3s</code> the localized message of the exception catched
   **                        during convert.
   ** </ul>
   */
  static final String STRING_ID = "javax.faces.converter.STRING";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Label</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argumment constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExpressionConverter() {
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
   ** being stored during the Apply Request Values phase of the request
   ** processing lifecycle.
   **
   ** @param  context            the {@link FacesContext} for the request being
   **                            processed.
   ** @param  component          the {@link UIComponent} with which this model
   **                            object value is associated.
   ** @param  value              the  value to be converted
   **                            (may be <code>null</code>).
   **
   ** @return                    the result of the conversion.
   */
  @Override
  public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
    return getAsString(context, component, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAsString (Converter)
  /**
   ** Convert the specified model object value, which is associated with the
   ** specified {@link UIComponent}, into a String that is suitable for being
   ** included in the response generated during the Render Response phase of the
   ** request processing lifeycle.
   **
   ** @param  context            the {@link FacesContext} for the request being
   **                            processed.
   ** @param  component          the {@link UIComponent} with which this model
   **                            object value is associated.
   ** @param  value              the Model object value to be converted
   **                            (may be <code>null</code>).
   **
   ** @return                    the result of the conversion.
   */
  @Override
  public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
    final ELContext         resolver   = context.getELContext();
    final ExpressionFactory factory    = context.getApplication().getExpressionFactory();
    final ValueExpression   expression = factory.createValueExpression(resolver, (String)value, String.class);
    // make it fail safe that always a value is returned
    String result = value.toString();
    try {
      // evaluate the returning value
      result = (String)expression.getValue(resolver);
    }
    catch (Throwable t) {
      // in case there is something that should be solved
      t.printStackTrace();
    }
    return result;
  }
}