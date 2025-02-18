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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   YNBooleanDefaultTrue.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    YNBooleanDefaultTrue.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.converter;

import javax.faces.context.FacesContext;

import javax.faces.component.UIComponent;

////////////////////////////////////////////////////////////////////////////////
// class YNBooleanDefaultTrue
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** {@link AbstractConverter} implementation for ...
 */
public class YNBooleanDefaultTrue extends AbstractConverter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The standard converter id for this converter. */
  public static final String CONVERTER_ID = "oracle.hst.foundation.faces.YNBooleanDefaultTrue";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>YNBooleanDefaultTrue</code> converter that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public YNBooleanDefaultTrue() {
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
   ** @param  context           the {@link FacesContext} for the request being
   **                           processed.
   ** @param  component         the {@link UIComponent} with which this model
   **                           object value is associated.
   ** @param  value             the {@link String} value to be converted (may be
   **                           <code>null</code>).
   **
   ** @return                   <code>null</code> if the value to convert is
   **                           <code>null</code>, otherwise the result of the
   **                           conversion.
   */
  @Override
  public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
    return value == null ? "Y" : "true".equals(value) ? "Y" : "N";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAsString (Converter)
  /**
   ** Convert the specified model object value, which is associated with the
   ** specified {@link UIComponent}, into a String that is suitable for being
   ** included in the response generated during the <i>Render Response</i> phase
   ** of the request processing lifeycle.
   **
   ** @param  context           the {@link FacesContext} for the request being
   **                           processed.
   ** @param  component         the {@link UIComponent} with which this model
   **                           object value is associated.
   ** @param  value             the Model object value to be converted (may be
   **                           <code>null</code>).
   **
   ** @return                   a zero-length String if value is
   **                           <code>null</code>, otherwise the result of the
   **                           conversion.
   */
  @Override
  public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
    return value == null ? "true" : "Y".equals(value) ? "true" : "false";
  }
}