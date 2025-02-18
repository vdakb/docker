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
    Subsystem   :   Vehicle Administration

    File        :   LicencePlateValidator_DE.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    LicencePlateValidator_DE.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-05-28   SBernet     First release version
*/

package bka.employee.portal.vehicle.vhu.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import oracle.hst.foundation.faces.ADF;

////////////////////////////////////////////////////////////////////////////////
// class LicencePlateValidator_DE
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implement validator methods to validate german licence plate records from
 ** UI.
 ** 
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LicencePlateValidator_DE implements Validator{

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final  String BUNDLE = "bka.employee.portal.vehicle.Frontend";
  
  public static final  String LICENCE_RX = "^[A-ZÄÖÜ]{1,3}\\-[ ]{0,1}[A-Z]{0,2}[0-9]{1,4}[H]{0,1}$";
  public static final  String FORBIDDEN_RX = "(.*?)-(HJ|KZ|NS|SA|SS|AH|HH|SD|IS)(.*?)";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LicencePlateValidator_DE</code> class that allow
   ** use as a Validator
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LicencePlateValidator_DE() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   * Perform the german licence plate checks against the specified UIComponent.
   * If any violations are found a {@link ValidatorException} will be thrown
   * containing the {@link FacesMessage} describing the failure. 
   * 
   * @param facesContext  FacesContext for the request we are processing
   * @param uIComponent   UIComponent we are checking for correctness
   * @param object        The value to validate 
   * 
   * @throws ValidatorException   Throws if validation failed
   */
  @Override
  public void validate(FacesContext facesContext, UIComponent uIComponent, Object object)
    throws ValidatorException {
    String licence = object.toString();
    //Check if licence match with a German vehicle licence plate
    //Otherwise check if licence contains fobidden symbol
    if (!licence.matches(LICENCE_RX)) {
      throw new ValidatorException(new FacesMessage(ADF.resourceBundleValue(BUNDLE, "LICENCE_ERROR_NOMATCH_GERMAN")));
    } else if(licence.matches(FORBIDDEN_RX)) {
      throw new ValidatorException(new FacesMessage(ADF.resourceBundleValue(BUNDLE, "LICENCE_ERROR_FORBIDDEN_SYMBOL")));
    }
  }
}
