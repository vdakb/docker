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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Self Service Portal
    Subsystem   :   Common Shared Components

    File        :   BooleanConverter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BooleanConverter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-03-03  DSteding    First release version
*/

package bka.employee.portal.converter;

import javax.faces.convert.Converter;

import javax.faces.context.FacesContext;

import javax.faces.component.UIComponent;

////////////////////////////////////////////////////////////////////////////////
// class BooleanConverter
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** {@link Converter} implementation for <code>java.lang.Boolean</code> (and
 ** boolean primitive) values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class BooleanConverter implements Converter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the standard converter id for this converter. */
  static final String CONVERTER_ID = "bka.employee.portal.Boolean";

  /**
   **The message identifier of the {@link javax.faces.application.FacesMessage}
   ** to be created if the conversion to <code>Boolean</code> fails.
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
  static final String BOOLEAN_ID = "javax.faces.converter.BooleanConverter.BOOLEAN";

  /**
   ** The message identifier of the {@link javax.faces.application.FacesMessage}
   ** to be created if the conversion of the <code>Boolean</code> value to
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
   ** Constructs a <code>BooleanConverter</code> backing bean that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public BooleanConverter() {
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
    return "true".equals(value) ? "1" : "0";
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
    return "1".equals(value) ? "true" : "false";
  }
}